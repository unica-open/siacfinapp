/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.math.BigDecimal;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestioneOrdinativoIncassoStep3Action extends ActionKeyGestioneOrdinativoIncassoAction{

	private static final long serialVersionUID = 1L;
	
	// usato per il seleziona provvisorio
	private String uidProvvDaRicerca;
	
	//@Autowired 
	//private transient PreDocumentoSpesaService preDocumentoSpesaService;
		
	public GestioneOrdinativoIncassoStep3Action () {
		//Specifichiamo la tipologia che puo essere pagamento o incasso,
		//nel nostro caso incasso:
		oggettoDaPopolare = OggettoDaPopolareEnum.ORDINATIVO_INCASSO;
	}

	
	@Override
	public void prepare() throws Exception {
		if(model!=null){
			//setto il titolo:
			this.model.setTitolo("Provvisori di cassa");
			
			//invoco il prepare della super classe:
			super.prepare();
		}
		caricaListeStep3();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		if(model==null){
			// significa che sono andato in inserimento -> consulta -> click su filo di arianna quote
			return "erroreFiloArianna";
		}
		
		//1. come prima cosa carichiamo i totali visualizzati nel footer: 
		calcolaTotaliFooter();
		
		//2. costruiamo la label:
		caricaLabelsInserisci(3, model.getGestioneOrdinativoStep1Model().getOrdinativo().getUid() == 0);
		
		//3. Variabile che mi indica lo stato pulito:
		setClearStatus(true);
		
		//4. precarico i valori di default della ricerca:
		model.getRicercaProvvisorioModel().setAnnoProvvisorioDa(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		model.getRicercaProvvisorioModel().setAnnoProvvisorioA(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		
		//OK
		return SUCCESS;
	}
	
	/**
	 * Metodo che parte all'evento di selezione di un provvisorio di cassa
	 * @return
	 */
	public String selezionaProvvisorio(){
		debug("selezionaProvvisorio", "seleziono il "+getUidProvvDaRicerca());
		//richiamo il metodo nella super classe:
		return super.selezionaProvvisorio(getUidProvvDaRicerca()); 
	}
	
	/**
	 * Metodo che parte all'evento di salvataggio dei provvisori
	 * @return
	 */
	public String salvaProvvisori(){
		// click su salva provvisori
		return super.salvaProvvisoriOrdinativoIncasso();
	}
	
	/**
	 * evento che annulla l'inserimento
	 * @return
	 */
	public String pulisciProvvisorio(){
		//Pulisce il dato
		model.getGestioneOrdinativoStep3Model().setProvvisorio(new ProvvisorioDiCassa());
		return INPUT;
	}
	
	public String aggiornaCopertura(){
		
		if(controlloGestioneProvvisorio(OPERAZIONE_MODIFICA).equalsIgnoreCase(INPUT)){
			setAncoraVisualizzaInserisciProvErrori(true);
			setAncoraVisualizzaInserisciProv(false);
			setAncoraVisualizza(false);
			return INPUT;
		}

		
		setUidProvvDaRicerca("");
		
		return SUCCESS;
		
	}
	
	
	
	public boolean disabilitaInserisciProvvisorio(){
		boolean ritorno = true;
		
		if(sonoInAggiornamentoIncasso()){
			    // in caso di aggiorna  
			
			    if(model.getGestioneOrdinativoStep3Model().getTotalizzatoreDaCollegare()==null || model.getGestioneOrdinativoStep3Model().getTotalizzatoreDaCollegare().compareTo(BigDecimal.ZERO)==0){
			    	// se total collegare e' uguale a zero non devo poter inserire
			    	ritorno = true;
			    }else{
			    	
			    	ritorno = false;
			    }
			
		}else{
		     // in caso di inserimento
			if(model.getGestioneOrdinativoStep3Model().getProvvisorio()!=null){
				if(model.getGestioneOrdinativoStep3Model().getProvvisorio().getImportoDaRegolarizzare()!=null && model.getGestioneOrdinativoStep3Model().getProvvisorio().getImportoDaRegolarizzare().compareTo(BigDecimal.ZERO)>0) {
					ritorno =  false;		
				}	
				
			}else ritorno = true;
		}
		return ritorno;
		
	}
	
	@Override
	public String aggiornaOrdinativoIncasso() {
	
		if(null!=model.getGestioneOrdinativoStep3Model().getTotalizzatoreDaCollegare() && model.getGestioneOrdinativoStep3Model().getTotalizzatoreDaCollegare().compareTo(BigDecimal.ZERO)>0){
		
			addErrore(ErroreFin.TOTALE_PROVVISORI_COLLEGATI_NON_COINCIDE_CON_IMPORTO_ORDINATIVO_SUB.getErrore(""));
			return INPUT;
		}
		return super.aggiornaOrdinativoIncasso();
	}
	
	@Override
	public String dettaglioAggiornaCoperture(){
		
		return super.dettaglioAggiornaCoperture();
	}

	
	
	public Boolean impostaValoreRiportaInTestataPageAggiornaProvvisori(Boolean valore){
		
		if(valore)
			return false;
		else
			return true;
	}

	
	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */
	public String getUidProvvDaRicerca() {
		return uidProvvDaRicerca;
	}

	public void setUidProvvDaRicerca(String uidProvvDaRicerca) {
		this.uidProvvDaRicerca = uidProvvDaRicerca;
	}
	
	/**
	 * Controlla se il metodo si sia concluso senza alcun errore. In caso contrario, rilancia un'eccezione per uscire dalla pagina.
	 * @throws GenericFrontEndMessagesException in caso di errori nei servizii
	 */
	/*
	protected void checkMetodoConclusoSenzaErrori() {
		// Se ho errori
		if(hasErrori()) {
			StringBuilder erroriRiscontrati = new StringBuilder();
			// Per ogni errore, aggiungo il testo
			for(Errore errore : model.getErrori()) {
				erroriRiscontrati.append(errore.getTesto() + "\n");
			}
			// Lancio l'eccezione
			throw new GenericFrontEndMessagesException(ErroreCore.ERRORE_DI_SISTEMA.getErrore(erroriRiscontrati.toString()).getTesto(),
					GenericFrontEndMessagesException.Level.ERROR);
		}
	}
	*/
	/**
	 * Carica la lista del Conto Tesoreria.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio fallisca
	 */
	/*
	protected void caricaListaContoTesoreria() throws WebServiceInvocationFailureException {
		List<ContoTesoreria> listaInSessione = sessionHandler.getParametro(FinSessionParameter.LISTA_CONTO_TESORERIA);
		if(listaInSessione == null) {
			LeggiContiTesoreria request = model.creaRequestLeggiContiTesoreria();
			logServiceRequest(request);
			LeggiContiTesoreriaResponse response = preDocumentoSpesaService.leggiContiTesoreria(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException("caricaListaContoTesoreria");
			}
			
			listaInSessione = response.getContiTesoreria();
			sessionHandler.setParametro(FinSessionParameter.LISTA_CONTO_TESORERIA, listaInSessione);
		}
		
		model.setListaContoTesoreria(listaInSessione);
	}
	*/
}
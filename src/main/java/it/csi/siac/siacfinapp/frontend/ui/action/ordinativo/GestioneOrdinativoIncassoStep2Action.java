/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentiSubAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentiSubAccertamentiResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaAccSubAcc;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestioneOrdinativoIncassoStep2Action extends ActionKeyGestioneOrdinativoIncassoAction{
	
	
	//
	//  INCASSO
	// 
	
	private static final long serialVersionUID = 1L;

	public GestioneOrdinativoIncassoStep2Action () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.ORDINATIVO_INCASSO;
	}
	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		this.model.setTitolo("Quote ordinativo");
		
   }
	
	
	/**
	 * Attiva il pulsante di aggiorna quota solo se l'ordinativo non e' stato emesso 
	 * 
	 * @return
	 * @throws Exception
	 */
   public boolean editaImportoQuota() throws Exception {
    	
	   //vado come prima cosa a leggermi il numero della quota in aggiornamento:
	   String numeroQuotaInAggiornamento = model.getGestioneOrdinativoStep2Model().getDettaglioQuotaOrdinativoModel().getNumeroQuota();
	   Integer numeroQuotaInAgg = FinUtility.parseNumeroIntero(numeroQuotaInAggiornamento);
	   
       boolean editaImportoQuota = true;
    	
       if(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso()!=null &&
    			!model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso().isEmpty() &&  model.isSonoInAggiornamentoIncasso()){
    	   
    	    //vado quindi ad iterare la lista di sub ordinativi di incasso (le quote)
    	    //cercando quella in aggiornamento:
   			List<SubOrdinativoIncasso> elencoSubOrdinativiDiIncasso = model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso();
	    	for (SubOrdinativoIncasso subOrdinativoIncasso: elencoSubOrdinativiDiIncasso) {
				
	    		if(numeroQuotaInAgg!=null && subOrdinativoIncasso.getNumero()!=null
	    				&& numeroQuotaInAgg.intValue()==subOrdinativoIncasso.getNumero().intValue()){
	    			
	    			//ho trovato la quota in questione
	    			
	    			if(subOrdinativoIncasso.getSubDocumentoEntrata()!=null){
	    				//essendoci un sub documento collegato la quota risulta non editabile
		    			editaImportoQuota = false;
		    			break;
		    		} else {
		    			
		    			//NON COLLEGATO A DOCUMENTO
		    			
		    			//FIX per SIAC-4842 Se l'Ordinativo e' in stato I si dovrebbe poter aggiornare l'Importo della quota
		    	    	// nel caso in cui l'Ordinativo non sia collegato ad un documento. 
		    	    	if(model.getGestioneOrdinativoStep1Model()!=null && 
		    	    			model.getGestioneOrdinativoStep1Model().getOrdinativo()!=null &&
		    	    			StatoOperativoOrdinativo.INSERITO.equals(model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo())){
		    	    		//la quota risulta editabile:
		    	    		editaImportoQuota = true;
		    	    	}
		    	    	//
		    		}
	    			
	    		}
	    		
			}

       }
    	
       //ritorno l'esito dell'analisi:
       return editaImportoQuota;
    }
	
	/**
	 * Per pilotare l'attivazione del pulsante prosegui
	 * @return
	 */
	public boolean attivaPulsanteProsegui(){
		if(!model.isSonoInAggiornamentoIncasso()){
			if(null!=model.getGestioneOrdinativoStep1Model().getOrdinativo() && model.getGestioneOrdinativoStep1Model().getOrdinativo().isFlagCopertura() && model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso()!= null && model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso().size()>0){
			   return true;	
			}else{
				return false;
			}
		}else{
			return false;
		}
		
	}
	
	@Override
	public String dettaglioAggiornaQuota(){
		//rimando al metodo nella super classe:
	    return super.dettaglioAggiornaQuota();
	}
	
	@Override
	public String dettaglioConsultaQuota(){
		//rimando al metodo nella super classe:
	    return super.dettaglioConsultaQuota();
	}

	@Override
	public String aggiornaQuotaOrdinativo()  throws Exception{
		if(presenzaPaginazione(ServletActionContext.getRequest())){
			return execute();
		}
		//rimando al metodo nella super classe:
		return super.aggiornaQuotaOrdinativo();
		
	}
	
	@Override
	public String forzaInserisciQuotaAccertamento() throws Exception{
		if(presenzaPaginazione(ServletActionContext.getRequest())){
			return execute();
		}
		//rimando al metodo nella super classe:
		return super.forzaInserisciQuotaAccertamento();
	}
	
	/**
	 * Metodo che gestisce l'inserimento di una quota
	 */
    @Override
	public String inserisciQuota() throws Exception {
		
    	if(StringUtils.isEmpty(model.getGestioneOrdinativoStep2Model().getRadioIdAccertamento())){
    		//non e' stato selezionato l'accertamento
    		addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Selezionare accertamento/subAccertamento"));
    		return INPUT;
    	}

		if(presenzaPaginazione(ServletActionContext.getRequest())){
			return execute();
		}
		
    	if(!isSoggettoCoerenteConClasse()){
    		return INPUT;
    	}
    	
    	//rimando al metodo della super classe:
    	String ritorno = super.inserisciQuotaAccertamento();
    	
    	// aggiorno la sommatoria delle quote
    	sommatoriaQuoteSubOrdIncasso();
		
    	return ritorno; 
	}
    
    /**
     * Metodo che si occupa della cancellazione di una quota
     * @return
     * @throws Exception
     */
    public String eliminaQuotaOrdinativo()  throws Exception{
		if(presenzaPaginazione(ServletActionContext.getRequest())){
			return execute();
		}
		//rimando al metodo nella super classe:
    	return super.eliminaQuotaOrdinativo(false);
    }
    
    /**
     * Per verficare se e' sub
     * @param numeroAccertamentoPadre
     * @return
     */
    public boolean isSubAccertamento(BigDecimal numeroAccertamentoPadre){
    	if(null==numeroAccertamentoPadre){
    		return false;
    	}
    	else return true;
    	
    }
    
    /**
     * Se e' un'istanza di Sub restituisce false
     * @param accertamento
     * @return
     */
    public boolean rigaAccertamento(Accertamento accertamento){
    	boolean tipoAccertamento = true;
    	if(accertamento instanceof SubAccertamento){
    		//e' un sub
    		tipoAccertamento = false;
    	}
    	return tipoAccertamento;
    }
    
    /**
     * Metodo execute della action
     */
    @Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
    	
    	//eseguo la ricerca:
    	executeRicercaAccertamentoPerOrdinativo();

		//carica le labels:
		caricaLabelsInserisci(2, model.getGestioneOrdinativoStep1Model().getOrdinativo().getUid() == 0);
		
		//controllo filo arianna:
		if(model.getGestioneOrdinativoStep1Model().getCapitolo()!=null){
			if(model.getGestioneOrdinativoStep1Model().getCapitolo().getAnno()==null){
				// significa che sono andato in inserimento -> consulta -> click su filo di arianna quote
				return "erroreFiloArianna";
			}
		}
		
		//setto il tipo di oggeto trattato:
		teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.ORDINATIVO_INCASSO.toString());
		
		//abilita o meno il bottone salva:
		attivaBottoneSalva();
		
		// effettua la somma delle righe delle quote
		if(model.isSonoInAggiornamentoIncasso()){
			sommatoriaQuoteSubOrdIncassoPerAggiorna();
		}else{
			sommatoriaQuoteSubOrdIncasso();
		}
		
		if (caricaListeBil(WebAppConstants.CAP_EG)) {
			return INPUT;
		}
	
		//Constanti.ORDINATIVO_TIPO_PAGAMENTO
		// Jira - 1357 in caso di errore di caricamento dei dati
		// dei classificatori non viene segnalato alcun errore
		// ma carica la pagina, al massimo non verranno visualizzate le combo relative
		
		caricaListeFin(TIPO_ACCERTAMENTO_A );
		
		//ricontrolliamo il siope che sia coerente:
		codiceSiopeChangedInternal(teSupport.getSiopeSpesaCod());
		//
			
		// imposto la descrizione della quota ocn quella dell'accertamento 
		String descrizioneQuota = "";
		if(model.getGestioneOrdinativoStep2Model().getListaAccertamento()!=null && !model.getGestioneOrdinativoStep2Model().getListaAccertamento().isEmpty() &&
				model.getGestioneOrdinativoStep2Model().getListaAccertamento().size()==1){
			descrizioneQuota = model.getGestioneOrdinativoStep2Model().getListaAccertamento().get(0).getDescrizione();
			model.getGestioneOrdinativoStep2Model().setDescrizioneQuota(descrizioneQuota);
			
		}
		
		return SUCCESS;
	}

    private void executeRicercaAccertamentoPerOrdinativo() {

    	// qui dovrebbe entrare solo se dallo step1 non e' stata eseguito il 'Cerca Accertamento'
    	if (model.getGestioneOrdinativoStep1Model().getAnnoAccertamento() == null || model.getGestioneOrdinativoStep1Model().getNumeroAccertamento() == null) {

    		if(!sonoInAggiornamento()){
    			
    			model.getGestioneOrdinativoStep2Model().setListaAccertamento(new ArrayList<Accertamento>());
    			model.getGestioneOrdinativoStep2Model().setListaAccertamentoOriginale(new ArrayList<Accertamento>());
    			model.getGestioneOrdinativoStep2Model().setResultSize(0);

    			RicercaAccertamentiSubAccertamenti request= new RicercaAccertamentiSubAccertamenti();
    			request.setEnte(sessionHandler.getEnte());
    			request.setRichiedente(sessionHandler.getRichiedente());


    			ParametroRicercaAccSubAcc param= new ParametroRicercaAccSubAcc();
    			param.setAnnoEsercizio(Integer.parseInt(sessionHandler.getAnnoEsercizio()));
    			param.setAnnoAccertamento(Integer.parseInt(sessionHandler.getAnnoEsercizio()));
    			param.setDisponibilitaAdIncassare(true);

    			// Capitolo
    			if (model.getGestioneOrdinativoStep1Model().getCapitolo() != null) {
    				param.setNumeroCapitolo(model.getGestioneOrdinativoStep1Model().getCapitolo().getNumCapitolo());
    				param.setNumeroArticolo(model.getGestioneOrdinativoStep1Model().getCapitolo().getArticolo());
    				if(null!=model.getGestioneOrdinativoStep1Model().getCapitolo().getUeb()){
    					param.setNumeroUEB(model.getGestioneOrdinativoStep1Model().getCapitolo().getUeb().intValue());
    				}
    			}


    			param.setCodiceDebitore(model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore());
    			param.setIsRicercaDaAccertamento(false);
    			request.setParametroRicercaAccSubAcc(param);


    			addNumAndPageSize(request, "listaAccertamentoOrdinativoId");

    			RicercaAccertamentiSubAccertamentiResponse response= movimentoGestionService.ricercaAccertamentiSubAccertamentiPerOrdinativoIncasso(request);

    			if(response.isFallimento() || !response.getErrori().isEmpty()){

    				//setto gli errori cosi che siano segnalati lato app!
    				addErrori(response.getErrori());

    			}else{

    				if (response.getListaAccertamenti() != null && response.getListaAccertamenti().size() > 0) {
    					for (Accertamento currentAccetamento : response.getListaAccertamenti()) {
    						addAccertamentiESubAccertamentiInUnicaLista(currentAccetamento);
    					}

    					model.getGestioneOrdinativoStep2Model().setResultSize(response.getNumRisultati());
    					
    					if(model.getGestioneOrdinativoStep2Model().getListaAccertamento()==null || model.getGestioneOrdinativoStep2Model().getListaAccertamento().size()==0){
    						addPersistentActionWarning(ErroreFin.CRU_WAR_1003.getErrore().getCodice()+" : "+ErroreFin.CRU_WAR_1003.getErrore(" accertamenti o subaccertamenti").getDescrizione());
    					}
    				} else {
    					addPersistentActionWarning(ErroreFin.CRU_WAR_1003.getErrore().getCodice()+" : "+ErroreFin.CRU_WAR_1003.getErrore(" accertamenti o subaccertamenti").getDescrizione());
    				}
    			}

    		}
    	}
    }
    
    private void addAccertamentiESubAccertamentiInUnicaLista(Accertamento accertamento) {

    	List<Accertamento> listaAccDaPresentare = new ArrayList<Accertamento>();

		String codiceSoggettoOrdinativo = model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore();

    	// se non presente sub allora cerco in accertamenti
    	if(accertamento.getElencoSubAccertamenti()==null || accertamento.getElencoSubAccertamenti().isEmpty()){

    		// aggiungo accertamenti associati al soggetto
    		listaAccDaPresentare.add(accertamento);		

    	}

    	// verifico subaccertamenti collegati
    	else{

    		for (SubAccertamento itSubAcc : accertamento.getElencoSubAccertamenti()) {

				if(itSubAcc.getSoggetto()!= null && codiceSoggettoOrdinativo.equals(itSubAcc.getSoggetto().getCodiceSoggetto())){

					listaAccDaPresentare.add(itSubAcc);

				}

    		}
    	}

    	if(!listaAccDaPresentare.isEmpty()){

    		// aggiungo tutti gli accertamenti e i sub-accertamenti trovati
    		model.getGestioneOrdinativoStep2Model().getListaAccertamento().addAll(listaAccDaPresentare);
    	}
    }
    
    /**
	 *  abilito il btn nuova liquidazione se presente l'azione
	 *  OP-SPE-insLiqMan
	 */
	public boolean abilitaNuovoAccertamento(){
		
		if(isAzioneAbilitata(CodiciOperazioni.OP_ENT_insAcc)){
			return true;
		}
		
		return false;
	}
	/**
	 * click su prosegui
	 * @return
	 */
	public String prosegui(){
		// deve esserci la quota
		if(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso() == null || model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiIncasso().size() == 0){
			addErrore(ErroreFin.ORDINATIVO_MANCANTE_QUOTE.getErrore("inserimento","ordinativo di incasso"));
			return INPUT;
		}
		return "prosegui";
	}
	
	
	
	/**
	 * GESTORE TRANSAZIONE ECONOMICA
	 */
	@Override
	public boolean missioneProgrammaAttivi() {
		return false;
	}

	@Override
	public boolean cofogAttivo() {
		return false;
	}

	@Override
	public boolean cupAttivo() {
		return false;
	}
	
	@Override
	public boolean programmaPoliticoRegionaleUnitarioAttivo() {
		return false;
	}

	@Override
	public boolean transazioneElementareAttiva() {
		// TE
		return true;
	}
	
	@Override
	public boolean altriClassificatoriAttivi() {
		return false;
	}

	
	@Override
	public boolean datiUscitaImpegno() {
		// dentro ordinativo di pagamento sono sempre oggetti di tipo impegno
		return false;
	}
	
	

	private boolean isSoggettoCoerenteConClasse(){

		boolean esito = false;

		List<Accertamento> listaAccertamento = model.getGestioneOrdinativoStep2Model().getListaAccertamento();

		String idAccertamentoSelezionato = model.getGestioneOrdinativoStep2Model().getRadioIdAccertamento();

		Accertamento AccertamentoScelto = null;


		if(!StringUtils.isEmpty(idAccertamentoSelezionato)){

			for (Accertamento accertamento : listaAccertamento) {

				if(accertamento.getUid()== Integer.valueOf(idAccertamentoSelezionato)){

					AccertamentoScelto = accertamento;

					break;
				}

			}


			if (!listaAccertamento.isEmpty() && AccertamentoScelto.getClasseSoggetto() != null){

				Soggetto s = model.getGestioneOrdinativoStep1Model().getSoggettoDettaglioPerClasse();

				if(s.getElencoClass()!=null && s.getElencoClass().size()>0){

					List<ClasseSoggetto> listaClassificazioni = new ArrayList<ClasseSoggetto>();

					for(int i=0; i<s.getElencoClass().size(); i++){

						ClasseSoggetto cls = new ClasseSoggetto(); 
						cls.setCodice(s.getElencoClass().get(i).getSoggettoClasseCode());
						cls.setDescrizione(s.getElencoClass().get(i).getSoggettoClasseDesc());
						listaClassificazioni.add(cls);

					}					

					if(listaClassificazioni!=null && listaClassificazioni.size()>0){

						for(int i=0; i<listaClassificazioni.size(); i++){

							if(listaClassificazioni.get(i).getCodice().equals(AccertamentoScelto.getClasseSoggetto().getCodice())){

								esito = true;
								break;
							}
							
							
						}
						
						if(!esito){

							if(!model.isProseguiConWarning()){								

								addPersistentActionWarning(ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore().getCodice() + " : " + ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore().getDescrizione());

								model.setProseguiConWarning(true);
							}
							else{
								
								esito = true;
							}

						}
						
					}
				}
				else{
					
					if(!model.isProseguiConWarning()){	
						
						addPersistentActionWarning(ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore().getCodice() + " : " + ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore().getDescrizione());

						model.setProseguiConWarning(true);
					}
					else{
						esito = true;
					}
					
				}

			}
			else{
				
				esito = true;
			}

		}
		
		return esito;
	}


}

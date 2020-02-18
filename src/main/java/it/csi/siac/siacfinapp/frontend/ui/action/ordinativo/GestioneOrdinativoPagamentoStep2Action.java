/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;



@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestioneOrdinativoPagamentoStep2Action extends ActionKeyGestioneOrdinativoPagamentoAction {

	private static final long serialVersionUID = 1L;

	public GestioneOrdinativoPagamentoStep2Action () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO;
	}
	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		
		//lista motivazioni assenza cig:
		caricaListaMotivazioniAssenzaCig();
		
		//tipo debito siope:
	    model.getGestioneOrdinativoStep2Model().setScelteTipoDebitoSiope(buildListaTipoDebitoSiope());
		
		//setto il titolo:
		this.model.setTitolo("Quote ordinativo");
	}
	
	@Override
	public String dettaglioAggiornaQuota(){
		
	      return super.dettaglioAggiornaQuota();
	}
	
	@Override
	public String aggiornaOrdinativo(){
		// richiamo su
		return super.aggiornaOrdinativo();
	}

	@Override
	public String aggiornaQuotaOrdinativo() throws Exception{
		
		if(presenzaPaginazione(ServletActionContext.getRequest())){
			return execute();
		}
		
		return super.aggiornaQuotaOrdinativo();
		
	}
	
    @Override
	public String inserisciQuota() throws Exception {
		
		if(presenzaPaginazione(ServletActionContext.getRequest())){
			return execute();
		}
    	
    	String ritorno = super.inserisciQuota();
    	// aggiorna la sommatoria delle quote
    	sommatoriaQuoteSubOrdPagamento();
		
    	return ritorno; 
	}
    
    public String eliminaQuotaOrdinativo() throws Exception {
    	
		if(presenzaPaginazione(ServletActionContext.getRequest())){
			return execute();
		}
    	// click su elimina quota
    	return super.eliminaQuotaOrdinativo(false);
    }
	
    
    public boolean editaImportoQuota() throws Exception {
    	
    	boolean editaImportoQuota = true;

    	if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getElencoSubOrdinativiDiPagamento()!=null &&
    			!model.getGestioneOrdinativoStep1Model().getOrdinativo().getElencoSubOrdinativiDiPagamento().isEmpty() &&  model.isSonoInAggiornamento()){
    	
	    	List<SubOrdinativoPagamento> elencoSubOrdinativiDiPagamento = model.getGestioneOrdinativoStep1Model().getOrdinativo().getElencoSubOrdinativiDiPagamento();
	    	for (SubOrdinativoPagamento subOrdinativoPagamento: elencoSubOrdinativiDiPagamento) {
				
	    		if(subOrdinativoPagamento.getSubDocumentoSpesa()!=null){
	    			editaImportoQuota = false;
	    			break;
	    		}
	    		
			}
    	}
    	
    	return editaImportoQuota;
    }
    
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		caricaLabelsInserisci(2, model.getGestioneOrdinativoStep1Model().getOrdinativo().getUid() == 0);
		
		// levo eventuali valori memorizzati da inserisci nuova liquidazione light
		model.setImpegnoPopUpNuovaLiquidazioneOrdinativo(null);
		
		if(model.getGestioneOrdinativoStep1Model().getCapitolo()!=null){
			if(model.getGestioneOrdinativoStep1Model().getCapitolo().getAnno()==null){
				
				// significa che sono andato in inserimento -> consulta -> click su filo di arianna quote
				return "erroreFiloArianna";
			}
		}
		
		teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO.toString());
		
		if(!model.isSonoInAggiornamento()){
			teSupport.setRicaricaAlberoPianoDeiConti(true);
		}
		
		
		attivaBottoneSalva();
		// effettua la somma delle righe delle quote
		if(model.isSonoInAggiornamento()){
			sommatoriaQuoteSubOrdPagamentoPerAggiorna();
		}else{
			sommatoriaQuoteSubOrdPagamento();
		}
		
		
		if (caricaListeBil(WebAppConstants.CAP_UG)) {
			return INPUT;
		}
	
		//ORDINATIVO_TIPO_PAGAMENTO
		// Jira - 1357 in caso di errore di caricamento dei dati
		// dei classificatori non viene segnalato alcun errore
		// ma carica la pagina, al massimo non verranno visualizzate le combo relative
		caricaListeFinOrdinativo(TIPO_ORDINATIVO_PAGAMENTO_P);
			
		return SUCCESS;
	}

	public boolean attivaPulsanteSalva(){
		
		boolean visualizzaSalvaBtn = true;
		
		// se il isFlagCopertura devo visualizzare il prosegui
		if(model.getGestioneOrdinativoStep1Model().getOrdinativo().isFlagCopertura()){
			// attivo il btn prosegui
			visualizzaSalvaBtn = false;
			
		}else if(sonoInAggiornamento()){
			
			visualizzaSalvaBtn = false;	 
			
		}else{
			
			// possibile bacarozzo
			if(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti()!=null && model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti().size()>0){
				visualizzaSalvaBtn = true;  // attivo il btn salva
			}else{
				visualizzaSalvaBtn = false; 
			}
			
		}
		
			
		return visualizzaSalvaBtn;
	}
	
	// true se sono presenti delle quote
	public boolean presenzaQuote(){

        boolean presentiQuote = false;
        if(model.isSonoInAggiornamento()){
        	if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo().equals(StatoOperativoOrdinativo.TRASMESSO) && isAzioneAbilitata(CodiciOperazioni.OP_SPE_varMan)){
        		presentiQuote= false;
        	}else if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo().equals(StatoOperativoOrdinativo.INSERITO) && isAzioneAbilitata(CodiciOperazioni.OP_SPE_aggMan)){
				if(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti()!=null && model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti().size()>0 && model.getGestioneOrdinativoStep1Model().getOrdinativo().isFlagCopertura()){
					presentiQuote = true;
				}
        	}
        }else{
        	
        	
        	if(model.getGestioneOrdinativoStep1Model().getOrdinativo().isFlagCopertura()){
        		if(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti()!=null && model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti().size()>0){
        			presentiQuote= true;
        		}
        	}
        }
		return presentiQuote;
	}
	
	
	/**
	 *  abilito il btn nuova liquidazione se presente l'azione
	 *  OP-SPE-insLiqMan
	 */
	public boolean abilitaNuovaLiquidazione(){
		// se e' presente l'operazione OP_SPE_insLiqMan 
		// allora puo inserire nuova liquidazione
		return isAzioneAbilitata(CodiciOperazioni.OP_SPE_insLiqMan);
	}
	
	
    public String rigaImpegno(Liquidazione liquidazione){
    	
    	String eUnImpegno = "impegno";
    	
    	if(liquidazione.getSubImpegno()!= null){
	    	if(!FinStringUtils.isEmpty(liquidazione.getSubImpegno().getNumero().toString())){
	    		eUnImpegno= "subImpegno";
	    	}
    	}else if(liquidazione.getImpegno().getElencoSubImpegni()!= null && liquidazione.getImpegno().getElencoSubImpegni().get(0)!= null){
    		if(!FinStringUtils.isEmpty(liquidazione.getImpegno().getElencoSubImpegni().get(0).getNumero().toString())){
    			eUnImpegno= "elencoSub";
    		}
    	}
    	
    	return eUnImpegno;
    }
	

	/**
	 * GESTORE TRANSAZIONE ECONOMICA
	 */
	@Override
	public boolean missioneProgrammaAttivi() {
		return true;
	}

	@Override
	public boolean cofogAttivo() {
		return true;
	}

	@Override
	public boolean cupAttivo() {
		return true;
	}
	
	@Override
	public boolean programmaPoliticoRegionaleUnitarioAttivo() {
		return true;
	}

	@Override
	public boolean transazioneElementareAttiva() {
		return true;
	}
	
	@Override
	public boolean altriClassificatoriAttivi() {
		return false;
	}
	
	
	@Override
	public boolean datiUscitaImpegno() {
		// dentro ordinativo di pagamento sono sempre oggetti di tipo impegno
		return true;
	}
}
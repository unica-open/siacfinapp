/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.mutuo;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinapp.frontend.ui.action.GenericPopupAction;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.mutuo.RicercaMutuoModel;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaMutuoResponse;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaMutuo;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class WizardRicercaMutuoAction extends GenericPopupAction<RicercaMutuoModel> {

	private static final long serialVersionUID = 1L;
	
	protected final static String GOTO_ELENCO_MUTUI = "gotoElencoMutui";
	protected final static String CU_OP_MUT_LEGGI_MUTUO = CodiciOperazioni.OP_MUT_leggiMutuo;
	protected final static String CU_OP_MUT_GESTISCI_MUTUO = CodiciOperazioni.OP_MUT_gestisciMutuo;
	protected final static String GOTO_ERRORE_RICERCA = "gotoErroreRicerca";
	protected String codiceMutuo;

	@Override
	public String getActionKey() {
		return "ricercaMutuo";
	}
	
	/**
	 * esegue la ricerca del mutuo
	 * @return
	 */
	protected String executeRicerca(){
	 
		//Compongo la request per il servizio:
		RicercaMutuo requestRicerca = convertiPerChiamataRicercaMutuo();
		
		//Richiamo il servizio:
	    RicercaMutuoResponse response = mutuoService.ricercaMutuo(requestRicerca);
	    
	    //Analizzo la risposta ottenuta dal servizio:
	    if(response.isFallimento()) {
	    	//ci sono errori
			addErrori("executeRicerca", response);
			return INPUT;
		}
	    //tutto ok
	    //setto i dati ottenuti nel model:
	    model.setElencoMutui(response.getElencoMutui());
	    model.setResultSize(response.getNumRisultati());
	    
	    //rimando ad elenco mutui:
	    return GOTO_ELENCO_MUTUI;
	}
	
	// pulisce dati form di ricerca
	protected void pulisceDatiRicerca(){
		model.setNumeroMutuo("");
		model.setNumeroRegistrazione(null);
		model.setProvvedimento(new ProvvedimentoImpegnoModel());
		model.setProvvedimentoSelezionato(false);
		model.setSoggetto(new SoggettoImpegnoModel());
		model.setSoggettoSelezionato(false);
	}
	
	/**
	 * verfico che se e' stata lanciata una ricerca precedente
	 * essa sia NON vuota (senza parametri)
	 * @param prm
	 * @return
	 */
	private boolean ricercaNonVuota(ParametroRicercaMutuo prm){
		
		boolean ricercaNonVuota=true;
		
		if(prm!=null && prm.getNumeroMutuo()!=null){
			//numero mutuo valorizzato
			return ricercaNonVuota;
		}
		
		if(prm!=null && prm.getNumeroRegistrazioneMutuo()!=null){
			//numero registrazione mutuo valorizzato
			return ricercaNonVuota;
		}
		
		if(prm!=null && prm.getSoggetto()!=null && !prm.getSoggetto().getCodiceSoggetto().isEmpty()){
			//codice soggetto valorizzato
			return ricercaNonVuota;
		}
		
		if(prm!=null && prm.getAttoAmministrativo()!=null && prm.getAttoAmministrativo().getNumero()!=0 && prm.getAttoAmministrativo().getAnno()!=0){
			//numero atto amministrativo valorizzato
			return ricercaNonVuota;
		}
		
		return false;
	}
	/**
	 * converter utilizzato per la chiamata del servizio
	 * di ricerca mutuo
	 * @return
	 */
	protected RicercaMutuo convertiPerChiamataRicercaMutuo(){
		
		  //istanzio la request:
		  RicercaMutuo rm = new RicercaMutuo();
		  
		  rm.setEnte(sessionHandler.getEnte());
		  rm.setRichiedente(sessionHandler.getRichiedente());
		  
		  ParametroRicercaMutuo prm = null;
		  
		  prm = (ParametroRicercaMutuo)sessionHandler.getParametro(FinSessionParameter.PAR_RICERCA_MUTUO);
		  
		  if(!ricercaNonVuota(prm)){
		      
			  prm = new ParametroRicercaMutuo();
			  
			  //numero mutuo
			  try{
				  if(null!=model.getNumeroMutuo() && StringUtils.isNotEmpty(model.getNumeroMutuo().trim())){
					  prm.setNumeroMutuo(model.getNumeroMutuo());
					  model.setPremutoPaginazione(1);
				  }else if(getCodiceMutuo()!= null && StringUtils.isNotEmpty(getCodiceMutuo())){
					  	  prm.setNumeroMutuo(getCodiceMutuo());
					  	  model.setPremutoPaginazione(1);
				  }
			  }catch(Exception e){
				  prm.setNumeroMutuo("");
			  }
			  
			  //numero registrazione
			  if(null!=model.getNumeroRegistrazione()){
				  prm.setNumeroRegistrazioneMutuo(model.getNumeroRegistrazione());
			  }
			  
			  // soggetto
			  Soggetto soggetto = new Soggetto();
			  soggetto.setCodiceSoggetto(model.getSoggetto().getCodCreditore());
			  prm.setSoggetto(soggetto);
			  
			  // provvedimento
			  AttoAmministrativo atto = new AttoAmministrativo();
			  
			  //anno provvedimento
			  if(null!=model.getProvvedimento().getAnnoProvvedimento()){
				  atto.setAnno(model.getProvvedimento().getAnnoProvvedimento());
			  }
			  
			  //numero provvedimento
			  if(null!=model.getProvvedimento().getNumeroProvvedimento()){
				  atto.setNumero(model.getProvvedimento().getNumeroProvvedimento().intValue());
			  }
			  
			  //tipo atto
			  TipoAtto tipoAtto = new TipoAtto();
			  tipoAtto.setCodice(String.valueOf(model.getProvvedimento().getIdTipoProvvedimento()));
			  atto.setTipoAtto(tipoAtto);
			  
			  //Struttura amministrativa selezionata
			  if(model.getProvvedimento().getUidStrutturaAmm()!=null){
				StrutturaAmministrativoContabile sac = new StrutturaAmministrativoContabile();
				sac.setUid(model.getProvvedimento().getUidStrutturaAmm());
				sac.setCodice(model.getProvvedimento().getCodiceStrutturaAmministrativa());
				sac.setDescrizione(model.getProvvedimento().getStrutturaAmministrativa());
				atto.setStrutturaAmmContabile(sac);
			  }
			  
			  //uid del provv se selezionato da compilazione guidata:
			  if(model.getProvvedimento()!=null && model.getProvvedimento().getUid()!=null){
				  atto.setUid(model.getProvvedimento().getUid());
			  }
			  
			  //setto l'atto nel parametro di ricerca:
			  prm.setAttoAmministrativo(atto);
			  
			  //setto il parametro di ricerca nella request:
			  rm.setParametroRicercaMutuo(prm);
			  
			  // setto in sessione i parametri di ricerca
			  sessionHandler.setParametro(FinSessionParameter.PAR_RICERCA_MUTUO, prm);
		  }else{
			  
			  //codice mutuo
			  if(getCodiceMutuo()!= null && StringUtils.isNotEmpty(getCodiceMutuo())){
				  ParametroRicercaMutuo prRimodulato = new ParametroRicercaMutuo();
				  prRimodulato.setNumeroMutuo(getCodiceMutuo());
				  prm = prRimodulato;
				  sessionHandler.setParametro(FinSessionParameter.PAR_RICERCA_MUTUO, prm);
			  	  model.setPremutoPaginazione(0);
			  }
			  
			  //setto il parametro di ricerca nella request:
			  rm.setParametroRicercaMutuo(prm);
		  }
		  
		  if(model.getPremutoPaginazione()!=0){
			  rm.setNumPagina(model.getPremutoPaginazione());
			  rm.setNumRisultatiPerPagina(DEFAULT_PAGE_SIZE);
		  }else{
			  //  : numeratore per la paginazione
			  addNumAndPageSize(rm, "ricercaMutuiID");
		  }
		  return rm;
	}


	@Override
	public String listaClasseSoggettoChanged() {
		model.getSoggetto().setCodCreditore(null);
		model.setSoggettoSelezionato(false);
		return "headerSoggetto";	
	}


	@Override
	protected void setCapitoloSelezionato(CapitoloImpegnoModel supportCapitolo) {}



	@Override
	protected void setErroreCapitolo() {}


	@Override
	protected void setSoggettoSelezionato(SoggettoImpegnoModel soggettoImpegnoModel) {
		model.setSoggetto(soggettoImpegnoModel);
		model.setSoggettoSelezionato(true);
		
	}


	@Override
	protected void setProvvedimentoSelezionato(ProvvedimentoImpegnoModel currentProvvedimento) {
		model.setProvvedimento(currentProvvedimento);
		model.setProvvedimentoSelezionato(true);
		
	}
	
	public boolean checkSoggettoStato(){
		if(model.isSoggettoSelezionato()){
			return true;
		}else{
			return false;
		}
	}
	public boolean checkProvvedimentoStato(){
	if(model.isProvvedimentoSelezionato()){
			return true;
		}else{
			return false;
		}
	}
	
	public String getCodiceMutuo() {
		return codiceMutuo;
	}


	public void setCodiceMutuo(String codiceMutuo) {
		this.codiceMutuo = codiceMutuo;
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
	public boolean transazioneElementareAttiva() {
		return false;
	}

	@Override
	public boolean altriClassificatoriAttivi() {
		return false;
	}

	@Override
	public String confermaPdc() {
		return null;
	}


	@Override
	public String confermaSiope() {
		return null;
	}
	
	@Override
	public boolean programmaPoliticoRegionaleUnitarioAttivo() {
		return false;
	}
	
	@Override
	public boolean datiUscitaImpegno() {
		 return true;
	}
	
	@Override
	public String listaClasseSoggettoDueChanged() {
		// Auto-generated method stub
		return null;
	}

	@Override
	protected void setSoggettoSelezionatoDue(SoggettoImpegnoModel soggettoImpegnoModel) {
		// Auto-generated method stub
	}
	
}

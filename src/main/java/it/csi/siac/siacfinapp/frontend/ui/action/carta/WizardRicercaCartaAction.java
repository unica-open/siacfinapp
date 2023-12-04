/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.carta;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacfinapp.frontend.ui.action.GenericPopupAction;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.carta.RicercaCartaModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinser.frontend.webservice.CartaContabileService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabileResponse;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaCartaContabile;

public class WizardRicercaCartaAction extends GenericPopupAction<RicercaCartaModel>{
	
	private static final long serialVersionUID = 1L;

	
	@Autowired
	protected transient CartaContabileService cartaContabileService;
	
	@Override
	protected void setCapitoloSelezionato(CapitoloImpegnoModel supportCapitolo) {
		model.setCapitolo(supportCapitolo);
		model.setCapitoloTrovato(true);
		model.setDatoPerVisualizza(supportCapitolo);
		model.setListaRicercaCapitolo(null);
		model.setCapitoloSelezionato(true);
		model.setToggleImputazioneContabiliAperto(true);
	}

	@Override
	protected void setErroreCapitolo() {
		
	}

	@Override
	protected void setSoggettoSelezionato(SoggettoImpegnoModel soggettoImpegnoModel) {
			model.setToggleImputazioneContabiliAperto(true);
			model.setSoggettoSelezionato(true);
			model.setSoggetto(soggettoImpegnoModel);
		
	}

	@Override
	protected void setProvvedimentoSelezionato(
			ProvvedimentoImpegnoModel currentProvvedimento) {
		//  Auto-generated method stub
		model.setProvvedimento(currentProvvedimento);
		model.setProvvedimentoSelezionato(true);
	}

	
	
	public String confermaImpegnoCarta(){
		// click di impegno dopo la ricerca
		if(model.isHasImpegnoSelezionatoPopup()==true){
			model.setNumeroImpegno(model.getnImpegno());
			model.setAnnoImpegno(model.getnAnno());
			model.setNumeroSub(model.getnSubImpegno());
			model.setnAnno(null);
			model.setnImpegno(null);
		}
		// cosi' tiene la tendina aperta
		model.setToggleImputazioneContabiliAperto(true);

        // pulisci i dati
		pulisciListeeSchedaPopup();
		return SUCCESS;
	}
	
	/**
	 * converter per la ricerca della carta
	 * @return
	 */
	private RicercaCartaContabile convertModelPerChiamataRicercaCarta(){
		// convertitore per la chiamata della ricerca
		RicercaCartaContabile rcc = new RicercaCartaContabile();
		rcc.setRichiedente(sessionHandler.getRichiedente());
		rcc.setEnte(sessionHandler.getEnte());
	    ParametroRicercaCartaContabile prk = new ParametroRicercaCartaContabile();
	    prk.setRichiedente(sessionHandler.getRichiedente());
	    // anno esercizio
	    prk.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
	    // numero DA
	    if(StringUtils.isNotEmpty(model.getNumeroCartaDa())){
	    	prk.setNumeroCartaContabileDa(Integer.parseInt(model.getNumeroCartaDa()));
	    }
	    // numero A
	    if(StringUtils.isNotEmpty(model.getNumeroCartaA())){
	    	prk.setNumeroCartaContabileA(Integer.parseInt(model.getNumeroCartaA()));
	    }
	    
	    // data Scadenza Da
	    if(StringUtils.isNotEmpty(model.getDataScadenzaCartaDa())){
	    	prk.setDataScadenzaDa(DateUtility.parse(model.getDataScadenzaCartaDa()));
	    }
	    
	    // data scadenda A
	    if(StringUtils.isNotEmpty(model.getDataScadenzaCartaA())){
	    	prk.setDataScadenzaA(DateUtility.parse(model.getDataScadenzaCartaA()));
	    }
	    
	    // stato
	    prk.setStatoOperativo(model.getStatoCarta());
	    
	    // descrizione
	    if(StringUtils.isNotEmpty(model.getDescrizioneCarta())){
	    	prk.setOggetto(model.getDescrizioneCarta());
	    }
	    
	  
	    // blocco del provvedimento
	    if (model.getProvvedimento()!=null){
	    	// setto anno per ricerca
	    	if(model.getProvvedimento().getAnnoProvvedimento()!=null)
	    		prk.setAnnoProvvedimento(model.getProvvedimento().getAnnoProvvedimento());
	    	
	    	// setto numero per ricerca
	    	if(model.getProvvedimento().getNumeroProvvedimento()!=null)
	    		prk.setNumeroProvvedimento(model.getProvvedimento().getNumeroProvvedimento().intValue());

			// setto tipo provvedimento
	    	if(model.getProvvedimento().getIdTipoProvvedimento()!=null){
				TipoAtto tipoAtto = new TipoAtto();
				tipoAtto.setUid(model.getProvvedimento().getIdTipoProvvedimento());
				
				if(model.getListaTipiProvvedimenti()!=null && model.getListaTipiProvvedimenti().size()>0){
					for (Object tipo : model.getListaTipiProvvedimenti()){
						if(tipo!=null){
							if (model.getProvvedimento().getIdTipoProvvedimento().compareTo(((TipoAtto)tipo).getUid())==0) {
								tipoAtto.setCodice(((TipoAtto)tipo).getCodice());
								break;
							}
						}
					}
				}
				
				prk.setTipoProvvedimento(tipoAtto.getCodice());
	    	}
	    }

	    // capitolo
	    if(model.getCapitolo()!=null){
	    	
	    	if(model.getCapitolo().getAnno()!=null){
	    		prk.setAnnoCapitolo(model.getCapitolo().getAnno());
	    	}
	    	if(model.getCapitolo().getNumCapitolo()!=null){
	    		prk.setNumeroCapitolo(new BigDecimal(model.getCapitolo().getNumCapitolo().intValue()));
	    	}
	    	if(model.getCapitolo().getArticolo()!=null){
	    		prk.setNumeroArticolo(new BigDecimal(model.getCapitolo().getArticolo().intValue()));
	    	}
	    	if(model.getCapitolo().getUeb()!=null){
	    		prk.setNumeroUEB(model.getCapitolo().getUeb().intValue());
	    	}
	    	
	    	
	    }
	    
	    // anno impegno
	    if(model.getAnnoImpegno()!=null){
	    	prk.setAnnoImpegno(model.getAnnoImpegno());
	    }
	    
	    // numero impegno
	    if(model.getNumeroImpegno()!=null){
	    	prk.setNumeroImpegno(new BigDecimal(model.getNumeroImpegno().intValue()));
	    }
	    
	    // numero subimpegno
	    if(model.getNumeroSub()!=null){
	    	prk.setNumeroSubImpegno(new BigDecimal(model.getNumeroSub().intValue()));
	    }
	    
	    // creditore
	    if(model.getSoggetto()!=null){
	    	if(StringUtils.isNotEmpty(model.getSoggetto().getCodCreditore())){
	    		prk.setCodiceCreditore(model.getSoggetto().getCodCreditore());
	    	}
	    	
	    }
	    
	    //set di richiedente
	    prk.setRichiedente(sessionHandler.getRichiedente());
	    
	    rcc.setParametroRicercaCartaContabile(prk);
	    // memorizzo in sessione i parametri con il quale ho lanciato la ricerca
	    sessionHandler.setParametro(FinSessionParameter.PAR_RICERCA_CARTA, prk);
	    
		// paginazione    
	    addNumAndPageSize(rcc, "ricercaCarteContID");
		
		
		return rcc;
	}
	
	/**
	 * utility per capire se la ricerca e' stata lanciata piena o meno
	 * @return
	 */
	private boolean parametriRicercaSelezionati(){
		boolean selezionati = true;
		
		ParametroRicercaCartaContabile prcc = (ParametroRicercaCartaContabile)sessionHandler.getParametro(FinSessionParameter.PAR_RICERCA_CARTA);
		
		// se il richiedente non e' settato allora sono passato dalla pagina
		// di ricerca al click di cerca con solamento l'oggetto RicercaCartaContabile istanziato
		
		if(prcc.getRichiedente()==null) selezionati = false;
		
		return selezionati;
		
	}
	
	protected String executeRicercaCartaContabile(){
		RicercaCartaContabile ricercaCartaContabile = new RicercaCartaContabile();
		
		// se sono stati settati gia' dei parametri per la ricerca
		if(parametriRicercaSelezionati()){
			// costruisco l'oggetto per la ricerca
			ParametroRicercaCartaContabile prcc =  (ParametroRicercaCartaContabile)sessionHandler.getParametro(FinSessionParameter.PAR_RICERCA_CARTA);
			prcc.setRichiedente(sessionHandler.getRichiedente());
			prcc.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
			ricercaCartaContabile.setParametroRicercaCartaContabile(prcc);
			ricercaCartaContabile.setEnte(sessionHandler.getEnte());
			ricercaCartaContabile.setRichiedente(sessionHandler.getRichiedente());
			// MEMORIZZO I PARAMETRI DI RICERCA CARTA
			sessionHandler.setParametro(FinSessionParameter.PAR_RICERCA_CARTA, prcc);
			
			addNumAndPageSize(ricercaCartaContabile, "ricercaCarteContID");
			
		}else{
		   // altrimenti li prendo dalla maschera	
		   ricercaCartaContabile = convertModelPerChiamataRicercaCarta();
		}
		
		// chiamata al servizio di ricerca 
		RicercaCartaContabileResponse response = cartaContabileService.ricercaCartaContabile(ricercaCartaContabile);
		
		if(response!=null && response.isFallimento()){
			addErrori(response.getErrori());
			return INPUT;
		}
		
		
		// gestione della response
		if(response!=null && response.getElencoCarteContabili()!=null && response.getElencoCarteContabili().size()>0){
			// setto i risultati della ricerca
			model.setElencoCarteContabili(response.getElencoCarteContabili());
			
			sessionHandler.setParametro(FinSessionParameter.RIGHE_DA_REGOLARIZZARE, response.getElencoCarteContabili());
			
			model.setResultSize(response.getNumRisultati());
		}else{
			model.setElencoCarteContabili(new ArrayList<CartaContabile>());
			sessionHandler.setParametro(FinSessionParameter.RIGHE_DA_REGOLARIZZARE, new ArrayList<CartaContabile>());
			model.setResultSize(0);
		}
		if(model.getPremutoPaginazione()!=0){
			ricercaCartaContabile.setNumPagina(model.getPremutoPaginazione());
			ricercaCartaContabile.setNumRisultatiPerPagina(DEFAULT_PAGE_SIZE);
			  
	    }else{
		  
		  
	    }
		return SUCCESS;
	}
	
	
	
	/**
	 * GESTORE TRANSAZIONE ECONOMICA sono tutti in override
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
		 return false;
	}

	@Override
	public String listaClasseSoggettoChanged() {
		//  Auto-generated method stub
		return null;
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
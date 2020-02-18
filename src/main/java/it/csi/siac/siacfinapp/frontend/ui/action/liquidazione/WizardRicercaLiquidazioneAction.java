/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.liquidazione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.GenericPopupAction;
import it.csi.siac.siacfinapp.frontend.ui.model.liquidazione.ImpegnoLiquidazioneModel;
import it.csi.siac.siacfinapp.frontend.ui.model.liquidazione.RicercaLiquidazioneModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazioni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazioniResponse;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaLiquidazione;

public class WizardRicercaLiquidazioneAction extends GenericPopupAction<RicercaLiquidazioneModel> {

	private static final long serialVersionUID = 1L;
	
	private static final String INS_LIQ="OP-SPE-insLiq";
	private static final String INS_LIQ_MAN="OP-SPE-insLiqMan";
	private static final String GEST_LIQ="OP-SPE-gestLiq";
	private static final String LEGGI_LIQ="OP-SPE-leggiLiq";
	
	@Override
	public String getActionKey() {
		return "ricercaLiquidazione";
	}
	
	//Metodo per pulire il model (annulla)
	protected void pulisciCampi(){
	
		model.setProvvedimento(new ProvvedimentoImpegnoModel());
		model.setSoggetto(new SoggettoImpegnoModel());
		model.setCapitolo(new CapitoloImpegnoModel());
		model.setImpegno(new ImpegnoLiquidazioneModel());
		model.setNumeroMutuo(null);
		model.setNumeroMutuoString(null);
		model.setNumeroMutuoImpegnoString(null);
		model.setNumeroLiquidazione(null);
		model.setNumeroLiquidazioneString(null);
		model.setAnnoLiquidazione(null);
		
		model.setCapitoloSelezionato(false);
		model.setHasImpegnoSelezionatoXPopup(false);
		model.setProvvedimentoSelezionato(false);
		model.setSoggettoSelezionato(false);
	}
	
	/**
	 * convertitore del model per poter richiamare il ricerca liquidazioni
	 * @return
	 */
	protected RicercaLiquidazioni convertiModelPerChiamataServizioRicercaLiquidazioni() {
		
		//inizializzo la request:
		RicercaLiquidazioni ricercaLiquidazioni = new RicercaLiquidazioni();
		
		//istanzio il parametro di ricerca:
		ParametroRicercaLiquidazione prl = new ParametroRicercaLiquidazione();
		
		
		//Richiedente
		ricercaLiquidazioni.setRichiedente(sessionHandler.getRichiedente());
		
		//Ente
		ricercaLiquidazioni.setEnte(sessionHandler.getAccount().getEnte());
		
		//Anno Esercizio
		prl.setAnnoBilancio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		prl.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		
		//COPIAMO DEL CAMPO HIDDEN PER VIA DEL CHECKBOX CHE PERDE IL VALORE:
		model.setEscludiAnnullati(model.getHiddenPerEscludiAnnullati());
		if(model.getEscludiAnnullati()!=null && model.getEscludiAnnullati()==true){
			List<String> statiDaEscludere = new ArrayList<String>();
			statiDaEscludere.add("A");
			prl.setStatiDaEscludere(statiDaEscludere);
		}
		//
		
		//Liquidazione
		if(model.getAnnoLiquidazione()!=null){
			prl.setAnnoLiquidazione(Integer.valueOf(model.getAnnoLiquidazione()));
		}
		if(model.getNumeroLiquidazione() != null){
			prl.setNumeroLiquidazione(model.getNumeroLiquidazione());
		}
	
		//mutuo
		if(model.getNumeroMutuo()!=null){
			prl.setNumeroMutuo(model.getNumeroMutuo());
		}
		
		//Anno Impegno
		if(model.getImpegno().getAnnoImpegno()!=null){
			prl.setAnnoImpegno(model.getImpegno().getAnnoImpegno());
		}
		
		//Numero impegno
		if(model.getImpegno().getNumeroImpegno()!=null){
			prl.setNumeroImpegno(BigDecimal.valueOf(model.getImpegno().getNumeroImpegno()));

			//numero sub
			if(model.getImpegno().getNumeroSub()!=null){
				prl.setNumeroSubImpegno(BigDecimal.valueOf(model.getImpegno().getNumeroSub()));
			}

			//numero mutuo
			if(model.getImpegno().getNumeroMutuo()!=null){
				prl.setNumeroMutuo(BigDecimal.valueOf(model.getImpegno().getNumeroMutuo()));
			}
		}
		
		//Soggetto
		if(model.getSoggetto() != null && (!StringUtils.isEmpty(model.getSoggetto().getCodCreditore()))){
			prl.setCodiceCreditore(model.getSoggetto().getCodCreditore());
		}
		
		//Gestisco Capitolo
		if(model.getCapitolo() != null){
			//jira 1339 - ricerca capitolo per 0
			
			//anno capitolo
			if(model.getCapitolo().getAnno() != null){
				prl.setAnnoCapitolo(model.getCapitolo().getAnno());
			}
			
			//numero capitolo
			if(model.getCapitolo().getNumCapitolo() != null){
				prl.setNumeroCapitolo(new BigDecimal(model.getCapitolo().getNumCapitolo()));
			}
			
			//numero articolo
			if(model.getCapitolo().getArticolo() != null){
				prl.setNumeroArticolo(new BigDecimal(model.getCapitolo().getArticolo()));
			}
			
			//ueb capitolo
			if(model.getCapitolo().getUeb() != null){
				prl.setNumeroUEB(model.getCapitolo().getUeb().intValue());
			}				
			
		}
		
		// Imposto il Provvedimento JIRA siac-3768
		if(model.getProvvedimento() != null){
			
			if(model.getProvvedimento().getUid() != null && model.getProvvedimento().getUid() != 0){
				//uid provvedimeneto
				prl.setUidProvvedimento(model.getProvvedimento().getUid());
			} else {
				
				//anno provvedimento
				if(model.getProvvedimento().getAnnoProvvedimento() != null && model.getProvvedimento().getAnnoProvvedimento() != 0){
					if(model.getProvvedimento().getAnnoProvvedimento().toString().length() == 4){
						prl.setAnnoProvvedimento(model.getProvvedimento().getAnnoProvvedimento());
					}
				}
				
				//numero provvedimento
				if(model.getProvvedimento().getNumeroProvvedimento() != null && model.getProvvedimento().getNumeroProvvedimento().intValue() != 0){
					prl.setNumeroProvvedimento(new BigDecimal(model.getProvvedimento().getNumeroProvvedimento().intValue()));
				}
				
				//id tipo provvedimento
				if(model.getProvvedimento().getIdTipoProvvedimento() != null && model.getProvvedimento().getIdTipoProvvedimento() != 0){
					prl.setTipoProvvedimento(String.valueOf(model.getProvvedimento().getIdTipoProvvedimento()));
				}	
				
				//uid struttura amministrativa
				if(model.getProvvedimento().getUidStrutturaAmm()!=null){
					prl.setUidStrutturaAmministrativaProvvedimento(model.getProvvedimento().getUidStrutturaAmm());
				}
				
			}
		}
		
		//Setto il tipo di ricerca:
		prl.setTipoRicerca(Constanti.TIPO_RICERCA_DA_LIQUIDAZIONE);
		
		//setto il paramentro nella request:
		ricercaLiquidazioni.setParametroRicercaLiquidazione(prl);
		
		//dati di paginazione:
		String keyRicercaLiquidazioniID = (new ParamEncoder("ricercaLiquidazioniID")).encodeParameterName(TableTagParameters.PARAMETER_PAGE);
		if(model.isFromAnnulla() && model.getNumeroPaginaElenco()>0){
			//da annulla
			getRequest().put(keyRicercaLiquidazioniID, Integer.toString(model.getNumeroPaginaElenco()));
		} 
		addNumAndPageSize(ricercaLiquidazioni,"ricercaLiquidazioniID");
		model.setNumeroPaginaElenco(ricercaLiquidazioni.getNumPagina());
		model.setFromAnnulla(false);
		
		//ritorno la request al chiamante:
		return ricercaLiquidazioni;
	}
	
	/**
	 * Esegue la ricerca
	 * @return
	 */
	protected String executeRicercaLiquidazioni() {

		//compongo la request:
		RicercaLiquidazioni parametroRicerca = convertiModelPerChiamataServizioRicercaLiquidazioni();
		
		//invoco il servizio di ricerca:
		RicercaLiquidazioniResponse response = liquidazioneService.ricercaLiquidazioni(parametroRicerca);
		
		//analizzo la response:
		if(response.isFallimento()){
			//ci sono errori
			if (response.getErrori()!=null && response.getErrori().size()>0) {
				for (Errore errore : response.getErrori()) {
					addPersistentActionError(errore.getCodice()+" "+errore.getDescrizione());
				}
			}
			return INPUT;
		}
				
		//setto denominazione sede se liq associata a sede --->> commentata per jira 1297						
		model.setElencoLiquidazioni(response.getElencoLiquidazioni());
		model.setResultSize(response.getNumRisultati());
		
		return "gotoElencoLiquidazioni";	
	}

	@Override
	public String listaClasseSoggettoChanged() {
		model.getSoggetto().setCodCreditore(null);
		model.setSoggettoSelezionato(false);
		return "headerSoggetto";	
	}

	@Override
	protected void setCapitoloSelezionato(CapitoloImpegnoModel supportCapitolo) {
		model.setCapitolo(supportCapitolo);
		model.setCapitoloSelezionato(true);
		
		model.setDatoPerVisualizza(supportCapitolo);
		model.setListaRicercaCapitolo(null);
		model.setCapitoloTrovato(false);
	}


	@Override
	protected void setErroreCapitolo() {
		StringBuilder supportMessaggio = new StringBuilder(); 
		supportMessaggio.append(model.getCapitolo().getAnno());
		supportMessaggio.append((model.getCapitolo().getNumCapitolo() != null) ? "/" + model.getCapitolo().getNumCapitolo() : "");
		supportMessaggio.append((model.getCapitolo().getArticolo() != null) ? "/" + model.getCapitolo().getArticolo() : "");
		supportMessaggio.append((model.getCapitolo().getUeb() != null) ? "/" + model.getCapitolo().getUeb() : "");
		addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo", supportMessaggio.toString()));
	}


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

	@Override
	public String confermaCompGuidata(){
		if(model.isHasImpegnoSelezionatoPopup()==true){
			model.getImpegno().setNumeroImpegno(model.getnImpegno());
			model.getImpegno().setAnnoImpegno(model.getnAnno());
			if(model.getnSubImpegno()!=null){
				model.getImpegno().setNumeroSub(model.getnSubImpegno());
			} else { 
				model.getImpegno().setNumeroSub(null);
			}	
			model.setNumeroMutuoPopup(null);
			model.setnAnno(null);
			model.setnImpegno(null);
			int voceMutuoScelta = model.getRadioVoceMutuoSelezionata();
			List<VoceMutuo> listaVocMutuo = model.getListaVociMutuo();		
			if(listaVocMutuo!=null && listaVocMutuo.size()>0){
				for(int j=0; j<listaVocMutuo.size();j++){
					if(listaVocMutuo.get(j).getUid()==voceMutuoScelta){
						model.getImpegno().setNumeroMutuo(Integer.valueOf(listaVocMutuo.get(j).getNumeroMutuo()));
						model.setNumeroMutuoImpegnoString(listaVocMutuo.get(j).getNumeroMutuo());
					}
				}
			}
		}
		pulisciListeeSchedaPopup();
		return INPUT;
	}
	
	public boolean isAbilitatoInserisciLiquidazione(){
		return isAzioneAbilitata(INS_LIQ);
	}
	
	public boolean isAbilitatoInserisciLiquidazioneMan(){
		return isAzioneAbilitata(INS_LIQ_MAN);
	}
	
	public boolean isAbilitatoGestisciLiquidazione(){
		return isAzioneAbilitata(GEST_LIQ);
	}
	
	public boolean isAbilitatoLeggiLiquidazione(){
		return isAzioneAbilitata(LEGGI_LIQ);
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

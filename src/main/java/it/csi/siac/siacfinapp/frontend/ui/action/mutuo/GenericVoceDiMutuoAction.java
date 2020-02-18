/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.mutuo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacfinapp.frontend.ui.action.GenericPopupAction;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.mutuo.GestioneVoceDiMutuoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaMutuoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaMutuoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaMutuoPerChiaveResponse;
import it.csi.siac.siacfinser.model.mutuo.Mutuo.StatoOperativoMutuo;
import it.csi.siac.siacfinser.model.mutuo.VariazioneImportoVoceMutuo;
import it.csi.siac.siacfinser.model.mutuo.VariazioneImportoVoceMutuo.TipoVariazioneImportoVoceMutuo;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;
import it.csi.siac.siacfinser.model.ric.RicercaMutuoK;

public class GenericVoceDiMutuoAction extends GenericPopupAction<GestioneVoceDiMutuoModel> {

	private static final long serialVersionUID = 1L;
	
	protected final static String GOTO_GESTIONE_VOCE_DI_MUTUO = "gotoGestioneVoceDiMutuo";
	protected final static String GOTO_ELENCO_VOCE_DI_MUTUO = "gotoElencoVociDiMutuo";
	protected final static String CU_GESTISCI_MUTUO = CodiciOperazioni.OP_MUT_gestisciMutuo;
	private String methodName;
	
	private String codiceMutuo;
	private String numeroVoceMutuoSelezionato;
	
	
	@Autowired
	protected transient MovimentoGestioneService movimentoGestioneService;

	@Override
	protected void setCapitoloSelezionato(CapitoloImpegnoModel supportCapitolo) {}
	
	@Override
	protected void setErroreCapitolo() {}

	@Override
	public String listaClasseSoggettoChanged() {
		return null;
	}

	@Override
	protected void setSoggettoSelezionato(SoggettoImpegnoModel soggettoImpegnoModel) {}

	@Override
	protected void setProvvedimentoSelezionato(ProvvedimentoImpegnoModel currentProvvedimento) {
		//setto il provvedimento nel model:
		model.setProvvedimento(currentProvvedimento);
		model.setProvvedimentoSelezionato(true);
	}
	
	@Override
	public String getActionKey() {
		return "gestioneVoceDiMutuo";
	}
	
	@Override
	public String getActionDataKeys() {
		return "model,numeroVoceMutuoSelezionato";
	}
	
	/**
	 * funzione utilizzata nella jsp
	 * @return
	 */
	public boolean isAbilitato(){
		//leggo lo stato:
		String statoMutuo =  model.getMutuoSelezionato().getStatoOperativoMutuo().name();
		//decico in base allo stato:
		if((statoMutuo.equalsIgnoreCase(StatoOperativoMutuo.DEFINITIVO.name()) ||
		   statoMutuo.equalsIgnoreCase(StatoOperativoMutuo.PROVVISORIO.name()) ) && 
		    FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(), CU_GESTISCI_MUTUO) ){
			//caso abilitato
		    return true;
		}
		//caso NON abilitato
		return false;
		
	}
	
	/**
	 * funzione che richiama la ricerca mutuo per chiave
	 * @return
	 */
	protected String routineRicercaMutuoPerChiave(){
		
		//compongo la request per il servizio:
		RicercaMutuoPerChiave requestRicerca = getRicercaMutuoPerChiave();
		
		//invoco il servizio di ricerca: 
		RicercaMutuoPerChiaveResponse response = mutuoService.ricercaMutuoPerChiave(requestRicerca );
		
		//analizzo l'esito del servizio:
		if ((response.isFallimento()) || ((response.getErrori() != null) && (response.getErrori().size() > 0))) {
			//presenza errori
			debug("getRicercaMutuoPerChiave", new Object[] { "Errore nella risposta del servizio" });
			addErrori(response.getErrori());
			return INPUT;
		}
		
		//tutto ok:
		model.setMutuoSelezionato(response.getMutuo());
		
		return "";
	}
	
	/**
	 * funzione che richiama la ricerca mutuo per chiave
	 * @return
	 */
	protected RicercaMutuoPerChiave getRicercaMutuoPerChiave(){
		//istanzio la request:
		RicercaMutuoPerChiave request = new RicercaMutuoPerChiave();

		//istanzio il parametro di ricerca:
		RicercaMutuoK pRicercaMutuoK = new RicercaMutuoK();
		
		//setto il codice mutuo
		pRicercaMutuoK.setMutCode(getCodiceMutuo());

		//l'ente
		request.setEnte(this.sessionHandler.getEnte());
		
		//il richiedente
		request.setRichiedente(this.sessionHandler.getRichiedente());

		//data di richiesta
		request.setDataOra(new Date());
		
		//setto il paramentro nella request
		request.setpRicercaMutuoK(pRicercaMutuoK);
		
		//ritorno la request al chiamante:
		return request;
	}
	
	protected String cicloPerVariazione(BigDecimal importoVariazione, TipoVariazioneImportoVoceMutuo tipo){
		
			for (VoceMutuo itVm : model.getMutuoSelezionato().getListaVociMutuo()) {
				
				if (null!=getNumeroVoceMutuoSelezionato() && itVm.getNumeroVoceMutuo().equals(getNumeroVoceMutuoSelezionato())) {
					// aggiorno il valore attuale della voce in questione
					
					if(tipo.equals(TipoVariazioneImportoVoceMutuo.RETTIFICA)){
						// se rettifica cambio il segno del valore
						// cosicche la subtract successiva continui
						// a funzionare correttamente
						importoVariazione = importoVariazione.negate();
					}
					
					if(tipo.equals(TipoVariazioneImportoVoceMutuo.RIDUZIONE) || tipo.equals(TipoVariazioneImportoVoceMutuo.ECONOMIA)){
						//Jira 592  setto direttamente la disponibilta'
						BigDecimal importoDisponibileModifiche= itVm.getImportoDisponibileModificheImpegno().negate();
						importoDisponibileModifiche=importoDisponibileModifiche.subtract(importoVariazione);
						itVm.setImportoDisponibileModificheImpegno(importoDisponibileModifiche.negate());
					}
					
					itVm.setImportoAttualeVoceMutuo(itVm.getImportoAttualeVoceMutuo().subtract(importoVariazione));
					
					VariazioneImportoVoceMutuo vim = new VariazioneImportoVoceMutuo();
					
				    // metto il meno davanti al numero
					vim.setImportoVariazioneVoceMutuo(importoVariazione.negate());

					vim.setTipoVariazioneImportoVoceMutuo(tipo);
					if (itVm.getListaVariazioniImportoVoceMutuo() == null) {
						itVm.setListaVariazioniImportoVoceMutuo(new ArrayList<VariazioneImportoVoceMutuo>());
					}
					itVm.getListaVariazioniImportoVoceMutuo().add(vim);

					AggiornaMutuo aggiornaMutuo = new AggiornaMutuo();
					aggiornaMutuo.setEnte(sessionHandler.getEnte());
					aggiornaMutuo.setRichiedente(sessionHandler.getRichiedente());
					
					aggiornaMutuo.setMutuo(model.getMutuoSelezionato());

					AggiornaMutuoResponse response = mutuoService.aggiornaMutuo(aggiornaMutuo);

					if (response != null && !response.isFallimento()) {
						setCodiceMutuo(model.getMutuoSelezionato().getCodiceMutuo());
						
						// routine di ricerca mutuo
						if(routineRicercaMutuoPerChiave().equalsIgnoreCase(INPUT)){
							return INPUT;
						}

						return SUCCESS;
						
					} else {
						setCodiceMutuo(model.getMutuoSelezionato().getCodiceMutuo());
						// routine di ricerca mutuo
						if(routineRicercaMutuoPerChiave().equalsIgnoreCase(INPUT)){
							return INPUT;
						}
						
						addErrori(response.getErrori());
						return INPUT;
					}
				}
			}
			
			return SUCCESS;
	}
	
	
	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getCodiceMutuo() {
		return codiceMutuo;
	}

	public void setCodiceMutuo(String codiceMutuo) {
		this.codiceMutuo = codiceMutuo;
	}

	public String getNumeroVoceMutuoSelezionato() {
		return numeroVoceMutuoSelezionato;
	}

	public void setNumeroVoceMutuoSelezionato(String numeroVoceMutuoSelezionato) {
		this.numeroVoceMutuoSelezionato = numeroVoceMutuoSelezionato;
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
		//Auto-generated method stub
		return null;
	}

	@Override
	protected void setSoggettoSelezionatoDue(SoggettoImpegnoModel soggettoImpegnoModel) {
		//Auto-generated method stub
	}
	
}

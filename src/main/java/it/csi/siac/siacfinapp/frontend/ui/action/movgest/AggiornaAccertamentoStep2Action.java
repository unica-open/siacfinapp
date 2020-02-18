/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamentoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.errore.ErroreFin;


	@Component
	@Scope(WebApplicationContext.SCOPE_REQUEST)
	public class AggiornaAccertamentoStep2Action extends ActionKeyAggiornaAccertamento{

		private static final long serialVersionUID = 1L;
		
		private boolean successStep2 = false;
		private String numeroAccertamento;
		private String annoAccertamento;
		
		/**
		 * settembre 2017 SIAC-5288
		 */
		@Override
		public boolean altriClassificatoriAttivi() {
			return super.altriClassificatoriAttivi() && isAbilitatoAggiornamentoGenerico();
		}
		
		public AggiornaAccertamentoStep2Action () {
		   	//setto la tipologia di oggetto trattato:
			oggettoDaPopolare = OggettoDaPopolareEnum.ACCERTAMENTO;
		}
		
		@Override
		public void prepare() throws Exception {
			setMethodName("prepare");
			//invoco il prepare della super classe:
			super.prepare();
			
			//setto il titolo:
			this.model.setTitolo("Aggiorna Accertamento - Classificazioni");
		}
		
		@Override
		@BreadCrumb("%{model.titolo}")
		public String execute() throws Exception {
			setMethodName("execute");
			
			// copio il valore di cup dalla prima pagina
			teSupport.setCup(model.getStep1Model().getCup());
			caricaMissione(model.getStep1Model().getCapitolo());
			caricaProgramma(model.getStep1Model().getCapitolo());
			// capitolo entrata non possiede il macroaggregato
			// Jira - 1357 in caso di errore di caricamento dei dati
			// dei classificatori non viene segnalato alcun errore
			// ma carica la pagina, al massimo non verranno visualizzate le combo relative
			caricaListeFin(TIPO_ACCERTAMENTO_A);
			
			if(null!=teSupport && StringUtils.isEmpty(teSupport.getOggettoAbilitaTE())){
				// utilizzato per la transazione e le condizioni di obbligatorieta
				teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.ACCERTAMENTO.toString());
			}
			
			//29-09-2015, VIENE ESEGUITO SEMPRE PERCHE' DAL GIRO DELL'INSERISCI SUB ACCERTAMENO SI PERDE LE LISTE:
			if (caricaListeBil(WebAppConstants.CAP_EG)) {
				return INPUT;
			}
			//
			
			caricaLabelsAggiorna(2);
			
			creaMovGestModelCache();
			
			//disabilito il caricamento degli alberi inutili per questo scenario (in AjaxAction.java):
			teSupport.setRicaricaAlberoPianoDeiConti(true);
			teSupport.setRicaricaStrutturaAmministrativa(false);
			teSupport.setRicaricaSiopeSpesa(true);
			//////////////////////////////////////////////////////////////////////////////////////////
			
		    return SUCCESS;

		}   
		
		public String salva() throws Exception {
			setMethodName("salva");
			log.debug(methodName, "provo a salvare");
			
			//controlli provvedimento rispetto all'abilitazione a gestire l'accertamento decentrato:
			String controlloProvv = provvedimentoConsentito();
			if(controlloProvv!=null){
				return controlloProvv;
			}
			//
			
			//ricontrolliamo il siope:
			codiceSiopeChangedInternal(teSupport.getSiopeSpesaCod());
			//
			
			// controlli tabellina 4.6
			List<Errore> erroriAbilitazione = abilitazioneCampiTE(oggettoDaPopolare);
			if(null!=erroriAbilitazione && erroriAbilitazione.size()>0){
				addErrori(erroriAbilitazione);
				return INPUT;
			}
			
			//CONTROLLO PIANO DEI CONTI SE CI SONO MOVIMENTI COLLEGATI:
			if(!controlloMovimentiCollegatiPerModificaPianoDeiConti(model.getAccertamentoInAggiornamento())){
				return INPUT;
			}
			//
			
			if(model.isProseguiConWarning()){
				if (isFromPopup()) {
	    			setShowPopUpMovColl(false);
	    		}else {
    				setShowPopUpMovColl(true);
    				addPersistentActionWarning(ErroreFin.WARNING_GENERICO.getErrore().getCodice()+" : "+ErroreFin.WARNING_GENERICO.getErrore("Accertamento con disponibilita' insufficiente. Si vuole procedere con l'aggiornamento?").getDescrizione());
	    			return INPUT;
	    		}
				
			}
			
			//16 febbraio 2017: pongo a false il parametro pulisci transazione elementare
			//e la pulisco manualmente solo dopo che il servizio ha finito con esito positivo
			//altrimenti si perde la transazione elementare per esito negativo:
			copiaTransazioneElementareSupportSuModel(false);
			//
			
			//SIAC-6000
			if(isNecessariaRichiestaConfermaUtentePerRedirezioneSuContabilitaGeneraleAggiornaAccertamento()) {
				model.setSaltaInserimentoPrimaNota(false);
				model.setRichiediConfermaRedirezioneContabilitaGenerale(true);
				return INPUT;
			}
			//
			
			AggiornaAccertamentoResponse response = movimentoGestionService.aggiornaAccertamento(convertiModelPerChiamataServizioAggiornaAccertamentiStep2()); 
			
			if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)){
				model.setRichiediConfermaRedirezioneContabilitaGenerale(false);
				debug(methodName, "Errore nella risposta del servizio");
				addErrori(methodName, response);
				return INPUT;
			} else {
				
				//SIAC-6000
				model.setUidDaCompletare(response.getRegistrazioneMovFinFIN() != null? response.getRegistrazioneMovFinFIN().getUid() : 0);
				//
				
				model.setRichiediConfermaRedirezioneContabilitaGenerale(false);

				//16 febbraio 2017: pulisco la transazione elementare solo per esito positivo:
				pulisciTransazioneElementare();
				//
				
				setSuccessStep2(true);
				
				// messaggio di ok nella pagina aggiorna
				addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " 
	            + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
				
				//controllo registrazione andata a buon fine:
				controlloRegistrazioneValidazionePrimaNota();
				//
				
				Accertamento accertamentoReload = response.getAccertamento();
				model.setAccertamentoRicaricatoDopoInsOAgg(accertamentoReload);
				
				caricaDatiAccertamento(true);
				
				creaMovGestModelCache();
				
				//SIAC-6000
				if(model.isEnteAbilitatoGestionePrimaNotaDaFinanziaria()
						&& model.isSaltaInserimentoPrimaNota() 
						&& model.getUidDaCompletare() > 0){
					return GO_TO_GEN;
				}
				//
				
				forceReload = true;
				return "gotoAggiorna";
			}	
		}  
			
		public String prosegui(){
			setMethodName("prosegui");
		    return "prosegui";
		}  
		
		public String indietroStep2(){
			setMethodName("indietro");
			
			forceReload=false;
			
			return "gotoAggiorna";
		}
		
		
		public String siSalva() throws Exception{
			setMethodName("siSalva");
			setFromPopup(true);
			return salva();
		}
		
		public String annulla() throws Exception {
		    return SUCCESS;
		}
		
		/* **************************************************************************** */
		/*  Getter / setter																*/
		/* **************************************************************************** */

		public String getNumeroAccertamento() {
			return numeroAccertamento;
		}

		public void setNumeroAccertamento(String numeroAccertamento) {
			this.numeroAccertamento = numeroAccertamento;
		}

		public String getAnnoAccertamento() {
			return annoAccertamento;
		}

		public void setAnnoAccertamento(String annoAccertamento) {
			this.annoAccertamento = annoAccertamento;
		}

		public boolean isSuccessStep2() {
			return successStep2;
		}

		public void setSuccessStep2(boolean successStep2) {
			this.successStep2 = successStep2;
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

	}
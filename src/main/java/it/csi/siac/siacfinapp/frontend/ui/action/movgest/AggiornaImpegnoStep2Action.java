/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.ParametroConfigurazioneEnteEnum;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegnoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.errore.ErroreFin;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaImpegnoStep2Action extends ActionKeyAggiornaImpegno{

	private static final long serialVersionUID = 1L;
	
	private boolean successStep2 = false;
	private String numeroImpegno;
	private String annoImpegno;
	
	public AggiornaImpegnoStep2Action () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.IMPEGNO;
	}
	
	/**
	 * settembre 2017 SIAC-5288
	 */
	@Override
	public boolean altriClassificatoriAttivi() {
		return super.altriClassificatoriAttivi() && isAbilitatoAggiornamentoGenerico();
	}
	
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		this.model.setTitolo("Aggiorna Impegno - Classificazioni");
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		
		leggiEventualiErroriEWarningAzionePrecedente(true);
		
		teSupport.setCup(model.getStep1Model().getCup());
		caricaMissione(model.getStep1Model().getCapitolo());
		caricaProgramma(model.getStep1Model().getCapitolo());
		caricaListaCofog(model.getStep1Model().getCapitolo().getIdProgramma());
		
		// Jira - 1357 in caso di errore di caricamento dei dati
		// dei classificatori non viene segnalato alcun errore
		// ma carica la pagina, al massimo non verranno visualizzate le combo relative
		caricaListeFin(TIPO_IMPEGNO_I);

		if(null!=teSupport && StringUtils.isEmpty(teSupport.getOggettoAbilitaTE())){
			// utilizzato per la transazione e le condizioni di obbligatorieta
			teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.IMPEGNO.toString());
		}
		
		//29-09-2015, VIENE ESEGUITO SEMPRE PERCHE' DAL GIRO DELL'INSERISCI SUB IMPEGNO SI PERDE LE LISTE:
		if (caricaListeBil(WebAppConstants.CAP_UG)) {
			return INPUT;
		}
		//
		
		caricaLabelsAggiorna(2);
		
		creaMovGestModelCache();
		
		//disabilito il caricamento degli alberi inutili per questo scnario (in AjaxAction.java):
		teSupport.setRicaricaAlberoPianoDeiConti(true);
		teSupport.setRicaricaStrutturaAmministrativa(false);
		teSupport.setRicaricaSiopeSpesa(true);
		//////////////////////////////////////////////////////////////////////////////////////////
		
		
		//siac-6997
		recuperaDescrizioneStrutturaCompetente();
		
	    return SUCCESS;
	}  
	
	public String salvaConByPassDodicesimi() throws Exception {
		return salvaInternal(true);
	}

	
	public String executeSalva() throws Exception {
		execute();
		return salva();
	}

	/**
	 * Wrapper di retrocompatibilita'
	 * @return
	 * @throws Exception
	 */
	public String salva() throws Exception {
		return salvaInternal(false);
	}
	
	private String salvaInternal(boolean byPassDodicesimi) throws Exception {
		setMethodName("salva");
		log.debug(methodName, "provo a salvare");

		model.resetErrori();
		model.resetWarning();
		model.resetMessaggi();
		
		//controlli provvedimento rispetto all'abilitazione a gestire l'impegno decentrato:
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
		if(!controlloMovimentiCollegatiPerModificaPianoDeiConti(model.getImpegnoInAggiornamento())){
			return INPUT;
		}
		//
		
		//SIAC-8825
		if (isObbligatorioPerimetroSanitario()) {
			List<Errore> erroriCapitoliPerimentroSanitario = validazioneCapitoliPerimetroSanitario(oggettoDaPopolare);
			if (! erroriCapitoliPerimentroSanitario.isEmpty()) {
				addErrori(erroriCapitoliPerimentroSanitario);
				return INPUT;
			}
		}
	
		//16 febbraio 2017: pongo a false il parametro pulisci transazione elementare
		//e la pulisco manualmente solo dopo che il servizio ha finito con esito positivo
		//altrimenti si perde la transazione elementare per esito negativo:
		copiaTransazioneElementareSupportSuModel(false);
		//
		
		AggiornaImpegno requestAggiorna = convertiModelPerChiamataServizioAggiornaImpegniStep2();
		
		//Eventuale by pass controllo dodicesimi:
		requestAggiorna.getImpegno().setByPassControlloDodicesimi(byPassDodicesimi);
		//
		
		//SIAC-6000
		if(isNecessariaRichiestaConfermaUtentePerRedirezioneSuContabilitaGeneraleAggiornaImpegno()) {
			model.setSaltaInserimentoPrimaNota(false);
			model.setRichiediConfermaRedirezioneContabilitaGenerale(true);
			return INPUT;
		}
		//
		
		
		if (requestAggiorna.getImpegno().getProgetto() != null) {
			Progetto progetto = requestAggiorna.getImpegno().getProgetto();
			
			progetto.setTipoProgetto(TipoProgetto.GESTIONE);
			progetto.setBilancio(sessionHandler.getBilancio());
			
			requestAggiorna.getImpegno().setIdCronoprogramma(model.getStep1Model().getIdCronoprogramma());
			requestAggiorna.getImpegno().setIdSpesaCronoprogramma(model.getStep1Model().getIdSpesaCronoprogramma());
		}
		
		AggiornaImpegnoResponse response = movimentoGestioneFinService.aggiornaImpegno(requestAggiorna); 
		
		if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)){
			
			model.setRichiediConfermaRedirezioneContabilitaGenerale(false);
			
			//per il modale di conferma per bypassare il controllo dodicesimi:
			if(!byPassDodicesimi && presenteSoloErroreDispDodicesimi(response)){
				setShowModaleConfermaSalvaConBypassDodicesimi(true);
				return INPUT;
			}
			
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

			
			Impegno impegnoReload = response.getImpegno();
			model.setImpegnoRicaricatoDopoInsOAgg(impegnoReload);
			forceReload = false;
			
			//OTTIMIZZAZIONI APRILE 2016, metto a false il parametro in modo che rieffettui il ricercaImpegnoPerChiave:
			//puo' risultare troppo lento dentro all'aggiorna impegno per molti sub
			caricaDati(false);
			//
			
			creaMovGestModelCache();
			
			//SIAC-6000
			if(model.isEnteAbilitatoGestionePrimaNotaDaFinanziaria()
					&& model.isSaltaInserimentoPrimaNota() 
					&& model.getUidDaCompletare() > 0){
				return GO_TO_GEN;
			}
			//
			
			return "gotoAggiorna";
		}	
	}  
		
	//SIAC-8825
	private List<Errore> validazioneCapitoliPerimetroSanitario (OggettoDaPopolareEnum oggettoDP){
		List<Errore> errori = new ArrayList<Errore>();
		if(null==teSupport.getPerimetroSanitarioSpesaSelezionato() || teSupport.getPerimetroSanitarioSpesaSelezionato().equals("")){
			 errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Capitoli perimetro sanitario")); 
		 }
		return errori;
	}
	
	public String confermaContoEconomico() {
		return SUCCESS;
	}
	
	public String confermaSiope() {
		return SUCCESS;
	}
	
	public String prosegui() {
		setMethodName("prosegui");
	    return "prosegui";
	}  
	
	public String indietroStep2(){
		setMethodName("indietro");
		
		forceReload=false;
		
		return "gotoAggiorna";
	}
	
	public String annulla() throws Exception {
	    return SUCCESS;
	}
	
	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */

	public String getNumeroImpegno() {
		return numeroImpegno;
	}

	public void setNumeroImpegno(String numeroImpegno) {
		this.numeroImpegno = numeroImpegno;
	}

	public String getAnnoImpegno() {
		return annoImpegno;
	}

	public void setAnnoImpegno(String annoImpegno) {
		this.annoImpegno = annoImpegno;
	}

	public boolean isSuccessStep2() {
		return successStep2;
	}

	public void setSuccessStep2(boolean successStep2) {
		this.successStep2 = successStep2;
	}

}
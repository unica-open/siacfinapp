/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceAccertamentiResponse;
import it.csi.siac.siacfinser.model.errore.ErroreFin;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisceAccertamentoStep2Action extends ActionKeyInserisceAccertamento {

	private static final long serialVersionUID = 1L;
	
	private static final String GO_TO_AGGIORNA = "gotoAggiorna";

	public InserisceAccertamentoStep2Action () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.ACCERTAMENTO;
	}
	
	/**
	 * in transazione elementare usa questo check facendo riferimento ai casi d'uso di aggiornamento
	 * 
	 * qui in inserimento devo ritornare sempre true
	 * 
	 * @return
	 */
	public boolean isAbilitatoAggiornamentoGenerico(){
		return true;
	}
	
	
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		this.model.setTitolo("Inserisce Accertamento - Classificazioni");
		if(null!=teSupport){
			teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.ACCERTAMENTO.toString());
		}
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		if(null!=teSupport){
			// copio il valore di cup dalla prima pagina
			teSupport.setDatiUscitaImpegno(datiUscitaImpegno());
			teSupport.setCup(model.getStep1Model().getCup());
		}else{
			// forzo il giro sulla prima pagina per 
			// errore filo di arianna JIra - 757
			return "erroreFiloArianna";
		}
		caricaMissione(model.getStep1Model().getCapitolo());
		caricaProgramma(model.getStep1Model().getCapitolo());

		// Jira - 1357 in caso di errore di caricamento dei dati
		// dei classificatori non viene segnalato alcun errore
		// ma carica la pagina, al massimo non verranno visualizzate le combo relative
		caricaListeFin(TIPO_ACCERTAMENTO_A);
		
		caricaLabelsInserisci(2);
		
		//disabilito il caricamento degli alberi inutili per questo scnario (in AjaxAction.java):
		teSupport.setRicaricaAlberoPianoDeiConti(true);
		//CR-2023 eliminato conto economico
		teSupport.setRicaricaStrutturaAmministrativa(false);
		teSupport.setRicaricaSiopeSpesa(true);
		
	    return SUCCESS;
	}   
	
	
	public String impostaPluriennalePrimeNoteEsercizioInCorso()  throws Exception {
		
		
		// mi limito a settare un flag e a richiamare il salva
		setPluriennalePrimeNoteEsercizioInCorso(true);
		setFromModaleConfermaAnnoDiCompetenzaInCorso(true);
		return salva();
		
	}
	
	
	public String impostaPluriennalePrimeNoteEsercizioFuturi()  throws Exception {
		
		
		// mi limito a settare un flag e a richiamare il salva
		setPluriennalePrimeNoteEsercizioInCorso(false);
		setFromModaleConfermaAnnoDiCompetenzaInCorso(true);
		return salva();
		
	}
	
	
	
	public String salva() throws Exception {
		setMethodName("salva");
		log.debug(methodName, "provo a salvare");
		
		//ricontrolliamo il siope:
		codiceSiopeChangedInternal(teSupport.getSiopeSpesaCod());
		
		//SIAC-6929
//		ProvvedimentoImpegnoModel pim = new ProvvedimentoImpegnoModel();
//		if(model.getStep1Model().getProvvedimento()!=null && model.getStep1Model().getProvvedimento().getUid()!=null){
//			pim = getProvvedimentoById(model.getStep1Model().getProvvedimento().getUid());
//			if(pim.getBloccoRagioneria()!= null && pim.getBloccoRagioneria().booleanValue()){
//				addErrore(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore("Numero Provvedimento " + 
//						pim.getNumeroProvvedimento() + " Oggetto " + pim.getOggetto()));
//				return INPUT;
//			}
//		}
		
		// CR-552: Prima di salvare l'accertamento verifico se devo rilanciare la modale di warning per il pluriennale
		// ma solo se l'anno del movimento è > dell'anno di bilancio
		// aggiungere la condizione sui titoli
		String codiceTitoloEntrata = model.getStep1Model().getCapitolo().getTitoloEntrata();
		String codiceTipologia  = model.getStep1Model().getCapitolo().getTipologia();
		
		boolean titoloTipologiaConfermaAnnoDiCompetenzaInCorso = false;
		if(codiceTitoloEntrata!=null){
			titoloTipologiaConfermaAnnoDiCompetenzaInCorso = codiceTitoloEntrata.equalsIgnoreCase(CLASS_CAPITOLO_TITOLO_4) && 
					codiceTipologia.equalsIgnoreCase(CLASS_CAPITOLO_TIPOLOGIA_4) ||
					codiceTitoloEntrata.equalsIgnoreCase(CLASS_CAPITOLO_TITOLO_5) ||
					codiceTitoloEntrata.equalsIgnoreCase(CLASS_CAPITOLO_TITOLO_6) ||
					codiceTitoloEntrata.equalsIgnoreCase(CLASS_CAPITOLO_TITOLO_7) ;
		}
		
		if((model.getStep1Model().getAnnoImpegno() > Integer.parseInt(sessionHandler.getAnnoEsercizio())) && titoloTipologiaConfermaAnnoDiCompetenzaInCorso){
			
			if (isFromModaleConfermaAnnoDiCompetenzaInCorso()) {
				
				setShowModaleConfermaAnnoDiCompetenzaInCorso(false);
				
				if(isPluriennalePrimeNoteEsercizioInCorso())
					model.getStep1Model().setAnnoScritturaEconomicoPatrimoniale(Integer.parseInt(sessionHandler.getAnnoEsercizio()));
				else 
					model.getStep1Model().setAnnoScritturaEconomicoPatrimoniale(null);
				
    		}else{
    			
				setShowModaleConfermaAnnoDiCompetenzaInCorso(true);
    			return INPUT;
			}
			
		}
		
		
		/**
		 *  nuovo confronto per capire il livello del PDC
		 */
		if(teSupport.getPianoDeiConti()!=null && teSupport.getPianoDeiConti().getTipoClassificatore()!=null && 
							!teSupport.getPianoDeiConti().getTipoClassificatore().getCodice().equalsIgnoreCase(V_LIVELLO_TIPO_CLASSIFICATORE)){
			//CR-2023 eliminato conto economico
			addErrore(ErroreFin.ELEM_PDC_NON_INDICATO.getErrore(""));
			return INPUT;
		}
		
		// controlli tabellina 4.6
		List<Errore> erroriAbilitazione = abilitazioneCampiTE(oggettoDaPopolare);
		if(null!=erroriAbilitazione && erroriAbilitazione.size()>0){
			addErrori(erroriAbilitazione);
			return INPUT;
		}
		copiaTransazioneElementareSupportSuModel(false);
		
		if(isNecessariaRichiestaConfermaUtentePerRedirezioneSuContabilitaGenerale()) {
			model.setSaltaInserimentoPrimaNota(false);
			model.setRichiediConfermaRedirezioneContabilitaGenerale(true);
			model.setMessaggioProsecuzioneSuContabilitaGenerale("&Egrave; possibile inserire una prima nota provvisoria o validarla ora.");
			return INPUT;
		}

		// aggiungo il warnig 
		
		InserisceAccertamentiResponse response =  inserisceAccertamento(model, false,null);
	
		if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
			debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return INPUT;
		}
		
		pulisciTransazioneElementare();
		
		// vai sulla pagina di aggiorna
		setNumeroAccertamento(String.valueOf(response.getElencoAccertamentiInseriti().get(0).getNumero())); 
		setAnnoAccertamento(String.valueOf(response.getElencoAccertamentiInseriti().get(0).getAnnoMovimento()));
		
		// messaggio di ok nella pagina aggiorna
		addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " 
				                   + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());

		clearActionData();
		ricaricaValoriDefaultStep1();

		//SIAC-5333
		model.setUidDaCompletare(response.getRegistrazioneMovFinFIN() != null? response.getRegistrazioneMovFinFIN().getUid() : 0);
		
		//controllo registrazione andata a buon fine:
		controlloRegistrazioneValidazionePrimaNota();
		//
		
		return model.isEnteAbilitatoGestionePrimaNotaDaFinanziaria() && model.isSaltaInserimentoPrimaNota() && model.getUidDaCompletare() != 0 ? GO_TO_GEN : GO_TO_AGGIORNA;

	}  
	/**
	 * Checks if is necessaria richiesta conferma utente per redirezione su contabilita generale.
	 * In fase si registrazione di impegni di spesa e di accertamenti di entrata: al momento del completamento dei dati finanziari
	 *  in oggetto (dopo il salva finale che rende definitivo l'impegno e l'accertamento)
	 *
	 * @return true, if is necessaria richiesta conferma utente per redirezione su contabilita generale
	 */
	private boolean isNecessariaRichiestaConfermaUtentePerRedirezioneSuContabilitaGenerale() {
		return model.isEnteAbilitatoGestionePrimaNotaDaFinanziaria()  && !model.isRichiediConfermaRedirezioneContabilitaGenerale() && model.isInserimentoSenzaSub()
				&& model.getStep1Model().getAnnoImpegno() == Integer.parseInt(sessionHandler.getAnnoEsercizio()) 
				&& !webAppSiNoToBool(model.getStep1Model().getFlagFattura())
				&& !webAppSiNoToBool(model.getStep1Model().getFlagCorrispettivo())
				&& model.isProvvedimentoDefinitivo();
	}

	public String prosegui() throws Exception {
		setMethodName("prosegui");
		
		// controllo sul V livello
		/**
		 *  nuovo confronto per capire il livello del PDC
		 */
		if(teSupport.getPianoDeiConti()!=null && teSupport.getPianoDeiConti().getTipoClassificatore()!=null && 
					!teSupport.getPianoDeiConti().getTipoClassificatore().getCodice().equalsIgnoreCase(V_LIVELLO_TIPO_CLASSIFICATORE)){
			//CR-2023 eliminato conto economico
			addErrore(ErroreFin.ELEM_PDC_NON_INDICATO.getErrore(""));
			return INPUT;
		}
		
		//SIAC-6929
		ProvvedimentoImpegnoModel pim = new ProvvedimentoImpegnoModel();
		if(model.getStep1Model().getProvvedimento()!=null && model.getStep1Model().getProvvedimento().getUid()!=null
				&& model.getStep1Model().getProvvedimento().getUid().intValue()>0){
			pim = getProvvedimentoById(model.getStep1Model().getProvvedimento().getUid());
			if(pim.getBloccoRagioneria()!= null && pim.getBloccoRagioneria().booleanValue()){
				addErrore(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore("Numero Provvedimento " + 
						pim.getNumeroProvvedimento() + " Oggetto " + pim.getOggetto()));
				return INPUT;
			}
		}
		
		// controlli tabellina 4.6
		List<Errore> erroriAbilitazione = abilitazioneCampiTE(oggettoDaPopolare);
		if(null!=erroriAbilitazione && erroriAbilitazione.size()>0){
			addErrori(erroriAbilitazione);
			return INPUT;
		}
		
		// effettua il salvataggio e poi passa ai pluriennali
		copiaTransazioneElementareSupportSuModel(false);
		InserisceAccertamentiResponse response =  inserisceAccertamento(model, false,null);
	
		if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
			debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return INPUT;
		}
		
		pulisciTransazioneElementare();
		
		// messaggio di ok nella terza pagina
		addPersistentActionMessage("FIN_INF_0070, Movimento inserito ( movimento=Accertamento, anno = " + response.getElencoAccertamentiInseriti().get(0).getAnnoMovimento() +  ", numero= "+ response.getElencoAccertamentiInseriti().get(0).getNumero() +" )");
		
		// salvo nel model step3 il numero e l'anno del  movimento di partenza, cosi da poterli e settare in fase di impostazione degli impegni pluriennali
		model.getStep3Model().setAnnoMovimentoInseritoInStep2(response.getElencoAccertamentiInseriti().get(0).getAnnoMovimento());
		model.getStep3Model().setNumeroMovimentoInseritoInStep2(response.getElencoAccertamentiInseriti().get(0).getNumero().intValue());
				
		sessionHandler.setParametro(FinSessionParameter.MOVGEST_INIZIALE, response.getElencoAccertamentiInseriti().get(0).getAnnoMovimento()+"||"+response.getElencoAccertamentiInseriti().get(0).getNumero());
		

		return "prosegui";
	}  
	
	public String annulla() throws Exception {
			// altri classificatori
			teSupport.setClassGenSelezionato1("");
			teSupport.setClassGenSelezionato2("");
			teSupport.setClassGenSelezionato3("");
			teSupport.setClassGenSelezionato4("");
			teSupport.setClassGenSelezionato5("");
		    return SUCCESS;
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
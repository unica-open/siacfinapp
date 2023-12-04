/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.util.ArrayList;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.ParametroConfigurazioneEnteEnum;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceImpegniResponse;
import it.csi.siac.siacfinser.model.errore.ErroreFin;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisceImpegnoStep2Action extends ActionKeyInserisceImpegno {

	private static final long serialVersionUID = 1L;
	
	
	private String numeroImpegno;
	private String annoImpegno;
	
	private String numeroImpegnoStruts;
	private String annoImpegnoStruts;
	
	//SIAC-5333
	private static final String GO_TO_AGGIORNA = "gotoAggiorna";
	
	
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
	
	public InserisceImpegnoStep2Action () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.IMPEGNO;
	}
	
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		this.model.setTitolo("Inserisce Impegno - Classificazioni");
		if(null!=teSupport){
			teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.IMPEGNO.toString());
		}
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		if(null!=teSupport){
			// copio il valore di cup dalla prima pagina
			teSupport.setCup(model.getStep1Model().getCup());
			teSupport.setDatiUscitaImpegno(datiUscitaImpegno());
		}else{
			// forzo il giro sulla prima pagina per 
			// errore filo di arianna JIra - 757
			return "erroreFiloArianna";
		}
		caricaMissione(model.getStep1Model().getCapitolo());
		caricaProgramma(model.getStep1Model().getCapitolo());
		caricaListaCofog(model.getStep1Model().getCapitolo().getIdProgramma());
		// Jira - 1357 in caso di errore di caricamento dei dati
		// dei classificatori non viene segnalato alcun errore
		// ma carica la pagina, al massimo non verranno visualizzate le combo relative
		caricaListeFin(TIPO_IMPEGNO_I);

		caricaLabelsInserisci(2);
		
		//disabilito il caricamento degli alberi inutili per questo scnario (in AjaxAction.java):
		teSupport.setRicaricaAlberoPianoDeiConti(true); 
		
		// CR-2023  eliminato conto economico
		
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
	
	public String salvaConByPassDodicesimi() throws Exception {
		return salvaInternal(true);
	}
	
	/**
	 * Wrapper di retrocompatibilita'
	 * @return
	 * @throws Exception
	 */
	public String salva() throws Exception {
		return salvaInternal(false);
	}
	
	//SIAC-8732
	public String proseguiSalva() throws Exception {
		return salva();
	}
	
	private String salvaInternal(boolean byPassDodicesimi) throws Exception {
		setMethodName("salva");
		log.debug(methodName, "provo a salvare");
		
		
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
		
		//TITOLO SPESA:
		String codiceTitoloSpesa = model.getStep1Model().getCapitolo().getTitoloSpesa();
		
		//CODICE MACRO AGGREGATO:
		String codiceMacroaggregato =  model.getStep1Model().getCapitolo().getCodiceMacroAggregato();
		
		//CONDIZIONE titolo conferma anno di competenza in corso:
		boolean titoloConfermaAnnoDiCompetenzaInCorso = codiceTitoloSpesa.equalsIgnoreCase(CLASS_CAPITOLO_TITOLO_3) ||
				codiceTitoloSpesa.equalsIgnoreCase(CLASS_CAPITOLO_TITOLO_4) && codiceMacroaggregato.equalsIgnoreCase(CLASS_CAPITOLO_MACROAGGREGATO_1) ||
				codiceTitoloSpesa.equalsIgnoreCase(CLASS_CAPITOLO_TITOLO_5) ;
		
		
		// CR-552: Prima di salvare l'impegno verifico se devo rilanciare la modale di warning per il pluriennale
		// ma solo se l'anno del movimento è > dell'anno di bilancio
		if((model.getStep1Model().getAnnoImpegno() > sessionHandler.getAnnoBilancio()) && titoloConfermaAnnoDiCompetenzaInCorso) {
			
			if (isFromModaleConfermaAnnoDiCompetenzaInCorso()) {
				
				setShowModaleConfermaAnnoDiCompetenzaInCorso(false);
				
				if(isPluriennalePrimeNoteEsercizioInCorso())
					model.getStep1Model().setAnnoScritturaEconomicoPatrimoniale(sessionHandler.getAnnoBilancio());
				else 
					model.getStep1Model().setAnnoScritturaEconomicoPatrimoniale(null);
				
    		}else{
    			
				setShowModaleConfermaAnnoDiCompetenzaInCorso(true);
    			return INPUT;
			}
			
			
			
		}
		
		//ricontrolliamo il siope:
		codiceSiopeChangedInternal(teSupport.getSiopeSpesaCod());
		//
		
		// controllo sul V livello
		/**
		 *  nuovo confronto per capire il livello del PDC
		 */
		if(teSupport.getPianoDeiConti()!=null && 
		   teSupport.getPianoDeiConti().getTipoClassificatore()!=null && 
		   !teSupport.getPianoDeiConti().getTipoClassificatore().getCodice().equalsIgnoreCase(V_LIVELLO_TIPO_CLASSIFICATORE)){
			
			// CR-2023  eliminato conto economico
			
			addErrore(ErroreFin.ELEM_PDC_NON_INDICATO.getErrore(""));
			
			return INPUT;
		}
		
		
		// controlli tabellina 4.6
		List<Errore> erroriAbilitazione = abilitazioneCampiTE(oggettoDaPopolare);
		if(null!=erroriAbilitazione && erroriAbilitazione.size()>0){
			addErrori(erroriAbilitazione);
			return INPUT;
		}
		//SIAC-8825
		if (isObbligatorioPerimetroSanitario()) {
			List<Errore> erroriCapitoliPerimentroSanitario = validazioneCapitoliPerimetroSanitario(oggettoDaPopolare);
			if (! erroriCapitoliPerimentroSanitario.isEmpty()) {
				addErrori(erroriCapitoliPerimentroSanitario);
				return INPUT;
			}
		}
		
		copiaTransazioneElementareSupportSuModel(false);
		
		boolean isSalvaSdF = salvaConSDF();
		//se devo saltare i 12, ho gia' chiesto se validare o meno la prima nota
		if(isNecessariaRichiestaConfermaUtentePerRedirezioneSuContabilitaGenerale()) {
			model.setSaltaInserimentoPrimaNota(false);
			model.setRichiediConfermaRedirezioneContabilitaGenerale(true);
			return INPUT;
		}
		
		InserisceImpegniResponse response = inserisceImpegno(model, false, isSalvaSdF, byPassDodicesimi, null);
	
		if(response!=null && response.isFallimento()){
			//per il modale di conferma per bypassare il controllo dodicesimi:
			if(!byPassDodicesimi && presenteSoloErroreDispDodicesimi(response)){
				setShowModaleConfermaSalvaConBypassDodicesimi(true);
				return INPUT;
			}
			//non dovrebbe arrivare mai qui
			model.setRichiediConfermaRedirezioneContabilitaGenerale(false);
			debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return INPUT;
		} else if(response!=null  && !response.isFallimento() && (response.getErrori() != null && response.getErrori().size() > 0)){
			     // NO FALLIMENTO SI ERRORI ---> WARNING
				debug(methodName, "Errore nella risposta del servizio");
				for (Errore warning : response.getErrori()) {
					addPersistentActionWarning(warning.getCodice() +"-"+warning.getDescrizione());
				}
		}
		
		model.setRichiediConfermaRedirezioneContabilitaGenerale(false);
		
		pulisciTransazioneElementare();
		
		// vai sulla pagina di aggiorna
		setNumeroImpegno(String.valueOf(response.getElencoImpegniInseriti().get(0).getNumeroBigDecimal()));
		setAnnoImpegno(String.valueOf(response.getElencoImpegniInseriti().get(0).getAnnoMovimento()));
		
		setNumeroImpegnoStruts(String.valueOf(response.getElencoImpegniInseriti().get(0).getNumeroBigDecimal()));
		setAnnoImpegnoStruts(String.valueOf(response.getElencoImpegniInseriti().get(0).getAnnoMovimento()));
	
		
		// messaggio di ok nella pagina aggiorna
		if(!isSalvaSdF){
			addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " 
	                   + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		} else {
			addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " 
	                   + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione() + ": IMPEGNO SENZA DISPONIBILITA' DI FONDI");
		}
		
		
		clearActionData();	
		ricaricaValoriDefaultStep1();
		//SIAC-5333
		model.setUidDaCompletare(response.getRegistrazioneMovFinFIN() != null? response.getRegistrazioneMovFinFIN().getUid() : 0);
		
		//controllo registrazione andata a buon fine:
		controlloRegistrazioneValidazionePrimaNota();
		//
		
		return model.isEnteAbilitatoGestionePrimaNotaDaFinanziaria() && model.isSaltaInserimentoPrimaNota() && model.getUidDaCompletare() != 0 ? GO_TO_GEN : GO_TO_AGGIORNA;
	}  
	
	public String prosegui() throws Exception {
		setMethodName("prosegui");
		
		// controllo sul V livello
		/**
		 *  nuovo confronto per capire il livello del PDC
		 */
		if(teSupport.getPianoDeiConti()!=null && 
		   teSupport.getPianoDeiConti().getTipoClassificatore()!=null && 
		   !teSupport.getPianoDeiConti().getTipoClassificatore().getCodice().equalsIgnoreCase(V_LIVELLO_TIPO_CLASSIFICATORE)){
			
			// CR-2023  eliminato conto economico
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
		
		
		InserisceImpegniResponse response =  inserisceImpegno(model, false,null);
	
		if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
			debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return INPUT;
		}
		
		pulisciTransazioneElementare();
		
		// messaggio di ok nella terza pagina
		addPersistentActionMessage("FIN_INF_0070, Movimento inserito ( movimento=Impegno, anno = " + response.getElencoImpegniInseriti().get(0).getAnnoMovimento() +  ", numero= "+ response.getElencoImpegniInseriti().get(0).getNumeroBigDecimal() +" )");
		
		// salvo nel model step3 il numero e l'anno del  movimento di partenza, cosi da poterli e settare in fase di impostazione degli impegni pluriennali
		model.getStep3Model().setAnnoMovimentoInseritoInStep2(response.getElencoImpegniInseriti().get(0).getAnnoMovimento());
		model.getStep3Model().setNumeroMovimentoInseritoInStep2(response.getElencoImpegniInseriti().get(0).getNumeroBigDecimal().intValue());
		
		sessionHandler.setParametro(FinSessionParameter.MOVGEST_INIZIALE, response.getElencoImpegniInseriti().get(0).getAnnoMovimento()+"||"+response.getElencoImpegniInseriti().get(0).getNumeroBigDecimal());
		
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
	 * Checks if is necessaria richiesta conferma utente per redirezione su contabilita generale.
	 * In fase si registrazione di impegni di spesa e di accertamenti di entrata: al momento del completamento dei dati finanziari
	 *  in oggetto (dopo il salva finale che rende definitivo l'impegno e l'accertamento)
	 *
	 * @return true, if is necessaria richiesta conferma utente per redirezione su contabilita generale
	 */
	private boolean isNecessariaRichiestaConfermaUtentePerRedirezioneSuContabilitaGenerale() { 
		//l'utente deve essere abilitato
		return model.isEnteAbilitatoGestionePrimaNotaDaFinanziaria() 
				&& (!model.isRichiediConfermaRedirezioneContabilitaGenerale()) 
				&& model.getStep1Model().getAnnoImpegno() == sessionHandler.getAnnoBilancio()
				&& model.isInserimentoSenzaSub() 
				//non duplico il controllo
//				&& model.isClassificazioneSpesaCorrettaPerContabilitaGenerale() 
				&& model.isProvvedimentoDefinitivo();
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

	public String getNumeroImpegnoStruts() {
		return numeroImpegnoStruts;
	}

	public void setNumeroImpegnoStruts(String numeroImpegnoStruts) {
		this.numeroImpegnoStruts = numeroImpegnoStruts;
	}

	public String getAnnoImpegnoStruts() {
		return annoImpegnoStruts;
	}

	public void setAnnoImpegnoStruts(String annoImpegnoStruts) {
		this.annoImpegnoStruts = annoImpegnoStruts;
	}
	
	//SIAC-8825
	private List<Errore> validazioneCapitoliPerimetroSanitario (OggettoDaPopolareEnum oggettoDP){
		List<Errore> errori = new ArrayList<Errore>();
		if(null==teSupport.getPerimetroSanitarioSpesaSelezionato() || teSupport.getPerimetroSanitarioSpesaSelezionato().equals("")){
			errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Capitoli perimetro sanitario")); 
		}
		return errori;
	}
		
}	
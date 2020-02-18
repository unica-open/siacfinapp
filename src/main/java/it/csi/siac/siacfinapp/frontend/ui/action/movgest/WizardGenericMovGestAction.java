/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincoloResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.StatoOperativoProgetto;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siacbilser.model.Vincolo;
import it.csi.siac.siacbilser.model.VincoloCapitoli;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.DettaglioVincoloModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciImpegnoStep1Model;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciMovGestModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProgettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.EsistenzaProgetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.EsistenzaProgettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Avanzovincolo;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamento;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

public class WizardGenericMovGestAction extends MovGestAction<GestisciMovGestModel> {

	private static final long serialVersionUID = 1L;
	
	protected static final String GOTO_GESTIONE_SUBIMPEGNO = "gotoGestioneSubimpegno";
	protected static final String GOTO_SUBIMPEGNO_STEP2 = "gotoSubimpegnoStep2";
	protected static final String GOTO_AGGIORNA_SUBIMPEGNO = "gotoAggiornaSubimpegno";
	protected static final String GOTO_GESTIONE_SUBACCERTAMENTO = "gotoGestioneSubaccertamento";
	protected static final String GOTO_SUBACCERTAMENTO_STEP2 = "gotoSubaccertamentoStep2";
	protected static final String GOTO_AGGIORNA_SUBACCERTAMENTO = "gotoAggiornaSubaccertamento";
	protected static final String GOTO_CONTABILITA_GENERALE = "gotoContabilitaGenerale";
	
	protected final static String ABILITAZIONE_GESTIONE = CodiciOperazioni.OP_SPE_gestisciImpegno;
	protected final static String ABILITAZIONE_GESTIONE_DECENTRATO = CodiciOperazioni.OP_SPE_gestisciImpegnoDecentrato;
	protected final static String ABILITAZIONE_GESTIONE_DECENTRATO_P = CodiciOperazioni.OP_SPE_gestisciImpegnoDecentratoP;
	
	protected boolean inserimentoSubimpegno;
	protected Integer idSubImpegno;
	private GestisciImpegnoStep1Model oggetto= new GestisciImpegnoStep1Model();
	
	
	protected static final String MOVGEST_PROVVISORIO = "PROVVISORIO";
	protected static final String STATO_MOVGEST_DEFINITIVO = "DEFINITIVO";
	protected static final String MOVGEST_DEFINITIVO_NON_LIQUIDABILE = "DEFINITIVO NON LIQUIDABILE";
	
	protected static final String CODICE_STATO_MOVGEST_PROVVISORIO = "p";
	protected static final String CODICE_STATO_MOVGEST_DEFINITIVO = "D";
	protected static final String CODICE_STATO_MOVGEST_DEFINITIVO_NON_LIQUIDABILE = "N";
	
	protected static final String TIPO_IMPEGNO_I = "I";
	protected static final String TIPO_ACCERTAMENTO_A = "A";
	
	protected static final String FORM = "FORM";
	protected static final String LABEL_TITOLO = "TITOLO";
	protected static final String LABEL_STEP1 = "STEP1";
	protected static final String LABEL_STEP3 = "STEP3";
	protected static final String LABEL_OGGETTO_GENERICO = "OGGETTO_GENERICO";
	protected static final String LABEL_OGGETTO_GENERICO_LOWER_CASE = "OGGETTO_GENERICO_LOWER_CASE";
	
	protected static final String LABEL_TAB_SUB = "TAB_SUB";
	protected static final String LABEL_TAB_AGG_ACC = "TAB_AGG_ACC";
	protected static final String LABEL_TAB_MOV_SPE = "TAB_MOV_SPE";
	protected static final String LABEL_TAB_SUB_LINK = "TAB_SUB_LINK";
	protected static final String LABEL_TAB_AGG_ACC_LINK = "TAB_AGG_ACC_LINK";
	protected static final String LABEL_TAB_MOV_SPE_LINK = "TAB_MOV_SPE_LINK";

	protected static final String LABEL_OGGETTO_GENERICO_PADRE = "OGGETTO_GENERICO_PADRE";
	protected static final String LABEL_OGGETTO_GENERICO_PADRE_LOWER_CASE = "OGGETTO_GENERICO_PADRE_LOWER_CASE";
	protected static final String LABEL_OGGETTO_GENERICO_VERBO = "OGGETTO_GENERICO_VERBO";
	protected static final String LABEL_INSERISCI = "INSERISCI";
	
	protected static final String PROVENIENZA_STILO = "STILO";
	
	// pop up di ricerca accertamenti per vincoli
	public String radioCodiceAccPerVincoli;
    protected static final String GOTO_RICERCA_ACC_VINCOLI ="ricercaAccPerVincoli";  
    protected Accertamento accertamentoPerVincolo;
    
	
    // campi utilizzati per la gestione dei vincoli
 	private String annoAccDaPassare;
 	private String numeroAccDaPassare;
 	private String annoAccPerAggiorna;
 	private String numeroAccPerAggiorna;
 	protected final String DETTAGLIO_VINCOLO = "dettaglioVincolo";
 	protected final String DETTAGLIO_AVANZO_VINCOLO = "dettaglioAvanzoVincolo";
 	
 	private String uidAvanzoVincoloDaPassare;
 	private String uidAvanzoVincoloPerAggiorna;
 	
 	public boolean showModaleConfermaSalvaConBypassDodicesimi=false;
 	public boolean fromModaleConfermaSalvaConBypassDodicesimi=false;
    
 	//scelta tra accertamento e avanzo vincolo:
 	protected static final String SCELTA_ACCERTAMENTO = "Accertamento";
 	protected static final String SCELTA_AVANZO = "FPV / Avanzo";
 	private final String DETTAGLIO_IMPORTO_RESIDUO_AVANZO_SELEZIONATO = "dettaglioImportoResiduoAvanzoSelezionato";
 	//
 	
 	//SIAC-6000
 	protected static final String GO_TO_GEN = "gotoContabilitaGenerale";
 	//
 	
 	/**
 	 * Metodo che mette a fattor comune la gestione del fallimento della registrazione della prima nota.
 	 * 
 	 * Da richiamare dopo aver richiamato il servizio che ci si aspetta che debba valorizzare UidDaCompletare.
 	 * 
 	 */
 	protected void controlloRegistrazioneValidazionePrimaNota(){
 		//se l'ente e' abilitato alla gestione delle prima nota
 		//se l'utente ha confermato
 		//se non e' stato generato un uid
 		if(model.isEnteAbilitatoGestionePrimaNotaDaFinanziaria() && model.isSaltaInserimentoPrimaNota() && model.getUidDaCompletare() == 0) {
 			cleanErrori();
 			//addErrore(ErroreGEN.OPERAZIONE_NON_CONSENTITA.getErrore(WebAppConstants.CONDIZIONI_REGISTRAZIONE_NON_SODDISFATTE));
 			addWarning(ErroreGEN.OPERAZIONE_NON_CONSENTITA.getErrore(WebAppConstants.CONDIZIONI_REGISTRAZIONE_NON_SODDISFATTE));
 			setErroriInSessionePerActionSuccessiva();
 			setWarningInSessionePerActionSuccessiva();
 		}
 	}
 	
 	
 	/**
 	 * imposta i dati del movimento da cui leggere dentro al model in cui impostare
 	 * @param movimentoDaCuiLeggere
 	 * @param modelInCuiImpostare
 	 */
 	protected void impostaDatiSiopePlusNelModel(MovimentoGestione movimentoDaCuiLeggere,GestisciImpegnoStep1Model modelInCuiImpostare){
		//motivazione assenza cig
		modelInCuiImpostare.setMotivazioneAssenzaCig(codiceMotivazioneAssenzaCig(movimentoDaCuiLeggere.getSiopeAssenzaMotivazione()));
		
		//tipo debito siope
		boolean commercialeConFatture = true;
		modelInCuiImpostare.setTipoDebitoSiope(valoreSiopeTipoDebitoPerRadioButton(movimentoDaCuiLeggere.getSiopeTipoDebito(),commercialeConFatture));
	}
 	
 	/**
	 * Messi a fattor comune i controlli di formato su CIG, CUP e nuovi campi SIOPE PLUS
	 * 
	 * Fa l'addError degli eventuli errori
	 * 
	 * @param step1Model
	 */
	protected void controlliCigCupESiopePlus(GestisciImpegnoStep1Model step1Model){
		
		//instazio la lista errori:
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		//metodo core che si basa sulla lista errori:
		controlliCigCupESiopePlus(step1Model, listaErrori);
		
		//aggiungo gli errori in pagina:
		if(!isEmpty(listaErrori)){
			addErrori(listaErrori);
		}
	}
	
	/**
	 * Messi a fattor comune i controlli di formato su CIG, CUP e nuovi campi SIOPE PLUS
	 * 
	 * Aggiunge gli eventuali errori a listaErrori
	 * @param step1Model
	 * @param listaErrori
	 */
	protected void controlliCigCupESiopePlus(GestisciImpegnoStep1Model step1Model, List<Errore> listaErrori ){
		// controllo di congruenza di CIG e CUP
	    if(step1Model.getCig()!=null && StringUtils.isNotEmpty(step1Model.getCig())){
	    	// controllo di congruenza CIG
	    	if(!FinUtility.cigValido(step1Model.getCig())){
	    		listaErrori.add(ErroreFin.FORMATO_NON_VALIDO_SECONDO.getErrore("CIG"));
	    	}
	    }
	    if(step1Model.getCup()!=null && StringUtils.isNotEmpty(step1Model.getCup())){
	    	// controllo di congruenza CIG
	    	if(!FinUtility.cupValido(step1Model.getCup())){
	    		listaErrori.add(ErroreFin.FORMATO_NON_VALIDO_SECONDO.getErrore("CUP"));
	    	}
	    } 
	    
	    //SIOPE PLUS:
	    if(FinStringUtils.entrambiValorizzati(step1Model.getCig(),step1Model.getMotivazioneAssenzaCig())){
	    	//CIG e Motivazione assenza CIG non possono coesistere
	    	listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("CIG", " (Compilare un solo campo tra CIG e Motivazione assenza CIG)"));
	    }
	    
	    if(isTipoCommerciale(step1Model.getTipoDebitoSiope())
	    		&& FinStringUtils.entrambiVuoti(step1Model.getCig(),step1Model.getMotivazioneAssenzaCig())){
	    	//SE Tipo SIOPE = Commerciale
	        //CIG o, in alternativa, Motivazione assenza CIG sono obbligatori 
	    	listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Tipo debito siope", " (Per tipo debito Commerciale occorre indicare il CIG oppure la Motivazione assenza CIG )"));
	    }
	}
 	
 	protected void initSceltaAccertamentoAvanzoList(){
 		ArrayList<String> sceltaAccertamentoAvanzoList = new ArrayList<String>();
 		sceltaAccertamentoAvanzoList.add(SCELTA_ACCERTAMENTO);
 		sceltaAccertamentoAvanzoList.add(SCELTA_AVANZO);
 		model.getStep1Model().setSceltaAccertamentoAvanzoList(sceltaAccertamentoAvanzoList);
 		model.getStep1Model().setTipoVincolo(SCELTA_ACCERTAMENTO);//default
 	}
 	
 	public String dettaglioImportoResiduoAvanzoSelezionato(){
 		HttpServletRequest request = ServletActionContext.getRequest();
 		String idAvanzo = request.getParameter("idAvanzo");
 		model.getStep1Model().setAvanzoVincoloSelezionato(idAvanzo);
 		return DETTAGLIO_IMPORTO_RESIDUO_AVANZO_SELEZIONATO;
 	}
 	
 	public BigDecimal getImportoResiduoAvanzoVincoloSelezionato(){
 		BigDecimal importo = null;
 		Avanzovincolo avanzovincolo = getAvanzoVincoloSelezionato();
 		if(avanzovincolo!=null && avanzovincolo.getDisponibileAvanzovincolo()!=null){
 			importo = avanzovincolo.getDisponibileAvanzovincolo();
 		}
 		return importo;
 	}
 	
 	protected Avanzovincolo getAvanzoVincoloSelezionato(){
 		Avanzovincolo avanzovincolo = null;
 		String selezionato = model.getStep1Model().getAvanzoVincoloSelezionato();
 		if(!StringUtils.isEmpty(selezionato)){
 			Integer idAvanzoVincolo = new Integer(selezionato);
 			avanzovincolo = FinUtility.getById(model.getListaAvanzovincolo(), idAvanzoVincolo);
 		}
 		return avanzovincolo;
 	}
 	
    
 	protected static final String IMP_NON_TUTTO_VINCOLATO = "Impegno vincolato non completamente collegato alle entrate: al salvataggio verra' salvato con importo liquidabile = 0";
	/**
	 * true se IMPEGNO, false se ACCERTAMENTO
	 * @return boolean
	 */
	public boolean oggettoDaPopolareImpegno() {
		return oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO);
	}
		
	/**
	 * CONVERTITORI STATO IMPEGNO/SUBIMPEGNO 
	 */
	
	protected String convertiStatoToCodifica(String stato) {
		String codifica = null;
		if (stato != null && !"".equalsIgnoreCase(stato)) {
			if (MOVGEST_PROVVISORIO.equalsIgnoreCase(stato)) {
				codifica = Constanti.MOVGEST_STATO_PROVVISORIO;
			} else if (STATO_MOVGEST_DEFINITIVO.equalsIgnoreCase(stato)) {
				codifica = Constanti.MOVGEST_STATO_DEFINITIVO;
			} else if (MOVGEST_DEFINITIVO_NON_LIQUIDABILE.equalsIgnoreCase(stato)) {
				codifica = Constanti.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE;
			} 
		}
		return codifica;
	}
	
	

	
	
	protected List<Errore> controlloAnni(){
		List<Errore> listaErrori= new ArrayList<Errore>();
		if(model.getStep1Model().getCapitolo().getAnno()!= null &&!(model.getStep1Model().getCapitolo().getAnno()>1900)){
			 listaErrori.add(ErroreCore.PARAMETRO_ERRATO.getErrore("Anno Capitolo "));
		}
		if(model.getStep1Model().getAnnoImpegno()!= null && !(model.getStep1Model().getAnnoImpegno()>1900)){
		    	 listaErrori.add(ErroreCore.PARAMETRO_ERRATO.getErrore(model.getLabels().get(LABEL_OGGETTO_GENERICO),model.getStep1Model().getAnnoImpegno().toString(), "> 1900" ));
		    }
		if(model.getStep1Model().getProvvedimento().getAnnoProvvedimento()!= null && !(model.getStep1Model().getProvvedimento().getAnnoProvvedimento()>1900)){
	    	 listaErrori.add(ErroreCore.PARAMETRO_ERRATO.getErrore("Anno Provvedimento ", model.getStep1Model().getProvvedimento().getAnnoProvvedimento().toString(), "> 1900"));
	    }
		if(LABEL_OGGETTO_GENERICO.equalsIgnoreCase("Impegno")){
		    if(model.getStep1Model().getAnnoFinanziamento()!=null && !(model.getStep1Model().getAnnoFinanziamento()>1900)){
		    	 listaErrori.add(ErroreCore.PARAMETRO_ERRATO.getErrore("Anno Finanziamento "));
		    }
		}
	    if(model.getStep1Model().getAnnoImpRiacc()!= null && !(model.getStep1Model().getAnnoImpRiacc()>1900)){
	    	 listaErrori.add(ErroreCore.PARAMETRO_ERRATO.getErrore("Anno Riaccertamento ",model.getStep1Model().getAnnoImpRiacc().toString(),">1900" ));
	    }
	    if(model.getStep1Model().getAnnoImpOrigine()!= null && !(model.getStep1Model().getAnnoImpOrigine()>1900)){
	    	 listaErrori.add(ErroreCore.PARAMETRO_ERRATO.getErrore("Anno Impegno di Origine ", model.getStep1Model().getAnnoImpOrigine().toString(), ">1900"));
	    }
	    return listaErrori;
	}
	
	protected void creaMovGestModelCache(){
		model.setStep1ModelCache(clone(model.getStep1Model()));
		model.setTransazioneElementareCache(clonaTransazioneElementare());
		model.setStep3ModelCache(clone(model.getStep3Model()));
		model.setStep1ModelSubimpegnoCache(clone(model.getStep1ModelSubimpegno()));
		model.setMovimentoSpesaModelCache(clone(model.getMovimentoSpesaModel()));
	}
	
	protected String convertiCodificaToStato(String codifica) {
		String stato = null;
		if (codifica != null && !"".equalsIgnoreCase(codifica)) {
			if (Constanti.MOVGEST_STATO_PROVVISORIO.equalsIgnoreCase(codifica)) {
				stato = MOVGEST_PROVVISORIO;
			} else if (Constanti.MOVGEST_STATO_DEFINITIVO.equalsIgnoreCase(codifica)) {
				stato = STATO_MOVGEST_DEFINITIVO;
			} else if (Constanti.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equalsIgnoreCase(codifica)) {
				stato = MOVGEST_DEFINITIVO_NON_LIQUIDABILE;
			} 
		}
		return stato;
	}

	protected void ricaricaValoriDefaultStep1(){
		if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
			model.getStep1Model().getCapitolo().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
			model.getCapitoloRicerca().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
			model.getStep1Model().setAnnoImpegno(Integer.parseInt(sessionHandler.getAnnoEsercizio()));
		}
		model.getStep1Model().setRiaccertato(WebAppConstants.No);
		model.getStep1Model().setSoggettoDurc(WebAppConstants.No);
		model.getStep1Model().setPluriennale(WebAppConstants.No);
		model.getStep1Model().setFlagFattura(WebAppConstants.No);
		model.getStep1Model().setFlagCorrispettivo(WebAppConstants.No);
		model.getStep1Model().setFlagAttivaGsa(WebAppConstants.No);
		model.getStep1Model().setStato(MOVGEST_PROVVISORIO);
	}
	
	public <IC extends ImportiCapitolo, ICE extends ImportiCapitolo> boolean controlloSACUtenteDecentratoCapUscitaGest(String azioneCercata, Capitolo<IC, ICE> capitolo) {
		boolean strutturaCompatibileCapitolo = false;
		if (isAzioneDecentrata(azioneCercata)) {
			
			//VERIFICO SE TRA LE STRUTTRE DELL'AZIONE VI E' QUELLA DEL CAPITOLO:
			AzioneConsentita azioneConsentita = azioneDecConsentitaIsPresentObj(azioneCercata);
			
			if (azioneConsentita != null) {
				List<StrutturaAmministrativoContabile> strutture = getStruttureAmministrativoContabileByAccount();
				if (strutture != null && strutture.size() > 0) {
					for (StrutturaAmministrativoContabile currentStruttura : strutture) {
						if (currentStruttura.getUid() == capitolo.getStrutturaAmministrativoContabile().getUid()) {
							strutturaCompatibileCapitolo = true;
							break;
						}
					}
				}
				
				if(!strutturaCompatibileCapitolo){
					//IN ALTERNATIVA VEDIAMO SE IL CAPITOLO HA DELLE DEROGHE DI VISIBILITA ALLA REGOLA PRINCIPALE:
					strutturaCompatibileCapitolo = controlloCapitoloVisibilitaStruttura(azioneConsentita, capitolo.getUid());
				}
				
			}
			
		} else {
			strutturaCompatibileCapitolo = true;
		}
		return strutturaCompatibileCapitolo;
	}
	
	/**
	 * Metodo che viene invocato all'onchange del campo di input del codice del progetto al
	 * fine di verificare se il codice corrisponde ad un progetto esistente
	 * @return
	 */
	public String codiceProgettoChanged(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String cod = request.getParameter("id");
	
		boolean beccato = false;
		//request per ricerca puntuale:
		RicercaPuntualeProgetto ricercaPuntualeProgetto = requestRicercaProgettoPerCodiceChanged(cod);
				
		if(ricercaPuntualeProgetto!=null){
			RicercaPuntualeProgettoResponse respPuntuale = progettoService.ricercaPuntualeProgetto(ricercaPuntualeProgetto);
			if(respPuntuale!=null && !respPuntuale.isFallimento() && respPuntuale.getProgetto()!=null){
				Progetto progettoTrovato = respPuntuale.getProgetto();
				if(progettoTrovato.getCodice()!=null && progettoTrovato.getCodice().equalsIgnoreCase(cod)){
					//beccato preciso
					popolaProgetto(progettoTrovato);
					beccato = true;
				}
			}
		}
		
		if(!beccato){
			Progetto progetto  = new Progetto();
			progetto.setCodice(cod);
			progetto.setDescrizione(null);
			popolaProgetto(progetto);
		}
		
 		return "labelProgetto";
	}
	
	/**
	 * Costruisce la requeste per ricerca puntualmente un progetto tramite il codice digitato nel txtfield
	 * @param codice
	 * @return
	 */
	private RicercaPuntualeProgetto requestRicercaProgettoPerCodiceChanged(String codice) {
		RicercaPuntualeProgetto ricercaPuntualeProgetto = null;
		if(codice!=null && !StringUtils.isEmpty(codice)){
			ricercaPuntualeProgetto = new RicercaPuntualeProgetto();
			ricercaPuntualeProgetto.setRichiedente(sessionHandler.getRichiedente());
			Progetto progetto = new Progetto();
			progetto.setEnte(sessionHandler.getEnte());
			progetto.setCodice(codice);
			progetto.setStatoOperativoProgetto(StatoOperativoProgetto.VALIDO);
			ricercaPuntualeProgetto.setProgetto(progetto);
		}
		return ricercaPuntualeProgetto;
	}
	
	/**
	 * Metodo che viene invocato all'onchange del campo di input dell'importo di una modifica di importo al
	 * fine di verificare se la disponibilita' capitolo e' superata o e' rientrata nel caso di SDF
	 * 
	 * Serve a cambiare al volo il pulsante salva con il punsalte salva che passa tramite invocazione di pop-up di conferma
	 * 
	 * @return
	 */
	public String importoModificaChanged(){
		HttpServletRequest request = ServletActionContext.getRequest();
		
		String impMod = request.getParameter("impMod");
		String cmptnz = request.getParameter("cmptnz");
		String abbchckd = request.getParameter("abbchckd");
		
		if(abbchckd!=null){
			if("true".equalsIgnoreCase(abbchckd)){
				model.setAbbchckd(true);
			} else {
			   model.setAbbchckd(false);
			}
		}
		
		model.setCmptnz(cmptnz);
		
		if(!FinStringUtils.isEmpty(impMod)){
			try{
				model.setImpMod(new BigDecimal(impMod));
			} catch (NumberFormatException nfe){
			}
		}
		
 		return "pulsantiSalvaModificaImporto";
	}
	
	/**
	 * Per verificare se il codice corrisponde ad un siope esistente
	 * @param cod
	 */
	@Override
	protected void codiceSiopeChangedInternal(String cod){
		TipiLista tipoList = null;
		if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO) || oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBIMPEGNO)){
			tipoList = TipiLista.TIPO_SIOPE_SPESA_I;
		} else if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ACCERTAMENTO) || oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBACCERTAMENTO)){
			tipoList = TipiLista.TIPO_SIOPE_ENTRATA_I;
		}
		//richiamo il metodo centralizzato 
		//passandogli la tipologia di lista riguardante 
		//il particolare oggettoDaPopolare:
		codiceSiopeChangedInternal(cod, tipoList);
	}
	
	/**
	 * Metodo abilitazione generica all'aggiornamento o annullamento di un impegno.
	 * 
	 * Non deve essere considerata l'abilitazione aggiorna GSA che va trattata in maniera indipendente.
	 * 
	 * @param impegno
	 * @return
	 */
	protected boolean isAbilitatoAggiornaAnnullaImpegno(Impegno impegno){
		
		boolean isAbilitato=false;

		if (isAbilitatoGestisciImpegno()) {
			//l'abilitazione a OP_SPE_gestisciImpegno
			//ci rende automaticamente abilitati
			isAbilitato=true;
		} else {
			if (isAbilitatoGestisciImpegnoDec()) {

				if(impegno != null && impegno.getUid() > 0){
					
					if(impegno.getStatoOperativoMovimentoGestioneSpesa().equals(Constanti.MOVGEST_STATO_PROVVISORIO) &&
							impegno.getAttoAmministrativo()!=null 
							&& Boolean.TRUE.equals(impegno.getAttoAmministrativo().getParereRegolaritaContabile())){
						//impegno e' provvisorio  e il provvedimento a lui collegato (se esiste) ha FlagParereRegolarita a TRUE
						return false;
					}
					
					isAbilitato=controlloSACUtenteDecentratoCapUscitaGest(CodiciOperazioni.OP_SPE_gestisciImpegnoDecentrato, impegno.getCapitoloUscitaGestione());
				}
			}
		}
	
		return isAbilitato;
	}
	
	/**
	 * Rm SIAC-5371
	 * funzione utilizzata su jsp per abilitazione o meno di funzionlità/ parti di jsp.
	 * @return true, if is abilitato tab modifiche
	 *
	 * @deprecated deprecato in occasione della SIAC-7360, pare non essere mai usato
	 * 
	 */
	@Deprecated
	public boolean isAbilitatoTabModifiche(){

		boolean isAbilitato= false;
		
		if(isAbilitatoGestisciImpegno()){
			
			if(isAbilitatoGestisciImpegnoDecentratoP()){
				
				if(isBilancioAttualeInPredisposizioneConsuntivo() &&
					isAbilitatoGestisciImpegnoRiaccertato()){
						isAbilitato = true;
				}else{
					
					
					if(oggettoDaPopolareImpegno() || oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBIMPEGNO)){
						
						if(model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa().equals(Constanti.MOVGEST_STATO_PROVVISORIO))
							return isAbilitato = true;
					}
				}
				
			} //else isAbilitato = false;
			
			isAbilitato = true;
		} 
		
		return isAbilitato;
		
	}
	
	/**
	 * Abilita lista ROR.
	 *
	 * @return true, if successful
	 */
	public boolean abilitaListaROR(){
		
		boolean abilitaListaROR = false;
		
		if(isAbilitatoGestisciImpegnoDecentratoP() && isBilancioAttualeInPredisposizioneConsuntivo() &&
				isAbilitatoGestisciImpegnoRiaccertato()){
			//rendo abilitata la lista ROR
			abilitaListaROR = true;
		}
		
		return abilitaListaROR;
	}
	
	public boolean abilitaListaRORAccertamento(){
		
		boolean abilitaListaROR = false;
		
		if(isAbilitatoGestisciAccertamentoDecentratoP() && isBilancioAttualeInPredisposizioneConsuntivo() &&
				isAbilitatoGestisciAccertamentoRiaccertato()){
			//rendo abilitata la lista ROR
			abilitaListaROR = true;
		}
		
		return abilitaListaROR;
	}
	
	
	protected boolean isAbilitatoAggiornaAnnullaAccertamento(Accertamento accertamento){
		boolean isAbilitato=false;

		if (isAbilitatoGestisciAccertamento()) {
			//l'abilitazione a OP_ENT_gestisciAccertamento
			//ci rende automaticamente abilitati
			isAbilitato=true;
		} else {
			if (isAbilitatoGestisciAccertamentoDec()) {

				if(accertamento != null && accertamento.getUid()>0){
					
					if(accertamento.getStatoOperativoMovimentoGestioneEntrata().equals(Constanti.MOVGEST_STATO_PROVVISORIO) &&
							accertamento.getAttoAmministrativo()!=null 
							&& Boolean.TRUE.equals(accertamento.getAttoAmministrativo().getParereRegolaritaContabile())){
						//impegno e' provvisorio  e il provvedimento a lui collegato (se esiste) ha FlagParereRegolarita a TRUE
						return false;
					}
					
					isAbilitato=controlloSACUtenteDecentratoCapUscitaGest(CodiciOperazioni.OP_ENT_gestisciAccertamentoDecentrato, accertamento.getCapitoloEntrataGestione());
				}
			}
		}

		return isAbilitato;
	}
	
	
	public boolean isAzioneDecentrato(){		
		return sessionHandler.getAzione().getNome().endsWith("Decentrato"); 
	}

	
	//Soggetto Decentrato
	public boolean isSoggettoDecentrato(){
		return isAzioneAbilitata(ABILITAZIONE_GESTIONE_DECENTRATO);
	}
	
	
	//Metodi per la profilazione degli impegni
	public boolean isAbilitatoGestisciImpegno(){
		return isAzioneAbilitata(CodiciOperazioni.OP_SPE_gestisciImpegno);
	}
	
	//abilitazione aggiorna impegno gsa
	public boolean isAbilitatoAggiornaImpegnoGSA(){
		return isAzioneAbilitata(CodiciOperazioni.OP_SPE_aggiornaImpegnoGSA);
	}
	
	//Metodi per la profilazione degli impegni
	public boolean isAbilitatoGestisciParere(){
		return isAzioneAbilitata(CodiciOperazioni.OP_SPE_gestisciParere);
	}
	
	public boolean isAbilitatoGestisciImpegnoDec(){
		return isAzioneAbilitata(CodiciOperazioni.OP_SPE_gestisciImpegnoDecentrato);
	}

	public boolean isAbilitatoGestisciImpegnoDecentratoP(){
		
		return isAzioneAbilitata(CodiciOperazioni.OP_SPE_gestisciImpegnoDecentratoP);
	}
	
	public boolean isAbilitatoGestisciAccertamentoDecentratoP(){
		
		return isAzioneAbilitata(CodiciOperazioni.OP_ENT_gestisciAccertamentoDecentratoP);
	}
	
	
	public boolean isAbilitatoGestisciImpegnoRiaccertato(){
		
		return isAzioneAbilitata(CodiciOperazioni.OP_SPE_gestisciImpegnoRIACC);
	}
	
	public boolean isAbilitatoGestisciAccertamentoRiaccertato(){
		
		return isAzioneAbilitata(CodiciOperazioni.OP_ENT_gestisciAccertamentoRIACC);
	}


	public boolean isAbilitatoLeggiImpegno(){
		return isAzioneAbilitata(CodiciOperazioni.LEGGI_IMP);
	}
	
	public boolean isEsistenzaSoggettoOClasse(){
		return esistenzaSoggettoOClasse();
	}
	
	//Metodi per la profilazione degli accertamenti
	public boolean isAbilitatoGestisciAccertamento(){
		return isAzioneAbilitata(CodiciOperazioni.OP_ENT_gestisciAccertamento);
	}
	
	//abilitazione aggiorna accertamento gsa
	public boolean isAbilitatoAggiornaAccertamentoGSA(){
		return isAzioneAbilitata(CodiciOperazioni.OP_ENT_aggiornaAccertamentoGSA);
	}
	
	public boolean isAbilitatoGestisciAccertamentoDec(){
		return isAzioneAbilitata(CodiciOperazioni.OP_ENT_gestisciAccertamentoDecentrato);
	}
	
	public boolean isAbilitatoLeggiAccertamento(){
		return isAzioneAbilitata(CodiciOperazioni.LEGGI_ACC);
	}

	public boolean isInserimentoSubimpegno() {
		return inserimentoSubimpegno;
	}

	public void setInserimentoSubimpegno(boolean inserimentoSubimpegno) {
		this.inserimentoSubimpegno = inserimentoSubimpegno;
	}

	public Integer getIdSubImpegno() {
		return idSubImpegno;
	}

	public void setIdSubImpegno(Integer idSubImpegno) {
		this.idSubImpegno = idSubImpegno;
	}

	@Override
	protected void setCapitoloSelezionato(CapitoloImpegnoModel supportCapitolo) {
		if (oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO) || oggettoDaPopolare.equals(OggettoDaPopolareEnum.ACCERTAMENTO)) {
			model.getStep1Model().setCapitolo(supportCapitolo);
			model.getStep1Model().setCapitoloSelezionato(true);
			teSupport.setPianoDeiConti(null);
		} else if (oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBIMPEGNO) || oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBACCERTAMENTO)) {
			model.getStep1ModelSubimpegno().setCapitolo(supportCapitolo);
			model.getStep1ModelSubimpegno().setCapitoloSelezionato(true);
		}
		model.setDatoPerVisualizza(supportCapitolo);
		model.setListaRicercaCapitolo(null);
		model.setCapitoloTrovato(false);
	}

	@Override
	public String listaClasseSoggettoChanged() {
		model.getStep1Model().getSoggetto().setCodCreditore(null);
		model.getStep1Model().setSoggettoSelezionato(false);
		return "headerSoggetto";	
	}

	@Override
	protected void setErroreCapitolo() {
		StringBuilder supportMessaggio = new StringBuilder(); 
		supportMessaggio.append(model.getStep1Model().getCapitolo().getAnno());
		supportMessaggio.append((model.getStep1Model().getCapitolo().getNumCapitolo() != null) ? "/" + model.getStep1Model().getCapitolo().getNumCapitolo() : "");
		supportMessaggio.append((model.getStep1Model().getCapitolo().getArticolo() != null) ? "/" + model.getStep1Model().getCapitolo().getArticolo() : "");
		supportMessaggio.append((model.getStep1Model().getCapitolo().getUeb() != null) ? "/" + model.getStep1Model().getCapitolo().getUeb() : "");
		addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo", supportMessaggio.toString()));
	}

	@Override
	protected void setSoggettoSelezionato(SoggettoImpegnoModel soggettoImpegnoModel) {
		if (oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO) || oggettoDaPopolare.equals(OggettoDaPopolareEnum.ACCERTAMENTO)) {
			model.getStep1Model().setSoggetto(soggettoImpegnoModel);
			model.getStep1Model().setSoggettoSelezionato(true);
		} else if (oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBIMPEGNO) || oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBACCERTAMENTO)) {
			model.getStep1ModelSubimpegno().setSoggetto(soggettoImpegnoModel);
			model.getStep1ModelSubimpegno().setSoggettoSelezionato(true);
		}
	}
	
	@Override
	protected void setProgettoSelezionato(ProgettoImpegnoModel progettoImpegnoModel) {
		model.getStep1Model().setProgetto(progettoImpegnoModel.getCodice());
		model.getStep1Model().setProgettoImpegno(progettoImpegnoModel);
		model.getStep1Model().setProgettoSelezionato(true);
	}
	
	
	@Override
	protected void popolaProgetto(Progetto p) {
		super.popolaProgetto(p);
		model.getStep1Model().setProgetto(p.getCodice());
	}

	@Override
	protected void popolaCronoprogramma(Cronoprogramma c) {
		model.getStep1Model().setCronoprogramma(c.getCodice());
		model.getStep1Model().setIdCronoprogramma(c.getUid());
		model.getStep1Model().setIdSpesaCronoprogramma(null);
	}
	
	/**
	 * Setta il siope trovato nel model
	 */
	@Override
	protected void setSiopeSpesaSelezionato(SiopeSpesa siopeTrovato,boolean trovato) {
		teSupport.setSiopeSpesaCod(siopeTrovato.getCodice());
		teSupport.setSiopeSpesa(siopeTrovato);
		teSupport.setTrovatoSiopeSpesa(trovato);
	}

	@Override
	protected void setProvvedimentoSelezionato(ProvvedimentoImpegnoModel currentProvvedimento) {
		GestisciImpegnoStep1Model iModel = (oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO) || oggettoDaPopolare.equals(OggettoDaPopolareEnum.ACCERTAMENTO)) ? model.getStep1Model() : model.getStep1ModelSubimpegno();
		iModel.setProvvedimento(currentProvvedimento);
		iModel.setProvvedimentoSelezionato(true);
		iModel.setStato(currentProvvedimento.getStato());
	}
	
	//setta un nuovo soggetto, nel momento in cui, dalla prima pagina, si sceglie di associare all'impegno/accertamento un soggetto tramite classe
	protected void controlloSoggettoSelezionatoEClasse(){
		if(model.getStep1Model().getSoggetto().getIdClasse()!= null && !"".equalsIgnoreCase(model.getStep1Model().getSoggetto().getIdClasse()) ){
			String tmpClasse= model.getStep1Model().getSoggetto().getClasse();
			String tmpIdClasse= model.getStep1Model().getSoggetto().getIdClasse();
			String tmpClasseDesc = model.getStep1Model().getSoggetto().getClasseDesc();
			model.getStep1Model().setSoggetto(new SoggettoImpegnoModel());
			model.getStep1Model().getSoggetto().setClasse(tmpClasse);
			model.getStep1Model().getSoggetto().setIdClasse(tmpIdClasse);
			model.getStep1Model().getSoggetto().setClasseDesc(tmpClasseDesc);
		}
	}
	
	//Inserisce la descrizione della classe Soggetto
	protected void inserisciDescClasseSoggettoAggiorna(){
    if(model.getStep1Model().getSoggetto().getClasse() != null && !"".equalsIgnoreCase(model.getStep1Model().getSoggetto().getClasse()) ){
    	
    	for(CodificaFin cod:model.getListaClasseSoggetto()){
    		if(cod.getCodice().equals(model.getStep1Model().getSoggetto().getClasse())){
    			model.getStep1Model().getSoggetto().setClasse(cod.getCodice());
    		}
    		
    		if(cod.getCodice().equalsIgnoreCase(model.getStep1Model().getSoggetto().getClasse())){
    			model.getStep1Model().getSoggetto().setClasseDesc(cod.getDescrizione());
    			break;
    		}
    	}
    }
	}
	
	
	// Rm jira 2060, modificato da raffi, testarlo!
	protected void inserisciDescClasseSoggetto(){
	    if(!StringUtils.isEmpty(model.getStep1Model().getSoggetto().getIdClasse())){
	    	
	    	for(CodificaFin codifica : model.getListaClasseSoggetto()){
	    		Integer idClasse=null;
	    		
	    		try{
	    			idClasse = Integer.parseInt(model.getStep1Model().getSoggetto().getIdClasse());
	    		}catch(NumberFormatException e){
	    			//
	    		}
    			//cerchiamo per id
    			if(codifica.getId().equals(idClasse)){
    				model.getStep1Model().getSoggetto().setIdClasse(Integer.toString(codifica.getUid()));
	    			model.getStep1Model().getSoggetto().setClasse(codifica.getCodice());
	    			model.getStep1Model().getSoggetto().setClasseDesc(codifica.getDescrizione());
	    			break;
	    		}
	    	}
	    }
	}
	
	//metodo della JSP per far vedere solo i dati parziali
	public boolean esistenzaSoggettoOClasse(){
		if(model.getStep1Model().getSoggetto().getClasse() != null && !"".equalsIgnoreCase(model.getStep1Model().getSoggetto().getClasse())){
			return false;
		}else{
			return true;
		}
	}
	
	
	/**
	 * 
	 *    PARTE RIGUARDANTE LA GESTIONE VINCOLI
	 */
	
	private RicercaAccertamentoPerChiaveOttimizzatoResponse executeRicercaAccertamentoPerChiaveOttimizzato()throws Exception {
		RicercaAccertamentoPerChiaveOttimizzatoResponse resp = null;
		
		// passati i controlli chiamo il ricerca accertamento per chiave
		RicercaAccertamentoPerChiaveOttimizzato rac = new RicercaAccertamentoPerChiaveOttimizzato();
		rac.setEnte(sessionHandler.getEnte());
		rac.setRichiedente(sessionHandler.getRichiedente());
		RicercaAccertamentoK rK = new RicercaAccertamentoK();
		rK.setAnnoAccertamento(model.getStep1Model().getAnnoAccertamentoVincolo());
		rK.setNumeroAccertamento(new BigDecimal(model.getStep1Model().getNumeroAccertamentoVincolo().intValue()));
		rK.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		rac.setpRicercaAccertamentoK(rK);
		
		
		resp = movimentoGestionService.ricercaAccertamentoPerChiaveOttimizzato(rac);
		
		return resp;
		
	}
	
	public String aggiungiVincolo() throws Exception {
		
		String tipoVincolo = model.getStep1Model().getTipoVincolo();	
		if(SCELTA_ACCERTAMENTO.equals(tipoVincolo)){
			return aggiungiVincoloAccertamento();
		} else if(SCELTA_AVANZO.equals(tipoVincolo)){
			return aggiungiAvanzovincolo();
		}
		return null;
	}
	
	
	private String aggiungiVincoloAccertamento() throws Exception {
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		// al click chiudo il form dati... in caso di errore verra riaperto
		model.getStep1Model().setInserisciVincoloBtn(false);
		
		// controlla dati
		if(null==model.getStep1Model().getAnnoAccertamentoVincolo()){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno accertamento vincolo "));
		}else{
			
			// l'anno dell'accertamento non può essere successivo all'anno impegno
			if(model.getStep1Model().getAnnoAccertamentoVincolo() > model.getStep1Model().getAnnoImpegno()){
				listaErrori.add(ErroreFin.ACCERTAMENTO_NON_VALIDO.getErrore());
			}
		}
		
		if(null==model.getStep1Model().getNumeroAccertamentoVincolo()){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero accertamento vincolo "));
		}
			
		
		//Controllo compilazione importo:
		String importoDigitatoString = model.getStep1Model().getImportoVincoloFormattato();
		Errore erroreImporto = controlloImportoVincoloDigitato(importoDigitatoString);
		if(erroreImporto!=null){
			listaErrori.add(erroreImporto);
		}
		//
		
		if(!listaErrori.isEmpty()){
			// apro il tab vincolo
			model.getStep1Model().setApriTabVincoli(true);
			// apro anche il tab di inserimento dati vincolo
			model.getStep1Model().setInserisciVincoloBtn(true);
			addErrori(listaErrori);
			return INPUT;
		}
	
		Accertamento accertamentoPerChiave = null;
		
		// se non arrivo da compilazione guidata
		if(model.getStep1Model().getAccertamentoPerVincolo()==null){
				
		
			// ricerca accertamento per chiave
			RicercaAccertamentoPerChiaveOttimizzatoResponse resp = executeRicercaAccertamentoPerChiaveOttimizzato();
			//qua va fatto il controllo
			
			
			
			if(resp==null || resp.isFallimento()){
				// apro il tab vincolo
				model.getStep1Model().setApriTabVincoli(true);
				// apro anche il tab di inserimento dati vincolo
				model.getStep1Model().setInserisciVincoloBtn(true);
				debug(methodName, "Errore nella risposta del servizio");
				if(resp.getAccertamento()==null && (resp.getErrori()==null || resp.getErrori().isEmpty())){
					addErrore(ErroreFin.MOV_NON_ESISTENTE.getErrore("Accertamento"));
				}else{
					addErrori(methodName, resp);
				}
				return INPUT;
			}
			accertamentoPerChiave = resp.getAccertamento();

		}else{
			// arrivo da compilazione guidata
			accertamentoPerChiave = model.getStep1Model().getAccertamentoPerVincolo();

		}
		
		//SIAC-7308
//		Boolean nonConsentito = verificaAccertamentoVincolo(accertamentoPerChiave);
//		if(nonConsentito){
//
//			listaErrori.add(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Vincolo non consentito"));
//			addErrori(listaErrori);
//			return INPUT;
//		}
		
		
		
		
		
		// controlli sulle disponibilita
		if(accertamentoPerChiave.getDisponibilitaUtilizzare().compareTo(model.getStep1Model().getImportoVincolo())<0){
			addErrore(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Vincoli", "ad utilizzare"));
		}

		// stato annullato
		if(accertamentoPerChiave.getStatoOperativoMovimentoGestioneEntrata().equals(Constanti.MOVGEST_STATO_ANNULLATO)){
			addErrore(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Accertamento", "annullato"));
		}
		
		// controllo che lo stess accertamento non sia gia' stato trattato 
		//  in un altro vincolo presente nella lista
		accertamentoGiaPresenteInVincoli(accertamentoPerChiave);
		
		if(hasActionErrors()){
			return INPUT;
		}
		
		// ricerca andata a buon fine
		// AGGIUNGO ALLA LISTA:
		aggiungiAListaVincoliImpegno(accertamentoPerChiave, null, model.getStep1Model().getImportoVincolo());
		
		// se tutto ok cancello i valori del form
		// e apro il tab vincolo
		this.annullaValoriVincolo();
		
		// calcolo i totali
		this.calcolaTotaliUtilizzabile();
		// trappo eventuali errori nei calcoli totali
		if(hasActionErrors()){
			return INPUT;
		}
		
		// sposta l'ancora all'altezza dei vincoli
		model.getStep1Model().setPortaAdAltezzaVincoli(true);
		
		return SUCCESS;
	}
	
	private String aggiungiAvanzovincolo() throws Exception {
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		// al click chiudo il form dati... in caso di errore verra riaperto
		model.getStep1Model().setInserisciVincoloBtn(false);
		
		
		//Controllo compilazione importo:
		String importoDigitatoString = model.getStep1Model().getImportoAvanzoVincoloFormattato();
		Errore erroreImporto = controlloImportoVincoloDigitato(importoDigitatoString);
		if(erroreImporto!=null){
			listaErrori.add(erroreImporto);
		}
		

		if(!listaErrori.isEmpty()){
			// apro il tab vincolo
			model.getStep1Model().setApriTabVincoli(true);
			// apro anche il tab di inserimento dati vincolo
			model.getStep1Model().setInserisciVincoloBtn(true);
			addErrori(listaErrori);
			return INPUT;
		}
		
		Avanzovincolo avanzovincoloSelezionato = getAvanzoVincoloSelezionato();
		
		if(avanzovincoloSelezionato==null || avanzovincoloSelezionato.getUid()<=0){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("FPV / Avanzo non selezionato"));
		}
		
		BigDecimal residuoAvanzo = getImportoResiduoAvanzoVincoloSelezionato();
		BigDecimal importoDigitato = model.getStep1Model().getImportoVincolo();
		
		//Controllo importo disponibile:
		
		if(importoDigitato.compareTo(residuoAvanzo)>0){
			listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("gestione vincolo", "a collegare"));
		}
		
		if(!listaErrori.isEmpty()){
			// apro il tab vincolo
			model.getStep1Model().setApriTabVincoli(true);
			// apro anche il tab di inserimento dati vincolo
			model.getStep1Model().setInserisciVincoloBtn(true);
			addErrori(listaErrori);
			return INPUT;
		}
	
		// controllo che lo stess accertamento non sia gia' stato trattato 
		//  in un altro vincolo presente nella lista
		avanzoVincoloGiaPresenteInVincoli(avanzovincoloSelezionato);
		
		if(hasActionErrors()){
			return INPUT;
		}
		
		// AGGIUNGO ALLA LISTA:
		aggiungiAListaVincoliImpegno(null, avanzovincoloSelezionato, importoDigitato);
		
		// se tutto ok cancello i valori del form
		// e apro il tab vincolo
		this.annullaValoriVincolo();
		
		// calcolo i totali
		this.calcolaTotaliUtilizzabile();
		// trappo eventuali errori nei calcoli totali
		if(hasActionErrors()){
			return INPUT;
		}
		
		// sposta l'ancora all'altezza dei vincoli
		model.getStep1Model().setPortaAdAltezzaVincoli(true);
		
		return SUCCESS;
	}
	
	private void aggiungiAListaVincoliImpegno(Accertamento accertamentoSelezionato, Avanzovincolo avanzovincoloSelezionato, BigDecimal importoDigitato){
		VincoloImpegno vincolo = new VincoloImpegno();
		
		//SIAC-5273 al primo inserimento di un vincolo
		//non veniva aggiornato il disponibile
		if(accertamentoSelezionato!=null){
			//caso accertamento
			BigDecimal dispUtilizzare = accertamentoSelezionato.getDisponibilitaUtilizzare();
			BigDecimal dispUtilizzareNew = dispUtilizzare.subtract(importoDigitato);
			//setto il nuovo disponibile a utilizzare:
			accertamentoSelezionato.setDisponibilitaUtilizzare(dispUtilizzareNew);
		} else if (avanzovincoloSelezionato !=null){
			//caso avanzo vincolo
			BigDecimal dispAvanzoVincolo = avanzovincoloSelezionato.getDisponibileAvanzovincolo();
			BigDecimal dispNew = dispAvanzoVincolo.subtract(importoDigitato);
			//setto il nuovo disponibile avanzo vincolo:
			avanzovincoloSelezionato.setDisponibileAvanzovincolo(dispNew);
		}
		//fine SIAC-5273 
		
		vincolo.setAccertamento(accertamentoSelezionato);
		vincolo.setAvanzoVincolo(avanzovincoloSelezionato);
		vincolo.setImporto(importoDigitato);
		
		if(model.getStep1Model().getListaVincoliImpegno()!=null && !model.getStep1Model().getListaVincoliImpegno().isEmpty()){
			model.getStep1Model().getListaVincoliImpegno().add(vincolo);
		}  else{
			List<VincoloImpegno> listaVincoli = new ArrayList<VincoloImpegno>();
			listaVincoli.add(vincolo);
			model.getStep1Model().setListaVincoliImpegno(listaVincoli);
		}
	}
	
	/**
	 * 
	 * Vale sia per vincoli su accertamento che avanzo vincolo
	 * 
	 * @param importoDigitato
	 * @param listaErrori
	 * @return
	 */
	private Errore controlloImportoVincoloDigitato(String importoDigitato){
		Errore errore = null;
		if(importoDigitato == null || importoDigitato.equals("0") || importoDigitato.equals("")){
			errore = ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo vincolo ");
		}else{
	    	try{
	    		// converto
	    		model.getStep1Model().setImportoVincolo(convertiImportoToBigDecimal(importoDigitato));
	    		if (model.getStep1Model().getImportoVincolo().compareTo(BigDecimal.ZERO) <= 0) {
	    			errore = ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo vincolo ");
	    		}
	    	}catch(Exception e){
	    		errore = ErroreFin.FORMATO_NON_VALIDO.getErrore("Importo vincolo ", "numerico");
	    	}
	   }
		return errore;
	}
	
	/**
	 * tasto azioni: di eliminazione della riga di vincolo
	 * @return
	 */
	public String eliminaAvanzoVincolo(){
		Integer uid = new Integer(getUidAvanzoVincoloDaPassare());
		if(model.getStep1Model().getListaVincoliImpegno()!=null && !model.getStep1Model().getListaVincoliImpegno().isEmpty()){
			VincoloImpegno vincoloDaEliminare = trovaVincoloInDataTableByUidAvanzo(uid);
			if(null!=vincoloDaEliminare){
				// allora lo tolgo dalla lista
				model.getStep1Model().getListaVincoliImpegno().remove(vincoloDaEliminare);
			}
		}
		// ricalcola i totali dopo aver eliminato
		calcolaTotaliUtilizzabile();
		
		// sposta l'ancora all'altezza dei vincoli
		model.getStep1Model().setPortaAdAltezzaVincoli(true);
		
		return SUCCESS;
	}
	
	private VincoloImpegno trovaVincoloInDataTableByAccertamento(Integer annoAcc, BigDecimal numeroAcc){
		VincoloImpegno vincolo = null;
		if(model.getStep1Model().getListaVincoliImpegno()!=null && !model.getStep1Model().getListaVincoliImpegno().isEmpty()){
			for (VincoloImpegno vincoloImp : model.getStep1Model().getListaVincoliImpegno()) {
				if(vincoloImp.getAccertamento()!=null && vincoloImp.getAccertamento().getAnnoMovimento() == annoAcc.intValue() &&
				   vincoloImp.getAccertamento().getNumero().equals(numeroAcc) ){
					vincolo = vincoloImp;
					break;
				}
			}
		}
		return vincolo;
	}
	
	private VincoloImpegno trovaVincoloInDataTableByUidAvanzo(Integer uid){
		VincoloImpegno vincolo = null;
		if(model.getStep1Model().getListaVincoliImpegno()!=null && !model.getStep1Model().getListaVincoliImpegno().isEmpty()){
			for (VincoloImpegno vincoloImp : model.getStep1Model().getListaVincoliImpegno()) {
				if(vincoloImp.getAvanzoVincolo()!=null && vincoloImp.getAvanzoVincolo().getUid()==uid.intValue()){
					vincolo = vincoloImp;
					break;
				}
			}
		}
		return vincolo;
	}
		
	/**
	 * tasto azioni: di eliminazione della riga di vincolo
	 * @return
	 */
	public String eliminaVincolo(){
		
		BigDecimal numeroAcc = new BigDecimal(getNumeroAccDaPassare());
		Integer annoAcc = new Integer(getAnnoAccDaPassare());
		
		VincoloImpegno vincoloDaEliminare = trovaVincoloInDataTableByAccertamento(annoAcc, numeroAcc);
		if(null!=vincoloDaEliminare){
			// allora lo tolgo dalla lista
			model.getStep1Model().getListaVincoliImpegno().remove(vincoloDaEliminare);
		}
		
		// ricalcola i totali dopo aver eliminato
		calcolaTotaliUtilizzabile();
		
		// sposta l'ancora all'altezza dei vincoli
		model.getStep1Model().setPortaAdAltezzaVincoli(true);
		
		return SUCCESS;
	}
	
	/**
	 * serve per la gestione del pop up di aggiornamento del vincolo
	 * @return
	 */
	public String aggiornaVincolo(){
		// usato per l'aggiorna impegno
		BigDecimal deltaImportiAgg = BigDecimal.ZERO;
		
		// identifico tramite gli hidden la riga selezionata
		if(model.getStep1Model().getDettaglioVincolo().getImportoDaAggiornareFormattato()!=null &&
			StringUtils.isNotEmpty(model.getStep1Model().getDettaglioVincolo().getImportoDaAggiornareFormattato())){
			
			model.getStep1Model().getDettaglioVincolo().setImportoDaAggiornare(convertiImportoToBigDecimal(model.getStep1Model().getDettaglioVincolo().getImportoDaAggiornareFormattato()));
			
			if(model.getStep1Model().getDettaglioVincolo().getImportoDaAggiornare().compareTo(BigDecimal.ZERO)<=0){
				addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo vincolo "));
			}
			
			if(model.isOperazioneAggiorna()){
				// sono in aggiorna impegno
				deltaImportiAgg = model.getStep1Model().getDettaglioVincolo().getImportoDaAggiornare().subtract(model.getStep1Model().getDettaglioVincolo().getImportoDaAggiornareOld());
				
				if(deltaImportiAgg.compareTo(model.getStep1Model().getDettaglioVincolo().getImportoUtilizzabile())>0){
					addErrore(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Vincoli", "ad utilizzare"));
				}
				
				
			}else{
			    // sono in inserisci impegno
				if(model.getStep1Model().getDettaglioVincolo().getImportoDaAggiornare().compareTo(model.getStep1Model().getDettaglioVincolo().getImportoUtilizzabile())>0){
					addErrore(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Vincoli", "ad utilizzare"));
				}
			}
			
		}else{
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo vincolo "));
		}
		
		if(hasActionErrors()){
			return INPUT;
		}
		
		BigDecimal numeroAcc = new BigDecimal(model.getStep1Model().getDettaglioVincolo().getNumeroAccertamento());
		Integer annoAcc = new Integer(model.getStep1Model().getDettaglioVincolo().getAnnoAccertamento());
		
		VincoloImpegno vincoloImp = trovaVincoloInDataTableByAccertamento(annoAcc, numeroAcc);
		
		if(vincoloImp!=null){
			BigDecimal nuovoImporto = model.getStep1Model().getDettaglioVincolo().getImportoDaAggiornare();
			BigDecimal vecchioImporto = vincoloImp.getImporto();
			
			BigDecimal delta = vecchioImporto.subtract(nuovoImporto);
			BigDecimal dispUtilizzare = vincoloImp.getAccertamento().getDisponibilitaUtilizzare();
			
			dispUtilizzare = dispUtilizzare.add(delta);
			
			vincoloImp.getAccertamento().setDisponibilitaUtilizzare(dispUtilizzare);
		
			vincoloImp.setImporto(model.getStep1Model().getDettaglioVincolo().getImportoDaAggiornare());
		}
		
		// ricalcola i totali dopo aver eliminato
		calcolaTotaliUtilizzabile();
		// sposta l'ancora all'altezza dei vincoli
		model.getStep1Model().setPortaAdAltezzaVincoli(true);
		return SUCCESS;
	}
	
	/**
	 * serve per la gestione del pop up di aggiornamento del vincolo
	 * @return
	 */
	public String aggiornaAvanzoVincolo(){
		// usato per l'aggiorna impegno
		BigDecimal deltaImportiAgg = BigDecimal.ZERO;
		
		// identifico tramite gli hidden la riga selezionata
		if(model.getStep1Model().getDettaglioVincolo().getImportoDaAggiornareFormattato()!=null &&
			StringUtils.isNotEmpty(model.getStep1Model().getDettaglioVincolo().getImportoDaAggiornareFormattato())){
			
			model.getStep1Model().getDettaglioVincolo().setImportoDaAggiornare(convertiImportoToBigDecimal(model.getStep1Model().getDettaglioVincolo().getImportoDaAggiornareFormattato()));
			
			if(model.getStep1Model().getDettaglioVincolo().getImportoDaAggiornare().compareTo(BigDecimal.ZERO)<=0){
				addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo vincolo "));
			}
			
			if(model.isOperazioneAggiorna()){
				// sono in aggiorna impegno
				deltaImportiAgg = model.getStep1Model().getDettaglioVincolo().getImportoDaAggiornare().subtract(model.getStep1Model().getDettaglioVincolo().getImportoDaAggiornareOld());
				
				if(deltaImportiAgg.compareTo(model.getStep1Model().getDettaglioVincolo().getImportoUtilizzabile())>0){
					addErrore(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Vincoli", "ad utilizzare"));
				}
				
				
			}else{
			    // sono in inserisci impegno
				if(model.getStep1Model().getDettaglioVincolo().getImportoDaAggiornare().compareTo(model.getStep1Model().getDettaglioVincolo().getImportoUtilizzabile())>0){
					addErrore(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Vincoli", "ad utilizzare"));
				}
			}
			
		}else{
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo vincolo "));
		}
		
		if(hasActionErrors()){
			return INPUT;
		}
		
		Integer uidAvanzoVincolo = new Integer(model.getStep1Model().getDettaglioVincolo().getUidAvanzoVincolo());
		VincoloImpegno vincoloInAggiornamento = trovaVincoloInDataTableByUidAvanzo(uidAvanzoVincolo);
		
		//aggiorno i dati:
		BigDecimal nuovoImporto = model.getStep1Model().getDettaglioVincolo().getImportoDaAggiornare();
		BigDecimal vecchioImporto = vincoloInAggiornamento.getImporto();
		BigDecimal delta = vecchioImporto.subtract(nuovoImporto);
		BigDecimal dispUtilizzare = vincoloInAggiornamento.getAvanzoVincolo().getDisponibileAvanzovincolo();
		dispUtilizzare = dispUtilizzare.add(delta);
		vincoloInAggiornamento.getAvanzoVincolo().setDisponibileAvanzovincolo(dispUtilizzare);
		vincoloInAggiornamento.setImporto(model.getStep1Model().getDettaglioVincolo().getImportoDaAggiornare());
		//
		
		// ricalcola i totali dopo aver eliminato
		calcolaTotaliUtilizzabile();
		// sposta l'ancora all'altezza dei vincoli
		model.getStep1Model().setPortaAdAltezzaVincoli(true);
		return SUCCESS;
	}


	
	/**
	 *  per l'apertura del pop up di aggiornamento del vincolo
	 * @return
	 */
	public String dettaglioAvanzoAggiornaVincolo(){
		
		Integer uid = new Integer(getUidAvanzoVincoloPerAggiorna());
		VincoloImpegno vincoloDaAggiornare = trovaVincoloInDataTableByUidAvanzo(uid);
		DettaglioVincoloModel dvm = new DettaglioVincoloModel();
		if(vincoloDaAggiornare!=null){
			
			dvm.setImportoUtilizzabile(vincoloDaAggiornare.getAvanzoVincolo().getDisponibileAvanzovincolo());
			dvm.setImportoDaAggiornare(vincoloDaAggiornare.getImporto());
			// utilizzato per l'aggiornamento
			dvm.setImportoDaAggiornareOld(vincoloDaAggiornare.getImporto());
			if(null!=vincoloDaAggiornare.getImporto()){
				dvm.setImportoDaAggiornareFormattato(convertiBigDecimalToImporto(vincoloDaAggiornare.getImporto()));
			}
			dvm.setUidAvanzoVincolo(String.valueOf(uid));
		}
		
		// setto iol dettaglio
		model.getStep1Model().setDettaglioVincolo(dvm);
		
		return DETTAGLIO_AVANZO_VINCOLO;
	}
	
	/**
	 *  per l'apertura del pop up di aggiornamento del vincolo
	 * @return
	 */
	public String dettaglioAggiornaVincolo(){
		
		BigDecimal numeroAcc = new BigDecimal(getNumeroAccPerAggiorna());
		Integer annoAcc = new Integer(getAnnoAccPerAggiorna());
		VincoloImpegno vincoloDaAggiornare = trovaVincoloInDataTableByAccertamento(annoAcc, numeroAcc);
		DettaglioVincoloModel dvm = new DettaglioVincoloModel();
		if(vincoloDaAggiornare!=null){
			
			dvm.setImportoUtilizzabile(vincoloDaAggiornare.getAccertamento().getDisponibilitaUtilizzare());
			dvm.setImportoDaAggiornare(vincoloDaAggiornare.getImporto());
			// utilizzato per l'aggiornamento
			dvm.setImportoDaAggiornareOld(vincoloDaAggiornare.getImporto());
			if(null!=vincoloDaAggiornare.getImporto()){
				dvm.setImportoDaAggiornareFormattato(convertiBigDecimalToImporto(vincoloDaAggiornare.getImporto()));
			}
			dvm.setAnnoAccertamento(String.valueOf(vincoloDaAggiornare.getAccertamento().getAnnoMovimento()));
			dvm.setNumeroAccertamento(String.valueOf(vincoloDaAggiornare.getAccertamento().getNumero()));
		}
		
		// setto iol dettaglio
		model.getStep1Model().setDettaglioVincolo(dvm);
		
		return DETTAGLIO_VINCOLO;
	}
	
	private void accertamentoGiaPresenteInVincoli(Accertamento accertamento){
		if(model.getStep1Model().getListaVincoliImpegno()!=null && !model.getStep1Model().getListaVincoliImpegno().isEmpty()){
			for (VincoloImpegno vincoloImp : model.getStep1Model().getListaVincoliImpegno()) {
				if(vincoloImp.getAccertamento()!=null){
					if(vincoloImp.getAccertamento().getAnnoMovimento() == accertamento.getAnnoMovimento() &&
							   vincoloImp.getAccertamento().getNumero().equals(accertamento.getNumero()) ){
						addErrore(ErroreFin.MOVGEST_GIA_PRESENTE.getErrore("Accertamento"));
						break;
					}
				}
			}
		}
	}
	
	private void avanzoVincoloGiaPresenteInVincoli(Avanzovincolo avanzovincolo){
		if(model.getStep1Model().getListaVincoliImpegno()!=null && !model.getStep1Model().getListaVincoliImpegno().isEmpty()){
			for (VincoloImpegno vincoloImp : model.getStep1Model().getListaVincoliImpegno()) {
				if(vincoloImp.getAvanzoVincolo()!=null){
					if(vincoloImp.getAvanzoVincolo().getUid() == avanzovincolo.getUid()){
						addErrore(ErroreFin.MOVGEST_GIA_PRESENTE.getErrore("Avanzovincolo"));
						break;
					}
				}
			}
		}
	}
	
	protected void calcolaTotaliUtilizzabile(){
		if(model.getStep1Model().getListaVincoliImpegno()==null || model.getStep1Model().getListaVincoliImpegno().isEmpty()){
			// non ci sono righe
			model.getStep1Model().setTotaleImportoDaCollegare(BigDecimal.ZERO);
			model.getStep1Model().setTotaleImportoVincoli(BigDecimal.ZERO);
		}else{
			// calcolo 
			BigDecimal totaleVincoli = BigDecimal.ZERO;
			BigDecimal totaleDaCollegare = BigDecimal.ZERO;
			for (VincoloImpegno vincoloImpegno : model.getStep1Model().getListaVincoliImpegno()) {
				
				totaleVincoli = totaleVincoli.add(vincoloImpegno.getImporto());
				
			}
			
			// totale da collegare = importo impegno - totale vincoli
			if(model.getStep1Model().getImportoFormattato() == null || 
				       model.getStep1Model().getImportoFormattato().equals("0") ||
				       model.getStep1Model().getImportoFormattato().equals("")){
				       // se non c'e'
				 	   addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo "+model.getLabels().get(LABEL_OGGETTO_GENERICO)));
			}else{
				// ho tutti i valori e posso fare i calcoli
				totaleDaCollegare = convertiImportoToBigDecimal(model.getStep1Model().getImportoFormattato()).subtract(totaleVincoli);
			}
			
			if(totaleDaCollegare.compareTo(BigDecimal.ZERO)<0){
				addErrore(ErroreFin.TOT_COLLEGA_VINCOLO.getErrore(""));
			}
			
			// sono in aggiornamento impegno
			if(model.isOperazioneAggiorna()){
				
				//SE Impegno.StatoOperativo = DEFINITIVO  e  TOTALEVINC <   Impegno.sommaLiquidazioniDoc    	
				if(!model.getStep1Model().getDescrizioneStatoOperativoMovimento().equals(Constanti.STATO_PROVVISORIO)){
					if(totaleVincoli.compareTo(model.getSommaLiquidazioneDoc())<0){
						addErrore(ErroreFin.MOV_VINCOLATO_LIQUIDATO.getErrore(""));
					}
				}
				
				
			}
						
			model.getStep1Model().setTotaleImportoDaCollegare(totaleDaCollegare);
			model.getStep1Model().setTotaleImportoVincoli(totaleVincoli);
		}
	}

	public String annullaValoriVincolo() throws Exception {
		
		model.getStep1Model().setAnnoAccertamentoVincolo(null);
		model.getStep1Model().setNumeroAccertamentoVincolo(null);
		model.getStep1Model().setImportoVincolo(null);
		model.getStep1Model().setImportoVincoloFormattato("");
		
		model.getStep1Model().setAccertamentoPerVincolo(null);
		model.getStep1Model().setListaAccPerVincoli(null);
		
		// apro il tab vincolo
		model.getStep1Model().setApriTabVincoli(true);
		
		// sposta l'ancora all'altezza dei vincoli
		model.getStep1Model().setPortaAdAltezzaVincoli(true);
		
		//dati avanzo vincolo:
		model.getStep1Model().setImportoAvanzoVincoloFormattato("");	
		initSceltaAccertamentoAvanzoList();
		
		return SUCCESS;
	}
	
	
	
	public String ricercaAccertamentoPerVincoli(){
		List<Errore> listaErrori = new ArrayList<Errore>();
	    if(null==model.getStep1Model().getAccertamentoRicerca().getAnnoAccertamento() ||
	    	StringUtils.isEmpty(model.getStep1Model().getAccertamentoRicerca().getAnnoAccertamento())){
	    	
	    	listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Accertamento "));
	    }
	    
	    if(null==model.getStep1Model().getAccertamentoRicerca().getNumeroAccertamento() ||
		    	StringUtils.isEmpty(model.getStep1Model().getAccertamentoRicerca().getNumeroAccertamento())){
		    	
		    	listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Accertamento "));
		}
		
	    if(!listaErrori.isEmpty()){
	    	addErrori(listaErrori);
	    }else{
	    	// lancio la ricerca 
	    	RicercaAccertamenti ra = new RicercaAccertamenti();
	    	ra.setRichiedente(sessionHandler.getRichiedente());
	    
	    	RicercaAccertamento rp = new RicercaAccertamento();
	    	rp.setAnnoBilancio(sessionHandler.getBilancio().getAnno());
	    	
	    	// anno acc
	    	if(null!=model.getStep1Model().getAccertamentoRicerca().getAnnoAccertamento() && StringUtils.isNumeric(model.getStep1Model().getAccertamentoRicerca().getAnnoAccertamento())){
	    		rp.setAnnoEsercizio(Integer.valueOf(model.getStep1Model().getAccertamentoRicerca().getAnnoAccertamento()));
	    		rp.setAnnoImpegno(Integer.valueOf(model.getStep1Model().getAccertamentoRicerca().getAnnoAccertamento()));
	    	}
	    	// numero acc
	    	if(null!=model.getStep1Model().getAccertamentoRicerca().getNumeroAccertamento() && 
	    			StringUtils.isNotEmpty(model.getStep1Model().getAccertamentoRicerca().getNumeroAccertamento()) && 
	    			StringUtils.isNumeric(model.getStep1Model().getAccertamentoRicerca().getNumeroAccertamento())){
		    	rp.setNumeroImpegno(Integer.valueOf(model.getStep1Model().getAccertamentoRicerca().getNumeroAccertamento()));
	    	}
	    	rp.setFlagDaRiaccertamento(WebAppConstants.No);
	    	
	    	// numero capitolo
	    	if(null!=model.getStep1Model().getAccertamentoRicerca().getNumeroCapitolo() &&
	    	   StringUtils.isNotEmpty(model.getStep1Model().getAccertamentoRicerca().getNumeroCapitolo())){
	    		
	    		if(StringUtils.isNumeric(model.getStep1Model().getAccertamentoRicerca().getNumeroCapitolo())){
	    			rp.setNumeroCapitolo(Integer.valueOf(model.getStep1Model().getAccertamentoRicerca().getNumeroCapitolo()));
	    		}
	    	}	
	    	
	    	// numero articolo
	    	if(null!=model.getStep1Model().getAccertamentoRicerca().getNumeroArticolo() &&
	 	    	   StringUtils.isNotEmpty(model.getStep1Model().getAccertamentoRicerca().getNumeroArticolo())){
		    	  
	    		   if(StringUtils.isNumeric(model.getStep1Model().getAccertamentoRicerca().getNumeroArticolo())){
		    			rp.setNumeroArticolo(Integer.valueOf(model.getStep1Model().getAccertamentoRicerca().getNumeroArticolo()));
		    	   }
	    	}

	    	// numero UEB
	    	if(null!=model.getStep1Model().getAccertamentoRicerca().getUeb() &&
		 	    	   StringUtils.isNotEmpty(model.getStep1Model().getAccertamentoRicerca().getUeb())){
	    		 	if(StringUtils.isNumeric(model.getStep1Model().getAccertamentoRicerca().getUeb())){
		    			rp.setNumeroUEB(Integer.valueOf(model.getStep1Model().getAccertamentoRicerca().getUeb()));
		    	   }
	    	}
	    	
	    	ra.setpRicercaAccertamento(rp);
	    	
	    	RicercaAccertamentiResponse resp = movimentoGestionService.ricercaAccertamenti(ra);
	    	
	    	
	    	if(resp!=null && resp.isFallimento()){
	    		addErrori(resp.getErrori());
	    		model.getStep1Model().setTrovatiAccPerVincolo(false);
	    	}else{
	    		model.getStep1Model().setTrovatiAccPerVincolo(true);
	    		model.getStep1Model().setListaAccPerVincoli(resp.getAccertamenti());
	    	}
	    }
		
		return GOTO_RICERCA_ACC_VINCOLI;
	}

	public String pulisciRicercaAccPerVincoli(){
		model.getStep1Model().setTrovatiAccPerVincolo(false);
		model.getStep1Model().setListaAccPerVincoli(null);
		return GOTO_RICERCA_ACC_VINCOLI;
	}
	
	
	public String selezionaAccPerVincolo() throws Exception {
		
		if(StringUtils.isEmpty(getRadioCodiceAccPerVincoli())){
			addActionError("Elemento non selezionato");
		}
		
		// apro il tab vincolo
		model.getStep1Model().setApriTabVincoli(true);
		// apro anche il tab di inserimento dati vincolo
		model.getStep1Model().setInserisciVincoloBtn(true);
		
        if (hasActionErrors()){
			return INPUT;
		}
		
		if(model.getStep1Model().getListaAccPerVincoli()!=null && !model.getStep1Model().getListaAccPerVincoli().isEmpty()){
			
			for (Accertamento accertamento : model.getStep1Model().getListaAccPerVincoli()) {
				if(accertamento.getUid() == Integer.valueOf(getRadioCodiceAccPerVincoli())){
					if(accertamento.getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase(Constanti.MOVGEST_STATO_ANNULLATO)){
						// stato annullato
						addErrore(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Accertamento", "annullato"));
					}else{
						// ok
						model.getStep1Model().setAnnoAccertamentoVincolo(accertamento.getAnnoMovimento());
						model.getStep1Model().setNumeroAccertamentoVincolo(accertamento.getNumero().intValue());
						RicercaAccertamentoPerChiaveOttimizzatoResponse respKAcc = executeRicercaAccertamentoPerChiaveOttimizzato();
						if(null!=respKAcc && !respKAcc.isFallimento()){
							model.getStep1Model().setAccertamentoPerVincolo(respKAcc.getAccertamento());
						}
					}
	                break;			
				}
			}
			
		}
		
		// sposta l'ancora all'altezza dei vincoli
		model.getStep1Model().setPortaAdAltezzaVincoli(true);
		
		return SUCCESS;
	}
	
	/**
	 * funzione che restituisce la lista dei vincoli
	 * legata al capitolo di impegno
	 * @param 
	 * @return
	 */
	protected ListaPaginata<VincoloCapitoli> getVincoliCapitoloSpesa(CapitoloImpegnoModel cap){
		  ListaPaginata<VincoloCapitoli> listaVincoli = null;
		  if(cap!=null){
			  RicercaVincolo rv = new RicercaVincolo();
			  rv.setParametriPaginazione(new ParametriPaginazione());
			  rv.setRichiedente(sessionHandler.getRichiedente());
			  Capitolo capitolo = new Capitolo();
			  capitolo.setAnnoCapitolo(cap.getAnno());
			  capitolo.setNumeroCapitolo(cap.getNumCapitolo());
			  capitolo.setNumeroArticolo(cap.getArticolo());
			  capitolo.setTipoCapitolo(TipoCapitolo.CAPITOLO_USCITA_GESTIONE);
			  if(cap.getUeb()!=null && !cap.getUeb().equals(BigDecimal.ZERO)){
				  capitolo.setNumeroUEB(cap.getUeb().intValue());
			  }
			  rv.setCapitolo(capitolo);
			  Vincolo vincolo = new Vincolo();
			  vincolo.setEnte(sessionHandler.getEnte());
			  rv.setVincolo(vincolo);
			  
			  RicercaVincoloResponse vincoloResponse = vincoloCapitoloService.ricercaVincolo(rv);
			  
			  if(vincoloResponse!=null && !vincoloResponse.isFallimento()){
				  
				  listaVincoli = vincoloResponse.getVincoloCapitoli();
			  }
		  }
		  
		  return listaVincoli;
	}
	
	protected List<VincoloCapitoli> getConFlagTrasferimenti(ListaPaginata<VincoloCapitoli> listaVincoliSpesa){
		List<VincoloCapitoli> vincoliConFlagTrasferimentiVincolati = new ArrayList<VincoloCapitoli>();
		if(listaVincoliSpesa!=null && !listaVincoliSpesa.isEmpty()){
			for (VincoloCapitoli vincoloCapitoli : listaVincoliSpesa){
				if(vincoloCapitoli!=null && vincoloCapitoli.getFlagTrasferimentiVincolati().booleanValue()){
					vincoliConFlagTrasferimentiVincolati.add(vincoloCapitoli);
				}
			}
		}
		return vincoliConFlagTrasferimentiVincolati;
	}
	
	
	protected EsistenzaProgettoResponse esistenzaProgetto(){
		EsistenzaProgettoResponse esistenzaResp = new EsistenzaProgettoResponse();
		EsistenzaProgetto ep = new EsistenzaProgetto();
		ep.setCodiceProgetto(model.getStep1Model().getProgetto());
		ep.setRichiedente(sessionHandler.getRichiedente());
    	ep.setBilancio(sessionHandler.getBilancio());
    	ep.setCodiceTipoProgetto(TipoProgetto.GESTIONE.getCodice());
    	
		esistenzaResp = genericService.cercaProgetto(ep);
    	
		if(esistenzaResp!=null && esistenzaResp.getEsito().toString().equalsIgnoreCase("FALLIMENTO")){
    		if(!esistenzaResp.isEsisteProgetto()){
    			
    			addPersistentActionError("Il valore del progetto " +model.getStep1Model().getProgetto()+ " non e' consentito: " + esistenzaResp.getErrori().get(0).getCodice() + " -" + esistenzaResp.getErrori().get(0).getDescrizione());
    			esistenzaResp.setErrori(new ArrayList<Errore>());
    		}
    	}
    	return esistenzaResp;
	}
	
	/**
	 *  fine VINCOLI
	 * 
	 */
	
	protected ProvvedimentoImpegnoModel getProvvedimentoById(int uid){
		ProvvedimentoImpegnoModel pim = new ProvvedimentoImpegnoModel();
		RicercaProvvedimento ricercaProvv = new RicercaProvvedimento();
		ricercaProvv.setEnte(sessionHandler.getEnte());
		ricercaProvv.setRichiedente(sessionHandler.getRichiedente());
		RicercaAtti ricercaAtti = new RicercaAtti();
		ricercaAtti.setUid(uid);
		ricercaProvv.setRicercaAtti(ricercaAtti);
		RicercaProvvedimentoResponse respAtti = provvedimentoService.ricercaProvvedimento(ricercaProvv);
		
		AttoAmministrativo atto = respAtti.getListaAttiAmministrativi().get(0);
		
		pim.setUid(atto.getUid());
		pim.setAnnoProvvedimento(atto.getAnno());
		pim.setNumeroProvvedimento(BigInteger.valueOf(atto.getNumero()));
		pim.setIdTipoProvvedimento(atto.getTipoAtto().getUid());
		pim.setCodiceTipoProvvedimento(atto.getTipoAtto().getCodice());
		pim.setTipoProvvedimento(atto.getTipoAtto().getDescrizione());
		pim.setOggetto(atto.getOggetto());
		pim.setCodiceStrutturaAmministrativa((atto.getStrutturaAmmContabile() != null)? atto.getStrutturaAmmContabile().getCodice() : null);
		pim.setStrutturaAmministrativa((atto.getStrutturaAmmContabile() != null)? atto.getStrutturaAmmContabile().getDescrizione() : null);
		pim.setBloccoRagioneria(atto.getBloccoRagioneria());
		if(atto.getStrutturaAmmContabile()!=null){
			// setto livello e uid che mi serviranno dopo
			pim.setLivello(atto.getStrutturaAmmContabile().getLivello());
			pim.setUidStrutturaAmm(atto.getStrutturaAmmContabile().getUid());
		}
		
		pim.setStato(atto.getStatoOperativo());
		
		return pim;
	}
	
	
	// se false abilita la check, se true disabilita
	public boolean abilitaParereFinanziario() {
		if(!isAzioneAbilitata(CodiciOperazioni.OP_SPE_gestisciImpegno) && 
				!isEnteAbilitatoParereFinanziario()
				) {
			return Boolean.TRUE;
		}else return Boolean.FALSE;

	}
	
	
	// se false abilita la check, se true disabilita
	public boolean visualizzaUEB() {
		return isEnteAbilitatoUeb();
	}

	/**
	 * Serve a pilotare il flag sanita' in accordo con la selezione di un capitolo o un provvedimento che 
	 * hanno una struttura amministrativa della sanita'
	 */
	protected void pilotaFlagSanita(){
		if(isEnteAbilitatoGestioneGsa()){
			String codiceStrutturaAmministrativaCapitolo = model.getStep1Model().getCapitolo().getCodiceStrutturaAmministrativa();
			String codiceStrutturaAmministrativaProvvedimento = model.getStep1Model().getProvvedimento().getCodiceStrutturaAmministrativa();
			String codiceGsa = getCodiceLivelloByTipo(TipologiaGestioneLivelli.GESTIONE_GSA);
			if(codiceGsa!=null){
				boolean poniASi = false;
				if(codiceStrutturaAmministrativaCapitolo!=null && codiceStrutturaAmministrativaCapitolo.startsWith(codiceGsa)){
					poniASi = true;
				} else if(codiceStrutturaAmministrativaProvvedimento!=null && codiceStrutturaAmministrativaProvvedimento.startsWith(codiceGsa)) {
					poniASi = true;
				}
				if(poniASi){
					model.getStep1Model().setFlagAttivaGsa(WebAppConstants.Si);
				}
			}
		}
		//
	}
	
	protected boolean compilatoProvvedimento(){
		if(model.getStep1Model().getProvvedimento()!=null
				&& model.getStep1Model().getProvvedimento().getUid()!=null &&
				model.getStep1Model().getProvvedimento().getUid().intValue()>0){
			return true;
		} else {
			return false;
		}
	}
	
	protected boolean provvedimentoHasSac(){
		if(compilatoProvvedimento() && model.getStep1Model().getProvvedimento().getUidStrutturaAmm()!=null
				&& model.getStep1Model().getProvvedimento().getUidStrutturaAmm().intValue()>0){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Serve a pilotare il siope in accordo con la selezione di un capitolo
	 */
	protected void pilotaSiope(){
		String codiceSiopeCapitolo = model.getStep1Model().getCapitolo().getCodiceSiopeSpesa();
		if(!StringUtils.isEmpty(codiceSiopeCapitolo)){
			codiceSiopeChangedInternal(codiceSiopeCapitolo);
		}
		//
	}
	
	protected <MMG extends ModificaMovimentoGestione> boolean isDiImporto(MMG modifica){
		boolean diImporto = false;
	    if(modifica.getImportoNew()!=null || modifica.getImportoOld()!=null){
	    	diImporto = true;
	    }
	    return diImporto;
	}

	public String getTestoConfermaPrimeNote(){
		return "La competenza economico patrimoniale &egrave; dell''esercizio in corso o degli esercizi futuri?";
	}
	
	public String getTestoConfermaInserisciSdf(){
		return ErroreFin.WARNING_CONFERMA_SALVA_IMPEGNO_SDF.getCodice()+ " - "+ ErroreFin.WARNING_CONFERMA_SALVA_IMPEGNO_SDF.getMessaggio();
	}
	
	public String getTestoConfermaInserisciSdfDiNuovoDisp(){
		return ErroreFin.WARNING_IMPEGNO_SDF_CON_DISPONIBILE_SALVA.getCodice()+" - "+ ErroreFin.WARNING_IMPEGNO_SDF_CON_DISPONIBILE_SALVA.getMessaggio();
	}
	
	protected <SR extends ServiceResponse> boolean presenteSoloErroreDispDodicesimi(SR response){
		boolean presenteSoloErroreDispDodicesimi = false;
		if(response.getErrori() != null && response.getErrori().size() == 1){
			Errore errore = response.getErrori().get(0);
			if("Impegno: disponibilit&agrave; dodicesimi &egrave; insufficiente.".equalsIgnoreCase(errore.getDescrizione())){
				presenteSoloErroreDispDodicesimi = true;
			}
		}
		return presenteSoloErroreDispDodicesimi;
	}
	
	protected List<Errore> controlloObbligatorietaProgetto(List<Errore> listaErrori){
		if(listaErrori==null){
			listaErrori = new ArrayList<Errore>();
		}
		 //OBBLIGATORIETA' PROGETTO:
	    if(oggettoDaPopolareImpegno() && isEnteConProgettoObbligatorio()){
	    	//obbligatorio solo se l'ente ha la variabile di obbligatorieta' del progetto
	    	//e per l'impegno
	    	if(StringUtils.isEmpty(model.getStep1Model().getProgetto())){
	    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Progetto"));
	    	}
	    }
	    //
	    return listaErrori;
	}
	
	protected boolean cercaCapitoliAmmessiPerDecentrato(List<VincoloCapitoli> vincoliConFlagTrasferimentiVincolati){
		// se sono decentrato gli accertamenti devono avere come
		// capitoli quelli ammessi dai vincoli
		
		boolean trovatiErrori = false;
		
		List<VincoloImpegno> vincoliNonAccettabile = new ArrayList<VincoloImpegno>();
		List<CapitoloEntrataGestione> capitoliNonInVincoli = new ArrayList<CapitoloEntrataGestione>();
		if(vincoliConFlagTrasferimentiVincolati!=null && vincoliConFlagTrasferimentiVincolati.size()>0){
			
			List<Integer> capitoliAmmessiIds = new ArrayList<Integer>();
			for(VincoloCapitoli vincoloCapIt : vincoliConFlagTrasferimentiVincolati){
				List<Integer> idList = FinUtility.getIdList(vincoloCapIt.getCapitoliEntrataGestione());
				if(idList!=null && idList.size()>0){
					capitoliAmmessiIds.addAll(idList);
				}
			}

			if(model.getStep1Model()!=null && !isEmpty(model.getStep1Model().getListaVincoliImpegno())){
				for (VincoloImpegno vi : model.getStep1Model().getListaVincoliImpegno()) {
					if(vi!=null && vi.getAccertamento()!=null){
						CapitoloEntrataGestione capitoloEntrataIterato = vi.getAccertamento().getCapitoloEntrataGestione();
						if(capitoloEntrataIterato!=null){
							int idCapitoloIterato = capitoloEntrataIterato.getUid();
							if(!capitoliAmmessiIds.contains(idCapitoloIterato)){
								vincoliNonAccettabile.add(vi);
								capitoliNonInVincoli.add(capitoloEntrataIterato);
							}	
						}
					}
				}
			}
			
			List<String> codiciGiaSegnati = new ArrayList<String>();
			if(!isEmpty(vincoliNonAccettabile)){
				for(VincoloImpegno vincoloNonAccIt : vincoliNonAccettabile){
					if(vincoloNonAccIt!=null){
						for(VincoloCapitoli vincoloCapIt : vincoliConFlagTrasferimentiVincolati){
							List<Integer> idCapitoliCollegati = FinUtility.getIdList(vincoloCapIt.getCapitoliEntrataGestione());
							int udiCapitoloAccertamento = vincoloNonAccIt.getAccertamento().getCapitoloEntrataGestione().getUid();
							if(idCapitoliCollegati!=null && !idCapitoliCollegati.contains(udiCapitoloAccertamento)){
								String codiceVincolo = vincoloCapIt.getCodice();
								if(codiceVincolo==null){
									codiceVincolo = "codice vincolo null";
								}
								if(!codiciGiaSegnati.contains(codiceVincolo)){
									if(isAzioneAbilitata(CodiciOperazioni.OP_SPE_gestisciImpegno)){
										addPersistentActionWarning(ErroreFin.CAPITOLO_ENTR_NON_IN_VINCOLO.getErrore(codiceVincolo).getCodice() +" "+ErroreFin.CAPITOLO_ENTR_NON_IN_VINCOLO.getErrore(codiceVincolo).getDescrizione());	
									} else {
										addErrore(ErroreFin.CAPITOLO_ENTR_NON_IN_VINCOLO.getErrore(codiceVincolo));
										trovatiErrori = true;
									}
								}
								codiciGiaSegnati.add(codiceVincolo);
							}
						}
					}
				}
			}
		
		}
		return trovatiErrori;
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
		return true;
	}

	@Override
	public String confermaPdc() {
		return SUCCESS;
	}


	@Override
	public String confermaSiope() {
		return SUCCESS;
	}

	public GestisciImpegnoStep1Model getOggetto() {
		return oggetto;
	}

	@Override
	public boolean datiUscitaImpegno() {
		if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO)) return true;
		else
			return false;
	}

	public void setOggetto(GestisciImpegnoStep1Model oggetto) {
		this.oggetto = oggetto;
	}

	public String getRadioCodiceAccPerVincoli() {
		return radioCodiceAccPerVincoli;
	}

	public void setRadioCodiceAccPerVincoli(String radioCodiceAccPerVincoli) {
		this.radioCodiceAccPerVincoli = radioCodiceAccPerVincoli;
	}

	public Accertamento getAccertamentoPerVincolo() {
		return accertamentoPerVincolo;
	}

	public void setAccertamentoPerVincolo(Accertamento accertamentoPerVincolo) {
		this.accertamentoPerVincolo = accertamentoPerVincolo;
	}

	public String getAnnoAccDaPassare() {
		return annoAccDaPassare;
	}

	public void setAnnoAccDaPassare(String annoAccDaPassare) {
		this.annoAccDaPassare = annoAccDaPassare;
	}

	public String getNumeroAccDaPassare() {
		return numeroAccDaPassare;
	}

	public void setNumeroAccDaPassare(String numeroAccDaPassare) {
		this.numeroAccDaPassare = numeroAccDaPassare;
	}

	public String getAnnoAccPerAggiorna() {
		return annoAccPerAggiorna;
	}

	public void setAnnoAccPerAggiorna(String annoAccPerAggiorna) {
		this.annoAccPerAggiorna = annoAccPerAggiorna;
	}

	public String getNumeroAccPerAggiorna() {
		return numeroAccPerAggiorna;
	}

	public void setNumeroAccPerAggiorna(String numeroAccPerAggiorna) {
		this.numeroAccPerAggiorna = numeroAccPerAggiorna;
	}

	public boolean isShowModaleConfermaSalvaConBypassDodicesimi() {
		return showModaleConfermaSalvaConBypassDodicesimi;
	}


	public void setShowModaleConfermaSalvaConBypassDodicesimi(
			boolean showModaleConfermaSalvaConBypassDodicesimi) {
		this.showModaleConfermaSalvaConBypassDodicesimi = showModaleConfermaSalvaConBypassDodicesimi;
	}
	
	
	/**
	 * @return the fromModaleConfermaSalvaConBypassDodicesimi
	 */
	public boolean isFromModaleConfermaSalvaConBypassDodicesimi() {
		return fromModaleConfermaSalvaConBypassDodicesimi;
	}

	/**
	 * @param fromModaleConfermaSalvaConBypassDodicesimi the fromModaleConfermaSalvaConBypassDodicesimi to set
	 */
	public void setFromModaleConfermaSalvaConBypassDodicesimi(
			boolean fromModaleConfermaSalvaConBypassDodicesimi) {
		this.fromModaleConfermaSalvaConBypassDodicesimi = fromModaleConfermaSalvaConBypassDodicesimi;
	}

	public String getUidAvanzoVincoloDaPassare() {
		return uidAvanzoVincoloDaPassare;
	}

	public void setUidAvanzoVincoloDaPassare(String uidAvanzoVincoloDaPassare) {
		this.uidAvanzoVincoloDaPassare = uidAvanzoVincoloDaPassare;
	}

	public String getUidAvanzoVincoloPerAggiorna() {
		return uidAvanzoVincoloPerAggiorna;
	}

	public void setUidAvanzoVincoloPerAggiorna(String uidAvanzoVincoloPerAggiorna) {
		this.uidAvanzoVincoloPerAggiorna = uidAvanzoVincoloPerAggiorna;
	}
	
	//CONTROLLO VINCOLO 6929
	
//	private Boolean verificaAccertamentoVincoloOLD(Accertamento accertamento){
		
//		String statoProvvedimento = model.getStep1Model().getProvvedimento().getStato();
//		String statoAccertamento = null;
//		String tipoAccertamento = null;
//		int uidAccertamento = 0;
//		
//		//atto amministrativo associato all'accertamento
//		AttoAmministrativo atto = accertamento.getAttoAmministrativo();
//		
//		//1 Condizione: è possibile aggiungere un vincolo con un accertamento a cui non è associato nulla, 
//		//non avendo associato all'impegno ne un provvedimento
//		if(atto == null || model.getStep1Model().getProvvedimento().getUid()==0){
//			return true;
//		}
//		//Se all'accertamento è associato un atto amministrativo...
//		if(accertamento.getAttoAmministrativo() != null){
//			statoAccertamento =  accertamento.getAttoAmministrativo().getStatoOperativo();
//			tipoAccertamento = accertamento.getAttoAmministrativo().getTipoAtto().getCodice();
//			uidAccertamento = accertamento.getAttoAmministrativo().getUid();
//			int uidProvvedimento =  model.getStep1Model().getProvvedimento().getUid();
//			
//			//Se lo stato del provvedimento è null, dunque non vi è provvedimento, è possibile associare
//			if(statoProvvedimento==null){
//				return true; //true non è possibile associare
//			}
//			//se entrambi sono di tipo determina (sia definitiva sia proposta
//			
//			//TODO al prossimo sviluppo, inserire Costanti nelle enum e migliorare condizione
//			if(areDeterminaOrPropostaDetermina(model.getStep1Model().getProvvedimento().getCodiceTipoProvvedimento(), tipoAccertamento)){
//				//se entrambi sono provvisorio ma diversi
//				if(statoProvvedimento.equals("PROVVISORIO") && statoAccertamento.equals("PROVVISORIO") &&  uidAccertamento>0 && (uidAccertamento != uidProvvedimento)){
//					return true;		
//				}else if(statoProvvedimento.equals("DEFINITIVO")) {
//					return true;
//				}
//			}else {
//				return true;
//			}
//	
//		}else{
//			//se l'atto è null, non permetti l'associazione
//			return true;
//		}
//		
//		return false;
//	}
		
		
	/*
	 * SIAC-7299
	 * Inserimento nuove regole per il vincolo con accertamento
	 * 
	 */
	protected boolean verificaAccertamentoVincolo(Accertamento accertamento){
		
		/*
		 *  l'impegno è PROVVISORIO collegato a una proposta di determina (atto di STILO)
		 *  E l'accertamento che si intende collegare è in stato PROVVISORIO  
		 *  E l’atto associato all'accertamento differisce da quello associato all'impegno
		 *  
		 *  non sarà possibile siglare il vincolo !!! 
		 */
		
		//RICAVARE IL PROVVEDIMENTO
		if( model.getStep1Model().getProvvedimento() != null && MOVGEST_PROVVISORIO.equals(model.getStep1Model().getProvvedimento().getStato()) &&
				model.getStep1Model().getProvvedimento().getUid()!=null
				&& model.getStep1Model().getProvvedimento().getUid().intValue()!=0 ){
				//CEHCK STILO
				if(model.getStep1Model().getProvvedimento().getProvenienza()!= null ){
					String provenienzaLower = model.getStep1Model().getProvvedimento().getProvenienza().toLowerCase();
					if(provenienzaLower.contains(PROVENIENZA_STILO.toLowerCase())){
						//CHECK SU ACCERTAMENTO
						if(accertamento == null || accertamento.getAttoAmministrativo()== null){
							return true;
						}else{
							if(MOVGEST_PROVVISORIO.equals(accertamento.getDescrizioneStatoOperativoMovimentoGestioneEntrata())
									&& accertamento.getAttoAmministrativo() != null && accertamento.getAttoAmministrativo().getUid()!=0
									&& model.getStep1Model().getProvvedimento().getUid().intValue()!= accertamento.getAttoAmministrativo().getUid()
									){
								return true;
			}
						}
						
						
						
					}
			}
		}
		return false;
	
		
	}
	
	
	
	
	
	
	
	
	//FIXME - da migliorare
	private boolean areDeterminaOrPropostaDetermina(String tipoProvvedimento, String tipoAccertamento ){
		
		if(tipoProvvedimento.equals("PDD") && tipoAccertamento.equals("PDD")){
			return true;
		}else{
			return false;
		}
		
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

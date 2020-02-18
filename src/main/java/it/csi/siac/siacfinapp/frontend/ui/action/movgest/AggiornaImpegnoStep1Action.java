/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbito;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbitoResponse;
import it.csi.siac.siacbilser.model.VincoloCapitoli;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.AccertamentoRicercaModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciImpegnoStep1Model;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProgettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.EsistenzaProgettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProvvedimento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProvvedimentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacgenser.model.ProgettoModelDetail;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaImpegnoStep1Action extends ActionKeyAggiornaImpegno {
	private String numeroImpegno;
	private String annoImpegno;
	private String arrivoDaInserimento;
	
	private static final long serialVersionUID = 1L;
	
	private static final String SI = "Si";
	private static final String NO = "No";
	private Boolean flagValido;
	private Boolean flagSoggettoValido;
	
	private final String DETTAGLIO_IMPORTO_CON_VINCOLO = "dettaglioImportoConVincolo";
	
	
	public AggiornaImpegnoStep1Action () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.IMPEGNO;
	}
	
	/**
	 *   settembre 2017 SIAC-5288
	 * @return
	 */
	public boolean isAbilitatoInserimentoVincolo(){
		return super.isAbilitatoAggiornamentoGenerico();
	}
	
	/**
	 *  settembre 2017 SIAC-5288
	 */
	@Override
	public boolean abilitatoModificaProvvedimento(){
		return isAbilitatoAggiornamentoGenerico();
	}
	
	@Override
	public boolean sonoInInserimento() {
		// ritorna sempre false
		return super.sonoInInserimento();
	}
	
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		this.model.setTitolo("Aggiorna Impegno - Dati Impegno");
		
		this.model.setOperazioneAggiorna(true);
		try{	
			if(model.getListaTipiProvvedimenti()==null || model.getListaTipiProvvedimenti().size()==0){
				caricaTipiProvvedimenti();
			}
			
			if(model.getListaTipiAmbito()==null || model.getListaTipiAmbito().isEmpty()){
				caricaListaAmbiti();
			}
			
			caricaStatiOperativiAtti();
			
			caricaListePerRicercaSoggetto();
			caricaListeGestisciImpegnoStep1();
			
			model.getStep1Model().setDaRiaccertamento(buildListaSiNo());
			model.getStep1Model().setListflagAttivaGsa(buildListaSiNo());
			
			model.getStep1Model().setDaPrenotazione(buildListaSiNo());
		    model.getStep1Model().setDiCassaEconomale(buildListaSiNo());
		    
		    model.getStep1Model().setScelteFrazionabile(buildListaFrazionabile());
			
			// non agisce l'ancora dei vincoli 
		    model.getStep1Model().setPortaAdAltezzaVincoli(false);
		    
		    //tipo debito siope:
		    model.getStep1Model().setScelteTipoDebitoSiope(buildListaTipoDebitoSiopePerImpegni());
		    
		}catch(Exception e){			
			log.error("prepare", e.getMessage());
		}
		
		setVisualizzaLinkConsultaModificheProvvedimento(true);
		
		setAbilitazioni();

	}

	private void caricaListaAmbiti() {
		
		RicercaTipiAmbito request = model.creaRequestRicercaTipiAmbito();
		request.setAnno(Integer.parseInt(sessionHandler.getAnnoEsercizio()));
		RicercaTipiAmbitoResponse response = progettoService.ricercaTipiAmbito(request);

		model.setListaTipiAmbito(response.getTipiAmbito());
	}
	
	public boolean isVisibileFlagFrazionabile(){
		//In aggiornamento il frazionabile compare solo se il bilancio e' in stato "Esercizio Provvisoro"
		return isBilancioAttualeInEsercizioProvvisorio();
	}
	
	public boolean isModificabileFlagFrazionabile(){
		//aggiornabile solo se provvisiorio
		if(model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa().equals(Constanti.MOVGEST_STATO_PROVVISORIO)){
			return true;
		}else {
			return false;
		}
	}
	
	public boolean isAbilitatoAggiornamentoCampiSiopePlus(){
		boolean abilitato = false;
		
		//il siope pluse e' aggiornabile se
		//1. siamo abilitati all'agiornamento secondo le regole generiche
		//e
		//2. l'impegno e' in stato provvisorio
		
		//10 NOVEMBRE 2017 per SIAC-5524 cambiano le regole di aggiornamento:
		//aggiornamento consentito per gli impegni Definitivi e definitivi non liquidabili
		//che NON abbiano nessun movimento collegato
		
		if(isAbilitatoAggiornamentoGenerico()){
			
			if(isImpegnoInAggiornamentoProvvisorio()){
				//se e' provvisorio ritorno direttamente true
				return true;
			}
			
			if(isImpegnoInAggiornamentoDefintivoNonLiquidabile()){
				//se e' definitivo non liquidabile devo verificare se ha sub o meno:
				return !presenteAlmenoUnMovValido(model.getListaSubimpegni(), OggettoDaPopolareEnum.SUBIMPEGNO.toString());
			}
			
			//se sono qui e' definitivo:
			BigDecimal disponibilitaLiqDaFunction = model.getImpegnoInAggiornamento().getDisponibilitaLiquidareBase();
			
			if(disponibilitaLiqDaFunction.compareTo(model.getImpegnoInAggiornamento().getImportoAttuale())==0){
				return true;
			} else {
				return false;
			}
			
		}
		return abilitato;
	}
	
	/**
	 * semplice utility per verificare se lo stato dell'impegno in aggiornamento e' o meno provvisorio
	 * @return
	 */
	private boolean isImpegnoInAggiornamentoProvvisorio(){
		if(Constanti.MOVGEST_STATO_PROVVISORIO.equals(model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa())){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * semplice utility per verificare se lo stato dell'impegno in aggiornamento e' o meno defintivo non liquidabile
	 * @return
	 */
	private boolean isImpegnoInAggiornamentoDefintivoNonLiquidabile(){
		if(Constanti.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equals(model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa())){
			return true;
		} else {
			return false;
		}
	}
	
	private void setAbilitazioni(){
		if(model.getStep1Model().getNumeroImpegno()!=null){
			
			setAbilitaModificaImporto(true);
			
			boolean bilPrecInPredisposizioneConsuntivo = isBilancioPrecedenteInPredisposizioneConsuntivo();
			
			setFlagValido(false);
			setFlagSoggettoValido(false);
			//stato D o ND
			if(!model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa().equals(Constanti.MOVGEST_STATO_PROVVISORIO)){
				setFlagValido(true);
				
				//nel caso in cui fossimo in predisposizione consuntivo, rimettiamo flagvalido a false
				//in modo che l'importo ritorni modificabile:
				
				if(bilPrecInPredisposizioneConsuntivo && isResiduo()){
					setAbilitaModificaImporto(true);
				} else {
					setAbilitaModificaImporto(false);
				}
				//
				
				setFlagSoggettoValido(true);
			}
			
			//JIRA  SIAC-3506 in caso di residuo con presenza di modifiche di importo valide, importo non modificabile:
			List<ModificaMovimentoGestioneSpesa> modifiche = model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa();
			if(presenteModificaDiImportoValida(modifiche) && isResiduo()){
				setAbilitaModificaImporto(false);
			}
			//
			
			//stato
			if(model.getImpegnoInAggiornamento().getElencoSubImpegni()!=null || (model.getListaSubimpegni()!=null && model.getListaSubimpegni().size()>0)){
				setFlagSoggettoValido(true);
			}
			
			// jira-1582
			// se e' in stato N e ci sono dei sub allora posso modificare il soggetto
			if(model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa().equals(Constanti.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE)){
				if(!presenteAlmenoUnMovValido(model.getListaSubimpegni(), OggettoDaPopolareEnum.SUBIMPEGNO.toString())){
					setFlagSoggettoValido(false);
				}
			}
		}
		if(model.getStep1Model().getProgetto()!= null){
			model.getStep1ModelSubimpegno().setProgetto(model.getStep1Model().getProgetto());
		}
	}
	
	/**
	 * metodo execute della action
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		//info per debug:
		setMethodName("execute");
		
		//ripopoliamo i dati provv dal model support:
		ripopolaProvvedimentoDaSupport();
		
		//Anno capitolo
		if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
			model.getStep1Model().getCapitolo().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
			model.getCapitoloRicerca().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
		}
	
		//Anno impegno
		if(getAnnoImpegno()!=null){
			model.getStep1Model().setAnnoImpegno(Integer.valueOf(getAnnoImpegno()));
			model.getStep1Model().setNumeroImpegno(Integer.valueOf(getNumeroImpegno()));
		} else {
			model.getStep1Model().setAnnoImpegno(model.getAnnoImpegno());
			model.getStep1Model().setNumeroImpegno(model.getNumeroImpegno());
		}

		//arrivo da inserimento o da indietro:
		if(getArrivoDaInserimento()!=null){
			if(getArrivoDaInserimento().equalsIgnoreCase(SI)){
				model.setFlagIndietro(true);
			}	
		} else if(model.getFlagIndietro()==null){
			model.setFlagIndietro(false);
		}
		
		//transazione elementare:
		if(teSupport==null){
			pulisciTransazioneElementare();
		}
		// utilizzato per la transazione e le condizioni di obbligatorieta
		teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.IMPEGNO.toString());
		
		if(forceReload){
			
			//CARICAMENTO AVANZOVINCOLI:
			caricaListaAvanzovincolo();
			initSceltaAccertamentoAvanzoList();
			//
			
			sessionHandler.cleanSafelyExcluding(FinSessionParameter.IMPEGNO_CERCATO);
			caricaDati(false);
			sessionHandler.setParametro(FinSessionParameter.IMPEGNO_CERCATO, getImpegnoToUpdate());
			
			// Jira - 1298 altrimenti carica ad ogni giro tutti i dati da bilancio
			if (caricaListeBil(WebAppConstants.CAP_UG)) {
				return INPUT;
			}
			
			if(model.getImpegnoInAggiornamento().getProgetto()!=null && model.getImpegnoInAggiornamento().getProgetto().getCodice()!=null){
				popolaProgetto(model.getImpegnoInAggiornamento().getProgetto());
			}
			
			if(((MovimentoGestione) sessionHandler.getParametro(FinSessionParameter.IMPEGNO_CERCATO)).isFlagSoggettoDurc()){
				model.getStep1Model().setSoggettoDurc(SI);
			} else {
				model.getStep1Model().setSoggettoDurc(NO);
			}
			
		}
		
		if(((MovimentoGestione) sessionHandler.getParametro(FinSessionParameter.IMPEGNO_CERCATO)).isFlagDaRiaccertamento()){
			model.getStep1Model().setRiaccertato(SI);
		} else {
			model.getStep1Model().setRiaccertato(NO);
		}
		
		//labels aggiorna:
		caricaLabelsAggiorna(1);
		
		//Metodo di salvataggio del model in model di cache
		creaMovGestModelCache();
		
		//controlliamo lo stato provvedimento:
		controllaStatoProvvedimento();
		
		model.setStep1ModelSubimpegno(new GestisciImpegnoStep1Model());
		model.setStep1ModelSubimpegnoCache(new GestisciImpegnoStep1Model());
		
		//disabilito il caricamento degli alberi inutili per questo scnario (in AjaxAction.java):
		teSupport.setRicaricaAlberoPianoDeiConti(false);
		//CR-2023
		teSupport.setRicaricaStrutturaAmministrativa(false);
		teSupport.setRicaricaSiopeSpesa(false);
		//////////////////////////////////////////////////////////////////////////////////////////
		
		// setto l'anno capitolo per la ricerca guidata del vincolo
		if(model.getStep1Model().getCapitolo()!=null && model.getStep1Model().getCapitolo().getAnno()!=null){
			model.getStep1Model().setAccertamentoRicerca(new AccertamentoRicercaModel());
			model.getStep1Model().getAccertamentoRicerca().setAnnoCapitolo(String.valueOf(model.getStep1Model().getCapitolo().getAnno()));
		}
		
		calcolaTotaliUtilizzabile();
		
		
		setAbilitazioni();
		
		if(salvaDaSDFANormale()){
			addActionWarningFin(ErroreFin.WARNING_IMPEGNO_SDF_CON_DISPONIBILE);
		}
		
		leggiEventualiErroriEWarningAzionePrecedente(true);
		
		return SUCCESS;
	}  
	
	/**
	 * ripete il movimento di gestione e riporta l'utente in inserimento
	 * @return
	 */
	public String ripeti(){
		//setto il parametro che serve a pilotare il ripeti:
		sessionHandler.setParametro(FinSessionParameter.MOVIMENTO_GESTIONE_DA_RIPETERE, model);
		return "inserisceImpegno";
	}
	
	// siac-3224
	public String consultaModificheProvvedimento() throws Exception{
		//info per debug:
		setMethodName("consultaModificheProvvedimento");
		//leggo i dati necessari:
		Impegno impegno = model.getImpegnoInAggiornamento();
		leggiStoricoProvvedimentoByMovimento(impegno);
		return "consultaModificheProvvedimento";
	}

	/**
	 * salvataggio bypassando controllo dodicesimi
	 * @return
	 * @throws Exception
	 */
	public String salvaConByPassDodicesimi() throws Exception {
		
		setFromModaleConfermaSalvaConBypassDodicesimi(true);
		
		setFromModaleConfermaModificaProvvedimento(true);
		setFromModaleConfermaSalvaModificaVincoli(true);
		setFromPopup(true);
		
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
	
	public String salvaInternal(boolean byPassDodicesimi) throws Exception {
		setMethodName("salva");
		
		//controlli provvedimento rispetto all'abilitazione a gestire l'impegno decentrato:
		String controlloProvv = provvedimentoConsentito();
		if(controlloProvv!=null){
			return controlloProvv;
		}
		

		
		  //SIAC-6693
		  //addPersistentActionWarning(IMP_NON_TUTTO_VINCOLATO);
		  Map<TipologiaGestioneLivelli, String> gestioneLivelli = model.getEnte().getGestioneLivelli();
		  String vincoloDec = gestioneLivelli.get(TipologiaGestioneLivelli.BLOCCO_VINCOLO_DEC);	

		// FIXME: forse questo controllo è di troppo, è gia fatto nel provvedimentoConsentito(), verificare con Silvia
		// Controllo SAC con STRUTTURA AMMINISTRATIVA
		// se nel provv c'e' la struttura allora devo verificare che sia una di quelle ammesse
		// Jira 1648 : devo togliere il controllo sul null, altrimenti non esegue sulla modifica del provvedimento scegliendone uno senza sac
		// sposto il controllo sul null in controlloSACStrutturaAmmUtenteDecentrato, cosi da poter verificare se il decentrato ha strutture
		// e, se e'  uidStrutturaAmm e' null, lancio il warning 
//		if(!controlloSACStrutturaAmmUtenteDecentrato(sessionHandler.getAzione().getNome(), model.getStep1Model().getProvvedimento().getUidStrutturaAmm())){
//			addPersistentActionWarning(ErroreFin.PROVVEDIMENTO_DECENTRATO_NON_VALIDO.getErrore().getCodice()+" - "+ErroreFin.PROVVEDIMENTO_DECENTRATO_NON_VALIDO.getErrore().getDescrizione());
//			return INPUT;
//		}
		
		//ricontrolliamo il siope:
		codiceSiopeChangedInternal(teSupport.getSiopeSpesaCod());
		//
		
		
		EsistenzaProgettoResponse esistenzaResp = new EsistenzaProgettoResponse();
		// verifica PROGETTO
		if(StringUtils.isNotEmpty(model.getStep1Model().getProgetto())){
			// metodo che verifica il progetto
			esistenzaResp = esistenzaProgetto();
	    }
		
		ListaPaginata<VincoloCapitoli> listaVincoliSpesa = getVincoliCapitoloSpesa(model.getStep1Model().getCapitolo());
		List<VincoloCapitoli> vincoliConFlagTrasferimentiVincolati = getConFlagTrasferimenti(listaVincoliSpesa);
		//CONTROLLO SU VINCOLI CAPITOLI:
		 boolean erroreVincoliCapitoli = cercaCapitoliAmmessiPerDecentrato(vincoliConFlagTrasferimentiVincolati);
		 if(erroreVincoliCapitoli){
			 return INPUT;
		 }
		
		// presenza di vincoli
		if(model.getStep1Model().getListaVincoliImpegno()!=null && !model.getStep1Model().getListaVincoliImpegno().isEmpty()){
			if((model.getStep1Model().getTotaleImportoDaCollegare().compareTo(BigDecimal.ZERO))>0){
				if(!model.isVisualizzaWarningImpegnoNonTotVincolato()){
					
					//SIAC-6693
					log.info(getMethodName(), "isSoggettoDecentrato1 "+ isSoggettoDecentrato());
					log.info(getMethodName(), "isAzioneDecentrato1 "+ isAzioneDecentrato());
					log.info(getMethodName(), "vincoloDec1 "+ vincoloDec);

					if(isSoggettoDecentrato() && isAzioneDecentrato() && vincoloDec!=null){
						  //addPersistentActionError(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());		
						  addErrore(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore(""));
					  }else{
						  addPersistentActionWarning(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());
					  }

					//addPersistentActionWarning(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());
					
					
					model.setVisualizzaWarningImpegnoNonTotVincolato(true);
					return INPUT;
				}
			}else if(model.getStep1Model().getTotaleImportoDaCollegare().compareTo(BigDecimal.ZERO)<0){
				
				addErrore(ErroreFin.TOT_COLLEGA_VINCOLO.getErrore(""));
				return INPUT;
			}
			
		}else{
		// se lista vincoli vuota ma obbligatorio == true
			if(!isEmpty(vincoliConFlagTrasferimentiVincolati)){
				addPersistentActionWarning(IMP_NON_TUTTO_VINCOLATO);
			}
			
			if(esistenzaResp.isFlagEsistenzaFPV()){
				//SIAC-6693
				log.info(getMethodName(), "isSoggettoDecentrato2 "+ isSoggettoDecentrato());
				log.info(getMethodName(), "isAzioneDecentrato2 "+ isAzioneDecentrato());
				log.info(getMethodName(), "vincoloDec2 "+ vincoloDec);

				  if(isSoggettoDecentrato() && isAzioneDecentrato() && vincoloDec!=null){
					  //addPersistentActionError(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());		
					  addErrore(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore(""));
			  
				  }else{
					  addPersistentActionWarning(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());
				  }

				//addPersistentActionWarning(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());
    		}
			
			//Map<TipologiaGestioneLivelli, String> gestioneLivelli1 = model.getEnte().getGestioneLivelli();
				
		}
		
		// controlli di congruenza di CIG e CUP e SIOPE PLUS:
		controlliCigCupESiopePlus(model.getStep1Model());
	    //
	    
	    //OBBLIGATORIETA' PROGETTO:
	    Errore erroreObbligatorietaProgetto = FinUtility.getFirst(controlloObbligatorietaProgetto(null));
	    if(erroreObbligatorietaProgetto!=null){
	    	addErrore(erroreObbligatorietaProgetto);
	    }
	    //
		
	    if(hasActionErrors()){
	    	return INPUT;
	    }
		
		setImpegnoToUpdate((Impegno)sessionHandler.getParametro(FinSessionParameter.IMPEGNO_CERCATO));
		
		// SALVATAGGIO STEP1
		String risultato = salvaStep1(byPassDodicesimi);
		
		if(risultato.equalsIgnoreCase("salva")){
			
			//if(isFromModaleConfermaModificaProvvedimento() || isFromModaleConfermaSalvaModificaVincoli()){
			//if(isFromModaleConfermaModificaProvvedimento() ){
			//setSuccessStep1(true);
				
			addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " 
		                   + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());	
			//}
		}
		
		//controllo registrazione andata a buon fine:
		controlloRegistrazioneValidazionePrimaNota();
		//
		
		setFlagValido(false);
		setFlagSoggettoValido(false);
		//stato D o ND
		if(!model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa().equals("P")){
			setFlagValido(true);
			setFlagSoggettoValido(true);
		}
		//stato P
		if(model.getImpegnoInAggiornamento().getElencoSubImpegni()!=null || (model.getListaSubimpegni()!=null && model.getListaSubimpegni().size()>0)){
			setFlagSoggettoValido(true);
		}
		
		if(getImpegnoToUpdate().isFlagDaRiaccertamento()){
			model.getStep1Model().setRiaccertato(SI);
		} else {
			model.getStep1Model().setRiaccertato(NO);
		}

		if(getImpegnoToUpdate().isFlagSoggettoDurc()){
			model.getStep1Model().setSoggettoDurc(SI);
		} else {
			model.getStep1Model().setSoggettoDurc(NO);
		}
		
		
		
		//SIAC-6000
		if(risultato.equalsIgnoreCase("salva") 
				&& model.isEnteAbilitatoGestionePrimaNotaDaFinanziaria()
				&& model.isSaltaInserimentoPrimaNota() 
				&& model.getUidDaCompletare() > 0){
			return GO_TO_GEN;
		}
		//
		
		forceReload = true;
		
		return risultato;
	}
	
	
	
	//PROSEGUI
		public String prosegui() {
			setMethodName("prosegui");	
			List<Errore> listaErrori = new ArrayList<Errore>();	
			//jira 945
			setShowPopUpMovColl(false);
			setProseguiStep1(true);		
			
			// 28/09/2017 : CR richiesta da Vitelli
			// Bisogna poter aggiornare i vincoli anche se il capitolo non ha disponibilità
			if (isFromModaleConfermaProseguiModificaVincoli()) {
				setShowModaleConfermaProseguiModificaVincoli(false);
			}else{
		
				//boolean impegnoDisponibilitaCapitoloZero =  disponibilitaImpegnare().compareTo(BigDecimal.ZERO) <= 0;
				
				// Eseguo il controllo solo se sono nella condizione del capitolo senza disponibilità
				//if(impegnoDisponibilitaCapitoloZero){
				
					if(verificaModificaImportoVincoli(false)){
						
						setProseguiStep1(false);
						return INPUT;
						
					}
				//} 
			}		
			
		  //SIAC-6693
		  //addPersistentActionWarning(IMP_NON_TUTTO_VINCOLATO);
		  Map<TipologiaGestioneLivelli, String> gestioneLivelli = model.getEnte().getGestioneLivelli();
		  String vincoloDec = gestioneLivelli.get(TipologiaGestioneLivelli.BLOCCO_VINCOLO_DEC);	

					
			// controlli di congruenza di CIG e CUP e SIOPE PLUS:
			controlliCigCupESiopePlus(model.getStep1Model());
		    //
		    
		    if(model.getStep1Model().getCapitolo().getNumCapitolo() == null || model.getStep1Model().getCapitolo().getNumCapitolo() == 0){
			    listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Capitolo "));
		    }
		    if(model.getStep1Model().getCapitolo().getArticolo() == null || model.getStep1Model().getCapitolo().getNumCapitolo() == 0){
			    listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Articolo "));
		    }
		    if(model.getStep1Model().getAnnoImpegno() == null || model.getStep1Model().getAnnoImpegno().intValue() == 0){
		    	if(oggettoDaPopolareImpegno()){
			    listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Impegno "));
		    	}else{
		    		 listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Accertamento "));
		    	}
		    } 
		    
		    //OBBLIGATORIETA' PROGETTO:
		    listaErrori = controlloObbligatorietaProgetto(listaErrori);
		    //
		    
		    if(oggettoDaPopolareImpegno()){
		    	  if(model.getStep1Model().getTipoImpegno() == null || "".equalsIgnoreCase(model.getStep1Model().getTipoImpegno())){
		   	 	   listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Impegno "));
		   	    }
		    }
		    
		    if(model.getStep1Model().getImportoFormattato() == null || 
		 	   model.getStep1Model().getImportoFormattato().equals("0") ||
		 	   model.getStep1Model().getImportoFormattato().equals("")){

		 	}else{
		 	   	try{
		 	   		// converto
		 	   		model.getStep1Model().setImportoImpegno(convertiImportoToBigDecimal(model.getStep1Model().getImportoFormattato()));
		 	   	}catch(Exception e){
		 	   		if(oggettoDaPopolareImpegno()){
		 	   			listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo Impegno "));
		 	   		}else{
		 	   			listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo Accertamento "));
		 	   		}
		 	    }
		 	}
		    //IMPLEMENTAZIONE CONTROLLI 2.8

		    //controllo anno e numero finanziamento
		    if(model.getStep1Model().getAnnoFinanziamento()!=null){
		    	if(model.getStep1Model().getAnnoFinanziamento()>model.getStep1Model().getAnnoImpegno()){
		    		listaErrori.add(ErroreFin.INCONGRUENZA_TRA_I_PARAMETRI_IMPEGNO.getErrore("Anno finanziamento maggiore dell'anno impegno"));
		    	}
		    }
				    
	  		//controllo anno e numero riaccertamento	
		    if (NO.equalsIgnoreCase(model.getStep1Model().getRiaccertato())) {
		    	model.getStep1Model().setAnnoImpRiacc(null);
		    	model.getStep1Model().setNumImpRiacc(null);
		    } else {
		    	if (model.getStep1Model().getAnnoImpRiacc() != null && model.getStep1Model().getNumImpRiacc() != null && model.getStep1Model().getAnnoImpegno()!=null ) {
			    	if (model.getStep1Model().getAnnoImpRiacc().compareTo(model.getStep1Model().getAnnoImpegno())>=0) {
			    		listaErrori.add(ErroreFin.INCONGRUENZA_TRA_I_PARAMETRI_RIACCERTAMENTO.getErrore(model.getLabels().get(LABEL_OGGETTO_GENERICO_PADRE),model.getLabels().get(LABEL_OGGETTO_GENERICO_PADRE)));
			    	} else {
			    		RicercaImpegnoPerChiaveOttimizzato rip = new RicercaImpegnoPerChiaveOttimizzato();
			    		rip.setEnte(sessionHandler.getEnte());
			    		rip.setRichiedente(sessionHandler.getRichiedente());
			    		RicercaImpegnoK k = new RicercaImpegnoK();
			    		// Jira - 630
			    		k.setAnnoEsercizio(model.getStep1Model().getAnnoImpOrigine());
			    		k.setAnnoImpegno(model.getStep1Model().getAnnoImpRiacc());
			    		k.setNumeroImpegno(new BigDecimal(model.getStep1Model().getNumImpRiacc()));
			    		rip.setpRicercaImpegnoK(k);
			    		
			    		//MARZO 2016: fix per performance
			    		rip.setCaricaSub(false);
			    		//
			    		
			    		RicercaImpegnoPerChiaveOttimizzatoResponse respRk = movimentoGestionService.ricercaImpegnoPerChiaveOttimizzato(rip);
			    		if(respRk!=null && respRk.getImpegno()!=null){
			    			if(respRk.getImpegno().getAnnoOriginePlur()!=0 &&
			    					respRk.getImpegno().getNumeroOriginePlur() !=null){

				    				
				    				if(model.getStep1Model().getAnnoImpOrigine() == null && model.getStep1Model().getNumImpOrigine() == null){		    				
				    					model.getStep1Model().setAnnoImpOrigine(respRk.getImpegno().getAnnoOriginePlur());
				    				}
				    				if(null!=respRk.getImpegno().getNumeroOriginePlur()){
				    					model.getStep1Model().setNumImpOrigine(respRk.getImpegno().getNumeroOriginePlur().intValue());	
				    				}
				    				
			    			}
			    			
		    				if(respRk.getImpegno().getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("A")){
		    					listaErrori.add(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA_FIN.getErrore("Impegno"," annullato"));
		    				}

			    		}
			    		else{
			    			if(oggettoDaPopolareImpegno()){
			    				//jira 945
			    				if (isFromPopup()) {
					    			setShowPopUpMovColl(false);
					    		}else {
				    				setShowPopUpMovColl(true);
					    			addPersistentActionWarning(ErroreCore.AGGIORNAMENTO_CON_CONFERMA_WARN.getErrore().getCodice()+" : "+ErroreCore.AGGIORNAMENTO_CON_CONFERMA_WARN.getErrore("Impegno riaccertato non","presente","l'aggiornamento").getDescrizione());						    			
					    		}
			    			}else{
			    				listaErrori.add(ErroreFin.MOVIMENTO_NON_TROVATO.getErrore("Accertamento riaccertato"));
			    				//cancello i valori nel model del movimento inesistente
				    			model.getStep1Model().setAnnoImpRiacc(null);
						    	model.getStep1Model().setNumImpRiacc(null);
						    	model.getStep1Model().setRiaccertato(WebAppConstants.No);
			    			}
			    		}
			    	}
			    } else {
			    	if (model.getStep1Model().getAnnoImpRiacc() != null && model.getStep1Model().getNumImpRiacc() == null) {
			    		if(oggettoDaPopolareImpegno()){
			    			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Impegno Riaccertamento "));
			    		}else{
			    			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Accertamento Riaccertamento "));
			    		}
			    		
			    	}  
			    	if (model.getStep1Model().getAnnoImpRiacc() == null && model.getStep1Model().getNumImpRiacc() != null) {
			    		if(oggettoDaPopolareImpegno()){
			    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Impegno Riaccertamento "));
			    	}else{
			    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Accertamento Riaccertamento "));

			    	}
			    	}
			    	if (model.getStep1Model().getAnnoImpRiacc() == null && model.getStep1Model().getNumImpRiacc() == null) {
			    		if(oggettoDaPopolareImpegno()){
			    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Impegno Riaccertamento "));
			    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Impegno Riaccertamento "));
			    	}else{
			    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Accertamento Riaccertamento "));
			    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Accertamento Riaccertamento "));
			    	}
			    	}
			    }
		    }
				    
		    //controllo anno e numero origine
		    if (model.getStep1Model().getAnnoImpOrigine() != null && model.getStep1Model().getNumImpOrigine() != null && model.getStep1Model().getAnnoImpegno()!=null ) {
		    	if (model.getStep1Model().getAnnoImpOrigine().compareTo(model.getStep1Model().getAnnoImpegno())>=0) {
		    		listaErrori.add(ErroreFin.INCONGRUENZA_TRA_I_PARAMETRI_RIACCERTAMENTO.getErrore(model.getLabels().get(LABEL_OGGETTO_GENERICO_PADRE),model.getLabels().get(LABEL_OGGETTO_GENERICO_PADRE)));
		    	}else if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO)){
					RicercaImpegnoPerChiaveOttimizzato rip = new RicercaImpegnoPerChiaveOttimizzato();
		    		rip.setEnte(sessionHandler.getEnte());
		    		rip.setRichiedente(sessionHandler.getRichiedente());
		    		RicercaImpegnoK k = new RicercaImpegnoK();
		    		k.setAnnoEsercizio(model.getStep1Model().getAnnoImpOrigine());
		    		k.setAnnoImpegno(model.getStep1Model().getAnnoImpOrigine());
		    		k.setNumeroImpegno(new BigDecimal(model.getStep1Model().getNumImpOrigine()));
		    		rip.setpRicercaImpegnoK(k);
		    		
		    		//MARZO 2016: fix per performance
		    		rip.setCaricaSub(false);
		    		//
		    		
		    		RicercaImpegnoPerChiaveOttimizzatoResponse respRk = movimentoGestionService.ricercaImpegnoPerChiaveOttimizzato(rip);
		    		
		    		if(respRk==null || respRk.getImpegno()==null){
		    			//jira 945
		    			
		    			if (isFromPopup()) {
			    			setShowPopUpMovColl(false);
			    		}else {
		    				setShowPopUpMovColl(true);
			    			addPersistentActionWarning(ErroreCore.AGGIORNAMENTO_CON_CONFERMA_WARN.getErrore().getCodice()+" : "+ErroreCore.AGGIORNAMENTO_CON_CONFERMA_WARN.getErrore("Impegno origine non","presente","l'aggiornamento").getDescrizione());						    			
			    		}
		    		}	
				} else {
					RicercaAccertamentoPerChiaveOttimizzato rap = new RicercaAccertamentoPerChiaveOttimizzato();
					rap.setEnte(sessionHandler.getEnte());
		    		rap.setRichiedente(sessionHandler.getRichiedente());
		    		RicercaAccertamentoK k = new RicercaAccertamentoK();
		    		// Jira - 630
		    		k.setAnnoEsercizio(model.getStep1Model().getAnnoImpOrigine());
		    		k.setAnnoAccertamento(model.getStep1Model().getAnnoImpOrigine());
		    		k.setNumeroAccertamento(new BigDecimal(model.getStep1Model().getNumImpOrigine()));
		    		rap.setpRicercaAccertamentoK(k);
		    		
		    		RicercaAccertamentoPerChiaveOttimizzatoResponse respRk = movimentoGestionService.ricercaAccertamentoPerChiaveOttimizzato(rap);
		    		
		    		if(respRk==null || respRk.getAccertamento()==null){
		    			listaErrori.add(ErroreFin.MOVIMENTO_NON_TROVATO.getErrore("accertamento origine"));
		    		}
				}
		    } else {
		    	if (model.getStep1Model().getAnnoImpOrigine() != null && model.getStep1Model().getNumImpOrigine() == null) {
		    		if(oggettoDaPopolareImpegno()){
		    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Impegno Origine "));
		    		}else{
		    			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Accertamento Origine "));
		    		}
		    	}  
		    	if (model.getStep1Model().getAnnoImpOrigine() == null && model.getStep1Model().getNumImpOrigine() != null) {
		    		if(oggettoDaPopolareImpegno()){
		    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Impegno Origine "));
		    		}else{
		    			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Accertamento Origine "));
		    		}
		    	}
		    }
				  		
		    //controllo data scadenza
		  	if(!StringUtils.isEmpty(model.getStep1Model().getScadenzaOld())){
		  		
		  		if(StringUtils.isEmpty(model.getStep1Model().getScadenza())){
		  			
		  			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Data Scadenza dd/MM/yyyy"));
		  		} else {
		  			
		  			if(model.getStep1Model().getScadenza().length() != 10){
		  				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
		  			} else {
		  				
		  				if (!DateUtility.isDate(model.getStep1Model().getScadenza(), "dd/MM/yyyy")) {
		  					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
		  				} else {				
		  					String annoScadenza = model.getStep1Model().getScadenza().split("/")[2];
		  					if(Integer.valueOf(annoScadenza) > model.getStep1Model().getAnnoImpegno() || Integer.valueOf(annoScadenza) < model.getStep1Model().getAnnoImpegno()){
		  						// FIXME JIRA: 1751
		  						String tipoMovGest = oggettoDaPopolare.toString();
		  						Integer annoMovGest = model.getStep1Model().getAnnoImpegno();
		  						listaErrori.add(ErroreCore.PARAMETRO_ERRATO.getErrore("Data Scadenza deve essere nell'anno dell'"+ tipoMovGest , model.getStep1Model().getScadenza(), String.valueOf(annoMovGest)));
		  					}
		  				}
		  			}
		  		}
		  		
			  } else {
			  		
				  if(!StringUtils.isEmpty(model.getStep1Model().getScadenza())){
			  			
			  			if(model.getStep1Model().getScadenza().length() != 10){
			  				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
			  			} else {
			  				if (!DateUtility.isDate(model.getStep1Model().getScadenza(), "dd/MM/yyyy")) {
			  					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
			  				} else {
			  					String annoScadenza = model.getStep1Model().getScadenza().split("/")[2];
			  					if(Integer.valueOf(annoScadenza) > model.getStep1Model().getAnnoImpegno() || Integer.valueOf(annoScadenza) < model.getStep1Model().getAnnoImpegno()){
			  						// FIXME JIRA: 1751
			  						String tipoMovGest = oggettoDaPopolare.toString();
			  						Integer annoMovGest = model.getStep1Model().getAnnoImpegno();
			  						listaErrori.add(ErroreCore.PARAMETRO_ERRATO.getErrore("Data Scadenza deve essere nell'anno dell'"+ tipoMovGest , model.getStep1Model().getScadenza(), String.valueOf(annoMovGest)));
			  						
			  					}
			  				}
			  			}
			  		}
			  }
				  		
			  EsistenzaProgettoResponse esistenzaResp = new EsistenzaProgettoResponse();
			  
		  	  //verifico l'esistenza del progetto
			  if(StringUtils.isNotEmpty(model.getStep1Model().getProgetto())){
				  esistenzaResp = esistenzaProgetto();
			    	
				  if(esistenzaResp.isFallimento())
			    		return INPUT;
			  }
					
			  ListaPaginata<VincoloCapitoli> listaVincoliSpesa = getVincoliCapitoloSpesa(model.getStep1Model().getCapitolo());
			  List<VincoloCapitoli> vincoliConFlagTrasferimentiVincolati = getConFlagTrasferimenti(listaVincoliSpesa);
			  
			  //CONTROLLO VINCOLI CAPITOLI:	 
			  boolean erroreVincoliCapitoli = cercaCapitoliAmmessiPerDecentrato(vincoliConFlagTrasferimentiVincolati);
			  
			  
			  if(erroreVincoliCapitoli){
				  return INPUT;
			  }
				
			  

			  
				if(model.getStep1Model().getListaVincoliImpegno()!=null && !model.getStep1Model().getListaVincoliImpegno().isEmpty()){
					if((model.getStep1Model().getTotaleImportoDaCollegare().compareTo(BigDecimal.ZERO))>0){
						//SIAC-6693
						log.info(getMethodName(), "isSoggettoDecentrato3 "+ isSoggettoDecentrato());
						log.info(getMethodName(), "isAzioneDecentrato3 "+ isAzioneDecentrato());
						log.info(getMethodName(), "vincoloDec3 "+ vincoloDec);

						  if(isSoggettoDecentrato() && isAzioneDecentrato() && vincoloDec!=null){
							  //addPersistentActionError(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());		
							  addErrore(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore(""));
						  
						  }else{
							  addPersistentActionWarning(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());
						  }
						  
					}else if(model.getStep1Model().getTotaleImportoDaCollegare().compareTo(BigDecimal.ZERO)<0){
						
						addErrore(ErroreFin.TOT_COLLEGA_VINCOLO.getErrore(""));
						return INPUT;
					}
						
				}else{
					// se lista vincoli vuota ma obbligatorio == true
					if(!isEmpty(vincoliConFlagTrasferimentiVincolati)){
						//addPersistentActionWarning(IMP_NON_TUTTO_VINCOLATO);
						//SIAC-6693
						log.info(getMethodName(), "isSoggettoDecentrato4 "+ isSoggettoDecentrato());
						log.info(getMethodName(), "isAzioneDecentrato4 "+ isAzioneDecentrato());
						log.info(getMethodName(), "vincoloDec4 "+ vincoloDec);

						  if(isSoggettoDecentrato() && isAzioneDecentrato() && vincoloDec!=null){
							  //addPersistentActionError(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());		
							  addErrore(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore(""));
						  
						  }else{
							  addPersistentActionWarning(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());
						  }

					}
					
					if(esistenzaResp.isFlagEsistenzaFPV()){
						//SIAC-6693
						log.info(getMethodName(), "isSoggettoDecentrato5 "+ isSoggettoDecentrato());
						log.info(getMethodName(), "isAzioneDecentrato5 "+ isAzioneDecentrato());
						log.info(getMethodName(), "vincoloDec5 "+ vincoloDec);

						  if(isSoggettoDecentrato() && isAzioneDecentrato() && vincoloDec!=null){
							  //addPersistentActionError(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());		
							  addErrore(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore(""));
					  
						  }else{
							  addPersistentActionWarning(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());
						  }

		    		}
				}
				
				    
			  //controllo disponibilita capitolo
				if(controlloImporti() && !model.getStep1Model().getImprtoVincoliModificato()){
			    	if(oggettoDaPopolareImpegno()){
			    		
			    		if(abilitatoAzioneGestisciImpegnoSDF()){
			    			//se era gia' SDF non devo visualizzare nulla, altrimenti il warning che diventera' SDF
			    			if(!model.getImpegnoInAggiornamento().isFlagSDF()){
			    				addPersistentActionWarningFin(ErroreFin.WARNING_IMPEGNO_SDF);
			    			}
						} else {
							listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE_IMPEGNO.getErrore("impegno", "AGGIORNAMENTO"));
						}
			    		
			    	}else{
			    		addPersistentActionWarning(ErroreFin.SUPERAMENTO_DISPONIBILITA.getCodice()+": "+ErroreFin.SUPERAMENTO_DISPONIBILITA.getErrore("").getDescrizione());
			    	}
			    }  else {
			    	//c'e' disponibilita del capitolo
			    	if(model.getImpegnoInAggiornamento().isFlagSDF()){
			    		//ma l'impegno era stato salvato in precendenza come SDF
			    		//visualizziamo il warning che verra salvato con SDF a false:
			    		addPersistentActionWarningFin(ErroreFin.WARNING_IMPEGNO_SDF_CON_DISPONIBILE);
			    	}
			    } 

				  //controllo stato movimento con soggetto
				if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ACCERTAMENTO)){
					//controllo stato movgest
					if(model.getAccertamentoInAggiornamento().getStatoOperativoMovimentoGestioneEntrata().equals("D")){
						//controllo modifica classe
						if(model.getAccertamentoInAggiornamento().getClasseSoggetto()!=null 
								&& !StringUtils.isEmpty(model.getAccertamentoInAggiornamento().getClasseSoggetto().getCodice())){
							
								//controllo esistenza modifica classe con nuova classe
								if(model.getStep1Model().getSoggetto()!=null
									&&	model.getStep1Model().getSoggetto().getClasse()!=null 
									&& !model.getStep1Model().getSoggetto().getClasse().equals(model.getAccertamentoInAggiornamento().getClasseSoggetto().getCodice())){
										listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Modifica Soggetto non consentita, stato Movimento Valido"));
								}
								//controllo esistenza modifica classe con nuovo soggetto
								else if(model.getStep1Model().getSoggetto()!=null
									&& model.getStep1Model().getSoggetto().getCodCreditore()!=null){
									
										if(model.getAccertamentoInAggiornamento().getSoggetto()!=null
											&& !StringUtils.isEmpty(model.getAccertamentoInAggiornamento().getSoggetto().getCodiceSoggetto())
											&& !model.getStep1Model().getSoggetto().getCodCreditore().equals(model.getAccertamentoInAggiornamento().getSoggetto().getCodiceSoggetto())){
											
												listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Modifica Soggetto non consentita, stato Movimento Valido"));
										}
								}
						}
						//controllo modifica soggetto
						else if(model.getAccertamentoInAggiornamento().getSoggetto()!=null
								&& !StringUtils.isEmpty(model.getAccertamentoInAggiornamento().getSoggetto().getCodiceSoggetto())){
								
								//controllo esistenza modifica soggetto con nuovo soggetto
								if(model.getStep1Model().getSoggetto()!=null
									&& model.getStep1Model().getSoggetto().getCodCreditore()!=null	
									&& !model.getStep1Model().getSoggetto().getCodCreditore().equals(model.getAccertamentoInAggiornamento().getSoggetto().getCodiceSoggetto())){
										listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Modifica Soggetto non consentita, stato Movimento Valido"));
								}
								//controllo esistenza modifica soggetto con nuova classe
								else if(model.getStep1Model().getSoggetto()!=null
									&& !StringUtils.isEmpty(model.getStep1Model().getSoggetto().getClasse())){
									
									if(model.getAccertamentoInAggiornamento().getClasseSoggetto()!=null 
										&& !StringUtils.isEmpty(model.getAccertamentoInAggiornamento().getClasseSoggetto().getCodice())
										&& !model.getStep1Model().getSoggetto().getClasse().equals(model.getAccertamentoInAggiornamento().getClasseSoggetto().getCodice())){
									
											listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Modifica Soggetto non consentita, stato Movimento Valido"));
									}
								}
								
							}
						}
				}else{
						if(model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa().equals("D")){
							//controllo modifica classe
							if(model.getImpegnoInAggiornamento().getClasseSoggetto()!=null 
									&& !StringUtils.isEmpty(model.getImpegnoInAggiornamento().getClasseSoggetto().getCodice())){
								
									//controllo esistenza modifica classe con nuova classe
									if(model.getStep1Model().getSoggetto()!=null
										&&	model.getStep1Model().getSoggetto().getClasse()!=null 
										&& !model.getStep1Model().getSoggetto().getClasse().equals(model.getImpegnoInAggiornamento().getClasseSoggetto().getCodice())){
										listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Modifica Soggetto non consentita, stato Movimento Valido"));
									}
									//controllo esistenza modifica classe con nuovo soggetto
									else if(model.getStep1Model().getSoggetto()!=null
										&& model.getStep1Model().getSoggetto().getCodCreditore()!=null){
										
											if(model.getImpegnoInAggiornamento().getSoggetto()!=null
												&& !StringUtils.isEmpty(model.getImpegnoInAggiornamento().getSoggetto().getCodiceSoggetto())
												&& !model.getStep1Model().getSoggetto().getCodCreditore().equals(model.getImpegnoInAggiornamento().getSoggetto().getCodiceSoggetto())){
												
													listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Modifica Soggetto non consentita, stato Movimento Valido"));
											}
									}
							}
							//controllo modifica soggetto
							else if(model.getImpegnoInAggiornamento().getSoggetto()!=null
									&& !StringUtils.isEmpty(model.getImpegnoInAggiornamento().getSoggetto().getCodiceSoggetto())){
									
									//controllo esistenza modifica soggetto con nuovo soggetto
									if(model.getStep1Model().getSoggetto()!=null
										&& model.getStep1Model().getSoggetto().getCodCreditore()!=null	
										&& !model.getStep1Model().getSoggetto().getCodCreditore().equals(model.getImpegnoInAggiornamento().getSoggetto().getCodiceSoggetto())){
											listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Modifica Soggetto non consentita, stato Movimento Valido"));
									}
									//controllo esistenza modifica soggetto con nuova classe
									else if(model.getStep1Model().getSoggetto()!=null
										&& !StringUtils.isEmpty(model.getStep1Model().getSoggetto().getClasse())){
										
										if(model.getImpegnoInAggiornamento().getClasseSoggetto()!=null 
											&& !StringUtils.isEmpty(model.getImpegnoInAggiornamento().getClasseSoggetto().getCodice())
											&& !model.getStep1Model().getSoggetto().getClasse().equals(model.getImpegnoInAggiornamento().getClasseSoggetto().getCodice())){
										
												listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Modifica Soggetto non consentita, stato Movimento Valido"));
										}
									}								
							}
							
						}
				}
				
				// CR SIAC-3224, in caso di aggiornamento il provvedimento si puo modificare
				if (isFromModaleConfermaProseguiModificaProvvedimento()) {
					setShowModaleConfermaProseguiModificaProvvedimento(false);
					
	    		}else{
					if(verificaModificaProvvedimento(false)){
						setProseguiStep1(false);
						return INPUT;
					}
				}
				
				//controllo formattazione importo
				if(model.getStep1Model().getImportoFormattato() == null || 
					model.getStep1Model().getImportoFormattato().equals("0") ||
					model.getStep1Model().getImportoFormattato().equals("")){
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo "+model.getLabels().get(LABEL_OGGETTO_GENERICO_PADRE)));
				}else{
					try{
						// converto
					    model.getStep1Model().setImportoImpegno(convertiImportoToBigDecimal(model.getStep1Model().getImportoFormattato()));
					    if (model.getStep1Model().getImportoImpegno().compareTo(BigDecimal.ZERO) <= 0) {
					      listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo "+model.getLabels().get(LABEL_OGGETTO_GENERICO_PADRE)));
			    		}
			    	}catch(Exception e){
			    		if(oggettoDaPopolareImpegno()){
			    			listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Importo Impegno ", "numerico"));
					    }else{
					    	 listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Importo Accertamento ", "numerico"));
					    }
					}
				}
				
				//Controllo dello stato del Bilancio
				if(oggettoDaPopolareImpegno()){
					if(controlloStatoBilancio(model.getStep1Model().getAnnoImpegno(),doveMiTrovo,"impegno")){
						return INPUT;
					}
				}else{			
					if(controlloStatoBilancio(model.getStep1Model().getAnnoImpegno(),doveMiTrovo,"accertamento")){
						return INPUT;
					}
				}
				
				// Controllo SAC con STRUTTURA AMMINISTRATIVA
				// se nel provv c'e' la struttura allora devo verificare che sia una di quelle ammesse
				// Jira 1648 : devo togliere il controllo sul null, altrimenti non esegue il controllo se modifico il provevdimento scegliendo uno senza sac
				// sposto il controllo sul null in controlloSACStrutturaAmmUtenteDecentrato, cosi li verifico che se il dec ha strutture
				// e se è uidStrutturaAmm è null lancio il warning 
				String controlloProvv = provvedimentoConsentito();
				if(controlloProvv!=null){
					return controlloProvv;
				}
					
				
				
				
				//in presenza della classe, pulisco totalmente il soggetto
			    controlloSoggettoSelezionatoEClasse();
				
			    //Inserisco la descrizione all'interno della classe del soggetto  vedi Jira e issue di laura
				inserisciDescClasseSoggettoAggiorna();
				
				
				if(oggettoDaPopolareImpegno()){
					// presenza di vincoli
					if(model.getStep1Model().getListaVincoliImpegno()!=null && !model.getStep1Model().getListaVincoliImpegno().isEmpty()){
						
						if((model.getStep1Model().getTotaleImportoDaCollegare().compareTo(BigDecimal.ZERO))>0){
							
							String msgWarningImpegnoNonTotVincolato = ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice().concat(" ").concat(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());
							
							//System.out.println("msgWarningImpegnoNonTotVincolato: " + msgWarningImpegnoNonTotVincolato) ;
							
							boolean aggiungiWarning = true;
							
							if(hasPersistentActionWarnings()){
								
								Collection<String> listaWarning = getActionWarnings();
								for (String warning : listaWarning) {
									
									if(warning.equalsIgnoreCase(msgWarningImpegnoNonTotVincolato)){
										aggiungiWarning = false;
										break;
									}
								}
							}
							
							if(aggiungiWarning){
								
								//SIAC-6693
								log.info(getMethodName(), "isSoggettoDecentrato6 "+ isSoggettoDecentrato());
								log.info(getMethodName(), "isAzioneDecentrato6 "+ isAzioneDecentrato());
								log.info(getMethodName(), "vincoloDec6 "+ vincoloDec);

								  if(isSoggettoDecentrato() && isAzioneDecentrato() && vincoloDec!=null){
									  //addPersistentActionError(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());		
									  addErrore(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore(""));
							  
								  }else{
									  addPersistentActionWarning(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());
								  }

								//addPersistentActionWarning(msgWarningImpegnoNonTotVincolato);
							}
							
						}else if(model.getStep1Model().getTotaleImportoDaCollegare().compareTo(BigDecimal.ZERO)<0){
							
							addErrore(ErroreFin.TOT_COLLEGA_VINCOLO.getErrore(""));
							return INPUT;
						}
					}
				}
				
				
				//SIAC-7321
				if(model.getStep1Model().getProvvedimento()!=null){
			    	if(model.getStep1Model().getListaVincoliImpegno()!= null && !model.getStep1Model().getListaVincoliImpegno().isEmpty()){
			    		List<VincoloImpegno> listaVincoli = model.getStep1Model().getListaVincoliImpegno();	
			    		for(VincoloImpegno v : listaVincoli){
			    				if(v.getAccertamento()!= null){
			    					boolean vincoloNonCorretto = verificaAccertamentoVincolo(v.getAccertamento());
			    					if(vincoloNonCorretto){
			    						listaErrori.add(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Presenza di Vincolo Accertamento non consentito"));
			    						break;
			    					}
			    				}
			    			}
			    	}
			    }
				
				
				
				
			    
			    if(listaErrori.isEmpty()) {
			    	setFromPopup(false);
			    	
					if (isShowPopUpMovColl()) {
						return INPUT;
					}
					
					//prima di proseguire vediamo se va cambiato il valore del flag sanita':
					pilotaFlagSanita();
					//
					
			    	
			    	return "prosegui";
			    } else {
			 	   addErrori(listaErrori);
			 	  
			 	   setFlagValido(false);
			 	   setFlagSoggettoValido(false);
			 	   //stato D o ND
			 	   if(!model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa().equals("P")){
			 		   setFlagValido(true);
					   setFlagSoggettoValido(true);
			 	   }
			 	   //stato P
			 	   if(model.getImpegnoInAggiornamento().getElencoSubImpegni()!=null || (model.getListaSubimpegni()!=null && model.getListaSubimpegni().size()>0)){
			 		   setFlagSoggettoValido(true);
			 	   }
					
				   return INPUT;
			    }
		}

		
		public String annulla()  throws Exception{
			setMethodName("annulla");

			model.setStep1Model(model.getStep1ModelCache());
			creaMovGestModelCache();
			
			//calcola totali
			calcolaTotaliUtilizzabile();
			
			// chiudo il tab vincoli
	        this.annullaValoriVincolo();

			return SUCCESS;
		}
		
	//jira 945
	public String siProsegui() throws Exception{
		setMethodName("siProsegui");

		setFromPopup(true);
		setFromModaleConfermaProseguiModificaProvvedimento(true);
		return prosegui();
	}

	//jira 945
	public String siSalva() throws Exception{
		setMethodName("siSalva");
		setFromPopup(true);
		setFromModaleConfermaModificaProvvedimento(true);
		return salva();
	}

	
	public String salvaDaModaleConfermaSalvaVincoli() throws Exception{
		setMethodName("salvaDaModaleConfermaSalvaVincoli");
		setFromPopup(true);
		setFromModaleConfermaModificaProvvedimento(true);
		setFromModaleConfermaSalvaModificaVincoli(true);
		return salva();
	}

	
	public String proseguiDaModaleConfermaSalvaVincoli() throws Exception{
		setMethodName("proseguiDaModaleConfermaSalvaVincoli");
		setFromPopup(true);
		setFromModaleConfermaModificaProvvedimento(true);
		setFromModaleConfermaProseguiModificaVincoli(true);
		return prosegui();
	}
	
	
	public String indietro(){
	
		return "gotoInserisciImpegno";
	}
	
	/**
	 * visualizza il ppo up con l'importo dell'impegno 
	 * in presenza di eventuali vincoli	
	 * @return
	 */
	public String dettaglioAggiornaImportoConVincoli(){
		
		// passo il valore nel model
		model.getStep1Model().setImportoInModificaFormattato(model.getStep1Model().getImportoFormattato());
		return DETTAGLIO_IMPORTO_CON_VINCOLO;
	}
	

	public String aggiornaImportoConVincolo(){
		
		if(StringUtils.isEmpty(model.getStep1Model().getImportoInModificaFormattato())){
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo "+model.getLabels().get(LABEL_OGGETTO_GENERICO_PADRE)));
			return INPUT;
		}
		// se non vuoto ripasso il valore nella pagina
		model.getStep1Model().setImportoFormattato(model.getStep1Model().getImportoInModificaFormattato());
		
		calcolaTotaliUtilizzabile();
		
		return SUCCESS;
		
	}
	
	/**
	 * pilota l'abilitazione alla modifica del parere finanziario
	 * @return
	 */
	public boolean abilitaModificaParereFinanziario() {
		boolean abilitazioneModifica = false;
		
		//SIAC-5288 settembre 2017 devo considerare anche il caso in cui 
		//sia entrato in aggiornamento ma disponendo
		//solo dell'autorizzazione a modificare gsa,
		//quindi valuto anche isAbilitatoAggiornamentoGenerico:
		if(!isAbilitatoAggiornamentoGenerico()){
			return false;
		}
		
		if(isEnteAbilitatoParereFinanziario() && isAzioneAbilitata(CodiciOperazioni.OP_SPE_gestisciParere)) {
			abilitazioneModifica = true;
		}
		
		return abilitazioneModifica;
	}
	
	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */
	
	public String getAnnoImpegno() {
		return annoImpegno;
	}



	public void setAnnoImpegno(String annoImpegno) {
		this.annoImpegno = annoImpegno;
	}



	public String getNumeroImpegno() {
		return numeroImpegno;
	}

	public void setNumeroImpegno(String numeroImpegno) {
		this.numeroImpegno = numeroImpegno;
	}

	public Boolean getFlagValido() {
		return flagValido;
	}

	public void setFlagValido(Boolean flagValido) {
		this.flagValido = flagValido;
	}

	public String getArrivoDaInserimento() {
		return arrivoDaInserimento;
	}

	public void setArrivoDaInserimento(String arrivoDaInserimento) {
		this.arrivoDaInserimento = arrivoDaInserimento;
	}

	public Boolean getFlagSoggettoValido() {
		return flagSoggettoValido;
	}

	public void setFlagSoggettoValido(Boolean flagSoggettoValido) {
		this.flagSoggettoValido = flagSoggettoValido;
	}

	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacfinapp.frontend.ui.action.GenericPopupAction#convertiModelPerChiamataServizioRicercaProgetto(it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProgettoImpegnoModel)
	 */
	@Override
	public RicercaSinteticaProgetto convertiModelPerChiamataServizioRicercaProgetto(ProgettoImpegnoModel criteriRicercaProgetto) {
		RicercaSinteticaProgetto ricercaSinteticaProgetto = super.convertiModelPerChiamataServizioRicercaProgetto(criteriRicercaProgetto);
		
		ricercaSinteticaProgetto.setProgettoModelDetails(ProgettoModelDetail.Cronoprogrammi);
		
		return ricercaSinteticaProgetto;
	}
	
	@Override
	public String ricercaProgetto() throws Exception {
		super.ricercaProgetto();
		
		model.moveListaRicercaProgettoToListaRicercaProgettoCronoprogrammi();
		
		return "ricercaProgettoCronop";
	}
	
	
	
	public String ricercaCronop() throws Exception {
		
		RicercaDeiCronoprogrammiCollegatiAlProvvedimento req = model.creaRequestRicercaCronoprogramma();
		
		req.setNumeroAttoAmm(model.getStep1Model().getProvvedimento().getNumeroProvvedimento());
		req.setAnnoAttoAmm(model.getStep1Model().getProvvedimento().getAnnoProvvedimento());
		req.setIdTipoProvvedimento(model.getStep1Model().getProvvedimento().getIdTipoProvvedimento());
		req.setIdStrutturaAmministrativoContabile(model.getStep1Model().getProvvedimento().getUidStrutturaAmm());

		RicercaDeiCronoprogrammiCollegatiAlProvvedimentoResponse res = progettoService.ricercaDeiCronoprogrammiCollegatiAlProvvedimento(req);
		
		model.setListaRicercaCronop(res.getCronoprogrammi());
		
		return "ricercaCronop";
	}
	
	
	
}
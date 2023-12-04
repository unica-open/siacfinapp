/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciImpegnoStep1Model;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.EsistenzaProgetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.EsistenzaProgettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaAccertamentoStep1Action extends ActionKeyAggiornaAccertamento
{
	private String numeroAccertamento;
	private String annoAccertamento;
	private String arrivoDaInserimento;
	private Boolean flagValido;
	private Boolean flagSoggettoValido;
	
	private Boolean abilitaModificaImportoUtilizzabile;
	
	private static final long serialVersionUID = 1L;
	
	private static final String SI = "Si";
	private static final String NO = "No";
	
	
	public AggiornaAccertamentoStep1Action () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.ACCERTAMENTO;
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
		this.model.setTitolo("Aggiorna Accertamento - Dati Accertamento");
		
		try{	
			
			//tipo lista provvedimenti
			if(model.getListaTipiProvvedimenti()==null || model.getListaTipiProvvedimenti().size()==0){
				caricaTipiProvvedimenti();
			}
			
			//carica stati operativi atti:
			caricaStatiOperativiAtti();
			
			//carica liste per ricerca soggetto
			caricaListePerRicercaSoggetto();
			
			//carica liste gestisci impegno step 1
			caricaListeGestisciImpegnoStep1();
			
			//scelta da riaccertamento:
			model.getStep1Model().setDaRiaccertamento(buildListaSiNo());
			//SIAC-6997
			model.getStep1Model().setDaReanno(buildListaSiNo());
			
			//scelta attiva gsa:
			model.getStep1Model().setListflagAttivaGsa(buildListaSiNo());
			
			
		}catch(Exception e){
			log.debug("prepara", e.getMessage());
		}	
		
		//link consulta modifiche provvedimento visibile:
		setVisualizzaLinkConsultaModificheProvvedimento(true);
		
		//settiamo le abilitazioni:
		setAbilitazioni();
	
	}
	
	/**
	 * gestione della abilitazioni
	 */
	private void setAbilitazioni(){
		if(model.getStep1Model().getAnnoImpegno()!=null){
			setFlagValido(false);
			setFlagSoggettoValido(false);
			setAbilitaModificaImporto(true);
			
			setAbilitaModificaImportoUtilizzabile(false);
			
			boolean bilPrecInPredisposizioneConsuntivo = isBilancioPrecedenteInPredisposizioneConsuntivo();
			
			//stato D o ND
			if(!model.getAccertamentoInAggiornamento().getStatoOperativoMovimentoGestioneEntrata().equals("P")){
				setFlagValido(true);
				setFlagSoggettoValido(true);
				//nel caso in cui fossimo in predisposizione consuntivo, rimettiamo flagvalido a false
				//in modo che l'importo ritorni modificabile:
				if(bilPrecInPredisposizioneConsuntivo && isResiduo()){
					setAbilitaModificaImporto(true);
				} else {
					setAbilitaModificaImporto(false);
				}
				//
				
			}
			//stato P
			if(model.getAccertamentoInAggiornamento().getElencoSubAccertamenti()!=null || (model.getListaSubaccertamenti()!=null && model.getListaSubaccertamenti().size()>0)){
				setFlagSoggettoValido(true);
			}
			
			//JIRA SIAC-2813 INDIPENDENTEMENTE DALLO STATO, la modifica dell'importo UTILIZZABILE DIPENDE SOLO DA:
			if(bilPrecInPredisposizioneConsuntivo && isResiduo()){
				setAbilitaModificaImportoUtilizzabile(true);
			} else {
				setAbilitaModificaImportoUtilizzabile(false);
			}
			//
			
			//JIRA  SIAC-3506 in caso di residuo con presenza di modifiche di importo valide, importo non modificabile:
			List<ModificaMovimentoGestioneEntrata> modifiche = model.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata();
			if(presenteModificaDiImportoValidaEntrata(modifiche) && isResiduo()){
				setAbilitaModificaImporto(false);
			}
			//
			
			// jira-1582
			// se e' in stato N e ci sono dei sub allora posso modificare il soggetto
			if(model.getAccertamentoInAggiornamento().getStatoOperativoMovimentoGestioneEntrata().equals(CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE)){
				if(!presenteAlmenoUnMovValido(model.getAccertamentoInAggiornamento().getElencoSubAccertamenti(), OggettoDaPopolareEnum.SUBACCERTAMENTO.toString())){
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
		
		sessionHandler.setParametro(FinSessionParameter.CLASSE_ACCERTAMENTO_ORIGINAL, null);
		sessionHandler.setParametro(FinSessionParameter.SOGGETTO_ACCERTAMENTO_ORIGINAL, null);
		
		//ripopoliamo i dati provv dal model support:
		ripopolaProvvedimentoDaSupport();
		
		//Anno capitolo
		if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
			model.getStep1Model().getCapitolo().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
			model.getCapitoloRicerca().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
		}
		
		//Anno accertamento
		if(getAnnoAccertamento()!=null){
			model.getStep1Model().setAnnoImpegno(Integer.valueOf(getAnnoAccertamento()));
			model.getStep1Model().setNumeroImpegno(Integer.valueOf(getNumeroAccertamento()));
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
		teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.ACCERTAMENTO.toString());
		
		if(forceReload){
			sessionHandler.cleanSafelyExcluding(FinSessionParameter.ACCERTAMENTO_CERCATO);
			caricaDatiAccertamento(false);
			//SIAC-8065 se trovo errori dal caricaDatiAccertamento torno indietro e li mostro
			if(model.hasErrori()) {
				return INPUT;
			}
			
			sessionHandler.setParametro(FinSessionParameter.ACCERTAMENTO_CERCATO, getAccertamentoToUpdate());
			
			// Jira - 1299 altrimenti carica ad ogni giro tutti i dati da bilancio
			if (caricaListeBil(WebAppConstants.CAP_EG)) {
				return INPUT;
			}
			
			if(model.getAccertamentoInAggiornamento().getProgetto()!=null && model.getAccertamentoInAggiornamento().getProgetto().getCodice()!=null){
				popolaProgetto(model.getAccertamentoInAggiornamento().getProgetto());
			}
		} 

		//SIAC-7406 commentata riga dopo l'aggiunta del campo reanno SIAC-6997
//		if(((MovimentoGestione) sessionHandler.getParametro(FinSessionParameter.ACCERTAMENTO_CERCATO)).isFlagDaRiaccertamento()){
//			model.getStep1Model().setRiaccertato(SI);
//		} else {
//			model.getStep1Model().setRiaccertato(NO);
//		}
		
		//labels aggiorna:
		caricaLabelsAggiorna(1);
		
		//Metodo di salvataggio del model in model di cache
		creaMovGestModelCache();
		
		//controlliamo lo stato provvedimento:
		controllaStatoProvvedimento();
		
		model.setStep1ModelSubimpegno(new GestisciImpegnoStep1Model());
		model.setStep1ModelSubimpegnoCache(new GestisciImpegnoStep1Model());
		
		//disabilito il caricamento degli alberi inutili per questo scenario (in AjaxAction.java):
		teSupport.setRicaricaAlberoPianoDeiConti(false);
		teSupport.setRicaricaStrutturaAmministrativa(false);
		teSupport.setRicaricaSiopeSpesa(false);
		//////////////////////////////////////////////////////////////////////////////////////////
		

		// setto l'anno capitolo per la ricerca guidata del vincolo
		
		//SIAC-6997
		recuperaDescrizioneStrutturaCompetente();
		
		setAbilitazioni();
		
		//SIAC-5889
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
		return "inserisceAccertamento";
	}
	
	// siac-3224
	public String consultaModificheProvvedimento() throws Exception{
		//info per debug:
		setMethodName("consultaModificheProvvedimento");
		//leggo i dati necessari:
		Accertamento accertamento = model.getAccertamentoInAggiornamento();
		leggiStoricoProvvedimentoByMovimento(accertamento);
		return "consultaModificheProvvedimento";
	}
	
	// SIAC-6997
	public String consultaModificheStrutturaCompetente() throws Exception{
		//info per debug:
		setMethodName("consultaModificheStrutturaCompetente");
		//leggo i dati necessari:
		Accertamento accertamento = model.getAccertamentoInAggiornamento();
		leggiStoricoStrutturaCompetenteByMovimento(accertamento);
		return "consultaModificheStrutturaCompetente";
	}
	
	public String siSalva() throws Exception{
		setMethodName("siSalva");
		setShowPopUpMovColl(Boolean.FALSE);
		setFromModaleConfermaModificaProvvedimento(true);
		setFromPopup(true);
		return salva();
	}
	
	
	public String salva() throws Exception {
		setMethodName("salva");
		
		
		// controlli tabellina 4.6
		List<Errore> erroriAbilitazione = abilitazioneCampiTE(oggettoDaPopolare);
		if(null!=erroriAbilitazione && erroriAbilitazione.size()>0){
			addErrori(erroriAbilitazione);
			return INPUT;
		}
		
		
		//controlli provvedimento rispetto all'abilitazione a gestire l'accertamento decentrato:
		String controlloProvv = provvedimentoConsentito();
		if(controlloProvv!=null){
			return controlloProvv;
		}
		//SIAC-6997 - CONTROLLO CHE SIA UNA SAC appartenente al ruolo loggato
	    if(model.getStep1Model().getStrutturaSelezionataCompetente() == null || model.getStep1Model().getStrutturaSelezionataCompetente().equals("")){
		    addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Struttura Competente "));
		    return INPUT;
	    }

		isSACStrutturaCompetente(null);
		//recuperaDescrizioneStrutturaCompetente();
		
		//ricontrolliamo il siope:
		codiceSiopeChangedInternal(teSupport.getSiopeSpesaCod());
		//
		
		//SIAC-8511
	    controllaLunghezzaMassimaDescrizioneMovimento(model.getStep1Model().getOggettoImpegno());
		
		//SIAC-6997
	    if(hasActionErrors()){
	    	return INPUT;
	    }
		
		setAccertamentoToUpdate((Accertamento)sessionHandler.getParametro(FinSessionParameter.ACCERTAMENTO_CERCATO));
		// SALVATAGGIO STEP1
		String risultato = salvaStep1(true);
		
		if(risultato.equalsIgnoreCase("salva")){
			addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " 
		                   + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());	
		}
		
		//controllo registrazione andata a buon fine:
		controlloRegistrazioneValidazionePrimaNota();
		//
		
		if(risultato.equalsIgnoreCase("salva")){
			if(isFromModaleConfermaModificaProvvedimento()){
				setSuccessStep1(true);
			}
		}
		
		setFlagValido(false);
		setFlagSoggettoValido(false);
		//stato D o ND
		if(!model.getAccertamentoInAggiornamento().getStatoOperativoMovimentoGestioneEntrata().equals("P")){
			setFlagValido(true);
			setFlagSoggettoValido(true);
		}
		//stato P
		if(model.getAccertamentoInAggiornamento().getElencoSubAccertamenti()!=null || (model.getListaSubaccertamenti()!=null && model.getListaSubaccertamenti().size()>0)){
			setFlagSoggettoValido(true);
		}
		
		//SIAC-7406 commentata riga dopo l'aggiunta del campo reanno SIAC-6997		
//		if(getAccertamentoToUpdate().isFlagDaRiaccertamento()){
//			model.getStep1Model().setRiaccertato(SI);
//		} else {
//			model.getStep1Model().setRiaccertato(NO);
//		}
		
		// Controllo SAC con STRUTTURA AMMINISTRATIVA
		// se nel provv c'e' la struttura allora devo verificare che sia una di quelle ammesse
		// Jira 1648 : devo togliere il controllo sul null, perchè 
		// altrimenti non esegue il controllo nel caso in cui modifico il provevdimento, scegliendo uno senza sac
		// Quindi sposto il controllo sul null in controlloSACStrutturaAmmUtenteDecentrato (lì verifico che se il dec ha strutture
		// e se è uidStrutturaAmm è null lancio il warning) 
		if(!controlloSACStrutturaAmmUtenteDecentrato(sessionHandler.getAzione().getNome(), model.getStep1Model().getProvvedimento().getUidStrutturaAmm())){
			addPersistentActionWarning(ErroreFin.PROVVEDIMENTO_DECENTRATO_NON_VALIDO.getErrore().getCodice()+" - "+ErroreFin.PROVVEDIMENTO_DECENTRATO_NON_VALIDO.getErrore().getDescrizione());
			return INPUT;
		}
		
		//SIAC-6997
		recuperaDescrizioneStrutturaCompetente();
		
		//SIAC-6000
		if(risultato.equalsIgnoreCase("salva") 
				&& model.isEnteAbilitatoGestionePrimaNotaDaFinanziaria()
				&& model.isSaltaInserimentoPrimaNota() 
				&& model.getUidDaCompletare() > 0){
			return GO_TO_GEN;
		}
		//
		//SIAC-6997
		forceReload = true;

		return risultato;
		
	}
	
	//PROSEGUI
	protected String prosegui(boolean proseguiSalva) {
		setMethodName("prosegui");	
		List<Errore> listaErrori = new ArrayList<Errore>();	
		String nomeParametroOmesso = new String();
		setShowPopUpMovColl(false);
		setProseguiStep1(true);		
		
	    //inizio SIAC-6997 CONTROLLO CHE SIA UNA SAC appartenente al ruolo loggato
	    /*if(model.getStep1Model().getStrutturaSelezionataCompetente() == null || model.getStep1Model().getStrutturaSelezionataCompetente().equals("")){
	    	listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Struttura Competente "));
		    return INPUT;
	    }*/

	    isSACStrutturaCompetente(listaErrori);
	    //recuperaDescrizioneStrutturaCompetente();
	    //fine SIAC-6997
		
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
			    if(oggettoDaPopolareImpegno()){
			    	  if(model.getStep1Model().getTipoImpegno() == null || "".equalsIgnoreCase(model.getStep1Model().getTipoImpegno())){
			   	 	   listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Impegno "));
			   	    }
			    }
			    if(model.getStep1Model().getImportoFormattato() == null || 
			 	   model.getStep1Model().getImportoFormattato().equals("0") ||
			 	   model.getStep1Model().getImportoFormattato().equals("")){
			 	   listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo "+model.getLabels().get(LABEL_OGGETTO_GENERICO)));
			 	}else{
			 	   	try{
			 	   		// converto
			 	   		model.getStep1Model().setImportoImpegno(convertiImportoToBigDecimal(model.getStep1Model().getImportoFormattato()));
					    if (model.getStep1Model().getImportoImpegno().compareTo(BigDecimal.ZERO) <= 0) {
						      listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo "+model.getLabels().get(LABEL_OGGETTO_GENERICO_PADRE)));
				    		}
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
			    if (NO.equalsIgnoreCase(model.getStep1Model().getRiaccertato())  && NO.equalsIgnoreCase(model.getStep1Model().getReanno())) {
			    	model.getStep1Model().setAnnoImpRiacc(null);
			    	model.getStep1Model().setNumImpRiacc(null);
			    } else {
			    	
			    	if(SI.equalsIgnoreCase(model.getStep1Model().getRiaccertato())) {
			    		nomeParametroOmesso = "da riaccertamento";
			    	}else if(SI.equalsIgnoreCase(model.getStep1Model().getReanno())){
			    		nomeParametroOmesso = "da reimputazione in corso d'anno";
			    	}
			    	
			    	if (model.getStep1Model().getAnnoImpRiacc() != null && model.getStep1Model().getNumImpRiacc() != null && model.getStep1Model().getAnnoImpegno()!=null ) {
				    	if (model.getStep1Model().getAnnoImpRiacc().compareTo(model.getStep1Model().getAnnoImpegno())>=0) {
				    		//correzione messaggio
				    		listaErrori.add(ErroreFin.INCONGRUENZA_TRA_I_PARAMETRI_RIACCERTAMENTO.getErrore(model.getLabels().get(LABEL_OGGETTO_GENERICO_PADRE),model.getLabels().get(LABEL_OGGETTO_GENERICO_PADRE)));
				    	} else {
				    		RicercaAccertamentoPerChiaveOttimizzato rap = new RicercaAccertamentoPerChiaveOttimizzato();
				    		rap.setEnte(sessionHandler.getEnte());
				    		rap.setRichiedente(sessionHandler.getRichiedente());
				    		RicercaAccertamentoK k = new RicercaAccertamentoK();
				    		// Jira - 630
				    		k.setAnnoEsercizio(model.getStep1Model().getAnnoImpOrigine());
				    		k.setAnnoAccertamento(model.getStep1Model().getAnnoImpRiacc());
				    		k.setNumeroAccertamento(new BigDecimal(model.getStep1Model().getNumImpRiacc()));
				    		rap.setpRicercaAccertamentoK(k);
				    		RicercaAccertamentoPerChiaveOttimizzatoResponse respRk = movimentoGestioneFinService.ricercaAccertamentoPerChiaveOttimizzato(rap);
				    		if(respRk!=null && respRk.getAccertamento()!=null){
				    			if(respRk.getAccertamento().getAnnoOriginePlur()!=0 &&
				    					respRk.getAccertamento().getNumeroOriginePlur() !=null){
				    								    				
					    				if(model.getStep1Model().getAnnoImpOrigine() == null && model.getStep1Model().getNumImpOrigine() == null){
					    					model.getStep1Model().setAnnoImpOrigine(respRk.getAccertamento().getAnnoOriginePlur());
					    				}
					    				if(null!=respRk.getAccertamento().getNumeroOriginePlur()){
					    					model.getStep1Model().setNumImpOrigine(respRk.getAccertamento().getNumeroOriginePlur().intValue());	
					    				}
					    				
					    			
				    				if(respRk.getAccertamento().getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase("A")){
				    					listaErrori.add(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA_FIN.getErrore("Accertamento"," annullato"));
				    				}
					    		
				    			}	
				    			
			    				if(respRk.getAccertamento().getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase("A")){
			    					listaErrori.add(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA_FIN.getErrore("Accertamento"," annullato"));
			    				}
				    		}
				    		else{
				    			if(oggettoDaPopolareImpegno()){
					    			listaErrori.add(ErroreFin.MOVIMENTO_NON_TROVATO.getErrore("impegno riaccertato"));
					    			
					    			//cancello i valori nel model del movimento inesistente
					    			model.getStep1Model().setAnnoImpRiacc(null);
							    	model.getStep1Model().setNumImpRiacc(null);
							    	model.getStep1Model().setRiaccertato(WebAppConstants.No);
							    	//SIAC-6997
							    	model.getStep1Model().setReanno(WebAppConstants.No);
				    			
				    			}else{
				    				if (isFromPopup() || proseguiSalva) {
						    			setShowPopUpMovColl(false);
						    		}else {
					    				setShowPopUpMovColl(true);
						    			addPersistentActionWarning(ErroreCore.AGGIORNAMENTO_CON_CONFERMA_WARN.getErrore().getCodice()+" : "+ErroreCore.AGGIORNAMENTO_CON_CONFERMA_WARN.getErrore("Accertamento riaccertato non","presente","l'aggiornamento").getDescrizione());						    			
						    		}
				    			}
				    		}
				    	}
				    } else {
				    	if (model.getStep1Model().getAnnoImpRiacc() != null && model.getStep1Model().getNumImpRiacc() == null) {
				    		if(oggettoDaPopolareImpegno()){
				    			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Impegno " + nomeParametroOmesso));
				    		}else{
				    			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Accertamento "  + nomeParametroOmesso));
				    		}
				    		
				    	}  
				    	if (model.getStep1Model().getAnnoImpRiacc() == null && model.getStep1Model().getNumImpRiacc() != null) {
				    		if(oggettoDaPopolareImpegno()){
				    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Impegno " + nomeParametroOmesso));
				    	}else{
				    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Accertamento " + nomeParametroOmesso));

				    	}
				    	}
				    	if (model.getStep1Model().getAnnoImpRiacc() == null && model.getStep1Model().getNumImpRiacc() == null) {
				    		if(oggettoDaPopolareImpegno()){
				    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Impegno " + nomeParametroOmesso));
				    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Impegno " + nomeParametroOmesso));
				    	}else{
				    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Accertamento " + nomeParametroOmesso));
				    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Accertamento " + nomeParametroOmesso));
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
			    		// Jira - 630
			    		k.setAnnoEsercizio(model.getStep1Model().getAnnoImpOrigine());
			    		k.setAnnoImpegno(model.getStep1Model().getAnnoImpOrigine());
			    		k.setNumeroImpegno(new BigDecimal(model.getStep1Model().getNumImpOrigine()));
			    		rip.setpRicercaImpegnoK(k);
			    		
			    		//MARZO 2016: ottimizzazioni carica sub:
			    		rip.setCaricaSub(false);
			    		//
			    		
			    		RicercaImpegnoPerChiaveOttimizzatoResponse respRk = movimentoGestioneFinService.ricercaImpegnoPerChiaveOttimizzato(rip);
			    		
			    		if(respRk==null || respRk.getImpegno()==null){
			    			listaErrori.add(ErroreFin.MOVIMENTO_NON_TROVATO.getErrore("impegno origine"));
			    		}	
					} else {
						RicercaAccertamentoPerChiaveOttimizzato rap = new RicercaAccertamentoPerChiaveOttimizzato();
						rap.setEnte(sessionHandler.getEnte());
			    		rap.setRichiedente(sessionHandler.getRichiedente());
			    		RicercaAccertamentoK k = new RicercaAccertamentoK();
			    		k.setAnnoEsercizio(model.getStep1Model().getAnnoImpOrigine());
			    		k.setAnnoAccertamento(model.getStep1Model().getAnnoImpOrigine());
			    		k.setNumeroAccertamento(new BigDecimal(model.getStep1Model().getNumImpOrigine()));
			    		rap.setpRicercaAccertamentoK(k);
			    		
			    		RicercaAccertamentoPerChiaveOttimizzatoResponse respRk = movimentoGestioneFinService.ricercaAccertamentoPerChiaveOttimizzato(rap);
			    		
			    		if(respRk==null || respRk.getAccertamento()==null){
			    			
			    			if (isFromPopup() || proseguiSalva) {
				    			setShowPopUpMovColl(false);
				    		}else {
			    				setShowPopUpMovColl(true);
				    			addPersistentActionWarning(ErroreCore.AGGIORNAMENTO_CON_CONFERMA_WARN.getErrore().getCodice()+" : "+ErroreCore.AGGIORNAMENTO_CON_CONFERMA_WARN.getErrore("Accertamento origine non","presente","l'aggiornamento").getDescrizione());						    			
				    		}
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
			  		
		  		//verifico l'esistenza del progetto
			    if(StringUtils.isNotEmpty(model.getStep1Model().getProgetto())){
			    	EsistenzaProgetto ep = new EsistenzaProgetto();
			    	ep.setCodiceProgetto(model.getStep1Model().getProgetto());
			    	ep.setRichiedente(sessionHandler.getRichiedente());
			    	ep.setBilancio(sessionHandler.getBilancio());
			    	ep.setCodiceTipoProgetto(TipoProgetto.GESTIONE.getCodice());
			    	
			    	EsistenzaProgettoResponse esistenzaResp = genericService.cercaProgetto(ep);
			    	
			    	if(esistenzaResp.isFallimento()){
			    		if(!esistenzaResp.isEsisteProgetto()){
			    			listaErrori.add(ErroreCore.ENTITA_INESISTENTE.getErrore("codice progetto", model.getStep1Model().getProgetto()));
			    		}
			    	}
			    }
				    
			  //controllo disponibilita capitolo
			  if(controlloImporti()) {
			    	if(oggettoDaPopolareImpegno()){
			    		listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE_IMPEGNO.getErrore("impegno", "AGGIORNAMENTO"));
			    	}else{
			    		addPersistentActionWarning(ErroreFin.SUPERAMENTO_DISPONIBILITA.getCodice()+": "+ErroreFin.SUPERAMENTO_DISPONIBILITA.getErrore("").getDescrizione());
			    		model.setProseguiConWarning(Boolean.TRUE);
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
				
				//controllo modifica provvedimento
				// CR SIAC-3224, in caso di aggiornamento il provvedimento si puo modificare
				if (isFromModaleConfermaProseguiModificaProvvedimento()) {
					setShowModaleConfermaProseguiModificaProvvedimento(false);
	    		}else{
					if(verificaModificaProvvedimento(false)){
						setProseguiStep1(false);
						return INPUT;
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

				// task-51 - aggiunto controllo su esistenza classe, altrimenti non cambiava il soggetto (nel metodo lo ricrea senza id)
				//in presenza della classe, pulisco totalmente il soggetto
				if(model.getStep1Model().getSoggetto().getClasse() != null && !"".equalsIgnoreCase(model.getStep1Model().getSoggetto().getClasse()) ){
				    controlloSoggettoSelezionatoEClasse();
				}
					
			    //Inserisco la descrizione all'interno della classe del soggetto  vedi Jira e issue di laura
				inserisciDescClasseSoggettoAggiorna();
				
			    if(listaErrori.isEmpty()) {
			    	setFromPopup(false);
			    	
					if (isShowPopUpMovColl()) {
						return INPUT;
					}
			    	
			          return PROSEGUI;
			    } else {
			 	   addErrori(listaErrori);
			 	   
			 	   setFlagValido(false);
			 	   setFlagSoggettoValido(false);
			 	   //stato D o ND
			 	   if(!model.getAccertamentoInAggiornamento().getStatoOperativoMovimentoGestioneEntrata().equals("P")){
			 		   setFlagValido(true);
			 		   setFlagSoggettoValido(true);
			 	   }
			 	   //stato P
			 	   if(model.getAccertamentoInAggiornamento().getElencoSubAccertamenti()!=null || (model.getListaSubaccertamenti()!=null && model.getListaSubaccertamenti().size()>0)){
			 		   setFlagSoggettoValido(true);
			 	   }
				   
				   return INPUT;
			    }
			}	
	
	public String siProsegui() throws Exception{
		setMethodName("siProsegui");
		
		setFromPopup(true);
		setFromModaleConfermaProseguiModificaProvvedimento(true);
		return prosegui();
	}
	
	
	
	public String indietro(){
	
		return "gotoInserisciImpegno";
	}

	public String annulla(){
		setMethodName("annulla");

		model.setStep1Model(model.getStep1ModelCache());
		creaMovGestModelCache();

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

	public Boolean getAbilitaModificaImportoUtilizzabile() {
		return abilitaModificaImportoUtilizzabile;
	}

	public void setAbilitaModificaImportoUtilizzabile(
			Boolean abilitaModificaImportoUtilizzabile) {
		this.abilitaModificaImportoUtilizzabile = abilitaModificaImportoUtilizzabile;
	}

	
	public boolean disableFlagPrevistaFattura() {
		return !isAbilitatoAggiornamentoGenerico() ||
				disableFlagPrevistoCorrispettivo() &&  WebAppConstants.Si.equals(model.getStep1Model().getFlagCorrispettivo());
	}
	
	public boolean disableFlagPrevistoCorrispettivo() {
		return !isAbilitatoAggiornamentoGenerico() ||
			   ! StatoOperativoMovimentoGestione.PROVVISORIO.name().equals(model.getStep1Model().getDescrizioneStatoOperativoMovimento());
	}

}
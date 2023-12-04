/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.liquidazione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.VerificaBloccoRORHelper;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.errore.ErroreFin;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciLiquidazioneStep1Action extends WizardInserisciLiquidazioneAction {

	private static final long serialVersionUID = 1L;
	private static final String PROSEGUI = "prosegui";
	private static final String ANNULLA = "annulla";	
	private static final String CAP_UG = "CAP-UG";
	
	private CapitoloImpegnoModel capitoloLiquidazioneModel; 
	
	public InserisciLiquidazioneStep1Action() {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.LIQUIDAZIONE;
	}

	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		if(this.model.getTitolo()!=null && this.model.getTitolo().equals("Aggiornamento Liquidazione")){
			//ripulisco i campi dello step 1 se vengo dallo step3
			this.model.setNumeroImpegno(null);
			this.model.setAnnoImpegno(null);
			this.model.setNumeroSub(null);
			this.model.setDescrizioneImpegnoPopup(null);
			this.model.setImpegnoPopup(null);
			this.model.setImpegno(null);
		}
		//setto il titolo:
		this.model.setTitolo("Inserimento Liquidazione - Ricerca Impegno");
		this.model.setStep("INSLIQSTEP1");
		//SIAC-5333
		this.model.setRichiediConfermaUtente(false);
	}

	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {	
		//pulisco il flag nel caso arrivassi dal valida ora direttamente qui:
		model.setSalvataConValidaOra(false);
		//controlli di abilitazioni preliminari:
		controlliAbilitazioniIniziali();
		return "success";
	}
	
	/**
	 * esegue la validazioni iniziali per il caso d'uso
	 * @return
	 */
	private boolean controlliAbilitazioniIniziali(){
		
		boolean tuttoOk = true;
		
		// precondizioni di ingresso liquidazione		
		if(isAbilitatoInsLiq()){
			// master
			setAzioniToCheck(AzioneConsentitaEnum.OP_SPE_insLiq.getNomeAzione());
			if(!checkAzioni()){			
			   addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));	
			   tuttoOk = false;
			}
		}else{
			addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
			tuttoOk = false;
		}
		
		//fase bilancio abilitata:
		if(!isFaseBilancioAbilitata()){
			addErrore(ErroreFin.OPERAZIONE_INCOMPATIBILE_PER_BILANCIO.getErrore("inserisci liquidazione", sessionHandler.getFaseBilancio()));
			model.setRicercaImpegnoStep1Abilitata(false);
			tuttoOk = false;
		}	
		
		return tuttoOk;
	}

	/**
	 * Gestione tasto prosegui
	 * @return
	 */
	public String prosegui() {
		//chiamo il metodo core di validazione dello step:
		return validateStep();
	}

	/**
	 * funzione di annulla valori nei campi
	 * @return
	 */
	public String annulla() {
		//richiamo il metodo che pulisce il model:
		pulisciCampiImpegno();
		return ANNULLA;
	}
	
	/**
	 * Viene pulito il moel relativo alla modale di ricerca dell'impegno
	 * @return
	 */
	public String azzeraModaleImpegno(){
		model.setInPopup(false);	
		//metodo che pulisce le variabili nel model:
		pulisciListeeSchedaPopup();
		return "refreshPopupModalImpegno";
	}

	/**
	 * Effettua i controlli per validare i dati immessi prima
	 * di consentire il passaggio allo step successivo
	 * @return
	 */
	private String validateStep() {
		model.setInPopup(false);
		
		//RICHIAMO I CONTROLLI INIZIALI PER SICUREZZA:
		boolean esitoIniziali = controlliAbilitazioniIniziali();
		if (!esitoIniziali) {
			pulisciCampiImpegno();
			return INPUT;
		}
		//END CONTROLLI INIZIALI
		
		//superato il primo check faccio altri controlli 
		
		boolean compilatiAnnoENumero = controlloAnnoENumeroCompilati();
		if (!compilatiAnnoENumero) {
			//ANNO E NUMERO non compilati
			return INPUT;
		}
		
		//impegno valorizzato e numero cambiato per capire se ricaricare o meno l'impegno da servizio.
		boolean impegnoValorizzatoENumeroCambiato = impegnoValorizzatoENumeroCambiato();
		
		boolean tuttoOk = true;
		
		if(model.isImpegnoAppenaSelezionatoDaCompGuidata() ||  model.getImpegno()==null || impegnoValorizzatoENumeroCambiato){
			//caso in cui serve invocare il servizio di ricerca impegno
			// per effettuo i controlli necessari per assicurarmi che l'utente abbia indicato un impegno accettabile
			tuttoOk = controlliRicaricandoImpegno();
		}else{
			//caso in cui non serve invocare nuovamente il servizio di ricerca impegno
			//effettuo i controlli necessari per assicurarmi che l'utente non abbia modificato i valori in modo incoerente
			tuttoOk = controlliSenzaRicaricareImpegno();
		}

		if(tuttoOk){
			
			//SIAC-6997-bloccoROR
			Impegno param = null;
			if(model.getImpegnoPerServizio() != null){
				param = model.getImpegnoPerServizio();
			}else{
				//SIAC-6997-bloccoROR: ricarico l'impegno per effettuare il controllo
				RicercaImpegnoPerChiaveOttimizzatoResponse respRk = ricercaImpegno(isCompilatoNumeroSubImpegno());
				if (respRk != null && respRk.getImpegno() != null) {
					param = respRk.getImpegno();
				}
			}
			boolean test = VerificaBloccoRORHelper.escludiImpegnoPerBloccoROR(sessionHandler.getAzioniConsentite(), param, sessionHandler.getAnnoBilancio());
			if(test){
				addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impegno/sub impegno residuo non utilizzabile"));
				return INPUT;
			}else if(param.getElencoSubImpegni() != null && !param.getElencoSubImpegni().isEmpty()){
				for(int k = 0; k < param.getElencoSubImpegni().size(); k++){
					test = VerificaBloccoRORHelper.escludiImpegnoPerBloccoROR(sessionHandler.getAzioniConsentite(), param.getElencoSubImpegni().get(k), sessionHandler.getAnnoBilancio());
					if(test)
						break;
				}
				if(test){
					addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impegno/sub impegno residuo non utilizzabile"));
					return INPUT;
				}
			}
			//END SIAC-6997-bloccoROR
			
			//OK NESSUN ERRORE, possiamo andare alla pagina successiva
			
			//fix per anomalia: SIAC-2257 (veniva perso il teSupport)
			preparaCampiInserisciLiquidazione();
			//
			
			impostaObbligatoriConUnSoloElementoInLista(oggettoDaPopolare);
			
			model.setHasImpegnoSelezionatoPopup(true);
			model.setHasImpegnoSelezionatoXPopup(true);
			return PROSEGUI;
		} else {
			//presenza di errori (gia settati nella action puntualmente dai metodi che hanno effettuato i controlli)
			return INPUT;
		}
	}
	
	/**
	 * Metodo che controlla la validita dell'impegno indicato andando ad effettuare la chiamata al
	 * servizio ricerca impegno per chiave.
	 * @return
	 */
	private boolean controlliRicaricandoImpegno(){
		
		boolean tuttoOk = true;
		
		//FLAG IMPEGNO APPENA SELEZIONATO:
		model.setImpegnoAppenaSelezionatoDaCompGuidata(false);
		
		//CHIAMIAMO IL SERVIZIO DI RICERCA IMPEGNO
		boolean indicatoSub = isCompilatoNumeroSubImpegno();
		//(specificando se e' stato compilato o meno il sub, in tal caso cambia la request da comporre)
		RicercaImpegnoPerChiaveOttimizzatoResponse respRk = ricercaImpegno(indicatoSub);
		
		if (respRk != null && respRk.getImpegno() != null) {
			
			boolean corrispondenzaImpegnoSubImpegno = false;
			Impegno impegno = respRk.getImpegno();
		
			String stato = impegno.getDescrizioneStatoOperativoMovimentoGestioneSpesa();
			
		 	//SIAC-5091 mancata Verifica presenza di Sub Annullato
			if("DEFINITIVO NON LIQUIDABILE".equals(stato) && !indicatoSub) {
				//Dato che sopra ho settato EscludiSubAnnullati nei parametri di richiamo del ricerca impegno
				//devo dedurre che se lo stato e' definitivo non liquidabile e non ho selezionato un sub
				//siamo nel caso di impegno con sub (che possono anche non esserci ancora o essere annullati)
				//e lancio errore:
				addErrore(ErroreFin.IMPEGNO_CON_SUBIMPEGNI.getErrore());
				return false;
			}
			
			//settiamo il dati principali dell'impegno nel model:
			setDatiImpegnoNelModel(impegno);
			
			//determino i sub impegni di riferimento:
			List<SubImpegno> elencoSubImpegni = elencoSubDiRiferimento(indicatoSub, respRk);
			
			if (!isEmpty(elencoSubImpegni)){
				//Esistono sub
				if (!isCompilatoNumeroSubImpegno()) {
					//IN CASO DI IMPEGNO CON DEI SUB DEVE ESSERE SELEZIONATO UN SUBIMPEGNO
					addErrore(ErroreFin.IMPEGNO_CON_SUBIMPEGNI.getErrore());
					return false;
				} else {
					//OK CASO DI IMPEGNO CON DEI SUB, E SUB INDICATO
					
					SubImpegno subCorrispondente = FinUtility.findSubImpegnoByNumero(elencoSubImpegni, model.getNumeroSub());
					if(subCorrispondente!=null){
						
						corrispondenzaImpegnoSubImpegno = true;							
						model.setImpegno(subCorrispondente);
						
						//VERIFICHIAMO CHE IL SUB SIA VALIDATO E CHE LO STATO SIA ACCETTABILE:
						boolean statoEValidato = controlliSuStatoESuValidato(subCorrispondente);
						if (!statoEValidato) {
							return false;
						}
					}

				}
				if (!corrispondenzaImpegnoSubImpegno) {
					addErrore(ErroreFin.MANCATA_CORRISPONDENZA_IMPEGNO_CON_SUBIMPEGNI.getErrore());
					return false;
				}
				
			} else {					

				//NON ESISTONO SUB
				
				if (model.getNumeroSub() != null && !model.getNumeroSub().equals(0)) {
					addErrore(ErroreFin.MANCATA_CORRISPONDENZA_IMPEGNO_CON_SUBIMPEGNI.getErrore());
					return false;
				}

				model.setImpegno(impegno);
				
				//VERIFICHIAMO CHE L'IMPEGNO SIA VALIDATO E CHE LO STATO SIA ACCETTABILE:
				boolean statoEValidato = controlliSuStatoESuValidato(impegno);
				if (!statoEValidato) {
					return false;
				}
				
			}
			
			aggiornaModuliLiquidazione(model.getImpegno());
			
			//Controlliamo se il soggetto e' bloccato:
			if (soggettoBloccato()) {
				addErrore(ErroreFin.SOGGETTO_BLOCCATO.getErrore());
				return false;
			}

		} else {
			//il servizio non ha trovato nulla
			addErrore(ErroreFin.MOVIMENTO_NON_TROVATO.getErrore("impegno"));
			return false;
		}
		
		return tuttoOk;
	}
	
	/**
	 * Metodo per mettere a fattor comune i controlli su stato e validato che erano duplicati
	 * per l'impegno e per il subimpegno
	 * @param stato
	 * @param validato
	 * @return
	 */
	private <I extends Impegno> boolean controlliSuStatoESuValidato(I impegnoOrSubimpegno){
		boolean tuttoOK = true;
		if(impegnoOrSubimpegno!=null){
			//oggetto valorizzato
			List<Errore> listaErrori = new ArrayList<Errore>();
			//leggo lo stato:
			String stato = impegnoOrSubimpegno.getDescrizioneStatoOperativoMovimentoGestioneSpesa();
			//leggo il flag validato:
			boolean validato = impegnoOrSubimpegno.isValidato();
			if (stato != null && !stato.equals("DEFINITIVO") && !stato.equals("DEFINITIVO NON LIQUIDABILE")) {
				//stato non accettabile
				listaErrori.add(ErroreFin.NUMERO_IMPEGNO_NON_VALIDO.getErrore("STATO IMPEGNO = "+ stato));
			}
			if(!validato){
				//non validato do errore
				listaErrori.add(ErroreFin.NUMERO_IMPEGNO_NON_VALIDO.getErrore("impegno NON VALIDATO"));
			}
			if (!listaErrori.isEmpty()) {
				//esito negativo
				addErrori(listaErrori);
				return false;
			}
		}
		return tuttoOK;
	}
	
	/**
	 * Metodo che controlla che l'utente non abbia digitato dati incoerenti quando non e' strettamente
	 * necessario invocare nuovamente il servizio di ricerca impegno per chiave
	 * al fine di validare l'impegno indicato.
	 * @return
	 */
	private boolean controlliSenzaRicaricareImpegno(){
		
		boolean tuttoOk = true;
		
		if (!isEmpty(model.getImpegno().getElencoSubImpegni())){
			
			//ci sono sub il elenco
			
			if(isNullOrZero(model.getNumeroSub())){
				//non e' compilato il numero sub
				addErrore(ErroreFin.IMPEGNO_CON_SUBIMPEGNI.getErrore());
				return false;
			} else {
				//numero sub compilato, deve trovare riscontro nell'elenco
				boolean corrispondenzaImpegnoSubImpegno = false;
				
				SubImpegno subCorrispondente = FinUtility.findSubImpegnoByNumero(model.getImpegno().getElencoSubImpegni(), model.getNumeroSub());
				if(subCorrispondente!=null){
					corrispondenzaImpegnoSubImpegno = true;								
					model.setImpegno(subCorrispondente);
				}
				
				if (!corrispondenzaImpegnoSubImpegno) {
					addErrore(ErroreFin.MANCATA_CORRISPONDENZA_IMPEGNO_CON_SUBIMPEGNI.getErrore());
					return false;
				}
					
			}
			
		} else {
			//non ci sono sub nell'elenco
			if (model.getNumeroSub() != null && !model.getNumeroSub().equals(0)) {
				//se e' indicato un sub lancio errore
				addErrore(ErroreFin.MANCATA_CORRISPONDENZA_IMPEGNO_CON_SUBIMPEGNI.getErrore());
				return false;
			}
		}				
		
		return tuttoOk;
		
	}
	

	
	/**
	 * Semplice metodo che controlla che siano stati compilati anno e numero
	 * @return
	 */
	private boolean controlloAnnoENumeroCompilati(){
		boolean tuttoOk = true;
		List<Errore> listaErrori = new ArrayList<Errore>();	
		//ANNO
		if (this.model.getAnnoImpegno() == null) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Impegno :Anno "));
		}
		//NUMERO
		if (this.model.getNumeroImpegno() == null) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Impegno :Numero "));
		}
		//AGGIUNGO GLI EVENTUALI ERRORI:
		if (!listaErrori.isEmpty()) {
			addErrori(listaErrori);
			tuttoOk = false;
		}
		return tuttoOk;
	}
	
	/**
	 * Ritorna true se model.getNumeroImpegno() diverso da null ed il suo numero
	 * e' diverso da model.getNumeroImpegno()
	 * @return
	 */
	private boolean impegnoValorizzatoENumeroCambiato(){
		boolean impegnoValorizzatoENumeroCambiato = false;
		if(model.getImpegno()!=null && this.model.getNumeroImpegno()!=null){
			//impegno diverso da null e numero impegno diverso da null
			BigDecimal numeroBD = new BigDecimal(this.model.getNumeroImpegno());
			impegnoValorizzatoENumeroCambiato = !(numeroBD).equals(model.getImpegno().getNumeroBigDecimal());
		}
		return impegnoValorizzatoENumeroCambiato;
	}
	
	/**
	 * Per verificare che il soggetto non sia in stato bloccato
	 * @return
	 */
	private boolean soggettoBloccato(){
		boolean soggettoBloccato = false;
		if(model.getImpegno() != null && model.getImpegno().getSoggetto() != null && model.getImpegno().getSoggetto().getStato() != null){
			//soggetto valorizzato con stato diverso da null
			if(!(model.getImpegno().getSoggetto().getStato()==StatoEntita.VALIDO) && !(model.getImpegno().getSoggetto().getStato() == StatoEntita.IN_LAVORAZIONE)){
				//se non e' ne valido ne in lavorazione
				soggettoBloccato = true;
			}
		}
		return soggettoBloccato;
	}
	
	/**
	 * A partire dall'impegno ottenuto dalla ricerca popola nel model:
	 * 
	 * Provvedimento
	 * Soggetto
	 * 
	 * @param impegno
	 */
	private void aggiornaModuliLiquidazione(Impegno impegno) {
		
		//PROVVEDIMENTO:
		AttoAmministrativo provvedimento = impegno.getAttoAmministrativo();
		if (provvedimento != null && provvedimento.getUid() != 0) {
			//ISTANZIO L'OGGETTO
			model.setProvvedimento(new ProvvedimentoImpegnoModel());
			//INVOCO IL METODO DI POPOLAMENTO:
			impostaProvvNelModel(provvedimento, model.getProvvedimento());
			//MARCO CHE C'E' UN PROVVEDIMENTO SELEZIONATO:
			model.setProvvedimentoSelezionato(true);
			
			//SIAC-5492 uso: FIX PER SIAC-3943
			//che sono lo stesso problema
			if(model.getProvvedimento()!=null &&
					NESSUNA_STRUTTURA_AMMINISTRATIVA.equals(model.getProvvedimento().getCodiceStrutturaAmministrativaPadre()) &&
					!StringUtils.isEmpty(model.getProvvedimento().getCodiceStrutturaAmministrativa())){
				model.getProvvedimento().setCodiceStrutturaAmministrativaPadre("");
			}
			//
			
		}
		
		//SOGGETTO:
		model.setSoggetto(new SoggettoImpegnoModel());
		model.setSoggettoSelezionato(false);
		if (impegno.getSoggetto() != null && impegno.getSoggetto().getCodiceSoggetto() != null && !"".equalsIgnoreCase(impegno.getSoggetto().getCodiceSoggetto())) {
			//setto i dati:
			model.getSoggetto().setCodCreditore(impegno.getSoggetto().getCodiceSoggetto());
			model.getSoggetto().setDenominazione(impegno.getSoggetto().getDenominazione());
			model.getSoggetto().setCodfisc(impegno.getSoggetto().getCodiceFiscale());
			model.getSoggetto().setStato(impegno.getSoggetto().getStatoOperativo());
			model.setSoggettoSelezionato(true);
		}
	}

	/**
	 * pulisci i campi della parte relativa ad impegno
	 */
	private void pulisciCampiImpegno() {
		model.setAnnoImpegno(null);
		model.setNumeroImpegno(null);
		model.setNumeroSub(null);
		model.setDescrizioneImpegnoPopup(null);
		model.setImpegnoPopup(new Impegno());
		model.setHasImpegnoSelezionatoPopup(false);
		model.setDisponibilita(null);
	}

	/**
	 * Prepara i campi nel model per la liquidazione andando verso lo step 2
	 */
	private void preparaCampiInserisciLiquidazione() {
		
		//come prima cosa richiamo il metoo
		//per pulire la transazione elementare:
		pulisciTransazioneElementare();		
		
		//setto che il tipo di oggetto 
		//per la te e' la liqudazione:
		teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.LIQUIDAZIONE.toString());
		
		caricaListeBil(CAP_UG);
		caricaTipiProvvedimenti();		
		
		//model del capitolo:
		CapitoloImpegnoModel capitolo = new CapitoloImpegnoModel();
		capitolo.setCodiceMissione(model.getCapitolo().getMissione().getCodice());
		capitolo.setDescrizioneMissione(model.getCapitolo().getMissione().getDescrizione());
		capitolo.setCodiceProgramma(model.getCapitolo().getProgramma().getCodice());
		capitolo.setDescrizioneProgramma(model.getCapitolo().getProgramma().getDescrizione());

		caricaMissione(capitolo);
		caricaProgramma(capitolo);
		if(model.getCapitolo().getProgramma()!=null){
			//setta la lista cofog in teSupport.listaCofog:
			caricaListaCofog(model.getCapitolo().getProgramma().getUid());
		}
		
		//IMPOSTIAMO LE LISTE (classe soggetto, conto tesoria, ecc)
		impostaListe();
	   	
	   	if(model.getImpegno()!=null){
	   		//impegno presente
	   		
	   		impostaDisponibilitaLiquidare();
			
	   		//DESCRIZIONE:
			model.setDescrizioneLiquidazione(model.getImpegno().getDescrizione());
			
			//Importo:
			model.setImportoLiquidazione(null);
			
			//Soggetto, modalita pagamento e sedi secondarie:
			impostaSoggettoModPagESedi();	
			
			//CIG CUP TIPO CONVALIDA E CONTO TESORIERE:
			
			model.setCig(model.getImpegno().getCig());
			model.setCup(model.getImpegno().getCup());	
			
			//SIOPE PLUS:
			//tipo debito siope:
		    model.setScelteTipoDebitoSiope(buildListaTipoDebitoSiope());
			
		    //dati di default ereditati da quelli dell'impegno o il sub selezionato:
			impostaDatiSiopePlusNelModel(model.getImpegno(), model);
			//FINE SIOPE PLUS
			
		   	model.setTipoConvalida("manuale");
			model.setContoTesoriere(null);
			
		 	//SIAC-2879 in caso di lista distinta con un solo elemento preimpostiamo con l'unico:
		   	if(model.getListDistinta()!=null && model.getListDistinta().size()==1 && model.getListDistinta().get(0)!=null){
		   		model.setDistinta(model.getListDistinta().get(0).getCodice());
		   	} else {
		   		model.setDistinta(null);
		   	}
			
			//IMPOSTIAMO LA TE SUPPORT:
			impostaDatiTeSupport();
	   	}
	   	
	  //converto capitoloUscita in capitoloImpegnoModel per richiamare il metodo centralizzato dei dati del capitolo
	  setCapitoloLiquidazioneModel(convertiCapitoloToCapitoloModel(model.getCapitolo()));
	  //seleziono il capitolo per la visualizazzione dei dettagli
	  setCapitoloSelezionato(getCapitoloLiquidazioneModel());
	   	
	}
	
	/**
	 * Metodo interno a preparaCampiInserisciLiquidazione.
	 */
	@SuppressWarnings("unchecked")
	private void impostaListe(){
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.CLASSE_SOGGETTO, TipiLista.CONTO_TESORERIA, TipiLista.DISTINTA);
	   	if(mappaListe.get(TipiLista.CLASSE_SOGGETTO)!=null){
	   		//setto lista classe soggetto
	   		model.setListaClasseSoggetto((List<CodificaFin>)mappaListe.get(TipiLista.CLASSE_SOGGETTO));
	   	}
	   	if(mappaListe.get(TipiLista.CONTO_TESORERIA)!=null){
	   		//setto conto tesoreria
	   		model.setListContoTesoreria((List<CodificaFin>)mappaListe.get(TipiLista.CONTO_TESORERIA));
	   	}
	   	if(mappaListe.get(TipiLista.DISTINTA)!=null){
	   		//setto distinta
	   		model.setListDistinta((List<CodificaFin>)mappaListe.get(TipiLista.DISTINTA));	
	   	}
	}
	
	/**
	 *  Metodo interno a preparaCampiInserisciLiquidazione.
	 */
	private void impostaSoggettoModPagESedi(){
		model.setRadioModPagSelezionato(0);
		model.setRadioSediSecondarieSoggettoSelezionato(0);	
		
		if (model.getImpegno().getSoggetto() != null && model.getImpegno().getSoggetto().getCodiceSoggetto() != null) {
			model.setHasCodiceSoggetto(true);
			model.setClasseImpegno("");
			model.getSoggetto().setCodCreditore(model.getImpegno().getSoggetto().getCodiceSoggetto());
			caricaListeCreditore(model.getImpegno().getSoggetto(), model.getImpegno().getSoggetto().getCodiceSoggetto());
		} else {
			model.setHasCodiceSoggetto(false);
			model.getSoggetto().setCodCreditore(null);
			if (model.getImpegno() != null	&& model.getImpegno().getClasseSoggetto() != null) {
				model.setClasseImpegno(model.getImpegno().getClasseSoggetto().getDescrizione());
			}
			model.setListaModalitaPagamentoSoggetto(null);
			model.setListaSediSecondarieSoggetto(null);
		}
	}
	
	/**
	 * Metodo interno a preparaCampiInserisciLiquidazione.
	 */
	private void impostaDatiTeSupport(){
		
		teSupport.setCup(model.getImpegno().getCup());
		
		if(model.getImpegno().getCodSiope()!=null){
			//siope presente, setto di dati a partire
			//da quelli dell'impegno
			SiopeSpesa siopeSpesam = new SiopeSpesa();
			siopeSpesam.setCodice(model.getImpegno().getCodSiope());
			siopeSpesam.setDescrizione(model.getImpegno().getDescCodSiope());
			teSupport.setSiopeSpesa(siopeSpesam);
			codiceSiopeChangedInternal(siopeSpesam.getCodice());
		}
		
		if(model.getImpegno().getCodMissione()!=null){
	   		//Missione presente
			teSupport.setMissioneSelezionata(model.getImpegno().getCodMissione());
		}
		
		if(model.getImpegno().getCodProgramma()!=null){
			//Programma presente
			teSupport.setProgrammaSelezionato(model.getImpegno().getCodProgramma());
		}

		if(model.getImpegno().getCodPdc()!=null){
			//piano dei conti presente
			teSupport.getPianoDeiConti().setCodice(model.getImpegno().getCodPdc());
		}
		
		if(model.getImpegno().getDescPdc() != null){
			//desrizione piano dei conti
			teSupport.getPianoDeiConti().setDescrizione(model.getImpegno().getCodPdc()+" - "+model.getImpegno().getDescPdc());
		}
		
		if(model.getImpegno().getIdPdc() != null){
			//id piano dei conti
			teSupport.getPianoDeiConti().setUid(model.getImpegno().getIdPdc());
		}
		
		// jira 3070
		if(model.getImpegno().getCodicePdc() != null){
			//codice piano dei conti
			TipoClassificatore tipoPdc = new TipoClassificatore();
			tipoPdc.setCodice(model.getImpegno().getCodicePdc());
			teSupport.getPianoDeiConti().setTipoClassificatore(tipoPdc);
		}
		
		if(model.getImpegno().getCodCofog()!=null){
			//codice cofog
			teSupport.setCofogSelezionato(model.getImpegno().getCodCofog());
		}
		
		if(model.getImpegno().getCodTransazioneEuropeaSpesa()!=null){
			teSupport.setTransazioneEuropeaSelezionato(model.getImpegno().getCodTransazioneEuropeaSpesa());
		}
		
		if(model.getImpegno().getCodRicorrenteSpesa()!=null){
			teSupport.setRicorrenteSpesaSelezionato(model.getImpegno().getCodRicorrenteSpesa());
		}
		
		if(model.getImpegno().getCodCapitoloSanitarioSpesa()!=null){
			teSupport.setPerimetroSanitarioSpesaSelezionato(model.getImpegno().getCodCapitoloSanitarioSpesa());
		}
		
		if(model.getImpegno().getCodPrgPolReg()!=null){
			teSupport.setPoliticaRegionaleSelezionato(model.getImpegno().getCodPrgPolReg());
		}
		
		if(model.getImpegno().getCodClassGen11()!=null){
			teSupport.setClassGenSelezionato1(model.getImpegno().getCodClassGen11());
		}
		
		if(model.getImpegno().getCodClassGen12()!=null){
			teSupport.setClassGenSelezionato2(model.getImpegno().getCodClassGen12());
		}
		
		if(model.getImpegno().getCodClassGen13()!=null){
			teSupport.setClassGenSelezionato3(model.getImpegno().getCodClassGen13());
		}
		
		if(model.getImpegno().getCodClassGen14()!=null){
			teSupport.setClassGenSelezionato4(model.getImpegno().getCodClassGen14());
		}
		
		if(model.getImpegno().getCodClassGen15()!=null){
			teSupport.setClassGenSelezionato5(model.getImpegno().getCodClassGen15());
		}
		if(model.getImpegno()!=null && model.getImpegno().getCapitoloUscitaGestione()!=null && 
				model.getImpegno().getCapitoloUscitaGestione().getMacroaggregato()!=null){
			teSupport.setIdMacroAggregato(model.getImpegno().getCapitoloUscitaGestione().getMacroaggregato().getUid());
		}
	}
	
	/**
	 * per settare i dati principali dell'impegno nel model
	 * @param impegno
	 */
	private void setDatiImpegnoNelModel(Impegno impegno){
		//settiamo il capitolo:
		if (impegno.getCapitoloUscitaGestione() != null) {
			//ok diverso da null
			model.setCapitolo(impegno.getCapitoloUscitaGestione());
		}
		//settiamo l'oggetto impegno da servizio:
		model.setImpegnoPerServizio(impegno);
		//descrizione:
		model.setDescrizioneImpegnoPopup(impegno.getDescrizione());
		//settiamo il tipo impegno:
		if (impegno.getTipoImpegno()!=null) {
			//ok diverso da null
			model.setDescrizioneTipoImpegnoPopup(impegno.getTipoImpegno().getDescrizione());
		}
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
	public boolean transazioneElementareAttiva() {
		return true;
	}

	@Override
	public boolean altriClassificatoriAttivi() {
		return false;
	}

	@Override
	public String confermaPdc() {
		return SUCCESS;
	}


	@Override
	public String confermaSiope() {
		return SUCCESS;
	}
	
	@Override
	public boolean programmaPoliticoRegionaleUnitarioAttivo() {
		return true;
	}

	@Override
	public boolean datiUscitaImpegno() {
		 return true;
	}

	public CapitoloImpegnoModel getCapitoloLiquidazioneModel() {
		return capitoloLiquidazioneModel;
	}

	public void setCapitoloLiquidazioneModel(CapitoloImpegnoModel capitoloLiquidazioneModel) {
		this.capitoloLiquidazioneModel = capitoloLiquidazioneModel;
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

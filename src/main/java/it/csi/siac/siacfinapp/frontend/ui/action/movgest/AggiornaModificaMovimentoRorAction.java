/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.opensymphony.xwork2.util.Element;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbito;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbitoResponse;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.AccertamentoRicercaModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestioneCruscottoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciImpegnoStep1Model;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciModificaMovimentoSpesaModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciMovGestModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ImpegniPluriennaliModel;
import it.csi.siac.siacfinapp.frontend.ui.util.CruscottoRorUtilities;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegnoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaAggiornaMovimento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaAggiornaMovimentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoROR;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione.StatoOperativoModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/*
 * Action per gestisci Impegno Ror...SIAC-6997*/
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaModificaMovimentoRorAction extends ActionKeyAggiornaImpegno {

	/**
	 * 
	 */
	// proprietà di aggiornaImpegnoStep1
	private static final long serialVersionUID = 1L;
	private static final String SUCCESS_SALVA = "success_salva";
	private static final String SUCCESS_AGGIORNA = "success_salva";
	private String numeroImpegno;
	private String annoImpegno;

	private Boolean flagValido;
	private Boolean flagSoggettoValido;
	private Boolean abilitaAzioneInserimentoProvvedimento = true;
	private boolean abilitaPropagaDaSub;

	// proprietà
	private String minImp;
	private String maxImp;
	private String minSub;
	private String maxSub;
	private String minAnche;
	private String maxAnche;
	private String tipoImpegno;
	private String importoImpegnoModImp;
	private String importoAttualeSubImpegno;
	private String descrizione;
	private List<String> tipoImpegnoModificaImportoList = new ArrayList<String>();
	private List<String> numeroSubImpegnoList = new ArrayList<String>();
	private List<String> abbinaList = new ArrayList<String>();
	private String abbina;
	private String abbinaChk;
	private boolean subImpegnoSelected = false;
	private String subSelected;
	private String anniPluriennali;
	private String idTipoMotivo;
	private boolean modificheDaPropagarePieno;

	private boolean propagaSelected = false;

	// Parametri SubImpegno
	private String numeroSubImpegno;
	private Integer uidMovgest;

	// IMPORTI CALCOLATI
	private String minImportoCalcolato;
	private String maxImportoCalcolato;
	private String idSub;
	private String minImportoSubCalcolato;
	private String maxImportoSubCalcolato;
	private BigDecimal minImportoImpegnoApp;
	private BigDecimal maxImportoImpegnoApp;
	private BigDecimal minImportoSubApp;
	private BigDecimal maxImportoSubApp;

	//SIAC-7349
	private BigDecimal minImportoImpegnoCompApp;
	private BigDecimal maxImportoImpegnoCompApp;	
	private AttoAmministrativo am;

	private static final String SI = "Si";
	private static final String NO = "No";

	// SIAC-6997 Properties
	private CodificaFin reimputazione;
	private CodificaFin cancellazioneInsussistenza;
	private CodificaFin cancellazioneInesigibilita;
	private CodificaFin mantenere;

	private String descrizioneMotivoReimp;
	private String descrizioneMotivoRorCancellazioneInsussistenza;
	private String descrizioneMotivoRorCancellazioneInesigibilita;
	private String descrizioneMotivoRorMantenere;

	// SIAC-6997
	// modifiche da cruscotto
	@Element(value = GestioneCruscottoModel.class)
	private List<GestioneCruscottoModel> listaReimputazioneTriennio;
	private Integer indiceRimozioneAnno;

	private GestioneCruscottoModel rorCancellazioneInesigibilita;
	private GestioneCruscottoModel rorCancellazioneInsussistenza;
	private GestioneCruscottoModel rorDaMantenere;
	private Boolean checkDaMantenere;

	// per aggiornamento
	private List<ModificaMovimentoGestioneSpesa> listaModificheRor;
	private List<ModificaMovimentoGestioneSpesa> listaModificheRorDaAggiornare;
	private Boolean insussistenzaRor;
	private Boolean inesigibilitaRor;
	private Boolean daMantenerePresente;
	private String uidModifica;
	
	private boolean disabledDaManterenere;
	
	/// motivi di con
	//RIEPILOGO DATI CRUSCOTTO
	private boolean inInserimento = true;
	//SIAC-7349 Inizio  SR190 CM 20/05/2020
	private boolean inImpegno = true;
	List<ModificaMovimentoGestioneSpesa> listaModificheSpeseCollegata;
	//SIAC-7349 Inizio  SR190 CM 20/05/2020

	// property di elenco modifiche spesa
	private MotiviReimputazioneROR[] motiviRorReimputazione;
	private MotiviCancellazioneROR[] motiviRorCancellazione;
	private MotiviMantenimentoROR[] motiviRorMantenimento;

	public AggiornaModificaMovimentoRorAction() {
		// setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.IMPEGNO;
	}


	
	@Autowired
	ProvvedimentoService provvedimentoService;

	
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");

		// leggo eventuali informazioni

		// invoco il prepare della super classe:
		super.prepare();
		this.model.setTitolo("Gestisci Impegno ROR");
		
		/**
		 * *modifiche in aggiornamento queste, vanno filtrate per stato valido.
		 * Non mostro quelle annullate
		 */

		List<ModificaMovimentoGestioneSpesa> modificheOld = sessionHandler
				.getParametro(FinSessionParameter.LISTA_MODIFICHE_PRIMA_AGGIORNAMENTO);// Modifiche
																						// attualmente
																						// in
																						// DB
		List<ModificaMovimentoGestioneSpesa> modifichePerCruscotto = new ArrayList<ModificaMovimentoGestioneSpesa>();
		if (modificheOld != null) {

			modifichePerCruscotto = CruscottoRorUtilities.getModifichePerCruscotto(modificheOld, modifichePerCruscotto);
		}

		setListaModificheRorDaAggiornare(modifichePerCruscotto);
		// Variazioni delle modifiche da salvare -> nuovi valori per
		// aggiornamento
		List<ModificaMovimentoGestioneSpesa> modificheList = model.getImpegnoInAggiornamento()
				.getListaModificheMovimentoGestioneSpesa();
		
		if(!modifichePerCruscotto.isEmpty()){
			setListaModificheRorDaAggiornare(modifichePerCruscotto);
			
		}else{
			setListaModificheRorDaAggiornare(modificheList);
		}

		// Filtro modifiche
		if (modifichePerCruscotto != null && !modifichePerCruscotto.isEmpty()) {
			modifichePerCruscotto = CruscottoRorUtilities.getModificheStatoValido(modifichePerCruscotto);
		}

		// Inizializzazione
		// controllo se invece si tratta di un subimpegno

		if (getNumeroSubImpegno() != null || model.getnSubImpegno() != null) {
			modifichePerCruscotto = checkSubHasNotModifies();
		}

		
		//SIAC-7349 Inizio  SR190 CM 20/05/2020
		setInImpegno(true);
		//SIAC-7349 Fine  SR190 CM 20/05/2020
		
		if (modifichePerCruscotto == null || modifichePerCruscotto.isEmpty()) {
			setInInserimento(true);
		} else {
			setInInserimento(false);
			checkInsussistenzaInsegibilitaMantenerePresenti(modifichePerCruscotto);
		}

		// prepare di aggiorna impegno. Carico tutte le configurazioni
		this.model.setOperazioneAggiorna(true);
		try {
			if (model.getListaTipiProvvedimenti() == null || model.getListaTipiProvvedimenti().size() == 0) {
				caricaTipiProvvedimenti();
			}

			if (model.getListaTipiAmbito() == null || model.getListaTipiAmbito().isEmpty()) {
				caricaListaAmbiti();
			}

			caricaStatiOperativiAtti();

			caricaListePerRicercaSoggetto();
			caricaListeGestisciImpegnoStep1();

			model.getStep1Model().setDaRiaccertamento(buildListaSiNo());
			model.getStep1Model().setListflagAttivaGsa(buildListaSiNo());

			// SIAC-6997
			model.getStep1Model().setDaReanno(buildListaSiNo());

			model.getStep1Model().setDaPrenotazione(buildListaSiNo());
			model.getStep1Model().setDiCassaEconomale(buildListaSiNo());

			model.getStep1Model().setScelteFrazionabile(buildListaFrazionabile());

			// non agisce l'ancora dei vincoli
			model.getStep1Model().setPortaAdAltezzaVincoli(false);

			// tipo debito siope:
			model.getStep1Model().setScelteTipoDebitoSiope(buildListaTipoDebitoSiopePerImpegni());
			inizializzaAggiornaModifiche();
			// inizializzaListaModifiche();

		} catch (Exception e) {
			log.error("prepare", e.getMessage());
		}

		setVisualizzaLinkConsultaModificheProvvedimento(false);

		if (abilitaListaROR()) {
			caricaListaMotiviROR();
			model.setGestioneModificaDecentratoEFaseROR(true);
		} else {
			caricaListaMotivi();
			model.setGestioneModificaDecentratoEFaseROR(false);
		}
		if(model.getCodicePdc()!=null){
			inizializzaDescrizioniEMotivi();			
		}
		model.setDaCruscotto(true);
		setAbilitazioni();
		
		//SIAC-7349 Inizio  SR190 CM 20/05/2020
		if (modifichePerCruscotto != null && !modifichePerCruscotto.isEmpty()) {

			boolean reimpPresent = CruscottoRorUtilities.checkReimpInModif(modifichePerCruscotto);

			if (reimpPresent) {
				List<ModificaMovimentoGestioneSpesa> listaReimputazioniPresenti = new ArrayList<ModificaMovimentoGestioneSpesa>();
				listaReimputazioniPresenti = CruscottoRorUtilities.getListaReimputazioniPresenti(modifichePerCruscotto);
				
				modifichePerCruscotto = CruscottoRorUtilities
						.getListaOtherCruscottoModifichePresenti(modifichePerCruscotto);
				listaReimputazioniPresenti.addAll(modifichePerCruscotto);
				setListaModificheSpeseCollegata(listaReimputazioniPresenti);
				
			} else {
				List<ModificaMovimentoGestioneSpesa> listeNuove = CruscottoRorUtilities.addReimputazioneInAggiornamento(
						modifichePerCruscotto, Integer.valueOf(sessionHandler.getBilancio().getAnno()));
				setListaModificheSpeseCollegata(listeNuove);
			}

		}
		//SIAC-7349 Fine  SR190 CM 20/05/2020
	}

	private List<ModificaMovimentoGestioneSpesa> checkSubHasNotModifies() {
		List<ModificaMovimentoGestioneSpesa> modifichePerCruscotto = new ArrayList<ModificaMovimentoGestioneSpesa>();
		if (getNumeroSubImpegno() != null || model.getnSubImpegno() != null) {

			List<SubImpegno> listaSubimpegni = model.getListaSubimpegni();
			SubImpegno sub = new SubImpegno();
			for (int i = 0; i < listaSubimpegni.size(); i++) {
				if (listaSubimpegni.get(i).getNumeroBigDecimal().equals(new BigDecimal(model.getnSubImpegno()))) {
					sub = listaSubimpegni.get(i);
					break;
				}
			}
			List<ModificaMovimentoGestioneSpesa> listaModifiche = new ArrayList<ModificaMovimentoGestioneSpesa>();
			listaModifiche = sub.getListaModificheMovimentoGestioneSpesa();
			if (listaModifiche == null) {
				listaModifiche = new ArrayList<ModificaMovimentoGestioneSpesa>();
			}

			modifichePerCruscotto = CruscottoRorUtilities.getModifichePerCruscotto(listaModifiche,
					modifichePerCruscotto);

			modifichePerCruscotto = CruscottoRorUtilities.getModificheStatoValido(modifichePerCruscotto);

		}

		return modifichePerCruscotto;
	}

	// Per la gestione delle modifiche pregresse -> Serve a mantenere una
	// coerenza con la maschera di Inserisci (quando non ve ne sono presenti)
	private void checkInsussistenzaInsegibilitaMantenerePresenti(List<ModificaMovimentoGestioneSpesa> modificheList) {

		int ineRor = 0;
		int insRor = 0;
		int manRor = 0;

		for (int i = 0; i < modificheList.size(); i++) {
			if (!modificheList.get(i).getTipoModificaMovimentoGestione().equals("REIMP")) {
				if (modificheList.get(i).getTipoModificaMovimentoGestione().equals("INEROR")) {
					ineRor++;
				} else if (modificheList.get(i).getTipoModificaMovimentoGestione().equals("INSROR")) {
					insRor++;
				} else if (modificheList.get(i).getTipoModificaMovimentoGestione().equals("RORM")) {
					manRor++;
				}
			}
		}

		if (ineRor > 0) {
			setInesigibilitaRor(true);
		} else {
			setInesigibilitaRor(false);
			rorCancellazioneInesigibilita = new GestioneCruscottoModel();
		}

		if (insRor > 0) {
			setInsussistenzaRor(true);
		} else {
			setInsussistenzaRor(false);
			rorCancellazioneInsussistenza = new GestioneCruscottoModel();
		}

		if (manRor > 0) {
			setDaMantenerePresente(true);
		} else {
			setDaMantenerePresente(false);
			rorDaMantenere = new GestioneCruscottoModel();
		}

	}

	// Inizializza la lista del triennio reimputazione, per costruire la mappa
	// nel cruscotto
	private void inizializzaListaModifiche() {
		GestioneCruscottoModel modifica0 = new GestioneCruscottoModel();
		GestioneCruscottoModel modifica1 = new GestioneCruscottoModel();
		GestioneCruscottoModel modifica2 = new GestioneCruscottoModel();

		listaReimputazioneTriennio = new ArrayList<GestioneCruscottoModel>();

		if (listaReimputazioneTriennio == null || listaReimputazioneTriennio.isEmpty()) {
			// anno di reimputazione deve essere maggiore dell'anno di bilancio
			Integer anno = Integer.valueOf(sessionHandler.getBilancio().getAnno());
			modifica0.setAnno(anno + 1);
			modifica0.setIndex(0);
			getListaReimputazioneTriennio().add(modifica0);
			modifica1.setAnno(anno + 2);
			modifica1.setIndex(1);
			getListaReimputazioneTriennio().add(modifica1);
			modifica2.setAnno(anno + 3);
			modifica2.setIndex(2);
			getListaReimputazioneTriennio().add(modifica2);
		}

	}

	/**
	 * metodo execute della action
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// ripopoliamo i dati provv dal model support:
		// setta nel mode
		model.setDaCruscotto(true);
		ripopolaProvvedimentoDaSupport();

		// Anno capitolo
		if (sessionHandler.getAnnoBilancio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoBilancio().toString())) {
			model.getStep1Model().getCapitolo().setAnno(new Integer(sessionHandler.getAnnoBilancio()));
			model.getCapitoloRicerca().setAnno(sessionHandler.getAnnoBilancio());
		}

		// Anno impegno
		if (getAnnoImpegno() != null) {
			model.getStep1Model().setAnnoImpegno(Integer.valueOf(getAnnoImpegno()));
			model.getStep1Model().setNumeroImpegno(Integer.valueOf(getNumeroImpegno()));
		} else {
			model.getStep1Model().setAnnoImpegno(model.getAnnoImpegno());
			model.getStep1Model().setNumeroImpegno(model.getNumeroImpegno());
		}

		if ((getNumeroSubImpegno() != null && !getNumeroSubImpegno().equals(""))) {
			model.getStep1Model().setNumeroSubImpegno(Integer.valueOf(getNumeroSubImpegno()));
		}

		// transazione elementare:
		if (teSupport == null) {
			pulisciTransazioneElementare();
		}
		// utilizzato per la transazione e le condizioni di obbligatorieta
		teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.IMPEGNO.toString());

		if (forceReload) {

			// CARICAMENTO AVANZOVINCOLI:
			caricaListaAvanzovincolo();
			initSceltaAccertamentoAvanzoList();
			//

			sessionHandler.cleanSafelyExcluding(FinSessionParameter.IMPEGNO_CERCATO);
			caricaDati(false);
			sessionHandler.setParametro(FinSessionParameter.IMPEGNO_CERCATO, getImpegnoToUpdate());
			// gestione subimpegni

			// Jira - 1298 altrimenti carica ad ogni giro tutti i dati da
			// bilancio
			if (caricaListeBil(WebAppConstants.CAP_UG)) {
				return INPUT;
			}

			if (model.getImpegnoInAggiornamento().getProgetto() != null
					&& model.getImpegnoInAggiornamento().getProgetto().getCodice() != null) {
				popolaProgetto(model.getImpegnoInAggiornamento().getProgetto());
			}

			if (((MovimentoGestione) sessionHandler.getParametro(FinSessionParameter.IMPEGNO_CERCATO))
					.isFlagSoggettoDurc()) {
				model.getStep1Model().setSoggettoDurc(SI);
			} else {
				model.getStep1Model().setSoggettoDurc(NO);
			}

		}

		if (((MovimentoGestione) sessionHandler.getParametro(FinSessionParameter.IMPEGNO_CERCATO))
				.isFlagDaRiaccertamento()) {
			model.getStep1Model().setRiaccertato(SI);
		} else {
			model.getStep1Model().setRiaccertato(NO);
		}

		if (((MovimentoGestione) sessionHandler.getParametro(FinSessionParameter.IMPEGNO_CERCATO)).isFlagDaReanno()) {
			model.getStep1Model().setReanno(SI);
		} else {
			model.getStep1Model().setReanno(NO);
		}

		creaMovGestModelCache();

		// controlliamo lo stato provvedimento:
		controllaStatoProvvedimento();

		model.setStep1ModelSubimpegno(new GestisciImpegnoStep1Model());
		model.setStep1ModelSubimpegnoCache(new GestisciImpegnoStep1Model());

		// disabilito il caricamento degli alberi inutili per questo scnario (in
		// AjaxAction.java):
		teSupport.setRicaricaAlberoPianoDeiConti(false);
		// CR-2023
		teSupport.setRicaricaStrutturaAmministrativa(false);
		teSupport.setRicaricaSiopeSpesa(false);
		//////////////////////////////////////////////////////////////////////////////////////////

		// setto l'anno capitolo per la ricerca guidata del vincolo
		if (model.getStep1Model().getCapitolo() != null && model.getStep1Model().getCapitolo().getAnno() != null) {
			model.getStep1Model().setAccertamentoRicerca(new AccertamentoRicercaModel());
			model.getStep1Model().getAccertamentoRicerca()
					.setAnnoCapitolo(String.valueOf(model.getStep1Model().getCapitolo().getAnno()));
		}

		calcolaTotaliUtilizzabile();

		if (salvaDaSDFANormale()) {
			addActionWarningFin(ErroreFin.WARNING_IMPEGNO_SDF_CON_DISPONIBILE);
		}

		// leggiEventualiErroriEWarningAzionePrecedente(true);
		clearActionErrors();
		sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE, null);
		
		// carico dati dell'impegno
		caricaDatiTotali(null);

		checkIfIsSub();

		List<StrutturaAmministrativoContabile> lista = sessionHandler.getAccount()
				.getStruttureAmministrativeContabili();

		if (lista != null && !lista.isEmpty()) {

			if (lista.size() > 1) {
				// devo filtrare l'elenco per codice
				for (int j = 0; j < lista.size(); j++) {

					if (lista.get(j).getUid() == Integer
							.parseInt(model.getStep1Model().getStrutturaSelezionataCompetente())) {

						model.getStep1Model().setStrutturaSelezionataCompetenteDesc(
								lista.get(j).getCodice() + "-" + lista.get(j).getDescrizione());
					}
				}
			}
		}

		if (abilitaListaROR()) {
			caricaListaMotiviROR();
			model.setGestioneModificaDecentratoEFaseROR(true);
		} else {
			caricaListaMotivi();
			model.setGestioneModificaDecentratoEFaseROR(false);
		}

		// Inizializzazione -> Vedi spiegazione alla prepare();

		

//		BigDecimal importoImpegno = (model.getStep1Model().getImportoImpegno());
//
//
//		BigDecimal importoDaRiaccertare = importoImpegno.subtract(importoPagatoAnnoAntecedente);
//
//		model.getStep1Model().setImportoDaRiaccertare(importoDaRiaccertare);

		List<ModificaMovimentoGestioneSpesa> modificheList = new ArrayList<ModificaMovimentoGestioneSpesa>();
		// fixme modifiche per subimpegno
		if ((getNumeroSubImpegno() != null && !getNumeroSubImpegno().equals("")) || model.getnSubImpegno() != null) {
			List<SubImpegno> listaSubimpegni = model.getListaSubimpegni();
			SubImpegno sub = new SubImpegno();
			for (int i = 0; i < listaSubimpegni.size(); i++) {
				if (listaSubimpegni.get(i).getNumeroBigDecimal().equals(new BigDecimal(model.getnSubImpegno()))) {
					sub = listaSubimpegni.get(i);
					break;
				}
			}
			modificheList = sub.getListaModificheMovimentoGestioneSpesa();

		} else {
			modificheList = model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa();
		}
		List<ModificaMovimentoGestioneSpesa> modifichePerCruscotto = new ArrayList<ModificaMovimentoGestioneSpesa>();
		if (modificheList != null) {

			modifichePerCruscotto = CruscottoRorUtilities.getModifichePerCruscotto(modificheList,
					modifichePerCruscotto);
		}
		if (modifichePerCruscotto != null) {
			modifichePerCruscotto = CruscottoRorUtilities.getModificheStatoValido(modifichePerCruscotto);

		}
		if (modifichePerCruscotto != null && !modifichePerCruscotto.isEmpty()) {

			boolean reimpPresent = CruscottoRorUtilities.checkReimpInModif(modifichePerCruscotto);
			List<ModificaMovimentoGestioneSpesa> cloneModifiche = clone(modifichePerCruscotto);
			sessionHandler.setParametro(FinSessionParameter.LISTA_MODIFICHE_PRIMA_AGGIORNAMENTO, cloneModifiche);
			
			//SIAC-7502 visualizzo tutto in positivo
			modifichePerCruscotto =convertiImportiInPositivo(modifichePerCruscotto, true);
			
			setInInserimento(false);
			//SIAC-7349 Inizio  SR190 CM 20/05/2020
			setInImpegno(true);
			//SIAC-7349 Inizio  SR190 CM 20/05/2020
			checkInsussistenzaInsegibilitaMantenerePresenti(modifichePerCruscotto);
			if (reimpPresent) {
				List<ModificaMovimentoGestioneSpesa> listaReimputazioniPresenti = new ArrayList<ModificaMovimentoGestioneSpesa>();
				listaReimputazioniPresenti = CruscottoRorUtilities.getListaReimputazioniPresenti(modifichePerCruscotto);
				
				modifichePerCruscotto = CruscottoRorUtilities
						.getListaOtherCruscottoModifichePresenti(modifichePerCruscotto);
				listaReimputazioniPresenti.addAll(modifichePerCruscotto);
				setListaModificheRor(listaReimputazioniPresenti);//
				//SIAC-7349 Inizio SR190 CM 21/05/2020
				setListaModificheSpeseCollegata(listaReimputazioniPresenti);
				//SIAC-7349 Fine SR190 CM 21/05/2020
				
			} else {
				List<ModificaMovimentoGestioneSpesa> listeNuove = CruscottoRorUtilities.addReimputazioneInAggiornamento(
						modifichePerCruscotto, Integer.valueOf(sessionHandler.getBilancio().getAnno()));
				setListaModificheRor(listeNuove);
				//SIAC-7349 Inizio SR190 CM 21/05/2020
				setListaModificheSpeseCollegata(listeNuove);
				//SIAC-7349 Fine SR190 CM 21/05/2020
			}

		} else {
			setInInserimento(true);
		}

//		BigDecimal importoGestito = CruscottoRorUtilities.calcolaImportoGestito(modificheList);
//		model.getStep1Model().setImportoGestito(importoGestito);
//
//		BigDecimal importoDaGestire = importoDaRiaccertare.subtract(importoGestito);
//		model.getStep1Model().setImportoDaGestire(importoDaGestire);

		inizializzaAggiornaModifiche();

		creaMovGestModelCache();

		// controllo se si tratta di subimpegno e setto nel model il
		// provvedimento associato al subimpegno: se non ci sono modifiche
		// imposto nel model il provvedimento relativo al subimpegno; se ci sono
		// modifiche, setto il provvedimento associato alle modifiche del
		// subimpegno.
		setAbilitazioni();
		
		// Controllo eventuali messaggi di successo provenienti dal salva
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiInformazioniAzionePrecedente();
		
		inizializzaDescrizioniEMotivi();


		inizializzaListaModifiche();
		

		return SUCCESS;

	}

	private List<ModificaMovimentoGestioneSpesa> convertiImportiInPositivo(
			List<ModificaMovimentoGestioneSpesa> listaModificheRor2, boolean b) {
		for(ModificaMovimentoGestioneSpesa mmgs : listaModificheRor2){
			if(!mmgs.getTipoModificaMovimentoGestione().equals("RORM")){
				BigDecimal importoPositivo = mmgs.getImportoOld().abs();
				mmgs.setImportoOld(importoPositivo);				
			}
		}
		return listaModificheRor2;
	}

	private void checkIfIsSub() {
		if ((getNumeroSubImpegno() != null && !getNumeroSubImpegno().equals("")) || model.getnSubImpegno() != null) {
			setTipoImpegno("SubImpegno");
			setSubSelected(String.valueOf(model.getnSubImpegno()));
			caricaDatiSub();
			
			if (model.getStep1Model().getNumeroSubImpegno().equals(Integer.valueOf(model.getnSubImpegno()))) {
				List<SubImpegno> listaSubimpegni = model.getListaSubimpegni();
				SubImpegno sub = new SubImpegno();
				for (int i = 0; i < listaSubimpegni.size(); i++) {
					if (listaSubimpegni.get(i).getNumeroBigDecimal().equals(new BigDecimal(model.getnSubImpegno()))) {
						sub = listaSubimpegni.get(i);
						break;
					}
				}
				List<ModificaMovimentoGestioneSpesa> listaModifiche = sub.getListaModificheMovimentoGestioneSpesa();
				List<ModificaMovimentoGestioneSpesa> modifichePerCruscotto = new ArrayList<ModificaMovimentoGestioneSpesa>();
				if (listaModifiche != null) {

					modifichePerCruscotto = CruscottoRorUtilities.getModifichePerCruscotto(listaModifiche,
							modifichePerCruscotto);
				}

				if (modifichePerCruscotto != null && !modifichePerCruscotto.isEmpty()) {
					modifichePerCruscotto = CruscottoRorUtilities.getModificheStatoValido(modifichePerCruscotto);
				}
				//fix provvedimento associato alle modifiche per il cruscotto valide
				if (modifichePerCruscotto != null && !modifichePerCruscotto.isEmpty()) {
					model.getStep1Model().setProvvedimento(CruscottoRorUtilities.mapAttoToProvv(
							modifichePerCruscotto.get(0).getAttoAmministrativo()));
					model.getStep1ModelCache().setProvvedimento(CruscottoRorUtilities.mapAttoToProvv(
							modifichePerCruscotto.get(0).getAttoAmministrativo()));

					// model.setImpegnoInAggiornamento(sub);
				} else {
					setInInserimento(true);
					model.getStep1Model()
							.setProvvedimento(CruscottoRorUtilities.mapAttoToProvv(sub.getAttoAmministrativo()));

				}
				model.setSubimpegnoInAggiornamento(sub);

			}
		} else {
			List<ModificaMovimentoGestioneSpesa> listaModifiche = model.getImpegnoInAggiornamento()
					.getListaModificheMovimentoGestioneSpesa();
			List<ModificaMovimentoGestioneSpesa> modifichePerCruscotto = new ArrayList<ModificaMovimentoGestioneSpesa>();
			if (listaModifiche != null) {

				modifichePerCruscotto = CruscottoRorUtilities.getModifichePerCruscotto(listaModifiche,
						modifichePerCruscotto);
			}
			if (modifichePerCruscotto != null) {
				modifichePerCruscotto = CruscottoRorUtilities.getModificheStatoValido(modifichePerCruscotto);
			}

			if (modifichePerCruscotto != null && !modifichePerCruscotto.isEmpty()) {
				model.getStep1Model().setProvvedimento(
						CruscottoRorUtilities.mapAttoToProvv(modifichePerCruscotto.get(0).getAttoAmministrativo()));
				
			} else {
				if (null != model.getStep1Model().getProvvedimento()
						&& null != model.getStep1Model().getProvvedimento().getAnnoProvvedimento()) {
					// CHIAMO IL POPOLA COSI HO UN'ISTANZA RICREATA CON IL "new"
					// in modo da evitare ogni incrocio di dati con il
					// provvedimento
					// salvato in memoria che verra' usato momentaneamente per
					// la modifica movimento:
					AttoAmministrativo attoImpegno = popolaProvvedimento(model.getStep1Model().getProvvedimento());
					setAm(attoImpegno);
				}

			}

		}
	}

	private void inizializzaDescrizioniEMotivi() {
		motiviRorReimputazione = CruscottoRorUtilities.filterByPdc(MotiviReimputazioneROR.values(), model.getCodicePdc());
		motiviRorCancellazione = CruscottoRorUtilities.filterByPdc(MotiviCancellazioneROR.values(), model.getCodicePdc());
		motiviRorMantenimento = CruscottoRorUtilities.filterByPdc(MotiviMantenimentoROR.values(), model.getCodicePdc());
		
		//SIAC-7989 refactoring for null safety - Begin
		List<CodificaFin> listaTipoMotivo = model.getMovimentoSpesaModel() != null && 
				model.getMovimentoSpesaModel().getListaTipoMotivo() != null 
					? model.getMovimentoSpesaModel().getListaTipoMotivo() : new ArrayList<CodificaFin>();
					
		reimputazione = CruscottoRorUtilities.getDescrizioneMotivo(listaTipoMotivo, "REIMP");
		cancellazioneInsussistenza = CruscottoRorUtilities.getDescrizioneMotivo(listaTipoMotivo, "INSROR");
		cancellazioneInesigibilita = CruscottoRorUtilities.getDescrizioneMotivo(listaTipoMotivo, "INEROR");
		mantenere = CruscottoRorUtilities.getDescrizioneMotivo(listaTipoMotivo, CODICE_MOTIVO_ROR_MANTENERE);

		descrizioneMotivoReimp = reimputazione != null && reimputazione.getDescrizione() != null ?
				reimputazione.getDescrizione() : new String();
		descrizioneMotivoRorCancellazioneInsussistenza = cancellazioneInsussistenza != null && cancellazioneInsussistenza.getDescrizione() != null ?
				cancellazioneInsussistenza.getDescrizione() : new String();
		descrizioneMotivoRorCancellazioneInesigibilita = cancellazioneInesigibilita != null && cancellazioneInesigibilita.getDescrizione() != null ?
				cancellazioneInesigibilita.getDescrizione() : new String();
		descrizioneMotivoRorMantenere = mantenere != null && mantenere.getDescrizione() != null ?
				mantenere.getDescrizione() : new String();
		//SIAC-7989 refactoring for null safety - End
				
	}

	// FIXME Da Modificare con i calcoli che ha gia effettuato Vincenzo

	public String caricaDatiSub() {
		setMethodName("caricaDatiSub");

		if (getAbbinaChk() != null && getAbbinaChk().equalsIgnoreCase("true")) {
			setAbbina("Modifica Anche Impegno");
		} else {
			setAbbina(null);
		}

		if (StringUtils.isEmpty(getTipoImpegno())) {
			setTipoImpegno("Impegno");
			setSubImpegnoSelected(false);
			model.setSubImpegnoSelectedMod(false);
		} else if (getTipoImpegno().equalsIgnoreCase("SubImpegno") ) {
			if (!StringUtils.isEmpty(getSubSelected()) || !model.getStep1Model().getNumeroSubImpegno().equals("")) {
				setSubImpegnoSelected(true);
				setTipoImpegno("SubImpegno");
				model.setSubImpegnoSelectedMod(true);

				for (SubImpegno sub : model.getListaSubimpegni()) {
					if (String.valueOf(sub.getNumeroBigDecimal()).equals(getSubSelected())) {

						setImportoAttualeSubImpegno(convertiBigDecimalToImporto(sub.getImportoAttuale()));
						model.setImportoAttualeSubImpegnoMod(convertiBigDecimalToImporto(sub.getImportoAttuale()));
						setNumeroSubImpegno(String.valueOf(sub.getNumeroBigDecimal()));
						model.setNumeroSubImpegnoMod(String.valueOf(sub.getNumeroBigDecimal()));

						if (sub.getDisponibilitaImpegnoModifica() == null) {
							sub.setDisponibilitaImpegnoModifica(BigDecimal.ZERO);
						}

						minImportoSubApp = sub.getDisponibilitaImpegnoModifica().negate();

						if (model.getImpegnoInAggiornamento().getAnnoMovimento() < sessionHandler.getAnnoBilancio()) {
							maxImportoSubApp = new BigDecimal(0);
						} else if (model.getImpegnoInAggiornamento().isFlagDaRiaccertamento()) {
							maxImportoSubApp = new BigDecimal(0);
						} else {
							maxImportoSubApp = model.getImpegnoInAggiornamento().getDisponibilitaSubimpegnare();
						}

						if (minImportoSubApp == null) {
							minImportoSubApp = new BigDecimal(0);
						}

						if (maxImportoSubApp == null) {
							maxImportoSubApp = new BigDecimal(0);
						}

						setMaxSub(convertiBigDecimalToImporto(maxImportoSubApp));
						setMinSub(convertiBigDecimalToImporto(minImportoSubApp));
						model.setMaxSubMod(convertiBigDecimalToImporto(maxImportoSubApp));
						model.setMinSubMod(convertiBigDecimalToImporto(minImportoSubApp));

						setMinImportoSubCalcolato(convertiBigDecimalToImporto(minImportoSubApp));
						model.setMinImportoSubCalcolatoMod(convertiBigDecimalToImporto(minImportoSubApp));
						setMaxImportoSubCalcolato(convertiBigDecimalToImporto(maxImportoSubApp));
						model.setMaxImportoSubCalcolatoMod(convertiBigDecimalToImporto(maxImportoSubApp));

						// Parte per anche impegno

						// Modifica importi richiesta
						setMinAnche(convertiBigDecimalToImporto(minImportoSubApp));
						model.getMovimentoSpesaModel().setMinAncheImpegno(minImportoSubApp);
						model.setMinAncheMod(convertiBigDecimalToImporto(minImportoSubApp));

						if (model.getStep1Model().getAnnoImpegno().intValue() == model.getImpegnoInAggiornamento()
								.getCapitoloUscitaGestione().getAnnoCapitolo().intValue()) {
							// anno corrente
							maxImportoImpegnoApp = model.getImpegnoInAggiornamento().getCapitoloUscitaGestione()
									.getImportiCapitolo().getDisponibilitaImpegnareAnno1();
						} else if (model.getStep1Model().getAnnoImpegno()
								.intValue() == (model.getImpegnoInAggiornamento().getCapitoloUscitaGestione()
										.getAnnoCapitolo().intValue() + 1)) {
							// anno +1
							maxImportoImpegnoApp = model.getImpegnoInAggiornamento().getCapitoloUscitaGestione()
									.getImportiCapitolo().getDisponibilitaImpegnareAnno2();
						} else if (model.getStep1Model().getAnnoImpegno()
								.intValue() == (model.getImpegnoInAggiornamento().getCapitoloUscitaGestione()
										.getAnnoCapitolo().intValue() + 2)) {
							// anno +2
							maxImportoImpegnoApp = model.getImpegnoInAggiornamento().getCapitoloUscitaGestione()
									.getImportiCapitolo().getDisponibilitaImpegnareAnno3();
						} else {
							// da GESTIRE NELLA PAGINA
							model.setFlagSuperioreTreAnni(true);
						}

						// GESTIONE FLAG SDF:
						if (model.getImpegnoInAggiornamento().isFlagSDF()) {
							maxImportoImpegnoApp = new BigDecimal(0);
						}
						//

						//SIAC-8594
						if (maxImportoImpegnoApp == null) {
							maxImportoImpegnoApp = new BigDecimal(0);
						}

						// Modifica importi richiesta
						setMaxAnche(convertiBigDecimalToImporto(maxImportoImpegnoApp));
						model.getMovimentoSpesaModel().setMaxAncheImpegno(maxImportoImpegnoApp);
						model.setMaxAncheMod(convertiBigDecimalToImporto(maxImportoImpegnoApp));
						model.setSubimpegnoInAggiornamento(sub);

					}
				}

			} else {
				setSubImpegnoSelected(false);
				model.setSubImpegnoSelectedMod(false);
				tipoImpegno = "Impegno";
			}
		}

		return SUCCESS;
	}

	private void caricaListaAmbiti() {
		RicercaTipiAmbito request = model.creaRequestRicercaTipiAmbito();
		request.setAnno(sessionHandler.getAnnoBilancio());
		RicercaTipiAmbitoResponse response = progettoService.ricercaTipiAmbito(request);
		model.setListaTipiAmbito(response.getTipiAmbito());
	}

	private void setAbilitazioni() {
		if (model.getStep1Model().getNumeroImpegno() != null) {

			setAbilitaModificaImporto(true);

			boolean bilPrecInPredisposizioneConsuntivo = isBilancioPrecedenteInPredisposizioneConsuntivo();

			setFlagValido(false);
			setFlagSoggettoValido(false);
			// stato D o ND
			if (!model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa()
					.equals(CostantiFin.MOVGEST_STATO_PROVVISORIO)) {
				setFlagValido(true);

				// nel caso in cui fossimo in predisposizione consuntivo,
				// rimettiamo flagvalido a false
				// in modo che l'importo ritorni modificabile:

				if (bilPrecInPredisposizioneConsuntivo && isResiduo()) {
					setAbilitaModificaImporto(true);
				} else {
					setAbilitaModificaImporto(false);
				}
				//

				setFlagSoggettoValido(true);
			}

			// JIRA SIAC-3506 in caso di residuo con presenza di modifiche di
			// importo valide, importo non modificabile:
			List<ModificaMovimentoGestioneSpesa> modifiche = model.getImpegnoInAggiornamento()
					.getListaModificheMovimentoGestioneSpesa();
			if (presenteModificaDiImportoValida(modifiche) && isResiduo()) {
				setAbilitaModificaImporto(false);
			}
			//

			// stato
			if (model.getImpegnoInAggiornamento().getElencoSubImpegni() != null
					|| (model.getListaSubimpegni() != null && model.getListaSubimpegni().size() > 0)) {
				setFlagSoggettoValido(true);
			}

			// jira-1582
			// se e' in stato N e ci sono dei sub allora posso modificare il
			// soggetto
			if (model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa()
					.equals(CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE)) {
				if (!presenteAlmenoUnMovValido(model.getListaSubimpegni(),
						OggettoDaPopolareEnum.SUBIMPEGNO.toString())) {
					setFlagSoggettoValido(false);
				}
			}
		}
		if (model.getStep1Model().getProgetto() != null) {
			model.getStep1ModelSubimpegno().setProgetto(model.getStep1Model().getProgetto());
		}
		if (model.getImpegnoInAggiornamento() != null
				&& model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa() != null
				&& model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa().equals("P")) {
			setAbilitaPropagaDaSub(false);
		} else {
			setAbilitaPropagaDaSub(true);
		}
	}

	public boolean abilitatoAzioneInserimentoProvvedimento() {
		return false;
	}

	// carica dati dell'impegno
	private void caricaDatiTotali(Impegno impegnoFresco) {

		List<ModificaMovimentoGestioneSpesa> listaModifiche = new ArrayList<ModificaMovimentoGestioneSpesa>();
		// Raffa:
		// Era commentato! Ricarica impegnoInAggiornamento
		// che risulta essere null dopo l'annulla della modifica importo
		RicercaImpegnoPerChiaveOttimizzatoROR parametroRicercaPerChiave = new RicercaImpegnoPerChiaveOttimizzatoROR();
		RicercaImpegnoK impegnoDaCercare = new RicercaImpegnoK();
		BigDecimal numeroImpegno = new BigDecimal(String.valueOf(model.getStep1Model().getNumeroImpegno()));

		// setto i dati di chiave dell'impegno:
		impegnoDaCercare.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
		impegnoDaCercare.setNumeroImpegno(numeroImpegno);
		impegnoDaCercare.setAnnoImpegno(model.getStep1Model().getAnnoImpegno());

		if (getNumeroSubImpegno() != null && !getNumeroSubImpegno().equals("")) {
			impegnoDaCercare.setNumeroSubDaCercare(new BigDecimal(getNumeroSubImpegno()));
		}

		// richiedente:
		parametroRicercaPerChiave.setRichiedente(sessionHandler.getRichiedente());
		// ente:
		parametroRicercaPerChiave.setEnte(model.getEnte());
		parametroRicercaPerChiave.setpRicercaImpegnoK(impegnoDaCercare);

		// non carichiamo i sub:
		parametroRicercaPerChiave.setCaricaSub(false);

		// imposto i dati opzionali:
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaElencoModificheMovGest(true);
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaDisponibileLiquidareEDisponibilitaInModifica(true);

		// PER SIAC-5785 - serve caricare anche il cig dato che viene
		// controllato nei controlli SIOPE PLUS:
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaCig(true);
		// il cup viene gratis assieme al cig (sono attr entrambi vedi
		// funzonamento servizio)
		// quindi tanto vale farlo caricare:
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaCup(true);
		//

		parametroRicercaPerChiave.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);

		// richiamo il servizio di ricerca:
		//RicercaImpegnoPerChiaveOttimizzatoResponse respRk = movimentoGestioneFinService	.ricercaImpegnoPerChiaveOttimizzato(parametroRicercaPerChiave);
		if(getUidMovgest()!=null){
			model.setUidMovgest(getUidMovgest());			
		}
		parametroRicercaPerChiave.setUidMovGest(getUidMovgest() != null ? getUidMovgest() : model.getUidMovgest());
		RicercaImpegnoPerChiaveOttimizzatoResponse respRk = movimentoGestioneFinService	.ricercaImpegnoPerChiaveOttimizzatoROR(parametroRicercaPerChiave);
		
		// qui forse devo verificare se si tratta di subimpegno
		// analizzo la risposta del servizio:

		if (!respRk.isFallimento() && respRk.getImpegno() != null) {
			impegnoFresco = respRk.getImpegno();
			String codePdc = CruscottoRorUtilities.codePdc(impegnoFresco.getCodPdc());
			model.setCodicePdc(codePdc);
			model.setImportoDaRiaccertare(impegnoFresco.getImportoDaRiaccertare());
			model.setImportoMassimoDaRiaccertare(impegnoFresco.getImportoMaxDaRiaccertare());
			model.setImportoModifiche(impegnoFresco.getImportoModifiche());
			model.setResiduoEventualeDaMantenere(impegnoFresco.getResiduoDaMantenere());
			//SIAC-7551 - 25/05/2020 - GM
			if(impegnoFresco.getDocumentiNoLiqAnnoSuccessivo() != null)
				model.setDocumentiNoLiqAnnoSuccessivo(impegnoFresco.getDocumentiNoLiqAnnoSuccessivo());
			else
				model.setDocumentiNoLiqAnnoSuccessivo(BigDecimal.ZERO);
			if(impegnoFresco.getLiquidatoAnnoSuccessivo() != null)
				model.setLiquidatoAnnoSuccessivo(impegnoFresco.getLiquidatoAnnoSuccessivo());
			else
				model.setLiquidatoAnnoSuccessivo(BigDecimal.ZERO);
			//FIXME Vincenzo: possibile nei submovimenti o refuso?
			model.setNumeroTotaleModifiche(impegnoFresco.getNumeroTotaleModifcheMovimento());
			
			//disabled checkDaMantenere
			BigDecimal residuoAZero = new BigDecimal("0.00");
			if(impegnoFresco.getResiduoDaMantenere().equals(residuoAZero)){
				setDisabledDaManterenere(true);				
			}else{
				setDisabledDaManterenere(false);
			}
		}
		
		//controllo se si tratta di subimpegno
		

		if (getNumeroSubImpegno() != null && !getNumeroSubImpegno().equals("")) {
			if (impegnoFresco.getStatoOperativoMovimentoGestioneSpesa().equals("P")) {
				setAbilitaPropagaDaSub(false);
			} else {
				setAbilitaPropagaDaSub(true);
			}

		}
		//model.setImpegnoInAggiornamento(impegnoFresco);
		// .........era commentato sino a qui!

		// SETTO L'IMPEGNO NEL MODEL:
		if (impegnoFresco != null) {
			model.setImpegnoRicaricatoDopoInsOAgg(impegnoFresco);
		}
		if (model.getImpegnoRicaricatoDopoInsOAgg() != null) {
			model.setImpegnoInAggiornamento(model.getImpegnoRicaricatoDopoInsOAgg());
			caricaDati(true);
			model.setImpegnoRicaricatoDopoInsOAgg(null);
		}

		// APRILE 2016 - ottimizzazioni sub
		List<SubImpegno> elencoSubImpegni = null;
		if (respRk != null) {
			elencoSubImpegni = respRk.getElencoSubImpegniTuttiConSoloGliIds();
		}
		if ((getNumeroSubImpegno() != null && !getNumeroSubImpegno().equals("")) && elencoSubImpegni != null && elencoSubImpegni.size() > 0) {
			SubImpegno sub = new SubImpegno();
			for (int i = 0; i < elencoSubImpegni.size(); i++) {
				if (elencoSubImpegni.get(i).getNumeroBigDecimal().equals(new BigDecimal(getNumeroSubImpegno()))) {
					sub = elencoSubImpegni.get(i);
					break;
				}
			}

			// check stesso provvedimento per i sub
			model.setnSubImpegno(Integer.valueOf(sub.getNumeroBigDecimal().intValue()));

			//13032020: fix devo prendere il provvedimento delle modifiche per il cruscotto quindi inserisco filtro

			String codePdc = CruscottoRorUtilities.codePdc(sub.getCodPdc());
			model.setCodicePdc(codePdc);


			if (sub.getListaModificheMovimentoGestioneSpesa() != null
					&& !sub.getListaModificheMovimentoGestioneSpesa().isEmpty()) {
				List<ModificaMovimentoGestioneSpesa> modifichePerCruscotto = new ArrayList<ModificaMovimentoGestioneSpesa>();
				modifichePerCruscotto = CruscottoRorUtilities.getModifichePerCruscotto(sub.getListaModificheMovimentoGestioneSpesa(),
							modifichePerCruscotto);
			
				if (modifichePerCruscotto != null) {
					modifichePerCruscotto = CruscottoRorUtilities.getModificheStatoValido(modifichePerCruscotto);
				}
				if(modifichePerCruscotto !=null && !modifichePerCruscotto.isEmpty()){
					model.getStep1ModelCache().setProvvedimento(CruscottoRorUtilities
							.mapAttoToProvv(modifichePerCruscotto.get(0).getAttoAmministrativo()));					
				}else{
					model.getStep1ModelCache()
					.setProvvedimento(CruscottoRorUtilities.mapAttoToProvv(sub.getAttoAmministrativo()));
				}
			} else if (sub.getListaModificheMovimentoGestioneSpesa() == null
					|| sub.getListaModificheMovimentoGestioneSpesa().isEmpty()) {
				model.getStep1ModelCache()
						.setProvvedimento(CruscottoRorUtilities.mapAttoToProvv(sub.getAttoAmministrativo()));

			}

		}

		model.getStep1Model().setImportoImpegno(model.getImpegnoInAggiornamento().getImportoAttuale()); 
		model.getStep1Model().setImportoFormattato(
				convertiBigDecimalToImporto(model.getImpegnoInAggiornamento().getImportoAttuale()));
		model.setTotaleSubImpegno(model.getImpegnoInAggiornamento().getTotaleSubImpegniBigDecimal());
		model.setDisponibilitaSubImpegnare(model.getImpegnoInAggiornamento().getDisponibilitaSubimpegnare());

		if (model.getImpegnoInAggiornamento() != null && elencoSubImpegni != null && elencoSubImpegni.size() > 0) {
			model.setListaSubimpegni(elencoSubImpegni);
		} else
			model.setListaSubimpegni(new ArrayList<SubImpegno>());

		// componiamo una lista modifiche comprendente sia quelle del movimento
		// che quelle dei suoi sub:

		// aggiungo quelle del movimento:
		if (model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa() != null
				&& model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa().size() > 0) {
			listaModifiche.addAll(model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa());
		}

		// aggiungo quelle dei sub:
		if (model.getListaSubimpegni() != null && model.getListaSubimpegni().size() > 0) {
			for (SubImpegno sub : model.getListaSubimpegni()) {
				if (sub.getListaModificheMovimentoGestioneSpesa() != null
						&& sub.getListaModificheMovimentoGestioneSpesa().size() > 0) {
					listaModifiche.addAll(sub.getListaModificheMovimentoGestioneSpesa());
				}
			}
		}

		// settiamo le modifiche nel model:
		model.getMovimentoSpesaModel().setListaModifiche(listaModifiche);
		model.getMovimentoSpesaModel().setImpegno(model.getImpegnoInAggiornamento());

		// creo il model cache:
		creaMovGestModelCache();

		// ripulisco i model di step1:
		model.setStep1ModelSubimpegno(new GestisciImpegnoStep1Model());
		model.setStep1ModelSubimpegnoCache(new GestisciImpegnoStep1Model());
		model.setImpegnoRicaricatoDopoInsOAgg(new Impegno());

	}

	// subito dopo, equivale alla prepare della InserisciModifica
	private void inizializzaAggiornaModifiche() {
		// Reimputazione:
		inizializzaReimputazioneInInserimentoNelModel();

		// Carico Lista Tipo Modifiche
		if (abilitaListaROR()) {
			caricaListaMotiviROR();
			model.setGestioneModificaDecentratoEFaseROR(true);
		} else {
			caricaListaMotivi();
			model.setGestioneModificaDecentratoEFaseROR(false);
		}

		if (model.getListaSubimpegni() != null && model.getListaSubimpegni().size() > 0) {

			// ciclo sui sub:
			for (SubImpegno sub : model.getListaSubimpegni()) {
				if (sub.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("D")) {
					if (sub.getNumeroBigDecimal() != null) {
						// aggiungo il sub alla lista
						numeroSubImpegnoList.add(String.valueOf(sub.getNumeroBigDecimal()));
					}
				}

			}

			if (numeroSubImpegnoList.size() > 0) {
				// ci sono sub
				tipoImpegnoModificaImportoList.add("Impegno");
				tipoImpegnoModificaImportoList.add("SubImpegno");

			} else {
				// non ci sono sub
				tipoImpegnoModificaImportoList.add("Impegno");
			}

		} else {
			// non ci sono sub
			tipoImpegnoModificaImportoList.add("Impegno");
		}

		// Calcolo importi
		//SIAC-7819 FL
		if(model.getDisponibilitaImpegnoModifica()==null){
			model.setDisponibilitaImpegnoModifica(BigDecimal.ZERO);
		}
		

		minImportoImpegnoApp = model.getImpegnoInAggiornamento().getDisponibilitaImpegnoModifica().negate();

		//SIAC-7349
		//Per come calcocata questa disponibilità (valore impegnato - liquidazioni)
		//il limite sinistro della singola componente equivale a quello dell'impegno
		//in quanto esso è riferito alla componente stessa
		minImportoImpegnoCompApp = minImportoImpegnoApp;

		if (model.getImpegnoInAggiornamento().getAnnoMovimento() < sessionHandler.getAnnoBilancio()) {
			maxImportoImpegnoApp = new BigDecimal(0);
			//SIAC-7349
			maxImportoImpegnoCompApp = new BigDecimal(0);
		} else {
			// SIAC-580

			if (model.getStep1Model().getAnnoImpegno().intValue() == model.getImpegnoInAggiornamento()
					.getCapitoloUscitaGestione().getAnnoCapitolo().intValue()) {
				// anno corrente
				maxImportoImpegnoApp = model.getImpegnoInAggiornamento().getCapitoloUscitaGestione()
						.getImportiCapitolo().getDisponibilitaImpegnareAnno1();
				//SIAC-7349 fare in modo di ottenere il valore degli importi capitolo della componente
				maxImportoImpegnoCompApp = getDisponibilitaModifica(0);
			} else if (model.getStep1Model().getAnnoImpegno().intValue() == (model.getImpegnoInAggiornamento()
					.getCapitoloUscitaGestione().getAnnoCapitolo().intValue() + 1)) {
				// anno +1
				maxImportoImpegnoApp = model.getImpegnoInAggiornamento().getCapitoloUscitaGestione()
						.getImportiCapitolo().getDisponibilitaImpegnareAnno2();
				//SIAC-7349 fare in modo di ottenere il valore degli importi capitolo della componente
				maxImportoImpegnoCompApp = getDisponibilitaModifica(1);
			} else if (model.getStep1Model().getAnnoImpegno().intValue() == (model.getImpegnoInAggiornamento()
					.getCapitoloUscitaGestione().getAnnoCapitolo().intValue() + 2)) {
				// anno +2
				maxImportoImpegnoApp = model.getImpegnoInAggiornamento().getCapitoloUscitaGestione()
						.getImportiCapitolo().getDisponibilitaImpegnareAnno3();
				//SIAC-7349 fare in modo di ottenere il valore degli importi capitolo della componente
				maxImportoImpegnoCompApp = getDisponibilitaModifica(2);


			} else {

				model.setFlagSuperioreTreAnni(true);
				return;
			}
		}

		if (minImportoImpegnoApp == null) {
			// pongo a zero
			minImportoImpegnoApp = new BigDecimal(0);
		}

		//SIAC-7349
		if(minImportoImpegnoCompApp == null){
					//pongo a zero
			minImportoImpegnoCompApp = new BigDecimal(0);
		}
		if (maxImportoImpegnoApp == null) {
			// pongo a zero
			maxImportoImpegnoApp = new BigDecimal(0);
		}
		//SIAC-7349
		if(maxImportoImpegnoCompApp == null){
					//pongo a zero
			maxImportoImpegnoCompApp = new BigDecimal(0);
		}

		// GESTIONE FLAG SDF:
		if (model.getImpegnoInAggiornamento().isFlagSDF()) {
			maxImportoImpegnoApp = new BigDecimal(0);
		}
		//SIAC-7349 importo a zero il maxImportoImpegnoCompApp come fatto in precedenza, a determinate condizioni
		if(model.getImpegnoInAggiornamento().isFlagSDF()){
			maxImportoImpegnoCompApp = new BigDecimal(0);
		}
		//

		if (maxImportoImpegnoApp.compareTo(new BigDecimal(0)) < 0) {
			maxImportoImpegnoApp = new BigDecimal(0);
		}

		//SIAC-7349 importo a zero il maxImportoImpegnoCompApp come fatto in precedenza, a determinate condizioni
		if(maxImportoImpegnoCompApp.compareTo(new BigDecimal(0)) < 0){
			maxImportoImpegnoCompApp = new BigDecimal(0);
		}
		
		//SIAC-7819 inizio FL
		model.setMinImportoImpComp(convertiBigDecimalToImporto(minImportoImpegnoCompApp));
		model.setMaxImportoImpComp(convertiBigDecimalToImporto(maxImportoImpegnoCompApp));
		//SIAC-7819 fine FL
		
		// Setto il massimo e i minimo nelle variabili finali:
		setMaxImp(convertiBigDecimalToImporto(maxImportoImpegnoApp));
		model.setMaxImpMod(convertiBigDecimalToImporto(maxImportoImpegnoApp));
		setMinImp(convertiBigDecimalToImporto(minImportoImpegnoApp));
		model.setMinImpMod(convertiBigDecimalToImporto(minImportoImpegnoApp));

		setMinImportoCalcolato(convertiBigDecimalToImporto(minImportoImpegnoApp));
		model.setMinImportoCalcolatoMod(convertiBigDecimalToImporto(minImportoImpegnoApp));
		setMaxImportoCalcolato(convertiBigDecimalToImporto(maxImportoImpegnoApp));
		model.setMaxImportoCalcolatoMod(convertiBigDecimalToImporto(maxImportoImpegnoApp));
		
		//SIAC-7551	22/05/2020 GM	
		//"importo massimo differibile" dovrà essere calcolato come "importo massimo da riaccertare" – "importo modifiche"
		//"importo massimo cancellabile" dovrà essere calcolato come "importo massimo da riaccertare" – "importo modifiche"
		BigDecimal massimoDifferibileCancellabile = BigDecimal.ZERO; 
		if(model.getImportoMassimoDaRiaccertare() != null && model.getImportoModifiche() != null && 
			model.getImportoMassimoDaRiaccertare().compareTo(model.getImportoModifiche()) > 0){
			massimoDifferibileCancellabile = (model.getImportoMassimoDaRiaccertare().subtract(model.getImportoModifiche())).abs();
		}
		//SIAC-7349
		model.setImportoMassimoDifferibile(convertiBigDecimalToImporto(massimoDifferibileCancellabile));
		model.setImportoMassimoCancellabile(convertiBigDecimalToImporto(massimoDifferibileCancellabile));
//		VECCHIO CODICE COMMENTATO	
//		if(model.getnSubImpegno()==null){
//			model.setImportoMassimoDifferibile(convertiBigDecimalToImporto(minImportoImpegnoApp.abs()));
//			model.setImportoMassimoCancellabile(convertiBigDecimalToImporto(minImportoImpegnoApp.abs()));
//		}else{
//			model.setImportoMassimoDifferibile(convertiBigDecimalToImporto(minImportoSubApp.abs()));
//			model.setImportoMassimoCancellabile(convertiBigDecimalToImporto(minImportoSubApp.abs()));
//		}
		//FINE SIAC-7551
		
		
		if (StringUtils.isEmpty(getAnniPluriennali())) {// da rivedere
			setAnniPluriennali("1");
		}
		
		
	}

	// Inserimento nuove modifiche -> Cruscotto "INSERISCI"
	public String salva() {
		//SIAC-8269
		clearWarningAndError(true);
		
		//String tipoMotivo = getIdTipoMotivo();
		// clono i dati che stanno per essere cambiati nel model per
		// ripristinarli in caso di salvataggio andato male:
		GestisciModificaMovimentoSpesaModel movimentoSpesaModelPrimaDiSalva = clone(model.getMovimentoSpesaModel());
		GestisciImpegnoStep1Model step1ModelPrimaDiSalva = clone(model.getStep1Model());
		Impegno impegnoInAggiornamentoPrimaDiSalva = clone(model.getImpegnoInAggiornamento());
		SubImpegno subimpegnoimpegnoInAggiornamentoPrimaDiSalva = clone(model.getSubimpegnoInAggiornamento());
		List<SubImpegno> listaSubPrimaDiSalva = clone(model.getListaSubimpegni());
		
		//per non permettere l'inserimento del da mantenere se nella costruzione è stato portato a zero
		BigDecimal importoInizialeImpegno = BigDecimal.ZERO;
		if(numeroSubImpegno != null && !numeroSubImpegno.equals("") ){
			setTipoImpegno("SubImpegno");
			importoInizialeImpegno = model.getSubimpegnoInAggiornamento().getImportoAttuale();
		}else{
			setTipoImpegno("Impegno");
			importoInizialeImpegno = model.getImpegnoInAggiornamento().getImportoAttuale();
		}


		List<ModificaMovimentoGestioneSpesa> modificheList = model.getImpegnoInAggiornamento()
				.getListaModificheMovimentoGestioneSpesa();// dopo va risettato
															// il model
		// errori da mostrare
		List<Errore> erroriSalvataggio = new ArrayList<Errore>();
		modificheList = FinUtility.rimuoviConIdZero(modificheList);
		if (modificheList == null) {
			modificheList = new ArrayList<ModificaMovimentoGestioneSpesa>();
		}

		
		if (model.getStep1Model().getProvvedimento().getAnnoProvvedimento() == null
				&& model.getStep1Model().getProvvedimento().getNumeroProvvedimento() == null) {
			Errore err = new Errore();
			err.setCodice(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("").getCodice());
			err.setDescrizione("Non &grave; stato selezionato nessun provvedimento");
			erroriSalvataggio.add(err);
			

		}
		
		if (model.getStep1ModelCache().getProvvedimento().getStato() != null
				&& !model.getStep1ModelCache().getProvvedimento().getStato().equals(StatoOperativoAtti.DEFINITIVO.getDescrizione())) {
			Errore err = ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("inserimento modifiche", "definitivo");
			erroriSalvataggio.add(err);
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, impegnoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva, subimpegnoimpegnoInAggiornamentoPrimaDiSalva);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
			

		}
		
		if (model.getStep1Model().getProvvedimento().getStato() != null
				&& model.getStep1Model().getProvvedimento().getStato().equals(StatoOperativoAtti.ANNULLATO.getDescrizione())) {
			Errore err = ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("inserimento modifiche", "definitivo o provvisorio");
			erroriSalvataggio.add(err);
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, impegnoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva, subimpegnoimpegnoInAggiornamentoPrimaDiSalva);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
			

		}

		// controllo se nella lista ci sono più reimputazioni per lo stesso anno
		erroriSalvataggio = CruscottoRorUtilities.checkAnniReimputazioneRipetuti(listaReimputazioneTriennio,
				erroriSalvataggio);
		erroriSalvataggio = CruscottoRorUtilities.checkAnnoNonCongruente(listaReimputazioneTriennio, erroriSalvataggio,
				sessionHandler.getBilancio().getAnno());
		
		erroriSalvataggio = CruscottoRorUtilities.checkCampiProvvedimentoVuoti(model.getStep1Model(), erroriSalvataggio);

		// controllo nuovo
		
		if (model.getStep1Model().getNumeroSubImpegno() == null
				|| model.getStep1Model().getNumeroSubImpegno().equals("")) {
			boolean stesso = checkProvvedimentoModificaImportoSoggettoMovimentoGestione(
					model.getStep1ModelCache().getProvvedimento(), model.getStep1Model().getProvvedimento(), "impegno",
					erroriSalvataggio);
		} else {
			boolean stesso = checkProvvedimentoModificaImportoSoggettoMovimentoGestione(
					model.getStep1ModelCache().getProvvedimento(), model.getStep1Model().getProvvedimento(),
					"SubImpegno", erroriSalvataggio);
		}
		
		
		//Controllo se sono stati valorizzati importi e descrizione
		erroriSalvataggio = CruscottoRorUtilities.checkModificaCompleta(erroriSalvataggio, listaReimputazioneTriennio,
				rorCancellazioneInesigibilita, rorCancellazioneInsussistenza, rorDaMantenere, checkDaMantenere);
		if(erroriSalvataggio != null && !erroriSalvataggio.isEmpty()){
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, impegnoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva, subimpegnoimpegnoInAggiornamentoPrimaDiSalva);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;		
		}
		///

		List<ModificaMovimentoGestioneSpesa> spese = costruisciListaMovimentiSpesaSUB(listaReimputazioneTriennio,
				rorCancellazioneInesigibilita, rorCancellazioneInsussistenza, rorDaMantenere, checkDaMantenere);
		
		//posso calcolare il residuo sottraendo al aggiungendo il delta importo impegno
		if(checkDaMantenere !=null && checkDaMantenere.equals(true) && !rorDaMantenere.getDescrizione().equals("")){
			BigDecimal residuoZero = new BigDecimal("0.00");
			BigDecimal deltaImportoImpegno = residuoDaMantenereDopoAggiornamento(importoInizialeImpegno);
			BigDecimal residuoDaMantenere = model.getResiduoEventualeDaMantenere();
			residuoDaMantenere = residuoDaMantenere.add(deltaImportoImpegno);
			if(residuoDaMantenere.compareTo(residuoZero)==0){
				Errore err = new Errore();
				err.setCodice(ErroreFin.WARNING_GENERICO.getErrore("").getCodice());
				err.setCodice(ErroreFin.WARNING_GENERICO.getErrore("Impossibile Inserire la modifica ROR - Da Mantenere: Residuo uguale a zero").getDescrizione());
				erroriSalvataggio.add(err);
				
			}
			if(!erroriSalvataggio.isEmpty()){
				ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, impegnoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva, subimpegnoimpegnoInAggiornamentoPrimaDiSalva);
				sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
						new ArrayList<Errore>(model.getErrori()));
				addErrori(erroriSalvataggio);
				setErroriInSessionePerActionSuccessiva();
				return INPUT;
				
			}
		}
		
		
		
		//Se il propaga è selezionato, devo controllare le spese di tipo SIM e non tutte quelle presenti in SPESE
		List<ModificaMovimentoGestioneSpesa> speseDaValidare = new ArrayList<ModificaMovimentoGestioneSpesa>();
		if(isPropagaSelected()){
			for (SubImpegno sub : model.getListaSubimpegni()) {
				// TODO subSelected corrrisponde al subimopegno caricato in mappa
				if (String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)) {
					speseDaValidare = sub.getListaModificheMovimentoGestioneSpesa();
					break;
				}
				
			}
			speseDaValidare =  CruscottoRorUtilities.speseDaValidarePerSubPropaga(speseDaValidare);			
			if(speseDaValidare != null && speseDaValidare.isEmpty()){
				Errore err = new Errore();
				err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
				err.setDescrizione("Salvataggio non effettuato: non &egrave; stata inserita nessuna modifica");
				erroriSalvataggio.add(err);
				ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, impegnoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva, subimpegnoimpegnoInAggiornamentoPrimaDiSalva);
				sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
						new ArrayList<Errore>(model.getErrori()));
				addErrori(erroriSalvataggio);
				setErroriInSessionePerActionSuccessiva();
				return INPUT;
				
				
			}
			erroriSalvataggio = checkModificheValide(speseDaValidare, erroriSalvataggio, false, null, null,	null, null);			
			
		}else{
			//questa fix viene fatta in quanto le modifiche di tipo SIM venivano inserite anche alle modifiche dell'impegno Padre.
			//se lavoro con il subimpegno devo validare le modifiche associate al sub in gestione
			//15/03/2020
			if(model.getnSubImpegno()!=null){
				for (SubImpegno sub : model.getListaSubimpegni()) {
					// TODO subSelected corrrisponde al subimopegno caricato in
					// mappa
					if (String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)) {
						speseDaValidare = sub.getListaModificheMovimentoGestioneSpesa();
						break;
					}
					
				}
				speseDaValidare =  CruscottoRorUtilities.speseDaValidarePerSubPropaga(speseDaValidare);			
				if(speseDaValidare != null && speseDaValidare.isEmpty()){
					Errore err = new Errore();
					err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
					err.setDescrizione("Salvataggio non effettuato: non &egrave; stata inserita nessuna modifica");
					erroriSalvataggio.add(err);
					ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, impegnoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva, subimpegnoimpegnoInAggiornamentoPrimaDiSalva);
					sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
							new ArrayList<Errore>(model.getErrori()));
					addErrori(erroriSalvataggio);
					setErroriInSessionePerActionSuccessiva();
					return INPUT;
					
					
				}
				erroriSalvataggio = checkModificheValide(speseDaValidare, erroriSalvataggio, false, null, null,	null, null);
				
				
			}else{
				
				if(spese != null && spese.isEmpty()){
					Errore err = new Errore();
					err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
					err.setDescrizione("Salvataggio non effettuato: non &egrave; stata inserita nessuna modifica");
					erroriSalvataggio.add(err);
					ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, impegnoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva, subimpegnoimpegnoInAggiornamentoPrimaDiSalva);
					sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
							new ArrayList<Errore>(model.getErrori()));
					addErrori(erroriSalvataggio);
					setErroriInSessionePerActionSuccessiva();
					return INPUT;				
				}
				
				erroriSalvataggio = checkModificheValide(spese, erroriSalvataggio, false, null, null,	null, null);
			}
			
		}
		modificheList.addAll(spese);


		if (erroriSalvataggio != null && !erroriSalvataggio.isEmpty()) {
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, impegnoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva, subimpegnoimpegnoInAggiornamentoPrimaDiSalva);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}

		model.getImpegnoInAggiornamento().setListaModificheMovimentoGestioneSpesa(modificheList);

		

		// Non veniva popolato il componente con i dati della trans. elemen.
		popolaStrutturaTransazioneElementare();
		//SIAC-8271
		if(numeroSubImpegno != null && StringUtils.isNotEmpty(numeroSubImpegno)) {
			impostaImportoAttualeSuSub();
		}
		
		AggiornaImpegno requestAggiorna = convertiModelPerChiamataServizioInserisciAggiornaModifiche(false);
	
		
		 

		if (requestAggiorna.getImpegno().getProgetto() != null) {
			Progetto progetto = requestAggiorna.getImpegno().getProgetto();

			progetto.setTipoProgetto(TipoProgetto.GESTIONE);
			progetto.setBilancio(sessionHandler.getBilancio());

			requestAggiorna.getImpegno().setIdCronoprogramma(model.getStep1Model().getIdCronoprogramma());
			requestAggiorna.getImpegno().setIdSpesaCronoprogramma(model.getStep1Model().getIdSpesaCronoprogramma());
		}
		//SIC-8675
		AnnullaAggiornaMovimento req = creaAnnullaAggiornaMovimentoRequestBase();
		req.setFlagAnnullaConsentito(false);
		req.setFlagAggiornaConsentito(true);
		req.setAggiornaImpegno(requestAggiorna);
		
		AnnullaAggiornaMovimentoResponse response = movimentoGestioneFinService.annullaAggiornaImpegno(req);
//		AggiornaImpegnoResponse response = movimentoGestioneFinService.aggiornaImpegno(requestAggiorna);

		if (response.isFallimento()) {
			//SIAC-8675
			List<Errore> errs = CruscottoRorUtilities.getErroreDaWrapper(response.getDescrizioneErrori(), response.getErrori());
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiSalva, step1ModelPrimaDiSalva, impegnoInAggiornamentoPrimaDiSalva, listaSubPrimaDiSalva, subimpegnoimpegnoInAggiornamentoPrimaDiSalva);
			addErrori("Errore al salvataggio delle modifiche", errs);
			return INPUT;
		}

		model.setImpegnoInAggiornamento(response.getImpegno());


		impostaInformazioneSuccessoAzioneInSessionePerRedirezione(
				ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " "
						+ ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());

		return SUCCESS_SALVA;

	}

	

	private void impostaInformazioneSuccessoAzioneInSessionePerRedirezione(String optional) {
		sessionHandler.setParametro(FinSessionParameter.INFORMAZIONI_AZIONE_PRECEDENTE, optional);
	}
	
	private void impostaMessaggioAzioneInSessionePerRedirezione(String optional) {
		sessionHandler.setParametro(FinSessionParameter.MESSAGGI_AZIONE_PRECEDENTE, optional);
	}

	/**
	 * Controlla se in sessione si abbiano informazioni relative ad un eventuale
	 * successo di un'azione precedente. <br>
	 * In tal caso, pulisce la sessione e comunica all'utente tale successo.
	 */	
	protected void leggiEventualiInformazioniAzionePrecedente() {
		// Recupera le informazioni da sessione
		String successo = sessionHandler.getParametro(FinSessionParameter.INFORMAZIONI_AZIONE_PRECEDENTE);
		if (successo != null) {
			// Pulisco la sessione
			sessionHandler.setParametro(FinSessionParameter.INFORMAZIONI_AZIONE_PRECEDENTE, null);
			addActionMessage(successo);

		}

	}
	
	protected void leggiEventualiMessaggiAzionePrecedente() {
		// Recupera le informazioni da sessione
		String successo = sessionHandler.getParametro(FinSessionParameter.MESSAGGI_AZIONE_PRECEDENTE);
		if (successo != null) {
			// Pulisco la sessione
			sessionHandler.setParametro(FinSessionParameter.MESSAGGI_AZIONE_PRECEDENTE, null);
			addActionWarning(successo);

		}

	}
	
	
	
	//fase di aggiornamento modifiche
	private List<Errore> checkModificheValide(List<ModificaMovimentoGestioneSpesa> spese,
			List<Errore> erroriSalvataggio, boolean aggiornamento, BigDecimal importoAnnullatoPrimaDiAggiornamentoReimputazione, 
			BigDecimal importoAnnullatoPrimaDiAggiornamentoCancellazione, 
			BigDecimal importoAnnullatoReimputazionePerRicreare, 
			BigDecimal importoAnnullatoCancellazionePerRicreare) {
		
		//Per effettuare il controllo degli importi, devo passare come lista le spese che non contengono RORM e una che contiene solo i rorm Dunque devo creare due arraylist di appoggio
		List<ModificaMovimentoGestioneSpesa> speseSenzaRorm = CruscottoRorUtilities.getSpeseRor(spese, "");
		List<ModificaMovimentoGestioneSpesa> speseRorm =  CruscottoRorUtilities.getSpeseRor(spese, "RORM");
		
		

		for (int i = 0; i < speseSenzaRorm.size(); i++) {
			ModificaMovimentoGestioneSpesa spesaSelezionata = speseSenzaRorm.get(i);
			if(spesaSelezionata.getStatoOperativoModificaMovimentoGestione().equals(StatoOperativoModificaMovimentoGestione.VALIDO)){
					
				if (tipoImpegno.equals("Impegno")) {
					erroriSalvataggio = controlliReimputazioneInInserimentoEMotivoRIAC(erroriSalvataggio, tipoImpegno, null,
							spesaSelezionata);
						
				} else if (tipoImpegno.equals("SubImpegno") && !isPropagaSelected()) {
					erroriSalvataggio = controlliReimputazioneInInserimentoEMotivoRIAC(erroriSalvataggio, tipoImpegno,
							"No Propaga", spesaSelezionata);
				} else if (tipoImpegno.equals("SubImpegno") && isPropagaSelected()) {
					erroriSalvataggio = controlliReimputazioneInInserimentoEMotivoRIAC(erroriSalvataggio, tipoImpegno,
							"Modifica Anche Impegno", spesaSelezionata);

				}
			}
		}

		erroriSalvataggio = controlloGestisciImpegnoDecentratoPerModifica(erroriSalvataggio);
			
		if(!erroriSalvataggio.isEmpty()){
			return erroriSalvataggio;
		}
			
			
			
			//qui controllo importi
		if (tipoImpegno.equals("Impegno")) {
			BigDecimal maxDiff = convertiImportoToBigDecimal(model.getImportoMassimoDifferibile());
			BigDecimal maxCanc = convertiImportoToBigDecimal(model.getImportoMassimoCancellabile());
			
			//BigDecimal limiteDestro = convertiImportoToBigDecimal(model.getMaxImportoCalcolatoMod());
			
			//SIAC-7349 - MR - START - 29/05/2020 Il limite destro, diventa la disponibilita ad impegnare della componente
			//SIAC-7819 FL 
			BigDecimal limiteDestro = model.getMaxImportoImpComp()!= null ? convertiImportoToBigDecimal(model.getMaxImportoImpComp()) : BigDecimal.ZERO   ;
			 
			if(aggiornamento==true){
				erroriSalvataggio = CruscottoRorUtilities.checkImportiReimpECancell(erroriSalvataggio, speseSenzaRorm, maxDiff, maxCanc, limiteDestro, 
						importoAnnullatoPrimaDiAggiornamentoReimputazione, 
						importoAnnullatoPrimaDiAggiornamentoCancellazione, 
						importoAnnullatoReimputazionePerRicreare, 
						importoAnnullatoCancellazionePerRicreare);
			}else{
				erroriSalvataggio = CruscottoRorUtilities.checkImportiReimpECancell(erroriSalvataggio, speseSenzaRorm, maxDiff, maxCanc,
				limiteDestro, null, null, null, null);				
			}
				
		} else if (tipoImpegno.equals("SubImpegno") && !isPropagaSelected()) {
			BigDecimal maxDiff = convertiImportoToBigDecimal(model.getImportoMassimoDifferibile());
			BigDecimal maxCanc = convertiImportoToBigDecimal(model.getImportoMassimoCancellabile());
			
			//SIAC-7349 - MR - START - 29/05/2020 Il limite destro, diventa la disponibilita ad impegnare della componente
			BigDecimal limiteDestro = convertiImportoToBigDecimal(model.getMaxImportoImpComp());
			//BigDecimal limiteDestro = convertiImportoToBigDecimal(model.getMaxImportoSubCalcolatoMod());
			
			if(aggiornamento==true){
				erroriSalvataggio = CruscottoRorUtilities.checkImportiReimpECancell(erroriSalvataggio, speseSenzaRorm, maxDiff, maxCanc, limiteDestro,
						importoAnnullatoPrimaDiAggiornamentoReimputazione, 
						importoAnnullatoPrimaDiAggiornamentoCancellazione, 
						importoAnnullatoReimputazionePerRicreare, 
						importoAnnullatoCancellazionePerRicreare);
			}else{
				erroriSalvataggio = CruscottoRorUtilities.checkImportiReimpECancell(erroriSalvataggio, speseSenzaRorm, maxDiff, maxCanc, limiteDestro,  null, null, null, null);				
			}
		} else if (tipoImpegno.equals("SubImpegno") && isPropagaSelected()) {
			
			//qui gli importi vanno gestiti solo per il subimpegno, in quanto per l'impegno padre non sono previste modifiche	
			//SIAC-7349 - MR - START - 29/05/2020 Il limite destro, diventa la disponibilita ad impegnare della componente
			BigDecimal limiteDestroPadre = convertiImportoToBigDecimal(model.getMaxImportoImpComp());
			//BigDecimal limiteDestroPadre = convertiImportoToBigDecimal(model.getMaxImportoCalcolatoMod());
			BigDecimal limiteDestroSub = convertiImportoToBigDecimal(model.getMaxImportoSubCalcolatoMod());
			
			BigDecimal maxDiff = convertiImportoToBigDecimal(model.getImportoMassimoDifferibile());//questo è riferito al subimpegno
			BigDecimal maxCanc = convertiImportoToBigDecimal(model.getImportoMassimoCancellabile());//questo è riferito al subimpegno
			BigDecimal maxDiffPadre = convertiImportoToBigDecimal(model.getMinImpMod());//questo è riferito al subimpegno
			BigDecimal maxCancPadre = convertiImportoToBigDecimal(model.getMinImpMod());//questo è riferito al subimpegno
				//qui eseguo i controlli anche sull'impegno padre
			if(aggiornamento==true){
				erroriSalvataggio = CruscottoRorUtilities.checkImportiReimpECancellSubTest(erroriSalvataggio, speseSenzaRorm, maxDiff, maxCanc, maxDiffPadre, maxCancPadre, 
						importoAnnullatoPrimaDiAggiornamentoReimputazione, 
						importoAnnullatoPrimaDiAggiornamentoCancellazione, 
						importoAnnullatoReimputazionePerRicreare, 
						importoAnnullatoCancellazionePerRicreare, limiteDestroPadre, limiteDestroSub);
				
			}else{
				erroriSalvataggio = CruscottoRorUtilities.checkImportiReimpECancellSubTest(erroriSalvataggio, speseSenzaRorm, maxDiff, maxCanc, maxDiffPadre, maxCancPadre, 
						null, null, null, null, limiteDestroPadre, limiteDestroSub);				
			}
		
				erroriSalvataggio = controllaRorMantenerePadre(erroriSalvataggio, speseRorm);
	
		}
		
			
			
		return erroriSalvataggio;

	}
	
	
	
	

	private List<Errore> controllaRorMantenerePadre(List<Errore> erroriSalvataggio, List<ModificaMovimentoGestioneSpesa> speseRorm) {
		if(speseRorm.isEmpty()){
			return erroriSalvataggio;
		}
		//List<SubImpegno> list = sessionHandler.getParametro(FinSessionParameter.RISULTATI_RICERCA_IMPEGNI);
		//Integer numeroImpegnoPadre = model.getNumeroImpegno();
		
		//issue-2
		//Impegno impegno = CruscottoRorUtilities.getImpegnoByNumero(list, numeroImpegnoPadre);
		Impegno impegno = model.getImpegnoInAggiornamento();
		
		if(impegno.getResiduoDaMantenere().equals(BigDecimal.ZERO)){
			Errore err = new Errore();
			err.setCodice("FIN_ERR_0040");
			err.setDescrizione(
					"Non &egrave; possibile propagare la modifica ROR - Da Mantenere all'impegno padre. Residuo eventuale da mantenere uguale a zero");
			erroriSalvataggio.add(err);
			
		}

		return erroriSalvataggio;
	
	}

	// metodo di aggiungi/rimuovi anno reimputazione
	public String aggiungiAnnoReimputazione() {
		//Integer anno = sessionHandler.getAnnoBilancio();

		if (isInInserimento()) {
			int numeroAnniSuccessivi = listaReimputazioneTriennio.size();
			GestioneCruscottoModel modifica = new GestioneCruscottoModel();
			modifica.setIndex(numeroAnniSuccessivi);
			listaReimputazioneTriennio.add(modifica);
		} else {
			ModificaMovimentoGestioneSpesa e = new ModificaMovimentoGestioneSpesa();
			e.setReimputazione(true);
			e.setTipoModificaMovimentoGestione("REIMP");
			e.setReimputazione(true);
			getListaModificheRor().add(e);
			//SIAC-7349 Inizio  SR190 CM 20/05/2020
			listaModificheSpeseCollegata.add(e);	
			//SIAC-7349 Inizio  SR190 CM 20/05/2020
		}

		return SUCCESS;
	}

	public String rimuoviAnnoReimputazione() {
		//Integer anno = sessionHandler.getAnnoBilancio();
		Integer indice = getIndiceRimozioneAnno();

		for (int j = 0; j < listaReimputazioneTriennio.size(); j++) {
			if (listaReimputazioneTriennio.get(j).getIndex().equals(indice)) {
				listaReimputazioneTriennio.remove(listaReimputazioneTriennio.get(j));
			}
		}
		return SUCCESS;
	}

	// costruzione lista di modifica spesa
	private List<ModificaMovimentoGestioneSpesa> costruisciListaMovimentiSpesaSUB(
			List<GestioneCruscottoModel> listaReimputazioneTriennio,
			GestioneCruscottoModel rorCancellazioneInesigibilita, GestioneCruscottoModel rorCancellazioneInsussistenza,
			GestioneCruscottoModel rorDaMantenere, Boolean isDaMantenere) {

		BigDecimal importoZero = new BigDecimal("0.00");
		List<ModificaMovimentoGestioneSpesa> listaToReturn = new ArrayList<ModificaMovimentoGestioneSpesa>();
		String tipoMovimento = "";

		if (model.getStep1Model().getNumeroSubImpegno() != null) {
			setSubSelected(model.getStep1Model().getNumeroSubImpegno().toString());
			setTipoImpegno("SubImpegno");
		}else{
			setTipoImpegno("Impegno");
		}

		if (tipoImpegno.equalsIgnoreCase("Impegno")) {

			tipoMovimento = "IMP";// per impegni
			for (GestioneCruscottoModel reimp : listaReimputazioneTriennio) {
				ModificaMovimentoGestioneSpesa spesa = costruisciSingolaSpesaSUB(reimp, "REIMP", tipoMovimento);
				if (spesa.getImportoOld().compareTo(importoZero) != 0 && !spesa.getDescrizione().equals("")) {
					listaToReturn.add(spesa);
				}
			}

			ModificaMovimentoGestioneSpesa spesaIneRor = costruisciSingolaSpesaSUB(rorCancellazioneInesigibilita,
					"INEROR", tipoMovimento);

			if (spesaIneRor.getImportoOld().compareTo(importoZero) != 0 && !spesaIneRor.getDescrizione().equals("")) {
				listaToReturn.add(spesaIneRor);
			}
			ModificaMovimentoGestioneSpesa spesaInsRor = costruisciSingolaSpesaSUB(rorCancellazioneInsussistenza,
					"INSROR", tipoMovimento);
			if (spesaInsRor.getImportoOld().compareTo(importoZero) != 0 && !spesaInsRor.getDescrizione().equals("")) {
				listaToReturn.add(spesaInsRor);
			}
			ModificaMovimentoGestioneSpesa spesaManRor = costruisciSingolaSpesaSUB(rorDaMantenere, "RORM",
					tipoMovimento);
			if ((isDaMantenere != null && isDaMantenere)
					&& (spesaManRor.getDescrizione() != null && !spesaManRor.getDescrizione().equals(""))) {
				listaToReturn.add(spesaManRor);
			}

		} else if (tipoImpegno.equals("SubImpegno") && !isPropagaSelected()) {

			tipoMovimento = "SIM";
			//lista per i sub 15/03/2020
			List<ModificaMovimentoGestioneSpesa> listaToReturnSub = new ArrayList<ModificaMovimentoGestioneSpesa>();
			for (SubImpegno sub : model.getListaSubimpegni()) {
				// TODO subSelected corrrisponde al subimopegno caricato in
				// mappa
				if (String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)) {
					listaToReturnSub = sub.getListaModificheMovimentoGestioneSpesa();
				}
			}
			listaToReturnSub = FinUtility.rimuoviConIdZero(listaToReturn);
			//

			if (listaToReturnSub == null) {
				listaToReturnSub = new ArrayList<ModificaMovimentoGestioneSpesa>();
			} else {
				//listaToReturn = CruscottoRorUtilities.getModificheStatoValido(listaToReturn); //ManuelL. 18/02 forse da eliminare per avere tutte le modifiche
			}
			for (GestioneCruscottoModel reimp : listaReimputazioneTriennio) {
				ModificaMovimentoGestioneSpesa spesa = costruisciSingolaSpesaSUB(reimp, "REIMP", tipoMovimento);
				if (spesa.getImportoOld().compareTo(importoZero) != 0 && !spesa.getDescrizione().equals("")) {
					listaToReturnSub.add(spesa);
				}
			}

			ModificaMovimentoGestioneSpesa spesaIneRor = costruisciSingolaSpesaSUB(rorCancellazioneInesigibilita,
					"INEROR", tipoMovimento);
			if (spesaIneRor.getImportoOld().compareTo(importoZero) != 0 && !spesaIneRor.getDescrizione().equals("")) {
				listaToReturnSub.add(spesaIneRor);
			}
			ModificaMovimentoGestioneSpesa spesaInsRor = costruisciSingolaSpesaSUB(rorCancellazioneInsussistenza,
					"INSROR", tipoMovimento);
			if (spesaInsRor.getImportoOld().compareTo(importoZero) != 0 && !spesaInsRor.getDescrizione().equals("")) {
				listaToReturnSub.add(spesaInsRor);
			}
			ModificaMovimentoGestioneSpesa spesaManRor = costruisciSingolaSpesaSUB(rorDaMantenere, "RORM",
					tipoMovimento);
			if ((isDaMantenere != null && isDaMantenere)
					&& (spesaManRor.getDescrizione() != null || !spesaManRor.getDescrizione().equals(""))) {
				listaToReturnSub.add(spesaManRor);
			}

			List<SubImpegno> nuovaSubImpegnoList = new ArrayList<SubImpegno>();

			for (SubImpegno sub : model.getListaSubimpegni()) {
				// TODO subSelected corrrisponde al subimopegno caricato in
				// mappa
				if (String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)) {
					sub.setListaModificheMovimentoGestioneSpesa(listaToReturnSub);
				}
				nuovaSubImpegnoList.add(sub);
			}
			model.setListaSubimpegni(nuovaSubImpegnoList);
			//fix numero modifiche

		} else if (tipoImpegno.equals("SubImpegno") && isPropagaSelected()) { // abbina.equalsIgnoreCase("Modifica
																				// Anche
																				// Impegno")
			// CARICO I DATI ANCHE PER L'IMPEGNO
			listaToReturn = model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa();

			if (listaToReturn == null) {
				listaToReturn = new ArrayList<ModificaMovimentoGestioneSpesa>();
			
			} else {
				//listaToReturn = CruscottoRorUtilities.getModificheStatoValido(listaToReturn);
			}
			listaToReturn = FinUtility.rimuoviConIdZero(listaToReturn);
			if (listaToReturn == null) {
				listaToReturn = new ArrayList<ModificaMovimentoGestioneSpesa>();
			}

			tipoMovimento = "IMP";
			for (GestioneCruscottoModel reimp : listaReimputazioneTriennio) {
				ModificaMovimentoGestioneSpesa spesa = costruisciSingolaSpesaSUB(reimp, "REIMP", tipoMovimento);
				if (spesa.getImportoOld().compareTo(importoZero) != 0 && !spesa.getDescrizione().equals("")) {
					listaToReturn.add(spesa);
				}
			}

			ModificaMovimentoGestioneSpesa spesaIneRor = costruisciSingolaSpesaSUB(rorCancellazioneInesigibilita,
					"INEROR", tipoMovimento);
			if (spesaIneRor.getImportoOld().compareTo(importoZero) != 0 && !spesaIneRor.getDescrizione().equals("")) {
				listaToReturn.add(spesaIneRor);
			}
			ModificaMovimentoGestioneSpesa spesaInsRor = costruisciSingolaSpesaSUB(rorCancellazioneInsussistenza,
					"INSROR", tipoMovimento);
			if (spesaInsRor.getImportoOld().compareTo(importoZero) != 0 && !spesaInsRor.getDescrizione().equals("")) {
				listaToReturn.add(spesaInsRor);
			}
			ModificaMovimentoGestioneSpesa spesaManRor = costruisciSingolaSpesaSUB(rorDaMantenere, "RORM",
					tipoMovimento);
			if ((isDaMantenere != null && isDaMantenere)
					&& (spesaManRor.getDescrizione() != null || !spesaManRor.getDescrizione().equals(""))) {
				listaToReturn.add(spesaManRor);
			}

			// Subimpegno
			List<ModificaMovimentoGestioneSpesa> modificheSubList = new ArrayList<ModificaMovimentoGestioneSpesa>();

			for (SubImpegno sub : model.getListaSubimpegni()) {
				// TODO subSelected corrrisponde al subimopegno caricato in
				// mappa
				if (String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)) {
					modificheSubList = sub.getListaModificheMovimentoGestioneSpesa();
				}
			}
			if (modificheSubList == null) {
				modificheSubList = new ArrayList<ModificaMovimentoGestioneSpesa>();
			} else {
				//modificheSubList = CruscottoRorUtilities.getModificheStatoValido(modificheSubList); //Forse da eliminare per avere tutte le modifiche
			}
			tipoMovimento = "SIM";
			for (GestioneCruscottoModel reimp : listaReimputazioneTriennio) {
				ModificaMovimentoGestioneSpesa spesa = costruisciSingolaSpesaSUB(reimp, "REIMP", tipoMovimento);
				if (spesa.getImportoOld().compareTo(importoZero) != 0 && !spesa.getDescrizione().equals("")) {
					modificheSubList.add(spesa);
				}
			}

			ModificaMovimentoGestioneSpesa spesaIneRorSub = costruisciSingolaSpesaSUB(rorCancellazioneInesigibilita,
					"INEROR", tipoMovimento);
			if (spesaIneRorSub.getImportoOld().compareTo(importoZero) != 0
					&& !spesaIneRorSub.getDescrizione().equals("")) {
				modificheSubList.add(spesaIneRorSub);
			}
			ModificaMovimentoGestioneSpesa spesaInsRorSub = costruisciSingolaSpesaSUB(rorCancellazioneInsussistenza,
					"INSROR", tipoMovimento);
			if (spesaInsRorSub.getImportoOld().compareTo(importoZero) != 0
					&& !spesaInsRorSub.getDescrizione().equals("")) {
				modificheSubList.add(spesaInsRorSub);
			}
			ModificaMovimentoGestioneSpesa spesaManRorSub = costruisciSingolaSpesaSUB(rorDaMantenere, "RORM",
					tipoMovimento);
			if ((isDaMantenere != null && isDaMantenere)
					&& (spesaManRor.getDescrizione() != null || !spesaManRor.getDescrizione().equals(""))) {
				modificheSubList.add(spesaManRorSub);
			}

			List<SubImpegno> nuovaSubImpegnoList = new ArrayList<SubImpegno>();
			for (SubImpegno sub : model.getListaSubimpegni()) {
				// TODO subSelected corrrisponde al subimopegno caricato in
				// mappa
				if (String.valueOf(sub.getNumeroBigDecimal()).equalsIgnoreCase(subSelected)) {
					sub.setListaModificheMovimentoGestioneSpesa(modificheSubList);
					
				}
				nuovaSubImpegnoList.add(sub);
			}
			// listaToReturn.addAll(modificheSubList);

			model.setListaSubimpegni(nuovaSubImpegnoList);

		}

		return listaToReturn;
	}

	private ModificaMovimentoGestioneSpesa costruisciSingolaSpesaSUB(GestioneCruscottoModel modificaDiSpesa,
			String tipoSpesa, String tipoMovimento) {
		ModificaMovimentoGestioneSpesa spesa = new ModificaMovimentoGestioneSpesa();

		String importoInInput = "";

		if (tipoSpesa.equals("RORM")) {
			importoInInput = "0.00"; // qui va messo l'intero import FIXME
		} else {

			importoInInput = modificaDiSpesa.getImporto();
		}
		if (importoInInput == null || importoInInput.equals("")) {
			importoInInput = "0.00";
		}

		BigDecimal importoDiInput = convertiImportoToBigDecimal(importoInInput);// da
		//FIX per importi in cancellazione da negare
		if(!tipoSpesa.equals("RORM") && importoDiInput.compareTo(BigDecimal.ZERO)>0){
			importoDiInput = importoDiInput.negate();
		}																		// controllare
																				// per
																				// mettere
																				// modificaDiSpesa.getImporto()
		BigDecimal importoAttuale = BigDecimal.ZERO;
		BigDecimal importoCambiato  = BigDecimal.ZERO;
		
		//FIXME 00.45 18/02/2020
		if(tipoMovimento.equals("SIM")){
			
			importoAttuale = model.getSubimpegnoInAggiornamento().getImportoAttuale();			
			importoCambiato = importoAttuale.add(importoDiInput);
			model.getSubimpegnoInAggiornamento().setImportoAttuale(importoCambiato);
			
			
		}else{
			
			importoAttuale = model.getImpegnoInAggiornamento().getImportoAttuale();
			
			
			importoCambiato = importoAttuale.add(importoDiInput);
			//SIAC-7963 la modifica era gia stata effettuata con la SIAC-7504
			////SIAC-7534  
			model.getImpegnoInAggiornamento().setImportoAttuale(importoCambiato);
			//model.getMovimentoSpesaModel().getImpegno().setImportoAttuale(importoCambiato);
			
		}// //FIXME
		

		spesa.setUid(0);
		spesa.setImportoOld(importoDiInput); //qui forse va inserito l'importo negato
		spesa.setImportoNew(importoCambiato);
		// SIAC-7504
		if(tipoMovimento.equals("IMP")){
			model.getMovimentoSpesaModel().getImpegno().setImportoAttuale(importoCambiato);
			model.getStep1Model().setImportoFormattato(convertiBigDecimalToImporto(importoCambiato));
			model.getStep1Model().setImportoImpegno(importoCambiato);
		}
		spesa.setAnnoMovimento(model.getMovimentoSpesaModel().getImpegno().getAnnoMovimento());



		spesa = settaDatiProvvDalModel(spesa, model.getStep1Model().getProvvedimento());

		spesa.setTipoModificaMovimentoGestione(modificaDiSpesa.getIdMotivo());
		if (getDescrizione() != null) {
			spesa.setDescrizione(getDescrizione().toUpperCase());
		}
		spesa.setTipoMovimento(tipoMovimento);

		settaDatiReimputazioneDalModel(spesa);


		spesa.setTipoModificaMovimentoGestione(tipoSpesa);
		spesa.setStatoOperativoModificaMovimentoGestione(StatoOperativoModificaMovimentoGestione.VALIDO);
		String descrizione = null;

		if (modificaDiSpesa.getDescrizione() != null && !modificaDiSpesa.getDescrizione().equals("")) {
			descrizione = CruscottoRorUtilities.troncaDescrizione(modificaDiSpesa.getDescrizione());
		} else {
			descrizione = "";
		}

		spesa.setDescrizione(descrizione);

		spesa.setTipoMovimento(tipoMovimento); 

		if (tipoSpesa.equals("REIMP")) {
			spesa.setReimputazione(true);
			spesa.setAnnoReimputazione(modificaDiSpesa.getAnno());
		}
		return spesa;
	}

	// Metodo per aggiornare le modifiche da cruscotto
	public String aggiornaModifiche() {
		//SIAC-8269
		clearWarningAndError(true);
		AnnullaAggiornaMovimento req = creaAnnullaAggiornaMovimentoRequestBase();
		
		GestisciModificaMovimentoSpesaModel movimentoSpesaModelPrimaDiAggiorna = clone(model.getMovimentoSpesaModel());
		GestisciImpegnoStep1Model step1ModelPrimaDiAggiorna = clone(model.getStep1Model());
		Impegno impegnoInAggiornamentoPrimaDiAggiorna = clone(model.getImpegnoInAggiornamento());
		List<SubImpegno> listaSubPrimaDiAggiorna = clone(model.getListaSubimpegni());
		SubImpegno subPrimaDiAggiorna = clone(model.getSubimpegnoInAggiornamento());
		List<ModificaMovimentoGestioneSpesa> modificheNuoveDaAggiungere = new ArrayList<ModificaMovimentoGestioneSpesa>();// quelle nuove
		List<ModificaMovimentoGestioneSpesa> modificheDaAggiornare = new ArrayList<ModificaMovimentoGestioneSpesa>(); //quelle esistenti da aggiornare
		List<ModificaMovimentoGestioneSpesa> modifichePreesistentiAggiornate = new ArrayList<ModificaMovimentoGestioneSpesa>(); //quelle modificate
		List<ModificaMovimentoGestioneSpesa> listaModificheOld = sessionHandler //quelle ottenute dal BE
				.getParametro(FinSessionParameter.LISTA_MODIFICHE_PRIMA_AGGIORNAMENTO);
		setModificheDaPropagarePieno(false);
		
		BigDecimal importoInizialeImpegno = BigDecimal.ZERO;
		//7469 prendo ilo stato del provvedimento dell'impegno o sub in aggiornamento
		StatoOperativoAtti statoAttiImpegno;
		if(numeroSubImpegno != null && !numeroSubImpegno.equals("")){
			setTipoImpegno("SubImpegno");
			importoInizialeImpegno = model.getSubimpegnoInAggiornamento().getImportoAttuale();
			statoAttiImpegno = subPrimaDiAggiorna.getAttoAmministrativo()==null ? null : subPrimaDiAggiorna.getAttoAmministrativo().getStatoOperativoAtti();
		}else{
			setTipoImpegno("Impegno");
			importoInizialeImpegno = model.getImpegnoInAggiornamento().getImportoAttuale();
			statoAttiImpegno = impegnoInAggiornamentoPrimaDiAggiorna.getAttoAmministrativo()==null ? null : impegnoInAggiornamentoPrimaDiAggiorna.getAttoAmministrativo().getStatoOperativoAtti();
		}
		
		List<Errore> erroriSalvataggio = new ArrayList<Errore>();
		
		
		
		if (model.getStep1Model().getProvvedimento().getAnnoProvvedimento() == null
				&& model.getStep1Model().getProvvedimento().getNumeroProvvedimento() == null) {
			Errore err = new Errore();
			err.setCodice(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("").getCodice());
			err.setDescrizione("Non è stato selezionato nessun provvedimento");
			erroriSalvataggio.add(err);

		}
		
		//FIX provvedimento provvisorio
		//SIAC-7469 il provvedimento per le modifiche può essere anche provvisorio
		if (model.getStep1Model().getProvvedimento().getStato() != null
				&& model.getStep1Model().getProvvedimento().getStato().equals(StatoOperativoAtti.ANNULLATO.getDescrizione())) {
			Errore err = ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("inserimento modifiche", "definitivo o provvisorio");
			erroriSalvataggio.add(err);
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, impegnoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;

		}
		//SIAC-7469 il provvedimento deve essere definitivo quello dell'impegno
		
		if (statoAttiImpegno == null || !statoAttiImpegno.getDescrizione().equals(StatoOperativoAtti.DEFINITIVO.getDescrizione())){
			Errore err = ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("inserimento modifiche", "definitivo");
			erroriSalvataggio.add(err);
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, impegnoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;

		}
		
		
		
		boolean stessoProvvedimento = stessoProvvedimento(model.getStep1ModelCache().getProvvedimento(), model.getStep1Model().getProvvedimento());
		
		boolean precedenteBloccatoDallaRagioneria = CruscottoRorUtilities.provvedimentoBloccato(model.getStep1ModelCache().getProvvedimento());
		
		if(precedenteBloccatoDallaRagioneria){
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, impegnoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			Errore err = new Errore();
			err.setCodice(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore(model.getStep1ModelCache().getProvvedimento().getOggetto()).getCodice());
			err.setDescrizione(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore(model.getStep1ModelCache().getProvvedimento().getOggetto()).getDescrizione());
			erroriSalvataggio.add(err);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
			
			
		}
		
		
			
		erroriSalvataggio = CruscottoRorUtilities.checkAnniReimputazioneRipetutiInAgg(listaModificheRor,
				erroriSalvataggio);
		erroriSalvataggio = CruscottoRorUtilities.checkAnnoNonCongruenteInAgg(listaModificheRor, erroriSalvataggio,
				sessionHandler.getBilancio().getAnno());
		
		if (erroriSalvataggio != null && !erroriSalvataggio.isEmpty()) {
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, impegnoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		
		// // Lista modifiche da annullare  in quanto si sono svuotati i campi
		List<ModificaMovimentoGestioneSpesa> modificheDaAnnullareDescImpNull = CruscottoRorUtilities
				.modificheDaAnnullare(listaModificheRor, checkDaMantenere);
		
		//qui ho necessita di avere le informazioni della modifica, dunque devo andarla a prendere dalal sessione
		List<ModificaMovimentoGestioneSpesa> modificheDaAnnullareComplete = CruscottoRorUtilities
				.modificheDaAnnullareComplete(modificheDaAnnullareDescImpNull, listaModificheOld);
		
		//SIAC-7349 Inizio SR190 CM 05/05/2020 Escludere dalla lista delle modifiche da annullare quelle che hanno modifiche collegate e tornare un errore
		erroriSalvataggio = CruscottoRorUtilities.checkModificheDaAnnullareModificheCollegate(modificheDaAnnullareDescImpNull, listaModificheOld, erroriSalvataggio);	
		//SIAC-7349 Fine  SR190 CM 05/05/2020
		
		
		if (listaModificheRor.size() > listaModificheOld.size()) {
			modificheNuoveDaAggiungere = CruscottoRorUtilities.modificheValideDaAggiungere(listaModificheRor); // setto
			erroriSalvataggio = CruscottoRorUtilities.checkModificheCompleteInAgg(erroriSalvataggio, modificheDaAggiornare);
		}
		if (erroriSalvataggio != null && !erroriSalvataggio.isEmpty()) {
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, impegnoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		
		
		//dovrei c
		
		//questi if servono per costruire nuove modifiche in aggiornamento
		if(getInesigibilitaRor().equals(false) && !CruscottoRorUtilities.checkEmptyModif(rorCancellazioneInesigibilita, "")){//significa che è un nuovo aggiornamento. Devo creare un mapping per mappare in modifica movimento
			ModificaMovimentoGestioneSpesa nuovaInesigibilita = CruscottoRorUtilities.mapCruscottoModelToMovimentoGestione(rorCancellazioneInesigibilita, "INEROR");
			modificheNuoveDaAggiungere.add(nuovaInesigibilita);			
		}else if(getInesigibilitaRor().equals(false) && (rorCancellazioneInesigibilita.getImporto() != null)){
			erroriSalvataggio = CruscottoRorUtilities.checkSingolaModificaCompleta(erroriSalvataggio, rorCancellazioneInesigibilita, null, null, null);
			
		}
		if(getInsussistenzaRor().equals(false) && !CruscottoRorUtilities.checkEmptyModif(rorCancellazioneInsussistenza, "")){//significa che è un nuovo aggiornamento. Devo creare un mapping per mappare in modifica movimento
			ModificaMovimentoGestioneSpesa nuovaIsussistenza = CruscottoRorUtilities.mapCruscottoModelToMovimentoGestione(rorCancellazioneInsussistenza, "INSROR");
			modificheNuoveDaAggiungere.add(nuovaIsussistenza);
			
		}else if(getInsussistenzaRor().equals(false) && (rorCancellazioneInsussistenza.getImporto() != null)){
			erroriSalvataggio = CruscottoRorUtilities.checkSingolaModificaCompleta(erroriSalvataggio, null, rorCancellazioneInsussistenza,null, null);
		}
		if(getDaMantenerePresente().equals(false) && getCheckDaMantenere() && !CruscottoRorUtilities.checkEmptyModif(rorDaMantenere, "RORM")){//significa che è un nuovo aggiornamento. Devo creare un mapping per mappare in modifica movimento
			ModificaMovimentoGestioneSpesa nuovoMantenere = CruscottoRorUtilities.mapCruscottoModelToMovimentoGestione(rorDaMantenere, "RORM");
			modificheNuoveDaAggiungere.add(nuovoMantenere);
			
		}else if(getDaMantenerePresente().equals(false) && getCheckDaMantenere()){
			erroriSalvataggio = CruscottoRorUtilities.checkSingolaModificaCompleta(erroriSalvataggio, null, null, rorDaMantenere, getCheckDaMantenere());
		}
		if (erroriSalvataggio != null && !erroriSalvataggio.isEmpty()) {
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, impegnoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}

		// prima però devo escludere quelli aggiunti da lista modifiche ror
		modifichePreesistentiAggiornate = CruscottoRorUtilities.modificheMovimentiPreesistenti(listaModificheRor);
		
		//SIAC-7349 Inizio SR190 CM 27/05/2020 Verifica se sono state aggiornate modifiche che hanno modifiche ad esse collegate e blocca la modifica
		erroriSalvataggio = CruscottoRorUtilities.checkMovimentiDaAggiornareModificheCollegate(modifichePreesistentiAggiornate, listaModificheOld, erroriSalvataggio);	

		if (erroriSalvataggio != null && !erroriSalvataggio.isEmpty()) {
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, impegnoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		//SIAC-7349 Fine SR190 CM 27/05/2020
		
		// Lista modifiche da annullare e inserire nuovamente, in quanto si è modificato l'importo
		List<ModificaMovimentoGestioneSpesa> modificheDaAnnullareERicerare = CruscottoRorUtilities
						.modificheDaAnnullarePerRicreare(modifichePreesistentiAggiornate, listaModificheOld, stessoProvvedimento);
		
		
		
		if(modificheDaAnnullareERicerare != null && modificheDaAnnullareERicerare.size()>0 && isPropagaSelected()){
			Errore err = new Errore();
			err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
			err.setDescrizione("Non &egrave; possibile modificare un importo col flag 'propagazione' attivo");
			erroriSalvataggio.add(err);
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, impegnoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
				
		}
		
		if(stessoProvvedimento){
			modificheDaAggiornare = CruscottoRorUtilities.getMovimentiDaAggiornare(modifichePreesistentiAggiornate,
					listaModificheOld);//
		}
		
		// Le modifiche da annullare sono quelle che hanno subito -> modifiche
		// in importo == 0 e descrizione null, che non andranno piu salvate
		// e in piu devo annullare quelle che hanno subito una modifica, dunque
		// quelle presenti in modifiche da aggiornare che coincidono
		/**
		 * modificheDaAnnullareDescImpNull modificheDaAggiornare
		 * 
		 */
		
		//in questo caso, i controlli per la testata devo farli solamente per quelle nuove che vengono inserite, in quanto in fase di aggiorna con propaga
		//vengono propagate solo quelle nuove

		modificheDaAggiornare.addAll(modificheNuoveDaAggiungere);
		
		
		// a partire da queste liste, ne va creata una nuova con la quale viene
		// creata la request di annullamento modifiche
		List<ModificaMovimentoGestioneSpesa> modifichePerAnnullamenti = new ArrayList<ModificaMovimentoGestioneSpesa>();
		if (!modificheDaAnnullareDescImpNull.isEmpty() || !modificheDaAnnullareERicerare.isEmpty()) {
			modifichePerAnnullamenti.addAll(modificheDaAnnullareDescImpNull);
			//se sono stati modificati importi e dunque si devono annullare
			modifichePerAnnullamenti.addAll(modificheDaAnnullareERicerare);
			//essendo che le reimputazioni vengono istanziate all'atterraggio, il metodo le vedrà da annullare. Dunque rimuovo quelle con uid 0
			modifichePerAnnullamenti = CruscottoRorUtilities.remuoviReimputazioniNonInserite(modifichePerAnnullamenti);
		}
		
		
		//questo calcolo mi serve per gestire la situazione mista aggiornamento e annullamento
		BigDecimal importoAnnullatoPrimaDiAggiornamento = BigDecimal.ZERO;
		//02/03/2020 
		//qui ho necessita degli importi reimputati e cancellati per il ricalcolo dei limiti importi di inserimento		
		BigDecimal importoAnnullatoPrimaDiAggiornamentoReimputazione = BigDecimal.ZERO;
		BigDecimal importoAnnullatoPrimaDiAggiornamentoCancellazione = BigDecimal.ZERO;
		
		
		//02/03/2020
		if(!modificheDaAnnullareDescImpNull.isEmpty()){
			importoAnnullatoPrimaDiAggiornamento = CruscottoRorUtilities.getImportoAttualeDaAnnullaInserisci(modificheDaAnnullareDescImpNull, listaModificheOld);
			
			importoAnnullatoPrimaDiAggiornamentoReimputazione = CruscottoRorUtilities.getImportoReimputatoAnnullato(modificheDaAnnullareDescImpNull, listaModificheOld);
			importoAnnullatoPrimaDiAggiornamentoCancellazione = CruscottoRorUtilities.getImportoCancellatoAnnullato(modificheDaAnnullareDescImpNull, listaModificheOld);
		}
		
		//per ricreare, clono quelle contenute nell'array delle modifiche da annullare e ricreare, e setto UID == 0
		List<ModificaMovimentoGestioneSpesa> nuoveDaRicreare = new ArrayList<ModificaMovimentoGestioneSpesa>();
		
		
		//FIXME se nuoveDaricerare pieno e propaga selected allora impedisci all'utente si salvare
		//devo trovare il vecchio importo
		//devo tener conto, per ricreare la spesa con il nuovo importo, anche le modifiche che annullo tramite uid spesa
		BigDecimal importoAnnullatoPerRicreare = BigDecimal.ZERO;
		BigDecimal importoAnnullatoReimputazionePerRicreare = BigDecimal.ZERO;
		BigDecimal importoAnnullatoCancellazionePerRicreare = BigDecimal.ZERO;
		
		if(modificheDaAnnullareERicerare.size()>0){
			nuoveDaRicreare = clone(modificheDaAnnullareERicerare);
			importoAnnullatoPerRicreare = CruscottoRorUtilities.getImportoAttualeDaAnnullaInserisci(nuoveDaRicreare, listaModificheOld);
			//02/03/2020
			importoAnnullatoReimputazionePerRicreare = CruscottoRorUtilities.getImportoReimputatoAnnullato(nuoveDaRicreare, listaModificheOld);
			importoAnnullatoCancellazionePerRicreare = CruscottoRorUtilities.getImportoCancellatoAnnullato(nuoveDaRicreare, listaModificheOld);
			
			BigDecimal nuovoImporto = BigDecimal.ZERO;
			
			if(numeroSubImpegno != null && !numeroSubImpegno.equals("")){
				nuovoImporto = model.getSubimpegnoInAggiornamento().getImportoAttuale().subtract(importoAnnullatoPerRicreare);
				nuovoImporto = nuovoImporto.subtract(importoAnnullatoPrimaDiAggiornamento);
				model.getSubimpegnoInAggiornamento().setImportoAttuale(nuovoImporto);
				setTipoImpegno("SubImpegno");

			}else{
				nuovoImporto = model.getImpegnoInAggiornamento().getImportoAttuale().subtract(importoAnnullatoPerRicreare);
				nuovoImporto = nuovoImporto.subtract(importoAnnullatoPrimaDiAggiornamento);
				model.getImpegnoInAggiornamento().setImportoAttuale(nuovoImporto);
				setTipoImpegno("Impegno");
			}

			
			for(ModificaMovimentoGestioneSpesa mmgs : nuoveDaRicreare){
				mmgs.setUid(0);
			}
			modificheDaAggiornare.addAll(nuoveDaRicreare);
		}else{
			BigDecimal nuovoImporto = BigDecimal.ZERO;
			//le modifiche di cui e' stata modificata solo la descrizione, hanno gia' decurtato l'importo attuale
			//SIAC-893
			BigDecimal importoDelleModificheDicuiModificatoSoloDesc =CruscottoRorUtilities.getImportoModificheConSolaDescrizioneModificataSpesa(modificheDaAggiornare, listaModificheOld);
			if(numeroSubImpegno != null && !numeroSubImpegno.equals("")){
				nuovoImporto = model.getSubimpegnoInAggiornamento().getImportoAttuale()
						.add(importoDelleModificheDicuiModificatoSoloDesc)
				//lascio questo come era prima, pur con dubbi sulla correttezza
					.subtract(importoAnnullatoPerRicreare)
					.subtract(importoAnnullatoPrimaDiAggiornamento);
				
				model.getSubimpegnoInAggiornamento().setImportoAttuale(nuovoImporto);
				setTipoImpegno("SubImpegno");

			}else{//SIAC-8093
				nuovoImporto =  model.getImpegnoInAggiornamento().getImportoAttuale()
						.add(importoDelleModificheDicuiModificatoSoloDesc)
				//lascio questo come era prima, pur con dubbi sulla correttezza
					.subtract(importoAnnullatoPerRicreare)
					.subtract(importoAnnullatoPrimaDiAggiornamento);
				model.getImpegnoInAggiornamento().setImportoAttuale(nuovoImporto);
				setTipoImpegno("Impegno");
			}
		}
		
		erroriSalvataggio = CruscottoRorUtilities.checkModificheCompleteInAgg(erroriSalvataggio, modificheDaAggiornare);
		
		
		
		//aggiungo le nuove modifiche annullate e in creazione: attento, devo tenere traccia dei vecchi importi; nel metodo di nuoveDaRicreare devo salvare il valore degli importo annullati
		
		// Costruisci request per aggiorna. Ciclo la lista delle modifiche, se
		// trovo importi e descrizione a null non le aggiungo come
		// nuovi movimento da inserire.
		List<ModificaMovimentoGestioneSpesa> modificheList = costruisciListaMovimentiSpesa(modificheDaAggiornare);
		
		
		
		if(modificheDaAggiornare != null && modificheDaAggiornare.isEmpty() && modifichePerAnnullamenti!=null && modifichePerAnnullamenti.isEmpty()){
			Errore err = new Errore();
			err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
			err.setDescrizione("Salvataggio non effettuato: non &egrave; stata aggiornata nessuna modifica");
			erroriSalvataggio.add(err);
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, impegnoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;				
		}
		
		//posso calcolare il residuo sottraendo al aggiungendo il delta importo impegno
		if(getDaMantenerePresente() && getCheckDaMantenere() != null && getCheckDaMantenere()==true){
			BigDecimal residuoZero = new BigDecimal("0.00");
			BigDecimal deltaImportoImpegno = residuoDaMantenereDopoAggiornamento(importoInizialeImpegno);
			BigDecimal residuoDaMantenere = model.getResiduoEventualeDaMantenere();
			residuoDaMantenere = residuoDaMantenere.add(deltaImportoImpegno);
			if(residuoDaMantenere.compareTo(residuoZero)==0){
				ModificaMovimentoGestioneSpesa mmgs = getDaMantenerePerAnnullare(listaModificheOld);
				modifichePerAnnullamenti.add(mmgs);
			}
		}
		
		if(modificheList != null && !modificheList.isEmpty()){
			erroriSalvataggio = checkModificheValide(modificheList, erroriSalvataggio, true,
					importoAnnullatoPrimaDiAggiornamentoReimputazione, importoAnnullatoPrimaDiAggiornamentoCancellazione,
					importoAnnullatoReimputazionePerRicreare, importoAnnullatoCancellazionePerRicreare);
		}
		erroriSalvataggio = CruscottoRorUtilities.checkCampiProvvedimentoVuoti(model.getStep1Model(), erroriSalvataggio);			
		
		
		if (erroriSalvataggio != null && !erroriSalvataggio.isEmpty()) {
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, impegnoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		
		//qui devo controllare se nelle modifiche ci sono degli atti inseriti che sono stati bloccati dalla ragioneria
		/*******CONTROLLO ERRORI PER PROVVEDIMENTO BLOCCATO****/
		if(modificheList.size()>0){
			ModificaMovimentoGestioneSpesa mmgs = modificheList.get(0);
			Integer idAtto = mmgs.getAttoAmministrativo().getUid();
			erroriSalvataggio = checkProvvedimentoInAggiorna(erroriSalvataggio, idAtto);
		}
		if (erroriSalvataggio != null && !erroriSalvataggio.isEmpty()) {
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, impegnoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		if(modificheDaAnnullareComplete.size()>0){
			ModificaMovimentoGestioneSpesa mmgs = modificheDaAnnullareComplete.get(0);
			Integer idAtto = mmgs.getAttoAmministrativo().getUid();
			erroriSalvataggio = checkProvvedimentoInAggiorna(erroriSalvataggio, idAtto);	
		}
		if (erroriSalvataggio != null && !erroriSalvataggio.isEmpty()) {
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, impegnoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE,
					new ArrayList<Errore>(model.getErrori()));
			addErrori(erroriSalvataggio);
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		//SIAC-8314		
		if(numeroSubImpegno != null && StringUtils.isNotBlank(numeroSubImpegno)) {
			impostaImportoAttualeSuSub();
		}
		
		//essendo che le modifiche vengono annullate e modificate per UID, viene in automatico il salvatagiio passando nella request, l'uid della modifica dell'impegno o subimpegno	
		AggiornaImpegno requestAggiorna = new AggiornaImpegno();
		requestAggiorna = convertiModelPerChiamataServizioInserisciAggiornaModifiche(false);
		
		//se vuoto, non chiamo il servizio, altrimenti annulla tutto l'impegno
		if(!modifichePerAnnullamenti.isEmpty()){
			AnnullaMovimentoSpesa requestAnnulla = convertiModelPerChiamataServizioAnnullaListaMovimenti(
					modifichePerAnnullamenti); // old list
			req.setAnnullaMovimento(requestAnnulla);
			req.setFlagAnnullaConsentito(true);
		}else{
			req.setFlagAnnullaConsentito(false);
		}
		//parte aggiorna
		
		
		if(!modificheList.isEmpty()){
			req.setFlagAggiornaConsentito(true);
			if(numeroSubImpegno != null && !numeroSubImpegno.equals("") ){
				BigDecimal numero = new BigDecimal(numeroSubImpegno);
				//SubImpegno sub = new SubImpegno();
				for(int i=0; i<requestAggiorna.getImpegno().getElencoSubImpegni().size(); i++){
					if((requestAggiorna.getImpegno().getElencoSubImpegni().get(i).getNumeroBigDecimal()).equals(numero)){
						if(requestAggiorna.getImpegno().getElencoSubImpegni().get(i).getListaModificheMovimentoGestioneSpesa()==null){
							List<ModificaMovimentoGestioneSpesa> listaNuovaDaAggiungere = new ArrayList<ModificaMovimentoGestioneSpesa>();
							listaNuovaDaAggiungere.addAll(modificheList);
							requestAggiorna.getImpegno().getElencoSubImpegni().get(i).setListaModificheMovimentoGestioneSpesa(listaNuovaDaAggiungere);
						}else{
							for(int j=0; j<requestAggiorna.getImpegno().getElencoSubImpegni().get(i).getListaModificheMovimentoGestioneSpesa().size(); j++){
								for(int h=0; h<modificheList.size();h++){
									if(modificheList.get(h).getUid() !=0 && requestAggiorna.getImpegno().getElencoSubImpegni().get(i).getListaModificheMovimentoGestioneSpesa().get(j).getUid()==modificheList.get(h).getUid()){
										modificheList.get(h).setUidSubImpegno(requestAggiorna.getImpegno().getElencoSubImpegni().get(i).getListaModificheMovimentoGestioneSpesa().get(j).getUid());
										modificheList.get(h).setTipoMovimento("SIM");
										requestAggiorna.getImpegno().getElencoSubImpegni().get(i).getListaModificheMovimentoGestioneSpesa().get(j).setDescrizioneModificaMovimentoGestione(modificheList.get(h).getDescrizioneModificaMovimentoGestione());
										requestAggiorna.getImpegno().getElencoSubImpegni().get(i).getListaModificheMovimentoGestioneSpesa().get(j).setImportoOld(modificheList.get(h).getImportoOld());
										
									}
								}
							}							
							for(int h=0; h<modificheList.size(); h++){
								if(modificheList.get(h).getUid()==0){
									requestAggiorna.getImpegno().getElencoSubImpegni().get(i).getListaModificheMovimentoGestioneSpesa().add(modificheList.get(h));
								}
							}								
						}
	
					}
				
				}

				//se propaga selezionato
				if(isPropagaSelected()==true){
					
					List<ModificaMovimentoGestioneSpesa> listeModifichePropagate = costruisciListaMovimentiSpesaPropaga(modificheDaAggiornare);					
					
					if(requestAggiorna.getImpegno().getListaModificheMovimentoGestioneSpesa()==null){
						List<ModificaMovimentoGestioneSpesa> listaNuovaDaAggiungere = new ArrayList<ModificaMovimentoGestioneSpesa>();
						listaNuovaDaAggiungere.addAll(listeModifichePropagate);
						if(!listeModifichePropagate.isEmpty()){
							setModificheDaPropagarePieno(true); 							
						}
						requestAggiorna.getImpegno().setListaModificheMovimentoGestioneSpesa(listaNuovaDaAggiungere);
						
					}else{
						if(!listeModifichePropagate.isEmpty()){
							setModificheDaPropagarePieno(true); 							
						}
						//requestAggiorna.getImpegno().getListaModificheMovimentoGestioneSpesa().addAll(listeModifichePropagate);
						for(int j=0; j<listeModifichePropagate.size(); j++){
							if(listeModifichePropagate.get(j).getUid()==0){
								requestAggiorna.getImpegno().getListaModificheMovimentoGestioneSpesa().add(listeModifichePropagate.get(j));
							}
						}
						
					}
				}

			}else{
				if(requestAggiorna.getImpegno().getListaModificheMovimentoGestioneSpesa()==null){
					List<ModificaMovimentoGestioneSpesa> listaNuovaDaAggiungere = new ArrayList<ModificaMovimentoGestioneSpesa>();
					listaNuovaDaAggiungere.addAll(modificheList);
					requestAggiorna.getImpegno().setListaModificheMovimentoGestioneSpesa(listaNuovaDaAggiungere);
					
				}else{
					
					for(int i=0; i<requestAggiorna.getImpegno().getListaModificheMovimentoGestioneSpesa().size(); i++){
						for(int j=0; j<modificheList.size(); j++){
							if(modificheList.get(j).getUid() !=0 && requestAggiorna.getImpegno().getListaModificheMovimentoGestioneSpesa().get(i).getUid()==modificheList.get(j).getUid()){
								requestAggiorna.getImpegno().getListaModificheMovimentoGestioneSpesa().get(i).setDescrizioneModificaMovimentoGestione(modificheList.get(j).getDescrizioneModificaMovimentoGestione());
								requestAggiorna.getImpegno().getListaModificheMovimentoGestioneSpesa().get(i).setImportoOld(modificheList.get(j).getImportoOld());
							}
						}
					}
					for(int j=0; j<modificheList.size(); j++){
						if(modificheList.get(j).getUid()==0){
							requestAggiorna.getImpegno().getListaModificheMovimentoGestioneSpesa().add(modificheList.get(j));
						}
					}
					
				}
				
			}

			req.setAggiornaImpegno(requestAggiorna);
		
		}else{
			req.setFlagAggiornaConsentito(false);
		}
		
		AnnullaAggiornaMovimentoResponse response = movimentoGestioneFinService.annullaAggiornaImpegno(req);
		if(response.isFallimento()){
			List<Errore> errs = CruscottoRorUtilities.getErroreDaWrapper(response.getDescrizioneErrori(), response.getErrori());
			
			ripristinaDatiNelModelPerSalvataggioConErrori(movimentoSpesaModelPrimaDiAggiorna, step1ModelPrimaDiAggiorna, impegnoInAggiornamentoPrimaDiAggiorna, listaSubPrimaDiAggiorna, subPrimaDiAggiorna);
			addErrori("Errore al salvataggio delle modifiche", errs);
			return INPUT;
		}
		
		
		sessionHandler 
		.setParametro(FinSessionParameter.LISTA_MODIFICHE_PRIMA_AGGIORNAMENTO, null);
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione(
				ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " "
						+ ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		if(isPropagaSelected()==true && isModificheDaPropagarePieno()==true){
			impostaMessaggioAzioneInSessionePerRedirezione(ErroreFin.NUOVE_MODIFICHE_PROPAGATE.getCodice() + " "
					+ ErroreFin.NUOVE_MODIFICHE_PROPAGATE.getErrore("impegno").getDescrizione());			
		}
//		if(getCheckDaMantenere() != null && getCheckDaMantenere()==true && modificheNuoveDaAggiungere.isEmpty() && getDaMantenerePresente()!=null && getDaMantenerePresente()==false){
//			impostaMessaggioAzioneInSessionePerRedirezione(ErroreFin.NUOVE_MODIFICHE_PROPAGATE.getCodice() + " "
//					+ ErroreFin.DA_MANTENERE_NON_INSERITA.getErrore("").getDescrizione());
//		}
		return SUCCESS_AGGIORNA;

	}

	protected AnnullaAggiornaMovimento creaAnnullaAggiornaMovimentoRequestBase() {
		AnnullaAggiornaMovimento req = new AnnullaAggiornaMovimento();
		req.setAnnoBilancio(sessionHandler.getAnnoBilancio());
		req.setDataOra(new Date());
		req.setRichiedente(sessionHandler.getRichiedente());
		
		//SIAC-8675
		req.setNumeroSub(numeroSubImpegno);
		req.setAnnoMovimento(model.getImpegnoInAggiornamento().getAnnoMovimento());
		req.setNumeroMovimento(model.getImpegnoInAggiornamento().getNumeroBigDecimal());
		req.setIdMovimento(model.getImpegnoInAggiornamento().getUid());
		req.setTipoMovimento(CostantiFin.MOVGEST_TIPO_IMPEGNO);
		req.setEnte(sessionHandler.getAccount().getEnte());
		return req;
	}

//	private void convertiImportiInNegativo() {
//		for(int i=0; i<listaModificheRor.size();i++){
//			BigDecimal importo = listaModificheRor.get(i).getImportoOld();
//			if(listaModificheRor.get(i).getUid() != 0 && importo.compareTo(BigDecimal.ZERO)>0){
//				listaModificheRor.get(i).setImportoOld(importo.negate());
//			}		
//		}				
//	}

	private ModificaMovimentoGestioneSpesa getDaMantenerePerAnnullare(
			List<ModificaMovimentoGestioneSpesa> listaModificheRor2) {
		for(ModificaMovimentoGestioneSpesa mmgs : listaModificheRor2){
			if(mmgs.getStatoOperativoModificaMovimentoGestione().equals(StatoOperativoModificaMovimentoGestione.VALIDO) &&
					mmgs.getTipoModificaMovimentoGestione().equals("RORM")){
				return mmgs;
			}
		}
		// TODO Auto-generated method stub
		return null;
	}

	private BigDecimal residuoDaMantenereDopoAggiornamento(BigDecimal importoInizialeImpegno) {
		BigDecimal importoFinale = BigDecimal.ZERO;
		if(numeroSubImpegno != null && !numeroSubImpegno.equals("")){
			importoFinale=model.getSubimpegnoInAggiornamento().getImportoAttuale();
		}else{
			importoFinale=model.getImpegnoInAggiornamento().getImportoAttuale();
		}
		return importoFinale.subtract(importoInizialeImpegno);
	}

	private List<ModificaMovimentoGestioneSpesa> costruisciListaMovimentiSpesa(
			List<ModificaMovimentoGestioneSpesa> listaModificheRor2) {
		List<ModificaMovimentoGestioneSpesa> modificheList = new ArrayList<ModificaMovimentoGestioneSpesa>();
		BigDecimal importoZero = new BigDecimal("0.00");
		for (int i = 0; i < listaModificheRor2.size(); i++) {
			ModificaMovimentoGestioneSpesa mmgs = listaModificheRor2.get(i);
			if (!mmgs.getImportoOld().equals(importoZero)
					&& !mmgs.getDescrizioneModificaMovimentoGestione().equals("")) {
				modificheList.add(singolaSpesaAggiorna(mmgs));
			}
			
			if(mmgs.getTipoModificaMovimentoGestione().equals("RORM") && !mmgs.getDescrizioneModificaMovimentoGestione().equals("")){
				modificheList.add(singolaSpesaAggiorna(mmgs));
				
			}

		}

		return modificheList;
	}
	
	
	//se ci sono nuove modifiche da annullare e ricreare, l'importo attuale deve tener conto della modifica che si sta annullando, quindi quando faccio l'annullamento
	//devo campiare l'ilmporto attuale dell'impegno in aggiornamento per creare la nuova spesa
	private ModificaMovimentoGestioneSpesa singolaSpesaAggiorna(ModificaMovimentoGestioneSpesa mmgs) {
		ModificaMovimentoGestioneSpesa spesa = new ModificaMovimentoGestioneSpesa();
		BigDecimal importoInInput = new BigDecimal("0.00");

		if (!mmgs.getTipoModificaMovimentoGestione().equals("RORM")) {
			importoInInput = mmgs.getImportoOld();
			if (importoInInput == null || importoInInput.equals("")) {
				importoInInput = new BigDecimal("0.00");
			}
		}

		BigDecimal importoDiInput = importoInInput;// da
		//SIAC-7405 in cancellazione positivo da negare
		if(!mmgs.getTipoModificaMovimentoGestione().equals("RORM") && importoDiInput.compareTo(BigDecimal.ZERO)>0){
			importoDiInput = importoDiInput.negate();
		}
		
		BigDecimal importoAttuale = model.getImpegnoInAggiornamento().getImportoAttuale();
		
		
		spesa.setTipoMovimento("IMP");//qui devo metterlo dinamico
		if(numeroSubImpegno!=null && !numeroSubImpegno.equals("")){
			spesa.setNumeroSubImpegno(Integer.valueOf(numeroSubImpegno));
			spesa.setTipoMovimento("SIM");//qui devo metterlo dinamico
			importoAttuale = model.getSubimpegnoInAggiornamento().getImportoAttuale();
				
		}
		
		BigDecimal importoCambiato = importoAttuale.add(importoDiInput);
		spesa.setUid(mmgs.getUid());
		spesa.setImportoOld(importoDiInput); // da																	// //FIXME
		spesa.setImportoNew(importoCambiato);
		if(numeroSubImpegno!=null && !numeroSubImpegno.equals("")){//
			model.getSubimpegnoInAggiornamento().setImportoAttuale(importoCambiato);	
		}else{
			model.getMovimentoSpesaModel().getImpegno().setImportoAttuale(importoCambiato);
			model.getImpegnoInAggiornamento().setImportoAttuale(importoCambiato);
			//.importoAttuale.malediozne
			model.getStep1Model().setImportoFormattato(convertiBigDecimalToImporto(importoCambiato));
			model.getStep1Model().setImportoImpegno(importoCambiato);	
		}
		

		

		// DATI DEL PROVVEDIMENTO DELLA MODIFICA:
		spesa = settaDatiProvvDalModel(spesa, model.getStep1Model().getProvvedimento());


		spesa.setTipoModificaMovimentoGestione(mmgs.getTipoModificaMovimentoGestione());
		String descrizione = null;

		if (mmgs.getDescrizioneModificaMovimentoGestione() != null && !mmgs.getDescrizioneModificaMovimentoGestione().equals("")) {
			descrizione = CruscottoRorUtilities.troncaDescrizione(mmgs.getDescrizioneModificaMovimentoGestione());
		} else {
			descrizione = "";
		}
		spesa.setDescrizioneModificaMovimentoGestione(descrizione);
		spesa.setDescrizione(descrizione);
		
//		if (model.getnSubImpegno() == null) {
//			spesa.setIdStrutturaAmministrativa(model.getImpegnoInAggiornamento().getIdStrutturaAmministrativa());
//			spesa.getAttoAmministrativo().setStrutturaAmmContabile(
//					model.getImpegnoInAggiornamento().getAttoAmministrativo().getStrutturaAmmContabile());
//			spesa.getAttoAmministrativo().setUid(model.getImpegnoInAggiornamento().getAttoAmministrativo().getUid());			
//		}
		if (mmgs.getTipoModificaMovimentoGestione().equals("REIMP")) {
			spesa.setReimputazione(true);
			spesa.setAnnoReimputazione(mmgs.getAnnoReimputazione());
		}
		spesa.setStatoOperativoModificaMovimentoGestione(StatoOperativoModificaMovimentoGestione.VALIDO);

		return spesa;
	}



	private AnnullaMovimentoSpesa convertiModelPerChiamataServizioAnnullaListaMovimenti(
			List<ModificaMovimentoGestioneSpesa> listaModificheRorDaAggiornare2) {

		AnnullaMovimentoSpesa request = new AnnullaMovimentoSpesa();
		request.setEnte(sessionHandler.getEnte());
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setBilancio(creaOggettoBilancio());

		// setto impegno
		Impegno impegno = new Impegno();
		impegno.setUid(model.getMovimentoSpesaModel().getImpegno().getUid());
		// setto uid movimento spesa

		List<ModificaMovimentoGestioneSpesa> listaMGP = new ArrayList<ModificaMovimentoGestioneSpesa>();
		for (int i = 0; i < listaModificheRorDaAggiornare2.size(); i++) {
			ModificaMovimentoGestioneSpesa mgs = new ModificaMovimentoGestioneSpesa();
			mgs.setUid(Integer.valueOf(listaModificheRorDaAggiornare2.get(i).getUid()));
			mgs.setTipoModificaMovimentoGestione(listaModificheRorDaAggiornare2.get(i).getTipoModificaMovimentoGestione());
			listaMGP.add(mgs);
		}
		//SIAC-7349 - MR - 29/05/2020 aggiungo la disponibilita della componente alla request
		//SIAC-7768  FL 18/09/2020
		if (model.getMaxImportoImpComp()!= null)
			impegno.setDisponibilitaImpegnareComponente(convertiImportoToBigDecimal(model.getMaxImportoImpComp()));
		
		listaMGP = firstCancellazioni(listaMGP);

		impegno.setListaModificheMovimentoGestioneSpesa(listaMGP);
		request.setImpegno(impegno);
		return request;

	}
	
	//devo prima passare le cancellazioni altrimenti non permette l'annullamento atomico delle modifiche
	//1 INSROR/INEROR -> 2 REIMP
	private List<ModificaMovimentoGestioneSpesa> firstCancellazioni(List<ModificaMovimentoGestioneSpesa> listaMGP) {
		List<ModificaMovimentoGestioneSpesa> listaOrdinata = new ArrayList<ModificaMovimentoGestioneSpesa>();
		for(ModificaMovimentoGestioneSpesa mmgs: listaMGP){
			
			if(mmgs.getTipoModificaMovimentoGestione().equals("INSROR") || mmgs.getTipoModificaMovimentoGestione().equals("INEROR")){
				listaOrdinata.add(mmgs);
			}
		}
		for(ModificaMovimentoGestioneSpesa mmgs: listaMGP){
			
			if(!mmgs.getTipoModificaMovimentoGestione().equals("INSROR") && !mmgs.getTipoModificaMovimentoGestione().equals("INEROR")){
				listaOrdinata.add(mmgs);
			}
		}
		
		return listaOrdinata;
	}

	/**
	 * pilota l'abilitazione alla modifica della propagazione all'impegno
	 * 
	 * @return
	 */
	public boolean abilitaModificaSubImpegno() {
		boolean abilitazioneModifica = false;

		if (getTipoImpegno().equalsIgnoreCase("SubImpegno")) {
			if (!StringUtils.isEmpty(getSubSelected())) {
				abilitazioneModifica = true;
				setSubImpegnoSelected(true);
				model.setSubImpegnoSelectedMod(true);
			}
		}

		return abilitazioneModifica;
	}

	/**
	 * @return the propagaSelected
	 */
	public boolean isPropagaSelected() {
		return propagaSelected;
	}

	/**
	 * @param propagaSelected
	 *            the propagaSelected to set
	 */
	public void setPropagaSelected(boolean propagaSelected) {
		this.propagaSelected = propagaSelected;
	}

	protected List<Errore> controlliReimputazioneInInserimentoEMotivoRIAC(List<Errore> listaErrori, String tipoImpegno,
			String abbina, ModificaMovimentoGestioneSpesa spesaSelezionata) {
		String reimputazione = null;
		if (spesaSelezionata.isReimputazione() == false) {
			reimputazione = WebAppConstants.No;
		} else {
			reimputazione = WebAppConstants.Si;
		}

		Bilancio bilancio = this.sessionHandler.getBilancio();
		int annoBilancio = bilancio.getAnno();

		if (listaErrori == null) {
			listaErrori = new ArrayList<Errore>();
		}

		WebAppConstants.Si.equals(reimputazione);
		boolean modificaSulSubSenzaIndicarneLaTestata = (tipoImpegno.equals("SubImpegno")
				&& !"Modifica Anche Impegno".equalsIgnoreCase(abbina))
				|| (tipoImpegno.equals("SubAccertamento") && !"Modifica Anche Accertamento".equalsIgnoreCase(abbina));
		
		//fix inserita per permettere l'aggiornamento delle reimputazioni quando si è in fase di aggiorna per le reimputazioni del subimpegno
		//15/03/2020 DA RIVEDERE in quanto visto che si tratta di un nuovo inserimento, per logica andrebbe propagata. 
		//tuttavia, la modifica di un importo non si puòà fare in modalita propaga in quanto andrebbe ad inserire una nuova
		//modifica al padre. Da togliere, se necessario isInInserimento()
		//if (modificaSulSubSenzaIndicarneLaTestata && WebAppConstants.Si.equals(reimputazione) && spesaSelezionata.getUid()==0)

		if (modificaSulSubSenzaIndicarneLaTestata && WebAppConstants.Si.equals(reimputazione) &&  spesaSelezionata.getUid()==0) {
			// La modifica e' sul sub, ma non e' stato selezionato anche
			// l'impegno, errore:
			Errore errore = tipoImpegno.equals("SubImpegno")
					? ErroreFin.REIMPUTAZIONE_NON_PREVISTA_PER_I_SUB_IMP_CRUSC.getErrore(spesaSelezionata.getAnnoReimputazione().toString())
					: ErroreFin.REIMPUTAZIONE_NON_PREVISTA_PER_I_SUB_ACC_CRUSC.getErrore(spesaSelezionata.getAnnoReimputazione().toString());
			listaErrori.add(errore);
			return listaErrori;// questo errore maschera quelli dopo, perche'
								// tanto l'utente dovra' mettere reimputazione
								// no
		}
		
		boolean isRORDaMantenere = "RORM".equals(StringUtils.defaultIfBlank(spesaSelezionata.getTipoModificaMovimentoGestione(), ""));
		
		boolean isImportoZero = BigDecimal.ZERO.compareTo(spesaSelezionata.getImportoOld()) == 0;
		
		if (isRORDaMantenere && !isImportoZero) {
			listaErrori.add(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" in caso di motivo ROR-Da mantenere ",
					"deve essere pari a zero"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
		
//		boolean isCancellazioni = "INEROR".equals(StringUtils.defaultIfBlank(spesaSelezionata.getTipoModificaMovimentoGestione(), "")) || "INSROR".equals(StringUtils.defaultIfBlank(spesaSelezionata.getTipoModificaMovimentoGestione(), ""));
//		boolean isImportoMinDiZero = BigDecimal.ZERO.compareTo(spesaSelezionata.getImportoOld()) > 0;
//		if(isCancellazioni && isImportoMinDiZero){
//			String motivo = spesaSelezionata.getTipoModificaMovimentoGestione().equals("INSROR") ? "ROR - Cancellazione per Insussistenza" : "ROR - Cancellazione per Inesigibilit&agrave;" ;
//			listaErrori.add(ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore(" in caso di motivo "+motivo, "l' importo deve essere maggiore di zero"));
//		}

		// controlli su reimputazione:
		controlliSuReimputazioneCrusc(listaErrori, tipoImpegno, String.valueOf(spesaSelezionata.getImportoOld()),
				spesaSelezionata.getTipoModificaMovimentoGestione(), reimputazione, annoBilancio, spesaSelezionata);


		

		return listaErrori;
	}

	private void controlliSuReimputazioneCrusc(List<Errore> listaErrori, String tipoImpegno,
			String importoImpegnoModImp, String tipoMotivo, String reimputazione, int annoBilancio,
			ModificaMovimentoGestioneSpesa spesaSelezionata) {
		if (!WebAppConstants.Si.equals(reimputazione)) {
			// non e' una reimputazione, non devo effettuare i controlli sulla
			// reimputazione
			return;
		}

		// se selezionato si, deve essere compilato l'anno:
		if (spesaSelezionata.getAnnoReimputazione() == null || spesaSelezionata.getAnnoReimputazione() <= 0) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Reimputazione"));
		} else if (spesaSelezionata.getAnnoReimputazione().intValue() <= Integer
				.valueOf(sessionHandler.getAnnoBilancio())) {
			// inoltre l'anno di reimputazione deve essere maggiore dell'anno di
			// bilancio:
			listaErrori.add(ErroreFin.VALORE_NON_VALIDO.getErrore("Anno Reimputazione",
					"(deve essere maggiore dell'anno di bilancio)"));
		}

		// in caso di reimputazione, l'importo della modifica puo' essere solo
		// negativo:
		//SIAC-7502 reimputazioni devono essere positive
//		if (!StringUtils.isEmpty(importoImpegnoModImp)
//				&& NumberUtil.isNumber(importoImpegnoModImp.replace(".", "").replace(",", "").replace("-", ""))) {
//			BigDecimal importoDiInput = convertiImportoToBigDecimal(importoImpegnoModImp); //FIXME
//			// nel caso importo fosse nullo scattera' gia' il controllo piu'
//			// avanti sulla sua obbligatorieta'
//			if (importoDiInput.compareTo(BigDecimal.ZERO) >= 0 && spesaSelezionata.getAnnoReimputazione() != null) {
//				Errore err = new Errore();
//				err.setCodice("FIN_ERR_0127");
//				err.setDescrizione("La reimputazione per l'anno " + spesaSelezionata.getAnnoReimputazione()
//						+ " puo' essere realizzata solo a seguito di una modifica in diminuzione di importo del movimento");
//				listaErrori.add(err); 
//			}
//		}
		
		

		boolean isMovimentoGestioneSpesa = tipoImpegno.equals("Impegno") || tipoImpegno.equals("SubImpegno");
		boolean isMovimentoGestioneEntrata = tipoImpegno.equals("SubAccertamento")
				|| tipoImpegno.equals("Accertamento");
		if (!isMovimentoGestioneSpesa && !isMovimentoGestioneEntrata) {
			// questo non dovrebbe mai capitare, messo per analogia con il
			// comportamento precedente
			return;
		}

//		//SIAC -7559 Inizio  ROR 10 11 FL 19/05/2020
//		if (annoBilancio > spesaSelezionata.getAnnoMovimento()) {
//			Errore erroreDaLanciare = isMovimentoGestioneSpesa
//					? ErroreFin.REIMPUTAZIONE_RESIDUI_NON_AMMESSO_IMP.getErrore()
//					: ErroreFin.REIMPUTAZIONE_RESIDUI_NON_AMMESSO_ACC.getErrore();
//			// se anno Impegno < anno di bilancio FIN_ERR_0129 - Reimputazione
//			// residui non ammesso, 'Accertamento'
//			listaErrori.add(erroreDaLanciare);
//		}
//		//SIAC -7559 Fine  ROR 10 11 FL 19/05/2020

		//SIAC -7551 Inizio  ROR 10 11 FL 19/05/2020
		/*
		 * SIAC-7689 
		 * FIX VG anno Movimento non sempre valorizzato
		 */
		if (model.getStep1Model()!= null && model.getStep1Model().getAnnoImpegno()!= null &&
				annoBilancio > model.getStep1Model().getAnnoImpegno().intValue()) {
			Errore erroreDaLanciare = isMovimentoGestioneSpesa
					? ErroreFin.REIMPUTAZIONE_RESIDUI_NON_AMMESSO_IMP.getErrore()
					: ErroreFin.REIMPUTAZIONE_RESIDUI_NON_AMMESSO_ACC.getErrore();
			// se anno Impegno < anno di bilancio FIN_ERR_0129 - Reimputazione
			// residui non ammesso, 'Accertamento'
			listaErrori.add(erroreDaLanciare);
		}
		//SIAC -7551 Fine  ROR 10 11 FL 19/05/2020
		

	}



	
	
	
	
	private ModificaMovimentoGestioneSpesa singolaSpesaAggiornaPropaga(ModificaMovimentoGestioneSpesa mmgs) {
		ModificaMovimentoGestioneSpesa spesa = new ModificaMovimentoGestioneSpesa();
		BigDecimal importoInInput = new BigDecimal("0.00");

		if (!mmgs.getTipoModificaMovimentoGestione().equals("RORM")) {
			importoInInput = mmgs.getImportoOld();
			if (importoInInput == null || importoInInput.equals("")) {
				importoInInput = new BigDecimal("0.00");
			}
		}

		BigDecimal importoDiInput = importoInInput;// da
		//SIAC-7405 in cancellazione positivo da negare
		if(!mmgs.getTipoModificaMovimentoGestione().equals("RORM") && importoDiInput.compareTo(BigDecimal.ZERO)>0){
			importoDiInput = importoDiInput.negate();
		}
		
		BigDecimal importoAttuale = model.getMovimentoSpesaModel().getImpegno().getImportoAttuale();
		BigDecimal importoCambiato = importoAttuale.add(importoDiInput);
		spesa.setUid(mmgs.getUid());
		spesa.setImportoOld(importoDiInput); // da																	// //FIXME
		spesa.setImportoNew(importoCambiato);
		spesa.setTipoMovimento("IMP");//qui devo metterlo dinamico
		
		model.getMovimentoSpesaModel().getImpegno().setImportoAttuale(importoCambiato);
		model.getStep1Model().setImportoFormattato(convertiBigDecimalToImporto(importoCambiato));
		model.getStep1Model().setImportoImpegno(importoCambiato);
		// DATI DEL PROVVEDIMENTO DELLA MODIFICA:
		spesa = settaDatiProvvDalModel(spesa, model.getStep1Model().getProvvedimento());

		spesa.setTipoModificaMovimentoGestione(getIdTipoMotivo());
		if (mmgs.getDescrizione() != null) {
			spesa.setDescrizione(mmgs.getDescrizione().toUpperCase());
		}

		settaDatiReimputazioneDalModel(spesa);
		spesa.setAttoAmministrativoAnno(
				String.valueOf(model.getStep1Model().getProvvedimento().getAnnoProvvedimento()));
		spesa.setAttoAmministrativoNumero(model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue());
		if (!isEmpty(model.getStep1Model().getProvvedimento().getCodiceTipoProvvedimento())) {
			spesa.setAttoAmministrativoTipoCode(
					String.valueOf(model.getStep1Model().getProvvedimento().getCodiceTipoProvvedimento()));
		} else {
			spesa.setAttoAmministrativoTipoCode(
					String.valueOf(model.getStep1Model().getProvvedimento().getTipoProvvedimento()));
		}
		spesa.setIdStrutturaAmministrativa(model.getStep1Model().getProvvedimento().getUidStrutturaAmm());
		AttoAmministrativo attoAmm = popolaProvvedimento(model.getStep1Model().getProvvedimento());
		spesa.setAttoAmministrativo(attoAmm);
		// va tenuto coerente:
		TipoAtto attoAmmTipoAtto = attoAmm.getTipoAtto();
		spesa.setAttoAmmTipoAtto(attoAmmTipoAtto);
		//


		spesa.setTipoModificaMovimentoGestione(mmgs.getTipoModificaMovimentoGestione());
		String descrizione = null;

		if (mmgs.getDescrizioneModificaMovimentoGestione() != null && !mmgs.getDescrizioneModificaMovimentoGestione().equals("")) {
			descrizione = CruscottoRorUtilities.troncaDescrizione(mmgs.getDescrizioneModificaMovimentoGestione());
		} else {
			descrizione = "";
		}
		spesa.setDescrizioneModificaMovimentoGestione(descrizione);
		spesa.setDescrizione(descrizione);
		
		//SIAC-7502 non verrà trovato il provvedimento se non commento questo
//		spesa.setIdStrutturaAmministrativa(model.getImpegnoInAggiornamento().getIdStrutturaAmministrativa());
//		spesa.getAttoAmministrativo().setStrutturaAmmContabile(
//				model.getImpegnoInAggiornamento().getAttoAmministrativo().getStrutturaAmmContabile());
//		spesa.getAttoAmministrativo().setUid(model.getImpegnoInAggiornamento().getAttoAmministrativo().getUid());
		if (mmgs.getTipoModificaMovimentoGestione().equals("REIMP")) {
			spesa.setReimputazione(true);
			spesa.setAnnoReimputazione(mmgs.getAnnoReimputazione());
		}
		spesa.setStatoOperativoModificaMovimentoGestione(StatoOperativoModificaMovimentoGestione.VALIDO);

		return spesa;
	}
	
	private List<ModificaMovimentoGestioneSpesa> costruisciListaMovimentiSpesaPropaga(
			List<ModificaMovimentoGestioneSpesa> listaModificheRor2) {
		List<ModificaMovimentoGestioneSpesa> modificheList = new ArrayList<ModificaMovimentoGestioneSpesa>();
		BigDecimal importoZero = new BigDecimal("0.00");
		for (int i = 0; i < listaModificheRor2.size(); i++) {
			ModificaMovimentoGestioneSpesa mmgs = listaModificheRor2.get(i);
			if (!mmgs.getImportoOld().equals(importoZero)
					&& !mmgs.getDescrizioneModificaMovimentoGestione().equals("")) {
				modificheList.add(singolaSpesaAggiornaPropaga(mmgs));
			}
			
			if(mmgs.getTipoModificaMovimentoGestione().equals("RORM") && !mmgs.getDescrizioneModificaMovimentoGestione().equals("")){
				modificheList.add(singolaSpesaAggiornaPropaga(mmgs));
			}

		}

		return modificheList;
	}
	
	public String annulla(){
		
		return SUCCESS_SALVA;
		
	}
	
	private void ripristinaDatiNelModelPerSalvataggioConErrori(GestisciModificaMovimentoSpesaModel movimentoSpesaModelPrimaDiSalva,
			GestisciImpegnoStep1Model step1ModelPrimaDiSalva,Impegno impegnoInAggiornamentoPrimaDiSalva,List<SubImpegno> listaSubPrimaDiSalva, SubImpegno subimpegnoInAggiornamentoPrimaDiSalva){
		//setto nel model i backup che mi sono fatto prima di invocare il servizio:
		model.setMovimentoSpesaModel(movimentoSpesaModelPrimaDiSalva);
		model.setStep1Model(step1ModelPrimaDiSalva);
		model.setImpegnoInAggiornamento(impegnoInAggiornamentoPrimaDiSalva);
		model.setListaSubimpegni(listaSubPrimaDiSalva);
		model.setSubimpegnoInAggiornamento(subimpegnoInAggiornamentoPrimaDiSalva);
	}

	/**
	 * @return the uidMovgest
	 */
	public Integer getUidMovgest() {
		return uidMovgest;
	}

	/**
	 * @param uidMovgest the uidMovgest to set
	 */
	public void setUidMovgest(Integer uidMovgest) {
		this.uidMovgest = uidMovgest;
	}
	
	protected AggiornaImpegno convertiModelPerChiamataServizioInserisciAggiornaModificheSub(boolean pulisciTE) {
		
		// AGGIORNA IMPEGNO
	   
	   AggiornaImpegno aggiornaImpegnoReq = new AggiornaImpegno();
	    
	   Bilancio bilancio = new Bilancio();
	   bilancio.setAnno(sessionHandler.getAnnoBilancio());
	   copiaTransazioneElementareSupportSuModel(pulisciTE);
	   Impegno impegnoDaAggiornare = new Impegno();
	   impegnoDaAggiornare = popolaImpegnoInserisciAggiornaModificheSub(model, null, bilancio);
	   
	   
	   aggiornaImpegnoReq.setImpegno(impegnoDaAggiornare);
	   aggiornaImpegnoReq.setEnte(model.getEnte());
	   aggiornaImpegnoReq.getImpegno().setSoggetto(impegnoDaAggiornare.getSoggetto());
	     
	   
	   // subimpegni
	   if(!isEmpty(model.getListaSubimpegni())){
		   aggiornaImpegnoReq.getImpegno().setElencoSubImpegni(model.getListaSubimpegni());
	   }else{
		   aggiornaImpegnoReq.getImpegno().setElencoSubImpegni(new ArrayList<SubImpegno>());
	   }
	   
	   // eventuali vincoli
	   if(null!=model.getStep1Model().getListaVincoliImpegno() && !model.getStep1Model().getListaVincoliImpegno().isEmpty()){
		   aggiornaImpegnoReq.getImpegno().setVincoliImpegno(model.getStep1Model().getListaVincoliImpegno());
	   }
		   
	   aggiornaImpegnoReq.setRichiedente(sessionHandler.getRichiedente());
	   aggiornaImpegnoReq.setUnitaElementareDiGestione(null);
	   aggiornaImpegnoReq.setBilancio(bilancio);
	   
	   return aggiornaImpegnoReq;
	}
	
	protected Impegno popolaImpegnoInserisciAggiornaModificheSub(GestisciMovGestModel datiImpegno, ImpegniPluriennaliModel pluriennale, Bilancio bilancio) {
		
		Impegno impegno = new Impegno();
		SubImpegno sub = datiImpegno.getSubimpegnoInAggiornamento();
		impegno.setCapitoloUscitaGestione(convertModelPerChiamata(datiImpegno.getStep1Model().getCapitolo()));
		impegno.setAnnoCapitoloOrigine(datiImpegno.getStep1Model().getCapitolo().getAnno());
		
		ClassificatoreGenerico cg = new ClassificatoreGenerico();
		cg.setCodice(sub.getTipoImpegno().getCodice());
		
		impegno.setTipoImpegno(cg);
		
		impegno.setStatoOperativoMovimentoGestioneSpesa(sub.getStato().toString());
	
		////////////////////////////////////////////////////////
		if (datiImpegno.getStep1Model().getAnnoFinanziamento() != null && datiImpegno.getStep1Model().getAnnoFinanziamento() != 0) { log.debug("","anno F -*-> " + datiImpegno.getStep1Model().getAnnoFinanziamento());
			impegno.setAnnoFinanziamento(datiImpegno.getStep1Model().getAnnoFinanziamento());
		}
		if (datiImpegno.getStep1Model().getNumeroFinanziamento() != null && datiImpegno.getStep1Model().getNumeroFinanziamento() != 0) { log.debug("","num F -*-> " + datiImpegno.getStep1Model().getNumeroFinanziamento());
			impegno.setNumeroAccFinanziamento(datiImpegno.getStep1Model().getNumeroFinanziamento());
		}
		
		//correzione anomalia flag riaccertamento
		if(datiImpegno.getStep1Model().getRiaccertato().equalsIgnoreCase("si") || datiImpegno.getStep1Model().getReanno().equalsIgnoreCase("si")){
			
			if(datiImpegno.getStep1Model().getRiaccertato().equalsIgnoreCase("si")) {
				impegno.setFlagDaRiaccertamento(true);
			}else if(datiImpegno.getStep1Model().getReanno().equalsIgnoreCase("si")) {
				impegno.setFlagDaReanno(true);
			}
			
			if (datiImpegno.getStep1Model().getAnnoImpRiacc() != null && datiImpegno.getStep1Model().getAnnoImpRiacc() != 0) {
				impegno.setAnnoRiaccertato(datiImpegno.getStep1Model().getAnnoImpRiacc());
			}
		
			if (datiImpegno.getStep1Model().getNumImpRiacc() != null && datiImpegno.getStep1Model().getNumImpRiacc().intValue()!=0) {
				impegno.setNumeroRiaccertato(new BigDecimal(datiImpegno.getStep1Model().getNumImpRiacc()));
			}
		} else {
			impegno.setFlagDaRiaccertamento(false);
			impegno.setFlagDaReanno(false);
		}	

		impegno.setFlagSoggettoDurc(datiImpegno.getStep1Model().getSoggettoDurc().equalsIgnoreCase("si"));
		
		if (datiImpegno.getStep1Model().getAnnoImpOrigine() != null && datiImpegno.getStep1Model().getAnnoImpOrigine() != 0) { log.debug("","anno O -*-> " + datiImpegno.getStep1Model().getAnnoImpOrigine());
			impegno.setAnnoImpegnoOrigine(datiImpegno.getStep1Model().getAnnoImpOrigine());
		}
		if (datiImpegno.getStep1Model().getNumImpOrigine() != null && datiImpegno.getStep1Model().getNumImpOrigine() != 0) { log.debug("","num O -*-> " + datiImpegno.getStep1Model().getNumImpOrigine());
			impegno.setNumImpegnoOrigine(datiImpegno.getStep1Model().getNumImpOrigine());
		}

		////////////////////////////////////////////////////////////////////////////////////////
		impegno.setAnnoMovimento(sub.getAnnoMovimento()); //da verificare
		impegno.setUid(sub.getUid()); //da verificare
		impegno.setNumeroBigDecimal(new BigDecimal(datiImpegno.getStep1Model().getNumeroImpegno())); //da verificare
		///////////////////////////////////////////////////////////////////////////////////////
		if(datiImpegno.getStep1Model().getOggettoImpegno()!=null){
			impegno.setDescrizione(datiImpegno.getStep1Model().getOggettoImpegno().toUpperCase());
		}
		impegno.setCig(datiImpegno.getStep1Model().getCig());
		impegno.setCup(datiImpegno.getStep1Model().getCup());
		
		
		//////////////////Modifica Movimento Spesa //////////////////////////////
		impegno.setListaModificheMovimentoGestioneSpesa(sub.getListaModificheMovimentoGestioneSpesa());
		/////////////////////////////////////////////////////////////////////////
		
		//PROGETTO
		if(null!=datiImpegno.getStep1Model().getProgetto()){
			Progetto progetto = new Progetto();
			progetto.setCodice(datiImpegno.getStep1Model().getProgetto());
			// SIAC-6990
			// Popolo i campi del progetto con i parametri che servono per il getProgetto() di > MGOD
			progetto.setTipoProgetto(TipoProgetto.GESTIONE);
			progetto.setBilancio(sessionHandler.getBilancio());
//			progetto.setStatoOperativoProgetto();
			
			impegno.setProgetto(progetto);
		}
		
		// popolo tutti e 2 i valori
		impegno.setImportoIniziale(sub.getImportoIniziale());
		impegno.setImportoAttuale(sub.getImportoAttuale()); 
	
		impegno.setDataScadenza(sub.getDataScadenza());
		
		// valori classificazioni della seconda pagina
		impegno.setCodMissione(sub.getCodMissione());
		impegno.setCodProgramma(sub.getCodProgramma());
		
		// provvedimento dell'impegno! 
		impegno.setAttoAmministrativo(sub.getAttoAmministrativo());
	
		///SOGGETTO:
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto(sub.getSoggetto().getCodiceSoggetto());
		impegno.setSoggetto(soggetto);
		
		
		
		
		impegno.setParereFinanziario(sub.getParereFinanziario());

		impegno.setCodPdc(datiImpegno.getPianoDeiConti().getCodice()); log.debug("","PdC -*-> " + datiImpegno.getPianoDeiConti().getCodice());
			
		impegno.setCodSiope(datiImpegno.getSiopeSpesa().getCodice()); log.debug("","Spesa -*-> " + datiImpegno.getSiopeSpesa().getCodice());
			
		impegno.setCodCofog(datiImpegno.getCofogSelezionato());
		impegno.setCodTransazioneEuropeaSpesa(datiImpegno.getTransazioneEuropeaSelezionato());
		impegno.setCodRicorrenteSpesa(datiImpegno.getRicorrenteSpesaSelezionato());
		impegno.setCodCapitoloSanitarioSpesa(datiImpegno.getPerimetroSanitarioSpesaSelezionato());
		impegno.setCodPrgPolReg(datiImpegno.getPoliticaRegionaleSelezionato());
		impegno.setCodClassGen11(datiImpegno.getClassGenSelezionato1());
		impegno.setCodClassGen12(datiImpegno.getClassGenSelezionato2());
		impegno.setCodClassGen13(datiImpegno.getClassGenSelezionato3());
		impegno.setCodClassGen14(datiImpegno.getClassGenSelezionato4());
		impegno.setCodClassGen15(datiImpegno.getClassGenSelezionato5());
		
		
		//SIOPE PLUS:
		impostaDatiSiopePlusPerInserisciAggiorna(datiImpegno.getStep1Model(), impegno);
		//END SIOPE PLUS
		impegno.setNumeroTotaleModifcheMovimento(sub.getNumeroTotaleModifcheMovimento());
		

		return impegno;
	}
	
	
	@Override
	public boolean oggettoDaPopolareImpegno(){
		return false;
	}
	
	private List<Errore> checkProvvedimentoInAggiorna(List<Errore> erroriSalvataggio, Integer uidAtto){
		RicercaAtti ricercaAtti = new RicercaAtti();
		RicercaProvvedimento ricProvvReq = new RicercaProvvedimento();
		ricProvvReq.setEnte(model.getEnte());
		ricercaAtti.setUid(uidAtto);
		ricProvvReq.setRicercaAtti(ricercaAtti);
		ricProvvReq.setRichiedente(sessionHandler.getRichiedente());
		RicercaProvvedimentoResponse resp = provvedimentoService.ricercaProvvedimento(ricProvvReq);
		if(resp.isFallimento()){
			Errore err = new Errore();
			err.setCodice(ErroreCore.ERRORE_DI_SISTEMA.getErrore("").getCodice());
			err.setDescrizione(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Errore nella ricerca del provvedimento associato").getDescrizione());
			erroriSalvataggio.add(err);
			return erroriSalvataggio;
		}
		
		if(resp.getListaAttiAmministrativi()!=null && !resp.getListaAttiAmministrativi().isEmpty()){
			AttoAmministrativo atto = resp.getListaAttiAmministrativi().get(0);
			if(atto.getBloccoRagioneria() != null && atto.getBloccoRagioneria()==true){
				Errore err = new Errore();
				err.setCodice(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore(atto.getOggetto()).getCodice());
				err.setDescrizione(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore(atto.getOggetto()).getDescrizione());
				erroriSalvataggio.add(err);
				return erroriSalvataggio;
			}
			
		}
		
		return erroriSalvataggio;
	
	}

	//SIAC-7349 - SR90 - MR - 03/2020 - Metodo per ottenere la disponibilita della componente
	private BigDecimal getDisponibilitaModifica(int i) {

		if(model.getImportiComponentiCapitolo() == null || model.getImportiComponentiCapitolo().isEmpty()){
			return BigDecimal.ZERO;	
		}else{
			if(i==0){				
			if(model.getImportiComponentiCapitolo().get(2) != null 
					&& model.getImportiComponentiCapitolo().get(2).getDettaglioAnno0() !=null &&
					model.getImportiComponentiCapitolo().get(2).getDettaglioAnno0().getImporto() != null){
				return  model.getImportiComponentiCapitolo().get(2).getDettaglioAnno0().getImporto();					
			}else{
					return BigDecimal.ZERO;	
				}				
			}else if(i==1){
				if(model.getImportiComponentiCapitolo().get(2) != null 
					&& model.getImportiComponentiCapitolo().get(2).getDettaglioAnno1() !=null &&
					model.getImportiComponentiCapitolo().get(2).getDettaglioAnno1().getImporto() != null){
					return  model.getImportiComponentiCapitolo().get(2).getDettaglioAnno1().getImporto();					
				}else{
					return BigDecimal.ZERO;	
					}
			}else if (i==2){
				if(model.getImportiComponentiCapitolo().get(2) != null 
						&& model.getImportiComponentiCapitolo().get(2).getDettaglioAnno2() !=null &&
						model.getImportiComponentiCapitolo().get(2).getDettaglioAnno2().getImporto() != null){
						return  model.getImportiComponentiCapitolo().get(2).getDettaglioAnno2().getImporto();					
				}else{
					return BigDecimal.ZERO;	
				}
			}			
		}			
			return BigDecimal.ZERO;	
	}
	//SIAC-7349 - End
	
	//SIAC-8271
	private void impostaImportoAttualeSuSub() {
		
		for (SubImpegno subImpegno : model.getListaSubimpegni()) {
			if(subImpegno.getUid() == model.getSubimpegnoInAggiornamento().getUid()) {
				subImpegno.setImportoAttuale(model.getSubimpegnoInAggiornamento().getImportoAttuale());
			}
		}
		
	}
	
	public Boolean getFlagValido() {
		return flagValido;
	}

	public void setFlagValido(Boolean flagValido) {
		this.flagValido = flagValido;
	}

	public Boolean getFlagSoggettoValido() {
		return flagSoggettoValido;
	}

	public void setFlagSoggettoValido(Boolean flagSoggettoValido) {
		this.flagSoggettoValido = flagSoggettoValido;
	}

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

	public Boolean getAbilitaAzioneInserimentoProvvedimento() {
		return abilitaAzioneInserimentoProvvedimento;
	}

	public void setAbilitaAzioneInserimentoProvvedimento(Boolean abilitaAzioneInserimentoProvvedimento) {
		this.abilitaAzioneInserimentoProvvedimento = abilitaAzioneInserimentoProvvedimento;
	}

	public String getMinImp() {
		return minImp;
	}

	public void setMinImp(String minImp) {
		this.minImp = minImp;
	}

	public String getMaxImp() {
		return maxImp;
	}

	public void setMaxImp(String maxImp) {
		this.maxImp = maxImp;
	}

	public String getMinSub() {
		return minSub;
	}

	public void setMinSub(String minSub) {
		this.minSub = minSub;
	}

	public String getMaxSub() {
		return maxSub;
	}

	public void setMaxSub(String maxSub) {
		this.maxSub = maxSub;
	}

	public String getMinAnche() {
		return minAnche;
	}

	public void setMinAnche(String minAnche) {
		this.minAnche = minAnche;
	}

	public String getMaxAnche() {
		return maxAnche;
	}

	public void setMaxAnche(String maxAnche) {
		this.maxAnche = maxAnche;
	}

	public String getTipoImpegno() {
		return tipoImpegno;
	}

	public void setTipoImpegno(String tipoImpegno) {
		this.tipoImpegno = tipoImpegno;
	}

	public String getImportoImpegnoModImp() {
		return importoImpegnoModImp;
	}

	public void setImportoImpegnoModImp(String importoImpegnoModImp) {
		this.importoImpegnoModImp = importoImpegnoModImp;
	}

	public String getImportoAttualeSubImpegno() {
		return importoAttualeSubImpegno;
	}

	public void setImportoAttualeSubImpegno(String importoAttualeSubImpegno) {
		this.importoAttualeSubImpegno = importoAttualeSubImpegno;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<String> getTipoImpegnoModificaImportoList() {
		return tipoImpegnoModificaImportoList;
	}

	public void setTipoImpegnoModificaImportoList(List<String> tipoImpegnoModificaImportoList) {
		this.tipoImpegnoModificaImportoList = tipoImpegnoModificaImportoList;
	}

	public List<String> getNumeroSubImpegnoList() {
		return numeroSubImpegnoList;
	}

	public void setNumeroSubImpegnoList(List<String> numeroSubImpegnoList) {
		this.numeroSubImpegnoList = numeroSubImpegnoList;
	}

	public List<String> getAbbinaList() {
		return abbinaList;
	}

	public void setAbbinaList(List<String> abbinaList) {
		this.abbinaList = abbinaList;
	}

	public String getAbbina() {
		return abbina;
	}

	public void setAbbina(String abbina) {
		this.abbina = abbina;
	}

	public String getAbbinaChk() {
		return abbinaChk;
	}

	public void setAbbinaChk(String abbinaChk) {
		this.abbinaChk = abbinaChk;
	}

	public boolean isSubImpegnoSelected() {
		return subImpegnoSelected;
	}

	public void setSubImpegnoSelected(boolean subImpegnoSelected) {
		this.subImpegnoSelected = subImpegnoSelected;
	}

	public String getSubSelected() {
		return subSelected;
	}

	public void setSubSelected(String subSelected) {
		this.subSelected = subSelected;
	}

	public String getAnniPluriennali() {
		return anniPluriennali;
	}

	public void setAnniPluriennali(String anniPluriennali) {
		this.anniPluriennali = anniPluriennali;
	}

	public String getIdTipoMotivo() {
		return idTipoMotivo;
	}

	public void setIdTipoMotivo(String idTipoMotivo) {
		this.idTipoMotivo = idTipoMotivo;
	}

	public String getNumeroSubImpegno() {
		return numeroSubImpegno;
	}

	public void setNumeroSubImpegno(String numeroSubImpegno) {
		this.numeroSubImpegno = numeroSubImpegno;
	}

	public String getMinImportoCalcolato() {
		return minImportoCalcolato;
	}

	public void setMinImportoCalcolato(String minImportoCalcolato) {
		this.minImportoCalcolato = minImportoCalcolato;
	}

	public String getMaxImportoCalcolato() {
		return maxImportoCalcolato;
	}

	public void setMaxImportoCalcolato(String maxImportoCalcolato) {
		this.maxImportoCalcolato = maxImportoCalcolato;
	}

	public String getIdSub() {
		return idSub;
	}

	public void setIdSub(String idSub) {
		this.idSub = idSub;
	}

	public String getMinImportoSubCalcolato() {
		return minImportoSubCalcolato;
	}

	public void setMinImportoSubCalcolato(String minImportoSubCalcolato) {
		this.minImportoSubCalcolato = minImportoSubCalcolato;
	}

	public String getMaxImportoSubCalcolato() {
		return maxImportoSubCalcolato;
	}

	public void setMaxImportoSubCalcolato(String maxImportoSubCalcolato) {
		this.maxImportoSubCalcolato = maxImportoSubCalcolato;
	}

	public BigDecimal getMinImportoImpegnoApp() {
		return minImportoImpegnoApp;
	}

	public void setMinImportoImpegnoApp(BigDecimal minImportoImpegnoApp) {
		this.minImportoImpegnoApp = minImportoImpegnoApp;
	}

	public BigDecimal getMaxImportoImpegnoApp() {
		return maxImportoImpegnoApp;
	}

	public void setMaxImportoImpegnoApp(BigDecimal maxImportoImpegnoApp) {
		this.maxImportoImpegnoApp = maxImportoImpegnoApp;
	}

	public BigDecimal getMinImportoSubApp() {
		return minImportoSubApp;
	}

	public void setMinImportoSubApp(BigDecimal minImportoSubApp) {
		this.minImportoSubApp = minImportoSubApp;
	}

	public BigDecimal getMaxImportoSubApp() {
		return maxImportoSubApp;
	}

	public void setMaxImportoSubApp(BigDecimal maxImportoSubApp) {
		this.maxImportoSubApp = maxImportoSubApp;
	}
	public BigDecimal getMinImportoImpegnoCompApp() {
		return minImportoImpegnoCompApp;
	}

	public void setMinImportoImpegnoCompApp(BigDecimal minImportoImpegnoCompApp) {
		this.minImportoImpegnoCompApp = minImportoImpegnoCompApp;
	}

	public BigDecimal getMaxImportoImpegnoCompApp() {
		return maxImportoImpegnoCompApp;
	}

	public void setMaxImportoImpegnoCompApp(BigDecimal maxImportoImpegnoCompApp) {
		this.maxImportoImpegnoCompApp = maxImportoImpegnoCompApp;
	}
	public AttoAmministrativo getAm() {
		return am;
	}

	public void setAm(AttoAmministrativo am) {
		this.am = am;
	}

	public MotiviReimputazioneROR[] getMotiviRorReimputazione() {
		return motiviRorReimputazione;
	}

	public void setMotiviRorReimputazione(MotiviReimputazioneROR[] motiviRorReimputazione) {
		this.motiviRorReimputazione = motiviRorReimputazione;
	}

	public MotiviCancellazioneROR[] getMotiviRorCancellazione() {
		return motiviRorCancellazione;
	}

	public void setMotiviRorCancellazione(MotiviCancellazioneROR[] motiviRorCancellazione) {
		this.motiviRorCancellazione = motiviRorCancellazione;
	}

	public MotiviMantenimentoROR[] getMotiviRorMantenimento() {
		return motiviRorMantenimento;
	}

	public void setMotiviRorMantenimento(MotiviMantenimentoROR[] motiviRorMantenimento) {
		this.motiviRorMantenimento = motiviRorMantenimento;
	}

	public CodificaFin getReimputazione() {
		return reimputazione;
	}

	public void setReimputazione(CodificaFin reimputazione) {
		this.reimputazione = reimputazione;
	}

	public String getDescrizioneMotivoReimp() {
		return descrizioneMotivoReimp;
	}

	public void setDescrizioneMotivoReimp(String descrizioneMotivoReimp) {
		this.descrizioneMotivoReimp = descrizioneMotivoReimp;
	}

	public String getDescrizioneMotivoRorCancellazioneInsussistenza() {
		return descrizioneMotivoRorCancellazioneInsussistenza;
	}

	public void setDescrizioneMotivoRorCancellazioneInsussistenza(
			String descrizioneMotivoRorCancellazioneInsussistenza) {
		this.descrizioneMotivoRorCancellazioneInsussistenza = descrizioneMotivoRorCancellazioneInsussistenza;
	}
	
	//SIAC-7349 Inizio  SR190 CM 20/05/2020
	public boolean isInImpegno() {
		return inImpegno;
	}

	public void setInImpegno(boolean inImpegno) {
		this.inImpegno = inImpegno;
	}
	
	public List<ModificaMovimentoGestioneSpesa> getListaModificheSpeseCollegata() {
		return listaModificheSpeseCollegata;
	}

	public void setListaModificheSpeseCollegata(List<ModificaMovimentoGestioneSpesa> listaModificheSpeseCollegata) {
		this.listaModificheSpeseCollegata = listaModificheSpeseCollegata;
	}
	//SIAC-7349 Inizio  SR190 CM 20/05/2020

	public boolean isInInserimento() {
		return inInserimento;
	}

	public void setInInserimento(boolean inInserimento) {
		this.inInserimento = inInserimento;
	}

	public List<GestioneCruscottoModel> getListaReimputazioneTriennio() {
		return listaReimputazioneTriennio;
	}

	public void setListaReimputazioneTriennio(List<GestioneCruscottoModel> listaReimputazioneTriennio) {
		this.listaReimputazioneTriennio = listaReimputazioneTriennio;
	}

	public Integer getIndiceRimozioneAnno() {
		return indiceRimozioneAnno;
	}

	public void setIndiceRimozioneAnno(Integer indiceRimozioneAnno) {
		this.indiceRimozioneAnno = indiceRimozioneAnno;
	}

	public GestioneCruscottoModel getRorCancellazioneInesigibilita() {
		return rorCancellazioneInesigibilita;
	}

	public void setRorCancellazioneInesigibilita(GestioneCruscottoModel rorCancellazioneInesigibilita) {
		this.rorCancellazioneInesigibilita = rorCancellazioneInesigibilita;
	}

	public GestioneCruscottoModel getRorCancellazioneInsussistenza() {
		return rorCancellazioneInsussistenza;
	}

	public void setRorCancellazioneInsussistenza(GestioneCruscottoModel rorCancellazioneInsussistenza) {
		this.rorCancellazioneInsussistenza = rorCancellazioneInsussistenza;
	}

	public GestioneCruscottoModel getRorDaMantenere() {
		return rorDaMantenere;
	}

	public void setRorDaMantenere(GestioneCruscottoModel rorDaMantenere) {
		this.rorDaMantenere = rorDaMantenere;
	}

	public CodificaFin getCancellazioneInsussistenza() {
		return cancellazioneInsussistenza;
	}

	public void setCancellazioneInsussistenza(CodificaFin cancellazioneInsussistenza) {
		this.cancellazioneInsussistenza = cancellazioneInsussistenza;
	}

	public CodificaFin getCancellazioneInesigibilita() {
		return cancellazioneInesigibilita;
	}

	public void setCancellazioneInesigibilita(CodificaFin cancellazioneInesigibilita) {
		this.cancellazioneInesigibilita = cancellazioneInesigibilita;
	}

	public CodificaFin getMantenere() {
		return mantenere;
	}

	public void setMantenere(CodificaFin mantenere) {
		this.mantenere = mantenere;
	}

	public String getDescrizioneMotivoRorCancellazioneInesigibilita() {
		return descrizioneMotivoRorCancellazioneInesigibilita;
	}

	public void setDescrizioneMotivoRorCancellazioneInesigibilita(
			String descrizioneMotivoRorCancellazioneInesigibilita) {
		this.descrizioneMotivoRorCancellazioneInesigibilita = descrizioneMotivoRorCancellazioneInesigibilita;
	}

	public String getDescrizioneMotivoRorMantenere() {
		return descrizioneMotivoRorMantenere;
	}

	public void setDescrizioneMotivoRorMantenere(String descrizioneMotivoRorMantenere) {
		this.descrizioneMotivoRorMantenere = descrizioneMotivoRorMantenere;
	}

	public List<ModificaMovimentoGestioneSpesa> getListaModificheRor() {
		return listaModificheRor;
	}

	public void setListaModificheRor(List<ModificaMovimentoGestioneSpesa> listaModificheRor) {
		this.listaModificheRor = listaModificheRor;
	}

	public List<ModificaMovimentoGestioneSpesa> getListaModificheRorDaAggiornare() {
		return listaModificheRorDaAggiornare;
	}

	public void setListaModificheRorDaAggiornare(List<ModificaMovimentoGestioneSpesa> listaModificheRorDaAggiornare) {
		this.listaModificheRorDaAggiornare = listaModificheRorDaAggiornare;
	}

	public Boolean getInesigibilitaRor() {
		return inesigibilitaRor;
	}

	public void setInesigibilitaRor(Boolean inesigibilitaRor) {
		this.inesigibilitaRor = inesigibilitaRor;
	}

	public Boolean getInsussistenzaRor() {
		return insussistenzaRor;
	}

	public void setInsussistenzaRor(Boolean insussistenzaRor) {
		this.insussistenzaRor = insussistenzaRor;
	}

	public Boolean getDaMantenerePresente() {
		return daMantenerePresente;
	}

	public void setDaMantenerePresente(Boolean daMantenerePresente) {
		this.daMantenerePresente = daMantenerePresente;
	}

	public String getUidModifica() {
		return uidModifica;
	}

	public void setUidModifica(String uidModifica) {
		this.uidModifica = uidModifica;
	}

	public boolean isDisabledDaManterenere() {
		return disabledDaManterenere;
	}

	public void setDisabledDaManterenere(boolean disabledDaManterenere) {
		this.disabledDaManterenere = disabledDaManterenere;
	}

	public boolean isAbilitaPropagaDaSub() {
		return abilitaPropagaDaSub;
	}

	public void setAbilitaPropagaDaSub(boolean abilitaPropagaDaSub) {
		this.abilitaPropagaDaSub = abilitaPropagaDaSub;
	}

	public boolean isModificheDaPropagarePieno() {
		return modificheDaPropagarePieno;
	}

	public void setModificheDaPropagarePieno(boolean modificheDaPropagarePieno) {
		this.modificheDaPropagarePieno = modificheDaPropagarePieno;
	}
	
	public Boolean getCheckDaMantenere() {
		return checkDaMantenere == null ? false : checkDaMantenere;
	}

	public void setCheckDaMantenere(Boolean checkDaMantenere) {
		this.checkDaMantenere = checkDaMantenere;
	}
}

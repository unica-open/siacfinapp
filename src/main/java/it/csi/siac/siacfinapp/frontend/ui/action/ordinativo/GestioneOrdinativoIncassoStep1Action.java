/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaContoVincolatoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaContoVincolatoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiSottoContiVincolatiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiSottoContiVincolatiCapitoloResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ImportiCapitoloModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.CausaleEntrataTendinoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.VerificaBloccoRORHelper;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.CommissioneDocumento;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.TipoAssociazioneEmissione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestioneOrdinativoIncassoStep1Action extends ActionKeyGestioneOrdinativoIncassoAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Queste variabili sono l'entry point per aggiorna:
	private String numeroOrdinativo;
	private String annoOrdinativo;
	
	private String numeroOrdinativoDaPassare;
	private String annoOrdinativoDaPassare;
	
	//Queste variabili sono l'entry point per inserimento da ripeti ordinativo esistente:
	private String numeroOrdinativoRipeti;
	private String annoOrdinativoRipeti;
	private String ripeti;
	//
	
	private String strutturaDaInserimento;

	public Integer idOrdinativo;
	public String doveMiTrovo;
	//SIAC-8850
	@Autowired
	private CapitoloService capitoloService;
	
	public GestioneOrdinativoIncassoStep1Action() {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.ORDINATIVO_INCASSO;
	}

	/**
	 * carica combo della pagina
	 */
	@SuppressWarnings("unchecked")
	private void caricaListeComboIncasso() {
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(
				TipiLista.DISTINTA_ENTRATA, TipiLista.CONTO_TESORERIA,
				TipiLista.CODICE_BOLLO, TipiLista.COMMISSIONI,
				TipiLista.AVVISO, TipiLista.NOTE_TESORIERE);              
		
		model.getGestioneOrdinativoStep1Model().setListaDistinta(((List<CodificaFin>) mappaListe.get(TipiLista.DISTINTA_ENTRATA)));
		model.getGestioneOrdinativoStep1Model().setListaContoTesoriere(((List<CodificaFin>) mappaListe.get(TipiLista.CONTO_TESORERIA)));
		model.getGestioneOrdinativoStep1Model().setListaBollo(((List<CodificaFin>) mappaListe.get(TipiLista.CODICE_BOLLO)));
		model.getGestioneOrdinativoStep1Model().setListaCommissioni(((List<CodificaFin>) mappaListe.get(TipiLista.COMMISSIONI)));
		model.getGestioneOrdinativoStep1Model().setListaAvviso(((List<CodificaFin>) mappaListe.get(TipiLista.AVVISO)));
		model.getGestioneOrdinativoStep1Model().setListaNoteTesoriere(((List<CodificaFin>) mappaListe.get(TipiLista.NOTE_TESORIERE)));
	}

	/**
	 * Si occupa di caricare la lista dei tipi causale entrata chiamando
	 * il relativo servizio
	 */
	protected void caricaTipiCausaleEntrata(){
		CausaleEntrataTendinoModel modelCausali = model.getGestioneOrdinativoStep1Model().getCausaleEntrataTendino();
		if(modelCausali==null){
			//inizializzazione del model
			model.getGestioneOrdinativoStep1Model().setCausaleEntrataTendino(new CausaleEntrataTendinoModel());
			modelCausali = model.getGestioneOrdinativoStep1Model().getCausaleEntrataTendino();
		}
		caricaTipiCausaleEntrata(modelCausali);
	}
	
	/**
	 * Gestisce l'evento di selezione di un elemento nel tendino del tipo causale
	 * @return
	 */
	public String tipoCausaleEntrataChanged(){
		CausaleEntrataTendinoModel modelTendiniCausali = model.getGestioneOrdinativoStep1Model().getCausaleEntrataTendino();
		//chiamo il metodo comune:
		tipoCausaleEntrataChanged(modelTendiniCausali);
		//
		return "tendinoCausali";//ritorno il redirect specifico di questo caso d'uso.
	}
	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		this.model.setTitolo("Gestione Ordinativo Incasso");
		
		//caricamento dei dati per la scelta del tipo causale:
		caricaTipiCausaleEntrata();
		

		if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
			model.getGestioneOrdinativoStep1Model().getCapitolo().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
			model.getCapitoloRicerca().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
		}
		caricaListePerRicercaSoggetto();
		if (model.getListaTipiProvvedimenti() == null || model.getListaTipiProvvedimenti().size() == 0) {
			caricaTipiProvvedimenti();
		}
		
		//liste combo:
		caricaListeComboIncasso();
		
		// SIAC-5933 - SIAC-6029 (va chiamato appena dopo liste combo):
		impostaDefaultContoTesoriere(sonoInAggiornamentoIncasso());

		// se vuoto vuol dire che primo giro e quindi resetto eventuali dati di
		// TE
		if (teSupport == null) {
			pulisciTransazioneElementare();
		}
		// resetto i checkbox del primo step e inizializzo a false per struts
		if (!model.getGestioneOrdinativoStep1Model().getOrdinativo().isFlagAllegatoCartaceo()) {
			model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagAllegatoCartaceo(false);
		}

		if (!model.getGestioneOrdinativoStep1Model().getOrdinativo().isFlagBeneficiMultiplo()) {
			model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagBeneficiMultiplo(false);
		}

		if (!model.getGestioneOrdinativoStep1Model().getOrdinativo().isFlagCopertura()) {
			model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagCopertura(false);
		}
		/* SIAC-7399
		 * Setting della variabile per invalidare
		 * il controllo sul blocco ragioneria
		 */
		model.setSkipControlloBloccoRagioneria(true);
		
		
		
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {

		//PRIMA COSA DA FARE, SET SE SONO IN AGGIORNAMENTO:
		// Vado a settare la variabile per vedere se sono in inserimento o
		// aggiornamento
		if (!model.isSonoInAggiornamentoIncasso()) {
			if (getAnnoOrdinativo() != null && getNumeroOrdinativo() != null) {
				model.setAnnoOrdinativoInAggiornamento(getAnnoOrdinativo());
				model.setNumeroOrdinativoInAggiornamento(getNumeroOrdinativo());
				model.setSonoInAggiornamentoIncasso(true);
			} else {
				model.setSonoInAggiornamentoIncasso(false);
			}
		}
		//END SET SONO IN AGGIORNAMENTO O MENO
		
		// controlli
		// Controllo stato bilancio
		controlloStatoBilancio(sessionHandler.getAnnoBilancio(),"GESTIONE", "ORDINATIVO INCASSO");
		
		//task-218
		model.setAnnoOrdinativoInAggiornamento(getAnnoOrdinativo());
		model.setNumeroOrdinativoInAggiornamento(getNumeroOrdinativo());

		//Controlliamo l'abilitazione:
		if(sonoInAggiornamentoIncasso()){
			//aggiornamento
			if(!isAzioneConsentita(AzioneConsentitaEnum.OP_ENT_AGGORDINC)){
				//non e' abilitato
				addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
			}
		} else {
			if(!isAzioneConsentita(AzioneConsentitaEnum.OP_ENT_insOrdInc)){
				//non e' abilitato
				addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
			}
		}
		//fine controllo abilitazione

		boolean inserimentoOrdinativo = true;
		if (idOrdinativo != null && idOrdinativo != 0) {
			inserimentoOrdinativo = false;
		}

		if (teSupport == null) {
			pulisciTransazioneElementare();
		}

		teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.ORDINATIVO_INCASSO.toString());
		if (teSupport.getPianoDeiConti() == null) {
			teSupport.setPianoDeiConti(model.getGestioneOrdinativoStep1Model().getCapitolo().getElementoPianoDeiConti());
		}
		caricaLabelsInserisci(1, inserimentoOrdinativo);

		if (caricaListeBil(WebAppConstants.CAP_EG)) {
			return INPUT;
		}

		// ORDINATIVO_TIPO_PAGAMENTO
		// Jira - 1357 in caso di errore di caricamento dei dati
		// dei classificatori non viene segnalato alcun errore
		// ma carica la pagina, al massimo non verranno visualizzate le combo
		// relative
		caricaListeFinOrdinativo(TIPO_ORDINATIVO_INCASSO_I);

		if(model.isSonoInAggiornamentoIncasso()){
			// CARICA DATI QUANDO ENTRO IN AGIGORNAMENTO
			if (model.isForceReloadAgiornamentoOrdinativoIncasso()) {
				List<ImportiCapitoloModel> listaVuota = new ArrayList<ImportiCapitoloModel>();
				model.getGestioneOrdinativoStep1Model().getCapitolo().setImportiCapitolo(listaVuota);
				caricaOrdinativoIncasso();
			}
		}
		
		//CR 746 - RIPETI
		if("si".equals(ripeti) && !FinStringUtils.isEmpty(annoOrdinativoRipeti) && !FinStringUtils.isEmpty(numeroOrdinativoRipeti)){
			caricaOrdinativoIncassoPerRipeti(annoOrdinativoRipeti, numeroOrdinativoRipeti);
		}
		
		
		return SUCCESS;
	}
	
	/**
	 * Gestisce l'aggiunta di un nuovo ordinativo di pagamento alla datatable
	 * degli ordinativi di pagamento collegati all'ordinativi di incasso
	 * 
	 * @return
	 * @throws Exception
	 */
	public String aggiungiOrdinativoDaCollegare() throws Exception {
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		model.getGestioneOrdinativoStep1Model().setApriTabOrdinativiCollegati(true);
		
		OrdinativoPagamento op = model.getGestioneOrdinativoStep1Model().getOrdinativo();
		// controlla dati
		if(model.getGestioneOrdinativoStep1Model().getNumeroOrdinativoDaCollegare() == null){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero ordinativo"));
		}else{
			// controlli da eseguire:
			// 1- che non sia gia' presente nella lista
			// 2- che la somma dei collegati non superi ll'importo dell'op
			Integer numeroOrdinativoDaCollegare = model.getGestioneOrdinativoStep1Model().getNumeroOrdinativoDaCollegare();
						
			if(op.getElencoOrdinativiCollegati()!=null && !op.getElencoOrdinativiCollegati().isEmpty()){
				for (it.csi.siac.siacfinser.model.ordinativo.Ordinativo oi : op.getElencoOrdinativiCollegati()) {
					if(numeroOrdinativoDaCollegare.equals(oi.getNumero())){
						addActionError(ERRORE_ORDINATIVO_COLLEGATO_PRESENTE);
						return INPUT;
					}
				}
			}
			
			// se presente recupero l'ordinativo lato ser
			RicercaOrdinativo reqOi = new RicercaOrdinativo();
			
			reqOi.setBilancio(sessionHandler.getBilancio());
			reqOi.setRichiedente(sessionHandler.getRichiedente());
			reqOi.setEnte(sessionHandler.getEnte());
			reqOi.setNumPagina(1);
			reqOi.setNumRisultatiPerPagina(1);
			
			ParametroRicercaOrdinativoPagamento parametri = new ParametroRicercaOrdinativoPagamento();
			parametri.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
			parametri.setNumeroOrdinativoDa( BigInteger.valueOf(numeroOrdinativoDaCollegare));
			
			
			//CODICE CREDITORE:
			//parametri.setCodiceCreditore(model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore());
						
			reqOi.setParametroRicercaOrdinativoPagamento(parametri);
			
			RicercaOrdinativoResponse resOi = ordinativoService.ricercaOrdinativoPagamento(reqOi);
			
			if(resOi == null){
				listaErrori.add(ErroreCore.ERRORE_DI_SISTEMA.getErrore("nella ricerca dell'ordinativo di pagamento numero: "+ numeroOrdinativoDaCollegare));
				addErrori(listaErrori);
				return INPUT;
			}
			
			if(resOi!=null && (resOi.isFallimento() || isEmpty(resOi.getElencoOrdinativoPagamento())) ){
				//dato che non c'e' piu' il soggetto come parametro commento:
				//listaErrori.add(ErroreCore.ENTITA_NON_TROVATA.getErrore("ordinativo", " numero " +numeroOrdinativoDaCollegare+" e soggetto "+parametri.getCodiceCreditore()));
				//e al suo posto:
				listaErrori.add(ErroreCore.ENTITA_NON_TROVATA.getErrore("ordinativo", " numero " +numeroOrdinativoDaCollegare));
				addErrori(listaErrori);
				return INPUT;
			}
			
			OrdinativoPagamento oi = resOi.getElencoOrdinativoPagamento().get(0);
			String codStato = oi.getCodStatoOperativoOrdinativo();
			if(!CostantiFin.D_ORDINATIVO_STATO_QUIETANZATO.equalsIgnoreCase(codStato) && !CostantiFin.D_ORDINATIVO_STATO_ANNULLATO.equalsIgnoreCase(codStato)){
				listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("ordinativo " + numeroOrdinativoDaCollegare, " stato diverso da QUIETANZATO o ANNULLATO"));
				addErrori(listaErrori);
				return INPUT;
			}
			
			
			// controllo subito se ha dei collegati, se li ha non e' ammesso
//			if(!FinStringUtils.isEmpty(oi.getElencoOrdinativiCollegati())){
//				addActionError(ERRORE_ORDINATIVI_COLLEGATI_ENTRATA);
//				return INPUT;
//			}
			
			// Per il controllo dell'importo devo prima ricercare l'ordinativo indicato
			// se l'importo totale e' ammesso lo aggiungo 
			
			
			BigDecimal totaleImportoCollegatiAggiornato = model.getGestioneOrdinativoStep1Model().getTotaleImportoCollegati().add(oi.getImportoOrdinativo());
			
			/*
			if(op.getImportoOrdinativo().compareTo(totaleImportoCollegatiAggiornato) < 0){
				//model.getGestioneOrdinativoStep1Model().setAbilitatoACollegareOrdinativiNuovi(false);
				addActionError(ERRORE_TOTALE_IMPORTO_COLLEGATI);
				return INPUT;
			}*/
				
			//OK AGGIUNGO L'ORDINATIVO:
			if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getElencoOrdinativiCollegati()==null ){
				model.getGestioneOrdinativoStep1Model().getOrdinativo().setElencoOrdinativiCollegati(new ArrayList<Ordinativo>());
			}
			
			//setto il tipo di associazione
			oi.setTipoAssociazioneEmissione(TipoAssociazioneEmissione.REI_ORD);
			model.getGestioneOrdinativoStep1Model().getOrdinativo().getElencoOrdinativiCollegati().add(oi);
			model.getGestioneOrdinativoStep1Model().setTotaleImportoCollegati(totaleImportoCollegatiAggiornato);
			model.getGestioneOrdinativoStep1Model().setAbilitatoACollegareOrdinativiNuovi(true);
			//	
			
			
		}
		
		
		if(!listaErrori.isEmpty() || hasActionErrors()){
			// apro il tab vincolo
			addErrori(listaErrori);
			return INPUT;
		}
		
		
		// sposta l'ancora all'altezza dei vincoli
		model.getGestioneOrdinativoStep1Model().setPortaAdAltezzaOrdinativiCollegati(true);
		model.getGestioneOrdinativoStep1Model().setNumeroOrdinativoDaCollegare(null);
		
		return SUCCESS;
	}
	
	/**
	 * Gestisce la cancellazione di un elemento dalla datatable
	 * degli ordinativi di pagamento collegati all'ordinativi di incasso
	 * 
	 * @return
	 * @throws Exception
	 */
	public String eliminaOrdinativoCollegato() throws Exception {
		
		OrdinativoPagamento op = model.getGestioneOrdinativoStep1Model().getOrdinativo();
		
		// controlli da eseguire:
		Integer numeroOrdinativoDaCollegare = Integer.parseInt(getNumeroOrdinativoDaPassare());
		
		List<Ordinativo> tmpElencoOrdinativiCollegati = new ArrayList<Ordinativo>();
		for (Ordinativo oi : op.getElencoOrdinativiCollegati()) {
		
			Integer numero = oi.getNumero();
			if(!numero.equals(numeroOrdinativoDaCollegare)){
				tmpElencoOrdinativiCollegati.add(oi);
			}
			
		}
		
		op.setElencoOrdinativiCollegati(tmpElencoOrdinativiCollegati);
		
		//Ricalcolo il totale! 
		BigDecimal calcoloImportoCollegati = BigDecimal.ZERO;
		
		for (Ordinativo oi : op.getElencoOrdinativiCollegati()) {
			calcoloImportoCollegati = calcoloImportoCollegati.add(oi.getImportoOrdinativo());
		}
		
		model.getGestioneOrdinativoStep1Model().setTotaleImportoCollegati(calcoloImportoCollegati);
		// sposta l'ancora all'altezza dei vincoli
		model.getGestioneOrdinativoStep1Model().setPortaAdAltezzaOrdinativiCollegati(true);
		
		return SUCCESS;
	}

	// cambiamdo codice creditore in step1 intercetta il cambio e fa il refresh
	// della tabella modpagamento
	public String sediIncasso() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String cod = request.getParameter("id");
		caricaListeCreditore(cod);

		return SEDISECONDARIE;
	}
	
	/*
	 * aggiorna la tabella modalita di pagamento su selezione della sede e aggiorna quindi la lista sedi associate alla modalita' di pagamento scelta(non-Javadoc)
	 * @see it.csi.siac.siacfinapp.frontend.ui.action.ordinativo.WizardGestioneOrdinativoAction#remodpagamento()
	 */
	@Override
	public String remodpagamento(){
	 		return super.remodpagamento();
	}

	/**
	 * stabilisce se il campo della causale risulta editabile o meno
	 * @return
	 */
	public boolean abilitaCausale() {
		boolean abilitata = false;
		if (!model.isSonoInAggiornamentoIncasso()) {
			//sono in inserimento e quindi e' sempre abilitata
			abilitata = true;
		} else {
			//sono in aggiornamento
			StatoOperativoOrdinativo stato = model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo();
			if(StatoOperativoOrdinativo.TRASMESSO.equals(stato) || StatoOperativoOrdinativo.INSERITO.equals(stato)){
				//in aggiornamento e' abilitata se lo stato e' inserito oppure trasmesso
				abilitata = true;
			}
		}
		return abilitata;
	}
	
	public boolean disabilitaCheckAggiornamento() {
		if (!model.isSonoInAggiornamentoIncasso()) {
			// Inserimento
			if (isAzioneConsentita(AzioneConsentitaEnum.OP_ENT_INS_ORD_INC_QUIETANZA)) {
				model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagCopertura(false);
				return false;
			}
		} else {
			// AGGIORNAMENTO
			if (model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo().equals(StatoOperativoOrdinativo.TRASMESSO)) {
				if (isAzioneConsentita(AzioneConsentitaEnum.OP_ENT_VARORDINC)) {
					model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagCopertura(false);
					return false;
				}
			}
			if (model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo().equals(StatoOperativoOrdinativo.QUIETANZATO)) {
				if (isAzioneConsentita(AzioneConsentitaEnum.OP_ENT_VARORDINC)) {
					model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagCopertura(false);
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public String selezionaSoggetto() {
		return selezionaSoggettoOrdinativo();
	}

	@Override
	public String resede() {
		// fuzione elenco sedi e modalita di pagamento
		return super.resede();
	}

	public boolean abilitaProvvisoriCassa() {
		// se non e' presente l'operazione OP_ENT_INS_ORD_INC_QUIETANZA
		// puoi fare i provvisori
		return !isAzioneConsentita(AzioneConsentitaEnum.OP_ENT_INS_ORD_INC_QUIETANZA);
	}

	public String prosegui() {

		//verifico l'abilitazione rispetto 
		//al fatto di essere in aggiornamento o inserimento:
		if(sonoInAggiornamentoIncasso()){
			//aggiornamento
			if(!isAzioneConsentita(AzioneConsentitaEnum.OP_ENT_AGGORDINC)){
				addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
				return INPUT;
			}
		}else{
			//inserimento
			if(!isAzioneConsentita(AzioneConsentitaEnum.OP_ENT_insOrdInc)){
				addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
				return INPUT;
			}
		}
		

		if (controlloStatoBilancio(sessionHandler.getAnnoBilancio(),	"Gestione", "Ordinativo Incasso")) {
			return INPUT;
		}

		// valorizzazione CHekbox
		model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagAllegatoCartaceo(valorizzaCheckbox("gestioneOrdinativoStep1Model.ordinativo.flagAllegatoCartaceo"));
		model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagBeneficiMultiplo(valorizzaCheckbox("gestioneOrdinativoStep1Model.ordinativo.flagBeneficiMultiplo"));
		model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagCopertura(valorizzaCheckbox("gestioneOrdinativoStep1Model.ordinativo.flagCopertura"));
		model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagDaTrasmettere(valorizzaCheckbox("gestioneOrdinativoStep1Model.ordinativo.flagDaTrasmettere"));

		List<Errore> listaErrori = new ArrayList<Errore>();
		
		//Controlli capitolo:
		controlloCapitoloProseguiStep1(listaErrori);

		if (oggettoDaPopolarePagamento()) {
			// pagamento
			if (model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore() == null	|| "".equals(model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore())) {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice Creditore"));
			}
		} else {
			// incasso
			if (model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore() == null	|| "".equals(model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore())) {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice Debitore"));
			}
		}
		
		//CONTROLLI SU DESCRIZIONE:
		controlloDescrizioneProseguiStep1(listaErrori);
		
		
		if (model.getGestioneOrdinativoStep1Model().getProvvedimento().getIdTipoProvvedimento() == null || "".equals(model.getGestioneOrdinativoStep1Model().getProvvedimento().getIdTipoProvvedimento())) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Provvedimento"));
		}
		if (model.getGestioneOrdinativoStep1Model().getProvvedimento().getAnnoProvvedimento() == null || "".equals(model.getGestioneOrdinativoStep1Model().getProvvedimento().getAnnoProvvedimento())) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Provvedimento"));
		} else if (model.getGestioneOrdinativoStep1Model().getProvvedimento().getAnnoProvvedimento() <= 1900) {
			listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Anno provvedimento", " > 1900"));
		}
		if (model.getGestioneOrdinativoStep1Model().getProvvedimento().getNumeroProvvedimento() == null	|| "".equals(model.getGestioneOrdinativoStep1Model().getProvvedimento().getNumeroProvvedimento())) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Provvedimento"));
		} else if (model.getGestioneOrdinativoStep1Model().getProvvedimento().getNumeroProvvedimento().intValue() <= 0) {
			listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Numero provvedimento", " > 0"));
		}
		if (model.getGestioneOrdinativoStep1Model().getOrdinativo().getCodiceBollo().getCodice() == null || "".equals(model.getGestioneOrdinativoStep1Model().getOrdinativo().getCodiceBollo().getCodice())) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Bollo"));
		}
		if (model.getGestioneOrdinativoStep1Model().getOrdinativo().getContoTesoreria().getCodice() == null || "".equals(model.getGestioneOrdinativoStep1Model().getOrdinativo().getContoTesoreria().getCodice())) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Conto Tesoriere"));
		}
		
		if (model.getGestioneOrdinativoStep1Model().getOrdinativo().getDistinta().getCodice() == null || "".equals(model.getGestioneOrdinativoStep1Model().getOrdinativo().getDistinta().getCodice())) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Distinta"));
		}

		if (model.getGestioneOrdinativoStep1Model().getAnnoAccertamento() != null && model.getGestioneOrdinativoStep1Model().getNumeroAccertamento() == null) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Accertamento"));
		} else if (model.getGestioneOrdinativoStep1Model().getAnnoAccertamento() == null && model.getGestioneOrdinativoStep1Model().getNumeroAccertamento() != null) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Accertamento"));
		}

		// JIRA 1059
		if (StringUtils.isNotEmpty(getStrutturaDaInserimento())) {
			setStrutturaAmministrativaSelezionataPerOrdInc(getStrutturaDaInserimento());
		}
		
		//Controllo ordinativi inseriti in caso di flag per reintroito checked:
		Errore erroreReintroto = controlliPresenzaOrditiviPerReintroito();
		if(erroreReintroto!=null){
			listaErrori.add(erroreReintroto);
		}
		//

		String response = "";
		if (listaErrori.isEmpty()) {
			response = controlloServiziProseguiIncasso(oggettoDaPopolare);
			
			if(response.equals(INPUT)) return INPUT;
			
			// sono in inserimento
			if (!sonoInAggiornamentoIncasso()) {
				if (model.getGestioneOrdinativoStep1Model().getSoggetto() != null && model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore() != null) {

					// carico i dati del soggetto inserito in modo da verficare
					// se e' associato ad una classe
					RicercaSoggettoPerChiave rsc = new RicercaSoggettoPerChiave();

					rsc.setEnte(sessionHandler.getEnte());
					rsc.setRichiedente(sessionHandler.getRichiedente());
					ParametroRicercaSoggettoK k = new ParametroRicercaSoggettoK();
					k.setCodice(model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore());
					k.setIncludeModif(false);
					rsc.setParametroSoggettoK(k);

					RicercaSoggettoPerChiaveResponse res = soggettoService.ricercaSoggettoPerChiave(rsc);

					if (res == null || res.isFallimento()) {

						// resp.setErrori(resp.getErrori());?? non ha senso!!!!
						addErrori(res.getErrori());
						return INPUT;
					}

												
					if(!isSoggettoCoerenteConClasse(res)){
						return INPUT;
					}

					model.getGestioneOrdinativoStep1Model().setSoggettoDettaglioPerClasse(res.getSoggetto());

				}
				//SIA-7470: se l'accertamento restituito dal metodo caricaAccertamentoOrdinativo() vuol dire che lo stesso è già stato
				//recuperato e testato (ai fini del bloccoROR) tramite il tasto "cerca" quindi è già presente e non serve ricaricarlo nè controllarlo.
				//Diversamente avremo un accertamento valorizzato se al tasto "prosegui" la ricerca non è ancora stata fatta.
				//O ancora è possibile che l'accertamento non sia stato inserito poichè non obbligatorio, e quindi non è controllabile.
				Accertamento accertamento = caricaAccertamentoOrdinativo();
				if(accertamento != null){
					//SIAC-7470: controllo bloccoROR nel caso in cui sia stato premuto "prosegui" senza  passare dal "cerca"
					//SIAC-7470 (ex SIAC-6997): controllo per bloccoROR
					boolean escludi = VerificaBloccoRORHelper.escludiAccertamentoPerBloccoROR(sessionHandler.getAzioniConsentite(), accertamento, sessionHandler.getAnnoBilancio());
					if(escludi){
						listaErrori.add(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Accertamento/sub accertamento residuo non utilizzabile"));
						model.setAccertamentoTrovato(Boolean.FALSE);
						addErrori(listaErrori);
						return INPUT;
					}else if(accertamento.getElencoSubAccertamenti() != null && !accertamento.getElencoSubAccertamenti().isEmpty()){
						for(int k = 0; k < accertamento.getElencoSubAccertamenti().size(); k++){
							escludi = VerificaBloccoRORHelper.escludiAccertamentoPerBloccoROR(sessionHandler.getAzioniConsentite(), accertamento.getElencoSubAccertamenti().get(k), sessionHandler.getAnnoBilancio());
							if(escludi)
								break;
						}
						if(escludi){
							listaErrori.add(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Accertamento/sub accertamento residuo non utilizzabile"));
							model.setAccertamentoTrovato(Boolean.FALSE);
							addErrori(listaErrori);
							return INPUT;
						}
					}
				}
				
			}
			
			// SIAC-5682
			boolean warningPdcReintroito = false;
			if (!sonoInAggiornamentoIncasso()) {
				//do il warning solo in inserimento
				if(!model.isProseguiConWarningReintroitoPdc() && model.getGestioneOrdinativoStep1Model().isFlagPerReintroiti()){
					if(!"E.9.01.99.99.999".equalsIgnoreCase(model.getGestioneOrdinativoStep1Model().getCodPdcAccertamentoCercato())){
						addPersistentActionWarning("L'ordinativo per reintroiti ha un piano dei conti non conguente con il livello E.9.01.99.99.999");
						warningPdcReintroito = true;
						model.setProseguiConWarningReintroitoPdc(true);
					}
				}
			}
			//

			if (hasActionErrors() || warningPdcReintroito) {
				return INPUT;
			}

		} else {
			addErrori(listaErrori);
			return INPUT;
		}
		return response;
	}
	
	/**
	 * Pilota la visilita del fla per reintroiti
	 * @return
	 */
	public boolean cercatoAccertamentoConPianoContiV(){
		if(sonoInAggiornamentoIncasso()){
			return true;
		} else {
			//return "E.9.01.99.99.999".equals(model.getGestioneOrdinativoStep1Model().getCodicePdcAccertamentoCercato());
			//Ritorno sempre true perche' non e' piu' obbligatorio ma solo warning:
			return true;
		}
	}
	
	private void pulisciOrditiviCollegati(){
		model.getGestioneOrdinativoStep1Model().setTotaleImportoCollegati(new BigDecimal(0.0));
		model.getGestioneOrdinativoStep1Model().getOrdinativo().setElencoOrdinativiCollegati(new ArrayList<Ordinativo>());
		model.getGestioneOrdinativoStep1Model().setFlagPerReintroiti(false);
		model.getGestioneOrdinativoStep1Model().setCodPdcAccertamentoCercato(null);
	}
	
	private void controlloCapitoloProseguiStep1(List<Errore> listaErrori){
		if (model.getGestioneOrdinativoStep1Model().getCapitolo().getAnno() == null	|| "".equals(model.getGestioneOrdinativoStep1Model().getCapitolo())) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Capitolo"));
		}
		if (model.getGestioneOrdinativoStep1Model().getCapitolo().getNumCapitolo() == null	|| "".equals(model.getGestioneOrdinativoStep1Model().getCapitolo().getNumCapitolo())) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Capitolo"));
		}
		if (model.getGestioneOrdinativoStep1Model().getCapitolo().getArticolo() == null || "".equals(model.getGestioneOrdinativoStep1Model().getCapitolo().getArticolo())) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Articolo Capitolo"));
		}
	}
	
	
	/**
	 * Se l'accertamento non ha un soggetto, ma l' accertamento e' associato ad una classe soggetto, 
	 * verifico che il soggetto inserito a mano appartenga alla classe soggetto caricata.
	 */
	private boolean isSoggettoCoerenteConClasse(RicercaSoggettoPerChiaveResponse response){

		boolean esito = false;

		List<Accertamento> listaAccertamento = model.getGestioneOrdinativoStep2Model().getListaAccertamento();
		
		//Questa condizione verra' saltata se la lista accertamento e' vuota, cioe' se non e' stata fatta una ricerca puntuale dell' accertamento.
		//Oppure se l' accertamento e' legata al soggetto e non alla classe soggetto.
		if (!listaAccertamento.isEmpty() && listaAccertamento.get(0).getClasseSoggetto() != null){

			Soggetto s = response.getSoggetto();

			if(s.getElencoClass()!=null && s.getElencoClass().size()>0){
				
				List<ClasseSoggetto> listaClassificazioni = new ArrayList<ClasseSoggetto>();
				
				for(int i=0; i<s.getElencoClass().size(); i++){
					
					ClasseSoggetto cls = new ClasseSoggetto(); 
					cls.setCodice(s.getElencoClass().get(i).getSoggettoClasseCode());
					cls.setDescrizione(s.getElencoClass().get(i).getSoggettoClasseDesc());
					listaClassificazioni.add(cls);
					
				}

				if(listaClassificazioni!=null && listaClassificazioni.size()>0){
					
					for(int i=0; i<listaClassificazioni.size(); i++){
						
						if(listaClassificazioni.get(i).getCodice().equals(listaAccertamento.get(0).getClasseSoggetto().getCodice())){
							
							esito = true;
							break;
						
						}
					}
					
					if(!esito){

						if(!model.isProseguiConWarning()){	
							
							model.getGestioneOrdinativoStep1Model().setSoggettoSelezionato(Boolean.FALSE);
							model.getGestioneOrdinativoStep1Model().setHasCodiceSoggettoAccertamento(Boolean.FALSE);

							addPersistentActionWarning(ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore().getCodice() + " : " + ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore().getDescrizione());

							model.setProseguiConWarning(true);
						}
						else{
							esito = true;
						}

					}
				}
			}
			else{
				
				if(!model.isProseguiConWarning()){	
					
					model.getGestioneOrdinativoStep1Model().setSoggettoSelezionato(Boolean.FALSE);
					model.getGestioneOrdinativoStep1Model().setHasCodiceSoggettoAccertamento(Boolean.FALSE);

					addPersistentActionWarning(ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore().getCodice() + " : " + ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore().getDescrizione());

					model.setProseguiConWarning(true);
				}
				else{
					esito = true;
				}

			}

		}
		else{
			
			esito = true;
		}

		return esito;
	}
	

	// Non penso sia completa
	private String controlloServiziProseguiIncasso(
			OggettoDaPopolareEnum oggettoDaPopolare) {
		boolean erroriTrovatiNeiServizi = false;
		if (!eseguiRicercaCapitolo(model.getGestioneOrdinativoStep1Model()
				.getCapitolo(), false, oggettoDaPopolare)) {
			erroriTrovatiNeiServizi = true;
		}
		if (!model.getGestioneOrdinativoStep1Model().isSoggettoSelezionato()
				&& model.getGestioneOrdinativoStep1Model().getSoggetto()
						.getCodCreditore() != null
				&& !"".equals(model.getGestioneOrdinativoStep1Model()
						.getSoggetto().getCodCreditore())) {
			if (!eseguiRicercaSoggetto(
					convertiModelPerChiamataServizioRicerca(model
							.getGestioneOrdinativoStep1Model().getSoggetto()),
					false, oggettoDaPopolare)) {
				erroriTrovatiNeiServizi = true;
			} else {

				// carico sedi e modalita pagamento
				if (model.getGestioneOrdinativoStep1Model().getSoggetto() != null) {

					ricercaSoggettoPerChiaveOrdinativo();

				}
			}
		}

		if (!erroriTrovatiNeiServizi && model.getGestioneOrdinativoStep1Model().getListaModalitaPagamento() != null
				&& model.getGestioneOrdinativoStep1Model().getListaModalitaPagamento().size() > 0) {
			for (ModalitaPagamentoSoggetto currentModPag : model.getGestioneOrdinativoStep1Model().getListaModalitaPagamento()) {
				if (model.getGestioneOrdinativoStep1Model().getRadioModPagSelezionato() == currentModPag.getUid()) {
					model.getGestioneOrdinativoStep1Model().setModpagamentoSelezionata(currentModPag);
					break;
				}
			}
		}

		boolean erroreProvvedimento = controlloServizioProvvedimento(model.getGestioneOrdinativoStep1Model().getProvvedimento(), 
				model.getGestioneOrdinativoStep1Model().isProvvedimentoSelezionato());
		
		erroriTrovatiNeiServizi = erroriTrovatiNeiServizi || erroreProvvedimento;

		return erroriTrovatiNeiServizi ? INPUT : "prosegui";
	}

	@Override
	public String aggiornaOrdinativoIncasso() {

		// valorizzazione CHekbox
		model.getGestioneOrdinativoStep1Model()
				.getOrdinativo()
				.setFlagAllegatoCartaceo(
						valorizzaCheckbox("gestioneOrdinativoStep1Model.ordinativo.flagAllegatoCartaceo"));
		model.getGestioneOrdinativoStep1Model()
				.getOrdinativo()
				.setFlagBeneficiMultiplo(
						valorizzaCheckbox("gestioneOrdinativoStep1Model.ordinativo.flagBeneficiMultiplo"));

		String returnAggiorna = super.aggiornaOrdinativoIncasso();
		if (returnAggiorna.equalsIgnoreCase(RETURN_AGGIORNA)) {
			try {
				execute();
			} catch (Exception e) {
			}
		}
		return returnAggiorna;
	}

	public String annullaStep1() {

		if (model.isSonoInAggiornamentoIncasso()) {
			model.setAnnoOrdinativoInAggiornamento(model.getGestioneOrdinativoStep1Model().getOrdinativo().getAnno().toString());
			model.setNumeroOrdinativoInAggiornamento(model.getGestioneOrdinativoStep1Model().getOrdinativo().getNumero().toString());
			caricaOrdinativoIncasso();

		} else {

			// inserimento
			model.setAccertamentoTrovato(false);
			model.getGestioneOrdinativoStep1Model().setCapitolo(new CapitoloImpegnoModel());
			model.getGestioneOrdinativoStep1Model().setCapitoloSelezionato(false);

			if(model.getGestioneOrdinativoStep2Model().getListaAccertamento()!=null &&
					!model.getGestioneOrdinativoStep2Model().getListaAccertamento().isEmpty()){
				model.getGestioneOrdinativoStep2Model().getListaAccertamento().get(0).setClasseSoggetto(null);
			}
						
			
			model.getGestioneOrdinativoStep1Model().setSoggetto(new SoggettoImpegnoModel());
			model.getGestioneOrdinativoStep1Model().setSoggettoSelezionato(false);
			model.getGestioneOrdinativoStep1Model().setHasCodiceSoggettoAccertamento(false);

			model.getGestioneOrdinativoStep1Model().setProvvedimento(new ProvvedimentoImpegnoModel());
			model.getGestioneOrdinativoStep1Model().setProvvedimentoSelezionato(false);

			model.getGestioneOrdinativoStep1Model().getSoggetto().setCodCreditore("");
			model.getGestioneOrdinativoStep1Model().setRadioSediSecondarieSoggettoSelezionato(0);
			model.getGestioneOrdinativoStep1Model().setListaSediSecondarie(null);
			model.getGestioneOrdinativoStep1Model().setAnnoAccertamento(null);
			model.getGestioneOrdinativoStep1Model().setNumeroAccertamento(null);
			model.getGestioneOrdinativoStep1Model().setNumeroSubAcc(null);
			model.getGestioneOrdinativoStep1Model().setOrdinativo(new OrdinativoPagamento());

			// pulisco i classificatori
			teSupport.setClassGenSelezionato1("");
			teSupport.setClassGenSelezionato2("");
			teSupport.setClassGenSelezionato3("");
			teSupport.setClassGenSelezionato4("");
			teSupport.setClassGenSelezionato5("");

			// presetto i valori base
			if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
				model.getGestioneOrdinativoStep1Model().getCapitolo().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
				model.getCapitoloRicerca().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
			}
			// presetto la commissione a beneficiario BN
			if (model.getGestioneOrdinativoStep1Model().getListaCommissioni().size() > 1) {
				for (CodificaFin lista : model.getGestioneOrdinativoStep1Model().getListaCommissioni()) {
					if (lista.getCodice().equalsIgnoreCase("BN")) {
						model.getGestioneOrdinativoStep1Model().getOrdinativo().setCommissioneDocumento(new CommissioneDocumento());
						model.getGestioneOrdinativoStep1Model().getOrdinativo().getCommissioneDocumento().setCodice(BENEFICIARIO);
						break;
					}
				}
			}
		}

	   // SIAC-5933 - SIAC-6029
	   impostaDefaultContoTesoriere(sonoInAggiornamentoIncasso());
	
	   return SUCCESS;
	}

	private void pulisciCampiSuRicercaAccertamentoConFallimento() {

		// inserimento
		model.setAccertamentoTrovato(false);
		model.getGestioneOrdinativoStep1Model().setCapitolo(new CapitoloImpegnoModel());
		model.getGestioneOrdinativoStep1Model().setCapitoloSelezionato(false);

		model.getGestioneOrdinativoStep1Model().setSoggetto(new SoggettoImpegnoModel());
		model.getGestioneOrdinativoStep1Model().setSoggettoSelezionato(false);
		
		model.getGestioneOrdinativoStep1Model().setHasCodiceSoggettoAccertamento(false);

		model.getGestioneOrdinativoStep1Model().setProvvedimento(new ProvvedimentoImpegnoModel());
		model.getGestioneOrdinativoStep1Model().setProvvedimentoSelezionato(false);

		model.getGestioneOrdinativoStep1Model().getSoggetto().setCodCreditore("");
		model.getGestioneOrdinativoStep1Model().setRadioSediSecondarieSoggettoSelezionato(0);
		model.getGestioneOrdinativoStep1Model().setListaSediSecondarie(null);
		model.getGestioneOrdinativoStep1Model().setAnnoAccertamento(null);
		model.getGestioneOrdinativoStep1Model().setNumeroAccertamento(null);
		model.getGestioneOrdinativoStep1Model().setNumeroSubAcc(null);

		model.getGestioneOrdinativoStep1Model().setOrdinativo(new OrdinativoPagamento());

		// presetto i valori base
		if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
			model.getGestioneOrdinativoStep1Model().getCapitolo().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
			model.getCapitoloRicerca().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
		}

	}

	//SIAC-8892
	public void caricaStanziamento(CapitoloEntrataGestione capEG) {
		CapitoloImpegnoModel capitoloModel = new CapitoloImpegnoModel();
		capitoloModel.setImportiCapitolo(new ArrayList<ImportiCapitoloModel>());
		capitoloModel.setImportiCapitoloEG(capEG.getListaImportiCapitolo());
		capitoloModel.setAnno(capEG.getAnnoCapitolo());
		ImportiCapitoloModel supportImporti;
		for (ImportiCapitoloEG currentImporto : capEG.getListaImportiCapitoloEG()) {
			supportImporti = new ImportiCapitoloModel();
			supportImporti.setAnnoCompetenza(currentImporto.getAnnoCompetenza());
			supportImporti.setCassa(currentImporto.getStanziamentoCassa());
			supportImporti.setResiduo(currentImporto.getStanziamentoResiduo());
			supportImporti.setCompetenza(currentImporto.getStanziamento());
			// RM 08042015, commentato perchè non usato, verifica pulizia functione e attibuti entità non piu usate
			
			capitoloModel.getImportiCapitolo().add(supportImporti);
		}
		
		model.getDatoPerVisualizza().setImportiCapitolo(capitoloModel.getImportiCapitolo());
		model.getGestioneOrdinativoStep1Model().getCapitolo().setImportiCapitoloEG(capitoloModel.getImportiCapitoloEG());
	}
	
	// CR 1912
	public String cercaAccertamento(){
		
		List<Errore> listaErrori= new ArrayList<Errore>();
		 
		// CR 1913, INSERIMENTO_ORDINATIVO INCASSO --> 
		// RM : se l'utente sceglie di eseguire una ricerca di accertamento per chiave 
		// nn vanno fatti i controlli su capitolo, soggetto e provvedimento
		if(!model.isSonoInAggiornamento()){
			
			
				//pulisco gli ordinativi collegati gia eventualmente inseriti:
				//(perche' non so se il nuovo accertamento avra' il piano dei conti V livello)
				pulisciOrditiviCollegati();
				//
				
				if(model.getGestioneOrdinativoStep1Model().getAnnoAccertamento()== null &&
						model.getGestioneOrdinativoStep1Model().getNumeroAccertamento() == null){
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Accertamento"));
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Accertamento"));
					
					model.setAccertamentoTrovato(Boolean.FALSE);
					addErrori(listaErrori);
					return INPUT;
					
				}

				if (model.getGestioneOrdinativoStep1Model().getAnnoAccertamento()!= null &&
						model.getGestioneOrdinativoStep1Model().getNumeroAccertamento()== null){
					
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Accertamento"));
					model.setAccertamentoTrovato(Boolean.FALSE);
					addErrori(listaErrori);
					return INPUT;
				}
					
				if (model.getGestioneOrdinativoStep1Model().getAnnoAccertamento()== null &&
						model.getGestioneOrdinativoStep1Model().getNumeroAccertamento()!= null){
					
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Accertamento"));
					model.setAccertamentoTrovato(Boolean.FALSE);
					addErrori(listaErrori);
					return INPUT;
				}

				RicercaAccertamentoPerChiaveOttimizzato req = new RicercaAccertamentoPerChiaveOttimizzato();
				req.setEnte(sessionHandler.getEnte());
				req.setRichiedente(sessionHandler.getRichiedente());
	    		RicercaAccertamentoK param = new RicercaAccertamentoK();
	    		param.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
	    		param.setAnnoAccertamento(model.getGestioneOrdinativoStep1Model().getAnnoAccertamento());
	    		param.setNumeroAccertamento(new BigDecimal(model.getGestioneOrdinativoStep1Model().getNumeroAccertamento()));
	    		//SIAC-8142
	    		req.setCaricaAnnoAccertamentiConStessoNumeroNelBilancio(true);
	    		req.setpRicercaAccertamentoK(param);
	    		RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestioneFinService.ricercaAccertamentoPerChiaveOttimizzato(req);
	    		
   		
				if (response != null && response.getAccertamento() != null) {

					Accertamento accertamento = response.getAccertamento();
					
					//SIAC-8142
					List<Integer> anniAccertamentiConStessoNumeroNelBilancio = response.getAnniAccertamentiConStessoNumeroNelBilancio();
					//NOTA: sto utilizzando la lista come un boolean, ma lascio la gestione con lista in previsione della richiesta di mostrare l'anno
					if(anniAccertamentiConStessoNumeroNelBilancio != null && !anniAccertamentiConStessoNumeroNelBilancio.isEmpty()) {
						addPersistentActionWarning(ErroreFin.ACCERTAMENTI_PRESENTI_NEL_BILANCIO.getErrore("accertamenti").getTesto());
					}
					// se l'accertamento va bene per essere visualizzata allora carico i dati di:
					// capitolo, porvvedimento, soggetto, modalita di pagamento
					// controllo pero' prima lo stato! ammetto solo stato DEFINITO o DEFINITIVO NON LOQUIDABILE
					if (!accertamento.getStatoOperativoMovimentoGestioneEntrata().equals(CostantiFin.MOVGEST_STATO_ANNULLATO)
							&& !accertamento.getStatoOperativoMovimentoGestioneEntrata().equals(CostantiFin.MOVGEST_STATO_PROVVISORIO)) {
						
						
						//SIAC-7470 (ex SIAC-6997): controllo per bloccoROR
						boolean escludi = VerificaBloccoRORHelper.escludiAccertamentoPerBloccoROR(sessionHandler.getAzioniConsentite(), accertamento, sessionHandler.getAnnoBilancio());
						if(escludi){
							listaErrori.add(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Accertamento/sub accertamento residuo non utilizzabile"));
							model.setAccertamentoTrovato(Boolean.FALSE);
							addErrori(listaErrori);
							return INPUT;
						}else if(accertamento.getElencoSubAccertamenti() != null && !accertamento.getElencoSubAccertamenti().isEmpty()){
							for(int k = 0; k < accertamento.getElencoSubAccertamenti().size(); k++){
								escludi = VerificaBloccoRORHelper.escludiAccertamentoPerBloccoROR(sessionHandler.getAzioniConsentite(), accertamento.getElencoSubAccertamenti().get(k), sessionHandler.getAnnoBilancio());
								if(escludi)
									break;
							}
							if(escludi){
								listaErrori.add(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Accertamento/sub accertamento residuo non utilizzabile"));
								model.setAccertamentoTrovato(Boolean.FALSE);
								addErrori(listaErrori);
								return INPUT;
							}
						}
					
						// controllo sui sub:  
						// 1. se ce ne e' uno solo e l'utente ha inserito il numero da gui
						// verifico che siano uguali, se non corrispondono avverto l'utente!
						// 2. se l'utente non ha inserito il numero del sub da gui e ho una lista di sub
						// lo segnalo all'utente in modo che possa scegliere
						boolean subTrovato = false;
						SubAccertamento subAcc = null;
						boolean accertInstatoDefinitivoConSubAnnullati= accertamento.getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase(CostantiFin.MOVGEST_STATO_DEFINITIVO);
						
						if(!accertInstatoDefinitivoConSubAnnullati && accertamento.getElencoSubAccertamenti()!=null && !accertamento.getElencoSubAccertamenti().isEmpty()){
							
							
							for (SubAccertamento sub : accertamento.getElencoSubAccertamenti()) {
								BigInteger numerosub = sub.getNumeroBigDecimal().toBigInteger();
								if(model.getGestioneOrdinativoStep1Model().getNumeroSubAcc()!=null){
									if(numerosub.equals(model.getGestioneOrdinativoStep1Model().getNumeroSubAcc())){
										//ok trovato corrisponde!
										subTrovato = true;
										subAcc = sub;
										break;
									}
								}
							}
							
							// perche' non e' stato inserito il numero sub a manina
							if(!subTrovato){
								if(accertamento.getElencoSubAccertamenti().size() == 1){
									BigInteger numeroSub = (accertamento.getElencoSubAccertamenti().get(0)).getNumeroBigDecimal().toBigInteger();
									model.getGestioneOrdinativoStep1Model().setNumeroSubAcc(numeroSub);
									subAcc = accertamento.getElencoSubAccertamenti().get(0);
								}else{
									
									//msg utente in modo che inserisca il sub che gli interessa!
									StringBuffer strNumAccertamenti = new StringBuffer("");
									int i = 0;
									for (Accertamento acc : accertamento.getElencoSubAccertamenti()) {
										
										if (i > 0) {
											strNumAccertamenti.append(" , ");
										}
										strNumAccertamenti.append("" + acc.getNumeroBigDecimal());
										i++;
										
									}
									
									addErrore(ErroreFin.ACCERTAMENTO_CON_SUBACCERTAMENTI.getErrore(strNumAccertamenti.toString()));
									return INPUT;
									
								}
							}
	
						}

				
						// Se il subAccertamento e' in stato annullato o provvisiorio rimando l'errore come per l'accertamento
						if (subAcc!=null && subAcc.getStatoOperativoMovimentoGestioneEntrata().equals(CostantiFin.MOVGEST_STATO_ANNULLATO)
								|| accertamento.getStatoOperativoMovimentoGestioneEntrata().equals(CostantiFin.MOVGEST_STATO_PROVVISORIO)) 
						{
							addErrore(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("il subAccertamento " + subAcc.getAnnoMovimento() + "/" + subAcc.getNumeroBigDecimal(), "stato = " + subAcc.getStatoOperativoMovimentoGestioneEntrata().toString()));
							return INPUT;
						}
						
						
						//JIRA  SIAC-4116 introdotto controllo:
						boolean subCompilatoMaNonPresente = subCompilatoMaNonPresente(accertamento);
						if(subCompilatoMaNonPresente){
							addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("subaccertamento",model.getGestioneOrdinativoStep1Model().getNumeroSubAcc()));
							return INPUT;
						}
						//
					
						// 1 - setto il capitolo
						CapitoloEntrataGestione capEG = accertamento.getCapitoloEntrataGestione();
						model.getGestioneOrdinativoStep1Model().getCapitolo().setAnno(capEG.getAnnoCapitolo());
						model.getGestioneOrdinativoStep1Model().getCapitolo().setArticolo(capEG.getNumeroArticolo());
						model.getGestioneOrdinativoStep1Model().getCapitolo().setNumCapitolo(capEG.getNumeroCapitolo());
						BigInteger bigIntUeb = new BigInteger(Integer.toString(capEG.getNumeroUEB()));
						model.getGestioneOrdinativoStep1Model().getCapitolo().setUeb(bigIntUeb);
						model.getGestioneOrdinativoStep1Model().getCapitolo().setDescrizione(capEG.getDescrizione());
						model.getGestioneOrdinativoStep1Model().getCapitolo().setCodiceStrutturaAmministrativa(capEG.getStrutturaAmministrativoContabile().getCodice());
						model.getGestioneOrdinativoStep1Model().getCapitolo().setDescrizioneStrutturaAmministrativa(capEG.getStrutturaAmministrativoContabile().getDescrizione());
						//SIAC-8892
						if(capEG.getTipoFinanziamento()!=null) {
							model.getGestioneOrdinativoStep1Model().getCapitolo().setTipoFinanziamento(capEG.getTipoFinanziamento().getDescrizione());	
						}
						model.getGestioneOrdinativoStep1Model().getCapitolo().setDescrizionePdcFinanziario(capEG.getElementoPianoDeiConti().getCodice()+" - "+capEG.getElementoPianoDeiConti().getDescrizione());
						caricaStanziamento(capEG);	
							
						model.getGestioneOrdinativoStep1Model().setCapitoloSelezionato(Boolean.TRUE);
						//SIAC-8850
						setContoTesoreriaModel(capEG.getUid());
						
						// 2 - setto il provevdimento, resetto prima in modo da evitare dati sporchi con ricerche precedenti
						model.getGestioneOrdinativoStep1Model().setProvvedimento(new ProvvedimentoImpegnoModel());
						AttoAmministrativo provvedimento = accertamento.getAttoAmministrativo();
						model.getGestioneOrdinativoStep1Model().getProvvedimento().setUid(provvedimento.getUid());
						model.getGestioneOrdinativoStep1Model().getProvvedimento().setAnnoProvvedimento(provvedimento.getAnno());
						model.getGestioneOrdinativoStep1Model().getProvvedimento().setNumeroProvvedimento(new BigInteger(Integer.toString(provvedimento.getNumero())));
						model.getGestioneOrdinativoStep1Model().getProvvedimento().setIdTipoProvvedimento(provvedimento.getTipoAtto().getUid());
						model.getGestioneOrdinativoStep1Model().getProvvedimento().setTipoProvvedimento(provvedimento.getTipoAtto().getDescrizione());
						model.getGestioneOrdinativoStep1Model().getProvvedimento().setCodiceTipoProvvedimento(provvedimento.getTipoAtto().getCodice());
						model.getGestioneOrdinativoStep1Model().getProvvedimento().setOggetto((provvedimento.getOggetto()!=null ?provvedimento.getOggetto(): "" ));
						model.getGestioneOrdinativoStep1Model().setProvvedimentoSelezionato(Boolean.TRUE);
						model.getGestioneOrdinativoStep1Model().getProvvedimento().setStato(provvedimento.getStatoOperativo());
							
						if(provvedimento.getStrutturaAmmContabile()!=null){
							// setto livello e uid che mi serviranno dopo
							model.getGestioneOrdinativoStep1Model().getProvvedimento().setCodiceStrutturaAmministrativa(provvedimento.getStrutturaAmmContabile().getCodice());
							model.getGestioneOrdinativoStep1Model().getProvvedimento().setStrutturaAmministrativa(provvedimento.getStrutturaAmmContabile().getDescrizione());
							model.getGestioneOrdinativoStep1Model().getProvvedimento().setLivello(provvedimento.getStrutturaAmmContabile().getLivello());
							model.getGestioneOrdinativoStep1Model().getProvvedimento().setUidStrutturaAmm(provvedimento.getStrutturaAmmContabile().getUid());
						}
							
						// carico prima il soggetto, sul soggetto poi ricerco le sedi secondarie e preseleziono 
						// se non ho sub prendo il soggetto del sub, che a sua volta potrebbe non esserci
						model.getGestioneOrdinativoStep1Model().setSoggetto(new SoggettoImpegnoModel());
						Soggetto soggetto = (subAcc == null || subAcc.getSoggetto() == null) ?accertamento.getSoggetto() : subAcc.getSoggetto();
						
						if(soggetto!=null){
							
							model.getGestioneOrdinativoStep1Model().getSoggetto().setCodCreditore(soggetto.getCodiceSoggetto());
							model.getGestioneOrdinativoStep1Model().getSoggetto().setDenominazione(soggetto.getDenominazione());
							model.getGestioneOrdinativoStep1Model().getSoggetto().setCodfisc(soggetto.getCodiceFiscale());
							model.getGestioneOrdinativoStep1Model().getSoggetto().setUid(soggetto.getUid());
								
							if(StringUtils.isNotBlank(soggetto.getCodiceSoggetto())){
								model.getGestioneOrdinativoStep1Model().setSoggettoSelezionato(Boolean.TRUE);
								model.getGestioneOrdinativoStep1Model().setHasCodiceSoggettoAccertamento(Boolean.TRUE);
							}else{
								model.getGestioneOrdinativoStep1Model().setSoggettoSelezionato(Boolean.FALSE);
								model.getGestioneOrdinativoStep1Model().setHasCodiceSoggettoAccertamento(Boolean.FALSE);
							}
							
							if(soggetto.getSediSecondarie()!=null){
								model.getGestioneOrdinativoStep1Model().setListaSediSecondarie(soggetto.getSediSecondarie());
							}
						
						}
						else {
							model.getGestioneOrdinativoStep1Model().setSoggettoSelezionato(Boolean.FALSE);
							model.getGestioneOrdinativoStep1Model().setHasCodiceSoggettoAccertamento(Boolean.FALSE);
						}
						
						// uso il radioAccertamento selezionato per controllare se rifare alcuni controlli sul prosegui
						List<Accertamento> accertamentoStep2List = new ArrayList<Accertamento>();
						String accertamentoSubSelected ;
						
						if(subAcc!=null ){
							accertamentoSubSelected = String.valueOf(subAcc.getUid());
							accertamentoStep2List.add(subAcc);
						}else{
							accertamentoSubSelected = String.valueOf(accertamento.getUid());
							accertamentoStep2List.add(accertamento);
						}
						
						//setto il codice del pdc:
						settaPdcDaAccertamentoCercato(accertamento, subAcc);
						//
						
						model.getGestioneOrdinativoStep2Model().setRadioIdAccertamento(accertamentoSubSelected);	
						model.getGestioneOrdinativoStep2Model().setListaAccertamento(accertamentoStep2List);
						model.getGestioneOrdinativoStep2Model().setListaAccertamentoOriginale(clone(accertamentoStep2List));
						
						
						// lancio la routine per caricare i dati della TE a partire dalla liquidazione preselezionata appena sopra
						ricaricaTEByIdAccertamento();
						model.setAccertamentoTrovato(Boolean.TRUE);
						model.getGestioneOrdinativoStep2Model().setResultSize(accertamentoStep2List.size());

						BigDecimal disponibilitaIncassare = subAcc!=null ? subAcc.getDisponibilitaIncassare() : accertamento.getDisponibilitaIncassare();
						
						if (disponibilitaIncassare.compareTo(BigDecimal.ZERO) <= 0) {
							addPersistentActionWarning(ErroreFin.DISPONIBILITA_ACCERTAMENTO_INSUFFICIENTE.getErrore().getCodice()+" : "+ErroreFin.DISPONIBILITA_ACCERTAMENTO_INSUFFICIENTE.getErrore().getDescrizione());
						}else{			
							// presetto l'importo della quota
							model.getGestioneOrdinativoStep2Model().setImportoQuotaFormattato(convertiBigDecimalToImporto(disponibilitaIncassare));
						}
					}else{
						addErrore(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("l'accertamento " + accertamento.getAnnoMovimento() + "/" + accertamento.getNumeroBigDecimal(), "stato = " + accertamento.getStatoOperativoMovimentoGestioneEntrata().toString()));
						return INPUT;
					}
			}else {
				// Accertamento inesistente
				addErrore(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore(" L'accertamento "));
				pulisciCampiSuRicercaAccertamentoConFallimento();
				return INPUT;
			}
		}
	
		
		return SUCCESS;
	
	}
	
	//SIAC-8850
	protected String setContoTesoreriaModel(Integer capitoloUid) {
		impostaDefaultContoTesoriereVar(sonoInAggiornamentoIncasso());
		
		LeggiSottoContiVincolatiCapitolo req = new LeggiSottoContiVincolatiCapitolo();
		CapitoloUscitaGestione capitoloUscitaTrovato = new CapitoloUscitaGestione();
		capitoloUscitaTrovato.setUid(capitoloUid);
		req.setCapitoloUscitaGestione(capitoloUscitaTrovato);
		req.setRichiedente(model.getRichiedente());
		LeggiSottoContiVincolatiCapitoloResponse response = capitoloService.leggiSottoContiVincolatiCapitoloService(req);
		if (response.isFallimento()) {
			addErrori(response);
			return INPUT;
		}
		if(response.getContoTesoreria()!=null) {
			model.getGestioneOrdinativoStep1Model().getOrdinativo().setContoTesoreria(response.getContoTesoreria());	
		}
			
		
		return SUCCESS;
	}
	
	
	@Override
	protected BigDecimal getDisponibilitaContoVincolato(ControllaDisponibilitaCassaContoVincolatoCapitoloResponse response) {
		return response.getDisponibilitaContoVincolatoEntrata();
	}

	
	@Override
	protected void setCapitoloControllaDisponibilitaCassaContoVincolato(
			ControllaDisponibilitaCassaContoVincolatoCapitolo controllaDisponibilitaCassaContoVincolatoCapitolo, 
			CapitoloImpegnoModel capitoloImpegnoModel) {
		
		CapitoloEntrataGestione capitolo = new CapitoloEntrataGestione();
		
		capitolo.setNumeroCapitolo(capitoloImpegnoModel.getNumCapitolo());
		capitolo.setNumeroArticolo(capitoloImpegnoModel.getArticolo());
		capitolo.setNumeroUEB(capitoloImpegnoModel.getUeb() == null ? null : capitoloImpegnoModel.getUeb().intValue());
		
		controllaDisponibilitaCassaContoVincolatoCapitolo.setCapitoloEntrataGestione(capitolo);
	}
	
	
	
	
	
	
	/*
	 * non fa nulla, ma visto che il jquery e' unico lo richiama lo stesso ma
	 * non ha alcun effetto
	 */
	public String caricaTitoloSedi() {

		return SEDISECONDARIE;
	}

	/* **************************************************************************** */
	/* Getter / setter */
	/* **************************************************************************** */

	public String getNumeroOrdinativo() {
		return numeroOrdinativo;
	}

	public void setNumeroOrdinativo(String numeroOrdinativo) {
		this.numeroOrdinativo = numeroOrdinativo;
	}

	public String getAnnoOrdinativo() {
		return annoOrdinativo;
	}

	public void setAnnoOrdinativo(String annoOrdinativo) {
		this.annoOrdinativo = annoOrdinativo;
	}

	public Integer getIdOrdinativo() {
		return idOrdinativo;
	}

	public void setIdOrdinativo(Integer idOrdinativo) {
		this.idOrdinativo = idOrdinativo;
	}

	public String getDoveMiTrovo() {
		return doveMiTrovo;
	}

	public void setDoveMiTrovo(String doveMiTrovo) {
		this.doveMiTrovo = doveMiTrovo;
	}

	public String getStrutturaDaInserimento() {
		return strutturaDaInserimento;
	}

	public void setStrutturaDaInserimento(String strutturaDaInserimento) {
		this.strutturaDaInserimento = strutturaDaInserimento;
	}

	public String getRipeti() {
		return ripeti;
	}

	public void setRipeti(String ripeti) {
		this.ripeti = ripeti;
	}

	public String getNumeroOrdinativoRipeti() {
		return numeroOrdinativoRipeti;
	}

	public void setNumeroOrdinativoRipeti(String numeroOrdinativoRipeti) {
		this.numeroOrdinativoRipeti = numeroOrdinativoRipeti;
	}

	public String getAnnoOrdinativoRipeti() {
		return annoOrdinativoRipeti;
	}

	public void setAnnoOrdinativoRipeti(String annoOrdinativoRipeti) {
		this.annoOrdinativoRipeti = annoOrdinativoRipeti;
	}

	public String getNumeroOrdinativoDaPassare() {
		return numeroOrdinativoDaPassare;
	}

	public void setNumeroOrdinativoDaPassare(String numeroOrdinativoDaPassare) {
		this.numeroOrdinativoDaPassare = numeroOrdinativoDaPassare;
	}

	public String getAnnoOrdinativoDaPassare() {
		return annoOrdinativoDaPassare;
	}

	public void setAnnoOrdinativoDaPassare(String annoOrdinativoDaPassare) {
		this.annoOrdinativoDaPassare = annoOrdinativoDaPassare;
	}

}
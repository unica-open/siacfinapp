/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccommon.util.iban.IbanUtil;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.AggiornaSoggettoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.ModalitaPagamentoSoggettoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.RicercaSoggettoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.ValidationUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.codicefiscale.ValidaCF;
import it.csi.siac.siacfinapp.frontend.ui.util.codicefiscale.VerificaPartitaIva;
import it.csi.siac.siacfinapp.frontend.ui.util.comparator.ComparatorModalitaPagamentoSoggettoByCodice;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggettoProvvisorio;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaBanca;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaBancaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaGruppoTipoAccreditoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaGruppoTipoAccreditoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.VerificaIban;
import it.csi.siac.siacfinser.frontend.webservice.msg.VerificaIbanResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.ModalitaAccreditoSoggetto;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.ComuneNascita;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.Sesso;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto.TipoAccredito;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import xyz.timedrain.arianna.plugin.BreadCrumb;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ModalitaPagamentoSoggettoAction extends AggiornaSoggettoGenericAction {

	private static final long serialVersionUID = -1445574284156602140L;
	
	//Start variabili nuove
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggettoToInsert = new ModalitaPagamentoSoggetto();
	private String dataScadenzaStringa;
	//End variabili nuove
	
	private boolean forceLoad = false;
	
	private ModalitaPagamentoSoggettoModel modelWeb = new ModalitaPagamentoSoggettoModel();

	//Utili alla visuallizazzione dei vari tab per l'inserimento di una nuova modalita pagamento (ved. method panelInsertPayment() )
	private boolean azioneAggiorna = false;
	private String tipoAccreditoPage = "";
	private boolean inserisciModPag = false;
	private boolean contoCorrenteVisible = false; 
	private boolean contanteVisible = false;
	private boolean cessioneVisible = false;
	private boolean cessioneSearchVisible = false;
	private boolean successInserimento = false;
	private boolean genericoVisibile = false;
	private boolean cessioneSearchStep2 = false;
	private String codiceSoggetto;
	private String codiceSoggettoRicercatoCessione;
	private String codiceSoggettoRicercatoCessioneAggiorna;
	private String idAccreditoTipoSelected;
	private String tipoind;
	
	
	//Start variabili visione aggiornamento mdp
	private boolean aggiornaContoCorrenteVisible = false;
	private boolean aggiornaContantiVisible = false;
	private boolean aggiornaCessioneSearch = false;
	private boolean aggiornaCessioneStep2 = false;
	private boolean aggiornaCessioneStep3 = false;
	private boolean aggiornaSuccessVisible = false;
	private boolean aggiornaGenericoVisible = false;
	
	private String aggiornaTipoAccredito = "";
	private String aggiornaCodiceSoggetto = "";
	private ModalitaPagamentoSoggetto soggettoDaAggiornare = new ModalitaPagamentoSoggetto();
	//End variabili visione aggiornamento mdp
	
	private boolean ancoraVisualizza = false;
	private boolean ancoraMdpVisualizza = false;	
	
	private AggiornaSoggettoModel aggiornaSoggettoModelCachePerAggiorna = new  AggiornaSoggettoModel();

	/**
	 * metodo prepare della action
	 */
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		//task-224
	    if(AzioneConsentitaEnum.OP_CEC_SOG_leggiSogg.getNomeAzione().equals(sessionHandler.getAzione().getNome())) {
	    	this.model.setTitolo("Modalita' di Pagamento Cassa Economale");
		}else {
			this.model.setTitolo("Modalita' di Pagamento");
		}
		//non siamo in aggiorna:
		setAzioneAggiorna(false);
		//carico le liste modalita pagamento:
		caricaListeModalitaPagamento();
		//clono il model:
		aggiornaSoggettoModelCachePerAggiorna = (clone(model));
	}	

	/**
	 * pre setting valori soggetto
	 */
	private void presettingValoriSoggetto(){
		
		// preset dei valori nuovi della MDP solo per
		// le PERSONE FISICHE
		if(model.getDettaglioSoggetto().getTipoSoggetto()!=null &&
			(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode().equals(PF) ||
			 model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode().equals(PFI))){
			
			// presetto l'intestazione 
			if(null==modelWeb.getModalitaPagamentoSoggettoToInsert()){
				ModalitaPagamentoSoggetto mpd = new ModalitaPagamentoSoggetto();
				modelWeb.setModalitaPagamentoSoggettoToInsert(mpd);
			}
			
			// intestazione conto
			if(null==modalitaPagamentoSoggettoToInsert.getIntestazioneConto()){
				modalitaPagamentoSoggettoToInsert.setIntestazioneConto(model.getDettaglioSoggetto().getDenominazione());
				
			}
			
			
			// codice fiscale quietanzante
			if(null==modelWeb.getModalitaPagamentoSoggettoToInsert().getCodiceFiscaleQuietanzante()){
				modalitaPagamentoSoggettoToInsert.setCodiceFiscaleQuietanzante(model.getDettaglioSoggetto().getCodiceFiscale());
			}
			
			// quietanzante
			if(null==modelWeb.getModalitaPagamentoSoggettoToInsert().getSoggettoQuietanzante()){
				modalitaPagamentoSoggettoToInsert.setSoggettoQuietanzante(model.getDettaglioSoggetto().getDenominazione());
			}
			
			// data nascita quietanzante
			if(null==modelWeb.getModalitaPagamentoSoggettoToInsert().getDataNascitaQuietanzante()){
				
				modalitaPagamentoSoggettoToInsert.setDataNascitaQuietanzante(DateUtility.convertiDataInGgMmYyyy(model.getDettaglioSoggetto().getDataNascita()));
			}
			
			if(null==modelWeb.getModalitaPagamentoSoggettoToInsert().getComuneNascita()){
				// setto il comune e la nazione del soggetto
				ComuneNascita comuneNascita = model.getDettaglioSoggetto().getComuneNascita();
				modalitaPagamentoSoggettoToInsert.setComuneNascita(comuneNascita);
			}
			
		}
	}
	
	/**
	 * Metodo excecute della action
	 */
	@Override
	//task-224
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");

		List<Errore> listaErrori = new ArrayList<Errore>();
		
		if(StringUtils.isEmpty(model.getDettaglioSoggetto().getCodiceSoggetto())){
			//Se non viene selezionato alcun soggetto, mostro l'errore
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto"));
			addErrori(listaErrori);
			return INPUT;
		} else {

			//Gestisco sedi secondarie
			if(!isEmpty(model.getListaSecondariaSoggetto())){
				modelWeb.setSediSecondarie(model.getListaSecondariaSoggetto());
			} 
			
			sessionHandler.setParametro(FinSessionParameter.MODALITA_PAGAMENTO_MODEL, modelWeb);
			
			return SUCCESS;
		}
		
	}
	
	/**
	 * salva generale
	 * @return
	 */
	public String salvaGenerale(){
		setMethodName("salvaGenerale");

		// inserisco il codice tipo persona
		model.getDettaglioSoggetto().getTipoSoggetto().setCodice(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode());
		model.getDettaglioSoggetto().setSediSecondarie(model.getListaSecondariaSoggetto());
		if (model.getDettaglioSoggetto().getSessoStringa() != null && !"".equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa())) {
			if (CostantiFin.MASCHIO.equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa().toUpperCase())) {
				model.getDettaglioSoggetto().setSesso(Sesso.MASCHIO);
			} else {
				model.getDettaglioSoggetto().setSesso(Sesso.FEMMINA);
			}
		}
		AggiornaSoggettoResponse response = null;
		
		//compongo la request:
		AggiornaSoggetto aggiornaSoggetto = convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(), false);
		
		//setto la codifica dell'ambito:
		aggiornaSoggetto.setCodificaAmbito(getcodiceAmbito());
		
		if (AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite())) {
		
			AggiornaSoggettoProvvisorio asp = new AggiornaSoggettoProvvisorio(aggiornaSoggetto);
			//richiamo il servizio di aggiornamento provvisorio:
			response = soggettoService.aggiornaSoggettoProvvisorio(asp);
	
		}else{
			//richiamo il servizio :
			response = soggettoService.aggiornaSoggetto(aggiornaSoggetto);
		}
		
		if(isFallimento(response)){
			//presenza errori
			
			debug(methodName, "Errore nella risposta del servizio");
			
			clearErrorsAndMessages();
			
			addErrori(methodName, response);
			
			debug(methodName, "Model: " + model);
			
			return INPUT;
		}
		
		//se sono qui tutto ok:
		clearErrorsAndMessages();
		addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore());
		return SUCCESS;
	}

	/**
	 * Modalita Pagamento
	 * @return
	 */
	public String modalitaPagamento(){
		setMethodName("modalitaPagamento");
		return SUCCESS;
	}


	/**
	 * Panel Insert Payment
	 * @return
	 */
	public String panelInsertPayment(){

		setMethodName("panelInsertPayment");

		handleModelBean();
		
		modelWeb.setTipoind(null);
		modelWeb.setIdAccreditoTipoSelected(null);
		
		//Preparo le liste per l'inserimento di nuove modalita di pagamento in sessione
		List<String> associataList = new ArrayList<String>();
		associataList.add("Soggetto");
		if(modelWeb.getSediSecondarie() != null){
			for(SedeSecondariaSoggetto sedSecSog : modelWeb.getSediSecondarie()){
				if(sedSecSog.getDescrizioneStatoOperativoSedeSecondaria().equalsIgnoreCase(CostantiFin.STATO_VALIDO) || sedSecSog.getDescrizioneStatoOperativoSedeSecondaria().equalsIgnoreCase(CostantiFin.STATO_PROVVISORIO)
					//correzione JIRA 740
					||	sedSecSog.getDescrizioneStatoOperativoSedeSecondaria().equalsIgnoreCase(CostantiFin.STATO_IN_MODIFICA)){
					associataList.add(sedSecSog.getDenominazione());
				}
			}
		}

		if(associataList != null){
			modelWeb.setAssociataList(associataList);
		}
		
		
		sessionHandler.setParametro(FinSessionParameter.MODALITA_PAGAMENTO_MODEL, modelWeb);
		
		handleVisualizationVariable(true, false, false, false, false, false, false);
		
		// setto l'ancora della pagina
		setAncoraVisualizza(true);

		return SUCCESS;
	}
	
	/**
	 * Handle Type Payment
	 * @return
	 */
	public String handleTypePayment(){
		setMethodName("handleTypePayment");
		
		//Compongo la request per il servizio findGruppoTipoAccreditoPerPk:
		RicercaGruppoTipoAccreditoPerChiave req = new RicercaGruppoTipoAccreditoPerChiave();
		req.setRichiedente(sessionHandler.getRichiedente());
		req.setTipoId(Integer.valueOf(modelWeb.getIdAccreditoTipoSelected()));
		//richiamo il servizio findGruppoTipoAccreditoPerPk :
		RicercaGruppoTipoAccreditoPerChiaveResponse resp = genericService.findGruppoTipoAccreditoPerPk(req);

		String tipoAccredito = modelWeb.getIdAccreditoTipoSelected();
		String associatoA = modelWeb.getTipoind();
		
		handleTipoAccredito(resp.getGruppoTipoAccredito().getCodice());
		handleModelBean();
		modelWeb.setCodeMdpSelected(resp.getGruppoTipoAccredito().getCodice());
		
		
		// preset dei valori del soggetto
		presettingValoriSoggetto();
		
		modelWeb.getModalitaPagamentoSoggettoToInsert().setAssociatoA(associatoA);
		
		switch (modelWeb.getModalitaPagamentoSoggettoToInsert().getTipoAccredito()) {
		case CB:
			handleVisualizationVariable(true, true, false, false, false, false, false, false);
			// setto l'ancora della pagina
			setAncoraMdpVisualizza(true);
			break;
		case CO:
			handleVisualizationVariable(true, false, true, false, false, false, false, false);
			// setto l'ancora della pagina
			setAncoraMdpVisualizza(true);
			break;
		case CBI:
			handleVisualizationVariable(true, true, false, false, false, false, false, false);
			// setto l'ancora della pagina
			setAncoraMdpVisualizza(true);	
			break;
		case CCP:
			handleVisualizationVariable(true, true, false, false, false, false, false, false);
			// setto l'ancora della pagina
			setAncoraMdpVisualizza(true);	
			break;
		case CSC:
			handleTipoAccreditoOriginale(tipoAccredito);
			handleVisualizationVariable(true, false, false, true, false, false, false, false);
			// setto l'ancora della pagina
			setAncoraMdpVisualizza(true);
			break;
		case CSI:
			handleTipoAccreditoOriginale(tipoAccredito);
			handleVisualizationVariable(true, false, false, true, false, false, false, false);
			// setto l'ancora della pagina
			setAncoraMdpVisualizza(true);
			break;
		case GE:
			handleVisualizationVariable(true, false, false, false, false, false, false, true);
			// setto l'ancora della pagina
			setAncoraMdpVisualizza(true);
			break;
		default:
			handleVisualizationVariable(true, true, false, false, false, false, false, false);
			// setto l'ancora della pagina
			setAncoraMdpVisualizza(true);	
			break;
		}
		
		modelWeb.setDenominazioneAssociatoA(associatoA);
		modelWeb.setIdTipoAccredito(tipoAccredito);
		modelWeb.setTipoind(associatoA);
		modelWeb.setIdAccreditoTipoSelected(tipoAccredito);
		
		sessionHandler.setParametro(FinSessionParameter.MODALITA_PAGAMENTO_MODEL, modelWeb);
		
		return SUCCESS;
	}

	/**
	 * Handle Tipo Accredito Originale
	 * @param tipoAccredito
	 */
	private void handleTipoAccreditoOriginale(String tipoAccredito) {
		ModalitaPagamentoSoggetto mps = new ModalitaPagamentoSoggetto();
		//setto il tipo accredito by id sul tipo indicato:
		mps.setTipoAccredito(getTipoAccreditoById(Integer.valueOf(tipoAccredito)));
		//setto nel model:
		modelWeb.getModalitaPagamentoSoggettoToInsert().setModalitaOriginale(mps);
	}
	
	/**
	 * Ricerca Soggetto ModPag
	 * @return
	 */
	public String ricercaSoggettoModPag() {
		setMethodName("ricercaSoggettoModPag");

		setTipoAccreditoPage(getTipoAccreditoPage());

		// setto l'ancora della pagina
		setAncoraMdpVisualizza(true);

		List<Errore> listaErroriPerValidazione = new ArrayList<Errore>();

		Integer codiceApp = modelWeb.getCodice();
		String codiceFiscaleApp = modelWeb.getCodiceFiscale().toUpperCase();
		String denominazioneApp = modelWeb.getDenominazione();
		String partitaIvaApp = modelWeb.getPartitaIva();

		RicercaSoggettoModel ricercaSoggettoModel = new RicercaSoggettoModel();
		ricercaSoggettoModel.setCodice(codiceApp);
		ricercaSoggettoModel.setCodiceFiscale(codiceFiscaleApp);
		ricercaSoggettoModel.setDenominazione(denominazioneApp);
		ricercaSoggettoModel.setPartitaIva(partitaIvaApp);

		handleModelBean();
		modelWeb.setCodice(codiceApp);
		modelWeb.setCodiceFiscale(codiceFiscaleApp);
		modelWeb.setDenominazione(denominazioneApp);
		modelWeb.setPartitaIva(partitaIvaApp);

		ValidationUtils.validaRicercaSoggetto(listaErroriPerValidazione, ricercaSoggettoModel);
		if (!listaErroriPerValidazione.isEmpty()) {
			addErrori(listaErroriPerValidazione);
			handleVisualizationVariable(true, false, false, true, false, false, false);
			return INPUT;
		} else {
			
			//effettuo la ricerca tramite servizio:
			RicercaSoggettiResponse response = soggettoService.ricercaSoggetti(convertiModelPerChiamataServizioRicerca());

			if (response.isFallimento()) {
				//ci sono errori
				addErrori(methodName, response);
				return INPUT;
			}

			List<Errore> listaErrori = new ArrayList<Errore>();

			if (isEmpty(response.getSoggetti())){
				listaErrori.add(ErroreCore.NESSUN_DATO_REPERITO.getErrore("Soggetto"));
			} else {
				List<Soggetto> soggettiValidati = new ArrayList<Soggetto>();

				validaSoggettiPerRicerca(response.getSoggetti(), soggettiValidati, listaErrori);
				modelWeb.setSoggettiRicercati(soggettiValidati);

				if(!isEmpty(modelWeb.getSoggettiRicercati())){
					handleVisualizationVariable(true, false, false, true, true, false, false);
					sessionHandler.setParametro(FinSessionParameter.MODALITA_PAGAMENTO_MODEL,modelWeb);
				}
			}

			if (!listaErrori.isEmpty()) {
				addErrori(listaErrori);
				handleVisualizationVariable(true, false, false, true, false, false, false);
				return INPUT;
			} else {
				return SUCCESS;
			}
				
		}
	}
	
	/**
	 * Ricerca Soggetto ModPag_step2
	 * @return
	 */
	public String ricercaSoggettoModPag_step2(){
		
		setMethodName("ricercaSoggettoModPag_step2");
		
		// setto l'ancora della pagina
		setAncoraMdpVisualizza(true);	
		
		setTipoAccreditoPage(getTipoAccreditoPage());
		
		handleModelBean();
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		if(StringUtils.isEmpty(getCodiceSoggettoRicercatoCessione())){
			//Se non viene selezionato alcun soggetto, mostro l'errore
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Selezionare un soggetto per la cessione"));
			addErrori(listaErrori);
			
			handleVisualizationVariable(true, false, false, true, true, false, false);
			
			return INPUT;
		} else {
			//Compongo la request per il servizio:
			RicercaSoggettoPerChiave requestRicerca = convertiModelPerChiamataServizioRicercaPerChiave(getCodiceSoggettoRicercatoCessione());
			//richiamo il servizio di ricerca:
			RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(requestRicerca);
			//Salvo il dettaglio del soggetto selezionato e le relative sedi e modalita di pagamento associate
			modelWeb.setDettaglioSoggettoCessione(response.getSoggetto());
			modelWeb.setModalitaPagamentoCessione(valutaListaMdp(response.getListaModalitaPagamentoSoggetto()));
			modelWeb.setSedisecondariaSoggettoCessione(response.getListaSecondariaSoggetto());
			
			handleVisualizationVariable(true, false, false, true, true, true, false);

			sessionHandler.setParametro(FinSessionParameter.MODALITA_PAGAMENTO_MODEL, modelWeb);
			
			return SUCCESS;
		}
		
	}
	
	/**
	 * Inserisci Mod Pag ContoCorrente
	 * @return
	 */
	public String inserisciModPagContoCorrente(){
		
		setMethodName("inserisciModPagContoCorrente");
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		boolean validate = false;
		
		handleModelBean();
		
		presettingValoriSoggetto();
		
		switch (modelWeb.getModalitaPagamentoSoggettoToInsert().getTipoAccredito()) {
		case CBI:
			
			boolean contoCorrenteMancanteCBI = false;
			
			if(modalitaPagamentoSoggettoToInsert.getContoCorrente() != null){
				if(!modalitaPagamentoSoggettoToInsert.getContoCorrente().isEmpty()){
					if(modalitaPagamentoSoggettoToInsert.getContoCorrente().trim().length() != 7){
						listaErrori.add(ErroreFin.FORMATO_BANCARIO_NON_VALIDO.getErrore("Conto Corrente", "7"));
						addErrori(listaErrori);
						handleVisualizationVariable(true, true, false, false, false, false, false);
						return INPUT;
					}
				} else {
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Conto Corrente obbligatorio"));
					addErrori(listaErrori);
					handleVisualizationVariable(true, true, false, false, false, false, false);
					return INPUT;
				}
				
			} else {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Conto Corrente obbligatorio"));
				addErrori(listaErrori);
				handleVisualizationVariable(true, true, false, false, false, false, false);
				return INPUT;
			}
			
			if(!contoCorrenteMancanteCBI){
				validate = true;
			}
			
			break;
		case CB:
			checkDatiContoCorrente(modalitaPagamentoSoggettoToInsert, listaErrori);
			if (listaErrori.isEmpty()){
				validate = true;
			} else {
				addErrori(listaErrori);
				handleVisualizationVariable(true, true, false, false, false, false, false);
				return INPUT;
			}
			break;
		case CCP:

			boolean contoCorrenteMancanteCCP = false;
			
			if(modalitaPagamentoSoggettoToInsert.getContoCorrente() != null){
				if(!modalitaPagamentoSoggettoToInsert.getContoCorrente().isEmpty()){
					if(modalitaPagamentoSoggettoToInsert.getContoCorrente().trim().length() != 12){
						listaErrori.add(ErroreFin.FORMATO_BANCARIO_NON_VALIDO.getErrore("Conto Corrente", "12"));
						addErrori(listaErrori);
						handleVisualizationVariable(true, true, false, false, false, false, false);
						return INPUT;
					} 
				} else {
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Conto Corrente obbligatorio"));
					addErrori(listaErrori);
					handleVisualizationVariable(true, true, false, false, false, false, false);
					return INPUT;
				}

			} else {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Conto Corrente obbligatorio"));
				addErrori(listaErrori);
				handleVisualizationVariable(true, true, false, false, false, false, false);
				return INPUT;
			}
			
			if(!contoCorrenteMancanteCCP){
				validate = true;
			}
			
			break;
		
		}
		
		
		if(validate){
			//ok validate
			
			ModalitaPagamentoSoggetto modPagToInsert = modelWeb.getModalitaPagamentoSoggettoToInsert();
			//gestisco lo stato utente
			
			//Gestisco la denominazione
			if(modelWeb.getDenominazioneAssociatoA().equals("Soggetto")){
				modPagToInsert.setCodiceSoggettoAssociato(Integer.valueOf(model.getDettaglioSoggetto().getUid()));
			} else {
				for(SedeSecondariaSoggetto sedSec : modelWeb.getSediSecondarie()){
					if(modelWeb.getDenominazioneAssociatoA().equals(sedSec.getDenominazione())){
						modPagToInsert.setCodiceSoggettoAssociato(Integer.valueOf(sedSec.getUid()));
					}
				}
					
			}

			boolean gestSogg = AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSogg, sessionHandler.getAzioniConsentite());
			boolean gestSoggDec = AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite());
			
			//Sistemo tutte le variabili
			modPagToInsert.setUid(0); //La setto a 0 per capire che sara da inserire lato backend
			
			if(modalitaPagamentoSoggettoToInsert.getIban() != null){
				modPagToInsert.setIban(modalitaPagamentoSoggettoToInsert.getIban().trim());
			}
			
			if(modalitaPagamentoSoggettoToInsert.getBic() != null){
				modPagToInsert.setBic(modalitaPagamentoSoggettoToInsert.getBic().trim());
			}
			
			if(modalitaPagamentoSoggettoToInsert.getDenominazioneBanca() != null){
				modPagToInsert.setDenominazioneBanca(modalitaPagamentoSoggettoToInsert.getDenominazioneBanca().trim());
			}
			
			if(modalitaPagamentoSoggettoToInsert.getContoCorrente() != null){
				modPagToInsert.setContoCorrente(modalitaPagamentoSoggettoToInsert.getContoCorrente().trim());
			}
			
			modPagToInsert.setAssociatoA(modelWeb.getDenominazioneAssociatoA());
			
			if(modalitaPagamentoSoggettoToInsert.getNote() != null){
				modPagToInsert.setNote(modalitaPagamentoSoggettoToInsert.getNote());
			}
			
			modPagToInsert.setPerStipendi(modalitaPagamentoSoggettoToInsert.getPerStipendi());

			// campi nuovi
			if(null!=modalitaPagamentoSoggettoToInsert.getIntestazioneConto()){
				modPagToInsert.setIntestazioneConto(modalitaPagamentoSoggettoToInsert.getIntestazioneConto());
			}
			
			
			if(getDataScadenzaStringa() != null && !getDataScadenzaStringa().isEmpty()){
					
				// controllo la data di nascita per le persone
				if(getDataScadenzaStringa().length()!=10){
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
					addErrori(listaErrori);
					handleVisualizationVariable(true, true, false, false, false, false, false);
					return INPUT;
				}else {
					
					if (!DateUtility.isDate(getDataScadenzaStringa(), "dd/MM/yyyy")) {
						listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
						addErrori(listaErrori);
						handleVisualizationVariable(true, true, false, false, false, false, false);
						return INPUT;
					} else {
						DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						
						try {
							Date dataScadenza = df.parse(getDataScadenzaStringa());
							
							Calendar cFrom = Calendar.getInstance();							
							cFrom.set(Calendar.HOUR_OF_DAY, 0);
							cFrom.set(Calendar.MINUTE, 0);
							cFrom.set(Calendar.SECOND, 0);
							cFrom.set(Calendar.MILLISECOND, 0);
							Timestamp now = new Timestamp(cFrom.getTime().getTime());
							
							if(dataScadenza.compareTo(now)==-1){
								listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Data Scadenza", "(Non deve essere minore della data odierna)"));
								addErrori(listaErrori);
								handleVisualizationVariable(true, true, false, false, false, false, false);
								return INPUT;
							}
							
							modPagToInsert.setDataFineValidita(dataScadenza);
						} catch (ParseException e) {
							//  Auto-generated catch block
						}
						
					}

				}
			}
			
			//Setto la gestione del tipo accredito
			modPagToInsert.setGestioneTipoAccredito(modPagToInsert.getTipoAccredito());
			
			//CORRETTIVA
			ModalitaAccreditoSoggetto modalitaScelta = getModalitaAccreditoScelta();

			modPagToInsert.setModalitaAccreditoSoggetto(modalitaScelta);
			
			if(gestSogg){
				modPagToInsert.setDescrizioneStatoModalitaPagamento("valido");
			}
			if(gestSoggDec){
				modPagToInsert.setDescrizioneStatoModalitaPagamento("provvisorio");
			}
			
			List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettolistToInsert = model.getDettaglioSoggetto().getModalitaPagamentoList();
			if(modalitaPagamentoSoggettolistToInsert != null){
				modalitaPagamentoSoggettolistToInsert.add(modPagToInsert);
			} else {
				modalitaPagamentoSoggettolistToInsert = new ArrayList<ModalitaPagamentoSoggetto>();
				modalitaPagamentoSoggettolistToInsert.add(modPagToInsert);
			}
 
			if (Boolean.TRUE.equals(modPagToInsert.getPerStipendi())) {
				ModalitaPagamentoSoggetto mdpPerStipendiIsSet = null;

				for (ModalitaPagamentoSoggetto m : modalitaPagamentoSoggettolistToInsert)
					if (m.getUid() != modPagToInsert.getUid())
						mdpPerStipendiIsSet = getMdpPerStipendi(m, modPagToInsert);

				if (mdpPerStipendiIsSet != null)
					addPersistentActionWarning(ErroreFin.WARNING_GENERICO.getErrore(String.format(
							"Attenzione: modalit&agrave; di pagamento impostata 'Per gli stipendi'. In precedenza era impostata la modalit&agrave; di pagamento [ %s ].",
							mdpPerStipendiIsSet.getCodiceModalitaPagamento())).getTesto());
			}			
			
			//AGGIUNGO LE MODALITA DI PAGAMENTO VECCHIE E NUOVE AL SOGGETTO IN SESSIONE!!!!
			model.getDettaglioSoggetto().setModalitaPagamentoList(modalitaPagamentoSoggettolistToInsert);
			model.getDettaglioSoggetto().setSediSecondarie(modelWeb.getSediSecondarie());
			
			resetVariable();
			handleVisualizationVariable(false, false, false, false, false, false, false);
			
			// inserisco il codice tipo persona
			model.getDettaglioSoggetto().getTipoSoggetto().setCodice(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode());
			model.getDettaglioSoggetto().setSediSecondarie(model.getListaSecondariaSoggetto());
			if (model.getDettaglioSoggetto().getSessoStringa() != null && !"".equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa())) {
				if (CostantiFin.MASCHIO.equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa().toUpperCase())) {
					model.getDettaglioSoggetto().setSesso(Sesso.MASCHIO);
				} else {
					model.getDettaglioSoggetto().setSesso(Sesso.FEMMINA);
				}
			}
			AggiornaSoggettoResponse response = null;
			
			//Compongo la request per il servizio:
			AggiornaSoggetto aggiornaSoggetto = convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(), false);
			
			//setto la codifica dell'ambito:
			aggiornaSoggetto.setCodificaAmbito(getcodiceAmbito());
			
			if (AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite())) {
				//Compongo la request per il servizio:
				AggiornaSoggettoProvvisorio asp = new AggiornaSoggettoProvvisorio(aggiornaSoggetto);
				//richiamo il servizio di aggiornamento provvisorio:
				response = soggettoService.aggiornaSoggettoProvvisorio(asp);
		
			}else{
				//richiamo il servizio di aggiornamento:
				response = soggettoService.aggiornaSoggetto(aggiornaSoggetto);
			}
			
			
			if(isFallimento(response)) {
				//ci sono errori
				debug(methodName, "Errore nella risposta del servizio");
				
				clearErrorsAndMessages();
				
				addErrori(methodName, response);
				
				debug(methodName, "Model: " + model);
				
				return INPUT;
			}
			
			clearErrorsAndMessages();
			
			// messaggio di ok nella pagina di elenco soggetti
			addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore()); 
			
			model.getDettaglioSoggetto().setModalitaPagamentoList(response.getSoggettoAggiornato().getModalitaPagamentoList());
			
			Collections.sort(model.getDettaglioSoggetto().getModalitaPagamentoList(), ComparatorModalitaPagamentoSoggettoByCodice.INSTANCE);
			
			return SUCCESS;
			
		} else {

			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Inserire o IBAN oppure Conto Corrente e Bic"));
			addErrori(listaErrori);
			handleVisualizationVariable(true, true, false, false, false, false, false);
			return INPUT;
		}
		
	}

	/**
	 * Check Dati Conto Corrente
	 * @param mdp
	 * @param listaErrori
	 */
	private void checkDatiContoCorrente(ModalitaPagamentoSoggetto mdp, List<Errore> listaErrori){
		//pulisco i campi:
		mdp.cleanFields();

		String iban = mdp.getIban();
		String denominazioneBanca = mdp.getDenominazioneBanca();
		String bic = mdp.getBic();
		String numeroConto = mdp.getContoCorrente();
		
		// SIAC-5207
		// TODO: inserire il codice corretto
		//if (!bic.matches("^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$")) {
		if (org.apache.commons.lang.StringUtils.isNotBlank(bic) && !bic.matches("^[A-Z0-9]+$")) {
			listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("BIC", bic));
		}
		
		if (org.apache.commons.lang.StringUtils.isBlank(iban)) {
			checkDatiContoCorrenteNoSepa(denominazioneBanca, bic, numeroConto, listaErrori);
			return;
		}
		
		boolean isSepa = true;
		
		String ibanErr = IbanUtil.checkIban(iban);
		
		//errori vari:
		
		//IBAN VUOTO
		if (ibanErr != null) {
			listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("IBAN", ": " + ibanErr));
			return;
		}
		
		//IBAN FORMATO controllo 1
		if (!iban.matches("^[A-Z]{2}.*")) {
			listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("IBAN", iban));
			return;
		}
		
		//IBAN FORMATO controllo 2
		if (iban.startsWith("IT")) {
			if (iban.matches(CostantiFin.REGEX_IBAN_IT_VALIDO)) {
				RicercaBancaResponse ricercaBancaResponse = ricercaBanca(iban);
				if (ricercaBancaResponse.isFallimento()){
					//ci sono errori
					listaErrori.add(ErroreCore.ERRORE_DI_SISTEMA.getErrore(ricercaBancaResponse.getDescrizioneErrori()));
				} else if (ricercaBancaResponse.getBanca() == null){
					addActionWarning("ABI o CAB non validi");
				}
			} else {
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("IBAN", iban));
			}
		} else {
			VerificaIbanResponse verificaIbanResponse = verificaIban(iban);

			if (verificaIbanResponse.isFallimento()){
				//ci sono errori
				listaErrori.addAll(verificaIbanResponse.getErrori());
			} else if (verificaIbanResponse.getSepa()){
				if (org.apache.commons.lang.StringUtils.isBlank(bic)){
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Inserire il codice BIC"));
				}
			} else {
				isSepa = false;
				checkDatiContoCorrenteNoSepa(denominazioneBanca, bic, numeroConto, listaErrori);
			}
		}

		if (isSepa && org.apache.commons.lang.StringUtils.isNotEmpty(numeroConto)){
			listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("numero del conto","se il conto e' in area SEPA"));
		}
		
	}
	
	/**
	 * Check Dati Conto Corrente NoSepa
	 * @param denominazioneBanca
	 * @param bic
	 * @param numeroConto
	 * @param listaErrori
	 */
	private void checkDatiContoCorrenteNoSepa(String denominazioneBanca, String bic, String numeroConto,List<Errore> listaErrori){
		
		//BIC
		if (org.apache.commons.lang.StringUtils.isBlank(bic)){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Inserire il codice BIC"));
		}

		//NUMERO CONTO
		if (org.apache.commons.lang.StringUtils.isBlank(numeroConto)){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Inserire il numero del conto"));
		}
		
		//DENOMINAZIONE BANCA
		if (org.apache.commons.lang.StringUtils.isBlank(denominazioneBanca)){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Inserire la denominazione della banca"));
		}
		
	}

	/**
	 * Verifica Iban
	 * @param iban
	 * @return
	 */
	private VerificaIbanResponse verificaIban(String iban){
		//compongo la request:
		VerificaIban verificaIban = new VerificaIban();
		verificaIban.setIban(iban);
		verificaIban.setRichiedente(sessionHandler.getRichiedente());
		verificaIban.setEnte(sessionHandler.getEnte());

		//richiamo il servizio di verifica iban:
		return soggettoService.verificaIban(verificaIban);
	}

	/**
	 * Ricerca Banca
	 * @param iban
	 * @return
	 */
	private RicercaBancaResponse ricercaBanca(String iban){
		//compongo la request:
		RicercaBanca ricercaBanca = new RicercaBanca();
		ricercaBanca.setIban(iban);
		ricercaBanca.setRichiedente(sessionHandler.getRichiedente());
		ricercaBanca.setEnte(sessionHandler.getEnte());

		//richiamo il servizio di ricerca banca:
		return soggettoService.ricercaBanca(ricercaBanca);
	}

	/**
	 * Inserisci ModPag Generico
	 * @return
	 */
	public String inserisciModPagGenerico(){
		setMethodName("inserisciModPagContanti");
		
		List<Errore> listaErrori = new ArrayList<Errore>();
			
		handleModelBean();
		
		ModalitaPagamentoSoggetto modPagToInsert = modelWeb.getModalitaPagamentoSoggettoToInsert();
		
		//Gestisco la denominazione
		if(modelWeb.getDenominazioneAssociatoA().equals("Soggetto")){
			modPagToInsert.setCodiceSoggettoAssociato(Integer.valueOf(model.getDettaglioSoggetto().getUid()));
		} else {
			for(SedeSecondariaSoggetto sedSec : modelWeb.getSediSecondarie()){
				if(modelWeb.getDenominazioneAssociatoA().equals(sedSec.getDenominazione())){
					modPagToInsert.setCodiceSoggettoAssociato(Integer.valueOf(sedSec.getUid()));
				}
			}
				
		}
		
		//gestisco lo stato utente
		boolean gestSogg = AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSogg, sessionHandler.getAzioniConsentite());
		boolean gestSoggDec = AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite());
		
		modPagToInsert.setUid(0); //La setto a 0 per capire che sara da inserire lato backend
		
		if(modalitaPagamentoSoggettoToInsert.getNote() != null){
			modPagToInsert.setNote(modalitaPagamentoSoggettoToInsert.getNote());
		}
		
		modPagToInsert.setPerStipendi(modalitaPagamentoSoggettoToInsert.getPerStipendi());

		if(getDataScadenzaStringa() != null && !getDataScadenzaStringa().isEmpty()){
			// controllo la data di nascita per le persone
			if(getDataScadenzaStringa().length()!=10){
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
				addErrori(listaErrori);
				handleVisualizationVariable(true, false, false, false, false, false, false, true);
				return INPUT;
			}else {
				
				if (!DateUtility.isDate(getDataScadenzaStringa(), "dd/MM/yyyy")) {
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
					addErrori(listaErrori);
					handleVisualizationVariable(true, false, false, false, false, false, false, true);
					return INPUT;
				} else {
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					
					try {
						Date dataScadenza = df.parse(getDataScadenzaStringa());
						
						Calendar cFrom = Calendar.getInstance();							
						cFrom.set(Calendar.HOUR_OF_DAY, 0);
						cFrom.set(Calendar.MINUTE, 0);
						cFrom.set(Calendar.SECOND, 0);
						cFrom.set(Calendar.MILLISECOND, 0);
						Timestamp now = new Timestamp(cFrom.getTime().getTime());
						
						if(dataScadenza.compareTo(now)==-1){
							listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Data Scadenza", "(Non deve essere minore della data odierna)"));
							addErrori(listaErrori);
							handleVisualizationVariable(true, false, false, false, false, false, false, true);
							return INPUT;
						}
						modPagToInsert.setDataFineValidita(dataScadenza);
					} catch (ParseException e) {
						//  Auto-generated catch block
					}
				}

			}
		}
		
		modPagToInsert.setSoggettoQuietanzante(modalitaPagamentoSoggettoToInsert.getSoggettoQuietanzante());
		modPagToInsert.setAssociatoA(modelWeb.getDenominazioneAssociatoA());
		
		if(gestSogg){
			modPagToInsert.setDescrizioneStatoModalitaPagamento("valido");
		}
		if(gestSoggDec){
			modPagToInsert.setDescrizioneStatoModalitaPagamento("provvisorio");
		}
		
		//Setto la gestione del tipo accredito
		modPagToInsert.setGestioneTipoAccredito(modPagToInsert.getTipoAccredito());
		
		//CORRETTIVA
		ModalitaAccreditoSoggetto modalitaScelta = getModalitaAccreditoScelta();

		modPagToInsert.setModalitaAccreditoSoggetto(modalitaScelta);
		
		List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettolistToInsert = model.getDettaglioSoggetto().getModalitaPagamentoList();
		if(modalitaPagamentoSoggettolistToInsert != null){
			modalitaPagamentoSoggettolistToInsert.add(modPagToInsert);
		} else {
			modalitaPagamentoSoggettolistToInsert = new ArrayList<ModalitaPagamentoSoggetto>();
			modalitaPagamentoSoggettolistToInsert.add(modPagToInsert);
		}
		
		if (Boolean.TRUE.equals(modPagToInsert.getPerStipendi())) {
			ModalitaPagamentoSoggetto mdpPerStipendiIsSet = null;

			for (ModalitaPagamentoSoggetto m : modalitaPagamentoSoggettolistToInsert)
				if (m.getUid() != modPagToInsert.getUid())
					mdpPerStipendiIsSet = getMdpPerStipendi(m, modPagToInsert);

			if (mdpPerStipendiIsSet != null)
				addPersistentActionWarning(ErroreFin.WARNING_GENERICO.getErrore(String.format(
						"Attenzione: modalit&agrave; di pagamento impostata 'Per gli stipendi'. In precedenza era impostata la modalit&agrave; di pagamento [ %s ].",
						mdpPerStipendiIsSet.getCodiceModalitaPagamento())).getTesto());
		}			
		

		//AGGIUNGO LE MODALITA DI PAGAMENTO VECCHIE E NUOVE AL SOGGETTO IN SESSIONE!!!!
		model.getDettaglioSoggetto().setModalitaPagamentoList(modalitaPagamentoSoggettolistToInsert);	
		model.getDettaglioSoggetto().setSediSecondarie(modelWeb.getSediSecondarie());
		
		resetVariable();
		handleVisualizationVariable(false, false, false, false, false, false, false, false);
		// inserisco il codice tipo persona
		model.getDettaglioSoggetto().getTipoSoggetto().setCodice(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode());
		model.getDettaglioSoggetto().setSediSecondarie(model.getListaSecondariaSoggetto());
		if (model.getDettaglioSoggetto().getSessoStringa() != null && !"".equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa())) {
			if (CostantiFin.MASCHIO.equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa().toUpperCase())) {
				model.getDettaglioSoggetto().setSesso(Sesso.MASCHIO);
			} else {
				model.getDettaglioSoggetto().setSesso(Sesso.FEMMINA);
			}
		}
		AggiornaSoggettoResponse response = null;
		
		//compongo la request del servizio:
		AggiornaSoggetto aggiornaSoggetto = convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(), false);
		
		aggiornaSoggetto.setCodificaAmbito(getcodiceAmbito());
		
		if (AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite())) {
		
			AggiornaSoggettoProvvisorio asp = new AggiornaSoggettoProvvisorio(aggiornaSoggetto);
			//richiamo il servizio di aggiornamento provvisorio:
			response = soggettoService.aggiornaSoggettoProvvisorio(asp);
	
		}else{
			//richiamo il servizio di aggiornamento:
			response = soggettoService.aggiornaSoggetto(aggiornaSoggetto);
		}
		
		if(isFallimento(response)) {
			//ci sono errori
			debug(methodName, "Errore nella risposta del servizio");
			
			clearErrorsAndMessages();
			
			addErrori(methodName, response);
			
			debug(methodName, "Model: " + model);
			
			return INPUT;
		}
		
		clearErrorsAndMessages();
		
		// messaggio di ok nella pagina di elenco soggetti
		addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore()); 
				                   
		
		model.getDettaglioSoggetto().setModalitaPagamentoList(response.getSoggettoAggiornato().getModalitaPagamentoList());
		
		
		Collections.sort(model.getDettaglioSoggetto().getModalitaPagamentoList(), ComparatorModalitaPagamentoSoggettoByCodice.INSTANCE);

		return SUCCESS;
	}
	
	/**
	 * Codice Ambito FIN
	 * @return
	 */
	protected String getcodiceAmbito() {
		return CostantiFin.AMBITO_FIN;
	}

	/**
	 * Inserisci ModPag Contanti
	 * @return
	 */
	public String inserisciModPagContanti() {
		setMethodName("inserisciModPagContanti");
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		boolean validate = false;
		
		boolean codFiscMancante = false;
		boolean quietanzanteMancante = false;
		
		handleModelBean();
		
		if(!StringUtils.isEmpty(modalitaPagamentoSoggettoToInsert.getCodiceFiscaleQuietanzante())){
			if(NumberUtils.isNumber(modalitaPagamentoSoggettoToInsert.getCodiceFiscaleQuietanzante())){
				
				if(!VerificaPartitaIva.controllaPIVA(modalitaPagamentoSoggettoToInsert.getCodiceFiscaleQuietanzante()).equalsIgnoreCase("OK")){
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Codice Fiscale Quietanzante", ""));
					addErrori(listaErrori);
					handleVisualizationVariable(true, false, true, false, false, false, false);
					return INPUT;
				}
				
			} else {
				if(!verificaFormaleCodiceFiscale(modalitaPagamentoSoggettoToInsert.getCodiceFiscaleQuietanzante().trim().toUpperCase())){
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Codice Fiscale Quietanzante", ""));
					addErrori(listaErrori);
					handleVisualizationVariable(true, false, true, false, false, false, false);
					return INPUT;
				}
				
				ValidaCF validaCF = new ValidaCF();
				if(!validaCF.controllaCF(modalitaPagamentoSoggettoToInsert.getCodiceFiscaleQuietanzante()).equals("OK")){
					addActionMessage("Verificare la correttezza del codice fiscale");
				}
			}
			
		} else {
			codFiscMancante = true;
		}
		
		if(modalitaPagamentoSoggettoToInsert.getSoggettoQuietanzante() != null){
			if(modalitaPagamentoSoggettoToInsert.getSoggettoQuietanzante().isEmpty()){
				quietanzanteMancante = true;
			}
		} else {
			quietanzanteMancante = true;
		}
		
		if(!codFiscMancante && quietanzanteMancante){
			validate = true;
		}
		
		if(codFiscMancante && !quietanzanteMancante){
			validate = true;
		}
		
		if(!codFiscMancante && !quietanzanteMancante){
			validate = true;
		}

		
		if(validate){
			
			handleModelBean();
			
			ModalitaPagamentoSoggetto modPagToInsert = modelWeb.getModalitaPagamentoSoggettoToInsert();
			
			//Gestisco la denominazione
			if(modelWeb.getDenominazioneAssociatoA().equals("Soggetto")){
				modPagToInsert.setCodiceSoggettoAssociato(Integer.valueOf(model.getDettaglioSoggetto().getUid()));
			} else {
				for(SedeSecondariaSoggetto sedSec : modelWeb.getSediSecondarie()){
					if(modelWeb.getDenominazioneAssociatoA().equals(sedSec.getDenominazione())){
						modPagToInsert.setCodiceSoggettoAssociato(Integer.valueOf(sedSec.getUid()));
					}
				}
					
			}
			
			//gestisco lo stato utente
			boolean gestSogg = AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSogg, sessionHandler.getAzioniConsentite());
			boolean gestSoggDec = AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite());
			
			modPagToInsert.setUid(0); //La setto a 0 per capire che sara da inserire lato backend
			
			if(modalitaPagamentoSoggettoToInsert.getNote() != null){
				modPagToInsert.setNote(modalitaPagamentoSoggettoToInsert.getNote());
			}
			
			
			modPagToInsert.setPerStipendi(modalitaPagamentoSoggettoToInsert.getPerStipendi());

			if(getDataScadenzaStringa() != null && !getDataScadenzaStringa().isEmpty()){
				// controllo la data di nascita per le persone
				if(getDataScadenzaStringa().length()!=10){
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
					addErrori(listaErrori);
					handleVisualizationVariable(true, false, true, false, false, false, false);
					return INPUT;
				}else {
					
					if (!DateUtility.isDate(getDataScadenzaStringa(), "dd/MM/yyyy")) {
						listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
						addErrori(listaErrori);
						handleVisualizationVariable(true, false, true, false, false, false, false);
						return INPUT;
					} else {
						DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						
						try {
							Date dataScadenza = df.parse(getDataScadenzaStringa());
							
							Calendar cFrom = Calendar.getInstance();							
							cFrom.set(Calendar.HOUR_OF_DAY, 0);
							cFrom.set(Calendar.MINUTE, 0);
							cFrom.set(Calendar.SECOND, 0);
							cFrom.set(Calendar.MILLISECOND, 0);
							Timestamp now = new Timestamp(cFrom.getTime().getTime());
							
							if(dataScadenza.compareTo(now)==-1){
								listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Data Scadenza", "(Non deve essere minore della data odierna)"));
								addErrori(listaErrori);
								handleVisualizationVariable(true, false, true, false, false, false, false);
								return INPUT;
							}
							modPagToInsert.setDataFineValidita(dataScadenza);
						} catch (ParseException e) {
							//  Auto-generated catch block
						}
					}
				}
			}
			
			if(modalitaPagamentoSoggettoToInsert.getDataNascitaQuietanzante()!=null 
					&& !modalitaPagamentoSoggettoToInsert.getDataNascitaQuietanzante().isEmpty() &&
					modalitaPagamentoSoggettoToInsert.getDataNascitaQuietanzante().length()==10){
				
				if (!DateUtility.isDate(modalitaPagamentoSoggettoToInsert.getDataNascitaQuietanzante(), "dd/MM/yyyy")) {
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data di nascita : dd/MM/yyyy","dd/MM/yyyy"));
					addErrori(listaErrori);
					handleVisualizationVariable(true, false, true, false, false, false, false);
					return INPUT;
				}	
				// corretto quindi setto data di nascita
				modPagToInsert.setDataNascitaQuietanzante(modalitaPagamentoSoggettoToInsert.getDataNascitaQuietanzante());
				
			}else{
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data di nascita : dd/MM/yyyy","dd/MM/yyyy"));
				addErrori(listaErrori);
				handleVisualizationVariable(true, false, true, false, false, false, false);
				return INPUT;
			}
			
			
			if(modalitaPagamentoSoggettoToInsert.getComuneNascita()!=null){
				// setto nazione e comune di nascita
				modPagToInsert.setComuneNascita(modalitaPagamentoSoggettoToInsert.getComuneNascita());
			}
			
			modPagToInsert.setCodiceFiscaleQuietanzante(modalitaPagamentoSoggettoToInsert.getCodiceFiscaleQuietanzante().trim());
			modPagToInsert.setSoggettoQuietanzante(modalitaPagamentoSoggettoToInsert.getSoggettoQuietanzante());
			modPagToInsert.setAssociatoA(modelWeb.getDenominazioneAssociatoA());
			
			if(gestSogg){
				modPagToInsert.setDescrizioneStatoModalitaPagamento("valido");
			}
			if(gestSoggDec){
				modPagToInsert.setDescrizioneStatoModalitaPagamento("provvisorio");
			}
			
			//Setto la gestione del tipo accredito
			modPagToInsert.setGestioneTipoAccredito(modPagToInsert.getTipoAccredito());
			
			//CORRETTIVA
			ModalitaAccreditoSoggetto modalitaScelta = getModalitaAccreditoScelta();
			
			modPagToInsert.setModalitaAccreditoSoggetto(modalitaScelta);
			
			List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettolistToInsert = model.getDettaglioSoggetto().getModalitaPagamentoList();
			if(modalitaPagamentoSoggettolistToInsert != null){
				modalitaPagamentoSoggettolistToInsert.add(modPagToInsert);
			} else {
				modalitaPagamentoSoggettolistToInsert = new ArrayList<ModalitaPagamentoSoggetto>();
				modalitaPagamentoSoggettolistToInsert.add(modPagToInsert);
			}
			
			
			if (Boolean.TRUE.equals(modPagToInsert.getPerStipendi())){
				ModalitaPagamentoSoggetto mdpPerStipendiIsSet = null;

				for (ModalitaPagamentoSoggetto m : modalitaPagamentoSoggettolistToInsert){
					if (m.getUid() != modPagToInsert.getUid()){
						mdpPerStipendiIsSet = getMdpPerStipendi(m, modPagToInsert);
					}
				}

				if (mdpPerStipendiIsSet != null){
					addPersistentActionWarning(ErroreFin.WARNING_GENERICO.getErrore(String.format(
							"Attenzione: modalit&agrave; di pagamento impostata 'Per gli stipendi'. In precedenza era impostata la modalit&agrave; di pagamento [ %s ].",
							mdpPerStipendiIsSet.getCodiceModalitaPagamento())).getTesto());
				}
			}			
			

			//AGGIUNGO LE MODALITA DI PAGAMENTO VECCHIE E NUOVE AL SOGGETTO IN SESSIONE!!!!
			model.getDettaglioSoggetto().setModalitaPagamentoList(modalitaPagamentoSoggettolistToInsert);	
			model.getDettaglioSoggetto().setSediSecondarie(modelWeb.getSediSecondarie());
			
			resetVariable();
			handleVisualizationVariable(false, false, false, false, false, false, false);
			// inserisco il codice tipo persona
			model.getDettaglioSoggetto().getTipoSoggetto().setCodice(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode());
			model.getDettaglioSoggetto().setSediSecondarie(model.getListaSecondariaSoggetto());
			if (model.getDettaglioSoggetto().getSessoStringa() != null && !"".equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa())) {
				if (CostantiFin.MASCHIO.equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa().toUpperCase())) {
					model.getDettaglioSoggetto().setSesso(Sesso.MASCHIO);
				} else {
					model.getDettaglioSoggetto().setSesso(Sesso.FEMMINA);
				}
			}
			AggiornaSoggettoResponse response = null;
			
			AggiornaSoggetto aggiornaSoggetto = convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(), false);
			
			aggiornaSoggetto.setCodificaAmbito(getcodiceAmbito());
			
			
			if (AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite())) {
			
				AggiornaSoggettoProvvisorio asp = new AggiornaSoggettoProvvisorio(aggiornaSoggetto);
				//richiamo il servizio di aggiornamento provvisorio:
				response = soggettoService.aggiornaSoggettoProvvisorio(asp);
		
			}else{
				//richiamo il servizio di aggiornamento:
				response = soggettoService.aggiornaSoggetto(aggiornaSoggetto);
			}
			
			if(isFallimento(response)) {
				//ci sono errori
				debug(methodName, "Errore nella risposta del servizio");
				
				clearErrorsAndMessages();
				
				addErrori(methodName, response);
				
				debug(methodName, "Model: " + model);
				
				return INPUT;
			}
			
			clearErrorsAndMessages();
			
			// messaggio di ok nella pagina di elenco soggetti
			addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore()); 
					                   
			
			model.getDettaglioSoggetto().setModalitaPagamentoList(response.getSoggettoAggiornato().getModalitaPagamentoList());
			
			Collections.sort(model.getDettaglioSoggetto().getModalitaPagamentoList(), ComparatorModalitaPagamentoSoggettoByCodice.INSTANCE);

			return SUCCESS;
			
		} else {
			
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Inserire Codice Fiscale Quietanzante o Soggetto Quietanzante"));
			addErrori(listaErrori);
			handleVisualizationVariable(true, false, true, false, false, false, false);
			return INPUT;
		}
		

	}

	/**
	 * Get Modalita Accredito Scelta
	 * @return
	 */
	private ModalitaAccreditoSoggetto getModalitaAccreditoScelta(){
		ModalitaAccreditoSoggetto modalitaScelta = null;
		
		for(CodificaFin tipo : model.getListaTipoAccredito()){
			if(tipo.getId().equals(Integer.valueOf(modelWeb.getIdAccreditoTipoSelected()))){
				//trovato
				modalitaScelta = new ModalitaAccreditoSoggetto();
				
				modalitaScelta.setCodice(tipo.getCodice());
				modalitaScelta.setDescrizione(tipo.getDescrizione());
				modalitaScelta.setUid(tipo.getId()); 
				
				break;
			}	
		}
			
		return modalitaScelta;
	}
	
	/**
	 * Inserisci ModPag Cessione
	 * @return
	 */
	public String inserisciModPagCessione(){
		
		setMethodName("inserisciModPagCessione");
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		handleModelBean();
		
		ModalitaPagamentoSoggetto modPagToInsert = modelWeb.getModalitaPagamentoSoggettoToInsert();
		
		//Gestisco la denominazione
		if(modelWeb.getDenominazioneAssociatoA().equals("Soggetto")){
			modPagToInsert.setCodiceSoggettoAssociato(Integer.valueOf(model.getDettaglioSoggetto().getCodiceSoggetto()));
		} else {
			for(SedeSecondariaSoggetto sedSec : modelWeb.getSediSecondarie()){
				if(modelWeb.getDenominazioneAssociatoA().equals(sedSec.getDenominazione())){
					modPagToInsert.setCodiceSoggettoAssociato(Integer.valueOf(sedSec.getUid()));
				}
			}
				
		}
		
		//gestisco lo stato utente
		boolean gestSogg = AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSogg, sessionHandler.getAzioniConsentite());
		boolean gestSoggDec = AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite());
		
		if(modalitaPagamentoSoggettoToInsert.getNote() != null){
			modPagToInsert.setNote(modalitaPagamentoSoggettoToInsert.getNote());
		}
		if(getDataScadenzaStringa() != null && !getDataScadenzaStringa().isEmpty()){
			// controllo la data di nascita per le persone
			if(getDataScadenzaStringa().length()!=10){
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
				addErrori(listaErrori);
				handleVisualizationVariable(true, false, false, true, true, true, false);
				return INPUT;
			}else {
				
				if (!DateUtility.isDate(getDataScadenzaStringa(), "dd/MM/yyyy")) {
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
					addErrori(listaErrori);
					handleVisualizationVariable(true, false, false, true, true, true, false);
					return INPUT;
				} else {
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					
					try {
						Date dataScadenza = df.parse(getDataScadenzaStringa());
						
						Calendar cFrom = Calendar.getInstance();							
						cFrom.set(Calendar.HOUR_OF_DAY, 0);
						cFrom.set(Calendar.MINUTE, 0);
						cFrom.set(Calendar.SECOND, 0);
						cFrom.set(Calendar.MILLISECOND, 0);
						Timestamp now = new Timestamp(cFrom.getTime().getTime());
						
						if(dataScadenza.compareTo(now)==-1){
							listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Data Scadenza", "(Non deve essere minore della data odierna)"));
							addErrori(listaErrori);
							handleVisualizationVariable(true, false, false, true, true, true, false);
							return INPUT;
						}
						modPagToInsert.setDataFineValidita(dataScadenza);
					} catch (ParseException e) {
						//  Auto-generated catch block
					}
				}

			}
		}
		
		Integer idModPagSelected = modalitaPagamentoSoggettoToInsert.getUid();
		ModalitaPagamentoSoggetto modPagCesDef = new ModalitaPagamentoSoggetto();
		
		for(ModalitaPagamentoSoggetto modApp : modelWeb.getModalitaPagamentoCessione()){
			if(modApp.getUid() == idModPagSelected.intValue()){
				modPagCesDef = modApp;
			}
		}
		
		if(modelWeb.getSedisecondariaSoggettoCessione() != null){
			boolean isSedeSec = false;
			for(SedeSecondariaSoggetto sedeApp : modelWeb.getSedisecondariaSoggettoCessione()){
				if(sedeApp.getDenominazione().equals(modPagCesDef.getAssociatoA())){
					isSedeSec = true;
					modPagToInsert.setCessioneCodSoggetto(sedeApp.getCodiceSedeSecondaria());
				}
			}
			if(!isSedeSec){
				modPagToInsert.setCessioneCodSoggetto(String.valueOf(modelWeb.getDettaglioSoggettoCessione().getCodiceSoggetto()));
			}
		} else {
			modPagToInsert.setCessioneCodSoggetto(String.valueOf(modelWeb.getDettaglioSoggettoCessione().getCodiceSoggetto()));
		}
		
		modPagToInsert.setAssociatoA(modelWeb.getDenominazioneAssociatoA());
		
		if(gestSogg){
			modPagToInsert.setDescrizioneStatoModalitaPagamento("valido");
		}
		if(gestSoggDec){
			modPagToInsert.setDescrizioneStatoModalitaPagamento("provvisorio");
		}
		
		modPagToInsert.setUid(0); //La setto a 0 per capire che sara da inserire lato backend
		
		modPagToInsert.setCessioneCodModPag(String.valueOf(modalitaPagamentoSoggettoToInsert.getUid()));
		ModalitaPagamentoSoggetto mdpCess2 = new ModalitaPagamentoSoggetto();
		mdpCess2.setUid(modalitaPagamentoSoggettoToInsert.getUid());
		modPagToInsert.setModalitaPagamentoSoggettoCessione2(mdpCess2);
		
		//Setto la gestione del tipo accredito
		modPagToInsert.setGestioneTipoAccredito(modPagToInsert.getTipoAccredito());
		
		//CORRETTIVA
		ModalitaAccreditoSoggetto modalitaScelta = getModalitaAccreditoScelta();

		modPagToInsert.setModalitaAccreditoSoggetto(modalitaScelta);
		
		
		List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettolistToInsert = new ArrayList<ModalitaPagamentoSoggetto>();
		
		if(model.getDettaglioSoggetto().getModalitaPagamentoList() != null) {
			for (ModalitaPagamentoSoggetto mps : model.getDettaglioSoggetto().getModalitaPagamentoList()) {
				if (StringUtils.isBlank(mps.getCessioneCodModPag()) || !mps.getCessioneCodModPag().equals(modPagToInsert.getCessioneCodModPag())) {
					modalitaPagamentoSoggettolistToInsert.add(mps);
				}
			}
		} 

		modalitaPagamentoSoggettolistToInsert.add(modPagToInsert);

		
		//AGGIUNGO LE MODALITA DI PAGAMENTO VECCHIE E NUOVE AL SOGGETTO IN SESSIONE!!!!
		model.getDettaglioSoggetto().setModalitaPagamentoList(modalitaPagamentoSoggettolistToInsert);	
		model.getDettaglioSoggetto().setSediSecondarie(modelWeb.getSediSecondarie());
		
		resetVariable();
		handleVisualizationVariable(false, false, false, false, false, false, false);
		// inserisco il codice tipo persona
		model.getDettaglioSoggetto().getTipoSoggetto().setCodice(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode());
		model.getDettaglioSoggetto().setSediSecondarie(model.getListaSecondariaSoggetto());
		if (model.getDettaglioSoggetto().getSessoStringa() != null && !"".equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa())) {
			if (CostantiFin.MASCHIO.equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa().toUpperCase())) {
				model.getDettaglioSoggetto().setSesso(Sesso.MASCHIO);
			} else {
				model.getDettaglioSoggetto().setSesso(Sesso.FEMMINA);
			}
		}
		AggiornaSoggettoResponse response = null;
		
		AggiornaSoggetto aggiornaSoggetto = convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(), false);
		
		aggiornaSoggetto.setCodificaAmbito(getcodiceAmbito());
		
		if (AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite())) {
		
			AggiornaSoggettoProvvisorio asp = new AggiornaSoggettoProvvisorio(aggiornaSoggetto);
			//richiamo il servizio di aggiornamento provvisiorio:
			response = soggettoService.aggiornaSoggettoProvvisorio(asp);
	
		}else{
			//richiamo il servizio di aggiornamento:
			response = soggettoService.aggiornaSoggetto(aggiornaSoggetto);
		}
		
		if(isFallimento(response)) {
			//ci sono errori
			debug(methodName, "Errore nella risposta del servizio");
			
			clearErrorsAndMessages();
			
			addErrori(methodName, response);
			
			debug(methodName, "Model: " + model);
			
			return INPUT;
		}
		
		clearErrorsAndMessages();
		
		// messaggio di ok nella pagina di elenco soggetti
		addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore()); 
				                   
		
		model.getDettaglioSoggetto().setModalitaPagamentoList(response.getSoggettoAggiornato().getModalitaPagamentoList());
		
		
		Collections.sort(model.getDettaglioSoggetto().getModalitaPagamentoList(), ComparatorModalitaPagamentoSoggettoByCodice.INSTANCE);

		return SUCCESS;
		
	}
	
	public String annullaInserimento(){
		setAzioneAggiorna(false);
		//Porto a "valore 0" tutte le variabili
		resetVariable();
		return SUCCESS;
	}
	
	
	public String aggiornaMDP(){
		setMethodName("aggiornaMDP");
		
		setAzioneAggiorna(true);
		
		String codiceSoggettoDaAggiornare = getAggiornaCodiceSoggetto();
		
		for(ModalitaPagamentoSoggetto soggApp : model.getDettaglioSoggetto().getModalitaPagamentoList()){
			if(soggApp.getUid() == Integer.valueOf(codiceSoggettoDaAggiornare).intValue()){
				soggettoDaAggiornare = soggApp;
			}
		}		
		
		int idTipoGruppoAccreditoApp = 0;
		for(CodificaFin fin : model.getListaTipoAccredito()){
			if(fin.getCodice().equalsIgnoreCase(soggettoDaAggiornare.getModalitaAccreditoSoggetto().getCodice())){
				idTipoGruppoAccreditoApp = fin.getId();
			}
		}
		
		RicercaGruppoTipoAccreditoPerChiave req = new RicercaGruppoTipoAccreditoPerChiave();
		req.setRichiedente(sessionHandler.getRichiedente());
		req.setTipoId(idTipoGruppoAccreditoApp);
		//richiamo il servizio
		RicercaGruppoTipoAccreditoPerChiaveResponse resp = genericService.findGruppoTipoAccreditoPerPk(req);
		
		String codiceGruppoAccredito = resp.getGruppoTipoAccredito().getCodice();
		
		if(codiceGruppoAccredito.equals(CostantiFin.D_ACCREDITO_TIPO_CODE_Circuito_bancario) 
				|| codiceGruppoAccredito.equals(CostantiFin.D_ACCREDITO_TIPO_CODE_Circuito_Banca_d_Italia) 
					|| codiceGruppoAccredito.equals(CostantiFin.D_ACCREDITO_TIPO_CODE_Circuito_Postale)){
			soggettoDaAggiornare.setTipoAccredito(TipoAccredito.valueOf(codiceGruppoAccredito));
			handleAggiornaVisualization(true, false, false, false, false, false);
			
			// setto l'ancora della pagina
			setAncoraMdpVisualizza(true);
		}
		if(codiceGruppoAccredito.equals(CostantiFin.D_ACCREDITO_TIPO_CODE_Contanti)){
			handleAggiornaVisualization(false, true, false, false, false, false);

			// setto l'ancora della pagina
			setAncoraMdpVisualizza(true);
		}
		if(codiceGruppoAccredito.equals(CostantiFin.D_ACCREDITO_TIPO_CODE_Generico)){
			handleAggiornaVisualization(false, false, false, false, false, false, true);

			// setto l'ancora della pagina
			setAncoraMdpVisualizza(true);
		}
		if(codiceGruppoAccredito.equals(CostantiFin.D_ACCREDITO_TIPO_CODE_Cessione_del_credito) || codiceGruppoAccredito.equals(CostantiFin.D_ACCREDITO_TIPO_CODE_Cessione_dell_incasso)){
			modelWeb.setModalitaPagamentoSoggettoToUpdate(soggettoDaAggiornare);
			handleAggiornaVisualization(false, false, false, false, true, false);
			
			// setto l'ancora della pagina
			setAncoraMdpVisualizza(true);
		}
		
		setAggiornaTipoAccredito(String.valueOf(soggettoDaAggiornare.getModalitaAccreditoSoggetto().getCodice()));
		if(soggettoDaAggiornare.getDataFineValidita() != null){
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			setDataScadenzaStringa(df.format(soggettoDaAggiornare.getDataFineValidita()));
		}
		
		handleModelBean();

		//patch per sistemare gli spazi in piu che uscivano nel caso fosse vuoto
		if(soggettoDaAggiornare.getCodiceFiscaleQuietanzante()!= null && 
				soggettoDaAggiornare.getCodiceFiscaleQuietanzante().trim().isEmpty()){
			soggettoDaAggiornare.setCodiceFiscaleQuietanzante("");
		}
		
		modelWeb.setModalitaPagamentoSoggettoToUpdate(soggettoDaAggiornare);
		
		sessionHandler.setParametro(FinSessionParameter.MODALITA_PAGAMENTO_MODEL, modelWeb);
		
		handleTipoAccreditoSoggettoUpdate(codiceGruppoAccredito);
		
		sessionHandler.setParametro(FinSessionParameter.MODALITA_PAGAMENTO_MODEL, modelWeb);
		
		return SUCCESS;
	}
	
	public String aggiornaModPagContoCorrente(){
		setMethodName("aggiornaModPagContoCorrente");
		setAzioneAggiorna(true);
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		boolean validate = false;
		
		handleModelBean();
		
		switch (modelWeb.getModalitaPagamentoSoggettoToUpdate().getTipoAccredito()) {
		case CBI:
			
			boolean contoCorrenteMancanteCBI = false;
			
			if(soggettoDaAggiornare.getContoCorrente() != null){
				if(!soggettoDaAggiornare.getContoCorrente().isEmpty()){
					if(soggettoDaAggiornare.getContoCorrente().trim().length() != 7){
						listaErrori.add(ErroreFin.FORMATO_BANCARIO_NON_VALIDO.getErrore("Conto Corrente", "7"));
						addErrori(listaErrori);
						handleAggiornaVisualization(true, false, false, false, false, false);
						return INPUT;
					}
				} else {
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Conto Corrente obbligatorio"));
					addErrori(listaErrori);
					handleAggiornaVisualization(true, false, false, false, false, false);
					return INPUT;
				}
				
			} else {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Conto Corrente obbligatorio"));
				addErrori(listaErrori);
				handleAggiornaVisualization(true, false, false, false, false, false);
				return INPUT;
			}
			
			if(!contoCorrenteMancanteCBI)
				validate = true;
			
			break;
		case CB:
		
			
			checkDatiContoCorrente(soggettoDaAggiornare, listaErrori);

			if (listaErrori.isEmpty())
				validate = true;
			else
			{
				addErrori(listaErrori);
				handleAggiornaVisualization(true, false, false, false, false, false);

				return INPUT;
			}
						
			
			
			
			break;
		case CCP:

			boolean contoCorrenteMancanteCCP = false;
			
			if(soggettoDaAggiornare.getContoCorrente() != null){
				if(!soggettoDaAggiornare.getContoCorrente().isEmpty()){
					if(soggettoDaAggiornare.getContoCorrente().trim().length() != 12){
						listaErrori.add(ErroreFin.FORMATO_BANCARIO_NON_VALIDO.getErrore("Conto Corrente", "12"));
						addErrori(listaErrori);
						handleAggiornaVisualization(true, false, false, false, false, false);
						return INPUT;
					} 
				} else {
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Conto Corrente obbligatorio"));
					addErrori(listaErrori);
					handleAggiornaVisualization(true, false, false, false, false, false);
					return INPUT;
				}

			} else {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Conto Corrente obbligatorio"));
				addErrori(listaErrori);
				handleAggiornaVisualization(true, false, false, false, false, false);
				return INPUT;
			}
			
			if(!contoCorrenteMancanteCCP)
				validate = true;
			
			break;
			
		default:
			break;
		
		}
		
		if(validate){
						
			ModalitaPagamentoSoggetto modPagToUpdate = modelWeb.getModalitaPagamentoSoggettoToUpdate();
			//gestisco lo stato utente
			
			boolean gestSogg = AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSogg, sessionHandler.getAzioniConsentite());
			boolean gestSoggDec = AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite());
			
			
			if(soggettoDaAggiornare.getIban() != null){
				modPagToUpdate.setIban(soggettoDaAggiornare.getIban().trim());
			}
			
			if(soggettoDaAggiornare.getDenominazioneBanca() != null){
				modPagToUpdate.setDenominazioneBanca(soggettoDaAggiornare.getDenominazioneBanca().trim());
			}
			
			if(soggettoDaAggiornare.getBic() != null){
				modPagToUpdate.setBic(soggettoDaAggiornare.getBic().trim());
			}
			
			if(soggettoDaAggiornare.getContoCorrente() != null){
				modPagToUpdate.setContoCorrente(soggettoDaAggiornare.getContoCorrente().trim());
			}
			
			if(soggettoDaAggiornare.getNote() != null){
				modPagToUpdate.setNote(soggettoDaAggiornare.getNote());
			}
			
			modPagToUpdate.setPerStipendi(soggettoDaAggiornare.getPerStipendi());

			// intestazione conto
			if(soggettoDaAggiornare.getIntestazioneConto()!=null){
				modPagToUpdate.setIntestazioneConto(soggettoDaAggiornare.getIntestazioneConto());
			}
			
			if(getDataScadenzaStringa() != null){
				if(!getDataScadenzaStringa().isEmpty()){
					
				// controllo la data di nascita per le persone
				if(getDataScadenzaStringa().length()!=10){
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
					addErrori(listaErrori);
					handleAggiornaVisualization(true, false, false, false, false, false);
					return INPUT;
				}else {
					
					if (!DateUtility.isDate(getDataScadenzaStringa(), "dd/MM/yyyy")) {
						listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
						addErrori(listaErrori);
						handleAggiornaVisualization(true, false, false, false, false, false);
						return INPUT;
					} else {
						DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						
						try {
							Date dataScadenza = df.parse(getDataScadenzaStringa());
							
							Calendar cFrom = Calendar.getInstance();							
							cFrom.set(Calendar.HOUR_OF_DAY, 0);
							cFrom.set(Calendar.MINUTE, 0);
							cFrom.set(Calendar.SECOND, 0);
							cFrom.set(Calendar.MILLISECOND, 0);
							Timestamp now = new Timestamp(cFrom.getTime().getTime());
							
							if(dataScadenza.compareTo(now)==-1){
								listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Data Scadenza", "(Non deve essere minore della data odierna)"));
								addErrori(listaErrori);
								handleAggiornaVisualization(true, false, false, false, false, false);
								return INPUT;
							}
							modPagToUpdate.setDataFineValidita(dataScadenza);							
						} catch (ParseException e) {
						}
					}

					}
				} else {
					modPagToUpdate.setDataFineValidita(null);
				}
			} else {
				modPagToUpdate.setDataFineValidita(null);
			}
			
			
			//Gestisco il tipo accredito
			modPagToUpdate.setTipoAccredito(modPagToUpdate.getTipoAccredito());
			modPagToUpdate.setModalitaAccreditoSoggetto(modPagToUpdate.getModalitaAccreditoSoggetto());
			
			if(gestSogg){
				modPagToUpdate.setDescrizioneStatoModalitaPagamento("valido");
			}
			if(gestSoggDec){
				if(!modPagToUpdate.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(CostantiFin.STATO_IN_MODIFICA_no_underscore)){
					if(modPagToUpdate.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("provvisorio")){
						if(decentratoSuPropriaOccorrenza(modPagToUpdate.getLoginCreazione()) || decentratoSuPropriaOccorrenza(modPagToUpdate.getLoginModifica())){
							modPagToUpdate.setModificaPropriaOccorrenza(true);
						}
					}
				}
			}
			
			List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoListToUpdateApp = model.getDettaglioSoggetto().getModalitaPagamentoList();
			List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoListToUpdate = new ArrayList<ModalitaPagamentoSoggetto>();
			modalitaPagamentoSoggettoListToUpdate.addAll(model.getDettaglioSoggetto().getModalitaPagamentoList());
			ModalitaPagamentoSoggetto mdpPerStipendiIsSet = null;
			for(ModalitaPagamentoSoggetto soggApp : modalitaPagamentoSoggettoListToUpdateApp){
				if(soggApp.getUid() == modPagToUpdate.getUid()){
					modalitaPagamentoSoggettoListToUpdate.remove(soggApp);
					modalitaPagamentoSoggettoListToUpdate.add(modPagToUpdate);
				} else {
					mdpPerStipendiIsSet = getMdpPerStipendi(soggApp, modPagToUpdate);
				}
			}
			
			if (mdpPerStipendiIsSet != null){
				addPersistentActionWarning(ErroreFin.WARNING_GENERICO.getErrore(String.format(
						"Attenzione: modalit&agrave; di pagamento impostata 'Per gli stipendi'. In precedenza era impostata la modalit&agrave; di pagamento [ %s ].",
						mdpPerStipendiIsSet.getCodiceModalitaPagamento())).getTesto());
			}


			//AGGIUNGO LE MODALITA DI PAGAMENTO VECCHIE E aggiornate AL SOGGETTO IN SESSIONE!!!!
			model.getDettaglioSoggetto().setModalitaPagamentoList(modalitaPagamentoSoggettoListToUpdate);
			model.getDettaglioSoggetto().setSediSecondarie(modelWeb.getSediSecondarie());
			
			resetVariable();
			handleAggiornaVisualization(false, false, false, false, false, false);
			setAzioneAggiorna(false);
			// inserisco il codice tipo persona
			model.getDettaglioSoggetto().getTipoSoggetto().setCodice(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode());
			model.getDettaglioSoggetto().setSediSecondarie(model.getListaSecondariaSoggetto());
			if (model.getDettaglioSoggetto().getSessoStringa() != null && !"".equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa())) {
				if (CostantiFin.MASCHIO.equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa().toUpperCase())) {
					model.getDettaglioSoggetto().setSesso(Sesso.MASCHIO);
				} else {
					model.getDettaglioSoggetto().setSesso(Sesso.FEMMINA);
				}
			}
			AggiornaSoggettoResponse response = null;
			
			AggiornaSoggetto aggiornaSoggetto = convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(), false);
			
			aggiornaSoggetto.setCodificaAmbito(getcodiceAmbito());
			
			if (AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite())) {
			
				AggiornaSoggettoProvvisorio asp = new AggiornaSoggettoProvvisorio(aggiornaSoggetto);
				//richiamo il servizio di aggiornamento provvisorio:
				response = soggettoService.aggiornaSoggettoProvvisorio(asp);
		
			}else{
				//richiamo il servizio di aggiornamento:
				response = soggettoService.aggiornaSoggetto(aggiornaSoggetto);
			}
			
			
			if(isFallimento(response)) {
				//ci sono errori
				debug(methodName, "Errore nella risposta del servizio");
				
				clearErrorsAndMessages();
				
				addErrori(methodName, response);
				
				debug(methodName, "Model: " + model);
				
				//reimposto le modalita' originarie (quelle prime dell'errore)
				this.setModel(aggiornaSoggettoModelCachePerAggiorna);
				
				return INPUT;
			}

			clearErrorsAndMessages();

			// messaggio di ok nella pagina di elenco soggetti
			addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore()); 
					                   
			
			model.getDettaglioSoggetto().setModalitaPagamentoList(response.getSoggettoAggiornato().getModalitaPagamentoList());
			
			return SUCCESS;
			
		} else {

			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Inserire o IBAN oppure Conto Corrente e Bic"));
			addErrori(listaErrori);
			handleAggiornaVisualization(true, false, false, false, false, false);
			return INPUT;
		}

	}
	
	public String aggiornaContante(){
		setMethodName("aggiornaContante");
		
		
		setAzioneAggiorna(true);
		List<Errore> listaErrori = new ArrayList<Errore>();
		
//		controllo formale codice fiscale    
		boolean validate = false;
		
		boolean codFiscMancante = false;
		boolean quietanzanteMancante = false;
		
		
		
		if(!StringUtils.isEmpty(soggettoDaAggiornare.getCodiceFiscaleQuietanzante())){
			if(NumberUtils.isNumber(soggettoDaAggiornare.getCodiceFiscaleQuietanzante())){
				
				if(!VerificaPartitaIva.controllaPIVA(soggettoDaAggiornare.getCodiceFiscaleQuietanzante()).equalsIgnoreCase("OK")){
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Codice Fiscale Quietanzante", ""));
					addErrori(listaErrori);
					handleAggiornaVisualization(false, true, false, false, false, false);
					return INPUT;
				}
				
			} else {
				if(!verificaFormaleCodiceFiscale(soggettoDaAggiornare.getCodiceFiscaleQuietanzante().trim().toUpperCase())){
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Codice Fiscale Quietanzante", ""));
					addErrori(listaErrori);
					handleAggiornaVisualization(false, true, false, false, false, false);
					return INPUT;
				}
				
				ValidaCF validaCF = new ValidaCF();
				if(!validaCF.controllaCF(soggettoDaAggiornare.getCodiceFiscaleQuietanzante()).equals("OK")){
					addActionMessage("Verificare la correttezza del codice fiscale");
				}
			}
			
		} else {
			codFiscMancante = true;
		}
		
		if(soggettoDaAggiornare.getSoggettoQuietanzante() != null){
			if(soggettoDaAggiornare.getSoggettoQuietanzante().isEmpty()){
				quietanzanteMancante = true;
			}
		} else {
			quietanzanteMancante = true;
		}
		
		if(!codFiscMancante && quietanzanteMancante){
			validate = true;
		}
		
		if(codFiscMancante && !quietanzanteMancante){
			validate = true;
		}
		
		if(!codFiscMancante && !quietanzanteMancante){
			validate = true;
		}

		
		if(validate){
			
			handleModelBean();
			
			ModalitaPagamentoSoggetto modPagToUpdate = modelWeb.getModalitaPagamentoSoggettoToUpdate();
			
			//gestisco lo stato utente
			boolean gestSogg = AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSogg, sessionHandler.getAzioniConsentite());
			boolean gestSoggDec = AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite());
			
			if(soggettoDaAggiornare.getNote() != null){
				modPagToUpdate.setNote(soggettoDaAggiornare.getNote());
			}
			
			modPagToUpdate.setPerStipendi(soggettoDaAggiornare.getPerStipendi());

			
			if(getDataScadenzaStringa() != null && !getDataScadenzaStringa().isEmpty()){
				// controllo la data di nascita per le persone
				if(getDataScadenzaStringa().length()!=10){
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
					addErrori(listaErrori);
					handleAggiornaVisualization(false, true, false, false, false, false);
					return INPUT;
				}else {
					
					if (!DateUtility.isDate(getDataScadenzaStringa(), "dd/MM/yyyy")) {
						listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
						addErrori(listaErrori);
						handleAggiornaVisualization(false, true, false, false, false, false);
						return INPUT;
					} else {
						DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						
						try {
							Date dataScadenza = df.parse(getDataScadenzaStringa());
							
							Calendar cFrom = Calendar.getInstance();							
							cFrom.set(Calendar.HOUR_OF_DAY, 0);
							cFrom.set(Calendar.MINUTE, 0);
							cFrom.set(Calendar.SECOND, 0);
							cFrom.set(Calendar.MILLISECOND, 0);
							Timestamp now = new Timestamp(cFrom.getTime().getTime());
							
							if(dataScadenza.compareTo(now)==-1){
								listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Data Scadenza", "(Non deve essere minore della data odierna)"));
								addErrori(listaErrori);
								handleAggiornaVisualization(false, true, false, false, false, false);
								return INPUT;
							}
							modPagToUpdate.setDataFineValidita(dataScadenza);		
						} catch (ParseException e) {
							
						}
					}

				}
			} else {
				modPagToUpdate.setDataFineValidita(null);
			}
			
			// verifica correttezza data di nascita
			if(soggettoDaAggiornare.getDataNascitaQuietanzante() != null && 
			   !soggettoDaAggiornare.getDataNascitaQuietanzante().isEmpty()){
				
			   // se c'e' scritto qualcosa nella data lo devo verificare
			   if(!DateUtility.isDate(soggettoDaAggiornare.getDataNascitaQuietanzante(), "dd/MM/yyyy")) {
				   listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
				   addErrori(listaErrori);
				   handleAggiornaVisualization(false, true, false, false, false, false);
				   return INPUT;
			   }
			}
			
			// nuovi campi
			// luogo e stato nascita quietanzante
			
			if(soggettoDaAggiornare.getComuneNascita()!=null){
				modPagToUpdate.setComuneNascita(soggettoDaAggiornare.getComuneNascita());
			}

			// data di nascita
			if(soggettoDaAggiornare.getDataNascitaQuietanzante()!=null){
				modPagToUpdate.setDataNascitaQuietanzante(soggettoDaAggiornare.getDataNascitaQuietanzante());
			}
			
			
			// fine - nuovi campi
			
			modPagToUpdate.setCodiceFiscaleQuietanzante(soggettoDaAggiornare.getCodiceFiscaleQuietanzante().trim());
			modPagToUpdate.setSoggettoQuietanzante(soggettoDaAggiornare.getSoggettoQuietanzante());
			
			if(gestSogg){
				modPagToUpdate.setDescrizioneStatoModalitaPagamento("valido");
			}
			if(gestSoggDec){
				if(!modPagToUpdate.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(CostantiFin.STATO_IN_MODIFICA_no_underscore)){
					if(modPagToUpdate.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("provvisorio")){
						if(decentratoSuPropriaOccorrenza(modPagToUpdate.getLoginCreazione()) || decentratoSuPropriaOccorrenza(modPagToUpdate.getLoginModifica())){
							modPagToUpdate.setModificaPropriaOccorrenza(true);
						}
					}
				}
			}
					
			
			//CORRETTIVA
			List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoListToUpdateApp = model.getDettaglioSoggetto().getModalitaPagamentoList();
			List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoListToUpdate = new ArrayList<ModalitaPagamentoSoggetto>();
			modalitaPagamentoSoggettoListToUpdate.addAll(model.getDettaglioSoggetto().getModalitaPagamentoList());
			ModalitaPagamentoSoggetto mdpPerStipendiIsSet = null;
			for(ModalitaPagamentoSoggetto soggApp : modalitaPagamentoSoggettoListToUpdateApp){
				if(soggApp.getUid() == modPagToUpdate.getUid()){	
					modalitaPagamentoSoggettoListToUpdate.remove(soggApp);
					modalitaPagamentoSoggettoListToUpdate.add(modPagToUpdate);
				} else {
					mdpPerStipendiIsSet = getMdpPerStipendi(soggApp, modPagToUpdate);
				}
			}
			
			if (mdpPerStipendiIsSet != null){
				addPersistentActionWarning(ErroreFin.WARNING_GENERICO.getErrore(String.format(
						"Attenzione: modalit&agrave; di pagamento impostata 'Per gli stipendi'. In precedenza era impostata la modalit&agrave; di pagamento [ %s ].",
						mdpPerStipendiIsSet.getCodiceModalitaPagamento())).getTesto());
			}
			
			//AGGIUNGO LE MODALITA DI PAGAMENTO VECCHIE E NUOVE AL SOGGETTO IN SESSIONE!!!!
			model.getDettaglioSoggetto().setModalitaPagamentoList(modalitaPagamentoSoggettoListToUpdate);	
			model.getDettaglioSoggetto().setSediSecondarie(modelWeb.getSediSecondarie());
			
			resetVariable();
			handleAggiornaVisualization(false, false, false, false, false, false);
			setAzioneAggiorna(false);
			// inserisco il codice tipo persona
			model.getDettaglioSoggetto().getTipoSoggetto().setCodice(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode());
			model.getDettaglioSoggetto().setSediSecondarie(model.getListaSecondariaSoggetto());
			if (model.getDettaglioSoggetto().getSessoStringa() != null && !"".equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa())) {
				if (CostantiFin.MASCHIO.equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa().toUpperCase())) {
					model.getDettaglioSoggetto().setSesso(Sesso.MASCHIO);
				} else {
					model.getDettaglioSoggetto().setSesso(Sesso.FEMMINA);
				}
			}
			AggiornaSoggettoResponse response = null;
			
			AggiornaSoggetto aggiornaSoggetto = convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(), false);
			
			aggiornaSoggetto.setCodificaAmbito(getcodiceAmbito());
			
			if (AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite())) {
			
				AggiornaSoggettoProvvisorio asp = new AggiornaSoggettoProvvisorio(aggiornaSoggetto);
				//richiamo il servizio di aggiornamento provvisorio:
				response = soggettoService.aggiornaSoggettoProvvisorio(asp);
		
			}else{
				//richiamo il servizio di aggiornamento:
				response = soggettoService.aggiornaSoggetto(aggiornaSoggetto);
			}
			
			
			if(isFallimento(response)) {
				//ci sono errori
				debug(methodName, "Errore nella risposta del servizio");
				
				clearErrorsAndMessages();
				
				addErrori(methodName, response);
				
				debug(methodName, "Model: " + model);
				
				//reimposto le modalita' originarie (quelle prime dell'errore)
				this.setModel(aggiornaSoggettoModelCachePerAggiorna);
				
				return INPUT;
			}
			
			clearErrorsAndMessages();
			
			// messaggio di ok nella pagina di elenco soggetti
			addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore()); 
					                   
			
			model.getDettaglioSoggetto().setModalitaPagamentoList(response.getSoggettoAggiornato().getModalitaPagamentoList());
			return SUCCESS; 
			
		} else {
			
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Inserire Codice Fiscale Quietanzante o Soggetto Quietanzante"));
			addErrori(listaErrori);
			handleAggiornaVisualization(false, true, false, false, false, false);
			return INPUT;
		}

	}
	
	
	
	
	
	
	
	
	
	
	public String aggiornaGenerico(){
		setMethodName("aggiornaContante");
		
		
		setAzioneAggiorna(true);
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		
			handleModelBean();
			
			ModalitaPagamentoSoggetto modPagToUpdate = modelWeb.getModalitaPagamentoSoggettoToUpdate();
			
			//gestisco lo stato utente
			boolean gestSogg = AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSogg, sessionHandler.getAzioniConsentite());
			boolean gestSoggDec = AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite());
			
			if(soggettoDaAggiornare.getNote() != null){
				modPagToUpdate.setNote(soggettoDaAggiornare.getNote());
			}
			
			modPagToUpdate.setPerStipendi(soggettoDaAggiornare.getPerStipendi());

			if(getDataScadenzaStringa() != null){
				if(!getDataScadenzaStringa().isEmpty()){
				// controllo la data di nascita per le persone
				if(getDataScadenzaStringa().length()!=10){
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
					addErrori(listaErrori);
					handleAggiornaVisualization(false, false, false, false, false, false, true);
					return INPUT;
				}else {
					
					if (!DateUtility.isDate(getDataScadenzaStringa(), "dd/MM/yyyy")) {
						listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
						addErrori(listaErrori);
						handleAggiornaVisualization(false, false, false, false, false, false, true);
						return INPUT;
					} else {
						DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						
						try {
							Date dataScadenza = df.parse(getDataScadenzaStringa());
							
							Calendar cFrom = Calendar.getInstance();							
							cFrom.set(Calendar.HOUR_OF_DAY, 0);
							cFrom.set(Calendar.MINUTE, 0);
							cFrom.set(Calendar.SECOND, 0);
							cFrom.set(Calendar.MILLISECOND, 0);
							Timestamp now = new Timestamp(cFrom.getTime().getTime());
							
							if(dataScadenza.compareTo(now)==-1){
								listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Data Scadenza", "(Non deve essere minore della data odierna)"));
								addErrori(listaErrori);
								handleAggiornaVisualization(false, false, false, false, false, false, true);
								return INPUT;
							}
							modPagToUpdate.setDataFineValidita(dataScadenza);		
						} catch (ParseException e) {
							
						}
					}

				}
				} else {
					modPagToUpdate.setDataFineValidita(null);
				}
			} else {
				modPagToUpdate.setDataFineValidita(null);
			}
			
			
		
			
				
			if(gestSogg){
				modPagToUpdate.setDescrizioneStatoModalitaPagamento("valido");
			}
			if(gestSoggDec){
				if(!modPagToUpdate.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(CostantiFin.STATO_IN_MODIFICA_no_underscore)){
					if(modPagToUpdate.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("provvisorio")){
						if(decentratoSuPropriaOccorrenza(modPagToUpdate.getLoginCreazione()) || decentratoSuPropriaOccorrenza(modPagToUpdate.getLoginModifica())){
							modPagToUpdate.setModificaPropriaOccorrenza(true);
						}
					}
				}
			}
					
			
			//CORRETTIVA
			List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoListToUpdateApp = model.getDettaglioSoggetto().getModalitaPagamentoList();
			List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoListToUpdate = new ArrayList<ModalitaPagamentoSoggetto>();
			modalitaPagamentoSoggettoListToUpdate.addAll(model.getDettaglioSoggetto().getModalitaPagamentoList());
			
			ModalitaPagamentoSoggetto mdpPerStipendiIsSet = null;
			
			for(ModalitaPagamentoSoggetto soggApp : modalitaPagamentoSoggettoListToUpdateApp){
				if(soggApp.getUid() == modPagToUpdate.getUid()){	
					
					modalitaPagamentoSoggettoListToUpdate.remove(soggApp);
					modalitaPagamentoSoggettoListToUpdate.add(modPagToUpdate);
				} else {
					mdpPerStipendiIsSet = getMdpPerStipendi(soggApp, modPagToUpdate);
				}
			}
			
			if (mdpPerStipendiIsSet != null){
				addPersistentActionWarning(ErroreFin.WARNING_GENERICO.getErrore(String.format(
						"Attenzione: modalit&agrave; di pagamento impostata 'Per gli stipendi'. In precedenza era impostata la modalit&agrave; di pagamento [ %s ].",
						mdpPerStipendiIsSet.getCodiceModalitaPagamento())).getTesto());
			}
			
			//AGGIUNGO LE MODALITA DI PAGAMENTO VECCHIE E NUOVE AL SOGGETTO IN SESSIONE!!!!
			model.getDettaglioSoggetto().setModalitaPagamentoList(modalitaPagamentoSoggettoListToUpdate);	
			model.getDettaglioSoggetto().setSediSecondarie(modelWeb.getSediSecondarie());
			
			resetVariable();
			handleAggiornaVisualization(false, false, false, false, false, false, false);
			setAzioneAggiorna(false);
			// inserisco il codice tipo persona
			model.getDettaglioSoggetto().getTipoSoggetto().setCodice(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode());
			model.getDettaglioSoggetto().setSediSecondarie(model.getListaSecondariaSoggetto());
			if (model.getDettaglioSoggetto().getSessoStringa() != null && !"".equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa())) {
				if (CostantiFin.MASCHIO.equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa().toUpperCase())) {
					model.getDettaglioSoggetto().setSesso(Sesso.MASCHIO);
				} else {
					model.getDettaglioSoggetto().setSesso(Sesso.FEMMINA);
				}
			}
			AggiornaSoggettoResponse response = null;
			
			AggiornaSoggetto aggiornaSoggetto = convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(), false);
			
			aggiornaSoggetto.setCodificaAmbito(getcodiceAmbito());
			
			if (AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite())) {
			
				AggiornaSoggettoProvvisorio asp = new AggiornaSoggettoProvvisorio(aggiornaSoggetto);
				//richiamo il servizio di aggiornamento provvisorio:
				response = soggettoService.aggiornaSoggettoProvvisorio(asp);
		
			}else{
				//richiamo il servizio di aggiornamento:
				response = soggettoService.aggiornaSoggetto(aggiornaSoggetto);
			}
			
			
			if(isFallimento(response)) {
				//ci sono errori
				debug(methodName, "Errore nella risposta del servizio");
				
				clearErrorsAndMessages();
				
				addErrori(methodName, response);
				
				debug(methodName, "Model: " + model);
				
				//reimposto le modalita' originarie (quelle prime dell'errore)
				this.setModel(aggiornaSoggettoModelCachePerAggiorna);
				
				return INPUT;
			}
			
			clearErrorsAndMessages();
			
			// messaggio di ok nella pagina di elenco soggetti
			addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore()); 
					                   
			
			model.getDettaglioSoggetto().setModalitaPagamentoList(response.getSoggettoAggiornato().getModalitaPagamentoList());
			return SUCCESS; 
			
		

	}

	
	private ModalitaPagamentoSoggetto getMdpPerStipendi(ModalitaPagamentoSoggetto otherMdp, ModalitaPagamentoSoggetto mdp){
		if (Boolean.TRUE.equals(mdp.getPerStipendi()) && Boolean.TRUE.equals(otherMdp.getPerStipendi()))
		{
			otherMdp.setPerStipendi(Boolean.FALSE);
			
			return otherMdp;
		}
		
		return null;
	}
	
	
	
	
	
	
	
	public String annullaModalitaDiPagamento(){

		setMethodName("annullaModalitaDiPagamento");
		
		String codiceSoggettoDaAggiornare = getAggiornaCodiceSoggetto();
		
		AggiornaSoggettoModel aggiornaSoggettoModelCache = (clone(model));
		
		List<ModalitaPagamentoSoggetto> modListApp = model.getDettaglioSoggetto().getModalitaPagamentoList();
		List<ModalitaPagamentoSoggetto> modListDef = new ArrayList<ModalitaPagamentoSoggetto>();
		modListDef.addAll(model.getDettaglioSoggetto().getModalitaPagamentoList());
		for(ModalitaPagamentoSoggetto mdpApp : modListApp){
			if(mdpApp.getUid() == Integer.valueOf(codiceSoggettoDaAggiornare).intValue()){
				ModalitaPagamentoSoggetto mdpDef = mdpApp;
				modListDef.remove(mdpDef);				
				mdpDef.setDescrizioneStatoModalitaPagamento("annullato");
				mdpDef.setTipoAccredito(mdpDef.getTipoAccredito());
				modListDef.add(mdpDef);
			}
		}
		
		model.getDettaglioSoggetto().setModalitaPagamentoList(modListDef);
		
		// inserisco il codice tipo persona
		model.getDettaglioSoggetto().getTipoSoggetto().setCodice(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode());
		model.getDettaglioSoggetto().setSediSecondarie(model.getListaSecondariaSoggetto());
		if (model.getDettaglioSoggetto().getSessoStringa() != null && !"".equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa())) {
			if (CostantiFin.MASCHIO.equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa().toUpperCase())) {
				model.getDettaglioSoggetto().setSesso(Sesso.MASCHIO);
			} else {
				model.getDettaglioSoggetto().setSesso(Sesso.FEMMINA);
			}
		}
		AggiornaSoggettoResponse response = null;
		
		AggiornaSoggetto aggiornaSoggetto = convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(), false);
		
		aggiornaSoggetto.setCodificaAmbito(getcodiceAmbito());
		
		if (AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite())) {
		
			AggiornaSoggettoProvvisorio asp = new AggiornaSoggettoProvvisorio(aggiornaSoggetto);
			//richiamo il servizio di aggiornamento provvisorio:
			response = soggettoService.aggiornaSoggettoProvvisorio(asp);
	
		}else{
			//richiamo il servizio di aggiornamento:
			response = soggettoService.aggiornaSoggetto(aggiornaSoggetto);
		}
		
		
		if(isFallimento(response)) {
			//ci sono errori
			debug(methodName, "Errore nella risposta del servizio");
						
			//reimposto le modalita' originarie (quelle prime dell'errore)
			this.setModel(aggiornaSoggettoModelCache);
			
			clearErrorsAndMessages();
			
			addErrori(methodName, response);
			
			return INPUT;
		}
		
		clearErrorsAndMessages();
		
		// messaggio di ok nella pagina di elenco soggetti
		addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore()); 
				                   
		
		model.getDettaglioSoggetto().setModalitaPagamentoList(response.getSoggettoAggiornato().getModalitaPagamentoList());
		
		return SUCCESS;
	}
	
	public String bloccaModalitaDiPagamento(){
		setMethodName("bloccaModalitaDiPagamento");
		
		String codiceSoggettoDaAggiornare = getAggiornaCodiceSoggetto();
		List<ModalitaPagamentoSoggetto> modListApp = model.getDettaglioSoggetto().getModalitaPagamentoList();
		List<ModalitaPagamentoSoggetto> modListDef = new ArrayList<ModalitaPagamentoSoggetto>();
		modListDef.addAll(model.getDettaglioSoggetto().getModalitaPagamentoList());
		for(ModalitaPagamentoSoggetto mdpApp : modListApp){
			if(mdpApp.getUid() == Integer.valueOf(codiceSoggettoDaAggiornare).intValue()){
				ModalitaPagamentoSoggetto mdpDef = mdpApp;
				modListDef.remove(mdpDef);
				mdpDef.setDescrizioneStatoModalitaPagamento("bloccato");
				mdpDef.setTipoAccredito(mdpDef.getTipoAccredito());
				modListDef.add(mdpDef);
			}
		}
		
		model.getDettaglioSoggetto().setModalitaPagamentoList(modListDef);
		
		
		// inserisco il codice tipo persona
		model.getDettaglioSoggetto().getTipoSoggetto().setCodice(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode());
		model.getDettaglioSoggetto().setSediSecondarie(model.getListaSecondariaSoggetto());
		if (model.getDettaglioSoggetto().getSessoStringa() != null && !"".equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa())) {
			if (CostantiFin.MASCHIO.equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa().toUpperCase())) {
				model.getDettaglioSoggetto().setSesso(Sesso.MASCHIO);
			} else {
				model.getDettaglioSoggetto().setSesso(Sesso.FEMMINA);
			}
		}
		AggiornaSoggettoResponse response = null;
		
		AggiornaSoggetto aggiornaSoggetto = convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(), false);
		
		aggiornaSoggetto.setCodificaAmbito(getcodiceAmbito());
		
		if (AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite())) {
		
			AggiornaSoggettoProvvisorio asp = new AggiornaSoggettoProvvisorio(aggiornaSoggetto);
			//richiamo il servizio di aggiornamento provvisorio:
			response = soggettoService.aggiornaSoggettoProvvisorio(asp);
	
		}else{
			//richiamo il servizio di aggiornamento:
			response = soggettoService.aggiornaSoggetto(aggiornaSoggetto);
		}
		
		
		if(isFallimento(response)) {
			//ci sono errori
			debug(methodName, "Errore nella risposta del servizio");
			
			clearErrorsAndMessages();
			addErrori(methodName, response);
			
			debug(methodName, "Model: " + model);
			
			return INPUT;
		}
		
		clearErrorsAndMessages();
		
		// messaggio di ok nella pagina di elenco soggetti
		addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore()); 
				                   
		
		model.getDettaglioSoggetto().setModalitaPagamentoList(response.getSoggettoAggiornato().getModalitaPagamentoList());
		
		return SUCCESS;
		
	}
	
	public String ricercaSoggettoModPagAggiorna(){
		setMethodName("ricercaSoggettoModPagAggiorna");
		
		setTipoAccreditoPage(getTipoAccreditoPage());
		
		List<Errore> listaErroriPerValidazione = new ArrayList<Errore>();
		
		Integer codiceApp = modelWeb.getCodiceAggiorna();
		String codiceFiscaleApp = modelWeb.getCodiceFiscaleAggiorna();
		String denominazioneApp = modelWeb.getDenominazioneAggiorna();
		String partitaIvaApp = modelWeb.getPartitaIvaAggiorna();
		
		RicercaSoggettoModel ricercaSoggettoModel = new RicercaSoggettoModel();
		ricercaSoggettoModel.setCodice(codiceApp);
		ricercaSoggettoModel.setCodiceFiscale(codiceFiscaleApp);
		ricercaSoggettoModel.setDenominazione(denominazioneApp);
		ricercaSoggettoModel.setPartitaIva(partitaIvaApp);
		
		handleModelBean();
		modelWeb.setCodiceAggiorna(codiceApp);
		modelWeb.setCodiceFiscaleAggiorna(codiceFiscaleApp);
		modelWeb.setDenominazioneAggiorna(denominazioneApp);
		modelWeb.setPartitaIva(partitaIvaApp);
		
		ValidationUtils.validaRicercaSoggetto(listaErroriPerValidazione, ricercaSoggettoModel);
		if(!listaErroriPerValidazione.isEmpty()) {
			addErrori(listaErroriPerValidazione);
			handleAggiornaVisualization(false, false, true, false, false, false);
			return INPUT;
		} else {
			RicercaSoggettiResponse response = soggettoService.ricercaSoggetti(convertiAggiornaModelPerChiamataServizioRicerca());
			//Controllo che il servizio non restituisca errori
			if(response.isFallimento()) {
				//ci sono errori
				addErrori(methodName, response);
				return INPUT;
			}
			
			List<Errore> listaErrori = new ArrayList<Errore>();

			if(response.getSoggetti() == null || response.getSoggetti().isEmpty())
			{
				listaErrori.add(ErroreCore.NESSUN_DATO_REPERITO.getErrore("Soggetto"));
			}
			else
			{
				List<Soggetto> soggettiValidati = new ArrayList<Soggetto>();
				
				validaSoggettiPerRicerca(response.getSoggetti(), soggettiValidati, listaErrori);

				modelWeb.setSoggettiRicercatiAggiorna(soggettiValidati);
				
				if(!isEmpty(modelWeb.getSoggettiRicercatiAggiorna())){
					handleAggiornaVisualization(false, false, true, true, false, false);
					sessionHandler.setParametro(FinSessionParameter.MODALITA_PAGAMENTO_MODEL, modelWeb);
				}
			} 

			if (listaErrori.isEmpty())
				return SUCCESS;
			else
			{
				addErrori(listaErrori);
				handleAggiornaVisualization(false, false, true, false, false, false);
	
				return INPUT;
			}
		}
	}
	
	public String ricercaSoggettoModPag_step2Aggiorna(){
		setMethodName("ricercaSoggettoModPag_step2Aggiorna");
		
		// setto l'ancora della pagina
		setAncoraMdpVisualizza(true);
		
		setTipoAccreditoPage(getTipoAccreditoPage());
		
		handleModelBean();
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		if(StringUtils.isEmpty(getCodiceSoggettoRicercatoCessioneAggiorna())){
			//Se non viene selezionato alcun soggetto, mostro l'errore
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Selezionare un soggetto per la cessione"));
			addErrori(listaErrori);
			
			handleAggiornaVisualization(false, false, false, true, true, false);
			
			return INPUT;
		} else {
			//richiamo il servizio di ricerca:
			RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(convertiModelPerChiamataServizioRicercaPerChiave(getCodiceSoggettoRicercatoCessioneAggiorna()));
			//Salvo il dettaglio del soggetto selezionato e le relative sedi e modalita di pagamento associate
			modelWeb.setDettaglioSoggettoCessioneAggiorna(response.getSoggetto());
			modelWeb.setModalitaPagamentoCessioneAggiorna(valutaListaMdp(response.getListaModalitaPagamentoSoggetto()));
			modelWeb.setSedisecondariaSoggettoCessioneAggiorna(response.getListaSecondariaSoggetto());
			
			handleAggiornaVisualization(false, false, true, true, true, false);
			sessionHandler.setParametro(FinSessionParameter.MODALITA_PAGAMENTO_MODEL, modelWeb);
			
			return SUCCESS;
		}
	}
	
	public String aggiornaModPagCessione(){
		setMethodName("aggiornaModPagCessione");
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		handleModelBean();
		
		ModalitaPagamentoSoggetto modPagToUpdateDef = modelWeb.getModalitaPagamentoSoggettoToUpdate();
		
		ModalitaPagamentoSoggetto modPagToProv = getSoggettoDaAggiornare();
		
		//gestisco lo stato utente
		boolean gestSogg = AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSogg, sessionHandler.getAzioniConsentite());
		boolean gestSoggDec = AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite());
		
		if(modPagToProv.getNote() != null){
			modPagToUpdateDef.setNote(modPagToProv.getNote());
		}
		if(getDataScadenzaStringa() != null){
			if(!getDataScadenzaStringa().isEmpty()){
			// controllo la data di nascita per le persone
			if(getDataScadenzaStringa().length()!=10){
				listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
				addErrori(listaErrori);
				handleAggiornaVisualization(false, false, false, false, true, false);
				return INPUT;
			}else {
				
				if (!DateUtility.isDate(getDataScadenzaStringa(), "dd/MM/yyyy")) {
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
					addErrori(listaErrori);
					handleAggiornaVisualization(false, false, false, false, true, false);
					return INPUT;
				} else {
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					
					try {
						Date dataScadenza = df.parse(getDataScadenzaStringa());
						
						Calendar cFrom = Calendar.getInstance();							
						cFrom.set(Calendar.HOUR_OF_DAY, 0);
						cFrom.set(Calendar.MINUTE, 0);
						cFrom.set(Calendar.SECOND, 0);
						cFrom.set(Calendar.MILLISECOND, 0);
						Timestamp now = new Timestamp(cFrom.getTime().getTime());
						
						if(dataScadenza.compareTo(now)==-1){
							listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Data Scadenza", "(Non deve essere minore della data odierna)"));
							addErrori(listaErrori);
							handleAggiornaVisualization(false, false, false, false, true, false);
							return INPUT;
						}
						modPagToUpdateDef.setDataFineValidita(dataScadenza);								
					} catch (ParseException e) {
						//  Auto-generated catch block
					}
				}

			}
			} else {
				modPagToUpdateDef.setDataFineValidita(null);
			}
		} else {
			modPagToUpdateDef.setDataFineValidita(null);
		}
		
		Integer idModPagSelected = modPagToProv.getUid();
		ModalitaPagamentoSoggetto modPagCesDef = new ModalitaPagamentoSoggetto();
		
		for(ModalitaPagamentoSoggetto modApp : modelWeb.getModalitaPagamentoCessioneAggiorna()){
			if(modApp.getUid() == idModPagSelected.intValue()){
				modPagCesDef = modApp;
			}
		}
		
		if(modelWeb.getSedisecondariaSoggettoCessioneAggiorna() != null){
			boolean isSedeSec = false;
			for(SedeSecondariaSoggetto sedeApp : modelWeb.getSedisecondariaSoggettoCessioneAggiorna()){
				if(sedeApp.getDenominazione().equals(modPagCesDef.getAssociatoA())){
					isSedeSec = true;
					modPagToUpdateDef.setCessioneCodSoggetto(sedeApp.getCodiceSedeSecondaria());
				}
			}
			if(!isSedeSec){
				modPagToUpdateDef.setCessioneCodSoggetto(String.valueOf(modelWeb.getDettaglioSoggettoCessioneAggiorna().getCodiceSoggetto()));
			}
		} else {
			modPagToUpdateDef.setCessioneCodSoggetto(String.valueOf(modelWeb.getDettaglioSoggettoCessioneAggiorna().getCodiceSoggetto()));
		}
		
		if(gestSogg){
			modPagToUpdateDef.setDescrizioneStatoModalitaPagamento("valido");
		}
		if(gestSoggDec){
			if(!modPagToUpdateDef.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(CostantiFin.STATO_IN_MODIFICA_no_underscore)){
				if(modPagToUpdateDef.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("provvisorio")){
					if(decentratoSuPropriaOccorrenza(modPagToUpdateDef.getLoginCreazione()) || decentratoSuPropriaOccorrenza(modPagToUpdateDef.getLoginModifica())){
						modPagToUpdateDef.setModificaPropriaOccorrenza(true);
					}
				}
			}
		}
		
		modPagToUpdateDef.setCessioneCodModPag(String.valueOf(soggettoDaAggiornare.getUid()));
		
		//Gestisco il tipo accredito
		modPagToUpdateDef.setTipoAccredito(TipoAccredito.valueOf(modPagToUpdateDef.getModalitaAccreditoSoggetto().getCodice()));
		
		List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoListToUpdateApp = model.getDettaglioSoggetto().getModalitaPagamentoList();
		List<ModalitaPagamentoSoggetto> modalitaPagamentoSoggettoListToUpdate = new ArrayList<ModalitaPagamentoSoggetto>();
		modalitaPagamentoSoggettoListToUpdate.addAll(model.getDettaglioSoggetto().getModalitaPagamentoList());
		for(ModalitaPagamentoSoggetto soggApp : modalitaPagamentoSoggettoListToUpdateApp){
			if(soggApp.getUid() == modPagToUpdateDef.getUid()){
				modalitaPagamentoSoggettoListToUpdate.remove(soggApp);
				modalitaPagamentoSoggettoListToUpdate.add(modPagToUpdateDef);
			}
		}

		//AGGIUNGO LE MODALITA DI PAGAMENTO VECCHIE E aggiornate AL SOGGETTO IN SESSIONE!!!!
		model.getDettaglioSoggetto().setModalitaPagamentoList(modalitaPagamentoSoggettoListToUpdate);		
		model.getDettaglioSoggetto().setSediSecondarie(modelWeb.getSediSecondarie());
		
		resetVariable();
		handleAggiornaVisualization(false, false, false, false, false, false);
		setAzioneAggiorna(false);
		// inserisco il codice tipo persona
		model.getDettaglioSoggetto().getTipoSoggetto().setCodice(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode());
		model.getDettaglioSoggetto().setSediSecondarie(model.getListaSecondariaSoggetto());
		if (model.getDettaglioSoggetto().getSessoStringa() != null && !"".equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa())) {
			if (CostantiFin.MASCHIO.equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa().toUpperCase())) {
				model.getDettaglioSoggetto().setSesso(Sesso.MASCHIO);
			} else {
				model.getDettaglioSoggetto().setSesso(Sesso.FEMMINA);
			}
		}
		AggiornaSoggettoResponse response = null;
		
		//Compongo la request per il servizio:
		AggiornaSoggetto aggiornaSoggetto = convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(), false);
		
		aggiornaSoggetto.setCodificaAmbito(getcodiceAmbito());
		
		if (AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite())) {
		
			AggiornaSoggettoProvvisorio asp = new AggiornaSoggettoProvvisorio(aggiornaSoggetto);
			//richiamo il servizio di aggiornamento provvisorio:
			response = soggettoService.aggiornaSoggettoProvvisorio(asp);
	
		}else{
			//richiamo il servizio di aggiornamento:
			response = soggettoService.aggiornaSoggetto(aggiornaSoggetto);
		}
		
		if(isFallimento(response)) {
			//ci sono errori
			debug(methodName, "Errore nella risposta del servizio");
			
			clearErrorsAndMessages();
			addErrori(methodName, response);
			
			debug(methodName, "Model: " + model);
			
			//reimposto le modalita' originarie (quelle prime dell'errore)
			this.setModel(aggiornaSoggettoModelCachePerAggiorna);
			
			return INPUT;
		}
		
		clearErrorsAndMessages();
		
		// messaggio di ok nella pagina di elenco soggetti
		addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore()); 
				                   
		
		model.getDettaglioSoggetto().setModalitaPagamentoList(response.getSoggettoAggiornato().getModalitaPagamentoList());
		
		return SUCCESS;
		
	}
	
	/**
	 * Elimina modalita di pagamento
	 * @return
	 */
	public String eliminaMdp(){
		String codMdp = getAggiornaCodiceSoggetto();
		
		AggiornaSoggettoModel aggiornaSoggettoModelCache = (clone(model));
		
		List<ModalitaPagamentoSoggetto> modListApp = model.getDettaglioSoggetto().getModalitaPagamentoList();
		List<ModalitaPagamentoSoggetto> modListDef = new ArrayList<ModalitaPagamentoSoggetto>();
		modListDef.addAll(model.getDettaglioSoggetto().getModalitaPagamentoList());
		for(ModalitaPagamentoSoggetto mdpApp : modListApp){
			if(mdpApp.getUid() == Integer.valueOf(codMdp).intValue()){
				ModalitaPagamentoSoggetto mdpDef = mdpApp;
				modListDef.remove(mdpDef);
			}
		}

		model.getDettaglioSoggetto().setModalitaPagamentoList(modListDef);
		
		// inserisco il codice tipo persona
		model.getDettaglioSoggetto().getTipoSoggetto().setCodice(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode());
		model.getDettaglioSoggetto().setSediSecondarie(model.getListaSecondariaSoggetto());
		if (model.getDettaglioSoggetto().getSessoStringa() != null && !"".equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa())) {
			if (CostantiFin.MASCHIO.equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa().toUpperCase())) {
				model.getDettaglioSoggetto().setSesso(Sesso.MASCHIO);
			} else {
				model.getDettaglioSoggetto().setSesso(Sesso.FEMMINA);
			}
		}
		
		
		AggiornaSoggettoResponse response = null;
		
		//Compongo la request per il servizio:
		AggiornaSoggetto aggiornaSoggetto = convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(), false);
		
		aggiornaSoggetto.setCodificaAmbito(getcodiceAmbito());
		
		if (AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite())) {
		
			AggiornaSoggettoProvvisorio asp = new AggiornaSoggettoProvvisorio(aggiornaSoggetto);
			//richiamo il servizio di aggiornamento provvisorio:
			response = soggettoService.aggiornaSoggettoProvvisorio(asp);
	
		}else{
			//richiamo il servizio di aggiornamento:
			response = soggettoService.aggiornaSoggetto(aggiornaSoggetto);
		}
		
		if(isFallimento(response)) {
			//ci sono errori
			debug(methodName, "Errore nella risposta del servizio");
		
			clearErrorsAndMessages();
			
			addErrori(methodName, response);									
			
			this.setModel(aggiornaSoggettoModelCache);
			
			return INPUT;
		}
		
		model.resetErrori();
		model.resetMessaggi();
		
		// messaggio di ok nella pagina di elenco soggetti
		addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore()); 
				                   		
		return SUCCESS;
	}
	
	/**
	 * Verifica Formale Codice Fiscale
	 * @param cf
	 * @return
	 */
	private boolean verificaFormaleCodiceFiscale(String cf){
		  if(cf!=null) {
			  //ok diverso da null
			  cf = cf.toUpperCase();
			  //applico l'espressione regolare:
			  return cf.matches("^[A-Z]{6}[0-9LMNPQRSTUV]{2}[A-Z][0-9LMNPQRSTUV]{2}[A-Z][0-9LMNPQRSTUV]{3}[A-Z]$");
		  }
		  return false;
	 }
	
	/**
	 * valida i soggetti
	 * @param soggettiTrovati
	 * @param soggettiValidati
	 * @param errori
	 */
	private void validaSoggettiPerRicerca(List<Soggetto> soggettiTrovati, List<Soggetto> soggettiValidati, List<Errore> errori){
		
		//Controllo che il Soggetto Contenga Modalita di Pagamento
		for (Soggetto soggettoApp : soggettiTrovati){
			if (soggettoApp.getCodiceSoggettoNumber().equals(model.getDettaglioSoggetto().getCodiceSoggettoNumber())){
				if (soggettiTrovati.size() == 1){
					errori.add(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("associare un soggetto diverso da quello corrente"));
				}
			} else {
				List<ModalitaPagamentoSoggetto> listaModalitaValide = new ArrayList<ModalitaPagamentoSoggetto>();
				soggettoApp.setModalitaPagamentoList(listaModalitaValide);
				int index = 0;
				// controllo che lo stato operativo del soggetto sia diverso
				// da
				// annullato e da sospeso
				if (soggettoApp.getStatoOperativo() != Soggetto.StatoOperativoAnagrafica.ANNULLATO
						&& soggettoApp.getStatoOperativo() != Soggetto.StatoOperativoAnagrafica.SOSPESO){
					//Compongo la request per il servizio:
					RicercaSoggettoPerChiave requestRicerca = convertiModelPerChiamataServizioRicercaPerChiave(soggettoApp.getCodiceSoggetto());
					//richiamo il servizio di ricerca:
					RicercaSoggettoPerChiaveResponse responseApp = soggettoService.ricercaSoggettoPerChiave(requestRicerca);
					if (responseApp.getListaModalitaPagamentoSoggetto() != null && !responseApp.getListaModalitaPagamentoSoggetto().isEmpty()){
						// controllo che ci siano modalita di pagamento nel
						// soggetto e che qualora ci siano , siano diverse
						// da
						// annullate o bloccate

						boolean foundModPag = false;

						for (ModalitaPagamentoSoggetto modPagApp : responseApp.getListaModalitaPagamentoSoggetto()){

							if (!modPagApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(CostantiFin.STATO_ANNULLATO)
									&& !modPagApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(CostantiFin.STATO_BLOCCATO)){
								// controllo che non sia di tipo cessione
								if (modPagApp.getModalitaAccreditoSoggetto().getCodice() != null){
									if (!modPagApp.isTipoCessione()){

										if (modPagApp.getDataFineValidita() != null){
											// codice per non considerare
											// scadute anche le modalita con
											// scadenza odierna
											Calendar cFrom = Calendar.getInstance();
											cFrom.set(Calendar.HOUR_OF_DAY, 0);
											cFrom.set(Calendar.MINUTE, 0);
											cFrom.set(Calendar.SECOND, 0);
											cFrom.set(Calendar.MILLISECOND, 0);
											Timestamp now = new Timestamp(cFrom.getTime().getTime());

											if (modPagApp.getDataFineValidita().compareTo(now) != -1){
												boolean alreadyAssociato = false;
												if (model.getDettaglioSoggetto().getModalitaPagamentoList() != null){
													for (ModalitaPagamentoSoggetto mdpApp : model.getDettaglioSoggetto().getModalitaPagamentoList()){
														if (mdpApp.getModalitaPagamentoSoggettoCessione2() != null){
															if (mdpApp.getModalitaPagamentoSoggettoCessione2().getUid() == modPagApp.getUid()){
																alreadyAssociato = true;
															}
														}
													}
												}

												if (!alreadyAssociato){
													soggettoApp.getModalitaPagamentoList().add(modPagApp);
													foundModPag = true;
													index++;
												}

											}
										} else {
											boolean alreadyAssociato = false;
											if (model.getDettaglioSoggetto().getModalitaPagamentoList() != null){
												for (ModalitaPagamentoSoggetto mdpApp : model.getDettaglioSoggetto().getModalitaPagamentoList()){
													if (mdpApp.getModalitaPagamentoSoggettoCessione2() != null
															&& mdpApp.getModalitaPagamentoSoggettoCessione2().getUid() == modPagApp.getUid()
															&& !mdpApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(CostantiFin.STATO_ANNULLATO)
															&& !mdpApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(CostantiFin.STATO_BLOCCATO)) {
														alreadyAssociato = true;

													}
												}
											}

											if (!alreadyAssociato){
												soggettoApp.getModalitaPagamentoList().add(modPagApp);
												foundModPag = true;
												index++;
											}
										}

									}
								}
							}
						}

						if (!foundModPag){
							errori.add(ErroreFin.MOD_PAGAMENTO_NON_PRESENTE.getErrore(soggettoApp.getDenominazione()));
						}
							
					} else {
						errori.add(ErroreFin.MOD_PAGAMENTO_NON_PRESENTE.getErrore(soggettoApp.getDenominazione()));
					}
						
				} else {
					errori.add(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore(soggettoApp.getDenominazione(), soggettoApp.getStatoOperativo()));
				}
					
				if (index > 0) {
					soggettiValidati.add(soggettoApp);
				}
			}
		}
		return;

	}
	
	/**
	 * valuta la lista di modalita pagamento
	 * @param listApp
	 * @return
	 */
	private List<ModalitaPagamentoSoggetto> valutaListaMdp(List<ModalitaPagamentoSoggetto> listApp){
		List<ModalitaPagamentoSoggetto> listaDef = new ArrayList<ModalitaPagamentoSoggetto>();
		
		//controllo che ci siano modalita di pagamento nel soggetto e che qualora ci siano , siano diverse da annullate o bloccate
		for(ModalitaPagamentoSoggetto modPagApp : listApp){
			int index = 0;
			ModalitaPagamentoSoggetto mdpDef = modPagApp;
			if (!modPagApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(CostantiFin.STATO_ANNULLATO)
					&& !modPagApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(CostantiFin.STATO_BLOCCATO)){
				// controllo che non sia di tipo cessione
				if (! modPagApp.isTipoCessione()){
					if (modPagApp.getDataFineValidita() != null){

						// codice per non considerare scadute anche le modalita
						// con scadenza odierna
						Calendar cFrom = Calendar.getInstance();
						cFrom.set(Calendar.HOUR_OF_DAY, 0);
						cFrom.set(Calendar.MINUTE, 0);
						cFrom.set(Calendar.SECOND, 0);
						cFrom.set(Calendar.MILLISECOND, 0);
						Timestamp now = new Timestamp(cFrom.getTime().getTime());

						if (modPagApp.getDataFineValidita().compareTo(now) != -1) {
							index++;
						}
					} else {
						index++;
					}
				}
			}
			
			if (index != 0) {
				boolean alreadyAssociato = false;
				
				if (model.getDettaglioSoggetto().getModalitaPagamentoList() != null){
					for (ModalitaPagamentoSoggetto mdpApp : model.getDettaglioSoggetto().getModalitaPagamentoList()){
						if (mdpApp.getCessioneCodSoggetto() != null && 
								mdpApp.getModalitaPagamentoSoggettoCessione2().getUid() == mdpDef.getUid() &&
								!mdpApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(CostantiFin.STATO_ANNULLATO)
								&& !mdpApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(CostantiFin.STATO_BLOCCATO)) {
							alreadyAssociato = true;
						}
					}
				}
				
				if (!alreadyAssociato){
					listaDef.add(mdpDef);
				}
					
			}
		}
		
		return listaDef;
	}
	
	/**
	 * Handle Visualization Variable
	 * @param booleanParams
	 */
	private void handleVisualizationVariable(boolean... booleanParams){
		this.inserisciModPag = booleanParams[0];
		this.contoCorrenteVisible = booleanParams[1];
		this.contanteVisible = booleanParams[2];
		this.cessioneVisible = booleanParams[3];
		this.cessioneSearchVisible = booleanParams[4];
		this.cessioneSearchStep2 = booleanParams[5];
		this.successInserimento = booleanParams[6];
		this.genericoVisibile = getBooleanIndex(booleanParams, 7); //FIX FIX FIX
	}
	
	/**
	 * Ritorna il valore del boolean alla posizione indicata
	 * @param array
	 * @param i
	 * @return
	 */
	private boolean getBooleanIndex(boolean[] array, int i){
		if (i < array.length){
			//elemento di posizione i
			return array[i];
		}
		return false;
	}

	/**
	 * Handle Aggiorna Visualization
	 * @param booleanParams
	 */
	private void handleAggiornaVisualization(boolean... booleanParams){
		this.aggiornaContoCorrenteVisible = getBooleanIndex(booleanParams, 0);
		this.aggiornaContantiVisible = getBooleanIndex(booleanParams, 1);
		this.aggiornaCessioneSearch = getBooleanIndex(booleanParams, 2);
		this.aggiornaCessioneStep2 = getBooleanIndex(booleanParams, 3);
		this.aggiornaCessioneStep3 = getBooleanIndex(booleanParams, 4);
		this.aggiornaSuccessVisible = getBooleanIndex(booleanParams, 5);
		this.aggiornaGenericoVisible = getBooleanIndex(booleanParams, 6);
	}
	
	/**
	 * Handle Tipo Accredito Soggetto Update
	 * @param idTipoAccredito
	 */
	private void handleTipoAccreditoSoggettoUpdate(String idTipoAccredito){
		
		handleModelBean();
		
		if(modelWeb.getModalitaPagamentoSoggettoToUpdate() == null){
			modelWeb.setModalitaPagamentoSoggettoToUpdate(new ModalitaPagamentoSoggetto());
		} 
		
		TipoAccredito ta = CostantiFin.codeToTipoAccredito(idTipoAccredito);
		modelWeb.getModalitaPagamentoSoggettoToUpdate().setTipoAccredito(ta);
		
		sessionHandler.setParametro(FinSessionParameter.MODALITA_PAGAMENTO_MODEL, modelWeb);
	}
	
	/**
	 * Handle Tipo Accredito
	 * @param idTipoAccredito
	 */
	private void handleTipoAccredito(String idTipoAccredito){
		
		//handle del model bean:
		handleModelBean();
		
		modelWeb.setModalitaPagamentoSoggettoToInsert(new ModalitaPagamentoSoggetto());
		
		TipoAccredito ta = CostantiFin.codeToTipoAccredito(idTipoAccredito);
		modelWeb.getModalitaPagamentoSoggettoToInsert().setTipoAccredito(ta);
		
		sessionHandler.setParametro(FinSessionParameter.MODALITA_PAGAMENTO_MODEL, modelWeb);
	}
	
	

	/**
	 * Metodo di utily per la pulizia delle variabili in sessione
	 */
	public void resetVariable(){

		//setto a fale le variabili boolean:
		setInserisciModPag(false);
		setContanteVisible(false);
		setContanteVisible(false);
		setCessioneSearchVisible(false);
		setCessioneVisible(false);
		setCessioneSearchStep2(false);
		setSuccessInserimento(false);
		
		//svuoto codice soggetto:
		setCodiceSoggettoRicercatoCessione("");
		
		//salvo il model in sessione:
		sessionHandler.setParametro(FinSessionParameter.MODALITA_PAGAMENTO_MODEL, modelWeb);
		
		//setto le sedi secondarie nel model web:
		modelWeb.setSediSecondarie(model.getListaSecondariaSoggetto());
	}
	
	/**
	 * Handle Model Bean
	 */
	private void handleModelBean(){
		//leggo il model dalla sessione:
		ModalitaPagamentoSoggettoModel modelApp = sessionHandler.getParametro(FinSessionParameter.MODALITA_PAGAMENTO_MODEL);
		//setto nel model di pagina:
		modelWeb = modelApp;
	}
	
	/**
	 * per verificare se e' decenstrato
	 * @return
	 */
	public boolean isDecentrato() {
		//verifico l'abilitazione all'azione decentrata:
		return AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite());
	}
	
	/**
	 * Metodo che controlla se le azioni relative ad un soggetto sono disponibili o meno
	 * @param decodificaOperazione
	 * @param codiceStato
	 * @param utenteUltimaModifica
	 * @return boolean 
	 */
	public boolean isAbilitato(Integer decodificaOperazione,Integer mdpId) {
		boolean abilitato = false;
		boolean gestSogg = AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSogg, sessionHandler.getAzioniConsentite());
		boolean gestSoggDec = AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite());
		
		ModalitaPagamentoSoggetto mdpApp = new ModalitaPagamentoSoggetto();
		for(ModalitaPagamentoSoggetto app : model.getDettaglioSoggetto().getModalitaPagamentoList()){
			if(app.getUid() == mdpId.intValue()){
				mdpApp = app;
			}
		}
		if(mdpApp != null){
			switch (decodificaOperazione) {
			case CONSULTA:
				abilitato = true;
				break;
			case AGGIORNA:
				if(gestSogg){
					if(mdpApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("valido")){
						abilitato = true;
					}
				}
				if(gestSoggDec){
					if(mdpApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("provvisorio")){
						abilitato = decentratoSuPropriaOccorrenza(mdpApp.getLoginCreazione()) || decentratoSuPropriaOccorrenza(mdpApp.getLoginModifica());
					}
					if(mdpApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("valido")){
						abilitato = true;
					}
					if(mdpApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("in_modifica") || mdpApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(CostantiFin.STATO_IN_MODIFICA_no_underscore)){
						abilitato = decentratoSuPropriaOccorrenza(mdpApp.getLoginCreazione()) || decentratoSuPropriaOccorrenza(mdpApp.getLoginModifica());
					}
				}

				break;
			case ANNULLA:
				if(gestSogg){
					if(mdpApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("valido")){
						abilitato = true;
					}
				}
				break;
			case SOSPENDI:
			case BLOCCA:
				if(gestSogg){
					if(mdpApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("valido")){
						abilitato = true;
					}
				}
				break;
			case CANCELLA:
				if(gestSogg){
					if(mdpApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("valido")){
						abilitato = true;
					}
				}
				if(gestSoggDec){
					if(mdpApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("provvisorio")){
						abilitato = decentratoSuPropriaOccorrenza(mdpApp.getLoginCreazione()) || decentratoSuPropriaOccorrenza(mdpApp.getLoginModifica());
					}
				}
				break;
			//SIAC-8799
			case VALIDA:		
					if(mdpApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("provvisorio") 
							|| mdpApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("in_modifica") 
							|| mdpApp.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(CostantiFin.STATO_IN_MODIFICA_no_underscore)){
						abilitato = AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_validaSogg , sessionHandler.getAzioniConsentite());
					}
				break;
			case COLLEGA:
				
				break;
			default:
				break;
			}
		}
		return abilitato;
	}
	
	/**
	 * Metodo che controlla che il soggetto in esame sia decentrato per l'utente corrente
	 * @param utenteUltimaModifica 
	 * @return boolean 
	 */
	private boolean decentratoSuPropriaOccorrenza(String utenteUltimaModifica) {
		return isUtenteLoggato(utenteUltimaModifica);
	}

	/**
	 * Metodo che converte il model utilizzato per il binding della pagina web, nel dto utilizzato nel servizio di Ricerca Soggetto
	 * @return RicercaSoggetti
	 */
	private RicercaSoggetti convertiModelPerChiamataServizioRicerca() {
		RicercaSoggetti ricercaSoggetti = new RicercaSoggetti();
		ricercaSoggetti.setRichiedente(sessionHandler.getRichiedente());
		ricercaSoggetti.setEnte(sessionHandler.getAccount().getEnte());
		ParametroRicercaSoggetto parametroRicercaSoggetto = new ParametroRicercaSoggetto();
		
		parametroRicercaSoggetto.setIncludeModif(true);
		
		if (modelWeb.getCodice() != null) {
			parametroRicercaSoggetto.setCodiceSoggetto(modelWeb.getCodice().toString());
		}
		parametroRicercaSoggetto.setCodiceFiscale(modelWeb.getCodiceFiscale());
		parametroRicercaSoggetto.setPartitaIva(modelWeb.getPartitaIva());
		parametroRicercaSoggetto.setDenominazione(modelWeb.getDenominazione());
		
		ricercaSoggetti.setParametroRicercaSoggetto(parametroRicercaSoggetto);
		return ricercaSoggetti;
	}
	
	/**
	 * Converti Aggiorna Model Per Chiamata Servizio Ricerca
	 * costruisce request per servizio ricerca
	 * @return
	 */
	private RicercaSoggetti convertiAggiornaModelPerChiamataServizioRicerca() {
		RicercaSoggetti ricercaSoggetti = new RicercaSoggetti();
		ricercaSoggetti.setRichiedente(sessionHandler.getRichiedente());
		ricercaSoggetti.setEnte(sessionHandler.getAccount().getEnte());
		ParametroRicercaSoggetto parametroRicercaSoggetto = new ParametroRicercaSoggetto();
		if (modelWeb.getCodiceAggiorna() != null) {
			parametroRicercaSoggetto.setCodiceSoggetto(modelWeb.getCodiceAggiorna().toString());
		}
		parametroRicercaSoggetto.setCodiceFiscale(modelWeb.getCodiceFiscaleAggiorna());
		parametroRicercaSoggetto.setPartitaIva(modelWeb.getPartitaIvaAggiorna());
		parametroRicercaSoggetto.setDenominazione(modelWeb.getDenominazioneAggiorna());
		
		ricercaSoggetti.setParametroRicercaSoggetto(parametroRicercaSoggetto);
		return ricercaSoggetti;
	}	


	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */

	public String getCodiceSoggetto() {
		return codiceSoggetto;
	}

	public String getTipoAccreditoPage() {
		return tipoAccreditoPage;
	}

	public void setTipoAccreditoPage(String tipoAccreditoPage) {
		this.tipoAccreditoPage = tipoAccreditoPage;
	}

	public boolean isContoCorrenteVisible() {
		return contoCorrenteVisible;
	}

	public void setContoCorrenteVisible(boolean contoCorrenteVisible) {
		this.contoCorrenteVisible = contoCorrenteVisible;
	}

	public boolean isContanteVisible() {
		return contanteVisible;
	}

	public void setContanteVisible(boolean contanteVisible) {
		this.contanteVisible = contanteVisible;
	}

	public boolean isCessioneVisible() {
		return cessioneVisible;
	}

	public void setCessioneVisible(boolean cessioneVisible) {
		this.cessioneVisible = cessioneVisible;
	}

	public boolean isCessioneSearchVisible() {
		return cessioneSearchVisible;
	}

	public void setCessioneSearchVisible(boolean cessioneSearchVisible) {
		this.cessioneSearchVisible = cessioneSearchVisible;
	}

	public boolean isInserisciModPag() {
		return inserisciModPag;
	}

	public String getIdAccreditoTipoSelected() {
		return idAccreditoTipoSelected;
	}


	public void setIdAccreditoTipoSelected(String idAccreditoTipoSelected) {
		this.idAccreditoTipoSelected = idAccreditoTipoSelected;
	}


	public void setInserisciModPag(boolean inserisciModPag) {
		this.inserisciModPag = inserisciModPag;
	}

	public void setCodiceSoggetto(String codiceSoggetto) {
		this.codiceSoggetto = codiceSoggetto;
	}

	public boolean isSuccessInserimento() {
		return successInserimento;
	}

	public void setSuccessInserimento(boolean successInserimento) {
		this.successInserimento = successInserimento;
	}

	public boolean isCessioneSearchStep2() {
		return cessioneSearchStep2;
	}

	public void setCessioneSearchStep2(boolean cessioneSearchStep2) {
		this.cessioneSearchStep2 = cessioneSearchStep2;
	}

	public String getCodiceSoggettoRicercatoCessione() {
		return codiceSoggettoRicercatoCessione;
	}

	public void setCodiceSoggettoRicercatoCessione(
			String codiceSoggettoRicercatoCessione) {
		this.codiceSoggettoRicercatoCessione = codiceSoggettoRicercatoCessione;
	}
	public String getTipoind() {
		return tipoind;
	}
	public void setTipoind(String tipoind) {
		this.tipoind = tipoind;
	}
	
	public ModalitaPagamentoSoggettoModel getModelWeb() {
		return modelWeb;
	}

	public void setModelWeb(ModalitaPagamentoSoggettoModel modelWeb) {
		this.modelWeb = modelWeb;
	}

	public ModalitaPagamentoSoggetto getModalitaPagamentoSoggettoToInsert() {
		return modalitaPagamentoSoggettoToInsert;
	}

	public void setModalitaPagamentoSoggettoToInsert(
			ModalitaPagamentoSoggetto modalitaPagamentoSoggettoToInsert) {
		this.modalitaPagamentoSoggettoToInsert = modalitaPagamentoSoggettoToInsert;
	}

	public String getDataScadenzaStringa() {
		return dataScadenzaStringa;
	}

	public void setDataScadenzaStringa(String dataScadenzaStringa) {
		this.dataScadenzaStringa = dataScadenzaStringa;
	}

	public boolean isAggiornaContoCorrenteVisible() {
		return aggiornaContoCorrenteVisible;
	}
	
	public void setAggiornaContoCorrenteVisible(boolean aggiornaContoCorrenteVisible) {
		this.aggiornaContoCorrenteVisible = aggiornaContoCorrenteVisible;
	}

	public boolean isAggiornaContantiVisible() {
		return aggiornaContantiVisible;
	}

	public void setAggiornaContantiVisible(boolean aggiornaContantiVisible) {
		this.aggiornaContantiVisible = aggiornaContantiVisible;
	}

	public boolean isAggiornaCessioneSearch() {
		return aggiornaCessioneSearch;
	}

	public void setAggiornaCessioneSearch(boolean aggiornaCessioneSearch) {
		this.aggiornaCessioneSearch = aggiornaCessioneSearch;
	}

	public boolean isAggiornaCessioneStep2() {
		return aggiornaCessioneStep2;
	}

	public void setAggiornaCessioneStep2(boolean aggiornaCessioneStep2) {
		this.aggiornaCessioneStep2 = aggiornaCessioneStep2;
	}

	public boolean isAggiornaCessioneStep3() {
		return aggiornaCessioneStep3;
	}

	public void setAggiornaCessioneStep3(boolean aggiornaCessioneStep3) {
		this.aggiornaCessioneStep3 = aggiornaCessioneStep3;
	}

	public boolean isAggiornaSuccessVisible() {
		return aggiornaSuccessVisible;
	}

	public void setAggiornaSuccessVisible(boolean aggiornaSuccessVisible) {
		this.aggiornaSuccessVisible = aggiornaSuccessVisible;
	}

	public String getAggiornaTipoAccredito() {
		return aggiornaTipoAccredito;
	}

	public void setAggiornaTipoAccredito(String aggiornaTipoAccredito) {
		this.aggiornaTipoAccredito = aggiornaTipoAccredito;
	}

	public ModalitaPagamentoSoggetto getSoggettoDaAggiornare() {
		return soggettoDaAggiornare;
	}

	public void setSoggettoDaAggiornare(
			ModalitaPagamentoSoggetto soggettoDaAggiornare) {
		this.soggettoDaAggiornare = soggettoDaAggiornare;
	}

	public String getAggiornaCodiceSoggetto() {
		return aggiornaCodiceSoggetto;
	}

	public void setAggiornaCodiceSoggetto(String aggiornaCodiceSoggetto) {
		this.aggiornaCodiceSoggetto = aggiornaCodiceSoggetto;
	}

	public String getCodiceSoggettoRicercatoCessioneAggiorna() {
		return codiceSoggettoRicercatoCessioneAggiorna;
	}

	public void setCodiceSoggettoRicercatoCessioneAggiorna(
			String codiceSoggettoRicercatoCessioneAggiorna) {
		this.codiceSoggettoRicercatoCessioneAggiorna = codiceSoggettoRicercatoCessioneAggiorna;
	}


	public boolean isAzioneAggiorna() {
		return azioneAggiorna;
	}


	public void setAzioneAggiorna(boolean azioneAggiorna) {
		this.azioneAggiorna = azioneAggiorna;
	}

	public boolean isForceLoad() {
		return forceLoad;
	}


	public void setForceLoad(boolean forceLoad) {
		this.forceLoad = forceLoad;
	}

	public boolean isAncoraVisualizza() {
		return ancoraVisualizza;
	}

	public void setAncoraVisualizza(boolean ancoraVisualizza) {
		this.ancoraVisualizza = ancoraVisualizza;
	}

	public boolean isAncoraMdpVisualizza() {
		return ancoraMdpVisualizza;
	}

	public void setAncoraMdpVisualizza(boolean ancoraMdpVisualizza) {
		this.ancoraMdpVisualizza = ancoraMdpVisualizza;
	}


	public boolean isGenericoVisibile() {
		return genericoVisibile;
	}


	public void setGenericoVisibile(boolean genericoVisibile) {
		this.genericoVisibile = genericoVisibile;
	}


	public boolean isAggiornaGenericoVisible() {
		return aggiornaGenericoVisible;
	}

	public void setAggiornaGenericoVisible(boolean aggiornaGenericoVisible) {
		this.aggiornaGenericoVisible = aggiornaGenericoVisible;
	}

}

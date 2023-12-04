/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.accertamento;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.accertamento.GestioneAccertamentoPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, accertamento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneAccertamentoInsPrimaNotaIntegrataBaseAction<M extends GestioneAccertamentoPrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaPrimaNotaIntegrataBaseAction<Accertamento, M>  {

	/** per serializzazione **/
	private static final long serialVersionUID = -761558327804386647L;
	
	/** Servizio per i movimenti di gestione */
	@Autowired @Qualifier("movimentoGestioneFinService")
	private transient MovimentoGestioneService movimentoGestioneFinService;
	
	/** Nome del model del completamento e validazione dell'accertamento. Modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_ACCERTAMENTO_FIN = "CompletaValidaAccertamentoInsPrimaNotaIntegrataFINModel";
	/** Nome del model del completamento e validazione dell'accertamento. Modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_ACCERTAMENTO_GSA = "CompletaValidaAccertamentoInsPrimaNotaIntegrataGSAModel";
	/** Nome del model del completamento dell'accertamento, modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_ACCERTAMENTO_FIN = "CompletaAccertamentoInsPrimaNotaIntegrataFINModel";
	/** Nome del model del completamento dell'accertamento, modulo GSA */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_ACCERTAMENTO_GSA = "CompletaAccertamentoInsPrimaNotaIntegrataGSAModel";
	
	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		
		// Recupero registrazione dalla sessione
		RegistrazioneMovFin registrazioneMovFin = sessionHandler.getParametro(FinSessionParameter.REGISTRAZIONEMOVFIN);
		model.setRegistrazioneMovFin(registrazioneMovFin);
		log.debug(methodName, "registrazione ottenuta");
		
		// Controllo che la registrazione non sia gia' in uso
		if(inEsecuzioneRegistrazioneMovFin()) {
			// Registrazione in uso (prima nota automatica): esco
			return INPUT;
		}
		
		// Pulisco l'accertamento dalla sessione
		sessionHandler.setParametro(FinSessionParameter.ACCERTAMENTO, null);
		
		// Recupero l'accertametno dalla registrazione
		Accertamento accertamento = (Accertamento) model.getRegistrazioneMovFin().getMovimento();
		model.setAccertamento(accertamento);
		
		// Inizializzo i dati della prima nota
		if(model.getPrimaNota() == null){
			model.setPrimaNota(new PrimaNota());
			model.impostaDatiNelModel();
		}
		// Calcolo le descrizioni dalla registrazione
		computaStringheDescrizioneDaRegistrazione(model.getRegistrazioneMovFin());
		// SIAC-5459
		trimDescrizione();
		// Calcolo il soggetto dalla registrazione
		ricavaSoggettoDaMovimento(accertamento);

		try{
			// Caricamento della lista delle causali EP
			caricaListaCausaleEP(false);
			// Caricamento della lista delle classi
			caricaListaClassi();
			// Caricamento della lista dei titoli
			caricaListaTitoli();
		}catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			// Metto in sessione gli errori ed esco
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		
		// Recupero dei dati dell'accertamento
		getDatiDaAccertamento(accertamento);
		return SUCCESS;
	}
	
	/**
	 * Ottiene i dati dell'accertamento
	 * @param accertamento l'accertamento da cui ottenere i dati
	 */
	private void getDatiDaAccertamento(Accertamento accertamento){
		// Carico da servizio
		RicercaAccertamentoPerChiaveOttimizzato req = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato(accertamento);
		logServiceRequest(req);
		
		// Chiamata al servizio
		RicercaAccertamentoPerChiaveOttimizzatoResponse res = movimentoGestioneFinService.ricercaAccertamentoPerChiaveOttimizzato(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			// Si sono verificati degli errori: esco.
			addErrori(res);
			return;
		}
		// L'accertamento da servizio
		Accertamento accertamentoServizio = res.getAccertamento();
		sessionHandler.setParametro(FinSessionParameter.ACCERTAMENTO, accertamentoServizio);
		
		// Imposto l'accertamento nel model
		model.setAccertamento(accertamentoServizio);
		// Recupero la categoriaTipologiaTitolo
		model.setClassificatoreGerarchico(model.getAccertamento().getCapitoloEntrataGestione().getCategoriaTipologiaTitolo());
		
		// SIAC-5294
		accertamentoServizio.setElencoSubAccertamenti(defaultingList(accertamentoServizio.getElencoSubAccertamenti()));
		// Imposto la request e la response in sessione
//		sessionHandler.setParametroXmlType(FinSessionParameter.REQUEST_RICERCA_ACCERTAMENTO_PER_CHIAVE_SUBACCERTAMENTI, req);
//		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_ACCERTAMENTO_PER_CHIAVE_SUBACCERTAMENTI,
//				CollectionUtil.toListaPaginata(accertamentoServizio.getElencoSubAccertamenti(), res.getNumPagina(), res.getNumeroTotaleSub()));
		
	}

	/**
	 * Ottiene il soggetto dal movimento.
	 * 
	 * @param accertamento l'accertamento da cui ottenere il soggetto
	 */
	private void ricavaSoggettoDaMovimento(Accertamento accertamento) {
		if (accertamento != null) {
			// Se ho l'impegno ne recupero il soggetto
			model.setSoggettoMovimentoFIN(accertamento.getSoggetto());
		}
	}
	
	@Override
	protected void computaStringheDescrizioneDaRegistrazione(RegistrazioneMovFin registrazioneMovFin){
		// Collettore dei dati del movimento
		StringBuilder movimento = new StringBuilder();
		// Collettore della descrizione
		StringBuilder descrizione = new StringBuilder();
		//collettore per il titolo
		StringBuilder titolo = new StringBuilder();
		// Il movimento di gestione
		Accertamento accertamento = (Accertamento) registrazioneMovFin.getMovimento();
		
		String annoNumeroAccertamento = new StringBuilder()
				.append(accertamento.getAnnoMovimento())
				.append("/")
				.append(accertamento.getNumeroBigDecimal().toPlainString())
				.toString();
		
		// Anno e numero
		descrizione.append("Acc ")
			.append(annoNumeroAccertamento);
		titolo.append("Accertamento ")
			.append(annoNumeroAccertamento);
		
		// Anno e numero (e stringa acc per riconoscerlo)
		movimento
			.append(annoNumeroAccertamento)
			.append(" (acc)");
		
		String descrizioneImp = accertamento.getDescrizione();
		if(StringUtils.isNotBlank(descrizioneImp)) {
			// Descrizione se presente
			descrizione.append(" ")
				.append(descrizioneImp);
			titolo.append(" -  ")
			.append(descrizioneImp);
		}
		
		titolo.append(accertamento.getImportoAttuale() != null? ( " - " + accertamento.getImportoAttuale().toString()) : "")
		.append(StringUtils.isNotBlank(accertamento.getDescrizioneStatoOperativoMovimentoGestioneEntrata())? ( " - " + accertamento.getDescrizioneStatoOperativoMovimentoGestioneEntrata()) : "" );

		// Imposto i dati del movimento nel model
		model.setDescrMovimentoFinanziario(movimento.toString());
		// Inizializzo la descrizione della prima nota
		model.getPrimaNota().setDescrizione(descrizione.toString());
		// Imposto la data di registrazione
		model.getPrimaNota().setDataRegistrazione(new Date());
		model.setCampoTitoloPagina(titolo.toString());
	}

	

	/**
	 * Validazione per il metodo {@link #completeSalva()}.
	 */
	public void validateCompleteSalva() {
		// Se non ho la prima nota, qualcosa e' andato storto: esco subito
		checkNotNull(model.getPrimaNota(), "Prima Nota Integrata ", true);
		// La causale EP deve essere presente
		checkNotNullNorInvalidUid(model.getCausaleEP(), "CausaleEP");
		// La data di registrazione deve essere presente
		checkNotNull(model.getPrimaNota().getDataRegistrazione(), "Data registrazione ");
		// La descrizione deve essere presente
		checkNotNullNorEmpty(model.getPrimaNota().getDescrizione(), "Descrizione ");

		// Devo avere le scritture corrette
		List<MovimentoDettaglio> listaMovimentiDettaglioFinal = checkScrittureCorrette();
		
		// Esco se ho errori
		if(hasErrori()) {
			return;
		}
		
		// Impostazione dei dati nel model
		MovimentoEP movEP = new MovimentoEP();
		// Imposto la causale
		movEP.setCausaleEP(model.getCausaleEP());
		// Imposta la registrazione
		movEP.setRegistrazioneMovFin(model.getRegistrazioneMovFin());
		// Imposto la lista dei dettagli
		model.setListaMovimentoDettaglio(listaMovimentiDettaglioFinal);
		movEP.setListaMovimentoDettaglio(listaMovimentiDettaglioFinal);
		
		// Aggiungo il movimento
		model.getListaMovimentoEP().add(movEP);
	}

	/**
	 * Completamento per lo step 2.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	
	public String completeSalva() {
		final String methodName = "completeSalva";
		
		// Aggiorno il numero di riga
		aggiornaNumeroRiga();
		// L'inserisci e' sempre presente da registrazione quindi chiamo il completa
		
		// Inserimento della causale
		RegistraPrimaNotaIntegrata req = model.creaRequestRegistraPrimaNotaIntegrata();
		logServiceRequest(req);
		// Chiamata al servizio
		RegistraPrimaNotaIntegrataResponse res = primaNotaService.registraPrimaNotaIntegrata(req);
		logServiceResponse(res);
		// Controllo gli errori
		if(res.hasErrori()) {
			// Si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'inserimento della Prima Nota integrata");
			addErrori(res);
			return INPUT;
		}
		
		log.info(methodName, "Registrata correttamente Prima Nota integrata con uid " + res.getPrimaNota().getUid());
		if (model.isValidazione()) {
			// Effettuo un log di fine validazione
			log.info(methodName, "Validata correttamente Prima Nota integrata con uid " + res.getPrimaNota().getUid());
		}
		
		// Imposto i dati della causale restituitimi dal servizio
		model.setPrimaNota(res.getPrimaNota());
		// Imposto il messaggio di successo
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}

	@Override
	protected ElementoPianoDeiConti ottieniElementoPianoDeiContiDaMovimento() {
		// Recupero il piano dei conti dal capitolo dell'accertamento
		return model.getAccertamento() != null
			&& model.getAccertamento().getCapitoloEntrataGestione() != null
				? model.getAccertamento().getCapitoloEntrataGestione().getElementoPianoDeiConti()
				: null;
	}
	
	@Override
	protected Soggetto ottieniSoggettoDaMovimento() {
		// Recupero il soggetto dall'impegno
		return model.getAccertamento() != null ? model.getAccertamento().getSoggetto() : null;
	}
}


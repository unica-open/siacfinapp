/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.impegno;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.impegno.GestioneImpegnoPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, impegno.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 13/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneImpegnoInsPrimaNotaIntegrataBaseAction<M extends GestioneImpegnoPrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaPrimaNotaIntegrataBaseAction<Impegno, M> {


	/** Per la serializzazione */
	private static final long serialVersionUID = -5788328200257976328L;

	/** Servizio per i movimenti di gestione */
	@Autowired @Qualifier("movimentoGestioneFinService")
	private transient MovimentoGestioneService movimentoGestioneFinService;
	
	/** Nome del model del completamento e validazione dell'accertamento. Modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_IMPEGNO_FIN = "CompletaValidaImpegnoInsPrimaNotaIntegrataFINModel";
	/** Nome del model del completamento e validazione dell'accertamento. Modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_IMPEGNO_GSA = "CompletaValidaImpegnoInsPrimaNotaIntegrataGSAModel";
	/** Nome del model del completamento dell'accertamento, modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_IMPEGNO_FIN = "CompletaImpegnoInsPrimaNotaIntegrataFINModel";
	/** Nome del model del completamento dell'accertamento, modulo GSA */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_IMPEGNO_GSA = "CompletaImpegnoInsPrimaNotaIntegrataGSAModel";
	
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
		
		// Pulisco l'impegno dalla sessione
		sessionHandler.setParametro(FinSessionParameter.IMPEGNO, null);
		
		// Recupero l'impegno dalla registrazione
		Impegno impegno = (Impegno) model.getRegistrazioneMovFin().getMovimento();
		model.setImpegno(impegno);
		
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
		ricavaSoggettoDaMovimento(impegno);

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
		
		// Recupero dei dati dell'impegno
		getDatiDaImpegno(impegno);
		return SUCCESS;
	}

	/**
	 * Ottiene i dati dell'impegno
	 * @param impegno l'impegno da cui ottenere i dati
	 */
	private void getDatiDaImpegno(Impegno impegno){
		// Creo la request per la chiamata al servizio
		RicercaImpegnoPerChiaveOttimizzato req = model.creaRequestRicercaImpegnoPerChiaveOttimizzato(impegno);
		logServiceRequest(req);
		
		// Chiamata al servizio
		RicercaImpegnoPerChiaveOttimizzatoResponse res = movimentoGestioneFinService.ricercaImpegnoPerChiaveOttimizzato(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			// Si sono verificati degli errori: esco.
			addErrori(res);
			return;
		}
		// L'impegno da servizio
		Impegno impegnoServizio = res.getImpegno();
		sessionHandler.setParametro(FinSessionParameter.IMPEGNO, impegnoServizio);
		
		// Imposto l'impegno nel model
		model.setImpegno(impegnoServizio);
		// Recupero il macroaggregato
		model.setClassificatoreGerarchico(model.getImpegno().getCapitoloUscitaGestione().getMacroaggregato());
		
		// SIAC-5294
		impegnoServizio.setElencoSubImpegni(defaultingList(impegnoServizio.getElencoSubImpegni()));
		// Imposto la request e la response in sessione
	}

	/**
	 * Ottiene il soggetto dal movimento.
	 * 
	 * @param impegno l'impegno da cui ottenere il soggetto
	 */
	private void ricavaSoggettoDaMovimento(Impegno impegno) {
		if (impegno != null) {
			// Se ho l'impegno ne recupero il soggetto
			model.setSoggettoMovimentoFIN(impegno.getSoggetto());
		}
	}
	
	@Override
	protected void computaStringheDescrizioneDaRegistrazione(RegistrazioneMovFin registrazioneMovFin){
		// Collettore dei dati del movimento
		StringBuilder movimento = new StringBuilder();
		// Collettore della descrizione
		StringBuilder descrizione = new StringBuilder();
		//collettore del titolo
		StringBuilder titolo = new StringBuilder();
		
		// Il movimento di gestione
		Impegno impegno = (Impegno) registrazioneMovFin.getMovimento();
		
		String annoNumeroImpegno = new StringBuilder()
				.append(impegno.getAnnoMovimento())
				.append("/")
				.append(impegno.getNumeroBigDecimal().toPlainString())
				.toString();
		
		// Anno e numero
		descrizione.append("Imp ")
			.append(annoNumeroImpegno);
		// Anno e numero (e stringa imp per riconoscerlo)
		movimento.append(annoNumeroImpegno)
					.append(" (imp)");
		// Anno e numero (e stringa imp per riconoscerlo)
		titolo.append("Impegno ")
			.append(annoNumeroImpegno);
		String descrizioneImpegno = impegno.getDescrizione();
		
		if(StringUtils.isNotBlank(descrizioneImpegno)) {
			// Descrizione se presente
			descrizione.append(" ")
				.append(descrizioneImpegno);
			titolo.append(" - ")
				.append(descrizioneImpegno);
		}
		
		titolo.append(impegno.getImportoAttuale() != null? ( " - " + impegno.getImportoAttuale().toString()) : "")
			.append(StringUtils.isNotBlank(impegno.getDescrizioneStatoOperativoMovimentoGestioneSpesa())? ( "-" + impegno.getDescrizioneStatoOperativoMovimentoGestioneSpesa()) : "" );

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
	 * Preparazione per il metodo {@link #completeSalva()}
	 */
	public void prepareCompleteSalva() {
		// SIAC-5336: pulizia del classificatore GSA
		model.setClassificatoreGSA(null);
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
		// Recupero il piano dei conti dal capitolo dell'impegno
		return model.getImpegno() != null
			&& model.getImpegno().getCapitoloUscitaGestione() != null
				? model.getImpegno().getCapitoloUscitaGestione().getElementoPianoDeiConti()
				: null;
	}
	
	@Override
	protected Soggetto ottieniSoggettoDaMovimento() {
		// Recupero il soggetto dall'impegno
		return model.getImpegno() != null ? model.getImpegno().getSoggetto() : null;
	}
}


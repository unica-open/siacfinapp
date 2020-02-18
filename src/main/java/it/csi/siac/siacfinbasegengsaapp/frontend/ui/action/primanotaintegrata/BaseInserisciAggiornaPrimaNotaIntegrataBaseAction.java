/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata;
 
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccommonapp.util.exception.GenericFrontEndMessagesException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.GenericContabilitaGeneraleAction;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.util.selector.CausaleEPSelector;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.util.wrapper.ElementoScritturaPrimaNotaIntegrata;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.CausaleService;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.RegistrazioneMovFinService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausaleResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoEvento;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

/**
 * Action base per i risultati di ricerca della registrazione.
 *
 * @author Simona Paggio
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/05/2015
 * @version 1.1.0 - 12/10/215 - gestione GEN/GSA
 * @param <E> la tipizzazione dell'entita
 * @param <M> la tipizzazione del model
 */
public abstract class BaseInserisciAggiornaPrimaNotaIntegrataBaseAction <E extends Entita, M extends BaseInserisciAggiornaPrimaNotaIntegrataBaseModel<E>>
		extends GenericContabilitaGeneraleAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8332479756820934287L;
	/** Serviz&icirc; della causale */
	@Autowired private transient CausaleService causaleService;
	/** Serviz&icirc; delle codifiche */
	@Autowired private transient CodificheService codificheService;
	/** Serviz&icirc; dei classificatori bil */
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	
	/** Serviz&icirc; della registrazione movfin */
	@Autowired protected transient RegistrazioneMovFinService registrazioneMovFinService;
	/** Serviz&icirc; della prima nota */
	@Autowired protected transient PrimaNotaService primaNotaService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		cleanErrori();
		cleanMessaggi();
		cleanInformazioni();
	}

	@Override
	public void prepareExecute() throws Exception {
		Object nonPulireModel = sessionHandler.getParametro(FinSessionParameter.NON_PULIRE_MODEL);
		sessionHandler.setParametro(FinSessionParameter.NON_PULIRE_MODEL, null);
		if(!Boolean.TRUE.equals(nonPulireModel)) {
			setModel(null);
			super.prepare();
		}
	}
	
	/**
	 * Atterraggio sulla pagina.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String landOnPage() {
		return SUCCESS;
	}
	
	/**
	 * Ricerca di dettaglio della registrazione movimento fin.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	protected void ricercaDettaglioRegistrazioneMovFin() throws WebServiceInvocationFailureException {
		RegistrazioneMovFin registrazioneMovFin = sessionHandler.getParametro(FinSessionParameter.REGISTRAZIONEMOVFIN);
		if(registrazioneMovFin == null) {
			throw new GenericFrontEndMessagesException("Nessuna registrazione caricata dal sistema");
		}
		model.setRegistrazioneMovFin(registrazioneMovFin);
	}
	
	/**
	 * Ricerca il dettaglio della prima nota tramite la registrazione.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	protected void ricercaDettaglioPrimaNota() throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDettaglioPrimaNota";
		RicercaDettaglioPrimaNotaIntegrata req = model.creaRequestRicercaDettaglioPrimaNotaIntegrata();
		logServiceRequest(req);
		RicercaDettaglioPrimaNotaIntegrataResponse res = primaNotaService.ricercaDettaglioPrimaNotaIntegrata(req);
		logServiceResponse(res);
		
		if(res.hasErrori() && !res.verificatoErrore(ErroreCore.ENTITA_INESISTENTE)) {
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		
		// Non ho errori. Controllo di avere la prima nota
		if(res.getPrimaNota() != null && StatoOperativoPrimaNota.PROVVISORIO.equals(res.getPrimaNota().getStatoOperativoPrimaNota())) {
			log.debug(methodName, "Nessun errore nel caricamento della prima nota integrata: presente in archivio con uid " + res.getPrimaNota().getUid());
			model.setPrimaNota(res.getPrimaNota());
			return;
		}
		
		log.debug(methodName, "Nessun errore nel caricamento della prima nota integrata, ma non presente in archivio");
		// Non ho la prima nota dalla response
		if(model.getPrimaNota() == null) {
			model.setPrimaNota(new PrimaNota());
		}
		model.getPrimaNota().setDataRegistrazione(new Date());
	}
	
	/**
	 * Controlla che le scritture siano corrette. Devono essere:
	 * <ul>
	 *     <li>
	 *         presenti almeno 2 scritture su conti con segni differenti (pu&ograve; essere anche lo stesso conto devono essere diversi i segni),
	 *         altrimenti viene visualizzato il messaggio
	 *         <code>&lt;COR_ERR_0044 - Operazione non consentita ('Devono essere presenti almeno due conti con segni differenti.')&gt;</code>
	 *     </li>
	 *     <li>
	 *         il totale dare deve essere uguale al totale avere: altrimenti viene visualizzato il messaggio
	 *         <code>&lt;COR_ERR_0044 - Operazione non consentita ('Il totale DARE deve essere UGUALE al totale AVERE.')&gt;</code>
	 *     </li>
	 * </ul>
	 * 
	 * @return la lista dei movimenti di dettaglio finali
	 */
	protected List<MovimentoDettaglio> checkScrittureCorrette() {
		int numeroScrittureDare = 0;
		int numeroScrittureAvere = 0;
		
		BigDecimal totaleDare = BigDecimal.ZERO;
		BigDecimal totaleAvere = BigDecimal.ZERO;
		
		List<ElementoScritturaPrimaNotaIntegrata> elaborata = model.getListaElementoScritturaPerElaborazione();
		List<MovimentoDettaglio> listaMovimentiDettaglioFinal = new ArrayList<MovimentoDettaglio>();
		
		for (ElementoScritturaPrimaNotaIntegrata elementoScrittura : elaborata){
			if (elementoScrittura != null && elementoScrittura.getMovimentoDettaglio() != null){
				BigDecimal importo = elementoScrittura.getMovimentoDettaglio().getImporto();
				
				checkCondition(elementoScrittura.getMovimentoDettaglio().getConto() != null && elementoScrittura.getMovimentoDettaglio().getConto().getUid() != 0,
						ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("e' necessario assegnare i conti a tutte le scritture"), true);
				
				if(importo != null) {
					// Aggiungo i dati al segno dare o avere
					if(elementoScrittura.isSegnoDare()) {
						numeroScrittureDare++;
						totaleDare = totaleDare.add(importo);
					} else if(elementoScrittura.isSegnoAvere()) {
						numeroScrittureAvere++;
						totaleAvere = totaleAvere.add(importo);
					}
					
					listaMovimentiDettaglioFinal.add(elementoScrittura.getMovimentoDettaglio());
				} else {
					checkCondition(false,
						ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo Conto " + elementoScrittura.getMovimentoDettaglio().getConto().getCodice()
							+ " " + elementoScrittura.getMovimentoDettaglio().getSegno()));
				}
				
			}
		}
		
		checkCondition(numeroScrittureDare > 0 && numeroScrittureAvere > 0, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Devono essere presenti almeno due conti con segni differenti"));
		checkCondition(totaleDare.subtract(totaleAvere).signum() == 0, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il totale DARE deve essere UGUALE al totale AVERE"));
		
		return listaMovimentiDettaglioFinal;
	}
	
	/**
	 * Calcola le stringhe per la descrizione a partire dalla registrazione.
	 * 
	 * @param registrazioneMovFin la registrazione da cui ottenere le descrizioni
	 */
	protected void computaStringheDescrizioneDaRegistrazione(RegistrazioneMovFin registrazioneMovFin) {
		// Implementazione vuota, da overridare se necessario
	}

	/**
	 * Calcola il parametro per porre in sessione la lista delle causali EP integrate.
	 * 
	 * @return il parametro per la sessione
	 */
	protected abstract FinSessionParameter getSessionParameterListaCausaleEPIntegrata();

	/**
	 * Caricamento delle liste causale.
	 * @param mayReuseSessionData se si possa riusare il dato in sessione
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	protected void caricaListaCausaleEP(boolean mayReuseSessionData) throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaCausaleEP";
		
		List<CausaleEP> listaCausaliEp = sessionHandler.getParametro(getSessionParameterListaCausaleEPIntegrata());
		Integer uidUltimoTipoEvento = sessionHandler.getParametro(FinSessionParameter.ULTIMO_TIPO_EVENTO_RICERCATO);
		
		RegistrazioneMovFin registrazioneMovFin = model.getRegistrazioneMovFin();
		//prendo il soggetto
		
		TipoEvento tipoEvento = null;
		if(registrazioneMovFin != null && registrazioneMovFin.getEvento() != null) {
			tipoEvento = registrazioneMovFin.getEvento().getTipoEvento();
			model.setEvento(registrazioneMovFin.getEvento());
		}
		
		model.setTipoEvento(tipoEvento);
		
		boolean isTipoEventoDiversoDaUltimo = tipoEvento != null  && uidUltimoTipoEvento != null && (tipoEvento.getUid() != uidUltimoTipoEvento.intValue());
		if (!mayReuseSessionData || (isTipoEventoDiversoDaUltimo || uidUltimoTipoEvento == null || listaCausaliEp == null)) {
			//recuperare da servizio la lista causali
			RicercaSinteticaModulareCausale req = model.creaRequestRicercaSinteticaModulareCausale();
			logServiceRequest(req);
			RicercaSinteticaModulareCausaleResponse res = causaleService.ricercaSinteticaModulareCausale(req);
			logServiceResponse(res);
			
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorMsg = createErrorInServiceInvocationString(req, res);
				log.warn(methodName, errorMsg);
				addErrori(res);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			if(res.getCausali() == null || res.getCausali().isEmpty()){
				//JIRA 3679
				ErroreGEN errore = (model.getAmbito() !=null && model.getAmbito().equals(Ambito.AMBITO_GSA)) ? ErroreGEN.OPERAZIONE_NON_CONSENTITA_0015_GSA : ErroreGEN.OPERAZIONE_NON_CONSENTITA_0015;
				String errorMsg = "Errore nell'invocazione del servizio RicercaSinteticaCausale "+ errore.getErrore().getTesto();
				log.warn(methodName, errorMsg);
				addErrore(errore.getErrore());
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			// Filtro le causali
			listaCausaliEp = filtraCausaliEP(res.getCausali());
			if(listaCausaliEp.isEmpty()){
				String errorMsg = "Errore nell'invocazione del servizio RicercaSinteticaCausale" + ErroreGEN.OPERAZIONE_NON_CONSENTITA_0014.getErrore().getTesto();
				log.warn(methodName, errorMsg);
				addErrore(ErroreGEN.OPERAZIONE_NON_CONSENTITA_0014.getErrore());
				throw new WebServiceInvocationFailureException(errorMsg);
			}

			// Aggiungo il risultato in sessione
			sessionHandler.setParametro(getSessionParameterListaCausaleEPIntegrata(), listaCausaliEp);
			sessionHandler.setParametro(FinSessionParameter.ULTIMO_TIPO_EVENTO_RICERCATO, model.getTipoEvento().getUid());
			
		}
		model.setListaCausaleEP(listaCausaliEp);
		impostaCausaleIniziale(listaCausaliEp);
		
	}
	
	/**
	 * FIltra le causali EP a partire dalla lista ottenuta dalla response.
	 * 
	 * @param listaCausaleEP la lista delle causali da filtrare
	 * @return le causali filtrate
	 */
	protected List<CausaleEP> filtraCausaliEP(List<CausaleEP> listaCausaleEP) {
		List<CausaleEP> listaCausaliEp = new ArrayList<CausaleEP>();
		for(CausaleEP c : listaCausaleEP){
			if(c.getSoggetto() == null || c.getSoggetto().getUid() == 0 || model.getSoggettoMovimentoFIN() == null || c.getSoggetto().getUid() == model.getSoggettoMovimentoFIN().getUid()) {
				listaCausaliEp.add(c);
			}
		}
		return listaCausaliEp;
	}

	/**
	 * Impostazione della causale iniziale
	 * @param listaCausaliEp la lista da cui ottenere la causale
	 */
	protected void impostaCausaleIniziale(List<CausaleEP> listaCausaliEp) {
		CausaleEPSelector causaleEPSelector = istanziaSelettoreCausale();
		// In alcuni casi non mi serve
		if(causaleEPSelector == null) {
			return;
		}
		CausaleEP causaleEP = causaleEPSelector.selezionaCausaleEP(listaCausaliEp);
		// TODO: ricerca dettaglio modulare?
		model.setCausaleEP(causaleEP);
	}
	
	/**
	 * Istanziazione del selettore della causale EP.
	 * 
	 * @return il selettore della causale
	 */
	protected CausaleEPSelector istanziaSelettoreCausale() {
		// Da implementare nelle sottoclassi
		return null;
	}
	
	/**
	 * Ottiene l'elemento del piano dei conti dal movimento.
	 * 
	 * @return l'elemento del piano dei conti
	 */
	protected ElementoPianoDeiConti ottieniElementoPianoDeiContiDaMovimento() {
		// Da implementare nelle sottoclassi
		return null;
	}
	
	/**
	 * Ottiene il soggetto dal movimento.
	 * 
	 * @return il soggetto
	 */
	protected Soggetto ottieniSoggettoDaMovimento() {
		// Da implementare nelle sottoclassi
		return null;
	}

	/**
	 * Caricamento della lista delle classi.
	 */
	protected void caricaListaClassi() {
		RicercaCodifiche reqRC = model.creaRequestRicercaClassi();
		logServiceRequest(reqRC);
		RicercaCodificheResponse resRC = codificheService.ricercaCodifiche(reqRC);
		logServiceResponse(resRC);
		
		if(!resRC.hasErrori()){
			model.setListaClassi(resRC.getCodifiche(ClassePiano.class));
		}else{
			addErrori(resRC);
		}
	}
	
	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una request per il servizio di {@link LeggiClassificatoriByTipoElementoBilResponse}.
	 * 
	 * @param codice il codice definente il classificatore
	 * @return la response corrispondente
	 */
	protected LeggiClassificatoriByTipoElementoBilResponse ottieniResponseLeggiClassificatoriByTipoElementoBil(String codice) {
		LeggiClassificatoriByTipoElementoBil req = model.creaRequestLeggiClassificatoriByTipoElementoBil(codice);
		logServiceRequest(req);
		LeggiClassificatoriByTipoElementoBilResponse res = classificatoreBilService.leggiClassificatoriByTipoElementoBil(req);
		logServiceResponse(res);
		return res;
	}
	
	/**
	 * Caricamento della lista dei titoli.
	 */
	protected void caricaListaTitoli() {
		LeggiClassificatoriByTipoElementoBilResponse response = ottieniResponseLeggiClassificatoriByTipoElementoBil(WebAppConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE);
		List<TitoloEntrata> listaTE = response.getClassificatoriTitoloEntrata();
		
		if(!listaTE.isEmpty()){
			model.setListaTitoloEntrata(listaTE);
		}else{
			model.setListaTitoloEntrata(new ArrayList<TitoloEntrata> ());
		}
		
		LeggiClassificatoriByTipoElementoBilResponse responseSpesa = ottieniResponseLeggiClassificatoriByTipoElementoBil(WebAppConstants.CODICE_CAPITOLO_USCITA_GESTIONE);
		List<TitoloSpesa> listaTS = responseSpesa.getClassificatoriTitoloSpesa();
		
		if(!listaTS.isEmpty()){
			model.setListaTitoloSpesa(listaTS);
		}else{
			model.setListaTitoloSpesa(new ArrayList<TitoloSpesa> ());
		}
	}
	
	
	/**
	 * Aggiornamento del numero di riga per i movimenti
	 */
	protected void aggiornaNumeroRiga() {
		List<MovimentoEP> listaMovimentoEP = model.getListaMovimentoEP();
		for(MovimentoEP mep : listaMovimentoEP) {
			int numeroRiga = 0;
			for(MovimentoDettaglio md : mep.getListaMovimentoDettaglio()) {
				md.setNumeroRiga(numeroRiga);
				numeroRiga++;
			}
		}
	}

	/**
	 * Controlla se l'elaborazione della registrazione movfin sia attiva. In tal caso esce dall'esecuzione
	 * @return <code>true</code> se l'esecuzione &eacute; attiva; <code>false</code> altrimenti
	 */
	protected boolean inEsecuzioneRegistrazioneMovFin() {
		RegistraPrimaNotaIntegrata req = model.creaRequestRegistraPrimaNotaIntegrataPerCheckEsecuzione();
		RegistraPrimaNotaIntegrataResponse res = primaNotaService.registraPrimaNotaIntegrata(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			setErroriInSessionePerActionSuccessiva();
			return true;
		}
		return false;
	}
	
	/**
	 * Trim della descrizione a 500 caratteri
	 */
	protected void trimDescrizione() {
		model.getPrimaNota().setDescrizione(StringUtils.substring(model.getPrimaNota().getDescrizione(), 0, 500));
	}
}

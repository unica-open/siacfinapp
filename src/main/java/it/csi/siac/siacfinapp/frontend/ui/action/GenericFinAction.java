/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.SerializationUtils;

import com.opensymphony.xwork2.ActionContext;

import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaClassificatore;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaClassificatoreResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonapp.action.GenericAction;
import it.csi.siac.siaccommonapp.util.exception.UtenteNonLoggatoException;
import it.csi.siac.siaccorser.frontend.webservice.ClassificatoreService;
import it.csi.siac.siaccorser.frontend.webservice.CoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabile;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabileResponse;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinapp.frontend.ui.action.ajax.ServiceCache;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;
import it.csi.siac.siacfinapp.frontend.ui.model.commons.GestoreDatiAlberoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciMovGestModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinActionUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.FinLogger;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.ClassificatoreFinService;
import it.csi.siac.siacfinser.frontend.webservice.GenericService;
import it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService;
import it.csi.siac.siacfinser.frontend.webservice.MutuoService;
import it.csi.siac.siacfinser.frontend.webservice.OrdinativoService;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.Liste;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.PaginazioneRequest;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccountPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccountPerChiaveResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;



public abstract class GenericFinAction<M extends GenericFinModel> extends GenericAction<M> implements ApplicationContextAware {


	private static final long serialVersionUID = -4825919870290768689L;
	
	@Autowired
	protected transient GenericService genericService;
	
	@Autowired
	protected transient ClassificatoreBilService classificatoreBilService;
	
	@Autowired
	protected transient ClassificatoreFinService classificatoreFinService;
	
	@Autowired
	protected MutuoService mutuoService;
	
	@Autowired
	protected LiquidazioneService liquidazioneService;
	
	@Autowired
	protected OrdinativoService ordinativoService;
	
	@Autowired
	protected ProvvisorioService provvisorioService;	
	
	@Autowired
	protected transient ClassificatoreService classificatoreService;
	
	@Autowired
	protected CoreService coreService;
	
	protected ApplicationContext ctx;
	
	@Autowired
	protected transient Long ttlCacheCodifiche;
	
	private ActionDataEnum actionDataMode = ActionDataEnum.SAVE;
	
	protected boolean persistentActionMessages, persistentActionErrors, persistentActionWarnings, forceReload = true, sonoInAggiornamentoOrdinativo;
	protected String forward;
	protected String ambito;
	
	// stringhe che contengono i nomi delle funzionalita' (comma-separated) sulle quali bisogna fare la verifica di abilitazione
	protected String azioniToCheck, azioniDecToCheck;
	
	public static final int DEFAULT_PAGE_SIZE = 10;
	
	private Collection<String> actionWarnings = new ArrayList<String>(0);
	
    
	public boolean bottoneIndietroDiTipoSecondBtn= false;
	
	protected static final String CLASS_CAPITOLO_TITOLO_1 = "1";
	protected static final String CLASS_CAPITOLO_TITOLO_2 = "2";
	protected static final String CLASS_CAPITOLO_TITOLO_3 = "3";
	protected static final String CLASS_CAPITOLO_TITOLO_4 = "4";
	protected static final String CLASS_CAPITOLO_TITOLO_5 = "5";
	protected static final String CLASS_CAPITOLO_TITOLO_6 = "6";
	protected static final String CLASS_CAPITOLO_TITOLO_7 = "7";
	
	protected static final String CLASS_CAPITOLO_TIPOLOGIA_4 ="4040100";
	protected static final String CLASS_CAPITOLO_MACROAGGREGATO_1 ="4010000";

	/**
	 *  stopWatchLogger utilizzato per lo stopWatch in alcuni punti
	 *  per verificare le prestazioni
	 */
	protected static FinLogger stopWatchLogger = FinLogger.getLoggerCSV("FIN");
	
	protected static final String RADICE_ALBERO = "0";
	
	// variabile di appoggio per il caricamento dati delle
	// stutture amministrative
	private ArrayList<GestoreDatiAlberoModel> listaStruttureAmministrativeTmpSupport;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();		
		
		final String methodName = "prepare";
		
		/* Imposta nel model il richiedente, l'account, l'ente e l'anno di esercizio dalla sessione */
		
		if(sessionHandler.getAccount() == null) {
			log.error(methodName, "Sessione scaduta");
			throw new UtenteNonLoggatoException("Sessione scaduta");
		}
		
		log.debug(methodName, "Inizializzazione dell'ente");
		model.setEnte(sessionHandler.getAccount().getEnte());	
		
		log.debug(methodName, "Inizializzazione del richiedente");
		model.setRichiedente(sessionHandler.getRichiedente());	
		
		// descrizione anno bilancio
		model.setDescrizioneAnnoBilancio(sessionHandler.getDescrizioneAnnoBilancio());
		
		String infoUtenteLogin =  sessionHandler.getAccount().getEnte().getNome() + " - " +
				sessionHandler.getAccount().getNome()  
			+ " - " +		sessionHandler.getOperatore().getNome() + " "+sessionHandler.getOperatore().getCognome() ;
		
		
		//set info utente login
		model.setInfoUtenteLogin(infoUtenteLogin);
		
		// pulisco sempre gli eventuali in errori sia in request che in session
		clearErrorsAndMessages(); 
		clearActionErrors(); 
		clearErrors();
		
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> actionDataKeyValues(){
		return (Map<String, Object>)getSession().get(FinSessionParameter.ACTION_DATA.getName());
	}
	

	public String getAccount(){
		return sessionHandler.getAccount().getNome();
	}

	/**
	 * metodo che gestisce i forward per le pagine con richiesta conferma modifica
	 * @return
	 */
	public String gestisciForward() {
		if(forward != null && !"".equalsIgnoreCase(forward)){
			if(ambito != null && !"".equalsIgnoreCase(ambito)){
				if(ambito.equalsIgnoreCase("movgest")){
					modelMovGestRollback();
					log.debug("","ROLLBACK");
				}
			}
			
			return forward;
		} else {
			return INPUT;
		}
		
	}
	
	
	/**
	 * metodo che gestisce la cache dei model ralativi al modulo Movimento Gestione
	 * @return
	 */
	protected void modelMovGestRollback(){
		((GestisciMovGestModel) model).setStep1Model(clone(((GestisciMovGestModel) model).getStep1ModelCache()));
		((GestisciMovGestModel) model).setStep3Model(clone(((GestisciMovGestModel) model).getStep3ModelCache()));
		((GestisciMovGestModel) model).setStep1ModelSubimpegno(clone(((GestisciMovGestModel) model).getStep1ModelSubimpegnoCache()));
		((GestisciMovGestModel) model).setStep2ModelSubimpegnoCache(clone(((GestisciMovGestModel) model).getStep2ModelSubimpegnoCache()));
		((GestisciMovGestModel) model).setMovimentoSpesaModel(clone(((GestisciMovGestModel) model).getMovimentoSpesaModelCache()));
	}
	
	protected LeggiClassificatoriGenericiByTipoElementoBilResponse caricaClassifGenericiFin(String tipoElementoBilancio ){
		//istanzio la request per il servizio leggiClassificatoriGenericiByTipoElementoBil:
		LeggiClassificatoriGenericiByTipoElementoBil bFin = new LeggiClassificatoriGenericiByTipoElementoBil();
		bFin.setTipoElementoBilancio(tipoElementoBilancio); 
		bFin.setAnno(Integer.parseInt(sessionHandler.getAnnoEsercizio()));
		bFin.setRichiedente(sessionHandler.getRichiedente());
		bFin.setIdEnteProprietario(sessionHandler.getEnte().getUid());
		//invoco il servizio leggiClassificatoriGenericiByTipoElementoBil:
		LeggiClassificatoriGenericiByTipoElementoBilResponse respBil = classificatoreBilService.leggiClassificatoriGenericiByTipoElementoBil(bFin);
	
		return respBil;
	}
	
	protected RicercaSinteticaClassificatoreResponse caricaClassifGenericiBilFiltrati(TipologiaClassificatore tipologiaClassificatore){
		//istanzio la request per il servizio ricercaSinteticaClassificatore:
		RicercaSinteticaClassificatore request = new RicercaSinteticaClassificatore();
		request.setTipologiaClassificatore(tipologiaClassificatore); 
		request.setAnno(Integer.parseInt(sessionHandler.getAnnoEsercizio()));
		request.setRichiedente(sessionHandler.getRichiedente());
		
		ParametriPaginazione pp = new ParametriPaginazione();
		pp.setNumeroPagina(0);
		pp.setElementiPerPagina(Integer.MAX_VALUE);
		request.setParametriPaginazione(pp);
		//invoco il servizio ricercaSinteticaClassificatore:
		RicercaSinteticaClassificatoreResponse response = classificatoreBilService.ricercaSinteticaClassificatore(request);
	
		return response;
	}
	
	/**
	 * Metodo di utilit&agrave; per il log degli errori dovuti alla validazione.
	 * 
	 * @param listaErrori la lista degli errori ottenuta
	 */
	protected void terminaValidazione(List<Errore> listaErrori) {
		final String methodName = "logValidazione";
		
		log.debug(methodName, "Validazione effetuata. Trovati degli errori? " + hasErrors());	
		// Carica gli errori, nel caso vi siano
		addErrori(listaErrori);
		
		// Log degli ActionErrors
		for(String errore : getActionErrors()){
			log.info(methodName, "Errore action: " + errore);
		}
		// Log dei FieldErrors
		for(Entry<String, List<String>> errore : getFieldErrors().entrySet()) {
			for(String error : errore.getValue()) {
				log.info(methodName, "Errore field: " + errore.getKey() + " - " + error);
			}
		}
		// Log degli ActionMessages
		for(String errore : getActionMessages()){
			log.info(methodName, "Messaggio action: " + errore);
		}
		// Log degli ActionMessages
		for(String errore : getActionMessages()){
			log.info(methodName, "Messaggio action: " + errore);
		}
		
	}
	public Object stack(String expr) {
		return ActionContext.getContext().getValueStack().findValue(expr);
	}
	
	public Map<String, Object> getApplication() {
		return ActionContext.getContext().getApplication();
	}
	
	public Map<String, Object> getSession() {
		return ActionContext.getContext().getSession();
	}
	
	public Map<String, Object> getRequest() {
		return ActionContext.getContext().getParameters();
	}
	
	/**
	 * Dato in input un elenco di tipiLista, si verifica che non siano gi? stati inseriti in application, quindi
	 * si invoca l'apposito servizio per quelli non ancora in cache e si restituisce la mappa completa di tutti i dati
	 * richiesti
	 * 
	 * <b>Nota bene:</b> in seguito alla mail di Panepinto del 21/11/2013, la cache non viene pi? inserita in application, ma in session
	 * 
	 * @author luca.romanello
	 * @param tipiLista
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Map<TipiLista, List<? extends CodificaFin>> getCodifiche(TipiLista... tipiLista) {
		final String methodName = "getCodifiche";
		// se non passo alcun elemento, ritorno una mappa vuota
		if (tipiLista == null){
			return new HashMap<TipiLista, List<? extends CodificaFin>>(0);
		}
		Map<TipiLista, List<? extends CodificaFin>> result = new HashMap<TipiLista, List<? extends CodificaFin>>(tipiLista.length);
		//total cache:
		Map<String, Object> totalCache = getSession();
		// Sincronizzo sulla classe perche' si va a scrivere sulla mappa comune a tutte le action e a tutte le sessioni
		//  valutare se fare in modo di caricare tutte le codifiche allo startup dell'applicativo, 
		//in modo da minimizzare il tempo di accesso a questo metodo durante la normale operativit?
		synchronized (GenericFinAction.class) {
			//KEY CODIFICHE:
			String key = FinSessionParameter.CODIFICHE.name();
			// Verifico la presenza in application di una cache per l'ente collegato all'utente
			Map<TipiLista, TTLCache> fullCache = (Map<TipiLista, TTLCache>)totalCache.get(key);
			if (fullCache == null) {
				// se non e' presente, la creo
				debug(methodName, "Creo la cache di decodifiche per la chiave ", key);
				fullCache = new HashMap<TipiLista, TTLCache>(0);
				totalCache.put(key, fullCache);
			}
			// costruisco la mappa con i risultati (che sara' un sottoinsieme della cache totale)
			//e l'elenco delle liste effettivamente da chiedere a servizio
			List<TipiLista> listForService = new ArrayList<TipiLista>();
			for (TipiLista current : tipiLista) {
				// per ogni elemento passato come parametro, verifico se ? presente sulla cache
				TTLCache currentCache = fullCache.get(current);
				List<? extends CodificaFin> currentCodifica = currentCache != null ? currentCache.getList() : null;
				// verifico se devo forzare il caricamento
				if (currentCodifica != null && !current.isForceReload() && (ttlCacheCodifiche == 0 || System.currentTimeMillis() - currentCache.getBirth() < ttlCacheCodifiche)) {
					// se e' presente, la tipologia non e' marcata forceReload
					//e la cache non e' scaduta lo aggiungo alla mappa da restituire
					if (ttlCacheCodifiche != 0){
						debug(methodName, "Cache trovata per la tipologia", current, " - TTL:", (ttlCacheCodifiche - System.currentTimeMillis() + currentCache.getBirth()));
					}else{
						debug(methodName, "Cache trovata per la tipologia", current);
					}	
					result.put(current, currentCodifica);
				} else {
					// se non e' presente o la tipologia e' marcata forceReload
					//lo aggiungo ai tipi effettivamente da chiedere a servizio
					debug(methodName, "Tipologia", current, "da ricercare da servizio");
					listForService.add(current);
				}
			}
			if (listForService.size() > 0) {
				// invoco il servizio solo se necessario
				//(cioe' se ci sono dei tipilista per i quali 
				//non sono state trovate occorrenze in cache) 
				// e aggiungo i risultati sia alla mappa da restituire,
				//sia alla cache, in modo che alla prossima chiamata le peschi dalla cache
				Liste lReq = new Liste();
				lReq.setRichiedente(sessionHandler.getRichiedente());
				lReq.getRichiedente().setAccount(sessionHandler.getAccount());
				lReq.setTipi(listForService.toArray(new TipiLista[listForService.size()]));
				lReq.setBilancio(sessionHandler.getBilancio());
				ListeResponse lResp = genericService.liste(lReq);
				if (lResp.getTipi() != null && lResp.getTipi().length > 0) {
					debug(methodName, "Risposta del servizio:", lReq.getTipi().length, "tipologie caricate (richieste: ", listForService.size(), ")");
					Map<TipiLista, ArrayList<? extends CodificaFin>> resMap = new HashMap<TipiLista, ArrayList<? extends CodificaFin>>(lResp.getTipi().length);
					try {
						BeanInfo info = Introspector.getBeanInfo(lResp.getClass());
						PropertyDescriptor[] propDescs = info.getPropertyDescriptors();
						enumCycle:
						for (TipiLista current : lResp.getTipi()) {
							for (PropertyDescriptor pd : propDescs) {
								if (current.getAttr().equals(pd.getName())) {
									resMap.put(current, (ArrayList<? extends CodificaFin>)pd.getReadMethod().invoke(lResp));
									continue enumCycle;
								}
							}
						}
					} catch (Exception exc) {
						log.error(methodName, "exc --> "+exc.getStackTrace());
					}
					result.putAll(resMap);
					for (Entry<TipiLista, ArrayList<? extends CodificaFin>> currentEntry : resMap.entrySet()) {
						fullCache.put(currentEntry.getKey(), new TTLCache(currentEntry.getValue(), System.currentTimeMillis()));
					}
				} else {
					info(methodName, "Risposta del servizio: nessuna tipologia caricata");
				}
			}
			
		}
		
		return result;
	}
	
	public String convertiBooleanToString(boolean valore) {
		if(valore){
			return  WebAppConstants.Si;
		} else {
			return WebAppConstants.No;
		}
	}
	
	public String convertiBooleanToFrazionabileString(boolean valore) {
		if(valore){
			return  WebAppConstants.FRAZIONABILE;
		} else {
			return WebAppConstants.NON_FRAZIONABILE;
		}
	}
	
	protected AggiornaSoggetto convertiModelPerChiamataServizioAggiornaSoggetto(Soggetto soggetto, boolean aggiornaSoloSedi) {
		AggiornaSoggetto aggiornaSoggetto = new AggiornaSoggetto();
		aggiornaSoggetto.setRichiedente(sessionHandler.getRichiedente());
		aggiornaSoggetto.setEnte(sessionHandler.getAccount().getEnte());
		aggiornaSoggetto.setAggiornaSoloSedi(aggiornaSoloSedi);

		
		if(soggetto.getTipoSoggetto()!=null && FinStringUtils.isEmpty(soggetto.getTipoSoggetto().getCodice())){
			soggetto.getTipoSoggetto().setCodice(soggetto.getTipoSoggetto().getSoggettoTipoCode());
		}
		
		
		aggiornaSoggetto.setSoggetto(soggetto);
		return aggiornaSoggetto;
	}
	
	
	protected String convertDateToString(Date dataDaConvertire){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);
		if(null==dataDaConvertire){
			return "";
		}else{
			return sdf.format(dataDaConvertire);
		}
		
	}
	
	
	
	public String checkBoxToString(String valoreBool){
		Boolean v = Boolean.valueOf(valoreBool); 
		return Boolean.toString(v != null && v);
	}
	
	// Conversione delle cifre euro
	
	protected String convertiBigDecimalToImporto(BigDecimal importoDB) {

		String importoFormattato = null;

		DecimalFormat df = new DecimalFormat("#,###,##0.00");
		df = (DecimalFormat)NumberFormat.getNumberInstance(Locale.ITALY);
		df.setParseBigDecimal(true);
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		importoFormattato = df.format(importoDB);

		return importoFormattato;
	}

	/**
	 * Metodo di comodo per esporre convertiImportoToBigDecimal a tutte le action
	 * @param importoFormattato
	 * @return
	 */
	protected BigDecimal convertiImportoToBigDecimal(String importoFormattato) {
		return FinActionUtils.convertiImportoToBigDecimal(importoFormattato);
	}
	
	/**
	 * 
	 * 
	 */
	protected boolean presenzaPaginazione (HttpServletRequest request){
		
		Map<String, String[]> mappaReq = request.getParameterMap();
      
		boolean risultato = false;
		
	    Set<String> keySet = mappaReq.keySet();
	    for(String key:keySet){
	         if(key.startsWith("d-") && key.indexOf("-p")!=-1){
	        	 risultato = true;
	        	 break;
	         }
	    }
	    
	    return risultato;
	    
	}
	
	protected int paginazioneRichiesta(HttpServletRequest request){
		
		Map<String, String[]> mappaReq = request.getParameterMap();
      
		Integer risultato = new Integer("0");
		
		
	    Set<String> keySet = mappaReq.keySet();
	    for(String key:keySet){
	         String[] value = mappaReq.get(key);
	         
	         if(key.startsWith("d-") && key.indexOf("-p")!=-1){
	        	 
	        	 
	        	 risultato = Integer.valueOf(value[0]);
	        	 break;
	         }

	    }
	    
	    return risultato.intValue();
	    
	}
	
	protected void resetPageNumberTableId(String tableId) {
		getSession().put((new ParamEncoder(tableId).encodeParameterName(TableTagParameters.PARAMETER_PAGE)), 1);
	}
	
	protected int readPageNumberTableId(String tableId) {

		Object pageNum = getRequest().get((new ParamEncoder(tableId).encodeParameterName(TableTagParameters.PARAMETER_PAGE)));

		if (pageNum == null) {
			pageNum = getSession().get((new ParamEncoder(tableId).encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
		}
		
		if (pageNum != null) {
			return parameterPageToInt(pageNum); 
		}
				
		return 1;
	}
	
	private int parameterPageToInt(Object pageNum ) {
		return pageNum instanceof Integer ? (Integer) pageNum
				: pageNum instanceof String ? Integer.parseInt(String.valueOf(pageNum))
						: Integer.parseInt(((String[]) pageNum)[0]);
	}
	
	
	protected void addNumAndPageSize(ParametriPaginazione parametriPaginazione, String tableId) {
		parametriPaginazione.setNumeroPagina(readPageNumberTableId(tableId));
		parametriPaginazione.setElementiPerPagina(DEFAULT_PAGE_SIZE);
	}
	
	protected void addNumAndPageSize(PaginazioneRequest request, String tableId) {
		request.setNumPagina(readPageNumberTableId(tableId));
		request.setNumRisultatiPerPagina(DEFAULT_PAGE_SIZE);
	}
	
	protected void changePageNumber(ParametriPaginazione parametriPaginazione, int delta) {
		parametriPaginazione.setNumeroPagina(parametriPaginazione.getNumeroPagina()+delta);
	}
	
	protected void error(String methodName, Throwable exc, Object... parameters) {
		log.error(methodName, getLogMessage(parameters), exc);
	}
	
	protected void debug(String methodName, Object... parameters) {
		if (log.isDebugEnabled()){
			log.debug(methodName, getLogMessage(parameters));
		}	
	}
	
	protected void info(String methodName, Object... parameters) {
		if (log.isInfoEnabled()){
			log.info(methodName, getLogMessage(parameters));
		}	
	}
	
	protected void warn(String methodName, Throwable exc, Object... parameters) {
		log.warn(methodName, getLogMessage(parameters), exc);
	}
	
	protected void error(String methodName, Object... parameters) {
		log.error(methodName, getLogMessage(parameters));
	}
	
	private String getLogMessage(Object... parameters) {
		if (parameters == null || parameters.length == 0){
			return "";
		}
		StringBuilder logMessage = new StringBuilder();
		for (int i=0; i<parameters.length; i++) {
			logMessage.append(parameters[i]).append(i == parameters.length - 1 ? "" : " ");
		}
		return logMessage.toString();
	}
	
	/**
	 * Metodo che ti restituisce TRUE se la fase di bilancio e':
	 * - Esercizio Provvisorio
	 * - Gestione
	 * - Assestamento
     * - Predisposizione Consuntivo
     * 
     * LEGENDA:
     *   L : Pluriennale
	     P : Previsione
		 E : Esercizio Provvisorio
		 G : Gestione
		 A : Assestamento
		 O : Consuntivo
		 C  : Chiuso
	 * @return boolean 
	 */
	public boolean isFaseBilancioAbilitata() {
		boolean faseBilancioInStatoCorretto = false;
		if(sessionHandler.getFaseBilancio().equals("E") || sessionHandler.getFaseBilancio().equals("G") || sessionHandler.getFaseBilancio().equals("A") ||
				sessionHandler.getFaseBilancio().equals("O")){
			faseBilancioInStatoCorretto = true;
		}
		return faseBilancioInStatoCorretto;
	}
	
	/**
	 * Metodo che permette di effettuare la copia di un model, cambiando l'allocazione di memoria del model copiato
	 * @param o
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T clone(T o) {
		if(o!=null){
			return (T) SerializationUtils.deserialize(SerializationUtils.serialize(o));
		}
		return null;
	}
	
	/**
	 * Metodo che controlla se l'account dell'utente e' abilitato ad eseguire una delle funzionalita' SIAC 
	 * in base all'associazione dell'azione sulla tabella SIAC_T_AZIONE
	 * 
	 * @param codeAzione
	 * @return boolean 
	 */
	public boolean isAzioneAbilitata(String codeAzione) {
		boolean abilitato = false;
		
		sessionHandler.getAccount();
		List<AzioneConsentita> azioniConsentite=sessionHandler.getAzioniConsentite();
		
		if (azioniConsentite!=null && azioniConsentite.size()>0){
			
			try {
				for (AzioneConsentita azioneConsentita : azioniConsentite) {
					
					if (azioneConsentita.getAzione().getNome().equals(codeAzione)) {
						//OK ABILITATO
						abilitato=true;
						break;
					}
				}
			} catch (Exception e) {
				log.error("isAzioneAbilitata", e.getMessage());
			}
		}
		
		return abilitato;
	}
	
	/**
	 * Metoto di comodo per i controlli che usano questa logica:
	 * 
	 * 1. aggiungo gli eventuali errori in sessione
	 * 
	 * 2. ritorno true se tutto ok (non ci sono errori)
	 *    ritorno false se ci sono errori (almeno uno)
	 * 
	 * 
	 * @param listaErrori
	 * @return
	 */
	protected boolean checkAndAddErrors(List<Errore> listaErrori){
		boolean tuttoOk = true;
		if(!isEmpty(listaErrori)){
			addErrori(listaErrori);
			tuttoOk = false;
		}
		return tuttoOk;
	}
	
	public String getActionKey() {
		return null;
	}
	
	public String getActionDataKeys() {
		return "model";
	}
	
	public String getExcludedMethods() {
		return null;
	}
	
	public void setModel(M model) {
		this.model = model;
	}
	
	protected void saveActionData() {
		this.actionDataMode = ActionDataEnum.SAVE;
	}
	
	protected void ignoreActionData() {
		this.actionDataMode = ActionDataEnum.IGNORE;
	}
	
	protected void clearActionData() {
		this.actionDataMode = ActionDataEnum.CLEAR;
	}
	
	public ActionDataEnum getActionDataMode() {
		return actionDataMode;
	}
	
	public boolean notExistsPreviousCrumb() {
		return getPreviousCrumb() == null;
	}
	
	public void addPersistentActionMessage(String message) {
		addActionMessage(message);
		persistentActionMessages = true;
	}
	
	public void addPersistentActionError(String error) {
		addActionError(error);
		persistentActionErrors = true;
	}
	
	public boolean hasPersistentActionMessages() {
		return persistentActionMessages;
	}
	
	public boolean hasPersistentActionErrors() {
		return persistentActionErrors;
	}

	public void setForceReload(boolean forceReload) {
		this.forceReload = forceReload;
	}
	
	public String appendForceReload(String url) {
		StringBuilder result = new StringBuilder(url);
		if (url != null) {
			char c = '?';
			if (url.indexOf(c) >= 0){
				c = '&';
			}
			result.append(c).append("forceReload=true");
		}
		return result.toString();
	}

	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public boolean isForceReload() {
		return forceReload;
	}

	public String getAmbito() {
		return ambito;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}
	
	
	public void setApplicationContext(ApplicationContext ctx) {
		this.ctx = ctx;
	}
	
	public ApplicationContext getApplicationContext() {
		return this.ctx;
	}

	public Collection<String> getActionWarnings() {
		return actionWarnings;
	}
	
	public void setActionWarnings(Collection<String> actionWarnings) {
		this.actionWarnings = actionWarnings;
	}

	public boolean hasActionWarnings() {
		return this.actionWarnings.size() > 0;
	}
	
	public void addActionWarningFin(ErroreFin warning) {
		this.actionWarnings.add(warning.getCodice() + " - " + warning.getMessaggio());
	}
	
	public void addActionWarnings(List<Errore> warnings) {
		if(!isEmpty(warnings)){
			for(Errore warningIt : warnings){
				if(warningIt!=null){
					addActionWarning(warningIt);
				}
			}
		}
	}
	
	protected void addWarning(Errore wrng){
	  model.addWarning(wrng);
	  addActionWarning(wrng);
	}
	
	public void addActionWarning(Errore warning) {
		this.actionWarnings.add(warning.getCodice() + " - " + warning.getDescrizione());
	}
	
	public void addActionWarning(String warning) {
		this.actionWarnings.add(warning);
	}
	
	public void addPersistentActionWarningFin(ErroreFin warning) {
		addActionWarning(warning.getCodice() + " - " + warning.getMessaggio());
		persistentActionWarnings = true;
	}
	
	public void addPersistentActionWarning(String warning) {
		addActionWarning(warning);
		persistentActionWarnings = true;
	}
	
	public boolean hasPersistentActionWarnings() {
		return persistentActionWarnings;
	}
	
	public String getAzioniToCheck() {
		return azioniToCheck;
	}

	public void setAzioniToCheck(String azioniToCheck) {
		this.azioniToCheck = azioniToCheck;
	}

	public String getAzioniDecToCheck() {
		return azioniDecToCheck;
	}

	public void setAzioniDecToCheck(String azioniDecToCheck) {
		this.azioniDecToCheck = azioniDecToCheck;
	}
	
	protected boolean checkAzioni(String csAzioni) {
		boolean result = false;
		if (csAzioni != null && csAzioni.length() > 0) {
			String[] azioni = csAzioni.split(",");
			for (String current : azioni) {
				if (isAzioneAbilitata(current.trim())) {
					result = true;
					break;
				}
			}
		} else {
			result = true;
		}
		return result;
	}

	/**
	 * verifica la presenza di abilitazione per le azioni indicate come prerequisiti dell'azione corrente
	 * 
	 * @return true se l'utente e' abilitato ad almeno una delle azioni indicate o se non sono indicate azioni, false altrimenti
	 */
	public boolean checkAzioni() {
		return checkAzioni(azioniToCheck);
	}
	
	/**
	 * verifica la presenza di abilitazione per le azioni da utente decentrato indicate come prerequisiti dell'azione corrente
	 * 
	 * @return true se l'utente e' abilitato ad almeno una delle azioni da utente decentrato indicate o se non sono indicate azioni, false altrimenti
	 */
	public boolean checkAzioniDec() {
		return checkAzioni(azioniDecToCheck);
	}
	
	/**
	 * verifica la presenza di abilitazione per le azioni indicate come prerequisiti dell'azione corrente
	 * <br/>
	 * Nell'implementazione di default e' un wrapper di checkAzioni e checkAzioniDec (somma logica);
	 * si consiglia di ridefinire questo e gli altri due metodi per aggiungere altri controlli preventivi
	 * 
	 * @return true se l'utente e' abilitato ad almeno una delle azioni indicate o se non sono indicate azioni, false altrimenti
	 */
	public boolean checkAllAzioni() {
		return checkAzioni() || checkAzioniDec();
	}


	public boolean isSonoInAggiornamentoOrdinativo() {
		return sonoInAggiornamentoOrdinativo;
	}


	public void setSonoInAggiornamentoOrdinativo(
			boolean sonoInAggiornamentoOrdinativo) {
		this.sonoInAggiornamentoOrdinativo = sonoInAggiornamentoOrdinativo;
	}
	
	protected boolean isAzioneDecentrata(String azione) {
		return azioneDecConsentitaIsPresentObj(azione) != null;
	}
	
	
	protected boolean isDecentrato() {
		return isAzioneDecentrata(sessionHandler.getAzione().getNome());
	}
	
	/*
	 *  verifico che sia decentrato
	 *  all'interno c'e' il meccanismo per cui se entri come lettore
	 *  devi verificare di possedere all'interno delle azioni 
	 *  consentite il dec, se si' allora passo le informazioni
	 */
	protected AzioneConsentita azioneDecConsentitaIsPresentObj(String azione) {
		List<AzioneConsentita> azioniConsentite = sessionHandler.getAzioniConsentite();
		AzioneConsentita result = null;
		List<String> decRich = getMapCorrispondenzeDec().get(azione);
		if ((decRich == null || decRich.size() == 0) && CodiciOperazioni.AZIONI_DECENTRATO.contains(azione)) {
			decRich = new ArrayList<String>(0);
			decRich.add(azione);
		}
		if (decRich != null){
			for (AzioneConsentita a : azioniConsentite) {
				if (decRich.contains(a.getAzione().getNome())) {
					result = a;
					break;
				}
			}
		}	
		return result;
	}
	
	/**
	 * Semplice metodo che si limita a verificare se l'azione indicata e' presente tra quelle dell'utente loggato
	 * @param azione
	 * @return
	 */
	protected boolean isConsentita(String azione)
	{
		if (azione != null)
			for (AzioneConsentita a : sessionHandler.getAzioniConsentite())
				if (azione.equalsIgnoreCase(a.getAzione().getNome()))
					return true;
			
		return false;
	}
	
	public Map<String, List<String>> getMapCorrispondenzeDec() {
		return CodiciOperazioni.MAP_CORRISPONDENZE_DEC;
	}
	
	/**
	 * dato un certo codice utente va a verificare se corrisponde allo stesso utente con cui ci si e' loggati
	 * @param utente
	 * @return
	 */
	protected boolean isUtenteLoggato(String utente) {
		boolean isUtetenLoggato = false;
		if (!FinStringUtils.isEmpty(utente)) {
			if (sessionHandler.getAccount() != null) {
				//istanzio la request per il servizio ricercaAccountPerChiave:
				RicercaAccountPerChiave request = new RicercaAccountPerChiave();
				request.setAccountId(sessionHandler.getAccount().getId());
				request.setEnte(sessionHandler.getEnte());
				request.setRichiedente(sessionHandler.getRichiedente());
				//invoco il servizio ricercaAccountPerChiave:
				RicercaAccountPerChiaveResponse response = genericService.ricercaAccountPerChiave(request);
				
				String accountCode = response.getAccountCode();
				
				if (!FinStringUtils.isEmpty(accountCode)) {
					//UGUALI
					boolean uguali = FinStringUtils.sonoUgualiTrimmed(accountCode, utente);
					isUtetenLoggato = uguali;
				}
			}
		}
		return isUtetenLoggato;
	}
	
	
	
	protected boolean valorizzaCheckbox(String nomeParametro){
		
		HttpServletRequest request = ServletActionContext.getRequest();
		boolean risultato = false;
		if(null==request.getParameter(nomeParametro)){
			risultato = false;
		}else if("true".equalsIgnoreCase(request.getParameter(nomeParametro))){
			
			     risultato = true;
		}else if("false".equalsIgnoreCase(request.getParameter(nomeParametro))){
			
		     risultato = false;
		}
		
		return risultato;
		
	}
	
	
	protected <O extends Object> Map<String, O> getCacheTyped(String keyCacheType){
		Map<String, Object> totalCache = getSession();
		@SuppressWarnings("unchecked")
		Map<String, O> fullCache = (Map<String, O>)totalCache.get(keyCacheType);
		if (fullCache == null) {
			// se non e' presente, la creo
			fullCache = new HashMap<String, O>(0);
			totalCache.put(keyCacheType, fullCache);
		}
		return fullCache;
	}
	
	/**
	 * Ritorna null se l'oggetto richiesto NON e' in cache oppure e' in cache ma e' scaduto il timeout
	 * @param fullCache
	 * @param requestSiopeKey
	 * @return
	 */
	protected <O extends Object> Serializable getOggettoInCache(Map<String, ServiceCache> fullCache,String requestSiopeKey){
		Serializable resp = null;
		boolean load = false;
		if(fullCache.containsKey(requestSiopeKey)){
			ServiceCache currentCache = fullCache.get(requestSiopeKey);
			if(currentCache!=null){
				boolean scaduta = isCacheScaduta(currentCache);
				if(!scaduta){
					//presente e non scaduta
					resp = currentCache.getValue();
				} else {
					//presente ma scaduta
					load = true;
				}
			} else {
				//per qualche motivo e' null
				load = true;
			}
		} else {
			//Non e' in cache va invocato il servizio
			load = true;
		}
		if(load){
			resp = null;
		}
		return resp;
	}
	
	
	protected boolean isCacheScaduta(Serializable currentCache){
		
		if(ttlCacheCodifiche == 0 || System.currentTimeMillis() - ((ServiceCache)currentCache).getBirth() < ttlCacheCodifiche){
			return false;//non e' scaduta
		} else {
			return true;//scaduta
		}
	}
	
	protected LeggiStrutturaAmminstrativoContabile buildStrutturaAmminstrativoContabileRequest(){
		LeggiStrutturaAmminstrativoContabile lsa = new LeggiStrutturaAmminstrativoContabile();
		lsa.setAnno(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		lsa.setIdEnteProprietario(sessionHandler.getEnte().getUid());
		lsa.setRichiedente(sessionHandler.getRichiedente());
		return lsa;
	}
	
	protected String buildStrutturaAmminstrativoContabileRequestKey(LeggiStrutturaAmminstrativoContabile request){
		String requestKey = buildBaseRequestKey();
		return requestKey;
	}
	
	/**
	 * utlizzato per generare chiavi univoche da mettere in cache
	 * @return
	 */
	protected String buildBaseRequestKey(){
		String requestKey = "";
		requestKey = requestKey +  sessionHandler.getRichiedente().getAccount().getUid();
		requestKey = requestKey + sessionHandler.getAnnoEsercizio();
		requestKey = requestKey + sessionHandler.getEnte().getUid();
		return requestKey;
	}
	
	
	@SuppressWarnings("rawtypes")
	protected LeggiStrutturaAmminstrativoContabileResponse getStrutturaAmministrativaCached(LeggiStrutturaAmminstrativoContabile request){
		// se non passo alcun elemento, ritorno una mappa vuota
		LeggiStrutturaAmminstrativoContabileResponse result = null;
		String key = "__STRUTTURA_AMMINISTRATIVA_KEY";
		String requestKey = buildStrutturaAmminstrativoContabileRequestKey(request);
		// Carico la cache per tipologia:
		Map<String, ServiceCache> fullCache = getCacheTyped(key);
		//pesco l'oggetto specifico dalla cache specifica:
		LeggiStrutturaAmminstrativoContabileResponse lsac = new LeggiStrutturaAmminstrativoContabileResponse();
		lsac.setListaStrutturaAmmContabile((List<StrutturaAmministrativoContabile>) getOggettoInCache(fullCache, requestKey));
		result = lsac;
		if(result.getListaStrutturaAmmContabile()==null){
			//se l'oggetto e' nullo devo invocare il servizio opportuno
			result = classificatoreService.leggiStrutturaAmminstrativoContabile(request);
			ServiceCache<Serializable> currentCache = new ServiceCache<Serializable>((Serializable)result.getListaStrutturaAmmContabile(), System.currentTimeMillis());
			fullCache.put(requestKey, currentCache);
		}
		return result;
	}
	
	protected String getAlberoStruttureAmministrativeContabiliJson(boolean isDecentrato) throws IOException, JSONException {
		
		
		List<GestoreDatiAlberoModel> alberoStruttureAmministrativeContabili = getAlberoStruttureAmministrativeContabili(isDecentrato);
		
		Collections.sort(alberoStruttureAmministrativeContabili, new Comparator<GestoreDatiAlberoModel>() {

			@Override
			public int compare(GestoreDatiAlberoModel o1, GestoreDatiAlberoModel o2) {
				return o1.getCodice().compareTo(o2.getCodice());
			}
		});
		
		return JSONUtil.serialize(alberoStruttureAmministrativeContabili);
	}
	
	
	protected List<GestoreDatiAlberoModel> getAlberoStruttureAmministrativeContabili(boolean isDecentrato) {
		LeggiStrutturaAmminstrativoContabile lsa = buildStrutturaAmminstrativoContabileRequest();
		LeggiStrutturaAmminstrativoContabileResponse responseLsa = getStrutturaAmministrativaCached(lsa);
		
		return creaAlberoStrutturaAmministrativa(responseLsa.getListaStrutturaAmmContabile(), isDecentrato);
	}
	
	
	/**
	 * giro ricorsivo per generalre l'albero della struttura
	 * @param listaStrutture
	 * @param isDecentrato
	 */
	protected ArrayList<GestoreDatiAlberoModel> creaAlberoStrutturaAmministrativa(List<StrutturaAmministrativoContabile> listaStrutture, boolean isDecentrato) {
		this.listaStruttureAmministrativeTmpSupport = new ArrayList<GestoreDatiAlberoModel>();
		if (listaStrutture != null && listaStrutture.size() > 0) {
			for (StrutturaAmministrativoContabile currentStrutturaAmministrativa : listaStrutture) {
				popolaStrutturaAmministrativa(currentStrutturaAmministrativa, RADICE_ALBERO, null, isDecentrato);
				if (currentStrutturaAmministrativa.getSubStrutture() != null && currentStrutturaAmministrativa.getSubStrutture().size() > 0) {
					ricorsivaGestioneStrutturaAmministrativa(currentStrutturaAmministrativa.getSubStrutture(), currentStrutturaAmministrativa.getCodice(), currentStrutturaAmministrativa.getUid(), isDecentrato);
				}
			}
		}
		return this.listaStruttureAmministrativeTmpSupport;
	}
	
	private void ricorsivaGestioneStrutturaAmministrativa(List<StrutturaAmministrativoContabile> struttureAmministrativeFiglie, String idPadre, Integer uidPadre, boolean isDecentrato) {
		for (StrutturaAmministrativoContabile currentStrutturaAmministrativa : struttureAmministrativeFiglie) {
			popolaStrutturaAmministrativa(currentStrutturaAmministrativa, idPadre, uidPadre, isDecentrato);
			if (currentStrutturaAmministrativa.getSubStrutture() != null && currentStrutturaAmministrativa.getSubStrutture().size() > 0) {
				// SIAC-6627: aggiunto codice per gestione ricorsione
				ricorsivaGestioneStrutturaAmministrativa(currentStrutturaAmministrativa.getSubStrutture(), idPadre + currentStrutturaAmministrativa.getCodice(), currentStrutturaAmministrativa.getUid(), isDecentrato);
			}
		}
	}
	
	private void popolaStrutturaAmministrativa(StrutturaAmministrativoContabile currentStrutturaAmministrativa, String idPadre, Integer uidPadre, boolean isDecentrato) {
		GestoreDatiAlberoModel supportGestoreStrutturaAmministrativa = new GestoreDatiAlberoModel();
		if (idPadre != null && !RADICE_ALBERO.equalsIgnoreCase(idPadre)) {
			supportGestoreStrutturaAmministrativa.setId(idPadre + currentStrutturaAmministrativa.getCodice());
			supportGestoreStrutturaAmministrativa.setpId(idPadre);
		} else {
			supportGestoreStrutturaAmministrativa.setId(currentStrutturaAmministrativa.getCodice());
			supportGestoreStrutturaAmministrativa.setpId(RADICE_ALBERO);
		}
		supportGestoreStrutturaAmministrativa.setUid(currentStrutturaAmministrativa.getUid());
		supportGestoreStrutturaAmministrativa.setCodice(currentStrutturaAmministrativa.getCodice());
		if (currentStrutturaAmministrativa.getTipoClassificatore() != null) {
			supportGestoreStrutturaAmministrativa.setTipo(currentStrutturaAmministrativa.getTipoClassificatore().getCodice());
		} 
		if (isDecentrato) {
			String supportName = (supportGestoreStrutturaAmministrativa.getTipo() != null && !"".equalsIgnoreCase(supportGestoreStrutturaAmministrativa.getTipo())) ? supportGestoreStrutturaAmministrativa.getTipo() + " - " : ""; 
			supportGestoreStrutturaAmministrativa.setName(currentStrutturaAmministrativa.getCodice()+" - "+supportName + currentStrutturaAmministrativa.getDescrizione()); 
		} else {
			supportGestoreStrutturaAmministrativa.setName(currentStrutturaAmministrativa.getCodice()+" - "+currentStrutturaAmministrativa.getDescrizione());
		}
		// SIAC-6083
		supportGestoreStrutturaAmministrativa.setUidPadre(uidPadre);
		this.listaStruttureAmministrativeTmpSupport.add(supportGestoreStrutturaAmministrativa);
	}


	public boolean isBottoneIndietroDiTipoSecondBtn() {
		return bottoneIndietroDiTipoSecondBtn;
	}

	public void setBottoneIndietroDiTipoSecondBtn(
			boolean bottoneIndietroDiTipoSecondBtn) {
		this.bottoneIndietroDiTipoSecondBtn = bottoneIndietroDiTipoSecondBtn;
	}
	
	/**
	 * Analizza l'esito della request e setta gli eventuali errori o warning
	 * @param response
	 * @param methodName
	 * @return
	 */
	protected <SR extends ServiceResponse> boolean analizzaEsitoServizio(SR response,String methodName){
		return analizzaEsitoServizio(response, methodName, false, false);
	}
	
	protected <SR extends ServiceResponse> boolean analizzaEsitoServizio(SR response,String methodName, boolean persistentError, boolean persistentWarning){
		boolean esito = true;
		if(response.isFallimento()){
			esito = false;
			error(methodName, "Errore nella risposta del servizio");
			if(!persistentError){
				addErrori(methodName, response);
			} else {
				addPersistentActionError(response.getErrori().get(0).getCodice()+" "+response.getErrori().get(0).getDescrizione());
			}
		} else if((response.getErrori() != null && response.getErrori().size() > 0)){
			List<Errore> erroriWarning=response.getErrori();
			for (Errore erroreWarning : erroriWarning) {
				if(!persistentWarning){
					addActionWarning(erroreWarning.getCodice()+" "+erroreWarning.getDescrizione());
				} else {
					addPersistentActionWarning(erroreWarning.getCodice()+" "+erroreWarning.getDescrizione());
				}
			}
		}
		return esito;
	}

	/**
	 * Metodo di comodo da usare nelle jsp tramite una action qualsiasi...
	 * @param numero
	 * @return
	 */
	public boolean isMaggioreDiZero(Integer numero){
		if(numero!=null && numero.intValue()>0){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * utility per risparmiare codice quando si devono inizializzare le solite liste si-no per i radio button di scelta
	 * @return
	 */
	public List<String> buildListaSiNo(){
		List<String> listaSiNo = new ArrayList<String>();
		listaSiNo.add(WebAppConstants.Si);
		listaSiNo.add(WebAppConstants.No);
		return listaSiNo;
	}
	
	public List<String> buildListaFrazionabile(){
		List<String> lista = new ArrayList<String>();
		lista.add(WebAppConstants.FRAZIONABILE);
		lista.add(WebAppConstants.NON_FRAZIONABILE);
		return lista;
	}
	
	/**
	 * Labels Commerciale - Non commerciale
	 * @return
	 */
	public List<String> buildListaTipoDebitoSiope(){
		List<String> lista = new ArrayList<String>();
		lista.add(WebAppConstants.COMMERCIALE);
		lista.add(WebAppConstants.NON_COMMERCIALE);
		return lista;
	}
	
	/**
	 *  SIAC-5524 : la label Commerciale per gli impegni deve diventare:
	 *  Commerciale (con fatture)
	 * @return
	 */
	public List<String> buildListaTipoDebitoSiopePerImpegni(){
		List<String> lista = new ArrayList<String>();
		lista.add(WebAppConstants.COMMERCIALE_CON_FATTURE);
		lista.add(WebAppConstants.NON_COMMERCIALE);
		return lista;
	}
	
	/**
	 * utility per gestire il fatto che la label commerciale, nel caso degli impegni,
	 * dalla SIAC-5524 e' diventata "Commerciale (con fatture)" 
	 * @param tipoDebitoSiope
	 * @return
	 */
	protected boolean isTipoCommerciale(String tipoDebitoSiope){
	    return WebAppConstants.COMMERCIALE.equalsIgnoreCase(tipoDebitoSiope) || WebAppConstants.COMMERCIALE_CON_FATTURE.equalsIgnoreCase(tipoDebitoSiope);
	}
	
	/**
	 * utility per gestire velocemente i radio si no
	 * @param siNo
	 * @return
	 */
	public boolean webAppSiNoToBool(String siNo){
		boolean booleanValue = false;
		if(siNo!=null & siNo.equals(WebAppConstants.Si)){
			booleanValue = true;
		}
		return booleanValue;
	}
	
	public String setNoIfNull(String variabileSiNo){
		if(variabileSiNo == null){
			variabileSiNo = WebAppConstants.No;
		}
		return variabileSiNo;
	}
	
	public String setFrazionabileIfNull(String variabileFrazNonFraz){
		if(variabileFrazNonFraz == null){
			variabileFrazNonFraz = WebAppConstants.FRAZIONABILE;
		}
		return variabileFrazNonFraz;
	}
	
	/**
	 * Converte un boolean in SI NO (MAIUSCOLI)
	 * @param siNo
	 * @return
	 */
	public String booleanToWebAppSINO(boolean booleanValue){
		String stringValue = "";
		if(booleanValue){
			stringValue = WebAppConstants.MSG_SI;
		} else {
			stringValue = WebAppConstants.MSG_NO;
		}
		return stringValue;
	}
	
	/**
	 * Se non e' presente ritorna null
	 * @param tipoCode
	 * @return
	 */
	public String getCodiceLivelloByTipo(TipologiaGestioneLivelli tipoCode){
		String trovato = null;
		if(tipoCode!=null && sessionHandler!=null && sessionHandler.getEnte()!=null){
			Map<TipologiaGestioneLivelli, String> livelli = sessionHandler.getEnte().getGestioneLivelli();
			if(livelli!=null && livelli.size()>0){
				Iterator<Map.Entry<TipologiaGestioneLivelli, String>> it = livelli.entrySet().iterator();
			    while (it.hasNext()) {
			    	Map.Entry<TipologiaGestioneLivelli, String> pair = it.next();
			        if(tipoCode.equals(pair.getKey())){
			        	trovato = pair.getValue();
			        	break;
			        }
			    }
			}
		}
		return trovato;
	}
	
	/**
	 * Se non e' presente ritorna null
	 * @param tipoCode
	 * @return
	 */
	public String getCodiceTipo(TipologiaGestioneLivelli tipoCode){
		String trovato = null;
		if(tipoCode!=null && sessionHandler!=null && sessionHandler.getEnte()!=null){
			Map<TipologiaGestioneLivelli, String> livelli = sessionHandler.getEnte().getGestioneLivelli();
			if(livelli!=null && livelli.size()>0){
				Iterator<Map.Entry<TipologiaGestioneLivelli, String>>  it = livelli.entrySet().iterator();
			    while (it.hasNext()) {
			    	Map.Entry<TipologiaGestioneLivelli, String> pair = it.next();
			        if(tipoCode.equals(pair.getKey())){
			        	TipologiaGestioneLivelli match = pair.getKey();
			        	trovato = match.getCodice();
			        	break;
			        }
			    }
			}
		}
		return trovato;
	}
	
	
	public boolean sonoLoStessoCapitolo(CapitoloUscitaGestione capitoloUscitaGestione,CapitoloImpegnoModel capitoloImpegnoModel){
		boolean stessoCapitolo = true;
		
		if(capitoloUscitaGestione==null || capitoloImpegnoModel == null){
			return false;
		}
		
		if(!FinStringUtils.sonoUgualiInt(capitoloUscitaGestione.getAnnoCapitolo(), capitoloImpegnoModel.getAnno())){
			return false;
		}
		
		if(!FinStringUtils.sonoUgualiInt(capitoloUscitaGestione.getNumeroCapitolo(), capitoloImpegnoModel.getNumCapitolo())){
			return false;
		}
		
		if(!FinStringUtils.sonoUgualiInt(capitoloUscitaGestione.getNumeroArticolo(), capitoloImpegnoModel.getArticolo())){
			return false;
		}
		
		Integer uebConfronto = (Integer) FinStringUtils.getZeroIfNull(capitoloImpegnoModel.getUeb());
	
		if(!FinStringUtils.sonoUgualiInt(capitoloUscitaGestione.getNumeroUEB(), uebConfronto)){
			return false;
		}
		
		return stessoCapitolo;
	}
	
	/**
	 * Metodo di comodo per esporre velocemente in tutte le action il metodo to list
	 * @param oggetto
	 * @return
	 */
	protected <T extends Object> List<T> toList(T... oggetto) {
		return FinUtility.toList(oggetto);
	}

	/**
	 * Metodo di comodo per esporre velocemente in tutte le action il metodo to list di piu liste
	 * @param liste
	 * @return
	 */
	protected <T extends Object> List<T> toList(List<T>... liste) {
		return FinUtility.toList(liste);
	}
	
	/**
	 * Metodo di comodo per esporre velocemente in tutte le action il check stringa vuota
	 * @param s
	 * @return
	 */
	protected boolean isEmpty(String s){
		return FinStringUtils.isEmpty(s);
	}
	
	/**
	 * Metodo di comodo per esporre velocemente in tutte le action il check lista vuota
	 * @param list
	 * @return
	 */
	protected <OBJ extends Object> boolean isEmpty(List<OBJ> list){
		return FinUtility.isEmpty(list);
	}
	
	/**
	 * Metodo di comodo per esporre velocemente in tutte le action il controllo su numero nullo o uguale a zero
	 * @param n
	 * @return
	 */
	protected boolean isNullOrZero(Number n){
		return FinUtility.isNullOrZero(n);
	}
	
	/**
	 * Metodo di comodo per esporre velocemente in tutte le action equalsIgnoreCaseSuAlmenoUno.
	 * 
	 * Ritorna true se la stringa s e' uguale ad almeno
	 * un elemento in list rispetto al confronto di tipo equalsIgnoreCase.
	 * 
	 * @param s
	 * @param list
	 * @return
	 */
	protected boolean equalsIgnoreCaseSuAlmenoUno(String s, String ... list){
		return FinStringUtils.equalsIgnoreCaseSuAlmenoUno(s, list);
	}
	
	/**
	 * Metodo di comodo per esporre velocemente il confronto di uguaglianza tra numerici
	 * che sappiamo essere di tipo intero.
	 * 
	 * Ritorna true se entrambi diversi da null e' con lo stesso intValue
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	protected boolean sonoUgualiInt(Number a, Number b){
		return FinStringUtils.sonoUgualiInt(a, b);
	}
	
	/**
	 * 
	 *  Metodo di comodo per esporre velocemente il confronto di uguaglianza tra numerici
	 * che sappiamo essere di tipo intero, uno dei quali salvato in una stringa.
	 * 
	 * Ritorna true se entrambi diversi da null e' con lo stesso intValue
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	protected boolean sonoUgualiInt(Number a, String b){
		return FinStringUtils.sonoUgualiInt(a, b);
	}
	
	/**
	 *  Metodo di comodo per esporre velocemente a tutte le action il metodo addAllConNew
	 * @param listaTo
	 * @param listaFrom
	 * @return
	 */
	protected <T extends Object> List<T> addAllConNew(List<T> listaTo,List<T> listaFrom){
		return FinUtility.addAllConNew(listaTo, listaFrom);
	}
	
	/**
	 * controlla solo se ci sono errori
	 * @param response
	 * @return
	 */
	protected boolean isFallimento(ServiceResponse response){
		if(response==null){
			//se nulla la considero fallimentare di default...
			return true;
		}
		if(response.isFallimento() || !isEmpty(response.getErrori())){
			return true;
		} else {
			return false;
		}
	}
	
	protected boolean isWarning(ServiceResponse response){
		if(response==null){
			//se nulla non e' un warning
			return false;
		}
		if(!response.isFallimento() && (response.getErrori() != null && response.getErrori().size() > 0)){
			//IL WARNING E' CARATTERIZZATO DA ESITO = SUCCESSO e presenza di elementi
			//in lista errori (che in questo caso sono dei warnings)
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * per verificare velocemente se la fase del bilancio precedente e' in predisposizione consuntivo
	 * @return
	 */
	protected boolean isBilancioPrecedenteInPredisposizioneConsuntivo(){
		String faseBilancioPrecedente = sessionHandler.getFaseBilancioPrecedente();
		return isPredisposizioneConsuntivo(faseBilancioPrecedente);
	}
	
	/**
	 * per verificare velocemente se la fase del bilancio attuale e' in predisposizione consuntivo
	 * @return
	 */
	protected boolean isBilancioAttualeInPredisposizioneConsuntivo(){
		String faseBilancio= sessionHandler.getFaseBilancio();
		return isPredisposizioneConsuntivo(faseBilancio);
	}
	
	/**
	 * per verificare velocemente se la fase del bilancio attuale e' in predisposizione consuntivo
	 * @return
	 */
	protected boolean isBilancioAttualeInEsercizioProvvisorio(){
		String faseBilancio= sessionHandler.getFaseBilancio();
		return isEsercizioProvvisorio(faseBilancio);
	}
	
	/**
	 * Dato un codice di fase bilanci verifica che corrisponda a predisposizione consuntivo
	 * @param faseBilancio
	 * @return
	 */
	protected boolean isPredisposizioneConsuntivo(String faseBilancio){
		if("O".equals(faseBilancio)){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Dato un codice di fase bilanci verifica che corrisponda a esercizio provvisorio
	 * @param faseBilancio
	 * @return
	 */
	protected boolean isEsercizioProvvisorio(String faseBilancio){
		if("E".equals(faseBilancio)){
			return true;
		} else {
			return false;
		}
	}
	
	
	
	
	/**
	 * setta nel model le variabili di comodo che verranno usate per capire se il bilancio all'anno precedente o quello attuale sono in predisposione consuntivo
	 */
	protected void settaBilancioInPredispConsuntivoNelModel(){
		//PRECEDENTE:
		boolean bilancioPrecedenteInPredisposizioneConsuntivo = isBilancioPrecedenteInPredisposizioneConsuntivo();
		this.model.setBilancioPrecedenteInPredisposizioneConsuntivo(bilancioPrecedenteInPredisposizioneConsuntivo);
		//ATTUALE:
		boolean bilancioAttualeInPredisposizioneConsuntivo = isBilancioAttualeInPredisposizioneConsuntivo();
		this.model.setBilancioAttualeInPredisposizioneConsuntivo(bilancioAttualeInPredisposizioneConsuntivo);
	}
	
	public boolean abilitatoAzioneInserimentoProvvedimento() {
		boolean abilitato = false;
		if(isAzioneAbilitata(CodiciOperazioni.OP_ATTGESC001_inserisciProvvedimento)){
			abilitato = true;
		}
		return abilitato;
	}
	
	public boolean abilitatoAzioneGestisciImpegnoSDF() {
		boolean abilitato = false;
		if(isAzioneAbilitata(CodiciOperazioni.OP_SPE_gestisciImpegnoSDF)){
			abilitato = true;
		}
		return abilitato;
	}
	
	public boolean abilitatoAzioneGestisciImpegnoDecentratP() {
		boolean abilitato = false;
		if(isAzioneAbilitata(CodiciOperazioni.OP_SPE_gestisciImpegnoDecentratoP)){
			abilitato = true;
		}
		return abilitato;
	}

	/**
	 * Rimuove dalla "cache" le liste identificate dalle chiavi <code>tipiLista</code>.
	 * 
	 * @param tipiLista
	 */
	@SuppressWarnings("unchecked")
	protected void removeFromCache(TipiLista... tipiLista) {
		// se non passo alcun elemento, ritorno una mappa vuota
		if (tipiLista == null || tipiLista.length==0){
			return ;
		}
		Map<String, Object> totalCache = getSession();
		synchronized (GenericFinAction.class) {
			String key = FinSessionParameter.CODIFICHE.name();
			// Verifico la presenza in application di una cache per l'ente collegato all'utente
			Map<TipiLista, TTLCache> fullCache = (Map<TipiLista, TTLCache>)totalCache.get(key);
			if (fullCache == null) {
				// se non e inizializzata, nothing to do
				return;
			}
			
			for (TipiLista current : tipiLista) {
				
				TTLCache currentCache = fullCache.get(current);
				
				if(currentCache==null){
					//non e' in cache, nothing to do
				} else {
					fullCache.remove(current);
				}
			}
			
		}
	}
	
	
	protected Map<Integer, StrutturaAmministrativoContabile> getElencoStruttureAmministrativoContabiliUtente()
	{
		if (sessionHandler.getParametro(FinSessionParameter.ELENCO_STRUTTURE_AMMINISTRATIVE_CONTABILI_UTENTE) == null)
			sessionHandler.setParametro(FinSessionParameter.ELENCO_STRUTTURE_AMMINISTRATIVE_CONTABILI_UTENTE,
					readElencoStruttureAmministrativoContabiliUtente());

		return sessionHandler.getParametro(FinSessionParameter.ELENCO_STRUTTURE_AMMINISTRATIVE_CONTABILI_UTENTE);
	}
	
	private Map<Integer, StrutturaAmministrativoContabile> readElencoStruttureAmministrativoContabiliUtente()
	{
		Map<Integer, StrutturaAmministrativoContabile> map = new LinkedHashMap<Integer, StrutturaAmministrativoContabile>();
		List<StrutturaAmministrativoContabile> list = getStruttureAmministrativoContabileByAccount();
		for(StrutturaAmministrativoContabile sac : list) {
			map.put(sac.getUid(), sac);
		}
		return map;
	}
	
	/**
	 * sceltaTipoDebitoSiope valorizzato con il valore del model
	 * che rappresenza la scelta del radio button relativo al tipo debito siope
	 * 
	 * metodo centralizzato per impegni, liquidazioni e ordinativi
	 * 
	 * @param sceltaTipoDebitoSiope
	 * @return
	 */
	protected SiopeTipoDebito siopeTipoDebitoDaSceltaNelModel(String sceltaTipoDebitoSiope){
		SiopeTipoDebito siopeTipoDebito = new SiopeTipoDebito();
		if(sceltaTipoDebitoSiope!=null){
			if(isTipoCommerciale(sceltaTipoDebitoSiope)){
				siopeTipoDebito.setDescrizione(WebAppConstants.COMMERCIALE);
				siopeTipoDebito.setCodice(Constanti.SIOPE_CODE_COMMERCIALE);
			}else{
				siopeTipoDebito.setDescrizione(WebAppConstants.NON_COMMERCIALE);
				siopeTipoDebito.setCodice(Constanti.SIOPE_CODE_NON_COMMERCIALE);
			}
		}
		return siopeTipoDebito;
	}
	
	/**
	 * motivazioneAssenzaCigSelezionata valorizzato con il valore del model
	 * che rappresenza la scelta nella lista di motivazioni assenza cig, puo' non esserci
	 * 
	 * metodo centralizzato per impegni, liquidazioni e ordinativi
	 * 
	 * @param motivazioneAssenzaCigSelezionata
	 * @return
	 */
	protected SiopeAssenzaMotivazione siopeAssenzaMotivazioneDaSceltaNelModel(String motivazioneAssenzaCigSelezionata){
		SiopeAssenzaMotivazione siopeAssenzaMotivazione = new SiopeAssenzaMotivazione();
		if(!isEmpty(motivazioneAssenzaCigSelezionata)){
			siopeAssenzaMotivazione.setCodice(motivazioneAssenzaCigSelezionata);
		} else {
			//e' stato tolto:
			siopeAssenzaMotivazione = null;
		}
		return siopeAssenzaMotivazione;
	}
	
	/**
	 * Wrapper di retrocompatibilita'
	 * 
	 * Ritorna la stringa da usare per il radio button di scelta
	 * commerciale - non commerciale
	 * 
	 * @param siopeTipoDebito
	 * @return
	 */
	protected String valoreSiopeTipoDebitoPerRadioButton(SiopeTipoDebito siopeTipoDebito){
		//presento sempre "Commerciale":
		boolean commercialeConFatture = false;
		return valoreSiopeTipoDebitoPerRadioButton(siopeTipoDebito, commercialeConFatture);
	}
	
	/**
	 * Ritorna la stringa da usare per il radio button di scelta:
	 * 
	 * Commerciale - Non commerciale
	 * 
	 * Se indicato commercialeConFatture = true 
	 * in caso di siope commerciale ritorna "Commerciale (con fatture)"
	 * 
	 * 
	 * @param siopeTipoDebito
	 * @param commercialeConFatture
	 * @return
	 */
	protected String valoreSiopeTipoDebitoPerRadioButton(SiopeTipoDebito siopeTipoDebito, boolean commercialeConFatture){
		String valorePerRadio = null;
		if(siopeTipoDebito!=null && !isEmpty(siopeTipoDebito.getCodice())){
			//setto la decodifica della label:
			String codiceTipoDebito = siopeTipoDebito.getCodice();
			if(codiceTipoDebito.equals(Constanti.SIOPE_CODE_COMMERCIALE)){
				
				if(commercialeConFatture){
					//CON LA SIAC-5524 : la label Commerciale per gli impegni deve diventare:
				    // Commerciale (con fatture)
					valorePerRadio = WebAppConstants.COMMERCIALE_CON_FATTURE;
				} else {
					//labal classica "Commerciale"
					valorePerRadio = WebAppConstants.COMMERCIALE;
				}
				
			} else if(codiceTipoDebito.equals(Constanti.SIOPE_CODE_NON_COMMERCIALE)){
				valorePerRadio = WebAppConstants.NON_COMMERCIALE;
			}
		} else {
			//default in assenza del dato sul movimento
			//metto null per evidenzire la sua assenza per quelli vecchi
			//per i quali ancora non vi era il dato
			valorePerRadio = null;
		}
		return valorePerRadio;
	}
	
	/**
	 * ritorna il codice della motivazione, da usare per il campo nel model
	 * che rappresenza la motivazione assenza cig selezionata nel tendino 
	 * di scelta 
	 * @param siopeAssenzaMotivazione
	 * @return
	 */
	protected String codiceMotivazioneAssenzaCig(SiopeAssenzaMotivazione siopeAssenzaMotivazione){
		String codiceMotivazioneAssenzaCig = null;
		if(siopeAssenzaMotivazione!=null && !isEmpty(siopeAssenzaMotivazione.getCodice())){
			//setto il codice nel model:
			codiceMotivazioneAssenzaCig = siopeAssenzaMotivazione.getCodice();
		}
		return codiceMotivazioneAssenzaCig;
	}
	
	/**
	 * ritorna la descrizione della motivazione, da usare nei 
	 * casi d'uso di consultazione
	 * 
	 * @param siopeAssenzaMotivazione
	 * @return
	 */
	protected String descrizioneMotivazioneAssenzaCig(SiopeAssenzaMotivazione siopeAssenzaMotivazione){
		String descrizione = null;
		if(siopeAssenzaMotivazione!=null && !isEmpty(siopeAssenzaMotivazione.getCodice())){
			//setto la descrizione nel model:
			descrizione = siopeAssenzaMotivazione.getDescrizione();
		}
		return descrizione;
	}
	
	protected String codiceTipoDebito(SiopeTipoDebito siopeSiopeTipoDebito){
		String codiceMotivazioneAssenzaCig = null;
		if(siopeSiopeTipoDebito!=null && !isEmpty(siopeSiopeTipoDebito.getCodice())){
			//setto il codice nel model:
			codiceMotivazioneAssenzaCig = siopeSiopeTipoDebito.getCodice();
		}
		return codiceMotivazioneAssenzaCig;
	}
	
	//SIAC-5333
	/**
	 * Imposta in sessione gli errori attualmente presenti nel model per garantirne l'accesso ad una action successiva.
	 */
	protected void setErroriInSessionePerActionSuccessiva() {
		// Se ho errori
		if(!model.getErrori().isEmpty()) {
			// Imposto gli errori in sessione
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE, new ArrayList<Errore>(model.getErrori()));
		}
	}
	
	/**
	 * Imposta in sessione i warning attualmente presenti nel model per garantirne l'accesso ad una action successiva.
	 */
	protected void setWarningInSessionePerActionSuccessiva() {
		// Se ho errori
		if(!model.getWarning().isEmpty()) {
			// Imposto gli errori in sessione
			sessionHandler.setParametro(FinSessionParameter.WARNING_AZIONE_PRECEDENTE, new ArrayList<Errore>(model.getWarning()));
		}
	}
	
	/**
	 * Gestisce assieme warning ed errori dell'azione precedente
	 * @param persistent
	 */
	protected void leggiEventualiErroriEWarningAzionePrecedente(boolean persistent){
		leggiEventualiErroriAzionePrecedente(persistent);
		leggiEventualiWarningAzionePrecedente(persistent);
	}
	
	/**
	 * Leggi eventuali errori azione precedente.
	 */
	protected void leggiEventualiErroriAzionePrecedente(boolean persistent) {
		List<Errore> listaErroriPrecedenti = sessionHandler.getParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE);
		if(listaErroriPrecedenti != null) {
			// Pulisco la sessione
			sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE, null);
			for(Errore err : listaErroriPrecedenti) {
				// Aggiungo ogni errore nel model
				if(persistent){
					addPersistentActionError(err.getTesto());
				} else {
					addActionError(err.getTesto());
				}
				
			}
		}
		
	}
	
	/**
	 * Leggi eventuali errori azione precedente.
	 */
	protected void leggiEventualiWarningAzionePrecedente(boolean persistent) {
		List<Errore> listaErroriPrecedenti = sessionHandler.getParametro(FinSessionParameter.WARNING_AZIONE_PRECEDENTE);
		if(listaErroriPrecedenti != null) {
			// Pulisco la sessione
			sessionHandler.setParametro(FinSessionParameter.WARNING_AZIONE_PRECEDENTE, null);
			for(Errore err : listaErroriPrecedenti) {
				// Aggiungo ogni errore nel model
				if(persistent){
					addPersistentActionWarning(err.getTesto());
				} else {
					addActionWarning(err.getTesto());
				}
				
			}
		}
		
	}
	
	/**
	 * Per esporre nelle jsp il ConverterEuro per i tag che non supportano il decorator
	 * (es. s:property)
	 * @param nb
	 * @return
	 */
	public String converterEuro(Number nb){
		Object conversione = ConverterEuro.conversione(nb);
		if(conversione!=null){
			return conversione.toString();
		} else {
			return "";
		}
	}
	
	protected List<StrutturaAmministrativoContabile> getStruttureAmministrativoContabileByAccount() {
		List<StrutturaAmministrativoContabile> res = sessionHandler.getAccount().getStruttureAmministrativeContabili();
		return res != null ? res : new ArrayList<StrutturaAmministrativoContabile>();
	}
	
}

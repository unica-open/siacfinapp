/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.Normalizzatore;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.IndirizzoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.RecapitoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.ValidationUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceSoggettoProvvisorio;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNomeResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuni;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuniResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.NaturaGiuridicaSoggetto;
import it.csi.siac.siacfinser.model.codifiche.TipoSoggetto;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.soggetto.ComuneNascita;
import it.csi.siac.siacfinser.model.soggetto.Contatto;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.Sesso;


/**
 * Action per la gestione del inserimento soggetto step2
 * 
 * @author paolos
 * 
 */

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciContattiAction  extends WizardScriviSoggettoAction {
	private static final long serialVersionUID = 1L;

	private String pressedButton;
	private String action;
	private String idIndirizzo;
	
	@Autowired
	private transient SoggettoService soggettoService;
	
	@Autowired
	private transient Normalizzatore recapitoContattiNormalizzatore;
	
	@Override
	public void prepare() throws Exception {

		//invoco il prepare della super classe:
		super.prepare();
		
		//puliamo gli errori:
		clearActionErrors();
		
		//e i messaggi:
		clearErrorsAndMessages();
		
		//settiamo il titolo:
		this.model.setTitolo("Inserimento recapiti");
		
		//carichiamo le liste:
		caricaListeInserisciIndirizzoSoggetto();
		
	}
	
	public String pulisciIndirizzi(){
		setMethodName("pulisciIndirizzi");
		model.setIndirizzi(new HashMap<String, IndirizzoModel>());
		model.setIndirizzo(new IndirizzoModel());
		model.getIndirizzo().setStato(WebAppConstants.CODICE_ITALIA);
		return "pulisciIndirizzo";
	}
	
	/**
	 * Per retrocompatibilita'
	 * 
	 * @param pulisciTutti
	 * @return
	 */
	public String pulisciContatto(){
		return this.pulisciContatto(true);
	}
	
	/**
	 * Metodo nato per il fix alla jira SIAC-2050 (venivano erroneamente sempre puliti tutti i contatti in inserimento dal tasto elimina
	 * su un ben preciso contatto)
	 * @param pulisciTutti
	 * @return
	 */
	public String pulisciContatto(boolean pulisciTutti){
		setMethodName("pulisciContatto");
		model.setRecapito(new RecapitoModel());
		if(pulisciTutti){
			model.resetRecapiti();	
		}
		return "pulisciContatto";
	}
	
	
	public String pulisciCampi(){
		setMethodName("pulisciCampi");
		model.setIndirizzo(new IndirizzoModel());
		model.getIndirizzo().setStato(WebAppConstants.CODICE_ITALIA);
		model.setRecapito(new RecapitoModel());
		return SUCCESS;
	}
	
	public String annullaIndirizziERecapiti(){
		//Puliamo gli indirizzi:
		pulisciIndirizzi();
		//lanciamo l'execute:
		return execute();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() {
		setMethodName("execute");
		
		boolean pulisciTutti = true;
		
		if (StringUtils.isNotEmpty(pressedButton))
		{
			// azione bottone indirizzi
			
			if ("eliminaIndirizzo".equals(pressedButton))
				model.getIndirizzi().remove(idIndirizzo);
			

			if (getPressedButton().indexOf("tipoRecapito") != -1)
			{
				debug(methodName,
						"acchiappo la stringa recapito "
								+ getPressedButton().substring(
										getPressedButton().indexOf("tipoRecapito") + 13));

				model.getRecapiti().remove(
						(String) getPressedButton().substring(
								getPressedButton().indexOf("tipoRecapito") + 13));

			}
			
			pulisciTutti = false;

		}
		
		if (model.getIndirizzo() == null) {
			pulisciIndirizzi();
		}
		
		pulisciContatto(pulisciTutti);
		
		log.debugEnd(methodName, "");
		return SUCCESS;
	}
	
	public String indietroStep1() {
		//torna alla pagina prima
		return "indietroStep1";
	}
	
	/**
	 * metodo che effettua il salvataggio dei dati
	 * 
	 * @return
	 */
	public String salva() {
		final String methodName = "salva";
		
		debug(methodName, "chiamo la soggetto service client");
		
		//istanzio la request per il servizio:
		InserisceSoggetto inserisceSoggetto = new InserisceSoggetto();
		
		inserisceSoggetto.setRichiedente(sessionHandler.getRichiedente());
		
		
		// travaso i dati in inserisce soggetto
		inserisceSoggetto = travasoDati(inserisceSoggetto);
		
		InserisceSoggettoResponse response = null;
		if (isInserimentoSoggettoProvvisorio()) {
			//invoco il servizio:
			response = soggettoService.inserisceSoggettoProvvisorio(new InserisceSoggettoProvvisorio(inserisceSoggetto));
		} else {
			inserisceSoggetto.setCodificaAmbito(getCodificaAmbito());
			//invoco il servizio:
			response = soggettoService.inserisceSoggetto(inserisceSoggetto);
		}
		
		debug(methodName, "!!!! dopo la chiamata al servizio !!!!");
		
		if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
			debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			
			debug(methodName, "Model: " + model);
			
			return INPUT;
		}
		
		
		model.setStatoSoggetto(response.getSoggetto().getStatoOperativo().name());
		model.setCodiceSoggetto(response.getSoggetto().getCodiceSoggetto());
		model.setFallimento(response.isFallimento());
		
		pulisciIndirizzi();
		pulisciContatto(true);
		
		
		return "salva";
		
	}

	/**
	 * @return
	 */
	protected boolean isInserimentoSoggettoProvvisorio() {
		return FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(), ABILITAZIONE_INSERIMENTO_DECENTRATO);
	}
	
	protected String getCodificaAmbito() {
		// default AMBITO_FIN
		return null; 
	}

	private InserisceSoggetto travasoDati(InserisceSoggetto inserisceSoggetto){
		final String methodName = "travasoDati";
		
		debug(methodName, "inizio il travaso dati");
		
		inserisceSoggetto.setEnte(sessionHandler.getRichiedente().getAccount().getEnte());
		
		Soggetto soggetto = new Soggetto();
		
		// tipo soggetto
		TipoSoggetto tipoSoggetto = new TipoSoggetto();
		tipoSoggetto.setCodice( model.getIdTipoSoggetto());
		soggetto.setTipoSoggetto(tipoSoggetto);
		
		// natura giuridica soggetto
		NaturaGiuridicaSoggetto naturaGiuridicaSoggetto = new NaturaGiuridicaSoggetto();
		
		// VARIAZIONE ID --> CODE 
		naturaGiuridicaSoggetto.setSoggettoTipoCode(model.getIdNaturaGiuridica());
		
		soggetto.setNaturaGiuridicaSoggetto(naturaGiuridicaSoggetto);
		
		 
		soggetto.setCodiceFiscale(model.getCodiceFiscale());
		soggetto.setCodiceFiscaleEstero(model.getCodiceFiscaleEstero());
		soggetto.setCognome(model.getCognome());
		soggetto.setNome(model.getNome());
		soggetto.setNote(model.getNote());
		soggetto.setPartitaIva(model.getPartitaIva());
		
		//SIAC-6565-CR12115
		soggetto.setEmailPec(model.getEmailPec());
		soggetto.setCodDestinatario(model.getCodDestinatario());
		soggetto.setCanalePA(model.getCanalePA());
		
		// ragione sociale
	    soggetto.setDenominazione(model.getDenominazione());
		
		
		if(StringUtils.isNotEmpty(model.getDataNascita())){
			soggetto.setDataNascita(DateUtility.stringToDate(model.getDataNascita(), "dd/MM/yyyy"));
		}
		
		soggetto.setDenominazione(model.getDenominazione());
		debug(methodName, "sesso "+model.getFlagSesso());
		if(null==model.getFlagSesso() || StringUtils.isEmpty(model.getFlagSesso())){
			//NON_DEFINITO
			soggetto.setSesso(Sesso.NON_DEFINITO);
		}else if(model.getFlagSesso().equalsIgnoreCase(Constanti.MASCHIO)){
			//MASCHIO
			soggetto.setSesso(Sesso.MASCHIO);
		}else {
			//FEMMINA
			soggetto.setSesso(Sesso.FEMMINA);
		}
		
		//  STATO VALIDO
		soggetto.setStato(it.csi.siac.siaccorser.model.Entita.StatoEntita.VALIDO);
		
		// date
		soggetto.setDataCreazione(new Date());
		soggetto.setDataValidita(new Date());
		
		
		debug(methodName, "Verifico il comune di nascita "+model.getComune());
		// comune
		
		ComuneNascita comuneNascita = new ComuneNascita();
		comuneNascita.setDescrizione(model.getComune());
		comuneNascita.setNazioneCode(model.getIdNazione());
		if(WebAppConstants.CODICE_ITALIA.equals(model.getIdNazione()) && StringUtils.isNotEmpty(model.getIdComune())){
			try{   
				comuneNascita.setComuneIstatCode(model.getIdComune());
			}catch(NumberFormatException nfe){
				log.error(methodName, "errore conversione id comune");
			}
		}
		
		soggetto.setComuneNascita(comuneNascita);
		
		debug(methodName, "COMUNE "+soggetto.getComuneNascita());
		
		// TIPO CLASSIFICAZIONE
		if(null!=model.getIdClasseSoggetto()){
			// genero l'array grande quanto le n selezioni da combo
			String[] arrayIdClasse = new String[model.getIdClasseSoggetto().length];
			for (int i = 0; i < model.getIdClasseSoggetto().length; i++) {
				
				debug(methodName, " ID CLASSE SOGGETTO "+model.getIdClasseSoggetto()[i]);
				arrayIdClasse[i] = model.getIdClasseSoggetto()[i];
				
				
			}  
			soggetto.setTipoClassificazioneSoggettoId(arrayIdClasse);
		}
		
		// TIPO ONERE
		if(null!=model.getIdTipoOnere()){
			// genero l'array grande quanto le n selezioni da combo
			String[] arrayIdOnere = new String[model.getIdTipoOnere().length];
			for (int i = 0; i < model.getIdTipoOnere().length; i++) {
				
				debug(methodName, " ID ONERE TIPO "+model.getIdTipoOnere()[i]);
				arrayIdOnere[i] = model.getIdTipoOnere()[i];
							
			}  
			soggetto.setTipoOnereId(arrayIdOnere);
		}
		
		// matricola
		soggetto.setMatricola(model.getMatricola());
		
		soggetto.setDataFineValiditaDurc(model.getDataFineValiditaDurc());
		soggetto.setTipoFonteDurc(model.getTipoFonteDurc());
		soggetto.setFonteDurcClassifId(model.getFonteDurcClassifId());
		soggetto.setNoteDurc(model.getNoteDurc());
		
		
		inserisceSoggetto.setSoggetto(soggetto);
		
		// travaso dati Recapiti e Indirizzi
		inserisceSoggetto = travasoDatiRecapiti(inserisceSoggetto);
		
		return inserisceSoggetto;
	}
	
	
	private InserisceSoggetto travasoDatiRecapiti(InserisceSoggetto inserisceSoggetto){
		
		setMethodName("travasoDatiRecapiti");
		debug(methodName, "entro qui "+model);
		debug(methodName, "entro qui "+model.getIndirizzi());
		
		if(null!=model.getIndirizzi() && model.getIndirizzi().size()>0){
			log.debug(methodName, "Ci sono degli indirizzi");
			List<IndirizzoSoggetto> listaIndirizzoSoggettos = new ArrayList<IndirizzoSoggetto>();
			Set<Entry<String, IndirizzoModel>>  setIter = model.getIndirizzi().entrySet();
			Iterator<Entry<String, IndirizzoModel>> it = setIter.iterator();
			while (it.hasNext()){
				Entry<String, IndirizzoModel> hm = it.next();
				
				log.debug(methodName, " Kiave "+hm.getKey()+" VALORE "+hm.getValue());
				
				IndirizzoModel src = hm.getValue();
				IndirizzoSoggetto dest = new IndirizzoSoggetto();
				
				if(null==src.getAvviso() || StringUtils.isEmpty(src.getAvviso())){
					dest.setAvviso(Boolean.FALSE.toString());	
				}else if(src.getAvviso().equalsIgnoreCase(WebAppConstants.MSG_SI)){
					dest.setAvviso(Boolean.TRUE.toString());
				}
				
				if(null==src.getPrincipale() || StringUtils.isEmpty(src.getPrincipale())){
					dest.setPrincipale(Boolean.FALSE.toString());	
				}else if(src.getPrincipale().equalsIgnoreCase(WebAppConstants.MSG_SI)){
					dest.setPrincipale(Boolean.TRUE.toString());
				}
				
				dest.setCap(src.getCap());
				dest.setDenominazione(src.getNomeVia());
				// Varizione ID --> CODE
				dest.setIdTipoIndirizzo(src.getTipoIndirizzo());
				dest.setNumeroCivico(src.getNumeroCivico());
				dest.setSedime(src.getSedime());
				dest.setIdComune(src.getIdComune());
				dest.setComune(src.getComune());
				dest.setCodiceNazione(src.getStato()); // nazione code
				
				
				
				listaIndirizzoSoggettos.add(dest);
				
			}
			// setto la lista degli indirizzi 
			inserisceSoggetto.getSoggetto().setIndirizzi(listaIndirizzoSoggettos);
			
		}
		
		if(null!=model.getRecapiti() && model.getRecapiti().size()>0){
			debug(methodName, "Ci sono dei contatti");
			inserisceSoggetto.getSoggetto().setContatti(new ArrayList<Contatto>(model.getRecapiti().values()));
		}
		
		return inserisceSoggetto;
		
	}
	
	public String elimina() {
		return SUCCESS;
	}
	
	/**
	 * metodo che effettua l'inserimento in sessione
	 * nella tabella degli indirizzi
	 * 
	 * @return
	 */
	public String salvaIndirizzo() {
		final String methodName = "salvaIndirizzo";
		
		if ("aggiornaIndirizzo".equals(pressedButton))
			model.getIndirizzi().remove(idIndirizzo);
			
		// eventuale verifica dei dati
		
		// aggiungo il dato inserito
		
		HashMap<String, IndirizzoModel> hm = null;
		if(null==model.getIndirizzi() ||  model.getIndirizzi().isEmpty()){
			debug(methodName, "lista vuota ne istanzio una nuova");
			hm = new HashMap<String, IndirizzoModel>();
		}else{
			debug(methodName, "lista non vuota, la uso");
			hm = model.getIndirizzi();
		}
		
		log.debug(methodName, "dalvo indirizzi "+model.getIndirizzi());
		
		int random  = (int)Math.floor(Math.random() * 10000);

		IndirizzoModel indirizzo = new IndirizzoModel();
		
		//ID INDIRIZZO:
		indirizzo.setIdIndirizzo(String.valueOf(random));
		
		//STATO:
		indirizzo.setStato(model.getIndirizzo().getStato());
		
		// ciclo per inserire il tipo di indirizzo come descrizione nella tabellina
		// degli indirizzi
		Iterator<CodificaFin> itTipoSede = 	model.getListaTipoIndirizzoSede().iterator();
		while (itTipoSede.hasNext()){
			CodificaFin t = itTipoSede.next();
			log.debug(methodName, "Verifico indirizzi "+model.getIndirizzo().getTipoIndirizzo() + " - confronto "+t.getCodice());
			if(t.getCodice().toString().equals(model.getIndirizzo().getTipoIndirizzo())){
				indirizzo.setTipoIndizzoDesc(t.getDescrizione());
			}
		}
		
		indirizzo.setTipoIndirizzo(model.getIndirizzo().getTipoIndirizzo());
		indirizzo.setSedime(model.getIndirizzo().getSedime());
		indirizzo.setNomeVia(model.getIndirizzo().getNomeVia());
		indirizzo.setNumeroCivico(model.getIndirizzo().getNumeroCivico());
		indirizzo.setCap(model.getIndirizzo().getCap());
		
		indirizzo.setIndirizzoEsteso(costruisceIndirizzoEsteso());

		log.debug(methodName,  "dalvo avviso "+model.getIndirizzo().isCheckAvviso());
		log.debug(methodName,  "dalvo princi "+model.getIndirizzo().isCheckPrincipale());
		
		//CHECK AVVISO
		if(model.getIndirizzo().isCheckAvviso()){
			log.debug(methodName, "dalvo valorizzo avviso a SI");
			indirizzo.setAvviso("si");
		}else{
			model.getIndirizzo().setCheckAvviso(false);
			indirizzo.setAvviso("");
		}
		
		//CHECK PRINCIPALE
		if (model.getIndirizzo().isCheckPrincipale()) {
			for (IndirizzoModel i : model.getIndirizzi().values()){
				if ("si".equalsIgnoreCase(i.getPrincipale())){
					i.setPrincipale("");
					addActionWarning("Attenzione: l'indirizzo e' stato selezionato come principale e andra' a sostituire quello precedente ");
					break;
				}
			}
			indirizzo.setPrincipale("si");
		} else {
			model.getIndirizzo().setCheckPrincipale(false);
			indirizzo.setPrincipale("");
		}
		
		log.debug(methodName,  "dalvo sono qui "+indirizzo.getAvviso());
		log.debug(methodName,  "dalvo sono qui "+indirizzo.getPrincipale());
		
		// controllo dati inseriti per indirizzo
		List<Errore> listaErrori = new ArrayList<Errore>();
		ValidationUtils.validaCampiInserimentoIndirizzi(listaErrori, model);
	
		if(null!=model.getIndirizzo() && StringUtils.isEmpty(model.getIndirizzo().getIdComune())){
			
			debug(methodName, "verifico la nazione ");
			
			if(WebAppConstants.CODICE_ITALIA.equals(model.getIndirizzo().getStato())){
				
				if(model.getIndirizzo().getComune()!=null && StringUtils.isNotEmpty(model.getIndirizzo().getComune())){
					ListaComunePerNomeResponse comunePerNomeResponse = controlloPuntualeComune(model.getIndirizzo().getComune(), model.getIndirizzo().getStato());
					if (comunePerNomeResponse == null || comunePerNomeResponse.isFallimento() || comunePerNomeResponse.getListaComuni() == null || comunePerNomeResponse.getListaComuni().size() == 0 || comunePerNomeResponse.getListaComuni().get(0).getId() == null) {
						listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Comune"));
					} else {
						model.getIndirizzo().setIdComune(comunePerNomeResponse.getListaComuni().get(0).getCodice());
						model.getIndirizzo().setComune(comunePerNomeResponse.getListaComuni().get(0).getDescrizione());
					}
				}
				
				// SIAC-5857: CAP non controllati quando minori di 5 crt per l'italia, per l'estero aperta   
				if (model.getIndirizzo().getCap().length() != 5) {
					listaErrori.add(ErroreFin.CAMPO_LUNGHEZZA_MINIMA_NON_RISPETTATA.getErrore("CAP", 5));
				}
				
			}
		}
		
		//LUNGHEZZA 30 CARATTERI MAX su indirizzo:
		if ((model.getIndirizzo().getSedime() + model.getIndirizzo().getNomeVia() + model.getIndirizzo().getNumeroCivico()).length() > 28){
			// considera spazi concatenazione
			listaErrori.add(ErroreCore.VALORE_NON_VALIDO.getErrore("Indirizzo","(L'indirizzo non deve superare i 30 caratteri)"));
		}
			
		//LUNGHEZZA 30 CARATTERI MAX su comune:
		if (model.getIndirizzo().getComune().length() > 30){
			listaErrori.add(ErroreCore.VALORE_NON_VALIDO.getErrore("Comune","(Il campo Comune non deve superare i 30 caratteri)"));
		}
		
		indirizzo.setComune(model.getIndirizzo().getComune());
		indirizzo.setIdComune(model.getIndirizzo().getIdComune());
		// catturo gli errori
		if(!listaErrori.isEmpty()) {
			addErrori(listaErrori);
			return INPUT;
		} 
		
		hm.put(indirizzo.getIdIndirizzo(), indirizzo);
		log.debug(methodName, "HM "+hm);
		model.setIndirizzi(hm);

		model.setIndirizzo(new IndirizzoModel());
		model.getIndirizzo().setStato(WebAppConstants.CODICE_ITALIA);
		
		sessionHandler.setParametro(FinSessionParameter.INSERISCI_SOGGETTO_STEP2, model);
		
		log.debug(methodName,   "dalvo dopo componi");
		log.debugEnd(methodName,"");
		return SUCCESS;
	}

	public String salvaRecapito() {
		final String methodName = "salvaRecapito";
				
		log.debug(methodName, "rex");

		List<Errore> listaErrori = new ArrayList<Errore>();
		ValidationUtils.validaRecapitiSoggetto(listaErrori, model.getRecapito());
		
		// catturo gli errori
		if(!listaErrori.isEmpty()) {
			addErrori(listaErrori);
			return INPUT;
		} 
		
		model.getRecapito().setListaRecapitoModo(model.getListaRecapitoModo());
		List<Contatto> l = recapitoContattiNormalizzatore.getModelNormalizzato(model.getRecapito(), Contatto.class);
		
		HashMap<String, Contatto> rec = new HashMap<String, Contatto>(l.size());
		outer:
		for (Contatto c : l) {
			for (CodificaFin cef : model.getListaRecapitoModo()) {
				if (cef.getCodice().equals(c.getContattoCodModo())) {
					c.setDescrizioneModo(cef.getDescrizione());
					rec.put(cef.getDescrizione(), c);
					continue outer;
				}
			}
		}
		model.setRecapiti(rec);
		
		model.getRecapito().setCheckAvvisoPec("");
		model.getRecapito().setCheckAvvisoEmail("");
		
		// metto in sessione il tutto
		sessionHandler.setParametro(FinSessionParameter.INSERISCI_SOGGETTO_STEP2, model);
		
		log.debugEnd(methodName, "");
	
		return SUCCESS;
	}	
	
	private String costruisceIndirizzoEsteso(){
		
		StringBuffer sb = new StringBuffer();
		
		//SEDIME
		if(StringUtils.isNotEmpty(model.getIndirizzo().getSedime())){
			sb.append(model.getIndirizzo().getSedime()).append(" ");
		}
		
		//NOME VIA
		if(StringUtils.isNotEmpty(model.getIndirizzo().getNomeVia())){
			sb.append(model.getIndirizzo().getNomeVia()).append(" ");
		}
		
		//NUMERO CIVICO
		if(StringUtils.isNotEmpty(model.getIndirizzo().getNumeroCivico())){
			sb.append(model.getIndirizzo().getNumeroCivico()).append(" ");
		}
		
		//CAP
		if(StringUtils.isNotEmpty(model.getIndirizzo().getCap())){
			sb.append(model.getIndirizzo().getCap()).append(" ");
		}
		
		//COMUNE
		if (StringUtils.isNotEmpty(model.getIndirizzo().getComune())){

			sb.append(model.getIndirizzo().getComune()).append(" ");

			//istanzio la request per il servizio:
			ListaComuni listaComuni = new ListaComuni();
			listaComuni.setDescrizioneComune(model.getIndirizzo().getComune());
			listaComuni.setRichiedente(sessionHandler.getRichiedente());

			//invoco il servizio:
			ListaComuniResponse lcr = genericService.cercaComuni(listaComuni);

			if (!(lcr.isFallimento() || lcr.getListaComuni() == null || lcr.getListaComuni().isEmpty())){
				sb.append(String.format("(%s)", lcr.getListaComuni().get(0).getSiglaProvincia())).append(" ");
			}
				
		}
	
		//STATO
		if (!WebAppConstants.CODICE_ITALIA.equals(model.getIndirizzo().getStato())){
			if (StringUtils.isNotEmpty(model.getIndirizzo().getNomeNazione())){
				sb.append(" - ").append(model.getIndirizzo().getNomeNazione());
			}
		}
		
		//ritorno la stringa appena composta:
		return sb.toString();
	}
	
	public String getPressedButton() {
		return pressedButton;
	}

	public void setPressedButton(String pressedButton) {
		this.pressedButton = pressedButton;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public String getIdIndirizzo()
	{
		return idIndirizzo;
	}

	public void setIdIndirizzo(String idIndirizzo)
	{
		this.idIndirizzo = idIndirizzo;
	}

}

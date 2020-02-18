/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.model.Normalizzatore;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.RecapitoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.ValidationUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggettoProvvisorio;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaSedeInModifica;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaSedeInModificaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNome;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNomeResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSedeSecondariaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSedeSecondariaPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.soggetto.Contatto;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.Sesso;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto.StatoOperativoSedeSecondaria;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaSediSecondarieAction extends AggiornaSoggettoGenericAction {

	private static final long serialVersionUID = 1L;
	private Integer idSedeSecondaria;
	private Integer idSedeSecondariaDaEliminare;
	private SedeSecondariaSoggetto selectedSede;
	private SedeSecondariaSoggetto selectedSedeMod;
	
	private boolean fromPulsanteInserisciNuova;
	
	// hidden per la gestione della pagina monca
	private String soggetto;
	private boolean fromGestisciSede;
	
	// hidden per la gestione della validazione
	private Integer idSedeSecondariaValidazione;
	private String codiceSedeSecondariaInValidazione;
	/////////////////////////////////////////////////
	
	@Autowired
	private transient Normalizzatore recapitoContattiNormalizzatore;

	public Integer getIdSedeSecondariaDaEliminare() {
		return idSedeSecondariaDaEliminare;
	}
	public void setIdSedeSecondariaDaEliminare(String idSedeSecondariaDaEliminare) {
		if (idSedeSecondariaDaEliminare != null && idSedeSecondariaDaEliminare.trim().length() > 0)
			this.idSedeSecondariaDaEliminare = Integer.valueOf(idSedeSecondariaDaEliminare);
	}

	public Integer getIdSedeSecondaria() {
		return idSedeSecondaria;
	}

	public void setIdSedeSecondaria(String idSedeSecondaria) {
		if (idSedeSecondaria != null && idSedeSecondaria.trim().length() > 0)
			this.idSedeSecondaria = Integer.valueOf(idSedeSecondaria);
	}

	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		model.setTitolo("Gestione sedi secondarie");
		//setto il titolo:
		pulisciNuovaSede();
		model.getRecapitoSS().setCheckAvvisoEmail("false");
		model.getRecapitoSS().setCheckAvvisoPec("false");
		model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().setAvviso("false");
		
	}
	
	private List<Errore> validateForm() {
		List<Errore> listaErrori = new ArrayList<Errore>();
		if(FinStringUtils.isEmpty(model.getNuovaSedeSecondaria().getDenominazione())) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Denominazione"));
		}
		ValidationUtils.validaCampiInserimentoIndirizzi(listaErrori, model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale());
		ValidationUtils.validaRecapitiSoggetto(listaErrori, model.getRecapitoSS());
		
		debug(methodName, " ciclo la lista errori ");
		
		if(!ciclaListaErrori(listaErrori, "Comune", ErroreCore.DATO_OBBLIGATORIO_OMESSO)){
			String[] idComune = (String[])getRequest().get("nuovaSedeSecondaria.indirizzoSoggettoPrincipale.idComune");
			
			if (idComune == null || 
					"".equalsIgnoreCase(idComune[0])) {
				ListaComunePerNome comunePerNome = new ListaComunePerNome();
				comunePerNome.setCodiceNazione(model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().getCodiceNazione());
				comunePerNome.setNomeComune(model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().getComune());
				comunePerNome.setRichiedente(sessionHandler.getRichiedente());
				comunePerNome.setRicercaPuntuale(true);
				ListaComunePerNomeResponse response = genericService.findComunePerNome(comunePerNome);
				if (response != null && response.getListaComuni() != null && response.getListaComuni().size() > 0 && response.getListaComuni().get(0).getCodice() != null) {
					model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().setIdComune(response.getListaComuni().get(0).getCodice());
				} else {
					model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().setIdComune("");
				}
			}
		    // se gia' non e' stato segnalato prima come errore il comune allora vado a verificare
			// che l'utente abbia usato l'autocomplete
			if(null!=model.getNuovaSedeSecondaria() && model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale() != null && 
				FinStringUtils.isEmpty(model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().getIdComune())){
				debug(methodName, "verifico la nazione ");
				if(WebAppConstants.CODICE_ITALIA.equals(model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().getCodiceNazione())){
					ListaComunePerNomeResponse comunePerNomeResponse = controlloPuntualeComune(model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().getComune(), model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().getCodiceNazione());
					if (comunePerNomeResponse == null || comunePerNomeResponse.isFallimento() || comunePerNomeResponse.getListaComuni() == null || comunePerNomeResponse.getListaComuni().size() == 0 || comunePerNomeResponse.getListaComuni().get(0).getId() == null) {
						listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Comune"));
					} else {
						model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().setIdComune(comunePerNomeResponse.getListaComuni().get(0).getCodice());
						model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().setComune(comunePerNomeResponse.getListaComuni().get(0).getDescrizione());
					}
					
				}
				
			}
	    }
		
		SedeSecondariaSoggetto sedeInInserimentoOppureModifica = model.getNuovaSedeSecondaria();
		if (model.getListaSecondariaSoggetto() != null && model.getListaSecondariaSoggetto().size() > 0) {
			for (SedeSecondariaSoggetto currentSede : model.getListaSecondariaSoggetto()) {
				if( (sedeInInserimentoOppureModifica.getUid() != currentSede.getUid()) ){
					//la sede iterata e' diversa da quella in inserimento/modifica
					if (currentSede.getDenominazione().equalsIgnoreCase(sedeInInserimentoOppureModifica.getDenominazione())) {
						listaErrori.add(ErroreFin.ENTITA_GIA_PRESENTE.getErrore("Sede secondaria", model.getNuovaSedeSecondaria().getDenominazione()));
						break;
					}
				}
			}
		}
		
		if(!listaErrori.isEmpty()) {
			addErrori(listaErrori);
		}
		
		return listaErrori;
	}
	
	private boolean ciclaListaErrori(List<Errore> listaErrori, String stringaPresente, ErroreCore erroreCore){
		
		boolean presenzaErrore = false;
		Iterator<Errore> it = listaErrori.iterator();
		while(it.hasNext()){
			Errore temp = it.next();
			if(temp.getDescrizione().contains(stringaPresente) &&
				temp.getCodice().equals(erroreCore.getCodice())	){
				presenzaErrore = true;
				break;
			}
		}
		
		return presenzaErrore;
		
	}
	
	private RicercaSoggettoPerChiaveResponse caricaDatiSoggettoDaService(){
		caricaListeInserisciSoggetto();
		setSoggetto(model.getDettaglioSoggetto().getCodiceSoggetto());
		//Compongo la request per il servizio:
		RicercaSoggettoPerChiave requestRicerca = convertiModelPerChiamataServizioRicercaPerChiave(getSoggetto());
		//invoco il servizio:
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(requestRicerca);
		//ritorno la resp al chiamante:
		return response;

	}
	
	/**
	 * excute della action
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		
		if(fromGestisciSede) {
			
			RicercaSoggettoPerChiaveResponse response = caricaDatiSoggettoDaService();
			
			if(response.isFallimento()) {
				addErrori(methodName, response);
				return INPUT;
			}
			// converto la data
			setDataNascitaStringa(convertDateToString(response.getSoggetto().getDataNascita()));
			
	
			// setto il radio estero si/no
			if(response.getSoggetto().isResidenteEstero()) response.getSoggetto().setResidenteEsteroStringa("si");
			else response.getSoggetto().setResidenteEsteroStringa("no");
			
			
			// setto il sesso 
			if(response.getSoggetto().getSesso().equals(Sesso.MASCHIO))
				 response.getSoggetto().setSessoStringa(Constanti.MASCHIO.toLowerCase());
			else response.getSoggetto().setSessoStringa(Constanti.FEMMINA.toLowerCase());
			
			// setto soggetto generale
			model.setDettaglioSoggetto(response.getSoggetto());
			model.setListaModalitaPagamentoSoggetto(response.getListaModalitaPagamentoSoggetto());
			
			///////////////////////////////////////////////////////////////////////
			// dovra' rimanere solo questo set
			model.setListaSecondariaSoggetto(response.getListaSecondariaSoggetto());
		
		}
		
		//Correggiamo la dicitura in modifica:
		 if(model.getListaSecondariaSoggetto()!= null && model.getListaSecondariaSoggetto().size()>0){
			   for(SedeSecondariaSoggetto sede: model.getListaSecondariaSoggetto()){
				    if(sede!=null && StatoOperativoAnagrafica.IN_MODIFICA.name().equalsIgnoreCase(sede.getDescrizioneStatoOperativoSedeSecondaria())){
				    	sede.setDescrizioneStatoOperativoSedeSecondaria(Constanti.STATO_IN_MODIFICA_no_underscore);
				    }
			   }
		 }
		
		pulisciNuovaSede();		
		return super.execute();
	}
	
	/**
	 * salvataggio che richiama la funzione generale
	 * @return
	 */
	public String salvaGenerale(){
		setMethodName("salvaGenerale");

		// inserisco il codice tipo persona
		model.getDettaglioSoggetto().getTipoSoggetto().setCodice(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode());
		model.getDettaglioSoggetto().setSediSecondarie(model.getListaSecondariaSoggetto());
		if (model.getDettaglioSoggetto().getSessoStringa() != null && !"".equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa())) {
			if (Constanti.MASCHIO.equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa().toUpperCase())) {
				model.getDettaglioSoggetto().setSesso(Sesso.MASCHIO);
			} else {
				model.getDettaglioSoggetto().setSesso(Sesso.FEMMINA);
			}
		}
		AggiornaSoggettoResponse response = null;
		
		//a seconda se siamo decentrati o meno richiamiamo il servizio
		//opportuno di aggiornamento del soggetto:
		if (FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(), ABILITAZIONE_GESTIONE_DECENTRATO)) {
			//AGGIORNAMENTO PROVVISIORIO
			AggiornaSoggettoProvvisorio asp = new AggiornaSoggettoProvvisorio(convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(),true));
			response = soggettoService.aggiornaSoggettoProvvisorio(asp);
		}else{
			//AGGIORNAMENTO NORMALE
			response = soggettoService.aggiornaSoggetto(convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(),true));
		}
		//ANALIZZIAMO LA RISPOSTA DEL SERVIZIO:
		if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
			debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			
			debug(methodName, "Model: " + model);
			
			return INPUT;
		}
		
		addActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " 
                + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		
		model.setListaSecondariaSoggetto(response.getSoggettoAggiornato().getSediSecondarie());
		return SUCCESS;
		
	}
	
	private void ricaricaSedi() {
		RicercaSoggettoPerChiaveResponse response = caricaDatiSoggettoDaService();
		if (response != null) {
			model.setListaSecondariaSoggetto(response.getListaSecondariaSoggetto());
		}
	}
	
	public String consultaSede() throws Exception {
		if (idSedeSecondaria != null) {
			selectedSede = model.getListaSecondariaSoggetto().get(idSedeSecondaria - 1);
			//Compongo la request per il servizio ricercaSedeSecondariaPerChiave:
			RicercaSedeSecondariaPerChiave request2 = new RicercaSedeSecondariaPerChiave();
			request2.setEnte(sessionHandler.getEnte());
			request2.setRichiedente(sessionHandler.getRichiedente());
			request2.setSoggetto(model.getDettaglioSoggetto());
			request2.setSedeSecondariaSoggetto(selectedSede);
			request2.setMod(false);
			RicercaSedeSecondariaPerChiaveResponse resp2 = soggettoService.ricercaSedeSecondariaPerChiave(request2);
			selectedSede.setUtenteCreazione(resp2.getSedeSecondariaSoggetto().getUtenteCreazione());
			Date data= resp2.getSedeSecondariaSoggetto().getDataCreazione();
			
			String utModDecentrato = resp2.getSedeSecondariaSoggetto().getUtenteModifica();
			Date dataModDecentrato = resp2.getSedeSecondariaSoggetto().getDataModifica();
			
			if(checkSedeInModifica()){
               //Ricerca della sede In modifica
				RicercaSedeSecondariaPerChiave request = new RicercaSedeSecondariaPerChiave();
					request.setEnte(sessionHandler.getEnte());
					request.setRichiedente(sessionHandler.getRichiedente());
					request.setSoggetto(model.getDettaglioSoggetto());
					request.setSedeSecondariaSoggetto(selectedSede);
					request.setMod(true);
				RicercaSedeSecondariaPerChiaveResponse resp = soggettoService.ricercaSedeSecondariaPerChiave(request);
				selectedSedeMod = resp.getSedeSecondariaSoggetto();

				if (FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(), ABILITAZIONE_GESTIONE_DECENTRATO) && 
						isUtenteLoggato(selectedSedeMod.getUtenteCreazione()) ) {
					//SONO DECENTRATO E SONO IL "MODIFICANTE" 
					// Jira - 947
					selectedSede.setUtenteModifica(utModDecentrato);
					selectedSede.setDataModifica(dataModDecentrato);
					selectedSede.setDenominazione(selectedSedeMod.getDenominazione());
					selectedSede.setIndirizzoSoggettoPrincipale(selectedSedeMod.getIndirizzoSoggettoPrincipale());
					selectedSede.setContatti(selectedSedeMod.getContatti());
					selectedSede.setDescrizioneStatoOperativoSedeSecondaria(Constanti.STATO_VALIDO);
					//ANCHE SE IL DEC STA GUARDANDO LA SUA PROPOSTA IN MODIFICA, LA DATA E L'UTENTE DEVONO ESSERE SEMPRE QUELLI ORIGINALI DELLA T_SOGGETTO
					selectedSede.setDataCreazione(data);
					selectedSede.setUtenteCreazione(selectedSede.getUtenteCreazione());
				}else if (!FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(), ABILITAZIONE_GESTIONE_DECENTRATO)){
					//SONO "CENTRATO" (cioe' l'admin) e vedo IN_MODIFICA
					selectedSede.setDescrizioneStatoOperativoSedeSecondaria(Constanti.STATO_VALIDO);
				} else if (FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(), ABILITAZIONE_GESTIONE_DECENTRATO) && 
						!isUtenteLoggato(selectedSedeMod.getUtenteCreazione()) ){
					//SONO DECENTRATO E NON SONO IL "MODIFICANTE" (vedo valido, cioe' non noto la modifica in corso da parte di un altro)
					selectedSede.setDescrizioneStatoOperativoSedeSecondaria(Constanti.STATO_VALIDO);
				}
				
			}else{
				//Cerca l'occorrenza della sede su db
				//Compongo la request per il servizio ricercaSedeSecondariaPerChiave:
				RicercaSedeSecondariaPerChiave request1 = new RicercaSedeSecondariaPerChiave();
				request1.setEnte(sessionHandler.getEnte());
				request1.setRichiedente(sessionHandler.getRichiedente());
				request1.setSoggetto(model.getDettaglioSoggetto());
				request1.setSedeSecondariaSoggetto(selectedSede);
				request1.setMod(false);
				//invoco il servizio:
				RicercaSedeSecondariaPerChiaveResponse resp1 = soggettoService.ricercaSedeSecondariaPerChiave(request1);
				//setto la sede appena caricata:
				selectedSede=resp1.getSedeSecondariaSoggetto();
			}
		}
		return "consulta";
	}
	
	public String modificaSede() throws Exception {
		if (idSedeSecondaria != null) {
			pulisciNuovaSede();
			selectedSede = model.getListaSecondariaSoggetto().get(idSedeSecondaria - 1);
			
			if(selectedSede!=null && selectedSede.getIndirizzoSoggettoPrincipale()!=null){
				if(selectedSede.getIndirizzoSoggettoPrincipale().getAvviso().equals(Constanti.TRUE)){
					selectedSede.getIndirizzoSoggettoPrincipale().setCheckAvviso(true);
				}else{
					selectedSede.getIndirizzoSoggettoPrincipale().setCheckAvviso(false);
				}
			}
			
			
			model.setNuovaSedeSecondaria(selectedSede);
			model.getRecapitoSS().setContatto(getDettaglioContatto("soggetto"));
			model.getRecapitoSS().setNumeroTelefono(getDettaglioContatto("telefono"));
			model.getRecapitoSS().setNumeroFax(getDettaglioContatto("fax"));
			model.getRecapitoSS().setSitoWeb(getDettaglioContatto("sito"));
			model.getRecapitoSS().setNumeroCellulare(getDettaglioContatto("cellulare"));
			Contatto c = getContatto("email");
			if (c != null) {
				model.getRecapitoSS().setEmail(c.getDescrizione());
				model.getRecapitoSS().setCheckAvvisoEmail(c.getAvviso() != null && (Constanti.TRUE.equals(c.getAvviso()) || "true".equals(c.getAvviso())) ? "true" : null);
			}
			c = getContatto("PEC");
			if (c != null) {
				model.getRecapitoSS().setPec(c.getDescrizione());
				model.getRecapitoSS().setCheckAvvisoPec(c.getAvviso() != null && (Constanti.TRUE.equals(c.getAvviso()) || "true".equals(c.getAvviso())) ? "true" : null);
			}
		}
		return "modifica";
	}

	public String validaSede() throws Exception {
		AggiornaSoggettoResponse response_1= null;
		if (idSedeSecondariaValidazione != null) {
			
			//Compongo la request per il servizio di aggiornamento del soggetto:
			AggiornaSoggetto soggetto = new AggiornaSoggetto();
			soggetto.setEnte(sessionHandler.getEnte());
			soggetto.setRichiedente(sessionHandler.getRichiedente());
			Soggetto sogg = new Soggetto();
			sogg.setUid(idSedeSecondariaValidazione);
			sogg.setCodiceSoggetto(codiceSedeSecondariaInValidazione);
			sogg.setStatoOperativo(StatoOperativoAnagrafica.VALIDO);
			soggetto.setSoggetto(sogg);
			sogg.setControlloSuSoggetto(false);
			
			//invoco il servizio di aggiornamento:
			response_1 = soggettoService.aggiornaSoggetto(soggetto);
			
			//analizzo il risultato otttenuto:
			if(response_1.getSoggettoAggiornato()!= null && response_1.getSoggettoAggiornato().getSediSecondarie()!= null ){
				model.setListaSecondariaSoggetto(response_1.getSoggettoAggiornato().getSediSecondarie());
			}
		}
		if (response_1 != null) {
			log.debug(methodName, "OK VALIDAZIONE SEDE SECONDARIA");
			addActionMessage("I dati sono stati salvati correttamente. &Egrave; stata validata la sede secondaria");
			return SUCCESS;
		} else {
			addActionError("Validazione sede secondaria non eseguita");
			return INPUT;
		}
		
	}
	
	public String rifiutaSede() throws Exception {
		AnnullaSedeInModificaResponse response = null;
		if (idSedeSecondariaValidazione != null) {

			//Compongo la request per il servizio:
			AnnullaSedeInModifica request = new AnnullaSedeInModifica();
			
			request.setEnte(sessionHandler.getEnte());
			request.setRichiedente(sessionHandler.getRichiedente());
			it.csi.siac.siacfinser.model.SedeSecondariaSoggetto sede = new it.csi.siac.siacfinser.model.SedeSecondariaSoggetto();
			sede.setUid(idSedeSecondariaValidazione);
			request.setSedeDaAggiornare(sede);

			response = soggettoService.annullaSedeInModifica(request);
			
			// Refresh dati del soggetto per variazione sedi secondarie
			RicercaSoggettoPerChiaveResponse responseRefresh = caricaDatiSoggettoDaService();				
			model.setListaSecondariaSoggetto(responseRefresh.getListaSecondariaSoggetto());
			if (responseRefresh.isFallimento()) {
				addActionError("Rifiuto sede secondaria non eseguito");
				return INPUT;
			}				
		}
		if (response != null) {
			log.debug(methodName, "OK RIFIUTO SEDE SECONDARIA");
			addActionMessage("I dati sono stati salvati correttamente. &Egrave; stata rifiutata la sede secondaria");
			return SUCCESS;
		} else {
			addActionError("Rifiuto sede secondaria non eseguito");
			return INPUT;
		}
	}
	
	public String pulisciNuovaSede() throws Exception {
		model.setRecapitoSS(new RecapitoModel());
		model.setIndirizzoSS(new IndirizzoSoggetto());
		model.setNuovaSedeSecondaria(new SedeSecondariaSoggetto());
		model.getNuovaSedeSecondaria().setIndirizzoSoggettoPrincipale(new IndirizzoSoggetto());
		model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().setCodiceNazione(WebAppConstants.CODICE_ITALIA);
		return SUCCESS;
	}
	
	public String pulisciSede() throws Exception {
		pulisciNuovaSede();
		return "modifica";
	}
	
	/**
	 * Cerca la sede con l'id indicato tra quelle in model.listaSecondariaSoggetto
	 * @param toFind
	 * @return
	 */
	private SedeSecondariaSoggetto findById(int toFind){
		//mi riconduco al metodo centralizzato:
		return FinUtility.getById(model.getListaSecondariaSoggetto(), toFind);
	}
	
	public String salvaSede() throws Exception {
		
	   // valorizzo il checkbox
	   model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().setCheckAvviso(valorizzaCheckbox("nuovaSedeSecondaria.indirizzoSoggettoPrincipale.checkAvviso"));	
		
	   List<Errore> listaerrori=  new ArrayList<Errore>();
	   if(model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().getNumeroCivico().isEmpty() || model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().getCap().isEmpty()){
		if(model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().getNumeroCivico()== null || model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().getNumeroCivico().isEmpty()){
			listaerrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Civico"));
		}
		
		if(model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().getCap()== null ||model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().getCap().isEmpty() ){
			listaerrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("CAP"));
		}
		if(listaerrori!= null){
			for(Errore e:listaerrori){
				addErrore(e);
			}
			model.setToggleSedeAperto(true);
			return INPUT;
		}
	   }else{
		if (!validateForm().isEmpty()){
			fromPulsanteInserisciNuova = false;
			model.setToggleSedeAperto(true);
			return INPUT;
		}		
		SedeSecondariaSoggetto sedeFinded = findById(model.getNuovaSedeSecondaria().getUid());
		if(sedeFinded==null && model.getListaSecondariaSoggetto()!=null && model.getListaSecondariaSoggetto().size()>0){
			if(idSedeSecondaria!=null && fromPulsanteInserisciNuova){
				idSedeSecondaria = null;
			}
		}
		List<Contatto> contatti = recapitoContattiNormalizzatore.getModelNormalizzato(model.getRecapitoSS(), Contatto.class);
		// se deseleziono tutti i valori setto cmq a null
		// cosi verra' tenuto nel model il valore vuoto
		model.getNuovaSedeSecondaria().setContatti(contatti);
		if (FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(), ABILITAZIONE_GESTIONE_DECENTRATO)) {
			 // utente decentrato
			model.getNuovaSedeSecondaria().setStatoOperativoSedeSecondaria(StatoOperativoSedeSecondaria.IN_MODIFICA);
			model.getNuovaSedeSecondaria().setDescrizioneStatoOperativoSedeSecondaria(Constanti.STATO_IN_MODIFICA_no_underscore);
			if(sedeFinded==null){
				//SE NON HA UN ID FISICO SUL DB VUOL DIRE CHE E' STATO APPENA INSERITO DURANTE LA SESSIONE IN CORSO 
				//E PUO' SOLO ESSERE PROVVISORIO
				model.getNuovaSedeSecondaria().setStatoOperativoSedeSecondaria(StatoOperativoSedeSecondaria.PROVVISORIO);
				model.getNuovaSedeSecondaria().setDescrizioneStatoOperativoSedeSecondaria(StatoOperativoSedeSecondaria.PROVVISORIO.name());
			} else {
				if(sedeFinded.getStatoOperativoSedeSecondaria().name().equals(StatoOperativoSedeSecondaria.PROVVISORIO.name())){
					//SE GIA ERA PROVVISORIO LO LASCIO PROVVISORIO
					model.getNuovaSedeSecondaria().setStatoOperativoSedeSecondaria(StatoOperativoSedeSecondaria.PROVVISORIO);
					model.getNuovaSedeSecondaria().setDescrizioneStatoOperativoSedeSecondaria(StatoOperativoSedeSecondaria.PROVVISORIO.name());
				}
			}
		}else{
			// utente normale
			model.getNuovaSedeSecondaria().setStatoOperativoSedeSecondaria(StatoOperativoSedeSecondaria.VALIDO); 
			model.getNuovaSedeSecondaria().setDescrizioneStatoOperativoSedeSecondaria(StatoOperativoSedeSecondaria.VALIDO.name());
		}
		
		model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().setPrincipale(Constanti.TRUE);
		
		// setto il check avviso in avviso
		if(model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().isCheckAvviso()){
			model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().setAvviso("true");
		}else{
			model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().setAvviso("false");
		}
		
		if (idSedeSecondaria == null){
			model.getListaSecondariaSoggetto().add(model.getNuovaSedeSecondaria());
			if (FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(), ABILITAZIONE_GESTIONE_DECENTRATO)) {
				 // utente decentrato
				model.getNuovaSedeSecondaria().setStatoOperativoSedeSecondaria(StatoOperativoSedeSecondaria.PROVVISORIO);
				model.getNuovaSedeSecondaria().setDescrizioneStatoOperativoSedeSecondaria(StatoOperativoSedeSecondaria.PROVVISORIO.name());
			}else{
				// utente normale
				model.getNuovaSedeSecondaria().setStatoOperativoSedeSecondaria(StatoOperativoSedeSecondaria.VALIDO);
				model.getNuovaSedeSecondaria().setDescrizioneStatoOperativoSedeSecondaria(StatoOperativoSedeSecondaria.VALIDO.name());
			}
		} else {
			model.getNuovaSedeSecondaria().getIndirizzoSoggettoPrincipale().setIndirizzoId(model.getListaSecondariaSoggetto().get(idSedeSecondaria-1).getIndirizzoSoggettoPrincipale().getIndirizzoId());
			model.getNuovaSedeSecondaria().setModificato(true);
			model.getListaSecondariaSoggetto().set(idSedeSecondaria-1, model.getNuovaSedeSecondaria());
		}
		pulisciNuovaSede();
		
		// gestione inserimento da pagina monca
		if(fromGestisciSede){
			// inserisco il codice tipo persona
			model.getDettaglioSoggetto().getTipoSoggetto().setCodice(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode());
			model.getDettaglioSoggetto().setSediSecondarie(model.getListaSecondariaSoggetto());
			AggiornaSoggettoResponse response = null;
			
			//a seconda se siamo decentrati o meno richiamiamo il servizio
			//opportuno di aggiornamento del soggetto:
			if (FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(), ABILITAZIONE_GESTIONE_DECENTRATO)) {
				//AGGIORNAMENTO PROVVISIORIO
				response = soggettoService.aggiornaSoggettoProvvisorio(new AggiornaSoggettoProvvisorio(convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(),true)));
			}else{
				//AGGIORNAMENTO NORMALE
				response = soggettoService.aggiornaSoggetto(convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(),true));
			}
			//ANALIZZIAMO LA RISPOSTA DEL SERVIZIO:
			if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
				//CI SONO ERRORI
				debug(methodName, "Errore nella risposta del servizio");
				addErrori(methodName, response);
				
				debug(methodName, "Model: " + model);
				fromPulsanteInserisciNuova = false;
				model.setToggleSedeAperto(true);
				return INPUT;
			}
			
			
			model.setListaSecondariaSoggetto(response.getSoggettoAggiornato().getSediSecondarie());
			
		}
		
		fromPulsanteInserisciNuova = false;
	   }
		
		// lancio il servizio salva sede che invoca il WS
	   model.setToggleSedeAperto(false);
	   return salvaGenerale();
	}
	

	/**
	 * Metodo che gestisce l'annullamento di una sede secondaria
	 * @return
	 */
	public String gestioneAnnullaSede() throws Exception {
		
		AggiornaSoggettoResponse response = null;
		
		if (idSedeSecondariaDaEliminare != null) {
			// La sede e' mantenuta con stato ANNULLATO
			SedeSecondariaSoggetto ss = model.getListaSecondariaSoggetto().get(idSedeSecondariaDaEliminare - 1); 
			
			//verifichiamo che non esista
			//una modalita di pagamento associata alla sede:
			if (esisteModalitaPagamentoAssociataSede(ss)) {
				//rilancio errore
				addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(String.format("sede %s associata a una o piu' modalita' di pagamento", ss.getDenominazione())));
				return INPUT;
			}
			
			SedeSecondariaSoggetto ssOrig = new SedeSecondariaSoggetto();
			ssOrig.setStatoOperativoSedeSecondaria(ss.getStatoOperativoSedeSecondaria());
			
			ss.setStatoOperativoSedeSecondaria(StatoOperativoSedeSecondaria.ANNULLATO);
			ss.setDescrizioneStatoOperativoSedeSecondaria(StatoOperativoSedeSecondaria.ANNULLATO.name());
			ss.setDataAnnullamento(new Date());
			
			//PREPARIAMO LA REQUEST PER INVOCARE IL SERVIZIO DI AGGIORNAMENTO:
			AggiornaSoggetto aggiornaSoggetto = convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(),true);
			
			aggiornaSoggetto.getSoggetto().setSediSecondarie(model.getListaSecondariaSoggetto());
			
			//a seconda se siamo decentrati o meno richiamiamo il servizio
			//opportuno di aggiornamento del soggetto:
			if (FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(), ABILITAZIONE_GESTIONE_DECENTRATO)) {
				//AGGIORNAMENTO PROVVISIORIO
				AggiornaSoggettoProvvisorio asp = new AggiornaSoggettoProvvisorio(aggiornaSoggetto);
				response = soggettoService.aggiornaSoggettoProvvisorio(asp);
			}else{
				//AGGIORNAMENTO NORMALE
				response = soggettoService.aggiornaSoggetto(aggiornaSoggetto);
			}
			//ANALIZZIAMO LA RISPOSTA DEL SERVIZIO:
			if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
				//CI SONO ERRORI
				debug(methodName, "Errore nella risposta del servizio");
				for (int i=0;  response.getErrori().size() > i ; i++ ) {
					String errDesc = response.getErrori().get(i).getDescrizione();
					if (ErroreFin.CANCELLAZIONE_SOGGETTO_IMPOSSIBILE.getErrore("Modalita' di pagamento","liquidazioni").getDescrizione().equals(errDesc)) {
						response.getErrori().remove(i);
						response.getErrori().add(i, ErroreFin.ANNULLAMENTO_SOGGETTO_IMPOSSIBILE.getErrore("sede","liquidazioni"));
					} else if (ErroreFin.CANCELLAZIONE_SOGGETTO_IMPOSSIBILE.getErrore("Modalita' di pagamento","ordinativi").getDescrizione().equals(errDesc)) {
						response.getErrori().remove(i);
						response.getErrori().add(i, ErroreFin.ANNULLAMENTO_SOGGETTO_IMPOSSIBILE.getErrore("sede","ordinativi"));
					}
				}
				addErrori(methodName, response);
				
				if (response.getSoggettoAggiornato() != null){
					model.setListaSecondariaSoggetto(response.getSoggettoAggiornato().getSediSecondarie());
				}

				ss.setStatoOperativoSedeSecondaria(ssOrig.getStatoOperativoSedeSecondaria());
				ss.setDescrizioneStatoOperativoSedeSecondaria(ssOrig.getStatoOperativoSedeSecondaria().name());
				ss.setDataAnnullamento(null);

				debug(methodName, "Model: " + model);
				return INPUT;
			}
		}
		
		//AGGIORNO LA LISTA SEDI NEL MODEL:
		model.setListaSecondariaSoggetto(response.getSoggettoAggiornato().getSediSecondarie());
		
		return SUCCESS;
	}
	
	/**
	 * Verifica se nella lista modalita pagamento salvata nel model
	 * ne esiste almeno una associata alla sede indicata
	 * @param ss
	 * @return
	 */
	private boolean esisteModalitaPagamentoAssociataSede(SedeSecondariaSoggetto ss){
		//ciclo sulla lista di modalita' di pagamento:
		for (ModalitaPagamentoSoggetto mdp : model.getListaModalitaPagamentoSoggetto()){
			if (mdp.getAssociatoA().equals(ss.getDenominazione())){
				//appena ne trovo una associata alla sede indicata ritorno true:
				return true;
			}
		}
		//non trovata, ritorno false:
		return false;
	}

	/**
	 * Metodo che gestisce la cancellazione di una sede secondaria
	 * @return
	 */
	public String gestioneEliminaSede() {
		if (idSedeSecondariaDaEliminare != null) {
			
			SedeSecondariaSoggetto ss = model.getListaSecondariaSoggetto().get(idSedeSecondariaDaEliminare - 1); 

			//verifichiamo che non esista
			//una modalita di pagamento associata alla sede:
			if (esisteModalitaPagamentoAssociataSede(ss)){
				//rilancio errore
				addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(String.format("sede %s associata a una o piu' modalita' di pagamento", ss.getDenominazione())));
				return INPUT;
			}
			
			// La sede e' rimossa dalla lista
			model.getListaSecondariaSoggetto().remove(idSedeSecondariaDaEliminare - 1);		
			model.getDettaglioSoggetto().setSediSecondarie(model.getListaSecondariaSoggetto());
			
			//a seconda se siamo decentrati o meno richiamiamo il servizio
			//opportuno di aggiornamento del soggetto:
			AggiornaSoggettoResponse response = null;			
			if (FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(), ABILITAZIONE_GESTIONE_DECENTRATO)) {
				//AGGIORNAMENTO PROVVISIORIO
				AggiornaSoggettoProvvisorio asp = new AggiornaSoggettoProvvisorio(convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(),true));
				response = soggettoService.aggiornaSoggettoProvvisorio(asp);
			}else{
				//AGGIORNAMENTO NORMALE
				response = soggettoService.aggiornaSoggetto(convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(),true));
			}
			//ANALIZZO LA RISPOSTA DEL SERVIZIO:
			if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
				debug(methodName, "Errore nella risposta del servizio");
				addErrori(methodName, response);
				ricaricaSedi();
				debug(methodName, "Model: " + model);
				return INPUT;
			}
			
			//SETTO LA NUOVA LISTA SEDI NEL MODEL:
			model.setListaSecondariaSoggetto(response.getSoggettoAggiornato().getSediSecondarie());
		}
		
		return SUCCESS;
	}

	public String gestioneValidaSede() throws Exception {
		if (idSedeSecondaria != null) {
			selectedSede = model.getListaSecondariaSoggetto().get(idSedeSecondaria - 1);
			
			//istanzio la request per la chiamata al servizio di
			//ricerca della sede secondaria
			RicercaSedeSecondariaPerChiave request = new RicercaSedeSecondariaPerChiave();
			
			//setto i campi di ricerca necessari:
			
			//ENTE
			request.setEnte(sessionHandler.getEnte());
			//RICHIEDENTE
			request.setRichiedente(sessionHandler.getRichiedente());
		    //SOGGETTO
			request.setSoggetto(model.getDettaglioSoggetto());
			//SEDE SECONDARIA SOGGETTO
			request.setSedeSecondariaSoggetto(selectedSede);
			//modificate:
			request.setMod(true);
				
			//invoco il servizio:
			RicercaSedeSecondariaPerChiaveResponse resp = soggettoService.ricercaSedeSecondariaPerChiave(request);
			
			//leggo i dati ottenuti dal servizio:
			selectedSedeMod = resp.getSedeSecondariaSoggetto();
			idSedeSecondariaValidazione = selectedSede.getUid();
		}
		return "valida";
	}
	
	/**
	 * 
	 * @param decodificaOperazione
	 * @param codiceStato
	 * @param utenteUltimaModifica
	 * @return
	 */
	public boolean isAbilitato(Integer decodificaOperazione, String codiceStato, String utenteUltimaModifica, String utenteUltimaModificaMod) {
		boolean abilitato = false;
		boolean gestSogg = FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(), ABILITAZIONE_GESTIONE);
		boolean gestSoggDec = FinUtility.azioneConsentitaIsPresent(sessionHandler.getAzioniConsentite(), ABILITAZIONE_GESTIONE_DECENTRATO);
		String supportStato = (codiceStato != null && stack(codiceStato) != null) ? stack(codiceStato).toString() : null;
		String supportUtenteUltimaModifica = (utenteUltimaModifica != null && stack(utenteUltimaModifica) != null) ? stack(utenteUltimaModifica).toString() : null;
		boolean checkUltimoUtenteModifica = decentratoSuPropriaOccorrenza(supportUtenteUltimaModifica);
		
		if (supportStato != null && !"".equalsIgnoreCase(supportStato) && decodificaOperazione != null) {
			switch (decodificaOperazione) {
			case CONSULTA:
				//CASO CONSULTA
				abilitato = true;
				break;
			case AGGIORNA:
				//CASO AGGIORNA
				if (CodiciStatoSoggetto.VALIDO.toString().equalsIgnoreCase(supportStato)) {
					abilitato = gestSogg || gestSoggDec;
				} else if (CodiciStatoSoggetto.PROVVISORIO.toString().equalsIgnoreCase(supportStato)) {
					abilitato = gestSoggDec && checkUltimoUtenteModifica;
				} else if (CodiciStatoSoggetto.IN_MODIFICA.toString().equalsIgnoreCase(supportStato)) {
					// In questo caso verifico che la modifica da utente decentrato (tabella mod) sia stata fatta dall'utente operante
					abilitato = gestSoggDec && checkUltimoUtenteModifica;
				}
				break;
			case ANNULLA:
				//CASO ANNULLA
				if (CodiciStatoSoggetto.VALIDO.toString().equalsIgnoreCase(supportStato)) {
					abilitato = gestSogg;
				}
				break;
			case CANCELLA:
				//CASO CANCELLA
				if (CodiciStatoSoggetto.VALIDO.toString().equalsIgnoreCase(supportStato)) {
					abilitato = gestSogg;
				} else if (CodiciStatoSoggetto.PROVVISORIO.toString().equalsIgnoreCase(supportStato) || CodiciStatoSoggetto.IN_MODIFICA.toString().equalsIgnoreCase(supportStato)) {
					abilitato = gestSoggDec && checkUltimoUtenteModifica;
				}
				break;
			case VALIDA:
				//CASO VALIDA
				if (CodiciStatoSoggetto.PROVVISORIO.toString().equalsIgnoreCase(supportStato) || CodiciStatoSoggetto.IN_MODIFICA.toString().equalsIgnoreCase(supportStato)) {
					abilitato = gestSogg;
				}
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
		boolean isDecentratoSuPropriaOccorrenza = isUtenteLoggato(utenteUltimaModifica);
		return isDecentratoSuPropriaOccorrenza;
	}
	

	public SedeSecondariaSoggetto getSelectedSede() {
		return selectedSede;
	}
	public SedeSecondariaSoggetto getSelectedSedeMod() {
		return selectedSedeMod;
	}
	
	public Contatto getContatto(String tipo) {
		return getContatto(tipo, false);
	}
	
	public String getContattoStr(String tipo) {
		Contatto c = getContatto(tipo);
		return c.getDescrizione() + (Constanti.TRUE.equals(c.getAvviso()) ? " (recapito per avviso)" : "");
	}
	
	protected Contatto getContatto(String tipo, boolean isMod) {
		List<Contatto> contatti = isMod? selectedSedeMod.getContatti() : selectedSede.getContatti();
		if (contatti != null) {
			for (Contatto c : contatti) if (c.getContattoCodModo().equalsIgnoreCase(tipo)) return c;
		}
		return null;
	}

	public String getDettaglioContatto(String tipo) {
		 return getDettaglioContatto(tipo, false);
	}
	
	public String getDettaglioContatto(String tipo, boolean isMod) {
		Contatto c = getContatto(tipo, isMod);
		if (c != null) {
			String dettaglioContatto = c.getDescrizione();
			if (c.getAvviso() != null && c.getAvviso().equals(Constanti.TRUE)){
				dettaglioContatto += " (recapito per avviso)";
			}
			return dettaglioContatto;
		}
		return null;
	}
	
	public boolean checkSedeInModifica(){
		if(selectedSede.getStatoOperativoSedeSecondaria().toString().equalsIgnoreCase(Constanti.STATO_IN_MODIFICA)){
			return true;
		}else{
			return false;
		}
	}
	
	// GETTER E SETTER:

	public String getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(String soggetto) {
		this.soggetto = soggetto;
	}

	public boolean isFromGestisciSede() {
		return fromGestisciSede;
	}

	public void setFromGestisciSede(boolean fromGestisciSede) {
		this.fromGestisciSede = fromGestisciSede;
	}
	public Integer getIdSedeSecondariaValidazione() {
		return idSedeSecondariaValidazione;
	}
	public void setIdSedeSecondariaValidazione(Integer idSedeSecondariaValidazione) {
		this.idSedeSecondariaValidazione = idSedeSecondariaValidazione;
	}
	public boolean isFromPulsanteInserisciNuova() {
		return fromPulsanteInserisciNuova;
	}
	public void setFromPulsanteInserisciNuova(boolean fromPulsanteInserisciNuova) {
		this.fromPulsanteInserisciNuova = fromPulsanteInserisciNuova;
	}
	public String getCodiceSedeSecondariaInValidazione() {
		return codiceSedeSecondariaInValidazione;
	}
	public void setCodiceSedeSecondariaInValidazione(String codiceSedeSecondariaInValidazione) {
		this.codiceSedeSecondariaInValidazione = codiceSedeSecondariaInValidazione;
	}
	
}

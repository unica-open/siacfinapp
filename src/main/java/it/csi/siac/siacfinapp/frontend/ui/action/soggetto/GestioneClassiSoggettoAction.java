/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.ValidationUtils;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiungiSoggettoAllaClassificazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiungiSoggettoAllaClassificazioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RimuoviSoggettoDaClassificazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.RimuoviSoggettoDaClassificazioneResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggetto;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestioneClassiSoggettoAction extends WizardGestioneClassiSoggettoAction{
	
	private static final long serialVersionUID = 5337260587632331114L;
	
	public boolean statusTabellaSoggettiDellaClasse;
	
	private String uidSoggDaRimuovere;
	private String codiceSoggDaRimuovere;

	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		//caricamento liste:
		caricaListePerRicercaSoggetto();
		//setto il titolo:
		this.model.setTitolo("Gestione Classi Soggetto");
		//settiamo lo status della tabella soggetti:
		setStatusTabellaSoggettiDellaClasse(true);
	}	
	
	/**
	 * gestisce la selezione del soggetto
	 */
	public String selezionaSoggetto() {	
		//metodo nella superclasse:
		String result = super.selezionaSoggetto();
		if (!SUCCESS.equals(result)){
			//esito negativo
			return result;
		}
		//tutto ok:
		daSelezioneClasse = true;
		pulisciSoggettoSelezionatoDaAggiungere = false;
		//andiamo a gestione classi soggetto:
		return GOTO_GESTIONE_CLASSI_SOGGETTO;
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		
		if(!daSelezioneClasse){
			//occorre pulire i campi
			pulisciCampi();
		}
		
		if (model.getIdClasse() != null && !ValidationUtils.RICERCA_VUOTA.equals(model.getIdClasse())) {
			//effettuiamo la ricerca dei soggetti per la classe selezionata
			ricercaSoggettiDellaClasseSelezionata();
		}
		
		//rimandiamo all'execute della superclasse:
	    return super.execute();
	}    
	
	
	@Override
	public boolean isBottoneIndietroDiTipoSecondBtn() {
		return true;
	}
	
	private void pulisciCampi(){
		//settiamo a null id classe:
		model.setIdClasse(null);
	}
	
	public boolean ricercaSoggettiEffettuata() {
		return model.isRicercaSoggettiEffettuata();
	}
	
	/**
	 * METODO MAPPATO SUL CLICK DEL PULSANTE
	 * @return
	 * @throws Exception
	 */
	public String gestisciSelezionata() throws Exception {
		pulisciSoggettoSelezionatoDaAggiungere = true;
		return ricercaSoggettiDellaClasseSelezionata();
	}
	
	/**
	 * Effettua la ricerca dei soggetti della classe selezionata
	 * @return
	 * @throws Exception
	 */
	public String ricercaSoggettiDellaClasseSelezionata() throws Exception {
		
		setMethodName("gestisciSelezionata");
			
		if(pulisciSoggettoSelezionatoDaAggiungere){
			//pulizia dati
			pulisciRicercaSoggettoDaAggiungere();
		}
		
		//istanzio la request per il servizio:
		RicercaSoggettiOttimizzato ricercaSoggetti = convertiModelPerChiamataServizioRicercaOttimizzzato();
		
		//setto il codice ambito:
		ricercaSoggetti.setCodiceAmbito(getCodiceAmbito());
		
		//invoco il servizio:
		RicercaSoggettiOttimizzatoResponse response = soggettoService.ricercaSoggettiOttimizzato(ricercaSoggetti);
		
		//da selezione classe viene posto a true:
		daSelezioneClasse = true;
		
		if(!isFallimento(response)){
			//il servizio non ha dato errori
			sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_SOGGETTI, response.getSoggetti());
			model.setResultSizeSoggettiDellaClasse(response.getNumRisultati());
			model.setRicercaSoggettiEffettuata(true);
		    return GOTO_GESTIONE_CLASSI_SOGGETTO;
		} else {
			//servizio terminato con fallimento
			model.setRicercaSoggettiEffettuata(false);
			addErrori(methodName, response);
			return INPUT;
		}
		
	}
	
	/**
	 * Aggiunta di un soggetto alla classe
	 * @return
	 * @throws Exception
	 */
	public String aggiungiSoggettoAllaClasse() throws Exception {
		
		if(model.getSoggettoSelezionatoDaAggiungere()!=null && !FinStringUtils.isEmpty(model.getSoggettoSelezionatoDaAggiungere().getCodCreditore())){
			
			//istanzio la request per il servizio:
			AggiungiSoggettoAllaClassificazione request = new AggiungiSoggettoAllaClassificazione();
			
			//setto il codice soggetto:
			request.setCodiceSoggetto(model.getSoggettoSelezionatoDaAggiungere().getCodCreditore());
			//setto l'ente:
			request.setEnte(sessionHandler.getEnte());
			//setto il richiedente:
			request.setRichiedente(sessionHandler.getRichiedente());
			
			//vado a pescare ella lista delle classi soggetto quella corrispondente a quella
			//selezionata e salvata in idClasse:
			CodificaFin classeSoggetto = FinUtility.getById(model.getListaClasseSoggetto(), model.getIdClasse());
			
			//codice della classificazione:
			request.setCodiceClassificazione(classeSoggetto.getCodice());
			
			//invoco il servizio:
			AggiungiSoggettoAllaClassificazioneResponse resp = soggettoService.aggiungiSoggettoAllaClassificazione(request);
			
			if(isFallimento(resp)) {
				//esito negativo
				debug(methodName, "Errore nella risposta del servizio");
				addErrori(methodName, resp);
				return INPUT;
			}
			
			//Effettuiamo la ricerca dei soggetti della classe selezionata:
			return ricercaSoggettiDellaClasseSelezionata();
			
		} else {
			//caso in cui l'utente non indica un soggetto
			addActionError("Nessun soggetto indicato");
			return INPUT;
		}
	}
	
	/**
	 * Per rimuovere un soggetto da una classe
	 * @return
	 * @throws Exception
	 */
	public String rimuoviSoggettoDaClasse()  throws Exception {
		
		//istanzio la request per il servizio:
		RimuoviSoggettoDaClassificazione request = new RimuoviSoggettoDaClassificazione();
		
		//setto il codice soggetto:
		request.setCodiceSoggetto(codiceSoggDaRimuovere);
		
		//setto l'ente:
		request.setEnte(sessionHandler.getEnte());
		
		//setto il richiedente:
		request.setRichiedente(sessionHandler.getRichiedente());
		
		//vado a pescare ella lista delle classi soggetto quella corrispondente a quella
		//selezionata e salvata in idClasse:
		CodificaFin classeSoggetto = FinUtility.getById(model.getListaClasseSoggetto(), model.getIdClasse());
		
		//codice della classificazione:
		request.setCodiceClassificazione(classeSoggetto.getCodice());
		
		//invoco il servizio:
		RimuoviSoggettoDaClassificazioneResponse resp = soggettoService.rimuoviSoggettoDaClassificazione(request);
		
		if(isFallimento(resp)) {
			//esito negativo
			debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, resp);
			return INPUT;
		}
		
		//Effettuiamo la ricerca dei soggetti della classe selezionata:
		return ricercaSoggettiDellaClasseSelezionata();
	}
	
	/**
	 * Metodo che costruisce la request per il servizio di ricerca soggetti
	 * a partire dai dati nel model
	 * @return
	 */
	protected RicercaSoggetti convertiModelPerChiamataServizioRicerca() {
		//inizializzo la request:
		RicercaSoggetti ricercaSoggetti = new RicercaSoggetti();
		//setto il richiedente:
		ricercaSoggetti.setRichiedente(sessionHandler.getRichiedente());
		//setto l'ente:
		ricercaSoggetti.setEnte(sessionHandler.getAccount().getEnte());
		
		ParametroRicercaSoggetto parametroRicercaSoggetto = new ParametroRicercaSoggetto();
		
		//chiediamo anche le modifiche:
		parametroRicercaSoggetto.setIncludeModif(true);
		
		if (model.getIdClasse() != null && !ValidationUtils.RICERCA_VUOTA.equals(model.getIdClasse())) {
			//setto la classe
			parametroRicercaSoggetto.setClasse(model.getIdClasse().toString());
		}
			
		ricercaSoggetti.setParametroRicercaSoggetto(parametroRicercaSoggetto);
		
		//paginazione dei risultati:
		addNumAndPageSize(ricercaSoggetti, "ricercaSoggettoID");
		
		return ricercaSoggetti;
	}
	
	/**
	 * Metodo che costruisce la request per il servizio di ricerca soggetti ottimizzato
	 * a partire dai dati nel model
	 * @return
	 */
	protected RicercaSoggettiOttimizzato convertiModelPerChiamataServizioRicercaOttimizzzato() {
		//inizializzo la request:
		RicercaSoggettiOttimizzato ricercaSoggetti = new RicercaSoggettiOttimizzato();
		
		//setto il richiedente:
		ricercaSoggetti.setRichiedente(sessionHandler.getRichiedente());
		
		//setto l'ente:
		ricercaSoggetti.setEnte(sessionHandler.getAccount().getEnte());
		ParametroRicercaSoggetto parametroRicercaSoggetto = new ParametroRicercaSoggetto();
		
		//chiediamo anche le modifiche:
		parametroRicercaSoggetto.setIncludeModif(true);
		
		if (model.getIdClasse() != null && !ValidationUtils.RICERCA_VUOTA.equals(model.getIdClasse())){
			//setto la classe
			parametroRicercaSoggetto.setClasse(model.getIdClasse().toString());
		}
			
		ricercaSoggetti.setParametroRicercaSoggetto(parametroRicercaSoggetto);
		
		//paginazione dei risultati:
		addNumAndPageSize(ricercaSoggetti, "ricercaSoggettoID");
		
		return ricercaSoggetti;
	}
	
	public String annulla() {
		model.setIdClasse(ValidationUtils.RICERCA_VUOTA);
		return SUCCESS;
	}

	public boolean isStatusTabellaSoggettiDellaClasse() {
		return statusTabellaSoggettiDellaClasse;
	}

	public void setStatusTabellaSoggettiDellaClasse(
			boolean statusTabellaSoggettiDellaClasse) {
		this.statusTabellaSoggettiDellaClasse = statusTabellaSoggettiDellaClasse;
	}

	public String getUidSoggDaRimuovere() {
		return uidSoggDaRimuovere;
	}

	public void setUidSoggDaRimuovere(String uidSoggDaRimuovere) {
		this.uidSoggDaRimuovere = uidSoggDaRimuovere;
	}

	public String getCodiceSoggDaRimuovere() {
		return codiceSoggDaRimuovere;
	}

	public void setCodiceSoggDaRimuovere(String codiceSoggDaRimuovere) {
		this.codiceSoggDaRimuovere = codiceSoggDaRimuovere;
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccommonapp.util.exception.ApplicationException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.GestionePrimaNotaIntegrataBaseModel;
import it.csi.siac.siacgenser.frontend.webservice.RegistrazioneMovFinService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Classe base di action per la gestione della prima nota integrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/10/2015
 * 
 * @param <M> la tipizzazione del model
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestionePrimaNotaIntegrataBaseAction<M extends GestionePrimaNotaIntegrataBaseModel> extends GenericContabilitaGeneraleAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -3130350115511899616L;
	
	@Autowired private transient RegistrazioneMovFinService registrazioneMovFinService;
	
	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		
		checkCasoDUsoApplicabile();
		// TODO verifica nome uid da usare nel model
		// In inserimento arrivo da registrazione
		RicercaDettaglioRegistrazioneMovFin request = model.creaRequestRicercaDettaglioRegistrazioneMovFin(model.getUidRegistrazione());
		logServiceRequest(request);
		
		RicercaDettaglioRegistrazioneMovFinResponse response = registrazioneMovFinService.ricercaDettaglioRegistrazioneMovFin(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioRegistrazioneMovFin.class, response));
			addErrori(response);
			return INPUT;
		}
		if (response.getRegistrazioneMovFin() == null) {
			log.info(methodName, "RegistrazioneMovFin con uid " + model.getUidRegistrazione() + " non presente");
			addErrore(ErroreCore.ENTITA_INESISTENTE.getErrore("Registrazione movimento finanziario", model.getUidRegistrazione()));
			return INPUT;
		}
		
		RegistrazioneMovFin regMovFin = response.getRegistrazioneMovFin();
		sessionHandler.setParametro(FinSessionParameter.REGISTRAZIONEMOVFIN, response.getRegistrazioneMovFin());
		String nomeAzioneRedirezione = componiStringaRedirezione(regMovFin);
		log.debug(methodName, "registrazione ottenuta: " + nomeAzioneRedirezione);
		model.setNomeAzioneRedirezione(nomeAzioneRedirezione);
		return SUCCESS;
	}

	/**
	 * Compone la stringa di redirezione.
	 * <br/>
	 * Il risultato sar&agrave; una stringa del tipo <pre>completa%VALIDA%%MODALITA%%TIPOCOLLEGAMENTO%PrimaNotaIntegrata</pre>.
	 * E.g.:
	 * <ul>
	 *     <li>completaValidaLiquidazioneInsPrimaNotaIntegrata</li>
	 *     <li>completaLiquidazioneAggPrimaNotaIntegrata</li>
	 * </ul>
	 * @param registrazioneMovFin la registrazione da cui ottenere l'azione
	 * @return il nome della redirezione da effettuare
	 */
	private String componiStringaRedirezione(RegistrazioneMovFin registrazioneMovFin) {
		StringBuilder result = new StringBuilder("completa");
		if (model.isValidazione()) {
			result.append("Valida");
		}
		//result.append(getTipologiaRegistrazione(registrazioneMovFin.getEvento().getTipoCollegamento()));
		
		result.append(registrazioneMovFin.getMovimento().getClass().getSimpleName());
		
		if(registrazioneMovFin.getMovimentoCollegato()!=null){
			result.append(registrazioneMovFin.getMovimentoCollegato().getClass().getSimpleName());
		}
		
		
		// TODO verificare se aggiornamento tramite la ricerca
		result.append("Ins");
		result.append("PrimaNotaIntegrata");
		// Lotto N
		// Aggiungo l'ambito
		String ambitoSuffix = model.getAmbito().getSuffix();
		result.append(ambitoSuffix);
		if(model.isFromCDUDocumento()) {
			result.append("daCDUDocumento");
		}

		return result.toString();
	}

	private void checkCasoDUsoApplicabile() throws ApplicationException {
	
		if(!model.isEnteAbilitatoGestionePrimaNotaDaFinanziaria()) {
			throw new ApplicationException(); 
		}
		
	}
}

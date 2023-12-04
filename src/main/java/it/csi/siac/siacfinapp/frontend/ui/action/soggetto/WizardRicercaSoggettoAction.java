/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;

import java.util.ArrayList;

import xyz.timedrain.arianna.plugin.BreadCrumb;

import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.RicercaElencoSoggettoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.ValidationUtils;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiResponse;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;


public class WizardRicercaSoggettoAction extends SoggettoAction<RicercaElencoSoggettoModel> {

	
	private static final long serialVersionUID = 1L;

	@Override
	public String getActionKey() {
		//action key di pagina
		return "ricercaSoggetto";
	}

	@BreadCrumb("%{model.titolo}")
	public String doExecute() throws Exception {
		//rimando all'execute della super classe:
		return execute();
	}
	
	/**
	 * Metodo che converte il model utilizzato per il binding della pagina web,
	 * nel dto utilizzato nel servizio di Ricerca Soggetto
	 * @return RicercaSoggetti
	 */
	protected RicercaSoggetti convertiModelPerChiamataServizioRicerca() {
		
		//istanzio la request per il servizio di ricerca soggetti:
		RicercaSoggetti ricercaSoggetti = new RicercaSoggetti();
		
		//setto i dati opportuni nella request:
		
		//RICHIEDENTE
		ricercaSoggetti.setRichiedente(sessionHandler.getRichiedente());
		
		//ENTE
		ricercaSoggetti.setEnte(sessionHandler.getAccount().getEnte());
		
		//istanzio il parametro di ricerca soggetto:
		ParametroRicercaSoggetto parametroRicercaSoggetto = new ParametroRicercaSoggetto();
		
		//richiedo anche le modifiche:
		parametroRicercaSoggetto.setIncludeModif(true);
		
		if (model.getCodice() != null) {
			//avendo il codice soggetto la ricerca e' per chiave tramite tala codice:
			parametroRicercaSoggetto.setCodiceSoggetto(model.getCodice().toString());
		}else{
			//non avendo il codice soggetto la ricerca si basa
			//su diversi parametri, e possono essere trovati piu soggetti:
			
			//codice fiscale
			parametroRicercaSoggetto.setCodiceFiscale(model.getCodiceFiscale());
			
			//codice fiscale estero
			parametroRicercaSoggetto.setCodiceFiscaleEstero(model.getCodiceFiscaleEstero());
			
			//partita iva
			parametroRicercaSoggetto.setPartitaIva(model.getPartitaIva());
			
			//denominazione
			parametroRicercaSoggetto.setDenominazione(model.getDenominazione());
			
//			SIAC-6565-CR1215
			//Email Pec
			parametroRicercaSoggetto.setEmailPec(model.getEmailPec());
			
			//Cod Destinatario
			parametroRicercaSoggetto.setCodDestinatario(model.getCodDescrizione());
			
			//stato soggetto
			if (model.getIdStato() != null && !ValidationUtils.RICERCA_VUOTA.equals(model.getIdStato())) {
				parametroRicercaSoggetto.setIdStatoSoggetto(model.getIdStato().toString());
			}
			
			//titolo natura giuridica
			if (model.getIdTipoNatura() != null && !ValidationUtils.RICERCA_VUOTA.equals(model.getIdTipoNatura())) {
				parametroRicercaSoggetto.setTitoloNaturaGiuridica(model.getIdTipoNatura().toString());
			}
			
			//forma giuridica
			if (model.getIdNaturaGiuridica() != null && !ValidationUtils.RICERCA_VUOTA.equals(model.getIdNaturaGiuridica())) {
				parametroRicercaSoggetto.setFormaGiuridica(model.getIdNaturaGiuridica().toString());
			}
			
			//sesso
			parametroRicercaSoggetto.setSesso(model.getSesso());
			
			//comune nascita
			parametroRicercaSoggetto.setComuneNascita(model.getIdComune());
			
			//classe
			if (model.getIdClasse() != null && !ValidationUtils.RICERCA_VUOTA.equals(model.getIdClasse())) {
				parametroRicercaSoggetto.setClasse(model.getIdClasse().toString());
			}
			
			//nazione
			if (model.getIdNazione()!=null){
				parametroRicercaSoggetto.setIdNazione(String.valueOf(model.getIdNazione()));
			}
			
		}
		
		//setto il parametro nella request:
		ricercaSoggetti.setParametroRicercaSoggetto(parametroRicercaSoggetto);
		
		//paginazione risultati:
		addNumAndPageSize(ricercaSoggetti, "ricercaSoggettoID");
		
		//termino restituendo la request al chiamante:
		return ricercaSoggetti;
	}
	
	protected String executeRicerca() {
		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_SOGGETTI, new ArrayList<Soggetto>());
		model.setResultSize(0);

		//istanzio la request per il servizio ricercaSoggetti:
		RicercaSoggetti ricercaSoggetti = convertiModelPerChiamataServizioRicerca();
		
		ricercaSoggetti.setCodiceAmbito(getCodiceAmbito());
		//invoco il servizio ricercaSoggetti:
		RicercaSoggettiResponse response = soggettoService.ricercaSoggetti(ricercaSoggetti);
		//Controllo che il servizio non restituisca errori
		if(response.isFallimento()) {
			addErrori(methodName, response);
			return INPUT;
		}		
		
		//Metto in sessione la lista ricevuta per utilizzarla in Elenco Soggetti
		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_SOGGETTI, response.getSoggetti());
		model.setResultSize(response.getNumRisultati());
		
	    return GOTO_ELENCO_SOGGETTI;
	}

	protected String getCodiceAmbito() {
		return null;
	}
	
}

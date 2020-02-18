/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.CollegaSoggettiModel;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLegameSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLegameSoggettiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLegameSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLegameSoggettiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.codifiche.TipoRelazioneSoggetto;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CollegaSoggettiAction extends SoggettoAction<CollegaSoggettiModel> {

	private static final long serialVersionUID = 5390265527775822659L;
	
	private String soggetto;
	private String radioCodiceSoggetto;
	private String tipoLegame;
	private String soggettoPrecedenteDaAnnullare;
	private String azioneDaEseguire;
	
	@Override
	public String getActionKey() {
		return "collegaSoggetti";
	}
	/**
	 * prepare della action
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		//carico le liste:
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.TIPO_LEGAME_SOGGETTO);
		model.setListaTipoLegame((List<CodificaFin>) mappaListe.get(TipiLista.TIPO_LEGAME_SOGGETTO));
	}	

	/**
	 * execute della action
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		//l'azione puo' essere annulla legame o annulla ricerca:
		if(getAzioneDaEseguire() != null && getAzioneDaEseguire().indexOf("soggettoPrecedenteDaAnnullare") != -1) {
			setSoggettoPrecedenteDaAnnullare(getAzioneDaEseguire().substring(getAzioneDaEseguire().indexOf("soggettoPrecedenteDaAnnullare") + 30));
			annullaLegame();
		} else {
			annullaRicerca();
		}		
		//ricarico il soggetto:
		caricaSoggettoCorrente();
		return SUCCESS;
	}

	/**
	 * Metodo che permette il ricaricamento della pagina
	 */
	private void caricaSoggettoCorrente() {
		
		//inizializzo la request per il servizio di ricerca soggetti:
		RicercaSoggetti ricercaSoggetti = new RicercaSoggetti();
		
		//setto i dati di ricerca:
		ricercaSoggetti.setRichiedente(sessionHandler.getRichiedente());
		ricercaSoggetti.setEnte(sessionHandler.getAccount().getEnte());
		
		ParametroRicercaSoggetto parametroRicercaSoggetto = new ParametroRicercaSoggetto();
		
		parametroRicercaSoggetto.setIncludeModif(true);
		
		if (getSoggetto() != null && !"".equalsIgnoreCase(getSoggetto())) {
			parametroRicercaSoggetto.setCodiceSoggetto(getSoggetto());
		} else {
			parametroRicercaSoggetto.setCodiceSoggetto(model.getSoggettoCorrente().getCodiceSoggetto());
		}
		ricercaSoggetti.setParametroRicercaSoggetto(parametroRicercaSoggetto);	
		
		//invocazione del servizio:
		RicercaSoggettiResponse response = soggettoService.ricercaSoggetti(ricercaSoggetti);
		
		//analisi della response del servizio:
	    if (response != null && response.getSoggetti() != null && response.getSoggetti().size() > 0) {
	    	model.setSoggettiSuccessivi(response.getSoggetti().get(0).getElencoSoggettiSuccessivi());
	    	model.setSoggettiPrecedenti(response.getSoggetti().get(0).getElencoSoggettiPrecedenti());	    	
	    	model.setSoggettoCorrente(response.getSoggetti().get(0));
	    	
	    }
	}
	
	/**
	 * Metodo che pulisce il pannello di ricerca
	 * @return String
	 */
	public String annullaRicerca() {
		model.setSoggettiRicerca(null);
		model.setCodice(null);
		model.setCodiceFiscale(null);
		model.setDenominazione(null);
		model.setIva(null);
		return SUCCESS;
	}
	
	/**
	 * Metodo che effettua la ricerca dei soggetti
	 * @return String
	 */
	public String cerca() {
		
		//inizializzo la request per il servizio di ricerca soggetti:
		RicercaSoggetti ricercaSoggetti = new RicercaSoggetti();
		
		//setto i dati di ricerca:
		ricercaSoggetti.setRichiedente(sessionHandler.getRichiedente());
		ricercaSoggetti.setEnte(sessionHandler.getAccount().getEnte());
		ParametroRicercaSoggetto parametroRicercaSoggetto = new ParametroRicercaSoggetto();
		parametroRicercaSoggetto.setCodiceSoggetto(model.getCodice());
		parametroRicercaSoggetto.setCodiceFiscale(model.getCodiceFiscale());
		parametroRicercaSoggetto.setPartitaIva(model.getIva());
		parametroRicercaSoggetto.setDenominazione(model.getDenominazione());
		ricercaSoggetti.setParametroRicercaSoggetto(parametroRicercaSoggetto);
		
		//invocazione del servizio:
		RicercaSoggettiResponse response = soggettoService.ricercaSoggetti(ricercaSoggetti);
		
		//analisi della response del servizio:
		if(response.isFallimento()) {
			addErrori(methodName, response);
			return INPUT;
		} else {
			model.setSoggettiRicerca(response.getSoggetti());
		}
		return SUCCESS;
	}
	
	/**
	 * Metodo che permette di creare un nuovo legame relativo al soggetto selezionato
	 * @return String
	 */
	public String creaLegame() {
		if(StringUtils.isEmpty(getRadioCodiceSoggetto())){
			//Se non viene selezionato alcun soggetto, mostro l'errore
			List<Errore> listaErrori = new ArrayList<Errore>();
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto"));
			addErrori(listaErrori);
			return INPUT;
		} else {
			//CREO EFFETTIVAMENTE IL LEGAME
			AggiornaLegameSoggetti request = new AggiornaLegameSoggetti();
			request.setEnte(sessionHandler.getEnte());
			request.setRichiedente(sessionHandler.getRichiedente());
			request.setTipoLegame(TipoRelazioneSoggetto.valueOf(tipoLegame));			
			request.setSoggettoCorrente(model.getSoggettoCorrente());
			request.setSoggettoPrecedente(new Soggetto());			
			request.getSoggettoPrecedente().setCodiceSoggetto(getRadioCodiceSoggetto());
			
			//invoco il servizio che si occupa di aggiornare i legami:
			AggiornaLegameSoggettiResponse response = soggettoService.aggiornaLegameSoggetti(request);	
			
			//analizzo l'esito:
			if (response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
				addErrori(methodName, response);
				return INPUT;
			} else {
				annullaRicerca();
				caricaSoggettoCorrente();
			}
		}
		return SUCCESS;
	}
	
	/**
	 * Metodo che permette di annullare un legame tra i soggetti coinvolti
	 * @return String
	 */
	public String annullaLegame() {
		
		//compongo la request per il servizio di annullamento legame:
		AnnullaLegameSoggetti request = new AnnullaLegameSoggetti();
		request.setEnte(sessionHandler.getEnte());
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setTipoLegame(TipoRelazioneSoggetto.valueOf(tipoLegame));
		request.setSoggettoCorrente(model.getSoggettoCorrente());
		request.setSoggettoPrecedente(new Soggetto());
		request.getSoggettoPrecedente().setCodiceSoggetto(getSoggettoPrecedenteDaAnnullare());
		
		//invoco il servizio annullaLegameSoggetti:
		AnnullaLegameSoggettiResponse response = soggettoService.annullaLegameSoggetti(request);
		
		//analizzo l'esito:
		if (response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
			addErrori(methodName, response);
			return INPUT;
		} else {
			caricaSoggettoCorrente();
		}
		return SUCCESS;
	}

	//GETTER e SETTER:
	
	public String getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(String soggetto) {
		this.soggetto = soggetto;
	}

	public String getRadioCodiceSoggetto() {
		return radioCodiceSoggetto;
	}

	public void setRadioCodiceSoggetto(String radioCodiceSoggetto) {
		this.radioCodiceSoggetto = radioCodiceSoggetto;
	}

	public String getTipoLegame() {
		return tipoLegame;
	}

	public void setTipoLegame(String tipoLegame) {
		this.tipoLegame = tipoLegame;
	}

	public String getSoggettoPrecedenteDaAnnullare() {
		return soggettoPrecedenteDaAnnullare;
	}

	public void setSoggettoPrecedenteDaAnnullare(
			String soggettoPrecedenteDaAnnullare) {
		this.soggettoPrecedenteDaAnnullare = soggettoPrecedenteDaAnnullare;
	}

	public String getAzioneDaEseguire() {
		return azioneDaEseguire;
	}

	public void setAzioneDaEseguire(String azioneDaEseguire) {
		this.azioneDaEseguire = azioneDaEseguire;
	}
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.soggetto;

import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.commons.SoggettoModel;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class CollegaSoggettiModel extends SoggettoModel {

	private static final long serialVersionUID = 1113952977517768741L;
	
	//lista successivi:
	private List<Soggetto> soggettiSuccessivi;
	
	//lista precedenti:
	private List<Soggetto> soggettiPrecedenti;
	
	//soggetto corrente:
	private Soggetto soggettoCorrente;
	
	//lista soggetti ricerca:
	private List<Soggetto> soggettiRicerca;
	
	//altri dati:
	private String codice, denominazione, codiceFiscale, iva;

	public CollegaSoggettiModel(){
		super();
		setTitolo("Collega Soggetti");
	}

	public List<Soggetto> getSoggettiSuccessivi() {
		return soggettiSuccessivi;
	}

	public void setSoggettiSuccessivi(List<Soggetto> soggettiSuccessivi) {
		this.soggettiSuccessivi = soggettiSuccessivi;
	}

	public List<Soggetto> getSoggettiPrecedenti() {
		return soggettiPrecedenti;
	}

	public void setSoggettiPrecedenti(List<Soggetto> soggettiPrecedenti) {
		this.soggettiPrecedenti = soggettiPrecedenti;
	}

	public Soggetto getSoggettoCorrente() {
		return soggettoCorrente;
	}

	public void setSoggettoCorrente(Soggetto soggettoCorrente) {
		this.soggettoCorrente = soggettoCorrente;
	}

	public List<Soggetto> getSoggettiRicerca() {
		return soggettiRicerca;
	}

	public void setSoggettiRicerca(List<Soggetto> soggettiRicerca) {
		this.soggettiRicerca = soggettiRicerca;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getIva() {
		return iva;
	}

	public void setIva(String iva) {
		this.iva = iva;
	}
	
}

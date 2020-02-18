/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.soggetto;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Model per la gestione dell'aggiornamento di un soggetto
 */
public class AggiornaSoggettoModel extends InserisciSoggettoModel{
	
	private static final long serialVersionUID = 1L;
	
	//soggetto principale:
	private Soggetto dettaglioSoggetto;
	
	//liste:
	private List<SedeSecondariaSoggetto> listaSecondariaSoggetto;
	private List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto;
	
	//sede secondaria:
	private SedeSecondariaSoggetto sedeSecondariaSoggetto;
	
	//Model per la gestione del recapito:
	private RecapitoModel recapitoSS;
	
	//indirizzo:
	private IndirizzoSoggetto indirizzoSS;
	
	//nuova sede:
	private SedeSecondariaSoggetto nuovaSedeSecondaria;

	//flag per gestione:
	private boolean modelCaricato, toggleSedeAperto = false;
	
	//soggetto per annulla:
	private Soggetto soggettoPerAnnulla;

	
	public AggiornaSoggettoModel(){
		setTitolo("Aggiorna soggetto");
	}

	public Soggetto getDettaglioSoggetto() {
		return dettaglioSoggetto;
	}

	public void setDettaglioSoggetto(Soggetto dettaglioSoggetto) {
		this.dettaglioSoggetto = dettaglioSoggetto;
	}

	public void setListaSecondariaSoggetto(List<SedeSecondariaSoggetto> listaSecondariaSoggetto) {
		this.listaSecondariaSoggetto = listaSecondariaSoggetto;
	}

	public void setListaModalitaPagamentoSoggetto(List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto) {
		this.listaModalitaPagamentoSoggetto = listaModalitaPagamentoSoggetto;
	}

	public List<SedeSecondariaSoggetto> getListaSecondariaSoggetto() {
		if (listaSecondariaSoggetto == null) listaSecondariaSoggetto = new ArrayList<SedeSecondariaSoggetto>(1);
		return listaSecondariaSoggetto;
	}

	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoSoggetto() {
		return listaModalitaPagamentoSoggetto;
	}

	public SedeSecondariaSoggetto getSedeSecondariaSoggetto() {
		return sedeSecondariaSoggetto;
	}

	public void setSedeSecondariaSoggetto(SedeSecondariaSoggetto sedeSecondariaSoggetto) {
		this.sedeSecondariaSoggetto = sedeSecondariaSoggetto;
	}

	public boolean isModelCaricato() {
		return modelCaricato;
	}

	public void setModelCaricato(boolean modelCaricato) {
		this.modelCaricato = modelCaricato;
	}

	public RecapitoModel getRecapitoSS() {
		return recapitoSS;
	}

	public void setRecapitoSS(RecapitoModel recapitoSS) {
		this.recapitoSS = recapitoSS;
	}
	
	public IndirizzoSoggetto getIndirizzoSS() {
		return indirizzoSS;
	}

	public void setIndirizzoSS(IndirizzoSoggetto indirizzoSS) {
		this.indirizzoSS = indirizzoSS;
	}

	public SedeSecondariaSoggetto getNuovaSedeSecondaria() {
		return nuovaSedeSecondaria;
	}

	public void setNuovaSedeSecondaria(SedeSecondariaSoggetto nuovaSedeSecondaria) {
		this.nuovaSedeSecondaria = nuovaSedeSecondaria;
	}

	public boolean isToggleSedeAperto() {
		return toggleSedeAperto;
	}

	public void setToggleSedeAperto(boolean toggleSedeAperto) {
		this.toggleSedeAperto = toggleSedeAperto;
	}

	public Soggetto getSoggettoPerAnnulla() {
		return soggettoPerAnnulla;
	}

	public void setSoggettoPerAnnulla(Soggetto soggettoPerAnnulla) {
		this.soggettoPerAnnulla = soggettoPerAnnulla;
	}

	
}

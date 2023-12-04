/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.soggetto;

import java.util.HashMap;

import it.csi.siac.siacfinapp.frontend.ui.model.commons.SoggettoModel;
import it.csi.siac.siacfinser.model.soggetto.Contatto;


public class InserisciContattiModel extends SoggettoModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2379201776356276005L;

	//model dell'indirizzo
	private IndirizzoModel indirizzo;
	
	//mappa per gli indirizzi
	private HashMap<String, IndirizzoModel> indirizzi;

	//recapito model
	private RecapitoModel recapito;
	
	//mappa dei recapiti
	private HashMap<String, Contatto> recapiti;
	
	public InserisciContattiModel(){
		super();
		setTitolo("Inserimento recapiti");
	}
	
	public IndirizzoModel getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(IndirizzoModel indirizzo) {
		this.indirizzo = indirizzo;
	}

	public HashMap<String, IndirizzoModel> getIndirizzi() {
		return indirizzi;
	}

	public void setIndirizzi(HashMap<String, IndirizzoModel> indirizzi) {
		this.indirizzi = indirizzi;
	}

	public RecapitoModel getRecapito() {
		return recapito;
	}

	public void setRecapito(RecapitoModel recapito) {
		this.recapito = recapito;
	}

	public HashMap<String, Contatto> getRecapiti() {
		return recapiti;
	}

	public void setRecapiti(HashMap<String, Contatto> recapiti) {
		if (this.recapiti == null) this.recapiti = new HashMap<String, Contatto>();
		this.recapiti.putAll(recapiti);
	}
	
}

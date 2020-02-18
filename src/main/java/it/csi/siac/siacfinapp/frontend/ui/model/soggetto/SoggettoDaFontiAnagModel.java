/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.soggetto;

import it.csi.siac.siacfinapp.frontend.ui.model.commons.SoggettoModel;

public class SoggettoDaFontiAnagModel extends SoggettoModel{

	private static final long serialVersionUID = -7945867595459512050L;
	
	//id
	private String idSoggetto;
	
	//fonte
	private String fonte;
	
	//tipo
	private String tipo;
	
	//cod fisc
	private String codiceFiscale;
	
	//descrizione
	private String descrizione;
	
	//indirizzo
	private String indirizzo;
	
	
	public String getIdSoggetto() {
		return idSoggetto;
	}
	public void setIdSoggetto(String idSoggetto) {
		this.idSoggetto = idSoggetto;
	}
	public String getFonte() {
		return fonte;
	}
	public void setFonte(String fonte) {
		this.fonte = fonte;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

}

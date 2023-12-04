/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.soggetto;

import it.csi.siac.siacfinapp.frontend.ui.model.commons.SoggettoModel;

public class RecapitoModel extends SoggettoModel{
	
	private static final long serialVersionUID = -8906938173870575032L;
	
	//numero telefono
	private String numeroTelefono;
	
	//cellulare
	private String numeroCellulare;
	
	//fax
	private String numeroFax;
	
	//pec
	private String pec;
	
	//email
	private String email;
	
	//sito
	private String sitoWeb;
	
	//check avviso pec
	private String checkAvvisoPec;
	
	//check avviso email
	private String checkAvvisoEmail;
	
	//contatto
	private String contatto;
	
	
	// visualizzazione lista
	private String tipo;
	private String datoEsteso;
	private String avviso;
	
	

	
	//GETTER E SETTER:
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getAvviso() {
		return avviso;
	}
	public void setAvviso(String avviso) {
		this.avviso = avviso;
	}
	public String getNumeroTelefono() {
		return numeroTelefono;
	}
	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}
	public String getNumeroCellulare() {
		return numeroCellulare;
	}
	public void setNumeroCellulare(String numeroCellulare) {
		this.numeroCellulare = numeroCellulare;
	}
	public String getNumeroFax() {
		return numeroFax;
	}
	public void setNumeroFax(String numeroFax) {
		this.numeroFax = numeroFax;
	}
	public String getPec() {
		return pec;
	}
	public void setPec(String pec) {
		this.pec = pec;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSitoWeb() {
		return sitoWeb;
	}
	public void setSitoWeb(String sitoWeb) {
		this.sitoWeb = sitoWeb;
	}
	public String getCheckAvvisoPec() {
		return checkAvvisoPec;
	}
	public void setCheckAvvisoPec(String checkAvvisoPec) {
		this.checkAvvisoPec = checkAvvisoPec;
	}
	public String getCheckAvvisoEmail() {
		return checkAvvisoEmail;
	}
	public void setCheckAvvisoEmail(String checkAvvisoEmail) {
		this.checkAvvisoEmail = checkAvvisoEmail;
	}
	public String getDatoEsteso() {
		return datoEsteso;
	}
	public void setDatoEsteso(String datoEsteso) {
		this.datoEsteso = datoEsteso;
	}
	public String getContatto() {
		return contatto;
	}
	public void setContatto(String contatto) {
		this.contatto = contatto;
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.soggetto;

import it.csi.siac.siacfinapp.frontend.ui.model.commons.SoggettoModel;


public class IndirizzoModel  extends SoggettoModel{
	
	private static final long serialVersionUID = 2616367569024270151L;
	
	//id indirizzo
	private String idIndirizzo;
	
	//tipo indirizzo
	private String tipoIndirizzo;
	
	//tipo indizzo desc
	private String tipoIndizzoDesc;
	
	//indirizzo esteso
	private String indirizzoEsteso;
	
	//principale
	private String principale;
	
	//avviso
	private String avviso;
	
	//sedime
	private String sedime;
	
	//nome via
	private String nomeVia;
	
	//numero civico
	private String numeroCivico;
	
	//stato
	private String stato;
	
	//nome nazione
	private String nomeNazione;
	
	//comune
	private String comune;
	
	//codice istat comune
	private String codiceIstatComune;
	
	private Integer comuneUid;
	
	//sigla provincia
	private String siglaProvincia;
	
	//CAP:
	private String cap;
	
	//check avviso
	private boolean checkAvviso;
	
	//check principale
	private boolean checkPrincipale;
	
	
	//GETTER E SETTER:
	
	public String getNomeNazione() {
		return nomeNazione;
	}

	public void setNomeNazione(String nomeNazione) {
		this.nomeNazione = nomeNazione;
	}

	public String getSiglaProvincia() {
		return siglaProvincia;
	}

	public void setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
	}

	public String getPrincipale() {
		return principale;
	}
	public void setPrincipale(String principale) {
		this.principale = principale;
	}
	public String getAvviso() {
		return avviso;
	}
	public void setAvviso(String avviso) {
		this.avviso = avviso;
	}
	public String getTipoIndirizzo() {
		return tipoIndirizzo;
	}
	public void setTipoIndirizzo(String tipoIndirizzo) {
		this.tipoIndirizzo = tipoIndirizzo;
	}
	public String getIndirizzoEsteso() {
		return indirizzoEsteso;
	}
	public void setIndirizzoEsteso(String indirizzoEsteso) {
		this.indirizzoEsteso = indirizzoEsteso;
	}
	public String getSedime() {
		return sedime;
	}
	public void setSedime(String sedime) {
		this.sedime = sedime;
	}
	public String getNomeVia() {
		return nomeVia;
	}
	public void setNomeVia(String nomeVia) {
		this.nomeVia = nomeVia;
	}
	public String getNumeroCivico() {
		return numeroCivico;
	}
	public void setNumeroCivico(String numeroCivico) {
		this.numeroCivico = numeroCivico;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
	}
	public String getCap() {
		return cap;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
	public boolean isCheckAvviso() {
		return checkAvviso;
	}
	public void setCheckAvviso(boolean checkAvviso) {
		this.checkAvviso = checkAvviso;
	}
	public boolean isCheckPrincipale() {
		return checkPrincipale;
	}
	public void setCheckPrincipale(boolean checkPrincipale) {
		this.checkPrincipale = checkPrincipale;
	}
	public String getIdIndirizzo() {
		return idIndirizzo;
	}
	public void setIdIndirizzo(String idIndirizzo) {
		this.idIndirizzo = idIndirizzo;
	}
	public String getTipoIndizzoDesc() {
		return tipoIndizzoDesc;
	}
	public void setTipoIndizzoDesc(String tipoIndizzoDesc) {
		this.tipoIndizzoDesc = tipoIndizzoDesc;
	}
	public String getCodiceIstatComune() {
		return codiceIstatComune;
	}
	public void setCodiceIstatComune(String codiceIstatComune) {
		this.codiceIstatComune = codiceIstatComune;
	}

	/**
	 * @return the comuneUid
	 */
	public Integer getComuneUid() {
		return comuneUid;
	}

	/**
	 * @param comuneUid the comuneUid to set
	 */
	public void setComuneUid(Integer comuneUid) {
		this.comuneUid = comuneUid;
	}
	

}

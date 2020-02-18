/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.soggetto;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.commons.SoggettoModel;

public class GenericoRicercaSoggettoModel extends SoggettoModel {

	private static final long serialVersionUID = 4943508575792862074L;
	
	public GenericoRicercaSoggettoModel(){
		//richiamo il costruttore della super classe
		super();
	}
	
	//codice
	private Integer codice;
	
	//codice fiscale
	private String codiceFiscale;
	
	//partita iva
	private String partitaIva;
	
	//denominazione
	private String denominazione;
	
//	SIAC-6565-CR1215
	private String emailPec;
	private String codDescrizione;
	
	//stato
	private Integer idStato;
	
	//natura
	private Integer idTipoNatura;
	private Integer idNaturaGiuridica;
	
	
	//sesso
	private String sesso;
	private List<String> listaSesso = new ArrayList<String>();	
	
	//luogo nascita
	private Integer idNazione;
	private String comune;
	private String idComune;
	private String provinciaNascita;
	private Integer idClasse;
	
	
	//GETTER E SETTER:
	
	public Integer getCodice() {
		return codice;
	}
	public void setCodice(Integer codice) {
		this.codice = codice;
	}
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	
	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}
	
	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	
	public Integer getIdStato() {
		return idStato;
	}

	public void setIdStato(Integer idStato) {
		this.idStato = idStato;
	}
	
	public Integer getIdNaturaGiuridica() {
		return idNaturaGiuridica;
	}

	public void setIdNaturaGiuridica(Integer idNaturaGiuridica) {
		this.idNaturaGiuridica = idNaturaGiuridica;
	}
	
	public Integer getIdTipoNatura() {
		return idTipoNatura;
	}

	public void setIdTipoNatura(Integer idTipoNatura) {
		this.idTipoNatura = idTipoNatura;
	}
	public String getSesso() {
		return sesso;
	}
	public void setSesso(String sesso) {
		this.sesso = sesso;
	}
	public List<String> getListaSesso() {
		return listaSesso;
	}
	public void setListaSesso(List<String> listaSesso) {
		this.listaSesso = listaSesso;
	}
	public Integer getIdNazione() {
		return idNazione;
	}
	public void setIdNazione(Integer idNazione) {
		this.idNazione = idNazione;
	}
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
	}
	public String getIdComune() {
		return idComune;
	}
	public void setIdComune(String idComune) {
		this.idComune = idComune;
	}
	public String getProvinciaNascita() {
		return provinciaNascita;
	}
	public void setProvinciaNascita(String provinciaNascita) {
		this.provinciaNascita = provinciaNascita;
	}
	public Integer getIdClasse() {
		return idClasse;
	}
	public void setIdClasse(Integer idClasse) {
		this.idClasse = idClasse;
	}
	public String getEmailPec() {
		return emailPec;
	}
	public void setEmailPec(String emailPec) {
		this.emailPec = emailPec;
	}
	public String getCodDescrizione() {
		return codDescrizione;
	}
	public void setCodDescrizione(String codDescrizione) {
		this.codDescrizione = codDescrizione;
	}
	
}

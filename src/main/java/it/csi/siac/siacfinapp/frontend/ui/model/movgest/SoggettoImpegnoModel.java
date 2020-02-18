/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

public class SoggettoImpegnoModel extends GenericFinModel {

	private static final long serialVersionUID = 1L;

	//cod creditore
	private String codCreditore;
	
	//id classe
	private String idClasse;
	
	//classe
	private String classe;
	
	//descrizione classe
	private String classeDesc;
	
	//lista classe soggetto
	private List<ClasseSoggetto> listClasseSoggetto;
	
	//codice fiscale
	private String codfisc;
	
	//partita iva
	private String piva;
	
	//denominazione
	private String denominazione;
	
	//stato
	private StatoOperativoAnagrafica stato;
	
	//uid
	private Integer uid;
	
	//GETTER E SETTER:

	public String getCodCreditore() {
		return codCreditore;
	}
	public void setCodCreditore(String codCreditore) {
		this.codCreditore = codCreditore;
	}
	public String getClasse() {
		return classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}
	public String getCodfisc() {
		return codfisc;
	}
	public void setCodfisc(String codfisc) {
		this.codfisc = codfisc;
	}
	public String getPiva() {
		return piva;
	}
	public void setPiva(String piva) {
		this.piva = piva;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public StatoOperativoAnagrafica getStato() {
		return stato;
	}
	public void setStato(StatoOperativoAnagrafica stato) {
		this.stato = stato;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public List<ClasseSoggetto> getListClasseSoggetto() {
		return listClasseSoggetto;
	}
	public void setListClasseSoggetto(List<ClasseSoggetto> listClasseSoggetto) {
		this.listClasseSoggetto = listClasseSoggetto;
	}
	public String getClasseDesc() {
		return classeDesc;
	}
	public void setClasseDesc(String classeDesc) {
		this.classeDesc = classeDesc;
	}
	public String getIdClasse() {
		return idClasse;
	}
	public void setIdClasse(String idClasse) {
		this.idClasse = idClasse;
	}	
	
}

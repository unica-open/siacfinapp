/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.commons;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;
import it.csi.siac.siacfinser.model.codifiche.CodificaExtFin;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;



public class SoggettoModel extends GenericFinModel {

	private static final long serialVersionUID = -1693161972396945931L;
	
	//NAZIONI
	private List<CodificaFin> nazioni = new ArrayList<CodificaFin>();
	
	//LISTA NATURA GIURIDICA
	private List<CodificaExtFin> listaNaturaGiuridica = new ArrayList<CodificaExtFin>();
	
	//SIAC-6564-CR1215
	//LISTA Canale PA
	private List<CodificaFin> listaCanalePA = new ArrayList<CodificaFin>();
	
	//LISTA TIPO SOGGETTO
	private List<CodificaExtFin> listaTipoSoggetto = new ArrayList<CodificaExtFin>();
	
	//LISTA CLASSE SOGGETTO
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	
	//LISTA TIPO INDIRIZZO SEDE
	private List<CodificaFin> listaTipoIndirizzoSede = new ArrayList<CodificaFin>();
	
	//LISTA STATO OPERATIVO SOGGETTO
	private List<CodificaFin> listaStatoOperativoSoggetto = new ArrayList<CodificaFin>();
	
	//LISTA FORMA GIURIDICATIPO
	private List<CodificaFin> listaFormaGiuridicaTipo = new ArrayList<CodificaFin>();
	
	//LISTA RECAPITO MODO
	private List<CodificaFin> listaRecapitoModo = new ArrayList<CodificaFin>();
	
	//LISTA TIPO ONERE
	private List<CodificaFin> listaTipoOnere = new ArrayList<CodificaFin>();
	
	//LISTA TIPO ACCREDITO
	private List<CodificaFin> listaTipoAccredito = new ArrayList<CodificaFin>();
	
	//LISTA TIPO LEGAME
	private List<CodificaFin> listaTipoLegame = new ArrayList<CodificaFin>();

	
	//GETTER E SETTER:
	
	public List<CodificaFin> getListaTipoIndirizzoSede() {
		return listaTipoIndirizzoSede;
	}

	public void setListaTipoIndirizzoSede(List<CodificaFin> listaTipoIndirizzoSede) {
		this.listaTipoIndirizzoSede = listaTipoIndirizzoSede;
	}

	public List<CodificaFin> getNazioni() {
		return nazioni;
	}

	public void setNazioni(List<CodificaFin> nazioni) {
		this.nazioni = nazioni;
	}

	public List<CodificaExtFin> getListaTipoSoggetto() {
		return listaTipoSoggetto;
	}

	public void setListaTipoSoggetto(List<CodificaExtFin> listaTipoSoggetto) {
		this.listaTipoSoggetto = listaTipoSoggetto;
	}

	public List<CodificaFin> getListaClasseSoggetto() {
		return listaClasseSoggetto;
	}

	public void setListaClasseSoggetto(List<CodificaFin> listaClasseSoggetto) {
		this.listaClasseSoggetto = listaClasseSoggetto;
	}

	public List<CodificaFin> getListaStatoOperativoSoggetto() {
		return listaStatoOperativoSoggetto;
	}

	public void setListaStatoOperativoSoggetto(List<CodificaFin> listaStatoOperativoSoggetto) {
		this.listaStatoOperativoSoggetto = listaStatoOperativoSoggetto;
	}

	public List<CodificaFin> getListaFormaGiuridicaTipo() {
		return listaFormaGiuridicaTipo;
	}

	public void setListaFormaGiuridicaTipo(List<CodificaFin> listaFormaGiuridicaTipo) {
		this.listaFormaGiuridicaTipo = listaFormaGiuridicaTipo;
	}

	public List<CodificaFin> getListaRecapitoModo() {
		return listaRecapitoModo;
	}

	public void setListaRecapitoModo(List<CodificaFin> listaRecapitoModo) {
		this.listaRecapitoModo = listaRecapitoModo;
	}

	public List<CodificaFin> getListaTipoOnere() {
		return listaTipoOnere;
	}

	public void setListaTipoOnere(List<CodificaFin> listaTipoOnere) {
		this.listaTipoOnere = listaTipoOnere;
	}

	public List<CodificaExtFin> getListaNaturaGiuridica() {
		return listaNaturaGiuridica;
	}

	public void setListaNaturaGiuridica(List<CodificaExtFin> listaNaturaGiuridica) {
		this.listaNaturaGiuridica = listaNaturaGiuridica;
	}

	public List<CodificaFin> getListaTipoAccredito() {
		return listaTipoAccredito;
	}

	public void setListaTipoAccredito(List<CodificaFin> listaTipoAccredito) {
		this.listaTipoAccredito = listaTipoAccredito;
	}

	public List<CodificaFin> getListaTipoLegame() {
		return listaTipoLegame;
	}

	public void setListaTipoLegame(List<CodificaFin> listaTipoLegame) {
		this.listaTipoLegame = listaTipoLegame;
	}

	public List<CodificaFin> getListaCanalePA() {
		return listaCanalePA;
	}

	public void setListaCanalePA(List<CodificaFin> listaCanalePA) {
		this.listaCanalePA = listaCanalePA;
	}
	
}

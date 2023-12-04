/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.soggetto;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;

public class GestioneClassiSoggettoModel extends GenericPopupModel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//variabili per la selezione di una classe che si vuole gestire:
	private Integer idClasse;
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	
	//variabili per la gestione dei soggetti gia' associati alla classe selezionata:
	private int resultSizeSoggettiDellaClasse;
	private boolean ricercaSoggettiEffettuata = false;
	
	//variabili per l'aggiunta di un nuovo soggetto alla classe selezionata:
	private boolean soggettoSelezionato = false;
	private SoggettoImpegnoModel soggettoSelezionatoDaAggiungere = new SoggettoImpegnoModel();
	
	public Integer getIdClasse() {
		return idClasse;
	}
	public void setIdClasse(Integer idClasse) {
		this.idClasse = idClasse;
	}
	
	
	public List<CodificaFin> getListaClasseSoggetto() {
		return listaClasseSoggetto;
	}
	public void setListaClasseSoggetto(List<CodificaFin> listaClasseSoggetto) {
		this.listaClasseSoggetto = listaClasseSoggetto;
	}
	public int getResultSizeSoggettiDellaClasse() {
		return resultSizeSoggettiDellaClasse;
	}
	public void setResultSizeSoggettiDellaClasse(int resultSizeSoggettiDellaClasse) {
		this.resultSizeSoggettiDellaClasse = resultSizeSoggettiDellaClasse;
	}
	public boolean isRicercaSoggettiEffettuata() {
		return ricercaSoggettiEffettuata;
	}
	public void setRicercaSoggettiEffettuata(boolean ricercaSoggettiEffettuata) {
		this.ricercaSoggettiEffettuata = ricercaSoggettiEffettuata;
	}
	public boolean isSoggettoSelezionato() {
		return soggettoSelezionato;
	}
	public void setSoggettoSelezionato(boolean soggettoSelezionato) {
		this.soggettoSelezionato = soggettoSelezionato;
	}
	public boolean isSoggettoTrovato() {
		return soggettoTrovato;
	}
	public void setSoggettoTrovato(boolean soggettoTrovato) {
		this.soggettoTrovato = soggettoTrovato;
	}
	public SoggettoImpegnoModel getSoggettoSelezionatoDaAggiungere() {
		return soggettoSelezionatoDaAggiungere;
	}
	public void setSoggettoSelezionatoDaAggiungere(
			SoggettoImpegnoModel soggettoSelezionatoDaAggiungere) {
		this.soggettoSelezionatoDaAggiungere = soggettoSelezionatoDaAggiungere;
	}
	
}

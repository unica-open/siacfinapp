/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.mutuo;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;

public class RicercaMutuoModel extends GenericPopupModel {

	private static final long serialVersionUID = 1L;

	//provvedimento model
	private ProvvedimentoImpegnoModel provvedimento = new ProvvedimentoImpegnoModel();
	
	//soggetto model
	private SoggettoImpegnoModel soggetto = new SoggettoImpegnoModel();
	
	//tipo mutuo:
	private String tipoMutuo;
	
	//stato:
	private String stato;
	
	//descrizione:
	private String descrizione;
	
	//data inizio:
	private String dataInizio;
	
	//data fine:
	private String dataFine;
	
	//note:
	private String note;
	
	//importo formattato:
	private String importoFormattato;
	
	//numero mutuo:
	private String numeroMutuo;
	
	//numero registrazione:
	private String numeroRegistrazione;
	
	//soggetto selezionato:
	private boolean soggettoSelezionato = false;
	
	//provvedimento selezionato:
	private boolean provvedimentoSelezionato = false;
	
	//lista tipologie mutuo:
	private List<CodificaFin> listaTipiMutuo;
	
	//mutui:
	private List<Mutuo> elencoMutui = new ArrayList<Mutuo>();
	
	//result size per paginazione:
	private int resultSize;
	
	//premuto paginazione:
	private int premutoPaginazione;
	
	
	//GETTER E SETTER:
	
	public ProvvedimentoImpegnoModel getProvvedimento() {
		return provvedimento;
	}
	public void setProvvedimento(ProvvedimentoImpegnoModel provvedimento) {
		this.provvedimento = provvedimento;
	}
	public SoggettoImpegnoModel getSoggetto() {
		return soggetto;
	}
	public void setSoggetto(SoggettoImpegnoModel soggetto) {
		this.soggetto = soggetto;
	}
	public String getTipoMutuo() {
		return tipoMutuo;
	}
	public void setTipoMutuo(String tipoMutuo) {
		this.tipoMutuo = tipoMutuo;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}
	public String getDataFine() {
		return dataFine;
	}
	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getImportoFormattato() {
		return importoFormattato;
	}
	public void setImportoFormattato(String importoFormattato) {
		this.importoFormattato = importoFormattato;
	}
	public String getNumeroRegistrazione() {
		return numeroRegistrazione;
	}
	public void setNumeroRegistrazione(String numeroRegistrazione) {
		this.numeroRegistrazione = numeroRegistrazione;
	}
	public String getNumeroMutuo() {
		return numeroMutuo;
	}
	public void setNumeroMutuo(String numeroMutuo) {
		this.numeroMutuo = numeroMutuo;
	}
	public boolean isSoggettoSelezionato() {
		return soggettoSelezionato;
	}
	public void setSoggettoSelezionato(boolean soggettoSelezionato) {
		this.soggettoSelezionato = soggettoSelezionato;
	}
	public boolean isProvvedimentoSelezionato() {
		return provvedimentoSelezionato;
	}
	public void setProvvedimentoSelezionato(boolean provvedimentoSelezionato) {
		this.provvedimentoSelezionato = provvedimentoSelezionato;
	}
	public List<Mutuo> getElencoMutui() {
		return elencoMutui;
	}
	public void setElencoMutui(List<Mutuo> elencoMutui) {
		this.elencoMutui = elencoMutui;
	}
	public int getResultSize() {
		return resultSize;
	}
	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}
	public List<CodificaFin> getListaTipiMutuo() {
		return listaTipiMutuo;
	}
	public void setListaTipiMutuo(List<CodificaFin> listaTipiMutuo) {
		this.listaTipiMutuo = listaTipiMutuo;
	}
	public int getPremutoPaginazione() {
		return premutoPaginazione;
	}
	public void setPremutoPaginazione(int premutoPaginazione) {
		this.premutoPaginazione = premutoPaginazione;
	}
	
}

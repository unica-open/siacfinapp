/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.mutuo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;

public class GestioneMutuoModel extends GenericPopupModel {

	private static final long serialVersionUID = 1L;
	
	//Model specifico del provvedimento dell'impegno:
	private ProvvedimentoImpegnoModel provvedimento = new ProvvedimentoImpegnoModel();
	
	//Model soggeto impegno:
	private SoggettoImpegnoModel soggetto = new SoggettoImpegnoModel();
	
	//tipo mutuo, stato, ecc ecc:
	private String tipoMutuo, stato, descrizione, dataInizio, dataFine, note, importoFormattato, codice;
	private String numeroRegistrazione;
	
	//durata del mutuo:
	public  BigInteger durata;
	
	//importo:
	private BigDecimal importo;
	
	//flag di comodo:
	private boolean soggettoSelezionato = false, provvedimentoSelezionato = false;
	
	//lista delle tipologie mutuo:
	private List<CodificaFin> listaTipiMutuo;
	
	//id mutuo:
	private Integer idMutuo;
	
	//labels:
	private Map<String, String> labels = new HashMap<String, String>();
	
	//lista voci mutuo:
	private List<VoceMutuo> listaVociMutuo;
	
	//codice:
	private String codiceMutuoCorrente;
	
	//disponibilita
	public BigDecimal disponibilitaMutuo;
	
	//impegnato
	public BigDecimal impegnatoVociMutuo;
	
	//model per gestire il mutuo iniziale 
	public GestioneMutuoModel mutuoIniziale;
	
	public List<VoceMutuo> getListaVociMutuo() {
		return listaVociMutuo;
	}
	public void setListaVociMutuo(List<VoceMutuo> listaVociMutuo) {
		this.listaVociMutuo = listaVociMutuo;
	}
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
	public String getNumeroRegistrazione() {
		return numeroRegistrazione;
	}
	public void setNumeroRegistrazione(String numeroRegistrazione) {
		this.numeroRegistrazione = numeroRegistrazione;
	}
	public BigInteger getDurata() {
		return durata;
	}
	public void setDurata(BigInteger durata) {
		this.durata = durata;
	}
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
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
	public List<CodificaFin> getListaTipiMutuo() {
		return listaTipiMutuo;
	}
	public void setListaTipiMutuo(List<CodificaFin> listaTipiMutuo) {
		this.listaTipiMutuo = listaTipiMutuo;
	}
	public String getImportoFormattato() {
		return importoFormattato;
	}
	public void setImportoFormattato(String importoFormattato) {
		this.importoFormattato = importoFormattato;
	}
	public Integer getIdMutuo() {
		return idMutuo;
	}
	public void setIdMutuo(Integer idMutuo) {
		this.idMutuo = idMutuo;
	}
	public Map<String, String> getLabels() {
		return labels;
	}
	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public String getCodiceMutuoCorrente() {
		return codiceMutuoCorrente;
	}
	public void setCodiceMutuoCorrente(String codiceMutuoCorrente) {
		this.codiceMutuoCorrente = codiceMutuoCorrente;
	}
	public BigDecimal getDisponibilitaMutuo() {
		return disponibilitaMutuo;
	}
	public void setDisponibilitaMutuo(BigDecimal disponibilitaMutuo) {
		this.disponibilitaMutuo = disponibilitaMutuo;
	}
	public BigDecimal getImpegnatoVociMutuo() {
		return impegnatoVociMutuo;
	}
	public void setImpegnatoVociMutuo(BigDecimal impegnatoVociMutuo) {
		this.impegnatoVociMutuo = impegnatoVociMutuo;
	}
	public GestioneMutuoModel getMutuoIniziale() {
		return mutuoIniziale;
	}
	public void setMutuoIniziale(GestioneMutuoModel mutuoIniziale) {
		this.mutuoIniziale = mutuoIniziale;
	}
	
}

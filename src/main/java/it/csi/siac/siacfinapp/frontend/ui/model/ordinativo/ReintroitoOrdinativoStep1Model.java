/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;

public class ReintroitoOrdinativoStep1Model extends GenericFinModel {

	private static final long serialVersionUID = 1L;
	
	
	//Scelta provvedimento usato:
	private List<String> scelteProvvedimentoDaUsare = new ArrayList<String>();
	private String provvedimentoDaUsare;
	
	private BigDecimal importoNetto;
	
	//model del provvedimento
	private ProvvedimentoImpegnoModel provvedimento = new ProvvedimentoImpegnoModel();
	private boolean provvedimentoSelezionato;
	//

	//anno e numero per cercare l'ordinativo da reintroitare:
	private BigInteger numeroOrdinativoPagamento;
	private Integer annoOrdinativoPagamento;
	
	//l'ordinativo trovato tramite cerca:
	private OrdinativoPagamento ordinativoDaReintroitare;
	//questo provvedimento e' quello contenuto in ordinativoDaReintroitare da non confondere con quello ricercato tramite modale
	private ProvvedimentoImpegnoModel provvedimentoOrdinativoDaReintroitare = new ProvvedimentoImpegnoModel();
	private SoggettoImpegnoModel soggettoOrdinativoDaReintroitare = new SoggettoImpegnoModel();
	private CapitoloImpegnoModel capitoloOrdinativoDaReintroitare = new CapitoloImpegnoModel();
	private String descrArrModPag;
	
	private List<DettaglioDocumentoModel> documentiCollegati;
	private BigDecimal importoDocumentiCollegati;
	//
	
	public BigInteger getNumeroOrdinativoPagamento() {
		return numeroOrdinativoPagamento;
	}

	public void setNumeroOrdinativoPagamento(BigInteger numeroOrdinativoPagamento) {
		this.numeroOrdinativoPagamento = numeroOrdinativoPagamento;
	}

	public ProvvedimentoImpegnoModel getProvvedimento() {
		return provvedimento;
	}

	public void setProvvedimento(ProvvedimentoImpegnoModel provvedimento) {
		this.provvedimento = provvedimento;
	}

	public boolean isProvvedimentoSelezionato() {
		return provvedimentoSelezionato;
	}

	public void setProvvedimentoSelezionato(boolean provvedimentoSelezionato) {
		this.provvedimentoSelezionato = provvedimentoSelezionato;
	}

	public List<String> getScelteProvvedimentoDaUsare() {
		return scelteProvvedimentoDaUsare;
	}

	public void setScelteProvvedimentoDaUsare(
			List<String> scelteProvvedimentoDaUsare) {
		this.scelteProvvedimentoDaUsare = scelteProvvedimentoDaUsare;
	}

	public String getProvvedimentoDaUsare() {
		return provvedimentoDaUsare;
	}

	public void setProvvedimentoDaUsare(String provvedimentoDaUsare) {
		this.provvedimentoDaUsare = provvedimentoDaUsare;
	}

	public Integer getAnnoOrdinativoPagamento() {
		return annoOrdinativoPagamento;
	}

	public void setAnnoOrdinativoPagamento(Integer annoOrdinativoPagamento) {
		this.annoOrdinativoPagamento = annoOrdinativoPagamento;
	}

	public OrdinativoPagamento getOrdinativoDaReintroitare() {
		return ordinativoDaReintroitare;
	}

	public void setOrdinativoDaReintroitare(OrdinativoPagamento ordinativoDaReintroitare) {
		this.ordinativoDaReintroitare = ordinativoDaReintroitare;
	}

	public ProvvedimentoImpegnoModel getProvvedimentoOrdinativoDaReintroitare() {
		return provvedimentoOrdinativoDaReintroitare;
	}

	public void setProvvedimentoOrdinativoDaReintroitare(ProvvedimentoImpegnoModel provvedimentoOrdinativoDaReintroitare) {
		this.provvedimentoOrdinativoDaReintroitare = provvedimentoOrdinativoDaReintroitare;
	}

	public SoggettoImpegnoModel getSoggettoOrdinativoDaReintroitare() {
		return soggettoOrdinativoDaReintroitare;
	}

	public void setSoggettoOrdinativoDaReintroitare(SoggettoImpegnoModel soggettoOrdinativoDaReintroitare) {
		this.soggettoOrdinativoDaReintroitare = soggettoOrdinativoDaReintroitare;
	}

	public CapitoloImpegnoModel getCapitoloOrdinativoDaReintroitare() {
		return capitoloOrdinativoDaReintroitare;
	}

	public void setCapitoloOrdinativoDaReintroitare(CapitoloImpegnoModel capitoloOrdinativoDaReintroitare) {
		this.capitoloOrdinativoDaReintroitare = capitoloOrdinativoDaReintroitare;
	}

	public String getDescrArrModPag() {
		return descrArrModPag;
	}

	public void setDescrArrModPag(String descrArrModPag) {
		this.descrArrModPag = descrArrModPag;
	}

	public BigDecimal getImportoNetto() {
		return importoNetto;
	}

	public void setImportoNetto(BigDecimal importoNetto) {
		this.importoNetto = importoNetto;
	}

	public List<DettaglioDocumentoModel> getDocumentiCollegati() {
		return documentiCollegati;
	}

	public void setDocumentiCollegati(List<DettaglioDocumentoModel> documentiCollegati) {
		this.documentiCollegati = documentiCollegati;
	}

	public BigDecimal getImportoDocumentiCollegati() {
		return importoDocumentiCollegati;
	}

	public void setImportoDocumentiCollegati(BigDecimal importoDocumentiCollegati) {
		this.importoDocumentiCollegati = importoDocumentiCollegati;
	}
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.mutuo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;

public class GestioneVoceDiMutuoModel extends GenericPopupModel {

	private static final long serialVersionUID = 1L;
	
	//provvdimento
	private ProvvedimentoImpegnoModel provvedimento = new ProvvedimentoImpegnoModel();
	private boolean provvedimentoSelezionato = false;
	
	//voce mutuo
	private Integer idVoceDiMutuo;
	
	//lista sub
	private List<ImpegnoSubimpegnoMutuoModel> listaImpegniSubimpegni = new ArrayList<ImpegnoSubimpegnoMutuoModel>();
	
	//importo nuova voce
	private String importoNuovaVoceDiMutuo;
	
	//mutuo selezionata
	private Mutuo mutuoSelezionato = new Mutuo();
	
	//altri campi
	private boolean toggleImpAccAperto;
	private String importoStornoStringa;
	private BigDecimal importoStorno;
	
	private boolean inserimentoStornoVoceMutuo = false;
	
	//per paginazione
	private int resultSize;
	
	
	//GESTIONE METODI SU ELEMENTI DI ELENCO VOCI MUTUO:
	private String numVoceMutuoSelezionato;
	private String importoDigitato;
	//
	

	//GETTER E SETTER:
	
	public Integer getIdVoceDiMutuo() {
		return idVoceDiMutuo;
	}

	public void setIdVoceDiMutuo(Integer idVoceDiMutuo) {
		this.idVoceDiMutuo = idVoceDiMutuo;
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

	public List<ImpegnoSubimpegnoMutuoModel> getListaImpegniSubimpegni() {
		return listaImpegniSubimpegni;
	}

	public void setListaImpegniSubimpegni(List<ImpegnoSubimpegnoMutuoModel> listaImpegniSubimpegni) {
		this.listaImpegniSubimpegni = listaImpegniSubimpegni;
	}

	public String getImportoNuovaVoceDiMutuo() {
		return importoNuovaVoceDiMutuo;
	}

	public void setImportoNuovaVoceDiMutuo(String importoNuovaVoceDiMutuo) {
		this.importoNuovaVoceDiMutuo = importoNuovaVoceDiMutuo;
	}

	public Mutuo getMutuoSelezionato() {
		return mutuoSelezionato;
	}

	public void setMutuoSelezionato(Mutuo mutuoSelezionato) {
		this.mutuoSelezionato = mutuoSelezionato;
	}

	public String getImportoStornoStringa() {
		return importoStornoStringa;
	}

	public void setImportoStornoStringa(String importoStornoStringa) {
		this.importoStornoStringa = importoStornoStringa;
	}

	public BigDecimal getImportoStorno() {
		return importoStorno;
	}

	public void setImportoStorno(BigDecimal importoStorno) {
		this.importoStorno = importoStorno;
	}

	public boolean isInserimentoStornoVoceMutuo() {
		return inserimentoStornoVoceMutuo;
	}

	public void setInserimentoStornoVoceMutuo(boolean inserimentoStornoVoceMutuo) {
		this.inserimentoStornoVoceMutuo = inserimentoStornoVoceMutuo;
	}

	public boolean isToggleImpAccAperto() {
		return toggleImpAccAperto;
	}

	public void setToggleImpAccAperto(boolean toggleImpAccAperto) {
		this.toggleImpAccAperto = toggleImpAccAperto;
	}

	public int getResultSize() {
		return resultSize;
	}

	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}

	public String getNumVoceMutuoSelezionato() {
		return numVoceMutuoSelezionato;
	}

	public void setNumVoceMutuoSelezionato(String numVoceMutuoSelezionato) {
		this.numVoceMutuoSelezionato = numVoceMutuoSelezionato;
	}

	public String getImportoDigitato() {
		return importoDigitato;
	}

	public void setImportoDigitato(String importoDigitato) {
		this.importoDigitato = importoDigitato;
	}
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

public class GestioneOrdinativoStep3Model  extends GenericFinModel {
	
	private static final long serialVersionUID = 1L;
	
	private ProvvisorioDiCassa provvisorio = new ProvvisorioDiCassa();
    
	
	private List<ProvvisorioDiCassa> listaProvvisori = new ArrayList<ProvvisorioDiCassa>();
	
	private List<ProvvisorioDiCassa> listaCoperture = new ArrayList<ProvvisorioDiCassa>();
	
	private BigDecimal totalizzatoreOrdinativo = BigDecimal.ZERO;
	private BigDecimal totalizzatorePagato = BigDecimal.ZERO;
	private BigDecimal totalizzatoreDaCollegare = BigDecimal.ZERO;
	
	//questo lo uso (per aggiornare la Copertura) per tenere traccia dell'importo prima che venga modificato al conferma:
	private BigDecimal importoCoperturaPrimaUpdate;
	private Boolean riportaInTestataPageInserisciProvvisorio = Boolean.FALSE; 
	private Boolean riportaInTestataPageAggiornaProvvisorio = Boolean.FALSE;
	private Boolean hiddenRiportaInTestataPageAggiornaProvvisorio ;
	//SIAC-6352
	private List<CodificaFin> listaContoTesoriere;

	/////////////////////////////////////////////////////////
	
		

	private ProvvisorioDiCassa visualizzaProvvisorio = new ProvvisorioDiCassa();

	/**
	 * @return the hiddenRiportaInTestataPageAggiornaProvvisorio
	 */
	public Boolean getHiddenRiportaInTestataPageAggiornaProvvisorio() {
		return hiddenRiportaInTestataPageAggiornaProvvisorio;
	}

	/**
	 * @param hiddenRiportaInTestataPageAggiornaProvvisorio the hiddenRiportaInTestataPageAggiornaProvvisorio to set
	 */
	public void setHiddenRiportaInTestataPageAggiornaProvvisorio(
			Boolean hiddenRiportaInTestataPageAggiornaProvvisorio) {
		this.hiddenRiportaInTestataPageAggiornaProvvisorio = hiddenRiportaInTestataPageAggiornaProvvisorio;
	}

	/**
	 * @return the riportaInTestataPageAggiornaProvvisorio
	 */
	public Boolean getRiportaInTestataPageAggiornaProvvisorio() {
		return riportaInTestataPageAggiornaProvvisorio;
	}

	/**
	 * @param riportaInTestataPageAggiornaCopertura the riportaInTestataPageAggiornaProvvisorio to set
	 */
	public void setRiportaInTestataPageAggiornaProvvisorio(
			Boolean riportaInTestataPageAggiornaProvvisorio) {
		this.riportaInTestataPageAggiornaProvvisorio = riportaInTestataPageAggiornaProvvisorio;
	}

	/**
	 * @return the riportaInTestataPageInserisciProvvisorio
	 */
	public Boolean getRiportaInTestataPageInserisciProvvisorio() {
		return riportaInTestataPageInserisciProvvisorio;
	}

	/**
	 * @param riportaInTestataPageInserisciProvvisorio the riportaInTestataPageInserisciProvvisorio to set
	 */
	public void setRiportaInTestataPageInserisciProvvisorio(
			Boolean riportaInTestataPageInserisciProvvisorio) {
		this.riportaInTestataPageInserisciProvvisorio = riportaInTestataPageInserisciProvvisorio;
	}

	public ProvvisorioDiCassa getVisualizzaProvvisorio() {
		return visualizzaProvvisorio;
	}

	public void setVisualizzaProvvisorio(ProvvisorioDiCassa visualizzaProvvisorio) {
		this.visualizzaProvvisorio = visualizzaProvvisorio;
	}

	public BigDecimal getTotalizzatoreOrdinativo() {
		return totalizzatoreOrdinativo;
	}

	public void setTotalizzatoreOrdinativo(BigDecimal totalizzatoreOrdinativo) {
		this.totalizzatoreOrdinativo = totalizzatoreOrdinativo;
	}

	public BigDecimal getTotalizzatorePagato() {
		return totalizzatorePagato;
	}

	public void setTotalizzatorePagato(BigDecimal totalizzatorePagato) {
		this.totalizzatorePagato = totalizzatorePagato;
	}

	public BigDecimal getTotalizzatoreDaCollegare() {
		return totalizzatoreDaCollegare;
	}

	public void setTotalizzatoreDaCollegare(BigDecimal totalizzatoreDaCollegare) {
		this.totalizzatoreDaCollegare = totalizzatoreDaCollegare;
	}

	public ProvvisorioDiCassa getProvvisorio() {
		return provvisorio;
	}

	public void setProvvisorio(ProvvisorioDiCassa provvisorio) {
		this.provvisorio = provvisorio;
	}

	

	public List<ProvvisorioDiCassa> getListaProvvisori() {
		return listaProvvisori;
	}

	public void setListaProvvisori(List<ProvvisorioDiCassa> listaProvvisori) {
		this.listaProvvisori = listaProvvisori;
	}

	public List<ProvvisorioDiCassa> getListaCoperture() {
		return listaCoperture;
	}

	public void setListaCoperture(List<ProvvisorioDiCassa> listaCoperture) {
		this.listaCoperture = listaCoperture;
	}

	public BigDecimal getImportoCoperturaPrimaUpdate() {
		return importoCoperturaPrimaUpdate;
	}

	public void setImportoCoperturaPrimaUpdate(
			BigDecimal importoCoperturaPrimaUpdate) {
		this.importoCoperturaPrimaUpdate = importoCoperturaPrimaUpdate;
	}

	/**
	 * @return the listaContoTesoriere
	 */
	public List<CodificaFin> getListaContoTesoriere() {
		return listaContoTesoriere;
	}

	/**
	 * @param listaContoTesoriere the listaContoTesoriere to set
	 */
	public void setListaContoTesoriere(List<CodificaFin> listaContoTesoriere) {
		this.listaContoTesoriere = listaContoTesoriere;
	}

	
}

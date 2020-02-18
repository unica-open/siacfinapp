/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import java.math.BigDecimal;
import java.util.Date;


public class MutuoConsulta extends CommonConsulta {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String numeroMovimentoGestione; 
	private String codiceMutuo;
	private String descrizioneMutuo;
	private String istitutoMutuante; // concatena mutuo.soggettoMutuo.codiceSoggetto + mutuo.soggettoMutuo.denominazione
	private String descrizioneTipoMutuo;
	private Date dataInizioMutuo;
	private BigDecimal importoAttualeMutuo;
	private BigDecimal importoAttualeVoceMutuo;
	
	
	/**
	 * @return the numeroMovimentoGestione
	 */
	public String getNumeroMovimentoGestione() {
		return numeroMovimentoGestione;
	}
	/**
	 * @param numeroMovimentoGestione the numeroMovimentoGestione to set
	 */
	public void setNumeroMovimentoGestione(String numeroMovimentoGestione) {
		this.numeroMovimentoGestione = numeroMovimentoGestione;
	}
	
	/**
	 * @return the importoAttualeVoceMutuo
	 */
	public BigDecimal getImportoAttualeVoceMutuo() {
		return importoAttualeVoceMutuo;
	}
	/**
	 * @param importoAttualeVoceMutuo the importoAttualeVoceMutuo to set
	 */
	public void setImportoAttualeVoceMutuo(BigDecimal importoAttualeVoceMutuo) {
		this.importoAttualeVoceMutuo = importoAttualeVoceMutuo;
	}
	/**
	 * @return the codiceMutuo
	 */
	public String getCodiceMutuo() {
		return codiceMutuo;
	}
	/**
	 * @param codiceMutuo the codiceMutuo to set
	 */
	public void setCodiceMutuo(String codiceMutuo) {
		this.codiceMutuo = codiceMutuo;
	}
	/**
	 * @return the descrizioneMutuo
	 */
	public String getDescrizioneMutuo() {
		return descrizioneMutuo;
	}
	/**
	 * @param descrizioneMutuo the descrizioneMutuo to set
	 */
	public void setDescrizioneMutuo(String descrizioneMutuo) {
		this.descrizioneMutuo = descrizioneMutuo;
	}
	/**
	 * @return the istitutoMutuante
	 */
	public String getIstitutoMutuante() {
		return istitutoMutuante;
	}
	/**
	 * @param istitutoMutuante the istitutoMutuante to set
	 */
	public void setIstitutoMutuante(String istitutoMutuante) {
		this.istitutoMutuante = istitutoMutuante;
	}
	/**
	 * @return the descrizioneTipoMutuo
	 */
	public String getDescrizioneTipoMutuo() {
		return descrizioneTipoMutuo;
	}
	/**
	 * @param descrizioneTipoMutuo the descrizioneTipoMutuo to set
	 */
	public void setDescrizioneTipoMutuo(String descrizioneTipoMutuo) {
		this.descrizioneTipoMutuo = descrizioneTipoMutuo;
	}
	/**
	 * @return the dataInizioMutuo
	 */
	public Date getDataInizioMutuo() {
		return dataInizioMutuo;
	}
	/**
	 * @param dataInizioMutuo the dataInizioMutuo to set
	 */
	public void setDataInizioMutuo(Date dataInizioMutuo) {
		this.dataInizioMutuo = dataInizioMutuo;
	}
	/**
	 * @return the importoAttualeMutuo
	 */
	public BigDecimal getImportoAttualeMutuo() {
		return importoAttualeMutuo;
	}
	/**
	 * @param importoAttualeMutuo the importoAttualeMutuo to set
	 */
	public void setImportoAttualeMutuo(BigDecimal importoAttualeMutuo) {
		this.importoAttualeMutuo = importoAttualeMutuo;
	}

	
	

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;

public class DettaglioDocumentoModel extends GenericFinModel {

	private static final long serialVersionUID = 1L;
	
	private String tipoDebitoSiope;
	private String descrizioneDocumento;
	private String numeroDocumento;
	private Date dataEmissioneDocumento;
	private BigDecimal importoDocumento;
	
	
	public String getTipoDebitoSiope() {
		return tipoDebitoSiope;
	}
	public void setTipoDebitoSiope(String tipoDebitoSiope) {
		this.tipoDebitoSiope = tipoDebitoSiope;
	}
	public String getDescrizioneDocumento() {
		return descrizioneDocumento;
	}
	public void setDescrizioneDocumento(String descrizioneDocumento) {
		this.descrizioneDocumento = descrizioneDocumento;
	}
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public Date getDataEmissioneDocumento() {
		return dataEmissioneDocumento;
	}
	public void setDataEmissioneDocumento(Date dataEmissioneDocumento) {
		this.dataEmissioneDocumento = dataEmissioneDocumento;
	}
	public BigDecimal getImportoDocumento() {
		return importoDocumento;
	}
	public void setImportoDocumento(BigDecimal importoDocumento) {
		this.importoDocumento = importoDocumento;
	}
	
}

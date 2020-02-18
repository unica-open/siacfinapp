/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import java.math.BigDecimal;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;

public class NuovoAccertamentoModel extends GenericFinModel {

	private static final long serialVersionUID = 1L;

	//numero
	private BigDecimal numeroAccertamento;
	
	//anno
	private Integer annoAccertamento;
	
	//descrizione
	private String descrizioneAccertamento;
	
	//importo
	private String importoAccertamento;
	
	public BigDecimal getNumeroAccertamento() {
		return numeroAccertamento;
	}
	public void setNumeroAccertamento(BigDecimal numeroAccertamento) {
		this.numeroAccertamento = numeroAccertamento;
	}
	public Integer getAnnoAccertamento() {
		return annoAccertamento;
	}
	public void setAnnoAccertamento(Integer annoAccertamento) {
		this.annoAccertamento = annoAccertamento;
	}
	public String getDescrizioneAccertamento() {
		return descrizioneAccertamento;
	}
	public void setDescrizioneAccertamento(String descrizioneAccertamento) {
		this.descrizioneAccertamento = descrizioneAccertamento;
	}
	public String getImportoAccertamento() {
		return importoAccertamento;
	}
	public void setImportoAccertamento(String importoAccertamento) {
		this.importoAccertamento = importoAccertamento;
	}

}

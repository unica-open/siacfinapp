/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import java.math.BigDecimal;

import it.csi.siac.siaccommonapp.model.GenericModel;

public class ImportiCapitoloModel extends GenericModel {

	private static final long serialVersionUID = 1L;
	
	//anno competenza:
	private Integer annoCompetenza;
	
	//importi:
	private BigDecimal competenza, residuo, cassa;
	
	public Integer getAnnoCompetenza() {
		return annoCompetenza;
	}
	public void setAnnoCompetenza(Integer annoCompetenza) {
		this.annoCompetenza = annoCompetenza;
	}
	public BigDecimal getCompetenza() {
		return competenza;
	}
	public void setCompetenza(BigDecimal competenza) {
		this.competenza = competenza;
	}
	public BigDecimal getResiduo() {
		return residuo;
	}
	public void setResiduo(BigDecimal residuo) {
		this.residuo = residuo;
	}
	public BigDecimal getCassa() {
		return cassa;
	}
	public void setCassa(BigDecimal cassa) {
		this.cassa = cassa;
	}
}

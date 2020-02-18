/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.siac.siaccommonapp.model.GenericModel;

public class ImpegniPluriennaliModel extends GenericModel {

	private static final long serialVersionUID = 1L;
	
	//anno
	private int annoImpPluriennale;
	
	//importo
	private BigDecimal importoImpPluriennale;
	private String importoImpPluriennaleString;
	
	//data scandenza
	private Date dataScadenzaImpPluriennale;
	private String dataScadenzaImpPluriennaleString;
	
	
	public int getAnnoImpPluriennale() {
		return annoImpPluriennale;
	}
	public void setAnnoImpPluriennale(int annoImpPluriennale) {
		this.annoImpPluriennale = annoImpPluriennale;
	}
	public BigDecimal getImportoImpPluriennale() {
		return importoImpPluriennale;
	}
	public void setImportoImpPluriennale(BigDecimal importoImpPluriennale) {
		this.importoImpPluriennale = importoImpPluriennale;
	}
	public Date getDataScadenzaImpPluriennale() {
		return dataScadenzaImpPluriennale;
	}
	public void setDataScadenzaImpPluriennale(Date dataScadenzaImpPluriennale) {
		this.dataScadenzaImpPluriennale = dataScadenzaImpPluriennale;
	}
	public String getImportoImpPluriennaleString() {
		return importoImpPluriennaleString;
	}
	public void setImportoImpPluriennaleString(String importoImpPluriennaleString) {
		this.importoImpPluriennaleString = importoImpPluriennaleString;
	}
	public String getDataScadenzaImpPluriennaleString() {
		return dataScadenzaImpPluriennaleString;
	}
	public void setDataScadenzaImpPluriennaleString(
			String dataScadenzaImpPluriennaleString) {
		this.dataScadenzaImpPluriennaleString = dataScadenzaImpPluriennaleString;
	}
	

}

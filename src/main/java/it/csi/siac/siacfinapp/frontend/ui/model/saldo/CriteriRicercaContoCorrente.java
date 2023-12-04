/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.saldo;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;

import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;

public class CriteriRicercaContoCorrente implements Serializable {
	private static final long serialVersionUID = -2194782158475018622L;

	//anno
	private Integer anno;
	
	//id classificazione conto
	private Integer idClassifConto;
	
	//Conto corrente
	private String contoCorrente;
	
	//data inizio
	private Date dataInizio;
	
	//data fine
	private Date dataFine;

	private static final Date MIN_DATE = new GregorianCalendar(1900, 0, 1).getTime();
	private static final Date MAX_DATE = new GregorianCalendar(9999, 0, 1).getTime();

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public Integer getIdClassifConto() {
		return idClassifConto;
	}

	public void setIdClassifConto(Integer idClassifConto) {
		this.idClassifConto = idClassifConto;
	}

	public void setDataInizioStr(String dataInizioStr) {
		setDataInizio(DateUtility.convertToDefaultDate(dataInizioStr, MIN_DATE));
	}

	public void setDataFineStr(String dataFineStr) {
		setDataFine(DateUtility.convertToDefaultDate(dataFineStr, MAX_DATE));
	}

	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public String getContoCorrente() {
		return contoCorrente;
	}

	public void setContoCorrente(String contoCorrente) {
		this.contoCorrente = contoCorrente;
	}

	public void setDataStr(String dataStr) {
		setDataInizioStr(dataStr);
		setDataFineStr(dataStr);
	}

}

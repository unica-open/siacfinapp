/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.saldo;

import java.math.BigDecimal;
import java.util.Date;

public class ConsultaDettaglioContoCorrenteModel extends BaseContoCorrenteModel {
	private static final long serialVersionUID = -5511677007702738234L;

	//data precedente:
	private Date dataPrec;
	
	//data:
	private Date data;
	
	//saldo precedente:
	private BigDecimal saldoCassaPrec;
	
	//saldo:
	private BigDecimal saldoCassa;

	public BigDecimal getSaldoCassaPrec() {
		return saldoCassaPrec;
	}

	public void setSaldoCassaPrec(BigDecimal saldoCassaPrec) {
		this.saldoCassaPrec = saldoCassaPrec;
	}

	public BigDecimal getSaldoCassa() {
		return saldoCassa;
	}

	public void setSaldoCassa(BigDecimal saldoCassa) {
		this.saldoCassa = saldoCassa;
	}

	public Date getDataPrec() {
		return dataPrec;
	}

	public void setDataPrec(Date dataPrec) {
		this.dataPrec = dataPrec;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

}
/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;


public class ConsultaOrdinativoModel extends GenericFinModel {
	
	private static final long serialVersionUID = 1L;
	
	//active tab:
	protected String activeTab = "";

	private Integer totaleOrdinativiCollegati;
	
	public String getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(String activeTab) {
		this.activeTab = activeTab;
	}

	public Integer getTotaleOrdinativiCollegati() {
		return totaleOrdinativiCollegati;
	}

	public void setTotaleOrdinativiCollegati(Integer totaleOrdinativiCollegati) {
		this.totaleOrdinativiCollegati = totaleOrdinativiCollegati;
	}
}

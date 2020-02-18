/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;

import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.InserisciSoggettoModel;

public abstract class WizardScriviSoggettoAction extends SoggettoAction<InserisciSoggettoModel> {
	
	private static final long serialVersionUID = 1L;

	@Override
	public String getActionKey() {
		//ritorno la action key di pagina
		return "salvaSoggetto";
	}

	@BreadCrumb("%{model.titolo}")
	public String doExecute() throws Exception {
		//rimando all'execute della super classe
		return execute();
	}
}

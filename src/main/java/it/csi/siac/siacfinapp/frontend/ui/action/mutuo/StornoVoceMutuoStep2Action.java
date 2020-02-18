/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.mutuo;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class StornoVoceMutuoStep2Action extends GestioneVoceDiMutuoStep1Action{

	private static final long serialVersionUID = 1L;
	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		this.model.setTitolo("Storno - Provvedimento");
	}	
	
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// chiama la super
		return super.execute();

	}
}
/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto.cec;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.action.soggetto.ElencoSoggettiAction;
import it.csi.siac.siacfinser.CostantiFin;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ElencoSoggettiCecAction extends ElencoSoggettiAction {

	private static final long serialVersionUID = 958365596583427352L;

	@Override
	protected String getCodiceAmbito() {
		//setto l'ambito
		//in questo caso cec
		return CostantiFin.AMBITO_CEC;
	}
	
}

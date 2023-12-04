/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto.cec;




import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.action.soggetto.InserisciSoggettoAction;
import it.csi.siac.siacfinser.CostantiFin;




@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciSoggettoCecAction extends InserisciSoggettoAction {

	private static final long serialVersionUID = 4818594660978688092L;

	@Override
	protected String getCodiceAmbito() {
		//ritorniamo la codifica 
		//dell'ambito, cec in questo caso
		return CostantiFin.AMBITO_CEC;
	}
	
	@Override
	protected void checkCampiFEL() {
		//SIAC-6834
	}
}

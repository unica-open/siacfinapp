/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto.cec;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.action.soggetto.InserisciContattiAction;
import it.csi.siac.siacfinser.CostantiFin;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciContattiCecAction extends InserisciContattiAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2989157179039967376L;

	@Override
	protected String getCodificaAmbito() {
		//ritorniamo la codifica 
		//dell'ambito, cec in questo caso
		return CostantiFin.AMBITO_CEC;
	}
	
	@Override
	protected boolean isInserimentoSoggettoProvvisorio() {
		//SIAC-5722: non devo inserire un soggetto provvisorio se sono in ambito CEC
		return false;
	}

}

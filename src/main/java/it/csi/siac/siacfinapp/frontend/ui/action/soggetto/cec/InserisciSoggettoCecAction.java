/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto.cec;




import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siacfinapp.frontend.ui.action.soggetto.InserisciSoggettoAction;
import it.csi.siac.siacfinser.Constanti;




@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciSoggettoCecAction extends InserisciSoggettoAction {

	private static final long serialVersionUID = 4818594660978688092L;

	@Override
	protected String getCodiceAmbito() {
		//ritorniamo la codifica 
		//dell'ambito, cec in questo caso
		return Constanti.AMBITO_CEC;
	}
	
	@Override
	protected void checkCampiFEL(List<Errore> listaErrori) {
		//SIAC-6834
	}
}

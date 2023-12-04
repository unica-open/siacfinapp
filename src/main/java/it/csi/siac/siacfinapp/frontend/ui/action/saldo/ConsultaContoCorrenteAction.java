/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.saldo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.model.saldo.ConsultaContoCorrenteModel;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaContoCorrenteAction extends BaseRicercaContoCorrenteAction<ConsultaContoCorrenteModel>{

	private static final long serialVersionUID = 1L;

	@Override
	public void validateCerca() {
		//invochiamo il validate della super classe:
		super.validateCerca();
		
		if (model.getCriteriRicerca().getDataInizio() == null){
			//da inizio non valorizzata
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("data"));
		}
	}
	
}

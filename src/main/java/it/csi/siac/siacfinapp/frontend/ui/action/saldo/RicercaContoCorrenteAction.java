/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.saldo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.model.saldo.RicercaContoCorrenteModel;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaContoCorrenteAction extends BaseRicercaContoCorrenteAction<RicercaContoCorrenteModel>{

	private static final long serialVersionUID = 1L;

	//per ora bastano i campi
	//nella superclasse

}

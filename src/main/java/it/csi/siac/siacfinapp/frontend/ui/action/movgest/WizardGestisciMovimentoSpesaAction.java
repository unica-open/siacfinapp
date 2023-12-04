/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

public class WizardGestisciMovimentoSpesaAction extends WizardAggiornaMovGestAction {


	private static final long serialVersionUID = 6753316042981814253L;
	
	//non ci sono campi,
	//al momento bastano quelli della super classe

	@Override
	public String getActionKey() {
		//ritorno la action key di pagina
		return "movimentoSpesa";
	}
	
}

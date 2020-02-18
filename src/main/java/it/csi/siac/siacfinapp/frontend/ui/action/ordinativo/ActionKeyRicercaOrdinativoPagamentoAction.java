/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

/**
 * classe utilizzata per la definizione univoca del modello dati in sessione
 * @author paolos
 *
 */
public class ActionKeyRicercaOrdinativoPagamentoAction extends WizardRicercaOrdinativoAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1488159655930053275L;

	@Override
	public String getActionKey() {
		return "ricercaOrdinativoPagamento";
	}
	
	protected void caricaLabelsInserisci(int step) {
	}

}

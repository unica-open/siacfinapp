/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.carta;

/**
 * classe utilizzata per la definizione univoca del modello dati in sessione
 * @author paolos
 *
 */
public class ActionKeyNuovaRigaDaDocumentiAction extends WizardNuovaRigaDaDocumentiAction {

	private static final long serialVersionUID = 1L;


	@Override
	public String getActionKey() {
		return "gestioneCartaNuovaRigaDaDocumenti";
	}

	
}

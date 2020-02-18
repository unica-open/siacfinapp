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
public class ActionKeyGestioneOrdinativoIncassoAction  extends WizardGestioneOrdinativoAction {

	private static final long serialVersionUID = 1L;

	@Override
	public String getActionKey() {
		return "gestioneOrdinativoIncasso";
	}
	
	/**
	 * gestione dinamica delle labels a seconda che si tratti di ordinativo
	 * incasso o pagamento
	 * 
	 * @param step
	 * @param isInserimento
	 */
	protected void caricaLabelsInserisci(int step, boolean isInserimento) {
		switch (step) {
		case 1:
			model.getLabels().put(FORM, "gestioneOrdinativoIncassoStep1");
			break;
			
		case 2:
			model.getLabels().put(FORM, "gestioneOrdinativoIncassoStep2");
			break;
			
		case 3:
			model.getLabels().put(FORM, "gestioneOrdinativoIncassoStep3");
			break;	
			
		default:
			model.getLabels().put(FORM, "gestioneOrdinativoIncassoStep1");
			break;
		}
		if (isInserimento) {
			model.getLabels().put(LABEL_TITOLO, "Inserisci ordinativo incasso");
		} else {
			model.getLabels().put(LABEL_TITOLO, "Aggiorna ordinativo incasso");
		}
		
		model.getLabels().put(LABEL_SOGGETTO, "Debitore");
	}
}
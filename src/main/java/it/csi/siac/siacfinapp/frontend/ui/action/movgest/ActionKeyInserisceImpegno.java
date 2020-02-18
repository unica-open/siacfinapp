/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

/**
 * classe utilizzata per la definizione univoca del modello dati in sessione
 * ed anche per la renderizzazione automatica delle label a seconda
 * che si tratti di impegno o accertamento
 * @author paolos
 *
 */
public class ActionKeyInserisceImpegno extends WizardInserisciMovGestAction {

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getActionKey() {
		return "inserisceImpegno";
	}
	
	protected void caricaLabelsInserisci(int step) {
		switch (step) {
		case 1:
			model.getLabels().put(FORM, "inserisceImpegno");
			break;
		case 2:
			model.getLabels().put(FORM, "inserisceImpegnoStep2");
			break;
		case 3:
			model.getLabels().put(FORM, "inserisceImpegnoStep3");
			break;
		default:
			model.getLabels().put(FORM, "inserisceImpegno");
			break;
		}
		model.getLabels().put(LABEL_TITOLO, "Inserisci impegno");
		model.getLabels().put(LABEL_STEP1, "Dati impegno");
		model.getLabels().put(LABEL_STEP3, "Impegni pluriennali");
		model.getLabels().put(LABEL_OGGETTO_GENERICO, "Impegno");
		model.getLabels().put(LABEL_OGGETTO_GENERICO_LOWER_CASE, "impegno");
	}
	
	/**
	 * utilizzata nelle sotto classi che trattano l'inserimento 
	 * del impegno
	 * @return
	 */
	public boolean sonoInInserimento(){
		return true;
	}

}

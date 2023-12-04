/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;

/**
 * classe utilizzata per la definizione univoca del modello dati in sessione
 * ed anche per la renderizzazione automatica delle label a seconda
 * che si tratti di impegno o accertamento
 * @author paolos
 *
 */
public class ActionKeyInserisceAccertamento extends WizardInserisciMovGestAction {

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getActionKey() {
		return "inserisceAccertamento";
	}
	
	protected void caricaLabelsInserisci(int step) {
		switch (step) {
		case 1:
			model.getLabels().put(FORM, "inserisceAccertamento");
			break;
		case 2:
			model.getLabels().put(FORM, "inserisceAccertamentoStep2");
			break;
		case 3:
			model.getLabels().put(FORM, "inserisceAccertamentoStep3");
			break;
		default:
			model.getLabels().put(FORM, "inserisceAccertamento");
			break;
		}
		model.getLabels().put(LABEL_TITOLO, "Inserisci accertamento");
		model.getLabels().put(LABEL_STEP1, "Dati accertamento");
		model.getLabels().put(LABEL_STEP3, "Accertamenti pluriennali");
		model.getLabels().put(LABEL_OGGETTO_GENERICO, "Accertamento");
		model.getLabels().put(LABEL_OGGETTO_GENERICO_LOWER_CASE, "accertamento");
	}
	
	/**
	 * utilizzata nelle sotto classi che trattano l'inserimento 
	 * del accertamento
	 * @return
	 */
	public boolean sonoInInserimento(){
		return true;
	}
	
	@Override
	//SIAC-7667
	protected boolean isPerimetroSanitarioCongruenteConGsa(CapitoloImpegnoModel capitolo) {
		return capitolo.getCodicePerimetroSanitarioEntrata() != null && CODICE_PERIMETRO_SANITARIO_ENTRATA_GSA.equals(capitolo.getCodicePerimetroSanitarioEntrata());
	}

}

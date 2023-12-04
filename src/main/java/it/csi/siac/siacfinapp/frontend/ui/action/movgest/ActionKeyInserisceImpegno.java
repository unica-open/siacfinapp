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
	
	@Override
	//SIAC-7667
	protected boolean isPerimetroSanitarioCongruenteConGsa(CapitoloImpegnoModel capitolo) {
		return capitolo.getCodicePerimetroSanitarioSpesa() != null && CODICE_PERIMETRO_SANITARIO_SPESA_GSA.equals(capitolo.getCodicePerimetroSanitarioSpesa());
	}
	//SIAC-8674
	@Override
	protected boolean caricaListaModificheCollegateAccertamento() {
		//in inserimento impegno, le modifiche collegate non servono: non ci sono ancora modifiche sull'impegno visto che lo si sta inserendo ora
		//ed anche l'importo "pending" non può esserci
		return false;
	}

}

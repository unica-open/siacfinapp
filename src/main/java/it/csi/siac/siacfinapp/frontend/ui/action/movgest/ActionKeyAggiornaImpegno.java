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
public class ActionKeyAggiornaImpegno extends WizardAggiornaSubMovGestAction {

	private static final long serialVersionUID = 1L;

	protected static final String CODICE_MOTIVO_ROR_MANTENERE = "RORM";

	protected static final String[] CODICI_MOTIVO_ROR = new String[] {CODICE_MOTIVO_ROR_MANTENERE,"VALTRI", "VTRAS", "VPCON", "VCMUT", "VFAEN", "CROR", "REIMP", "VPN"};
	
	@Override
	public String getActionKey() {
		return "aggiornaImpegno";
	}
	
	public boolean isAbilitatoAggiornamentoGenerico(){
		return isAbilitatoAggiornaAnnullaImpegno(model.getImpegnoInAggiornamento());
	}
	
	/**
	 * utilizzata nelle sotto classi che trattano l'aggiornamento 
	 * del impegno
	 * @return
	 */
	public boolean sonoInInserimento(){
		return false;
	}
	
	protected void caricaLabelsAggiorna(int step) {
		switch (step) {
		case 1:
			model.getLabels().put(FORM, "aggiornaImpegnoStep1");
			break;
		case 2:
			model.getLabels().put(FORM, "aggiornaImpegnoStep2");
			break;
		case 3:
			model.getLabels().put(FORM, "inserisciMovSpesaSoggetto");
			break;
		case 4:
			model.getLabels().put(FORM, "inserisciModificaMovimentoSpesaAccSoggetto");
			break;		
		default:
			model.getLabels().put(FORM, "aggiornaImpegnoStep1");
			break;
		}
		model.getLabels().put(LABEL_TITOLO, "Aggiorna Impegno");
		model.getLabels().put(LABEL_STEP1, "Dati impegno");
		model.getLabels().put(LABEL_OGGETTO_GENERICO_PADRE, "Impegno");
		model.getLabels().put(LABEL_OGGETTO_GENERICO_PADRE_LOWER_CASE, "impegno");
		model.getLabels().put(LABEL_OGGETTO_GENERICO, "Subimpegno");
		model.getLabels().put(LABEL_OGGETTO_GENERICO_VERBO, "subimpegnare");
		model.getLabels().put(LABEL_TAB_SUB, "Sub Impegno");
		model.getLabels().put(LABEL_TAB_SUB_LINK, "gotoSubImpegno_false");
		model.getLabels().put(LABEL_TAB_AGG_ACC, "Impegno");
		model.getLabels().put(LABEL_TAB_AGG_ACC_LINK, "gotoAggiornaImpegnoStep1_false");
		model.getLabels().put(LABEL_TAB_MOV_SPE, "Modifiche");
		model.getLabels().put(LABEL_TAB_MOV_SPE_LINK, "gotoElencoMovimentoSpesa_false");
	}
	
	protected void caricaLabelsAggiornaSub(int step) {
		switch (step) {
		case 1:
			model.getLabels().put(FORM, "gestioneSubimpegno");
			break;
		case 2:
			model.getLabels().put(FORM, "gestioneSubimpegnoStep2");
			break;
		default:
			model.getLabels().put(FORM, "gestioneSubimpegno");
			break;
		}
		model.getLabels().put(LABEL_TITOLO, "Aggiorna Impegno");
		model.getLabels().put(LABEL_STEP1, "Dati impegno");
		model.getLabels().put(LABEL_OGGETTO_GENERICO_PADRE, "Impegno");
		model.getLabels().put(LABEL_OGGETTO_GENERICO, "Subimpegno");
		model.getLabels().put(LABEL_OGGETTO_GENERICO_VERBO, "subimpegnare");
	}
}

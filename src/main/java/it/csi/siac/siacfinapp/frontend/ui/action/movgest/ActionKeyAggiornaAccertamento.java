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
public class ActionKeyAggiornaAccertamento extends WizardAggiornaSubMovGestAction {
	
	private static final long serialVersionUID = 1L;
		
	
	
	protected static final String CODICE_MOTIVO_ROR_MANTENERE = "RORM";
	
	protected static final String[] CODICI_MOTIVO_ROR = new String[] {CODICE_MOTIVO_ROR_MANTENERE,"VALTRI", "VTRAS", "VPCON", "VCMUT", "VFAEN", "CROR", "REIMP", "VPN"};

	@Override
	public String getActionKey() {
		return "aggiornaAccertamento";
	}
	
	public boolean isAbilitatoAggiornamentoGenerico(){
		return isAbilitatoAggiornaAnnullaAccertamento(model.getAccertamentoInAggiornamento());
	}
	
	/**
	 * utilizzata nelle sotto classi che trattano l'aggiornamento 
	 * del accertamento
	 * @return
	 */
	public boolean sonoInInserimento(){
		return false;
	}
	
	/**
	 * SIAC-7976
	 * A seguito della SIAC-7804 si slega il controllo sullo stato del movimento in caso
	 * di movimento decentrato dal metodo generico isAbilitatoAggiornaAnnullaAccertamento().
	 * 
	 * @return boolean
	 */
	@Override
	public boolean mostraCompilazioneGuidataProvvedimento() {
		// se decentrato dobbiamo assicurarci che sia un movimento in stato PROVVISORIO
//		return (("PROVVISORIO".equals(model.getAccertamentoInAggiornamento().getDescrizioneStatoOperativoMovimentoGestioneEntrata()) 
//				&& isAbilitatoGestisciAccertamentoDec()) 
//				// controllo per le modifiche di importo e soggetto 
//				|| (isAbilitatoGestisciAccertamentoDec() && permettoCompilazioneProvvedimentoActionDEC()))
//				// abilito in caso non sia un decentrato
//				|| !isAbilitatoGestisciAccertamentoDec();
		return super.mostraCompilazioneGuidataProvvedimento();
	}
	
	
	/**
	 * caricamento della label aggiornamento
	 * @param step
	 */
	protected void caricaLabelsAggiorna(int step) {
		switch (step) {
		case 1:
			//AGGIORNA ACCERTAMENTO STEP 1
			model.getLabels().put(FORM, "aggiornaAccertamentoStep1");
			break;
		case 2:
			//AGGIORNA ACCERTAMENTO STEP 2
			model.getLabels().put(FORM, "aggiornaAccertamentoStep2");
			break;
		case 3:
			//INSERISCI MODIFICA MOVIMENTO SPESA SOGGETTO
			model.getLabels().put(FORM, "inserisciModificaMovimentoSpesaSoggetto");
			break;
		case 4:
			//INSERISCI MODIFICA MOVIMENTO SPESA ACC SOGGETTO
			model.getLabels().put(FORM, "inserisciModificaMovimentoSpesaAccSoggetto");
			break;	
		default:
			//AGGIORNAACCERTAMENTO STEP 1 usato come default
			model.getLabels().put(FORM, "aggiornaAccertamentoStep1");
			break;
		}
		model.getLabels().put(LABEL_TITOLO, "Aggiorna accertamento");
		model.getLabels().put(LABEL_STEP1, "Dati accertamento");
		model.getLabels().put(LABEL_OGGETTO_GENERICO, "Subaccertamento");
		model.getLabels().put(LABEL_OGGETTO_GENERICO_PADRE, "Accertamento");
		model.getLabels().put(LABEL_OGGETTO_GENERICO_PADRE_LOWER_CASE, "accertamento");
		model.getLabels().put(LABEL_OGGETTO_GENERICO_VERBO, "subaccertare");
		model.getLabels().put(LABEL_TAB_SUB, "Sub Accertamento");
		model.getLabels().put(LABEL_TAB_SUB_LINK, "gotoSubImpegno_false");
		model.getLabels().put(LABEL_TAB_AGG_ACC, "Accertamento");
		model.getLabels().put(LABEL_TAB_AGG_ACC_LINK, "gotoAggiornaAccertamento_false");
		model.getLabels().put(LABEL_TAB_MOV_SPE, "Modifiche");
		model.getLabels().put(LABEL_TAB_MOV_SPE_LINK, "gotoElencoMovimentoSpesa_false");
	}
	
	/**
	 * caricamento della label aggiornamento sub
	 * 
	 * @param step
	 */
	protected void caricaLabelsAggiornaSub(int step) {
		switch (step) {
		case 1:
			//GESTIONE SUB ACCERTAMENTO
			model.getLabels().put(FORM, "gestioneSubaccertamento");
			break;
		case 2:
			//GESTIONE SUB ACCERTAMENTO STEP 2
			model.getLabels().put(FORM, "gestioneSubaccertamentoStep2");
			break;
		default:
			//GESTIONE SUB ACCERTAMENTO
			model.getLabels().put(FORM, "gestioneSubaccertamento");
			break;
		}
		model.getLabels().put(LABEL_TITOLO, "Aggiorna accertamento");
		model.getLabels().put(LABEL_STEP1, "Dati accertamento");
		model.getLabels().put(LABEL_OGGETTO_GENERICO, "Subaccertamento");
		model.getLabels().put(LABEL_OGGETTO_GENERICO_PADRE, "Accertamento");
		model.getLabels().put(LABEL_OGGETTO_GENERICO_VERBO, "subaccertare");
	}

	/**
	 * @return
	 */
	//SIAC-5943: il flag no fattura e' solo sulla testata
//	@Override
//	protected boolean condizioniRedirezioneContabilitaGeneraleSpecifiche() {
//		return model.getStep1ModelSubimpegno() != null && model.getStep1ModelSubimpegno().getFlagFattura() != null? webAppSiNoToBool(model.getStep1ModelSubimpegno().getFlagFattura()) : false;
//	}
	@Override
	//SIAC-7667
	protected boolean isPerimetroSanitarioCongruenteConGsa(CapitoloImpegnoModel capitolo) {
		return capitolo.getCodicePerimetroSanitarioEntrata() != null && CODICE_PERIMETRO_SANITARIO_ENTRATA_GSA.equals(capitolo.getCodicePerimetroSanitarioEntrata());
	}
	
}

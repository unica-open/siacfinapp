/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.subimpegno;

/**
 * Completamento e validazione della prima nota integrata sul subimpegno, base.
 * @author elisa
 * @version 1.0.0 - 12-03-2018
 */
public abstract class CompletaValidaSubImpegnoInsPrimaNotaIntegrataBaseModel extends GestioneSubImpegnoPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = -8512505090348801364L;

	@Override
	public boolean isAggiornamento() {
		return false;
	}
	
	@Override
	public boolean isValidazione() {
		return true;
	}

	@Override
	public boolean isFromRegistrazione() {
		return true;
	}

	@Override
	public String getBaseUrl() {
		return "completaValidaSubImpegnoInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

}

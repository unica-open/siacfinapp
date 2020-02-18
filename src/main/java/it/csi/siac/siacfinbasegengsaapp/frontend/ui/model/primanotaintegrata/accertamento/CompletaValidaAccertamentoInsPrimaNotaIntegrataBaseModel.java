/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.accertamento;


/**
 * Completamento e validazione della prima nota integrata sull'accertamento, base.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/10/2015
 */
public abstract class CompletaValidaAccertamentoInsPrimaNotaIntegrataBaseModel extends GestioneAccertamentoPrimaNotaIntegrataBaseModel{

	/** Per la serializzazione */
	private static final long serialVersionUID = 3123916901257559781L;

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
		return "completaValidaAccertamentoInsPrimaNotaIntegrata" + getAmbito().getSuffix();
	}

}

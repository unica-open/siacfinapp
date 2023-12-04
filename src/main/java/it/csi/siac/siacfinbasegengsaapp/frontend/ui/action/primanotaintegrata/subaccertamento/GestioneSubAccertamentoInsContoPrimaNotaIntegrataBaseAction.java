/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.subaccertamento;

import java.math.BigDecimal;

import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaContoPrimaNotaIntegrataAction;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.subaccertamento.GestioneSubAccertamentoPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacfinser.model.Accertamento;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, subaccertamento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 21/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneSubAccertamentoInsContoPrimaNotaIntegrataBaseAction<M extends GestioneSubAccertamentoPrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaContoPrimaNotaIntegrataAction <Accertamento, M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8015545551998956166L;
	
	@Override
	protected BigDecimal getImportoMovimento() {
		return model.getSubAccertamento() != null && model.getSubAccertamento().getImportoIniziale() != null ? model.getSubAccertamento().getImportoIniziale() : BigDecimal.ZERO;
	}
	
}


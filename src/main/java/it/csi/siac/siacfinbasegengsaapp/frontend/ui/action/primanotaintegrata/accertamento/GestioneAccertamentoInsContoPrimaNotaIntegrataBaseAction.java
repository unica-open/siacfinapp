/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.accertamento;

import java.math.BigDecimal;

import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaContoPrimaNotaIntegrataAction;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.accertamento.GestioneAccertamentoPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacfinser.model.Accertamento;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, accertamento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneAccertamentoInsContoPrimaNotaIntegrataBaseAction<M extends GestioneAccertamentoPrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaContoPrimaNotaIntegrataAction <Accertamento, M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6605882151619320481L;
	
	@Override
	protected BigDecimal getImportoMovimento() {
		return model.getAccertamento() != null && model.getAccertamento().getImportoIniziale() != null ? model.getAccertamento().getImportoIniziale() : BigDecimal.ZERO;
	}
	
}


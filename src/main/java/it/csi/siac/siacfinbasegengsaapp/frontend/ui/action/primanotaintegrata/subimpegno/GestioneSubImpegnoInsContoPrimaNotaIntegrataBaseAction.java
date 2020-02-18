/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.subimpegno;

import java.math.BigDecimal;

import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaContoPrimaNotaIntegrataAction;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.subimpegno.GestioneSubImpegnoPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacfinser.model.Impegno;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, subimpegno.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 21/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneSubImpegnoInsContoPrimaNotaIntegrataBaseAction<M extends GestioneSubImpegnoPrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaContoPrimaNotaIntegrataAction<Impegno,  M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2898576116775487641L;

	
	@Override
	protected BigDecimal getImportoMovimento() {
		return model.getSubImpegno() != null && model.getSubImpegno().getImportoIniziale() != null ? model.getSubImpegno().getImportoIniziale() : BigDecimal.ZERO;
	}

}


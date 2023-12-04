/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

public class ComparatorModalitaPagamentoSoggettoByCodice implements Comparator<ModalitaPagamentoSoggetto>, Serializable{

	private static final long serialVersionUID = 6909948621411055775L;

	public static final ComparatorModalitaPagamentoSoggettoByCodice INSTANCE = new ComparatorModalitaPagamentoSoggettoByCodice();

	private ComparatorModalitaPagamentoSoggettoByCodice() {
		super();
	}

	@Override
	public int compare(ModalitaPagamentoSoggetto o1, ModalitaPagamentoSoggetto o2){
		
		//il confronto tra ModalitaPagamentoSoggetto
		//avviene rispetto ai codici
		
		if (o1 == o2)
			return 0;

		if (o1 == null || o1.getCodiceModalitaPagamento() == null)
			return -1;

		if (o2 == null || o2.getCodiceModalitaPagamento() == null)
			return 1;

		if (Integer.parseInt(o1.getCodiceModalitaPagamento()) > Integer.parseInt(o2.getCodiceModalitaPagamento()))
			return 1;

		if (Integer.parseInt(o1.getCodiceModalitaPagamento()) < Integer.parseInt(o2.getCodiceModalitaPagamento()))
			return -1;

		return 0;
	}

}

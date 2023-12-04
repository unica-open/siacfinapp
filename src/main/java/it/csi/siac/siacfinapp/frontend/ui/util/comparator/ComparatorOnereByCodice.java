/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import it.csi.siac.siacfinser.model.soggetto.Onere;

public class ComparatorOnereByCodice implements Comparator<Onere>, Serializable {

	private static final long serialVersionUID = 1155824872118779451L;
	public static final ComparatorOnereByCodice INSTANCE = new ComparatorOnereByCodice();

	private ComparatorOnereByCodice() {
		super();
	}

	@Override
	public int compare(Onere o1, Onere o2) {
		
		//il confronto tra Onere
		//avviene rispetto ai codici
		
		if (o1 == o2)
			return 0;

		if (o1 == null || o1.getOnereCod() == null)
			return -1;

		if (o2 == null || o2.getOnereCod() == null)
			return 1;

		return o1.getOnereCod().compareTo(o2.getOnereCod());
	}

}

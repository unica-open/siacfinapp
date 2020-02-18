/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import it.csi.siac.siacfinser.model.soggetto.ClassificazioneSoggetto;

public class ComparatorClassificazioneSoggettoByCodice implements Comparator<ClassificazioneSoggetto>, Serializable{

	private static final long serialVersionUID = 1155824872118779451L;
	public static final ComparatorClassificazioneSoggettoByCodice INSTANCE = new ComparatorClassificazioneSoggettoByCodice();

	private ComparatorClassificazioneSoggettoByCodice() {
		super();
	}

	@Override
	public int compare(ClassificazioneSoggetto o1, ClassificazioneSoggetto o2) {
		
		//il confronto tra le classificazioni del soggetto
		//avviene rispetto al codice
		
		if (o1 == o2)
			return 0;

		if (o1 == null || o1.getSoggettoClasseCode() == null)
			return -1;

		if (o2 == null || o2.getSoggettoClasseCode() == null)
			return 1;

		return o1.getSoggettoClasseCode().compareTo(o2.getSoggettoClasseCode());
	}

}

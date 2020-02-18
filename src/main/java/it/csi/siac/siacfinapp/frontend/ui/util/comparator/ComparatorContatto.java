/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import it.csi.siac.siacfinser.model.soggetto.Contatto;

public class ComparatorContatto implements Comparator<Contatto>, Serializable {
	
	private static final long serialVersionUID = 1155824872118779451L;
	public static final ComparatorContatto INSTANCE = new ComparatorContatto();

	private ComparatorContatto() {
		super();
	}

	@Override
	public int compare(Contatto o1, Contatto o2) {
		
		//il confronto tra Contatti
		//avviene rispetto al codice
		
		if (o1 == o2)
			return 0;

		if (o1 == null || o1.getContattoCod() == null)
			return -1;

		if (o2 == null || o2.getContattoCod() == null)
			return 1;

		
		int compare = o1.getContattoCod().compareTo(o2.getContattoCod());
		
		if (compare != 0)
			return compare;
			
		//a parita di codice tuttavia,
		//ragioniamo su differenze di descrizione:
	
		return o1.getDescrizione().compareTo(o2.getDescrizione());
	}
}

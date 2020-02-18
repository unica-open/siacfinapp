/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;

public class ComparatorIndirizzoSoggettoByIndirizzoId implements Comparator<IndirizzoSoggetto>, Serializable{
	
	
	private static final long serialVersionUID = -3046628867674289880L;
	public static final ComparatorIndirizzoSoggettoByIndirizzoId INSTANCE = new ComparatorIndirizzoSoggettoByIndirizzoId();

	private ComparatorIndirizzoSoggettoByIndirizzoId() {
		super();
	}

	@Override
	public int compare(IndirizzoSoggetto o1, IndirizzoSoggetto o2) {
		
		//il confronto tra IndirizzoSoggetto
		//avviene rispetto agli id
		
		if (o1 == o2)
			return 0;

		if (o1 == null || o1.getIndirizzoId() == null)
			return -1;

		if (o2 == null || o2.getIndirizzoId() == null)
			return 1;

		return o1.getIndirizzoId().compareTo(o2.getIndirizzoId());
	}
	
}

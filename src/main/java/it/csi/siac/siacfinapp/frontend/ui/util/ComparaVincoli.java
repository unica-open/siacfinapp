/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util;

import java.math.BigDecimal;
import java.util.Comparator;

import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;

public class ComparaVincoli implements Comparator<VincoloImpegno> {
	
	public int compare (VincoloImpegno a, VincoloImpegno b){                                 
		BigDecimal importo1 = a.getImporto();
		BigDecimal importo2 = b.getImporto();
		int result = importo1.compareTo(importo2);
		if(result == 0){
			result = Integer.valueOf(a.getUid()).compareTo(Integer.valueOf(b.getUid()));
		}
		return result;
	}
}

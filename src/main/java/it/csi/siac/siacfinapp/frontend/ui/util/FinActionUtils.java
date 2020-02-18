/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util;


import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ImpegniPluriennaliModel;

/**
 * 
 * Classe per mettere a fattore comune logiche generiche ripetute qua 
 * e la per le classi action
 * 
 * @author claudio.picco
 *
 */
public final class FinActionUtils {
	
	protected static LogUtil log = new LogUtil(FinActionUtils.class);
	
	
	/**
	 * Dato l'importo pluriennale string ne estra la versione BigDecimal.
	 * 
	 * @param currentImpegnoPluriennale
	 * @return
	 */
	public final static BigDecimal convertiImportiPluriennaleString(ImpegniPluriennaliModel currentImpegnoPluriennale){
		BigDecimal importoBD=BigDecimal.ZERO;
		if(!StringUtils.isEmpty(currentImpegnoPluriennale.getImportoImpPluriennaleString())){
			if(currentImpegnoPluriennale.getImportoImpPluriennaleString().split(",").length <= 2){
				if(currentImpegnoPluriennale.getImportoImpPluriennaleString().split(".").length <= 2){
					importoBD = convertiImportoToBigDecimal(currentImpegnoPluriennale.getImportoImpPluriennaleString());
				}
			}
		}
		return importoBD;
	}
	
	/**
	 * Metodo centralizzato per convertire in BigDecimal un importo formattato per le action
	 * @param importoFormattato
	 * @return
	 */
	public final static BigDecimal convertiImportoToBigDecimal(String importoFormattato) {

		BigDecimal importoDB = null;

		if(null!=importoFormattato && importoFormattato.contains(".")){
			importoFormattato = importoFormattato.replace(".", "");
			if(FinStringUtils.isEmpty(importoFormattato)){
				importoFormattato = "0";
			}
		}
		
		
		if(null!=importoFormattato && importoFormattato.contains(",")){

			importoFormattato = importoFormattato.replace(",", ".");
			if(FinStringUtils.isEmpty(importoFormattato)) importoFormattato = "0";
		}	
		
		
		importoDB = new BigDecimal(importoFormattato).setScale(2,BigDecimal.ROUND_HALF_UP);

		return importoDB;
	}

}

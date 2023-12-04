/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;


public final class CodiciOperazioni {

	
	public static final Map<String, List<String>> MAP_CORRISPONDENZE_DEC = new HashMap<String, List<String>>(0);
	public static final List<String> AZIONI_DECENTRATO = new ArrayList<String>(0);
	/*
	 *  Associazione delle operazione di decentrato in base 
	 *  all'azione di entrata
	 *  
	 *  ad esempio:
	 *  leggiImpegno --> gestisciImpegnoDEC
	 *  
	 */
	static {
		List<String> ls1 = new ArrayList<String>();
		ls1.add(AzioneConsentitaEnum.OP_SPE_gestisciImpegnoDecentrato.getNomeAzione());
		MAP_CORRISPONDENZE_DEC.put(AzioneConsentitaEnum.LEGGI_IMP.getNomeAzione(), ls1);
		ls1 = new ArrayList<String>();
		ls1.add(AzioneConsentitaEnum.OP_ENT_gestisciAccertamentoDecentrato.getNomeAzione());
		MAP_CORRISPONDENZE_DEC.put(AzioneConsentitaEnum.LEGGI_ACC.getNomeAzione(), ls1);
	
		AZIONI_DECENTRATO.add(AzioneConsentitaEnum.OP_SPE_gestisciImpegnoDecentrato.getNomeAzione());
		AZIONI_DECENTRATO.add(AzioneConsentitaEnum.OP_ENT_gestisciAccertamentoDecentrato.getNomeAzione());
		
		AZIONI_DECENTRATO.add(AzioneConsentitaEnum.OP_SOG_inserisciSoggDec.getNomeAzione());
		AZIONI_DECENTRATO.add(AzioneConsentitaEnum.OP_SOG_leggiSoggDec.getNomeAzione());
		
	}
	
	private CodiciOperazioni() {
		// Non permettere l'instanziazione
	}
}
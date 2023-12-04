/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util;

import java.util.List;

import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione.StatoOperativoModificaMovimentoGestione;

/**
 * SIAC-6997-BLOCCOROR: Classe di utilit&agrave; per centralizzare i controlli sui blocchi ROR nelle varie casistiche previste:
 * - Inserimendo documento di Spesa
 * 
 * 
 * @author GM
 * @version 1.0.0 - 18/02/2020
 *
 */

//SIAC-6997-BLOCCOROR (esiste anche un'altra versione su BilApplication, progetto su cui questo non ha visibilità, nè viceversa)
public class VerificaBloccoRORHelper {
	
	/** Non instanziare la classe */
	private VerificaBloccoRORHelper() {}
	
	/**
	 * Alla presenza dell’azione "OP-COM-insAllegatoAttoNoRes - Blocco su liquidazione residui" di blocco contabilizzazione residui associata all’utenza di login, 
	 * non è consentita la copertura di un documento passivo attraverso impegni/sub impegni di anni precedenti (Anno impegno < anno bilancio). 
	 * L’unica eccezione si ha quando l’impegno/subimpegno residuo presenta modifica "ROR - da mantenere" associata ad un provvedimento definitivo in stato non annullato, 
	 * in tale caso è possibile inserire un impegno/sub-impegno a residuo.
	 * 
	 * @param azioniConsentite List<AzioneConsentita> presenti in sessione
	 * @param impegno l'impegno
	 * @param annoEsercizio l'anno di esercizio
	 * 
	 * @return escludiXBloccoROR boolean contenente l'esito dell'operazione: true se non supera il controllo, false se lo supera.
	 * 
	 * */
	public static boolean escludiImpegnoPerBloccoROR(List<AzioneConsentita> azioniConsentite, Impegno impegno, Integer annoEsercizio){
		boolean escludiXBloccoROR = false;
		if(isAzioneAbilitata(AzioneConsentitaEnum.BLOCCO_SU_LIQ_IMP_RESIDUI.getNomeAzione(), azioniConsentite) && 
			(impegno.getAnnoMovimento() > 0 && annoEsercizio != null && impegno.getAnnoMovimento() < annoEsercizio)){
			escludiXBloccoROR = true;
			if(impegno.getListaModificheMovimentoGestioneSpesa() != null && !impegno.getListaModificheMovimentoGestioneSpesa().isEmpty()){
				for(int j = 0; j < impegno.getListaModificheMovimentoGestioneSpesa().size(); j++){
					ModificaMovimentoGestione mmg = impegno.getListaModificheMovimentoGestioneSpesa().get(j);
					if(mmg.getTipoModificaMovimentoGestione() != null && mmg.getTipoModificaMovimentoGestione().equalsIgnoreCase(ModificaMovimentoGestione.CODICE_ROR_DA_MANTENERE) &&
						mmg.getAttoAmministrativo() != null && mmg.getAttoAmministrativo().getStatoOperativo() != null && mmg.getStatoOperativoModificaMovimentoGestione() != null &&
						mmg.getAttoAmministrativo().getStatoOperativo().equals(StatoOperativoAtti.DEFINITIVO.name()) && 
						!(mmg.getStatoOperativoModificaMovimentoGestione().name().equals(StatoOperativoModificaMovimentoGestione.ANNULLATO.name()))){
							escludiXBloccoROR = false;
							break;
					}
				}
			}
		}
		return escludiXBloccoROR;
	}
	
	public static boolean escludiAccertamentoPerBloccoROR(List<AzioneConsentita> azioniConsentite, Accertamento accertamento, Integer annoEsercizio){
		boolean escludiXBloccoROR = false;
		if(isAzioneAbilitata(AzioneConsentitaEnum.BLOCCO_SU_INCASSI_RESIDUI.getNomeAzione(), azioniConsentite) && 
			(accertamento.getAnnoMovimento() > 0 && annoEsercizio != null && accertamento.getAnnoMovimento() < annoEsercizio)){
			escludiXBloccoROR = true;
			if(accertamento.getListaModificheMovimentoGestioneEntrata() != null && !accertamento.getListaModificheMovimentoGestioneEntrata().isEmpty()){
				for(int j = 0; j < accertamento.getListaModificheMovimentoGestioneEntrata().size(); j++){
					ModificaMovimentoGestione mmg = accertamento.getListaModificheMovimentoGestioneEntrata().get(j);
					if(mmg.getTipoModificaMovimentoGestione() != null && mmg.getTipoModificaMovimentoGestione().equalsIgnoreCase(ModificaMovimentoGestione.CODICE_ROR_DA_MANTENERE) &&
						mmg.getAttoAmministrativo() != null && mmg.getAttoAmministrativo().getStatoOperativo() != null && mmg.getStatoOperativoModificaMovimentoGestione() != null &&
						mmg.getAttoAmministrativo().getStatoOperativo().equals(StatoOperativoAtti.DEFINITIVO.name()) && 
						!(mmg.getStatoOperativoModificaMovimentoGestione().name().equals(StatoOperativoModificaMovimentoGestione.ANNULLATO.name()))){
							escludiXBloccoROR = false;
							break;
					}
				}
			}
		}
		return escludiXBloccoROR;
	}
	
	
	private static boolean isAzioneAbilitata(String codeAzione, List<AzioneConsentita> azioniConsentite) {
		boolean abilitato = false;
		if (azioniConsentite!=null && azioniConsentite.size()>0){
			try {
				for (AzioneConsentita azioneConsentita : azioniConsentite) {
					if (azioneConsentita.getAzione().getNome().equals(codeAzione)) {
						//OK ABILITATO
						abilitato=true;
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return abilitato;
	}
}

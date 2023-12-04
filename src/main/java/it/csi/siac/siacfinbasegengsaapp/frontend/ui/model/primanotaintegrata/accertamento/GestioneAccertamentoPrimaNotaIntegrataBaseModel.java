/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.accertamento;

import java.util.EnumSet;

import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;

/**
 * Classe base di model per la gestione della prima nota integrata sull'accertamento.
 * 
 * @author Marchino Alessandro
 * @version 1.1.0 - 12/10/2015 - gestione GEN/GSA
 */
public abstract class GestioneAccertamentoPrimaNotaIntegrataBaseModel extends BaseInserisciAggiornaPrimaNotaIntegrataBaseModel<Accertamento>{

	/** Per la serializzazione */
	private static final long serialVersionUID = -3970075685504605509L;
	
	private Accertamento accertamento;

	/**
	 * @return the accertamento
	 */
	public Accertamento getAccertamento() {
		return accertamento;
	}

	/**
	 * @param accertamento the accertamento to set
	 */
	public void setAccertamento(Accertamento accertamento) {
		this.accertamento = accertamento;
	}
	
	@Override
	public String getConsultazioneSubpath() {
		return "Accertamento";
	}

	/**
	 * Crea una request per il servizio di {@link RicercaAccertamentoPerChiave}.
	 * @param acc l'accertamento da ricercare
	 * 
	 * @return la request creata
	 */
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato(Accertamento acc) {
		RicercaAccertamentoPerChiaveOttimizzato req = creaRequest(RicercaAccertamentoPerChiaveOttimizzato.class);
		
		req.setNumPagina(0);
		req.setNumRisultatiPerPagina(ELEMENTI_PER_PAGINA_RICERCA);
		req.setCaricaSub(false);
		
		RicercaAccertamentoK ricercaAccertamentoK = new RicercaAccertamentoK();
		ricercaAccertamentoK.setAnnoEsercizio(getAnnoEsercizioInt());
		ricercaAccertamentoK.setAnnoAccertamento(acc.getAnnoMovimento());
		ricercaAccertamentoK.setNumeroAccertamento(acc.getNumeroBigDecimal());
		
		req.setpRicercaAccertamentoK(ricercaAccertamentoK);
		req.setEnte(getEnte());
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		//Non richiedo NESSUN importo derivato del capitolo
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class)); 
		
		req.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		
		return req;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaContiConciliazionePerClasse}.
	 * 
	 * @param classeDiConciliazione la classeDiConciliazione per cui ricercare i conti di conciliazione
	 * @return la requet creata
	 */
	public RicercaContiConciliazionePerClasse creaRequestRicercaContiConciliazionePerClasse(ClasseDiConciliazione classeDiConciliazione) {
		RicercaContiConciliazionePerClasse req = creaRequest(RicercaContiConciliazionePerClasse.class);
		req.setClasseDiConciliazione(classeDiConciliazione);
		req.setCapitolo(getAccertamento().getCapitoloEntrataGestione());
		req.setSoggetto(getAccertamento().getSoggetto());
		return req;
	}
	
	/**
	 * Gets the anno movimento.
	 *
	 * @return the anno movimento
	 */
	public String  getAnnoMovimento() {
		return getAccertamento() != null ? String.valueOf(getAccertamento().getAnnoMovimento()) : "0";
	}
	
	/**
	 * Gets the numero movimento.
	 *
	 * @return the numero movimento
	 */
	public String  getNumeroMovimento() {
		return getAccertamento() != null ? getAccertamento().getNumeroBigDecimal().toString() : "0";
	}

}



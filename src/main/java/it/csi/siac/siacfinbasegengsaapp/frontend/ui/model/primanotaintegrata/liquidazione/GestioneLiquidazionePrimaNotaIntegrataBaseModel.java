/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.liquidazione;

import java.util.Date;

import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiave;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ric.RicercaLiquidazioneK;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;

/**
 * Classe di modello per la gestione della prima nota integrata sulla liquidazione.
 */
public abstract class GestioneLiquidazionePrimaNotaIntegrataBaseModel extends BaseInserisciAggiornaPrimaNotaIntegrataBaseModel<Liquidazione>{

	/** Per la serializzazione */
	private static final long serialVersionUID = -8279890168052512576L;
	private Liquidazione liquidazione;

	/**
	 * @return the liquidazione
	 */
	public Liquidazione getLiquidazione() {
		return liquidazione;
	}

	/**
	 * @param liquidazione the liquidazione to set
	 */
	public void setLiquidazione(Liquidazione liquidazione) {
		this.liquidazione = liquidazione;
	}
	
	@Override
	public String getConsultazioneSubpath() {
		return "Liquidazione";
	}

	/**
	 * Crea una request per il servizio di {@link RicercaLiquidazionePerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaLiquidazionePerChiave creaRequestRicercaLiquidazionePerChiave() {
		RicercaLiquidazionePerChiave req = new RicercaLiquidazionePerChiave();
		RicercaLiquidazioneK pRicercaLiquidazioneK = new RicercaLiquidazioneK();
		pRicercaLiquidazioneK.setAnnoLiquidazione(getLiquidazione().getAnnoLiquidazione());
		pRicercaLiquidazioneK.setAnnoEsercizio(getAnnoEsercizioInt());
		pRicercaLiquidazioneK.setNumeroLiquidazione(getLiquidazione().getNumeroLiquidazione());
		pRicercaLiquidazioneK.setBilancio(getBilancio());
		Liquidazione liquidazioneRequest = new Liquidazione();
		liquidazioneRequest.setNumeroLiquidazione(getLiquidazione().getNumeroLiquidazione());
		liquidazioneRequest.setAnnoLiquidazione(getLiquidazione().getAnnoLiquidazione());
		pRicercaLiquidazioneK.setLiquidazione(liquidazioneRequest );
		req.setpRicercaLiquidazioneK(pRicercaLiquidazioneK );
		req.setRichiedente(getRichiedente());
		req.setDataOra(new Date());
		req.setEnte(getEnte());
		return req;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaContiConciliazionePerClasse}.
	 * 
	 * @param classeDiConciliazione la classeDiConciliazione per cui ricercare i conti di conciliazione
	 * @return la requet creata
	 */
	public RicercaContiConciliazionePerClasse creaRequestRicercaContiConciliazionePerClasse(ClasseDiConciliazione classeDiConciliazione) {
		RicercaContiConciliazionePerClasse req= new RicercaContiConciliazionePerClasse	();
		req.setClasseDiConciliazione(classeDiConciliazione);
		req.setRichiedente(getRichiedente());
		req.setCapitolo(getLiquidazione().getCapitoloUscitaGestione());
		req.setSoggetto(getLiquidazione().getSoggettoLiquidazione());
		return req;
	}
}



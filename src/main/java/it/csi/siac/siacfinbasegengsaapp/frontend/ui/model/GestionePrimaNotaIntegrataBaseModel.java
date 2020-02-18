/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.model;

import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Model base per la gestione della prima nota integrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/10/2015
 */
public abstract class GestionePrimaNotaIntegrataBaseModel  extends GenericContabilitaGeneraleModel {

	
	/** Per la serializzazione */
	private static final long serialVersionUID = 8172312600419805611L;
	private Integer uidRegistrazione;
	private String nomeAzione;
	private String nomeAzioneRedirezione;
	private boolean isValidazione;
	//SIAC-5333
	private boolean fromCDUDocumento;
	
	/**
	 * @return the uidRegistrazione
	 */
	public Integer getUidRegistrazione() {
		return uidRegistrazione;
	}
	/**
	 * @param uidRegistrazione the uidRegistrazione to set
	 */
	public void setUidRegistrazione(Integer uidRegistrazione) {
		this.uidRegistrazione = uidRegistrazione;
	}
	/**
	 * @return the nomeAzione
	 */
	public String getNomeAzione() {
		return nomeAzione;
	}
	/**
	 * @param nomeAzione the nomeAzione to set
	 */
	public void setNomeAzione(String nomeAzione) {
		this.nomeAzione = nomeAzione;
	}
	/**
	 * @return the nomeAzioneRedirezione
	 */
	public String getNomeAzioneRedirezione() {
		return nomeAzioneRedirezione;
	}
	/**
	 * @param nomeAzioneRedirezione the nomeAzioneRedirezione to set
	 */
	public void setNomeAzioneRedirezione(String nomeAzioneRedirezione) {
		this.nomeAzioneRedirezione = nomeAzioneRedirezione;
	}
	/**
	 * @return the isValidazione
	 */
	public boolean isValidazione() {
		return isValidazione;
	}
	/**
	 * @param isValidazione the isValidazione to set
	 */
	public void setValidazione(boolean isValidazione) {
		this.isValidazione = isValidazione;
	}
	
	/**
	 * @return the fromCDUDocumento
	 */
	public boolean isFromCDUDocumento() {
		return fromCDUDocumento;
	}
	/**
	 * @param fromCDUDocumento the fromCDUDocumento to set
	 */
	public void setFromCDUDocumento(boolean fromCDUDocumento) {
		this.fromCDUDocumento = fromCDUDocumento;
	}
	

	/**
	 * @return the ambito
	 */
	public abstract Ambito getAmbito();

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioRegistrazioneMovFin}.
	 * @param uidRegistrazioneMovFin l'uid della registrazione del movimento finanziario
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioRegistrazioneMovFin creaRequestRicercaDettaglioRegistrazioneMovFin(Integer uidRegistrazioneMovFin) {
		RicercaDettaglioRegistrazioneMovFin request = creaRequest(RicercaDettaglioRegistrazioneMovFin.class);
		
		RegistrazioneMovFin registrazione = new RegistrazioneMovFin();
		registrazione.setUid(uidRegistrazioneMovFin);
		registrazione.setBilancio(getBilancio());
		registrazione.setAmbito(getAmbito());
		request.setRegistrazioneMovFin(registrazione);
	
		return request;
	}
	
	/**
	 * Controlla se l'ente sia o meno abilitato gestione prima nota da finanziaria.
	 *
	 * @return true, if is ente abilitato gestione prima nota da finanziaria false altrimenti
	 */
	public boolean isEnteAbilitatoGestionePrimaNotaDaFinanziaria() {
		return "TRUE".equals(getEnte().getGestioneLivelli().get(TipologiaGestioneLivelli.GESTIONE_PNOTA_DA_FIN));
	}
	
}

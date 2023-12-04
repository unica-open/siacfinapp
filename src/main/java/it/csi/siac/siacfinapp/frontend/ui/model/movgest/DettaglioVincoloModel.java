/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * utilizzato nella pop up di aggiornamento vincolo
 * in inserisci/aggiorna impegno
 * 
 * @author 
 *
 */
public class DettaglioVincoloModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//importo da aggiornare
	private BigDecimal importoDaAggiornare;
	
	//importo da aggiornare old
	private BigDecimal importoDaAggiornareOld;
	
	//importo utilizzabile
	private BigDecimal importoUtilizzabile;
	
	//anno accertamento
	private String annoAccertamento;
	
	//numero accertamento
	private String numeroAccertamento;
	
	//importo da aggiornare formattato
	private String  importoDaAggiornareFormattato;
	
	//uid avanzo vincolo
	private String  uidAvanzoVincolo;

	//GETTER E SETTER:
	
	public String getImportoDaAggiornareFormattato() {
		return importoDaAggiornareFormattato;
	}
	public void setImportoDaAggiornareFormattato(String importoDaAggiornareFormattato) {
		this.importoDaAggiornareFormattato = importoDaAggiornareFormattato;
	}
	public BigDecimal getImportoDaAggiornare() {
		return importoDaAggiornare;
	}
	public void setImportoDaAggiornare(BigDecimal importoDaAggiornare) {
		this.importoDaAggiornare = importoDaAggiornare;
	}
	public BigDecimal getImportoUtilizzabile() {
		return importoUtilizzabile;
	}
	public void setImportoUtilizzabile(BigDecimal importoUtilizzabile) {
		this.importoUtilizzabile = importoUtilizzabile;
	}
	public String getAnnoAccertamento() {
		return annoAccertamento;
	}
	public void setAnnoAccertamento(String annoAccertamento) {
		this.annoAccertamento = annoAccertamento;
	}
	public String getNumeroAccertamento() {
		return numeroAccertamento;
	}
	public void setNumeroAccertamento(String numeroAccertamento) {
		this.numeroAccertamento = numeroAccertamento;
	}
	public BigDecimal getImportoDaAggiornareOld() {
		return importoDaAggiornareOld;
	}
	public void setImportoDaAggiornareOld(BigDecimal importoDaAggiornareOld) {
		this.importoDaAggiornareOld = importoDaAggiornareOld;
	}
	public String getUidAvanzoVincolo() {
		return uidAvanzoVincolo;
	}
	public void setUidAvanzoVincolo(String uidAvanzoVincolo) {
		this.uidAvanzoVincolo = uidAvanzoVincolo;
	}
}

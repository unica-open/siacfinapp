/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;

public class DettaglioQuotaOrdinativoModel extends GenericFinModel{

	
	private static final long serialVersionUID = 1L;
	
	//descrizione
	private String descrizione;
	
	//data esecuzione pagamento
	private String dataEsecuzionePagamento;
	
	//importo quota formattato
	private String importoQuotaFormattato;
	
	//numero quota
	private String numeroQuota;
	
	//uid quota
	private String uIdQuota;
	
	//data scadenza subord
	private String dataScadenzaSubOrd;
	
	//accertamento
	private String accertamento;
	
	
	//edita data esecuzione pagamento
	private boolean editaDataEsecuzionePagamento = false;
	
	/**
	 * @return the abilitadataEsecuzionePagamento
	 */
	public boolean isEditaDataEsecuzionePagamento() {
		return editaDataEsecuzionePagamento;
	}
	/**
	 * @param editaDataEsecuzionePagamento the editaDataEsecuzionePagamento to set
	 */
	public void setEditaDataEsecuzionePagamento(boolean editaDataEsecuzionePagamento) {
		this.editaDataEsecuzionePagamento = editaDataEsecuzionePagamento;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getDataEsecuzionePagamento() {
		return dataEsecuzionePagamento;
	}
	public void setDataEsecuzionePagamento(String dataEsecuzionePagamento) {
		this.dataEsecuzionePagamento = dataEsecuzionePagamento;
	}
	
	public String getImportoQuotaFormattato() {
		return importoQuotaFormattato;
	}
	public void setImportoQuotaFormattato(String importoQuotaFormattato) {
		this.importoQuotaFormattato = importoQuotaFormattato;
	}
	public String getNumeroQuota() {
		return numeroQuota;
	}
	public void setNumeroQuota(String numeroQuota) {
		this.numeroQuota = numeroQuota;
	}
	public String getuIdQuota() {
		return uIdQuota;
	}
	public void setuIdQuota(String uIdQuota) {
		this.uIdQuota = uIdQuota;
	}
	public String getDataScadenzaSubOrd() {
		return dataScadenzaSubOrd;
	}
	public void setDataScadenzaSubOrd(String dataScadenzaSubOrd) {
		this.dataScadenzaSubOrd = dataScadenzaSubOrd;
	}
	public String getAccertamento() {
		return accertamento;
	}
	public void setAccertamento(String accertamento) {
		this.accertamento = accertamento;
	}

}

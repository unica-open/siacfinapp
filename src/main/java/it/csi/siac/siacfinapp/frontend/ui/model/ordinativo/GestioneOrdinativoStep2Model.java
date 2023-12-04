/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;

public class GestioneOrdinativoStep2Model  extends GenericFinModel {

	private static final long serialVersionUID = 1L;

	//lista sub ordinativi pagamenti
	List<SubOrdinativoPagamento> listaSubOrdinativiPagamenti = new ArrayList<SubOrdinativoPagamento>();
	
	//lista sub ordinativi incasso
	List<SubOrdinativoIncasso> listaSubOrdinativiIncasso = new ArrayList<SubOrdinativoIncasso>();

	//lista liquidazioni
	List<Liquidazione> listaLiquidazioni = new ArrayList<Liquidazione>();
	
	//lista accertamento
	List<Accertamento> listaAccertamento= new ArrayList<Accertamento>();
	
	//lista accertamento originale
	List<Accertamento> listaAccertamentoOriginale= new ArrayList<Accertamento>();
	
	private boolean checkProseguiQuoteIncasso;
	private boolean checkAggiornaQuoteIncasso;
	
	private int resultSize;
	
	//lo uso per aggiornare la quota:
	private DettaglioQuotaOrdinativoModel dettaglioQuotaOrdinativoModel = new DettaglioQuotaOrdinativoModel();
	//lo uso per aggiornare la quota:
	private DettaglioQuotaOrdinativoModel copiaDettaglioQuotaOrdinativoModel = new DettaglioQuotaOrdinativoModel();
	//invece questo lo uso (sempre per aggiornare la quota) ma per tenere traccia dell'importo prima che venga modificato al conferma:
	private BigDecimal importoQuotaPrimaUpdate;
	/////////////////////////////////////////////////////////
	
	private String descrizioneQuota;
	private String importoQuotaFormattato;
	private BigDecimal importoQuota;
	private String dataEsecuzionePagamento;
	private String radioIdLiquidazione;
	private BigDecimal sommatoriaQuote;
	private String radioIdAccertamento;
	
	
	//SIOPE PLUS
	
	//motivazione assenza cig
	private String cig;
	
	//motivazione assenza cig
	private String motivazioneAssenzaCig;
	public List<CodificaFin> listaMotivazioniAssenzaCig;
	
	//tipo debito siope
	private List<String> scelteTipoDebitoSiope = new ArrayList<String>();
	private String tipoDebitoSiope;
	//ENDO SIOPE PLUS
	
	
	public String getDescrizioneQuota() {
		return descrizioneQuota;
	}
	public void setDescrizioneQuota(String descrizioneQuota) {
		this.descrizioneQuota = descrizioneQuota;
	}
	
	public String getDataEsecuzionePagamento() {
		return dataEsecuzionePagamento;
	}
	public void setDataEsecuzionePagamento(String dataEsecuzionePagamento) {
		this.dataEsecuzionePagamento = dataEsecuzionePagamento;
	}
	
	public List<Liquidazione> getListaLiquidazioni() {
		return listaLiquidazioni;
	}
	public void setListaLiquidazioni(List<Liquidazione> listaLiquidazioni) {
		this.listaLiquidazioni = listaLiquidazioni;
	}
	public List<SubOrdinativoPagamento> getListaSubOrdinativiPagamenti() {
		return listaSubOrdinativiPagamenti;
	}
	public void setListaSubOrdinativiPagamenti(List<SubOrdinativoPagamento> listaSubOrdinativiPagamenti) {
		this.listaSubOrdinativiPagamenti = listaSubOrdinativiPagamenti;
	}
	public String getRadioIdLiquidazione() {
		return radioIdLiquidazione;
	}
	public void setRadioIdLiquidazione(String radioIdLiquidazione) {
		this.radioIdLiquidazione = radioIdLiquidazione;
	}
	
	public String getImportoQuotaFormattato() {
		return importoQuotaFormattato;
	}
	public void setImportoQuotaFormattato(String importoQuotaFormattato) {
		this.importoQuotaFormattato = importoQuotaFormattato;
	}
	public void setImportoQuota(BigDecimal importoQuota) {
		this.importoQuota = importoQuota;
	}
	public BigDecimal getSommatoriaQuote() {
		return sommatoriaQuote;
	}
	public void setSommatoriaQuote(BigDecimal sommatoriaQuote) {
		this.sommatoriaQuote = sommatoriaQuote;
	}
	public BigDecimal getImportoQuota() {
		return importoQuota;
	}
	public DettaglioQuotaOrdinativoModel getDettaglioQuotaOrdinativoModel() {
		return dettaglioQuotaOrdinativoModel;
	}
	public void setDettaglioQuotaOrdinativoModel(DettaglioQuotaOrdinativoModel dettaglioQuotaOrdinativoModel) {
		this.dettaglioQuotaOrdinativoModel = dettaglioQuotaOrdinativoModel;
	}
	public BigDecimal getImportoQuotaPrimaUpdate() {
		return importoQuotaPrimaUpdate;
	}
	public void setImportoQuotaPrimaUpdate(BigDecimal importoQuotaPrimaUpdate) {
		this.importoQuotaPrimaUpdate = importoQuotaPrimaUpdate;
	}
	public String getRadioIdAccertamento() {
		return radioIdAccertamento;
	}
	public void setRadioIdAccertamento(String radioIdAccertamento) {
		this.radioIdAccertamento = radioIdAccertamento;
	}
	public List<Accertamento> getListaAccertamento() {
		return listaAccertamento;
	}
	public void setListaAccertamento(List<Accertamento> listaAccertamento) {
		this.listaAccertamento = listaAccertamento;
	}
	public List<SubOrdinativoIncasso> getListaSubOrdinativiIncasso() {
		return listaSubOrdinativiIncasso;
	}
	public void setListaSubOrdinativiIncasso(List<SubOrdinativoIncasso> listaSubOrdinativiIncasso) {
		this.listaSubOrdinativiIncasso = listaSubOrdinativiIncasso;
	}
	public boolean isCheckProseguiQuoteIncasso() {
		return checkProseguiQuoteIncasso;
	}
	public void setCheckProseguiQuoteIncasso(boolean checkProseguiQuoteIncasso) {
		this.checkProseguiQuoteIncasso = checkProseguiQuoteIncasso;
	}
	public DettaglioQuotaOrdinativoModel getCopiaDettaglioQuotaOrdinativoModel() {
		return copiaDettaglioQuotaOrdinativoModel;
	}
	public void setCopiaDettaglioQuotaOrdinativoModel(DettaglioQuotaOrdinativoModel copiaDettaglioQuotaOrdinativoModel) {
		this.copiaDettaglioQuotaOrdinativoModel = copiaDettaglioQuotaOrdinativoModel;
	}
	public boolean isCheckAggiornaQuoteIncasso() {
		return checkAggiornaQuoteIncasso;
	}
	public void setCheckAggiornaQuoteIncasso(boolean checkAggiornaQuoteIncasso) {
		this.checkAggiornaQuoteIncasso = checkAggiornaQuoteIncasso;
	}
	/**
	 * @return the resultSize
	 */
	public int getResultSize() {
		return resultSize;
	}
	/**
	 * @param resultSize the resultSize to set
	 */
	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}
	public List<Accertamento> getListaAccertamentoOriginale() {
		return listaAccertamentoOriginale;
	}
	public void setListaAccertamentoOriginale(List<Accertamento> listaAccertamentoOriginale) {
		this.listaAccertamentoOriginale = listaAccertamentoOriginale;
	}
	public String getMotivazioneAssenzaCig() {
		return motivazioneAssenzaCig;
	}
	public void setMotivazioneAssenzaCig(String motivazioneAssenzaCig) {
		this.motivazioneAssenzaCig = motivazioneAssenzaCig;
	}
	public String getTipoDebitoSiope() {
		return tipoDebitoSiope;
	}
	public void setTipoDebitoSiope(String tipoDebitoSiope) {
		this.tipoDebitoSiope = tipoDebitoSiope;
	}
	public String getCig() {
		return cig;
	}
	public void setCig(String cig) {
		this.cig = cig;
	}
	public List<CodificaFin> getListaMotivazioniAssenzaCig() {
		return listaMotivazioniAssenzaCig;
	}
	public void setListaMotivazioniAssenzaCig(List<CodificaFin> listaMotivazioniAssenzaCig) {
		this.listaMotivazioniAssenzaCig = listaMotivazioniAssenzaCig;
	}
	public List<String> getScelteTipoDebitoSiope() {
		return scelteTipoDebitoSiope;
	}
	public void setScelteTipoDebitoSiope(List<String> scelteTipoDebitoSiope) {
		this.scelteTipoDebitoSiope = scelteTipoDebitoSiope;
	}
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;


public class ConsultaProvvisorioModel extends GestioneProvvisorioModel {
	
	private static final long serialVersionUID = 1L;
	
	private ProvvisorioDiCassa provvisorioCassa;
	
	//active tab:
	private String activeTab = "";
	
	//consultazione documenti:
	private List<SubdocumentoSpesa> listaSubDocSpesa;
	private List<SubdocumentoEntrata> listaSubDocEntrata;
	
	private int resultSizeSubDocSpesa;
	private int resultSizeSubDocEntrata;
	
	//consultazione ordinativi:
	private List<OrdinativoPagamento> listaOrdPag;
	private List<OrdinativoIncasso> listaOrdInc;
	
	private BigDecimal totImportiOrdinativi;
	private BigDecimal totImportiSubDoc;
	
	private int resultSizeSubOrdPag;
	private int resultSizeSubOrdInc;
	
	public ConsultaProvvisorioModel(){
		this.activeTab = "provvisorioCassa";
	}

	public String getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(String activeTab) {
		this.activeTab = activeTab;
	}

	public ProvvisorioDiCassa getProvvisorioCassa() {
		return provvisorioCassa;
	}

	public void setProvvisorioCassa(ProvvisorioDiCassa provvisorioCassa) {
		this.provvisorioCassa = provvisorioCassa;
	}

	public List<SubdocumentoSpesa> getListaSubDocSpesa() {
		return listaSubDocSpesa;
	}

	public void setListaSubDocSpesa(List<SubdocumentoSpesa> listaSubDocSpesa) {
		this.listaSubDocSpesa = listaSubDocSpesa;
	}

	public List<SubdocumentoEntrata> getListaSubDocEntrata() {
		return listaSubDocEntrata;
	}

	public void setListaSubDocEntrata(List<SubdocumentoEntrata> listaSubDocEntrata) {
		this.listaSubDocEntrata = listaSubDocEntrata;
	}

	public int getResultSizeSubDocSpesa() {
		return resultSizeSubDocSpesa;
	}

	public void setResultSizeSubDocSpesa(int resultSizeSubDocSpesa) {
		this.resultSizeSubDocSpesa = resultSizeSubDocSpesa;
	}

	public int getResultSizeSubDocEntrata() {
		return resultSizeSubDocEntrata;
	}

	public void setResultSizeSubDocEntrata(int resultSizeSubDocEntrata) {
		this.resultSizeSubDocEntrata = resultSizeSubDocEntrata;
	}

	public int getResultSizeSubOrdPag() {
		return resultSizeSubOrdPag;
	}

	public void setResultSizeSubOrdPag(int resultSizeSubOrdPag) {
		this.resultSizeSubOrdPag = resultSizeSubOrdPag;
	}

	public int getResultSizeSubOrdInc() {
		return resultSizeSubOrdInc;
	}

	public void setResultSizeSubOrdInc(int resultSizeSubOrdInc) {
		this.resultSizeSubOrdInc = resultSizeSubOrdInc;
	}


	public BigDecimal getTotImportiOrdinativi() {
		return totImportiOrdinativi;
	}

	public void setTotImportiOrdinativi(BigDecimal totImportiOrdinativi) {
		this.totImportiOrdinativi = totImportiOrdinativi;
	}

	public BigDecimal getTotImportiSubDoc() {
		return totImportiSubDoc;
	}

	public void setTotImportiSubDoc(BigDecimal totImportiSubDoc) {
		this.totImportiSubDoc = totImportiSubDoc;
	}

	public List<OrdinativoPagamento> getListaOrdPag() {
		return listaOrdPag;
	}

	public void setListaOrdPag(List<OrdinativoPagamento> listaOrdPag) {
		this.listaOrdPag = listaOrdPag;
	}

	public List<OrdinativoIncasso> getListaOrdInc() {
		return listaOrdInc;
	}

	public void setListaOrdInc(List<OrdinativoIncasso> listaOrdInc) {
		this.listaOrdInc = listaOrdInc;
	}
	
}

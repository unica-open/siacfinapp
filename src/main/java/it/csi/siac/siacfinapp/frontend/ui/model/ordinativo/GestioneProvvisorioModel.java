/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

public class GestioneProvvisorioModel extends GenericPopupModel {

	private static final long serialVersionUID = 1L;
	
	//provvisorio di cassa
	private ProvvisorioDiCassa provvisorioDiCassaInAggiornamento;
	private BigInteger idProvvisorioDiCassa;
	
	//importo:
	private BigDecimal importo;
	private String importoFormattato;
	
	//numero e anno provvisiorio:
	private BigInteger numeroProvvisorio;
	private Integer annoProvvisorio;
	
	private String codiceContoEvidenza;
	private String descrizioneContoEvidenza;

	
	//descCausale, denominazioneSoggetto, subCausale, dataEmissione:
	private String descCausale, denominazioneSoggetto, subCausale, dataEmissione;
	
	//data trasmissione
	private String dataTrasmissione;

	private String dataInvioServizio;
	private String dataPresaInCaricoServizio;
	private String dataRifiutoErrataAttribuzione;

	
	//accettato
	private String accettatoStr;
	
	//note
	private String note;
	
	//tipo documento prov
	private String tipoDocumentoProv;
	
	//lista tipo documento pro
	private List<String> tipoDocumentoProvList = new ArrayList<String>();
	
	//struttura selezionata su pagina
	private String strutturaSelezionataSuPagina;
	
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public BigInteger getNumeroProvvisorio() {
		return numeroProvvisorio;
	}
	public void setNumeroProvvisorio(BigInteger numeroProvvisorio) {
		this.numeroProvvisorio = numeroProvvisorio;
	}
	public Integer getAnnoProvvisorio() {
		return annoProvvisorio;
	}
	public void setAnnoProvvisorio(Integer annoProvvisorio) {
		this.annoProvvisorio = annoProvvisorio;
	}
	public String getDescCausale() {
		return descCausale;
	}
	public void setDescCausale(String descCausale) {
		this.descCausale = descCausale;
	}
	public String getDenominazioneSoggetto() {
		return denominazioneSoggetto;
	}
	public void setDenominazioneSoggetto(String denominazioneSoggetto) {
		this.denominazioneSoggetto = denominazioneSoggetto;
	}
	public String getSubCausale() {
		return subCausale;
	}
	public void setSubCausale(String subCausale) {
		this.subCausale = subCausale;
	}
	public String getDataEmissione() {
		return dataEmissione;
	}
	public void setDataEmissione(String dataEmissione) {
		this.dataEmissione = dataEmissione;
	}
	public String getTipoDocumentoProv() {
		return tipoDocumentoProv;
	}
	public void setTipoDocumentoProv(String tipoDocumentoProv) {
		this.tipoDocumentoProv = tipoDocumentoProv;
	}
	public List<String> getTipoDocumentoProvList() {
		return tipoDocumentoProvList;
	}
	public void setTipoDocumentoProvList(List<String> tipoDocumentoProvList) {
		this.tipoDocumentoProvList = tipoDocumentoProvList;
	}
	public BigInteger getIdProvvisorioDiCassa() {
		return idProvvisorioDiCassa;
	}
	public void setIdProvvisorioDiCassa(BigInteger idProvvisorioDiCassa) {
		this.idProvvisorioDiCassa = idProvvisorioDiCassa;
	}
	public String getImportoFormattato() {
		return importoFormattato;
	}
	public void setImportoFormattato(String importoFormattato) {
		this.importoFormattato = importoFormattato;
	}
	public ProvvisorioDiCassa getProvvisorioDiCassaInAggiornamento() {
		return provvisorioDiCassaInAggiornamento;
	}
	public void setProvvisorioDiCassaInAggiornamento(ProvvisorioDiCassa provvisorioDiCassaInAggiornamento) {
		this.provvisorioDiCassaInAggiornamento = provvisorioDiCassaInAggiornamento;
	}
	public String getStrutturaSelezionataSuPagina() {
		return strutturaSelezionataSuPagina;
	}
	public void setStrutturaSelezionataSuPagina(String strutturaSelezionataSuPagina) {
		this.strutturaSelezionataSuPagina = strutturaSelezionataSuPagina;
	}

	public String getDataTrasmissione() {
		return dataTrasmissione;
	}

	public void setDataTrasmissione(String dataTrasmissione) {
		this.dataTrasmissione = dataTrasmissione;
	}

	public String getAccettatoStr() {
		return accettatoStr;
	}

	public void setAccettatoStr(String accettatoStr) {
		this.accettatoStr = accettatoStr;
	}

	public Boolean getAccettato() {
		return "null".equals(accettatoStr) ? null : Boolean.valueOf(accettatoStr);
	}

	public void setAccettato(Boolean accettato) {
		this.accettatoStr = accettato == null ? "null" : accettato.toString();
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	public String getCodiceContoEvidenza() {
		return codiceContoEvidenza;
	}
	public void setCodiceContoEvidenza(String codiceContoEvidenza) {
		this.codiceContoEvidenza = codiceContoEvidenza;
	}
	public String getDescrizioneContoEvidenza() {
		return descrizioneContoEvidenza;
	}
	public void setDescrizioneContoEvidenza(String descrizioneContoEvidenza) {
		this.descrizioneContoEvidenza = descrizioneContoEvidenza;
	}
	public String getDataInvioServizio() {
		return dataInvioServizio;
	}
	public void setDataInvioServizio(String dataInvioServizio) {
		this.dataInvioServizio = dataInvioServizio;
	}
	public String getDataPresaInCaricoServizio() {
		return dataPresaInCaricoServizio;
	}
	public void setDataPresaInCaricoServizio(String dataPresaInCaricoServizio) {
		this.dataPresaInCaricoServizio = dataPresaInCaricoServizio;
	}
	public String getDataRifiutoErrataAttribuzione() {
		return dataRifiutoErrataAttribuzione;
	}
	public void setDataRifiutoErrataAttribuzione(String dataRifiutoErrataAttribuzione) {
		this.dataRifiutoErrataAttribuzione = dataRifiutoErrataAttribuzione;
	}
}

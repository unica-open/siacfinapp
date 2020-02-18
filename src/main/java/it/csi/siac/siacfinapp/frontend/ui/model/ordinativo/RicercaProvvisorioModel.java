/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;
import it.csi.siac.siacfinser.model.StrutturaAmministrativoContabileFlat;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

public class RicercaProvvisorioModel extends GenericPopupModel{

	private static final long serialVersionUID = 1L;

	//importo da a 
	private BigDecimal importoDa;
	private BigDecimal importoA;

	//dati del provvisorio:
	private BigInteger numeroProvvisorio;
	private Integer annoProvvisorio, annoProvvisorioDa, annoProvvisorioA, numeroProvvisorioDa, numeroProvvisorioA;
	private String descCausale, denominazioneSoggetto, subCausale, flagAnnullato, dataInizioEmissione, dataFineEmissione;
	
	//data inizio data fine trasmissione:
	private String dataInizioTrasmissione;
	private String dataFineTrasmissione;

	// SIAC-6879
	private String dataInizioInvioServizio;
	private String dataFineInvioServizio;
	private String dataInizioRifiutoErrataAttribuzione;
	private String dataFineRifiutoErrataAttribuzione;
	
	//per paginazione:
	private int resultSize;
	
	//elenco provvisori di cassa:
	private List<ProvvisorioDiCassa> elencoProvvisoriCassa = new ArrayList<ProvvisorioDiCassa>();

	//tipo documento provvisorio:
	private String tipoDocumentoProv;
	private List<String> tipoDocumentoProvList = new ArrayList<String>();

	//flag annullato prov:
	private String flagAnnullatoProv;
	private List<String> flagAnnullatoProvList = new ArrayList<String>();

	//flag da regolarizzare:
	private String flagDaRegolarizzare;
	private List<String> flagDaRegolarizzareList = new ArrayList<String>();

	//struttura selezionata:
	private String strutturaSelezionataSuPagina;

	//elenco strtture amministrative:
	private Map<Integer, StrutturaAmministrativoContabileFlat> elencoStruttureAmministrativoContabili;

	//provvisori sac:
	private List<ProvvisorioDiCassa> provvisoriSacSel = new ArrayList<ProvvisorioDiCassa>();
	private List<String> descrSacSel = new ArrayList<String>();

	private List<Boolean> provvisoriConSacUtente = new ArrayList<Boolean>();

	// SIAC-6353
	private List<CodificaFin> listaContoTesoriere = new ArrayList<CodificaFin>();
	private String contoTesoriere;
	
	public BigInteger getNumeroProvvisorio() {
		return numeroProvvisorio;
	}

	public Integer getAnnoProvvisorio() {
		return annoProvvisorio;
	}

	public Integer getAnnoProvvisorioDa() {
		return annoProvvisorioDa;
	}

	public Integer getAnnoProvvisorioA() {
		return annoProvvisorioA;
	}

	public String getDescCausale() {
		return descCausale;
	}

	public String getDenominazioneSoggetto() {
		return denominazioneSoggetto;
	}

	public String getSubCausale() {
		return subCausale;
	}

	public String getFlagDaRegolarizzare() {
		return flagDaRegolarizzare;
	}

	public String getFlagAnnullato() {
		return flagAnnullato;
	}

	public String getDataInizioEmissione() {
		return dataInizioEmissione;
	}

	public String getDataFineEmissione() {
		return dataFineEmissione;
	}

	public void setNumeroProvvisorio(BigInteger numeroProvvisorio) {
		this.numeroProvvisorio = numeroProvvisorio;
	}

	public Integer getNumeroProvvisorioDa() {
		return numeroProvvisorioDa;
	}

	public void setNumeroProvvisorioDa(Integer numeroProvvisorioDa) {
		this.numeroProvvisorioDa = numeroProvvisorioDa;
	}

	public Integer getNumeroProvvisorioA() {
		return numeroProvvisorioA;
	}

	public void setNumeroProvvisorioA(Integer numeroProvvisorioA) {
		this.numeroProvvisorioA = numeroProvvisorioA;
	}

	public void setAnnoProvvisorio(Integer annoProvvisorio) {
		this.annoProvvisorio = annoProvvisorio;
	}

	public void setAnnoProvvisorioDa(Integer annoProvvisorioDa) {
		this.annoProvvisorioDa = annoProvvisorioDa;
	}

	public void setAnnoProvvisorioA(Integer annoProvvisorioA) {
		this.annoProvvisorioA = annoProvvisorioA;
	}

	public void setDescCausale(String descCausale) {
		this.descCausale = descCausale;
	}

	public void setDenominazioneSoggetto(String denominazioneSoggetto) {
		this.denominazioneSoggetto = denominazioneSoggetto;
	}

	public void setSubCausale(String subCausale) {
		this.subCausale = subCausale;
	}

	public void setFlagDaRegolarizzare(String flagDaRegolarizzare) {
		this.flagDaRegolarizzare = flagDaRegolarizzare;
	}

	public void setFlagAnnullato(String flagAnnullato) {
		this.flagAnnullato = flagAnnullato;
	}

	public void setDataInizioEmissione(String dataInizio) {
		this.dataInizioEmissione = dataInizio;
	}

	public void setDataFineEmissione(String dataFine) {
		this.dataFineEmissione = dataFine;
	}

	public List<ProvvisorioDiCassa> getElencoProvvisoriCassa() {
		return elencoProvvisoriCassa;
	}

	public void setElencoProvvisoriCassa(List<ProvvisorioDiCassa> elencoProvvisoriCassa) {
		this.elencoProvvisoriCassa = elencoProvvisoriCassa;
	}

	public int getResultSize() {
		return resultSize;
	}

	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}

	public String getTipoDocumentoProv() {
		return tipoDocumentoProv;
	}

	public List<String> getTipoDocumentoProvList() {
		return tipoDocumentoProvList;
	}

	public void setTipoDocumentoProv(String tipoDocumentoProv) {
		this.tipoDocumentoProv = tipoDocumentoProv;
	}

	public void setTipoDocumentoProvList(List<String> tipoDocumentoProvList) {
		this.tipoDocumentoProvList = tipoDocumentoProvList;
	}

	public String getFlagAnnullatoProv() {
		return flagAnnullatoProv;
	}

	public void setFlagAnnullatoProv(String flagAnnullatoProv) {
		this.flagAnnullatoProv = flagAnnullatoProv;
	}

	public List<String> getFlagAnnullatoProvList() {
		return flagAnnullatoProvList;
	}

	public void setFlagAnnullatoProvList(List<String> flagAnnullatoProvList){
		this.flagAnnullatoProvList = flagAnnullatoProvList;
	}

	public List<String> getFlagDaRegolarizzareList(){
		return flagDaRegolarizzareList;
	}

	public void setFlagDaRegolarizzareList(List<String> flagDaRegolarizzareList){
		this.flagDaRegolarizzareList = flagDaRegolarizzareList;
	}

	public BigDecimal getImportoDa(){
		return importoDa;
	}

	public void setImportoDa(BigDecimal importoDa){
		this.importoDa = importoDa;
	}

	public BigDecimal getImportoA(){
		return importoA;
	}

	public void setImportoA(BigDecimal importoA){
		this.importoA = importoA;
	}

	public String getStrutturaSelezionataSuPagina(){
		return strutturaSelezionataSuPagina;
	}

	public void setStrutturaSelezionataSuPagina(String strutturaSelezionataSuPagina){
		this.strutturaSelezionataSuPagina = strutturaSelezionataSuPagina;
	}

	public List<ProvvisorioDiCassa> getProvvisoriSacSel(){
		return elencoProvvisoriCassa;
	}

	public void setProvvisoriSacSel(List<ProvvisorioDiCassa> provvisoriSacSel){
		this.provvisoriSacSel = provvisoriSacSel;
	}

	public Collection<StrutturaAmministrativoContabileFlat> getElencoStruttureAmministrativoContabili(){
		return elencoStruttureAmministrativoContabili.values();
	}

	public void setElencoStruttureAmministrativoContabili(Map<Integer, StrutturaAmministrativoContabileFlat> elencoStruttureAmministrativoContabili){
		this.elencoStruttureAmministrativoContabili = elencoStruttureAmministrativoContabili;
	}

	public List<String> getDescrSacSel() {
		return descrSacSel;
	}

	public void setDescrSacSel(List<String> descrSacSel) {
		this.descrSacSel = descrSacSel;
	}

	public void addDescrSacSel(Integer sacUid) {
		StrutturaAmministrativoContabileFlat sacFlat = elencoStruttureAmministrativoContabili.get(sacUid);
		descrSacSel.add(String.format("%s %s", sacFlat.getCodiceCompleto(), sacFlat.getDescrizione()));
	}

	public void resetDescrSacSel() {
		setDescrSacSel(new ArrayList<String>());
	}
	
	public List<Boolean> getProvvisoriConSacUtente() {
		return provvisoriConSacUtente;
	}

	public void addProvvisoriConSacUtente(Boolean b) {
		provvisoriConSacUtente.add(b);
	}

	public String getDataInizioTrasmissione() {
		return dataInizioTrasmissione;
	}

	public void setDataInizioTrasmissione(String dataInizioTrasmissione) {
		this.dataInizioTrasmissione = dataInizioTrasmissione;
	}

	public String getDataFineTrasmissione() {
		return dataFineTrasmissione;
	}

	public void setDataFineTrasmissione(String dataFineTrasmissione) {
		this.dataFineTrasmissione = dataFineTrasmissione;
	}

	public List<CodificaFin> getListaContoTesoriere() {
		return listaContoTesoriere;
	}

	public void setListaContoTesoriere(List<CodificaFin> listaContoTesoriere) {
		this.listaContoTesoriere = listaContoTesoriere;
	}

	public String getContoTesoriere() {
		return contoTesoriere;
	}

	public void setContoTesoriere(String contoTesoriere) {
		this.contoTesoriere = contoTesoriere;
	}

	public String getDataInizioInvioServizio() {
		return dataInizioInvioServizio;
	}

	public void setDataInizioInvioServizio(String dataInizioInvioServizio) {
		this.dataInizioInvioServizio = dataInizioInvioServizio;
	}

	public String getDataFineInvioServizio() {
		return dataFineInvioServizio;
	}

	public void setDataFineInvioServizio(String dataFineInvioServizio) {
		this.dataFineInvioServizio = dataFineInvioServizio;
	}

	public String getDataInizioRifiutoErrataAttribuzione() {
		return dataInizioRifiutoErrataAttribuzione;
	}

	public void setDataInizioRifiutoErrataAttribuzione(String dataInizioRifiutoErrataAttribuzione) {
		this.dataInizioRifiutoErrataAttribuzione = dataInizioRifiutoErrataAttribuzione;
	}

	public String getDataFineRifiutoErrataAttribuzione() {
		return dataFineRifiutoErrataAttribuzione;
	}

	public void setDataFineRifiutoErrataAttribuzione(String dataFineRifiutoErrataAttribuzione) {
		this.dataFineRifiutoErrataAttribuzione = dataFineRifiutoErrataAttribuzione;
	}
}

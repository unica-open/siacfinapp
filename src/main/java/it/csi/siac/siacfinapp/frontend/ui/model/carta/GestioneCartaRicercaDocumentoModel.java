/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.carta;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;

public class GestioneCartaRicercaDocumentoModel  extends GenericPopupModel{
	
	private static final long serialVersionUID = 1L;
	
	
	//model del provvedimento
	private ProvvedimentoImpegnoModel provvedimento = new ProvvedimentoImpegnoModel();
	
	//flag provvedimento selezionato
	private boolean provvedimentoSelezionato=false;
	
	//model del soggetto
	private SoggettoImpegnoModel soggetto;
	
	//soggetto selezionato
	private boolean soggettoSelezionato = false;
	
	//DOCUMENTO
	private String datiDocumentoAnno;
	private String datiDocumentoNumero;
	private String datiDocumentoQuota;
	private String dataDocumento;
	///
	
	//ELENCO:
	private String elencoNumero;
	private String elencoAnno;
	//
	
	//Ricerca:
	private List<SubdocumentoSpesa> elencoSubdocumentoSpesa = new ArrayList<SubdocumentoSpesa>();
	
	//paginazione:
	private int resultSize,premutoPaginazione;
	
	//titolo step:
	private String titoloStep;
	
	//uid doc selezionato dai risultati di ricerca:
	private int radioUidDocSelezionato;
	
	//per pilotare il pop up che chiede conferma se si vuole veramnete collegare:
	private boolean checkConfermaCollega;
	
	//per pilotare il pop up che chiede conferma con warning sul piano dei conti:
	private boolean checkWarningSenzaProvvisorioDiCassa;
	
	
	public ProvvedimentoImpegnoModel getProvvedimento() {
		return provvedimento;
	}
	public void setProvvedimento(ProvvedimentoImpegnoModel provvedimento) {
		this.provvedimento = provvedimento;
	}
	public boolean isProvvedimentoSelezionato() {
		return provvedimentoSelezionato;
	}
	public void setProvvedimentoSelezionato(boolean provvedimentoSelezionato) {
		this.provvedimentoSelezionato = provvedimentoSelezionato;
	}
	public String getTitoloStep() {
		return titoloStep;
	}
	public void setTitoloStep(String titoloStep) {
		this.titoloStep = titoloStep;
	}
	public SoggettoImpegnoModel getSoggetto() {
		return soggetto;
	}
	public void setSoggetto(SoggettoImpegnoModel soggetto) {
		this.soggetto = soggetto;
	}
	public boolean isSoggettoSelezionato() {
		return soggettoSelezionato;
	}
	public void setSoggettoSelezionato(boolean soggettoSelezionato) {
		this.soggettoSelezionato = soggettoSelezionato;
	}
	public String getDatiDocumentoAnno() {
		return datiDocumentoAnno;
	}
	public void setDatiDocumentoAnno(String datiDocumentoAnno) {
		this.datiDocumentoAnno = datiDocumentoAnno;
	}
	public String getDatiDocumentoNumero() {
		return datiDocumentoNumero;
	}
	public void setDatiDocumentoNumero(String datiDocumentoNumero) {
		this.datiDocumentoNumero = datiDocumentoNumero;
	}
	public String getDatiDocumentoQuota() {
		return datiDocumentoQuota;
	}
	public void setDatiDocumentoQuota(String datiDocumentoQuota) {
		this.datiDocumentoQuota = datiDocumentoQuota;
	}
	public String getDataDocumento() {
		return dataDocumento;
	}
	public void setDataDocumento(String dataDocumento) {
		this.dataDocumento = dataDocumento;
	}
	public int getResultSize() {
		return resultSize;
	}
	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}
	public int getPremutoPaginazione() {
		return premutoPaginazione;
	}
	public void setPremutoPaginazione(int premutoPaginazione) {
		this.premutoPaginazione = premutoPaginazione;
	}
	public List<SubdocumentoSpesa> getElencoSubdocumentoSpesa() {
		return elencoSubdocumentoSpesa;
	}
	public void setElencoSubdocumentoSpesa(List<SubdocumentoSpesa> elencoSubdocumentoSpesa) {
		this.elencoSubdocumentoSpesa = elencoSubdocumentoSpesa;
	}
	public String getElencoNumero() {
		return elencoNumero;
	}
	public void setElencoNumero(String elencoNumero) {
		this.elencoNumero = elencoNumero;
	}
	public String getElencoAnno() {
		return elencoAnno;
	}
	public void setElencoAnno(String elencoAnno) {
		this.elencoAnno = elencoAnno;
	}
	public int getRadioUidDocSelezionato() {
		return radioUidDocSelezionato;
	}
	public void setRadioUidDocSelezionato(int radioUidDocSelezionato) {
		this.radioUidDocSelezionato = radioUidDocSelezionato;
	}
	public boolean isCheckConfermaCollega() {
		return checkConfermaCollega;
	}
	public void setCheckConfermaCollega(boolean checkConfermaCollega) {
		this.checkConfermaCollega = checkConfermaCollega;
	}
	public boolean isCheckWarningSenzaProvvisorioDiCassa() {
		return checkWarningSenzaProvvisorioDiCassa;
	}
	public void setCheckWarningSenzaProvvisorioDiCassa(boolean checkWarningSenzaProvvisorioDiCassa) {
		this.checkWarningSenzaProvvisorioDiCassa = checkWarningSenzaProvvisorioDiCassa;
	}
	
}

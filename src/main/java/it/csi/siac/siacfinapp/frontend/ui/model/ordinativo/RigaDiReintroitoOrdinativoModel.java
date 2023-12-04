/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import java.math.BigDecimal;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;

public class RigaDiReintroitoOrdinativoModel extends GenericFinModel {

	private static final long serialVersionUID = 1L;
	
	
	private String codiceAssociazioneEmissione;
	
	//Esempio: Ordinativo incasso spl
	private String labelRigaOrdinativo;
	
	//descrizione 
	//(solo per righe che rappresentano ord incassso)
	private String descrizione;
	
	//(solo per righe che rappresentano ord incassso)
	private OrdinativoIncasso ordInc;
	
	//importo
	private BigDecimal importo;
	
	//qui l'impegno caricato con comp guidata:
	private Impegno impegnoDiDestinazione;
	//se selezionato un sub:
	private SubImpegno subImpegnoDiDestinazione;
	
	//dati compilati a mano:
	private Integer annoImpegno;
	private Integer numeroImpegno;
	private Integer numeroSubImpegno;
	
	//qui l'acc caricato con comp guidata:
	private Accertamento accertamentoDiDestinazione;
	//se selezionato un sub:
	private SubAccertamento subAccertamentoDiDestinazione;
	
	//dati compilati a mano:
	private Integer annoAccertamento;
	private Integer numeroAccertamento;
	private Integer numeroSubAccertamento;
	
	
	public OrdinativoIncasso getOrdInc() {
		return ordInc;
	}
	public void setOrdInc(OrdinativoIncasso ordInc) {
		this.ordInc = ordInc;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public Impegno getImpegnoDiDestinazione() {
		return impegnoDiDestinazione;
	}
	public void setImpegnoDiDestinazione(Impegno impegnoDiDestinazione) {
		this.impegnoDiDestinazione = impegnoDiDestinazione;
	}
	public Integer getAnnoImpegno() {
		return annoImpegno;
	}
	public void setAnnoImpegno(Integer annoImpegno) {
		this.annoImpegno = annoImpegno;
	}
	public Integer getNumeroImpegno() {
		return numeroImpegno;
	}
	public void setNumeroImpegno(Integer numeroImpegno) {
		this.numeroImpegno = numeroImpegno;
	}
	public Integer getNumeroSubImpegno() {
		return numeroSubImpegno;
	}
	public void setNumeroSubImpegno(Integer numeroSubImpegno) {
		this.numeroSubImpegno = numeroSubImpegno;
	}
	public Accertamento getAccertamentoDiDestinazione() {
		return accertamentoDiDestinazione;
	}
	public void setAccertamentoDiDestinazione(Accertamento accertamentoDiDestinazione) {
		this.accertamentoDiDestinazione = accertamentoDiDestinazione;
	}
	public Integer getAnnoAccertamento() {
		return annoAccertamento;
	}
	public void setAnnoAccertamento(Integer annoAccertamento) {
		this.annoAccertamento = annoAccertamento;
	}
	public Integer getNumeroAccertamento() {
		return numeroAccertamento;
	}
	public void setNumeroAccertamento(Integer numeroAccertamento) {
		this.numeroAccertamento = numeroAccertamento;
	}
	public Integer getNumeroSubAccertamento() {
		return numeroSubAccertamento;
	}
	public void setNumeroSubAccertamento(Integer numeroSubAccertamento) {
		this.numeroSubAccertamento = numeroSubAccertamento;
	}
	public String getCodiceAssociazioneEmissione() {
		return codiceAssociazioneEmissione;
	}
	public void setCodiceAssociazioneEmissione(String codiceAssociazioneEmissione) {
		this.codiceAssociazioneEmissione = codiceAssociazioneEmissione;
	}
	public String getLabelRigaOrdinativo() {
		return labelRigaOrdinativo;
	}
	public void setLabelRigaOrdinativo(String labelRigaOrdinativo) {
		this.labelRigaOrdinativo = labelRigaOrdinativo;
	}
	public SubImpegno getSubImpegnoDiDestinazione() {
		return subImpegnoDiDestinazione;
	}
	public void setSubImpegnoDiDestinazione(SubImpegno subImpegnoDiDestinazione) {
		this.subImpegnoDiDestinazione = subImpegnoDiDestinazione;
	}
	public SubAccertamento getSubAccertamentoDiDestinazione() {
		return subAccertamentoDiDestinazione;
	}
	public void setSubAccertamentoDiDestinazione(
			SubAccertamento subAccertamentoDiDestinazione) {
		this.subAccertamentoDiDestinazione = subAccertamentoDiDestinazione;
	}
	
}

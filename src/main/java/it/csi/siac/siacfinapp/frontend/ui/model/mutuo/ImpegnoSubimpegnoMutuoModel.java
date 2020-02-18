/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.mutuo;

import java.math.BigDecimal;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;

public class ImpegnoSubimpegnoMutuoModel extends GenericFinModel {

	private static final long serialVersionUID = 1L;
	
	//uid, anno, numero, uidImpegnoPadre
	private Integer uid, anno, numero, uidImpegnoPadre;
	
	//boolean impegno
	private boolean impegno;
	
	//capitolo
	private CapitoloUscitaGestione capitolo = new CapitoloUscitaGestione();
	
	//provvedimento
	private AttoAmministrativo provvedimento = new AttoAmministrativo();
	
	//stato, descrizione, importoFormattato, numeroImpegnoPadre
	private String stato, descrizione, importoFormattato, numeroImpegnoPadre;
	
	//importo, importoDaFinanziare
	private BigDecimal importo, importoDaFinanziare;
	
	//flag giaAssociatoAdUnaVoce
	private boolean giaAssociatoAdUnaVoce;
	
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getAnno() {
		return anno;
	}
	public void setAnno(Integer anno) {
		this.anno = anno;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public boolean isImpegno() {
		return impegno;
	}
	public void setImpegno(boolean impegno) {
		this.impegno = impegno;
	}
	public CapitoloUscitaGestione getCapitolo() {
		return capitolo;
	}
	public void setCapitolo(CapitoloUscitaGestione capitolo) {
		this.capitolo = capitolo;
	}
	public AttoAmministrativo getProvvedimento() {
		return provvedimento;
	}
	public void setProvvedimento(AttoAmministrativo provvedimento) {
		this.provvedimento = provvedimento;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public BigDecimal getImportoDaFinanziare() {
		return importoDaFinanziare;
	}
	public void setImportoDaFinanziare(BigDecimal importoDaFinanziare) {
		this.importoDaFinanziare = importoDaFinanziare;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getImportoFormattato() {
		return importoFormattato;
	}
	public void setImportoFormattato(String importoFormattato) {
		this.importoFormattato = importoFormattato;
	}
	public Integer getUidImpegnoPadre() {
		return uidImpegnoPadre;
	}
	public void setUidImpegnoPadre(Integer uidImpegnoPadre) {
		this.uidImpegnoPadre = uidImpegnoPadre;
	}
	public String getNumeroImpegnoPadre() {
		return numeroImpegnoPadre;
	}
	public void setNumeroImpegnoPadre(String numeroImpegnoPadre) {
		this.numeroImpegnoPadre = numeroImpegnoPadre;
	}
	public boolean isGiaAssociatoAdUnaVoce() {
		return giaAssociatoAdUnaVoce;
	}
	public void setGiaAssociatoAdUnaVoce(boolean giaAssociatoAdUnaVoce) {
		this.giaAssociatoAdUnaVoce = giaAssociatoAdUnaVoce;
	}
	
}

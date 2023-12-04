/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import java.math.BigInteger;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;

public class ProvvedimentoImpegnoModel extends GenericFinModel {

	private static final long serialVersionUID = 1L;

	//annoProvvedimento, uid, idTipoProvvedimento
	private Integer annoProvvedimento, uid, idTipoProvvedimento;
	
	//tipoProvvedimento, oggetto, strutturaAmministrativa, stato, codiceTipoProvvedimento
	private String tipoProvvedimento, oggetto, strutturaAmministrativa, stato, codiceTipoProvvedimento;
	
	//codiceStrutturaAmministrativa,codiceStrutturaAmministrativaPadre
	private String codiceStrutturaAmministrativa,codiceStrutturaAmministrativaPadre; 
	
	//valoreProvvedimento
	private CodificaFin valoreProvvedimento;
	
	//numeroProvvedimento
	private BigInteger numeroProvvedimento;
	
	//livello, uidStrutturaAmm
	private Integer livello, uidStrutturaAmm;
	
	//parereRegolaritaContabile
	private boolean parereRegolaritaContabile;
	
	//SIAC-6929
	private Boolean bloccoRagioneria;
	
	//SIAC-7299
	private String provenienza;
	
	public Integer getAnnoProvvedimento() {
		return annoProvvedimento;
	}
	public void setAnnoProvvedimento(Integer annoProvvedimento) {
		this.annoProvvedimento = annoProvvedimento;
	}
	public BigInteger getNumeroProvvedimento() {
		return numeroProvvedimento;
	}
	public void setNumeroProvvedimento(BigInteger numeroProvvedimento) {
		this.numeroProvvedimento = numeroProvvedimento;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getTipoProvvedimento() {
		return tipoProvvedimento;
	}
	public void setTipoProvvedimento(String tipoProvvedimento) {
		this.tipoProvvedimento = tipoProvvedimento;
	}
	public String getOggetto() {
		return oggetto;
	}
	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public CodificaFin getValoreProvvedimento() {
		return valoreProvvedimento;
	}
	public void setValoreProvvedimento(CodificaFin valoreProvvedimento) {
		this.valoreProvvedimento = valoreProvvedimento;
	}
	public String getStrutturaAmministrativa() {
		return strutturaAmministrativa;
	}
	public void setStrutturaAmministrativa(String strutturaAmministrativa) {
		this.strutturaAmministrativa = strutturaAmministrativa;
	}
	public Integer getIdTipoProvvedimento() {
		return idTipoProvvedimento;
	}
	public void setIdTipoProvvedimento(Integer idTipoProvvedimento) {
		this.idTipoProvvedimento = idTipoProvvedimento;
	}
	public String getCodiceTipoProvvedimento() {
		return codiceTipoProvvedimento;
	}
	public void setCodiceTipoProvvedimento(String codiceTipoProvvedimento) {
		this.codiceTipoProvvedimento = codiceTipoProvvedimento;
	}
	public String getCodiceStrutturaAmministrativa() {
		return codiceStrutturaAmministrativa;
	}
	public void setCodiceStrutturaAmministrativa(String codiceStrutturaAmministrativa) {
		this.codiceStrutturaAmministrativa = codiceStrutturaAmministrativa;
	}
	public Integer getLivello() {
		return livello;
	}
	public void setLivello(Integer livello) {
		this.livello = livello;
	}
	public String getCodiceStrutturaAmministrativaPadre() {
		return codiceStrutturaAmministrativaPadre;
	}
	public void setCodiceStrutturaAmministrativaPadre(
			String codiceStrutturaAmministrativaPadre) {
		this.codiceStrutturaAmministrativaPadre = codiceStrutturaAmministrativaPadre;
	}
	public Integer getUidStrutturaAmm() {
		return uidStrutturaAmm;
	}
	public void setUidStrutturaAmm(Integer uidStrutturaAmm) {
		this.uidStrutturaAmm = uidStrutturaAmm;
	}
	public boolean isParereRegolaritaContabile() {
		return parereRegolaritaContabile;
	}
	/**
	 * @return the bloccoRagioneria
	 */
	public Boolean getBloccoRagioneria() {
		return bloccoRagioneria;
	}
	/**
	 * @param bloccoRagioneria the bloccoRagioneria to set
	 */
	public void setBloccoRagioneria(Boolean bloccoRagioneria) {
		this.bloccoRagioneria = bloccoRagioneria;
	}
	public void setParereRegolaritaContabile(boolean parereRegolaritaContabile) {
		this.parereRegolaritaContabile = parereRegolaritaContabile;
	}
	/**
	 * @return the provenienza
	 */
	public String getProvenienza() {
		return provenienza;
	}
	/**
	 * @param provenienza the provenienza to set
	 */
	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}
	
}

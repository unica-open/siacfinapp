/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import java.util.List;

import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;

public class ModificaConsulta extends CommonConsulta {

	private static final long serialVersionUID = 1L;
	
	//SIAC-8834
	private Impegno impegnoAssociato;

	//numero, tipo, descrizione, motivo
	private String numero, tipo, descrizione, motivo;

	//descMain, descSub, numSub
	private String descMain, descSub, numSub;
	
	//provvedimento
	private final Provvedimento provvedimento = new Provvedimento();
	
	//SIAC-7349 Inizio  SR180 FL 09/04/2020
	private List<ModificaMovimentoGestioneSpesaCollegata> listaModificheMovimentoGestioneSpesaCollegata;
	//SIAC-7349 Fine SR180 FL 09/04/2020

	//soggetto attuale e precendente
	private final Soggetto soggettoAttuale = new Soggetto(), soggettoPrec = new Soggetto();

	private String bloccoRagioneria;
	
	// GETTER E SETTER:
	public String getBloccoRagioneria() {
		return bloccoRagioneria;
	}

	public void setBloccoRagioneria(String bloccoRagioneria) {
		this.bloccoRagioneria = bloccoRagioneria;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getDescMain() {
		return descMain;
	}

	public void setDescMain(String descMain) {
		this.descMain = descMain;
	}

	public String getDescSub() {
		return descSub;
	}

	public void setDescSub(String descSub) {
		this.descSub = descSub;
	}

	public String getNumSub() {
		return numSub;
	}

	public void setNumSub(String numSub) {
		this.numSub = numSub;
	}

	public Soggetto getSoggettoAttuale() {
		return soggettoAttuale;
	}

	public Soggetto getSoggettoPrec() {
		return soggettoPrec;
	}

	public Provvedimento getProvvedimento() {
		return provvedimento;
	}

	/**
	 * @return the listaModificheMovimentoGestioneSpesaCollegata
	 */
	public List<ModificaMovimentoGestioneSpesaCollegata> getListaModificheMovimentoGestioneSpesaCollegata() {
		return listaModificheMovimentoGestioneSpesaCollegata;
	}

	/**
	 * @param listaModificheMovimentoGestioneSpesaCollegata the listaModificheMovimentoGestioneSpesaCollegata to set
	 */
	public void setListaModificheMovimentoGestioneSpesaCollegata(
			List<ModificaMovimentoGestioneSpesaCollegata> listaModificheMovimentoGestioneSpesaCollegata) {
		this.listaModificheMovimentoGestioneSpesaCollegata = listaModificheMovimentoGestioneSpesaCollegata;
	}

	public Impegno getImpegnoAssociato() {
		return impegnoAssociato;
	}

	public void setImpegnoAssociato(Impegno impegnoAssociato) {
		this.impegnoAssociato = impegnoAssociato;
	}
}

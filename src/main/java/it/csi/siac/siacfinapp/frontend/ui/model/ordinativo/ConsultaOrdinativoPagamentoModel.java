/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.ordinativo;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.model.ClassificatoreStipendi;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;


public class ConsultaOrdinativoPagamentoModel extends ConsultaOrdinativoModel {
	
	private static final long serialVersionUID = 1L;
	
	private OrdinativoPagamento ordinativoPagamento;
	private Ordinativo ordinativoCollegatoSelezionato;
	
	private String descMissione = "";
	private String codMissione = "";
	private String codProgramma = "";
	private String descProgramma="";
	
    private List<ClassificatoreGenerico> listaClassificatoriGen21 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen22 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen23 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen24 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen25 = new ArrayList<ClassificatoreGenerico>();
    
    private List<ClassificatoreStipendi> listaClassificatoriStipendi = new ArrayList<ClassificatoreStipendi>();
    
    //SIOPE PLUS:
	//cig
  	private String cig;
  	
  	//motivazione assenza cig
  	private String motivazioneAssenzaCig;
  	
  	//tipo debito siope
  	private String tipoDebitoSiope;
  	//END SIOPE PLUS
	
    public ConsultaOrdinativoPagamentoModel(){
		super.activeTab = "ordinativoPagamento";
	}

	/* **************************************************************************** */
	/*   Getter / Setter															*/
	/* **************************************************************************** */
	
	public OrdinativoPagamento getOrdinativoPagamento() {
	    return ordinativoPagamento;
	}

	public void setOrdinativoPagamento(OrdinativoPagamento ordinativoPagamento) {
	    this.ordinativoPagamento = ordinativoPagamento;
	}

	public Ordinativo getOrdinativoCollegatoSelezionato() {
		return ordinativoCollegatoSelezionato;
	}

	public void setOrdinativoCollegatoSelezionato(
			Ordinativo ordinativoCollegatoSelezionato) {
		this.ordinativoCollegatoSelezionato = ordinativoCollegatoSelezionato;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen21() {
		return listaClassificatoriGen21;
	}

	public void setListaClassificatoriGen21(
			List<ClassificatoreGenerico> listaClassificatoriGen21) {
		this.listaClassificatoriGen21 = listaClassificatoriGen21;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen22() {
		return listaClassificatoriGen22;
	}

	public void setListaClassificatoriGen22(
			List<ClassificatoreGenerico> listaClassificatoriGen22) {
		this.listaClassificatoriGen22 = listaClassificatoriGen22;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen23() {
		return listaClassificatoriGen23;
	}

	public void setListaClassificatoriGen23(
			List<ClassificatoreGenerico> listaClassificatoriGen23) {
		this.listaClassificatoriGen23 = listaClassificatoriGen23;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen24() {
		return listaClassificatoriGen24;
	}

	public void setListaClassificatoriGen24(
			List<ClassificatoreGenerico> listaClassificatoriGen24) {
		this.listaClassificatoriGen24 = listaClassificatoriGen24;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen25() {
		return listaClassificatoriGen25;
	}

	public void setListaClassificatoriGen25(
			List<ClassificatoreGenerico> listaClassificatoriGen25) {
		this.listaClassificatoriGen25 = listaClassificatoriGen25;
	}

	/**
	 * @return the codMissione
	 */
	public String getCodMissione() {
		return codMissione;
	}

	/**
	 * @param codMissione the codMissione to set
	 */
	public void setCodMissione(String codMissione) {
		this.codMissione = codMissione;
	}

	/**
	 * @return the codProgramma
	 */
	public String getCodProgramma() {
		return codProgramma;
	}

	/**
	 * @param codProgramma the codProgramma to set
	 */
	public void setCodProgramma(String codProgramma) {
		this.codProgramma = codProgramma;
	}

	/**
	 * @return the descProgramma
	 */
	public String getDescProgramma() {
		return descProgramma;
	}

	/**
	 * @param descProgramma the descProgramma to set
	 */
	public void setDescProgramma(String descProgramma) {
		this.descProgramma = descProgramma;
	}

	public String getDescMissione() {
		return descMissione;
	}

	public void setDescMissione(String descMissione) {
		this.descMissione = descMissione;
	}

	public String getCig() {
		return cig;
	}

	public void setCig(String cig) {
		this.cig = cig;
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

	/**
	 * @return the listaClassificatoriStipendi
	 */
	public List<ClassificatoreStipendi> getListaClassificatoriStipendi() {
		return listaClassificatoriStipendi;
	}

	/**
	 * @param listaClassificatoriStipendi the listaClassificatoriStipendi to set
	 */
	public void setListaClassificatoriStipendi(List<ClassificatoreStipendi> listaClassificatoriStipendi) {
		this.listaClassificatoriStipendi = listaClassificatoriStipendi;
	}
	
	
}

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
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;


public class ConsultaOrdinativoIncassoModel extends ConsultaOrdinativoModel {
	
	private static final long serialVersionUID = 1L;
	
	//ORDINATIVO INCASSO:
	private OrdinativoIncasso ordinativoIncasso;
	
	//COLLEGATO SELEZIONATO:
	private Ordinativo ordinativoCollegatoSelezionato;
	
	//LISTE CLASSIFICATORI GENERICI:
    private List<ClassificatoreGenerico> listaClassificatoriGen26 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen27 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen28 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen29 = new ArrayList<ClassificatoreGenerico>();
    private List<ClassificatoreGenerico> listaClassificatoriGen30 = new ArrayList<ClassificatoreGenerico>();
    
    private List<ClassificatoreStipendi> listaClassificatoriStipendi = new ArrayList<ClassificatoreStipendi>();

    
    public ConsultaOrdinativoIncassoModel(){
    	//setto l'active tab
		super.activeTab = "ordinativoIncasso";
	}
    
    //GETTER E SETTER:
	
	public OrdinativoIncasso getOrdinativoIncasso() {
	    return ordinativoIncasso;
	}

	public void setOrdinativoIncasso(OrdinativoIncasso ordinativoIncasso) {
	    this.ordinativoIncasso = ordinativoIncasso;
	}

	public Ordinativo getOrdinativoCollegatoSelezionato() {
		return ordinativoCollegatoSelezionato;
	}

	public void setOrdinativoCollegatoSelezionato(
			Ordinativo ordinativoCollegatoSelezionato) {
		this.ordinativoCollegatoSelezionato = ordinativoCollegatoSelezionato;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen26() {
		return listaClassificatoriGen26;
	}

	public void setListaClassificatoriGen26(
			List<ClassificatoreGenerico> listaClassificatoriGen26) {
		this.listaClassificatoriGen26 = listaClassificatoriGen26;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen27() {
		return listaClassificatoriGen27;
	}

	public void setListaClassificatoriGen27(
			List<ClassificatoreGenerico> listaClassificatoriGen27) {
		this.listaClassificatoriGen27 = listaClassificatoriGen27;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen28() {
		return listaClassificatoriGen28;
	}

	public void setListaClassificatoriGen28(
			List<ClassificatoreGenerico> listaClassificatoriGen28) {
		this.listaClassificatoriGen28 = listaClassificatoriGen28;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen29() {
		return listaClassificatoriGen29;
	}

	public void setListaClassificatoriGen29(
			List<ClassificatoreGenerico> listaClassificatoriGen29) {
		this.listaClassificatoriGen29 = listaClassificatoriGen29;
	}

	public List<ClassificatoreGenerico> getListaClassificatoriGen30() {
		return listaClassificatoriGen30;
	}

	public void setListaClassificatoriGen30(
			List<ClassificatoreGenerico> listaClassificatoriGen30) {
		this.listaClassificatoriGen30 = listaClassificatoriGen30;
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

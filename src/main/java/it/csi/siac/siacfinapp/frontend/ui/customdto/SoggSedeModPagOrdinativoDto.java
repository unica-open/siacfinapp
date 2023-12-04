/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.customdto;

import java.io.Serializable;

import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * contenitore dati a uso e consumo interno tra i metodi delle Action relative all'ordinativo
 * @author claudio.picco
 *
 */
public class SoggSedeModPagOrdinativoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1285961305613960322L;
	
	private SedeSecondariaSoggetto sedeSecondariaSelezionata;
	private Soggetto soggettoCreditore;
	private ModalitaPagamentoSoggetto modPagSelezionata;
	
	public SedeSecondariaSoggetto getSedeSecondariaSelezionata() {
		return sedeSecondariaSelezionata;
	}
	public void setSedeSecondariaSelezionata(
			SedeSecondariaSoggetto sedeSecondariaSelezionata) {
		this.sedeSecondariaSelezionata = sedeSecondariaSelezionata;
	}
	public Soggetto getSoggettoCreditore() {
		return soggettoCreditore;
	}
	public void setSoggettoCreditore(Soggetto soggettoCreditore) {
		this.soggettoCreditore = soggettoCreditore;
	}
	public ModalitaPagamentoSoggetto getModPagSelezionata() {
		return modPagSelezionata;
	}
	public void setModPagSelezionata(ModalitaPagamentoSoggetto modPagSelezionata) {
		this.modPagSelezionata = modPagSelezionata;
	}
}

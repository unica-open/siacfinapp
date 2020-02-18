/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import java.math.BigDecimal;

import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoAmbito;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;

public class ProgettoImpegnoModel extends GenericFinModel {

	private static final long serialVersionUID = 1L;

	//codice
	private String codice;
	
	//descrizione
	private String descrizione;
	
	private TipoAmbito tipoAmbito;
	
	private Progetto progetto;
	
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	
	//valore complessivo
	private BigDecimal valoreComplessivo;
	
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public BigDecimal getValoreComplessivo() {
		return valoreComplessivo;
	}
	public void setValoreComplessivo(BigDecimal valoreComplessivo) {
		this.valoreComplessivo = valoreComplessivo;
	}
	public TipoAmbito getTipoAmbito() {
		return tipoAmbito;
	}
	public void setTipoAmbito(TipoAmbito tipoAmbito) {
		this.tipoAmbito = tipoAmbito;
	}
	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}
	public void setStrutturaAmministrativoContabile(StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}
	/**
	 * @return the progetto
	 */
	public Progetto getProgetto() {
		return progetto;
	}
	/**
	 * @param progetto the progetto to set
	 */
	public void setProgetto(Progetto progetto) {
		this.progetto = progetto;
	}


	
}

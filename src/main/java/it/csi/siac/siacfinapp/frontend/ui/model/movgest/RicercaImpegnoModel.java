/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;

public class RicercaImpegnoModel extends GenericFinModel {

	private static final long serialVersionUID = 2718637354856268419L;

	//anno esercizio
	private String annoEsercizio;
	
	//anno movimento
	private String annoMovimento;
	
	//numero impegno
	private String numeroImpegno;
	
	//id stato operativo movgest
	private String idStatoOperativoMovgest;
	
	//competenze
	private String competenze;
	
	//cup
	private String cup;
	
	//cig
	private String cig;
	
	//progetto
	private String progetto;
	
	//anno impegno riaccertamento
	private String annoImpRiacc;
	
	//numero impegno riaccertamento
	private String numeroImpRiacc;
	
	//anno impegno origine
	private String annoImpOrigine;
	
	//numero impegno origine
	private String numeroImpOrigine;

	//per marcare competenza tutti
	private boolean competenzaTutti;
	
	//per marcare competenza 
	private boolean competenzaCompetenza;
	
	//per marcare competenza corrente
	private boolean competenzaCorrente;
	
	//per marcare competenza futuri
	private boolean competenzaFuturi;
	
	//SIAC-6997
	private boolean competenzaResiduiRor;
	
	//struttura selezionata su pagina
	private String strutturaSelezionataSuPagina;
	
	//piano dei conti
	private ElementoPianoDeiConti pianoDeiConti = new ElementoPianoDeiConti();

	//SIAC-5253 nuovo campo per escludere i movimenti annullati:
	private Boolean escludiAnnullati = Boolean.FALSE;
	private Boolean hiddenPerEscludiAnnullati;
	
	//GETTER E SETTER:
	
	/**
	 * @return the strutturaSelezionataSuPagina
	 */
	public String getStrutturaSelezionataSuPagina() {
		return strutturaSelezionataSuPagina;
	}

	/**
	 * @param strutturaSelezionataSuPagina
	 *            the strutturaSelezionataSuPagina to set
	 */
	public void setStrutturaSelezionataSuPagina(String strutturaSelezionataSuPagina) {
		this.strutturaSelezionataSuPagina = strutturaSelezionataSuPagina;
	}

	public String getAnnoEsercizio() {
		return annoEsercizio;
	}

	public void setAnnoEsercizio(String annoEsercizio) {
		this.annoEsercizio = annoEsercizio;
	}

	public String getNumeroImpegno() {
		return numeroImpegno;
	}

	public void setNumeroImpegno(String numeroImpegno) {
		this.numeroImpegno = numeroImpegno;
	}

	public String getIdStatoOperativoMovgest() {
		return idStatoOperativoMovgest;
	}

	public void setIdStatoOperativoMovgest(String idStatoOperativoMovgest) {
		this.idStatoOperativoMovgest = idStatoOperativoMovgest;
	}

	public String getCompetenze() {
		return competenze;
	}

	public void setCompetenze(String competenze) {
		this.competenze = competenze;
	}

	public String getCup() {
		return cup;
	}

	public void setCup(String cup) {
		this.cup = cup;
	}

	public String getCig() {
		return cig;
	}

	public void setCig(String cig) {
		this.cig = cig;
	}

	public String getProgetto() {
		return progetto;
	}

	public void setProgetto(String progetto) {
		this.progetto = progetto;
	}

	public String getAnnoImpRiacc() {
		return annoImpRiacc;
	}

	public void setAnnoImpRiacc(String annoImpRiacc) {
		this.annoImpRiacc = annoImpRiacc;
	}

	public String getNumeroImpRiacc() {
		return numeroImpRiacc;
	}

	public void setNumeroImpRiacc(String numeroImpRiacc) {
		this.numeroImpRiacc = numeroImpRiacc;
	}

	public String getAnnoImpOrigine() {
		return annoImpOrigine;
	}

	public void setAnnoImpOrigine(String annoImpOrigine) {
		this.annoImpOrigine = annoImpOrigine;
	}

	public String getNumeroImpOrigine() {
		return numeroImpOrigine;
	}

	public void setNumeroImpOrigine(String numeroImpOrigine) {
		this.numeroImpOrigine = numeroImpOrigine;
	}

	public boolean isCompetenzaTutti() {
		return competenzaTutti;
	}

	public void setCompetenzaTutti(boolean competenzaTutti) {
		this.competenzaTutti = competenzaTutti;
	}

	public boolean isCompetenzaCompetenza() {
		return competenzaCompetenza;
	}

	public void setCompetenzaCompetenza(boolean competenzaCompetenza) {
		this.competenzaCompetenza = competenzaCompetenza;
	}

	public boolean isCompetenzaCorrente() {
		return competenzaCorrente;
	}

	public void setCompetenzaCorrente(boolean competenzaCorrente) {
		this.competenzaCorrente = competenzaCorrente;
	}

	public boolean isCompetenzaFuturi() {
		return competenzaFuturi;
	}

	public void setCompetenzaFuturi(boolean competenzaFuturi) {
		this.competenzaFuturi = competenzaFuturi;
	}

	public ElementoPianoDeiConti getPianoDeiConti() {
		return pianoDeiConti;
	}

	public void setPianoDeiConti(ElementoPianoDeiConti pianoDeiConti) {
		this.pianoDeiConti = pianoDeiConti;
	}

	/**
	 * @return the annoMovimento
	 */
	public String getAnnoMovimento() {
		return annoMovimento;
	}

	/**
	 * @param annoMovimento
	 *            the annoMovimento to set
	 */
	public void setAnnoMovimento(String annoMovimento) {
		this.annoMovimento = annoMovimento;
	}

	public Boolean getEscludiAnnullati() {
		return escludiAnnullati;
	}

	public void setEscludiAnnullati(Boolean escludiAnnullati) {
		this.escludiAnnullati = escludiAnnullati;
	}

	public Boolean getHiddenPerEscludiAnnullati() {
		return hiddenPerEscludiAnnullati;
	}

	public void setHiddenPerEscludiAnnullati(Boolean hiddenPerEscludiAnnullati) {
		this.hiddenPerEscludiAnnullati = hiddenPerEscludiAnnullati;
	}

	/**
	 * @return the competenzaResiduiRor
	 */
	public boolean isCompetenzaResiduiRor() {
		return competenzaResiduiRor;
	}

	/**
	 * @param competenzaResiduiRor the competenzaResiduiRor to set
	 */
	public void setCompetenzaResiduiRor(boolean competenzaResiduiRor) {
		this.competenzaResiduiRor = competenzaResiduiRor;
	}

	
}

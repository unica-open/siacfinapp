/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.util.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.OperazioneTipoImporto;
import it.csi.siac.siacgenser.model.OperazioneUtilizzoConto;
import it.csi.siac.siacgenser.model.OperazioneUtilizzoImporto;

/**
 * Elemento delle scritture corrispondenti alla lista movdettaglio del singolo movimentoEP per lanota integrata
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 28/04/2015
 * @author Elisa Chiari
 * @version 1.1.0 - 20/04/2017
 *
 */
public class ElementoScritturaPrimaNotaIntegrata implements Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1232650609511718524L;
	private MovimentoDettaglio movimentoDettaglio;
	private ContoTipoOperazione contoTipoOperazione;
	private ClasseDiConciliazione classeDiConcilazione;
	private boolean aggiornamentoImportoManuale;
	private Boolean daClasseConciliazione;
	private List<Conto> contiSelezionabiliDaClasseDiConciliazione = new ArrayList<Conto>();

	/**
	 * Costruttore di wrap.
	 * 
	 * @param movimentoDettaglio          il movimento da wrappare
	 * @param contoTipoOperazione         il conto da wrappare
	 * @param aggiornamentoImportoManuale se l'aggiornamento dell'importo sia di tipo manuale
	 * @param classeDiConcilazione        la classe di conciliazione
	 */
	public ElementoScritturaPrimaNotaIntegrata(MovimentoDettaglio movimentoDettaglio, ContoTipoOperazione contoTipoOperazione, boolean aggiornamentoImportoManuale,  ClasseDiConciliazione classeDiConcilazione) {
		super();
		this.movimentoDettaglio = movimentoDettaglio;
		this.contoTipoOperazione = contoTipoOperazione;
		this.aggiornamentoImportoManuale = aggiornamentoImportoManuale;
		this.classeDiConcilazione = classeDiConcilazione;
	}
	
	/**
	 * Costruttore di wrap.
	 * 
	 * @param movimentoDettaglio          il movimento da wrappare
	 * @param contoTipoOperazione         il conto da wrappare
	 * @param aggiornamentoImportoManuale se l'aggiornamento dell'importo sia di tipo manuale
	 */
	public ElementoScritturaPrimaNotaIntegrata(MovimentoDettaglio movimentoDettaglio, ContoTipoOperazione contoTipoOperazione, boolean aggiornamentoImportoManuale) {
		this(movimentoDettaglio, contoTipoOperazione, aggiornamentoImportoManuale, contoTipoOperazione.getClasseDiConciliazione());
	}
	
	/**
	 * Costruttore di wrap.
	 *
	 * @param movimentoDettaglio 							il movimento da wrappare
	 * @param contoTipoOperazione							il conto da wrappare
	 * @param aggiornamentoImportoManuale 					se l'aggiornamento dell'importo sia di tipo manuale
	 * @param listaContiSelezionabiliDaClasseConciliazione 	la lista dei conti selezionabili da classe conciliazione
	 */
	public ElementoScritturaPrimaNotaIntegrata(MovimentoDettaglio movimentoDettaglio, ContoTipoOperazione contoTipoOperazione, boolean aggiornamentoImportoManuale, List<Conto> listaContiSelezionabiliDaClasseConciliazione) {
		super();
		this.movimentoDettaglio = movimentoDettaglio;
		this.contoTipoOperazione = contoTipoOperazione;
		this.aggiornamentoImportoManuale = aggiornamentoImportoManuale;
		this.classeDiConcilazione = contoTipoOperazione.getClasseDiConciliazione();
		this.contiSelezionabiliDaClasseDiConciliazione = listaContiSelezionabiliDaClasseConciliazione ;
	}
	
	/**
	 * @return the movimentoDettaglio
	 */
	public MovimentoDettaglio getMovimentoDettaglio() {
		return movimentoDettaglio;
	}
	/**
	 * @param movimentoDettaglio the movimentoDettaglio to set
	 */
	public void setMovimentoDettaglio(MovimentoDettaglio movimentoDettaglio) {
		this.movimentoDettaglio = movimentoDettaglio;
	}
	/**
	 * @return the contoTipoOperazione
	 */
	public ContoTipoOperazione getContoTipoOperazione() {
		return contoTipoOperazione;
	}
	/**
	 * @param contoTipoOperazione the contoTipoOperazione to set
	 */
	public void setContoTipoOperazione(ContoTipoOperazione contoTipoOperazione) {
		this.contoTipoOperazione = contoTipoOperazione;
	}
	
	/**
	 * @return the classeDiConcilazione
	 */
	public ClasseDiConciliazione getClasseDiConcilazione() {
		return classeDiConcilazione;
	}

	/**
	 * @param classeDiConcilazione the classeDiConcilazione to set
	 */
	public void setClasseDiConcilazione(ClasseDiConciliazione classeDiConcilazione) {
		this.classeDiConcilazione = classeDiConcilazione;
	}

	/**
	 * @return the aggiornamentoImportoManuale
	 */
	public boolean isAggiornamentoImportoManuale() {
		return aggiornamentoImportoManuale;
	}
	/**
	 * @param aggiornamentoImportoManuale the aggiornamentoImportoManuale to set
	 */
	public void setAggiornamentoImportoManuale(boolean aggiornamentoImportoManuale) {
		this.aggiornamentoImportoManuale = aggiornamentoImportoManuale;
	}
	/**
	 * @return the codiceConto
	 */
	public String getCodiceConto() {
		return (getMovimentoDettaglio() != null && getMovimentoDettaglio().getConto() != null) ? getMovimentoDettaglio().getConto().getCodice() : (getClasseDiConcilazione() != null ? getClasseDiConcilazione().getDescrizione() : "");
	}
	
	/**
	 * @return the descrizioneConto
	 */
	public String getDescrizioneConto() {
		return getMovimentoDettaglio() != null && getMovimentoDettaglio().getConto() != null ? getMovimentoDettaglio().getConto().getDescrizione() : "";
	}
	/**
	 * @return the daClasseConcilazione
	 */
	public Boolean getDaClasseConciliazione() {
		return daClasseConciliazione;
	}

	/**
	 * @param daClasseConciliazione the daClasseConcilazione to set
	 */
	public void setDaClasseConciliazione(Boolean daClasseConciliazione) {
		this.daClasseConciliazione = daClasseConciliazione;
	}
	
	/**
	 * Gets the conti selezionabili da classe di conciliazione.
	 *
	 * @return the conti selezionabili da classe di conciliazione
	 */
	public List<Conto> getContiSelezionabiliDaClasseDiConciliazione() {
		return contiSelezionabiliDaClasseDiConciliazione != null? contiSelezionabiliDaClasseDiConciliazione : new ArrayList<Conto>();
	}

	/**
	 * Sets the conti selezionabili da classe di conciliazione.
	 *
	 * @param contiSelezionabiliDaClasseDiConciliazione the new conti selezionabili da classe di conciliazione
	 */
	public void setContiSelezionabiliDaClasseDiConciliazione(List<Conto> contiSelezionabiliDaClasseDiConciliazione) {
		this.contiSelezionabiliDaClasseDiConciliazione = contiSelezionabiliDaClasseDiConciliazione;
	}

	/**
	 * @return the dare
	 */
	public String getDare() {
		return getMovimentoDettaglio() != null
			&& getMovimentoDettaglio().getImporto() != null
			&& OperazioneSegnoConto.DARE.equals(getMovimentoDettaglio().getSegno())
				? formattaData(getMovimentoDettaglio().getImporto())
				: "";
	}
	
	/**
	 * @return the avere
	 */
	public String getAvere() {
		return getMovimentoDettaglio() != null
			&& getMovimentoDettaglio().getImporto() != null
			&& OperazioneSegnoConto.AVERE.equals(getMovimentoDettaglio().getSegno())
				? formattaData(getMovimentoDettaglio().getImporto())
				: "";
	}
	
	/**
	 * @return the segnoDare
	 */
	public boolean isSegnoDare() {
		return getMovimentoDettaglio() != null && OperazioneSegnoConto.DARE.equals(getMovimentoDettaglio().getSegno());
	}
	
	/**
	 * @return the segnoAvere
	 */
	public boolean isSegnoAvere() {
		return getMovimentoDettaglio() != null && OperazioneSegnoConto.AVERE.equals(getMovimentoDettaglio().getSegno());
	}
	
	/**
	 * @return the utilizzoContoProposto
	 */
	public boolean isUtilizzoContoProposto() {
		return getContoTipoOperazione() != null && OperazioneUtilizzoConto.PROPOSTO.equals(getContoTipoOperazione().getOperazioneUtilizzoConto());
	}
	
	/**
	 * @return the utilizzoContoObbligatorio
	 */
	public boolean isUtilizzoContoObbligatorio() {
		return getContoTipoOperazione() != null && OperazioneUtilizzoConto.OBBLIGATORIO.equals(getContoTipoOperazione().getOperazioneUtilizzoConto());
	}
	
	/**
	 * @return the utilizzoImportoProposto
	 */
	public boolean isUtilizzoImportoProposto() {
		return getContoTipoOperazione() != null && OperazioneUtilizzoImporto.PROPOSTO.equals(getContoTipoOperazione().getOperazioneUtilizzoImporto());
	}
	
	/**
	 * @return the utilizzoImportoNonModificabile
	 */
	public boolean isUtilizzoImportoNonModificabile() {
		return getContoTipoOperazione() != null && OperazioneUtilizzoImporto.NON_MODIFICABILE.equals(getContoTipoOperazione().getOperazioneUtilizzoImporto());
	}
	
	/**
	 * @return the tipoImportoLordo
	 */
	public boolean isTipoImportoLordo() {
		return getContoTipoOperazione() != null && OperazioneTipoImporto.LORDO.equals(getContoTipoOperazione().getOperazioneTipoImporto());
	}
	
	/**
	 * @return the tipoImportoImposta
	 */
	public boolean isTipoImportoImposta() {
		return getContoTipoOperazione() != null && OperazioneTipoImporto.IMPOSTA.equals(getContoTipoOperazione().getOperazioneTipoImporto());
	}
	
	/**
	 * @return the tipoImportoImponibile
	 */
	public boolean isTipoImportoImponibile() {
		return getContoTipoOperazione() != null && OperazioneTipoImporto.IMPONIBILE.equals(getContoTipoOperazione().getOperazioneTipoImporto());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ElementoScritturaPrimaNotaIntegrata)) {
			return false;
		}
		ElementoScritturaPrimaNotaIntegrata other = (ElementoScritturaPrimaNotaIntegrata) obj;
		// Controllo i conti.
		if(this.contoTipoOperazione == null  && other.contoTipoOperazione == null) {
			return true;
		}
		// Prolisso in quanto Sonar non mi riconosce lo XOR...
		if((this.contoTipoOperazione == null && other.contoTipoOperazione != null) || (this.contoTipoOperazione != null && other.contoTipoOperazione == null)) {
			return false;
		}
		return new EqualsBuilder()
			.append(this.contoTipoOperazione.getOperazioneSegnoConto(), other.contoTipoOperazione.getOperazioneSegnoConto())
			.append(this.contoTipoOperazione.getOperazioneTipoImporto(), other.contoTipoOperazione.getOperazioneTipoImporto())
			.append(this.contoTipoOperazione.getOperazioneUtilizzoConto(), other.contoTipoOperazione.getOperazioneUtilizzoConto())
			.append(this.contoTipoOperazione.getOperazioneUtilizzoImporto(), other.contoTipoOperazione.getOperazioneUtilizzoImporto())
			.isEquals();
	}
	
	@Override
	public int hashCode() {
		if(this.contoTipoOperazione == null) {
			return 0;
		}
		return new HashCodeBuilder()
			.append(this.contoTipoOperazione.getOperazioneSegnoConto())
			.append(this.contoTipoOperazione.getOperazioneTipoImporto())
			.append(this.contoTipoOperazione.getOperazioneUtilizzoConto())
			.append(this.contoTipoOperazione.getOperazioneUtilizzoImporto())
			.toHashCode();
	}
	
	/**
	 * Formatta data.
	 *
	 * @param bd the bd
	 * @return the string
	 */
	private String formattaData(BigDecimal bd) {
		DecimalFormat df = (DecimalFormat)NumberFormat.getInstance(Locale.ITALY);
		df.setParseBigDecimal(true);
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		return df.format(bd);
	}
	
}

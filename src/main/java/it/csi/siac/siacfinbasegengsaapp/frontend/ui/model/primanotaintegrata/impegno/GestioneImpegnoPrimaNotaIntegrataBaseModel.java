/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.impegno;

import java.util.Date;
import java.util.EnumSet;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;

/**
 * Classe base di model per la gestione della prima nota integrata sull'impegno.
 * 
 * @author Marchino Alessandro
 * @version 1.1.0 - 12/10/2015 - gestione GEN/GSA
 */
public abstract class GestioneImpegnoPrimaNotaIntegrataBaseModel extends BaseInserisciAggiornaPrimaNotaIntegrataBaseModel<Impegno>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2702843292890688351L;
	
	
	private Impegno impegno;
	
	/**
	 * @return the impegno
	 */
	public Impegno getImpegno() {
		return impegno;
	}

	/**
	 * @param impegno the impegno to set
	 */
	public void setImpegno(Impegno impegno) {
		this.impegno = impegno;
	}
	
	@Override
	public String getConsultazioneSubpath() {
		return "Impegno";
	}
	
	/**
	 * Alias per l'impegno
	 * @return the movimentoGestione
	 */
	public Impegno getMovimentoGestione() {
		return getImpegno();
	}
	
	/**
	 * @return the datiBaseCapitolo
	 */
	public String getDatiBaseCapitolo() {
		Capitolo<?, ?> capitolo = ottieniCapitolo();
		if(capitolo == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
			.append(capitolo.getNumeroCapitolo())
			.append(" / ")
			.append(capitolo.getNumeroArticolo());
		if(isGestioneUEB()) {
			sb.append(" / ")
				.append(capitolo.getNumeroUEB());
		}
		
		return sb.toString();
	}
	
	/**
	 * @return the datiBaseStrutturaAmministrativoContabileCapitolo
	 */
	public String getDatiBaseStrutturaAmministrativoContabileCapitolo() {
		Capitolo<?, ?> capitolo = ottieniCapitolo();
		if(capitolo == null || capitolo.getStrutturaAmministrativoContabile() == null) {
			return "";
		}
		return new StringBuilder()
			.append(capitolo.getStrutturaAmministrativoContabile().getCodice())
			.append(" - ")
			.append(capitolo.getStrutturaAmministrativoContabile().getDescrizione())
			.toString();
	}
	
	/**
	 * @return the datiBaseTipoFinanziamentoCapitolo
	 */
	public String getDatiBaseTipoFinanziamentoCapitolo() {
		Capitolo<?, ?> capitolo = ottieniCapitolo();
		if(capitolo == null || capitolo.getTipoFinanziamento() == null) {
			return "";
		}
		return new StringBuilder()
			.append(capitolo.getTipoFinanziamento().getCodice())
			.append(" - ")
			.append(capitolo.getTipoFinanziamento().getDescrizione())
			.toString();
	}
	
	/**
	 * @return the datiAttoAmministrativo
	 */
	public String getDatiAttoAmministrativo() {
		AttoAmministrativo attoAmministrativo = ottieniAttoAmministrativo();
		if(attoAmministrativo == null) {
			return "";
		}
		return new StringBuilder()
			.append(attoAmministrativo.getAnno())
			.append(" - ")
			.append(attoAmministrativo.getNumero())
			.toString();
	}
	
	/**
	 * @return the datiStrutturaAmministrativoContabile
	 */
	public String getDatiStrutturaAmministrativoContabile() {
		AttoAmministrativo attoAmministrativo = ottieniAttoAmministrativo();
		if(attoAmministrativo == null || attoAmministrativo.getStrutturaAmmContabile() == null) {
			return "";
		}
		return new StringBuilder()
			.append(attoAmministrativo.getStrutturaAmmContabile().getCodice())
			.append(" - ")
			.append(attoAmministrativo.getStrutturaAmmContabile().getDescrizione())
			.toString();
	}
	
	/**
	 * @return the datiAttoAmministrativoMovimentoGestione
	 */
	public String getDatiAttoAmministrativoMovimentoGestione() {
		if(getImpegno() == null || getImpegno().getAttoAmministrativo() == null) {
			return "";
		}
		return new StringBuilder()
			.append(getImpegno().getAttoAmministrativo().getAnno())
			.append(" - ")
			.append(getImpegno().getAttoAmministrativo().getNumero())
			.toString();
	}
	
	/**
	 * Ottiene il capitolo per il calcolo dei dati.
	 * @return il capitolo della transazione
	 */
	private Capitolo<?, ?> ottieniCapitolo() {
		return getImpegno() != null && getImpegno().getCapitoloUscitaGestione() != null
			? getImpegno().getCapitoloUscitaGestione()
			: null;
	}
	
	/**
	 * Ottiene l'atto amministrativo per il calcolo dei dati.
	 * @return l'atto amministrativo della transazione
	 */
	private AttoAmministrativo ottieniAttoAmministrativo() {
		return getImpegno() != null && getImpegno().getAttoAmministrativo() != null
			? getImpegno().getAttoAmministrativo()
			: null;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiaveOttimizzato}.
	 * 
	 * @param imp l'impegno per cui creare la request
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato(Impegno imp) {
		
		RicercaImpegnoPerChiaveOttimizzato req = new RicercaImpegnoPerChiaveOttimizzato();
		
		req.setDataOra(new Date());
		req.setRichiedente(richiedente);
		
		RicercaImpegnoK ricercaImpegnoK = new RicercaImpegnoK();
		ricercaImpegnoK.setAnnoEsercizio(getAnnoEsercizioInt());
		ricercaImpegnoK.setAnnoImpegno(impegno.getAnnoMovimento());
		ricercaImpegnoK.setNumeroImpegno(impegno.getNumero());
		
		req.setpRicercaImpegnoK(ricercaImpegnoK);
		req.setEnte(ente);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		req.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		// Non richiedo NESSUN importo derivato.
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		// Non richiedo NESSUN classificatore
		datiOpzionaliCapitoli.setTipologieClassificatoriRichiesti(EnumSet.of(TipologiaClassificatore.CDC, TipologiaClassificatore.TIPO_FINANZIAMENTO, TipologiaClassificatore.MACROAGGREGATO));
		req.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		req.setCaricaSub(false);
		return req;
		
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaContiConciliazionePerClasse}.
	 * 
	 * @param classeDiConciliazione la classeDiConciliazione per cui ricercare i conti di conciliazione
	 * @return la requet creata
	 */
	public RicercaContiConciliazionePerClasse creaRequestRicercaContiConciliazionePerClasse(ClasseDiConciliazione classeDiConciliazione) {
		RicercaContiConciliazionePerClasse req = creaRequest(RicercaContiConciliazionePerClasse.class);
		req.setClasseDiConciliazione(classeDiConciliazione);
		req.setCapitolo(getImpegno().getCapitoloUscitaGestione());
		req.setSoggetto(getImpegno().getSoggetto());
		return req;
	}
	
	/**
	 * Gets the anno movimento.
	 *
	 * @return the anno movimento
	 */
	public String  getAnnoMovimento() {
		return getImpegno() != null ? String.valueOf(getImpegno().getAnnoMovimento()) : "0";
	}
	
	/**
	 * Gets the numero movimento.
	 *
	 * @return the numero movimento
	 */
	public String  getNumeroMovimento() {
		return getImpegno() != null ? getImpegno().getNumero().toString() : "0";
	}
}



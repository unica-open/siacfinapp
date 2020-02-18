/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.subimpegno;

import java.util.Date;
import java.util.EnumSet;

import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;

/**
 * Classe base di model per la gestione della prima nota integrata sul subimpegno.
 * 
 * @author elisa
 * @version 1.0.0 - 12-03-2018
 */
public abstract class GestioneSubImpegnoPrimaNotaIntegrataBaseModel extends BaseInserisciAggiornaPrimaNotaIntegrataBaseModel<Impegno>{
	/** Per la serializzazione */
	private static final long serialVersionUID = 2242484089678087653L;
	
	private Impegno impegno;
	private SubImpegno subImpegno;

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

	/**
	 * @return the subImpegno
	 */
	public SubImpegno getSubImpegno() {
		return subImpegno;
	}

	/**
	 * @param subImpegno the subImpegno to set
	 */
	public void setSubImpegno(SubImpegno subImpegno) {
		this.subImpegno = subImpegno;
	}
	
	@Override
	public String getConsultazioneSubpath() {
		return "SubImpegno";
	}

	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiave}.
	 * 
	 * @param subimp il subimpegno per cui creare la request
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato(SubImpegno subimp) {
		Impegno impegnoPadre = new Impegno();
		impegnoPadre.setAnnoMovimento(subimp.getAnnoImpegnoPadre());
		impegnoPadre.setNumero(subimp.getNumeroImpegnoPadre());
		
		RicercaImpegnoPerChiaveOttimizzato req = new RicercaImpegnoPerChiaveOttimizzato();
		
		req.setDataOra(new Date());
		req.setRichiedente(richiedente);
		
		RicercaImpegnoK ricercaImpegnoK = new RicercaImpegnoK();
		ricercaImpegnoK.setAnnoEsercizio(getAnnoEsercizioInt());
		ricercaImpegnoK.setAnnoImpegno(impegnoPadre.getAnnoMovimento());
		ricercaImpegnoK.setNumeroImpegno(impegnoPadre.getNumero());
		
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
		req.setCaricaSub(true);
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

}



/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.saldo;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipologieClassificatori;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;

public abstract class BaseRicercaContoCorrenteModel extends GenericFinModel {
	
	private static final long serialVersionUID = 1L;

	private List<Codifica> elencoContiCorrenti;
	private CriteriRicercaContoCorrente criteriRicerca;
	private List<Integer> elencoAnniBilancio = new ArrayList<Integer>();

	public CriteriRicercaContoCorrente getCriteriRicerca() {
		return criteriRicerca == null ? criteriRicerca = new CriteriRicercaContoCorrente() : criteriRicerca;
	}

	public void setCriteriRicerca(CriteriRicercaContoCorrente criteriRicerca) {
		this.criteriRicerca = criteriRicerca;
	}

	/**
	 * A partire dai dati nel model compone la request per il servizio leggiClassificatoriByTipologieClassificatori
	 * @return
	 */
	public LeggiClassificatoriByTipologieClassificatori creaRequestLeggiClassificatoriByTipologieClassificatori() {
		//metodo della super classe:
		LeggiClassificatoriByTipologieClassificatori request = creaRequest(LeggiClassificatoriByTipologieClassificatori.class);
		//ente:
		request.setEnte(getEnte());
		//bilancio:
		request.setBilancio(getBilancioByAnno());
		List<TipologiaClassificatore> listaTipologieClassificatori = new ArrayList<TipologiaClassificatore>();
		//setto la tipologia richiesta:
		listaTipologieClassificatori.add(TipologiaClassificatore.CONTO_CORRENTE_PREDISPOSIZIONE_INCASSO);
		request.setListaTipologieClassificatori(listaTipologieClassificatori);
		return request;
	}

	private Bilancio getBilancioByAnno() {
		Bilancio bilancio = new Bilancio();
		bilancio.setAnno(criteriRicerca.getAnno());
		return bilancio;
	}

	public List<Codifica> getElencoContiCorrenti() {
		return elencoContiCorrenti;
	}

	public void setElencoContiCorrenti(List<Codifica> elencoContiCorrenti) {
		this.elencoContiCorrenti = elencoContiCorrenti;
	}

	public List<Integer> getElencoAnniBilancio() {
		return elencoAnniBilancio;
	}

	public void initElencoAnniBilancio(int annoRif) {
		for (int i = -2; i <= 2; i++){
			elencoAnniBilancio.add(annoRif + i);
		}
	}

}

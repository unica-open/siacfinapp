/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.EliminaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.StoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaStoricoImpegnoAccertamento;

/**
 * The Class GestisciStoricoImpegnoAccertamentoModel.
 * @author elisa
 * @version 1.0.0 - 05-07-2019
 */
public class GestisciStoricoImpegnoAccertamentoModel extends GenericPopupModel {

	private static final long serialVersionUID = 1L;
	
	
	private Impegno impegno;
	
	private SubImpegno subImpegno;
	
	private boolean ricercaImpegnoStep1Abilitata = true;
	
	private List<StoricoImpegnoAccertamento> listaStoricoImpegnoAccertamento = new ArrayList<StoricoImpegnoAccertamento>();
	
	private int resultSize;
	
	private StoricoImpegnoAccertamento storicoImpegnoAccertamentoInModifica;
	
	public int getResultSize() {
		return resultSize;
	}
	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}
	
	/**
	 * @return the storicoImpegnoAccertamentoInModifica
	 */
	public StoricoImpegnoAccertamento getStoricoImpegnoAccertamentoInModifica() {
		return storicoImpegnoAccertamentoInModifica;
	}
	/**
	 * @param storicoImpegnoAccertamentoInModifica the storicoImpegnoAccertamentoInModifica to set
	 */
	public void setStoricoImpegnoAccertamentoInModifica(StoricoImpegnoAccertamento storicoImpegnoAccertamentoInModifica) {
		this.storicoImpegnoAccertamentoInModifica = storicoImpegnoAccertamentoInModifica;
	}
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
	public boolean isRicercaImpegnoStep1Abilitata() {
		return ricercaImpegnoStep1Abilitata;
	}
	public void setRicercaImpegnoStep1Abilitata(boolean ricercaImpegnoStep1Abilitata) {
		this.ricercaImpegnoStep1Abilitata = ricercaImpegnoStep1Abilitata;
	}
	/**
	 * @return the listaStoricoImpegnoAccertamento
	 */
	public List<StoricoImpegnoAccertamento> getListaStoricoImpegnoAccertamento() {
		return listaStoricoImpegnoAccertamento;
	}
	
	/**
	 * Gets the abilita modifiche storico.
	 *
	 * @return the abilita modifiche storico
	 */
	public boolean getAbilitaModificheStorico() {
		return true;
	}
	/**
	 * @param listaStoricoImpegnoAccertamento the listaStoricoImpegnoAccertamento to set
	 */
	public void setListaStoricoImpegnoAccertamento(List<StoricoImpegnoAccertamento> listaStoricoImpegnoAccertamento) {
		this.listaStoricoImpegnoAccertamento = listaStoricoImpegnoAccertamento != null? listaStoricoImpegnoAccertamento : new ArrayList<StoricoImpegnoAccertamento>();
	}
	
	/**
	 * Crea request ricerca storico impegno accertamento.
	 *
	 * @param bilancio the bilancio
	 * @return the ricerca storico impegno accertamento
	 */
	public RicercaStoricoImpegnoAccertamento creaRequestRicercaStoricoImpegnoAccertamento(Bilancio bilancio) {
		RicercaStoricoImpegnoAccertamento req = creaRequest(RicercaStoricoImpegnoAccertamento.class);
		req.setEnte(getEnte());
		boolean subValorizzato = getSubImpegno() != null && getSubImpegno().getNumero() != null && BigDecimal.ZERO.compareTo(getSubImpegno().getNumero())<0;  
		ParametroRicercaStoricoImpegnoAccertamento parametroRicercaStoricoImpegnoAccertamento = new ParametroRicercaStoricoImpegnoAccertamento();
		
		StoricoImpegnoAccertamento st = new StoricoImpegnoAccertamento();
		st.setImpegno(getImpegno());
		st.setSubImpegno(subValorizzato? getSubImpegno() : null);
		parametroRicercaStoricoImpegnoAccertamento.setStoricoImpegnoAccertamento(st);
		
		parametroRicercaStoricoImpegnoAccertamento.setEscludiSubImpegni(Boolean.valueOf(!subValorizzato)); 
		
		parametroRicercaStoricoImpegnoAccertamento.setBilancio(bilancio);
		req.setParametroRicercaStoricoImpegnoAccertamento(parametroRicercaStoricoImpegnoAccertamento);
		return req;
	}
	
	/**
	 * Crea request inserisci storico impegno accertamento.
	 *
	 * @return the inserisce storico impegno accertamento
	 */
	public InserisceStoricoImpegnoAccertamento creaRequestInserisciStoricoImpegnoAccertamento(Bilancio bilancio) {
		InserisceStoricoImpegnoAccertamento req = creaRequest(InserisceStoricoImpegnoAccertamento.class);
		req.setStoricoImpegnoAccertamento(getStoricoImpegnoAccertamentoInModifica());
		
		//il subacc e' facoltativo
		SubAccertamento subAccertamento = req.getStoricoImpegnoAccertamento().getSubAccertamento();
		if(subAccertamento != null && (subAccertamento.getNumero() == null || BigDecimal.ZERO.compareTo(subAccertamento.getNumero()) > 0)) {
			req.getStoricoImpegnoAccertamento().setSubAccertamento(null);
		}
		
		req.getStoricoImpegnoAccertamento().setImpegno(getImpegno());
		req.setEnte(getEnte());
		req.setBilancio(bilancio);
		req.getStoricoImpegnoAccertamento().setSubImpegno(getSubImpegno() != null && getSubImpegno().getNumero() != null && BigDecimal.ZERO.compareTo(getSubImpegno().getNumero())<0?
				getSubImpegno() : null);
		return req;
	}
	
	/**
	 * Crea request aggiorna storico impegno accertamento.
	 *
	 * @return the aggiorna storico impegno accertamento
	 */
	public AggiornaStoricoImpegnoAccertamento creaRequestAggiornaStoricoImpegnoAccertamento(Bilancio bilancio) {
		AggiornaStoricoImpegnoAccertamento req = creaRequest(AggiornaStoricoImpegnoAccertamento.class);
		
		req.setStoricoImpegnoAccertamento(getStoricoImpegnoAccertamentoInModifica());
		
		//il subacc e' facoltativo
		SubAccertamento subAccertamento = req.getStoricoImpegnoAccertamento().getSubAccertamento();
		if(subAccertamento != null && subAccertamento.getNumero() != null && BigDecimal.ZERO.compareTo(subAccertamento.getNumero()) >0) {
			req.getStoricoImpegnoAccertamento().setSubAccertamento(null);
		}
		
		req.getStoricoImpegnoAccertamento().setImpegno(getImpegno());
		req.setEnte(getEnte());
		req.setBilancio(bilancio);
		return req;
	}
	
	/**
	 * Crea request elimina storico impegno accertamento.
	 *
	 * @param bilancio the bilancio
	 * @return the elimina storico impegno accertamento
	 */
	public EliminaStoricoImpegnoAccertamento creaRequestEliminaStoricoImpegnoAccertamento(Bilancio bilancio) {
		EliminaStoricoImpegnoAccertamento req = creaRequest(EliminaStoricoImpegnoAccertamento.class);
		req.setStoricoImpegnoAccertamento(getStoricoImpegnoAccertamentoInModifica());
		req.setBilancio(bilancio);
		return req;
	}
	
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.saldo;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacfinapp.frontend.ui.action.GenericFinAction;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.saldo.BaseContoCorrenteModel;
import it.csi.siac.siacfinapp.frontend.ui.model.saldo.CriteriRicercaContoCorrente;
import it.csi.siac.siacfinser.frontend.webservice.SaldoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiVociContoCorrente;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiVociContoCorrenteResponse;
import it.csi.siac.siacfinser.model.saldo.VociContoCorrente;

public abstract class BaseContoCorrenteAction<BCCM extends BaseContoCorrenteModel> extends GenericFinAction<BCCM>{
	
	private static final long serialVersionUID = 1L;

	@Autowired
	protected SaldoService saldoService;

	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo nel model:
		this.model.setTitolo(getTitolo());
	}

	protected abstract String getTitolo();

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		//cerchiamo le voci:
		cerca();
		return SUCCESS;
	}

	public void cerca() {
		//leggo le voci:
		VociContoCorrente voci = leggiVociContoCorrente();
		//e le setto nel model:
		model.setVociContoCorrente(voci);
	}

	protected VociContoCorrente leggiVociContoCorrente() {
		CriteriRicercaContoCorrente criteri = sessionHandler.getParametro(FinSessionParameter.RICERCA_CONTO_CORRENTE_CRITERI_RICERCA_CONTO_CORRENTE,CriteriRicercaContoCorrente.class);

		//istanzio la request per il servizio leggiVociContoCorrente:
		LeggiVociContoCorrente lvccReq = new LeggiVociContoCorrente();

		lvccReq.setAnno(criteri.getAnno());
		lvccReq.setIdClassifContoCorrente(criteri.getIdClassifConto());
		lvccReq.setDataInizio(criteri.getDataInizio());
		lvccReq.setDataFine(criteri.getDataFine());
		lvccReq.setEnte(sessionHandler.getEnte());
		lvccReq.setRichiedente(sessionHandler.getRichiedente());

		//invoco il servizio leggiVociContoCorrente:
		LeggiVociContoCorrenteResponse lvccRes = saldoService.leggiVociContoCorrente(lvccReq);

		VociContoCorrente vociContoCorrente = lvccRes.getVociContoCorrente();

		if (vociContoCorrente == null) {
			//se e' nullo devo istanziarlo
			//per evitare problemi di null pointer
			vociContoCorrente = new VociContoCorrente();
			vociContoCorrente.setIdClassifConto(criteri.getIdClassifConto());
			vociContoCorrente.setContoCorrente(criteri.getContoCorrente());
		}

		//ANNO:
		vociContoCorrente.setAnno(criteri.getAnno());

		return vociContoCorrente;
	}

}

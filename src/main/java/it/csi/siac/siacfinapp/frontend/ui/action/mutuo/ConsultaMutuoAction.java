/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.mutuo;

import java.util.Date;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.action.GenericFinAction;
import it.csi.siac.siacfinapp.frontend.ui.model.mutuo.ConsultaMutuoModel;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaMutuoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaMutuoPerChiaveResponse;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;
import it.csi.siac.siacfinser.model.ric.RicercaMutuoK;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaMutuoAction extends GenericFinAction<ConsultaMutuoModel> {

	private static final long serialVersionUID = 4663570533611765954L;
	
	private String methodName;
	private String numero;

	public String getActionKey() {
		return "consultaImpegno";
	}

	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		model.setTitolo("Consulta movimento");
	}

	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		// richiama servizio di ricerca
		RicercaMutuoPerChiaveResponse response = getMutuo();

		Mutuo mutuo = response.getMutuo();
		model.setMutuo(mutuo);
		// presenza di paginazione
		for (String key : getRequest().keySet()) {
			if (key.startsWith("d-") && key.endsWith("-p")){
				model.setActiveTab("voci");
			}
		}

		return "success";
	}

	/* **************************************************************************** */
	/*  Private methods																*/
	/* **************************************************************************** */

	/**
	 * restituisce l'oggetto mutuo da una ricerca per chiave
	 * @return
	 */
	private RicercaMutuoPerChiaveResponse getMutuo() {
		RicercaMutuoPerChiave request = new RicercaMutuoPerChiave();

		RicercaMutuoK pRicercaMutuoK = new RicercaMutuoK();
		pRicercaMutuoK.setMutCode(numero);

		request.setEnte(this.sessionHandler.getEnte());
		request.setRichiedente(this.sessionHandler.getRichiedente());

		request.setDataOra(new Date());
		request.setpRicercaMutuoK(pRicercaMutuoK);

		RicercaMutuoPerChiaveResponse response = this.mutuoService.ricercaMutuoPerChiave(request);
		if ((response.isFallimento()) || ((response.getErrori() != null) && (response.getErrori().size() > 0))) {
			debug(this.methodName, new Object[] { "Errore nella risposta del servizio" });
			addErrori(this.methodName, response);
			return null;
		}
		return response;
	}

	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */

	public String getMethodName() {
		return this.methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
}

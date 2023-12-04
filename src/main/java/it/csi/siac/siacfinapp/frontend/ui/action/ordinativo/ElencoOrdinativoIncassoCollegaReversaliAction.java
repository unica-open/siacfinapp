/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinser.frontend.webservice.msg.CollegaReversaliOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.CollegaReversaliOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaOrdinativoPagamentoK;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ElencoOrdinativoIncassoCollegaReversaliAction extends ElencoOrdinativoIncassoAction  {

	private static final long serialVersionUID = 1L;
	
	
	@Override
	public void prepare() throws Exception {

		super.prepare();

		this.model.setTitolo("Elenco ordinativi incasso");   
		
		model.setOrdinativoPagamento(readOrdinativoPagamento());
		
		model.setUidOrdIncassoSelezionati(null);
		model.setTotImportoSelezionati(null);
	}

	@Override
	protected RicercaOrdinativo convertiModelPerChiamataServizioRicercaOrdinativoIncasso() {
		
		RicercaOrdinativo req = super.convertiModelPerChiamataServizioRicercaOrdinativoIncasso();
		
		req.getParametroRicercaOrdinativoIncasso().setFiltraPerCollegaReversali(Boolean.TRUE);
		
		return req;
	}

	private OrdinativoPagamento readOrdinativoPagamento() {
		
		RicercaOrdinativoPagamentoPerChiave req = new RicercaOrdinativoPagamentoPerChiave();
		
		req.setDataOra(new Date());
		req.setRichiedente(sessionHandler.getRichiedente());
		req.setEnte(sessionHandler.getEnte());
		
		RicercaOrdinativoPagamentoK pRicercaOrdinativoPagamentoK = new RicercaOrdinativoPagamentoK();
		OrdinativoPagamento ordinativoPagamento = new OrdinativoPagamento();
		ordinativoPagamento.setUid(model.getUidOrdCollegaReversali());
		pRicercaOrdinativoPagamentoK.setOrdinativoPagamento(ordinativoPagamento);
		pRicercaOrdinativoPagamentoK.setBilancio(sessionHandler.getBilancio());
		
		req.setpRicercaOrdinativoPagamentoK(pRicercaOrdinativoPagamentoK);
		
		RicercaOrdinativoPagamentoPerChiaveResponse res = ordinativoService.ricercaOrdinativoPagamentoPerChiave(req);
		
		analizzaEsitoServizio(res, "it.csi.siac.siacfinapp.frontend.ui.action.ordinativo.ElencoOrdinativoIncassoCollegaReversaliAction.readOrdinativoPagamento()");
		
		return res.getOrdinativoPagamento();
	}
	
	
	public String collegaReversali() {
		CollegaReversaliOrdinativoPagamento req = new CollegaReversaliOrdinativoPagamento();
		
		req.setRichiedente(sessionHandler.getRichiedente());
		req.setIdOrdinativoPagamento(model.getUidOrdCollegaReversali());
		req.setIdOrdinativiIncasso(model.getUidOrdIncassoSelezionatiSplit());
		
		CollegaReversaliOrdinativoPagamentoResponse res = 
				ordinativoService.collegaReversaliOrdinativoPagamento(req); 

		if (res.hasErrori()) {
			addPersistentActionError(res.getDescrizioneErrori());
		} else {		
			addPersistentActionMessage("Operazione effettuata correttamente");
		}
		
		return "elencoOrdinativoPagamento";
	}
}
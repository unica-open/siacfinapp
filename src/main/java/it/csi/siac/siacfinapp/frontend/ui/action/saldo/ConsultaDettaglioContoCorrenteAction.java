/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.saldo;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.saldo.ConsultaDettaglioContoCorrenteModel;
import it.csi.siac.siacfinapp.frontend.ui.model.saldo.CriteriRicercaContoCorrente;
import it.csi.siac.siacfinser.frontend.webservice.msg.CalcolaSaldoCassaData;
import it.csi.siac.siacfinser.frontend.webservice.msg.CalcolaSaldoCassaDataResponse;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaDettaglioContoCorrenteAction extends BaseContoCorrenteAction<ConsultaDettaglioContoCorrenteModel>{
	private static final long serialVersionUID = 1L;

	@Override
	protected String getTitolo() {
		//ritorniamo il titolo per la pagina
		return "Consulta Conto Corrente";
	}

	@Override
	public void prepare() throws Exception {
		//invochiamo il prepare della super classe:
		super.prepare();

		//leggo i criteri di ricerca dalla sessione:
		CriteriRicercaContoCorrente criteri = sessionHandler.getParametro(FinSessionParameter.RICERCA_CONTO_CORRENTE_CRITERI_RICERCA_CONTO_CORRENTE,CriteriRicercaContoCorrente.class);
		
		//setto i dati nel model:
		model.setDataPrec(DateUtils.addDays(criteri.getDataInizio(), -1));
		model.setData(criteri.getDataInizio());
		
		//calcolo il saldo precedente:
		BigDecimal saldoPrec = calcolaSaldoCassaData(criteri.getIdClassifConto(), model.getDataPrec(), criteri.getAnno());
		model.setSaldoCassaPrec(saldoPrec);
		
		//calcolo il sado rispetto a data inizio:
		BigDecimal saldoCassa = calcolaSaldoCassaData(criteri.getIdClassifConto(), criteri.getDataInizio(), criteri.getAnno());
		model.setSaldoCassa(saldoCassa);
	}

	/**
	 * Calcola il saldo cassa alla data indicata
	 * @param idClassifConto
	 * @param data
	 * @param anno
	 * @return
	 */
	private BigDecimal calcolaSaldoCassaData(Integer idClassifConto, Date data, Integer anno){
		
		//istanzio la request per il servizio calcolaSaldoCassaData:
		CalcolaSaldoCassaData cscdReq = new CalcolaSaldoCassaData();

		//setto i dati nella request:
		cscdReq.setData(data);
		cscdReq.setAnno(anno);
		cscdReq.setIdClassifConto(idClassifConto);

		cscdReq.setEnte(sessionHandler.getEnte());
		cscdReq.setRichiedente(sessionHandler.getRichiedente());

		//invoco il servizio calcolaSaldoCassaData:
		CalcolaSaldoCassaDataResponse cscdRes = saldoService.calcolaSaldoCassaData(cscdReq);

		//leggo l'output del servizio 
		BigDecimal saldoCassa = cscdRes.getSaldoCassa();

		//e ritorno il valore del saldo:
		return saldoCassa;
	}
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.saldo;

import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipologieClassificatori;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipologieClassificatoriResponse;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.GenericFinAction;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.saldo.BaseRicercaContoCorrenteModel;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class BaseRicercaContoCorrenteAction<BRCCM extends BaseRicercaContoCorrenteModel> extends GenericFinAction<BRCCM>
{

	private static final long serialVersionUID = 1L;

	@Autowired
	private ClassificatoreBilService classificatoreBilService;

	@Override
	public void prepare() throws Exception {
		//chiamo il prepare della superclasse:
		super.prepare();
		//setto il titolo nel model:
		this.model.setTitolo("Ricerca Conto Corrente");
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		//leggo l'anno di esercizio dalla sessione:
		int annoEsercizio = sessionHandler.getAnnoBilancio();

		//setto l'anno nel model:
		model.initElencoAnniBilancio(annoEsercizio);

		if (model.getCriteriRicerca().getAnno() == null){
			//se nullo uso quello di esercizio
			model.getCriteriRicerca().setAnno(annoEsercizio);
		}

		//leggo i conti correnti:
		List<Codifica> contiCorrenti = readElencoContiCorrenti();
		//e li setto nel model:
		model.setElencoContiCorrenti(contiCorrenti);

		return SUCCESS;
	}

	/**
	 * Metodo di validazione
	 */
	public void validateCerca() {
		if (model.getCriteriRicerca().getIdClassifConto() == null){
			//conto corrente non indicato
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("conto corrente"));
		}
	}

	/**
	 * Rimanda al cerca
	 * @return
	 * @throws Exception
	 */
	public String cerca() throws Exception {
		//setto i dati del model nella session
		//per averli ancora al cambio pagina:
		sessionHandler.setParametro(FinSessionParameter.RICERCA_CONTO_CORRENTE_CRITERI_RICERCA_CONTO_CORRENTE,model.getCriteriRicerca());
		return "cerca";
	}

	/**
	 * Metodo per leggere i conti correnti
	 * @return
	 */
	private List<Codifica> readElencoContiCorrenti() {
		//istanzio la request per il servizio leggiClassificatoriByTipologieClassificatori:
		LeggiClassificatoriByTipologieClassificatori request = model.creaRequestLeggiClassificatoriByTipologieClassificatori();
		logServiceRequest(request);
		//invoco il servizio leggiClassificatoriByTipologieClassificatori:
		LeggiClassificatoriByTipologieClassificatoriResponse response = classificatoreBilService.leggiClassificatoriByTipologieClassificatori(request);
		logServiceResponse(response);
		//ritorno le codifiche dei conti correnti
		//ottenute dal servizio leggiClassificatoriByTipologieClassificatori:
		return response.getCodifiche();
	}

}

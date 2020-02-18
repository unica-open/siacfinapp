/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.util.ArrayList;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.GenericFinAction;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciStoricoImpegnoAccertamentoModel;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaStoricoImpegnoAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.EliminaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.EliminaStoricoImpegnoAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceStoricoImpegnoAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStoricoImpegnoAccertamentoResponse;
import it.csi.siac.siacfinser.model.StoricoImpegnoAccertamento;

/**
 * The Class GestisciStoricoImpegnoAccertamentoStep2Action.
 * @author elisa
 * @version 1.0.0 - 09-07-2019
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestisciStoricoImpegnoAccertamentoStep2Action extends GenericFinAction<GestisciStoricoImpegnoAccertamentoModel>{

	private static final long serialVersionUID = 1L;
	
	private static final String RICARICA_PAGINA = "refreshStep2";
	
	@Autowired
	private MovimentoGestioneService movimentoGestionService;
	
	
	/**
	 * Instantiates a new gestisci storico impegno accertamento step 2 action.
	 */
	public GestisciStoricoImpegnoAccertamentoStep2Action () {
	   	//setto la tipologia di oggetto trattato:
//		oggettoDaPopolare = OggettoDaPopolareEnum.;
	}
	
	@Override
	public String getActionKey() {
		return "storicoImpegnoAccertamento";
	}

	/**
	 * metodo prepare della action
	 */
	@Override
	public void prepare() throws Exception {
		
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		this.model.setTitolo("Gestione storico - Associa accertamento");
	    
	}
	
	/**
	 * metodo execute della action
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		resetPageNumberTableId("tabellaStoricoImpegniAccertamento");
		caricaListaStoricoNelModel();
		model.setStoricoImpegnoAccertamentoInModifica(null);
		return SUCCESS;
	}
	
	/**
	 * Carica lista storico nel model.
	 */
	public void caricaListaStoricoNelModel() {
		final String methodName = "caricaListaStoricoNelModel";
		//dati per la paginazione dei risultati:
		RicercaStoricoImpegnoAccertamento request = model.creaRequestRicercaStoricoImpegnoAccertamento(sessionHandler.getBilancio());
		RicercaStoricoImpegnoAccertamentoResponse res = movimentoGestionService.ricercaStoricoImpegnoAccertamento(request);
		if (res.hasErrori()) {
			addErrori(methodName, res);
			return;
		}
		model.setListaStoricoImpegnoAccertamento(res.getElencoStoricoImpegnoAccertamento());
		
		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_STORICO_IMPEGNI_ACCERTAMENTI, res.getElencoStoricoImpegnoAccertamento()!= null? res.getElencoStoricoImpegnoAccertamento() : new ArrayList<StoricoImpegnoAccertamento>() );
		model.setResultSize(res.getNumRisultati());
		
	}
	
	
	
	/**
	 * Validate inserisci storico.
	 */
	public void validateInserisciStorico() {
		if(model.getStoricoImpegnoAccertamentoInModifica() == null){
			addActionError(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("storico").getTesto());
		}
		if(model.getStoricoImpegnoAccertamentoInModifica().getAccertamento() == null || model.getStoricoImpegnoAccertamentoInModifica().getAccertamento().getAnnoMovimento() == 0 || model.getStoricoImpegnoAccertamentoInModifica().getAccertamento().getNumero() == null ){
			addActionError(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("accertamento").getTesto());
		}
		if(model.getImpegno() == null || model.getImpegno().getAnnoMovimento() == 0 || model.getImpegno().getNumero() == null) {
			addActionError(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("impegno").getTesto());
		}
	}
	
	/**
	 * Inserisci storico.
	 *
	 * @return the string
	 */
	public String inserisciStorico() {
		
		InserisceStoricoImpegnoAccertamento req = model.creaRequestInserisciStoricoImpegnoAccertamento(sessionHandler.getBilancio());
		InserisceStoricoImpegnoAccertamentoResponse response = movimentoGestionService.inserisceStoricoImpegnoAccertamento(req);
		if(response.hasErrori()){
			addErrori(response);
			return INPUT;
		}
		model.setStoricoImpegnoAccertamentoInModifica(null);
		return RICARICA_PAGINA;
	}
	
	/**
	 * Validate aggiorna storico.
	 */
	public void validateAggiornaStorico() {
		if(model.getStoricoImpegnoAccertamentoInModifica() == null || model.getStoricoImpegnoAccertamentoInModifica().getUid() == 0){
			addActionError(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("storico").getTesto());
		}
		if(model.getStoricoImpegnoAccertamentoInModifica().getAccertamento() == null || model.getStoricoImpegnoAccertamentoInModifica().getAccertamento().getAnnoMovimento() == 0 || model.getStoricoImpegnoAccertamentoInModifica().getAccertamento().getNumero() == null ){
			addActionError(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("accertamento").getTesto());
		}
		if(model.getImpegno() == null || model.getImpegno().getAnnoMovimento() == 0 || model.getImpegno().getNumero() == null) {
			addActionError(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("impegno").getTesto());
		}
	}
	
	/**
	 * Aggiorna storico.
	 *
	 * @return the string
	 */
	public String aggiornaStorico() {
		
		AggiornaStoricoImpegnoAccertamento req = model.creaRequestAggiornaStoricoImpegnoAccertamento(sessionHandler.getBilancio());
		AggiornaStoricoImpegnoAccertamentoResponse response = movimentoGestionService.aggiornaStoricoImpegnoAccertamento(req);
		if(response.hasErrori()){
			addErrori(response);
			return INPUT;
		}
		model.setStoricoImpegnoAccertamentoInModifica(null);
		return RICARICA_PAGINA;
	}


	
	/**
	 * Validate elimina storico.
	 */
	public void validateElimina() {
		if(model.getStoricoImpegnoAccertamentoInModifica() == null || model.getStoricoImpegnoAccertamentoInModifica().getUid() == 0){
			addActionError(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("storico").getTesto());
		}
	}
	
	/**
	 * Elimina storico.
	 *
	 * @return the string
	 */
	public String elimina() {
		EliminaStoricoImpegnoAccertamento req = model.creaRequestEliminaStoricoImpegnoAccertamento(sessionHandler.getBilancio());
		EliminaStoricoImpegnoAccertamentoResponse response = movimentoGestionService.eliminaStoricoImpegnoAccertamento(req);
		if(response.hasErrori()){
			addErrori(response);
			return INPUT;
		}
		model.setStoricoImpegnoAccertamentoInModifica(null);
		return RICARICA_PAGINA;
	}
	

	/**
	 * Annulla step 2.
	 *
	 * @return the string
	 */
	public String annullaStep2() {
		model.setStoricoImpegnoAccertamentoInModifica(null);
		model.setImpegno(null);
		model.setSubImpegno(null);
		return SUCCESS; // RICARICA_PAGINA;
	}
}
/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.subaccertamento;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.subaccertamento.GestioneSubAccertamentoPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, subaccertamento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneSubAccertamentoInsPrimaNotaIntegrataBaseAction<M extends GestioneSubAccertamentoPrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaPrimaNotaIntegrataBaseAction<Accertamento, M>  {

	/** Per la serializzazione */

	private static final long serialVersionUID = 5436796062428480819L;

	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	
	/** Nome del model del completamento e validazione dell'accertamento. Modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_SUBACCERTAMENTO_FIN = "CompletaValidaSubAccertamentoInsPrimaNotaIntegrataFINModel";
	/** Nome del model del completamento e validazione dell'accertamento. Modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_SUBACCERTAMENTO_GSA = "CompletaValidaSubAccertamentoInsPrimaNotaIntegrataGSAModel";
	/** Nome del model del completamento dell'accertamento, modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_SUBACCERTAMENTO_FIN = "CompletaSubAccertamentoInsPrimaNotaIntegrataFINModel";
	/** Nome del model del completamento dell'accertamento, modulo GSA */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_SUBACCERTAMENTO_GSA = "CompletaSubAccertamentoInsPrimaNotaIntegrataGSAModel";
	
	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		
		// Recupero registrazione dalla sessione
		RegistrazioneMovFin registrazioneMovFin = sessionHandler.getParametro(FinSessionParameter.REGISTRAZIONEMOVFIN);
		model.setRegistrazioneMovFin(registrazioneMovFin);
		log.debug(methodName, "registrazione ottenuta");
		
		if(inEsecuzioneRegistrazioneMovFin()) {
			return INPUT;
		}
		
		sessionHandler.setParametro(FinSessionParameter.SUBACCERTAMENTO, null);
		
		SubAccertamento subAccertamento = (SubAccertamento) model.getRegistrazioneMovFin().getMovimento();
		model.setSubAccertamento(subAccertamento);
		
		if(model.getPrimaNota() == null){
			model.setPrimaNota(new PrimaNota());
			model.impostaDatiNelModel();
		}
		computaStringheDescrizioneDaRegistrazione(model.getRegistrazioneMovFin());

		try{
			caricaListaCausaleEP(false);
			caricaListaClassi();
			caricaListaTitoli();
		}catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			// Metto in sessione gli errori ed esco
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		
		getDatiDaSubAccertamento(subAccertamento);
		ricavaSoggettoDaMovimento(model.getSubAccertamento());
		return SUCCESS;
	}

	/**
	 * Ottiene i dati del subaccertamento
	 * @param subAccertamento il subaccertamento da cui ottenere i dati
	 */
	private void getDatiDaSubAccertamento(SubAccertamento subAccertamento){
		SubAccertamento subAccertamentoSessione = sessionHandler.getParametro(FinSessionParameter.SUBACCERTAMENTO);
		Accertamento accertamentoSessione = sessionHandler.getParametro(FinSessionParameter.ACCERTAMENTO);
		
		if(subAccertamentoSessione == null || subAccertamentoSessione.getUid() != subAccertamento.getUid()) {
			// Carico da servizio

			RicercaAccertamentoPerChiaveOttimizzato request = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato(subAccertamento);
			logServiceRequest(request);
			
			RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(request);
			
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				return;
			}
			
			accertamentoSessione = response.getAccertamento();
			subAccertamentoSessione = findSubAccertamento(response.getAccertamento(), subAccertamento);
			// Imposto in sessione
			sessionHandler.setParametro(FinSessionParameter.SUBACCERTAMENTO, subAccertamentoSessione);
			sessionHandler.setParametro(FinSessionParameter.ACCERTAMENTO, accertamentoSessione);
		}
		
		model.setSubAccertamento(subAccertamentoSessione);
		model.setAccertamento(accertamentoSessione);
		model.setClassificatoreGerarchico(model.getAccertamento().getCapitoloEntrataGestione().getCategoriaTipologiaTitolo());
		
	}

	/**
	 * Trova il subAccertamento dall'accertamento restituito dal servizio di ricerca.
	 * 
	 * @param accertamento    l'accertamento del servizio di ricerca
	 * @param subAccertamento il subaccertamento con uid da trovare
	 * @return il subaccertamento
	 */
	private SubAccertamento findSubAccertamento(Accertamento accertamento, SubAccertamento subAccertamento) {
		final String methodName = "findSubAccertamento";
		if(accertamento == null || accertamento.getElencoSubAccertamenti() == null) {
			log.warn(methodName, "SubAccertamento non reperibile dalla response per la registrazione " + model.getRegistrazioneMovFin().getUid());
			return null;
		}
		for(SubAccertamento sa : accertamento.getElencoSubAccertamenti()) {
			if(sa != null && sa.getUid() == subAccertamento.getUid()) {
				return sa;
			}
		}
		log.warn(methodName, "Nessun subaccertamento con uid " + subAccertamento.getUid() + " reperibile nell'accertamento con uid " + accertamento.getUid()
				+ " ottenuto per la registrazione " + model.getRegistrazioneMovFin().getUid());
		return null;
	}

	/**
	 * Ottiene il soggetto dal movimento.
	 * 
	 * @param subAccertamento il subaccertamento da cui ottenere il soggetto
	 */
	private void ricavaSoggettoDaMovimento(SubAccertamento subAccertamento) {
		if (subAccertamento != null) {
			model.setSoggettoMovimentoFIN(subAccertamento.getSoggetto());
		}
	}
	
	@Override
	protected void computaStringheDescrizioneDaRegistrazione(RegistrazioneMovFin registrazioneMovFin){
		
		StringBuilder movimento = new StringBuilder();
		StringBuilder descrizione = new StringBuilder();
		StringBuilder titolo = new StringBuilder();
		SubAccertamento subAccertamento = (SubAccertamento) registrazioneMovFin.getMovimento();
		titolo.append("Subaccertamento ").append(componiStringaSubAcc(subAccertamento));
		
		descrizione.append("SubAcc ").append(componiStringaSubAcc(subAccertamento));
			
		if(StringUtils.isNotBlank(subAccertamento.getDescrizione())) {
			descrizione.append(" ")
				.append(subAccertamento.getDescrizione());
		}
		movimento.append(subAccertamento.getAnnoMovimento())
			.append("/")
			.append(componiStringaSubAcc(subAccertamento))
			.append(" (subacc)");

		model.setDescrMovimentoFinanziario(movimento.toString());
		model.getPrimaNota().setDescrizione(descrizione.toString());
		model.getPrimaNota().setDataRegistrazione(new Date());
		model.setCampoTitoloPagina(titolo.toString());
	}

	/**
	 * Validazione per il metodo {@link #completeSalva()}.
	 */
	public void validateCompleteSalva() {
		// Se non ho la prima nota, qualcosa e' andato storto: esco subito
		checkNotNull(model.getPrimaNota(), "Prima Nota Integrata ", true);
		checkNotNullNorInvalidUid(model.getCausaleEP(), "CausaleEP");
		checkNotNull(model.getPrimaNota().getDataRegistrazione(), "Data registrazione ");
		checkNotNullNorEmpty(model.getPrimaNota().getDescrizione(), "Descrizione ");

		List<MovimentoDettaglio> listaMovimentiDettaglioFinal = checkScrittureCorrette();
		
		// Esco se ho errori
		if(hasErrori()) {
			return;
		}
		
		MovimentoEP movEP = new MovimentoEP();
		movEP.setCausaleEP(model.getCausaleEP());
		movEP.setRegistrazioneMovFin(model.getRegistrazioneMovFin());
		model.setListaMovimentoDettaglio(listaMovimentiDettaglioFinal);
		movEP.setListaMovimentoDettaglio(listaMovimentiDettaglioFinal);
		
		model.getListaMovimentoEP().add(movEP);
	}

	/**
	 * Completamento per lo step 2.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	
	public String completeSalva() {
		final String methodName = "completeSalva";
		
		aggiornaNumeroRiga();
		// L'inserisci e' sempre presente da registrazione quindi chiamo il completa
		
		// Inserimento della causale
		RegistraPrimaNotaIntegrata request = model.creaRequestRegistraPrimaNotaIntegrata();
		logServiceRequest(request);
		RegistraPrimaNotaIntegrataResponse response = primaNotaService.registraPrimaNotaIntegrata(request);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'inserimento della Prima Nota integrata");
			addErrori(response);
			return INPUT;
		}
		
		log.info(methodName, "registrata correttamente Prima Nota integrata con uid " + response.getPrimaNota().getUid());
		if (model.isValidazione()) {
			log.info(methodName, "validata correttamente Prima Nota integrata con uid " + response.getPrimaNota().getUid());
		}
		
		// Imposto i dati della causale restituitimi dal servizio
		model.setPrimaNota(response.getPrimaNota());
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}

	@Override
	protected ElementoPianoDeiConti ottieniElementoPianoDeiContiDaMovimento() {
		return model.getAccertamento() != null
			&& model.getAccertamento().getCapitoloEntrataGestione() != null
				? model.getAccertamento().getCapitoloEntrataGestione().getElementoPianoDeiConti()
				: null;
	}
	
	@Override
	protected Soggetto ottieniSoggettoDaMovimento() {
		return model.getSubAccertamento() != null ? model.getSubAccertamento().getSoggetto() : null;
	}
	
	/**
	 * Componi stringa sub acc.
	 *
	 * @param subAccertamento the sub accertamento
	 * @return the string
	 */
	private String componiStringaSubAcc(SubAccertamento subAccertamento) {
		StringBuilder sub = new StringBuilder();
		sub.append(subAccertamento.getAnnoAccertamentoPadre())
		.append("/")
		.append(subAccertamento.getNumeroAccertamentoPadre().toPlainString())
		.append("-")
		.append(subAccertamento.getNumero().toPlainString());
		return sub.toString();
	}
}


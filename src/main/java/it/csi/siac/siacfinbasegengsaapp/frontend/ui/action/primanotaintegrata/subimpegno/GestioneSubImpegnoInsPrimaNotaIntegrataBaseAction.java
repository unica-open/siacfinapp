/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.subimpegno;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.subimpegno.GestioneSubImpegnoPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, subimpegno.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 21/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneSubImpegnoInsPrimaNotaIntegrataBaseAction<M extends GestioneSubImpegnoPrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaPrimaNotaIntegrataBaseAction<Impegno, M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8985890954785351729L;

	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	
	/** Nome del model del completamento e validazione dell'accertamento. Modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_SUBIMPEGNO_FIN = "CompletaValidaSubImpegnoInsPrimaNotaIntegrataFINModel";
	/** Nome del model del completamento e validazione dell'accertamento. Modulo GSA */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_SUBIMPEGNO_GSA = "CompletaValidaSubImpegnoInsPrimaNotaIntegrataGSAModel";
	/** Nome del model del completamento dell'accertamento, modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_SUBIMPEGNO_FIN = "CompletaSubImpegnoInsPrimaNotaIntegrataFINModel";
	/** Nome del model del completamento dell'accertamento, modulo GSA */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_SUBIMPEGNO_GSA = "CompletaSubImpegnoInsPrimaNotaIntegrataGSAModel";
	
	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		
//		checkCasoDUsoApplicabile();
		
		// Recupero registrazione dalla sessione
		RegistrazioneMovFin registrazioneMovFin = sessionHandler.getParametro(FinSessionParameter.REGISTRAZIONEMOVFIN);
		model.setRegistrazioneMovFin(registrazioneMovFin);
		log.debug(methodName, "registrazione ottenuta");
		
		if(inEsecuzioneRegistrazioneMovFin()) {
			return INPUT;
		}
		
		sessionHandler.setParametro(FinSessionParameter.SUBIMPEGNO, null);
		
		SubImpegno subImpegno = (SubImpegno) model.getRegistrazioneMovFin().getMovimento();
		model.setSubImpegno(subImpegno);
		
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
		
		getDatiDaSubImpegno(subImpegno);
		ricavaSoggettoDaMovimento(model.getSubImpegno());
		return SUCCESS;
	}

	/**
	 * Ottiene i dati del subimpegno
	 * @param subImpegno il subimpegno da cui ottenere i dati
	 */
	private void getDatiDaSubImpegno(SubImpegno subImpegno){
		SubImpegno subImpegnoSessione = sessionHandler.getParametro(FinSessionParameter.SUBIMPEGNO);
		Impegno impegnoSessione = sessionHandler.getParametro(FinSessionParameter.IMPEGNO);
		if(subImpegnoSessione == null || subImpegnoSessione.getUid() != subImpegno.getUid()) {
			// Carico da servizio

			RicercaImpegnoPerChiaveOttimizzato request = model.creaRequestRicercaImpegnoPerChiaveOttimizzato(subImpegno);
			logServiceRequest(request);
			
			RicercaImpegnoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				return;
			}
			
			impegnoSessione = response.getImpegno();
			subImpegnoSessione = findSubImpegno(response.getImpegno(), subImpegno);
			sessionHandler.setParametro(FinSessionParameter.SUBIMPEGNO, subImpegnoSessione);
			sessionHandler.setParametro(FinSessionParameter.IMPEGNO, impegnoSessione);
		}
		
		model.setSubImpegno(subImpegnoSessione);
		model.setImpegno(impegnoSessione);
		model.setClassificatoreGerarchico(model.getImpegno().getCapitoloUscitaGestione().getMacroaggregato());
		
	}
	
	/**
	 * Trova il subImpegno dall'impegno restituito dal servizio di ricerca.
	 * 
	 * @param impegno    l'impegno del servizio di ricerca
	 * @param subImpegno il subimpegno con uid da trovare
	 * @return il subimpegno
	 */
	private SubImpegno findSubImpegno(Impegno impegno, SubImpegno subImpegno) {
		final String methodName = "findSubImpegno";
		if(impegno == null || impegno.getElencoSubImpegni() == null) {
			log.warn(methodName, "SubImpegno non reperibile dalla response per la registrazione " + model.getRegistrazioneMovFin().getUid());
			return null;
		}
		for(SubImpegno si : impegno.getElencoSubImpegni()) {
			if(si != null && si.getUid() == subImpegno.getUid()) {
				return si;
			}
		}
		log.warn(methodName, "Nessun subimpegno con uid " + subImpegno.getUid() + " reperibile nell'impegno con uid " + impegno.getUid()
				+ " ottenuto per la registrazione " + model.getRegistrazioneMovFin().getUid());
		return null;
	}

	/**
	 * Ottiene il soggetto dal movimento.
	 * 
	 * @param subImpegno il subimpegno da cui ottenere il soggetto
	 */
	private void ricavaSoggettoDaMovimento(SubImpegno subImpegno) {
		if (subImpegno != null) {
			model.setSoggettoMovimentoFIN(subImpegno.getSoggetto());
		}
	}
	
	@Override
	protected void computaStringheDescrizioneDaRegistrazione(RegistrazioneMovFin registrazioneMovFin){
		StringBuilder movimento = new StringBuilder();
		StringBuilder descrizione = new StringBuilder();
		SubImpegno subImpegno = (SubImpegno) registrazioneMovFin.getMovimento();
		StringBuilder titolo = new StringBuilder();
		
		titolo.append("Subimpegno")
		.append(computaStringaSubImp(subImpegno));
		
		descrizione.append("SubImp ")
			.append(computaStringaSubImp(subImpegno));
		if(StringUtils.isNotBlank(subImpegno.getDescrizione())) {
			descrizione.append(" ")
				.append(subImpegno.getDescrizione());
		}
		movimento.append(subImpegno.getAnnoMovimento())
			.append("/")
			.append(computaStringaSubImp(subImpegno))
			.append(" (subimp)");

		model.setDescrMovimentoFinanziario(movimento.toString());
		model.getPrimaNota().setDescrizione(descrizione.toString());
		model.getPrimaNota().setDataRegistrazione(new Date());
		model.setCampoTitoloPagina(titolo.toString());
	}
	
	/**
	 * Computa stringa sub imp.
	 *
	 * @param subImpegno the sub impegno
	 * @return the string
	 */
	private String computaStringaSubImp(SubImpegno subImpegno) {
		StringBuilder sub = new StringBuilder();
		sub.append(subImpegno.getAnnoImpegnoPadre())
		.append("/")
		.append(subImpegno.getNumeroImpegnoPadre().toPlainString())
		.append("-")
		.append(subImpegno.getNumero().toPlainString());
		return sub.toString();
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
		return model.getImpegno() != null
			&& model.getImpegno().getCapitoloUscitaGestione() != null
				? model.getImpegno().getCapitoloUscitaGestione().getElementoPianoDeiConti()
				: null;
	}
	
	@Override
	protected Soggetto ottieniSoggettoDaMovimento() {
		return model.getSubImpegno() != null ? model.getSubImpegno().getSoggetto() : null;
	}
}


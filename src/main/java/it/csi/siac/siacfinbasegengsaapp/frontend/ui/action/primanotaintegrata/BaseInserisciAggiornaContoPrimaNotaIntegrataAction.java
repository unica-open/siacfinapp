/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.action.primanotaintegrata;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.util.wrapper.ElementoScritturaPrimaNotaIntegrata;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.util.wrapper.ElementoScritturaPrimaNotaIntegrataFactory;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioContoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

/**
 * Classe base di action per l'inserimento e l'aggiornamento dell'elencoscritture della prima nota.
 *
 * @author Paggio Simona
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/04/2015
 * @version 1.1.0 - 12/10/2015 - gestione GEN/GSA
 * @param <E> la tipizzazione dell'entit&agrave;
 * @param <M> la tipizzazione del model
 */

public abstract class BaseInserisciAggiornaContoPrimaNotaIntegrataAction <E extends Entita, M extends BaseInserisciAggiornaPrimaNotaIntegrataBaseModel<E>>
		extends BaseInserisciAggiornaPrimaNotaIntegrataBaseAction<E, M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -3039295832522576872L;
	
	/** Serviz&icirc; del conto */
	@Autowired protected transient ContoService contoService;
	
	/**
	 * Ricalcolo dei totali
	 */
	protected void ricalcolaTotali (){
		BigDecimal totaleDare = BigDecimal.ZERO;
		BigDecimal totaleAvere = BigDecimal.ZERO;
		
		for (ElementoScritturaPrimaNotaIntegrata datoScrittura : model.getListaElementoScritturaPerElaborazione()) {
			if(datoScrittura.getMovimentoDettaglio().getImporto() != null) {
				if (OperazioneSegnoConto.AVERE.equals(datoScrittura.getMovimentoDettaglio().getSegno())){
					totaleAvere = totaleAvere.add(datoScrittura.getMovimentoDettaglio().getImporto());
				} else {
					totaleDare = totaleDare.add(datoScrittura.getMovimentoDettaglio().getImporto());
				}
			}
		}
		
		model.setTotaleAvere(totaleAvere);
		model.setTotaleDare(totaleDare);
		model.setDaRegistrare(totaleAvere.subtract(totaleDare));
	}
	
	/**
	 * Ottiene la lista dei conti.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaContiIniziale() {
		ricalcolaTotali();
		return SUCCESS;
	}
	
	/**
	 * Ottiene la lista dei conti da associare alla prima nota.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaConti() {
		getDatiCausaleDaLista();
		CausaleEP causaleEP = model.getCausaleEP();
		boolean contiCausaleNonValorizzati = causaleEP == null || causaleEP.getUid() == 0 || causaleEP.getContiTipoOperazione() == null || causaleEP.getContiTipoOperazione().isEmpty();
		if(contiCausaleNonValorizzati) {
			model.setListaElementoScrittura(new ArrayList<ElementoScritturaPrimaNotaIntegrata>());
			model.setListaElementoScritturaPerElaborazione(new ArrayList<ElementoScritturaPrimaNotaIntegrata>());
			model.setContiCausale(false);
			return SUCCESS;
		}
		
		model.setContiCausale(true);
		List<ElementoScritturaPrimaNotaIntegrata> listaElemScritturaPNI = ElementoScritturaPrimaNotaIntegrataFactory.creaListaScrittureDaCausaleEP(model.getCausaleEP(),
				getImportoMovimento());
		
		model.setListaElementoScrittura(listaElemScritturaPNI);

		List<ElementoScritturaPrimaNotaIntegrata> clone = deepClone(model.getListaElementoScrittura());
		model.setListaElementoScritturaPerElaborazione(clone);
		ricalcolaTotali();
		return SUCCESS;
	}
	
	/**
	 * Ottiene la lista dei conti da associare alla prima nota.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaContiDaSelezioneCausale() {
		return SUCCESS;
	}

	/**
	 * Ottiene i dati della causale dalla lista
	 */
	protected void getDatiCausaleDaLista() {
		if(model.getCausaleEP() == null) {
			return;
		}
		int uidCausaleEP = model.getCausaleEP().getUid();
		for (CausaleEP causaleEP : model.getListaCausaleEP()) {
			if (uidCausaleEP == causaleEP.getUid()) {
				CausaleEP clone = deepClone(causaleEP);
				model.setCausaleEP(clone);
				return;
			}
		}
	}
	
	/**
	 * Ottiene l'importo del movimento per la creazione delle scritture
	 * 
	 * @return l'importo dle movimento
	 */
	protected abstract BigDecimal getImportoMovimento();
	
	/**
	 * Preparazione per il metodo {@link #inserisciConto()}.
	 */
	public void prepareInserisciConto() {
		model.setConto(null);
		model.setImportoCollapse(null);
		model.setOperazioneSegnoContoCollapse(null);
	}
	
	/**
	 * Aggiunge un conto manualmente inserito
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciConto() {
		
		MovimentoDettaglio md = new MovimentoDettaglio();
		
		md.setConto(model.getConto());
		md.setImporto(model.getImportoCollapse());
		md.setSegno(model.getOperazioneSegnoContoCollapse());
		
		ContoTipoOperazione ctop = new ContoTipoOperazione();
		ctop.setOperazioneSegnoConto(md.getSegno());
		ElementoScritturaPrimaNotaIntegrata elementoScritturaPrimaNotaIntegrata = ElementoScritturaPrimaNotaIntegrataFactory.creaElementoScritturaManuale(ctop,md);
		
		model.getListaElementoScritturaPerElaborazione().add(elementoScritturaPrimaNotaIntegrata);
		
		// Ricalcolo dei totali
		ricalcolaTotali();
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #inserisciConto()}.
	 */
	public void validateInserisciConto() {
		checkNotNull(model.getImportoCollapse(), "Importo");
		// SIAC-5719: gli importi possono anche essere negativi, ma non pari a zero
		checkCondition(model.getImporto() == null || model.getImporto().signum() != 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": non puo' essere pari a zero"));
		checkNotNull(model.getOperazioneSegnoContoCollapse(), "Segno");
		checkCondition(model.getConto() != null && StringUtils.isNotBlank(model.getConto().getCodice()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Conto"), true);
		
		Conto conto = checkAndObtainContoFogliaEsistenteUnivoco();
		// Imposto il conto nel model
		model.setConto(conto);
	}

	/**
	 * Controlla che il conto esista e se esiste che ve ne sia solo uno e che sia un conto di tipo foglia.
	 */
	protected Conto checkAndObtainContoFogliaEsistenteUnivoco() {
		RicercaSinteticaContoResponse resp = ricercaSinteticaConto(model.getConto());
		
		checkCondition(!resp.getConti().isEmpty(), ErroreCore.ENTITA_NON_TROVATA.getErrore("Conto", model.getConto().getCodice()), true);
		checkCondition(resp.getConti().size() < 2, ErroreFin.OGGETTO_NON_UNIVOCO.getErrore("Conto"), true);
		
		Conto conto = resp.getConti().get(0);
		checkCondition(Boolean.TRUE.equals(conto.getContoFoglia()), ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto " + conto.getCodice(), "non e' un Conto foglia"), true);
		return conto;
	}	
	
	/**
	 * Preparazione per il metodo {@link #aggiornaConto()}.
	 */
	public void prepareAggiornaConto() {
		model.setImporto(null);
		model.setOperazioneSegnoConto(null);
		model.setIndiceConto(null);
	}
	
	
	/**
	 * Aggiorna la singolaRiga
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaConto() {
		int idx = model.getIndiceConto().intValue();
		
		//TODO eventuialmente spostare in factory
		ElementoScritturaPrimaNotaIntegrata elementoScrittura = model.getListaElementoScritturaPerElaborazione().get(idx);
		elementoScrittura.getMovimentoDettaglio().setImporto(model.getImporto());
		if (!model.isContiCausale()) {
			elementoScrittura.getMovimentoDettaglio().setSegno(model.getOperazioneSegnoConto());
			elementoScrittura.getContoTipoOperazione().setOperazioneSegnoConto(model.getOperazioneSegnoConto());
		}
		elementoScrittura.setAggiornamentoImportoManuale(true);

		model.getListaElementoScritturaPerElaborazione().set(idx, elementoScrittura);
		ricalcolaTotali();
		model.setIndiceConto(null);
		impostaInformazioneSuccesso();
		
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #aggiornaConto()}.
	 */
	public void prepareAggiornaContoDaClasseDiConciliazione() {
		model.setImporto(null);
		model.setContoDaSostituire(null);
	}
	
	/**
	 * Aggiornamento del conto dalla classe di conciliazione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaContoDaClasseDiConciliazione() { 
		int idx = model.getIndiceConto().intValue();
		impostaDatiConto();
		Conto contoDaImpostare = model.getContoDaSostituire();
		ElementoScritturaPrimaNotaIntegrata elementoScrittura = model.getListaElementoScritturaPerElaborazione().get(idx);
		elementoScrittura.getContoTipoOperazione().setConto(contoDaImpostare);
		elementoScrittura.getMovimentoDettaglio().setConto(contoDaImpostare);
		model.getListaElementoScritturaPerElaborazione().set(idx, elementoScrittura);
		ricalcolaTotali();
		impostaInformazioneSuccesso();
		model.setIndiceConto(null);
		return SUCCESS;
	}
	
	/**
	 * Aggiornamento del conto dalla classe di conciliazione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniContiDaClasseDiConciliazione() {
		int idx = model.getIndiceConto().intValue();
		ElementoScritturaPrimaNotaIntegrata elementoScrittura = model.getListaElementoScritturaPerElaborazione().get(idx);
		model.setListaContoDaClasseConciliazione(elementoScrittura.getContiSelezionabiliDaClasseDiConciliazione());
		model.setIndiceConto(null);
		return SUCCESS;
	}
	
	/**
	  * Validazione per il metodo {@link #aggiornaContoDaClasseDiConciliazioneConti()}.
	 */
	public void validateAggiornaContoDaClasseDiConciliazioneConti(){
		Conto conto = checkAndObtainContoFogliaEsistenteUnivoco();
		// Imposto il conto nel model
		model.setContoDaSostituire(conto);
	}
	
	/**
	 * Aggiornamento del conto dalla classe di conciliazione di tipo Conti
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaContoDaClasseDiConciliazioneConti() {
		return aggiornaContoDaClasseDiConciliazione();
	}
	
	
	
	private void impostaDatiConto() {
		if(model.getContoDaSostituire() != null && model.getContoDaSostituire().getUid() != 0){
			log.debug("impostaDatiConto", "ho il conto!");
			RicercaDettaglioConto req = model.creaRequestRicercaDettaglioConto(model.getContoDaSostituire());
			RicercaDettaglioContoResponse res = contoService.ricercaDettaglioConto(req);
			model.setContoDaSostituire(res.getConto());
		}else{
			log.debug("impostaDatiConto", "non ho il conto!");
			model.setContoDaSostituire(null);
		}
	}

	/**
	 * Validazione per il metodo {@link #aggiornaConto()}.
	 */
	public void validateAggiornaConto() {
		checkNotNull(model.getImporto(), "Importo");
		// SIAC-5719: gli importi possono anche essere negativi, ma non pari a zero
		checkCondition(model.getImporto() == null || model.getImporto().signum() != 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": non puo' essere pari a zero"));
		checkCondition(model.isContiCausale() || model.getOperazioneSegnoConto() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Segno"));
		checkNotNull(model.getIndiceConto(), "Indice");
		
		int idx = model.getIndiceConto().intValue();
		int size = model.getListaElementoScritturaPerElaborazione().size();
		checkCondition(idx >= 0 && idx < size, ErroreCore.FORMATO_NON_VALIDO.getErrore("Indice", "deve essere compreso tra 0 e " + size));
		
		ElementoScritturaPrimaNotaIntegrata elementoScrittura = model.getListaElementoScritturaPerElaborazione().get(idx);
		checkNotNull(elementoScrittura, "Scrittura", true);
		checkCondition(!elementoScrittura.isUtilizzoImportoNonModificabile(), ErroreGEN.MOVIMENTO_CONTABILE_NON_MODIFICABILE.getErrore("l'utilizzo dell'importo e' non modificabile"));
	}
	
	/**
	 * Preparazione per il metodo {@link #eliminaConto()}.
	 */
	public void prepareEliminaConto() {
		model.setIndiceConto(null);
	}
	
	/**
	 * validate per Elimina la singolaRiga
	 */
	public void validateEliminaConto() {
		checkCondition(!model.isContiCausale(), ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il conto deriva da una causale"), true);
		checkNotNull(model.getIndiceConto(), "Indice");
		
		int idx = model.getIndiceConto().intValue();
		int size = model.getListaElementoScritturaPerElaborazione().size();
		checkCondition(idx >= 0 && idx < size, ErroreCore.FORMATO_NON_VALIDO.getErrore("Indice", "deve essere compreso tra 0 e " + size));
		
		ElementoScritturaPrimaNotaIntegrata elementoScrittura = model.getListaElementoScritturaPerElaborazione().get(idx);
		checkNotNull(elementoScrittura, "Scrittura", true);
		checkCondition(!elementoScrittura.isUtilizzoContoObbligatorio(), ErroreGEN.MOVIMENTO_CONTABILE_NON_ELIMINABILE.getErrore("l'utilizzo del conto e' obbligatorio"));
	
	}
	/**
	 * Elimina la singolaRiga
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String eliminaConto() {
		int idx = model.getIndiceConto().intValue();
		model.getListaElementoScritturaPerElaborazione().remove(idx);
		
		ricalcolaTotali();
		model.setIndiceConto(null);
		impostaInformazioneSuccesso();
		return SUCCESS;
	}

	
	/**
	 * Validate per aggiornamento importo proposto
	 * 
	 */
	public void validateImpostaImportoDaRegistrare() {
		checkCondition(model.isContiCausale(), ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("I conti non sono associati a una causale"), true);
	}

	/**
	 * Ricerca sintetica del conto.
	 * 
	 * @param conto il conto da cercare
	 * 
	 * @return la response del servizio
	 */
	private RicercaSinteticaContoResponse ricercaSinteticaConto(Conto conto) {
		RicercaSinteticaConto request = model.creaRequestRicercaSinteticaConto(conto);
		logServiceRequest(request);
	
		RicercaSinteticaContoResponse response = contoService.ricercaSinteticaConto(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			// Se ho errori esco
			addErrori(response);
			throw new ParamValidationException(createErrorInServiceInvocationString(RicercaSinteticaConto.class, response));
		}
		
		return response;
	}
}

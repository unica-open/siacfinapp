/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.util.CruscottoRorUtilities;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoEntrataResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaAccertamentiSubAccertamentiResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione.StatoOperativoModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ElencoAccertamentiAction extends WizardRicercaMovGestAction {

	private static final long serialVersionUID = -6262925228646115712L;

	protected final static int CONSULTA = 1;
	protected final static int AGGIORNA = 2;
	protected final static int ANNULLA = 3;
	protected final static int SOSPENDI = 4;
	protected final static int BLOCCA = 5;
	protected final static int CANCELLA = 6;
	protected final static int VALIDA = 7;
	protected final static int COLLEGA = 8;

	private String uidAccertamento;
	private String numeroAccertamento;
	private String numeroAccertamentoDaPassare;
	private String annoAccertamentoAccertamentoDaPassare;

	public boolean ricaricaPagina;
	public boolean status;

	/**
	 * prepare della action
	 */
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		// invoco il prepare della super classe:
		super.prepare();

		// setto il titolo:
		this.model.setTitolo("Elenco Accertamenti");
	}

	/**
	 * execute della action
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		if (ricaricaPagina) {
			addMessaggio(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore(""));
			return reload();
		}

		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_ACCERTAMENTI, new ArrayList<Accertamento>());
		model.setResultSize(0);

		// SIAC-6992
		if (isRicercaAccertamentiROR()) {
			executeRicercaAccertamentiROR();
			setStatus(true);
			return GOTO_ELENCO_ACCERTAMENTI_ROR;
		} else {
			// effettuiamo la ricerca:
			executeRicercaAccertamenti();
			setStatus(true);
			return SUCCESS;
		}

	}

	/**
	 * Per stabilire l'abilitazione ai pulsanti nella colonna azioni della
	 * datatable dei risultati
	 * 
	 * @param decodifica
	 * @param accUid
	 * @return
	 */
	public boolean isAbilitato(Integer decodifica, Integer accUid) {

		boolean abilitato = false;

		List<Accertamento> list = sessionHandler.getParametro(FinSessionParameter.RISULTATI_RICERCA_ACCERTAMENTI);

		// cerco l'accertamento della riga in questione
		// tra quelli nella lista che rappresenta la pagina visualizzata:
		Accertamento accertamento = FinUtility.getById(list, accUid);

		if (accertamento != null && accertamento.getUid() != 0) {
			// accertamento trovato

			switch (decodifica) {
			case CONSULTA:
				abilitato = true;
				break;

			case AGGIORNA:
				if (isFaseBilancioAbilitata()) {
					if (accertamento.getDescrizioneStatoOperativoMovimentoGestioneEntrata()
							.equalsIgnoreCase(CostantiFin.STATO_ANNULLATO)) {
						abilitato = false;
					} else {
						abilitato = true;
					}
				}
				break;

			case ANNULLA:
				if (isFaseBilancioAbilitata()) {
					if (accertamento.getDescrizioneStatoOperativoMovimentoGestioneEntrata()
							.equalsIgnoreCase(CostantiFin.STATO_PROVVISORIO)) {
						abilitato = true;
					}
				}
				break;
			}
		}

		return abilitato;
	}

	/**
	 * funzione di annulla accertamento e subito dopo lancio della ricerca per
	 * ottenere la lista aggiornata
	 * 
	 * @return
	 * @throws Exception
	 */
	public String annullaAccertamento() throws Exception {

		// info per i log:
		setMethodName("annullaImpegno");
		debug(methodName, "entro in annulla impegno " + getNumeroAccertamentoDaPassare() + " - "
				+ getAnnoAccertamentoAccertamentoDaPassare());

		// Carico i dati completi dell'impegno in questione.

		// Compongo la requet:
		RicercaAccertamentoPerChiaveOttimizzato requestRicerca = convertiModelPerChiamataServizioRicercaPerChiave();

		// Richiamo il servizio di ricerca per chiave:
		RicercaAccertamentoPerChiaveOttimizzatoResponse responseRicercaK = movimentoGestioneFinService
				.ricercaAccertamentoPerChiaveOttimizzato(requestRicerca);

		// verifico che ci siano dei sbuimpegni
		if (!responseRicercaK.isFallimento()) {
			if (responseRicercaK.getAccertamento() != null) {
				if (responseRicercaK.getAccertamento().getElencoSubAccertamenti() != null
						&& responseRicercaK.getAccertamento().getElencoSubAccertamenti().size() > 0) {

					// i subimpegni devono essere <> A e <> P
					for (int i = 0; i < responseRicercaK.getAccertamento().getElencoSubAccertamenti().size(); i++) {

						if (responseRicercaK.getAccertamento().getElencoSubAccertamenti().get(i)
								.getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase("D")
								|| responseRicercaK.getAccertamento().getElencoSubAccertamenti().get(i)
										.getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase("N")) {
							List<Errore> errori = new ArrayList<Errore>();

							errori.add(ErroreFin.PRESENZA_SUBIMPEGNI.getErrore(""));
							addErrori(errori);

							return INPUT;

						}
					}
				}
			}
		}

		// eseguo annullamento Accertamento

		AnnullaMovimentoEntrata request = new AnnullaMovimentoEntrata();
		request.setEnte(sessionHandler.getEnte());
		request.setRichiedente(sessionHandler.getRichiedente());

		request.setBilancio(creaOggettoBilancio());
		Accertamento accertamento = new Accertamento();
		accertamento.setUid(Integer.valueOf(uidAccertamento));
		request.setAccertamento(accertamento);
		// chiamo il servizio di annullamento
		AnnullaMovimentoEntrataResponse response = movimentoGestioneFinService.annullaMovimentoEntrata(request);

		if (response.isFallimento()) {
			debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return INPUT;
		}

		// rifaccio la ricerca e ricarico la lista impegni aggiornata
		RicercaSinteticaAccertamentiSubAccertamentiResponse responseRicerca = movimentoGestioneFinService
				.ricercaSinteticaAccertamentiSubAccertamenti(convertiModelPerChiamataServizioRicercaAccertamentiSub());

		List<Accertamento> listaCompleta = new ArrayList<Accertamento>();

		for (Accertamento app : responseRicerca.getListaAccertamenti()) {

			// Setto il capitolo
			if (app.getCapitoloEntrataGestione() != null) {
				if (app.getCapitoloEntrataGestione().getUid() != 0) {
					app.setCapitoloEntrataGestione(ricercaCapitoloEntrata(app.getCapitoloEntrataGestione().getUid()));
				}
			}

			// Setto il provvedimento
			if (app.getAttoAmministrativo() != null) {
				if (app.getAttoAmministrativo().getUid() != 0) {
					app.setAttoAmministrativo(ricercaAttoAmministrativo(app.getAttoAmministrativo().getUid()));
				}
			}

			listaCompleta.add(app);
		}

		// Metto in sessione la lista ricevuta per utilizzarla in Elenco
		// Soggetti
		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_ACCERTAMENTI, listaCompleta);

		if (!response.isFallimento()) {
			// TUTTO OK
			addActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " "
					+ ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		}

		setStatus(false);

		return SUCCESS;
	}

	/**
	 * Compone la request per il servizio di ricerca
	 * 
	 * @return
	 */
	protected RicercaAccertamentoPerChiaveOttimizzato convertiModelPerChiamataServizioRicercaPerChiave() {
		// istanzio valori dell'impegno da cercare (valori da passare dalla
		// ricerca)
		RicercaAccertamentoPerChiaveOttimizzato parametroRicercaPerChiave = new RicercaAccertamentoPerChiaveOttimizzato();
		RicercaAccertamentoK impegnoDaCercare = new RicercaAccertamentoK();

		// leggo l'ente:
		Ente ente = model.getEnte();

		// Anno esercizio:
		impegnoDaCercare.setAnnoEsercizio(sessionHandler.getAnnoBilancio());

		// Numero Movimento:
		impegnoDaCercare.setNumeroAccertamento(new BigDecimal(numeroAccertamentoDaPassare));

		// Anno movimento:
		impegnoDaCercare.setAnnoAccertamento(Integer.valueOf(annoAccertamentoAccertamentoDaPassare));

		parametroRicercaPerChiave.setRichiedente(sessionHandler.getRichiedente());
		parametroRicercaPerChiave.setEnte(ente);
		parametroRicercaPerChiave.setpRicercaAccertamentoK(impegnoDaCercare);

		return parametroRicercaPerChiave;
	}

	public String reload() {

		// Info per loggare:
		setMethodName("reload");
		debug(methodName, "entro nel reload ");

		// Compongo la request per ricaricare gli accertamenti:
		RicercaAccertamenti requestRicercaAccertamenti = convertiModelPerChiamataServizioRicercaAccertamenti();

		// Richiamo il servizio di ricerca accertamenti:
		RicercaAccertamentiResponse response = movimentoGestioneFinService.ricercaAccertamenti(requestRicercaAccertamenti);

		// Controllo che il servizio non restituisca errori
		if (response.isFallimento()) {
			addErrori(methodName, response);
			return INPUT;
		}
		// Metto in sessione la lista ricevuta per utilizzarla in Elenco
		// Soggetti
		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_ACCERTAMENTI, response.getAccertamenti());

		return SUCCESS;
	}

	/**
	 * Per stabilire l'abilitazione ad aggiornare
	 * 
	 * @param accUid
	 * @return
	 */
	public boolean isAbilitatoAggiornaAccertamento(Integer accUid) {
		// SIAC-5288 aggiornamento GSA:
		if (isAbilitatoAggiornaAccertamentoGSA()) {
			// l'abilitazione a OP-ENT-aggiornaAccertamentoGsa
			// ci rende automaticamente abilitati ad entrare in aggiorna
			return true;
		}
		// end SIAC-5288

		// ci riconduciamo alle logiche in comune tra aggiorna e annulla:
		return isAbilitatoAggiornaAnnullaAccertamento(accUid);
	}

	/**
	 * Per stabilire l'abilitazione ad annullare
	 * 
	 * @param accUid
	 * @return
	 */
	public boolean isAbilitatoAnnullaAccertamento(Integer accUid) {
		// ci riconduciamo alle logiche in comune tra aggiorna e annulla:
		return isAbilitatoAggiornaAnnullaAccertamento(accUid);
	}

	/**
	 * logiche in comune tra aggiorna ed annulla
	 * 
	 * @param accUid
	 * @return
	 */
	private boolean isAbilitatoAggiornaAnnullaAccertamento(Integer accUid) {

		List<Accertamento> list = sessionHandler.getParametro(FinSessionParameter.RISULTATI_RICERCA_ACCERTAMENTI);

		// cerco l'accertamento della riga in questione
		// tra quelli nella lista che rappresenta la pagina visualizzata:
		Accertamento accertamento = FinUtility.getById(list, accUid);

		// metodo core:
		return isAbilitatoAggiornaAnnullaAccertamento(accertamento);

	}

	public boolean isAbilitatoGestisciAccertamento(Integer impUid) {

		List<SubAccertamento> list = sessionHandler.getParametro(FinSessionParameter.RISULTATI_RICERCA_ACCERTAMENTI);
		int numeroInsussistenze = 0;
		int numeroInesigibilita = 0;
		int numeroMantenimenti = 0;

		// cerco l'impegno della riga in questione
		// tra quelli nella lista che rappresenta la pagina visualizzata:
		SubAccertamento accertamento  = FinUtility.getById(list, impUid);
		// List<ModificaMovimentoGestioneSpesa> listaModificheImpegno =
		// impegno.getListaModificheMovimentoGestioneSpesa();
		
		

		boolean abilita = true;
		if(accertamento.getStatoOperativoMovimentoGestioneEntrata().equals("A") || accertamento.getStatoOperativoMovimentoGestioneEntrata().equals("P")){
			abilita=false;
			return abilita;
		}

		if (accertamento.getListaModificheMovimentoGestioneEntrata() != null
				&& !accertamento.getListaModificheMovimentoGestioneEntrata().isEmpty()) {

			List<Integer> anniDiReimputazione = getAnniReimputazionePresentiAcc(
					accertamento.getListaModificheMovimentoGestioneEntrata());
			boolean duplicatiInLista = cercaDuplicatiAnniReimputazione(anniDiReimputazione);
			List<ModificaMovimentoGestioneEntrata> listaModifiche = accertamento.getListaModificheMovimentoGestioneEntrata();
			List<ModificaMovimentoGestioneEntrata> modifiche = new ArrayList<ModificaMovimentoGestioneEntrata>();
			modifiche = CruscottoRorUtilities.getModifichePerCruscottoAcc(listaModifiche, modifiche);
			modifiche = CruscottoRorUtilities.getModificheStatoValidoAccControllo(modifiche); 
			for (ModificaMovimentoGestioneEntrata mmgs : modifiche) {
				//SIAC-8673
				if(mmgs.isElaboraRorReanno()) {
					return false;
				}

				if (mmgs.getTipoModificaMovimentoGestione().equals("INEROR")) {
					numeroInesigibilita++;
				}
				if (mmgs.getTipoModificaMovimentoGestione().equals("INSROR")) {
					numeroInsussistenze++;
				}
				if (mmgs.getTipoModificaMovimentoGestione().equals("RORM")) {
					numeroMantenimenti++;
				}

			}
			
			if(duplicatiInLista == true || numeroInesigibilita >1 || numeroInsussistenze> 1 || numeroMantenimenti > 1){
				abilita=false;
				return abilita;
			}else{
				abilita=true;
			}
			
			
			
						
			boolean s = false;
			if (modifiche.size() > 1) {
				for (int i = 0; i < modifiche.size(); i++) {
					for (int j = 1; j < modifiche.size(); j++) {
						s = CruscottoRorUtilities.stessoProvvedimentoCruscotto(
								modifiche.get(i).getAttoAmministrativo(),
								modifiche.get(j).getAttoAmministrativo());
						if(s==false){
							abilita=false;
							return abilita;
						}else{
							abilita=true;
						}
					}

				}

			}

		}

		if (abilita) {
			// CONTROLLI SU
			/*
			 * importo impegno >0 E IMPORTO RESIDUO AL 31/12/N-1 > 0 E Deve
			 * essere vera una delle due condizioni seguenti • OP-SPE-gestImpROR
			 * • OP-SPE-gestImpRORdecentrato e sono rispettate le regole di
			 * visibilità definite a par. 2.7.1 E stato impegno = D || stato
			 * impegno = N
			 * 
			 */
			// SE TESTATA O SUB
			if (accertamento.getNumeroBigDecimal() == null) {
				// TESTATA
				// SIAC-8607
				if (accertamento.getImportoDaRiaccertare() != null
						&& accertamento.getImportoDaRiaccertare().compareTo(BigDecimal.ZERO) > 0
						&& CostantiFin.MOVGEST_STATO_DEFINITIVO.equals(accertamento.getStatoOperativoMovimentoGestioneEntrata())
						|| CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equals(accertamento.getStatoOperativoMovimentoGestioneEntrata())) {

					// CONTROLLO SAC
					/* SE AMMINISTRATORE VIENE SALTATO IL CONTROLLO SULLA SAC
					 * AMMINISTRATORE SE HA L'AZIONE  OP-SPE-gestImpROR
					 * 
					 */
					setAzioniDecToCheck(AzioneConsentitaEnum.OP_ENT_gestAccROR.getNomeAzione()); 
					if(checkAzioniDec()){  
						//CONTROLLO SOLO SU ESISTENZA SAC
						if(accertamento.getStrutturaCompetenteObj()==null || accertamento.getStrutturaCompetenteObj().getUid()==0){
							abilita = false;
						}
						
					}else{
						abilita = checkSacUtenteValida(accertamento.getStrutturaCompetenteObj());
					}
					
				} else {
					abilita = false;
					return abilita;
				}

			} else {
				// SUB
				// SIAC-8607
				if (accertamento.getImportoDaRiaccertare() != null
						&& accertamento.getImportoDaRiaccertare().compareTo(BigDecimal.ZERO) > 0
						&& CostantiFin.MOVGEST_STATO_DEFINITIVO.equals(accertamento.getStatoOperativoMovimentoGestioneEntrata())
						|| CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equals(accertamento.getStatoOperativoMovimentoGestioneEntrata())) {

					
					// CONTROLLO SAC
					/* SE AMMINISTRATORE VIENE SALTATO IL CONTROLLO SULLA SAC
					 * AMMINISTRATORE SE HA L'AZIONE  OP-SPE-gestImpROR
					 * 
					 */
					setAzioniDecToCheck(AzioneConsentitaEnum.OP_ENT_gestAccROR.getNomeAzione()); 
					if(checkAzioniDec()){  
						//CONTROLLO SOLO SU ESISTENZA SAC
						if(accertamento.getStrutturaCompetenteObj()==null || accertamento.getStrutturaCompetenteObj().getUid()==0){
							abilita = false;
						}
						
					}else{
						abilita = checkSacUtenteValida(accertamento.getStrutturaCompetenteObj());
					}

				} else {
					abilita = false;
				}
			}
		}

		return abilita;
	}

	
	
	private List<Integer> getAnniReimputazionePresentiAcc(List<ModificaMovimentoGestioneEntrata> listaModificheImpegno) {

		List<Integer> anniDiReimputazione = new ArrayList<Integer>();
		for (ModificaMovimentoGestioneEntrata mmgs : listaModificheImpegno) {
			//SIAC-8052
			if (mmgs.getStatoOperativoModificaMovimentoGestione()!= null && !mmgs.getStatoOperativoModificaMovimentoGestione().equals(StatoOperativoModificaMovimentoGestione.ANNULLATO) && mmgs.isReimputazione() && mmgs.getTipoModificaMovimentoGestione().equals("REIMP")) {
				anniDiReimputazione.add(mmgs.getAnnoReimputazione());
			}
		}
		return anniDiReimputazione;

	}

	// Cerca anni duplicati
	private boolean cercaDuplicatiAnniReimputazione(List<Integer> anniDiReimputazione) {
		if (anniDiReimputazione.size() > 0) {
			Set<Integer> set = new HashSet<Integer>(anniDiReimputazione);
			if (set.size() < anniDiReimputazione.size()) {
				return true;
			}
		}
		return false;
	}

	// GETTER E SETTER:

	public String inserisciAccertamenti() {
		return "gotoInserisciAccertamenti";
	}

	public String getUidAccertamento() {
		return uidAccertamento;
	}

	public void setUidAccertamento(String uidAccertamento) {
		this.uidAccertamento = uidAccertamento;
	}

	public String getNumeroAccertamento() {
		return numeroAccertamento;
	}

	public void setNumeroAccertamento(String numeroAccertamento) {
		this.numeroAccertamento = numeroAccertamento;
	}

	public String getNumeroAccertamentoDaPassare() {
		return numeroAccertamentoDaPassare;
	}

	public void setNumeroAccertamentoDaPassare(String numeroAccertamentoDaPassare) {
		this.numeroAccertamentoDaPassare = numeroAccertamentoDaPassare;
	}

	public String getAnnoAccertamentoAccertamentoDaPassare() {
		return annoAccertamentoAccertamentoDaPassare;
	}

	public void setAnnoAccertamentoAccertamentoDaPassare(String annoAccertamentoAccertamentoDaPassare) {
		this.annoAccertamentoAccertamentoDaPassare = annoAccertamentoAccertamentoDaPassare;
	}

	public boolean isRicaricaPagina() {
		return ricaricaPagina;
	}

	public void setRicaricaPagina(boolean ricaricaPagina) {
		this.ricaricaPagina = ricaricaPagina;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
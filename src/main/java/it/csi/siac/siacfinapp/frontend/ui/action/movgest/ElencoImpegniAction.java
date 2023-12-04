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
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniGlobal;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniGlobalResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione.StatoOperativoModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ElencoImpegniAction extends WizardRicercaMovGestAction {

	private static final long serialVersionUID = 1L;
	protected final static int CONSULTA = 1;
	protected final static int AGGIORNA = 2;
	protected final static int ANNULLA = 3;
	protected final static int SOSPENDI = 4;
	protected final static int BLOCCA = 5;
	protected final static int CANCELLA = 6;
	protected final static int VALIDA = 7;
	protected final static int COLLEGA = 8;
	protected final static int AGGIORNA_DECENTRATO = 9;
	protected final static int GESTIONE_CRUSCOTTO = 10;

	private String uidImpegno;
	private String numeroImpegno;
	private String numeroImpegnoDaPassare;
	private String annoImpegnoImpegnoDaPassare;

	public boolean ricaricaPagina;
	public boolean status;

	/*
	 * // SIAC-7042 private String cronoprogramma; private Integer
	 * idCronoprogramma; private Integer idSpesaCronoprogramma;
	 */

	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		// invoco il prepare della super classe:
		super.prepare();
		//SIAC-7772
		sessionHandler.setParametro(FinSessionParameter.ERRORI_AZIONE_PRECEDENTE, null);
		// setto il titolo:
		this.model.setTitolo("Elenco Impegni");

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

		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_IMPEGNI, new ArrayList<Impegno>());
		model.setResultSize(0);

		// SIAC-6992
		if (isRicercaImpegniROR()) {
			executeRicercaImpegniROR();
			setStatus(true);
			return GOTO_ELENCO_IMPEGNI_ROR;
		} else {
			// effettuiamo la ricerca:
			executeRicercaImpegni();
			setStatus(true);
			return SUCCESS;
		}

	}

	public String reload() {

		// Info per loggare:
		setMethodName("reload");
		debug(methodName, "entro nel reload ");

		// Compongo la request per ricaricare gli impegni:
		RicercaImpegniGlobal requestRicercaImpegni = convertiModelPerChiamataServizioRicercaImpegni();

		// Richiamo il servizio di ricerca impegni:
		RicercaImpegniGlobalResponse response = movimentoGestioneFinService.ricercaImpegni(requestRicercaImpegni);

		// Controllo che il servizio non restituisca errori
		if (response.isFallimento()) {
			addErrori(methodName, response);
			return INPUT;
		}
		// Metto in sessione la lista ricevuta per utilizzarla in Elenco
		// Soggetti
		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_IMPEGNI, response.getImpegniList());

		return SUCCESS;

	}

	/**
	 * convertitore per la chiamata al servizio di ricerca per chiave
	 * 
	 * @return
	 */
	protected RicercaImpegnoPerChiaveOttimizzato convertiModelPerChiamataServizioRicercaPerChiave() {
		// istanzio valori dell'impegno da cercare (valori da passare dalla
		// ricerca)
		RicercaImpegnoPerChiaveOttimizzato parametroRicercaPerChiave = new RicercaImpegnoPerChiaveOttimizzato();
		RicercaImpegnoK impegnoDaCercare = new RicercaImpegnoK();

		// leggo l'ente:
		Ente ente = model.getEnte();

		// Anno esercizio:
		impegnoDaCercare.setAnnoEsercizio(sessionHandler.getAnnoBilancio());

		// Numero Movimento:
		impegnoDaCercare.setNumeroImpegno(new BigDecimal(numeroImpegnoDaPassare));

		// Anno movimento:
		impegnoDaCercare.setAnnoImpegno(Integer.valueOf(annoImpegnoImpegnoDaPassare));

		parametroRicercaPerChiave.setRichiedente(sessionHandler.getRichiedente());
		parametroRicercaPerChiave.setEnte(ente);
		parametroRicercaPerChiave.setpRicercaImpegnoK(impegnoDaCercare);

		// OTTIMIZZAZIONE RICERCA:
		parametroRicercaPerChiave.setCaricaSub(false);

		return parametroRicercaPerChiave;
	}

	/**
	 * funzione di annulla impegno e subito dopo lancio della ricerca per
	 * ottenere la lista aggiornata
	 * 
	 * @return
	 * @throws Exception
	 */
	public String annullaImpegno() throws Exception {

		// info per i log:
		setMethodName("annullaImpegno");
		debug(methodName, "entro in annulla impegno ");

		// Carico i dati completi dell'impegno in questione.

		// Compongo la requet:
		RicercaImpegnoPerChiaveOttimizzato requestRicerca = convertiModelPerChiamataServizioRicercaPerChiave();

		// Richiamo il servizio di ricerca per chiave:
		RicercaImpegnoPerChiaveOttimizzatoResponse responseRicercaK = movimentoGestioneFinService
				.ricercaImpegnoPerChiaveOttimizzato(requestRicerca);

		// verifico che ci siano dei sbuimpegni
		if (!responseRicercaK.isFallimento()) {
			if (responseRicercaK.getImpegno() != null) {

				List<SubImpegno> elencoSubImpegni = responseRicercaK.getElencoSubImpegniTuttiConSoloGliIds();

				if (elencoSubImpegni != null && elencoSubImpegni.size() > 0) {

					// i subimpegni devono essere <> A e <> P
					for (int i = 0; i < elencoSubImpegni.size(); i++) {

						if (elencoSubImpegni.get(i).getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("D")
								|| elencoSubImpegni.get(i).getStatoOperativoMovimentoGestioneSpesa()
										.equalsIgnoreCase("N")) {
							List<Errore> errori = new ArrayList<Errore>();

							errori.add(ErroreFin.PRESENZA_SUBIMPEGNI.getErrore(""));
							addErrori(errori);

							return INPUT;

						}
					}
				}
			}
		}

		// eseguo annullamento impegno
		AnnullaMovimentoSpesa request = new AnnullaMovimentoSpesa();
		request.setEnte(sessionHandler.getEnte());
		request.setRichiedente(sessionHandler.getRichiedente());

		request.setBilancio(creaOggettoBilancio());

		Impegno impegno = new Impegno();
		impegno.setUid(Integer.valueOf(uidImpegno));

		request.setImpegno(impegno);
		// chiamo il servizio di annullamento
		AnnullaMovimentoSpesaResponse response = movimentoGestioneFinService.annullaMovimentoSpesa(request);

		if (response.isFallimento()) {
			debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return INPUT;
		}

		// rifaccio la ricerca e ricarico la lista impegni aggiornata
		RicercaImpegniGlobalResponse responseRicerca = movimentoGestioneFinService
				.ricercaImpegni(convertiModelPerChiamataServizioRicercaImpegni());

		List<Impegno> listaCompleta = new ArrayList<Impegno>();

		if (responseRicerca.getImpegniList() != null && responseRicerca.getImpegniList().size() > 0) {
			for (Impegno app : responseRicerca.getImpegniList()) {

				// Setto il capitolo
				if (app.getCapitoloUscitaGestione() != null) {
					if (app.getCapitoloUscitaGestione().getUid() != 0) {
						app.setCapitoloUscitaGestione(ricercaCapitoloUscita(app.getCapitoloUscitaGestione().getUid()));
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
		}

		// Metto in sessione la lista ricevuta per utilizzarla in Elenco
		// Soggetti
		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_IMPEGNI, listaCompleta);

		if (!response.isFallimento()) {
			// TUTTO OK
			addActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " "
					+ ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		}

		setStatus(false);

		return SUCCESS;
	}

	/**
	 * funzione utilizzato su jsp per abilitazione o meno di funzionalita
	 * 
	 * @param decodifica
	 * @param impUid
	 * @return
	 */
	public boolean isAbilitato(Integer decodifica, Integer impUid) {

		boolean abilitato = false;

		List<Impegno> list = sessionHandler.getParametro(FinSessionParameter.RISULTATI_RICERCA_IMPEGNI);

		// cerco l'impegno della riga in questione
		// tra quelli nella lista che rappresenta la pagina visualizzata:
		Impegno impegno = FinUtility.getById(list, impUid);

		if (impegno != null && impegno.getUid() != 0) {
			// impegno trovato

			switch (decodifica) {
			case CONSULTA:
				abilitato = true;
				break;

			case AGGIORNA:
				if (isFaseBilancioAbilitata()) {
					if (impegno.getDescrizioneStatoOperativoMovimentoGestioneSpesa()
							.equalsIgnoreCase(CostantiFin.STATO_ANNULLATO)) {
						abilitato = false;
					} else {
						abilitato = true;
					}
				}
				break;

			case ANNULLA:
				if (isFaseBilancioAbilitata()) {
					// lo stato deve avere questi valori e poi verifico sullo
					// stato del impegno E G A O
					if (impegno.getDescrizioneStatoOperativoMovimentoGestioneSpesa()
							.equalsIgnoreCase(CostantiFin.STATO_PROVVISORIO)) {
						abilitato = true;
					}
				}
				break;

			case AGGIORNA_DECENTRATO:
				if (isBilancioAttualeInPredisposizioneConsuntivo()) {
					if (!impegno.getDescrizioneStatoOperativoMovimentoGestioneSpesa()
							.equalsIgnoreCase(CostantiFin.STATO_ANNULLATO)) {
						abilitato = true;
					} else
						abilitato = false;
				}
				break;
			}

		}

		return abilitato;
	}

	/**
	 * funzione utilizzato su jsp per abilitazione o meno della funzionalita (e
	 * quindi del tasto) di aggiorna impegno
	 * 
	 * @param impUid
	 * @return
	 */
	public boolean isAbilitatoAggiornaImpegno(Integer impUid) {

		// SIAC-5288 aggiornamento GSA:
		if (isAbilitatoAggiornaImpegnoGSA()) {
			// l'abilitazione a OP-SPE-aggiornaImpegnoGsa
			// ci rende automaticamente abilitati ad entrare in aggiorna
			return true;
		}
		// end SIAC-5288

		// ci riconduciamo alle logiche in comune tra aggiorna e annulla:
		return isAbilitatoAggiornaAnnullaImpegno(impUid);
	}

	/**
	 * * funzione utilizzato su jsp per abilitazione o meno della funzionalita
	 * (e quindi del tasto) di annulla impegno
	 * 
	 * @param impUid
	 * @return
	 */
	public boolean isAbilitatoAnnullaImpegno(Integer impUid) {
		// ci riconduciamo alle logiche in comune tra aggiorna e annulla:
		return isAbilitatoAggiornaAnnullaImpegno(impUid);
	}

	/**
	 * logiche in comune all'abilitazione aggiorna e annulla di un impegno
	 * 
	 * @param impUid
	 * @return
	 */
	private boolean isAbilitatoAggiornaAnnullaImpegno(Integer impUid) {

		List<Impegno> list = sessionHandler.getParametro(FinSessionParameter.RISULTATI_RICERCA_IMPEGNI);

		// cerco l'impegno della riga in questione
		// tra quelli nella lista che rappresenta la pagina visualizzata:
		Impegno impegno = FinUtility.getById(list, impUid);

		// metodo core:
		return isAbilitatoAggiornaAnnullaImpegno(impegno);
	}
	// SIAC-6997 Metodi per gestione button Gestisci
	/* Ottieni anni di reimputazione presenti nelle liste */

	private List<Integer> getAnniReimputazionePresenti(List<ModificaMovimentoGestioneSpesa> listaModificheImpegno) {

		List<Integer> anniDiReimputazione = new ArrayList<Integer>();
		for (ModificaMovimentoGestioneSpesa mmgs : listaModificheImpegno) {
			if (!mmgs.getStatoOperativoModificaMovimentoGestione().equals(StatoOperativoModificaMovimentoGestione.ANNULLATO) && mmgs.isReimputazione() && mmgs.getTipoModificaMovimentoGestione().equals("REIMP")) {
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

	/**
	 * logiche in comune all'abilitazione del cruscotto
	 * 
	 * @param impUid
	 * @return
	 */
	public boolean isAbilitatoGestisciCruscottoImpegno(Integer impUid) {

		List<SubImpegno> list = sessionHandler.getParametro(FinSessionParameter.RISULTATI_RICERCA_IMPEGNI);
		int numeroInsussistenze = 0;
		int numeroInesigibilita = 0;
		int numeroMantenimenti = 0;

		// cerco l'impegno della riga in questione
		// tra quelli nella lista che rappresenta la pagina visualizzata:
		SubImpegno impegno = FinUtility.getById(list, impUid);
		// List<ModificaMovimentoGestioneSpesa> listaModificheImpegno =
		// impegno.getListaModificheMovimentoGestioneSpesa();
		boolean abilita = true;
		if (impegno.getStatoOperativoMovimentoGestioneSpesa().equals("A")
				|| impegno.getStatoOperativoMovimentoGestioneSpesa().equals("P")) {
			abilita = false;
			return abilita;
		}

		if (impegno.getListaModificheMovimentoGestioneSpesa() != null
				&& !impegno.getListaModificheMovimentoGestioneSpesa().isEmpty()) {

			List<Integer> anniDiReimputazione = getAnniReimputazionePresenti(
					impegno.getListaModificheMovimentoGestioneSpesa());
			boolean duplicatiInLista = cercaDuplicatiAnniReimputazione(anniDiReimputazione);
			List<ModificaMovimentoGestioneSpesa> listaModifiche = impegno.getListaModificheMovimentoGestioneSpesa();
			List<ModificaMovimentoGestioneSpesa> modifiche = new ArrayList<ModificaMovimentoGestioneSpesa>();
			modifiche = CruscottoRorUtilities.getModifichePerCruscotto(listaModifiche, modifiche);
			modifiche = CruscottoRorUtilities.getModificheStatoValidoImpControllo(modifiche);

			for (ModificaMovimentoGestioneSpesa mmgs : modifiche) {
				
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

			if (duplicatiInLista == true || numeroInesigibilita > 1 || numeroInsussistenze > 1
					|| numeroMantenimenti > 1) {
				abilita = false;
				return abilita;
			} else {
				abilita = true;
			}

			boolean s = false;
			if (modifiche.size() > 1) {
				for (int i = 0; i < modifiche.size(); i++) {
					for (int j = 1; j < modifiche.size(); j++) {
						s = CruscottoRorUtilities.stessoProvvedimentoCruscotto(
								modifiche.get(i).getAttoAmministrativo(),
								modifiche.get(j).getAttoAmministrativo());	
						if (s == false) {
							abilita = false;
							return abilita;
						} else {
							abilita = true;
						}

					}

				}

			}

		}//Check modifcihe FINE	
			
			if (abilita) {
				// CONTROLLI SU
				/*
				 * importo impegno >0 E IMPORTO RESIDUO AL 31/12/N-1 > 0 E Deve
				 * essere vera una delle due condizioni seguenti •
				 * OP-SPE-gestImpROR • OP-SPE-gestImpRORdecentrato e sono
				 * rispettate le regole di visibilità definite a par. 2.7.1 E
				 * stato impegno = D || stato impegno = N
				 * 
				 */
				// SE TESTATA O SUB
				if (impegno.getNumeroBigDecimal() == null) {
					// TESTATA
					//SIAC-8267, SIAC-8607 si rimuove controllo sull'importo attuale
					if (impegno.getImportoDaRiaccertare() != null
							&& impegno.getImportoDaRiaccertare().compareTo(BigDecimal.ZERO) > 0
							&& CostantiFin.MOVGEST_STATO_DEFINITIVO.equals(impegno.getStatoOperativoMovimentoGestioneSpesa())
							|| CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equals(impegno.getStatoOperativoMovimentoGestioneSpesa())) {

						// CONTROLLO SAC
						/* SE AMMINISTRATORE VIENE SALTATO IL CONTROLLO SULLA SAC
						 * AMMINISTRATORE SE HA L'AZIONE  OP-SPE-gestImpROR
						 * 
						 */
						setAzioniDecToCheck(AzioneConsentitaEnum.OP_SPE_gestImprROR.getNomeAzione()); 
						if(checkAzioniDec()){  
							//CONTROLLO SOLO SU ESISTENZA SAC
							if(impegno.getStrutturaCompetenteObj()==null || impegno.getStrutturaCompetenteObj().getUid()==0){
								abilita = false;
							}
							
						}else{
							abilita = checkSacUtenteValida(impegno.getStrutturaCompetenteObj());
						}
						
					} else {
						abilita = false;
						return abilita;
					}

				} else {
					// SUB
					//SIAC-8267, SIAC-8607 si rimuove controllo sull'importo attuale
					if (impegno.getImportoDaRiaccertare() != null
							&& impegno.getImportoDaRiaccertare().compareTo(BigDecimal.ZERO) > 0
							&& CostantiFin.MOVGEST_STATO_DEFINITIVO.equals(impegno.getStatoOperativoMovimentoGestioneSpesa())
							|| CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equals(impegno.getStatoOperativoMovimentoGestioneSpesa())) {

						
						// CONTROLLO SAC
						/* SE AMMINISTRATORE VIENE SALTATO IL CONTROLLO SULLA SAC
						 * AMMINISTRATORE SE HA L'AZIONE  OP-SPE-gestImpROR
						 * 
						 */
						setAzioniDecToCheck(AzioneConsentitaEnum.OP_SPE_gestImprROR.getNomeAzione()); 
						if(checkAzioni()){  
							//CONTROLLO SOLO SU ESISTENZA SAC
							if(impegno.getStrutturaCompetenteObj()==null || impegno.getStrutturaCompetenteObj().getUid()==0){
								abilita = false;
							}
							
						}else{
							abilita = checkSacUtenteValida(impegno.getStrutturaCompetenteObj());
						}
						

					} else {
						abilita = false;

					}
				}
			}

		

		return abilita;
	}

	/**
	 * click su inserisci impegno dalla pagina di elenco impegni
	 * 
	 * @return
	 */
	public String inserisciImpegni() {
		return "gotoInserisciImpegni";
	}

	// GETTER E SETTER:

	public String getUidImpegno() {
		return uidImpegno;
	}

	public void setUidImpegno(String uidImpegno) {
		this.uidImpegno = uidImpegno;
	}

	public String getNumeroImpegno() {
		return numeroImpegno;
	}

	public void setNumeroImpegno(String numeroImpegno) {
		this.numeroImpegno = numeroImpegno;
	}

	public String getNumeroImpegnoDaPassare() {
		return numeroImpegnoDaPassare;
	}

	public void setNumeroImpegnoDaPassare(String numeroImpegnoDaPassare) {
		this.numeroImpegnoDaPassare = numeroImpegnoDaPassare;
	}

	public String getAnnoImpegnoImpegnoDaPassare() {
		return annoImpegnoImpegnoDaPassare;
	}

	public void setAnnoImpegnoImpegnoDaPassare(String annoImpegnoImpegnoDaPassare) {
		this.annoImpegnoImpegnoDaPassare = annoImpegnoImpegnoDaPassare;
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

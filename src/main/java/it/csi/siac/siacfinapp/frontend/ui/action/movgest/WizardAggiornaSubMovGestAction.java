/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciImpegnoStep1Model;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ImpegniPluriennaliModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione.StatoOperativoModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class WizardAggiornaSubMovGestAction extends WizardAggiornaMovGestAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ProvvedimentoService provvedimentoService;

	protected static final String CODICE_MOTIVO_RIACCERTAMENTO = "RIAC";

	protected static final String PROSEGUI = "prosegui";

	protected void pulisciSoggettoSelezionato() {
		if (model != null) {
			model.setSubImpegnoSelectedMod(false);
			model.setSoggettoSubAttuale(null);
			model.setSoggettoImpegnoAttuale(null);
			model.setSoggettoTrovato(false);
			model.setUltimoSoggettoSelezionatoCorrettamente(null);
		}
	}

	/**
	 * Formatta e popola i dati dei pluriennali nel model.
	 * 
	 * Il contenuto di questo metodo era duplicato nelle execute di
	 * InserisciModificaMovimentoSpesaAccStep2Action e di
	 * InserisciModificaMovimentoSpesaStep2Action.
	 * 
	 * Per evitare duplicazioni di codice e' stato messo in questo metodo a
	 * fattor comune.
	 * 
	 * 
	 */
	protected void popolaPluriennaliNelModel() {
		if (model.getMovimentoSpesaModel().getListaImpegniPluriennali() == null
				|| model.getMovimentoSpesaModel().getListaImpegniPluriennali().size() == 0) {

			// se la lista dei pluriennali e' vuota
			// significa che questo metodo non e' ancora stato invocato,
			// devo popolarla correttamente:

			int annoImpegno = model.getMovimentoSpesaModel().getAnnoImpegno();
			if (annoImpegno != 0) {
				// anno impegno valorizzato correttamente

				if (model.getMovimentoSpesaModel().getNumeroPluriennali() != 1) {
					// caso piu' di un pluriennale
					for (int i = 0; i < model.getMovimentoSpesaModel().getNumeroPluriennali(); i++) {
						ImpegniPluriennaliModel impegniPluriennali = new ImpegniPluriennaliModel();
						impegniPluriennali.setAnnoImpPluriennale(annoImpegno + i + 1);
						impegniPluriennali.setDataScadenzaImpPluriennaleString(
								"31/12/" + impegniPluriennali.getAnnoImpPluriennale());
						model.getMovimentoSpesaModel().getListaImpegniPluriennali().add(impegniPluriennali);
					}
				} else {
					// caso solo un pluriennale
					ImpegniPluriennaliModel impegniPluriennali = new ImpegniPluriennaliModel();
					impegniPluriennali.setAnnoImpPluriennale(annoImpegno + 1);
					impegniPluriennali.setImportoImpPluriennaleString(
							convertiBigDecimalToImporto(model.getMovimentoSpesaModel().getImportoTot().negate()));
					impegniPluriennali
							.setDataScadenzaImpPluriennaleString("31/12/" + impegniPluriennali.getAnnoImpPluriennale());
					model.getMovimentoSpesaModel().getListaImpegniPluriennali().add(impegniPluriennali);
				}
			}
		}
	}

	/**
	 * 
	 * Il contenuto di questo metodo era duplicato nei vari metodi di
	 * salvataggio di InserisciModificaMovimentoSpesaAccStep2Action e di
	 * InserisciModificaMovimentoSpesaStep2Action.
	 * 
	 * Per evitare duplicazioni di codice e' stato messo in questo metodo a
	 * fattor comune.
	 * 
	 * @param totaleImporti
	 * @param listaErrori
	 * @return
	 */
	protected List<Errore> controlloDistribuzioneImporti(BigDecimal totaleImporti, List<Errore> listaErrori) {
		int differenzaImporti = model.getMovimentoSpesaModel().getImportoTot().negate().compareTo(totaleImporti);
		if (differenzaImporti != 0) {
			listaErrori.add(ErroreFin.DISTRIBUZIONE_IMPORTI_ERRATA.getErrore(""));
		}
		return listaErrori;
	}

	public String aggiornaContemporaneo() {

		if (model.getStep1ModelSubimpegno().getProvvedimento().getStato() != null
				&& !model.getStep1ModelSubimpegno().getProvvedimento().getStato().equals("")
				&& model.getStep1ModelSubimpegno().getProvvedimento().getStato().equals("PROVVISORIO")) {
			if (null != model.getStep1ModelSubimpegno().getImportoFormattato()
					&& !model.getStep1ModelSubimpegno().getImportoFormattato().equals("")) {
				try {
					model.getSubDettaglio().setImportoIniziale(
							convertiImportoToBigDecimal(model.getStep1ModelSubimpegno().getImportoFormattato()));
				} catch (Exception e) {
					model.getSubDettaglio().setImportoIniziale(BigDecimal.ZERO);
				}
			}

		}

		return "aggiornaContemporaneo";
	}

	protected boolean stessoProvvedimento(ProvvedimentoImpegnoModel provvedimentoCache,
			ProvvedimentoImpegnoModel provvedimentoInModifica) {

		Integer annoProvvedimentoImpegno = provvedimentoCache.getAnnoProvvedimento();
		Integer numeroProvvedimentoImpegno = provvedimentoCache.getNumeroProvvedimento().intValue();
		Integer idTipoProvvedimentoImpegno = provvedimentoCache.getIdTipoProvvedimento();
		Integer uidStrutturaAmministrativa = provvedimentoCache.getUidStrutturaAmm();

		Integer annoProvvedimentoModifica = provvedimentoInModifica.getAnnoProvvedimento();
		Integer numeroProvvedimentoModifica = provvedimentoInModifica.getNumeroProvvedimento().intValue();
		Integer idTipoProvvedimentoModifica = provvedimentoInModifica.getIdTipoProvvedimento();
		Integer uidStrutturaAmministrativaModifica = provvedimentoInModifica.getUidStrutturaAmm();

		boolean uguali = false;

		if (annoProvvedimentoImpegno.equals(annoProvvedimentoModifica)
				&& numeroProvvedimentoImpegno.equals(numeroProvvedimentoModifica)
				&& idTipoProvvedimentoImpegno.equals(idTipoProvvedimentoModifica) && FinStringUtils
						.sonoUgualiIntConNullUguale(uidStrutturaAmministrativa, uidStrutturaAmministrativaModifica)) {
			uguali = true;
		}

		return uguali;

	}

	// SIAC-6929 Metodo che mi restituisce, dopo aver fatto una chiamata a DB se
	// il provvedimento che voglio salvare è bloccato dalla ragioneria
	protected boolean provvedimentoBloccatoDaRagioneriaById(Integer provvedimentoInModifica) {
		RicercaProvvedimento ricercaProvvedimento = new RicercaProvvedimento();
		ricercaProvvedimento.setEnte(sessionHandler.getEnte());
		ricercaProvvedimento.setRichiedente(sessionHandler.getRichiedente());
		RicercaAtti ricercaAtti = new RicercaAtti();
		ricercaAtti.setUid(provvedimentoInModifica);
		ricercaProvvedimento.setRicercaAtti(ricercaAtti);
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(ricercaProvvedimento);
		if (response.getErrori().isEmpty()) {
			List<AttoAmministrativo> atto = response.getListaAttiAmministrativi();
			if (atto.get(0).getBloccoRagioneria() != null && atto.get(0).getBloccoRagioneria() == true) {
				return true;
			} else {
				return false;
			}
		}
		return false;

	}

	protected boolean checkProvvedimentoModificaImportoSoggettoMovimentoGestione(
			ProvvedimentoImpegnoModel provvedimentoCache, ProvvedimentoImpegnoModel provvedimentoInModifica,
			String tipoMovimentoGestione, List<Errore> listaErrori) {
		// Jira 2302: Controllo che il provvedimento della modifica sia diverso
		// dal provvedimento dell'impegno
		// Per decentrato l'errore è bloccante, altrimenti se utente non dec
		// solo warning

		boolean soggettoDecentrato = isSoggettoDecentrato();
		boolean erroreProvvedimento = false;

		boolean stessoProvvedimento = stessoProvvedimento(provvedimentoCache, provvedimentoInModifica);

		// SIAC-6929
		if (provvedimentoBloccatoDaRagioneriaById(provvedimentoInModifica.getUid())) {
			erroreProvvedimento = true;
			listaErrori.add(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA
					.getErrore("Numero Provvedimento " + provvedimentoInModifica.getNumeroProvvedimento() + " Oggetto "
							+ provvedimentoInModifica.getOggetto()));
		}

		if (stessoProvvedimento) {

			if (!soggettoDecentrato) {

				if (!model.isProseguiConWarning()) {
					
					addPersistentActionWarningFin(ErroreCore.INCONGRUENZA_NEI_PARAMETRI
							.getErrore("il provvedimento deve essere diverso da quello del movimento di tipo " + tipoMovimentoGestione));
					
					erroreProvvedimento = true;
					model.setProseguiConWarning(true);

					// JIRA SIAC-2519 LO RIMETTO A FALSE PER EVITARE DI APRIRE
					// IL POP-UP:
					model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
					// verra' rimesso a true dal metodo salvaPopup

				}

			} else {
				erroreProvvedimento = true;
				listaErrori.add(ErroreCore.INCONGRUENZA_NEI_PARAMETRI
						.getErrore("il provvedimento deve essere diverso da quello del movimento di tipo "
								+ tipoMovimentoGestione));
			}

		}

		return erroreProvvedimento;

	}

	protected AnnullaMovimentoEntrata convertiModelPerChiamataServizioAnnullaSubAcc(String uid) {
		AnnullaMovimentoEntrata request = new AnnullaMovimentoEntrata();
		request.setEnte(sessionHandler.getEnte());
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setBilancio(creaOggettoBilancio());

		Accertamento accertamento = new Accertamento();
		accertamento.setUid(model.getMovimentoSpesaModel().getAccertamento().getUid());
		ModificaMovimentoGestioneEntrata mge = new ModificaMovimentoGestioneEntrata();
		List<ModificaMovimentoGestioneEntrata> listaMGP = new ArrayList<ModificaMovimentoGestioneEntrata>();
		mge.setUid(Integer.valueOf(uid));
		listaMGP.add(mge);
		accertamento.setListaModificheMovimentoGestioneEntrata(listaMGP);
		request.setAccertamento(accertamento);
		return request;
	}

	/**
	 * Aprile 2016 wrapper di retrocompatibilita'
	 */
	protected void executeSub() {
		executeSub(false);
	}

	/**
	 * Aprile 2016 introdotta possibilita' di ricaricare il sub specifico da
	 * servizio
	 * 
	 * @param ricaricaSubDaServizio
	 */
	protected void executeSub(boolean ricaricaSubDaServizio) {
		if (isInserimentoSubimpegno()) {
			model.setStep1ModelSubimpegno(new GestisciImpegnoStep1Model());

			if (oggettoDaPopolareSubimpegno()) {
				model.setTitolo("Inserisci Subimpegno");
				model.getStep1ModelSubimpegno().setCig(model.getStep1Model().getCig());
				model.getStep1ModelSubimpegno().setCup(model.getStep1Model().getCup());

			} else {
				model.setTitolo("Inserisci Subaccertamento");
			}
			model.getStep1ModelSubimpegno().setProgetto(model.getStep1Model().getProgetto());
			model.getStep1ModelSubimpegno().setStato(model.getStep1Model().getProvvedimento().getStato());
			model.getStep1ModelSubimpegno().setImportoImpegno(model.getDisponibilitaSubImpegnare());
			model.getStep1ModelSubimpegno().setImportoImpegnoMod(BigDecimal.ZERO);
			model.getStep1ModelSubimpegno()
					.setImportoFormattato(convertiBigDecimalToImporto(model.getDisponibilitaSubImpegnare()));
			if (model.getStep1Model().getScadenza() != null
					&& !"".equalsIgnoreCase(model.getStep1Model().getScadenza())) {
				model.getStep1ModelSubimpegno().setScadenza(model.getStep1Model().getScadenza());
			}
			if (model.getStep1Model().getProvvedimento() != null) {
				model.getStep1ModelSubimpegno().setProvvedimento(model.getStep1Model().getProvvedimento());
				model.getStep1ModelSubimpegno().setProvvedimentoSelezionato(true);
			}
			pulisciTransazioneElementare();
			if (oggettoDaPopolareSubimpegno()) {
				caricaTSImpToSub();
			} else {
				caricaTSAccToSub();
			}

			if (oggettoDaPopolareSubimpegno()) {
				// SIOPE PLUS solo per sub imp (no per sub acc)

				// tipo debito siope:
				model.getStep1ModelSubimpegno().setScelteTipoDebitoSiope(buildListaTipoDebitoSiopePerImpegni());

				// dati di default ereditati da quelli dell'impegno in
				// aggiornamento:
				impostaDatiSiopePlusNelModel(model.getImpegnoInAggiornamento(), model.getStep1ModelSubimpegno());
				// FINE SIOPE PLUS
			}

		} else {
			// Aggiornamento
			if (oggettoDaPopolareSubimpegno()) {
				executeAggiornaSubimpegno(ricaricaSubDaServizio);

			} else {
				executeAggiornaSubaccertamento();
			}
			// descrizione subimpegno con anno e descrizione
			settaNumDescSub(oggettoDaPopolareSubimpegno());
		}

		model.getStep1ModelSubimpegno().setAnnoImpegno(model.getStep1Model().getAnnoImpegno());
	}

	private void settaNumDescSub(boolean isSubImpegno) {
		if (isSubImpegno) {
			if (model.getListaSubimpegni() != null && model.getListaSubimpegni().size() > 0) {
				for (SubImpegno itSubImp : model.getListaSubimpegni()) {
					if (Integer.valueOf(getIdSubImpegno()) == (itSubImp.getUid())) {
						model.getSubDettaglio().setDescrizione(itSubImp.getDescrizione());
						model.getSubDettaglio().setNumero(itSubImp.getNumeroBigDecimal().toString());
						model.getSubDettaglio().setDisponibilitaLiquidare(itSubImp.getDisponibilitaLiquidare());
						model.getSubDettaglio().setImportoIniziale(itSubImp.getImportoIniziale());
					}
				}
			}
		} else {
			if (model.getListaSubaccertamenti() != null && model.getListaSubaccertamenti().size() > 0) {
				for (SubAccertamento itSubAcc : model.getListaSubaccertamenti()) {
					if (Integer.valueOf(getIdSubImpegno()) == (itSubAcc.getUid())) {

						model.getSubDettaglio().setDescrizione(itSubAcc.getDescrizione());
						model.getSubDettaglio().setNumero(itSubAcc.getNumeroBigDecimal().toString());
						model.getSubDettaglio().setImportoIniziale(itSubAcc.getImportoIniziale());
					}
				}

			}
		}
	}

	private void caricaTSImpToSub() {
		teSupport.setMissioneSelezionata(model.getImpegnoInAggiornamento().getCodMissione());
		teSupport.setProgrammaSelezionato(model.getImpegnoInAggiornamento().getCodProgramma());
		if (model.getImpegnoInAggiornamento().getIdPdc() != null) {
			teSupport.getPianoDeiConti().setUid(model.getImpegnoInAggiornamento().getIdPdc());
		}

		// PDC Padre per creazione albero
		if (model.getStep1Model().getCapitolo().getIdPianoDeiConti() != null) {
			teSupport.setIdPianoDeiContiPadrePerAggiorna(model.getStep1Model().getCapitolo().getIdPianoDeiConti());
		}

		teSupport.getPianoDeiConti().setCodice(model.getImpegnoInAggiornamento().getCodPdc());
		// in descrizione metto codice e descrizione
		teSupport.getPianoDeiConti().setDescrizione(
				model.getImpegnoInAggiornamento().getCodPdc() + "- " + model.getImpegnoInAggiornamento().getDescPdc());

		teSupport.setIdMacroAggregato(model.getStep1Model().getCapitolo().getIdMacroAggregato());

		teSupport.setSiopeSpesa(new SiopeSpesa());
		if (model.getImpegnoInAggiornamento().getIdSiope() != null) {
			teSupport.getSiopeSpesa().setUid(model.getImpegnoInAggiornamento().getIdSiope());
		}
		teSupport.getSiopeSpesa().setCodice(model.getImpegnoInAggiornamento().getCodSiope());
		teSupport.getSiopeSpesa().setDescrizione(model.getImpegnoInAggiornamento().getDescCodSiope());

		codiceSiopeChangedInternal(model.getImpegnoInAggiornamento().getCodSiope());

		// cofog
		teSupport.setCofogSelezionato(model.getImpegnoInAggiornamento().getCodCofog());
		// transazione europea spesa
		teSupport.setTransazioneEuropeaSelezionato(model.getImpegnoInAggiornamento().getCodTransazioneEuropeaSpesa());
		// ricorrente spesa
		teSupport.setRicorrenteSpesaSelezionato(model.getImpegnoInAggiornamento().getCodRicorrenteSpesa());
		// sanitario spesa
		teSupport.setPerimetroSanitarioSpesaSelezionato(
				model.getImpegnoInAggiornamento().getCodCapitoloSanitarioSpesa());
		// politiche regionali
		teSupport.setPoliticaRegionaleSelezionato(model.getImpegnoInAggiornamento().getCodPrgPolReg());

		// copio tutto lo step 2 per gestire annulla
		model.setTransazioneElementareSubMovimentoCache(clone(teSupport));
	}

	private void caricaTSAccToSub() {
		teSupport.setMissioneSelezionata(model.getAccertamentoInAggiornamento().getCodMissione());
		teSupport.setProgrammaSelezionato(model.getAccertamentoInAggiornamento().getCodProgramma());
		if (model.getAccertamentoInAggiornamento().getIdPdc() != null) {
			teSupport.getPianoDeiConti().setUid(model.getAccertamentoInAggiornamento().getIdPdc());
		}
		teSupport.getPianoDeiConti().setCodice(model.getAccertamentoInAggiornamento().getCodPdc());

		// PDC Padre per creazione albero
		if (model.getStep1Model().getCapitolo().getIdPianoDeiConti() != null) {
			teSupport.setIdPianoDeiContiPadrePerAggiorna(model.getStep1Model().getCapitolo().getIdPianoDeiConti());
		}

		// in descrizione metto codice e descrizione
		teSupport.getPianoDeiConti().setDescrizione(model.getAccertamentoInAggiornamento().getCodPdc() + " - "
				+ model.getAccertamentoInAggiornamento().getDescPdc());

		// siope
		teSupport.setSiopeSpesa(new SiopeSpesa());
		if (model.getAccertamentoInAggiornamento().getIdSiope() != null) {
			teSupport.getSiopeSpesa().setUid(model.getAccertamentoInAggiornamento().getIdSiope());
		}
		teSupport.getSiopeSpesa().setCodice(model.getAccertamentoInAggiornamento().getCodSiope());
		teSupport.getSiopeSpesa().setDescrizione(model.getAccertamentoInAggiornamento().getDescCodSiope());

		codiceSiopeChangedInternal(model.getAccertamentoInAggiornamento().getCodSiope());

		// transazione europea entrata
		teSupport.setTransazioneEuropeaSelezionato(
				model.getAccertamentoInAggiornamento().getCodTransazioneEuropeaSpesa());
		// ricorrente entrata
		teSupport.setRicorrenteEntrataSelezionato(model.getAccertamentoInAggiornamento().getCodRicorrenteSpesa());

		// sanitario entrata
		teSupport.setPerimetroSanitarioEntrataSelezionato(
				model.getAccertamentoInAggiornamento().getCodCapitoloSanitarioSpesa());

		// copio tutto lo step 2 per gestire annulla
		model.setTransazioneElementareSubMovimentoCache(clone(teSupport));

	}

	/**
	 * Aprile 2016 introdotta possibilita' di ricaricare il sub specifico da
	 * servizio
	 * 
	 * @param ricaricaSubDaServizio
	 */
	private void executeAggiornaSubimpegno(boolean ricaricaSubDaServizio) {
		if (getIdSubImpegno() != null && model.getListaSubimpegni() != null && model.getListaSubimpegni().size() > 0) {
			for (SubImpegno currentSubimpegno : model.getListaSubimpegni()) {
				if (currentSubimpegno.getUid() == getIdSubImpegno().intValue()) {

					SubImpegno subImpegnoInAggiornamento = null;

					if (ricaricaSubDaServizio) {

						RicercaImpegnoPerChiaveOttimizzato rip = new RicercaImpegnoPerChiaveOttimizzato();
						//
						rip.setEnte(sessionHandler.getEnte());
						rip.setRichiedente(sessionHandler.getRichiedente());
						RicercaImpegnoK k = new RicercaImpegnoK();
						k.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
						k.setAnnoImpegno(this.model.getAnnoImpegno());
						k.setNumeroImpegno(new BigDecimal(this.model.getNumeroImpegno()));
						rip.setCaricaSub(true);
						k.setNumeroSubDaCercare(currentSubimpegno.getNumeroBigDecimal());
						//
						rip.setpRicercaImpegnoK(k);

						RicercaImpegnoPerChiaveOttimizzatoResponse respRk = movimentoGestioneFinService
								.ricercaImpegnoPerChiaveOttimizzato(rip);

						if (respRk != null && respRk.getImpegno() != null
								&& respRk.getImpegno().getElencoSubImpegni() != null
								&& respRk.getImpegno().getElencoSubImpegni().size() > 0) {

							subImpegnoInAggiornamento = respRk.getImpegno().getElencoSubImpegni().get(0);

						}

					} else {
						subImpegnoInAggiornamento = currentSubimpegno;
					}

					impostaDatiPerAggiornaSubImpegno(subImpegnoInAggiornamento);

					// JIRA SIAC-3506 in caso di residuo con presenza di
					// modifiche di importo valide, importo non modificabile:
					List<ModificaMovimentoGestioneSpesa> modifiche = currentSubimpegno
							.getListaModificheMovimentoGestioneSpesa();
					if (presenteModificaDiImportoValida(modifiche) && isResiduo()) {
						setAbilitaModificaImporto(false);
					}
					//

					break;
				}
			}
		}
	}

	protected boolean isResiduo() {
		Integer annoImpegno = model.getStep1Model().getAnnoImpegno();
		Integer annoEsercizio = sessionHandler.getAnnoBilancio();
		boolean residuo = annoImpegno.compareTo(annoEsercizio) < 0;
		return residuo;
	}

	protected boolean presenteModificaDiImportoValidaEntrata(List<ModificaMovimentoGestioneEntrata> modifiche) {
		boolean trovataValida = false;
		if (modifiche != null && modifiche.size() > 0) {
			for (ModificaMovimentoGestioneEntrata modIt : modifiche) {
				if (modIt.getStatoOperativoModificaMovimentoGestione()
						.equals(StatoOperativoModificaMovimentoGestione.VALIDO)) {
					if (modIt.getImportoNew() != null && modIt.getImportoOld() != null) {
						// e' di importo
						trovataValida = true;
						break;
					}
				}
			}
		}
		return trovataValida;
	}

	protected boolean presenteModificaDiImportoValida(List<ModificaMovimentoGestioneSpesa> modifiche) {
		boolean trovataValida = false;
		if (modifiche != null && modifiche.size() > 0) {
			for (ModificaMovimentoGestioneSpesa modIt : modifiche) {
				if (modIt.getStatoOperativoModificaMovimentoGestione()
						.equals(StatoOperativoModificaMovimentoGestione.VALIDO)) {
					if (modIt.getImportoNew() != null && modIt.getImportoOld() != null) {
						// e' di importo
						trovataValida = true;
						break;
					}
				}
			}
		}
		return trovataValida;
	}

	private void impostaDatiPerAggiornaSubImpegno(SubImpegno currentSubimpegno) {
		pulisciTransazioneElementare();
		model.setStep1ModelSubimpegno(new GestisciImpegnoStep1Model());
		model.setTitolo("Aggiorna Subimpegno");
		model.getStep1ModelSubimpegno().setCig(currentSubimpegno.getCig());
		model.getStep1ModelSubimpegno().setCup(currentSubimpegno.getCup());
		teSupport.setMissioneSelezionata(currentSubimpegno.getCodMissione());
		teSupport.setProgrammaSelezionato(currentSubimpegno.getCodProgramma());
		model.getStep1ModelSubimpegno().setOggettoImpegno(currentSubimpegno.getDescrizione());
		if (currentSubimpegno.getAttoAmministrativo() != null
				&& currentSubimpegno.getAttoAmministrativo().getUid() != 0) {
			caricaDatiProvvedimento(currentSubimpegno.getAttoAmministrativo());
		}
		if (currentSubimpegno.getSoggetto() != null && currentSubimpegno.getSoggetto().getCodiceSoggetto() != null
				&& !"".equalsIgnoreCase(currentSubimpegno.getSoggetto().getCodiceSoggetto())) {
			model.getStep1ModelSubimpegno().setSoggetto(new SoggettoImpegnoModel());
			model.getStep1ModelSubimpegno().getSoggetto()
					.setCodCreditore(currentSubimpegno.getSoggetto().getCodiceSoggetto());
			model.getStep1ModelSubimpegno().getSoggetto()
					.setDenominazione(currentSubimpegno.getSoggetto().getDenominazione());
			model.getStep1ModelSubimpegno().getSoggetto()
					.setCodfisc(currentSubimpegno.getSoggetto().getCodiceFiscale());
			model.getStep1ModelSubimpegno().setSoggettoSelezionato(true);
		}
		model.getStep1ModelSubimpegno()
				.setStato(currentSubimpegno.getDescrizioneStatoOperativoMovimentoGestioneSpesa());
		// SIAC-5943: devo sapere se lo stato precedente fosse o meno definitivo
		model.setStatoMovimentoGestioneOld(model.getStep1ModelSubimpegno().getStato());
		if (currentSubimpegno.getProgetto() != null) {
			model.getStep1ModelSubimpegno().setProgetto(currentSubimpegno.getProgetto().getDescrizione());
		}
		if (currentSubimpegno.getDataScadenza() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			model.getStep1ModelSubimpegno().setScadenza(sdf.format(currentSubimpegno.getDataScadenza()));
		}
		model.getStep1ModelSubimpegno()
				.setImportoFormattato(convertiBigDecimalToImporto(currentSubimpegno.getImportoAttuale()));
		model.getStep1ModelSubimpegno().setImportoImpegnoMod(currentSubimpegno.getImportoAttuale());
		model.getStep1ModelSubimpegno().setUid(currentSubimpegno.getUid());

		// disp a liquidare da function:
		model.getStep1ModelSubimpegno()
				.setDisponibileLiquidareDaFunction(currentSubimpegno.getDisponibilitaLiquidareBase());
		//

		// SIOPE PLUS solo per sub imp (no per sub acc)

		// tipo debito siope:
		model.getStep1ModelSubimpegno().setScelteTipoDebitoSiope(buildListaTipoDebitoSiopePerImpegni());

		// dati di default ereditati da quelli dell'impegno in aggiornamento:
		impostaDatiSiopePlusNelModel(currentSubimpegno, model.getStep1ModelSubimpegno());
		// FINE SIOPE PLUS

		// NON SETTAVA IL NUMERO:
		if (currentSubimpegno.getNumeroBigDecimal() != null) {
			model.getStep1ModelSubimpegno().setNumeroImpegno(currentSubimpegno.getNumeroBigDecimal().intValue());
		}

		// stato operativo:
		model.getStep1ModelSubimpegno().setStatoOperativo(currentSubimpegno.getStatoOperativoMovimentoGestioneSpesa());
		//

		model.setPianoDeiConti(new ElementoPianoDeiConti());
		if (currentSubimpegno.getIdPdc() != null) {
			teSupport.getPianoDeiConti().setUid(currentSubimpegno.getIdPdc());
		}
		// PDC Padre per creazione albero
		if (model.getStep1Model().getCapitolo().getIdPianoDeiConti() != null) {
			teSupport.setIdPianoDeiContiPadrePerAggiorna(model.getStep1Model().getCapitolo().getIdPianoDeiConti());
		}
		teSupport.getPianoDeiConti().setCodice(currentSubimpegno.getCodPdc());
		// inserisco codice e descrizione
		teSupport.getPianoDeiConti()
				.setDescrizione(currentSubimpegno.getCodPdc() + " - " + currentSubimpegno.getDescPdc());

		teSupport.setSiopeSpesa(new SiopeSpesa());
		if (currentSubimpegno.getIdSiope() != null) {
			teSupport.getSiopeSpesa().setUid(currentSubimpegno.getIdSiope());
		}
		teSupport.getSiopeSpesa().setCodice(currentSubimpegno.getCodSiope());
		teSupport.getSiopeSpesa().setDescrizione(currentSubimpegno.getDescCodSiope());

		codiceSiopeChangedInternal(currentSubimpegno.getCodSiope());

		teSupport.setCofogSelezionato(currentSubimpegno.getCodCofog());
		teSupport.setTransazioneEuropeaSelezionato(currentSubimpegno.getCodTransazioneEuropeaSpesa());
		teSupport.setRicorrenteSpesaSelezionato(currentSubimpegno.getCodRicorrenteSpesa());
		teSupport.setPerimetroSanitarioSpesaSelezionato(currentSubimpegno.getCodCapitoloSanitarioSpesa());
		teSupport.setPoliticaRegionaleSelezionato(currentSubimpegno.getCodPrgPolReg());

		// JIRA SIAC-3506 in caso di residuo con presenza di modifiche di
		// importo valide, importo non modificabile:
		List<ModificaMovimentoGestioneSpesa> modifiche = currentSubimpegno.getListaModificheMovimentoGestioneSpesa();
		if (presenteModificaDiImportoValida(modifiche) && isResiduo()) {
			setAbilitaModificaImporto(false);
		}
		//

	}

	private void executeAggiornaSubaccertamento() {
		if (getIdSubImpegno() != null && model.getListaSubaccertamenti() != null
				&& model.getListaSubaccertamenti().size() > 0) {
			for (SubAccertamento currentSubaccertamento : model.getListaSubaccertamenti()) {
				if (currentSubaccertamento.getUid() == getIdSubImpegno().intValue()) {
					pulisciTransazioneElementare();
					model.setStep1ModelSubimpegno(new GestisciImpegnoStep1Model());
					model.setTitolo("Aggiorna Subaccertamento");
					model.getStep1ModelSubimpegno().setOggettoImpegno(currentSubaccertamento.getDescrizione());
					if (currentSubaccertamento.getAttoAmministrativo() != null
							&& currentSubaccertamento.getAttoAmministrativo().getUid() != 0) {
						caricaDatiProvvedimento(currentSubaccertamento.getAttoAmministrativo());
					}
					if (currentSubaccertamento.getSoggetto() != null
							&& currentSubaccertamento.getSoggetto().getCodiceSoggetto() != null
							&& !"".equalsIgnoreCase(currentSubaccertamento.getSoggetto().getCodiceSoggetto())) {
						model.getStep1ModelSubimpegno().setSoggetto(new SoggettoImpegnoModel());
						model.getStep1ModelSubimpegno().getSoggetto()
								.setCodCreditore(currentSubaccertamento.getSoggetto().getCodiceSoggetto());
						model.getStep1ModelSubimpegno().getSoggetto()
								.setDenominazione(currentSubaccertamento.getSoggetto().getDenominazione());
						model.getStep1ModelSubimpegno().getSoggetto()
								.setCodfisc(currentSubaccertamento.getSoggetto().getCodiceFiscale());
						model.getStep1ModelSubimpegno().setSoggettoSelezionato(true);
					}
					model.getStep1ModelSubimpegno()
							.setStato(currentSubaccertamento.getDescrizioneStatoOperativoMovimentoGestioneEntrata());
					// SIAC-5943: devo sapere se lo stato precedente fosse o
					// meno definitivo
					model.setStatoMovimentoGestioneOld(model.getStep1ModelSubimpegno().getStato());
					if (currentSubaccertamento.getProgetto() != null) {
						model.getStep1ModelSubimpegno()
								.setProgetto(currentSubaccertamento.getProgetto().getDescrizione());
					}
					if (currentSubaccertamento.getDataScadenza() != null) {
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						model.getStep1ModelSubimpegno()
								.setScadenza(sdf.format(currentSubaccertamento.getDataScadenza()));
					}

					model.getStep1ModelSubimpegno().setImportoFormattato(
							convertiBigDecimalToImporto(currentSubaccertamento.getImportoAttuale()));
					model.getStep1ModelSubimpegno().setImportoImpegnoMod(currentSubaccertamento.getImportoAttuale());
					model.getStep1ModelSubimpegno().setUid(currentSubaccertamento.getUid());
					teSupport.setPianoDeiConti(new ElementoPianoDeiConti());
					if (currentSubaccertamento.getIdPdc() != null) {
						teSupport.getPianoDeiConti().setUid(currentSubaccertamento.getIdPdc());
					}
					teSupport.getPianoDeiConti().setCodice(currentSubaccertamento.getCodPdc());
					teSupport.getPianoDeiConti().setDescrizione(
							currentSubaccertamento.getCodPdc() + " - " + currentSubaccertamento.getDescPdc());
					// PDC Padre per creazione albero
					if (model.getStep1Model().getCapitolo().getIdPianoDeiConti() != null) {
						teSupport.setIdPianoDeiContiPadrePerAggiorna(
								model.getStep1Model().getCapitolo().getIdPianoDeiConti());
					}

					teSupport.setSiopeSpesa(new SiopeSpesa());
					if (currentSubaccertamento.getIdSiope() != null) {
						teSupport.getSiopeSpesa().setUid(currentSubaccertamento.getIdSiope());
					}
					teSupport.getSiopeSpesa().setCodice(currentSubaccertamento.getCodSiope());
					teSupport.getSiopeSpesa().setDescrizione(currentSubaccertamento.getDescCodSiope());

					codiceSiopeChangedInternal(currentSubaccertamento.getCodSiope());

					teSupport.setCofogSelezionato(currentSubaccertamento.getCodCofog());
					teSupport.setTransazioneEuropeaSelezionato(currentSubaccertamento.getCodTransazioneEuropeaSpesa());
					teSupport.setRicorrenteEntrataSelezionato(currentSubaccertamento.getCodRicorrenteSpesa());
					teSupport.setPerimetroSanitarioEntrataSelezionato(
							currentSubaccertamento.getCodCapitoloSanitarioSpesa());
					teSupport.setPoliticaRegionaleSelezionato(currentSubaccertamento.getCodPrgPolReg());

					// JIRA SIAC-3506 in caso di residuo con presenza di
					// modifiche di importo valide, importo non modificabile:
					List<ModificaMovimentoGestioneEntrata> modifiche = currentSubaccertamento
							.getListaModificheMovimentoGestioneEntrata();
					if (presenteModificaDiImportoValidaEntrata(modifiche) && isResiduo()) {
						setAbilitaModificaImporto(false);
					}
					//

					break;
				}
			}
		}
	}

	public String prosegui() {
		return prosegui(false);
	}
	
	protected String prosegui(boolean proseguiSalva) {
		
		List<Errore> errori = erroriSubimpegno();
		boolean warngins = presenzaWarningsSubimpegno();
		
		//SIAC-6929
			if(model.getStep1ModelSubimpegno().getProvvedimento()!=null && model.getStep1ModelSubimpegno().getProvvedimento().getUid()!=null 
					&& model.getStep1ModelSubimpegno().getProvvedimento().getUid().intValue()>0) {
				ProvvedimentoImpegnoModel pim = getProvvedimentoById(model.getStep1ModelSubimpegno().getProvvedimento().getUid());
				if(pim.getBloccoRagioneria()!= null && pim.getBloccoRagioneria().booleanValue()){
					errori.add(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore("Numero Provvedimento " + 
							pim.getNumeroProvvedimento() + " Oggetto " + pim.getOggetto()));
				}
			}
		
		
		
		
		
		

		if (errori != null && errori.size() > 0) {
			addErrori(errori);
			return INPUT;
		}
		if (warngins) {
			return INPUT;
		}

		// CR SIAC-3224, in caso di aggiornamento il provvedimento si puo
		// modificare
		if (isFromModaleConfermaProseguiModificaProvvedimento()) {
			setShowModaleConfermaProseguiModificaProvvedimento(false);

		} else {
			if (verificaModificaProvvedimentoSubMovimento(false)) {
				setProseguiStep1(false);
				return INPUT;
			}
		}

		if (!controlloProvvedimentoSubPerDecentratoP()) {
			return INPUT;
		}

		if (!controlloServiziSub()) {
			// setto il macroaggreagato
			teSupport.setIdMacroAggregato(model.getStep1Model().getCapitolo().getIdMacroAggregato());

			aggiornaStatoSub();
			model.setStep1ModelSubimpegnoCache(clone(model.getStep1ModelSubimpegno()));
			return GOTO_SUBIMPEGNO_STEP2;
		} else {
			return INPUT;
		}
		
	}

	// controllo bypassato dalla CR 3224
	public boolean campoDisabilitato() {
		return model.getStep1ModelSubimpegno().getUid() != null
				&& STATO_MOVGEST_DEFINITIVO.equalsIgnoreCase(model.getStep1ModelSubimpegno().getStato());
	}

	public boolean campoImportoAbilitato() {

		boolean abilitaModificaImporto = true;

		if (model.getStep1ModelSubimpegno().getUid() != null) {

			if (!MOVGEST_PROVVISORIO.equalsIgnoreCase(model.getStep1ModelSubimpegno().getStato())) {

				// nel caso in cui fossimo in predisposizione consuntivo..
				// in modo che l'importo ritorni modificabile:
				Integer annoImpegno = model.getStep1ModelSubimpegno().getAnnoImpegno();
				Integer annoEsercizio = sessionHandler.getAnnoBilancio();

				boolean bilancioPrecInPredisposizioneConsuntivo = isBilancioPrecedenteInPredisposizioneConsuntivo();

				if (bilancioPrecInPredisposizioneConsuntivo && annoImpegno.compareTo(annoEsercizio) < 0) {
					abilitaModificaImporto = true;
				} else {
					abilitaModificaImporto = false;
				}
				//
			}
		}

		return abilitaModificaImporto;

	}

	public boolean isAnnullaMovimento(Integer idMovimento) {
		boolean abilitato = false;
		if (oggettoDaPopolareImpegno()) {
			List<ModificaMovimentoGestioneSpesa> lista = model.getMovimentoSpesaModel().getListaModifiche();
			ModificaMovimentoGestioneSpesa movimento = new ModificaMovimentoGestioneSpesa();

			int maxIdModImp = 0;
			// REPLICARE SU ACCERTAMENTO!!!!!!
			for (ModificaMovimentoGestioneSpesa m : lista) {
				if (m.getImportoNew() != null) {
					if (m.getUid() > maxIdModImp && !m.getStatoOperativoModificaMovimentoGestione()
							.equals(StatoOperativoModificaMovimentoGestione.ANNULLATO))
						maxIdModImp = m.getUid();
				}
				if (m.getUid() == idMovimento) {
					movimento = m;
				}
			}

			if (movimento.getStatoOperativoModificaMovimentoGestione() != null) {
				if (movimento.getStatoOperativoModificaMovimentoGestione()
						.equals(StatoOperativoModificaMovimentoGestione.ANNULLATO)) {
					abilitato = false;
				} else {
					// se e' una modifica soggetto non la devo annullare
					if (movimento.getImportoNew() == null) {
						return false;
					} else {
						// se la modifica e' una modifica importo, ed essa e'
						// l'ultima modifica importo effettuata, la devo poter
						// annullare, altrimenti no
						// SIAC-8611 - disabilitare controllo su ultima modifica: posso aggiornare anche le precedenti (solo su impegni)
						//if (movimento.getUid() == maxIdModImp && maxIdModImp != 0) {
						abilitato = true;
						/*} else {
							abilitato = false;
						}*/
					}
				}
			} else {
				abilitato = false;
			}

			return abilitato;
		} else {
			List<ModificaMovimentoGestioneEntrata> lista = model.getMovimentoSpesaModel().getListaModificheEntrata();
			ModificaMovimentoGestioneEntrata movimento = new ModificaMovimentoGestioneEntrata();
			int maxIdModImp = 0;
			for (ModificaMovimentoGestioneEntrata m : lista) {

				if (m.getImportoNew() != null) {
					if (m.getUid() > maxIdModImp && !m.getStatoOperativoModificaMovimentoGestione()
							.equals(StatoOperativoModificaMovimentoGestione.ANNULLATO)) {
						maxIdModImp = m.getUid();
					}
				}

				if (m.getUid() == idMovimento) {
					movimento = m;
				}
			}
			if (movimento.getStatoOperativoModificaMovimentoGestione() != null) {
				if (movimento.getStatoOperativoModificaMovimentoGestione()
						.equals(StatoOperativoModificaMovimentoGestione.ANNULLATO)) {
					abilitato = false;
				} else {

					// se la modifica e' una modifica importo, ed essa e'
					// l'ultima modifica importo effettuata, la devo poter
					// annullare, altrimenti no
					if (movimento.getUid() == maxIdModImp && maxIdModImp != 0) {
						abilitato = true;
					} else {
						abilitato = false;
					}

				}
			} else {
				abilitato = false;
			}

			return abilitato;
		}
	}

	public boolean isAggiornaMovimento(Integer idMovimento) {
		boolean abilitato = true;
		if (oggettoDaPopolareImpegno()) {
			List<ModificaMovimentoGestioneSpesa> lista = model.getMovimentoSpesaModel().getListaModifiche();
			ModificaMovimentoGestioneSpesa movimento = new ModificaMovimentoGestioneSpesa();
			for (ModificaMovimentoGestioneSpesa m : lista) {
				if (m.getUid() == idMovimento) {
					movimento = m;
				}
			}
			if (movimento.getStatoOperativoModificaMovimentoGestione() != null) {
				if (movimento.getStatoOperativoModificaMovimentoGestione()
						.equals(StatoOperativoModificaMovimentoGestione.ANNULLATO)) {
					abilitato = false;
				} else {
					abilitato = true;
				}
			} else {
				abilitato = true;
			}

			return abilitato;

		} else {
			List<ModificaMovimentoGestioneEntrata> lista = model.getMovimentoSpesaModel().getListaModificheEntrata();
			ModificaMovimentoGestioneEntrata movimento = new ModificaMovimentoGestioneEntrata();
			for (ModificaMovimentoGestioneEntrata m : lista) {
				if (m.getUid() == idMovimento) {
					movimento = m;
				}
			}
			if (movimento.getStatoOperativoModificaMovimentoGestione() != null) {
				if (movimento.getStatoOperativoModificaMovimentoGestione()
						.equals(StatoOperativoModificaMovimentoGestione.ANNULLATO)) {
					abilitato = false;
				} else {
					abilitato = true;
				}
			} else {
				abilitato = true;
			}
			return abilitato;
		}
	}

	protected boolean isValorizzato(Soggetto sogg) {
		boolean valorizzato = false;
		if (sogg != null && sogg.getUid() != 0) {
			return true;
		}
		return valorizzato;
	}

	protected boolean isValorizzato(ClasseSoggetto sogg) {
		boolean valorizzato = false;
		if (sogg != null && sogg.getCodice() != null) {
			return true;
		}
		return valorizzato;
	}

	/**
	 * Setta i dati indicati in provvedimento dentro al model della pagina
	 * 
	 * @param provvedimento
	 */
	private void caricaDatiProvvedimento(AttoAmministrativo provvedimento) {
		// ISTANZIO L'OGGETTO PROVVEDIMENTO NEL MODEL:
		model.getStep1ModelSubimpegno().setProvvedimento(new ProvvedimentoImpegnoModel());
		// TRAVASIAMO I DATI DALL'OGGETTO ATTO AMMINISTRATIVO AL MODEL DELLA
		// PAGINA:
		impostaProvvNelModel(provvedimento, model.getStep1ModelSubimpegno().getProvvedimento());
		// MARCHIAMO CHE C'E' UN PROVVEDIMENTO SELEZIONATO:
		model.getStep1ModelSubimpegno().setProvvedimentoSelezionato(true);
	}

	protected <T extends MovimentoGestione> boolean presenteAlmenoUnMovValido(List<T> elencoSubMovimenti, String tipo) {
		boolean ris = false;
		if (elencoSubMovimenti != null) {
			if (tipo.equals(OggettoDaPopolareEnum.SUBIMPEGNO.toString())) {
				for (int i = 0; i < elencoSubMovimenti.size(); i++) {
					if (!((SubImpegno) elencoSubMovimenti.get(i)).getStatoOperativoMovimentoGestioneSpesa()
							.equals(CostantiFin.MOVGEST_STATO_ANNULLATO)) {
						ris = true;
						break;
					}
				}
			} else if (tipo.equals(OggettoDaPopolareEnum.SUBACCERTAMENTO.toString())) {
				for (int i = 0; i < elencoSubMovimenti.size(); i++) {
					if (!((SubAccertamento) elencoSubMovimenti.get(i)).getStatoOperativoMovimentoGestioneEntrata()
							.equals(CostantiFin.MOVGEST_STATO_ANNULLATO)) {
						ris = true;
						break;
					}
				}
			}
		}
		return ris;
	}

	// siac-3224
	public String consultaModificheProvvedimento() throws Exception {
		setMethodName("consultaModificheProvvedimento");
		MovimentoGestione sub = null;

		if (oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBIMPEGNO)) {
			sub = FinUtility.getById(model.getImpegnoInAggiornamento().getElencoSubImpegni(),
					model.getStep1ModelSubimpegno().getUid());
		} else if (oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBACCERTAMENTO)) {
			sub = FinUtility.getById(model.getAccertamentoInAggiornamento().getElencoSubAccertamenti(),
					model.getStep1ModelSubimpegno().getUid());
		}

		leggiStoricoProvvedimentoByMovimento(sub);

		return "consultaModificheProvvedimento";
	}
	
	
	//SIAC-6997
	public String consultaModificheStrutturaCompetente() throws Exception {
		setMethodName("consultaModificheStrutturaCompetente");
		MovimentoGestione sub = null;

		if (oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBIMPEGNO)) {
			sub = FinUtility.getById(model.getImpegnoInAggiornamento().getElencoSubImpegni(),
					model.getStep1ModelSubimpegno().getUid());
		} else if (oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBACCERTAMENTO)) {
			sub = FinUtility.getById(model.getAccertamentoInAggiornamento().getElencoSubAccertamenti(),
					model.getStep1ModelSubimpegno().getUid());
		}

		leggiStoricoStrutturaCompetenteByMovimento(sub);

		return "consultaModificheStrutturaCompetente";
	}

	protected List<Errore> controlliReimputazioneEMotivoInAggiornamento(List<Errore> listaErrori, String tipoMov,
			String tipoMotivo) {

		String reimputazione = model.getMovimentoSpesaModel().getReimputazione();

		Bilancio bilancio = this.sessionHandler.getBilancio();
		int annoBilancio = bilancio.getAnno();

		if (listaErrori == null) {
			listaErrori = new ArrayList<Errore>();
		}

		// controlli su reimputazione:
		if (WebAppConstants.Si.equals(reimputazione)) {

			// se selezionato si, deve essere compilato l'anno:
			if (model.getMovimentoSpesaModel().getAnnoReimputazione() == null
					|| model.getMovimentoSpesaModel().getAnnoReimputazione() <= 0) {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Reimputazione"));
			} else if (model.getMovimentoSpesaModel().getAnnoReimputazione().intValue() <= Integer
					.valueOf(sessionHandler.getAnnoEsercizio())) {
				// inoltre l'anno di reimputazione deve essere maggiore
				// dell'anno di bilancio:
				listaErrori.add(ErroreFin.VALORE_NON_VALIDO.getErrore("Anno Reimputazione",
						"(deve essere maggiore dell'anno di bilancio)"));
			}

			// in caso di reimputazione, l'importo della modifica puo' essere
			// solo negativo:
			BigDecimal importo = model.getMovimentoSpesaModel().getModificaAggiornamento().getImportoOld();
			// nel caso importo fosse nullo scattera' gia' il controllo piu'
			// avanti sulla sua obbligatorieta'
			if (importo != null && importo.compareTo(BigDecimal.ZERO) >= 0) {
				listaErrori.add(ErroreFin.REIMPUTAZIONE_RESIDUI_SU_MODIFICA_IN_AUMENTO.getErrore());
			}

			if (tipoMotivo.equalsIgnoreCase("CROR")) {
				// Se Motivo = Cancellazione per ROR inviare un messaggio di
				// errore FIN_ERR_0129 - Reimputazione non compatibile con
				// cancellazione, 'Impegno'
				// (o accertamento)
				Errore errore = tipoMov.equals("Spesa")
						? ErroreFin.REIMPUTAZIONE_NON_COMPATIBILE_CON_CANCELLAZIONE_IMP.getErrore()
						: ErroreFin.REIMPUTAZIONE_NON_COMPATIBILE_CON_CANCELLAZIONE_ACC.getErrore();
				listaErrori.add(errore);
			}

			// SIAC-6582
			if (annoBilancio > getMovimentoInAggiornamento().getAnnoMovimento()) {
				// se anno Impegno < anno di bilancio FIN_ERR_0129 -
				// Reimputazione residui non ammesso, 'Impegno'
				// (o accertamento)
				Errore errore = tipoMov.equals("Spesa") ? ErroreFin.REIMPUTAZIONE_RESIDUI_NON_AMMESSO_IMP.getErrore()
						: ErroreFin.REIMPUTAZIONE_RESIDUI_NON_AMMESSO_ACC.getErrore();
				listaErrori.add(errore);
			}

		}else{
			//SIAC-6997: [...ElaboratoRor...]Se REIMPUTAZIONE non settata deve essere nullo e non visualizzato
			model.getMovimentoSpesaModel().setElaboratoRor(null);
		}

		if (tipoMotivo.equalsIgnoreCase(CODICE_MOTIVO_RIACCERTAMENTO)) {

			// Se Motivo = RIACCERTAMENTO

			if (isBilancioAttualeInPredisposizioneConsuntivo()) {

				// se il bilancio in stato Predisposizione Consuntivo inviare
				// un messaggio di errore FIN_ERR_0129 - Riaccertamento residui
				// non ammesso in predisposizione consuntivo, 'Impegno'
				// (o Accertamento)
				Errore errore = tipoMov.equals("Spesa")
						? ErroreFin.RIACCERTAMENTO_RESIDUI_NON_AMMESSO_IN_PREDISPOSIZIONE_CONSUNTIVO_IMP.getErrore()
						: ErroreFin.RIACCERTAMENTO_RESIDUI_NON_AMMESSO_IN_PREDISPOSIZIONE_CONSUNTIVO_ACC.getErrore();
				listaErrori.add(errore);
			}

			if (WebAppConstants.Si.equals(reimputazione)) {

				// se REIMPUTAZIONE = TRUE inviare un messaggio di errore
				// FIN_ERR_0129 - Riaccertamento residui non ammesso
				// contemporaneamente alla reimputazione, 'Impegno'
				// (o accertamento)
				Errore errore = tipoMov.equals("Spesa")
						? ErroreFin.RIACCERTAMENTO_RESIDUI_NON_AMMESSO_ASSIEME_REIMPUTAZIONE_IMP.getErrore()
						: ErroreFin.RIACCERTAMENTO_RESIDUI_NON_AMMESSO_ASSIEME_REIMPUTAZIONE_ACC.getErrore();
				listaErrori.add(errore);

			}

			// SIAC-6582
			if (annoBilancio > getMovimentoInAggiornamento().getAnnoMovimento()) {
				// se anno Impegno < anno di bilancio FIN_ERR_0129 -
				// Riaccertamento residui non ammesso, 'Impegno'
				// (o accertamento)
				Errore errore = tipoMov.equals("Spesa") ? ErroreFin.RIACCERTAMENTO_RESIDUI_NON_AMMESSO_IMP.getErrore()
						: ErroreFin.RIACCERTAMENTO_RESIDUI_NON_AMMESSO_ACC.getErrore();
				listaErrori.add(errore);
			}

		}

		return listaErrori;
	}

	protected List<Errore> controlliReimputazioneInInserimentoEMotivoRIAC(List<Errore> listaErrori, String tipoImpegno,
			String abbina, String importoImpegnoModImp, String tipoMotivo) {

		String reimputazione = model.getMovimentoSpesaModel().getReimputazione();

		Bilancio bilancio = this.sessionHandler.getBilancio();
		int annoBilancio = bilancio.getAnno();

		if (listaErrori == null) {
			listaErrori = new ArrayList<Errore>();
		}

		WebAppConstants.Si.equals(reimputazione);
		//SIAC-8826: permettere tipo reimp o reanno anche sul solo subaccertamento (senza modifica contestuale dell'accertamento)
		boolean modificaSulSubSenzaIndicarneLaTestata = (tipoImpegno.equals("SubImpegno")
				&& !"Modifica Anche Impegno".equalsIgnoreCase(abbina));
		//		|| (tipoImpegno.equals("SubAccertamento") && !"Modifica Anche Accertamento".equalsIgnoreCase(abbina));

		if (modificaSulSubSenzaIndicarneLaTestata && WebAppConstants.Si.equals(reimputazione)) {
			// La modifica e' sul sub, ma non e' stato selezionato anche
			// l'impegno, errore:
			//SIAC-8826: permettere tipo reimp o reanno anche sul solo subaccertamento (senza modifica contestuale dell'accertamento)
			//non entrer mai nel caso REIMPUTAZIONE_NON_PREVISTA_PER_I_SUB_ACC
			Errore errore = tipoImpegno.equals("SubImpegno")
					? ErroreFin.REIMPUTAZIONE_NON_PREVISTA_PER_I_SUB_IMP.getErrore()
					: ErroreFin.REIMPUTAZIONE_NON_PREVISTA_PER_I_SUB_ACC.getErrore();
			
			listaErrori.add(errore);
			return listaErrori;// questo errore maschera quelli dopo, perche'
								// tanto l'utente dovra' mettere reimputazione
								// no
		}

		// controlli su reimputazione:
		controlliSuReimputazione(listaErrori, tipoImpegno, importoImpegnoModImp, tipoMotivo, reimputazione,
				annoBilancio);

		controlliMotivoRiac(listaErrori, tipoImpegno, tipoMotivo, reimputazione, annoBilancio);

		return listaErrori;
	}

	/**
	 * @param listaErrori
	 * @param tipoImpegno
	 * @param tipoMotivo
	 * @param reimputazione
	 * @param annoBilancio
	 */
	private void controlliMotivoRiac(List<Errore> listaErrori, String tipoImpegno, String tipoMotivo,
			String reimputazione, int annoBilancio) {
		if (!tipoMotivo.equalsIgnoreCase(CODICE_MOTIVO_RIACCERTAMENTO)) {
			return;
		}

		// Se Motivo = RIACCERTAMENTO
		if (isBilancioAttualeInPredisposizioneConsuntivo()) {

			// se il bilancio in stato Predisposizione Consuntivo inviare
			// un messaggio di errore FIN_ERR_0129 - Riaccertamento residui non
			// ammesso in predisposizione consuntivo, 'Impegno'
			// (o Accertamento)

			if (tipoImpegno.equals("SubImpegno") || tipoImpegno.equals("Impegno")) {
				listaErrori.add(
						ErroreFin.RIACCERTAMENTO_RESIDUI_NON_AMMESSO_IN_PREDISPOSIZIONE_CONSUNTIVO_IMP.getErrore());
			} else if (tipoImpegno.equals("SubAccertamento") || tipoImpegno.equals("Accertamento")) {
				listaErrori.add(
						ErroreFin.RIACCERTAMENTO_RESIDUI_NON_AMMESSO_IN_PREDISPOSIZIONE_CONSUNTIVO_ACC.getErrore());
			}
		}

		if (tipoImpegno.equals("SubImpegno") || tipoImpegno.equals("Impegno")) {
			// SIAC-6582
			if (annoBilancio > model.getImpegnoInAggiornamento().getAnnoMovimento()) {
				// se anno Impegno < anno di bilancio FIN_ERR_0129 -
				// Riaccertamento residui non ammesso, 'Impegno'
				listaErrori.add(ErroreFin.RIACCERTAMENTO_RESIDUI_NON_AMMESSO_IMP.getErrore());
			}
		} else if (tipoImpegno.equals("SubAccertamento") || tipoImpegno.equals("Accertamento")) {
			// SIAC-6582
			if (annoBilancio > model.getAccertamentoInAggiornamento().getAnnoMovimento()) {
				// se anno Impegno < anno di bilancio FIN_ERR_0129 -
				// Riaccertamento residui non ammesso, 'Accertamento'
				listaErrori.add(ErroreFin.RIACCERTAMENTO_RESIDUI_NON_AMMESSO_ACC.getErrore());
			}
		}

	}

	/**
	 * @param listaErrori
	 * @param tipoImpegno
	 * @param importoImpegnoModImp
	 * @param tipoMotivo
	 * @param reimputazione
	 * @param annoBilancio
	 */
	private void controlliSuReimputazione(List<Errore> listaErrori, String tipoImpegno, String importoImpegnoModImp,
			String tipoMotivo, String reimputazione, int annoBilancio) {
		if (!WebAppConstants.Si.equals(reimputazione)) {
			// non e' una reimputazione, non devo effettuare i controlli sulla
			// reimputazione
			return;
		}

		// se selezionato si, deve essere compilato l'anno:
		if (model.getMovimentoSpesaModel().getAnnoReimputazione() == null
				|| model.getMovimentoSpesaModel().getAnnoReimputazione() <= 0) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Reimputazione"));
		} else if (model.getMovimentoSpesaModel().getAnnoReimputazione().intValue() <= Integer
				.valueOf(sessionHandler.getAnnoEsercizio())) {
			// inoltre l'anno di reimputazione deve essere maggiore dell'anno di
			// bilancio:
			listaErrori.add(ErroreFin.VALORE_NON_VALIDO.getErrore("Anno Reimputazione",
					"(deve essere maggiore dell'anno di bilancio)"));
		}

		// in caso di reimputazione, l'importo della modifica puo' essere solo
		// negativo:
		if (!StringUtils.isEmpty(importoImpegnoModImp)
				&& NumberUtils.isNumber(importoImpegnoModImp.replace(".", "").replace(",", "").replace("-", ""))) {
			BigDecimal importoDiInput = convertiImportoToBigDecimal(importoImpegnoModImp);
			// nel caso importo fosse nullo scattera' gia' il controllo piu'
			// avanti sulla sua obbligatorieta'
			if (importoDiInput.compareTo(BigDecimal.ZERO) >= 0) {
				listaErrori.add(ErroreFin.REIMPUTAZIONE_RESIDUI_SU_MODIFICA_IN_AUMENTO.getErrore());
			}
		}

		boolean isMovimentoGestioneSpesa = tipoImpegno.equals("Impegno") || tipoImpegno.equals("SubImpegno");
		boolean isMovimentoGestioneEntrata = tipoImpegno.equals("SubAccertamento")
				|| tipoImpegno.equals("Accertamento");
		if (!isMovimentoGestioneSpesa && !isMovimentoGestioneEntrata) {
			// questo non dovrebbe mai capitare, messo per analogia con il
			// comportamento precedente
			return;
		}

		if (tipoMotivo.equalsIgnoreCase("CROR")) {
			// Se Motivo = Cancellazione per ROR inviare un messaggio di errore
			// FIN_ERR_0129 - Reimputazione non compatibile con cancellazione,
			// 'Impegno'
			// (o accertamento)
			Errore erroreDaLanciare = isMovimentoGestioneSpesa
					? ErroreFin.REIMPUTAZIONE_NON_COMPATIBILE_CON_CANCELLAZIONE_IMP.getErrore()
					: ErroreFin.REIMPUTAZIONE_NON_COMPATIBILE_CON_CANCELLAZIONE_ACC.getErrore();
			listaErrori.add(erroreDaLanciare);
		}

		if (tipoMotivo.equalsIgnoreCase(CODICE_MOTIVO_RIACCERTAMENTO)) {
			// se REIMPUTAZIONE = TRUE inviare un messaggio di errore
			// FIN_ERR_0129 - Riaccertamento residui non ammesso
			// contemporaneamente alla reimputazione, 'Impegno'
			// (o accertamento)
			Errore erroreDaLanciare = isMovimentoGestioneSpesa
					? ErroreFin.RIACCERTAMENTO_RESIDUI_NON_AMMESSO_ASSIEME_REIMPUTAZIONE_IMP.getErrore()
					: ErroreFin.RIACCERTAMENTO_RESIDUI_NON_AMMESSO_ASSIEME_REIMPUTAZIONE_ACC.getErrore();
			listaErrori.add(erroreDaLanciare);
		}

		if (annoBilancio > getMovimentoInAggiornamento().getAnnoMovimento()) {
			Errore erroreDaLanciare = isMovimentoGestioneSpesa
					? ErroreFin.REIMPUTAZIONE_RESIDUI_NON_AMMESSO_IMP.getErrore()
					: ErroreFin.REIMPUTAZIONE_RESIDUI_NON_AMMESSO_ACC.getErrore();
			// se anno Impegno < anno di bilancio FIN_ERR_0129 - Reimputazione
			// residui non ammesso, 'Accertamento'
			listaErrori.add(erroreDaLanciare);
		}

	}

	protected void inizializzaReimputazioneInInserimentoNelModel() {
		// Reimputazione:
		model.getMovimentoSpesaModel().setReimputazione(WebAppConstants.No);
		model.getMovimentoSpesaModel().setAnnoReimputazione(null);
		model.getMovimentoSpesaModel().setDaReimputazione(buildListaSiNo());
	}

	//SIAC-6997
	protected void inizializzaElaboraRorReannoInInserimentoNelModel(){
		//Reanno:
		model.getMovimentoSpesaModel().setElaboratoRor(WebAppConstants.No);
		model.getMovimentoSpesaModel().setAnnoReimputazione(null);
		model.getMovimentoSpesaModel().setDaElaboratoRor(buildListaSiNo());
	}
	
	protected <MMG extends ModificaMovimentoGestione> MMG settaDatiReimputazioneDalModel(MMG spesa) {
		if (WebAppConstants.Si.equals(model.getMovimentoSpesaModel().getReimputazione())) {
			spesa.setReimputazione(true);
			spesa.setElaboraRorReanno(false);
			spesa.setAnnoReimputazione(model.getMovimentoSpesaModel().getAnnoReimputazione());
		} else {
			spesa.setReimputazione(false);
			spesa.setAnnoReimputazione(null);
		}
		return spesa;
	}

	protected <MMG extends ModificaMovimentoGestione> void inizializzaReimputazioneInAggiornamentoNelModel(MMG spesa) {
		// Reimputazione:
		if (spesa.isReimputazione()) {
			model.getMovimentoSpesaModel().setReimputazione(WebAppConstants.Si);
			if(spesa.isElaboraRorReanno())
				model.getMovimentoSpesaModel().setElaboratoRor(WebAppConstants.Si);
			else
				model.getMovimentoSpesaModel().setElaboratoRor(WebAppConstants.No);
		} else {
			model.getMovimentoSpesaModel().setReimputazione(WebAppConstants.No);
			model.getMovimentoSpesaModel().setElaboratoRor(WebAppConstants.No);
		}
		model.getMovimentoSpesaModel().setAnnoReimputazione(spesa.getAnnoReimputazione());
		model.getMovimentoSpesaModel().setDaReimputazione(buildListaSiNo());
		model.getMovimentoSpesaModel().setDaElaboratoRor(buildListaSiNo());
		//
	}

	protected List<Errore> controlloGestisciImpegnoDecentratoPerModifica(List<Errore> listaErrori) {
		if (isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_gestisciImpegnoDecentratoP)) {
			// il provveddimento deve essere provvisorio
			if (!MOVGEST_PROVVISORIO.equals(model.getStep1Model().getProvvedimento().getStato())
					|| model.getStep1Model().getProvvedimento().isParereRegolaritaContabile()) {
				listaErrori.add(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Modifica Impegno Decentrato",
						"Provvisorio"));
				model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
			}
		}
		return listaErrori;
	}
	
	
	//SIAC-6929 
	protected List<Errore> controlloGestisciAccertamentoDecentratoPerModifica(List<Errore> listaErrori) {

		return controlloGestisciAccertamentoDecentratoPerModifica(listaErrori, false);
	}
	
	//SIAC-6929 - aggiunto flag per messaggio corretto da Aggiorna o Inserisci
	protected List<Errore> controlloGestisciAccertamentoDecentratoPerModifica(List<Errore> listaErrori,
			boolean aggiornamento) {

		String messaggioErrore = "Modifica Accertamento Decentrato";
		if (aggiornamento)
			messaggioErrore = "Aggiornamento Accertamento Decentrato";// SIAC-6929

		// Controllo gestisci decentrato: il provveddimento deve essere
		// provvisorio
		if (isAzioneConsentita(AzioneConsentitaEnum.OP_ENT_gestisciAccertamentoDecentratoP)
				&& (!MOVGEST_PROVVISORIO.equals(model.getStep1Model().getProvvedimento().getStato())
						|| model.getStep1Model().getProvvedimento().isParereRegolaritaContabile())) {

			listaErrori.add(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore(messaggioErrore, "Provvisorio"));
			model.getStep1Model().setCheckproseguiMovimentoSpesa(false);
		}
		return listaErrori;
	}

	

	
	// SIAC-8563
	public String proseguiSalva() throws Exception {
		String ret = prosegui(true);
		return PROSEGUI.equals(ret) ? "proseguiSalva" : ret;
	}

}

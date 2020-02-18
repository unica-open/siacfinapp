/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.ProgettoService;
import it.csi.siac.siacbilser.frontend.webservice.VincoloCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaClassificatoreResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgettoResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.PerimetroSanitarioSpesa;
import it.csi.siac.siacbilser.model.PoliticheRegionaliUnitarie;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.RicorrenteSpesa;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.StatoOperativoProgetto;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaSpesa;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabile;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabileResponse;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;
import it.csi.siac.siacfinapp.frontend.ui.model.commons.GestoreDatiAlberoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.commons.GestoreTransazioneElementareModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciMovGestModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ImportiCapitoloModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProgettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.ValidationUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.codicefiscale.CFGenerator;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.CaricaDatiVisibilitaSacCapitolo;
import it.csi.siac.siacfinser.frontend.webservice.msg.CaricaDatiVisibilitaSacCapitoloResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggetto;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

public abstract class GenericPopupAction<M extends GenericPopupModel> extends GestoreTransazioneElementareAction<M> {

	private static final long serialVersionUID = -4825919870290768689L;
	
	@Autowired
	protected transient ProvvedimentoService provvedimentoService;
	
	@Autowired
	protected transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	
	@Autowired
	protected transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	
	@Autowired
	protected transient ClassificatoreBilService classificatoreBilService;	

	@Autowired
	protected transient MovimentoGestioneService movimentoGestionService;
	
	@Autowired
	protected transient SoggettoService soggettoService;
	
	@Autowired
	protected transient ProgettoService progettoService;
	
	@Autowired
	protected transient VincoloCapitoloService vincoloCapitoloService;
	
	private static final int MAX_RICERCA_PROGETTI_MODALE = 16;
	
	protected static final String NESSUNA_STRUTTURA_AMMINISTRATIVA = "Nessuna Struttura Amministrativa";
	
	private String radioCodiceProgetto;
	
	private int radioUidCronoprogramma;
	
	private String radioCodiceSoggetto;
	private String radioCodiceImpegno;
	private String radioCodiceCapitolo;
	private StatoOperativoAnagrafica statoSoggettoSelezionato;
	private String strutturaAmministrativaSelezionata;
	private String tipoStrutturaAmministrativaSelezionata;
	private String pdcSelezionato;
	private String radioCodiceProvvedimento;
	private String strutturaAmministrativaSelezionataPerOrdInc;
	
	protected String strutturaSelezionataSuPagina;


	private static final String MODALIMPEGNO = "refreshPopupModalImpegno";
	public abstract String listaClasseSoggettoChanged();
	public abstract String listaClasseSoggettoDueChanged();
	
	/**
	 * ricerca capitolo
	 * @return
	 * @throws Exception
	 */
	public String ricercaCapitolo() throws Exception {
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		//Verifichiamo che i dati obbligatori siano stati compilati:
		
		//NUMERO CAPITOLO
		if (model.getCapitoloRicerca().getNumCapitolo() == null || model.getCapitoloRicerca().getNumCapitolo() == 0) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Capitolo "));
		}
		
		//ARTICOLO
		if (model.getCapitoloRicerca().getArticolo() == null) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Articolo "));
		}
		if (listaErrori.isEmpty()) {
			//OK SONO STATI COMPILATI I DATI OBBLIGATORI, 
			//PROSEGUO ESEGUENDO LA RICERCA DEL CAPITOLO:
			eseguiRicercaCapitolo(model.getCapitoloRicerca(), true, oggettoDaPopolare);
		} else {
			//NON SONO STATI COMPILATI I DATI OBBLIGATORI
			addErrori(listaErrori);
		}
		return "ricercaCapitolo";
	}

	/**
	 * esegui ricerca capitolo uscita
	 * @param capitoloDiRiferimento
	 * @param daRicerca
	 * @return
	 */
	private boolean eseguiRicercaCapitoloUscita(CapitoloImpegnoModel capitoloDiRiferimento, boolean daRicerca) {
		
		//Invoco il metodo che si occupa di richiamare il servizio di ricerca:
		RicercaSinteticaCapitoloUscitaGestioneResponse respCapUsc = ricercaCapitoloUscitaDaServizio(capitoloDiRiferimento);
		//
		
		//pulisco la lista capitoli da ricerca nel model:
		model.setListaRicercaCapitolo(new ArrayList<CapitoloImpegnoModel>());
		
		if(!isFallimento(respCapUsc) && !isEmpty(respCapUsc.getCapitoli())){
			//response ok e trovato almeno un capitolo
			
			// se non arrivo da pop up e trovo + capitoli allora errore
			if(!daRicerca && respCapUsc.getCapitoli().size()>1){
				List<Errore> listaErrori = new ArrayList<Errore>();
				listaErrori.add(ErroreFin.PIU_RISULTATI__PROVV_TROVATI.getErrore(""));
				addErrori(listaErrori);
				return false;
			}
			
			CapitoloImpegnoModel supportCapitolo;
			for (CapitoloUscitaGestione currentCapitolo : respCapUsc.getCapitoli()) {
				
				//SETTO I DATI:
				supportCapitolo = new CapitoloImpegnoModel();
				
				//ANNO
				supportCapitolo.setAnno(capitoloDiRiferimento.getAnno());
				
				//NUMERO CAPITOLO
				supportCapitolo.setNumCapitolo(currentCapitolo.getNumeroCapitolo());
				
				//ARTICOLO
				supportCapitolo.setArticolo(currentCapitolo.getNumeroArticolo());
				
				//UEB
				supportCapitolo.setUeb(BigInteger.valueOf(currentCapitolo.getNumeroUEB()));
				
				//DESCRIZIONE
				supportCapitolo.setDescrizione(currentCapitolo.getDescrizione());
				
				//STATO
				supportCapitolo.setStatoCapitolo(currentCapitolo.getStato());
				
				//TIPO FINANZIAMENTO
				if(currentCapitolo.getTipoFinanziamento()!=null){
					supportCapitolo.setTipoFinanziamento(currentCapitolo.getTipoFinanziamento().getDescrizione());
				}	
				
				//TITOLO SPESA
				if (currentCapitolo.getTitoloSpesa() != null) {
					supportCapitolo.setClassificazione(currentCapitolo.getTitoloSpesa().getCodice() + " ");
					supportCapitolo.setTitoloSpesa(currentCapitolo.getTitoloSpesa().getCodice());
				}
				
				//MACRO AGGREGATO
				if (currentCapitolo.getMacroaggregato() != null) {
					supportCapitolo.setIdMacroAggregato(currentCapitolo.getMacroaggregato().getUid());
					supportCapitolo.setCodiceMacroAggregato(currentCapitolo.getMacroaggregato().getCodice());
					supportCapitolo.setClassificazione(supportCapitolo.getClassificazione() + currentCapitolo.getMacroaggregato().getCodice());
					if (!daRicerca) {
						teSupport.setIdMacroAggregato(currentCapitolo.getMacroaggregato().getUid());
					}
				}
				
				//STRUTTURA AMMINISTRATIVA CONTABILE
				if (currentCapitolo.getStrutturaAmministrativoContabile() != null) {
					supportCapitolo.setCodiceStrutturaAmministrativa(currentCapitolo.getStrutturaAmministrativoContabile().getCodice());
					supportCapitolo.setDescrizioneStrutturaAmministrativa(currentCapitolo.getStrutturaAmministrativoContabile().getDescrizione());
					supportCapitolo.setUidStruttura(currentCapitolo.getStrutturaAmministrativoContabile().getUid());
				}
				
				//ELEMENTO PIANO DEI CONTI
				if (currentCapitolo.getElementoPianoDeiConti() != null) {
					supportCapitolo.setCodicePdcFinanziario(currentCapitolo.getElementoPianoDeiConti().getCodice());
					// modifica e metto codice e descrizione
					supportCapitolo.setDescrizionePdcFinanziario(currentCapitolo.getElementoPianoDeiConti().getCodice()+" - "+currentCapitolo.getElementoPianoDeiConti().getDescrizione());
					supportCapitolo.setIdPianoDeiConti(currentCapitolo.getElementoPianoDeiConti().getUid());
					// Classificatore per capire il livello
					supportCapitolo.setTipoClassificatorePdc(currentCapitolo.getElementoPianoDeiConti().getTipoClassificatore());
				}
				
				//LISTA IMPORTI CAPITOLO
				if(!isEmpty(currentCapitolo.getListaImportiCapitolo())){
					supportCapitolo.setImportiCapitoloUG(currentCapitolo.getListaImportiCapitolo());
					ImportiCapitoloModel supportImporti;
					for (ImportiCapitoloUG currentImporto : currentCapitolo.getListaImportiCapitolo()) {
						supportImporti = new ImportiCapitoloModel();
						supportImporti.setAnnoCompetenza(currentImporto.getAnnoCompetenza());
						supportImporti.setCassa(currentImporto.getStanziamentoCassa());
						supportImporti.setResiduo(currentImporto.getStanziamentoResiduo());
						supportImporti.setCompetenza(currentImporto.getStanziamento());
						
						// con i nuovi servizi di BIL le disponibilita
						// degli anni successivi si trattano in altra maniera
						
						if (currentImporto.getAnnoCompetenza().intValue() == supportCapitolo.getAnno().intValue()) {
							supportCapitolo.setDisponibileAnno1(currentCapitolo.getListaImportiCapitoloUG().get(0).getDisponibilitaImpegnareAnno1());
						} else if (currentImporto.getAnnoCompetenza().intValue() == (supportCapitolo.getAnno().intValue() + 1)) {
							supportCapitolo.setDisponibileAnno2(currentCapitolo.getListaImportiCapitoloUG().get(0).getDisponibilitaImpegnareAnno2());
						} else {
							supportCapitolo.setDisponibileAnno3(currentCapitolo.getListaImportiCapitoloUG().get(0).getDisponibilitaImpegnareAnno3());
						}
						supportCapitolo.getImportiCapitolo().add(supportImporti);
					}
				}
				
				//MISSIONE
				if (currentCapitolo.getMissione() != null) {
					supportCapitolo.setCodiceMissione(currentCapitolo.getMissione().getCodice());
					supportCapitolo.setDescrizioneMissione(currentCapitolo.getMissione().getDescrizione());
				}
				
				//PROGRAMMA
				if (currentCapitolo.getProgramma() != null) {
					supportCapitolo.setCodiceProgramma(currentCapitolo.getProgramma().getCodice());
					supportCapitolo.setDescrizioneProgramma(currentCapitolo.getProgramma().getDescrizione());
					supportCapitolo.setIdProgramma(currentCapitolo.getProgramma().getUid());
				}
				
				//UID
				supportCapitolo.setUid(currentCapitolo.getUid());
				
				// prevalorizzazione dati transazione elementare
				
				// codifica transazione europea
				if(null!=currentCapitolo.getTransazioneUnioneEuropeaSpesa()){
					supportCapitolo.setCodiceTransazioneEuropeaSpesa(currentCapitolo.getTransazioneUnioneEuropeaSpesa().getCodice());
				}
				
				// cofog
				if(null!=currentCapitolo.getClassificazioneCofog()){
					supportCapitolo.setCodiceClassificazioneCofog(currentCapitolo.getClassificazioneCofog().getCodice());
				}
				// ricorrente spesa
				if(null!=currentCapitolo.getRicorrenteSpesa()){
					supportCapitolo.setCodiceRicorrenteSpesa(currentCapitolo.getRicorrenteSpesa().getCodice());
				}
				
				// Perimetro sanitario spesa
				if(null!=currentCapitolo.getPerimetroSanitarioSpesa()){
					supportCapitolo.setCodicePerimetroSanitarioSpesa(currentCapitolo.getPerimetroSanitarioSpesa().getCodice());
				}
				
				// politiche regionali sanitarie
				if(null!=currentCapitolo.getPoliticheRegionaliUnitarie()){
					supportCapitolo.setCodicePoliticheRegionaliUnitarie(currentCapitolo.getPoliticheRegionaliUnitarie().getCodice());
				}
				
				//SIOPE SPESA:
				SiopeSpesa siopeSpesa = currentCapitolo.getSiopeSpesa();
				if(siopeSpesa!= null && !isEmpty(siopeSpesa.getCodice())){
					supportCapitolo.setCodiceSiopeSpesa(siopeSpesa.getCodice());
				}
				
				// flag impegnabile
				if(currentCapitolo.getFlagImpegnabile()!=null){
					supportCapitolo.setFlagImpegnabile(currentCapitolo.getFlagImpegnabile().booleanValue());
				}else{
					supportCapitolo.setFlagImpegnabile(false);
				}
				
				// VINCOLI DEL CAPITOLO:
				supportCapitolo.setListaVincoliCapitoloUEGest(currentCapitolo.getListaVincoliUEGest());
				//
				
				//CLASSIFICATORI GENERICI:
				supportCapitolo.setClassificatoriGenerici(currentCapitolo.getClassificatoriGenerici());
				//
				
				//TITOLO SPESA NELLA CLASSIFICAZIONE:
				if (currentCapitolo.getTitoloSpesa() != null) {
					supportCapitolo.setClassificazione(currentCapitolo.getTitoloSpesa().getCodice() + " ");
				}
				
				model.setCapitoloModelTrovatoDaServizio(supportCapitolo);
				
				if (daRicerca) {
					model.getListaRicercaCapitolo().add(supportCapitolo);
				} else {
					if (controlloSACUtenteDecentrato(sessionHandler.getAzione().getNome(), supportCapitolo)) {
						//Se sono nell'ordinativo, devo caricare subito gli eventuali dati della transazione economica
						if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO) 
								&& !supportCapitolo.getStatoCapitolo().equals(StatoEntita.VALIDO)){
							//controllo stato capitolo, ISSUE di laura
							addErrore(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Capitolo",supportCapitolo.getStatoCapitolo().toString() ));
							return false;
						}
						setCapitoloSelezionato(supportCapitolo);
					} else {
						List<Errore> listaErrori = new ArrayList<Errore>();
						listaErrori.add(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Selezione capitolo", "la struttura amministrativa del capitolo non e' compatibile con le strutture amministrative dell'utente"));
						addErrori(listaErrori);
						return false;
					}
					
					// requisito di impegnabilita'
					if(!supportCapitolo.isFlagImpegnabile()){
						addErrore(ErroreCore.DATE_INCONGRUENTI.getErrore(": il capitolo indicato non risulta impegnabile"));
						return false;
					}
				}
			}
			if (daRicerca) {
				model.setCapitoloTrovato(true);
			}
			return true;
		} else {
			if (daRicerca) {
				addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
			} else {
				setErroreCapitolo();
			}
			model.setCapitoloTrovato(false);
			return false;
		}
	}
	
	protected abstract void setCapitoloSelezionato(CapitoloImpegnoModel supportCapitolo);
	protected abstract void setErroreCapitolo();

	/**
	 * esegui ricerca capitolo entrata
	 * @param capitoloDiRiferimento
	 * @param daRicerca
	 * @return
	 */
	private boolean eseguiRicercaCapitoloEntrata(CapitoloImpegnoModel capitoloDiRiferimento, boolean daRicerca){
		
		//Invoco il metodo che si occupa di richiamare il servizio di ricerca:
		RicercaSinteticaCapitoloEntrataGestioneResponse respCapEnt = ricercaCapitoloEntrataDaServizio(capitoloDiRiferimento);
		//
		
		//pulisco la lista capitoli da ricerca nel model:
		model.setListaRicercaCapitolo(new ArrayList<CapitoloImpegnoModel>());
		//
		
		if(!isFallimento(respCapEnt) && !isEmpty(respCapEnt.getCapitoli())){
			//response ok e trovato almeno un capitolo
			
			// se non arrivo da pop up e trovo + capitoli allora errore
			if(!daRicerca && respCapEnt.getCapitoli().size()>1){
				List<Errore> listaErrori = new ArrayList<Errore>();
				listaErrori.add(ErroreFin.PIU_RISULTATI__PROVV_TROVATI.getErrore(""));
				addErrori(listaErrori);
				return false;
			}
			
			CapitoloImpegnoModel supportCapitolo;
			for (CapitoloEntrataGestione currentCapitolo : respCapEnt.getCapitoli()) {
				supportCapitolo = new CapitoloImpegnoModel();
				
				//ANNO
				supportCapitolo.setAnno(capitoloDiRiferimento.getAnno());
				
				//NUMERO
				supportCapitolo.setNumCapitolo(currentCapitolo.getNumeroCapitolo());
				
				//ARTICOLO
				supportCapitolo.setArticolo(currentCapitolo.getNumeroArticolo());
				
				//UEB
				supportCapitolo.setUeb(BigInteger.valueOf(currentCapitolo.getNumeroUEB()));
				
				//DESCRIZIONE
				supportCapitolo.setDescrizione(currentCapitolo.getDescrizione());
				
				//STATO OPERATIVO
				supportCapitolo.setStatoCapitolo(currentCapitolo.getStato());
				
				// Tipo finanziamento
				if(currentCapitolo.getTipoFinanziamento()!=null){
					supportCapitolo.setTipoFinanziamento(currentCapitolo.getTipoFinanziamento().getDescrizione());
				}	
				
				//TITOLO ENTRATA
				if (currentCapitolo.getTitoloEntrata() != null) {
					supportCapitolo.setClassificazione(currentCapitolo.getTitoloEntrata().getCodice() + " ");
					supportCapitolo.setTitoloEntrata(currentCapitolo.getTitoloEntrata().getCodice()); // questo valore lo uso nel controllo della generale (CR-552)
				}
				
				//CATEGORIA TIPOLOGIA TITOLO
				if (currentCapitolo.getCategoriaTipologiaTitolo() != null) {
					
					supportCapitolo.setTipologia(currentCapitolo.getCategoriaTipologiaTitolo().getCodice());
					
					supportCapitolo.setIdMacroAggregato(currentCapitolo.getCategoriaTipologiaTitolo().getUid());
					supportCapitolo.setClassificazione(supportCapitolo.getClassificazione() + currentCapitolo.getCategoriaTipologiaTitolo().getCodice());
					if (!daRicerca) {
						teSupport.setIdMacroAggregato(currentCapitolo.getCategoriaTipologiaTitolo().getUid());
					}
				}
				
				//STRUTTURA AMMINISTRATIVO CONTABILE
				if (currentCapitolo.getStrutturaAmministrativoContabile() != null) {
					supportCapitolo.setCodiceStrutturaAmministrativa(currentCapitolo.getStrutturaAmministrativoContabile().getCodice());
					supportCapitolo.setDescrizioneStrutturaAmministrativa(currentCapitolo.getStrutturaAmministrativoContabile().getDescrizione());
					supportCapitolo.setUidStruttura(currentCapitolo.getStrutturaAmministrativoContabile().getUid());
				}
				
				//ELEMENTO PIANO DEI CONTI
				if (currentCapitolo.getElementoPianoDeiConti() != null) {
					supportCapitolo.setCodicePdcFinanziario(currentCapitolo.getElementoPianoDeiConti().getCodice());
					// modifica e metto codice e descrizione
					supportCapitolo.setDescrizionePdcFinanziario(currentCapitolo.getElementoPianoDeiConti().getCodice() + "-"+currentCapitolo.getElementoPianoDeiConti().getDescrizione());
					supportCapitolo.setIdPianoDeiConti(currentCapitolo.getElementoPianoDeiConti().getUid());
					// tipoclassificatore per capire il livello
					supportCapitolo.setTipoClassificatorePdc(currentCapitolo.getElementoPianoDeiConti().getTipoClassificatore());
				}
				
				//LISTA IMPORTI CAPITOLO
				if(!isEmpty(currentCapitolo.getListaImportiCapitolo())){
					supportCapitolo.setImportiCapitoloEG(currentCapitolo.getListaImportiCapitolo());
					ImportiCapitoloModel supportImporti;
					for (ImportiCapitoloEG currentImporto : currentCapitolo.getListaImportiCapitolo()) {
						supportImporti = new ImportiCapitoloModel();
						supportImporti.setAnnoCompetenza(currentImporto.getAnnoCompetenza());
						supportImporti.setCassa(currentImporto.getStanziamentoCassa());
						supportImporti.setResiduo(currentImporto.getStanziamentoResiduo());
						supportImporti.setCompetenza(currentImporto.getStanziamento());
						
						// con i nuovi servizi di BIL le disponibilita
						// degli anni successivi si trattano in altra maniera
						if (currentImporto.getAnnoCompetenza().intValue() == supportCapitolo.getAnno().intValue()) {
							supportCapitolo.setDisponibileAnno1(currentCapitolo.getListaImportiCapitoloEG().get(0).getDisponibilitaAccertareAnno1());
						} else if (currentImporto.getAnnoCompetenza().intValue() == (supportCapitolo.getAnno().intValue() + 1)) {
							supportCapitolo.setDisponibileAnno2(currentCapitolo.getListaImportiCapitoloEG().get(0).getDisponibilitaAccertareAnno2());
						} else {
							supportCapitolo.setDisponibileAnno3(currentCapitolo.getListaImportiCapitoloEG().get(0).getDisponibilitaAccertareAnno3());
						}
						supportCapitolo.getImportiCapitolo().add(supportImporti);
					}
				}
				
				//UID
				supportCapitolo.setUid(currentCapitolo.getUid());
				
				// prevalorizzazione dati transazione elementare
				
				// codifica transazione europea
				if(null!=currentCapitolo.getTransazioneUnioneEuropeaEntrata()){
					supportCapitolo.setCodiceTransazioneEuropeaEntrata(currentCapitolo.getTransazioneUnioneEuropeaEntrata().getCodice());
				}
				
				// ricorrente entrata
				if(null!=currentCapitolo.getRicorrenteEntrata()){
					supportCapitolo.setCodiceRicorrenteEntrata(currentCapitolo.getRicorrenteEntrata().getCodice());
				}
				
				// Perimetro sanitario entrata
				if(null!=currentCapitolo.getPerimetroSanitarioEntrata()){
					supportCapitolo.setCodicePerimetroSanitarioEntrata(currentCapitolo.getPerimetroSanitarioEntrata().getCodice());
				}
				
				// flag accertabile
				if(currentCapitolo.getFlagImpegnabile()!=null){
					supportCapitolo.setFlagImpegnabile(currentCapitolo.getFlagImpegnabile().booleanValue());
				}else{
					supportCapitolo.setFlagImpegnabile(false);
				}
				
				//SIOPE ENTRATA:
				SiopeEntrata siopeEntrata = currentCapitolo.getSiopeEntrata();
				if(siopeEntrata != null && !isEmpty(siopeEntrata.getCodice())){
					supportCapitolo.setCodiceSiopeSpesa(siopeEntrata.getCodice());
				}
				
				// VINCOLI DEL CAPITOLO:
				supportCapitolo.setListaVincoliCapitoloUEGest(currentCapitolo.getListaVincoliUEGestione());
				//
				
				
				model.setCapitoloModelTrovatoDaServizio(supportCapitolo);
				
				if (daRicerca) {
					model.getListaRicercaCapitolo().add(supportCapitolo);
				} else {
					
					if (controlloSACUtenteDecentrato(sessionHandler.getAzione().getNome(), supportCapitolo)) {
						
						if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_INCASSO) 
								&& !supportCapitolo.getStatoCapitolo().equals(StatoEntita.VALIDO)){
							//controllo stato capitolo, ISSUE di laura
							addErrore(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Capitolo",supportCapitolo.getStatoCapitolo().toString() ));
							return false;
						}
						
						setCapitoloSelezionato(supportCapitolo);
						
					} else {
						List<Errore> listaErrori = new ArrayList<Errore>();
						listaErrori.add(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Selezione capitolo", "la struttura amministrativa del capitolo non e' compatibile con le strutture amministrative dell'utente"));
						addErrori(listaErrori);
						return false;
					}
					
					// flag di accertabilita'
					if(!supportCapitolo.isFlagImpegnabile()){
						addErrore(ErroreCore.DATE_INCONGRUENTI.getErrore(": il capitolo indicato non risulta accertabile"));
						return false;
					}
					
				}
			}
			if (daRicerca) {
				model.setCapitoloTrovato(true);
			}
			return true;
		} else {
			if (daRicerca) {
				addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
			} else {
				setErroreCapitolo();
			}
			model.setCapitoloTrovato(false);
			return false;
		}
	}
	
	/**
	 * esegui ricerca capitolo
	 * @param capitoloDiRiferimento
	 * @param daRicerca
	 * @param oggettoDaPopolare
	 * @return
	 */
	protected boolean eseguiRicercaCapitolo(CapitoloImpegnoModel capitoloDiRiferimento, boolean daRicerca, OggettoDaPopolareEnum oggettoDaPopolare) {
		/*
		 * USCITA: 
		 * - impegno
		 * - liquidazione
		 * - ordinativo pagamento
		 * - carta contabile
		 * 
		 */
		if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO) || oggettoDaPopolare.equals(OggettoDaPopolareEnum.LIQUIDAZIONE) || oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO) ||  oggettoDaPopolare.equals(OggettoDaPopolareEnum.CARTA)){
			return eseguiRicercaCapitoloUscita(capitoloDiRiferimento, daRicerca);
		}else{
			return eseguiRicercaCapitoloEntrata(capitoloDiRiferimento, daRicerca);
		}
	}
	
	/**
	 * gestione della selezione di un capitolo
	 * @return
	 */
	public String selezionaCapitolo() {		
		List<Errore> listaErrori = new ArrayList<Errore>();
		if(isEmpty(getRadioCodiceCapitolo())){
			//NON E' STATO SELEZIONATO NESSUN CAPITOLO .. 
			if (!isEmpty(model.getListaRicercaCapitolo())) { 
				// .. E SE CI SONO CAPITOLI IN LISTA RICERCA:
				//Se non viene selezionato alcun soggetto, mostro l'errore
				addActionError("Elemento non selezionato");
				addErrori(listaErrori);
				model.setCapitoloTrovato(false);
				model.setListaRicercaCapitolo(null);
			}
			return INPUT;
		} else {
			//IL CAPITOLO E' STATO SELEZIONATO (lista ricerca capitolo ha per forza elementi)
			for(CapitoloImpegnoModel currentCapitolo: model.getListaRicercaCapitolo()){				
				if (currentCapitolo.getUid().toString().equalsIgnoreCase(getRadioCodiceCapitolo())) {
					if (controlloSACUtenteDecentrato(sessionHandler.getAzione().getNome(), currentCapitolo)) {
						// in caso di ORDINATIVO PAGAMENTO 
						// devo prepopolare i valori della transazione elementare
						if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO) || oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_INCASSO)){
							//Se lo stato e' diverso da null ritorna errore
							if(!currentCapitolo.getStatoCapitolo().equals(StatoEntita.VALIDO)){
								addErrore(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Capitolo",currentCapitolo.getStatoCapitolo().toString() ));
								return INPUT;
							}
	                          // lista missione
							  caricaMissione(currentCapitolo);
							  caricaProgramma(currentCapitolo);
							
							  // missione
							  teSupport.setMissioneSelezionata(currentCapitolo.getCodiceMissione()); 
							  // programma
							  teSupport.setProgrammaSelezionato(currentCapitolo.getCodiceProgramma());
							  // piano dei conti
							  teSupport.setPianoDeiConti(new ElementoPianoDeiConti());
							  teSupport.getPianoDeiConti().setCodice(currentCapitolo.getCodicePdcFinanziario());
							  teSupport.getPianoDeiConti().setDescrizione(currentCapitolo.getDescrizionePdcFinanziario());
							  teSupport.getPianoDeiConti().setUid(currentCapitolo.getIdPianoDeiConti());
							  
							  // cofog
							  teSupport.setCofogSelezionato(currentCapitolo.getCodiceClassificazioneCofog());
							  
							  // codice transazione europea
							  teSupport.setTransazioneEuropeaSelezionato(currentCapitolo.getCodiceTransazioneEuropeaSpesa());
							  // siope
							  
							  // cup
							  // ricorrente spesa
							  teSupport.setRicorrenteSpesaSelezionato(currentCapitolo.getCodiceRicorrenteSpesa());
							  
							  // capitolo sanitario
							  teSupport.setPerimetroSanitarioSpesaSelezionato(currentCapitolo.getCodicePerimetroSanitarioSpesa());
							  
							  // politiche regionali unitarie
							  teSupport.setPoliticaRegionaleSelezionato(currentCapitolo.getCodicePoliticheRegionaliUnitarie());
							
						}
						setCapitoloSelezionato(currentCapitolo);			
					} else {
						listaErrori.add(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Selezione capitolo", "la struttura amministrativa del capitolo non e' compatibile con le strutture amministrative dell'utente"));
						addErrori(listaErrori);
					}
				}
			}
			return SUCCESS;
		}
	}

	/**
	 * pulisci ricerca capitolo
	 * @return
	 */
	public String pulisciRicercaCapitolo() {
		model.setListaRicercaCapitolo(null);
		//setto capitoloTrovato a false cosi' ogni volta che vengono
		//ripuliti i risultati della ricerca viene reimpostata
		//la non visibilita' del blocco che li contiene
		model.setCapitoloTrovato(false);
		return "ricercaCapitolo";
	}
	
	/**
	 * pulisci ricerca impegno
	 * @return
	 */
	public String pulisciRicercaImpegno() {
		//setto capitoloTrovato a false cosi' ogni volta 
		//che vengono ripuliti i risultati della ricerca 
		//viene reimpostata la non visibilita' del blocco che li contiene
		model.setIsnSubImpegno(false);
		model.setHasImpegnoSelezionatoPopup(false);
		model.setListaVociMutuo(new ArrayList<VoceMutuo>());
		model.setHasMutui(false);
		model.setRadioDocumentoSelezionato(0);
		model.setListaRicercaDocumento(new ArrayList<DocumentoSpesa>());
		return "refreshPopupModalImpegno";
	}
	
	/**
	 * pulisci ricerca documento
	 * @return
	 */
	public String pulisciRicercaDocumento() {
		//setto capitoloTrovato a false cosi' 
		//ogni volta che vengono ripuliti i risultati 
		//della ricerca viene reimpostata la non visibilita'
		//del blocco che li contiene
		model.setIsnSubImpegno(false);
		model.setHasSubDocumentiSpesa(false);
		model.setAnnoDoc(null);
		model.setCodiceTipoDoc(null);
		model.setNumDoc(null);
		model.setRadioDocumentoSelezionato(0);
		model.setRadioSubDocumentoSelezionato(0);
		model.setListaImpegniCompGuidata(new ArrayList<Impegno>());
		return "refreshPopupModalDocumento";
	}
	
	/**
	 * visualizza dettaglio capitolo
	 * @return
	 */
	public String visualizzaDettaglioCapitolo() {
		if(!isEmpty(getRadioCodiceCapitolo())){
			//se selezionato lo cerco in lista:
			if(!isEmpty(model.getListaRicercaCapitolo())){
				for (CapitoloImpegnoModel currentCapitolo : model.getListaRicercaCapitolo()) {
					if (currentCapitolo.getUid().intValue() == Integer.valueOf(getRadioCodiceCapitolo()).intValue()) {
						model.setDatoPerVisualizza(currentCapitolo);
						break;
					}
				}
			}
		}else{
			addErrore(ErroreCore.NESSUN_ELEMENTO_SELEZIONATO.getErrore());
		}
		return "visualizzaCapitolo";
	}
	
	/**
	 * wrapper di retrocompatibilita', va sul primo soggetto
	 * @param soggettoRiferimento
	 * @param daRicerca
	 * @param oggettoDaPopolare
	 * @return
	 */
	protected boolean eseguiRicercaSoggetto(RicercaSoggetti soggettoRiferimento, boolean daRicerca, OggettoDaPopolareEnum oggettoDaPopolare) {
		return eseguiRicercaSoggetto(soggettoRiferimento, daRicerca, oggettoDaPopolare, SoggettoDaCercareEnum.UNO);
	}
	
	
	/**
	 *  metodo interno ad eseguiRicercaSoggetto
	 * @param modaleSoggetto
	 * @return
	 */
	private SoggettoImpegnoModel determinaModelSogg(SoggettoDaCercareEnum modaleSoggetto) {
		SoggettoImpegnoModel soggDiRif = null;
		switch (modaleSoggetto.getValue()) {
		case 1:
			soggDiRif = model.getSoggettoRicerca();
			break;
		case 2:
			soggDiRif = model.getSoggettoRicercaDue();
			break;
		default:
			soggDiRif = model.getSoggettoRicerca();
			break;
		}
		return soggDiRif;
	}
	
	/**
	 *  metodo interno ad eseguiRicercaSoggetto
	 * @param soggettoImpegnoModel
	 * @param modaleSoggetto
	 */
	private void settaSoggettoSelezionato(SoggettoImpegnoModel soggettoImpegnoModel,SoggettoDaCercareEnum modaleSoggetto){
		switch (modaleSoggetto.getValue()) {
		case 1:
			setSoggettoSelezionato(soggettoImpegnoModel);
			break;
		case 2:
			setSoggettoSelezionatoDue(soggettoImpegnoModel);
			break;
		default:
			setSoggettoSelezionato(soggettoImpegnoModel);
			break;
		}
	}
	
	/**
	 * esegui ricerca soggetto
	 * @param soggettoRiferimento
	 * @param daRicerca
	 * @param oggettoDaPopolare
	 * @return
	 */
	protected boolean eseguiRicercaSoggetto(RicercaSoggetti soggettoRiferimento, boolean daRicerca, OggettoDaPopolareEnum oggettoDaPopolare,SoggettoDaCercareEnum modaleSoggetto) {
		
		
		SoggettoImpegnoModel soggettoModelDiRiferimento = determinaModelSogg(modaleSoggetto);
		
		//PROVA JIRA numero 12
		if( !isEmpty(soggettoRiferimento.getParametroRicercaSoggetto().getCodiceFiscale())){
		CFGenerator cfGen = new CFGenerator();
		if (!(cfGen.verificaFormaleCodiceFiscale(soggettoRiferimento.getParametroRicercaSoggetto().getCodiceFiscale()) || cfGen.verificaFormaleCodiceFiscaleNumerico(soggettoRiferimento.getParametroRicercaSoggetto().getCodiceFiscale().trim()))) {
			addErrore(ErroreFin.FORMATO_NON_VALIDO_SECONDO.getErrore("Codice Fiscale", "codice fiscale"));
			model.setListaRicercaSoggetto(null);
			return false;
		}
		}
		//PROVA JIRA numero 12
		if(!isEmpty(soggettoRiferimento.getParametroRicercaSoggetto().getPartitaIva())
				&& soggettoRiferimento.getParametroRicercaSoggetto().getPartitaIva().length() < 11){
			addErrore(ErroreFin.FORMATO_NON_VALIDO.getErrore("Partita Iva", "Numerico"));
			model.setListaRicercaSoggetto(null);
			return false;
		}
		
		RicercaSoggettiResponse response = ricercaSoggettoDaServizio(soggettoRiferimento);
		if (isFallimento(response) || isEmpty(response.getSoggetti())) {
			if (daRicerca) {
				addErrori("eseguiRicercaSoggetto", response);
				if (response.getErrori().isEmpty()) {
					addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", soggettoModelDiRiferimento.getCodCreditore()));
				}
			} else {
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", soggettoRiferimento.getParametroRicercaSoggetto().getCodiceSoggetto()));
			}
			model.setSoggettoTrovato(false);
			return false;
		} else {
			if (daRicerca) {
				model.setSoggettoTrovato(true);
				model.setListaRicercaSoggetto(response.getSoggetti());
			} else {
				
				//JIRA 690 i mutui possono avere un soggetto in stato bloccato
				if(!oggettoDaPopolare.equals(OggettoDaPopolareEnum.MUTUO)){
				
					if(response.getSoggetti().get(0).getStatoOperativo() != null &&
							!response.getSoggetti().get(0).getStatoOperativo().name().equals(Constanti.STATO_VALIDO) &&
							!response.getSoggetti().get(0).getStatoOperativo().name().equals(Constanti.STATO_IN_MODIFICA)){
						addErrore(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("soggetto=" + response.getSoggetti().get(0).getCodiceSoggetto(), response.getSoggetti().get(0).getStatoOperativo().name()));
						model.setListaRicercaSoggetto(null);
						model.setSoggettoTrovato(false);
						return false;
					}
				}else{
					
					if(response.getSoggetti().get(0).getStatoOperativo() != null &&
							!response.getSoggetti().get(0).getStatoOperativo().name().equals(Constanti.STATO_VALIDO) &&
							!response.getSoggetti().get(0).getStatoOperativo().name().equals(Constanti.STATO_IN_MODIFICA) &&
							!response.getSoggetti().get(0).getStatoOperativo().name().equals(Constanti.STATO_BLOCCATO)){
						addErrore(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("soggetto=" + response.getSoggetti().get(0).getCodiceSoggetto(), response.getSoggetti().get(0).getStatoOperativo().name()));
						model.setListaRicercaSoggetto(null);
						model.setSoggettoTrovato(false);
						return false;
					}
				}
				SoggettoImpegnoModel soggettoImpegnoModel = new SoggettoImpegnoModel();
				soggettoImpegnoModel.setCodCreditore(response.getSoggetti().get(0).getCodiceSoggetto());
				
				// Jira-639
				// inserisco la classe nel modello solo se arriva da ricerca
				if(!isEmpty(soggettoRiferimento.getParametroRicercaSoggetto().getClasse())){
					soggettoImpegnoModel.setClasse((response.getSoggetti().get(0)
						.getTipoClassificazioneSoggettoId() != null && response
						.getSoggetti().get(0)
						.getTipoClassificazioneSoggettoId().length > 0) ? response
						.getSoggetti().get(0)
						.getTipoClassificazioneSoggettoId()[0]
						: "");
				}
				soggettoImpegnoModel.setCodfisc(response.getSoggetti().get(0).getCodiceFiscale());
				soggettoImpegnoModel.setPiva(response.getSoggetti().get(0).getPartitaIva());
				soggettoImpegnoModel.setDenominazione(response.getSoggetti().get(0).getDenominazione());
				soggettoImpegnoModel.setStato(response.getSoggetti().get(0).getStatoOperativo());
				soggettoImpegnoModel.setUid(response.getSoggetti().get(0).getUid());
				
				model.setListaRicercaSoggetto(null);
				model.setSoggettoTrovato(false);
				settaSoggettoSelezionato(soggettoImpegnoModel, modaleSoggetto);
			}
		}
		return true;
	}
	
		/**
	 * esegui ricerca progetto
	 * @param progettoRequest
	 * @return
	 */
	protected boolean eseguiRicercaProgetto(RicercaSinteticaProgetto progettoRequest) {
		
		//Ricerca generica
		
		//invoco il servizio ricercaSinteticaProgetto:
		RicercaSinteticaProgettoResponse response = progettoService.ricercaSinteticaProgetto(progettoRequest);
		
		if (isFallimento(response) || isEmpty(response.getProgetti())) {
			addErrori("eseguiRicercaProgetto", response);
			if (response.getErrori().isEmpty()) {
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Progetto", model.getProgettoRicerca().getCodice()));
			}
			model.setProgettoTrovato(false);
			return false;
		} else {
			if(response.getTotaleElementi()>MAX_RICERCA_PROGETTI_MODALE){
				addErrore(ErroreFin.RICERCA_TROPPO_ESTESA.getErrore());
				model.setProgettoTrovato(false);
				return false;
			} else {
				model.setProgettoTrovato(true);
				model.setListaRicercaProgetto(response.getProgetti());
			}
		}
		return true;
	}
	
	/**
	 * esegui ricerca progetto puntuale
	 * @param progettoRequest
	 * @return
	 */
	protected boolean eseguiRicercaProgettoPuntuale(RicercaPuntualeProgetto progettoRequest) {
		
		//Ricerca generica
		
		//invoco il servizio ricercaPuntualeProgetto:
		RicercaPuntualeProgettoResponse response = progettoService.ricercaPuntualeProgetto(progettoRequest);
		
		if (response.isFallimento() || response.getProgetto() == null) {
			addErrori("eseguiRicercaProgetto", response);
			if (response.getErrori().isEmpty()) {
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Progetto", model.getProgettoRicerca().getCodice()));
			}
			model.setProgettoTrovato(false);
			return false;
		} else {
				model.setProgettoTrovato(true);
				List<Progetto> lista = new ArrayList<Progetto>();
				lista.add(response.getProgetto());
				model.setListaRicercaProgetto(lista );
		}
		return true;
	}

	protected abstract void setSoggettoSelezionato(SoggettoImpegnoModel soggettoImpegnoModel);
	
	protected abstract void setSoggettoSelezionatoDue(SoggettoImpegnoModel soggettoImpegnoModel);
	
	protected void setProgettoSelezionato(ProgettoImpegnoModel progettoImpegnoModel){
		return;
	}
	
	
	protected void setSiopeSpesaSelezionato(SiopeSpesa siopeTrovato,boolean trovato) {
		//le singole action devono implemenarlo facendo override
		return;
	}
	
	@Override
	protected void codiceSiopeChangedInternal(String cod){
		//non implementa nulla in questa action, 
		//solo per compatibilita' con l'interfaccia centralizzata
		
		//le singole action devono implemenarlo facendo override
	};
	
	/**
	 * ricerca progetto
	 * @return
	 * @throws Exception
	 */
	public String ricercaProgetto() throws Exception {
		//request per ricerca generica:
		
		//APRILE 2018 per SIAC-6086 commento questo:
		
		/*
		if(!isEmpty(model.getProgettoRicerca().getCodice()) && isEmpty(model.getProgettoRicerca().getDescrizione())){
			//se il codice e' valorizzato e la descrizione no, vado per chiave:
			RicercaPuntualeProgetto ricercaPuntualeProgetto = convertiModelPerChiamataServizioRicercaProgettoPuntuale(model.getProgettoRicerca().getCodice());
			eseguiRicercaProgettoPuntuale(ricercaPuntualeProgetto);
			
		} else {
			//altrimenti vado per ricerca generica:
			RicercaSinteticaProgetto requestRicerca = convertiModelPerChiamataServizioRicercaProgetto(model.getProgettoRicerca());
			eseguiRicercaProgetto(requestRicerca);
		}*/
		
		//e chiamo sempre la ricerca generica:
		
		// vado per ricerca generica:
		RicercaSinteticaProgetto requestRicerca = convertiModelPerChiamataServizioRicercaProgetto(model.getProgettoRicerca());
		eseguiRicercaProgetto(requestRicerca);
		
		return "ricercaProgetto";
	}
	
	/**
	 * ricerca soggetto
	 * @return
	 * @throws Exception
	 */
	public String ricercaSoggetto() throws Exception {
		eseguiRicercaSoggetto(convertiModelPerChiamataServizioRicerca(model.getSoggettoRicerca()), true, oggettoDaPopolare);
		return "ricercaSoggetto";
	}
	
	/**
	 * ricerca soggetto due --> per i rari casi in cui nella stessa pagina
	 *  si deve cercare un soggetto di qualche 
	 * altro tipo
	 * @return
	 * @throws Exception
	 */
	public String ricercaSoggettoDue() throws Exception {
		eseguiRicercaSoggetto(convertiModelPerChiamataServizioRicerca(model.getSoggettoRicercaDue()), true, oggettoDaPopolare,SoggettoDaCercareEnum.DUE);
		return "ricercaSoggetto";
	}
	
	/**
	 * converti model per chiamata servizio ricerca
	 * @param soggettoRiferimento
	 * @return
	 */
	protected RicercaSoggetti convertiModelPerChiamataServizioRicerca(SoggettoImpegnoModel soggettoRiferimento) {
		
		//istanzio la request:
		RicercaSoggetti ricercaSoggetti = new RicercaSoggetti();
		
		//setto i dati:
		
		//RICHIEDENTE
		ricercaSoggetti.setRichiedente(sessionHandler.getRichiedente());
		
		//ENTE
		ricercaSoggetti.setEnte(sessionHandler.getAccount().getEnte());
		
		//PARAMETRO RICERCA SOGGETTO:
		ParametroRicercaSoggetto parametroRicercaSoggetto = new ParametroRicercaSoggetto();
		
		//cod creditore
		if (soggettoRiferimento.getCodCreditore() != null) {
			parametroRicercaSoggetto.setCodiceSoggetto(soggettoRiferimento.getCodCreditore().toString());
		}
		
		//cod fiscale
		if(soggettoRiferimento.getCodfisc() != null){
			parametroRicercaSoggetto.setCodiceFiscale(soggettoRiferimento.getCodfisc().toString());
		}
		
		//partita iva
		if(soggettoRiferimento.getPiva() != null){
			parametroRicercaSoggetto.setPartitaIva(soggettoRiferimento.getPiva().toString());
		}
		
		//denominazione
		if(soggettoRiferimento.getDenominazione() != null){
			parametroRicercaSoggetto.setDenominazione(soggettoRiferimento.getDenominazione().toString());
		}
		
		//classe:
		//FIX PER  SIAC-5184 ho aggiunto il controllo sul fatto che sia un numero
		if(classeCompilata(soggettoRiferimento)){
			//la classe non e' vuota, e' un numerico ed e' diverso da -1 (ValidationUtils.RICERCA_VUOTA)
			parametroRicercaSoggetto.setClasse(soggettoRiferimento.getClasse().toString());
		}
		
		//setto il parametro di ricerca nella request:
		ricercaSoggetti.setParametroRicercaSoggetto(parametroRicercaSoggetto);
		
		//ritorno la request al chiamante:
		return ricercaSoggetti;
	}
	
	/*
	 * Controlla che la classe sia correttamente valorizzata
	 */
	protected boolean classeCompilata(SoggettoImpegnoModel soggettoRiferimento){
		String classe = soggettoRiferimento.getClasse();
		if(!FinStringUtils.isEmpty(classe) && FinUtility.isNumeroIntero(classe) && !(ValidationUtils.RICERCA_VUOTA.intValue() == Integer.valueOf(classe).intValue())){
			//la classe non e' vuota, e' un numerico ed e' diverso da -1 (ValidationUtils.RICERCA_VUOTA)
			return true;
		}
		return false;
	}
	
	/**
	 * converti model per chiamata servizio ricerca progetto
	 * @param criteriRicercaProgetto
	 * @return
	 */
	protected RicercaSinteticaProgetto convertiModelPerChiamataServizioRicercaProgetto(ProgettoImpegnoModel criteriRicercaProgetto) {
		RicercaSinteticaProgetto ricercaSinteticaProgetto = new RicercaSinteticaProgetto();
		ricercaSinteticaProgetto.setRichiedente(sessionHandler.getRichiedente());
		
		Progetto progetto = new Progetto();
		progetto.setEnte(sessionHandler.getEnte());
		progetto.setBilancio(sessionHandler.getBilancio());
		
		
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(MAX_RICERCA_PROGETTI_MODALE);
		parametriPaginazione.setNumeroPagina(0);
		ricercaSinteticaProgetto.setParametriPaginazione(parametriPaginazione);
		
		
		progetto.setCodice(criteriRicercaProgetto.getCodice());
		progetto.setDescrizione(criteriRicercaProgetto.getDescrizione());
		progetto.setTipoAmbito(criteriRicercaProgetto.getTipoAmbito());
		progetto.setStrutturaAmministrativoContabile(criteriRicercaProgetto.getStrutturaAmministrativoContabile());
		progetto.setTipoProgetto(TipoProgetto.GESTIONE);
		
		progetto.setStatoOperativoProgetto(StatoOperativoProgetto.VALIDO);
		
		ricercaSinteticaProgetto.setProgetto(progetto);
		
		return ricercaSinteticaProgetto;
	}
	
	/**
	 * Metodo che fa parte del meccanismo del modal con cui si seleziona un progetto da compilazione guidata
	 * @return
	 */
	public String selezionaProgetto() {	
		List<Errore> listaErrori = new ArrayList<Errore>();
		if(isEmpty(getRadioCodiceProgetto())){
			if (!isEmpty(model.getListaRicercaProgetto()) && !model.isProgettoTrovato()) {
				//Se non viene selezionato alcun progetto, mostro l'errore
				addActionError("Elemento non selezionato");
				addErrori(listaErrori);
				model.setListaRicercaProgetto(null);
				model.setProgettoTrovato(false);
			}
			return INPUT;
		} else {
			for(Progetto s: model.getListaRicercaProgetto()){
				if (s.getCodice().equalsIgnoreCase(getRadioCodiceProgetto())) {
					
					/*
					 *  i progetti che devo essere presi sono solo quelli in stato VALIDO o PROVVISORIO
					 *  questo dopo l'n-esima telefonata con chiara
					 */
					if(s.getStatoOperativoProgetto() != null && !s.getStatoOperativoProgetto().name().equals(Constanti.STATO_VALIDO) && !s.getStatoOperativoProgetto().name().equals(Constanti.STATO_IN_MODIFICA)){
						listaErrori.add(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("progetto="+s.getCodice(), s.getStatoOperativoProgetto().name()));
						addErrori(listaErrori);
						model.setListaRicercaProgetto(null);
						model.setProgettoTrovato(false);
						return INPUT;
					}else{
						popolaProgetto(s);
					} 
				}
			}
			return SUCCESS;
		}
	}
	
	public String selezionaProgettoCronop() {	
		List<Errore> listaErrori = new ArrayList<Errore>();
		if(getRadioUidCronoprogramma() == 0) {
			if (!isEmpty(model.getListaRicercaProgettoCronoprogrammi()) && !model.isProgettoTrovato()) {
				//Se non viene selezionato alcun progetto, mostro l'errore
				addActionError("Elemento non selezionato");
				addErrori(listaErrori);
				model.setListaRicercaProgetto(null);
				model.setProgettoTrovato(false);
			}
			return INPUT;
		} else {
			for(Cronoprogramma c: model.getListaRicercaProgettoCronoprogrammi()) {
				if (c.getUid() == getRadioUidCronoprogramma()) {
					
					Progetto p = c.getProgetto();
					
					if(p.getStatoOperativoProgetto() != null && !p.getStatoOperativoProgetto().name().equals(Constanti.STATO_VALIDO) && !p.getStatoOperativoProgetto().name().equals(Constanti.STATO_IN_MODIFICA)){
						listaErrori.add(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("progetto="+p.getCodice(), p.getStatoOperativoProgetto().name()));
						addErrori(listaErrori);
						model.setListaRicercaProgetto(null);
						model.setProgettoTrovato(false);
						return INPUT;
					}else{
						popolaProgetto(p);
						popolaCronoprogramma(c);
					} 
				}
			}
			return SUCCESS;
		}
	}
	
	/**
	 * Selezione del soggetto nella modale numero due
	 * @return
	 */
	public String selezionaSoggettoDue() {
		return selezionaSoggetto(SoggettoDaCercareEnum.DUE);
	}
	
	/**
	 * Wrapper di retrocompatibilita'
	 * @return
	 */
	public String selezionaSoggetto() {	
		return selezionaSoggetto(SoggettoDaCercareEnum.UNO);
	}
	
	/**
	 * metodo che seleziona soggetto
	 * @return
	 */
	public String selezionaSoggetto(SoggettoDaCercareEnum modaleSoggetto) {		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		List<Soggetto> listaRicercaSoggetto = model.getListaRicercaSoggetto();
		boolean soggettoTrovato = model.isSoggettoTrovato();
		
		if(isEmpty(getRadioCodiceSoggetto())){
			
			if (!isEmpty(listaRicercaSoggetto) && !soggettoTrovato) {
				//Se non viene selezionato alcun soggetto, mostro l'errore
				addActionError("Elemento non selezionato");
				addErrori(listaErrori);
				
				model.setListaRicercaSoggetto(null);
				model.setSoggettoTrovato(false);
			}
			return INPUT;
		} else {
			if(!isEmpty(listaRicercaSoggetto)){
				for(Soggetto s: listaRicercaSoggetto){
					if (s.getCodiceSoggetto().equalsIgnoreCase(getRadioCodiceSoggetto())) {
						
						/*
						 *  i soggetti che devo essere presi sono solo quelli in stato VALIDO o PROVVISORIO
						 *  questo dopo l'n-esima telefonata con chiara
						 */
						if(s.getStatoOperativo() != null && !s.getStatoOperativo().name().equals(Constanti.STATO_VALIDO) && !s.getStatoOperativo().name().equals(Constanti.STATO_IN_MODIFICA)){
							listaErrori.add(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("soggetto="+s.getCodiceSoggetto(), s.getStatoOperativo().name()));
							addErrori(listaErrori);
							model.setListaRicercaSoggetto(null);
							model.setSoggettoTrovato(false);
							return INPUT;
						}else{
							popolaSoggetto(s,modaleSoggetto);
						} 
					}
				}
			}
			return SUCCESS;
		}
	}
	
	/**
	 * seleziona soggetto controllo in modifica
	 * @return
	 */
	public String selezionaSoggettoControlloInModifica() {		
		List<Errore> listaErrori = new ArrayList<Errore>();
		if(isEmpty(getRadioCodiceSoggetto())){
			if (!isEmpty(model.getListaRicercaSoggetto()) && !model.isSoggettoTrovato()) {
				//Se non viene selezionato alcun soggetto, mostro l'errore
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto"));
				addErrori(listaErrori);
				model.setListaRicercaSoggetto(null);
				model.setSoggettoTrovato(false);
			}
			return INPUT;
		} else {
			for(Soggetto s: model.getListaRicercaSoggetto()){
				if (s.getCodiceSoggetto().equalsIgnoreCase(getRadioCodiceSoggetto())) {
					
					/*
					 *  i soggetti che devo essere presi sono solo quelli in stato VALIDO o PROVVISORIO
					 *  questo dopo l'n-esima telefonata con chiara
					 */
					//JIRA 690 i mutui possono avere un soggetto in stato bloccato
					if(!oggettoDaPopolare.equals(OggettoDaPopolareEnum.MUTUO)){
						if(s.getStatoOperativo() != null && !s.getStatoOperativo().name().equals(Constanti.STATO_VALIDO) && !s.getStatoOperativo().name().equals(Constanti.STATO_IN_MODIFICA)){
							listaErrori.add(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("soggetto="+s.getCodiceSoggetto(), s.getStatoOperativo().name()));
							addErrori(listaErrori);
							model.setListaRicercaSoggetto(null);
							model.setSoggettoTrovato(false);
							return INPUT;
						}else{
							popolaSoggetto(s);
						}
					}else{
						if(s.getStatoOperativo() != null && !s.getStatoOperativo().name().equals(Constanti.STATO_VALIDO) && !s.getStatoOperativo().name().equals(Constanti.STATO_IN_MODIFICA) && !s.getStatoOperativo().name().equals(Constanti.STATO_BLOCCATO)){
							listaErrori.add(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("soggetto="+s.getCodiceSoggetto(), s.getStatoOperativo().name()));
							addErrori(listaErrori);
							model.setListaRicercaSoggetto(null);
							model.setSoggettoTrovato(false);
							return INPUT;
						}else{
							popolaSoggetto(s);
						}
					}
				}
			}
			return SUCCESS;
		}
	}
	
	/**
	 * seleziona soggetto controllo in sospeso
	 * @return
	 */
	public String selezionaSoggettoControlloInSospeso() {		
		List<Errore> listaErrori = new ArrayList<Errore>();
		if(isEmpty(getRadioCodiceSoggetto())){
			if (!isEmpty(model.getListaRicercaSoggetto()) && !model.isSoggettoTrovato()) {
				//Se non viene selezionato alcun soggetto, mostro l'errore
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto"));
				addErrori(listaErrori);
				model.setListaRicercaSoggetto(null);
				model.setSoggettoTrovato(false);
			}
			return INPUT;
		} else {
			for(Soggetto s: model.getListaRicercaSoggetto()){
				if (s.getCodiceSoggetto().equalsIgnoreCase(getRadioCodiceSoggetto())) {
					
					/*
					 *  i soggetti che devo essere presi sono solo quelli in stato VALIDO o PROVVISORIO
					 *  questo dopo l'n-esima telefonata con chiara
					 */
					
					if(s.getStatoOperativo() != null && !s.getStatoOperativo().name().equals(Constanti.STATO_VALIDO) && !s.getStatoOperativo().name().equals("SOSPESO")){
						listaErrori.add(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("soggetto="+s.getCodiceSoggetto(), s.getStatoOperativo().name()));
						addErrori(listaErrori);
						model.setListaRicercaSoggetto(null);
						model.setSoggettoTrovato(false);
						return INPUT;
					}else{
						popolaSoggetto(s);
					}
				}
			}
			return SUCCESS;
		}
	}
	
	/**
	 * popola siope
	 * @param siopeSpesa
	 * @param trovato
	 */
	protected void popolaSiope(SiopeSpesa siopeSpesa,boolean trovato) {
		//SETTO IL SIOPE:
		setSiopeSpesaSelezionato(siopeSpesa,trovato);
	}
	
	/**
	 * popola progetto
	 * @param prg
	 */
	protected void popolaProgetto(CodificaFin prg) {
		Progetto progetto = new Progetto();
		progetto.setCodice(prg.getCodice());
		progetto.setDescrizione(prg.getDescrizione());
		popolaProgetto(progetto);
	}
	
	/**
	 * popola progetto
	 * @param prg
	 */
	protected void popolaProgetto(Progetto prg) {
		ProgettoImpegnoModel progettoImpegnoModel = new ProgettoImpegnoModel();
		progettoImpegnoModel.setCodice(prg.getCodice());
		progettoImpegnoModel.setDescrizione(prg.getDescrizione());
		progettoImpegnoModel.setValoreComplessivo(prg.getValoreComplessivo());
		progettoImpegnoModel.setProgetto(prg);
		model.setListaRicercaProgetto(null);
		model.setProgettoTrovato(false);
		setProgettoSelezionato(progettoImpegnoModel);
	}
	
	protected void popolaCronoprogramma(Cronoprogramma c) {
	}
	
	/**
	 * Wrapper di retrocompatibilita'
	 * @param s
	 * @param modaleSoggetto
	 */
	protected void popolaSoggetto(Soggetto s) {
		popolaSoggetto(s, SoggettoDaCercareEnum.UNO);
	}

	/**
	 * popola soggetto
	 * @param s
	 */
	protected void popolaSoggetto(Soggetto s,SoggettoDaCercareEnum modaleSoggetto) {
		SoggettoImpegnoModel soggettoImpegnoModel = new SoggettoImpegnoModel();
		soggettoImpegnoModel.setCodCreditore(s.getCodiceSoggetto());
		soggettoImpegnoModel.setCodfisc(s.getCodiceFiscale());
		soggettoImpegnoModel.setPiva(s.getPartitaIva());
		soggettoImpegnoModel.setDenominazione(s.getDenominazione());
		soggettoImpegnoModel.setStato(s.getStatoOperativo());
		soggettoImpegnoModel.setUid(s.getUid());
		if(!isEmpty(s.getElencoClass())){
			List<ClasseSoggetto> listaClassificazioni = new ArrayList<ClasseSoggetto>();
			for(int i=0; i<s.getElencoClass().size(); i++){
				ClasseSoggetto cls = new ClasseSoggetto(); 
				cls.setCodice(s.getElencoClass().get(i).getSoggettoClasseCode());
				cls.setDescrizione(s.getElencoClass().get(i).getSoggettoClasseDesc());
				listaClassificazioni.add(cls);
			}
			soggettoImpegnoModel.setListClasseSoggetto(listaClassificazioni);
		}
		model.setListaRicercaSoggetto(null);
		model.setSoggettoTrovato(false);
		settaSoggettoSelezionato(soggettoImpegnoModel, modaleSoggetto);
	}
	
	/**
	 * pulisci ricerca soggetto
	 * @return
	 */
	public String pulisciRicercaSoggetto() {
		model.setListaRicercaSoggetto(null);
		//setto soggettoTrovato a false cosi' ogni volta che vengono ripuliti i risultati della ricerca viene reimpostata la non visibilita' del blocco che li contiene
		model.setSoggettoTrovato(false);
		return "ricercaSoggetto";
	}
	
	/**
	 * pulisci ricerca progetto
	 * @return
	 */
	public String pulisciRicercaProgetto() {
		model.setListaRicercaProgetto(null);
		model.setProgettoTrovato(false);
		return "ricercaSoggetto";
	}
	
	/**
	 * inserisci provvedimento
	 * @return
	 * @throws Exception
	 */
	public String inserisciProvvedimento() throws Exception {
		
		//per avere sempre una situazione coerente:
		model.setProvvedimentoInserito(false);
		model.setNumeroProvvedimentoInserimentoDaResp(null);
		//
		
		ProvvedimentoImpegnoModel pr = model.getProvvedimentoInserimento();
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		// Anno provv (obl)
		if((pr.getAnnoProvvedimento() == null) || (pr.getAnnoProvvedimento() == 0)){
		    listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno "));
	    }
		
		//Numero provv (obl)
		if((pr.getAnnoProvvedimento() != null && pr.getAnnoProvvedimento() != 0) && 
				(pr.getNumeroProvvedimento() == null || pr.getNumeroProvvedimento().intValue() == 0) &&
				(pr.getTipoProvvedimento() != null || !isEmpty(getStrutturaAmministrativaSelezionata()))){
		    listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero del provvedimento"));
	    }
		//Tipo provv (obl)
		TipoAtto tipoAtto = buildAttoSelezionato(pr);
		if(tipoAtto==null || tipoAtto.getUid()<=0){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo provvedimento"));
		}
		//Stato (obl)
		if(isEmpty(pr.getStato())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Stato provvedimento"));
		}
		
		
	    if(listaErrori.isEmpty()) {
	    	
	    	//VERIFICHIAMO CHE NON ESISTA GIA' UN PROVVEDIMENTO CON I DATI INDICATI
	    	//  (purtroppo il servizio di inserimento non fa questi controlli e occorre effettura una chiamata
	    	//   al servizio di ricerca...)
	    	
	    	//istanzio la request per il servizio ricercaProvvedimento:
	    	RicercaProvvedimento ricercaProvv = builRequestRicercaProvvPerInserisci(tipoAtto, pr);
	    	//invoco il servizio ricercaProvvedimento:
			RicercaProvvedimentoResponse responseRicerca = provvedimentoService.ricercaProvvedimento(ricercaProvv);
	    	
			if(!isFallimento(responseRicerca)){
				
				
				StrutturaAmministrativoContabile strutturaAmm = buildStrutturaAmministrativaSelezionataSuPagina(pr);
				
				if(!isEmpty(responseRicerca.getListaAttiAmministrativi())){
					
					if(strutturaAmm==null || strutturaAmm.getUid()<=0){
						//no struttura amm indicata
						for(AttoAmministrativo struttIt : responseRicerca.getListaAttiAmministrativi()){
							if(struttIt!=null && struttIt.getStrutturaAmmContabile()==null){
								//esiste gia' un provvedimento con stesso anno, numero, tipo e SENZA struttura AMMINISTRATIVA
								listaErrori.add(ErroreFin.ENTITA_GIA_PRESENTE.getErrore("Provvedimento", pr.getAnnoProvvedimento() + "/" +
										pr.getNumeroProvvedimento().intValue() + " - " + tipoAtto.getDescrizione()));
								break;
							}
						}
					} else {
						//si struttura amm indicata
						for(AttoAmministrativo struttIt : responseRicerca.getListaAttiAmministrativi()){
							if(struttIt!=null && struttIt.getStrutturaAmmContabile()!=null && struttIt.getStrutturaAmmContabile().getUid()==strutturaAmm.getUid()){
								//esiste gia' un provvedimento con stesso anno, numero, tipo e CON LA STESSA struttura AMMINISTRATIVA
								listaErrori.add(ErroreFin.ENTITA_GIA_PRESENTE.getErrore("Provvedimento", pr.getAnnoProvvedimento() + "/" +
										pr.getNumeroProvvedimento().intValue() + " - " + tipoAtto.getDescrizione() + " - " + struttIt.getStrutturaAmmContabile().getCodice()));
								break;
							}
						}
					}
					
				}
				
				
				
				/*
				 * SIAC-6929
				 * Controllo blocco ragioneria
				 * capire bene il criterio di validità 
				 */
			    //if(model.isControlloBloccoRagioneriaAbilitato()){
//			    	if(responseRicerca.getListaAttiAmministrativi()!= null && !responseRicerca.getListaAttiAmministrativi().isEmpty()){
//			    		if(FinUtility.bloccataDaRagioneria(responseRicerca.getListaAttiAmministrativi())){
//			    			String oggettoPr = (pr.getOggetto()!= null) ? pr.getOggetto() : "";
//			    			listaErrori.add(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore("Numero Provvedimento " + 
//			    					pr.getNumeroProvvedimento() + " Oggetto " + oggettoPr));
//			    		}
//			    	}
			    //}
				
				 if(listaErrori.isEmpty()) {
					 
					 //FINALMENTE, DOPO TUTTI I CONTROLLI DEL CASO, METODO DI INSERIMENTO:
					InserisceProvvedimentoResponse resp = eseguiInserimentoProvvedimento(oggettoDaPopolare, pr , strutturaAmm, tipoAtto);
			    	if(!isFallimento(resp)){
			    			//OK
			    			model.setNumeroProvvedimentoInserimentoDaResp(BigInteger.valueOf(resp.getAttoAmministrativoInserito().getNumero()));
				    		impostaProvvNelModel(resp.getAttoAmministrativoInserito(), model.getProvvedimentoInserimento());
				    		
				    		String riepilogoInserito = 
				    				resp.getAttoAmministrativoInserito().getAnno()  + "/" + resp.getAttoAmministrativoInserito().getNumero() + " - " +
				    				resp.getAttoAmministrativoInserito().getTipoAtto().getDescrizione();
				    		if(resp.getAttoAmministrativoInserito().getStrutturaAmmContabile()!=null &&
				    				!isEmpty(resp.getAttoAmministrativoInserito().getStrutturaAmmContabile().getCodice()) ){
				    			riepilogoInserito = riepilogoInserito + " - " + resp.getAttoAmministrativoInserito().getStrutturaAmmContabile().getCodice();
				    		}
				    		
				    		addActionMessage("Provvedimento creato correttamente: " + riepilogoInserito);
					    	model.setProvvedimentoInserito(true);
				    	
			    	} else {
			    		if(resp!=null){
			    			addErrori(resp.getErrori());
			    		} else {
			    			addActionError("Errore generico durante l'inserimento");
			    		}
			    		model.setProvvedimentoInserito(false);
			    		model.setNumeroProvvedimentoInserimentoDaResp(null);
			    	}
			    	
			    	//
					 
				 } else {
			    	addErrori(listaErrori);
					model.setProvvedimentoInserito(false);
					model.setNumeroProvvedimentoInserimentoDaResp(null);
				 }
			
				
			} else {
				addActionError("Errore richiamando il servizio di ricerca per verificare se esiste gia' il provvedimento in inserimento");
				addErrori(responseRicerca.getErrori());
	    		model.setProvvedimentoInserito(false);
			}
			
	    	//
	    	
	    	
	    	
	    } else {
	    	addErrori(listaErrori);
			model.setProvvedimentoInserito(false);
	    }
	    return "esitoInserimentoProvvedimento";
	}
	
	/**
	 * buil request ricerca provv per inserisci
	 * @param tipoAtto
	 * @param pr
	 * @return
	 */
	private RicercaProvvedimento builRequestRicercaProvvPerInserisci(TipoAtto tipoAtto, ProvvedimentoImpegnoModel pr){
		RicercaProvvedimento ricercaProvv = new RicercaProvvedimento();
		ricercaProvv.setEnte(sessionHandler.getEnte());
		ricercaProvv.setRichiedente(sessionHandler.getRichiedente());
		RicercaAtti ricercaAtti = new RicercaAtti();
		ricercaAtti.setAnnoAtto(pr.getAnnoProvvedimento());
		ricercaAtti.setNumeroAtto(pr.getNumeroProvvedimento().intValue());
		ricercaAtti.setTipoAtto(tipoAtto);
		
		ricercaProvv.setRicercaAtti(ricercaAtti );
		return ricercaProvv;
	}


	/* ************************************************************************************************ */
	/*	Metodi per la compilazione guidata del Provvedimento											*/
	/* ************************************************************************************************ */
	
	/**
	 * ricerca provvedimento
	 * @return
	 * @throws Exception
	 */
	public String ricercaProvvedimento() throws Exception {
		ProvvedimentoImpegnoModel pr = model.getProvvedimentoRicerca();
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		// RM JIRA 1839, se indicati da paginetta anno e numero, scatta la ricerca
	    // se invece c'è anno e tipo o sac va indicato almeno un altro parametro
		if((pr.getAnnoProvvedimento() == null) || (pr.getAnnoProvvedimento() == 0)){
		    listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno "));
	    }
	    
		if((pr.getAnnoProvvedimento() != null && pr.getAnnoProvvedimento() != 0) && 
				(pr.getNumeroProvvedimento() == null || pr.getNumeroProvvedimento().intValue() == 0) &&
				(pr.getTipoProvvedimento() != null || !isEmpty(getStrutturaAmministrativaSelezionata()))){
					
		    listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero del provvedimento"));
	    }
	    
	    if(listaErrori.isEmpty()) {
	    	eseguiRicercaProvvedimento(oggettoDaPopolare, pr, true);
	    } else {
	    	addErrori(listaErrori);
	    	model.setListaRicercaProvvedimento(null);
			model.setProvvedimentoTrovato(false);
	    }
	    return "ricercaProvvedimento";
	}
	
	/**
	 * clear inserimento provvedimento
	 * @return
	 */
	public String clearInserimentoProvvedimento(){
		clearErrorsAndMessages();
		model.setProvvedimentoInserito(false);
		//aprendo il modale si pulisce tutto se un inserimento e' gia andato a buon fine:
		model.setProvvedimentoInserimento(new ProvvedimentoImpegnoModel());
		model.setNumeroProvvedimentoInserimentoDaResp(null);
		return "clearInserimentoProvvedimento";
	}
	
	/**
	 * seleziona provvedimento inserito
	 * @return
	 */
	public String selezionaProvvedimentoInserito() {
		if(model.isProvvedimentoInserito()){
			ProvvedimentoImpegnoModel supportProvvIns = clone(model.getProvvedimentoInserimento());
			
			if(model.getNumeroProvvedimentoInserimentoDaResp()!=null && model.getNumeroProvvedimentoInserimentoDaResp().intValue()>0){
				supportProvvIns.setNumeroProvvedimento(model.getNumeroProvvedimentoInserimentoDaResp());
			}
			
			
			if(supportProvvIns!=null){
				
				//da creazione appena effettuata potrebbe essere annullato:
				if(StatoOperativoAtti.ANNULLATO.toString().equalsIgnoreCase(supportProvvIns.getStato())){
					addActionError("Non e' possibile selezionare un provvedimento in stato " + StatoOperativoAtti.ANNULLATO.toString());
					return INPUT;
				}
				//
				
				String redirect = impostaProvvedimento(supportProvvIns);
				if(redirect!=null){
					return redirect;
				}
				
			}
		}
		return SUCCESS;
	}

	/**
	 * seleziona provvedimento
	 * @return
	 */
	public String selezionaProvvedimento() {		
		List<Errore> listaErrori = new ArrayList<Errore>();
		if(isEmpty(getRadioCodiceProvvedimento())){
			if (!isEmpty(model.getListaRicercaProvvedimento())) {
				//Se non viene selezionato alcun soggetto, mostro l'errore
				addActionError("Elemento non selezionato");
				addErrori(listaErrori);
				model.setProvvedimentoTrovato(false);
				model.setListaRicercaProvvedimento(null);
			}
			return INPUT;
		} else {
			for(ProvvedimentoImpegnoModel currentProvvedimento: model.getListaRicercaProvvedimento()){				
				if (currentProvvedimento.getUid().toString().equalsIgnoreCase(getRadioCodiceProvvedimento())) {
					String redirect = impostaProvvedimento(currentProvvedimento);
					if(redirect!=null){
						return redirect;
					}
					break;
				}
			}
			return SUCCESS;
		}
	}
	
	/**
	 * imposta provvedimento
	 * @param currentProvvedimento
	 * @return
	 */
	private String impostaProvvedimento(ProvvedimentoImpegnoModel currentProvvedimento){
		String redirect = null;
		List<Errore> listaErrori = new ArrayList<Errore>();
		if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO) || 
		   oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_INCASSO) || 
		   oggettoDaPopolare.equals(OggettoDaPopolareEnum.CARTA)){
			
				if(currentProvvedimento.getStato().equalsIgnoreCase("DEFINITIVO")){
					model.setProvvedimentoTrovato(false);
					model.setListaRicercaProvvedimento(null);
					currentProvvedimento.setCodiceStrutturaAmministrativaPadre(cercaIdPadre(currentProvvedimento));
					// per hidden strutturaSelezionataSuPagina
					// CR - 1907 --> null sul selezione provvedimento da modale
					setStrutturaSelezionataSuPagina((currentProvvedimento.getUidStrutturaAmm()!=null ? String.valueOf(currentProvvedimento.getUidStrutturaAmm()):""));
					setProvvedimentoSelezionato(currentProvvedimento);
				}else{
					if (oggettoDaPopolare.equals(OggettoDaPopolareEnum.CARTA)) {
						addErrore(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("gestione carta contabile", "definitivo"));
					} else {
						addErrore(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("gestione ordinativo", "definitivo"));
					}
				}
		}else{
			// Jira - 1586
			if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBACCERTAMENTO) || oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBIMPEGNO)){
				String tipoOggetto = oggettoDaPopolare.toString();
				// stato del movgest accertamento = a stato provvedimento del sub
				
				//FIX per JIRA SIAC-2734 controllo solo se il movimento in aggiornamento HA un provvedimento:
				boolean presenzaProvvedimentoInMovimento = presenzaProvvedimentoInMovimentoInAggiornamento();
				if(presenzaProvvedimentoInMovimento && 
						!currentProvvedimento.getStato().equals(((GestisciMovGestModel)model).getStep1Model().getProvvedimento().getStato())){
					
					boolean ok = currentProvvedimento.getStato().equalsIgnoreCase(Constanti.STATO_PROVVISORIO) &&
					((GestisciMovGestModel)model).getStep1Model().getProvvedimento().getStato().equalsIgnoreCase(Constanti.STATO_DEFINITIVO) ;
					
					if(!ok){
						// jira 1749 msg sbagliato
						listaErrori.add(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("il provvedimento "+ currentProvvedimento.getNumeroProvvedimento(), 
								currentProvvedimento.getStato() + " non &egrave compatibile con lo stato dell'entit&agrave "+ (tipoOggetto.equalsIgnoreCase("SUBIMPEGNO") ? "IMPEGNO" : "ACCERTAMENTO")));
						addErrori(listaErrori);
						return INPUT;
					}
				}
			}
			
			currentProvvedimento.setCodiceStrutturaAmministrativaPadre(cercaIdPadre(currentProvvedimento));
			// per hidden strutturaSelezionataSuPagina
			// CR - 1907 --> null sul selezione provvedimento da modale
			setStrutturaSelezionataSuPagina((currentProvvedimento.getUidStrutturaAmm()!=null ? String.valueOf(currentProvvedimento.getUidStrutturaAmm()):""));

			model.setProvvedimentoTrovato(false);
			model.setListaRicercaProvvedimento(null);
			setProvvedimentoSelezionato(currentProvvedimento);
		}
		
		//FIX PER SIAC-3943
		if(NESSUNA_STRUTTURA_AMMINISTRATIVA.equals(currentProvvedimento.getCodiceStrutturaAmministrativaPadre()) &&
				!isEmpty(currentProvvedimento.getCodiceStrutturaAmministrativa())){
			currentProvvedimento.setCodiceStrutturaAmministrativaPadre("");
		}
		//
		
		return redirect;
	}
	
	/**
	 * presenza provvedimento in movimento in aggiornamento
	 * @return
	 */
	private boolean presenzaProvvedimentoInMovimentoInAggiornamento(){
		boolean presenzaProvvedimentoInMovimento = false;
		AttoAmministrativo attoAmmMovimento = null;
		if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBACCERTAMENTO) || oggettoDaPopolare.equals(OggettoDaPopolareEnum.ACCERTAMENTO)){
			attoAmmMovimento = ((GestisciMovGestModel)model).getAccertamentoInAggiornamento().getAttoAmministrativo();
		}  else if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBIMPEGNO) || oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBIMPEGNO)){
			attoAmmMovimento = ((GestisciMovGestModel)model).getImpegnoInAggiornamento().getAttoAmministrativo();
		}
		if(attoAmmMovimento!=null && attoAmmMovimento.getUid()>=0){
			presenzaProvvedimentoInMovimento = true;
		}
		return presenzaProvvedimentoInMovimento;
	}
	
	protected abstract void setProvvedimentoSelezionato(ProvvedimentoImpegnoModel currentProvvedimento);

	
	/**
	 * cerca id padre
	 * @param currentProvvedimento
	 * @return
	 */
	protected String cercaIdPadre(ProvvedimentoImpegnoModel currentProvvedimento){
		if(currentProvvedimento != null && currentProvvedimento.getUidStrutturaAmm() != null && currentProvvedimento.getUidStrutturaAmm() != 0) {
			return cercaIdPadreByUid(currentProvvedimento);
		}
		return cercaIdPadreByCodice(currentProvvedimento);
	}
	
	/**
	 * cerca id padre
	 * @param currentProvvedimento
	 * @return
	 */
	private String cercaIdPadreByCodice(ProvvedimentoImpegnoModel currentProvvedimento){
		
		 //questo metodo realizza unapatch per cercare il padre di un nodo, 
		 // visto che dal servizio ricerca provvedimento arriva solo il codice e il livello
		 //e quindi devo rigirare l'albero per capire chi era il padre 
		 //in maniera da mettere il nome padre sull'etichetta es: 001 - 006 Mobilita
		
		boolean isDecentrato = isAzioneDecentrata(sessionHandler.getAzione().getNome());
		if(currentProvvedimento!=null && currentProvvedimento.getLivello()!= null 
				&& currentProvvedimento.getLivello()>1 && currentProvvedimento.getCodiceStrutturaAmministrativa()!=null){
			ArrayList<GestoreDatiAlberoModel> elencoStruttura = ottieniElencoStrutturaAmministrativa(isDecentrato);
			
			if(elencoStruttura!=null){
				for(GestoreDatiAlberoModel lista: elencoStruttura){
					// se e' maggiore di tre sara ad es: 005001 quindi io ho la parte finale del codice 
					// e vado a verificare proprio quella per cercare il padre
					if(lista.getId().length()>3 && lista.getId().endsWith(currentProvvedimento.getCodiceStrutturaAmministrativa())){
						for(GestoreDatiAlberoModel listaPadri: elencoStruttura){
							if(listaPadri.getId().length()==3 && listaPadri.getId().equals(lista.getId().substring(0,3))){
								return listaPadri.getId()+" - ";
							}
						}
						break;
					}
				}
			} else {
				//NON DOVREBBE MAI CADERE QUI.
				 return "";
			}
		
		}else if(currentProvvedimento.getLivello()!= null && currentProvvedimento.getLivello()==1){
		  return "";	
		}
		
		if(isDecentrato){
			// non c'e' nell'albero e' meglio non scrivere nulla piuttosto che la
			// frase successiva
			return "";
		}else{
			return NESSUNA_STRUTTURA_AMMINISTRATIVA;
		}
		
	}
	
	/**
	 * cerca id padre
	 * @param currentProvvedimento
	 * @return
	 */
	private String cercaIdPadreByUid(ProvvedimentoImpegnoModel currentProvvedimento){
		
		 //questo metodo realizza unapatch per cercare il padre di un nodo, 
		 // visto che dal servizio ricerca provvedimento arriva solo il codice e il livello
		 //e quindi devo rigirare l'albero per capire chi era il padre 
		 //in maniera da mettere il nome padre sull'etichetta es: 001 - 006 Mobilita
		
		boolean isDecentrato = isAzioneDecentrata(sessionHandler.getAzione().getNome());
		if(currentProvvedimento!=null && currentProvvedimento.getLivello()!= null 
				&& currentProvvedimento.getLivello()>1 && currentProvvedimento.getCodiceStrutturaAmministrativa()!=null){
			ArrayList<GestoreDatiAlberoModel> elencoStruttura = ottieniElencoStrutturaAmministrativa(isDecentrato);
			
			if(elencoStruttura!=null){
				for(GestoreDatiAlberoModel lista: elencoStruttura){
					// SIAC-6083: per uid, ricerco
					// Per uid: ricerco la struttura per uid e ne leggo il padre
					if(currentProvvedimento.getUidStrutturaAmm().equals(lista.getUid()) && lista.getUidPadre() != null){
						if(lista.getUidPadre() == null) {
							return "";
						}
						// Ricerco il padre per uid
						for(GestoreDatiAlberoModel listaPadre: elencoStruttura){
							if(lista.getUidPadre().equals(listaPadre.getUid())){
								return listaPadre.getId() + " - ";
							}
						}
					}
				}
			} else {
				//NON DOVREBBE MAI CADERE QUI.
				 return "";
			}
		
		}else if(currentProvvedimento.getLivello()!= null && currentProvvedimento.getLivello()==1){
		  return "";	
		}
		
		if(isDecentrato){
			// non c'e' nell'albero e' meglio non scrivere nulla piuttosto che la
			// frase successiva
			return "";
		}else{
			return NESSUNA_STRUTTURA_AMMINISTRATIVA;
		}
		
	}
	
	private ArrayList<GestoreDatiAlberoModel> ottieniElencoStrutturaAmministrativa(boolean isDecentrato) {
		ArrayList<GestoreDatiAlberoModel> elencoStruttura=(ArrayList<GestoreDatiAlberoModel>)sessionHandler.getParametro(FinSessionParameter.ELENCO_STRUTTURA_AMMINISTRATIVA);
		
		if(elencoStruttura==null){
			//SITUAZIONE IN CUI L'ALBERO DELLE STRUTTURE NON E' IN SESSIONE, ACCEDIAMO DIRETTAMENTE:
			LeggiStrutturaAmminstrativoContabile lsa = buildStrutturaAmminstrativoContabileRequest();
			LeggiStrutturaAmminstrativoContabileResponse responseLsa = getStrutturaAmministrativaCached(lsa);
			if(responseLsa!=null &&  responseLsa.getListaStrutturaAmmContabile()!=null){
				elencoStruttura = creaAlberoStrutturaAmministrativa(responseLsa.getListaStrutturaAmmContabile(), isDecentrato);
			}
		}
		return elencoStruttura;
	}
	
	/**
	 * caricaListePerRicercaSoggetto
	 */
	@SuppressWarnings("unchecked")
	protected void caricaListePerRicercaSoggetto() {
		//invoche get codifiche
	   	Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.CLASSE_SOGGETTO);
	   	//setto lista classe soggetto
	   	model.setListaClasseSoggetto((List<CodificaFin>)mappaListe.get(TipiLista.CLASSE_SOGGETTO));	   
   	}
	
	/**
	 * clear ricerca provvedimento
	 * @return
	 */
	public String clearRicercaProvvedimento() {		
		clearErrorsAndMessages();
		model.setListaRicercaProvvedimento(null);
		//setto provvedimentoTrovato a false cosi' ogni volta che vengono ripuliti i risultati della ricerca viene reimpostata la non visibilita' del blocco che li contiene
		model.setProvvedimentoTrovato(false);
		return "clearRicercaProvvedimento";
	}
	
	/**
	 * carica tipi provvedimenti
	 */
	protected void caricaTipiProvvedimenti(){
		//istanzio la request per il servizio getTipiProvvedimento:
		TipiProvvedimento tipiProvvedimento = new TipiProvvedimento();
		tipiProvvedimento.setRichiedente(sessionHandler.getRichiedente());
		tipiProvvedimento.setEnte(sessionHandler.getEnte());
		//invoco il servizio getTipiProvvedimento:
		TipiProvvedimentoResponse tipiProvvedimentoResponse = provvedimentoService.getTipiProvvedimento(tipiProvvedimento);
		List<TipoAtto> listaOttenuta = tipiProvvedimentoResponse.getElencoTipi();
		model.setListaTipiProvvedimenti(listaOttenuta);
	}
	
	/**
	 * Non carica chiamando nessun servizio. compone semplicemente una lista di stati dei provvedimenti basandosi sull'enum relativa
	 */
	protected void caricaStatiOperativiAtti(){
		List<StatoOperativoAtti> listaStatiProvvedimenti = new ArrayList<StatoOperativoAtti>();
		listaStatiProvvedimenti.add(StatoOperativoAtti.PROVVISORIO);
		listaStatiProvvedimenti.add(StatoOperativoAtti.DEFINITIVO);
		listaStatiProvvedimenti.add(StatoOperativoAtti.ANNULLATO);
		model.setListaStatiProvvedimenti(listaStatiProvvedimenti);
	}
	
	/**
	 * build atto selezionato
	 * @param provvedimentoImpegnoModel
	 * @return
	 */
	private TipoAtto buildAttoSelezionato(ProvvedimentoImpegnoModel provvedimentoImpegnoModel){
		TipoAtto tipoAtto = null;
		String idTipoAtto = (provvedimentoImpegnoModel.getTipoProvvedimento() != null) ? provvedimentoImpegnoModel.getTipoProvvedimento().toString() : null;
		if(idTipoAtto == null){
			idTipoAtto= (provvedimentoImpegnoModel.getIdTipoProvvedimento() != null) ? provvedimentoImpegnoModel.getIdTipoProvvedimento().toString() : null;
		}
		if (!isEmpty(idTipoAtto)) {
			tipoAtto = new TipoAtto("x","x");
			tipoAtto.setUid(Integer.parseInt(idTipoAtto));
			
			List<TipoAtto> listaTipoAtto = model.getListaTipiProvvedimenti();
			if(!isEmpty(listaTipoAtto)){
				for(TipoAtto tipoAttoIt : listaTipoAtto){
					if(tipoAttoIt!=null && tipoAttoIt.getUid()==tipoAtto.getUid()){
						tipoAtto.setCodice(tipoAttoIt.getCodice());
						tipoAtto.setDescrizione(tipoAttoIt.getDescrizione());
						break;
					}
				}
			}
		}
		return tipoAtto;
	}
	
	/**
	 * build struttura amministrativa selezionata su pagina
	 * @param provvedimentoImpegnoModel
	 * @return
	 */
	private StrutturaAmministrativoContabile buildStrutturaAmministrativaSelezionataSuPagina(ProvvedimentoImpegnoModel provvedimentoImpegnoModel){
		// nel caso in cui si seleziona la struttura sulla pagina in maniera manuale ne faccio un passaggio di variabili e lascio 
		// tutto invariato
		if (!strutturaSelezionataSuPaginaiIsVuota()){
			//se non e' vuota:
			strutturaAmministrativaSelezionata = strutturaSelezionataSuPagina;
		}
		
		// se arrivo da compilazione guidata la struttura ce l'ho sul model.provvedimento,
		// quindi aggiungo il controllo
		if (strutturaAmministrativaSelezionata == null){
			strutturaAmministrativaSelezionata = (provvedimentoImpegnoModel.getUidStrutturaAmm() != null) ? provvedimentoImpegnoModel.getUidStrutturaAmm().toString() : null;
		}
		
		StrutturaAmministrativoContabile strutturaAmm = null;
		if (!isEmpty(strutturaAmministrativaSelezionata)) {
			// Per la ricerca interessa solo l'uid
			strutturaAmm = new StrutturaAmministrativoContabile();
			strutturaAmm.setUid(Integer.parseInt(strutturaAmministrativaSelezionata));
		}
		
		return strutturaAmm;
	}
	
	/**
	 * esegui inserimento provvedimento
	 * @param oggettoDaPopolare
	 * @param provvedimentoImpegnoModel
	 * @param strutturaAmm
	 * @param tipoAtto
	 * @return
	 */
	protected InserisceProvvedimentoResponse eseguiInserimentoProvvedimento(OggettoDaPopolareEnum oggettoDaPopolare, ProvvedimentoImpegnoModel provvedimentoImpegnoModel,
			StrutturaAmministrativoContabile strutturaAmm, TipoAtto tipoAtto) {
		
		//istanzio la request per il servizio requestInsProvv:
		InserisceProvvedimento requestInsProvv = new InserisceProvvedimento();
		
		requestInsProvv.setEnte(sessionHandler.getEnte());
		requestInsProvv.setRichiedente(sessionHandler.getRichiedente());
		requestInsProvv.setStrutturaAmministrativoContabile(strutturaAmm);
		requestInsProvv.setTipoAtto(tipoAtto);
		
		AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
		attoAmministrativo.setAnno(provvedimentoImpegnoModel.getAnnoProvvedimento());
		attoAmministrativo.setNumero(provvedimentoImpegnoModel.getNumeroProvvedimento().intValue());
		attoAmministrativo.setTipoAtto(tipoAtto);
		attoAmministrativo.setStrutturaAmmContabile(strutturaAmm);
		attoAmministrativo.setOggetto(provvedimentoImpegnoModel.getOggetto());
		
		attoAmministrativo.setStatoOperativo(provvedimentoImpegnoModel.getStato());
		//SIAC-6929
		attoAmministrativo.setBloccoRagioneria(Boolean.FALSE);
		attoAmministrativo.setProvenienza(Constanti.ATTO_AMMINISTRATIVO_PROVENIENZA_MANUALE);
		
		requestInsProvv.setAttoAmministrativo(attoAmministrativo );
		
		//invoco il servizio inserisceProvvedimento:
		return provvedimentoService.inserisceProvvedimento(requestInsProvv);
		
	}
	
	
	/**
	 * Metodo per la compilazione del provvedimento
	 * @param atto
	 * @param listaRicercaProvvedimento
	 * @param listaTrovatiNonAnnullati
	 * @param daAggiungere
	 */
	private void buildProvvedimento(AttoAmministrativo atto, ArrayList<ProvvedimentoImpegnoModel> listaRicercaProvvedimento
			,ArrayList<ProvvedimentoImpegnoModel> listaTrovatiNonAnnullati,boolean daAggiungere){

		//COSTRUSICO L'OGGETTO:
		ProvvedimentoImpegnoModel pim = new ProvvedimentoImpegnoModel();
		pim.setUid(atto.getUid());
		pim.setAnnoProvvedimento(atto.getAnno());
		pim.setNumeroProvvedimento(BigInteger.valueOf(atto.getNumero()));
		pim.setIdTipoProvvedimento(atto.getTipoAtto().getUid());
		pim.setCodiceTipoProvvedimento(atto.getTipoAtto().getCodice());
		pim.setTipoProvvedimento(atto.getTipoAtto().getDescrizione());
		pim.setOggetto(atto.getOggetto());
		pim.setCodiceStrutturaAmministrativa((atto.getStrutturaAmmContabile() != null)? atto.getStrutturaAmmContabile().getCodice() : null);
		pim.setStrutturaAmministrativa((atto.getStrutturaAmmContabile() != null)? atto.getStrutturaAmmContabile().getDescrizione() : null);
		pim.setParereRegolaritaContabile(atto.getParereRegolaritaContabile());
		pim.setBloccoRagioneria(atto.getBloccoRagioneria());
		//SIAC-7299
		pim.setProvenienza(atto.getProvenienza());
		
		
		if(atto.getStrutturaAmmContabile()!=null){
			// setto livello e uid che mi serviranno dopo
			pim.setLivello(atto.getStrutturaAmmContabile().getLivello());
			pim.setUidStrutturaAmm(atto.getStrutturaAmmContabile().getUid());
		}
		pim.setStato(atto.getStatoOperativo());
		//
		
		//lista per JIRA SIAC-4074 (gestire il caso in cui non compilo la struttura ma ne trovo uno e uno solo
		listaTrovatiNonAnnullati.add(pim);
		//
		
		if(daAggiungere){
			listaRicercaProvvedimento.add(pim);
		}
	
	}
	
	
	
	/**
	 * esegui ricerca provvedimento
	 * @param oggettoDaPopolare
	 * @param filtroAtto
	 * @param daRicerca
	 * @return
	 */
	protected boolean eseguiRicercaProvvedimento(OggettoDaPopolareEnum oggettoDaPopolare, ProvvedimentoImpegnoModel filtroAtto, boolean daRicerca) {
		// Costruzione oggetti complessi
		TipoAtto tipoAtto = buildAttoSelezionato(filtroAtto);
		
		// nel caso in cui si seleziona la struttura sulla pagina in maniera manuale ne faccio un passaggio di variabili e lascio 
		// tutto invariato
		if (!strutturaSelezionataSuPaginaiIsVuota()){
			//se non e' vuota:
			strutturaAmministrativaSelezionata = strutturaSelezionataSuPagina;
			
			//SIAC-5619
			if(teSupport==null){
				teSupport = new GestoreTransazioneElementareModel();
			}
			teSupport.setStruttAmmOriginale(strutturaAmministrativaSelezionata);
			//
			
		}
		
		// se arrivo da compilazione guidata la struttura ce l'ho sul model.provvedimento,
		// quindi aggiungo il controllo
		if (strutturaAmministrativaSelezionata == null){
			strutturaAmministrativaSelezionata = (filtroAtto.getUidStrutturaAmm() != null) ? filtroAtto.getUidStrutturaAmm().toString() : null;
		}
		
		StrutturaAmministrativoContabile strutturaAmm = null;
		if (!isEmpty(strutturaAmministrativaSelezionata)) {
			// Per la ricerca interessa solo l'uid
			strutturaAmm = new StrutturaAmministrativoContabile();
			strutturaAmm.setUid(Integer.parseInt(strutturaAmministrativaSelezionata));
		}else{
			//JIRA 1059
			if(!daRicerca && oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_INCASSO)){
				if (!isEmpty(strutturaAmministrativaSelezionataPerOrdInc)) {
					// Per la ricerca interessa solo l'uid
					strutturaAmm = new StrutturaAmministrativoContabile();
					strutturaAmm.setUid(Integer.parseInt(strutturaAmministrativaSelezionataPerOrdInc));
				}
			}
		}
		
		RicercaAtti ricercaAtti = new RicercaAtti();
		ricercaAtti.setAnnoAtto(filtroAtto.getAnnoProvvedimento());
		if(filtroAtto.getNumeroProvvedimento() != null){
			  ricercaAtti.setNumeroAtto(filtroAtto.getNumeroProvvedimento().intValue());
	    }
		ricercaAtti.setTipoAtto(tipoAtto);

		ricercaAtti.setStrutturaAmministrativoContabile(strutturaAmm);
		if(daRicerca || oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_INCASSO)){
			ricercaAtti.setOggetto(filtroAtto.getOggetto());
		}
		
		//istanzio la request per il servizio ricercaProvvedimento:
		RicercaProvvedimento ricercaProvv = new RicercaProvvedimento();
		ricercaProvv.setEnte(sessionHandler.getEnte());
		ricercaProvv.setRichiedente(sessionHandler.getRichiedente());
		ricercaProvv.setRicercaAtti(ricercaAtti);
		
		//EFFETTUO LA CHIAMATA AL SERVIZIO RICERCAPROVVEDIMENTO:
		
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(ricercaProvv);
		
		ArrayList<ProvvedimentoImpegnoModel> listaRicercaProvvedimento = new ArrayList<ProvvedimentoImpegnoModel>();
		ArrayList<ProvvedimentoImpegnoModel> listaTrovatiNonAnnullati = new ArrayList<ProvvedimentoImpegnoModel>();
		
		if(!isFallimento(response) && !isEmpty(response.getListaAttiAmministrativi())){
			for (AttoAmministrativo atto : response.getListaAttiAmministrativi()) {
				if (!atto.getStatoOperativo().equals(StatoOperativoAtti.ANNULLATO.getDescrizione())) {
					
					boolean daAggiungere = true;
					if (!daRicerca && (strutturaAmm ==null || strutturaAmm.getUid()<=0) ) {
						//FIX PER JIRA SIAC-3353 occore gestire il caso in cui non sia indicata la struttura amministrativa:
						daAggiungere = false;
						//no struttura amm indicata
						if(atto!=null && atto.getStrutturaAmmContabile()==null){
							daAggiungere = true;
						}
					}
					/*
					 * SIAC-6929
					 * Sia per la ricerca guidata che per il proseguia fine pagina
					 * i valori ritornati non contengono casi di blocco
					 */
					if(daRicerca){
						//TOLGO QUELLI BLOCCATI
						if(atto.getBloccoRagioneria() == null || (atto.getBloccoRagioneria() != null && !atto.getBloccoRagioneria().booleanValue())){
							buildProvvedimento(atto, listaRicercaProvvedimento, listaTrovatiNonAnnullati, daAggiungere);
						}
					}else{
						buildProvvedimento(atto, listaRicercaProvvedimento, listaTrovatiNonAnnullati, daAggiungere);
					}
				}
			}
		}
		
		//FIX PER JIRA SIAC-4074 (gestire il caso in cui non compilo la struttura ma ne trovo uno e uno solo)
		if(!daRicerca && listaRicercaProvvedimento.isEmpty()){
			if(listaTrovatiNonAnnullati!=null && listaTrovatiNonAnnullati.size()==1){
				//se e solo se listaTrovatiNonAnnullati ne ha uno e uno solo lo uso come provvedimento:
				listaRicercaProvvedimento.add(listaTrovatiNonAnnullati.get(0));
			}
		}
		
		
		
		if(daRicerca){
			model.setListaRicercaProvvedimento(listaRicercaProvvedimento);
		}
		if (listaRicercaProvvedimento.isEmpty()) {
			if(daRicerca){
				addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
			}else {
				
				StringBuffer sbErr = new StringBuffer();
				if(null==ricercaAtti.getAnnoAtto()){
					sbErr.append("");
				}else {
					sbErr.append(ricercaAtti.getAnnoAtto());
				}
			
				if(null==ricercaAtti.getNumeroAtto()){
					sbErr.append("/(Numero Provvedimento non inserito)");
				}else {
					sbErr.append("/"+filtroAtto.getNumeroProvvedimento());
				}
				if(ricercaAtti.getTipoAtto()!= null && filtroAtto.getIdTipoProvvedimento() != null){
					if(model.getListaTipiProvvedimenti()!=null && !model.getListaTipiProvvedimenti().isEmpty()){
						for (int i = 0; i < model.getListaTipiProvvedimenti().size(); i++) {
							TipoAtto ciclo = (TipoAtto)model.getListaTipiProvvedimenti().get(i);
							if(tipoAtto.getUid() == ciclo.getUid()){
								sbErr.append(" e Tipo ="+ciclo.getDescrizione());
							}
						}
					} 
				}
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Provvedimento", sbErr.toString()));		
			}
			
			model.setProvvedimentoTrovato(false);
			return false;
		} else {
			if (!daRicerca) {
				if(listaRicercaProvvedimento.size()>1){
					addErrore(ErroreFin.PIU_RISULTATI_TROVATI.getErrore(""));
					return false;
				}
				/* SIAC-6929
				 * Se presente un solo elemento e bloccato
				 * viene mostrato il messaggio di errore
				 */
				if(listaRicercaProvvedimento.get(0).getBloccoRagioneria()!= null && listaRicercaProvvedimento.get(0).getBloccoRagioneria().booleanValue()){
					addErrore(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore("Numero Provvedimento " + 
							listaRicercaProvvedimento.get(0).getNumeroProvvedimento() + " Oggetto " + listaRicercaProvvedimento.get(0).getOggetto()));
				}
				
				
				
				if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO) || oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_INCASSO)){
					if(listaRicercaProvvedimento.get(0).getStato().equalsIgnoreCase(Constanti.STATO_PROVVISORIO)){
						addErrore(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("gestione ordinativo","definitivo"));
						return false;
					}
				}
				if (oggettoDaPopolare.equals(OggettoDaPopolareEnum.CARTA)) {
					if(listaRicercaProvvedimento.get(0).getStato().equalsIgnoreCase(Constanti.STATO_PROVVISORIO)){
						addErrore(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("gestione carta contabile", "definitivo"));
						return false;
					}										
				}
				
				model.setListaRicercaProvvedimento(null);
				model.setProvvedimentoTrovato(false);
				setProvvedimentoSelezionato(listaRicercaProvvedimento.get(0));
			} else {
				model.setProvvedimentoTrovato(true);
			}
		}
		return true;
	}
	
	/**
	 * controllo servizio provvedimento
	 * @param provvedimento
	 * @param provvedimentoSelezionato
	 * @return
	 */
	protected boolean controlloServizioProvvedimento(ProvvedimentoImpegnoModel provvedimento, boolean provvedimentoSelezionato) {
		// controllo provvedimento !provvedimentoSelezionato &&
		if ( (null != provvedimento.getAnnoProvvedimento() || null != provvedimento.getIdTipoProvvedimento() || null != provvedimento.getNumeroProvvedimento())) {
			if (null != provvedimento.getIdTipoProvvedimento()) {
				provvedimento.setTipoProvvedimento(String.valueOf(provvedimento.getIdTipoProvvedimento()));
			}
			if (!eseguiRicercaProvvedimento(oggettoDaPopolare, provvedimento, false)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * visualizza lista capitoli compatta
	 * @return
	 */
	public boolean visualizzaListaCapitoliCompatta(){
		//questo serve per per gestione lista capitoli completa o compatta
		//dentro la modale di ricerca capitolo:
		return model.isVisualizzaListCapitoliCompatta();
	}
	
	/**
	 * ricerca capitolo uscita da servizio
	 * @param capitoloDiRiferimento
	 * @return
	 */
	protected RicercaSinteticaCapitoloUscitaGestioneResponse ricercaCapitoloUscitaDaServizio(CapitoloImpegnoModel capitoloDiRiferimento) {
		
		RicercaSinteticaCapitoloUscitaGestioneResponse respDiRitorno = new RicercaSinteticaCapitoloUscitaGestioneResponse();
		
		//default false:
		model.setVisualizzaListCapitoliCompatta(false);
		
		int limitePochiElementi = 5;
		int limiteTutti = 10000;//numero sufficientemente alto da essere sicuri che basti a caricare tutti i capitoli richiesti
		
		//1. Chiamata con pochi elementi ma richiedendo tutti i dati.
		
		//istanzio la request per il servizio ricercaSinteticaCapitoloUscitaGestione:
		RicercaSinteticaCapitoloUscitaGestione rPochi = initReqRicercaCapitoloUscitaDaServizio(capitoloDiRiferimento, limitePochiElementi, false);
		//invoco il servizio ricercaSinteticaCapitoloUscitaGestione:
		RicercaSinteticaCapitoloUscitaGestioneResponse respPochi = capitoloUscitaGestioneService.ricercaSinteticaCapitoloUscitaGestione(rPochi);
		
		if(!isFallimento(respPochi)){
			if(respPochi.getTotaleElementi()>limitePochiElementi){
				//gli elementi da caricare sono piu' del limitePochiElementi
				
				//impostiamo la data table con meno elementi nella pop up modale:
				model.setVisualizzaListCapitoliCompatta(true);
				
				//istanzio la request per il servizio ricercaSinteticaCapitoloUscitaGestione:
				RicercaSinteticaCapitoloUscitaGestione rTutti = initReqRicercaCapitoloUscitaDaServizio(capitoloDiRiferimento, limiteTutti, true);
				//invoco il servizio ricercaSinteticaCapitoloUscitaGestione:
				RicercaSinteticaCapitoloUscitaGestioneResponse respTutti = capitoloUscitaGestioneService.ricercaSinteticaCapitoloUscitaGestione(rTutti);
				
				respDiRitorno = respTutti;
				
				
			} else {
				//OK E' BASTATA UNA CHIAMATA PERCHE' IL NUMERO RISULTATI E' MINORE UGUALE A limitePochiElementi
				respDiRitorno = respPochi;
			}
		}
		
		return respDiRitorno;
	} 
	
	/**
	 * Metodo di comodo per ricercaCapitoloUscitaDaServizio
	 * 
	 * @param capitoloDiRiferimento
	 * @param maxElementi
	 * @param datiCompatti
	 * @return
	 */
	private RicercaSinteticaCapitoloUscitaGestione initReqRicercaCapitoloUscitaDaServizio(CapitoloImpegnoModel capitoloDiRiferimento,int maxElementi, boolean datiCompatti){
		RicercaSinteticaCapitoloUscitaGestione r = new RicercaSinteticaCapitoloUscitaGestione();
		
		RicercaSinteticaCapitoloUGest ru = inizializzaRicercaSinteticaCapitoloUGest(capitoloDiRiferimento);
		ParametriPaginazione parametriPaginazionePochi = initPaginazioneRicercaSinteticaCapitoloUGest(maxElementi);
		
		r.setParametriPaginazione(parametriPaginazionePochi);
		
		r.setEnte(sessionHandler.getEnte());
		r.setRichiedente(sessionHandler.getRichiedente());
		r.setRicercaSinteticaCapitoloUGest(ru);
		
		if(datiCompatti){
			r.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
			r.setTipologieClassificatoriRichiesti(TipologiaClassificatore.CDC,TipologiaClassificatore.CDR,
					TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I, TipologiaClassificatore.PDC_II,
					TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V);
		}
		
		return r;
	}
	
	/**
	 * Metodo di comodo per il metod initReqRicercaCapitoloUscitaDaServizio
	 * @param capitoloDiRiferimento
	 * @return
	 */
	private RicercaSinteticaCapitoloUGest inizializzaRicercaSinteticaCapitoloUGest(CapitoloImpegnoModel capitoloDiRiferimento){
		RicercaSinteticaCapitoloUGest ru = new RicercaSinteticaCapitoloUGest();
		ru.setAnnoEsercizio(capitoloDiRiferimento.getAnno());
		if (capitoloDiRiferimento.getNumCapitolo() != null) {
			ru.setNumeroCapitolo(capitoloDiRiferimento.getNumCapitolo());
		}
		if (capitoloDiRiferimento.getArticolo() != null) {
			ru.setNumeroArticolo(capitoloDiRiferimento.getArticolo());
		}
		if (capitoloDiRiferimento.getUeb() != null) {
			ru.setNumeroUEB(capitoloDiRiferimento.getUeb().intValue());
		}
		if (!isEmpty(strutturaAmministrativaSelezionata)) {
			ru.setCodiceStrutturaAmmCont(strutturaAmministrativaSelezionata);
			ru.setCodiceTipoStrutturaAmmCont(tipoStrutturaAmministrativaSelezionata);
		}
		if (!isEmpty(pdcSelezionato)) {
			ru.setCodicePianoDeiConti(pdcSelezionato);
		}
		if(!isEmpty(capitoloDiRiferimento.getTipoFinanziamentoSelezionato())){
			ru.setCodiceTipoFinanziamento(capitoloDiRiferimento.getTipoFinanziamentoSelezionato());
		}
		return ru;
	}
	
	/**
	 * Metodo di comodo per il metod initReqRicercaCapitoloUscitaDaServizio
	 * @param numeroElementi
	 * @return
	 */
	private ParametriPaginazione initPaginazioneRicercaSinteticaCapitoloUGest(int numeroElementi){
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(numeroElementi);
		parametriPaginazione.setNumeroPagina(0);
		return parametriPaginazione;
	}
	
	/**
	 * ricerca capitolo entrata da servizio
	 * @param capitoloDiRiferimento
	 * @return
	 */
	protected RicercaSinteticaCapitoloEntrataGestioneResponse ricercaCapitoloEntrataDaServizio(CapitoloImpegnoModel capitoloDiRiferimento){
		
		RicercaSinteticaCapitoloEntrataGestioneResponse respDiRitorno = new RicercaSinteticaCapitoloEntrataGestioneResponse();
		
		//default false:
		model.setVisualizzaListCapitoliCompatta(false);
		
		int limitePochiElementi = 5;
		int limiteTutti = 10000;//numero sufficientemente alto da essere sicuri che basti a caricare tutti i capitoli richiesti
		
		//1. Chiamata con pochi elementi ma richiedendo tutti i dati
		
		//istanzio la request per il servizio ricercaSinteticaCapitoloEntrataGestione:
		RicercaSinteticaCapitoloEntrataGestione rPochi = initReqRicercaCapitoloEntrataDaServizio(capitoloDiRiferimento, limitePochiElementi, false);
		//invoco il servizio ricercaSinteticaCapitoloEntrataGestione:
		RicercaSinteticaCapitoloEntrataGestioneResponse respPochi = capitoloEntrataGestioneService.ricercaSinteticaCapitoloEntrataGestione(rPochi);
		
		if(!isFallimento(respPochi)){
			if(respPochi.getTotaleElementi()>limitePochiElementi){
				//gli elementi da caricare sono piu' del limitePochiElementi
				
				//impostiamo la data table con meno elementi nella pop up modale:
				model.setVisualizzaListCapitoliCompatta(true);
				
				//istanzio la request per il servizio ricercaSinteticaCapitoloEntrataGestione:
				RicercaSinteticaCapitoloEntrataGestione rTutti = initReqRicercaCapitoloEntrataDaServizio(capitoloDiRiferimento, limiteTutti, true);
				//invoco il servizio:
				RicercaSinteticaCapitoloEntrataGestioneResponse respTutti = capitoloEntrataGestioneService.ricercaSinteticaCapitoloEntrataGestione(rTutti);
				
				respDiRitorno = respTutti;
				
				
			} else {
				//OK E' BASTATA UNA CHIAMATA PERCHE' IL NUMERO RISULTATI E' MINORE UGUALE A limitePochiElementi
				respDiRitorno = respPochi;
			}
		}
		
		return respDiRitorno;
	}
	
	
	/**
	 * Metodo di comodo per ricercaCapitoloUscitaDaServizio
	 * 
	 * @param capitoloDiRiferimento
	 * @param maxElementi
	 * @param datiCompatti
	 * @return
	 */
	private RicercaSinteticaCapitoloEntrataGestione initReqRicercaCapitoloEntrataDaServizio(CapitoloImpegnoModel capitoloDiRiferimento,int maxElementi, boolean datiCompatti){
		RicercaSinteticaCapitoloEntrataGestione r = new RicercaSinteticaCapitoloEntrataGestione();
		
		RicercaSinteticaCapitoloEGest ru = inizializzaRicercaSinteticaCapitoloEGest(capitoloDiRiferimento);
		ParametriPaginazione parametriPaginazionePochi = initPaginazioneRicercaSinteticaCapitoloUGest(maxElementi);
		
		r.setParametriPaginazione(parametriPaginazionePochi);
		
		r.setEnte(sessionHandler.getEnte());
		r.setRichiedente(sessionHandler.getRichiedente());
		r.setRicercaSinteticaCapitoloEntrata(ru);
		
		if(datiCompatti){
			r.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
			r.setTipologieClassificatoriRichiesti(TipologiaClassificatore.CDC,TipologiaClassificatore.CDR,
					TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I, TipologiaClassificatore.PDC_II,
					TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, TipologiaClassificatore.PDC_V);
		}
		
		return r;
	}
	
	/**
	 * Metodo di comodo per il metod initReqRicercaCapitoloEntrataDaServizio
	 * @param capitoloDiRiferimento
	 * @return
	 */
	private RicercaSinteticaCapitoloEGest inizializzaRicercaSinteticaCapitoloEGest(CapitoloImpegnoModel capitoloDiRiferimento){
		RicercaSinteticaCapitoloEGest eg = new RicercaSinteticaCapitoloEGest();
		if (capitoloDiRiferimento.getNumCapitolo() != null) {
			eg.setNumeroCapitolo(capitoloDiRiferimento.getNumCapitolo());
		}
		if (capitoloDiRiferimento.getArticolo() != null) {
			eg.setNumeroArticolo(capitoloDiRiferimento.getArticolo());
		}
		if (capitoloDiRiferimento.getUeb() != null) {
			eg.setNumeroUEB(capitoloDiRiferimento.getUeb().intValue());
		}
		if (!isEmpty(strutturaAmministrativaSelezionata)) {
			eg.setCodiceStrutturaAmmCont(strutturaAmministrativaSelezionata);
			eg.setCodiceTipoStrutturaAmmCont(tipoStrutturaAmministrativaSelezionata);
		}
		if (!isEmpty(pdcSelezionato)) {
			eg.setCodicePianoDeiConti(pdcSelezionato);
		}
		eg.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));		
		
		if(!isEmpty(capitoloDiRiferimento.getTipoFinanziamentoSelezionato())){
			eg.setCodiceTipoFinanziamento(capitoloDiRiferimento.getTipoFinanziamentoSelezionato());
		}
		return eg;
	}
	
	/**
	 * ricerca soggetto da servizio
	 * @param soggettoRiferimento
	 * @return
	 */
	protected RicercaSoggettiResponse ricercaSoggettoDaServizio(RicercaSoggetti soggettoRiferimento) {
		soggettoRiferimento.getParametroRicercaSoggetto().setIncludeModif(false);
		//invoco il servizio ricercaSoggetti:
		return soggettoService.ricercaSoggetti(soggettoRiferimento);
	}
	
	/**
	 * controllo SAC utente decentrato
	 * @param azioneCercata
	 * @param capitolo
	 * @return
	 */
	public boolean controlloSACUtenteDecentrato(String azioneCercata, CapitoloImpegnoModel capitolo) {
		boolean strutturaCompatibileCapitolo = false;
		if (isAzioneDecentrata(azioneCercata)) {
		
			AzioneConsentita azioneConsentita = azioneDecConsentitaIsPresentObj(azioneCercata);
			
			if (azioneConsentita != null) {
				List<StrutturaAmministrativoContabile> strutture = getStruttureAmministrativoContabileByAccount();
				if(!isEmpty(strutture)){
					for (StrutturaAmministrativoContabile currentStruttura : strutture) {
						if (currentStruttura.getUid() == capitolo.getUidStruttura().intValue()) {
							strutturaCompatibileCapitolo = true;
							break;
						}
					}
				}
				
				if(!strutturaCompatibileCapitolo){
					//IN ALTERNATIVA VEDIAMO SE IL CAPITOLO HA DELLE DEROGHE DI VISIBILITA ALLA REGOLA PRINCIPALE:
					strutturaCompatibileCapitolo = controlloCapitoloVisibilitaStruttura(azioneConsentita, capitolo.getUid());
				}
				
			}
		} else {
			strutturaCompatibileCapitolo = true;
		}
		return strutturaCompatibileCapitolo;
	}
	
	/**
	 * 
	 * Valuta la visibilita' secondo le ulteriori regole definite in SiacRBilElemSacVisibilitaFin
	 * 
	 * @param azioneCercata
	 * @param uidCapitolo
	 * @return
	 */
	public boolean controlloCapitoloVisibilitaStruttura(AzioneConsentita azioneConsentita, Integer uidCapitolo) {
		boolean strutturaCompatibileCapitolo = false;
		
		//istanzio la request per il servizio caricaDatiVisibilitaSacCapitolo:
		CaricaDatiVisibilitaSacCapitolo req = new CaricaDatiVisibilitaSacCapitolo();
		req.setUidCapitolo(uidCapitolo);
		req.setRichiedente(sessionHandler.getRichiedente());
		//invoco il servizio caricaDatiVisibilitaSacCapitolo:
		CaricaDatiVisibilitaSacCapitoloResponse resp = genericService.caricaDatiVisibilitaSacCapitolo(req);
		
		if(!isFallimento(resp)){
			
			if(resp.isVisibiliAll()){
				strutturaCompatibileCapitolo = true;
			} else if(!isEmpty(resp.getIdSacVisibili())) {
				List<StrutturaAmministrativoContabile> listaSAC = getStruttureAmministrativoContabileByAccount();
				
				for(Integer idAmmessoIterato : resp.getIdSacVisibili()){
					if(FinUtility.presenteInLista(listaSAC, idAmmessoIterato)){
						strutturaCompatibileCapitolo = true;
						break;
					}
				}
			}
		}
			
		return strutturaCompatibileCapitolo;
	}
	
	/**
	 * controllo sac struttura amm utente decentrato
	 * @param azioneCercata
	 * @param uidStrutturaAmm
	 * @return
	 */
	public boolean controlloSACStrutturaAmmUtenteDecentrato(String azioneCercata, Integer uidStrutturaAmm) {
		boolean strutturaCompatibileCapitolo = false;
		if (isAzioneDecentrata(azioneCercata)) {
			AzioneConsentita azioneConsentita = azioneDecConsentitaIsPresentObj(azioneCercata);
			if (azioneConsentita != null) {
				List<StrutturaAmministrativoContabile> strutture = getStruttureAmministrativoContabileByAccount();
				if(!isEmpty(strutture)){
					// se trova le strutture del decentrato controllo innanzittutto
					// che se uidStrutturaAmm è  null imposto strutturaCompatibileCapitolo a false
					if(uidStrutturaAmm!=null){
						for (StrutturaAmministrativoContabile currentStruttura : strutture) {
							if (currentStruttura.getUid() == uidStrutturaAmm.intValue()) {
								strutturaCompatibileCapitolo = true;
								break;
							}
						}
					}else{
						strutturaCompatibileCapitolo = false;
					}
				}
			}
		} else {
			strutturaCompatibileCapitolo = true;
		}
		return strutturaCompatibileCapitolo;
	}
	
	/**
	 * ricerca guidata impegno
	 * @return
	 */
	public String ricercaGuidataImpegno(){		
		pulisciListeeSchedaPopup();
		model.setInPopup(true);
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		HttpServletRequest request = ServletActionContext.getRequest();		
		
		String annoimpegno = request.getParameter("anno");
		if (annoimpegno != null && !annoimpegno.isEmpty()) {
			try {
				Integer anno = Integer.valueOf(annoimpegno);
				model.setnAnno(anno);
				if (anno <= 1900) {
					listaErrori.add(ErroreCore.PARAMETRO_ERRATO.getErrore("Anno", annoimpegno, ">1900"));
				}
			} catch (NumberFormatException e) {
				listaErrori.add(ErroreFin.VALORE_NON_VALIDO.getErrore("Impegno :Numero ", annoimpegno));
			}
		} else {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Impegno :Anno "));
			model.setnAnno(null);
		}
		String numeroimpegno = request.getParameter("numero");
		if (numeroimpegno != null && !numeroimpegno.isEmpty()) {
			try {
			model.setnImpegno(Integer.valueOf(numeroimpegno));
			} catch (NumberFormatException e) {
				listaErrori.add(ErroreFin.VALORE_NON_VALIDO.getErrore("Impegno :Numero ", numeroimpegno));
			}

		} else {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Impegno :Numero "));
			model.setnImpegno(null);
		}
		
		if (!listaErrori.isEmpty()) {
			addErrori(listaErrori);
			return "refreshPopupModalImpegno";
		}
		
		//istanzio la request per il servizio ricercaImpegnoPerChiaveOttimizzato:
		RicercaImpegnoPerChiaveOttimizzato rip = new RicercaImpegnoPerChiaveOttimizzato();
		rip.setEnte(sessionHandler.getEnte());
		rip.setRichiedente(sessionHandler.getRichiedente());
		RicercaImpegnoK k = new RicercaImpegnoK();
		k.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		if(annoimpegno!=null){
			k.setAnnoImpegno(Integer.valueOf(annoimpegno));
		}
		if(numeroimpegno!=null){
			k.setNumeroImpegno(new BigDecimal(numeroimpegno));
		}
			
		rip.setpRicercaImpegnoK(k);
		
		//Richiedo alcuni dati opzionali:
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaDisponibileLiquidareEDisponibilitaInModifica(true);
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaVociMutuo(true);
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaMutui(true);
		rip.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds );
		
		//esclude il caricamento di tutti i sub con i dati pesanti:
		rip.setCaricaSub(false);
		//
		
		//invoco il servizio ricercaImpegnoPerChiaveOttimizzato:
		RicercaImpegnoPerChiaveOttimizzatoResponse respRk = movimentoGestionService.ricercaImpegnoPerChiaveOttimizzato(rip);
		if (respRk != null && respRk.getImpegno() != null) {
			Impegno impegno = respRk.getImpegno();
				if(impegno.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("D") || impegno.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("N")){
				//metto qui le info per la tabella impegno da visualizzaree sempre
					
					//setting di capitolo, provvedimento e soggetto:
					settaCapitoloProvvedimentoSoggettoPopUp(impegno);
					//
					
					model.setImpegnoPopup(impegno);
					model.setDescrizioneImpegnoPopup(impegno.getDescrizione());
					model.setHasImpegnoSelezionatoPopup(true);
					model.setHasImpegnoSelezionatoXPopup(true);
					List<VoceMutuo> listaVociMutuo = new ArrayList<VoceMutuo>();
					if(impegno!=null && impegno.getListaVociMutuo()!=null && impegno.getListaVociMutuo().size()>0){
						listaVociMutuo = impegno.getListaVociMutuo();
						model.setHasMutui(true);
					}
					model.setListaVociMutuo(listaVociMutuo);
				
				
				//verifico se presenti subimpegni, in questo caso popolo la tabella
				List<Impegno> listaTabellaImpegni = new ArrayList<Impegno>();
				if(!isEmpty(respRk.getElencoSubImpegniTuttiConSoloGliIds())){
					//setto isnSubImpegno variabile utilita' popup
					model.setIsnSubImpegno(true);
					for(int i = 0; i < respRk.getElencoSubImpegniTuttiConSoloGliIds().size(); i++){
						SubImpegno subImp = clone(respRk.getElencoSubImpegniTuttiConSoloGliIds().get(i));
						//il subimpegnp NON ha capitolo quindi uso quello dell'impegno:
						if(subImp.getCapitoloUscitaGestione()==null){
							subImp.setCapitoloUscitaGestione(impegno.getCapitoloUscitaGestione());
						}
						listaTabellaImpegni.add((Impegno)subImp);
					}
				}else{
					listaTabellaImpegni.add(impegno);
					//agiorno nsubImpegno e nisSubImpegno variabile utilita' popup
					model.setnSubImpegno(null);
					model.setIsnSubImpegno(false);
					//ISSUE R35
				}			
				model.setListaImpegniCompGuidata(listaTabellaImpegni);			
			} else{
				//ISSUE R35
				addErrore(ErroreFin.NUMERO_IMPEGNO_NON_VALIDO.getErrore("Impegno in stato: "+ impegno.getDescrizioneStatoOperativoMovimentoGestioneSpesa()));
			}			
		}else{
			listaErrori.add(ErroreFin.MOVIMENTO_NON_TROVATO.getErrore("impegno"));	
			if (!listaErrori.isEmpty()) {
				addErrori(listaErrori);
				model.setInPopup(true);
				return "refreshPopupModalImpegno";
				}
		}
		return MODALIMPEGNO;
	}
	
	/**
	 * Metodo che raccoglie a fattor comune i setting del capitolo pop up
	 * del soggetto pop up e del provvedimento pop up per un impegno appena
	 * trovato tramite conferma alle compilazioni guidate
	 * @param impegno
	 */
	protected void settaCapitoloProvvedimentoSoggettoPopUp(Impegno impegno){
		//CAPITOLO:
		if (impegno.getCapitoloUscitaGestione() != null) {
			model.setCapitoloPopup(impegno.getCapitoloUscitaGestione());
		}
		//PROVVEDIMENTO:
		settaProvvedimentoPopup(impegno.getAttoAmministrativo());
		//SOGGETTO:
		settaSoggettoPopup(impegno.getSoggetto(), impegno.getClasseSoggetto());
	}
	
	/**
	 * Metodo che raccoglie a fattor comune i setting del capitolo pop up
	 * del soggetto pop up e del provvedimento pop up per un accertamento appena
	 * trovato tramite conferma alle compilazioni guidate
	 * @param impegno
	 */
	protected void settaCapitoloProvvedimentoSoggettoPopUp(Accertamento accertamento){
		//CAPITOLO:
		if (accertamento.getCapitoloEntrataGestione() != null) {
			model.setCapitoloEntrataPopup(accertamento.getCapitoloEntrataGestione());
		}
		//PROVVEDIMENTO:
		settaProvvedimentoPopup(accertamento.getAttoAmministrativo());
		//SOGGETTO:
		settaSoggettoPopup(accertamento.getSoggetto(), accertamento.getClasseSoggetto());
	}
	
	protected void settaProvvedimentoPopup(AttoAmministrativo attoAmministrativo){
		if (attoAmministrativo != null	&& attoAmministrativo.getUid() != 0) {
			model.setProvvedimentoPopup(new ProvvedimentoImpegnoModel());
			if(attoAmministrativo!=null){
				model.getProvvedimentoPopup().setAnnoProvvedimento(attoAmministrativo.getAnno());
				model.getProvvedimentoPopup().setNumeroProvvedimento(BigInteger.valueOf(attoAmministrativo.getNumero()));
				if(attoAmministrativo.getTipoAtto()!=null){
					model.getProvvedimentoPopup().setCodiceTipoProvvedimento(attoAmministrativo.getTipoAtto().getCodice());
					model.getProvvedimentoPopup().setTipoProvvedimento(attoAmministrativo.getTipoAtto().getDescrizione());
					model.getProvvedimentoPopup().setIdTipoProvvedimento(attoAmministrativo.getTipoAtto().getUid());
				}
				model.getProvvedimentoPopup().setOggetto(attoAmministrativo.getOggetto());
				model.getProvvedimentoPopup().setStato(attoAmministrativo.getStatoOperativo());
				
				if(attoAmministrativo.getStrutturaAmmContabile()!=null){
					model.getProvvedimentoPopup().setStrutturaAmministrativa(attoAmministrativo.getStrutturaAmmContabile().getCodice());
				}
			}
		}
	}
	
	protected void settaSoggettoPopup(Soggetto soggetto, ClasseSoggetto classeSoggetto){
		if (soggetto != null && soggetto.getCodiceSoggetto() != null && !"".equalsIgnoreCase(soggetto.getCodiceSoggetto())) {
			model.setSoggettoPopup(new SoggettoImpegnoModel());
			model.getSoggettoPopup().setCodCreditore(soggetto.getCodiceSoggetto());
			model.getSoggettoPopup().setDenominazione(soggetto.getDenominazione());
			model.getSoggettoPopup().setCodfisc(soggetto.getCodiceFiscale());
			model.getSoggettoPopup().setStato(soggetto.getStatoOperativo());
			model.getSoggettoPopup().setPiva(soggetto.getPartitaIva());
			if(classeSoggetto!=null){
				model.getSoggettoPopup().setClasse(classeSoggetto.getDescrizione());
			}
			model.setSoggettoSelezionatoPopup(true);
		}
	}
	
	/**
	 * mutui per impegno
	 * @return
	 */
	public String mutuiPerImpegno(){
		//su selezione impegno nella tabella di ricercaImpegni guidata 
		//calcola la lista mutui associata se presente
		HttpServletRequest request = ServletActionContext.getRequest();
		String selection = request.getParameter("selection");
		Impegno impegnoSelezionato = new Impegno();
		if(model.getListaImpegniCompGuidata()!=null){
			for(int i=0; i<model.getListaImpegniCompGuidata().size();i++){
				if(model.getListaImpegniCompGuidata().get(i)!=null){
					int uid = model.getListaImpegniCompGuidata().get(i).getUid();
					if(uid==Integer.valueOf(selection)){
						model.setRadioImpegnoSelezionato(Integer.valueOf(selection));
						model.setHasImpegnoSelezionatoPopup(true);
						model.setHasImpegnoSelezionatoXPopup(true);
						impegnoSelezionato = model.getListaImpegniCompGuidata().get(i);
						if(model.isIsnSubImpegno()){
							//agiorno, essendo sub impegno, nImpegno e nsubImpegno variabile utilita' popup
							model.setnImpegno(model.getnImpegno());
							if(impegnoSelezionato.getNumero()!=null)
								model.setnSubImpegno(impegnoSelezionato.getNumero().intValue());
						}
						model.setImpegnoPopup(impegnoSelezionato);
					}
				}
			}
		}
		List<VoceMutuo> listaVociMutuo = new ArrayList<VoceMutuo>();
		if(impegnoSelezionato!=null){
			listaVociMutuo = impegnoSelezionato.getListaVociMutuo();
			model.setHasMutui(true);
		}
		model.setListaVociMutuo(listaVociMutuo);
		return MODALIMPEGNO;
	}
	
	/**
	 * conferma comp guidata
	 * @return
	 */
	public String confermaCompGuidata(){
		
		// Impelmentare la logica sul pulsante conferma che dovrebbe settare 
		// SOLO i quattro valori numero impegno,
		// numero sub impegno anno e numero mutuo su step1.
		
		if(model.isHasImpegnoSelezionatoPopup()){
			
			//FLAG IMPEGNO APPENA SELEZIONATO:
			model.setImpegnoAppenaSelezionatoDaCompGuidata(true);
			//
			
			model.setNumeroImpegno(model.getnImpegno());
			model.setAnnoImpegno(model.getnAnno());
			model.setNumeroSub(model.getnSubImpegno());
			
			String descrizioneTipoImpegnoPopup = (model.getImpegnoPopup().getTipoImpegno()!= null && !isEmpty(model.getImpegnoPopup().getTipoImpegno().getDescrizione())) ? model.getImpegnoPopup().getTipoImpegno().getDescrizione() : "" ;
			
			model.setDescrizioneTipoImpegnoPopup(descrizioneTipoImpegnoPopup);
			model.setNumeroMutuoPopup(null);
			model.setnAnno(null);
			model.setnImpegno(null);
			int voceMutuoScelta = model.getRadioVoceMutuoSelezionata();
			List<VoceMutuo> listaVocMutuo = model.getListaVociMutuo();
			
			if(!isEmpty(listaVocMutuo)){
				for(int j=0; j<listaVocMutuo.size();j++){
					if(listaVocMutuo.get(j).getUid()==voceMutuoScelta){
						model.setNumeroMutuoPopup(Integer.valueOf(listaVocMutuo.get(j).getNumeroMutuo()));
						model.setDisponibilita(listaVocMutuo.get(j).getImportoDisponibileLiquidareVoceMutuo());
					}
				}
			}
		}
		
		// salvo il capitole del impegno per caricate TE di una nuova liquidazione
		if(!isEmpty(model.getListaImpegniCompGuidata())){
			
			if(model.getRadioImpegnoSelezionato()==0){
				model.setRadioImpegnoSelezionato(model.getListaImpegniCompGuidata().get(0).getUid());
			}
			for (Impegno itImp : model.getListaImpegniCompGuidata()) {
				
				if(itImp.getUid()==model.getRadioImpegnoSelezionato()){
					
					model.setCapitoloPopup(itImp.getCapitoloUscitaGestione());
					
					// campi TE
					model.getCapitoloPopup().setClassificazioneCofog(new ClassificazioneCofog(itImp.getCodCofog(),""));
					model.getCapitoloPopup().setTransazioneUnioneEuropeaSpesa(new TransazioneUnioneEuropeaSpesa(itImp.getCodTransazioneEuropeaSpesa(),""));
					model.getCapitoloPopup().setRicorrenteSpesa(new RicorrenteSpesa(itImp.getCodRicorrenteSpesa(),""));
					model.getCapitoloPopup().setPerimetroSanitarioSpesa(new PerimetroSanitarioSpesa(itImp.getCodCapitoloSanitarioSpesa(),""));
					model.getCapitoloPopup().setPoliticheRegionaliUnitarie(new PoliticheRegionaliUnitarie(itImp.getCodPrgPolReg(),""));
					
					model.getCapitoloPopup().setSiopeSpesa(new SiopeSpesa(itImp.getCodSiope(), itImp.getDescCodSiope()));
					
					// salvo cup
					model.setCupImpegnoSelezionato(itImp.getCup());
					model.setCapitoloPopupNuovaLiquidazioneOrdinativo(model.getCapitoloPopup());
					model.setImpegnoPopUpNuovaLiquidazioneOrdinativo(itImp);
					break;
				}				
			}		
				}

		pulisciListeeSchedaPopup();
		return INPUT;
	}
	
	/**
	 * pulisci liste e scheda popup.
	 * 
	 * NON DEVE PULIRE QUESTI:
	 *  model.setnAnno(null)
	 *	model.setnImpegno(null)
	 * PERCHE' LO FA IL CHIAMANTE SOLO NEL CASO DI PAGINA CON MODALI MULTIPLE,
	 * IN CASO DI NORMALE SINGOLA DEVONO RIPRESENTARSI ALL'APERTURA DELLA MODALE
	 * 
	 */
	public void pulisciListeeSchedaPopup(){
		//Azzero liste e info precedenti
		model.setListaImpegniCompGuidata(new ArrayList<Impegno>());
		model.setListaVociMutuo(new ArrayList<VoceMutuo>());
		model.setRadioImpegnoSelezionato(0);
		model.setRadioVoceMutuoSelezionata(0);
		model.setHasImpegnoSelezionatoPopup(false);
		model.setHasMutui(false);
		model.setIsnSubImpegno(false);
		model.setnSubImpegno(null);
		model.setProvvedimentoPopup(null);
		model.setSoggettoPopup(null);
		model.setImpegnoPopup(null);
		model.setCapitoloPopup(null);
		
		//NOVEMBRE 2017 INTRODOTTO ANCHE L'ACCERTAMENTO POPUP:
		model.setListaAccertamentiCompGuidata(new ArrayList<Accertamento>());
		model.setAccertamentoPopup(null);
		model.setCapitoloEntrataPopup(null);
	}
	
	/**
	 * controllo stato bilancio
	 * @param anno
	 * @param doveMiTrovo
	 * @param oggettoDaPopolare
	 * @return
	 */
	protected boolean controlloStatoBilancio(Integer anno,String doveMiTrovo,String oggettoDaPopolare){
		if(sessionHandler.getFaseBilancio()!= null && sessionHandler.getFaseBilancio().equalsIgnoreCase("O") 
				&& anno != null && anno > sessionHandler.getBilancio().getAnno()){
			addErrore(ErroreFin.VALORE_NON_VALIDO.getErrore("anno movimento",": "+oggettoDaPopolare+" futuro non ammesso in doppia gestione"));
			return true;
		} 
		if(sessionHandler.getFaseBilancio()!= null && !(sessionHandler.getFaseBilancio().equalsIgnoreCase("E") ||  
				sessionHandler.getFaseBilancio().equalsIgnoreCase("G") || 
				sessionHandler.getFaseBilancio().equalsIgnoreCase("A") || 
				sessionHandler.getFaseBilancio().equalsIgnoreCase("O"))){
			StringTokenizer st = new StringTokenizer(sessionHandler.getDescrizioneAnnoBilancio(),"-");
			st.nextElement();
			String stato= st.nextElement().toString();
			addErrore(ErroreFin.OPERAZIONE_INCOMPATIBILE_PER_BILANCIO.getErrore(doveMiTrovo+" "+oggettoDaPopolare,stato));
			return true;
		}
		return false;
	}
	
	/**
	 * ripopola provvedimento da support
	 */
	protected void ripopolaProvvedimentoDaSupport(){
		if(model instanceof GestisciMovGestModel && ((GestisciMovGestModel)model).getStep1Model().getProvvedimentoSupport()!=null){
			AttoAmministrativo attoImpegno = popolaProvvedimento(((GestisciMovGestModel)model).getStep1Model().getProvvedimentoSupport());
			impostaProvvNelModel(attoImpegno, ((GestisciMovGestModel)model).getStep1Model().getProvvedimento());
		}
	}
	
	/**
	 * Si occupa di settare i dati nel model indicato in provvModel
	 * a partire dai dati letti in AttoAmministrativo provvedimento
	 * @param provvedimento
	 * @param provvModel
	 */
	protected void impostaProvvNelModel(AttoAmministrativo provvedimento, ProvvedimentoImpegnoModel provvModel){
		
		if(provvedimento!=null && provvModel!=null){
			
			//DATI GENERALI DEL PROVVEDIMENTO
			
			//ANNO:
			if(provvedimento.getAnno()!=0){ 
				provvModel.setAnnoProvvedimento(provvedimento.getAnno());
			}
			
			//NUMERO:
			if(provvedimento.getNumero()!=0){ 
				provvModel.setNumeroProvvedimento(BigInteger.valueOf(provvedimento.getNumero()));
			}
			
			//TIPO ATTO:
			if(provvedimento.getTipoAtto() != null){
				provvModel.setIdTipoProvvedimento(provvedimento.getTipoAtto().getUid());
				provvModel.setTipoProvvedimento(provvedimento.getTipoAtto().getDescrizione());
				provvModel.setCodiceTipoProvvedimento(provvedimento.getTipoAtto().getCodice());
			}
			
			//STATO OPERATIVO:
			provvModel.setStato(provvedimento.getStatoOperativo());
			//OGGETTO:
			provvModel.setOggetto(provvedimento.getOggetto());
			
			//SIAC-6929
			provvModel.setUid(provvedimento.getUid());
			
			
			//DATI DELLA STRUTTURA AMMINISTRATIVA:
			if(provvedimento.getStrutturaAmmContabile()!=null && provvedimento.getStrutturaAmmContabile().getDescrizione() != null){
				//1. setto la struttura amministrativa 
				provvModel.setStrutturaAmministrativa(provvedimento.getStrutturaAmmContabile().getDescrizione());
				provvModel.setCodiceStrutturaAmministrativa(provvedimento.getStrutturaAmmContabile().getCodice());
				provvModel.setLivello(provvedimento.getStrutturaAmmContabile().getLivello());
				provvModel.setUidStrutturaAmm(provvedimento.getStrutturaAmmContabile().getUid());
				
				//2. cerco il padre nell'albero:
				//il cerca id padre va esiguito ora non metterlo prima delle istruzioni precedenti:
				String codiceStrutturaAmministrativaPadre = cercaIdPadre(provvModel);
				
				//3. setto gli altri dati della struttura:
				provvModel.setCodiceStrutturaAmministrativaPadre(codiceStrutturaAmministrativaPadre);
			}
			
			//PARERE REGOLARITA':
			if(provvedimento.getParereRegolaritaContabile()!=null){
				provvModel.setParereRegolaritaContabile(provvedimento.getParereRegolaritaContabile());
			}
			
			
			//SIAC 7321
			provvModel.setProvenienza(provvedimento.getProvenienza());
			
		}
		
	}
	
	/**
	 * Se il codice della struttura padre e' incoerente viene aggiornato
	 * @param provvModel
	 */
	protected void reimpostaCodiceStrutturaPadre(ProvvedimentoImpegnoModel provvModel){
		if(		provvModel!=null && 
				provvModel.getCodiceStrutturaAmministrativaPadre()==null && 
				provvModel.getLivello()!=null &&
				provvModel.getLivello().intValue()>1
				){
			String codiceStrutturaAmministrativaPadre = cercaIdPadre(provvModel);
			provvModel.setCodiceStrutturaAmministrativaPadre(codiceStrutturaAmministrativaPadre);
		}
	}
	
	/**
	 * Chek se la variabile strutturaselezionasupagina e' vuota o meno
	 * @return
	 */
	private boolean strutturaSelezionataSuPaginaiIsVuota(){
		if (!isEmpty(strutturaSelezionataSuPagina) && FinStringUtils.contieneSoloNumeri(strutturaSelezionataSuPagina)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Dato il model di un provvedimento ne estrapola i dati e popola un model AttoAmministrativo
	 * @param provvModel
	 * @return
	 */
	protected AttoAmministrativo popolaProvvedimento(ProvvedimentoImpegnoModel provvModel){
		// provvedimento
		AttoAmministrativo am = null;
		if(provvModel!=null){

			if(null!=provvModel.getAnnoProvvedimento()){
				//Istanzio l'oggetto:
				am = new AttoAmministrativo();
				
				if(provvModel.getUid()!=null && provvModel.getUid().intValue()>0){
					am.setUid(provvModel.getUid());
				}
				
				//Numero, anno e stato operativo:
				am.setNumero(provvModel.getNumeroProvvedimento().intValue());
				am.setAnno(provvModel.getAnnoProvvedimento());
				am.setStatoOperativo(provvModel.getStato());
				
				//Tipo atto:
				@SuppressWarnings("unchecked")
				List<TipoAtto> listaTipoAtto = model.getListaTipiProvvedimenti();
				TipoAtto ta = new TipoAtto(); 
				
				if(listaTipoAtto!=null && !listaTipoAtto.isEmpty()){
					
					for (TipoAtto tipoAtto : listaTipoAtto) {
						
						if(tipoAtto.getUid() == provvModel.getIdTipoProvvedimento()){
							
							ta = tipoAtto;
							break;
						}
						
					}
				}
				
				am.setBloccoRagioneria(provvModel.getBloccoRagioneria());
				
				am.setTipoAtto(ta);
				
				//Struttura amministrativa selezionata
				if(provvModel.getUidStrutturaAmm()!=null){
					StrutturaAmministrativoContabile sac = new StrutturaAmministrativoContabile();
					sac.setUid(provvModel.getUidStrutturaAmm());
					sac.setCodice(provvModel.getCodiceStrutturaAmministrativa());
					sac.setDescrizione(provvModel.getStrutturaAmministrativa());
					am.setStrutturaAmmContabile(sac);
				}
				
				//FLAG PARERE:
				am.setParereRegolaritaContabile(provvModel.isParereRegolaritaContabile());
				
			}
		}
		return am;
	}

	/**
	 * Se il codice della struttura SELEZIONATA e incoerente viene aggiornata
	 * usato nell'execute della action dove deve essere maneggiato il provvedimendo tornando dal tasto indietro
	 * @param provvModel
	 */
	protected void reimpostaCodiceStrutturaSelezionata(ProvvedimentoImpegnoModel provvModel){
		if(provvModel!=null){
			//il model e' diverso da null
			if(provvModel.getCodiceStrutturaAmministrativa()==null && !strutturaSelezionataSuPaginaiIsVuota()){
				strutturaSelezionataSuPagina = null;
			} else if(provvModel.getCodiceStrutturaAmministrativa()!=null && strutturaSelezionataSuPaginaiIsVuota() && provvModel.getUidStrutturaAmm()!=null){
				strutturaSelezionataSuPagina = provvModel.getUidStrutturaAmm().toString();
			}
		} else {
			//il model e' null
			if(!strutturaSelezionataSuPaginaiIsVuota()){
				strutturaSelezionataSuPagina = null;
			}
		}
	}
	
	/**
	 * Copia i dati dal model di action indicato dentro all'oggetto ModificaMovimentoGestione indicato
	 * @param spesa
	 */
	protected <MVG extends ModificaMovimentoGestione> MVG settaDatiProvvDalModel(MVG modMovGest, ProvvedimentoImpegnoModel provvModel){
		modMovGest.setAttoAmministrativoAnno(String.valueOf(provvModel.getAnnoProvvedimento()));
		modMovGest.setAttoAmministrativoNumero(provvModel.getNumeroProvvedimento().intValue());
		if(!isEmpty(provvModel.getCodiceTipoProvvedimento())){
			modMovGest.setAttoAmministrativoTipoCode(String.valueOf(provvModel.getCodiceTipoProvvedimento()));
		} else {	
			modMovGest.setAttoAmministrativoTipoCode(String.valueOf(provvModel.getTipoProvvedimento()));
		}	
		modMovGest.setIdStrutturaAmministrativa(provvModel.getUidStrutturaAmm());
		AttoAmministrativo attoAmm = popolaProvvedimento(provvModel);
		modMovGest.setAttoAmministrativo(attoAmm);
		
		//va tenuto coerente:
		TipoAtto attoAmmTipoAtto = attoAmm.getTipoAtto();
		modMovGest.setAttoAmmTipoAtto(attoAmmTipoAtto);
		//
		
		return modMovGest;
	}
	
	/**
	 * converti capitolo to capitolo model
	 * @param capitoloUscitaTrovato
	 * @return
	 */
	protected CapitoloImpegnoModel convertiCapitoloToCapitoloModel(CapitoloUscitaGestione capitoloUscitaTrovato){
		
		CapitoloImpegnoModel capitolo = new CapitoloImpegnoModel();
		
		if(capitoloUscitaTrovato.getAnnoCapitolo()!=null){ 
			capitolo.setAnno(capitoloUscitaTrovato.getAnnoCapitolo());
		}
		
		if(capitoloUscitaTrovato.getNumeroCapitolo()!=null){ 
			capitolo.setNumCapitolo(capitoloUscitaTrovato.getNumeroCapitolo());
		}
		
		if(capitoloUscitaTrovato.getNumeroArticolo()!=null){ 
			capitolo.setArticolo(capitoloUscitaTrovato.getNumeroArticolo());
		}
		
		if(capitoloUscitaTrovato.getNumeroUEB()!=null){ 
			capitolo.setUeb(new BigInteger(String.valueOf(capitoloUscitaTrovato.getNumeroUEB())));
		}
		
		if(capitoloUscitaTrovato.getUid()!=0){ 
			capitolo.setUid(capitoloUscitaTrovato.getUid());
		}
		
		if(capitoloUscitaTrovato.getTipoFinanziamento()!=null){ 
			capitolo.setTipoFinanziamento(capitoloUscitaTrovato.getTipoFinanziamento().getDescrizione());
		}
		
		if (capitoloUscitaTrovato.getElementoPianoDeiConti() != null) {
			capitolo.setCodicePdcFinanziario(capitoloUscitaTrovato.getElementoPianoDeiConti().getCodice());
			capitolo.setDescrizionePdcFinanziario(capitoloUscitaTrovato.getElementoPianoDeiConti().getDescrizione());
			capitolo.setIdPianoDeiConti(capitoloUscitaTrovato.getElementoPianoDeiConti().getUid());
		}
		
		if (capitoloUscitaTrovato.getMacroaggregato() != null) {
			capitolo.setIdMacroAggregato(capitoloUscitaTrovato.getMacroaggregato().getUid());
			 teSupport.setIdMacroAggregato(capitoloUscitaTrovato.getMacroaggregato().getUid());
		}//non macroaggragato per capEntrata
		
		if (capitoloUscitaTrovato.getMissione() != null) {
			capitolo.setCodiceMissione(capitoloUscitaTrovato.getMissione().getCodice());
			capitolo.setDescrizioneMissione(capitoloUscitaTrovato.getMissione().getDescrizione());
		}
		
		if (capitoloUscitaTrovato.getProgramma() != null) {
			capitolo.setCodiceProgramma(capitoloUscitaTrovato.getProgramma().getCodice());
			capitolo.setDescrizioneProgramma(capitoloUscitaTrovato.getProgramma().getDescrizione());
			capitolo.setIdProgramma(capitoloUscitaTrovato.getProgramma().getUid());
		} 
		
		if (capitoloUscitaTrovato.getDescrizione() != null) {
			capitolo.setDescrizione(capitoloUscitaTrovato.getDescrizione());
		}
	
		if (capitoloUscitaTrovato.getElementoPianoDeiConti() != null) {
			capitolo.setElementoPianoDeiConti(capitoloUscitaTrovato.getElementoPianoDeiConti());
		}
		
		if (capitoloUscitaTrovato.getTipoFinanziamento()!= null) {
			capitolo.setFinanziamento(capitoloUscitaTrovato.getTipoFinanziamento());
		}
		
		if (capitoloUscitaTrovato.getStrutturaAmministrativoContabile()!= null && capitoloUscitaTrovato.getStrutturaAmministrativoContabile().getDescrizione() != null) {
			capitolo.setCodiceStrutturaAmministrativa(capitoloUscitaTrovato.getStrutturaAmministrativoContabile().getCodice());
			capitolo.setDescrizioneStrutturaAmministrativa(capitoloUscitaTrovato.getStrutturaAmministrativoContabile().getDescrizione());
			capitolo.setUidStruttura(capitoloUscitaTrovato.getStrutturaAmministrativoContabile().getUid());
		}
		
		//forse serve solo questo
		if(!isEmpty(capitoloUscitaTrovato.getListaImportiCapitolo())){
			capitolo.setImportiCapitoloUG(capitoloUscitaTrovato.getListaImportiCapitolo());
			ImportiCapitoloModel supportImporti;
			for (ImportiCapitoloUG currentImporto : capitoloUscitaTrovato.getListaImportiCapitolo()) {
				supportImporti = new ImportiCapitoloModel();
				supportImporti.setAnnoCompetenza(currentImporto.getAnnoCompetenza());
				supportImporti.setCassa(currentImporto.getStanziamentoCassa());
				supportImporti.setResiduo(currentImporto.getStanziamentoResiduo());
				supportImporti.setCompetenza(currentImporto.getStanziamento());
				
				if (currentImporto.getAnnoCompetenza().intValue() == capitolo.getAnno().intValue()) {
					capitolo.setDisponibileAnno1(capitoloUscitaTrovato.getListaImportiCapitoloUG().get(0).getDisponibilitaImpegnareAnno1());
				} else if (currentImporto.getAnnoCompetenza().intValue() == (capitolo.getAnno().intValue() + 1)) {
					capitolo.setDisponibileAnno2(capitoloUscitaTrovato.getListaImportiCapitoloUG().get(0).getDisponibilitaImpegnareAnno2());
				} else {
					capitolo.setDisponibileAnno3(capitoloUscitaTrovato.getListaImportiCapitoloUG().get(0).getDisponibilitaImpegnareAnno3());
				}
				capitolo.getImportiCapitolo().add(supportImporti);
			}
		} 
	
		return capitolo;
	}
	
	/**
	 * Routine a fattor comune da riutilizzare dopo aver chiamato il ricerca soggetto per chiave e si vuole
	 * presentare le modalita di pagamento nel tendino di scelta
	 * @param response
	 * @return
	 */
	protected ArrayList<ModalitaPagamentoSoggetto> filtraModalitaPagamentoSoggettoPerMascheraWeb(RicercaSoggettoPerChiaveResponse response){
		List<ModalitaPagamentoSoggetto> listaDaFiltrare = response.getListaModalitaPagamentoSoggetto();
		ArrayList<ModalitaPagamentoSoggetto> listMod= FinUtility.filtraValidi(listaDaFiltrare);
		listMod = impostaUuiModPagPerQuelleDiCessione(listMod);
		return listMod;
	}
	
	/**
	 * Il server bil restiuisce l'id della relazione siac_r_soggetto_relaz per quelli di cessione, va raggirato...
	 * @param listMod
	 * @return
	 */
	protected ArrayList<ModalitaPagamentoSoggetto> impostaUuiModPagPerQuelleDiCessione(ArrayList<ModalitaPagamentoSoggetto> listMod){
		ArrayList<ModalitaPagamentoSoggetto> listModRicomposta= new ArrayList<ModalitaPagamentoSoggetto>();
		if(!isEmpty(listMod)){
			for(ModalitaPagamentoSoggetto it: listMod){
				ModalitaPagamentoSoggetto modPag = it;
				int uidModPag = it.getUid();
				if(it.getModalitaPagamentoSoggettoCessione2()!=null){
					uidModPag = it.getModalitaPagamentoSoggettoCessione2().getUid();
				}
				modPag.setUid(uidModPag);
				listModRicomposta.add(modPag);
			}
		}
		return listModRicomposta;
	}
	
	/**
	 * Ottimizzazione nel caricamento dei classificatori , qui carichiamo solo il tipo finanziamento
	 * @return
	 */
	public boolean  caricaListaTipoFinanziamento() {
		
		RicercaSinteticaClassificatoreResponse res = caricaClassifGenericiBilFiltrati(TipologiaClassificatore.TIPO_FINANZIAMENTO);
	   
		if(res.isFallimento() || (res.getErrori() != null && !res.getErrori().isEmpty())) {
			
	   		addErrori("ricercaOrdinativo", res);
			
	   		return false;
		
	   	}else{
	   		
	   		List<TipoFinanziamento> listaTipiFinanziamento = new ArrayList<TipoFinanziamento>();
	   		
		   	if (res.getCodifiche() != null && !res.getCodifiche().isEmpty()) {
		   		for (Codifica codifica : res.getCodifiche()) {
		   			TipoFinanziamento tipoFinanziamento = new TipoFinanziamento();
		   			
		   			tipoFinanziamento.setUid(codifica.getUid());
		   			tipoFinanziamento.setCodice(codifica.getCodice());
		   			tipoFinanziamento.setDescrizione(codifica.getDescrizione());
		   			
		   			listaTipiFinanziamento.add(tipoFinanziamento);
				}
				
			}
		   	
		   	model.setListaTipoFinanziamento(listaTipiFinanziamento);
		}
	   	
	   	return true;
	}


	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */
	
	public String getRadioCodiceSoggetto() {
		return radioCodiceSoggetto;
	}

	public void setRadioCodiceSoggetto(String radioCodiceSoggetto) {
		this.radioCodiceSoggetto = radioCodiceSoggetto;
	}

	public String getRadioCodiceCapitolo() {
		return radioCodiceCapitolo;
	}

	public void setRadioCodiceCapitolo(String radioCodiceCapitolo) {
		this.radioCodiceCapitolo = radioCodiceCapitolo;
	}

	public StatoOperativoAnagrafica getStatoSoggettoSelezionato() {
		return statoSoggettoSelezionato;
	}

	public void setStatoSoggettoSelezionato(
			StatoOperativoAnagrafica statoSoggettoSelezionato) {
		this.statoSoggettoSelezionato = statoSoggettoSelezionato;
	}

	public String getStrutturaAmministrativaSelezionata() {
		return strutturaAmministrativaSelezionata;
	}

	public void setStrutturaAmministrativaSelezionata(
			String strutturaAmministrativaSelezionata) {
		this.strutturaAmministrativaSelezionata = strutturaAmministrativaSelezionata;
	}

	public String getPdcSelezionato() {
		return pdcSelezionato;
	}

	public void setPdcSelezionato(String pdcSelezionato) {
		this.pdcSelezionato = pdcSelezionato;
	}

	public String getRadioCodiceProvvedimento() {
		return radioCodiceProvvedimento;
	}

	public void setRadioCodiceProvvedimento(String radioCodiceProvvedimento) {
		this.radioCodiceProvvedimento = radioCodiceProvvedimento;
	}

	public String getTipoStrutturaAmministrativaSelezionata() {
		return tipoStrutturaAmministrativaSelezionata;
	}

	public void setTipoStrutturaAmministrativaSelezionata(
			String tipoStrutturaAmministrativaSelezionata) {
		this.tipoStrutturaAmministrativaSelezionata = tipoStrutturaAmministrativaSelezionata;
	}

	public String getRadioCodiceImpegno() {
		return radioCodiceImpegno;
	}

	public void setRadioCodiceImpegno(String radioCodiceImpegno) {
		this.radioCodiceImpegno = radioCodiceImpegno;
	}

	public String getStrutturaAmministrativaSelezionataPerOrdInc() {
		return strutturaAmministrativaSelezionataPerOrdInc;
	}

	public void setStrutturaAmministrativaSelezionataPerOrdInc(
			String strutturaAmministrativaSelezionataPerOrdInc) {
		this.strutturaAmministrativaSelezionataPerOrdInc = strutturaAmministrativaSelezionataPerOrdInc;
	}

	public String getStrutturaSelezionataSuPagina() {
		return strutturaSelezionataSuPagina;
	}

	public void setStrutturaSelezionataSuPagina(String strutturaSelezionataSuPagina) {
		this.strutturaSelezionataSuPagina = strutturaSelezionataSuPagina;
	}

	public String getRadioCodiceProgetto() {
		return radioCodiceProgetto;
	}

	public void setRadioCodiceProgetto(String radioCodiceProgetto) {
		this.radioCodiceProgetto = radioCodiceProgetto;
	}
	public int getRadioUidCronoprogramma() {
		return radioUidCronoprogramma;
	}
	public void setRadioUidCronoprogramma(int radioUidCronoprogramma) {
		this.radioUidCronoprogramma = radioUidCronoprogramma;
	}
	
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.commons.GestoreTransazioneElementareModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.NuovaLiquidazioneModel;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceLiquidazioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiaveResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.ric.RicercaLiquidazioneK;
import it.csi.siac.siacfinser.model.soggetto.ClassificazioneSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class NuovaLiquidazioneOrdinativoAction extends ActionKeyGestioneOrdinativoPagamentoAction{

	
	private static final long serialVersionUID = 1L;

	private static final String VAI_STEP2 = "vaiStep2";
	
	public NuovaLiquidazioneOrdinativoAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO;
	}
	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		this.model.setTitolo("Inserimento liquidazione ordinativo");
	}	
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		annullaInserimentoNuovaLiquidazione();
		
		
		if (caricaListeBil(WebAppConstants.CAP_UG)) {
			return INPUT;
		}
		teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.LIQUIDAZIONE.toString());
		
		
		if (presenzaQuoteCompetenza()) {
			model.getNuovaLiquidazioneModel().setAnnoImpegno(sessionHandler.getAnnoEsercizio());
		}
		
		return SUCCESS;
	}
	
	
	
	public String indietroDaLiquidazione(){
		
		// se premo tasto indietro e non ci sono delle quote devo ripulire la TE
		
		if(!presenzaQuote()){
			
			setTeSupport(new GestoreTransazioneElementareModel());
		}
		
		
		return "vaiStep2";
	}
	/**
	 * carica e setta i dati della TE
	 * @param cug
	 */
	private void caricaTE(CapitoloUscitaGestione cug){
		
		 CapitoloImpegnoModel supportCapitolo = caricaDatiCapitolo(cug);
		
		 caricaListaCofog(supportCapitolo.getIdProgramma());
		 
		 // lista missione
		  caricaMissione(supportCapitolo);
		  caricaProgramma(supportCapitolo);
		
		  // missione
		  teSupport.setMissioneSelezionata(supportCapitolo.getCodiceMissione()); 
		  // programma
		  teSupport.setProgrammaSelezionato(supportCapitolo.getCodiceProgramma());
		  // piano dei conti
		  teSupport.setPianoDeiConti(new ElementoPianoDeiConti());
		  teSupport.getPianoDeiConti().setCodice(supportCapitolo.getCodicePdcFinanziario());
		  teSupport.getPianoDeiConti().setDescrizione(supportCapitolo.getCodicePdcFinanziario()+" - "+supportCapitolo.getDescrizionePdcFinanziario());
		  teSupport.getPianoDeiConti().setUid(supportCapitolo.getIdPianoDeiConti());
		  teSupport.setIdMacroAggregato(supportCapitolo.getIdPianoDeiConti());
		  
		  // CR-2023 eliminato conto economico
		  
		  // cofog 
		  teSupport.setCofogSelezionato(supportCapitolo.getCodiceClassificazioneCofog());
		  
		  // codice transazione europea
		  teSupport.setTransazioneEuropeaSelezionato(supportCapitolo.getCodiceTransazioneEuropeaSpesa());
		  // siope
		  if(supportCapitolo.getCodiceSiopeSpesa()!=null && !supportCapitolo.getCodiceSiopeSpesa().equals("")){
			  SiopeSpesa siopeSpesa = new SiopeSpesa(supportCapitolo.getCodiceSiopeSpesa(), supportCapitolo.getDescrizioneSiopeSpesa());
			  teSupport.setSiopeSpesa( siopeSpesa);
		  }
		  // cup
		  teSupport.setCup(supportCapitolo.getCup());
		  // ricorrente spesa
		  teSupport.setRicorrenteSpesaSelezionato(supportCapitolo.getCodiceRicorrenteSpesa());
		  
		  // capitolo sanitario
		  teSupport.setPerimetroSanitarioSpesaSelezionato(supportCapitolo.getCodicePerimetroSanitarioSpesa());
		  
		  // politiche regionali unitarie
		  teSupport.setPoliticaRegionaleSelezionato(supportCapitolo.getCodicePoliticheRegionaliUnitarie());
		
	}

	/**
	 * annulla i dati della nuova liquidazione che si sta cercando di inserire
	 * @return
	 */
	public String annullaInserimentoNuovaLiquidazione(){
		model.setNuovaLiquidazioneModel(new NuovaLiquidazioneModel());
		model.setAnnoImpegno(null);
		model.setNumeroImpegno(null);
		model.setNumeroSub(null);
		model.setDescrizioneImpegnoPopup("");
		model.setDescrizioneTipoImpegnoPopup("");
		model.setHasImpegnoSelezionatoXPopup(false);
		if(!presenzaQuote()){
			pulisciTransazioneElementare();
			if (caricaListeBil(WebAppConstants.CAP_UG)) {
				return INPUT;
			}
			teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.LIQUIDAZIONE.toString());
		}
		
		if (presenzaQuoteCompetenza()) {
			model.getNuovaLiquidazioneModel().setAnnoImpegno(sessionHandler.getAnnoEsercizio());
		}
		
		return SUCCESS;
	}
	
	/**
	 * verifica congruita' campi al click di inserimento nuova liquidazione
	 * @return
	 */
	private List<Errore>  controllaCampi(){
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		if(StringUtils.isEmpty(model.getNuovaLiquidazioneModel().getAnnoImpegno())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Impegno"));
		} else {
			if (presenzaQuote()) {
				if (!presenzaQuoteCompetenza()) {
					if(Integer.parseInt(model.getNuovaLiquidazioneModel().getAnnoImpegno()) >= Integer.parseInt(sessionHandler.getAnnoEsercizio())){
						listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Anno Impegno maggiore/uguale dell'anno esercizio"));
					}
				} else {
					if(Integer.parseInt(model.getNuovaLiquidazioneModel().getAnnoImpegno()) != Integer.parseInt(sessionHandler.getAnnoEsercizio())){
						listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Anno Impegno diverso dall'anno esercizio"));
					}
				}
			} else {
				if(Integer.parseInt(model.getNuovaLiquidazioneModel().getAnnoImpegno()) > Integer.parseInt(sessionHandler.getAnnoEsercizio())){
					listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Anno Impegno maggiore dell'anno esercizio"));
				}
			}
			
		}
		if (StringUtils.isEmpty(model.getNuovaLiquidazioneModel().getNumeroImpegno())) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Impegno"));
		}
		if (StringUtils.isEmpty(model.getNuovaLiquidazioneModel().getDescrizioneLiquidazione())) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Descrizione Liquidazione"));
		}
		if (StringUtils.isEmpty(model.getNuovaLiquidazioneModel().getImportoLiquidazione())) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo Liquidazione"));
		}else if(convertiImportoToBigDecimal(model.getNuovaLiquidazioneModel().getImportoLiquidazione()).compareTo(BigDecimal.ZERO)==0){
			listaErrori.add((ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Importo non valido")));
		}
	
		
		return listaErrori;
	}
	
	
	//  da capire come devono essere fatti i controlli
	/*
	 *    2.15.5
	 */
	private Impegno ricercaImpegno(List<Errore> listaErrori){
		
		RicercaImpegnoPerChiaveOttimizzato rip = new RicercaImpegnoPerChiaveOttimizzato();
		rip.setEnte(sessionHandler.getEnte());
		rip.setRichiedente(sessionHandler.getRichiedente());
		RicercaImpegnoK k = new RicercaImpegnoK();
		k.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		if(StringUtils.isNotEmpty(model.getNuovaLiquidazioneModel().getAnnoImpegno())){
			k.setAnnoImpegno(Integer.valueOf(model.getNuovaLiquidazioneModel().getAnnoImpegno()));	
		}
		
		if(StringUtils.isNotEmpty(model.getNuovaLiquidazioneModel().getNumeroImpegno())){
			k.setNumeroImpegno(new BigDecimal(model.getNuovaLiquidazioneModel().getNumeroImpegno()));
		}
		rip.setpRicercaImpegnoK(k);
		
		//MARZO 2016: ottimizzazioni:
		rip.setCaricaSub(false);
		//
		
		// 1 - richiama servizio
		RicercaImpegnoPerChiaveOttimizzatoResponse respRk = movimentoGestionService.ricercaImpegnoPerChiaveOttimizzato(rip);
		
		if (respRk != null && respRk.getImpegno() != null) {
			debug("ricercaImpegno", "ciao");
			Impegno impegno = respRk.getImpegno();
			
			// 2 - impegno provvisorio o annullato
			if(impegno.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("P")){
				// provvisorio
				listaErrori.add(ErroreFin.MOV_PROVVISORIO.getErrore("Impegno"));
				
			}else if(impegno.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("A")){
				    //annullato
					listaErrori.add(ErroreFin.MOV_ANNULLATO.getErrore("Impegno"));
			
			// 3 - Impegno DEFINITIVO O NON LIQUIDABILE		
			}else if(impegno.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("D") || impegno.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("N")){
			    // Definitivo o NON liquidabile
				// controllo capitolo
				CapitoloImpegnoModel capitoloOrdinativo = model.getGestioneOrdinativoStep1Model().getCapitolo();
				if (capitoloOrdinativo.getAnno().intValue() != impegno.getCapitoloUscitaGestione().getAnnoCapitolo().intValue()
						|| capitoloOrdinativo.getNumCapitolo().intValue() != impegno.getCapitoloUscitaGestione().getNumeroCapitolo().intValue()
						|| capitoloOrdinativo.getArticolo().intValue() != impegno.getCapitoloUscitaGestione().getNumeroArticolo().intValue()
						|| (capitoloOrdinativo.getTipoFinanziamento()!=null && impegno.getCapitoloUscitaGestione().getTipoFinanziamento()!=null && capitoloOrdinativo.getTipoFinanziamento().compareTo(impegno.getCapitoloUscitaGestione().getTipoFinanziamento().getDescrizione()) != 0)) {
					listaErrori.add(ErroreFin.IMPEGNO_NON_COMPATIBILE.getErrore(""));
				}
			     
				
			}
			
			// 4- IMPEGNO DEFINITIVO
			if(impegno.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("D")){
				if (impegno.getSoggetto() != null && (impegno.getSoggetto().getCodiceSoggetto() == null || "".equalsIgnoreCase(impegno.getSoggetto().getCodiceSoggetto()))) {
					if (impegno.getSoggetto().getElencoClass() != null && impegno.getSoggetto().getElencoClass().size() > 0) {
						if (model.getGestioneOrdinativoStep1Model().getSoggetto().getListClasseSoggetto() != null && model.getGestioneOrdinativoStep1Model().getSoggetto().getListClasseSoggetto().size() > 0) {
							boolean classeTrovata = false;
							for (ClassificazioneSoggetto currentClasseImpegno : impegno.getSoggetto().getElencoClass()) {
								if (!classeTrovata) {
									for (ClasseSoggetto currentClasse : model.getGestioneOrdinativoStep1Model().getSoggetto().getListClasseSoggetto()) {
										if (currentClasse.getCodice().equalsIgnoreCase(currentClasseImpegno.getSoggettoClasseCode())) {
											classeTrovata = true;
											break;
										}
									}
								}
							}
							if (!classeTrovata) {
								listaErrori.add(ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore(""));
							}
						}
					}
				} else {
					if (!impegno.getSoggetto().getCodiceSoggetto().equalsIgnoreCase(model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore())) {
						listaErrori.add(ErroreFin.IMPEGNO_NON_COMPATIBILE.getErrore(""));
					}
				}
			}
			
			// 5 - IMPEGNO NON LIquidabile

			if(impegno.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("N")){
				
				List<SubImpegno> elencoSubImpegni = respRk.getElencoSubImpegniTuttiConSoloGliIds();
				
				if (elencoSubImpegni != null && elencoSubImpegni.size() > 0) {
					boolean soggettoTrovato = false;
					for (SubImpegno currentSubimpegno : elencoSubImpegni) {
						// in caso che il soggetto arrivi null per qualche operazione non corretta
						// ad esempio elimino un sogg collegato ad un impegno
						if(null!=currentSubimpegno.getSoggetto()){
							// verifico che il sub impegno sia in stato Valido
							if(currentSubimpegno.getStatoOperativoMovimentoGestioneSpesa()!=null && !currentSubimpegno.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase(Constanti.MOVGEST_STATO_ANNULLATO)){
								if (currentSubimpegno.getSoggetto().getCodiceSoggetto().equalsIgnoreCase(model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore())) {
									soggettoTrovato = true;
									//TODO:Caricare dati
									break;
								}
							}	
						}
					}
					if (!soggettoTrovato) {
						listaErrori.add(ErroreFin.IMPEGNO_NON_COMPATIBILE.getErrore(""));
					}
				} else {
					listaErrori.add(ErroreFin.IMPEGNO_NON_COMPATIBILE.getErrore(""));
				}
			}
			
			return impegno;
			
		}else{

			listaErrori.add(ErroreFin.MOV_NON_ESISTENTE.getErrore("Impegno"));
			return null;
		}
			
		
	}
	
	public boolean attivaTastoPreCompila(){
		
		if(null==model.getImpegnoPopUpNuovaLiquidazioneOrdinativo()) return true; // attiva tasto precompila
		else return false; // attiva btn salva
	}
	
	/**
	 * salvataggio nuova liquidazione e partenza dei controlli formali 
	 */
	public String salva() {
		
		// controlla campi
		List<Errore> listaErrori = controllaCampi();

		// 2.12.5
		// ricerca impegno valido 
		Impegno impegno = ricercaImpegno(listaErrori);
		
		if(listaErrori!=null && listaErrori.size()>0){
			addErrori(listaErrori);
			return INPUT;
		}
		
		// 
		if(null==model.getImpegnoPopUpNuovaLiquidazioneOrdinativo() || model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getAnnoMovimento()==0){
			// vuol dire che ho premuto salva senza passare da compilazione guidata di impegno
			// in questo caso popolo tutti i dati come se passasse da comp guidata
			RicercaImpegnoPerChiaveOttimizzato rip = new RicercaImpegnoPerChiaveOttimizzato();
			rip.setEnte(sessionHandler.getEnte());
			rip.setRichiedente(sessionHandler.getRichiedente());
			RicercaImpegnoK k = new RicercaImpegnoK();
			k.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
			k.setAnnoImpegno(Integer.parseInt(model.getNuovaLiquidazioneModel().getAnnoImpegno()));
			
			k.setNumeroImpegno(new BigDecimal(model.getNuovaLiquidazioneModel().getNumeroImpegno()));
			
			rip.setpRicercaImpegnoK(k);
			
			//MARZO 2016: ottimizzazioni:
			rip.setCaricaSub(false);
			//
			
			RicercaImpegnoPerChiaveOttimizzatoResponse respRk = movimentoGestionService.ricercaImpegnoPerChiaveOttimizzato(rip);
			
			if (respRk != null && respRk.getImpegno() != null) {
				
					List<SubImpegno> elencoSubImpegni = respRk.getElencoSubImpegniTuttiConSoloGliIds();
				
				    if(StringUtils.isNotEmpty(model.getNuovaLiquidazioneModel().getNumeroSub())){
				    	
				    	if(elencoSubImpegni==null || elencoSubImpegni.size()==0){
				    		listaErrori.add(ErroreFin.MOV_NON_ESISTENTE.getErrore("subimpegno"));
				    		addErrori(listaErrori);
				    		return INPUT;
				    	}
				    	
				    }else{
				    	// ma esistono dei sub
				    	if(elencoSubImpegni!=null && elencoSubImpegni.size()>0){
				    		
				    		addActionError("E' necessario indicare un subimpegno");
				    		return INPUT;
				    	}
				    }
				     
				
					caricaTE(respRk.getImpegno().getCapitoloUscitaGestione());
					
					model.setImpegnoPopUpNuovaLiquidazioneOrdinativo(respRk.getImpegno());
					
					if(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo()!=null){
						// carico piano dei conti
						// piano dei conti
						teSupport.setPianoDeiConti(new ElementoPianoDeiConti());
						teSupport.getPianoDeiConti().setCodice(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCodPdc());
						teSupport.getPianoDeiConti().setDescrizione(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCodPdc() +"-"+model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getDescPdc());
						teSupport.getPianoDeiConti().setUid(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getIdPdc());
						// setto id e id padre macro aggregato
						if(null!=model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCapitoloUscitaGestione().getMacroaggregato()){
							teSupport.setIdMacroAggregato(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCapitoloUscitaGestione().getMacroaggregato().getUid());
						}
						if(null!=model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCapitoloUscitaGestione().getElementoPianoDeiConti()){
							teSupport.setIdPianoDeiContiPadrePerAggiorna(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCapitoloUscitaGestione().getElementoPianoDeiConti().getUid());
						}
						
						//CR-2023 rimosso conto economico
						
						// setto il cig
						if(StringUtils.isNotEmpty(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCig())){
							model.getNuovaLiquidazioneModel().setCig(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCig());
							
						}
						
						// cod transazione europea
						if(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCodTransazioneEuropeaSpesa()!=null){
							teSupport.setTransazioneEuropeaSelezionato(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCodTransazioneEuropeaSpesa());
						}
						// siope
						if(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCodSiope()!=null && model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getDescCodSiope()!=null){
							SiopeSpesa sp = new SiopeSpesa();
							sp.setCodice(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCodSiope());
							sp.setDescrizione(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCodSiope() + "-"+model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getDescCodSiope());
							teSupport.setSiopeSpesa(sp);
						}
						
						// cup
						if(null!=model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCup()){
							teSupport.setCup(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCup());
						}
						
						// ricorrente
						if(null!=model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCodRicorrenteSpesa()){
							teSupport.setRicorrenteSpesaSelezionato(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCodRicorrenteSpesa());
						}
						
						// cap perimentro sanitario
						if(null!=model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCodCapitoloSanitarioSpesa()){
							teSupport.setPerimetroSanitarioSpesaSelezionato(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCodCapitoloSanitarioSpesa());
						}
						
						// programma politi regionale
						if(null!=model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCodPrgPolReg()){
							teSupport.setPoliticaRegionaleSelezionato(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCodPrgPolReg());
						}
						
						// cofog 
						if(null!=model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCodCofog()){
							teSupport.setCofogSelezionato(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCodCofog());
						}
						
					}
					
				}
				
				if(teSupport.getCup()!= null && model.getNuovaLiquidazioneModel() != null){
					model.getNuovaLiquidazioneModel().setCup(teSupport.getCup());
				}

				return INPUT;
			
		}
		
		//CR-2023  rimosso conto economico
		
		// inserimento della liquidazione
		InserisceLiquidazione request = new InserisceLiquidazione();
		request.setAnnoEsercizio(sessionHandler.getAnnoEsercizio());
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setEnte(sessionHandler.getAccount().getEnte());
		request.setBilancio(sessionHandler.getBilancio());
		Liquidazione liquidazione = new Liquidazione();
		
		// Jira 1976, in fase di inserimento di liquidazione da ordinativo si deve settare non il liqManuale ma il
		// liqAutomatica a S liquidazione.setLiqManuale(Constanti.LIQUIDAZIONE_AUTOMATICA)
		liquidazione.setLiqAutomatica(Constanti.LIQUIDAZIONE_LIQ_AUTOMATICA_SI);
		
		liquidazione.setDescrizioneLiquidazione(model.getNuovaLiquidazioneModel().getDescrizioneLiquidazione());
		if(model.getNuovaLiquidazioneModel().getImportoLiquidazione()!=null ||  StringUtils.isEmpty(model.getNuovaLiquidazioneModel().getImportoLiquidazione())){
			liquidazione.setImportoLiquidazione(convertiImportoToBigDecimal(model.getNuovaLiquidazioneModel().getImportoLiquidazione()));
		}
		liquidazione.setCig(model.getNuovaLiquidazioneModel().getCig());
		liquidazione.setCup(model.getNuovaLiquidazioneModel().getCup());
		
		liquidazione.setCodMissione(teSupport.getMissioneSelezionata());
		liquidazione.setCodProgramma(teSupport.getProgrammaSelezionato());
		liquidazione.setCodPdc(teSupport.getPianoDeiConti().getCodice());
		
		//CR-2023  rimosso conto economico
		
		liquidazione.setCodCofog(teSupport.getCofogSelezionato());
		liquidazione.setCodTransazioneEuropeaSpesa(teSupport.getTransazioneEuropeaSelezionato());
		if(null!=teSupport.getSiopeSpesa()){
			liquidazione.setCodSiope(teSupport.getSiopeSpesa().getCodice());
		}
		
		liquidazione.setCodRicorrenteSpesa(teSupport.getRicorrenteSpesaSelezionato());
		liquidazione.setCodCapitoloSanitarioSpesa(teSupport.getPerimetroSanitarioSpesaSelezionato());
		liquidazione.setCodPrgPolReg(teSupport.getPoliticaRegionaleSelezionato());		
		
		

		// ATTO AMMINISTRATIVO O PROVVEDIMENTO
		AttoAmministrativo attoAmministrativo =null;
		if(model.getGestioneOrdinativoStep1Model().getProvvedimento()!=null){
			if(model.getGestioneOrdinativoStep1Model().getProvvedimento().getAnnoProvvedimento()!=null && model.getGestioneOrdinativoStep1Model().getProvvedimento().getNumeroProvvedimento()!=null){
				attoAmministrativo = new AttoAmministrativo();
				attoAmministrativo.setAnno(model.getGestioneOrdinativoStep1Model().getProvvedimento().getAnnoProvvedimento());
				attoAmministrativo.setNumero(model.getGestioneOrdinativoStep1Model().getProvvedimento().getNumeroProvvedimento().intValue());
				attoAmministrativo.setStatoOperativo(model.getGestioneOrdinativoStep1Model().getProvvedimento().getStato());	
				
				// ricavo uid da codice ::::::::: ad esempio: codice=delibera -- uid=1 
				TipoAtto tipoAtto = new TipoAtto();
				tipoAtto.setUid(getInteroTipoProvvedimentoByCodice(model.getGestioneOrdinativoStep1Model().getProvvedimento().getCodiceTipoProvvedimento()));
				tipoAtto.setCodice(model.getGestioneOrdinativoStep1Model().getProvvedimento().getCodiceTipoProvvedimento());
				attoAmministrativo.setTipoAtto(tipoAtto);
				
			}
		}
		liquidazione.setAttoAmministrativoLiquidazione(attoAmministrativo);
		
		// STATO LIQUIDAZIONE -- VALIDO ????
		liquidazione.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.VALIDO);
		
		// SOGGETTO
		Soggetto soggetto = new Soggetto();
		ArrayList<ModalitaPagamentoSoggetto> listmps = new ArrayList<ModalitaPagamentoSoggetto>();
		
		// SOGGETTO - MDP
		listmps.add(recuperaModalitaPagamento());
		soggetto.setModalitaPagamentoList(listmps);
		
		// SOGGETTO - SEDE SECONDARIA
		ArrayList<SedeSecondariaSoggetto> listsss = new ArrayList<SedeSecondariaSoggetto>();
		listsss.add(recuperaSedeSecondaria());
		soggetto.setSediSecondarie(listsss);
		
		// SOGGETTO - CODICE CREDITORE
		soggetto.setCodiceSoggetto(model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore());
		liquidazione.setSoggettoLiquidazione(soggetto);	
		liquidazione.setSoggettoLiquidazione(soggetto);
		
		// IMPEGNO
		liquidazione.setImpegno(impegno);
		
		// LIQUIDAZIONE
		request.setLiquidazione(liquidazione);
		
		// CHIAMATA AL SERVICE
		InserisceLiquidazioneResponse response = liquidazioneService.inserisceLiquidazione(request);
		
		if(response!=null && response.isFallimento()){
			addErrori(response.getErrori());
			return INPUT;
		}
		
		RicercaLiquidazionePerChiave rlp = new RicercaLiquidazionePerChiave();
		
		RicercaLiquidazioneK prl = new RicercaLiquidazioneK(); 
		Liquidazione liq = new Liquidazione();
		
		liq.setAnnoLiquidazione(response.getLiquidazione().getAnnoLiquidazione());
		liq.setNumeroLiquidazione(response.getLiquidazione().getNumeroLiquidazione());
			
		prl.setTipoRicerca(Constanti.TIPO_RICERCA_DA_ORDINATIVO);
		prl.setLiquidazione(liq);
		prl.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		
		rlp.setEnte(sessionHandler.getAccount().getEnte());
		rlp.setRichiedente(sessionHandler.getRichiedente());
		rlp.setDataOra(new Date());
		rlp.setpRicercaLiquidazioneK(prl);
		
		
		RicercaLiquidazionePerChiaveResponse respLiq = liquidazioneService.ricercaLiquidazionePerChiave(rlp);
		
		
		if(respLiq!=null && respLiq.isFallimento()){
			addErrori(respLiq.getErrori());
			return INPUT;
		}
		
		// setto la liquidazione nella lista liquidazioni dello step2
		model.getGestioneOrdinativoStep2Model().getListaLiquidazioni().add(respLiq.getLiquidazione());
		
		
		// annullo cosi in caso rientro in inserimento di una nuova liquidazione trovo i campi vuoti
		annullaInserimentoNuovaLiquidazione();
		
		return VAI_STEP2;
	}
	
	/**
	 *  dato il codice tipo provvedimento (es. delibera) ritorna uid corrispondente
	 *  
	 * @param codiceProvv
	 * @return int
	 */
	private int getInteroTipoProvvedimentoByCodice(String codiceProvv){
		
		int ris = 0;
		if(model.getListaTipiProvvedimenti()!=null && model.getListaTipiProvvedimenti().size()>0){
			for (int i = 0; i < model.getListaTipiProvvedimenti().size(); i++) {
				TipoAtto temp = (TipoAtto)model.getListaTipiProvvedimenti().get(i);
				if(codiceProvv.equalsIgnoreCase(temp.getCodice())){
					ris=temp.getUid();
					break;
				}		
			}
		}
		return ris;
	}
	
	/**
	 * recupera l'oggetto ModalitaPagamentoSoggetto selezionato nello step1
	 *  
	 * @return
	 */
	private ModalitaPagamentoSoggetto recuperaModalitaPagamento(){
		
		ModalitaPagamentoSoggetto mdp = null;
		if(null!=model.getGestioneOrdinativoStep1Model().getListaModalitaPagamento()){
			for(ModalitaPagamentoSoggetto ciclaMdp : model.getGestioneOrdinativoStep1Model().getListaModalitaPagamento()){
				
				if(ciclaMdp.getUid()==model.getGestioneOrdinativoStep1Model().getRadioModPagSelezionato()){
					mdp = ciclaMdp;
					break;
				}
			}
		}
		return mdp;
	}
	
	/**
	 * recupera l'oggetto SedeSecondariaSoggetto selezionato nello step1
	 *  
	 * @return
	 */
	private SedeSecondariaSoggetto recuperaSedeSecondaria(){
		
		SedeSecondariaSoggetto ss = null;
		if(null!=model.getGestioneOrdinativoStep1Model().getListaSediSecondarie()){
			for(SedeSecondariaSoggetto ciclaSS : model.getGestioneOrdinativoStep1Model().getListaSediSecondarie()){
				
				if(ciclaSS.getUid()==model.getGestioneOrdinativoStep1Model().getRadioSediSecondarieSoggettoSelezionato()){
					
					ss = ciclaSS;
				}
			}
		}
		return ss;
	}
	
	/**
	 * GESTORE TRANSAZIONE ECONOMICA
	 */
	@Override
	public boolean missioneProgrammaAttivi() {
		return true;
	}

	@Override
	public boolean cofogAttivo() {
		return true;
	}

	@Override
	public boolean cupAttivo() {
		return true;
	}

	@Override
	public boolean transazioneElementareAttiva() {
		return true;
	}

	@Override
	public boolean altriClassificatoriAttivi() {
		return false;
	}
	@Override
	public String confermaPdc() {
		return SUCCESS;
	}


	@Override
	public String confermaSiope() {
		return SUCCESS;
	}
	
	@Override
	public boolean programmaPoliticoRegionaleUnitarioAttivo() {
		return true;
	}
	
	@Override
	public boolean datiUscitaImpegno() {
		 return true;
	}
	
	public boolean presenzaQuoteCompetenza() {
		if (model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti() != null && model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti().size() > 0) {
			for (SubOrdinativoPagamento currentQuota : model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti()) {
				if (currentQuota.getLiquidazione().getAnnoLiquidazione().intValue() == Integer.valueOf(sessionHandler.getAnnoEsercizio()).intValue()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean presenzaQuote() {
		if (model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti() != null && model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti().size() > 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public String confermaCompGuidata() {
		// compialazione guidata da popup
		super.confermaCompGuidata();
		if(model.getNuovaLiquidazioneModel()!=null){
			if( model.getNumeroSub()!=null && model.getNumeroSub().intValue()>0){
				model.getNuovaLiquidazioneModel().setNumeroSub(model.getNumeroSub().toString());
			}
			if(model.getNumeroMutuoPopup()!=null){
				model.getNuovaLiquidazioneModel().setNumeroMutuoPopupString(model.getNumeroMutuoPopup().toString());
			}
			
		}
		
		// nel caso NON ci sia una quota in in step2 allora non devo caricare i dati di TE ma
		// mantenere quelli gia' messi nella quota
		if(model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti()==null || model.getGestioneOrdinativoStep2Model().getListaSubOrdinativiPagamenti().size()==0){
			caricaTE(model.getCapitoloPopupNuovaLiquidazioneOrdinativo());
			
			if(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo()!=null){
				// carico piano dei conti
				// piano dei conti
				teSupport.setPianoDeiConti(new ElementoPianoDeiConti());
				teSupport.getPianoDeiConti().setCodice(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCodPdc());
				teSupport.getPianoDeiConti().setDescrizione(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCodPdc() +"-"+model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getDescPdc());
				teSupport.getPianoDeiConti().setUid(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getIdPdc());
				// setto id e id padre macro aggregato
				if(null!=model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCapitoloUscitaGestione().getMacroaggregato()){
					teSupport.setIdMacroAggregato(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCapitoloUscitaGestione().getMacroaggregato().getUid());
				}
				if(null!=model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCapitoloUscitaGestione().getElementoPianoDeiConti()){
					teSupport.setIdPianoDeiContiPadrePerAggiorna(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCapitoloUscitaGestione().getElementoPianoDeiConti().getUid());
				}
				
				// CR-2023  rimosso conto economico
				
				// setto il cig
				if(StringUtils.isNotEmpty(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCig())){
					model.getNuovaLiquidazioneModel().setCig(model.getImpegnoPopUpNuovaLiquidazioneOrdinativo().getCig());
					
				}
			}
		}
		
		
		if(teSupport.getCup()!= null && model.getNuovaLiquidazioneModel() != null){
			model.getNuovaLiquidazioneModel().setCup(teSupport.getCup());
		}
		
		return INPUT;
	}
	
}

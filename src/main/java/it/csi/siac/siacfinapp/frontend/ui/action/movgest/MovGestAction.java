/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;
import it.csi.siac.siaccorser.frontend.webservice.ClassificatoreService;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.action.GenericPopupAction;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.commons.MovGestModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciImpegnoStep1Model;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciMovGestModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ImpegniPluriennaliModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ModificaConsulta;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.MovimentoConsulta;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceAccertamentiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceImpegniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAvanzovincolo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAvanzovincoloResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Avanzovincolo;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ComponenteBilancioImpegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public abstract class MovGestAction<M extends MovGestModel> extends GenericPopupAction<M> {
	
	private static final long serialVersionUID = 1L;

	protected final static String GOTO_ELENCO_IMPEGNI = "gotoElencoImpegni"; 
	protected final static String GOTO_ELENCO_ACCERTAMENTI = "gotoElencoAccertamenti";
	//SIAC-6992
	protected final static String GOTO_ELENCO_IMPEGNI_ROR = "gotoElencoImpegniROR";
	protected final static String GOTO_ELENCO_ACCERTAMENTI_ROR = "gotoElencoAccertamentiROR";
	
	@Autowired
	protected transient ClassificatoreService classificatoreService;
	
	
	protected String methodName;
	
	protected void impostaDatiSiopePlusNelModelDiConsultazione(MovimentoGestione movimentoDaCuiLeggere,MovimentoConsulta modelInCuiImpostare){
		//motivazione assenza cig
		modelInCuiImpostare.setMotivazioneAssenzaCig(descrizioneMotivazioneAssenzaCig(movimentoDaCuiLeggere.getSiopeAssenzaMotivazione()));
		
		//tipo debito siope
		boolean commercialeConFatture = true;
		modelInCuiImpostare.setTipoDebitoSiope(valoreSiopeTipoDebitoPerRadioButton(movimentoDaCuiLeggere.getSiopeTipoDebito(),commercialeConFatture));
	}
	
	protected void caricaListaAvanzovincolo(){
		//CARICAMENTO AVANZOVINCOLI:
		RicercaAvanzovincolo reqAvanzoVincoli = new RicercaAvanzovincolo();
		reqAvanzoVincoli.setEnte(sessionHandler.getEnte());
		reqAvanzoVincoli.setRichiedente(sessionHandler.getRichiedente());
		reqAvanzoVincoli.setBilancio(sessionHandler.getBilancio());
		RicercaAvanzovincoloResponse respAvanzoVincoli = movimentoGestioneFinService.ricercaAvanzovincolo(reqAvanzoVincoli);
		List<Avanzovincolo> listaAvanzoVincoli = respAvanzoVincoli.getElencoAvanzovincolo();
		//setting nel model:
		model.setListaAvanzovincolo(listaAvanzoVincoli);
		//
	}
	
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public boolean isEnteConProgettoObbligatorio() {
		return presenteCodiceLivello(TipologiaGestioneLivelli.PROGETTO_OBBLIGATORIO);
	}
	
	public boolean isEnteAbilitatoGestioneGsa() {
		return presenteCodiceLivello(TipologiaGestioneLivelli.GESTIONE_GSA);
	}
	
	private boolean presenteCodiceLivello(TipologiaGestioneLivelli tipologiaGestioneLivelli){
		String codiceByTipo = getTipologiaGestioneLivelli(tipologiaGestioneLivelli);
		if(codiceByTipo!=null){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isEnteAbilitatoParereFinanziario() {
		return CostantiFin.GESTIONE_PARERE_FINANZIARIO.equals(sessionHandler.getEnte().getGestioneLivelli().get(TipologiaGestioneLivelli.GESTIONE_PARERE_FINANZIARIO));
	}
	
	protected boolean isEnteAbilitatoUeb() {
		
		return model.getEnte()!= null &&  model.getEnte().getGestioneLivelli() != null && "GESTIONE_UEB".equals(model.getEnte().getGestioneLivelli().get(TipologiaGestioneLivelli.LIVELLO_GESTIONE_BILANCIO));
		
	}
	
	@SuppressWarnings("unchecked")
	protected void caricaListeInserisciImpegnoStep1() {
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.CLASSE_SOGGETTO, TipiLista.TIPO_IMPEGNO);
		model.setListaClasseSoggetto((List<CodificaFin>)mappaListe.get(TipiLista.CLASSE_SOGGETTO));
		model.setListaTipiImpegno((List<CodificaFin>)mappaListe.get(TipiLista.TIPO_IMPEGNO));
	}	
	
	
	protected InserisceImpegniResponse inserisceImpegno(GestisciMovGestModel datiImpegno, boolean pluriennale,Integer annoScritturaEconomicoPatrimoniale) {
		return inserisceImpegno(datiImpegno, pluriennale, false,false,annoScritturaEconomicoPatrimoniale);
	}
	
	
	/**
	 * INSERISCI IMPEGNO
	 * @param datiImpegno
	 * @return
	 */
	protected InserisceImpegniResponse inserisceImpegno(GestisciMovGestModel datiImpegno, boolean pluriennale, boolean salvaSDF,
			boolean salvaConByPassDodicesimi,Integer annoScritturaEconomicoPatrimoniale) {
		
		
		/*
		 *  INIZIO - STOPWATCH
		 */
		StopWatch stopwatch = new StopWatch("stopWatchCategory");
		stopwatch.start();
		
		InserisceImpegniResponse resp = null;
		
		InserisceImpegni requestInserisci = null;
		
		if(pluriennale){
			// qui ci passa quando allo step3, se l'impengo e' pluriennale, deve salvare n impegni quanti anni ho indicato nello step 1
			requestInserisci = convertiModelCustomInRequestPluriennali(datiImpegno,annoScritturaEconomicoPatrimoniale);
		} else {
			requestInserisci = convertiModelCustomInRequest(datiImpegno);
			
			if(salvaSDF){
				requestInserisci.getPrimoImpegnoDaInserire().setFlagSDF(true);
			}
			
			if(salvaConByPassDodicesimi){
				requestInserisci.getPrimoImpegnoDaInserire().setByPassControlloDodicesimi(true);
			}
		}
		
	//	String xml = JAXBUtility.marshall(requestInserisci);
		
		//TODO MA SERVE? SONO UGUALI
		if (requestInserisci.getPrimoImpegnoDaInserire().getProgetto() != null) {
			Progetto progetto = requestInserisci.getPrimoImpegnoDaInserire().getProgetto();
			
			progetto.setTipoProgetto(TipoProgetto.GESTIONE);
			progetto.setBilancio(sessionHandler.getBilancio());
		}
		
		
		//Richiamo il service:
		resp = movimentoGestioneFinService.inserisceImpegni(requestInserisci);
		
		
		stopwatch.stop();
		stopWatchLogger.info("inserisceImpegno",  stopwatch.getTotalTimeMillis());
		
		/*
		 *  FINE - STOPWATCH
		 */
		return resp;		
	}
	
	/**
	 * INSERISCI ACCERTAMENTO
	 * @param datiImpegno
	 * @return
	 */
	protected InserisceAccertamentiResponse inserisceAccertamento(GestisciMovGestModel datiAccertamento, boolean pluriennale,Integer annoScritturaEconomicoPatrimoniale) {
		
		/*
		 *  INIZIO - STOPWATCH
		 */
		StopWatch stopwatch = new StopWatch("stopWatchCategory");
		stopwatch.start();
		
		InserisceAccertamentiResponse resp = null;
		
		if(pluriennale){
			resp = movimentoGestioneFinService.inserisceAccertamenti(convertiModelCustomAccertamentiRequestPluriennali(datiAccertamento,annoScritturaEconomicoPatrimoniale));
		}else{
			resp = movimentoGestioneFinService.inserisceAccertamenti(convertiModelCustomAccertamentiRequest(datiAccertamento));
		}
				
				
		
		stopwatch.stop();
		stopWatchLogger.info("inserisceAccertamento",  stopwatch.getTotalTimeMillis());
		
		/*
		 *  FINE - STOPWATCH
		 */
		return resp;		
	}
	
	protected InserisceImpegniResponse inserisceImpegoCustom(Impegno impegno, GestisciMovGestModel model){
		InserisceImpegniResponse resp = movimentoGestioneFinService.inserisceImpegni(convertiModelCustomPerModificaSpesaInRequest(impegno, model));
		return resp;
	}
	
	protected InserisceAccertamentiResponse inserisceAccertamentoCustom(Accertamento acc , GestisciMovGestModel datiAccertamento){
		InserisceAccertamentiResponse resp = movimentoGestioneFinService.inserisceAccertamenti(convertiModelCustomPerModificaEntrataInRequest(acc, datiAccertamento));
		return resp;
	}
	
	private InserisceAccertamenti convertiModelCustomPerModificaEntrataInRequest(Accertamento acc, GestisciMovGestModel datiAccertamento) {
		InserisceAccertamenti support = new InserisceAccertamenti();
		support.setEnte(sessionHandler.getEnte());
		support.setRichiedente(sessionHandler.getRichiedente());
	    //BILANCIO
		support.setBilancio(creaOggettoBilancio());

		if (datiAccertamento.getMovimentoSpesaModel().getListaImpegniPluriennali() != null && datiAccertamento.getMovimentoSpesaModel().getListaImpegniPluriennali().size() > 0) {
			
			List<Accertamento> listaPluriennali=new ArrayList<Accertamento>();
			for (ImpegniPluriennaliModel currentImpegnoPluriennale : datiAccertamento.getMovimentoSpesaModel().getListaImpegniPluriennali()) {
				if (currentImpegnoPluriennale.getImportoImpPluriennale()!=null) {
					if (currentImpegnoPluriennale.getImportoImpPluriennale().compareTo(BigDecimal.ZERO)>0) {
						listaPluriennali.add(popolaAccertamento(datiAccertamento, currentImpegnoPluriennale, support.getBilancio()));
					}
				}
			}
			
			List<Accertamento> appList = new ArrayList<Accertamento>();
			
			boolean primoAccertamento=true;
			
			for(Accertamento app : listaPluriennali){
				app.setFlagDaRiaccertamento(true);
				app.setFlagDaReanno(true);
				app.setAnnoRiaccertato(datiAccertamento.getAccertamentoInAggiornamento().getAnnoMovimento());
				app.setNumeroRiaccertato(datiAccertamento.getAccertamentoInAggiornamento().getNumeroBigDecimal());
				if(app.getCapitoloEntrataGestione()==null){
					app.setCapitoloEntrataGestione(datiAccertamento.getAccertamentoInAggiornamento().getCapitoloEntrataGestione());
				}
				
				if (primoAccertamento) {
					support.setPrimoAccertamentoDaInserire(app);
					primoAccertamento=false;
				}else {
					appList.add(app);
					
				}

			}
			
			listaPluriennali = appList;
			
			support.setAltriAccertamentiDaInserire(listaPluriennali);
		}
		
		return support;
	}
	
	
	private InserisceAccertamenti convertiModelCustomAccertamentiRequest(GestisciMovGestModel datiAccertamento) {
		InserisceAccertamenti support = new InserisceAccertamenti();
		support.setEnte(sessionHandler.getEnte());
		support.setRichiedente(sessionHandler.getRichiedente());
	    //BILANCIO
		support.setBilancio(creaOggettoBilancio());
		
		//IMPEGNO:
		support.setPrimoAccertamentoDaInserire(popolaAccertamento(datiAccertamento, null, support.getBilancio()));
		
		//PLURIENNALI
		if (datiAccertamento.getStep3Model().getListaImpegniPluriennali() != null && datiAccertamento.getStep3Model().getListaImpegniPluriennali().size() > 0) {
			List<Accertamento> listaPluriennali = new ArrayList<Accertamento>();
			for (ImpegniPluriennaliModel currentImpegnoPluriennale : datiAccertamento.getStep3Model().getListaImpegniPluriennali()) {
				if (currentImpegnoPluriennale.getImportoImpPluriennale() != null && currentImpegnoPluriennale.getImportoImpPluriennale().compareTo(BigDecimal.ZERO) > 0) {
					listaPluriennali.add(popolaAccertamento(datiAccertamento, currentImpegnoPluriennale, support.getBilancio()));
				}
			}
			support.setAltriAccertamentiDaInserire(listaPluriennali);
		}
		support.setSaltaInserimentoPrimaNota(datiAccertamento.isSaltaInserimentoPrimaNota());
		return support;
	}
	
	private InserisceAccertamenti convertiModelCustomAccertamentiRequestPluriennali(GestisciMovGestModel datiAccertamento,Integer annoScritturaEconomicoPatrimoniale) {
		InserisceAccertamenti support = new InserisceAccertamenti();
		support.setEnte(sessionHandler.getEnte());
		support.setRichiedente(sessionHandler.getRichiedente());
	    //BILANCIO
		support.setBilancio(creaOggettoBilancio());

		support.setPrimoAccertamentoDaInserire(popolaAccertamento(datiAccertamento, null, support.getBilancio()));
		
		
		if (datiAccertamento.getStep3Model().getListaImpegniPluriennali() != null && datiAccertamento.getStep3Model().getListaImpegniPluriennali().size() > 0) {
			List<Accertamento> listaPluriennali = new ArrayList<Accertamento>();
			
			
			for (int i = 0; i < datiAccertamento.getStep3Model().getListaImpegniPluriennali().size(); i++) {
				
				if(i==0){
					 // il primo elemento dei pluriennali diventa il primo elemento da inserire
					
					//anno
					datiAccertamento.getStep1Model().setAnnoImpegno(datiAccertamento.getStep3Model().getListaImpegniPluriennali().get(i).getAnnoImpPluriennale());
					
					// data scadenza
					if(datiAccertamento.getStep3Model().getListaImpegniPluriennali().get(i).getDataScadenzaImpPluriennaleString()!=null && 
							!datiAccertamento.getStep3Model().getListaImpegniPluriennali().get(i).getDataScadenzaImpPluriennaleString().equals("")){
						
						datiAccertamento.getStep1Model().setScadenza(datiAccertamento.getStep3Model().getListaImpegniPluriennali().get(i).getDataScadenzaImpPluriennaleString());
						
					}
					
					// importo
					if(null!=datiAccertamento.getStep3Model().getListaImpegniPluriennali().get(i).getImportoImpPluriennale()){
						
						datiAccertamento.getStep1Model().setImportoImpegno(datiAccertamento.getStep3Model().getListaImpegniPluriennali().get(i).getImportoImpPluriennale());
						
					}
					
					//jira 2306 Gestione pluriennale
					Accertamento primoAccertamentoPluriennaleDaInserire = popolaAccertamento(datiAccertamento, null, support.getBilancio());
					primoAccertamentoPluriennaleDaInserire.setAnnoAccertamentoOrigine(datiAccertamento.getStep3Model().getAnnoMovimentoInseritoInStep2());
					primoAccertamentoPluriennaleDaInserire.setNumAccertamentoOrigine(datiAccertamento.getStep3Model().getNumeroMovimentoInseritoInStep2());
					
					primoAccertamentoPluriennaleDaInserire.setAnnoScritturaEconomicoPatrimoniale(annoScritturaEconomicoPatrimoniale);
					
					support.setPrimoAccertamentoDaInserire(primoAccertamentoPluriennaleDaInserire);
					
				}else{
					Accertamento acc = popolaAccertamento(datiAccertamento, datiAccertamento.getStep3Model().getListaImpegniPluriennali().get(i), support.getBilancio());
					acc.setAnnoScritturaEconomicoPatrimoniale(annoScritturaEconomicoPatrimoniale);
					listaPluriennali.add(acc);
				}
				
			}
			
			// scorro gli impegni pluriennali pre-impostati e gli setto il numero e l'anno del movimento di origine
			for (Accertamento accertamento : listaPluriennali) {
				
				accertamento.setAnnoAccertamentoOrigine(datiAccertamento.getStep3Model().getAnnoMovimentoInseritoInStep2());
				accertamento.setNumAccertamentoOrigine(datiAccertamento.getStep3Model().getNumeroMovimentoInseritoInStep2());
			}
						
			support.setAltriAccertamentiDaInserire(listaPluriennali);
			
		}
		support.setSaltaInserimentoPrimaNota(datiAccertamento.isSaltaInserimentoPrimaNota());
		return support;
	}
	
	
	
	protected Bilancio creaOggettoBilancio(){
		Bilancio bilancio = new Bilancio();
		
		bilancio = sessionHandler.getBilancio();
		
		return bilancio;
	}
	
	
	private InserisceImpegni convertiModelCustomInRequest(GestisciMovGestModel datiImpegno) {
		InserisceImpegni support = new InserisceImpegni();
		support.setEnte(sessionHandler.getEnte());
		support.setRichiedente(sessionHandler.getRichiedente());
	    //BILANCIO
		support.setBilancio(creaOggettoBilancio());
		//IMPEGNO:
		Impegno impegnoDaInserire = popolaImpegno(datiImpegno, null, support.getBilancio(),false);
		
		support.setPrimoImpegnoDaInserire(impegnoDaInserire);
		
		//PLURIENNALI
		if (datiImpegno.getStep3Model().getListaImpegniPluriennali() != null && datiImpegno.getStep3Model().getListaImpegniPluriennali().size() > 0) {
			List<Impegno> listaPluriennali = new ArrayList<Impegno>();
			for (ImpegniPluriennaliModel currentImpegnoPluriennale : datiImpegno.getStep3Model().getListaImpegniPluriennali()) {
				if (currentImpegnoPluriennale.getImportoImpPluriennale() != null && currentImpegnoPluriennale.getImportoImpPluriennale().compareTo(BigDecimal.ZERO) >= 0) {
					listaPluriennali.add(popolaImpegno(datiImpegno, currentImpegnoPluriennale, support.getBilancio(),true));
				}
			}
			support.setAltriImpegniDaInserire(listaPluriennali);
		}
		
		support.setSaltaInserimentoPrimaNota(datiImpegno.isSaltaInserimentoPrimaNota());
		return support;
	}
	
	/**
	 * 
	 * @param datiImpegno
	 * @return
	 */
	private InserisceImpegni convertiModelCustomInRequestPluriennali(GestisciMovGestModel datiImpegno,Integer annoScritturaEconomicoPatrimoniale) {
		InserisceImpegni support = new InserisceImpegni();
		support.setEnte(sessionHandler.getEnte());
		support.setRichiedente(sessionHandler.getRichiedente());
	    //BILANCIO
		support.setBilancio(creaOggettoBilancio());
			
		//PLURIENNALI
		if (datiImpegno.getStep3Model().getListaImpegniPluriennali() != null && datiImpegno.getStep3Model().getListaImpegniPluriennali().size() > 0) {
			
			List<Impegno> listaPluriennali = new ArrayList<Impegno>();
			
			for (int i = 0; i < datiImpegno.getStep3Model().getListaImpegniPluriennali().size(); i++) {
				
				if(i==0){
					
					// conversione del primo elemento
					datiImpegno.getStep1Model().setAnnoImpegno(datiImpegno.getStep3Model().getListaImpegniPluriennali().get(i).getAnnoImpPluriennale());
					
					if(datiImpegno.getStep3Model().getListaImpegniPluriennali().get(i).getDataScadenzaImpPluriennaleString()!=null && 
							!datiImpegno.getStep3Model().getListaImpegniPluriennali().get(i).getDataScadenzaImpPluriennaleString().equals("")){
						
						datiImpegno.getStep1Model().setScadenza(datiImpegno.getStep3Model().getListaImpegniPluriennali().get(i).getDataScadenzaImpPluriennaleString());
						
					}
					
					if(null!=datiImpegno.getStep3Model().getListaImpegniPluriennali().get(i).getImportoImpPluriennale()){
						datiImpegno.getStep1Model().setImportoImpegno(datiImpegno.getStep3Model().getListaImpegniPluriennali().get(i).getImportoImpPluriennale());
						
					}
					
					//SIAC-7349
					datiImpegno.setComponenteBilancioUid(datiImpegno.getStep3Model().getListaImpegniPluriennali().get(i).getComponenteImpPluriennale());
					//datiImpegno.setAnnoImpegno(datiImpegno.getStep3Model().getListaImpegniPluriennali().get(i).getAnnoImpPluriennale());
					Impegno primoImpegnoPluriennaleDaInserire = popolaImpegno(datiImpegno, null, support.getBilancio(),true);
					/*
					 * SIAC-7816
					 * FIX -> Per il primo impegno pluriennale settiamo la componente dell impegno
					 * con la componente del primo impegno pluriennale 
					 */
					if(datiImpegno.getStep3Model().getListaImpegniPluriennali().get(i).getComponenteImpPluriennale() != null){
						ComponenteBilancioImpegno componenteBilancioImpegno = new ComponenteBilancioImpegno();
						componenteBilancioImpegno.setUid(datiImpegno.getStep3Model().getListaImpegniPluriennali().get(i).getComponenteImpPluriennale());
						primoImpegnoPluriennaleDaInserire.setComponenteBilancioImpegno(componenteBilancioImpegno);
					}
					
					
					primoImpegnoPluriennaleDaInserire.setAnnoImpegnoOrigine(datiImpegno.getStep3Model().getAnnoMovimentoInseritoInStep2());
					primoImpegnoPluriennaleDaInserire.setNumImpegnoOrigine(datiImpegno.getStep3Model().getNumeroMovimentoInseritoInStep2());
					
					primoImpegnoPluriennaleDaInserire.setAnnoScritturaEconomicoPatrimoniale(annoScritturaEconomicoPatrimoniale);
					
					support.setPrimoImpegnoDaInserire(primoImpegnoPluriennaleDaInserire);
				}else{
					Impegno imp = popolaImpegno(datiImpegno, datiImpegno.getStep3Model().getListaImpegniPluriennali().get(i), support.getBilancio(),true);
					imp.setAnnoScritturaEconomicoPatrimoniale(annoScritturaEconomicoPatrimoniale);
					listaPluriennali.add(imp);
				}
		
				
			}
			
			// scorro gli impegni pluriennali pre-impostati e gli setto il numero e l'anno del movimento di origine
			for (Impegno impegno : listaPluriennali) {
				
				impegno.setAnnoImpegnoOrigine(datiImpegno.getStep3Model().getAnnoMovimentoInseritoInStep2());
				impegno.setNumImpegnoOrigine(datiImpegno.getStep3Model().getNumeroMovimentoInseritoInStep2());
			}
			
			support.setAltriImpegniDaInserire(listaPluriennali);
			
		}
		return support;
	}
	
	
	
	//SIAC-6990
	//si apre a protected
	protected InserisceImpegni convertiModelCustomPerModificaSpesaInRequest(Impegno imp, GestisciMovGestModel datiImpegno) {
		InserisceImpegni support = new InserisceImpegni();
		support.setEnte(sessionHandler.getEnte());
		support.setRichiedente(sessionHandler.getRichiedente());
	    //BILANCIO
		support.setBilancio(creaOggettoBilancio());
		//IMPEGNO:

		if (datiImpegno.getMovimentoSpesaModel().getListaImpegniPluriennali() != null && datiImpegno.getMovimentoSpesaModel().getListaImpegniPluriennali().size() > 0) {
			List<Impegno> listaPluriennali = new ArrayList<Impegno>();
			for (ImpegniPluriennaliModel currentImpegnoPluriennale : datiImpegno.getMovimentoSpesaModel().getListaImpegniPluriennali()) {
				if (currentImpegnoPluriennale.getImportoImpPluriennale() != null && currentImpegnoPluriennale.getImportoImpPluriennale().compareTo(BigDecimal.ZERO) > 0) {
					listaPluriennali.add(popolaImpegno(datiImpegno, currentImpegnoPluriennale, support.getBilancio(),true));
					
				}
			}
			
			List<Impegno> appList = new ArrayList<Impegno>();
			
			boolean primoImpegno=true;
			
			for(Impegno app : listaPluriennali){
				app.setFlagDaRiaccertamento(true);
				app.setFlagDaReanno(true);
				app.setAnnoRiaccertato(datiImpegno.getImpegnoInAggiornamento().getAnnoMovimento());
				app.setNumeroRiaccertato(datiImpegno.getImpegnoInAggiornamento().getNumeroBigDecimal());
				if(app.getCapitoloUscitaGestione()==null){
					app.setCapitoloUscitaGestione(datiImpegno.getImpegnoInAggiornamento().getCapitoloUscitaGestione());
				}
				
				if (primoImpegno) {
					support.setPrimoImpegnoDaInserire(app);
					primoImpegno=false;
				}else {
					appList.add(app);
					
				}
			}
			
			listaPluriennali = appList;
			
			support.setAltriImpegniDaInserire(listaPluriennali);
		}
		return support;
	
	}
	
  	@SuppressWarnings("unchecked")
	protected void caricaListeGestisciImpegnoStep1() {
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.CLASSE_SOGGETTO, TipiLista.TIPO_IMPEGNO, TipiLista.MOTIVAZIONE_ASSENZA_CIG);
		model.setListaClasseSoggetto((List<CodificaFin>)mappaListe.get(TipiLista.CLASSE_SOGGETTO));
		model.setListaTipiImpegno((List<CodificaFin>)mappaListe.get(TipiLista.TIPO_IMPEGNO));
		model.setListaMotivazioniAssenzaCig((List<CodificaFin>)mappaListe.get(TipiLista.MOTIVAZIONE_ASSENZA_CIG));
	}		

	protected CapitoloUscitaGestione convertModelPerChiamata(CapitoloImpegnoModel capitoloDaConvertire) {
		CapitoloUscitaGestione capitolo = new CapitoloUscitaGestione();
		capitolo.setAnnoCapitolo(capitoloDaConvertire.getAnno());
		capitolo.setNumeroCapitolo(capitoloDaConvertire.getNumCapitolo());
		capitolo.setNumeroArticolo(capitoloDaConvertire.getArticolo());
		capitolo.setNumeroUEB(capitoloDaConvertire.getUeb().intValue());
		capitolo.setUid(capitoloDaConvertire.getUid());
		return capitolo;
	}
	
	protected CapitoloEntrataGestione convertModelAccertamentoPerChiamata(CapitoloImpegnoModel capitoloDaConvertire) {
		CapitoloEntrataGestione capitolo = new CapitoloEntrataGestione();
		capitolo.setAnnoCapitolo(capitoloDaConvertire.getAnno());
		capitolo.setNumeroCapitolo(capitoloDaConvertire.getNumCapitolo());
		capitolo.setNumeroArticolo(capitoloDaConvertire.getArticolo());
		capitolo.setNumeroUEB(capitoloDaConvertire.getUeb().intValue());
		capitolo.setUid(capitoloDaConvertire.getUid());
		return capitolo;
	}
	
	protected Impegno popolaImpegno(GestisciMovGestModel datiImpegno, ImpegniPluriennaliModel pluriennale, Bilancio bilancio, boolean daPluriennale) {
		Impegno impegno = new Impegno();
		GestisciImpegnoStep1Model datiImpegnoStep1 = datiImpegno.getStep1Model();
		impegno.setCapitoloUscitaGestione(convertModelPerChiamata(datiImpegnoStep1.getCapitolo()));
		impegno.setAnnoCapitoloOrigine(datiImpegnoStep1.getCapitolo().getAnno());
		
		
		impegno.setIdCronoprogramma(datiImpegnoStep1.getIdCronoprogramma());
		impegno.setIdSpesaCronoprogramma(datiImpegnoStep1.getIdSpesaCronoprogramma());
		
		//CR-552 AnnoScritturaEconomicoPatrimoniale
		if(datiImpegnoStep1.getAnnoScritturaEconomicoPatrimoniale()!=null){
			
			impegno.setAnnoScritturaEconomicoPatrimoniale(datiImpegnoStep1.getAnnoScritturaEconomicoPatrimoniale());
		}
		
		ClassificatoreGenerico cg = new ClassificatoreGenerico();
		cg.setCodice(datiImpegnoStep1.getTipoImpegno());
		
		impegno.setTipoImpegno( cg);
		
		//CONVERTO LO STATO
		if(datiImpegnoStep1.getStato() == null || StringUtils.isEmpty(datiImpegnoStep1.getStato())){
			impegno.setStatoOperativoMovimentoGestioneSpesa(CostantiFin.MOVGEST_STATO_PROVVISORIO);
		}else if(datiImpegnoStep1.getStato().equalsIgnoreCase(CostantiFin.STATO_PROVVISORIO)){
			impegno.setStatoOperativoMovimentoGestioneSpesa(CostantiFin.MOVGEST_STATO_PROVVISORIO);
		}else if(datiImpegnoStep1.getStato().equalsIgnoreCase("DEFINITIVO")){
				impegno.setStatoOperativoMovimentoGestioneSpesa(CostantiFin.MOVGEST_STATO_DEFINITIVO);
		}else if(datiImpegnoStep1.getStato().equalsIgnoreCase("DEFINITIVO NON LIQUIDABILE")){
				impegno.setStatoOperativoMovimentoGestioneSpesa(CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE);
		}
		
		
		impegno.setStatoOperativoMovimentoGestioneSpesa(datiImpegnoStep1.getStato());
		////////////////////////////////////////////////////////
		if (datiImpegnoStep1.getAnnoFinanziamento() != null && datiImpegnoStep1.getAnnoFinanziamento() != 0) {
			impegno.setAnnoFinanziamento(datiImpegnoStep1.getAnnoFinanziamento());
		}
		if (datiImpegnoStep1.getNumeroFinanziamento() != null && datiImpegnoStep1.getNumeroFinanziamento() != 0) {
			impegno.setNumeroAccFinanziamento(datiImpegnoStep1.getNumeroFinanziamento());
		}
		if (datiImpegnoStep1.getAnnoImpRiacc() != null && datiImpegnoStep1.getAnnoImpRiacc() != 0) {
			impegno.setAnnoRiaccertato(datiImpegnoStep1.getAnnoImpRiacc());
		}
		if (datiImpegnoStep1.getNumImpRiacc() != null && datiImpegnoStep1.getNumImpRiacc().intValue() != 0) {
			impegno.setNumeroRiaccertato(new BigDecimal(datiImpegnoStep1.getNumImpRiacc()));
		}
		
		impegno.setFlagDaRiaccertamento(datiImpegnoStep1.getRiaccertato().equalsIgnoreCase("si"));
		//SIAC-6997
		impegno.setFlagDaReanno(datiImpegnoStep1.getReanno().equalsIgnoreCase("si"));
		if(datiImpegnoStep1.getStrutturaSelezionataCompetente() != null && !datiImpegnoStep1.getStrutturaSelezionataCompetente().equals("")){
			impegno.setStrutturaCompetente(datiImpegnoStep1.getStrutturaSelezionataCompetente());
		}
		
		
		impegno.setFlagSoggettoDurc(datiImpegnoStep1.getSoggettoDurc().equalsIgnoreCase("si"));
		
		//COPIAMO DEL CAMPO HIDDEN PER VIA DEL CHECKBOX CHE PERDE IL VALORE:
		datiImpegnoStep1.setPrenotazioneLiquidabile(datiImpegnoStep1.getHiddenPerPrenotazioneLiquidabile());
		//
		
		//NUOVI CAMPI, prenotazione, Impegno di cassa economale, 
		if(datiImpegnoStep1.getPrenotazione().equalsIgnoreCase("si")){
			impegno.setFlagPrenotazione(true);
			if(datiImpegnoStep1.getPrenotazioneLiquidabile()!=null){
				impegno.setFlagPrenotazioneLiquidabile(datiImpegnoStep1.getPrenotazioneLiquidabile());
			}
		}else{
			impegno.setFlagPrenotazione(false);
			//PRENOTAZIONE A FALSE IMPLICA CHE PRENOTAZIONE LIQUIDABILE NON PUO' ESSERE TRUE
			//in quanto ha senso solo se prenotazione true:
			impegno.setFlagPrenotazioneLiquidabile(false);
		}
		
		
		
		if(datiImpegnoStep1.getCassaEconomale().equalsIgnoreCase("si")){
			impegno.setFlagCassaEconomale(true);
		}else{
			impegno.setFlagCassaEconomale(false);
		}
		//
		
		
		if(datiImpegnoStep1.getFrazionabile().equalsIgnoreCase(WebAppConstants.FRAZIONABILE)){
			impegno.setFlagFrazionabile(true);
		}else{
			impegno.setFlagFrazionabile(false);
		}
				
		if (datiImpegnoStep1.getAnnoImpOrigine() != null && datiImpegnoStep1.getAnnoImpOrigine() != 0) {
			impegno.setAnnoImpegnoOrigine(datiImpegnoStep1.getAnnoImpOrigine());
		}
		if (datiImpegnoStep1.getNumImpOrigine() != null && datiImpegnoStep1.getNumImpOrigine() != 0) {
			impegno.setNumImpegnoOrigine(datiImpegnoStep1.getNumImpOrigine());
		}
		
		impegno.setAnnoMovimento(datiImpegnoStep1.getAnnoImpegno());
		if(null!=datiImpegnoStep1.getOggettoImpegno()){
			impegno.setDescrizione(datiImpegnoStep1.getOggettoImpegno().toUpperCase());
		}	
		impegno.setCig(datiImpegnoStep1.getCig());
		impegno.setCup(datiImpegnoStep1.getCup());

		//PROGETTO:
		if(datiImpegnoStep1.getProgetto() != null){
			Progetto progetto = new Progetto();
			progetto.setCodice(datiImpegnoStep1.getProgetto());
			progetto.setDescrizione(datiImpegnoStep1.getProgettoImpegno().getDescrizione());
			// SIAC-6990
			// Popolo i campi del progetto con i parametri che servono per il getProgetto() di > MGOD
			progetto.setTipoProgetto(TipoProgetto.GESTIONE);
			progetto.setBilancio(sessionHandler.getBilancio());
			
			//SIAC-7610
			if(!StringUtils.isBlank(datiImpegnoStep1.getCronoprogramma()) && datiImpegnoStep1.getIdCronoprogramma() != 0) {
				Cronoprogramma cronoprogramma = new Cronoprogramma();
				cronoprogramma.setUid(datiImpegnoStep1.getIdCronoprogramma());
				cronoprogramma.setCodice(datiImpegnoStep1.getCronoprogramma());
				
				cronoprogramma.setProgetto(new Progetto());
				cronoprogramma.getProgetto().setUid(progetto.getUid());
				cronoprogramma.getProgetto().setCodice(progetto.getCodice());
				
				List<Cronoprogramma> listCronop = new ArrayList<Cronoprogramma>();
				listCronop.add(cronoprogramma);
				
				progetto.setCronoprogrammi(listCronop);
			}
			
			impegno.setProgetto(progetto);
		}
		
		// popolo tutti e 2 i valori
		impegno.setImportoIniziale(datiImpegnoStep1.getImportoImpegno());
		impegno.setImportoAttuale(datiImpegnoStep1.getImportoImpegno());
		
		
		
		//SIAC-7349
		if(datiImpegno.getImpegnoInAggiornamento() != null){
			if(datiImpegno.getImpegnoInAggiornamento().getComponenteBilancioImpegno() != null)
				impegno.setComponenteBilancioImpegno(datiImpegno.getImpegnoInAggiornamento().getComponenteBilancioImpegno());
			if(datiImpegno.getImpegnoInAggiornamento().getDisponibilitaImpegnareComponente() != null)
				impegno.setDisponibilitaImpegnareComponente(datiImpegno.getImpegnoInAggiornamento().getDisponibilitaImpegnareComponente());
		}
		
		
		
		/* SIAC-7349 Gestione Pluriennali
		 * variabile pluriennale valorizzata solo per gli anni pluriennali superiori al
		 * primo.Uid componente presente in  pluriennale.getComponenteImpPluriennale().intValue()
		 * anno pluriennale.getAnnoImpPluriennale()
		 */
		 if ( pluriennale != null && pluriennale.getAnnoImpPluriennale()>0 && pluriennale.getAnnoImpPluriennale() <= sessionHandler.getBilancio().getAnno() + 2) {
			BigDecimal disponibilitaEffettiva = BigDecimal.ZERO;
			if( datiImpegnoStep1.getImportiCapitoloPerComponente()!=null && !datiImpegnoStep1.getImportiCapitoloPerComponente().isEmpty()){
	    		for(ImportiCapitoloPerComponente icpc :datiImpegnoStep1.getImportiCapitoloPerComponente()){
	    			if(icpc.getTipoComponenteImportiCapitolo()!= null && 
	    					icpc.getTipoDettaglioComponenteImportiCapitolo().name().equals(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAIMPEGNARE.name())
	    					&& pluriennale.getComponenteImpPluriennale()!= null 
	    					&& icpc.getTipoComponenteImportiCapitolo().getUid()==pluriennale.getComponenteImpPluriennale().intValue()
	    					){
	    				if(pluriennale.getAnnoImpPluriennale() ==  icpc.getDettaglioAnno0().getAnnoCompetenza().intValue()){
							disponibilitaEffettiva = icpc.getDettaglioAnno0().getImporto();
						}else if(pluriennale.getAnnoImpPluriennale()  == icpc.getDettaglioAnno1().getAnnoCompetenza().intValue() ){
							disponibilitaEffettiva = icpc.getDettaglioAnno1().getImporto();
						}else if(pluriennale.getAnnoImpPluriennale()  == icpc.getDettaglioAnno2().getAnnoCompetenza().intValue() ){
							disponibilitaEffettiva = icpc.getDettaglioAnno2().getImporto();
						}
	    				break;
	    			}
	    		}
	    		impegno.setDisponibilitaImpegnareComponente(disponibilitaEffettiva);
	    	}
		}else{
			/* Se impegno singolo o pluriennale primo anno (pluriennale = null )
			 * trviamo la componetne in datiImpegno.getComponenteBilancioUid() e
			 * anno in datiImpegno.getStep1Model().getAnnoImpegno()
			 */
			 if (datiImpegno!= null && datiImpegno.getStep1Model()!=null && datiImpegno.getStep1Model().getAnnoImpegno()!= null  &&  datiImpegno.getComponenteBilancioUid()!= null
					 && datiImpegno.getStep1Model().getAnnoImpegno().compareTo(sessionHandler.getBilancio().getAnno() + 2) <= 0) {
				 
				 /*
				  * Nel caso in cui stiamo tattando un pluriennale al 
				  * di fuori del triennio non è necessario settare l'importo della componente
				  */
				 if (pluriennale == null || ( pluriennale != null && pluriennale.getAnnoImpPluriennale()>0 
						 && pluriennale.getAnnoImpPluriennale() <= sessionHandler.getBilancio().getAnno() + 2)) {
					 
					 BigDecimal disponibilitaEffettiva = BigDecimal.ZERO;
						if( datiImpegnoStep1.getImportiCapitoloPerComponente()!=null && !datiImpegnoStep1.getImportiCapitoloPerComponente().isEmpty()){
				    		for(ImportiCapitoloPerComponente icpc :datiImpegnoStep1.getImportiCapitoloPerComponente()){
				    			if(icpc.getTipoComponenteImportiCapitolo()!= null && 
				    					icpc.getTipoDettaglioComponenteImportiCapitolo().name().equals(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAIMPEGNARE.name())
				    					&& icpc.getTipoComponenteImportiCapitolo().getUid()==datiImpegno.getComponenteBilancioUid()
				    					){
				    				if(datiImpegno.getStep1Model().getAnnoImpegno() ==  icpc.getDettaglioAnno0().getAnnoCompetenza().intValue()){
										disponibilitaEffettiva = icpc.getDettaglioAnno0().getImporto();
									}else if(datiImpegno.getStep1Model().getAnnoImpegno()  == icpc.getDettaglioAnno1().getAnnoCompetenza().intValue() ){
										disponibilitaEffettiva = icpc.getDettaglioAnno1().getImporto();
									}else if(datiImpegno.getStep1Model().getAnnoImpegno()  == icpc.getDettaglioAnno2().getAnnoCompetenza().intValue() ){
										disponibilitaEffettiva = icpc.getDettaglioAnno2().getImporto();
									}
				    				break;
				    			}
				    		}
				    		impegno.setDisponibilitaImpegnareComponente(disponibilitaEffettiva);
				    	}
				}
			}
		}
		
		// valori classificazioni della seconda pagina
		impegno.setCodMissione(datiImpegno.getMissioneSelezionata());
		impegno.setCodProgramma(datiImpegno.getProgrammaSelezionato());
		
		if(datiImpegnoStep1.getScadenza() != null){
		
			impegno.setDataScadenza(DateUtility.parse(datiImpegnoStep1.getScadenza()));
		}
		
		//Programma:
		impegno.setCodProgramma(teSupport.getProgrammaSelezionato());
		
		//Missione:
		impegno.setCodMissione(teSupport.getMissioneSelezionata());
		
		
		// albero PDC
		impegno.setCodPdc(datiImpegno.getPianoDeiConti().getCodice());
		
		//SIOPE
		impegno.setCodSiope(datiImpegno.getSiopeSpesa().getCodice());
		
		impegno.setCodCofog(datiImpegno.getCofogSelezionato());
		impegno.setCodTransazioneEuropeaSpesa(datiImpegno.getTransazioneEuropeaSelezionato());
		impegno.setCodRicorrenteSpesa(datiImpegno.getRicorrenteSpesaSelezionato());
		impegno.setCodCapitoloSanitarioSpesa(datiImpegno.getPerimetroSanitarioSpesaSelezionato());
		impegno.setCodPrgPolReg(datiImpegno.getPoliticaRegionaleSelezionato());
		
		if(StringUtils.isNotEmpty(datiImpegno.getClassGenSelezionato1())){
			impegno.setCodClassGen11(datiImpegno.getClassGenSelezionato1());
		}
		if(StringUtils.isNotEmpty(datiImpegno.getClassGenSelezionato2())){
			impegno.setCodClassGen12(datiImpegno.getClassGenSelezionato2());
		}
		if(StringUtils.isNotEmpty(datiImpegno.getClassGenSelezionato3())){
			impegno.setCodClassGen13(datiImpegno.getClassGenSelezionato3());
		}
		if(StringUtils.isNotEmpty(datiImpegno.getClassGenSelezionato4())){
			impegno.setCodClassGen14(datiImpegno.getClassGenSelezionato4());
		}
		if(StringUtils.isNotEmpty(datiImpegno.getClassGenSelezionato5())){
			impegno.setCodClassGen15(datiImpegno.getClassGenSelezionato5());
		}
		
		// provvedimento
		impegno.setAttoAmministrativo(popolaProvvedimento(datiImpegno));
		
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto(datiImpegnoStep1.getSoggetto().getCodCreditore());
		datiImpegnoStep1.getSoggetto().getClasse();
		impegno.setSoggetto(soggetto);
		if (datiImpegnoStep1.getSoggetto().getClasse() != null && !"".equalsIgnoreCase(datiImpegnoStep1.getSoggetto().getClasse())) {
			ClasseSoggetto classe = new ClasseSoggetto();
			classe.setCodice(datiImpegnoStep1.getSoggetto().getClasse());
			impegno.setClasseSoggetto(classe);
		}
		
		//SIAC-7349
		if(datiImpegnoStep1.getCapitolo()!= null && datiImpegnoStep1.getComponenteImpegnoCapitoloUid()!=null){
			ComponenteBilancioImpegno componenteBilancioImpegno = new ComponenteBilancioImpegno();
			componenteBilancioImpegno.setUid(datiImpegnoStep1.getComponenteImpegnoCapitoloUid().intValue());
			impegno.setComponenteBilancioImpegno(componenteBilancioImpegno);
		}
		
		
		if (pluriennale != null) {
			if (pluriennale.getAnnoImpPluriennale() <= (bilancio.getAnno() + 2)) {
				impegno.setAnnoCapitoloOrigine(pluriennale.getAnnoImpPluriennale());
			} else {
				impegno.setAnnoCapitoloOrigine(bilancio.getAnno() + 2);
			}
			impegno.setAnnoMovimento(pluriennale.getAnnoImpPluriennale());
			impegno.setImportoIniziale(pluriennale.getImportoImpPluriennale());
			impegno.setImportoAttuale(pluriennale.getImportoImpPluriennale());
			impegno.setDataScadenza(pluriennale.getDataScadenzaImpPluriennale());
			//SIAC-7349
			if(pluriennale.getComponenteImpPluriennale()!= null && pluriennale.getComponenteImpPluriennale()!=null){
				ComponenteBilancioImpegno componenteBilancioImpegno = new ComponenteBilancioImpegno();
				componenteBilancioImpegno.setUid(pluriennale.getComponenteImpPluriennale());
				impegno.setComponenteBilancioImpegno(componenteBilancioImpegno);
			}
		}
		
		
		
		
		
		// eventuale presenza di vincoli
		if(datiImpegnoStep1.getListaVincoliImpegno()!=null && !datiImpegnoStep1.getListaVincoliImpegno().isEmpty()){
			if(!daPluriennale){
				impegno.setVincoliImpegno(datiImpegnoStep1.getListaVincoliImpegno());
			}
		}
		
		//FLAG ATTIVA GSA:
		impegno.setFlagAttivaGsa(webAppSiNoToBool(datiImpegnoStep1.getFlagAttivaGsa()));
		
		//NUMERO:
		if(datiImpegnoStep1.getNumeroImpegno()!=null && datiImpegnoStep1.getNumeroImpegno().intValue()>0){
			impegno.setNumeroBigDecimal(new BigDecimal(datiImpegnoStep1.getNumeroImpegno()));
		}
		
		//---------- SIOPE PLUS ----------
		impostaDatiSiopePlusPerInserisciAggiorna(datiImpegnoStep1, impegno);
		//---------- FINE SIOPE PLUS ----------
		
		
		return impegno;
	}
	
	
	
	
	/**
	 * prende i dati in GestisciMovGestModel e li travasa nell'oggetto impegnoOrSub che
	 * sta per essere usato in inserimento o aggiornamento service
	 * @param datiImpegno
	 * @param impegno
	 */
	protected void impostaDatiSiopePlusPerInserisciAggiorna(GestisciImpegnoStep1Model modelDaCuiLeggere, MovimentoGestione impegnoOrSub){
		//---------- SIOPE PLUS ----------
		
		//SIOPE TIPO DEBITO:
		SiopeTipoDebito siopeTipoDebito = siopeTipoDebitoDaSceltaNelModel(modelDaCuiLeggere.getTipoDebitoSiope());
		impegnoOrSub.setSiopeTipoDebito(siopeTipoDebito);
		
		//SIOPE MOTIVAZIONE ASSENZA CIG:
		SiopeAssenzaMotivazione siopeAssenzaMotivazione = siopeAssenzaMotivazioneDaSceltaNelModel(modelDaCuiLeggere.getMotivazioneAssenzaCig());
		impegnoOrSub.setSiopeAssenzaMotivazione(siopeAssenzaMotivazione);
		
		//---------- FINE SIOPE PLUS ----------
	}
	
	protected CapitoloEntrataGestione convertModelPerChiamataAcc(CapitoloImpegnoModel capitoloDaConvertire) {
		CapitoloEntrataGestione capitolo = new CapitoloEntrataGestione();
		capitolo.setAnnoCapitolo(capitoloDaConvertire.getAnno());
		capitolo.setNumeroCapitolo(capitoloDaConvertire.getNumCapitolo());
		capitolo.setNumeroArticolo(capitoloDaConvertire.getArticolo());
		capitolo.setNumeroUEB(capitoloDaConvertire.getUeb().intValue());
		capitolo.setUid(capitoloDaConvertire.getUid());
		return capitolo;
	}
	
	/**
	 * POPOLA ACCERTAMENTO
	 * @param datiAccertamento
	 * @param pluriennale
	 * @param bilancio
	 * @return
	 */
	protected Accertamento popolaAccertamento(GestisciMovGestModel datiAccertamento, ImpegniPluriennaliModel pluriennale, Bilancio bilancio) {
		Accertamento accertamento = new Accertamento();
		
		// CR - 552
		if(datiAccertamento.getStep1Model().getAnnoScritturaEconomicoPatrimoniale()!=null){
			accertamento.setAnnoScritturaEconomicoPatrimoniale(datiAccertamento.getStep1Model().getAnnoScritturaEconomicoPatrimoniale());
		}
		
		accertamento.setCapitoloEntrataGestione((convertModelPerChiamataAcc(datiAccertamento.getStep1Model().getCapitolo())));
			
		accertamento.setAnnoCapitoloOrigine(datiAccertamento.getStep1Model().getCapitolo().getAnno());
		//INSERIRE IL TIPO IMPEGNO CORRETTO : NON PRESENTE IN ACCERTAMENTO
		//CONVERTO LO STATO
		if(null==datiAccertamento.getStep1Model().getStato() || StringUtils.isEmpty(datiAccertamento.getStep1Model().getStato())){
			accertamento.setStatoOperativoMovimentoGestioneEntrata(CostantiFin.MOVGEST_STATO_PROVVISORIO);
		}else if(datiAccertamento.getStep1Model().getStato().equalsIgnoreCase(CostantiFin.STATO_PROVVISORIO)){
			accertamento.setStatoOperativoMovimentoGestioneEntrata(CostantiFin.MOVGEST_STATO_PROVVISORIO);
		}else if(datiAccertamento.getStep1Model().getStato().equalsIgnoreCase("DEFINITIVO")){
			accertamento.setStatoOperativoMovimentoGestioneEntrata(CostantiFin.MOVGEST_STATO_DEFINITIVO);
		}else if(datiAccertamento.getStep1Model().getStato().equalsIgnoreCase("DEFINITIVO NON LIQUIDABILE")){
			accertamento.setStatoOperativoMovimentoGestioneEntrata(CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE);
		}
		
		
		accertamento.setStatoOperativoMovimentoGestioneEntrata(datiAccertamento.getStep1Model().getStato());
		////////////////////////////////////////////////////////
		//INSERIRE IL TIPO FINANZIAMENTO : NON PRESENTE IN ACCERTAMENTO
		if (datiAccertamento.getStep1Model().getAnnoImpRiacc() != null && datiAccertamento.getStep1Model().getAnnoImpRiacc() != 0) {
			accertamento.setAnnoRiaccertato(datiAccertamento.getStep1Model().getAnnoImpRiacc());
		}
		if (datiAccertamento.getStep1Model().getNumImpRiacc() != null && datiAccertamento.getStep1Model().getNumImpRiacc().intValue() != 0) {
			accertamento.setNumeroRiaccertato(new BigDecimal(datiAccertamento.getStep1Model().getNumImpRiacc()));
		}
		
		if(datiAccertamento.getStep1Model().getRiaccertato().equalsIgnoreCase("si")){
			accertamento.setFlagDaRiaccertamento(true);
		} else {
			accertamento.setFlagDaRiaccertamento(false);
		}
		//SIAC-6997
		if(datiAccertamento.getStep1Model().getReanno().equalsIgnoreCase("si")){
			accertamento.setFlagDaReanno(true);
		} else {
			accertamento.setFlagDaReanno(false);
		}
		if(datiAccertamento.getStep1Model().getStrutturaSelezionataCompetente() != null && !datiAccertamento.getStep1Model().getStrutturaSelezionataCompetente().equals("")){
			//StrutturaAmministrativoContabile sac = new StrutturaAmministrativoContabile();
			//sac.setUid(Integer.valueOf(datiAccertamento.getStep1Model().getStrutturaSelezionataCompetente()));
			accertamento.setStrutturaCompetente(datiAccertamento.getStep1Model().getStrutturaSelezionataCompetente());
		}
		
		if (datiAccertamento.getStep1Model().getAnnoImpOrigine() != null && datiAccertamento.getStep1Model().getAnnoImpOrigine() != 0) {
			accertamento.setAnnoAccertamentoOrigine(datiAccertamento.getStep1Model().getAnnoImpOrigine());
		}
		if (datiAccertamento.getStep1Model().getNumImpOrigine() != null && datiAccertamento.getStep1Model().getNumImpOrigine() != 0) {
			accertamento.setNumAccertamentoOrigine(datiAccertamento.getStep1Model().getNumImpOrigine());
		}
		accertamento.setAnnoMovimento(datiAccertamento.getStep1Model().getAnnoImpegno());
		if(null!=datiAccertamento.getStep1Model().getOggettoImpegno()){
			accertamento.setDescrizione(datiAccertamento.getStep1Model().getOggettoImpegno().toUpperCase());
		}	
		//INSERIRE IL CIG/CUP : NON PRESENTE IN ACCERTAMENTO

		if(null!=datiAccertamento.getStep1Model().getProgetto()){
			Progetto progetto = new Progetto();
			progetto.setCodice(datiAccertamento.getStep1Model().getProgetto());
			if (datiAccertamento.getStep1Model().getProgettoImpegno().getProgetto() != null) {
				progetto.setTipoProgetto(datiAccertamento.getStep1Model().getProgettoImpegno().getProgetto().getTipoProgetto());
				progetto.setBilancio(datiAccertamento.getStep1Model().getProgettoImpegno().getProgetto().getBilancio());
			}
			accertamento.setProgetto(progetto);
		}
		
		// popolo tutti e 2 i valori
		accertamento.setImportoIniziale(datiAccertamento.getStep1Model().getImportoImpegno());
		accertamento.setImportoAttuale(datiAccertamento.getStep1Model().getImportoImpegno());
		
		// valori classificazioni della seconda pagina
		accertamento.setCodMissione(datiAccertamento.getMissioneSelezionata());
		accertamento.setCodProgramma(datiAccertamento.getProgrammaSelezionato());
		
		if(null!=datiAccertamento.getStep1Model().getScadenza()){
		
			accertamento.setDataScadenza(DateUtility.parse(datiAccertamento.getStep1Model().getScadenza()));
		}
		
		// albero PDC
		accertamento.setCodPdc(datiAccertamento.getPianoDeiConti().getCodice());
		accertamento.setIdPdc(datiAccertamento.getPianoDeiConti().getUid());
		accertamento.setDescPdc(datiAccertamento.getPianoDeiConti().getDescrizione());

		accertamento.setCodSiope(datiAccertamento.getSiopeSpesa().getCodice());
		
		accertamento.setCodCofog(datiAccertamento.getCofogSelezionato());
		accertamento.setCodTransazioneEuropeaSpesa(datiAccertamento.getTransazioneEuropeaSelezionato());
		accertamento.setCodRicorrenteSpesa(datiAccertamento.getRicorrenteEntrataSelezionato());
		accertamento.setCodCapitoloSanitarioSpesa(datiAccertamento.getPerimetroSanitarioEntrataSelezionato());
		accertamento.setCodPrgPolReg(datiAccertamento.getPoliticaRegionaleSelezionato());
		accertamento.setCodClassGen11(datiAccertamento.getClassGenSelezionato1());
		accertamento.setCodClassGen12(datiAccertamento.getClassGenSelezionato2());
		accertamento.setCodClassGen13(datiAccertamento.getClassGenSelezionato3());
		accertamento.setCodClassGen14(datiAccertamento.getClassGenSelezionato4());
		accertamento.setCodClassGen15(datiAccertamento.getClassGenSelezionato5());
		
		accertamento.setCodClassGen16(datiAccertamento.getClassGenSelezionato6());
		accertamento.setCodClassGen17(datiAccertamento.getClassGenSelezionato7());
		accertamento.setCodClassGen18(datiAccertamento.getClassGenSelezionato8());
		accertamento.setCodClassGen19(datiAccertamento.getClassGenSelezionato9());
		accertamento.setCodClassGen20(datiAccertamento.getClassGenSelezionato10());
		
		
		
		// provvedimento
		accertamento.setAttoAmministrativo(popolaProvvedimento(datiAccertamento));
				
		
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto(datiAccertamento.getStep1Model().getSoggetto().getCodCreditore());
		datiAccertamento.getStep1Model().getSoggetto().getClasse();
		accertamento.setSoggetto(soggetto);
		if (datiAccertamento.getStep1Model().getSoggetto().getClasse() != null && !"".equalsIgnoreCase(datiAccertamento.getStep1Model().getSoggetto().getClasse())) {
			ClasseSoggetto classe = new ClasseSoggetto();
			classe.setCodice(datiAccertamento.getStep1Model().getSoggetto().getClasse());
			accertamento.setClasseSoggetto(classe);
		}
		if (pluriennale != null) {
			if (pluriennale.getAnnoImpPluriennale() <= (bilancio.getAnno() + 2)) {
				accertamento.setAnnoCapitoloOrigine(pluriennale.getAnnoImpPluriennale());
			} else {
				accertamento.setAnnoCapitoloOrigine(bilancio.getAnno() + 2);
			}
			accertamento.setAnnoMovimento(pluriennale.getAnnoImpPluriennale());
			accertamento.setImportoIniziale(pluriennale.getImportoImpPluriennale());
			accertamento.setImportoAttuale(pluriennale.getImportoImpPluriennale());
			accertamento.setDataScadenza(pluriennale.getDataScadenzaImpPluriennale());
		}
		
		accertamento.setFlagFattura(webAppSiNoToBool(datiAccertamento.getStep1Model().getFlagFattura()));
		accertamento.setFlagCorrispettivo(webAppSiNoToBool(datiAccertamento.getStep1Model().getFlagCorrispettivo()));
		
		//FLAG ATTIVA GSA:
		accertamento.setFlagAttivaGsa(webAppSiNoToBool(datiAccertamento.getStep1Model().getFlagAttivaGsa()));
		
		//NUMERO:
		if(datiAccertamento.getStep1Model().getNumeroImpegno()!=null && datiAccertamento.getStep1Model().getNumeroImpegno().intValue()>0){
			accertamento.setNumeroBigDecimal(new BigDecimal(datiAccertamento.getStep1Model().getNumeroImpegno()));
		}
		
		//SIAC-8178
		accertamento.setCodiceVerbale(datiAccertamento.getStep1Model().getCodiceVerbale());
		
		return accertamento;
	}

	protected Impegno popolaImpegnoAggiorna (GestisciMovGestModel datiImpegno, ImpegniPluriennaliModel pluriennale, Bilancio bilancio) {
		Impegno impegno = new Impegno();
		popolaImpegnoAggiornaCore(datiImpegno, pluriennale, bilancio, impegno);
		
		// albero PDC
		impegno.setCodPdc(datiImpegno.getPianoDeiConti().getCodice()); log.debug("","PdC -*-> " + datiImpegno.getPianoDeiConti().getCodice());
		
		impegno.setCodSiope(datiImpegno.getSiopeSpesa().getCodice()); log.debug("","Spesa -*-> " + datiImpegno.getSiopeSpesa().getCodice());
		
		//MISSIONE E PROGRAMMA:
		impegno.setCodProgramma(datiImpegno.getProgrammaSelezionato());
		impegno.setCodMissione(datiImpegno.getMissioneSelezionata());
		//
		
		impegno.setCodCofog(datiImpegno.getCofogSelezionato());
		impegno.setCodTransazioneEuropeaSpesa(datiImpegno.getTransazioneEuropeaSelezionato());
		impegno.setCodRicorrenteSpesa(datiImpegno.getRicorrenteSpesaSelezionato());
		impegno.setCodCapitoloSanitarioSpesa(datiImpegno.getPerimetroSanitarioSpesaSelezionato());
		impegno.setCodPrgPolReg(datiImpegno.getPoliticaRegionaleSelezionato());
		impegno.setCodClassGen11(datiImpegno.getClassGenSelezionato1());
		impegno.setCodClassGen12(datiImpegno.getClassGenSelezionato2());
		impegno.setCodClassGen13(datiImpegno.getClassGenSelezionato3());
		impegno.setCodClassGen14(datiImpegno.getClassGenSelezionato4());
		impegno.setCodClassGen15(datiImpegno.getClassGenSelezionato5());
			
		
		impegno.setParereFinanziario(datiImpegno.getStep1Model().isParereFinanziario());
		impegno.setFlagCheckDisponibilitaCapitolo(datiImpegno.getStep1Model().getImprtoVincoliModificato()!=null ?datiImpegno.getStep1Model().getImprtoVincoliModificato(): Boolean.FALSE);
		
		if(datiImpegno.getStep1Model().getFlagAttivaGsa().equalsIgnoreCase("SI")){
			impegno.setFlagAttivaGsa(Boolean.TRUE);
		} else {
			impegno.setFlagAttivaGsa(Boolean.FALSE);
		}
			
		return impegno;
	}
	
	
	protected Impegno popolaImpegnoInserisciAggiornaModifiche(GestisciMovGestModel datiImpegno, ImpegniPluriennaliModel pluriennale, Bilancio bilancio) {
		
		Impegno impegno = new Impegno();
		
		impegno.setCapitoloUscitaGestione(convertModelPerChiamata(datiImpegno.getStep1Model().getCapitolo()));
		impegno.setAnnoCapitoloOrigine(datiImpegno.getStep1Model().getCapitolo().getAnno());
		
		ClassificatoreGenerico cg = new ClassificatoreGenerico();
		cg.setCodice(datiImpegno.getStep1Model().getTipoImpegno());
		
		impegno.setTipoImpegno(cg);
		
		impegno.setStatoOperativoMovimentoGestioneSpesa(datiImpegno.getStep1Model().getStato());
	
		////////////////////////////////////////////////////////
		if (datiImpegno.getStep1Model().getAnnoFinanziamento() != null && datiImpegno.getStep1Model().getAnnoFinanziamento() != 0) { log.debug("","anno F -*-> " + datiImpegno.getStep1Model().getAnnoFinanziamento());
			impegno.setAnnoFinanziamento(datiImpegno.getStep1Model().getAnnoFinanziamento());
		}
		if (datiImpegno.getStep1Model().getNumeroFinanziamento() != null && datiImpegno.getStep1Model().getNumeroFinanziamento() != 0) { log.debug("","num F -*-> " + datiImpegno.getStep1Model().getNumeroFinanziamento());
			impegno.setNumeroAccFinanziamento(datiImpegno.getStep1Model().getNumeroFinanziamento());
		}
		
		//correzione anomalia flag riaccertamento
		if(datiImpegno.getStep1Model().getRiaccertato().equalsIgnoreCase("si") || datiImpegno.getStep1Model().getReanno().equalsIgnoreCase("si")){
			
			if(datiImpegno.getStep1Model().getRiaccertato().equalsIgnoreCase("si")) {
				impegno.setFlagDaRiaccertamento(true);
			}else if(datiImpegno.getStep1Model().getReanno().equalsIgnoreCase("si")) {
				impegno.setFlagDaReanno(true);
			}
			
			if (datiImpegno.getStep1Model().getAnnoImpRiacc() != null && datiImpegno.getStep1Model().getAnnoImpRiacc() != 0) {
				impegno.setAnnoRiaccertato(datiImpegno.getStep1Model().getAnnoImpRiacc());
			}
		
			if (datiImpegno.getStep1Model().getNumImpRiacc() != null && datiImpegno.getStep1Model().getNumImpRiacc().intValue()!=0) {
				impegno.setNumeroRiaccertato(new BigDecimal(datiImpegno.getStep1Model().getNumImpRiacc()));
			}
		} else {
			impegno.setFlagDaRiaccertamento(false);
			impegno.setFlagDaReanno(false);
		}	

		impegno.setFlagSoggettoDurc(datiImpegno.getStep1Model().getSoggettoDurc().equalsIgnoreCase("si"));
		
		if (datiImpegno.getStep1Model().getAnnoImpOrigine() != null && datiImpegno.getStep1Model().getAnnoImpOrigine() != 0) { log.debug("","anno O -*-> " + datiImpegno.getStep1Model().getAnnoImpOrigine());
			impegno.setAnnoImpegnoOrigine(datiImpegno.getStep1Model().getAnnoImpOrigine());
		}
		if (datiImpegno.getStep1Model().getNumImpOrigine() != null && datiImpegno.getStep1Model().getNumImpOrigine() != 0) { log.debug("","num O -*-> " + datiImpegno.getStep1Model().getNumImpOrigine());
			impegno.setNumImpegnoOrigine(datiImpegno.getStep1Model().getNumImpOrigine());
		}

		////////////////////////////////////////////////////////////////////////////////////////
		impegno.setAnnoMovimento(datiImpegno.getStep1Model().getAnnoImpegno()); //da verificare
		impegno.setUid(datiImpegno.getStep1Model().getUid()); //da verificare
		impegno.setNumeroBigDecimal(new BigDecimal(datiImpegno.getStep1Model().getNumeroImpegno())); //da verificare
		///////////////////////////////////////////////////////////////////////////////////////
		if(datiImpegno.getStep1Model().getOggettoImpegno()!=null){
			impegno.setDescrizione(datiImpegno.getStep1Model().getOggettoImpegno().toUpperCase());
		}
		impegno.setCig(datiImpegno.getStep1Model().getCig());
		impegno.setCup(datiImpegno.getStep1Model().getCup());
		
		
		//////////////////Modifica Movimento Spesa //////////////////////////////
		impegno.setListaModificheMovimentoGestioneSpesa(datiImpegno.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa());
		/////////////////////////////////////////////////////////////////////////
		
		//PROGETTO
		if(null!=datiImpegno.getStep1Model().getProgetto()){
			Progetto progetto = new Progetto();
			progetto.setCodice(datiImpegno.getStep1Model().getProgetto());
			// SIAC-6990
			// Popolo i campi del progetto con i parametri che servono per il getProgetto() di > MGOD
			progetto.setTipoProgetto(TipoProgetto.GESTIONE);
			progetto.setBilancio(sessionHandler.getBilancio());
//			progetto.setStatoOperativoProgetto();
			
			impegno.setProgetto(progetto);
		}
		
		// popolo tutti e 2 i valori
		impegno.setImportoIniziale(datiImpegno.getStep1Model().getImportoImpegno());
		impegno.setImportoAttuale(convertiImportoToBigDecimal(datiImpegno.getStep1Model().getImportoFormattato())); 
		
		impegno.setDataScadenza(DateUtility.parse(datiImpegno.getStep1Model().getScadenza()));
		
		// valori classificazioni della seconda pagina
		impegno.setCodMissione(datiImpegno.getMissioneSelezionata());
		impegno.setCodProgramma(datiImpegno.getProgrammaSelezionato());
		
		// provvedimento dell'impegno! 
		impegno.setAttoAmministrativo(datiImpegno.getImpegnoInAggiornamento().getAttoAmministrativo());
	
		///SOGGETTO:
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto(datiImpegno.getStep1Model().getSoggetto().getCodCreditore());
		impegno.setSoggetto(soggetto);
		if (datiImpegno.getStep1Model().getSoggetto().getClasse() != null && !"".equalsIgnoreCase(datiImpegno.getStep1Model().getSoggetto().getClasse())) {
			ClasseSoggetto classe = new ClasseSoggetto();
			classe.setCodice(datiImpegno.getStep1Model().getSoggetto().getClasse());
			impegno.setClasseSoggetto(classe);
		}
		
		if (pluriennale != null) {
			if (pluriennale.getAnnoImpPluriennale() <= (bilancio.getAnno() + 2)) {
				impegno.setAnnoCapitoloOrigine(pluriennale.getAnnoImpPluriennale());
			} else {
				impegno.setAnnoCapitoloOrigine(bilancio.getAnno() + 2);
			}
			impegno.setAnnoMovimento(pluriennale.getAnnoImpPluriennale());
			impegno.setImportoIniziale(pluriennale.getImportoImpPluriennale());
			impegno.setDataScadenza(pluriennale.getDataScadenzaImpPluriennale());
		}
		
		impegno.setParereFinanziario(datiImpegno.getStep1Model().isParereFinanziario());

		impegno.setCodPdc(datiImpegno.getPianoDeiConti().getCodice()); 
			
		impegno.setCodSiope(datiImpegno.getSiopeSpesa().getCodice()); 
			
		impegno.setCodCofog(datiImpegno.getCofogSelezionato());
		impegno.setCodTransazioneEuropeaSpesa(datiImpegno.getTransazioneEuropeaSelezionato());
		impegno.setCodRicorrenteSpesa(datiImpegno.getRicorrenteSpesaSelezionato());
		impegno.setCodCapitoloSanitarioSpesa(datiImpegno.getPerimetroSanitarioSpesaSelezionato());
		impegno.setCodPrgPolReg(datiImpegno.getPoliticaRegionaleSelezionato());
		impegno.setCodClassGen11(datiImpegno.getClassGenSelezionato1());
		impegno.setCodClassGen12(datiImpegno.getClassGenSelezionato2());
		impegno.setCodClassGen13(datiImpegno.getClassGenSelezionato3());
		impegno.setCodClassGen14(datiImpegno.getClassGenSelezionato4());
		impegno.setCodClassGen15(datiImpegno.getClassGenSelezionato5());
		
		
		//SIOPE PLUS:
		impostaDatiSiopePlusPerInserisciAggiorna(datiImpegno.getStep1Model(), impegno);
		//END SIOPE PLUS
		impegno.setNumeroTotaleModifcheMovimento(datiImpegno.getImpegnoInAggiornamento().getNumeroTotaleModifcheMovimento());
		

		return impegno;
	}
	

	
	protected Impegno popolaImpegnoAggiornaSubimpegno(GestisciMovGestModel datiImpegno, ImpegniPluriennaliModel pluriennale, Bilancio bilancio) {
		Impegno impegno = new Impegno();
		popolaImpegnoAggiornaCore(datiImpegno, pluriennale, bilancio, impegno);
		
		// jira 4296
		// il flagGSA non viene gestito e quindi arriva al servizio sempre false
		impegno.setFlagAttivaGsa(datiImpegno.getStep1Model().getFlagAttivaGsa().equalsIgnoreCase("Si")? true : false );
		
		// albero PDC
		impegno.setCodPdc(datiImpegno.getImpegnoInAggiornamento().getCodPdc());
		
		impegno.setCodSiope(datiImpegno.getImpegnoInAggiornamento().getCodSiope());
		impegno.setCodCofog(datiImpegno.getImpegnoInAggiornamento().getCodCofog());
		impegno.setCodTransazioneEuropeaSpesa(datiImpegno.getImpegnoInAggiornamento().getCodTransazioneEuropeaSpesa());
		impegno.setCodRicorrenteSpesa(datiImpegno.getImpegnoInAggiornamento().getCodRicorrenteSpesa());
		impegno.setCodCapitoloSanitarioSpesa(datiImpegno.getImpegnoInAggiornamento().getCodCapitoloSanitarioSpesa());
		impegno.setCodPrgPolReg(datiImpegno.getImpegnoInAggiornamento().getCodPrgPolReg());
		impegno.setCodClassGen11(datiImpegno.getImpegnoInAggiornamento().getCodClassGen11());
		impegno.setCodClassGen12(datiImpegno.getImpegnoInAggiornamento().getCodClassGen12());
		impegno.setCodClassGen13(datiImpegno.getImpegnoInAggiornamento().getCodClassGen13());
		impegno.setCodClassGen14(datiImpegno.getImpegnoInAggiornamento().getCodClassGen14());
		impegno.setCodClassGen15(datiImpegno.getImpegnoInAggiornamento().getCodClassGen15());
		return impegno;
	}

	private void popolaImpegnoAggiornaCore(GestisciMovGestModel datiImpegno,
			ImpegniPluriennaliModel pluriennale, Bilancio bilancio,
			Impegno impegno) {
		impegno.setCapitoloUscitaGestione(convertModelPerChiamata(datiImpegno.getStep1Model().getCapitolo()));
		impegno.setAnnoCapitoloOrigine(datiImpegno.getStep1Model().getCapitolo().getAnno());
		
		ClassificatoreGenerico cg = new ClassificatoreGenerico();
		cg.setCodice(datiImpegno.getStep1Model().getTipoImpegno());
		
		impegno.setTipoImpegno( cg);
		//SIAC-7349: devo settare il componente per i controlli lato BE
		if(datiImpegno.getImpegnoInAggiornamento() != null){
			if(datiImpegno.getImpegnoInAggiornamento().getComponenteBilancioImpegno() != null)
				impegno.setComponenteBilancioImpegno(datiImpegno.getImpegnoInAggiornamento().getComponenteBilancioImpegno());
			if(datiImpegno.getImpegnoInAggiornamento().getDisponibilitaImpegnareComponente() != null)
				impegno.setDisponibilitaImpegnareComponente(datiImpegno.getImpegnoInAggiornamento().getDisponibilitaImpegnareComponente());
		}
		
		
		//FINE SIAC-7349
		impegno.setStatoOperativoMovimentoGestioneSpesa(datiImpegno.getStep1Model().getStato());
		
	
		////////////////////////////////////////////////////////
		if (datiImpegno.getStep1Model().getAnnoFinanziamento() != null && datiImpegno.getStep1Model().getAnnoFinanziamento() != 0) { log.debug("","anno F -*-> " + datiImpegno.getStep1Model().getAnnoFinanziamento());
			impegno.setAnnoFinanziamento(datiImpegno.getStep1Model().getAnnoFinanziamento());
		}
		if (datiImpegno.getStep1Model().getNumeroFinanziamento() != null && datiImpegno.getStep1Model().getNumeroFinanziamento() != 0) { log.debug("","num F -*-> " + datiImpegno.getStep1Model().getNumeroFinanziamento());
			impegno.setNumeroAccFinanziamento(datiImpegno.getStep1Model().getNumeroFinanziamento());
		}
		
		//correzione anomalia flag riaccertamento
		if(datiImpegno.getStep1Model().getRiaccertato().equalsIgnoreCase("si") || datiImpegno.getStep1Model().getReanno().equalsIgnoreCase("si")){
			
			if(datiImpegno.getStep1Model().getRiaccertato().equalsIgnoreCase("si")) {
				impegno.setFlagDaRiaccertamento(true);
			}else if(datiImpegno.getStep1Model().getReanno().equalsIgnoreCase("si")) {
				impegno.setFlagDaReanno(true);
			}
			
			if (datiImpegno.getStep1Model().getAnnoImpRiacc() != null && datiImpegno.getStep1Model().getAnnoImpRiacc() != 0) {
				impegno.setAnnoRiaccertato(datiImpegno.getStep1Model().getAnnoImpRiacc());
			}
		
			if (datiImpegno.getStep1Model().getNumImpRiacc() != null && datiImpegno.getStep1Model().getNumImpRiacc().intValue()!=0) {
				impegno.setNumeroRiaccertato(new BigDecimal(datiImpegno.getStep1Model().getNumImpRiacc()));
			}
		} else {
			impegno.setFlagDaRiaccertamento(false);
			impegno.setFlagDaReanno(false);
		}

		
		impegno.setFlagSoggettoDurc(datiImpegno.getStep1Model().getSoggettoDurc().equalsIgnoreCase("si"));
		
		if(datiImpegno.getStep1Model().getFrazionabile().equalsIgnoreCase(WebAppConstants.FRAZIONABILE)){
			impegno.setFlagFrazionabile(true);
		}else{
			impegno.setFlagFrazionabile(false);
		}
		
		//COPIAMO DEL CAMPO HIDDEN PER VIA DEL CHECKBOX CHE PERDE IL VALORE:
		datiImpegno.getStep1Model().setPrenotazioneLiquidabile(datiImpegno.getStep1Model().getHiddenPerPrenotazioneLiquidabile());
		//
		
		//NUOVI CAMPI, prenotazione, Impegno di cassa economale, 
		if(datiImpegno.getStep1Model().getPrenotazione().equalsIgnoreCase("si")){
			impegno.setFlagPrenotazione(true);
			if(datiImpegno.getStep1Model().getPrenotazioneLiquidabile()!=null){
				impegno.setFlagPrenotazioneLiquidabile(datiImpegno.getStep1Model().getPrenotazioneLiquidabile());
			}
		}else{
			impegno.setFlagPrenotazione(false);
			//PRENOTAZIONE A FALSE IMPLICA CHE PRENOTAZIONE LIQUIDABILE NON PUO' ESSERE TRUE
			//in quanto ha senso solo se prenotazione true:
			impegno.setFlagPrenotazioneLiquidabile(false);
		}
		
		if(datiImpegno.getStep1Model().getCassaEconomale().equalsIgnoreCase("si")){
			impegno.setFlagCassaEconomale(true);
		}else{
			impegno.setFlagCassaEconomale(false);
		}
		
		if(datiImpegno.getImpegnoInAggiornamento().isFlagSDF()){
			impegno.setFlagSDF(true);
		}else{
			impegno.setFlagSDF(false);
		}
		//

		if (datiImpegno.getStep1Model().getAnnoImpOrigine() != null && datiImpegno.getStep1Model().getAnnoImpOrigine() != 0) { log.debug("","anno O -*-> " + datiImpegno.getStep1Model().getAnnoImpOrigine());
			impegno.setAnnoImpegnoOrigine(datiImpegno.getStep1Model().getAnnoImpOrigine());
		}
		if (datiImpegno.getStep1Model().getNumImpOrigine() != null && datiImpegno.getStep1Model().getNumImpOrigine() != 0) { log.debug("","num O -*-> " + datiImpegno.getStep1Model().getNumImpOrigine());
			impegno.setNumImpegnoOrigine(datiImpegno.getStep1Model().getNumImpOrigine());
		}

		////////////////////////////////////////////////////////////////////////////////////////
		impegno.setAnnoMovimento(datiImpegno.getStep1Model().getAnnoImpegno()); //da verificare
		impegno.setUid(datiImpegno.getStep1Model().getUid()); //da verificare
		impegno.setNumeroBigDecimal(new BigDecimal(datiImpegno.getStep1Model().getNumeroImpegno())); //da verificare
		///////////////////////////////////////////////////////////////////////////////////////
		if(datiImpegno.getStep1Model().getOggettoImpegno()!=null){
			impegno.setDescrizione(datiImpegno.getStep1Model().getOggettoImpegno().toUpperCase());
		}
		impegno.setCig(datiImpegno.getStep1Model().getCig());
		impegno.setCup(datiImpegno.getStep1Model().getCup());
		
		//SIOPE PLUS:
		impostaDatiSiopePlusPerInserisciAggiorna(datiImpegno.getStep1Model(), impegno);
		//END SIOPE PLUS
		
		//////////////////Modifica Movimento Spesa //////////////////////////////
		impegno.setListaModificheMovimentoGestioneSpesa(datiImpegno.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa());
		/////////////////////////////////////////////////////////////////////////
		
		// PROGETTO
		if(null!=datiImpegno.getStep1Model().getProgetto()){
			Progetto progetto = new Progetto();
			// SIAC-6990
			// Popolo i campi del progetto con i parametri che servono per il getProgetto() di > MGOD
			progetto.setCodice(datiImpegno.getStep1Model().getProgetto());
			progetto.setTipoProgetto(TipoProgetto.GESTIONE);
			progetto.setBilancio(sessionHandler.getBilancio());
//			progetto.setStatoOperativoProgetto();
			impegno.setProgetto(progetto);
		}
		
		// popolo tutti e 2 i valori
		impegno.setImportoIniziale(datiImpegno.getStep1Model().getImportoImpegno());
		impegno.setImportoAttuale(convertiImportoToBigDecimal(datiImpegno.getStep1Model().getImportoFormattato())); 
		
		impegno.setDataScadenza(DateUtility.parse(datiImpegno.getStep1Model().getScadenza()));
		
		// valori classificazioni della seconda pagina
		impegno.setCodMissione(datiImpegno.getMissioneSelezionata());
		impegno.setCodProgramma(datiImpegno.getProgrammaSelezionato());
		// provvedimento
		impegno.setAttoAmministrativo(popolaProvvedimento(datiImpegno));
	
		//SOGGETTO:
		Soggetto soggetto = new Soggetto();
		soggetto.setCodiceSoggetto(datiImpegno.getStep1Model().getSoggetto().getCodCreditore());
		impegno.setSoggetto(soggetto);
		if (datiImpegno.getStep1Model().getSoggetto().getClasse() != null && !"".equalsIgnoreCase(datiImpegno.getStep1Model().getSoggetto().getClasse())) {
			ClasseSoggetto classe = new ClasseSoggetto();
			classe.setCodice(datiImpegno.getStep1Model().getSoggetto().getClasse());
			impegno.setClasseSoggetto(classe);
		}
		
		if (pluriennale != null) {
			if (pluriennale.getAnnoImpPluriennale() <= (bilancio.getAnno() + 2)) {
				impegno.setAnnoCapitoloOrigine(pluriennale.getAnnoImpPluriennale());
			} else {
				impegno.setAnnoCapitoloOrigine(bilancio.getAnno() + 2);
			}
			impegno.setAnnoMovimento(pluriennale.getAnnoImpPluriennale());
			impegno.setImportoIniziale(pluriennale.getImportoImpPluriennale());
			impegno.setDataScadenza(pluriennale.getDataScadenzaImpPluriennale());
		}
		
		impegno.setParereFinanziario(datiImpegno.getStep1Model().isParereFinanziario());
	}
	
	

	
	protected Accertamento popolaAccertamentoAggiorna (GestisciMovGestModel datiAccertamento, ImpegniPluriennaliModel pluriennale, Bilancio bilancio) {
		Accertamento accertamento = new Accertamento();
		popolaAccertamentoCore(datiAccertamento, pluriennale, bilancio,
				accertamento);
		
		
		// jira 4296
		// il flagGSA non viene gestito e quindi arriva al servizio sempre false
		accertamento.setFlagAttivaGsa(datiAccertamento.getStep1Model().getFlagAttivaGsa().equalsIgnoreCase("Si")? true : false );
		
		// albero PDC	
		accertamento.setCodPdc(datiAccertamento.getPianoDeiConti().getCodice()); 
		
		//SIOPE
		accertamento.setCodSiope(datiAccertamento.getSiopeSpesa().getCodice()); 
		
		accertamento.setCodCofog(datiAccertamento.getCofogSelezionato());
		accertamento.setCodTransazioneEuropeaSpesa(datiAccertamento.getTransazioneEuropeaSelezionato());
		accertamento.setCodRicorrenteSpesa(datiAccertamento.getRicorrenteEntrataSelezionato());
		accertamento.setCodCapitoloSanitarioSpesa(datiAccertamento.getPerimetroSanitarioEntrataSelezionato());
		accertamento.setCodPrgPolReg(datiAccertamento.getPoliticaRegionaleSelezionato());
		
		accertamento.setCodClassGen16(datiAccertamento.getClassGenSelezionato6());
		accertamento.setCodClassGen17(datiAccertamento.getClassGenSelezionato7());
		accertamento.setCodClassGen18(datiAccertamento.getClassGenSelezionato8());
		accertamento.setCodClassGen19(datiAccertamento.getClassGenSelezionato9());
		accertamento.setCodClassGen20(datiAccertamento.getClassGenSelezionato10());
		
		return accertamento;
		
	}
	
	protected Accertamento popolaAggiornaAccertamentoRequestPerAggiornaModifiche (GestisciMovGestModel datiAccertamento, ImpegniPluriennaliModel pluriennale, Bilancio bilancio) {
		Accertamento accertamento = new Accertamento();
		accertamento.setCapitoloEntrataGestione(convertModelAccertamentoPerChiamata(datiAccertamento.getStep1Model().getCapitolo()));
		accertamento.setAnnoCapitoloOrigine(datiAccertamento.getStep1Model().getCapitolo().getAnno());
		
		// GESTIONE PARERE FINANZIARIO PER L'ACCERTAMENTO E' SEMPRE TRUE
		accertamento.setParereFinanziario(Boolean.TRUE);
		
		//INSERIRE IL TIPO IMPEGNO CORRETTO
		ClassificatoreGenerico cg = new ClassificatoreGenerico();
		cg.setCodice(datiAccertamento.getStep1Model().getTipoImpegno());
		
		accertamento.setTipoImpegno(cg);
		
		accertamento.setStatoOperativoMovimentoGestioneEntrata(datiAccertamento.getStep1Model().getStato());
	
		////////////////////////////////////////////////////////
		//correzione anomalia flag riaccertamento
		//SIAC-6997
		if(datiAccertamento.getStep1Model().getRiaccertato().equalsIgnoreCase("si") || datiAccertamento.getStep1Model().getReanno().equalsIgnoreCase("si")){
			
			if(datiAccertamento.getStep1Model().getRiaccertato().equalsIgnoreCase("si")) {
				accertamento.setFlagDaRiaccertamento(true);
			}else if(datiAccertamento.getStep1Model().getReanno().equalsIgnoreCase("si")) {
				accertamento.setFlagDaReanno(true);
			}
			
			if (datiAccertamento.getStep1Model().getAnnoImpRiacc() != null && datiAccertamento.getStep1Model().getAnnoImpRiacc() != 0) { 
				accertamento.setAnnoRiaccertato(datiAccertamento.getStep1Model().getAnnoImpRiacc());
			}
			if (datiAccertamento.getStep1Model().getNumImpRiacc() != null && datiAccertamento.getStep1Model().getNumImpRiacc().intValue()!=0) { 
				accertamento.setNumeroRiaccertato(new BigDecimal(datiAccertamento.getStep1Model().getNumImpRiacc()));
			}
		} else { 
			accertamento.setFlagDaRiaccertamento(false);
			accertamento.setFlagDaReanno(false);
		}	
		
		//SIAC-6997
		if(datiAccertamento.getStep1Model().getStrutturaSelezionataCompetente() != null && !datiAccertamento.getStep1Model().getStrutturaSelezionataCompetente().equals("")){
			accertamento.setStrutturaCompetente(datiAccertamento.getStep1Model().getStrutturaSelezionataCompetente());
		}
		
		if (datiAccertamento.getStep1Model().getAnnoImpOrigine() != null && datiAccertamento.getStep1Model().getAnnoImpOrigine() != 0) { 
			accertamento.setAnnoAccertamentoOrigine(datiAccertamento.getStep1Model().getAnnoImpOrigine());
		}
		if (datiAccertamento.getStep1Model().getNumImpOrigine() != null && datiAccertamento.getStep1Model().getNumImpOrigine() != 0) { 
			accertamento.setNumAccertamentoOrigine(datiAccertamento.getStep1Model().getNumImpOrigine());
		}

		////////////////////////////////////////////////////////////////////////////////////////
		accertamento.setAnnoMovimento(datiAccertamento.getStep1Model().getAnnoImpegno()); //da verificare
		//accertamento.setUid(datiAccertamento.getStep1Model().getUid()); //da verificare
		accertamento.setUid(datiAccertamento.getAccertamentoInAggiornamento().getUid()); //da verificare
		accertamento.setNumeroBigDecimal(new BigDecimal(datiAccertamento.getStep1Model().getNumeroImpegno())); //da verificare
		accertamento.setTipoMovimento(datiAccertamento.getAccertamentoInAggiornamento().getTipoMovimento());
		accertamento.setTipoMovimentoDesc(datiAccertamento.getAccertamentoInAggiornamento().getTipoMovimentoDesc());
		///////////////////////////////////////////////////////////////////////////////////////
		
		if(null!=datiAccertamento.getStep1Model().getOggettoImpegno()){
			accertamento.setDescrizione(datiAccertamento.getStep1Model().getOggettoImpegno().toUpperCase());
		}
		
		//////////////////Modifica Movimento Spesa //////////////////////////////
		accertamento.setListaModificheMovimentoGestioneEntrata(datiAccertamento.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata());
		/////////////////////////////////////////////////////////////////////////
		
		if(null!=datiAccertamento.getStep1Model().getProgetto()){
			Progetto progetto = new Progetto();
			progetto.setCodice(datiAccertamento.getStep1Model().getProgetto());
			progetto.setTipoProgetto(TipoProgetto.GESTIONE);
			progetto.setBilancio(sessionHandler.getBilancio());
			accertamento.setProgetto(progetto);
		}
		
		// popolo tutti e 2 i valori
		accertamento.setImportoIniziale(datiAccertamento.getStep1Model().getImportoImpegno());
		accertamento.setImportoAttuale(convertiImportoToBigDecimal(datiAccertamento.getStep1Model().getImportoFormattato())); 
		
		accertamento.setDataScadenza(DateUtility.parse(datiAccertamento.getStep1Model().getScadenza()));
		
		// valori classificazioni della seconda pagina
		accertamento.setCodMissione(datiAccertamento.getMissioneSelezionata());
		accertamento.setCodProgramma(datiAccertamento.getProgrammaSelezionato());
		
		// provvedimento dell'impegno! (fix per jira SIAC-2647)
		accertamento.setAttoAmministrativo(datiAccertamento.getAccertamentoInAggiornamento().getAttoAmministrativo());
		
		impostaSoggettoOClassePerRequestAggiornaAccertamento(datiAccertamento, accertamento);
		
		if (pluriennale != null) {
			if (pluriennale.getAnnoImpPluriennale() <= (bilancio.getAnno() + 2)) {
				accertamento.setAnnoCapitoloOrigine(pluriennale.getAnnoImpPluriennale());
			} else {
				accertamento.setAnnoCapitoloOrigine(bilancio.getAnno() + 2);
			}
			accertamento.setAnnoMovimento(pluriennale.getAnnoImpPluriennale());
			accertamento.setImportoIniziale(pluriennale.getImportoImpPluriennale());
			accertamento.setDataScadenza(pluriennale.getDataScadenzaImpPluriennale());
		}
		
		accertamento.setCodPdc(datiAccertamento.getPianoDeiConti().getCodice()); 
		accertamento.setCodSiope(datiAccertamento.getSiopeSpesa().getCodice()); 
		
		accertamento.setCodCofog(datiAccertamento.getCofogSelezionato());
		accertamento.setCodTransazioneEuropeaSpesa(datiAccertamento.getTransazioneEuropeaSelezionato());
		accertamento.setCodRicorrenteSpesa(datiAccertamento.getRicorrenteEntrataSelezionato());
		accertamento.setCodCapitoloSanitarioSpesa(datiAccertamento.getPerimetroSanitarioEntrataSelezionato());
		accertamento.setCodPrgPolReg(datiAccertamento.getPoliticaRegionaleSelezionato());
		
		accertamento.setCodClassGen16(datiAccertamento.getClassGenSelezionato6());
		accertamento.setCodClassGen17(datiAccertamento.getClassGenSelezionato7());
		accertamento.setCodClassGen18(datiAccertamento.getClassGenSelezionato8());
		accertamento.setCodClassGen19(datiAccertamento.getClassGenSelezionato9());
		accertamento.setCodClassGen20(datiAccertamento.getClassGenSelezionato10());
		
		accertamento.setFlagFattura(webAppSiNoToBool(datiAccertamento.getStep1Model().getFlagFattura()));
		accertamento.setFlagCorrispettivo(webAppSiNoToBool(datiAccertamento.getStep1Model().getFlagCorrispettivo()));
		//
		
		
		//flag attiva gsa:
		accertamento.setFlagAttivaGsa(webAppSiNoToBool(datiAccertamento.getStep1Model().getFlagAttivaGsa()));
		//
		
		//JIRA SIAC-2813
		if(!StringUtils.isEmpty(datiAccertamento.getStep1Model().getImportoUtilizzabileFormattato())){
			accertamento.setImportoUtilizzabile(convertiImportoToBigDecimal(datiAccertamento.getStep1Model().getImportoUtilizzabileFormattato()));
		}
		
		
		return accertamento;
		
	}
	//SIAC-8503
	private void impostaSoggettoOClassePerRequestAggiornaAccertamento(GestisciMovGestModel datiAccertamento, Accertamento accertamento) {
		SoggettoImpegnoModel soggettoModel = datiAccertamento.getStep1Model().getSoggetto();
		String codCreditore = soggettoModel.getCodCreditore(); 
		
		String classeCode = soggettoModel.getClasse(); //soggettoModel.setCodCreditore("366032");
		
		
		boolean valorizzatoSoggetto = StringUtils.isNotBlank(codCreditore); 
		boolean valorizzataClasse = StringUtils.isNotBlank(classeCode);
		
		
		if(!(valorizzatoSoggetto && valorizzataClasse)) {
			//SOLO UNO TRA I DUE E' VALORIZZATO, PROCEDO CON IL POPOLAMENTO CON I DATI CHE MI SONO ARRIVATI
			effettuaPopolamentoSoggettoClasse(accertamento, codCreditore, classeCode);
			return;
		}
		//SIA CLASSE SOGGETTO CHE CODICE SONO VALORIZZATI, CONSIDERO I VALORI PRIMA DELLE MODIFICHE UTENTI
		String originalCodCreditore = sessionHandler.getParametro(FinSessionParameter.SOGGETTO_ACCERTAMENTO_ORIGINAL, String.class);
		String originalClasseCode = sessionHandler.getParametro(FinSessionParameter.CLASSE_ACCERTAMENTO_ORIGINAL, String.class);
		
		boolean accertamentoProvvisorio =  datiAccertamento.getStep1Model().getStatoOperativo() == "P";
		if(!accertamentoProvvisorio) {
			//L'ACCERTAMENTO NON E' PROVVISORIO, L'UTENTE NON AVREBBE DOVUTO POTER MODIFICARE QUESTI DATI, 
			//IMPOSTO QUELLI PRIMA DELLE SUE MODIFICHE
			effettuaPopolamentoSoggettoClasse(accertamento, originalCodCreditore, originalClasseCode);
			return;

		}
		boolean codCreditoreCambiato = codCreditore.equals(originalCodCreditore);
		boolean classeCambiata = classeCode.equals(originalClasseCode);
		if(codCreditoreCambiato &&!classeCambiata) {
			//SE IL CODICE E' CAMBIATO, MA LA CLASSE HA LO STESSO VALORE, DEVO SBIANCARE LA CLASSE PERCHE' E' UN VALORE VECCHIO
			effettuaPopolamentoSoggettoClasse(accertamento, codCreditore, null);
			return;
		}
		if(classeCambiata) {
			effettuaPopolamentoSoggettoClasse(accertamento, null, classeCode);
			return;
		}
		//CHE COSA FACCIO QUI? 
		effettuaPopolamentoSoggettoClasse(accertamento, codCreditore, classeCode);
			
	}

	private void effettuaPopolamentoSoggettoClasse(Accertamento accertamento, String originalCodCreditore, String originalClasseCode) {
		if(StringUtils.isNotBlank(originalCodCreditore)) {
			Soggetto soggetto = new Soggetto();
			soggetto.setCodiceSoggetto(originalCodCreditore);
			accertamento.setSoggetto(soggetto);
		}
		if (StringUtils.isNotBlank(originalClasseCode)) {
			ClasseSoggetto classe = new ClasseSoggetto();
			classe.setCodice(originalClasseCode);
			accertamento.setClasseSoggetto(classe);
		}
	}

	private void popolaAccertamentoCore(GestisciMovGestModel datiAccertamento,
			ImpegniPluriennaliModel pluriennale, Bilancio bilancio,
			Accertamento accertamento) {
		accertamento.setCapitoloEntrataGestione(convertModelAccertamentoPerChiamata(datiAccertamento.getStep1Model().getCapitolo()));
		accertamento.setAnnoCapitoloOrigine(datiAccertamento.getStep1Model().getCapitolo().getAnno());
		
		// GESTIONE PARERE FINANZIARIO PER L'ACCERTAMENTO E' SEMPRE TRUE
		accertamento.setParereFinanziario(Boolean.TRUE);
		
		//INSERIRE IL TIPO IMPEGNO CORRETTO
		ClassificatoreGenerico cg = new ClassificatoreGenerico();
		cg.setCodice(datiAccertamento.getStep1Model().getTipoImpegno());
		
		accertamento.setTipoImpegno(cg);
		
		accertamento.setStatoOperativoMovimentoGestioneEntrata(datiAccertamento.getStep1Model().getStato());
	
		////////////////////////////////////////////////////////
		//correzione anomalia flag riaccertamento
		if(datiAccertamento.getStep1Model().getRiaccertato().equalsIgnoreCase("si") || datiAccertamento.getStep1Model().getReanno().equalsIgnoreCase("si")){
			
			if(datiAccertamento.getStep1Model().getRiaccertato().equalsIgnoreCase("si")) {
				accertamento.setFlagDaRiaccertamento(true);
			}else if(datiAccertamento.getStep1Model().getReanno().equalsIgnoreCase("si")) {
				accertamento.setFlagDaReanno(true);
			}
			
			if (datiAccertamento.getStep1Model().getAnnoImpRiacc() != null && datiAccertamento.getStep1Model().getAnnoImpRiacc() != 0) { 
				accertamento.setAnnoRiaccertato(datiAccertamento.getStep1Model().getAnnoImpRiacc());
			}
			if (datiAccertamento.getStep1Model().getNumImpRiacc() != null && datiAccertamento.getStep1Model().getNumImpRiacc().intValue()!=0) { 
				accertamento.setNumeroRiaccertato(new BigDecimal(datiAccertamento.getStep1Model().getNumImpRiacc()));
			}
		} else { 
			accertamento.setFlagDaRiaccertamento(false);
			accertamento.setFlagDaReanno(false);
		}	
				
		if (datiAccertamento.getStep1Model().getAnnoImpOrigine() != null && datiAccertamento.getStep1Model().getAnnoImpOrigine() != 0) { 
			accertamento.setAnnoAccertamentoOrigine(datiAccertamento.getStep1Model().getAnnoImpOrigine());
		}
		if (datiAccertamento.getStep1Model().getNumImpOrigine() != null && datiAccertamento.getStep1Model().getNumImpOrigine() != 0) { 
			accertamento.setNumAccertamentoOrigine(datiAccertamento.getStep1Model().getNumImpOrigine());
		}

		////////////////////////////////////////////////////////////////////////////////////////
		accertamento.setAnnoMovimento(datiAccertamento.getStep1Model().getAnnoImpegno()); //da verificare
		//accertamento.setUid(datiAccertamento.getStep1Model().getUid()); //da verificare
		accertamento.setUid(datiAccertamento.getAccertamentoInAggiornamento().getUid()); //da verificare
		accertamento.setNumeroBigDecimal(new BigDecimal(datiAccertamento.getStep1Model().getNumeroImpegno())); //da verificare
		accertamento.setTipoMovimento(datiAccertamento.getAccertamentoInAggiornamento().getTipoMovimento());
		accertamento.setTipoMovimentoDesc(datiAccertamento.getAccertamentoInAggiornamento().getTipoMovimentoDesc());
		///////////////////////////////////////////////////////////////////////////////////////
		
		if(null!=datiAccertamento.getStep1Model().getOggettoImpegno()){
			accertamento.setDescrizione(datiAccertamento.getStep1Model().getOggettoImpegno().toUpperCase());
		}
		
		//////////////////Modifica Movimento Spesa //////////////////////////////
		accertamento.setListaModificheMovimentoGestioneEntrata(datiAccertamento.getAccertamentoInAggiornamento().getListaModificheMovimentoGestioneEntrata());
		/////////////////////////////////////////////////////////////////////////
		
		if(null!=datiAccertamento.getStep1Model().getProgetto()){
			Progetto progetto = new Progetto();
			progetto.setCodice(datiAccertamento.getStep1Model().getProgetto());
			progetto.setTipoProgetto(TipoProgetto.GESTIONE);
			progetto.setBilancio(sessionHandler.getBilancio());
			accertamento.setProgetto(progetto);
		}
		
		// popolo tutti e 2 i valori
		accertamento.setImportoIniziale(datiAccertamento.getStep1Model().getImportoImpegno());
		accertamento.setImportoAttuale(convertiImportoToBigDecimal(datiAccertamento.getStep1Model().getImportoFormattato())); 
		
		accertamento.setDataScadenza(DateUtility.parse(datiAccertamento.getStep1Model().getScadenza()));
		
		// valori classificazioni della seconda pagina
		accertamento.setCodMissione(datiAccertamento.getMissioneSelezionata());
		accertamento.setCodProgramma(datiAccertamento.getProgrammaSelezionato());
		
		// provvedimento
		accertamento.setAttoAmministrativo(popolaProvvedimento(datiAccertamento));
		
		//SIAC-8503: la modifica ha creato una regressione sulla classe e soggetto quando vengono modificati
		//la risolvo
		impostaSoggettoOClassePerRequestAggiornaAccertamento(datiAccertamento, accertamento);
		
		if (pluriennale != null) {
			if (pluriennale.getAnnoImpPluriennale() <= (bilancio.getAnno() + 2)) {
				accertamento.setAnnoCapitoloOrigine(pluriennale.getAnnoImpPluriennale());
			} else {
				accertamento.setAnnoCapitoloOrigine(bilancio.getAnno() + 2);
			}
			accertamento.setAnnoMovimento(pluriennale.getAnnoImpPluriennale());
			accertamento.setImportoIniziale(pluriennale.getImportoImpPluriennale());
			accertamento.setDataScadenza(pluriennale.getDataScadenzaImpPluriennale());
		}
		
		// 	SIAC-3654 veniva perso il flag prevista fattura:
		accertamento.setFlagFattura(webAppSiNoToBool(datiAccertamento.getStep1Model().getFlagFattura()));

		accertamento.setFlagCorrispettivo(webAppSiNoToBool(datiAccertamento.getStep1Model().getFlagCorrispettivo()));
	}
	
	protected Accertamento popolaAccertamentoAggiornaSubimpegno(GestisciMovGestModel datiAccertamento, ImpegniPluriennaliModel pluriennale, Bilancio bilancio) {
		Accertamento accertamento = new Accertamento();
		popolaAccertamentoCore(datiAccertamento, pluriennale, bilancio,
				accertamento);
		
		// jira 4296
		// il flagGSA non viene gestito e quindi arriva al servizio sempre false
		accertamento.setFlagAttivaGsa(datiAccertamento.getStep1Model().getFlagAttivaGsa().equalsIgnoreCase("Si")? true : false );
		
		// albero PDC
		accertamento.setCodPdc(datiAccertamento.getAccertamentoInAggiornamento().getCodPdc());
		
		accertamento.setCodSiope(datiAccertamento.getAccertamentoInAggiornamento().getCodSiope());
		accertamento.setCodCofog(datiAccertamento.getAccertamentoInAggiornamento().getCodCofog());
		accertamento.setCodTransazioneEuropeaSpesa(datiAccertamento.getAccertamentoInAggiornamento().getCodTransazioneEuropeaSpesa());
		accertamento.setCodRicorrenteSpesa(datiAccertamento.getAccertamentoInAggiornamento().getCodRicorrenteSpesa());
		accertamento.setCodCapitoloSanitarioSpesa(datiAccertamento.getAccertamentoInAggiornamento().getCodCapitoloSanitarioSpesa());
		accertamento.setCodPrgPolReg(datiAccertamento.getAccertamentoInAggiornamento().getCodPrgPolReg());
		accertamento.setCodClassGen11(datiAccertamento.getAccertamentoInAggiornamento().getCodClassGen11());
		accertamento.setCodClassGen12(datiAccertamento.getAccertamentoInAggiornamento().getCodClassGen12());
		accertamento.setCodClassGen13(datiAccertamento.getAccertamentoInAggiornamento().getCodClassGen13());
		accertamento.setCodClassGen14(datiAccertamento.getAccertamentoInAggiornamento().getCodClassGen14());
		accertamento.setCodClassGen15(datiAccertamento.getAccertamentoInAggiornamento().getCodClassGen15());
		return accertamento;
		
	}

	
	
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
	 * Metodo di comodo per valorizzare un AttoAmministrativo
	 * a partire da GestisciMovGestModel
	 * @param datiImpegno
	 * @return
	 */
	protected AttoAmministrativo popolaProvvedimento(GestisciMovGestModel datiImpegno){
		return popolaProvvedimento(datiImpegno.getStep1Model().getProvvedimento());
	}
	
	protected <MMG extends ModificaMovimentoGestione> ModificaConsulta settaDatiReimputazioneInModificaConsulta(MMG modifica,ModificaConsulta mc){
		if(modifica.isReimputazione()){
	    	mc.setAnnoReimputazione(modifica.getAnnoReimputazione());
	    	mc.setReimputazione(WebAppConstants.Si);
	    	//SIAC-6997: Solo se Reimputazione Si Aggiungere Elaborato ROR – Reimp. in corso d’anno.
			if(modifica.isElaboraRorReanno()){
				mc.setReanno(WebAppConstants.Si);
			}else{
				mc.setReanno(WebAppConstants.No);
			}
	    } else {
	    	mc.setAnnoReimputazione(null);
	    	mc.setReimputazione(WebAppConstants.No);
	    }
		return mc;
	}
	
	/**
	 * 
	 * Marzo 2017
	 * 
	 * Metodo nato in seguito alla CR  718	SIAC-4619
	 * 
	 * Viene usato nella jsp della transazione elementare per proteggere alcuni campi
	 * 
	 * @return
	 */
	public boolean disabilitaCampoTePerGestisciMovP(){
		boolean disabilita = false;
		if(teSupport!=null && teSupport.getOggettoAbilitaTE()!=null){
			if(teSupport.getOggettoAbilitaTE().equals(OggettoDaPopolareEnum.IMPEGNO.toString()) ||
					teSupport.getOggettoAbilitaTE().equals(OggettoDaPopolareEnum.SUBIMPEGNO.toString())){				
				// SIAC-7395
//				disabilita =  isAzioneConsentita(AzioniConsentite.OP_SPE_gestisciImpegnoDecentratoP;
				disabilita =  isAzioneConsentita(AzioneConsentitaEnum.OP_COM_bloccaCTE_Imp);
			
			} else if(teSupport.getOggettoAbilitaTE().equals(OggettoDaPopolareEnum.ACCERTAMENTO.toString()) ||
					teSupport.getOggettoAbilitaTE().equals(OggettoDaPopolareEnum.SUBACCERTAMENTO.toString())){
				// SIAC-7395
//				disabilita =  isAzioneConsentita(AzioniConsentite.OP_ENT_gestisciAccertamentoDecentratoP;
				disabilita =  isAzioneConsentita(AzioneConsentitaEnum.OP_COM_bloccaCTE_Acc);
			}
		}
		return disabilita;
	}
	
	/**
	 * Settembre 2017 in seguito a SIAC-5288 
	 * posso entrare in aggiornamento impegno accertamento per modificare
	 * il solo campo gsa e quindi gli altri campi possono essere disabilitati 
	 * se ho solo abilitazione gsa
	 * 
	 * Fare ovverride con regole specifiche dove serve, questo mantiene retrocompatibilita'
	 * ritornando sempre true.
	 * 
	 * @return
	 */
	public boolean abilitatoModificaProvvedimento(){
		return true;
	}
	

	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.liquidazione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceLiquidazioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.ContoTesoreria;
import it.csi.siac.siacfinser.model.Distinta;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto.TipoAccredito;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;



@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciLiquidazioneStep2Action extends WizardInserisciLiquidazioneAction{
	
	private static final long serialVersionUID = 1L;
	private static final String ANNULLA = "annulla";	
	private static final String INDIETRO = "indietro";
	private static final String MODPAGAMENTO = "modpagamento";
	private static final String SEDISECONDARIE = "sedisecondarie";
	private static final String TRX = "trx";
	private static final String SALVA = "salva";
	private static final String PROCEED = "proceed";
	
	
	public boolean isAbilitatoAggiornamentoCampiSiopePlus(){
		//il siope pluse e' aggiornabile durante l'inserimento della liquidazione
		return true;
	}
	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		this.model.setTitolo("Inserimento Liquidazione - Dati Liquidazione");
		this.model.setStep("INSLIQSTEP2");
		
		//lista motivazioni assenza cig:
		caricaListaMotivazioniAssenzaCig();
		
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.LIQUIDAZIONE;
	}	
	
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {	
		model.setListaModalitaPagamentoSoggetto(model.getListaModalitaPagamentoSoggettoOrigine());
		model.setLockImportoLiquidazione(false);
		model.setLockProvvedimento(false);
		model.setLockTipoConvalida(false);
		model.setRichiediConfermaUtente(false);
		if(model.getTipoConvalidaList()==null || (model.getTipoConvalidaList()!=null && model.getTipoConvalidaList().size()==0)){
			//imposto i dati dei tipi convalida
			
			//MANUALE:
			CodificaFin manuale = new CodificaFin();
			manuale.setId(1);
			manuale.setCodice("manuale");
			manuale.setDescrizione("Manuale");
			
			//AUTOMATIC:
			CodificaFin automatico = new CodificaFin();
			automatico.setId(2);
			automatico.setCodice("automatico");
			automatico.setDescrizione("Automatico");
			
			//SETTO IN LISTA:
			model.getTipoConvalidaList().add(manuale);
			model.getTipoConvalidaList().add(automatico);
		}
		
		//OPERAZIONI PER TENERE AGGIORNATE LE VARIABILI DELLA GESTIONE DEL PROVVEDIMENTO AMMINITRATIVO:
		reimpostaCodiceStrutturaPadre(model.getProvvedimento());
		reimpostaCodiceStrutturaSelezionata(model.getProvvedimento());
		//////////////////////////////////////////////////////////////////////////////////////////
		
		//disabilito il caricamento degli alberi inutili per questo scnario (in AjaxAction.java):
		teSupport.setRicaricaAlberoPianoDeiConti(false);
		//CR-2023 si elimina CE
		
		teSupport.setRicaricaStrutturaAmministrativa(true);
		teSupport.setRicaricaSiopeSpesa(false);
		//////////////////////////////////////////////////////////////////////////////////////////
		
		if(model.isSalvataConValidaOra()){
			//QUI ricaricare eventuali dati rimiasti indietro se l'utente clicca sul filo di arianno 
			//tornando indietro dopo il salvataggio con valida ora
			ricaricaDatiPerClickSuFiloDiAriannaDopoAverSalvatoConValidaOra();
			//pulisco il flag per non ricaricare nuovamente ad ogni passaggio successivo:
			model.setSalvataConValidaOra(false);
		}
		
		return "success";
	}
	
	/**
	 * Se l'utente inserisce una liquidazione, da valida ora, e poi ritorna sullo step 2
	 * tramite filo di arianno occorre ricaricare alcune cose, esigenza nata
	 * per la jira SIAC-6056
	 */
	private void ricaricaDatiPerClickSuFiloDiAriannaDopoAverSalvatoConValidaOra(){
		//CHIAMIAMO IL SERVIZIO DI RICERCA IMPEGNO
		//(specificando se e' stato compilato o meno il sub, in tal caso cambia la request da comporre)
		
		boolean indicatoSub = isCompilatoNumeroSubImpegno();
		
		RicercaImpegnoPerChiaveOttimizzatoResponse respRk = ricercaImpegno(indicatoSub);
		
		if(!isFallimento(respRk) && respRk.getImpegno()!=null){
			
			// SIAC-6056 si perde l'aggiornamento del disp a liquidare
			Impegno ricaricatoImp = respRk.getImpegno();
			if(indicatoSub){
				//determino i sub impegni di riferimento:
				List<SubImpegno> elencoSubImpegni = elencoSubDiRiferimento(indicatoSub, respRk);
				if (!isEmpty(elencoSubImpegni)){
					SubImpegno subCorrispondente = FinUtility.findSubImpegnoByNumero(elencoSubImpegni, model.getNumeroSub());
					model.setImpegno(subCorrispondente);
				}
			} else {
				model.setImpegno(ricaricatoImp);
			}
			
			//settiamo eventuali mutui:
			settaVociMutuoNelModel();
			
			//imposta disp liquidare:
			impostaDisponibilitaLiquidare();
			
			//
		}
	}
	
	public String annulla(){
		model.setHasImpegnoSelezionatoPopup(false);
		return ANNULLA;
	}
	
	/**
	 *  click su indietro
	 * @return
	 */
	public String indietro(){
		model.setHasImpegnoSelezionatoPopup(false);
		return INDIETRO;
	}
	
	/**
	 * controlli sui dati
	 * @return
	 */
	public String verifica(){
		model.setCheckWarningsLiquidazioneStep2(false);
		
		//istanzio la lista per contenere gli eventuali errori:
		List<Errore> listaErrori = new ArrayList<Errore>();		
		
		//OBBLIGATORIETA SOGGETTO:
		if (model.getSoggetto() == null) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto"));
		}
		
		if (model.getSoggetto() != null && model.getSoggetto().getCodCreditore()==null) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto :Codice "));
		}
		
		//SOGGETTO BLOCCATO:
		if (model.getSoggetto() != null	&& model.getSoggetto().getStato() != null	&& !(model.getSoggetto().getStato()==StatoOperativoAnagrafica.VALIDO) && !(model.getSoggetto().getStato()==StatoOperativoAnagrafica.SOSPESO)) {
			listaErrori.add(ErroreFin.SOGGETTO_BLOCCATO.getErrore());
		}
		
		//TIPO CONVALIDA NON COMPILATA:
		if (this.model.getTipoConvalida() == null) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Impegno :Emissione ordinativo "));
		}		
		
		//IMPORTO LIQUIDAZIONE NON COMPILATO:
		if (this.model.getImportoLiquidazione() == null || this.model.getImportoLiquidazione().isEmpty()) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Liquidazione :Importo "));
		}
		
		//INDIVIDUIAMO LA MODALITA PAGAMENTO SELEZIONATA:
		ModalitaPagamentoSoggetto mps = null;
		SedeSecondariaSoggetto sss = null;
		if(model.getListaModalitaPagamentoSoggetto()!=null && model.getListaModalitaPagamentoSoggetto().size()>0){
			for(int i=0; i<model.getListaModalitaPagamentoSoggetto().size(); i++){
				if(model.getListaModalitaPagamentoSoggetto().get(i).getUid() == (model.getRadioModPagSelezionato())){
					mps = model.getListaModalitaPagamentoSoggetto().get(i);
					break;
				}
			}
		}
		
		//SIAC-7159
		//si invalida la Modalita Pagamento PagoPA
		if(mps!=null && mps.getCodiceModalitaPagamento()!=null && "APA".equals(mps.getModalitaAccreditoSoggetto().getCodice())){
//		if(mps!=null && mps.getModalitaAccreditoSoggetto()!=null && mps.getModalitaAccreditoSoggetto().getCodice()!=null && "APA".equals(mps.getModalitaAccreditoSoggetto().getCodice())){
			listaErrori.add(it.csi.siac.siacfin2ser.model.errore.ErroreFin.MOD_PAGO_PA_NON_AMMESSA.getErrore(""));
		}
		
		//CONTROLLIAMO CHE IL TIPO DI ACCREDITO DELLA MOD PAG SIA ACCETTABILE:
		
		if(mps!=null && mps.getModalitaAccreditoSoggetto()!=null && mps.getModalitaAccreditoSoggetto().getCodice()!=null && mps.getModalitaAccreditoSoggetto().getCodice().equals(Constanti.D_ACCREDITO_TIPO_CODE_Cessione_del_credito)){
			if(mps.getTipoAccredito().equals(TipoAccredito.CSC)){
				listaErrori.add(ErroreFin.MOD_PAGAMENTO_STATO_CESSIONE.getErrore(""));
			}else{
				listaErrori.add(ErroreFin.MOD_PAGAMENTO_STATO.getErrore());
			}
		}
		
		if(mps!=null && mps.getDescrizioneStatoModalitaPagamento()!=null && (mps.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("BLOCCATO") || mps.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("ANNULLATO")|| mps.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("PROVVISORIO"))){
			if(mps.getTipoAccredito().equals(TipoAccredito.CSC)){
				listaErrori.add(ErroreFin.MOD_PAGAMENTO_STATO_CESSIONE.getErrore(""));
			}else{
				listaErrori.add(ErroreFin.MOD_PAGAMENTO_STATO.getErrore());
			}
		}
		
		//ANNO E NUMERO PROVVEDIMENTO OBBLIGATORI:
		if(model.getProvvedimento().getAnnoProvvedimento()==null || model.getProvvedimento().getNumeroProvvedimento()==null){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Provvedimento : Anno e Numero "));
		}	
		
		if (this.model.getImportoLiquidazione()!= null && !this.model.getImportoLiquidazione().isEmpty()) {
			
			BigDecimal importoLiqBDcontrollo = convertiImportoToBigDecimal(model.getImportoLiquidazione());
			boolean importominorezero=false;
			boolean importomaggioredisponibilita=false;
			
			if(BigDecimal.ZERO.compareTo(importoLiqBDcontrollo)>=0){
				 importominorezero=true;
			}			
			
			BigDecimal importoConfronto=model.getDisponibilita();
			
			if(importoLiqBDcontrollo!=null && importoConfronto!=null){
				if(importoLiqBDcontrollo.compareTo(importoConfronto)>0){
					importomaggioredisponibilita=true;
					
				}
			}
			
			if(importominorezero || importomaggioredisponibilita){
				listaErrori.add(ErroreFin.MOD_PAGAMENTO_NON_VALIDE.getErrore());
			}
			
		}	
		
		// jira siac-1760
	    // controllo di congruenza di CIG 
		//cup e siope plus:
	    controlliCigCupESiopePlus(model, listaErrori);
		//
		
		if (!listaErrori.isEmpty()) {
			//presenza errori
			addErrori(listaErrori);
			return INPUT;
		}
		
		
		// controlli tabellina 4.6
		List<Errore> erroriAbilitazione = abilitazioneCampiTE(oggettoDaPopolare);
		if(null!=erroriAbilitazione && erroriAbilitazione.size()>0){
			//presenza errori
			addErrori(erroriAbilitazione);
			return INPUT;
		}
		
		if(model.getListaSediSecondarieSoggetto()!=null && !model.getListaSediSecondarieSoggetto().isEmpty()){
		
			for(int i=0; i<model.getListaSediSecondarieSoggetto().size(); i++){
				if(model.getListaSediSecondarieSoggetto().get(i).getUid() == (model.getRadioSediSecondarieSoggettoSelezionato())){
					sss = model.getListaSediSecondarieSoggetto().get(i);
					break;
				}
			}
		}
		
		
		InserisceLiquidazione request = new InserisceLiquidazione();
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setEnte(sessionHandler.getAccount().getEnte());
		request.setBilancio(sessionHandler.getBilancio());
		request.setSaltaInserimentoPrimaNotaGEN(model.isSaltaInserimentoPrimaNota());
		Liquidazione liquidazione = new Liquidazione();
		liquidazione.setDescrizioneLiquidazione(model.getDescrizioneLiquidazione());		
		
		if(model.getImportoLiquidazione()!=null ||  StringUtils.isEmpty(model.getImportoLiquidazione())){
			BigDecimal importoLiqBD = convertiImportoToBigDecimal(model.getImportoLiquidazione());
			liquidazione.setImportoLiquidazione(importoLiqBD);
		}
		
		liquidazione.setCig(model.getCig());
		liquidazione.setCup(model.getCup());
		
		//impostiamo i dati del siope plus:
		impostaDatiSiopePlusPerInserisciAggiorna(model, liquidazione);
		//
		
		AttoAmministrativo attoAmministrativo = popolaProvvedimento(model.getProvvedimento());
		
		
		liquidazione.setAttoAmministrativoLiquidazione(attoAmministrativo);
		Impegno impegno = model.getImpegnoPerServizio();
		SubImpegno subImpegno = null;
		
		if(model.getImpegnoPerServizio()!=null && model.getImpegno()!=null){
			BigDecimal codImpegnoModel = model.getImpegno().getNumero();
			
			if(model.getImpegno() instanceof SubImpegno)	{
				for(int i = 0; i<impegno.getElencoSubImpegni().size();i++){
					if(impegno.getElencoSubImpegni().get(i).getNumero().intValue()==codImpegnoModel.intValue()){
						subImpegno =impegno.getElencoSubImpegni().get(i);
						break;
					}
				}						
				ArrayList<SubImpegno> subImpegni = new ArrayList<SubImpegno>();
				subImpegni.add(subImpegno);
				impegno.setElencoSubImpegni(subImpegni);

			}else{
				impegno = model.getImpegno();
			}
			
		}
		
		// jira 2031, non funziona il salva della voce di mutuo, nel ser ho verificato che non arriva la selezione
		// e che si prende sempre la prima voce della lista
		liquidazione.setNumeroMutuo(model.getNumeroMutuoPopup());
		
		liquidazione.setImpegno(impegno);
		liquidazione.setSubImpegno(subImpegno);
		
		
		Soggetto soggetto = new Soggetto();
		ArrayList<ModalitaPagamentoSoggetto> listmps = new ArrayList<ModalitaPagamentoSoggetto>();
		listmps.add(mps);
		soggetto.setModalitaPagamentoList(listmps);
		ArrayList<SedeSecondariaSoggetto> listsss = new ArrayList<SedeSecondariaSoggetto>();
		listsss.add(sss);
		soggetto.setSediSecondarie(listsss);
		soggetto.setCodiceSoggetto(model.getSoggetto().getCodCreditore());
		liquidazione.setSoggettoLiquidazione(soggetto);	
		liquidazione.setModalitaPagamentoSoggetto(mps);
		
		ContoTesoreria conto = new ContoTesoreria();
		conto.setCodice(model.getContoTesoriere());
		liquidazione.setContoTesoreria(conto);
		
		Distinta distinta = new Distinta();
		distinta.setCodice(model.getDistinta());
		liquidazione.setDistinta(distinta);		
		
		liquidazione.setLiqManuale(model.getTipoConvalida());
		// Jira 1976, in fase di inserimento di liquidazione da ordinativo si deve settare non il liqManuale ma il
		// liqAutomatica a S
		liquidazione.setLiqAutomatica(Constanti.LIQUIDAZIONE_LIQ_AUTOMATICA_NO);
		
		liquidazione.setCodMissione(teSupport.getMissioneSelezionata());
		liquidazione.setCodProgramma(teSupport.getProgrammaSelezionato());
		liquidazione.setCodPdc(teSupport.getPianoDeiConti().getCodice());
		//CR-2023 eliminato ocnto economico
		liquidazione.setCodCofog(teSupport.getCofogSelezionato());
		liquidazione.setCodTransazioneEuropeaSpesa(teSupport.getTransazioneEuropeaSelezionato());
		liquidazione.setCodSiope(teSupport.getSiopeSpesa().getCodice());
		liquidazione.setCodRicorrenteSpesa(teSupport.getRicorrenteSpesaSelezionato());
		liquidazione.setCodCapitoloSanitarioSpesa(teSupport.getPerimetroSanitarioSpesaSelezionato());
		liquidazione.setCodPrgPolReg(teSupport.getPoliticaRegionaleSelezionato());
		
		request.setLiquidazione(liquidazione);
		
		request.setAnnoEsercizio(sessionHandler.getAnnoEsercizio());
		model.setRequest(request);
		
		//prima di salvare verifico la presenzaClasseSoggetto e ritorno warnings nel caso di fallimento verifica
		boolean esitoPresenzaClasseSoggetto = checkPresenzaClasseSoggetto(impegno);
		
		//se esitoPresenzaClasseSoggetto non e' true, ossia se la classe soggettto valorizzata non e' uguale a una di quelle selezionate dalla compilazione automatica (o manuale)
		if(!esitoPresenzaClasseSoggetto){
			model.setCheckWarningsLiquidazioneStep2(true);
			addActionWarning(ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore().getCodice()+" : "+ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore().getDescrizione());
			return INPUT;
		}
		if(checkImpegnoConImportoVincolato(impegno)){
			model.setCheckWarningsLiquidazioneStep2(true);
			addActionWarning(ErroreFin.WARNING_IMPORTO_VINCOLATO.getErrore().getCodice() +" : "+ErroreFin.WARNING_IMPORTO_VINCOLATO.getErrore().getDescrizione());
			return INPUT;			
		}
		
		return chiamaSalvaORichiediConferma();
		
	}	

	//JIRA-SIAC-6688
	private boolean checkImpegnoConImportoVincolato(Impegno impegno) {		
		boolean ris = true;
		 List<VincoloImpegno> listaVincoli = impegno.getVincoliImpegno();
		 
		 if(listaVincoli == null || listaVincoli.isEmpty()) {
			 return false;
		 }

		 BigDecimal importoLiq = convertiImportoToBigDecimal(model.getImportoLiquidazione());
		 
		 BigDecimal totOrdinativiIncasso = BigDecimal.ZERO;
		 
		 //JIRA SIAC-7025 NPE per impegno con vincolo senza accertamento
		 for(VincoloImpegno vincolo : listaVincoli) {
			 if(vincolo.getAccertamento() != null) {
				 BigDecimal impAttuale    = vincolo.getAccertamento().getImportoAttuale();
				 BigDecimal dispIncassare = vincolo.getAccertamento().getDisponibilitaIncassare();
				 totOrdinativiIncasso = totOrdinativiIncasso.add(impAttuale.subtract(dispIncassare));
			 
				 if(importoLiq.compareTo(totOrdinativiIncasso)< 0){
					 ris = false;
					 return ris;
				 }
			 }
		 }
		return ris; 
	}
	
	
	/**
	 * Chiama salva O richiedi conferma.
	 *
	 * @return the string
	 */
	public String chiamaSalvaORichiediConferma() {
		model.setCheckWarningsLiquidazioneStep2(false);
		return isNecessariaRichiestaConfermaUtente()? INPUT : salva();
	}
	
	private boolean isNecessariaRichiestaConfermaUtente() {
		
		if(model.isEnteAbilitatoGestionePrimaNotaDaFinanziaria() && !model.isRichiediConfermaUtente()) {
			// se l'utente e' abilitato alla gestione della prima nota da finanziaria e non gli e' stato ancora chiesta conferma,imposto i dati per chiederla
			model.setSaltaInserimentoPrimaNota(false);
			model.setRichiediConfermaUtente(true);
			return true;
		}
		return false;
	}

	/**
	 * Se l'impegno non ha un soggetto  e quindi un codice soggetto associato, devo fare alcuni controlli, e quindi, solo nel caso in cui l'impegno abbia la classe soggetto valorizzata, 
	 * controllo che questa sia uguale ad una di quelle selezionate dalla compilazione automatica soggetto (o scelata manuale) altrimenti vado avanti 
	 * senza controlli sulla scelta soggetto effettuata dall'utente.
	 */
	public boolean checkPresenzaClasseSoggetto(Impegno impegno){
		boolean esitoPresenzaClasseSoggetto = true;
		if(!model.isHasCodiceSoggetto()){
			if(impegno.getClasseSoggetto()!=null && impegno.getClasseSoggetto().getDescrizione()!=null && !impegno.getClasseSoggetto().getDescrizione().isEmpty()){
				esitoPresenzaClasseSoggetto = false;
				List<ClasseSoggetto> listClasseSoggetto = model.getSoggetto().getListClasseSoggetto();
				if(listClasseSoggetto!=null && listClasseSoggetto.size()>0 && impegno.getClasseSoggetto()!=null){
					for(int i=0; i<listClasseSoggetto.size(); i++){
						if(listClasseSoggetto.get(i).getDescrizione().equals(impegno.getClasseSoggetto().getDescrizione())){
							esitoPresenzaClasseSoggetto = true;
							break;
						}
					}
				//se ho inserito manualmente il codice creditore non ho la lista classi soggetto quindi la recupero
				}else if(model.getSoggetto()!= null && model.getSoggetto().getCodCreditore()!=null && listClasseSoggetto==null){
					RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(convertiModelPerChiamataServizioRicercaPerChiave(model.getSoggetto().getCodCreditore()));
					Soggetto s = response.getSoggetto();
					if(s!=null){
						SoggettoImpegnoModel soggettoImpegnoModel = new SoggettoImpegnoModel();
						soggettoImpegnoModel.setCodCreditore(s.getCodiceSoggetto());
						soggettoImpegnoModel.setCodfisc(s.getCodiceFiscale());
						soggettoImpegnoModel.setPiva(s.getPartitaIva());
						soggettoImpegnoModel.setDenominazione(s.getDenominazione());
						soggettoImpegnoModel.setStato(s.getStatoOperativo());
						soggettoImpegnoModel.setUid(s.getUid());
						if(s.getElencoClass()!=null && s.getElencoClass().size()>0){
							List<ClasseSoggetto> listaClassificazioni = new ArrayList<ClasseSoggetto>();
							for(int i=0; i<s.getElencoClass().size(); i++){
								ClasseSoggetto cls = new ClasseSoggetto(); 
								cls.setCodice(s.getElencoClass().get(i).getSoggettoClasseCode());
								cls.setDescrizione(s.getElencoClass().get(i).getSoggettoClasseDesc());
								listaClassificazioni.add(cls);
							}
							soggettoImpegnoModel.setListClasseSoggetto(listaClassificazioni);
							if(listaClassificazioni!=null && listaClassificazioni.size()>0){
								for(int i=0; i<listaClassificazioni.size(); i++){
									if(listaClassificazioni.get(i).getDescrizione().equals(impegno.getClasseSoggetto().getDescrizione())){
										esitoPresenzaClasseSoggetto = true;
										break;
									}
								}
							}
						}
						model.setListaRicercaSoggetto(null);
						model.setSoggettoTrovato(false);
						setSoggettoSelezionato(soggettoImpegnoModel);
					}else{
						//: se chiamando il servizio RicercaSoggettoPerChiave ottengo FALLIMENTO passo true a presenzaClasseSoggetto e vado avanti con il salvataggio
						esitoPresenzaClasseSoggetto = true;
					}
				}
			}
		}
		return esitoPresenzaClasseSoggetto;
	}
	
	public String salva(){
		
		//ricontrolliamo il siope:
		codiceSiopeChangedInternal(teSupport.getSiopeSpesaCod());
		//
		

		// controllo sul V livello
		//nuovo confronto per capire
		//il livello del PDC
		if(teSupport.getPianoDeiConti()!=null && 
		   teSupport.getPianoDeiConti().getTipoClassificatore()!=null && 
		   !teSupport.getPianoDeiConti().getTipoClassificatore().getCodice().equalsIgnoreCase(V_LIVELLO_TIPO_CLASSIFICATORE)){
			
			// CR-2023
			addErrore(ErroreFin.IMPEGNO_NON_COMPATIBILE.getErrore(""));
			addErrore(ErroreFin.ELEM_PDC_NON_INDICATO.getErrore(""));
			//SIAC-5931: se ci sono errori, non devo mostrare di nuovo la richiesta di conferma, prima l'utente deve controllare l'errore 
			model.setRichiediConfermaUtente(false);
			return INPUT;
		}

		
		model.setCheckWarningsLiquidazioneStep2(false);
//		if(isNecessariaRichiestaConfermaUtente()) {
//			return INPUT;
//		}
		model.getRequest().setSaltaInserimentoPrimaNotaGEN(model.isSaltaInserimentoPrimaNota());
		
		InserisceLiquidazioneResponse response = liquidazioneService.inserisceLiquidazione(model.getRequest());	
		if(response.getEsito()==Esito.SUCCESSO){
			if(response.getLiquidazione()!=null){
				model.setAnnoLiquidazioneConsulta(response.getLiquidazione().getAnnoLiquidazione());
				model.setNumeroLiquidazioneConsulta(response.getLiquidazione().getNumeroLiquidazione());
			}
			addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " "  + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
			
			model.setUidDaCompletare(response.getRegistrazioneMovFinFIN() != null? response.getRegistrazioneMovFinFIN().getUid() : 0);
			//SIAC-5333
			if(model.isEnteAbilitatoGestionePrimaNotaDaFinanziaria() && model.isSaltaInserimentoPrimaNota() && model.getUidDaCompletare() == 0) {
				addPersistentActionError(ErroreGEN.OPERAZIONE_NON_CONSENTITA.getErrore(WebAppConstants.CONDIZIONI_REGISTRAZIONE_NON_SODDISFATTE).getTesto());
			}
			
			
			if(model.isEnteAbilitatoGestionePrimaNotaDaFinanziaria() && model.isSaltaInserimentoPrimaNota() && model.getUidDaCompletare() != 0){
				// SIAC-6056 si perde l'aggiornamento del disp a liquidare
				model.setSalvataConValidaOra(true);
				return PROCEED;
			} else {
				return SALVA;
			}
			
		}else{
			//presenza errori
			addErrori(response.getErrori());
			//SIAC-5881: se ci sono errori, non devo mostrare di nuovo la richiesta di conferma, prima l'utente deve controllare l'errore 
			model.setRichiediConfermaUtente(false);
			return INPUT;
		}
	}
	
	public String setcup(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String cup = request.getParameter("cup");
		model.getTrxModel().setCup(cup);
		return TRX;
	}
	
	//aggiorna la tabella modalita di pagamento su selezione della sede e aggiorna quindi la lista sedi associate alla modalita' di pagamento scelta
	public String remodpagamento(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String cod = request.getParameter("selection");
		String modpagOrig = request.getParameter("modpagOrig");
		int uidModpag = 0;
		try {
			uidModpag = Integer.parseInt(modpagOrig);
		} catch(NumberFormatException nfe) {/* Ignore error, might happen... */}
		
		if(!(cod.equals("false"))){
			List<ModalitaPagamentoSoggetto> listaModalitaPagamento = model.getListaModalitaPagamentoSoggettoOrigine();
			List<SedeSecondariaSoggetto>  listaSediSecondarie = model.getListaSediSecondarieSoggetto();
			SedeSecondariaSoggetto sedeSelezionata = null;
			for(int i = 0; i<listaSediSecondarie.size(); i++){
				if(Integer.parseInt(cod) == listaSediSecondarie.get(i).getUid()){
					sedeSelezionata = listaSediSecondarie.get(i);
					model.setSedeSelezionata(sedeSelezionata);
					break;
				}
			}
			if(sedeSelezionata!=null){
				List<ModalitaPagamentoSoggetto> relistaModalitaPagamento = new ArrayList<ModalitaPagamentoSoggetto>();
				int first = 0;
				if(listaModalitaPagamento!=null && listaModalitaPagamento.size()>0){
					for(int j=0; j<listaModalitaPagamento.size(); j++){				
						if(listaModalitaPagamento.get(j).getAssociatoA().equals(sedeSelezionata.getDenominazione())){
							first = first + 1;
							relistaModalitaPagamento.add(listaModalitaPagamento.get(j));
							if(first==1){
								model.setRadioModPagSelezionato(listaModalitaPagamento.get(j).getUid());
							}
						}
					}
				}
				if(relistaModalitaPagamento.size()>0){
					model.setListaModalitaPagamentoSoggetto(relistaModalitaPagamento);
					model.setRadioModPagSelezionato(uidModpag);
				}else{
					model.setListaModalitaPagamentoSoggetto(null);
				}
			}
		}else{
			model.setListaModalitaPagamentoSoggetto(model.getListaModalitaPagamentoSoggettoOrigine());
			model.setRadioModPagSelezionato(uidModpag);
		}
		
		// TODO: verificare: se non esiste nella lista modalita pagamento la mdp con uid == radioModPagSelezionato => radioModPagSelezionato = 0
		
 		return MODPAGAMENTO;
	}
	
	//aggiorna la tabella sedi settando (check) la sede collegata alla modalita' pagamento scelta
	public String resede(){
		//inserire la logica di set della sede associata..
		HttpServletRequest request = ServletActionContext.getRequest();
		String cod = request.getParameter("selection");
		List<ModalitaPagamentoSoggetto> listaModalitaPagamento = model.getListaModalitaPagamentoSoggettoOrigine();
		ModalitaPagamentoSoggetto modpagamentoSelezionato = null; 
		//cerco la modalita di pagamento:
		for(int j=0; j<listaModalitaPagamento.size(); j++){
			if(Integer.parseInt(cod) == listaModalitaPagamento.get(j).getUid()){
				//trovata
				modpagamentoSelezionato = listaModalitaPagamento.get(j);
				model.setModpagamentoSelezionata(listaModalitaPagamento.get(j));
			}
		}
		List<SedeSecondariaSoggetto>  listaSediSecondarie = model.getListaSediSecondarieSoggetto();
		if(listaSediSecondarie!=null && !listaSediSecondarie.isEmpty()){
			
			//Cerco la sede associata:
			boolean corrispondenzaSede = false;
			for(int i = 0; i<listaSediSecondarie.size(); i++){
				if(modpagamentoSelezionato.getAssociatoA().equals(listaSediSecondarie.get(i).getDenominazione())){
					//trovata
					model.setRadioSediSecondarieSoggettoSelezionato(listaSediSecondarie.get(i).getUid());
					corrispondenzaSede = true;
					break;
				}
			}
		
			if(!corrispondenzaSede){
				model.setRadioSediSecondarieSoggettoSelezionato(0);
			}
		}
		return SEDISECONDARIE;
	}
	
	//cambiamdo codice creditore in step1 intercetta il cambio e fa il refresh della tabella modpagamento
	public String modpagamento(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String cod = request.getParameter("id");
		//carico le liste a partire dal cod letto:
		caricaListeCreditore(cod);
 		return MODPAGAMENTO;
	}
	
	//cambiamdo codice creditore in step1 intercetta il cambio e fa il refresh della tabella sedi secondarie
	public String sedisecondarie(){
		return SEDISECONDARIE;
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
		teSupport.setRicaricaAlberoPianoDeiConti(false);
		// pulisce gli alberi in caso di cambiamento del pdc
		// CR-2023 eliminato contoEconomico
		teSupport.setSiopeSpesa(new SiopeSpesa());
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
	
	@Override
	public String listaClasseSoggettoDueChanged() {
		// Auto-generated method stub
		return null;
	}

	@Override
	protected void setSoggettoSelezionatoDue(SoggettoImpegnoModel soggettoImpegnoModel) {
		// Auto-generated method stub
	}
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaContoVincolatoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaContoVincolatoCapitoloResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Filter;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ImportiCapitoloModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.OilService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AccreditoTipoOilIsPagoPA;
import it.csi.siac.siacfinser.frontend.webservice.msg.AccreditoTipoOilIsPagoPAResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.CommissioneDocumento;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.TipoAssociazioneEmissione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ric.RicercaLiquidazioneK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto.TipoAccredito;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiSottoContiVincolatiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiSottoContiVincolatiCapitoloResponse;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestioneOrdinativoPagamentoStep1Action extends ActionKeyGestioneOrdinativoPagamentoAction{

	private static final long serialVersionUID = 1L;
	
	private String numeroOrdinativo;
	private String annoOrdinativo;
	
	private String numeroOrdinativoDaPassare;
	private String annoOrdinativoDaPassare;
	
	
	public Integer idOrdinativo;
	public String doveMiTrovo;
	
	//SIAC-8853
	@Autowired 
	private transient OilService oilService;
	
	//SIAC-8850
	@Autowired
	private CapitoloService capitoloService;
	
	public GestioneOrdinativoPagamentoStep1Action () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO;
	}

	/**
	 * metodo prepare della action
	 */
	@Override
	public void prepare() throws Exception {
		
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		this.model.setTitolo("Gestione Ordinativo Pagamento");
		if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
			model.getGestioneOrdinativoStep1Model().getCapitolo().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
			model.getCapitoloRicerca().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
		}
		
		//carico le liste ricerca soggetto:
		caricaListePerRicercaSoggetto();
		
		//lista tipi provvedimento:
		if(model.getListaTipiProvvedimenti()==null || model.getListaTipiProvvedimenti().size()==0){
			caricaTipiProvvedimenti();
		}
		
		//liste combo:
		caricaListeCombo();
		
		// SIAC-5933 - SIAC-6029 (va chiamato appena dopo liste combo):
		impostaDefaultContoTesoriere(sonoInAggiornamento());

		//transazione elementare:
		if(teSupport==null){
			pulisciTransazioneElementare();
		}
		
		if(!model.getGestioneOrdinativoStep1Model().getOrdinativo().isFlagAllegatoCartaceo()){
			model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagAllegatoCartaceo(false);
		}
		
		if(!model.getGestioneOrdinativoStep1Model().getOrdinativo().isFlagBeneficiMultiplo()){
			model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagBeneficiMultiplo(false);
		}
		
		if(!model.getGestioneOrdinativoStep1Model().getOrdinativo().isFlagCopertura()){
			model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagCopertura(false);
		}
		
		// non agisce l'ancora dei vincoli 
		model.getGestioneOrdinativoStep1Model().setPortaAdAltezzaOrdinativiCollegati(false);

		
		/* SIAC-7399
		 * Setting della variabile per invalidare
		 * il controllo sul blocco ragioneria
		 */
		model.setSkipControlloBloccoRagioneria(true);
		
	}
	
	/**
	 * metodo execute della action
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		//PRIMA COSA DA FARE, SET SE SONO IN AGGIORNAMENTO:
		//Vado a settare la variabile per vedere se sono in inserimento o aggiornamento
		if(!model.isSonoInAggiornamento()){
			if(getAnnoOrdinativo()!= null && getNumeroOrdinativo()!=null){
				model.setAnnoOrdinativoInAggiornamento(getAnnoOrdinativo());
				model.setNumeroOrdinativoInAggiornamento(getNumeroOrdinativo());
				model.setSonoInAggiornamento(true);
			}else{
				model.setSonoInAggiornamento(false);
			}
		}
		//END SET SONO IN AGGIORNAMENTO O MENO
		
		//pulisco i flag per i pop up di conferma:
		model.getGestioneOrdinativoStep1Model().setCheckWarningDaCollegareQuietanziato(false);
		//task-218
		model.setAnnoOrdinativoInAggiornamento(getAnnoOrdinativo());
		model.setNumeroOrdinativoInAggiornamento(getNumeroOrdinativo());
		//
		
		// verifico lo stato di bilancio
		// nel caso genero errore non appena si atterra sulla pagina
		controlloStatoBilancio(sessionHandler.getAnnoBilancio(), "INSERIMENTO", "ORDINATIVO");
		
		//Controlliamo l'abilitazione:
		if(sonoInAggiornamento()){
			//aggiornamento
			if(!isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_aggMan)){
				//non e' abilitato
				addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
			}
		} else {
		 	// SIAC-5957 non controllava in inserimento
			if(!isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_insMan)){
				//non e' abilitato
				addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
			}
		}
		//fine controllo abilitazione
			
		boolean inserimentoOrdinativo = true;
		if (idOrdinativo != null && idOrdinativo != 0) {
			inserimentoOrdinativo = false;
		}
		
		//transazione elementare:
		if(teSupport==null){
			pulisciTransazioneElementare();
		}
		
		//setto il tipo di oggetto trattato:
		teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO.toString());
		
		//carico le labels:
		caricaLabelsInserisci(1, inserimentoOrdinativo);
		
		//liste bil:
		if (caricaListeBil(WebAppConstants.CAP_UG)) {
			return INPUT;
		}
		
		if(model.isSonoInAggiornamento()){
			//CARICA DATI QUANDO ENTRO IN AGGIORNAMENTO
			if(model.isForceReloadAgiornamentoOrdinativo()){
				List<ImportiCapitoloModel> listaVuota = new ArrayList<ImportiCapitoloModel>();
				model.getGestioneOrdinativoStep1Model().getCapitolo().setImportiCapitolo(listaVuota);
				caricaDatiOrdinativo();
			}
			
			
		}else{
			//task-162
			impostaDefaultCommissioni();
			
		}
		
		//ORDINATIVO_TIPO_PAGAMENTO
		// Jira - 1357 in caso di errore di caricamento dei dati
		// dei classificatori non viene segnalato alcun errore
		// ma carica la pagina, al massimo non verranno visualizzate le combo relative
		caricaListeFinOrdinativo(TIPO_ORDINATIVO_PAGAMENTO_P);
		
		//OPERAZIONI PER TENERE AGGIORNATE LE VARIABILI DELLA GESTIONE DEL PROVVEDIMENTO AMMINITRATIVO:
		reimpostaCodiceStrutturaPadre(model.getGestioneOrdinativoStep1Model().getProvvedimento());
		reimpostaCodiceStrutturaSelezionata(model.getGestioneOrdinativoStep1Model().getProvvedimento());
		//////////////////////////////////////////////////////////////////////////////////////////
		
		//disabilito il caricamento degli alberi inutili per questo scenario (in AjaxAction.java):
		teSupport.setRicaricaAlberoPianoDeiConti(false);
		teSupport.setRicaricaStrutturaAmministrativa(true);//questo serve nelle ricerca guidate
		teSupport.setRicaricaSiopeSpesa(false);
		
		//SIAC-6138
		model.setChiediConfermaCollegamentoOrdinativo(false);
		model.setSaltaControlloSoggetto(false);
		return SUCCESS;
	}	
	
	//task-161
	private void impostaDefaultCommissioni() {
		//task-162
		//verifico se sono regp, in caso positivo presetto la commissione a esente
		if(!abilitaCampoDefaultCommissioniInserimentoOrdinativiPagamento()) {
			//presetto la commissione a beneficiario BN 
			if(model.getGestioneOrdinativoStep1Model().getListaCommissioni().size()>1){
				for(CodificaFin lista : model.getGestioneOrdinativoStep1Model().getListaCommissioni()){
					if(lista.getCodice().equalsIgnoreCase("BN")){
						model.getGestioneOrdinativoStep1Model().getOrdinativo().setCommissioneDocumento(new CommissioneDocumento());
						model.getGestioneOrdinativoStep1Model().getOrdinativo().getCommissioneDocumento().setCodice(BENEFICIARIO);
						break;
					} 
				}
			}
		}else {
			//presetto la commissione a esente ES
			if(model.getGestioneOrdinativoStep1Model().getListaCommissioni().size()>1){
				for(CodificaFin lista : model.getGestioneOrdinativoStep1Model().getListaCommissioni()){
					if(lista.getCodice().equalsIgnoreCase("ES")){
						model.getGestioneOrdinativoStep1Model().getOrdinativo().setCommissioneDocumento(new CommissioneDocumento());
						model.getGestioneOrdinativoStep1Model().getOrdinativo().getCommissioneDocumento().setCodice(ESENTE);
						break;
					}
				}
			}
		}
	}
	
    //SIAC-7159  //SIAC-8853
    public boolean modPagSelectedIsPagoPa(int uidModPag) {
    	ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = 
    			CollectionUtil.findFirst(model.getGestioneOrdinativoStep1Model().getListaModalitaPagamentoVisualizza(), new Filter<ModalitaPagamentoSoggetto>() {
    				@Override
    				public boolean isAcceptable(ModalitaPagamentoSoggetto source) {
    					return source.getUid() == uidModPag;
    				}
    			});

    	AccreditoTipoOilIsPagoPA req = model.creaRequestAccreditoTipoOilIsPagoPA();
    	logServiceRequest(req);
    	req.setCodiceAccreditoTipo(modalitaPagamentoSoggetto.getModalitaAccreditoSoggetto().getCodice());
    	AccreditoTipoOilIsPagoPAResponse res = oilService.accreditoTipoOilIsPagoPA(req);
    	logServiceResponse(res);

    	return res.isAccreditoTipoOilIsPagoPA();
    }
	
    
	public String prosegui(){
		
		
		
		//pulisco i flag per i pop up di conferma:
		model.getGestioneOrdinativoStep1Model().setCheckWarningDaCollegareQuietanziato(false);
		//
		
		//verifico l'abilitazione rispetto 
		//al fatto di essere in aggiornamento o inserimento:
		if(sonoInAggiornamento()){
			//aggiornamento
			if(!isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_aggMan)){
				addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
				return INPUT;
			}
		}else{
			//inserimento
			if(!isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_insMan)){
				addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
				return INPUT;
			}
		}
		
		
		// valorizzazione CHekbox
		model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagAllegatoCartaceo(valorizzaCheckbox("gestioneOrdinativoStep1Model.ordinativo.flagAllegatoCartaceo"));
		model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagBeneficiMultiplo(valorizzaCheckbox("gestioneOrdinativoStep1Model.ordinativo.flagBeneficiMultiplo"));
		model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagCopertura(valorizzaCheckbox("gestioneOrdinativoStep1Model.ordinativo.flagCopertura"));
		model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagDaTrasmettere(valorizzaCheckbox("gestioneOrdinativoStep1Model.ordinativo.flagDaTrasmettere"));

		//controllo stato del bilancio:
		if(controlloStatoBilancio(sessionHandler.getAnnoBilancio(),doveMiTrovo,"Ordinativo")){
			return INPUT;
		}
		
		List<Errore> listaErrori= new ArrayList<Errore>();
		// CR 1912, INSERIMENTO_ORDINATIVO PAGAMENTO --> 
		// se l'utente sceglie di eseguire una ricerca liquidazione per chiave nn vanno fatti i controlli su capitolo, soggetto creditore e provvedimento
		if(model.getGestioneOrdinativoStep1Model().getCapitolo().getAnno() == null || "".equals(model.getGestioneOrdinativoStep1Model().getCapitolo())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Capitolo"));
		}
		if(model.getGestioneOrdinativoStep1Model().getCapitolo().getNumCapitolo()== null || "".equals(model.getGestioneOrdinativoStep1Model().getCapitolo().getNumCapitolo())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Capitolo"));
		}
		if(model.getGestioneOrdinativoStep1Model().getCapitolo().getArticolo() == null || "".equals(model.getGestioneOrdinativoStep1Model().getCapitolo().getArticolo())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Articolo Capitolo"));
		}
		if(model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore() == null || "".equals(model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice Creditore"));
		}
		
		if(model.getGestioneOrdinativoStep1Model().getProvvedimento().getIdTipoProvvedimento() == null || "".equals(model.getGestioneOrdinativoStep1Model().getProvvedimento().getIdTipoProvvedimento())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Provvedimento"));
		}
		
		if(model.getGestioneOrdinativoStep1Model().getProvvedimento().getAnnoProvvedimento() == null || "".equals(model.getGestioneOrdinativoStep1Model().getProvvedimento().getAnnoProvvedimento())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Provvedimento"));
		}else if(model.getGestioneOrdinativoStep1Model().getProvvedimento().getAnnoProvvedimento()<=1900){
			listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Anno provvedimento"," > 1900"));
		}
		
		if(model.getGestioneOrdinativoStep1Model().getProvvedimento().getNumeroProvvedimento() == null || "".equals(model.getGestioneOrdinativoStep1Model().getProvvedimento().getNumeroProvvedimento())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Provvedimento"));
			
		}else if(model.getGestioneOrdinativoStep1Model().getProvvedimento().getNumeroProvvedimento().intValue()<=0){
			listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Numero provvedimento"," > 0"));
		}
		
		if(model.getGestioneOrdinativoStep1Model().getAnnoLiquidazione()!= null && model.getGestioneOrdinativoStep1Model().getNumeroLiquidazione() == null){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Liquidazione"));
		}else if(model.getGestioneOrdinativoStep1Model().getAnnoLiquidazione()== null && model.getGestioneOrdinativoStep1Model().getNumeroLiquidazione()!= null){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Liquidazione"));

		}
		
		// seleziono soggetto e non radio button su modalita pagamento
		if(model.getGestioneOrdinativoStep1Model().isSoggettoSelezionato() &&  model.getGestioneOrdinativoStep1Model().getRadioModPagSelezionato()==0){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Modalita' pagamento"));
		}else{
			if(model.getGestioneOrdinativoStep1Model().getRadioModPagSelezionato()!=0){
				for(ModalitaPagamentoSoggetto modPag : model.getGestioneOrdinativoStep1Model().getListaModalitaPagamentoVisualizza()){
					if(modPag.getUid()==model.getGestioneOrdinativoStep1Model().getRadioModPagSelezionato()){
						if(modPag.getTipoAccredito()!= null){
							if(modPag.getTipoAccredito().equals(TipoAccredito.CSC)){
								listaErrori.add(ErroreFin.MOD_PAGAMENTO_STATO_CESSIONE.getErrore(""));
							}
						}
					}
				}
			}else{
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Modalita' pagamento"));

			}
		}
		
		//SIAC-7159
		//si invalida la Modalita Pagamento PagoPA
		//SIAC-8853
		//task-48
		if(model.getGestioneOrdinativoStep1Model().getRadioModPagSelezionato()!=0) {
			if(modPagSelectedIsPagoPa(model.getGestioneOrdinativoStep1Model().getModpagamentoSelezionata().getUid())){
				addErrore(it.csi.siac.siacfin2ser.model.errore.ErroreFin.MOD_PAGO_PA_NON_AMMESSA.getErrore(""));
				return INPUT;
			}
		}
		

		// SIAC-6102
		//CONTROLLI SU DESCRIZIONE:
		controlloDescrizioneProseguiStep1(listaErrori);
		
		//codice bollo
		if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getCodiceBollo().getCodice() == null || "".equals(model.getGestioneOrdinativoStep1Model().getOrdinativo().getCodiceBollo().getCodice())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Bollo"));
		}
		
		//commissione documento
		if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getCommissioneDocumento().getCodice() == null || "".equals(model.getGestioneOrdinativoStep1Model().getOrdinativo().getCommissioneDocumento().getCodice())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Commissione"));
		}
		
		//conto tesoreria
		if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getContoTesoreria().getCodice() == null || "".equals(model.getGestioneOrdinativoStep1Model().getOrdinativo().getContoTesoreria().getCodice())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Conto Tesoriere"));
		}
		
		//codice
		if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getDistinta().getCodice() == null || "".equals(model.getGestioneOrdinativoStep1Model().getOrdinativo().getDistinta().getCodice())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Distinta"));
		}
		
		String ritorno = ""; 
		if(listaErrori.isEmpty()){
			
			//fino a qui non ci sono errori, proseguiamo con ulteriori controlli:
			ritorno = controlloServiziProseguiOrdinativo(oggettoDaPopolare);
			
			// carica liquidazioni puntuale o per ricerca
			
			// sono in inserimento
			if(!sonoInAggiornamento()){
			
				// se non arrivo dalla ricerca liquidazione per chiave carico la liquidazione in base ai parametri inseriti
				if(model.getGestioneOrdinativoStep2Model().getRadioIdLiquidazione()==null){
					caricaLiquidazioniOrdinativo();
				}
				
			}
			
			if(hasActionErrors() || hasActionWarnings()){
				return INPUT;
			}
						
			
		}else{
			//presenza errori
			addErrori(listaErrori);
			return INPUT;
		}
		
		debug("prosegui", "ritorno = "+ritorno);
		
		return ritorno;
		
	}
	
	//SIAC-8892
	public void caricaStanziamento(CapitoloUscitaGestione capUG) {
		CapitoloImpegnoModel capitoloModel = new CapitoloImpegnoModel();
		capitoloModel.setImportiCapitolo(new ArrayList<ImportiCapitoloModel>());
		capitoloModel.setImportiCapitoloUG(capUG.getListaImportiCapitolo());
		capitoloModel.setAnno(capUG.getAnnoCapitolo());
		ImportiCapitoloModel supportImporti;
		for (ImportiCapitoloUG currentImporto : capUG.getListaImportiCapitolo()) {
			supportImporti = new ImportiCapitoloModel();
			supportImporti.setAnnoCompetenza(currentImporto.getAnnoCompetenza());
			supportImporti.setCassa(currentImporto.getStanziamentoCassa());
			supportImporti.setResiduo(currentImporto.getStanziamentoResiduo());
			supportImporti.setCompetenza(currentImporto.getStanziamento());
			// RM 08042015, commentato perch non usato, verifica pulizia functione e attibuti entit non piu usate
			
			capitoloModel.getImportiCapitolo().add(supportImporti);
		}
		
		model.getDatoPerVisualizza().setImportiCapitolo(capitoloModel.getImportiCapitolo());
		model.getGestioneOrdinativoStep1Model().getCapitolo().setImportiCapitoloUG(capitoloModel.getImportiCapitoloUG());
	}
	
	//CR 1912
	public String cercaLiquidazione(){
		
		List<Errore> listaErrori= new ArrayList<Errore>();
		 
		// CR 1912, INSERIMENTO_ORDINATIVO PAGAMENTO --> 
		// RM : se l'utente sceglie di eseguire una ricerca liquidazione per chiave
		//non vanno fatti i controlli su capitolo, soggetto creditore e provvedimento
		if(!model.isSonoInAggiornamento()){
				
				//controllo che anno e numero siano presenti:
				if (model.getGestioneOrdinativoStep1Model().getNumeroLiquidazione()== null){
					//numero omesso
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Liquidazione"));
				}
					
				if (model.getGestioneOrdinativoStep1Model().getAnnoLiquidazione()== null){
					//anno omesso
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Liquidazione"));
				}
				
				if(!isEmpty(listaErrori)){
					//almeno uno tra anno e numero e' null
					model.setLiquidazioneTrovata(Boolean.FALSE);
					addErrori(listaErrori);
					return INPUT;
				}

				//preparo la request per chiamare il servizio di ricerca liquidazione per chiave:
				RicercaLiquidazionePerChiave req = new RicercaLiquidazionePerChiave();
				RicercaLiquidazioneK ricercaLiquidazioneK = new RicercaLiquidazioneK(); 
				Liquidazione liq = new Liquidazione();

				liq.setAnnoLiquidazione(model.getGestioneOrdinativoStep1Model().getAnnoLiquidazione());
				liq.setNumeroLiquidazione(new BigDecimal(model.getGestioneOrdinativoStep1Model().getNumeroLiquidazione()));
				ricercaLiquidazioneK.setLiquidazione(liq);
				ricercaLiquidazioneK.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
				ricercaLiquidazioneK.setBilancio(sessionHandler.getBilancio());
				ricercaLiquidazioneK.setTipoRicerca(CostantiFin.TIPO_RICERCA_DA_ORDINATIVO);
				req.setEnte(sessionHandler.getAccount().getEnte());
				req.setRichiedente(sessionHandler.getRichiedente());
				req.setDataOra(new Date());
				req.setpRicercaLiquidazioneK(ricercaLiquidazioneK);
				
				//richiamo il servizio:
				RicercaLiquidazionePerChiaveResponse response = liquidazioneService.ricercaLiquidazionePerChiave(req);
					
				//analizzo la risposta del servizio:
				if(response != null && response.isFallimento() && response.getErrori()!=null ){
					//ci sono errori:
					addErrori(response);
					return INPUT;
				}
					
				if(response != null && response.getLiquidazione() != null){

					Liquidazione liquidazione = response.getLiquidazione();
					
					// se la liquidazione va bene per essere visualizzata allora carico i dati di:
					// capitolo, porvvedimento, soggetto, modalita di pagamento
					if (liquidazione.getStatoOperativoLiquidazione().equals(StatoOperativoLiquidazione.VALIDO)) {
						
						if (liquidazione.getDisponibilitaPagare().compareTo(BigDecimal.ZERO) > 0) {
							
							//verifiche siope plus:
							boolean tuttoOkSiopePlus = verificaDatiSiopePlusLiqSelezionata(liquidazione,false,true);
							if(!tuttoOkSiopePlus){
								return INPUT;
							}
							//
						
							//FIX JIRA 3803
							model.getGestioneOrdinativoStep1Model().getOrdinativo().setDescrizione(liquidazione.getDescrizioneLiquidazione());
							
							// 1 - setto il capitolo
							CapitoloUscitaGestione capUG = liquidazione.getCapitoloUscitaGestione();
							model.getGestioneOrdinativoStep1Model().getCapitolo().setAnno(capUG.getAnnoCapitolo());
							model.getGestioneOrdinativoStep1Model().getCapitolo().setArticolo(capUG.getNumeroArticolo());
							model.getGestioneOrdinativoStep1Model().getCapitolo().setNumCapitolo(capUG.getNumeroCapitolo());
							BigInteger bigIntUeb = new BigInteger(Integer.toString(capUG.getNumeroUEB()));
							model.getGestioneOrdinativoStep1Model().getCapitolo().setUeb(bigIntUeb);
							model.getGestioneOrdinativoStep1Model().getCapitolo().setDescrizione(capUG.getDescrizione());
							model.getGestioneOrdinativoStep1Model().getCapitolo().setCodiceStrutturaAmministrativa(capUG.getStrutturaAmministrativoContabile().getCodice());
							model.getGestioneOrdinativoStep1Model().getCapitolo().setDescrizioneStrutturaAmministrativa(capUG.getStrutturaAmministrativoContabile().getDescrizione());
							//SIAC-8892
							if(capUG.getTipoFinanziamento()!=null) {
								model.getGestioneOrdinativoStep1Model().getCapitolo().setTipoFinanziamento(capUG.getTipoFinanziamento().getDescrizione());	
							}
							model.getGestioneOrdinativoStep1Model().getCapitolo().setDescrizionePdcFinanziario(capUG.getElementoPianoDeiConti().getCodice()+" - "+capUG.getElementoPianoDeiConti().getDescrizione());
							caricaStanziamento(capUG);
							
							model.getGestioneOrdinativoStep1Model().setCapitoloSelezionato(Boolean.TRUE);
							
							
							
							// 2 - setto il provevdimento
							model.getGestioneOrdinativoStep1Model().setProvvedimento(new ProvvedimentoImpegnoModel());
							AttoAmministrativo provvedimento = liquidazione.getAttoAmministrativoLiquidazione(); 
							model.getGestioneOrdinativoStep1Model().getProvvedimento().setUid(provvedimento.getUid());
							model.getGestioneOrdinativoStep1Model().getProvvedimento().setAnnoProvvedimento(provvedimento.getAnno());
							model.getGestioneOrdinativoStep1Model().getProvvedimento().setNumeroProvvedimento(new BigInteger(Integer.toString(provvedimento.getNumero())));
							model.getGestioneOrdinativoStep1Model().getProvvedimento().setIdTipoProvvedimento(provvedimento.getTipoAtto().getUid());
							model.getGestioneOrdinativoStep1Model().getProvvedimento().setTipoProvvedimento(provvedimento.getTipoAtto().getDescrizione());
							model.getGestioneOrdinativoStep1Model().getProvvedimento().setCodiceTipoProvvedimento(provvedimento.getTipoAtto().getCodice());
							model.getGestioneOrdinativoStep1Model().getProvvedimento().setOggetto((provvedimento.getOggetto()!=null ?provvedimento.getOggetto(): "" ));
							model.getGestioneOrdinativoStep1Model().setProvvedimentoSelezionato(Boolean.TRUE);
							//SIAC-7523
							sessionHandler.setParametro(FinSessionParameter.PROVVEDIMENTO_SELEZIONATO, Boolean.TRUE);
							//
							model.getGestioneOrdinativoStep1Model().getProvvedimento().setStato(provvedimento.getStatoOperativo());
							
							if(provvedimento.getStrutturaAmmContabile()!=null){
								// setto livello e uid che mi serviranno dopo
								model.getGestioneOrdinativoStep1Model().getProvvedimento().setCodiceStrutturaAmministrativa(provvedimento.getStrutturaAmmContabile().getCodice());
								model.getGestioneOrdinativoStep1Model().getProvvedimento().setStrutturaAmministrativa(provvedimento.getStrutturaAmmContabile().getDescrizione());
								model.getGestioneOrdinativoStep1Model().getProvvedimento().setLivello(provvedimento.getStrutturaAmmContabile().getLivello());
								model.getGestioneOrdinativoStep1Model().getProvvedimento().setUidStrutturaAmm(provvedimento.getStrutturaAmmContabile().getUid());
								
							}
							
							//3 - setto i dati della distinta e conto tesoreria
							
							if(liquidazione.getDistinta()!= null){							
								model.getGestioneOrdinativoStep1Model().getOrdinativo().getDistinta().setCodice(liquidazione.getDistinta().getCodice());							
							}
							
							/*if(liquidazione.getContoTesoreria()!=null){
								model.getGestioneOrdinativoStep1Model().getOrdinativo().getContoTesoreria().setCodice(liquidazione.getContoTesoreria().getCodice());
							}else {
								//SIAC-8850
								setContoTesoreriaModel(capUG.getUid());
							}*/
							
							//SIAC-8850
							if("INPUT".equals(setContoTesoreriaModel(capUG.getUid()))){
								if(liquidazione.getContoTesoreria()!=null) {
									model.getGestioneOrdinativoStep1Model().getOrdinativo().getContoTesoreria().setCodice(liquidazione.getContoTesoreria().getCodice());
								}
							}else {
								if("0000100".equals(model.getGestioneOrdinativoStep1Model().getOrdinativo().getContoTesoreria().getCodice())) {
									model.getGestioneOrdinativoStep1Model().getOrdinativo().getContoTesoreria().setCodice(liquidazione.getContoTesoreria().getCodice());
								}
							}
							
							// carico prima il soggetto, sul soggetto poi ricerco le sedi secondarie e preseleziono 
							model.getGestioneOrdinativoStep1Model().setSoggetto(new SoggettoImpegnoModel());
							Soggetto soggetto = liquidazione.getSoggettoLiquidazione();
							model.getGestioneOrdinativoStep1Model().getSoggetto().setCodCreditore(soggetto.getCodiceSoggetto());
							model.getGestioneOrdinativoStep1Model().getSoggetto().setDenominazione(soggetto.getDenominazione());
							model.getGestioneOrdinativoStep1Model().getSoggetto().setCodfisc(soggetto.getCodiceFiscale());
							model.getGestioneOrdinativoStep1Model().getSoggetto().setUid(soggetto.getUid());
							model.getGestioneOrdinativoStep1Model().setSoggettoSelezionato(Boolean.TRUE);
							
							if(soggetto.getSediSecondarie()!=null){
								model.getGestioneOrdinativoStep1Model().setListaSediSecondarie(soggetto.getSediSecondarie());
								
								// 2 - setto la sede secondaria
								if(liquidazione.getSedeSecondariaSoggetto()!=null){
									model.getGestioneOrdinativoStep1Model().setRadioSediSecondarieSoggettoSelezionato(liquidazione.getSedeSecondariaSoggetto());
								}
							
							
								// usera' il radioliquidazione selezionato per controllare se rifare alcuni controlli sul prosegui
								model.getGestioneOrdinativoStep2Model().setRadioIdLiquidazione(String.valueOf(liquidazione.getIdLiquidazione()));
								List<Liquidazione> liquidazioneStep2List = new ArrayList<Liquidazione>();
								liquidazioneStep2List.add(liquidazione);
								model.getGestioneOrdinativoStep2Model().setListaLiquidazioni(liquidazioneStep2List);
								model.getGestioneOrdinativoStep2Model().setImportoQuotaFormattato(convertiBigDecimalToImporto(liquidazione.getDisponibilitaPagare()));
								
								// lancio la routine per caricare i dati della TE 
								//a partire dalla liquidazione preselezionata appena sopra
								ricaricaTEByIdLiquidazione();
								
								model.setLiquidazioneTrovata(Boolean.TRUE);
							
							}
							
							//SIOPE PLUS
							impostaDatiSiopePlusNelModel(liquidazione, model.getGestioneOrdinativoStep2Model());
							//
							
							//setto la lista modalita pagamento per visualizza.
							if(soggetto.getModalitaPagamentoList()!=null && soggetto.getModalitaPagamentoList().size()>0){
								model.getGestioneOrdinativoStep1Model().setListaModalitaPagamentoVisualizza(soggetto.getModalitaPagamentoList());
							}
							
							//mod paga selezionata:
							if(liquidazione.getModalitaPagamentoSoggetto()!=null){
								model.getGestioneOrdinativoStep1Model().setRadioModPagSelezionato(liquidazione.getModalitaPagamentoSoggetto().getUid());
								model.getGestioneOrdinativoStep1Model().setModpagamentoSelezionata(liquidazione.getModalitaPagamentoSoggetto());
							}
							
							//Controllo scadenza modalita pagamento:
							if(!FinUtility.isMDPNonScaduta(liquidazione.getModalitaPagamentoSoggetto())){
								addErrore(ErroreFin.MOD_PAGAMENTO_SCADUTA.getErrore());
								return INPUT;
							}
							
						}else{
							//disponibilita liquidazione insufficiente
							addErrore(ErroreFin.DISPONIBILITA_LIQUIDAZIONE_INSUFFICIENTE.getErrore());
							return INPUT;
						}
						
					}else{
						//Stato liquidazione non accettabile
						addErrore(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("la liquidazione " + liquidazione.getAnnoLiquidazione() + "/" + liquidazione.getNumeroLiquidazione(), "stato = " + liquidazione.getStatoOperativoLiquidazione().name()));
						return INPUT;
					}
			}else {
				//liquidazione inesistente
				addErrore(ErroreFin.LIQUIDAZIONE_INESISTENTE.getErrore());
				return INPUT;
			}
		}
		
		return SUCCESS;
	}
	
	//SIAC-8850
	protected String setContoTesoreriaModel(Integer capitoloUid) {
		impostaDefaultContoTesoriereVar(sonoInAggiornamento());
		
		LeggiSottoContiVincolatiCapitolo req = new LeggiSottoContiVincolatiCapitolo();
		CapitoloUscitaGestione capitoloUscitaTrovato = new CapitoloUscitaGestione();
		capitoloUscitaTrovato.setUid(capitoloUid);
		req.setCapitoloUscitaGestione(capitoloUscitaTrovato);
		req.setRichiedente(model.getRichiedente());
		LeggiSottoContiVincolatiCapitoloResponse response = capitoloService.leggiSottoContiVincolatiCapitoloService(req);
		if (response.isFallimento()) {
			addErrori(response);
			return INPUT;
		}
		if(response.getContoTesoreria()!=null) {
			model.getGestioneOrdinativoStep1Model().getOrdinativo().setContoTesoreria(response.getContoTesoreria());	
		}
		return SUCCESS;
	}
	
	//Non penso sia completa
	private String controlloServiziProseguiOrdinativo(OggettoDaPopolareEnum oggettoDaPopolare){
		boolean erroriTrovatiNeiServizi = false;
		List<Errore> listaErrori = new ArrayList<Errore>();
		if (!eseguiRicercaCapitolo(model.getGestioneOrdinativoStep1Model().getCapitolo(), false, oggettoDaPopolare)) {
			erroriTrovatiNeiServizi = true;
		}
		if (!model.getGestioneOrdinativoStep1Model().isSoggettoSelezionato() && model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore() != null && !"".equals(model.getGestioneOrdinativoStep1Model().getSoggetto().getCodCreditore())) {
			if (!eseguiRicercaSoggetto(convertiModelPerChiamataServizioRicerca(model.getGestioneOrdinativoStep1Model().getSoggetto()), false, oggettoDaPopolare)) {
				erroriTrovatiNeiServizi = true;
			}else{
				
				// carico sedi e modalita pagamento
				if(model.getGestioneOrdinativoStep1Model().getSoggetto()!=null){
					
					ricercaSoggettoPerChiaveOrdinativo();
					
					if(model.getGestioneOrdinativoStep1Model().getRadioModPagSelezionato()==0){
						listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Modalita' pagamento"));
						addErrori(listaErrori);
						erroriTrovatiNeiServizi = true;
					}
				}
			}
		}
		
		if (!erroriTrovatiNeiServizi && model.getGestioneOrdinativoStep1Model().getListaModalitaPagamento() != null && model.getGestioneOrdinativoStep1Model().getListaModalitaPagamento().size() > 0) {
			for (ModalitaPagamentoSoggetto currentModPag : model.getGestioneOrdinativoStep1Model().getListaModalitaPagamento()) {
				if (model.getGestioneOrdinativoStep1Model().getRadioModPagSelezionato() == currentModPag.getUid()) {
					model.getGestioneOrdinativoStep1Model().setModpagamentoSelezionata(currentModPag);
					break;
				}
			}
		}
		
		boolean erroreProvvedimento = controlloServizioProvvedimento(model.getGestioneOrdinativoStep1Model().getProvvedimento(), model.getGestioneOrdinativoStep1Model().isProvvedimentoSelezionato());
		erroriTrovatiNeiServizi = erroriTrovatiNeiServizi || erroreProvvedimento;
		
		return erroriTrovatiNeiServizi ? INPUT : "prosegui";
	}
	
	/**
	 * gestione codice creditore changed
	 * @return
	 */
	public String modpagamento(){
		//cambiando codice creditore in step1 intercetta 
		//il cambio e fa il refresh della tabella modpagamento
		HttpServletRequest request = ServletActionContext.getRequest();
		String cod = request.getParameter("id");
		//carica le liste creditore
		caricaListeCreditore(cod);
 		return MODPAGAMENTO;
	}


	public String sedisecondarie(){
		return SEDISECONDARIE;
	}
	
   /**
    * carica il titolo della mod pag
    * @return
    */
   public String caricaTitoloModPag(){
	   model.getGestioneOrdinativoStep1Model().getModpagamentoSelezionata().getModalitaAccreditoSoggetto().getCodice();
	   model.getGestioneOrdinativoStep1Model().getModpagamentoSelezionata().getModalitaAccreditoSoggetto().getDescrizione();
	   return MODPAGAMENTO;
   }		
   
   /**
    * carica il titolo delle sedi
    * @return
    */
   public String caricaTitoloSedi(){
	   if(model.getGestioneOrdinativoStep1Model().getRadioSediSecondarieSoggettoSelezionato()!=0){
		   model.getGestioneOrdinativoStep1Model().getSedeSelezionata().getDenominazione();
	   }
	   return SEDISECONDARIE;
   }		
		
   /**
    * tasto annulla step1
    * @return
    */
   public String annullaStep1(){
	   
	   //pulisco i flag per i pop up di conferma:
	   model.getGestioneOrdinativoStep1Model().setCheckWarningDaCollegareQuietanziato(false);
 	   //
	   
	   //compportamento diverso se sono in inserimento o aggiornamento:
	   if(model.isSonoInAggiornamento()){
		   model.setAnnoOrdinativoInAggiornamento(model.getGestioneOrdinativoStep1Model().getOrdinativo().getAnno().toString());
		   model.setNumeroOrdinativoInAggiornamento(model.getGestioneOrdinativoStep1Model().getOrdinativo().getNumero().toString());
		   caricaDatiOrdinativo();
		   return SUCCESS;
	   }else{
		  // inserimento
		  model.getGestioneOrdinativoStep1Model().setCapitolo(new CapitoloImpegnoModel()); 
		  model.getGestioneOrdinativoStep1Model().setCapitoloSelezionato(false);
		  
		  model.getGestioneOrdinativoStep1Model().setSoggetto(new SoggettoImpegnoModel());
		  model.getGestioneOrdinativoStep1Model().setSoggettoSelezionato(false);
		  
		  model.getGestioneOrdinativoStep1Model().setProvvedimento(new ProvvedimentoImpegnoModel());
		  model.getGestioneOrdinativoStep1Model().setProvvedimentoSelezionato(false);
		  
		  model.getGestioneOrdinativoStep1Model().getSoggetto().setCodCreditore("");
		  model.getGestioneOrdinativoStep1Model().setListaModalitaPagamentoVisualizza(new ArrayList<ModalitaPagamentoSoggetto>());
		  model.getGestioneOrdinativoStep1Model().setRadioModPagSelezionato(0);
		  model.getGestioneOrdinativoStep1Model().setRadioSediSecondarieSoggettoSelezionato(0);
		  model.getGestioneOrdinativoStep1Model().setListaSediSecondarie(null);
		  model.getGestioneOrdinativoStep1Model().setAnnoLiquidazione(null);
		  model.getGestioneOrdinativoStep1Model().setNumeroLiquidazione(null);
		  model.getGestioneOrdinativoStep1Model().setOrdinativo(new OrdinativoPagamento());
		  model.getGestioneOrdinativoStep1Model().setModpagamentoSelezionata(null);
		 
		  model.setLiquidazioneTrovata(false);
		  
		  // pulisco i classificatori
		  teSupport.setClassGenSelezionato1("");
		  teSupport.setClassGenSelezionato2("");
		  teSupport.setClassGenSelezionato3("");
		  teSupport.setClassGenSelezionato4("");
		  teSupport.setClassGenSelezionato5("");
		  
		  
		  // presetto i valori base
		  if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
				model.getGestioneOrdinativoStep1Model().getCapitolo().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
				model.getCapitoloRicerca().setAnno(new Integer(sessionHandler.getAnnoEsercizio()));
		  }
		  //task-162
		  impostaDefaultCommissioni();
	   }
	   
	   // SIAC-5933 - SIAC-6029
	   impostaDefaultContoTesoriere(sonoInAggiornamento());
	   
	   return SUCCESS;
   }		
		

	@Override
	public String selezionaSoggetto() {
		return selezionaSoggettoOrdinativo();
	}
	
	
	/*
	 * aggiorna la tabella modalita di pagamento su selezione della sede e aggiorna quindi
	 *  la lista sedi associate alla modalita' di pagamento scelta(non-Javadoc)
	 * @see it.csi.siac.siacfinapp.frontend.ui.action.ordinativo.WizardGestioneOrdinativoAction#remodpagamento()
	 */
	@Override
	public String remodpagamento(){
		//rimando la metodo della super classe:
 		return super.remodpagamento();
	}

	
	public boolean disabilitaCheckAggiornamento(){
		if(model.isSonoInAggiornamento()){
			if(isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_aggManQuietanza)){
				model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagCopertura(false);
				return false;
			}else if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo().equals(StatoOperativoOrdinativo.TRASMESSO) && 
						isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_varMan)){
				model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagCopertura(false);
				return false;
			}else if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getStatoOperativoOrdinativo().equals(StatoOperativoOrdinativo.QUIETANZATO) && 
						isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_varMan)){
				model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagCopertura(false);
				return false;
			}
		}
		return true;
	}
	
	public boolean abilitaProvvisoriCassa(){
		// se non e' presente l'operazione OP_SPE_insManQuietanza 
		// puoi fare i provvisori
		return !isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_insManQuietanza);
	}
	
	
	@Override
	public String aggiornaOrdinativo() {
		
		// valorizzazione CHekbox
		model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagAllegatoCartaceo(valorizzaCheckbox("gestioneOrdinativoStep1Model.ordinativo.flagAllegatoCartaceo"));
		model.getGestioneOrdinativoStep1Model().getOrdinativo().setFlagBeneficiMultiplo(valorizzaCheckbox("gestioneOrdinativoStep1Model.ordinativo.flagBeneficiMultiplo"));
		
		
		String returnAggiorna = super.aggiornaOrdinativo();
		if (returnAggiorna.equalsIgnoreCase(RETURN_AGGIORNA)) {
			try {
				execute();
			} catch (Exception e) {}
		}
		return returnAggiorna;
	}
	
	public String aggiungiOrdinativoDaCollegare() throws Exception {
		
		//pulisco i flag per i pop up di conferma:
		model.getGestioneOrdinativoStep1Model().setCheckWarningDaCollegareQuietanziato(false);
		model.getGestioneOrdinativoStep1Model().setOrdIncassoDaConfermare(null);
		//
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		model.getGestioneOrdinativoStep1Model().setApriTabOrdinativiCollegati(true);
		
		OrdinativoPagamento op = model.getGestioneOrdinativoStep1Model().getOrdinativo();
		// controlla dati
		if(model.getGestioneOrdinativoStep1Model().getNumeroOrdinativoDaCollegare() == null){
			
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero ordinativo"));
			
		}else{
			
			// controlli da eseguire:
			// 1- che non sia gia' presente nella lista
			// 2- che la somma dei collegati non superi ll'importo dell'op
			Integer numeroOrdinativoDaCollegare = model.getGestioneOrdinativoStep1Model().getNumeroOrdinativoDaCollegare();
						
			if(op.getElencoOrdinativiCollegati()!=null && !op.getElencoOrdinativiCollegati().isEmpty()){
				
				for (it.csi.siac.siacfinser.model.ordinativo.Ordinativo oi : op.getElencoOrdinativiCollegati()) {
					if(numeroOrdinativoDaCollegare.equals(oi.getNumero())){
						addActionError(ERRORE_ORDINATIVO_COLLEGATO_PRESENTE);
						return INPUT;
						
					}
				}
			}
			
			// se presente recupero l'ordinativo lato ser
			RicercaOrdinativo reqOi = new RicercaOrdinativo();
			
			reqOi.setBilancio(sessionHandler.getBilancio());
			reqOi.setRichiedente(sessionHandler.getRichiedente());
			reqOi.setEnte(sessionHandler.getEnte());
			reqOi.setNumPagina(1);
			reqOi.setNumRisultatiPerPagina(1);
			
			ParametroRicercaOrdinativoIncasso parametri = new ParametroRicercaOrdinativoIncasso();
			parametri.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
			parametri.setNumeroOrdinativoDa( BigInteger.valueOf(numeroOrdinativoDaCollegare));
			
			// SIAC-5833 chiedo tutti gli stati e commento inserito:
			//parametri.setStatoOperativo(CostantiFin.D_ORDINATIVO_STATO_INSERITO);
			//SIAC-6138
//			parametri.setCodiceCreditore(model.getGestioneOrdinativoStep1Model().getOrdinativo().getSoggetto().getCodiceSoggetto());
						
			reqOi.setParametroRicercaOrdinativoIncasso(parametri);
			
			
			RicercaOrdinativoResponse resOi = ordinativoService.ricercaOrdinativoIncasso(reqOi);
			
			if(resOi == null){
				listaErrori.add(ErroreCore.ERRORE_DI_SISTEMA.getErrore("nella ricerca dell'ordinativo di incasso numero: "+ numeroOrdinativoDaCollegare));
				addErrori(listaErrori);
				return INPUT;
			}
			
			OrdinativoIncasso oi = filtraOrdinativoIncasso(resOi.getElencoOrdinativoIncasso(), numeroOrdinativoDaCollegare);
			
			if(resOi!=null && (resOi.isFallimento() || oi == null)){
				listaErrori.add(ErroreCore.ENTITA_NON_TROVATA.getErrore("ordinativo", " numero " +numeroOrdinativoDaCollegare));
				addErrori(listaErrori);
				return INPUT;
			}	
			
			String codStato = oi.getCodStatoOperativoOrdinativo();
			if(CostantiFin.D_ORDINATIVO_STATO_ANNULLATO.equalsIgnoreCase(codStato)){
				listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("ordinativo " + numeroOrdinativoDaCollegare, " perche' in stato ANNULLATO"));
				addErrori(listaErrori);
				return INPUT;
			}
			
			
			// controllo subito se ha dei collegati, se li ha non e' ammesso
			if(oi.isCollegatoAPagamento()){
				//SIAC-8245 si passa l'errore come errore di sistema per permetterne la corretta visualizzazione
				listaErrori.add(ErroreCore.ERRORE_DI_SISTEMA.getErrore(ERRORE_ORDINATIVI_COLLEGATI_SPESA));
				addErrori(listaErrori);
				return INPUT;
			}
			
			// Per il controllo dell'importo devo prima ricercare l'ordinativo indicato
			// se l'importo totale e' ammesso lo aggiungo 
			BigDecimal totaleImportoCollegatiAggiornato = model.getGestioneOrdinativoStep1Model().getTotaleImportoCollegati().add(oi.getImportoOrdinativo());
						
			if(op.getImportoOrdinativo().compareTo(totaleImportoCollegatiAggiornato) < 0){
				model.getGestioneOrdinativoStep1Model().setAbilitatoACollegareOrdinativiNuovi(false);
				//SIAC-8245 si passa l'errore come errore di sistema per permetterne la corretta visualizzazione
				listaErrori.add(ErroreCore.ERRORE_DI_SISTEMA.getErrore(ERRORE_TOTALE_IMPORTO_COLLEGATI));
				addErrori(listaErrori);
			}else{
				
				//A QUESTO PUNTO E' QUASI OK, SOLO NEL CASO FOSSE QUIETANZIATO CHIEDO CONFERMA CON POP UP:
				
				//questo verra' confermato nel metodo confermaAggiungiOrdinativoDaCollegare
				//al quale ci si arriva diretti oppure previa conferma nel pop up
				model.getGestioneOrdinativoStep1Model().setOrdIncassoDaConfermare(oi);
				//
				
				if(!model.isSaltaControlloSoggetto() && oi.getSoggetto().getUid() != model.getGestioneOrdinativoStep1Model().getOrdinativo().getSoggetto().getUid()) {
					model.setChiediConfermaCollegamentoOrdinativo(true);
					return INPUT;
				}
				
				if(CostantiFin.D_ORDINATIVO_STATO_QUIETANZATO.equalsIgnoreCase(codStato)){
					//setto il flag a true:
			    	model.getGestioneOrdinativoStep1Model().setCheckWarningDaCollegareQuietanziato(true);
			    	return INPUT;
				} else {
					return confermaAggiungiOrdinativoDaCollegare();
				}
				
			}
			
		}
		
		if(!listaErrori.isEmpty() || hasActionErrors()){
			// apro il tab vincolo
			addErrori(listaErrori);
			return INPUT;
		}
			
		return SUCCESS;
	}

	/**
	 * @param resOi
	 * @return
	 */
	private OrdinativoIncasso filtraOrdinativoIncasso(List<OrdinativoIncasso> listaOrdinativi, Integer numeroOrdinativoDaCollegare) {
		if(listaOrdinativi == null) {
			return null;
		}		
		for (OrdinativoIncasso ordInc : listaOrdinativi) {
			if(ordInc.getNumero().equals(numeroOrdinativoDaCollegare)) {
				return ordInc;
			}
		}
		return null;
	}
	
	public String confermaAggiungiOrdinativoDaCollegare(){
		
		//pulisco i flag per i pop up di conferma:
		model.getGestioneOrdinativoStep1Model().setCheckWarningDaCollegareQuietanziato(false);
		//
				
		OrdinativoIncasso oi = model.getGestioneOrdinativoStep1Model().getOrdIncassoDaConfermare();
		
		if(oi!=null){
			
			BigDecimal totaleImportoCollegatiAggiornato = model.getGestioneOrdinativoStep1Model().getTotaleImportoCollegati().add(oi.getImportoOrdinativo());
			
			if(model.getGestioneOrdinativoStep1Model().getOrdinativo().getElencoOrdinativiCollegati()==null ){
				model.getGestioneOrdinativoStep1Model().getOrdinativo().setElencoOrdinativiCollegati(new ArrayList<Ordinativo>());
			}
			
			//setto il tipo di associazione
			oi.setTipoAssociazioneEmissione(TipoAssociazioneEmissione.SUB_ORD);
			model.getGestioneOrdinativoStep1Model().getOrdinativo().getElencoOrdinativiCollegati().add(oi);
			model.getGestioneOrdinativoStep1Model().setTotaleImportoCollegati(totaleImportoCollegatiAggiornato);
			model.getGestioneOrdinativoStep1Model().setAbilitatoACollegareOrdinativiNuovi(true);
			
			// sposta l'ancora all'altezza dei vincoli
			model.getGestioneOrdinativoStep1Model().setPortaAdAltezzaOrdinativiCollegati(true);
			model.getGestioneOrdinativoStep1Model().setNumeroOrdinativoDaCollegare(null);
			
		}
		
		//pulisco l'ordinativo di passaggio:
		model.getGestioneOrdinativoStep1Model().setOrdIncassoDaConfermare(null);
		
		//SIAC-6138
		model.setSaltaControlloSoggetto(false);
		model.setChiediConfermaCollegamentoOrdinativo(false);
		
		return SUCCESS;
	}
	
		
	public String eliminaOrdinativoCollegato() throws Exception {
		
		//pulisco i flag per i pop up di conferma per evitare interferenze di dati sporchi:
		model.getGestioneOrdinativoStep1Model().setCheckWarningDaCollegareQuietanziato(false);
		//
		
		OrdinativoPagamento op = model.getGestioneOrdinativoStep1Model().getOrdinativo();
		
		
		// controlli da eseguire:
		Integer numeroOrdinativoDaCollegare = Integer.parseInt(getNumeroOrdinativoDaPassare());
		
		List<Ordinativo> tmpElencoOrdinativiCollegati = new ArrayList<Ordinativo>();
		for (Ordinativo oi : op.getElencoOrdinativiCollegati()) {
		
			Integer numero = oi.getNumero();
			if(!numero.equals(numeroOrdinativoDaCollegare)){
				tmpElencoOrdinativiCollegati.add(oi);
			}
			
		}
		
		op.setElencoOrdinativiCollegati(tmpElencoOrdinativiCollegati);
		
		//Ricalcolo il totale! 
		BigDecimal calcoloImportoCollegati = BigDecimal.ZERO;
		
		for (Ordinativo oi : op.getElencoOrdinativiCollegati()) {
			calcoloImportoCollegati = calcoloImportoCollegati.add(oi.getImportoOrdinativo());
		}
		
		model.getGestioneOrdinativoStep1Model().setTotaleImportoCollegati(calcoloImportoCollegati);
		// sposta l'ancora all'altezza dei vincoli
		model.getGestioneOrdinativoStep1Model().setPortaAdAltezzaOrdinativiCollegati(true);
		
		return SUCCESS;
	}
	
	
	

	@Override
	protected BigDecimal getDisponibilitaContoVincolato(ControllaDisponibilitaCassaContoVincolatoCapitoloResponse response) {
		return response.getDisponibilitaContoVincolatoSpesa();
	}

	
	@Override
	protected void setCapitoloControllaDisponibilitaCassaContoVincolato(
			ControllaDisponibilitaCassaContoVincolatoCapitolo controllaDisponibilitaCassaContoVincolatoCapitolo, 
			CapitoloImpegnoModel capitoloImpegnoModel) {
		
		CapitoloUscitaGestione capitolo = new CapitoloUscitaGestione();
		
		capitolo.setNumeroCapitolo(capitoloImpegnoModel.getNumCapitolo());
		capitolo.setNumeroArticolo(capitoloImpegnoModel.getArticolo());
		capitolo.setNumeroUEB(capitoloImpegnoModel.getUeb() != null? capitoloImpegnoModel.getUeb().intValue() : 0);
		
		controllaDisponibilitaCassaContoVincolatoCapitolo.setCapitoloUscitaGestione(capitolo);
	}
	
	
	
	
	@Override	
	public String resede(){
		return super.resede();
	}
	
	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */

	public Integer getIdOrdinativo() {
		return idOrdinativo;
	}

	public void setIdOrdinativo(Integer idOrdinativo) {
		this.idOrdinativo = idOrdinativo;
	}

	public String getNumeroOrdinativo() {
		return numeroOrdinativo;
	}

	public void setNumeroOrdinativo(String numeroOrdinativo) {
		this.numeroOrdinativo = numeroOrdinativo;
	}

	public String getAnnoOrdinativo() {
		return annoOrdinativo;
	}

	public void setAnnoOrdinativo(String annoOrdinativo) {
		this.annoOrdinativo = annoOrdinativo;
	}

	public String getDoveMiTrovo() {
		return doveMiTrovo;
	}

	public void setDoveMiTrovo(String doveMiTrovo) {
		this.doveMiTrovo = doveMiTrovo;
	}



	/**
	 * @return the numeroOrdinativoDaPassare
	 */
	public String getNumeroOrdinativoDaPassare() {
		return numeroOrdinativoDaPassare;
	}



	/**
	 * @param numeroOrdinativoDaPassare the numeroOrdinativoDaPassare to set
	 */
	public void setNumeroOrdinativoDaPassare(String numeroOrdinativoDaPassare) {
		this.numeroOrdinativoDaPassare = numeroOrdinativoDaPassare;
	}



	/**
	 * @return the annoOrdinativoDaPassare
	 */
	public String getAnnoOrdinativoDaPassare() {
		return annoOrdinativoDaPassare;
	}



	/**
	 * @param annoOrdinativoDaPassare the annoOrdinativoDaPassare to set
	 */
	public void setAnnoOrdinativoDaPassare(String annoOrdinativoDaPassare) {
		this.annoOrdinativoDaPassare = annoOrdinativoDaPassare;
	}
	
	
}
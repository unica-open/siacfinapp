/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbito;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbitoResponse;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;




/*
 * Action per gestisci Impegno Ror...SIAC-6997*/
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestisciImpegnoRorAction_old extends ActionKeyAggiornaImpegno {
	
	@Autowired @Qualifier("movimentoGestioneFinService")
	MovimentoGestioneService movimentoGestioneFinService;
	
	@Autowired
	ProvvedimentoService provvedimentoService;
	
	private String anno;
	private String numero;
	private boolean cruscottoDisabled = false;
	private Boolean flagValido;
	private Boolean flagSoggettoValido;
	
	public Boolean getFlagValido() {
		return flagValido;
	}

	public void setFlagValido(Boolean flagValido) {
		this.flagValido = flagValido;
	}

	public Boolean getFlagSoggettoValido() {
		return flagSoggettoValido;
	}

	public void setFlagSoggettoValido(Boolean flagSoggettoValido) {
		this.flagSoggettoValido = flagSoggettoValido;
	}

	public boolean isCruscottoDisabled() {
		return cruscottoDisabled;
	}

	public void setCruscottoDisabled(boolean cruscottoDisabled) {
		this.cruscottoDisabled = cruscottoDisabled;
	}

	private boolean impegnoRor = true;
	
	
	
	private static final String methodName ="ricercaPuntualeImpegno";

	//PROPRIETA DI APPOGGIO - forse da eliminare
	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	/********/
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public GestisciImpegnoRorAction_old () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.IMPEGNO;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		
		//ho ripreso tutto da da InserisciModificaMovimentoSpesaAction
		this.model.setOperazioneAggiorna(true);
		
		try{	
			if(model.getListaTipiProvvedimenti()==null || model.getListaTipiProvvedimenti().size()==0){
				caricaTipiProvvedimenti();
			}
			
			if(model.getListaTipiAmbito()==null || model.getListaTipiAmbito().isEmpty()){
				caricaListaAmbiti();
			}			
			caricaStatiOperativiAtti();
			
			caricaListePerRicercaSoggetto();
			caricaListeGestisciImpegnoStep1();
			
			model.getStep1Model().setDaRiaccertamento(buildListaSiNo());
			model.getStep1Model().setListflagAttivaGsa(buildListaSiNo());
			
			//SIAC-6997
			model.getStep1Model().setDaReanno(buildListaSiNo());
			
			model.getStep1Model().setDaPrenotazione(buildListaSiNo());
		    model.getStep1Model().setDiCassaEconomale(buildListaSiNo());
		    
		    model.getStep1Model().setScelteFrazionabile(buildListaFrazionabile());
			
			// non agisce l'ancora dei vincoli 
		    model.getStep1Model().setPortaAdAltezzaVincoli(false);
		    
		    //tipo debito siope:
		    model.getStep1Model().setScelteTipoDebitoSiope(buildListaTipoDebitoSiopePerImpegni());
		    
		    
//		    /* Struttura Amministrativo Contabile */
//			List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile =
//					sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
//			if (listaStrutturaAmministrativoContabile != null && !listaStrutturaAmministrativoContabile.isEmpty()) {
//				model.getStep1Model().setListaStrutturaAmministrativoContabile(listaStrutturaAmministrativoContabile);
//			}
		    
		}catch(Exception e){			
			log.error("prepare", e.getMessage());
		}
		
		setVisualizzaLinkConsultaModificheProvvedimento(true);
		setAbilitazioni();
		//this.model.setTitolo("Gestione ROR");
		sessionHandler.setParametro(FinSessionParameter.GESTISCI_IMPEGNO_MODEL, model);
	}	
	
	/**
	 * metodo execute della action
	 */
	@Override
	public String execute() throws Exception {
		model.getStep1Model().setAnnoImpegno(Integer.valueOf(getAnno()));
		model.getStep1Model().setNumeroImpegno(Integer.valueOf(getNumero()));
		
		String goToAction = "";

		RicercaImpegnoPerChiave request = createRequestForRicercaImpegno();
		RicercaImpegnoPerChiaveResponse rpr = movimentoGestioneFinService.ricercaImpegnoPerChiave(request);
		
		if(rpr.hasErrori()){
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, rpr.getErrori());
			return INPUT;
		}
		
		Impegno impegno = rpr.getImpegno();
		List<ModificaMovimentoGestioneSpesa> listaModificheImpegno = new ArrayList<ModificaMovimentoGestioneSpesa>();
		if(impegno.getListaModificheMovimentoGestioneSpesa() != null){
			listaModificheImpegno = impegno.getListaModificheMovimentoGestioneSpesa();
		}
		
		String nameActionOrDisabled = manageActionToGo(listaModificheImpegno);
		if(nameActionOrDisabled.equals("disabilita_cruscotto")){
			log.debug(methodName, "Errore nella risposta del servizio");
			setCruscottoDisabled(true);
			return "goToAggiornaRor";
			
		}
		String nameAction = manageActionToGo(listaModificheImpegno);
		goToAction = "goTo"+nameAction;
		if(!nameAction.equals("InserisciRor")){
			mapAttoToProvvedimentoModel(impegno);
		}
		//POI VA ELIMINATO
		mapAttoToProvvedimentoModel(impegno);

		
		return goToAction;
		
	}

	/*
	 * Questo metodo definisce la action su cui atterrare dopo l'execute in base alle varie condizioni delle modifiche effettuate sull'impegno*/
	private String manageActionToGo(List<ModificaMovimentoGestioneSpesa> listaModificheImpegno) {	
		
		boolean inserisciNuovaModifica = vaiSuInserisciModificaRor(listaModificheImpegno);
		
		//Se non ci sono modifiche, return direttamente la action di inserimento modifica
		if(listaModificheImpegno.isEmpty() || inserisciNuovaModifica){
			return "InserisciRor";
		}
		
		List<Integer> anniDiReimputazione = new ArrayList<Integer>();
		int  numeroMantenimenti = 0;
		int numeroCancellazioni = 0;
		for(ModificaMovimentoGestioneSpesa mmgs : listaModificheImpegno){
			if(mmgs.isReimputazione() && mmgs.getTipoModificaMovimentoGestione().equals("REIMP")){
				anniDiReimputazione.add(mmgs.getAnnoReimputazione());
			}
			if(mmgs.getTipoModificaMovimentoGestione().equals("CROR")){
				numeroCancellazioni++;
			}
			if(mmgs.getTipoModificaMovimentoGestione().equals(CODICE_MOTIVO_ROR_MANTENERE)){
				numeroMantenimenti++;
			}
			
		}
		
		boolean duplicatiInLista = cercaDuplicatiAnniReimputazione(anniDiReimputazione);
		
		
		
		return "";

	}
	
	
	private boolean vaiSuInserisciModificaRor(List<ModificaMovimentoGestioneSpesa> listaModificheImpegno) {
		List<String> motiviDiInteresse = new ArrayList<String>();
		for(ModificaMovimentoGestioneSpesa mmgs : listaModificheImpegno){
			motiviDiInteresse.add(mmgs.getTipoModificaMovimentoGestione());			
		}		
		if(!motiviDiInteresse.contains("REIMP") && !motiviDiInteresse.contains("CROR") && !motiviDiInteresse.contains(CODICE_MOTIVO_ROR_MANTENERE)){
			return true;
		}
		return false;
	}

	//cerco duplicati in lista utilizzando il Set (valori univoci)
	private boolean cercaDuplicatiAnniReimputazione(List<Integer> anniDiReimputazione) {
		if(anniDiReimputazione.size()>0){
			Set<Integer> set = new HashSet<Integer>(anniDiReimputazione);
			if(set.size() < anniDiReimputazione.size()){
				return true;
			}	
		}
		return false;
	}

	/*Crea request per chiamata*/
	private RicercaImpegnoPerChiave createRequestForRicercaImpegno() {
		RicercaImpegnoPerChiave request = new RicercaImpegnoPerChiave();
		RicercaImpegnoK ra = new RicercaImpegnoK();
		ra.setAnnoImpegno(Integer.valueOf(anno));
		ra.setNumeroImpegno(new BigDecimal(numero));
		ra.setAnnoEsercizio(sessionHandler.getAnnoBilancio());	
		request.setpRicercaImpegnoK(ra);
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setEnte(sessionHandler.getEnte());
		return request;
	}
	
	private void mapAttoToProvvedimentoModel(Impegno impegno) {
		ProvvedimentoImpegnoModel pim = new ProvvedimentoImpegnoModel();
		AttoAmministrativo atto = impegno.getAttoAmministrativo();
		pim.setNumeroProvvedimento(BigInteger.valueOf(atto.getNumero()));
		pim.setAnnoProvvedimento(atto.getAnno());
		pim.setOggetto(atto.getOggetto());
		pim.setTipoProvvedimento(atto.getTipoAtto().getDescrizione());
		pim.setCodiceStrutturaAmministrativa(atto.getStrutturaAmmContabile().getCodice());
		pim.setStrutturaAmministrativa(atto.getStrutturaAmmContabile().getDescrizione());
		pim.setStato(atto.getStatoOperativo());
		model.getStep1Model().setProvvedimento(pim);
		model.getStep1Model().getProvvedimento().setIdTipoProvvedimento(atto.getTipoAtto().getUid());
		model.getStep1Model().setProvvedimentoSelezionato(true);		
	}
	
	@Override
	public boolean oggettoDaPopolareImpegno() {
		return oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO) && !impegnoRor;
	}
	
	
	//Metodi copiati dalla Action InserisciModificaMovimentoDiSpesa
	private void setAbilitazioni(){
		if(model.getStep1Model().getNumeroImpegno()!=null){
			
			setAbilitaModificaImporto(true);
			
			boolean bilPrecInPredisposizioneConsuntivo = isBilancioPrecedenteInPredisposizioneConsuntivo();
			
			setFlagValido(false);
			setFlagSoggettoValido(false);
			//stato D o ND
			if(!model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa().equals(CostantiFin.MOVGEST_STATO_PROVVISORIO)){
				setFlagValido(true);
				
				//nel caso in cui fossimo in predisposizione consuntivo, rimettiamo flagvalido a false
				//in modo che l'importo ritorni modificabile:
				
				if(bilPrecInPredisposizioneConsuntivo && isResiduo()){
					setAbilitaModificaImporto(true);
				} else {
					setAbilitaModificaImporto(false);
				}
				//
				
				setFlagSoggettoValido(true);
			}
			
			//JIRA  SIAC-3506 in caso di residuo con presenza di modifiche di importo valide, importo non modificabile:
			List<ModificaMovimentoGestioneSpesa> modifiche = model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa();
			if(presenteModificaDiImportoValida(modifiche) && isResiduo()){
				setAbilitaModificaImporto(false);
			}
			//
			
			//stato
			if(model.getImpegnoInAggiornamento().getElencoSubImpegni()!=null || (model.getListaSubimpegni()!=null && model.getListaSubimpegni().size()>0)){
				setFlagSoggettoValido(true);
			}
			
			// jira-1582
			// se e' in stato N e ci sono dei sub allora posso modificare il soggetto
			if(model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa().equals(CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE)){
				if(!presenteAlmenoUnMovValido(model.getListaSubimpegni(), OggettoDaPopolareEnum.SUBIMPEGNO.toString())){
					setFlagSoggettoValido(false);
				}
			}
		}
		if(model.getStep1Model().getProgetto()!= null){
			model.getStep1ModelSubimpegno().setProgetto(model.getStep1Model().getProgetto());
		}
	}
	
	private void caricaListaAmbiti() {
		
		RicercaTipiAmbito request = model.creaRequestRicercaTipiAmbito();
		request.setAnno(sessionHandler.getAnnoBilancio());
		RicercaTipiAmbitoResponse response = progettoService.ricercaTipiAmbito(request);

		model.setListaTipiAmbito(response.getTipiAmbito());
	}
	
	
	
	
}

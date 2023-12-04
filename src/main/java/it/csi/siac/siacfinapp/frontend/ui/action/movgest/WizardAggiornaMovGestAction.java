/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.business.utility.capitolo.ComponenteImportiCapitoloPerAnnoHelper;
import it.csi.siac.siacbilser.frontend.webservice.ComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ErroreMovGestModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciImpegnoStep1Model;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ImportiCapitoloModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.MovimentoConsulta;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegnoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.EsistenzaProgetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.EsistenzaProgettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiStoricoAggiornamentoProvvedimentoMovimentoGestione;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiStoricoAggiornamentoProvvedimentoMovimentoGestioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiStoricoAggiornamentoStrutturaCompetenteMovimentoGestione;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiStoricoAggiornamentoStrutturaCompetenteMovimentoGestioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaReversaliByAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaReversaliByAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.VerificaLegameImpegnoLiquidazioni;
import it.csi.siac.siacfinser.frontend.webservice.msg.VerificaLegameImpegnoLiquidazioniResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.AccertamentoAbstract;
import it.csi.siac.siacfinser.model.AttoAmministrativoStoricizzato;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.ImpegnoAbstract;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.StrutturaAmmContabileFlatStoricizzato;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ComponenteBilancioImpegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

public class WizardAggiornaMovGestAction extends WizardGenericMovGestAction {

	private static final long serialVersionUID = 1L;

	protected final static String GOTO_ELENCO_MODIFICHE = "gotoElencoModifiche"; 
	private Impegno impegnoToUpdate = new Impegno();
	private Accertamento accertamentoToUpdate = new Accertamento();
	private CapitoloUscitaGestione capitoloUscitaTrovato = new CapitoloUscitaGestione();
	private CapitoloEntrataGestione capitoloEntrataTrovato = new CapitoloEntrataGestione();

	private boolean showPopUpMovColl;
	private boolean proseguiStep1;
	private boolean fromPopup;
	
	private boolean showModaleConfermaModificaProvvedimento;
	private boolean fromModaleConfermaModificaProvvedimento;
	
	private boolean showModaleConfermaProseguiModificaProvvedimento;
	private boolean fromModaleConfermaProseguiModificaProvvedimento;
	private boolean visualizzaLinkConsultaModificheProvvedimento = false;
	
	
	// 28/09/2017 RM: modifica richiesta da Vit 
	// Se l'impegno ha un capitolo senza disponibilità bisogna permettere la modifica dei vincoli
	private boolean showModaleConfermaSalvaModificaVincoli;
	private boolean fromModaleConfermaSalvaModificaVincoli;
	private boolean showModaleConfermaProseguiModificaVincoli;
	private boolean fromModaleConfermaProseguiModificaVincoli;
	
	private Boolean abilitaBottneSalvaDecentrato = true;

	
	private Boolean abilitaModificaImporto;
	
	private boolean successStep1 = false;
	private boolean provvedimentoAbilitato = false;
	private boolean soggettoAbilitato = false;
	private boolean progettoAbilitato = false;
	private boolean importoAbilitato = false;
	
	//SIAC-7861
	protected String uidPerDettaglioSub;
	protected String uidSubDaAnnullare;
	protected String numeroSubDaAnnullare;
	protected String uidSubDaEliminare;
	protected String numeroSubDaEliminare;
	
	private AttoAmministrativo provvedimento = new AttoAmministrativo();
	private Soggetto soggettoCreditore = new Soggetto();
	private ClasseSoggetto classeCreditore = new ClasseSoggetto();

	protected String doveMiTrovo;
	
	//SIAC-7349 - Inizio - SR90 - MR - 03/2020 - Inject servizio componenti capitolo
	@Autowired protected ComponenteImportiCapitoloService componenteImportiCapitoloService;
	protected BigDecimal disponibilitaImpegno;
	//SIAC-7349 - Fine
	

	/**
	 * Metodo introdotto con la:
	 * SIAC-4923 FIN - IMPEGNI e ACCERTAMENTI Aggiornamento pr decentrati (CR 912)
	 * 
	 *  Nel caso in cui l'utente abbia anche l'azione OP-SPE-gestisciImpegnoDecentratoP/OP-ENT-gestiscAccertamentoDecentratoP
	 *  non deve poter aggiornare nessun dato dell'impegno/accertamento definitivo o definitivo non liquidabile ne' fare modifiche 
	 *	
	 *  quando l'utente ha il'azione DecentratoP devono essere DISABILITATI:
	 *	1. Il SALVA nella prima e nella seconda videata del movimento
	 *		2. il folder modifiche 
	 * 
	 * @return
	 */
	public boolean disabilitaTabModificheAggiornamento(){
		boolean disabilitaTabModificheAggiornamento = false;
		
		if(oggettoDaPopolareImpegno() || oggettoDaPopolareSubimpegno()){
			
			if(model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa().equals(CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE)
			  || model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa().equals(CostantiFin.MOVGEST_STATO_DEFINITIVO)){
				//SIAC-7360, eliminato comportamento introdotto dalla SIAC-5885
				disabilitaTabModificheAggiornamento = !isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_GestisciModifica);
				setAbilitaBottneSalvaDecentrato(Boolean.valueOf(!isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_gestisciImpegnoDecentratoP)));
				//SIAC-7360 comportamento precedente:
//				boolean gestisciImpegnoDecentrato = isAzioneConsentita(AzioniConsentite.OP_SPE_gestisciImpegnoDecentratoP;
//				disabilitaTabModificheAggiornamento = gestisciModificaImp && !(isBilancioAttualeInPredisposizioneConsuntivo() && isAbilitatoGestisciImpegnoRiaccertato());
				
			}
			
		} else if(oggettoDaPopolareAccertamento() || oggettoDaPopolareSubaccertamento()){
			
			if(model.getAccertamentoInAggiornamento().getStatoOperativoMovimentoGestioneEntrata().equals(CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE)
					  || model.getAccertamentoInAggiornamento().getStatoOperativoMovimentoGestioneEntrata().equals(CostantiFin.MOVGEST_STATO_DEFINITIVO)){
				//SIAC-7360, eliminato comportamento introdotto dalla SIAC-5885
				disabilitaTabModificheAggiornamento = !isAzioneConsentita(AzioneConsentitaEnum.OP_ENT_GestisciModifica);
				setAbilitaBottneSalvaDecentrato(Boolean.valueOf(!isAzioneConsentita(AzioneConsentitaEnum.OP_ENT_gestisciAccertamentoDecentratoP)));
				//SIAC-7360 comportamento precedente:
//				disabilitaTabModificheAggiornamento = gestisciAccertamentoDecentrato && !(isBilancioAttualeInPredisposizioneConsuntivo() && isAbilitatoGestisciImpegnoRiaccertato());
			 }
			
		}
		
		return disabilitaTabModificheAggiornamento;
	}
	
	//SIAC-6997
	public boolean abilitaTabModificheImpAcc(){
		boolean abilitaTabModificheImpAcc = true;
		//SIAC-7988
		if(oggettoDaPopolareImpegno() || oggettoDaPopolareSubimpegno()){
			//SIAC-7988
			if(isAbilitatoImpegnoRORdecentratoEFaseBilancioPredisposizioneConsuntivo() ) {
				abilitaTabModificheImpAcc = false;
			}
		} else if(oggettoDaPopolareAccertamento() || oggettoDaPopolareSubaccertamento()){
			if(isAbilitatoAccertamentoRORdecentratoEFaseBilancioPredisposizioneConsuntivo()) {
				abilitaTabModificheImpAcc = false;
			}
		}
		
		return abilitaTabModificheImpAcc;
	}
	
	//PROVA TRASLOCO
	
	/**
	 * SIAC-8041
	 * Metodo di utilita' per ottenere le reimputazioni dalla lista delle modifiche collegate
	 * @param listaModifiche
	 * @return List<ModificaMovimentoGestioneSpesaCollegata> lista
	 */
	protected List<ModificaMovimentoGestioneSpesaCollegata> initListaModificheSpesaCollegataSoloReimp(List<ModificaMovimentoGestioneSpesaCollegata> listaModifiche){
		//da ridefinire in sottoclasse
		return null;
	}
	
	/**
	 * @return the visualizzaLinkConsultaModificheProvvedimento
	 */
	public boolean isVisualizzaLinkConsultaModificheProvvedimento() {
		return visualizzaLinkConsultaModificheProvvedimento;
	}

	/**
	 * @param visualizzaLinkConsultaModificheProvvedimento the visualizzaLinkConsultaModificheProvvedimento to set
	 */
	public void setVisualizzaLinkConsultaModificheProvvedimento(
			boolean visualizzaLinkConsultaModificheProvvedimento) {
		this.visualizzaLinkConsultaModificheProvvedimento = visualizzaLinkConsultaModificheProvvedimento;
	}

	/**
	 * @return the provvedimento
	 */
	public AttoAmministrativo getProvvedimento() {
		return provvedimento;
	}

	/**
	 * @param provvedimento the provvedimento to set
	 */
	public void setProvvedimento(AttoAmministrativo provvedimento) {
		this.provvedimento = provvedimento;
	}

	/**
	 * @return the showModaleConfermaModificaProvvedimento
	 */
	public boolean isShowModaleConfermaModificaProvvedimento() {
		return showModaleConfermaModificaProvvedimento;
	}

	/**
	 * @param showModaleConfermaModificaProvvedimento the showModaleConfermaModificaProvvedimento to set
	 */
	public void setShowModaleConfermaModificaProvvedimento(
			boolean showModaleConfermaModificaProvvedimento) {
		this.showModaleConfermaModificaProvvedimento = showModaleConfermaModificaProvvedimento;
	}

	/**
	 * @return the fromModaleConfermaModificaProvvedimento
	 */
	public boolean isFromModaleConfermaModificaProvvedimento() {
		return fromModaleConfermaModificaProvvedimento;
	}

	/**
	 * @param fromModaleConfermaModificaProvvedimento the fromModaleConfermaModificaProvvedimento to set
	 */
	public void setFromModaleConfermaModificaProvvedimento(
			boolean fromModaleConfermaModificaProvvedimento) {
		this.fromModaleConfermaModificaProvvedimento = fromModaleConfermaModificaProvvedimento;
	}

	/**
	 * true se IMPEGNO, false se ACCERTAMENTO
	 * @return boolean
	 */
	public boolean oggettoDaPopolareImpegno() {
		return oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO);
	}		
	
	/**
	 * true se SUBIMPEGNO, false se SUBACCERTAMENTO
	 * @return boolean
	 */
	public boolean oggettoDaPopolareSubimpegno() {
		return oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBIMPEGNO);
	}		
	
	public boolean oggettoDaPopolareAccertamento() {
		return oggettoDaPopolare.equals(OggettoDaPopolareEnum.ACCERTAMENTO);
	}		
	
	public boolean oggettoDaPopolareSubaccertamento() {
		return oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBACCERTAMENTO);
	}		
	
	public MovimentoGestione getMovimentoInAggiornamento() {
		
		if (oggettoDaPopolareImpegno()) {
			return model.getImpegnoInAggiornamento();
		}
		
		if (oggettoDaPopolareAccertamento()) {
			return model.getAccertamentoInAggiornamento();
		}

		return null;
	}
	
	/**
	 * convertiModelPerChiamataServizioAggiornaImpegniDaSubimpegno
	 * 
	 * @param listaErrori
	 */
	protected AggiornaImpegno convertiModelPerChiamataServizioAggiornaImpegniDaSubimpegno() {
			   
			   AggiornaImpegno aggiornaImpegnoReq = new AggiornaImpegno();
			    
			   Bilancio bilancio = new Bilancio();
			   bilancio.setAnno(sessionHandler.getAnnoBilancio());
			   Impegno impegnoDaAggiornare = popolaImpegnoAggiornaSubimpegno(model, null, bilancio);
			   
			   aggiornaImpegnoReq.setImpegno(impegnoDaAggiornare);
			   aggiornaImpegnoReq.setEnte(model.getEnte());
			   aggiornaImpegnoReq.getImpegno().setSoggetto(impegnoDaAggiornare.getSoggetto());
			     
			   //JIRA 1761 inserendo dei nuovi subimpegni si perdono i voncoli, e difatti non vengono settati sull'oggetto 
			   // impegnoDaAggiornare
			  if(model.getStep1Model().getListaVincoliImpegno()!=null && !model.getStep1Model().getListaVincoliImpegno().isEmpty()){
				  impegnoDaAggiornare.setVincoliImpegno(model.getStep1Model().getListaVincoliImpegno());
			  }
			   
			   // subimpegni
			  //
			  if(!isEmpty(model.getListaSubimpegni())){
				   aggiornaImpegnoReq.getImpegno().setElencoSubImpegni(model.getListaSubimpegni());
			   }else{
				   aggiornaImpegnoReq.getImpegno().setElencoSubImpegni(new ArrayList<SubImpegno>());
			   }
			   //
				   
			   aggiornaImpegnoReq.setRichiedente(sessionHandler.getRichiedente());
			   aggiornaImpegnoReq.setUnitaElementareDiGestione(null);
			   aggiornaImpegnoReq.getImpegno().setCapitoloUscitaGestione(capitoloUscitaTrovato);
			   aggiornaImpegnoReq.setBilancio(bilancio);
			   
			   //SIAC-5943
			   aggiornaImpegnoReq.setSaltaInserimentoPrimaNotaSuSub(model.isSaltaInserimentoPrimaNota());
			   
			   return aggiornaImpegnoReq;
			}
		
		
			
		
		
		/**
		 * crea la request per aggiornare l'impegno
		 * @param pulisciTE
		 * @param propagaISub
		 * @return
		 */
		protected AggiornaImpegno convertiModelPerChiamataServizioAggiornaImpegni(boolean pulisciTE, boolean propagaISub) {

			// AGGIORNA IMPEGNO
			   
		   AggiornaImpegno aggiornaImpegnoReq = new AggiornaImpegno();
		    
		   Bilancio bilancio = new Bilancio();
		   bilancio.setAnno(sessionHandler.getAnnoBilancio());
		   copiaTransazioneElementareSupportSuModel(pulisciTE);
		   Impegno impegnoDaAggiornare = popolaImpegnoAggiorna(model, null, bilancio);
		   
		   aggiornaImpegnoReq.setImpegno(impegnoDaAggiornare);
		   aggiornaImpegnoReq.setEnte(model.getEnte());
		   aggiornaImpegnoReq.getImpegno().setSoggetto(impegnoDaAggiornare.getSoggetto());
		     
		   
		   // subimpegni
		   if(propagaISub && !isEmpty(model.getListaSubimpegni())){
			   aggiornaImpegnoReq.getImpegno().setElencoSubImpegni(model.getListaSubimpegni());
		   }else{
			   aggiornaImpegnoReq.getImpegno().setElencoSubImpegni(new ArrayList<SubImpegno>());
		   }
		   
		   // eventuali vincoli
		   if(null!=model.getStep1Model().getListaVincoliImpegno() && !model.getStep1Model().getListaVincoliImpegno().isEmpty()){
			   aggiornaImpegnoReq.getImpegno().setVincoliImpegno(model.getStep1Model().getListaVincoliImpegno());
		   }
		   //SIAC-6997
		   aggiornaImpegnoReq.getImpegno().setStrutturaCompetente(model.getStep1Model().getStrutturaSelezionataCompetente());
		   
		   aggiornaImpegnoReq.setRichiedente(sessionHandler.getRichiedente());
		   aggiornaImpegnoReq.setUnitaElementareDiGestione(null);
		   aggiornaImpegnoReq.setBilancio(bilancio);
		   
		   return aggiornaImpegnoReq;
		}
		
		
		/**
		 * crea la request per aggiornare l'impegno
		 * in particolare rispetto alle modifiche
		 * @param pulisciTE
		 * @return
		 */
		protected AggiornaImpegno convertiModelPerChiamataServizioInserisciAggiornaModifiche(boolean pulisciTE) {
			
			// AGGIORNA IMPEGNO
		   
		   AggiornaImpegno aggiornaImpegnoReq = new AggiornaImpegno();
		   
		   //SIAC-8794
		   Bilancio bilancio = sessionHandler.getBilancio();
		   copiaTransazioneElementareSupportSuModel(pulisciTE);
		   Impegno impegnoDaAggiornare = popolaImpegnoInserisciAggiornaModifiche(model, null, bilancio);
		   aggiornaImpegnoReq.setImpegno(impegnoDaAggiornare);
		   aggiornaImpegnoReq.setEnte(model.getEnte());
		   aggiornaImpegnoReq.getImpegno().setSoggetto(impegnoDaAggiornare.getSoggetto());
		     
		   
		   // subimpegni
		   if(!isEmpty(model.getListaSubimpegni())){
			   aggiornaImpegnoReq.getImpegno().setElencoSubImpegni(model.getListaSubimpegni());
		   }else{
			   aggiornaImpegnoReq.getImpegno().setElencoSubImpegni(new ArrayList<SubImpegno>());
		   }
		   
		   // eventuali vincoli
		   if(null!=model.getStep1Model().getListaVincoliImpegno() && !model.getStep1Model().getListaVincoliImpegno().isEmpty()){
			   aggiornaImpegnoReq.getImpegno().setVincoliImpegno(model.getStep1Model().getListaVincoliImpegno());
		   }
			   
		   aggiornaImpegnoReq.setRichiedente(sessionHandler.getRichiedente());
		   aggiornaImpegnoReq.setUnitaElementareDiGestione(null);
		   aggiornaImpegnoReq.setBilancio(bilancio);
		   
		   //SIAC-7349 - Inizio - SR90 - MR - 03/2020 - Set della disponibilita per controlli BE
		   //FIX - 23/04/2020
		   if(model.getMaxImportoImpComp()!=null){
			   aggiornaImpegnoReq.getImpegno().setDisponibilitaImpegnareComponente(convertiImportoToBigDecimal(model.getMaxImportoImpComp()));			   
		   }
		   //SIAC-7349 - END	
		   
		   return aggiornaImpegnoReq;
		}
		
		
		/**
		 * SIAC-7861
		 * refactoring inserisciSubimpegno
		 * in questo modo le action AggiornaSubGenericAction e GestioneSubimpegnoAction
		 * saranno in possesso del relativo metodo
		 */
		public String inserisciSubimpegno() {
			inserisciSub();
			return GOTO_GESTIONE_SUBIMPEGNO;
		}
		
		/**
		 * SIAC-7861
		 * refactoring inserisciSub
		 * in questo modo le action AggiornaSubGenericAction e GestioneSubimpegnoAction
		 * saranno in possesso del relativo metodo
		 */
		public void inserisciSub() {
			setIdSubImpegno(null);
			setInserimentoSubimpegno(true);
			model.setProseguiConWarningControlloSoggetti(false);
			model.getStep1ModelSubimpegno().setOperazioneInserimento(true);
			
			//FIX per JIRA SIAC-3426:
			model.setSubDettaglio(new MovimentoConsulta());
		}
		
		/**
		 * SIAC-7861
		 * refactoring annullaSubImpegno
		 * in questo modo le action AggiornaSubGenericAction e GestioneSubimpegnoAction
		 * saranno in possesso del relativo metodo
		 */
		public String annullaSubImpegno(){
			setMethodName("annullaSubImpegno");
			AnnullaMovimentoSpesaResponse response = movimentoGestioneFinService.annullaMovimentoSpesa(convertiModelPerChiamataServizioAnnulla(getUidSubDaAnnullare()));
			
			if(!response.isFallimento()){
				
				if(response.getImpegno()!= null){
					//MODIFICA PER EVITARE MEGA QUERY
					model.setImpegnoRicaricatoDopoInsOAgg(response.getImpegno());
				}
				//OTTOBRE 2017 correggendo la JIRA SIAC-5339 mi riconduco a
				//quanto gia' fatto dopo l'azione elimina per le ottimizzazioni aprile 2016:
				//OTTIMIZZAZIONI APRILE 2016, il servizio aggiorna non restituisce piu' l'impegno per non rischiare timeout a caricarlo...
				forceReload=true;
				model.setImpegnoRicaricatoDopoInsOAgg(null);
				ricaricaSubImpegni();
				
				
				addActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " 
		                + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
				
				return GOTO_AGGIORNA_SUBIMPEGNO;
			
			} else {
				//SIAC-7853
				addErrori(response);
			}
			return SUCCESS;
		}
		
		/**
		 * SIAC-7861
		 * refactoring convertiModelPerChiamataServizioAnnulla
		 * in questo modo le action AggiornaSubGenericAction e GestioneSubimpegnoAction
		 * saranno in possesso del relativo metodo
		 */
		private AnnullaMovimentoSpesa convertiModelPerChiamataServizioAnnulla(String uid){
			AnnullaMovimentoSpesa request = new AnnullaMovimentoSpesa();
			request.setEnte(sessionHandler.getEnte());
			request.setRichiedente(sessionHandler.getRichiedente());
			request.setBilancio(creaOggettoBilancio());
			Impegno impegno = new Impegno();
			impegno.setAnnoMovimento(model.getImpegnoInAggiornamento().getAnnoMovimento());
			impegno.setNumeroBigDecimal(model.getImpegnoInAggiornamento().getNumeroBigDecimal());
			impegno.setUid(model.getImpegnoInAggiornamento().getUid());
			
			SubImpegno subImpegnoIntero = FinUtility.getById(model.getImpegnoInAggiornamento().getElencoSubImpegni(), Integer.valueOf(uid));
			List<SubImpegno> listaSubImpegnos = new ArrayList<SubImpegno>();
			
			listaSubImpegnos.add(subImpegnoIntero);
			impegno.setElencoSubImpegni(listaSubImpegnos);
			request.setImpegno(impegno);
			return request;
		}
		
		/**
		 * SIAC-7861
		 * refactoring eliminaSubImpegno
		 * in questo modo le action AggiornaSubGenericAction e GestioneSubimpegnoAction
		 * saranno in possesso del relativo metodo
		 */
		public String eliminaSubImpegno(){
			setMethodName("eliminaSubImpegno");
			if(null!=model.getListaSubimpegni() &&
			   !model.getListaSubimpegni().isEmpty()){
				
				List<SubImpegno> elencoSubImpegniDaEliminare = new ArrayList<SubImpegno>();
				
				for (int i = 0; i < model.getListaSubimpegni().size(); i++) {
					SubImpegno sub = model.getListaSubimpegni().get(i);
					if(sub.getUid() == Integer.valueOf(getUidSubDaEliminare())){
						elencoSubImpegniDaEliminare.add(sub);
					} 	 
				}
				
				//NUOVO FUNZIONAMENTO, NON DEVO PASSARE GLI ALTRI SUB:
				model.setListaSubimpegni(new ArrayList<SubImpegno>());
				
				AggiornaImpegno requestAggiorna = convertiModelPerChiamataServizioAggiornaImpegni(true,false);
				
				//Fix per SIOPE JIRA  SIAC-3913:
				requestAggiorna.getImpegno().setCodSiope(model.getImpegnoInAggiornamento().getCodSiope());
				//
				
				//FEB-MARZO 2016, NUOVO FUNZIONAMENTO. PER LA NUOVA PAGINAZIONE DEI SUB, quelli da eliminare vanno indicati
				//esplicitamente in una lista a parte:
				requestAggiorna.setElencoSubImpegniDaEliminare(elencoSubImpegniDaEliminare);
				//
				
				if (requestAggiorna.getImpegno().getProgetto() != null) {
					Progetto progetto = requestAggiorna.getImpegno().getProgetto();
					
					progetto.setTipoProgetto(TipoProgetto.GESTIONE);
					progetto.setBilancio(sessionHandler.getBilancio());

					requestAggiorna.getImpegno().setIdCronoprogramma(model.getStep1Model().getIdCronoprogramma());
					requestAggiorna.getImpegno().setIdSpesaCronoprogramma(model.getStep1Model().getIdSpesaCronoprogramma());
				}
				

				AggiornaImpegnoResponse response = movimentoGestioneFinService.aggiornaImpegno(requestAggiorna); 
				
				
				if(!response.isFallimento() && response.getImpegno()!= null){
					//OTTIMIZZAZIONI APRILE 2016, il servizio aggiorna non restituisce piu' l'impegno per non rischiare timeout a caricarlo...
					forceReload=true;
					model.setImpegnoRicaricatoDopoInsOAgg(null);
					return GOTO_AGGIORNA_SUBIMPEGNO;
				}else{
					addErrori(response.getErrori());
					return INPUT;
				}
			}
			ricaricaSubImpegni();
			return SUCCESS;
		}
		
		/**
		 * SIAC-7861
		 * refactoring ricaricaSubImpegni
		 * in questo modo le action AggiornaSubGenericAction e GestioneSubimpegnoAction
		 * saranno in possesso del relativo metodo
		 */
		private void ricaricaSubImpegni(){
			if(model.getImpegnoRicaricatoDopoInsOAgg()!= null){
				model.setListaSubimpegni(model.getImpegnoRicaricatoDopoInsOAgg().getElencoSubImpegni());
				model.setDisponibilitaSubImpegnare(model.getImpegnoRicaricatoDopoInsOAgg().getDisponibilitaSubimpegnare());
				model.setTotaleSubImpegno(model.getImpegnoRicaricatoDopoInsOAgg().getTotaleSubImpegniBigDecimal());
				model.setImpegnoInAggiornamento(model.getImpegnoRicaricatoDopoInsOAgg());
				setImpegnoToUpdate(model.getImpegnoRicaricatoDopoInsOAgg());
				caricaDati(true);
				model.setImpegnoRicaricatoDopoInsOAgg(null);
			}
		}
		
		/**
		 * Popola la request per il servizio di aggiorna accertamento
		 * @param pulisciTE
		 * @return
		 */
		protected AggiornaAccertamento convertiModelPerChiamataServizioAggiornaAccertamenti(boolean pulisciTE) {
			
			AggiornaAccertamento aggiornaAccertamentoReq = new AggiornaAccertamento();
			Bilancio bilancio = new Bilancio();
            bilancio.setAnno(sessionHandler.getAnnoBilancio());
            copiaTransazioneElementareSupportSuModel(pulisciTE);
            Accertamento accertamentoDaAggiornare = popolaAggiornaAccertamentoRequestPerAggiornaModifiche(model, null, bilancio);
				   
            aggiornaAccertamentoReq.setAccertamento(accertamentoDaAggiornare);
            aggiornaAccertamentoReq.setEnte(model.getEnte());
            aggiornaAccertamentoReq.getAccertamento().setSoggetto(accertamentoDaAggiornare.getSoggetto());
				  
            if(!isEmpty(model.getListaSubaccertamenti())){
			   aggiornaAccertamentoReq.getAccertamento().setElencoSubAccertamenti(model.getListaSubaccertamenti()); 
            }
					   
 		    //SIAC-6997
            aggiornaAccertamentoReq.getAccertamento().setStrutturaCompetente(model.getStep1Model().getStrutturaSelezionataCompetente());
 		   
            aggiornaAccertamentoReq.setRichiedente(sessionHandler.getRichiedente());
            aggiornaAccertamentoReq.setUnitaElementareGestioneE(null);
            aggiornaAccertamentoReq.setBilancio(bilancio);
            
            //SIAC-8178
			aggiornaAccertamentoReq.getAccertamento().setCodiceVerbale(model.getStep1Model().getCodiceVerbale());
				   
            return aggiornaAccertamentoReq;
		}
		
		/**
		 * Imposta la request per la chiamata aggiorna accertamento da aggiorna modifica importo/soggetto
		 * 
		 * @param pulisciTE
		 * @return
		 */
		protected AggiornaAccertamento convertiModelPerChiamataServiziInserisciAggiornaModifiche(boolean pulisciTE) {

		   AggiornaAccertamento aggiornaAccertamentoReq = new AggiornaAccertamento();
				    
		   Bilancio bilancio = new Bilancio();
		   bilancio.setAnno(sessionHandler.getAnnoBilancio());
		   copiaTransazioneElementareSupportSuModel(pulisciTE);
		   Accertamento accertamentoDaAggiornare = popolaAggiornaAccertamentoRequestPerAggiornaModifiche(model, null, bilancio);
				   
		   aggiornaAccertamentoReq.setAccertamento(accertamentoDaAggiornare);
		   aggiornaAccertamentoReq.setEnte(model.getEnte());
		   aggiornaAccertamentoReq.getAccertamento().setSoggetto(accertamentoDaAggiornare.getSoggetto());
				     
		   if(!isEmpty(model.getListaSubaccertamenti())){
			   aggiornaAccertamentoReq.getAccertamento().setElencoSubAccertamenti(model.getListaSubaccertamenti()); 
		   }
					   
		   aggiornaAccertamentoReq.setRichiedente(sessionHandler.getRichiedente());
		   aggiornaAccertamentoReq.setUnitaElementareGestioneE(null);
		   aggiornaAccertamentoReq.setBilancio(bilancio);
				   
		   return aggiornaAccertamentoReq;
		}
		
		
		
		/**
		 * crea la request per aggiornare l'impegno
		 * @return
		 */
		protected AggiornaAccertamento convertiModelPerChiamataServizioAggiornaAccertamentiDaSubimpegno() {

			// AGGIORNA ACCERTAMENTO
				   
		   AggiornaAccertamento aggiornaAccertamentoReq = new AggiornaAccertamento();
				    
		   Bilancio bilancio = new Bilancio();
		   bilancio.setAnno(sessionHandler.getAnnoBilancio());
		   Accertamento accertamentoDaAggiornare = popolaAccertamentoAggiornaSubimpegno(model, null, bilancio);
				   
		   aggiornaAccertamentoReq.setAccertamento(accertamentoDaAggiornare);
		   aggiornaAccertamentoReq.setEnte(model.getEnte());
		   aggiornaAccertamentoReq.getAccertamento().setSoggetto(accertamentoDaAggiornare.getSoggetto());
				     
		   if(!isEmpty(model.getListaSubaccertamenti())){
			   aggiornaAccertamentoReq.getAccertamento().setElencoSubAccertamenti(model.getListaSubaccertamenti()); 
		   }
					   
		   aggiornaAccertamentoReq.setRichiedente(sessionHandler.getRichiedente());
		   aggiornaAccertamentoReq.setUnitaElementareGestioneE(null);
		   aggiornaAccertamentoReq.getAccertamento().setCapitoloEntrataGestione(capitoloEntrataTrovato);
		   aggiornaAccertamentoReq.setBilancio(bilancio);
			
		 //SIAC-5943
		  aggiornaAccertamentoReq.setSaltaInserimentoPrimaNotaSuSub(model.isSaltaInserimentoPrimaNota());
		//SIAC-8178
			aggiornaAccertamentoReq.getAccertamento().setCodiceVerbale(model.getStep1Model().getCodiceVerbale());
		   
		   return aggiornaAccertamentoReq;
		}
	   
		/**
		 * compone la request per ricercare l'impegno
		 * @return
		 */
		protected RicercaImpegnoPerChiaveOttimizzato convertiModelPerChiamataServizioRicercaPerChiave() {
			
			//RICERCA IMPEGNO PER CHIAVE
			
			//istanzio valori dell'impegno da cercare (valori da passare dalla ricerca)
			RicercaImpegnoPerChiaveOttimizzato parametroRicercaPerChiave = new RicercaImpegnoPerChiaveOttimizzato();
			RicercaImpegnoK impegnoDaCercare = new RicercaImpegnoK();
			BigDecimal numeroImpegno = new BigDecimal(String.valueOf(model.getStep1Model().getNumeroImpegno())	);
			
			impegnoDaCercare.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
			impegnoDaCercare.setNumeroImpegno(numeroImpegno);
			impegnoDaCercare.setAnnoImpegno(model.getStep1Model().getAnnoImpegno());
			Ente enteProva = model.getEnte();
				
			parametroRicercaPerChiave.setRichiedente(sessionHandler.getRichiedente());
			parametroRicercaPerChiave.setEnte(enteProva);
			parametroRicercaPerChiave.setpRicercaImpegnoK(impegnoDaCercare);
			
			//PER SIAC-5785 - serve caricare anche il cig dato che viene controllato nei controlli SIOPE PLUS:
			DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliSubs = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
			//serve caricare anche il cig dato che viene controllato nei controlli SIOPE PLUS:
			datiOpzionaliSubs.setCaricaCig(true);
			//il cup viene gratis assieme al cig (sono attr entrambi vedi funzonamento servizio)
			//quindi tanto vale farlo caricare:
			datiOpzionaliSubs.setCaricaCup(true);
			parametroRicercaPerChiave.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliSubs);
			//
			
			return parametroRicercaPerChiave;
		}
		
		/**
		 * Compone la request per ricercare l'accertamento
		 * @return
		 */
		protected RicercaAccertamentoPerChiaveOttimizzato convertiModelAccertamentoPerChiamataServizioRicercaPerChiave() {
			//RICERCA ACCERTAMENTO PER CHIAVE
			
			//istanzio valori dell'impegno da cercare (valori da passare dalla ricerca)
			RicercaAccertamentoPerChiaveOttimizzato parametroRicercaPerChiave = new RicercaAccertamentoPerChiaveOttimizzato();
			RicercaAccertamentoK accertamentoDaCercare = new RicercaAccertamentoK();
			BigDecimal numeroImpegno = new BigDecimal(String.valueOf(model.getStep1Model().getNumeroImpegno()));
			
			accertamentoDaCercare.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
			accertamentoDaCercare.setNumeroAccertamento(numeroImpegno);
			accertamentoDaCercare.setAnnoAccertamento(model.getStep1Model().getAnnoImpegno());
			Ente enteProva = model.getEnte();
					
			parametroRicercaPerChiave.setRichiedente(sessionHandler.getRichiedente());
			parametroRicercaPerChiave.setEnte(enteProva);
			parametroRicercaPerChiave.setpRicercaAccertamentoK(accertamentoDaCercare);
			parametroRicercaPerChiave.setCaricaFlagPresenteStoricizzazioneNelBilancio(accertamentoDaCercare.getAnnoAccertamento().intValue() < accertamentoDaCercare.getAnnoEsercizio().intValue());
			
			//SIAC-8275
			parametroRicercaPerChiave.setCaricalistaModificheCollegate(Boolean.FALSE);
			
			return parametroRicercaPerChiave;
		}
	   
		/**
		 * controlla eventuali errori sugli iporti
		 * @return
		 */
		protected boolean controlloImporti() {
			//versione tre (aggiorna)
			
			//verifica degli importi
			boolean erroreImporti = false;
			
			if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO)){
				
				BigDecimal disponibilitaImpegnare = disponibilitaImpegnare();
				
				//controllo disponibilita se impegno provvisorio (nuovo importo deve essere minore della disponibilita' del capitolo) - jira 743			
				//riapertura jira 743.2
				//controllo disponibilita se impegno non provv (disponibilita non deve essere minore di zero)	--> jira 743.2 indip da stato			
					if (model.getStep1Model().getImportoImpegno()!=null &&
							model.getStep1Model().getCapitoloImpegno()!=null && model.getStep1Model().getCapitoloImpegno().getImportiCapitolo()!=null &&
									disponibilitaImpegnare!=null){
						
						BigDecimal nuovoImporto = model.getStep1Model().getImportoImpegno();
						BigDecimal maxImporto = disponibilitaImpegnare.add(model.getImpegnoInAggiornamento().getImportoAttuale());
						
						if(nuovoImporto.compareTo(maxImporto)>0){
						erroreImporti = true;
						}						
					}
				} else {
					
					BigDecimal disponibilitaAccertare = disponibilitaAccertare();
					
				//controllo disponibilita se accertamento provvisorio (nuovo importo deve essere minore della disponibilita' del capitolo) - jira 743			
				//controllo disponibilita se accertamento non provv (disponibilita non deve essere minore di zero)	--> jira 743.2 indip da stato			
					if (model.getStep1Model().getImportoImpegno()!=null && 										
							model.getStep1Model().getCapitoloAccertamento()!=null  && model.getStep1Model().getCapitoloAccertamento().getImportiCapitolo()!=null && 
									disponibilitaAccertare!=null){
						
						BigDecimal nuovoImporto = model.getStep1Model().getImportoImpegno();
						BigDecimal maxImporto = disponibilitaAccertare.add(model.getAccertamentoInAggiornamento().getImportoAttuale());

						if(nuovoImporto.compareTo(maxImporto)>0){
						erroreImporti = true;
					}	
					}
				}				
			
		    return erroreImporti;
		} 
		
		//SIAC-7349		
		public RicercaComponenteImportiCapitolo creaRequestRicercaComponenteImportiCapitolo() {
			RicercaComponenteImportiCapitolo request = new RicercaComponenteImportiCapitolo();
			request.setDataOra(new Date());
			request.setRichiedente(sessionHandler.getRichiedente());
			request.setAnnoBilancio(sessionHandler.getBilancio().getAnno());
			request.setCapitolo(new CapitoloUscitaPrevisione());
			//request.getCapitolo().setUid(model.getStep1Model().getCapitolo().getUid());
			request.getCapitolo().setUid(model.getStep1Model().getCapitoloImpegno().getUid());
			request.setAbilitaCalcoloDisponibilita(true);
			return request;
		}
		
		/**
		 * calcola la disponibilita' ad impegnare
		 * @return
		 */
		protected BigDecimal disponibilitaImpegnare(){
			//SIAC-7349: la disponibilita ad impegnare deve essere recuperata dalla componente del capitolo
			if(disponibilitaImpegno != null && disponibilitaImpegno.compareTo(BigDecimal.ZERO) == 1){
				return disponibilitaImpegno;
			}else{
				RicercaComponenteImportiCapitolo reqComponenti = creaRequestRicercaComponenteImportiCapitolo();
				RicercaComponenteImportiCapitoloResponse resComponenti = componenteImportiCapitoloService.ricercaComponenteImportiCapitolo(reqComponenti);
				List<ImportiCapitoloPerComponente> importiComponentiCapitolo = new ArrayList<ImportiCapitoloPerComponente>();
				if(resComponenti.hasErrori()){
					//rtilancia errore applicativo
				}else{
					Integer annoImpegno = model.getStep1Model().getAnnoImpegno();
					int annualita = annoImpegno - sessionHandler.getBilancio().getAnno();
					importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponenteSingoloImportiCapitoloPerAnno(resComponenti.getListaImportiCapitolo(), annualita);
					List<ImportiCapitoloPerComponente> componenteImpegno = null;
					int uidComponente = 0;
					if(getImpegnoToUpdate() != null && getImpegnoToUpdate().getComponenteBilancioImpegno() != null && getImpegnoToUpdate().getComponenteBilancioImpegno().getIdTipoComponente() != null)
						uidComponente = getImpegnoToUpdate().getComponenteBilancioImpegno().getIdTipoComponente();
					else if(model.getImpegnoInAggiornamento() != null && model.getImpegnoInAggiornamento().getComponenteBilancioImpegno() != null && model.getImpegnoInAggiornamento().getComponenteBilancioImpegno().getIdTipoComponente() != null)
						uidComponente = model.getImpegnoInAggiornamento().getComponenteBilancioImpegno().getIdTipoComponente();
					if(importiComponentiCapitolo != null && !importiComponentiCapitolo.isEmpty()){
						componenteImpegno = getComponenteImpegno(importiComponentiCapitolo, uidComponente);						
					}	
					if(componenteImpegno != null){
						model.setImportiComponentiCapitolo(componenteImpegno);
						for(ImportiCapitoloPerComponente ci :componenteImpegno){
							if(ci.getTipoDettaglioComponenteImportiCapitolo()!= null &&
								ci.getTipoDettaglioComponenteImportiCapitolo().name().equals(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAIMPEGNARE.name())){
								
								if(annoImpegno.intValue() ==  ci.getDettaglioAnno0().getAnnoCompetenza().intValue()){
									disponibilitaImpegno = ci.getDettaglioAnno0().getImporto();
								}else if(annoImpegno.intValue()  == ci.getDettaglioAnno1().getAnnoCompetenza().intValue() ){
									disponibilitaImpegno = ci.getDettaglioAnno1().getImporto();
								}else if(annoImpegno.intValue()  == ci.getDettaglioAnno2().getAnnoCompetenza().intValue() ){
									disponibilitaImpegno = ci.getDettaglioAnno2().getImporto();
								}else
									disponibilitaImpegno = null;
							}
						}
					}
				}
			}
			
			BigDecimal disponibilitaImpegnareCapitolo;
			if (model.getStep1Model().getAnnoImpegno().intValue() == model.getStep1Model().getCapitoloImpegno().getAnnoCapitolo().intValue() ) {
				disponibilitaImpegnareCapitolo = model.getStep1Model().getCapitoloImpegno().getImportiCapitolo().getDisponibilitaImpegnareAnno1();
			} else if (model.getStep1Model().getAnnoImpegno().intValue() == (model.getStep1Model().getCapitoloImpegno().getAnnoCapitolo().intValue() +1)) {
				disponibilitaImpegnareCapitolo = model.getStep1Model().getCapitoloImpegno().getImportiCapitolo().getDisponibilitaImpegnareAnno2();
			} else if (model.getStep1Model().getAnnoImpegno().intValue() == (model.getStep1Model().getCapitoloImpegno().getAnnoCapitolo().intValue() +2)) {
				disponibilitaImpegnareCapitolo = model.getStep1Model().getCapitoloImpegno().getImportiCapitolo().getDisponibilitaImpegnareAnno3();
			}else{
				// non devono scattare i controlli
				disponibilitaImpegnareCapitolo = null;
			}
			/* SIAC-7349 VG
			 * Come disponibilità a impegnare dobbiamo prendere il minimo tra 
			 * quella della componente (disponibilitaImpegno)
			 * e quella del capitolo (disponibilitaImpegnareCapitolo)
			 */
			if(disponibilitaImpegnareCapitolo!= null && disponibilitaImpegno!= null){
				disponibilitaImpegno = disponibilitaImpegno.min(disponibilitaImpegnareCapitolo);
			}
			
			model.getImpegnoInAggiornamento().setDisponibilitaImpegnareComponente(disponibilitaImpegno);
			return disponibilitaImpegno;
		}
		
		/**
		 * calcola la disponibilita' ad accertare
		 * @return
		 */
		protected BigDecimal disponibilitaAccertare(){
			BigDecimal disponibilitaAccertare =  BigDecimal.ZERO;
				
				if (model.getStep1Model().getAnnoImpegno().intValue() == model.getStep1Model().getCapitoloAccertamento().getAnnoCapitolo().intValue() ) {
					
					disponibilitaAccertare = model.getStep1Model().getCapitoloAccertamento().getImportiCapitolo().getDisponibilitaAccertareAnno1();
					
				} else if (model.getStep1Model().getAnnoImpegno().intValue() == (model.getStep1Model().getCapitoloAccertamento().getAnnoCapitolo().intValue() +1)) {
					
					disponibilitaAccertare = model.getStep1Model().getCapitoloAccertamento().getImportiCapitolo().getDisponibilitaAccertareAnno2();
					
				} else if (model.getStep1Model().getAnnoImpegno().intValue() == (model.getStep1Model().getCapitoloAccertamento().getAnnoCapitolo().intValue() +2)) {
					
					disponibilitaAccertare = model.getStep1Model().getCapitoloAccertamento().getImportiCapitolo().getDisponibilitaAccertareAnno3();
				}else{
					// non devono scattare i controlli
					disponibilitaAccertare = null;
				}
				
				return disponibilitaAccertare;
		}
		
		/**
		 * evento pulsante annulla
		 * @return
		 * @throws Exception
		 */
		public String annulla() throws Exception {
		    return SUCCESS;
		}
		
		
		/**
		 * richiama il servizio di lettura dello storico delle modifche del provvedimento
		 * @param movimento
		 */
		public void leggiStoricoProvvedimentoByMovimento(MovimentoGestione movimento) {
			LeggiStoricoAggiornamentoProvvedimentoMovimentoGestione req = new LeggiStoricoAggiornamentoProvvedimentoMovimentoGestione();
			req.setRichiedente(sessionHandler.getRichiedente());
			req.setMovimento(movimento);
			
			LeggiStoricoAggiornamentoProvvedimentoMovimentoGestioneResponse res = movimentoGestioneFinService.leggiStoricoAggiornamentoProvvedimentoMovimentoGestione(req);
			
			if(res!=null && !res.isFallimento()){
				
				if(res.getStoricoAttoAmministrativi()!=null && !res.getStoricoAttoAmministrativi().isEmpty()){
					model.setListaModificheProvvedimento(res.getStoricoAttoAmministrativi());
				}else{
					model.setListaModificheProvvedimento(new ArrayList<AttoAmministrativoStoricizzato>());
				}
			}
		}
		
		/**
		 * richiama il servizio di lettura dello storico delle modifche della Struttura Competente
		 * @param movimento
		 */
		public void leggiStoricoStrutturaCompetenteByMovimento(MovimentoGestione movimento) {
			LeggiStoricoAggiornamentoStrutturaCompetenteMovimentoGestione req = new LeggiStoricoAggiornamentoStrutturaCompetenteMovimentoGestione();
			req.setRichiedente(sessionHandler.getRichiedente());
			req.setMovimento(movimento);
			
			LeggiStoricoAggiornamentoStrutturaCompetenteMovimentoGestioneResponse res = movimentoGestioneFinService.leggiStoricoAggiornamentoStrutturaCompetenteMovimentoGestione(req);
			
			if(res!=null && !res.isFallimento()){
				
				if(res.getStoricoStrutturaCompetente()!=null && !res.getStoricoStrutturaCompetente().isEmpty()){
					model.setListaModificheStruttureCompetenti(res.getStoricoStrutturaCompetente());
				}else{
					model.setListaModificheStruttureCompetenti(new ArrayList<StrutturaAmmContabileFlatStoricizzato>());
				}
			}
		}
		
		/**
		 * Esegui i controlli prima di eseguire il salvataggio
		 * 
		 * @return
		 */
		public String salvaStep1(boolean byPassDodicesimi){
			
			String risultato = controllaCampiAggiorna1();
			
			if(!"prosegui".equals(risultato)){
				return risultato;
			}
			
			if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO)){
				
				if (isShowPopUpMovColl()) {
					return INPUT;
				}
				// setto il false nel converti cosicche' in caso di errore non va a pulire la TE
				
				AggiornaImpegno requestAggiorna = convertiModelPerChiamataServizioAggiornaImpegni(false,false);
				
				//SALVA SDF:
				if(salvaConSDF()){
					requestAggiorna.getImpegno().setFlagSDF(true);
				}
				
				//Eventuale by pass controllo dodicesimi:
				requestAggiorna.getImpegno().setByPassControlloDodicesimi(byPassDodicesimi);
				//
				
				if(isNecessariaRichiestaConfermaUtentePerRedirezioneSuContabilitaGeneraleAggiornaImpegno()) {
					model.setSaltaInserimentoPrimaNota(false);
					model.setRichiediConfermaRedirezioneContabilitaGenerale(true);
					return INPUT;
				}
				
				if (requestAggiorna.getImpegno().getProgetto() != null) {
					Progetto progetto = requestAggiorna.getImpegno().getProgetto();
					
					progetto.setTipoProgetto(TipoProgetto.GESTIONE);
					progetto.setBilancio(sessionHandler.getBilancio());

					requestAggiorna.getImpegno().setIdCronoprogramma(model.getStep1Model().getIdCronoprogramma());
					requestAggiorna.getImpegno().setIdSpesaCronoprogramma(model.getStep1Model().getIdSpesaCronoprogramma());
				}
				
				
				AggiornaImpegnoResponse response = movimentoGestioneFinService.aggiornaImpegno(requestAggiorna); 
				
				boolean flagPostAggiorna = false;
				
				if(isFallimento(response)){
					
					model.setRichiediConfermaRedirezioneContabilitaGenerale(false);
					
					//per il modale di conferma per bypassare il controllo dodicesimi:
					if(!byPassDodicesimi && presenteSoloErroreDispDodicesimi(response)){

						setShowModaleConfermaSalvaConBypassDodicesimi(true);
						
						return INPUT;
					}
					
					debug(methodName, "Errore nella risposta del servizio");
					addErrori(methodName, response);
					return INPUT;
				}else{
					// se tutto ok allora pulisco veramente la TE
					pulisciTransazioneElementare();
					//APRILE 2016
					//l'aggiorna impegno non richiama piu' ricercaImpegnoPerChiaveOttimizzato (benche' ottimizzato) 
					//e' troppo rischioso perche' l'aggiorna impegno
					//ha richiesto un po' di tempo e qui il timeout e' quasi assicurato con troppi sub...conviene spezzare il caricamento
					//e rieffettuarlo dalla action per evitare timeout
					model.setImpegnoRicaricatoDopoInsOAgg(null);
					model.setVisualizzaWarningImpegnoNonTotVincolato(false);
					flagPostAggiorna = false;
					// Ripulisco i warning, perchè l'impegno è stato salvato!
					
					if(hasPersistentActionWarnings()){
						setActionWarnings(new ArrayList<String>(0));
					}
					
					//SIAC-6000
					model.setUidDaCompletare(response.getRegistrazioneMovFinFIN() != null? response.getRegistrazioneMovFinFIN().getUid() : 0);
					//
					
//					addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
				
				}
				
				model.setRichiediConfermaRedirezioneContabilitaGenerale(false);

				caricaDati(flagPostAggiorna);
				creaMovGestModelCache();
			}else{
				
				if (isShowPopUpMovColl()) {
					return INPUT;
				}
				// setto il false nel converti cosicche' in caso di errore non va a pulire la TE
				AggiornaAccertamento request = convertiModelPerChiamataServizioAggiornaAccertamenti(false);
				
				//FIX: non aggiornava il provvedimento se veniva cambiato:
				//mi tocca fare un fix qui in questa maniera brutta perche' ho notato che c'e' uno spaghetti code bestiale e
				//sicuramente vado in conflitto con altri fix 
				//(sicuramente con questo fix // provvedimento dell'impegno! (fix per jira SIAC-2647) 
				// nel metodo popolaAggiornaAccertamentoRequestPerAggiornaModifiche di MovGestAction)
				if(request!=null && request.getAccertamento()!=null){
					request.getAccertamento().setAttoAmministrativo(popolaProvvedimento(model));
				}
				//END FIX
				
				if(isNecessariaRichiestaConfermaUtentePerRedirezioneSuContabilitaGeneraleAggiornaAccertamento()) {
					model.setSaltaInserimentoPrimaNota(false);
					model.setRichiediConfermaRedirezioneContabilitaGenerale(true);
					return INPUT;
				}
				
				AggiornaAccertamentoResponse response = movimentoGestioneFinService.aggiornaAccertamento(request); 
				
				boolean flagPostAggiorna = false;
				if(isFallimento(response)){
					model.setRichiediConfermaRedirezioneContabilitaGenerale(false);
					debug(methodName, "Errore nella risposta del servizio");
					addErrori(methodName, response);
					return INPUT;
				}else{
					
					// se tutto ok allora pulisco veramente la TE
					pulisciTransazioneElementare();
					
					Accertamento accertamentoReload = response.getAccertamento();
					model.setAccertamentoRicaricatoDopoInsOAgg(accertamentoReload);
					flagPostAggiorna = true;
					
					//SIAC-6000
					model.setUidDaCompletare(response.getRegistrazioneMovFinFIN() != null? response.getRegistrazioneMovFinFIN().getUid() : 0);
					//
					
				}
				
				model.setRichiediConfermaRedirezioneContabilitaGenerale(false);
				
				caricaDatiAccertamento(flagPostAggiorna);
				creaMovGestModelCache();
				
				//SIAC-8065 se trovo errori dal caricaDatiAccertamento torno indietro e li mostro
				if(model.hasErrori()) {
					return INPUT;
				}
			}	
				
			return "salva";
		}
		
		/**
		 * verifica l'abilitazione sul provvedimento
		 * @return
		 */
		protected String provvedimentoConsentito(){
			
			String redirect = null;
			
			if(isSoggettoDecentrato()){
				
				String azioneGestisciDecentratoP = null;
				String azioneGestisciDecentrato = null;
				
				if(oggettoDaPopolareImpegno()){
					azioneGestisciDecentratoP = AzioneConsentitaEnum.OP_SPE_gestisciImpegnoDecentratoP.getNomeAzione();
					azioneGestisciDecentrato = AzioneConsentitaEnum.OP_SPE_gestisciImpegnoDecentrato.getNomeAzione();
				} else {
					azioneGestisciDecentratoP = AzioneConsentitaEnum.OP_ENT_gestisciAccertamentoDecentratoP.getNomeAzione();
					azioneGestisciDecentrato = AzioneConsentitaEnum.OP_ENT_gestisciAccertamentoDecentrato.getNomeAzione();
				}
				
				if(isAzioneAbilitata(azioneGestisciDecentratoP) && compilatoProvvedimento()){
					//il provveddimento, se indicato, deve essere provvisorio
					
					//FIX per SIAC-4067, il provvedimento non compilato non deve generare errore.
					boolean provvedimentoProvvisorio = true;
					if (!MOVGEST_PROVVISORIO.equals(model.getStep1Model().getProvvedimento().getStato())){
						provvedimentoProvvisorio = false;
					}
					if(!provvedimentoProvvisorio){
						if(oggettoDaPopolareImpegno()){
							addErrore(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Inserimento Impegno Decentrato", "Provvisorio"));
						} else {
							addErrore(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Inserimento Accertamento Decentrato", "Provvisorio"));
						}
						return INPUT;
					}
				} 
				
				if(compilatoProvvedimento() && provvedimentoHasSac() && isAzioneAbilitata(azioneGestisciDecentrato)){
					//provvedimneto compilato, ha una struttura amministrativa e sono 
					//in gestione decentrata
					
					//il provvedimento deve avere la mia struttura, ma solo come warning
					if(!controlloSACStrutturaAmmUtenteDecentrato(sessionHandler.getAzione().getNome(), model.getStep1Model().getProvvedimento().getUidStrutturaAmm())){
						if(!model.isProseguiConWarningSacDelProvvPerDecentrato()){
							addPersistentActionWarning(ErroreFin.PROVVEDIMENTO_DECENTRATO_NON_VALIDO.getErrore().getCodice()+" - "+ErroreFin.PROVVEDIMENTO_DECENTRATO_NON_VALIDO.getErrore().getDescrizione());
							model.setProseguiConWarningSacDelProvvPerDecentrato(true);
						}
					}
				}
				
			} else {
				//Controllo che c'era gia':
				if(!controlloSACStrutturaAmmUtenteDecentrato(sessionHandler.getAzione().getNome(), model.getStep1Model().getProvvedimento().getUidStrutturaAmm())){
					addPersistentActionWarning(ErroreFin.PROVVEDIMENTO_DECENTRATO_NON_VALIDO.getErrore().getCodice()+" - "+ErroreFin.PROVVEDIMENTO_DECENTRATO_NON_VALIDO.getErrore().getDescrizione());
					return INPUT;
				}
			}
			
			//SIAC-6929
			ProvvedimentoImpegnoModel pim = new ProvvedimentoImpegnoModel();
			if(model.getStep1Model().getProvvedimento()!=null && model.getStep1Model().getProvvedimento().getUid()!=null &&
					model.getStep1Model().getProvvedimento().getUid().intValue() > 0){
				pim = getProvvedimentoById(model.getStep1Model().getProvvedimento().getUid());
				if(pim.getBloccoRagioneria()!= null && pim.getBloccoRagioneria().booleanValue()){
					addErrore(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore("Numero Provvedimento " + 
							pim.getNumeroProvvedimento() + " Oggetto " + pim.getOggetto()));
					return INPUT;
				}
			}
			
			
			//SIAC-7321
			if(model.getStep1Model().getProvvedimento()!=null){
		    	if(model.getStep1Model().getListaVincoliImpegno()!= null && !model.getStep1Model().getListaVincoliImpegno().isEmpty()){
		    		List<VincoloImpegno> listaVincoli = model.getStep1Model().getListaVincoliImpegno();	
		    		for(VincoloImpegno v : listaVincoli){
		    				if(v.getAccertamento()!= null){
		    					ErroreMovGestModel vincoloNonCorretto = verificaAccertamentoVincolo(v.getAccertamento());
		    					if(vincoloNonCorretto != null){
		    						addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(vincoloNonCorretto.getDescrizione()));
		    						break;
		    					}
		    				}
		    			}
		    	}
		    }
			
			
			
			
			return redirect;
			
		}
		
		/**
		 * Gestisce il caricamento dati dell'impegno tramite chiamata al servizio ricercaImpegnoPerChiaveOttimizzato
		 * oppure tramite flagPostAggiorna utilizzando i dati gia' caricati nel model 
		 * per ridurre il numero di chiamate al servizio
		 * 
		 * @param flagPostAggiorna
		 * @return
		 */
		protected RicercaImpegnoPerChiaveOttimizzatoResponse caricaDati(boolean flagPostAggiorna){
			RicercaImpegnoPerChiaveOttimizzatoResponse response= new RicercaImpegnoPerChiaveOttimizzatoResponse();
			
			if(flagPostAggiorna){
				//SENZA USARE IL SERVIZIO
				response.setImpegno(model.getImpegnoRicaricatoDopoInsOAgg());
				response.setCapitoloUscitaGestione(model.getImpegnoRicaricatoDopoInsOAgg().getCapitoloUscitaGestione());
			}else{
				//RICHIAMANDO IL SERVIZIO
				RicercaImpegnoPerChiaveOttimizzato ricercaModel = convertiModelPerChiamataServizioRicercaPerChiave();
				ricercaModel.setCaricaSub(false);
				response = movimentoGestioneFinService.ricercaImpegnoPerChiaveOttimizzato(ricercaModel);
			}
			
			
			
			if(response!=null){
				
				//CARICAMENTO SUBIMPEGNI
				//APRILE 2016, ottimizzazioni sub:
				List<SubImpegno> elencoSubImpegni = null;
				elencoSubImpegni = response.getElencoSubImpegniTuttiConSoloGliIds();
				//
				
				if(!isEmpty(elencoSubImpegni)){
					model.setListaSubimpegni(elencoSubImpegni);
				}else {
					model.setListaSubimpegni(new ArrayList<SubImpegno>());
				}
				
				GestisciImpegnoStep1Model step1Model = model.getStep1Model();
				//SIAC-8868
				step1Model.getCapitolo().setCodiceStrutturaAmministrativa(null);
				
				if(response.getCapitoloUscitaGestione()!=null){
					
					
					setCapitoloUscitaTrovato(response.getCapitoloUscitaGestione());
					
					if(step1Model.getCapitoloImpegno().getAnnoCapitolo()==null || step1Model.getCapitolo().getCodiceStrutturaAmministrativa() == null){
						step1Model.setCapitoloImpegno(getCapitoloUscitaTrovato());
						step1Model.setCapitoloAccertamento(getCapitoloEntrataTrovato());
						impostaDatiCapitoloDaMovimentoCaricato();
					}	
				}											
	
				Impegno impegno = response.getImpegno();
				
				//SIAC-7349 - Inizio - SR90 - MR - 03/2020 - Ottengo la componente associata all'impegno in esame
				ComponenteBilancioImpegno cbi = null;
				if(impegno.getComponenteBilancioImpegno() == null){
					
					
					
					
					
					
				}else{
					cbi = impegno.getComponenteBilancioImpegno();
					model.getStep1Model().getCapitolo().setComponenteBilancioUid(cbi.getIdTipoComponente());
				}
				
				//SIAC-7349 - Fine				
				
				
				
				
				//SIAC-7349 - Inizio - SR90 - MR - 03/2020 
				/*Chiamata al servizio delle componenti del capitolo associato all'impegno.
				Viene estratta successivamente la componente associata all'impegno.*/
				RicercaComponenteImportiCapitolo reqComponenti = creaRequestRicercaComponenteImportiCapitolo(response.getCapitoloUscitaGestione());
				reqComponenti.setAbilitaCalcoloDisponibilita(true);
				RicercaComponenteImportiCapitoloResponse resComponenti = componenteImportiCapitoloService
						.ricercaComponenteImportiCapitolo(reqComponenti);
					
				List<ImportiCapitoloPerComponente> importiComponentiCapitolo = new ArrayList<ImportiCapitoloPerComponente>();
				if(resComponenti.hasErrori()){
						//TODO lancia errore applicativo
				}else{
					int annualita = getAnnualitaImpegno(impegno.getAnnoMovimento());
					importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper
								.toComponenteSingoloImportiCapitoloPerAnno(resComponenti.getListaImportiCapitolo(), annualita);
					List<ImportiCapitoloPerComponente> componenteCapitoloImpegno = new ArrayList<ImportiCapitoloPerComponente>();
					//ADEGUAMENTO 09/04/2020 per anagrafica componente
					int uidTipoComponente = (impegno.getComponenteBilancioImpegno() != null) ? impegno.getComponenteBilancioImpegno().getIdTipoComponente() : 0;
					if(importiComponentiCapitolo != null && !importiComponentiCapitolo.isEmpty()){
						componenteCapitoloImpegno = getComponenteImpegno(importiComponentiCapitolo, uidTipoComponente);						
					}
					
					model.setImportiComponentiCapitolo(componenteCapitoloImpegno);
					//7349 - Start - MR - 23/04/2020 - Adeguamento per nome componente
					if(cbi!= null && cbi.getDescrizioneTipoComponente() != null){
						model.setNomeComponente(cbi.getDescrizioneTipoComponente());
					}else{
						model.setNomeComponente("N/A");
					}
				}
				//SIAC-7349 - Fine

				//SIAC-8027 per i controlli sugli importi per eventuali modifiche su impegni SDF o non
				//la disponibilita' a subimpegnare e' utilizzata come workaround dalla SIAC-7349
				//nel metodo servizioGestioneSubimpegno()
				model.getImpegnoInAggiornamento().setDisponibilitaImpegnareComponente(model.getDisponibilitaSubImpegnare());
				//

				if(impegno.getAttoAmministrativo()!=null){ 
					provvedimento = impegno.getAttoAmministrativo();
				}
				
				//SETTING DEI DATI DEL PROVVEDIMENTO NEL MODEL:
				impostaDatiProvvedimentoDaMovimentoCaricato();
	
				//SIAC-7838
				step1Model.setSoggettoImpegno(new Soggetto());
				step1Model.setSoggetto(new SoggettoImpegnoModel());
				
				if(impegno.getSoggetto()!=null){ 
					//SIAC-7619
					//rendo null la classeCreditore se la classeSoggetto e' invalidata o non corretta
					classeCreditore = impegno.getClasseSoggetto();
					soggettoCreditore = impegno.getSoggetto();
					step1Model.setSoggettoImpegno(soggettoCreditore);
				}
				//SETTING DATI SOGGETTO:
				impostaDatiSoggettoDaMovimentoCaricato();		
	
				if(impegno!=null){ 
					setImpegnoToUpdate(impegno);
					model.setImpegnoInAggiornamento(impegno);
					// disponibilita a subimpegnare
					model.setDisponibilitaSubImpegnare(impegno.getDisponibilitaSubimpegnare());
					// disponibilita a liquidare
					model.setDisponibilitaLiquidare(impegno.getDisponibilitaLiquidare());
					
					model.setTotaleSubImpegno(impegno.getTotaleSubImpegniBigDecimal());
					
				}
				
				//SIAC-6997
				if(StringUtils.isEmpty(impegno.getStrutturaCompetente())) {
					//se non l'hai trovato significa che l'impegno memorizzato è stato inserito prima delle modifiche effettuate e che hanno aggiunto struttura competente,
					//di conseguenza prima struttura competente non era obbligatorio e quindi nel db è null
					if(step1Model.getCapitoloImpegno() != null && step1Model.getCapitoloImpegno().getStrutturaAmministrativoContabile() != null &&
						step1Model.getCapitoloImpegno().getStrutturaAmministrativoContabile().getUid() > 0){
						step1Model.setStrutturaSelezionataCompetente(String.valueOf(step1Model.getCapitoloImpegno().getStrutturaAmministrativoContabile().getUid()));
					}
				}else {
					step1Model.setStrutturaSelezionataCompetente(impegno.getStrutturaCompetente());
				}
				//fine SIAC-6997
				
				step1Model.setCronoprogramma(impegno.getCronoprogramma());
				step1Model.setIdCronoprogramma(impegno.getIdCronoprogramma());
				step1Model.setIdSpesaCronoprogramma(impegno.getIdSpesaCronoprogramma());
				
				impostaDatiDaImpegnoCaricato();	
				
				// eventuali vincoli
				if(impegno.getVincoliImpegno()!=null && !impegno.getVincoliImpegno().isEmpty()){
					
					step1Model.setListaVincoliImpegno(impegno.getVincoliImpegno());
					
				}
			
			}
			
			return response;
		}
		
		//SIAC-7349 - Inizio - SR90 - MR - 03/2020  per ottenere l'indice di ricerca nella lista degli importi in base all'anno di bilancio e del movimento
		private int getAnnualitaImpegno(int annoMovimento) {
			return annoMovimento - sessionHandler.getBilancio().getAnno();
		
		}
		//SIAC-7349 - Fine

		//SIAC-7349 - Inizio - SR90 - MR - 03/2020 - Metodo per ottenere la componente associata by uid dalla response dell'impegno
		private List<ImportiCapitoloPerComponente> getComponenteImpegno(
				List<ImportiCapitoloPerComponente> importiComponentiCapitolo, int uidTipoComponente) {
			List<ImportiCapitoloPerComponente> impegnatoPerComponente = new ArrayList<ImportiCapitoloPerComponente>();
			if(importiComponentiCapitolo !=null && !importiComponentiCapitolo.isEmpty() && uidTipoComponente>0){
				for(ImportiCapitoloPerComponente icc : importiComponentiCapitolo){
					if(icc.getTipoComponenteImportiCapitolo()!= null && icc.getTipoComponenteImportiCapitolo().getUid()== uidTipoComponente){ 
						impegnatoPerComponente.add(icc);
					}
				}				
			}		
			return impegnatoPerComponente;
		}
		//SIAC-7349 - Fine

		/**
		 * impegno senza disponibilita fondi
		 * @return
		 */
		public boolean isImpegnoSdf(){
			return model.getImpegnoInAggiornamento().isFlagSDF();
		}
		
		/**
		 * Gestisce il caricamento dati dell'accertamento tramite chiamata al servizio ricercaAccertamentoPerChiaveOttimizzato
		 * oppure tramite flagPostAggiorna utilizzando i dati gia' caricati nel model 
		 * per ridurre il numero di chiamate al servizio
		 * 
		 * @param flagPostAggiorna
		 * @return
		 */
		protected void caricaDatiAccertamento(boolean flagPostAggiorna){
			RicercaAccertamentoPerChiaveOttimizzatoResponse response= new RicercaAccertamentoPerChiaveOttimizzatoResponse();
			
			if(flagPostAggiorna){
				//RESPONSE POPOLATA DA DATI NEL MODEL
				response.setAccertamento(model.getAccertamentoRicaricatoDopoInsOAgg());
				response.setCapitoloEntrataGestione(model.getAccertamentoRicaricatoDopoInsOAgg().getCapitoloEntrataGestione());
				//SIAC-6995
				response.setHasStoricizzazioneNellBilancio(model.getConLegameStoricizzato());
				//SIAC-8503
				classeCreditore = model.getAccertamentoRicaricatoDopoInsOAgg().getClasseSoggetto();
				soggettoCreditore = model.getAccertamentoRicaricatoDopoInsOAgg().getSoggetto();
				model.getStep1Model().setSoggettoImpegno(soggettoCreditore);
			}else{
				//RESPONSE POPOLATA DA CHIAMATA AL SERVIZIO
				response = movimentoGestioneFinService.ricercaAccertamentoPerChiaveOttimizzato(convertiModelAccertamentoPerChiamataServizioRicercaPerChiave());
			}
			
			//SIAC-8065 loggo gli errori 
			if(isFallimento(response)) {
				debug("Errore nel servizio di ricerca dell'accertamento");
				addErrori(response);
				return;
			}
			
			if(response != null){
				
				//CARICAMENTO SUBIMPEGNI
				if (response.getAccertamento() != null && !isEmpty(response.getAccertamento().getElencoSubAccertamenti())) {
					model.setListaSubaccertamenti(response.getAccertamento().getElencoSubAccertamenti());	//in attesa dei sub-accertamenti
				}else {
					model.setListaSubaccertamenti(new ArrayList<SubAccertamento>());	//in attesa dei sub-accertamenti
				}
							
				if(response.getCapitoloEntrataGestione()!=null){ 
					setCapitoloEntrataTrovato(response.getCapitoloEntrataGestione());
					
					if(model.getStep1Model().getCapitoloAccertamento().getAnnoCapitolo()==null){
						model.getStep1Model().setCapitoloImpegno(new CapitoloUscitaGestione());
						model.getStep1Model().setCapitoloAccertamento(getCapitoloEntrataTrovato());
						impostaDatiCapitoloDaMovimentoCaricato();
					}	
				}			

				if(response.getAccertamento().getAttoAmministrativo()!=null){ 
					provvedimento = response.getAccertamento().getAttoAmministrativo();
				}
				
				//SETTING DEI DATI DEL PROVVEDIMENTO NEL MODEL:
				impostaDatiProvvedimentoDaMovimentoCaricato();

				if(response.getAccertamento().getSoggetto()!=null){ 
					classeCreditore = response.getAccertamento().getClasseSoggetto();
					soggettoCreditore = response.getAccertamento().getSoggetto();
					model.getStep1Model().setSoggettoImpegno(soggettoCreditore);
				}
				//SETTING DATI SOGGETTO:
				impostaDatiSoggettoDaMovimentoCaricato();		

				if(response.getAccertamento()!=null){ 
					setAccertamentoToUpdate(response.getAccertamento());
					model.setAccertamentoInAggiornamento(response.getAccertamento());
					
					model.setDisponibilitaSubImpegnare(response.getAccertamento().getDisponibilitaSubAccertare());
					model.setTotaleSubImpegno(response.getAccertamento().getTotaleSubAccertamentiBigDecimal());
					
				}
				
				//SIAC-6997
				if(StringUtils.isEmpty(response.getAccertamento().getStrutturaCompetente())) {
					//se non l'hai trovato significa che l'impegno memorizzato è stato inserito prima delle modifiche effettuate e che hanno aggiunto struttura competente,
					//di conseguenza prima struttura competente non era obbligatorio e quindi nel db è null
					if(model.getStep1Model().getCapitoloAccertamento() != null && model.getStep1Model().getCapitoloAccertamento().getStrutturaAmministrativoContabile() != null &&
						model.getStep1Model().getCapitoloAccertamento().getStrutturaAmministrativoContabile().getUid() > 0){
						model.getStep1Model().setStrutturaSelezionataCompetente(String.valueOf(model.getStep1Model().getCapitoloAccertamento().getStrutturaAmministrativoContabile().getUid()));
					}
				}else {
					model.getStep1Model().setStrutturaSelezionataCompetente(response.getAccertamento().getStrutturaCompetente());
				}
				
				model.setConLegameStoricizzato(response.getHasStoricizzazioneNellBilancio());
				
				//COMPLETIAMO IL SETTING NEL MODEL DEI DATI APPENA CARICATI:
				impostaDatiDaAccertamentoCaricato();
				
			}
			
		}
		
		/**
		 * Metodo interno a caricaDati per completare
		 * i setting nel model dei dati dell'impegno appena caricato
		 */
		private void impostaDatiDaImpegnoCaricato(){ 
			setTitolo();
			
			//Gestione stato impegno
			if(provvedimento.getStatoOperativo()!=null){ 	
				//STATO PROVVISORIO
				
				if(provvedimento.getStatoOperativo().equalsIgnoreCase(CostantiFin.MOVGEST_STATO_PROVVISORIO)){ 
					model.getStep1Model().setStato((provvedimento.getStatoOperativo()));					
				} else if(provvedimento.getStatoOperativo().equalsIgnoreCase(CostantiFin.MOVGEST_STATO_DEFINITIVO)){
					//STATO DEFINITIVO
					if(soggettoCreditore!=null){
						model.getStep1Model().setStato(CostantiFin.MOVGEST_STATO_DEFINITIVO);
					} else{
						//STATO DEFINITIVO NON LIQUIDABILE
						model.getStep1Model().setStato(CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE);
					}
				} else {
					model.getStep1Model().setStato(getImpegnoToUpdate().getStatoOperativoMovimentoGestioneSpesa());
				}
			} else{
				model.getStep1Model().setStato(getImpegnoToUpdate().getStatoOperativoMovimentoGestioneSpesa());
			}
			
			//STATO PROVVEDIMENTO PROVVISORIO:
			if(model.getStep1Model().getStato().equalsIgnoreCase(CostantiFin.MOVGEST_STATO_PROVVISORIO)){	
				setProvvedimentoAbilitato(true);
				setProgettoAbilitato(true);
			}
			
			//DESCRIZIONE STATO OPERATIVO:
			model.getStep1Model().setDescrizioneStatoOperativoMovimento(getImpegnoToUpdate().getDescrizioneStatoOperativoMovimentoGestioneSpesa());
			
			//DATA STATO OPERATIVO:
			model.getStep1Model().setDataStatoOperativoMovimento(getImpegnoToUpdate().getDataStatoOperativoMovimentoGestioneSpesa());
			
			//UID
			if(getImpegnoToUpdate().getUid()!=0){
				model.getStep1Model().setUid(getImpegnoToUpdate().getUid());
			}
			
			//ANNO MOVIMENTO
			if(getImpegnoToUpdate().getAnnoMovimento()!=0){
				model.getStep1Model().setAnnoImpegno(getImpegnoToUpdate().getAnnoMovimento());
			}
			
			//NUMERO
			if(getImpegnoToUpdate().getNumeroBigDecimal()!=null){
				model.getStep1Model().setNumeroImpegno(getImpegnoToUpdate().getNumeroBigDecimal().intValue());
			}
					
			if(model.getStep1Model().getStato().equalsIgnoreCase(CostantiFin.MOVGEST_STATO_PROVVISORIO) && getImpegnoToUpdate().getElencoSubImpegni()==null ){	log.debug("","stato movSpesa + sub imp: " + getImpegnoToUpdate().getStatoOperativoMovimentoGestioneSpesa() + " " + getImpegnoToUpdate().getElencoSubImpegni());
				setSoggettoAbilitato(true);}
			
			if(getImpegnoToUpdate().getDescrizione()!=null){
				model.getStep1Model().setOggettoImpegno(getImpegnoToUpdate().getDescrizione());
			}
			
			// aggiunta parere finanziario
			if(getImpegnoToUpdate().getParereFinanziario()!=null){
				model.getStep1Model().setParereFinanziario(getImpegnoToUpdate().getParereFinanziario());
				model.getStep1Model().setParereFinanziarioLoginOperazione(getImpegnoToUpdate().getParereFinanziarioLoginOperazione());
				model.getStep1Model().setParereFinanziarioDataModifica(getImpegnoToUpdate().getParereFinanziarioDataModifica());
			}
			
			//IMPORTO INIZIALE
			if(getImpegnoToUpdate().getImportoIniziale()!=null){
				model.getStep1Model().setImportoImpegno(getImpegnoToUpdate().getImportoIniziale());
				model.getStep1Model().setImportoImpegnoMod(getImpegnoToUpdate().getImportoAttuale());
				model.getStep1Model().setImportoFormattato(convertiBigDecimalToImporto(getImpegnoToUpdate().getImportoAttuale()));
				setImportoAbilitato(true);
			}else if(model.getStep1Model().getImportoImpegnoMod()!=null){
				setImportoAbilitato(true);
				model.getStep1Model().setImportoImpegnoMod(getImpegnoToUpdate().getImportoAttuale());
			}
			
			//DATA SCADENZA
			if(getImpegnoToUpdate().getDataScadenza()!=null){
		    	model.getStep1Model().setScadenza(DateUtility.formatDate(getImpegnoToUpdate().getDataScadenza()));
		    	model.getStep1Model().setScadenzaOld(DateUtility.formatDate(getImpegnoToUpdate().getDataScadenza()));
			}
			
			//CIG
			if(getImpegnoToUpdate().getCig()!=null){
				model.getStep1Model().setCig(getImpegnoToUpdate().getCig());
			}

			//CUP
			if(getImpegnoToUpdate().getCup()!=null){
				model.getStep1Model().setCup(getImpegnoToUpdate().getCup());
			}
			
			//SIOPE PLUS:
			impostaDatiSiopePlusNelModel(getImpegnoToUpdate(), model.getStep1Model());
			//FINE SIOPE PLUS
			
			//PROGETTO
			if(null!=getImpegnoToUpdate().getProgetto() && getImpegnoToUpdate().getProgetto().getCodice()!=null){ 
				if(!StringUtils.isEmpty(getImpegnoToUpdate().getProgetto().getCodice())){
					model.getStep1Model().setProgetto(getImpegnoToUpdate().getProgetto().getCodice());
				} else {
					model.getStep1Model().setProgetto("");
				}					
			}
			
			//FRAZIONABILE
			model.getStep1Model().setFrazionabile(convertiBooleanToFrazionabileString(getImpegnoToUpdate().isFlagFrazionabile()));
			
			//RIACCERTATO
			model.getStep1Model().setRiaccertato(convertiBooleanToString(getImpegnoToUpdate().isFlagDaRiaccertamento()));
			
			//REANNO - SIAC-6997
			model.getStep1Model().setReanno(convertiBooleanToString(getImpegnoToUpdate().isFlagDaReanno()));
			
			// durc
			model.getStep1Model().setSoggettoDurc(convertiBooleanToString(getImpegnoToUpdate().isFlagSoggettoDurc()));
			
			//PRENOTAZIONE
			model.getStep1Model().setPrenotazione(convertiBooleanToString(getImpegnoToUpdate().isFlagPrenotazione()));
			
			//CASSA ECONOMALE
			model.getStep1Model().setCassaEconomale(convertiBooleanToString(getImpegnoToUpdate().isFlagCassaEconomale()));
			
			//PRENOTAZIONE LIQUIDABILE
			model.getStep1Model().setPrenotazioneLiquidabile(getImpegnoToUpdate().isFlagPrenotazioneLiquidabile());
			model.getStep1Model().setHiddenPerPrenotazioneLiquidabile(getImpegnoToUpdate().isFlagPrenotazioneLiquidabile());
				
			//IMPEGNO DA RIACCERTAMENTO:
			if(getImpegnoToUpdate().isFlagDaRiaccertamento() || getImpegnoToUpdate().isFlagDaReanno()){
				if(getImpegnoToUpdate().getAnnoRiaccertato()!=0){
					model.getStep1Model().setAnnoImpRiacc(getImpegnoToUpdate().getAnnoRiaccertato());
				}
				if(getImpegnoToUpdate().getNumeroRiaccertato()!=null){
					model.getStep1Model().setNumImpRiacc(getImpegnoToUpdate().getNumeroRiaccertato().intValue());
				}
			}else{
				model.getStep1Model().setAnnoImpRiacc(null);
				model.getStep1Model().setNumImpRiacc(null);
			}
			
			//ANNO ORIGINE PLURIENNALE
			if(getImpegnoToUpdate().getAnnoOriginePlur()!=0){ 
				model.getStep1Model().setAnnoImpOrigine(getImpegnoToUpdate().getAnnoOriginePlur());
			}
			
			//NUMERO ORIGINE PLURIENNALE
			if(getImpegnoToUpdate().getNumeroOriginePlur()!=null){ 
				model.getStep1Model().setNumImpOrigine(getImpegnoToUpdate().getNumeroOriginePlur().intValue());
			}
			
			//TIPO IMPEGNO
			if(getImpegnoToUpdate().getTipoImpegno()!=null){
				model.getStep1Model().setTipoImpegno(getImpegnoToUpdate().getTipoImpegno().getCodice());
			}
			
			//ANNO FINANZIAMENTO
			if(getImpegnoToUpdate().getAnnoFinanziamento()!=0){
				model.getStep1Model().setAnnoFinanziamento(getImpegnoToUpdate().getAnnoFinanziamento());
			}
			
			//NUMERO ACC FINANZIAMENTO
			if(getImpegnoToUpdate().getNumeroAccFinanziamento()!=0){
				model.getStep1Model().setNumeroFinanziamento(getImpegnoToUpdate().getNumeroAccFinanziamento());
			}
			
			//UID
			model.getStep1Model().setUid(getImpegnoToUpdate().getUid());
			
			//caricamento seconda pagina
			//valori classificazioni
			if(getImpegnoToUpdate().getCodMissione()!=null){
				teSupport.setMissioneSelezionata(getImpegnoToUpdate().getCodMissione());
			}
			
			//COD PROGRAMMA
			if(getImpegnoToUpdate().getCodProgramma()!=null){
				teSupport.setProgrammaSelezionato(getImpegnoToUpdate().getCodProgramma());
			}

			// MACRO AGGREGATO
			if(model.getStep1Model().getCapitolo().getIdMacroAggregato()!=null){
				teSupport.setIdMacroAggregato(model.getStep1Model().getCapitolo().getIdMacroAggregato());
			}
			
			//albero PDC
			if (teSupport.getPianoDeiConti()==null) {
				teSupport.setPianoDeiConti(new ElementoPianoDeiConti());
			}
			 
			//CODICE PIANO DEI CONTI
			if(getImpegnoToUpdate().getCodPdc()!=null){
				teSupport.getPianoDeiConti().setCodice(getImpegnoToUpdate().getCodPdc());
			}
			
			//DESCRIZIONE PIANO DEI CONTI
			if(getImpegnoToUpdate().getDescPdc() != null){
				// codice + descrizione
				teSupport.getPianoDeiConti().setDescrizione(getImpegnoToUpdate().getCodPdc() +" - "+ getImpegnoToUpdate().getDescPdc());
			}
			
			//ID PIANO DEI CONTI
			if(getImpegnoToUpdate().getIdPdc() != null){
				teSupport.getPianoDeiConti().setUid(getImpegnoToUpdate().getIdPdc());
			}
			
			//PDC Padre per creazione albero
			if(model.getStep1Model().getCapitolo().getIdPianoDeiConti() != null){
				teSupport.setIdPianoDeiContiPadrePerAggiorna(model.getStep1Model().getCapitolo().getIdPianoDeiConti());
			}
			
			//CR-2023 eliminare conto economico
			// albero Conto Economico tutti i set che c'erano qui vengono tolti
			
			// SIOPE
			if (getImpegnoToUpdate().getCodSiope()!=null) {
				teSupport.getSiopeSpesa().setCodice(getImpegnoToUpdate().getCodSiope());
				teSupport.setSiopeSpesaCod(getImpegnoToUpdate().getCodSiope());
				teSupport.getSiopeSpesa().setDescrizione(getImpegnoToUpdate().getDescCodSiope());
			}
			
			//ID SIOPE
			if(getImpegnoToUpdate().getIdSiope() != null){
				teSupport.getSiopeSpesa().setUid(getImpegnoToUpdate().getIdSiope());
			}
			
			//COFOG
			if(getImpegnoToUpdate().getCodCofog()!=null){
				teSupport.setCofogSelezionato(getImpegnoToUpdate().getCodCofog());
			}
			
			//CODICE TRANSAZIONE EUROPEA SPESA
			if(getImpegnoToUpdate().getCodTransazioneEuropeaSpesa()!=null){
				teSupport.setTransazioneEuropeaSelezionato(getImpegnoToUpdate().getCodTransazioneEuropeaSpesa());
			}
			
			//CODICE RICORRENTE SPESA
			if(getImpegnoToUpdate().getCodRicorrenteSpesa()!=null){
				teSupport.setRicorrenteSpesaSelezionato(getImpegnoToUpdate().getCodRicorrenteSpesa());
			}
			
			//CODICE CAPITOLO SANITARIO SPESA
			if(getImpegnoToUpdate().getCodCapitoloSanitarioSpesa()!=null){
				teSupport.setPerimetroSanitarioSpesaSelezionato(getImpegnoToUpdate().getCodCapitoloSanitarioSpesa());
			}
			
			//CODICE PROGRAMMA POLITICHE REGIONALI
			if(getImpegnoToUpdate().getCodPrgPolReg()!=null){
				teSupport.setPoliticaRegionaleSelezionato(getImpegnoToUpdate().getCodPrgPolReg());
			}
			
			//CODICE CLASSI GEN 11
			if(getImpegnoToUpdate().getCodClassGen11()!=null){
				teSupport.setClassGenSelezionato1(getImpegnoToUpdate().getCodClassGen11());
			}
			
			//CODICE CLASSI GEN 12
			if(getImpegnoToUpdate().getCodClassGen12()!=null){
				teSupport.setClassGenSelezionato2(getImpegnoToUpdate().getCodClassGen12());
			}
			
			//CODICE CLASSI GEN 13
			if(getImpegnoToUpdate().getCodClassGen13()!=null){
				teSupport.setClassGenSelezionato3(getImpegnoToUpdate().getCodClassGen13());
			}
			
			//CODICE CLASSI GEN 14
			if(getImpegnoToUpdate().getCodClassGen14()!=null){
				teSupport.setClassGenSelezionato4(getImpegnoToUpdate().getCodClassGen14());
			}
			
			//CODICE CLASSI GEN 15
			if(getImpegnoToUpdate().getCodClassGen15()!=null){
				teSupport.setClassGenSelezionato5(getImpegnoToUpdate().getCodClassGen15());
			}
			
			//CODICE SOGGETTO CREDITORE
			if(getImpegnoToUpdate().getSoggetto()!= null && getImpegnoToUpdate().getSoggetto().getCodiceSoggetto()!=null){
				model.getStep1Model().getSoggetto().setCodCreditore(getImpegnoToUpdate().getSoggetto().getCodiceSoggetto());
			}
			
			//IMPORTO INIZIALE
			if(getImpegnoToUpdate().getImportoIniziale() != null){
				model.getStep1Model().setImportoImpegnoIniziale(getImpegnoToUpdate().getImportoIniziale());
			}
			
			//IMPORTO ATTUALE
			if(getImpegnoToUpdate().getImportoAttuale() != null){
				model.getStep1Model().setImportoImpegno(getImpegnoToUpdate().getImportoAttuale());
			}
			
			//STATO
			if(!StringUtils.isEmpty(getImpegnoToUpdate().getStato().name())){
				model.getStep1Model().setStato(getImpegnoToUpdate().getStato().name());
			}
			
			//STATO OPERATIVO
			if(!StringUtils.isEmpty(getImpegnoToUpdate().getStatoOperativoMovimentoGestioneSpesa())){
				model.getStep1Model().setStatoOperativo(getImpegnoToUpdate().getStatoOperativoMovimentoGestioneSpesa());
			}
			
			// somma liquidazione doc per vincoli
			model.setSommaLiquidazioneDoc(getImpegnoToUpdate().getSommaLiquidazioniDoc());
			model.setDisponibilitaImpegnoModifica(getImpegnoToUpdate().getDisponibilitaImpegnoModifica());
			
			//flag attiva gsa:
			model.getStep1Model().setFlagAttivaGsa(convertiBooleanToString(getImpegnoToUpdate().isFlagAttivaGsa()));
			//
			
			//aggiunta 21/02/2020 . SIAC-6997
			if(getImpegnoToUpdate().getStrutturaCompetenteLetta() != null) {
				model.getStep1Model().setStrutturaSelezionataCompetenteDesc(getImpegnoToUpdate().getStrutturaCompetenteLetta().getCodice() + " - " + getImpegnoToUpdate().getStrutturaCompetenteLetta().getDescrizione());
			}else {
				model.getStep1Model().setStrutturaSelezionataCompetenteDesc(null);
			}
			
		}
		
		
		/**
		 * Metodo interno a caricaDatiAccertamento per completare
		 * i setting nel model dei dati dell'accertamento appena caricato
		 */
		private void impostaDatiDaAccertamentoCaricato(){ 
			setTitolo();
			
			//Gestione stato impegno
			if(provvedimento.getStatoOperativo()!=null){
				//STATO PROVVISORIO
				if(provvedimento.getStatoOperativo().equalsIgnoreCase(CostantiFin.STATO_PROVVISORIO)){ 
					model.getStep1Model().setStato((provvedimento.getStatoOperativo()));
				} else if(provvedimento.getStatoOperativo().equalsIgnoreCase("DEFINITIVO")){
					//STATO DEFINITIVO
					if(soggettoCreditore!=null){
						model.getStep1Model().setStato("DEFINITIVO");
					} else{
						//STATO DEFINITIVO NON LIQUIDABILE
						model.getStep1Model().setStato(CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE);
					}
				} else {
					model.getStep1Model().setStato(getAccertamentoToUpdate().getStatoOperativoMovimentoGestioneEntrata());
				}
			} else{
				model.getStep1Model().setStato(getAccertamentoToUpdate().getStatoOperativoMovimentoGestioneEntrata());
			}
			
			//STATO PROVVISORIO:
			if(model.getStep1Model().getStato().equalsIgnoreCase(CostantiFin.STATO_PROVVISORIO)){
				setProvvedimentoAbilitato(true);
				setProgettoAbilitato(true);
			}
			
			//UID		
			if(getAccertamentoToUpdate().getUid()!=0){
				model.getStep1Model().setUid(getAccertamentoToUpdate().getUid());
			}
			
			//ANNO MOVIMENO
			if(getAccertamentoToUpdate().getAnnoMovimento()!=0){
				model.getStep1Model().setAnnoImpegno(getAccertamentoToUpdate().getAnnoMovimento());
			}
			
			//NUMERO MOVIMENTO
			if(getAccertamentoToUpdate().getNumeroBigDecimal()!=null){
				model.getStep1Model().setNumeroImpegno(getAccertamentoToUpdate().getNumeroBigDecimal().intValue());
			}
			
			//DESCRIZIONE STATO OPERATIVO
			model.getStep1Model().setDescrizioneStatoOperativoMovimento(getAccertamentoToUpdate().getDescrizioneStatoOperativoMovimentoGestioneEntrata());
			
			//DATA STATO OPERATIVO
			model.getStep1Model().setDataStatoOperativoMovimento(getAccertamentoToUpdate().getDataStatoOperativoMovimentoGestioneEntrata());
			
			//SOGGETTO ABILITATO
			if(model.getStep1Model().getStato().equalsIgnoreCase("P") && getAccertamentoToUpdate().getElencoSubAccertamenti()==null ){	
				setSoggettoAbilitato(true);
			}
			
			//DESCRIZIONE
			if(getAccertamentoToUpdate().getDescrizione()!=null){
				model.getStep1Model().setOggettoImpegno(getAccertamentoToUpdate().getDescrizione());
			}
			
			//IMPORTO UTILIZZABILE
			if(getAccertamentoToUpdate().getImportoUtilizzabile()!=null){
				model.getStep1Model().setImportoUtilizzabileFormattato(convertiBigDecimalToImporto(getAccertamentoToUpdate().getImportoUtilizzabile()));
			}
			
			//IMPORTO INIZIALE
			if(getAccertamentoToUpdate().getImportoIniziale()!=null){
				model.getStep1Model().setImportoImpegno(getAccertamentoToUpdate().getImportoIniziale());
				model.getStep1Model().setImportoImpegnoMod(getAccertamentoToUpdate().getImportoAttuale());
				model.getStep1Model().setImportoFormattato(convertiBigDecimalToImporto(getAccertamentoToUpdate().getImportoAttuale()));
				setImportoAbilitato(true);
			}else if(model.getStep1Model().getImportoImpegnoMod()!=null){
				setImportoAbilitato(true);
				model.getStep1Model().setImportoImpegnoMod(getAccertamentoToUpdate().getImportoAttuale());
			}
			
			//DATA SCADENZA
			if(getAccertamentoToUpdate().getDataScadenza()!=null){
		    	model.getStep1Model().setScadenza(DateUtility.formatDate(getAccertamentoToUpdate().getDataScadenza()));
		    	model.getStep1Model().setScadenzaOld(DateUtility.formatDate(getAccertamentoToUpdate().getDataScadenza()));
			}
			
			//PROGETTO
			if(null!=getAccertamentoToUpdate().getProgetto() && getAccertamentoToUpdate().getProgetto().getCodice()!=null){ 
				if(!StringUtils.isEmpty(getAccertamentoToUpdate().getProgetto().getCodice())){
					model.getStep1Model().setProgetto(getAccertamentoToUpdate().getProgetto().getCodice());
				} else {
					model.getStep1Model().setProgetto("");
				}					
			}
			
			//RIACCERTATO
			model.getStep1Model().setRiaccertato(convertiBooleanToString(getAccertamentoToUpdate().isFlagDaRiaccertamento()));
			
			//REANNO
			model.getStep1Model().setReanno(convertiBooleanToString(getAccertamentoToUpdate().isFlagDaReanno()));
					
			//FLAG DA RIACCERTAMENTO
			if(getAccertamentoToUpdate().isFlagDaRiaccertamento() || getAccertamentoToUpdate().isFlagDaReanno()){
				
				if(getAccertamentoToUpdate().getAnnoRiaccertato()!=0){
					model.getStep1Model().setAnnoImpRiacc(getAccertamentoToUpdate().getAnnoRiaccertato());
				}
		
				if(getAccertamentoToUpdate().getNumeroRiaccertato()!=null){
					model.getStep1Model().setNumImpRiacc(getAccertamentoToUpdate().getNumeroRiaccertato().intValue());
				}
				
			} else {
				model.getStep1Model().setAnnoImpRiacc(null);
				model.getStep1Model().setNumImpRiacc(null);
			}
			
			//siac-6997
//			model.getStep1Model().setStrutturaSelezionataCompetente(getAccertamentoToUpdate().getStrutturaCompetente());
			//aggiunta 21/02/2020 . SIAC-6997
			if(getAccertamentoToUpdate().getStrutturaCompetenteLetta() != null) {
				model.getStep1Model().setStrutturaSelezionataCompetenteDesc(getAccertamentoToUpdate().getStrutturaCompetenteLetta().getCodice() + " - " + getAccertamentoToUpdate().getStrutturaCompetenteLetta().getDescrizione());
			}else {
				model.getStep1Model().setStrutturaSelezionataCompetenteDesc(null);
			}
			
			//ANNO ORIGINE PLURIENNALE
			if(getAccertamentoToUpdate().getAnnoOriginePlur()!=0){ 
				model.getStep1Model().setAnnoImpOrigine(getAccertamentoToUpdate().getAnnoOriginePlur());
			}
			
			//NUMERO ORIGINE PLURIENNALE
			if(getAccertamentoToUpdate().getNumeroOriginePlur()!=null){
				model.getStep1Model().setNumImpOrigine(getAccertamentoToUpdate().getNumeroOriginePlur().intValue());
			}
			
			//TIPO IMPEGNO
			if(getAccertamentoToUpdate().getTipoImpegno()!=null){
				model.getStep1Model().setTipoImpegno(getAccertamentoToUpdate().getTipoImpegno().getCodice()); 
			}
			
			//UID
			model.getStep1Model().setUid(getAccertamentoToUpdate().getUid());
			
			//caricamento seconda pagina
			//valori classificazioni
			
			//MISSIONE
			if(getAccertamentoToUpdate().getCodMissione()!=null){
				model.setMissioneSelezionata(getAccertamentoToUpdate().getCodMissione());
			}
			
			//COD PROGRAMMA
			if(getAccertamentoToUpdate().getCodProgramma()!=null){
				model.setProgrammaSelezionato(getAccertamentoToUpdate().getCodProgramma());
			}

			//albero PDC
			if (teSupport.getPianoDeiConti()==null) {
				teSupport.setPianoDeiConti(new ElementoPianoDeiConti());
			}
			
			//CODICE PIANO DEI CONTI
			if(getAccertamentoToUpdate().getCodPdc()!=null){				
				teSupport.getPianoDeiConti().setCodice(getAccertamentoToUpdate().getCodPdc());
			}
			
			//DESCRIZIONE PIANO DEI CONTI
			if(getAccertamentoToUpdate().getDescPdc() != null){
				teSupport.getPianoDeiConti().setDescrizione(getAccertamentoToUpdate().getCodPdc()+" - "+getAccertamentoToUpdate().getDescPdc());
			}
			
			//ID PIANO DEI CONTI
			if(model.getAccertamentoInAggiornamento().getIdPdc() != null){
				teSupport.getPianoDeiConti().setUid(model.getAccertamentoInAggiornamento().getIdPdc());
			}
			
			//ripristiniamo l'id macro aggregato:
			if(teSupport.getIdMacroAggregato()==null){
				teSupport.setIdMacroAggregato(model.getStep1Model().getCapitolo().getIdMacroAggregato());
			}
			
			//ID PIANO DEI CONTI CAPITOLO
			if(model.getStep1Model().getCapitolo().getIdPianoDeiConti() != null){
				teSupport.setIdPianoDeiContiPadrePerAggiorna(model.getStep1Model().getCapitolo().getIdPianoDeiConti());
			}
			
			// albero conto economico
			//CR-2023 eliminare conto economico tutti i set che c'erano qui vengono tolti
			
			// siope
			if (getAccertamentoToUpdate().getCodSiope()!=null) {
				teSupport.getSiopeSpesa().setCodice(getAccertamentoToUpdate().getCodSiope());
				teSupport.setSiopeSpesaCod(getAccertamentoToUpdate().getCodSiope());
				teSupport.getSiopeSpesa().setDescrizione(getAccertamentoToUpdate().getDescCodSiope());
			}
			
			//SIOPE
			if(getAccertamentoToUpdate().getIdSiope()!= null){
				teSupport.getSiopeSpesa().setUid(getAccertamentoToUpdate().getIdSiope());
			}
			
			//COFOF
			if(getAccertamentoToUpdate().getCodCofog()!=null){
				teSupport.setCofogSelezionato(getAccertamentoToUpdate().getCodCofog());
			}
			
			//TRANSAZIONE EUROPEA
			if(getAccertamentoToUpdate().getCodTransazioneEuropeaSpesa()!=null){
				teSupport.setTransazioneEuropeaSelezionato(getAccertamentoToUpdate().getCodTransazioneEuropeaSpesa());
			}
			
			//RICORRENTE
			if(getAccertamentoToUpdate().getCodRicorrenteSpesa()!=null){
				teSupport.setRicorrenteEntrataSelezionato(getAccertamentoToUpdate().getCodRicorrenteSpesa());
			}
			
			//CAPITOLO SANITARIO
			if(getAccertamentoToUpdate().getCodCapitoloSanitarioSpesa()!=null){
				teSupport.setPerimetroSanitarioEntrataSelezionato(getAccertamentoToUpdate().getCodCapitoloSanitarioSpesa());
			}
			
			//CODICE PROGRAMMA POLITICHE REGIONALI
			if(getAccertamentoToUpdate().getCodPrgPolReg()!=null){
				teSupport.setPoliticaRegionaleSelezionato(getAccertamentoToUpdate().getCodPrgPolReg());
			}
			
			//CODICE CLASSIFICATORE 16
			if(getAccertamentoToUpdate().getCodClassGen16()!=null){
				teSupport.setClassGenSelezionato6(getAccertamentoToUpdate().getCodClassGen16());
			}
			
			//CODICE CLASSIFICATORE 17
			if(getAccertamentoToUpdate().getCodClassGen17()!=null){
				teSupport.setClassGenSelezionato7(getAccertamentoToUpdate().getCodClassGen17());
			}
			
			//CODICE CLASSIFICATORE 18
			if(getAccertamentoToUpdate().getCodClassGen18()!=null){
				teSupport.setClassGenSelezionato8(getAccertamentoToUpdate().getCodClassGen18());
			}
			
			//CODICE CLASSIFICATORE 19
			if(getAccertamentoToUpdate().getCodClassGen19()!=null){
				teSupport.setClassGenSelezionato9(getAccertamentoToUpdate().getCodClassGen19());
			}
			
			//CODICE CLASSIFICATORE 20
			if(getAccertamentoToUpdate().getCodClassGen20()!=null){
				teSupport.setClassGenSelezionato10(getAccertamentoToUpdate().getCodClassGen20());
			}
			
			//CODICE SOGGETTO
			if(getAccertamentoToUpdate().getSoggetto()!= null && getAccertamentoToUpdate().getSoggetto().getCodiceSoggetto()!=null){
				model.getStep1Model().getSoggetto().setCodCreditore(getAccertamentoToUpdate().getSoggetto().getCodiceSoggetto());
			}
			
			//DISPONIBILITA UTILIZZARE
			if(getAccertamentoToUpdate().getDisponibilitaUtilizzare() != null){
				model.getStep1Model().setDisponibilitaUtilizzare(getAccertamentoToUpdate().getDisponibilitaUtilizzare());
			}
			
			//IMPORTO INIZIALE
			if(getAccertamentoToUpdate().getImportoIniziale() != null){
				model.getStep1Model().setImportoImpegnoIniziale(getAccertamentoToUpdate().getImportoIniziale());
			}
			
			//IMPORTO ATTUALE
			if(getAccertamentoToUpdate().getImportoAttuale() != null){
				model.getStep1Model().setImportoImpegno(getAccertamentoToUpdate().getImportoAttuale());
			}
			
			//STATO
			if(!StringUtils.isEmpty(getAccertamentoToUpdate().getStato().name())){
				model.getStep1Model().setStato(getAccertamentoToUpdate().getStato().name());
			}
			
			//STATO OPERATIVO
			if(!StringUtils.isEmpty(getAccertamentoToUpdate().getStatoOperativoMovimentoGestioneEntrata())){
				model.getStep1Model().setStatoOperativo(getAccertamentoToUpdate().getStatoOperativoMovimentoGestioneEntrata());
			}
			
			//SIAC-8178
			if(!StringUtils.isEmpty(getAccertamentoToUpdate().getCodiceVerbale())){
				model.getStep1Model().setCodiceVerbale(getAccertamentoToUpdate().getCodiceVerbale());;
			}
			
			model.getStep1Model().setFlagFattura(convertiBooleanToString(getAccertamentoToUpdate().isFlagFattura()));
			model.getStep1Model().setFlagCorrispettivo(convertiBooleanToString(getAccertamentoToUpdate().isFlagCorrispettivo()));
			//
			
			//flag attiva gsa:
			model.getStep1Model().setFlagAttivaGsa(convertiBooleanToString(getAccertamentoToUpdate().isFlagAttivaGsa()));
			//
			
		}
		
		/**
		 * Metodo interno a caricaDati e caricaDatiAccertamento
		 * per completare i setting nel model dei dati del capitolo 
		 * dell'impegno o dell'accertamento appena caricati
		 */
		private void impostaDatiCapitoloDaMovimentoCaricato(){
			
			//ANNO
			if(capitoloUscitaTrovato.getAnnoCapitolo()!=null){ 
				model.getStep1Model().getCapitolo().setAnno(capitoloUscitaTrovato.getAnnoCapitolo());
			}else{
				model.getStep1Model().getCapitolo().setAnno(capitoloEntrataTrovato.getAnnoCapitolo());
			}
			
			//NUMERO
			if(capitoloUscitaTrovato.getNumeroCapitolo()!=null){ 
				model.getStep1Model().getCapitolo().setNumCapitolo(capitoloUscitaTrovato.getNumeroCapitolo());
			}else if(capitoloEntrataTrovato.getAnnoCapitolo()!=null && capitoloEntrataTrovato.getNumeroCapitolo()!=null){
				model.getStep1Model().getCapitolo().setNumCapitolo(capitoloEntrataTrovato.getNumeroCapitolo());
			}
			
			//ARTICOLO
			if(capitoloUscitaTrovato.getNumeroArticolo()!=null){ 
				model.getStep1Model().getCapitolo().setArticolo(capitoloUscitaTrovato.getNumeroArticolo());
			}else if(capitoloEntrataTrovato.getAnnoCapitolo()!=null && capitoloEntrataTrovato.getNumeroArticolo()!=null){
				model.getStep1Model().getCapitolo().setArticolo(capitoloEntrataTrovato.getNumeroArticolo());
			}
			
			//UEB
			if(capitoloUscitaTrovato.getNumeroUEB()!=null){ 
				model.getStep1Model().getCapitolo().setUeb(new BigInteger(String.valueOf(capitoloUscitaTrovato.getNumeroUEB())));
			}else if(capitoloEntrataTrovato.getAnnoCapitolo()!=null && capitoloEntrataTrovato.getNumeroUEB()!=null){
				model.getStep1Model().getCapitolo().setUeb(new BigInteger(String.valueOf(capitoloEntrataTrovato.getNumeroUEB())));
			}
			
			//UID
			if(capitoloUscitaTrovato.getUid()!=0){ 
				model.getStep1Model().getCapitolo().setUid(capitoloUscitaTrovato.getUid());
			}else if(capitoloEntrataTrovato.getAnnoCapitolo()!=null && capitoloEntrataTrovato.getUid()!=0){
				model.getStep1Model().getCapitolo().setUid(capitoloEntrataTrovato.getUid());
			}
			
			//TIPO FINANZIAMENTO
			if(capitoloUscitaTrovato.getTipoFinanziamento()!=null){ 
				model.getStep1Model().getCapitolo().setTipoFinanziamento(capitoloUscitaTrovato.getTipoFinanziamento().getDescrizione());
			}else if(capitoloEntrataTrovato.getAnnoCapitolo()!=null && capitoloEntrataTrovato.getTipoFinanziamento()!=null){
				model.getStep1Model().getCapitolo().setTipoFinanziamento(capitoloEntrataTrovato.getTipoFinanziamento().getDescrizione());
			}
			
			//ID MACRO AGGREGATO DA CATEGORIA TIPOLOGIA TITOLO
			if (capitoloEntrataTrovato.getCategoriaTipologiaTitolo() != null) {
				model.getStep1Model().getCapitolo().setIdMacroAggregato(capitoloEntrataTrovato.getCategoriaTipologiaTitolo().getUid());
				teSupport.setIdMacroAggregato(capitoloEntrataTrovato.getCategoriaTipologiaTitolo().getUid());
			}
			
			//PIANO DEI CONTI
			if (capitoloUscitaTrovato.getElementoPianoDeiConti() != null) {
				model.getStep1Model().getCapitolo().setCodicePdcFinanziario(capitoloUscitaTrovato.getElementoPianoDeiConti().getCodice());
				model.getStep1Model().getCapitolo().setDescrizionePdcFinanziario(capitoloUscitaTrovato.getElementoPianoDeiConti().getCodice()+" - "+capitoloUscitaTrovato.getElementoPianoDeiConti().getDescrizione());
				model.getStep1Model().getCapitolo().setIdPianoDeiConti(capitoloUscitaTrovato.getElementoPianoDeiConti().getUid());
			}else if(capitoloEntrataTrovato.getAnnoCapitolo()!=null && capitoloEntrataTrovato.getElementoPianoDeiConti() != null){
				model.getStep1Model().getCapitolo().setCodicePdcFinanziario(capitoloEntrataTrovato.getElementoPianoDeiConti().getCodice());
				model.getStep1Model().getCapitolo().setDescrizionePdcFinanziario(capitoloEntrataTrovato.getElementoPianoDeiConti().getCodice()+" - "+capitoloEntrataTrovato.getElementoPianoDeiConti().getDescrizione());
				model.getStep1Model().getCapitolo().setIdPianoDeiConti(capitoloEntrataTrovato.getElementoPianoDeiConti().getUid());
			}
			
			//MACRO AGGREGATO
			if (capitoloUscitaTrovato.getMacroaggregato() != null) {
				model.getStep1Model().getCapitolo().setIdMacroAggregato(capitoloUscitaTrovato.getMacroaggregato().getUid());
				teSupport.setIdMacroAggregato(capitoloUscitaTrovato.getMacroaggregato().getUid());
			}//non macroaggragato per capEntrata
			
			//MISSIONE
			if (capitoloUscitaTrovato.getMissione() != null) {
				model.getStep1Model().getCapitolo().setCodiceMissione(capitoloUscitaTrovato.getMissione().getCodice());
				model.getStep1Model().getCapitolo().setDescrizioneMissione(capitoloUscitaTrovato.getMissione().getDescrizione());
			}
			
			//PROGRAMMA
			if (capitoloUscitaTrovato.getProgramma() != null) {
				model.getStep1Model().getCapitolo().setCodiceProgramma(capitoloUscitaTrovato.getProgramma().getCodice());
				model.getStep1Model().getCapitolo().setDescrizioneProgramma(capitoloUscitaTrovato.getProgramma().getDescrizione());
				model.getStep1Model().getCapitolo().setIdProgramma(capitoloUscitaTrovato.getProgramma().getUid());
			}
			
			//DESCRIZIONE
			if (capitoloUscitaTrovato.getDescrizione() != null) {
				model.getStep1Model().getCapitolo().setDescrizione(capitoloUscitaTrovato.getDescrizione());
			} else if(capitoloEntrataTrovato.getDescrizione() != null){
				model.getStep1Model().getCapitolo().setDescrizione(capitoloEntrataTrovato.getDescrizione());
			}
		
			//ELEMENTO PIANO DEI CONTI
			if (capitoloUscitaTrovato.getElementoPianoDeiConti() != null) {
				model.getStep1Model().getCapitolo().setElementoPianoDeiConti(capitoloUscitaTrovato.getElementoPianoDeiConti());
			} else if(capitoloEntrataTrovato.getDescrizione() != null){
				model.getStep1Model().getCapitolo().setElementoPianoDeiConti(capitoloEntrataTrovato.getElementoPianoDeiConti());
			}
			
			//TIPO FINANZIAMENTO
			if (capitoloUscitaTrovato.getTipoFinanziamento()!= null) {
				model.getStep1Model().getCapitolo().setFinanziamento(capitoloUscitaTrovato.getTipoFinanziamento());
			} else if(capitoloEntrataTrovato.getDescrizione() != null){
				model.getStep1Model().getCapitolo().setFinanziamento(capitoloEntrataTrovato.getTipoFinanziamento());
			}
			
			//STRUTTURA AMMINISTRATIVA CONTABILE
			if (capitoloUscitaTrovato.getStrutturaAmministrativoContabile()!= null && capitoloUscitaTrovato.getStrutturaAmministrativoContabile().getDescrizione() != null) {
				model.getStep1Model().getCapitolo().setCodiceStrutturaAmministrativa(capitoloUscitaTrovato.getStrutturaAmministrativoContabile().getCodice());
				model.getStep1Model().getCapitolo().setDescrizioneStrutturaAmministrativa(capitoloUscitaTrovato.getStrutturaAmministrativoContabile().getDescrizione());
				model.getStep1Model().getCapitolo().setUidStruttura(capitoloUscitaTrovato.getStrutturaAmministrativoContabile().getUid());
			} else if(capitoloEntrataTrovato.getStrutturaAmministrativoContabile()!= null && capitoloEntrataTrovato.getStrutturaAmministrativoContabile().getDescrizione() != null){
				model.getStep1Model().getCapitolo().setCodiceStrutturaAmministrativa(capitoloEntrataTrovato.getStrutturaAmministrativoContabile().getCodice());
				model.getStep1Model().getCapitolo().setDescrizioneStrutturaAmministrativa(capitoloEntrataTrovato.getStrutturaAmministrativoContabile().getDescrizione());
				model.getStep1Model().getCapitolo().setUidStruttura(capitoloEntrataTrovato.getStrutturaAmministrativoContabile().getUid());
			}
			
			//LISTA IMPORTI CAPITOLO
			if(!isEmpty(capitoloUscitaTrovato.getListaImportiCapitolo())){
				model.getStep1Model().getCapitolo().setImportiCapitoloUG(capitoloUscitaTrovato.getListaImportiCapitolo());
				ImportiCapitoloModel supportImporti;
				for (ImportiCapitoloUG currentImporto : capitoloUscitaTrovato.getListaImportiCapitolo()) {
					supportImporti = new ImportiCapitoloModel();
					supportImporti.setAnnoCompetenza(currentImporto.getAnnoCompetenza());
					supportImporti.setCassa(currentImporto.getStanziamentoCassa());
					supportImporti.setResiduo(currentImporto.getStanziamentoResiduo());
					supportImporti.setCompetenza(currentImporto.getStanziamento());
					model.getStep1Model().getCapitolo().getImportiCapitolo().add(supportImporti);
				}
			} else if(!isEmpty(capitoloEntrataTrovato.getListaImportiCapitolo())){
				model.getStep1Model().getCapitolo().setImportiCapitoloEG(capitoloEntrataTrovato.getListaImportiCapitolo());
				ImportiCapitoloModel supportImporti;
				for (ImportiCapitoloEG currentImporto : capitoloEntrataTrovato.getListaImportiCapitolo()) {
					supportImporti = new ImportiCapitoloModel();
					supportImporti.setAnnoCompetenza(currentImporto.getAnnoCompetenza());
					supportImporti.setCassa(currentImporto.getStanziamentoCassa());
					supportImporti.setResiduo(currentImporto.getStanziamentoResiduo());
					supportImporti.setCompetenza(currentImporto.getStanziamento());
					
					if (currentImporto.getAnnoCompetenza().intValue() == model.getStep1Model().getCapitolo().getAnno().intValue()) {
						model.getStep1Model().getCapitolo().setDisponibileAnno1(capitoloEntrataTrovato.getListaImportiCapitoloEG().get(0).getDisponibilitaAccertareAnno1());
					} else if (currentImporto.getAnnoCompetenza().intValue() == (model.getStep1Model().getCapitolo().getAnno().intValue() + 1)) {
						model.getStep1Model().getCapitolo().setDisponibileAnno2(capitoloEntrataTrovato.getListaImportiCapitoloEG().get(0).getDisponibilitaAccertareAnno2());
					} else {
						model.getStep1Model().getCapitolo().setDisponibileAnno3(capitoloEntrataTrovato.getListaImportiCapitoloEG().get(0).getDisponibilitaAccertareAnno3());
					}
					model.getStep1Model().getCapitolo().getImportiCapitolo().add(supportImporti);
				}
			} 
			
			//seleziono il capitolo per la visualizazzione dei dettagli
			setCapitoloSelezionato(model.getStep1Model().getCapitolo());
			
		}
		
		
		/**
		 * Metodo interno a caricaDati e caricaDatiAccertamento
		 * per completare i setting nel model dei dati del provvedimento 
		 * dell'impegno o dell'accertamento appena caricati
		 */
		private void impostaDatiProvvedimentoDaMovimentoCaricato(){
			//TRAVASIAMO I DATI DALL'OGGETTO ATTO AMMINISTRATIVO AL MODEL DELLA PAGINA
			impostaProvvNelModel(provvedimento, model.getStep1Model().getProvvedimento());
			//MARCHIAMO CHE C'E' UN PROVVEDIMENTO SELEZIONATO
			model.getStep1Model().setProvvedimentoSelezionato(true);
			
			//FIX PER SIAC-3943
			if(model.getStep1Model().getProvvedimento()!=null &&
					NESSUNA_STRUTTURA_AMMINISTRATIVA.equals(model.getStep1Model().getProvvedimento().getCodiceStrutturaAmministrativaPadre()) &&
					!StringUtils.isEmpty(model.getStep1Model().getProvvedimento().getCodiceStrutturaAmministrativa())){
				model.getStep1Model().getProvvedimento().setCodiceStrutturaAmministrativaPadre("");
			}
			//
			
		}
		
		/**
		 * Metodo interno a caricaDati e caricaDatiAccertamento
		 * per completare i setting nel model dei dati del soggetto 
		 * dell'impegno o dell'accertamento appena caricati
		 */
		private void impostaDatiSoggettoDaMovimentoCaricato(){	
			
			//puliamo dal model eventuali dati di classe rimasti in precedenza 
			//per esempio se arrivo da ripeti impegno e ho cambiato da classe a soggetto
			//SIAC-8503, spostato in testa poiche' in alcuni casi mi faceva vedere i dati del soggetto anche se su db non erano presenti
			model.getStep1Model().getSoggetto().setClasse(null);
			model.getStep1Model().getSoggetto().setIdClasse(null);
			model.getStep1Model().getSoggetto().setCodCreditore(null);
			model.getStep1Model().getSoggetto().setCodfisc(null);
			model.getStep1Model().getSoggetto().setDenominazione(null);
			sessionHandler.setParametro(FinSessionParameter.SOGGETTO_ACCERTAMENTO_ORIGINAL, null);
			sessionHandler.setParametro(FinSessionParameter.CLASSE_ACCERTAMENTO_ORIGINAL, null);
			//CODICE
			if(soggettoCreditore != null && soggettoCreditore.getCodiceSoggetto()!=null){ 
				model.getStep1Model().getSoggetto().setCodCreditore(soggettoCreditore.getCodiceSoggetto());
				sessionHandler.setParametro(FinSessionParameter.SOGGETTO_ACCERTAMENTO_ORIGINAL, soggettoCreditore.getCodiceSoggetto());
			}

			//CODICE FISCALE
			if(soggettoCreditore != null && !StringUtils.isEmpty(soggettoCreditore.getCodiceFiscale())){
				model.getStep1Model().getSoggetto().setCodfisc(soggettoCreditore.getCodiceFiscale());
			}

			//DENOMINAZIONE
			if(soggettoCreditore != null && !StringUtils.isEmpty(soggettoCreditore.getDenominazione())){
				model.getStep1Model().getSoggetto().setDenominazione(soggettoCreditore.getDenominazione());
			}
			model.getStep1Model().setSoggettoSelezionato(true);

			if(classeCreditore!=null && classeCreditore.getCodice()!=null){
				model.getStep1Model().getSoggetto().setClasse(classeCreditore.getCodice());
				//SIAC-8503
				sessionHandler.setParametro(FinSessionParameter.CLASSE_ACCERTAMENTO_ORIGINAL, classeCreditore.getCodice());
				model.getStep1Model().getSoggetto().setCodCreditore(null);	
				model.getStep1Model().getSoggetto().setCodfisc(null);
				model.getStep1Model().getSoggetto().setDenominazione(null);
			} 
			
			//Inserisco la descrizione all'interno della classe del soggetto  vedi Jira e issue di laura
			inserisciDescClasseSoggettoAggiorna();
		}
		
		/**
		 * Compone ed imposta il titolo con i dati di impegno o accertamento
		 */
		private void setTitolo(){
			String titolo = "";
			
			if(getImpegnoToUpdate().getAnnoMovimento()!=0){ 
				titolo = titolo + "Impegno " + getImpegnoToUpdate().getAnnoMovimento();} 
			else if(getAccertamentoToUpdate().getAnnoMovimento()!=0){
				titolo = titolo + "Impegno " + getAccertamentoToUpdate().getAnnoMovimento();
			} else {
				titolo = titolo + " anno mancante ";
			}
			
			if(getImpegnoToUpdate().getNumeroBigDecimal()!=null){ 
				titolo = titolo + "/" + getImpegnoToUpdate().getNumeroBigDecimal();} 
			else if(getAccertamentoToUpdate().getNumeroBigDecimal()!=null){
				titolo = titolo + "/" + getAccertamentoToUpdate().getNumeroBigDecimal();
			} else {
				titolo = titolo + " numero mancante ";
			}
			
			if(getImpegnoToUpdate().getImportoAttuale()!=null){ 
				titolo = titolo + " - " + getImpegnoToUpdate().getImportoAttuale();
			}else if(getAccertamentoToUpdate().getImportoAttuale()!=null){
				titolo = titolo + " - " + getAccertamentoToUpdate().getImportoAttuale();
			} else {
				titolo = titolo + " importo mancante ";
			}
			
			if(getImpegnoToUpdate().getStatoOperativoMovimentoGestioneSpesa()!=null){ 
				titolo = titolo + " - stato operativo: " + getImpegnoToUpdate().getStatoOperativoMovimentoGestioneSpesa();
			}else if(getAccertamentoToUpdate().getStatoOperativoMovimentoGestioneEntrata()!=null){ 
				titolo = titolo + " - stato operativo: " + getAccertamentoToUpdate().getStatoOperativoMovimentoGestioneEntrata();
			}else {
				titolo = titolo + " stato mancante ";
			}
			
			model.getStep1Model().setTitolo(titolo);
			
		}
		
		/**
		 * Effettua i controlli sullo stato del provvedimento
		 */
		protected void controllaStatoProvvedimento(){
		
			//controllo stato movimento - provvedimento
		    if (model.getStep1Model().getProvvedimento() != null
					&& model.getStep1Model().getProvvedimento().getStato() != null) {
					
				if (model.getStep1Model().getProvvedimento().getStato().equals(MOVGEST_PROVVISORIO)) {
					if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ACCERTAMENTO)){
						model.getAccertamentoInAggiornamento().setStatoOperativoMovimentoGestioneEntrata("P");
					} else if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO)){
						model.getImpegnoInAggiornamento().setStatoOperativoMovimentoGestioneSpesa("P");
					}					
				
				} else if (model.getStep1Model().getProvvedimento().getStato().equals(STATO_MOVGEST_DEFINITIVO)) {
					
					boolean presenzaClasseSoggetto = (model.getStep1Model().getSoggetto() != null && StringUtils.isNotEmpty(model.getStep1Model().getSoggetto().getClasse()));
					boolean presenzaSoggetto =  ( model.getStep1Model().getSoggetto()!=null && model.getStep1Model().getSoggetto().getCodCreditore()!=null && StringUtils.isNotEmpty(model.getStep1Model().getSoggettoImpegno().getCodiceSoggetto()) );
				    
					if (presenzaClasseSoggetto || presenzaSoggetto) {
				    	 if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ACCERTAMENTO)){	
				    		 model.getAccertamentoInAggiornamento().setStatoOperativoMovimentoGestioneEntrata("D");
				    	 } else if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO)){
				    		 model.getImpegnoInAggiornamento().setStatoOperativoMovimentoGestioneSpesa("D");
				    	 }				    	 
				
				     } else {
				    	 if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ACCERTAMENTO)){
				    		 model.getAccertamentoInAggiornamento().setStatoOperativoMovimentoGestioneEntrata("N");
				    	 } else if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO)){
				    		 model.getImpegnoInAggiornamento().setStatoOperativoMovimentoGestioneSpesa("N");
				    	 }				    	 
				     }
				}
			}else {
				//gestione caso provvedimento si null
				if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ACCERTAMENTO)){
					model.getAccertamentoInAggiornamento().setStatoOperativoMovimentoGestioneEntrata("P");
				} else if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO)){
					model.getImpegnoInAggiornamento().setStatoOperativoMovimentoGestioneSpesa("P");
				}				
			}
		}
		
		/**
		 * Controllo campi all'aggiornamento da salva step 1
		 * @return
		 */
		private String controllaCampiAggiorna1(){

			List<Errore> listaErrori = new ArrayList<Errore>();
			String nomeParametroOmesso = new String();
			
			if(!isFromModaleConfermaSalvaConBypassDodicesimi()){
				setShowPopUpMovColl(false);
				setShowModaleConfermaModificaProvvedimento(false);
				setShowModaleConfermaSalvaModificaVincoli(false);
			}
			
			setProseguiStep1(false);
				
			
			//controllo anno e numero finanziamento
		    if(model.getStep1Model().getAnnoFinanziamento()!=null 
		    		&& model.getStep1Model().getAnnoFinanziamento()>model.getStep1Model().getAnnoImpegno()){
	    		listaErrori.add(ErroreFin.INCONGRUENZA_TRA_I_PARAMETRI_IMPEGNO.getErrore("Anno finanziamento maggiore dell'anno impegno"));
		    }
		    
	    	if(model.getStep1Model().getRiaccertato().equalsIgnoreCase(WebAppConstants.Si)) {
	    		nomeParametroOmesso = "da riaccertamento";
	    	}else if(model.getStep1Model().getReanno().equalsIgnoreCase(WebAppConstants.Si)){
	    		nomeParametroOmesso = "da reimputazione in corso d'anno";
	    	}
		    
			//controllo anno e numero riaccertamento	
			if(model.getStep1Model().getRiaccertato().equalsIgnoreCase(WebAppConstants.Si) || model.getStep1Model().getReanno().equalsIgnoreCase(WebAppConstants.Si)){
				if(model.getStep1Model().getAnnoImpRiacc()==null || model.getStep1Model().getNumImpRiacc()==null){
						listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno e/o Numero impegno " + nomeParametroOmesso));
				} else if(model.getStep1Model().getAnnoImpRiacc().toString().length() < 4 || model.getStep1Model().getAnnoImpRiacc().toString().length() > 5){
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno " + nomeParametroOmesso));
				} else if(model.getStep1Model().getAnnoImpRiacc().compareTo(model.getStep1Model().getAnnoImpegno())>=0){
					
					if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO)){
						//correzione messaggio
						listaErrori.add(ErroreFin.INCONGRUENZA_TRA_I_PARAMETRI_RIACCERTAMENTO.getErrore("Impegno","Impegno"));
					}else{
						listaErrori.add(ErroreFin.INCONGRUENZA_TRA_I_PARAMETRI_RIACCERTAMENTO.getErrore("Accertamento","Accertamento"));
					}	
						
				}else  if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO)){
					
		    		RicercaImpegnoPerChiaveOttimizzato rip = new RicercaImpegnoPerChiaveOttimizzato();
		    		rip.setEnte(sessionHandler.getEnte());
		    		rip.setRichiedente(sessionHandler.getRichiedente());
		    		RicercaImpegnoK k = new RicercaImpegnoK();
		    		//JIRA 630
		    		k.setAnnoEsercizio(model.getStep1Model().getAnnoImpRiacc());
		    		k.setAnnoImpegno(model.getStep1Model().getAnnoImpRiacc());
		    		k.setNumeroImpegno(new BigDecimal(model.getStep1Model().getNumImpRiacc()));
		    		rip.setpRicercaImpegnoK(k);
		    		
		    		RicercaImpegnoPerChiaveOttimizzatoResponse respRk = movimentoGestioneFinService.ricercaImpegnoPerChiaveOttimizzato(rip);
		    		
		    		if(respRk!=null && respRk.getImpegno()!=null){
		    			
		    			if(respRk.getImpegno().getAnnoOriginePlur()!=0 &&
		    					respRk.getImpegno().getNumeroOriginePlur() !=null){
		    				
		    				if(model.getStep1Model().getAnnoImpOrigine() == null && model.getStep1Model().getNumImpOrigine() == null){		    				
		    					model.getStep1Model().setAnnoImpOrigine(respRk.getImpegno().getAnnoOriginePlur());
		    				}
		    				if(null!=respRk.getImpegno().getNumeroOriginePlur()){
		    					model.getStep1Model().setNumImpOrigine(respRk.getImpegno().getNumeroOriginePlur().intValue());	
		    				}
		    			}	
		    			
	    				if(respRk.getImpegno().getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("A")){
	    					listaErrori.add(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA_FIN.getErrore("Impegno"," annullato"));
	    				}
		    		} else{
		    			//jira 945
		    			if (isFromPopup()) {
			    			setShowPopUpMovColl(false);
			    		}else {
		    				setShowPopUpMovColl(true);
		    				addPersistentActionWarning(ErroreCore.AGGIORNAMENTO_CON_CONFERMA_WARN.getErrore().getCodice()+" : "+ErroreCore.AGGIORNAMENTO_CON_CONFERMA_WARN.getErrore("Impegno riaccertato non","presente","l'impegno").getDescrizione());						    			
			    		}
		    		}
		    		
		    	} else {
		    		RicercaAccertamentoPerChiaveOttimizzato rap = new RicercaAccertamentoPerChiaveOttimizzato();
		    		rap.setEnte(sessionHandler.getEnte());
		    		rap.setRichiedente(sessionHandler.getRichiedente());
		    		RicercaAccertamentoK k = new RicercaAccertamentoK();
		    		//JIRA 630
		    		k.setAnnoEsercizio(model.getStep1Model().getAnnoImpRiacc());
		    		k.setAnnoAccertamento(model.getStep1Model().getAnnoImpRiacc());
		    		k.setNumeroAccertamento(new BigDecimal(model.getStep1Model().getNumImpRiacc()));
		    		rap.setpRicercaAccertamentoK(k);
		    		
		    		RicercaAccertamentoPerChiaveOttimizzatoResponse respRk = movimentoGestioneFinService.ricercaAccertamentoPerChiaveOttimizzato(rap);
		    		
		    		if(respRk!=null && respRk.getAccertamento()!=null){
		    			
		    			if(respRk.getAccertamento().getAnnoOriginePlur()!=0 &&
		    					respRk.getAccertamento().getNumeroOriginePlur() !=null){
		    				
		    				
		    				if(model.getStep1Model().getAnnoImpOrigine() == null && model.getStep1Model().getNumImpOrigine() == null){
		    					model.getStep1Model().setAnnoImpOrigine(respRk.getAccertamento().getAnnoOriginePlur());
		    				}
		    				if(null!=respRk.getAccertamento().getNumeroOriginePlur()){
		    					model.getStep1Model().setNumImpOrigine(respRk.getAccertamento().getNumeroOriginePlur().intValue());	
		    				}
		    			}	
		    			
	    				if(respRk.getAccertamento().getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase("A")){
	    					listaErrori.add(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA_FIN.getErrore("Accertamento"," annullato"));
	    				}
		    		} else{
		    			
		    			if (isFromPopup()) {
			    			setShowPopUpMovColl(false);
			    		}else {
		    				setShowPopUpMovColl(true);
		    				addPersistentActionWarning(ErroreCore.AGGIORNAMENTO_CON_CONFERMA_WARN.getErrore().getCodice()+" : "+ErroreCore.AGGIORNAMENTO_CON_CONFERMA_WARN.getErrore("Accertamento riaccertato non","presente","l'aggiornamento").getDescrizione());						    			
			    		}
		    			
		    		}
		    	}
			} else {

				model.getStep1Model().setAnnoImpRiacc(null);
		    	model.getStep1Model().setNumImpRiacc(null);
	    		model.getStep1Model().setRiaccertato(WebAppConstants.No);
	    		model.getStep1Model().setReanno(WebAppConstants.No);

			}
		    
			//controllo anno e numero origine
			if(!(model.getStep1Model().getAnnoImpOrigine()==null && model.getStep1Model().getNumImpOrigine()==null)){
				if(!(model.getStep1Model().getAnnoImpOrigine()!=null && model.getStep1Model().getNumImpOrigine()!=null)){
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno e/o Numero impegno origine"));
				}else if(model.getStep1Model().getAnnoImpOrigine().toString().length() < 4 || model.getStep1Model().getAnnoImpOrigine().toString().length() > 5){
					listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno Impegno di origine"));
				}else if(model.getStep1Model().getAnnoImpOrigine().compareTo(model.getStep1Model().getAnnoImpegno())>=0){										
					//correzione mex
					listaErrori.add(ErroreFin.INCONGRUENZA_TRA_I_PARAMETRI_RIACCERTAMENTO.getErrore(model.getLabels().get(LABEL_OGGETTO_GENERICO_PADRE),model.getLabels().get(LABEL_OGGETTO_GENERICO_PADRE)));
				}else if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO)){
					RicercaImpegnoPerChiaveOttimizzato rip = new RicercaImpegnoPerChiaveOttimizzato();
		    		rip.setEnte(sessionHandler.getEnte());
		    		rip.setRichiedente(sessionHandler.getRichiedente());
		    		RicercaImpegnoK k = new RicercaImpegnoK();
		    		// Jira - 630
		    		k.setAnnoEsercizio(model.getStep1Model().getAnnoImpOrigine());		    		
		    		k.setAnnoImpegno(model.getStep1Model().getAnnoImpOrigine());
		    		k.setNumeroImpegno(new BigDecimal(model.getStep1Model().getNumImpOrigine()));
		    		rip.setpRicercaImpegnoK(k);
		    		
		    		//MARZO 2016, OTTIMIZZAZIONI:
		    		rip.setCaricaSub(false);
		    		//
		    		
	    			//jira 945
		    		if (isFromPopup()) {
		    			setShowPopUpMovColl(false);
		    		}else {
		    			RicercaImpegnoPerChiaveOttimizzatoResponse respRk = movimentoGestioneFinService.ricercaImpegnoPerChiaveOttimizzato(rip);

		    			if(respRk==null || respRk.getImpegno()==null){
		    				setShowPopUpMovColl(true);
		    				addPersistentActionWarning(ErroreCore.AGGIORNAMENTO_CON_CONFERMA_WARN.getErrore().getCodice()+" : "+ErroreCore.AGGIORNAMENTO_CON_CONFERMA_WARN.getErrore("Impegno origine non","presente","l'aggiornamento").getDescrizione());
		    			}	
		    		}
				} else {
					RicercaAccertamentoPerChiaveOttimizzato rap = new RicercaAccertamentoPerChiaveOttimizzato();
					rap.setEnte(sessionHandler.getEnte());
		    		rap.setRichiedente(sessionHandler.getRichiedente());
		    		RicercaAccertamentoK k = new RicercaAccertamentoK();
		    		// Jira - 630
		    		k.setAnnoEsercizio(model.getStep1Model().getAnnoImpOrigine());
		    		k.setAnnoAccertamento(model.getStep1Model().getAnnoImpOrigine());
		    		k.setNumeroAccertamento(new BigDecimal(model.getStep1Model().getNumImpOrigine()));
		    		rap.setpRicercaAccertamentoK(k);
		    		
		    		if (isFromPopup()) {
		    			setShowPopUpMovColl(false);
		    		}else {
		    			RicercaAccertamentoPerChiaveOttimizzatoResponse respRk = movimentoGestioneFinService.ricercaAccertamentoPerChiaveOttimizzato(rap);

		    			if(respRk==null || respRk.getAccertamento()==null){
		    				setShowPopUpMovColl(true);
		    				addPersistentActionWarning(ErroreCore.AGGIORNAMENTO_CON_CONFERMA_WARN.getErrore().getCodice()+" : "+ErroreCore.AGGIORNAMENTO_CON_CONFERMA_WARN.getErrore("Accertamento origine non","presente","l'aggiornamento").getDescrizione());
		    			}

		    		}
				}
			}
			
			//controllo data scadenza
			if(!StringUtils.isEmpty(model.getStep1Model().getScadenzaOld())){
				if(StringUtils.isEmpty(model.getStep1Model().getScadenza())){
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Data Scadenza dd/MM/yyyy"));
				} else {
					if(model.getStep1Model().getScadenza().length() != 10){
						listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
					} else {
						if (!DateUtility.isDate(model.getStep1Model().getScadenza(), "dd/MM/yyyy")) {
							listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
						} else {
							
							String annoScadenza = model.getStep1Model().getScadenza().split("/")[2];
							if(Integer.valueOf(annoScadenza) > model.getStep1Model().getAnnoImpegno() || Integer.valueOf(annoScadenza) < model.getStep1Model().getAnnoImpegno()){
								String tipoMovGest = oggettoDaPopolare.toString();
		  						Integer annoMovGest = model.getStep1Model().getAnnoImpegno();
		  						listaErrori.add(ErroreCore.PARAMETRO_ERRATO.getErrore("Data Scadenza deve essere nell'anno dell'"+ tipoMovGest , model.getStep1Model().getScadenza(), String.valueOf(annoMovGest)));
							}
							
						}
					}
				}
			} else {
				if(!StringUtils.isEmpty(model.getStep1Model().getScadenza())){
					if(model.getStep1Model().getScadenza().length() != 10){
						listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
					} else {
						if (!DateUtility.isDate(model.getStep1Model().getScadenza(), "dd/MM/yyyy")) {
							listaErrori.add(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
						} else {
							String annoScadenza = model.getStep1Model().getScadenza().split("/")[2];
							if(Integer.valueOf(annoScadenza) > model.getStep1Model().getAnnoImpegno() || Integer.valueOf(annoScadenza) < model.getStep1Model().getAnnoImpegno()){
								String tipoMovGest = oggettoDaPopolare.toString();
		  						Integer annoMovGest = model.getStep1Model().getAnnoImpegno();
		  						listaErrori.add(ErroreCore.PARAMETRO_ERRATO.getErrore("Data Scadenza deve essere nell'anno dell'"+ tipoMovGest , model.getStep1Model().getScadenza(), String.valueOf(annoMovGest)));
							}
						}
					}
				}
			}
			
			//controllo stato movimento con soggetto
			if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ACCERTAMENTO)){
				//controllo stato movgest
				if(model.getAccertamentoInAggiornamento().getStatoOperativoMovimentoGestioneEntrata().equals("D")){
					//controllo modifica classe
					if(model.getAccertamentoInAggiornamento().getClasseSoggetto()!=null 
							&& !StringUtils.isEmpty(model.getAccertamentoInAggiornamento().getClasseSoggetto().getCodice())){
						
							//controllo esistenza modifica classe con nuova classe
							if(model.getStep1Model().getSoggetto()!=null
								&&	model.getStep1Model().getSoggetto().getClasse()!=null 
								&& !model.getStep1Model().getSoggetto().getClasse().equals(model.getAccertamentoInAggiornamento().getClasseSoggetto().getCodice())){
									listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Modifica Soggetto non consentita, stato Movimento Valido"));
							} else if(model.getStep1Model().getSoggetto()!=null
								&& model.getStep1Model().getSoggetto().getCodCreditore()!=null){
								//controllo esistenza modifica classe con nuovo soggetto
								
									if(model.getAccertamentoInAggiornamento().getSoggetto()!=null
										&& !StringUtils.isEmpty(model.getAccertamentoInAggiornamento().getSoggetto().getCodiceSoggetto())
										&& !model.getStep1Model().getSoggetto().getCodCreditore().equals(model.getAccertamentoInAggiornamento().getSoggetto().getCodiceSoggetto())){
										
											listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Modifica Soggetto non consentita, stato Movimento Valido"));
									}
							}
					} else if(model.getAccertamentoInAggiornamento().getSoggetto()!=null
							&& !StringUtils.isEmpty(model.getAccertamentoInAggiornamento().getSoggetto().getCodiceSoggetto())){
						//controllo modifica soggetto
							
							//controllo esistenza modifica soggetto con nuovo soggetto
							if(model.getStep1Model().getSoggetto()!=null
								&& model.getStep1Model().getSoggetto().getCodCreditore()!=null	
								&& !model.getStep1Model().getSoggetto().getCodCreditore().equals(model.getAccertamentoInAggiornamento().getSoggetto().getCodiceSoggetto())){
									listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Modifica Soggetto non consentita, stato Movimento Valido"));
							} else if(model.getStep1Model().getSoggetto()!=null
								&& !StringUtils.isEmpty(model.getStep1Model().getSoggetto().getClasse())){
								//controllo esistenza modifica soggetto con nuova classe
								
								if(model.getAccertamentoInAggiornamento().getClasseSoggetto()!=null 
									&& !StringUtils.isEmpty(model.getAccertamentoInAggiornamento().getClasseSoggetto().getCodice())
									&& !model.getStep1Model().getSoggetto().getClasse().equals(model.getAccertamentoInAggiornamento().getClasseSoggetto().getCodice())){
								
										listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Modifica Soggetto non consentita, stato Movimento Valido"));
								}
							}
							
						}
					}
			}else{
					if(model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa().equals("D")){
						//controllo modifica classe
						if(model.getImpegnoInAggiornamento().getClasseSoggetto()!=null 
								&& !StringUtils.isEmpty(model.getImpegnoInAggiornamento().getClasseSoggetto().getCodice())){
							
								//controllo esistenza modifica classe con nuova classe
								if(model.getStep1Model().getSoggetto()!=null
									&&	model.getStep1Model().getSoggetto().getClasse()!=null 
									&& !model.getStep1Model().getSoggetto().getClasse().equals(model.getImpegnoInAggiornamento().getClasseSoggetto().getCodice())){
									listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Modifica Soggetto non consentita, stato Movimento Valido"));
								}
								//controllo esistenza modifica classe con nuovo soggetto
								else if(model.getStep1Model().getSoggetto()!=null
									&& model.getStep1Model().getSoggetto().getCodCreditore()!=null){
									
										if(model.getImpegnoInAggiornamento().getSoggetto()!=null
											&& !StringUtils.isEmpty(model.getImpegnoInAggiornamento().getSoggetto().getCodiceSoggetto())
											&& !model.getStep1Model().getSoggetto().getCodCreditore().equals(model.getImpegnoInAggiornamento().getSoggetto().getCodiceSoggetto())){
											
												listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Modifica Soggetto non consentita, stato Movimento Valido"));
										}
								}
						} else if(model.getImpegnoInAggiornamento().getSoggetto()!=null
								&& !StringUtils.isEmpty(model.getImpegnoInAggiornamento().getSoggetto().getCodiceSoggetto())){
							//controllo modifica soggetto
								
								//controllo esistenza modifica soggetto con nuovo soggetto
								if(model.getStep1Model().getSoggetto()!=null
									&& model.getStep1Model().getSoggetto().getCodCreditore()!=null	
									&& !model.getStep1Model().getSoggetto().getCodCreditore().equals(model.getImpegnoInAggiornamento().getSoggetto().getCodiceSoggetto())){
										listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Modifica Soggetto non consentita, stato Movimento Valido"));
								}
								//controllo esistenza modifica soggetto con nuova classe
								else if(model.getStep1Model().getSoggetto()!=null
									&& !StringUtils.isEmpty(model.getStep1Model().getSoggetto().getClasse())){
									
									if(model.getImpegnoInAggiornamento().getClasseSoggetto()!=null 
										&& !StringUtils.isEmpty(model.getImpegnoInAggiornamento().getClasseSoggetto().getCodice())
										&& !model.getStep1Model().getSoggetto().getClasse().equals(model.getImpegnoInAggiornamento().getClasseSoggetto().getCodice())){
									
											listaErrori.add(ErroreFin.INCONGRUENZA_NEI_PARAMETRI_.getErrore("Modifica Soggetto non consentita, stato Movimento Valido"));
									}
								}								
						}
						
					}
			}
			
			// CR SIAC-3224, in caso di aggiornamento il provvedimento si puo modificare
			if (isFromModaleConfermaModificaProvvedimento()) {
				setShowModaleConfermaModificaProvvedimento(false);
    		}else{
				if(verificaModificaProvvedimento(true)){
					return INPUT;
				}
			}
			
			//controllo formattazione importo
			if(model.getStep1Model().getImportoFormattato() == null || 
				model.getStep1Model().getImportoFormattato().equals("0") ||
				model.getStep1Model().getImportoFormattato().equals("")){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo "+model.getLabels().get(LABEL_OGGETTO_GENERICO_PADRE)));
			}else{
				try{
					// converto
				    model.getStep1Model().setImportoImpegno(convertiImportoToBigDecimal(model.getStep1Model().getImportoFormattato()));
				    
				    if(abilitaModificaImporto!=null && abilitaModificaImporto){
				    	//Strettamente minore da errore
			    	   if (model.getStep1Model().getImportoImpegno().compareTo(BigDecimal.ZERO) < 0) {
					      listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo "+model.getLabels().get(LABEL_OGGETTO_GENERICO_PADRE)));
			    		}
			    	} else {
			    		//Minore o uguale da errore
		    		   if (model.getStep1Model().getImportoImpegno().compareTo(BigDecimal.ZERO) <= 0) {
					      listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo "+model.getLabels().get(LABEL_OGGETTO_GENERICO_PADRE)));
			    		}
			    	}
		    	}catch(Exception e){
		    		if(oggettoDaPopolareImpegno()){
		    			listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Importo Impegno ", "numerico"));
				    }else{
				    	 listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Importo Accertamento ", "numerico"));
				    }
				}
			}
			

			if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO)){
				
				// quindi se vincoli modificati & disponibilità capitolo < 0
			    if (isFromModaleConfermaSalvaModificaVincoli()) {
			    	setShowModaleConfermaSalvaModificaVincoli(false);
			     }else{
		    	 
					if(verificaModificaImportoVincoli(true))
						return INPUT;
						
				} 
		    	 
		    }
		    
			
			//controllo disponibilita capitolo
			if(controlloImporti() && !model.getStep1Model().getImprtoVincoliModificato()){
		    	if(oggettoDaPopolareImpegno()){
		    		if(!salvaConSDF()){
		    			listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE_IMPEGNO.getErrore("impegno", "AGGIORNAMENTO"));
		    		}
		    	}else{
		    		//SIAC-7668
		    		if (isFromPopup() || model.isRichiediConfermaRedirezioneContabilitaGenerale()) {
		    			setShowPopUpMovColl(false);
		    		}else {
		    			setShowPopUpMovColl(Boolean.TRUE);
		    			addPersistentActionWarning(ErroreFin.WARNING_GENERICO.getErrore().getCodice()+" : "+ErroreFin.WARNING_GENERICO.getErrore("Accertamento con disponibilita' insufficiente. Si vuole procedere con l'aggiornamento?").getDescrizione());
		    			return INPUT;
		    		}
		    	}
		    }
			
			//Controllo dello stato del Bilancio
			if(oggettoDaPopolareImpegno()){
				if(controlloStatoBilancio(model.getStep1Model().getAnnoImpegno(),doveMiTrovo,"impegno")){
					return INPUT;
				}
			}else{			
				if(controlloStatoBilancio(model.getStep1Model().getAnnoImpegno(),doveMiTrovo,"accertamento")){
					return INPUT;
				}
			}
			
	 		//verifico l'esistenza del progetto
		    if(StringUtils.isNotEmpty(model.getStep1Model().getProgetto())){
		    	EsistenzaProgetto ep = new EsistenzaProgetto();
		    	ep.setCodiceProgetto(model.getStep1Model().getProgetto());
		    	ep.setRichiedente(sessionHandler.getRichiedente());
		    	ep.setBilancio(sessionHandler.getBilancio());
		    	ep.setCodiceTipoProgetto(TipoProgetto.GESTIONE.getCodice());
		    	
		    	EsistenzaProgettoResponse esistenzaResp = genericService.cercaProgetto(ep);
		    	if(esistenzaResp.isFallimento() && !esistenzaResp.isEsisteProgetto()){
	    			listaErrori.add(ErroreCore.ENTITA_INESISTENTE.getErrore("codice progetto", model.getStep1Model().getProgetto()));
		    	}
		    }
			
			if(listaErrori.isEmpty()) {
				setFromPopup(false);
				
				   return "prosegui";
			} else {
				   addErrori(listaErrori);
				   return INPUT;
			}				
			
		}

		/**
		 * 
		 */
		public boolean verificaModificaImportoVincoli(boolean arrivoDaSalva) {
			
			boolean vincoliAggiornati = false;
			boolean capitoloSenzaDisponibilita =  disponibilitaImpegnare()!=null && disponibilitaImpegnare().compareTo(BigDecimal.ZERO) < 0;
			
			// NUOVA
			model.getStep1Model().getListaVincoliImpegno();
			
			model.getImpegnoInAggiornamento().getVincoliImpegno();
			 
			BigDecimal nuovoImportoVincoliTot = BigDecimal.ZERO; 
			
			if(model.getStep1Model().getListaVincoliImpegno()!=null && !model.getStep1Model().getListaVincoliImpegno().isEmpty()){
				for (VincoloImpegno vincolo : model.getStep1Model().getListaVincoliImpegno()) {
					nuovoImportoVincoliTot = nuovoImportoVincoliTot.add(vincolo.getImporto());
				}
			}
			
			
			BigDecimal oldImportoVincoliTot = BigDecimal.ZERO;
			
			if(model.getStep1ModelCache().getListaVincoliImpegno()!=null && !model.getStep1ModelCache().getListaVincoliImpegno().isEmpty()){
				for (VincoloImpegno vincoloOld : model.getStep1ModelCache().getListaVincoliImpegno()) {
					
					oldImportoVincoliTot = oldImportoVincoliTot.add(vincoloOld.getImporto());
				}
			}
			 
			
			if(nuovoImportoVincoliTot.compareTo(BigDecimal.ZERO) > 0 && nuovoImportoVincoliTot.compareTo(oldImportoVincoliTot) !=0 && capitoloSenzaDisponibilita){
				
				vincoliAggiornati = true;
				model.getStep1Model().setMessaggioDiConfermaModificaVincoli(ErroreFin.CONFERMA_AGGIORNAMENTO_VINCOLO_CON_CAPITOLO_SENZA_DISPONIBILITA.getErrore().getCodice()+": " + ErroreFin.CONFERMA_AGGIORNAMENTO_VINCOLO_CON_CAPITOLO_SENZA_DISPONIBILITA.getErrore().getDescrizione());
				model.getStep1Model().setImprtoVincoliModificato(true);
				
				if(arrivoDaSalva){
					setShowModaleConfermaSalvaModificaVincoli(true);
				}else{
					setShowModaleConfermaProseguiModificaVincoli(true);
				}
			}else model.getStep1Model().setImprtoVincoliModificato(false);
			
			return vincoliAggiornati;
		}
		
		/**
		 * salva con flag sdf
		 * @return
		 */
		public boolean salvaConSDF() {
			if(oggettoDaPopolareImpegno()){
				//vale solo per impegni
				
				boolean erroreImportoCapitolo = controlloImporti();
				if(erroreImportoCapitolo){
					
					//abilitazione all'azione OP-SPE-gestisciImpegnoSDF
					boolean abilitatoAzioneGestisciImpegnoSDF = abilitatoAzioneGestisciImpegnoSDF();
					
					if(abilitatoAzioneGestisciImpegnoSDF){
						return true;
					}
				}
			}
			return false;
		}
		
		/**
		 * per controllare se l'impegno salvato con sdf = false potra' essere salvato come SDF
		 * @return
		 */
		public boolean salvaDaNormaleASDF() {
			if(oggettoDaPopolareImpegno() && !model.getImpegnoInAggiornamento().isFlagSDF()){
				//vale solo per impegni
				boolean erroreImportoCapitolo = controlloImporti();
				if(erroreImportoCapitolo){
					
					//abilitazione all'azione OP-SPE-gestisciImpegnoSDF
					boolean abilitatoAzioneGestisciImpegnoSDF = abilitatoAzioneGestisciImpegnoSDF();
					
					if(abilitatoAzioneGestisciImpegnoSDF){
						return true;
					}
				}
			}
			return false;
		}
		
		/**
		 * Quando un impegno che e' salvato come SDF ritorna dentro la disponibilita del capitolo
		 * @return
		 */
		public boolean salvaDaSDFANormale() {
			if(oggettoDaPopolareImpegno() && model.getImpegnoInAggiornamento().isFlagSDF()){
				//vale solo per impegni
				boolean erroreImportoCapitolo = controlloImporti();
				if(!erroreImportoCapitolo){
					return true;
				}
			}
			return false;
		}

		/**
		 * CR SIAC-3224, in caso di aggiornamento il provvedimento si puo modificare
		 * 
		 * @return
		 */
		public boolean verificaModificaProvvedimento(boolean arrivoDaSalva) {
			
			boolean provvedimendoCambiato = false;
			
			String statoOperativoMovimento = "";
			Integer annoProvvedimento = 0;
			Integer numeroProvvedimento = 0;
			Integer idTipoatto = 0; 
			Integer idSac = 0; 
			
			String msgPerMovimentiCollegatiAOrdinativiOLiquidazioni ="";
			
			boolean provvedimentoOldEsistente = false;
			
			if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.ACCERTAMENTO)){
				
				provvedimentoOldEsistente = model.getAccertamentoInAggiornamento().getAttoAmministrativo()!=null ? true: false;
				
				if(!provvedimentoOldEsistente)
					return provvedimendoCambiato ;
				
				statoOperativoMovimento = model.getAccertamentoInAggiornamento().getStatoOperativoMovimentoGestioneEntrata();
				
				// vecchio provvedimento
				annoProvvedimento = model.getAccertamentoInAggiornamento().getAttoAmministrativo().getAnno();
				numeroProvvedimento =  model.getAccertamentoInAggiornamento().getAttoAmministrativo().getNumero();
				idTipoatto = model.getAccertamentoInAggiornamento().getAttoAmministrativo().getTipoAtto().getUid();
				idSac =  (model.getAccertamentoInAggiornamento().getAttoAmministrativo().getStrutturaAmmContabile()!=null ? model.getAccertamentoInAggiornamento().getAttoAmministrativo().getStrutturaAmmContabile().getUid() : 0);
			
				//jira siac-4188
				boolean accertamentoConOrdinativi = false;
				
				RicercaReversaliByAccertamento req = new  RicercaReversaliByAccertamento();
				req.setRichiedente(sessionHandler.getRichiedente());
				req.setEnte(sessionHandler.getEnte());
				req.setUidMovimento(model.getAccertamentoInAggiornamento().getUid());
				
				RicercaReversaliByAccertamentoResponse res = movimentoGestioneFinService.ricercaReversaliByAccertamento(req);
								
				if(res!=null && !res.isFallimento()){
					
					accertamentoConOrdinativi = res.getReversaliCollegateAlMovimento();
				}
					
				
				if(accertamentoConOrdinativi){
					msgPerMovimentiCollegatiAOrdinativiOLiquidazioni = ErroreFin.CONFERMA_AGGIORNAMENTO_PROVVEDIMENTO_CON_MOVIMENTO_CON_ORDINATIVI.getMessaggio();
				}
				
			}else if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO)){
				
				provvedimentoOldEsistente = model.getImpegnoInAggiornamento().getAttoAmministrativo()!=null ? true: false;
				
				if(!provvedimentoOldEsistente)
					return provvedimendoCambiato ;
				
				statoOperativoMovimento = model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa();
				
				// vecchio provvedimento
				annoProvvedimento = model.getImpegnoInAggiornamento().getAttoAmministrativo().getAnno();
				numeroProvvedimento =  model.getImpegnoInAggiornamento().getAttoAmministrativo().getNumero();
				idTipoatto = model.getImpegnoInAggiornamento().getAttoAmministrativo().getTipoAtto().getUid();
				idSac =  (model.getImpegnoInAggiornamento().getAttoAmministrativo().getStrutturaAmmContabile()!=null ? model.getImpegnoInAggiornamento().getAttoAmministrativo().getStrutturaAmmContabile().getUid() : 0);
				
				boolean impegnoConLiquidazioni = false;
				
				VerificaLegameImpegnoLiquidazioni req = new  VerificaLegameImpegnoLiquidazioni();
				req.setRichiedente(sessionHandler.getRichiedente());
				req.setEnte(sessionHandler.getEnte());
				req.setUidMovimento(model.getImpegnoInAggiornamento().getUid());
				
				VerificaLegameImpegnoLiquidazioniResponse res = movimentoGestioneFinService.verificaLegameImpegnoLiquidazioni(req);
								
				if(res!=null && !res.isFallimento()){
					
					impegnoConLiquidazioni = res.getLiquidazioniCollegateAlMovimento();
				}
				
				if(impegnoConLiquidazioni){
					msgPerMovimentiCollegatiAOrdinativiOLiquidazioni = ErroreFin.CONFERMA_AGGIORNAMENTO_PROVVEDIMENTO_CON_MOVIMENTO_CON_LIQUIDAZIONI.getMessaggio();
				}
			}
			
		
			// IN caso di movimento D o N verifico se il provvedimento e' cambiato: 
			// se si visualizzo la finestra di alert per richiedere la conferma utente
			
			boolean definitivo = statoOperativoMovimento.equals("D") || statoOperativoMovimento.equals("N");
					
			if(definitivo && model.getStep1Model().getProvvedimento()!=null){
													
				// nuovo provvedimento
				Integer annoProvvedimentoDaAggiornare = model.getStep1Model().getProvvedimento().getAnnoProvvedimento();
				Integer numeroProvvedimentoDaAggiornare = model.getStep1Model().getProvvedimento().getNumeroProvvedimento().intValue();
				Integer idTipoattoDaAggiornare = model.getStep1Model().getProvvedimento().getIdTipoProvvedimento();
				Integer idSacDaAggiornare = (model.getStep1Model().getProvvedimento().getUidStrutturaAmm()!=null ? model.getStep1Model().getProvvedimento().getUidStrutturaAmm(): 0) ;
				
				boolean annoProvvedimentoCambiato = (annoProvvedimentoDaAggiornare != 0 && !annoProvvedimento.equals(annoProvvedimentoDaAggiornare));
				boolean numeroProvvedimentoCambiato = (numeroProvvedimentoDaAggiornare != 0 && !numeroProvvedimento.equals(numeroProvvedimentoDaAggiornare));
				boolean tipoProvvedimentoCambiato = (idTipoattoDaAggiornare != 0  && !idTipoatto.equals( idTipoattoDaAggiornare));
				boolean sacProvvedimentoCambiata = (idSacDaAggiornare!=0 && !idSac.equals(idSacDaAggiornare));

				if(annoProvvedimentoCambiato || numeroProvvedimentoCambiato || tipoProvvedimentoCambiato || sacProvvedimentoCambiata) {
					
					// CR SIAC-3224, imposto il messaggio di alert
				    String alertDiConfermaModificaProvvedimento ="";
				    	
				    if(statoOperativoMovimento.equals(CostantiFin.MOVGEST_STATO_DEFINITIVO)){
				    	alertDiConfermaModificaProvvedimento = ErroreFin.CONFERMA_AGGIORNAMENTO_PROVVEDIMENTO_CON_MOVIMENTO_DEFINITIVO
			    			.getErrore(model.getStep1Model().getProvvedimento().getStato())
			    			.getDescrizione();
				    }
				    
				    if(statoOperativoMovimento.equals(CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE)){
				    	alertDiConfermaModificaProvvedimento = ErroreFin.CONFERMA_AGGIORNAMENTO_PROVVEDIMENTO_CON_MOVIMENTO_DEFINITIVO_NON_LIQUIDABILE
				    		.getErrore(model.getStep1Model().getProvvedimento().getStato())
				    		.getDescrizione();
				    }
				   
				    model.getStep1Model().setAlertDiConfermaModificaProvvedimento(alertDiConfermaModificaProvvedimento);
					model.getStep1Model().setAlertMovimentoCollegatoAOrdinativiOLiquidazioni(msgPerMovimentiCollegatiAOrdinativiOLiquidazioni);
				    
					if(arrivoDaSalva){
						setShowModaleConfermaModificaProvvedimento(true);
					} else {
						setShowModaleConfermaProseguiModificaProvvedimento(true);
					}
					
	    			provvedimendoCambiato = true;
	    		}	
				
			}
				
			
			return provvedimendoCambiato;
		}
		
		
		
		/**
		 * 
		 */
		/**
		 * CR SIAC-3224, in caso di aggiornamento il provvedimento si puo modificare
		 * 
		 * @return
		 */
		public boolean verificaModificaVincoli(boolean arrivoDaSalva) {
			
			boolean modificaVincoli = false;
				
			
			// STEP1 verifica esistenza vincoli
//				provvedimentoOldEsistente = model.getImpegnoInAggiornamento().getAttoAmministrativo()!=null ? true: false;
//				
//				if(!provvedimentoOldEsistente)
//					return provvedimendoCambiato ;
				

			// Se vincoli cambiati....impostare il msg corretto
			//msgConfermaPerModificaVincoli = ErroreFin.CONFERMA_AGGIORNAMENTO_PROVVEDIMENTO_CON_MOVIMENTO_CON_LIQUIDAZIONI.getMessaggio();
			
			// se c'è una modifica ...
			if(true) {
					
				if(arrivoDaSalva){
					setShowModaleConfermaSalvaModificaVincoli(true);
				}else{
					setShowModaleConfermaProseguiModificaVincoli(true);
				}
				modificaVincoli = true;
	    			
	    	}	
				
			return modificaVincoli;
		}
		
		
		
		/**
		 * CR SIAC-3224, in caso di aggiornamento il provvedimento si puo modificare
		 * 
		 * @return
		 */
		public boolean verificaModificaProvvedimentoSubMovimento(boolean arrivoDaSalva) {
			
			boolean provvedimendoCambiato = false;
			
			String statoOperativoMovimento = "";
			Integer annoProvvedimento = 0;
			Integer numeroProvvedimento = 0;
			Integer idTipoatto = 0; 
			Integer idSac = 0; 
			
			String msgPerMovimentiCollegatiAOrdinativiOLiquidazioni ="";
			
			boolean subEsistenste = false; 			
						
			if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBACCERTAMENTO)){
				
				statoOperativoMovimento = model.getStep1ModelSubimpegno().getStato();

				MovimentoGestione subAccertamento= FinUtility.getById(model.getAccertamentoInAggiornamento().getElencoSubAccertamenti(), model.getStep1ModelSubimpegno().getUid());
				
				// su un movimento definitivo non liquidabile, il sub potrebbe non esserci quindi se siamo all'inserimento di un nuovo sub non deve partire il controllo
				subEsistenste = subAccertamento !=null ? true: false;
				
				if(!subEsistenste)
					return provvedimendoCambiato ;
				
				// vecchio provvedimento
				annoProvvedimento = subAccertamento.getAttoAmministrativo().getAnno();
				numeroProvvedimento =  subAccertamento.getAttoAmministrativo().getNumero();
				idTipoatto = subAccertamento.getAttoAmministrativo().getTipoAtto().getUid();
				idSac =  (subAccertamento.getAttoAmministrativo().getStrutturaAmmContabile()!=null ? subAccertamento.getAttoAmministrativo().getStrutturaAmmContabile().getUid() : 0);
			
				
				//jira siac-4188
				boolean accertamentoConOrdinativi = false;
				
				RicercaReversaliByAccertamento req = new  RicercaReversaliByAccertamento();
				req.setRichiedente(sessionHandler.getRichiedente());
				req.setEnte(sessionHandler.getEnte());
				req.setUidMovimento(subAccertamento.getUid());
				
				RicercaReversaliByAccertamentoResponse res = movimentoGestioneFinService.ricercaReversaliByAccertamento(req);
								
				if(res!=null && !res.isFallimento()){
					
					accertamentoConOrdinativi = res.getReversaliCollegateAlMovimento();
				}
				
				if(accertamentoConOrdinativi){
					msgPerMovimentiCollegatiAOrdinativiOLiquidazioni = ErroreFin.CONFERMA_AGGIORNAMENTO_PROVVEDIMENTO_CON_MOVIMENTO_CON_ORDINATIVI.getMessaggio();
				}
				
			}else if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBIMPEGNO)){
				
				statoOperativoMovimento =  model.getStep1ModelSubimpegno().getStato();
				
				MovimentoGestione subImpegno = FinUtility.getById(model.getImpegnoInAggiornamento().getElencoSubImpegni(), model.getStep1ModelSubimpegno().getUid());
				
				subEsistenste = subImpegno !=null ? true: false;
				
				if(!subEsistenste)
					return provvedimendoCambiato ;
				
				// vecchio provvedimento
				annoProvvedimento = subImpegno.getAttoAmministrativo().getAnno();
				numeroProvvedimento =  subImpegno.getAttoAmministrativo().getNumero();
				idTipoatto = subImpegno.getAttoAmministrativo().getTipoAtto().getUid();
				idSac =  (subImpegno.getAttoAmministrativo().getStrutturaAmmContabile()!=null ? subImpegno.getAttoAmministrativo().getStrutturaAmmContabile().getUid() : 0);
				
				boolean impegnoConLiquidazioni = false;
				
				VerificaLegameImpegnoLiquidazioni req = new  VerificaLegameImpegnoLiquidazioni();
				req.setRichiedente(sessionHandler.getRichiedente());
				req.setEnte(sessionHandler.getEnte());
				req.setUidMovimento(subImpegno.getUid());
				
				VerificaLegameImpegnoLiquidazioniResponse res = movimentoGestioneFinService.verificaLegameImpegnoLiquidazioni(req);
								
				if(res!=null && !res.isFallimento()){
					
					impegnoConLiquidazioni = res.getLiquidazioniCollegateAlMovimento();
				}
				
				if(impegnoConLiquidazioni){
					msgPerMovimentiCollegatiAOrdinativiOLiquidazioni = ErroreFin.CONFERMA_AGGIORNAMENTO_PROVVEDIMENTO_CON_MOVIMENTO_CON_LIQUIDAZIONI.getMessaggio();
				}
			}
			
		
			// IN caso di movimento D o N verifico se il provvedimento e' cambiato: 
			// se si visualizzo la finestra di alert per richiedere la conferma utente
			
			boolean definitivo = statoOperativoMovimento.equals(STATO_MOVGEST_DEFINITIVO)
					|| statoOperativoMovimento.equals(MOVGEST_DEFINITIVO_NON_LIQUIDABILE);
					
			if(definitivo && model.getStep1ModelSubimpegno().getProvvedimento() !=null){
													
				// nuovo provvedimento
				Integer annoProvvedimentoDaAggiornare = model.getStep1ModelSubimpegno().getProvvedimento().getAnnoProvvedimento();
				Integer numeroProvvedimentoDaAggiornare = model.getStep1ModelSubimpegno().getProvvedimento().getNumeroProvvedimento().intValue();
				Integer idTipoattoDaAggiornare = model.getStep1ModelSubimpegno().getProvvedimento().getIdTipoProvvedimento();
				Integer idSacDaAggiornare = (model.getStep1ModelSubimpegno().getProvvedimento().getUidStrutturaAmm()!=null ? model.getStep1ModelSubimpegno().getProvvedimento().getUidStrutturaAmm(): 0) ;
				
				boolean annoProvvedimentoCambiato = (annoProvvedimentoDaAggiornare != 0 && !annoProvvedimento.equals(annoProvvedimentoDaAggiornare));
				boolean numeroProvvedimentoCambiato = (numeroProvvedimentoDaAggiornare != 0 && !numeroProvvedimento.equals(numeroProvvedimentoDaAggiornare));
				boolean tipoProvvedimentoCambiato = (idTipoattoDaAggiornare != 0  && !idTipoatto.equals( idTipoattoDaAggiornare));
				boolean sacProvvedimentoCambiata = (idSacDaAggiornare!=0 && !idSac.equals(idSacDaAggiornare));

				if(annoProvvedimentoCambiato || numeroProvvedimentoCambiato || tipoProvvedimentoCambiato || sacProvvedimentoCambiata) {
					
					// CR SIAC-3224, imposto il messaggio di alert
				    String alertDiConfermaModificaProvvedimento = "";
				    
				    if(statoOperativoMovimento.equals(STATO_MOVGEST_DEFINITIVO)){
				    	alertDiConfermaModificaProvvedimento = ErroreFin.CONFERMA_AGGIORNAMENTO_PROVVEDIMENTO_CON_MOVIMENTO_DEFINITIVO
			    			.getErrore(model.getStep1Model().getProvvedimento().getStato())
			    			.getDescrizione();
				    }
				    
				    if(statoOperativoMovimento.equals(MOVGEST_DEFINITIVO_NON_LIQUIDABILE)){
				    	alertDiConfermaModificaProvvedimento = ErroreFin.CONFERMA_AGGIORNAMENTO_PROVVEDIMENTO_CON_MOVIMENTO_DEFINITIVO_NON_LIQUIDABILE
				    		.getErrore(model.getStep1Model().getProvvedimento().getStato())
				    		.getDescrizione();
				    }
				    
				    model.getStep1Model().setAlertDiConfermaModificaProvvedimento(alertDiConfermaModificaProvvedimento);
					model.getStep1Model().setAlertMovimentoCollegatoAOrdinativiOLiquidazioni(msgPerMovimentiCollegatiAOrdinativiOLiquidazioni);
				    
					if(arrivoDaSalva){
						setShowModaleConfermaModificaProvvedimento(true);
					} else {
						setShowModaleConfermaProseguiModificaProvvedimento(true);
					}
					
	    			provvedimendoCambiato = true;
	    		}	
				
			}
				
			
			return provvedimendoCambiato;
		}
			  	
		/**
		 * Compone la request per la chiamata al servizio di aggiorna
		 * @return
		 */
		protected AggiornaImpegno convertiModelPerChiamataServizioAggiornaImpegniStep2() {
			//AGGIORNA IMPEGNO STEP 2
			
			AggiornaImpegno aggiornaImpegnoReq = new AggiornaImpegno();
			
			Bilancio bilancio = new Bilancio();
			bilancio.setAnno(sessionHandler.getAnnoBilancio());
			
			Impegno impegnoDaAggiornare = popolaImpegnoAggiorna(model, null, bilancio);
			impegnoDaAggiornare.setAnnoMovimento(model.getStep1Model().getAnnoImpegno());
			
			BigDecimal numImp = new BigDecimal(String.valueOf(model.getStep1Model().getNumeroImpegno()));
			impegnoDaAggiornare.setNumeroBigDecimal(numImp);
			impegnoDaAggiornare.setUid(model.getStep1Model().getUid());
			
			aggiornaImpegnoReq.setImpegno(impegnoDaAggiornare);
			aggiornaImpegnoReq.setEnte(model.getEnte());
			aggiornaImpegnoReq.getImpegno().setSoggetto(impegnoDaAggiornare.getSoggetto());
			
			 // subimpegni
			//MAGGIO 2016, per paginazione dei sub non vanno piu' indicati i sub che devono restare invariati:
			aggiornaImpegnoReq.getImpegno().setElencoSubImpegni(new ArrayList<SubImpegno>());
			//
			
			 // eventuali vincoli
		     if(null!=model.getStep1Model().getListaVincoliImpegno() && !model.getStep1Model().getListaVincoliImpegno().isEmpty()){
			   aggiornaImpegnoReq.getImpegno().setVincoliImpegno(model.getStep1Model().getListaVincoliImpegno());
		     }
		     //
			
			aggiornaImpegnoReq.setRichiedente(sessionHandler.getRichiedente());
			aggiornaImpegnoReq.setUnitaElementareDiGestione(null);
			
			aggiornaImpegnoReq.getImpegno().setCapitoloUscitaGestione(model.getStep1Model().getCapitoloImpegno());
			aggiornaImpegnoReq.setBilancio(bilancio);
		
			//SIAC-6997
			aggiornaImpegnoReq.getImpegno().setStrutturaCompetente(model.getStep1Model().getStrutturaSelezionataCompetente());
			

			return aggiornaImpegnoReq;
		}
		
		/**
		 * Compone la request per la chiamata al servizio di aggiorna
		 * @return
		 */
		protected AggiornaAccertamento convertiModelPerChiamataServizioAggiornaAccertamentiStep2() {
			//AGGIORNA ACCERTAMENTO STEP 2
			
			AggiornaAccertamento aggiornaAccertamentoReq = new AggiornaAccertamento();
			
			Bilancio bilancio = new Bilancio();
			bilancio.setAnno(sessionHandler.getAnnoBilancio());
			
			Accertamento accertamentoDaAggiornare = popolaAccertamentoAggiorna(model, null, bilancio);
			accertamentoDaAggiornare.setAnnoMovimento(model.getStep1Model().getAnnoImpegno());
			
			BigDecimal numImp = new BigDecimal(String.valueOf(model.getStep1Model().getNumeroImpegno()));
			accertamentoDaAggiornare.setNumeroBigDecimal(numImp);
			accertamentoDaAggiornare.setUid(model.getStep1Model().getUid());
			
			aggiornaAccertamentoReq.setAccertamento(accertamentoDaAggiornare); 
			aggiornaAccertamentoReq.setEnte(model.getEnte()); 
			
			//task-51
			//aggiornaAccertamentoReq.getAccertamento().setSoggetto(model.getStep1Model().getSoggettoImpegno());       
			aggiornaAccertamentoReq.getAccertamento().setSoggetto(accertamentoDaAggiornare.getSoggetto());
			
			if(!isEmpty(model.getListaSubaccertamenti())){
				aggiornaAccertamentoReq.getAccertamento().setElencoSubAccertamenti(model.getListaSubaccertamenti()); 
			}
			
			aggiornaAccertamentoReq.setRichiedente(sessionHandler.getRichiedente());
			aggiornaAccertamentoReq.setUnitaElementareGestioneE(null);
			
			aggiornaAccertamentoReq.getAccertamento().setCapitoloEntrataGestione(capitoloEntrataTrovato);
			aggiornaAccertamentoReq.setBilancio(bilancio);
		
			//SIAC-6997
			aggiornaAccertamentoReq.getAccertamento().setStrutturaCompetente(model.getStep1Model().getStrutturaSelezionataCompetente());
			//SIAC-8178
			aggiornaAccertamentoReq.getAccertamento().setCodiceVerbale(model.getStep1Model().getCodiceVerbale());
			
			// SIAC-8739
			if (ObjectUtils.defaultIfNull(model.getStep1Model().getSoggetto().getUid(), 0) > 0) {
				aggiornaAccertamentoReq.getAccertamento().getSoggetto().setUid(model.getStep1Model().getSoggetto().getUid());
				aggiornaAccertamentoReq.getAccertamento().getSoggetto().setCodiceSoggetto(model.getStep1Model().getSoggetto().getCodCreditore());
			}
			
			return aggiornaAccertamentoReq;      
		}
		
		
		/**
		 * salvataggio dei sub
		 * 
		 * @param controlliFormali
		 * @return
		 */
		public String salva(boolean controlliFormali) {
			
			//SALVA SUB-IMPEGNI/ACCERTAMENTI
			
			// CR SIAC-3224, in caso di aggiornamento il provvedimento si puo modificare
			//SIAC-5343: se sto arrivando dalla modale di richiedsta di conferma di redirezione, bypasso il controllo (gia' effettuato precedentemente)
			if (isFromModaleConfermaModificaProvvedimento() || model.isRichiediConfermaRedirezioneContabilitaGenerale()) {
				setShowModaleConfermaModificaProvvedimento(false);
    		}else if(!isProseguiStep1() && verificaModificaProvvedimentoSubMovimento(true)){
				setSuccessStep1(false);
				return INPUT;
			}
		   
			// controlli tabellina 4.6
			List<Errore> erroriAbilitazione = abilitazioneCampiTE(oggettoDaPopolare);
			
			if(!isEmpty(erroriAbilitazione)){
				addErrori(erroriAbilitazione);
				return INPUT; 
			}
			
			// controllo sul V livello
			if(teSupport.getPianoDeiConti()!=null && teSupport.getPianoDeiConti().getCodice()!=null && teSupport.getPianoDeiConti().getCodice().endsWith(IV_LIVELLO)){
				addErrore(ErroreFin.ELEM_PDC_NON_INDICATO.getErrore(""));
				return INPUT;
			}
			
			MovimentoGestione subMovimentoInAggiornamento = null;
			if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.SUBIMPEGNO)){
				subMovimentoInAggiornamento = FinUtility.getById(model.getImpegnoInAggiornamento().getElencoSubImpegni(), model.getStep1ModelSubimpegno().getUid());
			} else {
				subMovimentoInAggiornamento = FinUtility.getById(model.getAccertamentoInAggiornamento().getElencoSubAccertamenti(), model.getStep1ModelSubimpegno().getUid());
			}
			
			if(!controlloProvvedimentoSubPerDecentratoP()){
				return INPUT;
			}
			
			//CONTROLLO PIANO DEI CONTI SE CI SONO MOVIMENTI COLLEGATI:
			if(!controlloMovimentiCollegatiPerModificaPianoDeiConti(subMovimentoInAggiornamento)){
				return INPUT;
			}
			//
		   
		   if (controlliFormali) {
			   List<Errore> errori = erroriSubimpegno();
			   
			   boolean warngins = presenzaWarningsSubimpegno();
			   
			   if(!isEmpty(errori)){
					addErrori(errori);
					return INPUT;
				}
				if(warngins){
					return INPUT;
				}
				
				if (!controlloServiziSub()) {
					impostaDatiPassaggioDiStato();
					aggiornaStatoSub();
					if(isNecessariaRichiestaConfermaUtentePerRedirezioneSuContabilitaGenerale()) {
						model.setSaltaInserimentoPrimaNota(false);
						model.setRichiediConfermaRedirezioneContabilitaGenerale(true);
						return INPUT;
					}					
					if (oggettoDaPopolareSubimpegno()) { 
						String resultServizio = servizioGestioneSubimpegno();
						if(INPUT.equals(resultServizio) || GOTO_CONTABILITA_GENERALE.equals(resultServizio)){
							return resultServizio;
						}
					} else {
						String resultServizio = servizioGestioneSubaccertamento();
						if(INPUT.equals(resultServizio) || GOTO_CONTABILITA_GENERALE.equalsIgnoreCase(resultServizio)){
							return resultServizio;
						}
					}
					model.setRichiediConfermaRedirezioneContabilitaGenerale(false);
					model.setStep1ModelSubimpegno(new GestisciImpegnoStep1Model());
				    model.setStep1ModelSubimpegnoCache(new GestisciImpegnoStep1Model());
				    
				    boolean richiestaRedirezioneContabilitaGenerale = model.isEnteAbilitatoGestionePrimaNotaDaFinanziaria() && model.isSaltaInserimentoPrimaNota();
				   
				    /*
				    if( richiestaRedirezioneContabilitaGenerale && model.getUidDaCompletare() == 0) {
						cleanErrori();
						Errore errore = ErroreGEN.OPERAZIONE_NON_CONSENTITA.getErrore("Non e' stata inserita alcuna registrazione, impossibile validare la prima nota");
						addErrore(errore);
						addPersistentActionWarning(errore.getTesto());
//						setErroriInSessionePerActionSuccessiva();
					}*/
				    
					//controllo registrazione andata a buon fine:
					controlloRegistrazioneValidazionePrimaNota();
					//
				    
					//SIAC-7861 ho notato che se tanto si passa nel result di GOTO_AGGIORNA_SUBIMPEGNO
					//la AggiornaSubimpegnoAction provvede a fornire il messaggio (vedasi SIAC-7855), in questo modo, si evitano doppioni
//				    addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getCodice()+"-"+ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
					return richiestaRedirezioneContabilitaGenerale && model.getUidDaCompletare() != 0 ? GOTO_CONTABILITA_GENERALE : GOTO_AGGIORNA_SUBIMPEGNO;
				} else {
					return INPUT;
				}
				
		   } else {
			   impostaDatiPassaggioDiStato();
			   aggiornaStatoSub();
			   if(isNecessariaRichiestaConfermaUtentePerRedirezioneSuContabilitaGenerale()) {
					model.setSaltaInserimentoPrimaNota(false);
					model.setRichiediConfermaRedirezioneContabilitaGenerale(true);
					return INPUT;
				}
			   if (oggettoDaPopolareSubimpegno()) {
					if(servizioGestioneSubimpegno().equals(INPUT)){
						return INPUT;
					}
				} else {
					if(servizioGestioneSubaccertamento().equalsIgnoreCase(INPUT)){
						return INPUT;
					}
				}
			   model.setStep1ModelSubimpegno(new GestisciImpegnoStep1Model());
			   model.setStep1ModelSubimpegnoCache(new GestisciImpegnoStep1Model());
				//SIAC-7861 ho notato che se tanto si passa nel result di GOTO_AGGIORNA_SUBIMPEGNO
				//la AggiornaSubimpegnoAction provvede a fornire il messaggio (vedasi SIAC-7855), in questo modo, si evitano doppioni
//			    addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getCodice()+"-"+ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
			   //SIAC-5943
			   model.setRichiediConfermaRedirezioneContabilitaGenerale(false);
			   boolean richiestaRedirezioneContabilitaGenerale = model.isEnteAbilitatoGestionePrimaNotaDaFinanziaria() && model.isSaltaInserimentoPrimaNota();
			    if( richiestaRedirezioneContabilitaGenerale && model.getUidDaCompletare() == 0) {
					cleanErrori();
					Errore errore = ErroreGEN.OPERAZIONE_NON_CONSENTITA.getErrore("Non e' stata inserita alcuna registrazione, impossibile validare la prima nota");
					addErrore(errore);
					addPersistentActionWarning(errore.getTesto());
//					setErroriInSessionePerActionSuccessiva();
				}
			   return richiestaRedirezioneContabilitaGenerale && model.getUidDaCompletare() != 0 ? GOTO_CONTABILITA_GENERALE : GOTO_AGGIORNA_SUBIMPEGNO;
		   }
	}
		
	private void impostaDatiPassaggioDiStato() {
		model.getStep1ModelSubimpegno().setPassaggioAStatoDefinitivo(isStatoSubDaProvvisorioADefinitivo());
	}
		
	/**
	 * Controlla se lo stato del subimpeno passi da provvisorio a definitivo. Questo avviene se e' stato modificato il provvedimento di un sub provvisorio mettendone uno con stato definitivo.s
	 * NB deve essere eseguito prima che nel model venga modificato lo stato del sub 
	 * @return true, if successful
	 */
	private boolean isStatoSubDaProvvisorioADefinitivo() {
		//SIAC-5943
		// ho modificato il provvedimento e quello nuovo e' di stato definitivo
		boolean provvedimentoCambiatoInUnoDefinitivo = model.getStep1ModelSubimpegno().getProvvedimento() != null && model.getStep1ModelSubimpegno().getProvvedimento().getUid() != null?
				STATO_MOVGEST_DEFINITIVO.equals(model.getStep1ModelSubimpegno().getProvvedimento().getStato())
				: false;
		//lo stato del sub era definitivo
		boolean statoSubPrecedenteProvvisorio = MOVGEST_PROVVISORIO.equals(model.getStatoMovimentoGestioneOld()); 
		return model.getStep1ModelSubimpegno().isOperazioneInserimento() || (provvedimentoCambiatoInUnoDefinitivo &&  statoSubPrecedenteProvvisorio);
	}

	/**
	 * Controllo Provvedimento Sub Per DecentratoP
	 * @return
	 */
	protected boolean controlloProvvedimentoSubPerDecentratoP(){
		boolean esito = true;
		String azioneGestisciDecentratoP = null;
		ProvvedimentoImpegnoModel provvSub = null;
		if(oggettoDaPopolareSubimpegno()){
			azioneGestisciDecentratoP = AzioneConsentitaEnum.OP_SPE_gestisciImpegnoDecentratoP.getNomeAzione();
			provvSub = model.getStep1ModelSubimpegno().getProvvedimento();
		} else if(oggettoDaPopolareSubaccertamento()){
			azioneGestisciDecentratoP = AzioneConsentitaEnum.OP_ENT_gestisciAccertamentoDecentratoP.getNomeAzione();
			provvSub = model.getStep1ModelSubimpegno().getProvvedimento();
		}
		
		boolean decentratoP = isAzioneAbilitata(azioneGestisciDecentratoP);
		
		if(decentratoP && provvSub!=null){
			//il provveddimento, se indicato, deve essere provvisorio
			boolean provvedimentoProvvisorio = true;
			if (!MOVGEST_PROVVISORIO.equals(provvSub.getStato())){
				provvedimentoProvvisorio = false;
			}
			if(!provvedimentoProvvisorio){
				if(oggettoDaPopolareSubimpegno()){
					addErrore(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Inserimento Sub Impegno Decentrato", "Provvisorio"));
				} else {
					addErrore(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Inserimento Sub Accertamento Decentrato", "Provvisorio"));
				}
				esito = Boolean.FALSE;
			}
		} 
		return esito;
	}
	   
	/**
	 * Aggiorna Stato Sub
	 */
	protected void aggiornaStatoSub() {
		if (model.getStep1ModelSubimpegno().getProvvedimento() != null && model.getStep1ModelSubimpegno().getProvvedimento().getUid() != null) {
			if (model.getStep1ModelSubimpegno().getProvvedimento().getStato().equals(MOVGEST_PROVVISORIO)) {
				model.getStep1ModelSubimpegno().setStato(MOVGEST_PROVVISORIO);
			} else if (model.getStep1ModelSubimpegno().getProvvedimento().getStato().equals(STATO_MOVGEST_DEFINITIVO)) {
			    model.getStep1ModelSubimpegno().setStato(STATO_MOVGEST_DEFINITIVO);
			}
		}
	}
	
	/**
	 * Servizio Gestione Subimpegno
	 * @return
	 */
	private String servizioGestioneSubimpegno() {
		
		if(isEmpty(model.getListaSubimpegni())){
			model.setListaSubimpegni(new ArrayList<SubImpegno>());
	    }
		
		//MAGGIO 2016, per ottimizzazioni bisogna indicare solo l'impegno da inserire/modificare:
		SubImpegno subInInserimentoOAggiornamento = convertiSubimpegnoModelToSubimpegno();
		model.setListaSubimpegni(toList(subInInserimentoOAggiornamento));
		//
		
		   
		AggiornaImpegno requestAggiorna = convertiModelPerChiamataServizioAggiornaImpegniDaSubimpegno();
		
		
		//SALVA SDF:
		if(salvaConSDF()){
			requestAggiorna.getImpegno().setFlagSDF(true);
		}

		if (requestAggiorna.getImpegno().getProgetto() != null) {
			Progetto progetto = requestAggiorna.getImpegno().getProgetto();
			
			progetto.setTipoProgetto(TipoProgetto.GESTIONE);
			progetto.setBilancio(sessionHandler.getBilancio());

			requestAggiorna.getImpegno().setIdCronoprogramma(model.getStep1Model().getIdCronoprogramma());
			requestAggiorna.getImpegno().setIdSpesaCronoprogramma(model.getStep1Model().getIdSpesaCronoprogramma());
		}
		
		/*
		 * SIAC-7349
		 * Settiamo come campo di massima disponibilita della componente
		 * quello della massima disponibilita del subimpegno a impegnare quello dell'impegno
		 */
		requestAggiorna.getImpegno().setDisponibilitaImpegnareComponente(model.getDisponibilitaSubImpegnare());
		
		AggiornaImpegnoResponse response= movimentoGestioneFinService.aggiornaImpegno(requestAggiorna);
		   
		   if(!response.isFallimento()){
			  
			   /*
			   APRILE 2016, 
				  l'aggiorna impegno non richiama piu' ricercaImpegnoPerChiave (benche' ottimizzato) e' troppo rischioso perche' l'aggiorna impegno
				  ha richiesto un po' di tempo e qui il timeout e' quasi assicurato con troppi sub...conviene spezzare il caricamento
				  e rieffettuarlo dalla action per evitare timeout */
			   
			   model.setImpegnoRicaricatoDopoInsOAgg(null);
			   forceReload = true;
			   
			   
			   model.setStep1ModelSubimpegno(new GestisciImpegnoStep1Model());
			   //SIAC-5943
			   model.setUidDaCompletare(response.getRegistrazioneMovFinFIN() != null? response.getRegistrazioneMovFinFIN().getUid() : 0);
			   return SUCCESS;
		   }else{
				
			   addErrori(methodName, response);
			   return INPUT;
		   }
	}
	   
	/**
	 * Servizio Gestione Subaccertamento
	 * @return
	 */
	private String servizioGestioneSubaccertamento() {
		   if(isEmpty(model.getListaSubaccertamenti())){
				model.setListaSubaccertamenti(new ArrayList<SubAccertamento>());
		   }
		   if (model.getStep1ModelSubimpegno().getUid() == null) {
			   model.getListaSubaccertamenti().add(convertiSubaccertamentoModelToSubaccertamento(null));
			 
		   } else {
			    SubAccertamento subaccertamentoDaModificare = null;
			    for (SubAccertamento currentSubaccertamento : model.getListaSubaccertamenti()) {
					if (model.getStep1ModelSubimpegno().getUid() != null && currentSubaccertamento.getUid() == model.getStep1ModelSubimpegno().getUid().intValue()) {
						BigDecimal numeroSubImpegno = currentSubaccertamento.getNumeroBigDecimal();
						subaccertamentoDaModificare = convertiSubaccertamentoModelToSubaccertamento(numeroSubImpegno);
						model.getListaSubaccertamenti().remove(currentSubaccertamento);
						break;
					}
				}
			    if (subaccertamentoDaModificare != null) {
			    	model.getListaSubaccertamenti().add(subaccertamentoDaModificare);	
			    }
		   }
		   AggiornaAccertamentoResponse response= movimentoGestioneFinService.aggiornaAccertamento(convertiModelPerChiamataServizioAggiornaAccertamentiDaSubimpegno()); 
		   if(!isFallimento(response)){
			   model.setAccertamentoRicaricatoDopoInsOAgg(response.getAccertamento());
			   model.setStep1ModelSubimpegno(new GestisciImpegnoStep1Model());
			   model.setUidDaCompletare(response.getRegistrazioneMovFinFIN() != null? response.getRegistrazioneMovFinFIN().getUid() : 0);
			   return SUCCESS;
		   }else{
			   addErrori(response.getErrori());
			   return INPUT;
		   }
	}
	   
	/**
	 * Controllo Servizi Sub
	 * @return
	 */
	   protected boolean controlloServiziSub() {
			boolean erroriTrovatiNeiServizi = false;
			if (!model.getStep1ModelSubimpegno().isSoggettoSelezionato() && model.getStep1ModelSubimpegno().getSoggetto().getCodCreditore() != null && !"".equals(model.getStep1ModelSubimpegno().getSoggetto().getCodCreditore())) {
				if (!eseguiRicercaSoggetto(convertiModelPerChiamataServizioRicerca(model.getStep1ModelSubimpegno().getSoggetto()), false, OggettoDaPopolareEnum.SUBIMPEGNO)) {
					erroriTrovatiNeiServizi = true;
				}
			} 
			if (!model.getStep1ModelSubimpegno().isProvvedimentoSelezionato() && 
					(null!=model.getStep1ModelSubimpegno().getProvvedimento().getAnnoProvvedimento() ||
					null!=model.getStep1ModelSubimpegno().getProvvedimento().getIdTipoProvvedimento() || 
					null!=model.getStep1ModelSubimpegno().getProvvedimento().getNumeroProvvedimento())){
				if (!eseguiRicercaProvvedimento(OggettoDaPopolareEnum.SUBIMPEGNO, model.getStep1ModelSubimpegno().getProvvedimento(), false)) {
					erroriTrovatiNeiServizi = true;
				}
			}
			return erroriTrovatiNeiServizi;
		}
	   
	   protected List<Errore> erroriSubimpegno() {
			List<Errore> errori = new ArrayList<Errore>();
			
			// cod sogg e provvedimento
			
			if(model.getStep1ModelSubimpegno().getSoggetto().getCodCreditore()==null || "".equalsIgnoreCase(model.getStep1ModelSubimpegno().getSoggetto().getCodCreditore())){
				errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice Creditore "));
			}
			
			if(model.getStep1ModelSubimpegno().getProvvedimento()!=null){
				 if((model.getStep1ModelSubimpegno().getProvvedimento().getAnnoProvvedimento() == null) || (model.getStep1ModelSubimpegno().getProvvedimento().getAnnoProvvedimento() == 0)){
					 errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Provvedimento :Anno "));
				}
				    if(((model.getStep1ModelSubimpegno().getProvvedimento().getNumeroProvvedimento() == null) || (model.getStep1ModelSubimpegno().getProvvedimento().getNumeroProvvedimento().intValue() == 0)) && model.getStep1ModelSubimpegno().getProvvedimento().getIdTipoProvvedimento()== null){
				    	errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Provvedimento: Numero o Tipo "));
				    }
				
			}else{
				errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Dati del provvedimento"));
			}
			
		    if(model.getStep1ModelSubimpegno().getImportoFormattato() == null || "".equalsIgnoreCase(model.getStep1ModelSubimpegno().getImportoFormattato())){
		 	    errori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo Subimpegno "));
		    }else{
		    	model.getStep1ModelSubimpegno().setImportoImpegno(convertiImportoToBigDecimal(model.getStep1ModelSubimpegno().getImportoFormattato()));
		    	BigDecimal deltaImporto = model.getDisponibilitaSubImpegnare().add((model.getStep1ModelSubimpegno().getImportoImpegnoMod() != null) ? model.getStep1ModelSubimpegno().getImportoImpegnoMod() : BigDecimal.ZERO);
		    	
		    	if(MOVGEST_PROVVISORIO.equals(model.getStep1ModelSubimpegno().getStato())){
	    			
		    		//FIX PER  	SIAC-2991, in caso di SUB PROVVISORIO METTO STRETTAMENTE MINORE DI ZERO PER LANCIARE ERRORE:
		    		if (model.getStep1ModelSubimpegno().getImportoImpegno().compareTo(BigDecimal.ZERO) < 0 || 
			    			model.getStep1ModelSubimpegno().getImportoImpegno().compareTo(deltaImporto) > 0) {
			    		errori.add(ErroreFin.SUPERAMENTO_DISPONIBILITA.getErrore(""));
			    		model.getStep1ModelSubimpegno().setImportoImpegno(BigDecimal.ZERO);
			    	}
		    		
	    		} else {
	    			if (model.getStep1ModelSubimpegno().getImportoImpegno().compareTo(BigDecimal.ZERO) < 0 || 
			    			model.getStep1ModelSubimpegno().getImportoImpegno().compareTo(deltaImporto) > 0) {
			    		errori.add(ErroreFin.SUPERAMENTO_DISPONIBILITA.getErrore(""));
			    		model.getStep1ModelSubimpegno().setImportoImpegno(BigDecimal.ZERO);
			    	}
	    			
	    			if (model.getStep1ModelSubimpegno().getImportoImpegno().compareTo(BigDecimal.ZERO) == 0) {
			    		errori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Importo", "inserire un importo maggiore di zero"));
			    	}
	    		}
		    	
		    }
		    if(StringUtils.isNotEmpty(model.getStep1ModelSubimpegno().getScadenza())){
				if(model.getStep1ModelSubimpegno().getScadenza().length()!=10){
					errori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Data Scadenza","dd/mm/yyyy"));
				}else {
					Date dataInserita = DateUtility.parse(model.getStep1ModelSubimpegno().getScadenza());
					if(dataInserita == null) {
						errori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Data Scadenza","dd/mm/yyyy"));
					} else {
						Calendar c = new GregorianCalendar();
						c.setTime(dataInserita);
						if (c.get(Calendar.YEAR) != model.getStep1ModelSubimpegno().getAnnoImpegno().intValue()) {
							if(oggettoDaPopolareSubimpegno()){
								errori.add(ErroreFin.DATA_SCADENZA_IMPEGNO_ERRATA_PARAMETRIZZATA.getErrore("impegno"));
							}else{
								errori.add(ErroreFin.DATA_SCADENZA_IMPEGNO_ERRATA_PARAMETRIZZATA.getErrore("accertamento"));
							}
						}
					}
				}
			}
		    
		    // jira siac-1748
		    // controllo di congruenza di CIG e CUP
		    controlliCigCupESiopePlus(model.getStep1ModelSubimpegno(), errori);
		    //
		    
			return errori;
		}
	   
	   /**
	    * Per inserisci/aggiorna sub impegnp
	    * @return
	    */
	   protected boolean presenzaWarningsSubimpegno() {
			boolean presenzaWarningsSubimpegno = false;
		    if (controlloSoggettoInSubimpegni() && !model.isProseguiConWarningControlloSoggetti()) {
		    	//presenti errori in controlloSoggettoInSubimpegni che ha restituito true
		    	//e prosegui con warning e' a false
				Errore errorWarning = ErroreFin.DATO_SOGGETTO_PRESENTE.getErrore("Soggetto", model.getStep1ModelSubimpegno().getSoggetto().getCodCreditore());
				addPersistentActionWarning(errorWarning.getDescrizione());
				presenzaWarningsSubimpegno = Boolean.TRUE;
				model.setProseguiConWarningControlloSoggetti(true);
		    }
			return presenzaWarningsSubimpegno;
		}
	   
	   /**
	    * Restituisce true se presenti errori
	    * @return
	    */
	   protected boolean controlloSoggettoInSubimpegni() {
		   boolean errore = false;
		   if (model.getStep1ModelSubimpegno().getSoggetto() != null && 
				   model.getStep1ModelSubimpegno().getSoggetto().getCodCreditore() != null && !"".equalsIgnoreCase(model.getStep1ModelSubimpegno().getSoggetto().getCodCreditore())) {
			   
			   if(oggettoDaPopolareSubimpegno()){
				   // SUBIMPEGNO
				   
				   List<SubImpegno> allSubs = model.getListaTuttiSubimpegniDatiMinimi();
				   
				   if(!isEmpty(allSubs)){
					   for (SubImpegno currentSubimpegno : allSubs) {
						   if (model.getStep1ModelSubimpegno().getUid() == null || model.getStep1ModelSubimpegno().getUid().intValue() != currentSubimpegno.getUid()) {
							   if (currentSubimpegno.getStatoOperativoMovimentoGestioneSpesa()!=null && 
									!currentSubimpegno.getStatoOperativoMovimentoGestioneSpesa().equals(CostantiFin.MOVGEST_STATO_ANNULLATO) &&  // diverso da stato annullato
									 currentSubimpegno.getSoggetto() != null && currentSubimpegno.getSoggetto().getCodiceSoggetto() != null && !"".equalsIgnoreCase(currentSubimpegno.getSoggetto().getCodiceSoggetto())) {
								   if (currentSubimpegno.getSoggetto().getCodiceSoggetto().equalsIgnoreCase(model.getStep1ModelSubimpegno().getSoggetto().getCodCreditore())) {
									   errore = true;
									   break;
								   }
							   }
						   }
					   }
				   }
			   }else{
				   
				   // SUBACCERTAMENTO
				   if(!isEmpty(model.getListaSubaccertamenti())){
					   for (SubAccertamento currentSubAccertamento : model.getListaSubaccertamenti()) {
						   if (model.getStep1ModelSubimpegno().getUid() == null || model.getStep1ModelSubimpegno().getUid().intValue() != currentSubAccertamento.getUid()) {
							   if (currentSubAccertamento.getStatoOperativoMovimentoGestioneEntrata()!=null && 
									 !currentSubAccertamento.getStatoOperativoMovimentoGestioneEntrata().equals(CostantiFin.MOVGEST_STATO_ANNULLATO) &&  // diverso da stato annullato
									   currentSubAccertamento.getSoggetto() != null && currentSubAccertamento.getSoggetto().getCodiceSoggetto() != null &&
									   !"".equalsIgnoreCase(currentSubAccertamento.getSoggetto().getCodiceSoggetto())) {
								   if (currentSubAccertamento.getSoggetto().getCodiceSoggetto().equalsIgnoreCase(model.getStep1ModelSubimpegno().getSoggetto().getCodCreditore())) {
									   errore = true;
									   break;
								   }
							   }
						   }
					   }
				   }
			   }
		   }
		   return errore;
	   }
	   
	   /**
	    * Compone l'oggetto SubImpegno per la chiamata al servizio
	    * @return
	    */
	   protected SubImpegno convertiSubimpegnoModelToSubimpegno() {
		   copiaTransazioneElementareSupportSuModel(true);
		    SubImpegno subImpegno = new SubImpegno();
		    subImpegno.setStatoOperativoMovimentoGestioneSpesa(convertiStatoToCodifica(model.getStep1ModelSubimpegno().getStato()));
		    
		    if(model.getStep1ModelSubimpegno().getOggettoImpegno()!=null){
		    	subImpegno.setDescrizione(model.getStep1ModelSubimpegno().getOggettoImpegno().toUpperCase());
		    }
		    subImpegno.setImportoAttuale(model.getStep1ModelSubimpegno().getImportoImpegno());
		    subImpegno.setImportoIniziale(model.getStep1ModelSubimpegno().getImportoImpegno());
		    
		    subImpegno.setCig(model.getStep1ModelSubimpegno().getCig());
		    subImpegno.setCup(model.getStep1ModelSubimpegno().getCup());
		    
		    //SIOPE PLUS
		    impostaDatiSiopePlusPerInserisciAggiorna(model.getStep1ModelSubimpegno(), subImpegno);
		    //END SIOPE PLUS
		    
			Progetto progetto = new Progetto();
			progetto.setCodice(model.getStep1ModelSubimpegno().getProgetto());
			subImpegno.setProgetto(progetto);
			if (model.getStep1ModelSubimpegno().getScadenza() != null && !"".equalsIgnoreCase(model.getStep1ModelSubimpegno().getScadenza())) {
				subImpegno.setDataScadenza(DateUtility.parse(model.getStep1ModelSubimpegno().getScadenza()));
			}
			if (model.getStep1ModelSubimpegno().getProvvedimento() != null && model.getStep1ModelSubimpegno().isProvvedimentoSelezionato()) {
				AttoAmministrativo atto = popolaProvvedimento(model.getStep1ModelSubimpegno().getProvvedimento());
				subImpegno.setAttoAmministrativo(atto);
			}
			if (model.getStep1ModelSubimpegno().getSoggetto().getCodCreditore() != null && !"".equalsIgnoreCase(model.getStep1ModelSubimpegno().getSoggetto().getCodCreditore())) {
				Soggetto soggetto = new Soggetto();
				soggetto.setCodiceSoggetto(model.getStep1ModelSubimpegno().getSoggetto().getCodCreditore());
				subImpegno.setSoggetto(soggetto);
			}
			subImpegno.setCodMissione(model.getMissioneSelezionata());
			subImpegno.setCodProgramma(model.getProgrammaSelezionato());
			subImpegno.setCodPdc(model.getPianoDeiConti().getCodice()); 
			// CR-2023 eliminare conto economico, tolto il set sul sub impegno
			subImpegno.setCodSiope(model.getSiopeSpesa().getCodice());
			subImpegno.setCodCofog(model.getCofogSelezionato());
			subImpegno.setCodTransazioneEuropeaSpesa(model.getTransazioneEuropeaSelezionato());
			subImpegno.setCodRicorrenteSpesa(model.getRicorrenteSpesaSelezionato());
			subImpegno.setCodCapitoloSanitarioSpesa(model.getPerimetroSanitarioSpesaSelezionato());
			subImpegno.setCodPrgPolReg(model.getPoliticaRegionaleSelezionato());
			if (model.getStep1ModelSubimpegno().getUid() != null) {
				subImpegno.setUid(model.getStep1ModelSubimpegno().getUid());
			}
			
			//non vendiva settato il numero:
			if(model.getStep1ModelSubimpegno().getNumeroImpegno()!=null){
				subImpegno.setNumeroBigDecimal(new BigDecimal(model.getStep1ModelSubimpegno().getNumeroImpegno().intValue()));
			}
			
		    return subImpegno;
	   }
	   
	   /**
	    * Compone l'oggetto SubAccertamento per la chiamata al servizio
	    * @param numeroSubAccertamento
	    * @return
	    */
	   protected SubAccertamento convertiSubaccertamentoModelToSubaccertamento(BigDecimal numeroSubAccertamento) {
		   copiaTransazioneElementareSupportSuModel(true);
		    SubAccertamento subaccertamento = new SubAccertamento();
		    subaccertamento.setStatoOperativoMovimentoGestioneEntrata(convertiStatoToCodifica(model.getStep1ModelSubimpegno().getStato()));
		    
		    if(model.getStep1ModelSubimpegno().getOggettoImpegno()!=null){
		    	subaccertamento.setDescrizione(model.getStep1ModelSubimpegno().getOggettoImpegno().toUpperCase());
		    }
		    subaccertamento.setImportoAttuale(model.getStep1ModelSubimpegno().getImportoImpegno());
		    subaccertamento.setImportoIniziale(model.getStep1ModelSubimpegno().getImportoImpegno());
			Progetto progetto = new Progetto();
			progetto.setCodice(model.getStep1ModelSubimpegno().getProgetto());
			progetto.setTipoProgetto(TipoProgetto.GESTIONE);
			progetto.setBilancio(sessionHandler.getBilancio());
			subaccertamento.setProgetto(progetto);
			if (model.getStep1ModelSubimpegno().getScadenza() != null && !"".equalsIgnoreCase(model.getStep1ModelSubimpegno().getScadenza())) {
				subaccertamento.setDataScadenza(DateUtility.parse(model.getStep1ModelSubimpegno().getScadenza()));
			}
			if (model.getStep1ModelSubimpegno().getProvvedimento() != null && model.getStep1ModelSubimpegno().isProvvedimentoSelezionato()) {
				AttoAmministrativo atto = popolaProvvedimento(model.getStep1ModelSubimpegno().getProvvedimento());
				subaccertamento.setAttoAmministrativo(atto);
			}
			if (model.getStep1ModelSubimpegno().getSoggetto().getCodCreditore() != null && !"".equalsIgnoreCase(model.getStep1ModelSubimpegno().getSoggetto().getCodCreditore())) {
				Soggetto soggetto = new Soggetto();
				soggetto.setCodiceSoggetto(model.getStep1ModelSubimpegno().getSoggetto().getCodCreditore());
				subaccertamento.setSoggetto(soggetto);
			}
			
		 	//SIAC-5801: dava null pointer per assenza id pdc:
			subaccertamento.setIdPdc(model.getPianoDeiConti().getUid());
			//
			
			subaccertamento.setCodPdc(model.getPianoDeiConti().getCodice()); 
			// CR-2023 eliminare conto economico, tolto il set sul sub accertamento
			subaccertamento.setCodSiope(model.getSiopeSpesa().getCodice());
			subaccertamento.setCodCofog(model.getCofogSelezionato());
			subaccertamento.setCodTransazioneEuropeaSpesa(model.getTransazioneEuropeaSelezionato());
			subaccertamento.setCodRicorrenteSpesa(model.getRicorrenteEntrataSelezionato());
			subaccertamento.setCodCapitoloSanitarioSpesa(model.getPerimetroSanitarioEntrataSelezionato());
			subaccertamento.setCodPrgPolReg(model.getPoliticaRegionaleSelezionato());
			if (model.getStep1ModelSubimpegno().getUid() != null) {
				subaccertamento.setUid(model.getStep1ModelSubimpegno().getUid());
				
			}
			if(numeroSubAccertamento!=null && numeroSubAccertamento.intValue()>0){
				subaccertamento.setNumeroBigDecimal(numeroSubAccertamento);
			}
		    return subaccertamento;
	   }
	   
	   /**
	    * effettua la ricerca da servizio del provvedimento
	    * @param uidAtto
	    * @return
	    */
		protected AttoAmministrativo ricercaAttoAmministrativo(Integer uidAtto){
			
			AttoAmministrativo atto = new AttoAmministrativo();
			boolean cache = false;
			
			for(AttoAmministrativo app : model.getAtti()){
				if(app.getUid() == uidAtto){
					atto = app;
					cache = true;
				}
			}
			
			if(!cache){
				RicercaProvvedimento ricercaProvvedimento = new RicercaProvvedimento();
				RicercaAtti atti = new RicercaAtti();
				
				atti.setUid(uidAtto);
				ricercaProvvedimento.setRicercaAtti(atti);
				ricercaProvvedimento.setEnte(sessionHandler.getEnte());
				ricercaProvvedimento.setRichiedente(sessionHandler.getRichiedente());
				
				RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(ricercaProvvedimento);
				
				if(!isEmpty(response.getListaAttiAmministrativi())){
					atto = response.getListaAttiAmministrativi().get(0);
					//inserisco nella cache
					model.getAtti().add(response.getListaAttiAmministrativi().get(0));
				}
				
			}
			
			return atto;
		}
	   
	   protected void caricaListaMotivi(){
		   
			Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.TIPO_MOTIVO);
			model.getMovimentoSpesaModel().setListaTipoMotivo((List<CodificaFin>)mappaListe.get(TipiLista.TIPO_MOTIVO));
	   }

	   @SuppressWarnings("unchecked")
	protected void caricaListaMotiviAgg(){
			Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.TIPO_MOTIVO_AGG);
			model.getMovimentoSpesaModel().setListaTipoMotivoAggiudicazione((List<CodificaFin>)mappaListe.get(TipiLista.TIPO_MOTIVO_AGG));
	   }

	   
	   /**
	    * Rm SIAC-5371
	    * Se la condizione è soddisfatta devo scremare la lista dei motivi  
	    */
	   @SuppressWarnings("unchecked")
	   protected void caricaListaMotiviROR(){
		   
			Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.TIPO_MOTIVO_ROR);
			model.getMovimentoSpesaModel().setListaTipoMotivo((List<CodificaFin>)mappaListe.get(TipiLista.TIPO_MOTIVO_ROR));
	   }

		/**
		 * GESTORE TRANSAZIONE ECONOMICA
		 */
		@Override
		public String confermaPdc() {
		   teSupport.setRicaricaAlberoPianoDeiConti(false);
				   	
		   // pulisce gli alberi in caso di cambiamento del pdc
		   // CR-2023 eliminare conto economico,  i set che c'erano qui vengono tolti 
		   //(setRicaricaAlberoContoEconomico  e setContoEconomico in te support)
			
		   // CONTROLLO SUI QUARTI LIVELLI
		   if(null!=teSupport.getPianoDeiConti() && teSupport.getPianoDeiConti().getCodice().endsWith("000")){
				teSupport.setPianoDeiConti(new ElementoPianoDeiConti());
				addErrore(ErroreFin.ELEM_PDC_NON_INDICATO.getErrore(""));
				
		   }
			
		   return SUCCESS;
		}	
		   

		/**
		 * Metodo per popolare la struttura della transazione elementare
		 */
		public void popolaStrutturaTransazioneElementare(){
			
			//MISSIONE
			if(model.getImpegnoInAggiornamento().getCodMissione()!=null){
				teSupport.setMissioneSelezionata(model.getImpegnoInAggiornamento().getCodMissione());
			}
			
			//PROGRAMMA
			if(model.getImpegnoInAggiornamento().getCodProgramma()!=null){
				teSupport.setProgrammaSelezionato(model.getImpegnoInAggiornamento().getCodProgramma());
			}

			
			// MACRO AGGREGATO
			if(model.getStep1Model().getCapitolo().getIdMacroAggregato()!=null){
				teSupport.setIdMacroAggregato(model.getStep1Model().getCapitolo().getIdMacroAggregato());
			}
			//albero PDC
			if(model.getImpegnoInAggiornamento().getCodPdc()!=null){
				teSupport.getPianoDeiConti().setCodice(model.getImpegnoInAggiornamento().getCodPdc());
			}
			
			//DESCRIZIONE PIANO DEI CONTI
			if(model.getImpegnoInAggiornamento().getDescPdc() != null){
				teSupport.getPianoDeiConti().setDescrizione(model.getImpegnoInAggiornamento().getCodPdc()+" - "+ model.getImpegnoInAggiornamento().getDescPdc());
			}
			
			//ID PIANO DEI CONTI
			if(model.getImpegnoInAggiornamento().getIdPdc() != null){
				teSupport.getPianoDeiConti().setUid(model.getImpegnoInAggiornamento().getIdPdc());
			}
			
			//PDC Padre per creazione albero
			if(model.getStep1Model().getCapitolo().getIdPianoDeiConti() != null){
				teSupport.setIdPianoDeiContiPadrePerAggiorna(model.getStep1Model().getCapitolo().getIdPianoDeiConti());
			}
			
			//CR-2023 eliminare conto economico,  i set che c'erano qui vengono tolti 
			
			//COFOG SELEZIONATO
			if(model.getImpegnoInAggiornamento().getCodCofog()!=null){
				teSupport.setCofogSelezionato(model.getImpegnoInAggiornamento().getCodCofog());
			}
			
			//TRANSAZIONE EUROPEA SELEZIONATO
			if(model.getImpegnoInAggiornamento().getCodTransazioneEuropeaSpesa()!=null){
				teSupport.setTransazioneEuropeaSelezionato(model.getImpegnoInAggiornamento().getCodTransazioneEuropeaSpesa());
			}
			
			//RICORRENTE SPESA SELEZIONATO
			if(model.getImpegnoInAggiornamento().getCodRicorrenteSpesa()!=null){
				teSupport.setRicorrenteSpesaSelezionato(model.getImpegnoInAggiornamento().getCodRicorrenteSpesa());
			}
			
			//PERIMETRO SANITARIO SPESA SELEZIONATO
			if(model.getImpegnoInAggiornamento().getCodCapitoloSanitarioSpesa()!=null){
				teSupport.setPerimetroSanitarioSpesaSelezionato(model.getImpegnoInAggiornamento().getCodCapitoloSanitarioSpesa());
			}
			
			//POLITICA REGIONALE SELEZIONATO
			if(model.getImpegnoInAggiornamento().getCodPrgPolReg()!=null){
				teSupport.setPoliticaRegionaleSelezionato(model.getImpegnoInAggiornamento().getCodPrgPolReg());
			}
			
			//CLASSIFICATORE GENERICO SELEZIONATO 1
			if(model.getImpegnoInAggiornamento().getCodClassGen11()!=null){
				teSupport.setClassGenSelezionato1(model.getImpegnoInAggiornamento().getCodClassGen11());
			}
			
			//CLASSIFICATORE GENERICO SELEZIONATO 2
			if(model.getImpegnoInAggiornamento().getCodClassGen12()!=null){
				teSupport.setClassGenSelezionato2(model.getImpegnoInAggiornamento().getCodClassGen12());
			}
			
			//CLASSIFICATORE GENERICO SELEZIONATO 3
			if(model.getImpegnoInAggiornamento().getCodClassGen13()!=null){
				teSupport.setClassGenSelezionato3(model.getImpegnoInAggiornamento().getCodClassGen13());
			}
			
			//CLASSIFICATORE GENERICO SELEZIONATO 4
			if(model.getImpegnoInAggiornamento().getCodClassGen14()!=null){
				teSupport.setClassGenSelezionato4(model.getImpegnoInAggiornamento().getCodClassGen14());
			}
			
			//CLASSIFICATORE GENERICO SELEZIONATO 5
			if(model.getImpegnoInAggiornamento().getCodClassGen15()!=null){
				teSupport.setClassGenSelezionato5(model.getImpegnoInAggiornamento().getCodClassGen15());
			}

		}
		
		/**
		 * Serve per capire se lanciare il warning di altri movimenti collegati in caso di modifica al piano dei conti
		 * di un impegno/accertamento/subimpegno/subaccertamento
		 * @param movimentoGestione
		 * @return
		 */
		protected <MG extends MovimentoGestione> boolean controlloMovimentiCollegatiPerModificaPianoDeiConti(MG movimentoGestione){
			//CONTROLLO PIANO DEI CONTI SE CI SONO MOVIMENTI COLLEGATI:
			if(teSupport!=null && movimentoGestione!=null){
				if(teSupport.getPianoDeiConti()!=null && movimentoGestione.getIdPdc()!=null 
						&& teSupport.getPianoDeiConti().getUid()!=movimentoGestione.getIdPdc().intValue()){
					//E' stato cambiato il piano dei conti
					//devo verificare che non ci siano movimenti collegati:
					BigDecimal importoAttuale = movimentoGestione.getImportoAttuale();
					
					BigDecimal disponibilitaConfronto = null;
					Errore warning = null;
					
					if(movimentoGestione instanceof ImpegnoAbstract){
						disponibilitaConfronto = ((ImpegnoAbstract)movimentoGestione).getDisponibilitaLiquidareBase();
						
						warning = ErroreFin.WARNING_IMPEGNO_SUBIMPEGNO_PER_PDC_MODIFICATO.getErrore("L'impegno");
						if(movimentoGestione instanceof SubImpegno){
							warning = ErroreFin.WARNING_IMPEGNO_SUBIMPEGNO_PER_PDC_MODIFICATO.getErrore("Il subimpegno");
						}
						
					} else if(movimentoGestione instanceof AccertamentoAbstract){
						disponibilitaConfronto = ((AccertamentoAbstract)movimentoGestione).getDisponibilitaIncassare();
						
						warning = ErroreFin.MOVIMENTO_COLLEGATO_PER_PDC_MODIFICATO.getErrore("accertamento");
						if(movimentoGestione instanceof SubAccertamento){
							warning = ErroreFin.SUB_MOVIMENTO_COLLEGATO_PER_PDC_MODIFICATO.getErrore("subaccertamento");
						}
					}
					
					if(disponibilitaConfronto!=null && disponibilitaConfronto.compareTo(importoAttuale)!=0){
						if(!model.isProseguiConWarningPianoDeiConti()){
							addPersistentActionWarning(warning.getCodice() +" : " + warning.getDescrizione());
							model.setProseguiConWarningPianoDeiConti(true);
							//RETURN WARNING:
							return false;
						}
					}
				}
			}
			//TUTTO OK:
			return true;
		}
		
		/**
		 * per checkare se si e' abilitata la modifica del progetto
		 * @return
		 */
		public boolean abilitaModificaProgetto() {
			boolean abilitazioneModifica = false;
			
			/*
			
			vecchio controllo non eliminare, puo' essere utile come traccia
			se venissero richiesti di nuovo controlli simili
			
			if(oggettoDaPopolareImpegno() || oggettoDaPopolareSubimpegno()){
				if(model.getImpegnoInAggiornamento()!=null && model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa()!=null){
					
					if(model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa().toString().equalsIgnoreCase(CostantiFin.MOVGEST_STATO_PROVVISORIO)){
						//se provvisorio e' modificabile
						abilitazioneModifica = true;
					} else if(model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa().toString().equalsIgnoreCase(CostantiFin.MOVGEST_STATO_DEFINITIVO)
							|| model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa().toString().equalsIgnoreCase(CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE)){
						//se impegno definitivo 
						if(isEnteConProgettoObbligatorio()){
							//un impegno definitivo con variabile di ente progetto obbligatorio deve permettere la modifica 
							//del progetto se il progetto e' assente (perche' in tal caso si tratta di impegni nati
							//prima della variabile di obblitatorieta del progetto
							if(model.getImpegnoInAggiornamento().getProgetto()==null ||
									StringUtils.isEmpty(model.getImpegnoInAggiornamento().getProgetto().getCodice())){
								abilitazioneModifica = true;
							}
						}
					}
					
				}
			} else {
				if(model.getAccertamentoInAggiornamento()!=null && model.getAccertamentoInAggiornamento().getStatoOperativoMovimentoGestioneEntrata()!=null){
					if(model.getAccertamentoInAggiornamento().getStatoOperativoMovimentoGestioneEntrata().toString().equalsIgnoreCase(CostantiFin.MOVGEST_STATO_PROVVISORIO)){
						//se provvisorio e' modificabile
						abilitazioneModifica = true;
					}
				}
			}
			
			*/
			
			//13 febbraio 2017: rilassimo i vincoli di aggiornabilita' sul progetto:
			String stato = null;
			if(oggettoDaPopolareImpegno() || oggettoDaPopolareSubimpegno()){
				stato = model.getImpegnoInAggiornamento().getStatoOperativoMovimentoGestioneSpesa().toString();
			} else {
				stato = model.getAccertamentoInAggiornamento().getStatoOperativoMovimentoGestioneEntrata().toString();
			}
			if(stato!=null && !stato.equalsIgnoreCase(CostantiFin.MOVGEST_STATO_ANNULLATO)){
				abilitazioneModifica = true;
			}
			//
			
			
			return abilitazioneModifica;
		}
		
		/**
		 * Metodo per popolare la struttura della transazione elementare
		 */
		public void popolaStrutturaTransazioneElementareAcc(){
			//ATTENZIONE: CR 2023 si elimina il conto economico
			
			//MISSIONE SELEZIONATA
			teSupport.setMissioneSelezionata(model.getAccertamentoInAggiornamento().getCodMissione());
			
			//PROGRAMMA SELEZIONATO
			teSupport.setProgrammaSelezionato(model.getAccertamentoInAggiornamento().getCodProgramma());
			
			//ID PIANO DEI CONTI
			if (model.getAccertamentoInAggiornamento().getIdPdc() != null) {
				teSupport.getPianoDeiConti().setUid(model.getAccertamentoInAggiornamento().getIdPdc());
			}
			
			//CODICE PIANO DEI CONTI
			teSupport.getPianoDeiConti().setCodice(model.getAccertamentoInAggiornamento().getCodPdc());
			
			//DESCRIZIONE PIANO DEI CONTI
			teSupport.getPianoDeiConti().setDescrizione(model.getAccertamentoInAggiornamento().getCodPdc()+" - "+model.getAccertamentoInAggiornamento().getDescPdc());

			//CR-2023 eliminare conto economico,  i set che c'erano qui vengono tolti 
			
			//COFOG SELEZIONATO
			teSupport.setCofogSelezionato(model.getAccertamentoInAggiornamento().getCodCofog());
			
			//TRANSAZIONE EUROPEA SELEZIONATO
			teSupport.setTransazioneEuropeaSelezionato(model.getAccertamentoInAggiornamento().getCodTransazioneEuropeaSpesa());
			
			//CODICE RICORRENTE SPESA
			teSupport.setRicorrenteEntrataSelezionato(model.getAccertamentoInAggiornamento().getCodRicorrenteSpesa());
			
			//CODICE CAPITOLO SANITARIO SPESA
			teSupport.setPerimetroSanitarioEntrataSelezionato(model.getAccertamentoInAggiornamento().getCodCapitoloSanitarioSpesa());
			
			//CODICE PROGRAMMA POLITICHE REGIONALI
			teSupport.setPoliticaRegionaleSelezionato(model.getAccertamentoInAggiornamento().getCodPrgPolReg());
			
			//CODICE CLASSIFICATORE GENERICO 16
			if(model.getAccertamentoInAggiornamento().getCodClassGen16()!=null){
				teSupport.setClassGenSelezionato6(model.getAccertamentoInAggiornamento().getCodClassGen16());
			}
			
			//CODICE CLASSIFICATORE GENERICO 17
			if(model.getAccertamentoInAggiornamento().getCodClassGen17()!=null){
				teSupport.setClassGenSelezionato7(model.getAccertamentoInAggiornamento().getCodClassGen17());
			}
			
			//CODICE CLASSIFICATORE GENERICO 18
			if(model.getAccertamentoInAggiornamento().getCodClassGen18()!=null){
				teSupport.setClassGenSelezionato8(model.getAccertamentoInAggiornamento().getCodClassGen18());
			}
			
			//CODICE CLASSIFICATORE GENERICO 19
			if(model.getAccertamentoInAggiornamento().getCodClassGen19()!=null){
				teSupport.setClassGenSelezionato9(model.getAccertamentoInAggiornamento().getCodClassGen19());
			}
			
			//CODICE CLASSIFICATORE GENERICO 20
			if(model.getAccertamentoInAggiornamento().getCodClassGen20()!=null){
				teSupport.setClassGenSelezionato10(model.getAccertamentoInAggiornamento().getCodClassGen20());
			}
		}			
	   	   
		public Impegno getImpegnoToUpdate() {
			return impegnoToUpdate;
		}

		public void setImpegnoToUpdate(Impegno impegnoToUpdate) {
			this.impegnoToUpdate = impegnoToUpdate;
		}

		public CapitoloUscitaGestione getCapitoloUscitaTrovato() {
			return capitoloUscitaTrovato;
		}

		public void setCapitoloUscitaTrovato(CapitoloUscitaGestione capitoloTrovato) {
			this.capitoloUscitaTrovato = capitoloTrovato;
		}
		//PROVA TRASLOCO
		public Soggetto getSoggettoCreditore() {
			return soggettoCreditore;
		}

		public void setSoggettoCreditore(Soggetto soggettoCreditore) {
			this.soggettoCreditore = soggettoCreditore;
		}

		public boolean isProvvedimentoAbilitato() {
			return provvedimentoAbilitato;
		}

		public void setProvvedimentoAbilitato(boolean provvedimentoAbilitato) {
			this.provvedimentoAbilitato = provvedimentoAbilitato;
		}

		public boolean isSoggettoAbilitato() {
			return soggettoAbilitato;
		}

		public void setSoggettoAbilitato(boolean soggettoAbilitato) {
			this.soggettoAbilitato = soggettoAbilitato;
		}

		public boolean isProgettoAbilitato() {
			return progettoAbilitato;
		}

		public void setProgettoAbilitato(boolean progettoAbilitato) {
			this.progettoAbilitato = progettoAbilitato;
		}

		public boolean isImportoAbilitato() {
			return importoAbilitato;
		}

		public void setImportoAbilitato(boolean importoAbilitato) {
			this.importoAbilitato = importoAbilitato;
		}


		public boolean isSuccessStep1() {
			return successStep1;
		}

		public void setSuccessStep1(boolean successStep1) {
			this.successStep1 = successStep1;
		}


		public CapitoloEntrataGestione getCapitoloEntrataTrovato() {
			return capitoloEntrataTrovato;
		}


		public void setCapitoloEntrataTrovato(CapitoloEntrataGestione capitoloTrovato) {
			capitoloEntrataTrovato = capitoloTrovato;
		}


		public ClasseSoggetto getClasseCreditore() {
			return classeCreditore;
		}


		public void setClasseCreditore(ClasseSoggetto classeCreditore) {
			this.classeCreditore = classeCreditore;
		}


		public Accertamento getAccertamentoToUpdate() {
			return accertamentoToUpdate;
		}


		public void setAccertamentoToUpdate(Accertamento accertamento) {
			this.accertamentoToUpdate = accertamento;
		}

		public String getDoveMiTrovo() {
			return doveMiTrovo;
		}

		public void setDoveMiTrovo(String doveMiTrovo) {
			this.doveMiTrovo = doveMiTrovo;
		}

		public boolean isShowPopUpMovColl() {
			return showPopUpMovColl;
		}

		public void setShowPopUpMovColl(boolean showPopUpMovColl) {
			this.showPopUpMovColl = showPopUpMovColl;
		}

		public boolean isProseguiStep1() {
			return proseguiStep1;
		}

		public void setProseguiStep1(boolean proseguiStep1) {
			this.proseguiStep1 = proseguiStep1;
		}

		public boolean isFromPopup() {
			return fromPopup;
		}

		public void setFromPopup(boolean fromPopup) {
			this.fromPopup = fromPopup;
		}

		public Boolean getAbilitaModificaImporto() {
			return abilitaModificaImporto;
		}

		public void setAbilitaModificaImporto(Boolean abilitaModificaImporto) {
			this.abilitaModificaImporto = abilitaModificaImporto;
		}
		
		//start SIAC-7861
		public String getUidPerDettaglioSub() {
			return uidPerDettaglioSub;
		}

		public void setUidPerDettaglioSub(String uidPerDettaglioSub) {
			this.uidPerDettaglioSub = uidPerDettaglioSub;
		}

		public String getUidSubDaAnnullare() {
			return uidSubDaAnnullare;
		}

		public void setUidSubDaAnnullare(String uidSubDaAnnullare) {
			this.uidSubDaAnnullare = uidSubDaAnnullare;
		}

		public String getNumeroSubDaAnnullare() {
			return numeroSubDaAnnullare;
		}

		public void setNumeroSubDaAnnullare(String numeroSubDaAnnullare) {
			this.numeroSubDaAnnullare = numeroSubDaAnnullare;
		}

		public String getUidSubDaEliminare() {
			return uidSubDaEliminare;
		}

		public void setUidSubDaEliminare(String uidSubDaEliminare) {
			this.uidSubDaEliminare = uidSubDaEliminare;
		}

		public String getNumeroSubDaEliminare() {
			return numeroSubDaEliminare;
		}

		public void setNumeroSubDaEliminare(String numeroSubDaEliminare) {
			this.numeroSubDaEliminare = numeroSubDaEliminare;
		}
		//end SIAC-7861

		/**
		 * @return the showModaleConfermaProseguiModificaProvvedimento
		 */
		public boolean isShowModaleConfermaProseguiModificaProvvedimento() {
			return showModaleConfermaProseguiModificaProvvedimento;
		}

		/**
		 * @param showModaleConfermaProseguiModificaProvvedimento the showModaleConfermaProseguiModificaProvvedimento to set
		 */
		public void setShowModaleConfermaProseguiModificaProvvedimento(
				boolean showModaleConfermaProseguiModificaProvvedimento) {
			this.showModaleConfermaProseguiModificaProvvedimento = showModaleConfermaProseguiModificaProvvedimento;
		}

		/**
		 * @return the fromModaleConfermaProseguiModificaProvvedimento
		 */
		public boolean isFromModaleConfermaProseguiModificaProvvedimento() {
			return fromModaleConfermaProseguiModificaProvvedimento;
		}

		/**
		 * @param fromModaleConfermaProseguiModificaProvvedimento the fromModaleConfermaProseguiModificaProvvedimento to set
		 */
		public void setFromModaleConfermaProseguiModificaProvvedimento(
				boolean fromModaleConfermaProseguiModificaProvvedimento) {
			this.fromModaleConfermaProseguiModificaProvvedimento = fromModaleConfermaProseguiModificaProvvedimento;
		}


		/**
		 * @return the showModaleConfermaSalvaModificaVincoli
		 */
		public boolean isShowModaleConfermaSalvaModificaVincoli() {
			return showModaleConfermaSalvaModificaVincoli;
		}

		/**
		 * @param showModaleConfermaSalvaModificaVincoli the showModaleConfermaSalvaModificaVincoli to set
		 */
		public void setShowModaleConfermaSalvaModificaVincoli(
				boolean showModaleConfermaSalvaModificaVincoli) {
			this.showModaleConfermaSalvaModificaVincoli = showModaleConfermaSalvaModificaVincoli;
		}

		/**
		 * @return the fromModaleConfermaSalvaModificaVincoli
		 */
		public boolean isFromModaleConfermaSalvaModificaVincoli() {
			return fromModaleConfermaSalvaModificaVincoli;
		}

		/**
		 * @param fromModaleConfermaSalvaModificaVincoli the fromModaleConfermaSalvaModificaVincoli to set
		 */
		public void setFromModaleConfermaSalvaModificaVincoli(
				boolean fromModaleConfermaSalvaModificaVincoli) {
			this.fromModaleConfermaSalvaModificaVincoli = fromModaleConfermaSalvaModificaVincoli;
		}

		/**
		 * @return the showModaleConfermaProseguiModificaVincoli
		 */
		public boolean isShowModaleConfermaProseguiModificaVincoli() {
			return showModaleConfermaProseguiModificaVincoli;
		}

		/**
		 * @param showModaleConfermaProseguiModificaVincoli the showModaleConfermaProseguiModificaVincoli to set
		 */
		public void setShowModaleConfermaProseguiModificaVincoli(
				boolean showModaleConfermaProseguiModificaVincoli) {
			this.showModaleConfermaProseguiModificaVincoli = showModaleConfermaProseguiModificaVincoli;
		}

		/**
		 * @return the fromModaleConfermaProseguiModificaVincoli
		 */
		public boolean isFromModaleConfermaProseguiModificaVincoli() {
			return fromModaleConfermaProseguiModificaVincoli;
		}

		/**
		 * @param fromModaleConfermaProseguiModificaVincoli the fromModaleConfermaProseguiModificaVincoli to set
		 */
		public void setFromModaleConfermaProseguiModificaVincoli(
				boolean fromModaleConfermaProseguiModificaVincoli) {
			this.fromModaleConfermaProseguiModificaVincoli = fromModaleConfermaProseguiModificaVincoli;
		}

		
		/**
		 * @return the abilitaBottneProseguiESalva
		 */
		public Boolean isAbilitaBottneSalvaDecentrato() {
			return abilitaBottneSalvaDecentrato;
		}

		/**
		 * @param abilitaBottneProseguiESalva the abilitaBottneProseguiESalva to set
		 */
		public void setAbilitaBottneSalvaDecentrato(Boolean abilitaBottneSalvaDecentrato) {
			this.abilitaBottneSalvaDecentrato = abilitaBottneSalvaDecentrato;
		}
		
		private boolean isNecessariaRichiestaConfermaUtentePerRedirezioneSuContabilitaGenerale() { 
			//l'utente deve essere abilitato
			return model.isEnteAbilitatoGestionePrimaNotaDaFinanziaria() 
					&& (!model.isRichiediConfermaRedirezioneContabilitaGenerale()) 
					&& model.getStep1Model().getAnnoImpegno() == sessionHandler.getAnnoBilancio()
					&& STATO_MOVGEST_DEFINITIVO.equals(model.getStep1ModelSubimpegno().getStato())
					//SIAC-5943: questo vale per i sub
					&& model.getStep1ModelSubimpegno() != null 
					&& model.getStep1ModelSubimpegno().isPassaggioAStatoDefinitivo()
					&& condizioniRedirezioneContabilitaGeneraleSpecifiche();
		}
		
		protected boolean isNecessariaRichiestaConfermaUtentePerRedirezioneSuContabilitaGeneraleAggiornaImpegno() { 
			//l'utente deve essere abilitato
			return model.isEnteAbilitatoGestionePrimaNotaDaFinanziaria() 
					&& (!model.isRichiediConfermaRedirezioneContabilitaGenerale()) 
					&& model.getStep1Model().getAnnoImpegno() == sessionHandler.getAnnoBilancio()
					&& soggettoOClasseSoggetto()
					&& nuovoProvvedimentoDefinitivo()
					&& condizioniRedirezioneContabilitaGeneraleSpecifiche();
			
		}
		
		
		protected boolean isNecessariaRichiestaConfermaUtentePerRedirezioneSuContabilitaGeneraleAggiornaAccertamento() { 
			//l'utente deve essere abilitato
			return model.isEnteAbilitatoGestionePrimaNotaDaFinanziaria() 
					&& (!model.isRichiediConfermaRedirezioneContabilitaGenerale()) 
					&& model.getStep1Model().getAnnoImpegno() == sessionHandler.getAnnoBilancio()
					&& soggettoOClasseSoggetto()
					&& nuovoProvvedimentoDefinitivoAcc()
					&& condizioniRedirezioneContabilitaGeneraleSpecifiche();
			
		}
		
		/**
		 * Semplice utility che ritorna true se almeno uno tra classe e' soggetto e' stato valorizzato
		 * @return
		 */
		private boolean soggettoOClasseSoggetto(){
			if(model.getStep1Model().getSoggetto()!=null){
				if(!isEmpty(model.getStep1Model().getSoggetto().getClasse())){
					return true;
				}
				if(!isEmpty(model.getStep1Model().getSoggetto().getCodCreditore())){
					return true;
				}
			}
			return false;
		}
		
		/**
		 * Semplice utility per verificare se l'impegno in aggiornamento ha subito il cambio di provvedimento
		 * da provvisorio a definitivo
		 * @return
		 */
		private boolean nuovoProvvedimentoDefinitivo(){
			String statoCodOld = getStatoProvvedimentoPrimaDellAggiornamento();
			String statoCodNew = getStatoProvvedimentoCheIntendoAggiornare();
			return passaggioAStatoDefinitivo(statoCodOld, statoCodNew);
		}
		
		/**
		 * Semplice utility per verificare se l'accertamento in aggiornamento ha subito il cambio di provvedimento
		 * da provvisorio a definitivo
		 * @return
		 */
		private boolean nuovoProvvedimentoDefinitivoAcc(){
			String statoCodOld = getStatoProvvedimentoPrimaDellAggiornamentoAcc();
			String statoCodNew = getStatoProvvedimentoCheIntendoAggiornare();
			return passaggioAStatoDefinitivo(statoCodOld, statoCodNew);
		}
		
		/**
		 * Utility per leggere lo stato del provvedimento che si vuole aggiornare per l'impegno 
		 * in aggiornamento
		 * @return
		 */
		private String getStatoProvvedimentoCheIntendoAggiornare(){
			String stato = null;
			if(model.getStep1Model()!=null && model.getStep1Model().getProvvedimento()!=null){
				stato = model.getStep1Model().getProvvedimento().getStato(); 
			}
			return stato;
		}
		
		/**
		 * Utility per leggere lo stato del provvedimento che ancora ha l'impegno 
		 * che si sta aggiornando
		 * @return
		 */
		private String getStatoProvvedimentoPrimaDellAggiornamento(){
			String stato = null;
			if(model.getImpegnoInAggiornamento()!=null && model.getImpegnoInAggiornamento().getAttoAmministrativo()!=null){
				stato = model.getImpegnoInAggiornamento().getAttoAmministrativo().getStatoOperativo();
			}
			return stato;
		}
		
		/**
		 * Utility per leggere lo stato del provvedimento che ancora ha l'accertamento 
		 * che si sta aggiornando
		 * @return
		 */
		private String getStatoProvvedimentoPrimaDellAggiornamentoAcc(){
			String stato = null;
			if(model.getAccertamentoInAggiornamento()!=null && model.getAccertamentoInAggiornamento().getAttoAmministrativo()!=null){
				stato = model.getAccertamentoInAggiornamento().getAttoAmministrativo().getStatoOperativo();
			}
			return stato;
		}
		
		/**
		 * Semplice utility per verificare se lo stato nuovo e' definitivo e quello vecchio non lo era
		 * @param statoCodOld
		 * @param statoCodNew
		 * @return
		 */
		private  boolean passaggioAStatoDefinitivo(String statoCodOld, String statoCodNew){
			boolean passaggioAStatoDefinitivo = false;
			if(statoCodNew!=null && 
					//CostantiFin.MOVGEST_STATO_DEFINITIVO.equals(statoCodNew) && 
					"DEFINITIVO".equals(statoCodNew) && 
					!statoCodNew.equals(statoCodOld) 
					){
				//il nuovo stato e' definitivo ed e' diverso dallo stato vecchio
				passaggioAStatoDefinitivo = true;		
				
			}
			return passaggioAStatoDefinitivo;
		}

		/**
		 * @return
		 */
		protected boolean condizioniRedirezioneContabilitaGeneraleSpecifiche() {
			//da implementare nelle sotto classi
			return true;
		}
		
		//SIAC-7349		
		public RicercaComponenteImportiCapitolo creaRequestRicercaComponenteImportiCapitolo(CapitoloUscitaGestione capitolo) {
			RicercaComponenteImportiCapitolo request = new RicercaComponenteImportiCapitolo();
			request.setDataOra(new Date());
			request.setRichiedente(sessionHandler.getRichiedente());
			request.setAnnoBilancio(sessionHandler.getBilancio().getAnno());
			request.setCapitolo(new CapitoloUscitaPrevisione());
			request.getCapitolo().setUid(capitolo.getUid());
			return request;
		}
		
		
}



/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioAccertamento;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioAccertamentoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioImpegno;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioImpegnoResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.movimentogestione.MutuoAssociatoMovimentoGestione;
import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.exception.ImportoDeltaException;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.commons.ConsultaMovimentiModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CommonConsulta.Soggetto;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ImportiCapitoloModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ModificaConsulta;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.MovimentoConsulta;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.ComparaVincoli;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.ConsultaDettaglioAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.ConsultaDettaglioAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ConsultaDettaglioImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.ConsultaDettaglioImpegnoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ConsultaVincoliAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.ConsultaVincoliAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiStoricoAggiornamentoProvvedimentoMovimentoGestione;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiStoricoAggiornamentoProvvedimentoMovimentoGestioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiStoricoAggiornamentoStrutturaCompetenteMovimentoGestione;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiStoricoAggiornamentoStrutturaCompetenteMovimentoGestioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModulareModificaMovimentoSpesaCollegata;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModulareModificaMovimentoSpesaCollegataResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaModulareVincoliAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaModulareVincoliAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaModulareVincoliImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaModulareVincoliImpegnoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStoricoImpegnoAccertamentoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.AttoAmministrativoStoricizzato;
import it.csi.siac.siacfinser.model.ClasseSoggetto;
import it.csi.siac.siacfinser.model.DettaglioImportiAccertamento;
import it.csi.siac.siacfinser.model.DettaglioImportiImpegno;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.StoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.model.StrutturaAmmContabileFlatStoricizzato;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione.StatoOperativoModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;
import it.csi.siac.siacfinser.model.movgest.VincoloAccertamento;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import xyz.timedrain.arianna.plugin.BreadCrumb;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaMovimentoAction extends MovGestAction<ConsultaMovimentiModel> {
	
	private static final long serialVersionUID = 2652920214934382402L;
	
	private String uidPerDettaglioSub;
	private String uidPerDettaglioMod;
	
	private String uidSubMovimento;

	private String anno;
	private String numero;
	private String tipo;
	
	private boolean visualizzaLinkConsultaModificheProvvedimento = false;
	
	protected static final String LABEL_OGGETTO_GENERICO_PADRE = "OGGETTO_GENERICO_PADRE";
	protected static final String LABEL_OGGETTO_GENERICO_PADRE_LOWER_CASE = "OGGETTO_GENERICO_PADRE_LOWER_CASE";
	protected static final String BLOCCO_RAGIONERIA_SI = "SI";
	protected static final String BLOCCO_RAGIONERIA_NO = "NO";
	protected static final String BLOCCO_RAGIONERIA_NA = "N/A";
	
	//task-93
	@Autowired @Qualifier("movimentoGestioneBilService")
	protected MovimentoGestioneService movimentoGestioneBilService;
	
	private static final Comparator<MutuoAssociatoMovimentoGestione> MutuoAssociatoMovimentoGestioneNumeroMutuoComparator = new Comparator<MutuoAssociatoMovimentoGestione>() {
		@Override
		public int compare(MutuoAssociatoMovimentoGestione arg0, MutuoAssociatoMovimentoGestione arg1) {
			return arg0.getMutuo().getNumero().compareTo(arg1.getMutuo().getNumero());
		}};	
	
	public boolean isImpegnoSdf(){
		if (tipo.equals("I")) {
			//ha senso solo per impegno
			return model.getMovimento().isImpegnoSDF();
		}
		return false;
	}

	@Override
	public String getActionKey() {
		return "consultaImpegno";
	}
	
	
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		setVisualizzaLinkConsultaModificheProvvedimento(true);
		
		//setto il titolo:
		this.model.setTitolo("Consulta movimento");
	}	

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		if (tipo.equals("I")) {
			
			RicercaImpegnoPerChiaveOttimizzatoResponse response = getImpegno();
			if (response == null){
				// ritorno errore
				return INPUT;
			}
			
			// SIAC-6268
			//transazione elementare:
			if(teSupport==null){
				pulisciTransazioneElementare();
			}
			
			// utilizzato per la transazione e le condizioni di obbligatorieta
			teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.IMPEGNO.toString());
			caricaListeFin("I");
			
			List<ClassificatoreGenerico> lista11 = teSupport.getListaClassificatoriGen11();
			
			String codiceDaCercare = response.getImpegno().getCodClassGen11();
			
			for (int i = 0; i < lista11.size(); i++) {
				if (lista11.get(i).getCodice().equals(codiceDaCercare)) {
					model.getMovimento().setTitoloGiuridico(lista11.get(i).getCodice() + " - " + lista11.get(i).getDescrizione());
					break;
				}
			}
			
			List<ClassificatoreGenerico> lista12 = teSupport.getListaClassificatoriGen12();
			
			codiceDaCercare = response.getImpegno().getCodClassGen12();
			
			for (int i = 0; i < lista12.size(); i++) {
				if (lista12.get(i).getCodice().equals(codiceDaCercare)) {
					model.getMovimento().setTipoTracciabilita(lista12.get(i).getCodice() + " - " + lista12.get(i).getDescrizione());
					break;
				}
			}
			
			List<ClassificatoreGenerico> lista13 = teSupport.getListaClassificatoriGen13();
			
			codiceDaCercare = response.getImpegno().getCodClassGen13();
			
			for (int i = 0; i < lista13.size(); i++) {
				if (lista13.get(i).getCodice().equals(codiceDaCercare)) {
					model.getMovimento().setVoceTracciabilita(lista13.get(i).getCodice() + " - " + lista13.get(i).getDescrizione());
					break;
				}
			}
			
			List<ClassificatoreGenerico> lista14 = teSupport.getListaClassificatoriGen14();
			
			codiceDaCercare = response.getImpegno().getCodClassGen14();
			
			for (int i = 0; i < lista14.size(); i++) {
				if (lista14.get(i).getCodice().equals(codiceDaCercare)) {
					model.getMovimento().setClassificazioneFSC(lista14.get(i).getCodice() + " - " + lista14.get(i).getDescrizione());
					break;
				}
			}
			
			oggettoDaPopolare= OggettoDaPopolareEnum.IMPEGNO;
			loadResponse(response, response.getCapitoloUscitaGestione());
			//task-93 qui metto il metodo per ricavare la lista degli impegni che sono collegati al mutuo
			loadResponseMutuiImpegno(response);
			
			model.getLabels().put(LABEL_OGGETTO_GENERICO_PADRE, "Impegno");
			model.getLabels().put(LABEL_OGGETTO_GENERICO_PADRE_LOWER_CASE, "impegno");
			
			//MARZO 2017 CR 396:
			ConsultaDettaglioImpegno reqConsDettaglio = new ConsultaDettaglioImpegno();
			reqConsDettaglio.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
			reqConsDettaglio.setAnnoMovimento(Integer.valueOf(anno));
			reqConsDettaglio.setEnte(sessionHandler.getEnte());
			reqConsDettaglio.setRichiedente(sessionHandler.getRichiedente());
			reqConsDettaglio.setNumeroMovimento(new BigDecimal(numero));
			ConsultaDettaglioImpegnoResponse respCons = movimentoGestioneFinService.consultaDettaglioImpegno(reqConsDettaglio);
			//inizio SIAC-6997
			DettaglioImportiImpegno dettaglioImporti = respCons.getImpegnoDettaglioImporti();
			
			if(dettaglioImporti.getStrutturaCompetente()==null) {
				model.getMovimento().setStrutturaCompetente("");
			}else {
				model.getMovimento().setStrutturaCompetente(dettaglioImporti.getStrutturaCompetente().getCodice() + " - " + dettaglioImporti.getStrutturaCompetente().getDescrizione());
			}
			
//			dettaglioImporti.getStrutturaCompetente().equals(null) ? model.getMovimento().setStrutturaCompetente("") : model.getMovimento().setStrutturaCompetente(dettaglioImporti.getStrutturaCompetente().getCodice() + " - " + dettaglioImporti.getStrutturaCompetente().getDescrizione());
			
			//model.getMovimento().setStrutturaCompetente(dettaglioImporti.getStrutturaCompetente().getCodice() + " - " + dettaglioImporti.getStrutturaCompetente().getDescrizione());
			//fine SIAC-6997
			model.setDettaglioImporti(dettaglioImporti);
			model.setImpegnoConsulta(response.getImpegno());
			
			//task-250
			model.getMovimento().setSoggetto(impostaSoggetto(model.getImpegnoConsulta().getSoggetto(),model.getImpegnoConsulta().getClasseSoggetto()));
			
			
		}
		else if (tipo.equals("A")) {
			RicercaAccertamentoPerChiaveOttimizzatoResponse response = getAccertamento();
			if (response == null){
				// ritorno errore
				return INPUT;
			}	
			oggettoDaPopolare= OggettoDaPopolareEnum.ACCERTAMENTO;
			loadResponse(response.getAccertamento(), response.getCapitoloEntrataGestione());
			//task-93 qui metto il metodo per ricavare la lista degli accertamenti che sono collegati al mutuo
			loadResponseMutuiAccertamento(response.getAccertamento());
			
			model.getLabels().put(LABEL_OGGETTO_GENERICO_PADRE, "Accertamento");
			model.getLabels().put(LABEL_OGGETTO_GENERICO_PADRE_LOWER_CASE, "accertamento");
			
			// SIAC-6247: classificatore Titolo Giuridico non visibile in consulta
			//transazione elementare:
			if(teSupport==null){
				pulisciTransazioneElementare();
			}
			
			// utilizzato per la transazione e le condizioni di obbligatorieta
			teSupport.setOggettoAbilitaTE(OggettoDaPopolareEnum.ACCERTAMENTO.toString());
			caricaListeFin("A");
			
			List<ClassificatoreGenerico> lista16 = teSupport.getListaClassificatoriGen16();
			
			String codiceDaCercare = response.getAccertamento().getCodClassGen16();
			
			for (int i = 0; i < lista16.size(); i++) {
				if (lista16.get(i).getCodice().equals(codiceDaCercare)) {
					model.getMovimento().setTitoloGiuridico(lista16.get(i).getCodice() + " - " + lista16.get(i).getDescrizione());
					break;
				}
			}
			
			
			//APRILE 2017 CR 627:
			ConsultaDettaglioAccertamento reqConsDettaglio = new ConsultaDettaglioAccertamento();
			reqConsDettaglio.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
			reqConsDettaglio.setAnnoMovimento(Integer.valueOf(anno));
			reqConsDettaglio.setEnte(sessionHandler.getEnte());
			reqConsDettaglio.setRichiedente(sessionHandler.getRichiedente());
			reqConsDettaglio.setNumeroMovimento(new BigDecimal(numero));
			ConsultaDettaglioAccertamentoResponse respCons = movimentoGestioneFinService.consultaDettaglioAccertamento(reqConsDettaglio);
			//inizio SIAC-6997
			DettaglioImportiAccertamento dettaglioImporti = respCons.getAcertamentoDettaglioImporti();
//			model.getMovimento().setStrutturaCompetente(dettaglioImporti.getStrutturaCompetente().getCodice() + " - " + dettaglioImporti.getStrutturaCompetente().getDescrizione());
			
			if(dettaglioImporti.getStrutturaCompetente()==null) {
				model.getMovimento().setStrutturaCompetente("");
			}else {
				model.getMovimento().setStrutturaCompetente(dettaglioImporti.getStrutturaCompetente().getCodice() + " - " + dettaglioImporti.getStrutturaCompetente().getDescrizione());			}
			
			model.setDettaglioImportiAcc(dettaglioImporti);
			model.setAccertamentoConsulta(response.getAccertamento());

			//task-250
			model.getMovimento().setSoggetto(impostaSoggetto(model.getAccertamentoConsulta().getSoggetto(), model.getAccertamentoConsulta().getClasseSoggetto()));
			
			
			
			//SETTEMBRE 2017 CR 946 - Consultazione Accertamenti - vincoli
			ConsultaVincoliAccertamento reqConsVincoli = new ConsultaVincoliAccertamento();
			reqConsVincoli.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
			reqConsVincoli.setAnnoMovimento(Integer.valueOf(anno));
			reqConsVincoli.setEnte(sessionHandler.getEnte());
			reqConsVincoli.setRichiedente(sessionHandler.getRichiedente());
			reqConsVincoli.setNumeroMovimento(new BigDecimal(numero));
			ConsultaVincoliAccertamentoResponse respVincoli = movimentoGestioneFinService.consultaVincoliAccertamento(reqConsVincoli);
			
			List<VincoloAccertamento> vincoli = respVincoli.getVincoli();
			loadVincoli(response.getAccertamento(), vincoli);		
			
		}
		
		//ordinamento
		Collections.sort(model.getListaModifiche(), new NumComparator());
		
		return SUCCESS;
	}
	
	//task-250
	private Soggetto impostaSoggetto(it.csi.siac.siacfinser.model.soggetto.Soggetto soggetto, it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto classe) {
		Soggetto s = model.getMovimento().getSoggetto();
		s.setCodice(soggetto.getCodice());
		s.setCodiceFiscale(soggetto.getCodiceFiscale());
		s.setDenominazione(soggetto.getDenominazione());
		s.setPartitaIva(soggetto.getPartitaIva());
		if(classe != null) {
			s.setClasseSoggettoCodice(classe.getCodice());
			s.setClasseSoggettoDescrizione(classe.getDescrizione());
		}else {
			s.setClasseSoggettoCodice(null);
			s.setClasseSoggettoDescrizione(null);
		}
		return s;
	}
	
	// inizio SIAC-6997
	public String consultaModificheStrutturaCompetente() throws Exception{
		//info per debug:
		setMethodName("consultaModificheStrutturaCompetente");
		
		//leggo i dati necessari:
		if (tipo.equals("I")) {
			Impegno impegno = model.getImpegnoConsulta();
			leggiStoricoStrutturaCompetenteByMovimento(impegno);
		}else {
			Accertamento accertamento = model.getAccertamentoConsulta();
			leggiStoricoStrutturaCompetenteByMovimento(accertamento);
		}
		
		return "consultaModificheStrutturaCompetente";
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
	
	// fine SIAC-6997
	
	/**
	 * Load legame movimento storicizzato.
	 *
	 * @return the string
	 */
	//siac-6702
	public String loadLegameMovimentoStoricizzato() {
		Impegno imp = null;
		Accertamento acc = null;
		if (tipo.equals("I")) {
			imp = new Impegno();
			imp.setAnnoMovimento(Integer.valueOf(model.getMovimento().getAnno()));
			imp.setNumeroBigDecimal(new BigDecimal(model.getMovimento().getNumero()));
		}
		if(tipo.equals("A")) {
			acc = new Accertamento();
			acc.setAnnoMovimento(Integer.valueOf(model.getMovimento().getAnno()));
			acc.setNumeroBigDecimal(new BigDecimal(model.getMovimento().getNumero()));
		}
		RicercaStoricoImpegnoAccertamento req = model.creaRequestRicercaStoricoImpegnoAccertamento(sessionHandler.getBilancio(), imp, acc);
		RicercaStoricoImpegnoAccertamentoResponse res = movimentoGestioneFinService.ricercaStoricoImpegnoAccertamento(req);
		if(res.hasErrori()) {
			addErrori(res);
			return "errori";
		}
		
		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_STORICO_IMPEGNI_ACCERTAMENTI, res.getElencoStoricoImpegnoAccertamento()!= null? res.getElencoStoricoImpegnoAccertamento() : new ArrayList<StoricoImpegnoAccertamento>() );
		model.setResultSize(res.getNumRisultati());
		
		return "tabellaStorico";
	}
	
	public boolean statoImpegnoDefinitivo(){
		boolean statoDef = false;
		if(model.getMovimento()!=null && model.getMovimento().getStatoOperativo().equalsIgnoreCase(CostantiFin.STATO_DEFINITIVO)){
			statoDef = true;
		}
		return statoDef;
	}
	/**
	 * Carica il dettaglio del subimpegno / subaccertamento dalla lista
	 * @return
	 */
	public String dettaglioSubPopup() {
		setMethodName("dettaglioSubPopup");
		debug(methodName, "UID sub:"+getUidPerDettaglioSub());

		model.setSubDettaglio(model.getListaSub().get(Integer.valueOf(getUidPerDettaglioSub())));
		
		
		String numeroSub = model.getSubDettaglio().getNumero();
		
		
		if(model.getMovimento().getTipoMovimento() == MovimentoConsulta.IMPEGNO){
			
			ConsultaDettaglioImpegno reqCons = new ConsultaDettaglioImpegno();
			reqCons.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
			reqCons.setAnnoMovimento(Integer.valueOf(model.getMovimento().getAnno()));
			reqCons.setEnte(sessionHandler.getEnte());
			reqCons.setNumeroMovimento(new BigDecimal(model.getMovimento().getNumero()));
			reqCons.setNumeroSub(new BigDecimal(numeroSub));
			reqCons.setRichiedente(sessionHandler.getRichiedente());
			ConsultaDettaglioImpegnoResponse resp = movimentoGestioneFinService.consultaDettaglioImpegno(reqCons);
			
			if(resp!=null){
				model.setDettaglioImportiSubSelezionato(resp.getImpegnoDettaglioImporti());
			}
			
		} else {
			
			ConsultaDettaglioAccertamento reqCons = new ConsultaDettaglioAccertamento();
			reqCons.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
			reqCons.setAnnoMovimento(Integer.valueOf(model.getMovimento().getAnno()));
			reqCons.setEnte(sessionHandler.getEnte());
			reqCons.setNumeroMovimento(new BigDecimal(model.getMovimento().getNumero()));
			reqCons.setNumeroSub(new BigDecimal(numeroSub));
			reqCons.setRichiedente(sessionHandler.getRichiedente());
			ConsultaDettaglioAccertamentoResponse resp = movimentoGestioneFinService.consultaDettaglioAccertamento(reqCons);
			
			if(resp!=null){
				model.setDettaglioImportiSubAccSelezionato(resp.getAcertamentoDettaglioImporti());
			}
			
		}
		
		
		
		return "dettaglioSubPopup";
	}

	/**
	 * Carica il dettaglio della modifica dalla lista
	 * @return
	 */
	public String dettaglioModPopup() {
		setMethodName("dettaglioModPopup");
		debug(methodName, "UID mod: "+getUidPerDettaglioMod());
		
		model.setModificaDettaglio(model.getListaModifiche().get(Integer.valueOf(getUidPerDettaglioMod()) - 1));
		
		return "dettaglioModPopup";
	}

	/* **************************************************************************** */
	/*  Private methods	per IMPEGNO													*/
	/* **************************************************************************** */
	
	/**
	 * Ricerca l'Impegno per chiave e gestisce gli errori o il fallimento della chiamata al servizio
	 * @return
	 */
	private RicercaImpegnoPerChiaveOttimizzatoResponse getImpegno () {
		RicercaImpegnoPerChiaveOttimizzato request = new RicercaImpegnoPerChiaveOttimizzato();
		RicercaImpegnoK k = new RicercaImpegnoK();
			k.setAnnoImpegno(Integer.valueOf(anno));
			k.setNumeroImpegno(new BigDecimal(numero));
			k.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
		request.setpRicercaImpegnoK(k);
		request.setEnte(sessionHandler.getEnte());
		request.setRichiedente(sessionHandler.getRichiedente());
		
		//MARZO 2016: nuovi setting per chiamata ottimizzata:
		request.setCaricaSub(false);
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaElencoModificheMovGest(true);
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaCig(true);
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaCup(true);
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaDisponibileLiquidareEDisponibilitaInModifica(true);
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaDisponibilePagare(true);
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaDisponibileFinanziare(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds );
		//
		
		RicercaImpegnoPerChiaveOttimizzatoResponse response =  movimentoGestioneFinService.ricercaImpegnoPerChiaveOttimizzato(request);
		
		if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
			debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return null;
		}
		return response;
	}

	/**
	 * Carica in un oggetto MovimentoConsulta i dati dell'Impegno
	 * @param mc
	 * @param impegno
	 * @param capitolo
	 * @return
	 */
	private MovimentoConsulta mapMovimento (MovimentoConsulta mc, Impegno impegno, CapitoloUscitaGestione capitolo) {
		mc.setTipoMovimento(MovimentoConsulta.IMPEGNO);
		// movimento
		//
		mc.setIdMovimento(impegno.getUid());
		mc.setAnno(String.valueOf(impegno.getAnnoMovimento()));
		mc.setNumero(String.valueOf(impegno.getNumeroBigDecimal()));
		mc.setDescrizione(impegno.getDescrizione());
	    mc.setImporto(impegno.getImportoAttuale());
	    mc.setImportoIniziale(impegno.getImportoIniziale());
	    if (impegno.getUtenteCreazione() != null) 	mc.setUtenteCreazione(impegno.getUtenteCreazione());
	    if (impegno.getUtenteModifica() != null) 	mc.setUtenteModifica(impegno.getUtenteModifica());
	    mc.setDataInserimento(impegno.getDataEmissioneSupport());
	    mc.setDataModifica(impegno.getDataModifica());
	    mc.setStatoOperativo(impegno.getDescrizioneStatoOperativoMovimentoGestioneSpesa());
	    mc.setDataStatoOperativo(impegno.getDataStatoOperativoMovimentoGestioneSpesa());
	    if (impegno.getTipoImpegno() != null){
	    	mc.setTipo(impegno.getTipoImpegno().getDescrizione());
	    	mc.setTipoCode(impegno.getTipoImpegno().getCodice());
	    }
	    if (impegno.getDataScadenza() != null){
	    	mc.setDataScadenza(impegno.getDataScadenza());
	    }
	    mc.setCig(impegno.getCig());
        mc.setCup(impegno.getCup());
        if (impegno.getProgetto() != null){
        	mc.setProgetto(impegno.getProgetto().getCodice());
        }
        if(impegno.getAnnoScritturaEconomicoPatrimoniale()!=null){
        	mc.setAnnoScritturaEconomicoPatrimoniale(impegno.getAnnoScritturaEconomicoPatrimoniale().toString());
        }
        
        //SIAC-7349
        if(impegno.getComponenteBilancioImpegno() != null)
        	mc.setDescrizioneComponente(impegno.getComponenteBilancioImpegno().getDescrizioneTipoComponente());
        
        //SIAC-6929
        if(!ObjectUtils.equals(impegno.getAttoAmministrativo(), null) 
        		&& impegno.getAttoAmministrativo().getBloccoRagioneria()!= null) {
        
            	if(impegno.getAttoAmministrativo().getBloccoRagioneria().booleanValue()){
            		mc.setBloccoRagioneria(ConsultaMovimentoAction.BLOCCO_RAGIONERIA_SI);	
            	}else {
            		mc.setBloccoRagioneria(ConsultaMovimentoAction.BLOCCO_RAGIONERIA_NO);
            	}
            }else {
            	mc.setBloccoRagioneria(ConsultaMovimentoAction.BLOCCO_RAGIONERIA_NA);
        }
        
        
        // parere finanziario
        if(impegno.getParereFinanziario()){
        	mc.setParereFinanziario("SI");
        }else mc.setParereFinanziario("NO");
        
        mc.setParereFinanziarioDataModifica(impegno.getParereFinanziarioDataModifica());
        mc.setParereFinanziarioLoginOperazione(impegno.getParereFinanziarioLoginOperazione());
        
        //TRANSAZIONE ELEMENTARE:
        // SIAC-6158
//        mc.setCodificaTransazioneElementare(FinStringUtils.smartConcatMult(
//        		" - ",
//        		impegno.getCodMissione(),
//        		impegno.getCodProgramma(), 
//        		impegno.getCodPdc(),
//        		impegno.getCodCofog(),
//        		impegno.getCodSiope()));
        //SIAC-8840
        mc.setCodificaTransazioneElementare(componiTransazioneElementareImpegno(impegno,capitolo));
        
	    // disponibilita
	    mc.setDisponibilitaSub(impegno.getDisponibilitaSubimpegnare());
	    mc.setTotaleSub(impegno.getTotaleSubImpegniBigDecimal());
	    mc.setDisponibilitaLiquidare(impegno.getDisponibilitaLiquidare());
	    
	    // 	SIAC-5558 il servizio e' corretto che possa darmi un valaore diverso da zero per 
	    //  definitivi non liquidabili, ma sulla maschera di consulta lo setto a zero:
	    if(CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equals(impegno.getStatoOperativoMovimentoGestioneSpesa())){
	    	 mc.setDisponibilitaPagare(new BigDecimal(0.0));
	    } else {
	    	 mc.setDisponibilitaPagare(impegno.getDisponibilitaPagare());
	    }
	    //
	    
	    mc.setDisponibilitaFinanziare(impegno.getDisponibilitaFinanziare());
	    mc.setDisponibilitaVincolare(impegno.getDisponibilitaVincolare());
	    
	    // provvedimento
	    if (impegno.getAttoAmministrativo() != null) {
	    	mc.getProvvedimento().setAnno(String.valueOf(impegno.getAttoAmministrativo().getAnno()));
	    	mc.getProvvedimento().setNumero(String.valueOf(impegno.getAttoAmministrativo().getNumero()));
	    	mc.getProvvedimento().setOggetto(impegno.getAttoAmministrativo().getOggetto());
	    	if (impegno.getAttoAmministrativo().getTipoAtto() != null) {
	    		//RM jira 1980: visualizzare il codice non la descrizione
	    		mc.getProvvedimento().setTipo(impegno.getAttoAmministrativo().getTipoAtto().getCodice());
	    	}
	    	if (impegno.getAttoAmministrativo().getStrutturaAmmContabile() != null) {
	    		mc.getProvvedimento().setStruttura(impegno.getAttoAmministrativo().getStrutturaAmmContabile().getCodice());
	    		// SIAC-5220: si vuole visualizzare codice + descrizione della SAC
	    		mc.getProvvedimento().setStrutturaCompleta(impegno.getAttoAmministrativo().getStrutturaAmmContabile().getCodice() + " - " + impegno.getAttoAmministrativo().getStrutturaAmmContabile().getDescrizione());
	    	} else {
	    		// SIAC-5220: pulisco i dati dalla SAC
	    		mc.getProvvedimento().setStruttura(null);
	    		mc.getProvvedimento().setStrutturaCompleta(null);
	    	}
	    	mc.getProvvedimento().setStato(impegno.getAttoAmministrativo().getStatoOperativo());
	    	
	    	//SIAC_6929
	    	if(impegno.getAttoAmministrativo().getBloccoRagioneria() == null){
	        	mc.getProvvedimento().setBloccoRagioneria("N/A");
	        }else {
	        	if(impegno.getAttoAmministrativo().getBloccoRagioneria().booleanValue()){
	        		mc.getProvvedimento().setBloccoRagioneria("SI");	
	        	}else {
	        		mc.getProvvedimento().setBloccoRagioneria("NO");
	        	}
	        }
	    	
	    }
	    
	    // soggetto
	    if ((impegno.getSoggetto() != null) && (impegno.getSoggetto().getUid() != 0)) {
		    mc.getSoggetto().setCodice(impegno.getSoggetto().getCodiceSoggetto());
		    mc.getSoggetto().setDenominazione(impegno.getSoggetto().getDenominazione());
		    mc.getSoggetto().setCodiceFiscale(impegno.getSoggetto().getCodiceFiscale());
		    mc.getSoggetto().setPartitaIva(impegno.getSoggetto().getPartitaIva());
	    } else if (impegno.getClasseSoggetto() != null) {
	    	mc.getSoggetto().setClasseSoggettoCodice(impegno.getClasseSoggetto().getCodice());
	    	mc.getSoggetto().setClasseSoggettoDescrizione(impegno.getClasseSoggetto().getDescrizione());
	    }
        
	    //Frazionabile:
    	mc.setFrazionabileValorizzato(impegno.isFrazionabileValorizzato());
    	mc.setFrazionabile(impegno.isFlagFrazionabile());
    	if(impegno.isFlagFrazionabile()){
    		mc.setFrazionabileString(WebAppConstants.FRAZIONABILE);
    	} else {
    		mc.setFrazionabileString(WebAppConstants.NON_FRAZIONABILE);
    	}
    	
        // riaccertamento
        if (impegno.isFlagDaRiaccertamento()){ 
        	mc.setDaRiaccertamento(WebAppConstants.MSG_SI + " " + impegno.getAnnoRiaccertato() + " / " + impegno.getNumeroRiaccertato());
        }else{
        	mc.setDaRiaccertamento(WebAppConstants.MSG_NO);
        }	
        
        // inizio SIAC-6997
        if (impegno.isFlagDaReanno()){ 
        	mc.setDaReanno(WebAppConstants.MSG_SI + " " + impegno.getAnnoRiaccertato() + " / " + impegno.getNumeroRiaccertato());
        }else{
        	mc.setDaReanno(WebAppConstants.MSG_NO);
        }
        
        // fine SIAC-6997
                
        if (impegno.isFlagSoggettoDurc()){ 
        	mc.setSoggettoDurc(WebAppConstants.MSG_SI);
        }else{
        	mc.setSoggettoDurc(WebAppConstants.MSG_NO);
        }	
                
        //PRENOTATO
        if (impegno.isFlagPrenotazione()){
        	if(impegno.isFlagPrenotazioneLiquidabile()){
        		mc.setPrenotatoString(WebAppConstants.PRENOTATO_LIQUIDABILE);
        	} else {
        		mc.setPrenotatoString(WebAppConstants.PRENOTATO);
        	}
        }else{
        	mc.setPrenotatoString(WebAppConstants.NON_PRENOTATO);
        }	
        mc.setPrenotato(impegno.isFlagPrenotazione());
        
        //flag attiva gsa:
        mc.setFlagAttivaGsa(booleanToWebAppSINO(impegno.isFlagAttivaGsa()));
        //
        
        //flag impegno sdf:
        mc.setImpegnoSDF(impegno.isFlagSDF());
        //
        
        //SIOPE PLUS:
        impostaDatiSiopePlusNelModelDiConsultazione(impegno, mc);
		// end siope plus
		
        // capitolo (anno capitolo, / numero capito / ueb descrizione  - codiceStruttuAmm - tipoFinanz)
        if (capitolo != null){
        	mc.getCapitolo().setAnno(capitolo.getAnnoCapitolo());
        	mc.getCapitolo().setNumCapitolo(capitolo.getNumeroCapitolo());
        	//controllare UEB
        	mc.getCapitolo().setUeb(BigInteger.valueOf(capitolo.getNumeroUEB()));
        	mc.getCapitolo().setDescrizione(capitolo.getDescrizione());
        	mc.getCapitolo().setArticolo(capitolo.getNumeroArticolo());

        	if (capitolo.getStrutturaAmministrativoContabile() != null)	{
        		mc.getCapitolo().setCodiceStrutturaAmministrativa((capitolo.getStrutturaAmministrativoContabile().getCodice()));
        	}	
        	if (capitolo.getTipoFinanziamento() != null){
        		mc.getCapitolo().setTipoFinanziamento(capitolo.getTipoFinanziamento().getDescrizione());
        	}	
        	
        	// Tipo finanziamento
			if(capitolo.getTipoFinanziamento()!=null){
				mc.getCapitolo().setTipoFinanziamento(capitolo.getTipoFinanziamento().getDescrizione());
			}	
			
			if (capitolo.getTitoloSpesa() != null) {
				mc.getCapitolo().setClassificazione(capitolo.getTitoloSpesa().getCodice() + " ");
			}
			if (capitolo.getMacroaggregato() != null) {
				mc.getCapitolo().setIdMacroAggregato(capitolo.getMacroaggregato().getUid());
				mc.getCapitolo().setClassificazione(mc.getCapitolo().getClassificazione() + capitolo.getMacroaggregato().getCodice());
			}
			if (capitolo.getStrutturaAmministrativoContabile() != null) {
				mc.getCapitolo().setCodiceStrutturaAmministrativa(capitolo.getStrutturaAmministrativoContabile().getCodice());
				mc.getCapitolo().setDescrizioneStrutturaAmministrativa(capitolo.getStrutturaAmministrativoContabile().getDescrizione());
				mc.getCapitolo().setUidStruttura(capitolo.getStrutturaAmministrativoContabile().getUid());
			}
			if (capitolo.getElementoPianoDeiConti() != null) {
				mc.getCapitolo().setCodicePdcFinanziario(capitolo.getElementoPianoDeiConti().getCodice());
				mc.getCapitolo().setDescrizionePdcFinanziario(capitolo.getElementoPianoDeiConti().getDescrizione());
				mc.getCapitolo().setIdPianoDeiConti(capitolo.getElementoPianoDeiConti().getUid());
			}
			if (capitolo.getListaImportiCapitolo() != null && capitolo.getListaImportiCapitolo().size() > 0) {
				mc.getCapitolo().setImportiCapitoloUG(capitolo.getListaImportiCapitolo());
				List<ImportiCapitoloModel> importiCapitoloModel = new ArrayList<ImportiCapitoloModel>();
				for (ImportiCapitoloUG currentImporto : capitolo.getListaImportiCapitolo()) {
					ImportiCapitoloModel supportImporti = new ImportiCapitoloModel();
					supportImporti.setAnnoCompetenza(currentImporto.getAnnoCompetenza());
					supportImporti.setCassa(currentImporto.getStanziamentoCassa());
					supportImporti.setResiduo(currentImporto.getStanziamentoResiduo());
					supportImporti.setCompetenza(currentImporto.getStanziamento());
					
					// con i nuovi servizi di BIL le disponibilita
					// degli anni successivi si trattano in altra maniera
					
					if (currentImporto.getAnnoCompetenza().intValue() == mc.getCapitolo().getAnno().intValue()) {
						mc.getCapitolo().setDisponibileAnno1(capitolo.getListaImportiCapitoloUG().get(0).getDisponibilitaImpegnareAnno1());
					} else if (currentImporto.getAnnoCompetenza().intValue() == (mc.getCapitolo().getAnno().intValue() + 1)) {
						mc.getCapitolo().setDisponibileAnno2(capitolo.getListaImportiCapitoloUG().get(0).getDisponibilitaImpegnareAnno2());
					} else {
						mc.getCapitolo().setDisponibileAnno3(capitolo.getListaImportiCapitoloUG().get(0).getDisponibilitaImpegnareAnno3());
					}
					importiCapitoloModel.add(supportImporti);
				}
				mc.getCapitolo().setImportiCapitolo(importiCapitoloModel);
			}
        	
        }
        
        // SIAC-6695
		mc.setMotivazioneDisponibilitaLiquidare(impegno.getMotivazioneDisponibilitaLiquidare());
		mc.setMotivazioneDisponibilitaPagare(impegno.getMotivazioneDisponibilitaPagare());
		mc.setMotivazioneDisponibilitaFinanziare(impegno.getMotivazioneDisponibilitaFinanziare());
		mc.setMotivazioneDisponibilitaSubImpegnare(impegno.getMotivazioneDisponibilitaSubImpegnare());
		mc.setMotivazioneDisponibilitaImpegnoModifica(impegno.getMotivazioneDisponibilitaImpegnoModifica());
		mc.setMotivazioneDisponibilitaVincolare(impegno.getMotivazioneDisponibilitaVincolare());
		
		//SIAC-6865
		if(StringUtils.isNotEmpty(impegno.getAnnoPrenotazioneOrigine())) {
			mc.setAggiudicazioneDaPrenotazione(true);
			mc.setImpegnoPrenotazioneOrigine(impegno.getImpegnoPrenotazioneOrigine());
			mc.setProvvedimentoAggiudicazione(impegno.getAttoAmministrativoAggiudicazione());
		}
        
        return mc;
	}  
	
	/**
	 * Nella consultazione degli impegni definire meglio le informazioni della transazione elementare dividendola 
	 * su cinque righe e aggiungendo le label come nell'esempio per la spesa:
	 * <pre>
	 * 		Transazione Elementare Missione: 14 Programma: 1401 Cofog: 04,4
	 * 							   P.d.C. finanziario: U.2.04.23.01.001 Siope: YYYY
	 * 							   Capitoli perimetro sanitario: 2 Ricorrente: 3
	 * 							   Codifica transazione europea: 1 Cup: A00TLOU2JKJHKUH
	 * 							   Programma pol. reg. unitaria: 4
	 * </pre>
	 * o su tre righe per l'entrata aggiungendo le label come nell'esempio:
	 * <pre>
	 * 		Transazione Elementare P.d.C. finanziario: E.3.03.02.01.001 Siope: YYYY
	 * 							   Capitoli perimetro sanitario: 1 Ricorrente: 2
	 * 							   Codifica transazione europea: 1 
	 * </pre>
	 * @param impegno l'impegno
	 * @return la transazione
	 */
    //SIAC-8629 Generics e perimetro sanitario
	private <MOV extends MovimentoGestione> String componiTransazioneElementare(MOV movimento) {
		List<String> lines = new ArrayList<String>();
		List<String> firstLine = new ArrayList<String>();
		// Prima riga: missione, programma, cofog
		if(StringUtils.isNotBlank(movimento.getCodMissione())) {
			firstLine.add("<strong>Missione:</strong> " + movimento.getCodMissione());
		}
		if(StringUtils.isNotBlank(movimento.getCodProgramma())) {
			firstLine.add("<strong>Programma:</strong> " + movimento.getCodProgramma());
		}
		if(StringUtils.isNotBlank(movimento.getCodCofog())) {
			firstLine.add("<strong>Cofog:</strong> " + movimento.getCodCofog());
		}
		aggiungiRigaTransazioneElemenatale(lines, firstLine);
		
		// Seconda riga: pdc, siope
		List<String> secondLine = new ArrayList<String>();
		if(StringUtils.isNotBlank(movimento.getCodPdc())) {
			secondLine.add("<strong>P.d.C. finanziario:</strong> " + movimento.getCodPdc());
		}
		if(StringUtils.isNotBlank(movimento.getCodSiope())) {
			secondLine.add("<strong>Siope:</strong> " + movimento.getCodSiope());
		}
		aggiungiRigaTransazioneElemenatale(lines, secondLine);

		// Terza riga: perimetro sanitario, ricorrente
		List<String> thirdLine = new ArrayList<String>();
		if(StringUtils.isNotBlank(movimento.getCodCapitoloSanitarioSpesa())) {
			thirdLine.add("<strong>Capitoli perimetro sanitario:</strong> " + movimento.getCodCapitoloSanitarioSpesa());
		}
		if(StringUtils.isNotBlank(movimento.getCodRicorrenteSpesa())) {
			thirdLine.add("<strong>Ricorrente:</strong> " + movimento.getCodRicorrenteSpesa());
		}
		aggiungiRigaTransazioneElemenatale(lines, thirdLine);

		// Quarta riga: transazione europea, cup
		List<String> fourthLine = new ArrayList<String>();
		if(StringUtils.isNotBlank(movimento.getCodTransazioneEuropeaSpesa())) {
			fourthLine.add("<strong>Codifica transazione europea:</strong> " + movimento.getCodTransazioneEuropeaSpesa());
		}
		if(StringUtils.isNotBlank(movimento.getCup())) {
			fourthLine.add("<strong>Cup:</strong> " + movimento.getCup());
		}
		aggiungiRigaTransazioneElemenatale(lines, fourthLine);

		// Quinta riga: programma politica regionale
		List<String> fifthLine = new ArrayList<String>();
		if(StringUtils.isNotBlank(movimento.getCodPrgPolReg())) {
			fifthLine.add("<strong>Programma pol. reg. unitaria:</strong> " + movimento.getCodPrgPolReg());
		}
		aggiungiRigaTransazioneElemenatale(lines, fifthLine);
		
		return StringUtils.join(lines, "<br/>");
	} 
	//SIAC-8840
	private <MOV extends MovimentoGestione> String componiTransazioneElementareImpegno(MOV movimento, CapitoloUscitaGestione capitolo) {
		List<String> lines = new ArrayList<String>();
		List<String> firstLine = new ArrayList<String>();
		// Prima riga: missione, programma, cofog
		if(capitolo!=null) {
			if(StringUtils.isNotBlank(movimento.getCodMissione())) {
				firstLine.add("<strong>Missione:</strong> " + capitolo.getMissione().getCodice());
			}
			if(StringUtils.isNotBlank(movimento.getCodProgramma())) {
				firstLine.add("<strong>Programma:</strong> " + capitolo.getProgramma().getCodice());
			}
			if(StringUtils.isNotBlank(movimento.getCodCofog())) {
				//SIAC-8875
				firstLine.add("<strong>Cofog:</strong> " + movimento.getCodCofog());
			}
		}else {
			//SIAC-8858
			if(StringUtils.isNotBlank(movimento.getCodMissione())) {
				firstLine.add("<strong>Missione:</strong> " + movimento.getCodMissione());
			}
			if(StringUtils.isNotBlank(movimento.getCodProgramma())) {
				firstLine.add("<strong>Programma:</strong> " + movimento.getCodProgramma());
			}
			if(StringUtils.isNotBlank(movimento.getCodCofog())) {
				firstLine.add("<strong>Cofog:</strong> " + movimento.getCodCofog());
			}
		}
		aggiungiRigaTransazioneElemenatale(lines, firstLine);
		
		// Seconda riga: pdc, siope
		List<String> secondLine = new ArrayList<String>();
		if(StringUtils.isNotBlank(movimento.getCodPdc())) {
			secondLine.add("<strong>P.d.C. finanziario:</strong> " + movimento.getCodPdc());
		}
		if(StringUtils.isNotBlank(movimento.getCodSiope())) {
			secondLine.add("<strong>Siope:</strong> " + movimento.getCodSiope());
		}
		aggiungiRigaTransazioneElemenatale(lines, secondLine);

		// Terza riga: perimetro sanitario, ricorrente
		List<String> thirdLine = new ArrayList<String>();
		if(StringUtils.isNotBlank(movimento.getCodCapitoloSanitarioSpesa())) {
			thirdLine.add("<strong>Capitoli perimetro sanitario:</strong> " + movimento.getCodCapitoloSanitarioSpesa());
		}
		if(StringUtils.isNotBlank(movimento.getCodRicorrenteSpesa())) {
			thirdLine.add("<strong>Ricorrente:</strong> " + movimento.getCodRicorrenteSpesa());
		}
		aggiungiRigaTransazioneElemenatale(lines, thirdLine);

		// Quarta riga: transazione europea, cup
		List<String> fourthLine = new ArrayList<String>();
		if(StringUtils.isNotBlank(movimento.getCodTransazioneEuropeaSpesa())) {
			fourthLine.add("<strong>Codifica transazione europea:</strong> " + movimento.getCodTransazioneEuropeaSpesa());
		}
		if(StringUtils.isNotBlank(movimento.getCup())) {
			fourthLine.add("<strong>Cup:</strong> " + movimento.getCup());
		}
		aggiungiRigaTransazioneElemenatale(lines, fourthLine);

		// Quinta riga: programma politica regionale
		List<String> fifthLine = new ArrayList<String>();
		if(StringUtils.isNotBlank(movimento.getCodPrgPolReg())) {
			fifthLine.add("<strong>Programma pol. reg. unitaria:</strong> " + movimento.getCodPrgPolReg());
		}
		aggiungiRigaTransazioneElemenatale(lines, fifthLine);
		
		return StringUtils.join(lines, "<br/>");
	} 
	
	//SIAC-8629
	private void aggiungiRigaTransazioneElemenatale(List<String> lines, List<String> linesToAdd) {
		if(CollectionUtils.isNotEmpty(linesToAdd)) {
			lines.add(StringUtils.join(linesToAdd, ' '));
		}
	}
	
	/**
	 * Carica in un oggetto ModificaConsulta i dati della modifica movimento di spesa
	 * @param mc
	 * @param modifica
	 * @return
	 */
	private ModificaConsulta mapModifica (ModificaConsulta mc, ModificaMovimentoGestioneSpesa modifica) {
		mc.setTipoMovimento(MovimentoConsulta.IMPEGNO);
		// modifica
		mc.setNumero(String.valueOf(modifica.getNumeroModificaMovimentoGestione()));
		mc.setDescrizione(modifica.getDescrizioneModificaMovimentoGestione());
	    mc.setImporto(modifica.getImportoOld());
	    //SIAC-8834
	    mc.setImpegnoAssociato(modifica.getImpegno());
	    mc.setMotivo(modifica.getTipoMovimentoDesc());
	    mc.setUtenteCreazione(modifica.getUtenteCreazione());
	    mc.setUtenteModifica(modifica.getUtenteModifica());
	    mc.setDataInserimento(modifica.getDataEmissione());
	    mc.setDataModifica(modifica.getDataModifica());
	    mc.setStatoOperativo(modifica.getStatoOperativoModificaMovimentoGestione().name());
	    //SIAC-8183
	    mc.setDataStatoOperativo(modifica.getDataFromStatoOperativo());
	    
	    //Reimputazione:
	    mc = settaDatiReimputazioneInModificaConsulta(modifica, mc);
	    
	    // provvedimento
	    if (modifica.getAttoAmministrativo() != null) {
	    	mc.getProvvedimento().setAnno(String.valueOf(modifica.getAttoAmministrativo().getAnno()));
	    	mc.getProvvedimento().setNumero(String.valueOf(modifica.getAttoAmministrativo().getNumero()));
	    	mc.getProvvedimento().setOggetto(modifica.getAttoAmministrativo().getOggetto());
	    	if (modifica.getAttoAmministrativo().getTipoAtto() != null) {
	    		mc.getProvvedimento().setTipo(modifica.getAttoAmministrativo().getTipoAtto().getDescrizione());
	    	}
	    	if (modifica.getAttoAmministrativo().getStrutturaAmmContabile() != null) {
	    		mc.getProvvedimento().setStruttura(modifica.getAttoAmministrativo().getStrutturaAmmContabile().getCodice());
	    	}
	    	mc.getProvvedimento().setStato(modifica.getAttoAmministrativo().getStatoOperativo());
	    	
	    	//SIAC-6929 
				if(modifica.getAttoAmministrativo().getBloccoRagioneria() == null){
		        	mc.setBloccoRagioneria(ConsultaMovimentoAction.BLOCCO_RAGIONERIA_NA);
		        }else {
		        	if(modifica.getAttoAmministrativo().getBloccoRagioneria().booleanValue()){
		        		mc.setBloccoRagioneria(ConsultaMovimentoAction.BLOCCO_RAGIONERIA_SI);	
		        	}else {
		        		mc.setBloccoRagioneria(ConsultaMovimentoAction.BLOCCO_RAGIONERIA_NO);
		        	}
			}
	    	
	    }
	    
	    // soggetti per variazione soggetto
	    
	    //PRECEDENTE:
	    if ((modifica.getSoggettoOldMovimentoGestione() != null) && (modifica.getSoggettoOldMovimentoGestione().getUid() != 0)) {
		    mc.getSoggettoPrec().setCodice(modifica.getSoggettoOldMovimentoGestione().getCodiceSoggetto());
		    mc.getSoggettoPrec().setDenominazione(modifica.getSoggettoOldMovimentoGestione().getDenominazione());
		    mc.getSoggettoPrec().setCodiceFiscale(modifica.getSoggettoOldMovimentoGestione().getCodiceFiscale());
		    mc.getSoggettoAttuale().setPartitaIva(modifica.getSoggettoOldMovimentoGestione().getPartitaIva());
	    } else if (modifica.getClasseSoggettoOldMovimentoGestione() != null) {
	    	mc.getSoggettoPrec().setClasseSoggettoCodice(modifica.getClasseSoggettoOldMovimentoGestione().getCodice());
	    	mc.getSoggettoPrec().setClasseSoggettoDescrizione(modifica.getClasseSoggettoOldMovimentoGestione().getDescrizione());
	    }
	    
	    //ATTUALE:
	    if ((modifica.getSoggettoNewMovimentoGestione() != null) && (modifica.getSoggettoNewMovimentoGestione().getUid() != 0)) {
		    mc.getSoggettoAttuale().setCodice(modifica.getSoggettoNewMovimentoGestione().getCodiceSoggetto());
		    mc.getSoggettoAttuale().setDenominazione(modifica.getSoggettoNewMovimentoGestione().getDenominazione());
		    mc.getSoggettoAttuale().setCodiceFiscale(modifica.getSoggettoNewMovimentoGestione().getCodiceFiscale());
		    mc.getSoggettoAttuale().setPartitaIva(modifica.getSoggettoNewMovimentoGestione().getPartitaIva());
	    } else if (modifica.getClasseSoggettoNewMovimentoGestione() != null && modifica.getClasseSoggettoNewMovimentoGestione().getUid() != 0) {
	    	mc.getSoggettoAttuale().setClasseSoggettoCodice(modifica.getClasseSoggettoNewMovimentoGestione().getCodice());
	    	mc.getSoggettoAttuale().setClasseSoggettoDescrizione(modifica.getClasseSoggettoNewMovimentoGestione().getDescrizione());
	    }	   
	    
	    //SIAC-7349 Inizio  SR180 CM 22/04/2020 Aggiunto per visualizzazione lista modifiche movimenti di spesa in consulta impegno
	    mc.setListaModificheMovimentoGestioneSpesaCollegata(modifica.getListaModificheMovimentoGestioneSpesaCollegata());
	    //SIAC-7349 Fine  SR180 CM 22/04/2020
	    
	    return mc;
	}

	/**
	 * Carica i dati provenienti dal servizio nel modello dati della pagina per un Impegno
	 * @param impegno
	 * @param capitolo
	 */
	private void loadResponse(RicercaImpegnoPerChiaveOttimizzatoResponse response, CapitoloUscitaGestione capitolo){
		
		Impegno impegno = response.getImpegno();
		
		mapMovimento(model.getMovimento(), impegno, capitolo);

		String descImpegno = model.getMovimento().getAnno() + "/" + model.getMovimento().getNumero() + 
				" - " + StringUtils.defaultString(model.getMovimento().getDescrizione()) +
				" - " + convertiBigDecimalToImporto(model.getMovimento().getImporto()) + 
				" (" +  model.getMovimento().getStatoOperativo() + " dal " + convertDateToString(model.getMovimento().getDataStatoOperativo()) + ")";	
		
		
		loadResponseSubimp(descImpegno,response);
		loadResponseModimp(descImpegno,response);

		loadVincoli(impegno, descImpegno);
		//SIAC-8650
		loadVincoliOriginari(impegno);
	}
	
	//task-93
	private void loadResponseMutuiImpegno(RicercaImpegnoPerChiaveOttimizzatoResponse response){
		
		Impegno impegno = response.getImpegno();
		RicercaDettaglioImpegno request = model.creaRequestRicercaDettaglioImpegnoMutuo();
		request.setImpegno(impegno);
		
		//la res di ricercaDettaglioImpegno è valorizzata corretta
		RicercaDettaglioImpegnoResponse res = movimentoGestioneBilService.ricercaDettaglioImpegno(request);
		//la res non ha gli impegni che res ha valorizzato nel service
		
		if(!res.hasErrori() && res.getImpegno() != null) {
			model.setElencoMutuiAssociati(CollectionUtil.getSortedList(res.getImpegno().getElencoMutuiAssociati(), MutuoAssociatoMovimentoGestioneNumeroMutuoComparator));
		}
	}
	
	
	//SIAC-8650
	private void loadVincoliOriginari(Impegno impegno) {
		RicercaSinteticaModulareVincoliImpegno req = model
				.creaRequestRicercaSinteticaModulareVincoliOriginariImpegno(sessionHandler.getAnnoBilancio());
		
		RicercaSinteticaModulareVincoliImpegnoResponse res = 
				movimentoGestioneFinService.ricercaSinteticaModulareVincoliImpegno(req);
		
		if(res.hasErrori()) {
			model.addErrori(res.getErrori());
			return;
		}
		
		model.setListaVincoliImpegnoOriginari(res.getVincoliImpegno());
		model.setPrimoImpCatena(res.getImpegno());
	}

	//SIAC-8650
	private void loadVincoliOriginari(Accertamento accertamento) {
		RicercaSinteticaModulareVincoliAccertamento req = model
				.creaRequestRicercaSinteticaModulareVincoliOriginariAccertamento(sessionHandler.getAnnoBilancio());
		
		RicercaSinteticaModulareVincoliAccertamentoResponse res = 
				movimentoGestioneFinService.ricercaSinteticaModulareVincoliAccertamento(req);
		
		if(res.hasErrori()) {
			model.addErrori(res.getErrori());
			return;
		}
		
		model.setListaVincoliAccertamentoOriginari(res.getVincoliAccertamento());
		model.setPrimoAccCatena(res.getAccertamento());
	}
	
	/**
	 * Carica nel model una lista di subimpegni
	 * @param subResponse
	 */
	private void loadResponseSubimp(String descImpegno,RicercaImpegnoPerChiaveOttimizzatoResponse response) {
		ArrayList<MovimentoConsulta> subImp = new ArrayList<MovimentoConsulta>();
		
		List<SubImpegno> elencoSub = response.getElencoSubImpegniTuttiConSoloGliIds();
		
		BigDecimal totSub = new BigDecimal(0);
		
		if (elencoSub != null) {
			for (SubImpegno subimp : elencoSub) {
				MovimentoConsulta mc = new MovimentoConsulta();
				subImp.add(mapMovimento(mc, subimp, null));
				mc.setDescSuper(descImpegno);
				//SIAC-7046
				if(!"A".equalsIgnoreCase(subimp.getStatoOperativoMovimentoGestioneSpesa().toString())){
					totSub = totSub.add(subimp.getImportoAttuale());
				}
			}
		}
		
		model.setTotImportiAttualiSubValidi(totSub);
		model.setListaSub(subImp);
	}
	
	/**
	 * Carica nel model una lista di modifiche movimenti di spesa
	 * @param modResponse
	 */
	private void loadResponseModimp (String descImpegno,RicercaImpegnoPerChiaveOttimizzatoResponse response) {
		ArrayList<ModificaConsulta> modList = new ArrayList<ModificaConsulta>();
		
		Impegno impegno = response.getImpegno();
		
		List<SubImpegno> elencoSubImpegni = response.getElencoSubImpegniTuttiConSoloGliIds();
		
		if (impegno.getListaModificheMovimentoGestioneSpesa() != null) {
			for (ModificaMovimentoGestioneSpesa modif : impegno.getListaModificheMovimentoGestioneSpesa()) {
				ModificaConsulta mc = new ModificaConsulta();
				modList.add(mapModifica(mc, modif));
				mc.setDescMain(descImpegno);
			}
		}
		
		if (elencoSubImpegni != null) {
			for (SubImpegno subimp : elencoSubImpegni) {
				if (subimp.getListaModificheMovimentoGestioneSpesa() != null) {
					String descSub = subimp.getNumeroBigDecimal() + " (" +  subimp.getStatoOperativoMovimentoGestioneSpesa() + " dal " + convertDateToString(subimp.getDataStatoOperativoMovimentoGestioneSpesa()) + ")";	
					for (ModificaMovimentoGestioneSpesa modif : subimp.getListaModificheMovimentoGestioneSpesa()) {
						ModificaConsulta mc = new ModificaConsulta();
						modList.add(mapModifica(mc, modif));
						mc.setDescMain(descImpegno);
				    	mc.setNumSub(String.valueOf(subimp.getNumeroBigDecimal()));
				    	mc.setDescSub(descSub);
						
					}
				}
			}
		}
		model.setListaModifiche(modList);
	}
	
	//SIAC-7349 - CONTABILIA-234 - 01/07/2020 - GM: mostrare il residuo delle modifiche di un accertamento che rimane da collegare/
	private BigDecimal calcolaDiCuiPending(Impegno impegno) throws ImportoDeltaException{
		BigDecimal vincoliDiCuiPending = BigDecimal.ZERO;
		BigDecimal totPrimoValore = BigDecimal.ZERO;
		BigDecimal totSecondoValore = BigDecimal.ZERO;
		if(impegno.getListaModificheMovimentoGestioneSpesa() != null && !impegno.getListaModificheMovimentoGestioneSpesa().isEmpty()){
			for(int j =  0; j < impegno.getListaModificheMovimentoGestioneSpesa().size(); j++){
				if(!impegno.getListaModificheMovimentoGestioneSpesa().get(j).isElaboraRorReanno() &&
					impegno.getListaModificheMovimentoGestioneSpesa().get(j).isReimputazione() && 
					!(impegno.getListaModificheMovimentoGestioneSpesa().get(j).getStatoOperativoModificaMovimentoGestione().name().equals(StatoOperativoModificaMovimentoGestione.ANNULLATO.name())) &&
					impegno.getListaModificheMovimentoGestioneSpesa().get(j).getImportoOld() != null &&
					impegno.getListaModificheMovimentoGestioneSpesa().get(j).getImportoOld().compareTo(BigDecimal.ZERO) < 0){
					//PRENDO SEMPRE IL VALORE DELLA MODIFICA COME PRIMO STEP
					totPrimoValore = totPrimoValore.add(impegno.getListaModificheMovimentoGestioneSpesa().get(j).getImportoOld().abs());
					BigDecimal importoDeltaVincolo = impegno.getListaModificheMovimentoGestioneSpesa().get(j).getImportoDeltaVincolo();
					if(importoDeltaVincolo != null && importoDeltaVincolo.compareTo(BigDecimal.ZERO) > 0){
						if(importoDeltaVincolo.compareTo(impegno.getListaModificheMovimentoGestioneSpesa().get(j).getImportoOld().abs()) > 0){
							throw new ImportoDeltaException("Errore nel calcolo di importo delta vincoli");
						}else
							totPrimoValore = totPrimoValore.subtract(importoDeltaVincolo);
					}
					if(impegno.getListaModificheMovimentoGestioneSpesa().get(j).getListaModificheMovimentoGestioneSpesaCollegata() != null &&
						!impegno.getListaModificheMovimentoGestioneSpesa().get(j).getListaModificheMovimentoGestioneSpesaCollegata().isEmpty()){
						for(int k =  0; k < impegno.getListaModificheMovimentoGestioneSpesa().get(j).getListaModificheMovimentoGestioneSpesaCollegata().size(); k++){
							totSecondoValore = totSecondoValore.add(
								impegno.getListaModificheMovimentoGestioneSpesa().get(j).getListaModificheMovimentoGestioneSpesaCollegata().get(k).getImportoCollegamento()
							);
						}
					}
					//QUESTO E' IL VALORE DA RIPARTIRE SUI VINCOLI DI TIPO ACCERTAMENTO CHE NON HANNO MODIFICHE GIA' COLLEGATE
					vincoliDiCuiPending = totPrimoValore.subtract(totSecondoValore);				}
			}
		}
		return vincoliDiCuiPending;
	}
	//END SIAC-7349 - CONTABILIA-234 - 01/07/2020 - GM
		
	private void loadVincoli(Impegno impegno, String descImpegno){
		//SIAC-7349 - CONTABILIA-234 - 01/07/2020 - GM: mostrare il residuo delle modifiche di un accertamento che rimane da collegare
		BigDecimal vincoliDiCuiPending = BigDecimal.ZERO;
		try{
			vincoliDiCuiPending = calcolaDiCuiPending(impegno);
		}catch(ImportoDeltaException e){
			addErrore(ErroreFin.IMPORTO_DELTA_VINCOLI.getErrore(""));
		}
		//END SIAC-7349 - CONTABILIA-234 - 01/07/2020 - GM
	
		List<VincoloImpegno> vincoliList = new ArrayList<VincoloImpegno>();
		if(impegno.getVincoliImpegno()!=null && !impegno.getVincoliImpegno().isEmpty()){
			Collections.sort(impegno.getVincoliImpegno(), Collections.reverseOrder(new ComparaVincoli()));
			for (VincoloImpegno vincoloImpegno : impegno.getVincoliImpegno()) {
		
				//SIAC-7349 - CONTABILIA-234 - 01/07/2020 - GM: se il vincolo è legato ad un accertamento verifico se questo ha delle modifiche già collegate e quindi lo scarto
				//se invece non ha modifiche collegate allora devo calcorare il residuo "diCuiPending" fino ad esaurimento
				if(vincoloImpegno.getAccertamento() != null && 
					(vincoloImpegno.getAccertamento().getListaModificheMovimentoGestioneSpesaCollegata() == null || 
					(vincoloImpegno.getAccertamento().getListaModificheMovimentoGestioneSpesaCollegata() != null && 
					vincoloImpegno.getAccertamento().getListaModificheMovimentoGestioneSpesaCollegata().isEmpty()))){
					if(vincoliDiCuiPending.compareTo(BigDecimal.ZERO) > 0){
						int comparing = vincoliDiCuiPending.compareTo(vincoloImpegno.getImporto()); 
						if(comparing >= 0){
							//diCUiPending > importoVincolo
							vincoliDiCuiPending = vincoliDiCuiPending.subtract(vincoloImpegno.getImporto());
							vincoloImpegno.setDiCuiPending(vincoloImpegno.getImporto());
						}else if (comparing < 0){
							//diCUiPending < importoVincolo
							vincoloImpegno.setDiCuiPending(vincoliDiCuiPending);
							vincoliDiCuiPending = BigDecimal.ZERO;
						}
					}
				}
				//END SIAC-7349 - CONTABILIA-234 - 01/07/2020 - GM
				
				vincoliList.add(vincoloImpegno);
			}
		}
		model.setListaVincoliImpegno(vincoliList);
	}
	
	/**
	 * si occupa di settare nel model i dati per il 
	 * tab degli impegni vincolati all'accertamento consultato
	 * @param accertamento
	 * @param vincoli
	 */
	private void loadVincoli(Accertamento accertamento, List<VincoloAccertamento> vincoli){
		
		//setto la lista con i vincoli:
		model.setListaVincoliAccertamento(vincoli);
		
		//deduco il totale vincoli:
		BigDecimal totVincoli = calcolaTotVincoli(vincoli);
		
		//setto il totale nel model:
		model.setTotVincoliAccertamento(totVincoli);
		
		//deduco la quota ancora utilizzabile
		//come differenza tra l'importo attuale dell'accertamento
		//e la somma dei vincoli:
		BigDecimal importoAcc = accertamento.getImportoAttuale();
		BigDecimal quotaNonVincolataAccertamento = importoAcc.subtract(totVincoli);
		
		//setto la quota non vincolata nel model:
		model.setQuotaNonVincolataAccertamento(quotaNonVincolataAccertamento);
	}
	
	/**
	 * Data una lista di vincoli accertamento itera ne ritorna il totale dei loro valori
	 * @param vincoli
	 * @return
	 */
	private BigDecimal calcolaTotVincoli(List<VincoloAccertamento> vincoli){
		BigDecimal totVincoli = BigDecimal.ZERO;
		if(!isEmpty(vincoli)){
			//Ci sono vincoli
			
			//itero per calcolarmi il totale:
			for(VincoloAccertamento it: vincoli){
				if(it!=null && it.getImporto()!=null){
					//aggiungo il valore dell'elemento
					//iterato alla sommatoria
					totVincoli = totVincoli.add(it.getImporto());
				}
			}
			
		}
		return totVincoli;
	}



	
	/* **************************************************************************** */
	/*  Private methods	per ACCERTAMENTO											*/
	/* **************************************************************************** */

	/**
	 * Ricerca l'Accertamento per chiave e gestisce gli errori o il fallimento della chiamata al servizio
	 * @return
	 */
	private RicercaAccertamentoPerChiaveOttimizzatoResponse getAccertamento () {
		RicercaAccertamentoPerChiaveOttimizzato request = new RicercaAccertamentoPerChiaveOttimizzato();
		RicercaAccertamentoK k = new RicercaAccertamentoK();
			k.setAnnoAccertamento(Integer.valueOf(anno));
			k.setNumeroAccertamento(new BigDecimal(numero));
			k.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
		request.setpRicercaAccertamentoK(k);
		request.setEnte(sessionHandler.getEnte());
		//SIAC-8467
		request.setCaricalistaModificheCollegate(false);
		request.setRichiedente(sessionHandler.getRichiedente());
		
		RicercaAccertamentoPerChiaveOttimizzatoResponse response =  movimentoGestioneFinService.ricercaAccertamentoPerChiaveOttimizzato(request);
		
		if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
			debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			return null;
		}
		return response;
	}

	/**
	 * Carica in un oggetto MovimentoConsulta i dati dell'Impegno
	 * @param mc
	 * @param impegno
	 * @param capitolo
	 * @return
	 */
	private MovimentoConsulta mapMovimento (MovimentoConsulta mc, Accertamento accertamento, CapitoloEntrataGestione capitolo) {
		mc.setTipoMovimento(MovimentoConsulta.ACCERTAMENTO);
		// movimento
		mc.setIdMovimento(accertamento.getUid());
		mc.setAnno(String.valueOf(accertamento.getAnnoMovimento()));
		mc.setNumero(String.valueOf(accertamento.getNumeroBigDecimal()));
		mc.setDescrizione(accertamento.getDescrizione());
	    mc.setImporto(accertamento.getImportoAttuale());
	    mc.setImportoIniziale(accertamento.getImportoIniziale());
	    if (accertamento.getUtenteCreazione() != null) 	mc.setUtenteCreazione(accertamento.getUtenteCreazione());
	    if (accertamento.getUtenteModifica() != null) 	mc.setUtenteModifica(accertamento.getUtenteModifica());
	    // utilizzo la support altrimenti arriva vuota
	    mc.setDataInserimento(accertamento.getDataEmissioneSupport());
	    mc.setDataModifica(accertamento.getDataModifica());
	    mc.setStatoOperativo(accertamento.getDescrizioneStatoOperativoMovimentoGestioneEntrata());
	    mc.setDataStatoOperativo(accertamento.getDataStatoOperativoMovimentoGestioneEntrata());
	    if (accertamento.getTipoMovimentoDesc() != null){
	    	mc.setTipo(accertamento.getTipoMovimentoDesc());
	    }
	    if (accertamento.getDataScadenza() != null)	{
	    	mc.setDataScadenza(accertamento.getDataScadenza());
	    }
        if (accertamento.getProgetto() != null)	{
        	mc.setProgetto(accertamento.getProgetto().getCodice());
        }
        if(accertamento.getAnnoScritturaEconomicoPatrimoniale()!=null){
        	mc.setAnnoScritturaEconomicoPatrimoniale(accertamento.getAnnoScritturaEconomicoPatrimoniale().toString());
        }
        /*SIAC-6929
       // if(accertamento.getAttoAmministrativo().getBloccoRagioneria() == null){
        if(!ObjectUtils.equals(accertamento.getAttoAmministrativo(), null) 
        		&& accertamento.getAttoAmministrativo().getBloccoRagioneria()!= null) {
        	mc.setBloccoRagioneria(ConsultaMovimentoAction.BLOCCO_RAGIONERIA_NA);
        }else if(accertamento.getAttoAmministrativo().getBloccoRagioneria() != null){
        	if(accertamento.getAttoAmministrativo().getBloccoRagioneria().booleanValue()){
        		mc.setBloccoRagioneria(ConsultaMovimentoAction.BLOCCO_RAGIONERIA_SI);	
        	}else {
        		mc.setBloccoRagioneria(ConsultaMovimentoAction.BLOCCO_RAGIONERIA_NO);
        	}
        }
        */
        
      //SIAC-6929
        if(!ObjectUtils.equals(accertamento.getAttoAmministrativo(), null) 
        		&& accertamento.getAttoAmministrativo().getBloccoRagioneria()!= null) {
        
            	if(accertamento.getAttoAmministrativo().getBloccoRagioneria().booleanValue()){
            		mc.setBloccoRagioneria(ConsultaMovimentoAction.BLOCCO_RAGIONERIA_SI);	
            	}else {
            		mc.setBloccoRagioneria(ConsultaMovimentoAction.BLOCCO_RAGIONERIA_NO);
            	}
            }else {
            	mc.setBloccoRagioneria(ConsultaMovimentoAction.BLOCCO_RAGIONERIA_NA);
        }
        
        //SIAC-8629
        //TRANSAZIONE ELEMENTARE:
        mc.setCodificaTransazioneElementare(componiTransazioneElementare(accertamento));
        
        
        // parere finanziario
        if(accertamento.getParereFinanziario()){
        	mc.setParereFinanziario("SI");
        }else mc.setParereFinanziario("NO");
        
        mc.setParereFinanziarioDataModifica(accertamento.getParereFinanziarioDataModifica());
        mc.setParereFinanziarioLoginOperazione(accertamento.getParereFinanziarioLoginOperazione());
        //SIAC-8178
	    mc.setCodiceVerbale(StringUtils.defaultString(accertamento.getCodiceVerbale()));
        
	    // disponibilita
	    mc.setDisponibilitaSub(accertamento.getDisponibilitaSubAccertare());
	    mc.setDisponibilitaIncassare(accertamento.getDisponibilitaIncassare());
	    mc.setTotaleSub(accertamento.getTotaleSubAccertamentiBigDecimal());
	    mc.setDisponibilitaUtilizzare(accertamento.getDisponibilitaUtilizzare());
	    
	    // provvedimento
	    if (accertamento.getAttoAmministrativo() != null) {
	    	mc.getProvvedimento().setAnno(String.valueOf(accertamento.getAttoAmministrativo().getAnno()));
	    	mc.getProvvedimento().setNumero(String.valueOf(accertamento.getAttoAmministrativo().getNumero()));
	    	mc.getProvvedimento().setOggetto(accertamento.getAttoAmministrativo().getOggetto());
	    	if (accertamento.getAttoAmministrativo().getTipoAtto() != null) {
	    		mc.getProvvedimento().setTipo(accertamento.getAttoAmministrativo().getTipoAtto().getDescrizione());
	    	}
	    	if (accertamento.getAttoAmministrativo().getStrutturaAmmContabile() != null) {
	    		mc.getProvvedimento().setStruttura(accertamento.getAttoAmministrativo().getStrutturaAmmContabile().getCodice());
	    	}
	    	mc.getProvvedimento().setStato(accertamento.getAttoAmministrativo().getStatoOperativo());
	    	//SIAC_6929
	    	if(accertamento.getAttoAmministrativo().getBloccoRagioneria() == null){
	        	mc.getProvvedimento().setBloccoRagioneria("N/A");
	        }else if(accertamento.getAttoAmministrativo().getBloccoRagioneria() != null){
	        	if(accertamento.getAttoAmministrativo().getBloccoRagioneria().booleanValue()){
	        		mc.getProvvedimento().setBloccoRagioneria("SI");	
	        	}else {
	        		mc.getProvvedimento().setBloccoRagioneria("NO");
	        	}
	        }
	    	
	    	
	    	
	    }
	    
	    // soggetto
	    if ((accertamento.getSoggetto() != null) && (accertamento.getSoggetto().getUid() != 0)) {
		    mc.getSoggetto().setCodice(accertamento.getSoggetto().getCodiceSoggetto());
		    mc.getSoggetto().setDenominazione(accertamento.getSoggetto().getDenominazione());
		    mc.getSoggetto().setCodiceFiscale(accertamento.getSoggetto().getCodiceFiscale());
		    mc.getSoggetto().setPartitaIva(accertamento.getSoggetto().getPartitaIva());
	    } else if (accertamento.getClasseSoggetto() != null) {
	    	mc.getSoggetto().setClasseSoggettoCodice(accertamento.getClasseSoggetto().getCodice());
	    	mc.getSoggetto().setClasseSoggettoDescrizione(accertamento.getClasseSoggetto().getDescrizione());
	    }
        
        // riaccertamento
        if (accertamento.isFlagDaRiaccertamento()) {
        	mc.setDaRiaccertamento(WebAppConstants.MSG_SI + " " + accertamento.getAnnoRiaccertato() + " / " + accertamento.getNumeroRiaccertato());
        }else{
        	mc.setDaRiaccertamento(WebAppConstants.MSG_NO);
        }	
        
        // inizio SIAC-6997
        if (accertamento.isFlagDaReanno()){ 
        	mc.setDaReanno(WebAppConstants.MSG_SI + " " + accertamento.getAnnoRiaccertato() + " / " + accertamento.getNumeroRiaccertato());
        }else{
        	mc.setDaReanno(WebAppConstants.MSG_NO);
        }
        
        // fine SIAC-6997
        
        mc.setFlagFattura(booleanToWebAppSINO(accertamento.isFlagFattura()));
        mc.setFlagCorrispettivo(booleanToWebAppSINO(accertamento.isFlagCorrispettivo()));
        
        //flag attiva gsa:
        mc.setFlagAttivaGsa(booleanToWebAppSINO(accertamento.isFlagAttivaGsa()));
        //

		
        // capitolo (anno capitolo, / numero capito / ueb descrizione  - codiceStruttuAmm - tipoFinanz)
        if (capitolo != null){
        	mc.getCapitolo().setAnno(capitolo.getAnnoCapitolo());
        	mc.getCapitolo().setNumCapitolo(capitolo.getNumeroCapitolo());
        	mc.getCapitolo().setUeb(BigInteger.valueOf(capitolo.getNumeroUEB()));
        	mc.getCapitolo().setDescrizione(capitolo.getDescrizione());
        	mc.getCapitolo().setArticolo(capitolo.getNumeroArticolo());
        	
        	if (capitolo.getStrutturaAmministrativoContabile() != null){	
        		mc.getCapitolo().setCodiceStrutturaAmministrativa(capitolo.getStrutturaAmministrativoContabile().getCodice());
        	}	
        	if (capitolo.getTipoFinanziamento() != null){
        		mc.getCapitolo().setTipoFinanziamento(capitolo.getTipoFinanziamento().getDescrizione());
        	}
			// Tipo finanziamento
			if(capitolo.getTipoFinanziamento()!=null){
				mc.getCapitolo().setTipoFinanziamento(capitolo.getTipoFinanziamento().getDescrizione());
			}	
			
			if (capitolo.getTitoloEntrata() != null) {
				mc.getCapitolo().setClassificazione(capitolo.getTitoloEntrata().getCodice() + " ");
			}
			if (capitolo.getCategoriaTipologiaTitolo() != null) {
				mc.getCapitolo().setIdMacroAggregato(capitolo.getCategoriaTipologiaTitolo().getUid());
				mc.getCapitolo().setClassificazione(mc.getCapitolo().getClassificazione() + capitolo.getCategoriaTipologiaTitolo().getCodice());
			}
			if (capitolo.getStrutturaAmministrativoContabile() != null) {
				mc.getCapitolo().setCodiceStrutturaAmministrativa(capitolo.getStrutturaAmministrativoContabile().getCodice());
				mc.getCapitolo().setDescrizioneStrutturaAmministrativa(capitolo.getStrutturaAmministrativoContabile().getDescrizione());
				mc.getCapitolo().setUidStruttura(capitolo.getStrutturaAmministrativoContabile().getUid());
			}
			if (capitolo.getElementoPianoDeiConti() != null) {
				mc.getCapitolo().setCodicePdcFinanziario(capitolo.getElementoPianoDeiConti().getCodice());
				mc.getCapitolo().setDescrizionePdcFinanziario(capitolo.getElementoPianoDeiConti().getDescrizione());
				mc.getCapitolo().setIdPianoDeiConti(capitolo.getElementoPianoDeiConti().getUid());
			}
        	
			List<ImportiCapitoloModel> importiCapitolo = new ArrayList<ImportiCapitoloModel>();
        	//PROVA COLOMBO  SIAMO IN ACCERTAMENTO QUINDI CAPITOLO ENTRATA!!!!!
        	mc.getCapitolo().setImportiCapitoloEG(capitolo.getListaImportiCapitolo());
			for (ImportiCapitoloEG currentImporto : capitolo.getListaImportiCapitolo()) {
				ImportiCapitoloModel supportImporti = new ImportiCapitoloModel();
				supportImporti.setAnnoCompetenza(currentImporto.getAnnoCompetenza());
				supportImporti.setCassa(currentImporto.getStanziamentoCassa());
				supportImporti.setResiduo(currentImporto.getStanziamentoResiduo());
				supportImporti.setCompetenza(currentImporto.getStanziamento());
				// RM 08042015, commentato perche' non usato, verifica pulizia functione e attibuti entita' non piu usate
				
				// con i nuovi servizi di BIL le disponibilita
				// degli anni successivi si trattano in altra maniera
				
				if (currentImporto.getAnnoCompetenza().intValue() == mc.getCapitolo().getAnno().intValue()) {
					mc.getCapitolo().setDisponibileAnno1(capitolo.getListaImportiCapitoloEG().get(0).getDisponibilitaAccertareAnno1());
				} else if (currentImporto.getAnnoCompetenza().intValue() == (mc.getCapitolo().getAnno().intValue() + 1)) {
					mc.getCapitolo().setDisponibileAnno2(capitolo.getListaImportiCapitoloEG().get(0).getDisponibilitaAccertareAnno2());
				} else {
					mc.getCapitolo().setDisponibileAnno3(capitolo.getListaImportiCapitoloEG().get(0).getDisponibilitaAccertareAnno3());
				}
				importiCapitolo.add(supportImporti);
			}
			mc.getCapitolo().setImportiCapitolo(importiCapitolo);
        	// FINE PROVA COLOMBO
        }
        
        // SIAC-6695
		mc.setMotivazioneDisponibilitaIncassare(accertamento.getMotivazioneDisponibilitaIncassare());
		mc.setMotivazioneDisponibilitaSubAccertare(accertamento.getMotivazioneDisponibilitaSubAccertare());
		mc.setMotivazioneDisponibilitaUtilizzare(accertamento.getMotivazioneDisponibilitaUtilizzare());
        
        return mc;
	} 
	
	/**
	 * Carica in un oggetto ModificaConsulta i dati della modifica movimento di entrata
	 * @param mc
	 * @param modifica
	 * @return
	 */
	private ModificaConsulta mapModifica (ModificaConsulta mc, ModificaMovimentoGestioneEntrata modifica) {
		mc.setTipoMovimento(MovimentoConsulta.ACCERTAMENTO);
		// modifica
		mc.setNumero(String.valueOf(modifica.getNumeroModificaMovimentoGestione()));
		mc.setDescrizione(modifica.getDescrizioneModificaMovimentoGestione());
	    mc.setImporto(modifica.getImportoOld());
	    mc.setMotivo(modifica.getTipoMovimentoDesc());
	    mc.setUtenteCreazione(modifica.getUtenteCreazione());
	    mc.setUtenteModifica(modifica.getUtenteModifica());
	    // bug che riporta sempre la data vuota
	    // Jira-444
	    mc.setDataInserimento(modifica.getDataEmissione());
	    mc.setDataModifica(modifica.getDataModifica());
	    mc.setStatoOperativo(modifica.getStatoOperativoModificaMovimentoGestione().name());
	    //SIAC-8183
	    mc.setDataStatoOperativo(modifica.getDataFromStatoOperativo());
	    
	    //Reimputazione:
	    mc = settaDatiReimputazioneInModificaConsulta(modifica, mc);
	    
	    // provvedimento
	    if (modifica.getAttoAmministrativo() != null) {
	    	mc.getProvvedimento().setAnno(String.valueOf(modifica.getAttoAmministrativo().getAnno()));
	    	mc.getProvvedimento().setNumero(String.valueOf(modifica.getAttoAmministrativo().getNumero()));
	    	mc.getProvvedimento().setOggetto(modifica.getAttoAmministrativo().getOggetto());
	    	if (modifica.getAttoAmministrativo().getTipoAtto() != null) {
	    		mc.getProvvedimento().setTipo(modifica.getAttoAmministrativo().getTipoAtto().getDescrizione());
	    	}
	    	if (modifica.getAttoAmministrativo().getStrutturaAmmContabile() != null) {
	    		mc.getProvvedimento().setStruttura(modifica.getAttoAmministrativo().getStrutturaAmmContabile().getCodice());
	    	}
	    	mc.getProvvedimento().setStato(modifica.getAttoAmministrativo().getStatoOperativo());
	    }
	    
	    // soggetti per variazione soggetto
	    if ((modifica.getSoggettoOldMovimentoGestione() != null) && (modifica.getSoggettoOldMovimentoGestione().getUid() != 0)) {
		    mc.getSoggettoPrec().setCodice(modifica.getSoggettoOldMovimentoGestione().getCodiceSoggetto());
		    mc.getSoggettoPrec().setDenominazione(modifica.getSoggettoOldMovimentoGestione().getDenominazione());
		    mc.getSoggettoPrec().setCodiceFiscale(modifica.getSoggettoOldMovimentoGestione().getCodiceFiscale());
		    mc.getSoggettoPrec().setPartitaIva(modifica.getSoggettoOldMovimentoGestione().getPartitaIva());
	    } else if (modifica.getClasseSoggettoOldMovimentoGestione() != null) {
	    	mc.getSoggettoPrec().setClasseSoggettoCodice(modifica.getClasseSoggettoOldMovimentoGestione().getCodice());
	    	mc.getSoggettoPrec().setClasseSoggettoDescrizione(modifica.getClasseSoggettoOldMovimentoGestione().getDescrizione());
	    }
	    if ((modifica.getSoggettoNewMovimentoGestione() != null) && (modifica.getSoggettoNewMovimentoGestione().getUid() != 0)) {
		    mc.getSoggettoAttuale().setCodice(modifica.getSoggettoNewMovimentoGestione().getCodiceSoggetto());
		    mc.getSoggettoAttuale().setDenominazione(modifica.getSoggettoNewMovimentoGestione().getDenominazione());
		    mc.getSoggettoAttuale().setCodiceFiscale(modifica.getSoggettoNewMovimentoGestione().getCodiceFiscale());
		    mc.getSoggettoAttuale().setPartitaIva(modifica.getSoggettoNewMovimentoGestione().getPartitaIva());
	    } else if (modifica.getClasseSoggettoNewMovimentoGestione() != null && modifica.getClasseSoggettoNewMovimentoGestione().getUid() != 0) {
	    	mc.getSoggettoAttuale().setClasseSoggettoCodice(modifica.getClasseSoggettoNewMovimentoGestione().getCodice());
	    	mc.getSoggettoAttuale().setClasseSoggettoDescrizione(modifica.getClasseSoggettoNewMovimentoGestione().getDescrizione());
	    }	  
	    
	    //SIAC-7349 Inizio  SR180 CM 22/04/2020 Aggiunto per visualizzazione lista modifiche movimenti di spesa in consulta accertamento
	    mc.setListaModificheMovimentoGestioneSpesaCollegata(model.getMapModificheSpesaCollegate().get(modifica.getIdModificaMovimentoGestione()));
	    //SIAC-7349 Fine  SR180 CM 22/04/2020
	    
	    return mc;
	}

	/**
	 * Carica i dati provenienti dal servizio nel modello dati della pagina per un Impegno
	 * @param impegno
	 * @param capitolo
	 */
	private void loadResponse(Accertamento accertamento, CapitoloEntrataGestione capitolo){
		
		mapMovimento(model.getMovimento(), accertamento, capitolo);

		String descAccertamento = model.getMovimento().getAnno() + "/" + model.getMovimento().getNumero() + 
				" - " + StringUtils.defaultString(model.getMovimento().getDescrizione()) +
				" - " + convertiBigDecimalToImporto(model.getMovimento().getImporto()) + 
				" (" +  model.getMovimento().getStatoOperativo() + " dal " + convertDateToString(model.getMovimento().getDataStatoOperativo()) + ")";	
		
		initMapListaModificheMovimentoGestioneCollegate(accertamento);
		loadResponseSubacc(accertamento, descAccertamento);
		loadResponseModacc(accertamento, descAccertamento);
		//SIAC-8650
		loadVincoliOriginari(accertamento);
	}
	
	//task-93
	private void loadResponseMutuiAccertamento(Accertamento accertamento){
				
		RicercaDettaglioAccertamento request = model.creaRequestRicercaDettaglioAccertamentoMutuo();
		request.setAccertamento(accertamento);
		
		RicercaDettaglioAccertamentoResponse res = movimentoGestioneBilService.ricercaDettaglioAccertamento(request);
		
		if(!res.hasErrori() && res.getAccertamento()!=null) {
			model.setElencoMutuiAssociati(CollectionUtil.getSortedList(res.getAccertamento().getElencoMutuiAssociati(), MutuoAssociatoMovimentoGestioneNumeroMutuoComparator));
		}
	}
	
	/**
	 * Carica nel model una lista di subaccertamenti
	 * @param subResponse
	 */
	private void loadResponseSubacc (Accertamento accertamento, String descAccertamento) {
		ArrayList<MovimentoConsulta> subAcc = new ArrayList<MovimentoConsulta>();
		
		BigDecimal totSub = new BigDecimal(0);
		
		if (accertamento.getElencoSubAccertamenti() != null) {
			for (SubAccertamento subAccIt : accertamento.getElencoSubAccertamenti()) {
				MovimentoConsulta mc = new MovimentoConsulta();
				subAcc.add(mapMovimento(mc, subAccIt, null));
				mc.setDescSuper(descAccertamento);
				//SIAC-7046
				if(!"A".equalsIgnoreCase(subAccIt.getStatoOperativoMovimentoGestioneEntrata().toString())){
					totSub = totSub.add(subAccIt.getImportoAttuale());
				}
			}
		}
		
		model.setTotImportiAttualiSubValidi(totSub);
		model.setListaSub(subAcc);
	}
	
	/**
	 * Carica nel model una lista di modifiche movimenti di spesa
	 * @param modResponse
	 */
	private void loadResponseModacc (Accertamento accertamento, String descImpegno) {
		ArrayList<ModificaConsulta> modList = new ArrayList<ModificaConsulta>();
		
		if (accertamento.getListaModificheMovimentoGestioneEntrata() != null) {
			for (ModificaMovimentoGestioneEntrata modif : accertamento.getListaModificheMovimentoGestioneEntrata()) {
				ModificaConsulta mc = new ModificaConsulta(); 
				if(modif.getAttoAmministrativo() != null){
					if(modif.getAttoAmministrativo().getBloccoRagioneria() == null){
						mc.setBloccoRagioneria("N/A");
					}else if(modif.getAttoAmministrativo().getBloccoRagioneria().booleanValue()){
						mc.setBloccoRagioneria("SI");
					}else{
						mc.setBloccoRagioneria("NO");
					}
				}
				modList.add(mapModifica(mc, modif));
				mc.setDescMain(descImpegno);
			}
		}
		
		if (accertamento.getElencoSubAccertamenti() != null) {
			for (SubAccertamento subacc : accertamento.getElencoSubAccertamenti()) {
				if (subacc.getListaModificheMovimentoGestioneEntrata() != null) {
					String descSub = subacc.getNumeroBigDecimal() + " (" +  subacc.getStatoOperativoMovimentoGestioneEntrata() + " dal " + convertDateToString(subacc.getDataStatoOperativoMovimentoGestioneEntrata()) + ")";	
					for (ModificaMovimentoGestioneEntrata modif : subacc.getListaModificheMovimentoGestioneEntrata()) {
						ModificaConsulta mc = new ModificaConsulta();
						modList.add(mapModifica(mc, modif));
						mc.setDescMain(descImpegno);
				    	mc.setNumSub(String.valueOf(subacc.getNumeroBigDecimal()));
				    	mc.setDescSub(descSub);
					}
				}
			}
		}
		model.setListaModifiche(modList);
	}
	
	public boolean oggettoDaPopolareImpegno(){
		if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO)){
			return true;
		}else{
			return false;
		}
	}
	
	
	//Classe per ordinamento della lista
	class NumComparator implements Comparator<ModificaConsulta> {
	    @Override
	    public int compare(ModificaConsulta objToCampareUno, ModificaConsulta objToCampareDue) {
			if(objToCampareUno!=null && objToCampareDue!=null){
			    	return Integer.parseInt(objToCampareDue.getNumero()) < Integer.parseInt(objToCampareUno.getNumero()) ? -1 : objToCampareUno.getNumero() ==  objToCampareDue.getNumero() ? 0 : 1;
			} else {
	            return -1;
	        }
	    }
	}
	
	@Override
	protected void setCapitoloSelezionato(CapitoloImpegnoModel supportCapitolo) {}

	@Override
	public String listaClasseSoggettoChanged() {
		return null;	
	}

	@Override
	protected void setErroreCapitolo() {}

	@Override
	protected void setSoggettoSelezionato(SoggettoImpegnoModel soggettoImpegnoModel) {}

	@Override
	protected void setProvvedimentoSelezionato(ProvvedimentoImpegnoModel currentProvvedimento) {}
	
	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */

	public String getUidPerDettaglioSub() {
		return uidPerDettaglioSub;
	}
	public void setUidPerDettaglioSub(String uidPerDettaglioSub) {
		this.uidPerDettaglioSub = uidPerDettaglioSub;
	}

	public String getUidPerDettaglioMod() {
		return uidPerDettaglioMod;
	}
	public void setUidPerDettaglioMod(String uidPerDettaglioMod) {
		this.uidPerDettaglioMod = uidPerDettaglioMod;
	}

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

	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	/**
	 * GESTORE TRANSAZIONE ECONOMICA
	 */
	@Override
	public boolean missioneProgrammaAttivi() {
		return false;
	}

	@Override
	public boolean cofogAttivo() {
		return false;
	}

	@Override
	public boolean cupAttivo() {
		return false;
	}

	@Override
	public boolean transazioneElementareAttiva() {
		return false;
	}

	@Override
	public boolean altriClassificatoriAttivi() {
		return false;
	}

	@Override
	public String confermaPdc() {
		return null;
	}


	@Override
	public String confermaSiope() {
		return null;
	}

	@Override
	public boolean programmaPoliticoRegionaleUnitarioAttivo() {
		return false;
	}
	
	@Override
	public boolean datiUscitaImpegno() {
		if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO)){
			return true;
		}else{
			return false;
		}	
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
	
	
	// siac-3762
	public String consultaModificheProvvedimento() throws Exception{
		setMethodName("consultaModificheProvvedimento");
		
		impostaMovimentoGestione(tipo.equals("I") ? new Impegno(): new Accertamento(), model.getMovimento().getIdMovimento(), CostantiFin.MOVGEST_TS_TIPO_TESTATA);
		
		return "consultaModificheProvvedimento";
	}
	
	//  siac-3762
	public String consultaModificheProvvedimentoSub() throws Exception{
		setMethodName("consultaModificheProvvedimentoSub");
		
		impostaMovimentoGestione(tipo.equals("I") ? new SubImpegno(): new SubAccertamento(), Integer.parseInt(getUidSubMovimento()), CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO);
		
		return "consultaModificheProvvedimento";
	}


	public void impostaMovimentoGestione(MovimentoGestione movimento, Integer idMovimento, String tipoMovimento) {
		
		movimento.setUid(idMovimento);
		movimento.setTipoMovimento(tipoMovimento);
		leggiStoricoProvvedimentoByMovimento(movimento);
		
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
	 * @return the uidSubMovimento
	 */
	public String getUidSubMovimento() {
		return uidSubMovimento;
	}


	/**
	 * @param uidSubMovimento the uidSubMovimento to set
	 */
	public void setUidSubMovimento(String uidSubMovimento) {
		this.uidSubMovimento = uidSubMovimento;
	}
	
	@Override
	public String listaClasseSoggettoDueChanged() {
		//Auto-generated method stub
		return null;
	}

	@Override
	protected void setSoggettoSelezionatoDue(SoggettoImpegnoModel soggettoImpegnoModel) {
		//Auto-generated method stub
	}
	
	private void initMapListaModificheMovimentoGestioneCollegate(Accertamento movimentoGestione){
		Map<Integer, List<ModificaMovimentoGestioneSpesaCollegata>> map = new HashMap<Integer, List<ModificaMovimentoGestioneSpesaCollegata>>();
		RicercaModulareModificaMovimentoSpesaCollegata req = new RicercaModulareModificaMovimentoSpesaCollegata();
		req.setRichiedente(model.getRichiedente());
		req.setAnnoBilancio(model.getnAnno());
		req.setDataOra(new Date());
		req.setAccertamento(movimentoGestione);
		
		RicercaModulareModificaMovimentoSpesaCollegataResponse response = movimentoGestioneFinService.ricercaModulareModificaMovimentoSpesaCollegata(req);
	
		if(response != null && (response.isFallimento() || response.hasErrori())){
			addErrori(response.getErrori());
			return;
		}
		
		if(response != null) {
			for (ModificaMovimentoGestioneSpesaCollegata modifica : CoreUtil.checkList(response.getListaModificheMovimentoGestioneSpesaCollegata())) {
				if(!map.containsKey(modifica.getModificaMovimentoGestioneEntrata().getIdModificaMovimentoGestione())) {
					map.put(modifica.getModificaMovimentoGestioneEntrata().getIdModificaMovimentoGestione(), new ArrayList<ModificaMovimentoGestioneSpesaCollegata>());
				}
				map.get(modifica.getModificaMovimentoGestioneEntrata().getIdModificaMovimentoGestione()).add(modifica);
			}
			
			model.setMapModificheSpesaCollegate(map);
		}
	}


}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbito;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbitoResponse;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestioneCruscottoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciMovGestModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegnoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciImpegnoRorAction extends ActionKeyAggiornaImpegno {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String anno;
	private String numero;
	private MotiviReimputazioneROR[] motiviRorReimputazione;
	private CodificaFin reimputazione;
	private String descrizioneMotivoReimp;
	private List<GestioneCruscottoModel> listaGestioneCruscotto;
	
	/****************/
	private String minImp;
	private String maxImp;
	private String minSub;
	private String maxSub;
	private String minAnche;
	private String maxAnche;
	private String tipoImpegno;
	private String importoImpegnoModImp;
	private String importoAttualeSubImpegno;
	private String descrizione;
	private List<String> tipoImpegnoModificaImportoList = new ArrayList<String>();
	private List<String> numeroSubImpegnoList = new ArrayList<String>();
	private List<String> abbinaList = new ArrayList<String>();
	private String abbina;
	private String abbinaChk;
	private boolean subImpegnoSelected = false;
	private String subSelected;
	private String anniPluriennali;
	private String idTipoMotivo;
	
	//Parametri SubImpegno
	private String numeroSubImpegno;
	
	
	//IMPORTI CALCOLATI
	private String minImportoCalcolato;
	private String maxImportoCalcolato;
	private String idSub;
	private String minImportoSubCalcolato;
	private String maxImportoSubCalcolato;
	private BigDecimal minImportoImpegnoApp;
	private BigDecimal maxImportoImpegnoApp;
	private BigDecimal minImportoSubApp;
	private BigDecimal maxImportoSubApp;
	
	private AttoAmministrativo am;
	
	private Boolean flagValido;
	private Boolean flagSoggettoValido;
	/*****************/

	
	public Boolean getFlagValido() {
		return flagValido;
	}


	public String getMinImp() {
		return minImp;
	}


	public void setMinImp(String minImp) {
		this.minImp = minImp;
	}


	public String getMaxImp() {
		return maxImp;
	}


	public void setMaxImp(String maxImp) {
		this.maxImp = maxImp;
	}


	public String getMinSub() {
		return minSub;
	}


	public void setMinSub(String minSub) {
		this.minSub = minSub;
	}


	public String getMaxSub() {
		return maxSub;
	}


	public void setMaxSub(String maxSub) {
		this.maxSub = maxSub;
	}


	public String getMinAnche() {
		return minAnche;
	}


	public void setMinAnche(String minAnche) {
		this.minAnche = minAnche;
	}


	public String getMaxAnche() {
		return maxAnche;
	}


	public void setMaxAnche(String maxAnche) {
		this.maxAnche = maxAnche;
	}


	public String getTipoImpegno() {
		return tipoImpegno;
	}


	public void setTipoImpegno(String tipoImpegno) {
		this.tipoImpegno = tipoImpegno;
	}


	public String getImportoImpegnoModImp() {
		return importoImpegnoModImp;
	}


	public void setImportoImpegnoModImp(String importoImpegnoModImp) {
		this.importoImpegnoModImp = importoImpegnoModImp;
	}


	public String getImportoAttualeSubImpegno() {
		return importoAttualeSubImpegno;
	}


	public void setImportoAttualeSubImpegno(String importoAttualeSubImpegno) {
		this.importoAttualeSubImpegno = importoAttualeSubImpegno;
	}


	public String getDescrizione() {
		return descrizione;
	}


	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}


	public List<String> getTipoImpegnoModificaImportoList() {
		return tipoImpegnoModificaImportoList;
	}


	public void setTipoImpegnoModificaImportoList(List<String> tipoImpegnoModificaImportoList) {
		this.tipoImpegnoModificaImportoList = tipoImpegnoModificaImportoList;
	}


	public List<String> getNumeroSubImpegnoList() {
		return numeroSubImpegnoList;
	}


	public void setNumeroSubImpegnoList(List<String> numeroSubImpegnoList) {
		this.numeroSubImpegnoList = numeroSubImpegnoList;
	}


	public List<String> getAbbinaList() {
		return abbinaList;
	}


	public void setAbbinaList(List<String> abbinaList) {
		this.abbinaList = abbinaList;
	}


	public String getAbbina() {
		return abbina;
	}


	public void setAbbina(String abbina) {
		this.abbina = abbina;
	}


	public String getAbbinaChk() {
		return abbinaChk;
	}


	public void setAbbinaChk(String abbinaChk) {
		this.abbinaChk = abbinaChk;
	}


	public boolean isSubImpegnoSelected() {
		return subImpegnoSelected;
	}


	public void setSubImpegnoSelected(boolean subImpegnoSelected) {
		this.subImpegnoSelected = subImpegnoSelected;
	}


	public String getSubSelected() {
		return subSelected;
	}


	public void setSubSelected(String subSelected) {
		this.subSelected = subSelected;
	}


	public String getAnniPluriennali() {
		return anniPluriennali;
	}


	public void setAnniPluriennali(String anniPluriennali) {
		this.anniPluriennali = anniPluriennali;
	}


	public String getIdTipoMotivo() {
		return idTipoMotivo;
	}


	public void setIdTipoMotivo(String idTipoMotivo) {
		this.idTipoMotivo = idTipoMotivo;
	}


	public String getNumeroSubImpegno() {
		return numeroSubImpegno;
	}


	public void setNumeroSubImpegno(String numeroSubImpegno) {
		this.numeroSubImpegno = numeroSubImpegno;
	}


	public String getMinImportoCalcolato() {
		return minImportoCalcolato;
	}


	public void setMinImportoCalcolato(String minImportoCalcolato) {
		this.minImportoCalcolato = minImportoCalcolato;
	}


	public String getMaxImportoCalcolato() {
		return maxImportoCalcolato;
	}


	public void setMaxImportoCalcolato(String maxImportoCalcolato) {
		this.maxImportoCalcolato = maxImportoCalcolato;
	}


	public String getIdSub() {
		return idSub;
	}


	public void setIdSub(String idSub) {
		this.idSub = idSub;
	}


	public String getMinImportoSubCalcolato() {
		return minImportoSubCalcolato;
	}


	public void setMinImportoSubCalcolato(String minImportoSubCalcolato) {
		this.minImportoSubCalcolato = minImportoSubCalcolato;
	}


	public String getMaxImportoSubCalcolato() {
		return maxImportoSubCalcolato;
	}


	public void setMaxImportoSubCalcolato(String maxImportoSubCalcolato) {
		this.maxImportoSubCalcolato = maxImportoSubCalcolato;
	}


	public BigDecimal getMinImportoImpegnoApp() {
		return minImportoImpegnoApp;
	}


	public void setMinImportoImpegnoApp(BigDecimal minImportoImpegnoApp) {
		this.minImportoImpegnoApp = minImportoImpegnoApp;
	}


	public BigDecimal getMaxImportoImpegnoApp() {
		return maxImportoImpegnoApp;
	}


	public void setMaxImportoImpegnoApp(BigDecimal maxImportoImpegnoApp) {
		this.maxImportoImpegnoApp = maxImportoImpegnoApp;
	}


	public BigDecimal getMinImportoSubApp() {
		return minImportoSubApp;
	}


	public void setMinImportoSubApp(BigDecimal minImportoSubApp) {
		this.minImportoSubApp = minImportoSubApp;
	}


	public BigDecimal getMaxImportoSubApp() {
		return maxImportoSubApp;
	}


	public void setMaxImportoSubApp(BigDecimal maxImportoSubApp) {
		this.maxImportoSubApp = maxImportoSubApp;
	}


	public AttoAmministrativo getAm() {
		return am;
	}


	public void setAm(AttoAmministrativo am) {
		this.am = am;
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


	public List<GestioneCruscottoModel> getListaGestioneCruscotto() {
		return listaGestioneCruscotto;
	}


	public void setListaGestioneCruscotto(List<GestioneCruscottoModel> listaGestioneCruscotto) {
		this.listaGestioneCruscotto = listaGestioneCruscotto;
	}

	@Autowired @Qualifier("movimentoGestioneFinService")
	MovimentoGestioneService movimentoGestioneFinService;


	


	public String getDescrizioneMotivoReimp() {
		return descrizioneMotivoReimp;
	}


	public void setDescrizioneMotivoReimp(String descrizioneMotivoReimp) {
		this.descrizioneMotivoReimp = descrizioneMotivoReimp;
	}


	public CodificaFin getReimputazione() {
		return reimputazione;
	}


	public void setReimputazione(CodificaFin reimputazione) {
		this.reimputazione = reimputazione;
	}


	public MotiviReimputazioneROR[] getMotiviRorReimputazione() {
		return motiviRorReimputazione;
	}


	public void setMotiviRorReimputazione(MotiviReimputazioneROR[] motiviRorReimputazione) {
		this.motiviRorReimputazione = motiviRorReimputazione;
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
	
	public InserisciImpegnoRorAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.IMPEGNO;
	}


	@Override
	public void prepare() throws Exception {
		
		
		super.prepare();
		motiviRorReimputazione = MotiviReimputazioneROR.values();
		setMethodName("prepare");
		
		//Reimputazione:
		inizializzaReimputazioneInInserimentoNelModel();
		
		//invoco il prepare della super classe:
		
		
		//setto il titolo:
		super.model.setTitolo("Inserisci Modifica Importo");
		
		//Carico Lista Tipo Modifiche
		if(abilitaListaROR()){
			caricaListaMotiviROR();
			model.setGestioneModificaDecentratoEFaseROR(true);
		}else{
			caricaListaMotivi();
			model.setGestioneModificaDecentratoEFaseROR(false);
		}
			
		//Preparo Attibruti per la pagina
		getAbbinaList().add("Modifica Anche Impegno");
		
		if(model.getListaSubimpegni() != null && model.getListaSubimpegni().size() > 0){
			
			//ciclo sui sub:
			for(SubImpegno sub : model.getListaSubimpegni()){				
				if(sub.getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("D")){
					if(sub.getNumeroBigDecimal() != null){
						//aggiungo il sub alla lista
						numeroSubImpegnoList.add(String.valueOf(sub.getNumeroBigDecimal()));
					}	
				}
				
			}
			
			if(numeroSubImpegnoList.size() > 0){
				//ci sono sub
				tipoImpegnoModificaImportoList.add("Impegno");
				tipoImpegnoModificaImportoList.add("SubImpegno");
				
			} else {
				//non ci sono sub
				tipoImpegnoModificaImportoList.add("Impegno");	
			}

		} else {
			//non ci sono sub
			tipoImpegnoModificaImportoList.add("Impegno");
		}
	
		
		//Calcolo importi
		
		if(model.getDisponibilitaImpegnoModifica()==null){
			model.setDisponibilitaImpegnoModifica(BigDecimal.ZERO);
		}
		
		//FIX PER JIRA SIAC-3801
		//minImportoImpegnoApp = minoreTraDisponibileInModificaEDispAVincolare().negate();

		// RM JIRA 5371
		minImportoImpegnoApp = model.getDisponibilitaImpegnoModifica().negate();
		
		
		//
		
		if(model.getImpegnoInAggiornamento().getAnnoMovimento() < sessionHandler.getAnnoBilancio()){
			maxImportoImpegnoApp = new BigDecimal(0);
		} else if(model.getImpegnoInAggiornamento().isFlagDaRiaccertamento()){
			maxImportoImpegnoApp = new BigDecimal(0);
		} else {
			//SIAC-580
			
			if(model.getStep1Model().getAnnoImpegno().intValue() == model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getAnnoCapitolo().intValue() ){
				// anno corrente
				maxImportoImpegnoApp = model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getImportiCapitolo().getDisponibilitaImpegnareAnno1();
			}else if(model.getStep1Model().getAnnoImpegno().intValue()  == (model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getAnnoCapitolo().intValue() +1) ){
//				anno  +1
				maxImportoImpegnoApp = model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getImportiCapitolo().getDisponibilitaImpegnareAnno2();
			}else if(model.getStep1Model().getAnnoImpegno().intValue()  == (model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getAnnoCapitolo().intValue() +2) ){
//				anno +2
				maxImportoImpegnoApp = model.getImpegnoInAggiornamento().getCapitoloUscitaGestione().getImportiCapitolo().getDisponibilitaImpegnareAnno3();
			
			//SIAC-6990
			//si consente l'inserimento delle modifiche solo sui tre anni successivi (inerenti alla disponibilita' massima dei capitoli)
			}else{
				//  da GESTIRE NELLA PAGINA
//				addErrore(new Errore("FIN_ERR_0289", "Impossibile inserire modifiche superiori alla durata dei capitoli"));
				model.setFlagSuperioreTreAnni(true);
				return;
			}
		}
		
		if(minImportoImpegnoApp == null){
			//pongo a zero
			minImportoImpegnoApp = new BigDecimal(0);
		}
		
		if(maxImportoImpegnoApp == null){
			//pongo a zero
			maxImportoImpegnoApp = new BigDecimal(0);
		}
		
		
		//GESTIONE FLAG SDF:
		if(model.getImpegnoInAggiornamento().isFlagSDF()){
			maxImportoImpegnoApp = new BigDecimal(0);
		}
		//
		
		if(maxImportoImpegnoApp.compareTo(new BigDecimal(0)) < 0){
			maxImportoImpegnoApp = new BigDecimal(0);
		}
		
		//Setto il massimo e i minimo nelle variabili finali:
		setMaxImp(convertiBigDecimalToImporto(maxImportoImpegnoApp));
		model.setMaxImpMod(convertiBigDecimalToImporto(maxImportoImpegnoApp));
		setMinImp(convertiBigDecimalToImporto(minImportoImpegnoApp));
		model.setMinImpMod(convertiBigDecimalToImporto(minImportoImpegnoApp));
		
		setMinImportoCalcolato(convertiBigDecimalToImporto(minImportoImpegnoApp));
		model.setMinImportoCalcolatoMod(convertiBigDecimalToImporto(minImportoImpegnoApp));
		setMaxImportoCalcolato(convertiBigDecimalToImporto(maxImportoImpegnoApp));
		model.setMaxImportoCalcolatoMod(convertiBigDecimalToImporto(maxImportoImpegnoApp));
		
		if (StringUtils.isEmpty(getAnniPluriennali())) {
			setAnniPluriennali("1");
		}
		//SOLO PROVA : conservo tutto il model in sessione e lo setto successivamente?
		//FIXME
//		sessionHandler.setParametro(FinSessionParameter.GESTISCI_IMPEGNO_MODEL, model);
		sessionHandler.setParametro(FinSessionParameter.PROVVEDIMENTO_IMPEGNO, model.getStep1Model().getProvvedimento());
		
	}
	
	
	private CodificaFin getReimputazioneMotivo(List<CodificaFin> listaTipoMotivo) {
		for(CodificaFin codifica : listaTipoMotivo){
			if(codifica.getCodice().equals("REIMP")){
				return codifica;
			}
		}
		return null;
	}


	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		reimputazione = getReimputazioneMotivo(model.getMovimentoSpesaModel().getListaTipoMotivo());
		descrizioneMotivoReimp=reimputazione.getDescrizione();
		if(null!=model.getStep1Model().getProvvedimento() && null!=model.getStep1Model().getProvvedimento().getAnnoProvvedimento()){	
			//CHIAMO IL POPOLA COSI HO UN'ISTANZA RICREATA CON IL "new" in modo da evitare ogni incrocio di dati con il provvedimento
			//salvato in memoria che verra' usato momentaneamente per la modifica movimento:
			AttoAmministrativo attoImpegno = popolaProvvedimento(model.getStep1Model().getProvvedimento());
			setAm(attoImpegno);
		}
		return SUCCESS;
		
		
	}
	
	public String salva() throws Exception{
		//FIXME non lo so: non mi convince piu di tanto
		setModel((GestisciMovGestModel) sessionHandler.getParametro(FinSessionParameter.GESTISCI_IMPEGNO_MODEL));
		RicercaImpegnoPerChiave request = createRequestForRicercaImpegno();
		
		RicercaImpegnoPerChiaveResponse rpr = movimentoGestioneFinService.ricercaImpegnoPerChiave(request);
		
		if(rpr.hasErrori()){
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, rpr.getErrori());
			return INPUT;
		}
		
		Impegno impegno = rpr.getImpegno();		
		AggiornaImpegno ai = convertiModelPerChiamataServizioInserisciAggiornaModifiche(impegno);
		ModificaMovimentoGestioneSpesa spesa = new ModificaMovimentoGestioneSpesa();
		
		//Non lo so...da rivederlo FIXME TODO
		ProvvedimentoImpegnoModel pim = sessionHandler.getParametro(FinSessionParameter.PROVVEDIMENTO_IMPEGNO);
		//ProvvedimentoImpegnoModel pim = model.getStep1Model().getProvvedimento();
//		modifica.setAnnoReimputazione(2021);
//		modifica.setAttoAmministrativo(impegno.getAttoAmministrativo());
//		modifica.setUid(0);
//		modifica.setTipoModificaMovimentoGestione("REIMP");
//		modifica.setDescrizioneModificaMovimentoGestione("PROVA MANUEL PIPPO BAUDO");
//		modifica.setImportoOld(convertiImportoToBigDecimal("-1,00"));
//		//modifica.s
//		modifica.setImportoNew(impegno.getImportoAttuale().add(convertiImportoToBigDecimal("-1,00")));
//		modifica = settaDatiProvvDalModel(modifica, pim);		
//		modifica.setTipoMovimento("IMP");
//		modifica.setIdStrutturaAmministrativa(impegno.getIdStrutturaAmministrativa());
//		modifica.getAttoAmministrativo().setStrutturaAmmContabile(impegno.getAttoAmministrativo().getStrutturaAmmContabile());
//		modifica.getAttoAmministrativo().setUid(impegno.getAttoAmministrativo().getUid());
		
		BigDecimal importoDiInput = convertiImportoToBigDecimal("-1,00");
		BigDecimal importoAttuale = impegno.getImportoAttuale();
		
		BigDecimal importoCambiato = importoAttuale.add(importoDiInput);
		
		spesa.setUid(0);	
		spesa.setImportoOld(convertiImportoToBigDecimal("-1,00"));
		spesa.setImportoNew(importoCambiato);
		
		
		
		
		
		
		
		
		
		impegno.setImportoAttuale(importoCambiato);
		
//		spesa.setAttoAmministrativoAnno(String.valueOf(pim.getAnnoProvvedimento()));
//		spesa.setAttoAmministrativoNumero(pim.getNumeroProvvedimento().intValue());
//		if(!isEmpty(pim.getCodiceTipoProvvedimento())){
//			spesa.setAttoAmministrativoTipoCode(String.valueOf(pim.getCodiceTipoProvvedimento()));
//		} else {	
//			spesa.setAttoAmministrativoTipoCode(String.valueOf(pim.getTipoProvvedimento()));
//		}	
//		spesa.setIdStrutturaAmministrativa(pim.getUidStrutturaAmm());
//		AttoAmministrativo attoAmm = popolaProvvedimento(pim);
//		spesa.setAttoAmministrativo(attoAmm);
//		
//		//va tenuto coerente:
//		TipoAtto attoAmmTipoAtto = attoAmm.getTipoAtto();
//		spesa.setAttoAmmTipoAtto(attoAmmTipoAtto);
		
		
		
		spesa.setAttoAmministrativoAnno(String.valueOf(pim.getAnnoProvvedimento()));
		spesa.setAttoAmministrativoNumero(pim.getNumeroProvvedimento().intValue());
		if(!isEmpty(pim.getCodiceTipoProvvedimento())){
			spesa.setAttoAmministrativoTipoCode(String.valueOf(pim.getCodiceTipoProvvedimento()));
		} else {	
			spesa.setAttoAmministrativoTipoCode(String.valueOf(pim.getTipoProvvedimento()));
		}	
		spesa.setIdStrutturaAmministrativa(pim.getUidStrutturaAmm());
		AttoAmministrativo attoAmm = popolaProvvedimento(pim);
		spesa.setAttoAmministrativo(attoAmm);
		
		//va tenuto coerente:
		TipoAtto attoAmmTipoAtto = attoAmm.getTipoAtto();
		spesa.setAttoAmmTipoAtto(attoAmmTipoAtto);
		//
		
		spesa.setTipoModificaMovimentoGestione("REIMP");
		spesa.setDescrizione("PROVA INSERIMENTO DA GESTISCI".toUpperCase());
		spesa.setTipoMovimento("IMP");
		spesa.setIdStrutturaAmministrativa(impegno.getIdStrutturaAmministrativa());
		spesa.getAttoAmministrativo().setStrutturaAmmContabile(impegno.getAttoAmministrativo().getStrutturaAmmContabile());
		spesa.getAttoAmministrativo().setUid(impegno.getAttoAmministrativo().getUid());
		
		
		List<ModificaMovimentoGestioneSpesa> lista = new ArrayList<ModificaMovimentoGestioneSpesa>();
		lista.add(spesa);
		impegno.setListaModificheMovimentoGestioneSpesa(lista);
		//impegno.setImportoAttuale(impegno.getImportoAttuale());
		//da capire
		
		//model.getImpegnoInAggiornamento().getListaModificheMovimentoGestioneSpesa().add(modifica);
		ai.setImpegno(impegno);
		ai.setRichiedente(sessionHandler.getRichiedente());
		ai.setEnte(sessionHandler.getEnte());

		
		AggiornaImpegnoResponse response = movimentoGestioneFinService.aggiornaImpegno(ai);

		return "success";
	}
	
	private RicercaImpegnoPerChiave createRequestForRicercaImpegno() {
		RicercaImpegnoPerChiave request = new RicercaImpegnoPerChiave();
		RicercaImpegnoK ra = new RicercaImpegnoK();
		ra.setAnnoImpegno(2019);
		ra.setNumeroImpegno(new BigDecimal("9"));
		ra.setAnnoEsercizio(sessionHandler.getAnnoBilancio());	
		request.setpRicercaImpegnoK(ra);
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setEnte(sessionHandler.getEnte());
		return request;
	}
	
	
	protected AggiornaImpegno convertiModelPerChiamataServizioInserisciAggiornaModifiche (Impegno impegno) {
		
		// AGGIORNA IMPEGNO	   
	   AggiornaImpegno aggiornaImpegnoReq = new AggiornaImpegno();	    
	   Bilancio bilancio = new Bilancio();
	   bilancio.setAnno(sessionHandler.getAnnoBilancio());	   
	   aggiornaImpegnoReq.setImpegno(impegno);
	   aggiornaImpegnoReq.setEnte(sessionHandler.getEnte());
	   aggiornaImpegnoReq.getImpegno().setSoggetto(impegno.getSoggetto());
	     
	   
	   // subimpegni
	   if(!isEmpty(impegno.getElencoSubImpegni())){
		   aggiornaImpegnoReq.getImpegno().setElencoSubImpegni(impegno.getElencoSubImpegni());
	   }else{
		   aggiornaImpegnoReq.getImpegno().setElencoSubImpegni(new ArrayList<SubImpegno>());
	   }
	   
	   // eventuali vincoli
	   if(null!=impegno.getVincoliImpegno() && !impegno.getVincoliImpegno().isEmpty()){
		   aggiornaImpegnoReq.getImpegno().setVincoliImpegno(impegno.getVincoliImpegno());
	   }
		   
	   aggiornaImpegnoReq.setRichiedente(sessionHandler.getRichiedente());
	   aggiornaImpegnoReq.setUnitaElementareDiGestione(null);
	   aggiornaImpegnoReq.setBilancio(bilancio);
	   
	   return aggiornaImpegnoReq;
	}
	
	

	
	private void caricaListaAmbiti() {
		
		RicercaTipiAmbito request = model.creaRequestRicercaTipiAmbito();
		request.setAnno(sessionHandler.getAnnoBilancio());
		RicercaTipiAmbitoResponse response = progettoService.ricercaTipiAmbito(request);

		model.setListaTipiAmbito(response.getTipiAmbito());
	}
	
	
	

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.liquidazione.ImpegnoLiquidazioneModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.CausaleEntrataTendinoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.RicercaOrdinativoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaOrdinativoPagamento;

public class WizardRicercaOrdinativoAction extends GenericOrdinativoAction<RicercaOrdinativoModel> {

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getActionKey() {
		return "ricercaOrdinativo";
	}
	
	/**
	 * Compone la request per il servizio di ricerca
	 * @return
	 */
	protected RicercaOrdinativo convertiModelPerChiamataServizioRicercaOrdinativo() {
		//istanzio la request:
		RicercaOrdinativo ricercaOrdinativo = new RicercaOrdinativo();
		
		//popolo opportunamente i campi della request:
		
		ParametroRicercaOrdinativoPagamento prop = new ParametroRicercaOrdinativoPagamento();
		
		//Richiedente
		ricercaOrdinativo.setRichiedente(sessionHandler.getRichiedente());
		//ente
		ricercaOrdinativo.setEnte(sessionHandler.getAccount().getEnte());
		//Anno Esercizio
		prop.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
		
		//numero ordinativo da
		if(model.getNumeroOrdinativoDa() != null){
			prop.setNumeroOrdinativoDa(model.getNumeroOrdinativoDa());
		}
		
		//numero ordinativo a
		if(model.getNumeroOrdinativoA() != null){
			prop.setNumeroOrdinativoA(model.getNumeroOrdinativoA());
		}
		
		//data emissione da
		if(StringUtils.isNotEmpty(model.getDataEmissioneDa())){
			prop.setDataEmissioneDa(DateUtility.parse(model.getDataEmissioneDa()));
		}	
		
		//data emissione a
		if(StringUtils.isNotEmpty(model.getDataEmissioneA())){
			prop.setDataEmissioneA(DateUtility.parse(model.getDataEmissioneA()));
		}

		//distinta
		if(StringUtils.isNotEmpty(model.getDistinta())){
			prop.setCodiceDistinta(model.getDistinta());
		}

		//conto tesoriere
		if(StringUtils.isNotEmpty(model.getContoTesoriere())){
			prop.setContoDelTesoriere(model.getContoTesoriere());
		}

		//data trasmissione oil
		if(StringUtils.isNotEmpty(model.getDataTrasmissioneOIL())){
			prop.setDataTrasmissioneOIL(DateUtility.parse(model.getDataTrasmissioneOIL()));
		}
		
		//stato operativo
		if(StringUtils.isNotEmpty(model.getStatoOperativo())){
			prop.setStatoOperativo(model.getStatoOperativo());
		}
		
		//COPIAMO DEL CAMPO HIDDEN PER VIA DEL CHECKBOX CHE PERDE IL VALORE:
		model.setEscludiAnnullati(model.getHiddenPerEscludiAnnullati());
		if(model.getEscludiAnnullati()!=null && model.getEscludiAnnullati()==true){
			List<String> statiDaEscludere = new ArrayList<String>();
			statiDaEscludere.add("A");
			prop.setStatiDaEscludere(statiDaEscludere);
		}
			
		//stati da escludere:
		if(StringUtils.isNotEmpty(model.getStatoOperativo()) && "A".equals(model.getStatoOperativo())){
			//In caso di annullato vince su escludi
			if(model.getEscludiAnnullati()!=null && model.getEscludiAnnullati()==true){
				prop.setStatiDaEscludere(null);
			}
		}
		
		//DESCRIZIONE:
		if(model.getDescrizioneOrdinativo() != null){
			prop.setDescrizione(model.getDescrizioneOrdinativo());
		}
		
		
		if(model.getDaTrasmettere() != null){
			prop.setDaTrasmettere(model.getDaTrasmettere());
		}
		
		

		//anno liquidazione
		if(model.getAnnoLiquidazione()!=null){
			prop.setAnnoLiquidazione(Integer.valueOf(model.getAnnoLiquidazione()));
		}
		
		//numero liquidazione
		if(model.getNumeroLiquidazione() != null){
			prop.setNumeroLiquidazione(model.getNumeroLiquidazione());
		}
		
		//anno impegno
		if(model.getImpegno().getAnnoImpegno()!=null){
			prop.setAnnoImpegno(model.getImpegno().getAnnoImpegno());
		}
		
		//numero impegno
		if(model.getImpegno().getNumeroImpegno()!=null){
			prop.setNumeroImpegno(BigDecimal.valueOf(model.getImpegno().getNumeroImpegno()));

			//numero sub
			if(model.getImpegno().getNumeroSub()!=null){
				prop.setNumeroSubImpegno(BigDecimal.valueOf(model.getImpegno().getNumeroSub()));
			}

				}
		
		//Soggetto
		if(model.getSoggetto() != null && (!StringUtils.isEmpty(model.getSoggetto().getCodCreditore()))){
			prop.setCodiceCreditore(model.getSoggetto().getCodCreditore());
		}
		
		//Soggetto creditore cessione incasso
		if(model.getSoggettoDue() != null && (!StringUtils.isEmpty(model.getSoggettoDue().getCodCreditore()))){
			prop.setCodiceCreditoreCessioneIncasso(model.getSoggettoDue().getCodCreditore());
		}
		
		//Gestisco Capitolo	//jira 1327 - ricerca capitolo per 0
		if(model.getCapitolo() != null){
			
			//anno capitolo
			if(model.getCapitolo().getAnno() != null){
				prop.setAnnoCapitolo(model.getCapitolo().getAnno());
			}
			
			//numero capitolo
			if(model.getCapitolo().getNumCapitolo() != null){
				prop.setNumeroCapitolo(new BigDecimal(model.getCapitolo().getNumCapitolo()));
			}
			
			//articolo
			if(model.getCapitolo().getArticolo() != null){
				prop.setNumeroArticolo(new BigDecimal(model.getCapitolo().getArticolo()));
			}
			
			//ueb
			if(model.getCapitolo().getUeb() != null){
				prop.setNumeroUEB(model.getCapitolo().getUeb().intValue());
			}				
			
		}
		
		// Imposto il Provvedimento
		if(model.getProvvedimento() != null){
			
			if(model.getProvvedimento().getUid() != null && model.getProvvedimento().getUid() != 0){
				
				prop.setUidProvvedimento(model.getProvvedimento().getUid());
				
			} else {
				
				if(model.getProvvedimento().getAnnoProvvedimento() != null && model.getProvvedimento().getAnnoProvvedimento() != 0){
					if(model.getProvvedimento().getAnnoProvvedimento().toString().length() == 4){
						prop.setAnnoProvvedimento(model.getProvvedimento().getAnnoProvvedimento());
					}
				}
				
				if(model.getProvvedimento().getNumeroProvvedimento() != null && model.getProvvedimento().getNumeroProvvedimento().intValue() != 0){
					prop.setNumeroProvvedimento(new BigDecimal(model.getProvvedimento().getNumeroProvvedimento().intValue()));
				}
				
				if(model.getProvvedimento().getIdTipoProvvedimento() != null && model.getProvvedimento().getIdTipoProvvedimento() != 0){
					prop.setCodiceTipoProvvedimento(String.valueOf(model.getProvvedimento().getIdTipoProvvedimento()));
				}	
				
				if(model.getProvvedimento().getUidStrutturaAmm()!=null){
					prop.setUidStrutturaAmministrativoContabile(model.getProvvedimento().getUidStrutturaAmm());
				}
				
				
			}
		}
						
		ricercaOrdinativo.setParametroRicercaOrdinativoPagamento(prop);
		addNumAndPageSize(ricercaOrdinativo, "ricercaOrdinativoPagamentoID");
		
		return ricercaOrdinativo;
	}
	
	/**
	 * convertitore utilizzaro per la ricerca ordinato incasso
	 * @return
	 */
	protected RicercaOrdinativo convertiModelPerChiamataServizioRicercaOrdinativoIncasso() {
		ParametroRicercaOrdinativoIncasso proi = new ParametroRicercaOrdinativoIncasso();
		RicercaOrdinativo ricercaOrdinativo = new RicercaOrdinativo();
		
		//Ente
		ricercaOrdinativo.setRichiedente(sessionHandler.getRichiedente());
		ricercaOrdinativo.setEnte(sessionHandler.getAccount().getEnte());
		
		//Anno Esercizio
		proi.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
		
		//Ordinativo incasso
		if(model.getNumeroOrdinativoDa() != null){
			proi.setNumeroOrdinativoDa(model.getNumeroOrdinativoDa());
		}
		if(model.getNumeroOrdinativoA() != null){
			proi.setNumeroOrdinativoA(model.getNumeroOrdinativoA());
		}
		
		if(StringUtils.isNotEmpty(model.getDataEmissioneDa())){
			proi.setDataEmissioneDa(DateUtility.parse(model.getDataEmissioneDa()));
		}		
		if(StringUtils.isNotEmpty(model.getDataEmissioneA())){
			proi.setDataEmissioneA(DateUtility.parse(model.getDataEmissioneA()));
		}

		if(StringUtils.isNotEmpty(model.getStatoOperativo())){
			proi.setStatoOperativo(model.getStatoOperativo());
		}
		
		//COPIAMO DEL CAMPO HIDDEN PER VIA DEL CHECKBOX CHE PERDE IL VALORE:
		model.setEscludiAnnullati(model.getHiddenPerEscludiAnnullati());
		if(model.getEscludiAnnullati()!=null && model.getEscludiAnnullati()==true){
			List<String> statiDaEscludere = new ArrayList<String>();
			statiDaEscludere.add("A");
			proi.setStatiDaEscludere(statiDaEscludere );
		}
		//
		
		if(StringUtils.isNotEmpty(model.getStatoOperativo()) && "A".equals(model.getStatoOperativo())){
			//In caso di annullato vince su escludi
			if(model.getEscludiAnnullati()!=null && model.getEscludiAnnullati()==true){
				proi.setStatiDaEscludere(null);
			}
		}

		if(StringUtils.isNotEmpty(model.getDistinta())){
			proi.setCodiceDistinta(model.getDistinta());
		}
		
		//conto tesoriere
		if(StringUtils.isNotEmpty(model.getContoTesoriere())){
			proi.setContoDelTesoriere(model.getContoTesoriere());
		}

		if(StringUtils.isNotEmpty(model.getDataTrasmissioneOIL())){
			proi.setDataTrasmissioneOIL(DateUtility.parse(model.getDataTrasmissioneOIL()));
		}
		
		//DESCRIZIONE:
		if(model.getDescrizioneOrdinativo() != null){
			proi.setDescrizione(model.getDescrizioneOrdinativo());
		}
		
		if(model.getDaTrasmettere() != null){
			proi.setDaTrasmettere(model.getDaTrasmettere());
		}
		
		
		//CAUSALE:
		CausaleEntrataTendinoModel causale = model.getCausaleEntrataTendino();
		if(causale!=null && !StringUtils.isEmpty(causale.getCodiceCausale())){
			proi.setCodiceCausale(causale.getCodiceCausale());
		}
		
		//Soggetto
		if(model.getSoggetto() != null && (!StringUtils.isEmpty(model.getSoggetto().getCodCreditore()))){
			proi.setCodiceCreditore(model.getSoggetto().getCodCreditore());
		}
		
		//Accertamento
		if(model.getAnnoAccertamento()!=null){
			proi.setAnnoMovimento(model.getAnnoAccertamento());
		}
		if(model.getNumeroAccertamento()!=null){
			proi.setNumeroMovimento(model.getNumeroAccertamento());

			if(model.getNumeroSubAccertamento()!=null){
				proi.setNumeroSubMovimento(model.getNumeroSubAccertamento());
			}
		}
		
		
		//Gestisco Capitolo	//jira 1328 - ricerca capitolo per 0
		if(model.getCapitolo() != null){
			if(model.getCapitolo().getAnno() != null){
				proi.setAnnoCapitolo(model.getCapitolo().getAnno());
			}
			if(model.getCapitolo().getNumCapitolo() != null){
				proi.setNumeroCapitolo(new BigDecimal(model.getCapitolo().getNumCapitolo()));
			}			
			if(model.getCapitolo().getArticolo() != null){
				proi.setNumeroArticolo(new BigDecimal(model.getCapitolo().getArticolo()));
			}
			if(model.getCapitolo().getUeb() != null){
				proi.setNumeroUEB(model.getCapitolo().getUeb().intValue());
			}				
			
		}
		
		// Imposto il Provvedimento
		if(model.getProvvedimento() != null){
			
			if(model.getProvvedimento().getUid() != null && model.getProvvedimento().getUid() != 0){
				
				proi.setUidProvvedimento(model.getProvvedimento().getUid());
				
			} else {
				
				if(model.getProvvedimento().getAnnoProvvedimento() != null && model.getProvvedimento().getAnnoProvvedimento() != 0){
					if(model.getProvvedimento().getAnnoProvvedimento().toString().length() == 4){
						proi.setAnnoProvvedimento(model.getProvvedimento().getAnnoProvvedimento());
					}
				}
				
				if(model.getProvvedimento().getNumeroProvvedimento() != null && model.getProvvedimento().getNumeroProvvedimento().intValue() != 0){
					proi.setNumeroProvvedimento(new BigDecimal(model.getProvvedimento().getNumeroProvvedimento().intValue()));
				}
				
				if(model.getProvvedimento().getIdTipoProvvedimento() != null && model.getProvvedimento().getIdTipoProvvedimento() != 0){
					proi.setCodiceTipoProvvedimento(String.valueOf(model.getProvvedimento().getIdTipoProvvedimento()));
				}	
				
				if(model.getProvvedimento().getUidStrutturaAmm()!=null){
					proi.setUidStrutturaAmministrativoContabile(model.getProvvedimento().getUidStrutturaAmm());
				}
				
				
			}
		}
		
		ricercaOrdinativo.setParametroRicercaOrdinativoIncasso(proi);
		addNumAndPageSize(ricercaOrdinativo, "ricercaOrdinativoIncassoID");
		
		return ricercaOrdinativo;
	}
	
	public String annullaRicercaOrdinativi(){
		// pulisce i campi di ricerca
		pulisciCampi();
		
		String returnValue="goToRicercaOrdinativoPagamento";
		
		if (oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_INCASSO)) {
			returnValue="goToRicercaOrdinativoIncasso";
		}

		return returnValue;
	}
	
	//Metodo per pulire il model (annulla)
	protected void pulisciCampi(){
	
		model.setProvvedimento(new ProvvedimentoImpegnoModel());
		model.setSoggetto(new SoggettoImpegnoModel());
		model.setCapitolo(new CapitoloImpegnoModel());
		model.setImpegno(new ImpegnoLiquidazioneModel());

		model.setNumeroOrdinativoA(null);
		model.setNumeroOrdinativoDa(null);
		model.setDataEmissioneA(null);
		model.setDataEmissioneDa(null);
		model.setDistinta(null);
		model.setContoTesoriere(null);
		model.setStatoOperativo(null);
		model.setDataTrasmissioneOIL(null);
		
		model.setNumeroLiquidazione(null);
		model.setNumeroLiquidazioneString(null);
		model.setAnnoLiquidazione(null);
		
		model.setCapitoloSelezionato(false);
		model.setHasImpegnoSelezionatoXPopup(false);
		model.setProvvedimentoSelezionato(false);
		model.setSoggettoSelezionato(false);
		
		model.setAnnoAccertamento(null);
		model.setNumeroAccertamento(null);
		model.setNumeroSubAccertamento(null);
		
		model.setEscludiAnnullati(false);
		model.setHiddenPerEscludiAnnullati(false);
		
	}
	
	protected String executeRicercaOrdinativoPagamento() {
	
		RicercaOrdinativoResponse response = ordinativoService.ricercaOrdinativoPagamento(convertiModelPerChiamataServizioRicercaOrdinativo());
		
		if(response.isFallimento()){
			
			if (response.getErrori()!=null && response.getErrori().size()>0) {
				for (Errore errore : response.getErrori()) {
					addPersistentActionError(errore.getCodice()+" "+errore.getDescrizione());
				}
			}
			return INPUT;
		}		
		
		model.setElencoOrdinativoPagamento(response.getElencoOrdinativoPagamento());
		model.setResultSize(response.getNumRisultati());
		
		model.setTotImporti(response.getTotImporti());
		
		
		if (model.getResultSize()>0) {
			for (OrdinativoPagamento ordinativoPagamentoCorr : model.getElencoOrdinativoPagamento()) {
				ordinativoPagamentoCorr.setCodStatoOperativoOrdinativo(CostantiFin.statoOperativoOrdinativoEnumToString(ordinativoPagamentoCorr.getStatoOperativoOrdinativo()));
			}
		}
		
		return "gotoElencoOrdinativoPagamento";
		    
	}
	
	protected String executeRicercaOrdinativoIncasso() {
		
		RicercaOrdinativoResponse response = ordinativoService.ricercaOrdinativoIncasso(convertiModelPerChiamataServizioRicercaOrdinativoIncasso());
		
		if(response.isFallimento()){
	
			if (response.getErrori()!=null && response.getErrori().size()>0) {
				for (Errore errore : response.getErrori()) {
					addPersistentActionError(errore.getCodice()+" "+errore.getDescrizione());
				}
			}
			return INPUT;
		}		
		
		model.setElencoOrdinativoIncasso(response.getElencoOrdinativoIncasso());
		model.setResultSize(response.getNumRisultati());
		
		model.setTotImporti(response.getTotImporti());
		
		if (model.getResultSize()>0) {  
			for (OrdinativoIncasso ordinativoIncassoCorr : model.getElencoOrdinativoIncasso()) {
				ordinativoIncassoCorr.setCodStatoOperativoOrdinativo(CostantiFin.statoOperativoOrdinativoEnumToString(ordinativoIncassoCorr.getStatoOperativoOrdinativo()));
			}
		}
		
		return "gotoElencoOrdinativoIncasso";
		    
	}
	
	protected List<Errore> checkParametri(){
		List<Errore> listaErrori = new ArrayList<Errore>();
		boolean noInputData=true;
		
		//Condizioni ricerca - Ordinativo
		if (model.getNumeroOrdinativoDa()!=null || model.getNumeroOrdinativoA()!=null) {
			noInputData=false;
		}

		if (!StringUtils.isEmpty(model.getDataEmissioneDa()) || !StringUtils.isEmpty(model.getDataEmissioneA())) {
			noInputData=false;
		}
		
		if (!StringUtils.isEmpty(model.getDistinta()) || !StringUtils.isEmpty(model.getDataTrasmissioneOIL())) {
			noInputData=false;
		}
		
		// 	SIAC-5831 il parametro di ricerca conto tesoriere e' ora anche per incasso 
		//(tolto if che controllava che fosse di pagamento)
		if (!StringUtils.isEmpty(model.getContoTesoriere())) {
			noInputData=false;
		}

		//LO STATO SIA PER INCASSO CHE PER PAGAMENTO:
		if (!StringUtils.isEmpty(model.getStatoOperativo())) {
			noInputData=false;
		}
		
		//COPIAMO DEL CAMPO HIDDEN PER VIA DEL CHECKBOX CHE PERDE IL VALORE:
		model.setEscludiAnnullati(model.getHiddenPerEscludiAnnullati());
		if(model.getEscludiAnnullati()!=null && model.getEscludiAnnullati()==true){
			noInputData=false;
		}
		//
		
		
		//DESCRIZIONE
		if (!StringUtils.isEmpty(model.getDescrizioneOrdinativo())) {
			noInputData=false;
		}
		
		if (model.getDaTrasmettere() != null) {
			noInputData=false;	
		}
		
		
		//CAUSALE:
		if (oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_INCASSO)) {
			CausaleEntrataTendinoModel causale = model.getCausaleEntrataTendino();
			if(causale!=null && !StringUtils.isEmpty(causale.getCodiceCausale())){
				noInputData=false;
			}
		}

		//Condizioni ricerca - Da-A numOrd
		if (model.getNumeroOrdinativoDa()!=null && model.getNumeroOrdinativoA()!=null) {
			if (model.getNumeroOrdinativoDa().compareTo(model.getNumeroOrdinativoA())==1) {
				listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Numero Ordinativo Da/Numero Ordinativo A","(Numero Ordinativo Da deve essere minore di Numero Ordinativo A)"));
			}
		}
		
		if (model.getNumeroOrdinativoDa()==null && model.getNumeroOrdinativoA()!=null) {
			listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Numero Ordinativo Da/Numero Ordinativo A","(Non e' possibile inserire Numero Ordinativo A senza Numero Ordinativo Da)"));
		}
		
		//Condizioni ricerca - Da-A dataOrd
		if (!StringUtils.isEmpty(model.getDataEmissioneDa()) || !StringUtils.isEmpty(model.getDataEmissioneA())) {

			DateFormat df=null;
			Date parsedTimeDa=null;
			Date parsedTimeA=null;

			if (!StringUtils.isEmpty(model.getDataEmissioneDa())) {
				if (model.getDataEmissioneDa().length() == 8) {
					df = new SimpleDateFormat("dd/MM/yy");
				} else {
					df = new SimpleDateFormat("dd/MM/yyyy");
				}
				
				try {
					parsedTimeDa=df.parse(model.getDataEmissioneDa());
				} catch (ParseException e) {
					listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Data Emissione Da",""));				}
			}

			if (!StringUtils.isEmpty(model.getDataEmissioneA())) {
				if (model.getDataEmissioneA().length() == 8) {
					df = new SimpleDateFormat("dd/MM/yy");
				} else {
					df = new SimpleDateFormat("dd/MM/yyyy");
				}
				
				try {
					parsedTimeA=df.parse(model.getDataEmissioneA());
				} catch (ParseException e) {
					listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Data Emissione A",""));				
				}
			}

			if (parsedTimeDa==null && parsedTimeA!=null) {
				listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Data Emissione Da/Data Emissione A","(Non e' possibile inserire Data Emissione A senza Data Emissione Da)"));
			}
			
			if (parsedTimeDa!=null && parsedTimeA!=null && parsedTimeDa.after(parsedTimeA)) {
				listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Data Emissione Da/Data Emissione A","(Data Emissione Da deve essere minore di Data Emissione A)"));
			}

		}
		

		
		//Condizioni ricerca - Liquidazioni
		if (oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO)) {
			model.setNumeroLiquidazione(null);
			if (!StringUtils.isEmpty(model.getNumeroLiquidazioneString())) {
				model.setNumeroLiquidazione(new BigInteger(model.getNumeroLiquidazioneString()));
			}

			if(model.getNumeroLiquidazione()!=null){
				noInputData=false;
				if(model.getAnnoLiquidazione()==null){
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Liquidazione obbligatorio"));
				}
			}
			if(model.getAnnoLiquidazione()!=null){
				noInputData=false;
				if(model.getNumeroLiquidazione()==null){
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Liquidazione obbligatorio"));
				}

				if (!(model.getAnnoLiquidazione().compareTo(Integer.valueOf(1900))==1)) {
					listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Anno Liquidazione","(deve essere maggiore di 1900)"));				
				}
			}
		}
		
		//Condizioni ricerca - Impegni
		if (oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO)) {
			if(model.getImpegno().getNumeroImpegno()!=null){
				noInputData=false;
				if(model.getImpegno().getAnnoImpegno()==null){
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Impegno obbligatorio"));
				}
			}
			if(model.getImpegno().getAnnoImpegno()!=null){
				noInputData=false;
				if(model.getImpegno().getNumeroImpegno()==null){
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Impegno obbligatorio"));
				}

				if (!(model.getImpegno().getAnnoImpegno().compareTo(Integer.valueOf(1900))==1)) {
					listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Anno Impegno","(deve essere maggiore di 1900)"));				
				}
			}
		}
		
		//Condizioni ricerca - Accertamento
		if (oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_INCASSO)) {
			if(model.getNumeroAccertamento()!=null){
				noInputData=false;
				if(model.getAnnoAccertamento()==null){
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno e Numero Accertamento devono essere entrambi valorizzati."));
				}
			}
			if(model.getAnnoAccertamento()!=null){
				noInputData=false;
				if(model.getNumeroAccertamento()==null){
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno e Numero Accertamento devono essere entrambi valorizzati."));
				}

				if (!(model.getAnnoAccertamento().compareTo(Integer.valueOf(1900))==1)) {
					listaErrori.add(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Anno Accertamento","(deve essere maggiore di 1900)"));				
				}
			}
		}

		//Condizioni ricerca - Capitolo
		if (model.getCapitolo().getNumCapitolo()!=null || model.getCapitolo().getArticolo()!=null || model.getCapitolo().getUeb()!=null) {
			noInputData=false;
		}

		if (model.getCapitolo().getNumCapitolo()==null){
			if (model.getCapitolo().getArticolo()!=null || model.getCapitolo().getUeb()!=null) {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Capitolo obbligatorio"));
			}
		}
				
		if (model.getCapitolo().getArticolo()==null){
			if (model.getCapitolo().getNumCapitolo()!=null || model.getCapitolo().getUeb()!=null) {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Articolo Capitolo obbligatorio"));
			}
		}
		
		//Condizioni ricerca nuova adeguata alle altre (vedi impegno/accertamento) - Provvedimento
		if(model.getProvvedimento().getAnnoProvvedimento()!=null && model.getProvvedimento().getAnnoProvvedimento().intValue()>0 ){
			noInputData=false;
			if(model.getProvvedimento().getNumeroProvvedimento()==null && 
					model.getProvvedimento().getIdTipoProvvedimento()==null){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero o Tipo Provvedimento obbligatorio con Anno Provvedimento"));
			}
		}

		if(model.getProvvedimento().getNumeroProvvedimento()!=null || 
				model.getProvvedimento().getIdTipoProvvedimento()!=null){
			noInputData=false;
			if(model.getProvvedimento().getAnnoProvvedimento()==null || model.getProvvedimento().getAnnoProvvedimento().intValue()==0 ){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Provvedimento obbligatorio con Numero o Tipo Provvedimento"));
			}
		}
		
		//Jira 1907/1908, ricerco impegni anche x uid sac provvedimento
		if(!StringUtils.isEmpty(strutturaSelezionataSuPagina)){
			noInputData=false;
			model.getProvvedimento().setUidStrutturaAmm(Integer.parseInt(strutturaSelezionataSuPagina));
		}
		
		
		//Condizioni ricerca - Soggetto
		if (!StringUtils.isEmpty(model.getSoggetto().getCodCreditore())) {
			noInputData=false;
		}
		
		//Condizioni ricerca - Soggetto cessione incasso
		if (!StringUtils.isEmpty(model.getSoggettoDue().getCodCreditore())) {
			noInputData=false;
		}

		if (noInputData) {
			listaErrori.add(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore(""));
		}
		
		return listaErrori;
	}
	
	protected void caricaListeCombo() {

		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.DISTINTA, TipiLista.DISTINTA_ENTRATA, TipiLista.CONTO_TESORERIA, TipiLista.STATO_ORDINATIVO);
		
		if (oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO)) {
			List<CodificaFin> distinte = (List<CodificaFin>)mappaListe.get(TipiLista.DISTINTA)!=null && !((List<CodificaFin>)mappaListe.get(TipiLista.DISTINTA)).isEmpty() ? (List<CodificaFin>)mappaListe.get(TipiLista.DISTINTA) : new ArrayList<CodificaFin>();
			model.setListaDistinta(distinte);
		}
		
		if (oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_INCASSO)) {
			model.setListaDistinta(((List<CodificaFin>)mappaListe.get(TipiLista.DISTINTA_ENTRATA)));
		}
		
		//il conto tesoriere e' per tutti:
		List<CodificaFin> cctesoriere = (List<CodificaFin>)mappaListe.get(TipiLista.CONTO_TESORERIA)!=null && !((List<CodificaFin>)mappaListe.get(TipiLista.CONTO_TESORERIA)).isEmpty() ? (List<CodificaFin>)mappaListe.get(TipiLista.CONTO_TESORERIA) : new ArrayList<CodificaFin>();
		model.setListaContoTesoriere(cctesoriere);
		
		//LO STATO PER TUTTI:
		model.setListaStatoOperativo(((List<CodificaFin>)mappaListe.get(TipiLista.STATO_ORDINATIVO)));
	}
	
	//Metodi per la profilazione degli ordinativi
	public boolean isAbilitatoRicMan(){
		return isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_RICMAN);
	}

	@Override
	public String listaClasseSoggettoChanged() {
		model.getSoggetto().setCodCreditore(null);
		model.setSoggettoSelezionato(false);
		return "headerSoggetto";	
	}
	
	@Override
	public String listaClasseSoggettoDueChanged() {
		model.getSoggettoDue().setCodCreditore(null);
		model.setSoggettoSelezionatoDue(false);
		return "headerSoggetto";	
	}

	@Override
	protected void setCapitoloSelezionato(CapitoloImpegnoModel supportCapitolo) {
		model.setCapitolo(supportCapitolo);
		model.setCapitoloSelezionato(true);
		
		model.setDatoPerVisualizza(supportCapitolo);
		model.setListaRicercaCapitolo(null);
		model.setCapitoloTrovato(false);
	}

	@Override
	protected void setErroreCapitolo() {
		StringBuilder supportMessaggio = new StringBuilder(); 
		supportMessaggio.append(model.getCapitolo().getAnno());
		supportMessaggio.append((model.getCapitolo().getNumCapitolo() != null) ? "/" + model.getCapitolo().getNumCapitolo() : "");
		supportMessaggio.append((model.getCapitolo().getArticolo() != null) ? "/" + model.getCapitolo().getArticolo() : "");
		supportMessaggio.append((model.getCapitolo().getUeb() != null) ? "/" + model.getCapitolo().getUeb() : "");
		addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo", supportMessaggio.toString()));
	}

	@Override
	protected void setSoggettoSelezionato(SoggettoImpegnoModel soggettoImpegnoModel) {
		model.setSoggetto(soggettoImpegnoModel);
		model.setSoggettoSelezionato(true);
	}
	
	@Override
	protected void setSoggettoSelezionatoDue(SoggettoImpegnoModel soggettoImpegnoModel) {
		model.setSoggettoDue(soggettoImpegnoModel);
		model.setSoggettoSelezionatoDue(true);
	}

	@Override
	protected void setProvvedimentoSelezionato(ProvvedimentoImpegnoModel currentProvvedimento) {
		model.setProvvedimento(currentProvvedimento);
		model.setProvvedimentoSelezionato(true);
	}

	@Override
	public String confermaCompGuidata(){
		if(model.isHasImpegnoSelezionatoPopup()==true){
			model.getImpegno().setNumeroImpegno(model.getnImpegno().intValue());
			model.getImpegno().setAnnoImpegno(model.getnAnno());
			if(model.getnSubImpegno()!=null){
				model.getImpegno().setNumeroSub(model.getnSubImpegno().intValue());
			} else { 
				model.getImpegno().setNumeroSub(null);
			}	
			model.setnAnno(null);
			model.setnImpegno(null);
		}
		pulisciListeeSchedaPopup();
		return INPUT;
	}

	public boolean isOrdinativoIncasso(){
		boolean returnValue=false;

		if (oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_INCASSO)) {
			returnValue=true;
		}

		return returnValue;
	}

	public boolean checkSoggettoStato(){
		if(model.isSoggettoSelezionato()){
			return true;
		}else{
			return false;
		}
	}
	public boolean checkProvvedimentoStato(){
		return false;
	}
	
	@Override
	public boolean missioneProgrammaAttivi() {
		//  Auto-generated method stub
		return false;
	}

	@Override
	public boolean cofogAttivo() {
		//  Auto-generated method stub
		return false;
	}

	@Override
	public boolean cupAttivo() {
		//  Auto-generated method stub
		return false;
	}

	@Override
	public boolean programmaPoliticoRegionaleUnitarioAttivo() {
		//  Auto-generated method stub
		return false;
	}

	@Override
	public boolean transazioneElementareAttiva() {
		//  Auto-generated method stub
		return false;
	}

	@Override
	public boolean altriClassificatoriAttivi() {
		//  Auto-generated method stub
		return false;
	}

	@Override
	public String confermaPdc() {
		//  Auto-generated method stub
		return null;
	}


	@Override
	public String confermaSiope() {
		//  Auto-generated method stub
		return null;
	}

	@Override
	public boolean datiUscitaImpegno() {
		//  Auto-generated method stub
		return true;
	}

}
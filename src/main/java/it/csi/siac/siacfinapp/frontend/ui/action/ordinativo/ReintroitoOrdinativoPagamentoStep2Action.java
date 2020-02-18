/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.customdto.EsitoControlliDto;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.RigaDiReintroitoOrdinativoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaSoggettiDellaClasse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaSoggettiDellaClasseResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ReintroitoOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.MovimentoKey;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ReintroitoOrdinativoPagamentoStep2Action extends ActionKeyReintroitoOrdinativoPagamentoAction{

	private static final long serialVersionUID = 1L;
	
	
	private static final String IMPEGNO_NETTO = "cgNettoImp";
	private static final String IMPEGNO_RITENUTE = "cgRitenuteImp";
	private static final String ACCERTAMENTO_RITENUTE = "cgRitenuteAcc";
	
	
	public ReintroitoOrdinativoPagamentoStep2Action () {
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
		this.model.setTitolo("Gestione Reintroiti - Compilazione dati");
	    
	}
	
	/**
	 * metodo execute della action
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * Scatta al change dei campi editabili nelle varie righe 
	 * degli ordinativi di incasso
	 */
	public void saveOnChangeElementoRigaOrdinaivi(){
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String nuovoValore = request.getParameter("value");
		String name = request.getParameter("name");
		
		String nomeCampoCoinvolto = nomeCampoCoinvolto(name);
		int indiceCampoCoinvolto = indiceCampoCoinvolto(name);
		
		if(nomeCampoCoinvolto!=null && indiceCampoCoinvolto>-1){
			aggiornaValoreElementoRigaOrdinativi(nomeCampoCoinvolto,indiceCampoCoinvolto,nuovoValore);
		}
	}
	
	/**
	 * Aggiorna il valore del campo che e' stato modificato in una riga degli ordinativi di incasso
	 * @param nomeCampoCoinvolto
	 * @param indiceCampoCoinvolto
	 * @param nuovoValore
	 */
	private void aggiornaValoreElementoRigaOrdinativi(String nomeCampoCoinvolto, int indiceCampoCoinvolto,String nuovoValore) {
		if("descrizione".equalsIgnoreCase(nomeCampoCoinvolto)){
			model.getReintroitoOrdinativoStep2Model().getListOrdInc().get(indiceCampoCoinvolto).setDescrizione(nuovoValore);
		} else if("importo".equalsIgnoreCase(nomeCampoCoinvolto)){
			BigDecimal nuovoValoreBb = new BigDecimal(nuovoValore);
			model.getReintroitoOrdinativoStep2Model().getListOrdInc().get(indiceCampoCoinvolto).setImporto(nuovoValoreBb );
		}
	}

	/**
	 * Dato il name di un elemento appena modificato individua il suo indice
	 * @param name
	 * @return
	 */
	private int indiceCampoCoinvolto(String name){
		return FinStringUtils.substringTraQuadreInt(name);
	}
	
	/**
	 * Dato il  name di un elemento appena modificato individua il campo a cui fa riferimento 
	 * all'interno dell'elemento che corrisponde al campo appena editato
	 * @param name
	 * @return
	 */
	private String nomeCampoCoinvolto(String name){
		String nomeCampoCoinvolto = null;
		if(name!=null){
			if(name.contains("descrizione")){
				nomeCampoCoinvolto = "descrizione";
			} else if(name.contains("importo")){
				nomeCampoCoinvolto = "importo";
			}
		}
		return nomeCampoCoinvolto;
	}
	
	/**
	 * Controlli da effettuare alla pressione del tasto prosegui 
	 * verso lo step 3
	 * 
	 * Aggiunge gli errori in sessione e ritorna:
	 * true se tutto ok
	 * false se trovati errori
	 * 
	 * @return
	 */
	private boolean controlliPerProsegui(){
		
		List<Errore> listaErrori= new ArrayList<Errore>();
		
		//CONTROLLI DI OBBLIGATORIETA:
		listaErrori = controlliDatiObbligatoriCompilati(listaErrori);
		
		if(isEmpty(listaErrori)){
			//se qui non ci sono errori i dati minimi sono compilati, procedo con controlli piu' approfonditi:
			//Coerenza soggetti:
			EsitoControlliDto controlliSogg = controlliCoerenzaSoggetti();
			if(controlliSogg.presenzaErrori()){
				listaErrori = FinUtility.addAll(listaErrori, controlliSogg.getListaErrori());
			}
			//
		}
		
		if(isEmpty(listaErrori)){
			//se non ho errori nemmeno tra i soggetti eseguo la coerenza movimenti:
			listaErrori =  controlliDisponibilitaLiquidareImpegni(listaErrori);
			listaErrori =  controlliDisponibilitaDiCassaImpegni(listaErrori);
		}
		
		return checkAndAddErrors(listaErrori);
	}
	
	/**
	 * Effettaua i controllo di obbligatorieta per i dati dello step 2
	 * @param listaErrori
	 * @return
	 */
	private List<Errore> controlliDatiObbligatoriCompilati(List<Errore> listaErrori){
		//IMPEGNO DI DESTINAZIONE (DA RIGA NETTO):
		Impegno impegnoDest = model.getReintroitoOrdinativoStep2Model().getNetto().getImpegnoDiDestinazione();
		if(FinUtility.isEmpty(impegnoDest)){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Indicare impegno destinazione (riga netto)"));
		}
		
		if(ciSonoRitenute()){
			//TUTTI GLI IMPEGNI DELLE RIGHE:
			boolean tuttiImpCompilati = compilatiTuttiMovimenti(model.getReintroitoOrdinativoStep2Model().getListOrdInc(),true);
			if(!tuttiImpCompilati){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Compilare tutti gli impegni"));
			}
			
			//TUTTI GLI ACCERTAMENTI DELLE RIGHE:
			boolean tuttiAccCompilati = compilatiTuttiMovimenti(model.getReintroitoOrdinativoStep2Model().getListOrdInc(),false);
			if(!tuttiAccCompilati){
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Compilare tutti gli accertamenti"));
			}
			//
		}
		return listaErrori;
	}
	
	/**
	 * Controlla la coerenza dei soggetti
	 * @return
	 */
	private EsitoControlliDto controlliCoerenzaSoggetti() {
		
		EsitoControlliDto controlliSogg = new EsitoControlliDto();
		
		//per evitare di avere lo stesso messaggio di warning o errore replicato:
		List<Integer> idImpegniGiaControllati = new ArrayList<Integer>();
		List<Integer> idAccertamentiGiaControllati = new ArrayList<Integer>();
		//
		
		//1. controlliamo la coerenza dell'impegno principale:
		OrdinativoPagamento ordPag = model.getReintroitoOrdinativoStep1Model().getOrdinativoDaReintroitare();
		Impegno impegnoDest = model.getReintroitoOrdinativoStep2Model().getNetto().getImpegnoDiDestinazione();
		SubImpegno subImpegnoDest = model.getReintroitoOrdinativoStep2Model().getNetto().getSubImpegnoDiDestinazione();
		Integer numeroSub = model.getReintroitoOrdinativoStep2Model().getNetto().getNumeroSubImpegno();
		controlliSogg = controlloCoerenzaSoggetto(impegnoDest,numeroSub,subImpegnoDest,ordPag,controlliSogg,idImpegniGiaControllati);
		
		//2. controlliamo la coerenza degli impegni e accertamenti delle ritenute:
		List<RigaDiReintroitoOrdinativoModel> ritenuteSplit = model.getReintroitoOrdinativoStep2Model().getListOrdInc();
		if(!FinStringUtils.isEmpty(ritenuteSplit)){
			for(RigaDiReintroitoOrdinativoModel ritIt: ritenuteSplit){
				//CONTROLLIAMO IL SOGGETTO DELL'IMPEGNO
				controlliSogg = controlloCoerenzaSoggetto(ritIt.getImpegnoDiDestinazione(),ritIt.getNumeroSubImpegno(),ritIt.getSubImpegnoDiDestinazione(), ordPag,controlliSogg,idImpegniGiaControllati);
				//CONTROLLIAMO IL SOGGETTO DELL'ACCERTAMENTO
				controlliSogg = controlloCoerenzaSoggetto(ritIt.getAccertamentoDiDestinazione(),ritIt.getNumeroSubAccertamento(),ritIt.getSubAccertamentoDiDestinazione(),ritIt.getOrdInc(),controlliSogg,idAccertamentiGiaControllati);
			}
		}
		
		return controlliSogg;
	}
	
	private EsitoControlliDto controlliCoerenzaPianiDeiConti() {
		
		EsitoControlliDto esito = new EsitoControlliDto();
		
		//per evitare di avere lo stesso messaggio di warning replicato:
		List<Integer> idImpegniGiaControllati = new ArrayList<Integer>();
		List<Integer> idAccertamentiGiaControllati = new ArrayList<Integer>();
		//
		
		//IMPEGNO DI DESTINAZIONE (DA RIGA NETTO):
		esito = controlloPdc(idImpegniGiaControllati, model.getReintroitoOrdinativoStep2Model().getNetto().getImpegnoDiDestinazione(), esito);
		//
		
		//CONTROLLIAMO IMPEGNI E ACCERTAMENTI DELLE EVENTUALI RITENUTE:
		if(ciSonoRitenute()){
			
			for(RigaDiReintroitoOrdinativoModel it: model.getReintroitoOrdinativoStep2Model().getListOrdInc()){
			
				//impegno:
				esito = controlloPdc(idImpegniGiaControllati, it.getImpegnoDiDestinazione(), esito);
				
				//accertamento:
				esito = controlloPdc(idAccertamentiGiaControllati, it.getAccertamentoDiDestinazione(), esito);
				
			}
			
		}
		
		return esito;
	}
	
	private EsitoControlliDto controlloPdc(List<Integer> idImpegniGiaControllati, Impegno impegno, EsitoControlliDto esito){
		if(!idImpegniGiaControllati.contains(impegno.getUid())){
			Errore errorePdcImpIt = controlloPdc(impegno);
			if(errorePdcImpIt!=null){
				esito.addWarning(errorePdcImpIt);
			}
			idImpegniGiaControllati.add(impegno.getUid());
		}
		return esito;
	}
	
	private Errore controlloPdc(Impegno impegno){
		if(!Constanti.D_CLASS_TIPO_PIANO_DEI_CONTI_V.equalsIgnoreCase(impegno.getCodicePdc())
				|| !impegno.getCodPdc().equalsIgnoreCase("U.7.01.99.01.001")){
			MovimentoKey movKey = buildMovimentoKey(impegno, null);
			String nomeImp = buildNomeEntitaPerMessaggiDiErrore(movKey, true)  + " " +  buildCodiceEntitaPerMessaggiDiErrore(movKey);
			String errorePdc = " - Attenzione l'impegno selezionato non è in partita di giro, deve appartenere al piano dei conti: U.7.01.99.01.001";
			return ErroreCore.VALORE_NON_VALIDO.getErrore(nomeImp, errorePdc);
		}
		return null;
	}
	
	private EsitoControlliDto controlloPdc(List<Integer> idAccertamentiGiaControllati, Accertamento accertamento, EsitoControlliDto esito){
		if(!idAccertamentiGiaControllati.contains(accertamento.getUid())){
			Errore errorePdcImpIt = controlloPdc(accertamento);
			if(errorePdcImpIt!=null){
				esito.addWarning(errorePdcImpIt);
			}
			idAccertamentiGiaControllati.add(accertamento.getUid());
		}
		return esito;
	}
	
	private Errore controlloPdc(Accertamento accertamento){
		if(!Constanti.D_CLASS_TIPO_PIANO_DEI_CONTI_V.equalsIgnoreCase(accertamento.getCodicePdc())
				|| !accertamento.getCodPdc().equalsIgnoreCase("E.9.01.99.99.999")){
			MovimentoKey movKey = buildMovimentoKey(accertamento, null);
			String nomeAcc = buildNomeEntitaPerMessaggiDiErrore(movKey, false) + " " + buildCodiceEntitaPerMessaggiDiErrore(movKey);
			String errorePdc = " - Attenzione l'accertamento selezionato non è in partita di giro, deve appartenere al piano dei conti: E.9.01.99.99.999";
			return ErroreCore.VALORE_NON_VALIDO.getErrore(nomeAcc, errorePdc);
		}
		return null;
	}
	
	
	
	private EsitoControlliDto controlloCoerenzaSoggetto(Impegno impegno,Integer numeroSub,SubImpegno subImpegno, OrdinativoPagamento ordPag,
			EsitoControlliDto controlliSogg,List<Integer> idImpegniGiaControllati) {
		
		if(!idImpegniGiaControllati.contains(impegno.getUid())){
			
			
			if(!soggettoCoerente(impegno,subImpegno,ordPag)){
				String nomeEntita = buildNomeEntitaPerMessaggiDiErrore(buildMovimentoKey(impegno,numeroSub), true);
				String codiceEntita = buildCodiceEntitaPerMessaggiDiErrore(buildMovimentoKey(impegno,numeroSub));
				String msg = "Non ha un soggetto compatibile con l'ordinativo di pagamento.";
				controlliSogg.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore(nomeEntita + ": " + codiceEntita, msg));
			}
			if(!classeSoggettoCoerentePerReintroito(impegno,subImpegno,ordPag)){
				String nomeEntita = buildNomeEntitaPerMessaggiDiErrore(buildMovimentoKey(impegno,numeroSub), true);
				String codiceEntita = buildCodiceEntitaPerMessaggiDiErrore(buildMovimentoKey(impegno,numeroSub));
				String msg = "Non ha una classe coerente con il soggetto dell'ordinativo di pagamento.";
				controlliSogg.addWarning(ErroreCore.VALORE_NON_VALIDO.getErrore(nomeEntita + ": " + codiceEntita, msg));
			} 
			
			
			idImpegniGiaControllati.add(impegno.getUid());
		}
		
		return controlliSogg;
	}
	
	private EsitoControlliDto controlloCoerenzaSoggetto(Accertamento accertamento,Integer numeroSub,SubAccertamento subAccertamento, OrdinativoIncasso ordInc,
			EsitoControlliDto controlliSogg,List<Integer> idAccertamentiGiaControllati) {
		
		if(!idAccertamentiGiaControllati.contains(accertamento.getUid())){
			
			if(!soggettoCoerente(accertamento,subAccertamento,ordInc)){
				String nomeEntita = buildNomeEntitaPerMessaggiDiErrore(buildMovimentoKey(accertamento,numeroSub), false);
				String codiceEntita = buildCodiceEntitaPerMessaggiDiErrore(buildMovimentoKey(accertamento,numeroSub));
				String msg = "Non ha un soggetto compatibile con l'ordinativo di incasso.";
				controlliSogg.addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore(nomeEntita + ": " + codiceEntita, msg));
			}
			if(!classeSoggettoCoerentePerReintroito(accertamento,subAccertamento,ordInc)){
				String nomeEntita = buildNomeEntitaPerMessaggiDiErrore(buildMovimentoKey(accertamento,numeroSub), false);
				String codiceEntita = buildCodiceEntitaPerMessaggiDiErrore(buildMovimentoKey(accertamento,numeroSub));
				String msg = "Non ha una classe coerente con il soggetto dell'ordinativo di incasso.";
				controlliSogg.addWarning(ErroreCore.VALORE_NON_VALIDO.getErrore(nomeEntita + ": " + codiceEntita, msg));
			}
			
			idAccertamentiGiaControllati.add(accertamento.getUid());
		}
		
		return controlliSogg;
	}
	
	private boolean soggettoCoerente(Impegno impegno,SubImpegno subImpegno, OrdinativoPagamento ordPag) {
		Soggetto soggImp = soggettoImpOrSub(impegno, subImpegno);
		ClasseSoggetto classeSoggImp = impegno.getClasseSoggetto();
		Soggetto soggOrd = ordPag.getSoggetto();
		return soggettoCoerentePerReintroito(soggImp, classeSoggImp, soggOrd);
	}
	
	private boolean soggettoCoerente(Accertamento accertamento,SubAccertamento subAccertamento, OrdinativoIncasso ordinativoIncasso) {
		Soggetto soggAcc = soggettoAccOrSub(accertamento, subAccertamento);
		ClasseSoggetto classeSoggAcc = accertamento.getClasseSoggetto();
		Soggetto soggOrd = ordinativoIncasso.getSoggetto();
		return soggettoCoerentePerReintroito(soggAcc, classeSoggAcc, soggOrd);
	}
	
	private boolean classeSoggettoCoerentePerReintroito(Impegno impegno,SubImpegno subImpegno, OrdinativoPagamento ordPag) {
		ClasseSoggetto classeSoggImp = impegno.getClasseSoggetto();
		Soggetto soggOrd = ordPag.getSoggetto();
		return classeSoggettoCoerentePerReintroito(classeSoggImp, soggOrd);
	}
	
	private boolean classeSoggettoCoerentePerReintroito(Accertamento accertamento,SubAccertamento subAccertamento, OrdinativoIncasso ordinativoIncasso) {
		ClasseSoggetto classeSoggAcc = accertamento.getClasseSoggetto();
		Soggetto soggOrd = ordinativoIncasso.getSoggetto();
		return classeSoggettoCoerentePerReintroito(classeSoggAcc, soggOrd);
	}
	
	private Soggetto soggettoImpOrSub(Impegno impegno, SubImpegno subImpegno){
		if(subImpegno!=null){
			return subImpegno.getSoggetto();
		} else {
			return impegno.getSoggetto();
		}
	}
	
	private Soggetto soggettoAccOrSub(Accertamento accertamento, SubAccertamento subAccertamento){
		if(subAccertamento!=null){
			return subAccertamento.getSoggetto();
		} else {
			return accertamento.getSoggetto();
		}
	}
	
	private boolean soggettoCoerentePerReintroito(Soggetto soggMov,ClasseSoggetto classeSoggMov, Soggetto soggOrd) {
		
		//PREMESSA: gli ordinativi hanno sempre il soggetto
		//soggOrd non puo' essere nullo
		
		//1. PER STESSO SOGG OK
		if(soggMov!=null && !FinStringUtils.isEmpty(soggMov.getCodiceSoggetto())){
			//IL MOVIMENTO HA UN SOGGETTO
			if(FinStringUtils.sonoUguali(soggMov.getCodiceSoggetto() , soggOrd.getCodiceSoggetto())){
				//STESSO SOGGETTO: ritorno true
				 return true;
			} else {
				//SOGGETTI DIVERSI
				return false;
			}
		}
		
		//ritorno true perche' se non ha il soggetto il controllo non blocca
		return true;
	}
	
	private boolean classeSoggettoCoerentePerReintroito(ClasseSoggetto classeSoggMov, Soggetto soggOrd) {
		
		//PREMESSA: gli ordinativi hanno sempre il soggetto
		//soggOrd non puo' essere nullo
		
		if(classeSoggMov!=null && !FinStringUtils.isEmpty(classeSoggMov.getCodice())){
			String codClasse = classeSoggMov.getCodice();
		
			//carico i soggetti della classe del movimento:
			ListaSoggettiDellaClasseResponse resp = caricaSoggettiDellaClasse(codClasse);
			
			if(!isFallimento(resp)){
				
				List<CodificaFin> listaCodificheSoggetti = resp.getListaSoggetti();
				
				List<String> codiciSoggetti = FinUtility.listaCodici(listaCodificheSoggetti);
				
				if(FinStringUtils.isEmpty(codiciSoggetti)){
					//2. IL MOV HA UNA CLASSE PRIVA DI SOGGETTI OK
					return true;
				} else {
					//3. IL MOV HA UNA CLASSE CON SOGGETTI-> OK SE IL SOGG DELL'ORD E' TRA QUESTI
					String codSoggOrd = soggOrd.getCodiceSoggetto();
					if(FinStringUtils.contenutoIn(codSoggOrd, codiciSoggetti)){
						return true;
					} else {
						return false;
					}
				}
				
			}
		}
		
		//ritorno true perche' se non e' entrato nell'if sopra vuol dire che il controllo che 
		//fa fede e' gia' quello sul soggetto secco fatto in precendenza, e aggiungere questo warning
		//non avrebbe sigificato:
		return true;
	}
	
	/**
	 * Carica la lista di codifiche dei soggetti della classe indicata appoggiandosi
	 * al servizio listaSoggettiDellaClasse
	 * @param codClasse
	 * @return
	 */
	private ListaSoggettiDellaClasseResponse caricaSoggettiDellaClasse(String codClasse){
		ListaSoggettiDellaClasse requestSoggettiDellaClasse = new ListaSoggettiDellaClasse();
		requestSoggettiDellaClasse.setCodiceClasse(codClasse);
		requestSoggettiDellaClasse.setCodificaAmbito(Constanti.AMBITO_FIN);
		requestSoggettiDellaClasse.setDataOra(new Date());
		requestSoggettiDellaClasse.setEnte(sessionHandler.getEnte());
		requestSoggettiDellaClasse.setRichiedente(sessionHandler.getRichiedente());
		return soggettoService.listaSoggettiDellaClasse(requestSoggettiDellaClasse);
	}
	
	private List<Errore> controlliDisponibilitaDiCassaImpegni(List<Errore> listaErrori){
		//   Verifichiamo che il disponibile a pagare del capitolo dell'impegno di destinazione
		//   sia maggiore dell'importo netto + (l'eventuale) sommatoria degli ordinativi 
		//   di incasso delle rige di ritenute in cui e' (eventualmente) indicato tale impegno
		
		OrdinativoPagamento ordinativoPagamentoRicaricato = model.getReintroitoOrdinativoStep1Model().getOrdinativoDaReintroitare();
		
		Impegno impegnoDest = model.getReintroitoOrdinativoStep2Model().getNetto().getImpegnoDiDestinazione();
		Integer numSubDest = model.getReintroitoOrdinativoStep2Model().getNetto().getNumeroSubImpegno();
		
		BigDecimal importoNetto = calcolaImportoNettoPerReintroito(ordinativoPagamentoRicaricato);
		
		List<RigaDiReintroitoOrdinativoModel> listaRitenuteInfo = model.getReintroitoOrdinativoStep2Model().getListOrdInc();
		
		//CONTROLLIAMO IL DISP A PAGARE DEL CAPITOLO DELL'IMPEGNO DESTINZIONE:
		listaErrori = controlloDisponibilitaPagareCapitoloImpegno(listaErrori, impegnoDest, numSubDest, importoNetto);
		
		// CONTROLLIAMO I DISP A PAGARE DEI CAPITOLI DEGLI GLI ALTRI IMPEGNI (o sub) :
		MovimentoKey impegnoDestinazioneKey = buildMovimentoKey(impegnoDest,numSubDest);
		
		if(!FinStringUtils.isEmpty(listaRitenuteInfo)){
			for(RigaDiReintroitoOrdinativoModel ritenutaIt: listaRitenuteInfo){
				//DEVO ESCLUDERE QUELLO DESTINAZIONE IN QUANTO E' GIA' STATO VERIFICATO SOPRA:
				Impegno impegnoIt = ritenutaIt.getImpegnoDiDestinazione();
				Integer numSubIt = ritenutaIt.getNumeroSubImpegno();
				MovimentoKey keyIt = buildMovimentoKey(impegnoIt,numSubIt);
				if(!FinUtility.sonoUguali(keyIt, impegnoDestinazioneKey)){
					//qui bisogna passare importo netto a null:
					listaErrori = controlloDisponibilitaPagareCapitoloImpegno(listaErrori, impegnoIt, numSubIt, null);
				}
			}
		}
		
		return listaErrori;
	}
	
	
	private List<Errore> controlliDisponibilitaLiquidareImpegni(List<Errore> listaErrori){
		//   Verifichiamo che il disponibile a liquidare dell'impegno di destinazione
		//   sia maggiore dell'importo netto + (l'eventuale) sommatoria degli ordinativi 
		//   di incasso delle rige di ritenute in cui e' (eventualmente) indicato tale impegno
		
		OrdinativoPagamento ordinativoPagamentoRicaricato = model.getReintroitoOrdinativoStep1Model().getOrdinativoDaReintroitare();
		
		Impegno impegnoDest = model.getReintroitoOrdinativoStep2Model().getNetto().getImpegnoDiDestinazione();
		Integer numSubDest = model.getReintroitoOrdinativoStep2Model().getNetto().getNumeroSubImpegno();
		
		BigDecimal importoNetto = calcolaImportoNettoPerReintroito(ordinativoPagamentoRicaricato);
		
		List<RigaDiReintroitoOrdinativoModel> listaRitenuteInfo = model.getReintroitoOrdinativoStep2Model().getListOrdInc();
		
		//CONTROLLIAMO IL DISP A LIQUIDARE DELL'IMPEGNO DESTINZIONE:
		listaErrori = controlloDisponibilitaLiquidareImpegno(listaErrori, impegnoDest, numSubDest, importoNetto);
		
		// CONTROLLIAMO I DISP A LIQUIDARE DEGLI GLI ALTRI IMPEGNI (o sub) :
		MovimentoKey impegnoDestinazioneKey = buildMovimentoKey(impegnoDest,numSubDest);
		if(!FinStringUtils.isEmpty(listaRitenuteInfo)){
			for(RigaDiReintroitoOrdinativoModel ritenutaIt: listaRitenuteInfo){
				//DEVO ESCLUDERE QUELLO DESTINAZIONE IN QUANTO E' GIA' STATO VERIFICATO SOPRA:
				Impegno impegnoIt = ritenutaIt.getImpegnoDiDestinazione();
				Integer numSubIt = ritenutaIt.getNumeroSubImpegno();
				MovimentoKey keyIt = buildMovimentoKey(impegnoIt,numSubIt);
				if(!FinUtility.sonoUguali(keyIt, impegnoDestinazioneKey)){
					//qui bisogna passare importo netto a null:
					listaErrori = controlloDisponibilitaLiquidareImpegno(listaErrori, impegnoIt, numSubIt, null);
				}
			}
		}
		
		return listaErrori;
	}
	
	private List<Errore> controlloDisponibilitaLiquidareImpegno(List<Errore> listaErrori,Impegno impegno,Integer numSub, BigDecimal importoNetto){
		
		MovimentoKey movKey = buildMovimentoKey(impegno,numSub);
		
		if(importoNetto==null){
			//il chiamante ci passare importo netto valorizzato per il caso dell'impegno destinazione del reintroito
			//per il quale si deve verificare che:
			// dispLiqImpOSubDestinazione >= importoNetto + sommaImportiOrdinativiCollegati
			//invece per tutti gli altri impegni delle (eventuali) ritenute importo netto non va considerato in quella uguaglianza
			//da rispettare
			importoNetto = new BigDecimal(0.0);
		}
		List<RigaDiReintroitoOrdinativoModel> listaRitenuteInfo = model.getReintroitoOrdinativoStep2Model().getListOrdInc();
		List<RigaDiReintroitoOrdinativoModel> ritenuteConQuestoImpOrQuestoSub = conStessoImpegno(listaRitenuteInfo, movKey);
		BigDecimal sommaImportiOrdinativiCollegati = sommaImportiOrdinativiIncasso(ritenuteConQuestoImpOrQuestoSub);
		
		BigDecimal dispLiqImpOSub = null;
		if(FinUtility.isSub(movKey)){
			dispLiqImpOSub = impegno.getElencoSubImpegni().get(0).getDisponibilitaLiquidare();
		} else {
			dispLiqImpOSub = impegno.getDisponibilitaLiquidare();
		}
		
		if(dispLiqImpOSub.compareTo(importoNetto.add(sommaImportiOrdinativiCollegati))<0){
			String nomeEntita = buildNomeEntitaPerMessaggiDiErrore(movKey, true);
			String codiceEntita = buildCodiceEntitaPerMessaggiDiErrore(movKey);
			listaErrori.add(ErroreCore.VALORE_NON_VALIDO.getErrore(nomeEntita + ": " + codiceEntita, "La sua disponibilita' a liquidare e' minore degli importi da reintroitare"));
		}
		return listaErrori;
	}
	
	/**
	 * Dato un certo impegno (o sub) verifica che il disponibile a pagare del suo capitolo sia maggiore o uguale
	 * alla sommatoria degli importi degli ordinativi di incasso per i quali e' stato indicato di utilizzare
	 * tale impegno per il reintroito piu' il valore di importoNetto.
	 * 
	 * importoNetto passatoci o meno dal chiamante e' definito come:
	 * Importo ord pag in reintroito - SOMMATORIA importi degli ordinativi di incasso ad esso collegati
	 * 
	 * @param impInfo
	 * @param esito
	 * @param importoNetto
	 * @return
	 */
	private List<Errore> controlloDisponibilitaPagareCapitoloImpegno(List<Errore> listaErrori,Impegno impegno,Integer numSub, BigDecimal importoNetto){
		MovimentoKey movKey = buildMovimentoKey(impegno,numSub);
		if(importoNetto==null){
			//il chiamante ci passare importo netto valorizzato per il caso dell'impegno destinazione del reintroito
			//per il quale si deve verificare che:
			// dispLiqImpOSubDestinazione >= importoNetto + sommaImportiOrdinativiCollegati
			//invece per tutti gli altri impegni delle (eventuali) ritenute importo netto non va considerato in quella uguaglianza
			//da rispettare
			importoNetto = new BigDecimal(0.0);
		}
		List<RigaDiReintroitoOrdinativoModel> listaRitenuteInfo = model.getReintroitoOrdinativoStep2Model().getListOrdInc();
		List<RigaDiReintroitoOrdinativoModel> ritenuteConQuestoImpOrQuestoSub = conStessoImpegno(listaRitenuteInfo, movKey);
		BigDecimal sommaImportiOrdinativiCollegati = sommaImportiOrdinativiIncasso(ritenuteConQuestoImpOrQuestoSub);
		BigDecimal dispPagareCapitolo = impegno.getCapitoloUscitaGestione().getImportiCapitolo().getDisponibilitaPagare();
		if(dispPagareCapitolo.compareTo(importoNetto.add(sommaImportiOrdinativiCollegati))<0){
			String nomeEntita = buildNomeEntitaPerMessaggiDiErrore(movKey, true);
			String codiceEntita = buildCodiceEntitaPerMessaggiDiErrore(movKey);
			listaErrori.add(ErroreCore.VALORE_NON_VALIDO.getErrore(nomeEntita + ": " + codiceEntita, "La sua disponibilita' a pagare del suo capitolo e' minore degli importi da reintroitare"));
		}
		return listaErrori;
	}
	
	protected List<RigaDiReintroitoOrdinativoModel> conStessoImpegno(List<RigaDiReintroitoOrdinativoModel> lista, MovimentoKey impKey){
		List<RigaDiReintroitoOrdinativoModel> conStessoImpegno = new ArrayList<RigaDiReintroitoOrdinativoModel>();
		if(!FinStringUtils.isEmpty(lista)){
			for(RigaDiReintroitoOrdinativoModel it : lista){
				if(it!=null && it.getImpegnoDiDestinazione()!=null){
					MovimentoKey keyIt = buildMovimentoKey(it.getImpegnoDiDestinazione(), it.getNumeroSubImpegno());
					if(FinUtility.sonoUguali(keyIt, impKey)){
						conStessoImpegno.add(clone(it));
					}
				}
			}
		}
		return conStessoImpegno;
	}
	
	/**
	 * Metodo di appoggio per verificare che tutti gli impegni o accertamenti siano stati compilati.
	 * 
	 * Ritorna false se almeno uno e' non compilato.
	 * 
	 * Il boolean impegni serve a dire al metodo se deve verificare gli impegni o gli accertamenti.
	 * 
	 * @param listaRighe
	 * @param impegni
	 * @return
	 */
	private boolean compilatiTuttiMovimenti(List<RigaDiReintroitoOrdinativoModel> listaRighe, boolean impegni) {
		boolean tuttiCompilati = true;
		if(listaRighe!=null && listaRighe.size()>0){
			for(RigaDiReintroitoOrdinativoModel it: listaRighe){
				MovimentoGestione daVerificare = it.getAccertamentoDiDestinazione();
				if(impegni){
					daVerificare = it.getImpegnoDiDestinazione();
				}
				if(FinUtility.isEmpty(daVerificare)){
					tuttiCompilati = false;
					break;
				}
			}
		}
		return tuttiCompilati;
	}
	
	
	/**
	 * Gestisce il pulsante che richiede il controllo delle disponibilita' a liquidare
	 * @return
	 */
	public String controlloDispLiquidare(){
		
		List<Errore> listaErrori= new ArrayList<Errore>();
		
		//pulisco il warning soggetti per evitare la comparsa del popup:
		pulisciChecksWarningPopUp();
		//
		
		//CONTROLLI DI OBBLIGATORIETA:
		listaErrori = controlliDatiObbligatoriCompilati(listaErrori);
		
		if(isEmpty(listaErrori)){
			//se non ho errori nemmeno tra i soggetti eseguo la coerenza movimenti:
			listaErrori =  controlliDisponibilitaLiquidareImpegni(listaErrori);
		}
		
		boolean esito = checkAndAddErrors(listaErrori);
		
		if(esito){
			addActionMessage("Gli impegni indicati rispettano i controlli di disponibilita' a liquidare");
			//addPersistentActionMessage
		}
		
		return INPUT;
	}
	
	/**
	 * Gestisce il pulsante che richiede il controllo delle disponibilita' di cassa
	 * @return
	 */
	public String controlloDispDiCassa(){
		
		//pulisco il warning soggetti per evitare la comparsa del popup:
		pulisciChecksWarningPopUp();
		//
		
		List<Errore> listaErrori= new ArrayList<Errore>();
		
		//CONTROLLI DI OBBLIGATORIETA:
		listaErrori = controlliDatiObbligatoriCompilati(listaErrori);
		
		if(isEmpty(listaErrori)){
			//se non ho errori nemmeno tra i soggetti eseguo la coerenza movimenti:
			listaErrori =  controlliDisponibilitaDiCassaImpegni(listaErrori);
		}
		
		boolean esito = checkAndAddErrors(listaErrori);
		
		if(esito){
			addActionMessage("I capitoli degli impegni indicati rispettano i controlli disponibilita di cassa");
			//addPersistentActionMessage
		}
		
		return INPUT;
	}
	
	/**
	 * 
	 * Evento correlato alla pressione del tasto esegui,
	 * in caso di warning non bloccanti sui soggetti pilota 
	 * il pop up di conferma.
	 * 
	 * @return
	 */
	public String preCheckCoerenzaSoggetti(){
		
		//pulisco i flag per i pop up di conferma:
		pulisciChecksWarningPopUp();
		
		
		//CONTROLLI DI OBBLIGATORIETA:
		List<Errore> listaErroriObbligatorieta = new ArrayList<Errore>();
		listaErroriObbligatorieta = controlliDatiObbligatoriCompilati(listaErroriObbligatorieta);
		
		//se ci sono errori bloccanti di dati non compilati blocco tutto subito:
		if(!checkAndAddErrors(listaErroriObbligatorieta)){
			return INPUT;
		}
		
		
		//lancio i controlli sui soggetti:
		EsitoControlliDto controlliSogg = controlliCoerenzaSoggetti();
		
		//se ci sono errori bloccanti blocco tutto subito:
		if(!checkAndAddErrors(controlliSogg.getListaErrori())){
			return INPUT;
		}
		
		//A questo punto posso avere dei warning da sottoporre nel pop up
		//oppure proseguo senza segnalazioni sui soggetti:
		
	    if(controlliSogg.presenzaWarning()){
	    	//Aggiungo i messaggi di warning ottenuti:
	    	addActionWarnings(controlliSogg.getListaWarning());
	    	//setto il flag a true:
	    	model.getReintroitoOrdinativoStep2Model().setCheckWarningSoggetti(true);
	    	return INPUT;
	    } else {
	    	//return prosegui();
	    	return preCheckCoerenzaPianoDeiConti();
	    }
		
	}
	
	
	public String preCheckCoerenzaPianoDeiConti(){
		
		//pulisco i flag per i pop up di conferma:
		pulisciChecksWarningPopUp();
		
		
		//CONTROLLI DI OBBLIGATORIETA: non servono perche'
		// sono sequenziale al preCheckCoerenzaSoggetti
		
		
		//lancio i controlli sui soggetti:
		EsitoControlliDto controlliSogg = controlliCoerenzaPianiDeiConti();
		
		//se ci sono errori bloccanti blocco tutto subito:
		if(!checkAndAddErrors(controlliSogg.getListaErrori())){
			return INPUT;
		}
		
		//A questo punto posso avere dei warning da sottoporre nel pop up
		//oppure proseguo senza segnalazioni sui soggetti:
		
	    if(controlliSogg.presenzaWarning()){
	    	//Aggiungo i messaggi di warning ottenuti:
	    	addActionWarnings(controlliSogg.getListaWarning());
	    	//setto il flag a true:
	    	model.getReintroitoOrdinativoStep2Model().setCheckWarningPianoDeiConti(true);
	    	return INPUT;
	    } else {
	    	return prosegui();
	    }
		
	}

	/**
	 * Metodo che gestisce il tasto prosegui
	 * verso lo step3
	 * @return
	 */
	public String prosegui(){
		
		//pulisco i flag per i pop up di conferma:
		pulisciChecksWarningPopUp();
		
		
		//1. CONTROLLI PROSEGUI:
		if(!controlliPerProsegui()){
			//ci sono errori
			return INPUT;
		}
		
		
		//2. RICHIAMIAMO IL SERVIZIO:
		
		AsyncServiceRequestWrapper<ReintroitoOrdinativoPagamento> reqAsync = buildReintroitoOrdinativoPagamentoRequestAsync();
		AsyncServiceResponse resp = ordinativoService.reintroitoOrdinativoPagamentoAsync(reqAsync);
		if(isFallimento(resp)){
			addErrori(resp);
			return INPUT;
		}
		//
		
		//INIBISCO ULTERIORI MODIFICHE:
		model.setElaborazioneAvviata(true);
		//
		
		//PULISCO PER SICUREZZA IL FLAG DI INIZIALIZZAZIONE STEP1 (cosi non blocco nuovi reintroiti):
		model.setDatiInizializzatiPrepareStep1(false);
		model.setDatiInizializzatiExecuteStep1(false);
		//
		
		//Presento il mesaggio di operazione lanciata:
		Errore messaggioAvvio = ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("di reintroito ordinativo", "l'esisto sara' disponibile su cruscotto");
		addMessaggio(messaggioAvvio);
		
		//ok
		return INPUT;
	}
	
	public String indietroStep2(){
		forceReload=false;
		return "gotoAggiorna";
	}
	
	private void aggiornaRiferimentoRiga(){
		HttpServletRequest request = ServletActionContext.getRequest();		
		String riferimentoRiga = request.getParameter("riferimentoRigaSelezionata");
		model.getReintroitoOrdinativoStep2Model().setRiferimentoRiga(riferimentoRiga);
	}
	
	private String leggiRiferimentoRiga(){
		return model.getReintroitoOrdinativoStep2Model().getRiferimentoRiga();
	}

	private void azzeraModale(){
		//AGGIORNO IL RIFERIMENTO ALLA RIGA:
		aggiornaRiferimentoRiga();
		//
		
		//pulisco anno e numero:
		model.setnAnno(null);
		model.setnImpegno(null);
		//
		
		model.setInPopup(false);	
		//metodo che pulisce le variabili nel model:
		pulisciListeeSchedaPopup();
	}
	
	/**
	 * Viene pulito il model relativo alla modale di ricerca dell'impegno
	 * @return
	 */
	public String azzeraModaleImpegno(){
		azzeraModale();
		return "refreshPopupModalImpegno";
	}
	
	/**
	 * Viene pulito il moel relativo alla modale di ricerca dell'accertamento
	 * @return
	 */
	public String azzeraModaleAccertamento(){
		azzeraModale();
		return "refreshPopupModalAccertamento";
	}
	
	
	/**
	 * Controlli da effettuare per l'impegno ricercato tramite ricerca
	 * da compilazione guidata
	 * 
	 * Aggiunge gli errori in sessione e ritorna:
	 * true se tutto ok
	 * false se trovati errori
	 * 
	 * @return
	 */
	private boolean controlliDiMeritoImpegnoCercato(RicercaImpegnoPerChiaveOttimizzatoResponse respRk){
		
		List<Errore> listaErrori= new ArrayList<Errore>();
		
		//4. ANALIZZO LA RISPOSTA DEL SERVIZIO:
		if(isFallimento(respRk) || respRk.getImpegno() == null){
			//ci sono errori:
			listaErrori.add(ErroreFin.MOVIMENTO_NON_TROVATO.getErrore("impegno"));
		} else {
			
			//qui sicuramente respRk e respRk.getImpegno sono diveri da null, ulteriori controlli:
			
			Impegno impegno = respRk.getImpegno();
			
			//STATO:
			if(!Constanti.MOVGEST_STATO_DEFINITIVO.equalsIgnoreCase(impegno.getStatoOperativoMovimentoGestioneSpesa())
				&& !Constanti.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equalsIgnoreCase(impegno.getStatoOperativoMovimentoGestioneSpesa())){
				listaErrori.add(ErroreFin.NUMERO_IMPEGNO_NON_VALIDO.getErrore("Impegno in stato: "+ impegno.getDescrizioneStatoOperativoMovimentoGestioneSpesa()));
			}
			
			//V LIVELLO PDC:
			Errore errorePdc = controlloPdc(impegno);
			if(errorePdc!=null){
				addActionWarning(errorePdc);
			}
			//
			
		}
			
		boolean esito =  checkAndAddErrors(listaErrori);
		if(!esito){
			model.setInPopup(true);
		}
		return esito;
	}
	
	/**
	 * Controlli da effettuare per l'accertamento ricercato tramite ricerca
	 * da compilazione guidata
	 * 
	 * Aggiunge gli errori in sessione e ritorna:
	 * true se tutto ok
	 * false se trovati errori
	 * 
	 * @return
	 */
	private boolean controlliDiMeritoAccertamentoCercato(RicercaAccertamentoPerChiaveOttimizzatoResponse respRk){
		
		List<Errore> listaErrori= new ArrayList<Errore>();
		
		//4. ANALIZZO LA RISPOSTA DEL SERVIZIO:
		if(isFallimento(respRk) || respRk.getAccertamento() == null){
			//ci sono errori:
			listaErrori.add(ErroreFin.MOVIMENTO_NON_TROVATO.getErrore("accertamento"));
		} else {
			
			//qui sicuramente respRk e respRk.getAccertamento sono diveri da null, ulteriori controlli:
			
			Accertamento accertamento = respRk.getAccertamento();
			
			//STATO:
			if(!Constanti.MOVGEST_STATO_DEFINITIVO.equalsIgnoreCase(accertamento.getStatoOperativoMovimentoGestioneEntrata())
					&& !Constanti.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equalsIgnoreCase(accertamento.getStatoOperativoMovimentoGestioneEntrata())){
				listaErrori.add(ErroreFin.NUMERO_IMPEGNO_NON_VALIDO.getErrore("Accertamento in stato: "+ accertamento.getStatoOperativoMovimentoGestioneEntrata()));
			}
			
			//V LIVELLO PDC:
			Errore errorePdc = controlloPdc(accertamento);
			if(errorePdc!=null){
				addActionWarning(errorePdc);
			}
			//
		}
			
		boolean esito =  checkAndAddErrors(listaErrori);
		if(!esito){
			model.setInPopup(true);
		}
		return esito;
	}
	
	/**
	 * Ricerca dell'impegno tramite compilazione guidata
	 * @return
	 */
	public String ricercaImpegnoCompilazioneGuidata(){		
		
		//puliamo i dati nel model:
		pulisciListeeSchedaPopup();
		
		//marchiamo che siamo passati dal pop up:
		model.setInPopup(true);
		
		//istanzio la lista errori:
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		//leggo anno e numero impegno dalla pagina:
		HttpServletRequest request = ServletActionContext.getRequest();		
		String annoimpegno = request.getParameter("anno");
		String numeroimpegno = request.getParameter("numero");
		
		//Controlli su anno e numero:
		listaErrori = controllaAnnoENumeroPerRicercaImpegnoCompilazioneGuidata(annoimpegno, numeroimpegno,listaErrori,false);
		
		if (!listaErrori.isEmpty()) {
			// ci sono errori
			addErrori(listaErrori);
			return "refreshPopupModalImpegno";
		}
		
		//compongo la request per la ricerca dell'impegno:
		RicercaImpegnoPerChiaveOttimizzato rip = builRequestPerRicercaImpegnoCompilazioneGuidata(annoimpegno, numeroimpegno);
		
		//invoco il servizio di ricerca:
		RicercaImpegnoPerChiaveOttimizzatoResponse respRk = movimentoGestionService.ricercaImpegnoPerChiaveOttimizzato(rip);
		
		if(!controlliDiMeritoImpegnoCercato(respRk)){
			//l'impegno non va bene
			return "refreshPopupModalImpegno";
		}
		
		Impegno impegno = respRk.getImpegno();
		
		//Sempre in seguito alle ottimizzazioni uso la lista sub impegni minimale:
		impegno.setElencoSubImpegni(respRk.getElencoSubImpegniTuttiConSoloGliIds());
		//
		
		//metto qui le info per la tabella impegno da visualizzaree sempre
		
		//setting di capitolo, provvedimento e soggetto:
		settaCapitoloProvvedimentoSoggettoPopUp(impegno);
		//
		
		model.setImpegnoPopup(impegno);
		model.setDescrizioneImpegnoPopup(impegno.getDescrizione());
		model.setHasImpegnoSelezionatoPopup(true);
		model.setHasImpegnoSelezionatoXPopup(true);
		//
		
		//imposto i dati dei sub:
		List<SubImpegno> elencoSubImpegni = respRk.getElencoSubImpegniTuttiConSoloGliIds();
		impostaListaImpegniCompGuidata(elencoSubImpegni, impegno);
		
		return "refreshPopupModalImpegno";
	}
	
	/**
	 * Ricerca dell'impegno tramite compilazione guidata
	 * @return
	 */
	public String ricercaAccertamentoCompilazioneGuidata(){		
		
		//puliamo i dati nel model:
		pulisciListeeSchedaPopup();
		
		//marchiamo che siamo passati dal pop up:
		model.setInPopup(true);
		
		//istanzio la lista errori:
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		//leggo anno e numero impegno dalla pagina:
		HttpServletRequest request = ServletActionContext.getRequest();		
		String annoimpegno = request.getParameter("anno");
		String numeroimpegno = request.getParameter("numero");
		
		//Controlli su anno e numero:
		listaErrori = controllaAnnoENumeroPerRicercaImpegnoCompilazioneGuidata(annoimpegno, numeroimpegno,listaErrori,true);
		
		if (!listaErrori.isEmpty()) {
			// ci sono errori
			addErrori(listaErrori);
			return "refreshPopupModalAccertamento";
		}
		
		//compongo la request per la ricerca dell'impegno:
		RicercaAccertamentoPerChiaveOttimizzato rip = builRequestPerRicercaAccertamentoCompilazioneGuidata(annoimpegno, numeroimpegno);
		
		//invoco il servizio di ricerca:
		RicercaAccertamentoPerChiaveOttimizzatoResponse respRk = movimentoGestionService.ricercaAccertamentoPerChiaveOttimizzato(rip);
		
		if(!controlliDiMeritoAccertamentoCercato(respRk)){
			//l'accertamento non va bene
			return "refreshPopupModalAccertamento";
		}
		
		Accertamento accertamento = respRk.getAccertamento();
		
		//Sempre in seguito alle ottimizzazioni uso la lista sub impegni minimale:
		accertamento.setElencoSubAccertamenti(respRk.getElencoSubAccertamentiTuttiConSoloGliIds());
		//
		
		//metto qui le info per la tabella impegno da visualizzaree sempre
		
		//setting di capitolo, provvedimento e soggetto:
		settaCapitoloProvvedimentoSoggettoPopUp(accertamento);
		//
		
		model.setAccertamentoPopup(accertamento);
		model.setDescrizioneImpegnoPopup(accertamento.getDescrizione());
		model.setHasImpegnoSelezionatoPopup(true);
		model.setHasImpegnoSelezionatoXPopup(true);
		//
		
		//imposto i dati dei sub:
		List<SubAccertamento> elencoSubAccertamenti = respRk.getElencoSubAccertamentiTuttiConSoloGliIds();
		impostaListaAccertamentiCompGuidata(elencoSubAccertamenti, accertamento);
		
		return "refreshPopupModalAccertamento";
	}
	
	/**
	 * Per un impegno appena caricato da ricerca da modale setta i dati per gli
	 * eventuali sub da selezionare nella modale.
	 * 
	 * L'impegno stesso diventa l'unico elemento in listaImpegniCompGuidata 
	 * nel caso in cui non ci siano sub.
	 * 
	 * @param elencoSubImpegni
	 * @param impegno
	 */
	private void impostaListaImpegniCompGuidata(List<SubImpegno> elencoSubImpegni, Impegno impegno){
		//verifico se presenti subimpegni, in questo caso popolo la tabella
		List<Impegno> listaTabellaImpegni = new ArrayList<Impegno>();		
		
		//tolgo gli annullati:
		List<SubImpegno> senzaAnnullati = FinUtility.rimuoviAnnullati(elencoSubImpegni);
		
		//aggiungo il capitolo se assenate sui sub:
		senzaAnnullati = FinUtility.impostaCapitoloDaImpegnoSeAssente(senzaAnnullati, impegno);
		
		if (senzaAnnullati != null && senzaAnnullati.size() > 0) {
			for(SubImpegno subImp: senzaAnnullati){
				listaTabellaImpegni.add((Impegno)subImp);
			}
		}		
		
		if(!listaTabellaImpegni.isEmpty()){
			model.setIsnSubImpegno(true);
		}else{
			listaTabellaImpegni.add(impegno);
			model.setnSubImpegno(null);
			model.setIsnSubImpegno(false);
		}
		
		model.setListaImpegniCompGuidata(listaTabellaImpegni);	
	}
	
	/**
	 * Per un accertamento appena caricato da ricerca da modale setta i dati per gli
	 * eventuali sub da selezionare nella modale.
	 * 
	 * L'accertamento stesso diventa l'unico elemento in listaAccertamentiCompGuidata 
	 * nel caso in cui non ci siano sub.
	 * 
	 * @param elencoSubImpegni
	 * @param impegno
	 */
	private void impostaListaAccertamentiCompGuidata(List<SubAccertamento> elencoSubAccertamenti, Accertamento accertamento){
		//verifico se presenti subimpegni, in questo caso popolo la tabella
		List<Accertamento> listaTabellaImpegni = new ArrayList<Accertamento>();		
		
		//tolgo gli annullati:
		List<SubAccertamento> senzaAnnullati = FinUtility.rimuoviSubAccAnnullati(elencoSubAccertamenti);
		
		//aggiungo il capitolo se assenate sui sub:
		senzaAnnullati = FinUtility.impostaCapitoloDaAccertamentoSeAssente(senzaAnnullati, accertamento);
		
		if (senzaAnnullati != null && senzaAnnullati.size() > 0) {
			for(SubAccertamento subImp: senzaAnnullati){
				listaTabellaImpegni.add((Accertamento)subImp);
			}
		}		
		
		if(!listaTabellaImpegni.isEmpty()){
			model.setIsnSubImpegno(true);
		}else{
			listaTabellaImpegni.add(accertamento);
			model.setnSubImpegno(null);
			model.setIsnSubImpegno(false);
		}
		
		model.setListaAccertamentiCompGuidata(listaTabellaImpegni);	
	}
	
	
	/**
	 * Controlla la validita' dei dati immessi in anno e numero impegno
	 * @param annoimpegno
	 * @param numeroimpegno
	 * @param listaErrori
	 * @return
	 */
	protected List<Errore> controllaAnnoENumeroPerRicercaImpegnoCompilazioneGuidata(String annoimpegno,String numeroimpegno, List<Errore> listaErrori, boolean entrata){
		String movimento = "Impegno";
		if(entrata){
			movimento = "Accertamento";
		}
		if (annoimpegno != null && !annoimpegno.isEmpty()) {
			try {
				Integer anno = Integer.valueOf(annoimpegno);
				model.setnAnno(anno);
				if (anno <= 1900) {
					//anno troppo antico
					listaErrori.add(ErroreCore.PARAMETRO_ERRATO.getErrore("Anno", annoimpegno, ">1900"));
				}
			} catch (NumberFormatException e) {
				//valore non valido
				listaErrori.add(ErroreFin.VALORE_NON_VALIDO.getErrore(movimento + " :Numero ", annoimpegno));
			}
		} else {
			//dato omesso
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(movimento + " :Anno "));
			model.setnAnno(null);
		}
		
		if (numeroimpegno != null && !numeroimpegno.isEmpty()) {
			try {
			model.setnImpegno(Integer.valueOf(numeroimpegno));
			} catch (NumberFormatException e) {
				//non e' un numero
				listaErrori.add(ErroreFin.VALORE_NON_VALIDO.getErrore(movimento + " :Numero ", numeroimpegno));
			}

		} else {
			//dato omesso
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(movimento + " :Numero "));
			model.setnImpegno(null);
		}
		return listaErrori;
	}
	
	/**
	 * Costruisce la request per la ricerca 
	 * dell'impegno da compilazione guidata
	 * @param annoimpegno
	 * @param numeroimpegno
	 * @return
	 */
	private RicercaImpegnoPerChiaveOttimizzato builRequestPerRicercaImpegnoCompilazioneGuidata(String annoimpegno,String numeroimpegno){
		
		//istanzio la request:
		RicercaImpegnoPerChiaveOttimizzato rip = new RicercaImpegnoPerChiaveOttimizzato();
			
		//popoliamo la request:
		
		//ente
		rip.setEnte(sessionHandler.getEnte());
		
		//richiedente
		
		rip.setRichiedente(sessionHandler.getRichiedente());
		
		//chiave di ricerca dell'impegno:
		RicercaImpegnoK k = new RicercaImpegnoK();
		k.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		if(annoimpegno!=null){
			//anno
			k.setAnnoImpegno(new Integer(annoimpegno));
		}
		if(numeroimpegno!=null){
			//numero
			k.setNumeroImpegno(new BigDecimal(numeroimpegno));
		}
		rip.setpRicercaImpegnoK(k);
		
		//APRILE 2016: OTTIMIZZAZIONI CHIAMATA RICERCA IMPEGNO:
		rip.setCaricaSub(false);
		//
		
		//Dati opzionali necessari:
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaDisponibileLiquidareEDisponibilitaInModifica(true);
		rip.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		return rip;
	}
	
	/**
	 * Costruisce la request per la ricerca 
	 * dell'accertamento da compilazione guidata
	 * @param annoimpegno
	 * @param numeroimpegno
	 * @return
	 */
	private RicercaAccertamentoPerChiaveOttimizzato builRequestPerRicercaAccertamentoCompilazioneGuidata(String annoAccertamento,String numeroaccertamento){
		
		//istanzio la request:
		RicercaAccertamentoPerChiaveOttimizzato rip = new RicercaAccertamentoPerChiaveOttimizzato();
			
		//popoliamo la request:
		
		//ente
		rip.setEnte(sessionHandler.getEnte());
		
		//richiedente
		
		rip.setRichiedente(sessionHandler.getRichiedente());
		
		//chiave di ricerca dell'impegno:
		RicercaAccertamentoK k = new RicercaAccertamentoK();
		k.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		if(annoAccertamento!=null){
			//anno
			k.setAnnoAccertamento(new Integer(annoAccertamento));
		}
		if(numeroaccertamento!=null){
			//numero
			k.setNumeroAccertamento(new BigDecimal(numeroaccertamento));
		}
		rip.setpRicercaAccertamentoK(k);
		
		//APRILE 2016: OTTIMIZZAZIONI CHIAMATA RICERCA IMPEGNO:
		rip.setCaricaSub(false);
		//
		
		//Dati opzionali necessari:
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setCaricaDisponibileLiquidareEDisponibilitaInModifica(true);
		rip.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		return rip;
	}
	
	/**
	 * conferma comp guidata
	 * @return
	 */
	public String confermaCompGuidataImpegno(){
		
		// Implementare la logica sul pulsante conferma che dovrebbe settare 
		// SOLO i quattro valori numero impegno,
		// numero sub impegno anno e numero mutuo su step1.
		
		//pulisco i warning per evitare la comparsa dei popup:
		pulisciChecksWarningPopUp();
		//
		
		if(model.isHasImpegnoSelezionatoPopup()){
			
			//FLAG IMPEGNO APPENA SELEZIONATO:
			model.setImpegnoAppenaSelezionatoDaCompGuidata(true);
			//
			
			//Leggo riferimento riga per capire da quale riga e' stata invocata la compilazione guidata:
			String riferimentoRiga = leggiRiferimentoRiga();
			
			if(!FinStringUtils.isEmpty(riferimentoRiga)){
				
				if(IMPEGNO_NETTO.equals(riferimentoRiga)){
					
					//IMPEGNO NETTO
					impostaImpegnoSelezionatoDaCompGuidata(model.getReintroitoOrdinativoStep2Model().getNetto());
					
				} else if(IMPEGNO_RITENUTE.equals(riferimentoRiga)){
					
					//imposto l'impegno sulla riga delle ritenute:
					impostaImpegnoSelezionatoDaCompGuidata(model.getReintroitoOrdinativoStep2Model().getRitenute());
					//e lo replico su tutte le righe degli ordinativi:
					for(int i=0;i<model.getReintroitoOrdinativoStep2Model().getListOrdInc().size();i++){
						impostaImpegnoSelezionatoDaCompGuidata(model.getReintroitoOrdinativoStep2Model().getListOrdInc().get(i));
					}
					
					
				} else if(ACCERTAMENTO_RITENUTE.equals(riferimentoRiga)){
					
					//imposto l'impegno sulla riga delle ritenute:
					impostaAccertamentoSelezionatoDaCompGuidata(model.getReintroitoOrdinativoStep2Model().getRitenute());
					//e lo replico su tutte le righe degli ordinativi:
					for(int i=0;i<model.getReintroitoOrdinativoStep2Model().getListOrdInc().size();i++){
						impostaAccertamentoSelezionatoDaCompGuidata(model.getReintroitoOrdinativoStep2Model().getListOrdInc().get(i));
					}
					
				} else {
					//CASO SPECIFICA RIGA ORDINATIVI INCASSO
					String numeroRiga = estraiNumeroRigaDopoUnderscore(riferimentoRiga);
					if(FinUtility.isNumeroIntero(numeroRiga)){
						//GESTIONE DINAMICA INDIVIDUARE QUALE RIGA DEGLI ORDINATIVI
						int indiceRiga = FinUtility.parseNumeroInt(numeroRiga);
						if(riferimentoRiga.startsWith("imp_")){
							impostaImpegnoSelezionatoDaCompGuidata(model.getReintroitoOrdinativoStep2Model().getListOrdInc().get(indiceRiga));
						} else {
							impostaAccertamentoSelezionatoDaCompGuidata(model.getReintroitoOrdinativoStep2Model().getListOrdInc().get(indiceRiga));
						}
					}
				}
				
			}
			
		}
		
		//FINE:
		pulisciListeeSchedaPopup();
		return INPUT;
	}
	
	/**
	 * dato un riferimento riga del tipo "imp_1" oppure "acc_3"
	 * ritorna la parte di stringa dopo l'underscore in questi casi 
	 * ritornerebbe "1" nel primo esempio e "3" nel secondo
	 * @param riferimentoRiga
	 * @return
	 */
	private String estraiNumeroRigaDopoUnderscore(String riferimentoRiga){
		String parteDopoUnderscore = "";
		List<String> tokens = FinStringUtils.getListByToken(riferimentoRiga, "_");
		if(tokens!=null && tokens.size()>0){
			parteDopoUnderscore = tokens.get(1);
		}
		return parteDopoUnderscore;
	}
	
	private SubImpegno subImpegnoSelezionato(){
		SubImpegno sub = null;
		List<Impegno> listaCompGuidata = model.getListaImpegniCompGuidata();
		if(!isEmpty(listaCompGuidata)){
			
			if(model.getRadioImpegnoSelezionato()==0){
				model.setRadioImpegnoSelezionato(listaCompGuidata.get(0).getUid());
			}
			
			int uidSelezionato = model.getRadioImpegnoSelezionato();
			
			Impegno selezionato = FinUtility.getById(listaCompGuidata, uidSelezionato);
			
			if(selezionato!=null && selezionato instanceof SubImpegno){
				sub = (SubImpegno) selezionato;
			}
			
		}
		return sub;
	}
	
	private SubAccertamento subAccertamentoSelezionato(){
		SubAccertamento sub = null;
		List<Accertamento> listaCompGuidata = model.getListaAccertamentiCompGuidata();
		if(!isEmpty(listaCompGuidata)){
			
			if(model.getRadioImpegnoSelezionato()==0){
				model.setRadioImpegnoSelezionato(listaCompGuidata.get(0).getUid());
			}
			
			int uidSelezionato = model.getRadioImpegnoSelezionato();
			
			Accertamento selezionato = FinUtility.getById(listaCompGuidata, uidSelezionato);
			
			if(selezionato!=null && selezionato instanceof SubAccertamento){
				sub = (SubAccertamento) selezionato;
			}
			
		}
		return sub;
	}
	
	
	private void impostaImpegnoSelezionatoDaCompGuidata(RigaDiReintroitoOrdinativoModel riga){
		
		SubImpegno subImpegno = clone(subImpegnoSelezionato());
		Impegno impegno = clone(model.getImpegnoPopup());
		
		riga.setImpegnoDiDestinazione(impegno);
		riga.setSubImpegnoDiDestinazione(subImpegno);
		
		riga.setNumeroImpegno(model.getnImpegno());
		riga.setAnnoImpegno(model.getnAnno());
		
		if(subImpegno!=null && subImpegno.getNumero()!=null){
			riga.setNumeroSubImpegno(subImpegno.getNumero().intValue());
		}
		
	}
	
	private void impostaAccertamentoSelezionatoDaCompGuidata(RigaDiReintroitoOrdinativoModel riga){
		
		SubAccertamento subAccertamento = clone(subAccertamentoSelezionato());
		Accertamento accertamento = clone(model.getAccertamentoPopup());
		
		riga.setAccertamentoDiDestinazione(accertamento);
		riga.setSubAccertamentoDiDestinazione(subAccertamento);
		
		riga.setNumeroAccertamento(model.getnImpegno());
		riga.setAnnoAccertamento(model.getnAnno());
		
		if(subAccertamento!=null && subAccertamento.getNumero()!=null){
			riga.setNumeroSubAccertamento(subAccertamento.getNumero().intValue());
		}
	}
	
	/**
	 * Pulisce i dati immessi tramite il tasto annulla
	 * @return
	 */
	public String annullaStep2(){
		super.annullaModelStep2();
		return SUCCESS;
	}
	
}
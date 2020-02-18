/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.mutuo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.mutuo.ImpegnoSubimpegnoMutuoModel;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaMutuoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniSubImpegniPerVociMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniSubimpegniPerVociMutuoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;
import it.csi.siac.siacfinser.model.mutuo.VariazioneImportoVoceMutuo.TipoVariazioneImportoVoceMutuo;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo.OrigineVoceMutuo;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaImpSub;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestioneVoceDiMutuoStep2Action extends GenericVoceDiMutuoAction {

	private static final long serialVersionUID = 1L;
	
	private Integer uidImpegnoSubimpegnoSelezionato;
	private String codiceMutuo;
	
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		this.model.setTitolo("Gestione Voce di Mutuo - Inserimento Voce");
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		
		//istanzio la lista nel model:
		model.setListaImpegniSubimpegni(new ArrayList<ImpegnoSubimpegnoMutuoModel>());
		
		//istanzio la request per il servizio di ricerca:
		RicercaImpegniSubImpegniPerVociMutuo ricercaImpegniSubImpegni = builRequestRicercaDaExecute();
		
		/*
		 *  INIZIO - STOPWATCH
		 */
		StopWatch stopwatch = new StopWatch("stopWatchCategory");
		stopwatch.start();
		
		//lancio la ricerca impegni con i dati caricati precedentemente
		RicercaImpegniSubimpegniPerVociMutuoResponse response = movimentoGestioneService.ricercaImpegniSubimpegniPerVociMutuo(ricercaImpegniSubImpegni );
		
		//Azzero i risultati precedenti:
		model.setListaImpegniSubimpegni(new ArrayList<ImpegnoSubimpegnoMutuoModel>());
		sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_IMPEGNI_SUB_PER_VOCI_MUTUO, null);
		model.setResultSize(0);
		//
		
		stopwatch.stop();
		stopWatchLogger.info(this.getClass().getName(), "ricercaImpegniSubimpegni",  stopwatch.getTotalTimeMillis());
		
		/*
		 *  FINE - STOPWATCH
		 */
		//controllo che la ricerca sia andata a buon fine
		if (response != null && !response.isFallimento()) {
			ImpegnoSubimpegnoMutuoModel impegnoSubimpegnoMutuoModel = null;
			//controllo che la lista non sia vuota
			if (response.getListaImpegni() != null && response.getListaImpegni().size() > 0) {
				
				for (Impegno currentImpegno : response.getListaImpegni()) {
					
					
					
					if(currentImpegno.getElencoSubImpegni() != null && currentImpegno.getElencoSubImpegni().size() > 0) {
						SubImpegno currentSubimpegno = currentImpegno.getElencoSubImpegni().get(0);
						
						//istanzio l'oggetto per il model:
						impegnoSubimpegnoMutuoModel = new ImpegnoSubimpegnoMutuoModel();
						
						//verifico che non sia gia stato utilizzato per altre voci:
						boolean presente = isPresenteTraQuelliGiaCoinvolti(currentSubimpegno, currentImpegno.getNumero());
						impegnoSubimpegnoMutuoModel.setGiaAssociatoAdUnaVoce(presente);
						
						//carico l'impegno/sub da stampare
						impegnoSubimpegnoMutuoModel.setUid(currentSubimpegno.getUid());
						impegnoSubimpegnoMutuoModel.setImpegno(false);
						impegnoSubimpegnoMutuoModel.setUidImpegnoPadre(currentImpegno.getUid());
						impegnoSubimpegnoMutuoModel.setNumeroImpegnoPadre(currentImpegno.getNumero().toString());
						impegnoSubimpegnoMutuoModel.setAnno(currentSubimpegno.getAnnoMovimento());
						impegnoSubimpegnoMutuoModel.setNumero(currentSubimpegno.getNumero().intValue());
						impegnoSubimpegnoMutuoModel.setDescrizione(currentSubimpegno.getDescrizione());
						impegnoSubimpegnoMutuoModel.setCapitolo(currentImpegno.getCapitoloUscitaGestione()); // capitolo del impegno
						impegnoSubimpegnoMutuoModel.setStato(currentSubimpegno.getStatoOperativoMovimentoGestioneSpesa());
						impegnoSubimpegnoMutuoModel.setProvvedimento(currentSubimpegno.getAttoAmministrativo());
						impegnoSubimpegnoMutuoModel.setImporto(currentSubimpegno.getImportoAttuale());
						if (currentSubimpegno.getDisponibilitaFinanziare() != null) {
							impegnoSubimpegnoMutuoModel.setImportoDaFinanziare(currentSubimpegno.getDisponibilitaFinanziare());
						} else {
							impegnoSubimpegnoMutuoModel.setImportoDaFinanziare(BigDecimal.ZERO);
						}
						//aggiorngo il sub alla lista
						model.getListaImpegniSubimpegni().add(impegnoSubimpegnoMutuoModel);
						
					}else{
						//istanzio l'oggetto per il model:
						impegnoSubimpegnoMutuoModel = new ImpegnoSubimpegnoMutuoModel();
						
						//verifico che non sia gia stato utilizzato per altre voci:
						boolean presente = isPresenteTraQuelliGiaCoinvolti(currentImpegno);
						impegnoSubimpegnoMutuoModel.setGiaAssociatoAdUnaVoce(presente);
						
						//popolo l'impegno che verra' stampato
						impegnoSubimpegnoMutuoModel.setUid(currentImpegno.getUid());
						impegnoSubimpegnoMutuoModel.setImpegno(true);
						impegnoSubimpegnoMutuoModel.setAnno(currentImpegno.getAnnoMovimento());
						impegnoSubimpegnoMutuoModel.setNumero(currentImpegno.getNumero().intValue());
						impegnoSubimpegnoMutuoModel.setDescrizione(currentImpegno.getDescrizione());
						impegnoSubimpegnoMutuoModel.setCapitolo(currentImpegno.getCapitoloUscitaGestione());
						impegnoSubimpegnoMutuoModel.setStato(currentImpegno.getStatoOperativoMovimentoGestioneSpesa());
						impegnoSubimpegnoMutuoModel.setProvvedimento(currentImpegno.getAttoAmministrativo());
						impegnoSubimpegnoMutuoModel.setImporto(currentImpegno.getImportoAttuale());
					
						if (currentImpegno.getDisponibilitaFinanziare() != null) {
							impegnoSubimpegnoMutuoModel.setImportoDaFinanziare(currentImpegno.getDisponibilitaFinanziare());
						} else {
							impegnoSubimpegnoMutuoModel.setImportoDaFinanziare(BigDecimal.ZERO);
						}
						//aggiungo alla lista l'impengo/SUB
						model.getListaImpegniSubimpegni().add(impegnoSubimpegnoMutuoModel);
					}
					
				}
				
				
				
				
				sessionHandler.setParametro(FinSessionParameter.RISULTATI_RICERCA_IMPEGNI_SUB_PER_VOCI_MUTUO, model.getListaImpegniSubimpegni());
				model.setResultSize(response.getNumRisultati());
				
				
			}
		} else {
			//stampo eventuali errori
			addErrori(getMethodName(), response);
			return INPUT;
		}
	    return SUCCESS;
	}
	
	/**
	 * Compone la request per la ricerca lanciata dentro il metoto execute
	 * @return
	 */
	private RicercaImpegniSubImpegniPerVociMutuo builRequestRicercaDaExecute(){
		RicercaImpegniSubImpegniPerVociMutuo ricercaImpegniSubImpegni = new RicercaImpegniSubImpegniPerVociMutuo();
		
		//ente
		ricercaImpegniSubImpegni.setEnte(sessionHandler.getEnte());
		
		//richiedente
		ricercaImpegniSubImpegni.setRichiedente(sessionHandler.getRichiedente());
		
		//istanzio il parametro di ricerca:
		ParametroRicercaImpSub parametroRicercaImpSub = new ParametroRicercaImpSub();
		
		//anno esercizio
		parametroRicercaImpSub.setAnnoEsercizio(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		
		//anno impegno
		parametroRicercaImpSub.setAnnoImpegno(Integer.valueOf(sessionHandler.getAnnoEsercizio()));

		//provvedimento
		if (model.getProvvedimento() != null) {
			
			//tipo provv
			TipoAtto ta = new TipoAtto();
			if(null!=model.getProvvedimento().getIdTipoProvvedimento()){
				//uid tipo
				ta.setUid(model.getProvvedimento().getIdTipoProvvedimento());
			}
			
			//codice tipo
			ta.setCodice(model.getProvvedimento().getCodiceTipoProvvedimento());
			
			//descrizione tipo
			ta.setDescrizione(model.getProvvedimento().getTipoProvvedimento());
			
			//setto il tipo provv nel parametro di ricerca
			parametroRicercaImpSub.setTipoProvvedimento(ta);
			
			//anno provvedimento
			parametroRicercaImpSub.setAnnoProvvedimento(model.getProvvedimento().getAnnoProvvedimento());
			
			//numero provvedimento
			if(model.getProvvedimento().getNumeroProvvedimento()!= null){
				parametroRicercaImpSub.setNumeroProvvedimento(model.getProvvedimento().getNumeroProvvedimento().intValue());
			}
		}
		
		//tipo impegno
		parametroRicercaImpSub.setTipoImpegno("MUT");
		
		//flag ricerca da impegno a false:
		parametroRicercaImpSub.setIsRicercaDaImpegno(Boolean.FALSE);
		
		//IPOTESI MODIFICA:
		parametroRicercaImpSub.setStato(Constanti.MOVGEST_STATO_DEFINITIVO);
		parametroRicercaImpSub.setIdMutuo(model.getMutuoSelezionato().getUid());
		//
		
		//setto il parametro nella request
		ricercaImpegniSubImpegni.setParametroRicercaImpSub(parametroRicercaImpSub);
		
		//Rm imposto la paginazione! 
		addNumAndPageSize(ricercaImpegniSubImpegni, "ricercaImpegniSubimpegniID");
		
		//ritorno la request al chiamante:
		return ricercaImpegniSubImpegni;
	}
	
	protected boolean isPresenteTraQuelliGiaCoinvolti(Impegno currentimpegno){
		boolean presente = false;
		if(currentimpegno!=null){
			
			BigDecimal numeroImpegno =  currentimpegno.getNumero();
			int annoMovimento = currentimpegno.getAnnoMovimento();
			
			List<Impegno> impegniCoinvolit = impegniGiaCoinvoltiInVoci();
			if(impegniCoinvolit!=null && impegniCoinvolit.size()>0){
				for(Impegno impIT : impegniCoinvolit){
					if(impIT!=null && (impIT.getElencoSubImpegni()==null || impIT.getElencoSubImpegni().size()==0) ){
						//mi aspetto solo l'impegno senza sub in accordo con il comportamento del servizio 
						//RicercaImpegniSubimpegniPerVociMutuoService
						int annoMov = impIT.getAnnoMovimento();
						BigDecimal numeroImp = impIT.getNumero();
						if(annoMovimento==annoMov && numeroImp.intValue()==numeroImpegno.intValue()){
							presente = true;
							break;
						}
					}
				}
			}
		}
		return presente;
	}
	
	protected boolean isPresenteTraQuelliGiaCoinvolti(SubImpegno currentSubimpegno, BigDecimal numeroImpegno){
		boolean presente = false;
		if(currentSubimpegno!=null && numeroImpegno!=null){
			List<Impegno> impegniCoinvolit = impegniGiaCoinvoltiInVoci();
			if(impegniCoinvolit!=null && impegniCoinvolit.size()>0){
				for(Impegno impIT : impegniCoinvolit){
					if(impIT!=null && impIT.getElencoSubImpegni()!=null && impIT.getElencoSubImpegni().size()>0){
						//mi aspetto un solo elemento, in accordo con il comportamento del servizio 
						//RicercaImpegniSubimpegniPerVociMutuoService
						SubImpegno subImpIt = impIT.getElencoSubImpegni().get(0);
						if(subImpIt!=null){
							
							int annoMov = impIT.getAnnoMovimento();
							BigDecimal numeroImp = impIT.getNumero();
							BigDecimal numeroSub = subImpIt.getNumero();
							
							int annoMovSubImp = currentSubimpegno.getAnnoMovimento();
							BigDecimal numeroSubImpegno = currentSubimpegno.getNumero();
							
							if(annoMovSubImp==annoMov 
									&& numeroImp.intValue()==numeroImpegno.intValue() &&
									numeroSub.intValue()==numeroSubImpegno.intValue()){
								presente = true;
								break;
							}
						}
						
					}
				}
			}
		}
		return presente;
	}
	
	protected List<Impegno> impegniGiaCoinvoltiInVoci(){
		List<Impegno> listaImpegni = new ArrayList<Impegno>();
		if(model!=null && model.getMutuoSelezionato()!=null && model.getMutuoSelezionato().getListaVociMutuo()!=null){
			List<VoceMutuo> listaVoci = model.getMutuoSelezionato().getListaVociMutuo();
			if(listaVoci!=null && listaVoci.size()>0){
				for(VoceMutuo voceIt : listaVoci){
					if(voceIt!=null && voceIt.getImpegno()!=null){
						listaImpegni.add(voceIt.getImpegno());
					}
				}
			}
			
		}
		return listaImpegni;
	}
	
	public String aggiornaImportoVoceDiMutuo() {
		if (uidImpegnoSubimpegnoSelezionato != null && uidImpegnoSubimpegnoSelezionato.intValue() != 0) {
			for (ImpegnoSubimpegnoMutuoModel currentImpegnoSubimpegno : model.getListaImpegniSubimpegni()) {
				if (currentImpegnoSubimpegno.getUid().intValue() == uidImpegnoSubimpegnoSelezionato.intValue()) {
					
					if(!model.isInserimentoStornoVoceMutuo()){
						model.setImportoNuovaVoceDiMutuo(convertiBigDecimalToImporto(BigDecimal.ZERO));
					}
					break;
				}
			}
		} else {
			if(!model.isInserimentoStornoVoceMutuo()){
				model.setImportoNuovaVoceDiMutuo(null);
			}
		}
		return "aggiornaImportoVoceDiMutuo";
	}
	
	public String salvaVoce() {
		setMethodName("salvaVoce");
		List<Errore> listaErrori = new ArrayList<Errore>();
		ImpegnoSubimpegnoMutuoModel impegnoSubimpegnoFinanziabile = null;
		BigDecimal importoNuovaVoceDiMutuoSupport = null;
		if (model.getImportoNuovaVoceDiMutuo() == null || model.getImportoNuovaVoceDiMutuo().equals("0") || model.getImportoNuovaVoceDiMutuo().equals("")) {
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo Voce di Mutuo"));
		} else {
			try {
				importoNuovaVoceDiMutuoSupport = convertiImportoToBigDecimal(model.getImportoNuovaVoceDiMutuo());
				if (importoNuovaVoceDiMutuoSupport.compareTo(BigDecimal.ZERO) <= 0) {
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo Voce di Mutuo"));
				}
				if (model.getMutuoSelezionato() != null && model.getMutuoSelezionato().getUid() != 0) {
					if (model.getMutuoSelezionato().getDisponibileMutuo().compareTo(BigDecimal.ZERO) <= 0) {
						listaErrori.add(ErroreFin.MUTUO_TOTALMENTE_FINANZIATO.getErrore(""));
					}
					if (importoNuovaVoceDiMutuoSupport.compareTo(model.getMutuoSelezionato().getDisponibileMutuo()) > 0) {
						listaErrori.add(ErroreFin.IMPORTO_VOCE_MUTUO_MAGGIORE_DISPONIBILE_MUTUO.getErrore(""));
					}
				}
				if (uidImpegnoSubimpegnoSelezionato != null && uidImpegnoSubimpegnoSelezionato.intValue() != 0) {
					for (ImpegnoSubimpegnoMutuoModel currentImpegnoSubimpegno : model.getListaImpegniSubimpegni()) {
						if (currentImpegnoSubimpegno.getUid().intValue() == uidImpegnoSubimpegnoSelezionato.intValue()) {
							impegnoSubimpegnoFinanziabile = currentImpegnoSubimpegno;
							break;
						}
					}
					if (impegnoSubimpegnoFinanziabile != null) {
						if (importoNuovaVoceDiMutuoSupport.compareTo(impegnoSubimpegnoFinanziabile.getImportoDaFinanziare()) > 0) {
							listaErrori.add(ErroreFin.IMPORTO_VOCE_MUTUO_MAGGIORE_DISPONIBILITA_IMPEGNO.getErrore(""));
						}
					}
				}
			} catch (Exception e) {
				listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Importo  Voce di Mutuo ", "numerico"));
			}
		}
		if (listaErrori.isEmpty()) {
			AggiornaMutuo aggiornaMutuo = new AggiornaMutuo();
			aggiornaMutuo.setEnte(sessionHandler.getEnte());
			aggiornaMutuo.setRichiedente(sessionHandler.getRichiedente());
			
			AggiornaMutuoResponse response = null;
			
			if(model.isInserimentoStornoVoceMutuo()){
				// occore inserire una nuova voce di mutuo
				
				VoceMutuo vmn = creaNuovaVoceMutuo(impegnoSubimpegnoFinanziabile, importoNuovaVoceDiMutuoSupport, OrigineVoceMutuo.STORNO);
				model.getMutuoSelezionato().getListaVociMutuo().add(vmn);
				
				String ritornoCiclo = cicloPerVariazione(importoNuovaVoceDiMutuoSupport, TipoVariazioneImportoVoceMutuo.STORNO);
			
			    if(ritornoCiclo.equals(SUCCESS)){
			    	setCodiceMutuo(model.getMutuoSelezionato().getCodiceMutuo());
			    	addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
			    	model.setToggleImpAccAperto(false);
					return GOTO_ELENCO_VOCE_DI_MUTUO;
			    }
				
				
			}else {
				VoceMutuo vm = creaNuovaVoceMutuo(impegnoSubimpegnoFinanziabile,importoNuovaVoceDiMutuoSupport, OrigineVoceMutuo.ORIGINALE);
				
				model.getMutuoSelezionato().getListaVociMutuo().add(vm);
				
				aggiornaMutuo.setMutuo(model.getMutuoSelezionato());
				response = mutuoService.aggiornaMutuo(aggiornaMutuo);
				
			}	
			
			
			
			if (response != null && !response.isFallimento()) {
				setCodiceMutuo(model.getMutuoSelezionato().getCodiceMutuo());
				
				Mutuo mutuoTemp = model.getMutuoSelezionato();
				clearActionData();
				// reinserisco il mutuo selezionato nel model
				model.setMutuoSelezionato(mutuoTemp);
				addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
				model.setToggleImpAccAperto(false);
				return GOTO_ELENCO_VOCE_DI_MUTUO;
			} else {
				model.setToggleImpAccAperto(true);
				return INPUT;
			}
			
		} else {
			addErrori(listaErrori);
			model.setToggleImpAccAperto(true);
			return INPUT;
		}
	}

	private VoceMutuo creaNuovaVoceMutuo(ImpegnoSubimpegnoMutuoModel impegnoSubimpegnoFinanziabile, BigDecimal importoNuovaVoceDiMutuoSupport, OrigineVoceMutuo origineVoceMutuo) {
		VoceMutuo vm = new VoceMutuo();
		if (impegnoSubimpegnoFinanziabile != null) {
			if (!impegnoSubimpegnoFinanziabile.isImpegno()) {
				vm.setImpegno(new Impegno());
				vm.getImpegno().setUid(impegnoSubimpegnoFinanziabile.getUidImpegnoPadre());
				SubImpegno sub = new SubImpegno();
				sub.setUid(impegnoSubimpegnoFinanziabile.getUid());
				sub.setDisponibilitaFinanziare(impegnoSubimpegnoFinanziabile.getImportoDaFinanziare());
				List<SubImpegno> listaSub = new ArrayList<SubImpegno>();
				listaSub.add(sub);
				vm.getImpegno().setElencoSubImpegni(listaSub);
			} else {
				vm.setImpegno(new Impegno());
				vm.getImpegno().setUid(impegnoSubimpegnoFinanziabile.getUid());
				vm.getImpegno().setDisponibilitaFinanziare(impegnoSubimpegnoFinanziabile.getImportoDaFinanziare());
			}
		}
		if (importoNuovaVoceDiMutuoSupport != null) {
			// setto gli importi della voce di mutuo
			vm.setImportoInizialeVoceMutuo(importoNuovaVoceDiMutuoSupport);
			vm.setImportoAttualeVoceMutuo(importoNuovaVoceDiMutuoSupport);
		}
		vm.setOrigineVoceMutuo(origineVoceMutuo);
		if(model.getMutuoSelezionato().getListaVociMutuo()==null){
			model.getMutuoSelezionato().setListaVociMutuo(new ArrayList<VoceMutuo>());
		}
		return vm;
	}
	
	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */

	public Integer getUidImpegnoSubimpegnoSelezionato() {
		return uidImpegnoSubimpegnoSelezionato;
	}

	public void setUidImpegnoSubimpegnoSelezionato(
			Integer uidImpegnoSubimpegnoSelezionato) {
		this.uidImpegnoSubimpegnoSelezionato = uidImpegnoSubimpegnoSelezionato;
	}

	public String getCodiceMutuo() {
		return codiceMutuo;
	}

	public void setCodiceMutuo(String codiceMutuo) {
		this.codiceMutuo = codiceMutuo;
	}
}
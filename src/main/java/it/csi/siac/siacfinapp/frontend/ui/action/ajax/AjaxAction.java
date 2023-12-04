/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ajax;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.business.utility.capitolo.ComponenteImportiCapitoloPerAnnoHelper;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.ComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.TipoComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiConti;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiContiResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiope;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiopeResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImpegnabileComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabile;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabileResponse;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinapp.frontend.ui.action.GenericFinAction;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.ajax.AjaxModel;
import it.csi.siac.siacfinapp.frontend.ui.model.commons.GestoreDatiAlberoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinser.frontend.webservice.ClassificatoreFinService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuni;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaSedime;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaSedimeResponse;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AjaxAction extends GenericFinAction<AjaxModel>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4852924671931064776L;
	
	
	
	private String idMacroaggregato, idPianoDeiConti, idPianoDeiContiCapitoloPadrePerAggiorna; 
	private String datiUscitaImpegno;
	private String term;
	private String idNazione;
	
	
	//VARIABILI PER PILOTARE I CARICAMENTI:
	private boolean ricaricaAlberoPianoDeiConti = true;
	
	private String struttAmmOriginale;
	private String struttAmmOriginaleCompetente;
	
	private boolean ricaricaStrutturaAmministrativa = true;
	private boolean ricaricaSiopeSpesa = true;
			
	private boolean daRicerca = false;
	
	//SIAC-7349
	private String radioCodiceCapitolo;
	private Integer numCapitolo;
	private Integer anno;
	private Integer articolo;
	private String azione;
	
	//SIAC-7349
	@Autowired protected ComponenteImportiCapitoloService componenteImportiCapitoloService;
	@Autowired protected TipoComponenteImportiCapitoloService tipoComponenteImportiCapitoloService;
	
	@Autowired
	protected transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;

	@Autowired
	protected transient ClassificatoreBilService classificatoreBilService;
	@Autowired
	protected transient ClassificatoreFinService classificatoreFinService;
	
	@Override
	public String getActionKey() {
		return "gestioneDatiAjax";
	}
	
	@Override
	public String execute() throws Exception {
		
		return SUCCESS;
	}
	
	/**
	 * utilizzata per popolare le combo comuni
	 * @return
	 * @throws Exception
	 */
	public String getComuniLike() throws Exception {
		final String methodName = "getComuniLike";
		
		
		log.debug(methodName, "AJAX - comune    "+getTerm());
		log.debug(methodName, "AJAX -id nazione "+getIdNazione());
		
		ListaComuni listaComuni = new ListaComuni();
		
		listaComuni.setDescrizioneComune(getTerm());
		listaComuni.setIdStato(getIdNazione());
		listaComuni.setRichiedente(sessionHandler.getRichiedente());
		
		log.debug(methodName, "CHIAMO LA FUNZIONE");
		ListaComuniResponse listaComuniResponse = genericService.cercaComuni(listaComuni);
		
		model.setListaComuni(listaComuniResponse.getListaComuni());
			
		return SUCCESS;
	}

	/**
	 * utilizzata per popolare le combo sedime (via, viale...)
	 * @return
	 * @throws Exception
	 */
	public String getSedimiLike() throws Exception {
		ListaSedime request = new ListaSedime();
		request.setDescrizioneSedime(term);
		request.setRichiedente(sessionHandler.getRichiedente());
		ListaSedimeResponse listaComuniResponse = genericService.listaSedime(request);
		model.setListaSedimi(listaComuniResponse.getListaSedime());
		return SUCCESS;
	}
	
	/**
	 * utilizzato per popolare il pdc
	 * @return
	 * @throws Exception
	 */
	public String getPianoDeiConti() throws Exception {
		if (ricaricaAlberoPianoDeiConti) {
			boolean chiamataSenzaPadre = true;
			LeggiTreePianoDeiConti lpdc = buildPianoDeiContiRequest();
			if (idMacroaggregato != null){
				chiamataSenzaPadre = false;
			}
			//Inserita if per ricerca PDC su modalCapitolo troppo lenta
			if (!chiamataSenzaPadre || daRicerca) {
				LeggiTreePianoDeiContiResponse responseLpdc = getPianoDeiContiCached(lpdc);
				model.setListaPianoDeiConti(new ArrayList<GestoreDatiAlberoModel>());
				if (responseLpdc != null && responseLpdc.getTreeElementoPianoDeiConti() != null && responseLpdc.getTreeElementoPianoDeiConti().size() > 0) {
					boolean padreTrovato = false;
					for (ElementoPianoDeiConti currentElementoPdc : responseLpdc.getTreeElementoPianoDeiConti()) {
						
						boolean figlioDaUtilizzare = false;
						if (chiamataSenzaPadre) {
							popolaElementoPdc(currentElementoPdc, RADICE_ALBERO);
							padreTrovato = true;
						} else {
							if ((idPianoDeiContiCapitoloPadrePerAggiorna != null && !idPianoDeiContiCapitoloPadrePerAggiorna.equalsIgnoreCase("") && Integer.valueOf(idPianoDeiContiCapitoloPadrePerAggiorna).intValue() == currentElementoPdc.getUid())) {
								popolaElementoPdc(currentElementoPdc, RADICE_ALBERO);
								padreTrovato = true;
								figlioDaUtilizzare = true;
							} else if (idPianoDeiConti != null && Integer.valueOf(idPianoDeiConti).intValue() == currentElementoPdc.getUid()) {
								popolaElementoPdc(currentElementoPdc, RADICE_ALBERO);
								padreTrovato = true;
								figlioDaUtilizzare = true;
							}
						}
						if (currentElementoPdc.getElemPdc() != null && currentElementoPdc.getElemPdc().size() > 0) {
							ricorsivaGestioneElementoPdc(currentElementoPdc.getElemPdc(), currentElementoPdc.getCodice(), padreTrovato, figlioDaUtilizzare);
						}
					}
				}
			}
		} else {
			if (model.getListaPianoDeiConti() != null && model.getListaPianoDeiConti().size() > 0) {
				for (GestoreDatiAlberoModel currentElementoPdc : model.getListaPianoDeiConti()) {
					if (idPianoDeiConti != null && !"".equalsIgnoreCase(idPianoDeiConti)) {
						if (Integer.valueOf(idPianoDeiConti).intValue() == currentElementoPdc.getUid()) {
							currentElementoPdc.setChecked(true);
						} else {
							currentElementoPdc.setChecked(false);
						}
						currentElementoPdc.setOpen(true);
					}
				}
			}
			
		}
		// ordinamento voci del pdc
		if(model.getListaPianoDeiConti()!=null && model.getListaPianoDeiConti().size()>0){
			
			Collections.sort(model.getListaPianoDeiConti(), new Comparator<GestoreDatiAlberoModel>() {

				@Override
				public int compare(GestoreDatiAlberoModel o1, GestoreDatiAlberoModel o2) {
					return o1.getCodice().compareTo(o2.getCodice());
				}
				
			});
		}
		
		return SUCCESS;
	}
	
	/*
	 *  per ora non e' piu' utilizzato
	 *  al momento non ci sono pagine che necessitano
	 *  il caricamento di alberi completi
	 */
	public String getPianoDeiContiCompleto() throws Exception {
		if (ricaricaAlberoPianoDeiConti) {
			LeggiTreePianoDeiConti lpdc = new LeggiTreePianoDeiConti();
			lpdc.setAnno(sessionHandler.getAnnoBilancio());
			lpdc.setIdEnteProprietario(sessionHandler.getEnte().getUid());
			lpdc.setRichiedente(sessionHandler.getRichiedente());
			//Inserita if per ricerca PDC su modalCapitolo troppo lenta
			LeggiTreePianoDeiContiResponse responseLpdc = classificatoreBilService.leggiTreePianoDeiConti(lpdc);
			model.setListaPianoDeiContiCompleto(new ArrayList<GestoreDatiAlberoModel>());
			if (responseLpdc != null && responseLpdc.getTreeElementoPianoDeiConti() != null && responseLpdc.getTreeElementoPianoDeiConti().size() > 0) {
				popolaElementoPdcCompleto(responseLpdc.getTreeElementoPianoDeiConti().get(0), RADICE_ALBERO);
				if (responseLpdc.getTreeElementoPianoDeiConti().get(0).getElemPdc() != null && responseLpdc.getTreeElementoPianoDeiConti().get(0).getElemPdc().size() > 0) {
					ricorsivaGestioneElementoPdcCompleto(responseLpdc.getTreeElementoPianoDeiConti().get(0).getElemPdc(), responseLpdc.getTreeElementoPianoDeiConti().get(0).getCodice());
				}
			}
		} else {
			for (GestoreDatiAlberoModel currentElementoPdc : model.getListaPianoDeiContiCompleto()) {
				if (idPianoDeiConti != null && !"".equalsIgnoreCase(idPianoDeiConti)) {
					if (Integer.valueOf(idPianoDeiConti).intValue() == currentElementoPdc.getUid()) {
						currentElementoPdc.setChecked(true);
					} else {
						currentElementoPdc.setChecked(false);
					}
					currentElementoPdc.setOpen(true);
				}
			}
		}
		
		if(model.getListaPianoDeiConti()!=null && model.getListaPianoDeiConti().size()>0){
			
			Collections.sort(model.getListaPianoDeiConti(), new Comparator<GestoreDatiAlberoModel>() {

				@Override
				public int compare(GestoreDatiAlberoModel o1, GestoreDatiAlberoModel o2) {
					return o1.getCodice().compareTo(o2.getCodice());
				}
				
			});
		}
		
		return SUCCESS;
	}
	
	/**
	 * utilizzato per popolare l'albero della struttura amministrativa
	 * @return
	 * @throws Exception
	 */
	public String getStrutturaAmministrativa() throws Exception {
		if(ricaricaStrutturaAmministrativa){
			
			List<GestoreDatiAlberoModel> listaStruttureAmministrativeTmpSupport = new ArrayList<GestoreDatiAlberoModel>();
			
			// JIRA - 1648 
			// l'albero della struttura va sempre visualizzato tutto
			LeggiStrutturaAmminstrativoContabile lsa = buildStrutturaAmminstrativoContabileRequest();
			LeggiStrutturaAmminstrativoContabileResponse responseLsa = getStrutturaAmministrativaCached(lsa);
			model.setListaStrutturaAmministrative(new ArrayList<GestoreDatiAlberoModel>());
			listaStruttureAmministrativeTmpSupport = creaAlberoStrutturaAmministrativa(responseLsa.getListaStrutturaAmmContabile(), false);
			
		    //PUO' essere indicato un valore iniziale checkato:
			if(!StringUtils.isEmpty(struttAmmOriginale)){
				Integer uidIniziale = new Integer(struttAmmOriginale);
				for(GestoreDatiAlberoModel it: listaStruttureAmministrativeTmpSupport){
					if(it!=null && it.getUid()==uidIniziale.intValue()){
						it.setChecked(true);
						break;
					}
				}
			}
			
			model.setListaStrutturaAmministrative(listaStruttureAmministrativeTmpSupport);
		}
		// ordinamento della struttura amministrativa
		if(model.getListaStrutturaAmministrative()!=null && model.getListaStrutturaAmministrative().size()>0){
			sessionHandler.setParametro(FinSessionParameter.ELENCO_STRUTTURA_AMMINISTRATIVA, model.getListaStrutturaAmministrative());
			Collections.sort(model.getListaStrutturaAmministrative(), new Comparator<GestoreDatiAlberoModel>() {

				@Override
				public int compare(GestoreDatiAlberoModel o1, GestoreDatiAlberoModel o2) {
					return o1.getCodice().compareTo(o2.getCodice());
				}
				
			});
		}
		
		return SUCCESS;
	}

	
	//SIAC-7477
	/**
	 * utilizzato per popolare l'albero della struttura amministrativa
	 * @return
	 * @throws Exception
	 */
	public String getStrutturaAmministrativaCompetente() throws Exception {
		if(ricaricaStrutturaAmministrativa){
			
			List<GestoreDatiAlberoModel> listaStruttureAmministrativeTmpSupport = new ArrayList<GestoreDatiAlberoModel>();
			
			// JIRA - 1648 
			// l'albero della struttura va sempre visualizzato tutto
			LeggiStrutturaAmminstrativoContabile lsa = buildStrutturaAmminstrativoContabileRequest();
			LeggiStrutturaAmminstrativoContabileResponse responseLsa = getStrutturaAmministrativaCached(lsa);
			model.setListaStrutturaAmministrativeCompetente(new ArrayList<GestoreDatiAlberoModel>());
			listaStruttureAmministrativeTmpSupport = creaAlberoStrutturaAmministrativa(responseLsa.getListaStrutturaAmmContabile(), false);
			
		    //PUO' essere indicato un valore iniziale checkato:
			if(!StringUtils.isEmpty(struttAmmOriginaleCompetente)){
				Integer uidIniziale = new Integer(struttAmmOriginaleCompetente);
				for(GestoreDatiAlberoModel it: listaStruttureAmministrativeTmpSupport){
					if(it!=null && it.getUid()==uidIniziale.intValue()){
						it.setChecked(true);
						break;
					}
				}
			}
			
			model.setListaStrutturaAmministrativeCompetente(listaStruttureAmministrativeTmpSupport);
		}
		// ordinamento della struttura amministrativa
		if(model.getListaStrutturaAmministrativeCompetente()!=null && model.getListaStrutturaAmministrativeCompetente().size()>0){
			sessionHandler.setParametro(FinSessionParameter.ELENCO_STRUTTURA_AMMINISTRATIVA_COMPETENTE, model.getListaStrutturaAmministrativeCompetente());
			Collections.sort(model.getListaStrutturaAmministrativeCompetente(), new Comparator<GestoreDatiAlberoModel>() {

				@Override
				public int compare(GestoreDatiAlberoModel o1, GestoreDatiAlberoModel o2) {
					return o1.getCodice().compareTo(o2.getCodice());
				}
				
			});
		}
		
		return SUCCESS;
	}

	/**
	 * utlizzato per generare chiavi univoche da mettere in cache l'albero siope
	 * @return
	 */
	private String buildSiopeRequestKey(LeggiTreeSiope requestSiope,boolean entrata){
		String requestKey = buildBaseRequestKey();
		requestKey = requestKey + Integer.valueOf(idPianoDeiConti);
		if(entrata){
			requestKey = requestKey + "_E";
		} else {
			requestKey = requestKey + "_U"; 
		}
		return requestKey;
	}
	
	private LeggiTreeSiope buildSiopeRequest(){
				LeggiTreeSiope leggiTreeSiope = new LeggiTreeSiope();
				leggiTreeSiope.setRichiedente(sessionHandler.getRichiedente());
				leggiTreeSiope.setAnno(sessionHandler.getAnnoBilancio());
				leggiTreeSiope.setIdEnteProprietario(sessionHandler.getEnte().getUid());
				leggiTreeSiope.setIdCodificaPadre(Integer.valueOf(idPianoDeiConti));
		return leggiTreeSiope;
	}
	
	/**
	 * utlizzato per generare chiavi univoche da mettere in cache il PDC
	 * @return
	 */
	private LeggiTreePianoDeiConti buildPianoDeiContiRequest(){
		LeggiTreePianoDeiConti lpdc = new LeggiTreePianoDeiConti();
		lpdc.setAnno(sessionHandler.getAnnoBilancio());
		lpdc.setIdEnteProprietario(sessionHandler.getEnte().getUid());
		lpdc.setRichiedente(sessionHandler.getRichiedente());
		if (idMacroaggregato != null && !StringUtils.isBlank(idMacroaggregato)){
			lpdc.setIdCodificaPadre(Integer.valueOf(idMacroaggregato));
		}
		return lpdc;
	}
	
	private String buildPianoDeiContiRequestKey(LeggiTreePianoDeiConti request){
		String requestKey = buildBaseRequestKey();
		if (idMacroaggregato != null){
			requestKey = requestKey + "_p" +idMacroaggregato;
		} else {
			requestKey = requestKey + "_np";
		}
		return requestKey;
	}
	
	@SuppressWarnings("rawtypes")
	private LeggiTreeSiopeResponse getSiopeCached(LeggiTreeSiope requestSiope, boolean entrata) {
		// se non passo alcun elemento, ritorno una mappa vuota
		LeggiTreeSiopeResponse result = null;
		synchronized (AjaxAction.class) {
			String key = "__SIOPE_KEY";
			String requestSiopeKey = buildSiopeRequestKey(requestSiope,entrata);
			// Carico la cache per tipologia:
			Map<String, ServiceCache> fullCache = getCacheTyped(key);
			//pesco l'oggetto specifico dalla cache specifica:
			LeggiTreeSiopeResponse ltsr = new LeggiTreeSiopeResponse();
			
			if(entrata){
				// siope entrata
				ltsr.setTreeSiopeEntrata((List<SiopeEntrata>)getOggettoInCache(fullCache, requestSiopeKey));
			}else{
				// siope spesa
				ltsr.setTreeSiopeSpesa((List<SiopeSpesa>)getOggettoInCache(fullCache, requestSiopeKey));
			}
			result  = ltsr;
			ServiceCache<Serializable> currentCache = null;
			if(entrata){
				if(result.getTreeSiopeEntrata()==null){
					result = classificatoreBilService.leggiTreeSiopeEntrata(requestSiope);
					currentCache = new ServiceCache<Serializable>((Serializable)result.getTreeSiopeEntrata(), System.currentTimeMillis());
				}else{
					result = classificatoreBilService.leggiTreeSiopeSpesa(requestSiope);
					currentCache = new ServiceCache<Serializable>((Serializable)result.getTreeSiopeSpesa(), System.currentTimeMillis());
				}
			}
			
			fullCache.put(requestSiopeKey, currentCache);
			
		}
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	private LeggiTreePianoDeiContiResponse getPianoDeiContiCached(LeggiTreePianoDeiConti request){
		// se non passo alcun elemento, ritorno una mappa vuota
		LeggiTreePianoDeiContiResponse result = null;
		String key = "__PIANO_DEI_CONTI_KEY";
		String requestKey = buildPianoDeiContiRequestKey(request);
		// Carico la cache per tipologia:
		Map<String, ServiceCache> fullCache = getCacheTyped(key);
		//pesco l'oggetto specifico dalla cache specifica:
		LeggiTreePianoDeiContiResponse ltpc = new LeggiTreePianoDeiContiResponse();
		ltpc.setTreeElementoPianoDeiConti((List<ElementoPianoDeiConti>)getOggettoInCache(fullCache, requestKey));
		result = ltpc;
		if(result.getTreeElementoPianoDeiConti()==null){
			//se l'oggetto e' nullo devo invocare il servizio opportuno
			result = classificatoreBilService.leggiTreePianoDeiConti(request);
			ServiceCache<Serializable> currentCache = new ServiceCache<Serializable>((Serializable)result.getTreeElementoPianoDeiConti(), System.currentTimeMillis());
			fullCache.put(requestKey, currentCache);
		}
		return result;
	}
	
	public String getSiopeSpesa() {
		if(ricaricaSiopeSpesa){
			if (idPianoDeiConti != null && !"".equalsIgnoreCase(idPianoDeiConti)) {
				LeggiTreeSiope leggiTreeSiope = buildSiopeRequest();
				LeggiTreeSiopeResponse leggiTreeSiopeResponse = null;
				if(datiUscitaImpegno.equals("true")){
					// IMPEGNO
					leggiTreeSiopeResponse = getSiopeCached(leggiTreeSiope,false);
					model.setListaSiopeSpesa(new ArrayList<GestoreDatiAlberoModel>());
					if (leggiTreeSiopeResponse != null && leggiTreeSiopeResponse.getTreeSiopeSpesa() != null && leggiTreeSiopeResponse.getTreeSiopeSpesa().size() > 0) {
						for (SiopeSpesa currentElementoSiopeSpesa : leggiTreeSiopeResponse.getTreeSiopeSpesa()) {
							popolaElementoSiopeSpesa(currentElementoSiopeSpesa, RADICE_ALBERO);
							if (currentElementoSiopeSpesa.getFigli() != null && currentElementoSiopeSpesa.getFigli().size() > 0) {
								ricorsivaGestioneElementoSiopeSpesa(currentElementoSiopeSpesa.getFigli(), currentElementoSiopeSpesa.getCodice());
							}
						}
					}
				}else{
					// ACCERTAMENTO
					leggiTreeSiopeResponse = getSiopeCached(leggiTreeSiope,true);
					model.setListaSiopeSpesa(new ArrayList<GestoreDatiAlberoModel>());
					if (leggiTreeSiopeResponse != null && leggiTreeSiopeResponse.getTreeSiopeEntrata() != null && leggiTreeSiopeResponse.getTreeSiopeEntrata().size() > 0) {
						for (SiopeEntrata currentElementoSiopeEntrata : leggiTreeSiopeResponse.getTreeSiopeEntrata()) {
							popolaElementoSiopeEntrata(currentElementoSiopeEntrata, RADICE_ALBERO);
							if (currentElementoSiopeEntrata.getFigli() != null && currentElementoSiopeEntrata.getFigli().size() > 0) {
								ricorsivaGestioneElementoSiopeEntrata(currentElementoSiopeEntrata.getFigli(), currentElementoSiopeEntrata.getCodice());
							}
						}
					}
				}
				
			}
		}
		return SUCCESS;
	}
	
	private void ricorsivaGestioneElementoSiopeSpesa(List<SiopeSpesa> elementiSiopeSpesaFigli, String idPadre) {
		for (SiopeSpesa currentElementoSiopeSpesa : elementiSiopeSpesaFigli) {
			popolaElementoSiopeSpesa(currentElementoSiopeSpesa, idPadre);
			if (currentElementoSiopeSpesa.getFigli() != null && currentElementoSiopeSpesa.getFigli().size() > 0) {
				ricorsivaGestioneElementoSiopeSpesa(currentElementoSiopeSpesa.getFigli(), currentElementoSiopeSpesa.getCodice());
			}
		}
	}
	
	private void ricorsivaGestioneElementoSiopeEntrata(List<SiopeEntrata> elementiSiopeEntrataFigli, String idPadre) {
		for (SiopeEntrata currentElementoSiopeEntrata : elementiSiopeEntrataFigli) {
			popolaElementoSiopeEntrata(currentElementoSiopeEntrata, idPadre);
			
			if (currentElementoSiopeEntrata.getFigli() != null && currentElementoSiopeEntrata.getFigli().size() > 0) {
				ricorsivaGestioneElementoSiopeEntrata(currentElementoSiopeEntrata.getFigli(), currentElementoSiopeEntrata.getCodice());
			}
		}
	}
	
	private void ricorsivaGestioneElementoPdc(List<ElementoPianoDeiConti> elementiPdcFigli, String idPadre, boolean padreTrovato, boolean figlioDaUtilizzare) {
		for (ElementoPianoDeiConti currentElementoPdc : elementiPdcFigli) {
			if (idPianoDeiContiCapitoloPadrePerAggiorna != null && !idPianoDeiContiCapitoloPadrePerAggiorna.equalsIgnoreCase("") && Integer.valueOf(idPianoDeiContiCapitoloPadrePerAggiorna).intValue() == currentElementoPdc.getUid()) {
				popolaElementoPdc(currentElementoPdc, !padreTrovato ? RADICE_ALBERO : idPadre);
				padreTrovato = true;
				if (currentElementoPdc.getElemPdc() != null && currentElementoPdc.getElemPdc().size() > 0) {
					ricorsivaGestioneElementoPdc(currentElementoPdc.getElemPdc(), currentElementoPdc.getCodice(), padreTrovato, true);
				}
			} else {
				if (idPianoDeiConti != null && !"".equalsIgnoreCase(idPianoDeiConti)) {
					if (Integer.valueOf(idPianoDeiConti).intValue() == currentElementoPdc.getUid()) {
						popolaElementoPdc(currentElementoPdc, !padreTrovato ? RADICE_ALBERO : idPadre);
						padreTrovato = true;
						if (currentElementoPdc.getElemPdc() != null && currentElementoPdc.getElemPdc().size() > 0) {
							ricorsivaGestioneElementoPdc(currentElementoPdc.getElemPdc(), currentElementoPdc.getCodice(), padreTrovato, true);
						}
					} else if (figlioDaUtilizzare) {
						popolaElementoPdc(currentElementoPdc, !padreTrovato ? RADICE_ALBERO : idPadre);
					}
				} else {
					if (padreTrovato == true) {
						popolaElementoPdc(currentElementoPdc, !padreTrovato ? RADICE_ALBERO : idPadre);
						padreTrovato = true;
					}
				}
				if (currentElementoPdc.getElemPdc() != null && currentElementoPdc.getElemPdc().size() > 0) {
					ricorsivaGestioneElementoPdc(currentElementoPdc.getElemPdc(), currentElementoPdc.getCodice(), padreTrovato, false);
				}
			}
		}
	}
	
	private void ricorsivaGestioneElementoPdcCompleto(List<ElementoPianoDeiConti> elementiPdcFigli, String idPadre) {
		for (ElementoPianoDeiConti currentElementoPdc : elementiPdcFigli) {
			popolaElementoPdcCompleto(currentElementoPdc, idPadre);
			if (currentElementoPdc.getElemPdc() != null && currentElementoPdc.getElemPdc().size() > 0) {
				ricorsivaGestioneElementoPdcCompleto(currentElementoPdc.getElemPdc(), currentElementoPdc.getCodice());
			}
		}
	}
	
	
	private void popolaElementoPdc(ElementoPianoDeiConti currentElementoPdc, String idPadre) {
		GestoreDatiAlberoModel supportElementoPdc = new GestoreDatiAlberoModel();
		if (idPadre != null && !RADICE_ALBERO.equalsIgnoreCase(idPadre)) {
			supportElementoPdc.setpId(idPadre);
		} else {
			supportElementoPdc.setpId(RADICE_ALBERO);
		}
		supportElementoPdc.setId(currentElementoPdc.getCodice());
		supportElementoPdc.setUid(currentElementoPdc.getUid());
		supportElementoPdc.setName(currentElementoPdc.getCodice() + " - " + currentElementoPdc.getDescrizione());
		supportElementoPdc.setCodice(currentElementoPdc.getCodice());
	    supportElementoPdc.setTipoClassificatore(currentElementoPdc.getTipoClassificatore());
		
		if (idPianoDeiConti != null && !"".equalsIgnoreCase(idPianoDeiConti)) {
			if (Integer.valueOf(idPianoDeiConti).intValue() == currentElementoPdc.getUid()) {
				supportElementoPdc.setChecked(true);
				supportElementoPdc.setOpen(true);
			}
		}
		model.getListaPianoDeiConti().add(supportElementoPdc);
	}
	
	private void popolaElementoPdcCompleto(ElementoPianoDeiConti currentElementoPdc, String idPadre) {
		GestoreDatiAlberoModel supportElementoPdc = new GestoreDatiAlberoModel();
		if (idPadre != null && !RADICE_ALBERO.equalsIgnoreCase(idPadre)) {
			supportElementoPdc.setpId(idPadre);
		} else {
			supportElementoPdc.setpId(RADICE_ALBERO);
		}
		supportElementoPdc.setId(currentElementoPdc.getCodice());
		supportElementoPdc.setUid(currentElementoPdc.getUid());
		supportElementoPdc.setName(currentElementoPdc.getCodice()+" - "+currentElementoPdc.getDescrizione());
		supportElementoPdc.setCodice(currentElementoPdc.getCodice());
		if (idPianoDeiConti != null && !"".equalsIgnoreCase(idPianoDeiConti)) {
			if (Integer.valueOf(idPianoDeiConti).intValue() == currentElementoPdc.getUid()) {
				supportElementoPdc.setChecked(true);
				supportElementoPdc.setOpen(true);
			}
		}
		model.getListaPianoDeiContiCompleto().add(supportElementoPdc);
	}
	
	private void popolaElementoSiopeSpesa(SiopeSpesa currentElementoSiopeSpesa, String idPadre) {
		GestoreDatiAlberoModel supportElementoSiopeSpesa = new GestoreDatiAlberoModel();
		if (idPadre != null && !RADICE_ALBERO.equalsIgnoreCase(idPadre)) {
			supportElementoSiopeSpesa.setpId(idPadre);
		} else {
			supportElementoSiopeSpesa.setpId(RADICE_ALBERO);
		}
		supportElementoSiopeSpesa.setId(currentElementoSiopeSpesa.getCodice());
		supportElementoSiopeSpesa.setUid(currentElementoSiopeSpesa.getUid());
		supportElementoSiopeSpesa.setName(currentElementoSiopeSpesa.getCodice()+" - "+currentElementoSiopeSpesa.getDescrizione());
		supportElementoSiopeSpesa.setCodice(currentElementoSiopeSpesa.getCodice());
		model.getListaSiopeSpesa().add(supportElementoSiopeSpesa);
	}
	
	
	private void popolaElementoSiopeEntrata(SiopeEntrata currentElementoSiopeEntrata, String idPadre) {
		GestoreDatiAlberoModel supportElementoSiopeEntrata = new GestoreDatiAlberoModel();
		if (idPadre != null && !RADICE_ALBERO.equalsIgnoreCase(idPadre)) {
			supportElementoSiopeEntrata.setpId(idPadre);
		} else {
			supportElementoSiopeEntrata.setpId(RADICE_ALBERO);
		}
		supportElementoSiopeEntrata.setId(currentElementoSiopeEntrata.getCodice());
		supportElementoSiopeEntrata.setUid(currentElementoSiopeEntrata.getUid());
		supportElementoSiopeEntrata.setName(currentElementoSiopeEntrata.getCodice()+" - "+currentElementoSiopeEntrata.getDescrizione());
		supportElementoSiopeEntrata.setCodice(currentElementoSiopeEntrata.getCodice());
		model.getListaSiopeSpesa().add(supportElementoSiopeEntrata);
	}
	
	
	
	/**
	 * SIAC 7349
	 * Servizio per il recupero delle componenti impegnabili
	 * chiamato all'interno della modale
	 * @return
	 */
	public String getComponentiBilancioCapitolo() {
		List<ImportiCapitoloPerComponente> resultList = new ArrayList<ImportiCapitoloPerComponente>();
		int uidCapitoloSelezionato=0;
		if(!isEmpty(getRadioCodiceCapitolo())){
			uidCapitoloSelezionato = Integer.valueOf(getRadioCodiceCapitolo()).intValue();
			boolean abilitaControlloStanziamento = false;//SOLO SE DA INSERISCI
			if(AZIONE_INSERISCI.equals(getAzione())){
				abilitaControlloStanziamento = true;
			}
			loadComponentiBilancioCapitolo( uidCapitoloSelezionato, resultList,abilitaControlloStanziamento);
		}
		/*
		 * COMPOSIZIONE DEL FILTRO
		 * Per la selezione delle componenti
		 */
		
		List<ImportiCapitoloPerComponente> resultListCompleto = new ArrayList<ImportiCapitoloPerComponente>();
		RicercaTipoComponenteImportiCapitolo r = new RicercaTipoComponenteImportiCapitolo();
		r.setDataOra(new Date());
		r.setAnnoBilancio(sessionHandler.getBilancio().getAnno());
		r.setRichiedente(sessionHandler.getRichiedente());
		RicercaTipoComponenteImportiCapitoloResponse respo = new RicercaTipoComponenteImportiCapitoloResponse();
		if(AZIONE_RICERCA.equals(getAzione())){
			respo = tipoComponenteImportiCapitoloService.ricercaTipoComponenteImportiCapitoloTotali(r);
		}
		//TODO
		if(AZIONE_INSERISCI.equals(getAzione())){
			respo = tipoComponenteImportiCapitoloService.ricercaTipoComponenteImportiCapitoloImpegnabili(r);
		}
		if(respo!= null && respo.getListaTipoComponenteImportiCapitolo()!= null && !respo.getListaTipoComponenteImportiCapitolo().isEmpty()){
			for(TipoComponenteImportiCapitolo tcic : respo.getListaTipoComponenteImportiCapitolo()){
				ImportiCapitoloPerComponente  icpcWrapper = new ImportiCapitoloPerComponente();
				icpcWrapper.setUidComponente(tcic.getUid());
				icpcWrapper.setTipoComponenteImportiCapitolo(tcic);
				resultListCompleto.add(icpcWrapper);
			}
			
		}
		
		model.setListaComponentiBilancioCompleta(resultListCompleto);
		
		model.setListaComponentiBilancio(resultList);
		return SUCCESS;
	}
	
	
	
	
	/*
	 * VECCHIA IMPLEMENTAZIONE CON
	 * DIPENDENZA DAL CAPITOLO
	 * 
	 */
//	public String getComponentiBilancioDaCapitolo() {
//		List<ImportiCapitoloPerComponente> resultList = new ArrayList<ImportiCapitoloPerComponente>();
//		int maxElementi = 5;
//		if(getNumCapitolo()!= null && getAnno()!= null && getArticolo()!= null){
//			CapitoloImpegnoModel capitoloDiRiferimento = new CapitoloImpegnoModel();
//			capitoloDiRiferimento.setArticolo(getArticolo());
//			capitoloDiRiferimento.setAnno(getAnno());
//			capitoloDiRiferimento.setNumCapitolo(getNumCapitolo());
//			RicercaSinteticaCapitoloUscitaGestione req = new RicercaSinteticaCapitoloUscitaGestione();
//			RicercaSinteticaCapitoloUGest ru = new RicercaSinteticaCapitoloUGest();
//			ru.setAnnoEsercizio(capitoloDiRiferimento.getAnno());
//			ru.setNumeroCapitolo(capitoloDiRiferimento.getNumCapitolo());
//			ru.setNumeroArticolo(capitoloDiRiferimento.getArticolo());
//			ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
//			parametriPaginazione.setElementiPerPagina(maxElementi);
//			parametriPaginazione.setNumeroPagina(0);
//			req.setParametriPaginazione(parametriPaginazione);
//			req.setEnte(sessionHandler.getEnte());
//			req.setRichiedente(sessionHandler.getRichiedente());
//			req.setRicercaSinteticaCapitoloUGest(ru);
//			//invoco il servizio ricercaSinteticaCapitoloUscitaGestione:
//			RicercaSinteticaCapitoloUscitaGestioneResponse res = capitoloUscitaGestioneService.ricercaSinteticaCapitoloUscitaGestione(req);
//			if(res!= null && res.getCapitoli()!= null && !res.getCapitoli().isEmpty()){
//				int uidCaptolo = res.getCapitoli().get(0).getUid();
//				model.setUidCapitolo(uidCaptolo);
//				boolean abilitaControlloStanziamento = false;//SOLO SE DA INSERISCI
//				if(AZIONE_INSERISCI.equals(getAzione())){
//					abilitaControlloStanziamento = true;
//				}
//				loadComponentiBilancioCapitolo( uidCaptolo, resultList,abilitaControlloStanziamento);
//			}
//		}		
//		
//		model.setListaComponentiBilancio(resultList);
//		
//		return SUCCESS;
//	}

	
	private void loadComponentiBilancioCapitolo(int uidCapitoloSelezionato, 
			List<ImportiCapitoloPerComponente> resultList, boolean abilitaControlloImporto){
		RicercaComponenteImportiCapitolo request = new RicercaComponenteImportiCapitolo();
		request.setDataOra(new Date());
		request.setAnnoBilancio(sessionHandler.getBilancio().getAnno());
		request.setRichiedente(sessionHandler.getRichiedente());
		request.setCapitolo(new CapitoloUscitaPrevisione());
		request.getCapitolo().setUid(uidCapitoloSelezionato);
		request.setAbilitaCalcoloDisponibilita(true);
		RicercaComponenteImportiCapitoloResponse resComponenti = componenteImportiCapitoloService.ricercaComponenteImportiCapitolo(request);
		if(resComponenti!= null){
			List<ImportiCapitoloPerComponente> importiComponentiCapitolo  = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerAnno(resComponenti.getListaImportiCapitolo());
			
			
			//SIAC-7349 - GS - Start - 28/07/2020 - Nuovo metodo per mostrare componenti nel triennio senza stanziamento
			if(resComponenti.getListaImportiCapitoloTriennioNoStanz() != null &&  !resComponenti.getListaImportiCapitoloTriennioNoStanz().isEmpty()) {
				Integer annoEsercizio = sessionHandler.getAnnoBilancio();
				importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerTriennioNoStanz(importiComponentiCapitolo, resComponenti.getListaImportiCapitoloTriennioNoStanz(), annoEsercizio);
			}
			//SIAC-7349 - End
			
			
			
			
			if(importiComponentiCapitolo!= null && !importiComponentiCapitolo.isEmpty()){
				
				Map<Integer, Integer> mapComponentiStanziamentiValidi = new HashMap<Integer, Integer>();
				for(ImportiCapitoloPerComponente icpc :importiComponentiCapitolo){
					if(icpc.getTipoComponenteImportiCapitolo()!= null){
						if(abilitaControlloImporto){
							//INSERIMENTO
							if(icpc.getTipoComponenteImportiCapitolo().getImpegnabileComponenteImportiCapitolo()!= null
									&& (ImpegnabileComponenteImportiCapitolo.SI.name().equals(icpc.getTipoComponenteImportiCapitolo().getImpegnabileComponenteImportiCapitolo().name())
									)){
								mapComponentiStanziamentiValidi.put( icpc.getUidComponente(),  icpc.getUidComponente());
								buildListaDisponibilita(mapComponentiStanziamentiValidi, icpc,resultList);
								}
						}else{
							//RICERCA
							mapComponentiStanziamentiValidi.put( icpc.getUidComponente(),  icpc.getUidComponente());
							buildListaDisponibilita(mapComponentiStanziamentiValidi, icpc,resultList);
							
						}
					}
				}
			}
		}
	}
	
	
	
	
	private void buildListaDisponibilita(Map<Integer, Integer> mapComponentiStanziamentiValidi, 
			ImportiCapitoloPerComponente icpc, List<ImportiCapitoloPerComponente> resultList){
		if(icpc.getTipoDettaglioComponenteImportiCapitolo()!= null && mapComponentiStanziamentiValidi.containsKey(icpc.getUidComponente())
				&& icpc.getTipoDettaglioComponenteImportiCapitolo().name().equals(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAIMPEGNARE.name())){
			
			if( icpc.getDettaglioAnno0()!= null && icpc.getDettaglioAnno0().getImporto()!= null){
				BigDecimal importoDisponibilita0 = icpc.getDettaglioAnno0().getImporto();
				if(importoDisponibilita0.compareTo(BigDecimal.ZERO)<0){
					icpc.getDettaglioAnno0().setDisponibilita(FinUtility.importoFormatter(BigDecimal.ZERO));
				}else{
					icpc.getDettaglioAnno0().setDisponibilita(FinUtility.importoFormatter(importoDisponibilita0));
				}
				
			}
			if( icpc.getDettaglioAnno1()!= null && icpc.getDettaglioAnno1().getImporto()!= null){
				BigDecimal importoDisponibilita1 = icpc.getDettaglioAnno1().getImporto();
				if(importoDisponibilita1.compareTo(BigDecimal.ZERO)<0){
					icpc.getDettaglioAnno1().setDisponibilita(FinUtility.importoFormatter(BigDecimal.ZERO));
				}else{
					icpc.getDettaglioAnno1().setDisponibilita(FinUtility.importoFormatter(importoDisponibilita1));
				}
				
				
			}
			if(icpc.getDettaglioAnno2()!= null && icpc.getDettaglioAnno2().getImporto()!= null){
				BigDecimal importoDisponibilita2 =icpc.getDettaglioAnno2().getImporto();
				if(importoDisponibilita2.compareTo(BigDecimal.ZERO)<0){
					icpc.getDettaglioAnno2().setDisponibilita(FinUtility.importoFormatter(BigDecimal.ZERO));
				}else{
					icpc.getDettaglioAnno2().setDisponibilita(FinUtility.importoFormatter(importoDisponibilita2));
				}
				
				
			}
			//SETTING UID 
			icpc.setUidComponente(icpc.getTipoComponenteImportiCapitolo().getUid());
			resultList.add(icpc);
		}
		
		
	}
	
	
	
	private void checkStanziamentoValido(Map<Integer, Integer> mapComponentiStanziamentiValidi, 
			ImportiCapitoloPerComponente icpc, int uidComponente){
		if((icpc.getDettaglioAnno0()!= null && icpc.getDettaglioAnno0().getImporto() != null && icpc.getDettaglioAnno0().getAnnoCompetenza() >= sessionHandler.getBilancio().getAnno() && icpc.getDettaglioAnno0().getImporto().intValue()>0)
				|| (icpc.getDettaglioAnno1()!= null && icpc.getDettaglioAnno1().getImporto() != null && icpc.getDettaglioAnno1().getAnnoCompetenza() >= sessionHandler.getBilancio().getAnno() && icpc.getDettaglioAnno1().getImporto().intValue()>0)
				|| (icpc.getDettaglioAnno2()!= null && icpc.getDettaglioAnno2().getImporto() != null && icpc.getDettaglioAnno2().getAnnoCompetenza() >= sessionHandler.getBilancio().getAnno() && icpc.getDettaglioAnno2().getImporto().intValue()>0)
				){
			mapComponentiStanziamentiValidi.put(uidComponente, uidComponente);
		}
	}
	
//	private void checkStanziamentoValido(Map<Integer, Integer> mapComponentiStanziamentiValidi, 
//			 int annoPartenzaDisponibilita, DettaglioComponenteImportiCapitolo dcic, int uidComponente){
//		
//		if(annoPartenzaDisponibilita >= sessionHandler.getBilancio().getAnno() && dcic!= null && dcic.getImporto()!= null
//				&& dcic.getImporto().intValue()>0){
//			mapComponentiStanziamentiValidi.put(uidComponente, uidComponente);
//		}
//	}
	
	
	public String getComponentiBilancioTotali() {
		List<ImportiCapitoloPerComponente> resultList = new ArrayList<ImportiCapitoloPerComponente>();
		RicercaTipoComponenteImportiCapitolo r = new RicercaTipoComponenteImportiCapitolo();
		r.setDataOra(new Date());
		r.setAnnoBilancio(sessionHandler.getBilancio().getAnno());
		r.setRichiedente(sessionHandler.getRichiedente());
		RicercaTipoComponenteImportiCapitoloResponse respo = tipoComponenteImportiCapitoloService.ricercaTipoComponenteImportiCapitoloTotali(r);
		if(respo!= null && respo.getListaTipoComponenteImportiCapitolo()!= null && !respo.getListaTipoComponenteImportiCapitolo().isEmpty()){
			for(TipoComponenteImportiCapitolo tcic : respo.getListaTipoComponenteImportiCapitolo()){
				//METTIAMO SOLO ID E DESCRIZIONE
				ImportiCapitoloPerComponente  icpcWrapper = new ImportiCapitoloPerComponente();
				icpcWrapper.setUidComponente(tcic.getUid());
				icpcWrapper.setTipoComponenteImportiCapitolo(tcic);
				resultList.add(icpcWrapper);
			}
		}
		
		model.setListaComponentiBilancio(resultList);
		return SUCCESS;
	}
	
	
	
	public String getComponentiBilancioDaCapitolo() {
		int maxElementi = 5;
		if(getNumCapitolo()!= null && getAnno()!= null && getArticolo()!= null){
			CapitoloImpegnoModel capitoloDiRiferimento = new CapitoloImpegnoModel();
			capitoloDiRiferimento.setArticolo(getArticolo());
			capitoloDiRiferimento.setAnno(getAnno());
			capitoloDiRiferimento.setNumCapitolo(getNumCapitolo());
			RicercaSinteticaCapitoloUscitaGestione req = new RicercaSinteticaCapitoloUscitaGestione();
			RicercaSinteticaCapitoloUGest ru = new RicercaSinteticaCapitoloUGest();
			ru.setAnnoEsercizio(capitoloDiRiferimento.getAnno());
			ru.setNumeroCapitolo(capitoloDiRiferimento.getNumCapitolo());
			ru.setNumeroArticolo(capitoloDiRiferimento.getArticolo());
			ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
			parametriPaginazione.setElementiPerPagina(maxElementi);
			parametriPaginazione.setNumeroPagina(0);
			req.setParametriPaginazione(parametriPaginazione);
			req.setEnte(sessionHandler.getEnte());
			req.setRichiedente(sessionHandler.getRichiedente());
			req.setRicercaSinteticaCapitoloUGest(ru);
			//invoco il servizio ricercaSinteticaCapitoloUscitaGestione:
			RicercaSinteticaCapitoloUscitaGestioneResponse res = capitoloUscitaGestioneService.ricercaSinteticaCapitoloUscitaGestione(req);
			if(res!= null && res.getCapitoli()!= null && !res.getCapitoli().isEmpty()){
				int uidCaptolo = res.getCapitoli().get(0).getUid();
				model.setUidCapitolo(uidCaptolo);
			}
		}	
		
		List<ImportiCapitoloPerComponente> resultList = new ArrayList<ImportiCapitoloPerComponente>();
		RicercaTipoComponenteImportiCapitolo r = new RicercaTipoComponenteImportiCapitolo();
		r.setDataOra(new Date());
		r.setAnnoBilancio(sessionHandler.getBilancio().getAnno());
		r.setRichiedente(sessionHandler.getRichiedente());
		RicercaTipoComponenteImportiCapitoloResponse respo = tipoComponenteImportiCapitoloService.ricercaTipoComponenteImportiCapitoloImpegnabili(r);
		if(respo!= null && respo.getListaTipoComponenteImportiCapitolo()!= null && !respo.getListaTipoComponenteImportiCapitolo().isEmpty()){
			for(TipoComponenteImportiCapitolo tcic : respo.getListaTipoComponenteImportiCapitolo()){
				//METTIAMO SOLO ID E DESCRIZIONE
				ImportiCapitoloPerComponente  icpcWrapper = new ImportiCapitoloPerComponente();
				icpcWrapper.setUidComponente(tcic.getUid());
				icpcWrapper.setTipoComponenteImportiCapitolo(tcic);
				resultList.add(icpcWrapper);
			}
		}
		
		model.setListaComponentiBilancio(resultList);
		return SUCCESS;
	
		
	}
	
	
	
	
	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */
	
	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getIdNazione() {
		return idNazione;
	}

	public void setIdNazione(String idNazione) {
		this.idNazione = idNazione;
	}

	public String getIdMacroaggregato() {
		return idMacroaggregato;
	}

	public void setIdMacroaggregato(String idMacroaggregato) {
		this.idMacroaggregato = idMacroaggregato;
	}

	public String getIdPianoDeiConti() {
		return idPianoDeiConti;
	}

	public void setIdPianoDeiConti(String idPianoDeiConti) {
		this.idPianoDeiConti = idPianoDeiConti;
	}

	public boolean isRicaricaAlberoPianoDeiConti() {
		return ricaricaAlberoPianoDeiConti;
	}

	public void setRicaricaAlberoPianoDeiConti(boolean ricaricaAlberoPianoDeiConti) {
		this.ricaricaAlberoPianoDeiConti = ricaricaAlberoPianoDeiConti;
	}

	public boolean isDaRicerca() {
		return daRicerca;
	}

	public void setDaRicerca(boolean daRicerca) {
		this.daRicerca = daRicerca;
	}

	public String getIdPianoDeiContiCapitoloPadrePerAggiorna() {
		return idPianoDeiContiCapitoloPadrePerAggiorna;
	}

	public void setIdPianoDeiContiCapitoloPadrePerAggiorna(
			String idPianoDeiContiCapitoloPadrePerAggiorna) {
		this.idPianoDeiContiCapitoloPadrePerAggiorna = idPianoDeiContiCapitoloPadrePerAggiorna;
	}

	public String getDatiUscitaImpegno() {
		return datiUscitaImpegno;
	}

	public void setDatiUscitaImpegno(String datiUscitaImpegno) {
		this.datiUscitaImpegno = datiUscitaImpegno;
	}

	public boolean isRicaricaStrutturaAmministrativa() {
		return ricaricaStrutturaAmministrativa;
	}

	public void setRicaricaStrutturaAmministrativa(
			boolean ricaricaStrutturaAmministrativa) {
		this.ricaricaStrutturaAmministrativa = ricaricaStrutturaAmministrativa;
	}

	public boolean isRicaricaSiopeSpesa() {
		return ricaricaSiopeSpesa;
	}

	public void setRicaricaSiopeSpesa(boolean ricaricaSiopeSpesa) {
		this.ricaricaSiopeSpesa = ricaricaSiopeSpesa;
	}

	public String getStruttAmmOriginale() {
		return struttAmmOriginale;
	}

	public void setStruttAmmOriginale(String struttAmmOriginale) {
		this.struttAmmOriginale = struttAmmOriginale;
	}

	/**
	 * @return the struttAmmOriginaleCompetente
	 */
	public String getStruttAmmOriginaleCompetente() {
		return struttAmmOriginaleCompetente;
	}

	/**
	 * @param struttAmmOriginaleCompetente the struttAmmOriginaleCompetente to set
	 */
	public void setStruttAmmOriginaleCompetente(String struttAmmOriginaleCompetente) {
		this.struttAmmOriginaleCompetente = struttAmmOriginaleCompetente;
	}

	/**
	 * @return the radioCodiceCapitolo
	 */
	public String getRadioCodiceCapitolo() {
		return radioCodiceCapitolo;
	}

	/**
	 * @param radioCodiceCapitolo the radioCodiceCapitolo to set
	 */
	public void setRadioCodiceCapitolo(String radioCodiceCapitolo) {
		this.radioCodiceCapitolo = radioCodiceCapitolo;
	}
	
	

	
	
	public Integer getNumCapitolo() {
		return numCapitolo;
	}

	/**
	 * @param numCapitolo the numCapitolo to set
	 */
	public void setNumCapitolo(Integer numCapitolo) {
		this.numCapitolo = numCapitolo;
	}

	/**
	 * @return the anno
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @return the articolo
	 */
	public Integer getArticolo() {
		return articolo;
	}

	/**
	 * @param anno the anno to set
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	/**
	 * @param articolo the articolo to set
	 */
	public void setArticolo(Integer articolo) {
		this.articolo = articolo;
	}

	/**
	 * @return the azione
	 */
	public String getAzione() {
		return azione;
	}

	/**
	 * @param azione the azione to set
	 */
	public void setAzione(String azione) {
		this.azione = azione;
	}

	
	
	
	
	
}

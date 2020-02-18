/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ajax;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiConti;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiContiResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiope;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiopeResponse;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabile;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabileResponse;
import it.csi.siac.siacfinapp.frontend.ui.action.GenericFinAction;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.ajax.AjaxModel;
import it.csi.siac.siacfinapp.frontend.ui.model.commons.GestoreDatiAlberoModel;
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
	
	private boolean ricaricaStrutturaAmministrativa = true;
	private boolean ricaricaSiopeSpesa = true;
			
	private boolean daRicerca = false;
	
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
			lpdc.setAnno(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
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
				leggiTreeSiope.setAnno(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
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
		lpdc.setAnno(Integer.valueOf(sessionHandler.getAnnoEsercizio()));
		lpdc.setIdEnteProprietario(sessionHandler.getEnte().getUid());
		lpdc.setRichiedente(sessionHandler.getRichiedente());
		if (idMacroaggregato != null){
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
	
}

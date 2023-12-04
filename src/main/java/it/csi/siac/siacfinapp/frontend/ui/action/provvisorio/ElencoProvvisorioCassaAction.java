/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.provvisorio;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabile;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabileResponse;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSacProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSacProvvisoriDiCassaResponse;
import it.csi.siac.siacfinser.model.StrutturaAmministrativoContabileFlat;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ElencoProvvisorioCassaAction extends WizardRicercaProvvisorioAction{
	private static final long serialVersionUID = 1L;
	
	private String parameterPageName, parameterPageValue;

	@Override
	public void prepare() throws Exception{
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		this.model.setTitolo("Elenco provvisori cassa");
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception{
		
		//leggo l'elenco delle sac:
		Map<Integer, StrutturaAmministrativoContabileFlat> elencoSac = readElencoStruttureAmministrativoContabili();
		
		//setto l'elenco nel model:
		model.setElencoStruttureAmministrativoContabili(elencoSac);

		// se la ricerca torna degli errori torno sulla form di ricerca
		if (executeRicercaProvvisoriCassa().equals(INPUT)){
			return "gotoErroreRicerca";
		}
			
		//setto i dati nel model:
		initDataForProvvisoriCassa();

		return SUCCESS;
	}

	public String aggiornaSac() throws Exception {
		AggiornaSacProvvisoriDiCassa req = new AggiornaSacProvvisoriDiCassa();

		req.setElencoProvvisoriDiCassa(model.getProvvisoriSacSel());
		req.setEnte(sessionHandler.getEnte());
		req.setRichiedente(sessionHandler.getRichiedente());

		AggiornaSacProvvisoriDiCassaResponse res = provvisorioService.aggiornaSacProvvisoriDiCassa(req);

		if (res.isFallimento()) {
			addErrori(res);
			return INPUT;
		}

		addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore().getTesto());
		
		return "elencoProvvisorioCassa";
	}

	/**
	 * legge le sac
	 * @return
	 */
	private Map<Integer, StrutturaAmministrativoContabileFlat> readElencoStruttureAmministrativoContabili(){
		
		//istanzio la request per il servizio:
		LeggiStrutturaAmminstrativoContabile request = new LeggiStrutturaAmminstrativoContabile();

		request.setAnno(sessionHandler.getAnnoBilancio());
		request.setIdEnteProprietario(sessionHandler.getEnte().getUid());
		request.setRichiedente(sessionHandler.getRichiedente());

		//invoco il servizio:
		LeggiStrutturaAmminstrativoContabileResponse response = classificatoreService.leggiStrutturaAmminstrativoContabile(request);

		return getAllStrutture(response.getListaStrutturaAmmContabile(), null);
	}

	/**
	 * si occupa di mettere in una mappa l'elencoStruttureAmministrativoContabili ricevute.
	 * l'id e' chiave di mappatura.
	 * @param elencoStruttureAmministrativoContabili
	 * @param codicePadre
	 * @return
	 */
	private Map<Integer, StrutturaAmministrativoContabileFlat> getAllStrutture(List<StrutturaAmministrativoContabile> elencoStruttureAmministrativoContabili, String codicePadre){
		
		//istanzio la mappa:
		Map<Integer, StrutturaAmministrativoContabileFlat> map = new LinkedHashMap<Integer, StrutturaAmministrativoContabileFlat>();

		//itero l'elenco e popolo la mappa:
		for (StrutturaAmministrativoContabile sac : elencoStruttureAmministrativoContabili){
			StrutturaAmministrativoContabileFlat sacFlat = new StrutturaAmministrativoContabileFlat(sac, codicePadre);
			map.put(sac.getUid(), sacFlat);
			map.putAll(getAllStrutture(sac.getSubStrutture(), sacFlat.getCodice()));
		}

		//restituisco la mappa appea creata:
		return map;
	}

	/**
	 * Inizializza i dati dei provviosori di cassa nel model
	 */
	private void initDataForProvvisoriCassa(){
		model.resetDescrSacSel();
		if (model.getElencoProvvisoriCassa() != null){
			for (ProvvisorioDiCassa pc : model.getElencoProvvisoriCassa()){
				if (!isUtenteAmministratore()){
					setDescrSacSel(pc);
				}
				setProvvisorioConSacUtente(pc);
			}
		}
	}

	private void setProvvisorioConSacUtente(ProvvisorioDiCassa pc){
		StrutturaAmministrativoContabile sac = pc.getStrutturaAmministrativoContabile();
		Map<Integer, StrutturaAmministrativoContabile> elencoSacUtente = getElencoStruttureAmministrativoContabiliUtente();
		model.addProvvisoriConSacUtente(sac != null ? elencoSacUtente.containsKey(sac.getUid()) : false);
	}

	private void setDescrSacSel(ProvvisorioDiCassa pc){
		StrutturaAmministrativoContabile sac = pc.getStrutturaAmministrativoContabile();
		if (sac != null){
			model.addDescrSacSel(sac.getUid());
		} else {
			model.getDescrSacSel().add(null);
		}
	}
	
	public String getParameterPageName() {
		return parameterPageName;
	}

	public void setParameterPageName(String parameterPageName) {
		this.parameterPageName = parameterPageName;
	}

	public String getParameterPageValue() {
		return StringUtils.defaultIfBlank(parameterPageValue, "1") ;
	}

	public void setParameterPageValue(String parameterPageValue) {
		this.parameterPageValue = parameterPageValue;
	}
	 
}
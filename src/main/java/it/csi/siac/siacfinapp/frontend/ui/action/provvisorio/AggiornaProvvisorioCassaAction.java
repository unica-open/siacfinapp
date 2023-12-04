/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.provvisorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiaveResponse;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.RicercaProvvisorioDiCassaK;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaProvvisorioCassaAction extends WizardAggiornaProvvisorioAction {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		this.model.setTitolo("Aggiorna provvisorio di cassa");
	}	
	
	/**
	 * execute della action
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {

		if(teSupport==null){
			//se nulla istanziamo una nuova transazione elementare:
			pulisciTransazioneElementare();
		}
		
		//Pulisco i campi 
	   	pulisciCampi();
	   	
	   	if (sessionHandler.getAnnoEsercizio() != null && !"".equalsIgnoreCase(sessionHandler.getAnnoEsercizio())) {
			model.setAnnoProvvisorio(new Integer(sessionHandler.getAnnoEsercizio()));
		}
	   	
	   	//Prepariamo la request per il servizio di ricerca del provvisiorio di cassa:
		RicercaProvvisorioDiCassaPerChiave reqRicercaPerChiave = componiRequestRicercaProvvisorioDiCassaPerChiave();
		
		//Invochiamo il servizio:
		RicercaProvvisorioDiCassaPerChiaveResponse respRicerca = provvisorioService.ricercaProvvisorioDiCassaPerChiave(reqRicercaPerChiave);
	   	
		//Analizziamo la risposta ottenuta:
		if(isFallimento(respRicerca)){
			//ci sono errori
			addErrori("executeInserisciProvvisorioCassa", respRicerca);
			return INPUT;
		}
	   	
	   	//set radio tipo
	   	model.getTipoDocumentoProvList().add("Entrata");
	   	model.getTipoDocumentoProvList().add("Spesa");
	   	
	   	ProvvisorioDiCassa provvisorioDiCassa = respRicerca.getProvvisorioDiCassa();
	   	
	   	impostaDatiNeiCampi(clone(provvisorioDiCassa));
	   	
	   	//
	   	model.setProvvisorioDiCassaInAggiornamento(clone(provvisorioDiCassa));
	   	
	   	teSupport.setRicaricaStrutturaAmministrativa(true);
	   	if(provvisorioDiCassa.getStrutturaAmministrativoContabile()!=null){
	   		teSupport.setStruttAmmOriginale(Integer.toString(provvisorioDiCassa.getStrutturaAmministrativoContabile().getUid()));
		} else {
			teSupport.setStruttAmmOriginale("-1");
		}
	   	
	   	
	   	return SUCCESS;
	}
	
	/**
	 * Crea la request per richiamare il servizio di ricerca del provvisiorio
	 * @return
	 */
	private RicercaProvvisorioDiCassaPerChiave componiRequestRicercaProvvisorioDiCassaPerChiave(){
	   	RicercaProvvisorioDiCassaPerChiave reqRicercaPerChiave = new RicercaProvvisorioDiCassaPerChiave();
	   	
	   	//bilancio
	   	reqRicercaPerChiave.setBilancio(sessionHandler.getBilancio());
	   	
	   	//ente
	   	reqRicercaPerChiave.setEnte(sessionHandler.getEnte());
	   	
	   	//richiedente
	   	reqRicercaPerChiave.setRichiedente(sessionHandler.getRichiedente());
	   	
	   	//parametro provvisiorio:
	   	RicercaProvvisorioDiCassaK pRicercaProvvisorioK = new RicercaProvvisorioDiCassaK();
	   	//anno provvisorio:
	   	pRicercaProvvisorioK.setAnnoProvvisorioDiCassa(new Integer(annoProvv));
		//numero provvisorio:
	   	pRicercaProvvisorioK.setNumeroProvvisorioDiCassa(new Integer(numeroProvv));
	   	
	   	//Tipo provvisorio:
	   	TipoProvvisorioDiCassa tipoProvvisorioDiCassa = TipoProvvisorioDiCassa.S;
	   	if(TipoProvvisorioDiCassa.E.toString().equals(tipoProvv)){
	   		tipoProvvisorioDiCassa = TipoProvvisorioDiCassa.E;
	   	}
		pRicercaProvvisorioK.setTipoProvvisorioDiCassa(tipoProvvisorioDiCassa);
		
		//setto il parametro nella request:
		reqRicercaPerChiave.setpRicercaProvvisorioK(pRicercaProvvisorioK);
		
		//ritorno la request al chiamante:
		return reqRicercaPerChiave;
	}
	
	public void primaDiAggiornaProvvisorioDiCassa(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String struttSel = request.getParameter("struttAmmSelezionata");
		model.setStrutturaSelezionataSuPagina(struttSel);
	}
	
	public String aggiornaProvvisorioDiCassa(){
		List<Errore> listaErrori = new ArrayList<Errore>();

		//Dati obbligatori:
	    listaErrori = controlliObbligatorietaProvvisorioDiCassa(listaErrori);								
	
		//VALIDO
		if(listaErrori.isEmpty()){			
			executeAggiornaProvvisorioCassa();
		} else {
			addErrori(listaErrori);
			return INPUT;
		}	
		
		return "elencoProvvisorioCassa";
	}	
	

	@Override
	protected List<Errore> controlliObbligatorietaProvvisorioDiCassa(List<Errore> listaErrori ){
		super.controlliObbligatorietaProvvisorioDiCassa(listaErrori);
		
		if(model.getTipoDocumentoProv().equalsIgnoreCase("Entrata")) {
			
			if(model.getAccettato() == null && 
					(isUtenteDecentrato() ||  
					StringUtils.isNotEmpty(model.getDataPresaInCaricoServizio()) || 
					StringUtils.isNotEmpty(model.getDataRifiutoErrataAttribuzione()))) {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("accettato"));
			}

			if(Boolean.TRUE.equals(model.getAccettato()) && StringUtils.isEmpty(model.getDataPresaInCaricoServizio())) {
				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("data presa in carico dal Servizio"));
			}
			
			if(Boolean.FALSE.equals(model.getAccettato())) {
				if (StringUtils.isEmpty(model.getDataRifiutoErrataAttribuzione())) {
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("data rifiuto per errata attribuzione"));
				}

				if (isUtenteDecentrato() && StringUtils.isBlank(model.getNote())) {
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("note"));
				}
			}
		}
		
		return listaErrori;
	}

	
	/**
	 * inserisce il valore di default nella chekboxList della pagina
	 * @return
	 */
	public String[] getDefaultValueAnnulla(){
		return new String [] {"no"};
	}

	public boolean isProvvisorioConSacUtente()
	{
		Map<Integer, StrutturaAmministrativoContabile> elencoSacUtente = getElencoStruttureAmministrativoContabiliUtente();

		StrutturaAmministrativoContabile sac = model.getProvvisorioDiCassaInAggiornamento()
				.getStrutturaAmministrativoContabile();

		return sac != null ? elencoSacUtente.containsKey(sac.getUid()) : false;

	}
	
}

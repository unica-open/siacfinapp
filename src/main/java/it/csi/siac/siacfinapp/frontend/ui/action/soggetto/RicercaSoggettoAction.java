/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.dispatcher.Parameter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.util.ValidationUtils;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNome;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNomeResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import xyz.timedrain.arianna.plugin.BreadCrumb;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaSoggettoAction extends WizardRicercaSoggettoAction{
	
	private static final long serialVersionUID = 5337260587632331114L;

	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		//carico le liste:
		caricaListePerRicercaSoggetto();
		//setto il titolo:
		//task-224
		if(AzioneConsentitaEnum.OP_CEC_SOG_leggiSogg.getNomeAzione().equals(sessionHandler.getAzione().getNome())) {
			this.model.setTitolo("Ricerca Soggetto Cassa Economale");
		}else {
			this.model.setTitolo("Ricerca Soggetto");	
		}
	}	
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		
		//puliamo i campi del model:
		pulisciCampi();
		
		//Imposto i valori di default		
		model.setIdStato(STATO_VALIDO);
		
		//rimando alla execute della super classe:
	    return super.execute();
	}    
	
	
	@Override
	public boolean isBottoneIndietroDiTipoSecondBtn() {
		return true;
	}
	
	/**
	 * pulisce i campi del model
	 */
	private void pulisciCampi(){
		//puliamo i campi:
		model.setCodice(null);
		model.setCodiceFiscale(null);
		model.setCodiceFiscaleEstero(null);
		model.setDenominazione(null);
		model.setIdTipoNatura(null);
		model.setIdNaturaGiuridica(null);
		model.setIdStato(null);
		model.setSesso(null);
		model.setComune("");
		model.setIdComune("");
		model.setIdClasse(null);
		model.setPartitaIva("");
		//SIAC-6565-CR1215
		model.setEmailPec(null);
		model.setCodDescrizione(null);
	}
	
	/**
	 * effettua la ricerca
	 * @return
	 * @throws Exception
	 */
	public String cerca() throws Exception {
		
		List<String>  listaWarnings = new ArrayList<String>();
		
		setMethodName("cerca");
		
		//setto il codice fiscale
		model.setCodiceFiscale(model.getCodiceFiscale().toUpperCase());
		
		if (model.getCodice() == null) {
			if (model.getIdNaturaGiuridica() != null && model.getIdNaturaGiuridica() != -1){
				if (model.getDenominazione().toString().equals("")){
					//denominazione assente
					addActionError("Inserisci la Denominazione");
					return INPUT;
				}
			}
			if (model.getSesso() != null || !model.getComune().isEmpty()){
				//task-131
				//String[] idComune = (String[]) getRequest().get("idComune");
				Parameter idComune = getRequest().get("idComune");
				//if (idComune == null || "".equalsIgnoreCase(idComune[0])){
				if (idComune == null || "".equalsIgnoreCase(idComune.getValue())){	
					//preparo la request per il servizio findComunePerNome:
					ListaComunePerNome comunePerNome = new ListaComunePerNome();
					comunePerNome.setCodiceNazione(model.getIdNazione().toString());
					comunePerNome.setNomeComune(model.getComune());
					comunePerNome.setRichiedente(sessionHandler.getRichiedente());
					comunePerNome.setRicercaPuntuale(true);
					
					//invoco il servizio findComunePerNome:
					ListaComunePerNomeResponse response = genericService.findComunePerNome(comunePerNome);
					
					//analizzo la risposta del servizio:
					if (response != null && response.getListaComuni() != null && response.getListaComuni().size() > 0 && response.getListaComuni().get(0).getCodice() != null){
						//trovato comune
						model.setIdComune(response.getListaComuni().get(0).getCodice());
					} else {
						//comune non trovato
						model.setComune("");
					}
				}

				if (model.getDenominazione().toString().equals("")){
					//denominazione assente
					addActionError("Inserisci la Denominazione");
					return INPUT;
				}
				if (model.getDenominazione().length() < 3){
					//denominazione troppo corta
					addActionError("Inserisci almeno 3 caratteri per la Denominazione");
					return INPUT;
				}
			}

		}	
		
		List<Errore> listaErroriPerValidazione = new ArrayList<Errore>();
		
		//validiamo i dati:
		ValidationUtils.validaRicercaSoggetto(listaErroriPerValidazione, listaWarnings, model);
		
		
		if(null!=model.getIdNazione() && null!=model.getComune() &&  StringUtils.isNotEmpty(model.getComune()) && 
		   (null==model.getIdComune() || StringUtils.isEmpty(model.getIdComune()))){
			
			//entro qui perche si e' perso l'hidden del comune
			
			//info per diagnostica:
			log.debug(methodName, "entro qui perche si e' perso l'hidden del comune");
			
			//istanzio la request per il servizio:
			ListaComunePerNome lcn = new ListaComunePerNome();
			lcn.setCodiceNazione(model.getIdNazione().toString());
			lcn.setNomeComune(model.getComune());
			lcn.setRicercaPuntuale(true);
			lcn.setRichiedente(sessionHandler.getRichiedente());
			
			//invoco il servizio:
			ListaComunePerNomeResponse listaResp = genericService.findComunePerNome(lcn);
			
			//leggiamo i dati ottenuti:
			List<CodificaFin> arrayComuni = listaResp.getListaComuni();
			
			if(null!=arrayComuni){
			  // prendo il primo elemeno
			  CodificaFin codificaFin = arrayComuni.get(0);
			  if(codificaFin.getId() == null){
				  //dato omesso
				  addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Comune"));
				  return INPUT;
			  }
			  model.setIdComune(codificaFin.getId().toString());
			} 
			
		}
		
		if(!listaErroriPerValidazione.isEmpty()) {
			//errori
			addErrori(listaErroriPerValidazione);
			return INPUT;
		} else {		
			//aggiungo i warning:
			for (String w : listaWarnings){
				addPersistentActionWarning(w);
			}
			return GOTO_ELENCO_SOGGETTI;
		}
		
	}
	
	public String annulla() {
		//puliamo i campi
		model.setCodice(null);
		model.setCodiceFiscale(null);
		model.setCodiceFiscaleEstero(null);
		model.setPartitaIva(null);
		model.setDenominazione(null);
		model.setIdStato(STATO_VALIDO);
		model.setIdNaturaGiuridica(ValidationUtils.RICERCA_VUOTA);
		model.setSesso(null);
		model.setComune(null);
		model.setIdComune(null);
		model.setProvinciaNascita(null);
		model.setIdClasse(ValidationUtils.RICERCA_VUOTA);
		//SIAC-6565-CR1215
		model.setEmailPec(null);
		model.setCodDescrizione(null);
		return SUCCESS;
	}
	
	private void caricaListePerRicercaSoggetto() {
		//Creazione della lista relativa al sesso
		if (model.getListaSesso() == null || model.getListaSesso().size() == 0) {
			model.setListaSesso(new ArrayList<String>());
			model.getListaSesso().add(CostantiFin.SESSO_M);
			model.getListaSesso().add(CostantiFin.SESSO_F);
		}
		//Caricamento liste dinamiche
		caricaListeRicercaSoggetto();
	}

}

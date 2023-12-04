/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.Normalizzatore;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.IndirizzoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.RecapitoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.ValidationUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggettoProvvisorio;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNomeResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.soggetto.Contatto;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.Sesso;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaRecapitiAction extends AggiornaSoggettoGenericAction { 
	
	private static final long serialVersionUID = -8867672287087797785L;
	
	private String pressedButton;
	private String action;
	private String idIndirizzo;
	private String idIndirizzoProvvisorio;
	
	private static final String GOTO_ELENCO_SOGGETTI ="gotoElencoSoggetti";
	
	@Autowired
	private transient Normalizzatore recapitoContattiNormalizzatore;
	
	/**
	 * Metodo prepare della action
	 */
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		//task-224
	    if(AzioneConsentitaEnum.OP_CEC_SOG_leggiSogg.getNomeAzione().equals(sessionHandler.getAzione().getNome())) {
	    	this.model.setTitolo("Aggiorna recapiti Cassa Economale");
		}else {
			this.model.setTitolo("Aggiorna recapiti");
		}
		// carico le liste della pagina
		caricaListeInserisciIndirizzoSoggetto();
	}	
	
	/**
	 * funzione interna di eliminazione del indirizzo
	 * @param idDaEliminare
	 */
	private void eliminaIndirizzo(String idDaEliminare) {
		setMethodName("eliminaIndirizzo");
		if (model.getDettaglioSoggetto() != null && model.getDettaglioSoggetto().getIndirizzi() != null
				&& !model.getDettaglioSoggetto().getIndirizzi().isEmpty()){
			//ciclo sugli indirizzi per cercare quello in questione
			for (IndirizzoSoggetto currentIndirizzo : model.getDettaglioSoggetto().getIndirizzi()){
				if (idDaEliminare.equalsIgnoreCase(String.valueOf(currentIndirizzo.getIndirizzoId()))){
					//trovato, lo rimuovo:
					model.getDettaglioSoggetto().getIndirizzi().remove(currentIndirizzo);
					break;
				}
			}
		}
	}
	
	/**
	 * Metodo execute della action
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		
		
		if (StringUtils.isNotEmpty(getPressedButton())){
			
			//vediamo se e' stato premuto il pulsante elimina:
			if ("eliminaIndirizzo".equals(pressedButton)) {
				if (idIndirizzo != null){
					//indirizzo normale
					eliminaIndirizzo(idIndirizzo);
				} else if (idIndirizzoProvvisorio != null){
					//indirizzo provvisorio
					eliminaIndirizzo(idIndirizzoProvvisorio);
				}
			}
			
			if(getPressedButton().indexOf("uid")!=-1) {
				String uidContattoDaEliminare = (String) getPressedButton().substring(getPressedButton().indexOf("uid") + 4);
				if (model.getDettaglioSoggetto() != null && model.getDettaglioSoggetto().getContatti() != null && model.getDettaglioSoggetto().getContatti().size() > 0) {
					for (Contatto currentContatto : model.getDettaglioSoggetto().getContatti()) {
						if (currentContatto.getUidProvvisiorio() != null && !"".equalsIgnoreCase(currentContatto.getUidProvvisiorio())) {
							if (uidContattoDaEliminare.equalsIgnoreCase(currentContatto.getUidProvvisiorio())) {
								 model.getDettaglioSoggetto().getContatti().remove(currentContatto);
								 break;
							}
						} else {
							if (uidContattoDaEliminare.equalsIgnoreCase(String.valueOf(currentContatto.getUid()))) {
								 model.getDettaglioSoggetto().getContatti().remove(currentContatto);
								 break;
							}
						}
					}
				}
			}
		}
		
		//puliamo gli indirizzi:
		pulisciIndirizzi();
		
		//richiamiamo la execute della super classe:
		return super.execute();
	}
	
	/**
	 * Metodo che pulisce l'indirizzo dal model
	 * @return
	 */
	public String pulisciIndirizzi(){
		setMethodName("pulisciIndirizzi");
		indirizzoSoggetto = new IndirizzoSoggetto();
		model.setIndirizzo(new IndirizzoModel());
		indirizzoSoggetto.setCodiceNazione(WebAppConstants.CODICE_ITALIA);
		return "pulisciIndirizzo";
	}
	
	/**
	 * Metodo che pulisce il contatto dal model
	 * @return
	 */
	public String pulisciContatto(){
		setMethodName("pulisciContatto");
		model.setRecapito(new RecapitoModel());
		return SUCCESS;
	}
	
	/**
	 * Pulisci campi dal modal.
	 * @return
	 */
	public String pulisciCampi(){
		setMethodName("pulisciCampi");
		indirizzoSoggetto = new IndirizzoSoggetto();
		model.setRecapito(new RecapitoModel());
		return SUCCESS;
	}
	
	/**
	 * controllo avviso
	 * 
	 * @param valore
	 * @return
	 */
	public boolean controlloAvviso(String valore){
		if(valore !=null && valore.equalsIgnoreCase("true")){
			return true;
		}
		return false;
	}
	
	/**
	 * salvataggio del recapito
	 * 
	 * @return
	 */
	public String salvaRecapito() {
		setMethodName("salvaRecapito");
		model.getRecapito().setListaRecapitoModo(model.getListaRecapitoModo());
		List<Contatto> l = recapitoContattiNormalizzatore.getModelNormalizzato(model.getRecapito(), Contatto.class);
		if (model.getDettaglioSoggetto().getContatti() == null) {
			model.getDettaglioSoggetto().setContatti(new ArrayList<Contatto>());
		}
		outer:
		for (Contatto c : l) {
			for (CodificaFin cef : model.getListaRecapitoModo()) {
				if (cef.getCodice().equals(c.getContattoCodModo())) {
					c.setDescrizioneModo(cef.getDescrizione());
					c.setUidProvvisiorio(PROVVISORIO + idProvvisorio++);
					for (Contatto contattoPresente : model.getDettaglioSoggetto().getContatti()) {
						if (c.getContattoCodModo().equals(contattoPresente.getContattoCodModo())) {
							//trovato
							model.getDettaglioSoggetto().getContatti().remove(contattoPresente);
							break;
						} 
					}
					model.getDettaglioSoggetto().getContatti().add(c);
					continue outer;
				}
			}
		}
		model.getRecapito().setCheckAvvisoPec("");
		model.getRecapito().setCheckAvvisoEmail("");
		return SUCCESS;
	}	
	
	/**
	 * salvataggio indirizzo
	 * @return
	 */
	public String salvaIndirizzo(){
		setMethodName("salvaIndirizzo");
		
		//inizializzo la lista errori:
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		if ("aggiornaIndirizzo".equals(pressedButton)){
			eliminaIndirizzo(idIndirizzo);
		}
		
		ValidationUtils.validaCampiAggiornamentoIndirizzi(listaErrori, getIndirizzoSoggetto());
				
		if(null!=getIndirizzoSoggetto() &&
			StringUtils.isEmpty(getIndirizzoSoggetto().getCodiceIstatComune())){
			
			debug(methodName, "verifico la nazione ");
			
			if(WebAppConstants.CODICE_ITALIA.equals(getIndirizzoSoggetto().getCodiceNazione())){
				ListaComunePerNomeResponse comunePerNomeResponse = controlloPuntualeComune(getIndirizzoSoggetto().getComune(), getIndirizzoSoggetto().getCodiceNazione());
				if (comunePerNomeResponse == null || comunePerNomeResponse.isFallimento() || comunePerNomeResponse.getListaComuni() == null || comunePerNomeResponse.getListaComuni().size() == 0 || comunePerNomeResponse.getListaComuni().get(0).getId() == null) {
					listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Comune"));
				} else {
					getIndirizzoSoggetto().setCodiceIstatComune(comunePerNomeResponse.getListaComuni().get(0).getCodice());
					getIndirizzoSoggetto().setComune(comunePerNomeResponse.getListaComuni().get(0).getDescrizione());
				}
			}		
		}
		
		
		// catturo gli errori
		if(!listaErrori.isEmpty()) {
			addErrori(listaErrori);
			return INPUT;
		} 
		
		//aggiungo indirizzo alla lista
		debug(methodName, "SONO QUI "+model.getDettaglioSoggetto().getIndirizzi());
		indirizzoSoggetto.setIndirizzoIdProvvisorio(PROVVISORIO + idProvvisorio++);
		
		// setto a zero id cosi sul lato service va in inserimento
		indirizzoSoggetto.setIndirizzoId(0);
		
		if (model.getDettaglioSoggetto().getIndirizzi() == null) {
			model.getDettaglioSoggetto().setIndirizzi(new ArrayList<IndirizzoSoggetto>());
		}
		
		
		// ciclo per inserire il tipo di indirizzo come descrizione nella tabellina
		// degli indirizzi
		Iterator<CodificaFin> itTipoSede = 	model.getListaTipoIndirizzoSede().iterator();
		while (itTipoSede.hasNext()){
			CodificaFin t = itTipoSede.next();
			
			if(t.getCodice().toString().equals(indirizzoSoggetto.getIdTipoIndirizzo())){
				indirizzoSoggetto.setIdTipoIndirizzoDesc(t.getDescrizione());
			}
		}
		 
		
		// check
		if(indirizzoSoggetto.isCheckAvviso()){
			//avviso true
		  indirizzoSoggetto.setAvviso("true");
		}
		
		
		if (indirizzoSoggetto.isCheckPrincipale()) {
			for (IndirizzoSoggetto i : model.getDettaglioSoggetto().getIndirizzi()){
				if ("true".equalsIgnoreCase(i.getPrincipale())) {
					i.setPrincipale("");
					addActionWarning("Attenzione: l'indirizzo e' stato selezionato come principale e andra' a sostituire quello precedente ");
					break;
				}
			}
			//SETTO PRINCIPALE A TRUE:
			indirizzoSoggetto.setPrincipale("true");
		} else {
			//SETTO PRINCIPALE A FALSE:
			indirizzoSoggetto.setCheckPrincipale(false);
			indirizzoSoggetto.setPrincipale("");
		}
			
		model.getDettaglioSoggetto().getIndirizzi().add(indirizzoSoggetto);
		
		//puliamo gli indirizzi:
		pulisciIndirizzi();
		
	    return SUCCESS;	
	}	
	
	/**
	 * meotodo di salvataggio
	 * @return
	 */
	public String salva(){
		setMethodName("salva");
		
		// inserisco il codice tipo persona
		model.getDettaglioSoggetto().getTipoSoggetto().setCodice(model.getDettaglioSoggetto().getTipoSoggetto().getSoggettoTipoCode());
		model.getDettaglioSoggetto().setSediSecondarie(model.getListaSecondariaSoggetto());
		if (model.getDettaglioSoggetto().getSessoStringa() != null && !"".equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa())) {
			if (CostantiFin.MASCHIO.equalsIgnoreCase(model.getDettaglioSoggetto().getSessoStringa().toUpperCase())) {
				model.getDettaglioSoggetto().setSesso(Sesso.MASCHIO);
			} else {
				model.getDettaglioSoggetto().setSesso(Sesso.FEMMINA);
			}
		}
		
		//compongo la request per il servizio di aggiornamento:
		AggiornaSoggetto aggiornaSoggetto = convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(),false);
		
		//SIAC-6847
		//valorizzo la codificaAmbito
		aggiornaSoggetto.setCodificaAmbito(model.getDettaglioSoggetto().getCodificaAmbito());
		
		AggiornaSoggettoResponse response = null; 
		//SIAC-6847
		//a seguito della SIAC-5722 i soggetti di ambito CEC non devono seguire le operazioni legate al soggetto provvisorio
		//eseguo il controllo che non siano di AMBITO_CEC
		if (AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite()) 
				&& (aggiornaSoggetto.getCodificaAmbito() != null 
				&& aggiornaSoggetto.getCodificaAmbito().equals("AMBITO_CEC") == false)) {
			//CASO PROVVISORIO
			AggiornaSoggettoProvvisorio asp = new AggiornaSoggettoProvvisorio(aggiornaSoggetto);
			response = soggettoService.aggiornaSoggettoProvvisorio(asp);
		}else{
			//CASO NORMALE
			aggiornaSoggetto.setCodificaAmbito(getCodificaAmbito());
			response = soggettoService.aggiornaSoggetto(aggiornaSoggetto);
		}
		
		//analizzo la response del servizio:
		if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
			debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			
			debug(methodName, "Model: " + model);
			
			return INPUT;
		}
		
		return GOTO_ELENCO_SOGGETTI;
	}
	
	
	protected String getCodificaAmbito() {
		// default AMBITO_FIN
		return null;
	}

	public String annullaRecapito(){
		model.getDettaglioSoggetto().setIndirizzi(clone(model.getSoggettoPerAnnulla().getIndirizzi()));
		model.getDettaglioSoggetto().setContatti(clone(model.getSoggettoPerAnnulla().getContatti()));
		return INPUT;
	}

	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */
	
	public String getPressedButton() {
		return pressedButton;
	}

	public void setPressedButton(String pressedButton) {
		this.pressedButton = pressedButton;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public String getIdIndirizzo()
	{
		return idIndirizzo;
	}

	public void setIdIndirizzo(String idIndirizzo)
	{
		this.idIndirizzo = idIndirizzo;
	}

	public String getIdIndirizzoProvvisorio()
	{
		return idIndirizzoProvvisorio;
	}

	public void setIdIndirizzoProvvisorio(String idIndirizzoProvvisorio)
	{
		this.idIndirizzoProvvisorio = idIndirizzoProvvisorio;
	}
	
}

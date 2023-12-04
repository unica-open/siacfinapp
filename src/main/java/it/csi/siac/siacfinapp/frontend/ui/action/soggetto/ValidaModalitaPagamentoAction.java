/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;

import java.util.ArrayList;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggettoProvvisorio;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaModalitaPagamentoInModifica;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.Sesso;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto.TipoAccredito;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ValidaModalitaPagamentoAction extends AggiornaSoggettoGenericAction{

	private static final long serialVersionUID = -1297603438461728232L;

	private String mdpId;
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggettoOut;
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggettoToValidate;
	
	
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		this.model.setTitolo("Valida Pagamento");
	}	
	
	@Override
	@BreadCrumb("Valida Pagamento")
	public String execute() throws Exception{
		setMethodName("execute");
		
		String uIdModalitaToValidate = getMdpId();
		if(uIdModalitaToValidate != null){
			
			//istanzio la request per il servizio ricercaModalitaPagamentoPerChiave:
			RicercaModalitaPagamentoPerChiave req = convertiModelPerChiamataServizioRicercaModalitaPagamentoPerChiave(uIdModalitaToValidate);
			
			//invoco il servizio ricercaModalitaPagamentoPerChiave:
			RicercaModalitaPagamentoPerChiaveResponse response = soggettoService.ricercaModalitaPagamentoPerChiave(req);
			
			//Analizzo la risposta del servizio:
			if(response != null){
				if(response.getModalitaPagamentoSoggettoInModifica() != null){
					modalitaPagamentoSoggettoOut = response.getModalitaPagamentoSoggetto();
					modalitaPagamentoSoggettoToValidate = response.getModalitaPagamentoSoggettoInModifica();
				} else {
					modalitaPagamentoSoggettoOut = response.getModalitaPagamentoSoggetto();
				}
			} else {
				return "backMod";
			}
			
			
		} else {
			return "backMod";
		}
		
		setMdpId(uIdModalitaToValidate);
		
		return SUCCESS;
	}
	
	public String validaProposta(){
		setMethodName("validaProposta");
		
		String uIdModalitaToValidate = getMdpId();
		
		//istanzio la request per il servizio ricercaModalitaPagamentoPerChiave:
		RicercaModalitaPagamentoPerChiave req = convertiModelPerChiamataServizioRicercaModalitaPagamentoPerChiave(uIdModalitaToValidate);
		
		//invoco il servizio ricercaModalitaPagamentoPerChiave:
		RicercaModalitaPagamentoPerChiaveResponse response = soggettoService.ricercaModalitaPagamentoPerChiave(req);
		
		//Analizzo la risposta del servizio:
		if(response != null){
			if(response.getModalitaPagamentoSoggettoInModifica() != null){
				modalitaPagamentoSoggettoOut = response.getModalitaPagamentoSoggetto();
				modalitaPagamentoSoggettoToValidate = response.getModalitaPagamentoSoggettoInModifica();
			} else {
				modalitaPagamentoSoggettoOut = response.getModalitaPagamentoSoggetto();
			}
			
		} else {
			return "backMod";
		}
		
		ModalitaPagamentoSoggetto modalitaPagamentoSoggettoOut = getModalitaPagamentoSoggettoOut();
		ModalitaPagamentoSoggetto modalitaPagamentoValidate = getModalitaPagamentoSoggettoToValidate();
		
		List<ModalitaPagamentoSoggetto> modListApp = model.getDettaglioSoggetto().getModalitaPagamentoList();
		List<ModalitaPagamentoSoggetto> modListDef = new ArrayList<ModalitaPagamentoSoggetto>();
		modListDef.addAll(model.getDettaglioSoggetto().getModalitaPagamentoList());
		
		if(modalitaPagamentoSoggettoOut != null){
			
			ModalitaPagamentoSoggetto mdpDef = null;
			for(ModalitaPagamentoSoggetto mdpApp : modListApp){
				if(mdpApp.getUid() == modalitaPagamentoSoggettoOut.getUid()){
					mdpDef = mdpApp;
					modListDef.remove(mdpDef);
				}
			}
			
			if(mdpDef != null){
				modalitaPagamentoSoggettoOut = mdpDef;
			}
			
			if(modalitaPagamentoSoggettoOut.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(CostantiFin.STATO_IN_MODIFICA_no_underscore)){
				
				modalitaPagamentoValidate.setDescrizioneStatoModalitaPagamento(CostantiFin.STATO_VALIDO);
				modalitaPagamentoValidate.setAssociatoA(modalitaPagamentoSoggettoOut.getAssociatoA());
				modalitaPagamentoValidate.setModalitaAccreditoSoggetto(modalitaPagamentoSoggettoOut.getModalitaAccreditoSoggetto());
				
				modalitaPagamentoValidate.setTipoAccredito(modalitaPagamentoSoggettoOut.getTipoAccredito());
				modalitaPagamentoValidate.setUid(modalitaPagamentoSoggettoOut.getUid());
				TipoAccredito tipoAccredito = CostantiFin.codeToTipoAccredito(modalitaPagamentoSoggettoOut.getModalitaAccreditoSoggetto().getCodice());
				modalitaPagamentoValidate.setTipoAccredito(tipoAccredito);
				
				modListDef.add(modalitaPagamentoValidate);
				
				//istanzio la request per il servizio annullaModalitaPagamentoInModifica:
				AnnullaModalitaPagamentoInModifica ampim = convertiModelPerChiamataServizioAnnullaModalitaPagamentoInModifica(modalitaPagamentoSoggettoOut.getUid());
				
				//invoco il servizio annullaModalitaPagamentoInModifica:
				soggettoService.annullaModalitaPagamentoInModifica(ampim);
				
			} else if (modalitaPagamentoSoggettoOut.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("provvisorio") || modalitaPagamentoSoggettoOut.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("bloccato")){
				modalitaPagamentoSoggettoOut.setDescrizioneStatoModalitaPagamento(CostantiFin.STATO_VALIDO);
				TipoAccredito tipoAccredito = CostantiFin.codeToTipoAccredito(modalitaPagamentoSoggettoOut.getModalitaAccreditoSoggetto().getCodice());
				modalitaPagamentoSoggettoOut.setTipoAccredito(tipoAccredito);
				modListDef.add(modalitaPagamentoSoggettoOut);
					
			}
			
		}
		
		model.getDettaglioSoggetto().setModalitaPagamentoList(modListDef);
		
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
		AggiornaSoggettoResponse responseSoggetto = null;
		
		if (AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite())) {
		
			//istanzio la request per il servizio aggiornaSoggettoProvvisorio:
			AggiornaSoggettoProvvisorio asp = new AggiornaSoggettoProvvisorio(convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(),false));
			
			//invoco il servizio aggiornaSoggettoProvvisorio:
			responseSoggetto = soggettoService.aggiornaSoggettoProvvisorio(asp);
	
		}else{
			
			//istanzio la request per il servizio aggiornaSoggetto:
			AggiornaSoggetto as = convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(),false);
			
			//invoco il servizio aggiornaSoggetto:
			responseSoggetto = soggettoService.aggiornaSoggetto(as);
		}
		
		//Analizzo la risposta del servizio:
		if(responseSoggetto.isFallimento() || (responseSoggetto.getErrori() != null && responseSoggetto.getErrori().size() > 0)) {
			debug(methodName, "Errore nella risposta del servizio");
			
			clearErrorsAndMessages();
			
			addErrori(methodName, responseSoggetto);
			
			debug(methodName, "Model: " + model);
			
			return INPUT;
		}
		
		clearErrorsAndMessages();
		
		// messaggio di ok nella pagina di elenco soggetti
		addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " 
				                   + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		
		model.getDettaglioSoggetto().setModalitaPagamentoList(responseSoggetto.getSoggettoAggiornato().getModalitaPagamentoList());
		
		return "backMod";
	}
	
	/**
	 * Rifiuta validazione
	 * @return
	 */
	public String rifiutaValidazione(){
		setMethodName("rifiutaValidazione");
		
		String uIdModalitaToValidate = getMdpId();
		
		List<ModalitaPagamentoSoggetto> listaModApp = model.getDettaglioSoggetto().getModalitaPagamentoList();
		
		//cerco nella lista listaModApp quella con id uguale a uIdModalitaToValidate:
		ModalitaPagamentoSoggetto modalitaPagamentoSoggettoOut = FinUtility.getById(listaModApp, uIdModalitaToValidate);
		
		if(modalitaPagamentoSoggettoOut.getUid() == 0){
			for(ModalitaPagamentoSoggetto mdpApp : model.getDettaglioSoggetto().getModalitaPagamentoList()){
				if(mdpApp.getUidOrigine() != null && mdpApp.getUidOrigine() != 0){
					if(uIdModalitaToValidate.equalsIgnoreCase(String.valueOf(mdpApp.getUidOrigine()))){
						log.debug("","OOOK");
						modalitaPagamentoSoggettoOut = mdpApp;
						modalitaPagamentoSoggettoOut.setUid(Integer.valueOf(uIdModalitaToValidate));
					}
				}

			}
		}
		
		//Se lo stato e' in modifica basta cancellare l'occorrenza in siac_t_modpag_mod e ricaricare la lista 
		boolean noModifica = true;
		
		if(modalitaPagamentoSoggettoOut.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("provvisorio") || modalitaPagamentoSoggettoOut.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase("bloccato")){
			
			List<ModalitaPagamentoSoggetto> modListApp = model.getDettaglioSoggetto().getModalitaPagamentoList();
			List<ModalitaPagamentoSoggetto> modListDef = new ArrayList<ModalitaPagamentoSoggetto>();
			modListDef.addAll(model.getDettaglioSoggetto().getModalitaPagamentoList());
			
			for(ModalitaPagamentoSoggetto mdpApp : modListApp){
				if(mdpApp.getUid() == modalitaPagamentoSoggettoOut.getUid()){
					ModalitaPagamentoSoggetto mdpDef = mdpApp;
					modListDef.remove(mdpDef);
				}
			}
			
			model.getDettaglioSoggetto().setModalitaPagamentoList(modListDef);
			
		} else if(modalitaPagamentoSoggettoOut.getDescrizioneStatoModalitaPagamento().equalsIgnoreCase(CostantiFin.STATO_IN_MODIFICA_no_underscore)){
			List<ModalitaPagamentoSoggetto> modListApp = model.getDettaglioSoggetto().getModalitaPagamentoList();
			List<ModalitaPagamentoSoggetto> modListDef = new ArrayList<ModalitaPagamentoSoggetto>();
			modListDef.addAll(model.getDettaglioSoggetto().getModalitaPagamentoList());
			
			for(ModalitaPagamentoSoggetto mdpApp : modListApp){
				if(mdpApp.getUid() == modalitaPagamentoSoggettoOut.getUid()){
					ModalitaPagamentoSoggetto mdpDef = mdpApp;
					modListDef.remove(mdpDef);
				} else if(mdpApp.getUidOrigine() != null && mdpApp.getUidOrigine() == modalitaPagamentoSoggettoOut.getUid()){
					ModalitaPagamentoSoggetto mdpDef = mdpApp;
					modListDef.remove(mdpDef);
				}
			}
			
			modalitaPagamentoSoggettoOut.setDescrizioneStatoModalitaPagamento("valido");
			modListDef.add(modalitaPagamentoSoggettoOut);
			
			model.getDettaglioSoggetto().setModalitaPagamentoList(modListDef);
			
			//Rimuovo l'istanza temporanea della modifica
			
			//istanzio la request per il servizio annullaModalitaPagamentoInModifica:
			AnnullaModalitaPagamentoInModifica req = convertiModelPerChiamataServizioAnnullaModalitaPagamentoInModifica(modalitaPagamentoSoggettoOut.getUid());
			
			//invoco il servizio annullaModalitaPagamentoInModifica:
			soggettoService.annullaModalitaPagamentoInModifica(req);
			
			noModifica = false;
			
		}
	
		
		if(noModifica){
			
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
			AggiornaSoggettoResponse responseSoggetto = null;
			
			if (AzioneConsentitaEnum.isConsentito(AzioneConsentitaEnum.OP_SOG_gestisciSoggDec, sessionHandler.getAzioniConsentite())) {
			
				//istanzio la request per il servizio aggiornaSoggettoProvvisorio:
				AggiornaSoggettoProvvisorio asp = new AggiornaSoggettoProvvisorio(convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(),false));
				
				//invoco il servizio aggiornaSoggettoProvvisorio:
				responseSoggetto = soggettoService.aggiornaSoggettoProvvisorio(asp);
		
			}else{
				
				//istanzio la request per il servizio aggiornaSoggetto:
				AggiornaSoggetto as = convertiModelPerChiamataServizioAggiornaSoggetto(model.getDettaglioSoggetto(),false);
				
				//invoco il servizio aggiornaSoggetto:
				responseSoggetto = soggettoService.aggiornaSoggetto(as);
			}
			
			//Analizzo la risposta del servizio:
			if(responseSoggetto.isFallimento() || (responseSoggetto.getErrori() != null && responseSoggetto.getErrori().size() > 0)) {
				debug(methodName, "Errore nella risposta del servizio");
				
				clearErrorsAndMessages();
				
				addErrori(methodName, responseSoggetto);
				
				debug(methodName, "Model: " + model);
				
				return INPUT;
			}
			
			model.getDettaglioSoggetto().setModalitaPagamentoList(responseSoggetto.getSoggettoAggiornato().getModalitaPagamentoList());
		}
		
		//pulisco i vecchi errori e messaggi eventaulmente presenti:
		clearErrorsAndMessages();
		
		// messaggio di ok nella pagina di elenco soggetti
		addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		
		return "backMod";
	}

	/**
	 * Costruisce la request per il servizio RicercaModalitaPagamentoPerChiave
	 * @param modPagId
	 * @return
	 */
	protected RicercaModalitaPagamentoPerChiave convertiModelPerChiamataServizioRicercaModalitaPagamentoPerChiave(String modPagId) {
		RicercaModalitaPagamentoPerChiave ricercaModalitaPagamentoPerChiave = new RicercaModalitaPagamentoPerChiave();
		ricercaModalitaPagamentoPerChiave.setRichiedente(sessionHandler.getRichiedente());
		ricercaModalitaPagamentoPerChiave.setEnte(sessionHandler.getAccount().getEnte());
		
		//cerco in model.getDettaglioSoggetto().getModalitaPagamentoList() quello
		//con l'id uguale a modPagId:
		ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = FinUtility.getById(model.getDettaglioSoggetto().getModalitaPagamentoList(), modPagId);
		
		//Settiamo la modalita pagamento:
		if(modalitaPagamentoSoggetto == null || modalitaPagamentoSoggetto.getUid() == 0){
			for(ModalitaPagamentoSoggetto mdpApp : model.getDettaglioSoggetto().getModalitaPagamentoList()){
				if(mdpApp.getUidOrigine() != null && mdpApp.getUidOrigine() != 0){
					if(modPagId.equalsIgnoreCase(String.valueOf(mdpApp.getUidOrigine()))){
						log.debug("","OOOK");
						modalitaPagamentoSoggetto = mdpApp;
						modalitaPagamentoSoggetto.setUid(Integer.valueOf(modPagId));
					}
				}

			}
		}
		
		//settiamo la sede secondaria:
		if(!model.getDettaglioSoggetto().getDenominazione().equals(modalitaPagamentoSoggetto.getAssociatoA())){
			for(SedeSecondariaSoggetto sedeApp : model.getListaSecondariaSoggetto()){
				if(sedeApp.getDenominazione().equals(modalitaPagamentoSoggetto.getAssociatoA())){
					ricercaModalitaPagamentoPerChiave.setSedeSecondariaSoggetto(sedeApp);
				}
			}
		}
		
		ricercaModalitaPagamentoPerChiave.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto);
		ricercaModalitaPagamentoPerChiave.setSoggetto(model.getDettaglioSoggetto());

		//ritorno la request al chiamante:
		return ricercaModalitaPagamentoPerChiave;
	}

	/**
	 * Costruisce la request per il servizio AnnullaModalitaPagamentoInModifica
	 * @param modPagId
	 * @return
	 */
	protected AnnullaModalitaPagamentoInModifica convertiModelPerChiamataServizioAnnullaModalitaPagamentoInModifica(Integer modPagId) {
		AnnullaModalitaPagamentoInModifica annullaModalitaPagamentoInModifica = new AnnullaModalitaPagamentoInModifica();
		annullaModalitaPagamentoInModifica.setRichiedente(sessionHandler.getRichiedente());
		annullaModalitaPagamentoInModifica.setEnte(sessionHandler.getAccount().getEnte());
		
		//cerco in model.getDettaglioSoggetto().getModalitaPagamentoList() quello
		//con l'id uguale a modPagId:
		ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = FinUtility.getById(model.getDettaglioSoggetto().getModalitaPagamentoList(), modPagId);
		
		//setto la sede secondaria:
		if(!model.getDettaglioSoggetto().getDenominazione().equals(modalitaPagamentoSoggetto.getAssociatoA())){
			for(SedeSecondariaSoggetto sedeApp : model.getListaSecondariaSoggetto()){
				if(sedeApp.getDenominazione().equals(modalitaPagamentoSoggetto.getAssociatoA())){
					annullaModalitaPagamentoInModifica.setSedeSecondariaSoggetto(sedeApp);
				}
			}
		}
		annullaModalitaPagamentoInModifica.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto);
		annullaModalitaPagamentoInModifica.setSoggetto(model.getDettaglioSoggetto());

		//ritorno la request al chiamante:
		return annullaModalitaPagamentoInModifica;
	}
	
	public String getMdpId() {
		return mdpId;
	}

	public void setMdpId(String mdpId) {
		this.mdpId = mdpId;
	}

	public ModalitaPagamentoSoggetto getModalitaPagamentoSoggettoOut() {
		return modalitaPagamentoSoggettoOut;
	}

	public void setModalitaPagamentoSoggettoOut(
			ModalitaPagamentoSoggetto modalitaPagamentoSoggettoOut) {
		this.modalitaPagamentoSoggettoOut = modalitaPagamentoSoggettoOut;
	}

	public ModalitaPagamentoSoggetto getModalitaPagamentoSoggettoToValidate() {
		return modalitaPagamentoSoggettoToValidate;
	}

	public void setModalitaPagamentoSoggettoToValidate(
			ModalitaPagamentoSoggetto modalitaPagamentoSoggettoToValidate) {
		this.modalitaPagamentoSoggettoToValidate = modalitaPagamentoSoggettoToValidate;
	}
	
}

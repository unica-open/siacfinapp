/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.carta;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinser.frontend.webservice.StampaCartaContabileService;
import it.csi.siac.siacfinser.frontend.webservice.msg.StampaRiepilogoCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.StampaRiepilogoCartaContabileResponse;
import it.csi.siac.siacfinser.model.carta.CartaContabile;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class StampaCartaContabileAction extends ActionKeyGestioneCartaAction {

	private static final long serialVersionUID = 1L;

	//input stream per il file di stampa:
	private InputStream fileInputStream;
	
	//numero e anno carta:
	private String numeroCarta;
	private String annoCarta;
	
	@Autowired
	protected transient StampaCartaContabileService stampaCartaContabileService;
	
	public StampaCartaContabileAction () {
		//setto l'oggetto da popolare
		//Carta contabile in questo caso
		oggettoDaPopolare = OggettoDaPopolareEnum.CARTA;
	}

	@Override
	public void prepare() throws Exception {
		//richiamo il prepare della super classe:
		super.prepare();
	}

	@Override
	public String execute() throws Exception {
		
		String methodName = "StampaCartaContabileAction.execute";

		//istanzio la request per il servizio:
		StampaRiepilogoCartaContabile stampaCartaContabileReq = new StampaRiepilogoCartaContabile();
		
		//popolo la request:
		stampaCartaContabileReq .setRichiedente(sessionHandler.getRichiedente());
		stampaCartaContabileReq.setEnte(sessionHandler.getEnte());
		CartaContabile cartaContabile = new CartaContabile();
		cartaContabile.setNumero(Integer.parseInt(numeroCarta));
		cartaContabile.setBilancio(sessionHandler.getBilancio());
		stampaCartaContabileReq.setCartaContabile(cartaContabile );
		
		//invoco il servizio di stampa carta contabile:
		StampaRiepilogoCartaContabileResponse response = stampaCartaContabileService.stampaRiepilogoCartaContabile(stampaCartaContabileReq);
		
		byte[] empty = new byte[0];
		
		//analizzo l'esito del servizio:
		
		if(response!=null && response.isFallimento()){
			//servizio terminato con errori
			debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			fileInputStream = new ByteArrayInputStream(empty);
			return SUCCESS;
		}else if(response!=null  && !response.isFallimento() && (response.getErrori() != null && response.getErrori().size() > 0)){
			// NO FALLIMENTO SI ERRORI ---> WARNING
			fileInputStream = new ByteArrayInputStream(empty);
			debug(methodName, "Errore nella risposta del servizio");
			for (Errore warning : response.getErrori()) {
				//aggiungo i warning
				addPersistentActionWarning(warning.getCodice() +"-"+warning.getDescrizione());
			}
			return SUCCESS;
		}
		
		//leggo il file generato:
		File file = response.getReport();
		fileInputStream = new ByteArrayInputStream(file.getContenuto());
		
		return SUCCESS;
	}
	
	//GETTER E SETTER:	
	
	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}

	public String getNumeroCarta() {
		return numeroCarta;
	}

	public void setNumeroCarta(String numeroCarta) {
		this.numeroCarta = numeroCarta;
	}

	public String getAnnoCarta() {
		return annoCarta;
	}

	public void setAnnoCarta(String annoCarta) {
		this.annoCarta = annoCarta;
	}
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siaccommonapp.model.GenericModel;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.ServiceRequest;


/**
 * Model per la fin 
 * 
 * @author paolos
 * 
 */
public class GenericFinModel extends GenericModel {

	private static final long serialVersionUID = 2614562573398743714L;
	
	//ente
	protected Ente ente;
	
	//richiedente
	protected Richiedente richiedente;
	
	//info utente login
	protected String infoUtenteLogin;
	
	//per settare se il bilancio precedente e' in predisposizione consuntivo
	protected  boolean bilancioPrecedenteInPredisposizioneConsuntivo;
	
	//per settare se il bilancio attuale e' in predisposizione consuntivo
	protected  boolean bilancioAttualeInPredisposizioneConsuntivo;
	
	//visualizzazione compatta della lista capitoli:
    private boolean visualizzaListCapitoliCompatta;
    
    //variabile di appoggio per salvare i waring
    private List<Errore> warning = new ArrayList<Errore>();
    
   

	//CREA LA REQUEST GENERICA:
    protected <R extends ServiceRequest> R creaRequest(Class<R> cls){
		R request;

		try{
			request = cls.newInstance();
		} catch (InstantiationException e){
			throw new IllegalArgumentException("InstantiationException during instance building", e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("IllegalAccessException during instance building", e);
		} catch (ExceptionInInitializerError e) {
			throw new IllegalArgumentException("ExceptionInInitializerError during instance building", e);
		} catch (SecurityException e) {
			throw new IllegalArgumentException("SecurityException during instance building", e);
		}

		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());

		return request;
	}
    
    //GETTER E SETTER:

	public Ente getEnte() {
		return ente;
	}
	public void setEnte(Ente ente) {
		this.ente = ente;
	}
	/**
	 * @return the infoUtenteLogin
	 */
	public String getInfoUtenteLogin() {
		return infoUtenteLogin;
	}
	/**
	 * @param infoUtenteLogin the infoUtenteLogin to set
	 */
	public void setInfoUtenteLogin(String infoUtenteLogin) {
		this.infoUtenteLogin = infoUtenteLogin;
	}
	public boolean isBilancioPrecedenteInPredisposizioneConsuntivo() {
		return bilancioPrecedenteInPredisposizioneConsuntivo;
	}
	public void setBilancioPrecedenteInPredisposizioneConsuntivo(
			boolean bilancioPrecedenteInPredisposizioneConsuntivo) {
		this.bilancioPrecedenteInPredisposizioneConsuntivo = bilancioPrecedenteInPredisposizioneConsuntivo;
	}
	public boolean isBilancioAttualeInPredisposizioneConsuntivo() {
		return bilancioAttualeInPredisposizioneConsuntivo;
	}
	public void setBilancioAttualeInPredisposizioneConsuntivo(boolean bilancioAttualeInPredisposizioneConsuntivo) {
		this.bilancioAttualeInPredisposizioneConsuntivo = bilancioAttualeInPredisposizioneConsuntivo;
	}
	
	public boolean isVisualizzaListCapitoliCompatta() {
		return visualizzaListCapitoliCompatta;
	}
	public void setVisualizzaListCapitoliCompatta(boolean visualizzaListCapitoliCompatta) {
		this.visualizzaListCapitoliCompatta = visualizzaListCapitoliCompatta;
	}

	public Richiedente getRichiedente() {
		return richiedente;
	}

	public void setRichiedente(Richiedente richiedente) {
		this.richiedente = richiedente;
	}

	public List<Errore> getWarning() {
		return warning;
	}

	public void setWarning(List<Errore> warning) {
		this.warning = warning;
	}
	
	public void addWarning(Errore wrng){
		warning.add(wrng);
	}
	
	public boolean hasWarning() {
		return getWarning().size() > 0 ? true : false;
	}

	public void resetWarning() {
		this.warning = new ArrayList<Errore>();
	}
	


	
	
}

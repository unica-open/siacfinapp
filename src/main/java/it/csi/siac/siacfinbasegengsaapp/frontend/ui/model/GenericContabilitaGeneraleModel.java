/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.model;

import java.util.Date;

import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdPadre;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siaccommonapp.util.exception.GenericFrontEndMessagesException;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinapp.frontend.ui.exception.ApplicationException;
import it.csi.siac.siacfinapp.frontend.ui.model.GenericFinModel;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;

/**
 * Model per la CapUscitaPrevisione
 * 
 * @author AR
 * @author Domenico Lisi
 * @author Alessandro Marchino
 * @version 1.0.1 05/07/2013
 * 
 */
public abstract class GenericContabilitaGeneraleModel extends GenericFinModel {

	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5423699814710020723L;

	// FIXME: configurabile
	/** Gli elementi per pagina nella ricerca back-end */
	public static final int ELEMENTI_PER_PAGINA_RICERCA = 10;
	
	private String annoEsercizio;
	
	private Bilancio bilancio;
	private AzioneRichiesta azioneRichiesta;
	
	/**
	 * @return the annoEsercizio
	 */
	public String getAnnoEsercizio() {
		return annoEsercizio;
	}

	/**
	 * @param annoEsercizio the annoEsercizio to set
	 */
	public void setAnnoEsercizio(String annoEsercizio) {
		this.annoEsercizio = annoEsercizio;
	}

	/**
	 * @return the bilancio
	 */
	public Bilancio getBilancio() {
		return bilancio;
	}

	/**
	 * @param bilancio the bilancio to set
	 */
	public void setBilancio(Bilancio bilancio) {
		this.bilancio = bilancio;
	}

	/**
	 * @return the operatore
	 */
	public Operatore getOperatore() {
		return getRichiedente() != null ? getRichiedente().getOperatore() : null;
	}
	
	/**
	 * @return the azioneRichiesta
	 */
	public AzioneRichiesta getAzioneRichiesta() {
		return azioneRichiesta;
	}

	/**
	 * @param azioneRichiesta the azioneRichiesta to set
	 */
	public void setAzioneRichiesta(AzioneRichiesta azioneRichiesta) {
		this.azioneRichiesta = azioneRichiesta;
	}

	/**
	 * Restituisce il suffisso per l'azione. Inizializzato a stringa vuota.
	 * 
	 * @return il suffisso
	 */
	public String getSuffisso() {
		return "";
	}
	
	/**
	 * @return the dataOdierna
	 */
	public Date getDataOdierna() {
		return new Date();
	}
	
	/**
	 * Crea un istanza di ParametriPaginazione con numero di elementi per pagina
	 * impostato al default.
	 *
	 * @return the parametri paginazione
	 */
	protected ParametriPaginazione creaParametriPaginazione() {
		return creaParametriPaginazione(ELEMENTI_PER_PAGINA_RICERCA);
	}
	
	/**
	 * Crea un istanza di ParametriPaginazione con numero di elementi per pagina
	 * passato come parametro.
	 *
	 * @param elementiPerPagina the elementi per pagina
	 * @return the parametri paginazione
	 */
	protected ParametriPaginazione creaParametriPaginazione(int elementiPerPagina) {
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		
		parametriPaginazione.setElementiPerPagina(elementiPerPagina);
		parametriPaginazione.setNumeroPagina(0);
		
		return parametriPaginazione;
	}
	
	/* ==== Requests comuni ==== */

	/**
	 * Creazione di una Request per il servizio di Leggi Classificatori Generici By Tipo Elemento BIL
	 * 
	 * @param tipoElementoBilancio	il tipo dell'Elemento da richiedere
	 * @return						la Request creata
	 */
	public LeggiClassificatoriGenericiByTipoElementoBil creaRequestLeggiClassificatoriGenericiByTipoElementoBil(String tipoElementoBilancio) {
		LeggiClassificatoriGenericiByTipoElementoBil request = new LeggiClassificatoriGenericiByTipoElementoBil();
		request.setAnno(Integer.parseInt(annoEsercizio));
		request.setDataOra(new Date());
		request.setIdEnteProprietario(ente.getUid());
		request.setRichiedente(richiedente);
		request.setTipoElementoBilancio(tipoElementoBilancio);
		return request;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di una Request per il servizio di Leggi Classificatori 
	 * By Tipo Elemento BIL.
	 * 
	 * @param tipoElementoBilancio	il tipo dell'Elemento da richiedere
	 * @return						la Request creata
	 */
	public LeggiClassificatoriByTipoElementoBil creaRequestLeggiClassificatoriByTipoElementoBil(String tipoElementoBilancio) {
		LeggiClassificatoriByTipoElementoBil request = creaRequest(LeggiClassificatoriByTipoElementoBil.class);

		request.setAnno(Integer.parseInt(annoEsercizio));
		request.setIdEnteProprietario(ente.getUid());
		request.setTipoElementoBilancio(tipoElementoBilancio);
		
		return request;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di una Request per il servizio di Leggi Classificatori
	 * BIL By Id Padre.
	 * 
	 * @param idPadre	id del Classificatore padre
	 * @return			la request creata
	 */
	public LeggiClassificatoriBilByIdPadre creaRequestLeggiClassificatoriBilByIdPadre(int idPadre) {
		LeggiClassificatoriBilByIdPadre request = creaRequest(LeggiClassificatoriBilByIdPadre.class);

		request.setAnno(Integer.parseInt(annoEsercizio));
		request.setIdEnteProprietario(ente.getUid());
		request.setIdPadre(idPadre);
		
		return request;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di una Request per il servizio di Ricerca Dettaglio Bilancio.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioBilancio creaRequestRicercaDettaglioBilancio() {
		RicercaDettaglioBilancio request = new RicercaDettaglioBilancio();
		
		request.setDataOra(new Date());
		request.setRichiedente(richiedente);
		
		request.setChiaveBilancio(getBilancio().getUid());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaCodifiche}.
	 * @param codifiche le codifice da injettare. Accetta classi e stringhe
	 * 
	 * @return la request creata
	 */
	public RicercaCodifiche creaRequestRicercaCodifiche(Object... codifiche) {
		RicercaCodifiche request = creaRequest(RicercaCodifiche.class);
		request.addCodifiche(codifiche);
		return request;
	}
	
	
	
	/**
	 * Crea un'istanza della request.
	 * 
	 * @param clazz la classe della request
	 * 
	 * @return la request con data e richiedente compilati
	 * 
	 * @throws IllegalArgumentException nel caso in cui non sia possibile instanziare la request
	 */
	@Override
	protected <R extends ServiceRequest> R creaRequest(Class<R> clazz) {
		R request = super.creaRequest(clazz);
		request.setAnnoBilancio(bilancio.getAnno());
		return request;
	}
	
	/* **** Metodi di utilita **** */
	
	
	/**
	 * Imposta l'anno di esercizio cui il Model si riferisce a partire da un intero.
	 * 
	 * @param annoEsercizioInt l'anno di esercizio come Integer
	 */
	public void setAnnoEsercizioInt(Integer annoEsercizioInt) {
		annoEsercizio = String.valueOf(annoEsercizioInt);
	}
	/**
	 * Imposta l'anno di esercizio nel getBilancio().
	 * @throws ApplicationException 
	 */
	public void impostaAnnoBilancio() throws ApplicationException{
		if(getBilancio() == null) {
			setBilancio(new Bilancio());
		}
		getBilancio().setAnno(getAnnoEsercizioInt());
	}
	
	/**
	 * Restituisce l'anno di esercizio cui il model si riferisce come intero.
	 * 
	 * @return l'anno di esercizio
	 */
	public Integer getAnnoEsercizioInt() {
		Integer anno = null;
		try {
			anno = Integer.valueOf(annoEsercizio);
		} catch(NumberFormatException e) {
			throw new GenericFrontEndMessagesException("Formato dell'anno esercizio non valido");
		}
		return anno;
	}
	
	/**
	 * oOntrolla se l'ente gestisca o meno il tipologia di livello con il codice fornito
	 * @param tipologiaGestioneLivelli la tipologia da controllare
	 * @param value il valore da verificare
	 * @return <code>true</code> se l'ente gestisce il livello con dato codice
	 */
	protected boolean isGestioneLivello(TipologiaGestioneLivelli tipologiaGestioneLivelli, String value) {
		return getEnte() != null && getEnte().getGestioneLivelli() != null && value.equals(getEnte().getGestioneLivelli().get(tipologiaGestioneLivelli));
	}
	
	/**
	 * Controlla se l'ente gestisce o meno le UEB.
	 * 
	 * @return <code>true</code> se l'ente gestisce le UEB; <code>false</code> in caso contrario
	 */
//	FIXME: togliere !
	public boolean isGestioneUEB() {
		return isGestioneLivello(TipologiaGestioneLivelli.LIVELLO_GESTIONE_BILANCIO, WebAppConstants.GESTIONE_UEB);
	}
	
}

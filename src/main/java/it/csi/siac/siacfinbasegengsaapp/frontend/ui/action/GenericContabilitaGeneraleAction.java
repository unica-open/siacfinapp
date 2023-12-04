/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.action;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

import com.opensymphony.xwork2.Preparable;

import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.UtenteNonLoggatoException;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.GenericFinAction;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinbasegengsaapp.frontend.ui.model.GenericContabilitaGeneraleModel;

/**
 * 
 * Specializza le Action di bilancio che utilizzano GenericBilancioModel.
 * Prepara il Model con i parametri comuni di bilancio.
 * 
 * @author Domenico
 *
 * @param <M> Model di riferimento
 */
public abstract class GenericContabilitaGeneraleAction<M extends GenericContabilitaGeneraleModel> extends GenericFinAction<M> {
	
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 58815488018374471L;
	/** Stringa per il risultato di richiesta */
	protected static final String ASK = "ask";
	/** Il titolo del model, per la gestione con i Crumb */
	protected static final String MODEL_TITOLO = "%{model.titolo}";
	
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		super.prepare();
		//Forzo
		
		try {
			// Imposta nel model il richiedente, l'account, l'ente e l'anno di esercizio dalla sessione
			log.debug(methodName, "Inizializzazione dell'account");
			model.setAccount(sessionHandler.getAccount());
			log.debug(methodName, "Inizializzazione dell'anno di esercizio");
			model.setAnnoEsercizio(sessionHandler.getAnnoEsercizio());
			log.debug(methodName, "Inizializzazione della descrizione del bilancio");
			model.setDescrizioneAnnoBilancio(sessionHandler.getDescrizioneAnnoBilancio());
			log.debug(methodName, "Inizializzazione del bilancio");
			model.setBilancio(sessionHandler.getBilancio());
			log.debug(methodName, "Inizializzazione dell'anno di bilancio per il Bilancio");
			model.impostaAnnoBilancio();
			log.debug(methodName, "Inizializzazione dell'azione richiesta");
			model.setAzioneRichiesta(sessionHandler.getAzioneRichiesta());
			this.forward = SUCCESS;
		} catch(Exception exception) {
			// Errore nell'impostazione dei dati. La sessione era terminata
			log.error(methodName, "Errore nella configurazione dei campi comuni");
			throw new UtenteNonLoggatoException("Errore nella configurazione dei campi comuni", exception);
		}
	}
	
	/**
	 * @see Preparable#prepare()
	 * @throws Exception in caso di errore nell'invocazione del metodo
	 */
	public void prepareExecute() throws Exception {
		// Empty
	}
	
	/**
	 * Metodo di utilit&agrave; per il controllo di validit&agrave; di una condizione.
	 * <br>
	 * Nel caso in cui la condizione non sia rispettata, aggiunge l'errore all'interno della action.
	 * @param condition la condizione da verificare
	 * @param errore    l'errore da injettare
	 */
	protected void checkCondition(boolean condition, Errore errore) {
		// Non rilancia l'eccezione
		checkCondition(condition, errore, false);
	}
	
	/**
	 * Metodo di utilit&agrave; per il controllo di validit&agrave; di una condizione e per l'opzionale lancio di un'eccezione.
	 * <br>
	 * Nel caso in cui la condizione non sia rispettata, aggiunge l'errore all'interno della action.
	 * @param condition      la condizione da verificare
	 * @param errore         l'errore da injettare nel caso in cui la condizione sia violata
	 * @param throwException se l'eccezione sia da sollevare
	 * @throws ParamValidationException nel caso in cui la condizione non sia verificata e in cui si sia scelto di rilanciare l'eccezione 
	 */
	protected void checkCondition(boolean condition, Errore errore, boolean throwException) {
		// Controllo della condizione
		if(!condition) {
			// Aggiunge l'errore
			addErrore(errore);
			if(throwException) {
				// Lancia l'errore se richiesto
				throw new ParamValidationException("Error found: " + errore.getTesto());
			}
		}
	}
	
//	/**
//	 * Costrusice una stringa per segnalare l'errore nell'invocazione del servizio.
//	 * @param req il request del servizio che ha fornito l'errore
//	 * @param res la response tramite cui loggare gli errori
//	 * @return la stringa di errore
//	 */
//	public String createErrorInServiceInvocationString(ServiceRequest req, ServiceResponse res) {
//		if(req == null) {
//			// La request non e' valorizzata
//			return "NULL request";
//		}
//		// Crea l'errore a partire dalla classe
//		return createErrorInServiceInvocationString(req.getClass(), res);
//	}
	
//	/**
//	 * Costrusice una stringa per segnalare l'errore nell'invocazione del servizio.
//	 * @param requestClazz il servizio che ha fornito l'errore
//	 * @param res          la response tramite cui loggare gli errori
//	 * @return la stringa di errore
//	 */
//	protected <REQ extends ServiceRequest, RES extends ServiceResponse> String createErrorInServiceInvocationString(Class<REQ> requestClazz, RES res) {
//		// Creo la stringa di errore
//		StringBuilder sb = new StringBuilder()
//			.append("Errore nell'invocazione del servizio ")
//			.append(requestClazz.getSimpleName());
//		if(res != null && res.getErrori() != null) {
//			// Se ho errori
//			for(Errore errore : res.getErrori()) {
//				// Aggiungo errore per errore
//				sb.append(" - ")
//					.append(errore.getTesto());
//			}
//		}
//		// Restituisco la stringa
//		return sb.toString();
//	}
	
	/**
	 * Imposta in sessione un parametro relativo al successo dell'azione attuale per una redirezione ad un'azione futura.
	 */
	protected void impostaInformazioneSuccessoAzioneInSessionePerRedirezione() {
		// Impostazione dell'informazione di successo senza dati opzionali
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione("");
	}
	
	/**
	 * Imposta in sessione un parametro relativo al successo dell'azione attuale per una redirezione ad un'azione futura.
	 * 
	 * @param optional le eventuali informazioni aggiuntive
	 */
	protected void impostaInformazioneSuccessoAzioneInSessionePerRedirezione(String optional) {
		// Creo la lista delle informazioni
		List<Informazione> lista = new ArrayList<Informazione>();
		// Creo la lista delle informazioni
		Informazione informazione = new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo" + optional);
		// Imposto l'informazione di sucesso
		lista.add(informazione);
		// Imposto la lista in sessione
		sessionHandler.setParametro(FinSessionParameter.INFORMAZIONI_AZIONE_PRECEDENTE, lista);
		addPersistentActionMessage(informazione.getTesto());
	}
	
	/**
	 * Metodo di utilit&agrave; per il controllo di non-<code>null</code>it&agrave; per un dato campo.
	 * <br>
	 * Nel caso in cui il campo sia <code>null</code>, aggiunge un errore alla action.
	 * @param campo			il campo da controllare
	 * @param nomeCampo		il nome del campo
	 */
	protected void checkNotNull(Object campo, String nomeCampo) {
		// Non rilancia l'eccezione
		checkNotNull(campo, nomeCampo, false);
	}
	
	/**
	 * Metodo di utilit&agrave; per il controllo di non-<code>null</code>it&agrave; per un dato campo.
	 * <br>
	 * Nel caso in cui il campo sia <code>null</code>, aggiunge un errore alla action.
	 * @param campo          il campo da controllare
	 * @param nomeCampo      il nome del campo
	 * @param throwException se sia da rilanciare un'eccezione
	 * @throws ParamValidationException nel caso in cui la condizione non sia verificata e in cui si sia scelto di rilanciare l'eccezione
	 */
	protected void checkNotNull(Object campo, String nomeCampo, boolean throwException) {
		// Controlla che il campo non sia null
		checkCondition(campo != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(nomeCampo), throwException);
	}
	
	/**
	 * Controlla che il campo non sia <code>null</code> n&eacute; vuoto.
	 * <br>
	 * In caso contrario, aggiunge un errore alla action.
	 * @param campo			il campo da controllare
	 * @param nomeCampo		il nome del campo
	 */
	protected void checkNotNullNorEmpty(String campo, String nomeCampo) {
		// Non rilancio l'eccezione
		checkNotNullNorEmpty(campo, nomeCampo, false);
	}
	
	/**
	 * Controlla che il campo non sia <code>null</code> n&eacute; vuoto.
	 * <br>
	 * In caso contrario, aggiunge un errore alla action.
	 * @param campo          il campo da controllare
	 * @param nomeCampo      il nome del campo
	 * @param throwException se sia da rilanciare un'eccezione
	 * @throws ParamValidationException nel caso in cui la condizione non sia verificata e in cui si sia scelto di rilanciare l'eccezione
	 */
	protected void checkNotNullNorEmpty(String campo, String nomeCampo, boolean throwException) {
		// Controllo che il campo non sia vuoto
		checkCondition(StringUtils.isNotBlank(campo), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(nomeCampo), throwException);
	}
	
	/**
	 * Controlla che il campo non sia <code>null</code> n&eacute; abbia un uid invalido.
	 * <br>
	 * In caso contrario, aggiunge un errore alla action.
	 * @param campo			il campo da controllare
	 * @param nomeCampo		il nome del campo
	 */
	protected void checkNotNullNorInvalidUid(Entita campo, String nomeCampo) {
		// Non rilancio l'eccezione
		checkNotNullNorInvalidUid(campo, nomeCampo, false);
	}
	
	/**
	 * Controlla che il campo non sia <code>null</code> n&eacute; abbia un uid invalido.
	 * <br>
	 * In caso contrario, aggiunge un errore alla action.
	 * @param campo          il campo da controllare
	 * @param nomeCampo      il nome del campo
	 * @param throwException se sia da rilanciare un'eccezione
	 * @throws ParamValidationException nel caso in cui la condizione non sia verificata e in cui si sia scelto di rilanciare l'eccezione
	 */
	protected void checkNotNullNorInvalidUid(Entita campo, String nomeCampo, boolean throwException) {
		// Controllo che il campo sia valorizzato con un uid
		checkCondition(campo != null && campo.getUid() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(nomeCampo), throwException);
	}
	

	/**
	 * Carica l'informazione di successo per l'operazione all'interno del model.
	 */
	protected void impostaInformazioneSuccesso() {
		// Non imposto informazioni aggiuntive
		impostaInformazioneSuccesso("");
	}
	

	/**
	 * Carica l'informazione di successo per l'operazione all'interno del model.
	 * @param optional informazioni aggiuntive da apporre
	 */
	protected void impostaInformazioneSuccesso(String optional) {
		// Aggiungo l'informazioni di successo
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo" + optional));
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T deepClone(T source) {
		// Caso limite
		if(source == null) {
			return null;
		}
		if(source instanceof Serializable) {
			return (T) SerializationUtils.clone((Serializable)source);
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XMLEncoder out = new XMLEncoder(bos);
		out.writeObject(source);
		out.close();
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		XMLDecoder in = new XMLDecoder(bis);
		
		T result = (T) in.readObject();
		in.close();
		return result;
	}
	
	/**
	 * Fornisce un valore di default alla lista fornita in input nel caso in cui essa sia <code>null</code>.
	 * 
	 * @param list la lista da defaultare
	 * @return la lista originale, se non <code>null</code>; una lista vuota in caso contrario
	 */
	protected <T> List<T> defaultingList(List<T> list) {
		// Se la lista non e' valorizzata la inizializza
		return list == null ? new ArrayList<T>() : list;
	}
	
	
}

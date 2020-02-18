/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.soggetto;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siacfinapp.frontend.ui.model.soggetto.GestioneClassiModel;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaClasse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaClasseResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceClasse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ModificaClasse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.soggetto.ClassificazioneSoggetto;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestioneClassiAction extends SoggettoAction<GestioneClassiModel> {
	
	private static final String	DONE = "done";
	
	private static final long serialVersionUID = 5337260587632331114L;
	
	public boolean statusTabellaSoggettiDellaClasse;
	
	private int uidClasseDaRimuovere;

	private	String uidClasseDaModificare;
	
	public int getUidClasseDaRimuovere() {
		return uidClasseDaRimuovere;
	}

	public void setUidClasseDaRimuovere(int uidClasseDaRimuovere) {
		this.uidClasseDaRimuovere = uidClasseDaRimuovere;
	}

	private CodificaFin	classeSelezionata;
	
	
	public CodificaFin getClasseSelezionata() {
		return classeSelezionata;
	}

	public void setClasseSelezionata(CodificaFin classeSelezionata) {
		this.classeSelezionata = classeSelezionata;
	}

	public String getUidClasseDaModificare() {
		return uidClasseDaModificare;
	}

	public void setUidClasseDaModificare(String uidClasseDaModificare) {
		this.uidClasseDaModificare = uidClasseDaModificare;
	}
	  
	@Override
	public void prepare() throws Exception { 
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		model.setTitolo("Gestione Classi");	
		//carichiamo l'elenco delle classi:
		caricaElencoClassi();
		// recupera la classe dalla sessione
	}
	
	private void caricaElencoClassi() {
		// carica l'elenco e lo memorizza nel model 
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.CLASSE_SOGGETTO);		
		List elencoSiacDSoggettoClasse = mappaListe.get(TipiLista.CLASSE_SOGGETTO);
		model.setListaClasse(elencoSiacDSoggettoClasse);
		model.setTotalSizeElencoClassi(elencoSiacDSoggettoClasse.size());
	}
	
	public boolean getEditMode() {
		return classeSelezionata != null && classeSelezionata.getId() != 0;
	}
	
	public String goEditMode() throws Exception {
		CodificaFin selected = lookupSelected();
		classeSelezionata = new CodificaFin(selected.getId(), selected.getDescrizione(), selected.getCodice() );		
		return INPUT;
	}

	public String annullaModifica() throws Exception {
		classeSelezionata = null;
		return INPUT;
	}
	
	public String aggiungiClasse() throws Exception {
	
		// l'univocità del codice è controllata durante la chiamata al db ...
		// qui controllo soltanto che il codice non sia vuoto
		
		// validate fields
		if ( !validaCampi() ) {
			return INPUT;
		}
		
		//istanzio la request per il servizio:
		InserisceClasse req = new InserisceClasse();
		ClassificazioneSoggetto cs = new ClassificazioneSoggetto();
		cs.setSoggettoClasseCode(classeSelezionata.getCodice() );
		cs.setSoggettoClasseDesc( classeSelezionata.getDescrizione() );
		req.setClasseSoggetto(cs);
		req.setEnte(sessionHandler.getEnte());
		req.setRichiedente(sessionHandler.getRichiedente());
		//invoco il servizio:
		ServiceResponse resp = soggettoService.inserisceClasse(req);				
		if ( resp.getEsito() != Esito.SUCCESSO ) {
			addActionError("Impossibile aggiungere la nuova classe");
			for ( Errore err : resp.getErrori() ) {
				addActionError(err.getTesto());
			}
			uidClasseDaRimuovere = 0;			
			return INPUT;			
		} else {
			// ripulisce la classe corrente ed invalida la cache
			removeFromCache(TipiLista.CLASSE_SOGGETTO);
			addActionMessage("La classe " + classeSelezionata.getCodice() + " &egrave; stata aggiunta");			
			classeSelezionata = null;
			uidClasseDaRimuovere = 0;
			// ricarica l'elenco
			removeFromCache(TipiLista.CLASSE_SOGGETTO);
			caricaElencoClassi();						
			return SUCCESS;
		}		
	}
	
	public String doModificaClasse() throws Exception {
		
		// validate fields
		if ( !validaCampi() ) {
			return INPUT;
		}
			
		ClassificazioneSoggetto cs = new ClassificazioneSoggetto();
		cs.setSoggettoClasseCode(classeSelezionata.getCodice() );
		cs.setSoggettoClasseDesc(classeSelezionata.getDescrizione() );
		cs.setIdSoggClasse(classeSelezionata.getId());
		
		//istanzio la request per il servizio:
		ModificaClasse req = new ModificaClasse();
		req.setClasseSoggetto(cs);
		req.setEnte(sessionHandler.getEnte());
		req.setRichiedente(sessionHandler.getRichiedente());
		
		//invoco il servizio:
		ServiceResponse resp = soggettoService.modificaClasse(req);
		if ( resp.getEsito() != Esito.SUCCESSO ) {
			addActionError("Impossibile modificare l'entit&agrave;");
			for ( Errore err : resp.getErrori() ) {
				addActionError(err.getTesto());
			}
			return INPUT;
		} else {
			// deseleziona la riga, e lascia l'edit mode
			removeFromCache(TipiLista.CLASSE_SOGGETTO);
			addActionMessage("La classe " + classeSelezionata.getCodice() + " &egrave; stata modificata");			
			classeSelezionata = null;
			uidClasseDaRimuovere = 0;
			// ricarica l'elenco
			removeFromCache(TipiLista.CLASSE_SOGGETTO);
			caricaElencoClassi();			
			return SUCCESS;
		}
	}
	
	public String doRimuoviClasse()  throws Exception {
		// recupera id/codice della classe e prepara la request per i servizi
		
		//istanzio la request per il servizio:
		AnnullaClasse	req = new AnnullaClasse();
		
		Richiedente richiedente = sessionHandler.getRichiedente();
		Account account = sessionHandler.getAccount();
		richiedente.setAccount(account);
		req.setRichiedente(richiedente);
		req.setEnte(sessionHandler.getEnte());

				
		ClassificazioneSoggetto cs = new ClassificazioneSoggetto();
		cs.setIdSoggClasse(uidClasseDaRimuovere);
		req.setClasseSoggetto(cs);
		
		//invoco il servizio:
		AnnullaClasseResponse resp = soggettoService.annullaClasse(req);
		// recupera la codifica da eliminare/chiudere
		
		if ( resp.getEsito() != Esito.SUCCESSO ) {
			addActionError("Impossibile eliminare l'entita");
			for ( Errore err : resp.getErrori() ) {
				addActionError(err.getTesto());
			}
			return INPUT;
		} else {
			// ho eliminato la classe, quindi "azzero" il parametro
			uidClasseDaRimuovere = 0;			
			removeFromCache(TipiLista.CLASSE_SOGGETTO);
			return DONE;
		}
	}
	
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		setMethodName("execute");
		
		if ( uidClasseDaRimuovere != 0 ) {
			 return doRimuoviClasse();
		}
		
		if ( StringUtils.isNotEmpty( uidClasseDaModificare ) ) {
			CodificaFin selected = lookupSelected();
			if ( selected != null ) {
				classeSelezionata = selected;
			}
		}
		
	    return super.execute();
	}    
	
	public CodificaFin lookupSelected() throws Exception 
	{
		int uid = Integer.parseInt( this.getUidClasseDaModificare() );
		// recupera la riga selezionata (model)
		for ( CodificaFin row : model.getListaClasse() ) {
			if ( row.getId().intValue() == uid) {
				return row;
			}
		}
		
		return null;
	}
	
		
	/** Valida i campi recuperati dal form
	 * 
	 * @param instance
	 */
	private boolean validaCampi() {
		boolean valid = true;
		if ( classeSelezionata != null ) {
			// trim input fields
			classeSelezionata.setCodice(classeSelezionata.getCodice().trim()); 				  
			classeSelezionata.setDescrizione(classeSelezionata.getDescrizione().trim()); 				  
			
			if ( classeSelezionata.getDescrizione().length() == 0 ) {
				addActionError("La descrizione non pu&ograve; essere vuota");
				valid = false;
			}
			
			if ( classeSelezionata.getCodice().length() < 2 ) {
				addActionError("Il codice deve essere di almeno 2 caratteri");
				valid = false;
			}
		}
		
		return valid;
	}
}

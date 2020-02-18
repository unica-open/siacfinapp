/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.carta;

import java.util.List;
import java.util.Map;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.opensymphony.xwork2.ActionContext;

import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestioneCartaStep2Action extends ActionKeyGestioneCartaAction {

	private static final long serialVersionUID = 1L;
	
	//numero della riga da eliminare
	private String numeroRigaDaEliminare;

	public GestioneCartaStep2Action () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.CARTA;
	}
	
	@Override
	public void prepare() throws Exception {
		
		//richiamo il prepare della super classe:
		super.prepare();
		
		//setto il titolo dello step:
		this.model.setTitoloStep(creaTitoloCarta());
		//setto il titolo generale di pagina:
		this.model.setTitolo("Gestione carta contabile - Gestione righe");
		
		//caricamento lista contoTesoriere
		Map<TipiLista, List<? extends CodificaFin>> mappaListe = getCodifiche(TipiLista.CONTO_TESORERIA);
		model.setListaContoTesoriereRiga(((List<CodificaFin>)mappaListe.get(TipiLista.CONTO_TESORERIA)));
		
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {

		//OPERAZIONI PER TENERE AGGIORNATE LE VARIABILI DELLA GESTIONE DEL PROVVEDIMENTO AMMINITRATIVO:
		reimpostaCodiceStrutturaPadre(model.getProvvedimento());
		reimpostaCodiceStrutturaSelezionata(model.getProvvedimento());
		//////////////////////////////////////////////////////////////////////////////////////////
		
		//carico la lista delle righe doc:
		List<PreDocumentoCarta> righeDaDoc =  (List<PreDocumentoCarta>) ActionContext.getContext().getSession().get("LISTA_PRE_DOC_DA_DOCUMENTI");
		
		//ciclo sulle righe:
		if(righeDaDoc != null && righeDaDoc.size()>0){
			for(PreDocumentoCarta rigaIt : righeDaDoc){
				//setto nel model i dati della riga iterata:
				inserisciRigaInternal(rigaIt);
			}
		}
		
		//Vanno rimossi:
		ActionContext.getContext().getSession().remove("LISTA_PRE_DOC_DA_DOCUMENTI");
		
		//Rimuoviamo la fotocopia del model che usiamo per comunicare i dati al sotto caso d'uso nuova riga da documenti:
		ActionContext.getContext().getSession().remove("KEY_GESTIONE_CARTA_MODEL");
		
		
		return SUCCESS;
	}

	public boolean disableStep2() {
		boolean disabled=false;

		//se sono in aggiornamento
		if (model.isAggiornamento()) {
			if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.PROVVISORIO.toString())) {
				//Caso stato PROVVISORIO
				if (!isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTA) && !isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTARAGIO)) {
					disabled=true;
				}
			} else if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.COMPLETATO.toString())) {
				//Caso stato COMPLETATO
				disabled=true;
			} else if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.TRASMESSO.toString())) {
				//Caso stato TRASMESSO
				disabled=true;
			}
		}

		return disabled;
	}

	public boolean disableAggiornaRiga(String numeroRiga) {
		boolean disabled=false;

		//se sono in aggiornamento
		if (model.isAggiornamento()) {
			if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.PROVVISORIO.toString())) {
				//Caso stato PROVVISORIO
				if (!isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTA) && !isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTARAGIO)) {
					disabled=true;
				}
			} else if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.COMPLETATO.toString())) {
				//Caso stato COMPLETATO
				if (!isAzioneAbilitata(CodiciOperazioni.OP_SPE_AGGCARTARAGIO)) {
					disabled=true;
				}
			} else if (model.getStatoOperativoCarta()!=null && 
					model.getStatoOperativoCarta().equalsIgnoreCase(CartaContabile.StatoOperativoCartaContabile.TRASMESSO.toString())) {
				//Caso stato TRASMESSO
				List<SubdocumentoSpesa> listaSubDocumentiSpesaCollegati=null;
				if (numeroRiga!=null) {
					Integer numeroRigaInt=Integer.parseInt(numeroRiga);
					
					if (model.getListaRighe()!=null && model.getListaRighe().size()>0) {
						for (PreDocumentoCarta riga : model.getListaRighe()) {
							if (riga.getNumero().compareTo(numeroRigaInt)==0) {
								listaSubDocumentiSpesaCollegati=riga.getListaSubDocumentiSpesaCollegati();
								break;
							}
						}
					}
					
				}
				if (!isAzioneAbilitata(CodiciOperazioni.OP_SPE_regCarta) || listaSubDocumentiSpesaCollegati==null) {
					disabled=true;
				}
			}
		}

		return disabled;
	}

	public int numeroRighe(){
		int numRighe=0;
		
		if (model.getListaRighe()!=null && model.getListaRighe().size()>0) {
			numRighe=model.getListaRighe().size();
		}

		return numRighe;
	}

	public String eliminaRiga(){
		return super.eliminaRiga(numeroRigaDaEliminare);
	}
	
	public String indietro(){
		if (model.isAggiornamento()) {
			return "gotoStep1Aggiorna";
		}
		return "gotoStep1";
	}
	
	
	public String nuovaRigaDaDocumenti(){
		ActionContext.getContext().getSession().put("KEY_GESTIONE_CARTA_MODEL", model);
		return "nuovaRigaDaDocumentiStep1";
	}
	
	//GETTER E SETTER:
	
	public String getNumeroRigaDaEliminare() {
		return numeroRigaDaEliminare;
	}

	public void setNumeroRigaDaEliminare(String numeroRigaDaEliminare) {
		this.numeroRigaDaEliminare = numeroRigaDaEliminare;
	}
}

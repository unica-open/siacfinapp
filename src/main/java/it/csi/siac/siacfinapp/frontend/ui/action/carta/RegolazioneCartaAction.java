/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.carta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RegolarizzaCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.RegolarizzaCartaContabileResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RegolazioneCartaAction extends WizardRegolarizzaCartaAction {

	
	private static final long serialVersionUID = 1L;
	
	private String numeroAnnoCapitoloPerElimina;
	private String numeroAnnoMovimentoPerElimina;
	private String numeroImpegnoDaPassarePerElimina;
	private String numeroSubImpegnoDaPassarePerElimina;
	
	private String tripletta;
	private boolean ricaricaDaRegolarizza;
	private boolean documentoAppenaCollegato;
	
	private String uidSelezPerModifica;
	private String uidTemporaneo;
	
	/**
	 * costruttore
	 */
	public RegolazioneCartaAction () {
	   	//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.CARTA;
	}
	
	/**
	 * metodo prepare della action
	 */
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		this.model.setTitolo("Completa Carta Contabile");
	}	
	
	/**
	 * metodo execute della action
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		//nel caso ricaricaDaRegolarizza setto il parametro regolarizzazione avvenuta:
		if(ricaricaDaRegolarizza || documentoAppenaCollegato){
			sessionHandler.setParametro(FinSessionParameter.REGOLARIZZAZIONE_AVVENUTA, WebAppConstants.Si);
		}
		
		//SIAC-6076 ho appena collegato un documento, ricarico per avere l'elenco doc aggiornato:
		if(documentoAppenaCollegato){
			caricaDatiCartaInRegolazione(model.getCartaContabileDaRicerca().getNumero(), model.getCartaContabileDaRicerca().getBilancio().getAnno());
		}
		//
	    
		//setto il dettaglio della riga selezionata:
	    model.setDettaglioRiga(dettaglioRigaSelezionata(model.getNumeroRigaSelezionata()));
	    
	  //se la riga ha un impegno:
	    if(model.getDettaglioRiga().getImpegno()!=null){
	    	
	    	// devo disabilitare e proteggere i campi della collega impegni
	    	model.setAnnoImpegno(model.getDettaglioRiga().getImpegno().getAnnoMovimento());
	    	model.setNumeroImpegno(model.getDettaglioRiga().getImpegno().getNumero().intValue());
	    	
	    	//in caso di sub setto il suo numero nel model:
	    	if(model.getDettaglioRiga().getSubImpegno()!=null && model.getDettaglioRiga().getSubImpegno().getNumero()!=null){
	    		model.setNumeroSub(model.getDettaglioRiga().getSubImpegno().getNumero().intValue());
	    	}
	    	
	    	//imposto i flag di appoggio:
	    	model.setHasImpegnoSelezionatoXPopup(true);
	    	model.setHasCollegaImpegnoBloccato(true);
	    	model.setToggleCollegaImpegnoAperto(true);
	    }
	    
	    // calcolo la somma da mettere nel footer
	    sommaRigheDocCollegatiRegolazione();
	    
		return SUCCESS;
	}
	
	/**
	 * per gestire il redirect verso la ricerca dei documenti da collegare
	 * @return
	 */
	public String collegaDocumento(){
		return "collegaDocumento";
	}
	
	/**
	 * per pilotare l'abilitazione della compilazione guidata per collegare
	 * @return
	 */
	public boolean disabilitaCompilazioneGuidataPerCollega(){
		if(model.isHasCollegaImpegnoBloccato()){
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Metodo che gestisce l'elimina di un doc collegato
	 * @return
	 */
	public String eliminaDocumentoCollegato(){
		// ciclo sulle righe doc e vado a filtrare per quelle con documento == null
		if(model.getDettaglioRiga().getListaSubDocumentiSpesaCollegati()!=null && model.getDettaglioRiga().getListaSubDocumentiSpesaCollegati().size()>0){
			
			SubdocumentoSpesa sdsDaEliminare = null;
			Iterator<SubdocumentoSpesa> it = model.getDettaglioRiga().getListaSubDocumentiSpesaCollegati().iterator();
			while(it.hasNext()){
				
				SubdocumentoSpesa sds = it.next();
				if(sds.getDocumento()==null){
					// allora e' una riga con la il btn azioni attivo
					// confronto con i valori della popup (sei sicuro di...)
					
					if(Integer.parseInt(uidTemporaneo)==sds.getUid()){
						
						sdsDaEliminare = sds;
						break;
					}
				}
			}
			
			// elimino la riga
			if(sdsDaEliminare!=null)  model.getDettaglioRiga().getListaSubDocumentiSpesaCollegati().remove(sdsDaEliminare);
			
			// aggiorno i totali dopo l'eliminazione
			sommaRigheDocCollegatiRegolazione();
		}
		
		return SUCCESS;
	}

	/**
	 * effettua la sommatoria dei doc collegati da regolarizzare
	 * e setta il risultanto nel model
	 */
	private void sommaRigheDocCollegatiRegolazione(){
		
		BigDecimal somma = BigDecimal.ZERO;
		
		//se ci sono sub documenti spesa collegati:
		if(!isEmpty(model.getDettaglioRiga().getListaSubDocumentiSpesaCollegati())){
			
		   //itero su di essi calcolando la somma:
		   Iterator<SubdocumentoSpesa> it = model.getDettaglioRiga().getListaSubDocumentiSpesaCollegati().iterator();
		   
		   while(it.hasNext()){
			   SubdocumentoSpesa sd = it.next();
			   somma = somma.add(sd.getImporto());
		   }
			
		}
		
		// setto la somma nel model
        model.setSommaDocCollegatiRegolazione(somma);		
		
	}
	
	
	public String confermaImpegnoCarta(){
		if(model.isHasImpegnoSelezionatoPopup()==true){
			model.setNumeroImpegno(model.getnImpegno());
			model.setAnnoImpegno(model.getnAnno());
			model.setNumeroSub(model.getnSubImpegno());
			model.setNumeroMutuoPopup(null);
			model.setnAnno(null);
			model.setnImpegno(null);
			int voceMutuoScelta = model.getRadioVoceMutuoSelezionata();
			List<VoceMutuo> listaVocMutuo = model.getListaVociMutuo();		
			if(listaVocMutuo!=null && listaVocMutuo.size()>0){
				for(int j=0; j<listaVocMutuo.size();j++){
					if(listaVocMutuo.get(j).getUid()==voceMutuoScelta){
						model.setNumeroMutuoPopup(Integer.valueOf(listaVocMutuo.get(j).getNumeroMutuo()));
						model.setDisponibilita(listaVocMutuo.get(j).getImportoDisponibileLiquidareVoceMutuo());
					}
				}
			}
		}
		model.setToggleCollegaImpegnoAperto(true);


		pulisciListeeSchedaPopup();
		return SUCCESS;
	}
	
	public String dettaglioAggiornaImportoRegolarizzazione(){
		
		String split[]=getTripletta().split("_");
		
		String uid = split[1];
		
		for(SubdocumentoSpesa subDoc: model.getDettaglioRiga().getListaSubDocumentiSpesaCollegati()){
			if(subDoc.getDocumento()== null){
				
				if( Integer.parseInt(uid)==subDoc.getUid()){
					
					if(null!=subDoc.getSubImpegno()){
						model.getDocumentoInModifica().setSubImpegno(subDoc.getSubImpegno());
					}
					model.getDocumentoInModifica().setImpegno(subDoc.getImpegno());
					model.getDocumentoInModifica().setImporto(subDoc.getImporto());
					model.getDocumentoInModifica().setFlagConvalidaManuale(subDoc.getFlagConvalidaManuale());
					model.getDocumentoInModifica().setImportoDaDedurre(subDoc.getImportoDaDedurre());
					// ultimo bilift con probabile bug
					model.getDocumentoInModifica().setStato(subDoc.getStato());
					model.setImportoPopupFormattato(null);
					model.setImportoPopupFormattato(convertiBigDecimalToImporto(subDoc.getImporto()));
					model.getDocumentoInModifica().setUid(subDoc.getUid());
				}
				
			}
		}
		
		
		
		
		return "dettaglioDocInModificaPerAggiorna";
	}
	// conferma su popup
	public String confermaCollegaImpegno(){
		
		List<Errore> listaErrori= new ArrayList<Errore>();
		
		if(model.getAnnoImpegno() == null || FinStringUtils.isEmpty(model.getAnnoImpegno().toString())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Impegno"));
		}
		
		if(model.getNumeroImpegno() == null || FinStringUtils.isEmpty(model.getNumeroImpegno().toString())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Impegno"));
		}
		if(model.getImportoImpegno() == null || FinStringUtils.isEmpty(model.getImportoImpegno())){
			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo Impegno")); 
		}
		
		
		if(!listaErrori.isEmpty()){
			addErrori(listaErrori);
			model.setToggleCollegaImpegnoAperto(true);
			return INPUT;
		}
		
		RicercaImpegnoPerChiaveOttimizzato req = convertiModelPerChiamataServizioRicercaPerChiave();
		
		req.setCaricaSub(false);
		
		RicercaImpegnoPerChiaveOttimizzatoResponse response = movimentoGestionService.ricercaImpegnoPerChiaveOttimizzato(req);
		
		if(response.isFallimento()){
			addErrori(response.getErrori());
			
			if(response.getErrori()==null || response.getErrori().size()==0){
				
				addErrore(ErroreFin.MOV_NON_ESISTENTE.getErrore("Impegno"));
			}
			model.setToggleCollegaImpegnoAperto(true);
			return INPUT;
		}else if(response.getImpegno()== null){
			addErrore(ErroreFin.MOV_NON_ESISTENTE.getErrore("Impegno"));
			model.setToggleCollegaImpegnoAperto(true);
			return INPUT;
		}else if(response.getImpegno().getStatoOperativoMovimentoGestioneSpesa().equals(Constanti.MOVGEST_STATO_ANNULLATO)){
			addErrore(ErroreFin.MOV_ANNULLATO.getErrore("Impegno"));
			model.setToggleCollegaImpegnoAperto(true);
			return INPUT;
		}else if(response.getImpegno().getStatoOperativoMovimentoGestioneSpesa().equals(Constanti.MOVGEST_STATO_PROVVISORIO)){
			addErrore(ErroreFin.MOV_PROVVISORIO.getErrore("Impegno"));
			model.setToggleCollegaImpegnoAperto(true);
			return INPUT;
		}
		
		SubImpegno trovatoSubImpegno= null;
		
		
		//SE L'impegno e' in stato DEFINITIVO
		if(response.getImpegno().getStatoOperativoMovimentoGestioneSpesa().equals(Constanti.MOVGEST_STATO_DEFINITIVO)){
			if(model.getNumeroSub()== null || FinStringUtils.isEmpty(model.getNumeroSub().toString())){
				
				//Se esiste il codice Soggetto
				if(response.getImpegno().getSoggetto()!= null && !FinStringUtils.isEmpty(response.getImpegno().getSoggetto().getCodiceSoggetto())){
					
					//se il codice creditore e' diverso da quello dell'impegno scelto
					if(!response.getImpegno().getSoggetto().getCodiceSoggetto().equals(model.getDettaglioRiga().getSoggetto().getCodiceSoggetto())){
						addErrore(ErroreFin.IMPEGNO_NON_COMPATIBILE.getErrore(""));
						model.setToggleCollegaImpegnoAperto(true);
						return INPUT;
					}
					//Se non esiste il codice soggetto si va a controlalre la classe
				}else{
					//Se la classe NON e' presente all'interno del soggetto della riga deve fare l'errore
					boolean flag=false;
					String[] a=model.getDettaglioRiga().getSoggetto().getTipoClassificazioneSoggettoId();
					
					if (a != null) {
						for(int i=0; i<= a.length;i++){
							if(a[i].equals(response.getImpegno().getClasseSoggetto().getCodice())){
								flag=true;
								break;
							}
						}
					}
					
					if(flag==false){
						addErrore(ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore(""));
						model.setToggleCollegaImpegnoAperto(true);
						return INPUT;
					}
					
				}
			
			}else{
				addErrore(ErroreFin.MOV_NON_ESISTENTE.getErrore("Sub Impegno"));
				model.setToggleCollegaImpegnoAperto(true);
				return INPUT;
			}
			
		}else{
			//L'impegno e' in stato Definitivo non liquidabile
			//Se ho scritto il numero del subImpegno
			if(model.getNumeroSub()!= null && !FinStringUtils.isEmpty(model.getNumeroSub().toString())){
				if(response.getElencoSubImpegniTuttiConSoloGliIds()!= null && response.getElencoSubImpegniTuttiConSoloGliIds().size()>0){
					
					boolean esisteSubImpegno = false;
					for(SubImpegno sub : response.getElencoSubImpegniTuttiConSoloGliIds()){
						//Se esiste il subImpegno
						if(sub.getNumero().compareTo(BigDecimal.valueOf(model.getNumeroSub()))==0){
							// devo controllare il soggetto
							if(sub.getSoggetto()== null || !sub.getSoggetto().getCodiceSoggetto().equals(model.getDettaglioRiga().getSoggetto().getCodiceSoggetto())){
								addErrore(ErroreFin.IMPEGNO_NON_COMPATIBILE.getErrore(""));
								model.setToggleCollegaImpegnoAperto(true);
								return INPUT;
							}
							trovatoSubImpegno= sub;
							esisteSubImpegno = true;
							// trovato il sub quindi break ed esco dal ciclo	
							break;
							
						}
					}
					
					if(!esisteSubImpegno){
						//SE il sub richiesto non ESISTE
						addErrore(ErroreFin.MOV_NON_ESISTENTE.getErrore("Sub Impegno"));
						model.setToggleCollegaImpegnoAperto(true);
						return INPUT;
					}
					
				}else{
					//Se non esistono SUB
					addErrore(ErroreFin.IMPEGNO_NON_COMPATIBILE.getErrore(""));
					model.setToggleCollegaImpegnoAperto(true);
					return INPUT;
				}
			}else{
				addErrore(ErroreFin.SUB_IMP_PRESENTE.getErrore(""));
				model.setToggleCollegaImpegnoAperto(true);
				return INPUT;
			}
			
		}
		
		
		// passati tutti i controlli devo aggiungere la riga SubdocumentoSpesa
		SubdocumentoSpesa aggiuntaRiga = new SubdocumentoSpesa();
		aggiuntaRiga.setDocumento(null);
		
		aggiuntaRiga.setUid(creaIdTemporaneo());
	    // aggiungo impegno appena trovato
		aggiuntaRiga.setImpegno(response.getImpegno());
		
		if(trovatoSubImpegno!= null){
			aggiuntaRiga.setSubImpegno(trovatoSubImpegno);
		}
		
		
		// aggiungo importo
		aggiuntaRiga.setImporto(convertiImportoToBigDecimal(model.getImportoImpegno()));
		
		// nel caso inizio da una situazione in cui non ci sono oggetti collegati
		// la lista vale Null quindi la istanzio
		if(null==model.getDettaglioRiga().getListaSubDocumentiSpesaCollegati()) model.getDettaglioRiga().setListaSubDocumentiSpesaCollegati(new ArrayList<SubdocumentoSpesa>());
		
		model.getDettaglioRiga().getListaSubDocumentiSpesaCollegati().add(aggiuntaRiga);
		
		// calcolo la somma da mettere nel footer
	    sommaRigheDocCollegatiRegolazione();
	    
	    
	    // pulisco i campi di collega impegno se non ho l'impegno bloccato
	    // altrimenti l'utente puo' collegare impegni che non c'entrano nulla
	    if(!model.isHasCollegaImpegnoBloccato()){
	    	pulisciFormCollegaImpegno();
	    }else{
	    	 // cancello solo l'importo
	    	  model.setImportoImpegno("");
	    }
	    
		addActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " 
                + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		
		return SUCCESS; //"gestRigheCarta";
	}
	
	
	private int creaIdTemporaneo(){
		int idTemp = 1;
		
		// considero anche quelli gia' regolarizzati tanto e' un id momentaneo e basta
		if(model.getDettaglioRiga().getListaSubDocumentiSpesaCollegati()!=null && model.getDettaglioRiga().getListaSubDocumentiSpesaCollegati().size()>0){
			
			idTemp = idTemp + model.getDettaglioRiga().getListaSubDocumentiSpesaCollegati().size();
			
		}
		
		return idTemp;
		
	}
	
	
	public String consolidaRegolarizzazione(){
		
		RegolarizzaCartaContabile rcc = new RegolarizzaCartaContabile();
		
		rcc.setBilancio(sessionHandler.getBilancio());
		rcc.setEnte(sessionHandler.getEnte());
		rcc.setRichiedente(sessionHandler.getRichiedente());
		
		CartaContabile cartaContabile = new CartaContabile();
		
		cartaContabile = model.getCartaContabileDaRicerca();
		
		
		List<SubdocumentoSpesa> listaDaEsporreInCasoErrore = clone(model.getDettaglioRiga().getListaSubDocumentiSpesaCollegati());
		
		// devo passare al servizio solamente le righe che ho aggiunto con collega impegno,
		// quindi vado a ciclare e scegliere quelle per cui il documento e' == Null 
		
		List<SubdocumentoSpesa> listaSubDaRegolarizzare = new ArrayList<SubdocumentoSpesa>(); 
		
		if(model.getDettaglioRiga().getListaSubDocumentiSpesaCollegati()!=null && model.getDettaglioRiga().getListaSubDocumentiSpesaCollegati().size()>0){
			
			Iterator<SubdocumentoSpesa> it = model.getDettaglioRiga().getListaSubDocumentiSpesaCollegati().iterator();
			
			while(it.hasNext()){
				SubdocumentoSpesa sds = it.next();
				if(sds.getDocumento()==null){
					
					listaSubDaRegolarizzare.add(sds);
					
				}
			}
			
		}else{
			addActionError("Non esistono documenti da consolidare");
			return INPUT;
		}
		
		List<PreDocumentoCarta> listaPreDoc = new ArrayList<PreDocumentoCarta>();
		
		// metto nella lista il predoc scelto come dettaglio
		
		model.getDettaglioRiga().setListaSubDocumentiSpesaCollegati(listaSubDaRegolarizzare);
		
		listaPreDoc.add(model.getDettaglioRiga());
		cartaContabile.setListaPreDocumentiCarta(listaPreDoc);
		
		rcc.setCartaContabileDaRegolarizzare(cartaContabile);
		
		
		RegolarizzaCartaContabileResponse response = cartaContabileService.regolarizzaCartaContabile(rcc);
		
		if(response!=null && response.isFallimento()){
			addErrori(response.getErrori());
			
			model.getDettaglioRiga().setListaSubDocumentiSpesaCollegati(listaDaEsporreInCasoErrore);
			
			return INPUT;
		}
		
		// setto il model con i nuovi valori
		model.setCartaContabileDaRicerca(response.getCartaContabile());
		if(null!=response.getCartaContabile().getListaPreDocumentiCarta()){
			model.getCartaContabileDaRicerca().setListaPreDocumentiCarta(response.getCartaContabile().getListaPreDocumentiCarta());
		}
		
		if(model.getCartaContabileDaRicerca().getListaPreDocumentiCarta()!=null && model.getCartaContabileDaRicerca().getListaPreDocumentiCarta().size()>0){
			Iterator<PreDocumentoCarta> it = model.getCartaContabileDaRicerca().getListaPreDocumentiCarta().iterator();
			while(it.hasNext()){
				
				PreDocumentoCarta pdc = it.next();
				
				if(String.valueOf(pdc.getNumero()).equalsIgnoreCase(model.getNumeroRigaSelezionata())){
					
					model.setDettaglioRiga(pdc);
				}
				
				
			}
			
		}
		
		// pulisco i campi di collega impegno
	    pulisciFormCollegaImpegno();
		
	    addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " 
                + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
	    
		
		return "consolida";
	}
	
	public String aggiornaRigaDoc(){
		
		if(model.getDocumentoInModifica()!= null){
			
			
			String uid = getUidSelezPerModifica();
			
			
			
			for(SubdocumentoSpesa subDoc: model.getDettaglioRiga().getListaSubDocumentiSpesaCollegati()){
				if(subDoc.getDocumento()== null){
					
					if(subDoc.getUid()==Integer.parseInt(uid)){
						subDoc.setImporto(convertiImportoToBigDecimal(model.getImportoPopupFormattato()));
						break;
						
					}
					
				}
			}
			
		}
		sommaRigheDocCollegatiRegolazione();
		return SUCCESS;
	}
	
	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */
	

	public String getNumeroAnnoCapitoloPerElimina() {
		return numeroAnnoCapitoloPerElimina;
	}

	public void setNumeroAnnoCapitoloPerElimina(String numeroAnnoCapitoloPerElimina) {
		this.numeroAnnoCapitoloPerElimina = numeroAnnoCapitoloPerElimina;
	}

	public String getNumeroAnnoMovimentoPerElimina() {
		return numeroAnnoMovimentoPerElimina;
	}

	public void setNumeroAnnoMovimentoPerElimina(
			String numeroAnnoMovimentoPerElimina) {
		this.numeroAnnoMovimentoPerElimina = numeroAnnoMovimentoPerElimina;
	}

	public String getNumeroImpegnoDaPassarePerElimina() {
		return numeroImpegnoDaPassarePerElimina;
	}

	public void setNumeroImpegnoDaPassarePerElimina(
			String numeroImpegnoDaPassarePerElimina) {
		this.numeroImpegnoDaPassarePerElimina = numeroImpegnoDaPassarePerElimina;
	}

	public String getTripletta() {
		return tripletta;
	}

	public void setTripletta(String tripletta) {
		this.tripletta = tripletta;
	}

	public String getNumeroSubImpegnoDaPassarePerElimina() {
		return numeroSubImpegnoDaPassarePerElimina;
	}

	public void setNumeroSubImpegnoDaPassarePerElimina(
			String numeroSubImpegnoDaPassarePerElimina) {
		this.numeroSubImpegnoDaPassarePerElimina = numeroSubImpegnoDaPassarePerElimina;
	}

	public boolean getRicaricaDaRegolarizza() {
		return ricaricaDaRegolarizza;
	}

	public void setRicaricaDaRegolarizza(boolean ricaricaDaRegolarizza) {
		this.ricaricaDaRegolarizza = ricaricaDaRegolarizza;
	}

	public String getUidSelezPerModifica() {
		return uidSelezPerModifica;
	}

	public void setUidSelezPerModifica(String uidSelezPerModifica) {
		this.uidSelezPerModifica = uidSelezPerModifica;
	}

	public String getUidTemporaneo() {
		return uidTemporaneo;
	}

	public void setUidTemporaneo(String uidTemporaneo) {
		this.uidTemporaneo = uidTemporaneo;
	}

	public boolean isDocumentoAppenaCollegato() {
		return documentoAppenaCollegato;
	}

	public void setDocumentoAppenaCollegato(boolean documentoAppenaCollegato) {
		this.documentoAppenaCollegato = documentoAppenaCollegato;
	}
	
}

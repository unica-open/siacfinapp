/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.carta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.util.FinUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RegolarizzaCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.RegolarizzaCartaContabileResponse;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;
import it.csi.siac.siacfinser.model.errore.ErroreFin;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestRigheCartaAction extends WizardRegolarizzaCartaAction {

	
	private static final long serialVersionUID = 1L;
	
	private String anno;
	private String numero;
	private String ckRigaSelezionata;
	
	private String numeroDaRegolarizzare;
	private String numeroDaRegolarizzarePassato; 
	
	/**
	 * costruttore
	 */
	public GestRigheCartaAction () {
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
		this.model.setTitolo("Regolarizza Carta Contabile");
	}	
	
	
	/**
	 * metodo execute della action
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		//CONTROLLI
		
		//Controllo stato bilancio
		if(sessionHandler.getFaseBilancio()!= null && !(sessionHandler.getFaseBilancio().equalsIgnoreCase("E") ||  
				sessionHandler.getFaseBilancio().equalsIgnoreCase("G") || 
				sessionHandler.getFaseBilancio().equalsIgnoreCase("A") || 
				sessionHandler.getFaseBilancio().equalsIgnoreCase("O"))){
			
			//errore nella fase del bilancio
			StringTokenizer st = new StringTokenizer(sessionHandler.getDescrizioneAnnoBilancio(),"-");
			st.nextElement();
			String stato= st.nextElement().toString();
			addErrore(ErroreFin.OPERAZIONE_INCOMPATIBILE_PER_BILANCIO.getErrore("GESTIONE"+" "+oggettoDaPopolare,stato));
			
			addPersistentActionError(ErroreFin.OPERAZIONE_INCOMPATIBILE_PER_BILANCIO.getErrore("").getCodice() + " "+ ErroreFin.OPERAZIONE_INCOMPATIBILE_PER_BILANCIO.getErrore("GESTIONE"+" "+oggettoDaPopolare,stato).getDescrizione());
			
			return "elencoCarta";
		}
		
		//verifica di abilitazione:
		if(!isAzioneAbilitata(CodiciOperazioni.OP_SPE_regCarta)){
			addPersistentActionError(ErroreFin.UTENTE_NON_ABILITATO.getErrore("").getCodice() + " "+ErroreFin.UTENTE_NON_ABILITATO.getErrore("").getDescrizione());
			return "elencoCarta";
		}
		
		//carichiamo i dati da servizio:
		caricaDatiCartaInRegolazione(Integer.parseInt(getNumero()), Integer.parseInt(getAnno()));
		//
		
	    return SUCCESS;
	}
	
	/**
	 * gestione tasto indietro
	 * @return
	 */
	public String indietro(){
		// torno indietro a elenco risultati
		return "indietroRisultati";
	}
	
	/**
	 * completa i dati di riga
	 * @return
	 */
	public String completaRiga(){
		
		//info per il debug:
		debug("completa riga", "click su completa riga");
		
		//controlliamo se e' stata selezionata correttamente:
		if(StringUtils.isEmpty(getCkRigaSelezionata())){
			//errore di selezione
			addErrore(ErroreCore.SELEZIONARE_UN_SOLO_ELEMENTO.getErrore(""));	
		}else{
			 String[] righeSelezionate = getCkRigaSelezionata().split(",");
			 if(righeSelezionate.length>1){
				//errore di selezione
				 addErrore(ErroreCore.SELEZIONARE_UN_SOLO_ELEMENTO.getErrore(""));	
			 }
		}
		
		// se il controllo ha settato errori:
		if(hasActionErrors()){
			return INPUT;
		}
		
		//OK SELEZIONATO UN SOLO ELEMENO, CONTROLLI SULL'ELEMENTO:
		PreDocumentoCarta rigaSelezionata = dettaglioRigaSelezionata(getCkRigaSelezionata());
		if(FinUtility.haImpegnoOrSubImpegno(rigaSelezionata)){
			String msg = "La riga indicata e' gia' collegata ad un movimento";
			addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Riga " + rigaSelezionata.getNumero(), msg));	
		}
		
		// se il controllo ha settato errori:
		if(hasActionErrors()){
			return INPUT;
		}
		
		//settiamo il numero della riga selezionata:
		model.setNumeroRigaSelezionata(getCkRigaSelezionata());
		
		// pulisco i dati della form di collega impegno
		// della pagina successiva
		pulisciFormCollegaImpegno();
		
		// pulisco e mi dice che non ho fatto regolarizzazioni
		sessionHandler.setParametro(FinSessionParameter.REGOLARIZZAZIONE_AVVENUTA, WebAppConstants.No);
		
		// vado in regolazione carta singola
		return "regolazioneCarta";
		
	}
	
	/**
	 * verifica se l'id indicato e' presente tra quelli in lista
	 * @param idRiga
	 * @param listaRigheDaRego
	 * @return
	 */
	private boolean idRigaContenutoInLista(String idRiga, List<String> listaRigheDaRego){
		boolean trovato = false;
		Iterator<String> it = listaRigheDaRego.iterator();
		//itero sulla lista di id (salvati sotto forma di stringhe):
		while(it.hasNext()){
			String s = it.next();
			if(s.equalsIgnoreCase(idRiga)){
				//ho trovato l'elemento ricercato
				trovato = true;
				break;
			}
		}
		return trovato;
	}
	
	/**
	 * utility per passare da un tokenizer ad una lista di stringhe
	 * @param st
	 * @return
	 */
	private List<String> ritornaListaId(StringTokenizer st){
		
		//istanzio la lista di stringhe:
		List<String> s = new ArrayList<String>();
		
		//itero gli elementi:
		while(st.hasMoreElements()){
			//aggiungo uno ad uno gli elementi in lista:
			String idRiga = (String)st.nextElement();
			s.add(idRiga);
		}	
		
		//ritorno la lista appena costruita:
		return s;
	}
	
	/**
	 * ritorna i pre doc delle righe indicate in input
	 * @param listaRigheDaRego
	 * @return
	 */
	private List<PreDocumentoCarta> ritornaListaPreDoc(List<String> listaRigheDaRego){
		
		List<PreDocumentoCarta> listaPreDocDaRego  = new ArrayList<PreDocumentoCarta>();
		
		//verifico che ci siano pre documenti carta:
		if(null!=model.getCartaContabileDaRicerca().getListaPreDocumentiCarta() && model.getCartaContabileDaRicerca().getListaPreDocumentiCarta().size()>0){
			
			Iterator<PreDocumentoCarta> it = model.getCartaContabileDaRicerca().getListaPreDocumentiCarta().iterator();
			
			//Iter sui pre documenti carta:
			while(it.hasNext()){
				
				PreDocumentoCarta pd = it.next();
				
				//se il numero di questo pre documento carta e' tra quelli richiesti in input
				//aggiungo tale pre documento alla lista che verra' restituita:
				if( idRigaContenutoInLista(String.valueOf(pd.getNumero()), listaRigheDaRego)){
					listaPreDocDaRego.add(pd);
				}
			}
			
		}
		
		//restituisco la lista appena popolata:
		return listaPreDocDaRego;
	}
	
	/**
	 * Effettua la regolarizzazione degli elementi selezionati:
	 * @return
	 */
	public String regolarizzaSelezionati(){
		
		//info per il debug:
		debug("cregolarizzaSelezionati", "click su regolarizza Selezionati");
		
		
		StringTokenizer  st = new StringTokenizer(getNumeroDaRegolarizzare(), ",");
		// ottengo la lista di id da regolarizzare
		List<String> listaRigheDaRego = ritornaListaId(st);
		
		// tramite gli id scelti compongo la lista di predoc da regolarizzare
		List<PreDocumentoCarta> listaPreDocDaRego = ritornaListaPreDoc(listaRigheDaRego);
		
		
		// ora ho la lista dei predoc da regolare --> listaPreDocDaRego
		// per ogni predoc genero un lista[1] di regolarizzazione (subDocumentoSpesa)
		// contenente impegno o importo
		
		Iterator<PreDocumentoCarta> itPreDoc = listaPreDocDaRego.iterator();
	
		while(itPreDoc.hasNext()){
			
			PreDocumentoCarta pdc = itPreDoc.next();
			// solo quelle con importo da rego maggiore di zero
			if(pdc.getImportoDaRegolarizzare().compareTo(BigDecimal.ZERO)>0){
				SubdocumentoSpesa sdp = new SubdocumentoSpesa();
				
				sdp.setImporto(pdc.getImportoDaRegolarizzare());
				sdp.setImpegno(pdc.getImpegno());
				
				//  se c'e' setto il subimpegno
				if(pdc.getSubImpegno()!=null) sdp.setSubImpegno(pdc.getSubImpegno());
				
				List<SubdocumentoSpesa> unSDSInLista = new ArrayList<SubdocumentoSpesa>();
				
				//UN SDS IN LISTA
				unSDSInLista.add(sdp);
				
				pdc.setListaSubDocumentiSpesaCollegati(unSDSInLista);
				
			}
		}
		

		// creo la request per la funzione di regolarizzazione
		RegolarizzaCartaContabile rcc = new RegolarizzaCartaContabile();
		
		rcc.setBilancio(sessionHandler.getBilancio());
		rcc.setEnte(sessionHandler.getEnte());
		rcc.setRichiedente(sessionHandler.getRichiedente());

        CartaContabile ccDaRego = clone(model.getCartaContabileDaRicerca());
        
        ccDaRego.setListaPreDocumentiCarta(listaPreDocDaRego);
		
		rcc.setCartaContabileDaRegolarizzare(ccDaRego);
		
		//richiamo il servizio di regolarizzazione:
		RegolarizzaCartaContabileResponse response = cartaContabileService.regolarizzaCartaContabile(rcc);
		
		//analizzo la risposta del servizio:
		if(response!=null && response.isFallimento()){
			//ci sono errori:
			addErrori(response.getErrori());
			return INPUT;
		}
		
		//setto la lista pre documenti carta ricaricata dal servizio:
		model.getCartaContabileDaRicerca().setListaPreDocumentiCarta(response.getCartaContabile().getListaPreDocumentiCarta());
		
		//presento il messagio di operazione eseguita correttamente:
		addActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		
		return SUCCESS;
		
	}
	
	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */
	
	public String getAnno() {
		return anno;
	}


	public void setAnno(String anno) {
		this.anno = anno;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	public String getCkRigaSelezionata() {
		return ckRigaSelezionata;
	}


	public void setCkRigaSelezionata(String ckRigaSelezionata) {
		this.ckRigaSelezionata = ckRigaSelezionata;
	}


	public String getNumeroDaRegolarizzare() {
		return numeroDaRegolarizzare;
	}


	public void setNumeroDaRegolarizzare(String numeroDaRegolarizzare) {
		this.numeroDaRegolarizzare = numeroDaRegolarizzare;
	}


	public String getNumeroDaRegolarizzarePassato() {
		return numeroDaRegolarizzarePassato;
	}


	public void setNumeroDaRegolarizzarePassato(String numeroDaRegolarizzarePassato) {
		this.numeroDaRegolarizzarePassato = numeroDaRegolarizzarePassato;
	}
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.carta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.carta.GestioneCartaModel;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabilePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabilePerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabileResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaCartaContabile;
import it.csi.siac.siacfinser.model.ric.RicercaCartaContabileK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

public class WizardRegolarizzaCartaAction extends AbstractCartaAction<GestioneCartaModel> {

	private static final long serialVersionUID = 1L;

	@Override
	public String getActionKey() {
		return "regolarizzaCarta";
	}
	
	public String pulisciRicercaImpegno() {
        
		// pulisci campi della form
		model.setIsnSubImpegno(false);
		model.setHasImpegnoSelezionatoPopup(false);

		model.setListaImpegniCompGuidata(new ArrayList<Impegno>());

		
		
		return "refreshPopupModalImpegno";
	}

	/**
	 *  pulisco i campi di collega impegno della pagina di regolazione
	 */
	protected void pulisciFormCollegaImpegno(){
		
	    model.setAnnoImpegno(null);
	    model.setNumeroImpegno(null);
	    model.setNumeroSub(null);
	    model.setImportoImpegno("");
        model.setHasImpegnoSelezionatoXPopup(false);
        model.setToggleCollegaImpegnoAperto(false);
		model.setHasCollegaImpegnoBloccato(false);
	}
	
	/**
	 * Azzero liste e info precedenti
	 */
	public void pulisciListeeSchedaPopup(){
		model.setListaImpegniCompGuidata(new ArrayList<Impegno>());

		model.setRadioImpegnoSelezionato(0);
		model.setHasImpegnoSelezionatoPopup(false);
		model.setIsnSubImpegno(false);
		model.setnSubImpegno(null);
		model.setProvvedimentoPopup(null);
		model.setSoggettoPopup(null);
		model.setImpegnoPopup(null);
		model.setCapitoloPopup(null);
	}
	
	
	
	//RICERCA IMPEGNO PER CHIAVE
	protected RicercaImpegnoPerChiaveOttimizzato convertiModelPerChiamataServizioRicercaPerChiave() {
		//istanzio valori dell'impegno da cercare (valori da passare dalla ricerca)
		RicercaImpegnoPerChiaveOttimizzato parametroRicercaPerChiave = new RicercaImpegnoPerChiaveOttimizzato();
		RicercaImpegnoK impegnoDaCercare = new RicercaImpegnoK();
		BigDecimal numeroImpegno = new BigDecimal(String.valueOf(model.getNumeroImpegno()));
		Ente enteProva = new Ente();
		
		impegnoDaCercare.setAnnoEsercizio(sessionHandler.getAnnoBilancio());
		impegnoDaCercare.setNumeroImpegno(numeroImpegno);
		impegnoDaCercare.setAnnoImpegno(model.getAnnoImpegno());
		enteProva = model.getEnte();
			
		parametroRicercaPerChiave.setRichiedente(sessionHandler.getRichiedente());
		parametroRicercaPerChiave.setEnte(enteProva);
		parametroRicercaPerChiave.setpRicercaImpegnoK(impegnoDaCercare);
		
		return parametroRicercaPerChiave;
	}
	
	/* **************************************************************************** */
	/* generic pop up action														*/
	/* **************************************************************************** */
	

	/* **************************************************************************** */
	/*  transazione elementare														*/
	/* **************************************************************************** */
	

	protected void caricaDatiCartaInRegolazione(Integer numero, Integer anno){
		List<CartaContabile> listaCarteContabiles = new ArrayList<CartaContabile>();
		
		if(sessionHandler.getParametro(FinSessionParameter.REGOLARIZZAZIONE_AVVENUTA)!=null &&
			((String)sessionHandler.getParametro(FinSessionParameter.REGOLARIZZAZIONE_AVVENUTA)).equals(WebAppConstants.Si)	){
			//occorre ricaricare i dati
			debug("regolarizzazione", "devo ricaricare i dati");
			ParametroRicercaCartaContabile prcc = (ParametroRicercaCartaContabile)sessionHandler.getParametro(FinSessionParameter.PAR_RICERCA_CARTA); 
			
			//costruisco la request per il servizio di ricerca:
			RicercaCartaContabile ricercaCartaContabile = new RicercaCartaContabile();
			ricercaCartaContabile.setParametroRicercaCartaContabile(prcc);
			ricercaCartaContabile.setRichiedente(sessionHandler.getRichiedente());
			ricercaCartaContabile.setEnte(sessionHandler.getEnte());
			
			//richiamo il servizio di ricerca:
			RicercaCartaContabileResponse response = cartaContabileService.ricercaCartaContabile(ricercaCartaContabile);
			
			//leggo i dati ottenuti:
			listaCarteContabiles = response.getElencoCarteContabili();
		}else{
			//non server ricaricare i dati, li leggo dal parametro in sessione:
			listaCarteContabiles = (List<CartaContabile>)sessionHandler.getParametro(FinSessionParameter.RIGHE_DA_REGOLARIZZARE);	
		}
		
		
	    //itero sulle righe carte contabili per vestirle con dati ultariori
		//richiamando per ognuna di esse il servizio di caricamento per chiave:
		if(listaCarteContabiles!=null && listaCarteContabiles.size()>0){
            Iterator<CartaContabile> itCarte = listaCarteContabiles.iterator();
            while(itCarte.hasNext()){
                CartaContabile cc = itCarte.next();
                if(cc.getBilancio().getAnno()==(anno) && cc.getNumero().intValue() == (numero)){
                	//se bilancio e numero coincidono   
                	
                	//compongo la request per il servizio:
                    RicercaCartaContabilePerChiave rcc = new RicercaCartaContabilePerChiave();
                    rcc.setEnte(sessionHandler.getEnte());
                    rcc.setRichiedente(sessionHandler.getRichiedente());
                    rcc.setCercaMdpCessionePerChiaveModPag(true);
                          
                    RicercaCartaContabileK k = new RicercaCartaContabileK();
                    k.setBilancio(sessionHandler.getBilancio());
                          
                    CartaContabile ricercaCarta = new CartaContabile();
                    ricercaCarta.setNumero(numero);
                    k.setCartaContabile(ricercaCarta);

                    rcc.setpRicercaCartaContabileK(k);
                          
                    //richiamo il servizio:
                    RicercaCartaContabilePerChiaveResponse responseCarta =  cartaContabileService.ricercaCartaContabilePerChiave(rcc);
                          
                    //analizzo la risposta del servizio:
                    if(null!=responseCarta && !responseCarta.isFallimento()){
                        // trovata la carta contabile la setto
                        model.setCartaContabileDaRicerca(responseCarta.getCartaContabile());
                    }
                                                  
                 }
            }
            
        }
		
		// calcolo la somma delle righe
		sommaRigheCarta();
		
		// verfico la presenza o meno di valuta estera
		valutaEstera();
		
		// pulisco e mi dice che non ho fatto regolarizzazioni
		sessionHandler.setParametro(FinSessionParameter.REGOLARIZZAZIONE_AVVENUTA, WebAppConstants.No);
		
		//vediamo se ci sono righe che si possono selezionare:
		boolean presenzaRighe = presenzaRigheSelezionabili();
		
		//setto tale informazione nel model:
		model.setPresenzaRigheSelezionabili(presenzaRighe);
	}
	
	/**
	 * somma gli importi di tutti i pre documenti carta
	 * e conta il numero righe
	 */
	protected void sommaRigheCarta(){
		
		//inizializzo le variabili di appoggio:
		BigDecimal sommatoriaRighe = new BigDecimal(0);
		int numeroRighe = 0;
		
		//verifico se ci sono pre documenti carta:
		if(model.getCartaContabileDaRicerca().getListaPreDocumentiCarta()!=null && model.getCartaContabileDaRicerca().getListaPreDocumentiCarta().size()>0){
			Iterator<PreDocumentoCarta> itRighe = model.getCartaContabileDaRicerca().getListaPreDocumentiCarta().iterator();
			
			//itero sulla lista pre documenti carta:
			while(itRighe.hasNext()){
				
				PreDocumentoCarta pc = itRighe.next();
				sommatoriaRighe = sommatoriaRighe.add(pc.getImporto());
				numeroRighe = numeroRighe+1;
			}
		}
		
		// setto la sommatoria da visualizzare in testa alla pagina
		model.setSommaRigheCarta(sommatoriaRighe);
		model.setNumeroRighe(String.valueOf(numeroRighe));
	}
	
	/**
	 * Se c'e' una carta estera setta il campo ValutaPerRegolarizzazione nel model
	 */
	private void valutaEstera(){
		if(null!=model.getCartaContabileDaRicerca().getListaCarteEstere() && model.getCartaContabileDaRicerca().getListaCarteEstere().size()>0){
			// prendo l'unico elemento della lista
			model.setValutaPerRegolarizzazione(model.getCartaContabileDaRicerca().getListaCarteEstere().get(0));
		}else {
			// lo setto a null cosi effettuo il controllo
			model.setValutaPerRegolarizzazione(null);
		}
	}
	
	/**
	 * Verifica se esiste almeno una riga selezionabile
	 * @return
	 */
	private boolean presenzaRigheSelezionabili(){
		boolean presenzaRighe = false;
		if(model.getCartaContabileDaRicerca()!=null && !isEmpty(model.getCartaContabileDaRicerca().getListaPreDocumentiCarta())){
			//itero su lista predocumenti carta:
			for(PreDocumentoCarta preDocIt : model.getCartaContabileDaRicerca().getListaPreDocumentiCarta()){
				if(preDocIt!=null && preDocIt.getImportoDaRegolarizzare()!=null && preDocIt.getImportoDaRegolarizzare().compareTo(BigDecimal.ZERO)>0){
					//una riga e' selezionabile se l'importo da regolarizzare 
					//del predocumento e' maggiore di zero
					presenzaRighe = true;
					break;
				}
			}
		}
		return presenzaRighe;
	}
	
	protected PreDocumentoCarta dettaglioRigaSelezionata(String numeroRigaSelezionata){
		PreDocumentoCarta dettaglioRiga = new PreDocumentoCarta(); 
		if(model.getCartaContabileDaRicerca().getListaPreDocumentiCarta()!=null && model.getCartaContabileDaRicerca().getListaPreDocumentiCarta().size()>0){
	    	Iterator<PreDocumentoCarta> it =model.getCartaContabileDaRicerca().getListaPreDocumentiCarta().iterator();
	    	while(it.hasNext()){
	    		PreDocumentoCarta pd = it.next();
	    		if(pd.getNumero()==( Integer.valueOf(numeroRigaSelezionata))){
	    		  dettaglioRiga = pd;	
	    		}
	    	}
		 } 
		return dettaglioRiga; 
	}
	

}
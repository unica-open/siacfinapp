/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.carta;

import org.apache.struts2.ServletActionContext;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.SoggettoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaCartaContabileResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaCartaContabileResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabilePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabilePerChiaveResponse;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.carta.CartaContabile.StatoOperativoCartaContabile;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ric.RicercaCartaContabileK;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ElencoCartaAction extends ActionKeyRicercaCartaAction {
	
	private static final long serialVersionUID = 1L;

	public Integer uidCartaDaAnnullare;
	public Integer numeroCartaDaAnnullare;
	public String numRegistrazioneCarta;
	public Integer numeroCartaDaModificare;
	public String nuovoStatoCartaDaModificare;
	private boolean clearPagina;
	private String pulisciPagine;
	
	public boolean loadDati = true;
	
	
	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo:
		this.model.setTitolo("Risultati ricerca carta contabile");
	}

	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		debug("pulisce pagine", pulisciPagine);
		if(null!=pulisciPagine && pulisciPagine.equalsIgnoreCase(WebAppConstants.Si)){
			setClearPagina(true);
		}
		
		if(presenzaPaginazione(ServletActionContext.getRequest())){
			
			// ho cliccato sulla paginazione
			int paginata = paginazioneRichiesta(ServletActionContext.getRequest());
			if(paginata!=0){
				model.setPremutoPaginazione(paginata);
			}
			
		}
		
		//eseguo carta contabile:
		executeRicercaCartaContabile();
		
		// pulisce le liste quando effettuo una ricerca
		puliscoDatiRicerca();
		
	    return SUCCESS;
	}
	
	private void puliscoDatiRicerca(){
		// pulisci il form di ricerca con 
		// tutti i suoi campi
		
		model.setNumeroCartaA("");
		model.setNumeroCartaDa("");
		model.setDescrizioneCarta("");
		model.setProvvedimento(new  ProvvedimentoImpegnoModel());
		model.setCapitoloSelezionato(false);
		model.setHasImpegnoSelezionatoXPopup(false);
		model.setProvvedimentoSelezionato(false);
		model.setSoggettoSelezionato(false);
		model.setCapitolo(new CapitoloImpegnoModel());
		model.setAnnoImpegno(null);
		// jira-1380
		model.setNumeroImpegno(null);
		model.setNumeroSub(null);
		model.setStatoCarta("");
		model.setDataScadenzaCartaA("");
		model.setDataScadenzaCartaDa("");
		model.setnImpegno(null);
		model.setnSubImpegno(null);
		model.setSoggetto(new SoggettoImpegnoModel());
	
	}
	
	/**
	 * funzione utilizzata da jsp per abilitare/disabilitare
	 * tasto
	 * 
	 * @param azione
	 * @param stato
	 * @return
	 */
	public boolean abilitaTasto(String azione, StatoOperativoCartaContabile stato){
		
		//AZIONE AGGIORNA:
		if(azione.equalsIgnoreCase("aggiorna")){
			
			boolean abilitato = isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_AGGCARTA) || isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_AGGCARTARAGIO);
			if(!abilitato){
				return false;
			}
			 
			 
			if(stato.equals(StatoOperativoCartaContabile.PROVVISORIO) || stato.equals(StatoOperativoCartaContabile.COMPLETATO)|| stato.equals(StatoOperativoCartaContabile.TRASMESSO)){
				return true;
			}
		}
		
		//AZIONE ANNULLA:
		if(azione.equalsIgnoreCase("annulla")){
			
			boolean abilitato = (isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_AGGCARTA) || isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_AGGCARTARAGIO));
			if(!abilitato){
				return false;
			}
			
			if(stato.equals(StatoOperativoCartaContabile.PROVVISORIO) || stato.equals(StatoOperativoCartaContabile.COMPLETATO)){
				return true;
			}
		}
		
		//AZIONE REGOLARIZZA:
		if(azione.equalsIgnoreCase("regolarizza")){
			
			boolean abilitato = isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_regCarta);
			 if(!abilitato){
					return false;
			 }
			
			if(stato.equals(StatoOperativoCartaContabile.TRASMESSO)){
				return true;
			}
		}
		
		//AZIONE COMPLETA:
		if(azione.equalsIgnoreCase("completa")){
			
			 boolean abilitato = isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_AGGCARTA);
			 if(!abilitato){
					return false;
			 }
			
			if(stato.equals(StatoOperativoCartaContabile.PROVVISORIO)){
				return true;
			}
		}
		
		//AZIONE TRASMETTI:
		if(azione.equalsIgnoreCase("trasmetti")){
			
			boolean abilitato = isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_AGGCARTARAGIO);
			if(!abilitato){
				return false;
			}
			
			if(stato.equals(StatoOperativoCartaContabile.COMPLETATO)){
				return true;
			}
		}
		
		//AZIONE RIPORTA A PROVVISORIO:
		if(azione.equalsIgnoreCase("ripAProv")){
			
			boolean abilitato = isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_AGGCARTARAGIO);
			if(!abilitato){
				return false;
			}
			
			if(stato.equals(StatoOperativoCartaContabile.COMPLETATO)){
				return true;
			}
		}
		
		//AZIONE RIPORTA A COMPLETATO:
		if(azione.equalsIgnoreCase("ripACompl")){
			
			boolean abilitato = isAzioneConsentita(AzioneConsentitaEnum.OP_SPE_AGGCARTARAGIO);
			if(!abilitato){
				return false;
			}
			
			if(stato.equals(StatoOperativoCartaContabile.TRASMESSO)){
				return true;
			}
		}
		
		return false;
		
	}
	
	
	/**
	 * tasto annulla carta
	 * 
	 * @return
	 * @throws Exception
	 */
	public String annullaCarta() throws Exception{
		
		// verfico eventuale click precedente di paginazione
		if(!presenzaPaginazione(ServletActionContext.getRequest())){
			CartaContabile cartaDaAnnullare =new CartaContabile();
			//cerco la carta con il numero da annullare:
			for(CartaContabile carta : model.getElencoCarteContabili()){
				if(carta.getNumero().equals(getNumeroCartaDaAnnullare())){
					//trovata
					cartaDaAnnullare= carta;
				}
			}
			
			// cerco l'elemento e guardo se e' annullabile
			if(cartaDaAnnullare.getListaPreDocumentiCarta()!= null && cartaDaAnnullare.getListaPreDocumentiCarta().size()>0 ){
				//itero sui predocumenti carta:
				for(PreDocumentoCarta pdc:cartaDaAnnullare.getListaPreDocumentiCarta()){
					if(pdc.getListaSubDocumentiSpesaCollegati()!= null && pdc.getListaSubDocumentiSpesaCollegati().size()>0 ){
						//itero sui sub documenti spesa collegati:
						for(SubdocumentoSpesa sds : pdc.getListaSubDocumentiSpesaCollegati()){
							DocumentoSpesa dsc = sds.getDocumento();
							if(dsc!=null){
								TipoDocumento tipoDoc = dsc.getTipoDocumento();
								if(tipoDoc!=null){
									if(tipoDoc.getCodice().equalsIgnoreCase(CostantiFin.D_DOC_TIPO_CARTA_CONTABILE_CODE)){
										
										// in alcuni casi potrebbe arrivare dal servizio
										// ricercaDettaglioQuotaSpesa lo stato = Null
										if(dsc.getStatoOperativoDocumento()!=null){
											if(!dsc.getStatoOperativoDocumento().equals(StatoOperativoDocumento.ANNULLATO)){
												addErrore(ErroreFin.ANNULLAMENTO_CARTA_IMPOSSIBILE.getErrore("la carta contiene righe collegate a documenti di tipo CCN, occorre annullare i documenti per procedere all'annullamento della carta"));
												break;
											}
										}	
									}
								}
							}
						}
					}
				}
			}
			
			if(hasActionErrors()){
				//ci sono errori
				return INPUT;	
			}
				
			//istanzio la request per richiamare il servizio di annullamento:
			AnnullaCartaContabile request = new AnnullaCartaContabile();
			request.setEnte(sessionHandler.getEnte());
			request.setRichiedente(sessionHandler.getRichiedente());
			
			request.setBilancio(sessionHandler.getBilancio());
			
			request.setCartaContabileDaAnnullare(cartaDaAnnullare);
			// procedo con la funzione di annullamento:
			AnnullaCartaContabileResponse response = cartaContabileService.annullaCartaContabile(request);
			
			//analizzo la risposta del serivizio:
			if(response.isFallimento()) {
				//il servizio riporta errori
				if(null!=response.getErrori() && null!=response.getErrori().get(0)){
					addPersistentActionError(response.getErrori().get(0).getCodice()+" "+response.getErrori().get(0).getDescrizione());
				}
				return INPUT;
			}
			
			// rieseguo la ricerca
			executeRicercaCartaContabile();
			
			//setto il messaggio di operazione effettuata correttamente:
			addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
		}else{
			// rieseguo la ricerca
			executeRicercaCartaContabile();
		}
		return SUCCESS;
	}
	
	/**
	 * cambio dello stato
	 * @return
	 * @throws Exception
	 */
	public String cambiaStatoCarta() throws Exception{
		// verifico eventuale click precedente di paginazione
		if(!presenzaPaginazione(ServletActionContext.getRequest())){
			
			CartaContabile cartaDaModificare =new CartaContabile();
			for(CartaContabile carta : model.getElencoCarteContabili()){
				if(carta.getNumero().equals(getNumeroCartaDaModificare())){
					cartaDaModificare= carta;
				}
			}
	
			StatoOperativoCartaContabile nuovoStato=CartaContabile.StatoOperativoCartaContabile.valueOf(getNuovoStatoCartaDaModificare());
			
			AggiornaCartaContabile request = new AggiornaCartaContabile();
			request.setEnte(sessionHandler.getEnte());
			request.setRichiedente(sessionHandler.getRichiedente());
			request.setBilancio(sessionHandler.getBilancio());
			
			//istanzio la request per la ricerca carta per chiave
			RicercaCartaContabilePerChiave rcc = new RicercaCartaContabilePerChiave();
	        rcc.setEnte(sessionHandler.getEnte());
	        rcc.setRichiedente(sessionHandler.getRichiedente());
	        
	        RicercaCartaContabileK k = new RicercaCartaContabileK();
	        k.setBilancio(sessionHandler.getBilancio());
	        
	        CartaContabile ricercaCarta = new CartaContabile();
	        ricercaCarta.setNumero(cartaDaModificare.getNumero());
	        k.setCartaContabile(ricercaCarta);
	
	        rcc.setpRicercaCartaContabileK(k);
	        rcc.setCercaMdpCessionePerChiaveModPag(true);
	        
	        //richiamo il servizio di ricerca carta contabile per chiave:
	        RicercaCartaContabilePerChiaveResponse responseCarta =  cartaContabileService.ricercaCartaContabilePerChiave(rcc);
			
	        
	        //uso la carta contabile appena ricaricata come base
	        //per invocare il servizio di aggiornamento al quale
	        //passo il nuovo stato:
	        CartaContabile cartaDaRicerca = responseCarta.getCartaContabile();
	        cartaDaRicerca.setStatoOperativoCartaContabile(nuovoStato);
			request.setCartaContabile(cartaDaRicerca);

			//invoco il servizio di aggiornamento che cambiera' lo stato della carta:
			AggiornaCartaContabileResponse response = cartaContabileService.aggiornaCartaContabile(request);
			
			//analizzo l'esito del servizio:
			if (response.isFallimento()) {
				//presenza errori
				addErrori(response.getErrori());
				return INPUT;
			}else{
				//tutto ok
				addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " " + ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
			}
			
			cartaDaModificare.setStatoOperativoCartaContabile(nuovoStato);
		}else{
			// rieseguo la ricerca
			executeRicercaCartaContabile();
		}
		
		return SUCCESS;
	}
	
	/**
	 * operazione trasmetti carta
	 * @return
	 */
	public String trasmettiCarta(){
		addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getCodice() + " "  + "Carta trasmessa con successo");
		return SUCCESS;
	}

	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */
	
	public Integer getUidCartaDaAnnullare() {
		return uidCartaDaAnnullare;
	}


	public void setUidCartaDaAnnullare(Integer uidCartaDaAnnullare) {
		this.uidCartaDaAnnullare = uidCartaDaAnnullare;
	}


	public Integer getNumeroCartaDaAnnullare() {
		return numeroCartaDaAnnullare;
	}


	public void setNumeroCartaDaAnnullare(Integer numeroCartaDaAnnullare) {
		this.numeroCartaDaAnnullare = numeroCartaDaAnnullare;
	}


	public String getNumRegistrazioneCarta() {
		return numRegistrazioneCarta;
	}


	public void setNumRegistrazioneCarta(String numRegistrazioneCarta) {
		this.numRegistrazioneCarta = numRegistrazioneCarta;
	}


	public boolean isLoadDati() {
		return loadDati;
	}


	public void setLoadDati(boolean loadDati) {
		this.loadDati = loadDati;
	}


	public Integer getNumeroCartaDaModificare() {
		return numeroCartaDaModificare;
	}


	public void setNumeroCartaDaModificare(Integer numeroCartaDaModificare) {
		this.numeroCartaDaModificare = numeroCartaDaModificare;
	}


	public String getNuovoStatoCartaDaModificare() {
		return nuovoStatoCartaDaModificare;
	}


	public void setNuovoStatoCartaDaModificare(String nuovoStatoCartaDaModificare) {
		this.nuovoStatoCartaDaModificare = nuovoStatoCartaDaModificare;
	}


	public boolean isClearPagina() {
		return clearPagina;
	}


	public void setClearPagina(boolean clearPagina) {
		this.clearPagina = clearPagina;
	}


	public String getPulisciPagine() {
		return pulisciPagine;
	}


	public void setPulisciPagine(String pulisciPagine) {
		this.pulisciPagine = pulisciPagine;
	}

}

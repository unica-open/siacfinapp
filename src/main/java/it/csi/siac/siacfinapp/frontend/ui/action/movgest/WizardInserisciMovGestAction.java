/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;

import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siacbilser.model.VincoloCapitoli;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.handler.session.FinSessionParameter;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.CapitoloImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ImpegniPluriennaliModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;
import it.csi.siac.siacfinapp.frontend.ui.util.WebAppConstants;
import it.csi.siac.siacfinser.CodiciOperazioni;
import it.csi.siac.siacfinser.frontend.webservice.msg.EsistenzaProgetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.EsistenzaProgettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceAccertamentiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceImpegniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

public class WizardInserisciMovGestAction extends WizardGenericMovGestAction {

	private static final long serialVersionUID = 1L;
	
	//CAMPI INSERIMENTO STEP 3
	public int campoPerPulire;
	public String numeroImpegno;
	public String annoImpegno;
	
	public String numeroImpegnoStruts3;
	public String annoImpegnoStruts3;
	
	
	public String numeroAccertamento;
	public String annoAccertamento;
	public boolean impegnoRiaccGiaRicercata;
	public boolean accPlurRiaccGiaRicercata;
	public boolean impegnoOrigineGiaRicercata;
	
	public String doveMiTrovo;
	
	public boolean pluriennalePrimeNoteEsercizioInCorso;
	public boolean showModaleConfermaAnnoDiCompetenzaInCorso;
	public boolean fromModaleConfermaAnnoDiCompetenzaInCorso;
	
	public boolean salvaConSDF() {
		if(oggettoDaPopolareImpegno()){
			//vale solo per impegni
			
			boolean erroreImportoCapitolo = controlloImporti(model.getStep1Model().getAnnoImpegno());
			if(erroreImportoCapitolo){
				
				//abilitazione all'azione OP-SPE-gestisciImpegnoSDF
				boolean abilitatoAzioneGestisciImpegnoSDF = abilitatoAzioneGestisciImpegnoSDF();
				
				if(abilitatoAzioneGestisciImpegnoSDF){
					return true;
				}
			}
		}
		return false;
	}
	
	
	public boolean movimentoConAnnoPluriennale()  {
	
		
		if(model.getStep1Model().getAnnoImpegno() > Integer.parseInt(sessionHandler.getAnnoEsercizio())){
						
			log.debug("movimentoConAnnoPluriennale","sto inserendo un movimento con anno successivo all'anno di esercizio!");

			return true;
			
		} else return false;
		
	}
	
	public String forzaProsegui() throws Exception{
		if(model.getStep1Model().isCheckproseguiRiacc()){
			model.getStep1Model().setCheckproseguiRiacc(false);
			model.getStep1Model().setRiaccertato(WebAppConstants.Si);
			setImpegnoRiaccGiaRicercata(true);
			
		}
		
		if(model.getStep1Model().isCheckproseguiOrigin()){
			model.getStep1Model().setCheckproseguiOrigin(false);
			setImpegnoOrigineGiaRicercata(true);
		}
		return prosegui();
	}
	
	public String prosegui() throws Exception {
		setMethodName("prosegui");
		List<Errore> listaErrori = new ArrayList<Errore>();
		boolean erroreAnnoImpegno = false;
		model.getStep1Model().setCheckproseguiRiacc(false);
		model.getStep1Model().setCheckproseguiOrigin(false);
		
		//Controllo dello stato del Bilancio
		if(oggettoDaPopolareImpegno()){
			// precondizioni IMPEGNO
			if(isAbilitatoGestisciImpegnoDec()){
				// decentrato
				setAzioniDecToCheck(CodiciOperazioni.OP_SPE_gestisciImpegnoDecentrato);
				if(!checkAzioniDec()){
				   addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));	
				   return INPUT;
				}
			}else if(isAbilitatoGestisciImpegno()){
				    // master
				setAzioniToCheck(CodiciOperazioni.OP_SPE_gestisciImpegno);
				if(!checkAzioni()){
					   addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));	
					   return INPUT;
					}
			}else{
				 addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));
				 return INPUT;
			}
			
			if(controlloStatoBilancio(model.getStep1Model().getAnnoImpegno(),doveMiTrovo,"impegno")){
				return INPUT;
			}
		}else{	
			// precondizioni ACCERTAMENTO
			if(isAbilitatoGestisciAccertamentoDec()){
				// decentrato
				setAzioniDecToCheck(CodiciOperazioni.OP_ENT_gestisciAccertamentoDecentrato);
				if(!checkAzioniDec()){
				   addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));	
				   return INPUT;
				}
			}else if(isAbilitatoGestisciAccertamento()){
				    // master
				setAzioniToCheck(CodiciOperazioni.OP_ENT_gestisciAccertamento);
				if(!checkAzioni()){
					   addErrore(ErroreFin.UTENTE_NON_ABILITATO.getErrore(""));	
					   return INPUT;
					}
			}
			
			if(controlloStatoBilancio(model.getStep1Model().getAnnoImpegno(),doveMiTrovo,"accertamento")){
				return INPUT;
			}
		}
		//Controllo la presenza di errori sugli anni 
		List<Errore> erroriAnni =controlloAnni();
		if(!erroriAnni.isEmpty()){
			for(Errore e:erroriAnni){
				listaErrori.add(e);
			}
		}
		
	    if(model.getStep1Model().getCapitolo().getNumCapitolo() == null || model.getStep1Model().getCapitolo().getNumCapitolo().intValue() == 0){
		    listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Capitolo "));
	    }
	    if(model.getStep1Model().getCapitolo().getArticolo() == null){
		    listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Articolo "));
	    }
	    if(model.getStep1Model().getAnnoImpegno() == null || model.getStep1Model().getAnnoImpegno().intValue() == 0){
	    	if(oggettoDaPopolareImpegno()){
		    listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Impegno "));
	    	}else{
	    		 listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Accertamento "));
	    	}
		    erroreAnnoImpegno = true;
	    } else {
	    	if (model.getStep1Model().getAnnoImpegno().intValue() < Integer.valueOf((sessionHandler.getAnnoEsercizio())).intValue()) {
	    		
	    		if(!isBilancioPrecedenteInPredisposizioneConsuntivo()){
	    			listaErrori.add(ErroreFin.ANNO_MOVIMENTO_NON_VALIDO.getErrore(""));
	    		} else {
	    			
	    			//ANCHE IL NUMERO DEVE ESSERE OBBLIGATORIO IN QUESTO CASO:
	    			if (model.getStep1Model().getNumeroImpegno() == null ||
	    					model.getStep1Model().getNumeroImpegno().intValue() == 0) {
	    				listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Impegno "));
	    			}
	    			
	    		}
	    		
	    		
	    	} else {
	    		model.getStep1Model().setNumeroImpegno(null);
	    	}
	    }
	    if(oggettoDaPopolareImpegno()){
	    	  if(model.getStep1Model().getTipoImpegno() == null || "".equalsIgnoreCase(model.getStep1Model().getTipoImpegno())){
	   	 	   listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Impegno "));
	   	    }
	    }
	    
	    //OBBLIGATORIETA' PROGETTO:
	    listaErrori = controlloObbligatorietaProgetto(listaErrori);
	    //
	    
	    
	    
  		//verifico l'esistenza del progetto
	    if(StringUtils.isNotEmpty(model.getStep1Model().getProgetto())){
	    	EsistenzaProgetto ep = new EsistenzaProgetto();
	    	ep.setCodiceProgetto(model.getStep1Model().getProgetto());
	    	ep.setRichiedente(sessionHandler.getRichiedente());
	    	ep.setBilancio(sessionHandler.getBilancio());
	    	ep.setCodiceTipoProgetto(TipoProgetto.GESTIONE.getCodice());
	    	
	    	EsistenzaProgettoResponse esistenzaResp = genericService.cercaProgetto(ep);
	    	
	    	if(esistenzaResp.isFallimento()){
	    		if(!esistenzaResp.isEsisteProgetto()){
	    			listaErrori.add(ErroreCore.ENTITA_INESISTENTE.getErrore("codice progetto", model.getStep1Model().getProgetto()));
	    		}
	    	} else {
	    		Progetto p = new Progetto();
	    		p.setCodice(model.getStep1Model().getProgetto());
	    		p.setBilancio(sessionHandler.getBilancio());
	    		p.setTipoProgetto(TipoProgetto.GESTIONE);
	    		model.getStep1Model().getProgettoImpegno().setProgetto(p);
	    	}
	    }
	    
	    if(model.getStep1Model().getAnnoFinanziamento()!=null){
	    	if(model.getStep1Model().getAnnoFinanziamento()>model.getStep1Model().getAnnoImpegno()){
	    		listaErrori.add(ErroreFin.INCONGRUENZA_TRA_I_PARAMETRI_IMPEGNO.getErrore(""));
	    	}
	    }
	  
	    if(model.getStep1Model().getImportoFormattato() == null || model.getStep1Model().getImportoFormattato().equals("")){
	 	   listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo "+model.getLabels().get(LABEL_OGGETTO_GENERICO)));
	    }else{
	    	try{
	    		
	    		boolean erroreImportoZero = false;
	    		
	    		// converto
	    		model.getStep1Model().setImportoImpegno(convertiImportoToBigDecimal(model.getStep1Model().getImportoFormattato()));
	    		model.getStep1Model().setImportoFormattato(convertiBigDecimalToImporto(model.getStep1Model().getImportoImpegno()));
	    		
	    		//CONTROLLO ANCORA CHE NON SIA MINORE O UGUALE A ZERO:
	    		if (model.getStep1Model().getImportoImpegno().compareTo(BigDecimal.ZERO) == 0) {
	    			
	    			//Se l'importo e' uguale a zero e' accettabile solo se residuo e anno precendente in predisposizone consuntivo
	    			//e si tratta di un ACCERTAMENTO
	    			
	    			if(oggettoDaPopolareImpegno()){
	    				//SE IMPEGNO SEMPRE ERRORE PER IMPORTO ZERO
	    				erroreImportoZero = true;
	    			}
	    			
	    			if (model.getStep1Model().getAnnoImpegno().intValue() >= Integer.valueOf((sessionHandler.getAnnoEsercizio())).intValue() ||
	    					!isBilancioPrecedenteInPredisposizioneConsuntivo()) {
	    				erroreImportoZero = true;
	    				
	    			}
	    		} else if (model.getStep1Model().getImportoImpegno().compareTo(BigDecimal.ZERO) < 0) {
	    			//se negativo sempre errore
	    			erroreImportoZero = true;
	    		}
	    		
	    		if(erroreImportoZero){
	    			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo "+model.getLabels().get(LABEL_OGGETTO_GENERICO)));
	    		}
	    		
	    	}catch(Exception e){
	    		if(oggettoDaPopolareImpegno()){
	    			listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Importo Impegno ", "numerico"));
		    	}else{
		    		listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Importo Accertamento ", "numerico"));
		    	}
	    	}
	    }
	    
	    //IMPEGNO/ACCERTAMENTO RIACCERTATO
	    //2.8.2.2
	    if (WebAppConstants.No.equalsIgnoreCase(model.getStep1Model().getRiaccertato())) {
	    	model.getStep1Model().setAnnoImpRiacc(null);
	    	model.getStep1Model().setNumImpRiacc(null);
	    } else {
	    	if (model.getStep1Model().getAnnoImpRiacc() != null && model.getStep1Model().getNumImpRiacc() != null && model.getStep1Model().getAnnoImpegno()!=null ) {
		    	if (model.getStep1Model().getAnnoImpRiacc().compareTo(model.getStep1Model().getAnnoImpegno())>=0) {
		    		listaErrori.add(ErroreFin.INCONGRUENZA_TRA_I_PARAMETRI_RIACCERTAMENTO.getErrore(model.getLabels().get(LABEL_OGGETTO_GENERICO),model.getLabels().get(LABEL_OGGETTO_GENERICO)));
		    	} else {
		    		if(sessionHandler.getFaseBilancio().equalsIgnoreCase("O") && model.getStep1Model().getAnnoImpRiacc()>sessionHandler.getBilancio().getAnno()){
		    			StringTokenizer st = new StringTokenizer(sessionHandler.getDescrizioneAnnoBilancio(),"-");
		    			st.nextElement();
		    			String stato = st.nextElement().toString();
			    		listaErrori.add(ErroreFin.OPERAZIONE_INCOMPATIBILE_PER_BILANCIO.getErrore("Da Riaccertamento",stato));
			    		return INPUT;
	    			}
	    			
		    		
		    		if(oggettoDaPopolareImpegno() && !isImpegnoRiaccGiaRicercata()){
		    			RicercaImpegnoPerChiaveOttimizzatoResponse respRk = null;
		    			RicercaImpegnoPerChiaveOttimizzato rip = new RicercaImpegnoPerChiaveOttimizzato();
			    		rip.setEnte(sessionHandler.getEnte());
			    		rip.setRichiedente(sessionHandler.getRichiedente());
			    		RicercaImpegnoK k = new RicercaImpegnoK();
			    		//JIRA 630
			    		k.setAnnoEsercizio(model.getStep1Model().getAnnoImpRiacc());
			    		k.setAnnoImpegno(model.getStep1Model().getAnnoImpRiacc());
			    		k.setNumeroImpegno(new BigDecimal(model.getStep1Model().getNumImpRiacc()));
			    		rip.setpRicercaImpegnoK(k);
			    		
			    		//MARZO 2016: fix per performance
			    		rip.setCaricaSub(false);
			    		//
			    		
			    		respRk = movimentoGestionService.ricercaImpegnoPerChiaveOttimizzato(rip);
			    		if(respRk!=null && respRk.getImpegno()!=null){
			    			if(respRk.getImpegno().getAnnoOriginePlur()!=0 && respRk.getImpegno().getNumeroOriginePlur() !=null){
			    				if(model.getStep1Model().getAnnoImpOrigine() == null && model.getStep1Model().getNumImpOrigine() == null){
			    					model.getStep1Model().setAnnoImpOrigine(respRk.getImpegno().getAnnoOriginePlur());
				    				if(null!=respRk.getImpegno().getNumeroOriginePlur())
				    					model.getStep1Model().setNumImpOrigine(respRk.getImpegno().getNumeroOriginePlur().intValue());	
			    				}
			    			}
			    				if(respRk.getImpegno().getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("A")){
			    					listaErrori.add(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA_FIN.getErrore("Impegno da riaccertamento"," annullato"));
			    				}
			    		}else{
			    			
				    			listaErrori.add(ErroreCore.AGGIORNAMENTO_CON_CONFERMA.getErrore("Impegno riaccertato"," inesistente"));
			    		
			    		}
			    		
			    		}else if(!oggettoDaPopolareImpegno()){
			    			RicercaAccertamentoPerChiaveOttimizzatoResponse respAccRk = null;
			    			RicercaAccertamentoPerChiaveOttimizzato rip = new RicercaAccertamentoPerChiaveOttimizzato();
				    		rip.setEnte(sessionHandler.getEnte());
				    		rip.setRichiedente(sessionHandler.getRichiedente());
				    	 	RicercaAccertamentoK k = new RicercaAccertamentoK();
				    	 	//JIRA 630
				    		k.setAnnoEsercizio(model.getStep1Model().getAnnoImpRiacc());
				    		k.setAnnoAccertamento(model.getStep1Model().getAnnoImpRiacc());
				    		k.setNumeroAccertamento(new BigDecimal(model.getStep1Model().getNumImpRiacc()));
				    		rip.setpRicercaAccertamentoK(k);
			    			
			    		    respAccRk = movimentoGestionService.ricercaAccertamentoPerChiaveOttimizzato(rip);
			    		    if(respAccRk!=null && respAccRk.getAccertamento()!=null){
			    		    	// Jira-381 se c'e' un origine legata la metto nel model
				    			if(respAccRk.getAccertamento().getAnnoOriginePlur()!=0 && respAccRk.getAccertamento().getNumeroOriginePlur() !=null){
				    				if(model.getStep1Model().getAnnoImpOrigine() == null && model.getStep1Model().getNumImpOrigine() == null){
				    					
					    				model.getStep1Model().setAnnoImpOrigine(respAccRk.getAccertamento().getAnnoOriginePlur());
					    				if(null!=respAccRk.getAccertamento().getNumeroOriginePlur())
					    					model.getStep1Model().setNumImpOrigine(respAccRk.getAccertamento().getNumeroOriginePlur().intValue());	
				    				}
				    			}
				    			//Jira 381 (Questo controllo era gia' presente ancor prima che uscisse la Jira)
				    			if(respAccRk.getAccertamento().getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase("A")){
				    				model.getStep1Model().setAnnoImpRiacc(null);
					    			model.getStep1Model().setNumImpRiacc(null);
					    			model.getStep1Model().setRiaccertato(WebAppConstants.No);
					    			// da CU il messaggio deve essere bloccante
					    			listaErrori.add(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA_FIN.getErrore("Accertamento da riaccertamento"," annullato"));
				    			}
				    		}else{
					            addPersistentActionWarning(ErroreFin.MOVIMENTO_NON_TROVATO.getErrore("Accertamento riaccertato").getCodice()+" : "+ErroreFin.MOVIMENTO_NON_TROVATO.getErrore("Accertamento riaccertato").getDescrizione());
				    			
				    		}
		    		}
		    		
		    	}
		    } else {
		    	if (model.getStep1Model().getAnnoImpRiacc() != null && model.getStep1Model().getNumImpRiacc() == null) {
		    		if(oggettoDaPopolareImpegno()){
		    			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Impegno Riaccertamento "));
		    		}else{
		    			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Accertamento Riaccertamento "));
		    		}
		    		
		    	}  
		    	if (model.getStep1Model().getAnnoImpRiacc() == null && model.getStep1Model().getNumImpRiacc() != null) {
		    		if(oggettoDaPopolareImpegno()){
		    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Impegno Riaccertamento "));
		    	}else{
		    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Accertamento Riaccertamento "));
		    	}
		    	}
		    	if (model.getStep1Model().getAnnoImpRiacc() == null && model.getStep1Model().getNumImpRiacc() == null) {
		    		if(oggettoDaPopolareImpegno()){
		    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Impegno Riaccertamento "));
		    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Impegno Riaccertamento "));
		    	}else{
		    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Accertamento Riaccertamento "));
		    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Accertamento Riaccertamento "));
		    	}
		    	}
		    }
	    }
	    
	    //IMPEGNO/ACCERTAMENTO ORIGINE
	    //IMPLEMENTAZIONE CONTROLLI 2.8
	    //2.8.2.1
	    if (model.getStep1Model().getAnnoImpOrigine() != null && model.getStep1Model().getNumImpOrigine() != null && model.getStep1Model().getAnnoImpegno()!=null ) {
	    	
	    	if (model.getStep1Model().getAnnoImpOrigine().compareTo(model.getStep1Model().getAnnoImpegno())>=0) {
	    		
	    		listaErrori.add(ErroreFin.INCONGRUENZA_TRA_I_PARAMETRI_ORIGINE.getErrore(model.getLabels().get(LABEL_OGGETTO_GENERICO), model.getLabels().get(LABEL_OGGETTO_GENERICO)));
	    	}else{
	    		if(oggettoDaPopolareImpegno() && !isImpegnoOrigineGiaRicercata()){
	    			// IMPEGNI
			    		RicercaImpegnoPerChiaveOttimizzato rip = new RicercaImpegnoPerChiaveOttimizzato();
			    		rip.setEnte(sessionHandler.getEnte());
			    		rip.setRichiedente(sessionHandler.getRichiedente());
			    		RicercaImpegnoK k = new RicercaImpegnoK();
			    		// Jira - 630
			    		k.setAnnoEsercizio(model.getStep1Model().getAnnoImpOrigine());
			    		k.setAnnoImpegno(model.getStep1Model().getAnnoImpOrigine());
			    		k.setNumeroImpegno(new BigDecimal(model.getStep1Model().getNumImpOrigine()));
			    		rip.setpRicercaImpegnoK(k);
			    		
			    		//MARZO 2016: fix per performance
			    		rip.setCaricaSub(false);
			    		//
			    		
			    		RicercaImpegnoPerChiaveOttimizzatoResponse respRk = movimentoGestionService.ricercaImpegnoPerChiaveOttimizzato(rip);
			    		if(respRk==null || respRk.getImpegno()==null){
			    			listaErrori.add(ErroreCore.AGGIORNAMENTO_CON_CONFERMA.getErrore(model.getLabels().get(LABEL_OGGETTO_GENERICO)+" origine","inesistente"));
			    		}else if(respRk.getImpegno().getStatoOperativoMovimentoGestioneSpesa().equalsIgnoreCase("A")){
	    					listaErrori.add(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA_FIN.getErrore("Impegno di origine"," annullato"));
	    				}
			    		
			    		
			    	}else if(!oggettoDaPopolareImpegno()){
			    		// ACC
			    		RicercaAccertamentoPerChiaveOttimizzato rip = new RicercaAccertamentoPerChiaveOttimizzato();
			    		rip.setEnte(sessionHandler.getEnte());
			    		rip.setRichiedente(sessionHandler.getRichiedente());
			    		RicercaAccertamentoK k = new RicercaAccertamentoK();
			    		// Jira - 630
			    		k.setAnnoEsercizio(model.getStep1Model().getAnnoImpOrigine());
			    		k.setAnnoAccertamento(model.getStep1Model().getAnnoImpOrigine());
			    		k.setNumeroAccertamento(new BigDecimal(model.getStep1Model().getNumImpOrigine()));
			    		rip.setpRicercaAccertamentoK(k);
			    		
			    		RicercaAccertamentoPerChiaveOttimizzatoResponse respRk = movimentoGestionService.ricercaAccertamentoPerChiaveOttimizzato(rip);
			    		if(respRk==null || respRk.getAccertamento()==null){
			    			//Jira 381
			    			addPersistentActionWarning(ErroreFin.MOVIMENTO_NON_TROVATO.getErrore("Accertamento origine").getCodice()+" : "+ErroreFin.MOVIMENTO_NON_TROVATO.getErrore("Accertamento origine").getDescrizione());
			    		}else if(respRk.getAccertamento().getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase("A") &&  respRk.getAccertamento().getStatoOperativoMovimentoGestioneEntrata()!= null ){
		    				model.getStep1Model().setAnnoImpRiacc(null);
			    			model.getStep1Model().setNumImpRiacc(null);
			    			model.getStep1Model().setRiaccertato(WebAppConstants.No);
		    				
			    			// da CU il messaggio deve essere bloccante
			    			listaErrori.add(ErroreFin.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA_FIN.getErrore("Accertamento di origine"," annullato"));
		    			}
			    	}
	    	}
	    } else {
	    	
	    	if (model.getStep1Model().getAnnoImpOrigine() != null && model.getStep1Model().getNumImpOrigine() == null) {
	    		if(oggettoDaPopolareImpegno()){
	    			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Impegno Origine "));
	    		}else{
	    			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Accertamento Origine "));
	    		}
	    	}  
	    	if (model.getStep1Model().getAnnoImpOrigine() == null && model.getStep1Model().getNumImpOrigine() != null) {
	    		if(oggettoDaPopolareImpegno()){
	    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Impegno Origine "));
	    		}else{
	    			listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Accertamento Origine "));
	    		}
	    	}
	    }
	    
	    EsistenzaProgettoResponse esistenzaResp = new EsistenzaProgettoResponse();
	    
	    // verifico l'esistenza del PROGETTO
	    if(StringUtils.isNotEmpty(model.getStep1Model().getProgetto())){
	    	// verifica PROGETTO
			// metodo che verifica il progetto
			esistenzaResp = esistenzaProgetto();
			if(esistenzaResp.getErrori()!=null && !esistenzaResp.getErrori().isEmpty()){
				listaErrori.addAll(esistenzaResp.getErrori());
			}
	    }
	    
	    if(oggettoDaPopolareImpegno()){
		    // controllo di congruenza di CIG e CUP
		    controlliCigCupESiopePlus(model.getStep1Model(),listaErrori);
	    }
	    if(StringUtils.isNotEmpty(model.getStep1Model().getScadenza())){
			if(model.getStep1Model().getScadenza().length()!=10){
				log.debug(methodName, "errore sulla lunghezza");
				listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Data Scadenza","dd/mm/yyyy"));
			}else {
				Date dataInserita = DateUtility.parse(model.getStep1Model().getScadenza());
				log.debug(methodName, "data inserita "+dataInserita);
				if(null==dataInserita){
					listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Data Scadenza","dd/mm/yyyy"));
				} else if (!erroreAnnoImpegno) {
					Calendar c = new GregorianCalendar();
					c.setTime(dataInserita);
					if (c.get(Calendar.YEAR) != model.getStep1Model().getAnnoImpegno().intValue()) {
						if(oggettoDaPopolareImpegno()){
							listaErrori.add(ErroreFin.DATA_SCADENZA_IMPEGNO_ERRATA.getErrore(""));
						}else{
							listaErrori.add(ErroreFin.DATA_SCADENZA_ACCERTAMENTO_ERRATA.getErrore(""));
						}
					}
				}
			}
		}
	    model.getStep1Model().getRiaccertato();
	    // controllo pluriennale
	    if(model.getStep1Model().getPluriennale().equalsIgnoreCase(WebAppConstants.Si)){
	    	
	    	if(model.getStep1Model().getNumeroPluriennali()==null || model.getStep1Model().getNumeroPluriennali()==0){
	    			
	    		listaErrori.add(ErroreFin.FORMATO_NON_VALIDO.getErrore("Anni pluriennali","numero intero"));
	    			
	    	}else if(sessionHandler.getFaseBilancio().equalsIgnoreCase("O")){
	    		StringTokenizer st = new StringTokenizer(sessionHandler.getDescrizioneAnnoBilancio(),"-");
	    		st.nextElement();
	    		String stato= st.nextElement().toString();
	    		listaErrori.add(ErroreFin.OPERAZIONE_INCOMPATIBILE_PER_BILANCIO.getErrore("Inserimento Anni Pluriennali",stato));
	    		
	    	}
	    	

	    }else if(model.getStep1Model().getPluriennale().equalsIgnoreCase(WebAppConstants.No)){
	    	
	    	    // Jira - 615
	    	    if(null!=model.getStep3Model() && null!=model.getStep3Model().getListaImpegniPluriennali())
	    	    	model.getStep3Model().setListaImpegniPluriennali(new ArrayList<ImpegniPluriennaliModel>());
	    	    
	    }
	    
	    
	    boolean erroreImportoCapitolo = controlloImporti(model.getStep1Model().getAnnoImpegno());
	    
	    if (erroreImportoCapitolo) {
	    	
	    	if(oggettoDaPopolareImpegno()){
	    		
	    		if(abilitatoAzioneGestisciImpegnoSDF()){
					addPersistentActionWarning(ErroreFin.WARNING_IMPEGNO_SDF.getCodice() + " - " + ErroreFin.WARNING_IMPEGNO_SDF.getMessaggio());
				} else {
					listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE_IMPEGNO.getErrore("impegno", "INSERIMENTO"));
					addErrori(listaErrori);
					return INPUT;
				}
	    		
	    	}else{
	    		addPersistentActionWarning(ErroreFin.SUPERAMENTO_DISPONIBILITA.getCodice()+": "+ErroreFin.SUPERAMENTO_DISPONIBILITA.getErrore("").getDescrizione());
	    	}
	    }
	    

	    // controllo il provvedimento
//	    if(model.getStep1Model().getProvvedimento()!=null && 
//	    	model.getStep1Model().getProvvedimento().getAnnoProvvedimento()!=null){
//	 
//	    	if(model.getStep1Model().getProvvedimento().getNumeroProvvedimento()==null)
//	    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero provvedimento "));
//	    }
	    
	    //SIAC-6929
	    if(model.getStep1Model().getProvvedimento()!=null && 
		    	model.getStep1Model().getProvvedimento().getAnnoProvvedimento()!=null){
		    	if(model.getStep1Model().getProvvedimento().getNumeroProvvedimento()==null){
		    		listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero provvedimento "));
		    	}else{
		    		//CHECK BLOCCO RAGIONERIA
		    		ProvvedimentoImpegnoModel pim = new ProvvedimentoImpegnoModel();
		    		if(model.getStep1Model().getProvvedimento()!=null && model.getStep1Model().getProvvedimento().getUid()!=null
		    				&& model.getStep1Model().getProvvedimento().getUid().intValue()>0){
		    			pim = getProvvedimentoById(model.getStep1Model().getProvvedimento().getUid());
		    			if(pim.getBloccoRagioneria()!= null && pim.getBloccoRagioneria().booleanValue()){
		    				listaErrori.add(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore("Numero Provvedimento " + 
		    						pim.getNumeroProvvedimento() + " Oggetto " + pim.getOggetto()));
		    			}
		    		}
		    	}
		    }
	    
	    
	    
	    //SIAC-7321
	    if(model.getStep1Model().getProvvedimento()!=null){
	    	if(model.getStep1Model().getListaVincoliImpegno()!= null && !model.getStep1Model().getListaVincoliImpegno().isEmpty()){
	    		List<VincoloImpegno> listaVincoli = model.getStep1Model().getListaVincoliImpegno();	
	    		for(VincoloImpegno v : listaVincoli){
	    				if(v.getAccertamento()!= null){
	    					boolean vincoloNonCorretto = verificaAccertamentoVincolo(v.getAccertamento());
	    					if(vincoloNonCorretto){
	    						listaErrori.add(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Presenza di Vincolo Accertamento non consentito"));
	    						break;
	    					}
	    				}
	    			}
	    	}
	    }
	    
	    
	   
	    
	    if(listaErrori.isEmpty()) {
	    	
	    	if(oggettoDaPopolareImpegno()){
	    		// Controllo sul flag RPV
	    		if(model.getStep1Model().getListaVincoliImpegno()==null || model.getStep1Model().getListaVincoliImpegno().isEmpty()){
	    			// se lista vincoli vuota ma obbligatorio == true
	    			if(esistenzaResp.isFlagEsistenzaFPV()){
	    				String par = sessionHandler.getParametriAzioneSelezionata();    				
	    				Map<TipologiaGestioneLivelli, String> gestioneLivelli = model.getEnte().getGestioneLivelli();
	    				String vincoloDec = gestioneLivelli.get(TipologiaGestioneLivelli.BLOCCO_VINCOLO_DEC);	    				
	    				//SIAC-6693
	    				if(isSoggettoDecentrato() && isAzioneDecentrato() && vincoloDec!=null){
	    					//addPersistentActionError(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());	    					
	    					//listaErrori.add(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore(""));	   
	    					addErrore(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore(""));
	    				}else{	    					    				
	    					addPersistentActionWarning(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());
	    				}
	    				
		    		}
	    		}
	    	}	    	
	    	// lancio tutti i controlli sui servizi
	        return controlliServiziPerProsegui(oggettoDaPopolare,listaErrori);	       
	    } else {	    	
	       // il pop up deve uscire fuori solo in presenza dell'errore 0032 	
	       if(listaErrori.size()==1 || listaErrori.size()==2){	
		       Iterator<Errore> it = listaErrori.iterator();
		       while(it.hasNext()){
		    	   Errore cicla = it.next();
		    	   if(cicla.getCodice().equalsIgnoreCase("COR_CON_0032")){
		    		   if(cicla.getDescrizione().equalsIgnoreCase(ErroreCore.AGGIORNAMENTO_CON_CONFERMA.getErrore("Impegno riaccertato"," inesistente").getDescrizione())){
		    			   model.getStep1Model().setCheckproseguiRiacc(true);
		    		   }
		    		   if(cicla.getDescrizione().equalsIgnoreCase(ErroreCore.AGGIORNAMENTO_CON_CONFERMA.getErrore(model.getLabels().get(LABEL_OGGETTO_GENERICO)+" origine","inesistente").getDescrizione())){
		    			   model.getStep1Model().setCheckproseguiOrigin(true);
		    		   }
		    	   }
		    	   
		       }
	       }	
	       
	       //Se nella lista errori ci sono piu' errori, cancello gli errori di aggiornamento con conferma
	       //onde evitare che appaia nella schermata l'errore, senza la popup
	       if(!listaErrori.isEmpty() && listaErrori.size()>=3){	
	    	   List<Errore> list= new ArrayList<Errore>();
	    	   for(Errore e: listaErrori){
	    		   if(e.getCodice().equalsIgnoreCase("COR_CON_0032")){
	    			   list.add(e);
	    		   }
	    	   }
	    	   if(!list.isEmpty()){
		    	   for(Errore eTemp: list){
		    		   listaErrori.remove(eTemp);
		    	   }
	    	   }
	       }
	       
	       
	 	   addErrori(listaErrori);
		   return INPUT;
	    }
	
	}
	
	/**
	 * Con la  SIAC-4943  CR 552 viene introdotta una modale di conferma 
	 * al salvataggio dei pluriennali
	 * @return
	 */
	public boolean salvaConConfermaPrimaNota() {
		
		boolean salvaConConfermaPrimaNota = false;
		
		if(oggettoDaPopolareImpegno()){
			
	    	CapitoloImpegnoModel capitolo = model.getStep1Model().getCapitolo();
	    	
	    	String codiceTitoloSpesa = capitolo.getTitoloSpesa();
	    	String codiceMacroaggregato = capitolo.getCodiceMacroAggregato();
	    	
	    	salvaConConfermaPrimaNota = codiceTitoloSpesa.equalsIgnoreCase(CLASS_CAPITOLO_TITOLO_3) ||
				codiceTitoloSpesa.equalsIgnoreCase(CLASS_CAPITOLO_TITOLO_4) && codiceMacroaggregato.equalsIgnoreCase(CLASS_CAPITOLO_MACROAGGREGATO_1) ||
				codiceTitoloSpesa.equalsIgnoreCase(CLASS_CAPITOLO_TITOLO_5);
	    		
	    	    		
	    				
		} else {
			
	    	CapitoloImpegnoModel capitolo = model.getStep1Model().getCapitolo();
	    	
	    	String codiceTitoloEntrata = capitolo.getTitoloEntrata();
	    	String codiceTipologia = capitolo.getTipologia();
	    	
	    	salvaConConfermaPrimaNota = (codiceTitoloEntrata.equalsIgnoreCase(CLASS_CAPITOLO_TITOLO_4) && 
					codiceTipologia.equalsIgnoreCase(CLASS_CAPITOLO_TIPOLOGIA_4)) ||
					codiceTitoloEntrata.equalsIgnoreCase(CLASS_CAPITOLO_TITOLO_5) ||
					codiceTitoloEntrata.equalsIgnoreCase(CLASS_CAPITOLO_TITOLO_6) ||
					codiceTitoloEntrata.equalsIgnoreCase(CLASS_CAPITOLO_TITOLO_7) ;

		}
		
		return salvaConConfermaPrimaNota;
	}
	
	private boolean controlloImporti(Integer annoImpegno) {
		//verifica degli importi
		boolean erroreImporti = false;
	    Integer annoEsercio = Integer.valueOf(sessionHandler.getAnnoEsercizio());
	    if (annoImpegno != null && annoImpegno.compareTo(annoEsercio + 2) <= 0) {
	    	
	    
	    	if(oggettoDaPopolareImpegno()){
	    		
	    		
		    	if(model.getStep1Model().getCapitolo().getImportiCapitoloUG().size()!=0){
		    		
			    	// ricavo la mappa con gli anni/valori
			    	Map<Integer, BigDecimal> mappaAnniValori = ritornaMappaAnniDisponibilita(oggettoDaPopolareImpegno());
			    	
			    	// verifico le disponibilita
			    	BigDecimal bd = mappaAnniValori.get(annoImpegno);
			    	
			    	if(bd!=null){
			    		if (null!=model.getStep1Model().getImportoImpegno() && model.getStep1Model().getImportoImpegno().compareTo(bd) > 0) {
			    			 erroreImporti = true;
			    		}
			    	}
			    	
		    		
		    	}
	    	}else{
	    		
	    		
	    		
		    	if(model.getStep1Model().getCapitolo().getImportiCapitoloEG().size()!=0){
		    		
			    	// ricavo la mappa con gli anni/valori
			    	Map<Integer, BigDecimal> mappaAnniValori = ritornaMappaAnniDisponibilita(oggettoDaPopolareImpegno());
			    	
			    	// verifico le disponibilita
			    	BigDecimal bd = mappaAnniValori.get(annoImpegno);
			    	if(bd!=null){
			    		if (null!=model.getStep1Model().getImportoImpegno() && model.getStep1Model().getImportoImpegno().compareTo(bd) > 0) {
			    			 erroreImporti = true;
			    		}
			    	}
		    	}
	    		
	    	}
	    	
	    }
	    return erroreImporti;
	}   
	
	
	private Map<Integer, BigDecimal> ritornaMappaAnniDisponibilita(boolean oggettoImpegno){
		
		Map<Integer, BigDecimal> mappa = new HashMap<Integer, BigDecimal>();
		
    	if(oggettoImpegno){
    	
			for(int i =0;i<3;i++){
	    		if(i==0){
	    			mappa.put(model.getStep1Model().getCapitolo().getImportiCapitoloUG().get(0).getAnnoCompetenza()+i, model.getStep1Model().getCapitolo().getImportiCapitoloUG().get(0).getDisponibilitaImpegnareAnno1());
	    		}
	    		if(i==1){
	    			mappa.put(model.getStep1Model().getCapitolo().getImportiCapitoloUG().get(0).getAnnoCompetenza()+i, model.getStep1Model().getCapitolo().getImportiCapitoloUG().get(0).getDisponibilitaImpegnareAnno2());
	
	    		}
	    		
	    		if(i==2){
	    			mappa.put(model.getStep1Model().getCapitolo().getImportiCapitoloUG().get(0).getAnnoCompetenza()+i, model.getStep1Model().getCapitolo().getImportiCapitoloUG().get(0).getDisponibilitaImpegnareAnno3());
	    		}
			}
    	}else{
    		for(int i =0;i<3;i++){
	    		if(i==0){
	    			mappa.put(model.getStep1Model().getCapitolo().getImportiCapitoloEG().get(0).getAnnoCompetenza()+i, model.getStep1Model().getCapitolo().getImportiCapitoloEG().get(0).getDisponibilitaAccertareAnno1());
	    		}
	    		if(i==1){
	    			mappa.put(model.getStep1Model().getCapitolo().getImportiCapitoloEG().get(0).getAnnoCompetenza()+i, model.getStep1Model().getCapitolo().getImportiCapitoloEG().get(0).getDisponibilitaAccertareAnno2());
	
	    		}
	    		
	    		if(i==2){
	    			mappa.put(model.getStep1Model().getCapitolo().getImportiCapitoloEG().get(0).getAnnoCompetenza()+i, model.getStep1Model().getCapitolo().getImportiCapitoloEG().get(0).getDisponibilitaAccertareAnno3());
	    		}
			}
    	}
		
		return mappa;
		
	}
	
	private boolean controlloImportiPlur(Integer annoImpegno, boolean oggettoImpegno){
		boolean erroreImporti = false;
		Integer annoEsercio = Integer.valueOf(sessionHandler.getAnnoEsercizio());
		ImpegniPluriennaliModel currentImpPlur= new ImpegniPluriennaliModel();
		for(ImpegniPluriennaliModel ipm: model.getStep3Model().getListaImpegniPluriennali()){
			if(ipm.getAnnoImpPluriennale() == annoImpegno ){
				currentImpPlur=ipm;
			}
		}
		
	    if (annoImpegno != null && annoImpegno.compareTo(annoEsercio + 2) <= 0) {
	    	
	    	// ricavo la mappa con gli anni/valori
	    	Map<Integer, BigDecimal> mappaAnniValori = ritornaMappaAnniDisponibilita(oggettoImpegno);
	    	
	    	// verifico le disponibilita
	    	BigDecimal bd = mappaAnniValori.get(annoImpegno);
    		if (currentImpPlur.getImportoImpPluriennale().compareTo(bd) > 0) {
    			 erroreImporti = true;
    		}
    		 
	    }
	    return erroreImporti;
	}
	
	protected String controlliServiziPerProsegui(OggettoDaPopolareEnum oggettoDaPopolare) {
		return controlliServiziPerProsegui(oggettoDaPopolare,null);
	}

	protected String controlliServiziPerProsegui(OggettoDaPopolareEnum oggettoDaPopolare,List<Errore> listaErrori1) {
		boolean erroriTrovatiNeiServizi = false;
		List<Errore> listaErrori = null;
		if(listaErrori1 ==null || listaErrori1.size()==0){
			listaErrori = new ArrayList<Errore>();
		}else{
			listaErrori=listaErrori1;
		}
		
		// Carico e controllo il capitolo
		if (!model.getStep1Model().isCapitoloSelezionato()) {
			if (!eseguiRicercaCapitolo(model.getStep1Model().getCapitolo(), false, oggettoDaPopolare)) {
				erroriTrovatiNeiServizi = true;
				model.getStep1Model().setCapitoloSelezionato(false);
			} else {
			   if (model.getStep1Model().getCapitolo() != null) {
				   boolean erroreImportoCapitolo = controlloImporti(model.getStep1Model().getAnnoImpegno());
					  if (erroreImportoCapitolo) {
						  
						if(oggettoDaPopolareImpegno()){
							
							if(abilitatoAzioneGestisciImpegnoSDF()){
								addPersistentActionWarning(ErroreFin.WARNING_IMPEGNO_SDF.getCodice() + " - " + ErroreFin.WARNING_IMPEGNO_SDF.getMessaggio());
							} else {
								listaErrori.add(ErroreFin.DISPONIBILITA_INSUFFICIENTE_IMPEGNO.getErrore("impegno", "INSERIMENTO"));
								addErrori(listaErrori);
								return INPUT;
							}
						  
						}else{
					      addPersistentActionWarning(ErroreFin.SUPERAMENTO_DISPONIBILITA.getCodice()+": "+ErroreFin.SUPERAMENTO_DISPONIBILITA.getErrore("").getDescrizione());

						}
				  }
				  if(teSupport.getPianoDeiConti()==null || teSupport.getPianoDeiConti().getCodice()==null || teSupport.getPianoDeiConti().getCodice().equals("")){
					  teSupport.setPianoDeiConti(new ElementoPianoDeiConti());
					  teSupport.getPianoDeiConti().setCodice(model.getStep1Model().getCapitolo().getCodicePdcFinanziario());
					  teSupport.getPianoDeiConti().setDescrizione(model.getStep1Model().getCapitolo().getDescrizionePdcFinanziario());
					  teSupport.getPianoDeiConti().setUid(model.getStep1Model().getCapitolo().getIdPianoDeiConti());
					  teSupport.getPianoDeiConti().setTipoClassificatore(model.getStep1Model().getCapitolo().getTipoClassificatorePdc());
				  }
				 
				  if(null!=model.getCapitoloModelTrovatoDaServizio()){
		
					 if(oggettoDaPopolare.equals(OggettoDaPopolareEnum.IMPEGNO) || oggettoDaPopolare.equals(OggettoDaPopolareEnum.LIQUIDAZIONE) || oggettoDaPopolare.equals(OggettoDaPopolareEnum.ORDINATIVO_PAGAMENTO)){
						  
						 // cofog 
						 if(null!=model.getCapitoloModelTrovatoDaServizio().getCodiceClassificazioneCofog()){
							 teSupport.setCofogSelezionato(model.getCapitoloModelTrovatoDaServizio().getCodiceClassificazioneCofog());
						 }
						 
						 // transazione europea spesa
						 if(null!=model.getCapitoloModelTrovatoDaServizio().getCodiceTransazioneEuropeaSpesa()){
							 teSupport.setTransazioneEuropeaSelezionato(model.getCapitoloModelTrovatoDaServizio().getCodiceTransazioneEuropeaSpesa());
						 } 
						  
						 // ricorrente spesa
						 if(null!=model.getCapitoloModelTrovatoDaServizio().getCodiceRicorrenteSpesa()){
							 teSupport.setRicorrenteSpesaSelezionato(model.getCapitoloModelTrovatoDaServizio().getCodiceRicorrenteSpesa());
						 } 
						  
						 // perimetro sanitario spesa
						  if(null!=model.getCapitoloModelTrovatoDaServizio().getCodicePerimetroSanitarioSpesa())
							  teSupport.setPerimetroSanitarioSpesaSelezionato(model.getCapitoloModelTrovatoDaServizio().getCodicePerimetroSanitarioSpesa());
						  
						  
						 // politiche regionali sanitarie 
						 if(null!=model.getCapitoloModelTrovatoDaServizio().getCodicePoliticheRegionaliUnitarie()) 
							 teSupport.setPoliticaRegionaleSelezionato(model.getCapitoloModelTrovatoDaServizio().getCodicePoliticheRegionaliUnitarie());
					  
					  }else{
						  // in caso di Accertamento
						  
						  // transazione europea entrata
						  if(null!=model.getCapitoloModelTrovatoDaServizio().getCodiceTransazioneEuropeaEntrata()){
							  teSupport.setTransazioneEuropeaSelezionato(model.getCapitoloModelTrovatoDaServizio().getCodiceTransazioneEuropeaEntrata());
						  }
						  
						  // ricorrente entrata
						  if(null!=model.getCapitoloModelTrovatoDaServizio().getCodiceRicorrenteEntrata()){
							  teSupport.setRicorrenteEntrataSelezionato(model.getCapitoloModelTrovatoDaServizio().getCodiceRicorrenteEntrata());
						  }
						  
						  // perimetro sanitario entrata
						  if(null!=model.getCapitoloModelTrovatoDaServizio().getCodicePerimetroSanitarioEntrata()){
							  teSupport.setPerimetroSanitarioEntrataSelezionato(model.getCapitoloModelTrovatoDaServizio().getCodicePerimetroSanitarioEntrata());
						  }
						  
						  
					  }
				  }
	   		   }
			}
		} else {
			 
			if (model.getStep1Model().getCapitolo() != null) {
				 
				if(teSupport.getPianoDeiConti()==null || teSupport.getPianoDeiConti().getCodice()==null || teSupport.getPianoDeiConti().getCodice().equals("")){
					 teSupport.setPianoDeiConti(new ElementoPianoDeiConti());
					 teSupport.getPianoDeiConti().setCodice(model.getStep1Model().getCapitolo().getCodicePdcFinanziario());
					 teSupport.getPianoDeiConti().setDescrizione(model.getStep1Model().getCapitolo().getDescrizionePdcFinanziario());
		   			 teSupport.getPianoDeiConti().setUid(model.getStep1Model().getCapitolo().getIdPianoDeiConti());
		   			 teSupport.setIdMacroAggregato(model.getStep1Model().getCapitolo().getIdMacroAggregato());
		   		 	 teSupport.getPianoDeiConti().setTipoClassificatore(model.getStep1Model().getCapitolo().getTipoClassificatorePdc());
		   		 	 
		   		 	 if(oggettoDaPopolareImpegno()){ // impegno - uscita
		   		 		 
		   		 		 // transazione spesa
		   		 		 teSupport.setTransazioneEuropeaSelezionato(model.getStep1Model().getCapitolo().getCodiceTransazioneEuropeaSpesa());
		   		 		 // cofog
		   		 		 teSupport.setCofogSelezionato(model.getStep1Model().getCapitolo().getCodiceClassificazioneCofog());
		   		 		 // ricorrente spesa
		   		 		 teSupport.setRicorrenteSpesaSelezionato(model.getStep1Model().getCapitolo().getCodiceRicorrenteSpesa());
		   		 		 // perimetro sanitario spesa
		   		 		 teSupport.setPerimetroSanitarioSpesaSelezionato(model.getStep1Model().getCapitolo().getCodicePerimetroSanitarioSpesa());
		   		 		 // politiche regionali sanitarie
		   		 		 teSupport.setPoliticaRegionaleSelezionato(model.getStep1Model().getCapitolo().getCodicePoliticheRegionaliUnitarie());
		   		 		 
		   		 		 
		   		 	 }else{ // accertamento
		   		 		 		   		 		 
		   		 	     // transazione entrata
		   		 		 teSupport.setTransazioneEuropeaSelezionato(model.getStep1Model().getCapitolo().getCodiceTransazioneEuropeaEntrata());
		   		 		 // ricorrente entrata
		   		 		 teSupport.setRicorrenteEntrataSelezionato(model.getStep1Model().getCapitolo().getCodiceRicorrenteEntrata());
		   		 		 // sanitario entrata
		   		 		 teSupport.setPerimetroSanitarioEntrataSelezionato(model.getStep1Model().getCapitolo().getCodicePerimetroSanitarioEntrata());
		   		 	 }
		   		 	 
				 }
	   		   }
		}
		
		// Carico e controllo il soggetto
		if (!model.getStep1Model().isSoggettoSelezionato() && model.getStep1Model().getSoggetto().getCodCreditore() != null && !"".equals(model.getStep1Model().getSoggetto().getCodCreditore())) {
			if (!eseguiRicercaSoggetto(convertiModelPerChiamataServizioRicerca(model.getStep1Model().getSoggetto()), false, oggettoDaPopolare)) {
				erroriTrovatiNeiServizi = true;
			}
		}
		
		// Carico e controllo il provvedimento
		if(model.getStep1Model().getProvvedimento().getAnnoProvvedimento()==null &&
				(model.getStep1Model().getProvvedimento().getNumeroProvvedimento()==null || model.getStep1Model().getProvvedimento().getNumeroProvvedimento().compareTo(BigInteger.ZERO)==0) &&
				model.getStep1Model().getProvvedimento().getIdTipoProvvedimento()==null){
			
			model.getStep1Model().setProvvedimento(new ProvvedimentoImpegnoModel());
			model.getStep1Model().setProvvedimentoSelezionato(false);
			
		}
		boolean erroreProvvedimento = controlloServizioProvvedimento(model.getStep1Model().getProvvedimento(), model.getStep1Model().isProvvedimentoSelezionato());
		erroriTrovatiNeiServizi = erroriTrovatiNeiServizi || erroreProvvedimento;
		if (model.getStep1Model().getProvvedimento() != null && model.getStep1Model().getProvvedimento().getUid() != null) {
			if (model.getStep1Model().getProvvedimento().getStato().equals(MOVGEST_PROVVISORIO)) {
				model.getStep1Model().setStato(MOVGEST_PROVVISORIO);
			} else if (model.getStep1Model().getProvvedimento().getStato().equals(STATO_MOVGEST_DEFINITIVO)) {
			     if (null != model.getStep1Model().getSoggetto().getCodCreditore() && StringUtils.isNotEmpty(model.getStep1Model().getSoggettoImpegno().getCodiceSoggetto())) {
			    	 model.getStep1Model().setStato(STATO_MOVGEST_DEFINITIVO);
			     } else {
			    	 model.getStep1Model().setStato(MOVGEST_DEFINITIVO_NON_LIQUIDABILE);
			     }
			}
		}
		
		
		// Controllo provvedimento con STRUTTURA AMMINISTRATIVA
		// Se nel provvedimento c'e' la sac allora devo verificare che sia una di quelle ammesse
		// Jira 1648 : Se modifico il provevdimento scegliendone uno SENZA sac, bisogna verificare il null 
		// lo faccio in controlloSACStrutturaAmmUtenteDecentrato --> quindi verifico che se il decentrato ha strutture
		// e uidStrutturaAmm è null, bisogna lanciare il warning) 
		
		
		
		if(isSoggettoDecentrato()){
			
			String azioneGestisciDecentratoP = null;
			String azioneGestisciDecentrato = null;
			
			if(oggettoDaPopolareImpegno()){
				azioneGestisciDecentratoP = CodiciOperazioni.OP_SPE_gestisciImpegnoDecentratoP;
				azioneGestisciDecentrato = CodiciOperazioni.OP_SPE_gestisciImpegnoDecentrato;
			} else {
				azioneGestisciDecentratoP = CodiciOperazioni.OP_ENT_gestisciAccertamentoDecentratoP;
				azioneGestisciDecentrato = CodiciOperazioni.OP_ENT_gestisciAccertamentoDecentrato;
			}
			
			if(isAzioneAbilitata(azioneGestisciDecentratoP) && compilatoProvvedimento()){
				//il provveddimento, se indicato, deve essere provvisorio
				
				//FIX per SIAC-4067, il provvedimento non compilato non deve generare errore.
				boolean provvedimentoProvvisorio = true;
				if (!MOVGEST_PROVVISORIO.equals(model.getStep1Model().getProvvedimento().getStato())){
					provvedimentoProvvisorio = false;
				}
				if(!provvedimentoProvvisorio){
					if(oggettoDaPopolareImpegno()){
						addErrore(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Inserimento Impegno Decentrato", "Provvisorio"));
					} else {
						addErrore(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Inserimento Accertamento Decentrato", "Provvisorio"));
					}
					erroreProvvedimento = Boolean.TRUE;
				}
			} 
			
			if(compilatoProvvedimento() && provvedimentoHasSac()){
				if(isAzioneAbilitata(azioneGestisciDecentrato)){
					//il provvedimento deve avere la mia struttura, ma solo come warning
					if(!controlloSACStrutturaAmmUtenteDecentrato(sessionHandler.getAzione().getNome(), model.getStep1Model().getProvvedimento().getUidStrutturaAmm())){
						if(!model.isProseguiConWarningSacDelProvvPerDecentrato()){
							addPersistentActionWarning(ErroreFin.PROVVEDIMENTO_DECENTRATO_NON_VALIDO.getErrore().getCodice()+" - "+ErroreFin.PROVVEDIMENTO_DECENTRATO_NON_VALIDO.getErrore().getDescrizione());
							erroreProvvedimento = Boolean.TRUE;
							model.setProseguiConWarningSacDelProvvPerDecentrato(true);
						}
					}
				}
			}
			
		} else {
			//Controllo che c'era gia':
			if(!controlloSACStrutturaAmmUtenteDecentrato(sessionHandler.getAzione().getNome(), model.getStep1Model().getProvvedimento().getUidStrutturaAmm())){
				addPersistentActionWarning(ErroreFin.PROVVEDIMENTO_DECENTRATO_NON_VALIDO.getErrore().getCodice()+" - "+ErroreFin.PROVVEDIMENTO_DECENTRATO_NON_VALIDO.getErrore().getDescrizione());
				erroreProvvedimento = Boolean.TRUE;
			}
		}
		
		
		
		
		// Aggiungo controllo su sac del capitolo e sac del provvedimento, devono essere uguali 
		// il controllo scatta anche quando la sac del provvedimento e' nulla
		// Il controllo non e' bloccante, quindi al click successivo devo poter proseguire, 
		if(model.getStep1Model().getCapitolo().getUidStruttura()!=null && model.getStep1Model().getProvvedimento().getNumeroProvvedimento()!=null){	
			
			Integer uidSacCapitolo = model.getStep1Model().getCapitolo().getUidStruttura(); // capitolo model valorizzato poco piu sopra in eseguiRicercaCapitolo
			Integer uidSacProvvedimento = model.getStep1Model().getProvvedimento().getUidStrutturaAmm()!=null ? model.getStep1Model().getProvvedimento().getUidStrutturaAmm() : 0;
			if(!uidSacCapitolo.equals(uidSacProvvedimento)){
				
				
				if(!model.isProseguiConWarning()){
					addPersistentActionWarning(ErroreFin.SAC_PROVVEDIMENTO_E_CAPITOLO_INCONGRUENTE.getErrore().getCodice()+" - "+ErroreFin.SAC_PROVVEDIMENTO_E_CAPITOLO_INCONGRUENTE.getErrore().getDescrizione());
					erroreProvvedimento = Boolean.TRUE;
					model.setProseguiConWarning(true);
				}
				
			}
		}
		
		//in presenza della classe, pulisco totalmente il soggetto
	    controlloSoggettoSelezionatoEClasse();
		
	    //Inserisco la descrizione all'interno della classe del soggetto  vedi Jira e issue di laura
		inserisciDescClasseSoggetto();
		
		boolean erroreVincoliCapitoli = false;
		
		// controlli su VINCOLI
		if(oggettoDaPopolareImpegno()){
			// presenza di vincoli
			
			ListaPaginata<VincoloCapitoli> listaVincoliSpesa = getVincoliCapitoloSpesa(model.getCapitoloModelTrovatoDaServizio());
			List<VincoloCapitoli> vincoliConFlagTrasferimentiVincolati = getConFlagTrasferimenti(listaVincoliSpesa);
			//CONTROLLO SU VINCOLI CAPITOLI:
			erroreVincoliCapitoli = cercaCapitoliAmmessiPerDecentrato(vincoliConFlagTrasferimentiVincolati);
			
			if(model.getStep1Model().getListaVincoliImpegno()!=null && !model.getStep1Model().getListaVincoliImpegno().isEmpty()){
				if((model.getStep1Model().getTotaleImportoDaCollegare().compareTo(BigDecimal.ZERO))>0){
					
    				Map<TipologiaGestioneLivelli, String> gestioneLivelli = model.getEnte().getGestioneLivelli();
    				String vincoloDec = gestioneLivelli.get(TipologiaGestioneLivelli.BLOCCO_VINCOLO_DEC);	
    				//SIAC-6693
    				if(isSoggettoDecentrato() && isAzioneDecentrato() && vincoloDec!=null){
    					//addPersistentActionError(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());	    					
    					//listaErrori.add(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore(""));	 
    					addErrore(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore(""));
    					erroreVincoliCapitoli = true;
    				}else{
    					addPersistentActionWarning(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());
    				}				
				}else if(model.getStep1Model().getTotaleImportoDaCollegare().compareTo(BigDecimal.ZERO)<0){
					erroriTrovatiNeiServizi = true;
					addErrore(ErroreFin.TOT_COLLEGA_VINCOLO.getErrore(""));
				}
			}else{
				// se lista vincoli vuota ma obbligatorio == true
				if(!isEmpty(vincoliConFlagTrasferimentiVincolati)){
					//addPersistentActionWarning(IMP_NON_TUTTO_VINCOLATO);
    				Map<TipologiaGestioneLivelli, String> gestioneLivelli = model.getEnte().getGestioneLivelli();
    				String vincoloDec = gestioneLivelli.get(TipologiaGestioneLivelli.BLOCCO_VINCOLO_DEC);	
    				//SIAC-6693
    				if(isSoggettoDecentrato() && isAzioneDecentrato() && vincoloDec!=null){
    					//addPersistentActionError(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());	    					
    					//listaErrori.add(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore(""));
    					erroreVincoliCapitoli = true;
    					addErrore(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore(""));
    				}else{
    					addPersistentActionWarning(ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getCodice() +" "+ErroreFin.IMPEGNO_NON_TOT_VINCOLATO.getErrore("").getDescrizione());
    				}
    				
				}
			}
		}
		
		if(erroriTrovatiNeiServizi || erroreProvvedimento || erroreVincoliCapitoli){
			return INPUT;
		}else{
			
			//prima di proseguire vediamo se va cambiato il valore del flag sanita':
			pilotaFlagSanita();
			//
			
			//e vediamo se va cambiato il siope in accordo col capitolo:
			pilotaSiope();
			//
			
			impostaObbligatoriConUnSoloElementoInLista(oggettoDaPopolare);
			
			return "prosegui";
		}
			
		
 		
	}

	
		protected void executeStep3() {
		setMethodName("execute");
		if(model.getStep1Model().getNumeroPluriennali() != null){
			model.getStep3Model().setListaImpegniPluriennali(new ArrayList<ImpegniPluriennaliModel>());				
			int annoImpegno = model.getStep1Model().getAnnoImpegno();
			for(int i=0;i<model.getStep1Model().getNumeroPluriennali();i++){			
				ImpegniPluriennaliModel impegniPluriennali =new ImpegniPluriennaliModel();
				impegniPluriennali.setAnnoImpPluriennale(annoImpegno+i+1);
				if(model.getStep1Model().getScadenza()!=null && 
				   !StringUtils.isEmpty(model.getStep1Model().getScadenza())){
					Calendar c = new GregorianCalendar();
					c.setTime(DateUtility.parse(model.getStep1Model().getScadenza()));
					c.set(Calendar.YEAR, impegniPluriennali.getAnnoImpPluriennale());
					impegniPluriennali.setDataScadenzaImpPluriennale(c.getTime());
					// setto anche la data nel formato stringa
					SimpleDateFormat formatDateJava = new SimpleDateFormat("dd/MM/yyyy");
					String DataString= formatDateJava.format(impegniPluriennali.getDataScadenzaImpPluriennale());
					impegniPluriennali.setDataScadenzaImpPluriennaleString(DataString);
				}
				model.getStep1Model().setImportoImpegno(convertiImportoToBigDecimal(model.getStep1Model().getImportoFormattato()));
				impegniPluriennali.setImportoImpPluriennale(model.getStep1Model().getImportoImpegno());
				impegniPluriennali.setImportoImpPluriennaleString(model.getStep1Model().getImportoFormattato());
				model.getStep3Model().getListaImpegniPluriennali().add(impegniPluriennali);
			}
		}
	}
	
	// IMPEGNO
	public String salvaPluriennaleImpegno(Integer annoScritturaEconomicoPatrimoniale) throws Exception {
		setMethodName("salvaPluriennaleImpegno");
		// cancello dal model le righe con ZERO
		cancellaRigheZero();		
		InserisceImpegniResponse response = null;		
		if (controlliFormaliPluriennale()) {
			return INPUT;
		} else {			
			// se ci sono i valori importi allora ci sono pluriennali
			if(model.getStep3Model().getListaImpegniPluriennali()!=null && model.getStep3Model().getListaImpegniPluriennali().size()>0){			
				/*
				 *  INIZIO - STOPWATCH
				 */
				StopWatch stopwatch = new StopWatch("stopWatchCategory");
				stopwatch.start();
			
				response = inserisceImpegno(model, true,annoScritturaEconomicoPatrimoniale);
				
				stopwatch.stop();
				stopWatchLogger.info(this.getClass().getName(), methodName,  stopwatch.getTotalTimeMillis());
				
				/*
				 *  FINE - STOPWATCH
				 */
				
				if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
					debug(methodName, "Errore nella risposta del servizio");
					addErrori(methodName, response);
					return INPUT;
				}
			}	
			// vai sulla pagina di aggiorna
			// prendo quello di in sessione iniziale 
			
			String concatenazioneMovGest =  (String)sessionHandler.getParametro(FinSessionParameter.MOVGEST_INIZIALE);
			StringTokenizer st = new StringTokenizer(concatenazioneMovGest, "||");
			
			
			setAnnoImpegnoStruts3(st.nextToken());
			setNumeroImpegnoStruts3(st.nextToken());
			
			clearActionData();
			ricaricaValoriDefaultStep1();
			addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
			if(response!=null){
				for(int i=0; i< response.getElencoImpegniInseriti().size();i++){
					//messaggio inserimento pluriennali come da richiesta laura
						addPersistentActionMessage("FIN_INF_0070, Movimento inserito ( movimento=Impegno, anno = " + response.getElencoImpegniInseriti().get(i).getAnnoMovimento() +  ", numero= "+ response.getElencoImpegniInseriti().get(i).getNumero() +" )");
					
				}
			}
		
		    return "gotoAggiorna";
		    
		}
	}

	
	public String forzaSalvaPluriennaleAccertamento() throws Exception {
		model.getStep3Model().setCheckproseguiPlurAcc(false);
		setAccPlurRiaccGiaRicercata(true);
		return salvaPluriennaleAccertamento(null);
	}
	
	
	private void cancellaRigheZero(){
		//ciclo per levare dal model
		// le righe con importo == ZERO
		List<ImpegniPluriennaliModel> listaElementiDaLevare = new ArrayList<ImpegniPluriennaliModel>();
		
		for(ImpegniPluriennaliModel iteroMulti : model.getStep3Model().getListaImpegniPluriennali()){
			
			if(null!=iteroMulti.getImportoImpPluriennaleString() && !iteroMulti.getImportoImpPluriennaleString().trim().equals("")){
				iteroMulti.setImportoImpPluriennale(convertiImportoToBigDecimal(iteroMulti.getImportoImpPluriennaleString()));
			}else{
				iteroMulti.setImportoImpPluriennale(BigDecimal.ZERO);
			}
			if(iteroMulti.getImportoImpPluriennale().compareTo(BigDecimal.ZERO)==0){
				listaElementiDaLevare.add(iteroMulti);
			}
		}
		
		// rimuovo le righe dal model appena trovate
		Iterator<ImpegniPluriennaliModel> itLista =  listaElementiDaLevare.iterator();
		while(itLista.hasNext()){
			model.getStep3Model().getListaImpegniPluriennali().remove(itLista.next());
		}
	}
	
	// ACCERTAMENTO
	public String salvaPluriennaleAccertamento(Integer annoScritturaEconomicoPatrimoniale) throws Exception {
		model.getStep3Model().setCheckproseguiPlurAcc(false);
		setMethodName("salvaPluriennaleAccertamento");
		// cancello dal model le righe con ZERO
		cancellaRigheZero();
		
		InserisceAccertamentiResponse response = null;
		
		if (controlliFormaliPluriennale()) {
			return INPUT;
		} else {
			

		       // il pop up deve uscire fuori solo in presenza del errore 0032 	
		       if(getActionWarnings().size()==1){	
			       Iterator<String> it = getActionWarnings().iterator();
			       while(it.hasNext()){
			    	   String cicla = it.next();
			    	   if(cicla.equalsIgnoreCase(ErroreFin.SUPERAMENTO_DISPONIBILITA.getErrore().getCodice()+" : "+ErroreFin.SUPERAMENTO_DISPONIBILITA.getErrore().getDescrizione())){
			    		   model.getStep3Model().setCheckproseguiPlurAcc(true);
			    		   return INPUT;
			    	   }
			    	   
			       }
		       }
		       
		    // se ci sono i valori importi allora ci sono pluriennali
			if(model.getStep3Model().getListaImpegniPluriennali()!=null && model.getStep3Model().getListaImpegniPluriennali().size()>0){   
				
				response =  inserisceAccertamento(model, true,annoScritturaEconomicoPatrimoniale);
				if(response.isFallimento() || (response.getErrori() != null && response.getErrori().size() > 0)) {
					debug(methodName, "Errore nella risposta del servizio");
					addErrori(methodName, response);
					return INPUT;
				}
			}

			String concatenazioneMovGest =  (String)sessionHandler.getParametro(FinSessionParameter.MOVGEST_INIZIALE);
			StringTokenizer st = new StringTokenizer(concatenazioneMovGest, "||");
			
			
			setAnnoAccertamento(st.nextToken());
			setNumeroAccertamento(st.nextToken());
			
			
			clearActionData();
			ricaricaValoriDefaultStep1();
			addPersistentActionMessage(ErroreFin.OPERAZIONE_EFFETTUATA_CORRETTAMENTE.getErrore("").getDescrizione());
			if(null!=response){
				for(int i=0; i< response.getElencoAccertamentiInseriti().size();i++){
					//messaggio inserimento pluriennali come da richiesta laura
						addPersistentActionMessage("FIN_INF_0070, Movimento inserito ( movimento=Accertamento, anno = " + response.getElencoAccertamentiInseriti().get(i).getAnnoMovimento() +  ", numero= "+ response.getElencoAccertamentiInseriti().get(i).getNumero() +" )");
					
				}
			}
			return "gotoAggiorna";
		}
	}  
	
	private boolean controlliFormaliPluriennale() {
		Map<Integer, Integer> controlloAnni = new HashMap<Integer, Integer>();
		if (model.getStep3Model().getListaImpegniPluriennali() != null && model.getStep3Model().getListaImpegniPluriennali().size() > 0) {
			for (ImpegniPluriennaliModel currentImpegnoPluriennale : model.getStep3Model().getListaImpegniPluriennali()) {
				
				if(currentImpegnoPluriennale.getDataScadenzaImpPluriennale()==null && StringUtils.isNotEmpty(currentImpegnoPluriennale.getDataScadenzaImpPluriennaleString())){
					
					//Inserire controllo se scrivo cavolate all'interno della data
					if(currentImpegnoPluriennale.getDataScadenzaImpPluriennaleString().length() != 10){
		  				addErrore(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
		  				return true;
		  			} else 	if (!DateUtility.isDate(currentImpegnoPluriennale.getDataScadenzaImpPluriennaleString(), "dd/MM/yyyy")) {
		  					addErrore(ErroreCore.FORMATO_NON_VALIDO.getErrore("Data Scadenza : dd/MM/yyyy","dd/MM/yyyy"));
		  					return true;
		  				}
		  				
					
					// ho inserito la data nel text ma non e' ancora valorizzato l'oggetto date
					currentImpegnoPluriennale.setDataScadenzaImpPluriennale(DateUtility.parse(currentImpegnoPluriennale.getDataScadenzaImpPluriennaleString()));
				}
				
				if (currentImpegnoPluriennale.getDataScadenzaImpPluriennale() != null && (currentImpegnoPluriennale.getDataScadenzaImpPluriennaleString() != null && !currentImpegnoPluriennale.getDataScadenzaImpPluriennaleString().equals(""))) {
					
					
					// prendo la data formato stringa e ne faccio i controlli
					currentImpegnoPluriennale.setDataScadenzaImpPluriennale(DateUtility.parse(currentImpegnoPluriennale.getDataScadenzaImpPluriennaleString()));
					
					Calendar c = new GregorianCalendar();
					c.setTime(currentImpegnoPluriennale.getDataScadenzaImpPluriennale());
					if (c.get(Calendar.YEAR) != currentImpegnoPluriennale.getAnnoImpPluriennale()) {
						if(oggettoDaPopolareImpegno()){
							addErrore(ErroreFin.DATA_SCADENZA_IMPEGNO_ERRATA.getErrore(""));
						}else{
							addErrore(ErroreFin.DATA_SCADENZA_ACCERTAMENTO_ERRATA.getErrore(""));
						}
						
						return true;
					}
					
				}else currentImpegnoPluriennale.setDataScadenzaImpPluriennale(null);
				
				try{
				currentImpegnoPluriennale.setImportoImpPluriennale(convertiImportoToBigDecimal(currentImpegnoPluriennale.getImportoImpPluriennaleString()));
				}catch(Exception e){
					addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo"));
					return true;
				}
				if(currentImpegnoPluriennale.getImportoImpPluriennale().compareTo(BigDecimal.ZERO)<=0){
					currentImpegnoPluriennale.setImportoImpPluriennale(BigDecimal.ZERO);
				}else{
					currentImpegnoPluriennale.setImportoImpPluriennale(convertiImportoToBigDecimal(currentImpegnoPluriennale.getImportoImpPluriennaleString()));
				}
				
				
				controlloAnni.put(currentImpegnoPluriennale.getAnnoImpPluriennale(), (controlloAnni.get(currentImpegnoPluriennale.getAnnoImpPluriennale()) != null) ? controlloAnni.get(currentImpegnoPluriennale.getAnnoImpPluriennale()) + 1 : 1);
				if (controlloAnni.get(currentImpegnoPluriennale.getAnnoImpPluriennale()) > 1) {
					addErrore(ErroreFin.ANNO_PLURIENNALE_GIA_PRESENTE.getErrore(String.valueOf(currentImpegnoPluriennale.getAnnoImpPluriennale())));
					return true;
				}
				// controllo salvataggi pluriennali
				
				if (controlloImportiPlur(currentImpegnoPluriennale.getAnnoImpPluriennale(), oggettoDaPopolareImpegno())) {
			    	if(oggettoDaPopolareImpegno()){
			    		addErrore(ErroreFin.DISPONIBILITA_INSUFFICIENTE_IMPEGNI_PLUR.getErrore("Inserimento", String.valueOf(currentImpegnoPluriennale.getAnnoImpPluriennale())));
			    		return true;
				    }else if(!isAccPlurRiaccGiaRicercata()){
				    	//Jira 559
				    	addActionWarning(ErroreFin.SUPERAMENTO_DISPONIBILITA.getErrore().getCodice()+" : "+ErroreFin.SUPERAMENTO_DISPONIBILITA.getErrore().getDescrizione());
				    	return false;
				    }
			    	
			    }
				
			}
		}
		
		
		
		
		return false;
	}  
	
	public String pulisciCampoPluriennale() {
		setMethodName("pulisciCampoPluriennale");
		for(ImpegniPluriennaliModel i: model.getStep3Model().getListaImpegniPluriennali()){			
			if(i.getAnnoImpPluriennale() == getCampoPerPulire()){
				i.setAnnoImpPluriennale(0);
				i.setDataScadenzaImpPluriennale(null);
				i.setDataScadenzaImpPluriennaleString("");
				i.setImportoImpPluriennale(BigDecimal.ZERO);
				i.setImportoImpPluriennaleString("0");
				return SUCCESS;
			}		
		}
		/* AGGIUNGERE ERRORE GIUSTO */
		addErrore(methodName, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("ERRORE") );
		return INPUT;
	}
	
	//Jira numero 16; se lo stato del bilancio e' predisposizione consuntivo, l'hanno dell'impegno/accertamente
	public boolean disabilitatoSePredispConsul(){
		if(sessionHandler.getFaseBilancio().equalsIgnoreCase("O") && sessionHandler.getFaseBilancio()!= null){
			return true;
		}else{
			return false;
		}
	}

	public int getCampoPerPulire() {
		return campoPerPulire;
	}

	public void setCampoPerPulire(int campoPerPulire) {
		this.campoPerPulire = campoPerPulire;
	}

	public String getNumeroImpegno() {
		return numeroImpegno;
	}

	public void setNumeroImpegno(String numeroImpegno) {
		this.numeroImpegno = numeroImpegno;
	}

	public String getAnnoImpegno() {
		return annoImpegno;
	}

	public void setAnnoImpegno(String annoImpegno) {
		this.annoImpegno = annoImpegno;
	}

	public String getNumeroAccertamento() {
		return numeroAccertamento;
	}

	public void setNumeroAccertamento(String numeroAccertamento) {
		this.numeroAccertamento = numeroAccertamento;
	}

	public String getAnnoAccertamento() {
		return annoAccertamento;
	}

	public void setAnnoAccertamento(String annoAccertamento) {
		this.annoAccertamento = annoAccertamento;
	}

	public boolean isImpegnoRiaccGiaRicercata() {
		return impegnoRiaccGiaRicercata;
	}

	public void setImpegnoRiaccGiaRicercata(boolean impegnoRiaccGiaRicercata) {
		this.impegnoRiaccGiaRicercata = impegnoRiaccGiaRicercata;
	}

	public String getNumeroImpegnoStruts3() {
		return numeroImpegnoStruts3;
	}

	public void setNumeroImpegnoStruts3(String numeroImpegnoStruts3) {
		this.numeroImpegnoStruts3 = numeroImpegnoStruts3;
	}

	public String getAnnoImpegnoStruts3() {
		return annoImpegnoStruts3;
	}

	public void setAnnoImpegnoStruts3(String annoImpegnoStruts3) {
		this.annoImpegnoStruts3 = annoImpegnoStruts3;
	}

	public String getDoveMiTrovo() {
		return doveMiTrovo;
	}

	public void setDoveMiTrovo(String doveMiTrovo) {
		this.doveMiTrovo = doveMiTrovo;
	}

	/**
	 * GESTORE TRANSAZIONE ECONOMICA
	 */
	@Override
	public String confermaPdc() {
		teSupport.setRicaricaAlberoPianoDeiConti(false);
			
		//nuovo confronto per capire il livello del PDC
		if(teSupport.getPianoDeiConti()!=null && 
				   teSupport.getPianoDeiConti().getTipoClassificatore()!=null && 
				   !teSupport.getPianoDeiConti().getTipoClassificatore().getCodice().equalsIgnoreCase(V_LIVELLO_TIPO_CLASSIFICATORE)){
		
			teSupport.setPianoDeiConti(new ElementoPianoDeiConti());
			addErrore(ErroreFin.ELEM_PDC_NON_INDICATO.getErrore(""));
			
		}
		return SUCCESS;
	}

	public boolean isAccPlurRiaccGiaRicercata() {
		return accPlurRiaccGiaRicercata;
	}

	public void setAccPlurRiaccGiaRicercata(boolean accPlurRiaccGiaRicercata) {
		this.accPlurRiaccGiaRicercata = accPlurRiaccGiaRicercata;
	}

	public boolean isImpegnoOrigineGiaRicercata() {
		return impegnoOrigineGiaRicercata;
	}

	public void setImpegnoOrigineGiaRicercata(boolean impegnoOrigineGiaRicercata) {
		this.impegnoOrigineGiaRicercata = impegnoOrigineGiaRicercata;
	}


	/**
	 * @return the showModaleConfermaAnnoDiCompetenzaInCorso
	 */
	public boolean isShowModaleConfermaAnnoDiCompetenzaInCorso() {
		return showModaleConfermaAnnoDiCompetenzaInCorso;
	}


	/**
	 * @param showModaleConfermaAnnoDiCompetenzaInCorso the showModaleConfermaAnnoDiCompetenzaInCorso to set
	 */
	public void setShowModaleConfermaAnnoDiCompetenzaInCorso(
			boolean showModaleConfermaAnnoDiCompetenzaInCorso) {
		this.showModaleConfermaAnnoDiCompetenzaInCorso = showModaleConfermaAnnoDiCompetenzaInCorso;
	}


	/**
	 * @return the fromModaleConfermaAnnoDiCompetenzaInCorso
	 */
	public boolean isFromModaleConfermaAnnoDiCompetenzaInCorso() {
		return fromModaleConfermaAnnoDiCompetenzaInCorso;
	}


	/**
	 * @param fromModaleConfermaAnnoDiCompetenzaInCorso the fromModaleConfermaAnnoDiCompetenzaInCorso to set
	 */
	public void setFromModaleConfermaAnnoDiCompetenzaInCorso(
			boolean fromModaleConfermaAnnoDiCompetenzaInCorso) {
		this.fromModaleConfermaAnnoDiCompetenzaInCorso = fromModaleConfermaAnnoDiCompetenzaInCorso;
	}


	/**
	 * @return the pluriennalePrimeNoteEsercizioInCorso
	 */
	public boolean isPluriennalePrimeNoteEsercizioInCorso() {
		return pluriennalePrimeNoteEsercizioInCorso;
	}


	/**
	 * @param pluriennalePrimeNoteEsercizioInCorso the pluriennalePrimeNoteEsercizioInCorso to set
	 */
	public void setPluriennalePrimeNoteEsercizioInCorso(boolean pluriennalePrimeNoteEsercizioInCorso) {
		this.pluriennalePrimeNoteEsercizioInCorso = pluriennalePrimeNoteEsercizioInCorso;
	}

	
	/**
	 * Checks if is richiedi conferma redirezione contabilita generale.
	 *
	 * @return true, if is richiedi conferma redirezione contabilita generale
	 */
//	//SIAC-5333
//	public boolean isRichiediConfermaRedirezioneContabilitaGenerale() {
//		return model.isRichiediConfermaRedirezioneContabilitaGenerale();
//	}
	
	


}

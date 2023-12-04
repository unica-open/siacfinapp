/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaSubGenericAction extends WizardAggiornaMovGestAction {

	private static final long serialVersionUID = 1L;

	protected boolean ricaricaDopoInserimento = false;
	
	protected void caricaLabelsAggiornaSub() {
		if (oggettoDaPopolareSubimpegno()) {
			model.getLabels().put(FORM, "aggiornaSubimpegno");
			model.getLabels().put(LABEL_OGGETTO_GENERICO_PADRE, "Impegno");
			model.getLabels().put(LABEL_OGGETTO_GENERICO, "Subimpegno");
			model.getLabels().put(LABEL_OGGETTO_GENERICO_VERBO, "subimpegnare");
			model.getLabels().put(LABEL_INSERISCI, "inserisci subimpegno");
		} else {
			model.getLabels().put(FORM, "aggiornaSubaccertamento");
			model.getLabels().put(LABEL_OGGETTO_GENERICO_PADRE, "Accertamento");
			model.getLabels().put(LABEL_OGGETTO_GENERICO, "Subaccertamento");
			model.getLabels().put(LABEL_OGGETTO_GENERICO_VERBO, "subaccertare");
			model.getLabels().put(LABEL_INSERISCI, "inserisci subaccertamento");
		}
	}
	
	/**
	 * serve per far comparire o meno il pulsante di subacc/subimp
	 * 
	 * Il movgest non deve essere in stato D
	 * 
	 * @return
	 */
	public boolean statoSubDefinitivo(){
		debug("statoSubDefinitivo", "verifico la condizione per far apparire il btn");
		boolean ritorno = false;
		
		if(StringUtils.isNotEmpty(model.getStep1Model().getSoggetto().getCodCreditore()) ||
				StringUtils.isNotEmpty(model.getStep1Model().getSoggetto().getClasse()) ||
				model.getStep1Model().getStatoOperativo().equalsIgnoreCase("D")){
			
			ritorno = true;
		} else {
			
			/*
			 * per potersi vedere il bottone il bialncio deve essere
			 * E -Esercizio Provvisorio
			 * G -Gestione
			 * A - Assestamento
			 * O - Predisposizione Consuntivo 
			 */
			
			if(sessionHandler.getFaseBilancio() != null && !(sessionHandler.getFaseBilancio().equalsIgnoreCase("E") ||  
					sessionHandler.getFaseBilancio().equalsIgnoreCase("G") || 
					sessionHandler.getFaseBilancio().equalsIgnoreCase("A") || 
					sessionHandler.getFaseBilancio().equalsIgnoreCase("O") )){

				ritorno = true;
				
			} else {
				ritorno = false;
			}
			
		}
		
		return ritorno;
	}
	
//	public String inserisciSubimpegno() {
//		inserisciSub();
//		return GOTO_GESTIONE_SUBIMPEGNO;
//	}
//	
//	public void inserisciSub() {
//		setIdSubImpegno(null);
//		setInserimentoSubimpegno(true);
//		model.setProseguiConWarningControlloSoggetti(false);
//		model.getStep1ModelSubimpegno().setOperazioneInserimento(true);
//		
//		//FIX per JIRA SIAC-3426:
//		model.setSubDettaglio(new MovimentoConsulta());
//	}
	
	/* **************************************************************************** */
	/*  Controllo bottoniera 														*/
	/* **************************************************************************** */
	
	/**
	 *  logiche comuni per subimp e subacc
	 * @param stato
	 * @return
	 */
	protected boolean isAggiornaAbilitato(String stato){
		boolean abilita = true;
		
		if(stato.equals("A")){
		     	 return false;
		}	
		
		abilita = isFaseBilancioAbilitata();
		
	    return abilita;
	}

	public boolean isEliminaAbilitato(String stato){
		
		boolean abilita = true;
		
		if(stato.equalsIgnoreCase("A")){
		     return false;
		}
		
		if(stato.equalsIgnoreCase("D")){
			return false;
		}
		abilita = isFaseBilancioAbilitata();
		
	    return abilita;
	}
	
	/**
	 * logiche comuni per subimp e subacc
	 * @param stato
	 * @return
	 */
	protected boolean isAnnullaAbilitato(String stato){
		
		boolean abilita = true;
		
		if(stato.equals("A")){
		     	 return false;
		}
		if(stato.equals("P")){
	     	 return false;
		}
		
	    return abilita;
	}

	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */
	
	public boolean isRicaricaDopoInserimento() {
		return ricaricaDopoInserimento;
	}

	public void setRicaricaDopoInserimento(boolean ricaricaDopoInserimento) {
		this.ricaricaDopoInserimento = ricaricaDopoInserimento;
	}
	
}
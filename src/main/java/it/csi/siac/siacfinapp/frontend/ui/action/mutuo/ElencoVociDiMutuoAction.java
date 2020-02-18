/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.mutuo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.util.FinStringUtils;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.mutuo.VariazioneImportoVoceMutuo.TipoVariazioneImportoVoceMutuo;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;




@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ElencoVociDiMutuoAction extends GenericVoceDiMutuoAction {

	private static final long serialVersionUID = 1L;
	private Integer idVoceDiMutuo;
	
	private String importoEconomia;
	private String importoRiduzione;
	private String importoRettifica;
	
	public boolean status;
	
	
	@Override
	public void prepare() throws Exception {
		//invoco il prepare della super classe:
		super.prepare();
		
		//setto il titolo:
		this.model.setTitolo("Elenco Voci di Mutuo");
	}	
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		debug("execute", " codice voce mutuo "+getCodiceMutuo());
		
		// routine di ricerca mutuo
		if(getCodiceMutuo()!=null){
			if(routineRicercaMutuoPerChiave().equalsIgnoreCase(INPUT)){
				return INPUT;
			}
		}
			
	    return SUCCESS;
	}
	
	public void preInserisciModifica(){
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String numVoceMutuoSel = request.getParameter("numVoceMutuoSel");
		String importoDigit = request.getParameter("importoDigit");
	
		model.setImportoDigitato(importoDigit);
		model.setNumVoceMutuoSelezionato(numVoceMutuoSel);
		
	}
	
	public void postInserisciModifica(){
		model.setImportoDigitato(null);
		model.setNumVoceMutuoSelezionato(null);
	}
	
	/**
	 * funzione di inserimento di economia
	 * @return
	 */
	public String inserisciEconomiaVoceMutuo(){
		
		String numVoceMutuoSel = model.getNumVoceMutuoSelezionato();
		String importoDigitato = model.getImportoDigitato();
		postInserisciModifica();
		
		if(!FinStringUtils.isEmpty(numVoceMutuoSel)){
			setNumeroVoceMutuoSelezionato(numVoceMutuoSel);
			//
			setImportoEconomia(importoDigitato);
			//
			
			debug("execute", " importo  "+getImportoEconomia());
			debug("execute", " importo  "+getNumeroVoceMutuoSelezionato());
			List<Errore> listaErrori = new ArrayList<Errore>();		
			//controllo che il dato oggligatorio sia stato inserito
			if(null==getImportoEconomia() || StringUtils.isEmpty(getImportoEconomia()) ){
				 listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo economia"));
			}else{
				//catturo la voce di mutuo che ho selezionato
				VoceMutuo voce= new VoceMutuo();
				for(VoceMutuo vm:model.getMutuoSelezionato().getListaVociMutuo()){
					if(getNumeroVoceMutuoSelezionato().equalsIgnoreCase(vm.getNumeroVoceMutuo())){
						voce=vm;
					}
				}
				
				BigDecimal importoDisponibile = null;
				
				if(voce.getImportoDisponibileModificheImpegno()==null){
					voce.setImportoDisponibileModificheImpegno(BigDecimal.ZERO);
					importoDisponibile = voce.getImportoDisponibileModificheImpegno();
				}else{
					importoDisponibile = voce.getImportoDisponibileModificheImpegno().negate();
				}
				//se la disponibilita' e' minore rispetto all'importo inserito, scateno l'errore
				if(importoDisponibile.compareTo(convertiImportoToBigDecimal(getImportoEconomia()))<0 ){
					 listaErrori.add(ErroreFin.IMPORTO_ECO_RID_MAGGIORE.getErrore(""));
					 addErrori(listaErrori);
					 return INPUT;
				}
	  
				BigDecimal importoEconomiaSupport = convertiImportoToBigDecimal(getImportoEconomia());
				//se l'importo economia e' inferiore o uguale a 0 scateno l'errore
				if (importoEconomiaSupport.compareTo(BigDecimal.ZERO) <= 0) {
					
					listaErrori.add(ErroreFin.IMPORTO_ECO_RID_MAGGIORE.getErrore(""));
					
					//l'importo economia deve essere piu' piccolo della disponibilita' del mutuo
				}else if(importoEconomiaSupport.compareTo(model.getMutuoSelezionato().getDisponibileMutuo())>0){
					
					
							listaErrori.add(ErroreFin.IMPORTO_ECO_RID_MAGGIORE.getErrore(""));
				}else {
				
		
					if (model.getMutuoSelezionato().getListaVociMutuo() != null	&& model.getMutuoSelezionato().getListaVociMutuo().size() > 0) {
						
						// inserisco la variazione passando importo e tipoVariazione
						return cicloPerVariazione(importoEconomiaSupport, TipoVariazioneImportoVoceMutuo.ECONOMIA);
					}	
				}
			}
		 
			//stampo eventuali errori trovati
			if(!listaErrori.isEmpty()) {
				addErrori(listaErrori);
				return INPUT; 
			}
			
			return SUCCESS;
			
		}
		return INPUT;
	}
	
	/**
	 * funzione di inserimento di riduzione
	 * @return
	 */
	public String inserisciRiduzioneVoceMutuo(){
		
		String numVoceMutuoSel = model.getNumVoceMutuoSelezionato();
		String importoDigitato = model.getImportoDigitato();
		postInserisciModifica();
		
		if(!FinStringUtils.isEmpty(numVoceMutuoSel)){
			setNumeroVoceMutuoSelezionato(numVoceMutuoSel);
			//
			setImportoRiduzione(importoDigitato);
			//
			
			debug("execute", " importo  "+getImportoEconomia());
			debug("execute", " importo  "+getNumeroVoceMutuoSelezionato());
			List<Errore> listaErrori = new ArrayList<Errore>();		
			//controllo che sia stato inserito un valore nel campo
			if(null==getImportoRiduzione() || StringUtils.isEmpty(getImportoRiduzione()) ){
				 listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo riduzione voce"));
			}else{
				//catturo la voce di mutuo selezionata
				VoceMutuo voce= new VoceMutuo();
				for(VoceMutuo vm:model.getMutuoSelezionato().getListaVociMutuo()){
					if(getNumeroVoceMutuoSelezionato().equalsIgnoreCase(vm.getNumeroVoceMutuo())){
						voce=vm;
					}
				}
	  
				BigDecimal importoDisponibile = null;
				
				if(voce.getImportoDisponibileModificheImpegno()==null){
					voce.setImportoDisponibileModificheImpegno(BigDecimal.ZERO);
					importoDisponibile = voce.getImportoDisponibileModificheImpegno();
				}else{
					
					importoDisponibile = voce.getImportoDisponibileModificheImpegno().negate();
					
				}
				//se la disponibilita' e' minore rispetto all'importo inserito, scateno l'errore
				if(importoDisponibile.compareTo(convertiImportoToBigDecimal(getImportoRiduzione()))<0 ){
					 listaErrori.add(ErroreFin.IMPORTO_ECO_RID_MAGGIORE.getErrore(""));
					 addErrori(listaErrori);
					 return INPUT;
				}
				
		
				BigDecimal importoRiduzioneSupport = convertiImportoToBigDecimal(getImportoRiduzione());
				//controllo che l'importo inserito non sia minore o uguale a 0
				if (importoRiduzioneSupport.compareTo(BigDecimal.ZERO) <= 0) {
					
					listaErrori.add(ErroreFin.IMPORTO_ECO_RID_MAGGIORE.getErrore(""));
					//se l'importo inserito e' pi grande della disponibilita' scateno l'errore
				}else if(importoRiduzioneSupport.compareTo(model.getMutuoSelezionato().getDisponibileMutuo())>0){
					
					
					listaErrori.add(ErroreFin.IMPORTO_ECO_RID_MAGGIORE.getErrore(""));
			}else{
			
				if(model.getMutuoSelezionato().getListaVociMutuo()!=null && model.getMutuoSelezionato().getListaVociMutuo().size()>0 ){
						// inserisco la variazione passando importo e tipoVariazione
						return cicloPerVariazione(importoRiduzioneSupport, TipoVariazioneImportoVoceMutuo.RIDUZIONE);
					}
				}
			}
			
			//stampo eventuali errori
			if(!listaErrori.isEmpty()) {
				addErrori(listaErrori);
				return INPUT; 
			}
			
			return SUCCESS;
			
		}
		
		return INPUT; 
		
	}
	
	
	/**
	 * funzione di inserimento di rettifica
	 * @return
	 */
	public String inserisciRettificaVoceMutuo(){
		
		String numVoceMutuoSel = model.getNumVoceMutuoSelezionato();
		String importoDigitato = model.getImportoDigitato();
		postInserisciModifica();
		
		if(!FinStringUtils.isEmpty(numVoceMutuoSel)){
			setNumeroVoceMutuoSelezionato(numVoceMutuoSel);
			//
			setImportoRettifica(importoDigitato);
			//
			
			
			debug("execute", " importo  "+getImportoEconomia());
			debug("execute", " importo  "+getNumeroVoceMutuoSelezionato());
			List<Errore> listaErrori = new ArrayList<Errore>();		
			//controllo che il campo non sia vuoto
			if(null==getImportoRettifica() || StringUtils.isEmpty(getImportoRettifica()) ){
				 listaErrori.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo Rettifica voce"));
			}else{
				
				BigDecimal importoRettificaSupport = convertiImportoToBigDecimal(getImportoRettifica());
				
				
				//Prendere impegno
				VoceMutuo voceMutuoSelezionata= null;
				BigDecimal sommatoriaImportoVoci= new BigDecimal(0) ;
				//Cerco la voce di mutuo selezionata
				if(model.getMutuoSelezionato()!=null && model.getMutuoSelezionato().getListaVociMutuo()!=null
						&& model.getMutuoSelezionato().getListaVociMutuo().size()>0){
					
					for(VoceMutuo v: model.getMutuoSelezionato().getListaVociMutuo()){
						if(v!=null && v.getNumeroVoceMutuo()!=null){
							if(v.getNumeroVoceMutuo().equalsIgnoreCase(getNumeroVoceMutuoSelezionato())){
								voceMutuoSelezionata=v;
								break;
							}
						}
					}
					//sonno gli importi delle voci di mutuo
					for(VoceMutuo v: model.getMutuoSelezionato().getListaVociMutuo()){
						if(v!=null && v.getNumeroVoceMutuo()!=null){
							if(v.getImportoAttualeVoceMutuo()!= null){
								sommatoriaImportoVoci=sommatoriaImportoVoci.add(v.getImportoAttualeVoceMutuo());
							}
						}
					}
					
				}
				if (importoRettificaSupport.compareTo(BigDecimal.ZERO) <= 0) {
					
					// controllo su voce di mutuo IMPORTO_VOCE_MUTUO_INFERIORE_ALLA_SOMMA_LIQUIDAZIONI_SU_IMPEGNO
					if(voceMutuoSelezionata!=null){
						//qui voceMutuoSelezionata non sara' mai null (o cmq non dovrebbe esserlo), avvolgo in questo controllo per le metriche sonar.
						if(importoRettificaSupport.negate().compareTo(voceMutuoSelezionata.getImportoAttualeVoceMutuo()) >0 ){
							listaErrori.add(ErroreFin.IMPORTO_ECO_RID_MAGGIORE.getErrore(""));
						}
					}
					
					/*  DA DECOMMENTARE QUANDO VERRa' IMPLEMENTATO NELLA LIQUIDAZIONE, L'IMPORTO LIQUIDATO VOCE NULLO, PERCHe' ARRIVA SEMPRE NULL 
					if(voceMutuoSelezionata.getImportoAttualeVoceMutuo().intValue()< voceMutuoSelezionata.getImportoLiquidatoVoceMutuo().intValue()){
						listaErrori.add(ErroreFin.IMPORTO_VOCE_MUTUO_INFERIORE_ALLA_SOMMA_LIQUIDAZIONI_SU_IMPEGNO.getErrore(""));
					}
					*/
					
				}else{
					if(voceMutuoSelezionata!=null && voceMutuoSelezionata.getImportoAttualeVoceMutuo()!=null &&
							voceMutuoSelezionata.getImpegno()!=null && voceMutuoSelezionata.getImpegno().getDisponibilitaFinanziare()!=null){
						//controlli di null pointer perfettamente ridondanti, li metto solo per far felice sonar.
						
						BigDecimal bd= importoRettificaSupport;
						bd=bd.add(voceMutuoSelezionata.getImportoAttualeVoceMutuo());
						 
						//se l'importo della rettifica e' maggiore della disponibilita' a finanziare dell'impegno, scateno l'errore  
						if(bd.compareTo(voceMutuoSelezionata.getImpegno().getDisponibilitaFinanziare())>0){
							listaErrori.add(ErroreFin.IMPORTO_VOCE_MUTUO_MAGGIORE_DISPONIBILITA_IMPEGNO.getErrore(""));
						}
						 
						//aggiungo alla sommatoria l'importo rettifica
						sommatoriaImportoVoci=sommatoriaImportoVoci.add(importoRettificaSupport);
						
						//se l'importo del mutuo meno il totale delle voci e' minore a 0 scateno l'errore
						if(model.getMutuoSelezionato().getImportoInizialeMutuo().subtract(sommatoriaImportoVoci).compareTo(BigDecimal.ZERO)<0){
							listaErrori.add(ErroreFin.IMPORTO_VOCE_MUTUO_MAGGIORE_DISPONIBILE_MUTUO.getErrore(""));
						}else if(model.getMutuoSelezionato().getImportoInizialeMutuo().subtract(voceMutuoSelezionata.getImportoAttualeVoceMutuo()).compareTo(BigDecimal.ZERO)<0){
							listaErrori.add(ErroreFin.IMPORTO_VOCE_MUTUO_MAGGIORE_DISPONIBILE_MUTUO.getErrore(""));
						}	
						// che la somma
						
					}
				}
				
				if(listaErrori.isEmpty()){
					if(model.getMutuoSelezionato().getListaVociMutuo()!=null && model.getMutuoSelezionato().getListaVociMutuo().size()>0 ){
						// inserisco la variazione passando importo e tipoVariazione
						return cicloPerVariazione(importoRettificaSupport, TipoVariazioneImportoVoceMutuo.RETTIFICA);
					}
				}
			}
			//stampo eventuali errori
			if(!listaErrori.isEmpty()) {
				addErrori(listaErrori);
				return INPUT; 
			}
			return SUCCESS;	
			
		}
		
		return INPUT; 
	}
	
	/**
	 * disabilita funzione se la disponibilita == ZERO
	 * @return
	 */
	public boolean disabilitaPerDisponibilita(){
		if(model.getMutuoSelezionato().getDisponibileMutuo().compareTo(BigDecimal.ZERO)==0){
			return true;
		}else{
			return false;
		}
	}
	
	
	/**
	 * funzione di inserimento di una nuova voce di mutuo
	 * @return
	 */
	public String inserisciVoceDiMutuo() {
		setIdVoceDiMutuo(null);
		return GOTO_GESTIONE_VOCE_DI_MUTUO; 
	}
 
	/* **************************************************************************** */
	/*  Getter / setter																*/
	/* **************************************************************************** */
	public Integer getIdVoceDiMutuo() {
		return idVoceDiMutuo;
	}

	public void setIdVoceDiMutuo(Integer idVoceDiMutuo) {
		this.idVoceDiMutuo = idVoceDiMutuo;
	}

	public String getImportoEconomia() {
		return importoEconomia;
	}

	public void setImportoEconomia(String importoEconomia) {
		this.importoEconomia = importoEconomia;
	}

	public String getImportoRiduzione() {
		return importoRiduzione;
	}

	public void setImportoRiduzione(String importoRiduzione) {
		this.importoRiduzione = importoRiduzione;
	}

	public String getImportoRettifica() {
		return importoRettifica;
	}

	public void setImportoRettifica(String importoRettifica) {
		this.importoRettifica = importoRettifica;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
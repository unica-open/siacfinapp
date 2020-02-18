/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.mutuo;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;


@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class StornoVoceMutuoStep1Action extends GenericVoceDiMutuoAction{

	
	private static final long serialVersionUID = 1L;
	
	//numero voce mutuo
	private String numeroVoceMutuo;

	
	@Override
	public void prepare() throws Exception {
		//richiamo il prepare della super classe:
		super.prepare();
		//setto il titolo di pagina:
		this.model.setTitolo("Storno - Importo");
	}	
	
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		//informazioni utili per debuggare:
		debug("execute", " codice voce mutuo "+model.getMutuoSelezionato().getCodiceMutuo());
		debug("execute", " numeroVoceMutuo "+getNumeroVoceMutuo());
		
		//setto il numero voce mutuo selezionato:
		setNumeroVoceMutuoSelezionato(getNumeroVoceMutuo());
	
		return SUCCESS;
	}
	
	
	public String prosegui(){
		debug("prosegui", " ecco ");
		
		if(StringUtils.isEmpty(model.getImportoStornoStringa())){
			//importo mancante
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo economia"));
			return INPUT;
		}else{
			BigDecimal valore = convertiImportoToBigDecimal(model.getImportoStornoStringa());
			//cerco la voce di mutuo sulla quale voglio operare 
			VoceMutuo voce= new VoceMutuo();
			for(VoceMutuo vm:model.getMutuoSelezionato().getListaVociMutuo()){
				if(getNumeroVoceMutuoSelezionato().equalsIgnoreCase(vm.getNumeroVoceMutuo())){
					//trovata
					voce=vm;
				}
			}
			
			//controllo obbligatorieta importo disponibile modifiche:
			if(voce.getImportoDisponibileModificheImpegno()!= null){
				if(voce.getImportoDisponibileModificheImpegno().negate().subtract(valore).compareTo(BigDecimal.ZERO)<0 ){
					//errore disponbilita' insufficiente
					addErrore(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Inserimento storno voce di mutuo","modifiche"));
					return INPUT;
				}
			}else{
				//errore disponbilita' insufficiente
				addErrore(ErroreFin.DISPONIBILITA_INSUFFICIENTE.getErrore("Inserimento storno voce di mutuo","modifiche"));
				return INPUT;
			}
			
			
			debug("prosegui", " valore "+valore);
			
			//setto il nuovo importo nel model:
			model.setImportoNuovaVoceDiMutuo(convertiBigDecimalToImporto(valore));
		}
		
		return "prosegui";
	}

	// GETTER E SETTER:	

	public String getNumeroVoceMutuo() {
		return numeroVoceMutuo;
	}

	public void setNumeroVoceMutuo(String numeroVoceMutuo) {
		this.numeroVoceMutuo = numeroVoceMutuo;
	}
}
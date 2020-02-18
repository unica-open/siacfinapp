/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.movgest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacfinapp.frontend.ui.action.OggettoDaPopolareEnum;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ImpegniPluriennaliModel;
import it.csi.siac.siacfinapp.frontend.ui.util.DateUtility;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisceImpegnoStep3Action extends ActionKeyInserisceImpegno {
	
	private static final long serialVersionUID = 1L;
	
	public InserisceImpegnoStep3Action () {
		//setto la tipologia di oggetto trattato:
		oggettoDaPopolare = OggettoDaPopolareEnum.IMPEGNO;
	}
	
	@Override
	public void prepare() throws Exception {
		setMethodName("prepare");
		//invoco il prepare della super classe:
		super.prepare();
		//setto il titolo di pagina:
		this.model.setTitolo("Inserisce Impegno - Impegni pluriennali");
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		
		// forzo il giro sulla prima pagina per 
		// errore filo di arianna JIra - 757
		if(teSupport==null){
			return "erroreFiloArianna";
		}
	
		//richiamo l'execute specifico per lo step 3:
		executeStep3();
		
		//Carico la label:
		caricaLabelsInserisci(3);
	    return SUCCESS;
	}   
	
	public String annulla() throws Exception {
		Integer cont=1;
		//ciclo sulla lista di impegni pluriennali:
		for(ImpegniPluriennaliModel i: model.getStep3Model().getListaImpegniPluriennali()){
			i.setAnnoImpPluriennale(model.getStep1Model().getAnnoImpegno()+cont);
			i.setImportoImpPluriennale(model.getStep1Model().getImportoImpegno());
			i.setImportoImpPluriennaleString(model.getStep1Model().getImportoFormattato());
			if(model.getStep1Model().getScadenza()!=null && 
			   !StringUtils.isEmpty(model.getStep1Model().getScadenza())){
					Calendar c = new GregorianCalendar();
					c.setTime(DateUtility.parse(model.getStep1Model().getScadenza()));
					c.set(Calendar.YEAR, i.getAnnoImpPluriennale());
					i.setDataScadenzaImpPluriennale(c.getTime());
					// setto anche la data nel formato stringa
					SimpleDateFormat formatDateJava = new SimpleDateFormat("dd/MM/yyyy");
					String DataString= formatDateJava.format(i.getDataScadenzaImpPluriennale());
					i.setDataScadenzaImpPluriennaleString(DataString);
			}else{
				//pulisco data scadenza pluriennale:
				i.setDataScadenzaImpPluriennale(new Date());
				i.setDataScadenzaImpPluriennaleString("");
			}
			cont++;
		}
		
	    return SUCCESS;
	}
	
	public String salvaPluriennalePrimeNoteEsercizioInCorso() throws Exception {
		Integer annoEsercio = Integer.valueOf(sessionHandler.getAnnoEsercizio());
		Integer annoScritturaEconomicoPatrimoniale = annoEsercio;
		//invoco il metodo centralizzato passando l'annoScritturaEconomicoPatrimoniale:
		return salvaPluriennaleImpegno(annoScritturaEconomicoPatrimoniale);
	}
	
	public String salvaPluriennalePrimeNoteEserciziFuturi() throws Exception {
		//invoco il metodo centralizzato senza l'annoScritturaEconomicoPatrimoniale:
		return salvaPluriennaleImpegno(null);
	}
	
	public String salvaPluriennale() throws Exception {
		//invoco il metodo centralizzato senza l'annoScritturaEconomicoPatrimoniale:
		return salvaPluriennaleImpegno(null);
	}
	
}

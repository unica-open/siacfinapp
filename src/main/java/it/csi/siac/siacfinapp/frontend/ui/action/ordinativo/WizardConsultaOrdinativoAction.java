/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.ordinativo;

import it.csi.siac.siacfinapp.frontend.ui.action.GenericFinAction;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.ConsultaOrdinativoModel;



public abstract class WizardConsultaOrdinativoAction<M extends ConsultaOrdinativoModel> extends GenericFinAction<M> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4825919870290768689L;
	
	protected String methodName;
	protected String anno;
	protected String numero;
	
	//tab folder selezionato:
	protected String tabFolder;

	public String numeroOrdinativoSelezionato;
	
	public void cambiaTabFolder(){
		 
		if("tabQuote".equals(tabFolder)){
			//tab delle quote
			model.setActiveTab("quote");
		}
		
		if("tabProvvisori".equals(tabFolder)){
			//tab dei provvisori
			model.setActiveTab("provvisori");
		}
		
		if("tabOrdinativiCollegati".equals(tabFolder)){
			//tab degli ordinativi collegati
			model.setActiveTab("ordinativiCollegati");
		}
		
		if("tabOrdinativoIncasso".equals(tabFolder)){
			//tab ordinativo incasso
			model.setActiveTab("ordinativoIncasso");
		}
		
		if("tabOrdinativoPagamento".equals(tabFolder)){
			//tab ordinativo pagamento
			model.setActiveTab("ordinativoPagamento");
		}
		
	}
	
	// GETTER e SETTER: 														*/

	public String getMethodName() {
		return this.methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

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

	public String getNumeroOrdinativoSelezionato() {
		return this.numeroOrdinativoSelezionato;
	}

	public void setNumeroOrdinativoSelezionato(String numeroOrdinativoSelezionato) {
		this.numeroOrdinativoSelezionato = numeroOrdinativoSelezionato;
	}

	public String getTabFolder() {
		return tabFolder;
	}

	public void setTabFolder(String tabFolder) {
		this.tabFolder = tabFolder;
	}

}

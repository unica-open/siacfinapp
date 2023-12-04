/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.provvisorio;

import it.csi.siac.siacfinapp.frontend.ui.action.GenericFinAction;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.ConsultaProvvisorioModel;



public abstract class WizardConsultaProvvisorioAction<M extends ConsultaProvvisorioModel> extends GenericFinAction<M> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4825919870290768689L;
	
	protected String methodName;
	
	protected String numeroProvv;
	protected String annoProvv;
	protected String tipoProvv;
	
	//tab folder selezionato:
	protected String tabFolder;

	public String numeroOrdinativoSelezionato;
	
	private final static String ACTIVE_TAB_PROVVISORIO_CASSA = "provvisorioCassa";
	private final static String ACTIVE_TAB_DOCUMENTI = "documenti";
	private final static String ACTIVE_TAB_ORDINATIVI = "ordinativi";
	
	public void cambiaTabFolder(){
		 
		if("tabProvvisorioCassa".equals(tabFolder)){
			//tab delle quote
			model.setActiveTab(ACTIVE_TAB_PROVVISORIO_CASSA);
		}
		
		if("tabDocumenti".equals(tabFolder)){
			//tab dei provvisori
			model.setActiveTab(ACTIVE_TAB_DOCUMENTI);
		}
		
		if("tabOrdinativi".equals(tabFolder)){
			//tab degli ordinativi collegati
			model.setActiveTab(ACTIVE_TAB_ORDINATIVI);
		}
		
	}
	
	protected boolean isActiveTabProvvisorioCassa(){
		return ACTIVE_TAB_PROVVISORIO_CASSA.equals(model.getActiveTab());
	}
	
	protected boolean isActiveTabDocumenti(){
		return ACTIVE_TAB_DOCUMENTI.equals(model.getActiveTab());
	}
	
	protected boolean isActiveTabOrdinativi(){
		return ACTIVE_TAB_ORDINATIVI.equals(model.getActiveTab());
	}
	
	
	// GETTER e SETTER: 														*/

	public String getMethodName() {
		return this.methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
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

	public String getNumeroProvv() {
		return numeroProvv;
	}

	public void setNumeroProvv(String numeroProvv) {
		this.numeroProvv = numeroProvv;
	}

	public String getAnnoProvv() {
		return annoProvv;
	}

	public void setAnnoProvv(String annoProvv) {
		this.annoProvv = annoProvv;
	}

	public String getTipoProvv() {
		return tipoProvv;
	}

	public void setTipoProvv(String tipoProvv) {
		this.tipoProvv = tipoProvv;
	}

}

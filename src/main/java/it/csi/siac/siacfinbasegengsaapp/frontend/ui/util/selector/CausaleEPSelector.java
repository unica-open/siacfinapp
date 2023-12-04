/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.util.selector;

import java.util.Collection;

import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacgenser.model.CausaleEP;

/**
 * Selettore per la causale EP.
 * <br/>
 * L'algoritmo di filtro varia a seconda dell'ambito.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/10/2015
 *
 */
public abstract class CausaleEPSelector {
	
	protected final LogUtil log = new LogUtil(getClass());
	
	
//	protected ElementoPianoDeiConti elementoPianoDeiConti;
//	protected Soggetto soggetto;
	
//	/**
//	 * Costruttore.
//	 * @param elementoPianoDeiConti l'elemento del piano dei conti da utilizzare
//	 * @param soggetto              il soggetto da utilizzare
//	 */
//	protected CausaleEPSelector(ElementoPianoDeiConti elementoPianoDeiConti, Soggetto soggetto) {
//		this.elementoPianoDeiConti = elementoPianoDeiConti;
//		this.soggetto = soggetto;
//	}
	
	/**
	 * Seleziona una causale EP all'interno dell'elenco fornito.
	 * 
	 * @param causali le causali da cui ottenere la causale EP
	 * 
	 * @return la causale EP selezionata
	 */
	public abstract CausaleEP selezionaCausaleEP(Collection<CausaleEP> causali);

	/**
	 * Seleziona la causale di default nel caso in cui rimangano causali EP.
	 * 
	 * @param causali le causali da cui ottenere la causale EP di default
	 * @return la causale di default
	 */
	protected CausaleEP selezionaCausaleDefault(Iterable<CausaleEP> causali) {
		for(CausaleEP cep : causali) {
			if(Boolean.TRUE.equals(cep.getCausaleDiDefault())) {
				return cep;
			}
		}
		return null;
	}

}

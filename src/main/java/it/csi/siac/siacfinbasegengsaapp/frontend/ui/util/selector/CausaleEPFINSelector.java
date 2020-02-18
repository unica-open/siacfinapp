/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.util.selector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.CausaleEP;

/**
 * Selettore per la causale EP per il modulo FIN.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/10/2015
 *
 */
public class CausaleEPFINSelector extends CausaleEPSelector {
	
	private final ElementoPianoDeiConti elementoPianoDeiConti;
	private final Soggetto soggetto;
	
	/**
	 * Costruttore.
	 * @param elementoPianoDeiConti l'elemento del piano dei conti da utilizzare
	 * @param soggetto              il soggetto da utilizzare
	 */
	public CausaleEPFINSelector(ElementoPianoDeiConti elementoPianoDeiConti, Soggetto soggetto) {
		this.elementoPianoDeiConti = elementoPianoDeiConti;
		this.soggetto = soggetto;
	}

	/**
	 * Il sistema in automatico dovr&agrave; proporre la lista delle causali individuate attraverso questo algoritmo.
	 * <br/>
	 * Nella maggior parte dei casi l'algoritmo permetter&agrave; di individuare una sola causale.
	 * <ul>
	 *     <li>
	 *         A partire dal TipoEvento e dall'Evento associato alla richiesta di registrazione selezionare l'elenco delle causali integrate (TipoCausale = <code>I</code> )
	 *         possibili (sono ammesse causali  in stato <code>V-Valido</code> e attive nell'anno di bilancio su cui si sta lavorando);
	 *     </li>
	 *     <li>
	 *         Dopodich&eacute; in base al V livello del Piano dei Conti Finanziario associato al Movimento Finanziario coinvolto (ad es. impegno, accertamento, &hellip;)
	 *         si prendono in considerazione quelle collegate alle stesso conto finanziario. Per le quote documento si fa riferimento al piano dei conti finanziario del movimento gestione
	 *         ad esse collegato, quindi impegno o accertamento ad es.).
	 *     </li>
	 *     <li>
	 *         Se il V livello del piano dei conti finanziario non &eacute; sufficiente per determinare univocamente una causale &eacute; necessario verificare anche i suoi eventuali classificatori collegati,
	 *         ed infine l'eventuale soggetto abbinato. Oppure si controllano gli altri classificatori previsti per tutti gli enti.
	 *     </li>
	 * </li>
	 * Trovata univocamente la causale, se questa &eacute; fornita di un soggetto, occorre verificare che sia coerente con il soggetto del movimento finanziario associato alla registrazione.
	 * Se sono diversi visualizzare il seguente messaggio <code>&lt;GEN_ERR_0014&gt;</code>
	 */
	@Override
	public CausaleEP selezionaCausaleEP(Collection<CausaleEP> causali) {
		final String methodName = "selezionaCausaleEP";
		List<CausaleEP> listaCausaleEP = new ArrayList<CausaleEP>(causali);
		
		// Se ho un'unica causale, la prendo
		if(listaCausaleEP.size() == 1) {
			log.debug(methodName, "Un'unica causale era presente. La seleziono subito");
			return listaCausaleEP.get(0);
		}
		
		// Prendo il movimento di gestione
		if(elementoPianoDeiConti == null) {
			log.debug(methodName, "Nessun elemento del piano dei conti fornito");
			return selezionaCausaleDefault(listaCausaleEP);
		}
		
		log.debug(methodName, "Uid elemento del piano dei conti per cui filtrare: " + elementoPianoDeiConti.getUid());
		
		for(Iterator<CausaleEP> it = listaCausaleEP.iterator(); it.hasNext();) {
			CausaleEP causaleEP = it.next();
			if(causaleEP.getElementoPianoDeiConti() == null || causaleEP.getElementoPianoDeiConti().getUid() != elementoPianoDeiConti.getUid()) {
				it.remove();
			}
		}
		
		if(listaCausaleEP.isEmpty()) {
			log.debug(methodName, "Nessuna causale EP disponibile dopo il filtro per elemento piano dei conti (uid: " + elementoPianoDeiConti.getUid() + ")");
			return null;
		}
		// Se ho un'unica causale, la prendo
		if(listaCausaleEP.size() == 1) {
			log.debug(methodName, "Un'unica causale collegata all'elemento del piano dei conti con uid " + elementoPianoDeiConti.getUid());
			return listaCausaleEP.get(0);
		}

		// TODO: da non sviluppare in V1
		// Controllo classificatori
		
		// Controllo soggetto associato
		if(soggetto == null) {
			log.debug(methodName, "Nessun soggetto fornito");
			return selezionaCausaleDefault(listaCausaleEP);
		}
		
		log.debug(methodName, "Uid soggetto per cui filtrare: " + soggetto.getUid());
		
		for(Iterator<CausaleEP> it = listaCausaleEP.iterator(); it.hasNext();) {
			CausaleEP causaleEP = it.next();
			if(causaleEP.getSoggetto() == null || causaleEP.getSoggetto().getUid() != soggetto.getUid()) {
				it.remove();
			}
		}
		
		if(listaCausaleEP.isEmpty()) {
			log.debug(methodName, "Nessuna causale EP disponibile dopo il filtro per soggetto (uid: " + soggetto.getUid() + ")");
			return null;
		}
		// Se ho un'unica causale, la prendo
		if(listaCausaleEP.size() == 1) {
			log.debug(methodName, "Un'unica causale collegata al soggetto con uid " + soggetto.getUid());
			return listaCausaleEP.get(0);
		}
		
		// Lotto N: presento la causale di default
		return selezionaCausaleDefault(listaCausaleEP);
	}


}

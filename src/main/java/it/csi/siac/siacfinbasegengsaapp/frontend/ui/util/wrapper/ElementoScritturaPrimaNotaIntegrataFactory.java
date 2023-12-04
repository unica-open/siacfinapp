/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.util.wrapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneUtilizzoImporto;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Factory per Elemento delle scritture per lo step 1 della PrimaNotaIntegrata
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 28/04/2015
 *
 */
public final class ElementoScritturaPrimaNotaIntegrataFactory {
	/** Non instanziare la classe */
	private ElementoScritturaPrimaNotaIntegrataFactory() {
	}
	
	/**
	 * Creazione della lista di scritture necessarie alla prima nota ricavate da causaleEP
	 * 
	 * @param causaleEP CausaleEp da cui ricavare i conti
	 * @param importo Importo da associare ai conti da cui ricavare i conti
	 * 
	 * @return la lista delle scritture
	 */
	public static List<ElementoScritturaPrimaNotaIntegrata> creaListaScrittureDaCausaleEP(CausaleEP causaleEP, BigDecimal importo) {
		List<ElementoScritturaPrimaNotaIntegrata> listaScritture = new ArrayList<ElementoScritturaPrimaNotaIntegrata>();
		List<ContoTipoOperazione> listaCTOp = causaleEP.getContiTipoOperazione();
		int numeroRiga = 0;
		for (ContoTipoOperazione cTOp : listaCTOp) {
			MovimentoDettaglio movDettaglio = new MovimentoDettaglio();
			if (cTOp.getOperazioneSegnoConto() != null) {
				movDettaglio.setNumeroRiga(Integer.valueOf(numeroRiga));
				movDettaglio.setConto(cTOp.getConto());
				movDettaglio.setSegno(cTOp.getOperazioneSegnoConto());
				movDettaglio.setImporto(importo);
			}
			// nella modalita aggiornamento metto false perche' al momento il valore e' preso da causale e non dal campo di comodo
			listaScritture.add(new ElementoScritturaPrimaNotaIntegrata(movDettaglio, cTOp, false));
			numeroRiga++;
		}
		return listaScritture;
	}
	
	/**
	 * Creazione della lista di scritture necessarie alla prima nota ricavate da causaleEP
	 * 
	 * @param causaleEP CausaleEp da cui ricavare i conti
	 * @param importo Importo da associare ai conti da cui ricavare i conti
	 * @param conti conti trovati per sostituzione automatica della classe di concilaizione
	 * 
	 * @return la lista delle scritture
	 */
	public static List<ElementoScritturaPrimaNotaIntegrata> creaListaScrittureDaCausaleEP(CausaleEP causaleEP, BigDecimal importo, List<Conto> conti) {
		List<ElementoScritturaPrimaNotaIntegrata> listaScritture = new ArrayList<ElementoScritturaPrimaNotaIntegrata>();
		List<ContoTipoOperazione> listaCTOp = causaleEP.getContiTipoOperazione();
		int numeroRiga = 0;
		for (ContoTipoOperazione cTOp : listaCTOp) {
			Boolean daClasseConciliazione = null;
			if(cTOp.getConto() == null || cTOp.getConto().getUid() == 0){
				daClasseConciliazione = Boolean.TRUE;
			}
			if(cTOp.getConto() == null && conti != null && conti.size() == 1){
				cTOp.setConto(conti.get(0));
			}
			MovimentoDettaglio movDettaglio = new MovimentoDettaglio();
			if (cTOp.getOperazioneSegnoConto() != null) {
				movDettaglio.setNumeroRiga(Integer.valueOf(numeroRiga));
				movDettaglio.setConto(cTOp.getConto());
				movDettaglio.setSegno(cTOp.getOperazioneSegnoConto());
				movDettaglio.setImporto(importo);
			}
			// nella modalita aggiornamento metto false perche' al momento il valore e' preso da causale e non dal campo di comodo
			ElementoScritturaPrimaNotaIntegrata nuovoElemento = new ElementoScritturaPrimaNotaIntegrata(movDettaglio, cTOp, false);
			nuovoElemento.setDaClasseConciliazione(daClasseConciliazione);
			listaScritture.add(nuovoElemento);
			numeroRiga++;
		}
		return listaScritture;
	}
	
	/**
	 * Creazione della lista di scritture necessarie alla prima nota ricavate da causaleEP
	 * 
	 * @param causaleEP CausaleEp da cui ricavare i conti
	 * @param importo Importo da associare ai conti da cui ricavare i conti
	 * @param mappaConti conti trovati per sostituzione automatica della classe di concilaizione
	 * 
	 * @return la lista delle scritture
	 */
	public static List<ElementoScritturaPrimaNotaIntegrata> creaListaScrittureDaCausaleEPEContiConciliazione(CausaleEP causaleEP, BigDecimal importo, Map<ClasseDiConciliazione,List<Conto>> mappaConti) {
		List<ElementoScritturaPrimaNotaIntegrata> listaScritture = new ArrayList<ElementoScritturaPrimaNotaIntegrata>();
		List<ContoTipoOperazione> listaCTOp = causaleEP.getContiTipoOperazione();
		int numeroRiga = 0;
		for (ContoTipoOperazione cTOp : listaCTOp) {
			Boolean daClasseConciliazione = null;
			List<Conto> conti = new ArrayList<Conto>();
			if(cTOp.getClasseDiConciliazione() !=null){
				daClasseConciliazione = Boolean.TRUE;
				conti = mappaConti.get(cTOp.getClasseDiConciliazione());
				if(cTOp.getConto() == null && conti != null && conti.size() == 1){
					cTOp.setConto(conti.get(0));
				}
			}
			
			MovimentoDettaglio movDettaglio = new MovimentoDettaglio();
			if (cTOp.getOperazioneSegnoConto() != null) {
				movDettaglio.setNumeroRiga(Integer.valueOf(numeroRiga));
				movDettaglio.setConto(cTOp.getConto());
				movDettaglio.setSegno(cTOp.getOperazioneSegnoConto());
				movDettaglio.setImporto(importo);
			}
			// nella modalita aggiornamento metto false perche' al momento il valore e' preso da causale e non dal campo di comodo
			ElementoScritturaPrimaNotaIntegrata nuovoElemento = new ElementoScritturaPrimaNotaIntegrata(movDettaglio, cTOp, false,conti);
			nuovoElemento.setDaClasseConciliazione(daClasseConciliazione);
			listaScritture.add(nuovoElemento);
			numeroRiga++;
		}
		return listaScritture;
	}


	/**
	 * Creazione della lista di scritture necessarie alla prima nota ricavate da causaleEP
	 * 
	 * @param causaleEP CausaleEp da cui ricavare i conti
	 * @param regMovFin la registrazione del movimento finanziario
	 * 
	 * @return la lista delle scritture
	 */
	public static List<ElementoScritturaPrimaNotaIntegrata> creaListaScrittureDaCausaleEP(CausaleEP causaleEP, RegistrazioneMovFin regMovFin) {
		List<ElementoScritturaPrimaNotaIntegrata> listaScritture = new ArrayList<ElementoScritturaPrimaNotaIntegrata>();
		List<ContoTipoOperazione> listaCTOp = causaleEP.getContiTipoOperazione();
		int numeroRiga = 0;
		for (ContoTipoOperazione cTOp : listaCTOp) {
			MovimentoDettaglio movDettaglio = new MovimentoDettaglio();
			
			if (cTOp.getOperazioneSegnoConto() != null) {
				movDettaglio.setNumeroRiga(Integer.valueOf(numeroRiga));
				movDettaglio.setConto(cTOp.getConto());
				movDettaglio.setSegno(cTOp.getOperazioneSegnoConto());
				// TODO in integrata l'importo dipende dal tipo movimento.
			}
			// nella modalita aggiornamento metto false perche' al momento il valore e' preso da causale e non dal campo di comodo
			listaScritture.add(new ElementoScritturaPrimaNotaIntegrata(movDettaglio, cTOp, false));
			numeroRiga++;
		}
		return listaScritture;
	}
	
	/**
	 * Creazione dell'elemento di scruitttura manuale
	 * 
	 * @param cTOp ContoTipoOperazione per la scrittura
	 * @param movDettaglio MovimentoDettaglio per la scrittura
	 * 
	 * 
	 * @return la scrittura
	 */
	public static ElementoScritturaPrimaNotaIntegrata creaElementoScritturaManuale(ContoTipoOperazione cTOp, MovimentoDettaglio movDettaglio) {
		return new ElementoScritturaPrimaNotaIntegrata(movDettaglio, cTOp, true);
	}

	/**
	 * Creazione della lista di scritture necessarie alla prima nota ricavate da primanotaIntegrata se aggiornamento
	 * 
	 * @param primaNotaIntegrata PrimaNota da cui ricavare i conti
	 * @param contiCausale boolean che indica se i conti sono da causale
	 * 
	 * @return la lista delle scritture
	 */
	public static List<ElementoScritturaPrimaNotaIntegrata> creaListaScrittureDaPrimaNota(PrimaNota primaNotaIntegrata, boolean contiCausale) {
		List<ElementoScritturaPrimaNotaIntegrata> listaScritture = new ArrayList<ElementoScritturaPrimaNotaIntegrata>();
		List<MovimentoEP> listaMovEp = primaNotaIntegrata.getListaMovimentiEP();
		if (listaMovEp != null) {
			for (MovimentoEP movEP : listaMovEp) {
				for (MovimentoDettaglio movDettaglio : movEP.getListaMovimentoDettaglio()) {
						
					if (movDettaglio != null) {
			
						ContoTipoOperazione cTOp = new ContoTipoOperazione();
						cTOp.setOperazioneSegnoConto(movDettaglio.getSegno());
						cTOp.setOperazioneUtilizzoImporto(OperazioneUtilizzoImporto.PROPOSTO);
						// nella modalita aggiornamento metto false perche' al momento il valore e' preso da causale e non dal campo di comodo
						listaScritture.add(new ElementoScritturaPrimaNotaIntegrata(movDettaglio, cTOp, !contiCausale));
					}
				}
			}
		}
		return listaScritture;
	}
	
	/**
	 * Creazione della lista di scritture necessarie alla prima nota ricavate da primanotaIntegrata senza considerare se i conti siano o meno derivati da una causale
	 * 
	 * @param primaNotaIntegrata PrimaNota da cui ricavare i conti
	 * 
	 * @return la lista delle scritture
	 */
	public static List<ElementoScritturaPrimaNotaIntegrata> creaListaScrittureDaPrimaNota(PrimaNota primaNotaIntegrata) {
		return creaListaScrittureDaPrimaNota(primaNotaIntegrata,false);
	}
	
	/**
	 * Creazione della lista di scritture a partire dal singolo movimento EP
	 * @param movimentoEP il movimento EP
	 * @param contiCausale se la causale ha conti
	 * @return la lista di scritture
	 */
	public static List<ElementoScritturaPrimaNotaIntegrata> creaListaScrittureDaSingoloMovimentoEP(MovimentoEP movimentoEP, Boolean contiCausale) {
		List<ElementoScritturaPrimaNotaIntegrata> listaScritture = new ArrayList<ElementoScritturaPrimaNotaIntegrata>();
		if(movimentoEP != null){
			for (MovimentoDettaglio movDettaglio : movimentoEP.getListaMovimentoDettaglio()) {
					
				if (movDettaglio != null) {
		
					ContoTipoOperazione cTOp = new ContoTipoOperazione();
					cTOp.setOperazioneSegnoConto(movDettaglio.getSegno());
					cTOp.setOperazioneUtilizzoImporto(OperazioneUtilizzoImporto.PROPOSTO);
					// nella modalita aggiornamento metto false perche' al momento il valore e' preso da causale e non dal campo di comodo
					listaScritture.add(new ElementoScritturaPrimaNotaIntegrata(movDettaglio, cTOp, !Boolean.TRUE.equals(contiCausale)));
				}
			}
		}
		return listaScritture;
	}
}

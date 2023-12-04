/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinapp.frontend.ui.action.movgest.MotiviCancellazioneAccROR;
import it.csi.siac.siacfinapp.frontend.ui.action.movgest.MotiviCancellazioneROR;
import it.csi.siac.siacfinapp.frontend.ui.action.movgest.MotiviMantenimentoAccROR;
import it.csi.siac.siacfinapp.frontend.ui.action.movgest.MotiviMantenimentoROR;
import it.csi.siac.siacfinapp.frontend.ui.action.movgest.MotiviReimputazioneAccROR;
import it.csi.siac.siacfinapp.frontend.ui.action.movgest.MotiviReimputazioneROR;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestioneCruscottoModel;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.GestisciImpegnoStep1Model;
import it.csi.siac.siacfinapp.frontend.ui.model.movgest.ProvvedimentoImpegnoModel;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione.StatoOperativoModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;

/***
 * 07/02/2020* Ricevuto M. L.
 */

// Classe statica con metodi di utilità per la gestione del cruscotto
public final class CruscottoRorUtilities {

	// Descrizione del motivo
	public static CodificaFin getDescrizioneMotivo(List<CodificaFin> listaTipoMotivo, String motivo) {
		for (CodificaFin codifica : listaTipoMotivo) {
			if (codifica.getCodice().equals(motivo)) {
				return codifica;
			}
		}
		return null;
	}

	public static BigDecimal calcolaImportoGestito(List<ModificaMovimentoGestioneSpesa> modificheList) {
		//SIAC-8007 -Inizio   sostituisco int con BigDecimal
		BigDecimal importoGestito = BigDecimal.ZERO;
		//int importoGestito = 0;
		if (modificheList != null) {
			for (ModificaMovimentoGestioneSpesa mmgs : modificheList) {
				String motivoModifica = mmgs.getTipoModificaMovimentoGestione();
				// FIXME per le cancellazioni
				if (motivoModifica.equals("REIMP") || motivoModifica.equals("RORM") || motivoModifica.equals("INEROR")
						|| motivoModifica.equals("INSROR")) {
					//int importo = mmgs.getImportoOld().intValue();
					//importoGestito = importoGestito + importo;
					importoGestito =  importoGestito.add(mmgs.getImportoOld());
				}
			}

		}
		//return new BigDecimal(importoGestito);
		return  importoGestito;
	}

	public static BigDecimal calcolaImportoGestitoAcc(List<ModificaMovimentoGestioneEntrata> modificheList) {

		//SIAC-8007 -Inizio   sostituisco int con BigDecimal
		BigDecimal importoGestito = BigDecimal.ZERO;
		//int importoGestito = 0;
		if (modificheList != null) {
			for (ModificaMovimentoGestioneEntrata mmgs : modificheList) {
				String motivoModifica = mmgs.getTipoModificaMovimentoGestione();
				// FIXME per le cancellazioni
				if (motivoModifica.equals("REIMP") || motivoModifica.equals("RORM") || motivoModifica.equals("INEROR")
						|| motivoModifica.equals("INSROR")) {
					//int importo = mmgs.getImportoOld().intValue();
					//importoGestito = importoGestito + importo;
					importoGestito =  importoGestito.add(mmgs.getImportoOld());
				}
			}

		}
		//return new BigDecimal(importoGestito);
		return importoGestito;
	}

	
	//SIAC-7349 Inizio SR190 CM 05/05/2020 Escludere dalla lista delle modifiche da annullare quelle che hanno modifiche collegate e tornare un errore
	public static List<Errore> checkModificheDaAnnullareModificheCollegate(
			List<ModificaMovimentoGestioneSpesa> modificheDaAnnullare, List<ModificaMovimentoGestioneSpesa> listaModificheOld, List<Errore> errori) {
		
		if(modificheDaAnnullare != null && modificheDaAnnullare.size()>0 && listaModificheOld != null && listaModificheOld.size()>0) {
			
			List<ModificaMovimentoGestioneSpesaCollegata> listaModificheCollegate = new ArrayList<ModificaMovimentoGestioneSpesaCollegata>();
			
			for(int i=0; i<modificheDaAnnullare.size(); i++){
				for(int j=0; j<listaModificheOld.size(); j++){
					if(modificheDaAnnullare.get(i).getUid()==(listaModificheOld.get(j).getUid())){
						
						listaModificheCollegate = listaModificheOld.get(j).getListaModificheMovimentoGestioneSpesaCollegata();
						
						if (listaModificheCollegate != null) {
							//torna errore
							Errore err = new Errore();
							err.setCodice(ErroreCore.ANNULLAMENTO_NON_POSSIBILE.getErrore("").getCodice());
							err.setDescrizione(
									"La reimputazione di spesa dell'anno " + modificheDaAnnullare.get(i).getAnnoReimputazione()
											+ " non puo' essere annullata in quanto presenta delle modifiche ad essa collegate.");
							errori.add(err);
							
						}
					}
				}
			}
			
		}
		return errori;
	}
	
	//Verifica se sono state aggiornate modifiche che hanno modifiche ad esse collegate
	public static List<Errore>  checkMovimentiDaAggiornareModificheCollegate(
			List<ModificaMovimentoGestioneSpesa> modifichePreesistentiAggiornate,
			List<ModificaMovimentoGestioneSpesa> listaModificheOld, List<Errore> errori) {
			
		BigDecimal importoNullo = new BigDecimal("0.00");
	
		if(modifichePreesistentiAggiornate != null && listaModificheOld != null 
				&& modifichePreesistentiAggiornate.size()>0 && listaModificheOld.size()>0
					&& modifichePreesistentiAggiornate.size() == listaModificheOld.size()) {
			
			List<ModificaMovimentoGestioneSpesaCollegata> listaModificheCollegate = new ArrayList<ModificaMovimentoGestioneSpesaCollegata>();
			
			for (int i = 0; i < modifichePreesistentiAggiornate.size(); i++) {
				for(int j=0; j<listaModificheOld.size(); j++){
				
					if(modifichePreesistentiAggiornate.get(i).getUid()==(listaModificheOld.get(j).getUid())){
						
						BigDecimal importoSpesa = modifichePreesistentiAggiornate.get(i).getImportoOld() == null ? new BigDecimal("0.00")
								: modifichePreesistentiAggiornate.get(i).getImportoOld();

						boolean cambiatoImporto = importoSpesa
								.compareTo(listaModificheOld.get(j).getImportoOld().abs()) != 0; //.abs()

						if (cambiatoImporto) {
							
							listaModificheCollegate = listaModificheOld.get(j).getListaModificheMovimentoGestioneSpesaCollegata();
							
							if (listaModificheCollegate != null) {
								Errore err = new Errore();
								err.setCodice(ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("").getCodice());
								err.setDescrizione(
										"La reimputazione di spesa dell'anno " + modifichePreesistentiAggiornate.get(i).getAnnoReimputazione()
												+ " non puo' essere modificata in quanto presenta delle modifiche ad essa collegate.");
								errori.add(err);
								
							}
		
						}
						
					}
					
				}

			}
		}

		return errori;
	}
	
	//SIAC-7349 Fine
	
	public static List<Errore> checkAnniReimputazioneRipetuti(List<GestioneCruscottoModel> listaReimputazioneTriennio2,
			List<Errore> errori) {
		List<Integer> anni = new ArrayList<Integer>();
		for (int i = 0; i < listaReimputazioneTriennio2.size(); i++) {
			if (listaReimputazioneTriennio2.get(i).getAnno() != null) {
				anni.add(listaReimputazioneTriennio2.get(i).getAnno());
			}
		}
		if (listaReimputazioneTriennio2.size() > 0) {
			Set<Integer> set = new HashSet<Integer>(anni);
			if (set.size() < anni.size()) {
				Errore err = new Errore();
				err.setCodice(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("").getCodice());
				err.setDescrizione("Sono state inserite pi&ugrave; reimputazioni per lo stesso anno");
				errori.add(err);

			}
		}
		return errori;
	}

	public static List<Errore> checkAnniReimputazioneRipetutiInAgg(
			List<ModificaMovimentoGestioneSpesa> listaReimputazioneTriennio2, List<Errore> errori) {
		List<Integer> anni = new ArrayList<Integer>();
		for (int i = 0; i < listaReimputazioneTriennio2.size(); i++) {
			if (listaReimputazioneTriennio2.get(i).getAnnoReimputazione() != null) {
				anni.add(listaReimputazioneTriennio2.get(i).getAnnoReimputazione());
			}
		}
		if (listaReimputazioneTriennio2.size() > 0) {
			Set<Integer> set = new HashSet<Integer>(anni);
			if (set.size() < anni.size()) {
				Errore err = new Errore();
				err.setCodice(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("").getCodice());
				err.setDescrizione("Sono state inserite pi&ugrave; reimputazioni per lo stesso anno");
				errori.add(err);

			}
		}
		return errori;
	}

	public static List<Errore> checkAnniReimputazioneRipetutiInAggAcc(
			List<ModificaMovimentoGestioneEntrata> listaReimputazioneTriennio2, List<Errore> errori) {
		List<Integer> anni = new ArrayList<Integer>();
		for (int i = 0; i < listaReimputazioneTriennio2.size(); i++) {
			if (listaReimputazioneTriennio2.get(i).getAnnoReimputazione() != null) {
				anni.add(listaReimputazioneTriennio2.get(i).getAnnoReimputazione());
			}
		}
		if (listaReimputazioneTriennio2.size() > 0) {
			Set<Integer> set = new HashSet<Integer>(anni);
			if (set.size() < anni.size()) {
				Errore err = new Errore();
				err.setCodice(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("").getCodice());
				err.setDescrizione("Sono state inserite pi&ugrave; reimputazioni per lo stesso anno");
				errori.add(err);

			}
		}
		return errori;
	}

	// Controlla che gli anni non siano minori di n+1. Devono rispettare la
	// condizione che anno>n+2, condizione gia implicita nella definizione del
	// triennio di reimputazione
	public static List<Errore> checkAnnoNonCongruente(List<GestioneCruscottoModel> listaReimputazioneTriennio2,
			List<Errore> errori, int annoBilancio) {
		for (int i = 0; i < listaReimputazioneTriennio2.size(); i++) {
			if (listaReimputazioneTriennio2.get(i).getAnno() != null
					&& (listaReimputazioneTriennio2.get(i).getAnno() < (annoBilancio + 1))) {
				Errore err = new Errore();
				err.setCodice(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("").getCodice());
				err.setDescrizione("L'anno di reimputazione " + listaReimputazioneTriennio2.get(i).getAnno()
						+ " non rispetta la condizione necessaria per l'inserimento");
				errori.add(err);
			}

		}
		return errori;
	}

	public static List<Errore> checkAnnoNonCongruenteInAgg(
			List<ModificaMovimentoGestioneSpesa> listaReimputazioneTriennio2, List<Errore> errori, int annoBilancio) {
		for (int i = 0; i < listaReimputazioneTriennio2.size(); i++) {
			if (listaReimputazioneTriennio2.get(i).getAnnoReimputazione() != null
					&& (listaReimputazioneTriennio2.get(i).getAnnoReimputazione() < (annoBilancio + 1))) {
				Errore err = new Errore();
				err.setCodice(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("").getCodice());
				err.setDescrizione(
						"L'anno di reimputazione " + listaReimputazioneTriennio2.get(i).getAnnoReimputazione()
								+ " non rispetta la condizione necessaria per l'inserimento");
				errori.add(err);
			}

		}
		return errori;
	}

	public static List<Errore> checkAnnoNonCongruenteInAggAcc(
			List<ModificaMovimentoGestioneEntrata> listaReimputazioneTriennio2, List<Errore> errori, int annoBilancio) {
		for (int i = 0; i < listaReimputazioneTriennio2.size(); i++) {
			if (listaReimputazioneTriennio2.get(i).getAnnoReimputazione() != null
					&& (listaReimputazioneTriennio2.get(i).getAnnoReimputazione() < (annoBilancio + 1))) {
				Errore err = new Errore();
				err.setCodice(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("").getCodice());
				err.setDescrizione(
						"L'anno di reimputazione " + listaReimputazioneTriennio2.get(i).getAnnoReimputazione()
								+ " non rispetta la condizione necessaria per l'inserimento");
				errori.add(err);
			}

		}
		return errori;
	}

	// non è possibile salvare una descrizione con una lunghezza maggiore a 500;
	// tronco la descrizione a 488.
	public static String troncaDescrizione(String descrizione2) {
		if (descrizione2.length() < 499) {
			return descrizione2;
		} else {
			return descrizione2.substring(0, 498);
		}
	}

	public static ProvvedimentoImpegnoModel mapAttoToProvv(AttoAmministrativo attoAmministrativo) {
		//GESTIONE NULL 7502 
		ProvvedimentoImpegnoModel pim = new ProvvedimentoImpegnoModel();
		pim.setOggetto(attoAmministrativo.getOggetto() == null ? "" : attoAmministrativo.getOggetto());
		pim.setAnnoProvvedimento(attoAmministrativo.getAnno());
		pim.setNumeroProvvedimento(BigInteger.valueOf(attoAmministrativo.getNumero()));
		pim.setBloccoRagioneria(attoAmministrativo.getBloccoRagioneria());
		
		String codiceProvvedimento = attoAmministrativo.getTipoAtto() == null ? "" : attoAmministrativo.getTipoAtto().getCodice();
		pim.setCodiceTipoProvvedimento(codiceProvvedimento);
		
		int codiceTipoProvvedimento = attoAmministrativo.getTipoAtto()== null ? 0 :  attoAmministrativo.getTipoAtto().getUid();
		pim.setIdTipoProvvedimento(codiceTipoProvvedimento);
		
		String descTipo = attoAmministrativo.getTipoAtto()==null ? "" : attoAmministrativo.getTipoAtto().getDescrizione();
		pim.setTipoProvvedimento(descTipo);
		
		Integer uidStrutturaProvvedimento = (attoAmministrativo.getStrutturaAmmContabile() == null) ? 0 : (attoAmministrativo.getStrutturaAmmContabile().getUid());
		pim.setUidStrutturaAmm(uidStrutturaProvvedimento);//FIXME arriva null a volte
		
		String titolo = attoAmministrativo.getOggetto()==null ? "" : attoAmministrativo.getOggetto();
		pim.setTitolo(titolo);
		
		String descStrutt = attoAmministrativo.getStrutturaAmmContabile() == null ? "" : attoAmministrativo.getStrutturaAmmContabile().getDescrizione();
		pim.setStrutturaAmministrativa(descStrutt);
		
		String codeStrutt = attoAmministrativo.getStrutturaAmmContabile() == null ? "" : attoAmministrativo.getStrutturaAmmContabile().getCodice();
		pim.setCodiceStrutturaAmministrativa(codeStrutt);
		
		String statoOp = attoAmministrativo.getStatoOperativo()==null ? "" : attoAmministrativo.getStatoOperativo();
		pim.setStato(statoOp);
		
		pim.setUid(attoAmministrativo.getUid());
		return pim;
	}

	// Questo metodo permette di filtrare le modifiche VALIDE, ovvero quelle che
	// non sono state annullate
	public static List<ModificaMovimentoGestioneSpesa> getModificheStatoValido(
			List<ModificaMovimentoGestioneSpesa> modificheList) {
		List<ModificaMovimentoGestioneSpesa> modificheValide = new ArrayList<ModificaMovimentoGestioneSpesa>();
		for (int i = 0; i < modificheList.size(); i++) {
			if (modificheList.get(i).getCodiceStatoOperativoModificaMovimentoGestione() == null
					|| modificheList.get(i).getCodiceStatoOperativoModificaMovimentoGestione().equals("V")) {
				modificheValide.add(modificheList.get(i));
			}

		}
		return modificheValide;
	}

	public static List<ModificaMovimentoGestioneEntrata> getModificheStatoValidoAcc(
			List<ModificaMovimentoGestioneEntrata> modificheList) {
		List<ModificaMovimentoGestioneEntrata> modificheValide = new ArrayList<ModificaMovimentoGestioneEntrata>();
		for (int i = 0; i < modificheList.size(); i++) {
			if (modificheList.get(i).getCodiceStatoOperativoModificaMovimentoGestione() == null
					|| modificheList.get(i).getCodiceStatoOperativoModificaMovimentoGestione().equals("V")) {
				modificheValide.add(modificheList.get(i));
			}

		}
		return modificheValide;
	}

	public static List<ModificaMovimentoGestioneEntrata> getModificheStatoValidoAccControllo(
			List<ModificaMovimentoGestioneEntrata> modificheList) {
		List<ModificaMovimentoGestioneEntrata> modificheValide = new ArrayList<ModificaMovimentoGestioneEntrata>();
		for (int i = 0; i < modificheList.size(); i++) {
			if (modificheList.get(i).getStatoOperativoModificaMovimentoGestione() == null
					|| modificheList.get(i).getStatoOperativoModificaMovimentoGestione()
							.equals(StatoOperativoModificaMovimentoGestione.VALIDO)) {
				modificheValide.add(modificheList.get(i));
			}

		}
		return modificheValide;
	}

	public static List<ModificaMovimentoGestioneSpesa> getModificheStatoValidoImpControllo(
			List<ModificaMovimentoGestioneSpesa> modificheList) {
		List<ModificaMovimentoGestioneSpesa> modificheValide = new ArrayList<ModificaMovimentoGestioneSpesa>();
		for (int i = 0; i < modificheList.size(); i++) {
			if (modificheList.get(i).getStatoOperativoModificaMovimentoGestione() == null
					|| modificheList.get(i).getStatoOperativoModificaMovimentoGestione()
							.equals(StatoOperativoModificaMovimentoGestione.VALIDO)) {
				modificheValide.add(modificheList.get(i));
			}

		}
		return modificheValide;
	}

	public static List<ModificaMovimentoGestioneSpesa> getModifichePerCruscotto(
			List<ModificaMovimentoGestioneSpesa> modificheOld,
			List<ModificaMovimentoGestioneSpesa> modifichePerCruscotto) {
		for (int i = 0; i < modificheOld.size(); i++) {
			if (modificheOld.get(i).getTipoModificaMovimentoGestione() != null && ((modificheOld.get(i).getTipoModificaMovimentoGestione().equals("REIMP")
					&& modificheOld.get(i).isReimputazione() == true)
					|| modificheOld.get(i).getTipoModificaMovimentoGestione().equals("INSROR")
					|| modificheOld.get(i).getTipoModificaMovimentoGestione().equals("INEROR")
					|| modificheOld.get(i).getTipoModificaMovimentoGestione().equals("RORM"))) {
				modifichePerCruscotto.add(modificheOld.get(i));

			}

		}
		return modifichePerCruscotto;
	}

	public static List<ModificaMovimentoGestioneEntrata> getModifichePerCruscottoAcc(
			List<ModificaMovimentoGestioneEntrata> modificheOld,
			List<ModificaMovimentoGestioneEntrata> modifichePerCruscotto) {
		for (int i = 0; i < modificheOld.size(); i++) {
			if (modificheOld.get(i).getTipoModificaMovimentoGestione() != null && ((modificheOld.get(i).getTipoModificaMovimentoGestione().equals("REIMP")
					&& modificheOld.get(i).isReimputazione() == true)
					|| modificheOld.get(i).getTipoModificaMovimentoGestione().equals("INSROR")
					|| modificheOld.get(i).getTipoModificaMovimentoGestione().equals("INEROR")
					|| modificheOld.get(i).getTipoModificaMovimentoGestione().equals("RORM"))) {
				modifichePerCruscotto.add(modificheOld.get(i));

			}

		}
		return modifichePerCruscotto;
	}

	public static boolean checkReimpInModif(List<ModificaMovimentoGestioneSpesa> modifichePerCruscotto) {
		for (int i = 0; i < modifichePerCruscotto.size(); i++) {
			if (modifichePerCruscotto.get(i).getTipoModificaMovimentoGestione().equals("REIMP")) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkReimpInModifAcc(List<ModificaMovimentoGestioneEntrata> modifichePerCruscotto) {
		for (int i = 0; i < modifichePerCruscotto.size(); i++) {
			if (modifichePerCruscotto.get(i).getTipoModificaMovimentoGestione().equals("REIMP")) {
				return true;
			}
		}
		return false;
	}

	public static List<ModificaMovimentoGestioneSpesa> getListaReimputazioniPresenti(
			List<ModificaMovimentoGestioneSpesa> modifichePerCruscotto) {
		List<ModificaMovimentoGestioneSpesa> modificheToReturn = new ArrayList<ModificaMovimentoGestioneSpesa>();
		for (int i = 0; i < modifichePerCruscotto.size(); i++) {
			if (modifichePerCruscotto.get(i).getStatoOperativoModificaMovimentoGestione()
					.equals(StatoOperativoModificaMovimentoGestione.VALIDO)
					&& modifichePerCruscotto.get(i).getTipoModificaMovimentoGestione().equals("REIMP")
					&& modifichePerCruscotto.get(i).isReimputazione() == true) {
				modificheToReturn.add(modifichePerCruscotto.get(i));

			}
		}
		if (!modificheToReturn.isEmpty() && modificheToReturn.size() > 1) {

			Collections.sort(modificheToReturn, new Comparator<ModificaMovimentoGestioneSpesa>() {
				@Override
				public int compare(ModificaMovimentoGestioneSpesa o1, ModificaMovimentoGestioneSpesa o2) {
					if (o1.getAnnoReimputazione() == o2.getAnnoReimputazione())
						return 0;
					return o1.getAnnoReimputazione() < o2.getAnnoReimputazione() ? -1 : 1;
				}
			});
		}
		return modificheToReturn;
	}

	public static List<ModificaMovimentoGestioneEntrata> getListaReimputazioniPresentiAcc(
			List<ModificaMovimentoGestioneEntrata> modifichePerCruscotto) {
		List<ModificaMovimentoGestioneEntrata> modificheToReturn = new ArrayList<ModificaMovimentoGestioneEntrata>();
		for (int i = 0; i < modifichePerCruscotto.size(); i++) {
			if (modifichePerCruscotto.get(i).getStatoOperativoModificaMovimentoGestione()
					.equals(StatoOperativoModificaMovimentoGestione.VALIDO)
					&& modifichePerCruscotto.get(i).getTipoModificaMovimentoGestione().equals("REIMP")) {
				modificheToReturn.add(modifichePerCruscotto.get(i));

			}
		}
		if (!modificheToReturn.isEmpty() && modificheToReturn.size() > 1) {

			Collections.sort(modificheToReturn, new Comparator<ModificaMovimentoGestioneEntrata>() {
				@Override
				public int compare(ModificaMovimentoGestioneEntrata o1, ModificaMovimentoGestioneEntrata o2) {
					if (o1.getAnnoReimputazione() == o2.getAnnoReimputazione())
						return 0;
					return o1.getAnnoReimputazione() < o2.getAnnoReimputazione() ? -1 : 1;
				}
			});
		}
		return modificheToReturn;
	}

	public static List<ModificaMovimentoGestioneSpesa> getListaOtherCruscottoModifichePresenti(
			List<ModificaMovimentoGestioneSpesa> modifichePerCruscotto) {
		List<ModificaMovimentoGestioneSpesa> modificheToReturn = new ArrayList<ModificaMovimentoGestioneSpesa>();
		for (int i = 0; i < modifichePerCruscotto.size(); i++) {
			boolean modificaCruscotto = modifichePerCruscotto.get(i).getTipoModificaMovimentoGestione().equals("INSROR")
					|| modifichePerCruscotto.get(i).getTipoModificaMovimentoGestione().equals("INEROR")
					|| modifichePerCruscotto.get(i).getTipoModificaMovimentoGestione().equals("RORM");
			if (modifichePerCruscotto.get(i).getStatoOperativoModificaMovimentoGestione()
					.equals(StatoOperativoModificaMovimentoGestione.VALIDO) && modificaCruscotto) {
				modificheToReturn.add(modifichePerCruscotto.get(i));

			}
		}

		return modificheToReturn;
	}

	public static List<ModificaMovimentoGestioneEntrata> getListaOtherCruscottoModifichePresentiAcc(
			List<ModificaMovimentoGestioneEntrata> modifichePerCruscotto) {
		List<ModificaMovimentoGestioneEntrata> modificheToReturn = new ArrayList<ModificaMovimentoGestioneEntrata>();
		for (int i = 0; i < modifichePerCruscotto.size(); i++) {
			boolean modificaCruscotto = modifichePerCruscotto.get(i).getTipoModificaMovimentoGestione().equals("INSROR")
					|| modifichePerCruscotto.get(i).getTipoModificaMovimentoGestione().equals("INEROR")
					|| modifichePerCruscotto.get(i).getTipoModificaMovimentoGestione().equals("RORM");
			if (modifichePerCruscotto.get(i).getStatoOperativoModificaMovimentoGestione()
					.equals(StatoOperativoModificaMovimentoGestione.VALIDO) && modificaCruscotto) {
				modificheToReturn.add(modifichePerCruscotto.get(i));

			}
		}

		return modificheToReturn;
	}

	public static List<ModificaMovimentoGestioneSpesa> addReimputazioneInAggiornamento(
			List<ModificaMovimentoGestioneSpesa> modifichePerCruscotto, Integer anno) {
		List<ModificaMovimentoGestioneSpesa> listeNuove = new ArrayList<ModificaMovimentoGestioneSpesa>();
		ModificaMovimentoGestioneSpesa m1 = new ModificaMovimentoGestioneSpesa();
		m1.setTipoModificaMovimentoGestione("REIMP");
		m1.setReimputazione(true);
		m1.setAnnoReimputazione(anno + 1);
		m1.setUid(0);
		ModificaMovimentoGestioneSpesa m2 = new ModificaMovimentoGestioneSpesa();
		m2.setTipoModificaMovimentoGestione("REIMP");
		m2.setReimputazione(true);
		m2.setAnnoReimputazione(anno + 2);
		m2.setUid(0);
		ModificaMovimentoGestioneSpesa m3 = new ModificaMovimentoGestioneSpesa();
		m3.setTipoModificaMovimentoGestione("REIMP");
		m3.setReimputazione(true);
		m3.setAnnoReimputazione(anno + 3);
		m3.setUid(0);
		listeNuove.add(m1);
		listeNuove.add(m2);
		listeNuove.add(m3);
		listeNuove.addAll(modifichePerCruscotto);
		return listeNuove;
	}

	public static List<ModificaMovimentoGestioneEntrata> addReimputazioneInAggiornamentoAcc(
			List<ModificaMovimentoGestioneEntrata> modifichePerCruscotto, Integer anno) {
		List<ModificaMovimentoGestioneEntrata> listeNuove = new ArrayList<ModificaMovimentoGestioneEntrata>();
		ModificaMovimentoGestioneEntrata m1 = new ModificaMovimentoGestioneEntrata();
		m1.setTipoModificaMovimentoGestione("REIMP");
		m1.setReimputazione(true);
		m1.setAnnoReimputazione(anno + 1);
		m1.setUid(0);
		ModificaMovimentoGestioneEntrata m2 = new ModificaMovimentoGestioneEntrata();
		m2.setTipoModificaMovimentoGestione("REIMP");
		m2.setReimputazione(true);
		m2.setAnnoReimputazione(anno + 2);
		m2.setUid(0);
		ModificaMovimentoGestioneEntrata m3 = new ModificaMovimentoGestioneEntrata();
		m3.setTipoModificaMovimentoGestione("REIMP");
		m3.setReimputazione(true);
		m3.setAnnoReimputazione(anno + 3);
		m3.setUid(0);
		listeNuove.add(m1);
		listeNuove.add(m2);
		listeNuove.add(m3);
		listeNuove.addAll(modifichePerCruscotto);
		return listeNuove;
	}

	public static List<ModificaMovimentoGestioneSpesa> modificheDaAnnullare(
			List<ModificaMovimentoGestioneSpesa> listaModificheRor2, Boolean daMantenere) {
		BigDecimal importoNullo = new BigDecimal("0.00");
		if (daMantenere == null)
			daMantenere = false;
		List<ModificaMovimentoGestioneSpesa> modificheDaAnnullare = new ArrayList<ModificaMovimentoGestioneSpesa>();
		for (int i = 0; i < listaModificheRor2.size(); i++) {
			String descrizione = listaModificheRor2.get(i).getDescrizioneModificaMovimentoGestione();

			BigDecimal perProva = listaModificheRor2.get(i).getImportoOld() == null ? new BigDecimal("0.00")
					: listaModificheRor2.get(i).getImportoOld();

			boolean importoZero = perProva.equals(importoNullo);
			if (!listaModificheRor2.get(i).getTipoModificaMovimentoGestione().equals("RORM")
					//&& (descrizione == null || descrizione.equals("")) && importoZero) {
					&& importoZero) {
				modificheDaAnnullare.add(listaModificheRor2.get(i));
			}
			if (listaModificheRor2.get(i).getTipoModificaMovimentoGestione().equals("RORM")
					&& (!daMantenere || (descrizione == null || descrizione.equals("")))) {
				modificheDaAnnullare.add(listaModificheRor2.get(i));
			}
		}
		return modificheDaAnnullare;
	}

	public static List<ModificaMovimentoGestioneSpesa> modificheDaAnnullarePerRicreare(
			List<ModificaMovimentoGestioneSpesa> listaModificheRor2,
			List<ModificaMovimentoGestioneSpesa> listaModificheRorOld, Boolean stessoProvvedimento) {

		if (stessoProvvedimento != null && stessoProvvedimento == false) {
			BigDecimal zero = new BigDecimal("0.00");
			List<ModificaMovimentoGestioneSpesa> modificheDaAnnullare = new ArrayList<ModificaMovimentoGestioneSpesa>();
			for (int i = 0; i < listaModificheRor2.size(); i++) {
				
				if (listaModificheRor2.get(i).getImportoOld() != null && listaModificheRor2.get(i).getImportoOld().compareTo(zero)!=0//SIAC-7995 Controllo che anche l'importo sia diverso da zero
						&& listaModificheRor2.get(i).getDescrizioneModificaMovimentoGestione() != null) {
					modificheDaAnnullare.add(listaModificheRor2.get(i));
				}

			}
			return modificheDaAnnullare;
		} else {
			List<ModificaMovimentoGestioneSpesa> modificheDaAnnullare = new ArrayList<ModificaMovimentoGestioneSpesa>();
			BigDecimal zero = new BigDecimal("0.00");
			for (int i = 0; i < listaModificheRor2.size(); i++) {				
				for(int j=0; j<listaModificheRorOld.size(); j++){
					ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa2 = listaModificheRor2.get(i);
					ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesaOld = listaModificheRorOld.get(j);
					if (modificaMovimentoGestioneSpesa2.getUid()==modificaMovimentoGestioneSpesaOld.getUid() && modificaMovimentoGestioneSpesa2.getImportoOld() != null
							&& modificaMovimentoGestioneSpesaOld.getImportoOld() != null
							&& modificaMovimentoGestioneSpesa2.getDescrizioneModificaMovimentoGestione() != null
							&& !modificaMovimentoGestioneSpesa2.getDescrizioneModificaMovimentoGestione().equals("")
							&& modificaMovimentoGestioneSpesa2.getImportoOld().abs()
							.compareTo(modificaMovimentoGestioneSpesaOld.getImportoOld().abs()) != 0 && modificaMovimentoGestioneSpesa2.getImportoOld().compareTo(zero)!=0) {
						modificheDaAnnullare.add(modificaMovimentoGestioneSpesa2);
					}					
				}
				
				


			}
			return modificheDaAnnullare;
		}

	}

	public static List<ModificaMovimentoGestioneEntrata> modificheDaAnnullarePerRicreareAcc(
			List<ModificaMovimentoGestioneEntrata> listaModificheRor2,
			List<ModificaMovimentoGestioneEntrata> listaModificheRorOld, Boolean stessoProvvedimento) {

		if (stessoProvvedimento != null && stessoProvvedimento == false) {
			BigDecimal zero = new BigDecimal("0.00");
			List<ModificaMovimentoGestioneEntrata> modificheDaAnnullare = new ArrayList<ModificaMovimentoGestioneEntrata>();
			for (int i = 0; i < listaModificheRor2.size(); i++) {

				if (listaModificheRor2.get(i).getImportoOld() != null  && listaModificheRor2.get(i).getImportoOld().compareTo(zero)!=0//SIAC-7995 Controllo che anche l'importo sia diverso da zero
						&& listaModificheRor2.get(i).getDescrizioneModificaMovimentoGestione() != null) {
					modificheDaAnnullare.add(listaModificheRor2.get(i));
				}

			}
			return modificheDaAnnullare;
		} else {
			List<ModificaMovimentoGestioneEntrata> modificheDaAnnullare = new ArrayList<ModificaMovimentoGestioneEntrata>();
			BigDecimal zero = new BigDecimal("0.00");
 
			for (int i = 0; i < listaModificheRor2.size(); i++) {				
				int recordAgg=0;//SIAC-7349   SR190 FL 07/05/2020 
				for(int j=0; j<listaModificheRorOld.size(); j++){
					if (listaModificheRor2.get(i).getUid()==listaModificheRorOld.get(j).getUid() && listaModificheRor2.get(i).getImportoOld() != null
							&& listaModificheRorOld.get(j).getImportoOld() != null
							&& listaModificheRor2.get(i).getDescrizioneModificaMovimentoGestione() != null
							&& !listaModificheRor2.get(i).getDescrizioneModificaMovimentoGestione().equals("")
							&& listaModificheRor2.get(i).getImportoOld().abs()
							.compareTo(listaModificheRorOld.get(j).getImportoOld().abs()) != 0 && listaModificheRor2.get(i).getImportoOld().compareTo(zero)!=0) {
						//SIAC-7349 Inizio  SR190 FL 07/05/2020 
						recordAgg++;
						//modificheDaAnnullare.add(listaModificheRor2.get(i));
						//SIAC-7349 Fine  SR190 FL 07/05/2020 
						
					}					
					
					
					//SIAC-7349 Inizio  SR190 FL 07/05/2020 
					//CONTROLLO CHE SIA STATO MODIFICATO SOLO L'IMPORTO COLLEGAMENTO CANCELLO E RICREO
					if (listaModificheRor2.get(i).getUid()==listaModificheRorOld.get(j).getUid() && listaModificheRor2.get(i).getImportoOld() != null ) {
						List<ModificaMovimentoGestioneSpesaCollegata>  lmgscOLD = listaModificheRorOld.get(j).getListaModificheMovimentoGestioneSpesaCollegata();
						if ( recordAgg ==0  &&  lmgscOLD!= null && !lmgscOLD.isEmpty()) {
							List<ModificaMovimentoGestioneSpesaCollegata> lmgscAGG = listaModificheRor2.get(i).getListaModificheMovimentoGestioneSpesaCollegata();
							if (lmgscAGG!=null && !lmgscAGG.isEmpty()) {
								for (ModificaMovimentoGestioneSpesaCollegata mgscAGG : lmgscAGG) {		
									for (ModificaMovimentoGestioneSpesaCollegata mgscOLD : lmgscOLD) {
										if ( mgscOLD.getModificaMovimentoGestioneSpesa().getImpegno().getNumeroBigDecimal().equals(mgscAGG.getModificaMovimentoGestioneSpesa().getImpegno().getNumeroBigDecimal())
												&& mgscOLD.getModificaMovimentoGestioneSpesa().getNumeroModificaMovimentoGestione() == mgscAGG.getModificaMovimentoGestioneSpesa().getNumeroModificaMovimentoGestione()
												&& mgscOLD.getImportoCollegamento().compareTo(mgscAGG.getImportoCollegamento()) != 0 
												&& mgscAGG.getImportoCollegamento().compareTo(zero)!=0
											) {
											recordAgg++;
										}
										 
									}
								}
							}
						}
					}
				
					//SIAC-7349 Fine  SR190 FL 07/05/2020 
				}
				if (recordAgg> 0) {
					modificheDaAnnullare.add(listaModificheRor2.get(i));
				}
			}
			
 
			return modificheDaAnnullare;

		}
	}

	public static List<ModificaMovimentoGestioneEntrata> modificheDaAnnullareAcc(
			List<ModificaMovimentoGestioneEntrata> listaModificheRor2, Boolean daMantenere) {
		BigDecimal importoNullo = new BigDecimal("0.00");
		if (daMantenere == null)
			daMantenere = false;
		List<ModificaMovimentoGestioneEntrata> modificheDaAnnullare = new ArrayList<ModificaMovimentoGestioneEntrata>();
		for (int i = 0; i < listaModificheRor2.size(); i++) {
			String descrizione = listaModificheRor2.get(i).getDescrizioneModificaMovimentoGestione();

			BigDecimal perProva = listaModificheRor2.get(i).getImportoOld() == null ? new BigDecimal("0.00")
					: listaModificheRor2.get(i).getImportoOld();

			boolean importoZero = perProva.equals(importoNullo);
			if (!listaModificheRor2.get(i).getTipoModificaMovimentoGestione().equals("RORM")
					&& importoZero) {
//					&& (descrizione == null || descrizione.equals("")) && importoZero) {
				modificheDaAnnullare.add(listaModificheRor2.get(i));
			}
			if (listaModificheRor2.get(i).getTipoModificaMovimentoGestione().equals("RORM")
					&& (!daMantenere || (descrizione == null || descrizione.equals("")))) {
				modificheDaAnnullare.add(listaModificheRor2.get(i));
			}
		}
		return modificheDaAnnullare;
	}

	public static List<ModificaMovimentoGestioneSpesa> modificheMovimentiPreesistenti(
			List<ModificaMovimentoGestioneSpesa> listaModificheRor2) {
		List<ModificaMovimentoGestioneSpesa> listaModifichePreesistenti = new ArrayList<ModificaMovimentoGestioneSpesa>();
		for (int i = 0; i < listaModificheRor2.size(); i++) {
			if (listaModificheRor2.get(i).getUid() != 0)
				listaModifichePreesistenti.add(listaModificheRor2.get(i));
		}

		return listaModifichePreesistenti;
	}

	public static List<ModificaMovimentoGestioneEntrata> modificheMovimentiPreesistentiAcc(
			List<ModificaMovimentoGestioneEntrata> listaModificheRor2) {
		List<ModificaMovimentoGestioneEntrata> listaModifichePreesistenti = new ArrayList<ModificaMovimentoGestioneEntrata>();
		for (int i = 0; i < listaModificheRor2.size(); i++) {
			if (listaModificheRor2.get(i).getUid() != 0)
				listaModifichePreesistenti.add(listaModificheRor2.get(i));
		}

		return listaModifichePreesistenti;
	}

	public static List<ModificaMovimentoGestioneSpesa> getMovimentiDaAggiornare(
			List<ModificaMovimentoGestioneSpesa> listaModificheRor2,
			List<ModificaMovimentoGestioneSpesa> listaModificheRorDaAggiornare2) {

		BigDecimal importoNullo = new BigDecimal("0.00");
		List<ModificaMovimentoGestioneSpesa> movDaAggiornare = new ArrayList<ModificaMovimentoGestioneSpesa>();

		//SIAC-7349 Inizio SR190 CM 05/06/2020 ordinate le liste in base all'uid per aggiornare correttamente la descrizione di una modifica di impegno
		if (!listaModificheRor2.isEmpty() && listaModificheRor2.size() > 1) {
			Collections.sort(listaModificheRor2, new Comparator<ModificaMovimentoGestioneSpesa>() {
				@Override
				public int compare(ModificaMovimentoGestioneSpesa o1, ModificaMovimentoGestioneSpesa o2) {
					if (o1.getUid() == o2.getUid())
						return 0;
					return o1.getUid() < o2.getUid() ? -1 : 1;
				}
			});
		}
		
		if (!listaModificheRorDaAggiornare2.isEmpty() && listaModificheRorDaAggiornare2.size() > 1) {
			Collections.sort(listaModificheRorDaAggiornare2, new Comparator<ModificaMovimentoGestioneSpesa>() {
				@Override
				public int compare(ModificaMovimentoGestioneSpesa o1, ModificaMovimentoGestioneSpesa o2) {
					if (o1.getUid() == o2.getUid())
						return 0;
					return o1.getUid() < o2.getUid() ? -1 : 1;
				}
			});
		}
		//SIAC-7349 Fine SR190 CM 05/06/2020
		
		if (listaModificheRor2.size() == listaModificheRorDaAggiornare2.size()) {
			for (int i = 0; i < listaModificheRor2.size(); i++) {
				// FIXME prova per il mantenere
				BigDecimal importoSpesa = listaModificheRor2.get(i).getImportoOld() == null ? new BigDecimal("0.00")
						: listaModificheRor2.get(i).getImportoOld();

				boolean modificato = (!listaModificheRor2.get(i).getDescrizioneModificaMovimentoGestione()
						.equals(listaModificheRorDaAggiornare2.get(i).getDescrizioneModificaMovimentoGestione()));

				boolean daNonAggiungere = listaModificheRor2.get(i).getDescrizioneModificaMovimentoGestione().equals("")
						&& importoSpesa.equals(importoNullo);
				boolean cambiatoImporto = importoSpesa
						.compareTo(listaModificheRorDaAggiornare2.get(i).getImportoOld().abs()) != 0; //SIAC-7349 SR190 CM 28/05/2020 aggiunto abs() per fix aggiorna modifica impegno
				if (modificato && !daNonAggiungere && !cambiatoImporto) {
					movDaAggiornare.add(listaModificheRor2.get(i));
				}

			}
		}

		return movDaAggiornare;
	}

	
	
	public static List<ModificaMovimentoGestioneEntrata> getMovimentiDaAggiornareAcc(
			List<ModificaMovimentoGestioneEntrata> listaModificheRor2,
			List<ModificaMovimentoGestioneEntrata> listaModificheRorDaAggiornare2) {

		BigDecimal importoNullo = new BigDecimal("0.00");
		List<ModificaMovimentoGestioneEntrata> movDaAggiornare = new ArrayList<ModificaMovimentoGestioneEntrata>();
		
		//SIAC-7349 Inizio SR190 CM 05/06/2020 ordinate le liste in base all'uid per aggiornare correttamente la descrizione di una modifica di impegno
		if (!listaModificheRor2.isEmpty() && listaModificheRor2.size() > 1) {
			Collections.sort(listaModificheRor2, new Comparator<ModificaMovimentoGestioneEntrata>() {
				@Override
				public int compare(ModificaMovimentoGestioneEntrata o1, ModificaMovimentoGestioneEntrata o2) {
					if (o1.getUid() == o2.getUid())
						return 0;
					return o1.getUid() < o2.getUid() ? -1 : 1;
				}
			});
		}
		
		if (!listaModificheRorDaAggiornare2.isEmpty() && listaModificheRorDaAggiornare2.size() > 1) {
			Collections.sort(listaModificheRorDaAggiornare2, new Comparator<ModificaMovimentoGestioneEntrata>() {
				@Override
				public int compare(ModificaMovimentoGestioneEntrata o1, ModificaMovimentoGestioneEntrata o2) {
					if (o1.getUid() == o2.getUid())
						return 0;
					return o1.getUid() < o2.getUid() ? -1 : 1;
				}
			});
		}
		//SIAC-7349 Fine SR190 CM 05/06/2020
		
		if (listaModificheRor2.size() == listaModificheRorDaAggiornare2.size()) {
			for (int i = 0; i < listaModificheRor2.size(); i++) {
				// FIXME prova per il mantenere
				BigDecimal importoEntrata = listaModificheRor2.get(i).getImportoOld() == null ? new BigDecimal("0.00")
						: listaModificheRor2.get(i).getImportoOld();

				boolean modificato = (!listaModificheRor2.get(i).getDescrizioneModificaMovimentoGestione()
						.equals(listaModificheRorDaAggiornare2.get(i).getDescrizioneModificaMovimentoGestione()));
						//|| !importoEntrata.equals(listaModificheRorDaAggiornare2.get(i).getImportoOld())); //SIAC-7960 modificato il controllo e sotto con  importo in abs
				boolean daNonAggiungere = listaModificheRor2.get(i).getDescrizioneModificaMovimentoGestione().equals("")
						&& importoEntrata.equals(importoNullo);
				boolean cambiatoImporto = importoEntrata
						.compareTo(listaModificheRorDaAggiornare2.get(i).getImportoOld().abs()) != 0; //SIAC-7349 SR190 CM 03/06/2020 aggiunto abs() per fix aggiorna modifica accertamento
				if (modificato && !daNonAggiungere && !cambiatoImporto) {
					movDaAggiornare.add(listaModificheRor2.get(i));
				}
			}
		}

		return movDaAggiornare;
	}

	public static List<ModificaMovimentoGestioneSpesa> modificheValideDaAggiungere(
			List<ModificaMovimentoGestioneSpesa> listaModificheRor2) {

		BigDecimal importoNullo = new BigDecimal("0.00");
		List<ModificaMovimentoGestioneSpesa> nuoviElementiDaAggiungere = new ArrayList<ModificaMovimentoGestioneSpesa>();
		// dimensione lista new > old. Nuove annualita aggiunte
		for (int i = 0; i < listaModificheRor2.size(); i++) {
			ModificaMovimentoGestioneSpesa mmgs = listaModificheRor2.get(i);
			String descrizione = mmgs.getDescrizioneModificaMovimentoGestione();
			BigDecimal importo = mmgs.getImportoOld();
			int uid = mmgs.getUid();

			if (uid == 0 && !importo.equals(importoNullo)) {
				nuoviElementiDaAggiungere.add(mmgs);
			}
		}

		return nuoviElementiDaAggiungere;
	}

	public static List<ModificaMovimentoGestioneEntrata> modificheValideDaAggiungereAcc(
			List<ModificaMovimentoGestioneEntrata> listaModificheRor2) {

		BigDecimal importoNullo = new BigDecimal("0.00");
		List<ModificaMovimentoGestioneEntrata> nuoviElementiDaAggiungere = new ArrayList<ModificaMovimentoGestioneEntrata>();
		// dimensione lista new > old. Nuove annualita aggiunte
		for (int i = 0; i < listaModificheRor2.size(); i++) {
			ModificaMovimentoGestioneEntrata mmgs = listaModificheRor2.get(i);
			String descrizione = mmgs.getDescrizioneModificaMovimentoGestione();
			BigDecimal importo = mmgs.getImportoOld();
			int uid = mmgs.getUid();

			if (uid == 0 && (descrizione != null || !descrizione.equals("")) && !importo.equals(importoNullo)) {
				nuoviElementiDaAggiungere.add(mmgs);
			}
		}

		return nuoviElementiDaAggiungere;
	}

	public static ModificaMovimentoGestioneSpesa mapCruscottoModelToMovimentoGestione(
			GestioneCruscottoModel nuovaModifica, String tipoModifica) {
		ModificaMovimentoGestioneSpesa modifica = new ModificaMovimentoGestioneSpesa();
		modifica.setUid(0);
		if (tipoModifica.equals("RORM") && nuovaModifica.getImporto() == null) {
			nuovaModifica.setImporto("0.00");
		}

		BigDecimal importo = FinActionUtils.convertiImportoToBigDecimal(nuovaModifica.getImporto());
		modifica.setDescrizioneModificaMovimentoGestione(nuovaModifica.getDescrizione());
		modifica.setImportoOld(importo);
		modifica.setTipoModificaMovimentoGestione(tipoModifica);
		modifica.setReimputazione(false);
		return modifica;
	}

	public static ModificaMovimentoGestioneEntrata mapCruscottoModelToMovimentoGestioneAcc(
			GestioneCruscottoModel nuovaModifica, String tipoModifica) {
		ModificaMovimentoGestioneEntrata modifica = new ModificaMovimentoGestioneEntrata();
		modifica.setUid(0);
		if (tipoModifica.equals("RORM") && nuovaModifica.getImporto() == null) {
			nuovaModifica.setImporto("0.00");
		}
		BigDecimal importo = FinActionUtils.convertiImportoToBigDecimal(nuovaModifica.getImporto());
		modifica.setDescrizioneModificaMovimentoGestione(nuovaModifica.getDescrizione());
		modifica.setImportoOld(importo);
		modifica.setTipoModificaMovimentoGestione(tipoModifica);
		modifica.setReimputazione(false);
		return modifica;
	}

	public static boolean checkEmptyModif(GestioneCruscottoModel nuovaModifica, String optional) {
		if (optional.equals("RORM")) {
			return nuovaModifica.getDescrizione().equals("") || nuovaModifica.getDescrizione() == null;
		}
		// return nuovaModifica.getDescrizione().equals("") &&
		// nuovaModifica.getImporto().equals("");
		return (nuovaModifica.getDescrizione() == null || nuovaModifica.getDescrizione().equals(""))
				|| (nuovaModifica.getImporto() == null || nuovaModifica.getImporto().equals("")
						|| nuovaModifica.getImporto().equals("0.00"));
	}

	public static List<ModificaMovimentoGestioneSpesa> remuoviReimputazioniNonInserite(
			List<ModificaMovimentoGestioneSpesa> modifichePerAnnullamenti) {
		List<ModificaMovimentoGestioneSpesa> modifichePerAnnullamentiUtil = new ArrayList<ModificaMovimentoGestioneSpesa>();

		for (int i = 0; i < modifichePerAnnullamenti.size(); i++) {
			if (modifichePerAnnullamenti.get(i).getUid() != 0) {
				modifichePerAnnullamentiUtil.add(modifichePerAnnullamenti.get(i));
			}
		}

		return modifichePerAnnullamentiUtil;
	}

	public static List<ModificaMovimentoGestioneEntrata> remuoviReimputazioniAccNonInserite(
			List<ModificaMovimentoGestioneEntrata> modifichePerAnnullamenti) {
		List<ModificaMovimentoGestioneEntrata> modifichePerAnnullamentiUtil = new ArrayList<ModificaMovimentoGestioneEntrata>();

		for (int i = 0; i < modifichePerAnnullamenti.size(); i++) {
			if (modifichePerAnnullamenti.get(i).getUid() != 0) {
				modifichePerAnnullamentiUtil.add(modifichePerAnnullamenti.get(i));
			}
		}

		return modifichePerAnnullamentiUtil;
	}

	public static boolean checkIfProvvedimentoModificato(ProvvedimentoImpegnoModel provvedimento,
			ProvvedimentoImpegnoModel provvedimento2) {
		if (provvedimento.getUid() != provvedimento2.getUid()) {
			return true;
		}
		return false;
	}

	public static boolean stessoProvvedimento(AttoAmministrativo attoAmministrativo,
			AttoAmministrativo attoAmministrativo2) {

		Integer annoProvvedimentoImpegno = attoAmministrativo.getAnno();
		Integer numeroProvvedimentoImpegno = attoAmministrativo.getNumero();
		String codiceProvvedimentoImpegno = attoAmministrativo.getTipoAtto().getCodice();
		Integer uidStrutturaAmministrativa = attoAmministrativo.getStrutturaAmmContabile().getUid();

		Integer annoProvvedimentoModifica = attoAmministrativo2.getAnno();
		Integer numeroProvvedimentoModifica = attoAmministrativo2.getNumero();
		String codiceProvvedimentoModifica = attoAmministrativo2.getTipoAtto().getCodice();
		Integer uidStrutturaAmministrativaModifica = attoAmministrativo2.getStrutturaAmmContabile().getUid();

		boolean uguali = false;

		if (annoProvvedimentoImpegno.equals(annoProvvedimentoModifica)
				&& numeroProvvedimentoImpegno.equals(numeroProvvedimentoModifica)
				&& codiceProvvedimentoImpegno.equals(codiceProvvedimentoModifica) && FinStringUtils
						.sonoUgualiIntConNullUguale(uidStrutturaAmministrativa, uidStrutturaAmministrativaModifica)) {
			uguali = true;
		}

		return uguali;

	}

	public static boolean stessoProvvedimentoCruscotto(AttoAmministrativo attoAmministrativo,
			AttoAmministrativo attoAmministrativo2) {

		if (attoAmministrativo.getUid() == attoAmministrativo2.getUid())
			return true;

		return false;
	}

	public static List<Errore> checkCampiProvvedimentoVuoti(GestisciImpegnoStep1Model step1Model,
			List<Errore> erroriSalvataggio) {

		if (step1Model.getProvvedimento().getAnnoProvvedimento() != null) {
			if (step1Model.getProvvedimento().getAnnoProvvedimento() == 0) {
				erroriSalvataggio.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Provvedimento"));
			}
		} else {
			erroriSalvataggio.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno Provvedimento"));

		}

		// Numero Provvedimento Obbligatorio
		if (step1Model.getProvvedimento().getNumeroProvvedimento() != null) {
			if (step1Model.getProvvedimento().getNumeroProvvedimento().intValue() == 0) {
				erroriSalvataggio.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Provvedimento"));
			}
		} else {
			erroriSalvataggio.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero Provvedimento"));
		}

		// Tipo Provvedimento Obbligatorio
		if (step1Model.getProvvedimento().getIdTipoProvvedimento() != null) {
			if (step1Model.getProvvedimento().getIdTipoProvvedimento() == 0) {
				erroriSalvataggio.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Provvedimento"));
			}
		} else {
			erroriSalvataggio.add(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Provvedimento"));
		}

		return erroriSalvataggio;
	}

	// Questo metodo mi serve a calcolare le vecchie cancellazioni
	public static BigDecimal getImportoAttualeDaAnnullaInserisci(List<ModificaMovimentoGestioneSpesa> nuoveDaRicreare,
			List<ModificaMovimentoGestioneSpesa> vecchiePerImporti) {
		/*
		 * SIAC-7689
		 * FIX importi decimali
		 */
		BigDecimal importoAttualeNuovo = BigDecimal.ZERO;
		for (int i = 0; i < vecchiePerImporti.size(); i++) {
			for (int j = 0; j < nuoveDaRicreare.size(); j++) {
				if (vecchiePerImporti.get(i).getUid() == nuoveDaRicreare.get(j).getUid()) {
					//importoAttualeNuovo += vecchiePerImporti.get(i).getImportoOld().intValue();
					importoAttualeNuovo =  importoAttualeNuovo.add(vecchiePerImporti.get(i).getImportoOld());
				}
			}

		}

		return importoAttualeNuovo;

	}

	public static BigDecimal getImportoAttualeDaAnnullaInserisciAcc(
			List<ModificaMovimentoGestioneEntrata> nuoveDaRicreare,
			List<ModificaMovimentoGestioneEntrata> vecchiePerImporti) {
		/*
		 * SIAC-7689
		 * FIX importi decimali
		 */
		BigDecimal importoAttualeNuovo = BigDecimal.ZERO;
		//int importoAttualeNuovo = 0;
		for (int i = 0; i < vecchiePerImporti.size(); i++) {
			for (int j = 0; j < nuoveDaRicreare.size(); j++) {
				if (vecchiePerImporti.get(i).getUid() == nuoveDaRicreare.get(j).getUid()) {
					//importoAttualeNuovo += vecchiePerImporti.get(i).getImportoOld().intValue();
					importoAttualeNuovo =  importoAttualeNuovo.add(vecchiePerImporti.get(i).getImportoOld());
				}
			}

		}

		//return new BigDecimal(importoAttualeNuovo);
		return importoAttualeNuovo;
	}

	// Nuove implementazioni controllo importi modifiche
	public static List<Errore> checkImportiReimpECancell(List<Errore> listaErrori,
			List<ModificaMovimentoGestioneSpesa> spese, BigDecimal maxDiff, BigDecimal maxCanc, 
			BigDecimal limiteDestro,
			BigDecimal importoAnnullatoPrimaDiAggiornamentoReimputazione, 
			BigDecimal importoAnnullatoPrimaDiAggiornamentoCancellazione, 
			BigDecimal importoAnnullatoReimputazionePerRicreare, 
			BigDecimal importoAnnullatoCancellazionePerRicreare) {
		
		if(spese.isEmpty()){
			return listaErrori;
		}
		
		BigDecimal importoReimputato = BigDecimal.ZERO;
		BigDecimal importoDaCancellare = BigDecimal.ZERO;
		
		//da annullare nella transazione
		BigDecimal impAnnullatoAggReimp = (importoAnnullatoPrimaDiAggiornamentoReimputazione == null) ? BigDecimal.ZERO : importoAnnullatoPrimaDiAggiornamentoReimputazione;
		BigDecimal impAnnullatoAggCanc = (importoAnnullatoPrimaDiAggiornamentoCancellazione == null) ? BigDecimal.ZERO : importoAnnullatoPrimaDiAggiornamentoCancellazione;
		//da annullare nella transazione per ricreare
		BigDecimal impAnnullatoRicReimp = (importoAnnullatoReimputazionePerRicreare == null) ? BigDecimal.ZERO : importoAnnullatoReimputazionePerRicreare;
		BigDecimal impAnnullatoRicCanc = (importoAnnullatoCancellazionePerRicreare == null) ? BigDecimal.ZERO : importoAnnullatoCancellazionePerRicreare;
		
		BigDecimal importoMassimoDifferibile = maxDiff;
	
		BigDecimal importoMassimoCancellabile = maxCanc.negate();
		

		
		for (ModificaMovimentoGestioneSpesa mmgs : spese) {

			if (mmgs.getUid() == 0 && mmgs.getStatoOperativoModificaMovimentoGestione().equals(StatoOperativoModificaMovimentoGestione.VALIDO)
					&& mmgs.getTipoModificaMovimentoGestione() != null
					&& mmgs.getTipoModificaMovimentoGestione().equals("REIMP")) {
				importoReimputato = importoReimputato.add(mmgs.getImportoOld());

			} else if (mmgs.getUid()==0 && mmgs.getStatoOperativoModificaMovimentoGestione()
					.equals(StatoOperativoModificaMovimentoGestione.VALIDO)
					&& mmgs.getTipoModificaMovimentoGestione() != null
					&& (mmgs.getTipoModificaMovimentoGestione().equals("INSROR")
							|| mmgs.getTipoModificaMovimentoGestione().equals("INEROR"))) {

				importoDaCancellare = importoDaCancellare.add(mmgs.getImportoOld());

			}

		}

		
		BigDecimal importoAggiornatoReimpDiff = importoMassimoDifferibile.add(impAnnullatoAggReimp.abs()).add(impAnnullatoRicReimp.abs());
		BigDecimal importoAggiornatoReimpCanc = importoMassimoCancellabile.add(impAnnullatoAggCanc).add(impAnnullatoRicCanc);
		
		limiteDestro = limiteDestro.subtract(impAnnullatoAggCanc).subtract(impAnnullatoRicCanc);
		//qui devo stare attento alle situazioni miste in aggiornamento
		
		
		importoReimputato = importoReimputato.abs();
		
		if (importoReimputato.compareTo(importoAggiornatoReimpDiff) > 0) {
			Errore err = new Errore();
			err.setCodice("FIN_ERR_0040");
			err.setDescrizione(
					"La somma delle modifiche di tipo ROR - Reimputazione deve essere essere inferiore all' Importo Massimo Differibile");
			listaErrori.add(err);

		}
//
		if (importoDaCancellare.compareTo(importoAggiornatoReimpCanc) < 0) {
			Errore err = new Errore();
			err.setCodice("FIN_ERR_0040");
			err.setDescrizione(
					"La somma delle modifiche di tipo ROR - Cancellazione INS/INE deve essere essere inferiore all' Importo Massimo Cancellabile");
			listaErrori.add(err);

		}
		
		if (importoDaCancellare.compareTo(limiteDestro) > 0) {
			Errore err = new Errore();
			err.setCodice("FIN_ERR_0040");
			err.setDescrizione(
					"La somma delle modifiche di tipo ROR - Cancellazione INS/INE deve essere essere inferiore al limite massimo");
			listaErrori.add(err);

		}


		return listaErrori;
	}

	// Nuove implementazioni controllo importi modifiche
	// Nuove implementazioni controllo importi modifiche
		public static List<Errore> checkImportiReimpECancellAcc(List<Errore> listaErrori,
				List<ModificaMovimentoGestioneEntrata> spese, BigDecimal maxDiff, BigDecimal maxCanc, 
				BigDecimal limiteDestro,
				BigDecimal importoAnnullatoPrimaDiAggiornamentoReimputazione, 
				BigDecimal importoAnnullatoPrimaDiAggiornamentoCancellazione, 
				BigDecimal importoAnnullatoReimputazionePerRicreare, 
				BigDecimal importoAnnullatoCancellazionePerRicreare) {
			
			if(spese.isEmpty()){
				return listaErrori;
			}
			
			BigDecimal importoReimputato = BigDecimal.ZERO;
			BigDecimal importoDaCancellare = BigDecimal.ZERO;
			
			//da annullare nella transazione
			BigDecimal impAnnullatoAggReimp = (importoAnnullatoPrimaDiAggiornamentoReimputazione == null) ? BigDecimal.ZERO : importoAnnullatoPrimaDiAggiornamentoReimputazione;
			BigDecimal impAnnullatoAggCanc = (importoAnnullatoPrimaDiAggiornamentoCancellazione == null) ? BigDecimal.ZERO : importoAnnullatoPrimaDiAggiornamentoCancellazione;
			//da annullare nella transazione per ricreare
			BigDecimal impAnnullatoRicReimp = (importoAnnullatoReimputazionePerRicreare == null) ? BigDecimal.ZERO : importoAnnullatoReimputazionePerRicreare;
			BigDecimal impAnnullatoRicCanc = (importoAnnullatoCancellazionePerRicreare == null) ? BigDecimal.ZERO : importoAnnullatoCancellazionePerRicreare;
			
			BigDecimal importoMassimoDifferibile = maxDiff;
		
			BigDecimal importoMassimoCancellabile = maxCanc.negate();
			

			
			for (ModificaMovimentoGestioneEntrata mmgs : spese) {

				if (mmgs.getUid() == 0 && mmgs.getStatoOperativoModificaMovimentoGestione().equals(StatoOperativoModificaMovimentoGestione.VALIDO)
						&& mmgs.getTipoModificaMovimentoGestione() != null
						&& mmgs.getTipoModificaMovimentoGestione().equals("REIMP")) {
					importoReimputato = importoReimputato.add(mmgs.getImportoOld());

				} else if (mmgs.getUid() == 0 && mmgs.getStatoOperativoModificaMovimentoGestione()
						.equals(StatoOperativoModificaMovimentoGestione.VALIDO)
						&& mmgs.getTipoModificaMovimentoGestione() != null
						&& (mmgs.getTipoModificaMovimentoGestione().equals("INSROR")
								|| mmgs.getTipoModificaMovimentoGestione().equals("INEROR"))) {

					importoDaCancellare = importoDaCancellare.add(mmgs.getImportoOld());

				}

			}

			
			BigDecimal importoAggiornatoReimpDiff = importoMassimoDifferibile.add(impAnnullatoAggReimp.abs()).add(impAnnullatoRicReimp.abs());
			BigDecimal importoAggiornatoReimpCanc = importoMassimoCancellabile.add(impAnnullatoAggCanc).add(impAnnullatoRicCanc);
			
			limiteDestro = limiteDestro.subtract(impAnnullatoAggCanc).subtract(impAnnullatoRicCanc);
			//qui devo stare attento alle situazioni miste in aggiornamento
			
			
			importoReimputato = importoReimputato.abs();
			
			if (importoReimputato.compareTo(importoAggiornatoReimpDiff) > 0) {
				Errore err = new Errore();
				err.setCodice("FIN_ERR_0040");
				err.setDescrizione(
						"La somma delle modifiche di tipo ROR - Reimputazione deve essere essere inferiore all' Importo Massimo Differibile");
				listaErrori.add(err);

			}
	//
			if (importoDaCancellare.compareTo(importoAggiornatoReimpCanc) < 0) {
				Errore err = new Errore();
				err.setCodice("FIN_ERR_0040");
				err.setDescrizione(
						"La somma delle modifiche di tipo ROR - Cancellazione INS/INE deve essere essere inferiore all' Importo Massimo Cancellabile");
				listaErrori.add(err);

			}
			
			if (importoDaCancellare.compareTo(limiteDestro) > 0) {
				Errore err = new Errore();
				err.setCodice("FIN_ERR_0040");
				err.setDescrizione(
						"La somma delle modifiche di tipo ROR - Cancellazione INS/INE deve essere essere inferiore al limite massimo");
				listaErrori.add(err);

			}


			return listaErrori;
		}
	
	public static List<Errore> checkImportiReimpECancellSubTest(List<Errore> listaErrori,
			List<ModificaMovimentoGestioneSpesa> spese, BigDecimal maxDiff, BigDecimal maxCanc, BigDecimal maxDiffPadre, BigDecimal maxCancPadre, BigDecimal importoAnnullatoPrimaDiAggiornamentoReimputazione, BigDecimal importoAnnullatoPrimaDiAggiornamentoCancellazione, BigDecimal importoAnnullatoReimputazionePerRicreare, BigDecimal importoAnnullatoCancellazionePerRicreare, 
			BigDecimal limiteDestroPadre, BigDecimal limiteDestroSub) {
		
		if(spese.isEmpty()){
			return listaErrori;
		}
		
		BigDecimal importoReimputato = BigDecimal.ZERO;
		BigDecimal importoDaCancellare = BigDecimal.ZERO;
		
		
		//da annullare nella transazione		
		BigDecimal impAnnullatoAggReimp = (importoAnnullatoPrimaDiAggiornamentoReimputazione == null) ? BigDecimal.ZERO : importoAnnullatoPrimaDiAggiornamentoReimputazione;
		BigDecimal impAnnullatoAggCanc = (importoAnnullatoPrimaDiAggiornamentoCancellazione == null) ? BigDecimal.ZERO : importoAnnullatoPrimaDiAggiornamentoCancellazione;
		//da annullare nella transazione per ricreare
		BigDecimal impAnnullatoRicReimp = (importoAnnullatoReimputazionePerRicreare == null) ? BigDecimal.ZERO : importoAnnullatoReimputazionePerRicreare;
		BigDecimal impAnnullatoRicCanc = (importoAnnullatoCancellazionePerRicreare == null) ? BigDecimal.ZERO : importoAnnullatoCancellazionePerRicreare;
		
		BigDecimal importoMassimoDifferibile = maxDiff;
		BigDecimal importoMassimoCancellabile = maxCanc.negate();
		
		importoMassimoDifferibile = importoMassimoDifferibile.add(impAnnullatoAggReimp.abs()).add(impAnnullatoRicReimp.abs());
		//qui aggiungo la quantita annullata e quella annullata per essere ricreata per reimputazione
		importoMassimoCancellabile = importoMassimoCancellabile.add(impAnnullatoAggCanc).add(impAnnullatoRicCanc);
		//qui aggiungo la quantita annullata e quella annullata per essere ricreata per cancellazione
		
		BigDecimal importoMassimoDifferibilePadre = maxDiffPadre.abs();
		BigDecimal importoMassimoCancellabilePadre = maxCancPadre;
		for (ModificaMovimentoGestioneSpesa mmgs : spese) {

			if (mmgs.getUid()== 0 && mmgs.getStatoOperativoModificaMovimentoGestione().equals(StatoOperativoModificaMovimentoGestione.VALIDO)
					&& mmgs.getTipoModificaMovimentoGestione() != null
					&& mmgs.getTipoModificaMovimentoGestione().equals("REIMP")) {
				importoReimputato = importoReimputato.add(mmgs.getImportoOld());

			} else if (mmgs.getUid()== 0 && mmgs.getStatoOperativoModificaMovimentoGestione()
					.equals(StatoOperativoModificaMovimentoGestione.VALIDO)
					&& mmgs.getTipoModificaMovimentoGestione() != null
					&& (mmgs.getTipoModificaMovimentoGestione().equals("INSROR")
							|| mmgs.getTipoModificaMovimentoGestione().equals("INEROR"))) {

				importoDaCancellare = importoDaCancellare.add(mmgs.getImportoOld());

			}

		}

		BigDecimal importoAggiornatoReimpDiff = importoMassimoDifferibile.add(impAnnullatoAggReimp.abs()).add(impAnnullatoRicReimp.abs());
		BigDecimal importoAggiornatoReimpCanc = importoMassimoCancellabile.add(impAnnullatoAggCanc).add(impAnnullatoRicCanc);
		limiteDestroSub = limiteDestroSub.subtract(impAnnullatoAggCanc).subtract(impAnnullatoRicCanc);
		
		importoReimputato = importoReimputato.abs();
		
		


		if (importoReimputato.compareTo(importoAggiornatoReimpDiff) > 0) {
			Errore err = new Errore();
			err.setCodice("FIN_ERR_0040");
			err.setDescrizione(
					"La somma delle modifiche di tipo ROR - Reimputazione deve essere essere inferiore all' Importo Massimo Differibile");
			listaErrori.add(err);

		}
		//SIAC-7534
		//if (importoReimputato.compareTo(importoMassimoDifferibilePadre) > 0) {
		if (importoReimputato.compareTo(importoMassimoDifferibile) > 0) {
			Errore err = new Errore();
			err.setCodice("FIN_ERR_0040");
			err.setDescrizione(
					"La somma delle modifiche di tipo ROR - Reimputazione deve essere essere inferiore all' Importo Massimo Differibile dell' impegno padre");
			listaErrori.add(err);

		}

		if (importoDaCancellare.compareTo(importoAggiornatoReimpCanc) < 0) {//fixme <0 
			Errore err = new Errore();
			err.setCodice("FIN_ERR_0040");
			err.setDescrizione(
					"La somma delle modifiche di tipo ROR - Cancellazione INS/INE deve essere essere inferiore all' Importo Massimo Cancellabile");
			listaErrori.add(err);

		}
		if (importoDaCancellare.compareTo(limiteDestroSub) > 0) {//fixme <0 
			Errore err = new Errore();
			err.setCodice("FIN_ERR_0040");
			err.setDescrizione(
					"La somma delle modifiche di tipo ROR - Cancellazione INS/INE deve essere essere inferiore al limite massimo");
			listaErrori.add(err);

		}
		//SIAC-7534
		//if (importoDaCancellare.compareTo(importoMassimoCancellabilePadre) < 0) {
		if (importoDaCancellare.compareTo(importoMassimoCancellabile) < 0) {
			Errore err = new Errore();
			err.setCodice("FIN_ERR_0040");
			err.setDescrizione(
					"La somma delle modifiche di tipo ROR - Cancellazione INS/INE deve essere essere inferiore all' Importo Massimo Cancellabile dell' impegno padre");
			listaErrori.add(err);

		}
		if (importoDaCancellare.compareTo(limiteDestroPadre) > 0) {
			Errore err = new Errore();
			err.setCodice("FIN_ERR_0040");
			err.setDescrizione(
					"La somma delle modifiche di tipo ROR - Cancellazione INS/INE deve essere essere inferiore inferiore al limite massimo dell'impegno padre");
			listaErrori.add(err);

		}


		return listaErrori;
	}
	
	public static List<Errore> checkImportiReimpECancellSubTestAcc(List<Errore> listaErrori,
			List<ModificaMovimentoGestioneEntrata> spese, BigDecimal maxDiff, BigDecimal maxCanc, BigDecimal maxDiffPadre, BigDecimal maxCancPadre, BigDecimal importoAnnullatoPrimaDiAggiornamentoReimputazione, BigDecimal importoAnnullatoPrimaDiAggiornamentoCancellazione, BigDecimal importoAnnullatoReimputazionePerRicreare, BigDecimal importoAnnullatoCancellazionePerRicreare, 
			BigDecimal limiteDestroPadre, BigDecimal limiteDestroSub) {
		
		if(spese.isEmpty()){
			return listaErrori;
		}
		
		BigDecimal importoReimputato = BigDecimal.ZERO;
		BigDecimal importoDaCancellare = BigDecimal.ZERO;
		
		
		//da annullare nella transazione		
		BigDecimal impAnnullatoAggReimp = (importoAnnullatoPrimaDiAggiornamentoReimputazione == null) ? BigDecimal.ZERO : importoAnnullatoPrimaDiAggiornamentoReimputazione;
		BigDecimal impAnnullatoAggCanc = (importoAnnullatoPrimaDiAggiornamentoCancellazione == null) ? BigDecimal.ZERO : importoAnnullatoPrimaDiAggiornamentoCancellazione;
		//da annullare nella transazione per ricreare
		BigDecimal impAnnullatoRicReimp = (importoAnnullatoReimputazionePerRicreare == null) ? BigDecimal.ZERO : importoAnnullatoReimputazionePerRicreare;
		BigDecimal impAnnullatoRicCanc = (importoAnnullatoCancellazionePerRicreare == null) ? BigDecimal.ZERO : importoAnnullatoCancellazionePerRicreare;
		
		BigDecimal importoMassimoDifferibile = maxDiff;
		BigDecimal importoMassimoCancellabile = maxCanc.negate();
		
		importoMassimoDifferibile = importoMassimoDifferibile.add(impAnnullatoAggReimp.abs()).add(impAnnullatoRicReimp.abs());
		//qui aggiungo la quantita annullata e quella annullata per essere ricreata per reimputazione
		importoMassimoCancellabile = importoMassimoCancellabile.add(impAnnullatoAggCanc).add(impAnnullatoRicCanc);
		//qui aggiungo la quantita annullata e quella annullata per essere ricreata per cancellazione
		
		BigDecimal importoMassimoDifferibilePadre = maxDiffPadre.abs();
		BigDecimal importoMassimoCancellabilePadre = maxCancPadre;
		for (ModificaMovimentoGestioneEntrata mmgs : spese) {

			if (mmgs.getUid()== 0 && mmgs.getStatoOperativoModificaMovimentoGestione().equals(StatoOperativoModificaMovimentoGestione.VALIDO)
					&& mmgs.getTipoModificaMovimentoGestione() != null
					&& mmgs.getTipoModificaMovimentoGestione().equals("REIMP")) {
				importoReimputato = importoReimputato.add(mmgs.getImportoOld());

			} else if (mmgs.getUid()== 0 && mmgs.getStatoOperativoModificaMovimentoGestione()
					.equals(StatoOperativoModificaMovimentoGestione.VALIDO)
					&& mmgs.getTipoModificaMovimentoGestione() != null
					&& (mmgs.getTipoModificaMovimentoGestione().equals("INSROR")
							|| mmgs.getTipoModificaMovimentoGestione().equals("INEROR"))) {

				importoDaCancellare = importoDaCancellare.add(mmgs.getImportoOld());

			}

		}

		BigDecimal importoAggiornatoReimpDiff = importoMassimoDifferibile.add(impAnnullatoAggReimp.abs()).add(impAnnullatoRicReimp.abs());
		BigDecimal importoAggiornatoReimpCanc = importoMassimoCancellabile.add(impAnnullatoAggCanc).add(impAnnullatoRicCanc);
		
		limiteDestroSub = limiteDestroSub.subtract(impAnnullatoAggCanc).subtract(impAnnullatoRicCanc);
		importoReimputato = importoReimputato.abs();
		
		


		if (importoReimputato.compareTo(importoAggiornatoReimpDiff) > 0) {
			Errore err = new Errore();
			err.setCodice("FIN_ERR_0040");
			err.setDescrizione(
					"La somma delle modifiche di tipo ROR - Reimputazione deve essere essere inferiore all' Importo Massimo Differibile");
			listaErrori.add(err);

		}
		//SIAC-7534
		//if (importoReimputato.compareTo(importoMassimoDifferibilePadre) > 0) {
		if (importoReimputato.compareTo(importoMassimoDifferibile) > 0) {
			Errore err = new Errore();
			err.setCodice("FIN_ERR_0040");
			err.setDescrizione(
					"La somma delle modifiche di tipo ROR - Reimputazione deve essere essere inferiore all' Importo Massimo Differibile dell' impegno padre");
			listaErrori.add(err);

		}

		if (importoDaCancellare.compareTo(importoAggiornatoReimpCanc) < 0) {//fixme <0 
			Errore err = new Errore();
			err.setCodice("FIN_ERR_0040");
			err.setDescrizione(
					"La somma delle modifiche di tipo ROR - Cancellazione INS/INE deve essere essere inferiore all' Importo Massimo Cancellabile");
			listaErrori.add(err);

		}
		if (importoDaCancellare.compareTo(limiteDestroSub) > 0) {//fixme <0 
			Errore err = new Errore();
			err.setCodice("FIN_ERR_0040");
			err.setDescrizione(
					"La somma delle modifiche di tipo ROR - Cancellazione INS/INE deve essere essere inferiore al limite massimo");
			listaErrori.add(err);

		}
		
		//SIAC-7534
		//if (importoDaCancellare.compareTo(importoMassimoCancellabilePadre) < 0) {
		if (importoDaCancellare.compareTo(importoMassimoCancellabilePadre) < 0) {
			Errore err = new Errore();
			err.setCodice("FIN_ERR_0040");
			err.setDescrizione(
					"La somma delle modifiche di tipo ROR - Cancellazione INS/INE deve essere essere inferiore all' Importo Massimo Cancellabile dell' impegno padre");
			listaErrori.add(err);

		}
		if (importoDaCancellare.compareTo(limiteDestroPadre) > 0) {
			Errore err = new Errore();
			err.setCodice("FIN_ERR_0040");
			err.setDescrizione(
					"La somma delle modifiche di tipo ROR - Cancellazione INS/INE deve essere essere inferiore inferiore al limite massimo dell'impegno padre");
			listaErrori.add(err);

		}


		return listaErrori;
	}

	public static BigDecimal getImportoReimputatoAnnullato(
			List<ModificaMovimentoGestioneSpesa> modificheDaAnnullareDescImpNull,
			List<ModificaMovimentoGestioneSpesa> listaModificheOld) {
		//SIAC-8007 -Inizio   sostituisco int con BigDecimal
		BigDecimal importoAttualeNuovo = BigDecimal.ZERO;
		//int importoAttualeNuovo = 0;
		for (int i = 0; i < modificheDaAnnullareDescImpNull.size(); i++) {
			for (int j = 0; j < listaModificheOld.size(); j++) {
				if (modificheDaAnnullareDescImpNull.get(i).getTipoModificaMovimentoGestione()!=null && 
						modificheDaAnnullareDescImpNull.get(i).getTipoModificaMovimentoGestione().equals("REIMP") 
						&& modificheDaAnnullareDescImpNull.get(i).getUid() == listaModificheOld.get(j).getUid()) {
					//importoAttualeNuovo += listaModificheOld.get(j).getImportoOld().intValue();
					importoAttualeNuovo =  importoAttualeNuovo.add(listaModificheOld.get(j).getImportoOld());
				}
			}

		}
		//questo mi restituisce l'importo annullato relativo alle reimputazioni, da passare ai nuovi metodi per i controlli
		//return new BigDecimal(importoAttualeNuovo);
		return importoAttualeNuovo;

	}
	
	public static BigDecimal getImportoReimputatoAnnullatoAcc(
			List<ModificaMovimentoGestioneEntrata> modificheDaAnnullareDescImpNull,
			List<ModificaMovimentoGestioneEntrata> listaModificheOld) {
		//SIAC-8007 -Inizio   sostituisco int con BigDecimal
		BigDecimal importoAttualeNuovo = BigDecimal.ZERO;
		//int importoAttualeNuovo = 0;
		for (int i = 0; i < modificheDaAnnullareDescImpNull.size(); i++) {
			for (int j = 0; j < listaModificheOld.size(); j++) {
				if (modificheDaAnnullareDescImpNull.get(i).getTipoModificaMovimentoGestione()!=null && 
						modificheDaAnnullareDescImpNull.get(i).getTipoModificaMovimentoGestione().equals("REIMP") 
						&& modificheDaAnnullareDescImpNull.get(i).getUid() == listaModificheOld.get(j).getUid()) {
					//importoAttualeNuovo += listaModificheOld.get(j).getImportoOld().intValue();
					importoAttualeNuovo =  importoAttualeNuovo.add(listaModificheOld.get(j).getImportoOld());
				}
			}

		}
		//questo mi restituisce l'importo annullato relativo alle reimputazioni, da passare ai nuovi metodi per i controlli
		//return new BigDecimal(importoAttualeNuovo);
		return importoAttualeNuovo;

	}

	public static BigDecimal getImportoCancellatoAnnullato(
			List<ModificaMovimentoGestioneSpesa> modificheDaAnnullareDescImpNull,
			List<ModificaMovimentoGestioneSpesa> listaModificheOld) {
		//SIAC-8007 -Inizio   sostituisco int con BigDecimal
		BigDecimal importoAttualeNuovo = BigDecimal.ZERO;
		//int importoAttualeNuovo = 0;
		for (int i = 0; i < modificheDaAnnullareDescImpNull.size(); i++) {
			for (int j = 0; j < listaModificheOld.size(); j++) {
				if (modificheDaAnnullareDescImpNull.get(i).getTipoModificaMovimentoGestione()!=null && 
						(modificheDaAnnullareDescImpNull.get(i).getTipoModificaMovimentoGestione().equals("INSROR") ||
								modificheDaAnnullareDescImpNull.get(i).getTipoModificaMovimentoGestione().equals("INEROR"))
						&& modificheDaAnnullareDescImpNull.get(i).getUid() == listaModificheOld.get(j).getUid()) {
					//importoAttualeNuovo += listaModificheOld.get(j).getImportoOld().intValue();
					importoAttualeNuovo =  importoAttualeNuovo.add(listaModificheOld.get(j).getImportoOld());
				}
			}

		}
		//questo mi restituisce l'importo annullato relativo alle reimputazioni, da passare ai nuovi metodi per i controlli
		//return new BigDecimal(importoAttualeNuovo);
		return importoAttualeNuovo;
	}
	
	public static BigDecimal getImportoCancellatoAnnullatoAcc(
			List<ModificaMovimentoGestioneEntrata> modificheDaAnnullareDescImpNull,
			List<ModificaMovimentoGestioneEntrata> listaModificheOld) {
		//SIAC-8007 -Inizio   sostituisco int con BigDecimal
		BigDecimal importoAttualeNuovo = BigDecimal.ZERO;
		//int importoAttualeNuovo = 0;		
		for (int i = 0; i < modificheDaAnnullareDescImpNull.size(); i++) {
			for (int j = 0; j < listaModificheOld.size(); j++) {
				if (modificheDaAnnullareDescImpNull.get(i).getTipoModificaMovimentoGestione()!=null && 
						(modificheDaAnnullareDescImpNull.get(i).getTipoModificaMovimentoGestione().equals("INSROR") ||
								modificheDaAnnullareDescImpNull.get(i).getTipoModificaMovimentoGestione().equals("INEROR"))
						&& modificheDaAnnullareDescImpNull.get(i).getUid() == listaModificheOld.get(j).getUid()) {
					//importoAttualeNuovo += listaModificheOld.get(j).getImportoOld().intValue();
					importoAttualeNuovo =  importoAttualeNuovo.add(listaModificheOld.get(j).getImportoOld());
				}
			}

		}
		//questo mi restituisce l'importo annullato relativo alle reimputazioni, da passare ai nuovi metodi per i controlli
		//return new BigDecimal(importoAttualeNuovo);
		return importoAttualeNuovo;
	}

	public static List<Errore> getErroreDaWrapper(String descrizioneErrori, List<Errore> listErrori) {
		int start = descrizioneErrori.indexOf("{");
		int end = descrizioneErrori.indexOf("}");
		if(start <0 || end < 0) {
			return listErrori;
		}
		String errore = descrizioneErrori.substring(start, end+1);		
		int separator = errore.indexOf("-");
		String codiceErrore = errore.substring(1, separator-1).trim();
		String descrizioneErrore = errore.substring(separator+1, errore.indexOf("}")).trim();
		
		Errore err = new Errore();
		err.setCodice(codiceErrore);
		err.setDescrizione(descrizioneErrore);
		
		
		return Arrays.asList(err);
	}
	
	public static boolean checkIfHasRorm(List<ModificaMovimentoGestioneSpesa> spese) {
		for(ModificaMovimentoGestioneSpesa mmgs :spese){
			if(mmgs.getTipoModificaMovimentoGestione().equals("RORM")){
				return true;
			}
		}
		return false;
	}
	
	public static SubImpegno getMovGestPadreByNumero(List<SubImpegno> lista, String numero){
		
		for(SubImpegno sub : lista){
			if (sub.getNumeroBigDecimal().equals(numero)){
				return sub;
			}
		}
		return null;
	}

	public static List<ModificaMovimentoGestioneSpesa> speseDaValidarePerSubPropaga(
			List<ModificaMovimentoGestioneSpesa> spese) {
		List<ModificaMovimentoGestioneSpesa> speseToReturn = new ArrayList<ModificaMovimentoGestioneSpesa>();
		for(ModificaMovimentoGestioneSpesa mmgs: spese){
			if(mmgs.getUid()==0 && mmgs.getTipoMovimento().equals("SIM") && mmgs.getStatoOperativoModificaMovimentoGestione().equals(StatoOperativoModificaMovimentoGestione.VALIDO)){
				speseToReturn.add(mmgs);
			}
		}

		return speseToReturn;
	}
	
	public static List<ModificaMovimentoGestioneEntrata> speseDaValidarePerSubPropagaAcc(
			List<ModificaMovimentoGestioneEntrata> spese) {
		List<ModificaMovimentoGestioneEntrata> speseToReturn = new ArrayList<ModificaMovimentoGestioneEntrata>();
		for(ModificaMovimentoGestioneEntrata mmgs: spese){
			if(mmgs.getUid()==0 && mmgs.getTipoMovimento().equals("SAC") && mmgs.getStatoOperativoModificaMovimentoGestione().equals(StatoOperativoModificaMovimentoGestione.VALIDO)){
				speseToReturn.add(mmgs);
			}
		}

		return speseToReturn;
	}

	public static List<ModificaMovimentoGestioneSpesa> getSpeseRor(List<ModificaMovimentoGestioneSpesa> spese,
			String optional) {
		List<ModificaMovimentoGestioneSpesa> speseToReturn = new ArrayList<ModificaMovimentoGestioneSpesa>();
		for(ModificaMovimentoGestioneSpesa  mmgs : spese){
			if(mmgs.getStatoOperativoModificaMovimentoGestione().equals(StatoOperativoModificaMovimentoGestione.VALIDO) && optional.equals("") && mmgs.getTipoModificaMovimentoGestione().equals("REIMP") || mmgs.getTipoModificaMovimentoGestione().equals("INSROR") ||mmgs.getTipoModificaMovimentoGestione().equals("INEROR") ){
				speseToReturn.add(mmgs);
			}else if(mmgs.getStatoOperativoModificaMovimentoGestione().equals(StatoOperativoModificaMovimentoGestione.VALIDO) && optional.equals("RORM") && mmgs.getTipoModificaMovimentoGestione().equals("RORM")){
				speseToReturn.add(mmgs);
			}
		}
		return speseToReturn;
	}
	
	public static List<ModificaMovimentoGestioneEntrata> getSpeseRorAcc(List<ModificaMovimentoGestioneEntrata> spese,
			String optional) {
		List<ModificaMovimentoGestioneEntrata> speseToReturn = new ArrayList<ModificaMovimentoGestioneEntrata>();
		for(ModificaMovimentoGestioneEntrata  mmgs : spese){
			if(mmgs.getStatoOperativoModificaMovimentoGestione().equals(StatoOperativoModificaMovimentoGestione.VALIDO) && optional.equals("") && mmgs.getTipoModificaMovimentoGestione().equals("REIMP") || mmgs.getTipoModificaMovimentoGestione().equals("INSROR") ||mmgs.getTipoModificaMovimentoGestione().equals("INEROR") ){
				speseToReturn.add(mmgs);
			}else if(mmgs.getStatoOperativoModificaMovimentoGestione().equals(StatoOperativoModificaMovimentoGestione.VALIDO) && optional.equals("RORM") && mmgs.getTipoModificaMovimentoGestione().equals("RORM")){
				speseToReturn.add(mmgs);
			}
		}
		return speseToReturn;
	}

	public static Impegno getImpegnoByNumero(List<SubImpegno> list, Integer numeroImpegnoPadre) {
		BigDecimal numImp = new BigDecimal(numeroImpegnoPadre.toString());
		for(SubImpegno sub : list){
			if(sub.getNumeroImpegnoPadre().compareTo(numImp)==0 && sub.getNumeroBigDecimal() == null){
				return sub;
			}
		}
		return null;
	}
	
	public static Accertamento getAccertamentoByNumero(List<SubAccertamento> list, Integer numeroAccPadre) {
		BigDecimal numImp = new BigDecimal(numeroAccPadre.toString());
		for(SubAccertamento sub : list){
			if(sub.getNumeroAccertamentoPadre().compareTo(numImp)==0 && sub.getNumeroBigDecimal() == null){
				return sub;
			}
		}
		return null;
	}

	public static boolean provvedimentoBloccato(ProvvedimentoImpegnoModel provvedimento) {
		
		if(provvedimento.getBloccoRagioneria()!= null && provvedimento.getBloccoRagioneria().equals(true)){
			return true;
		}
		return false;	
	}

	public static List<ModificaMovimentoGestioneSpesa> modificheDaAnnullareComplete(
			List<ModificaMovimentoGestioneSpesa> listaModificheRor,
			List<ModificaMovimentoGestioneSpesa> listaModificheOld) {
		List<ModificaMovimentoGestioneSpesa> modificheDaAnnullareComplete = new ArrayList<ModificaMovimentoGestioneSpesa>();
		
		for(int i=0; i<listaModificheRor.size(); i++){
			for(int j=0; j<listaModificheOld.size(); j++){
				if(listaModificheRor.get(i).getUid()==(listaModificheOld.get(j).getUid())){
					modificheDaAnnullareComplete.add(listaModificheOld.get(j));
				}
			}
		}
		return modificheDaAnnullareComplete;
	}
	
	public static List<ModificaMovimentoGestioneEntrata> modificheDaAnnullareCompleteAcc(
			List<ModificaMovimentoGestioneEntrata> listaModificheRor,
			List<ModificaMovimentoGestioneEntrata> listaModificheOld) {
		List<ModificaMovimentoGestioneEntrata> modificheDaAnnullareComplete = new ArrayList<ModificaMovimentoGestioneEntrata>();
		
		for(int i=0; i<listaModificheRor.size(); i++){
			for(int j=0; j<listaModificheOld.size(); j++){
				if(listaModificheRor.get(i).getUid()==(listaModificheOld.get(j).getUid())){
					modificheDaAnnullareComplete.add(listaModificheOld.get(j));
				}
			}
		}
		return modificheDaAnnullareComplete;
	}

	public static List<Errore> checkModificaCompleta(List<Errore> erroriSalvataggio, List<GestioneCruscottoModel> listaReimputazioneTriennio,
			GestioneCruscottoModel rorCancellazioneInesigibilita, GestioneCruscottoModel rorCancellazioneInsussistenza,
			GestioneCruscottoModel rorDaMantenere, Boolean checkDaMantenere) {
		
		String zero = "0,00";
		for(GestioneCruscottoModel gcm : listaReimputazioneTriennio){
			boolean importoNullo = gcm.getImporto() != null && (gcm.getImporto().equals("") || gcm.getImporto().equals(zero));
			if(!importoNullo && gcm.getDescrizione()!=null && gcm.getDescrizione().equals("")){
				Errore err = new Errore();
				err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
				err.setDescrizione(ErroreFin.WARNING_GENERICO.getErrore("Impossibile proseguire: non &egrave stata inserita la motivazione per la modifica ROR- Reimputazione per l' anno "+gcm.getAnno().toString()).getDescrizione());
				erroriSalvataggio.add(err);
			}
			//SIAC-7502 importo maggiori di zero
			BigDecimal importoNegativoReimp;
			if(gcm.getImporto() != null){
				importoNegativoReimp = FinActionUtils.convertiImportoToBigDecimal(gcm.getImporto());
				if(importoNegativoReimp.compareTo(BigDecimal.ZERO)<0){
					Errore err = ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore("ROR - Reimputazione, anno "+gcm.getAnno().toString(),"l' importo deve essere maggiore di zero");
					erroriSalvataggio.add(err);
				}
				
			}

			////////////////////
			
		}
		boolean importoNullo = rorCancellazioneInesigibilita.getImporto() != null && (rorCancellazioneInesigibilita.getImporto().equals("") || rorCancellazioneInesigibilita.getImporto().equals(zero));
		if(!importoNullo && rorCancellazioneInesigibilita.getDescrizione()!=null && rorCancellazioneInesigibilita.getDescrizione().equals("")){
			Errore err = new Errore();
			err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
			err.setDescrizione(ErroreFin.WARNING_GENERICO.getErrore("Impossibile proseguire: non &egrave stata inserita la motivazione per la modifica ROR-Cancellazione Inesigibilit&agrave;").getDescrizione());
			erroriSalvataggio.add(err);
		}
		//SIAC-7502 importo maggiori di zero
		BigDecimal importoNegativoInes;
		if(rorCancellazioneInesigibilita.getImporto() != null){
			importoNegativoInes = FinActionUtils.convertiImportoToBigDecimal(rorCancellazioneInesigibilita.getImporto());
			if(importoNegativoInes.compareTo(BigDecimal.ZERO)<0){
				Errore err = ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore("ROR - Cancellazione per Inesigibilit&agrave;","l' importo deve essere maggiore di zero");
				erroriSalvataggio.add(err);
			}
			
		}

		////////////////////
		importoNullo = rorCancellazioneInsussistenza.getImporto() != null && (rorCancellazioneInsussistenza.getImporto().equals("") || rorCancellazioneInsussistenza.getImporto().equals(zero));
		if(!importoNullo && rorCancellazioneInsussistenza.getDescrizione()!=null && rorCancellazioneInsussistenza.getDescrizione().equals("")){
			Errore err = new Errore();
			err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
			err.setDescrizione(ErroreFin.WARNING_GENERICO.getErrore("Impossibile proseguire: non &egrave stata inserita la motivazione per la modifica ROR-Cancellazione Insussistenza").getDescrizione());
			erroriSalvataggio.add(err);
		}
		//SIAC-7502 importo maggiori di zero
		BigDecimal importoNegativoInsus;
		if(rorCancellazioneInsussistenza.getImporto() != null){
			importoNegativoInsus = FinActionUtils.convertiImportoToBigDecimal(rorCancellazioneInsussistenza.getImporto());
			if(importoNegativoInsus.compareTo(BigDecimal.ZERO)<0){
				Errore err = ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore("ROR - Cancellazione per Insussistenza;","l' importo deve essere maggiore di zero");
				erroriSalvataggio.add(err);
			}
			
		}
		if(checkDaMantenere!= null && checkDaMantenere==true && rorDaMantenere.getDescrizione().equals("")){
			Errore err = new Errore();
			err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
			err.setDescrizione(ErroreFin.WARNING_GENERICO.getErrore("Impossibile proseguire: non &egrave stata inserita la motivazione per la modifica ROR-Da Mantenere").getDescrizione());
			erroriSalvataggio.add(err);
		}
		return erroriSalvataggio;
		

	}

	public static List<Errore> checkModificheCompleteInAgg(List<Errore> erroriSalvataggio,
			List<ModificaMovimentoGestioneSpesa> modificheDaAggiornare) {
		BigDecimal zero = new BigDecimal("0.00");
		
		for(ModificaMovimentoGestioneSpesa mmgs: modificheDaAggiornare){
			String desc = mmgs.getDescrizioneModificaMovimentoGestione();
			String desc1 = mmgs.getDescrizione();
			if(mmgs.getImportoOld() != null && mmgs.getImportoOld().compareTo(zero)!=0 && desc !=null && desc.equals("")){
				if(mmgs.getTipoModificaMovimentoGestione().equals("REIMP")){
					Errore err = new Errore();
					err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
					err.setDescrizione(ErroreFin.WARNING_GENERICO.getErrore("Impossibile proseguire: non &egrave stata inserita la motivazione per la modifica ROR- Reimputazione per l''anno "+mmgs.getAnnoReimputazione().toString()).getDescrizione());
					erroriSalvataggio.add(err);
					
					
				}else if(mmgs.getTipoModificaMovimentoGestione().equals("INEROR")){
					Errore err = new Errore();
					err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
					err.setDescrizione(ErroreFin.WARNING_GENERICO.getErrore("Impossibile proseguire: non &egrave stata inserita la motivazione per la modifica ROR-Cancellazione Inesigibilit&agrave;").getDescrizione());
					erroriSalvataggio.add(err);
					//FIX 7502 importi in cancellazione negativi
					
					
				}else if(mmgs.getTipoModificaMovimentoGestione().equals("INSROR")){
					Errore err = new Errore();
					err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
					err.setDescrizione(ErroreFin.WARNING_GENERICO.getErrore("Impossibile proseguire: non &egrave stata inserita la motivazione per la modifica ROR-Cancellazione Insussistenza").getDescrizione());
					erroriSalvataggio.add(err);
				}
	
			}
			
				
			//FIX 7502 importi in aggiornamenti per cancellazione negativi
			if(mmgs.getImportoOld() != null && mmgs.getImportoOld().compareTo(zero)<0){
				if(mmgs.getTipoModificaMovimentoGestione().equals("INEROR")){
					Errore err = ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore("ROR - Cancellazione per Inesigibilit&agrave;","l' importo deve essere maggiore di zero");
					erroriSalvataggio.add(err);	
				}else if(mmgs.getTipoModificaMovimentoGestione().equals("INSROR")){
					Errore err = ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore("ROR - Cancellazione per Insussistenza","l' importo deve essere maggiore di zero");
					erroriSalvataggio.add(err);
				}
				else if(mmgs.getTipoModificaMovimentoGestione().equals("REIMP")){
					Errore err = ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore("ROR - Reimputazione anno "+mmgs.getAnnoReimputazione().toString(),"l' importo deve essere maggiore di zero");
					erroriSalvataggio.add(err);
				}
	
			}
			
			
	
		}
		
		
		
		
		
		return erroriSalvataggio;
	}

	public static List<Errore> checkModificheCompleteInAggAcc(List<Errore> erroriSalvataggio,
			List<ModificaMovimentoGestioneEntrata> modificheDaAggiornare) {
		BigDecimal zero = new BigDecimal("0.00");
		
		for(ModificaMovimentoGestioneEntrata mmgs: modificheDaAggiornare){
			String desc = mmgs.getDescrizioneModificaMovimentoGestione();
			String desc1 = mmgs.getDescrizione();
			if(mmgs.getImportoOld() != null && mmgs.getImportoOld().compareTo(zero)!=0 && desc !=null && desc.equals("")){
				if(mmgs.getTipoModificaMovimentoGestione().equals("REIMP")){
					Errore err = new Errore();
					err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
					//SIAC-7349 - gestito nullPointerException
					if (mmgs.getAnnoReimputazione() !=null) {
						err.setDescrizione(ErroreFin.WARNING_GENERICO.getErrore("Impossibile proseguire: non &egrave stata inserita la motivazione per la modifica ROR- Reimputazione per l''anno "+mmgs.getAnnoReimputazione().toString()).getDescrizione());
					}else {
						err.setDescrizione(ErroreFin.WARNING_GENERICO.getErrore("Impossibile proseguire: non &egrave stata inserita la motivazione per la modifica ROR- Reimputazione per l''anno ").getDescrizione());	
					}
					erroriSalvataggio.add(err);
					
					
				}else if(mmgs.getTipoModificaMovimentoGestione().equals("INEROR")){
					Errore err = new Errore();
					err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
					err.setDescrizione(ErroreFin.WARNING_GENERICO.getErrore("Impossibile proseguire: non &egrave stata inserita la motivazione per la modifica ROR-Cancellazione Inesigibilit&agrave;").getDescrizione());
					erroriSalvataggio.add(err);
					
				}else if(mmgs.getTipoModificaMovimentoGestione().equals("INSROR")){
					Errore err = new Errore();
					err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
					err.setDescrizione(ErroreFin.WARNING_GENERICO.getErrore("Impossibile proseguire: non &egrave stata inserita la motivazione per la modifica ROR-Cancellazione Insussistenza").getDescrizione());
					erroriSalvataggio.add(err);
				}
	
			}
			
			//FIX 7502 importi in aggiornamenti per cancellazione negativi
			if(mmgs.getImportoOld() != null && mmgs.getImportoOld().compareTo(zero)<0){
				if(mmgs.getTipoModificaMovimentoGestione().equals("INEROR")){
					Errore err = ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore("ROR - Cancellazione per Inesigibilit&agrave;","l' importo deve essere maggiore di zero");
					erroriSalvataggio.add(err);	
				}else if(mmgs.getTipoModificaMovimentoGestione().equals("INSROR")){
					Errore err = ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore("ROR - Cancellazione per Insussistenza","l' importo deve essere maggiore di zero");
					erroriSalvataggio.add(err);
				}
				else if(mmgs.getTipoModificaMovimentoGestione().equals("REIMP")){
					Errore err = ErroreCore.IMPORTI_NON_VALIDI_PER_ENTITA.getErrore("ROR - Reimputazione anno "+mmgs.getAnnoReimputazione().toString(),"l' importo deve essere maggiore di zero");
					erroriSalvataggio.add(err);
				}
	
			}
	
		}

		return erroriSalvataggio;
	}

	public static List<Errore> checkSingolaModificaCompleta(List<Errore> erroriSalvataggio, GestioneCruscottoModel inesigibilita, GestioneCruscottoModel insussistenza,
			GestioneCruscottoModel rorDaMantenere, Boolean checkDaMantenere) {
		
		boolean notHasImporto = true;

		if(insussistenza != null){
			notHasImporto = insussistenza.getImporto()!= null && (insussistenza.getImporto().equals("") || insussistenza.getImporto().equals("0,00"));
			if(!notHasImporto && insussistenza.getDescrizione()!= null && insussistenza.getDescrizione().equals("")){
				Errore err = new Errore();
				err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
				err.setDescrizione(ErroreFin.WARNING_GENERICO.getErrore("Impossibile proseguire: non &egrave stata inserita la motivazione per la modifica ROR-Cancellazione Insussistenza").getDescrizione());
				erroriSalvataggio.add(err);
				return erroriSalvataggio;
				
			}else{
				return erroriSalvataggio;
			}
			
			
		}else if(inesigibilita != null){
			notHasImporto = inesigibilita.getImporto()!= null && (inesigibilita.getImporto().equals("") || inesigibilita.getImporto().equals("0,00"));
			if(!notHasImporto && inesigibilita.getDescrizione()!= null && inesigibilita.getDescrizione().equals("")){
				Errore err = new Errore();
				err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
				err.setDescrizione(ErroreFin.WARNING_GENERICO.getErrore("Impossibile proseguire: non &egrave stata inserita la motivazione per la modifica ROR-Cancellazione Inesigibilit&agrave;").getDescrizione());
				erroriSalvataggio.add(err);
				return erroriSalvataggio;
			}else{
				return erroriSalvataggio;
			}
			
		}else if(rorDaMantenere != null && checkDaMantenere!=null && checkDaMantenere==true){
			if(rorDaMantenere.getDescrizione()!= null && rorDaMantenere.getDescrizione().equals("")){
				Errore err = new Errore();
				err.setCodice(ErroreFin.WARNING_GENERICO.getCodice());
				err.setDescrizione(ErroreFin.WARNING_GENERICO.getErrore("Impossibile proseguire: non &egrave stata inserita la motivazione per la modifica ROR-Da Mantenere").getDescrizione());
				erroriSalvataggio.add(err);
				return erroriSalvataggio;
			}else{
				return erroriSalvataggio;
			}
			
			
		}

		// TODO Auto-generated method stub
		return erroriSalvataggio;
	}

	public static String codePdc(String codPdc) {
		char ch = codPdc.charAt(0);
        if(Character.isDigit(ch)){
        	return (codPdc.substring(0,4));//Sachin  
        }else{
        	return (codPdc.substring(2,6));
        	
        }

	}

	//SIAC-7968  
	public static MotiviMantenimentoROR[] filterByPdc(MotiviMantenimentoROR[] values, String codicePdc) {
		List<MotiviMantenimentoROR> lista = new ArrayList<MotiviMantenimentoROR>();
		
		for(int i=0; i<values.length-1; i++){
			String codice = values[i].getPdc();
			if(codice.equals(codicePdc)){
				lista.add(values[i]);
			}	
		}
		MotiviMantenimentoROR[] listToReturn = new MotiviMantenimentoROR[lista.size()];
		return lista.toArray(listToReturn);
	}

	public static MotiviReimputazioneROR[] filterByPdc(MotiviReimputazioneROR[] values, String codicePdc) {
		List<MotiviReimputazioneROR> lista = new ArrayList<MotiviReimputazioneROR>();
		
		for(int i=0; i<values.length-1; i++){
			String codice = values[i].getPdc();
			if(codice.equals(codicePdc)){
				lista.add(values[i]);
			}	
		}
		MotiviReimputazioneROR[] listToReturn = new MotiviReimputazioneROR[lista.size()];
		return lista.toArray(listToReturn);
	}

	public static MotiviCancellazioneROR[] filterByPdc(MotiviCancellazioneROR[] values, String codicePdc) {
		List<MotiviCancellazioneROR> lista = new ArrayList<MotiviCancellazioneROR>();
		
		for(int i=0; i<values.length-1; i++){
			String codice = values[i].getPdc();
			if(codice.equals(codicePdc)){
				lista.add(values[i]);
			}	
		}
		MotiviCancellazioneROR[] listToReturn = new MotiviCancellazioneROR[lista.size()];
		return lista.toArray(listToReturn);
	}

	//SIAC-7968  Inizio  
	public static MotiviMantenimentoAccROR[] filterAccByPdc(MotiviMantenimentoAccROR[] values, String codicePdc) {
		List<MotiviMantenimentoAccROR> lista = new ArrayList<MotiviMantenimentoAccROR>();
		
		for(int i=0; i<values.length-1; i++){
			String codice = values[i].getPdc();
			if(codice.equals(codicePdc)){
				lista.add(values[i]);
			}	
		}
		MotiviMantenimentoAccROR[] listToReturn = new MotiviMantenimentoAccROR[lista.size()];
		return lista.toArray(listToReturn);
	}

	public static MotiviReimputazioneAccROR[] filterAccByPdc(MotiviReimputazioneAccROR[] values, String codicePdc) {
		List<MotiviReimputazioneAccROR> lista = new ArrayList<MotiviReimputazioneAccROR>();
		
		for(int i=0; i<values.length-1; i++){
			String codice = values[i].getPdc();
			if(codice.equals(codicePdc)){
				lista.add(values[i]);
			}	
		}
		MotiviReimputazioneAccROR[] listToReturn = new MotiviReimputazioneAccROR[lista.size()];
		return lista.toArray(listToReturn);
	}

	public static MotiviCancellazioneAccROR[] filterAccByPdc(MotiviCancellazioneAccROR[] values, String codicePdc) {
		List<MotiviCancellazioneAccROR> lista = new ArrayList<MotiviCancellazioneAccROR>();
		
		for(int i=0; i<values.length-1; i++){
			String codice = values[i].getPdc();
			if(codice.equals(codicePdc)){
				lista.add(values[i]);
			}	
		}
		MotiviCancellazioneAccROR[] listToReturn = new MotiviCancellazioneAccROR[lista.size()];
		return lista.toArray(listToReturn);
	}
	//SIAC-7968  Fine

	/**
	 * Estraggo l'importo delle modifiche pre-esistenti per le quali vi e' stata una sola modifica della descrizione.
	 *
	 * @param nuoveDaRicreare the nuove da ricreare
	 * @param listaModificheOld the lista modifiche old
	 * @return the importo modifiche con sola descrizione modificata
	 */
	//SIAC-8093
	public static BigDecimal getImportoModificheConSolaDescrizioneModificataEntrata(List<ModificaMovimentoGestioneEntrata> nuoveDaRicreare, List<ModificaMovimentoGestioneEntrata> listaModificheOld) {
		
		BigDecimal importoModificheConSolaDescrizioneModificata = BigDecimal.ZERO;
		
		for (ModificaMovimentoGestioneEntrata modificaAggiornata : nuoveDaRicreare) {
			
			String descrizioneModificaMovimentoGestione = modificaAggiornata.getDescrizioneModificaMovimentoGestione();
			BigDecimal importoModifica = modificaAggiornata.getImportoOld();
			//se l'importo e' stato sbiancato, la modifica sara' annullata. un cambiamento di stato non e' un cambiamento di sola descrizione, non lo considero.
			if((importoModifica == null || BigDecimal.ZERO.compareTo(importoModifica) == 0) && StringUtils.isBlank(descrizioneModificaMovimentoGestione)) {
				continue;
			}
			//prendo la modifica prima che arrivasse all'utente
			ModificaMovimentoGestioneEntrata modificaPrecedente = extractModificaEntrata(modificaAggiornata.getUid(), listaModificheOld);
			
			if(isModificaAggiornataNellaSolaDescrizione(modificaPrecedente, importoModifica, descrizioneModificaMovimentoGestione)) {
				importoModificheConSolaDescrizioneModificata = importoModificheConSolaDescrizioneModificata.add(importoModifica);
			}
		}
		return importoModificheConSolaDescrizioneModificata;
	}
	
	
	/**
	 * Estraggo l'importo delle modifiche pre-esistenti per le quali vi e' stata una sola modifica della descrizione.
	 *
	 * @param nuoveDaRicreare the nuove da ricreare
	 * @param listaModificheOld the lista modifiche old
	 * @return the importo modifiche con sola descrizione modificata
	 */
	//SIAC-8093
	public static BigDecimal getImportoModificheConSolaDescrizioneModificataSpesa(List<ModificaMovimentoGestioneSpesa> nuoveDaRicreare, List<ModificaMovimentoGestioneSpesa> listaModificheOld) {
		
		BigDecimal importoModificheConSolaDescrizioneModificata = BigDecimal.ZERO;
		
		for (ModificaMovimentoGestioneSpesa modificaAggiornata : nuoveDaRicreare) {
			
			String descrizioneModificaMovimentoGestione = modificaAggiornata.getDescrizioneModificaMovimentoGestione();
			BigDecimal importoModifica = modificaAggiornata.getImportoOld();
			//se l'importo e' stato sbiancato, la modifica sara' annullata. un cambiamento di stato non e' un cambiamento di sola descrizione, non lo considero.
			if((importoModifica == null || BigDecimal.ZERO.compareTo(importoModifica) == 0) && StringUtils.isBlank(descrizioneModificaMovimentoGestione)) {
				continue;
			}
			//prendo la modifica prima che arrivasse all'utente
			ModificaMovimentoGestioneSpesa modificaPrecedente = extractModificaSpesa(modificaAggiornata.getUid(), listaModificheOld);
			
			if(isModificaAggiornataNellaSolaDescrizione(modificaPrecedente, importoModifica, descrizioneModificaMovimentoGestione)) {
				importoModificheConSolaDescrizioneModificata = importoModificheConSolaDescrizioneModificata.add(importoModifica);
			}
			
		}
		return importoModificheConSolaDescrizioneModificata;
	}
	
	
	/**
	 * Gets the importo.
	 *
	 * @param modificaPrecedente the modifica precedente
	 * @param importoModificaAggiornato the importo modifica
	 * @param descrizioneModificaAggiornata the descrizione modifica movimento gestione
	 * @return the importo
	 */
	private static boolean isModificaAggiornataNellaSolaDescrizione(ModificaMovimentoGestione modificaPrecedente, BigDecimal importoModificaAggiornato, String descrizioneModificaAggiornata) {
		if(modificaPrecedente == null) {
			return false;
		}
		
		//estraggo i dati (per ora posso cambiare solo importo e descrizione, quindi prendo solo questi due)
		BigDecimal importoModificaOld = modificaPrecedente.getImportoOld();
		String descrizioneModificaMovimentoGestioneOld = modificaPrecedente.getDescrizioneModificaMovimentoGestione();
		
		//metto il valore assoluto. Questa gestione non ottimale e' coerente con tutto il resto.
		boolean importoCambiato=importoModificaOld!= null && importoModificaOld.abs().compareTo(importoModificaAggiornato) != 0;
		boolean descrizioneCambiata = descrizioneModificaMovimentoGestioneOld != null && !descrizioneModificaMovimentoGestioneOld.equals(descrizioneModificaAggiornata);
		//se l'importo non e' cambiato ma la descrizione si, allora considero la modifica
		return !importoCambiato && descrizioneCambiata; 
	}

	private static ModificaMovimentoGestioneEntrata extractModificaEntrata(Integer uid, List<ModificaMovimentoGestioneEntrata> listaModifiche) {
		if(uid == null || uid.intValue() == 0) {
			return null;
		}
		
		for (ModificaMovimentoGestioneEntrata mod : listaModifiche) {
			if(mod != null && mod.getUid() == uid) {
				return mod;
			}
			
		}
		return null;
	}
	
	private static ModificaMovimentoGestioneSpesa extractModificaSpesa(Integer uid, List<ModificaMovimentoGestioneSpesa> listaModifiche) {
		if(uid == null || uid.intValue() == 0) {
			return null;
		}
		
		for (ModificaMovimentoGestioneSpesa mod : listaModifiche) {
			if(mod != null && mod.getUid() == uid) {
				return mod;
			}
			
		}
		return null;
	}

	

}

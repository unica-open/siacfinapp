/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfinapp.frontend.ui.model.ordinativo.DettaglioDocumentoModel;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.MovimentoKey;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * 
 * utility varie che vengono richiamate da finapp 
 * 
 * @author paolos
 *
 */
public final class FinUtility {
	
	protected static LogUtil log = new LogUtil(FinUtility.class);
	
	/**
	 * Metodo di comodo per i controlli formali, verifica se il movimento passato e' stato popolat o meno.
	 * Si basa sulla presenza del numero e dell'anno
	 * @param mg
	 * @return
	 */
	public final static boolean isEmpty(MovimentoGestione mg){
		boolean vuoto = true;
		if(mg!=null && mg.getAnnoMovimento()>0 && maggioreDiZero(mg.getNumeroBigDecimal())){
			vuoto = false;
		}
		return vuoto;
	}
	
	public final static boolean maggioreDiZero(Number numero){
		return numero!=null && numero.intValue()>0;
	}
	
	public final static List<SubAccertamento> rimuoviSubAccAnnullati(List<SubAccertamento> elencoSubAccertamenti){
		List<SubAccertamento> senzaAnnullati = new ArrayList<SubAccertamento>();
		if(elencoSubAccertamenti!=null && elencoSubAccertamenti.size()>0){
			for(int i = 0; i < elencoSubAccertamenti.size(); i++){
				SubAccertamento subImp = clone(elencoSubAccertamenti.get(i));
				if(!subImp.getStatoOperativoMovimentoGestioneEntrata().equals(CostantiFin.MOVGEST_STATO_ANNULLATO)){
					senzaAnnullati.add(subImp);
				}
			}
		}
		return senzaAnnullati;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public final static List<Ordinativo> rimuoviOrdinativiAnnullati(List<Ordinativo> lista){
		List<Ordinativo> senzaAnnullati = new ArrayList<Ordinativo>();
		if(!FinUtility.isEmpty(lista)){
			for(Ordinativo it: lista){
				if(it!=null && !CostantiFin.D_ORDINATIVO_STATO_ANNULLATO.equals(it.getStatoOperativoOrdinativo())){
					senzaAnnullati.add(clone(it));
				}
			}
		}
		return senzaAnnullati;
	}
	
	public final static DettaglioDocumentoModel documentoSpesaToDettaglioModel(DocumentoSpesa doc){
		DettaglioDocumentoModel docModel = new DettaglioDocumentoModel();
		docModel.setDataEmissioneDocumento(doc.getDataEmissione());
		docModel.setNumeroDocumento(doc.getNumero());
		docModel.setDescrizioneDocumento(doc.getDescrizione());
		docModel.setImportoDocumento(doc.getImporto());
		return docModel;
	}
	
	public static <T extends Object> List<T> getElementiNonNulli(List<T> list){
		List<T> nonNulli = null;
		int numeroNonNulli = numeroElementiNonNulli(list);
		if(numeroNonNulli>0){
			nonNulli = new ArrayList<T>(numeroNonNulli);
			for(T it : list){
				if(it!=null){
					nonNulli.add(it);
				}
			}
		}
		return nonNulli;
	}
	
	public final static List<DocumentoSpesa> ordinaPerNumero(List<DocumentoSpesa> elenco){
		
		List<DocumentoSpesa> listaNonNulli = getElementiNonNulli(elenco);
		
		if(listaNonNulli!=null && listaNonNulli.size()>0){
			
			Collections.sort(listaNonNulli, new Comparator<DocumentoSpesa>() {
				
				@Override
				public int compare(DocumentoSpesa o1, DocumentoSpesa o2) {
					
					String numeroUno = o1.getNumero();
					String numeroDue = o2.getNumero();
					
					return numeroUno.compareTo(numeroDue);
						
				}
			});
		}
		return listaNonNulli;
	}
	
	public final static List<DocumentoSpesa> distintiDocumentiDellOrdinativo(OrdinativoPagamento ordPag){
		List<DocumentoSpesa> distintiDoc = null;
		if(ordPag!=null){
			distintiDoc = distintiDocumentiBySubOrds(ordPag.getElencoSubOrdinativiDiPagamento());
			distintiDoc = ordinaPerNumero(distintiDoc);
		}
		return distintiDoc;
	}
	
	public final static List<DocumentoSpesa> distintiDocumentiBySubOrds(List<SubOrdinativoPagamento> subOrds){
		List<DocumentoSpesa> distintiDoc = null;
		if(!FinStringUtils.isEmpty(subOrds)){
			List<SubdocumentoSpesa> subDocs =  estraiSubDoc(subOrds);
			distintiDoc = distintiDocumentiBySubDocs(subDocs);
		}
		return distintiDoc;
	}
	
	public final static List<DocumentoSpesa> distintiDocumentiBySubDocs(List<SubdocumentoSpesa> subDocs){
		List<DocumentoSpesa> distintiDoc = null;
		if(!FinStringUtils.isEmpty(subDocs)){
			distintiDoc = new  ArrayList<DocumentoSpesa>();
			ArrayList<String> giaAggiunti = new ArrayList<String>();
			for(SubdocumentoSpesa subDocIt: subDocs){
				if(subDocIt!=null && subDocIt.getDocumento()!=null){
					if(!giaAggiunti.contains(subDocIt.getDocumento().getNumero())){
						distintiDoc.add(subDocIt.getDocumento());
						giaAggiunti.add(subDocIt.getDocumento().getNumero());
					}
				}
			}
		}
		return distintiDoc;
	}
	
	public final static List<SubdocumentoSpesa> estraiSubDoc(List<SubOrdinativoPagamento> subOrds){
		List<SubdocumentoSpesa> subDocs = null;
		if(!FinStringUtils.isEmpty(subOrds)){
			subDocs = new ArrayList<SubdocumentoSpesa>();
			for(SubOrdinativoPagamento subOrdIt : subOrds){
				if(subOrdIt!=null && subOrdIt.getSubDocumentoSpesa()!=null){
					subDocs.add(subOrdIt.getSubDocumentoSpesa());
				}
			}
		}
		return subDocs;
	}
	
	
//		SubOrdinativoPagamento subOrd = ordinativoTrovato.getElencoSubOrdinativiDiPagamento().get(0);
//		SubdocumentoSpesa subDoc = subOrd.getSubDocumentoSpesa();
//		if(subDoc!=null && subDoc.getDocumento()!=null){
//			DocumentoSpesa doc = subDoc.getDocumento();
//	}
	
	public final static List<SubImpegno> rimuoviAnnullati(List<SubImpegno> elencoSubImpegni ){
		List<SubImpegno> senzaAnnullati = new ArrayList<SubImpegno>();
		if(elencoSubImpegni!=null && elencoSubImpegni.size()>0){
			for(int i = 0; i < elencoSubImpegni.size(); i++){
				SubImpegno subImp = clone(elencoSubImpegni.get(i));
				if(!subImp.getStatoOperativoMovimentoGestioneSpesa().equals(CostantiFin.MOVGEST_STATO_ANNULLATO)){
					senzaAnnullati.add(subImp);
				}
			}
		}
		return senzaAnnullati;
	}
	
	public final static List<SubAccertamento> impostaCapitoloDaAccertamentoSeAssente(List<SubAccertamento> elencoSubAccertamenti, Accertamento accertamento){
		if(elencoSubAccertamenti!=null && elencoSubAccertamenti.size()>0 && accertamento!=null && accertamento.getCapitoloEntrataGestione()!=null){
			for(SubAccertamento subAcc: elencoSubAccertamenti){
				if(subAcc.getCapitoloEntrataGestione()==null){
					subAcc.setCapitoloEntrataGestione(accertamento.getCapitoloEntrataGestione());
				}
			}
		}
		return elencoSubAccertamenti;
	}
	
	public final static List<SubImpegno> impostaCapitoloDaImpegnoSeAssente(List<SubImpegno> elencoSubImpegni, Impegno impegno){
		if(elencoSubImpegni!=null && elencoSubImpegni.size()>0 && impegno!=null && impegno.getCapitoloUscitaGestione()!=null){
			for(SubImpegno subImp: elencoSubImpegni){
				if(subImp.getCapitoloUscitaGestione()==null){
					subImp.setCapitoloUscitaGestione(impegno.getCapitoloUscitaGestione());
				}
			}
		}
		return elencoSubImpegni;
	}
	
	
	public final static String formatDataDdMmYy(Date data){
		try{
			DateFormat df = new SimpleDateFormat("dd/MM/YYYY");
			return df.format(data);
		}catch (Exception e){
			//meglio una data vuota che l'applicativo bloccato
			return "";
		}
	}
	
	public final static <T extends Object> boolean haUnSoloElementoNonNullo(List<T> lista){
		if(lista!=null && lista.size()==1 && lista.get(0)!=null){
			return true;
		} else {
			return false;
		}
	}
	
	
//	/**
//	 * 
//	 * tra la lista delle azioni consentite ritorna true/false a
//	 * seconda che sia presente una particolare azione passata
//	 * come parametro
//	 * @param azioneCercata
//	 * @param azioniConsentite
//	 * 
//	 * @return boolean
//	 */
//	public static boolean azioneConsentitaIsPresent(String azioneCercata, 
//													List<AzioneConsentita> azioniConsentite){
//		final String methodName = "azioneConsentitaIsPresent";
//		boolean ris = false;
//		log.debug(methodName, "Entro in azioneConsentitaIsPresent !!!!!");
//
//		for (AzioneConsentita azioneConsentita : azioniConsentite) {
//
//			if(azioneCercata.equalsIgnoreCase(azioneConsentita.getAzione().getNome())){
//				
//				ris = true;
//			}
//		}
//		
//		return ris;
//		
//	}
	
//	/**
//	 * 
//	 * tra la lista delle azioni consentite ritorna l'oggetto relativo all'azione consentita
//	 * 
//	 * @param azioniConsentite
//	 * @param azioneCercata
//	 * @return AzioneConsentita
//	 */
//	public static AzioneConsentita azionedConsentitaIsPresentObj(List<AzioneConsentita> azioniConsentite, String azioneCercata) {
//		AzioneConsentita azioneConsentita = null;
//		for (AzioneConsentita azioneConsentitaCurrent : azioniConsentite) {
//			if(azioneCercata.equalsIgnoreCase(azioneConsentitaCurrent.getAzione().getNome())){
//				azioneConsentita = azioneConsentitaCurrent;
//				break;
//			}
//		}
//		return azioneConsentita;
//	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> T clone(T source) {
        return (T)deserialize(serialize(source));
	}

	/**
	 * Serializza l'oggetto sullo stream
	 * 
	 * @param obj
	 * @param outputStream
	 * @throws Exception
	 */
	public static void serialize(Object obj, OutputStream outputStream) {
		if (outputStream == null) {
			throw new IllegalArgumentException("The OutputStream must not be null");
		}
		
		try{
			ObjectOutputStream out = new ObjectOutputStream(outputStream);
			try {
				out.writeObject(obj);
			} finally {
				try {
					out.close();
				} catch (IOException exIgnored) {
					// printStackTrace
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

    /**
     * Serializza l'oggetto trasformandolo in un array di byte
     * 
     * @param obj
     * @return
     * @throws Exception
     */
    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        try{
        	serialize(obj, baos);
        	return baos.toByteArray();
        }finally{
        	try {
				baos.close();
			} catch (IOException eIgnored) {
				// printStackTrace
			}
        }
    }

    /**
     * Deserializza un oggetto a partire dallo stream
     * 
     * @param inputStream
     * @return
     * @throws ClassNotFoundException 
     * @throws Exception
     */
    public static Object deserialize(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("The InputStream must not be null");
        }
		try {
			ObjectInputStream in = new ObjectInputStream(inputStream);
			try {
				return in.readObject();
			} finally {
				try {
					in.close();
				} catch (IOException eIgnored) {
					// printStackTrace
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
    }

    /**
     * Deserializza un oggetto a partire dall'array di byte
     * 
     * @param objectData
     * @return
     * @throws Exception
     */
    public static Object deserialize(byte[] objectData) {
        if (objectData == null) {
            throw new IllegalArgumentException("The byte[] must not be null");
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
        try{
        	return deserialize(bais);
        }finally{
        	try {
				bais.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
        }
    }
   
    public final static <T extends Entita> boolean presenteInLista(List<T> lista, Integer id){
    	T finded = getById(lista, id);
    	if(finded!=null){
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public final static <T extends Entita> List<T> soloValidi(List<T> lista, Timestamp ts){
		List<T> validi = new ArrayList<T>();
		if(lista!=null && lista.size()>0){
			for(T it : lista){
				Timestamp dataFineValidita = DateUtility.convertiDataInTimeStamp(it.getDataFineValidita());
				if((dataFineValidita==null || dataFineValidita.after(ts))){
					validi.add(it);
				}
			}
		}
		return validi;
	}
    
	public final static <T extends Entita> List<T> rimuoviConIdZero(List<T> lista){
		return removeById(lista, 0);
	}
	
	public final static <T extends Entita> List<T> removeById(List<T> lista, Integer id){
		List<T> listaRitorno = null;
		if(id!=null && lista!=null && lista.size()>0){
			listaRitorno = new ArrayList<T>();
			for(T iterato : lista){
				if(iterato!=null && iterato.getUid()!=id.intValue()){
					listaRitorno.add(clone(iterato));
				}
			}	
		}
		return listaRitorno;
	}
	
	/**
	 * Utility che data una lista di CodificaFin ritorna una lista contenente gli 
	 * elementi in input tranne quelli il cui codice e' uguale a quelli indicati
	 * @param lista
	 * @param codiciDaRimuovere
	 * @return
	 */
	public final static List<CodificaFin> removeByCodes(List<CodificaFin> lista, String ... codiciDaRimuovere){
		List<CodificaFin> listaRitorno = clone(lista);
		if(!isEmpty(lista) && codiciDaRimuovere!=null && codiciDaRimuovere.length>0){
			for(String it: codiciDaRimuovere){
				listaRitorno = removeByCode(listaRitorno, it);
			}
		}
		return listaRitorno;
	}
	
	/**
	 * Utility che data una lista di CodificaFin ritorna una lista contenente gli 
	 * elementi in input tranne quelli il cui codice e' uguale a quello indicato
	 * @param lista
	 * @param codiceDaRimuovere
	 * @return
	 */
	public final static List<CodificaFin> removeByCode(List<CodificaFin> lista, String codiceDaRimuovere){
		List<CodificaFin> listaRitorno = new ArrayList<CodificaFin>();
		if(!isEmpty(lista) && !FinStringUtils.isEmpty(codiceDaRimuovere)){
			for(CodificaFin it: lista){
				if(it!=null && !codiceDaRimuovere.equals(it.getCodice())){
					listaRitorno.add(clone(it));
				}
			}
		}
		return listaRitorno;
	}
    
    public final static <T extends Entita> T getById(List<T> lista, String idString){
    	T finded = null;
    	if(idString!=null){
    		idString = idString.trim();
    		Integer id = Integer.parseInt(idString);
    		finded = getById(lista, id);
    	}
    	return finded;
    }
	
	public final static <T extends Entita> T getById(List<T> lista, Integer id){
		if(lista!=null && lista.size()>0){
			for(T iterato : lista){
				if(iterato!=null && id!=null && iterato.getUid()==id.intValue()){
					return iterato;
				}
			}	
		}
		return null;
	}
	
	public final static <T extends Entita> List<T> getById(List<T> lista, String[] listaId){
		List<T> trovati = null;
		if(lista!=null && lista.size()>0 && listaId!=null && listaId.length>0){
			trovati = new ArrayList<T>();
			for(String idIt : listaId){
				T finded = getById(lista, idIt);
				trovati.add(finded);
			}
		}
		return trovati;
	}
	
	public static ArrayList<ModalitaPagamentoSoggetto> filtraValidi(List<ModalitaPagamentoSoggetto> lista){
		ArrayList<ModalitaPagamentoSoggetto> listMod= new ArrayList<ModalitaPagamentoSoggetto>();
		
		//step 1: filtro in base alla data di fine validita
		Timestamp now=  new Timestamp(System.currentTimeMillis());
		ArrayList<ModalitaPagamentoSoggetto> listaDataValida = (ArrayList<ModalitaPagamentoSoggetto>) FinUtility.soloValidi(lista, now);
		
		//step 2: ulteriore filtro in base allo stato:
		if(listaDataValida!= null && listaDataValida.size()>0){
			for(ModalitaPagamentoSoggetto modPag:listaDataValida){
				if(modPag!=null && CostantiFin.STATO_VALIDO.equalsIgnoreCase(modPag.getDescrizioneStatoModalitaPagamento())){
					modPag.setDescrizioneStatoModalitaPagamento(modPag.getDescrizioneStatoModalitaPagamento().toUpperCase());
					listMod.add(modPag);
				}
			}
		}
		return listMod;
	}
	
	public static boolean isValida(ModalitaPagamentoSoggetto mps){
		if(mps==null){
			return false;
		}
		ArrayList<ModalitaPagamentoSoggetto> lista = new ArrayList<ModalitaPagamentoSoggetto>();
		lista.add(mps);
		ArrayList<ModalitaPagamentoSoggetto> validi = filtraValidi(lista);
		boolean valida = false;
		if(validi!=null && validi.size()>0){
			valida = true;
		}
		return valida;
	}
	
	public static boolean isMDPNonScaduta(ModalitaPagamentoSoggetto mdp){

		if(mdp!=null){

			Timestamp now=  new Timestamp(System.currentTimeMillis());

			Timestamp dataScadenza = DateUtility.convertiDataInTimeStamp(mdp.getDataScadenza());

			if((dataScadenza==null || dataScadenza.after(now))){
				return true;
			}
			else
				return false;

		}
		return true;
	}
	
	
	public static  ArrayList<ModalitaPagamentoSoggetto> soloMDPNonScadute(List<ModalitaPagamentoSoggetto> lista){
		
		ArrayList<ModalitaPagamentoSoggetto> elencoMDPNonScadute = new ArrayList<ModalitaPagamentoSoggetto>();
		
		if(lista!=null && !lista.isEmpty()){
			
			for(ModalitaPagamentoSoggetto mdp : lista){

				if(isMDPNonScaduta(mdp)){
					elencoMDPNonScadute.add(mdp);
				}
			}
		}
		return elencoMDPNonScadute;
	}
	
	public static <S extends SubOrdinativo> BigDecimal sommaImportiAttuali(List<S> listaSubOrd){
		//sommatoria parte a zero
		BigDecimal sommatoria = BigDecimal.ZERO;
		if(!isEmpty(listaSubOrd)){
			//ci sono sub ordinativi
			for(S sub : listaSubOrd){
				//ciclando i sub ordinativo
				if(sub!=null && sub.getImportoAttuale()!=null){
					//aggiungo l'importo iterato in sommatoria
					sommatoria = sommatoria.add(sub.getImportoAttuale());
				}
			}
			
		}
		return sommatoria;
	}
	
	/**
	 * Cerca nella lista tipi indicata l'elemento col codice da ricercare
	 * @param codice
	 * @param listaTipi
	 * @return
	 */
	public static TipoCausale tipoCausaleByCodice(String codice,List<TipoCausale> listaTipi){
		TipoCausale trovato = null;
		if(listaTipi!=null && listaTipi.size()>0){
			//ok ci sono elementi
			for(TipoCausale it: listaTipi){
				if(it!=null && it.getCodice()!=null && it.getCodice().equals(codice)){
					//trovato con lo stesso codice
					trovato = it;
					break;
				}
			}
		}
		return trovato;
	}
	
	/**
	 * Cerca nella lista causali indicata l'elemento col codice da ricercare
	 * @param codice
	 * @param listaCausali
	 * @return
	 */
	public static CausaleEntrata causaleByCodice(String codice,List<CausaleEntrata> listaCausali){
		CausaleEntrata trovato = null;
		if(listaCausali!=null && listaCausali.size()>0){
			//ok ci sono elementi
			for(CausaleEntrata it: listaCausali){
				if(it!=null && it.getCodice()!=null && it.getCodice().equals(codice)){
					//trovato con lo stesso codice
					trovato = it;
					break;
				}
			}
		}
		return trovato;
	}
	
	public static int getUidByCode(List<CodificaFin> lista, String code){
		int uid = 0;
		CodificaFin codifica = getByCode(lista, code);
		if(codifica!=null){
			uid = codifica.getUid();
		}
		return uid;
	}
	
	public static CodificaFin getByCode(List<CodificaFin> lista, String code){
		CodificaFin trovato = null;
		if(!isEmpty(lista) && code!=null){
			//se gli oggetti sono valorizzati
			for(CodificaFin it : lista){
				//controllo che sia uguale:
				if(it!=null && it.getCodice()!=null && it.getCodice().equals(code)){
					//trovato
					trovato = it;
					break;
				}
			}
		}
		return trovato;
	}
	
	/**
	 * se c'e' deve essere lungo 10 alfanumerici
	 * @param cig
	 * @return
	 */
	public static boolean cigValido(String cig){
		boolean valido = true;
		// lungo esattamente 10
		if(cig != null && StringUtils.isNotEmpty(cig) && cig.trim().length()!=10){
			valido = false;
		}
		return valido;
	}
	
	/**
	 * Il codice deve essere di 15 caratteri alfanumerici, 
	 * con il primo e quarto carattere che devono essere una lettera.
	 * @param cup
	 * @return
	 */
	public static boolean cupValido(String cup){
		
		boolean valido = true;
		if(cup != null && StringUtils.isNotEmpty(cup) && cup.trim().length()==15){
			for (int i=0; i<cup.length(); i++) {
				if(i==0 || i==3){
					char carattere = cup.charAt(i);
					if(StringUtils.isNumeric(String.valueOf(carattere))){
						valido = false;
						break;
					}
				}
			}
		}else if(cup != null && StringUtils.isNotEmpty(cup) && cup.trim().length()!=15){
			// se diverso da lunghezza 15 cmq errore
			valido = false;
		}
		return valido;
	}
	

	/**
	 * una lista e' considerata non vuota quando ha almeno un elemento non nullo
	 * @param list
	 * @return
	 */
	public static <T extends Object> boolean isEmpty(List<T> list){
		if(numeroElementiNonNulli(list)>0){
			return false;
		} else {
			return true;
		}
	}
	
	public final static <T extends Object> T getFirst(List<T> lista){
		if(lista!=null && lista.size()>0){
			return lista.get(0);
		}
		return null;
	}

	/**
	 * Ritorna l'anno di una data
	 * @param data
	 * @return
	 */
	public static int getYear(Date data){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		return calendar.get(Calendar.YEAR);
	}


	public static <T extends Object> int numeroElementiNonNulli(List<T> list){
		int numero = 0;
		if(list!=null && list.size()>0){
			int nonNulli = 0;
			for(T it : list){
				if(it!=null){
					nonNulli++;
				}
			}
			numero = nonNulli;
		}
		return numero;
	}

	
	public static boolean stessoAnno(Date data, int anno){
		boolean stessoAnno = false;
		if(data!=null){
			int annoData = getYear(data);
			if(annoData==anno){
				stessoAnno = true;
			}
		}
		return stessoAnno;
	}
	
	/**
	 * Ritorna null se non e' numero
	 * @param s
	 * @return
	 */
	public static Integer parseNumeroIntero(String s) {
		Integer numero = null;
		if(isNumeroIntero(s)){
			numero=Integer.parseInt(s); 
		}
		return numero;
	}
	
	/**
	 * Ritorna 0 se non e' numero
	 * @param s
	 * @return
	 */
	public static int parseNumeroInt(String s) {
		int numero = 0;
		if(isNumeroIntero(s)){
			numero=Integer.parseInt(s); 
		}
		return numero;
	}
	
	public static boolean isNumeroIntero(String numero) {
		boolean isNumero = false;
		if(!FinStringUtils.isEmpty(numero)){
			try  {
				Integer.parseInt(numero); 
				isNumero = true;
			}catch (Exception t){
				isNumero = false;
			}
		}
		return isNumero;
	}
	
	public static boolean isNullOrZero(Number numero){
		return numero == null || (numero != null && numero.intValue()==0);
	}
	
	public static boolean isNumero(String numero) {
		boolean isNumero = false;
		if(!FinStringUtils.isEmpty(numero)){
			try  {
				BigDecimal numeroBD = new BigDecimal(numero);
				if(numeroBD!=null){
					isNumero = true;
				}
			}catch (Exception t){
				isNumero = false;
			}
		}
		return isNumero;
	}

	public final static <T extends Object> List<T> toList(T ... oggetto){
		List<T> l = new ArrayList<T>();
		if(oggetto!=null && oggetto.length>0){
			for(T it: oggetto){
				if(it!=null){
					l.add(it);
				}
			}
		}
		return l;
	}
	
	public final static <T extends Object> List<T> toList(List<T> ... liste){
		List<T> l = new ArrayList<T>();
		if(liste!=null && liste.length>0){
			for(List<T> listIt : liste){
				if(listIt!=null && listIt.size()>0){
					for(T it: listIt){
						if(it!=null){
							l.add(it);
						}
					}
				}
			}
		}
		return l;
	}
	
	/**
	 * Data una lista di oggetti che estendono Entita restituisce una 
	 * semplice lista degli id di tali oggetti
	 * @param lista
	 * @return
	 */
	public final static <T extends Entita> List<Integer> getIdList(List<T> lista){
		List<Integer> listaId = null;
		if(lista!=null && lista.size()>0){
			listaId = new ArrayList<Integer>();
			for(T iterato : lista){
				if(iterato!=null){
					listaId.add(iterato.getUid());
				}
			}	
		}
		return listaId;
	}
	
	/**
	 * Metodo di comodo per trovare il sub con il numero indicato da una lista
	 * 
	 * Ritorna null se non presente.
	 * 
	 * @param lista
	 * @param numero
	 * @return
	 */
	public final static SubImpegno findSubImpegnoByNumero(List<SubImpegno> lista, Integer numero){
		SubImpegno trovato = null;
		if(!isEmpty(lista) && numero!=null){
			//se gli oggetti sono valorizzati
			for(SubImpegno it : lista){
				//controllo che sia uguale:
				if(it!=null && it.getNumeroBigDecimal()!=null && numero.intValue() == it.getNumeroBigDecimal().intValue()){
					//trovato
					trovato = it;
					break;
				}
			}
		}
		return trovato;
	}
	

	
	public final static  BigDecimal minTraBD(BigDecimal primo, BigDecimal secondo){
		BigDecimal ris = BigDecimal.ZERO;
		
		if(primo.compareTo(secondo)>0){
			
			ris = secondo;
			
		}else if(primo.compareTo(secondo)<0){
			ris = primo;
		}else{
			// se sono uguali uno qualsiasi
			ris = primo;
		}
		
		return ris;
	}
	
	public final static List<Integer> listaIds(List<CodificaFin> listaCodifiche){
		List<Integer> ids = new ArrayList<Integer>();
		if(!FinStringUtils.isEmpty(listaCodifiche)){
			for(CodificaFin it: listaCodifiche){
				if(it!=null){
					ids.add(it.getUid());
				}
			}
		}
		return ids;
	}
	
	public final static List<String> listaCodici(List<CodificaFin> listaCodifiche){
		List<String> ids = new ArrayList<String>();
		if(!FinStringUtils.isEmpty(listaCodifiche)){
			for(CodificaFin it: listaCodifiche){
				if(it!=null && !FinStringUtils.isEmpty(it.getCodice())){
					ids.add(it.getCodice());
				}
			}
		}
		return ids;
	}
	
	public final static boolean sonoUguali(MovimentoKey key1, MovimentoKey key2){
		boolean uguali = false;
		if(!isEmpty(key1) && !isEmpty(key2)){
			if(FinStringUtils.sonoUgualiAncheNull(key1.getAnnoMovimento(), key2.getAnnoMovimento()) && 
					FinStringUtils.sonoUgualiAncheNull(key1.getNumeroMovimento(), key2.getNumeroMovimento()) && 
					FinStringUtils.sonoUgualiAncheNull(key1.getAnnoEsercizio(), key2.getAnnoEsercizio())){
				
				//FIN QUI IL MOVIMENTO E' LO STESSO, VEDIAMO IL SUB EVENTUALE:
				
				boolean unoSiDueNo = isSub(key1) && !isSub(key2);
				boolean unoNoDueSi = !isSub(key1) && isSub(key2);
				if(unoSiDueNo || unoNoDueSi){
					//se uno e' sub e l'altro no --> non sono uguali
					return false;
				}
				
				boolean tuttiEDueSub = isSub(key1) && isSub(key2);
				if(tuttiEDueSub){
					//se tutti e due sono sub deve essere lo stesso sub:
					return FinStringUtils.sonoUgualiAncheNull(key1.getNumeroSubMovimento(), key2.getNumeroSubMovimento());
				}
				
				
				//se sono qui non sono sub nessuno dei due quindi sono uguali
				uguali = true;
			}
		}
		return uguali;
	}
	
	public final static boolean isSub(MovimentoKey mk){
		boolean isSub = false;
		if(!isEmpty(mk) && maggioreDiZero(mk.getNumeroSubMovimento())){
			isSub = true;
		}
		return isSub;
	}
	
	/**
	 * Metodo di comodo per i controlli formali, verifica se il movimento passato e' stato popolato o meno.
	 * Si basa sulla presenza del numero e dell'anno
	 * @param mk
	 * @return
	 */
	public final static boolean isEmpty(MovimentoKey mk){
		boolean vuoto = true;
		if(mk!=null && mk.getAnnoMovimento()>0 && maggioreDiZero(mk.getNumeroMovimento()) && maggioreDiZero(mk.getAnnoEsercizio())){
			vuoto = false;
		}
		return vuoto;
	}
	
	/**
	 * Metodo creato perche' addll modifica la listaTo il che in alcuni casi non va bene
	 */
	public final static <T extends Object> List<T> addAllConNew(List<T> listaTo,List<T> listaFrom){
		
		ArrayList<T> listaAll = new ArrayList<T>();
		
		if(listaTo!=null && listaTo.size()>0){
			for(T it : listaTo){
				if(it!=null){
					T nuovoOggetto = it;
					listaAll.add(nuovoOggetto);
				}
			}
		}
		if(listaFrom!=null && listaFrom.size()>0){
			for(T it : listaFrom){
				if(it!=null){
					T nuovoOggetto = it;
					listaAll.add(nuovoOggetto);
				}
			}
		}
		
		//Termino restituendo l'oggetto di ritorno: 
        return listaAll;
	}

	/**
	 * Versione null safe del classico addAll di java util list
	 * @param listaTo
	 * @param listaFrom
	 * @return
	 */
	public final static <T extends Object> List<T> addAll(List<T> listaTo,List<T> listaFrom){
		if(listaTo==null){
			listaTo = new ArrayList<T>();
		}
		if(listaFrom !=null && listaFrom.size()>0){
			listaTo.addAll(listaFrom);
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaTo;
	}
	
	public final static List<Ordinativo> rimuoviOrdinativiDiPagamento(List<Ordinativo> lista){
		List<Ordinativo> filtrati = new ArrayList<Ordinativo>();
		if(!FinStringUtils.isEmpty(lista)){
			for(Ordinativo it: lista){
				if(!instanceofPagamento(it)){
					filtrati.add(clone(it));
				}
			}
		}
		return filtrati;
	}
	
	public final static List<Ordinativo> rimuoviOrdinativiDiIncasso(List<Ordinativo> lista){
		List<Ordinativo> filtrati = new ArrayList<Ordinativo>();
		if(!FinStringUtils.isEmpty(lista)){
			for(Ordinativo it: lista){
				if(!instanceofIncasso(it)){
					filtrati.add(clone(it));
				}
			}
		}
		return filtrati;
	}
	
	public final static boolean instanceofPagamento(Ordinativo it){
		if(it!=null){
			return (it instanceof OrdinativoPagamento) || (it instanceof SubOrdinativoPagamento);
		} else {
			return false;
		}
	}
	
	public final static boolean instanceofIncasso(Ordinativo it){
		if(it!=null){
			return (it instanceof OrdinativoIncasso) || (it instanceof SubOrdinativoIncasso);
		} else {
			return false;
		}
	}
	
	public final static boolean haImpegnoOrSubImpegno(PreDocumentoCarta rigaCarta){
		return haImpegno(rigaCarta) || haSubImpegno(rigaCarta);
	}
	
	public final static boolean haSubImpegno(PreDocumentoCarta rigaCarta){
		return rigaCarta!=null && isValorizzato(rigaCarta.getSubImpegno());
	}
	
	public final static boolean haImpegno(PreDocumentoCarta rigaCarta){
		return rigaCarta!=null && isValorizzato(rigaCarta.getImpegno());
	}
	
	public final static boolean isValorizzato(MovimentoGestione mg){
		return mg!=null && mg.getNumeroBigDecimal()!=null && mg.getNumeroBigDecimal().longValue()>0;
	}
	
	
	public final static Boolean bloccataDaRagioneria(List<AttoAmministrativo> attiAmministrativi){
		Boolean result = Boolean.FALSE;
		for(int i=0;i<attiAmministrativi.size();i++){
			if(attiAmministrativi.get(i).getDataCancellazione()== null && attiAmministrativi.get(i).getBloccoRagioneria()!= null && 
					attiAmministrativi.get(i).getBloccoRagioneria().booleanValue()){
				result = Boolean.TRUE;
				break;
			}
		}
		
		return result;
	}
	
	/** SIAC-7349
	 * Restituisce l'ImportoCapitolo avente anno pari a quanto passato in input.
	 * @param <T> la tipizzazione degli importi del capitolo
	 * 
	 * @param listaImportiCapitolo la lista da cui ottenere l'importo
	 * @param anno                 l'anno dell'importo da ottenere
	 * 
	 * @return l'importo corrispondente all'anno, se presente; <code>null</code> altrimenti
	 */
	public static <T extends ImportiCapitolo> T searchByAnno(List<T> listaImportiCapitolo, Integer anno) {
		// Fallback per la nullità della lista
		if (listaImportiCapitolo == null || anno == null) {
			return null;
		}

		// Inizializzo a null il risultato
		T result = null;

		for (T t : listaImportiCapitolo) {
			// Controllo l'anno di competenza
			if (t != null && anno.equals(t.getAnnoCompetenza())) {
				// Importo trovato: lo imposto nel risultato
				result = t;
				break;
			}
		}

		return result;
	}

	
	
	
	public final static String importoFormatter(BigDecimal importo){
		 DecimalFormat dFormat = new DecimalFormat("#,###,##0.00");
		 dFormat = (DecimalFormat)NumberFormat.getNumberInstance(Locale.ITALY);
			dFormat.setParseBigDecimal(true);
			dFormat.setMinimumFractionDigits(2);
			dFormat.setMaximumFractionDigits(2);
			String str = dFormat.format(importo);
			
			return str;
			
	}
	
	
	
	
}

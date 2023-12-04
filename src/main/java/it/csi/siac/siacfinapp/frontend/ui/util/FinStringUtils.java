/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import it.csi.siac.siacfinser.CostantiFin;

public class FinStringUtils {
	
	/**
	 * Data una stringa va a pescare la prima occorrenza dentro delle parantesi quadre,
	 * assume che tale occorrenza sia un intero, se cosi non fosse torna -1
	 * 
	 * Es: per stringa = reintroitoOrdinativoStep2Model.listOrdInc[3].importo"
	 * 
	 * Ritorna 3
	 * 
	 * @param stringa
	 * @return
	 */
	public static int substringTraQuadreInt(String stringa){
		int indiceCoinvolto = -1;
		String indiceString = FinStringUtils.substringTraQuadre(stringa);
		if(FinUtility.isNumeroIntero(indiceString)){
			indiceCoinvolto = FinUtility.parseNumeroInt(indiceString);
		}
		return indiceCoinvolto;
	}
	
	/**
	 * Ritorna la prima sub string trovata tra parentesi quadre, se non trova nulla torna null
	 * 
	 * Es: per stringa = reintroitoOrdinativoStep2Model.listOrdInc[3].importo"
	 * 
	 * Ritorna "3"
	 * 
	 * @param stringa
	 * @return
	 */
	public static String substringTraQuadre(String stringa){
		return substringTraCaratteri('[', ']', stringa);
	}
	
	/**
	 * ritorna la prima substring dentro i due caratteri indicati
	 * @param carattereInizio
	 * @param carattereFine
	 * @param stringa
	 * @return
	 */
	public static String substringTraCaratteri(char carattereInizio, char carattereFine, String stringa){
		String subString = null;
		if(stringa!=null){
			int inizio = stringa.indexOf(carattereInizio);
			if(inizio!=-1){
				inizio = inizio +1;
			}
			int fine = stringa.indexOf(carattereFine);
			if(inizio!=-1 && fine!=-1 && fine>inizio){
				subString = stringa.substring(inizio, fine);
			}
		}
		return subString;
	}
	
	/**
	 * Ritorna true se la stringa s e' uguale ad almeno
	 * un elemento in list rispetto al confronto di tipo equalsIgnoreCase.
	 * 
	 * Da utilizzare al posto di condizioni di tanti equalsIgnoreCase in OR
	 * per stare bono sonar quando che dice che non puoi usare troppe condizioni nelle if
	 * 
	 * @param s
	 * @param list
	 * @return
	 */
	public static boolean equalsIgnoreCaseSuAlmenoUno(String s, String ... list){
		if(s!=null && list!=null){
			for(String it: list){
				if(it!=null){
					if(it.equalsIgnoreCase(s)){
						return true;
					}
				}
			}
		}
		return false;
	}

	public static int length(String s){
		int length = 0;
		if(s!=null){
			length = s.length();
		}
		return length;
	}
	
	/**
	 * puo essere utile per controllare due campi 
	 * che devono essere valorizzati in mutua esclusione oppure entrambi
	 * @param s1
	 * @param s2
	 * @return
	 */
	public final static boolean entrambiValorizzati(String s1, String s2) {
		if(!isEmpty(s1) && !isEmpty(s2)){
			return true;
		}
		return false;
	}
	
	/**
	 * puo essere utile per controllare due campi che devono o non devono essere entrambi vuoti
	 * @param s1
	 * @param s2
	 * @return
	 */
	public final static boolean entrambiVuoti(String s1, String s2) {
		if(isEmpty(s1) && isEmpty(s2)){
			return true;
		}
		return false;
	}
	
	public final static boolean isEmpty(String s) {
		if (s == null){
			return true;
		} else{
			return "".equals(s.trim());
		}
	}
	
	public static <T extends Object> boolean isEmpty(List<T> list){
		if(numeroElementi(list)>0){
			return false;
		} else {
			return true;
		}
	}
	
	public static <T extends Object> int numeroElementi(List<T> list){
		int numero = 0;
		if(list!=null){
			numero = list.size();
		}
		return numero;
	}
	
	
	public static Number getZeroIfNull(Number a){
		Number valore = 0;
		if(a!=null){
			valore = a.intValue(); 
		}
		return valore;
	}
	
	/**
	 * Ritorna true se entrambi diversi da null e' con lo stesso intValue
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean sonoUgualiInt(Number a, Number b){
		boolean uguali = false;
		if(a!=null && b != null){
			if(a.intValue()==b.intValue()){
				 uguali = true;
			}
		}
		return uguali;
		
	}
	
	
	public static boolean sonoUgualiAncheNull(Integer s1,Integer s2){
		if(s1==null && s2!=null){
			return false;
		}
		if(s1!=null && s2==null){
			return false;
		}
		if(s1==null && s2==null){
			return true;
		}
		return s1.equals(s2);
	}
	
	
	public static boolean sonoUgualiAncheNull(BigDecimal s1,BigDecimal s2){
		if(s1==null && s2!=null){
			return false;
		}
		if(s1!=null && s2==null){
			return false;
		}
		if(s1==null && s2==null){
			return true;
		}
		return s1.equals(s2);
	}
	
	/**
	 * Ritorna true se entrambi diversi da null e' con lo stesso intValue
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean sonoUgualiInt(Number a, String b){
		boolean uguali = false;
		if(a!=null && b != null && FinUtility.isNumeroIntero(b)){
			if(a.intValue()==Integer.valueOf(b).intValue()){
				 uguali = true;
			}
		}
		return uguali;
	}
	
	public static boolean sonoUgualiIntConNullUguale(Number s1,Number s2){
		if(s1==null && s2!=null){
			return false;
		}
		if(s1!=null && s2==null){
			return false;
		}
		if(s1==null && s2==null){
			return true;
		}
		return sonoUgualiInt(s1, s2);
	}
	
	public static boolean sonoUgualiTrimmed(String s1,String s2){
		if(isEmpty(s1) && !isEmpty(s2)){
			return false;
		}
		if(!isEmpty(s1) && isEmpty(s2)){
			return false;
		}
		if(isEmpty(s1) && isEmpty(s2)){
			return true;
		}
		s1 = s1.trim();
		s2 = s2.trim();
		return s1.equalsIgnoreCase(s2);
	}
	
	public static boolean sonoUguali(String s1,String s2){
		if(isEmpty(s1) && !isEmpty(s2)){
			return false;
		}
		if(!isEmpty(s1) && isEmpty(s2)){
			return false;
		}
		if(isEmpty(s1) && isEmpty(s2)){
			return true;
		}
		return s1.equalsIgnoreCase(s2);
	}
	
	public final static boolean stringToBooleanForDb(String s) {
		if (CostantiFin.TRUE.equals(s)) {
			return true;
		}
		
		return false;
	}
	
	public final static boolean contieneSoloNumeri(String s) {
		boolean contieneSoloNumeri = false;
		if(!isEmpty(s) && s.length()>0){
			contieneSoloNumeri = true;
			String support = s.trim();
			for (int i = 0; i < support.length(); i++){
				if (!Character.isDigit(support.charAt(i))){
					return false;
		        }
			}
		}
	    return contieneSoloNumeri;
	}
	
	public static String smartConcat(String separatore, String s1, String s2){
		String concatenate = "";
		if(!isEmpty(s1)){
			concatenate = concatenate + s1;
		}
		if(!isEmpty(s2)){
			if(!isEmpty(concatenate)){
				concatenate = concatenate + separatore;
			}
			concatenate = concatenate + s2;
		}
		concatenate = concatenate.trim();
		return concatenate;
	}
	
	public static String smartConcatMult(String separatore, String ... s2){
		String concatenate = "";
		for(String iterata: s2){
			concatenate = smartConcat(separatore, concatenate, iterata);
		}
		return concatenate;
	}
	
	public final static List<String> getListByToken(String stringa, String token){
		String[] array = getArrayByToken(stringa, token);
		return arrayToArrayList(array);
	}
	
	/**
	 * Esegue il tokenizer sulla stringa rispetto al token indicato e restituisce
	 * i vari token in un array di stringhe
	 * @param stringa
	 * @param token
	 * @return
	 */
	public final static String[] getArrayByToken(String stringa, String token){
		String tokens[] = null;
		if(stringa == null || token == null)
			return null;
		if(token.length() >= stringa.length()){
			if(token.equals(stringa)){
				return null;
			}
			tokens = new String[1];
			tokens[0] = stringa;
			return tokens;
		} 
		StringTokenizer st = new StringTokenizer(stringa, token);
		if(st!=null && st.countTokens()>0){
			tokens = new String[st.countTokens()];
			int i=0;
			while(st.hasMoreTokens()){
				tokens[i] = st.nextToken();
				i++;
			}
		}
		return tokens;
	}
	
	public static ArrayList<String> arrayToArrayList(String[] parole){
		 ArrayList<String> list = new ArrayList<String>();
		 if(parole!=null && parole.length>0){
			 for(int i=0;i<parole.length;i++){
				 list.add(parole[i]);
			 }
		 }
		 return list;
	}
	
	public static boolean contenutoIn(String s, String ... list){
		for(String it: list){
			if(sonoUguali(s, it)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean contenutoIn(String s, List<String> list){
		if(!isEmpty(list)){
			for(String it: list){
				if(sonoUguali(s, it)){
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean contenutoIn(Integer valore, List<Integer> list){
		if(!isEmpty(list) && valore!=null){
			for(Integer it: list){
				if(it!=null && it.intValue()==valore.intValue()){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Utility per scrivere velocemente le liste di stringhe
	 * @param s
	 * @return
	 */
	public static List<String> toList(String ... s){
		 ArrayList<String> list = new ArrayList<String>();
		 if(s!=null && s.length>0){
			 for(int i=0;i<s.length;i++){
				 if(s!=null){
					 list.add(s[i]);
				 }
			 }
		 }
		 return list;
	}
	
}

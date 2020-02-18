/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
  * Descrizione: Classe incaricata al calcolo del codice fiscale
  *
  * Autore: Marco 'RootkitNeo' C.
  *
  *         Distribuito entro i termini della licenza GNU/GPL v.3
  *
  */
package it.csi.siac.siacfinapp.frontend.ui.util.codicefiscale;



import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;


/**
 * usato per la generazione di un CF a partire dai
 * dati standard : nome, cognome, data di nascita e comune
 * @author 
 *
 */
public class CFGenerator {
	
	
  // Variabili di istanza
  // -------------------------------------------------------------------------------------------------------------------------------------------------------------
  private String nome, cognome, comune, m, sesso;
  private int anno, giorno;
  
  // Array statici
  private final char[] elencoPari = {'0','1','2','3','4','5','6','7','8','9','A','B',
                                    'C','D','E','F','G','H','I','J','K','L','M','N',
                                    'O','P','Q','R','S','T','U','V','W','X','Y','Z'
                                };
                                   
  private final int[] elencoDispari= {1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 1, 0, 5, 7, 9, 13,
                                15, 17, 19, 21, 2, 4, 18, 20, 11, 3, 6, 8, 12, 14, 16,
                                10, 22, 25, 24, 23
                               };
  
  private final String[][] mese = { {"1","A"},
                                    {"2","B"},
                                    {"3","C"},
                                    {"4","D"},
                                    {"5","E"},
                                    {"6","H"},
                                    {"7","L"},
                                    {"8","M"},
                                    {"9","P"},
                                    {"10","R"},
                                    {"11","S"},
                                    {"12","T"}
                                  };
  // --------------------------------------------------------------------------------------------------------------------------------------------------------------
  
  
  // Inizializza le variabili di istanza della classe
  // --------------------------------------------------------------------------------------------------------------------------------------------------------------
  public CFGenerator(String nome, String cognome, String comune, String m, int anno, int giorno,String sesso) {
    this.nome = nome;
    this.cognome = cognome;
    this.comune = comune;
    this.m = m;
    this.anno = anno;
    this.giorno = giorno;
    this.sesso = sesso;
    
  } 
  
  
  /**
   * costruttore vuoto che mi serve per scorporare i valori
   */
  public CFGenerator(){
	  
  }
  
  
  
  // Fine costruttore
  
  
  
  // -----------------------------------------------------------------------------------------------------------------------------------------------------------------
  
  
  // Metogi getter per ottenere gli elementi della classe
  // Interfacce piu' comode ed ordinate per l'accesso alle funzionalita'
  // -----------------------------------------------------------------------------------------------------------------------------------------------------------------
  String getNome() {
    return modificaNC(nome,true);
  }
  String getCognome() {
    return modificaNC(cognome,false);
  }
  
  String getNomeInserito() {
    return nome;
  }
  String getCognomeInserito() {
    return cognome;
  }
  String getMese() {
    return modificaMese();
  }
  String getMeseInserito() {
    return m;
  }
  String getAnno() {
	String a = new String();
	int modAnno = anno%100;
	
	if(modAnno<9){
		//aggiungo lo zero davati
		a = "0"+modAnno+"";
	}else{
	
		a = ""+modAnno+"";
	}
    return a;
  }
  int getAnnoInserito() {
    return anno;
  }
  String getGiorno() {
  String gg = new String();
  gg = (sesso.equals("M")) ? (giorno >= 10 ? ""+giorno+"" : "0"+giorno) : ""+(giorno+40);
    return gg;
  }
  int getGiornoInserito() {
    return giorno;
  }
  String getComune() {
    return elaboraCodiceComune();
  }
  String getCodice() {
    return calcolaCodice();
  }
  String getCodiceFiscale() {
    return toString();
  }
  // -----------------------------------------------------------------------------------------------------------------------------------------------------------
  
  
  // I seguenti metodi svolgono le operazioni specifiche sui dati
  
  /**
       * @param stringa                  Corrisponde al nome/cognome da modificare
       * @param cod                       Se cod e' true, indica il nome; altrimenti il cognome
       * @return nuovaStringa       Restituisce la stringa modificata
       */
  // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  private String modificaNC(String stringa, boolean cod) {
    String nuovastringa = "";
    stringa = stringa.replaceAll(" ", "");           // Rimuovo eventuali spazi
    stringa = stringa.toLowerCase();
    
    String consonanti = getConsonanti(stringa);      // Ottengo tutte le consonanti e tutte le vocali della stringa
    String vocali = getVocali(stringa);
    
    // Controlla i possibili casi
    if(consonanti.length() == 3) {                   
      // La stringa contiene solo 3 consonanti, quindi ho gia' la modifica
      nuovastringa = consonanti;
    } else if((consonanti.length() < 3) && (stringa.length() >= 3)) {
      // Le consonanti non sono sufficienti, e la stinga e' piu' lunga o
      // uguale a 3 caratteri [aggiungo le vocali mancanti]
      nuovastringa = consonanti;
      nuovastringa = aggiungiVocali(nuovastringa, vocali);
    } else if((consonanti.length() < 3) && (stringa.length() < 3)) {
      // Le consonanti non sono sufficienti, e la stringa 
      //contiene meno di 3 caratteri [aggiungo consonanti e vocali, e le x]
      nuovastringa = consonanti;
      nuovastringa += vocali;
      nuovastringa = aggiungiX(nuovastringa);
    } 
                                                     // Le consonanti sono in eccesso, prendo solo le 
                                                     //prime 3 nel caso del cognome; nel caso del nome la 0, 2, 3
    else if(consonanti.length() > 3) {
      // true indica il nome e false il cognome
      if(!cod) {
    	  nuovastringa = consonanti.substring(0,3);
      }else {
    	  nuovastringa = consonanti.charAt(0) +""+ consonanti.charAt(2) +""+ consonanti.charAt(3);
      }
    }
    
    return nuovastringa;
  }
  // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  
  
  // Aggiunge le X sino a raggiungere una lunghezza complessiva di 3 caratteri
  // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  private String aggiungiX(String stringa) {
    while(stringa.length() < 3) {
      stringa += "x";
    }
    return stringa;
  }
  // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  
  // Aggiunge le vocali alla stringa passata per parametro
  // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  private String aggiungiVocali(String stringa, String vocali) {
    int index = 0;
    while(stringa.length() < 3) {
      stringa += vocali.charAt(index);
      index++;
    }
    return stringa; 
  }
  // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  
  // Toglie dalla stringa tutte le consonanti
  // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  private String getVocali(String stringa) {
    stringa = stringa.replaceAll("[^aeiou]", "");
    return stringa;
  }
  // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  
  // Toglie dalla stringa tutte le vocali
  // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  private String getConsonanti(String stringa) {
    stringa = stringa.replaceAll("[aeiou]","");
    return stringa;
  }
  // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  
  // Restituisce il codice del mese
  // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  private String modificaMese() {
    for(int i=0; i<mese.length; i++) {
      if(mese[i][0].equalsIgnoreCase(m)) {
    	  return mese[i][1];
      }
    }
    return null;
  }
  // ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  
  // Elabora codice del comune
  // ------ --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  private String elaboraCodiceComune() {
  String cc="";
    try {
         
      // Calcola leggo RS 
      InputStream is = this.getClass().getResourceAsStream("/Comuni.txt");
      
      //Calcola leggo da scanner
      Scanner scanner = new Scanner(is);
      //Calcola valore
      scanner.useDelimiter("\r\n");
      //Calcola leggi comuni e cerca 
      while(scanner.hasNext()) {
        String s1 = scanner.nextLine();
        String s2 = s1.substring(0,s1.indexOf('-')-1);
        
        if(s2.equalsIgnoreCase(comune)) {
          cc = s1.substring(s1.lastIndexOf(' ')+1);
        }
      }
      
      scanner.close();
    } catch(Exception e) {
    }
    return cc;
  }
  // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 
  // Calcolo del Codice di Controllo
  // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  public String calcolaCodice() {
    String str = getCognome().toUpperCase()+getNome().toUpperCase()+getAnno()+getMese()+getGiorno()+getComune();
    
    int pari=0,dispari=0;
    
    for(int i=0; i<str.length(); i++) {
      char ch = str.charAt(i);              
      // i-esimo carattere della stringa
      
      // Il primo carattere e' il numero 1 non 0
      if((i+1) % 2 == 0) {
        int index = Arrays.binarySearch(elencoPari,ch);
        pari += (index >= 10) ? index-10 : index;
      } else {
        int index = Arrays.binarySearch(elencoPari,ch);
        dispari += elencoDispari[index];
      }
    }
    
    int controllo = (pari+dispari) % 26;
    controllo += 10;
    return elencoPari[controllo]+"";
  }
  // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  
  // Viene richiamato per una stampa
  public String toString() {
    return getCognome().toUpperCase()+getNome().toUpperCase()+getAnno()+getMese()+getGiorno()+getComune()+getCodice();
  }
  
  
  public boolean verificaFormaleCodiceFiscale(String cf){
	  
	  
	  return cf.matches("^[A-Z]{6}[0-9LMNPQRSTUV]{2}[A-Z][0-9LMNPQRSTUV]{2}[A-Z][0-9LMNPQRSTUV]{3}[A-Z]$");
	  
  }
  
 public boolean verificaFormaleCodiceFiscaleNumerico(String cf){
	  
	  
	  return cf.matches("^[0-9]{11}$");
	  
  }
  
  /**
   * dato un cf ne estrae la data di nascita il sesso e la citta'
   * 
   * @param cf
 * @throws Exception 
   */
  public String scorporaCF(String cf) throws Exception{

	  
	 if(!(verificaFormaleCodiceFiscale(cf) || verificaFormaleCodiceFiscaleNumerico(cf))){
		 
		 throw new Exception("Codice fiscale non formalmente valido");
	
	 }
	
	 cf = new OmocodiaUtil().convertToStandard(cf);
	 
	// levo le prime sei lettere
	cf = cf.substring(6);
	
	
	String anno = cf.substring(0,2);

	String mese = cf.substring(2,3);

	String giorno = cf.substring(3,5);

	String sesso = cf.substring(3,5);

	String comune = cf.substring(5,9);
	
	
	StringBuffer sb  = new StringBuffer();
	sb.append(getAnnoScorporato(anno)).append("||")
	.append(meseScorporato(mese)).append("||")
	.append(giornoScorporato(giorno)).append("||")
	.append(sessoScorporato(sesso)).append("||")
	.append(comune);
	
	return sb.toString();
	 
  }
  
  private String sessoScorporato(String giorno) {

	  String ris = (Integer.parseInt(giorno)<40) ?  "m" :  "f";
	 
	  return ris;
	  
  }
  
  private String giornoScorporato(String giorno) {
		
	  String ris = (Integer.parseInt(giorno)<10) ?  "0"+giorno :  giorno;
	 
	  return ris;
	  
  }
  
  private String meseScorporato(String codiceMese) {
	    for(int i=0; i<mese.length; i++) {
	      for (int j = 0; j < 2; j++) {
	    	  if(codiceMese.equals(mese[i][j])){
	    		  
	    		  return mese[i][j-1];
	    	  }
		  }

	    }
	    return null;
	  }
  
  private String getAnnoScorporato(String anno) {
		
		if(anno.startsWith("0")||
			anno.startsWith("1")){
			anno = "20"+anno;
		}else{
			anno = "19"+anno;
		}
	
	    return anno;
	  }
  
}
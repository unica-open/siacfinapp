/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siaccommon.util.date.DateConverter;
import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * Classe contenente le utility utilizzate dall'applicativo per effettuare
 * operazioni con le date
 *
 * @author 70153
 */
public class DateUtility {

	public static final String DATEPATTERN = "dd/MM/yyyy";

	private static final String DATE_LONG_PATTERN = "dd/MM/yyyy : HH:mm:ss";

	private static final String DATE_LONG_PATTERN_24_HOUR = "dd/MM/yyyy HH:mm:ss";
	public static final String FORMAT_DATE_24_HOUR = "dd/MM/yyyy HH.mm.ss";
	
	private static final LogUtil LOG = new LogUtil(DateUtility.class);

	public DateUtility() {
	}

	/**
	 * Formatta il parametro java.sql.Date secondo il pattern gg/mm/aaaa
	 * 
	 * @param aDate
	 *            Date
	 * @return String
	 */
	public static String format(java.sql.Date aDate) {
		return format(aDate, DATEPATTERN);
	}

	/**
	 * Formatta il parametro java.sql.Date secondo il pattern gg/mm/aaaa
	 * hh:mm:ss
	 * 
	 * @param aDate
	 *            Date
	 * @return String
	 */
	public static String formatLong(java.sql.Date aDate) {
		return format(aDate, DATE_LONG_PATTERN);
	}

	/**
	 * Formatta il parametro java.sql.Date secondo il pattern gg/mm/aaaa
	 * 24hh:mm:ss
	 * 
	 * @param aDate
	 *            Date
	 * @return String
	 */
	public static String format24Hours(java.sql.Date aDate) {
		return format(aDate, DATE_LONG_PATTERN_24_HOUR);
	}

	private static String format(java.sql.Date aDate, String aPattern) {
		// utilizzo del formatter specifico
		SimpleDateFormat sdf = new SimpleDateFormat(aPattern);

		try {
			return sdf.format(aDate);
		} catch (Exception ex) {
		}

		return "";
	}

	/**
	 * presa una data in input restituisce in formato Date
	 * 
	 * @param data
	 * @return Date
	 */
	public static java.sql.Date parse(String dataInput) {
		java.sql.Date result = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);
		try {
			result = new java.sql.Date(sdf.parse(dataInput).getTime());
		} catch (Exception exc) {
		}
		return result;
	}

	public static java.sql.Date parseLong(String dataInput) {
		java.sql.Date result = null;
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_LONG_PATTERN_24_HOUR);
		sdf.setLenient(false);
		try {
			result = new java.sql.Date(sdf.parse(dataInput).getTime());
		} catch (Exception exc) {
		}
		return result;
	}

	public static String getCurrentDate() {
		Calendar cal = new GregorianCalendar();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);

		String giorno = "" + day;
		String mese = "" + month;
		if (giorno.length() < 2) {
			giorno = "0" + giorno;
		}
		if (mese.length() < 2) {
			mese = "0" + mese;
		}
		String dataDelGiorno = giorno + "/" + mese + "/" + year;

		return dataDelGiorno;
	}

	public static int getCurrentYear() {
		Calendar cal = new GregorianCalendar();
		return cal.get(Calendar.YEAR);
	}

	public static int getCurrentMonth() {
		Calendar cal = new GregorianCalendar();
		return cal.get(Calendar.MONTH) + 1;
	}

	public static String getCurrentDateAndFirstDay(String date) {
		String giorno = "01";
		int index = date.indexOf("/");
		int index2 = date.indexOf("/", index + 1);
		String mese = date.substring(index + 1, index2);
		String year = date.substring(index2 + 1, date.length());
		String dataDelGiorno = giorno + "/" + mese + "/" + year;
		return dataDelGiorno;
	}

	public static String getCurrentDateAndLastDay(String date) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		int index = date.indexOf("/");
		int index2 = date.indexOf("/", index + 1);
		String mese = date.substring(index + 1, index2);
		String year = date.substring(index2 + 1, date.length());
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Integer.parseInt(year), Integer.parseInt(mese) - 1, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		String dataDelGiorno = format.format(cal.getTime());
		return dataDelGiorno;
	}

	/**
	 * Controlla che la Data Precedente non sia superiore alla Data Successiva e
	 * che entrambe non siano superiori alla Data di Sistema
	 */
	public static boolean checkDateOrder(String dataPrec, String dataSucc,
			boolean canBeEqual) {
		String pattern = "dd/MM/yyyy";
		SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);
		try {
			if (StringUtils.isEmpty(dataPrec) || StringUtils.isEmpty(dataSucc)) {
				return false;
			}
			java.util.Date dataInizio = dateFormatter.parse(dataPrec);
			java.util.Date dataFine = dateFormatter.parse(dataSucc);
			java.util.Date oggi = dateFormatter.parse(getCurrentDate());
			int confronto = dataInizio.compareTo(dataFine);
			if (confronto > 0 || (!canBeEqual && confronto == 0)
					|| oggi.compareTo(dataFine) < 0) {
				return false;
			}

		} catch (ParseException parseEx) {
		}
		return true;
	}

	/**
	 * Controlla che la Data Precedente non sia superiore alla Data Successiva
	 * 
	 */
	public static boolean checkDateOrderWithoutSystem(String dataPrec,
			String dataSucc) {
		boolean esito = false;
		String pattern = "dd/MM/yyyy";

		SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);
		try {
			java.util.Date dataInizio = dateFormatter.parse(dataPrec);
			java.util.Date dataFine = dateFormatter.parse(dataSucc);
			int confronto = dataInizio.compareTo(dataFine);
			if (confronto <= 0) {
				esito = true;
			}
		} catch (ParseException parseEx) {
		}

		return esito;
	}

	/**
	 * Controlla che la Data Precedente sia minore o uguale alla Data Successiva
	 */
	public static boolean checkDateMinoreUguale(String dataPrec, String dataSucc) {
		String pattern = "dd/MM/yyyy";
		SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);
		try {
			java.util.Date dataInizio = dateFormatter.parse(dataPrec);
			java.util.Date dataFine = dateFormatter.parse(dataSucc);

			int confronto = dataFine.compareTo(dataInizio);
			if (confronto < 0) {
				return false;
			}
		} catch (ParseException parseEx) {
		}
		return true;
	}

	public static boolean checkOrarioValido(String h, String m) {
		boolean result = false;
		int h_test = Integer.parseInt(h);
		int m_test = Integer.parseInt(m);
		if (h_test > -1 && h_test < 24) {
			if (m_test > -1 && m_test < 60) {
				result = true;
			}
		}
		return result;
	}

	public static Timestamp buildTimestamp(String data) {
		Timestamp result = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);
		try {
			result = new Timestamp(sdf.parse(data).getTime());
		} catch (Exception exc) {
			//printStackTrace
		}
		return result;
	}

	/**
	 * Controlla che le 2 date siano uguali
	 */
	public static boolean checkDateUguale(String dataPrec, String dataSucc) {
		String pattern = "dd/MM/yyyy";
		SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);
		try {
			java.util.Date dataInizio = dateFormatter.parse(dataPrec);
			java.util.Date dataFine = dateFormatter.parse(dataSucc);

			int confronto = dataFine.compareTo(dataInizio);
			if (confronto == 0) {
				return true;
			}
		} catch (ParseException parseEx) {
		}
		return false;
	}

	public static List<String> getTokens(String s, String delimiter,
			boolean returnDelims) {
		List<String> v = new ArrayList<String>();
		if (s != null) {
			StringTokenizer st = new StringTokenizer(s, delimiter, returnDelims);
			while (st.hasMoreTokens()) {
				v.add(st.nextToken().trim());
			}
		}
		return v;
	}

	/*
	 * Funzione che aggiunge gli zeri nella data dove mancano, per poter
	 * arrivare ad un formato DD/MM/YYYY
	 * 
	 */
	public static String fillZeroInDate(String s) {
		String ris = "";

		List<String> elementi = getTokens(s, "/", false);

		String gg = elementi.get(0);
		String mm = elementi.get(1);
		String aa = elementi.get(2);

		if (gg.length() == 1) {
			gg = "0" + gg;
		}

		if (mm.length() == 1) {
			if (mm.equals("0")) {
				// siccome in calendar 0 = gennaio
				// allora lo valorizzo a 1
				mm = "01";
			} else {
				mm = "0" + mm;
			}
		}

		ris = gg + "/" + mm + "/" + aa;

		return ris;
	}

	/**
	 * Confronta i 2 orari dati in formato HH:MM e verifica che la fine non sia
	 * precedente ad inizio
	 * 
	 */
	public static boolean confrontaOrari(String ora_inizio, String ora_fine) {
		boolean trovato = false;

		String ora_inizio_format = StringUtils.substringBefore(ora_inizio, ":");
		String min_inizio_format = StringUtils.substringAfter(ora_inizio, ":");

		String ora_fine_format = StringUtils.substringBefore(ora_fine, ":");
		String min_fine_format = StringUtils.substringAfter(ora_fine, ":");

		int int_ora_inizio_format = Integer.parseInt(ora_inizio_format);
		int int_min_inizio_format = Integer.parseInt(min_inizio_format);
		int int_ora_fine_format = Integer.parseInt(ora_fine_format);
		int int_min_fine_format = Integer.parseInt(min_fine_format);

		if (int_ora_inizio_format == int_ora_fine_format) {

			if (int_min_inizio_format <= int_min_fine_format) {

				trovato = true;
			} else {

				trovato = false;
			}
		} else {

			if (int_ora_inizio_format > int_ora_fine_format) {

				trovato = false;
			} else {

				trovato = true;
			}

		}

		return trovato;

	}

	/**
	 * calcola i gg di differenza fra le date
	 */
	public static int ggDiffTraDate(String dataPrec, String dataSucc) {
		String pattern = "dd/MM/yyyy";
		int dateDiff = 0;
		SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);
		try {
			java.util.Date dataInizio = dateFormatter.parse(dataPrec);
			java.util.Date dataFine = dateFormatter.parse(dataSucc);

			Calendar c_prec = null;
			Calendar c_succ = null;
			c_prec = Calendar.getInstance(TimeZone.getDefault());
			c_succ = Calendar.getInstance(TimeZone.getDefault());

			c_prec.setTime(dataInizio);
			c_succ.setTime(dataFine);
			long ldate1 = dataInizio.getTime()
					+ c_prec.get(Calendar.ZONE_OFFSET)
					+ c_prec.get(Calendar.DST_OFFSET);

			long ldate2 = dataFine.getTime() + c_succ.get(Calendar.ZONE_OFFSET)
					+ c_succ.get(Calendar.DST_OFFSET);

			int hr1 = (int) (ldate1 / 3600000); // 60*60*1000
			int hr2 = (int) (ldate2 / 3600000);
			int days1 = (int) hr1 / 24;
			int days2 = (int) hr2 / 24;
			dateDiff = days2 - days1;

		} catch (ParseException parseEx) {

		}
		return dateDiff;
	}

	public static String getDataValida(String data) {

		String pattern = "dd/MM/yyyy";
		SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);
		java.sql.Date ogg = null;
		try {
			ogg = new java.sql.Date(dateFormatter.parse(data).getTime());

		} catch (Exception e) {

		}

		return format(ogg);
	}

	public static Date stringToDate(String dataDaConvertire, String dateFormat) {
		DateFormat df = new SimpleDateFormat(dateFormat);

		Date data = null;
		try {
			data = df.parse(dataDaConvertire);
		} catch (ParseException e) {
			// printStackTrace
		}

		return data;
	}

	public static boolean checkNumber(String s) throws Exception {
		try {
			String ok = "0123456789";

			for (int i = 0; i < s.length(); i++) {
				if (ok.indexOf(new String(new char[] { s.charAt(i) })) == -1) {
					return false;
				}
			}
		} catch (Exception ex) {
			// se errore restituisco il false
			throw ex;
		}
		return true;
	}

	public static boolean checkDate(String date, String formato)
			throws Exception {
		String[] v = date.split("/");
		// verifico che la data sia stata inserita correttamente.
		String dd = (String) v[0];
		String mm = (String) v[1];
		String aa = (String) v[2];
		dd = dd.length() < 2 ? "0" + dd : dd;
		mm = mm.length() < 2 ? "0" + mm : mm;

		int lunghezza_ammessa = formato.length();
		/*
		 * la stringa non deve essere minore del numero dei caratteri presenti
		 * nel formato
		 */
		if (date.length() < lunghezza_ammessa) {
			return false;
		}

		if (dd.equals("00") || mm.equals("00") || aa.equals("0000")) {
			return false;
		}
		try {
			checkNumber(dd);
			checkNumber(mm);
			checkNumber(aa);
		} catch (Exception e) {
			throw e;
		}
		int _dd = 0;
		int _aa = 0;
		int _mm = 0;

		try {
			_dd = Integer.parseInt(dd);
			_aa = Integer.parseInt(aa);
			_mm = Integer.parseInt(mm);
		} catch (NumberFormatException nf) {
			// non e' un numero
			return false;
		}
		if (_mm > 12 || _mm == 0) {
			return false;
		}
		// il controllo sull'esattezza della data viene eseguita da oracle.
		if (_mm == 11 || _mm == 4 || _mm == 6 || _mm == 9) {
			if (_dd > 30 || _dd == 0) {
				return false;
			}
		} else {
			if (_mm == 1 || _mm == 3 || _mm == 5 || _mm == 7 || _mm == 8
					|| _mm == 10) {
				if (_dd > 31) {
					return false;
				}
			}
		}
		if (_mm == 2) {
			// verifica sull'anno bisestile
			if (_aa % 4 != 0) { // && _aa%400!=0){//se l'anno secolare
								// (1800,1900,2000) e' divisibile per 400 l'anno
								// non
								// e' bisestile
				if (_dd > 28) {
					return false;
				}

			} else {
				if (_aa % 400 == 0 || _aa % 4 == 0 || _aa == 0) {
					if (_dd > 29) {
						return false;
					}
				} else {
					if (_dd > 28) {
						return false;
					}
				}
			}

		}
		return true;

	}

	public static String addDay(String dateDbf, int dayToAdd) {

		String dataNew = "";
		String DATE_FORMAT = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		try {
			Calendar c1 = Calendar.getInstance();

			int aaaa = Integer.parseInt(dateDbf.substring(6));
			int mm = Integer.parseInt(dateDbf.substring(3, 5)) - 1;
			int dd = Integer.parseInt(dateDbf.substring(0, 2));

			c1.set(aaaa, mm, dd);
			c1.add(Calendar.DATE, dayToAdd);

			dataNew = sdf.format(c1.getTime());
		} catch (Exception e) {
			// printStackTrace
		}
		// RITORNA DIFFERENZA DATE
		return dataNew;
	}

	public static java.sql.Date parseData(String dataInput) {
		java.sql.Date result = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);
		try {
			result = new java.sql.Date(sdf.parse(dataInput).getTime());
		} catch (Exception exc) {

		}
		return result;
	}

	public static java.util.Date parseUtilData(String dataInput) {
		java.util.Date result = null;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);
		try {
			result = new java.sql.Date(sdf.parse(dataInput).getTime());
		} catch (Exception exc) {
			LOG.error("parseUtilData", exc.getMessage(), exc);
			//printStackTrace
		}
		return result;
	}
	
	public static java.util.Date parseUtilDataJSON(String dataInput) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		sdf.setLenient(false);
		return sdf.parse(dataInput);
	}

	public static java.util.Date parseUtilDataFormat2(String dataInput) {
		java.util.Date result = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		try {
			result = new java.sql.Date(sdf.parse(dataInput).getTime());
		} catch (Exception exc) {
			LOG.error("parseUtilDataFormat2", exc.getMessage(), exc);

			//printStackTrace
		}
		return result;
	}

	public static java.util.Date parseUtilDataFormatExt(String dataInput) {

		java.util.Date result = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH.mm.ss");
		sdf.setLenient(false);
		try {
			result = new java.sql.Date(sdf.parse(dataInput).getTime());
		} catch (Exception exc) {
			LOG.error("parseUtilDataFormatExt", exc.getMessage(), exc);
		}
		return result;
	}

	/**
	 * Metodo che verifica se la Stringa passata contiene una data con il giusto
	 * pattern
	 */
	public static boolean isDate(String str, String datePattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		sdf.setLenient(false);
		try {
			@SuppressWarnings("unused")
			Date testDate = sdf.parse(str);
		} catch (Exception exc) {
			return false;
		}
		return true;
	}

	/**
	 * Metodo che verifica se d1 > d2 e' -1 se d1 > d2 e' 1 se d1 < d2
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int compareDate(String d1, String d2) {

		int day = Integer.parseInt(d1.substring(0, 2));
		int month = Integer.parseInt(d1.substring(3, 5));
		int year = Integer.parseInt(d1.substring(6, 10));
		int day2 = Integer.parseInt(d2.substring(0, 2));
		int month2 = Integer.parseInt(d2.substring(3, 5));
		int year2 = Integer.parseInt(d2.substring(6, 10));
		Date data1 = (new GregorianCalendar(year, month, day)).getTime();
		Date data2 = (new GregorianCalendar(year2, month2, day2)).getTime();
		return data1.compareTo(data2);
	}

	public static String formatDate(Date data) {
		return (data == null ? null : (new SimpleDateFormat("dd/MM/yyyy",
				Locale.ITALIAN)).format(data));
	}

	public static Date getDataSistema() {
		GregorianCalendar gc = new GregorianCalendar();
		Date d = gc.getTime();
		return d;
	}

	/**
	 * restituisce la data di sistema secondo il formato richiesto tramite la
	 * variabile formato
	 * 
	 * @param formato
	 *            string
	 * @return String
	 * @throw non definito
	 */
	public static String getDataSistema(String formato) {

		GregorianCalendar gc = new GregorianCalendar();
		Date d = gc.getTime();

		return new SimpleDateFormat(formato).format(d);

	}

	public static java.sql.Timestamp getDate(String data) {
		java.sql.Timestamp timestamp = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date parsedDate = dateFormat.parse(data);
			timestamp = new java.sql.Timestamp(parsedDate.getTime());
		} catch (Exception e) {
			// printStackTrace
		}

		return timestamp;
	}

	public static int getLastDay(int year, int month) {
		GregorianCalendar gc = new GregorianCalendar(year, month - 1, 1);
		gc.set(Calendar.DAY_OF_MONTH,
				gc.getActualMaximum(Calendar.DAY_OF_MONTH));

		return gc.get(Calendar.DAY_OF_MONTH);
	}

	enum FormatoData {
		DB("yyyy-MM-dd HH:mm:ss"), aaaaMMdd_HHmmssSSS("yyyy-MM-dd HH:mm:ss.SSS"), ggMMaaaahhmmss(
				"dd/MM/yyyy HH:mm:ss"), ggMMyyyy_hhmmss("dd-MM-yyyy hh:mm:ss"), ggMMaaaa(
				"dd/MM/yyyy"), yyyyMMdd("yyyy-MM-dd"), MMMddHHmmssyyyyz(
				"MMM dd HH:mm:ss yyyy zzz", Locale.UK), MMMddHHmmssyyy(
				"MMM dd HH:mm:ss yyyy");

		private String pattern;
		private Locale locale;

		FormatoData(String pattern) {
			this.pattern = pattern;
			this.locale = Locale.getDefault();
		}

		FormatoData(String pattern, Locale locale) {
			this.pattern = pattern;
			this.locale = locale;
		}

		public SimpleDateFormat getFormatter() {
			return new SimpleDateFormat(pattern, locale);
		}

	}

	public final static String convertiDataInGgMmYyyy(Date _time) {
		if (_time == null)
			return null;
		try {
			return FormatoData.ggMMaaaa.getFormatter().format(_time);
		} catch (Exception e) {
			return null;
		}
	}

	public final static Timestamp convertiDataInTimeStamp(Date data) {
		Timestamp ts = null;
		try {
			ts = new Timestamp(data.getTime());
			return ts;
		} catch (Exception e) {
			return ts;
		}
	}

	/**
	 * Gets the anno.
	 *
	 * @param date
	 *            the date
	 * @return the anno
	 */
	public static int getAnno(Date date) {
		GregorianCalendar cg = new GregorianCalendar();
		cg.setTime(date);
		int anno = cg.get(Calendar.YEAR);
		return anno;
	}

	public static Date convertToDefaultDate(String dateStr, Date defaultDate) {
		if (StringUtils.isBlank(dateStr))
			return defaultDate;

		return DateConverter.convertFromString(dateStr);
	}

	public static String convertToString(Date date) {
		return date == null ? null : DateConverter.convertToString(date);
	}

}

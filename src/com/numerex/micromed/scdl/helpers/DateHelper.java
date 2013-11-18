package com.numerex.micromed.scdl.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Utility class to manage Dates
 * @author dmoretti
 *
 */
public class DateHelper {

	private Locale locale = null;

	private static Locale defaultLocale = new Locale("us", "US");

	private SimpleDateFormat formatter = null;

	public DateHelper(String dsPatron) {
		this.locale = new Locale("us", "US");
		this.formatter = new SimpleDateFormat(dsPatron, locale);		
	}

	public DateHelper(String dsPatron, Locale locale) {
		this.formatter = new SimpleDateFormat(dsPatron, locale);		
		this.locale = locale;
	}

	public String dateToString(Date dtFecha) {
		if (dtFecha == null) {
			return null;
		}
		return this.formatter.format(dtFecha);
	}

	public static String dateToString(Date dtFecha, String dsPatron) {
		if (dtFecha == null) {
			return "";
		}
		DateFormat formatter = new SimpleDateFormat(dsPatron, defaultLocale);
		return formatter.format(dtFecha);
	}


	public Date stringToDate(String dsFecha) {
		Date retorno = null;
		try {
			retorno = this.formatter.parse(dsFecha);
		} catch (ParseException e) {
			return new Date();
		}
		return retorno;
	}
	public static Date stringToDate(String fecha, String formato) {
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		Date retorno = null;
		try {
			retorno = sdf.parse(fecha);
		} catch (ParseException e) {
			return new Date();
		}
		return retorno;
	}

}

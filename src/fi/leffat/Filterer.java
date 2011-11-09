package fi.leffat;

import java.util.Calendar;

import android.content.SharedPreferences;
import android.util.Log;

public class Filterer
{
	/**
	 * Compare operands for filtering
	 */
	private static final byte GREATER_THAN = 1;
	private static final byte LESS_THAN    = 2;
	
	/**
	 * Program preferences
	 */
	private SharedPreferences prefs;
	
	/**
	 * Stores preferences
	 * 
	 * @param _prefs Shared preferences from context
	 */
	public Filterer(SharedPreferences _prefs)
	{
		prefs = _prefs;
	}
	
	/**
	 * Checks if the given movie passes the filters or not
	 * 
	 * @param movie A single movie to check for filtering
	 */
	public final boolean isFiltered(Movie movie)
	{
		// Check if production year filter is active
		if (prefs.getBoolean("FILTER_ACTIVE_PRODUCTION_YEAR", false) == true ) {
			int value = prefs.getInt("FILTER_VALUE_PRODUCTION_YEAR", 0);
			int compare = prefs.getInt("FILTER_COMPARE_PRODUCTION_YEAR", GREATER_THAN);
			
			// Filter is valid
			if (value != 0) {
				int ref = Integer.parseInt(movie.productionYear); // Reference where to compare
				
				// Check if filter matches the movie
				if ((compare == GREATER_THAN && ref < value) || (compare == LESS_THAN && ref > value)) {
					return true;
				}
			}
		}
		
		// Check if genre filter is active
		if (prefs.getBoolean("FILTER_ACTIVE_GENRE", false) == true) {
			String value = prefs.getString("FILTER_VALUE_GENRE", "NONE");
			
			// Filter is valid
			if (value != "NONE") {
				String ref = movie.getGenres(); // Reference where to compare
				
				// Check if filter matches the movie
				if (ref.lastIndexOf(value) != -1) {
					return true;
				}
			}
		}
		
		// Check if age filter is active
		if (prefs.getBoolean("FILTER_ACTIVE_AGE", false) == true) {
			String value = prefs.getString("FILTER_VALUE_AGE", "0");
			
			// Filter is valid
			if (value != "0") {
				String ref = movie.rating;
				
				// Check if filter matches the movie
				if (ref.equals(value)) {
					return true;
				}
			}
		}
		
		// TODO: Leffan aloitus/lopetusajan filtteri ei ole logiikaltaan alkuunsakaan korrekti
		// TODO: Filtterin pitäisi skipata tulevien päivien filtteröinti tämän filtterin kohdalla,
		//		 jottei muiltakin päiviltä katoaisi saman kellonajan elokuvat
		// TODO: String to int -muunnos ja sillee
		// Check if starting/ending time-filter is active
		if (prefs.getBoolean("FILTER_ACTIVE_TIME", false) == true) {
			String value = prefs.getString("FILTER_VALUE_START_TIME", "-1");
			
			// Filter is valid
			if (value != "-1") {
				// TODO: Tämä uusiks
				String ref = movie.showStartTimeH;
				
				// Check if filter matches the movie
				if (ref.equals(value)) {
					return true;
				}
			}
		}
		
		// Check if name-filter is active
		if (prefs.getBoolean("FILTER_ACTIVE_NAME", false) == true) {
			String value = prefs.getString("FILTER_VALUE_TIME", "0");
			
			// Filter is valid
			if (value != "0") {
				String ref = movie.title;
				String ref2 = movie.originalTitle;
				
				// Check if filter matches the movie
				if (ref.lastIndexOf(value) != -1 || ref2.lastIndexOf(value) != -1) {
					return true;
				}
			}
		}
		
		// Check if shown shows-filter is active
		if (prefs.getBoolean("FILTER_ACTIVE_SHOWN_SHOWS", false) == true) {
			int ref = Integer.parseInt(movie.showStartTimeH);
			
			// Check if filter matches the movie
			if (ref < Calendar.HOUR_OF_DAY) {
				return true;
			}
		}
		
		return false;
	}
}

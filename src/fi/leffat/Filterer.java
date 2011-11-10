package fi.leffat;

import android.content.SharedPreferences;

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
		
		//TODO: Lis‰‰ filttereit‰ samaan tapaan kuin alla.
		
		// Check if production year filter is active
		if (prefs.getBoolean("FILTER_ACTIVE_PRODUCTION_YEAR", false) == true ) {
			int value = prefs.getInt("FILTER_VALUE_PRODUCTION_YEAR", 0);
			int compare = prefs.getInt("FILTER_COMPARE_PRODUCTION_YEAR", GREATER_THAN);
			
			// Filter is valid
			if (value != 0) {
				int ref = Integer.parseInt(movie.productionYear);
				
				// Check if filter matches the movie
				if ((compare == GREATER_THAN && ref < value) || (compare == LESS_THAN && ref > value)) {
					return true;
				}
			}
		}
		
		// TODO: Genre-filtteri
		
		// TODO: Ik‰raja-filtteri
		
		// TODO: Aika-filtteri
		
		// TODO: Nimi-filtteri
		
		return false;
	}
}

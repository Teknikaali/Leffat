package fi.justjoo;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

/**
 * Handles reading the finnkino.xml file on the phone and parsing the contents.
 */
public class XmlParser
{
	/**
	 * An array for the movies.
	 */
	static private ArrayList<ArrayList<Movie>> days;
	
	/**
	 * Formatted dates.
	 */
	private String[] dates;
	
	/* ============================================
	 *  Constructors
	 * ============================================ */
	public XmlParser()
	{
		days = new ArrayList<ArrayList<Movie>>(7);
		
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		
		// Format the current date
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		dates = new String[7];
		
		Date c_date = new Date();
		for(int i = 0; i < 7; ++i) {
			dates[i] = sdf.format(c_date.getTime() + MILLIS_IN_DAY * i);
		}
	}
	
	/* ============================================
	 *  New methods
	 * ============================================ */
	/**
	 * Reads the XML-file on the phone and parses it by saving only the relevant data.
	 * Also, it sorts all movies according to date into an ArrayList.
	 * 
	 * @param _context Current application context
	 * 
	 * @return Returns 0 on success, -1 on failure
	 */
	@SuppressWarnings("unchecked")
	public int parseXml(Context _context, int dayIndex)
	{
		String filePath;
		
		ArrayList<Movie> movies = new ArrayList<Movie>();
		int index = -1;

		// Define the file path
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			filePath = Environment.getExternalStorageDirectory()+"/JustJoo/finnkino_" + dates[dayIndex] + ".xml";
		}
		else {
			filePath = "finnkino_" + dates[dayIndex] + ".xml";
		}

		// Open inputstream to the file
		File file = new File(filePath);
		FileInputStream stream;
		
		try {
			stream = new FileInputStream(file);
			
			// Set up XmlPullParser
			XmlPullParser xpp = Xml.newPullParser();
			xpp.setInput(stream, "UTF-8");
			
			// Read all contents until EOF
			int eventType = xpp.getEventType();
			
			while (eventType != XmlPullParser.END_DOCUMENT) {
				
				if(eventType == XmlPullParser.START_TAG) {
					// (Show-tag marks the beginning of a new movie)
					if (xpp.getName() ==  "Show") {
						movies.add(new Movie());
						++index;
					}
					// Parse dates for easier use
					else if (xpp.getName().equals("dttmShowStart")) {
						String temp = xpp.nextText();
						
						if (temp != null && temp != "") {
							String date = "";
							String[] time = {"", ""};
							int timeIndex = 0;
							
							for (int j = 0; j < temp.length(); ++j) {
								if (temp.charAt(j) != 'T') {
									date += temp.charAt(j);
								}
								else {
									for (int k = j + 1; k < temp.length(); ++k) {
										if (temp.charAt(k) == ':') {
											++timeIndex;
										}
										else if (timeIndex < 2) {
											time[timeIndex] += temp.charAt(k);
										}
									}
									
									break;
								}
							}
							
							movies.get(index).showStartDate = date;
							
							movies.get(index).showStartTimeH = time[0];
							movies.get(index).showStartTimeM = time[1];
						}
					}
					// Get movie title
					else if (xpp.getName().equals("Title")) {
						movies.get(index).title = xpp.nextText();
					}
					// Get original title of the movie (in case it was translated)
					else if (xpp.getName().equals("OriginalTitle")) {
						movies.get(index).originalTitle = xpp.nextText();
					}
					// Get production year
					else if (xpp.getName().equals("ProductionYear")) {
						movies.get(index).productionYear = xpp.nextText();
					}
					// Get show URL
					else if (xpp.getName().equals("ShowURL")) {
						movies.get(index).showUrl = xpp.nextText();
					}
					// Get length in minutes (integer, not string)
					else if (xpp.getName().equals("LengthInMinutes")) {
						movies.get(index).lengthInMinutes = Integer.parseInt(xpp.nextText());
					}
					// Get rating label
					else if (xpp.getName().equals("RatingLabel")) {
						movies.get(index).rating = xpp.nextText();
					}
					// Get image URL for the rating label
					else if (xpp.getName().equals("RatingImageUrl")) {
						movies.get(index).ratingImageUrl = xpp.nextText();
					}
					// Get genres, explode them and add into an arraylist one by one
					else if (xpp.getName().equals("Genres")) {
						String temp = xpp.nextText();
						
						String temp2 = "";
						
						for (int j = 0; j < temp.length(); ++j) {
							if (temp.charAt(j) == ',') {
								movies.get(index).genres.add(temp2);
								temp2 = "";
							}
							else if (temp.charAt(j) != ' ') {
								temp2 += temp.charAt(j);
							}
						}
						
						movies.get(index).genres.add(temp2);
					}
					// Get theatre
					else if (xpp.getName().equals("Theatre")) {
						movies.get(index).theatre = xpp.nextText();
					}
					// Get auditorium
					else if (xpp.getName().equals("TheatreAuditorioum")) {
						movies.get(index).theatreAuditorium = xpp.nextText();
					}
					// Get small promotional image of the movie
					else if (xpp.getName().equals("EventSmallImagePortrait")) {
						movies.get(index).smallImageUrl = xpp.nextText();
					}
					// Get larger promotional image of the movie
					else if (xpp.getName().equals("EventLargeImagePortrait")) {
						movies.get(index).largeImageUrl = xpp.nextText();
					}
				}
				
				eventType = xpp.next();
			}
		
		} catch (Exception e) {
			e.printStackTrace();// Something went wrong and the file could not be read!
			return -1;
		}

		days.add((ArrayList<Movie>) movies.clone());
		
		return 0;
	}
	
	/**
	 * Returns all movies sorted into groups by dates.
	 */
	public ArrayList<ArrayList<Movie>> getList()
	{
		return days;
	}
	
	/**
	 * Returns one movie according to the given day and movie indexes.
	 * 
	 * @param _dayIndex   Day index (0-6)
	 * @param _movieIndex Movie index (0..*)
	 * 
	 * @return The desired Movie with related data, returns empty Movie
	 *         if given indexes were invalid.
	 */
	public Movie getMovie(int _dayIndex, int _movieIndex)
	{
		if (_dayIndex < days.size() && _movieIndex < days.get(_dayIndex).size()) {
			return days.get(_dayIndex).get(_movieIndex);
		}
		else {
			return new Movie();
		}
	}
}
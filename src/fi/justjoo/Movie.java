package fi.justjoo;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

/**
 * Movie is class tha holds all the information of one individual show.
 */
public class Movie
{
	/** Start day of the show. */
	public String showStartDate;
	
	/** Start time (hour) of the show. */
	public String showStartTimeH;
	
	/** Start time (minute) of the show. */
	public String showStartTimeM;
	
	/** Current title of the movie (may be translated). */
	public String title;
	
	/** Original title of the movie. */
	public String originalTitle;
	
	/** Production year of the movie. */
	public String productionYear;
	
	/** Show URL on Finnkino.fi */
	public String showUrl;
	
	/** Length of the movie in minutes. */
	public int lengthInMinutes;
	
	/** Rating of the movie (for example "K-13"). */
	public String rating;
	
	/** URL for the image of rating label. */
	public String ratingImageUrl;
	
	/** All different genres of the movie. */
	public ArrayList<String> genres;
	
	/** Theatre of the show. */
	public String theatre;
	
	/** Auditorium of the theatre where the show is held. */
	public String theatreAuditorium;
	
	/** URL for the small promotional image of the movie. */
	public String smallImageUrl;
	
	/** URL for the large promotional image of the movie. */
	public String largeImageUrl;
	
	/* ============================================
	 *  Constructors
	 * ============================================ */
	/**
	 * Initializes arrays.
	 */
	public Movie()
	{
		genres = new ArrayList<String>();
	}
	
	/* ============================================
	 *  Accessors
	 * ============================================ */
	/**
	 * Downloads the small movie image from Finnkino.fi and returns
	 * it as an ImageView.<br><br>
	 * 
	 * If the image is not found, there is no internet connection or some other
	 * error occurred, the default image is used instead (no_movie_img_found.png).
	 * 
	 * @param _context Application context (use getApplicationContext() for this)
	 * 
	 * @return ImageView containing the image
	 */
	public ImageView getSmallImage(Context _context)
	{
		ImageView iv = new ImageView(_context);
		
		if (smallImageUrl.length() > 0) {
			try {
				URL ulrn = new URL(smallImageUrl);
				HttpURLConnection con = (HttpURLConnection) ulrn.openConnection();
				InputStream is = con.getInputStream();
				Bitmap bmp = BitmapFactory.decodeStream(is);
				
				if (bmp != null) {
					iv.setImageBitmap(bmp);
					return iv;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		iv.setImageResource(R.drawable.no_movie_img_found);
		
		return null;
	}
	
	/**
	 * Downloads the large movie image from Finnkino.fi and returns
	 * it as an ImageView.<br><br>
	 * 
	 * If the image is not found, there is no internet connection or some other
	 * error occurred, the default image is used instead (no_movie_img_found.png).
	 * 
	 * @param _context Application context (use getApplicationContext() for this)
	 * 
	 * @return ImageView containing the image
	 */
	public ImageView getLargeImage(Context _context)
	{
		ImageView iv = new ImageView(_context);
		
		if (largeImageUrl.length() > 0) {
			try {
				URL ulrn = new URL(largeImageUrl);
				HttpURLConnection con = (HttpURLConnection) ulrn.openConnection();
				InputStream is = con.getInputStream();
				Bitmap bmp = BitmapFactory.decodeStream(is);
				
				if (bmp != null) {
					iv.setImageBitmap(bmp);
					return iv;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		iv.setImageResource(R.drawable.no_movie_img_found);
		
		return null;
	}
	
	/**
	 * Returns all genres as a single String.
	 * 
	 * @return String containing all genres
	 */
	public String getGenres()
	{
		if (genres != null) {
			String all = "";
			
			for (int i = 0; i < genres.size(); ++i) {
				if (i > 0) {
					all += ", ";
				}
				all += genres.get(i);
			}
			
			return all;
		}
		else {
			return "ERROR";
		}
	}
	
	/**
	 * Returns the start time of the show.
	 * 
	 * @return Start time as a String
	 */
	public String getStartTime()
	{
		
		if (showStartTimeH.length() > 0) {
			String time = showStartTimeH + ":";
			
			if (showStartTimeM.length() > 0) {
				time += showStartTimeM;
			}
			else {
				time += "ERROR";
			}
			
			return time;
		}
		else {
			return "ERROR";
		}
	}
}
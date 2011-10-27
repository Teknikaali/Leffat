package fi.leffat;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Movie is class tha holds all the information of one individual show.
 */
public class Movie implements Parcelable
{
	/** Start day of the show. */
	public String showStartDate;
	
	/** Start time (hour) of the show. */
	public String showStartTimeH;
	
	/** Start time (minute) of the show. */
	public String showStartTimeM;
	
	public String parsedTime;
	
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
	public String parsedGenres;
	
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
	 * Downloads the small movie image from Finnkino.fi and returns it as a Bitmap.<br><br>
	 * 
	 * If the image is not found, there is no internet connection or some other
	 * error occurred, the default image is used instead (no_movie_img_found.png).
	 * 
	 * @param _context Application context (use getApplicationContext() for this)
	 * 
	 * @return Bitmap containing the image
	 */
	public Bitmap getSmallImage(Context _context)
	{
		if (smallImageUrl.length() > 0) {
			try {
				URL ulrn = new URL(smallImageUrl);
				HttpURLConnection con = (HttpURLConnection) ulrn.openConnection();
				con.setDoInput(true);
				con.connect();
				InputStream is = con.getInputStream();
				Bitmap bmp = BitmapFactory.decodeStream(is);
				
				if (bmp != null) {
					return bmp;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return BitmapFactory.decodeResource(_context.getResources(), R.drawable.no_movie_img_found);
	}
	
	/**
	 * Downloads the large movie image from Finnkino.fi and returns it as a Bitmap.<br><br>
	 * 
	 * If the image is not found, there is no internet connection or some other
	 * error occurred, the default image is used instead (no_movie_img_found.png).
	 * 
	 * @param _context Application context (use getApplicationContext() for this)
	 * 
	 * @return Bitmap containing the image
	 */
	public Bitmap getLargeImage(Context _context)
	{
		if (largeImageUrl.length() > 0) {
			try {
				URL ulrn = new URL(largeImageUrl);
				HttpURLConnection con = (HttpURLConnection) ulrn.openConnection();
				con.setDoInput(true);
				con.connect();
				InputStream is = con.getInputStream();
				Bitmap bmp = BitmapFactory.decodeStream(is);
				
				if (bmp != null) {
					return bmp;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return BitmapFactory.decodeResource(_context.getResources(), R.drawable.no_movie_img_found);
	}
	
	/**
	 * Downloads the rating image from Finnkino.fi and returns it as a Bitmap.
	 * 
	 * @param _context Application context (use getApplicationContext() for this)
	 * 
	 * @return Bitmap containing the image
	 */
	public Bitmap getRatingImage(Context _context)
	{
		Bitmap bmp;
		
		if (ratingImageUrl.length() > 0) {
			try {
				URL ulrn = new URL(ratingImageUrl);
				HttpURLConnection con = (HttpURLConnection) ulrn.openConnection();
				con.setDoInput(true);
				con.connect();
				InputStream is = con.getInputStream();
				bmp = BitmapFactory.decodeStream(is);
				
				if (bmp != null) {
					return bmp;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	/**
	 * Returns all genres as a single String.
	 * 
	 * @return String containing all genres
	 */
	public String getGenres()
	{
		if (parsedGenres != null) {
			return parsedGenres;
		}
		else if (genres != null) {
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
		if (parsedTime != null) {
			return parsedTime;
		}
		else if (showStartTimeH.length() > 0) {
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
	
	/* ============================================
	 *  Interface methods
	 * ============================================ */
	public int describeContents()
	{
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(showStartDate);
		dest.writeString(getStartTime());
		dest.writeString(title);
		dest.writeString(originalTitle);
		dest.writeString(productionYear);
		dest.writeString(showUrl);
		dest.writeInt(lengthInMinutes);
		dest.writeString(rating);
		dest.writeString(ratingImageUrl);
		dest.writeString(getGenres());
		dest.writeString(theatre);
		dest.writeString(theatreAuditorium);
		dest.writeString(smallImageUrl);
		dest.writeString(largeImageUrl);
	}
	
	public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>()
	{
		public Movie createFromParcel(Parcel in)
		{
			return new Movie(in);
		}
		
		public Movie[] newArray(int size)
		{
			return new Movie[size];
		}
	};
	
	private Movie(Parcel in)
	{
		genres = new ArrayList<String>();
		
		showStartDate     = in.readString();
		parsedTime        = in.readString();
		title             = in.readString();
		originalTitle     = in.readString();
		productionYear    = in.readString();
		showUrl           = in.readString();
		lengthInMinutes   = in.readInt();
		rating            = in.readString();
		ratingImageUrl    = in.readString();
		parsedGenres      = in.readString();
		theatre           = in.readString();
		theatreAuditorium = in.readString();
		smallImageUrl     = in.readString();
		largeImageUrl     = in.readString();
	}
}
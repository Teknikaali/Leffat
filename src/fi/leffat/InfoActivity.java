package fi.leffat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * InfoActivity shows more detailed information about the movie that is
 * passed to it via intent.
 */
public class InfoActivity extends Activity 
{
	/**
	 * The movie passed via intent.
	 */
	private Movie movie;
	
	/**
	 * Context of this activity.
	 */
	private static Context context;

	/* ============================================
	 *  Inherited methods
	 * ============================================ */
	/**
	 * Sets the view and removes titlebar.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        
    	requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.infoscreen);
	}
	
	/**
	 * Populates all data.
	 */
	@Override
	public void onResume()
	{
		super.onResume();
		
		context = this;
		
		movie = getIntent().getExtras().getParcelable("movie");
		
		// Load images on different thread, in order to allow the window to open
		// immediately after it has started
		Thread imageThread = new Thread() {
			@Override
			public void run()
			{
				final Bitmap smallBmp  = movie.getSmallImage(context);
				final Bitmap ratingBmp = movie.getRatingImage(context);
				
				runOnUiThread(new Runnable() {
					public void run() {
		    			ImageView movieImage = (ImageView) findViewById(R.id.movie_thumbnail);
		    			movieImage.setImageBitmap(smallBmp);
		    			
		    			ImageView ratingImage = (ImageView) findViewById(R.id.rating);
		    			ratingImage.setImageBitmap(ratingBmp);
					}
				});
			}
		};
		imageThread.start();
		
		// Populate data
		TextView movieTitle = (TextView) findViewById(R.id.original_name);
		movieTitle.setText(movie.originalTitle);
		
		TextView translatedTitle = (TextView) findViewById(R.id.translated_name);
		translatedTitle.setText(movie.title);
		
		TextView genre = (TextView) findViewById(R.id.movie_genre);
		genre.setText(movie.getGenres());
		
		TextView year = (TextView) findViewById(R.id.year);
		year.setText(movie.productionYear);
		
		TextView movieUrl = (TextView) findViewById(R.id.url);
		movieUrl.setText(movie.showUrl);
	}

}

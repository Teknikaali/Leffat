package fi.justjoo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class PopupActivity extends Activity 
{
	

	private String imageUrl;
	private String ratingUrl;
	private String movieName;
	private String translatedName;
	private String genres;
	private String productionYear;
	private String url;
	
	private static Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        
    	requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.popup);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		context = this;
		
		Bundle bundle = getIntent().getExtras();
		movieName = bundle.getString("originalName");
		imageUrl = bundle.getString("movieThumbnail");
		ratingUrl = bundle.getString("ratingImage");
		translatedName = bundle.getString("translatedName");
		genres = bundle.getString("genres");
		productionYear = bundle.getString("productionYear");
		url = bundle.getString("url");
		
		Thread imageThread = new Thread()
		{
    		public void run()
    		{
    			final Bitmap image = getBitmapFromURL(imageUrl);
    			final Bitmap rating = getBitmapFromURL(ratingUrl);
    			
    			runOnUiThread(new Runnable() {
					public void run() {
		    			ImageView movieImage = (ImageView) findViewById(R.id.movie_thumbnail);
		    			movieImage.setImageBitmap(image);
		    			
		    			ImageView ratingImage = (ImageView) findViewById(R.id.rating);
		    			ratingImage.setImageBitmap(rating);
					}
    			});
    		}
    		
    		private Bitmap getBitmapFromURL(String src)
    		{
    	        try {
    	            URL url = new URL(src);
    	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    	            connection.setDoInput(true);
    	            connection.connect();
    	            InputStream input = connection.getInputStream();
    	            Bitmap myBitmap = BitmapFactory.decodeStream(input);
    	            return myBitmap;
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	            return null;
    	        }
    	    }
		};
		imageThread.start();
		
		TextView movieTitle = (TextView) findViewById(R.id.original_name);
		movieTitle.setText(movieName);
		
		TextView translatedTitle = (TextView) findViewById(R.id.translated_name);
		translatedTitle.setText(translatedName);
		
		TextView genre = (TextView) findViewById(R.id.movie_genre);
		genre.setText(genres);
		
		TextView year = (TextView) findViewById(R.id.year);
		year.setText(productionYear);
		
		TextView movieUrl = (TextView) findViewById(R.id.url);
		movieUrl.setText(url);
	}

}

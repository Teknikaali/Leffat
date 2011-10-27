package fi.leffat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

// TODO Kommentoi
public class LoadActivity extends Activity
{
	/**
	 * Activity context.
	 */
	private static Context context;

	/* ============================================
	 *  Inherited methods
	 * ============================================ */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		
		setContentView(R.layout.loadingscreen);
		
		context = this;
		
		loadShowData();
	}

	/* ============================================
	 *  New methods
	 * ============================================ */
	/**
	 * Loads required XML-files from Finnkino.fi and saves them to
	 */
    private void loadShowData()
    {
    	Thread thread = new Thread() {
    		public void run() {
    			runOnUiThread(new Runnable() {
		    		public void run() {
		    			ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar1);
		    			bar.setMax(16);
		    			bar.setProgress(0);
		    		}
		        });
    			
		        FileLoader loader = new FileLoader();
		        
		        String result = (loader.prepare(getApplicationContext())).getValue();
		        new Thread(new Alerter(result, true)).start();
		        
		        if (!result.equals("SUCCESS")) {
		        	return;
		        }
		        
		        for (int i = 0; i < 7; ++i) {
			        result = (loader.loadFile(getApplicationContext(), i)).getValue();
			        new Thread(new Alerter(result, false)).start();
			        
			        if (!result.equals("SUCCESS")) {
			        	return;
			        }
		        }
		        
		        for (int i = 0; i < 7; ++i) {
		        	MainActivity.parser.parseXml(getApplicationContext(), i);
			        new Thread(new Alerter(result, false)).start();
		        }
		        
		        runOnUiThread(new Runnable() {
		    		public void run() {
						ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar1);
						bar.setProgress(16);
						
    					setResult(Activity.RESULT_OK, null);
    					finish();
		    		}
		        });
    		}
    	};
    	thread.start();
    }

	/* ============================================
	 *  Nested classes
	 * ============================================ */
    /**
     * Alerter is a private inner class of LoadActivity and it is responsible for
     * updating the progress bar and showing alert dialogs if errors occur.
     */
	private class Alerter implements Runnable
	{
		/**
		 * Message to show in the alert dialog.
		 */
		private String message;
		
		/**
		 * Should config screen be opened after this alert or not.
		 */
		private boolean openConfig;
		
		/**
		 * Saves the parameters.
		 * 
		 * @param _message    Message to show
		 * @param _openConfig Open config dialog after this?
		 */
		public Alerter(String _message, boolean _openConfig)
		{
			message = _message;
			openConfig = _openConfig;
		}
		
		public void run()
		{
			if (!message.equals("SUCCESS")) {
				runOnUiThread(new Runnable() {
		    		public void run() {
		    			AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		    			alertDialog.setTitle(R.string.error_title);
		    			alertDialog.setMessage(message);
		    			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		    				public void onClick(DialogInterface dialog, int which) {
		    					
		    					if (openConfig) {
		    						setResult(0, null);
		    					}
		    					else {
		    						setResult(-2, null);
		    					}
		    					
		    					finish();
		    				}
		    			});
		    			alertDialog.setIcon(R.drawable.error);
		    			alertDialog.show();
		    		}
		    	});
			}
			else {
				runOnUiThread(new Runnable() {
		    		public void run() {
		        		ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar1);
		        		if (bar != null) {
		        			bar.incrementProgressBy(1);
		        		
			        		if (bar.getProgress() == 2) {
			        			TextView loadingText = (TextView) findViewById(R.id.loadingText);
					        	loadingText.setText(R.string.loadingText_1);
			        		}
			        		else if (bar.getProgress() == 8) {
			        			TextView loadingText = (TextView) findViewById(R.id.loadingText);
					        	loadingText.setText(R.string.loadingText_2);
			        		}
			        		else if (bar.getProgress() == 15) {
			        			TextView loadingText = (TextView) findViewById(R.id.loadingText);
					        	loadingText.setText(R.string.loadingText_3);
			        		}
		        		}
		    		}
		    	});
			}
		}
	}
}

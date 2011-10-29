package fi.leffat;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * InfoActivity shows more detailed information about the movie that is
 * passed to it via intent.
 */
public class SearchActivity extends Activity 
{
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

		setContentView(R.layout.searchscreen);
	}
	
	/**
	 * Handles searchbox.
	 */
	@Override
	public void onResume()
	{
		super.onResume();
		
		final EditText searchBox = (EditText) findViewById(R.id.searchText);
		searchBox.setOnKeyListener(new OnKeyListener()
		{
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
					search(searchBox.getText().toString());
				}
				
				return false;
			}
		});
	}
	
	public void search(String _search)
	{
		final ListView results = (ListView) findViewById(R.id.resultList);
		
		TextView result = new TextView(this);
		result.setText("Find it yourself...");
		
		results.addFooterView(result);
		
		results.invalidateViews();
		results.requestLayout();
	}
}

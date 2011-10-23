package fi.leffat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

/**
 * FileLoader is responsible for loading the XML-file from FinnKino's websites.
 */
public class FileLoader
{
	/**
	 * Enum for the result of actions.
	 */
	enum FileLoaderResult
	{
		SUCCESS(0),
		NO_AREACODE(1),
		INVALID_URL(2),
		NO_CONNECTION(3),
		CANNOT_OPEN_INPUT_STREAM(4),
		CANNOT_OPEN_OUTPUT_STREAM(5),
		INPUT_OUTPUT_EXCEPTION(6),
		CANNOT_CREATE_DIRECTORY(7);
		
		private final int value;
		
		private final String errorMsgs[] = {MainActivity.context.getString(R.string.success),
				MainActivity.context.getString(R.string.err_areacode),
				MainActivity.context.getString(R.string.err_invalid_url),
				MainActivity.context.getString(R.string.err_no_connection),
				MainActivity.context.getString(R.string.err_istream),
				MainActivity.context.getString(R.string.err_ostream),
				MainActivity.context.getString(R.string.err_io_exception)};

		private FileLoaderResult(int value) {
			this.value = value;
		}
		
		public String getValue() {
			return errorMsgs[value];
		}
	}
	
	/**
	 * Application context.
	 */
	private Context context;
	
	/**
	 * Area code.
	 */
	private String areaCode;
	
	/**
	 * Formatted dates.
	 */
	private String[] days;
	
	/**
	 * Tells if an SD-card is available.
	 */
	private boolean hasExternalStorage;
	
	/* ============================================
	 *  New methods
	 * ============================================ */
	/**
	 * Prepares FileLoader for opening connection to Finnkino and loading/saving files.
	 * 
	 * @param _context Application context
	 * 
	 * @return {@link FileLoaderResult}
	 */
	public FileLoaderResult prepare(Context _context)
	{
		context = _context;
		
		// Get area code from settings
		SharedPreferences leffaPrefs = context.getSharedPreferences("leffaPrefs", Context.MODE_WORLD_READABLE);
		areaCode = leffaPrefs.getString("AREA_CODE", null);
		
		if (areaCode == null) {
			return FileLoaderResult.NO_AREACODE;
		}
		
		// Generate dates
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		days = new String[7];
		
		Date date = new Date();
		for(int i = 0; i < 7; ++i) {
			days[i] = sdf.format(date.getTime() + MILLIS_IN_DAY * i);
		}
		
		// Clean up old files
		new Cleaner().run(context, days);
		
		// Check for SD-card
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			File folder = new File(Environment.getExternalStorageDirectory() + "/JustJoo");
			if (!folder.exists()) {
				if (!folder.mkdir()) {
					return FileLoaderResult.CANNOT_CREATE_DIRECTORY;
				}
			}
			
			hasExternalStorage = true;
		}
		else {
			hasExternalStorage = false;
		}
		
		return FileLoaderResult.SUCCESS;
	}
	
	/**
	 * Loads one XML-file from Finnkino.fi and saves it to the internal or external
	 * storage (prefers external storage if it is available).
	 * 
	 * @param _context  Application context
	 * @param _dayIndex Index of the day (0-6)
	 * 
	 * @return {@link FileLoaderResult}
	 */
	public FileLoaderResult loadFile(Context _context, int _dayIndex)
	{
		boolean fileAlreadyExists = false;
		
		// Check if the file already exists or not
		if (hasExternalStorage) {
			File file = new File(Environment.getExternalStorageDirectory() + "/JustJoo/finnkino_" + days[_dayIndex] + ".xml");
			if (file.exists()) {
				fileAlreadyExists = true;
			}
		}
		else {
			File file = _context.getFileStreamPath("finnkino_" + days[_dayIndex] + ".xml");
			if (file.exists()) {
				fileAlreadyExists = true;
			}
		}
		
		if (!fileAlreadyExists) {
			URL url;
			try {
				Log.e("USING URL", "http://www.finnkino.fi/xml/Schedule/?area="+areaCode+"&dt=" + days[_dayIndex]);
				url = new URL("http://www.finnkino.fi/xml/Schedule/?area="+areaCode+"&dt=" + days[_dayIndex]);
			}
			catch (Exception e) {
				return FileLoaderResult.INVALID_URL;
			}
	
			// Open connection
			HttpURLConnection ucon = null;
			try {
				ucon = (HttpURLConnection) url.openConnection();
				ucon.setRequestMethod("GET");
				ucon.setDoOutput(true);
				ucon.connect();
			}
			catch (Exception e) {
				return FileLoaderResult.NO_CONNECTION;
			}
			
			// Prepare for reading the file
			InputStream streamIn;
			try {
				streamIn = ucon.getInputStream();
			} catch (Exception e) {
				return FileLoaderResult.CANNOT_OPEN_INPUT_STREAM;
			}
			
			FileOutputStream streamOut;
			try {
				if (hasExternalStorage) {
					streamOut = new FileOutputStream(new File(Environment.getExternalStorageDirectory()+"/JustJoo/finnkino_" + days[_dayIndex] + ".xml"));
				}
				else {
					streamOut = _context.openFileOutput("finnkino_" + days[_dayIndex] + ".xml", Context.MODE_PRIVATE);
				}
			} catch (Exception e) {
				return FileLoaderResult.CANNOT_OPEN_OUTPUT_STREAM;
			}
				
			// Read the file until EOF
			byte[] buffer = new byte[1024];
			int length = 0;
			try {
				while ((length = streamIn.read(buffer)) != -1) {
					streamOut.write(buffer, 0, length);
				}
			}
			catch (Exception e) {
				return FileLoaderResult.INPUT_OUTPUT_EXCEPTION;
			}
			ucon.disconnect();
		}
		
        return FileLoaderResult.SUCCESS;
	}
}
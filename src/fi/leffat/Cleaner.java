package fi.leffat;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

/**
 * Deletes unnecessary files from both the external and internal storage.
 */
public class Cleaner
{
	/**
	 * Executes the deletion process.
	 * 
	 * @param _context  Application context required for handling files
	 * @param _reqFiles Formatted days (the necessary files)
	 */
	public void run(Context _context, String[] _reqFiles)
	{
		SharedPreferences leffaPrefs = _context.getSharedPreferences("leffaPrefs", Context.MODE_WORLD_READABLE);

		File dir;
		
		if (leffaPrefs.getBoolean("AREA_CODE_CHANGED", true)) {
	        SharedPreferences.Editor prefsEditor = leffaPrefs.edit();
	        prefsEditor.putBoolean("AREA_CODE_CHANGED", false);
            prefsEditor.commit();
			
			// Clean up external storage if available
			if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
				dir = new File(Environment.getExternalStorageDirectory() + "/JustJoo");
	
				if (dir.exists() && dir.isDirectory()) {
					deleteAll(dir);
				}
			}
			
			// Clean up internal storage
			dir = _context.getFilesDir();
			deleteAll(dir);
		}
		else {
			// Clean up external storage if available
			if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
				dir = new File(Environment.getExternalStorageDirectory() + "/JustJoo");
	
				if (dir.exists() && dir.isDirectory()) {
					deleteNotNeeded(dir, _reqFiles);
				}
			}
			
			// Clean up internal storage
			dir = _context.getFilesDir();
			deleteNotNeeded(dir, _reqFiles);
		}
	}
	
	/**
	 * Deletes all files that are not in the list of required files (_reqFiles).
	 * 
	 * @param _dir      File directory
	 * @param _reqFiles Required files
	 */
	private void deleteNotNeeded(File _dir, String[] _reqFiles)
	{
		// Get a list of all files
		String[] files = _dir.list();
		
		// Delete files that are not on both lists
		for (int i = files.length - 1; i >= 0; --i) {
			for (int j = _reqFiles.length - 1; j >= 0; --j) {
				if (("finnkino_"+_reqFiles[j]+".xml").equals(files[i])) {
					break;
				}
				else if (j == 0) {
					new File(_dir, files[i]).delete();
				}
			}
		}
	}
	
	/**
	 * Deletes absolutely everything.
	 * 
	 * @param _dir File directory
	 */
	private void deleteAll(File _dir)
	{
		// Get a list of all files
		String[] files = _dir.list();
		
		// Delete files that are not on both lists
		for (int i = files.length - 1; i >= 0; --i) {
			new File(_dir, files[i]).delete();
		}
		
		files = _dir.list();
	}
}

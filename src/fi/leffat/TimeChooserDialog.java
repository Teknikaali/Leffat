package fi.leffat;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TimeChooserDialog extends Dialog
{
	public TimeChooserDialog(Context context) 
	{
		super(context);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timepickerdialog);
	}
	
	public class OnClick implements OnClickListener
	{
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			
		}
	}
}

/**
 * Added by zhangyanhu C01012,2014-7-17
 */
package com.icatch.wcm2app.ExtendComponent;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.icatch.wcm2app.R;

/**
 * Added by zhangyanhu C01012,2014-7-17
 */
public class DownloadDialog extends Dialog{
	/**
	 * Added by zhangyanhu C01012,2014-7-17
	 */
	private TextView title;
	private TextView message;
	private ListView downloadStatus;
	private Button exit;
	public DownloadDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.download_dialog);
		title = (TextView)findViewById(R.id.title);
		message = (TextView)findViewById(R.id.message);
		downloadStatus = (ListView)findViewById(R.id.downloadStatus);
		exit = (Button)findViewById(R.id.exit);
	}
	
	public void setTitle(String myTitle){
		title.setText(myTitle);
	}
	
	public void setMessage(String myMessage){
		message.setText(myMessage);
	}
	
	public void setAdapter(ListAdapter adapter){
		downloadStatus.setAdapter(adapter);
	}
	
	public Button getDrawBackButton(){
		return exit;
	}
}

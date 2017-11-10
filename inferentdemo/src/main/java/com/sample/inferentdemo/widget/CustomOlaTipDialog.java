package com.sample.inferentdemo.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.sample.inferentdemo.R;


public class CustomOlaTipDialog extends Dialog {
	public interface CustomOlaTipDialogListener {
		public void onOkButtonPress();

		
	}

	private CustomOlaTipDialogListener listener = null; 
	private static TextView mMessage;
	private static TextView ok  ;
	private static CustomOlaTipDialog dialog;
	
	
    public CustomOlaTipDialog(Context context){
        super(context);
    }
     
    public CustomOlaTipDialog(Context context, int theme) {
        super(context, theme);
    }
     

	public static CustomOlaTipDialog createDialog(Context context) {
		dialog = new CustomOlaTipDialog(context);
		
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		dialog.setContentView(R.layout.custom_tip_dialog);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dialog_box_content));
		
		mMessage =(TextView)dialog.findViewById(R.id.loadingmsg);
		ok = (TextView)dialog.findViewById(R.id.text_ok);
	
		return dialog;
	}
	
	public void setMessage(String strMessage) {
		if (mMessage == null) {
			mMessage = (TextView) dialog.findViewById(R.id.loadingmsg);
		}
		mMessage.setText(strMessage);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (dialog == null)
			return;
		
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onOkButtonPress();
				}
				dialog.cancel();
			}
		});
	}

	public void setListener(final CustomOlaTipDialogListener listener) {
		this.listener = listener;
	}
}
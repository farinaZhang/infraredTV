package com.sample.inferentdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class RotateView extends View {
	private int width;
	private int height;
	private float degree;
	private double max_width;
	private Bitmap rotate_image;
	private Paint paint = new Paint();
	private Handler handler;
	private int dalay_time = 20;
	private final int START_ROTATE = 1 ;
	//旋转速度
	private final int START_SPEED = 10;
	private boolean isSpeed = false;
	public RotateView(Context context, AttributeSet attrs){
		super(context, attrs);
		initHandler();
	}
	public RotateView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHandler();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));  
		Matrix matrix = new Matrix();
		matrix.setTranslate((float) width / 2, (float) height / 2);
		matrix.preRotate(degree);
		matrix.preTranslate(-(float) (width / 2), -(float) (height / 2));
		matrix.postTranslate((float) (max_width - width) / 2,(float) (max_width - height) / 2);
		canvas.drawBitmap(rotate_image, matrix, paint);
	}
	
	private void initHandler(){
		paint.setAntiAlias(true); 
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case START_ROTATE:
					addDegree();
					handler.sendEmptyMessageDelayed(START_ROTATE, dalay_time);
					invalidate(); //更新
					break;
				default:
					break;
				}
			}
		};
	}	
	
	public void setImageDrawable(int id){
		BitmapDrawable drawable = (BitmapDrawable) getContext().getResources().getDrawable(id);
		rotate_image = drawable.getBitmap();
		drawable.setAntiAlias(true);
		width = rotate_image.getWidth();
		height = rotate_image.getHeight();
		max_width = Math.sqrt(width * width + height + height);
		postInvalidate();
	}
	
	private void addDegree(){
		degree += START_SPEED;
		if (degree > 360 || degree < -360){
			degree = degree % 360;
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension((int) max_width, (int) max_width);
	}
	
	public void startRotate(){
		if(isSpeed == false){
			isSpeed = true;
			handler.sendEmptyMessage(START_ROTATE);
		}
	}
	
	public void stopRotate(){
		if(isSpeed){
			isSpeed = false;
			handler.removeMessages(START_ROTATE);
		}
	}
	
	

}

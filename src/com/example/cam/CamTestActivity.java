package com.example.cam;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.buaa.network.HttpUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

public class CamTestActivity extends Activity {
	private static final String TAG = "CamTestActivity";
	Preview preview;
	Button buttonClick;
	Camera camera;
	String fileName;
	Activity act;
	Context ctx;
	Button uploadButton;
	String trainName ;
	private int picCount = 0;
	String APP_SDCARD_FOLDER = 
	            Environment.getExternalStorageDirectory().getAbsolutePath();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent(); //用于激活它的意图对象
        
		trainName = intent.getStringExtra("Name");
        System.out.println("user name "+trainName);
        
        
        startAddPic();
		
		ctx = this;
		act = this;
		picCount = 0;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);
		
		//LinearLayout myLayout = new LinearLayout(context);  
		
		preview = new Preview(this, (SurfaceView)findViewById(R.id.surfaceView));
		preview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		((FrameLayout) findViewById(R.id.preview)).addView(preview);
		preview.setKeepScreenOn(true);
		
		buttonClick = (Button) findViewById(R.id.buttonClick);
		
		buttonClick.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//				preview.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
				camera.takePicture(shutterCallback, rawCallback, jpegCallback);
			}
		});
		
		buttonClick.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View arg0) {
				camera.autoFocus(new AutoFocusCallback(){
					@Override
					public void onAutoFocus(boolean arg0, Camera arg1) {
						//camera.takePicture(shutterCallback, rawCallback, jpegCallback);
					}
				});
				return true;
			}
		});
		
//		//Use code to create button
//		uploadButton = new Button(ctx);
//		uploadButton.setWidth(LayoutParams.WRAP_CONTENT);
//		uploadButton.setHeight(LayoutParams.WRAP_CONTENT);
//		uploadButton.setText("upload");
//		uploadButton.setGravity(Gravity.CENTER);
//		//uploadButton.setVisibility(0);
//		    
//		//preview.addView(uploadButton);
//		
//		uploadButton.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				//TODO connet the server
//				
//			}
//		});
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		//      preview.camera = Camera.open();
		camera = Camera.open();
		camera.startPreview();
		preview.setCamera(camera);
	}

	@Override
	protected void onPause() {
		if(camera != null) {
			camera.stopPreview();
			preview.setCamera(null);
			camera.release();
			camera = null;
		}
		super.onPause();
	}

	private void resetCam() {
		camera.startPreview();
		preview.setCamera(camera);
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			// Log.d(TAG, "onShutter'd");
		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			// Log.d(TAG, "onPictureTaken - raw");
		}
	};

	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream = null;
			try {
				// Write to SD Card
				String Filename = System.currentTimeMillis()+"";
				String storepath = APP_SDCARD_FOLDER+"/liuyuxiao/";
				File destDir = new File(storepath);
				  if (!destDir.exists()) {
				   destDir.mkdirs();
				  }

				fileName = String.format("%s.jpg", Filename);
				outStream = new FileOutputStream(storepath+fileName);
				outStream.write(data);
				outStream.close();
				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
				
				upload(new File(storepath+fileName));
				
				System.out.println("批次countchange"+picCount);
				// 计数增加
				picCount ++;
				//System.out.println("pic count"+picCount);
				if(picCount == 3)
				{
					buttonClick.setText("上传");
					
					buttonClick.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							startTrain();
						}
					});
					
				}
				resetCam();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println(e.toString());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e.toString());
			} finally {
				
			}
			Log.d(TAG, "onPictureTaken - jpeg");
		}
	};
	
	
	public void upload(File myfile) {

		RequestParams params = new RequestParams();
		params.put("person_name", trainName);
		try {
			System.out.println("file size"+myfile.length());
			params.put("file", myfile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpUtil.post(params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonobject) {
				// TODO Auto-generated method stub
				super.onSuccess(jsonobject);
				String statuscode = null;
				try {
					statuscode = jsonobject.getString("code");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("return json data"+jsonobject.toString());
			}

			public void onFailure(Throwable arg0) { // 失败，调用
				System.out.println("onfailure");
				//status = 2;
			}

			public void onFinish() { // 完成后调用，失败，成功，都要掉
				System.out.println("onfinish");
			}

			@Override
			protected void handleFailureMessage(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub

				super.handleFailureMessage(arg0, arg1);
				//status = 2;
				System.out.println("onfailuremessage" + arg0 + arg1);
			};

		},"/postAddFace/");
	}
	
	
	public void startAddPic() {

		RequestParams params = new RequestParams();
		params.put("person_name", trainName);
		HttpUtil.post(null, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonobject) {
				// TODO Auto-generated method stub
				super.onSuccess(jsonobject);
				String statuscode = null;
				try {
					statuscode = jsonobject.getString("code");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("return json data"+jsonobject.toString());
			}

			public void onFailure(Throwable arg0) { // 失败，调用
				System.out.println("onfailure");
				//status = 2;
			}

			public void onFinish() { // 完成后调用，失败，成功，都要掉
				System.out.println("onfinish");
			}

			@Override
			protected void handleFailureMessage(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub

				super.handleFailureMessage(arg0, arg1);
				//status = 2;
				System.out.println("onfailuremessage" + arg0 + arg1);
			};

		},"/postAddPerson/");
	}
	
	public void startTrain() {

		RequestParams params = new RequestParams();
		//params.put("request", "train");
		HttpUtil.post(null, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonobject) {
				// TODO Auto-generated method stub
				super.onSuccess(jsonobject);
				String statuscode = null;
				try {
					statuscode = jsonobject.getString("code");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("return json data"+jsonobject.toString());
			}

			public void onFailure(Throwable arg0) { // 失败，调用
				System.out.println("onfailure");
				//status = 2;
			}

			public void onFinish() { // 完成后调用，失败，成功，都要掉
				System.out.println("onfinish");
			}

			@Override
			protected void handleFailureMessage(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub

				super.handleFailureMessage(arg0, arg1);
				//status = 2;
				System.out.println("onfailuremessage" + arg0 + arg1);
			};

		},"/postTrain/");
	}
}

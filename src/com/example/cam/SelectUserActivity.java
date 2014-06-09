package com.example.cam;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.buaa.network.HttpUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SelectUserActivity extends Activity {
	private static String[] mNamelist= null;
	private TextView view ;
	private Spinner spinner;
	private ArrayAdapter<String> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	
		setContentView(R.layout.selectuser);
		
		upload(null);
		
		view = (TextView) findViewById(R.id.spinnerText);
		spinner = (Spinner) findViewById(R.id.Spinner01);
		//����ѡ������ArrayAdapter��������
		
		
	}

	
	
	public void upload(String message) {

		RequestParams params = new RequestParams();
		HttpUtil.get("/getPersonList", new JsonHttpResponseHandler() {

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

				//System.out.println("jsonobject"+jsonobject.toString());
				
				ArrayList<String> myNameArray = new ArrayList<String>();
				
				myNameArray.add("��ѡ��");
				
				for(int i=1;i<10;i++)
				{
					try {
						String oneperson = jsonobject.getString(i+"");
						System.out.println("oneperson"+oneperson);
						myNameArray.add(oneperson);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						System.out.println(e.toString());
						break;
					}			
					
				}
				
				mNamelist = (String[]) myNameArray.toArray(new String[0]);
				
			System.out.println("person name length"+mNamelist.length);
				
				
			adapter = new ArrayAdapter<String>(SelectUserActivity.this,android.R.layout.simple_spinner_item,mNamelist);
			
			//���������б�ķ��
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			//��adapter ��ӵ�spinner��
			spinner.setAdapter(adapter);
			
			//����¼�Spinner�¼�����  
			spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
			
			//����Ĭ��ֵ
			spinner.setVisibility(View.VISIBLE);	
				
				
			}

			public void onFailure(Throwable arg0) { // ʧ�ܣ�����
				System.out.println("onfailure");
				//status = 2;
			}

			public void onFinish() { // ��ɺ���ã�ʧ�ܣ��ɹ�����Ҫ��
				System.out.println("onfinish");
			}

			@Override
			protected void handleFailureMessage(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub

				super.handleFailureMessage(arg0, arg1);
				//status = 2;
				System.out.println("onfailuremessage" + arg0 + arg1);
			};

		});
	}
	
	
	//ʹ��������ʽ����
		class SpinnerSelectedListener implements OnItemSelectedListener{

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				view.setText("����Glass����˭��");
				if(arg2 != 0)
				{
					view.setText("����Glass����˭��"+mNamelist[arg2]);
				Intent intent = new Intent(getApplicationContext(), CamTestActivity.class);        
		            intent.putExtra("Name", mNamelist[arg2]);
		            startActivity(intent);
		            }
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		}
}

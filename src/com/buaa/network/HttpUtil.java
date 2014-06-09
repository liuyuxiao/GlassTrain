package com.buaa.network;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUtil {
    private static     AsyncHttpClient client =new AsyncHttpClient();   
    private static String baseurl = "http://115.28.246.138:8080";
    
    static
    {
        client.setTimeout(50000);  
    }
    public static void get(RequestParams params,AsyncHttpResponseHandler res)  
    {
        client.get(baseurl, params,res);
    }
    public static void get(String params,JsonHttpResponseHandler res)  
    {
     
        client.get(baseurl +params, null,res);
    }
    public static void get(String urlString,RequestParams params,JsonHttpResponseHandler res)   
    {
        client.get(urlString, params,res);
    }
    public static void get(String uString, BinaryHttpResponseHandler bHandler)   
    {
        client.get(uString, bHandler);
    }
    public static AsyncHttpClient getClient()
    {
        return client;
    }
    public static void post(RequestParams params,JsonHttpResponseHandler res,String addpath) 
    {
    	client.post(baseurl+addpath, params, res);
    	System.out.println("client message"+client.getHttpClient().getParams());
    	//System.out.println("client message url"+client.getHttpClient());
    }
	public static void post(RequestParams params,
			AsyncHttpResponseHandler res) {
		// TODO Auto-generated method stub
		client.post(baseurl, params, res);
	}
}
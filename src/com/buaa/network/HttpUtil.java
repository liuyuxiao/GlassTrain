package com.buaa.network;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUtil {
    private static     AsyncHttpClient client =new AsyncHttpClient();   
    private static String baseurl = "http://app.feimou.com";
    
    static
    {
        client.setTimeout(10000);  
    }
    public static void get(RequestParams params,AsyncHttpResponseHandler res)  
    {
        client.get(baseurl, params,res);
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
    public static void post(RequestParams params,JsonHttpResponseHandler res) 
    {
    	client.post(baseurl, params, res);
    }
	public static void post(RequestParams params,
			AsyncHttpResponseHandler res) {
		// TODO Auto-generated method stub
		client.post(baseurl, params, res);
	}
}
package com.nitkkr.gawds.tech16.src;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Home Laptop on 20-Dec-16.
 */

class InputStreamVolleyRequest extends Request<byte[]>
{
	private final Response.Listener<byte[]> mListener;
	private Map<String, String> mParams;

	public Map<String, String> responseHeaders ;

	public InputStreamVolleyRequest(int method, String mUrl ,Response.Listener<byte[]> listener,
	                                Response.ErrorListener errorListener, HashMap<String, String> params)
	{
		super(method, mUrl, errorListener);
		setShouldCache(false);
		mListener = listener;
		mParams=params;
	}

	@Override
	protected Map<String, String> getParams() throws com.android.volley.AuthFailureError
	{
		return mParams;
	}

	@Override
	protected void deliverResponse(byte[] response)
	{
		mListener.onResponse(response);
	}

	@Override
	protected Response<byte[]> parseNetworkResponse(NetworkResponse response)
	{
		//Initialise local responseHeaders map with response headers received
		responseHeaders = response.headers;

		//Pass the response data here
		return Response.success( response.data, HttpHeaderParser.parseCacheHeaders(response));
	}
}
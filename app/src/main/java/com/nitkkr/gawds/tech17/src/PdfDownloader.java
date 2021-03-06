package com.nitkkr.gawds.tech17.src;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.nitkkr.gawds.tech17.R;
import com.nitkkr.gawds.tech17.helper.ActivityHelper;
import com.nitkkr.gawds.tech17.helper.ResponseStatus;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import static com.nitkkr.gawds.tech17.helper.ActivityHelper.getApplicationContext;

/**
 * Created by Home Laptop on 20-Dec-16.
 */

public class PdfDownloader
{
	class Holder
	{
		public int ID;
		public iCallback callback;
	}

	HashMap<String, Holder> Downloading;

	private static PdfDownloader pdfHelper = new PdfDownloader();

	public static PdfDownloader getInstance()
	{
		return pdfHelper;
	}

	private PdfDownloader()
	{
		Downloading = new HashMap<>();
	}

	private File getDataFolder()
	{
		File dataDir = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			dataDir = new File(Environment.getExternalStorageDirectory(), getApplicationContext().getString(R.string.FolderName));
			if (!dataDir.isDirectory())
			{
				dataDir.mkdirs();
			}
		}

		if (!dataDir.isDirectory())
		{
			dataDir = getApplicationContext().getDir(getApplicationContext().getString(R.string.FolderName), Context.MODE_PRIVATE);
		}

		return dataDir;
	}

	public interface iCallback
	{
		void DownloadComplete(String url, ResponseStatus status);
	}

	public String getFileName(String url)
	{
		return URLUtil.guessFileName(url, null, null);
	}

	public boolean isPdfExisting(String FileName)
	{
		return new File(getDataFolder(), FileName + ".pdf").exists();
	}

	public boolean isPdfDownloading(String url)
	{
		return Downloading.keySet().contains(getFileName(url));
	}

	public void DownloadPdf(final String url, final String FileName, final iCallback callback, final Context context)
	{
		if(!PdfExists(url))
		{
			Toast.makeText(context,"Pdf Unavailable",Toast.LENGTH_SHORT).show();
			if (callback != null)
			{
				callback.DownloadComplete(url, ResponseStatus.FAILED);
			}
			return;
		}
		if (isPdfExisting(FileName))
		{
			viewPdfIfExists(FileName, context);
			if (callback != null)
			{
				callback.DownloadComplete(url, ResponseStatus.SUCCESS);
			}
			return;
		}

		if (!ActivityHelper.isInternetConnected())
		{
			Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
			if (callback != null)
			{
				callback.DownloadComplete(url, ResponseStatus.FAILED);
			}
			return;
		}


		final Holder holder = new Holder();
		holder.callback = callback;
		NotificationGenerator generator = new NotificationGenerator(context);

		holder.ID = generator.pdfNotification("Downloading Pdf", "Downloading", FileName + ".pdf");

		Downloading.put(getFileName(url), holder);

		InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, url,
				new Response.Listener<byte[]>()
				{
					@Override
					public void onResponse(byte[] response)
					{
						try
						{
							if (response != null)
							{
								File Path = new File(getDataFolder(), FileName + ".pdf");
								FileOutputStream outputStream = new FileOutputStream(Path);
								outputStream.write(response);
								outputStream.close();

								try
								{
									Holder holder1 = Downloading.get(getFileName(url));
									iCallback call = holder1.callback;
									Downloading.remove(getFileName(url));


									File file = new File(getDataFolder(), FileName + ".pdf");
									Intent target = new Intent(Intent.ACTION_VIEW);
									target.setDataAndType(Uri.fromFile(file), "application/pdf");
									target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

									Intent intent = Intent.createChooser(target, "Open File");

									NotificationGenerator generator1 = new NotificationGenerator(context);
									generator1.pdfNotification(holder1.ID, "Download Complete", "Download Complete", FileName + ".pdf", intent, true);

									if (call != null)
									{
										call.DownloadComplete(url, ResponseStatus.SUCCESS);
									}
								}
								catch (Exception e)
								{
									e.printStackTrace();
									throw new Exception("Error");
								}
							}
							else
							{
								throw new Exception("No Response");
							}
						}
						catch (Exception e)
						{
							try
							{
								Holder holder1 = Downloading.get(getFileName(url));
								iCallback call = holder1.callback;
								Downloading.remove(getFileName(url));

								NotificationGenerator generator1 = new NotificationGenerator(context);
								generator1.pdfNotification(holder1.ID, "Download Failed", "Download Failed", FileName + ".pdf", null, true);

								if (call != null)
								{
									call.DownloadComplete(url, ResponseStatus.FAILED);
								}
							}
							catch (Exception ex)
							{
								ex.printStackTrace();
							}

							Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
							e.printStackTrace();
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						try
						{
							Holder holder1 = Downloading.get(getFileName(url));
							iCallback call = holder1.callback;
							Downloading.remove(getFileName(url));

							NotificationGenerator generator1 = new NotificationGenerator(context);
							generator1.pdfNotification(holder1.ID, "Download Failed", "Download Failed", FileName + ".pdf", null, true);

							if (call != null)
							{
								call.DownloadComplete(url, ResponseStatus.FAILED);
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}

						Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
						error.printStackTrace();
					}
				}, null);

		//request.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,	DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		request.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack());
		mRequestQueue.add(request);
	}

	public void viewPdfIfExists(String FileName, Context context)
	{
		if (!isPdfExisting(FileName))
		{
			return;
		}

		File file = new File(getDataFolder(), FileName + ".pdf");
		Intent target = new Intent(Intent.ACTION_VIEW);
		target.setDataAndType(Uri.fromFile(file), "application/pdf");
		target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

		Intent intent = Intent.createChooser(target, "Open File");
		try
		{
			context.startActivity(intent);
		}
		catch (Exception e)
		{
			Toast.makeText(context, "Install a Pdf Viewer", Toast.LENGTH_LONG).show();
		}
	}

	public void removeListener(String url)
	{
		if (isPdfDownloading(url))
		{
			Downloading.put(getFileName(url), null);
		}
	}

	public static boolean PdfExists(String url)
	{
		/*try {
			HttpURLConnection.setFollowRedirects(false);
			// note : you may also need
			//        HttpURLConnection.setInstanceFollowRedirects(false)
			HttpURLConnection con =
					(HttpURLConnection) new URL(URLName).openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}*/
		return !url.equals("");
	}

}

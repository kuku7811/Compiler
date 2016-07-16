package com.java.compiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;
import android.app.*;
import android.content.*;
import android.content.res.*;
import java.io.*;
import com.java.utils.*;

public class LoaderActivity extends Activity
{
    public String s="";
	public String fn="";
	public String address="";
	public String path="";
	Address a=new Address();
	File file;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		address = a.getAddress();
		if(address.length()==0){
			exit();
		}
		path = a.getPath();
		File f=new File(path);
		if(!f.exists()){
			f.mkdirs();
		}
		file=new File(path + "/" + getLocalFileName(address));
		fn=file.toString();
		s = getLocalFileName(address);
		downloadFile(a.getAddress());
	}

	private void downloadFile(String url)
	{
		final ProgressDialog progressDialog = new ProgressDialog(this);
		new AsyncTask<String, Integer, File>() {
			private Exception m_error = null;

			@Override
			protected void onPreExecute()
			{
				progressDialog.setMessage("Загрузка из:\n" + address +"\nв:\n"+file.toString()+"\n"+a.getSize());
				progressDialog.setCancelable(false);
				progressDialog.setMax(100);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.show();
			}
			@Override
			protected File doInBackground(String... params)
			{
				URL url;
				HttpURLConnection urlConnection;
				InputStream inputStream;
				int totalSize;
				int downloadedSize;
				byte[] buffer;
				int bufferLength;
				FileOutputStream fos = null;
				try
				{
					url = new URL(params[0]);
					urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setRequestMethod("GET");
					urlConnection.setDoOutput(true);
					urlConnection.connect();
					fos = new FileOutputStream(file);
					inputStream = urlConnection.getInputStream();
					totalSize = urlConnection.getContentLength();
					downloadedSize = 0;
					buffer = new byte[1024*50];
					bufferLength = 0;
					while ((bufferLength = inputStream.read(buffer)) > 0)
					{
						fos.write(buffer, 0, bufferLength);
						downloadedSize += bufferLength;
						publishProgress(downloadedSize, totalSize);
						a.setSize(downloadedSize+" / "+totalSize);
						
					}
					fos.close();
					inputStream.close();
					return file;
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				return null;
			}
			protected void onProgressUpdate(Integer... values)
			{
				progressDialog.setProgress((int) ((values[0] / (float) values[1]) * 100));
				progressDialog.setMessage("Загрузка из:\n" + address +"\nв:\n"+file.toString()+"\n"+a.getSize());
			};

			@Override
			protected void onPostExecute(File file)
			{
				progressDialog.hide();
				exit();
			}
		}.execute(url);
    }
	public String getLocalFileName(String address)
	{
		int lastSlashIndex = address.lastIndexOf('/');
		if (lastSlashIndex >= 0 &&
			lastSlashIndex < address.length() - 1)
		{
			address = address.substring(lastSlashIndex + 1);
		}
		return address;
	}
	public String stripClassName(String className, int x)
	{
        return className.substring(0, className.length() - x);
    }
	public void exit()
	{
		Intent intent = new Intent();
		intent.setClass(LoaderActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	public void Toast(String s)
	{
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
	}
}
package com.java.compiler;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.os.*;
import android.content.*;
import android.widget.*;
import android.app.*;
import android.content.*;
import android.content.res.*;
import java.io.*;
import java.util.zip.*;
import android.util.*;
import java.util.*;
import com.java.utils.*;

public class UnzipActivity extends Activity
{
	public String s="";
	public String fn="";
	public String from="";
	public String to="";
	Address a=new Address();
	File file;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		from = a.getFrom();
		to = a.getTo();
		unzip(from, to);
	}

	private void unzip(final String from, final String to)
	{
		final ProgressDialog progressDialog = new ProgressDialog(this);
		new AsyncTask<String, Integer, File>() {
			private Exception m_error = null;

			@Override
			protected void onPreExecute()
			{
				progressDialog.setMessage("Распаковка из:\n" + from + "\nв:\n" + to + "\n" + a.getSize());
				progressDialog.setCancelable(false);
				progressDialog.setMax(100);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.show();
			}
			@Override
			protected File doInBackground(String... params)
			{
				try
				{
					String szZipFilePath = params[0];
					String szExtractPath = params[1];
					int i;
					ZipFile zf;
					Vector zipEntries = new Vector();
					try
					{
						zf = new ZipFile(szZipFilePath);
						Enumeration en = zf.entries();
						while (en.hasMoreElements())
						{
							zipEntries.addElement(en.nextElement());
						}
						for (i = 0; i < zipEntries.size(); i++)
						{
							ZipEntry ze = (ZipEntry)zipEntries.elementAt(i);
							publishProgress(i, zipEntries.size());
							a.setSize(i + " / " + zipEntries.size());
							extractFromZip(szZipFilePath, szExtractPath, ze.getName(), zf, ze);
						}
						zf.close();
						Log.i("Unzip", "Done");
					}
					catch (Exception ex)
					{
						Log.e("Unzip", "Error in unzip", ex);
					}

					return file;
				}
				catch (Exception e)
				{
					alert(e.toString());
					m_error = e;
				}
				return null;
			}
			protected void onProgressUpdate(Integer... values)
			{
				progressDialog.setProgress((int) ((values[0] / (float) values[1]) * 100));
				progressDialog.setMessage("Распаковка из:\n" + from + "\nв:\n" + to + "\n" + a.getSize());
			};

			@Override
			protected void onPostExecute(File file)
			{
				if (m_error != null)
				{
					alert(m_error.toString());
					return;
				}
				progressDialog.hide();
				Delete.delete(from);
				exit();
			}
		}.execute(from, to);
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
		intent.setClass(UnzipActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	public void Toast(String s)
	{
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
	}
	public void alert(String error)
	{
		AlertDialog.Builder msg=new AlertDialog.Builder(this);

		msg.setMessage(error).setCancelable(false);
		msg.setPositiveButton("Да", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface d, int id)
				{

					unzip(a.getFrom(), a.getTo());
				}
			});
		msg.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface d, int id)
				{
					copyFromAssets("help.zip", a.getSrc() + "/help.zip");
					unzip(a.getSrc() + "/help.zip", a.getSrc());
					d.dismiss();
					exit();
				}
			});
		msg.show();
	}

	public void copyFromAssets(String from, String to)
	{
		try
		{
			AssetManager am = getAssets();
			InputStream is=am.open(from);
			OutputStream os=new FileOutputStream(to);
			byte[] buffer=new byte[1024 * 20];
			int i;
			while ((i = is.read(buffer)) != -1)
			{
				os.write(buffer, 0, i);
			}
			is.close();
			os.flush();
			os.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


// ============================================
// extractFromZip
// ============================================
	static void extractFromZip(String szZipFilePath, String szExtractPath, String szName, ZipFile zf, ZipEntry ze)
	{
		if (ze.isDirectory()) return;
		String szDstName = slash2sep(szName);
		String szEntryDir;
		if (szDstName.lastIndexOf(File.separator) != -1)
		{
			szEntryDir = szDstName.substring(0, szDstName.lastIndexOf(File.separator));
		}
		else
			szEntryDir = "";
		try
		{
			File newDir = new File(szExtractPath + File.separator + szEntryDir);
			newDir.mkdirs();
			FileOutputStream fos = new FileOutputStream(szExtractPath + File.separator + szDstName);
			InputStream is = zf.getInputStream(ze);
			byte[] buf = new byte[1024 * 50];
			int nLength;
			while (true)
			{
				try
				{
					nLength = is.read(buf);
				}
				catch (EOFException ex)
				{
					break;
				}
				if (nLength < 0) break;
				fos.write(buf, 0, nLength);
			}
			is.close();
			fos.close();
		}
		catch (Exception ex)
		{
			Log.e("Unzip", "Error in extractFromZip", ex);
		}
	}
// ============================================
// slash2sep
// ============================================
	static String slash2sep(String src)
	{
		int i;
		char[] chDst = new char[src.length()];
		String dst;
		for (i = 0; i < src.length(); i++)
		{
			if (src.charAt(i) == '/')
				chDst[i] = File.separatorChar;
			else
				chDst[i] = src.charAt(i);
		}
		dst = new String(chDst);
		return dst;
	}
}
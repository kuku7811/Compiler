package com.java.compiler;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import android.content.*;
import android.content.res.*;
import com.java.utils.*;
import bsh.*;

public class MainActivity extends Activity
{
	EditText ed;
    boolean flag=true;
	String reset="",path="",def="";
	Address a=new Address();
	String fn=a.address;
	RW rw=new RW();

    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ide_main);
		ed = (EditText)findViewById(R.id.ed);
		init();
    }

	public void init()
	{
		path = "/mnt/sdcard/Compiler";
		def = path + "/tmp/default";
		a.setPath(path);
		a.setSrc(path + "/src");
		a.setTmp(path + "/tmp");
		if (!new File(def).exists())
		{
			help();
		}
		start();
		if (fn.length() == 0)
		{
			fn = rw.read(def);
		}
		a.setAddress(fn);
		ed.setText(rw.read(fn));
		syntax();
	}
	
	public void help()
	{
		copyFromAssets("help.zip", a.getPath() + "/help.zip");
		rw.write(def, a.getSrc() + "/Main.java");
		a.setAddress(a.getSrc() + "/Main.java");
		unzip(a.getPath() + "/help.zip", a.getPath());
	}
	
	public void start()
	{
		String[] dirs={"bin","boot","libs","src","res","tmp"};
		create(path, dirs);
		if (!new File(path + "/boot/rt.jar").exists())
		{
			copyFromAssets("data.zip", path + "/boot/data.zip");
			unzip(path + "/boot/data.zip", path + "/boot");
		}
	}

	public void download(String from, String to)
	{
		a.setAddress(from);
		a.setPath(to);
		a.setSrc(path + "/src");
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, LoaderActivity.class);
		startActivity(intent);
		finish();
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

	public void create(String dir, String[] ff)
	{
		for (String fn:ff)
		{
			new File(dir + "/" + fn).mkdirs();
		}
	}

	public void create(String fn)
	{
		new File(fn).mkdirs();
	}

	public void Toast(String s)
	{
		Toast.makeText(this, s, Toast.LENGTH_LONG).show();
	}

	public void run()
	{
		String in=path + "/tmp/in";
		if (!new File(in).exists())
		{
			rw.write(in, "");
		}
		String out=path + "/tmp/out";
		String err=path + "/tmp/err";
		String p="";
		try
		{
			if (flag == true)
			{
				rw.setOut(in, out, err);
				reset = ed.getText().toString();
				if (fn.endsWith(".bsh"))
				{
					rw.write(fn, reset);
					Interpreter i=new Interpreter();
					i.eval(reset);
				}
				if (fn.endsWith(".java"))
				{
					a.setPath(path);
					a.setTmp(path + "/tmp");
					String src="";
					if (fn.contains("/src"))
					{
						src = new File(fn.substring(0, fn.indexOf("/src/") + 5)).getAbsolutePath();
					}
					else
					{
						src = new File(fn).getParent();
						a.setAddress(fn);
					}
					a.setSrc(src);
					Compile comp=new Compile();
					Dex dex=new Dex();
					Classload cll=new Classload();
					String name=new File(fn).getName();
					if (rw.read(fn).compareTo(reset) != 0)
					{
						rw.write(fn, reset);
						a.flag = true;
					}
					if (a.flag == true)
					{
						new File(path + "/tmp/classes.dex.zip").delete();
						if (comp.compile() == true)
						{
							dex.dex();
						}
						if (new File(path + "/tmp/classes.dex.zip").exists())
						{
							a.flag = false;
						}
					}
					File f=new File(fn);
					if(fn.contains("/src")){
					if (!f.getParent().endsWith("/src"))
					{

						while (!f.getParent().endsWith("/src"))
						{
							p = f.getParent().substring(f.getParent().lastIndexOf("/") + 1) + "." + p;
							f = new File(f.getParent());
						}
						p = p.substring(0, p.length() - 1);
						name = p + "." + name;
					}
					}
					if (new File(path + "/tmp/classes.dex.zip").exists())
					{
						cll.classload(path + "/tmp/classes.dex.zip", name);
					}
				}
				else
				{
					rw.write(fn, reset);
				}
				rw.close();
				if (rw.read(err).length() > 0)
				{
					ed.setText(rw.read(err));
				}
				else
				{
					ed.setText(rw.read(out));	
				}
				syntax();
			}

			else
			{
				flag = false;
				ed.setText(reset);
				syntax();
			}
			flag = !flag;
		}
		catch (Exception e)
		{
			flag = false;
			ed.setText(e.toString());
			syntax();
		}
	}

	public void syntax()
	{
		Syntax syn=new Syntax(this);
		syn.highlight(ed.getText(), ed.getText().toString());
	}

	public void open()
	{
		if (fn.length() == 0)
		{
			fn = path + "/src/Main.java";
		}
		Address.address = fn;
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, FileChooser.class);
		startActivity(intent);
		finish();
	}

	public void onBackPressed()
	{
		run();
	}

	public void unzip(String from, String to)
	{
		a.setFrom(from);
		a.setTo(to);
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, UnzipActivity.class);
		startActivity(intent);
		finish();
	}

	public void exit()
	{
		finish();
		System.exit(0);
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(Menu.NONE, 1, Menu.NONE, R.string.open);
        menu.add(Menu.NONE, 2, Menu.NONE, R.string.run);
		menu.add(Menu.NONE, 3, Menu.NONE, R.string.exit);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId())
		{
			case 1:
				open();
				break;
			case 2:
				run();
				break;
			case 3:
				exit();
				break;
		}
        return super.onOptionsItemSelected(item);
    }
}

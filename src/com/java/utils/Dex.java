package com.java.utils;
import java.io.*;
import com.java.compiler.*;

public class Dex
{
	public Dex()
	{}
	Address a=new Address();
	String path=a.getPath();

	public boolean dex()
	{
		try
		{
			new File(path + "/tmp/classes.dex.dex").delete();
			String[] dex;
			File f1=new File(path + "/libs");
			if (f1.list().length > 0)
			{
				dex = new String[]{"--dex","--output=" + path + "/tmp/classes.dex", path + "/bin", path + "/libs"};
			}
			else
			{
				dex = new String[]{"--dex", "--output=" + path + "/tmp/classes.dex", path + "/bin"};
			}
			fnDx(dex);
			Pack pack=new Pack();
			pack.pack(path + "/tmp/classes.dex");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public void fnDx(String[] args)
	{
		try
		{
			com.android.dx.command.Main.main(args);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
}
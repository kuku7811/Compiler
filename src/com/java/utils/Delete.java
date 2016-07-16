package com.java.utils;
import java.io.File;
public class Delete
{
	public Delete()
	{}
	public static void delete(String fn)
	{
		File f=new File(fn);
		if (f.isDirectory())
		{
			del(fn);
		}
		else
		{
			f.delete();
		}
	}
	public static void del(String dir)
	{
		File f=new File(dir);
		File[] ff=f.listFiles();
		if (ff.length > 0)
		{
			for (File fn:ff)
			{
				if (fn.isDirectory())
				{
					del(fn.toString());
				}
				else
				{
					fn.delete();
				}
			}
		}
		f.delete();
	}
}
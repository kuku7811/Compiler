package com.java.utils;
import java.io.File;
import java.lang.reflect.*;
import java.util.*;
import com.java.compiler.*;

public class Classload
{
	public Classload()
	{}
	public void classload(String path, String fn)
	{
		try
		{
			MyDexClassLoader dex=new MyDexClassLoader(path);
			String className=normalize(stripClassName(fn, 5));
			String[] param={""};
			Class c=dex.loadClass(className);
			Method[] mm=c.getDeclaredMethods();
			boolean flag=false;
			for (Method m:mm)
			{
				if (m.getName().equals("main"))
				{
					flag = true;
					break;
				}
			}
			if (flag == true)
			{
				((Class)dex.loadClass(className)).getMethod("main", String[].class).invoke((Object)param.length, new Object[]{param});
			}
			else
			{
				dex.loadClass(className).newInstance();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    }
	private String stripClassName(String className, int x)
	{
        return className.substring(0, className.length() - x);
    }

    private String normalize(String className)
	{
        return className.replace('/', '.');
    }
}
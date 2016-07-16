package com.java.utils;
import java.io.*;

public class FileSearch
{
	public FileSearch()
	{}
	String s="";
	String path="";
	String endsWith="";

	public String search(String path, String end)
	{
		this.path = path;
		this.endsWith = end;
		dir(path);
		return s;
	}

	public void dir(String dir)
	{
		File[] files=new File(dir).listFiles();
		for (File f:files)
		{
			if (f.isFile())
			{
				if (f.toString().endsWith(endsWith))
				{
					this.s += dir + "/" + f.getName() + " ";
				}
			}
			else
			{
				dir(dir + "/" + f.getName());
			}
		}
	}
}
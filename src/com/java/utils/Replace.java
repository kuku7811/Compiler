package com.java.utils;
import java.io.*;

public class Replace
{

	static String path="";
	
	public Replace()
	{}
	
	public static void main(String[] args)
	{
		path = args[0];
		repl(path);
	}

	public static String pack(String fn)
	{
		String t="";
		String s=new File(fn).getParent();
		String n=new File(s).getName(); 
		while (!s.equals(path))
		{
			t = n + "." + t;
			s = new File(s).getParent();
			n = new File(s).getName();
		}
		String p="package " + t.substring(0, t.length() - 1) + ";";
		return p;
	}

	public static void repl(String path)
	{
		for (String f:new FileSearch().search(path, ".java").split(" "))
		{
			write(f, append(read(f), pack(f)));
		}
	}

	public static String[] read(String fn)
	{
		String s="";
		try
		{
			Reader fr=new BufferedReader(new FileReader(fn));
			int i=0;
			StringBuffer sb=new StringBuffer();
			char[] buf=new char[1024 * 50];
			while ((i = fr.read(buf)) != -1)
			{
				sb.append(buf, 0, i);
			}
			fr.close();
			for (String t:sb.toString().split("\n"))
			{
				if (t.startsWith("package"))
				{
					continue;
				}
				s += t + "\n";
			}

		}
		catch (Exception e)
		{}
		return s.split("\n");
	}

	public static void write(String fn, String[] ss)
	{
		try
		{
			FileWriter fw=new FileWriter(fn);
			int i=0;
			for (i = 0;i < (ss.length - 1);i++)
			{
				fw.append(ss[i] + "\n");
			}
            fw.append(ss[i]);
			fw.close();
		}
		catch (IOException e)
		{}
	}

	public static String[] append(String[] ss, String s)
	{
		String[] str=new String[ss.length + 1];
		int i=1;
		str[0] = s;
		for (String a:ss)
		{
			str[i] = a;
			i++;
		}
		return str;
	}

}
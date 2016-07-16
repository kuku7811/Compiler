package com.java.utils;
import java.io.*;
import com.java.compiler.*;
import org.eclipse.jdt.internal.compiler.batch.*;

public class Compile
{
    public Compile()
	{}

	Address a=new Address();
	RW rw=new RW();
	String tmp=a.getTmp();
	String src=a.getSrc();
	String sourcepath=src;
	String path=a.getPath();
	String bin=path + "/bin";
	String res=path + "/res";
	String[] s={};
	String[] args={};
	
	public boolean compile()
	{
		boolean ok=true;
		boolean b=true;
		new Delete().delete(bin);
		new File(bin).mkdirs();
		new File(res).mkdirs();
		rw.copy(res, bin);

		String[] boot=new FileSearch().search(path + "/boot", ".jar").split(" ");
		String[] cp=new FileSearch().search(path + "/libs", ".jar").split(" ");
		String bootclasspath="";
		for (String x:boot)
		{
			bootclasspath += x + ":";
		}
		if (cp[0].endsWith(".jar"))
		{
			String classpath="";
			for (String x:cp)
			{
				classpath += x + ":";
			}
			s = new String[]{"-nowarn","-source","1.5","-bootclasspath",bootclasspath,"-classpath",classpath,"-sourcepath",sourcepath,"-d",bin};
		}
		else 
		{
			s = new String[]{"-nowarn","-source","1.5","-bootclasspath",bootclasspath,"-sourcepath",sourcepath,"-d",bin};
		}
		Main m = new Main(new PrintWriter(System.out), new PrintWriter(System.err), false);
		if (!sourcepath.contains("/src"))
		{
			String[] ff=new String[]{a.getAddress()};
			args = new String[s.length + ff.length];
			int i=0;
			for (String a:s)
			{
				args[i] = a;
				i++;
			}
			for (String a:ff)
			{
				args[i] = a;
				i++;
			}
		}
		else
		{
			String[] ff=new FileSearch().search(src, ".java").split(" ");
			args = new String[s.length + ff.length];
			int i=0;
			for (String a:s)
			{
				args[i] = a;
				i++;
			}
			for (String a:ff)
			{
				args[i] = a;
				i++;
			}
		}
		ok = m.compile(args);

		if (ok == true)
		{
			pack(bin, tmp + "/test.jar");
			b = true;
			return true;
		}
		else
		{
			b = false;
		}
		return b;
	}

	public String[] search(String path, String end)
	{
		String[] s=new FileSearch().search(path, end).split(" ");
		int i=0;
		for (String x:s)
		{
			s[i] = x.substring(bin.length() + 1);
			i++;
		}
		return s;
	}
	
	public void pack(String bin, String jar)
	{
		RW.copy(res, bin);
		Pack pack=new Pack();
		String[] s=new FileSearch().search(bin, "").split(" ");
		String[] t=new String[s.length];
		int i=0;
		for (String x:s)
		{
			t[i] = x.substring(bin.length() + 1);
			i++;
		}
		pack.pack(bin, t, jar);
	}
	
}

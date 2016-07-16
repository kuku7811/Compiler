package com.java.utils;

public class Address
{
	public Address()
	{}
	public static String param="";
	public static String address="";
	public static String src="";
	public static String tmp="";
	public static String path="";
	public static String size="";
	public static String from="";
	public static String to="";
	public static boolean flag=true;

	public String getFrom()
	{
		return from;
	}
	public String getTo()
	{
		return to;
	}
	public void setFrom(String from)
	{
		this.from = from;
	}
	public void setTo(String to)
	{
		this.to = to;
	}
	public String getSize()
	{
		return size;
	}
	public String getPath()
	{
		return path;
	}
	public void setSize(String size)
	{
		this.size = size;
	}
	public void setPath(String path)
	{
		this.path = path;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getAddress()
	{
		return this.address;
	}
	public void setSrc(String src)
	{
		this.src = src;
	}
	public String getSrc()
	{
		return this.src;
	}
	public void setTmp(String tmp)
	{
		this.tmp = tmp;
	}
	public String getTmp()
	{
		return this.tmp;
	}
}
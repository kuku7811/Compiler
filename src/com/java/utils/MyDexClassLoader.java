package com.java.utils;
import dalvik.system.DexClassLoader;
import java.io.File;

public class MyDexClassLoader extends DexClassLoader
{
	public MyDexClassLoader(String path)
	{
		super(path, new File(path).getParent(), path, new MyClassLoader());
	}
	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException
	{
		return super.loadClass(className);
	}
	public String findLibrary(String name)
	{
		return 	super.findLibrary(name);
	}
}
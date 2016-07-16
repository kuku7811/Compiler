package com.java.utils;
import java.util.zip.*;
import java.io.*;
public class Pack
{
	public void pack(String file)
	{
		try
		{
			File f=new File(file);
			InputStream is=new FileInputStream(f);
			File zf=new File(file + ".zip");
			OutputStream os=new FileOutputStream(zf);
			ZipOutputStream zos=new ZipOutputStream(os);
			zos.putNextEntry(new ZipEntry(f.getName()));
			byte[] buffer=new byte[2048];
			int len;
			while ((len = is.read(buffer)) > 0)
			{
				zos.write(buffer, 0, len);
			}
			zos.closeEntry();
			is.close();
			zos.close();
			os.close();
			new File(file).delete();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void pack(String path, String[] filesToZip, String zipFileName)
	{
		try
		{
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileName));
			// метод сжатия
			zos.setLevel(Deflater.DEFAULT_COMPRESSION);
			for (int i = 0; i < filesToZip.length; i++)
			{
				zos.putNextEntry(new ZipEntry(filesToZip[i]));
				FileInputStream in = 
					new FileInputStream(path + "/" + filesToZip[i]);
				byte[] buffer=new byte[2048];
				int len;
				while ((len = in.read(buffer)) > 0)
				{
					zos.write(buffer, 0, len);
				}
				zos.closeEntry();
				in.close();
				//new File(filesToZip[i]).delete();
			}
			zos.close();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			System.err.println("Некорректный аргумент");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.err.println("Файл не найден");
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.err.println("Ошибка доступа");
		}
	}
}
package com.java.utils;
import java.io.*;

public class ProcessRun
{
	public void run(String s) {

		Process process = null;
		DataOutputStream os = null;
		DataInputStream is = null;
		try {

			String cmd = "sh " + "/mnt/sdcard/1.sh ";

			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(cmd + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
			is = new DataInputStream(process.getInputStream());
			byte[] buffer=new byte[1024];
			int i=0;
			while((i=is.read(buffer))!=-1){
				System.out.write(buffer,0,i);
			}
			is.close();
			os.close();
		} catch (Exception e) {
			System.err.println("*** DEBUG *** ROOT REE" + e.getMessage());

		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
			}
		}

	}
	
}
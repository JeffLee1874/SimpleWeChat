package socket;

import java.net.*;
import java.io.*;

public class ServerThread extends Thread{
	private Socket socket = null;
	
	public ServerThread(Socket socket)
	{
		this.socket = socket;
	}
	
	public void run()
	{
		InputStream is = null;
		OutputStream os = null;
		InputStreamReader is_string = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			is = socket.getInputStream();
			is_string = new InputStreamReader(is);
			br = new BufferedReader(is_string);
			
			String info = br.readLine();
			while(true)
			{
				if (info.equals("ע��"))
				{
					os = socket.getOutputStream();
					pw = new PrintWriter(os);
					pw.write("���������˻����û��������룺");
				}
				if (info.substring(0, 6).equals("talk_to"))
				{
					
				}
				if (info.equals("��¼"))
				{
					os = socket.getOutputStream();
					pw = new PrintWriter(os);
					pw.write("�������û��������룺");
				}
			}
		}catch(Exception e){
			
		}
	}
}

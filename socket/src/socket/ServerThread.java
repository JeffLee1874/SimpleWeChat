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
				if (info.equals("注册"))
				{
					os = socket.getOutputStream();
					pw = new PrintWriter(os);
					pw.write("请输入新账户的用户名和密码：");
				}
				if (info.substring(0, 6).equals("talk_to"))
				{
					
				}
				if (info.equals("登录"))
				{
					os = socket.getOutputStream();
					pw = new PrintWriter(os);
					pw.write("请输入用户名和密码：");
				}
			}
		}catch(Exception e){
			
		}
	}
}

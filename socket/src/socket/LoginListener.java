package socket;          

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class LoginListener implements ActionListener{
	private JTextField jt;//账号输入框对象
	private JPasswordField jp;//密码输入框对象
	private Frame login;//定义一个窗体对象
	
	public LoginListener(Frame login, JTextField jt, JPasswordField jp) {
		this.login = login;
		this.jp = jp;
		this.jt = jt;
	}
	
	public void actionPerformed(ActionEvent e){             //登录时和服务器通信
		try {
			Socket socket = new Socket("172.23.5.82",7070);
			BufferedReader bf = null;
			PrintWriter pw = null;
			bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream());
			String acc = jt.getText();
			String password = jp.getText();
			pw.println("login");
			pw.flush();
			String output_message = bf.readLine();
			pw.println(acc);
			pw.flush();
			output_message = bf.readLine();
			pw.println(password);
			pw.flush();
			output_message = bf.readLine();
			Object[] option = {"OK"};
			if (output_message.equals("Server: Login Successful！"))
			{
				int m = JOptionPane.showOptionDialog(null, "Login Successful", "status", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, option[0]);  
				System.out.println(output_message);
				//进入聊天界面
				login.dispose();
				Chat chatframe = new Chat(socket);
				chatframe.showUI();
			}
			if (output_message.equals("Server: Wrong key,please try again") || output_message.equals("Server: Non-existent username,please register"))
			{
				int m = JOptionPane.showOptionDialog(null, output_message, "status", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, option[0]);  
				socket.shutdownInput();
				socket.shutdownOutput();
				socket.close();
			}
		}catch(Exception a) {
			a.printStackTrace();
		}
	}
}

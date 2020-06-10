package socket;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Confirm implements ActionListener{
	private JTextField jt;//账号输入框对象
	private JPasswordField jp;//密码输入框对象
	private Frame re;//定义一个窗体对象
	
	public Confirm(Frame register, JTextField jt, JPasswordField jp) {
		this.re = register;
		this.jp = jp;
		this.jt = jt;
	}
	
	public void actionPerformed(ActionEvent e) {       //和服务器通信并尝试注册 
		try {
			Socket socket = new Socket("172.23.5.82",7070);
			BufferedReader bf = null;
			PrintWriter pw = null;
			bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream());
			String acc = jt.getText();
			String password = jp.getText();
			pw.println("register");
			pw.flush();
			String output_message = bf.readLine();
			pw.println(acc);
			pw.flush();
			output_message = bf.readLine();
			pw.println(password);
			pw.flush();
			output_message = bf.readLine();      //向服务器请求注册并上交账户密码，接着查看注册状态
			System.out.println(output_message);
			Object[] option = {"OK"};
			int m = JOptionPane.showOptionDialog(null, output_message, "status", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, option[0]);  
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();
			if (output_message.equals("Server: Registration Successful！"))    //注册成功就自动关闭注册窗口
				re.dispose();             
		}catch(Exception a) {
			a.printStackTrace();
		}
	}
}

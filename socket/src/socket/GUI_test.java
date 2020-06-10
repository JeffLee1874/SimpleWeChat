package socket;        //登录和注册界面

import java.awt.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class GUI_test {
	public static void main(String[] args) throws UnknownHostException, IOException
	{
		Frame f = new Frame("WeChat v1.0");
		f.setSize(400,500);
		f.setLocation(700,300);
		f.setLayout(new FlowLayout(FlowLayout.CENTER,10,1));
		Color bgColor = new Color(203855);
		f.setBackground(bgColor);
		
		Dimension dim_image = new Dimension(500, 200);
		Dimension dim_text = new Dimension(64, 50);
		Dimension dim_button = new Dimension(60,30);
		Dimension dim_field = new Dimension(250, 20);
		
		//添加图片
		ImageIcon imageIcon = new ImageIcon("C:\\Users\\Administrator\\Desktop\\2.jpg");
		JLabel imagelabel = new JLabel(imageIcon);
		imagelabel.setPreferredSize(dim_image);
		f.add(imagelabel);
		
		//添加账号标签
		JLabel account = new JLabel();
		account.setText("Account: ");
		account.setPreferredSize(dim_text);
		account.setForeground(Color.white);
		f.add(account);
		
		//添加账号输入框，并且监听事件
		JTextField textaccount = new JTextField();
		textaccount.setPreferredSize(dim_field);
		f.add(textaccount);
		
		//添加密码标签
		JLabel password = new JLabel();
		password.setText("Password: ");
		password.setPreferredSize(dim_text);
		password.setForeground(Color.white);
		f.add(password);
		
		//添加密码输入框，并且监听事件
		JPasswordField pw = new JPasswordField();
		pw.setPreferredSize(dim_field);
		f.add(pw);
		  
		//添加登录按钮，并且监听事件
		Button log_but = new Button("Login");
		log_but.setBackground(Color.white);
		log_but.setPreferredSize(dim_button);
		f.add(log_but);
		
		//添加注册按钮，并且监听事件
		Button re_but = new Button("Register");
		re_but.setBackground(Color.white);
		re_but.setPreferredSize(dim_button);
		f.add(re_but);
		
		f.setResizable(false);
		f.setVisible(true);
		
		LoginListener login = new LoginListener(f,textaccount ,pw);
		log_but.addActionListener(login);
		
		RegisterListener register = new RegisterListener();
		re_but.addActionListener(register);
		
		
		//添加窗口事件
		f.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				f.dispose();
			}
		});
	}
}

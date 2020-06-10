package socket;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegisterListener implements ActionListener{
	public RegisterListener()
	{
		
	}
	
	public void actionPerformed(ActionEvent e)
	{
		Frame f = new Frame("Register");
		f.setSize(400,500);
		f.setLocation(700,300);
		f.setLayout(new FlowLayout(FlowLayout.CENTER,10,1));
		Color bgColor = new Color(203855);
		f.setBackground(bgColor);
		
		Dimension dim_image = new Dimension(500, 200);
		Dimension dim_text = new Dimension(95, 50);
		Dimension dim_button = new Dimension(60,30);
		Dimension dim_field = new Dimension(250, 20);
		
		//添加图片
		ImageIcon imageIcon = new ImageIcon("C:\\Users\\Administrator\\Desktop\\2.jpg");
		JLabel imagelabel = new JLabel(imageIcon);
		imagelabel.setPreferredSize(dim_image);
		f.add(imagelabel);
		
		//添加账号标签
		JLabel account = new JLabel();
		account.setText("New Account: ");
		account.setPreferredSize(dim_text);
		account.setForeground(Color.white);
		f.add(account);
		
		//添加账号输入框，并且监听事件
		JTextField textaccount = new JTextField();
		textaccount.setPreferredSize(dim_field);
		f.add(textaccount);
		
		//添加密码标签
		JLabel password = new JLabel();
		password.setText("New Password: ");
		password.setPreferredSize(dim_text);
		password.setForeground(Color.white);
		f.add(password);
		
		//添加密码输入框，并且监听事件
		JPasswordField pw = new JPasswordField();
		pw.setPreferredSize(dim_field);
		f.add(pw);
		  
		//添加确认注册的按钮，并且监听事件
		Button log_but = new Button("Confirm");
		log_but.setBackground(Color.white);
		log_but.setPreferredSize(dim_button);
		f.add(log_but);
		
		//添加窗口事件
		f.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				f.dispose();
			}
		});
		
		Confirm confirm = new Confirm(f,textaccount ,pw);
		log_but.addActionListener(confirm);
		
		f.setVisible(true);
		f.setResizable(false);
	}
}

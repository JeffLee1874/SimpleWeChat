package socket;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class delete_f {
	public Socket socket;
	public delete_f(Socket a)
	{
		this.socket = a;
	}
	
	public void show() {
		Frame f = new Frame("WeChat v1.0");
		f.setSize(300,200);
		f.setLocation(900,400);
		f.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		f.setBackground(Color.white);
		
		Dimension dim_text = new Dimension(150, 100);
		Dimension dim_button = new Dimension(60,30);
		Dimension dim_field = new Dimension(100, 25);
		
		JLabel server_message = new JLabel();
		server_message.setText("Enter friend's name:  ");
		server_message.setPreferredSize(dim_text);
		server_message.setFont(new Font("微软雅黑", Font.BOLD, 12));
		server_message.setForeground(Color.black);
		f.add(server_message);
		
		JTextField name = new JTextField();
		name.setPreferredSize(dim_field);
		f.add(name);
		
		Button confirm = new Button("confirm");
		confirm.setPreferredSize(dim_button);
		confirm.setBackground(Color.white);
		f.add(confirm);
		
		f.setVisible(true);
		f.setResizable(false);
		
		f.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				f.dispose();
			}
		});
		
		confirm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				try {
					String f_name = name.getText();
					PrintWriter pw = new PrintWriter(socket.getOutputStream());
					BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					pw.println("delete friend");
					pw.flush();
					pw.println(f_name);
					pw.flush();
					Chat.windows.get(f_name).dispose();                           //关闭好友的聊天窗口
					Chat.windows.remove(f_name);
					f.dispose();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		f.setVisible(true);
	}
}

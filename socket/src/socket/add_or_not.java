package socket;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class add_or_not {
	public Socket socket;
	public String message;
	public add_or_not(Socket a, String b)
	{
		this.socket = a;
		message = b;
	}
	
	public void show() {
		Frame f = new Frame("WeChat v1.0");
		f.setSize(300,200);
		f.setLocation(900,400);
		f.setLayout(new FlowLayout(FlowLayout.CENTER,20,0));
		f.setBackground(Color.white);
		
		Dimension dim_text = new Dimension(250, 100);
		Dimension dim_button = new Dimension(60,30);
		
		JLabel server_message = new JLabel();
		server_message.setText(message);
		server_message.setPreferredSize(dim_text);
		server_message.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 12));
		server_message.setForeground(Color.black);
		f.add(server_message);

		Button agree = new Button("agree");
		agree.setPreferredSize(dim_button);
		agree.setBackground(Color.white);
		f.add(agree);
		
		Button refuse = new Button("refuse");
		refuse.setPreferredSize(dim_button);
		refuse.setBackground(Color.white);
		f.add(refuse);
		
		agree.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				try {
					PrintWriter pw = new PrintWriter(socket.getOutputStream());
					pw.println("agree");
					pw.flush();
					pw.println("agree");
					pw.flush();
					f.dispose();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		refuse.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				try {
					PrintWriter pw = new PrintWriter(socket.getOutputStream());
					pw.println("refuse");
					pw.flush();
					pw.println("refuse");
					pw.flush();
					f.dispose();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		f.setVisible(true);
		f.setResizable(false);
	}
}

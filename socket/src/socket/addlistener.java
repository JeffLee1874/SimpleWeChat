package socket;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import javax.swing.JOptionPane;

public class addlistener implements ActionListener{
	public Socket socket;
	
	public addlistener(Socket a) {
		this.socket = a;
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw.println("add friend");
			String server_message = bf.readLine();
			add_message message = new add_message(socket);
			message.show();
			server_message = bf.readLine();
			Object[] option = {"OK"};
			int m = JOptionPane.showOptionDialog(null, server_message, "status", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, option[0]);			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}

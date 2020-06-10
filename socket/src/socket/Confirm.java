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
	private JTextField jt;//�˺���������
	private JPasswordField jp;//������������
	private Frame re;//����һ���������
	
	public Confirm(Frame register, JTextField jt, JPasswordField jp) {
		this.re = register;
		this.jp = jp;
		this.jt = jt;
	}
	
	public void actionPerformed(ActionEvent e) {       //�ͷ�����ͨ�Ų�����ע�� 
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
			output_message = bf.readLine();      //�����������ע�Ტ�Ͻ��˻����룬���Ų鿴ע��״̬
			System.out.println(output_message);
			Object[] option = {"OK"};
			int m = JOptionPane.showOptionDialog(null, output_message, "status", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, option[0]);  
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();
			if (output_message.equals("Server: Registration Successful��"))    //ע��ɹ����Զ��ر�ע�ᴰ��
				re.dispose();             
		}catch(Exception a) {
			a.printStackTrace();
		}
	}
}

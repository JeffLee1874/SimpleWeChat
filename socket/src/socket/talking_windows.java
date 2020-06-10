package socket;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class talking_windows {
	public String with_who;
	public Socket socket;
	
	public static String gettime() {                       //设置返回时间的格式为string
		Calendar cal=Calendar.getInstance();      
		int y=cal.get(Calendar.YEAR);      
		int m=cal.get(Calendar.MONTH);      
		int d=cal.get(Calendar.DATE);      
		int h=cal.get(Calendar.HOUR_OF_DAY);      
		int mi=cal.get(Calendar.MINUTE);       
		String time=y+"."+m+"."+d+": "+h+": "+mi;
		return time;
	}
	
	public talking_windows(String name, Socket a)
	{
		this.with_who = name;
		this.socket = a;
		Frame f = new Frame("Chat with " + name);
		f.setSize(600,700);
		f.setLocation(950,300);
		f.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));
		f.setBackground(Color.white);
		
		Dimension dim_receive = new Dimension(570, 300);
		Dimension dim_send = new Dimension(570, 100);
		Dimension dim_button = new Dimension(90,30);
		Dimension dim_image = new Dimension(600, 200);
		
		//添加图片
		ImageIcon imageIcon = new ImageIcon("C:\\Users\\Administrator\\Desktop\\1.jpg");
		JLabel imagelabel = new JLabel(imageIcon);
		imagelabel.setPreferredSize(dim_image);
		f.add(imagelabel);
		
		TextArea receive = new TextArea();
		receive.setFont(new Font("微软雅黑", Font.BOLD, 13));
		receive.setPreferredSize(dim_receive);
		
		TextArea send = new TextArea();
		send.setFont(new Font("微软雅黑", Font.BOLD, 13));
		send.setPreferredSize(dim_send);
		f.add(receive);
		f.add(send);

		Button file = new Button("send file");
		file.setPreferredSize(dim_button);
		file.setBackground(Color.white);
		f.add(file);
		
		Button message = new Button("send message");
		message.setPreferredSize(dim_button);
		message.setBackground(Color.white);
		f.add(message);
		
		f.setVisible(false);
		f.setResizable(false);
		
		//添加发送消息事件
		message.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				try {
					PrintWriter pw = new PrintWriter(socket.getOutputStream());
					pw.println("send message");
					pw.flush();
					pw.println(with_who);
					pw.flush();
					pw.println("message");
					pw.flush();
					pw.println(send.getText());
					pw.flush();
					receive.append("[" + gettime() + "] "+ " I: " + send.getText() + '\n');
					send.setText("");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		//添加发送文件信息
		file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				try {
					String path = null;
					JFileChooser fc = new JFileChooser();
					fc.setDialogTitle("Choose file");
					fc.setApproveButtonText("确定");
					fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(null)) {
						path = fc.getSelectedFile().getPath();              //文件路径
						File file = new File(path);
						PrintWriter pw = new PrintWriter(socket.getOutputStream());
						pw.println("send message");
						pw.flush();
						pw.println(with_who);
						pw.flush();
						pw.println("file");
						pw.flush();
						FileInputStream fis = new FileInputStream(file);
		            	DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		            	dos.writeUTF(file.getName());
		            	dos.flush();
		            	dos.writeLong(file.length());
		            	dos.flush();
		            	byte[] bytes = new byte[1024];
		            	int length = 0;
		            	while((length = fis.read(bytes, 0, bytes.length)) != -1)
		            	{
		            		dos.write(bytes, 0, length);
		            		dos.flush();
		            	}
		            	fis.close();
		            	Object[] option = {"OK"};
					    int m = JOptionPane.showOptionDialog(null, "send successfully", "status", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, option[0]);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		f.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				f.setVisible(false);
			}
		});
		
		Chat.windows.put(name, f);
		Chat.textarea.put(name, receive);
	}
}

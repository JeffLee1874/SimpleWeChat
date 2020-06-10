package socket;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

//用户面板和聊天界面
//JScrollPane —— 好友列表（带滚轮）

public class Chat {
	public static HashMap<String, Frame> windows = new HashMap<String, Frame>();
	public static HashMap<String, TextArea> textarea = new HashMap<String, TextArea>();
	public static HashMap<String, JLabel> unread_message = new HashMap<String, JLabel>();
	public static Dimension dim_panel = new Dimension(235, 10000);
	public String Friend_name;
	public Socket socket;
	public static JPanel friendlist = new JPanel();
	public Dimension dim_button = new Dimension(200,40);
	public Dimension dim_label = new Dimension(17,40);
	public Chat(Socket a) {
		this.socket = a;
	}
	public void showUI() throws UnknownHostException, IOException
	{
		//好友列表总框
        Frame f = new Frame("WeChat v1.0");
		f.setSize(250,700);
		f.setLocation(710,300);
		f.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		f.setBackground(Color.white);
		
		Dimension dim_scroll = new Dimension(235,700);
		Dimension dim_text = new Dimension(235, 50);
		Dimension dim_add = new Dimension(50,30);
		
		JLabel title = new JLabel();
		title.setText("Friend List:   ");
		title.setPreferredSize(dim_text);
		title.setFont(new Font("微软雅黑", Font.BOLD, 16));
		title.setForeground(Color.black);
		f.add(title);
		
		//添加好友的按钮
		Button add = new Button("add");
		add.setPreferredSize(dim_add);
		add.setBackground(Color.white);
		f.add(add);   
		
		//删除好友的按钮
		Button delete = new Button("delete");
		delete.setPreferredSize(dim_add);
		delete.setBackground(Color.white);
		f.add(delete);
		
		//注销的按钮
		Button logout = new Button("LogOut");
		logout.setPreferredSize(dim_add);
		logout.setBackground(Color.white);
		f.add(logout);
		
		//刷新好友列表的按钮
		Button Refresh = new Button("Refresh");
		Refresh.setPreferredSize(dim_add);
		Refresh.setBackground(Color.white);
		f.add(Refresh);
		
		friendlist.setPreferredSize(dim_panel);
		friendlist.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		
		//设置滚轮组件
		JScrollPane list = new JScrollPane(friendlist);
		list.setPreferredSize(dim_scroll);
		
		f.add(list);
		friendlist.setVisible(true);
		list.setVisible(true);
		f.setVisible(true);
		f.setResizable(false);
		
		//开启新的进程来监听服务器发送的消息
		Thread ct=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        receive();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        ct.start();
		
		
		//添加加好友按钮事件
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
			    add_message message = new add_message(socket);
				message.show();
			}
		});
		
		//添加删除好友按钮事件
		delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
			    delete_f message = new delete_f(socket);
				message.show();
			}
		});
		
		//添加注销事件
		logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				PrintWriter pw;
				try {
					pw = new PrintWriter(socket.getOutputStream());
					pw.println("logout");
					pw.flush();
					ct.stop();
					socket.shutdownInput();
					socket.shutdownOutput();
					socket.close();
					f.dispose();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		//添加刷新好友列表事件
		Refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				try {
					PrintWriter pw = new PrintWriter(socket.getOutputStream());
					pw.println("friendslist");
					pw.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				};
			}
		});
		
		f.addWindowListener(new WindowAdapter(){                                  //关闭程序
			public void windowClosing(WindowEvent e)
			{
				try {
					for(Entry<String, Frame> c : windows.entrySet())
					{
						c.getValue().dispose();
					}
					PrintWriter pw;
					pw = new PrintWriter(socket.getOutputStream());
					pw.println("logout");
					pw.flush();
					ct.stop();
					socket.shutdownInput();
					socket.shutdownOutput();
					socket.close();
					f.dispose();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				
			}
		});
		
	}
	
	public void receive() throws IOException {	
    	BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String temp = bf.readLine();
        System.out.println(temp);
    	if (temp.equals("Server: a file will be sent"))
    	{
    		DataInputStream dis = new DataInputStream(socket.getInputStream());
    		String fileName = dis.readUTF();
    		long fileLength = dis.readLong();
    		System.out.println(fileName + "   " + fileLength);
    		String path = "D:/";
    		File directory = new File(path);
    		File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
    		FileOutputStream fos = new FileOutputStream(file);
    		byte[] bytes = new byte[1024];
    		int read =0;
    		long passread = 0;
            while(true)
            {
            	if(passread == fileLength)
            		break;
            	if(dis != null) {
            		read = dis.read(bytes, 0, bytes.length);
            		fos.write(bytes, 0, read);
            		fos.flush();
            	}
        		passread += read;
            	if(read == -1)
            		break;
            	System.out.println(passread + "      " + fileLength);
            }
            fos.close();
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.println("ack");
            pw.flush();
            pw.println("ack");
            pw.flush();
            String re_message = bf.readLine();
            String username = re_message.substring(0, re_message.length()-53);
            textarea.get(username).append("["+ talking_windows.gettime()+ "] " + re_message + '\n');
            String countplus = unread_message.get(username).getText().substring(1);                   //增加未读消息
    		int count = Integer.valueOf(countplus) + 1;
    		unread_message.get(username).setText(" " + String.valueOf(count));
        }
    	else if(temp.contains("Server:"))
    	{
    		if(temp.contains("wants to add you as friend"))                              //响应好友请求
    		{
    			add_or_not add = new add_or_not(socket, temp.substring(8));
    			add.show();
    		}
    		else if(temp.contains("start to send friendlist"))                           //接受好友列表
    		{
    			friendlist.removeAll();
    			int numbers_of_friends = Integer.valueOf(temp.substring(32));
    			System.out.println(numbers_of_friends);
    			if(numbers_of_friends != 0)
    			{
    				for(int i = 0; i < numbers_of_friends; i++)
    			    {
    					Friend_name = bf.readLine();
    				    if(!windows.containsKey(Friend_name))
    				    {
    		    			talking_windows chat_frame = new talking_windows(Friend_name, socket);
    				    }
    				    Button friend = new Button(Friend_name);
        				friend.setPreferredSize(dim_button);
        			    friend.setFont(new Font("微软雅黑", Font.BOLD, 16));
        				friend.setBackground(Color.white);
        				
        				friend.addActionListener(new ActionListener() {
        					public void actionPerformed(ActionEvent e)
        					{
        						if(windows.containsKey(friend.getLabel()))
        							windows.get(friend.getLabel()).setVisible(true);
        						unread_message.get(friend.getLabel()).setText(" 0");
        					}
        				});
    				    friendlist.add(friend);
    					//添加未读消息标签
    					JLabel unread = new JLabel();
    					unread.setPreferredSize(dim_label);
    					unread.setOpaque(true);
    					unread.setBackground(Color.white);
    					unread.setText(" 0");
    					unread.setFont(new Font("微软雅黑", Font.BOLD, 16));
    					unread.setForeground(Color.red);
    				    friendlist.add(unread);
    				    unread_message.put(Friend_name, unread);
    			    }
    			}
    		}
    		else if(!temp.contains("Enter your friend's username:") && !temp.contains("Which friend do you want to delete") && !temp.contains("friend's name"))
    		{
    			Object[] option = {"OK"};
			    int m = JOptionPane.showOptionDialog(null, temp.substring(8), "status", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, option[0]);
    		}
    	}
    	else
    	{
    		String time = temp;
    		String user = bf.readLine();
    		String mess = bf.readLine();
    		textarea.get(user).append(time + user + mess + '\n');
    		String countplus = unread_message.get(user).getText().substring(1);                   //增加未读消息
    		int count = Integer.valueOf(countplus) + 1;
    		unread_message.get(user).setText(" " + String.valueOf(count));
    	}
    }
}
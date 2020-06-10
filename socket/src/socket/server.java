package socket;

import java.net.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;



class user {
	public String username=null;
	public String userkey=null;
	public static HashMap<String, user> friend;// 储存用户好友列表的hashamap
	public boolean isonline = false;// 默认离线状态
	public Socket socket=null;

	public user() {
		// TODO Auto-generated method stub
	}

	public user(String name, String key, Socket socket) {
		username = name;
		userkey = key;
		this.socket = socket;
		System.out.println(socket.getLocalAddress() + " " + username + " Registration Successful");
	}
}

public class server {
	public static user clientuser[] = new user[1000];
	public static int i = 0;
	public static Map<String, user> map = new HashMap<String, user>();// 储存已注册用户的hashmap

	public static void main(String[] args) throws IOException {
		System.out.println("Server is already");
		ServerSocket srvSocket = new ServerSocket(9000);// 创建服务器套接字
		while (true) // 服务器一旦开启就一直运行，不断地监听和接收新的socket连接
		{
			Socket socket = srvSocket.accept(); // 监听收到的请求并创建连接套接字
			System.out.println("收到新的用户" + socket.getLocalAddress() + "连接请求");
			ServerThread client = new ServerThread(socket);
			Thread clientthread = new Thread(client);// 每次收到socket都new线程实现多线程即多客户端
			clientthread.start();// 为连接到服务器的请求建立一个持续的连接
		}
	}

	public static class ServerThread implements Runnable {
		public Socket socket;

		public ServerThread(Socket s) {
			this.socket = s;
		}


		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println(socket.getLocalAddress() + " 线程开始");
			BufferedReader bf = null;
			PrintWriter pw = null;
			try {
				bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				pw = new PrintWriter(socket.getOutputStream(), true);
				// bf为客户端传来的输入流，pw为服务器输出流
				String name = null;
				String key  = null;
				String request = bf.readLine(); // 接受从客户端传来的输入流即用户指令
				System.out.println("当前接受到 " + socket.getLocalAddress() + " 的命令是: " + request);
				if (request.startsWith("register")) { // 用户注册账户
					pw.println("Server: Username: ");
					name = bf.readLine();
					pw.println("Server: Key: ");
					key = bf.readLine();
					if(map.containsKey(name))
						pw.println("Server: Existent username,please use another name");
					else {
						clientuser[i] = new user(name, key, socket);
						map.put(name, clientuser[i]);
						clientuser[i].friend=new HashMap();
						// 将账号存入HashMap中，以username作为key便于以后的查找
						pw.println("Server: Registration Successful！");
						i++;
					}
					return;
				}//注册成功后直接断开连接以防占用
				else if (request.startsWith("file"))
				{
					System.out.println("Server: a file will be sent");
	                DataInputStream in =new DataInputStream(socket.getInputStream());
	                String fileName = in.readUTF();
	        		long fileLength = in.readLong();
	        		File directory = new File("E:\\");
	        		File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
					FileOutputStream fos = new FileOutputStream(file);
		    		System.out.println("======== 开始接收文件 ========");
		    		byte[] bytes = new byte[1024];
		    		int read =0;
		    		long passread = 0;
		            while(true)
		            {
		            	if(in != null) {
		            		read = in.read(bytes);
		            	}
	            		passread += read;
		            	if(read == -1 || passread == fileLength)
		            		break;
		            }
	                System.out.println("transmisson finish");
					
				}
				
				else if (request.startsWith("login")) { // 用户登录
					pw.println("Server: Username: ");
					name = bf.readLine();
					System.out.println(name);
					pw.println("Server: Key: ");
					key = bf.readLine();
					System.out.println(key);
					if (map.containsKey(name)) {
						user a = map.get(name);// 获取username对应的信息
						if (key.equals(a.userkey)) {
							a.isonline = true;
							a.socket=socket;
							map.put(name, a);//更新socket和在线状态
							pw.println("Server: Login Successful！");
						} // 登录成功，进入下一步操作
						else {
							pw.println("Server: Wrong key,please try again");
							return;
						} // 密码错误，要求用户重新登陆
					} else {
						pw.println("Server: Non-existent username,please register");
						return;
					} // 不存在的用户名，要求用户注册
				}//除非用户账户密码匹配成功登录，别的情况一律断开与用户连接以防占用

				// 用户成功登陆，可进行下一步操作
				user a =map.get(name);// 获取用户信息
				String command =null;
				while (true) {
					command = bf.readLine();
					System.out.println("当前接受到用户 " + name + " 的指令是: " + command);
					if (command.equals("logout")) {
						map.get(name).isonline = false;
						break;
					} // 用户注销

					else if (command.equals("friendslist")) { // 查看好友列表
						for (Map.Entry m : a.friend.entrySet()) {
							String friend = (String) m.getKey();
							pw.println(friend);// 遍历好友列表将好友用户名输出给客户端
						}
					}

					else if (command.equals("add friend")) { // 添加好友
						pw.println("Server: Enter your friend's username: ");
						String friendname = bf.readLine();
						System.out.println(friendname);
						if (map.containsKey(friendname)) {
							user b = map.get(friendname);
							if (map.get(friendname).isonline) {
								BufferedReader bf2 = new BufferedReader(new InputStreamReader(b.socket.getInputStream()));
								PrintWriter pw2 = new PrintWriter(new OutputStreamWriter(b.socket.getOutputStream()), true);
								// 给被添加用户创建输入输出流
								pw2.println("Server: User " + name + " wants to add you as friend");
								String respond = bf2.readLine();
								if (respond.equals("agree")) {
									pw.println("Server: "+friendname + " now is your friend!");
									pw2.println("Server: "+name+" now is your friend!");
                                    a.friend.put(friendname, b);
									b.friend.put(name, a);// 互相将好友加至好友列表
								} else
									pw.println("Server: Your requstion was refused!");// 请求被拒绝
							} else// 好友不在线无法收到请求
								pw.println("Server: Your friend is not online,try next time");
						}
						else
							pw.println("Server: Non-existent username");
					}

					else if (command.equals("delete friend")) {// 删除好友
						pw.println("Server: Which friend do you want to delete");
						String df = bf.readLine();
						if(a.friend.containsKey(df))
							a.friend.remove(df); // 将好友移出储存好友列表的hashmap
						else
							pw.println("Server: "+df+" now is not your friend");
					}

					else if (command.equals("send message")) {// 发送消息
						pw.println("Server: friend's name");
						String target = bf.readLine();
						if (!a.friend.containsKey(target))
							pw.println("Server: "+target + "is not your friend");// 不在好友列表中
						else {
							user tar = map.get(target);
							if (!tar.isonline)// 好友不在线无法接收消息
								pw.println("Server: Your friend is not online,try next time");
							else {
								PrintWriter pw2 = new PrintWriter(tar.socket.getOutputStream(), true);
								// 给接受用户创建输出流
								// opem datainoutstream
								String mask = bf.readLine();// mask是用户发送信息的标识码
								if (mask.equals("message")) {//用户发的是对话
									String message=bf.readLine();
									PrintWriter pw3 = new PrintWriter(new OutputStreamWriter(tar.socket.getOutputStream()), true);
									// 给接受用户创建输入输出流
									pw3.println("["+gettime()+"] "+name+": "+message);}
								else if(mask.equals("file")) {//用户发的是文件
									PrintWriter pw4 =new PrintWriter(tar.socket.getOutputStream(),true);
									pw4.println("Server: a file will be sent");
					                DataInputStream in =new DataInputStream(a.socket.getInputStream());
									DataOutputStream out =new DataOutputStream(tar.socket.getOutputStream());
									String fileName = in.readUTF();//传输文件名
									out.writeUTF(fileName);
									out.flush();
									Long length=in.readLong();//传输文件大小
									out.writeLong(length);
									out.flush();
									System.out.println(fileName+length);
									Long j=(long) 0;
					                // 文件字节传输
					                byte[] b= new byte[1024*8];
					                int i=0;
					                while((i = in.read(b, 0, b.length)) != -1) {
					                    System.out.println("以传输大小："+j+"本次已执行read");
					                    System.out.println("本次循环传输数据长度："+i+" 以传输大小："+j+" 文件大小： "+length);
					                	out.write(b, 0, i);
					                	System.out.println("以传输大小："+j+"本次已执行write");
					                    out.flush();
					                    j+=i;
					                }//下面一句改为out.println不用发时间
					                out.writeUTF("["+gettime()+"] "+name+" sends a file "+fileName+" to you,file has stored in D:\\FakeWeChat");
					                System.out.println("transmisson finish");
									
								
								}
								else if(mask.equals("picture")) {//用户发的是图片
									PrintWriter pw4 =new PrintWriter(tar.socket.getOutputStream(),true);
									pw4.println("Server: a picture will be sent");
									pw4.println("["+gettime()+"] "+name+" sends a image to you,image has stored in D:\\FakeWeChat");
								}
							}
						}
					}
					
					else
						pw.println("Server: Unrecognized command");
				}
			} catch (

			Exception e) {
				System.out.println(e.toString());
			} finally { // 关闭连接
				System.out.println("客户端" + socket.getLocalPort() + "断开连接");
				try {
					if (bf != null) {
						bf.close();
					}
					if (pw != null) {
						pw.close();
					}
					if (socket != null) {
						socket.close();
						
					} // 关闭输入输出流和连接套接字
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
	}
	
	public static String gettime() {
		Calendar cal=Calendar.getInstance();      
		int y=cal.get(Calendar.YEAR);      
		int m=cal.get(Calendar.MONTH);      
		int d=cal.get(Calendar.DATE);      
		int h=cal.get(Calendar.HOUR_OF_DAY);      
		int mi=cal.get(Calendar.MINUTE);       
		String time=y+"."+m+"."+d+": "+h+": "+mi;
		return time;
	}
}
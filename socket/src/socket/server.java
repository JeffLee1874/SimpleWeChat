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
	public static HashMap<String, user> friend;// �����û������б��hashamap
	public boolean isonline = false;// Ĭ������״̬
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
	public static Map<String, user> map = new HashMap<String, user>();// ������ע���û���hashmap

	public static void main(String[] args) throws IOException {
		System.out.println("Server is already");
		ServerSocket srvSocket = new ServerSocket(9000);// �����������׽���
		while (true) // ������һ��������һֱ���У����ϵؼ����ͽ����µ�socket����
		{
			Socket socket = srvSocket.accept(); // �����յ������󲢴��������׽���
			System.out.println("�յ��µ��û�" + socket.getLocalAddress() + "��������");
			ServerThread client = new ServerThread(socket);
			Thread clientthread = new Thread(client);// ÿ���յ�socket��new�߳�ʵ�ֶ��̼߳���ͻ���
			clientthread.start();// Ϊ���ӵ���������������һ������������
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
			System.out.println(socket.getLocalAddress() + " �߳̿�ʼ");
			BufferedReader bf = null;
			PrintWriter pw = null;
			try {
				bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				pw = new PrintWriter(socket.getOutputStream(), true);
				// bfΪ�ͻ��˴�������������pwΪ�����������
				String name = null;
				String key  = null;
				String request = bf.readLine(); // ���ܴӿͻ��˴��������������û�ָ��
				System.out.println("��ǰ���ܵ� " + socket.getLocalAddress() + " ��������: " + request);
				if (request.startsWith("register")) { // �û�ע���˻�
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
						// ���˺Ŵ���HashMap�У���username��Ϊkey�����Ժ�Ĳ���
						pw.println("Server: Registration Successful��");
						i++;
					}
					return;
				}//ע��ɹ���ֱ�ӶϿ������Է�ռ��
				else if (request.startsWith("file"))
				{
					System.out.println("Server: a file will be sent");
	                DataInputStream in =new DataInputStream(socket.getInputStream());
	                String fileName = in.readUTF();
	        		long fileLength = in.readLong();
	        		File directory = new File("E:\\");
	        		File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
					FileOutputStream fos = new FileOutputStream(file);
		    		System.out.println("======== ��ʼ�����ļ� ========");
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
				
				else if (request.startsWith("login")) { // �û���¼
					pw.println("Server: Username: ");
					name = bf.readLine();
					System.out.println(name);
					pw.println("Server: Key: ");
					key = bf.readLine();
					System.out.println(key);
					if (map.containsKey(name)) {
						user a = map.get(name);// ��ȡusername��Ӧ����Ϣ
						if (key.equals(a.userkey)) {
							a.isonline = true;
							a.socket=socket;
							map.put(name, a);//����socket������״̬
							pw.println("Server: Login Successful��");
						} // ��¼�ɹ���������һ������
						else {
							pw.println("Server: Wrong key,please try again");
							return;
						} // �������Ҫ���û����µ�½
					} else {
						pw.println("Server: Non-existent username,please register");
						return;
					} // �����ڵ��û�����Ҫ���û�ע��
				}//�����û��˻�����ƥ��ɹ���¼��������һ�ɶϿ����û������Է�ռ��

				// �û��ɹ���½���ɽ�����һ������
				user a =map.get(name);// ��ȡ�û���Ϣ
				String command =null;
				while (true) {
					command = bf.readLine();
					System.out.println("��ǰ���ܵ��û� " + name + " ��ָ����: " + command);
					if (command.equals("logout")) {
						map.get(name).isonline = false;
						break;
					} // �û�ע��

					else if (command.equals("friendslist")) { // �鿴�����б�
						for (Map.Entry m : a.friend.entrySet()) {
							String friend = (String) m.getKey();
							pw.println(friend);// ���������б������û���������ͻ���
						}
					}

					else if (command.equals("add friend")) { // ��Ӻ���
						pw.println("Server: Enter your friend's username: ");
						String friendname = bf.readLine();
						System.out.println(friendname);
						if (map.containsKey(friendname)) {
							user b = map.get(friendname);
							if (map.get(friendname).isonline) {
								BufferedReader bf2 = new BufferedReader(new InputStreamReader(b.socket.getInputStream()));
								PrintWriter pw2 = new PrintWriter(new OutputStreamWriter(b.socket.getOutputStream()), true);
								// ��������û��������������
								pw2.println("Server: User " + name + " wants to add you as friend");
								String respond = bf2.readLine();
								if (respond.equals("agree")) {
									pw.println("Server: "+friendname + " now is your friend!");
									pw2.println("Server: "+name+" now is your friend!");
                                    a.friend.put(friendname, b);
									b.friend.put(name, a);// ���ཫ���Ѽ��������б�
								} else
									pw.println("Server: Your requstion was refused!");// ���󱻾ܾ�
							} else// ���Ѳ������޷��յ�����
								pw.println("Server: Your friend is not online,try next time");
						}
						else
							pw.println("Server: Non-existent username");
					}

					else if (command.equals("delete friend")) {// ɾ������
						pw.println("Server: Which friend do you want to delete");
						String df = bf.readLine();
						if(a.friend.containsKey(df))
							a.friend.remove(df); // �������Ƴ���������б��hashmap
						else
							pw.println("Server: "+df+" now is not your friend");
					}

					else if (command.equals("send message")) {// ������Ϣ
						pw.println("Server: friend's name");
						String target = bf.readLine();
						if (!a.friend.containsKey(target))
							pw.println("Server: "+target + "is not your friend");// ���ں����б���
						else {
							user tar = map.get(target);
							if (!tar.isonline)// ���Ѳ������޷�������Ϣ
								pw.println("Server: Your friend is not online,try next time");
							else {
								PrintWriter pw2 = new PrintWriter(tar.socket.getOutputStream(), true);
								// �������û����������
								// opem datainoutstream
								String mask = bf.readLine();// mask���û�������Ϣ�ı�ʶ��
								if (mask.equals("message")) {//�û������ǶԻ�
									String message=bf.readLine();
									PrintWriter pw3 = new PrintWriter(new OutputStreamWriter(tar.socket.getOutputStream()), true);
									// �������û��������������
									pw3.println("["+gettime()+"] "+name+": "+message);}
								else if(mask.equals("file")) {//�û��������ļ�
									PrintWriter pw4 =new PrintWriter(tar.socket.getOutputStream(),true);
									pw4.println("Server: a file will be sent");
					                DataInputStream in =new DataInputStream(a.socket.getInputStream());
									DataOutputStream out =new DataOutputStream(tar.socket.getOutputStream());
									String fileName = in.readUTF();//�����ļ���
									out.writeUTF(fileName);
									out.flush();
									Long length=in.readLong();//�����ļ���С
									out.writeLong(length);
									out.flush();
									System.out.println(fileName+length);
									Long j=(long) 0;
					                // �ļ��ֽڴ���
					                byte[] b= new byte[1024*8];
					                int i=0;
					                while((i = in.read(b, 0, b.length)) != -1) {
					                    System.out.println("�Դ����С��"+j+"������ִ��read");
					                    System.out.println("����ѭ���������ݳ��ȣ�"+i+" �Դ����С��"+j+" �ļ���С�� "+length);
					                	out.write(b, 0, i);
					                	System.out.println("�Դ����С��"+j+"������ִ��write");
					                    out.flush();
					                    j+=i;
					                }//����һ���Ϊout.println���÷�ʱ��
					                out.writeUTF("["+gettime()+"] "+name+" sends a file "+fileName+" to you,file has stored in D:\\FakeWeChat");
					                System.out.println("transmisson finish");
									
								
								}
								else if(mask.equals("picture")) {//�û�������ͼƬ
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
			} finally { // �ر�����
				System.out.println("�ͻ���" + socket.getLocalPort() + "�Ͽ�����");
				try {
					if (bf != null) {
						bf.close();
					}
					if (pw != null) {
						pw.close();
					}
					if (socket != null) {
						socket.close();
						
					} // �ر�����������������׽���
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
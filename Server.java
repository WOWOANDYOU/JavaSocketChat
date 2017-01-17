import java.net.*;
import java.io.*;
import java.util.*;

public class Server
{
	boolean isstart = false;//�����ж� ������Ƿ��Ѿ�������
	//public DataInputStream dis = null;
	//public String str = null;
	public ServerSocket ss = null;
	//public Socket s = null;
	List<Client> clients = new ArrayList<>();
	//DataOutputStream dos = null;
	
	public static void main(String[] args)  //Ҫ��ε���readUTF()����   Client�� ���Ͷ��ٴθ������ ����˾�Ҫ���ܶ��ٴ� �õ���ѭ��
	{
		new Server().start();
	}
	
	public void start()
	{
		try
		{
			 ss = new ServerSocket(8888);//�����������
			 isstart = true;//����������� ��������Ϊ true
		}
		catch(BindException e)//����쳣һ��Ҫ �ȱ� IOException �� ���� ��Ȼ ����Ȳ��� IOException ��� �쳣�Ͳ������� Ȼ��ͻᱨ�� ��ʾҪ�Ȳ��� ����쳣
		{
			System.out.println("�˿�ʹ���С���");
			System.out.println("��ص������ ��Ȼ��������������ˡ�");
			System.exit(0);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		try 
		{
			while(isstart)//������������һֱ ѭ�� ����Ƿ��пͻ�������
			{
				Socket s = ss.accept();//�ͻ������ӵ�����˺� ������Ϊtrue
	System.out.println("a client connect");
				Client c = new Client(s);
				clients.add(c);
				new Thread(c).start();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	class Client implements Runnable
	{
		private Socket s = null;
		private DataInputStream dis = null;
		DataOutputStream dos = null;
		private boolean isconnect = false;
		
		Client(Socket s)
		{
			this.s = s;
			try
			{
				dis = new DataInputStream(s.getInputStream());//�ͻ������ӵ�����˺� ����isconnect =  true
				dos = new DataOutputStream(s.getOutputStream());
				isconnect = true;
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		public void send(String str)
		{
			try
			{
				dos.writeUTF(str);
				dos.flush();
			} 
			catch (IOException e)
			{
				clients.remove(this);
				System.out.println("a client quit");
			}
		}
		@Override
		public void run()
		{
			try
			{
				while(isconnect)//�ͻ������ӷ���˺� ѭ�� �Ա� ���� �ͻ��� ÿһ�η��͵���Ϣ
				{
					//dis = new DataInputStream(s.getInputStream());
					String str = dis.readUTF();
					System.out.println(str);
					for(int i=0;i<clients.size();i++)
					{
						Client c = clients.get(i);
						c.send(str);
					}
					//dis.close();
					//ss.close();
				}
				//dis.close();//�������û������ ���� �ͻ���û������ ������� �ͽ��ܵ��ر�*/
			}
			catch(EOFException e)
			{
				System.out.println("Client disconnect!");
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if(dis!=null) dis.close();
					if(s!=null) s.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				System.out.println("Client disconnect!");
			}
		}
	}
}


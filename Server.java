import java.net.*;
import java.io.*;
import java.util.*;

public class Server
{
	boolean isstart = false;//用来判断 服务端是否已经起来了
	//public DataInputStream dis = null;
	//public String str = null;
	public ServerSocket ss = null;
	//public Socket s = null;
	List<Client> clients = new ArrayList<>();
	//DataOutputStream dos = null;
	
	public static void main(String[] args)  //要多次调用readUTF()方法   Client端 发送多少次给服务端 服务端就要接受多少次 用到死循环
	{
		new Server().start();
	}
	
	public void start()
	{
		try
		{
			 ss = new ServerSocket(8888);//服务端起来了
			 isstart = true;//服务端起来后 将其设置为 true
		}
		catch(BindException e)//这个异常一定要 先比 IOException 先 捕获 不然 如果先捕获 IOException 这个 异常就捕获不了了 然后就会报错 提示要先捕获 这个异常
		{
			System.out.println("端口使用中……");
			System.out.println("请关掉服务端 ，然后重新启动服务端。");
			System.exit(0);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		try 
		{
			while(isstart)//服务端起来后就一直 循环 检测是否有客户端连接
			{
				Socket s = ss.accept();//客户端连接到服务端后 设置其为true
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
				dis = new DataInputStream(s.getInputStream());//客户端连接到服务端后 设置isconnect =  true
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
				while(isconnect)//客户端连接服务端后 循环 以便 接受 客户端 每一次发送的消息
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
				//dis.close();//当服务端没有起来 或者 客户端没有连接 到服务端 就将管道关闭*/
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


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;


public class Client extends Frame
{
	TextField txf = new TextField();
	//TextField txf = null;
	//Ӧ����Ҫ���г�ʼ��  ��Ȼ txa.setText(txa.getText()+str+"\n");���ڼ�¼�����¼�����ͻ� ÿ�ζ�Ϊnull ��¼���治��
	TextArea txa = new TextArea();
	DataOutputStream dos = null;
	DataInputStream dis = null;
	Socket s = null;
	boolean isconnect = false;
	Thread receive = new Thread(new Receive());//new Receive()�����ˣ���
	
	public static void main(String[] args)
	{
		new Client().lanuch();
	}
	public void lanuch()
	{
		Frame frame = new Frame("Chat!");
		frame.setLayout(new BorderLayout());
		frame.setLocation(200,300);
		frame.setSize(200, 150);
		//txf = new TextField();
		//txa = new TextArea();
		frame.add(txf, BorderLayout.SOUTH);//����������ܵ�˳��Ҳ�����Ⱥ��
		frame.add(txa, BorderLayout.CENTER);//
		txf.addActionListener(new TxfListener());
		//txa.setEditable(false);
		frame.pack();
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter()
				{
					@Override
					public void windowClosing(WindowEvent e)
					{
						disconnect();
						System.exit(0);
					}
				});
		connected();
	}
	
	public void disconnect()
	{
		try
		{
			dos.close();
			s.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void connected()
	{
		try
		{
			 s = new Socket("127.0.0.1",8888);
			 dos = new DataOutputStream(s.getOutputStream());
			 dis = new DataInputStream(s.getInputStream());
			 isconnect = true;
			 receive.start();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
System.out.println("connect");
	}
	
	class Receive implements Runnable
	{

		@Override
		public void run() {
			try
			{
				while(isconnect)
				 {
					 String str = dis.readUTF(); 
					 txa.setText(txa.getText()+str+"\n");
				 }
			}
			catch(SocketException e)
			{
				System.out.println("ϵͳ�˳�����");
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	private class TxfListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			String str = txf.getText();
			//txa.setText(str);
			txf.setText("");
			try 
			{
				dos.writeUTF(str);
				dos.flush();
				//dos.close();
				//s.close();
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
		
	}
}

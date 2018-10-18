package cn;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
//Java program to illustrate Client side 
//Implementation using DatagramSocket 
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress; 
import java.util.Scanner; 

class object implements Serializable
{
	String message;
	int seq_num;
	int window_size;
	int my_port;
	String my_ip;
	int ack;
	public object(String message,int seq_num,int window_size,int my_port,String my_ip,int ack)
	{
		this.message=message;
		this.seq_num=seq_num;
		this.window_size = window_size;
		this.my_port = my_port;
		this.my_ip = my_ip;
		this.ack = ack;
	}

}

public class rdt3_client_final
{ 
public static void main(String args[]) throws IOException 
{ 
   Scanner sc = new Scanner(System.in); 
   
     System.out.println("Enter the port number you want to connect at :- ");
	 int port = sc.nextInt();
	 sc.nextLine();
	 System.out.println("Enter the ip address you want to connect at :- ");
	 String ip_addr = sc.nextLine();
	 
	 System.out.println("Enter the window size :- ");
	 int n = sc.nextInt();
	 
	 System.out.println("Enter the port number you want to listen at :- ");
	 int listen_port = sc.nextInt();
	 sc.nextLine();
	 
	 System.out.println("Enter the your ip address :- ");
	 String my_ip = sc.nextLine();

   DatagramSocket ds = new DatagramSocket(listen_port); 
   InetAddress ip = InetAddress.getByName(ip_addr); 
   
   int packet_num = 0;

  // loop while user not enters "bye" 
   for (int i=0;i<20000;i++)
	 {
		 String inp = Integer.toString(i);
		 packet_num = (packet_num+1)%n;
		 object packet = new object(inp,packet_num,n,listen_port,my_ip,0);		 
		 ByteArrayOutputStream bos = new ByteArrayOutputStream();
	     ObjectOutputStream oos = new ObjectOutputStream(bos);
	     oos.writeObject(packet);
	     oos.flush();
		 byte buf[] = bos.toByteArray();
		 
       DatagramPacket DpSend = 
             new DatagramPacket(buf, buf.length, ip, port); 
       ds.send(DpSend); 
       
       //Read acknowledgement
       
       byte[] receive = new byte[2048];
       DatagramPacket  DpReceive = new DatagramPacket(receive, receive.length); 
		    	 
       try 
       {
		    ds.receive(DpReceive);
		    ByteArrayInputStream in = new ByteArrayInputStream(receive);
		    ObjectInputStream is = new ObjectInputStream(in);
		  
		    object ack_packet = (object)is.readObject();   
		     
		    		 
		    		
		    System.out.println("Ack from server for:-"+i+ ack_packet.ack);
		                 
		    receive = new byte[2048]; 
		    		 
	 	 }
		  	 	 
	 	 catch (ClassNotFoundException e) 
	 	 {
		 		 e.printStackTrace();
		 }
       
	 }
   while (true) 
   { 
       String inp = sc.nextLine(); 
       packet_num = (packet_num+1)%n;
       
       object packet = new object(inp,packet_num,n,listen_port,my_ip,0);		 
       ByteArrayOutputStream bos = new ByteArrayOutputStream();
	   ObjectOutputStream oos = new ObjectOutputStream(bos);
	   oos.writeObject(packet);
	   oos.flush();
	   byte buf[] = bos.toByteArray();       
         
		 
       DatagramPacket DpSend = 
             new DatagramPacket(buf, buf.length, ip, port); 
       ds.send(DpSend);
       
       if (inp.equals("bye")) 
         break; 
   } 
} 
} 
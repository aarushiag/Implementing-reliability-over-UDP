//client to Server objects reaching along with seq num

package cn;


import java.io.ByteArrayOutputStream;
//Java program to illustrate Client side 
//Implementation using DatagramSocket 
import java.io.IOException;
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
	public object(String message,int seq_num,int window_size)
	{
		this.message=message;
		this.seq_num=seq_num;
		this.window_size = window_size;
	}

}

public class rd3_client
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
 
     DatagramSocket ds = new DatagramSocket(); 
     InetAddress ip = InetAddress.getByName(ip_addr); 
     
     int packet_num = 0;

    // loop while user not enters "bye" 
     for (int i=0;i<20000;i++)
	 {
		 String inp = Integer.toString(i);
		 packet_num = (packet_num+1)%n;
		 object packet = new object(inp,packet_num,n);		 
		 ByteArrayOutputStream bos = new ByteArrayOutputStream();
	     ObjectOutputStream oos = new ObjectOutputStream(bos);
	     oos.writeObject(packet);
	     oos.flush();
		 byte buf[] = bos.toByteArray();
		 
         DatagramPacket DpSend = 
               new DatagramPacket(buf, buf.length, ip, port); 
         ds.send(DpSend); 
         
	 }
     while (true) 
     { 
         String inp = sc.nextLine(); 
         packet_num = (packet_num+1)%n;

         
         object packet = new object(inp,packet_num,n);		 
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
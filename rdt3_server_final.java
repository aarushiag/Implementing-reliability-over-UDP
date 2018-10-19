package cn;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
//Java program to illustrate Server side 
//Implementation using DatagramSocket 
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress; 
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Scanner; 



class object implements Serializable
{
	String message;
	int seq_num;
	 
	int my_port;
	String my_ip;
	int ack;
	public object(String message,int seq_num,int my_port,String my_ip,int ack)
	{
		this.message=message;
		this.seq_num=seq_num;
		 
		this.my_port = my_port;
		this.my_ip = my_ip;
		this.ack = ack;
	}

}

public class rdt3_server_final
{ 
 
	static volatile int pointer =-1;

	
	public static void main(String[] args) throws IOException 
	{ 
		int window_size =0;
		int client_port =0;
		InetAddress  client_address;
		 
		
		
		System.out.println("Enter the port number you want to listen at :- ");
		Scanner obj = new Scanner(System.in);
		int port = obj.nextInt();
		 
		System.out.println("Enter the window size :- ");
		int n = obj.nextInt();
		 
		 
	    DatagramSocket receiving_port = new DatagramSocket(port);
	    DatagramSocket sending_port = new DatagramSocket(8080);
	    
	    byte[] receive = new byte[50000]; 
	   
	    DatagramPacket DpReceive = null; 
	   
	//   for(int i=0;i<20000;i++)
	    while(true)
	    {
	  	 
	  	 DpReceive = new DatagramPacket(receive, receive.length); 
	
	       
	  	 	try {
		    		receiving_port.receive(DpReceive);
		    		ByteArrayInputStream in = new ByteArrayInputStream(receive);
		    		ObjectInputStream is = new ObjectInputStream(in);
		    		 
		    		object packet = (object)is.readObject();   
		    		String data = packet.message;
		    		client_port = packet.my_port;
		    		client_address = InetAddress.getByName(packet.my_ip);
		    		System.out.println(data);
		    		
		    		if(packet.seq_num == (pointer+1)%n)
		    		{
		    		   	 System.out.println("Client:-" + data);		    		    
		    		     pointer=(pointer+1)%n;
		    		}
		    		
		    		 //Send Acknowledgement 
	    		   	 object ack_packet = new object(null,packet.seq_num,0,null,pointer);		 
	    			 ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    		     ObjectOutputStream oos = new ObjectOutputStream(bos);
	    		     oos.writeObject(ack_packet);
	    		     oos.flush();
	    			 byte buf[] = bos.toByteArray();
	    			 
	    			 DatagramPacket DpSend = 
	    		             new DatagramPacket(buf, buf.length, client_address, client_port); 
	    		       sending_port.send(DpSend);
		    		             
		    		receive = new byte[50000]; 
		    		
		    		
		    	 
		    		
		    		 
	  		 }
	  	 	  
	  	 	 catch (ClassNotFoundException e) 
	  	 	 {
	  	 		 e.printStackTrace();
	  		 }
	//  		 String data = new String(receive);
	//  		 data = data.trim();   		 
	  		 
	  	      
	  	 
	    }
	   
	 //  receiving_port.setSoTimeout(0);
	 //  while (true) 
	//   { 
	
	//  	 DpReceive = new DatagramPacket(receive, receive.length); 
	//  	 
	//  	 try 
	//  	 {
	//  		receiving_port.receive(DpReceive);
	//  		ByteArrayInputStream in = new ByteArrayInputStream(receive);
	//  		ObjectInputStream is = new ObjectInputStream(in);
	//  		 
	//  		object packet = (object)is.readObject();   
	//  		String data = packet.message;
	//  		window_size = packet.window_size;
	//		client_port = packet.my_port;
	//		client_address = InetAddress.getByName(packet.my_ip);
	//  		if (data.equals("bye")) 
	//  	    { 
	//  	        System.out.println("Client EXITING"); 
	//  	        break; 
	//  	    } 
	//  		
	//  		System.out.println("Client:-" + data);
	//  		             
	//  		receive = new byte[2048]; 
	//  		 
	//		 }
	//	 	 
	//	 	 catch (ClassNotFoundException e) 
	//	 	 {
	//	 		 e.printStackTrace();
	//		 }
	//  	 
//	     } 
	} 

} 
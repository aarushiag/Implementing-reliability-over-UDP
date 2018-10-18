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

public class rdt3_server_final
{ 

static int pointer =0;
static int window_size =0;
static int client_port =0;
static  InetAddress  client_address;

public static void main(String[] args) throws IOException 
{ 
   // Step 1 : Create a socket to listen at specified port 
	 System.out.println("Enter the port number you want to listen at :- ");
	 Scanner obj = new Scanner(System.in);
	 int port = obj.nextInt();
   DatagramSocket ds = new DatagramSocket(port); 
   byte[] receive = new byte[2048]; 
   
   DatagramPacket DpReceive = null; 
   
   for(int i=0;i<20000;i++)
   {
  	 
  	 DpReceive = new DatagramPacket(receive, receive.length); 

       
  	 	try {
	    		 ds.receive(DpReceive);
	    		 ByteArrayInputStream in = new ByteArrayInputStream(receive);
	    		 ObjectInputStream is = new ObjectInputStream(in);
	    		 
	    		object packet = (object)is.readObject();   
	    		String data = packet.message;
	    		window_size = packet.window_size;
	    		client_port = packet.my_port;
	    		client_address = InetAddress.getByName(packet.my_ip);
	    		if(Integer.parseInt(data)==pointer)
	    		{
	    		   	 System.out.println("Client:-" + data);
	    		   	 pointer++;
	    		   	 
	    		   	 //Send Acknowledgement
	    		   	 object ack_packet = new object(null,0,0,0,null,1);		 
	    			 ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    		     ObjectOutputStream oos = new ObjectOutputStream(bos);
	    		     oos.writeObject(packet);
	    		     oos.flush();
	    			 byte buf[] = bos.toByteArray();
	    			 
	    			 DatagramPacket DpSend = 
	    		             new DatagramPacket(buf, buf.length, client_address, client_port); 
	    		       ds.send(DpSend);
	    		}
	    		else
	    		{
	    		            	 
	    		   	 for(int j=0;j<Integer.parseInt(data)-pointer;j++)
	    		   	 {System.out.println("Packet lost! :(((");}
	    		   	 System.out.println("Client:-" + data); 
	    		   	 pointer=Integer.parseInt(data)+1;
	    		}  
	    		             
	    		receive = new byte[2048]; 
	    		ds.setSoTimeout(10000);
	    		
	    		 
  		 }
  	 	  catch(SocketTimeoutException e)
	  		  {
	  		   		for(int j=pointer;j<20000;j++)
	  		  		{
	  		 		  System.out.println("Packet lost! :(((");
	  		    	}
	  		    	break;
	  		   }
  	 	 catch (ClassNotFoundException e) 
  	 	 {
  	 		 e.printStackTrace();
  		 }
//  		 String data = new String(receive);
//  		 data = data.trim();   		 
  		 
  	      
  	 
   }
   
   ds.setSoTimeout(0);
   while (true) 
   { 

  	 DpReceive = new DatagramPacket(receive, receive.length); 
  	 
  	 try 
  	 {
  		ds.receive(DpReceive);
  		ByteArrayInputStream in = new ByteArrayInputStream(receive);
  		ObjectInputStream is = new ObjectInputStream(in);
  		 
  		object packet = (object)is.readObject();   
  		String data = packet.message;
  		window_size = packet.window_size;
		client_port = packet.my_port;
		client_address = InetAddress.getByName(packet.my_ip);
  		if (data.equals("bye")) 
  	    { 
  	        System.out.println("Client EXITING"); 
  	        break; 
  	    } 
  		
  		System.out.println("Client:-" + data);
  		             
  		receive = new byte[2048]; 
  		 
		 }
	 	 
	 	 catch (ClassNotFoundException e) 
	 	 {
	 		 e.printStackTrace();
		 }
  	 
   } 
} 

} 
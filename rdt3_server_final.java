package cn;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
//Java program to illustrate Server side 
//Implementation using DatagramSocket 
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress; 
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner; 



class object implements Serializable
{
	String message;
	int seq_num;
	 
	int my_port;
	String my_ip;
	int ack;
	int rcwnd;
	public object(String message,int seq_num,int my_port,String my_ip,int ack,int rcwnd)
	{
		this.message=message;
		this.seq_num=seq_num;
		 
		this.my_port = my_port;
		this.my_ip = my_ip;
		this.ack = ack;
		this.rcwnd = rcwnd;
	}

}

public class rdt3_server_final
{ 
 
	static volatile int pointer =-1;

	static ArrayList<Integer> buffer;
	static int buf_len = 0;
	static long time =0;
 
	public static void main(String[] args) throws IOException 
	{ 
		int window_size =0;
		int client_port =0;
		InetAddress  client_address;
		 
		
		
		System.out.println("Enter the port number you want to listen at :- ");
		Scanner obj = new Scanner(System.in);
		int port = obj.nextInt();
		 
		System.out.println("Enter the buffer window size :- ");
		int n = obj.nextInt();
	    buf_len = n;
		 
		buffer =  new ArrayList<>();
				
	    DatagramSocket receiving_port = new DatagramSocket(port);
	    DatagramSocket sending_port = new DatagramSocket(8080);
	    
	    byte[] receive = new byte[50000]; 
	   
	    receiving_port.setSoTimeout(5000);
	    DatagramPacket DpReceive = null; 
	   
 
	    
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
		    		
		    		int counter = 0;
		    		
		    		if(packet.seq_num == (pointer+1))
		    		{
		    			if(buffer.isEmpty())
			    		   	time =  System.currentTimeMillis();
		    			
		    		   	buffer.add(packet.seq_num);;		    		   	
		    			System.out.println("Successfully received from Client:-" + data);		    		    
		    		    pointer=(pointer+1);
		    		    counter = 1;
		    		}
		    		
		    		else
		    		{
		    			System.out.println("Packet Discarded:-"+data);
		    		}
		    		
		    		 //Send Acknowledgement 
	    		   	 object ack_packet = new object(null,packet.seq_num,0,null,pointer,buf_len-buffer.size());		 
	    			 ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    		     ObjectOutputStream oos = new ObjectOutputStream(bos);
	    		     oos.writeObject(ack_packet);
	    		     oos.flush();
	    			 byte buf[] = bos.toByteArray();
	    			 
	    			 DatagramPacket DpSend = 
	    		             new DatagramPacket(buf, buf.length, client_address, client_port); 
	    		       sending_port.send(DpSend);
	    		       
	    		     
	    		       if (buffer.size()==buf_len || System.currentTimeMillis()-time >500)
	   	    		    {
	    		    	   int x = buffer.size();
	   	    		    	 for(int i=0;i<x/2;i++)
	   	    		    	 {
	   	    		    		 System.out.println("Successfully transferred to above layer :- "+buffer.remove(0));;
	   	    		    	 }
	   	    		    	 time = System.currentTimeMillis();
	   	    		    }
	    		       
	    		      
 		    		             
		    		receive = new byte[50000];    	 
		    		
		    		 
	  		 }
	  	 	
	  	 	 catch(SocketTimeoutException e)
			 {
	  	 		while(!buffer.isEmpty())
	  	 		{
	  	 			System.out.println("Successfully transferred to above layer :- "+buffer.remove(0));;
	  	 		}
	  	 		 
			 }
	  	 	  
	  	 	 catch (ClassNotFoundException e) 
	  	 	 {
	  	 		 e.printStackTrace();
	  		 }
   	      
	  	 
	    }
	   
 
	} 

} 
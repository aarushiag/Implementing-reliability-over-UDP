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
import java.net.SocketTimeoutException;
import java.util.HashMap;
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

 
public class rdt3_client_final
{ 
	
	static int packet_num = 0;
	static int window_strt = 0;
	static int window_end = 0;
	static long timeout = 0;
	static int max_window_strt =0;
	static int max_packets = 2000;
	static int last_ack = 0;
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
		 window_end = Math.min(n-1, max_packets-1);
		 max_window_strt = Math.max(0,max_packets-n);
		 
		 System.out.println("Enter the port number you want to listen at :- ");
		 int listen_port = sc.nextInt();
		 sc.nextLine();
		 
		 System.out.println("Enter the your ip address :- ");
		 String my_ip = sc.nextLine();
	
	   DatagramSocket receiving_port = new DatagramSocket(listen_port);
	   DatagramSocket sending_port = new DatagramSocket(8000);
	   
	   InetAddress ip = InetAddress.getByName(ip_addr); 
	   
 
	   receiving_port.setSoTimeout(100);
	   
	   Thread t1 = new Thread(){
	         public void run(){
	      	 for (int i=window_strt;i<=window_end;i++)
	  		 {
	      		 
	  			 String inp = Integer.toString(i);
	  			 object packet = new object(inp,packet_num,listen_port,my_ip,0,0);		 
	  			 ByteArrayOutputStream bos = new ByteArrayOutputStream();
	  		     ObjectOutputStream oos;
				try {
					oos = new ObjectOutputStream(bos);
					oos.writeObject(packet);
					
					 byte buf[] = bos.toByteArray();
		  			 
			  	     DatagramPacket DpSend = 
			  	             new DatagramPacket(buf, buf.length, ip, port); 
			  	       sending_port.send(DpSend);
			  	     timeout = System.currentTimeMillis(); 
			  	
			  	     packet_num = (packet_num+1);
			  	       
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}	  		      
	  			
   
	  		 }
	      	 
	         }
	     };
	     
	     
	     Thread t2 = new Thread(){
	    	 
	         public void run(){	        	 
	        	 
		  	       
		  	     //Read acknowledgement
	        	 
	        	
	        	 while(true)
	        	 {
		  	       
		  	        byte[] receive = new byte[50000];
		  	        DatagramPacket  DpReceive = new DatagramPacket(receive, receive.length); 
		  			    	 
		  	        try 
		  	        {
		  	        	try
		  			    {
		  	        		
			  	        	receiving_port.receive(DpReceive);
			  			    ByteArrayInputStream in = new ByteArrayInputStream(receive);
			  			    ObjectInputStream is = new ObjectInputStream(in);
			  			  
			  			    object ack_packet = (object)is.readObject();   
			  			     
			  			 
			  			  
			  			  if(ack_packet!=null)
			  			  {
			  			    		
			  			    System.out.println("Ack from server for:-"+ack_packet.seq_num+" "+ ack_packet.ack);
			  			    
			  			   
			  			   
			  			    
			  			   
			  			    if(ack_packet.ack==ack_packet.seq_num)
			  			    {
			  			    	   last_ack= ack_packet.ack;
			  			    	 
								   if(ack_packet.ack==max_packets-1)
								   {
									   break;
								   }
			  			    	 	
								   //Shift the window one right
								   if(ack_packet.ack<max_window_strt)
								   {
					  			    	window_strt++;
					  			    	window_end++;
					  			    	
					  			    // Send the last packet of the new window
					  			      
//					  			       if(ack_packet.rcwnd!=0)
//					  			       {
						  			       String inp = Integer.toString(window_end);
						  			 	   object packet = new object(inp,packet_num,listen_port,my_ip,0,0);	
						  			 	   
							  			   ByteArrayOutputStream bos = new ByteArrayOutputStream();
							  		       ObjectOutputStream oos;
											 
										   oos = new ObjectOutputStream(bos);
										   oos.writeObject(packet);
											
										   byte buf[] = bos.toByteArray();
									  			 
										   DatagramPacket DpSend = 
										  	             new DatagramPacket(buf, buf.length, ip, port); 
										   sending_port.send(DpSend);
										   timeout = System.currentTimeMillis();
										  	
										   packet_num = (packet_num+1);	
//					  			       }
								   
								   }				  
							   
			  			    	 
			  			    	
			  			    }
			  			    else
			  			    {
			  			    	//Retransmission due to cumack
			  			    	long curr_time = System.currentTimeMillis();
			  			    	 
			  			    	//Timeout
			  			    	if(curr_time-timeout>200)
			  			    	{
			  			    		//Send the lost packets of the window again
			  			    		  int rcwnd = ack_packet.rcwnd;
			  			    		  for(int i=last_ack+1;i<=window_end;i++)
	 		  			    		  {
			  			    		   String inp = Integer.toString(i);
					  			 	   object packet = new object(inp,i,listen_port,my_ip,0,0);	
					  			       ByteArrayOutputStream bos = new ByteArrayOutputStream();
						  		       ObjectOutputStream oos;
										
									   oos = new ObjectOutputStream(bos);
									   oos.writeObject(packet);
											
									   byte buf[] = bos.toByteArray();
								  			 
									   DatagramPacket DpSend = 
									  	             new DatagramPacket(buf, buf.length, ip, port); 
									   sending_port.send(DpSend);				  	       
									   timeout = System.currentTimeMillis(); 
	 		  			    		  }
										   		    
			  			    		
			  			    	}
			  			    	
		  			       }	  			    
		  			     
		  			    		 
		  		 	     }
		  			 }
		  	        	
		  	         catch(SocketTimeoutException e)
					 {
		  			    	//Retransmission due to lost packet
			  	        	for(int i=last_ack+1;i<=window_end;i++)
	 			    		  {
				    		   String inp = Integer.toString(i);
			  			 	   object packet = new object(inp,i,listen_port,my_ip,0,0);	
			  			       ByteArrayOutputStream bos = new ByteArrayOutputStream();
				  		       ObjectOutputStream oos;
								
							   oos = new ObjectOutputStream(bos);
							   oos.writeObject(packet);
									
							   byte buf[] = bos.toByteArray();
						  			 
							   DatagramPacket DpSend = 
							  	             new DatagramPacket(buf, buf.length, ip, port); 
							   sending_port.send(DpSend);				  	       
							   timeout = System.currentTimeMillis(); 
 			    		     }
					  	  
					  	  
					  }
		  			  
		  	        }
		  	        
		  	      
		  	       
		  	       	catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
		  			  	 	 
		  		 	catch (ClassNotFoundException e) 
		  		 	{
		  				 e.printStackTrace();
		  			}
		  	       
		  	       
	        	 }	 
	             
	         }
	     };
	 
	     t2.start();
	     t1.start();
	     
	} 
} 
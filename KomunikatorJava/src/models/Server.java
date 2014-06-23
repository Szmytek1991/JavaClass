package models;

 import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import windows.TcpConnection;
 
 public class Server
   extends Thread
 {
   @SuppressWarnings({ "unchecked", "rawtypes" })
private static Vector<Socket> sockets = new Vector(3);
   @SuppressWarnings({ "rawtypes", "unchecked" })
private static Vector<String> name = new Vector(3);
   @SuppressWarnings({ "unchecked", "rawtypes" })
private static Vector<String> ip = new Vector(3);
   @SuppressWarnings({ "rawtypes", "unchecked" })
private static Vector<ObjectOutputStream> out = new Vector(3);
   @SuppressWarnings({ "unchecked", "rawtypes" })
private static Vector<ObjectInputStream> in = new Vector(3);
   private static int port;
   private int ID;
   
   private Server() {}
   
   private Server(Socket socket)
   {
     try
     {
       sockets.add(socket);
       out.add(new ObjectOutputStream(socket.getOutputStream()));
       ((ObjectOutputStream)out.get(this.ID = out.size() - 1)).flush();
       in.add(new ObjectInputStream(socket.getInputStream()));
       ip.add(socket.getInetAddress().getHostAddress());
     }
     catch (IOException ex)
     {
       Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
     }
   }
   
   public static void start(String a_port)
   {
     port = Integer.parseInt(a_port);
     ServerSocket ss = null;
     try
     {
    	 	InetAddress thisIp = null;
			NetworkInterface ni = NetworkInterface.getByName("wlan0");    
	    	Enumeration<InetAddress> inetAddresses =  ni.getInetAddresses();
	    	while(inetAddresses.hasMoreElements()) {  
	    	         InetAddress ia = inetAddresses.nextElement();  
	    	         if(!ia.isLinkLocalAddress()) {  
	    	             thisIp=ia;
	    	         }    
	    	} 
       ss = new ServerSocket(port, 0,thisIp);
       try
       {
         for (;;)
         {
           Server t = new Server(ss.accept());
           t.start();
         }
       }
       catch (IOException ex)
       {
         Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
       }
     }
     catch (IOException ex)
     {
       Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
     }
   }
   
   public void run()
   {
     try
     {
       name.add(((ObjectInputStream)in.get(this.ID)).readObject().toString());
       sendMessage(null, (String)name.get(this.ID) + " joined conversation");
       while(true)
       {
         sendMessage((String)name.get(this.ID), ((ObjectInputStream)in.get(this.ID)).readObject().toString());
       }
     }
     catch (Exception ex)
     {
       try
       {
         ((Socket)sockets.get(this.ID)).close();
       }
       catch (IOException ex1)
       {
         Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex1);
       }
       ip.remove(this.ID);
       sockets.remove(this.ID);
       out.remove(this.ID);
       in.remove(this.ID);
       sendMessage(null, (String)name.remove(this.ID) + " left");
     }
   }
   
   private static synchronized void sendMessage(String name, String msg)
   {
     Calendar c = Calendar.getInstance();
     DecimalFormat df = new DecimalFormat("00");
     String time = df.format(c.get(11)) + ":" + df.format(c.get(12)) + ":" + df.format(c.get(13));
     if(name==null)
     {
    	 
    	 name = "server";
     }
     msg = time + "\n " + name + ":\n" + msg;
     for (int i = 0; i < out.size(); i++) {
       try
       {
         ((ObjectOutputStream)out.get(i)).writeObject(msg);
         ((ObjectOutputStream)out.get(i)).flush();
       }
       catch (IOException ex)
       {
         Logger.getLogger(TcpConnection.class.getName()).log(Level.SEVERE, null, ex);
       }
     }
   }
 }

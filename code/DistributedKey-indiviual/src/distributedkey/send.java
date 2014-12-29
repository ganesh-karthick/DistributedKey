/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package distributedkey;

import java.net.*;
import java.util.*;

/**
 *
 * @author Ganesh Karthick.R
 */
public class send {
    DatagramSocket ds;
    member me;
    String msg,cmd;
    Byte buffer[];
    int noofusers;
    int ports[];
    RC4 rc4;
    int key=31101;
    int broadcastports[];
    boolean isstandalone;
    persistentdata pd;
    receive r;
    final int startpt=3000,stoppt=3010,defport=3000;
    
    public send(DatagramSocket ds,int ports[],member me)
    {
        this.ds=ds;
        this.ports=ports;
        this.me=me;
        rc4=new RC4(key);
        broadcastports=new int[51];
        for(int i=startpt;i<=stoppt;i++)
            broadcastports[i-defport]=i;
    }
    public send(DatagramSocket ds,int port,member me)
    {
        this.ds=ds;
        ports=new int[1];
        ports[0]=port;
        this.me=me;
        rc4=new RC4(key);
        broadcastports=new int[51];
        for(int i=startpt;i<=stoppt;i++)
            broadcastports[i-defport]=i;
    }
    public send(DatagramSocket ds)
    {
        this.ds=ds;
        ports=new int[1];
        ports[0]=defport;
         rc4=new RC4(key);
         broadcastports=new int[51];
        for(int i=startpt;i<=stoppt;i++)
            broadcastports[i-defport]=i;
    }
    public DatagramPacket getpacket(String msg,InetAddress ip,int pt,boolean isencrypted)
    {
        if(isencrypted)
          return new DatagramPacket(rc4.encrypt(msg).getBytes(),msg.getBytes().length,ip,pt);
        else
          return new DatagramPacket(msg.getBytes(),msg.getBytes().length,ip,pt); 
    }
    public DatagramPacket getpacket(byte msg[],InetAddress ip,int pt,boolean isencrypted)
    {
        if(isencrypted)
          return new DatagramPacket(rc4.encryptbyte(new String(msg)),msg.length,ip,pt);
        else
          return new DatagramPacket(msg,msg.length,ip,pt); 
    }
    public void setstandlone(boolean flag)
    {
        isstandalone=flag;
    }
    public void setpd(persistentdata pd)
    {
        this.pd=pd;
    }
    public void setreceive(receive r)
    {
        this.r=r;
    }
    public Vector getofflinemsgs()
    {
        return pd.getallofflinemsgs();
    }
    public boolean isalreadythere(Object otmp[],String id)
   {
        boolean isthere=false;
        String tmp;
        for(int i=0;i<otmp.length;i++)
        {
            tmp=(String)otmp[i];
            if(id.equalsIgnoreCase(tmp))
                isthere=true;
        }
        return isthere;
   }
    synchronized public void sendmsg(String msg,String toip,boolean isencrypted)  //UNICAST
    {
        try
        {
         member m=new member(toip);   
         this.msg=msg;
         for(int i=0;i<ports.length;i++)
          if(isalreadythere(r.getonlinemembers(),toip))   
           ds.send(getpacket(msg,m.getip(),m.getport(),isencrypted));
          else
            pd.addofflinemsg(new offlinemsg(new Date(),toip,msg));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e.toString());
        }   
    }
     synchronized public void sendmsg(byte[] msg,String toip,boolean isencrypted)  //UNICAST
    {
        try
        {
         member m=new member(toip);   
         for(int i=0;i<ports.length;i++)
          if(isalreadythere(r.getonlinemembers(),toip))   
           ds.send(getpacket(msg,m.getip(),m.getport(),isencrypted));
          else
            pd.addofflinemsg(new offlinemsg(new Date(),toip,new String(msg)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e.toString());
        }   
    }
    synchronized public void sendmsg(String msg,String toip[],boolean isencrypted) //MULTICAST
    {
        try
        {
         int i;
         member m[]=new member[toip.length];   
         for(i=0;i<toip.length;i++)
             m[i]=new member(toip[i]);
         this.msg=msg;
         for(i=0;i<m.length;i++)
           if(m[i].getport()!=me.getport()) 
               if(isalreadythere(r.getonlinemembers(),m[i].getid())) 
                 ds.send(getpacket(msg,m[i].getip(),m[i].getport(),isencrypted));
               else
                  pd.addofflinemsg(new offlinemsg(new Date(),m[i].getid(),msg));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }   
    }
     synchronized public void sendmsg(byte[] msg,String toip[],boolean isencrypted) //MULTICAST
    {
        try
        {
         int i;
         member m[]=new member[toip.length];   
         for(i=0;i<toip.length;i++)
             m[i]=new member(toip[i]);
         for(i=0;i<m.length;i++)
           if(m[i].getport()!=me.getport()) 
               if(isalreadythere(r.getonlinemembers(),m[i].getid())) 
                 ds.send(getpacket(msg,m[i].getip(),m[i].getport(),isencrypted));
               else
                  pd.addofflinemsg(new offlinemsg(new Date(),m[i].getid(),new String(msg)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }   
    }
    synchronized public void sendmsg(String msg,boolean isstandalone,boolean isencrypted)  //BROADCAST
    {
        try
        {
         InetAddress ip=InetAddress.getByName("1.1.1.1");
         if(!isstandalone)
             ds.send(getpacket(msg,ip,defport,isencrypted));
         else
         {
             for(int i=startpt;i<=stoppt;i++)
                 if(i!=me.getport())
                  ds.send(getpacket(msg,InetAddress.getLocalHost(),i,isencrypted));
         }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    } 
    synchronized public void sendmsg(String msg,boolean isencrypted)  //BROADCAST
    {
        try
        {
         InetAddress ip=InetAddress.getByName("1.1.1.1");
         if(!isstandalone)
             ds.send(getpacket(msg,ip,defport,isencrypted));
         else
         {
             for(int i=startpt;i<=stoppt;i++)
                 if(i!=me.getport())
                  ds.send(getpacket(msg,InetAddress.getLocalHost(),i,isencrypted));
         }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }  

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package distributedkey;
import java.util.*;
import java.net.*;
import java.io.*;
/**
 *
 * @author Ganesh Karthick.R
 */
public class parser implements Serializable
{
  String id;
  String name;
  String ip; 
  int port;
  int key;
  String host;
  String fname;
  public parser(){}
  public parser(String theid,boolean ismemberid)      /*FORMAT:ID=NAME@HOSTNAME#IP#PORT#PUBKEY*/
  {
    StringTokenizer st=new StringTokenizer(theid,"@#");
    id=theid;
    while(st.hasMoreTokens())
    {
      name=st.nextToken();
      host=st.nextToken();
      ip=st.nextToken();
      try{
       port=Integer.parseInt(st.nextToken()); 
       if(ismemberid)
        key=Integer.parseInt(st.nextToken()); 
      }
     catch(Exception e){}
    }
     fname=name+"@"+host+"#"+String.valueOf(port); /*UNIQUE FILE NAME FOR EVERY CLIENT TO STORE OFFLINE CONTENT*/
  }
  
  public String getid(){return id;}
  public int getport(){return port;}
  public int getkey(){return key;}
  public InetAddress getip()
  {
   try{
    return InetAddress.getByName(host);
   }
   catch(Exception e)
   {
     System.out.println(e);
   }
   return null;
  }
  public String getname(){return name;}
  public String gethost(){return host;}
  public String getfname() {return fname;}
  public String toString()   /*PRINTS CLIENT OBJECT IN A FORMAT*/
  {
    return "Name:"+name+" "+"IP:"+getip()+" "+"Host:"+gethost()+" "+"Port:"+getport();
  }
  public boolean equals(parser p)  /*CHECKS WHETHER TWO INSTANCES ARE EQUAL*/
  {
    if(name.equals(p.name)&&ip.equals(p.ip)&&host.equals(p.host)&&port==p.port&&p.key==key)
      return true;
    else
      return false;       
  }
  static boolean isvaliduserid(String tmp,boolean ismemberid)	
  {
    boolean isvalid=true;  
    String id,name,host,ip;
    int port,key;
    StringTokenizer st=new StringTokenizer(tmp,"@#");
    while(st.hasMoreTokens())
    {
      name=st.nextToken();
      host=st.nextToken();
      ip=st.nextToken();
      try{
       port=Integer.parseInt(st.nextToken()); 
       if(ismemberid)
        key=Integer.parseInt(st.nextToken()); 
      }
     catch(Exception e)
     {
         isvalid=false;
     }
    }
    return isvalid;
  }
}



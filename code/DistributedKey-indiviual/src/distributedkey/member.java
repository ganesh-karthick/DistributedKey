/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package distributedkey;

/**
 *
 * @author Ganesh Karthick.R
 */
import java.util.*;
import java.net.*;
import java.io.*;

public class member implements Serializable
{
	String id,password;
	InetAddress ip;
	int port;
	String host,name,fname;
        int pukey,prkey;
        modfunction mf;
	public member(String id)
	{
	  	parser p=new parser(id,true);
	  	this.id=id;
	  	this.name=p.getname(); 
	  	this.ip=p.getip();
	  	this.host=p.gethost();
	  	this.port=p.getport();
	  	this.pukey=p.getkey();
                this.prkey=0;
                mf=new modfunction();
                this.fname=p.getfname();
	}
	public member(String name,InetAddress ip,String host,int port,int pukey)
	{
	  	this.ip=ip;
	  	this.host=host;
	  	this.port=port;
	  	this.name=name;
	  	this.id=name+"@"+ip.getHostName()+"#"+ip.getHostAddress()+"#"+new Integer(port).toString()+"#"+new Integer(pukey).toString();
                this.prkey=0;
                mf=new modfunction();
                this.fname=name+"@"+host+":"+String.valueOf(port);
	}
	public String getname()
	{
	    return name;
	}
        public void setpassword(String pwd)
        {
            password=pwd;
        }
        public String getpassword()
        {
            return password;
        }
        public String getfname() 
        {
            return fname;
        }
        public int gethashvalue()
        {
            int temp=0;
            String tmp=name+password;
            for(int i=0;i<tmp.length();i++)
                temp+=(int)tmp.charAt(i);
            return Math.abs(temp);
        }
        
	public String getid()
	{
		return id;
	}
        public void setpubkey(int pukey)
        {
            this.pukey=pukey;
        }
        public int getpubkey()
        {
            return pukey;
        }
        public void setprikey(int prkey)
        {
            this.prkey=prkey;
        }
        public int getprikey()
        {
            return prkey;
        }
	public InetAddress getip()
	{
		return ip;
	}
	public String gethost()
	{
		return host;
	}
        public int getport()
	{
		return port;
	}
    public String toString()   /*PRINTS CLIENT OBJECT IN A FORMAT*/
    {
        return id;
    }
}

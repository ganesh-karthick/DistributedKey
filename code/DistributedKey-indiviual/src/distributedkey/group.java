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
public class group extends TimerTask
{
	int count,key;
	String name,id,cmd,algorithm;
	Vector v,vadd,vremove;
        member owner,me;
        modfunction mf;
        boolean iskeydaemonstarted,hasfirstrunoccurred,haschanged;
        send s;
        Timer tmr;
        tree t;
        int rounds,membershipevents;
        long waitime,repeatime;
        nodecache nc;
        preprocess pr;
	public group(String name,member owner,send s,nodecache nc,member me)
	{
		v=new Vector(5,3);
                vadd=new Vector(5,3);
                vremove=new Vector(5,3);
		count=v.size();
		this.name=name;
                this.owner=owner;
                id=name+"@"+owner.gethost()+"#"+owner.getip().getHostAddress()+"#"+String.valueOf(owner.getport());
                this.s=s;
                waitime=100;
                repeatime=3000;
                this.nc=nc;
                mf=new modfunction();
                iskeydaemonstarted=false;
                this.me=me;
                algorithm="REBUILD";
                if(algorithm.equalsIgnoreCase("QUEUEBATCH"))
                    pr=new preprocess(s, nc);
                hasfirstrunoccurred=false;
                haschanged=false;
                rounds=membershipevents=0;
                t=null;
	}
        
        public group(String theid,send s,nodecache nc,member me)
	{
		v=new Vector(5,3);
		count=v.size();
                parser p=new parser(theid,false);
                name=p.getname();
                owner=new member(theid);
                vadd=new Vector(5,3);
                vremove=new Vector(5,3);
                id=theid;
                this.s=s;
                waitime=100;
                repeatime=3000;
                this.nc=nc;
                this.me=me;
                mf=new modfunction();
                iskeydaemonstarted=false;
                algorithm="REBUILD";
                if(algorithm.equalsIgnoreCase("QUEUEBATCH"))
                    pr=new preprocess(s,nc);
                hasfirstrunoccurred=false;
                haschanged=false;
                t=null;
	}
	public String getname()
	{
		return name;
	}
        public String getid()
        {
            return id;
        }
        public String toString()
        {
            return id;
        }
        public void setkey(int key)
        {
            this.key=key;
        }
        public int getkey()
        {
            return key;
        }
        public int getnodekeyvalue(int prkey,int pukey)
	{
		return mf.getmodval(pukey, prkey);
	}
	public void addmember(member m)
	{
           
		v.add(m);
                membershipevents++;
		count=v.size();
	}
        public member getowner()
        {
            return owner;
        }
        public void setowner(String id)
        {
            owner=new member(id);
        }
        public void setowner(member m)
        {
            owner=m;
        }
        public void addmember(String id)
        {
            addmember(new member(id));
        }
	synchronized public void removemember(member m)
	{
                Object obj[]=v.toArray();
                member temp;
                
                
                    membershipevents++;
                    byte cnt=0;
                    while(cnt<obj.length)
                    {       
                        temp=(member)obj[cnt];
                        if(temp.getid().equalsIgnoreCase(m.getid()))
                         v.removeElementAt((int)cnt);
                        cnt++;          
                    }
              
               
		count=v.size();
	}
        synchronized public void removemember(String id)
	{
		removemember(new member(id));
	}
        public boolean iskeydaemonrunning()
        {
            return iskeydaemonstarted;
        }
        public int getcount()
        {
            return count;
        }
       public String[] getstringitems(Object obj[])  
       {
        String items[]=new String[obj.length];
        for(int i=0;i<obj.length;i++)
          items[i]=obj[i].toString();
        return items;
       }
	public Object[] getallmembers()
	{
		Object o[]=v.toArray();
		return o;
	}
        public member getmember(String tid)
        {
            return getmember(tid);
        }
        public void setinterval(long waitime,long repeattime)
        {
            this.waitime=waitime;
            this.repeatime=repeattime;
        }
        public void startkeydaemon(String id)
        {
             run();
             iskeydaemonstarted=true;
        }
        public void stopkeydaemon()
        {
            
            if(tmr!=null)
             tmr.cancel();
            member mtmp;
            Object otemp[]=vremove.toArray(),ov[]=v.toArray();
            for(int i=0;i<otemp.length;i++)        
                {
                    mtmp=(member)otemp[i];
                    for(int j=0;j<ov.length;j++)
                    {
                        member mv=(member)ov[j];
                        if(mv.getid().equalsIgnoreCase(mtmp.getid()))
                            v.removeElementAt(j);
                    }
                }        
            iskeydaemonstarted=false;
        }
        public void run()
        {
           member mtmp;
           Object onode[];
           rounds++;
         
            try{ 
                        
            synchronized(v)
            {
             onode=v.toArray();
            }
            if(onode!=null&&onode.length==1)
            {
                mtmp=(member)onode[0];
                key=mtmp.getprikey();
                hasfirstrunoccurred=true;
            }
            else{
                t=new tree(v,s,nc);
                t.construct();
                key=t.getgrpkey();
                hasfirstrunoccurred=true;
             }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
           printmetrics();
           if(t!=null)
           t.setexponent(0);
           }
        public void printmetrics()
        {
            if(name.equals("ag"))
            {
             System.out.print("\n Rounds: "+rounds+" EVENTS :"+membershipevents);
             if(t!=null)
             System.out.print(" Computations:"+t.getexponent());
            }
        }
        public Vector getvector()
        {
            return v;
        }
	public member getmember(member m)
	{
	  String tid=m.getid();
	  member temp;
	  Iterator itr=v.iterator();
	  while(itr.hasNext())
	  {
	   temp=(member)itr.next();
	   if(tid.equalsIgnoreCase(temp.id))
	    return temp;
	   } 
	  return null;	
	}
        public boolean ismember(String mid)
        {
            Object obj[]=v.toArray();
            member temp;
            byte cnt=0;
            while(cnt<obj.length)
            {
               temp=(member)obj[cnt];
               if(temp.getid().equalsIgnoreCase(mid))
                   return true;
               cnt++;
            }
            return false;
        }
        
}
               
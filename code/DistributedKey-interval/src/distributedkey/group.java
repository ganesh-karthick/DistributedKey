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
	Vector v,vadd,vremove,vtmp;
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
                vtmp=new Vector(5,3);
                this.me=me;
                algorithm="REBUILD";
                if(algorithm.equalsIgnoreCase("QUEUEBATCH"))
                    pr=new preprocess(s, nc);
                hasfirstrunoccurred=false;
                haschanged=false;
                rounds=membershipevents=0;
                 t=null;
	}
        public group(String name,member owner,send s,nodecache nc,member me,String algorithm)
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
                 vtmp=new Vector(5,3);
                this.me=me;
                this.algorithm=algorithm;
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
                 vtmp=new Vector(5,3);
                iskeydaemonstarted=false;
                algorithm="REBUILD";
                if(algorithm.equalsIgnoreCase("QUEUEBATCH"))
                    pr=new preprocess(s,nc);
                hasfirstrunoccurred=false;
                haschanged=false;
                t=null;
	}
        public group(String theid,send s,nodecache nc,member me,String algorithm)
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
                vtmp=new Vector(5,3);
                iskeydaemonstarted=false;
                this.algorithm=algorithm;
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
            vtmp.add(m);
            if(!hasfirstrunoccurred)
            {
		v.add(m);
                membershipevents++;
            }
            else
            {
                vadd.add(m);
                membershipevents++;
                if(algorithm.equalsIgnoreCase("QUEUEBATCH"))
                  pr.add(m);
            }
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
                byte cnt=0;
                    while(cnt<obj.length)
                    {       
                        temp=(member)obj[cnt];
                        if(temp.getid().equalsIgnoreCase(m.getid()))
                         vtmp.removeElementAt((int)cnt);
                        cnt++;          
                    }
                cnt=0;
                if(!hasfirstrunoccurred)
                {
                    membershipevents++;
                    while(cnt<obj.length)
                    {       
                        temp=(member)obj[cnt];
                        if(temp.getid().equalsIgnoreCase(m.getid()))
                         v.removeElementAt((int)cnt);
                        cnt++;          
                    }
                   
                }
                else
                    vremove.add(m);
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
        public String getalgorithm()
        {
            return algorithm;
        }
        public void setalgorithm(String alg)
        {
            this.algorithm=alg;
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
             tmr=new Timer(true);
             
              tmr.schedule(this,waitime,repeatime);
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
           if(algorithm.equalsIgnoreCase("REBUILD"))
           {
            try{ 
                 Object otemp[]=vadd.toArray(),ov[]=v.toArray();
                
                otemp=vadd.toArray();
                for(int i=0;i<vadd.size();i++)
                {
                    v.add(otemp[i]);
                    haschanged=true;
                }
                otemp=vremove.toArray();
                for(int i=0;i<otemp.length;i++)        
                {
                    mtmp=(member)otemp[i];
                    for(int j=0;j<ov.length;j++)
                    {
                        member mv=(member)ov[j];
                        if(mv.getid().equalsIgnoreCase(mtmp.getid()))
                            v.removeElementAt(j);
                        haschanged=true;
                    }
                }        
            vadd.removeAllElements();
            vremove.removeAllElements();
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
                 haschanged=false;
                
             }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
           }
           else if(algorithm.equalsIgnoreCase("QUEUEBATCH"))
           {
               try{
               Object otemp[]=vadd.toArray(),ov[]=v.toArray();
                Vector vcopy=(Vector)v.clone();
                for(int i=0;i<otemp.length;i++)
                   v.add(otemp[i]);
                otemp=vremove.toArray();
                for(int i=0;i<otemp.length;i++)        
                {
                    mtmp=(member)otemp[i];
                    haschanged=true;
                    for(int j=0;j<ov.length;j++)
                    {
                        member mv=(member)ov[j];
                        if(mv.getid().equalsIgnoreCase(mtmp.getid())) 
                               v.removeElementAt(j); 
                    }
                }                
                if(!hasfirstrunoccurred||haschanged)
                {
                    if(haschanged)
                    {
                         vcopy=(Vector)v.clone();
                         otemp=vadd.toArray();
                         ov=vcopy.toArray();
                         for(int i=0;i<otemp.length;i++)        
                         {
                           mtmp=(member)otemp[i]; 
                           for(int j=0;j<ov.length;j++)
                           {
                            member mv=(member)ov[j];
                            if(mv.getid().equalsIgnoreCase(mtmp.getid())) 
                               vcopy.removeElementAt(j); 
                           }
                         }     
                         haschanged=false;
                         
                    }
                   t=new tree(vcopy,s,nc); 
                   t.construct();
                   process();
                   key=t.getgrpkey();
                   hasfirstrunoccurred=true;      
                }
                else
                {  
                   Object on[]=pr.toArray();
                   t.remerge(on);
                   process(); 
                   key=t.getgrpkey();
                   hasfirstrunoccurred=true;   
                   pr.empty();
                } 
                vadd.removeAllElements();
                vremove.removeAllElements();
               }
               catch(Exception ex)
               {
                   ex.printStackTrace();
               }   
             }
           setdata();
           printmetrics();
           membershipevents=0;
           if(t!=null)
           t.setexponent(0);
        }
        public void printmetrics()
        {
            if(name.equals("ag"))
            {
             System.out.print("\nTime "+repeatime+" Rounds: "+rounds+" EVENTS :"+membershipevents);
             if(t!=null)
             System.out.print(" Computation:"+t.getexponent());
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
        public void process()
        {
            v=(Vector)vtmp.clone();
            t=new tree(v,s,nc);
        }
        public void setdata()
        {
         if(membershipevents==0&&t!=null)
               t.setexponent(0);
           if(membershipevents==1&&t!=null)
               t.setexponent(1);
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
               
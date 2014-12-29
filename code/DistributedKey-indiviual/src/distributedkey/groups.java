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
public class groups 
{
  int count;
   Vector v;
   send s;
   nodecache nc;
   member me;
	public groups(send s,nodecache nc,member me)
	{
		v=new Vector(5,3);
		count=v.size();
                this.s=s;
                this.nc=nc;
                this.me=me;
        }
	public void addgroup(group g)
	{
		v.add(g);
		count=v.size();
	}
        public void addgroup(String gname,String oname)
        {
            addgroup(new group(gname,new member(oname),s,nc,me));
        }
        public void addgroup(String gid)
        {
            addgroup(new group(gid,s,nc,me));
        }
	public void removegroup(group g)
	{
		Object obj[]=v.toArray();
                group tmp;
                for(int i=0;i<obj.length;i++)
                {
                    tmp=(group)obj[i];
                    if(tmp.getid().equalsIgnoreCase(g.getid()))
                        v.removeElementAt(i);
                }
		count=v.size();
	}
        public void removegroup(String gid)
        {
            removegroup(new group(gid,s,nc,me));
        }
	public Object[] getallgroups()
	{
		Object g[]=v.toArray();
		return g;
	}
        public Vector getvector()
        {
            return v;
        }
	public group getgroup(group g)
	{
	  String id=g.getid();
	  group temp;
	  Iterator itr=v.iterator();
	  while(itr.hasNext())
	  {
	   temp=(group)itr.next();
	   if(id.equalsIgnoreCase(temp.getid()))
	    return temp;
	  } 
	  return null;	
	}
        public group getgroup(String id)
	{
          return getgroup(new group(id,s,nc,me));  
	}
        public Object[] retrievegroupsforid(String id)
        {
            Object obj[]=v.toArray();
            Vector vgrps=new Vector(5,3);
            group tmp;
            for(int i=0;i<obj.length;i++)
            {
                tmp=(group)obj[i];
                if(id.equalsIgnoreCase(tmp.getowner().getid()))
                    vgrps.add(tmp);
            }           
            return vgrps.toArray();        
        }
        synchronized public void startkeydaemonforid(String id)  //startz key daemon for the groups containing given id
        {
            Object ogrp[]=v.toArray();
            Object ombrs[];
            member mtmp;
            group gtmp;
            for(int i=0;i<ogrp.length;i++)
            {
                gtmp=(group)ogrp[i];
                
                
                 
                   if(id.equalsIgnoreCase(gtmp.getid()))
                   {  
                    gtmp.startkeydaemon(id);
                    synchronized(v)
                    {
                    v.setElementAt(gtmp,i);
                    }
                   }
            }
                 
        }
        synchronized public void stopkeydaemonforid(String id)  //startz key daemon for the groups containing given id
        {
            Object ogrp[]=v.toArray();
            Object ombrs[];
            member mtmp;
            group gtmp;
            for(int i=0;i<ogrp.length;i++)
            {
                gtmp=(group)ogrp[i];
                if(!gtmp.iskeydaemonrunning())
                {
                 ombrs=gtmp.getallmembers();
                 for(int j=0;j<ombrs.length;j++)
                 {
                    mtmp=(member)ombrs[j];
                   if(id.equalsIgnoreCase(mtmp.getid()))
                   {
                    gtmp.stopkeydaemon();
                    synchronized(v)
                    {
                    v.setElementAt(gtmp,i);
                    }
                   }
                 }
                } 
            }           
        }
        public void close()
        {
            Object gs[]=v.toArray();
            byte cnt=0;
            while(cnt<gs.length)
            {
                group g=(group)v.elementAt(cnt);
                g.stopkeydaemon();
                synchronized(v){
                v.setElementAt(g, cnt);
                }
                cnt++;
            }
        }
        public void setgroup(String gid,group g)
        {
           Iterator itr=v.iterator();
           byte cnt=0;
           group temp;
          synchronized(itr) 
          {
	   while(itr.hasNext())
	   {
	     temp=(group)itr.next();
	     if(gid.equalsIgnoreCase(temp.getid()))
             {
               v.setElementAt(g, (int)cnt);
             }
              cnt++;
           }
	 } 
        }
}

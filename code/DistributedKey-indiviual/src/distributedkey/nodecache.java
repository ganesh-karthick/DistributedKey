/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package distributedkey;
import java.io.Serializable;
import java.util.*;
/**
 *
 * @author Ganesh Karthick.R
 */
public class nodecache implements Serializable{
    Vector v;
    public nodecache(int size)
    {
         v=new Vector(size);
    }  
    public nodecache(int size,int growth)
    {
         v=new Vector(size,growth);
    }
    public Object[] getnodeoflength(int len)
    {
        Vector vtemp=new Vector(3,3);
        nodedata ntemp;
        Object ov[]=v.toArray();
        for(int i=0;i<ov.length;i++)
        {
            ntemp=(nodedata)ov[i];
            if(len==ntemp.getlength())
                vtemp.add(ntemp);        
        }
        if(v.size()==0)
            return null;
        else
            return v.toArray();
        
    }
    public Object[] getvector()
    {
      return v.toArray();
    }
    public void add(nodedata nd)
    {
        if(search(nd.getids())==null)
          v.add(nd);
    }
    public void remove(nodedata nd)
    {
        Object o[]=v.toArray();
        nodedata tmp;
        for(int i=0;i<o.length;i++)
        {
            tmp=(nodedata)o[i];
            if(tmp.getids().equalsIgnoreCase(nd.getids()))
                v.remove(i);
        }
    }
    public void update(nodedata nd,String ids)
    {
        Object o[]=v.toArray();
        nodedata tmp;
        for(int i=0;i<o.length;i++)
        {
            tmp=(nodedata)o[i];
            if(ids.equalsIgnoreCase(nd.getids()))
                v.setElementAt(nd, i);
        }
    }
    public nodedata search(String ids)
    {
      int i,len=0;
      nodedata temp;
      String idr,t1,t2;
      StringTokenizer st=new StringTokenizer(ids,":");
      t1=st.nextToken();
      t2=st.nextToken();
      idr=t2+":"+t1;
      for(i=0;i<ids.length();i++)
          if(ids.charAt(i)==':')
              len++;
      len++;
      Object on[]=getnodeoflength(len);
      if(on==null)
          return null;
      for(i=0;i<on.length;i++)
      {
          temp=(nodedata)on[i];
          if(temp.getids().equalsIgnoreCase(ids))
          {
             // System.out.println("ret"+temp.toString());
              return temp;
          }
          if(len==2)
           if(temp.getids().equalsIgnoreCase(idr))
              {
             // System.out.println("ret"+temp.toString());
              return temp;
              }
      }
      return null;         
    }
    public void printcache()
    {
        nodedata ntmp;
        Object on[]=v.toArray();
        for(int i=0;i<on.length;i++)
        {
         ntmp=(nodedata)on[i];   
         System.out.println(ntmp);
        }
    }
}

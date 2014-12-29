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

public class persistentdata implements Serializable {
    
    Vector<offlinemsg> voffmsg;
    Vector<String> vaddrbook;
    persistentgroups pgrps;
    String me;
    nodecache nc;
    public persistentdata(String me)
    {
        voffmsg=new Vector(5,3);
        vaddrbook=new Vector(5,3);
        this.me=me;
        pgrps=new persistentgroups(me);
    }
    public nodecache getnodecache()
    {
        return nc;
    }
    public void setnodecache(nodecache nc)
    {
        this.nc=nc;
    }
    public void createnewgroup()
    {
        pgrps=new persistentgroups(me);
    }
    public Object[] getmsgforid(String id)
    {
        Object otmp[]=voffmsg.toArray();
        offlinemsg offtmp;
        Vector<offlinemsg> vtmp=new Vector(5,3);
        for(int i=0;i<otmp.length;i++)
        {
            offtmp=(offlinemsg)otmp[i];
            if(offtmp.getid().equalsIgnoreCase(id))
             vtmp.add(offtmp);
        }
        return vtmp.toArray();       
    }
    public void delmsgforid(String id)
    {
        Object otmp[]=voffmsg.toArray();
        offlinemsg offtmp;
        for(int i=0;i<otmp.length;i++)
        {
            offtmp=(offlinemsg)otmp[i];
            if(offtmp.getid().equalsIgnoreCase(id))
             voffmsg.remove(i);
        }   
    }
    public void addofflinemsg(offlinemsg msg)
    {
        voffmsg.add(msg);
    }
    public String getidforme()
    {
        return me;
    }
    public void addtoaddrbook(String id)
    {
        boolean isthere=false;
        Object otmp[]=vaddrbook.toArray();
        String tmp;
        for(int i=0;i<otmp.length;i++)
        {
            tmp=(String)otmp[i];
            if(id.equalsIgnoreCase(tmp))
                isthere=true;
        }
        if(!isthere)
         vaddrbook.add(id);
    }
    public void removeaddr(String id)
    {
        Object otmp[]=vaddrbook.toArray();
        String tmp;
        for(int i=0;i<otmp.length;i++)
        {
            tmp=(String)otmp[i];
            if(id.equalsIgnoreCase(tmp))
                vaddrbook.remove(i);
        }
    }
    public Vector getallofflinemsgs()
    {
        return voffmsg;
    }
    public void setofflinemsgs(Vector v)
    {
        this.voffmsg=v;
    }
    public void setaddrbook(Vector v)
    {
        vaddrbook=(Vector<String>)v.clone();
    }
    public Object[] getaddresses()
    {
        return vaddrbook.toArray();
    }
    public void addgroup(String id,String owner,int key,Object ombrs[])
    {
        pgrps.addgroup(new persistentgroup(id, owner,key,ombrs));
    }
   
    public persistentgroups getpgroups()
    {
        return pgrps;
    }
    public void printaddresses()
    {
        Object otmp[]=vaddrbook.toArray();
        String tmp;
        for(int i=0;i<otmp.length;i++)
        {
            tmp=(String)otmp[i];
            System.out.println(tmp);
        }
    }
}

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
public class persistentgroup implements Serializable{
    
    String id;
    String owner;
    int count;
    Vector vmbrs;
    int key;
    public persistentgroup(String id,String owner,int key,Object ombrs[]){
        this.id=id;
        this.owner=owner;
        this.count=ombrs.length;
        this.key=key;
        vmbrs=new Vector(5,3);
        for(int i=0;i<this.count;i++)
            vmbrs.add(ombrs[i]);      
    }
    public String getid()
    {
            return id;
    }
    public Object[] getallmembers()
    {
		Object o[]=vmbrs.toArray();
		return o;
    }
     public String getowner()
       {
            return owner;
       }
     public int getcount()
     {
         return count;
     }
     public int getkey()
     {
         return key;
     }
}

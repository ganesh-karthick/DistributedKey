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
public class persistentgroups implements Serializable{
     Vector<persistentgroup> vgrps;
     int count;
     String me;
     public persistentgroups(String me)
     {
         this.me=me;
         vgrps=new Vector(5,3);
         count=0;
     }
     public void addgroup(persistentgroup pg)
     {
         vgrps.add(pg);
         count=vgrps.size();
     }
     public Object[] getgroups()
     {
         return vgrps.toArray();
     }
    public int getgroupcount()
    {
        return count;
    }
}

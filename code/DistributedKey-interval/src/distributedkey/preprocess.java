/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package distributedkey;
import java.util.*;
/**
 *
 * @author Ganesh Karthick.R
 */
public class preprocess {
    tree t;
    Vector v;
    send s;
    nodecache nc;
    boolean hasfirstaddoccurred;
    public preprocess(send s,nodecache nc)
    {
        this.v=new Vector(5,3);
        this.s=s;
        this.nc=nc;
        hasfirstaddoccurred=false;
    }
        public void add(member m)
        {
             v.add(m);       
             hasfirstaddoccurred=true;
            
        }
        public node getroot()
        {
            printdata();
            t=new tree(v,s,nc);
            t.construct();
            return t.getroot(); 
        }
        public void empty()
        {
            v.removeAllElements();
            hasfirstaddoccurred=false;
        }
        public Object[] toArray()
        {
            return v.toArray();
        }
         public void printdata()
         {
          member mtmp;
          Object on[]=v.toArray();
          for(int i=0;i<on.length;i++)
          {
           mtmp=(member)on[i];   
           System.out.println(mtmp);
          }
         }
}

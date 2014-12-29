/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package distributedkey;

import java.io.Serializable;

/**
 *
 * @author Ganesh Karthick.R
 */
public class nodedata implements Serializable{
    int key;
    String ids;
    int len;
    public nodedata(String ids,int key)
    {
       this.key=key;
       this.ids=ids;
       len=0;
       for(int i=0;i<ids.length();i++)
          if(ids.charAt(i)==':')
              len++;
       len+=1;
    }
    public nodedata(String ids,int key,int len)
    {
       this.key=key;
       this.ids=ids;
       this.len=len;
    }
    public int getlength()
    {
        return len;
    }
    public int getkey()
    {
        return key;
    }
    public void setkey(int key)
    {
         this.key=key;
    }
    public String getids()
    {
            return ids;
    }
    public void setids(String ids)
    {
            this.ids=ids;
    }
    public String toString()
    {
        return ids+":"+String.valueOf(key);
    }

}

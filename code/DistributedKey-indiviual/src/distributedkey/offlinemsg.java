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
public class offlinemsg implements Serializable {
    Date date;
    String id;
    String msg;
    String group;
    
    public offlinemsg(Date date,String id,String group,String msg)
    {
        this.date=date;
        this.id=id;
        this.group=group;
        this.msg=msg;
    }
    public offlinemsg(Date date,String id,String msg)
    {
         String tmp1,tmp2;
        this.date=date;
        this.id=id;
        StringTokenizer st=new StringTokenizer(msg,":");
        if(msg.startsWith("GRPMSG:"))
        {
            st.nextToken();
            st.nextToken();
            this.group=st.nextToken()+"( Sent At: "+date.toString()+" )";
            tmp1=st.nextToken();
            this.msg="GRPMSG:T:"+this.group+":"+tmp1;
        }
        if(msg.startsWith("ONMBRMSG:"))
        {
            st=new StringTokenizer(msg,":>");
            st.nextToken();
            st.nextToken();
            tmp1=st.nextToken()+"( Sent At: "+date.toString()+" )>";
            tmp2=st.nextToken();
            this.msg="ONMBRMSG:T:"+tmp1+tmp2;  
        }

    }
    public offlinemsg(String id,String group,String msg)
    {
        this.date=new Date();
        this.id=id;
        this.group=group;
        this.msg=msg;
    }
    public String getid()
    {
        return id;
    }
    public String getmsg()
    {
        return msg;
    }
}

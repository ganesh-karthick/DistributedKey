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
public class node {
        int key;
	node nleft;
	node nright;
        node nup;
	int pukey,prkey;
        String ids;
	modfunction mf;
        int depth;
	public node(member m)
        {
            this.nleft=null;
            this.nright=null;
            this.nup=null;
            ids=m.getid();
            this.pukey=m.getpubkey();
            this.prkey=m.getprikey();
            mf=new modfunction();
            depth=0;
        }
	public node(node nl,node nr,node nup,int prkey)
	{
		this.nleft=nl;
		this.nright=nr;
                this.nup=nup;
		this.prkey=prkey;
                this.ids=nl.getids()+":"+nr.getids();
                mf=new modfunction();
		this.pukey=mf.getmodval(prkey);
                depth=0;
	}
        public node(node nl,node nr,node nup,int pukey,int prkey)
	{
		this.nleft=nl;
		this.nright=nr;
                this.nup=nup;
                mf=new modfunction();
                this.ids=nl.getids()+":"+nr.getids();
		this.pukey=pukey;
		this.prkey=prkey;
                depth=0;
	}
        public String getids()
        {
            return ids;
        }
        public void setids(String ids)
        {
            this.ids=ids;
        }
        public int getprikey()
        {
            return prkey;
        }
        public void setprikey(int prkey)
        {
            this.prkey=prkey;
        }
        public int getpubkey()
        {
            return pukey;
        }
        public void setpubkey(int pukey)
        {
            this.pukey=pukey;
        }
        public String toString() 
        {
            return ids;
        }
}

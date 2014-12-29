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
public class tree {
        node root,n1,n2,ntemp;
        static node nshallow;
	Vector vmembers;
	modfunction mf;
	queue q;
        String cmd;
        send s;
        nodecache  nc;
        int exp;
	public tree(Vector v,send s,nodecache nc)
	{
		node temp;
                mf=new modfunction();
		q=new queue(5,3);
		this.vmembers=(Vector)v.clone();
                this.s=s;
                this.nc=nc;
		Iterator itr=vmembers.iterator();
                exp=0;
		while(itr.hasNext())
		{
			temp=new node(((member)itr.next()));
			q.enqueue(temp);
		}
               
	}
        public void setmembers(Vector v)
        {
            this.vmembers=v;
        }
        public int  getgrpkey()
        {
            return root.getprikey();
        }
        public void setgrpkey(int key)     
        {
            root.setprikey(key);
        }
	public int getnodekeyvalue(int prkey,int pukey)
	{
                 exp++;
		return mf.getmodval(pukey, prkey);
	}
        public int getexponent()
        {
            return exp;
        }
        public void setexponent(int exp)
        {
            this.exp=exp;
        }
        public node getshallowest()
        {
            return nshallow;
        }
        public node getroot()
        {
            return root;
        }
	public void construct()
	{
           nodedata tmp;  
		while(q.size()>1)
		{  
			n1=(node)q.dequeue();
			n2=(node)q.dequeue();
                        if(n1.getprikey()==0&&n2.getprikey()==0)
                        {
                         tmp=nc.search(n1.getids()+":"+n2.getids());
                         if(tmp!=null)
                         {
                            ntemp=new node(n1,n2,null,tmp.getkey());
                            n1.nup=ntemp;
                            n2.nup=ntemp;
			    q.enqueue(ntemp);
                         }
                         else
                           System.out.println("ERROR NODE NOT FOUND:"+n1.getids()+":"+n2.getids());
                        }
                        else if(n1.getprikey()!=0)
                        {
                            ntemp=new node(n1,n2,null,getnodekeyvalue(n1.getprikey(),n2.getpubkey()));
                            n1.nup=ntemp;
                            n2.nup=ntemp;
                            q.enqueue(ntemp);
                        }
                        else if(n2.getprikey()!=0)
                        {
                            ntemp=new node(n1,n2,null,getnodekeyvalue(n2.getprikey(),n1.getpubkey()));
                            n1.nup=ntemp;
                            n2.nup=ntemp;
                            q.enqueue(ntemp);
                        }
                        else
                           System.out.println("ERROR NODE NOT FOUND:"+ntemp);
                }  
           if(q.size()>=1)
            root=(node)q.dequeue();
           else
             root=null;   
        }  
        public void setdepths(node root,int count,node nprev)
        {   
          if(root==null)  
              return;
          root.depth=count;
          if(root.depth<=nprev.depth&&root.nleft==null&&root.nright==null)
              nshallow=root;
          if(root.nleft!=null)
              setdepths(root.nleft,count++,root);
          if(root.nright!=null)
              setdepths(root.nright,count++,root);             
        }
        public void remerge(Object on[])
        {
            q.enqueue(root);
            for(int i=0;i<on.length;i++)
            {
                vmembers.add(on[i]);
                q.enqueue(new node((member)on[i]));
            }
            construct();
        }
        public void merge(node nnew)
        {
            if(nnew==null)
                return;
            nodedata tmp;
            node rcopy=root;
            setdepths(rcopy,0,rcopy);
                  if(nshallow.getprikey()!=0)
                  {
                   ntemp=new node(nshallow,nnew,nshallow.nup,getnodekeyvalue(nshallow.getprikey(),nnew.getpubkey()));
                   nshallow.nup=ntemp;
                   nnew.nup=ntemp;          
                  }
                  else if(nnew.getprikey()!=0)
                  {
                   ntemp=new node(nshallow,nnew,nshallow.nup,getnodekeyvalue(nnew.getprikey(),nshallow.getpubkey()));
                   nshallow.nup=ntemp;
                   nnew.nup=ntemp;
                  }
                  else if(nshallow.getprikey()==0&&nnew.getprikey()==0)
                  {
                         tmp=nc.search(nshallow.getids()+":"+nnew.getids());
                         if(tmp!=null)
                         {
                            ntemp=new node(nshallow,nnew,nshallow.nup,tmp.getkey());
                            nshallow.nup=ntemp;
                            nnew.nup=ntemp;
                         }
                          else
                           System.out.println("ERROR NODE NOT FOUND:"+nshallow.getids()+":"+nnew.getids());
                  }
                  else
                     System.out.println("ERROR NODE NOT FOUND:"+ntemp);
                  while(ntemp.nup!=null)
                  {
                   ntemp=ntemp.nup;
                   n1=ntemp.nleft;
                   n2=ntemp.nright;    
                   if(n1.getprikey()!=0)
                    ntemp.setprikey(getnodekeyvalue(n1.getprikey(),n2.getpubkey()));
                   else if(n2.getprikey()!=0)
                    ntemp.setprikey(getnodekeyvalue(n2.getprikey(),n1.getpubkey())); 
                   else if(n1.getprikey()==0&&n2.getprikey()==0)
                   {
                         tmp=nc.search(n1.getids()+":"+n2.getids());
                         if(tmp!=null)
                             ntemp.setprikey(tmp.getkey()); 
                         else
                            System.out.println("ERROR NODE NOT FOUND:"+n1.getids()+":"+n2.getids());
                   }
                   else
                     System.out.println("ERROR NODE NOT FOUND:"+ntemp);
                  }
           root=ntemp;
        }
        
 }

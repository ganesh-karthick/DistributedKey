/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package distributedkey;
import java.io.IOException;
import java.net.*;
import java.util.*;
import javax.swing.*;
/**
 *
 * @author Ganesh Karthick.R
 */
public class receive extends Thread {
 DatagramSocket ds;
 int key=31101;
 final int groupflag=1,memberflag=2,strflag=3;
 JTextArea  jtxtahist;
 JTextField jtxtgdkey;
 JList jlstonmembers,jlstmd,jlstmg;
 JComboBox jcmbgroupsd,jcmbgroupsg;
 Vector onmembers;
 member me,temp;
 groups grps;
  RC4 rc4;
 byte buffer[];
 String msg,cmd,tstr,mcopy;
 StringTokenizer st;
 nodecache nc;
 modfunction mf;
 send s;
 ftpwindow ftp;
 public receive(DatagramSocket ds,JTextArea jtxtahist,JList jlstonmembers,JComboBox jcmbgroupsd,JComboBox jcmbgroupsg,JList jlstmd,JList jlstmg,JTextField jtxtgdkey,member me,groups grps,nodecache nc,send s)
 {
  this.ds=ds;
  this.jtxtahist=jtxtahist;
  this.jlstonmembers=jlstonmembers;
  this.me=me;  
  mf=new modfunction();
  this.jcmbgroupsd=jcmbgroupsd;
  this.jcmbgroupsg=jcmbgroupsg;
  this.grps=grps;
  rc4=new RC4(key);
  this.jlstmd=jlstmd;
  this.jlstmg=jlstmg;
  this.jtxtgdkey=jtxtgdkey;
  this.nc=nc;
  onmembers=new Vector(5,3);
  jlstonmembers.setListData(onmembers);
  ftp=new ftpwindow(false,null,me,s,onmembers.toArray(),"",0);
  buffer=new byte[1024];
  this.s=s;
 }
 public boolean isalreadythere(Vector v,String s,int type) throws IOException
 {
     Iterator itr=v.iterator();
     String stmp;
     member mtmp;
     group gtmp;
     boolean flag=false;
     switch(type)
     {
         case groupflag:    while(itr.hasNext())
	                    {
	                      gtmp=(group)itr.next();
	                      if(gtmp.getname().equalsIgnoreCase(s))
	                        flag = true;
	                    } 
                            break;
        case memberflag:    while(itr.hasNext())
	                    {
	                      mtmp=(member)itr.next();
	                      if(mtmp.getid().equalsIgnoreCase(s))
	                        flag=true;
	                    }
                            break;                    
        case strflag:    while(itr.hasNext())
	                    {
	                      stmp=(String)itr.next();
	                      if(stmp.equalsIgnoreCase(s))
	                        flag = true;
	                    } 
                            break;  
         default:
                 flag = false;
                 break;
             
     }
	  
    return flag;
 }
 public Vector getallitems(JComboBox jcmb)
 {
     Vector vtmp=new Vector(jcmb.getItemCount());
     for(int i=0;i<jcmb.getItemCount();i++)
         vtmp.add(jcmb.getItemAt(i));
     return vtmp;
 }
 public DatagramPacket getpacket(String msg,InetAddress ip,int pt,boolean isencrypt)
    {
       if(isencrypt) 
        return new DatagramPacket(rc4.encrypt(msg).getBytes(),msg.getBytes().length,ip,pt);
       else
        return new DatagramPacket(msg.getBytes(),msg.getBytes().length,ip,pt); 
    }
 public String[] getstringitems(Object obj[])  
{
    String items[]=new String[obj.length];
    for(int i=0;i<obj.length;i++)
        items[i]=obj[i].toString();
    return items;
}
 public Object[] getonlinemembers()
 {
     return onmembers.toArray();
 }
 public byte[] concat(byte b1[],byte b2[])
 {
     int i;
     byte buf[]=new byte[b1.length+b2.length];
     for(i=0;i<b1.length;i++)
         buf[i]=b1[i];
     for(i=b1.length;i<b1.length+b2.length;i++)
         buf[i]=b2[i-b1.length];
     return buf;
 }
    //@Override
 synchronized public void run() 
 {
    try{
        DatagramPacket p;
        nodedata nd,ndr;
        String ids,stmp;
        jtxtahist.setText("RECENT MESSAGES"); 
        while(true)
        {
            try{
                
         p=new DatagramPacket(buffer,buffer.length);
         try{
          if(ds.isClosed())
          {
             msg="USER HAS LOGGED OFF...!!!";
             jtxtahist.append("\n"+msg);
             return;
          }
          ds.receive(p);
         }
         catch(Exception ex)
         {
             
         }
         msg=new String(p.getData(),0,p.getLength());
         st=new StringTokenizer(msg,":");
         if(msg.startsWith("HELLO:"))  //HELLO
         {
          st.nextToken();
          String toip[];
          temp=new member(st.nextToken()); //onmembers has string ids in it
          Object on[];
          nodedata ntmp;
          if(isalreadythere(onmembers,temp.getid(),strflag)==false)
          {   
            System.out.println(me.getprikey());
            onmembers.add(temp.getid());
            ids=temp.getid()+":"+me.getid(); //normal
            int keyg=mf.getmodval(temp.getpubkey(),me.getprikey());
            nd=new nodedata(ids,keyg);
            nc.add(nd);
            jlstonmembers.setListData(onmembers);
            cmd="HELLO:"+me.getid();
            ds.send(getpacket(cmd,temp.getip(),temp.getport(),false));
            on=nc.getvector();
            rc4=new RC4(keyg);
            toip=getstringitems(onmembers.toArray());
            for(int i=0;i<on.length;i++)
            {
             ntmp=(nodedata)on[i];  
             stmp=ntmp.getids()+":"+String.valueOf(ntmp.getkey());
             for(int j=0;j<toip.length;j++)
             {
                 nodedata nkey=nc.search(me.getid()+":"+toip[j]);
                 rc4.setkey(nkey.getkey());
                 byte bbuf[]=concat(("PUBKEY:"+me.getid()+":").getBytes(),rc4.encryptbyte(stmp));
                 s.sendmsg(bbuf,toip[j],false);
             }
            }
          }  
          grps.startkeydaemonforid(me.getid());
          on=s.pd.getmsgforid(temp.getid());
          if(on.length!=0)
          {
             offlinemsg tmp;
             for(int i=0;i<on.length;i++) 
             {
               tmp=(offlinemsg)on[i];
               System.out.print(tmp.getmsg());
               s.sendmsg(tmp.getmsg(),temp.getid(),false);
             }
          }
          s.pd.delmsgforid(temp.getid());
          jtxtahist.append("\n"+msg); 
         }
         else if(msg.startsWith("CRTEGRP:")) //CREATE GROUP
         {
          String sg,sd;   
          group g;
          st.nextToken();
          tstr=st.nextToken(); 
          if(!isalreadythere(grps.getvector(),tstr,groupflag))
          {  
           member owner=new member(st.nextToken());   
           String gname=tstr+"@"+owner.gethost()+"#"+owner.getip().getHostAddress()+"#"+String.valueOf(owner.getport());
           grps.addgroup(gname);   
           g=grps.getgroup(gname);
           g.addmember(owner);
           grps.setgroup(gname, g);
           if(!isalreadythere(getallitems(jcmbgroupsd),gname,strflag)) 
            jcmbgroupsd.addItem(gname);
           if(!isalreadythere(getallitems(jcmbgroupsg),gname,strflag))
            jcmbgroupsg.addItem(gname);
           sd=(String)jcmbgroupsd.getSelectedItem();
           sg=(String)jcmbgroupsg.getSelectedItem();
           if(sd.equalsIgnoreCase(gname))
            jlstmd.setListData(g.getvector());
           if(sg.equalsIgnoreCase(gname))
            jlstmg.setListData(g.getvector());
          }
          jtxtahist.append("\n"+msg); 
         }
         else if(msg.startsWith("JOIN:")) //JOIN
         {
             String mid,gid;
             st.nextToken();
             gid=st.nextToken();
             mid=st.nextToken();
             if(!mid.equalsIgnoreCase(me.getid()))
             {
              group g=grps.getgroup(gid);
              if(g==null)
              {
                  cmd="REFGRP:"+me.getid();
                  s.sendmsg(cmd, false);
                  return;
              }
              if(!isalreadythere(g.getvector(),mid,memberflag))
              {
               g.addmember(mid);
               grps.setgroup(gid, g);
              }
              if(g.ismember(me.getid()))
              {
                  grps.startkeydaemonforid(g.getid());
              }
             }
             jtxtahist.append("\n"+msg); 
             
         }
         else if(msg.startsWith("LEAVE:")) //LEAVE
         {
             String mid,gid;
             st.nextToken();
             gid=st.nextToken();
             mid=st.nextToken();
             if(!mid.equalsIgnoreCase(me.getid()))
             {
              group g=grps.getgroup(gid);
              if(g==null)
                  return;
              if(isalreadythere(g.getvector(),mid,memberflag))
              {
                 g.removemember(mid); 
                 grps.setgroup(gid,g);
              }
              if(g.ismember(me.getid()))
              {
                  grps.startkeydaemonforid(g.getid());
              }
             }
             jtxtahist.append("\n"+msg); 
         }
         else if(msg.startsWith("EXIT:"))
         {
             st.nextToken();
             temp=new member(st.nextToken());
             if(isalreadythere(onmembers,temp.getid(),strflag))
             {
                 onmembers.remove(temp.getid());  
                 jlstonmembers.setListData(onmembers);
             }
             jtxtahist.append("\n"+msg); 
             
         }
         else if(msg.startsWith("ONMBRMSG:")) //ONMBRMSG:(t/f):msg 
         {
          st.nextToken();
          String off=st.nextToken();
          if(off.equals("T"))
           msg=st.nextToken()+":"+st.nextToken()+":"+st.nextToken()+":"+st.nextToken();
          else
            msg=st.nextToken();  
          jtxtahist.append("\n"+msg);
         }
         else if(msg.startsWith("GRPMSG:")) //GRPMSG:(t/f):msg 
         {
          st.nextToken();
          group g; //HH:MM:SS
          String encmsg="",off=st.nextToken(),gname; //"T" is offlinemsg,"F" is onlinemsg
          if(off.equals("T")) //GETTING ACTUAL GRP NAME FOR OFFLINE MSG FORMAT:GNAME(TIME)
          {
              gname=st.nextToken()+":"+st.nextToken()+":"+st.nextToken()+":"+st.nextToken();
              g=grps.getgroup(gname.substring(0,gname.indexOf("(")));
          }
          else
          {
           gname=st.nextToken();   
           g=grps.getgroup(gname); 
          }
          int keyg=g.getkey();
          System.out.println(keyg);
          rc4=new RC4(keyg);
          encmsg=st.nextToken();
           while(st.hasMoreTokens())
               encmsg+=(":"+st.nextToken());
          String tmsg=rc4.decrypt(encmsg);
          jtxtahist.append("\n"+"FROM:"+gname+"\nMESSAGE:"+tmsg); 
         }
         else if(msg.startsWith("REFGRP:")) //REFGRP:REQUESTOR'S ID
         {
             st.nextToken();
             member mtmp,dest=new member(st.nextToken());
             group gtmp;
             Object ombrs[],ogrps[]=grps.retrievegroupsforid(me.getid());
             int i,j;
             for(i=0;i<ogrps.length;i++)
             {
                 gtmp=(group)ogrps[i];
                 cmd="CRTEGRP:"+gtmp.getname()+":"+me.getid();
                 s.sendmsg(cmd,dest.getid(),false);
                 ombrs=gtmp.getallmembers();
                 for(j=0;j<ombrs.length;j++)
                 {
                     mtmp=(member)ombrs[j];
                     cmd="JOIN:"+gtmp.getid()+":"+mtmp.getid();
                     s.sendmsg(cmd,dest.getid(),false);
                 }  
             }
         }
         else if(msg.startsWith("GKEY:")) //GKEY:GROUP NAME:KEY
         {
             String sg,sd;  
             st.nextToken();
             String gname=st.nextToken();
             int keyg=Integer.valueOf(st.nextToken());
             /*group g=grps.getgroup(gname);
             g.setkey(keyg);
             grps.setgroup(gname, g);
             sd=(String)jcmbgroupsd.getSelectedItem();
             if(sd!=null)
               if(sd.equalsIgnoreCase(gname))
                 jtxtgdkey.setText(String.valueOf(keyg)); */
             //jtxtahist.append("\n"+msg);  
         }
         else if(msg.startsWith("PUBKEY:")) //PUBKEY:RIGHT NODE:LEFT NODE:KEY
         {
           st.nextToken();
           String t1,t2,from,encmsg;
           char cbuf[]=msg.toCharArray();
           byte cipher[],buf[]=p.getData();
           from=st.nextToken();
           nodedata ntmp=nc.search(from+":"+me.getid());
           int keyg=ntmp.getkey();
           rc4=new RC4(keyg);
           int i,j=0,k=0;
           for(i=0;i<cbuf.length;i++)
           {
               if(cbuf[i]==':')
                   j++;
               if(j==2)
               {
                   k=i+1;
                   break;
               }
           }
           cipher=new byte[p.getLength()-k];
           for(i=k;i<cbuf.length;i++)
               cipher[i-k]=buf[i];
           encmsg=rc4.decrypt(cipher);
           st=new StringTokenizer(encmsg,":");
           t1=st.nextToken();
           t2=st.nextToken();
           ids=t1+":"+t2;
           keyg=Integer.parseInt(st.nextToken());
           nc.add(new nodedata(ids,keyg));
           jtxtahist.append("\nPUBKEY:"+from+":"+encmsg);    
         }
         else if(msg.startsWith("FTP:")) //FTP:from id:file name:file size
         {
             try{
             jtxtahist.append("\n"+msg); 
             st.nextToken();
             String mfrom=st.nextToken();
             String fname=st.nextToken()+":"+st.nextToken();
             long filesize=Long.parseLong(st.nextToken());
             ftp.setdetails(new member(mfrom), fname, filesize);
             ftp.setVisible(true);
             }
             catch(Exception e)
             {
              e.printStackTrace();
              System.out.println(e.getMessage());
              ftp.setVisible(false);
              ftp=new ftpwindow(false,null,me,s,onmembers.toArray(),"",0);
             }
         }
         else if(msg.startsWith("DELGRP:"))
         {
             st.nextToken();
             System.out.println("cool");
             String tmp,owner=st.nextToken();
             String gid=st.nextToken();
             group g=grps.getgroup(gid);
             if(g==null)
                 return;
            // if(owner.equalsIgnoreCase(g.getowner().getid()))
             {
                 grps.stopkeydaemonforid(gid);
                 for(int i=0;i<jcmbgroupsd.getItemCount();i++) 
                 {
                     tmp=(String)jcmbgroupsd.getItemAt(i);
                     if(tmp.equalsIgnoreCase(gid))
                     {
                         jcmbgroupsd.removeItemAt(i);
                     }
                 }
                 for(int i=0;i<jcmbgroupsg.getItemCount();i++) 
                 {
                     tmp=(String)jcmbgroupsg.getItemAt(i);
                     if(tmp.equalsIgnoreCase(gid))
                     {
                         jcmbgroupsg.removeItemAt(i);
                     }
                 }
                 grps.removegroup(gid);
                 jtxtahist.append("\n"+msg); 
             }
             
         }
         else
          jtxtahist.append("\n"+msg);     
        }
        catch(Exception e)
         {
          e.printStackTrace();
          System.out.println(e.getMessage());
         }  
       }
    }
    catch(Exception e)
    {
        e.printStackTrace();
        System.out.println(e.getMessage());
    }
    
 }
}

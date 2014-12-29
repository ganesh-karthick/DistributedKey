/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package distributedkey;

/**
 *
 * @author Ganesh Karthick.R
 */
class RC4 extends symmetriccipher
{	
    int s[],t[];
	public RC4(long key)
	{
		this.key=(long)key;
		this.keylen=8;
		s=new int[257];
		t=new int[257];
                isstring=false;
	}	
	public RC4(int key)
	{
		this.keylen=4;
		this.key=key;
		s=new int[257];
		t=new int[257];
                isstring=false;
	}
        public RC4()
	{
		this.keylen=4;
		this.key=31101;
		s=new int[257];
		t=new int[257];
                isstring=false;
	}
	public RC4(String key)
	{
		this.keylen=key.length();
		this.skey=key;
		isstring=true;
		s=new int[257];
		t=new int[257];
	}
	public void setup(String msg)
	{
	 try
		{	 
		 int i,j=0,ts;
		 byte tk[]=null;	
		 if(isstring)
		  tk=skey.getBytes();
		 long temp=key;
		 this.bufencrypt=msg;
		 for(i=0;i<256;i++)            //Intialisation of S
		 {
			s[i]=i;
			if(isstring) 
			 t[i]=tk[i%keylen];
			else
			{
				t[i]=(int)(temp>>(i%keylen))&0x00000000000000FF;
				temp=key;
			}			    
		 }
		 for(i=0;i<256;i++)             //Permutation 
		 {
			j=(j+s[i]+t[i])%256;
			ts=s[i];s[i]=s[j];s[j]=ts;
		 }
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}		
	}
	public String encrypt(String m)
	{
		byte msg[]=m.getBytes();
	    try{
	     int i=256,j=0,p=0,k=0,ts=0;
		 setup(new String(msg));
		 while(p<msg.length)
		 {
			i=(i+1)%256;
			j=(j+s[i])%256;
			ts=s[i];s[i]=s[j];s[j]=ts;
			k=(char)s[(s[i]+s[j])%256];
                        msg[p]=(byte) (((byte)k)-msg[p]);
			p+=1;
		 }
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}	
		return new String(msg,0,msg.length);	
	}
        public byte[] encryptbyte(String m)
	{
		byte msg[]=m.getBytes();
	    try{
	     int i=256,j=0,p=0,k=0,ts=0;
		 setup(new String(msg));
		 while(p<msg.length)
		 {
			i=(i+1)%256;
			j=(j+s[i])%256;
			ts=s[i];s[i]=s[j];s[j]=ts;
			k=(char)s[(s[i]+s[j])%256];
                        msg[p]=(byte) (((byte)k)-msg[p]);
			p+=1;
		 }
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}	
		return msg;	
	}
    public String decrypt(String m)
	{
	    byte msg[]=m.getBytes();
	    try{
		 setup(new String(msg));
		 int i=256,j=0,p=0,k=0,ts=0;
		 while(p<msg.length)
		 {
			i=(i+1)%256;
			j=(j+s[i])%256;
			ts=s[i];s[i]=s[j];s[j]=ts;
			k=s[(s[i]+s[j])%256];
		        msg[p]=(byte) (((byte)k) - msg[p]);
			p+=1;
		 }
		}
		catch(Exception e) 
		{
	    	e.printStackTrace();
		}	
		return  new String(msg,0,msg.length);
	}
    public String decrypt(byte[] m)
	{
	    byte msg[]=m;
	    try{
		 setup(new String(msg));
		 int i=256,j=0,p=0,k=0,ts=0;
		 while(p<msg.length)
		 {
			i=(i+1)%256;
			j=(j+s[i])%256;
			ts=s[i];s[i]=s[j];s[j]=ts;
			k=s[(s[i]+s[j])%256];
		        msg[p]=(byte) (((byte)k) - msg[p]);
			p+=1;
		 }
		}
		catch(Exception e) 
		{
	    	e.printStackTrace();
		}	
		return  new String(msg,0,msg.length);
	}
}
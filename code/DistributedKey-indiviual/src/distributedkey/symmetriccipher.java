/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package distributedkey;

/**
 *
 * @author Ganesh Karthick.R
 */
public abstract class symmetriccipher
{
	String bufencrypt,bufdecrypt,skey;
	long key;
	int keylen;
	boolean isstring;
	abstract public String encrypt(String m);
	abstract public String decrypt(String m);
	public long getkey()
	{
		return key;
	}
	public void setkey(int key)
	{
	 	this.key=(long)key;
	}
	public void setkey(long key)
	{
	 	this.key=key;
	}
	public void setkey(String key)
	{
	 	this.skey=key;
	}
}

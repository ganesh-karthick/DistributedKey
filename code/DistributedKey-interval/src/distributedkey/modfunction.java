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
public class modfunction implements Serializable{
    int alpha,prime;
    public modfunction()
    {
        alpha =2;
        prime=67;
    }
    public modfunction(int alpha,int prime)
    {
        this.alpha=alpha;
        this.prime=prime;
    }
    public int getalpha()
    {
        return alpha;
    }
    public int getprime()
    {
        return prime;
    }
     int getmodval(int a,int b,int n)
	{
		long d=1;
		for(int i=32;i>=0;i--)
		{
			d=((long)(d*d))%n;
			if(((b>>i)&1)==1)
			  d=((long)(d*a))%n;			  
		}
		return (int)d;
		
	}
     int getmodval(int a,int b)
	{
		long d=1;
		for(int i=32;i>=0;i--)
		{
			d=((long)(d*d))%prime;
			if(((b>>i)&1)==1)
			  d=((long)(d*a))%prime;			  
		}
		return (int)d;
		
	}
       int getmodval(int b)
	{
		long d=1;
		for(int i=32;i>=0;i--)
		{
			d=((long)(d*d))%prime;
			if(((b>>i)&1)==1)
			  d=((long)(d*alpha))%prime;			  
		}
		return (int)d;
		
	}
    

}

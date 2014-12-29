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
public class queue{
 
    Vector v; 
    int len;
        public queue(int size)
        {
            v=new Vector(size);
        }
        public queue(int size,int growth)
        {
            v=new Vector(size,growth);
            len=v.size();
        }
	public void enqueue (Object element)
	{
		v.addElement (element);
                len=v.size();
	}	

	public Object dequeue ()
	{	       
		if (v.size() == 0)
			throw new NullPointerException();
		Object obj = v.elementAt(0);	
		v.removeElementAt (0);
                len = v.size();
		return obj;
	}
        public Object peek(int no)
        {
            return v.elementAt(no);
        }
        public int size()
        {
             len = v.size();
             return len;
        }
        public boolean isempty()
        {
            if(len==0)
                return true;
            else
                return false;
        }
        
}

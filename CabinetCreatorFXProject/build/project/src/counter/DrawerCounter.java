/**
 * 
 */
package counter;

/**
 * @author antho
 *
 */
public class DrawerCounter 
{
	public int index = 0;
	public DrawerCounter(int index)
	{
		this.index = index;
	}
	public void incrementCounter()
	{
		this.index++;
	}
	/**
	 * Decrement counter and throws exception for going negative
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public void decrementCounter() throws ArrayIndexOutOfBoundsException
	{
		if(this.index<=0)
		{
			throw new ArrayIndexOutOfBoundsException();
		}
		this.index--;
	}
	
	
}

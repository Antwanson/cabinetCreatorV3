/**
 * 
 */
package woodWorking;

/**
 * @author antho
 *
 */
public class WoodObject {
	protected double width = 100;
	protected double height = 100;
	protected double depth = 100;
	public WoodObject(double w, double h, double d)
	{
		this.width = w;
		this.height = h;
		this.depth = d;
	}
	public void setDimensions(double w, double h, double d)
	{
		this.width = w;
		this.height = h;
		this.depth = d;
	}
	public double getWidth()
	{
		return this.width;
	}
	public double getHeight()
	{
		return this.height;
	}
	public double getDepth()
	{
		return this.depth;
	}
	public String getText()
	{
		String dimentionsString = this.width + "," + this.height + "," + this.depth;
		return dimentionsString;
	}
	public Boolean isCabinetEmpty()
	{
		return true;
	}
}

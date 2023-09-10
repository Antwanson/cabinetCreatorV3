/**
 * 
 */
package woodWorking;
import java.util.ArrayList;

import cabinetError.InvalidCabinetException;

/**
 * @author antho
 *
 */
public class SingleCabinet extends WoodObject {

	/**
	 * @param w
	 * @param h
	 * @param d
	 */
	protected ArrayList<Drawer> cabinetDrawers = new ArrayList<>();
	protected double drawerWidthSpacing = 2;
	protected double drawerHeightSpacing = 2;
	protected double drawerDepthSpacing = 2;
	protected double minDrawerWidth = 2;
	protected double minDrawerHeight = 2;
	protected double minDrawerDepth = 2;
	protected double minCabinetWidth = 6;
	protected double minCabinetHeight = 6;
	protected double minCabinetDepth = 6;
	
	public SingleCabinet(double w, double h, double d) 
	{
		
		super(w, h, d);
	}
	@Override
	public void setDimensions(double w, double h, double d) 
	{
		super.setDimensions(w, h, d);
	}
	public void setAndCheckDimensions(double w, double h, double d) throws InvalidCabinetException
	{
		double spaceVertical = drawerHeightSpacing;
		boolean isCorrect = true;
		for(int i=0; i<cabinetDrawers.size(); i++) 
		{
			if(cabinetDrawers.get(i).getWidth()>(w-(drawerWidthSpacing*2)))
			{
				isCorrect = false;
			}
			if(cabinetDrawers.get(i).getDepth()>(d-drawerDepthSpacing)) 
			{
				isCorrect = false;
			}
			spaceVertical += drawerHeightSpacing+cabinetDrawers.get(i).getHeight();
			
		}
		if(h<this.minCabinetHeight||
				w<this.minCabinetWidth||
				d<this.minCabinetDepth)
		{
			isCorrect = false;
		}
		if(!(spaceVertical<=h)) 
		{
			isCorrect = false;
		}
		if(isCorrect==false) 
		{
			throw new InvalidCabinetException("The cabinet dimensions entered are not correct");
		}
		super.setDimensions(w, h, d);
		
	}
	public void setAndCheckDimensions(double w, double h, double d, double newCabinetWidth, double newCabinetHeight, double newCabinetDepth, int index) throws InvalidCabinetException
	{
		double spaceVertical = drawerHeightSpacing;
		boolean isCorrect = true;
		for(int i=0; i<cabinetDrawers.size(); i++) 
		{
			if(i != index)
			{
				if(cabinetDrawers.get(i).getWidth()>(w-(drawerWidthSpacing*2)))
				{
					isCorrect = false;
				}
				if(cabinetDrawers.get(i).getDepth()>(d-drawerDepthSpacing)) 
				{
					isCorrect = false;
				}
			}
			else
			{
				if(newCabinetWidth>(w-(drawerWidthSpacing*2)))
				{
					isCorrect = false;
				}
				if(newCabinetDepth>(d-drawerDepthSpacing)) 
				{
					isCorrect = false;
				}
			}
			spaceVertical += drawerHeightSpacing+cabinetDrawers.get(i).getHeight();
			
			
		}
		spaceVertical+=(newCabinetHeight-cabinetDrawers.get(index).height);
		if(h<this.minCabinetHeight||
				w<this.minCabinetWidth||
				d<this.minCabinetDepth)
		{
			isCorrect = false;
		}
		if(!(spaceVertical<=h)) 
		{
			isCorrect = false;
		}
		if(isCorrect==false) 
		{
			System.out.print("I am dumb");
			throw new InvalidCabinetException("The cabinet dimensions entered are not correct");
		}
		super.setDimensions(w, h, d);
		
	}
	/**
	 * 
	 * Adds a new drawer to the cabinet
	 * @param w
	 * @param h
	 * @param d
	 */
	public void addDrawer(double w, double h, double d) throws InvalidCabinetException
	{
		if(canAddDrawer()==false)
		{
			throw new InvalidCabinetException("There is no space for this drawer");
		}
		cabinetDrawers.add(new Drawer(w,h,d));
		
	}
	public void addDrawer() throws InvalidCabinetException
	{
		if(canAddDrawer()==false)
		{
			throw new InvalidCabinetException("There is no space for this drawer");
		}
		cabinetDrawers.add(new Drawer(minDrawerWidth,minDrawerHeight,minDrawerDepth));
	}
	public void removeDrawer(int index)
	{
		cabinetDrawers.remove(index);
	}
	/**
	 * sets the dimensions of a drawer
	 * @param index
	 * @param w
	 * @param h
	 * @param d
	 */
	public void setDrawerDimensions(int index,double w, double h, double d) throws InvalidCabinetException
	{
		double spaceVertical = drawerHeightSpacing;
		boolean isCorrect = true;
		for(int i=0; i<cabinetDrawers.size(); i++) 
		{
			spaceVertical += drawerHeightSpacing+cabinetDrawers.get(i).getHeight();
		}
		if(w>(super.width-(drawerWidthSpacing*2)))
		{
			isCorrect = false;
		}
		if(d>(super.depth-drawerDepthSpacing)) 
		{
			isCorrect = false;
		}
		if(h<this.minDrawerHeight||
				w<this.minDrawerWidth||
				d<this.minDrawerDepth)
		{
			isCorrect = false;
		}
		if(!(spaceVertical+(h-cabinetDrawers.get(index).getHeight())<=super.height)) 
		{
			isCorrect = false;
		}
		if(isCorrect == false) 
		{
			throw new InvalidCabinetException("The drawer dimensions entered are not correct");
		}
		cabinetDrawers.get(index).setDimensions(w, h, d);
	}
	public void setDrawerSpacing(double width, double height, double depth)
	{
		this.drawerWidthSpacing = width;
		this.drawerHeightSpacing = height;
		this.drawerDepthSpacing = depth;
	}
	public double getDrawerWidth(int index) 
	{
		return cabinetDrawers.get(index).getWidth();
	}
	public double getDrawerHeight(int index) 
	{
		return cabinetDrawers.get(index).getHeight();
	}
	public double getDrawerDepth(int index) 
	{
		return cabinetDrawers.get(index).getDepth();
	}
	public double getDrawerSpacingWidth()
	{
		return this.drawerWidthSpacing;
	}
	public double getDrawerSpacingHeight()
	{
		return this.drawerHeightSpacing;
	}
	public double getDrawerSpacingDepth()
	{
		return this.drawerDepthSpacing;
	}
	public int getDrawerListSize()
	{
		return cabinetDrawers.size();
	}
	public double getDrawerYPosition(int index, double StageHeight)
	{
		double drawerPos = 0;
		double topPosition = (StageHeight/2) - (super.height/2) + drawerHeightSpacing;
		double drawerTopPosition = topPosition;
		for(int i = 0;i<index; i++)
		{
			drawerTopPosition += cabinetDrawers.get(i).getHeight() + drawerHeightSpacing;
		}
		drawerPos = drawerTopPosition + (cabinetDrawers.get(index).getHeight()/2);
		return drawerPos;
	}
	@Override
	public String getText()
	{
		String dimString = "Cabinet\n";
		dimString += super.getText() + "\nDrawers\n";
		for(int i = 0; i<cabinetDrawers.size(); i++)
		{
			dimString += cabinetDrawers.get(i).getText() + "\n";
		}
		return dimString;
	}
	public Boolean canAddDrawer()
	{
		boolean isCorrect = true;
		double spaceVertical = drawerHeightSpacing;
		for(int i=0; i<cabinetDrawers.size(); i++) 
		{
			spaceVertical += drawerHeightSpacing+cabinetDrawers.get(i).getHeight();
		}
		if(!(spaceVertical+this.drawerHeightSpacing+this.minDrawerHeight<=super.height)) 
		{
			isCorrect = false;
		}
		return isCorrect;
	}
	public Boolean checkDimensions()
	{
		double spaceVertical = drawerHeightSpacing;
		boolean isCorrect = true;
		for(int i=0; i<cabinetDrawers.size(); i++) 
		{
			if(cabinetDrawers.get(i).getWidth()>(super.width-(drawerWidthSpacing*2)))
			{
				isCorrect = false;
			}
			if(cabinetDrawers.get(i).getDepth()>(super.depth-drawerDepthSpacing)) 
			{
				isCorrect = false;
			}
			spaceVertical += drawerHeightSpacing+cabinetDrawers.get(i).getHeight();
			if(cabinetDrawers.get(i).getHeight()<this.minDrawerHeight||
					cabinetDrawers.get(i).getWidth()<this.minDrawerWidth||
					cabinetDrawers.get(i).getDepth()<this.minDrawerDepth)
			{
				isCorrect = false;
			}
		}
		if(!(spaceVertical<=super.height)) 
		{
			isCorrect = false;
		}
		//throw new Exception();
		return isCorrect;
	}
	@Override
	public Boolean isCabinetEmpty()
	{
		return cabinetDrawers.isEmpty();
	}
}

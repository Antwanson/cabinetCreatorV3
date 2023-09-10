/**
 * 
 */
package counter;

import java.io.File;

/**
 * @author antho
 *
 */
public class FileHolderObject {
	
	File projectFile = null;
	/**
	 * 
	 */
	public FileHolderObject() {
		
	}
	public void setFile(File file)
	{
		this.projectFile = file;
	}
	public File getFile()
	{
		return this.projectFile;
	}
}

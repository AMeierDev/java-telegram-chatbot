package de.bigamgamen.java.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class IOHelper {
	/**
	 * Searches for a resource in the application's classpath and the
	 * filesystem.
	 * <p>
	 * If the resource is found an {@link InputStream} is returned to read from
	 * the resource.<br>
	 * If no resource is found an {@link FileNotFoundException} is thrown.
	 * 
	 * @param relativePath
	 *            The relative path of the resource, e.g. 'res/pics/splash.png'
	 * @return An {@link InputStream} to read from the resource
	 * @throws IOException
	 *             if an IO-error occurs
	 * @throws FileNotFoundException
	 *             if the resource cannot be found
	 * @since 3.1
	 */
	public static InputStream findResource(String relativePath) throws IOException,
			FileNotFoundException
	{
		relativePath = relativePath.replace('\\','/');
		
		ClassLoader classLoader = IOHelper.class.getClassLoader();
		InputStream in = classLoader.getResourceAsStream(relativePath);
		if(in == null)
		{
			if(relativePath.startsWith("/"))
			{
				in = classLoader.getResourceAsStream(relativePath.substring(1));
			}
			else
			{
				in = classLoader.getResourceAsStream("/" + relativePath);
			}
		}
		if(in != null)
		{
			return in;
		}
		
		File f = new File(relativePath).getAbsoluteFile();
		if(f.exists())
		{
			return new FileInputStream(f);
		}
		
		String projectHome = System.getProperty("project.home",null);
		if(projectHome != null)
		{
			StringBuilder path = new StringBuilder();
			if(projectHome.endsWith("/"))
			{
				path.append(projectHome,0,projectHome.length() - 1);
			}
			else
			{
				path.append(projectHome);
			}
			if(!relativePath.startsWith("/"))
			{
				path.append("/");
			}
			path.append(relativePath);
			
			f = new File(path.toString()).getAbsoluteFile();
			if(f.exists())
			{
				return new FileInputStream(f);
			}
		}
		
		throw new FileNotFoundException(relativePath);
	}
}

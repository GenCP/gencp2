package view.bean.controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Copiar {
	
	public Copiar(String origen, String destino) throws IOException {
		File arcOrigen = new File(origen);
		File arcDestino = new File(destino);
		
		if (!arcOrigen.exists())
		      throw new IOException("Error al copiar: " + "No Existe archivo: "
		          + arcOrigen);
		    if (!arcOrigen.isFile())
		      throw new IOException("Error al copiar: " + "Es un Directorio "
		          + arcOrigen);
		    if (!arcOrigen.canRead())
		      throw new IOException("FileCopy: " + "source file is unreadable: "
		          + arcOrigen);

		    if (arcDestino.isDirectory())
		      arcDestino = new File(arcDestino, arcOrigen.getName());

		    if (arcDestino.exists()) {
		      if (!arcDestino.canWrite())
		        throw new IOException("Error al Copiar Archivo "
		            + "No se puede sobreescribir el archivo: " + arcDestino);
		    } 
		    else 
		    {
		      String parent = arcDestino.getParent();
		      if (parent == null)
		        parent = System.getProperty("user.dir");
		      File dir = new File(parent);
		      if (!dir.exists())
		        throw new IOException("FileCopy: "
		            + "destination directory doesn't exist: " + parent);
		      if (dir.isFile())
		        throw new IOException("FileCopy: "
		            + "destination is not a directory: " + parent);
		      if (!dir.canWrite())
		        throw new IOException("FileCopy: "
		            + "destination directory is unwriteable: " + parent);
		    }

		    FileInputStream from = null;
		    FileOutputStream to = null;
		    try {
		      from = new FileInputStream(arcOrigen);
		      to = new FileOutputStream(arcDestino);
		      byte[] buffer = new byte[4096];
		      int bytesRead;

		      while ((bytesRead = from.read(buffer)) != -1)
		        to.write(buffer, 0, bytesRead); // write
		    } 
		    finally {
		      
		    	if (from != null)
		    		try {
		    			from.close();
		    		} 
		    		catch (IOException e) 
		    		{
		    			e.printStackTrace();
		    		}
		    	if (to != null)
		    		try {
		    			to.close();
		    		} 
		    	catch (IOException e) {
		    			e.printStackTrace();
		    		}
		    }
	}
}



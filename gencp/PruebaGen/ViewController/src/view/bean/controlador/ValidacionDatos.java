package view.bean.controlador;

public class ValidacionDatos {
@SuppressWarnings("finally")
public static boolean isValorCorrecto(String valor, String tda){
		boolean flag = false;
		try{
				if(tda.contains("boolean") 
						&& !valor.trim().equals("true") 
						&& !valor.trim().equals("false"))
				{throw new Exception();	}
				else if(tda.contains("char") && valor.trim().length()>1)
				{throw new Exception();	}
				else if(tda.contains("short")) 
				{Short.parseShort((valor.trim()));	}
				else if(tda.contains("byte"))
				{ Byte.parseByte((valor.trim()));}
				else if(tda.contains("int"))
				{Integer.parseInt((valor.trim()));	}
				else if(tda.contains("long"))
				{ Long.parseLong((valor.trim()));}
				else if(tda.contains("float"))
				{Float.parseFloat((valor.trim()));}
				else if(tda.contains("double"))
				{Double.parseDouble((valor.trim()));}
		}
		catch(Exception e){
			flag=true;
		}
		finally{
			return flag;	
		}
	}
}

package view.bean.controlador;


import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;


public class MiTabla extends AbstractTableModel {
	

	private static final long serialVersionUID = -7510951455306844082L;
	
	
	Object[][] datos ;

	
	
	class TableListener implements TableModelListener{
		public void tableChanged(TableModelEvent tme){	}
	}
		
		
		public MiTabla(Object[][] _datos){
			addTableModelListener( new TableListener());
			datos = _datos;
		}
		
		
		public int getColumnCount(){return datos[0].length;}
		
		public int getRowCount(){return datos.length;}
		
		
		public Object getValueAt(int fila, int col){
			return datos[fila][col];
		}
		
		@Override
		public void setValueAt(Object valor, int fila, int col){
			if(isCellEditable(fila, col)){//nuevo
				datos[fila][col]=valor;
				fireTableDataChanged();
			}
		}
		
		@Override
		public boolean isCellEditable(int fila, int col){
			if(datos[fila][1].toString().equals("void")
					|| col == 0 || col == 1)
				return	false;
			else
				return true;
		}
				@Override
				@SuppressWarnings("unchecked")
		public Class getColumnClass(int colIndex){
			if(colIndex==3)
				return Boolean.class;
			else
				return Object.class;
		}
	
	}



package edu.uf.interactable.invitrogrowth;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Field {

	private static Field field;
	
	public static final double X_BIN = 60000;
	public static final double Y_BIN = 60000;
	public static final double Z_BIN = 5000;
	
	private PrintWriter pw;
	static int objects = 0;
	
    private ArrayList<Afumigatus> list;

    private Field() { 
    	list = new ArrayList<>();
    }
    
    public static Field getField() {
    	if(field == null) {
    		field = new Field();
    	}
    	return field;
    }
    
    public void next(int i, int k) {
    	//ArrayList<Afumigatus> clist = (ArrayList<Afumigatus>) this.list.clone();
    	int j = 0;
    	objects = 0;
    	int size = this.list.size();
    	for(int ii = 0; ii < size; ii++) {
    		Afumigatus cell = this.list.get(j);
    		if(!cell.isGrowable()) {
    			list.remove(j);
    			j--;
    		}else {
    			cell.grow(this);
    			cell.updateStatus();
    		}
    		j++;
    		objects++;
    	}

    }

    public void setCell(Afumigatus cell) {
    	list.add(cell);
    }
	
}

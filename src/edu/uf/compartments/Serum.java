package edu.uf.compartments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.uf.control.Exec;
import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.Cell;
import edu.uf.interactable.InfectiousAgent;
import edu.uf.interactable.Interactable;
import edu.uf.interactable.Liver;
import edu.uf.interactable.Molecule;
import edu.uf.interactable.Phagocyte;
import edu.uf.time.Clock;

public class Serum implements Compartment{


	private static Serum lung = null;
	
	protected List<Interactable> agents = new ArrayList<>();
	
	private Serum() {}
	
	
	
	public static Serum getLung() {
		if(lung == null) {
			lung = new Serum();
		}
		return lung;
	}
	
	@Override
	public void next() {
    	Clock.updade();
    	this.interact();
    	this.update();
    }
	
	private void interact() {
    	
        Collections.shuffle(agents, new Random());
        int size = agents.size();
        for(int i = 0; i < size; i++) {
        	for(int j = 0; i < size; i++) {
        		agents.get(i).interact(agents.get(j), 0, -1, -1);
        	}
        }
    }
	
	private void update() {
		for(Interactable agent : agents) {
			if(agent instanceof Molecule) {
				((Molecule) agent).degrade();
			}else if(agent instanceof Cell) {
				((Cell) agent).updateStatus();
			}
		}
	}
    
    public void setAgent(Interactable agent) {
    	agents.add(agent); 
    }

	
}

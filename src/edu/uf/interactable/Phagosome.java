package edu.uf.interactable;

import java.util.*;

public class Phagosome {
	
	public HashMap<Integer, Afumigatus> agents;
	public boolean hasConidia;

	public Phagosome() {
        this.agents = new HashMap<>();
        this.hasConidia = false;
	}

    public void setAgent(Afumigatus agent) {
        this.agents.put(agent.getId(), agent);
    } 

    public Cell removeAgent(int id) {
        return this.agents.remove(id);
    }
	
}

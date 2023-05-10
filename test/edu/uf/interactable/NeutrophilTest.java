package edu.uf.interactable;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.uf.interactable.covid.DAMP;
import edu.uf.interactable.covid.SAMP;
import edu.uf.interactable.covid.SarsCoV2;
import edu.uf.utils.Constants;

class NeutrophilTest {
/*	
	String[] p = new String[] {
			"RESTING", 
			"ACTIVE", 
			"ALT_ACTIVE", 
			"MIX_ACITVE", 
			"INACTIVE", 
			"ANERGIC", 
			"TNF_ACTIVE", 
			"ANG_ACTIVE", 
			"IFN_ACTIVE", 
			"APOPTOTIC", 
			"NECROTIC",
			"IL6_ACTIVE",
			"OPEN"
	};
	String[] s = new String[] {"ALIVE", "APOPTOTIC", "NECROTIC", "DYING", "DEAD", "RESTING_CONIDIA", "SWELLING_CONIDIA", "GERMINATING", "HYPHAE", "STERILE_CONIDIA"};

	@Test
	void testCreateNewBooleanNetwork() {
		System.out.println("\nTest CreateNewBooleanNetwork!\n");
		Neutrophil m = new Neutrophil(1.0);
		System.out.println("Expected: RESTING. Find: " + p[m.getPhenotype()]);
		TNFa t = TNFa.getMolecule(new double[1][1][1][1], null);
		t.set(1, 0, 0, 0, 0);
		t.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		System.out.println("Expected: ACTIVE. Find: " + p[m.getPhenotype()]);
		
		m = new Neutrophil(1.0);
		Afumigatus a = new Afumigatus();
		a.setStatus(Afumigatus.SWELLING_CONIDIA);
		for(int i = 0; i < 30; i++)
			a.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		System.out.println("Expected: MIX_ACTIVE. Find: " + p[m.getPhenotype()]);
		
		m = new edu.uf.interactable.covid.Neutrophil();
		int k = 0;
		while(m.getPhenotype() != Phenotypes.APOPTOTIC) {
			k++;
			m.updateStatus();
		}
		System.out.println(k);
	}

	@Test
	void testInteract() {
		System.out.println("\nTest Interact!\n");
		Constants.PR_N_HYPHAE = 1;
		Neutrophil m = new Neutrophil(1.0);
		TNFa t = TNFa.getMolecule(new double[1][1][1][1], null);
		MIP2 i = MIP2.getMolecule(new double[1][1][1][1], null);
		t.set(0, 0, 0, 0, 0);
		i.set(0, 0, 0, 0, 0);
		Afumigatus a = new Afumigatus();
		a.setStatus(Afumigatus.HYPHAE);
		a.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		System.out.println("Expected: MIX_ACTIVE 0.0 DYING. Find: " + p[m.getPhenotype()] + " " + t.get(0, 0, 0, 0) + " " + s[a.getStatus()]);
		t.interact(m, 0, 0, 0);
		i.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		System.out.println("Expected: RESTING >0.0 0.0. Find: " + p[m.getPhenotype()] + " " + t.get(0, 0, 0, 0) + " " + i.get(0, 0, 0, 0));
		
		
		m = new Neutrophil(1.0);
		t.set(1, 0, 0, 0, 0);
		i.set(0, 0, 0, 0, 0);
		t.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		i.interact(m, 0, 0, 0);
		System.out.println("Expected: ACTIVE 1.0 >0.0. Find: " + p[m.getPhenotype()] + " " + t.get(0, 0, 0, 0) + " " + i.get(0, 0, 0, 0));
		
		//MULTIPLE INTERACTIONS: VIRUS, TNF-a, PNEUMOCYTE, APOPTOSIS, THEN INTERACT WITH MACROPHAGE
		
		m = new edu.uf.interactable.covid.Neutrophil();
		m.setPhenotype(Phenotypes.ACTIVE);
		edu.uf.interactable.covid.Pneumocyte pp = null;
		while(pp == null || pp.getPhenotype() != Phenotypes.APOPTOTIC) {
			pp = new edu.uf.interactable.covid.Pneumocyte();
			
			pp.interact(m, 0, 0, 0);
			pp.updateStatus();
		}
		System.out.println("expected APOPTOTIC find: " + p[pp.getPhenotype()]);
		SAMP samp = SAMP.getMolecule(new double[1][1][1][1], null);
		samp.addPhenotype(Phenotypes.APOPTOTIC);
		samp.interact(pp, 0, 0, 0);
		System.out.println("expected 1.0, ACTIVE find: " + samp.get(0, 0, 0, 0) + ", " + p[m.getPhenotype()]);
		samp.interact(m, 0, 0, 0);
		m.updateStatus();
		System.out.println("expected 1.0, ACTIVE find: " + samp.get(0, 0, 0, 0) + ", " + p[m.getPhenotype()]);
		
		
		m = new edu.uf.interactable.covid.Neutrophil();
		pp = null;
		while(pp == null || pp.getPhenotype() != Phenotypes.NECROTIC) {
			 pp = new edu.uf.interactable.covid.Pneumocyte();
			
			SarsCoV2 v = SarsCoV2.getMolecule(new double[1][1][1][1], null);
			v.set(1, 0, 0, 0, 0);
			pp.interact(v, 0, 0, 0);
			pp.updateStatus();
		}
		System.out.println("expected NECROTIC find: " + p[pp.getPhenotype()]);
		DAMP damp = DAMP.getMolecule(new double[1][1][1][1], null);
		damp.addPhenotype(Phenotypes.NECROTIC);
		damp.interact(pp, 0, 0, 0);
		System.out.println("expected 1.0, RESTING find: " + damp.get(0, 0, 0, 0) + ", " + p[m.getPhenotype()]);
		damp.interact(m, 0, 0, 0);
		m.updateStatus();
		System.out.println("expected 0.0, ACTIVE/NECROTIC find: " + damp.get(0, 0, 0, 0) + ", " + p[m.getPhenotype()]);
	}
	
	*/
	
	

}

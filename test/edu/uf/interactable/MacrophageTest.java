package edu.uf.interactable;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.uf.interactable.covid.DAMP;
import edu.uf.interactable.covid.SAMP;
import edu.uf.interactable.covid.SarsCoV2;
import edu.uf.intracellularState.Phenotypes;
import edu.uf.time.Clock;
import edu.uf.utils.Constants;

class MacrophageTest {
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
		Macrophage m = new Macrophage(1.0);
		System.out.println("Expected: RESTING. Find: " + p[m.getPhenotype()]);
		TNFa t = TNFa.getMolecule(new double[1][1][1][1], null);
		t.set(1, 0, 0, 0, 0);
		//t.values[0][0][0][0] = 1;
		t.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		System.out.println("Expected: ACTIVE. Find: " + p[m.getPhenotype()]);
		
		m = new Macrophage(1.0);
		TGFb b = TGFb.getMolecule(new double[1][1][1][1], null);
		b.set(1, 0, 0, 0, 0);
		b.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		System.out.println("Expected: INACTIVE. Find: " + p[m.getPhenotype()]);
		
		m = new Macrophage(1.0);
		Afumigatus a = new Afumigatus();
		a.setStatus(Afumigatus.SWELLING_CONIDIA);
		for(int i = 0; i < 30; i++)
			a.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		System.out.println("Expected: MIX_ACTIVE. Find: " + p[m.getPhenotype()]);
		
		m = new Macrophage(1.0);
		Neutrophil n = new Neutrophil(1.0);
		n.setStatus(Neutrophil.APOPTOTIC);
		for(int i = 0; i < 30; i++) {
			n.interact(m, 0, 0, 0);
			n.setStatus(Neutrophil.APOPTOTIC);
		}
		m.processBooleanNetwork();
		System.out.println("Expected: INACTIVE. Find: " + p[m.getPhenotype()]);
		
		m = new Macrophage(1.0);
		Heme h = Heme.getMolecule(new double[1][1][1][1], null);
		h.set(1, 0, 0, 0, 0);
		h.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		System.out.println("Expected: ACTIVE. Find: " + p[m.getPhenotype()]);
		
		m = new Macrophage(1.0);
		Hemopexin x = Hemopexin.getMolecule(new double[2][1][1][1], null);
		x.set(1, 1, 0, 0, 0);
		x.interact(m, 0, 0, 0);
		h.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		System.out.println("Expected: RESTING. Find: " + p[m.getPhenotype()]);
		
		m = new Macrophage(1.0);
		a = new Afumigatus();
		a.setStatus(Afumigatus.SWELLING_CONIDIA);
		for(int i = 0; i < 30; i++)
			a.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		System.out.println("Expected: MIX_ACTIVE. Find: " + p[m.getPhenotype()]);
		m.processBooleanNetwork();
		System.out.println("Expected: INACTIVE. Find: " + p[m.getPhenotype()]);
		
		
		m = new Macrophage(1.0);
		a = new Afumigatus();
		a.setStatus(Afumigatus.SWELLING_CONIDIA);
		while(m.getPhenotype() != Phenotypes.MIX_ACTIVE) {
			a.interact(m, 0, 0, 0);
			Clock.updade();
			m.processBooleanNetwork();
		}
		
		for(int i = 0; i < 30; i++) {
			Clock.updade();
			m.processBooleanNetwork();
			System.out.println(i + " " + p[m.getPhenotype()]);
		}
		
		
		
		
	}
	
	@Test
	void testInteract() {
		System.out.println("\nTest Interact!\n");
		Constants.PR_MA_HYPHAE = 1;
		Macrophage m = new Macrophage(1.0);
		TNFa t = TNFa.getMolecule(new double[1][1][1][1], null);
		MIP2 i = MIP2.getMolecule(new double[1][1][1][1], null);
		t.set(0, 0, 0, 0, 0);
		i.set(0, 0, 0, 0, 0);
		Afumigatus a = new Afumigatus();
		a.setStatus(Afumigatus.HYPHAE);
		a.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		System.out.println("Expected: MIX_ACTIVE 0.0 HYPHAE. Find: " + p[m.getPhenotype()] + " " + t.get(0, 0, 0, 0) + " " + s[a.getStatus()]);
		a.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		System.out.println("Expected: MIX_ACTIVE 0.0 DYING. Find: " + p[m.getPhenotype()] + " " + t.get(0, 0, 0, 0) + " " + s[a.getStatus()]);
		t.interact(m, 0, 0, 0);
		i.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		System.out.println("Expected: INACTIVE >0.0 0.0. Find: " + p[m.getPhenotype()] + " " + t.get(0, 0, 0, 0) + " " + i.get(0, 0, 0, 0));
		
		
		m = new Macrophage(1.0);
		t.set(1, 0, 0, 0, 0);
		i.set(0, 0, 0, 0, 0);
		t.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		i.interact(m, 0, 0, 0);
		System.out.println("Expected: ACTIVE 1.0 >0.0. Find: " + p[m.getPhenotype()] + " " + t.get(0, 0, 0, 0) + " " + i.get(0, 0, 0, 0));
		
		
		m = new Macrophage(1.0);
		TGFb b = TGFb.getMolecule(new double[1][1][1][1], null);
		b.set(0, 0, 0, 0, 0);
		t.set(0, 0, 0, 0, 0);
		Neutrophil n = new Neutrophil(1.0);
		n.setStatus(Neutrophil.APOPTOTIC);
		for(int j = 0; j < 30; j++) {
			n.interact(m, 0, 0, 0);
			n.setStatus(Neutrophil.APOPTOTIC);
		}
		m.processBooleanNetwork();
		b.interact(m, 0, 0, 0);
		t.interact(m, 0, 0, 0);
		System.out.println("Expected: INACTIVE >0.0 0.0. Find: " + p[m.getPhenotype()] + " " + b.get(0, 0, 0, 0) + " " + t.get(0, 0, 0, 0));
		
		m = new Macrophage(1.0);
		t.set(1, 0, 0, 0, 0);
		for(int j = 0; j < 40; j++) {
			Clock.updade();
			t.interact(m, 0, 0, 0);
			m.processBooleanNetwork();
			System.out.println(j + " " + p[m.getPhenotype()]);
		}
		
		//MULTIPLE INTERACTIONS: VIRUS, TNF-a, PNEUMOCYTE, APOPTOSIS, THEN INTERACT WITH MACROPHAGE
		m = new Macrophage(1.0);
		edu.uf.interactable.covid.Pneumocyte pp = null;
		while(pp == null || pp.getPhenotype() != Phenotypes.APOPTOTIC) {
			 pp = new edu.uf.interactable.covid.Pneumocyte();
			
			SarsCoV2 v = SarsCoV2.getMolecule(new double[1][1][1][1], null);
			v.set(1, 0, 0, 0, 0);
			pp.interact(v, 0, 0, 0);
			pp.updateStatus();
		}
		System.out.println("expected APOPTOTIC find: " + p[pp.getPhenotype()]);
		SAMP samp = SAMP.getMolecule(new double[1][1][1][1], null);
		samp.addPhenotype(Phenotypes.APOPTOTIC);
		samp.interact(pp, 0, 0, 0);
		System.out.println("expected 1.0, RESTING find: " + samp.get(0, 0, 0, 0) + ", " + p[m.getPhenotype()]);
		samp.interact(m, 0, 0, 0);
		m.updateStatus();
		System.out.println("expected 0.0, INACTIVE find: " + samp.get(0, 0, 0, 0) + ", " + p[m.getPhenotype()]);
		
		
		m = new Macrophage(1.0);
		pp = null;
		while(pp == null || pp.getPhenotype() != Phenotypes.NECROTIC) {
			 pp = new edu.uf.interactable.covid.Pneumocyte();
			
			SarsCoV2 v = SarsCoV2.getMolecule(new double[1][1][1][1], null);
			v.set(1, 0, 0, 0, 0);
			pp.interact(v, 0, 0, 0);
			pp.interact(t, 0, 0, 0);
			pp.updateStatus();
		}
		System.out.println("expected NECROTIC find: " + p[pp.getPhenotype()]);
		DAMP damp = DAMP.getMolecule(new double[1][1][1][1], null);
		damp.addPhenotype(Phenotypes.NECROTIC);
		damp.interact(pp, 0, 0, 0);
		System.out.println("expected 1.0, RESTING find: " + damp.get(0, 0, 0, 0) + ", " + p[m.getPhenotype()]);
		damp.interact(m, 0, 0, 0);
		m.updateStatus();
		System.out.println("expected 0.0, ACTIVE find: " + damp.get(0, 0, 0, 0) + ", " + p[m.getPhenotype()]);
		
		
		Constants.PR_MA_PHAG = 1;
		m = new Macrophage(1.0);
		a = new Afumigatus();
		a.setStatus(Afumigatus.GERM_TUBE);
		System.out.println("Expected: RESTING. Find: " + p[m.getPhenotype()]);
		a.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		System.out.println("Expected: MIX_ACTIVE Find: " + p[m.getPhenotype()]);
		
		
	}*/

}

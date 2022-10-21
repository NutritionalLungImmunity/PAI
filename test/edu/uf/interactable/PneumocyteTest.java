package edu.uf.interactable;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.uf.utils.Constants;

class PneumocyteTest {
/*	
	String[] p = new String[] {"RESTING", "ACTIVE", "ALT_ACTIVE", "MIX_ACITVE", "INACTIVE"};
	String[] s = new String[] {"ALIVE", "APOPTOTIC", "NECROTIC", "DYING", "DEAD", "RESTING_CONIDIA", "SWELLING_CONIDIA", "GERMINATING", "HYPHAE", "STERILE_CONIDIA"};

	@Test
	void testCreateNewBooleanNetwork() {
		System.out.println("\nTest CreateNewBooleanNetwork!\n");
		Pneumocyte m = new Pneumocyte();
		System.out.println("Expected: RESTING. Find: " + p[m.getPhenotype()]);
		TNFa t = TNFa.getMolecule(new double[1][1][1][1], null);
		t.set(1, 0, 0, 0, 0);
		t.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		System.out.println("Expected: ACTIVE. Find: " + p[m.getPhenotype()]);
		m.processBooleanNetwork();
		System.out.println("~Find: " + p[m.getPhenotype()]);
		
		m = new Pneumocyte();
		Afumigatus a = new Afumigatus();
		a.setStatus(Afumigatus.SWELLING_CONIDIA);
		for(int i = 0; i < 60; i++)
			a.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		System.out.println("Expected: MIX_ACTIVE. Find: " + p[m.getPhenotype()]);

	}

	@Test
	void testInteract() {
		System.out.println("\nTest Interact!\n");
		Constants.PR_P_INT = 1;
		Pneumocyte m = new Pneumocyte();
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
		System.out.println("Expected: MIX_ACTIVE 0.0 HYPHAE. Find: " + p[m.getPhenotype()] + " " + t.get(0, 0, 0, 0) + " " + s[a.getStatus()]);
		t.interact(m, 0, 0, 0);
		i.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		System.out.println("Expected: RESTING >0.0 0.0. Find: " + p[m.getPhenotype()] + " " + t.get(0, 0, 0, 0) + " " + i.get(0, 0, 0, 0));
		
		
		m = new Pneumocyte();
		t.set(1, 0, 0, 0, 0);
		i.set(0, 0, 0, 0, 0);
		t.interact(m, 0, 0, 0);
		m.processBooleanNetwork();
		i.interact(m, 0, 0, 0);
		System.out.println("Expected: ACTIVE 1.0 >0.0. Find: " + p[m.getPhenotype()] + " " + t.get(0, 0, 0, 0) + " " + i.get(0, 0, 0, 0));
	}
*/
}

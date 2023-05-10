/*package edu.uf.interactable.capa;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.TGFb;
import edu.uf.interactable.TNFa;
import edu.uf.intracellularState.Phenotypes;
import edu.uf.time.Clock;
import edu.uf.utils.Constants;

class CapaMacrophageTest {

	@Test
	void test() {
		CapaMacrophage m = new CapaMacrophage(0);
		System.out.println("expected: false, false " + " found: " + m.inPhenotype(Phenotypes.ACTIVE) + ", " + m.inPhenotype(Phenotypes.INACTIVE));
		Afumigatus a = new Afumigatus();
		a.setStatus(Afumigatus.SWELLING_CONIDIA);
		Constants.PR_MA_PHAG = 1.0;
		
		m.interact(a, 0, 0, 0);
		
		for(int i = 0; i < 260; i++) {
			System.out.println("iteration: " + i + " macrophage active: " + m.inPhenotype(Phenotypes.ACTIVE));
			m.updateStatus();
			Clock.updade();
		}
		
		
		System.out.println("\n##########################################\n");
		
		TGFb b = TGFb.getMolecule(new double[1][1][1][1], null);
		b.set(10.0, 0, 0, 0, 0);
		
		
		for(int i = 0; i < 20; i++) {
			m.interact(b, 0, 0, 0);
			Clock.updade();
		}
		for(int i = 0; i < 260; i++) {
			System.out.println("iteration: " + i + " macrophage inactive: " + m.inPhenotype(Phenotypes.INACTIVE));
			m.updateStatus();
			Clock.updade();
		}
		
		System.out.println("\n##########################################\n");
		
		TNFa t = TNFa.getMolecule(new double[1][1][1][1], null);
		t.set(10.0, 0, 0, 0, 0);
		
		
		for(int i = 0; i < 20; i++) {
			m.interact(t, 0, 0, 0);
			Clock.updade();
		}
		for(int i = 0; i < 260; i++) {
			System.out.println("iteration: " + i + " macrophage inactive: " + m.inPhenotype(Phenotypes.ACTIVE));
			m.updateStatus();
			Clock.updade();
		}
		
	}

}*/

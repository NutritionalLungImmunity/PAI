/*package edu.uf.main.initialize;

import java.util.ArrayList;
import java.util.List;

import edu.uf.Diffusion.Diffuse;
import edu.uf.control.MultiThreadExec;
import edu.uf.geometry.Voxel;
import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.TAFC;
import edu.uf.interactable.invitro.FreeIron;
import edu.uf.interactable.invitro.Glutamate;
import edu.uf.interactable.invitro.InVitroHeme;
import edu.uf.interactable.invitro.InvitroAfumigatus;
import edu.uf.interactable.invitro.TinProtoporphyrin;
import edu.uf.utils.Constants;

public class InitializeHemeInVitroModel extends InitializeBaseInvitroModel{

	@Override
	public void initializeMolecules(Voxel[][][] grid, int xbin, int ybin, int zbin, Diffuse diffuse, boolean verbose) {
		if(verbose) {
    		System.out.println("Initializing Iron, TAFC, Heme, Tin-Protoporphyrin, Glutamate");
    	}
    	FreeIron iron = FreeIron.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	TAFC tafc = TAFC.getMolecule(new double[2][xbin][ybin][zbin], diffuse);
    	InVitroHeme heme = InVitroHeme.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	TinProtoporphyrin tinProtoporphyrin = TinProtoporphyrin.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	Glutamate glu = Glutamate.getMolecule(new double[1][xbin][ybin][zbin], diffuse);
    	
    	
    	MultiThreadExec.setMolecule(iron);
    	MultiThreadExec.setMolecule(heme);
    	MultiThreadExec.setMolecule(tafc);
    	MultiThreadExec.setMolecule(tinProtoporphyrin);
    	MultiThreadExec.setMolecule(glu);
    	
    	
    	
    	Voxel.setMolecule(TAFC.NAME, tafc, true);
    	for(int x = 0; x < xbin; x++) 
        	for(int y = 0; y < ybin; y++)
        		for(int z = 0; z < zbin; z++) {
        			iron.values[0][x][y][z] = Constants.INIT_IRON;
        			heme.values[0][x][y][z] = Constants.INIT_HEME;//Constants.HEME_SYSTEM_CONCENTRATION;
        			tinProtoporphyrin.values[0][x][y][z] = Constants.INIT_TIN_PROTOPORPHYRIN;//Constants.HEME_SYSTEM_CONCENTRATION;
        			glu.values[0][x][y][z] = Constants.INIT_GLU;
        		}
    	Voxel.setMolecule(InVitroHeme.NAME, heme, true);
    	Voxel.setMolecule(TinProtoporphyrin.NAME, tinProtoporphyrin, true);
    	Voxel.setMolecule(Glutamate.NAME, glu, true);
    	Voxel.setMolecule(FreeIron.NAME, iron, true);
    	
    	this.setSecretionPhenotypes();
    	
		
	}
	
	public List<Afumigatus> infect(int numAspergillus, Voxel[][][] grid, int xbin, int ybin, int zbin, int status, double initIron, double sigma, boolean verbose) {
    	if(verbose)System.out.println("Infecting with " + numAspergillus + " conidia!");
    	List<Afumigatus> list = new ArrayList<>();
        for (int i = 0; i < numAspergillus; i++) {
        	int x = randint(0, xbin-1);
            int y = randint(0, ybin-1);
            int z = randint(0, zbin-1);
            InvitroAfumigatus a = new InvitroAfumigatus(
            		x, y, z, x, y, z, random(), random(), random(), 
            		0, initIron, 0, 0, true
            );
            list.add(a);
            a.setStatus(status);
            grid[x][y][z].setCell(a);
        }
    	return list;
    }
	
}*/

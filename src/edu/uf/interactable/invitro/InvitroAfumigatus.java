/*package edu.uf.interactable.invitro;

import edu.uf.interactable.Afumigatus;
import edu.uf.interactable.Interactable;
import edu.uf.utils.Constants;
import edu.uf.utils.Rand;
import edu.uf.utils.Util;

public class InvitroAfumigatus extends Afumigatus{
	
	public static final int[] INIT_AFUMIGATUS_BOOLEAN_STATE = new int[] {1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

	public static final int Orn = 22;
	public static final int Glu = 23;
	
	public static final int SPECIES_NUM = 24;
	
    
    public InvitroAfumigatus(double xRoot, double yRoot, double zRoot, double xTip, double yTip, double zTip, 
    		double dx, double dy, double dz, int growthIteration, double ironPool, int status, 
    		int state, boolean isRoot) {
    	super(xRoot, yRoot, zRoot, xTip, yTip, zTip, dx, dy, dz, growthIteration, ironPool, status, state, isRoot);
    	this.setBooleanNetwork(InvitroAfumigatus.INIT_AFUMIGATUS_BOOLEAN_STATE.clone());
    }

    protected boolean templateInteract(Interactable interactable, int x, int y, int z) {
        if (interactable instanceof InVitroHeme) {
        	InVitroHeme heme = (InVitroHeme) interactable;
            if(this.getStatus() != Afumigatus.RESTING_CONIDIA) {
            	double qtty = Constants.HEME_UP * heme.values[0][x][y][z];
            	this.incIronPool(qtty);
            	heme.dec(qtty, x, y, z);
            	if(Util.activationFunction(heme.get(x, y, z), Constants.Kd_Heme, Constants.STD_UNIT_T) > Rand.getRand().randunif()) {
            		this.setBooleanNetwork(InvitroAfumigatus.Glu, 1);
            	}
            }
            return true;  
        }
        return interactable.interact(this, x, y, z);
    }
    
    
	public void processBooleanNetwork() {
        if(this.getBnIteration() == 15) {
        	int[] temp = new int[InvitroAfumigatus.SPECIES_NUM];
        	//for(int i = 0; i < Afumigatus.SPECIES_NUM; i++)
        	//	temp[i] = 0;

            temp[Afumigatus.hapX] = -this.booleanNetwork[Afumigatus.SreA] + 1;
            temp[Afumigatus.sreA] = -this.booleanNetwork[Afumigatus.HapX] + 1;
            temp[Afumigatus.HapX] = this.booleanNetwork[Afumigatus.hapX] & (-this.booleanNetwork[Afumigatus.LIP] + 1);
            temp[Afumigatus.SreA] = this.booleanNetwork[Afumigatus.sreA] & this.booleanNetwork[Afumigatus.LIP];
            temp[Afumigatus.RIA] = -this.booleanNetwork[Afumigatus.SreA] + 1;
            temp[Afumigatus.EstB] = -this.booleanNetwork[Afumigatus.SreA] + 1;
            temp[Afumigatus.MirB] = this.booleanNetwork[Afumigatus.HapX] & (-this.booleanNetwork[Afumigatus.SreA] + 1);
            temp[Afumigatus.SidA] = this.booleanNetwork[Afumigatus.HapX] & (-this.booleanNetwork[Afumigatus.SreA] + 1);
            temp[Afumigatus.Tafc] = this.booleanNetwork[Afumigatus.SidA] & this.booleanNetwork[InvitroAfumigatus.Orn];
            temp[Afumigatus.ICP] = (-this.booleanNetwork[Afumigatus.HapX] + 1) & (this.booleanNetwork[Afumigatus.VAC] | this.booleanNetwork[Afumigatus.FC1fe]);
            temp[Afumigatus.LIP] = (this.booleanNetwork[Afumigatus.Fe] & this.booleanNetwork[Afumigatus.RIA]) | this.lipActivation();
            temp[Afumigatus.CccA] = -this.booleanNetwork[Afumigatus.HapX] + 1;
            temp[Afumigatus.FC0fe] = this.booleanNetwork[Afumigatus.SidA];
            temp[Afumigatus.FC1fe] = this.booleanNetwork[Afumigatus.LIP] & this.booleanNetwork[Afumigatus.FC0fe];
            temp[Afumigatus.VAC] = this.booleanNetwork[Afumigatus.LIP] & this.booleanNetwork[Afumigatus.CccA];
            // temp[Afumigatus.ROS] = this.boolean_network[Afumigatus.LIP] | 
            //                        (this.boolean_network[Afumigatus.O] & (- (this.boolean_network[Afumigatus.SOD2_3] & this.boolean_network[Afumigatus.ThP] 
            //                         & this.boolean_network[Afumigatus.Cat1_2]) + 1)) 
            //                        | (this.boolean_network[Afumigatus.ROS] & (- (this.boolean_network[Afumigatus.SOD2_3] 
            //                         & (this.boolean_network[Afumigatus.ThP] | this.boolean_network[Afumigatus.Cat1_2])) + 1));
            temp[Afumigatus.ROS] = (this.booleanNetwork[Afumigatus.O] & (- (this.booleanNetwork[Afumigatus.SOD2_3] & this.booleanNetwork[Afumigatus.ThP] 
                                   & this.booleanNetwork[Afumigatus.Cat1_2]) + 1)) 
                                  | (this.booleanNetwork[Afumigatus.ROS] & (- (this.booleanNetwork[Afumigatus.SOD2_3] 
                                   & (this.booleanNetwork[Afumigatus.ThP] | this.booleanNetwork[Afumigatus.Cat1_2])) + 1));
            temp[Afumigatus.Yap1] = this.booleanNetwork[Afumigatus.ROS];
            temp[Afumigatus.SOD2_3] = this.booleanNetwork[Afumigatus.Yap1];
            temp[Afumigatus.Cat1_2] = this.booleanNetwork[Afumigatus.Yap1] & (-this.booleanNetwork[Afumigatus.HapX] + 1);
            temp[Afumigatus.ThP] = this.booleanNetwork[Afumigatus.Yap1];

            temp[Afumigatus.Fe] = 0; // might change according to iron environment?
            temp[Afumigatus.O] = 0;
            temp[InvitroAfumigatus.Orn] = this.booleanNetwork[InvitroAfumigatus.Glu];
            temp[InvitroAfumigatus.Glu] = 0;
            //temp[Afumigatus.TAFCBI] = 0;

            //print(this.boolean_network)
            for(int i = 0; i < InvitroAfumigatus.SPECIES_NUM; i++) {
                this.booleanNetwork[i] = temp[i];
                //System.out.print(this.booleanNetwork[i] + " ");
            }
            //System.out.println();
            this.setBnIteration(0);
        }else
            this.setBnIteration(this.getBnIteration() + 1);
    }
	
	protected Afumigatus createAfumigatus(double xRoot, double yRoot, double zRoot, double xTip, double yTip, double zTip, 
    		double dx, double dy, double dz, int growthIteration, double ironPool, int status, 
    		int state, boolean isRoot) {
    	return new InvitroAfumigatus(xRoot, yRoot, zRoot,
    			xTip, yTip, zTip,
                dx, dy, dy, growthIteration,
                ironPool, status, state, isRoot);
    }
    
	
}*/

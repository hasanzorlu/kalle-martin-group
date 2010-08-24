/*
* TurtleKit - A 'reactive simulation platform' using MadKit Kernel
* Copyright (C) 2000-2007 Fabien Michel, Gregory Beurier
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

package turtlekit2.kernel.observers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import madkit.kernel.AbstractAgent;
import madkit.kernel.ReferenceableAgent;
import madkit.kernel.Watcher;
import madkit.simulation.probes.ReflexiveProbe;
import turtlekit2.kernel.creators.Tk2Launcher;
import turtlekit2.kernel.environment.Patch;
import turtlekit2.kernel.environment.PatchVariable;
import turtlekit2.kernel.pheromone.GridVariable;
import turtlekit2.kernel.tools.XMLAttributes;

/** Observer is an abstract class that have to be extended in order to initialize patches
 * or make some observations.
 * To observe turtle, create TurtleProbe on a special role and then add it using addProbe();
 * walkers = new TurtleProbe(getSimulationGroup(),"walker");
 * addProbe(walkers);
 *
 * You can specially create Turtle[] variables using TurtleProbe's getTurtles() method.
 * This method permits to obtain an array of turtle regarding the role they play.
 * MoreOver this agent has access to the patchGrid variable in order to observe
 * or initialize the patches.
 * @author Fabien MICHEL, Gregory BEURIER
 * @see TurtleProbe
 * @version 3.0 20/02/2002 */

public abstract class Observer extends Watcher implements ReferenceableAgent {
	private static final long serialVersionUID = 1L;
	public String simulationGroup = null;
    public Patch[][] patchGrid;
    public Map<String, GridVariable> gridValues=new HashMap<String, GridVariable>();
    public int envWidth,envHeight;
    public XMLAttributes attrib;
    public ArrayList<PatchVariable> flavors;
    
    
    
    /** MadKit kernel usage*/
    public void activate() {
        println("activated");
        
        requestRole(Tk2Launcher.COMMUNITY, simulationGroup, "observer",null);
        addProbe(new EnvProbe("grid",simulationGroup,"world",this));
        addProbe(new EnvProbe("gridVariables",simulationGroup,"world",this));
        setup();
    }
    
    public double[][] getAllValues(String variableName){
        if(gridValues!=null && gridValues.get(variableName)!=null)
            return gridValues.get(variableName).getAllValues();
        else
            return null;
    }
    
    final public String getSimulationGroup(){return simulationGroup;}
    
    /**override this method to observe the state of the world using turtle tables
     * or/and the patchGrid variable*/
    public void watch(){};
    
    public void reset(){};
    
    /**override this method to make other initializations,not in constructor*/
    public void setup(){
    };
    
    @SuppressWarnings("unchecked")
	final synchronized void updateWorldData(Object o) {
        if(o instanceof Patch[][])
            patchGrid=(Patch[][])o;
        else
            gridValues=(Map<String, GridVariable>)o;
    }
    
    public XMLAttributes getAttrib() {
        return attrib;
    }
    public void setAttrib(XMLAttributes attrib) {
        this.attrib = attrib;
    }
	/**
	 * @return Returns the flavors.
	 */
    /*************PatchVariables Methods for obs**********/
	public ArrayList<PatchVariable> getFlavors() {
		return flavors;
	}
	
	public PatchVariable getFlavor(int i) {
		return (PatchVariable)(flavors.get(i));
	}
	
	public String getFlavorName(int i) {
		return ((PatchVariable)(flavors.get(i))).getName();
	}
	
	public boolean flavorsContains(String name){
		for(int i=0; i<flavors.size();i++){
			if(((PatchVariable)(flavors.get(i))).getName().equals(name)) return true;
		}
		return false;
	}
	
	/*********************************************************/
	/**
	 * @param flavors The flavors to set.
	 */
	public void setFlavors(ArrayList<PatchVariable> flavors) {
		this.flavors = flavors;
	}
	
}

class EnvProbe extends ReflexiveProbe {
    Observer myViewer;
    
    EnvProbe(String property, String group, String role,Observer viewer) {
        super(Tk2Launcher.COMMUNITY,group,role,property);
        myViewer=viewer;
    }
    
    public void update(AbstractAgent theAgent, boolean added) {
        super.update(theAgent,added);
        if(added)
            myViewer.updateWorldData(getObject(theAgent));
        System.err.println(this);
    }
    
    public void initialize() {
        super.initialize();
        if(numberOfAgents()>0)
            myViewer.updateWorldData(getObject(getAgentNb(0)));//(Patch[][])getObject(getAgentNb(0)));*/
        System.err.println(this);
    }
    
}

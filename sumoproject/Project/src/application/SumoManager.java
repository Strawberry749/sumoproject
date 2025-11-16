package application;

import java.util.ArrayList;
import java.util.List;

import de.tudresden.sumo.cmd.Route;
import de.tudresden.sumo.cmd.Inductionloop;
import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoVehicleData;
import it.polito.appeal.traci.SumoTraciConnection;
import de.tudresden.sumo.cmd.Vehicletype;
import de.tudresden.sumo.objects.SumoColor;
import de.tudresden.sumo.util.SumoCommand;
import javafx.concurrent.Task;
import java.util.concurrent.atomic.AtomicBoolean;

public class SumoManager {
	//Creates object for the connection
	private SumoTraciConnection conn;
	// Bool to represent if the connection is active
	private AtomicBoolean simulationRunning = new AtomicBoolean(false);
	//Setting variable for the length of a simulation step
	double step_length = 0.1;
	int vehicle_num;
	public boolean startSimulation() {
		// Tries to start the server
		try {
			
			//Setting variables for the connection
			String sumo_bin = "sumo-gui";
			String sumo_config = "path to config.sumocfg";
			conn = new SumoTraciConnection(sumo_bin, sumo_config);
			conn.addOption("step-length", step_length + ""); //Adds the length of each simulation step
			conn.addOption("start", "true"); //start sumo immediately
			conn.addOption("num-clients", "1"); //How many clients connect
			
			//Runs sumo instance and tries to connect to server
			conn.runServer();
			//sets order
	        conn.setOrder(1);
			simulationRunning.set(true);
			return true;
			//Adds settings to the simulation
			
	        
		//If it doesnt work, prints the error 
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
	}

    }
	public void startSimulationLoop() {        
        // Create a new "Task"		
        Task<Void> simulationTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {            	      	       
        	        //Main Loop
        	        while(simulationRunning.get()) {
        	        	conn.do_timestep();
        	        	Thread.sleep((long) (step_length * 1000));
        	        }
                	} catch (IllegalStateException e) {
                    System.out.println("Connection was closed by an error. Stopping simulation loop.");
                    simulationRunning.set(false);
                		
        	        
        	        //closes the connection
        	        conn.close();
        	        System.out.println("Sim conn closed");
                    // This loop runs on a background thread
                    
                } catch (Exception e) {
                	// Only print error if we didn't stop it ourselves
                    if (simulationRunning.get()) { 
                        e.printStackTrace();
                    }
                } finally {
                	// always executes, closes application
                	if(conn != null) {
                		try {
                			conn.close();
                			System.out.println("Sim conn closed");
                		} catch (Exception e) {
                			
                		}
                	}
                }
                return null;
            }
        };

        // Start the task on a new thread
        new Thread(simulationTask).start();
    }
	public void stopSimulation() {
		// Tells the loop to stop
        simulationRunning.set(false); 
	}
	public List<String> getRouteIDs() {
		// Gets Route IDs so we can choose them via dropdown menu
        if (conn == null) return new ArrayList<String>();
        try {
            return (List<String>)conn.do_job_get(Route.getIDList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<String>();
        }
    }
	
	public List<String> getVehicleTypeIDs() {
		// Gets Vehicle Types so we can choose them via dropdown menu
	    if (conn == null) return new ArrayList<String>();
	    try {
	        return (List<String>)conn.do_job_get(Vehicletype.getIDList());
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<String>();
	    }
	}
	
	public String injectVehicle(String route, String model, String speed) throws Exception {
		// Used to inject Vehicles, takes values provided and passes the Id to handleInjectButton in InjectVehicleController as a local variable
        if (conn == null || !simulationRunning.get()) {
            throw new Exception("Simulation is not running.");
        }        
        String vehicleId = "v_" + System.currentTimeMillis();        
        conn.do_job_set(Vehicle.addFull(vehicleId, route, model, "now", "0", "0", speed, "current", "max", "current", "", "", "", 0, 0));        
        return vehicleId;
    }
	
	public void setVehicleColor(String vehicleId, SumoColor color) {
		// passes color to handleInjectButton in InjectVehicleController as a local variable
    if (conn == null) return;
    try {
        conn.do_job_set(Vehicle.setColor(vehicleId, color));
    } catch (Exception e) {
        e.printStackTrace();
    }
    
}


}

package de.frauas;
/*
Put header here


 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import de.tudresden.sumo.cmd.Inductionloop;
import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoVehicleData;
import it.polito.appeal.traci.SumoTraciConnection;

public class FXMLController implements Initializable {
    
    @FXML
    private Label lblOut;
    
    @FXML
    private void btnClickAction(ActionEvent event) {
        //Hello World button
        lblOut.setText("Hello World!");

        //Setting variables for the connection
		String sumo_bin = "sumo-gui";
		String sumo_config = "/home/user/SumoProject/sumoproject/src/main/config/config.sumocfg";
		//Setting variable for the length of a simulation step
		double step_length = 0.1;
		int vehicle_num;
		
		//Tries to start the server
		try {
			//Creates objekt for the connection
			SumoTraciConnection conn = new SumoTraciConnection(sumo_bin, sumo_config);
			//Adds settings to the simulation
			conn.addOption("step-length", step_length + ""); //Adds the length of each simulation step
			conn.addOption("start", "true"); //start sumo immediately
			conn.addOption("num-clients", "1"); //How many clients connect
			
			//Runs sumo instance and tries to connect to server
			conn.runServer();
			//sets order
	        conn.setOrder(1);
	       
	        //Main Loop
	        int lastPhase = -1;
	         for (int i = 1; i < 6000; i++) {
	             conn.do_timestep();
	             conn.do_job_set(Vehicle.addFull("v" + i, "r1", "car", "now", "0", "0", "max", "current", "max", "current", "", "", "", 0, 0));
	             double timeSeconds = (double)conn.do_job_get(Simulation.getTime());
	             int tlsPhase = (int)conn.do_job_get(Trafficlight.getPhase("gneJ1"));
	             if (tlsPhase != lastPhase) {
	                 String tlsPhaseName = (String)conn.do_job_get(Trafficlight.getPhaseName("gneJ1"));
	                 System.out.println(String.format("Step %s, tlsPhase %s (%s)", timeSeconds, tlsPhase, tlsPhaseName));
	                 lastPhase = tlsPhase;
	             }

	             SumoVehicleData vehData = (SumoVehicleData)conn.do_job_get(Inductionloop.getVehicleData("loop1"));
	             for (SumoVehicleData.VehicleData d : vehData.ll) {
	                 System.out.println(String.format("  veh=%s len=%s entry=%s leave=%s type=%s", d.vehID, d.length, d.entry_time, d.leave_time, d.typeID));
	             }
	         }
	        
	        //closes the connection
	        conn.close();
	        
		//If it doesnt work, prints the error 
		} catch (Exception ex) {
			ex.printStackTrace();
	}

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}

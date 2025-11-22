package main;

import java.io.File;
import java.nio.file.Paths;

import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.cmd.Vehicle;
import it.polito.appeal.traci.SumoTraciConnection;
import de.tudresden.sumo.objects.SumoStringList;

public class SumoDemo {

    private String sumo_bin;
    private String sumo_config;
    private SumoTraciConnection conn;


    // Constructors to initialize sumo_bin and sumo_config variables and creates new SumoTraciConnection object.
    public SumoDemo(String sumo_bin, String sumo_config){
        this.sumo_bin = sumo_bin;
        this.sumo_config = sumo_config;
        this.conn = new SumoTraciConnection(this.sumo_bin, this.sumo_config);
    }

    public SumoDemo(){
        this.sumo_bin = "sumo-gui";

        // Get relative path with correct separators for windows and unix.
        String rel_cfg_path = "sumoproject" + File.separator + "src" + File.separator + "main" + File.separator + "config" + File.separator + "config.sumocfg";
        // Turn it into an absolute path
        String abs_cfg_path_string = Paths.get(rel_cfg_path).toAbsolutePath().toString();

        this.sumo_config = abs_cfg_path_string;
        this.conn = new SumoTraciConnection(this.sumo_bin, this.sumo_config);
    }

    // Method to connect to Sumo instance, run a simulation and disconnect.
    public void connect() throws Exception{

        try{
            // Sets the step length to 1 second and makes the simulation start right when Sumo instance is run.
            conn.addOption("step-length", "1");
            conn.addOption("start", "true");
            // Tries to connect to Sumo instance.
            conn.runServer();

            // Gets a list of all traffic light ID's and prints the ID for each traffic light in the list.
            SumoStringList tl_list = (SumoStringList) conn.do_job_get(Trafficlight.getIDList());
            
            for(String id : tl_list){
                System.out.println("Traffic Light: " + id);
            }

            // Loop which goes through 100 time steps, spawns 2 vehicles on different edges each timestep and prints out the traffic light state each timestep.
            for(int i=0; i<100; i++){
                conn.do_timestep();
                // slows down the speed of execution so vehicles move slower in the simulation.
                Thread.sleep(50);

                // Adds One vehicle on edge West-East and one on edge South-North
                conn.do_job_set(Vehicle.addFull("vehicle_r1_" + i, "r1", "car", "now", "0", "0", "max", "current", "max", "current", "", "", "", 0, 0));
                conn.do_job_set(Vehicle.addFull("vehicle_r2_" + i, "r2", "car", "now", "0", "0", "max", "current", "max", "current", "", "", "", 0, 0));

                String current_tl_state = (String) conn.do_job_get(Trafficlight.getRedYellowGreenState("gneJ1"));

                System.out.println("Current Traffic Light state = " + current_tl_state);
            }

            // closes the connection to sumo.
            conn.close();

        }
        // Prints an error if exception is thrown.
        catch (Exception excep){
            excep.printStackTrace();
        }
        
    }

    //Not needed because implemented into SceneController.java
    // Main method creates a SumoDemo object and calls connect() method.
    /*public static void main(String[] args) throws Exception{
        SumoDemo dem1 = new SumoDemo();

        dem1.connect();

    }*/
    
}

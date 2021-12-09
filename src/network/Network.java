package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

class Helper{
    public static void print(Object o){
        FileWriter fw = null;
        try {
            fw = new FileWriter("log.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println(o);
            pw.flush();
        } catch (IOException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println(o);
    }
}

class Router {
    private List<String> connections = new ArrayList();
    private static Semaphore semaphore;
    
    Router(int numberOfConnections, int numberOfDevices){
        semaphore = new Semaphore(numberOfConnections);
        for (int i=1; i <= numberOfConnections; i++){
            String connection = "Connection " + Integer.toString(i);
            connections.add(connection);
        }
    }
    
    public static void occupyConnection(Device device){
        semaphore.Wait(device);
        device.setConnection("Connection *");
    }
    
    public static void releaseConnection(){
        semaphore.Signal();
    }

}

class Device extends Thread{
    private final String name;
    private final String type;
    private String connection;
    
    public Device(){
        this.name = "unknown";
        this.type = "unknown type";
    }
    public Device(String name, String type){
        this.name = name;
        this.type = type;
    }
    public void setConnection(String connection){
        this.connection = connection;
        Helper.print(this.connection + " : " + this.name + " Occupied");
    }
    
    @Override
    public void run(){
        try {
            Random objGenerator = new Random();
            Helper.print(this.connection + " : " + this.name + "->" + "login");
            sleep( objGenerator.nextInt(5000) );
            Helper.print(this.connection + " : " + this.name + "->" + "performing online activities");
            sleep( objGenerator.nextInt(5000) );
            Helper.print(this.connection + " : " + this.name + "->" + "logout");
            Router.releaseConnection();
        } catch (InterruptedException ex) {
            
        }
    }
    
    @Override
    public String toString(){
        return "("+this.name+") ("+this.type+")";
    }
}

class Semaphore{
    private static int maxDevices ;  // whatever maximum number
    private static int availableDevices;

    Semaphore(){
        this.maxDevices = 2;
        availableDevices = 2;
    }
    
    Semaphore(int maxDevices){
        this.maxDevices = maxDevices;
        availableDevices = maxDevices;
    }
    
    public synchronized void Wait(Device d){  
        boolean waiting = false;
        boolean printed = false;
        while(availableDevices==0){
            waiting = true;
            if(!printed){
                //System.out.println(d + " arrived and waiting");
                Helper.print(d + " arrived and waiting");
                printed = true;
            }
            try{ wait(1); }
            catch(InterruptedException e){}
        }
        if(!waiting){
            //System.out.println(d + " arrived");
            Helper.print(d + " arrived");
        }
        availableDevices--;  
    }

    public synchronized void Signal(){
        while(availableDevices==maxDevices){
            try{ wait(); }
            catch(InterruptedException e){}
        }
        availableDevices++;  
    }
    
    public int getAvailable(){
        return Semaphore.availableDevices;
    }
}

public class Network {

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        
        System.out.println("What is the number of WI-FI Connections?");
        int connectionsNumber = in.nextInt();
        System.out.println("What is the number of devices Clients want to connect?");
        int devicesNumber = in.nextInt();

        Router r = new Router(connectionsNumber,devicesNumber);
        List<Device> devices = new ArrayList(0);
        
        for(int i=0; i<devicesNumber ;i++){
            BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
            String parsedInput[] = bi.readLine().split(" ");
            
            String deviceName = parsedInput[0];
            String deviceType = parsedInput[1];
            
            devices.add( new Device(deviceName,deviceType));
        }
        
        for(Device device : devices){
            r.occupyConnection(device);
            device.start();
        }
    }
    
}

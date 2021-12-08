package network;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        System.out.println(this.connection + " : " + this.name + " Occupied");
    }
    
    @Override
    public void run(){
        try {
            Random objGenerator = new Random();
            System.out.println(this.connection + " : " + this.name + "->" + "login");
            sleep( objGenerator.nextInt(5000) );
            System.out.println(this.connection + " : " + this.name + "->" + "performing online activities");
            sleep( objGenerator.nextInt(5000) );
            System.out.println(this.connection + " : " + this.name + "->" + "logout");
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
                System.out.println(d + " arrived and waiting");
                printed = true;
            }
            try{ wait(1); }
            catch(InterruptedException e){}
        }
        if(!waiting){
            System.out.println(d + " arrived");
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

    public static void main(String[] args) {
        Router r = new Router(2,4);
        
        Device d1 = new Device("d1","mobile");
        Device d2 = new Device("d2","pc");
        Device d3 = new Device("d3","tablet");
        Device d4 = new Device("d4","mobile");
        r.occupyConnection(d1);
        d1.start();
        r.occupyConnection(d2);
        d2.start();
        r.occupyConnection(d3);
        d3.start();
        r.occupyConnection(d4);
        d4.start();
    }
    
}

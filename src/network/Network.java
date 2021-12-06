package network;

import java.util.Random;

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
        System.out.println(this.connection + ": " + this.name + " Occupied");
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
        } catch (InterruptedException ex) {
            
        }
    }
    
    @Override
    public String toString(){
        return "("+this.name+") ("+this.type+") ";
    }
}

class Semaphore{
    private static final int maxDevices = 3;  // whatever maximum number
    private static int availableDevices = maxDevices;

    public synchronized void Wait(){  
        while(availableDevices==0){  
            try{ wait(); }
            catch(InterruptedException e){}
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
}

public class Network {

    public static void main(String[] args) {
        Device d1 = new Device("d1","mobile");
        d1.setConnection("Connection 1");
        Device d2 = new Device("d2","pc");
        d2.setConnection("Connection 2");
        Device d3 = new Device("d3","tablet");
        d3.setConnection("Connection 3");
        Device d4 = new Device("d4","mobile");
        d4.setConnection("Connection 4");
        
        d1.start();
        d2.start();
        d3.start();
        d4.start();
    }
    
}

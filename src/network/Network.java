package network;

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
        
    }
    
}

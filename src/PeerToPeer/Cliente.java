/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PeerToPeer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kevin
 */
public class Cliente implements Runnable{

    InetAddress broadcastAddr;
    DatagramSocket ds;
    DatagramPacket dp;
    StringTokenizer tokens;
    String usrName,msjRecibido,addrDestino,parser,index,reporte,nodo;    
    int port;    
    HashMap<String , String> map;
    byte [] buffer;
   
    public Cliente(InetAddress ipBroadcast,int port,String name,HashMap<String,String> map) throws SocketException{
        this.broadcastAddr = ipBroadcast;
        this.usrName=name;
        this.port=port;
        this.map=map;       
        ds = new DatagramSocket(port);                
    }
    @Override
    public void run() {
     escuchar();
    }
    
    private void escuchar(){
        buffer = new byte[256];
        while(true){            
            try {               
                dp = new DatagramPacket(buffer, buffer.length);
                ds.receive(dp);                
                msjRecibido = new String(dp.getData(), 0, dp.getLength()); 
                addrDestino=dp.getAddress().getHostAddress();
                parser=parsear(msjRecibido,addrDestino);
                if(parser!=null){
                    System.out.println(parser);   
                }                
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
    private String parsear(String msj, String hostAddress){       
        if(msj.equals("--listar")){                        
            try {
                buffer = new byte[256];                
                reporte="report@"+usrName;
                buffer = reporte.getBytes();               
                dp = new DatagramPacket(buffer,buffer.length,broadcastAddr,port);     
                ds.send(dp);              
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }            
        return null;
        }
        tokens = new StringTokenizer(msj,"@");
        index=tokens.nextToken();
        if(index.equals(usrName)||index.equals("global")){
            if(index.equals("global")){
                for (Map.Entry<String, String> entry : map.entrySet()) {                                      
                    if(entry.getValue().equals(hostAddress)){
                        System.out.println(entry.getKey()+" desde "+hostAddress+" les dice a todos:");              
                        break;
                    }                
                }   
            }              
            if(index.equals(usrName)){
                for (Map.Entry<String, String> entry : map.entrySet()) {                                
                    if(entry.getValue().equals(hostAddress)){
                        System.out.println(entry.getKey()+" desde "+hostAddress+" dice:");              
                        break;
                    }
                }
            }
        return tokens.nextToken(); 
        }          
        if(index.equals("report")){
            nodo=tokens.nextToken();
            map.put(nodo, hostAddress);
        return null;
        }                  
    return null;                        
    }
}

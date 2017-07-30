/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PeerToPeer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author kevin
 */
public class Server implements Runnable {

    DatagramSocket ds;
    DatagramPacket dp;
    InetAddress broadcastAddr;    
    HashMap<String,String> map;    
    BufferedReader in;
    byte [] buffer;
    int port;
    String usrName; 
    String Line=null;
            
    public Server(InetAddress broadcastAddr,int port,String name,HashMap<String,String> map) throws SocketException{
        this.usrName=name;
        this.broadcastAddr=broadcastAddr;
        this.port=port;
        this.map=map;
        this.ds=new DatagramSocket();        
    }
    @Override
    public void run() {       
        buffer = new byte[256];            
        while(true){
            try {                                                        
                in = new BufferedReader(new InputStreamReader(System.in));                
                Line = in.readLine();
                if(Line.compareTo("--listar")==0){
                    map.clear();                    
                }                                                   
                buffer = Line.getBytes();               
                dp = new DatagramPacket(buffer,buffer.length,broadcastAddr,port);     
                ds.send(dp);
                if(Line.compareTo("--listar")==0){
                    Thread.sleep(1000);                    
                    for (Map.Entry<String, String> entry : map.entrySet()) {                        
                        System.out.println("Usuario Connectado: "+entry.getKey()+"\t"+entry.getValue());
                    }                   
                }
//                if (Line.compareTo("--jugar")==0) {
//                    new Palabras().frase();
//                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}

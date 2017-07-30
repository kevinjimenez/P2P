/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PeerToPeer;

import java.io.FileNotFoundException;
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
import jdk.nashorn.internal.ir.BreakNode;

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
    Palabras cartas;
    
   
    public Cliente(InetAddress ipBroadcast,int port,String name,HashMap<String,String> map) throws SocketException, FileNotFoundException{
        this.broadcastAddr = ipBroadcast;
        this.usrName=name;
        this.port=port;
        this.map=map;       
        ds = new DatagramSocket(port);                
        cartas = new Palabras();
    }
    @Override
    public void run() {
     listener();
    }
    
    private void listener(){
        buffer = new byte[256];
        while(true){            
            try {               
                dp = new DatagramPacket(buffer, buffer.length);
                ds.receive(dp);                
                msjRecibido = new String(dp.getData(), 0, dp.getLength());
                //System.out.println(msjRecibido);
                addrDestino=dp.getAddress().getHostAddress();
                //System.err.println("Que pasa aki");                
                parser=parsear(msjRecibido,addrDestino);
                if(parser!=null){
                    System.out.println(parser);   
                }                
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
    private String parsear(String mensaje, String hostAddress) throws FileNotFoundException, IOException{       
        System.out.println("ingresando --> "+mensaje);
        if(mensaje.equals("--listar")){                        
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
//        if (mensaje.compareTo("--jugar")==0) {
//            System.out.println("inicio el juego");
//        
//        return null;
//        }
        tokens = new StringTokenizer(mensaje,"@");        
        index=tokens.nextToken();
        System.out.println("indice: "+index);
        if(index.equals(usrName)||index.equals("global")){
            System.out.println(" :'v "+index);
            if(index.equals("global")){
                for (Map.Entry<String, String> entry : map.entrySet()) {                                      
                    if(entry.getValue().equals(hostAddress)){
                        System.out.println(entry.getKey()+" desde "+hostAddress+" les dice a todos:");              
                        break;
                    }                
                }   
            }              
            if(index.equals(usrName)){
//                String play =tokens.nextToken();
//                if (play.compareTo("jugar")==0) {
//                    new Palabras().frase();
//                    
//                }
                System.out.println(":v "+index);
                for (Map.Entry<String, String> entry : map.entrySet()) {                                
                    if(entry.getValue().equals(hostAddress)){
                        System.out.println(entry.getKey()+" desde "+hostAddress+" dice:");              
                        break;
                    }
                }
            }
            String next = tokens.nextToken();        
            System.out.println("siguiente token: "+next.toString());
            if (next.toString().equals("play")) {
                System.out.println("bien vaquero");
                for (Barajas b : cartas.frase()) {
                    System.out.println(b.id+":"+b.palabra);
                }
                //new Palabras().frase();
            }
        return next;
        }
        System.out.println("khee "+index);
        if(index.equals("report")){
            nodo=tokens.nextToken();
            System.out.println(nodo);
            map.put(nodo, hostAddress);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println(key+":"+value);
            }
        return null;
        }                  
    return null;                        
    }
}

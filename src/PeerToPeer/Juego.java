/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PeerToPeer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 * @author kevin
 */
public class Juego {
    HashMap<String, String> map;
    HashMap<Integer, String> listCards;
    HashMap<Integer, String> sobras;
    byte [] buffer;
    int [] noSriven;
    DatagramPacket dp;
    //DatagramSocket ds;
    String usrNameRecivido,word;
    StringTokenizer tokens;
    Integer key;
    int valor;
    
    public Juego(HashMap<String, String> map) {
        this.map=map;
        //this.valor=valor;
        listCards=new HashMap<>();
        
        
        
    }
    
    
    public void play(DatagramSocket ds, String msjRecive, String usrName,String reporte) throws IOException, InterruptedException{
        for (int i = 0; i < map.size()*10; i++) {
            buffer=new byte[256];
            dp=new DatagramPacket(buffer, buffer.length);
            ds.receive(dp);
            msjRecive=new String(dp.getData(),0,dp.getLength());
            System.out.println(msjRecive);
            if(msjRecive.compareTo("jugar")!=0){
                tokens = new StringTokenizer(msjRecive,"@");
                usrNameRecivido=tokens.nextToken();
                key = Integer.parseInt(tokens.nextToken());
                word = tokens.nextToken();
                System.out.println(usrNameRecivido+" name "+usrName+" "+key+" "+word);
                if (usrNameRecivido.equals(usrName)) {
                    listCards.put(key, word);
                    valor = Integer.parseInt(tokens.nextToken());
                }
            }
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey()+" "+entry.getValue());            
        }
        sortCards();
        Thread.sleep(1000);
        intercambioCards(reporte, ds);
        
    }
    public void sendCads(String str){ //intercambio        
        int newKey=Integer.parseInt(str);
        String neWord=str;
        if (((valor-1)*10)<newKey&&(valor)*10>=newKey) {
            listCards.put(newKey, neWord);
            System.out.println("cartas finales");
            for (Map.Entry<Integer, String> entry : listCards.entrySet()) {                
                System.out.println(entry.getKey()+" "+entry.getValue());
            }
        }
    }
    
    public void sortCards(){
        noSriven = new int[20];
        int index=0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String llave = entry.getKey();
            String value = entry.getValue();
            if (((valor-1)*10)<Integer.parseInt(llave)&&(valor)*10>=Integer.parseInt(llave)) {                
            }else{
                sobras.put(Integer.parseInt(llave), value);
                noSriven[index]=Integer.parseInt(llave);
            }
            index++;
        }
        for (int i = 0; i < noSriven.length; i++) {
            listCards.remove(noSriven[i]);            
        }
        System.out.println("Mias");
        for (Map.Entry<Integer, String> entry : listCards.entrySet()) {
            System.out.println(entry.getKey()+" "+entry.getValue());            
        }
        System.out.println("NO sirven");
        for (Map.Entry<Integer, String> entry : sobras.entrySet()) {
            System.out.println(entry.getKey()+" "+entry.getValue());            
        }
    }
    
    public void intercambioCards(String reporte,DatagramSocket ds) throws IOException{
        for (Map.Entry<Integer, String> entry : sobras.entrySet()) {
            buffer = new byte[256];
            reporte="intercambio@"+entry.getKey()+"@"+entry.getValue();
            System.out.println(reporte);
            buffer=reporte.getBytes();
            dp=new DatagramPacket(buffer, buffer.length);
            ds.send(dp);
        }
    }

    
    
    
}

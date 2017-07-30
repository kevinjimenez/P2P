/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PeerToPeer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 *
 * @author kevin
 */
public class Palabras {
        
    Stack < Integer > listRandom;
    HashMap<Integer, String> listaPalabrar; 
    ArrayList<Barajas> listaPro;
    File texto ;
    BufferedReader in;
    StringTokenizer tokens;
    String Line;
    int lend;
    int llave;

    public Palabras() throws FileNotFoundException {
        listaPalabrar = new HashMap<>();
        listaPro=new ArrayList<>();
        texto = new File("src/frases.txt");
        in = new BufferedReader(new FileReader(texto));
    }
    
    
    public ArrayList<Barajas> frase() throws IOException {                
        Line = in.readLine();
        tokens = new StringTokenizer(Line," ");        
        int i = 1;
        while (tokens.hasMoreTokens()) {
            String palabra = tokens.nextToken().replaceAll("[,|.|:|;]", "");
            listaPalabrar.put(i, palabra);            
            i++;
        }      
        for (Integer word : reparto(listaPalabrar.size())) {
            //System.out.println(word+" = "+listaPalabrar.get(word));
            Barajas carta = new Barajas();
            carta.id=word;
            carta.palabra=listaPalabrar.get(word);
            listaPro.add(carta);
        }
//        System.out.println("\n");
//        for (Barajas x : listaPro) {
//            System.out.println(x.id+" : "+x.palabra);
//        }
        return listaPro;
        
    }
    
    private Stack<Integer> reparto(int tamano){
        listRandom = new Stack<Integer>();
        for (int i = 0; i < tamano; i++) {
            llave=(int) Math.floor(Math.random()*tamano);
            while (listRandom.contains(llave)) {                
                llave = (int) Math.floor(Math.random()*tamano);
            }
            listRandom.push(llave);
        }        
        return listRandom;
    }
    
    
    
}

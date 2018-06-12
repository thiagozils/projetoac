/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.catolicasc.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author Thiago
 */
public class TesteJ48 {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        Instances instance;
        DataSource ds;
        try {
            
            ds = new DataSource("C:\\JAVA\\TestesAI\\src\\br\\org\\catolicasc\\src\\ac.arff");
            instance = ds.getDataSet();
            instance.setClassIndex(instance.numAttributes() -1);

            J48 arvore = new J48();

            arvore.buildClassifier(instance);
            System.out.println("" + arvore);
            System.out.println("Resultado: " + arvore);

            Instance inst = new DenseInstance(4);

            System.out.println("Primeiro atributo: " + instance.attribute(0));
            System.out.println("Segundo atributo: " + instance.attribute(1));
            System.out.println("Terceiro atributo: " + instance.attribute(2));
            System.out.println("Quarto atributo: " + instance.attribute(3));
            
            
            String server = "localhost";
            String port = "3306";
            String user = "root";
            String passwd = "1234";
            String database = "projetoai";

            String url = "jdbc:mysql://" + server + ":" + port + "/" + database;
            Connection con = DriverManager.getConnection(url, user, passwd);

            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM projetoai.sensor ORDER BY timestamp desc limit 1");
            
            
            double temperatura = 0;
            double humidity = 0;
            while (res.next()) {
                temperatura = res.getDouble("temperature");
                humidity = res.getDouble("humidity");

            }
           
            inst.setValue(instance.attribute(0), temperatura);
            inst.setValue(instance.attribute(1), humidity);
            inst.setDataset(instance);
            System.out.println("\nInstância: " + inst);

            double teste = arvore.classifyInstance(inst);
            
            
            
            
            if (teste == 1) {
                JOptionPane.showMessageDialog(null,"Temperatura atual: "+temperatura+"\n Umidade atual: "+humidity+"\n Não Liga !");
            } else {
                JOptionPane.showMessageDialog(null,"Temperatura atual: "+temperatura+"\n Umidade atual: "+humidity+"\n Liga !");
            }

        } catch (Exception ex) {
            Logger.getLogger(TesteJ48.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

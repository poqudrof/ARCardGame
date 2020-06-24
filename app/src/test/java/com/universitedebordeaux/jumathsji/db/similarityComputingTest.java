package com.universitedebordeaux.jumathsji.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class similarityComputingTest{
    private similarityComputing similarityComputing;

    String a = "Lorem ipsum dolor sit amet."; // 5 words Lorem Ipsum
    String b = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent eu."; // 10 words Lorem Ipsum
    String c = "Lorem ipsum dolor amet sit."; // 5 words Lorem Ipsum w/ 2 last words inverted
    String d = ""; // Void text
    String e = "Lorme ipsum dolor sit amet, consectetur adipiscing elit. Praesent eu."; //same as b, with 2 inverted letters
    String f = "Lorem ipsum dolor sit amet."; // Same as a
    String g = "Lorem ipsum ipsum sit amet.";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void tmpDBTest1(){
        assertEquals(0, similarityComputing.testDB(a,b, c));
    }

    @Test
    public void tmpTestDB2(){
        assertEquals(2, similarityComputing.testDB(a, a, f));

    }

    @Test
    public void tmpTestDB3(){
        assertEquals(0, similarityComputing.testDB(a, b, e));
    }

    @Test
    public void tmpTestDB4() {
        assertEquals(2, similarityComputing.testDB(a, b, f));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testNaive_ill(){
        similarityComputing.naiveCompute(a, d);
    }

    @Test
    public void testNaive_equal(){
        assertEquals(100, similarityComputing.naiveCompute(a, a), 0.00001);
    }

    @Test
    public void testNaive_50(){
        assertNotEquals(100, similarityComputing.naiveCompute(a, b), 0.0001);
    }

    @Test
    public void testNaive_inversWords(){
        assertEquals(60, similarityComputing.naiveCompute(a, c), 0.000001);
    }

    @Test
    public void testNaive_inversLetter(){
        assertEquals(90, similarityComputing.naiveCompute(b, e), 0.000001);
    }

    @Test
    public void testNaive_nearlySim(){
        assertEquals(80, similarityComputing.naiveCompute(a, g), 0.00001);
    }


    //@Test
    public void allDBTest_85() {
        testDB(85);
    }

    //@Test
    public void allDBTest_90(){
        testDB(90);
    }

    @Test
    public void e3dcm1DBTest_85(){testOnDB("e3dcm1.yaml", 85);}

    @Test
    public void e3dcm1DBTest_90(){testOnDB("e3dcm1.yaml", 90);}

    @Test
    public void specificDBTest_85(){testOnDB("emcm2.yaml", 85);}

    @Test
    public void specificDBTest_90(){testOnDB("emcm2.yaml", 90);}

    private void testDB(double limit){
        try {
            System.out.println("Verifying cards are not above "+ limit+"% similar");
            System.out.println("Starting card similarity verification of the database");
            Yaml yaml = new Yaml(new CardConstructor());

            InputStream is1 = getClass().getClassLoader().getResourceAsStream("assets/jumathsji");
            BufferedReader br = new BufferedReader(new InputStreamReader(is1));

            assertNotEquals(null, is1);
            System.out.println("Cards directory successfully opened");

            Map<CardYaml, CardYaml> simCards = new HashMap<>();
            int count = 0;
            String filename = null;

            while ((filename = br.readLine()) != null) {
                InputStream is2 = getClass().getClassLoader().getResourceAsStream("assets/jumathsji/" + filename);
                assertNotEquals(null, is2);

                System.out.println("Verifying file : " + filename);

                List<CardYaml> db = yaml.load(is2);

                for (CardYaml a : db) {
                    for (CardYaml b : db) {
                        similarityComputing.similarCards(a, b, simCards, limit);
                    }
                }

                    is2.close();
            }

            if (simCards.size() == 0){
                System.out.println("Database checked. No identical cards found");
            }
            else{
                int cpt = 0;
                for (CardYaml i : simCards.keySet()){
                    String idA = i.id;
                    String idB = simCards.get(i).id;
                    if (!idA.equals(idB)) {
                        cpt++;
                        System.out.println(idA + " " + idB);
                    }

                }
                System.out.println("There is "+ cpt+ " similarites");
            }

            is1.close();
        } catch (IOException e) {

        }
    }

    private void testOnDB(String filename, double limit){
        try{
            System.out.println("Verifying cards are not above "+ limit+"% similar");
            System.out.println("Starting card similarity verification of the database");
            Yaml yaml = new Yaml(new CardConstructor());

            InputStream is1 = getClass().getClassLoader().getResourceAsStream("assets/jumathsji");
            BufferedReader br = new BufferedReader(new InputStreamReader(is1));

            assertNotEquals(null, is1);
            System.out.println("Cards directory successfully opened");

            Map<CardYaml, CardYaml> simCards = new HashMap<>();
            int count = 0;

            InputStream is2 = getClass().getClassLoader().getResourceAsStream("assets/jumathsji/" + filename);
            assertNotEquals(null, is2);

            System.out.println("Verifying file : " + filename);

            List<CardYaml> db = yaml.load(is2);

            for (CardYaml a : db) {
                for (CardYaml b : db) {
                    similarityComputing.similarCards(a, b, simCards, limit);
                }
            }

            is2.close();
            int cpt = 0;
            for (CardYaml i : simCards.keySet()){
                String idA = i.id;
                String idB = simCards.get(i).id;
                if (!idA.equals(idB)) {
                    cpt++;
                    System.out.println(idA + " " + idB);
                }

            }
            System.out.println("There is (are) "+ cpt+ " similarity(ies)");

            is1.close();
            System.out.println("");
        }
        catch (IOException e){

        }
    }
}
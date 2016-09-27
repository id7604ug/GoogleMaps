package com.anthony;
import com.google.maps.ElevationApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.ElevationResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

public class Main {
    static Scanner stringScanner = new Scanner(System.in);
    public static void main(String[] args) throws Exception{

        String key = null;
//        // Read key from file
//        try (BufferedReader reader = new BufferedReader(new FileReader("key.txt"))){
//            key = reader.readLine();
//            System.out.println(key); // Key check
//        } catch (Exception ioe) {
//            System.out.println("No key file found, or could not read key. Please verify key.txt present");
//            System.exit(-1); // Quit program - need to fix before continuing
//        }
        // Read key from file
        try (BufferedReader reader = new BufferedReader(new FileReader("key.txt"))){
            key = reader.readLine();
            System.out.println(key); // Keycheck
        } catch (Exception ioe){
            System.out.println("No key file found, or could not read key. Please verify key.txt present");
            System.exit(-1); // Quit program - need to fix before continuing
        }

        // Create a context
        // Do this one time at the start of your code, once you've read your key
        GeoApiContext context = new GeoApiContext().setApiKey(key);


        //We'll use ElevationAPI to request the elevation //todo implement finding the lat lng
        LatLng mctcLatLng = new LatLng(44.973074, -93.283356);
        ElevationResult[] results = ElevationApi.getByPoints(context, mctcLatLng).await();

        GeocodingResult[] results1 = GeocodingApi.geocode(context, "New York").await();

         //All APIs seem to return an array of results.
        // So we will expect an array with one result
        if (results.length >= 1){
            // Get first ElevationResult object
            ElevationResult mctcElevation = results[0];
            System.out.println("The elevation of MCTC above sea level is " + mctcElevation.elevation + " meters");
            // Let's do some rounding :)
            System.out.println(String.format("The elevation of MCTC above sea lever is %.2f meters.", mctcElevation.elevation));
        }
    }
}

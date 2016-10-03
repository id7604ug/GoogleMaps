package com.anthony;
// Import all the google maps things
import com.google.maps.ElevationApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.ElevationResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;

import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

public class Main {
    private static Scanner stringScanner = new Scanner(System.in);
    public static void main(String[] args) throws Exception{

        String key = null;
        // Read key from file
        try (BufferedReader reader = new BufferedReader(new FileReader("key.txt"))){
            key = reader.readLine();
//            System.out.println(key); // Keycheck
        } catch (Exception ioe){
            System.out.println("No key file found, or could not read key. Please verify key.txt present");
            System.exit(-1); // Quit program - need to fix before continuing
        }

        // Create a context for the GeoApiContext
        // Do this one time at the start of your code, once you've read your key
        GeoApiContext context = new GeoApiContext().setApiKey(key);
        System.out.println("What place would you like to find the elevation for? ");
        // Get the query from the user
        String address = stringScanner.nextLine();

        // Query google for geoCoded result(s)
        GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
        // Pull the full name of what the user most likely meant to search
        String foundAddress = results[0].formattedAddress;
        String place = results[0].formattedAddress.split(",")[0];
        // Pull the geometry from the result
        Geometry foundGeometry = results[0].geometry;

        // Create LatLng for found geometry
        LatLng foundLatLng = foundGeometry.location;
        ElevationResult[] foundElivation = ElevationApi.getByPoints(context, foundLatLng).await();

        //All APIs seem to return an array of results.
        // So we will expect an array with one result
        if (foundElivation.length >= 1){
            // Get first ElevationResult object
            ElevationResult elevation = foundElivation[0];
            // Output name of place
            System.out.println("I think you searched for: " + foundAddress);
//            System.out.println("The elevation of " + place +" above sea level is " + elevation.elevation + " meters");
            // Rounding
            System.out.println(String.format("The elevation of " + place + " above sea level is %.2f meters.", elevation.elevation));
        }
    }
}

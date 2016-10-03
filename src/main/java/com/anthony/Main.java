package com.anthony;
// Import all the google maps things
import com.google.maps.ElevationApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.ElevationResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import java.io.*;
import java.util.Scanner;

public class Main {
    private static Scanner stringScanner = new Scanner(System.in);
    private static Scanner numberScanner = new Scanner(System.in);
    public static void main(String[] args) throws Exception{

        String key = null;
        // Read key from file
        try (BufferedReader reader = new BufferedReader(new FileReader("key.txt"))){
            key = reader.readLine();
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
        // Create variable to store user choice
        int userChoice = -1;
        // Variable to track if the place was found
        boolean notFound = true;
        // Loop to continue until the user finds the place they are searching for
        while (notFound && results.length >= 1) {
            // Get users choice between the returned results
            userChoice = getUserChoice(results, userChoice);
            // Check if the user indicated they could not find their result
            if (userChoice == 0) {
                System.out.println("Add some more details for the search:");
                // Append users input to the query string
                address += " " + stringScanner.nextLine();
                // Query google again
                results = GeocodingApi.geocode(context, address).await();
            } else {
                // Fix user choice for array
                userChoice -= 1;
                // Pull the LatLng from the results
                LatLng foundLatLng = results[userChoice].geometry.location;
                // Query google for the elevation result using foundLatLng
                ElevationResult[] foundelevation = ElevationApi.getByPoints(context, foundLatLng).await();
                // Pull the first elevation
                ElevationResult elevation = foundelevation[0];
                // Output name of place
                System.out.println("You searched for: " + results[userChoice].formattedAddress);
                // Rounding
                System.out.println(String.format("The elevation of " + results[userChoice].formattedAddress + " above sea level is %.2f meters.", elevation.elevation));
                notFound = false;
            }
        }
        // Check if
        if (results.length < 1){
            System.out.println("We were unable to find the place you entered.");
            System.out.println("Please make sure you typed the name in correctly.");
        }
    }

    private static int getUserChoice(GeocodingResult[] results, int userChoice) {
        while (userChoice <= 0 || userChoice > results.length) {
            // Show results
            System.out.println("Type the number of your result: ");
            for (int i = 0; i < results.length; i++) {
                GeocodingResult result = results[i];
                System.out.println((i + 1) + ": " + result.formattedAddress);
            }
            System.out.println("0: My result is not here.");
            // Get users choice
            userChoice = numberScanner.nextInt();
            if (userChoice == 0){
                break;
            }
        }
        return userChoice;
    }
}

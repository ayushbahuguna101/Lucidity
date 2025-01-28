import java.util.*;

public class Main {

    public static void main(String[] args) {
        double speed = 20.0; // Speed assumption in km/h

        // All locations
        double[] amanLocation = {12.93, 77.62};
        double[] r1Location = {12.92, 77.63};
        double[] r2Location = {12.95, 77.64};
        double[] c1Location = {12.94, 77.66};
        double[] c2Location = {12.96, 77.65};

        double pt1 = 8.0; // Prep time for R1 in minutes
        double pt2 = 10.0; // Prep time for R2 in minutes

        Map<String, double[]> locations = new HashMap<>();
        locations.put("Aman", amanLocation);
        locations.put("R1", r1Location);
        locations.put("R2", r2Location);
        locations.put("C1", c1Location);
        locations.put("C2", c2Location);

        // Order mapping: customer to restaurant
        Map<String, String> customerToRestaurant = Map.of("C1", "R1", "C2", "R2");

        Set<String> restaurants = new HashSet<>(List.of("R1", "R2"));
        Set<String> customers = new HashSet<>(List.of("C1", "C2"));

        String currentLocation = "Aman"; // Start from Aman's location
        double totalTime = 0;

        while (!restaurants.isEmpty() || !customers.isEmpty()) {
            String nextLocation = null;
            double shortestTime = Double.MAX_VALUE;

            // next best location to visit
            for (String loc : restaurants) {
                double travelTime = calculateTravelTime(currentLocation, loc, locations, speed);
                if (travelTime < shortestTime) {
                    shortestTime = travelTime;
                    nextLocation = loc;
                }
            }

            for (String loc : customers) {
                String requiredRestaurant = customerToRestaurant.get(loc);
                if (!restaurants.contains(requiredRestaurant)) { // Only deliver if the food is picked up
                    double travelTime = calculateTravelTime(currentLocation, loc, locations, speed);
                    if (travelTime < shortestTime) {
                        shortestTime = travelTime;
                        nextLocation = loc;
                    }
                }
            }

            // Update state for the next iteration
            totalTime += shortestTime;
            currentLocation = nextLocation;

            // Mark visited locations as done
            restaurants.remove(nextLocation);
            customers.remove(nextLocation);
        }

        System.out.println("Total delivery time: " + totalTime * 60 + " minutes");
    }

    private static double calculateTravelTime(String current, String next, Map<String, double[]> locations, double speed) {
        double[] currentLoc = locations.get(current);
        double[] nextLoc = locations.get(next);
        double distance = haversine(currentLoc[0], currentLoc[1], nextLoc[0], nextLoc[1]);
        return distance / speed;
    }

    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // Radius of Earth in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c; // Distance in kilometers
    }
}

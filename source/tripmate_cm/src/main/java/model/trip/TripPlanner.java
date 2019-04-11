package model.trip;

import model.poi.POI;

/**
 * Created by Jingwu Xu on 2019-04-04
 */
abstract class TripPlanner {
    Trip trip;
    double[][] distances;

    TripPlanner(Trip trip) {
        this.trip = trip;
        computeDistance();
        System.out.println("TripPlanner");
        for (int i = 0; i < distances.length; i++){
            for (int j = 0; j < distances[i].length; j++) {
                System.out.print(distances[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Takes in a Trip object, plan trip accordingly and return the trip order
     * @return An integer array that indicates the order of the trip
     */
    abstract int[] planTrip();

    /**
     * Compute the distance table between POIs in the list
     */
    private void computeDistance() {
        distances = new double[trip.size][trip.size];
        System.out.println("TripPlanner size:" + trip.size);
        for(int i = 0; i < trip.size; i++) {
            for(int j = i; j < trip.size; j++) {
                POI poi_1 = trip.pList.get(i);
                POI poi_2 = trip.pList.get(j);
                distances[i][j] = distances[j][i] = distance(poi_1.lat.doubleValue(), poi_2.lat.doubleValue(),
                                                             poi_1.lng.doubleValue(), poi_2.lng.doubleValue(),
                                                             0, 0);
            }
        }
    }

    /**
     * https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
     * @param lat1 latitude of place 1 in double format
     * @param lat2 latitude of place 2 in double format
     * @param lon1 longitude of place 1 in double format
     * @param lon2 longitude of place 2 in double format
     * @param el1 height of place 1 in double format
     * @param el2 height of place 2 in doubel format
     * @return euclidean distance
     */
    private static double distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
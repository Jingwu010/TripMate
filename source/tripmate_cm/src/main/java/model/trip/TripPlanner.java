package model.trip;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by Jingwu Xu on 2019-04-04
 */
class TripPlanner {

    private TripPlanner() {}

    /**
     * Takes in a Trip object, plan trip accordingly and return the trip order
     * @param trip Trip object that contains a list of POIs
     * @return An integer array that indicates the order of the trip
     */
    static int[] planTrip(Trip trip) {
        return NearestNeighbour.planTrip(trip);
    }
}

// STATELESS OR STATEFUL?
class NearestNeighbour {
    private static Trip trip;

    static int[] planTrip(Trip trip) {
        NearestNeighbour.trip = trip;

        int[] best_route = null;
        double smallest_dist = Double.MAX_VALUE;
        for (int sIdx = 0; sIdx < trip.size; sIdx++) {
            int[] tmp_route = getNearestNeighbourRoute(sIdx);
            double tmp_dist = getDistance(tmp_route);
            if (smallest_dist > tmp_dist) {
                smallest_dist = tmp_dist;
                best_route = tmp_route;
            }
        }
        return best_route;
    }

    /**
     * Get the total euclidean distance of the route
     * @param route An integer array indicates the route
     * @return The total euclidean distance
     */
    private static double getDistance(int[] route) {
        double tot_dist = 0;
        for (int i = 1; i < route.length; i++) {
            tot_dist += trip.distances[route[i-1]][route[i]];
        }
        return tot_dist;
    }

    /**
     * Starting from a point return a route that covers all places
     * and return to the starting point according to the nearest neighbour algorithm
     * @param start An integer indicates the index of start place
     * @return An integer array indicates the route
     */
    private static int[] getNearestNeighbourRoute(int start) {
        int[] route = new int[trip.size+1];
        route[0] = start;
        route[trip.size] = start;
        for (int i = 1; i < trip.size; i++) {
            int next = getNearestNeighbour(route[i-1], Arrays.copyOfRange(route, 0, i));
            route[i] = next;
        }
        return route;
    }

    /**
     * Get the nearest neighbouring point of current location that not in the previous route
     * @param loc  Current location (index) in the trip
     * @param prev A list of previous locations (indexes) in the route
     * @return The index of nearest neighbouring point
     */
    private static int getNearestNeighbour(int loc, int[] prev) {
        double smallest_dist = Double.MAX_VALUE;
        int smallest_idx = 0;
        for (int i = 0; i < trip.size; i++) {
            if (i == loc || check(prev, i)) continue;
            if (smallest_dist > trip.distances[loc][i]) {
                smallest_dist = trip.distances[loc][i];
                smallest_idx = i;
            }
        }
        return smallest_idx;
    }

    /**
     * check if the specified element is present in the array or not
     * @param arr int array
     * @param toCheckValue the target value to check
     * @return boolean
     */
    private static boolean check(int[] arr, int toCheckValue) {
        return IntStream.of(arr).anyMatch(x -> x == toCheckValue);
    }
}
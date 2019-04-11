package model.trip;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by Jingwu Xu on 2019-04-11
 */
class NearestNeighbourPlanner extends TripPlanner{
    public NearestNeighbourPlanner(Trip trip) {
        super(trip);
    }

    @Override
    public int[] planTrip() {
        if (trip.start == null && trip.stop == null) {
            // if the start place and stop place are not provided
            // plan a trip with any start point and end after exploring all places
            return searchFull();
        } else if (trip.start != null && trip.stop == null) {
            // if start place is provided, the itinerary should return back to the start place
            return getNearestNeighbourRoute(trip.getLocation(trip.start), trip.getLocation(trip.start), trip.size+1);

        } else {
            // if start place and stop place are both provided, plan a trip
            return getNearestNeighbourRoute(trip.getLocation(trip.start), trip.getLocation(trip.stop), trip.size);
        }
    }

    private int[] searchFull() {
        int[] best_route = null;
        double smallest_dist = Double.MAX_VALUE;
        for (int sIdx = 0; sIdx < trip.size; sIdx++) {
            int[] tmp_route = Arrays.copyOfRange(getNearestNeighbourRoute(sIdx, sIdx, trip.size+1), 0, trip.size);
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
    private double getDistance(int[] route) {
        double tot_dist = 0;
        for (int i = 1; i < route.length; i++) {
            tot_dist += distances[route[i-1]][route[i]];
        }
        return tot_dist;
    }

    /**
     * Starting from a point return a route that covers all places
     * and return to the starting point according to the nearest neighbour algorithm
     * @param start An integer indicates the index of start place
     * @param stop An integer indicates the index of stop place
     * @param stop An integer indicates the size of the route
     * @return An integer array indicates the route
     */
    private int[] getNearestNeighbourRoute(int start, int stop, int size) {
        int[] route = new int[size];
        route[0] = start;
        route[size-1] = stop;
        for (int i = 1; i < size-1; i++) {
            int next = getNearestNeighbour(route[i-1],
                    Arrays.copyOfRange(route, 0, i),
                    Arrays.copyOfRange(route, route.length-1, route.length));
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
    private int getNearestNeighbour(int loc, int[] prev, int[] post) {
        double smallest_dist = Double.MAX_VALUE;
        int smallest_idx = 0;
        for (int i = 0; i < trip.size; i++) {
            if (i == loc || check(prev, i) || check(post, i)) continue;
            if (smallest_dist > distances[loc][i]) {
                smallest_dist = distances[loc][i];
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
    private boolean check(int[] arr, int toCheckValue) {
        return IntStream.of(arr).anyMatch(x -> x == toCheckValue);
    }
}
package model.trip;

import model.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jingwu Xu on 2019-04-04
 */
public class Trip {
    List<Location> locList;
    Location start;
    Location stop;
    int[] route;
    TripPlanner tp;

    /**
     * Construct the trip from a list of Locations
     * @param locList a list of Location objects that to be visited
     */
    public Trip(List<Location> locList) {
        this.locList = locList;
    }

    /**
     * Construct the trip from a list of Locations with specific start Location
     * The trip starts and ends at start Location
     * @param locList a list of Location objects that to be visited
     * @param start a start Location where trip starts and ends at
     */
    public Trip(List<Location> locList, Location start) {
        this(locList);
        this.start = start;
        if (getLocation(start)==-1) this.locList.add(start);
    }

    /**
     * Construct the trip from a list of Locations with specific start Location and stop Location
     * The trip starts at start and ends at stop
     * @param locList a list of Location objects that to be visited
     * @param start a start Location where trip starts from
     * @param stop a start Location where trip ends at
     */
    public Trip(List<Location> locList, Location start, Location stop) {
        this(locList, start);
        this.stop = stop;
        if (getLocation(stop)==-1) this.locList.add(stop);
    }

    int getLocation(Location loc) {
        for (int i = 0; i < locList.size(); i++) {
            if (loc.equals(locList.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public List<Location> planTrip() {
        tp = new NearestNeighbourPlanner(this);
        route = tp.planTrip();

        List<Location> sorted_trip = new ArrayList<>();
        for (int i = 0; i < route.length; i++) {
            sorted_trip.add(locList.get(route[i]));
        }
        return sorted_trip;
    }
}

package services;

import java.io.Serializable;
import entities.Route;
import entities.Vehicle;
import java.io.Serializable;
import java.util.List;

public class BackupData implements Serializable {
    private List<Route> routes;
    private List<Vehicle> vehicles;

    public BackupData(List<Route> routes, List<Vehicle> vehicles) {
        this.routes = routes;
        this.vehicles = vehicles;
    }
    public List<Route> getRoutes() {
        return routes;
    }
    public List<Vehicle> getVehicles() {
        return vehicles;
    }
}

package autoMaintProgram;

import org.springframework.data.annotation.Id;

public class VehicleEntity {
    @Id
    private String vehicleId;

    private String garageId;
    private String vehicleName;
    private String vehicleYear;
    private String vehicleMake;
    private String vehicleModel;

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleYear(String vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public String getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setGarageId(String garageId) {
        this.garageId = garageId;
    }

    public String getGarageId() {
        return garageId;
    }
}

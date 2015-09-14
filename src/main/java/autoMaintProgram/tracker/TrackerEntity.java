package autoMaintProgram.tracker;

import org.springframework.data.annotation.Id;

public class TrackerEntity {

    @Id
    String id;
    private String vehicleId;
    private String name;
    private String description;
    private String priority;
    private Byte pendingStatus;


    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Byte getPendingStatus() {
        return pendingStatus;
    }

    public void setPendingStatus(Byte pendingStatus) {
        this.pendingStatus = pendingStatus;
    }
}

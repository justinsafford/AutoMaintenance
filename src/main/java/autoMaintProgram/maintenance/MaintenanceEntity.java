package autoMaintProgram.maintenance;

import org.springframework.data.annotation.Id;

public class MaintenanceEntity {

    @Id
    private String id;

    private String type;
    private String description;
    private String mileageExpected;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMileageExpected() {
        return mileageExpected;
    }

    public void setMileageExpected(String mileageExpected) {
        this.mileageExpected = mileageExpected;
    }

}

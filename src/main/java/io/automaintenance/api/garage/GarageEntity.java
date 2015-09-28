package io.automaintenance.api.garage;

import org.springframework.data.annotation.Id;

public class GarageEntity {

    @Id
    private String garageId;

    private String garageName;

    public void setGarageName(String garageName) {
        this.garageName = garageName;
    }

    public String getGarageName() {
        return garageName;
    }

    public void setGarageId(String garageId) {
        this.garageId = garageId;
    }

    public String getGarageId() {
        return garageId;
    }
}

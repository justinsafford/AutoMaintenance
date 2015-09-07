package autoMaintProgram;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public class GarageEntity {

    @Id
    private UUID garageId;

    private String garageName;


    public void setGarageName(String garageName) {
        this.garageName = garageName;
    }

    public String getGarageName() {
        return garageName;
    }

    public void setGarageId(UUID garageId) {
        this.garageId = garageId;
    }

    public UUID getGarageId() {
        return garageId;
    }
}

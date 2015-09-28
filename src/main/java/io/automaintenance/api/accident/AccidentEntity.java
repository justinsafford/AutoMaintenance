package io.automaintenance.api.accident;

import org.springframework.data.annotation.Id;

public class AccidentEntity {

    @Id
    private String id;

    private String vehicleId;
    private String type;
    private String description;
    private String damageLevel;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDamageLevel(String damageLevel) {
        this.damageLevel = damageLevel;
    }

    public String getDamageLevel() {
        return damageLevel;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }
}

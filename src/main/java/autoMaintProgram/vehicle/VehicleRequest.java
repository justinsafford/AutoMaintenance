package autoMaintProgram.vehicle;

public class VehicleRequest {
    private String name;
    private String year;
    private String make;
    private String model;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getMake() {
        return make;
    }


    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }
}

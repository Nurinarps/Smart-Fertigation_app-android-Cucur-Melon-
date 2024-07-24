package com.example.cucurmelo.data.models;

public class sensors {

    private double moisture1;
    private double moisture2;
    private double moisture3;
    private double moisture4;
    private double moisture5;
    private double moisture6;
    private double flowRate1;
    private double flowRate2;
    private double flowRate3;
    private double temperature;
    private double lux;
    private String timestamp; // Menambahkan atribut timestamp

    // No-argument constructor required for Firebase
    public sensors() {
    }

    // Constructor with all fields
    public sensors(double moisture1, double moisture2, double moisture3, double moisture4, double moisture5, double moisture6,
                   double flowRate1, double flowRate2, double flowRate3, double temperature, double lux, String timestamp) {
        this.moisture1 = moisture1;
        this.moisture2 = moisture2;
        this.moisture3 = moisture3;
        this.moisture4 = moisture4;
        this.moisture5 = moisture5;
        this.moisture6 = moisture6;
        this.flowRate1 = flowRate1;
        this.flowRate2 = flowRate2;
        this.flowRate3 = flowRate3;
        this.temperature = temperature;
        this.lux = lux;
        this.timestamp = timestamp;
    }

    // Getters and setters for all fields
    public double getMoisture1() { return moisture1; }
    public void setMoisture1(double moisture1) { this.moisture1 = moisture1; }

    public double getMoisture2() { return moisture2; }
    public void setMoisture2(double moisture2) { this.moisture2 = moisture2; }

    public double getMoisture3() { return moisture3; }
    public void setMoisture3(double moisture3) { this.moisture3 = moisture3; }

    public double getMoisture4() { return moisture4; }
    public void setMoisture4(double moisture4) { this.moisture4 = moisture4; }

    public double getMoisture5() { return moisture5; }
    public void setMoisture5(double moisture5) { this.moisture5 = moisture5; }

    public double getMoisture6() { return moisture6; }
    public void setMoisture6(double moisture6) { this.moisture6 = moisture6; }

    public double getFlowRate1() { return flowRate1; }
    public void setFlowRate1(double flowRate1) { this.flowRate1 = flowRate1; }

    public double getFlowRate2() { return flowRate2; }
    public void setFlowRate2(double flowRate2) { this.flowRate2 = flowRate2; }

    public double getFlowRate3() { return flowRate3; }
    public void setFlowRate3(double flowRate3) { this.flowRate3 = flowRate3; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public double getLux() { return lux; }
    public void setLux(double lux) { this.lux = lux; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}

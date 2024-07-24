package com.example.cucurmelo.data.models;

public class relays {

    private boolean relay1;
    private boolean relay2;
    private boolean relay3;

    // Default constructor required for calls to DataSnapshot.getValue(Relays.class)
    public relays() {
    }

    public relays(boolean relay1, boolean relay2, boolean relay3) {
        this.relay1 = relay1;
        this.relay2 = relay2;
        this.relay3 = relay3;
    }

    public boolean isRelay1() {
        return relay1;
    }

    public void setRelay1(boolean relay1) {
        this.relay1 = relay1;
    }

    public boolean isRelay2() {
        return relay2;
    }

    public void setRelay2(boolean relay2) {
        this.relay2 = relay2;
    }

    public boolean isRelay3() {
        return relay3;
    }

    public void setRelay3(boolean relay3) {
        this.relay3 = relay3;
    }
}

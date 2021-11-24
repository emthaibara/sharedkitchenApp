package com.dachuang.signserviceprovider.pojo;
import java.util.Objects;

public class RegisteredSchool {

    private String registeredschool;

    public RegisteredSchool() {
    }

    public RegisteredSchool(String registeredschool) {
        this.registeredschool = registeredschool;
    }

    @Override
    public String toString() {
        return "RegisteredSchool{" +
                "registeredschool='" + registeredschool + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisteredSchool that = (RegisteredSchool) o;
        return Objects.equals(registeredschool, that.registeredschool);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registeredschool);
    }

    public String getRegisteredschool() {
        return registeredschool;
    }

    public void setRegisteredschool(String registeredschool) {
        this.registeredschool = registeredschool;
    }
}
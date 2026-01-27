package model;

import java.util.UUID;

public class ProtectedObject {
    private UUID id;
    private String name;
    private String address;
    private String type;

    public ProtectedObject() {
        this.id = UUID.randomUUID();
    }

    public ProtectedObject(UUID id, String name, String address, String type) {
        this.id = id != null ? id : UUID.randomUUID();
        this.name = name;
        this.address = address;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class Builder {
        private String name;
        private String address;
        private String type;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public ProtectedObject build() {
            return new ProtectedObject(null, name, address, type);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "Об'єкт: " + name + " (" + type + "), Адреса: " + address;
    }
}

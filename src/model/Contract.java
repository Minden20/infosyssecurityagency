package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import user.Guard;

public class Contract {
    private UUID id;
    private UUID clientId;
    private ProtectedObject protectedObject;
    private List<Guard> guards;
    private LocalDate startDate;
    private LocalDate endDate;
    private double price;
    private ContractStatus status;

    public enum ContractStatus {
        ACTIVE, TERMINATED, PENDING
    }

    public Contract() {
        this.id = UUID.randomUUID();
        this.guards = new ArrayList<>();
        this.status = ContractStatus.PENDING;
    }

    public Contract(UUID id, UUID clientId, ProtectedObject protectedObject, List<Guard> guards, 
                   LocalDate startDate, LocalDate endDate, double price, ContractStatus status) {
        this.id = id != null ? id : UUID.randomUUID();
        this.clientId = clientId;
        this.protectedObject = protectedObject;
        this.guards = guards;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.status = status != null ? status : ContractStatus.PENDING;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getClientId() { return clientId; }
    public void setClientId(UUID clientId) { this.clientId = clientId; }

    public ProtectedObject getProtectedObject() {
        return protectedObject;
    }

    public void setProtectedObject(ProtectedObject protectedObject) {
        this.protectedObject = protectedObject;
    }

    public List<Guard> getGuards() {
        return guards;
    }

    public void setGuards(List<Guard> guards) {
        this.guards = guards;
    }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public ContractStatus getStatus() { return status; }
    public void setStatus(ContractStatus status) { this.status = status; }

    public static class Builder {
        private UUID id;
        private UUID clientId;
        private ProtectedObject protectedObject;
        private List<Guard> guards = new ArrayList<>();
        private LocalDate startDate;
        private LocalDate endDate;
        private double price;
        private ContractStatus status = ContractStatus.PENDING;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder clientId(UUID clientId) { this.clientId = clientId; return this; }
        public Builder protectedObject(ProtectedObject protectedObject) {
            this.protectedObject = protectedObject;
            return this;
        }

        public Builder guards(List<Guard> guards) {
            this.guards = guards;
            return this;
        }
        
        public Builder addGuard(Guard guard) {
            this.guards.add(guard);
            return this;
        }

        public Builder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public Builder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public Builder price(double price) { this.price = price; return this; }
        public Builder status(ContractStatus status) { this.status = status; return this; }

        public Contract build() {
            return new Contract(id, clientId, protectedObject, guards, startDate, endDate, price, status);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}

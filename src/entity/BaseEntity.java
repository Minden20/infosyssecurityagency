package entity;

import java.util.UUID;

public class BaseEntity implements IEntity {
    private final UUID id;
    private final String firstName;
    private final String secondName;
    private final String thirdName;
    private final String email;

    public BaseEntity(String firstName, String secondName, String thirdName, String email) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.email = email;
    }

    @Override
    public UUID getId() {
        return id;
    }
    
    @Override
    public String getFirstName() {
        return firstName;
    }
    
    @Override
    public String getSecondName() {
        return secondName;
    }
    
    @Override
    public String getThirdName() {
        return thirdName;
    }
    
    @Override
    public String getEmail() {
        return email;
    }
}

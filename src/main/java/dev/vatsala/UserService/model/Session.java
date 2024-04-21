package dev.vatsala.UserService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity(name ="Session")
@Getter
@Setter
public class Session extends BaseModel{

    private String token;
    private Date ExpiringAt;
    private Date loginAt;

    @ManyToOne
    private Users user;

    @Enumerated
    private SessionStatus sessionStatus;

}

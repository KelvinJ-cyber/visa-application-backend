package com.kelvin.visa_application_site.Admin.model;
import com.kelvin.visa_application_site.Users.model.Users;
import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    private String message;
    private String type;
    private LocalDateTime time;
    private boolean read;

    public Notification(int notificationId, LocalDateTime time, String type, String message, Users user) {
        this.id = notificationId;
        this.time = time;
        this.type = type;
        this.message = message;
        this.user = user;
    }

    public Notification(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public boolean isRead() {
        return read;
    }
}

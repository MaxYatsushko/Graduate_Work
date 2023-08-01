package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String phone;
    private LocalDate regDate;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    private String role;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Collection<Ad> userAds;

    public User(String email, String firstName, String lastName, String password, String phone,
                LocalDate regDate, Image image, String role) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.phone = phone;
        this.regDate = regDate;
        this.image = image;
        this.role = role;
    }

    public User() {
    }

    public String getPassword() {
        return password;
    }

    public CharSequence getPasswordCharSeq() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Collection<Ad> getUserAds() {
        return userAds;
    }

    public void setUserAds(Collection<Ad> userAds) {
        this.userAds = userAds;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDate regDate) {
        this.regDate = regDate;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }


    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", id=" + id +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", regDate='" + regDate + '\'' +
                ", image='" + image + '\'' +
                ", role='" + role + '\'' +
                ", userAds=" + userAds +
                '}';
    }
}
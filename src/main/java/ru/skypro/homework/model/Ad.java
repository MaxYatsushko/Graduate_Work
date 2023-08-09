package ru.skypro.homework.model;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.OnDeleteAction;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "ads")
public class Ad {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name ="image_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Image image;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int price;
    private String title;
    private String description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "id", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Comment> adsComments;

    public Ad(){}


    public Ad(User user, CreateOrUpdateAdDto newAd){
        this.author = user;
        this.price = newAd.getPrice();
        this.title = newAd.getTitle();
        this.description = newAd.getDescription();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Image  getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

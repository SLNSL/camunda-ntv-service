package ru.ntv.entity.articles;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import ru.ntv.entity.articles.Article;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "theme")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "theme_name")
    private String themeName;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "article_theme",
            joinColumns = {@JoinColumn(name = "theme_id")},
            inverseJoinColumns = {@JoinColumn(name = "article_id")}
    )
    private List<Article> articles;
}

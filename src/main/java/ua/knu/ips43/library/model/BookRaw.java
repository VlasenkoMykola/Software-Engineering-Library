package ua.knu.ips43.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRaw {
    @Id
    private int id;
    private String title;
    //private String annotation;
    //private String genres;
    private String description;
    private String lang;
    // private int year;
}

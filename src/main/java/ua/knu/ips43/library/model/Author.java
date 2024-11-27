package ua.knu.ips43.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    private int id;
    //private String letter;
    //private String search_name;
    private String full_name;
    private String first_name;
    private String middle_name;
    private String last_name;
    private String description;
}

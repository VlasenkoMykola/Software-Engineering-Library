package ua.knu.ips43.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
//import java.sql.Timestamp;

import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reader {
    // reader id is also used as library card id
    @Id
    private int id;
    private String first_name;
    private String middle_name;
    private String last_name;
    private String address;
    private String phone;
    private String email;
    private Date registration_date;
    private Date valid_to;
}

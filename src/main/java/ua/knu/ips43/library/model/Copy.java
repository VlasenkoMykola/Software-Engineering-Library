package ua.knu.ips43.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Copy {
    @Id
    private int id;
    private int book_id;
    private Integer reader_id;
    private CopyStatus status;
    private Timestamp status_change;
    private Date book_or_lend_from;
    private Date book_or_lend_to;
}

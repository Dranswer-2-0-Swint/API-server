package com.t3q.dranswer.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class LoggingFilterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private String header;
    @Column(nullable = true)
    private String queryString;
    @Column(nullable = true)
    private String RequestBody;
    @Column(nullable = true)
    private String ResponseBody;
}

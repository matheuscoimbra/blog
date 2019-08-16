package com.mc.blog.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
@Entity
public class Estatistica implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "estatistica_id",nullable = false)
    private Long id;

    @Column()
    private Long categoria;

    @Column()
    private Long usuarios;

    @Column()
    private Long artigos;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Column()
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

}

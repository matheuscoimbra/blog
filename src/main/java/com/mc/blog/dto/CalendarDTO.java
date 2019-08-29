package com.mc.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data @Builder
@EqualsAndHashCode
public class CalendarDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    private Long id;
    private String title;

    private Date startDate;
}
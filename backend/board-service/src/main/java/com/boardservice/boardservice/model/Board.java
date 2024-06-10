package com.boardservice.boardservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "board")
public class Board {

    @Id
    private Integer boardSeq;

    private String boardTitle;

    private String boardContent;

    private Integer userSeq;
}

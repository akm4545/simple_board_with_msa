package com.boardservice.boardservice.dto;

import com.boardservice.boardservice.model.Board;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardRequestDto {
    private Integer boardSeq;

    private String boardTitle;

    private String boardContent;
}

package com.boardservice.boardservice.dto;

import com.boardservice.boardservice.model.Board;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardResponseDto {
    private Integer boardSeq;

    private String boardTitle;

    private String boardContent;

    public BoardResponseDto(Board board){
        this.boardSeq = board.getBoardSeq();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
    }
}

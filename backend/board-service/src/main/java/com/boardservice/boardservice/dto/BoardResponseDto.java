package com.boardservice.boardservice.dto;

import com.boardservice.boardservice.model.Board;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
//HATEOAS 구현을 위해 상속
public class BoardResponseDto extends RepresentationModel<BoardResponseDto> {
    private Integer boardSeq;

    private String boardTitle;

    private String boardContent;

    public BoardResponseDto(Board board){
        this.boardSeq = board.getBoardSeq();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
    }
}

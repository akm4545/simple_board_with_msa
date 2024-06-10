package com.boardservice.boardservice.dto.board;

import com.boardservice.boardservice.dto.reply.ReplyResponseDto;
import com.boardservice.boardservice.dto.user.UserResponseDto;
import com.boardservice.boardservice.model.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
//HATEOAS 구현을 위해 상속
public class BoardResponseDto extends RepresentationModel<BoardResponseDto> {
    private Integer boardSeq;

    private String boardTitle;

    private String boardContent;

    private Integer userSeq;

    private String userId;

    private List<ReplyResponseDto> replyList;

    public BoardResponseDto(Board board){
        this.boardSeq = board.getBoardSeq();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
    }

    public BoardResponseDto(Board board, UserResponseDto user){
        this.boardSeq = board.getBoardSeq();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.userId =user.getUserId();
    }

    public BoardResponseDto(Board board, UserResponseDto user, List<ReplyResponseDto> replyList){
        this.boardSeq = board.getBoardSeq();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.userSeq = board.getBoardSeq();
        this.userId = user.getUserId();
        this.replyList = replyList;
    }
}

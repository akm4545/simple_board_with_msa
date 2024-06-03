package com.boardservice.boardservice.controller;

import com.boardservice.boardservice.dto.BoardRequestDto;
import com.boardservice.boardservice.dto.BoardResponseDto;
import com.boardservice.boardservice.dto.BoardSeqRequestDto;
import com.boardservice.boardservice.model.Board;
import com.boardservice.boardservice.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//HATEOAS 사용을 위해 import
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board/{boardSeq}")
    public ResponseEntity<BoardResponseDto> selectBoard(@PathVariable Integer boardSeq) {
        BoardSeqRequestDto requestDto = BoardSeqRequestDto.builder()
                .boardSeq(boardSeq)
                .build();

        BoardResponseDto responseDto = boardService.selectBoard(requestDto);

//        HATEOAS 설정
        responseDto.add(linkTo(methodOn(BoardController.class)
                .selectBoard(boardSeq))
                .withSelfRel(),
                linkTo(methodOn(BoardController.class)
                        .selectBoardList())
                        .withRel("selectBoardList"),
                linkTo(methodOn(BoardController.class)
                        .insertBoard(BoardRequestDto.builder().build()))
                        .withRel("insertBoard"),
                linkTo(methodOn(BoardController.class)
                        .updateBoard(boardSeq, BoardRequestDto.builder().build()))
                        .withRel("updateBoard"),
                linkTo(methodOn(BoardController.class)
                        .deleteBoard(boardSeq))
                        .withRel("deleteBoard"));

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/board/list")
    public ResponseEntity<List<BoardResponseDto>> selectBoardList(){
        List<BoardResponseDto> responseDtoList = boardService.selectBoardList();

        responseDtoList.forEach(response -> response.add(linkTo(methodOn(BoardController.class)
                        .selectBoard(response.getBoardSeq()))
                        .withRel("selectBoard"),
                linkTo(methodOn(BoardController.class)
                        .selectBoardList())
                        .withSelfRel(),
                linkTo(methodOn(BoardController.class)
                        .insertBoard(BoardRequestDto.builder().build()))
                        .withRel("insertBoard"),
                linkTo(methodOn(BoardController.class)
                        .updateBoard(response.getBoardSeq(), BoardRequestDto.builder().build()))
                        .withRel("updateBoard"),
                linkTo(methodOn(BoardController.class)
                        .deleteBoard(BoardRequestDto.builder().build().getBoardSeq()))
                        .withRel("deleteBoard")));

        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping("/board")
    public ResponseEntity<BoardResponseDto> insertBoard(@RequestBody BoardRequestDto requestDto){
        BoardResponseDto responseDto = boardService.insertBoard(requestDto);

        responseDto.add(linkTo(methodOn(BoardController.class)
                        .selectBoard(requestDto.getBoardSeq()))
                        .withRel("selectBoard"),
                linkTo(methodOn(BoardController.class)
                        .selectBoardList())
                        .withRel("selectBoardList"),
                linkTo(methodOn(BoardController.class)
                        .insertBoard(BoardRequestDto.builder().build()))
                        .withSelfRel(),
                linkTo(methodOn(BoardController.class)
                        .updateBoard(requestDto.getBoardSeq(), BoardRequestDto.builder().build()))
                        .withRel("updateBoard"),
                linkTo(methodOn(BoardController.class)
                        .deleteBoard(requestDto.getBoardSeq()))
                        .withRel("deleteBoard"));

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/board/{boardSeq}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Integer boardSeq,@RequestBody BoardRequestDto requestDto){
        requestDto.setBoardSeq(boardSeq);

        BoardResponseDto responseDto = boardService.updateBoard(requestDto);

        responseDto.add(linkTo(methodOn(BoardController.class)
                        .selectBoard(requestDto.getBoardSeq()))
                        .withRel("selectBoard"),
                linkTo(methodOn(BoardController.class)
                        .selectBoardList())
                        .withRel("selectBoardList"),
                linkTo(methodOn(BoardController.class)
                        .insertBoard(BoardRequestDto.builder().build()))
                        .withRel("insertBoard"),
                linkTo(methodOn(BoardController.class)
                        .updateBoard(requestDto.getBoardSeq(), BoardRequestDto.builder().build()))
                        .withSelfRel(),
                linkTo(methodOn(BoardController.class)
                        .deleteBoard(requestDto.getBoardSeq()))
                        .withRel("deleteBoard"));

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/board/{boardSeq}")
    public ResponseEntity<BoardResponseDto> deleteBoard(@PathVariable Integer boardSeq){
        BoardSeqRequestDto requestDto = BoardSeqRequestDto.builder()
                .boardSeq(boardSeq)
                .build();

        boardService.deleteBoard(requestDto);

        BoardResponseDto responseDto = BoardResponseDto.builder().build();

        responseDto.add(linkTo(methodOn(BoardController.class)
                        .selectBoard(requestDto.getBoardSeq()))
                        .withRel("selectBoard"),
                linkTo(methodOn(BoardController.class)
                        .selectBoardList())
                        .withRel("selectBoardList"),
                linkTo(methodOn(BoardController.class)
                        .insertBoard(BoardRequestDto.builder().build()))
                        .withRel("insertBoard"),
                linkTo(methodOn(BoardController.class)
                        .updateBoard(requestDto.getBoardSeq(), BoardRequestDto.builder().build()))
                        .withRel("updateBoard"),
                linkTo(methodOn(BoardController.class)
                        .deleteBoard(requestDto.getBoardSeq()))
                        .withSelfRel());

        return ResponseEntity.ok(responseDto);
    }
}

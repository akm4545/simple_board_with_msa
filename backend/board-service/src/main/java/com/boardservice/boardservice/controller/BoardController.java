package com.boardservice.boardservice.controller;

import com.boardservice.boardservice.dto.BoardRequestDto;
import com.boardservice.boardservice.dto.BoardResponseDto;
import com.boardservice.boardservice.dto.BoardSeqRequestDto;
import com.boardservice.boardservice.model.Board;
import com.boardservice.boardservice.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/board/list")
    public ResponseEntity<List<BoardResponseDto>> selectBoardList(){
        List<BoardResponseDto> responseDtoList = boardService.selectBoardList();

        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping("/board")
    public ResponseEntity<BoardResponseDto> insertBoard(@RequestBody BoardRequestDto requestDto){
        BoardResponseDto responseDto = boardService.insertBoard(requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/board/{boardSeq}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Integer boardSeq,@RequestBody BoardRequestDto requestDto){
        requestDto.setBoardSeq(boardSeq);

        BoardResponseDto responseDto = boardService.updateBoard(requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/board/{boardSeq}")
    public void deleteBoard(@PathVariable Integer boardSeq){
        BoardSeqRequestDto requestDto = BoardSeqRequestDto.builder()
                .boardSeq(boardSeq)
                .build();

        boardService.deleteBoard(requestDto);

    }
}

package com.boardservice.boardservice.controller;

import com.boardservice.boardservice.model.Board;
import com.boardservice.boardservice.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board/{boardSeq}")
    public Board selectBoard(@PathVariable Integer boardSeq) {
        return boardService.selectBoard(boardSeq);
    }

    @GetMapping("/board/list")
    public List<Board> selectBoardList(){
        return boardService.selectBoardList();
    }

    @PostMapping("/board")
    public Board insertBoard(@RequestBody Board board){
        return boardService.insertBoard(board);
    }

    @PutMapping("/board/{boardSeq}")
    public Board updateBoard(@PathVariable Integer boardSeq,@RequestBody Board board){
        board.setBoardSeq(boardSeq);

        return boardService.updateBoard(board);
    }

    @DeleteMapping("/board/{boardSeq}")
    public void deleteBoard(@PathVariable Integer boardSeq){
        boardService.deleteBoard(boardSeq);
    }
}

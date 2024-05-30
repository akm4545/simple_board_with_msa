package com.boardservice.boardservice.service;

import com.boardservice.boardservice.model.Board;
import com.boardservice.boardservice.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public Board selectBoard(Integer boardSeq) {
        return boardRepository.findById(boardSeq).get();
    }

    public List<Board> selectBoardList() {
        return boardRepository.findAll();
    }

    @Transactional
    public Board insertBoard(Board board) {
        return boardRepository.save(board);
    }

    @Transactional
    public Board updateBoard(Board board) {
        Board updateBoard = boardRepository.findById(board.getBoardSeq()).get();

        updateBoard.setBoardTitle(board.getBoardTitle());
        updateBoard.setBoardContent(board.getBoardContent());

        return updateBoard;
    }

    @Transactional
    public void deleteBoard(Integer boardSeq) {
        boardRepository.deleteById(boardSeq);
    }
}

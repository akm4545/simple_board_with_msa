package com.boardservice.boardservice.service;

import com.boardservice.boardservice.dto.BoardRequestDto;
import com.boardservice.boardservice.dto.BoardResponseDto;
import com.boardservice.boardservice.dto.BoardSeqRequestDto;
import com.boardservice.boardservice.model.Board;
import com.boardservice.boardservice.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardResponseDto selectBoard(BoardSeqRequestDto requestDto) {
        Board board = boardRepository.findById(requestDto.getBoardSeq()).get();

        BoardResponseDto responseDto = new BoardResponseDto(board);

        return responseDto;
    }

    public List<BoardResponseDto> selectBoardList() {
        List<Board> boardList = boardRepository.findAll();

        List<BoardResponseDto> responseDtoList = boardList.stream().map(BoardResponseDto::new).toList();

        return responseDtoList;
    }

    @Transactional
    public BoardResponseDto insertBoard(BoardRequestDto requestDto) {
        Board board = new Board();

        board.setBoardContent(requestDto.getBoardContent());
        board.setBoardTitle(requestDto.getBoardTitle());

        BoardResponseDto responseDto = new BoardResponseDto(boardRepository.save(board));

        return responseDto;
    }

    @Transactional
    public BoardResponseDto updateBoard(BoardRequestDto requestDto) {
        Board updateBoard = boardRepository.findById(requestDto.getBoardSeq()).get();

        updateBoard.setBoardTitle(requestDto.getBoardTitle());
        updateBoard.setBoardContent(requestDto.getBoardContent());

        BoardResponseDto responseDto = new BoardResponseDto(updateBoard);

        return responseDto;
    }

    @Transactional
    public void deleteBoard(BoardSeqRequestDto requestDto) {
        boardRepository.deleteById(requestDto.getBoardSeq());
    }
}

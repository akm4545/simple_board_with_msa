package com.boardservice.boardservice.service;

import com.boardservice.boardservice.dto.board.BoardRequestDto;
import com.boardservice.boardservice.dto.board.BoardResponseDto;
import com.boardservice.boardservice.dto.board.BoardSeqRequestDto;
import com.boardservice.boardservice.dto.reply.ReplyResponseDto;
import com.boardservice.boardservice.dto.user.UserListRequestDto;
import com.boardservice.boardservice.dto.user.UserResponseDto;
import com.boardservice.boardservice.model.Board;
import com.boardservice.boardservice.repository.BoardRepository;
import com.boardservice.boardservice.service.client.ReplyFeignClient;
import com.boardservice.boardservice.service.client.UserFeignClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    private final UserFeignClient userFeignClient;

    private final ReplyFeignClient replyFeignClient;

    @CircuitBreaker(name = "boardService", fallbackMethod = "buildFallbackBoard")
    @Bulkhead(name="bulkheadBoardService", fallbackMethod = "buildFallbackBoard")
    @Retry(name = "retryBoardService", fallbackMethod = "buildFallbackBoard")
    public BoardResponseDto selectBoard(BoardSeqRequestDto requestDto) {
        Board board = boardRepository.findById(requestDto.getBoardSeq()).get();
        ResponseEntity<UserResponseDto> userResponse = userFeignClient.selectUser(board.getUserSeq());
        ResponseEntity<List<ReplyResponseDto>> replyResponse = replyFeignClient.selectReplyList(board.getBoardSeq());

        UserResponseDto userResponseDto = userResponse.getBody();
        List<ReplyResponseDto> replyResponseDtoList = replyResponse.getBody();

        BoardResponseDto responseDto = new BoardResponseDto(board, userResponseDto, replyResponseDtoList);

        return responseDto;
    }

    @CircuitBreaker(name = "boardService", fallbackMethod = "buildFallbackBoardList")
    @Bulkhead(name="bulkheadBoardService", fallbackMethod = "buildFallbackBoardList")
    @Retry(name = "retryBoardService", fallbackMethod = "buildFallbackBoardList")
    public List<BoardResponseDto> selectBoardList() {
        List<Board> boardList = boardRepository.findAll();
        List<Integer> userSeqList = boardList.stream().map(Board::getUserSeq).toList();
        UserListRequestDto userRequestDto = UserListRequestDto.builder()
                .userSeqList(userSeqList)
                .build();

        ResponseEntity<List<UserResponseDto>> userListResponse = userFeignClient.selectUserList(userRequestDto);
        Map<Integer, UserResponseDto> userMap = userListResponse.getBody().stream().collect(Collectors.toMap(
                UserResponseDto::getUserSeq,
                value -> value
        ));

        List<BoardResponseDto> responseDtoList = boardList.stream()
                .map(board ->
                        new BoardResponseDto(board, userMap.get(board.getUserSeq()))
                )
                .toList();

        return responseDtoList;
    }

    @Transactional
    @CircuitBreaker(name = "boardService", fallbackMethod = "buildFallbackBoard")
    @Bulkhead(name="bulkheadBoardService", fallbackMethod = "buildFallbackBoard")
    @Retry(name = "retryBoardService", fallbackMethod = "buildFallbackBoard")
    public BoardResponseDto insertBoard(BoardRequestDto requestDto) {
        Board board = new Board();

        board.setBoardContent(requestDto.getBoardContent());
        board.setBoardTitle(requestDto.getBoardTitle());
        board.setUserSeq(requestDto.getUserSeq());

        BoardResponseDto responseDto = new BoardResponseDto(boardRepository.save(board));

        return responseDto;
    }

    @Transactional
    @CircuitBreaker(name = "boardService", fallbackMethod = "buildFallbackBoard")
    @Bulkhead(name="bulkheadBoardService", fallbackMethod = "buildFallbackBoard")
    @Retry(name = "retryBoardService", fallbackMethod = "buildFallbackBoard")
    public BoardResponseDto updateBoard(BoardRequestDto requestDto) {
        Board updateBoard = boardRepository.findById(requestDto.getBoardSeq()).get();

        updateBoard.setBoardTitle(requestDto.getBoardTitle());
        updateBoard.setBoardContent(requestDto.getBoardContent());

        BoardResponseDto responseDto = new BoardResponseDto(updateBoard);

        return responseDto;
    }

    @Transactional
    @CircuitBreaker(name = "boardService")
    @Bulkhead(name="bulkheadBoardService")
    @Retry(name = "retryBoardService")
    public void deleteBoard(BoardSeqRequestDto requestDto) {
        boardRepository.deleteById(requestDto.getBoardSeq());
    }

    private BoardResponseDto buildFallbackBoard(BoardSeqRequestDto requestDto, Throwable t) {
        return new BoardResponseDto();
    }

    private BoardResponseDto buildFallbackBoard(BoardRequestDto requestDto, Throwable t) {
        return new BoardResponseDto();
    }

    private List<BoardResponseDto> buildFallbackBoardList(Throwable t) {
        return List.of(new BoardResponseDto());
    }
}

package com.boardservice.boardservice.dto.board;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardSeqRequestDto {
    private Integer boardSeq;
}

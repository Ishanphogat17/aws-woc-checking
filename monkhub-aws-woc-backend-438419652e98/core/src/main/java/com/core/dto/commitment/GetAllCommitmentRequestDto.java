package com.core.dto.commitment;

import com.core.enums.FilterType;
import com.core.enums.SortBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllCommitmentRequestDto {
    private Integer pageNumber=0;
    private Integer pageSize=10;
    private FilterType filterType;
    private SortBy sortBy= SortBy.LATEST;
    private String searchQuery="";
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;
}

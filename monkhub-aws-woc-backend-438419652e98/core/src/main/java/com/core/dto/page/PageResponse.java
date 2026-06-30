package com.core.dto.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageResponse<T> {
    private T data;
    private Integer pageNumber;
    private Integer pageSize;
    private Boolean isLast;
    private Long totalElements;
    private Integer totalPages;
}
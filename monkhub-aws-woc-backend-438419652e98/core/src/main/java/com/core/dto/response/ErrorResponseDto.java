package com.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ErrorResponseDto implements Serializable {
    private String timestamp;
    private Boolean status;
    private String message;
    private String path;
}

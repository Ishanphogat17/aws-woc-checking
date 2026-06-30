package com.core.dto.storage;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCompressImageUrlRequestDto {
    private String imageUrl;
    private Integer width;
    private Integer height;
}

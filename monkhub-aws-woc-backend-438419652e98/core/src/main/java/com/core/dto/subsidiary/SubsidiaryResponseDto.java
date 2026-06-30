package com.core.dto.subsidiary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubsidiaryResponseDto implements Serializable {
    private UUID subsidiaryId;
    private String name;
    private String displayName;
}


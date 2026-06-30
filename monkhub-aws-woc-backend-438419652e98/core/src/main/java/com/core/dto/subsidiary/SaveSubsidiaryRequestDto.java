package com.core.dto.subsidiary;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveSubsidiaryRequestDto {
    private UUID subsidiaryId;

    @NotBlank(message = "Name must not be empty")
    private String name;

    @NotBlank(message = "Display Name must not be empty")
    private String displayName;
}


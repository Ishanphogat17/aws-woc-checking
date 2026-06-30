package com.core.dto.commitment;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCommitmentRequestDto {
    @NotEmpty(message = "commitmentIds list must not be empty")
    private Set<UUID> commitmentIds;
}

package com.core.repository;

import com.core.entity.Commitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface CommitmentRepository extends JpaRepository<Commitment, UUID> {

    @Query("""
        SELECT c FROM Commitment c
        WHERE (
            :name IS NULL 
            OR :name = '' 
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))
        )
        AND c.isActive = :isActive
      """)
    Page<Commitment> findCommitments(
            String name,
            boolean isActive,
            Pageable pageable
    );

    @Query("""
        SELECT c FROM Commitment c
        WHERE (
            :name IS NULL 
            OR :name = '' 
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))
        )
        AND c.isActive = :isActive
        AND c.createdAt BETWEEN :start AND :end
    """)
    Page<Commitment> findCommitments(
            String name,
            boolean isActive,
            ZonedDateTime start,
            ZonedDateTime end,
            Pageable pageable
    );

    List<Commitment> findByCommitmentIdInAndIsActive(Set<UUID> commitmentIds, boolean isActive);

}

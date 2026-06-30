package com.core.repository;

import com.core.entity.Subsidiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface SubsidiaryRepository extends JpaRepository<Subsidiary, UUID> {

    List<Subsidiary> findByIsActiveTrue();

    @Query("""
            select s
            from Subsidiary s
            where s.isActive = true
              and (
                 lower(s.name) like lower(concat('%', :searchQuery, '%'))
                 or lower(s.displayName) like lower(concat('%', :searchQuery, '%'))
              )
            """)
    List<Subsidiary> searchActiveSubsidiaries(@Param("searchQuery") String searchQuery);

    Optional<Subsidiary> findBySubsidiaryIdAndIsActiveTrue(UUID subsidiaryId);

    List<Subsidiary> findBySubsidiaryIdInAndIsActiveTrue(Set<UUID> subsidiaryIds);
}


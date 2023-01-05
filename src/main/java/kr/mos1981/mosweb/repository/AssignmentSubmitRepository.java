package kr.mos1981.mosweb.repository;

import kr.mos1981.mosweb.entity.AssignmentSubmit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentSubmitRepository extends JpaRepository<AssignmentSubmit, Long> {
    List<AssignmentSubmit> findByAssignId(Long assignId);

    Optional<AssignmentSubmit> findByAssignIdAndCreateBy(Long assignId, String createBy);
}

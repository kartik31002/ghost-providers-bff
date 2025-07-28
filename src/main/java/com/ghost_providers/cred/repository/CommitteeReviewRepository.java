package com.ghost_providers.cred.repository;

import com.ghost_providers.cred.model.CommitteeReview;
import com.ghost_providers.cred.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommitteeReviewRepository extends JpaRepository<CommitteeReview, Long> {
}


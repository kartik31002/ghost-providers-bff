package com.ghost_providers.cred.repository;

import com.ghost_providers.cred.model.AlertAndReminder;
import com.ghost_providers.cred.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertAndReminderRepository extends JpaRepository<AlertAndReminder, Long> {
}


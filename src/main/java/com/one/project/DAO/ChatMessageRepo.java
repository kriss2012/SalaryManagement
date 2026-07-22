package com.one.project.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.one.project.Employee.ChatMessage;

@Repository
public interface ChatMessageRepo extends JpaRepository<ChatMessage, Long> {
}

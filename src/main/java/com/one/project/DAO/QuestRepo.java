package com.one.project.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.one.project.Employee.Quest;

@Repository
public interface QuestRepo extends JpaRepository<Quest, Long> {
}

package com.one.project.Employee;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private int powerReward;
    private int requiredPower;
    private String status; // "AVAILABLE", "ACTIVE", "COMPLETED"
    private String participants; // Comma-separated employee IDs, e.g. "1001,1002"

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPowerReward() {
        return powerReward;
    }

    public void setPowerReward(int powerReward) {
        this.powerReward = powerReward;
    }

    public int getRequiredPower() {
        return requiredPower;
    }

    public void setRequiredPower(int requiredPower) {
        this.requiredPower = requiredPower;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public Quest(String title, String description, int powerReward, int requiredPower, String status, String participants) {
        this.title = title;
        this.description = description;
        this.powerReward = powerReward;
        this.requiredPower = requiredPower;
        this.status = status;
        this.participants = participants;
    }

    public Quest() {
    }

    // Helper: checks if employee ID is in the participant list
    public boolean hasParticipant(int empId) {
        if (participants == null || participants.isEmpty()) {
            return false;
        }
        String[] ids = participants.split(",");
        String target = String.valueOf(empId);
        for (String id : ids) {
            if (id.trim().equals(target)) {
                return true;
            }
        }
        return false;
    }

    // Helper: add participant
    public void addParticipant(int empId) {
        String target = String.valueOf(empId);
        if (participants == null || participants.isEmpty()) {
            participants = target;
        } else if (!hasParticipant(empId)) {
            participants = participants + "," + target;
        }
    }
}

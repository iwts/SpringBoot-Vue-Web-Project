package edu.simrobot.pojo.dto;

import edu.simrobot.pojo.StudentForWork;

public class Participant {
    private String name;
    private StudentForWork.Status status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StudentForWork.Status getStatus() {
        return status;
    }

    public void setStatus(StudentForWork.Status status) {
        this.status = status;
    }
}

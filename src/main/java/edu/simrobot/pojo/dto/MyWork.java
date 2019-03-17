package edu.simrobot.pojo.dto;

import edu.simrobot.pojo.StudentForWork;
import edu.simrobot.pojo.Work;

public class MyWork {
    private Work work;
    private StudentForWork.Status status;

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public StudentForWork.Status getStatus() {
        return status;
    }

    public void setStatus(StudentForWork.Status status) {
        this.status = status;
    }
}

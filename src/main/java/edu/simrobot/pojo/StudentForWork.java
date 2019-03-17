package edu.simrobot.pojo;

public class StudentForWork {
    public enum Status{
        COMPLETE,
        UNDONE,
        LEAVE
    }

    private String studentId;
    private String workId;
    private Status status;

    public StudentForWork(){}
    public StudentForWork(String studentId,String workId,Status status){
        this.studentId = studentId;
        this.workId = workId;
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

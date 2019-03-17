package edu.simrobot.pojo;

import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

public class Work {
    private String workId;
    private String name;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date deadLine;

    private String description;

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

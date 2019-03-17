package edu.simrobot.service;

import edu.simrobot.pojo.StudentForWork;
import edu.simrobot.pojo.Work;
import java.util.List;
import java.util.Map;

public interface WorkService {

    /**
     * 获取正在进行的所有工作，使用JSON返回
     * @return List<Work> 返回List，泛型是工作实体类
     * */
    List<Work> getWorkList();

    /**
     * 获取已结束的所有工作，使用JSON返回
     * 与方法 {@link WorkService#getWorkList()} 不同的是：
     * 该方法返回的是已到达截止时间的工作，而不是未完成工作，两者
     * 在数据库中应属于不同的表
     *
     * @return List<MyWork> 返回List，泛型是工作实体类
     * */
    List<Work> getCompleteWorkList();

    /**
     * 学生加入工作，专门使用传输类，封装了申请加入工作的相关信息
     * 使用JSON返回状态码
     *
     * @param apply 专用的传输类
     * @return String JSON返回状态码
     * */
    String join(StudentForWork apply);

    /**
     * 创建一个工作
     * @param work 提交一个工作类
     * */
    void create(Work work);

    /**
     * 删除一个工作
     * 由于有多表在使用其外键，所以在数据库上需设置级联删除
     *
     * @param work 提交工作实体类，其实主要是使用其中的 workId 属性
     * */
    void delete(Work work);

    /**
     * 认为一个工作已完成
     * 主要用于工作截止时间到期后，专门的定时器在检测到该工作已超期，
     * 调用该方法，使得工作从 work 表中删除，同时备份到已完成工作表
     * completeWork 中存放。用于区分到期和未到期工作。
     * 同时调用 {@link WorkService#delete(Work)} 方法对备份后的
     * 工作进行删除
     *
     * @param work 提交工作实体类，其实主要是使用其中的 workId 属性
     * */
    void complete(Work work);

    /**
     * 对一个工作获取参与该工作的所有学生信息
     * 工作又分为已经结束的工作（可能以后会存在历史工作）与正在进行的
     * 工作，所以在搜索的时候，算法应该一致，但调用的数据库中的表应该
     * 有明显区分。而利用 isComplete 属性能够区分具体是哪一种工作，
     * 从而区分对哪个表进行操作。
     * 在返回值上，使用JSON返回前端需要的数据，根据前端的不同，Map的
     * 内容也应随之变化
     *
     * @param workId 工作ID必须要有
     * @param isComplete true认为是已经完成的工作，false相反
     * @return Map<String,Object> 作为JSON对象响应前端
     * */
    Map<String,Object> getParticipants(String workId, boolean isComplete);
}

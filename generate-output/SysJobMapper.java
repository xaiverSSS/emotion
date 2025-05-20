package SysJobMapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;
public interface SysJobMapper { 

     @Insert("INSERT INTO sys_job (`id`, `name`, `last_run_time`, `last_success_time`, `times`, `faliure_times`, `success_times`, `total_seconds`, `ave_seconds`, `min_seconds`, `max_seconds`)VALUES (#{id}, #{name}, #{lastRunTime}, #{lastSuccessTime}, #{times}, #{faliureTimes}, #{successTimes}, #{totalSeconds}, #{aveSeconds}, #{minSeconds}, #{maxSeconds})")
     void insert(@Param("sysJobDO") SysJobDO sysJobDO);

     @Delete("DELETE FROM sys_jobWHERE (id = #{id})")
     void delete(@Param("id") Long id);

     @Select("SELECT id, name, last_run_time, last_success_time, times, faliure_times, success_times, total_seconds, ave_seconds, min_seconds, max_secondsFROM sys_jobWHERE (id = #{id})")
     SysJobDO select(@Param("id") Long id);

     @Update("UPDATE sys_jobSET `id` = #{id}, `name` = #{name}, `last_run_time` = #{lastRunTime}, `last_success_time` = #{lastSuccessTime}, `times` = #{times}, `faliure_times` = #{faliureTimes}, `success_times` = #{successTimes}, `total_seconds` = #{totalSeconds}, `ave_seconds` = #{aveSeconds}, `min_seconds` = #{minSeconds}, `max_seconds` = #{maxSeconds}WHERE (id = #{id})")
     void update(@Param("sysJobDO") SysJobDO sysJobDO);
}
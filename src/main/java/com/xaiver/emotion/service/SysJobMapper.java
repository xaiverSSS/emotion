package com.xaiver.emotion.service;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

public class SysJobMapper {

     @Insert("INSERT INTO sys_job
 (`id`, `name`, `last_run_time`, `last_success_time`, `times`, `faliure_times`, `success_times`, `total_seconds`, `ave_seconds`, `min_seconds`, `max_seconds`, `id`, `name`, `last_run_time`, `last_success_time`, `times`, `faliure_times`, `success_times`, `total_seconds`, `ave_seconds`, `min_seconds`, `max_seconds`)
VALUES (#{id}, #{name}, #{lastRunTime}, #{lastSuccessTime}, #{times}, #{faliureTimes}, #{successTimes}, #{totalSeconds}, #{aveSeconds}, #{minSeconds}, #{maxSeconds}, #{id}, #{name}, #{lastRunTime}, #{lastSuccessTime}, #{times}, #{faliureTimes}, #{successTimes}, #{totalSeconds}, #{aveSeconds}, #{minSeconds}, #{maxSeconds}))"
     void insert(@Param("sysJobDO") SysJobDO sysJobDO);

     @Delete("DELETE FROM sys_job
WHERE (id = #{id}))"
     void delete(@Param("id") Long id);

     @Select("SELECT id, name, last_run_time, last_success_time, times, faliure_times, success_times, total_seconds, ave_seconds, min_seconds, max_seconds, id, name, last_run_time, last_success_time, times, faliure_times, success_times, total_seconds, ave_seconds, min_seconds, max_seconds
FROM sys_job
WHERE (id = #{id}))"
     SysJobDO select(@Param("id") Long id);

     @Update("SELECT id, name, last_run_time, last_success_time, times, faliure_times, success_times, total_seconds, ave_seconds, min_seconds, max_seconds, id, name, last_run_time, last_success_time, times, faliure_times, success_times, total_seconds, ave_seconds, min_seconds, max_seconds
FROM sys_job
WHERE (id = #{id}))"
     void update(@Param("sysJobDO") SysJobDO sysJobDO);}
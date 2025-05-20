package SysJobDO;

import java.lang.Long;
import java.util.Date;
import java.lang.Integer;
import java.lang.String;
public class SysJobDO { 

     private Long id;

     private String name;

     private Date lastRunTime;

     private Date lastSuccessTime;

     private Integer times;

     private Integer faliureTimes;

     private Integer successTimes;

     private Long totalSeconds;

     private Long aveSeconds;

     private Long minSeconds;

     private Long maxSeconds;

}
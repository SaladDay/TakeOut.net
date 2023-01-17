package cn.saladday.rjTakeOut.utils;

public class ThreadLocalEmpIdDataUtil {
    //全局只有一个，但是在不同线程，存储的数据是分离的
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

    public static Long getEmpId(){
        return threadLocal.get();
    }
    public static void setEmpId(Long empId){
        threadLocal.set(empId);
    }

    public static Long getUserId(){
        return threadLocal.get();
    }
    public static void setUserId(Long UserId){
        threadLocal.set(UserId);
    }


}

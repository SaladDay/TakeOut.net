package cn.saladday.rjTakeOut.common;

import cn.saladday.rjTakeOut.domain.Employee;
import cn.saladday.rjTakeOut.utils.ThreadLocalEmpIdDataUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 元数据处理器，属于MybatisPlus
 * 事实上是操作一些关于信息的信息的(MetaObject)
 * 在MetaObject中有originalObject，因此可以用来
 * 在操作数据库之前给数据对象 处理 公共数据
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("[新增操作公共数据处理]");

        metaObject.setValue("createUser", ThreadLocalEmpIdDataUtil.getEmpId());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateUser", ThreadLocalEmpIdDataUtil.getEmpId());
        metaObject.setValue("updateTime", LocalDateTime.now());

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("[更新操作公共数据处理]");


        metaObject.setValue("updateUser", ThreadLocalEmpIdDataUtil.getEmpId());
        metaObject.setValue("updateTime", LocalDateTime.now());

    }
}

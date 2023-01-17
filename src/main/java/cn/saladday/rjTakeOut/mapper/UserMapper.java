package cn.saladday.rjTakeOut.mapper;

import cn.saladday.rjTakeOut.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.stream.BaseStream;
@Mapper
public interface UserMapper extends BaseMapper<User> {
}

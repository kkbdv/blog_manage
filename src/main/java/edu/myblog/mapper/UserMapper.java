package edu.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.myblog.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {

    public User seletOneUserById(@Param("account") String account);
}

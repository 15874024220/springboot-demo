package com.example.demo.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {

    @Insert("INSERT INTO message(user_id, content, type, create_time) VALUES(#{userId}, #{content}, #{type}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Message message);

    @Select("SELECT * FROM message WHERE user_id = #{userId} ORDER BY create_time DESC LIMIT #{limit}")
    List<Message> findMessagesByUserId(@Param("userId") String userId, @Param("limit") int limit);

    /**
     * 传统MyBatis方式，在XML中定义SQL
      */
    List<Message> selectByUserId(@Param("userId") String userId);

    /**
     * 复杂联表查询
     */
    List<Message> selectWithUserInfo(@Param("userId") String userId);
}
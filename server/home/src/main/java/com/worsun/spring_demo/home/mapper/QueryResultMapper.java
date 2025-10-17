package com.worsun.spring_demo.home.mapper;

import com.worsun.spring_demo.home.model.QueryResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface QueryResultMapper {

    @Select("SELECT #{id} AS id, #{name} AS name, #{email} AS email")
    QueryResult mapToResult(@Param("id") String id,
            @Param("name") String name,
            @Param("email") String email);
}
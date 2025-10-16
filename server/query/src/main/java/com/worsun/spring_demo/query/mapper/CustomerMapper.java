package com.worsun.spring_demo.query.mapper;

import com.worsun.spring_demo.query.domain.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CustomerMapper {

  Optional<Customer> findById(@Param("id") Long id);

  List<Customer> searchCustomers(@Param("name") String name, @Param("email") String email);
}
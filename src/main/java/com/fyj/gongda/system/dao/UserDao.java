package com.fyj.gongda.system.dao;

import com.fyj.gongda.model.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User> {

    public User findByStudentId(String studentId);
}

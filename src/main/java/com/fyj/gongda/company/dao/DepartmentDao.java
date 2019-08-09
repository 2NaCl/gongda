package com.fyj.gongda.company.dao;

import com.fyj.gongda.model.domain.company.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 部门dao接口
 */
public interface DepartmentDao extends JpaRepository<Department,String> ,JpaSpecificationExecutor<Department> {
}

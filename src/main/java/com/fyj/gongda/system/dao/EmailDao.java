package com.fyj.gongda.system.dao;

import com.fyj.gongda.model.domain.system.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmailDao extends JpaRepository<Email,String>, JpaSpecificationExecutor<Email> {
}

package com.fyj.gongda.company.service;

import com.fyj.gongda.common.service.BaseService;
import com.fyj.gongda.common.utils.IdWorker;
import com.fyj.gongda.company.dao.DepartmentDao;
import com.fyj.gongda.model.domain.company.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService extends BaseService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 1.保存部门
     */
    public void save(Department department) {
        //设置主键的值
        String id = idWorker.nextId()+"";
        department.setId(id);
        //调用dao保存部门
        departmentDao.save(department);
    }

    /**
     * 2.更新部门
     */
    public void update(Department department) {
        //1.根据id查询部门
        Department dept = departmentDao.findById(department.getId()).get();
        //2.设置部门属性
        dept.setCode(department.getCode());
        dept.setIntroduce(department.getIntroduce());
        dept.setName(department.getName());
        //3.更新部门
        departmentDao.save(dept);
    }

    /**
     * 3.根据id查询部门
     */
    public Department findById(String id) {
        return departmentDao.findById(id).get();
    }

    /**
     * 4.查询全部部门列表
     */
    public List<Department> findAll(String companyId) {
        /**
         * 用户构造查询条件
         *      1.只查询companyId
         *      2.很多的地方都需要根据companyId查询
         *      3.很多的对象中都具有companyId
         *
         */
//        Specification<Department> spec = new Specification<Department>() {
//            /**
//             * 用户构造查询条件
//             *      root   ：包含了所有的对象数据
//             *      cq     ：一般不用
//             *      cb     ：构造查询条件
//             */
//            public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
//                //根据企业id查询
//                return cb.equal(root.get("companyId").as(String.class),companyId);
//            }
//        };
        return departmentDao.findAll(getSpec(companyId));
    }

    /**
     * 5.根据id删除部门
     */
    public void deleteById(String id) {
        departmentDao.deleteById(id);
    }


}

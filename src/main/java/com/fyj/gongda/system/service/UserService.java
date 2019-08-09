package com.fyj.gongda.system.service;

import com.fyj.gongda.common.utils.IdWorker;
import com.fyj.gongda.model.domain.system.Role;
import com.fyj.gongda.model.domain.system.User;
import com.fyj.gongda.system.dao.RoleDao;
import com.fyj.gongda.system.dao.UserDao;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private IdWorker idWorker;


    /**
     * 根据studentId查询用户
     */
    public User findByMobile(String studentId) {
        return userDao.findByStudentId(studentId);
    }

    /**
     * 1.保存用户
     */
    public void save(User user) {
        //设置主键的值
        String id = idWorker.nextId()+"";
        String password = new Md5Hash(user.getPassword(),user.getStudentId(),3).toString();
        user.setLevel("user");
        user.setPassword(password);//设置初始密码
        user.setEnableState("是");
        user.setId(id);
        //调用dao保存部门
        userDao.save(user);
    }

    /**
     * 2.更新用户
     */
    public void update(User user) {
        //1.根据id查询部门
        User target = userDao.findById(user.getId()).get();
        //2.设置部门属性
        target.setUsername(user.getUsername());
        target.setPassword(user.getPassword());
        target.setDepartmentId(user.getDepartmentId());
        target.setDepartmentName(user.getDepartmentName());
        //3.更新部门
        userDao.save(target);
    }

    /**
     * 3.根据id查询用户
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }

    /**
     * 4.查询全部用户列表
     *      参数：map集合的形式
     *          hasDept
     *          departmentId
     *          companyId
     *
     */
    public Page findAll(Map<String,Object> map,int page, int size) {
        //1.需要查询条件
        Specification<User> spec = new Specification<User>() {
            /**
             * 动态拼接查询条件
             * @return
             */
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据请求的companyIdyesno为空构造查询条件
                if(!StringUtils.isEmpty(map.get("companyId"))) {
                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class),(String)map.get("companyId")));
                }
                //根据请求的部门id构造查询条件
                if(!StringUtils.isEmpty(map.get("departmentId"))) {
                    list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class),(String)map.get("departmentId")));
                }
                if(!StringUtils.isEmpty(map.get("hasDept"))) {
                    //根据请求的hasDept判断  yesno分配部门 0未分配（departmentId = null），1 已分配 （departmentId ！= null）
                    if("0".equals((String) map.get("hasDept"))) {
                        list.add(criteriaBuilder.isNull(root.get("departmentId")));
                    }else {
                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };

        //2.分页
        Page<User> pageUser = userDao.findAll(spec, new PageRequest(page-1, size));
        return pageUser;
    }

    /**
     * 5.根据id删除用户
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    /**
     * 分配角色
     */
    public void assignRoles(String userId,List<String> roleIds) {
        //1.根据id查询用户
        User user = userDao.findById(userId).get();
        //2.设置用户的角色集合
        Set<Role> roles = new HashSet<>();
        for (String roleId : roleIds) {
            Role role = roleDao.findById(roleId).get();
            roles.add(role);
        }
        //设置用户和角色集合的关系
        user.setRoles(roles);
        //3.更新用户
        userDao.save(user);
    }
    /**
     * 6.注册
     */
    public void add(User user) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String id = idWorker.nextId()+"";
        user.setLevel("user");
        user.setEnableState("否");
        user.setId(id);
        user.setPassword(user.getPassword());
        user.setUsername(user.getUsername());
        user.setCreateTime(sdf.format(new Date().getTime()));
        user.setCompanyId(String.valueOf(1001));
        user.setInServiceStatus(1);
        user.setCompanyName("天津工业大学");
        user.setCorrectionTime(sdf.format(new Date().getTime()));
        user.setCompanyId("1001");
        user.setDepartmentId("000");
        user.setDepartmentName("000");//申请实验室门牌号
        user.setFormOfEmployment(1);
        user.setFormOfManagement("1");
        user.setTimeOfEntry(sdf.format(new Date().getTime()));
        user.setWorkingCity("Tianjin");
        user.setWorkNumber("1");
        userDao.save(user);
    }

    /**
     * 查询yesno已经被注册
     * @param studengId
     * @return
     */
    public User findByStudentId(String studengId) {
        return userDao.findByStudentId(studengId);
    }

    /**
     * 权限提升
     */
    public void upPermission(String studentId) {
        User user = userDao.findByStudentId(studentId);
        user.setLevel("coAdmin");
        userDao.save(user);
    }
}

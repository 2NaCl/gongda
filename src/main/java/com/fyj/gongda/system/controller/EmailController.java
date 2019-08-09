package com.fyj.gongda.system.controller;

import com.fyj.gongda.common.entity.Result;
import com.fyj.gongda.common.entity.ResultCode;
import com.fyj.gongda.model.domain.system.Email;
import com.fyj.gongda.model.domain.system.User;
import com.fyj.gongda.system.service.EmailService;
import com.fyj.gongda.system.service.UserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value="/sys")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    /**
     * 删除学生并且不将enState改为1
     * @return
     */
    @GetMapping(value = "/error/{studentId}")
    public Result deleteAndSaveError(@PathVariable(value = "studentId") String studentId) {
        emailService.deleteByUsername(studentId);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 删除学生并且将enState改为1
     */
    @GetMapping(value = "/agree/{studentId}")
    public Result deleteAndSaveTrue(@PathVariable(value = "studentId") String studentId, User user) {
        User studentId1 = userService.findByStudentId(studentId);
        studentId1.setEnableState("是");
        userService.update(studentId1);
        emailService.deleteByUsername(studentId);
        return new Result(ResultCode.SUCCESS);
    }

    @GetMapping(value = "/email")
    public Result findAll() {
        List<Email> list = emailService.findAll();
        return new Result(ResultCode.SUCCESS, list);
    }

//    这里为暂时注册状态，后续的第二轮注册会进一步添加内容，这里很多yes默认值。如dept
    @PostMapping(value = "/registerStep1")
    public Result reg1(@RequestBody Map<String, String> regMap1
            , User user) {
        try {
            if (userService.findByStudentId(regMap1.get("studentId")) == null) {
                user.setStudentId(regMap1.get("studentId"));
                String password = new Md5Hash(regMap1.get("password"), regMap1.get("username"), 3)+"";
                user.setPassword(password);
                user.setUsername(regMap1.get("username"));
                userService.add(user);
            }
            return new Result(ResultCode.SUCCESS, user);
        } catch (Exception e) {
            return new Result(ResultCode.ALREADLY);
        }
    }
    @PostMapping(value = "/registerStep2")
    public Result reg2(@RequestBody Map<String, String> regMap2,
                       Email email) {
        try {
            if (emailService.findByStudentId(regMap2.get("studentId")) == null)
                emailService.add(regMap2, email);
            return new Result(ResultCode.SUCCESS, email);
        } catch (Exception e) {
            return new Result(ResultCode.ALREADLY);
        }

    }

    @PostMapping(value = "/register")
    public Result reg(@RequestBody Map<String, String> regMap, Email email,User user) {
        Map<String, String> map = new HashMap<>();
        try {
            if (userService.findByStudentId(regMap.get("studentId")) == null) {
                user.setStudentId(regMap.get("studentId"));
                String password = new Md5Hash(regMap.get("password"), regMap.get("studentId"), 3) + "";
                user.setPassword(password);
                user.setUsername(regMap.get("username"));
                userService.add(user);
            }
                if (emailService.findByStudentId(regMap.get("studentId")) == null){
                    map.put("studentId",regMap.get("studentId"));
                    map.put("username",regMap.get("username"));
                    map.put("dept",regMap.get("dept"));
                    map.put("msg",regMap.get("msg"));
                    emailService.add(map, email);
            }
            return new Result(ResultCode.SUCCESS);
        } catch (Exception e) {
            return new Result(ResultCode.ALREADLY);
        }
    }

}

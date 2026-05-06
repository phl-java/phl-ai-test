package com.phl.phlaicode.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.phl.phlaicode.model.dto.user.UserQueryRequest;
import com.phl.phlaicode.model.entity.User;
import com.phl.phlaicode.model.vo.LoginUserVO;
import com.phl.phlaicode.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 用户 服务层。
 *
 * @author 程序员林</a>
 */
public interface UserService extends IService<User> {

    long userRegister(String userAccount, String userPassword, String checkPassword);


    /**
     * 获取默认密码的加密
     * @param defaultPassword 默认密码
     * @return 加密后的密码
     */
    String getEncryptPassword(String defaultPassword);

    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);


    User getLoginUser(HttpServletRequest request);


    LoginUserVO getLoginUserVO(User loginUser);


    boolean userLogout(HttpServletRequest request);

    UserVO getUserVO(User user);

    QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest);

    List<UserVO> getUserVOList(List<User> userList);

}

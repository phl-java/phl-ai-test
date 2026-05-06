package com.phl.phlaicode.service;

import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.phl.phlaicode.exception.BusinessException;
import com.phl.phlaicode.exception.ErrorCode;
import com.phl.phlaicode.model.dto.app.AppAddRequest;
import com.phl.phlaicode.model.dto.app.AppQueryRequest;
import com.phl.phlaicode.model.entity.App;
import com.phl.phlaicode.model.entity.User;
import com.phl.phlaicode.model.vo.AppVO;
import com.phl.phlaicode.model.vo.UserVO;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 应用 服务层。
 *
 * @author 程序员林</ a>
 */
public interface AppService extends IService<App> {


    /**
     * 创建应用
     *
     * @param appAddRequest
     * @param loginUser
     * @return
     */
    Long createApp(AppAddRequest appAddRequest, User loginUser);

    /**
     * 应用部署
     *
     * @param appId     应用 ID
     * @param loginUser 登录用户
     * @return 可访问的部署地址
     */
    String deployApp(Long appId, User loginUser);

    /**
     * 异步生成应用截图并更新封面
     *
     * @param appId  应用ID
     * @param appUrl 应用访问URL
     */
    void generateAppScreenshotAsync(Long appId, String appUrl);

    /**
     * 根据应用对象获取应用VO对象。
     *
     * @param app 应用对象
     * @return 应用VO对象
     */
    AppVO getAppVO(App app);


    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    List<AppVO> getAppVOList(List<App> appList);


    /**
     * 根据应用id、消息和登录用户生成代码
     *
     * @param appId     应用id
     * @param message   提示词
     * @param loginUser 登录用户
     * @return 代码
     */
    Flux<String> chatToGenCode(Long appId, String message, User loginUser);


}

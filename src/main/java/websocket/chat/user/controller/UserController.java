package websocket.chat.user.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import websocket.chat.BaseController;
import websocket.chat.constant.Constant;
import websocket.chat.user.service.UserService;
import websocket.chat.user.vo.AddFriendRequest;
import websocket.chat.user.vo.DeleteFriendRequest;
import websocket.chat.user.vo.FriendListRequest;
import websocket.chat.user.vo.FriendListResponse;
import websocket.chat.user.vo.SearchUserRequest;
import websocket.chat.user.vo.UpdateUserInfoRequest;
import websocket.chat.user.vo.UserInfoRequest;
import websocket.chat.user.vo.UserListResponse;
import websocket.chat.user.vo.UserVO;
import websocket.chat.util.ApiResponse;
import websocket.chat.util.JsonUtil;

/**
 * 用户管理控制器
 * Date: 2016-04-03
 *
 * @author wangzhonglin
 */
@RestController
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping("api/addFriend")
    public String addFriend(@Param("d") String d, @Param("cb") String cb) {
        AddFriendRequest request = JsonUtil.safelyParseObject(d, AddFriendRequest.class);
        if (request == null) {
            ApiResponse apiResponse = ApiResponse.create(false, Constant.NULL_PARAM_MESSAGE, Constant.ERROR_CODE, null);
            return toJson(apiResponse, cb);
        }
        if (!checkLogin(request.getLoginSessionId(), request.getSenderUserId())) {
            ApiResponse apiResponse = ApiResponse.create(false, Constant.USER_NOT_LOGIN_MESSAGE, Constant.ERROR_CODE, null);
            return toJson(apiResponse, cb);
        }

        int insert = userService.addFriend(request.getSenderUserId(), request.getReceiverUserId());
        if (insert == 0) {
            ApiResponse apiResponse = ApiResponse.create(false, Constant.EXECUTION_FAILURE, Constant.ERROR_CODE, null);
            return toJson(apiResponse, cb);
        }
        ApiResponse apiResponse = ApiResponse.create(true, Constant.SUCCESS_MESSAGE, Constant.SUCCESS_CODE, null);
        return toJson(apiResponse, cb);
    }

    @RequestMapping("api/deleteFriend")
    public String deleteFriend(@Param("d") String d, @Param("cb") String cb) {
        DeleteFriendRequest request = JsonUtil.safelyParseObject(d, DeleteFriendRequest.class);
        if (request == null) {
            ApiResponse apiResponse = ApiResponse.create(false, Constant.NULL_PARAM_MESSAGE, Constant.ERROR_CODE, null);
            return toJson(apiResponse, cb);
        }
        if (!checkLogin(request.getLoginSessionId(), request.getUserId())) {
            ApiResponse apiResponse = ApiResponse.create(false, Constant.USER_NOT_LOGIN_MESSAGE, Constant.ERROR_CODE, null);
            return toJson(apiResponse, cb);
        }

        userService.deleteFriend(request.getUserId(), request.getFriendId());
        ApiResponse apiResponse = ApiResponse.create(true, Constant.SUCCESS_MESSAGE, Constant.SUCCESS_CODE, null);
        return toJson(apiResponse, cb);
    }

    @RequestMapping("api/getFriendList")
    public String getFriendList(@Param("d") String d, @Param("cb") String cb) {
        FriendListRequest request = JsonUtil.safelyParseObject(d, FriendListRequest.class);
        if (request == null) {
            ApiResponse apiResponse = ApiResponse.create(false, Constant.NULL_PARAM_MESSAGE, Constant.ERROR_CODE, null);
            return toJson(apiResponse, cb);
        }
        if (!checkLogin(request.getLoginSessionId(), request.getUserId())) {
            ApiResponse apiResponse = ApiResponse.create(false, Constant.USER_NOT_LOGIN_MESSAGE, Constant.ERROR_CODE, null);
            return toJson(apiResponse, cb);
        }

        FriendListResponse response = userService.getFriendListByUserId(request.getUserId());
        ApiResponse apiResponse = ApiResponse.create(true, Constant.SUCCESS_MESSAGE, Constant.SUCCESS_CODE, response);
        return toJson(apiResponse, cb);
    }

    @RequestMapping("api/searchUser")
    public String searchUser(@Param("d") String d, @Param("cb") String cb) {
        SearchUserRequest request = JsonUtil.safelyParseObject(d, SearchUserRequest.class);
        if (request == null) {
            ApiResponse apiResponse = ApiResponse.create(false, Constant.NULL_PARAM_MESSAGE, Constant.ERROR_CODE, null);
            return toJson(apiResponse, cb);
        }
        if (!checkLogin(request.getLoginSessionId(), request.getUserId())) {
            ApiResponse apiResponse = ApiResponse.create(false, Constant.USER_NOT_LOGIN_MESSAGE, Constant.ERROR_CODE, null);
            return toJson(apiResponse, cb);
        }

        UserListResponse response = userService.searchUser(request.getKeyword());
        ApiResponse apiResponse = ApiResponse.create(true, Constant.SUCCESS_MESSAGE, Constant.SUCCESS_CODE, response);
        return toJson(apiResponse, cb);
    }

    @RequestMapping("api/getUserInfo")
    public String getUserInfo(@Param("d") String d, @Param("cb") String cb) {
        UserInfoRequest request = JsonUtil.safelyParseObject(d, UserInfoRequest.class);
        if (request == null) {
            ApiResponse apiResponse = ApiResponse.create(false, Constant.NULL_PARAM_MESSAGE, Constant.ERROR_CODE, null);
            return toJson(apiResponse, cb);
        }
        if (!checkLogin(request.getLoginSessionId(), request.getUserId())) {
            ApiResponse apiResponse = ApiResponse.create(false, Constant.USER_NOT_LOGIN_MESSAGE, Constant.ERROR_CODE, null);
            return toJson(apiResponse, cb);
        }

        UserVO userVO = userService.getUserInfo(request.getUserId());
        UserListResponse.EachUser userInfo = UserListResponse.createEachUser(userVO);
        ApiResponse apiResponse = ApiResponse.create(true, Constant.SUCCESS_MESSAGE, Constant.SUCCESS_CODE, userInfo);
        return toJson(apiResponse, cb);
    }

    @RequestMapping(value = "api/updateUserInfo", method = RequestMethod.POST)
    public String updateUserInfo(@Param("d") String d) {
        UpdateUserInfoRequest request = JsonUtil.safelyParseObject(d, UpdateUserInfoRequest.class);
        if (request == null) {
            ApiResponse apiResponse = ApiResponse.create(false, Constant.NULL_PARAM_MESSAGE, Constant.ERROR_CODE, null);
            return toJson(apiResponse);
        }
        if (!checkLogin(request.getLoginSessionId(), request.getUserId())) {
            ApiResponse apiResponse = ApiResponse.create(false, Constant.USER_NOT_LOGIN_MESSAGE, Constant.ERROR_CODE, null);
            return toJson(apiResponse);
        }

        userService.updateUserInfo(request.getUserId(), request.getUserName(), request.getUserNickname(), request.getPassword(),
                request.getSex(), request.getSignature(), request.getAvatar());
        ApiResponse apiResponse = ApiResponse.create(true, Constant.SUCCESS_MESSAGE, Constant.SUCCESS_CODE, null);
        return toJson(apiResponse);
    }
}

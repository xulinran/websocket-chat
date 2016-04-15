package websocket.chat.user.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import websocket.chat.BaseController;
import websocket.chat.constant.Constant;
import websocket.chat.user.service.UserService;
import websocket.chat.user.vo.AddFriendRequest;
import websocket.chat.user.vo.DeleteFriendRequest;
import websocket.chat.user.vo.FriendListRequest;
import websocket.chat.user.vo.SearchUserRequest;
import websocket.chat.user.vo.UserListResponse;
import websocket.chat.util.ApiResponse;
import websocket.chat.util.JsonUtil;

/**
 * 用户管理控制器
 * Date: 2016-04-03
 *
 * @author wangzhonglin
 */
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping("api/addFriend")
    public String addFriend(@Param("d") String d, @Param("cb") String cb) {
        AddFriendRequest request = JsonUtil.safelyParseObject(d, AddFriendRequest.class);
        if (request == null) {
            ApiResponse apiResponse = ApiResponse.create(false, "添加好友失败, 参数为空！", Constant.ERROR_CODE, null);
            return toJson(apiResponse, cb);
        }
        if (!checkLogin(request.getLoginSessionId(), request.getSenderUserId())) {
            ApiResponse apiResponse = ApiResponse.create(false, "添加好友失败, 用户未登录！", Constant.ERROR_CODE, null);
            return toJson(apiResponse, cb);
        }

        userService.addFriend(request.getSenderUserId(), request.getReceiverUserId());
        ApiResponse apiResponse = ApiResponse.create(true, "SUCCESS", Constant.SUCCESS_CODE, null);
        return toJson(apiResponse, cb);
    }

    @RequestMapping("api/deleteFriend")
    public String deleteFriend(@Param("d") String d, @Param("cb") String cb) {
        DeleteFriendRequest request = JsonUtil.safelyParseObject(d, DeleteFriendRequest.class);
        if (request == null) {
            ApiResponse apiResponse = ApiResponse.create(false, "参数为空", Constant.ERROR_CODE, null);
            return toJson(apiResponse, cb);
        }
        if (!checkLogin(request.getLoginSessionId(), request.getUserId())) {
            ApiResponse apiResponse = ApiResponse.create(false, "用户未登录", Constant.ERROR_CODE, null);
            return toJson(apiResponse, cb);
        }

        userService.deleteFriend(request.getUserId(), request.getFriendId());
        ApiResponse apiResponse = ApiResponse.create(true, "SUCCESS", Constant.SUCCESS_CODE, null);
        return toJson(apiResponse, cb);
    }

    @RequestMapping("api/getFriendList")
    public String getFriendList(@Param("d") String d, @Param("cb") String cb) {
        FriendListRequest request = JsonUtil.safelyParseObject(d, FriendListRequest.class);
        if (request == null) {
            ApiResponse apiResponse = ApiResponse.create(false, "参数为空", Constant.ERROR_CODE, null);
            return toJson(apiResponse, cb);
        }
        if (!checkLogin(request.getLoginSessionId(), request.getUserId())) {
            ApiResponse apiResponse = ApiResponse.create(false, "用户未登录", Constant.ERROR_CODE, null);
            return toJson(apiResponse, cb);
        }

        userService.getFriendListByUserId(request.getUserId());
        ApiResponse apiResponse = ApiResponse.create(true, "SUCCESS", Constant.SUCCESS_CODE, null);
        return toJson(apiResponse, cb);
    }

    @RequestMapping("api/searchUser")
    public String searchUser(@Param("d") String d, @Param("cb") String cb) {
        SearchUserRequest request = JsonUtil.safelyParseObject(d, SearchUserRequest.class);
        if (request == null) {
            ApiResponse apiResponse = ApiResponse.create(false, "参数为空", Constant.ERROR_CODE, null);
            return toJson(apiResponse, cb);
        }
        if (!checkLogin(request.getLoginSessionId(), request.getUserId())) {
            ApiResponse apiResponse = ApiResponse.create(false, "用户未登录", Constant.ERROR_CODE, null);
            return toJson(apiResponse, cb);
        }

        UserListResponse response = userService.searchUser(request.getKeyword());
        ApiResponse apiResponse = ApiResponse.create(true, "SUCCESS", Constant.SUCCESS_CODE, response);
        return toJson(apiResponse, cb);
    }
}
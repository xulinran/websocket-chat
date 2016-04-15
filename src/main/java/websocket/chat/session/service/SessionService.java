package websocket.chat.session.service;

import websocket.chat.session.vo.LatestSessionListResponse;
import websocket.chat.session.vo.SessionVO;

import java.util.List;

/**
 * SessionService
 * Date: 2016-04-06
 *
 * @author wangzhonglin
 */
public interface SessionService {

    SessionVO getSessionBySessionId(int sessionId);

    int createSession(SessionVO sessionVO);

    List<SessionVO> getSessionListByUserId(int userId);

    LatestSessionListResponse getLatestSessionList(int userId);

    int deleteSession(int sessionId);
}
package eyemazev20.utils;
import com.sun.security.auth.UserPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;


public class UserHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (!(request instanceof ServletServerHttpRequest)) {
            System.err.println("conn failed!");
            return null;
        }
        final var servletServerHttpRequest = (ServletServerHttpRequest) request;
        HttpSession httpSession = servletServerHttpRequest.getServletRequest().getSession();
        //System.err.println("-*****************************************");
        return new UserPrincipal(httpSession.getAttribute("loginUUID").toString());
    }
}//*/

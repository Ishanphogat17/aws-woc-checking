package com.core.interceptor;

import com.core.constant.WebSocketConstant;
import com.core.property.JwtProperty;
import com.core.security.JwtTokenHelper;
import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class WebSocketMessageInterceptor implements ChannelInterceptor {

    private final JwtProperty jwtProperty;
    private final JwtTokenHelper jwtTokenHelper;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (WebSocketConstant.CONNECT.equals(accessor.getCommand() != null ? accessor.getCommand().name() : null)) {
            String authHeader = accessor.getFirstNativeHeader(WebSocketConstant.AUTHORIZATION);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    String username = jwtTokenHelper.getUsernameFromToken(token);

                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(username, null);

                    accessor.setUser(authentication);

                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid or expired JWT token");
                }
            } else {
                throw new IllegalArgumentException("Missing Authorization header");
            }
        }
        return message;
    }
}

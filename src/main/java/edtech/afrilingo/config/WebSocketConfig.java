package edtech.afrilingo.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        logger.info("Configuring WebSocket message broker");
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        logger.info("Registering WebSocket endpoints");
        registry.addEndpoint("/ws")
               .setAllowedOriginPatterns("*");
        logger.info("WebSocket endpoint /ws registered successfully");
    }
    
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    try {
                        String authHeader = null;
                        if (accessor.getNativeHeader("Authorization") != null && !accessor.getNativeHeader("Authorization").isEmpty()) {
                            authHeader = accessor.getNativeHeader("Authorization").get(0);
                        } else if (accessor.getFirstNativeHeader("authorization") != null) {
                            authHeader = accessor.getFirstNativeHeader("authorization");
                        }

                        if (authHeader != null && !authHeader.isBlank()) {
                            String token = authHeader;
                            if (token.toLowerCase().startsWith("bearer ")) {
                                token = token.substring(7);
                            }
                            String username = jwtService.extractUsername(token);
                            if (username != null && !username.isBlank()) {
                                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                                if (jwtService.isTokenValid(token, userDetails)) {
                                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                            userDetails, null, userDetails.getAuthorities());
                                    accessor.setUser(authentication);
                                    logger.debug("STOMP CONNECT authenticated for user: {}", username);
                                } else {
                                    logger.warn("Invalid JWT on STOMP CONNECT for username: {}", username);
                                }
                            }
                        } else {
                            logger.debug("No Authorization header present on STOMP CONNECT");
                        }
                    } catch (Exception e) {
                        logger.warn("Failed STOMP CONNECT authentication: {}", e.getMessage());
                    }
                }
                return message;
            }
        });
    }
    
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        logger.info("Configuring WebSocket transport settings");
        registration.setMessageSizeLimit(64 * 1024) // 64KB
                   .setSendBufferSizeLimit(512 * 1024) // 512KB
                   .setSendTimeLimit(20000); // 20 seconds
    }
}
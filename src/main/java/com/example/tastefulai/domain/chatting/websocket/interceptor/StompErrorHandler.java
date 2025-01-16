//package com.example.tastefulai.domain.chatting.websocket.interceptor;
//
//import com.example.tastefulai.global.error.errorcode.ErrorCode;
//import com.example.tastefulai.global.error.exception.CustomException;
//import io.jsonwebtoken.ExpiredJwtException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageDeliveryException;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;
//
//import java.nio.charset.MalformedInputException;
//import java.nio.charset.StandardCharsets;
//
//@Component
//@Slf4j
//public class StompErrorHandler extends StompSubProtocolErrorHandler {
//
//    public StompErrorHandler() {
//        super();
//    }
//
//    @Override
//    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable exception) {
//        log.error("WebSocket message processing error", exception);
//
//        if (exception instanceof MalformedInputException) {
//            log.info("Malformed input error detected.");
//            return createErrorMessage(clientMessage, ErrorCode.INVALID_VALUE);
//        }
//
//        if (exception instanceof MessageDeliveryException) {
//            Throwable cause = exception.getCause();
//            if (cause instanceof ExpiredJwtException) {
//                log.info("Expired token detected during message delivery.");
//                return createErrorMessage(clientMessage, ErrorCode.EXPIRED_TOKEN);
//            } else if (cause instanceof CustomException) {
//                CustomException customException = (CustomException) cause;
//                log.info("Custom exception detected: {}", customException.getErrorCode());
//                return createErrorMessage(clientMessage, customException.getErrorCode());
//            }
//            log.info("Unhandled delivery exception.");
//        }
//
//        return super.handleClientMessageProcessingError(clientMessage, exception);
//    }
//
//    private Message<byte[]> createErrorMessage(Message<byte[]> clientMessage, ErrorCode errorCode) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
//        accessor.setMessage(errorCode.name());
//        accessor.setLeaveMutable(true);
//
//        String errorMessage = errorCode.getMessage();
//        log.info("Returning error message: {}", errorMessage);
//
//        return MessageBuilder.createMessage(errorMessage.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
//    }
//}

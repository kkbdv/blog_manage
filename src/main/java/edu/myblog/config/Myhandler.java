package edu.myblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.http.WebSocket;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
@Component
public class Myhandler implements WebSocketHandler {
    // 所有用户集合
    private static CopyOnWriteArraySet<WebSocketSession> wsSet = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        System.out.println("添加在线用户");
        wsSet.add(webSocketSession);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        System.out.println("连接已关闭...");
        // 前端刷新的时候会触发close()
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    public void sendAllMessage(WebSocketMessage message){
        for(WebSocketSession ws:wsSet){
            System.out.println(ws.getId()+"已发送");
            // 判断连接是否可用，可用就回传消息不可以就移除
            if(ws.isOpen()){
                try {
                    ws.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                wsSet.remove(ws);
            }

        }
    }
}

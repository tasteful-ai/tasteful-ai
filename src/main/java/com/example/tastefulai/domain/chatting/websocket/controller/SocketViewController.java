package com.example.tastefulai.domain.chatting.websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SocketViewController {

    @GetMapping("/test")
    public String test() {
        return "index";
    }
}

package com.mi.teamarket.controller;

import com.mi.teamarket.entity.Chat;
import com.mi.teamarket.entity.Session;
import com.mi.teamarket.entity.Status;
import com.mi.teamarket.mapper.ChatMapper;
import com.mi.teamarket.mapper.SessionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private SessionMapper sessionMapper;

    @PostMapping("/post")
    public Session postChatMessage(@RequestParam Integer fromId, @RequestParam Integer toId, String message) {
        var c = new Chat();
        c.setFromId(fromId);
        c.setToId(toId);
        c.setMessage(message);
        var s = new Session();
        s.setUser1(fromId);
        s.setUser2(toId);
        sessionMapper.insertOrUpdate(s);
        c.setSessionId(s.getSessionId());
        chatMapper.insert(c);
        return s;
    }

    @PostMapping("/post/{id}")
    public Status postChatMessageWithSessionId(@RequestParam Integer fromId, @RequestParam Integer toId, String message, @PathVariable Integer id) {
        var c = new Chat();
        c.setFromId(fromId);
        c.setToId(toId);
        c.setMessage(message);
        c.setSessionId(id);
        chatMapper.insert(c);
        return Status.getSuccessInstance("消息记录插入成功");
    }
    // 没写完

}

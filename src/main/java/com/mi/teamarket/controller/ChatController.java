package com.mi.teamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.teamarket.entity.Chat;
import com.mi.teamarket.entity.Session;
import com.mi.teamarket.entity.SessionChats;
import com.mi.teamarket.entity.Status;
import com.mi.teamarket.mapper.ChatMapper;
import com.mi.teamarket.mapper.SessionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        return Status.getSuccessInstance("消息记录发送成功");
    }

    @GetMapping("/getChatsBySessionId/{sessionId}")
    public List<Chat> getChatsBySessionId(@PathVariable Integer sessionId) {
        QueryWrapper<Chat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("session_id", sessionId);
        return chatMapper.selectList(queryWrapper);
    }

    @GetMapping("/getAllChatByUserId/{id}")
    public List<SessionChats> getAllChatByUserId(@PathVariable Integer id) {
        List<SessionChats> return_list = new ArrayList<>();
        QueryWrapper<Session> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user1", id);
        queryWrapper.eq("closed", true);
        var s1 = sessionMapper.selectList(queryWrapper);
        for (var s : s1) {
            SessionChats sc = new SessionChats();
            sc.setSessionId(s.getSessionId());
            sc.setUser1(s.getUser1());
            sc.setUser2(s.getUser2());
            sc.setClosed(s.isClosed());
            sc.setCreatedTime(s.getCreatedTime());
            QueryWrapper<Chat> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("session_id", s.getSessionId());
            var l = chatMapper.selectList(queryWrapper2);
            sc.setChatList(l);
            return_list.add(sc);
        }
        return return_list;
    }


}

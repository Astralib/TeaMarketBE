package com.mi.teamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.teamarket.entity.Session;
import com.mi.teamarket.entity.Status;
import com.mi.teamarket.mapper.ServerStatusMapper;
import com.mi.teamarket.mapper.SessionMapper;
import com.mi.teamarket.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/session")
public class SessionController {
    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private ServerStatusMapper serverStatusMapper;

    @PostMapping("/CreateASession/{userId}")
    public Session CreateASession(@PathVariable Integer userId) {
        var s = new Session();
        var ss = serverStatusMapper.getAServerStatus();
        if (ss == null) {
            s.setSessionId(-1);
            return s;
        }
        s.setUser1(userId);
        s.setUser2(ss.getServerUserId());
        s.setCreatedTime(Utility.getCurrentTimeDate());
        ss.setLastChatWith(userId);
        ss.setAvailable(false);

        serverStatusMapper.insertOrUpdate(ss);
        sessionMapper.insertOrUpdate(s);
        // System.out.println(s);
        return s;
    }

    @GetMapping("/getUsingSession/{userId}")
    public Session getUsingSession(@PathVariable Integer userId) {
        QueryWrapper<Session> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user1", userId);
        queryWrapper.eq("closed", false);
        try {
            return sessionMapper.selectOne(queryWrapper);
        } catch (Exception e) {
            return CreateASession(userId);
        }

    }

    @PostMapping("/closeSessionByUserId/{userId}")
    public Status closeSessionByUserId(@PathVariable Integer userId) {
        sessionMapper.closeOldSession(userId);
        return Status.getSuccessInstance();
    }
}

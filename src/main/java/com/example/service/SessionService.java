package com.example.service;


import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class SessionService {

    @Autowired
    private HazelcastInstance hazelcastInstance;

    public String getSessionId() {
        HttpSession session = getCurrentSession();
        return session != null ? session.getId() : "No session";
    }

    public String getNodeInfo() {
        return hazelcastInstance.getCluster().getLocalMember().toString();
    }

    public void storeData(String key, String value) {
        HttpSession session = getCurrentSession();
        if (session != null) {
            session.setAttribute(key, value);
        }
    }

    public String getData(String key) {
        HttpSession session = getCurrentSession();
        if (session != null) {
            Object value = session.getAttribute(key);
            return value != null ? value.toString() : null;
        }
        return null;
    }

    public void clearData(String key) {
        HttpSession session = getCurrentSession();
        if (session != null) {
            session.removeAttribute(key);
        }
    }

    private HttpSession getCurrentSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        return request.getSession(true);
    }
}

package com.sunxu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

/**
 * @author 孙许
 * @date 2018/02/08
 * @description
 */
@WebListener
public class SessionListener implements HttpSessionListener, HttpSessionIdListener {

    Logger logger = LogManager.getLogger(SessionListener.class);


    @Override
    public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
        logger.debug("sessionIdChanged: " + "Session ID " + oldSessionId + " changed to " + event.getSession().getId());
        SessionRegistry.updateSessionId(event.getSession(), oldSessionId);
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.debug("sessionCreated: " + "Session" + se.getSession().getId() + "created.");
        SessionRegistry.addSession(se.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.debug("sessionDestroyed: " + "Session" + se.getSession().getId() + "destroyed.");
        SessionRegistry.removeSession(se.getSession());
    }
}

package org.openkoala.koala.auth.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Compatibility listener for legacy web.xml entries.
 */
public class InitSuperUserListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // The original listener lived in the removed ss3adapter package.
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // No resources to release.
    }
}

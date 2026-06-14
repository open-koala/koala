package org.openkoala.security.shiro.spring;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.BeanInitializationException;

/**
 * Shiro 1.2.3 does not expose FilterChainResolver as a bean property.
 */
public class CustomShiroFilterFactoryBean extends ShiroFilterFactoryBean {

    private FilterChainResolver filterChainResolver;

    public void setFilterChainResolver(FilterChainResolver filterChainResolver) {
        this.filterChainResolver = filterChainResolver;
    }

    @Override
    protected AbstractShiroFilter createInstance() throws Exception {
        SecurityManager securityManager = getSecurityManager();
        if (securityManager == null) {
            throw new BeanInitializationException("SecurityManager property must be set.");
        }
        if (!(securityManager instanceof WebSecurityManager)) {
            throw new BeanInitializationException("The security manager does not implement the WebSecurityManager interface.");
        }

        FilterChainResolver resolver = filterChainResolver;
        if (resolver == null) {
            FilterChainManager manager = createFilterChainManager();
            PathMatchingFilterChainResolver pathResolver = new PathMatchingFilterChainResolver();
            pathResolver.setFilterChainManager(manager);
            resolver = pathResolver;
        }
        return new SpringShiroFilter((WebSecurityManager) securityManager, resolver);
    }

    private static final class SpringShiroFilter extends AbstractShiroFilter {

        private SpringShiroFilter(WebSecurityManager securityManager, FilterChainResolver resolver) {
            setSecurityManager(securityManager);
            setFilterChainResolver(resolver);
        }
    }
}

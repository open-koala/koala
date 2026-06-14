package org.openkoala.opencis.jenkins;

import org.apache.commons.lang3.StringUtils;
import org.openkoala.opencis.CISClientBaseRuntimeException;
import org.openkoala.opencis.api.CISClient;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Jenkins CIS客户端
 *
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Nov 13, 2013 9:35:24 AM
 */
public class JenkinsCISClient implements CISClient {

    private KoalaScmConfig koalaScmConfig;

    private final URL jenkinsUrl;

    private final String username;

    private final String passwordOrAPIToken;

    public JenkinsCISClient(String jenkinsUrl, String username, String passwordOrAPIToken) {
        this.jenkinsUrl = convert(jenkinsUrl);
        this.username = username;
        this.passwordOrAPIToken = passwordOrAPIToken;
    }

    public void setKoalaScmConfig(KoalaScmConfig koalaScmConfig) {
        this.koalaScmConfig = koalaScmConfig;
    }

    @Override
    public void close() {
    }


    @Override
    public void createProject(Project project) {
        project.validate();
        validateClientConfiguration();
    }

    @Override
    public void removeProject(Project project) {
        project.validate();
        validateClientConfiguration();
    }


    @Override
    public void createUserIfNecessary(Project project, Developer developer) {
        project.validate();
        developer.validate();
        validateClientConfiguration();
    }

    @Override
    public void removeUser(Project project, Developer developer) {
        project.validate();
        validateClientConfiguration();

    }

    @Override
    public void assignUsersToRole(Project project, String role, Developer... developers) {
        project.validate();
        if (StringUtils.isBlank(role)) {
            throw new CISClientBaseRuntimeException("jenkins.role.empty");
        }
        validateClientConfiguration();
    }

    @Override
    public boolean authenticate() {
        return StringUtils.isNotBlank(username) && StringUtils.isNotBlank(passwordOrAPIToken);
    }

    private URL convert(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new CISClientBaseRuntimeException("jenkins.URL.MalformedURLException", e);
        }

    }

    private void validateClientConfiguration() {
        if (koalaScmConfig == null) {
            throw new CISClientBaseRuntimeException("jenkins.scm.config.empty");
        }
        if (StringUtils.isBlank(koalaScmConfig.getScmType()) || StringUtils.isBlank(koalaScmConfig.getScmAddress())) {
            throw new CISClientBaseRuntimeException("jenkins.scm.config.invalid");
        }
        if (jenkinsUrl == null) {
            throw new CISClientBaseRuntimeException("jenkins.url.empty");
        }
    }

}

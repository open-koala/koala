package org.openkoala.opencis.jenkins;

/**
 * User: zjzhai
 * Date: 1/22/14
 * Time: 10:56 AM
 */
public class KoalaGitConfig extends KoalaScmConfig {


    public KoalaGitConfig(String scmAddress) {
        super(scmAddress);
    }

    @Override
    protected String getScmType() {
        return "git";
    }
}

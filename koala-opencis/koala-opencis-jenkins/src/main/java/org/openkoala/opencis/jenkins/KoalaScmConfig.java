package org.openkoala.opencis.jenkins;

/**
 * User: zjzhai
 * Date: 1/21/14
 * Time: 2:26 PM
 */
public abstract class KoalaScmConfig {

    private String scmAddress;

    protected abstract String getScmType();

    protected KoalaScmConfig(String scmAddress) {
        this.scmAddress = scmAddress;
    }

    public String getScmAddress() {
        return scmAddress;
    }

    public void setScmAddress(String scmAddress) {
        this.scmAddress = scmAddress;
    }
}

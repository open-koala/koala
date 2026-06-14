package org.openkoala.opencis.jenkins;

/**
 * User: zjzhai
 * Date: 1/22/14
 * Time: 10:56 AM
 */
public class KoalaSvnConfig extends KoalaScmConfig {


    public KoalaSvnConfig(String scmAddress) {
        super(scmAddress);
    }

    @Override
    protected String getScmType() {
        return "svn";
    }
}

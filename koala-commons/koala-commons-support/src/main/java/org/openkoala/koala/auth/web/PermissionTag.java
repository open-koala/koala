package org.openkoala.koala.auth.web;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Compatibility tag for legacy permission.tld descriptors.
 */
public class PermissionTag extends TagSupport {

    private static final long serialVersionUID = -6891567948979982627L;

    private String identify;

    @Override
    public int doStartTag() throws JspException {
        return EVAL_BODY_INCLUDE;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }
}

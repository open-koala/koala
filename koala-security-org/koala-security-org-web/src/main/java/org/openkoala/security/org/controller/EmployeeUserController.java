package org.openkoala.security.org.controller;

import javax.inject.Inject;

import org.dayatang.querychannel.Page;
import org.openkoala.koala.commons.InvokeResult;
import org.openkoala.security.org.facade.command.*;
import org.openkoala.security.org.facade.SecurityOrgAccessFacade;
import org.openkoala.security.org.facade.SecurityOrgConfigFacade;
import org.openkoala.security.org.facade.dto.AuthorizationCommand;
import org.openkoala.security.org.facade.dto.EmployeeUserDTO;
import org.openkoala.security.org.facade.dto.OrgPermissionDTO;
import org.openkoala.security.org.facade.dto.OrgRoleDTO;
import org.openkoala.security.shiro.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 更改属性，撤销、重置密码，激活、挂起、都在UserController中。
 */
@Controller
@RequestMapping("/auth/employeeUser")
public class EmployeeUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeUserController.class);

    @Inject
    private SecurityOrgConfigFacade securityOrgConfigFacade;

    @Inject
    private SecurityOrgAccessFacade securityOrgAccessFacade;

    /**
     * 添加用户
     *
     * @param command
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public InvokeResult add(CreateEmpolyeeUserCommand command) {
        String createOwner = CurrentUser.getUserAccount();
        command.setCreateOwner(createOwner);
        return securityOrgConfigFacade.createEmployeeUser(command);
    }

    @ResponseBody
    @RequestMapping(value =  "/update", method = RequestMethod.POST)
    public InvokeResult update(ChangeEmployeeUserPropsCommand command){
        return securityOrgConfigFacade.changeEmployeeUserProps(command);
    }

    // ~ 授权

    @ResponseBody
    @RequestMapping(value = "/pagingQueryGrantRoleByUserId", method = RequestMethod.GET)
    public Page<OrgRoleDTO> pagingQueryRolesByUserId(int page, int pagesize, Long userId) {
        return securityOrgAccessFacade.pagingQueryGrantRolesByUserId(page, pagesize, userId);
    }


    /**
     * 根据用户ID分页查询已经授权的权限
     *
     * @param page
     * @param pagesize
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pagingQueryGrantPermissionByUserId", method = RequestMethod.GET)
    public Page<OrgPermissionDTO> pagingQueryGrantPermissionByUserId(int page, int pagesize, Long userId) {
        return  securityOrgAccessFacade.pagingQueryGrantPermissionsByUserId(page, pagesize, userId);
    }


    @ResponseBody
    @RequestMapping(value = "/grantRolesToUserInScope", method = RequestMethod.POST)
    public InvokeResult grantRolesToUserInScope(AuthorizationCommand authorization){
        return securityOrgConfigFacade.grantRolesToUserInScope(authorization);
    }


    @ResponseBody
    @RequestMapping(value = "/terminateUserFromRoleInScope", method = RequestMethod.POST)
    public InvokeResult terminateUserFromRoleInScope(Long userId, TerminateUserFromRoleInScopeCommand[] commands) {
        return securityOrgConfigFacade.terminateUserFromRoleInScope(userId, commands);
    }

    @ResponseBody
    @RequestMapping(value = "/terminateUserFromPermissionInScope", method = RequestMethod.POST)
    public InvokeResult terminateUserFromPermissionInScope(Long userId, TerminateUserFromPermissionInScopeCommand[] commands) {
        return securityOrgConfigFacade.terminateUserFromPermissionInScope(userId, commands);
    }

    @ResponseBody
    @RequestMapping(value = "/pagingQuery", method = RequestMethod.GET)
    public Page<EmployeeUserDTO> pagingQuery(int page, int pagesize, EmployeeUserDTO queryEmployeeUserCondition) {
        return securityOrgAccessFacade.pagingQueryEmployeeUsers(page,pagesize,queryEmployeeUserCondition);
    }

}
package com.panda;

import org.apache.directory.api.ldap.model.constants.AuthenticationLevel;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.schema.SchemaManager;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.LdapPrincipal;
import org.apache.directory.server.core.api.interceptor.context.BindOperationContext;
import org.apache.directory.server.core.authn.AbstractAuthenticator;

/**
 * @Program: VirtualLdapDemo
 * @Description:
 * @Author: JinHu
 * @Create: 2019-09-09 14:47
 */
public class MyAuthenticator extends AbstractAuthenticator {
    private DirectoryService directoryService;

    public MyAuthenticator(AuthenticationLevel type, DirectoryService directoryService) {
        super(type);
        this.directoryService = directoryService;
    }

    public LdapPrincipal authenticate(BindOperationContext ctx) throws LdapException {
        SchemaManager schemaManager = directoryService.getSchemaManager();
        try {
            Dn dn = ctx.getDn();
            String user = LdapUtil.getUserCnByDn(dn.toString());
            if (user == null) {
                user = LdapUtil.getAdminCnByDn(dn.toString());
                if (user == null) {
                    return null;
                }
                else {
                    // TODO authenticate admin authority
                }
            }
            else {
                String inputPassword = new String(ctx.getCredentials());
                Entry entry = LdapUtil.innerGetUserInfoByDn(schemaManager, dn.toString());
                if (!inputPassword.equals(new String(entry.get("userPassword").getBytes()))) {
                    return null;
                }
            }
        } catch (NullPointerException e) {
            throw new LdapException("Authenticate Exception: can't get user's password");
        }

        LdapPrincipal principal = new LdapPrincipal(schemaManager,
                ctx.getDn(),
                AuthenticationLevel.SIMPLE,
                ctx.getCredentials());

        principal.setSchemaManager(schemaManager);
        principal.setUserPassword(ctx.getCredentials());


        return principal;
    }
}

package com.panda;

import org.apache.directory.api.ldap.model.constants.AuthenticationLevel;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.server.ApacheDsService;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.InstanceLayout;
import org.apache.directory.server.core.api.interceptor.Interceptor;
import org.apache.directory.server.core.api.interceptor.context.AddOperationContext;
import org.apache.directory.server.core.authn.AuthenticationInterceptor;
import org.apache.directory.server.core.authn.Authenticator;

import java.util.LinkedList;
import java.util.List;

/**
 * @Program: VirtualLdapDemo
 * @Description:
 * @Author: JinHu
 * @Create: 2019-09-09 15:02
 */
public class VirtualLdapServer {
    private static final String INSTANCE_DIRECTORY_PATH = "instance";
    private static final String BASE_DN = "ou=ldap,o=test";
    private static final String PARTITION_ID = "test";
    private DirectoryService directoryService;

    public void start() {
        initDsService();
        initLdapPartition();
        initLdapAuthenticator();
    }

    private void initDsService() {
        try {
            ApacheDsService apacheDsService = new ApacheDsService();
            apacheDsService.start(new InstanceLayout(INSTANCE_DIRECTORY_PATH));
            directoryService = apacheDsService.getDirectoryService();
            directoryService.setAccessControlEnabled(false);
        } catch (Exception e) {

        }
    }

    private void initLdapPartition() {
        try {
            Dn suffixDn = new Dn(BASE_DN);
            MyPartition myPartition = new MyPartition(directoryService);
            myPartition.setSchemaManager(directoryService.getSchemaManager());
            myPartition.setId(PARTITION_ID);
            myPartition.setSuffixDn(suffixDn);

            Entry entry = new DefaultEntry(
                    BASE_DN,
                    "objectClass: top",
                    "objectClass: organization",
                    "objectClass: organizationalUnit",
                    "o: ccb",
                    "ou: ldap"
            );
            myPartition.setContextEntry(entry);
            myPartition.add(new AddOperationContext(directoryService.getSession(), entry));
            directoryService.addPartition(myPartition);
        } catch (Exception e) {

        }
    }

    private void initLdapAuthenticator() {
        try {
            MyAuthenticator myAuthenticator = new MyAuthenticator(AuthenticationLevel.SIMPLE, directoryService);
            myAuthenticator.init(directoryService);
            AuthenticationInterceptor authenticationInterceptor = new AuthenticationInterceptor();
            authenticationInterceptor.init(directoryService);
            authenticationInterceptor.setAuthenticators(new Authenticator[] {myAuthenticator});
            List<Interceptor> list = new LinkedList<Interceptor>();
            list.add(authenticationInterceptor);
            directoryService.setInterceptors(list);
        } catch (Exception e) {

        }
    }

    public static void main(String []args) {
        VirtualLdapServer virtualLdapServer = new VirtualLdapServer();
        virtualLdapServer.start();
    }
}

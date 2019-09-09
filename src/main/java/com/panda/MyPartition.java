package com.panda;

import org.apache.directory.api.ldap.model.cursor.ListCursor;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.filter.AndNode;
import org.apache.directory.api.ldap.model.filter.EqualityNode;
import org.apache.directory.api.ldap.model.filter.PresenceNode;
import org.apache.directory.api.ldap.model.schema.SchemaManager;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.filtering.EntryFilteringCursor;
import org.apache.directory.server.core.api.filtering.EntryFilteringCursorImpl;
import org.apache.directory.server.core.api.interceptor.context.*;
import org.apache.directory.server.core.api.partition.AbstractPartition;
import org.apache.directory.server.core.api.partition.Subordinates;

import javax.naming.InvalidNameException;
import java.util.LinkedList;
import java.util.List;

/**
 * @Program: VirtualLdapDemo
 * @Description:
 * @Author: JinHu
 * @Create: 2019-09-09 15:00
 */
public class MyPartition extends AbstractPartition {
    private DirectoryService directoryService;

    public MyPartition(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }


    @Override
    protected void doInit() throws InvalidNameException, LdapException {

    }

    @Override
    protected void doRepair() throws LdapException {

    }

    @Override
    public Entry delete(DeleteOperationContext deleteOperationContext) throws LdapException {
        return null;
    }

    @Override
    public void add(AddOperationContext addOperationContext) throws LdapException {

    }

    @Override
    public void modify(ModifyOperationContext modifyOperationContext) throws LdapException {

    }

    @Override
    public EntryFilteringCursor search(SearchOperationContext ctx) throws LdapException {
        SchemaManager schemaManager = directoryService.getSchemaManager();
        try {
            List<Entry> list = new LinkedList<Entry>();
            String filterValue = null;
            Entry entry = null;

            if (ctx.getFilter() instanceof AndNode) {
                AndNode andNode = (AndNode) ctx.getFilter();
                filterValue = (String) andNode.get("cn");
                entry = LdapUtil.innerGetUserInfoByCn(schemaManager, filterValue);
            }
            else if (ctx.getFilter() instanceof EqualityNode) {
                EqualityNode equalityNode = (EqualityNode) ctx.getFilter();
                filterValue = equalityNode.getValue().toString();
                entry = LdapUtil.innerGetUserInfoByCn(schemaManager, filterValue);
            }
            else if (ctx.getFilter() instanceof PresenceNode) {
                entry = LdapUtil.innerGetUserInfoByDn(schemaManager, ctx.getDn().toString());
            }
            else {
                throw new LdapException("search exception");
            }

            list.add(entry);
            return new EntryFilteringCursorImpl(new ListCursor<Entry>(list), ctx, schemaManager);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Entry lookup(LookupOperationContext lookupOperationContext) throws LdapException {
        return null;
    }

    @Override
    public boolean hasEntry(HasEntryOperationContext hasEntryOperationContext) throws LdapException {
        return false;
    }

    @Override
    public void rename(RenameOperationContext renameOperationContext) throws LdapException {

    }

    @Override
    public void move(MoveOperationContext moveOperationContext) throws LdapException {

    }

    @Override
    public void moveAndRename(MoveAndRenameOperationContext moveAndRenameOperationContext) throws LdapException {

    }

    @Override
    public void unbind(UnbindOperationContext unbindOperationContext) throws LdapException {

    }

    @Override
    protected void doDestroy() throws Exception {

    }

    @Override
    public void sync() throws Exception {

    }

    @Override
    public void saveContextCsn() throws Exception {

    }

    @Override
    public Subordinates getSubordinates(Entry entry) throws LdapException {
        return null;
    }
}

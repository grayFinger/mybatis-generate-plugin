package com.dora.mybatis.plugin.api;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.util.StringUtility;

public class RenameFullyQualifiedTable extends FullyQualifiedTable {
    private FullyQualifiedTable table;

    private String domainObjectName;

    public RenameFullyQualifiedTable(String introspectedCatalog, String introspectedSchema, String introspectedTableName, String domainObjectName, String alias, boolean ignoreQualifiersAtRuntime, String runtimeCatalog, String runtimeSchema, String runtimeTableName, boolean delimitIdentifiers, Context context) {
        super(introspectedCatalog, introspectedSchema, introspectedTableName, domainObjectName, alias, ignoreQualifiersAtRuntime, runtimeCatalog, runtimeSchema, runtimeTableName, delimitIdentifiers, context);
        this.domainObjectName = domainObjectName;
    }

    public RenameFullyQualifiedTable(TableConfiguration tc, FullyQualifiedTable table, Context context) {
        this(table.getIntrospectedCatalog(), table
                        .getIntrospectedSchema(), table
                        .getIntrospectedTableName(), table
                        .getDomainObjectName(), table
                        .getAlias(),
                StringUtility.isTrue(tc.getProperty("ignoreQualifiersAtRuntime")), tc
                        .getProperty("runtimeCatalog"), tc
                        .getProperty("runtimeSchema"), tc
                        .getProperty("runtimeTableName"), (tc
                        .isDelimitIdentifiers() ||
                        StringUtility.stringContainsSpace(tc.getCatalog()) ||
                        StringUtility.stringContainsSpace(tc.getSchema()) ||
                        StringUtility.stringContainsSpace(tc.getTableName())), context);
    }

    public String getDomainObjectName() {
        System.out.println("RenameFullQualifiedTable::getDomainObjectName===>" + this.domainObjectName);
        if (this.domainObjectName != null)
            return this.domainObjectName;
        return super.getDomainObjectName();
    }

    public void setDomainObjectName(String domainObjectName) {
        this.domainObjectName = domainObjectName;
    }
}

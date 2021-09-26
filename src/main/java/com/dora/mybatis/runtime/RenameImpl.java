package com.dora.mybatis.runtime;

import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3SimpleImpl;
import org.mybatis.generator.internal.util.StringUtility;

import java.text.MessageFormat;

public class RenameImpl extends IntrospectedTableMyBatis3SimpleImpl {
    protected String calculateMyBatis3XmlMapperFileName() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append(".xml");
        return sb.toString();
    }

    private String getName() {
        String domainObjectName = this.fullyQualifiedTable.getDomainObjectName();
        String searchString = this.tableConfiguration.getProperty("searchString");
        String replaceString = this.tableConfiguration.getProperty("replaceString");
        if (searchString != null) {
            if (replaceString == null)
                replaceString = "";
            domainObjectName = domainObjectName.replaceFirst(searchString, replaceString);
        }
        String mapperName = this.tableConfiguration.getMapperName();
        if (mapperName == null)
            return domainObjectName + "Mapper";
        int ind = mapperName.lastIndexOf('.');
        if (ind != -1)
            mapperName = mapperName.substring(ind + 1);
        return MessageFormat.format(mapperName, new Object[] { domainObjectName });
    }

    protected void calculateJavaClientAttributes() {
        if (this.context.getJavaClientGeneratorConfiguration() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(calculateJavaClientImplementationPackage());
            sb.append('.');
            sb.append(this.fullyQualifiedTable.getDomainObjectName());
            sb.append("DAOImpl");
            setDAOImplementationType(sb.toString());
            sb.setLength(0);
            sb.append(calculateJavaClientInterfacePackage());
            sb.append('.');
            sb.append(this.fullyQualifiedTable.getDomainObjectName());
            sb.append("DAO");
            setDAOInterfaceType(sb.toString());
            sb.setLength(0);
            sb.append(calculateJavaClientInterfacePackage());
            sb.append('.');
            sb.append(getName());
            setMyBatis3JavaMapperType(sb.toString());
            sb.setLength(0);
            sb.append(calculateJavaClientInterfacePackage());
            sb.append('.');
            if (StringUtility.stringHasValue(this.tableConfiguration.getSqlProviderName())) {
                sb.append(this.tableConfiguration.getSqlProviderName());
            } else {
                sb.append(this.fullyQualifiedTable.getDomainObjectName());
                sb.append("SqlProvider");
            }
            setMyBatis3SqlProviderType(sb.toString());
        }
    }
}

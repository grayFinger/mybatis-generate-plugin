package com.dora.mybatis.plugin;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.*;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class KdllDaDataMapperPlugin extends PluginAdapter {

    public boolean validate(List<String> list) {
        return true;
    }

    public boolean sqlMapBaseColumnListElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean sqlMapBlobColumnListElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean sqlMapSelectAllElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean sqlMapResultMapWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        Set<FullyQualifiedJavaType> superInterfaceTypes = interfaze.getSuperInterfaceTypes();
        if (superInterfaceTypes != null && interfaze.getSuperInterfaceTypes().size() > 0) {
            Iterator<FullyQualifiedJavaType> javaTypeIt = superInterfaceTypes.iterator();
            FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
            while (javaTypeIt.hasNext()) {
                FullyQualifiedJavaType javaType = javaTypeIt.next();
                String type = this.context.getJavaClientGeneratorConfiguration().getProperty(javaType.getFullyQualifiedName() + ".type");
                if (type != null && Boolean.valueOf(type).booleanValue()) {
                    javaTypeIt.remove();
                    interfaze.addSuperInterface(new FullyQualifiedJavaType(javaType.getFullyQualifiedName() + "<" + entityType.getShortName() + ">"));
                }
            }
        }
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        try {
            Field field = sqlMap.getClass().getDeclaredField("isMergeable");
            field.setAccessible(true);
            field.setBoolean(sqlMap, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement select = new XmlElement("insert");
        select.addAttribute(new Attribute("id", "insertData"));
        select.addAttribute(new Attribute("parameterType", "list"));
        XmlElement forEach = new XmlElement("foreach");
        forEach.addAttribute(new Attribute("collection", "list"));
        forEach.addAttribute(new Attribute("item", "item"));
        forEach.addAttribute(new Attribute("separator", ","));
        StringBuilder valBuf = new StringBuilder();
        StringBuilder colBuf = new StringBuilder();
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        Iterator<IntrospectedColumn> columnIt = columns.iterator();
        while (columnIt.hasNext()) {
            IntrospectedColumn column = columnIt.next();
            valBuf.append("\t\t#{item.").append(column.getJavaProperty()).append("}");
            colBuf.append("\t\t").append(column.getActualColumnName());
            if (columnIt.hasNext()) {
                valBuf.append(",\n");
                colBuf.append(",\n");
            }
        }
        select.addElement((Element)new TextElement(" insert into " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + " (\n" + colBuf + "\n\t) values "));
        forEach.addElement((Element)new TextElement("(\n" + valBuf + "\n\t)"));
        select.addElement((Element)forEach);
        XmlElement parentElement = document.getRootElement();
        parentElement.addElement((Element)select);
        return true;
    }
}

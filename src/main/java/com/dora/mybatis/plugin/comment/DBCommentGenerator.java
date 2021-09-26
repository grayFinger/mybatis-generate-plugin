package com.dora.mybatis.plugin.comment;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Properties;

public class DBCommentGenerator implements CommentGenerator {
    @Override
    public void addConfigurationProperties(Properties properties) {}
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
            field.addJavaDocLine("/**");
            StringBuilder column = new StringBuilder();
            column.append(" * ");
            column.append(introspectedColumn.getRemarks());
            field.addJavaDocLine(column.toString());
            field.addJavaDocLine(" */");
        }
    }
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {}
    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        innerAddClassComment((InnerClass)topLevelClass, introspectedTable);
    }

    private void innerAddClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
        String comment = introspectedTable.getTableConfiguration().getProperty("comment");
        if (StringUtility.stringHasValue(introspectedTable.getRemarks()))
            comment = introspectedTable.getRemarks();
        if (comment != null) {
            innerClass.addJavaDocLine("/**");
            StringBuilder column = new StringBuilder();
            column.append(" * ");
            column.append(comment);
            innerClass.addJavaDocLine(column.toString());
            innerClass.addJavaDocLine(" */");
        }
    }
    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
        innerAddClassComment(innerClass, introspectedTable);
    }
    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean b) {}
    @Override
    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {}
    @Override
    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {}
    @Override
    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {}
    @Override
    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {}
    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {}
    @Override
    public void addComment(XmlElement xmlElement) {
        xmlElement.addElement((Element)new TextElement("<!--"));
        StringBuilder sb = new StringBuilder();
        sb.append("  WARNING - ");
        sb.append("@mbggenerated");
        xmlElement.addElement((Element)new TextElement(sb.toString()));
        xmlElement.addElement((Element)new TextElement("-->"));
    }

    @Override
    public void addRootComment(XmlElement xmlElement) {}

}

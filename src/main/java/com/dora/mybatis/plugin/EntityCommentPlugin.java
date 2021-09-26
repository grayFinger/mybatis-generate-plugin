package com.dora.mybatis.plugin;

import com.dora.mybatis.plugin.comment.DBCommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Context;

import java.util.List;

public class EntityCommentPlugin extends PluginAdapter {
    public boolean validate(List<String> list) {
        System.out.println("---------------EntityCommentPlugin----------modelBaseRecordClassGenerated-------");
        return true;
    }

    public void setContext(Context context) {
        super.setContext(context);
        System.out.println("---------------EntityCommentPlugin----------modelBaseRecordClassGenerated-------");
        CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();
        commentGeneratorConfiguration.setConfigurationType(DBCommentGenerator.class.getCanonicalName());
        context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);
    }

    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String sequence = introspectedTable.getTableConfiguration().getProperty("sequence");
        System.out.println("---------------EntityCommentPlugin----------modelBaseRecordClassGenerated-------");
        if ("true".equals(sequence)) {
            topLevelClass.addImportedType("com.talkweb.common.annotation.Sequence");
            topLevelClass.addAnnotation("@Sequence");
        }
        return true;
    }
}

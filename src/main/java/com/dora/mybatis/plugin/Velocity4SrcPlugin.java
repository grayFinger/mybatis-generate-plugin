package com.dora.mybatis.plugin;

import com.dora.mybatis.plugin.velocity.VelocityUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.io.File;
import java.util.*;

public class Velocity4SrcPlugin extends PluginAdapter {
    private String templatePath;

    private String targetPath;

    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
    }

    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.templatePath = properties.getProperty("templatePath");
        System.out.println("模板文件路径"+ this.templatePath);
        this.targetPath = properties.getProperty("targetPath");
        System.out.println("目标文件路径"+ this.targetPath);
    }

    public boolean validate(List<String> list) {
        return (this.templatePath != null && this.targetPath != null);
    }

    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        String targetPackage = introspectedTable.getTableConfiguration().getProperty("targetPackage");
        if (targetPackage == null)
            targetPackage = "";
        Map<String, Object> params = new HashMap<>();
        String entityName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        params.put("Entity", entityName);
        params.put("targetPackage", targetPackage);
        params.put("module", getModule(targetPackage));
        params.put("entity", entityName.substring(0, 1).toLowerCase() + entityName.substring(1));
        params.put("columns", introspectedTable.getAllColumns());
        params.put("tableName", introspectedTable.getFullyQualifiedTable().getIntrospectedTableName());
        params.put("table", introspectedTable);
        params.put("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        params.put("author", System.getProperty("user.name"));
        String comment = introspectedTable.getRemarks();
        if (StringUtils.isEmpty(comment))
            comment = introspectedTable.getTableConfiguration().getProperty("comment");
        params.put("comment", comment);
        File templateFilePath = new File(this.templatePath);
        File targetFilePath = new File(this.targetPath);
        if (templateFilePath.exists()) {
            if (targetFilePath.exists())
                targetFilePath.delete();
            targetFilePath.mkdirs();
            evaluate(targetPackage, targetFilePath, templateFilePath, params);
        } else {
            System.out.println("模板文件路径"+ templateFilePath.getAbsolutePath() + "]无效，忽略生成");
        }
        return super.contextGenerateAdditionalJavaFiles(introspectedTable);
    }

    private String getModule(String targetPackage) {
        if (targetPackage != null && targetPackage.length() > 0) {
            int index = targetPackage.lastIndexOf(".");
            if (index > -1)
                return targetPackage.substring(index + 1);
            return targetPackage;
        }
        return targetPackage;
    }

    private void transferTo(String targetPackage, File targetFilePath, File file, Map<String, Object> params) {
        try {
            String targetPath = targetPackage.replaceAll("\\.", "/");
            String fileName = file.getName();
            fileName = VelocityUtils.evaluate(params, fileName);
            File targetFile = new File(targetFilePath.getCanonicalPath() + "/" + targetPath);
            if (!targetFile.exists())
                targetFile.mkdirs();
            targetFile = new File(targetFile.getAbsolutePath() + "/" + fileName);
            if (!targetFile.exists())
                VelocityUtils.transferTo(params, file, targetFile);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void evaluate(String targetPackage, File targetFilePath, File file, Map<String, Object> params) {
        if (file.isDirectory()) {
            String sourceTargetPackage = targetPackage;
            for (File tmp : file.listFiles()) {
                if (!file.getAbsolutePath().equals(this.templatePath))
                    targetPackage = sourceTargetPackage + "." + file.getName();
                System.out.println("targetPackage--------:"+targetPackage);
                evaluate(targetPackage, targetFilePath, tmp, params);
            }
        } else {
            transferTo(targetPackage, targetFilePath, file, params);
        }
    }
}

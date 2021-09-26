package com.dora.mybatis.plugin.velocity;



import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VelocityTools {
    public static String insertBatchColumns(List<IntrospectedColumn> columns) {
        StringBuilder builder = new StringBuilder();
        for (IntrospectedColumn column : columns)
            builder.append(column.getActualColumnName()).append(",");
        if (builder.length() > 0)
            builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public static String insertBatchFields(List<IntrospectedColumn> columns, String prev) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isBlank(prev)) {
            prev = "";
        } else if (!prev.endsWith(".")) {
            prev = prev + ".";
        }
        for (IntrospectedColumn column : columns) {
            builder.append("#{").append(prev).append(column.getJavaProperty());
            if (column.getJdbcType() == 93 || column.getJdbcType() == 91 || column.getJdbcType() == 92)
                builder.append(", jdbcType=TIMESTAMP");
            builder.append("},");
        }
        if (builder.length() > 0)
            builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public static String insertColumns(List<IntrospectedColumn> columns, boolean firstComma) {
        int index = 0;
        StringBuilder builder = new StringBuilder();
        for (IntrospectedColumn column : columns) {
            builder.append("<if test=\"").append(column.getJavaProperty()).append(" != null");
            if (isString(column))
                builder.append(" and ").append(column.getJavaProperty()).append(" != ''");
            builder.append("\">");
            if (index > 0 || firstComma)
                builder.append(",");
            builder.append(column.getActualColumnName());
            builder.append("</if>\n\t\t\t");
            index++;
        }
        if (builder.length() > 0)
            builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public static String insertFields(List<IntrospectedColumn> columns, String prev, boolean firstComma) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isBlank(prev)) {
            prev = "";
        } else if (!prev.endsWith(".")) {
            prev = prev + ".";
        }
        int index = 0;
        for (IntrospectedColumn column : columns) {
            builder.append("<if test=\"").append(column.getJavaProperty()).append(" != null");
            if (isString(column))
                builder.append(" and ").append(column.getJavaProperty()).append(" != ''");
            builder.append("\">");
            if (index > 0 || firstComma)
                builder.append(",");
            builder.append("#{").append(prev).append(column.getJavaProperty());
            if (column.getJdbcType() == 93 || column.getJdbcType() == 91 || column.getJdbcType() == 92)
                builder.append(", jdbcType=TIMESTAMP");
            builder.append("}</if>\n\t\t\t");
            index++;
        }
        if (builder.length() > 0)
            builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    private static boolean isString(IntrospectedColumn column) {
        int jdbcType = column.getJdbcType();
        if (jdbcType == 12 || jdbcType == 1 || jdbcType == 2005 || jdbcType == -15 || jdbcType == 2011 || jdbcType == -9)
            return true;
        return false;
    }

    public static String updateFields(List<IntrospectedColumn> columns, String prev) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isBlank(prev)) {
            prev = "";
        } else if (!prev.endsWith(".")) {
            prev = prev + ".";
        }
        for (IntrospectedColumn column : columns) {
            builder.append("<if test=\"").append(prev).append(column.getJavaProperty()).append(" != null");
            if (isString(column))
                builder.append(" and ").append(prev).append(column.getJavaProperty()).append(" != ''");
            builder.append("\">");
            builder.append(column.getActualColumnName()).append(" = ").append("#{").append(prev).append(column.getJavaProperty());
            if (column.getJdbcType() == 93 || column.getJdbcType() == 91 || column.getJdbcType() == 92)
                builder.append(", jdbcType=TIMESTAMP");
            builder.append("}, </if>\n\t\t\t");
        }
        if (builder.length() > 4)
            builder.deleteCharAt(builder.length() - 4);
        return builder.toString();
    }

    public static String whereFields(List<IntrospectedColumn> columns, String prev, String columnPrev) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isBlank(prev)) {
            prev = "";
        } else if (!prev.endsWith(".")) {
            prev = prev + ".";
        }
        if (StringUtils.isBlank(columnPrev)) {
            columnPrev = "";
        } else {
            columnPrev = columnPrev + ".";
        }
        for (IntrospectedColumn column : columns) {
            builder.append("<if test=\"").append(prev).append(column.getJavaProperty()).append(" != null");
            if (isString(column))
                builder.append(" and ").append(prev).append(column.getJavaProperty()).append(" != ''");
            builder.append("\">");
            builder.append(" and ").append(columnPrev).append(column.getActualColumnName()).append(" = ").append("#{").append(prev).append(column.getJavaProperty());
            if (column.getJdbcType() == 93 || column.getJdbcType() == 91 || column.getJdbcType() == 92)
                builder.append(", jdbcType=TIMESTAMP");
            builder.append("}</if>\n\t\t\t");
        }
        if (builder.length() > 3)
            builder.deleteCharAt(builder.length() - 3);
        return builder.toString();
    }

    public static String resultMap(List<IntrospectedColumn> columns) {
        StringBuilder builder = new StringBuilder();
        for (IntrospectedColumn column : columns) {
            if (column.getJdbcType() == 93 || column.getJdbcType() == 91 || column.getJdbcType() == 92) {
                builder.append("<result property=\"").append(column.getJavaProperty()).append("\" ");
                builder.append("column=\"").append(column.getActualColumnName()).append("\" ");
                builder.append("jdbcType=\"TIMESTAMP\" />\n\t\t");
                continue;
            }
            if (!column.getActualColumnName().equals(column.getJavaProperty())) {
                builder.append("<result property=\"").append(column.getJavaProperty()).append("\" ");
                builder.append("column=\"").append(column.getActualColumnName()).append("\" />\n\t\t");
            }
        }
        if (builder.length() > 3)
            builder.deleteCharAt(builder.length() - 3);
        return builder.toString();
    }

    public static String primaryKey(IntrospectedTable table) {
        List<IntrospectedColumn> columns = table.getPrimaryKeyColumns();
        if (columns != null && columns.size() > 0) {
            StringBuilder builder = new StringBuilder();
            Iterator<IntrospectedColumn> columnIt = columns.iterator();
            while (columnIt.hasNext()) {
                IntrospectedColumn column = columnIt.next();
                builder.append(column.getActualColumnName()).append(" = #{").append(column.getJavaProperty()).append("}");
                if (columnIt.hasNext())
                    builder.append(" and ");
            }
            return builder.toString();
        }
        return null;
    }

    public static String duplicateKey(List<IntrospectedColumn> columns) {
        StringBuilder builder = new StringBuilder();
        if (columns.size() > 0) {
            builder.append("ON DUPLICATE KEY UPDATE\n\t\t\t");
            Iterator<IntrospectedColumn> columnIterator = columns.iterator();
            while (columnIterator.hasNext()) {
                IntrospectedColumn column = columnIterator.next();
                builder.append(column.getActualColumnName()).append(" = values(").append(column.getActualColumnName()).append(")");
                if (columnIterator.hasNext())
                    builder.append(",\n\t\t\t");
            }
        }
        return builder.toString();
    }

    private static String newLine(int tabNum, String... texts) {
        StringBuilder builder = new StringBuilder();
        if (texts.length > 0)
            for (String text : texts)
                builder.append(text);
        builder.append("\n");
        for (int i = 0; i < tabNum; i++)
            builder.append("\t");
        return builder.toString();
    }

    public static String entityImports(List<IntrospectedColumn> columns) {
        StringBuilder imports = new StringBuilder();
        for (IntrospectedColumn column : columns) {
            if (!ignoreEntityImport(column) && column.getFullyQualifiedJavaType().getImportList().size() > 0) {
                List<String> list = new ArrayList<>();
                for (String im : column.getFullyQualifiedJavaType().getImportList()) {
                    if (!list.contains(im)) {
                        list.add(im);
                        imports.append(newLine(0, new String[] { "import ", im, ";" }));
                    }
                }
            }
        }
        return imports.toString();
    }

    public static String entityFields(List<IntrospectedColumn> columns, boolean containMethods) {
        StringBuilder fields = new StringBuilder();
        StringBuilder methods = new StringBuilder();
        StringBuilder builder = new StringBuilder();
        for (IntrospectedColumn column : columns) {
            if (StringUtils.isNotEmpty(column.getRemarks())) {
                fields.append(newLine(1, new String[] { "/**" }));
                fields.append(newLine(1, new String[] { " * ", column.getRemarks() }));
                fields.append(newLine(1, new String[] { "*/" }));
            }
            String shortName = getJavaTypeShortName(column);
            fields.append(newLine(1, new String[] { "private ", shortName, " ", column.getJavaProperty(), ";" }));
            fields.append(newLine(1, new String[0]));
            if (containMethods) {
                String prop = column.getJavaProperty().substring(0, 1).toUpperCase() + column.getJavaProperty().substring(1);
                methods.append(newLine(2, new String[] { "public ", shortName, " get", prop, "() {" }));
                methods.append(newLine(1, new String[] { "return ", column.getJavaProperty(), ";" }));
                methods.append(newLine(1, new String[] { "}" }));
                methods.append(newLine(1, new String[0]));
                methods.append(newLine(2, new String[] { "public void set", prop, "(", shortName, " ", column.getJavaProperty(), ") {" }));
                methods.append(newLine(1, new String[] { "this.", column.getJavaProperty(), " = ", column.getJavaProperty(), ";" }));
                methods.append(newLine(1, new String[] { "}" }));
                methods.append(newLine(1, new String[0]));
            }
        }
        builder.append(fields);
        builder.append(methods);
        return builder.toString();
    }

    private static boolean ignoreEntityImport(IntrospectedColumn column) {
        if (column.getJdbcType() == 3 && column.getScale() == 0)
            return true;
        return false;
    }

    private static String getJavaTypeShortName(IntrospectedColumn column) {
        if (column.getJdbcType() == 3 && column.getScale() == 0) {
            if (column.getLength() < 22)
                return "Integer";
            return "Long";
        }
        return column.getFullyQualifiedJavaType().getShortName();
    }

    public static String entityFields(List<IntrospectedColumn> columns) {
        return entityFields(columns, true);
    }
}

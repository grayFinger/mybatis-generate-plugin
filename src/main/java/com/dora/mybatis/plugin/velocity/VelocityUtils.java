package com.dora.mybatis.plugin.velocity;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class VelocityUtils {
    public static VelocityContext buildVelocityContext(Map<String, Object> context) {
        VelocityContext context_ = new VelocityContext();
        if (context != null && context.size() > 0) {
            Iterator<String> it = context.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                context_.put(key, context.get(key));
            }
        }
        context_.put("tools", new VelocityTools());
        return context_;
    }

    public static String evaluate(Map<String, Object> context, String templateName, String template) {
        StringWriter out = new StringWriter();
        try {
            VelocityContext velocityContext = buildVelocityContext(context);
            Velocity.evaluate((Context)velocityContext, out, templateName, template);
            return out.toString();
        } catch (Throwable t) {
            t.printStackTrace();
            return template;
        }
    }

    public static void transferTo(Map<String, Object> context, File source, File target) {
        Writer writer = null;
        Reader reader = null;
        try {
            VelocityContext velocityContext = buildVelocityContext(context);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), "UTF-8"));
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(source), "UTF-8"));
            Velocity.evaluate((Context)velocityContext, writer, "transferTo", reader);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException iOException) {}
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException iOException) {}
        }
    }

    public static String evaluate(Map<String, Object> context, String template) {
        return evaluate(context, template, template);
    }
}

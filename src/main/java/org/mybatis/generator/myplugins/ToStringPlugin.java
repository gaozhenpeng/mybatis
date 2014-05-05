package org.mybatis.generator.myplugins;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * <B>�ļ����ƣ�</B>ToStringPlugin.java<BR>
 * <B>�ļ�������</B><BR>
 * <BR>
 * <B>��Ȩ������</B>(C)2013-2015<BR>
 * <B>��˾���ţ�</B>NERCIS �����о��� <BR>
 * <B>����ʱ�䣺</B>2013��11��12��<BR>
 * 
 * @author ������ gaozhp@nercis.ac.cn
 * @version 
 */
public class ToStringPlugin extends PluginAdapter {
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }
    
    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }

    private void generateToString(IntrospectedTable introspectedTable,
            TopLevelClass topLevelClass) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setName("toString"); //$NON-NLS-1$
        if (introspectedTable.isJava5Targeted()) {
            method.addAnnotation("@Override"); //$NON-NLS-1$
        }

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        method.addBodyLine("return org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString(this);"); //$NON-NLS-1$
       
        topLevelClass.addMethod(method);
    }
}


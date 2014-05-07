package org.mybatis.generator.myplugins;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * 
 * <B>文件名称：</B>IbatisServicePlugin.java<BR>
 * <B>文件描述：</B><BR>
 * <BR>
 * <B>版权声明：</B>(C)2014-2015<BR>
 * <B>公司部门：</B>研究部 <BR>
 * <B>创建时间：</B>2014年5月7日<BR>
 * 
 * @author 高振鹏 gaozhenpeng@aliyun.com
 * @version
 */
public class IbatisServicePlugin  extends PluginAdapter {

    private FullyQualifiedJavaType serviceType;
    private FullyQualifiedJavaType autowired;
    private FullyQualifiedJavaType service;
    private FullyQualifiedJavaType transactional;
    private FullyQualifiedJavaType mapper;
    
    private String servicePack;
    private String project;
    private String pojoUrl;
    private String sqlmapUrl;
    
    private boolean enableAnnotation = true;
    
    java.text.DateFormat format = new java.text.SimpleDateFormat(  
            "yyyy-MM-dd");  

    private String javaDocLine="/** "
            + '\r'+"* <B>文件名称：</B>Service.java<BR>"
            + '\r'+"* <B>文件描述：</B><BR>"
            + '\r'+"* <BR>"
            + '\r'+"* <B>版权声明：</B>(C)2013-2015<BR>"
            + '\r'+"* <B>公司部门：</B> 基础研究部 <BR>"
            + '\r'+"* <B>创建时间：</B>"+format.format(new Date())+"<BR>"
            + '\r'+"*"
            + '\r'+"* @author 高振鹏 gaozhenpeng@aliyun.com"
            + '\r'+"* @version 1.0 "
            + '\r'+"*/";
    @Override
    public boolean validate(List<String> warnings) {

        pojoUrl = context.getJavaModelGeneratorConfiguration().getTargetPackage();
        sqlmapUrl = context.getJavaClientGeneratorConfiguration().getTargetPackage();
        servicePack = properties.getProperty("servicePack");
        project = properties.getProperty("targetProject");
        
        if (enableAnnotation) {
            autowired = new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired");
            service = new FullyQualifiedJavaType("org.springframework.stereotype.Service");
            transactional = new FullyQualifiedJavaType("org.springframework.transaction.annotation.Transactional");
        }
        // TODO Auto-generated method stub
        return true;
    }
    
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> files = new ArrayList<GeneratedJavaFile>();
        
        String table = introspectedTable.getBaseRecordType();
        String tableName = table.replaceAll(this.pojoUrl + ".", "");
        serviceType = new FullyQualifiedJavaType(servicePack + "." + tableName + "Service");
        TopLevelClass topLevelClass = new TopLevelClass(serviceType);
        addServiceImpl(topLevelClass, introspectedTable, tableName, files);
              
        return files;
    }
    
    /**
     * 添加实现类
     * 
     * @param introspectedTable
     * @param tableName
     * @param files
     */
    protected void addServiceImpl(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, String tableName,
                    List<GeneratedJavaFile> files) {
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.setSuperClass("CommonsService<T, Q,PK>");
        topLevelClass.addJavaDocLine(javaDocLine.replace("Service.java", tableName+"Service.java"));
       
        if (enableAnnotation) {
            topLevelClass.addAnnotation("@Service(\""+uncapitalize(tableName)+"Service\")");
            topLevelClass.addAnnotation("@Transactional");
            
            topLevelClass.addImportedType(service);
            topLevelClass.addImportedType(transactional);
            topLevelClass.addImportedType(autowired);
            
         }
         // 添加引用mapper
        addField(topLevelClass, tableName);
        
        addMethod(topLevelClass, tableName);
        // 添加方法
        //topLevelClass.addMethod(countByExample(introspectedTable, tableName));
        GeneratedJavaFile file = new GeneratedJavaFile(topLevelClass, project,"utf-8",new DefaultJavaFormatter());
        
        files.add(file);
    }
    
    /**
     * 添加注释
     * 
     * @param field
     * @param comment
     */
    protected void addComment(JavaElement field, String comment) {
        StringBuilder sb = new StringBuilder();
        field.addJavaDocLine("/**");
        sb.append(" * ");
        //comment = comment.replaceAll(OutputUtilities.newLine("");.lineSeparator, "<br>"+OutputUtilities.lineSeparator+"\t * ");
        sb.append(comment);
        field.addJavaDocLine(sb.toString());
        field.addJavaDocLine(" */");
    }
    
    /**
     * 添加字段
     * 
     * @param topLevelClass
     */
    protected void addField(TopLevelClass topLevelClass, String tableName) {
            // 添加 dao
            Field field = new Field();
            String mapperName=tableName+"Mapper";
            field.setName(uncapitalize(mapperName)); // 设置变量名
            topLevelClass.addImportedType(sqlmapUrl+"."+mapperName);
            field.setType(new FullyQualifiedJavaType(mapperName+"<T, Q, PK>")); // 类型
            //addComment(field, "执行结果");
            field.setVisibility(JavaVisibility.PUBLIC);
            if (enableAnnotation) {
                    field.addAnnotation("@Autowired");
            }
            topLevelClass.addField(field);
    }
    
    /**
     * 添加方法
     * 
     */
    protected void addMethod(TopLevelClass topLevelClass,String tableName) {
            String mapperName=tableName+"Mapper";
            Method method = new Method();
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addAnnotation("@Override");
            method.setReturnType(new FullyQualifiedJavaType(mapperName+"<T, Q, PK>"));
            method.setName("getMapper");
            //method.addParameter(new Parameter(FullyQualifiedJavaType.getBooleanPrimitiveInstance(), "success"));
            method.addBodyLine(" // TODO Auto-generated method stub");
            method.addBodyLine("return mapper;");
            topLevelClass.addMethod(method);
    }
    
    public static String uncapitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuilder(strLen)
            .append(Character.toLowerCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
    }
    
}


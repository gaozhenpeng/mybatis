/**
 * 
 */
package org.mybatis.generator;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.IgnoredColumn;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.DefaultShellCallback;


/**
 * @author 振鹏
 *
 */
public class Generator {
	
	public static void main (String arg[]) throws Exception{
		   List<String> warnings = new ArrayList<String>();
		   boolean overwrite = true;
		   String entry = "E:\\mysql-connector-java-5.1.22.jar";
		   String driverClass="com.mysql.jdbc.Driver"; //${jdbc.driver}
		   String connectionURL="jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8"; //${jdbc.url}
		   String userId="root"; //${jdbc.username}
		   String password="master"; //${jdbc.password}
		   
		   String targetPackage="com.nercis.isscp.dbengine"; //${targetPackage}
		   String targetProject="E:\\myworkspace\\mybatis-test\\src\\main\\java"; //${targetProject}
		   
		   String configurationType="XMLMAPPER"; 
		   
		   Context context = new Context(ModelType.FLAT);
		   context.setId("myBatast");
		   context.setTargetRuntime("MyBatis3");
		   
		   //<plugin>
		   PluginConfiguration  pluginConfiguration = new PluginConfiguration();
		   pluginConfiguration.setConfigurationType("org.mybatis.generator.myplugins.ToStringPlugin");
		   pluginConfiguration.setConfigurationType("org.mybatis.generator.plugins.SerializablePlugin");
		   pluginConfiguration.setConfigurationType("org.mybatis.generator.plugins.RenameExampleClassPlugin");
		   pluginConfiguration.addProperty("searchString", "Example$");
		   pluginConfiguration.addProperty("replaceString", "Query");
		   context.addPluginConfiguration(pluginConfiguration);
		   
		   // <commentGenerator>
		   CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();
		   commentGeneratorConfiguration.addProperty("suppressAllComments", Boolean.TRUE.toString());
		   context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);
		   
		   //<jdbcConnection>
		   JDBCConnectionConfiguration jdbcConnectionConfiguration =new JDBCConnectionConfiguration();
		   jdbcConnectionConfiguration.setDriverClass(driverClass);
		   jdbcConnectionConfiguration.setConnectionURL(connectionURL);
		   jdbcConnectionConfiguration.setUserId(userId);
		   jdbcConnectionConfiguration.setPassword(password);
		   context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);
		   
		   //<javaModelGenerator>
		   JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
		   javaModelGeneratorConfiguration.setTargetPackage(targetPackage+".domain");
		   javaModelGeneratorConfiguration.setTargetProject(targetProject);
		   javaModelGeneratorConfiguration.addProperty("trimStrings", Boolean.TRUE.toString());
		   context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);
		   
		   //<sqlMapGenerator>
		   SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
		   sqlMapGeneratorConfiguration.setTargetPackage(targetPackage+".repository.mapper");
		   sqlMapGeneratorConfiguration.setTargetProject(targetProject);
		   //sqlMapGeneratorConfiguration.addProperty("", "");
		   context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);
		   
		   //<javaClientGenerator>
		   JavaClientGeneratorConfiguration javaClientGeneratorConfiguration =  new JavaClientGeneratorConfiguration();
		   javaClientGeneratorConfiguration.setTargetPackage(targetPackage+".repository");
		   javaClientGeneratorConfiguration.setTargetProject(targetProject);
		   javaClientGeneratorConfiguration.setConfigurationType(configurationType);
		   javaClientGeneratorConfiguration.addProperty("enableSubPackages", Boolean.FALSE.toString());
		   javaClientGeneratorConfiguration.addProperty("methodNameCalculator", "extended");
		   context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);
		   
		   //<table>
		   TableConfiguration tc = new TableConfiguration(context);
		   tc.setTableName("user");
		   tc.setDomainObjectName("User");
		   IgnoredColumn ignoredColumn = new IgnoredColumn("id");
		   tc.addIgnoredColumn(ignoredColumn);
		   
		   tc.setSelectByExampleStatementEnabled(Boolean.TRUE);
		   tc.setSelectByPrimaryKeyStatementEnabled(Boolean.TRUE);
		   tc.setUpdateByExampleStatementEnabled(Boolean.TRUE);
		   tc.setUpdateByPrimaryKeyStatementEnabled(Boolean.TRUE);
		   tc.setInsertStatementEnabled(Boolean.TRUE);
		   tc.setDeleteByExampleStatementEnabled(Boolean.TRUE);
		   tc.setDeleteByPrimaryKeyStatementEnabled(Boolean.TRUE);
		   tc.setDelimitIdentifiers(Boolean.TRUE);
		   
		   context.addTableConfiguration(tc);
		   
		   Configuration config = new Configuration();
		   config.addClasspathEntry(entry);
		   config.addContext(context);
		  

		   //   ... fill out the config object as appropriate...

		   DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		   MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		   myBatisGenerator.generate(null);
	}

}

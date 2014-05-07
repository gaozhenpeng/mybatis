package org.mybatis.generator.myplugins;

import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * 
 * <B>文件名称：</B>SelectOneByExamplePlugin.java<BR>
 * <B>文件描述：</B><BR>
 * <BR> Adds "selectOneByExample" method to the appropriate Mapper interface returning exactly one object instance.<br/>
 * Example configuration:<br/>
 * <tt>
 * <pre>
 * &lt;generatorConfiguration&gt;
 *  &lt;context ...&gt;
 *
 *      &lt;plugin type="org.mybatis.generator.myplugins.SelectOneByExamplePlugin"/&gt;
 *      ...
 *
 *  &lt;/context&gt;
 * &lt;/generatorConfiguration&gt;
 * </pre>
 * </tt>
 * <br/> Properties:<br/> <ul> <li><strong>methodToGenerate</strong> (optional) : the name of the method to generate.
 * Default: <strong>selectOneByExample</strong></li> <li><strong>excludeClassNamesRegexp</strong> (optional): classes to
 * exclude from generation as regular expression. Default: none</li> </ul>
 *
 * <B>版权声明：</B>(C)2014-2015<BR>
 * <B>公司部门：</B>研究部 <BR>
 * <B>创建时间：</B>2014年5月7日<BR>
 * 
 * @author 高振鹏 gaozhenpeng@aliyun.com
 * @version
 */
public class SelectOneByExamplePlugin extends PluginAdapter {

    private Config config;

    /**
     * {@inheritDoc}
     */
    public boolean validate(List<String> warnings) {
        if (this.config == null)
            this.config = new Config(getProperties());
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze,
                                                                 IntrospectedTable introspectedTable) {

        if (!config.shouldExclude(interfaze.getType()))
            interfaze.addMethod(generateSelectOneByExample(method, introspectedTable));

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
                                                                    IntrospectedTable introspectedTable) {

        if (!config.shouldExclude(interfaze.getType()))
            interfaze.addMethod(generateSelectOneByExample(method, introspectedTable));

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method,
                                                                 TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (!config.shouldExclude(topLevelClass.getType()))
            topLevelClass.addMethod(generateSelectOneByExample(method, introspectedTable));
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method,
                                                                    TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (!config.shouldExclude(topLevelClass.getType()))
            topLevelClass.addMethod(generateSelectOneByExample(method, introspectedTable));
        return true;
    }

    private Method generateSelectOneByExample(Method method, IntrospectedTable introspectedTable) {
        System.out.println("==========kao");
        Method m = new Method(config.methodToGenerate);
        m.setVisibility(method.getVisibility());
        FullyQualifiedJavaType returnType = introspectedTable.getRules().calculateAllFieldsClass();
        m.setReturnType(returnType);

        List<String> annotations = method.getAnnotations();
        for (String a : annotations) {
            m.addAnnotation(a);
        }

        List<Parameter> params = method.getParameters();
        for (Parameter p : params) {
            m.addParameter(p);
        }

        context.getCommentGenerator().addGeneralMethodComment(m, introspectedTable);
        return m;
    }

    private static final class Config extends BasePluginConfig {

        private static final String defaultMethodToGenerate = "selectOneByExample";
        private static final String methodToGenerateKey = "methodToGenerate";

        private String methodToGenerate;

        protected Config(Properties props) {
            super(props);
            this.methodToGenerate = props.getProperty(methodToGenerateKey, defaultMethodToGenerate);
        }
    }
}

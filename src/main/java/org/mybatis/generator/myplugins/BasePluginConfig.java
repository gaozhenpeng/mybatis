package org.mybatis.generator.myplugins;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.util.Properties;
import java.util.regex.Pattern;

/**
 * 
 * <B>�ļ����ƣ�</B>BasePluginConfig.java<BR>
 * <B>�ļ�������</B><BR>
 * <BR>
 * <B>��Ȩ������</B>(C)2014-2015<BR>
 * <B>��˾���ţ�</B>�о��� <BR>
 * <B>����ʱ�䣺</B>2014��5��7��<BR>
 * 
 * @author ������ gaozhenpeng@aliyun.com
 * @version
 */
public abstract class BasePluginConfig {

    private static final String excludeClassNamesRegexpKey = "excludeClassNamesRegexp";

    private Pattern excludeClassNamesRegexp;

    protected BasePluginConfig(Properties props) {

        Pattern excludeClassNamesRegexp = null;
        String regexp = props.getProperty(excludeClassNamesRegexpKey, null);
        if (regexp != null)
            this.excludeClassNamesRegexp = Pattern.compile(regexp);
    }


    boolean shouldExclude(FullyQualifiedJavaType type) {
        return this.shouldExclude(type.getFullyQualifiedName());
    }

    boolean shouldExclude(String className) {

        if (excludeClassNamesRegexp != null && excludeClassNamesRegexp.matcher(className).matches()) {
            return true;
        }

        return false;
    }
}

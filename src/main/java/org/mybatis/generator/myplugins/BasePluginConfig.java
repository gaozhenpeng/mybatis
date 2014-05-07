package org.mybatis.generator.myplugins;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.util.Properties;
import java.util.regex.Pattern;

/**
 * 
 * <B>文件名称：</B>BasePluginConfig.java<BR>
 * <B>文件描述：</B><BR>
 * <BR>
 * <B>版权声明：</B>(C)2014-2015<BR>
 * <B>公司部门：</B>研究部 <BR>
 * <B>创建时间：</B>2014年5月7日<BR>
 * 
 * @author 高振鹏 gaozhenpeng@aliyun.com
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

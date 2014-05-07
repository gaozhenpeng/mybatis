/**
 * 
 */
package org.mybatis.generator.myplugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * 
 * <B>文件名称：</B>DeleteBatchPlugin.java<BR>
 * <B>文件描述：</B><BR>
 * <BR>mybatis generator 批量删除插件
 * <B>版权声明：</B>(C)2014-2015<BR>
 * <B>公司部门：</B>研究部 <BR>
 * <B>创建时间：</B>2014年5月7日<BR>
 * 
 * @author 高振鹏 gaozhenpeng@aliyun.com
 * @version
 */
public class DeleteBatchPlugin extends PluginAdapter{

	@Override
	public boolean validate(List<String> warnings) {
		// TODO Auto-generated method stub
		return true;
	}

	 @Override
	    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {

	        XmlElement answer = new XmlElement("delete"); //$NON-NLS-1$

	        answer.addAttribute(new Attribute(
	                "id", "deleteBatch")); //$NON-NLS-1$
	        
	        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
	                "java.util.List"));

	        context.getCommentGenerator().addComment(answer);

	        StringBuilder sb = new StringBuilder();
	        sb.append("delete from "); //$NON-NLS-1$
	        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
	        answer.addElement(new TextElement(sb.toString()));

	        boolean and = false;
	        for (IntrospectedColumn introspectedColumn : introspectedTable
	                .getPrimaryKeyColumns()) {
	            sb.setLength(0);
	            if (and) {
	                sb.append("  and "); //$NON-NLS-1$
	            } else {
	                sb.append("where "); //$NON-NLS-1$
	                and = true;
	            }

	            sb.append(MyBatis3FormattingUtilities
	                    .getEscapedColumnName(introspectedColumn));
	            sb.append(" in "); //$NON-NLS-1$
	            sb.append("\n"); //$NON-NLS-1$
	               
		        sb.append("<foreach collection=\"list\" item=\"item\" index=\"index\"  open=\"(\" separator=\",\" close=\")\">"); //$NON-NLS-1$
		        sb.append("\n"); //$NON-NLS-1$
	            sb.append("#{item} ");
	            sb.append("\n"); //$NON-NLS-1$
		        sb.append("</foreach>");
	            answer.addElement(new TextElement(sb.toString()));
	        }

	        if (context.getPlugins()
	                .sqlMapDeleteByPrimaryKeyElementGenerated(answer,
	                        introspectedTable)) {
	        	document.getRootElement().addElement(answer);
	        }
			return true;
	 }
	    
	
}

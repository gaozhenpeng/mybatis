/**
 * 
 */
package org.mybatis.generator.myplugins;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.myplugins.CachePlugin.CacheProperty;

/**
 * @author ’Ò≈Ù
 *
 */
public class InsertBatchPlugin extends PluginAdapter{

	@Override
	public boolean validate(List<String> warnings) {
		// TODO Auto-generated method stub
		return true;
	}

	 @Override
	    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		 XmlElement answer = new XmlElement("insert"); //$NON-NLS-1$
		 
	        answer.addAttribute(new Attribute(
	                 "id", "insertBatch")); //$NON-NLS-1$

	        FullyQualifiedJavaType parameterType = introspectedTable.getRules()
	                 .calculateAllFieldsClass();

	         answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
	                 "java.util.List"));


	        context.getCommentGenerator().addComment(answer);

	        GeneratedKey gk = introspectedTable.getGeneratedKey();
	        if (gk != null) {
	            IntrospectedColumn introspectedColumn = introspectedTable
	                    .getColumn(gk.getColumn());
	            // if the column is null, then it's a configuration error. The
	            // warning has already been reported
	            if (introspectedColumn != null) {
	                if (gk.isJdbcStandard()) {
	                    answer.addAttribute(new Attribute(
	                            "useGeneratedKeys", "true")); //$NON-NLS-1$ //$NON-NLS-2$
	                    answer.addAttribute(new Attribute(
	                            "keyProperty", introspectedColumn.getJavaProperty())); //$NON-NLS-1$
	                } else {
	                   // answer.addElement(getSelectKey(introspectedColumn, gk));
	                }
	            }
	        }

	        StringBuilder insertClause = new StringBuilder();
	        StringBuilder valuesClause = new StringBuilder();

	        insertClause.append("insert into "); //$NON-NLS-1$
	        insertClause.append(introspectedTable
	                .getFullyQualifiedTableNameAtRuntime());
	        insertClause.append(" ("); //$NON-NLS-1$

	        valuesClause.append("values"); //$NON-NLS-1$
	        valuesClause.append("\n"); //$NON-NLS-1$
	        valuesClause.append("<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\" > "); //$NON-NLS-1$
	        valuesClause.append("\n"); //$NON-NLS-1$
	        valuesClause.append("("); //$NON-NLS-1$
	       

	        List<String> valuesClauses = new ArrayList<String>();
	        Iterator<IntrospectedColumn> iter = introspectedTable.getAllColumns()
	                .iterator();
	        while (iter.hasNext()) {
	            IntrospectedColumn introspectedColumn = iter.next();
	            if (introspectedColumn.isIdentity()) {
	                // cannot set values on identity fields
	                continue;
	            }

	            insertClause.append(MyBatis3FormattingUtilities
	                    .getEscapedColumnName(introspectedColumn));
	            valuesClause.append(getParameterClause(introspectedColumn));
	            if (iter.hasNext()) {
	                insertClause.append(", "); //$NON-NLS-1$
	                valuesClause.append(", "); //$NON-NLS-1$
	            }

	            if (valuesClause.length() > 80) {
	                answer.addElement(new TextElement(insertClause.toString()));
	                insertClause.setLength(0);
	                OutputUtilities.xmlIndent(insertClause, 1);

	                valuesClauses.add(valuesClause.toString());
	                valuesClause.setLength(0);
	                OutputUtilities.xmlIndent(valuesClause, 1);
	            }
	        }

	        insertClause.append(')');
	        answer.addElement(new TextElement(insertClause.toString()));

	        valuesClause.append(')');
	        valuesClause.append("\n"); //$NON-NLS-1$
	        valuesClause.append("</foreach>");
	         
	        valuesClauses.add(valuesClause.toString());

	        for (String clause : valuesClauses) {
	            answer.addElement(new TextElement(clause));
	        }

	        if (context.getPlugins().sqlMapInsertElementGenerated(answer,
	                introspectedTable)) {
	            //parentElement.addElement(answer);
	            document.getRootElement().addElement(answer);
	        }
	        return true;
	 }
	    
	 private static String getParameterClause(
	            IntrospectedColumn introspectedColumn) {
	        return getParameterClause(introspectedColumn, null);
	    }

	    private static String getParameterClause(
	            IntrospectedColumn introspectedColumn, String prefix) {
	        StringBuilder sb = new StringBuilder();

	        sb.append("#{item."); //$NON-NLS-1$
	        sb.append(introspectedColumn.getJavaProperty(prefix));
	        sb.append(",jdbcType="); //$NON-NLS-1$
	        sb.append(introspectedColumn.getJdbcTypeName());

	        if (stringHasValue(introspectedColumn.getTypeHandler())) {
	            sb.append(",typeHandler="); //$NON-NLS-1$
	            sb.append(introspectedColumn.getTypeHandler());
	        }

	        sb.append('}');

	        return sb.toString();
	    }
}

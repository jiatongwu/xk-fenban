package cn.xvkang.jfinal.directive;

import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import cn.xvkang.dao.TXmsMapper;
import cn.xvkang.entity.TXms;
import cn.xvkang.utils.SpringUtils;

public class XmIdToNameDirective extends Directive {


	@Override
	public void exec(Env arg0, Scope scope, Writer writer) {
		TXmsMapper tXmsMapper = SpringUtils.ac.getBean(TXmsMapper.class);
		Object[] values = exprList.evalExprList(scope);
		try {
			String xmid = (String) values[0];
			TXms selectByPrimaryKey = tXmsMapper.selectByPrimaryKey(xmid);
			write(writer, selectByPrimaryKey.getXm());
		} catch (Exception e) {
			e.printStackTrace();
			write(writer, "");
		}

	}
}

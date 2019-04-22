package cn.xvkang.jfinal.directive;

import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

public class SchoolIdToNameDirective extends Directive {




	@Override
	public void exec(Env arg0, Scope scope, Writer writer) {
		//Object[] values = exprList.evalExprList(scope);
		write(writer, "name");
		
	}
}

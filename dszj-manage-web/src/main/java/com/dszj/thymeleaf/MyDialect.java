package com.dszj.thymeleaf;

import java.util.LinkedHashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * 
 * @author yys
 */
public class MyDialect extends AbstractProcessorDialect implements IExpressionObjectDialect {

    private static final String NAME = "MyDialect";
    private static final String PREFIX = "my";
    private IExpressionObjectFactory expressionObjectFactory = null;

    public MyDialect() {
        super(NAME, PREFIX, StandardDialect.PROCESSOR_PRECEDENCE);
    }

    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        LinkedHashSet processors = new LinkedHashSet();
        processors.add(new SelectDictAttrProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new SelectListAttrProcessor(TemplateMode.HTML, dialectPrefix));
        return processors;
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        if (this.expressionObjectFactory == null) {
            this.expressionObjectFactory = new MyExpressionObjectFactory();
        }
        return this.expressionObjectFactory;
    }
}

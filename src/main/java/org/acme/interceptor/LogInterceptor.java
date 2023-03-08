package org.acme.interceptor;

import java.io.IOException;
import javax.inject.Inject;
import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import org.acme.service.LogService;

@Provider
@ConstrainedTo(RuntimeType.SERVER)
public class LogInterceptor implements ReaderInterceptor {

    @Inject
    protected LogService logService;

    @Override
    public Object aroundReadFrom(final ReaderInterceptorContext context) throws IOException, WebApplicationException {
        var entity = context.proceed();
        logService.log((String) entity);
        return entity;
    }
}

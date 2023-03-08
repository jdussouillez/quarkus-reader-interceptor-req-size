# quarkus-reader-interceptor-req-size

Simple project based on Quarkus [rest-client-reactive-quickstart](https://github.com/quarkusio/quarkus-quickstarts/tree/2.16.4.Final/rest-client-reactive-quickstart) to reproduce a bug.

Linked issues:
- https://github.com/quarkusio/quarkus/issues/31692
- https://github.com/quarkusio/quarkus/issues/28446

## How to run

1. Run the server

```sh
./mvnw quarkus:dev
```

2. Call the API with a body of less than 10000 bytes. It works.

```sh
$ curl -i -X POST http://localhost:8080/test -H "Content-Type: application/octet-stream" --data-binary "@tests/9kb"
```

Output:
```
HTTP/1.1 100 Continue

HTTP/1.1 200 OK
content-length: 4
Content-Type: application/json;charset=UTF-8

true
```

3. Call the API with a body of more than 10000 bytes. Now it fails.

```sh
$ curl -i -X POST http://localhost:8080/test -H "Content-Type: application/octet-stream" --data-binary "@tests/10kb"
```

Output:
```
HTTP/1.1 100 Continue

HTTP/1.1 500 Internal Server Error
content-type: application/json; charset=utf-8
content-length: 2074

{"details":"Error id 3dc3e65e-a1dd-41a4-ad22-3946f969a450-4, javax.enterprise.context.ContextNotActiveException: RequestScoped context was not active when trying to obtain a bean instance for a client proxy of CLASS bean [class=org.acme.service.LogService, id=922c737080882371f004038baf0a71f17589d79d]","stack":"javax.enterprise.context.ContextNotActiveException: RequestScoped context was not active when trying to obtain a bean instance for a client proxy of CLASS bean [class=org.acme.service.LogService, id=922c737080882371f004038baf0a71f17589d79d]\n\t- you can activate the request context for a specific method using the @ActivateRequestContext interceptor binding\n\tat io.quarkus.arc.impl.ClientProxies.getDelegate(ClientProxies.java:55)\n\tat org.acme.service.LogService_ClientProxy.arc$delegate(Unknown Source)\n\tat org.acme.service.LogService_ClientProxy.log(Unknown Source)\n\tat org.acme.interceptor.LogInterceptor.aroundReadFrom(LogInterceptor.java:23)\n\tat org.jboss.resteasy.reactive.server.jaxrs.ReaderInterceptorContextImpl.proceed(ReaderInterceptorContextImpl.java:54)\n\tat org.jboss.resteasy.reactive.server.handlers.RequestDeserializeHandler.handle(RequestDeserializeHandler.java:88)\n\tat io.quarkus.resteasy.reactive.server.runtime.QuarkusResteasyReactiveRequestContext.invokeHandler(QuarkusResteasyReactiveRequestContext.java:108)\n\tat org.jboss.resteasy.reactive.common.core.AbstractResteasyReactiveContext.run(AbstractResteasyReactiveContext.java:145)\n\tat io.quarkus.vertx.core.runtime.VertxCoreRecorder$14.runWith(VertxCoreRecorder.java:576)\n\tat org.jboss.threads.EnhancedQueueExecutor$Task.run(EnhancedQueueExecutor.java:2449)\n\tat org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run(EnhancedQueueExecutor.java:1478)\n\tat org.jboss.threads.DelegatingRunnable.run(DelegatingRunnable.java:29)\n\tat org.jboss.threads.ThreadLocalResettingRunnable.run(ThreadLocalResettingRunnable.java:29)\n\tat io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)\n\tat java.base/java.lang.Thread.run(Thread.java:833)"}
```

Stacktrace on the server side :

```
2023-03-08 14:40:30,265 ERROR [org.jbo.res.rea.com.cor.AbstractResteasyReactiveContext] (executor-thread-0) Request failed: javax.enterprise.context.ContextNotActiveException: RequestScoped context was not active when trying to obtain a bean instance for a client proxy of CLASS bean [class=org.acme.service.LogService, id=922c737080882371f004038baf0a71f17589d79d]
	- you can activate the request context for a specific method using the @ActivateRequestContext interceptor binding
	at io.quarkus.arc.impl.ClientProxies.getDelegate(ClientProxies.java:55)
	at org.acme.service.LogService_ClientProxy.arc$delegate(Unknown Source)
	at org.acme.service.LogService_ClientProxy.log(Unknown Source)
	at org.acme.interceptor.LogInterceptor.aroundReadFrom(LogInterceptor.java:23)
	at org.jboss.resteasy.reactive.server.jaxrs.ReaderInterceptorContextImpl.proceed(ReaderInterceptorContextImpl.java:54)
	at org.jboss.resteasy.reactive.server.handlers.RequestDeserializeHandler.handle(RequestDeserializeHandler.java:88)
	at io.quarkus.resteasy.reactive.server.runtime.QuarkusResteasyReactiveRequestContext.invokeHandler(QuarkusResteasyReactiveRequestContext.java:108)
	at org.jboss.resteasy.reactive.common.core.AbstractResteasyReactiveContext.run(AbstractResteasyReactiveContext.java:145)
	at io.quarkus.vertx.core.runtime.VertxCoreRecorder$14.runWith(VertxCoreRecorder.java:576)
	at org.jboss.threads.EnhancedQueueExecutor$Task.run(EnhancedQueueExecutor.java:2449)
	at org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run(EnhancedQueueExecutor.java:1478)
	at org.jboss.threads.DelegatingRunnable.run(DelegatingRunnable.java:29)
	at org.jboss.threads.ThreadLocalResettingRunnable.run(ThreadLocalResettingRunnable.java:29)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.base/java.lang.Thread.run(Thread.java:833)
```

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
$ curl -i -X POST http://localhost:8080/test -H "Content-Type: multipart/form-data" -F file=@tests/9kb -F description=foo
```

Output:
```
HTTP/1.1 200 OK
content-length: 4
Content-Type: application/json;charset=UTF-8

true
```

3. Call the API with a body of more than 10000 bytes. Now it fails.

```sh
$ curl -i -X POST http://localhost:8080/test -H "Content-Type: multipart/form-data" -F file=@tests/10kb -F description=foo
```

Output:
```
HTTP/1.1 200 OK
content-length: 4
Content-Type: application/json;charset=UTF-8

true
```

There's no log on the server side, the `ReaderInterceptor` is not called.

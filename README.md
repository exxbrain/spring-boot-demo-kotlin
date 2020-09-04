## Getting started

- Edit datasource config in
```src/main/resources/application.properties```
- Run
```
./gradlew
```

### Add changelog

- Modify your JPA entity (add a field, a relationship, etc.)
- Compile your application (this works on the compiled Java code, so
  don’t forget to compile!)
- Run
```
./gradlew diffChangelog -PrunList=diffLog
```
(or
```
./gradlew assemble diffChangelog -PrunList=diffLog
```
to compile before)
- A new “change log” is created in your
  ```src/main/resources/db/changelog``` directory
- Review this change log and add it to your
  ```src/main/resources/db/changelog/db.changelog-master.yaml``` file,
  so it is applied the next time you run your application
  
## Production

### Run
```
./gradlew -Pprod
```

## Other cases

### Run jar
```
./gradlew build && java -jar build/libs/spring-boot-demo-0.0.1-SNAPSHOT.jar
```
### Docker
Build docker image
```
./gradlew build && docker build -t exxbrain/spring-boot-demo .
```

http://qr.nspk.ru

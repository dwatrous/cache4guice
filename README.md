cache4guice
===========

Aspect Oriented Programming (AOP) utility to manage caching along side Google Guice.

This originally came from this project: https://code.google.com/p/cache4guice/

The current version has been updated to use maven and injection is more standard.

# Example Usage

In your module, you need to include the desired Cache module

```java
injector = Guice.createInjector(new ServletModule(), new ApplicationModule(), new EhCacheModule());
```

After that, you can annotate any function to be cached

```java
    @Cached
    public String getFullAddress(Integer id, String language) {
        // do something to find and calculate the full address, like a database query
        String fullAddress = rs.get("address") + rs.get("zip");
        return fullAddress;
    }
```

That's it. Now the EhCache module will intercept calls to getFullAddress based on a key calculated from the input parameters to the function. If a match is found, the function won't even be executed. Otherwise, it will execute the function and cache the result before returning.

Note that the input parameters should always produce an identical result for the same input values for caching to be valid.

It's also possible to set the time to live for a function in the annotation. For example, this next example will keep an item in the cache for an hour.

```java
    @Cached(timeToLiveSeconds = 3600)
    public String getFullAddress(Integer id, String language) {
        // do something to find and calculate the full address, like a database query
        String fullAddress = rs.get("address") + rs.get("zip");
        return fullAddress;
    }
```

# Maven

This project is not in the central maven repository, but it does maintain a branch here on github that will allow you to include it in your pom.

To do this, add the repository like this:

```xml
    <repository>
        <id>com.github.cache4guice</id>
        <url>https://raw.github.com/dwatrous/cache4guice/mvn-repo</url>
        <!-- use snapshot version -->
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
```

And the dependency, like this.

```xml
    <dependency>
        <groupId>com.github</groupId>
        <artifactId>cache4guice</artifactId>
        <version>0.1</version>
    </dependency>
```

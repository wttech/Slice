# About Slice

## Purpose

Slice is a framework which simplifies Sling/Adobe AEM development by using dependency injection pattern (DI). It glues Sling and Google Guice together, allowing developers to create a code with a clean separation of concerns. You can map resources to Java models seamlessly and thanks to DI arrange your application in easily testable and maintainable code.

**What can you gain?**
 * Lean and neat code, slicker design!
 * Improved testability of your code - thanks to dependency injection it can be easily unit-tested.
 * Improved maintenance of your code - all business logic arranged in clean and simple Java classes (POJOs)
 * Easy to start with! Slice is easy to learn and if you use it in all your projects, your developers know exactly how to start.
 * Faster development – code reuse, dependency injection and simplicity make you more efficient than ever.
 * Overall costs reduced!

## Features
### Separation of concerns
No more business logic in your view (JSP, Sightly scripts) - susiness logic's place is in Java classes ans Slice knows it!

**Slice loves Sightly**. Sightly loves Slice. Both are perfect match. Seamless integration you will love:
```html
<div data-sly-use.model="com.example.components.text.TextModel">
    <p>${model.text}<p>
</div>
```

**JSPs made clean and tidy** - no more this ugly scriptlets:
```jsp
<slice:lookup var="model" type="<%=com.example.components.text.TextModel%>" />
<p>${model.text}</p>
```

**Reusable Java models** which expose data for your view - note same model can be used for Sightly and JSP components - one model to feed them all!
```java
@SliceResource
public class TextModel {
  
    @JcrProperty
    private String text;
  
    public String getText() {
        return text;
    }
}
```

### Mapping resources into Java objects

Slice allows you to map a resource into plain Java object. It's annotation-driven, very easy to use and fully extensible, so you can write your own ways of mapping if a set of available features is not enough for your needs. Slice supports mapping of :
 * simple properties (String, Long, Boolean, etc) into primitives and objects
 * simple properties into enums.
 * multi-value properties into arrays or lists
 * child resources into a Java object or list of objects
 
The following code snippet demonstrates all above features in one model. It's simple - just annotate a class with @SliceResource and its fields with @JcrProperty to get auto mapping of resource properties to class fields:

```java
@SliceResource
public class ComplexTextModel {

	@JcrProperty
	private String text;
	
	@JcrProperty
	private String[] styles;

	@JcrProperty
	private AlignmentEnum alignment;

	@Children(LinkModel.class)
	@JcrProperty
	private List<LinkModel> links;
	
	@JcrProperty
	private ImageModel image;

	//do whatever business logic you want in your model
}

public enum AlignmentEnum {
	LEFT, RIGHT, CENTER;
}

@SliceResource(MappingStrategy.ALL)
public class LinkModel {
	private String path;
	private String label;
}

@SliceResource(MappingStrategy.ALL)
public class ImageModel {
	private String imagePath;
	private String altText;
}
```




### Dependency Injection with Google Guice. Why it's awesome? Take a look here: [Google I/O 2009 - Big Modular Java with Guice](https://www.youtube.com/watch?v=hBVJbzAagfs), and here to check [motivation of Google Guice creators](https://code.google.com/p/google-guice/wiki/)
```java
code snippets here
```




Since Slice 3.1 the AEM/CQ related modules have been extracted to separate projects:
* Slice AEM v6.0 Addon: https://github.com/Cognifide/Slice-AEM60
* Slice CQ v5.6 Addon: https://github.com/Cognifide/Slice-CQ56/
* Slice CQ v5.5 Addon: https://github.com/Cognifide/Slice-CQ55/


## Prerequisites

* AEM / Apache Sling 2
* Maven 2.x, 3.x

## Installation

Slice is available from Maven Central Repo. However if you want to check out the newest development version, do the following:

Checkout the source code:

    cd [folder of your choice]
    git clone git://github.com/Cognifide/Slice.git
    cd Slice

Compile and install:

    mvn install

## Usage

Add dependencies to your POM file:

```xml
(...)
<dependency>
	<groupId>com.cognifide.slice</groupId>
	<artifactId>slice-core-api</artifactId>
	<version>4.0.0</version>
</dependency>
<dependency>
	<groupId>com.cognifide.slice</groupId>
	<artifactId>slice-core</artifactId>
	<version>4.0.0</version>
</dependency>
<dependency>
	<groupId>com.cognifide.slice</groupId>
	<artifactId>slice-mapper</artifactId>
	<version>4.0.0</version>
</dependency>
<dependency>
	<groupId>com.cognifide.slice</groupId>
	<artifactId>slice-mapper-api</artifactId>
	<version>4.0.0</version>
</dependency>
(...)
```

Prepare Injector of your application in BundleActivator. Example activator:

```java
package com.example.app;

import java.util.ArrayList;
import java.util.List;
 
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
 
import com.cognifide.slice.api.injector.InjectorRunner;
import com.cognifide.slice.commons.SliceModulesFactory;
import com.cognifide.slice.cq.module.CQModulesFactory;
 
import com.google.inject.Module;
 
public class Activator implements BundleActivator {

private static final String BUNDLE_NAME_FILTER = "app.*";

private static final String BASE_PACKAGE = "com.example.app";

@Override
public void start(final BundleContext bundleContext) {

	final InjectorRunner injectorRunner = new InjectorRunner(bundleContext, INJECTOR_NAME, BUNDLE_NAME_FILTER, BASE_PACKAGE);

	final List<Module> sliceModules = SliceModulesFactory.createModules(bundleContext);
	final List<Module> cqModules = CQModulesFactory.createModules(); //if you use AEM/CQ add-on
	final List<Module> customModules = createCustomModules();

	injectorRunner.installModules(sliceModules);
	injectorRunner.installModules(cqModules);
	injectorRunner.installModules(customModules);

	injectorRunner.start();
}

private List<Module> createCustomModules() {
	List<Module> applicationModules = new ArrayList<Module>();
	//populate the list with your modules
	return applicationModules;
}
}
```

[Read more about setting up your application](https://cognifide.atlassian.net/wiki/display/SLICE/Setting+up+-+4.0)

# Commercial Support

Technical support can be made available if needed. Please [contact us](https://www.cognifide.com/get-in-touch/) for more details.

We can:

* prioritize your feature request,
* tailor the product to your needs,
* provide a training for your engineers,
* support your development teams.

# More documentation
------------------
* [Full introduction to Slice](https://cognifide.atlassian.net/wiki/display/SLICE/About+Slice)
* [Slice Wiki](https://cognifide.atlassian.net/wiki/display/SLICE)
* [Slice Issue tracking](https://cognifide.atlassian.net/browse/SLICE)

* [Cognifide.com](http://cognifide.com)

* [Maven](http://maven.apache.org)
* [CRX](http://www.day.com/day/en/products/crx.html)
* [CRX API](http://dev.day.com/content/docs/en/crx/current/how_to/package_manager.html#Package%20Manager%20HTTP%20Service%20API)

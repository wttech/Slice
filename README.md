# About Slice

## Purpose

Slice is a framework that glues Sling and Google Guice together. It eases mapping between
resources to classes and allows to use dependency injection inside Sling and CQ applications.

In 3.1.0 version of Slice the CQ related modules have been extracted to separate projects:
* Slice CQ v5.5 Addon: https://github.com/Cognifide/Slice-CQ55/
* Slice CQ v5.6 Addon: https://github.com/Cognifide/Slice-CQ56/
* Slice AEM v6.0 Addon: https://github.com/Cognifide/Slice-AEM60

## Features
##### Sightly loves Slice. Slice loves Sightly. Both are perfect match. Seamless integration you will love.
```html
<div data-sly-use.model="com.cognifide.example.core.components.richtext.TextModel">
 <p>${model.text}<p>
</div>
```
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
##### Doing JSPs? No more business logic in JSPs (view layer). Business logic's place is in Java ans Slice knows about it!
```jsp
<slice:lookup var="model" type="<%=com.cognifide.example.core.components.richtext.TextModel%>" />
<p>${model.text}</p>

// Note that this sample uses exactly same model. One model to feed them all!
```

##### Dependency Injection with Google Guice. Why it's awesome? Take a look here: [Google I/O 2009 - Big Modular Java with Guice](https://www.youtube.com/watch?v=hBVJbzAagfs), and here to check [motivation of Google Guice creators](https://code.google.com/p/google-guice/wiki/)
```java
code snippets here
```
##### Lean and neat code, slicker design!
##### Improved testability of your code! Thanks to dependency injection (DI) they can be easily unit-tested.
##### Easy to start with! Slice is easy to learn and if you use it in all your projects, your developers know exactly how to start.
##### Faster development – code reuse, dependency injection and simplicity make you more efficient than ever.
##### Overall costs reduced!


## Prerequisites

* CQ / Apache Sling 2
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

    (...)
    <dependency>
        <groupId>com.cognifide.slice</groupId>
        <artifactId>slice-core-api</artifactId>
        <version>3.1.0</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>com.cognifide.slice</groupId>
        <artifactId>slice-core</artifactId>
        <version>3.1.0</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>com.cognifide.slice</groupId>
        <artifactId>slice-mapper</artifactId>
        <version>3.1.0</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>com.cognifide.slice</groupId>
        <artifactId>slice-mapper-api</artifactId>
        <version>3.1.0</version>
        <scope>compile</scope>
    </dependency>
    (...)

Prepare Injector of your application in BundleActivator. Example activator:

	import java.util.ArrayList;
	import java.util.List;
	
	import org.osgi.framework.BundleActivator;
	import org.osgi.framework.BundleContext;
	
	import com.cognifide.slice.api.context.ContextScope;
	import com.cognifide.slice.api.injector.InjectorRunner;
	import com.cognifide.slice.commons.SliceModulesFactory;
	import com.cognifide.slice.core.internal.context.SliceContextScope;
	import com.cognifide.slice.cq.module.CQModulesFactory;
	import com.cognifide.slice.validation.ValidationModulesFactory;
	import com.google.inject.Module;

	public class Activator implements BundleActivator {
	
		private static final String BUNDLE_NAME_FILTER = "com\\.company\\.application\\.webapp\\..*";
	
		private static final String BASE_PACKAGE = "com.company.application";
	
		private static final String INJECTOR_NAME = "injectorName";
	
		@Override
		public void start(BundleContext bundleContext) throws Exception {
			final ContextScope scope = new SliceContextScope();
			final InjectorRunner injectorRunner = new InjectorRunner(bundleContext, INJECTOR_NAME, scope);
			
			List<Module> sliceModules = SliceModulesFactory.createModules(bundleContext, INJECTOR_NAME,
					BUNDLE_NAME_FILTER, BASE_PACKAGE);
			List<Module> cqModules = CQModulesFactory.createModules();
			List<Module> validationModules = ValidationModulesFactory.createModules();
			List<Module> customModules = createCustomModules();
			
			injectorRunner.installModules(sliceModules);
			injectorRunner.installModules(cqModules);
			injectorRunner.installModules(validationModules);
			injectorRunner.installModules(customModules);
			
			injectorRunner.start();
		}
	
		private List<Module> createCustomModules() {
			List<Module> applicationModules = new ArrayList<Module>();
			//populate list with your modules
			return applicationModules;
		}
	}

SliceModulesFactory requires some String parameters. It will look for classes matching BUNDLE_NAME_FILTER under package BASE_PACKAGE and
annotated with @SliceResource. It will then bind them to provider that automatically maps resource properties to fields.
You can install many Slice Resource modules on one Injector.

## Mapping Slice Resources

Annotate any class with @SliceResource annotation to get auto mapping of resource properties to class fields:

    @SliceResource
    public class RichTextModel {

        @JcrProperty
        @Unescaped
        private String text;

		@Inject
        public RichTextModel() {
        }

        public String getText() {
            return text;
        }

	}

Map current resource in .jsp file to an instance of the given class:

    <slice:lookup appName="injectorName" var="model" type="<%=RichTextModel.class%>"/>

Use properties from model variable:

    <div class="richText">
        ${model.text}
    </div>

# Commercial Support

Technical support can be made available if needed. Please [contact us](https://www.cognifide.com/get-in-touch/) for more details.

We can:

* prioritize your feature request,
* tailor the product to your needs,
* provide a training for your engineers,
* support your development teams.

More documentation
------------------
* [Full introduction to Slice](https://cognifide.atlassian.net/wiki/display/SLICE/About+Slice)
* [Slice Wiki](https://cognifide.atlassian.net/wiki/display/SLICE)
* [Slice Issue tracking](https://cognifide.atlassian.net/browse/SLICE)

* [Cognifide.com](http://cognifide.com)

* [Maven](http://maven.apache.org)
* [CRX](http://www.day.com/day/en/products/crx.html)
* [CRX API](http://dev.day.com/content/docs/en/crx/current/how_to/package_manager.html#Package%20Manager%20HTTP%20Service%20API)

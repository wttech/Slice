![Cognifide logo](http://cognifide.github.io/images/cognifide-logo.png)

[![Build Status](https://travis-ci.org/Cognifide/Slice.svg?branch=master)](https://travis-ci.org/Cognifide/Slice)
[![Coverity Status](https://scan.coverity.com/projects/4863/badge.svg)](https://scan.coverity.com/projects/4863)
[![Coverage Status](https://coveralls.io/repos/Cognifide/Slice/badge.svg)](https://coveralls.io/r/Cognifide/Slice)
[![Latest release](https://maven-badges.herokuapp.com/maven-central/com.cognifide.slice/slice-assembly/badge.svg)](http://mvnrepository.com/artifact/com.cognifide.slice/slice-assembly)
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
No more business logic in your view (JSP, Sightly scripts) - business logic's place is in Java classes and Slice knows it!

**Slice loves Sightly**. Sightly loves Slice. Both are perfect match. Seamless integration you will love:
```html
<div data-sly-use.model="com.example.components.text.TextModel">
    <p>${model.text}<p>
</div>
```

**JSPs made clean and tidy** - no more these ugly scriptlets.
```jsp
<slice:lookup var="model" type="<%= com.example.components.text.TextModel.class %>" />
<p>${model.text}</p>
```
or it can be 
```jsp
<slice:lookup var="model" type="com.example.components.text.TextModel" />
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

Interested in details? Read about [Slice concepts](https://cognifide.atlassian.net/wiki/display/SLICE/Slice+concepts+-+4.3) and [how it works internally](https://cognifide.atlassian.net/wiki/pages/viewpage.action?pageId=18579473) on our Wiki.

### Mapping resources into Java objects

Slice allows you to map a resource into plain Java object. It's annotation-driven, very easy to use and fully extensible, so you can write your own ways of mapping if a set of available features is not enough for your needs. Slice supports mapping of:
 * simple properties (String, Long, Boolean, etc) into primitives and objects
 * simple properties into enums.
 * multi-value properties into arrays or lists
 * child resources into a Java object or list of objects
 * nested resources/classes hierarchy
 
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

	//... do whatever business logic you want in your model
}

public enum AlignmentEnum {
	LEFT, RIGHT, CENTER;
}

@SliceResource(MappingStrategy.ALL)
public class LinkModel {
	private String path;
	private String label;
	//...
}

@SliceResource(MappingStrategy.ALL)
public class ImageModel {
	private String imagePath;
	private String altText;
	//...
}
```

Read more about mapping on our [Wiki](https://cognifide.atlassian.net/wiki/display/SLICE/Mapper+-+4.3).


### Dependency Injection with Google Guice

If your AEM components are more than simple text/image/title components (and they certainly are), then you probably need to combine their functionality with some external data or more complex business logic provided by other classes. Dependency injection allows you to do this easily and keep your code testable without boiler-plate code and unneeded arguments in methods used only to propagate a value down into the class hierarchy.

We took Guice as a DI container. Why it's awesome? Take a look here: [Google I/O 2009 - Big Modular Java with Guice](https://www.youtube.com/watch?v=hBVJbzAagfs), and here to check [motivation of Google Guice creators](https://code.google.com/p/google-guice/wiki/)

To demonstrate an example of a complex component which combines use of Slice features with power of DI, take a look at the following code which is an implementation of a Twitter component.

```html
<div data-sly-use.model="com.example.components.twitter.TwitterModel">
	<ul data-sly-list="${model.tweets}">
		<li>${item}</li>
	</ul>
</div>
```

```java
@SliceResource
public class TwitterModel {

	@JcrProperty
	private int limit;

	private final TwitterHandler twitterHandler;
	
	@Inject
	public TwitterModel(TwitterHandler twitterHandler) {
		this.twitterHandler = twitterHandler;
	}

	//returns list of tweets limited by number configurable by authors
	public List<String> getTweets() {
		List<String> tweets = twitterHandler.readTweets();
		return tweets.subList(0, Math.max(tweets.size(), limit));
	}
	
	//...
}
```

The model of the component is fairly simple and fully testable because you can easily mock the TwitterHandler in your unit test case.

TwitterHandler is also very simple as it uses Twitter client (from Twitter4j library). Please note that this client is injected by Guice and you don't have to care about its configuration in the handler itself.

```java
public class TwitterHandler {

	@Inject
	private Twitter twitterClient; //Twitter4j client

	public List<String> readTweets() {
		List<String> tweets = new ArrayList<String>();
		List<Status> statuses = twitterClient.getHomeTimeline();
		for (Status status : statuses) {
			tweets.add(status.getText());
		}
		return tweets;
	}
}
```

The configuration is set while instantiating the twitter client by Guice. To instruct Guice how to create the client object we need to create a so called [provider](https://code.google.com/p/google-guice/wiki/ProvidesMethods). You can do this in module configuration. It reads some configuration properties from repository (using ModelProvider). ContextScope instructs Guice to create such an object only once per request or OSGi service call - yes, you can reuse the TwitterHandler in you OSGi services which are request/resource agnostic - that's the power of Slice!.

```java
public class MyModule extends AbstractModule {

	private static final String TWITTER_CONFIG_PATH = "/etc/twitter/configuration/jcr:content/twitterConfig";

	@Provides
	@ContextScoped
	public Twitter getTwitter(ModelProvider modelProvider) {
		TwitterConfiguration config = modelProvider.get(TwitterConfiguration.class,
				TWITTER_CONFIG_PATH);
		ConfigurationBuilder builder = new ConfigurationBuilder(); // from Twitter4j
		builder.setOAuthConsumerKey(config.getOAuthKey())
				.setOAuthConsumerSecret(config.getOAuthSecret());
		TwitterFactory factory = new TwitterFactory(builder.build());
		return factory.getInstance();
	}

	//...

}
```

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
	<version>5.0.0</version>
</dependency>
<dependency>
	<groupId>com.cognifide.slice</groupId>
	<artifactId>slice-core</artifactId>
	<version>4.3.0</version>
</dependency>
<dependency>
	<groupId>com.cognifide.slice</groupId>
	<artifactId>slice-mapper</artifactId>
	<version>4.3.0</version>
</dependency>
<dependency>
	<groupId>com.cognifide.slice</groupId>
	<artifactId>slice-mapper-api</artifactId>
	<version>4.3.0</version>
</dependency>
(...)
```

Last thing you need to do is preparing Injector of your application in BundleActivator. Read more on how to do this on our [Wiki](https://cognifide.atlassian.net/wiki/display/SLICE/Setting+up+-+4.3)

Since Slice 3.1 the AEM/CQ related modules have been extracted to separate projects:
* Slice AEM v6.0 and 6.1 Addon: https://github.com/Cognifide/Slice-AEM60
* Slice CQ v5.6 Addon: https://github.com/Cognifide/Slice-CQ56/
* Slice CQ v5.5 Addon: https://github.com/Cognifide/Slice-CQ55/


# Commercial Support

Technical support can be made available if needed. Please [contact us](mailto:labs-support@cognifide.com) for more details.

We can:

* prioritize your feature request,
* tailor the product to your needs,
* provide a training for your engineers,
* support your development teams.


# More documentation
------------------
* [Full documentation of Slice 4.3](https://cognifide.atlassian.net/wiki/display/SLICE/About+Slice+-+4.3)
* [Slice 4.3 APIdocs](http://cognifide.github.io/Slice/apidocs/4-3-0/)
* [Slice Wiki](https://cognifide.atlassian.net/wiki/display/SLICE)
* [Slice users mailing group](http://slice-users.2340343.n4.nabble.com/) if you have any question on how to use it
* [Slice issue tracking](https://cognifide.atlassian.net/browse/SLICE)

![Cognifide logo](http://cognifide.github.io/images/cognifide-logo.png)

[![Build Status](https://travis-ci.org/Cognifide/Slice.svg?branch=master)](https://travis-ci.org/Cognifide/Slice)
[![Coverity Status](https://scan.coverity.com/projects/4863/badge.svg)](https://scan.coverity.com/projects/4863)
[![Coverage Status](https://coveralls.io/repos/Cognifide/Slice/badge.svg)](https://coveralls.io/r/Cognifide/Slice)
[![Latest release](https://maven-badges.herokuapp.com/maven-central/com.cognifide.slice/slice-assembly/badge.svg)](http://mvnrepository.com/artifact/com.cognifide.slice/slice-assembly)

<img alt="Slice logo, a simple, white outline of a slice of pizza against a sleek, flat, colourful background" title="Slice logo" width="200px" src="data:image/png;base64,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"/>

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
No more business logic in your view (JSP, [HTL](https://docs.adobe.com/docs/en/htl/overview.html) scripts) - business logic's place is in Java classes and Slice knows it!

**Slice loves HTL**. HTL loves Slice. They go together like strawberries and cream! Seamless integration you will love:
```html
<div data-sly-use.model="com.example.components.text.TextModel">
    <p>${model.text}<p>
</div>
```

**JSPs made clean and tidy** - no more ugly scriptlets.
```jsp
<slice:lookup var="model" type="<%=com.example.components.text.TextModel%>" />
<p>${model.text}</p>
```

**Reusable Java models** which expose data for your view - note that the same model can be used by HTL and JSP components - one model to feed them all!
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

### Mapping resources to Java objects

Slice allows you to map a resource to a plain Java object. It's annotation-driven, very easy to use and fully extensible, so you can write your own ways of mapping if a set of available features is not enough for your needs. Slice supports mapping of:
 * simple properties (`String`, `Long`, `Boolean`, etc.) into primitives and objects
 * simple properties into enums.
 * multi-value properties to arrays or lists
 * child resources to a Java object or list of objects
 * nested resources/classes hierarchy
 
The following code snippet demonstrates all of the above features in one model. It's simple - just annotate a class with `@SliceResource` and its fields with `@JcrProperty` to get auto mapping of resource properties to class fields:

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

Slice is available from the Maven Central Repo. However if you want to check out the newest development version, do the following:

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
	<version>4.3.0</version>
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

The last thing you need to do is to prepare an `Injector` of your application in its `BundleActivator`. Read more on how to do this on our [Wiki](https://cognifide.atlassian.net/wiki/display/SLICE/Setting+up+-+4.3)

Since Slice 3.1 the AEM/CQ related modules have been extracted to separate projects:
* Slice AEM v6.0, 6.1 and 6.2 Addon: https://github.com/Cognifide/Slice-AEM60
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

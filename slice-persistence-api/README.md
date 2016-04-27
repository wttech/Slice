![Cognifide logo](http://cognifide.github.io/Carty/assets/media/cognifide_logo.png)

# Slice persistence

## Purpose

Slice persistence is an OSGi module that allows to save Slice models back into Sling repository.

## Features

* Saves simple field types as JCR properties (`String`, `int`, `Calendar`, etc.)
* Supports nested models - will be persisted as subresources
* Allows for customizing the property name with `@JcrProperty`
* Supports collections and arrays of properties
* Supports collections and arrays of nested models with Slice `@Children`

## Prerequisites

* AEM 6

## Installation

Add dependency to your project:

    <dependency>
        <groupId>com.cognifide.slice</groupId>
        <artifactId>slice-persistence-api</artifactId>
        <version><!-- current slice version --></version>
        <scope>provided</scope>
    </dependency>

## Usage

Consider following model:

    @SliceResource
    public class SampleModel {
    
        @JcrProperty
        private int pageLimit;
    
        @JcrProperty
        private PaginationType type; // PaginationType is an enum
    
        @JcrProperty
        private String[] tags;
    
        @JcrProperty
        @Children(SubModel.class)
        private List<SubModel> subModels;
    
        private String notAJcrProperty; // won't be serialized
    
        //...
    }

Model can be persisted as follows:

    @Inject
    private ModelPersister modelPersister;
    
    //...
    
    SampleModel model = resource.adaptTo(SampleModel.class); // inject SampleModel with Slice
    model.setPageLimit(123);
    modelPersister.persist(model, resource);
    resource.getResourceResolver().commit();

# Commercial Support

Technical support can be made available if needed. Please [contact us](http://www.cognifide.com/contact/) for more details.

We can:

* prioritize your feature request,
* tailor the product to your needs,
* provide a training for your engineers,
* support your development teams.
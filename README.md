# About Slice

## Purpose

Slice is a framework that glues Sling and Google Guice together. It eases mapping between
resources to classed and to using dependency injection inside Sling and CQ applications.

## Prerequisites

* CQ / Apache Sling 2
* Maven 2.x, 3.x

## Installation

Checkout the source code:

    cd [folder of your choice]
    git clone git://github.com/Cognifide/Slice.git
    cd Slice

Compile and install:

    mvn clean package install

## Usage

Add dependencied to your POM file:

    (...)
    <dependency>
        <groupId>com.cognifide.slice</groupId>
        <artifactId>slice-core-api</artifactId>
        <version>2.0.0</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>com.cognifide.slice</groupId>
        <artifactId>slice-core</artifactId>
        <version>2.0.0</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>com.cognifide.slice</groupId>
        <artifactId>slice-mapper</artifactId>
        <version>2.0.0</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>com.cognifide.slice</groupId>
        <artifactId>slice-mapper-api</artifactId>
        <version>2.0.0</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>com.cognifide.slice</groupId>
        <artifactId>slice-cq</artifactId>
        <version>2.0.0</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>com.cognifide.slice</groupId>
        <artifactId>slice-cq-taglib</artifactId>
        <version>2.0.0</version>
        <scope>compile</scope>
    </dependency>
    (...)

Prepare Injector service in Activator:

    private static final String INJECTOR_NAME = "injectorName";

    final ServiceReference injectorServiceRunnerFactoryService = bundleContext
        .getServiceReference(InjectorServiceRunnerFactory.class.getName());
    final InjectorServiceRunnerFactory injectorServiceRunnerFactory = (InjectorServiceRunnerFactory) bundleContext
        .getService(injectorServiceRunnerFactoryService);
    final InjectorServiceRunner injectorServiceRunner = injectorServiceRunnerFactory
        .getInjectorServiceRunner(bundleContext, INJECTOR_NAME);

Install CQ modules:

    final ServiceReference cqModulesInstallerService = bundleContext
        .getServiceReference(CQModulesInstaller.class.getName());
    final CQModulesInstaller cqModulesFactory = (CQModulesInstaller) bundleContext
        .getService(cqModulesInstallerService);
    cqModulesFactory.installCQModules(injectorServiceRunner);

If you want to use Slice with Sling only, install Sling modules instead of CQ one:

    final ServiceReference sliceModulesInstallerService = bundleContext
        .getServiceReference(SliceModulesInstaller.class.getName());
    final SliceModulesInstaller sliceModulesInstaller = (SliceModulesInstaller) bundleContext
        .getService(sliceModulesInstallerService);
    sliceModulesInstaller.installSliceModules(injectorServiceRunner);

Install Slice Resource module:

    private static final String BUNDLE_NAME_FILTER = "com\\.company\\.application\\.webapp\\..*";
    private static final String BASE_PACKAGE = "com.company.application";

    final ServiceReference sliceModulesInstallerService = bundleContext
        .getServiceReference(SliceModulesInstaller.class.getName());
    final SliceModulesInstaller sliceModulesInstaller = (SliceModulesInstaller) bundleContext
        .getService(sliceModulesInstallerService);
    sliceModulesInstaller.installSliceResourceModule(injectorServiceRunner, BUNDLE_NAME_FILTER, BASE_PACKAGE);

It will look for any classes in filters matching BUNDLE_NAME_FILTER under package BASE_PACKAGE for classes
annotated with @SliceResource and bind them to provider that automatically maps resource properties to fields.
You can install many Slice Resource modules on one Injector.

Finally, start the Injector service:

    injectorServiceRunner.start();

## Mapping Slice Resources

Map any class with @SliceResource module to get auto mapping of resource properties to class fields:

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

Map current resource in .jsp file to this class:

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
* [Slice Wiki](https://github.com/Cognifide/Slice/wiki)
* [Cognifide.com](http://cognifide.com)
* [Maven](http://maven.apache.org)
* [CRX](http://www.day.com/day/en/products/crx.html)
* [CRX API](http://dev.day.com/content/docs/en/crx/current/how_to/package_manager.html#Package%20Manager%20HTTP%20Service%20API)
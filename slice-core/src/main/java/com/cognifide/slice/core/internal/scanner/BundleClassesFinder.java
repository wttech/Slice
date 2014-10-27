package com.cognifide.slice.core.internal.scanner;

/*-
 * #%L
 * Slice - Core
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2012 Cognifide Limited
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import com.cognifide.slice.annotations.OsgiService;
import org.objectweb.asm.ClassReader;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BundleClassesFinder {

    private static final Logger LOG = LoggerFactory.getLogger(BundleClassesFinder.class);

    private static final String RESOURCE_PATTERN = "*.class";

    private final Collection<Bundle> bundles;

    private final String basePackage;

    private List<ClassFilter> filters = new ArrayList<BundleClassesFinder.ClassFilter>();

    public BundleClassesFinder(Collection<Bundle> bundles, String basePackage) {
        this.bundles = bundles;
        this.basePackage = basePackage.replace('.', '/');
    }

    public Collection<Class<?>> getClasses() {

        Collection<Class<?>> classes = new ArrayList<Class<?>>();

        for (Bundle bundle : this.bundles) {

            if (LOG.isInfoEnabled()) {
                LOG.info("Searching for classes annotated with SliceResource in '"
                        + bundle.getSymbolicName() + "' bundle, package: " + this.basePackage);
            }

            @SuppressWarnings("unchecked")
            Enumeration<URL> classEntries = bundle.findEntries(this.basePackage, RESOURCE_PATTERN, true);

            while ((classEntries != null) && classEntries.hasMoreElements()) {
                try {
                    URL classURL = classEntries.nextElement();
                    ClassReader classReader = new ClassReader(classURL.openStream());

                    if (accepts(classReader)) {
                        String className = classReader.getClassName().replace('/', '.');

                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Slice Resource class: " + className + " has been found.");
                        }

                        Class<?> clazz = bundle.loadClass(className);
                        classes.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    LOG.error("Error loading class!", e);
                } catch (IOException e) {
                    LOG.error("Error reading the class!", e);
                }
            }
        }

        if (LOG.isInfoEnabled()) {
            LOG.info("Found: " + classes.size()
                    + " Slice Resource classes. Switch to debug logging level to see them all.");
        }

        return classes;
    }

    private boolean accepts(ClassReader classReader) {
        for (ClassFilter classFilter : this.filters) {
            if (classFilter.accepts(classReader)) {
                return true;
            }
        }
        return false;
    }

    public void addFilter(ClassFilter classFilter) {
        this.filters.add(classFilter);
    }

    public interface ClassFilter {
        boolean accepts(ClassReader classReader);
    }

    public List<Class<?>> traverseBundlesForOsgiServices() {
        List<Class<?>> osgiClasses = new ArrayList<Class<?>>();
        for (Bundle bundle : bundles) {
            BundleContext context = bundle.getBundleContext();
            ServiceReference ref = context.getServiceReference(PackageAdmin.class.getName());
            PackageAdmin packageAdmin = (PackageAdmin) context.getService(ref);
            ExportedPackage[] exportedPackages = packageAdmin.getExportedPackages(bundle);
            for (ExportedPackage ePackage : exportedPackages) {
                String packageName = ePackage.getName();
                String packagePath = "/" + packageName.replace('.', '/');
                //find all the class files in current exported package
                Enumeration clazzes = bundle.findEntries(packagePath, "*.class", false);
                while (clazzes.hasMoreElements()) {
                    URL url = (URL) clazzes.nextElement();
                    String className = getClassNameFromUrl(url);
                    String fullClassName = packageName + "." + className;
                    try {
                        Class clazz = bundle.loadClass(fullClassName);
                        //check whether any class field is annotated with OsgiService tag.
                        Field[] fields = clazz.getDeclaredFields();
                        for (Field field : fields) {
                            if (field.isAnnotationPresent(OsgiService.class)) {
                                Class fieldClass = field.getType();
                                if (!osgiClasses.contains(fieldClass)) {
                                    osgiClasses.add(fieldClass);
                                }
                                break;
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        LOG.error("Class not found in the bundle!",e);
                    }
                }
            }
        }
        return osgiClasses;
    }

    private String getClassNameFromUrl(URL url) {
        String path = url.getPath();
        int index = path.lastIndexOf("/");
        int endIndex = path.length() - 6;//Strip ".class" substring
        return path.substring(index + 1, endIndex);
    }
}

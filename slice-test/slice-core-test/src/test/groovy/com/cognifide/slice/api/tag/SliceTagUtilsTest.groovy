/*-
 * #%L
 * Slice - Core Tests
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

package com.cognifide.slice.api.tag

import com.cognifide.slice.test.setup.BaseSetup
import org.apache.sling.api.scripting.SlingBindings
import org.apache.sling.api.scripting.SlingScriptHelper
import org.apache.sling.commons.classloader.DynamicClassLoaderManager
import org.junit.Assert

import javax.servlet.ServletRequest
import javax.servlet.jsp.PageContext
import java.lang.reflect.Method

/**
 * @author Shashi Bhushan
 * Date: 23.08.16
 */
class SliceTagUtilsTest extends BaseSetup {

    def "Get Class object, given the String type"() {
        given:
        final PageContext pageContext = Mock(PageContext){
            getRequest()>>  Mock(ServletRequest){
                getAttribute(_ as String) >> Mock(SlingBindings) {
                    getSling() >> Mock(SlingScriptHelper) {
                        getService(_ as Class) >> Mock(DynamicClassLoaderManager) {
                            getDynamicClassLoader() >> Mock(ClassLoader) {
                                loadClass(_ as String) >> SliceTagUtils.class
                            }
                        }
                    }
                }
            }
        }

        when:
        Class aClass = SliceTagUtils.getClassFromType(pageContext, "com.cognifide.slice.api.tag.SliceTagUtils");

        then: "aClass object should not be null and should be equal to expected Class"
        Assert.assertNotNull(aClass)
        Assert.assertEquals(SliceTagUtils.class, aClass);
    }
}

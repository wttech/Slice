/*-
 * #%L
 * Slice - Mapper API
 * %%
 * Copyright (C) 2012 Wunderman Thompson Technology
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
package com.cognifide.slice.mapper.api.processor;

/**
 * Its purpose is to add a possibility to register custom post-processors with defined priority. Objects of
 * this class should be registered with Guice's multibindings.<br>
 * The priority parameter is used to sort processors. Priority post-processors with priority greater or equal
 * to 0 will take precedence over any other {@link FieldPostProcessor}s. Any {@link FieldPostProcessor}s will
 * take precedence over priority post-processors added with the priority lower than 0.
 * 
 * @author maciej.dybek
 */
public interface PriorityFieldPostProcessor extends FieldPostProcessor, PriorityProcessor {

}

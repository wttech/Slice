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
 * Interface intended to be implemented by {@link PriorityFieldProcessor} and
 * {@link PriorityFieldPostProcessor}. Priority processors can be registered with Guice's multibindings and
 * allows one to extend list of custom processors. The general rule for priority is: the higher the priority the
 * sooner given processor will be used. For details on how to use priority see {@link PriorityFieldProcessor}
 * and {@link PriorityFieldPostProcessor}.
 * 
 * @author maciej.dybek
 */
public interface PriorityProcessor {
	int getPriority();
}

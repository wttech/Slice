/*-
 * #%L
 * Slice - Mapper API
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
package com.cognifide.slice.mapper.api.processor;

/**
 * Its purpose is to add a possibility to register custom processors with defined priority. Objects of this
 * class should be registered with Guice's multibindings. <br>
 * The priority parameter is used to sort processors. Processors with higher priority will take precedence
 * over those with lower prioroty. Notice that all {@link PriorityFieldProcessor}s always take precedence over
 * any other {@link FieldProcessor}s.
 * 
 * @author maciej.dybek
 */
public interface PriorityFieldProcessor extends FieldProcessor, PriorityProcessor {

}

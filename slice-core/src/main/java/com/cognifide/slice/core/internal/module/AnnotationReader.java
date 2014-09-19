/*-
 * #%L
 * Slice - Core
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2012 - 2014 Cognifide Limited
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

package com.cognifide.slice.core.internal.module;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * It is used by {@link ClassReader} to visit all annotations of a class. Names of read annotations are stored
 * internally so that one can verify if a class is annotated by a given annotation or not.
 * 
 */
public class AnnotationReader implements AnnotationVisitor, ClassVisitor {

	private List<String> annotations = new ArrayList<String>();;

	@Override
	public void visit(int paramInt1, int paramInt2, String paramString1, String paramString2,
			String paramString3, String[] paramArrayOfString) {
		annotations.clear();
	}

	@Override
	public AnnotationVisitor visitAnnotation(String paramString, boolean paramBoolean) {
		String annotationClassName = getAnnotationClassName(paramString);
		annotations.add(annotationClassName);
		return this;
	}

	/**
	 * Verifies if a class contains a specified annotation or not
	 * 
	 * @param annotation
	 * @return <code>true</code> if a class is annotated by specified annotation, <code>false</code> otherwise
	 */
	public boolean isAnnotationPresent(Class<? extends Annotation> annotation) {
		boolean result;
		if (annotations != null) {
			String name = annotation.getName();
			result = annotations.contains(name);
		} else {
			result = false;
		}
		return result;
	}

	private String getAnnotationClassName(String rawName) {
		return rawName.substring(1, rawName.length() - 1).replace('/', '.');
	}

	// ---------------------UNUSED METHODS------------------------------------------------

	@Override
	public void visit(String paramString, Object paramObject) {
	}

	@Override
	public void visitEnum(String paramString1, String paramString2, String paramString3) {
	}

	@Override
	public AnnotationVisitor visitAnnotation(String paramString1, String paramString2) {
		return this;
	}

	@Override
	public AnnotationVisitor visitArray(String paramString) {
		return this;
	}

	@Override
	public void visitEnd() {
	}

	@Override
	public void visitAttribute(Attribute paramAttribute) {

	}

	@Override
	public FieldVisitor visitField(int paramInt, String paramString1, String paramString2,
			String paramString3, Object paramObject) {
		return null;
	}

	@Override
	public void visitInnerClass(String paramString1, String paramString2, String paramString3, int paramInt) {
	}

	@Override
	public MethodVisitor visitMethod(int paramInt, String paramString1, String paramString2,
			String paramString3, String[] paramArrayOfString) {
		return null;
	}

	@Override
	public void visitOuterClass(String paramString1, String paramString2, String paramString3) {
	}

	@Override
	public void visitSource(String paramString1, String paramString2) {
	}

}

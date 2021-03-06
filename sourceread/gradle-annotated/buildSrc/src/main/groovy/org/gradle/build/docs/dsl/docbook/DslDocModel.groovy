/*
 * Copyright 2010 the original author or authors.
 *
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
 */
package org.gradle.build.docs.dsl.docbook

import org.gradle.build.docs.XIncludeAwareXmlProvider
import org.gradle.build.docs.dsl.source.TypeNameResolver
import org.gradle.build.docs.dsl.docbook.model.ClassExtensionMetaData
import org.gradle.build.docs.dsl.source.model.ClassMetaData
import org.gradle.build.docs.model.ClassMetaDataRepository
import org.w3c.dom.Document
import org.gradle.build.docs.dsl.docbook.model.ClassDoc

class DslDocModel {
    private final File classDocbookDir
    private final Document document
    private final Map<String, ClassDoc> classes = [:]
    private final ClassMetaDataRepository<ClassMetaData> classMetaData
    private final Map<String, ClassExtensionMetaData> extensionMetaData
    private final JavadocConverter javadocConverter
    private final ClassDocBuilder docBuilder
    private final LinkedList<String> currentlyBuilding = new LinkedList<String>()

    DslDocModel(File classDocbookDir, Document document, ClassMetaDataRepository<ClassMetaData> classMetaData, Map<String, ClassExtensionMetaData> extensionMetaData) {
        this.classDocbookDir = classDocbookDir
        this.document = document
        this.classMetaData = classMetaData
        this.extensionMetaData = extensionMetaData
        javadocConverter = new JavadocConverter(document, new JavadocLinkConverter(document, new TypeNameResolver(classMetaData), new LinkRenderer(document, this), classMetaData))
        docBuilder = new ClassDocBuilder(this, javadocConverter)
    }

    Collection<ClassDoc> getClasses() {
        return classes.values().findAll { !it.name.contains('.internal.') }
    }

    boolean isKnownType(String className) {
        return classMetaData.find(className) != null
    }

    ClassDoc findClassDoc(String className) {
        ClassDoc classDoc = classes[className]
        if (classDoc == null && getFileForClass(className).isFile()) {
            return getClassDoc(className)
        }
        return classDoc
    }

    ClassDoc getClassDoc(String className) {
        ClassDoc classDoc = classes[className]
        if (classDoc == null) {
            classDoc = loadClassDoc(className)
            classes[className] = classDoc
            new ReferencedTypeBuilder(this).build(classDoc)
        }
        return classDoc
    }

    private ClassDoc loadClassDoc(String className) {
        if (currentlyBuilding.contains(className)) {
            throw new RuntimeException("Cycle building $className. Currently building $currentlyBuilding")
        }
        currentlyBuilding.addLast(className)
        try {
            ClassMetaData classMetaData = classMetaData.find(className)
            if (!classMetaData) {
                if (!className.contains('.internal.')) {
                    throw new RuntimeException("No meta-data found for class '$className'.")
                }
                classMetaData = new ClassMetaData(className)
            }
            try {
                ClassExtensionMetaData extensionMetaData = extensionMetaData[className]
                if (!extensionMetaData) {
                    extensionMetaData = new ClassExtensionMetaData(className)
                }
                File classFile = getFileForClass(className)
                if (!classFile.isFile()) {
                    throw new RuntimeException("Docbook source file not found for class '$className' in $classDocbookDir.")
                }
                XIncludeAwareXmlProvider provider = new XIncludeAwareXmlProvider()
                def doc = new ClassDoc(className, provider.parse(classFile), document, classMetaData, extensionMetaData)
                docBuilder.build(doc)
                return doc
            } catch (ClassDocGenerationException e) {
                throw e
            } catch (Exception e) {
                throw new ClassDocGenerationException("Could not load the class documentation for class '$className'.", e)
            }
        } finally {
            currentlyBuilding.removeLast()
        }
    }

    private File getFileForClass(String className) {
        File classFile = new File(classDocbookDir, "${className}.xml")
        classFile
    }
}

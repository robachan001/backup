/*
 * Copyright 2013 the original author or authors.
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

package org.gradle.buildinit.plugins.internal

import org.gradle.api.internal.DocumentationRegistry
import org.gradle.api.internal.file.FileResolver

class LanguageLibraryProjectInitDescriptor extends TemplateBasedProjectInitDescriptor {

    protected final String templatepackage
    protected final TemplateLibraryVersionProvider libraryVersionProvider
    private final String languageId

    LanguageLibraryProjectInitDescriptor(String templatepackage, String languageId,
                                         TemplateLibraryVersionProvider libraryVersionProvider,
                                         FileResolver fileResolver,
                                         DocumentationRegistry documentationRegistry,
                                         ProjectInitDescriptor... delegates) {
        super(fileResolver, documentationRegistry, delegates);
        this.languageId = languageId
        this.libraryVersionProvider = libraryVersionProvider
        this.templatepackage = templatepackage
    }

    @Override
    URL getBuildFileTemplate() {
        return getClass().getResource("/org/gradle/buildinit/tasks/templates/${templatepackage}/build.gradle.template");
    }

    protected generateClass(String sourceRoot, String clazzFileName) {
        if (fileResolver.resolveFilesAsTree("src/main/$languageId").empty || fileResolver.resolveFilesAsTree("src/test/$languageId").empty) {
            File sourceRootFolder = fileResolver.resolve(sourceRoot)
            File clazzFile = new File(sourceRootFolder, clazzFileName)
            URL productionClazzFileTemplate = getClass().getResource("/org/gradle/buildinit/tasks/templates/${templatepackage}/${clazzFileName}.template");
            generateFileFromTemplate(productionClazzFileTemplate, clazzFile, [:])
        }
    }
}

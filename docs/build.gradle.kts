/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.project.config.publishing.ProjectPublishingConfiguration

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.mkDocs)
}

antibytesDocumentation {
    versioning.set(ProjectPublishingConfiguration(project).versioning)
}

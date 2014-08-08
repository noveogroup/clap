package com.noveogroup.clap.gradle.config

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

class ClapOptions extends Options {

    Project project

    NamedDomainObjectContainer<CustomOptions> custom

    ClapOptions(Project project) {
        this.project = project
        this.custom = project.container(CustomOptions) { new CustomOptions(name: it) }
    }

    void custom(Action<NamedDomainObjectContainer<CustomOptions>> action) { action.execute(custom) }

}

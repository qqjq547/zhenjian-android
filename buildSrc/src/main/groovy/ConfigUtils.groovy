import org.gradle.api.Project
import org.gradle.api.ProjectEvaluationListener
import org.gradle.api.ProjectState
import org.gradle.api.invocation.Gradle

class ConfigUtils {

    static init(Gradle gradle) {
        generateDep(gradle)
        addCommonGradle(gradle)
        TaskDurationUtils.init(gradle)
    }

    private static addCommonGradle(Gradle gradle) {
        gradle.addProjectEvaluationListener(new ProjectEvaluationListener() {
            /**
             * 执行在各 module 的 build.gradle 之前
             * @param project
             */
            @Override
            void beforeEvaluate(Project project) {
                //GLog.d("beforeEvaluate")
                // 在 project 的 build.gradle 前 do sth.
                if (project.subprojects.isEmpty()) {// 定位到具体 project
                    if (project.path.startsWith(":plugin")) {
                        return
                    }
                    if (project.name.endsWith("_app")) {
                        GLog.l(project.toString() + " applies buildApp.gradle")
                        project.apply {
                            from "${project.rootDir.path}/buildApp.gradle"
                        }
                    } else {
                        GLog.l(project.toString() + " applies buildLib.gradle")
                        project.apply {
                            from "${project.rootDir.path}/buildLib.gradle"
                        }
                    }
                }
            }

            /**
             * 执行在各 module 的 build.gradle 之后
             * @param project
             * @param state
             */
            @Override
            void afterEvaluate(Project project, ProjectState state) {
                //GLog.d("afterEvaluate")
            }
        })
    }

    /**
     * 根据 depConfig 生成 dep
     */
    private static generateDep(Gradle gradle) {
        def configs = [:]
        for (Map.Entry<String, DepConfig> entry : Config.depConfig.entrySet()) {
            def (name, config) = [entry.key, entry.value]
            if (entry.value.pluginPath) {
                config.dep = config.pluginPath
            } else {
                if (config.useLocal) {
                    config.dep = gradle.rootProject.findProject(config.projectPath)
                } else {
                    config.dep = config.remotePath
                }
            }
            configs.put(name, config)
        }
        GLog.l("generateDep = ${GLog.object2String(configs)}")
    }

    static getApplyPkgs() {
        def pkgs = [:]
        for (Map.Entry<String, DepConfig> entry : Config.depConfig.entrySet()) {
            if (entry.value.isApply && entry.key.endsWith("_pkg")) {
                pkgs.put(entry.key, entry.value)
            }
        }
        GLog.d("getApplyPkgs = ${GLog.object2String(pkgs)}")
        return pkgs
    }

    static getApplyExports() {
        def exports = [:]
        for (Map.Entry<String, DepConfig> entry : Config.depConfig.entrySet()) {
            if (entry.value.isApply && entry.key.endsWith("_export")) {
                exports.put(entry.key, entry.value)
            }
        }
        GLog.d("getApplyExports = ${GLog.object2String(exports)}")
        return exports
    }

    static getApplyPlugins() {
        def plugins = [:]
        for (Map.Entry<String, DepConfig> entry : Config.depConfig.entrySet()) {
            if (entry.value.isApply && entry.value.pluginPath) {
                plugins.put(entry.key, entry.value)
            }
        }
        GLog.d("getApplyPlugins = ${GLog.object2String(plugins)}")
        return plugins
    }

}
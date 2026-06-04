package tokyo.northside

import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import java.util.concurrent.ConcurrentHashMap

abstract class XvfbService : BuildService<BuildServiceParameters.None> {
    private val pidByDisplay = ConcurrentHashMap<String, String>()

    fun setPid(display: String, pid: String?) {
        if (pid.isNullOrBlank()) {
            pidByDisplay.remove(display)
        } else {
            pidByDisplay[display] = pid
        }
    }

    fun getPid(display: String): String? {
        return pidByDisplay[display]
    }

    fun hasPid(display: String): Boolean {
        return pidByDisplay.containsKey(display)
    }
    fun allDisplays(): Set<String> = pidByDisplay.keys.toSet()
}

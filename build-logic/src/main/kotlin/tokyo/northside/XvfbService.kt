package tokyo.northside

import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import java.util.concurrent.atomic.AtomicReference

abstract class XvfbService : BuildService<BuildServiceParameters.None> {
    private val pidRef = AtomicReference("")

    fun setPid(pid: String?) {
        pidRef.set(pid.orEmpty())
    }

    fun getPid(): String {
        return pidRef.get().orEmpty()
    }

    fun hasPid(): Boolean {
        return getPid().isNotBlank()
    }

    fun clearPid() {
        pidRef.set("")
    }
}
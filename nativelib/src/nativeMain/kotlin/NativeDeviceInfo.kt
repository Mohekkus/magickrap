import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.*
import platform.posix.uname
import platform.posix.utsname

expect object NativeDeviceInfo {
    fun getHardwareInfo(): String
}
actual object NativeDeviceInfo {
    @OptIn(ExperimentalForeignApi::class)
    actual fun getHardwareInfo(): String {
        val utsname = nativeHeap.alloc<utsname>()
        try {
            uname(utsname.ptr)
            return "${utsname.machine.toKString()} (${utsname.sysname.toKString()} ${utsname.release.toKString()})"
        } finally {
            nativeHeap.free(utsname)
        }
    }
}
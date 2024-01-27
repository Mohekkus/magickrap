class NativeDeviceInfoInterface {

    fun getHardware(): String {
        return NativeDeviceInfo.getHardwareInfo()
    }
}
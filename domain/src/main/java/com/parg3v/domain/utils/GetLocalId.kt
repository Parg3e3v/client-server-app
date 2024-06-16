package com.parg3v.domain.utils

import java.net.Inet4Address
import java.net.NetworkInterface

fun getLocalIpAddress(): String? {
    val interfaces = NetworkInterface.getNetworkInterfaces()
    for (inf in interfaces) {
        val addresses = inf.inetAddresses
        for (address in addresses) {
            if (!address.isLoopbackAddress && address is Inet4Address) {
                return address.hostAddress
            }
        }
    }
    return null
}

package fr.wifinder.wifinderandroid.services

import android.net.wifi.ScanResult
import fr.wifinder.wifinderandroid.activities.HotspotsListActivity
import fr.wifinder.wifinderandroid.apis.CrowdsensingControllerApi
import fr.wifinder.wifinderandroid.models.FindInput
import fr.wifinder.wifinderandroid.models.HotspotInformation


class HotspotService(activity: HotspotsListActivity) {
    private val api = CrowdsensingControllerApi(activity = activity)

    fun findBestWifi(lat: Double, lng: Double, scanResults: List<ScanResult>): String? {
        val hotspotList = arrayListOf<HotspotInformation>()

        for (scanResult in scanResults) {
            val hotspot = HotspotInformation(scanResult.BSSID, scanResult.SSID, scanResult.level, scanResult.frequency)
            hotspotList.add(hotspot)
        }

        val input = FindInput(hotspotList.toTypedArray(), lat, lng)
        return try {
            api.findUsingPOST(input)
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
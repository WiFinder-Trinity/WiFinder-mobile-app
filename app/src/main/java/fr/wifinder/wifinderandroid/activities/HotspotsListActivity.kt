package fr.wifinder.wifinderandroid.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import fr.wifinder.wifinderandroid.R
import java.util.*
import java.util.logging.Logger


/* TODO: Documentation officielle https://developer.android.com/guide/topics/connectivity/wifi-scan#kotlin*/
class HotspotsListActivity : AppCompatActivity() {
    private lateinit var wifiManager: WifiManager
    private lateinit var locationManager: LocationManager
    private lateinit var textView: TextView
    private lateinit var listView: ListView
    private lateinit var scanButton: Button
    private lateinit var findButton: Button

    private val logger = Logger.getLogger(HotspotsListActivity::class.java.name)

    private var size = 10
    private lateinit var scanResults: List<ScanResult>
    private val scanNames = arrayListOf<String>()

    private lateinit var adapter: ArrayAdapter<String>

    private val wifiScanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            scanResults = wifiManager.scanResults
            unregisterReceiver(this)
            size = scanResults.size

            logger.info("wifi scanner received information !")
            textView.text = Date().toString() + " : got " + size.toString() + " wifi Hotspots"

            for (scanResult in scanResults) {
                scanNames.add(scanResult.BSSID + " : " + scanResult.SSID + " | " + scanResult.level.toString() + "dB")
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotspots_list)

        //set Text view
        textView = findViewById(R.id.textInfo)
        textView.text = "Application starting ...."

        //set List view
        listView = findViewById(R.id.wifiList)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, scanNames)
        listView.adapter = adapter

        //set Scan Button
        scanButton = findViewById(R.id.scanBtn)
        scanButton.setOnClickListener { scanWifi() }

        //set Find Button
        findButton = findViewById(R.id.findBtn)
        findButton.setOnClickListener { callFindService() }

        //set Managers
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!wifiManager.isWifiEnabled) {
            Toast.makeText(this, "WiFi is disabled ... We need to enable it", Toast.LENGTH_LONG).show()
            wifiManager.isWifiEnabled = true
        }

        scanWifi()
    }

    fun scanWifi() {
        scanNames.clear()
        registerReceiver(wifiScanReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        Toast.makeText(this, "Scanning Wifi ....", Toast.LENGTH_SHORT).show()
        val success = wifiManager.startScan()

        if (!success)
            Toast.makeText(this, "Scanning Failed :'(", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, "Scanning Succeed ;)", Toast.LENGTH_SHORT).show()
    }

    fun callFindService() {

    }
}

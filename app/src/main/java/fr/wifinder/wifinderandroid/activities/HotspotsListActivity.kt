package fr.wifinder.wifinderandroid.activities

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.location.LocationManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import fr.wifinder.wifinderandroid.R
import fr.wifinder.wifinderandroid.services.HotspotService
import java.util.*
import java.util.logging.Logger


/* TODO: Documentation officielle https://developer.android.com/guide/topics/connectivity/wifi-scan#kotlin*/
class HotspotsListActivity : AppCompatActivity() {
    // Android API Managers
    private lateinit var wifiManager: WifiManager
    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Graphic elements
    private lateinit var textView: TextView
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var scanButton: Button
    private lateinit var findButton: Button

    private val logger = Logger.getLogger(HotspotsListActivity::class.java.name)

    private val hs = HotspotService(this)

    // Data
    private var size = 0
    private lateinit var scanResults: List<ScanResult>
    private val scanNames = arrayListOf<String>()
    private var current_lng = .0
    private var current_lat = .0

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //TODO : Warning !! GPS must be enable to work well
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

    @SuppressLint("MissingPermission")
    fun callFindService() {
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        current_lat = location.latitude
                        current_lng = location.longitude
                        Toast.makeText(this, "Position : " + location.latitude + " " + location.longitude, Toast.LENGTH_SHORT).show()
                        hs.findBestWifi(current_lat, current_lng, scanResults)
                    } else
                        Toast.makeText(this, "Position not found :(", Toast.LENGTH_SHORT).show()
                }
    }
}

package fr.wifinder.wifinderandroid.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import fr.wifinder.wifinderandroid.services.PermissionsService
import fr.wifinder_trinity.wifinderandroid.R
import kotlinx.android.synthetic.main.activity_hotspots_list.*
import java.util.*

/* TODO: Documentation officielle https://developer.android.com/guide/topics/connectivity/wifi-scan#kotlin*/
class HotspotsListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val ps = PermissionsService()

    private lateinit var wifiManager: WifiManager
    private var permissionsGranted = false

    private val wifiScanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if (!success) {
                textView.text = "Failure [" + Date().toString() + "]: " +wifiManager.scanResults.size.toString() +" hotspots"
            } else{
                textView.text = "Success [" +Date().toString() + "]: " + wifiManager.scanResults.size.toString()+ " hotspots"
            }
            // TODO: reload hotspots list
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotspots_list)

        permissionsGranted = ps.getHotspotsListPermission(this)
        if(permissionsGranted) {
            wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val intentFilter = IntentFilter()
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            baseContext.registerReceiver(wifiScanReceiver, intentFilter)
            wifiManager.startScan()

            textView.text = "INIT: "+ wifiManager.scanResults.size.toString()

            viewManager = LinearLayoutManager(this)
            viewAdapter = HotspotAdapter(wifiManager.scanResults)
            recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                /* setHasFixedSize(true) */
                layoutManager = viewManager
                adapter = viewAdapter
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        permissionsGranted = false
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionsGranted = true
                }
            }
        }
    }

        /*companion object {

            class NetworkTask internal constructor(context: HotspotsListActivity) : AsyncTask<Int, Void, String>(){

                private var api: CrowdsensingControllerApi = CrowdsensingControllerApi()
                private val activityReference: WeakReference<HotspotsListActivity> = WeakReference(context)

                override fun doInBackground(vararg param: Int?): String {
                    Log.i("Requête: ", param.toString())
                    return api.testUsingGET("Bonjour")
                }

                override fun onPostExecute(result: String?) {
                    super.onPostExecute(result)
                    val activity = activityReference.get()
                    activity?.text_view?.text = result.let { it }
                }
            }
        }*/
}

package fr.wifinder.wifinderandroid.activities

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import fr.wifinder.wifinderandroid.apis.CrowdsensingControllerApi
import fr.wifinder_trinity.wifinderandroid.R
import kotlinx.android.synthetic.main.activity_hotspots_list.*
import java.lang.ref.WeakReference
import java.util.Arrays.asList



class HotspotsListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotspots_list)
        viewManager = LinearLayoutManager(this)
        viewAdapter = HotspotAdapter()

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            /* setHasFixedSize(true) */
            layoutManager = viewManager
            adapter = viewAdapter
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

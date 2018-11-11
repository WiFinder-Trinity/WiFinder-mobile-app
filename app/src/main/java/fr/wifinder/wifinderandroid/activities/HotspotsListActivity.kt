package fr.wifinder.wifinderandroid.activities

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import fr.wifinder.wifinderandroid.apis.CrowdsensingControllerApi
import fr.wifinder_trinity.wifinderandroid.R
import kotlinx.android.synthetic.main.activity_hotspots_list.*
import java.lang.ref.WeakReference

class HotspotsListActivity : AppCompatActivity() {

    private var nbClick = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotspots_list)
        button.setOnClickListener {
            nbClick++
            //NetworkTask(this).execute(2)
        }
        webview!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        webview!!.loadUrl("https://www.google.fr/")
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

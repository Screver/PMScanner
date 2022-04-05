package be.sremy.maraudeursscanner

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import be.sremy.maraudeursscanner.Entities.QrCodes
import com.budiyev.android.codescanner.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.my_row.*

private const val CAMERA_REQUEST_CODE = 101

class MainActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var scanner_view: CodeScannerView
    private lateinit var tv_textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonListActivity = findViewById<FloatingActionButton>(R.id.list_button)
        buttonListActivity.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

        tv_textView = findViewById(R.id.tv_textView)
        scanner_view = findViewById(R.id.scanner_view)

        setupPermission()
        codeScanner()
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, scanner_view)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    val filtered = "[]\']"
                    var decode = it.text.filterNot{filtered.indexOf(it) > -1}
                    var elements = decode.split(",").toTypedArray()

                    //SI LE QR CODE EST VALIDE
                    if (elements.size == 5) {
                        val qrcode = QrCodes(elements[0], elements[1], elements[2], elements[3].replace(" ","").toInt())

                        val databaseHandler: DatabaseHandler = DatabaseHandler(applicationContext)
                        val ticket = databaseHandler.searchSingleTicket(qrcode.number)

                        var newflag = ""
                        if (ticket.flag == "FALSE") {
                            newflag = "TRUE"
                            tv_textView.text = qrcode.date + " " + qrcode.jour + " " + qrcode.number + ticket.flag
                            databaseHandler.updateTicket(TicketModelClass(ticket.id,"",newflag))
                        } else{
                            tv_textView.text = "CE QR CODE A DEJA ETE VALIDE"
                        }
                    } else {
                        tv_textView.text = "CE QR CODE N'EST PAS VALIDE"
                    }
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "Problème d'implémentation de la caméra : ${it.message}")
                }
            }
        }

//                  A rajouter pour traiter les code un par un en cliquant sur l'écran

//        scanner_view.setOnClickListener {
//            codeScanner.startPreview()
//        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermission() {
        val permission = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode : Int, permissions: Array<out String>,grantResults: IntArray) {
        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Vous devez autoriser la caméra pour utiliser l'application. Fermez et relancez...", Toast.LENGTH_SHORT).show()
                } else {
                    // si ça fonctionne
                }
            }
        }
    }
}
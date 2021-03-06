package be.sremy.maraudeursscanner

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import be.sremy.maraudeursscanner.Entities.QrCodes
import com.budiyev.android.codescanner.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search_dialog.*

private const val CAMERA_REQUEST_CODE = 101

class MainActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var scanner_view: CodeScannerView
    private lateinit var tv_textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();

        setContentView(R.layout.activity_main)

        val numeroListActivity = findViewById<TextView>(R.id.nmbre_places)
        numeroListActivity.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

        val buttonSearchActivity = findViewById<FloatingActionButton>(R.id.search_button)
        buttonSearchActivity.setOnClickListener {
            searchDialog()
        }

        tv_textView = findViewById(R.id.tv_textView)
        scanner_view = findViewById(R.id.scanner_view)

        countTickets()
        setupPermission()
        codeScanner()
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, scanner_view)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    val filtered = "[]\']"
                    val decode = it.text.filterNot{filtered.indexOf(it) > -1}
                    val elements = decode.split(",").toTypedArray()

                    //SI LE QR CODE EST VALIDE
                    if (elements.size == 5) {
                        val qrcode = QrCodes(elements[0], elements[1], elements[2], elements[3].replace(" ","").toInt())

//                        //TODO Selon les places ?? exclure
//                        if (qrcode.number in 43..46 || qrcode.number in 79..81 || qrcode.number in 109..110) {
//                            Toast.makeText(applicationContext, "Les tickets 43, 44, 45, 46, 109 et 110 n'existent pas... Cfr billetterie", Toast.LENGTH_LONG).show()
//                            tv_textView.text = getText(R.string.fraud_ticket)
//
//                        } else {

                        val databaseHandler: DatabaseHandler = DatabaseHandler(applicationContext)
                        val ticket = databaseHandler.searchSingleTicket(qrcode.number)
                        if (qrcode.jour != ticket.day) {
                            tv_textView.text = getText(R.string.unvalid_ticket)
                        } else {
                        var newflag = ""
                        if (ticket.flag == "FALSE") {
                            newflag = "TRUE"
                            tv_textView.text = getText(R.string.valid_ticket)

                            databaseHandler.updateTicket(TicketModelClass(ticket.id,"",newflag))
                        } else{
                            tv_textView.text = getText(R.string.already_validated)
                        }
                        }
//                        } //TODO Selon les places ?? exclure
                    } else {
                        tv_textView.text = getText(R.string.unvalid_ticket)
                    }
                    countTickets()
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "Probl??me d'impl??mentation de la cam??ra : ${it.message}")
                }
            }
        }

//                  A rajouter pour traiter les code un par un en cliquant sur l'??cran
        scanner_view.setOnClickListener {
            codeScanner.startPreview()
            tv_textView.text = getText(R.string.scannez_un_billet)

        }
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Vous devez autoriser la cam??ra pour utiliser l'application. Fermez et relancez...", Toast.LENGTH_SHORT).show()
                } else {
                    // si ??a fonctionne
                }
            }
        }
    }

    fun searchDialog() {
        val searchDialog = Dialog(this, R.style.Theme_Dialog)
        searchDialog.setCancelable(false)

        searchDialog.setContentView(R.layout.search_dialog)

        searchDialog.switch_search_button.setOnClickListener(View.OnClickListener{
            val number = searchDialog.etSearchNumber.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if (number.isNotEmpty() && number.isDigitsOnly()) {
                if (number.toInt() in 1..440) {
//                    if (number.toInt() in 43..46 || number.toInt() in 79..81 || number.toInt() in 109..110) { //TODO Selon les places ?? exclure
//                        tv_textView.text = getText(R.string.fraud_ticket)
//                        Toast.makeText(applicationContext, "Les tickets 43, 44, 45, 46, 79,80,81, 109 et 110 n'existent pas... Voir billetterie", Toast.LENGTH_LONG).show()
//                        searchDialog.dismiss()
//                    } else {
                        val ticket = databaseHandler.searchSingleTicket(number.toInt())
                        var newflag = ""
                        if (ticket.flag == "FALSE") {
                            newflag = "TRUE"
                            val status = databaseHandler.updateTicket(
                                TicketModelClass(
                                    number.toInt(),
                                    "",
                                    newflag
                                )
                            )
                            if (status > -1) {
                                searchDialog.dismiss()
                                Toast.makeText(
                                    applicationContext,
                                    "Ticket mis ?? jour",
                                    Toast.LENGTH_SHORT
                                ).show()
                                tv_textView.text = getText(R.string.valid_ticket)
                                countTickets()
                            }
                        } else {
                            tv_textView.text = getText(R.string.already_validated)
                            searchDialog.dismiss()
                        }
//                    } //TODO Selon les places ?? exclure
                }else {
                    Toast.makeText(applicationContext, "Votre num??ro de ticket n'est pas valide", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Vous devez entrer un num??ro de ticket...", Toast.LENGTH_SHORT).show()
            }
        })
        searchDialog.cancel_button.setOnClickListener(View.OnClickListener { searchDialog.dismiss() })
        countTickets()
        searchDialog.show()
    }

    fun countTickets(){
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        nmbre_places.text = databaseHandler.countPresentTicket().toString()
    }

}
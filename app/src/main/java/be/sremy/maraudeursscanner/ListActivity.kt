package be.sremy.maraudeursscanner

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.LinearLayoutManager
import be.sremy.maraudeursscanner.Adapters.ItemAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_list.*

import kotlinx.android.synthetic.main.search_dialog.*

class ListActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        setupListOfDataIntoRecyclerView()

        val buttonSearchActivity = findViewById<FloatingActionButton>(R.id.search_ticket)
        buttonSearchActivity.setOnClickListener {
            searchDialog()
        }
    }

    fun switchTicketFlag(ticketModelClass: TicketModelClass, pos: Int) {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        var newflag = ""
        newflag = if (ticketModelClass.flag == "FALSE") {
            "TRUE"
        } else {
            "FALSE"
        }

        list_view.layoutManager = LinearLayoutManager(this)


        databaseHandler.updateTicket(TicketModelClass(ticketModelClass.id,"",newflag))

        val itemAdapter = ItemAdapter(this,getItemsList())

        list_view.adapter = itemAdapter
        list_view.scrollToPosition(pos-1)

    }

    private fun setupListOfDataIntoRecyclerView() {
        if (getItemsList().size > 0 ) {
            list_view.visibility = View.VISIBLE
            list_noRecord.visibility = View.GONE

            list_view.layoutManager = LinearLayoutManager(this)

            val itemAdapter = ItemAdapter(this, getItemsList())

            list_view.adapter = itemAdapter
        } else {
            list_view.visibility = View.GONE
            list_noRecord.visibility = View.VISIBLE
        }
    }

    private fun getItemsList(): ArrayList<TicketModelClass> {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)

        return databaseHandler.viewTickets()
    }

    fun searchDialog() {
        val searchDialog = Dialog(this, R.style.Theme_Dialog)
        searchDialog.setCancelable(false)

        searchDialog.setContentView(R.layout.search_dialog)
        searchDialog.switch_search_button.text = getString(R.string.search_dialog_for_list)

        searchDialog.switch_search_button.setOnClickListener(View.OnClickListener{
            val number = searchDialog.etSearchNumber.text.toString()

            if (number.isNotEmpty() && number.isDigitsOnly()) {
                var ticketNum  = number.toInt()
//                val ticketNum  = number.toInt()
                if (ticketNum in 1..440) {

////                    //TODO Selon les places à exclure
//                    if (number.toInt() in 43..46) {
//                        ticketNum = 42
//                        Toast.makeText(applicationContext, "Les tickets 43, 44, 45, 46, 109 et 110 n'existent pas... Voir billetterie", Toast.LENGTH_LONG).show()
//                    } else if (number.toInt() >= 43) {
//                        ticketNum -= 4
//                    }
//                    if (number.toInt() in 79..81) {
//                        ticketNum = 74
//                        Toast.makeText(applicationContext, "Les tickets 43, 44, 45, 46, 109 et 110 n'existent pas... Voir billetterie", Toast.LENGTH_LONG).show()
//
//                    } else if (number.toInt() >= 79) {
//                        ticketNum -= 3
//                    }
//                    if (number.toInt() in 109..110) {
//                        ticketNum = 100
//                        Toast.makeText(applicationContext, "Les tickets 43, 44, 45, 46, 109 et 110 n'existent pas... Voir billetterie", Toast.LENGTH_LONG).show()
//
//                    } else if (number.toInt() >= 109) {
//                        ticketNum -= 2
//                    }


                    list_view.layoutManager = LinearLayoutManager(this)
                    val itemAdapter = ItemAdapter(this,getItemsList())
                    list_view.adapter = itemAdapter
                    list_view.scrollToPosition(ticketNum-1)
                    searchDialog.dismiss()

                    }else {
                    Toast.makeText(applicationContext, "Votre numéro de ticket n'est pas valide", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Vous devez entrer un numéro de ticket...", Toast.LENGTH_SHORT).show()
            }
        })
        searchDialog.cancel_button.setOnClickListener(View.OnClickListener { searchDialog.dismiss() })
        searchDialog.show()
    }
}
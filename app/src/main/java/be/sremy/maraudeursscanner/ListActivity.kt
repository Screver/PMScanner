package be.sremy.maraudeursscanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import be.sremy.maraudeursscanner.Adapters.ItemAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.my_row.*
import kotlinx.android.synthetic.main.my_row.view.*

class ListActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        setupListOfDataIntoRecyclerView()
    }

    fun switchTicketFlag(ticketModelClass : TicketModelClass) {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        var newflag = ""
        newflag = if (ticketModelClass.flag == "FALSE") {
            "TRUE"
        } else {
            "FALSE"
        }

        databaseHandler.updateTicket(TicketModelClass(ticketModelClass.id,"",newflag))
        setupListOfDataIntoRecyclerView()


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
}
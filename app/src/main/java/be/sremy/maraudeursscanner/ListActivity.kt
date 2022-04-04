package be.sremy.maraudeursscanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import be.sremy.maraudeursscanner.Adapters.ItemAdapter
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    private val db: DatabaseHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val db = DatabaseHandler(this)

        list_view.LayoutManager = LinearLayoutManager(this)

        val itemAdapter = ItemAdapter(this, getItemsList())

        list_view.adapter = itemAdapter
    }


    private fun getItemsList(): ArrayList<String> {
//        val databaseHandler: DatabaseHandler = DatabaseHandler(this)

        val list = ArrayList<String>()

        for(i in 1..15){
            list.add("Item $i")
        }

        return list
//        return db.viewTickets()
    }
}
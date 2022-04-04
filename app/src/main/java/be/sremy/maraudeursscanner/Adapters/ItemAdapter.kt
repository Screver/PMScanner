package be.sremy.maraudeursscanner.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import be.sremy.maraudeursscanner.R
import kotlinx.android.synthetic.main.my_row.view.*

class ItemAdapter(val context: Context, val items: ArrayList<String>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.my_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.tvItem.text = item

        if (position % 2 == 0) {
            holder.cardViewItem.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.lightGray
                )
            )
        } else {
            holder.cardViewItem.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItem = view.ticket_flag_text
        val cardViewItem = view.ticket_day_text

    }
}
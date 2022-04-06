package be.sremy.maraudeursscanner.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import be.sremy.maraudeursscanner.ListActivity
import be.sremy.maraudeursscanner.R
import be.sremy.maraudeursscanner.TicketModelClass
import kotlinx.android.synthetic.main.my_row.view.*

class ItemAdapter(val context: Context, val items: ArrayList<TicketModelClass>) :
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

        holder.ticket_id.text = item.id.toString()
        holder.ticketFlag.text = item.flag
        holder.ticketDay.text = item.day


        if (position % 2 == 0) {
            holder.backgroundColor.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.transparent
                )
            )
        } else {
            holder.backgroundColor.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.slightly_transparent
                )
            )
        }

        holder.flagSwitchButton.setOnClickListener{view ->
            if (context is ListActivity) {
                context.switchTicketFlag(item)
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val backgroundColor = view.cardview_item

        val ticket_id = view.ticket_id_text
        val ticketFlag = view.ticket_flag_text
        val ticketDay = view.ticket_day_text
        val flagSwitchButton = view.flag_switch_button

    }
}
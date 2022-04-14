package be.sremy.maraudeursscanner.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import be.sremy.maraudeursscanner.ListActivity
import be.sremy.maraudeursscanner.R
import be.sremy.maraudeursscanner.TicketModelClass
import kotlinx.android.synthetic.main.my_row.view.*

class ItemAdapter(private val context: Context, private val items: ArrayList<TicketModelClass>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate( R.layout.my_row, parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        //Setting the ticket number
        holder.ticket_id.text = item.id.toString()

        //Setting the day
        holder.ticketDay.text = item.day

        //Setting validation button
        holder.flagSwitchButton.setOnClickListener{
            if (context is ListActivity) {
                 val pos = holder.adapterPosition
                context.switchTicketFlag(item, pos)
                Toast.makeText(context, pos.toString(), Toast.LENGTH_SHORT ).show()
            }
        }

        //Setting ticket flag image
        if (item.flag == "FALSE") {
            holder.ticketFlag.setImageResource(R.drawable.ic_ticket_nothere)
        } else {
            holder.ticketFlag.setImageResource(R.drawable.ic_ticket_present)
        }

        //Setting alternative background
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
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val backgroundColor = view.cardview_item

        val ticket_id = view.ticket_id_text
        val ticketFlag = view.ticket_flag_image
        val ticketDay = view.ticket_day_text
        val flagSwitchButton = view.flag_switch_button

    }
}
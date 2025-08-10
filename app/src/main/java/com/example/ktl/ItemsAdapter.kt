package com.example.ktl

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView

class ItemsAdapter(var items: List<Item>,var context: Context): RecyclerView.Adapter<ItemsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        val name:TextView = view.findViewById(R.id.item_list_title_name)
        val data:TextView = view.findViewById(R.id.item_list_data)
        val time:TextView = view.findViewById(R.id.item_list_time)
        val line:View = view.findViewById(R.id.line)
        //val desc:TextView = view.findViewById(R.id.item_list_desc)
        val price:TextView = view.findViewById(R.id.item_list_text_count_price)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_list, parent,false)




        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.name.text = items[position].name


        if(position==0){
            holder.data.text = items[position].data

            holder.line.visibility = View.INVISIBLE


            }
        if (position!==0 && items[position].data.equals(items[position-1].data))
            {
                holder.data.text =""
                holder.line.visibility = View.INVISIBLE
            }else{
            holder.data.text = items[position].data
            holder.line.visibility = View.VISIBLE
            }

        holder.time.text = items[position].time
       // holder.desc.text = items[position].desc
        holder.price.text = items[position].col.toString() + " чел.   " + items[position].price.toString() + "руб."

        holder.time.setOnClickListener {

            val intent = Intent(context,ItemActivity::class.java)

            intent.putExtra("itemName",items[position].name)
            intent.putExtra("itemData",items[position].data)
            intent.putExtra("itemTime",items[position].time)
            intent.putExtra("itemCol",items[position].col)
            intent.putExtra("itemDesc",items[position].desc)
            intent.putExtra("itemPrice",items[position].price)
            intent.putExtra("itemId",items[position].id)

            System.out.println("!!!!!! ${items[position].id})")
                context.startActivity(intent)

        }
    }


}
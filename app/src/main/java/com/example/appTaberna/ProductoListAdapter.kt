package com.example.appTaberna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ProductoListAdapter : ListAdapter<Producto, ProductoListAdapter.WordViewHolder>(WORDS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val descripcionItemView: TextView = itemView.findViewById(R.id.txtViewDescripcion)
        private val precioItemView: TextView = itemView.findViewById(R.id.txtViewPrecio)

        //init {
          //  itemView.setOnClickListener {
            //    onItemClick?.invoke(getItem(adapterPosition))
            //}
        //}

        fun bind(producto: Producto) {
            descripcionItemView.text = producto.descripcion
            precioItemView.text = producto.precio
        }

        companion object {
            fun create(parent: ViewGroup): WordViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.recyclerview_item, parent, false)
                return WordViewHolder(view)
            }
        }
    }

    companion object {
        private val WORDS_COMPARATOR = object : DiffUtil.ItemCallback<Producto>() {
            override fun areItemsTheSame(oldItem: Producto, newItem: Producto): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Producto, newItem: Producto): Boolean {
                return oldItem == newItem
            }
        }
    }
}
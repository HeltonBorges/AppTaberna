package com.example.appTaberna

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    var listaProduccto: MutableList<Producto> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = ProductoListAdapter {
            Toast.makeText(requireContext(), it.descripcion, Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        cargarlista(adapter)

    }

    fun cargarlista(adapter: ProductoListAdapter){
        val TAG = "App"
        db.collection("productos")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        listaProduccto.add(Producto(document.id,
                                document.get("Descripcion") as String,
                                document.get("Precio") as String))
                        Log.d(TAG, "$listaProduccto")
                    }
                    adapter.submitList(listaProduccto)
                    //adapter.onItemClick = {
                      //  Toast.makeText(requireContext(), "${it.descripcion}", Toast.LENGTH_SHORT).show()
                    //}

                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }

    }
}
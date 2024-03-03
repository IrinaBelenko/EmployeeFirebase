package com.example.employeefirebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TodoListFragment : Fragment() {

    private var list: ListView? = null
    private var account: GoogleSignInAccount? = null
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(
        "https://employeelist-f6d95-default-rtdb.europe-west1.firebasedatabase.app/"
    )
    private var target: DatabaseReference? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.todo_list_fragment, container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab: FloatingActionButton = view.findViewById(R.id.fab)
        list = view.findViewById(R.id.employeeList)
        account = GoogleSignIn.getLastSignedInAccount(requireContext())
        target = database.reference
            .child(account?.id ?: "unknown_account").child("todo")
        fab.setOnClickListener {
            val activity = requireActivity() as onAddClickListener
            activity.onFabClick()
        }

        target?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val todoList = mutableListOf<String>()
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val employee = it?.getValue(String::class.java) ?: ""
                        todoList.add(employee)
                    }
                    val adapter = ArrayAdapter(
                        requireActivity(),
                        android.R.layout.simple_list_item_1, todoList
                    )
                    list?.adapter = adapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}

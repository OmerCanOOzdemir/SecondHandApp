package com.example.secondhandapplication.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.secondhandapp.data.user.UserViewModel
import com.example.secondhandapplication.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text


class EditEmailPasswordFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_edit_email_password, container, false)

        //Get firebaseAuth instance
        auth = FirebaseAuth.getInstance()


        //set user values
        val email = view.findViewById<EditText>(R.id.update_email_input)
        email.setText(auth.currentUser!!.email)

        //Update event
        val update_btn = view.findViewById<Button>(R.id.update_email_password_button)
        update_btn.setOnClickListener {
            updateUserFirebase(view)
        }
        return view
    }
    private fun updateUserFirebase(view: View){
        val email = view.findViewById<EditText>(R.id.update_email_input)
        val password = view.findViewById<EditText>(R.id.update_password_input)
        val retype_password = view.findViewById<EditText>(R.id.update_retype_password)
        val old_password = view.findViewById<EditText>(R.id.update_old_password_input)

        if(!TextUtils.isEmpty(email.text.toString()) && TextUtils.isEmpty(password.text.toString())&& TextUtils.isEmpty(retype_password.text.toString())){
            updateOnlyEmail(email,old_password,view)
        }else if(!TextUtils.isEmpty(email.text.toString()) && !TextUtils.isEmpty(password.text.toString())&& !TextUtils.isEmpty(retype_password.text.toString())){
            if(retype_password.text.toString().equals(password.text.toString())){
                updateEmailAndPassword(email,old_password,password,view)
            }else{

                retype_password.error = getString(R.string.compare_password_error)
                retype_password.requestFocus()
            }
        }

    }
    private fun updateUserRoom(old_email:String,new_email:String){
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.updateEmailUser(old_email,new_email)

    }
    private fun updateOnlyEmail(email :EditText,old_password:EditText, view: View){
        if(TextUtils.isEmpty(old_password.text)){
            old_password.error = getString(R.string.old_password_empty_error)
            old_password.requestFocus()
        }else{
            //Update email only
            val user = auth.currentUser
            val old_email = user!!.email
            val credential = EmailAuthProvider.getCredential(user!!.email!!,old_password.text.toString())
            //For updating mail must be reauthenticate
            user.reauthenticate(credential).addOnCompleteListener {
                user!!.updateEmail(email.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            updateUserRoom(old_email!!,email.text.toString())
                            Toast.makeText(context,getString(R.string.email_update),Toast.LENGTH_SHORT).show()
                            Navigation.findNavController(view).navigate(R.id.action_editEmailPasswordFragment_to_profileFragment)

                        }
                    }
            }
        }
    }
    private fun updateEmailAndPassword(email:EditText,old_password : EditText,new_password: EditText,view: View){
        val user = auth.currentUser
        val old_email = user!!.email
        val credential = EmailAuthProvider.getCredential(user!!.email!!, old_password.text.toString())

        user.reauthenticate(credential).addOnCompleteListener {
            user.updatePassword(new_password.text.toString()).addOnCompleteListener {
                task -> if(task.isSuccessful){
                user!!.updateEmail(email.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            updateUserRoom(old_email!!,email.text.toString())
                            Toast.makeText(context,getString(R.string.update_email_password),Toast.LENGTH_SHORT).show()
                            Navigation.findNavController(view).navigate(R.id.action_editEmailPasswordFragment_to_profileFragment)

                        }
                    }
            }
            }

        }
    }

}
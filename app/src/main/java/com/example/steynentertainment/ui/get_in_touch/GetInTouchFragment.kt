package com.example.steynentertainment.ui.get_in_touch

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.steynentertainment.R
import com.google.android.material.textfield.TextInputEditText

class GetInTouchFragment : Fragment()
{
    private lateinit var name: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var subject: TextInputEditText
    private lateinit var message: TextInputEditText
    private lateinit var sendMessageBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_get_in_touch, container, false)

        name = view.findViewById(R.id.nameEditText)
        email = view.findViewById(R.id.emailText)
        subject = view.findViewById(R.id.subjectText)
        message = view.findViewById(R.id.messageText)
        sendMessageBtn = view.findViewById(R.id.sendMessageBtn)

        sendMessageBtn.setOnClickListener {
            if (validateFields())
            {
                sendEmail()
            }
        }
        return view
    }

    //Validating user inputs
    private fun validateFields(): Boolean
    {
        var isValid = true

        if (name.text.isNullOrBlank())
        {
            name.error = "Name cannot be empty"
            isValid = false
        }
        else
        {
            name.error = null
        }

        if (email.text.isNullOrBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches())
        {
            email.error = "Enter a valid and registered email"
            isValid = false
        }
        else
        {
            email.error = null
        }

        if (subject.text.isNullOrBlank())
        {
            subject.error = "Subject cannot be empty"
            isValid = false
        }
        else
        {
            subject.error = null
        }

        if (message.text.isNullOrBlank())
        {
            message.error = "Message cannot be empty"
            isValid = false
        }
        else
        {
            message.error = null
        }
        return isValid
    }

    //This method uses an intent to send email to the user
    private fun sendEmail()
    {
        val recipientEmail = email.text.toString()
        val senderName = name.text.toString()
        val emailSubject = subject.text.toString()
        val emailMessage = message.text.toString()
        //Using intent to allow user to send email using signed in email address
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
        intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
        intent.putExtra(Intent.EXTRA_TEXT, "Sender's Name: $senderName\n\n$emailMessage")

        try
        {
            startActivity(Intent.createChooser(intent, "Send Email"))
        }
        catch (e: ActivityNotFoundException)
        {
            // Handle the case where no email client is installed
            Toast.makeText(context, "No email client installed!", Toast.LENGTH_SHORT).show()
        }
    }
}
////////////////////////////////////////////////// END OF CLASS //////////////////////////////////////////////////
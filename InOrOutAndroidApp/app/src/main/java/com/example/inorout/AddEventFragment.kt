package com.example.inorout

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddEventFragment : Fragment() {
    lateinit var eventName: EditText
    lateinit var eventType: EditText
    lateinit var eventDescription: EditText
    lateinit var numOfApplicantsNeeded: EditText
    lateinit var occurenceSetTime: TextView
    lateinit var occurenceSetDate: TextView
    lateinit var addEventButton: Button
    private lateinit var chooseImage: Button
    lateinit var imageUrl: TextView
    lateinit var progressBar: ProgressBar
    var selectedImage: Uri? = null
    lateinit var sessionAuthenticator: SessionAuthenticator
    private lateinit var responseManager: ResponseManager





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_add_event, container, false
        )
        sessionAuthenticator = SessionAuthenticator(requireActivity())
        responseManager = ResponseManager(requireContext())

        eventName = view.findViewById(R.id.editTextEventName)
        eventType = view.findViewById(R.id.editTextEventType)
        eventDescription = view.findViewById(R.id.editTextEventDescription)
        numOfApplicantsNeeded = view.findViewById(R.id.ediTextNumOfApplicantsNeeded)
        chooseImage = view.findViewById(R.id.imagePickerButton)
        progressBar = view.findViewById(R.id.progressbar)
        progressBar.visibility = View.INVISIBLE
        imageUrl = view.findViewById(R.id.imageUrl)
        chooseImage.setOnClickListener{
            openImageSelector()
        }

        //dialog for setting the time of event
        occurenceSetTime = view.findViewById(R.id.occurenceSetTime)
        occurenceSetTime.setOnClickListener{
           val calendar = Calendar.getInstance()
           val timePickerListener = TimePickerDialog.OnTimeSetListener{
               timePicker, hour, minute ->
               calendar.set(Calendar.HOUR_OF_DAY,hour)
               calendar.set(Calendar.MINUTE,minute)
               occurenceSetTime.text = SimpleDateFormat("HH:mm").format(calendar.time)

           }
            TimePickerDialog(requireContext(),timePickerListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show()

           }


        //dialog for setting the date of the event
        occurenceSetDate = view.findViewById(R.id.occurenceSetDate)
        occurenceSetDate.setOnClickListener{
            val calendar = Calendar.getInstance()
            var datePickerListener = DatePickerDialog.OnDateSetListener { DatePicker, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_YEAR, day)
                occurenceSetDate.text = SimpleDateFormat("YYYY-MM-DD").format(calendar.time)
            }
            DatePickerDialog(requireContext(),datePickerListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_YEAR)).show()

        }



        addEventButton = view.findViewById(R.id.submitAddEventButton)
        addEventButton.setOnClickListener{

            if(TextUtils.isEmpty(eventName.text)) eventName.error = "Required!"
            else if(TextUtils.isEmpty(eventType.text)) eventType.error = "Required!"
            else if(TextUtils.isEmpty(eventDescription.text)) eventDescription.error = "Required!"
            else if(TextUtils.isEmpty(numOfApplicantsNeeded.text)) numOfApplicantsNeeded.error = "Required!"

            else {

                val fileResolver =
                    activity?.contentResolver?.openFileDescriptor(selectedImage!!, "r")
                val file = File(
                    activity?.cacheDir,
                    activity?.contentResolver?.getFileName(selectedImage!!)
                )
                val inputStream = FileInputStream(fileResolver?.fileDescriptor)
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)


                var eventAdd = EventAdd(
                    name = eventName.text.toString(),
                    type = eventType.text.toString(),
                    description = eventDescription.text.toString(),
                    occurence = occurenceDateTimeValue(
                        occurenceSetDate.text.toString(),
                        occurenceSetTime.text.toString()
                    ),
                    numOfApplicantsNeeded = numOfApplicantsNeeded.text.toString().toInt(),
                )
                val service = retrofit.create(SimpleService::class.java)
                progressBar.visibility = View.VISIBLE
                CoroutineScope(Dispatchers.IO).launch {

                    val body = MultipartBody.Part.createFormData(
                        "image", "userImage",
                        file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    )
                    val response = service.eventCreate(
                        token = "Bearer ${sessionAuthenticator.fetchAccessToken()}",
                        body, eventAdd = eventAdd.toPartMap()
                    )
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            progressBar.visibility = View.INVISIBLE
                            Toast.makeText(
                                requireContext(),
                                "You have successfully created an event",
                                Toast.LENGTH_LONG
                            ).show()
                            clearInput()
                        } else {
                            var error =
                                responseManager.getResponseErrorDetails(response.errorBody())
                            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()

                        }
                    }


                }
            }
        }



        return view


    }
    private fun occurenceDateTimeValue(yearMonthDay: String,hourMinute: String): String{

       return "$yearMonthDay"+"T${hourMinute}:00.290Z"
    }

    private fun openImageSelector() {
        Intent(Intent.ACTION_PICK).also{
            it.type="image/*"
            startActivityForResult(it,100)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                100-> {
                    selectedImage=data?.data
                    imageUrl.text = "Image Selected!"

            }

            }
        }
    }

    private fun clearInput(){
        eventName.text.clear()
        eventType.text.clear()
        eventDescription.text.clear()
        numOfApplicantsNeeded.text.clear()
        occurenceSetTime.text = ""
        occurenceSetDate.text = ""
        imageUrl.text = ""
        selectedImage = null

    }

}






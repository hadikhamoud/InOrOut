package com.example.inorout

import android.content.Intent
import android.media.metrics.Event
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.inorout.ui.theme.InOrOutTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class InsideEventActivity : ComponentActivity() {
    var givenEventId: Int? = null
    lateinit var givenEvent: EventCard
    lateinit var responseManager: ResponseManager
    private lateinit var sessionAuthenticator: SessionAuthenticator
    val service = retrofit.create(SimpleService::class.java)
    var dialogValue: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        responseManager = ResponseManager(this)

        val intent = intent
        givenEventId = intent.getIntExtra("extra_Event_Id",R.drawable.loading)
        Log.e("given event inside inside events actvitiy",givenEventId.toString())

        sessionAuthenticator = SessionAuthenticator(this)
        getEvent()


    }


    @Preview(showBackground = true)
    @Composable
    fun titleWithCardAll() {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {


            ImageCard(
                rememberAsyncImagePainter(givenEvent.images?.get(0)?.url),
                contentDescription = null,
                title = null
            )
        }
    }


    @Composable
    fun eventOwner() {
        Row {
            Image(
                painter = painterResource(id = R.drawable.person_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .offset((-1).dp, y = 5.dp)
            )

            (if(givenEvent.userFullName==null) "Francois" else givenEvent.userFullName)?.let {
                Text(
                    text = it,
                    modifier = Modifier.offset((-5).dp, 8.dp),

                    )
            }


        }
    }


    @Composable
    fun occurrenceDateAndTime() {
        Row {
            Image(
                painter = painterResource(id = R.drawable.calendar_today_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .offset((-1).dp, y = 5.dp)
            )

            (if(givenEvent.occurence==null) "Soon!" else (givenEvent!!.occurenceFormat(true)))?.let {
                Text(
                    text = it,
                    modifier = Modifier.offset((-5).dp, 8.dp),
                    fontFamily = FontFamily(Font(R.font.sfproregular)),

                    )
            }


        }
    }

    @Composable
    fun description() {
        Text(
            text = givenEvent.description,
            maxLines = 4,
            color = Color.Gray,
            fontFamily = FontFamily(Font(R.font.sfproregular)),
            modifier = Modifier.padding(vertical = 10.dp)

        )
    }

    @Composable
    fun ImageCard(
        painter: Painter,
        contentDescription: String?,
        title: String?,
        modifier: Modifier = Modifier

    ) {
        val showDialog =  remember { mutableStateOf(false) }

        if(showDialog.value)
            CustomDialog(value = "", setShowDialog = {
                showDialog.value = it
            }) {
                Log.i("HomePage","HomePage : $it")
            }



        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clickable { },
            elevation = 10.dp,
            shape = RoundedCornerShape(20.dp),
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(textAlign = TextAlign.Center,
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = FontFamily(Font(R.font.sfprosemibold)),
                                fontSize = 20.sp,
                                color = Color(0xFF96EAC2)
                            )
                        ) {
                            append(givenEvent.type)
                        }
                    }
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = FontFamily(Font(R.font.sfprosemibold)),
                                fontSize = 30.sp,
                                color = Color(0xFFFF6B00)
                            )
                        ) {
                            append(givenEvent.name)
                        }
                    },
                    modifier = Modifier.offset(y = (-10).dp)
                )

                Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(400.dp)

                )


                occurrenceDateAndTime()
                eventOwner()
                description()

                Row{
                    Button(onClick = {
                        showDialog.value = true
                    }, modifier = Modifier.padding(horizontal = 20.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                    ) {
                        Text(text = "write description",
                            fontFamily = FontFamily(Font(R.font.sfproregular)),
                            color = Color.White

                        )
                    }

                    Button(onClick = {
                        dialogValue?.let {
                            sendInterest(it)
                        }
                    },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                    ) {
                        Text(text = "Apply",
                            fontFamily = FontFamily(Font(R.font.sfproregular)),
                            color = Color.White

                        )
                    }
                }



            }
        }


    }

    @Composable
    fun CustomDialog(value: String, setShowDialog: (Boolean) -> Unit, setValue: (String) -> Unit) {
        val txtFieldError = remember { mutableStateOf("") }
        val txtField = remember { mutableStateOf(value) }

        Dialog(onDismissRequest = { setShowDialog(false) }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Why should we select you?",
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontFamily = FontFamily(Font(R.font.sfproregular)),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        TextField(
                            singleLine  = false,
                            modifier = Modifier
                                .fillMaxWidth(),
//                                .border(
//                                    BorderStroke(
//                                        width = 2.dp,
//                                        color = colorResource(id = if (txtFieldError.value.isEmpty()) android.R.color.holo_green_light else android.R.color.holo_red_dark)
//                                    ),
                                    shape = RoundedCornerShape(50),

                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,

                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            placeholder = { Text(text = "Enter Description") },
                            value = txtField.value,
                            onValueChange = {
                                txtField.value = it.take(300)
                            })

                        Spacer(modifier = Modifier.height(20.dp))

                        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                            Button(
                                onClick = {
                                    if (txtField.value.isEmpty()) {
                                        txtFieldError.value = "you have to fill out field"
                                        return@Button
                                    }
                                    setValue(txtField.value)
                                    dialogValue = txtField.value
                                    setShowDialog(false)
                                },
                                shape = RoundedCornerShape(50.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)

                            ) {
                                Text(text = "Done",
                                    fontFamily = FontFamily(Font(R.font.sfproregular)),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

    }
    fun getEvent(){

  CoroutineScope(Dispatchers.IO).launch {
            val response = service.getCommunityEvent(token = "Bearer ${sessionAuthenticator.fetchAccessToken()}",givenEventId)
            withContext(Dispatchers.Main){
                givenEvent = response
                createUi()
            }
        }

    }

    fun createUi(){
        setContent {
            InOrOutTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    titleWithCardAll()
                }
            }
        }

    }

    fun sendInterest(userEventDescription: String){
        var interestedApplicant = InterestedApplicant(
            givenEventId,
            userEventDescription
        )

            CoroutineScope(Dispatchers.IO).launch {
                val response = service.applicantinterest(token = "Bearer ${sessionAuthenticator.fetchAccessToken()}",interestedApplicant)
                withContext(Dispatchers.Main){
                    if(response.isSuccessful){
                        Toast.makeText(applicationContext, "Successful!", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@InsideEventActivity,InsideAppActivity::class.java)
                        startActivity(intent)
                        finish()


                    }
                    else{
                        var error = responseManager.getResponseErrorDetails(response.errorBody())
                        Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()

                    }
                }
            }


    }


}

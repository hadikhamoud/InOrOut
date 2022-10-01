package com.example.inorout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Surface
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class InsideEventFragment : Fragment() {
    var fragmentView: View? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        if (fragmentView != null) {
            return fragmentView;
        }
        val view = inflater.inflate(
            R.layout.fragment_inside_event, container, false
        )
        view.findViewById<ComposeView>(R.id.compose_view).setContent {
//           titleWithCard()
        }
        fragmentView = view
        return view
    }

}




//@Composable
//fun Card(
//    modifier: Modifier = Modifier,
//    enabled: Boolean = true,
//    shape: Shape = MaterialTheme.shapes.medium,
//    backgroundColor: Color = MaterialTheme.colors.surface,
//    contentColor: Color = contentColorFor(backgroundColor),
//    border: BorderStroke? = null,
//    elevation: Dp = 1.dp,
//    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
//    content: @Composable () -> Unit
//): Unit{}
//

//@Composable
//fun CardDemo() {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .fillMaxHeight()
//            .padding(15.dp)
//            .clickable { },
//        elevation = 10.dp,
//        shape = MaterialTheme.shapes.large,
//    ) {
//        Column(
//            modifier = Modifier.padding(4.dp)
//        ) {
//            Text(
//                buildAnnotatedString {
//                    append("welcome to ")
//                    withStyle(style = SpanStyle(fontFamily = FontFamily(Font(R.font.sfproregular)), fontWeight = FontWeight.W900, color = Color(0xFF4552B8))
//                    ) {
//                        append("Jetpack Compose Playground")
//                    }
//                }
//            )
//            Text(
//                buildAnnotatedString {
//                    append("Now you are in the ")
//                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)) {
//                        append("Card")
//                    }
//                    append(" section")
//                }
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun titleWithCard(){
//    Column (
//        modifier = Modifier.fillMaxSize()){
//
//        Text(
//            text = "Events",
//            fontSize = 60.sp,
//            fontFamily = FontFamily(Font(R.font.sfproheavy)),
//            modifier = Modifier
//                .fillMaxWidth()
//                .wrapContentWidth(Alignment.CenterHorizontally)
//                .padding(top = 30.dp)
//
//
//        )
//
//        ImageCard(
//            painterResource(id = R.drawable.calendar_today_foreground),
//            contentDescription = null,
//            title = null
//        )
//    }
//}
//
//
//@Composable
//fun occurrenceDateAndTime(){
//    Row{
//        Image(
//            painter = painterResource(id = R.drawable.calendar_today_foreground),
//            contentDescription = null
//        )
//
//    }
//}
//
//@Composable
//fun ImageCard(
//    painter: Painter,
//    contentDescription: String?,
//    title: String?,
//    modifier: Modifier = Modifier
//    )
//    {
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight()
//                .padding(15.dp)
//                .clickable { },
//            elevation = 10.dp,
//            shape = MaterialTheme.shapes.large,
//        ) {
//            Column(
//                modifier = Modifier.padding(15.dp)
//            ) {
//                Image(
//                    painter = painter,
//                    contentDescription = contentDescription,
//                    contentScale = ContentScale.Fit,
//                )
//
//                    Text(textAlign = TextAlign.Center,
//                        text = buildAnnotatedString {
//                            withStyle(
//                                style = SpanStyle(
//                                    fontFamily = FontFamily(Font(R.font.sfproregular)),
//                                    fontSize = 20.sp,
//                                    color = Color(0xFF96EAC2)
//                                )
//                            ) {
//                                append("Community Event type")
//                            }
//                        }
//                    )
//                    Text(
//                        text = buildAnnotatedString {
//                            withStyle(
//                                style = SpanStyle(
//                                    fontFamily = FontFamily(Font(R.font.sfproregular)),
//                                    fontSize = 25.sp,
//                                    color = Color(0xFFFF6B00)
//                                )
//                            ) {
//                                append("Community Event Title")
//                            }
//                        }
//                    )
//                occurrenceDateAndTime()
//
//                }
//            }
//        }

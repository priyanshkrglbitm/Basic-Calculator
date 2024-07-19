package com.example.basiccalculator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val buttonList= listOf(
    "AC","( )","%","÷",
    "7","8","9","x",
    "4","5","6","-",
    "1","2","3","+",
    "0",".","clr","=",
)

@Composable
fun Calculator(modifier: Modifier =Modifier , viewModel: CalculatorViewModel){

    val equationText =  viewModel.equationText.observeAsState()
    val resultText =  viewModel.resultText.observeAsState()

    Box(modifier = Modifier){
        Column(
            modifier =Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End
        ){
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text =equationText.value?:"",
                style= TextStyle(
                    fontSize = 45.sp,
                    textAlign = TextAlign.End
                ),
                maxLines=5,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier=Modifier.weight(1f))

            Text(
                text =resultText.value?:"0",
                style= TextStyle(
                    fontSize = 70.sp,
                    textAlign = TextAlign.End
                ),
                maxLines=1,
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(4)
            ){
                items(buttonList){
                    CalculatorButton(btn=it , onClick = {
                        viewModel.onButtonClick(it)
                    })
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun CalculatorButton(btn: String , onClick : ()->Unit) {
    Box(modifier = Modifier.padding(8.dp)){
        FloatingActionButton(
            onClick = onClick ,
            modifier=Modifier.size(80.dp),
            shape= CircleShape,
            contentColor = androidx.compose.ui.graphics.Color.White,
            containerColor = getColor(btn)
            ) {
            Text(text = btn , fontSize = 25.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun getColor(button: String): Color {
    return when (button) {
        "AC" -> Color(0xFFFF0000)
        "%", "( )", "÷", "x", "-", "+", "=" -> Color(0xFF595959) // Grey for operators
        else -> Color(0XFF302e2e)
    }
}




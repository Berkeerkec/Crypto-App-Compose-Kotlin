package com.berkesoft.retrofitcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.berkesoft.retrofitcompose.model.CryptoModel
import com.berkesoft.retrofitcompose.service.CryptoApi
import com.berkesoft.retrofitcompose.ui.theme.RetrofitComposeTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    val BaseUrl = "https://raw.githubusercontent.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RetrofitComposeTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(){

    var cryptoModels = remember {
        mutableStateListOf<CryptoModel>()
    }
    val BaseUrl = "https://raw.githubusercontent.com/"


    val retrofit = Retrofit.Builder()
        .baseUrl(BaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CryptoApi::class.java)

    val call = retrofit.getData()

    call.enqueue(object : Callback<List<CryptoModel>>{
        override fun onResponse(
            call: Call<List<CryptoModel>>,
            response: Response<List<CryptoModel>>
        ) {
            if (response.isSuccessful){
                response.body()?.let {
                    //List
                    cryptoModels.addAll(it)
                }
            }
        }
        override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
            t.printStackTrace()
        }

    })

    Scaffold(topBar = {AppBar()}) {
        CryptoList(cryptos = cryptoModels)
    }
}

@Composable
fun AppBar(){
    TopAppBar(contentPadding = PaddingValues(10.dp)) {
        Text(text = "Retrofit Compose", fontSize = 26.sp)
    }
}


@Composable
fun CryptoList(cryptos : List<CryptoModel>){

    LazyColumn(contentPadding = PaddingValues(5.dp)){
        items(cryptos){ crypto ->
        CrytoRow(crypto = crypto)
        }
    }
}


@Composable
fun CrytoRow(crypto : CryptoModel){

    Column(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colors.surface)) {
        Text(text = crypto.currency
            , style = MaterialTheme.typography.h4
            , modifier = Modifier.padding(2.dp)
            , fontWeight = FontWeight.Bold)

        Text(text = crypto.price
            , modifier = Modifier.padding(2.dp)
            , style = MaterialTheme.typography.h5)
    }

}





@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RetrofitComposeTheme {
        CrytoRow(crypto = CryptoModel("BerkeCoin", "361"))
    }
}
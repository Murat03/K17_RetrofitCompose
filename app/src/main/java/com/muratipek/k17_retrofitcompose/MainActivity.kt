package com.muratipek.k17_retrofitcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muratipek.k17_retrofitcompose.model.CryptoModel
import com.muratipek.k17_retrofitcompose.service.CryptoAPI
import com.muratipek.k17_retrofitcompose.ui.theme.K17_RetrofitComposeTheme
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            K17_RetrofitComposeTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {

    var cryptoModels = remember{ mutableStateListOf <CryptoModel>() }

    val BASE_URL = "https://raw.githubusercontent.com/"

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CryptoAPI::class.java)
    val call = retrofit.getData()

    call.enqueue(object : Callback<List<CryptoModel>>{
        override fun onResponse(
            call: Call<List<CryptoModel>>,
            response: Response<List<CryptoModel>>
        ) {
            if(response.isSuccessful){
                response.body()?.let{
                    cryptoModels.addAll(it)
                }
            }
        }

        override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
            t.printStackTrace()
        }

    })

    Scaffold(topBar = {SpecialTopAppBar()}) { padding ->
        CryptoList(cryptos = cryptoModels)
    }
}
@Composable
fun SpecialTopAppBar(){
    TopAppBar(contentPadding = PaddingValues(10.dp)) {
        Text(text = "Retrofit Compose", fontSize = 26.sp)
    }
}
@Composable
fun CryptoList(cryptos: List<CryptoModel>){
    LazyColumn(contentPadding = PaddingValues(5.dp)){
        items(cryptos){ crypto ->
        CryptoRow(crypto = crypto)
        }
    }
}
@Composable
fun CryptoRow(crypto: CryptoModel){
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colors.surface)) {
        Text(text = crypto.currency,
        style = MaterialTheme.typography.h4,
        modifier = Modifier.padding(2.dp),
            fontWeight = FontWeight.Bold
        )
        Text(text = crypto.price,
        style = MaterialTheme.typography.h5,
        modifier = Modifier.padding(2.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    K17_RetrofitComposeTheme {
        CryptoRow(crypto = CryptoModel("BTC", "32453"))
    //MainScreen()
    }
}
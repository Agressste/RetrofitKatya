package com.example.retrofitlesson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.example.retrofitlesson.databinding.ActivityMainBinding
import com.example.retrofitlesson.retrofit.AuthRequest
import com.example.retrofitlesson.retrofit.MainApi
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val mainApi = retrofit.create(MainApi::class.java)

        binding.button.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val user = mainApi.auth(
                    AuthRequest(
                        binding.username.text.toString(), //emilys
                        binding.password.text.toString()  //emilyspass
                    )
                )
                runOnUiThread {
                    binding.apply {
                        Picasso.get().load(user.image).into(im)
                        firstName.text = user.firstName
                        lastName.text = user.lastName
                    }
                }
            }
        }

    }
}
package com.example.progothon



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.progothon.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var name:String
        binding.eunoia.setOnClickListener {
            name=binding.tveu.text.toString()
            val intent= Intent(this,Eunoia::class.java)
            intent.putExtra("key",name)
            startActivity(intent)
        }
        binding.epmoc.setOnClickListener {
            name=binding.tvep.text.toString()
            val intent= Intent(this,epmoc::class.java)
            intent.putExtra("key",name)
            startActivity(intent)
        }
        binding.aavesh.setOnClickListener {
            name=binding.tvav.text.toString()
            val intent= Intent(this,aavesh::class.java)
            intent.putExtra("key",name)
            startActivity(intent)
        }
        binding.zenith.setOnClickListener {
            name=binding.tvze.text.toString()
            val intent= Intent(this,Zenith::class.java)
            intent.putExtra("key",name)
            startActivity(intent)
        }
        binding.force.setOnClickListener {
            name=binding.tvfo.text.toString()
            val intent= Intent(this,Force::class.java)
            intent.putExtra("key",name)
            startActivity(intent)
        }
        binding.pixcel.setOnClickListener {
            name=binding.tvpi.text.toString()
            val intent= Intent(this,Pixcel::class.java)
            intent.putExtra("key",name)
            startActivity(intent)
        }

    }
}
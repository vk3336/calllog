package com.example.calllogger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calllogger.databinding.ActivitySettingsBinding
import com.example.calllogger.util.ConfigManager

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var configManager: ConfigManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        configManager = ConfigManager.getInstance(this)
        
        setupUI()
        loadSettings()
    }
    
    private fun setupUI() {
        binding.buttonSave.setOnClickListener {
            saveSettings()
        }
    }
    
    private fun loadSettings() {
        binding.editTextSim1Name.setText(configManager.sim1Name ?: "Personal SIM")
        binding.editTextSim2Name.setText(configManager.sim2Name ?: "Work SIM")
    }
    
    private fun saveSettings() {
        configManager.sim1Name = binding.editTextSim1Name.text.toString().trim()
        configManager.sim2Name = binding.editTextSim2Name.text.toString().trim()
        
        // Show success message and finish
        finish()
    }
}
package com.plcoding.qonversionprep

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.plcoding.qonversionprep.ui.theme.QonversionPrepTheme
import com.qonversion.android.sdk.Qonversion
import com.qonversion.android.sdk.QonversionError
import com.qonversion.android.sdk.QonversionOfferingsCallback
import com.qonversion.android.sdk.QonversionPermissionsCallback
import com.qonversion.android.sdk.dto.QPermission
import com.qonversion.android.sdk.dto.offerings.QOfferings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QonversionPrepTheme {
                val viewModel: QonversionViewModel = viewModel()
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        if (viewModel.hasPremiumPermission) {
                            item {
                                Text(
                                    text = "Yaay, you got premium access!",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Green)
                                        .padding(16.dp)
                                )
                            }
                        }
                        items(viewModel.offerings) { offering ->
                            Text(
                                text = offering.offeringID,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        Qonversion.purchase(
                                            this@MainActivity,
                                            offering.products.firstOrNull() ?: return@clickable,
                                            object : QonversionPermissionsCallback {
                                                override fun onError(error: QonversionError) {
                                                    Toast
                                                        .makeText(
                                                            this@MainActivity,
                                                            "Purchase failed: ${error.description}, ${error.additionalMessage}",
                                                            Toast.LENGTH_LONG
                                                        )
                                                        .show()
                                                }

                                                override fun onSuccess(permissions: Map<String, QPermission>) {
                                                    Toast
                                                        .makeText(
                                                            this@MainActivity,
                                                            "Purchase successful",
                                                            Toast.LENGTH_LONG
                                                        )
                                                        .show()
                                                    viewModel.updatePermissions()
                                                }
                                            }
                                        )
                                    }
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
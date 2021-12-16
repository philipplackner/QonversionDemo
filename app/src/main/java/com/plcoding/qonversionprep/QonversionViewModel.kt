package com.plcoding.qonversionprep

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.qonversion.android.sdk.Qonversion
import com.qonversion.android.sdk.QonversionError
import com.qonversion.android.sdk.QonversionOfferingsCallback
import com.qonversion.android.sdk.QonversionPermissionsCallback
import com.qonversion.android.sdk.dto.QPermission
import com.qonversion.android.sdk.dto.offerings.QOffering
import com.qonversion.android.sdk.dto.offerings.QOfferings
import com.qonversion.android.sdk.dto.products.QProduct

class QonversionViewModel: ViewModel() {

    var offerings by mutableStateOf<List<QOffering>>(emptyList())
        private set

    var hasPremiumPermission by mutableStateOf(false)
        private set

    init {
        updatePermissions()
        loadOfferings()
    }

    private fun loadOfferings() {
        Qonversion.offerings(
            object : QonversionOfferingsCallback {
                override fun onError(error: QonversionError) {
                    // Handle error
                    Log.d(TAG, "onError: ${error.description}")
                }

                override fun onSuccess(offerings: QOfferings) {
                    this@QonversionViewModel.offerings = offerings.availableOfferings
                }
            }
        )
    }

    fun updatePermissions() {
        Qonversion.checkPermissions(object : QonversionPermissionsCallback {
            override fun onError(error: QonversionError) {
                Log.d(TAG, "onError: ${error.description}")
            }

            override fun onSuccess(permissions: Map<String, QPermission>) {
                hasPremiumPermission = permissions["Premium"]?.isActive() == true
                Log.d(TAG, "permissions: ${permissions.keys.toList()}")
            }
        })
    }

}
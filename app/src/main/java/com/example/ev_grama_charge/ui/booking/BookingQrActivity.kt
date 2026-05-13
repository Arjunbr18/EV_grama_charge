package com.example.ev_grama_charge.ui.booking

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ev_grama_charge.databinding.ActivityBookingQrBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class BookingQrActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityBookingQrBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityBookingQrBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        val bookingId =
            intent.getStringExtra(
                "bookingId"
            ) ?: "UNKNOWN"

        binding.tvBookingId.text =
            bookingId

        generateQrCode(bookingId)
    }

    private fun generateQrCode(
        text: String
    ) {

        val writer =
            QRCodeWriter()

        val bitMatrix =
            writer.encode(
                text,
                BarcodeFormat.QR_CODE,
                800,
                800
            )

        val width =
            bitMatrix.width

        val height =
            bitMatrix.height

        val bitmap =
            Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.RGB_565
            )

        for (x in 0 until width) {

            for (y in 0 until height) {

                bitmap.setPixel(

                    x,
                    y,

                    if (
                        bitMatrix.get(x, y)
                    ) {
                        android.graphics.Color.BLACK
                    } else {
                        android.graphics.Color.WHITE
                    }
                )
            }
        }

        binding.imageQr.setImageBitmap(bitmap)
    }
}
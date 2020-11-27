package com.example.ordering

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class BrowseUserRequests : AppCompatActivity() {

    companion object {
        const val EXTRA_UID = "extra_uid"
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ORIGIN_LAT = "extra_origin_lat"
        const val EXTRA_ORIGIN_LONG = "extra_origin_long"
        const val EXTRA_DESTINATION_LAT = "extra_dest_lat"
        const val EXTRA_DESTINATION_LONG = "extra_dest_long"
        const val EXTRA_ORIGIN_ADDRESS = "extra_origin_addr"
        const val EXTRA_DESTINATION_ADDRESS = "extra_dest_addr"
    }

    private lateinit var rcvRequests: RecyclerView
    // TODO: Replace the data coming from the database
    private var userData = arrayOf(
            UserRequest(
                    "test1",
                    "Jan Carlo V. Tolentino",
                    18.1960,
                    120.5927,
                    18.2704,
                    120.6085,
                    "San Nicholas, Ilocos Norte",
                    "Laoag City, Ilocos Norte"
            ),
            UserRequest(
                    "test2",
                    "Renzther James Barnido",
                    10.0,
                    11.0,
                    10.0,
                    11.0,
                    "San Nicholas, Ilocos Norte",
                    "Laoag City, Ilocos Norte"
            )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser_requests)

        rcvRequests = findViewById(R.id.rcv_requests)
        rcvRequests.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcvRequests.adapter = UserRequestsAdapter(userData)
        rcvRequests.addOnItemTouchListener(
                RecyclerItemClickListener(
                        applicationContext,
                        rcvRequests,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View?, position: Int) {
                                Log.d(localClassName, position.toString())
                                next(userData[position])
                            }

                            override fun onLongItemClick(view: View?, position: Int) {}

                        })
        )
    }

    private fun next(user: UserRequest) {
        // TODO: Replace the dialog text with something more proper
        AlertDialog.Builder(this)
                .setTitle("Confirm for accepting the client")
                .setMessage("You're about to accept the request of the client. This action is irrevocable.")
                .setIcon(R.drawable.ic_baseline_confirmation_number_24)
                .setPositiveButton(
                        "Accept Request"
                ) { _, _ ->
                    // TODO: Update the database to tell the client it has been accepted by a rider
                    val intent =
                            Intent(applicationContext, OnRideDutyActivity::class.java).apply {
                                putExtra(EXTRA_UID, user.uid)
                                putExtra(EXTRA_USERNAME, user.username)
                                putExtra(EXTRA_ORIGIN_ADDRESS, user.originAddress)
                                putExtra(EXTRA_DESTINATION_ADDRESS, user.destAddress)
                                putExtra(EXTRA_ORIGIN_LAT, user.originLat)
                                putExtra(EXTRA_ORIGIN_LONG, user.originLong)
                                putExtra(EXTRA_DESTINATION_LAT, user.destLat)
                                putExtra(EXTRA_DESTINATION_LONG, user.destLong)
                            }
                    startActivity(intent)
                }
                .setNegativeButton("Cancel", null)
                .show()
    }
}
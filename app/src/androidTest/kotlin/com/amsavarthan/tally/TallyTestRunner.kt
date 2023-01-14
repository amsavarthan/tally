package com.amsavarthan.tally

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

internal class TallyTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?,
    ): Application {
        return super.newApplication(
            /* cl = */ cl,
            /* className = */ HiltTestApplication::class.java.name,
            /* context = */ context
        )
    }

}
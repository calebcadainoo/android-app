/*
 * Copyright (c) 2020 Proton Technologies AG
 *
 * This file is part of ProtonVPN.
 *
 * ProtonVPN is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProtonVPN is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProtonVPN.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.protonvpn.android.api

import android.os.Build
import com.protonvpn.android.BuildConfig
import com.protonvpn.android.bus.EventBus
import com.protonvpn.android.bus.ForcedLogout
import com.protonvpn.android.vpn.VpnStateMonitor
import me.proton.core.network.domain.ApiClient
import java.util.Locale

class VpnApiClient : ApiClient {

    private var vpnStateMonitor: VpnStateMonitor? = null

    fun init(monitor: VpnStateMonitor) {
        vpnStateMonitor = monitor
    }

    override val appVersionHeader = "AndroidVPN_" + BuildConfig.VERSION_NAME + BuildConfig.STORE_SUFFIX
    override val enableDebugLogging = BuildConfig.DEBUG
    override val shouldUseDoh = vpnStateMonitor?.isConnected != true

    override val userAgent: String
        get() = String.format(Locale.US, "ProtonVPN/%s (Android %s; %s %s)",
                BuildConfig.VERSION_NAME, Build.VERSION.RELEASE, Build.BRAND, Build.MODEL)

    override fun forceUpdate() {
        EventBus.postOnMain(ForcedLogout.INSTANCE)
    }
}
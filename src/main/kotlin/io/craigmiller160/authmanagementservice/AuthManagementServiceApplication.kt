/*
 *     Auth Management Service
 *     Copyright (C) 2020 Craig Miller
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.craigmiller160.authmanagementservice

import io.craigmiller160.webutils.tls.TlsConfigurer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuthManagementServiceApplication

private const val TRUST_STORE_TYPE = "JKS"
private const val TRUST_STORE_PATH = "truststore.jks"
private const val TRUST_STORE_PASSWORD = "changeit"

fun main(args: Array<String>) {
	TlsConfigurer.configureTlsTrustStore(TRUST_STORE_PATH, TRUST_STORE_TYPE, TRUST_STORE_PASSWORD)
	runApplication<AuthManagementServiceApplication>(*args)
}

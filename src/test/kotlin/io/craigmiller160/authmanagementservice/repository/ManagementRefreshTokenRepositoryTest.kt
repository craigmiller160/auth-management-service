package io.craigmiller160.authmanagementservice.repository

import io.craigmiller160.authmanagementservice.entity.ManagementRefreshToken
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class ManagementRefreshTokenRepositoryTest {

    @Autowired
    private lateinit var manageRefreshTokenRepo: ManagementRefreshTokenRepository

    private lateinit var refreshToken1: ManagementRefreshToken
    private lateinit var refreshToken2: ManagementRefreshToken

    @BeforeEach
    fun setup() {
        refreshToken1 = ManagementRefreshToken(
                0, "1", "token1"
        )
        refreshToken1 = manageRefreshTokenRepo.save(refreshToken1)

        refreshToken2 = ManagementRefreshToken(
                0, "2", "token2"
        )
        refreshToken2 = manageRefreshTokenRepo.save(refreshToken2)
    }

    @AfterEach
    fun clean() {
        manageRefreshTokenRepo.deleteAll()
    }

    @Test
    fun test_findByTokenId() {
        val result = manageRefreshTokenRepo.findByTokenId("1")
        assertEquals(refreshToken1, result)
    }

    @Test
    fun test_removeByTokenId() {
        val result = manageRefreshTokenRepo.removeByTokenId("1")
        assertEquals(1, result)

        val results = manageRefreshTokenRepo.findAll()
        assertEquals(1, results.size)
        assertEquals(refreshToken2, results[0])
    }

}

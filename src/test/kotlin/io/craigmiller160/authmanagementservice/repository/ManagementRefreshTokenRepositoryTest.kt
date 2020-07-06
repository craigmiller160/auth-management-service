package io.craigmiller160.authmanagementservice.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class ManagementRefreshTokenRepositoryTest {

    @Autowired
    private lateinit var manageRefreshTokenRepo: ManagementRefreshTokenRepository

    @Test
    fun test_findByTokenId() {
        TODO("Finish this")
    }

    @Test
    fun test_removeByTokenId() {
        TODO("Finish this")
    }

}

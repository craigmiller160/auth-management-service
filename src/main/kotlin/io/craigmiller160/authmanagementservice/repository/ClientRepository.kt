package io.craigmiller160.authmanagementservice.repository

import io.craigmiller160.authmanagementservice.entity.Client
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository : JpaRepository<Client,Long>
package com.controlcv.backendkotlin.repositories

import com.controlcv.backendkotlin.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Int> {
    fun findByEmail(email: String): User?
}
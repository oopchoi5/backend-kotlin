package com.controlcv.backendkotlin.services

import com.controlcv.backendkotlin.models.User
import com.controlcv.backendkotlin.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun save(user: User): User{
        return this.userRepository.save(user)
    }
}
package de.neuefische.backend.user

import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepo : MongoRepository<User, String> {
    fun findByName(name: String): User?
}

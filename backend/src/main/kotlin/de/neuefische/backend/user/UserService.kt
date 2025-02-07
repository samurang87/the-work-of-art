package de.neuefische.backend.user

import de.neuefische.backend.common.toMedium
import de.neuefische.backend.exceptions.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepo: UserRepo,
) {
    fun getUserByName(name: String): UserProfileResponse? =
        userRepo.findByName(name)?.let { user ->
            UserProfileResponse(
                id = user.id.value.toString(),
                name = user.name,
                bio = user.bio,
                imageUrl = user.imageUrl,
                mediums = user.mediums?.map { it.lowercase } ?: emptyList(),
            )
        }

    fun getUserById(id: String): UserProfileResponse? =
        userRepo.findByIdOrNull(id)?.let { user ->
            UserProfileResponse(
                id = user.id.value.toString(),
                name = user.name,
                bio = user.bio,
                imageUrl = user.imageUrl,
                mediums = user.mediums?.map { it.lowercase } ?: emptyList(),
            )
        }

    private fun createUser(oauthUsername: String): User {
        val user =
            User(
                name = oauthUsername,
                bio = "",
                imageUrl = "",
                mediums = emptyList(),
            )
        return userRepo.save(user)
    }

    fun findOrCreateUser(oauthUsername: String): User = userRepo.findByName(oauthUsername) ?: createUser(oauthUsername)

    fun updateUser(
        userId: String,
        req: UserProfileUpdateRequest,
    ): User {
        val user =
            userRepo.findByIdOrNull(userId)
                ?: throw NotFoundException("User $userId not found")

        val updatedUser =
            user.copy(
                bio = req.bio,
                imageUrl = req.imageUrl,
                mediums = req.mediums.mapNotNull { it.toMedium() },
            )

        return userRepo.save(updatedUser)
    }
}

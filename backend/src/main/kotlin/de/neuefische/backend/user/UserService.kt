package de.neuefische.backend.user

import org.springframework.stereotype.Service

@Service
class UserService(private val userRepo: UserRepo) {

    fun getUserByName(name: String): UserProfileResponse? {
        return userRepo.findByName(name)?.let { user ->
            UserProfileResponse(
                id = user.id.value.toString(),
                name = user.name,
                bio = user.bio,
                imageUrl = user.imageUrl,
                mediums = user.mediums?.map { it.lowercase } ?: emptyList()
            )
        }
    }

}

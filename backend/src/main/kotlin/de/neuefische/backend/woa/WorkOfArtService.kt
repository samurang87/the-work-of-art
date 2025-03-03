package de.neuefische.backend.woa

import de.neuefische.backend.common.Medium
import de.neuefische.backend.common.toMedium
import org.bson.BsonObjectId
import org.bson.types.ObjectId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service

@Service
class WorkOfArtService(
    private val workOfArtRepo: WorkOfArtRepo,
) {
    private fun workOfArtResponse(woa: WorkOfArt): WorkOfArtResponse =
        WorkOfArtResponse(
            id = woa.id.value.toString(),
            user = woa.user.value.toString(),
            userName = woa.userName,
            challengeId = woa.challengeId?.value?.toString(),
            title = woa.title,
            description = woa.description,
            imageUrl = woa.imageUrl,
            medium = woa.medium.lowercase,
            materials =
                woa.materials?.map { material ->
                    MaterialDAO(
                        name = material.name,
                        identifier = material.identifier,
                        brand = material.brand,
                        line = material.line,
                        type = material.type,
                        medium = material.medium?.lowercase,
                    )
                } ?: emptyList(),
            createdAt = woa.createdAt.toString(),
        )

    private fun workOfArtShortResponses(worksOfArt: List<WorkOfArt>) =
        worksOfArt
            .sortedByDescending { it.createdAt }
            .map { workOfArt ->
                WorkOfArtShortResponse(
                    id = workOfArt.id.value.toString(),
                    user = workOfArt.user.value.toString(),
                    userName = workOfArt.userName,
                    title = workOfArt.title,
                    imageUrl = workOfArt.imageUrl,
                    medium = workOfArt.medium.lowercase,
                    createdAt = workOfArt.createdAt.toString(),
                )
            }

    fun getWorkOfArtById(id: String): WorkOfArtResponse? =
        workOfArtRepo.findByIdOrNull(id)?.let { workOfArt ->
            workOfArtResponse(workOfArt)
        }

    fun getAllWorksOfArt(mediums: List<Medium>? = null): List<WorkOfArtShortResponse> {
        val worksOfArt =
            if (mediums.isNullOrEmpty()) {
                workOfArtRepo.findAll()
            } else {
                workOfArtRepo.findAllByMediumIn(mediums)
            }
        return workOfArtShortResponses(worksOfArt)
    }

    fun getAllWorksOfArtByUser(user: String): List<WorkOfArtShortResponse> {
        val worksOfArt = workOfArtRepo.findAllByUser(BsonObjectId(ObjectId(user)))
        return workOfArtShortResponses(worksOfArt)
    }

    private fun constructWorkOfArt(
        user: String,
        userName: String,
        challengeId: String?,
        title: String,
        description: String,
        imageUrl: String,
        medium: String,
        materials: List<MaterialDAO>,
    ): WorkOfArt =
        WorkOfArt(
            user = BsonObjectId(ObjectId(user)),
            userName = userName,
            challengeId = challengeId?.let { BsonObjectId(ObjectId(it)) },
            title = title,
            description = description,
            imageUrl = imageUrl,
            medium =
                medium.toMedium()
                    ?: throw IllegalArgumentException("Medium is unavailable"),
            materials =
                materials.map { material ->
                    Material(
                        name = material.name,
                        identifier = material.identifier,
                        brand = material.brand,
                        line = material.line,
                        type = material.type,
                        medium =
                            medium.toMedium()
                                ?: throw IllegalArgumentException("Medium is unavailable"),
                    )
                },
        )

    fun createWorkOfArt(request: WorkOfArtCreateOrUpdateRequest): WorkOfArtResponse {
        val workOfArt =
            constructWorkOfArt(
                user = request.user,
                userName = request.userName,
                challengeId = request.challengeId,
                title = request.title,
                description = request.description ?: "",
                imageUrl = request.imageUrl,
                medium = request.medium,
                materials = request.materials,
            )
        val savedWorkOfArt = workOfArtRepo.save(workOfArt)
        return workOfArtResponse(savedWorkOfArt)
    }

    fun updateWorkOfArt(
        id: String,
        request: WorkOfArtCreateOrUpdateRequest,
        currentUser: String,
    ): WorkOfArtResponse {
        val existingWorkOfArt =
            workOfArtRepo.findByIdOrNull(id)
                ?: throw IllegalArgumentException("Work of Art not found")

        if (currentUser != existingWorkOfArt.userName) {
            throw AccessDeniedException("You can update only your own work of art")
        }

        val updatedWorkOfArt =
            constructWorkOfArt(
                user = request.user,
                userName = request.userName,
                challengeId = request.challengeId,
                title = request.title,
                description = request.description ?: "",
                imageUrl = request.imageUrl,
                medium = request.medium,
                materials = request.materials,
            ).copy(id = existingWorkOfArt.id)

        val savedWorkOfArt = workOfArtRepo.save(updatedWorkOfArt)
        return workOfArtResponse(savedWorkOfArt)
    }

    fun deleteWorkOfArt(
        id: String,
        currentUser: String,
    ): String {
        val existingWorkOfArt =
            workOfArtRepo.findByIdOrNull(id)
                ?: throw IllegalArgumentException("Work of Art not found")

        if (currentUser != existingWorkOfArt.userName) {
            throw AccessDeniedException("You can delete only your own work of art")
        }

        workOfArtRepo.deleteById(id)
        return id
    }
}

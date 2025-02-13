package de.neuefische.backend.woa

import de.neuefische.backend.common.Medium
import de.neuefische.backend.common.toMedium
import org.bson.BsonObjectId
import org.bson.types.ObjectId
import org.springframework.data.repository.findByIdOrNull
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
        return worksOfArt
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
    }

    fun createWorkOfArt(request: WorkOfArtCreateOrUpdateRequest): WorkOfArtResponse {
        val workOfArt =
            WorkOfArt(
                user = BsonObjectId(ObjectId(request.user)),
                userName = request.userName,
                challengeId = request.challengeId?.let { BsonObjectId(ObjectId(it)) },
                title = request.title,
                description = request.description,
                imageUrl = request.imageUrl,
                medium =
                    request.medium.toMedium()
                        ?: throw IllegalArgumentException("Medium is unavailable"),
                materials =
                    request.materials.map { material ->
                        Material(
                            name = material.name,
                            identifier = material.identifier,
                            brand = material.brand,
                            line = material.line,
                            type = material.type,
                            medium = material.medium?.let { Medium.valueOf(it.uppercase()) },
                        )
                    },
            )
        val savedWorkOfArt = workOfArtRepo.save(workOfArt)
        return workOfArtResponse(savedWorkOfArt)
    }

    fun updateWorkOfArt(
        id: String,
        request: WorkOfArtCreateOrUpdateRequest,
    ): WorkOfArtResponse {
        val existingWorkOfArt =
            workOfArtRepo.findByIdOrNull(id)
                ?: throw IllegalArgumentException("Work of Art not found")

        val updatedWorkOfArt =
            existingWorkOfArt.copy(
                challengeId = request.challengeId?.let { BsonObjectId(ObjectId(it)) },
                title = request.title,
                description = request.description,
                imageUrl = request.imageUrl,
                medium =
                    request.medium.toMedium()
                        ?: throw IllegalArgumentException("Medium is unavailable"),
                materials =
                    request.materials.map { material ->
                        Material(
                            name = material.name,
                            identifier = material.identifier,
                            brand = material.brand,
                            line = material.line,
                            type = material.type,
                            medium = material.medium?.let { Medium.valueOf(it.uppercase()) },
                        )
                    },
            )

        val savedWorkOfArt = workOfArtRepo.save(updatedWorkOfArt)
        return workOfArtResponse(savedWorkOfArt)
    }
}

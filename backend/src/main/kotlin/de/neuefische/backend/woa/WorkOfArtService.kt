package de.neuefische.backend.woa

import de.neuefische.backend.common.Medium
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class WorkOfArtService(private val workOfArtRepo: WorkOfArtRepo) {

    fun getWorkOfArtById(id: String): WorkOfArtResponse? {

        return workOfArtRepo.findByIdOrNull(id)?.let { workOfArt ->
            WorkOfArtResponse(
                id = workOfArt.id.value.toString(),
                user = workOfArt.user.value.toString(),
                challengeId = workOfArt.challengeId?.value?.toString(),
                title = workOfArt.title,
                description = workOfArt.description,
                imageUrl = workOfArt.imageUrl,
                medium = workOfArt.medium.lowercase,
                materials = workOfArt.materials?.map { material ->
                    MaterialResponse(
                        name = material.name,
                        identifier = material.identifier,
                        brand = material.brand,
                        line = material.line,
                        type = material.type,
                        medium = material.medium?.lowercase,
                    )
                } ?: emptyList(),
                createdAt = workOfArt.createdAt.toString(),
            )
        }
    }

    fun getAllWorksOfArt(mediums: List<Medium>? = null): List<WorkOfArtShortResponse> {
        val worksOfArt = if (mediums.isNullOrEmpty()) {
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
                title = workOfArt.title,
                imageUrl = workOfArt.imageUrl,
                medium = workOfArt.medium.lowercase,
                createdAt = workOfArt.createdAt.toString(),
            )
        }
    }
}

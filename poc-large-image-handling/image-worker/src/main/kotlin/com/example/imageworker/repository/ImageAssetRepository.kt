package com.example.imageworker.repository

import com.example.common.domain.ImageAsset
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ImageAssetRepository : JpaRepository<ImageAsset, UUID>

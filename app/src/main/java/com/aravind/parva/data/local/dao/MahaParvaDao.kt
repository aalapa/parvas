package com.aravind.parva.data.local.dao

import androidx.room.*
import com.aravind.parva.data.local.entities.MahaParvaEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for MahaParva
 */
@Dao
interface MahaParvaDao {
    
    @Query("SELECT * FROM maha_parvas ORDER BY startDate DESC")
    fun getAllMahaParvas(): Flow<List<MahaParvaEntity>>

    @Query("SELECT * FROM maha_parvas WHERE id = :id")
    suspend fun getMahaParvaById(id: String): MahaParvaEntity?

    @Query("SELECT * FROM maha_parvas WHERE id = :id")
    fun getMahaParvaByIdFlow(id: String): Flow<MahaParvaEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMahaParva(mahaParva: MahaParvaEntity)

    @Update
    suspend fun updateMahaParva(mahaParva: MahaParvaEntity)

    @Delete
    suspend fun deleteMahaParva(mahaParva: MahaParvaEntity)

    @Query("DELETE FROM maha_parvas WHERE id = :id")
    suspend fun deleteMahaParvaById(id: String)

    @Query("SELECT COUNT(*) FROM maha_parvas")
    suspend fun getMahaParvaCount(): Int
}


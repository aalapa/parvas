package com.aravind.parva.data.repository

import com.aravind.parva.data.local.dao.MahaParvaDao
import com.aravind.parva.data.local.entities.MahaParvaEntity
import com.aravind.parva.data.model.HoldPeriod
import com.aravind.parva.data.model.MahaParva
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository for MahaParva data operations
 * Single source of truth for Maha-Parva data
 * Later: Add Supabase sync here
 */
class MahaParvaRepository(private val mahaParvaDao: MahaParvaDao) {

    /**
     * Get all Maha-Parvas as a Flow
     */
    val allMahaParvas: Flow<List<MahaParva>> = 
        mahaParvaDao.getAllMahaParvas().map { entities ->
            entities.map { it.toMahaParva() }
        }

    /**
     * Get a specific Maha-Parva by ID as a Flow
     */
    fun getMahaParvaById(id: String): Flow<MahaParva?> =
        mahaParvaDao.getMahaParvaByIdFlow(id).map { it?.toMahaParva() }

    /**
     * Get a specific Maha-Parva by ID (one-time)
     */
    suspend fun getMahaParvaByIdOnce(id: String): MahaParva? {
        return mahaParvaDao.getMahaParvaById(id)?.toMahaParva()
    }

    /**
     * Get all Maha-Parvas (one-time, for export)
     */
    suspend fun getAllMahaParvasOnce(): List<MahaParva> {
        return mahaParvaDao.getAllMahaParvasOnce().map { it.toMahaParva() }
    }
    
    /**
     * Insert a Maha-Parva (for import)
     */
    suspend fun insertMahaParva(mahaParva: MahaParva) {
        mahaParvaDao.insertMahaParva(MahaParvaEntity.fromMahaParva(mahaParva))
    }

    /**
     * Insert or update a Maha-Parva
     */
    suspend fun saveMahaParva(mahaParva: MahaParva) {
        mahaParvaDao.insertMahaParva(MahaParvaEntity.fromMahaParva(mahaParva))
    }

    /**
     * Update an existing Maha-Parva
     */
    suspend fun updateMahaParva(mahaParva: MahaParva) {
        mahaParvaDao.updateMahaParva(MahaParvaEntity.fromMahaParva(mahaParva))
    }

    /**
     * Delete a Maha-Parva
     */
    suspend fun deleteMahaParva(mahaParva: MahaParva) {
        mahaParvaDao.deleteMahaParvaById(mahaParva.id)
    }

    /**
     * Get count of Maha-Parvas
     */
    suspend fun getMahaParvaCount(): Int {
        return mahaParvaDao.getMahaParvaCount()
    }

    /**
     * Update a specific Dina's notes and completion status
     */
    suspend fun updateDina(
        mahaParvaId: String,
        dayNumber: Int,
        dailyIntention: String,
        notes: String,
        isCompleted: Boolean
    ) {
        val mahaParva = getMahaParvaByIdOnce(mahaParvaId) ?: return
        
        val updatedParvas = mahaParva.parvas.map { parva ->
            val updatedSaptahas = parva.saptahas.map { saptaha ->
                val updatedDinas = saptaha.dinas.map { dina ->
                    if (dina.dayNumber == dayNumber) {
                        dina.copy(
                            dailyIntention = dailyIntention,
                            notes = notes,
                            isCompleted = isCompleted
                        )
                    } else {
                        dina
                    }
                }
                saptaha.copy(dinas = updatedDinas)
            }
            parva.copy(saptahas = updatedSaptahas)
        }
        
        saveMahaParva(mahaParva.copy(parvas = updatedParvas))
    }

    /**
     * Update a Parva's custom goal
     */
    suspend fun updateParvaGoal(
        mahaParvaId: String,
        parvaIndex: Int,
        goal: String
    ) {
        val mahaParva = getMahaParvaByIdOnce(mahaParvaId) ?: return
        
        val updatedParvas = mahaParva.parvas.mapIndexed { index, parva ->
            if (index == parvaIndex) {
                parva.copy(customGoal = goal)
            } else {
                parva
            }
        }
        
        saveMahaParva(mahaParva.copy(parvas = updatedParvas))
    }

    /**
     * Update a Saptaha's custom goal
     */
    suspend fun updateSaptahaGoal(
        mahaParvaId: String,
        parvaIndex: Int,
        saptahaIndex: Int,
        goal: String
    ) {
        val mahaParva = getMahaParvaByIdOnce(mahaParvaId) ?: return
        
        val updatedParvas = mahaParva.parvas.mapIndexed { pIdx, parva ->
            if (pIdx == parvaIndex) {
                val updatedSaptahas = parva.saptahas.mapIndexed { sIdx, saptaha ->
                    if (sIdx == saptahaIndex) {
                        saptaha.copy(customGoal = goal)
                    } else {
                        saptaha
                    }
                }
                parva.copy(saptahas = updatedSaptahas)
            } else {
                parva
            }
        }
        
        saveMahaParva(mahaParva.copy(parvas = updatedParvas))
    }

    /**
     * Update hold periods for a Maha-Parva
     * Regenerates all Parvas/Saptahas/Dinas with adjusted dates
     * Preserves all user data (goals, notes, completion)
     */
    suspend fun updateHoldPeriods(
        mahaParvaId: String,
        holdPeriods: List<HoldPeriod>
    ) {
        val mahaParva = getMahaParvaByIdOnce(mahaParvaId) ?: return
        
        // Regenerate with new hold periods (preserves user data)
        val regenerated = mahaParva.regenerateWithHolds(holdPeriods)
        
        saveMahaParva(regenerated)
    }
}


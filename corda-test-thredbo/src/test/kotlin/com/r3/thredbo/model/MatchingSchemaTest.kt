package com.r3.thredbo.model

import io.requery.kotlin.eq
import io.requery.kotlin.invoke
import io.requery.sql.KotlinConfiguration
import io.requery.sql.KotlinEntityDataStore
import io.requery.sql.SchemaModifier
import io.requery.sql.TableCreationMode
import org.h2.jdbcx.JdbcDataSource
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by josecoll on 13/01/2017.
 */
class MatchingSchemaTest {
    var instance : KotlinEntityDataStore<Any>? = null
    val data : KotlinEntityDataStore<Any> get() = instance!!

    @Before
    fun setup() {
        val model = Models.MODEL
        val dataSource = JdbcDataSource()
        dataSource.setUrl("jdbc:h2:~/testh2")
        dataSource.user = "sa"
        dataSource.password = "sa"

        val configuration = KotlinConfiguration(
                dataSource = dataSource,
                model = model,
                statementCacheSize = 0,
                useDefaultLogging = true)
        instance = KotlinEntityDataStore(configuration)
        val tables = SchemaModifier(configuration)
        tables.dropTables()
        val mode = TableCreationMode.CREATE
        tables.createTables(mode)
    }

    @Test
    fun testInsert() {
        val state = MatchingSchemaV1.MatchingState("12345", "BUY")
//                MatchingSchemaV1.PersistentStateRef(StateRef(SecureHash.randomSHA256(), Random().nextInt(32))))
        data.invoke {
            insert(state)
            val result = select(MatchingSchemaV1.MatchingState::class) where (MatchingSchemaV1.MatchingState::linearId eq state.linearId) limit 10
            assertEquals(result().first().linearId, state.linearId)
        }
    }
}
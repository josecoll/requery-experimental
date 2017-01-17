package net.corda.node.services.vault.schemas

import io.requery.kotlin.eq
import io.requery.kotlin.invoke
import io.requery.sql.KotlinConfiguration
import io.requery.sql.KotlinEntityDataStore
import io.requery.sql.SchemaModifier
import io.requery.sql.TableCreationMode
import org.h2.jdbcx.JdbcDataSource
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * Created by josecoll on 13/01/2017.
 */
class VaultSchemaTest {

    var instance : KotlinEntityDataStore<Any>? = null
    val data : KotlinEntityDataStore<Any> get() = instance!!

    @Before
    fun setup() {
        val dataSource = JdbcDataSource()
        dataSource.setUrl("jdbc:h2:~/testh2")
        dataSource.user = "sa"
        dataSource.password = "sa"

        val configuration = KotlinConfiguration(dataSource = dataSource, model = Models.VAULT, useDefaultLogging = true)
        instance = KotlinEntityDataStore<Any>(configuration)
        val tables = SchemaModifier(configuration)
        val mode = TableCreationMode.DROP_CREATE
        tables.createTables(mode)
    }

    @After
    fun tearDown() {
        data.close()
    }

    /**
     *  Vault Schema: VaultStates
     */
    @Test
    fun testInsertState() {
        val state = VaultStatesEntity()
        state.txId ="12345"
        state.index = 0
        data.invoke {
            insert(state)
            val result = select(VaultSchema.VaultStates::class) where (VaultSchema.VaultStates::txId eq state.txId) limit 10
            Assert.assertSame(result().first(), state)

        }
    }

    /**
     *  Vault Schema: VaultFungibleState
     */
    @Test
    fun testInsertFungibleState() {
        val state = VaultFungibleStateEntity()
        state.txId ="12345"
        state.index = 0
        data.invoke {
            insert(state)
            val result = select(VaultSchema.VaultFungibleState::class) where (VaultSchema.VaultFungibleState::txId eq state.txId) limit 10
            Assert.assertSame(result().first(), state)

        }
    }
}
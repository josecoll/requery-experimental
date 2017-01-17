package com.r3.thredbo.model

import io.requery.Persistable
import net.corda.core.contracts.StateRef
import net.corda.core.schemas.MappedSchema
import net.corda.core.serialization.toHexString
import java.io.Serializable
import javax.persistence.*

object MatchingSchema

object MatchingSchemaV1 : MappedSchema(schemaFamily = MatchingSchema.javaClass, version = 1, mappedTypes = listOf(MatchingState::class.java)) {

    @MappedSuperclass
    open class PersistentState(
            @EmbeddedId
            var stateRef: PersistentStateRef? = null
    ) : Serializable, Persistable

    @Embeddable
    class PersistentStateRef(
            @Column(length = 64)
            var txId: String?,

            @Column()
            var index: Int?
    ) : Serializable {
        constructor(stateRef: StateRef) : this(stateRef.txhash.bytes.toHexString(), stateRef.index)
        /*
         JPA Query requirement:
         @Entity classes should have a default (non-arg) constructor to instantiate the objects when retrieving them from the database.
        */
        constructor() : this(null, null)
    }

    @Entity
    @Table(name = "matching_states")
    class MatchingState(
            @get:Column(name = "linearId", nullable = false)
            var linearId: String,

            @get:Column(name = "action", nullable = false)
            var action: String

//            @Embedded
//            var stateRef: PersistentStateRef
//
//            @get:Column(name = "senderRef", nullable = false)
//            var senderRef: String,
//
//            @get:Column(name = "commonRef", nullable = false)
//            var commonRef: String,
//
////            @Type(type = "com.r3.thredbo.model.SecureHashTypeMapper")
////            @Column(name = "fingerprint", nullable = false)
////            var fingerprint: SecureHash,
//
//            @get:Column(name = "relatedRef", nullable = true)
//            var relatedRef: String?,
//
//            @get:Column(name = "accountA", nullable = true)
//            var accountA: String?,
//
//            @get:Column(name = "bicA", nullable = false)
//            var bicA: String,
//
////            @Type(type = "com.r3.thredbo.model.PartyTypeMapper")
////            @Columns(columns = arrayOf(Column(name = "partyA_name"),
////                    Column(name = "partyA_owningKey")))
////            var cordaPartyA: Party,
//
//            @get:Column(name = "accountB", nullable = true)
//            var accountB: String?,
//
//            @get:Column(name = "bicB", nullable = false)
//            var bicB: String,
//
////            @Type(type = "com.r3.thredbo.model.PartyTypeMapper")
////            @Columns(columns = arrayOf(Column(name = "partyB_name"),
////                    Column(name = "partyB_owningKey")))
////            var cordaPartyB: Party,
//
//            @get:Column(name = "tradeDate", nullable = false)
//            var tradeDate: Instant,
//
//            @get:Column(name = "valueDate", nullable = false)
//            var valueDate: Instant,
//
//            @get:Column(name = "submissionTime", nullable = false)
//            var submissionTime: Instant,
//
//            @get:Column(name = "boughtAmount", scale = 2, precision = 19, nullable = false)
//            var boughtAmount: BigDecimal,
//
//            @get:Column(name = "boughtCurrency", length = 3, nullable = false)
//            var boughtCurrency: String,
//
//            @get:Column(name = "soldAmount", scale = 2, precision = 19, nullable = false)
//            var soldAmount: BigDecimal,
//
//            @get:Column(name = "soldCurrency", length = 3, nullable = false)
//            var soldCurrency: String,
//
//            @get:Column(name = "rate", scale = 8, precision = 19, nullable = false)
//            var rate: BigDecimal
    )
        //: PersistentState()
//
}
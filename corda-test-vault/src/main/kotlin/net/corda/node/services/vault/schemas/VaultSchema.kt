package net.corda.node.services.vault.schemas

import io.requery.*
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.requery.converters.InstantConverter
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

object VaultSchema {

    @Table(name = "vault_states")
    @Entity(model = "vault")
    interface VaultStates : PersistentState {

        @get:Column(name = "notary_name")
        var notaryName: String
        //
        // references a concrete ContractState that is [QueryableState] and has a [MappedSchema]
        @get:Column(name = "contract_state_class_name")
        var contractStateClassName: String
        // references a concrete ContractState that is [QueryableState] and has a [MappedSchema] version
        @get:Column(name = "contract_state_class_version")
//        @get:Version
        var contractStateClassVersion: Int

        @get:Column(name = "state_status")
        var stateStatus: StateStatus

        // refers to timestamp recorded upon entering AWAITING_CONSENSUS state
        @get:Column(name = "committed")
        @get:Convert(InstantConverter::class)
        var committed: Instant

        // refers to timestamp recorded upon entering CONSENSUS_AGREED_UNCONSUMED state
        @get:Column(name = "notarised")
        @get:Convert(InstantConverter::class)
        var notarised: Instant

        // refers to timestamp recorded upon entering CONSENSUS_AGREED_CONSUMED state
        @get:Column(name = "consumed")
        @get:Convert(InstantConverter::class)
        var consumed: Instant

        @get:Column(name = "lock_id", nullable = true)
        var lockId: String
    }

    enum class StateStatus {
        AWAITING_CONSENSUS, CONSENSUS_AGREED_UNCONSUMED, CONSENSUS_AGREED_CONSUMED
    }

    @Table(name = "vault_consumed_fungible_states")
    @Entity(model = "vault")
    interface VaultFungibleState : PersistentState {

        @get:OneToMany(mappedBy = "key")
        var participants: VaultKey

        @get:OneToOne(mappedBy = "key")
        var ownerKey: VaultKey

        var quantity: Long
        var ccyCode: String

        @get:OneToOne(mappedBy = "key")
        var issuerKey: VaultKey
        var issuerRef: ByteArray

        @get:OneToMany(mappedBy = "key")
        var exitKeys: VaultKey
    }

    @Table(name = "vault_consumed_linear_states")
    @Entity(model = "vault")
    interface VaultLinearState : PersistentState {

        @get:OneToMany(mappedBy = "key")
        var participants: VaultKey

        @get:OneToOne(mappedBy = "key")
        var ownerKey: VaultKey

        @get:Index("externalId_index")
        var externalId: String
        @get:Column(length = 36, unique = true, nullable = false)
        var uuid: UUID

        var dealRef: String
        @get:OneToMany(mappedBy = "name")
        var dealParties: VaultParty
    }

    @Table(name = "vault_keys")
    @Entity(model = "vault")
    interface VaultKey : Persistable {
        @get:Key
        @get:Column(length = 255)
        @get:ForeignKey
        var key: String
    }

    @Table(name = "vault_parties")
    @Entity(model = "vault")
    interface VaultParty : Persistable {
        @get:ForeignKey
        @get:Key
        var name: String
        @get:ForeignKey
        @get:Key
        @get:Column(length = 255)
        var key: String
    }
}

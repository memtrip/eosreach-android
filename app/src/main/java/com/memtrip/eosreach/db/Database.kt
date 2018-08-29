package com.memtrip.eosreach.db

import android.app.Application
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.memtrip.eosreach.utils.RxSchedulers
import dagger.Module
import dagger.Provides
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Database(entities = [AccountEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
}

@Entity(tableName = "Account")
data class AccountEntity(
    @ColumnInfo(name = "publicKey") val publicKey: String,
    @ColumnInfo(name = "accountName") val accountName: String,
    @PrimaryKey(autoGenerate = true) val uid: Int = 0
)

@Dao
interface AccountDao {

    @Insert
    fun insertAll(pinyin: List<AccountEntity>)

    @Query("DELETE FROM Account WHERE publicKey = :publicKey")
    fun deleteBy(publicKey: String)

    @Query("SELECT DISTINCT uid, publicKey, accountName FROM Account WHERE publicKey = :publicKey ORDER BY uid ASC LIMIT 0, 100")
    fun getAccountsForPublicKey(publicKey: String): List<AccountEntity>

    @Query("SELECT DISTINCT uid, publicKey, accountName FROM Account ORDER BY uid ASC LIMIT 0, 100")
    fun getAccounts(): List<AccountEntity>

    @Query("SELECT * FROM Account ORDER BY uid ASC LIMIT 0, 1")
    fun getLatestAccount(): List<AccountEntity>

    @Query("SELECT COUNT(*) FROM Account")
    fun count(): Int
}

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun appDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application.applicationContext, AppDatabase::class.java, "eosreach")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun accountDao(appDatabase: AppDatabase): AccountDao = appDatabase.accountDao()
}

class InsertAccountsForPublicKey @Inject internal constructor(
    private val accountDao: AccountDao,
    private val rxSchedulers: RxSchedulers
) {

    fun replace(publicKey: String, accounts: List<String>): Single<List<AccountEntity>> {

        val publicKeyAccountEntities = accounts.map { accountName ->
            AccountEntity(publicKey, accountName)
        }

        return Completable
            .fromAction { accountDao.deleteBy(publicKey) }
            .andThen(Completable.fromAction { accountDao.insertAll(publicKeyAccountEntities) })
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
            .toSingle { publicKeyAccountEntities }
    }
}

class CountAccounts @Inject internal constructor(
    private val accountDao: AccountDao,
    private val rxSchedulers: RxSchedulers
) {

    fun count(): Single<Int> {
        return Single.fromCallable { accountDao.count() }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }
}

class GetAccounts @Inject internal constructor(
    private val accountDao: AccountDao,
    private val rxSchedulers: RxSchedulers
) {

    fun select(): Single<List<AccountEntity>> {
        return Single.fromCallable { accountDao.getAccounts() }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
    }
}

class GetLatestAccount @Inject internal constructor(
    private val accountDao: AccountDao,
    private val rxSchedulers: RxSchedulers
) {

    fun select(): Single<AccountEntity> {
        return Single.fromCallable { accountDao.getLatestAccount() }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
            .map { it[0] }
    }
}
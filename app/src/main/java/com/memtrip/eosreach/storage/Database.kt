package com.memtrip.eosreach.storage

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

@Database(entities = [PublicKeyAccountEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
}

@Entity(tableName = "PublicKeyAccount")
data class PublicKeyAccountEntity(
    @ColumnInfo(name = "publicKey") val publicKey: String,
    @ColumnInfo(name = "accountName") val accountName: String,
    @PrimaryKey(autoGenerate = true) val uid: Int = 0
)

@Dao
interface AccountDao {

    @Insert
    fun insertAll(pinyin: List<PublicKeyAccountEntity>)

    @Query("SELECT DISTINCT uid, publicKey, accountName FROM PublicKeyAccount WHERE publicKey = :publicKey ORDER BY uid ASC LIMIT 0, 100")
    fun getAccountsForPublicKey(publicKey: String): List<PublicKeyAccountEntity>
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

    fun insert(publicKey: String, accounts: List<String>): Single<List<PublicKeyAccountEntity>> {

        val publicKeyAccountEntity = accounts.map {
            PublicKeyAccountEntity(publicKey, it)
        }

        return Completable
            .fromAction { accountDao.insertAll(publicKeyAccountEntity) }
            .observeOn(rxSchedulers.main())
            .subscribeOn(rxSchedulers.background())
            .toSingle { publicKeyAccountEntity }
    }
}
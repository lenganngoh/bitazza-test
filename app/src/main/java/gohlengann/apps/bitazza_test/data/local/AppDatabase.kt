package gohlengann.apps.bitazza_test.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import gohlengann.apps.bitazza_test.data.model.entity.AuthenticatedUser
import gohlengann.apps.bitazza_test.data.model.entity.Instrument
import gohlengann.apps.bitazza_test.data.model.entity.Product

@Database(entities = [AuthenticatedUser::class, Instrument::class, Product::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun authenticatedUserDao(): AuthenticatedUserDao
    abstract fun instrumentDao(): InstrumentDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "bitazza-test.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
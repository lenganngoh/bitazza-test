package gohlengann.apps.bitazza_test.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import gohlengann.apps.bitazza_test.data.model.entity.AuthenticatedUser
import gohlengann.apps.bitazza_test.data.model.entity.Instrument

@Database(entities = [AuthenticatedUser::class, Instrument::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun authenticatedUserDao(): AuthenticatedUserDao
    abstract fun instrumentDao(): InstrumentDao

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
package gohlengann.apps.bitazza_test.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gohlengann.apps.bitazza_test.data.model.entity.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM product")
    fun getLocalProducts(): LiveData<List<Product>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: Product)

    @Query("DELETE FROM product")
    fun clearProducts()
}
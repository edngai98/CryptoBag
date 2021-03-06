package au.edu.unsw.infs3634.cryptobag.Entities;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import au.edu.unsw.infs3634.cryptobag.Entities.Coin;

@Dao
public interface CoinDao {

    @Query("SELECT * FROM coin")
    List<Coin> getCoins();

    @Query("SELECT * FROM coin WHERE id == :coinId")
    Coin getCoin(String coinId);

    @Insert
    void insertCoins(Coin...coins);

    @Query("DELETE FROM coin")
    void deleteCoins();
}

package au.edu.unsw.infs3634.cryptobag.Entities;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import au.edu.unsw.infs3634.cryptobag.Entities.Coin;
import au.edu.unsw.infs3634.cryptobag.Entities.CoinDao;

@Database(entities = {Coin.class}, version = 1)
public abstract class CoinDatabase extends RoomDatabase {
    public abstract CoinDao coinDao();
}

package au.edu.unsw.infs3634.cryptobag;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.infs3634.cryptobag.Entities.Coin;
import au.edu.unsw.infs3634.cryptobag.Entities.CoinDatabase;
import au.edu.unsw.infs3634.cryptobag.Entities.CoinLoreResponse;
import au.edu.unsw.infs3634.cryptobag.Entities.CoinService;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private boolean mTwoPane;
    private CoinAdapter mAdapter;
    private CoinDatabase coinDatabase;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.detail_container) != null) {
            mTwoPane = true;
        }

        RecyclerView mRecyclerView = findViewById(R.id.rvList);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CoinAdapter(this, new ArrayList<Coin>(), mTwoPane);
        mRecyclerView.setAdapter(mAdapter);

        coinDatabase = Room.databaseBuilder(getApplicationContext(), CoinDatabase.class, "myDB")
                .build();


        new MyTask().execute();
        new insertUserTask().execute();

    }


    public class MyTask extends AsyncTask<Void, Void, List<Coin>> {
        @Override
        protected void onPostExecute(List<Coin> coins) {
            super.onPostExecute(coins);
            //mAdapter.setCoins(coins);
        }

        @Override
        protected List<Coin> doInBackground(Void... voids) {
            coinDatabase.coinDao().deleteCoins();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.coinlore.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            CoinService service = retrofit.create(CoinService.class);
            Call<CoinLoreResponse> coinsCall = service.getCoins();

            List<Coin> coins = null;
            try {
                Response<CoinLoreResponse> response = coinsCall.execute();
                coins = response.body().getData();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Coin[] coinList = coins.toArray(new Coin[coins.size()]);
            coinDatabase.coinDao().insertCoins(coinList);
            return null;
        }
    }

    public class insertUserTask extends AsyncTask<Void, Void, List<Coin>> {


        @Override
        protected List<Coin> doInBackground(Void... voids) {
            List coinsTest = coinDatabase.coinDao().getCoins();
            return coinsTest;
        }

        @Override
        protected void onPostExecute(List<Coin> coins) {
            super.onPostExecute(coins);
            mAdapter.setCoins(coins);
        }
    }
}

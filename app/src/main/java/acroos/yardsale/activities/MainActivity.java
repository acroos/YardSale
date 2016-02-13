package acroos.yardsale.activities;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import acroos.yardsale.R;
import acroos.yardsale.data.DatabaseHandler;
import acroos.yardsale.models.ForSaleItem;
import acroos.yardsale.models.YardSaleUser;

public class MainActivity extends AppCompatActivity {

    private YardSaleUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUser = DatabaseHandler.getInstance().getUser(extras.getString("USERNAME"));
        }

        TextView mFullNameTextView = (TextView) findViewById(R.id.full_name_text_view);
        final TextView mUserNameTextView = (TextView) findViewById(R.id.email_text_view);
        ImageView mProfilePicture = (ImageView) findViewById(R.id.profile_picture);
        ListView mForSaleItems = (ListView) findViewById(R.id.for_sale_items_list);
        Button mAddItemButton = (Button) findViewById(R.id.add_item_button);

        if (mUser.getFullName() != null) {
            mFullNameTextView.setText(mUser.getFullName());
        }

        mUserNameTextView.setText(mUser.getUsername());

        if (mUser.getProfilePicturePath() != null) {
            mProfilePicture.setImageBitmap(BitmapFactory.decodeFile(mUser.getProfilePicturePath()));
        } else {
            mProfilePicture.setBackgroundColor(Color.GREEN);
        }



        ArrayList<ForSaleItem> forSaleItems = DatabaseHandler.getInstance().getItemsForUser(mUser.getId());
        final ArrayList<String> forSaleItemNames = new ArrayList<>();
        for(ForSaleItem item : forSaleItems) {
            forSaleItemNames.add(item.getItemName());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, forSaleItemNames);
        mForSaleItems.setAdapter(adapter);

        mAddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForSaleItem item = new ForSaleItem().setItemName("Item " + new Random().nextInt()).setPrice(11.99f);
                DatabaseHandler.getInstance().addItem(item, mUser.getId());

                forSaleItemNames.add(item.getItemName());
                adapter.notifyDataSetChanged();
            }
        });
    }
}

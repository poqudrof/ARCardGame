package com.universitedebordeaux.jumathsji;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    // private static final int RC_OCR_CAPTURE = 9003;

    // private String current_tip = "";
    // private String current_answer = "";

    // private List<CardWithLines> current_cardList;
    // private int current_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        setContentView(R.layout.activity_second);

        current_cardList = new ArrayList<>();
        current_index = 0;

        startOcr();
        */
    }

    //Function call after click event on Answer Button
    /*
    public void getSolution(View view) {
        Log.d("getSolution", "getSolution");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(current_answer);
        builder.setTitle("Réponse");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Function call after click event on Tip Button
    public void getHelp(View view) {
        Log.d("getHelp", "getHelp");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(current_tip);
        builder.setTitle("Aide");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Function call after click event on Camera Button
    public void validation(View view) {
        Log.d("validation", "validation");
        startOcr();
    }

    //Function call after click event on Next Button
    public void nextCard(View view) {
        Log.d("next card", "display next card of the list current");

        current_index++;
        if (current_index == current_cardList.size()) {
            current_index = 0;
        }

        displayCard(current_cardList.get(current_index));
        selectColorType(current_cardList.get(current_index));
    }

    //Start the activity OCR
    private void startOcr() {
        Intent intent = new Intent(this, TextRecognitionActivity.class);
        startActivityForResult(intent, RC_OCR_CAPTURE);
    }

    // Start the Asynchronous Search
    public void startSearch(String id) {
        // CardSearchTask
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //After result on Activity OCR display Card
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_OCR_CAPTURE && resultCode == CommonStatusCodes.SUCCESS && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                fillListCard(extras.getParcelableArrayList("CARD_LIST"));
            }
        }
    }

    //Return a list of x Cards in order to display them as a carousel
    public void fillListCard(List<CardWithLines> result) {
        current_cardList.clear();

        if (result != null) {
            current_cardList.addAll(result);
        }

        Button next_button = findViewById(R.id.display_next);
        if (current_cardList.size() > 1) {
            next_button.setVisibility(View.VISIBLE);
        } else {
            next_button.setVisibility(View.INVISIBLE);
        }
        current_index = 0;
        displayCard(current_cardList.get(0));
        selectColorType(current_cardList.get(0));
    }

    //Display the card from the Bundle Result
    protected void displayCard(CardWithLines cardWithLines) {

        Card card = cardWithLines.card;
        //Complete each TextView/Button for each Card Element
        TextView card_id = findViewById(R.id.display_card_code);
        card_id.setText(card.id);

        TextView card_title = findViewById(R.id.display_card_title);
        card_title.setText(card.title);

        TextView card_text = findViewById((R.id.display_card_text));
        card_text.setText(cardWithLines.getText());

        Button card_numberR = findViewById((R.id.display_card_numberR));
        String tmp = "R" + card.number;
        card_numberR.setText(tmp);

        Button card_numberA = findViewById((R.id.display_card_numberA));
        tmp = "A" + card.number;
        card_numberA.setText(tmp);

        //Save the answer and tip for later
        current_answer = card.answer;
        current_tip = card.tip;

        //Set Visible the Tip Button if there is
        if (current_tip == null) {
            card_numberA.setVisibility(View.INVISIBLE);
        } else {
            card_numberA.setVisibility(View.VISIBLE);
        }

        //Find and display the image associated with the Card if there are
        ImageView card_img = findViewById(R.id.display_img);

        //String folder = this.getResources().getString(R.string.database_phone_path) + "img/";
        String folder = getApplicationContext().getExternalFilesDir("jumathsji").getPath() + "/figs/";
        String img_name = folder + card.id + ".jpg";

        File file = new File(img_name);
        if (file.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(img_name);
            card_img.setImageBitmap(Bitmap.createScaledBitmap(bm, bm.getWidth() * 4,
                    bm.getHeight() * 4, false));
        } else {
            card_img.setImageBitmap(null);
        }
    }

    //Setting the color theme for the Card Display
    //Ideally a file should contain the names of Categories and their colors
    protected void selectColorType(CardWithLines cardWithLines) {
        String type = cardWithLines.card.title;
        int color = ContextCompat.getColor(this, R.color.colorTitleEspaces3D);

        switch (type) {
            case "Espaces 3D":
                color = ContextCompat.getColor(this, R.color.colorTitleEspaces3D);
                break;
            case "English Maths":
                color = ContextCompat.getColor(this, R.color.colorTitleEnglishMaths);
                break;
            case "Montagne de problème":
                color = ContextCompat.getColor(this, R.color.colorTitleMontagneDeProblèmes);
                break;
            case "Plaine de 2D":
                color = ContextCompat.getColor(this, R.color.colorTitlePlaineDe2D);
                break;
            case "Vallée des nombres":
                color = ContextCompat.getColor(this, R.color.colorTitleValléeDesNombres);
                break;
            default:
                break;
        }

        TextView card_title = findViewById(R.id.display_card_title);
        card_title.setTextColor(color);
        Button card_numberR = findViewById((R.id.display_card_numberR));
        card_numberR.setTextColor(color);
        Button card_numberA = findViewById((R.id.display_card_numberA));
        card_numberA.setTextColor(color);
    }

    //Use to create a toolbar above the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ocr_menu, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //Used to select an action in the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.action_goBack) {
            Log.d("Case goBack", "Go to Main Activity case chosen");
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.action_settings) {
                Log.d("Case Option", "Option case chosen");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */
}
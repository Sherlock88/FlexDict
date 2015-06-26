package in.dipanjan.flexdict;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;


/* http://cyrilmottier.com/2011/08/08/listview-tips-tricks-3-create-fancy-listviews */
public class AllWordsList extends ListActivity {

    private AllWordsListAdapter allWordsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_all_words);
        Intent localIntent = getIntent();
        Bundle params = localIntent.getExtras();
        ArrayList<String> allWordsList = params.getStringArrayList("AllWords");
        int wordCount = allWordsList.size();
        String[] allWordsArray = new String[wordCount];
        for (int i = 0; i < wordCount; i++)
            allWordsArray[i] = new String((i + 1) + ". " + allWordsList.get(i).toUpperCase());
        allWordsListAdapter = new AllWordsListAdapter(allWordsArray);
        setListAdapter(allWordsListAdapter);

        final ListView lv = (ListView) findViewById(android.R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listViewAdapter, View listView, int listItemSelected, long listLong) {
                //String selectedFromList = (String) (lv.getItemAtPosition(listItemSelected));
                //Log.i("Flex_Debug", selectedFromList);
                Intent output = new Intent();
                output.putExtra("SeletedWordID", (listItemSelected + 1));
                setResult(RESULT_OK, output);
                finish();
            }
        });
    }


    private class AllWordsListAdapter extends BaseAdapter {

        private String[] mData;
        public AllWordsListAdapter(String[] data) {
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.length;
        }

        @Override
        public String getItem(int position) {
            return mData[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView result;

            if (convertView == null) {
                result = (TextView) getLayoutInflater().inflate(R.layout.list_all_words_item, parent, false);
            } else {
                result = (TextView) convertView;
            }

            final String seletedItem = getItem(position);
            result.setBackgroundResource(R.drawable.list_selector_focused);
            result.setText(seletedItem);
            result.setTag(position);

            return result;
        }
    }
}

package com.example.mmclient.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mmclient.R;
import com.example.mmclient.adapter.CheckBoxToMemberAdapter;
import com.example.mmclient.domain.Member;
import com.example.mmclient.domain.MemberAndAmount;
import com.example.mmclient.domain.SimpleCalculator;
import com.example.mmclient.service.GetGoogleAccount;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Activity class for Submit Amount screen.
 *
 * @author SWAPNIL
 */
public class SubmitAmountActivity extends Activity {

    BigDecimal anupamAmount = BigDecimal.ZERO;
    BigDecimal anuragAmount = BigDecimal.ZERO;
    BigDecimal arpanaAmount = BigDecimal.ZERO;
    String paidBy;
    String description;
    String amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_amount);
        TextView welcomeTextView = (TextView) findViewById(R.id.welcomeMessageId);
        welcomeTextView.setText("Welcome !");
    }

    /**
     * This method will be called when user enters amount and clicks on submit
     * button.
     *
     * @param view
     */
    public void submitAmount(View view) {
        EditText amountEditText = (EditText) findViewById(R.id.amountId);
         amount = amountEditText.getText().toString();
        System.out.println(amount);
        process(amount);
        amountEditText.setText("");

        TextView amountAddedTextView = (TextView) findViewById(R.id.amountAddedMessageId);
        amountAddedTextView.setText("Added Amount Rs" + amount
                + " in google sheet");
    }

    /**
     * Process the given Amount.
     *
     * @param amount
     */
    private void process(String amount) {
        List<CheckBox> checkBoxs = new ArrayList<CheckBox>();
        CheckBox anupamCheckBox = (CheckBox) findViewById(R.id.anupamId);
        checkBoxs.add(anupamCheckBox);
        CheckBox anuragCheckBox = (CheckBox) findViewById(R.id.anuragId);
        checkBoxs.add(anuragCheckBox);
        CheckBox arpanaCheckBox = (CheckBox) findViewById(R.id.arpanaId);
        checkBoxs.add(arpanaCheckBox);

        List<Member> members = CheckBoxToMemberAdapter.toMembers(checkBoxs);
        List<MemberAndAmount> memberAndAmounts = SimpleCalculator.calculate(
                members, new BigDecimal(amount));


        for (MemberAndAmount memberAndAmount : memberAndAmounts) {
            System.out.println(memberAndAmount.getAmount());
            if (memberAndAmount.getMember().getFirstName()
                    .equalsIgnoreCase("anupam")) {
                anupamAmount = memberAndAmount.getAmount();
            }
            if (memberAndAmount.getMember().getFirstName()
                    .equalsIgnoreCase("anurag")) {
                anuragAmount = memberAndAmount.getAmount();
            }
            if (memberAndAmount.getMember().getFirstName()
                    .equalsIgnoreCase("arpana")) {
                arpanaAmount = memberAndAmount.getAmount();
            }
        }

        TextView expense = (TextView) findViewById(R.id.expenseId);
        String name = new GetGoogleAccount(getSystemService(ACCOUNT_SERVICE))
                .getName();
        Spinner memberDropDown = (Spinner) findViewById(R.id.spendById);
         paidBy = memberDropDown.getSelectedItem().toString();
        /*new CallAPI(new BigDecimal(amount), anupamAmount, anuragAmount,
                arpanaAmount, expense.getText().toString(), name,
                memberDropDown.getSelectedItem().toString()).execute();*/
        description=expense.getText().toString();

        //submitDetailsToSpreadSheet(expense.getText().toString(), anupamAmount, arpanaAmount, anuragAmount, amount, paidBy);
        new SpreadSheetProcess().execute();
        resetUIFields(anupamCheckBox, anuragCheckBox, arpanaCheckBox, expense,
                memberDropDown);


    }



    /**
     * Reset the UI fields to default.
     *
     * @param anupamCheckBox
     * @param anuragCheckBox
     * @param arpanaCheckBox
     * @param expense
     *
     */
    private void resetUIFields(CheckBox anupamCheckBox,
                               CheckBox anuragCheckBox, CheckBox arpanaCheckBox, TextView expense,
                               Spinner memberDropDown) {
        expense.setText("");
        anupamCheckBox.setChecked(false);
        anuragCheckBox.setChecked(false);
        arpanaCheckBox.setChecked(false);
        memberDropDown.setSelection(0);
    }

    private class SpreadSheetProcess extends AsyncTask<String, String, String> {



        @Override
        protected String doInBackground(String... arg0) {

            SharedPreferences pref = getSharedPreferences("AppPref", MODE_PRIVATE);
            String token = pref.getString("Access Token", null);
            SpreadsheetService service =
                    new SpreadsheetService("MySpreadsheetIntegration-v1");
            service.setAuthSubToken(token);

            URL SPREADSHEET_FEED_URL = null;
            try {
                SPREADSHEET_FEED_URL = new URL(
                        "https://spreadsheets.google.com/feeds/spreadsheets/private/full");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            SpreadsheetFeed feed = null;
            try {
                feed = service.getFeed(SPREADSHEET_FEED_URL,
                        SpreadsheetFeed.class);

                List<SpreadsheetEntry> spreadsheets = feed.getEntries();


                int count = spreadsheets.size() - 1;
                SpreadsheetEntry spreadsheet = null;
                while (count >= 0) {
                    if (spreadsheets.get(count--).getTitle().getPlainText().equals("Step expenses Sheet")) {
                        spreadsheet = spreadsheets.get(count + 1);
                        System.out.println("file selected: " + spreadsheet.getTitle().getPlainText());
                        break;
                    }
                }

                WorksheetFeed worksheetFeed = service.getFeed(
                        spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
                List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
                WorksheetEntry worksheet = worksheets.get(0);

                // Fetch the list feed of the worksheet.
                URL listFeedUrl = worksheet.getListFeedUrl();
                ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

                //*System.out.println(listFeed.getEntries().size());
                //  ListEntry row = listFeed.getEntries().get(0);
                // Set<String> columnHeadings = row.getCustomElements().getTags();

                ListEntry row = new ListEntry();
                row.getCustomElements().setValueLocal("Timestamp", new Date().toString());
                row.getCustomElements().setValueLocal("ExpenseReason", description);
                row.getCustomElements().setValueLocal("Anupam", anupamAmount.toString());
                row.getCustomElements().setValueLocal("Anurag", anuragAmount.toString());
                row.getCustomElements().setValueLocal("Arpana", arpanaAmount.toString());
                row.getCustomElements().setValueLocal("TotalAmount", amount);
                row.getCustomElements().setValueLocal("EntryBy", "ankitprahladsoni@gmail.com");
                row.getCustomElements().setValueLocal("SpentBy", paidBy);
                // Send the new row to the API for insertion.
                row = service.insert(listFeedUrl, row);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

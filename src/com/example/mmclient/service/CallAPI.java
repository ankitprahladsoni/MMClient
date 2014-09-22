package com.example.mmclient.service;

import android.os.AsyncTask;

import com.example.mmclient.model.AuthPreferences;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class CallAPI extends AsyncTask<String,String,Void> {

	public final static String apiURL = "https://docs.google.com/forms/d/1ofJB5V1sugFMkdUvaarPo-8uo0j_oBNTZFSWEg_b3QQ/formResponse?";

	private BigDecimal totalAmount = BigDecimal.ZERO;
	private BigDecimal anupamAmount = BigDecimal.ZERO;
	private BigDecimal anuragAmount = BigDecimal.ZERO;
	private BigDecimal arpanaAmount = BigDecimal.ZERO;

	public String spentBy;
    public String description;
    AuthPreferences authPreferences;

    public CallAPI(AuthPreferences authPreferences, String description, BigDecimal anupamAmount, BigDecimal arpanaAmount, BigDecimal anuragAmount, BigDecimal amount, String spentBy) {
    this.description=description;
        this.anupamAmount=anupamAmount;
        this.arpanaAmount=arpanaAmount;
        this.anuragAmount=anuragAmount;
        this.totalAmount=amount;
        this.spentBy=spentBy;
        this.authPreferences = authPreferences;
    }

    @Override
    protected Void doInBackground(String... arg0) {

        String token = authPreferences.getToken();
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
        SpreadsheetFeed feed;
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

            ListEntry row = new ListEntry();
            row.getCustomElements().setValueLocal("Timestamp", new Date().toString());
            row.getCustomElements().setValueLocal("ExpenseReason", description);
            row.getCustomElements().setValueLocal("Anupam", anupamAmount.toString());
            row.getCustomElements().setValueLocal("Anurag", anuragAmount.toString());
            row.getCustomElements().setValueLocal("Arpana", arpanaAmount.toString());
            row.getCustomElements().setValueLocal("TotalAmount", totalAmount.toString());
            row.getCustomElements().setValueLocal("EntryBy", authPreferences.getEmail());
            row.getCustomElements().setValueLocal("SpentBy", spentBy);
            service.insert(listFeedUrl, row);
        } catch (IOException | ServiceException e) {
            e.printStackTrace();
        }
        return null;
    }
}

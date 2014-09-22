package com.example.mmclient.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.example.mmclient.model.AuthPreferences;
import com.example.mmclient.service.CallAPI;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * Activity class for Submit Amount screen.
 *
 * @author SWAPNIL
 */
public class SubmitAmountActivity extends NavDrawerActivity {

    BigDecimal anupamAmount = BigDecimal.ZERO;
    BigDecimal anuragAmount = BigDecimal.ZERO;
    BigDecimal arpanaAmount = BigDecimal.ZERO;
    String paidBy;
    String description;
    String amount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_submit_amount, null, false);
        mDrawerLayout.addView(contentView, 0);

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
        Spinner memberDropDown = (Spinner) findViewById(R.id.spendById);
         paidBy = memberDropDown.getSelectedItem().toString();
        new CallAPI(new AuthPreferences(this),expense.getText().toString(), anupamAmount, arpanaAmount, anuragAmount, new BigDecimal(amount), paidBy).execute();

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

}

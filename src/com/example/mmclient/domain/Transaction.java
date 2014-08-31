/**
 * 
 */
package com.example.mmclient.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SWAPNIL
 * 
 */
public class Transaction {
	private String name;
	private BigDecimal totalAmount = BigDecimal.ZERO;
	private List<MemberAndAmount> memberAndAmounts = new ArrayList<MemberAndAmount>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<MemberAndAmount> getMemberAndAmounts() {
		return memberAndAmounts;
	}

	public void setMemberAndAmounts(List<MemberAndAmount> memberAndAmounts) {
		this.memberAndAmounts = memberAndAmounts;
	}

}

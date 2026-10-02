package com.netbanking.ejb;

import javax.ejb.Stateless;

@Stateless
public class LoanCalculatorBean {

    public double calculateInterest(double amount,
            double rate,
            int years) {

        return (amount * rate * years) / 100;
    }

    public double calculateTotalAmount(double amount,
            double rate,
            int years) {

        double interest =
                calculateInterest(amount, rate, years);

        return amount + interest;
    }
}
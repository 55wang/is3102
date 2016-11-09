/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import java.util.List;
import java.util.Stack;
import java.util.Vector;

/**
 *
 * @author leiyang
 */
public class GenerateAccountAndCCNumber {
    /*
     * Javascript credit card number generator Copyright (C) 2006-2012 Graham King
     *
     * This program is free software; you can redistribute it and/or modify it
     * under the terms of the GNU General Public License as published by the
     * Free Software Foundation; either version 2 of the License, or (at your
     * option) any later version.
     *
     * This program is distributed in the hope that it will be useful, but
     * WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
     * Public License for more details.
     *
     * You should have received a copy of the GNU General Public License along
     * with this program; if not, write to the Free Software Foundation, Inc.,
     * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
     *
     * www.darkcoding.net
     */

    public static void main(String[] args) {
    }


    static String strrev(String str) {
        if (str == null) {
            return "";
        }
        String revstr = "";
        for (int i = str.length() - 1; i >= 0; i--) {
            revstr += str.charAt(i);
        }

        return revstr;
    }

    /*
     * 'prefix' is the start of the CC number as a string, any number of digits.
     * 'length' is the length of the CC number to generate. Typically 13 or 16
     */
    static String completed_number(String prefix, int length) {

        String ccnumber = prefix;

		// generate digits
        while (ccnumber.length() < (length - 1)) {
            ccnumber += new Double(Math.floor(Math.random() * 10)).intValue();
        }

		// reverse number and convert to int
        String reversedCCnumberString = strrev(ccnumber);

        List<Integer> reversedCCnumberList = new Vector<Integer>();
        for (int i = 0; i < reversedCCnumberString.length(); i++) {
            reversedCCnumberList.add(new Integer(String
                    .valueOf(reversedCCnumberString.charAt(i))));
        }

		// calculate sum
        int sum = 0;
        int pos = 0;

        Integer[] reversedCCnumber = reversedCCnumberList
                .toArray(new Integer[reversedCCnumberList.size()]);
        while (pos < length - 1) {

            int odd = reversedCCnumber[pos] * 2;
            if (odd > 9) {
                odd -= 9;
            }

            sum += odd;

            if (pos != (length - 2)) {
                sum += reversedCCnumber[pos + 1];
            }
            pos += 2;
        }

		// calculate check digit
        int checkdigit = new Double(
                ((Math.floor(sum / 10) + 1) * 10 - sum) % 10).intValue();
        ccnumber += checkdigit;

        return ccnumber;

    }

    public static String generateReferenceNumber(int length) {
        return completed_number("",length );
    }


}

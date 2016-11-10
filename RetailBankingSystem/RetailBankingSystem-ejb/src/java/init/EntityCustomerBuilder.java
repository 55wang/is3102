/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.common.NewCustomerSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import entity.customer.Customer;
import entity.customer.MainAccount;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils;
import server.utilities.HashPwdUtils;
import util.exception.common.DuplicateMainAccountExistException;
import util.exception.common.UpdateMainAccountException;

/**
 *
 * @author wang
 */
@LocalBean
@Stateless
public class EntityCustomerBuilder {

    @EJB
    private NewCustomerSessionBeanLocal newCustomerSessionBean;
    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;

    public MainAccount initCustomer() {

        try {

            String u0 = ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID_0;
            String p0 = HashPwdUtils.hashPwd("password");

            Customer c0 = new Customer(); //Customer Group 1: Young, or lower income
            c0.setAddress("23 BALAM, 08-08"); //make it a bit more real
            try {
                c0.setBirthDay(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-1993"));
            } catch (Exception ex) {
            }
            c0.setEmail("hxh307@hotmail.com");
            c0.setFirstname("Yang");
            c0.setLastname("Lei");
            c0.setFullName("Lei Yang");
            c0.setOccupation(EnumUtils.Occupation.FREELANCER);
            c0.setGender(EnumUtils.Gender.MALE); // pls modify gender to enum type
            c0.setIdentityType(EnumUtils.IdentityType.NRIC);
            c0.setIdentityNumber("S7654321Z");
            c0.setIncome(EnumUtils.Income.FROM_6000_TO_8000);
            c0.setActualIncome(3000.0);
            c0.setEducation(EnumUtils.Education.UNIVERSITY);
            c0.setEmploymentStatus(EnumUtils.EmploymentStatus.EMPLOYEE);
            c0.setPhone("98238750"); //must use real phone number as we need sms code
            c0.setPostalCode("654321");
            c0.setIndustry(EnumUtils.Industry.RETAIL);
            c0.setNationality(EnumUtils.Nationality.SINGAPORE);
            c0.setMaritalStatus(EnumUtils.MaritalStatus.SINGLE);

            MainAccount ma0 = new MainAccount();
            ma0.setUserID(u0);
            ma0.setPassword(p0);
            ma0.setStatus(EnumUtils.StatusType.ACTIVE);
            ma0 = mainAccountSessionBean.createMainAccount(ma0);
            c0 = newCustomerSessionBean.createCustomer(c0);

            ma0.setCustomer(c0);
            c0.setMainAccount(ma0);
            newCustomerSessionBean.updateCustomer(c0);
            mainAccountSessionBean.updateMainAccount(ma0);

            String u1 = ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID_1;
            String p1 = HashPwdUtils.hashPwd("password");

            Customer c = new Customer(); //Customer Group 1: Young, or lower income
            c.setAddress("10 Punggol, 08-08"); //make it a bit more real
            try {
                c.setBirthDay(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-1993"));
            } catch (Exception ex) {
            }
            c.setEmail("wangzhe.lynx@gmail.com");
            c.setFirstname("Yifan");
            c.setLastname("Chen");
            c.setFullName("Chen Yifan");
            c.setOccupation(EnumUtils.Occupation.FREELANCER);
            c.setGender(EnumUtils.Gender.MALE); // pls modify gender to enum type
            c.setIdentityType(EnumUtils.IdentityType.NRIC);
            c.setIdentityNumber("S1234567Z");
            c.setIncome(EnumUtils.Income.FROM_2000_TO_4000);
            c.setActualIncome(3000.0);
            c.setEducation(EnumUtils.Education.DIPLOMA);
            c.setEmploymentStatus(EnumUtils.EmploymentStatus.EMPLOYEE);
            c.setPhone("81567758"); //must use real phone number as we need sms code
            c.setPostalCode("654321");
            c.setIndustry(EnumUtils.Industry.RETAIL);
            c.setNationality(EnumUtils.Nationality.SINGAPORE);
            c.setMaritalStatus(EnumUtils.MaritalStatus.SINGLE);

            MainAccount ma = new MainAccount();
            ma.setUserID(u1);
            ma.setPassword(p1);
            ma.setStatus(EnumUtils.StatusType.ACTIVE);
            ma = mainAccountSessionBean.createMainAccount(ma);
            c = newCustomerSessionBean.createCustomer(c);

            ma.setCustomer(c);
            c.setMainAccount(ma);
            newCustomerSessionBean.updateCustomer(c);
            mainAccountSessionBean.updateMainAccount(ma);

            String u2 = ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID_2;
            String p2 = HashPwdUtils.hashPwd("password");

            Customer c2 = new Customer(); //Cutomer Group 2: transaction intensive
            c2.setAddress("19 Tanglin Road, 131-1-1");
            try {
                c2.setBirthDay(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-1989"));
            } catch (Exception ex) {
            }
            c2.setEmail("ripple0204@gmail.com");
            c2.setLastname("Alice");
            c2.setFirstname("Lee");
            c2.setFullName("Lee Alice");
            c2.setOccupation(EnumUtils.Occupation.EXECUTIVE);
            c2.setGender(EnumUtils.Gender.FEMALE);
            c2.setIdentityType(EnumUtils.IdentityType.NRIC);
            c2.setIdentityNumber("S0000002Z");
            c2.setIncome(EnumUtils.Income.FROM_4000_TO_6000);
            c2.setActualIncome(6000.0);
            c2.setEducation(EnumUtils.Education.UNIVERSITY);
            c2.setEmploymentStatus(EnumUtils.EmploymentStatus.EMPLOYEE);
            c2.setIndustry(EnumUtils.Industry.HOTEL_RESTAURANT);
            c2.setNationality(EnumUtils.Nationality.SINGAPORE); //enum type if possible
            c2.setPhone("83193049"); //must use real phone number as we need sms code
            c2.setPostalCode("654302");
            c2.setMaritalStatus(EnumUtils.MaritalStatus.SINGLE);

            MainAccount ma2 = new MainAccount();
            ma2.setUserID(u2);
            ma2.setPassword(p2);
            ma2.setStatus(EnumUtils.StatusType.ACTIVE);
            ma2 = mainAccountSessionBean.createMainAccount(ma2);
            c2 = newCustomerSessionBean.createCustomer(c2);

            ma2.setCustomer(c2);
            c2.setMainAccount(ma2);
            newCustomerSessionBean.updateCustomer(c2);
            mainAccountSessionBean.updateMainAccount(ma2);

            String u3 = ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID_3;
            String p3 = HashPwdUtils.hashPwd("password");

            Customer c3 = new Customer(); //Customer Group 3: higher income, Loan demanded
            c3.setAddress("9 Thomson Road, 9-1-B"); //make it a bit more real
            try {
                c3.setBirthDay(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-1979"));
            } catch (Exception ex) {
            }
            c3.setEmail("sunyuxuan123@gmail.com");
            c3.setLastname("Jack");
            c3.setFirstname("Woon");
            c3.setFullName("Woon Jack");
            c3.setOccupation(EnumUtils.Occupation.OTHERS);
            c3.setGender(EnumUtils.Gender.MALE);
            c3.setIdentityType(EnumUtils.IdentityType.NRIC);
            c3.setIdentityNumber("S0000003Z");
            c3.setIncome(EnumUtils.Income.FROM_6000_TO_8000);
            c3.setActualIncome(8000.0);
            c3.setEducation(EnumUtils.Education.UNIVERSITY);
            c3.setEmploymentStatus(EnumUtils.EmploymentStatus.EMPLOYEE);
            c3.setIndustry(EnumUtils.Industry.SHIPPING_TRANSPORT);
            c3.setNationality(EnumUtils.Nationality.SINGAPORE);
            c3.setPhone("85404279");
            c3.setPostalCode("114302");
            c3.setMaritalStatus(EnumUtils.MaritalStatus.MARRIED);

            MainAccount ma3 = new MainAccount();
            ma3.setUserID(u3);
            ma3.setPassword(p3);
            ma3.setStatus(EnumUtils.StatusType.ACTIVE);
            ma3 = mainAccountSessionBean.createMainAccount(ma3);
            c3 = newCustomerSessionBean.createCustomer(c3);

            ma3.setCustomer(c3);
            c3.setMainAccount(ma3);
            newCustomerSessionBean.updateCustomer(c3);
            mainAccountSessionBean.updateMainAccount(ma3);

            String u4 = ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID_4;
            String p4 = HashPwdUtils.hashPwd("password");

            Customer c4 = new Customer(); //Customer group 4: wealthy customer
            c4.setAddress("3 Sim Lim Avenue, 898B-501"); //make it a bit more real
            try {
                c4.setBirthDay(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-1970"));
            } catch (Exception ex) {
            }
            c4.setEmail("raymondlei90s@gmail.com");
            c4.setLastname("Raymond");
            c4.setFirstname("Lei");
            c4.setFullName("Lei Raymond");
            c4.setOccupation(EnumUtils.Occupation.SUPERVISOR);
            c4.setGender(EnumUtils.Gender.MALE);
            c4.setIdentityType(EnumUtils.IdentityType.NRIC);
            c4.setIdentityNumber("S0000004Z");
            c4.setIncome(EnumUtils.Income.OVER_10000);
            c4.setActualIncome(15000.0);
            c4.setEducation(EnumUtils.Education.POSTGRAD);
            c4.setEmploymentStatus(EnumUtils.EmploymentStatus.EMPLOYEE);
            c4.setIndustry(EnumUtils.Industry.IT_TELCO);
            c4.setNationality(EnumUtils.Nationality.SINGAPORE);
            c4.setPhone("94761895");
            c4.setPostalCode("224302");
            c4.setMaritalStatus(EnumUtils.MaritalStatus.MARRIED);

            MainAccount ma4 = new MainAccount();
            ma4.setUserID(u4);
            ma4.setPassword(p4);
            ma4.setStatus(EnumUtils.StatusType.ACTIVE);
            ma4 = mainAccountSessionBean.createMainAccount(ma4);
            c4 = newCustomerSessionBean.createCustomer(c4);

            ma4.setCustomer(c4);
            c4.setMainAccount(ma4);
            newCustomerSessionBean.updateCustomer(c4);
            mainAccountSessionBean.updateMainAccount(ma4);

            String u5 = ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID_5;
            String p5 = HashPwdUtils.hashPwd("password");

            Customer c5 = new Customer();
            c5.setAddress("28 West Coast Road, B-2"); //make it a bit more real
            try {
                c5.setBirthDay(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-1970"));
            } catch (Exception ex) {
            }
            c5.setEmail("lei_flash@hotmail.com");
            c5.setLastname("Low");
            c5.setFirstname("Coco");
            c5.setFullName("Low Coco");
            c5.setOccupation(EnumUtils.Occupation.MANAGERIAL);
            c5.setGender(EnumUtils.Gender.FEMALE);
            c5.setIdentityType(EnumUtils.IdentityType.NRIC);
            c5.setIdentityNumber("S0000005Z");
            c5.setIncome(EnumUtils.Income.FROM_2000_TO_4000);
            c5.setActualIncome(3000.0);
            c5.setEducation(EnumUtils.Education.UNIVERSITY);
            c5.setEmploymentStatus(EnumUtils.EmploymentStatus.EMPLOYEE);
            c5.setIndustry(EnumUtils.Industry.ENTERTAINMENT);
            c5.setNationality(EnumUtils.Nationality.SINGAPORE);
            c5.setPhone("94761895");
            c5.setPostalCode("224302");
            c5.setMaritalStatus(EnumUtils.MaritalStatus.MARRIED);

            MainAccount ma5 = new MainAccount();
            ma5.setUserID(u5);
            ma5.setPassword(p5);
            ma5.setStatus(EnumUtils.StatusType.ACTIVE);
            ma5 = mainAccountSessionBean.createMainAccount(ma5);
            c5 = newCustomerSessionBean.createCustomer(c5);

            ma5.setCustomer(c5);
            c5.setMainAccount(ma5);
            newCustomerSessionBean.updateCustomer(c5);
            mainAccountSessionBean.updateMainAccount(ma5);

            String u6 = "c0000006";
            String p6 = HashPwdUtils.hashPwd("password");

            Customer c6 = new Customer();
            c6.setAddress("67 Ang Mo Kio Avenue, 256-1"); //make it a bit more real
            c6.setBirthDay(new Date()); //make some real birthday.
            c6.setEmail("daisyqiu@gmail.com");
            c6.setFirstname("Xiaqing");
            c6.setOccupation(EnumUtils.Occupation.SALES);
            c6.setGender(EnumUtils.Gender.MALE); // pls modify gender to enum type
            c6.setIdentityType(EnumUtils.IdentityType.NRIC);
            c6.setIdentityNumber("S1209183Z");
            c6.setIncome(EnumUtils.Income.FROM_2000_TO_4000);
            c6.setEducation(EnumUtils.Education.UNIVERSITY);
            c6.setLastname("Qiu");
            c6.setEmploymentStatus(EnumUtils.EmploymentStatus.EMPLOYEE);
            c6.setNationality(EnumUtils.Nationality.INDONESIA); //enum type if possible
            c6.setPhone("81509281"); //must use real phone number as we need sms code
            c6.setPostalCode("118921");

            MainAccount ma6 = new MainAccount();
            ma6.setUserID(u6);
            ma6.setPassword(p6);
            ma6.setStatus(EnumUtils.StatusType.ACTIVE);
            mainAccountSessionBean.createMainAccount(ma6);
            newCustomerSessionBean.createCustomer(c6);

            ma6.setCustomer(c6);
            c6.setMainAccount(ma6);
            newCustomerSessionBean.updateCustomer(c6);
            mainAccountSessionBean.updateMainAccount(ma6);

            String u7 = "c0000007";
            String p7 = HashPwdUtils.hashPwd("password");

            Customer c7 = new Customer();
            c7.setAddress("555 Clementi Road, 419-807"); //make it a bit more real
            c7.setBirthDay(new Date()); //make some real birthday.
            c7.setEmail("daisykoo@gmail.com");
            c7.setFirstname("Daisy");
            c7.setOccupation(EnumUtils.Occupation.FREELANCER);
            c7.setGender(EnumUtils.Gender.MALE); // pls modify gender to enum type
            c7.setIdentityType(EnumUtils.IdentityType.NRIC);
            c7.setIdentityNumber("S1290528Z");
            c7.setIncome(EnumUtils.Income.FROM_2000_TO_4000);
            c7.setEducation(EnumUtils.Education.POSTGRAD);
            c7.setEmploymentStatus(EnumUtils.EmploymentStatus.EMPLOYEE);
            c7.setLastname("Koo");
            c7.setNationality(EnumUtils.Nationality.INDONESIA); //enum type if possible
            c7.setPhone("91027903"); //must use real phone number as we need sms code
            c7.setPostalCode("002987");

            MainAccount ma7 = new MainAccount();
            ma7.setUserID(u7);
            ma7.setPassword(p7);
            ma7.setStatus(EnumUtils.StatusType.ACTIVE);
            mainAccountSessionBean.createMainAccount(ma7);
            newCustomerSessionBean.createCustomer(c7);

            ma7.setCustomer(c7);
            c7.setMainAccount(ma7);
            newCustomerSessionBean.updateCustomer(c7);
            mainAccountSessionBean.updateMainAccount(ma7);

            String u8 = "c0000008";
            String p8 = HashPwdUtils.hashPwd("password");

            Customer c8 = new Customer();
            c8.setAddress("9 Hougang Road, 193-303"); //make it a bit more real
            c8.setBirthDay(new Date()); //make some real birthday.
            c8.setEmail("vincentlee@gmail.com");
            c8.setFirstname("Vincent");
            c8.setOccupation(EnumUtils.Occupation.DIPLOMAT);
            c8.setGender(EnumUtils.Gender.MALE); // pls modify gender to enum type
            c8.setIdentityType(EnumUtils.IdentityType.NRIC);
            c8.setIdentityNumber("S1209123Z");
            c8.setIncome(EnumUtils.Income.FROM_6000_TO_8000);
            c8.setEducation(EnumUtils.Education.UNIVERSITY);
            c8.setEmploymentStatus(EnumUtils.EmploymentStatus.EMPLOYEE);
            c8.setLastname("Lee");
            c8.setNationality(EnumUtils.Nationality.SINGAPORE); //enum type if possible
            c8.setPhone("99910888"); //must use real phone number as we need sms code
            c8.setPostalCode("020988");

            MainAccount ma8 = new MainAccount();
            ma8.setUserID(u8);
            ma8.setPassword(p8);
            ma8.setStatus(EnumUtils.StatusType.ACTIVE);
            mainAccountSessionBean.createMainAccount(ma8);
            newCustomerSessionBean.createCustomer(c8);

            ma8.setCustomer(c8);
            c8.setMainAccount(ma8);
            newCustomerSessionBean.updateCustomer(c8);
            mainAccountSessionBean.updateMainAccount(ma8);

            String u9 = "c0000009";
            String p9 = HashPwdUtils.hashPwd("password");

            Customer c9 = new Customer();
            c9.setAddress("17 South Buona Vista Road, 29-905"); //make it a bit more real
            c9.setBirthDay(new Date()); //make some real birthday.
            c9.setEmail("cassychoi@gmail.com");
            c9.setFirstname("Cassy");
            c9.setOccupation(EnumUtils.Occupation.SELF_EMPLOYED);
            c9.setGender(EnumUtils.Gender.FEMALE); // pls modify gender to enum type
            c9.setIdentityType(EnumUtils.IdentityType.NRIC);
            c9.setIdentityNumber("S3334567Z");
            c9.setIncome(EnumUtils.Income.FROM_6000_TO_8000);
            c9.setEducation(EnumUtils.Education.DIPLOMA);
            c9.setEmploymentStatus(EnumUtils.EmploymentStatus.EMPLOYEE);
            c9.setLastname("Choi");
            c9.setNationality(EnumUtils.Nationality.UNITED_STATES); //enum type if possible
            c9.setPhone("80031182"); //must use real phone number as we need sms code
            c9.setPostalCode("019090");

            MainAccount ma9 = new MainAccount();
            ma9.setUserID(u9);
            ma9.setPassword(p9);
            ma9.setStatus(EnumUtils.StatusType.ACTIVE);
            mainAccountSessionBean.createMainAccount(ma9);
            newCustomerSessionBean.createCustomer(c9);

            ma9.setCustomer(c9);
            c9.setMainAccount(ma9);
            newCustomerSessionBean.updateCustomer(c9);
            mainAccountSessionBean.updateMainAccount(ma9);

            return ma;

        } catch (DuplicateMainAccountExistException | UpdateMainAccountException e) {
            System.out.println("Exception, not returning mainaccount");
            return null;
        }
    }

}

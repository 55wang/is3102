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
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils;
import server.utilities.HashPwdUtils;

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

    public void initCustomer() {
        String p = HashPwdUtils.hashPwd("password");

        Customer c = new Customer();
        c.setAddress("some fake address"); //make it a bit more real
        c.setBirthDay(new Date()); //make some real birthday.
        c.setEmail("wangzhe.lynx@gmail.com");
        c.setFirstname("Yifan");
        c.setGender(EnumUtils.Gender.MALE); // pls modify gender to enum type
        c.setIdentityType(EnumUtils.IdentityType.NRIC);
        c.setIdentityNumber("S1234567Z");
        c.setIncome(EnumUtils.Income.FROM_2000_TO_4000);
        c.setEducation(EnumUtils.Education.DIPLOMA);
        c.setLastname("Chen");
        c.setNationality(EnumUtils.Nationality.CHINA); //enum type if possible
        c.setPhone("81567758"); //must use real phone number as we need sms code
        c.setPostalCode("654321");

        MainAccount ma = new MainAccount();
        ma.setUserID(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID);
        ma.setPassword(p);
        ma.setStatus(EnumUtils.StatusType.ACTIVE);
        ma = mainAccountSessionBean.createMainAccount(ma);
        c = newCustomerSessionBean.createCustomer(c);

        ma.setCustomer(c);
        c.setMainAccount(ma);
        newCustomerSessionBean.updateCustomer(c);

        String u2 = "c0000002";
        String p2 = HashPwdUtils.hashPwd("password");

        Customer c2 = new Customer();
        c2.setAddress("19 Tanglin Road, 131-1-1"); //make it a bit more real
        c2.setBirthDay(new Date()); //make some real birthday.
        c2.setEmail("wangzhe.lynx2@gmail.com");
        c2.setFirstname("Zhe");
        c2.setGender(EnumUtils.Gender.MALE); // pls modify gender to enum type
        c2.setIdentityType(EnumUtils.IdentityType.NRIC);
        c2.setIdentityNumber("S1234223Z");
        c2.setIncome(EnumUtils.Income.FROM_4000_TO_6000);
        c2.setEducation(EnumUtils.Education.POSTGRAD);
        c2.setLastname("Wang");
        c2.setNationality(EnumUtils.Nationality.CHINA_HONG_KONG); //enum type if possible
        c2.setPhone("81567712"); //must use real phone number as we need sms code
        c2.setPostalCode("654302");

        MainAccount ma2 = new MainAccount();
        ma2.setUserID(u2);
        ma2.setPassword(p2);
        ma2.setStatus(EnumUtils.StatusType.ACTIVE);
        ma2 = mainAccountSessionBean.createMainAccount(ma2);
        c2 = newCustomerSessionBean.createCustomer(c2);

        ma2.setCustomer(c2);
        c2.setMainAccount(ma2);
        newCustomerSessionBean.updateCustomer(c2);

        String u3 = ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID_2;
        String p3 = HashPwdUtils.hashPwd("password");

        Customer c3 = new Customer();
        c3.setAddress("9 Thomson Road, 9-1-B"); //make it a bit more real
        c3.setBirthDay(new Date()); //make some real birthday.
        c3.setEmail("leiyang007@gmail.com");
        c3.setFirstname("Yang");
        c3.setGender(EnumUtils.Gender.FEMALE); // pls modify gender to enum type
        c3.setIdentityType(EnumUtils.IdentityType.NRIC);
        c3.setIdentityNumber("S7654321Z");
        c3.setIncome(EnumUtils.Income.FROM_6000_TO_8000);
        c3.setEducation(EnumUtils.Education.SECONDARY);
        c3.setLastname("Lei");
        c3.setNationality(EnumUtils.Nationality.GERMANY); //enum type if possible
        c3.setPhone("94761895"); //must use real phone number as we need sms code
        c3.setPostalCode("654111");

        MainAccount ma3 = new MainAccount();
        ma3.setUserID(u3);
        ma3.setPassword(p3);
        ma3.setStatus(EnumUtils.StatusType.ACTIVE);
        ma3 = mainAccountSessionBean.createMainAccount(ma3);
        c3 = newCustomerSessionBean.createCustomer(c3);

        ma3.setCustomer(c3);
        c3.setMainAccount(ma3);
        newCustomerSessionBean.updateCustomer(c3);

        String u4 = "c0000004";
        String p4 = HashPwdUtils.hashPwd("password");

        Customer c4 = new Customer();
        c4.setAddress("3 Sim Lim Avenue, 898B-501"); //make it a bit more real
        c4.setBirthDay(new Date()); //make some real birthday.
        c4.setEmail("sunyuxuan123@gmail.com");
        c4.setFirstname("Yuxuan");
        c4.setGender(EnumUtils.Gender.MALE); // pls modify gender to enum type
        c3.setIdentityType(EnumUtils.IdentityType.NRIC);
        c4.setIdentityNumber("S1243267Z");
        c4.setIncome(EnumUtils.Income.FROM_8000_TO_10000);
        c4.setEducation(EnumUtils.Education.TECHNICAL);
        c4.setLastname("Sun");
        c4.setNationality(EnumUtils.Nationality.INDONESIA); //enum type if possible
        c4.setPhone("81123558"); //must use real phone number as we need sms code
        c4.setPostalCode("621329");

        MainAccount ma4 = new MainAccount();
        ma4.setUserID(u4);
        ma4.setPassword(p4);
        ma4.setStatus(EnumUtils.StatusType.ACTIVE);
        mainAccountSessionBean.createMainAccount(ma4);
        newCustomerSessionBean.createCustomer(c4);

        ma4.setCustomer(c4);
        c4.setMainAccount(ma4);
        newCustomerSessionBean.updateCustomer(c4);

        String u5 = "c0000005";
        String p5 = HashPwdUtils.hashPwd("password");

        Customer c5 = new Customer();
        c5.setAddress("28 West Coast Road, B-2"); //make it a bit more real
        c5.setBirthDay(new Date()); //make some real birthday.
        c5.setEmail("lilitong01@gmail.com");
        c5.setFirstname("Litong");
        c5.setGender(EnumUtils.Gender.MALE); // pls modify gender to enum type
        c5.setIdentityType(EnumUtils.IdentityType.NRIC);
        c5.setIdentityNumber("S1289812Z");
        c5.setIncome(EnumUtils.Income.FROM_2000_TO_4000);
        c5.setEducation(EnumUtils.Education.OTHERS);
        c5.setLastname("Chen");
        c5.setNationality(EnumUtils.Nationality.JAPAN); //enum type if possible
        c5.setPhone("90028125");//must use real phone number as we need sms code
        c5.setPostalCode("001409");

        MainAccount ma5 = new MainAccount();
        ma5.setUserID(u5);
        ma5.setPassword(p5);
        ma5.setStatus(EnumUtils.StatusType.ACTIVE);
        mainAccountSessionBean.createMainAccount(ma5);
        newCustomerSessionBean.createCustomer(c5);

        ma5.setCustomer(c5);
        c5.setMainAccount(ma5);
        newCustomerSessionBean.updateCustomer(c5);

        String u6 = "c0000006";
        String p6 = HashPwdUtils.hashPwd("password");

        Customer c6 = new Customer();
        c6.setAddress("67 Ang Mo Kio Avenue, 256-1"); //make it a bit more real
        c6.setBirthDay(new Date()); //make some real birthday.
        c6.setEmail("daisyqiu@gmail.com");
        c6.setFirstname("Xiaqing");
        c6.setGender(EnumUtils.Gender.MALE); // pls modify gender to enum type
        c6.setIdentityType(EnumUtils.IdentityType.NRIC);
        c6.setIdentityNumber("S1209183Z");
        c6.setIncome(EnumUtils.Income.FROM_2000_TO_4000);
        c6.setEducation(EnumUtils.Education.UNIVERSITY);
        c6.setLastname("Qiu");
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

        String u7 = "c0000007";
        String p7 = HashPwdUtils.hashPwd("password");

        Customer c7 = new Customer();
        c7.setAddress("555 Clementi Road, 419-807"); //make it a bit more real
        c7.setBirthDay(new Date()); //make some real birthday.
        c7.setEmail("daisykoo@gmail.com");
        c7.setFirstname("Daisy");
        c7.setGender(EnumUtils.Gender.MALE); // pls modify gender to enum type
        c7.setIdentityType(EnumUtils.IdentityType.NRIC);
        c7.setIdentityNumber("S1290528Z");
        c7.setIncome(EnumUtils.Income.FROM_2000_TO_4000);
        c7.setEducation(EnumUtils.Education.POSTGRAD);
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

        String u8 = "c0000008";
        String p8 = HashPwdUtils.hashPwd("password");

        Customer c8 = new Customer();
        c8.setAddress("9 Hougang Road, 193-303"); //make it a bit more real
        c8.setBirthDay(new Date()); //make some real birthday.
        c8.setEmail("vincentlee@gmail.com");
        c8.setFirstname("Vincent");
        c8.setGender(EnumUtils.Gender.MALE); // pls modify gender to enum type
        c8.setIdentityType(EnumUtils.IdentityType.NRIC);
        c8.setIdentityNumber("S1209123Z");
        c8.setIncome(EnumUtils.Income.FROM_6000_TO_8000);
        c8.setEducation(EnumUtils.Education.UNIVERSITY);
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

        String u9 = "c0000009";
        String p9 = HashPwdUtils.hashPwd("password");

        Customer c9 = new Customer();
        c9.setAddress("17 South Buona Vista Road, 29-905"); //make it a bit more real
        c9.setBirthDay(new Date()); //make some real birthday.
        c9.setEmail("cassychoi@gmail.com");
        c9.setFirstname("Cassy");
        c9.setGender(EnumUtils.Gender.FEMALE); // pls modify gender to enum type
        c9.setIdentityType(EnumUtils.IdentityType.NRIC);
        c9.setIdentityNumber("S3334567Z");
        c9.setIncome(EnumUtils.Income.FROM_6000_TO_8000);
        c9.setEducation(EnumUtils.Education.DIPLOMA);
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
        
    }

}

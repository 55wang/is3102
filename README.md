IS3102 16/17 SEM1

Retail Banking Track, each team would get one same grade in the following order. Which one do u want?
A+, A-, B+, B, B-

How to import data?
run this in cmdline
mysql -u root --password=password < RecreateDatabase.sql


# ENUM Follow the Following Practices: #
	public enum UserRole {

        GENERAL_TELLER("General Teller"),
        CUSTOMER_SERVICE("Customer Service Representative"),
        LOAN_OFFICIER("Loan Officer"),
        FINANCIAL_OFFICER("Financial Officer"),
        FINANCIAL_ANALYST("Financial Analyst"),
        PRODUCT_MANAGER("Product Manager"),
        SUPER_ADMIN("Super Admin");
        
        private String value;

        UserRole(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static UserRole getEnum(String value) {
            for (UserRole v : values()) {
                if (v.getValue().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }
    }

## To convert a string back to enum: ##
	EnumType.getEnum("The String");

## To Generate a select menu list: ##
	In your managed bean:
	private List<String> selectEnums = CommonUtils.getEnumList(EnumUtils.SomeEnum.class);

# Input Format Mask #
	TODO: XiaQing Please put some examples here

# Output Format #
	TODO: Some examples
	// This displays in 0.0000% 
	<f:convertNumber type="percent" minFractionDigits="4"/>


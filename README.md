IS3102 16/17 SEM1

Retail Banking Track, each team would get one same grade in the following order. Which one do u want?
A+, A-, B+, B, B-

## How to import data? run this in cmdline ##
mysql -u root --password=password < RecreateDatabase.sql

# Git master and branch

    git checkout card
    git pull origin card
    git rebase master
    git checkout master
    git merge card
    git checkout card

# Work flow for module

    1. Create Entity Class
    2. Sketch out storyboard and edit Entity Class
    3. Implement EntitiesBuilder, create demo data, link, logic here.
    4. After all the logic is done, it consider

# Entity standard

```java
@Entity
//Optional if you want to make it as a parent
//@Inheritance(strategy=InheritanceType.JOINED) 
public /*abstract*/ class SomeClass implements Serializable {

    // basic id and timestamp
    @Id
    private Long id;
    @Temporal(value = TemporalType.TIMESTAMP)
    private final Date creationDate = new Date();

    // info
    private String someOtherInfos;
    //...more

    // mapping
    @ManyToOne(cascade = CascadeType.MERGE)
    private MainAccount mainAccount;
    //... more

    // functions
    public String getFullName() {
        // some 
    }

    // Buildin functions
    // toString
    // equal

    // getters and setters
}
```

# EJB standard

```java
    public Entity createEntity(Entity en) return entity
    public Entity getEntityById(Long id) return Entity
    public Entity updateEntity(Entity en) return Entity
    public List<Entity> getListEntityByEntity(Entity en) return List<Entity>
```

# Managed Bean Standard

```java
    @EJB // place all ejb at the most top
    private SomeSessionBeanLocal someBean;

    // Followed by other variables/attributes
    private String someField;

    // Followed by default constructor
    public SomeManagedBean(){}

    // Followed by @PostConstruct
    @PostConstruct
    public void init() {
        // some init
    }

    // Followed by some public functions
    public void someAction() {

    }

    // Followed by some private functions
    private void somePrivateFunction() {

    }

    // Last, place setters and getters at the back
    public getters and setters () {}

```
# Customer side xhtml standard
```xhtml
<?xml version='1.0' encoding='UTF-8' ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html"
      xmlns:f="http://xmlns.jcp.org/jsf/core"
      xmlns:em="http://xmlns.jcp.org/jsf/composite/emcomp"
      xmlns:p="http://primefaces.org/ui"
      xmlns:c="http://xmlns.jcp.org/jsp/jstl/core">

    <f:view>
        <h:head>
            <title>Merlion Bank</title>
            <em:cssImport></em:cssImport>
        </h:head>
        <h:body>
            <em:customerHeader context="personal" userName="#{customerHomeManagedBean.customer.firstname }"></em:customerHeader>
            <em:customerNavBar context="personal" personalPage="cards"></em:customerNavBar>
	    	<div class="customer_main_container">
				<p:panel header="<<Put header here>>">
					<h:form id="mainForm">                        
						<p:messages id="messagesStatus" redisplay="false">
			    
				 		<!-- Your Own Code Here -->
				 
				 		</p:messages>
					</h:form>
				</p:panel>              
			</div>
            <em:customerFooter></em:customerFooter>
        </h:body>
    </f:view>
</html>
```
### Change the project name accordingly ###

### Do no commit .xml and .properties file ###
git update-index --assume-unchanged filename
### If pull merge failed and aborted because of the .xml and .properties file, use the below command ###
git update-index --no-assume-unchanged filename
git status
git stash
git pull

    git update-index --assume-unchanged RetailBankingSystem/RetailBankingSystem-ejb/nbproject/build-impl.xml
    git update-index --assume-unchanged RetailBankingSystem/StaffInternalSystem/nbproject/build-impl.xml
    git update-index --assume-unchanged RetailBankingSystem/InternetBankingSystem/nbproject/build-impl.xml
    git update-index --assume-unchanged RetailBankingSystem/RetailBankingSystem-ejb/nbproject/project.properties
    git update-index --assume-unchanged RetailBankingSystem/StaffInternalSystem/nbproject/project.properties
    git update-index --assume-unchanged RetailBankingSystem/InternetBankingSystem/nbproject/project.properties
    git update-index --assume-unchanged RetailBankingSystem/RetailBankingSystem-ejb/nbproject/genfiles.properties
    git update-index --assume-unchanged RetailBankingSystem/RetailBankingSystem-ejb/nbproject/project.xml
    git update-index --assume-unchanged RetailBankingSystem/InternetBankingSystem/nbproject/build-impl.xml~
    git update-index --assume-unchanged RetailBankingSystem/RetailBankingSystem-ejb/nbproject/build-impl.xml~


# Error Handling** IMPORTANT #
Standardize Error Handingly Message from server.utilities.ConstantUtils.java

	public static final String OLD_PASSWORD_NOTMTACH = "Successful! You have reset your password. ";
    public static final String PASSWORD_CHANGE_SUCCESS = "Successful! You have reset your password. ";


# ENUM Follow the Following Practices: #

```java
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
```

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


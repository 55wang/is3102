/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.wealth;

/**
 *
 * @author wang
 */
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import static server.utilities.CommonUtils.getPrependFolderName;

public class TestR {

    public static void main(String a[]) {
        RConnection connection = null;

        try {
            /* Create a connection to Rserve instance running
             * on default port 6311
             */
            connection = new RConnection("127.0.0.1", 6311);
            String systemUser = System.getProperty("user.name");
            System.out.println(systemUser);
//            String vector = "c(1,2,3,4)";
//            connection.eval("meanVal=mean(" + vector + ")");
//            double mean = connection.eval("meanVal").asDouble();
//            System.out.println("The mean of given vector is=" + mean);

            /* Note four slashes (\\\\) in the path */
            System.out.println(getPrependFolderName() + "ConstructPortfolio.R");
            connection.eval("source('/Users/VIN-S/Documents/is3102/RCode/ConstructPortfolioModel.R')");
            int num1 = 10;
            int num2 = 20;
            int sum = connection.eval("constructPortfolioModel()").asInteger();
            System.out.println(sum);

            //Integer result = connection.eval("constructPortfolio()").asInteger();
            //System.out.println(result);
        } catch (RserveException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}

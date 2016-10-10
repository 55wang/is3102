/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.wealth;

import entity.wealth.FinancialInstrument;
import entity.wealth.FinancialInstrumentAndWeight;
import entity.wealth.InvestmentPlan;
import entity.wealth.PortfolioModel;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import server.utilities.CommonUtils;
import server.utilities.EnumUtils.FinancialInstrumentClass;
import server.utilities.EnumUtils.InvestmentRiskLevel;

/**
 *
 * @author VIN-S
 */
@Stateless
public class DesignInvestmentPlanSessionBean implements DesignInvestmentPlanSessionBeanLocal {

    @EJB
    private FinancialInstrumentSessionBeanLocal financialInstrumentSessionBean;
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public InvestmentPlan generateSuggestedInvestmentPlan(InvestmentPlan ip) {
        List<FinancialInstrument> allFinancialInstruments = financialInstrumentSessionBean.getAllFinancialInstruments();

        //Mathmatical Caculation
        //Input is wealthmanagementsubscriber's risk tolerance score, allFinancialInstruments' deviation, return and correlation
        //output is arraylist suggestedFinancialInstruments and according percentage
        //End of Mathmatical Calculation
        //use sql query here to get the weight and its returns
        Long inputRowNames = Math.round(ip.getWealthManagementSubscriber().getRiskToleranceScore() / 47.0 * 289.0);

        if (inputRowNames > 289) {
            inputRowNames = 289L;
        } else if (inputRowNames < 1) {
            inputRowNames = 1L;
        }

        System.out.println("################ this is input: " + inputRowNames);
        Query q = em.createQuery("SELECT a from PortfolioModel a WHERE a.row_names=:inSdResult");
        q.setParameter("inSdResult", Long.toString(inputRowNames));
        PortfolioModel pm = (PortfolioModel) q.getSingleResult();

        System.out.println("pm tgt_returns: " + pm.getTgt_returns());

        List<FinancialInstrumentAndWeight> suggestedFinancialInstruments = new ArrayList<FinancialInstrumentAndWeight>();

//        ArrayList<Double> modelWeights = new ArrayList<>();
//        modelWeights.add(pm.getCORPORATE_BONDS());
//        modelWeights.add(pm.getDIVIDEND_STOCKS());
//        modelWeights.add(pm.getEMERGING_MARKET_BONDS());
//        modelWeights.add(pm.getEMERGING_MARKETS());
//        modelWeights.add(pm.get);
//        modelWeights.add(pm.getUS_STOCKS());
//        modelWeights.add(pm.getUS_STOCKS());
//        modelWeights.add(pm.getUS_STOCKS());
//        modelWeights.add(pm.getUS_STOCKS());
//        modelWeights.add(pm.getUS_STOCKS());
//        modelWeights.add(pm.getUS_STOCKS());
        for (int i = 0; i < allFinancialInstruments.size(); i++) {
            if (allFinancialInstruments.get(i).getName().equals(FinancialInstrumentClass.CORPORATE_BONDS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(pm.getCORPORATE_BONDS());
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(FinancialInstrumentClass.DIVIDEND_GROWTH_STOCKS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(pm.getDIVIDEND_STOCKS());
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(FinancialInstrumentClass.EMERGING_MARKET_BONDS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(pm.getEMERGING_MARKET_BONDS());
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(FinancialInstrumentClass.EMERGING_MARKET_STOCKS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(pm.getEMERGING_MARKETS());
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(FinancialInstrumentClass.FOREIGN_DEVELOPED_STOCKS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(pm.getFOREIGN_STOCKS());
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(FinancialInstrumentClass.MUNICIPAL_BONDS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(pm.getMUNICIPAL_BONDS());
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(FinancialInstrumentClass.NATURAL_RESOURCES)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(pm.getNATURAL_RESOURCES());
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(FinancialInstrumentClass.REAL_ESTATE)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(pm.getREAL_ESTATE());
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(FinancialInstrumentClass.TREASURY_INFLATION_PROTECTED_SECURITIES)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(pm.getTIPS());
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(FinancialInstrumentClass.US_GOVERNMENT_BONDS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(pm.getUS_GOVERNMENT_BONDS());
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(FinancialInstrumentClass.US_STOCKS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(pm.getUS_STOCKS());
                suggestedFinancialInstruments.add(fiaw);
            }
        }
//        FinancialInstrumentAndWeight fiaw1 = new FinancialInstrumentAndWeight();
//        fiaw1.setFi(allFinancialInstruments.get(0));
//        fiaw1.setWeight(0.12);
//        suggestedFinancialInstruments.add(fiaw1);
//
//        FinancialInstrumentAndWeight fiaw2 = new FinancialInstrumentAndWeight();
//        fiaw2.setFi(allFinancialInstruments.get(2));
//        fiaw2.setWeight(0.32);
//        suggestedFinancialInstruments.add(fiaw2);
//
//        FinancialInstrumentAndWeight fiaw3 = new FinancialInstrumentAndWeight();
//        fiaw3.setFi(allFinancialInstruments.get(3));
//        fiaw3.setWeight(0.06);
//        suggestedFinancialInstruments.add(fiaw3);
//
//        FinancialInstrumentAndWeight fiaw4 = new FinancialInstrumentAndWeight();
//        fiaw4.setFi(allFinancialInstruments.get(6));
//        fiaw4.setWeight(0.14);
//        suggestedFinancialInstruments.add(fiaw4);
//
//        FinancialInstrumentAndWeight fiaw5 = new FinancialInstrumentAndWeight();
//        fiaw5.setFi(allFinancialInstruments.get(8));
//        fiaw5.setWeight(0.22);
//        suggestedFinancialInstruments.add(fiaw5);
//
//        FinancialInstrumentAndWeight fiaw6 = new FinancialInstrumentAndWeight();
//        fiaw6.setFi(allFinancialInstruments.get(10));
//        fiaw6.setWeight(0.14);
//        suggestedFinancialInstruments.add(fiaw6);
//
//        FinancialInstrumentAndWeight fiaw7 = new FinancialInstrumentAndWeight();
//        fiaw7.setFi(allFinancialInstruments.get(1));
//        fiaw7.setWeight(0.0);
//        suggestedFinancialInstruments.add(fiaw7);
//
//        FinancialInstrumentAndWeight fiaw8 = new FinancialInstrumentAndWeight();
//        fiaw8.setFi(allFinancialInstruments.get(4));
//        fiaw8.setWeight(0.0);
//        suggestedFinancialInstruments.add(fiaw8);
//
//        FinancialInstrumentAndWeight fiaw9 = new FinancialInstrumentAndWeight();
//        fiaw9.setFi(allFinancialInstruments.get(5));
//        fiaw9.setWeight(0.0);
//        suggestedFinancialInstruments.add(fiaw9);
//
//        FinancialInstrumentAndWeight fiaw10 = new FinancialInstrumentAndWeight();
//        fiaw10.setFi(allFinancialInstruments.get(7));
//        fiaw10.setWeight(0.0);
//        suggestedFinancialInstruments.add(fiaw10);
//
//        FinancialInstrumentAndWeight fiaw11 = new FinancialInstrumentAndWeight();
//        fiaw11.setFi(allFinancialInstruments.get(9));
//        fiaw11.setWeight(0.0);
//        suggestedFinancialInstruments.add(fiaw11);

        ip.setSystemPredictReturn(pm.getTgt_returns());
        Double predictRisk = pm.getTgt_sdresult() * (47 - 13) + 13;
        ip.setSystemPredictRisk(predictRisk);
        if (predictRisk < 18) {
            ip.setRiskLevel(InvestmentRiskLevel.LOW_RISK);
        } else if (predictRisk < 22) {
            ip.setRiskLevel(InvestmentRiskLevel.BELOW_AVERAGE_RISK);
        } else if (predictRisk < 28) {
            ip.setRiskLevel(InvestmentRiskLevel.AVERAGE_RISK);
        } else if (predictRisk < 32) {
            ip.setRiskLevel(InvestmentRiskLevel.ABOVE_AVERAGE_RISK);
        } else {
            ip.setRiskLevel(InvestmentRiskLevel.HIGH_RISK);
        }

        ip.setSuggestedFinancialInstruments(suggestedFinancialInstruments);

        em.merge(ip);
        return ip;
    }

    @Override
    public InvestmentPlan updateSuggestedInvestmentPlan(InvestmentPlan ip) {
        //Regenerate the suggested investment plan based on updated information

        //Input is wealthmanagementsubscriber's risk tolerance score, allFinancialInstruments' deviation, return and correlation
        //output is arraylist suggestedFinancialInstruments and according percentage
        //End of Mathmatical Caculation
        // is this to predict expected score?
        RConnection connection = null;
        Double sdResult = 0.0;
        Double returnResult = 0.0;
        
        try {
            /* Create a connection to Rserve instance running
             * on default port 6311
             */
            connection = new RConnection("127.0.0.1", 6311);

            String prependingPath = CommonUtils.getPrependFolderName();
            connection.eval("source('" + prependingPath + "ConstructPortfolioModel.R')");

            String parameter = "";
            for (int i = 0; i < ip.getSuggestedFinancialInstruments().size(); i++) {
                if (ip.getSuggestedFinancialInstruments().get(i).getFi().getName().equals(FinancialInstrumentClass.US_STOCKS)) {
                    parameter += ip.getSuggestedFinancialInstruments().get(i).getWeight() + ", ";
                }
            }

            for (int i = 0; i < ip.getSuggestedFinancialInstruments().size(); i++) {
                if (ip.getSuggestedFinancialInstruments().get(i).getFi().getName().equals(FinancialInstrumentClass.FOREIGN_DEVELOPED_STOCKS)) {
                    parameter += ip.getSuggestedFinancialInstruments().get(i).getWeight() + ", ";
                }
            }

            for (int i = 0; i < ip.getSuggestedFinancialInstruments().size(); i++) {
                if (ip.getSuggestedFinancialInstruments().get(i).getFi().getName().equals(FinancialInstrumentClass.EMERGING_MARKET_STOCKS)) {
                    parameter += ip.getSuggestedFinancialInstruments().get(i).getWeight() + ", ";
                }
            }

            for (int i = 0; i < ip.getSuggestedFinancialInstruments().size(); i++) {
                if (ip.getSuggestedFinancialInstruments().get(i).getFi().getName().equals(FinancialInstrumentClass.DIVIDEND_GROWTH_STOCKS)) {
                    parameter += ip.getSuggestedFinancialInstruments().get(i).getWeight() + ", ";
                }
            }

            for (int i = 0; i < ip.getSuggestedFinancialInstruments().size(); i++) {
                if (ip.getSuggestedFinancialInstruments().get(i).getFi().getName().equals(FinancialInstrumentClass.REAL_ESTATE)) {
                    parameter += ip.getSuggestedFinancialInstruments().get(i).getWeight() + ", ";
                }
            }

            for (int i = 0; i < ip.getSuggestedFinancialInstruments().size(); i++) {
                if (ip.getSuggestedFinancialInstruments().get(i).getFi().getName().equals(FinancialInstrumentClass.NATURAL_RESOURCES)) {
                    parameter += ip.getSuggestedFinancialInstruments().get(i).getWeight() + ", ";
                }
            }

            for (int i = 0; i < ip.getSuggestedFinancialInstruments().size(); i++) {
                if (ip.getSuggestedFinancialInstruments().get(i).getFi().getName().equals(FinancialInstrumentClass.TREASURY_INFLATION_PROTECTED_SECURITIES)) {
                    parameter += ip.getSuggestedFinancialInstruments().get(i).getWeight() + ", ";
                }
            }

            for (int i = 0; i < ip.getSuggestedFinancialInstruments().size(); i++) {
                if (ip.getSuggestedFinancialInstruments().get(i).getFi().getName().equals(FinancialInstrumentClass.MUNICIPAL_BONDS)) {
                    parameter += ip.getSuggestedFinancialInstruments().get(i).getWeight() + ", ";
                }
            }

            for (int i = 0; i < ip.getSuggestedFinancialInstruments().size(); i++) {
                if (ip.getSuggestedFinancialInstruments().get(i).getFi().getName().equals(FinancialInstrumentClass.CORPORATE_BONDS)) {
                    parameter += ip.getSuggestedFinancialInstruments().get(i).getWeight() + ", ";
                }
            }

            for (int i = 0; i < ip.getSuggestedFinancialInstruments().size(); i++) {
                if (ip.getSuggestedFinancialInstruments().get(i).getFi().getName().equals(FinancialInstrumentClass.EMERGING_MARKET_BONDS)) {
                    parameter += ip.getSuggestedFinancialInstruments().get(i).getWeight() + ", ";
                }
            }

            for (int i = 0; i < ip.getSuggestedFinancialInstruments().size(); i++) {
                if (ip.getSuggestedFinancialInstruments().get(i).getFi().getName().equals(FinancialInstrumentClass.US_GOVERNMENT_BONDS)) {
                    parameter += ip.getSuggestedFinancialInstruments().get(i).getWeight() + ", ";
                }
            }
            
            parameter = parameter.substring(0, parameter.length()-2);

            System.out.println(parameter);

            sdResult = connection.eval("getSdRegression(" + parameter + ")").asDouble();
            returnResult = connection.eval("getReturnRegression(" + parameter + ")").asDouble();
            System.out.println("risk: " +sdResult);
            System.out.println("return: "+returnResult);
            
        } catch (RserveException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }

        ip.setSystemPredictReturn(returnResult);
        ip.setSystemPredictRisk(sdResult);
        return ip;
    }

    @Override
    public InvestmentPlan submitSuggestedInvestmentPlan(InvestmentPlan ip) {
        em.merge(ip);

        return ip;
    }
}

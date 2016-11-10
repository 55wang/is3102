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
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import server.utilities.ConstantUtils;
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

        ip.setSystemPredictReturn(pm.getTgt_returns());
        Long predictRisk = Math.round((pm.getTgt_sdresult() - 0.12788392) * (47 - 13)/0.0403 + 13);
        ip.setSystemPredictRisk(predictRisk.intValue());
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
        ip.setSuggestedFinancialInstruments(roundWeightToOne(ip.getSuggestedFinancialInstruments()));

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
            connection = new RConnection("localhost", 6311);

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
        
        ip.setSuggestedFinancialInstruments(roundWeightToOne(ip.getSuggestedFinancialInstruments()));

        ip.setSystemPredictReturn(returnResult);
        
        Long predictRisk = Math.round((sdResult - 0.12788392) * (47 - 13)/0.0403 + 13);
        ip.setSystemPredictRisk(predictRisk.intValue());
        System.out.println("After convert: "+predictRisk);
        
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
        
        return ip;
    }

    @Override
    public InvestmentPlan submitSuggestedInvestmentPlan(InvestmentPlan ip) {
        em.merge(ip);

        return ip;
    }
    
    private List<FinancialInstrumentAndWeight> roundWeightToOne(List<FinancialInstrumentAndWeight> suggestedFinancialInstruments){
        Double sumup = 0.0;
        Double max = 0.0;
        int maxIndex = 0;
        for(int i = 0; i< suggestedFinancialInstruments.size();i++){
            sumup += suggestedFinancialInstruments.get(i).getWeight();
            if(max < suggestedFinancialInstruments.get(i).getWeight()){
                max = suggestedFinancialInstruments.get(i).getWeight();
                maxIndex = i;
            }
        }
        if(sumup > 1.0)
            suggestedFinancialInstruments.get(maxIndex).setWeight(max-(sumup-1.0));
        else if(sumup < 1.0)
            suggestedFinancialInstruments.get(maxIndex).setWeight(max+(sumup-1.0));
        
        return suggestedFinancialInstruments;
    }
}

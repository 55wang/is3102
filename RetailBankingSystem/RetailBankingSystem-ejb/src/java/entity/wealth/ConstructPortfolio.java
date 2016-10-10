/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.wealth;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author wang
 */
@Entity
public class ConstructPortfolio implements Serializable {

    //dont change the variable type as it is according to R
    @Id
    private String row_names;
    private Double tgt_returns;
    private Double tgt_sdresult;
    private Double US_STOCKS;
    private Double FOREIGN_STOCKS;
    private Double EMERGING_MARKETS;
    private Double DIVIDEND_STOCKS;
    private Double REAL_ESTATE;
    private Double NATURAL_RESOURCES;
    private Double TIPS;
    private Double MUNICIPAL_BONDS;
    private Double CORPORATE_BONDS;
    private Double EMERGING_MARKET_BONDS;
    private Double US_GOVERNMENT_BONDS;

    public String getRow_names() {
        return row_names;
    }

    public void setRow_names(String row_names) {
        this.row_names = row_names;
    }

    public Double getTgt_returns() {
        return tgt_returns;
    }

    public void setTgt_returns(Double tgt_returns) {
        this.tgt_returns = tgt_returns;
    }

    public Double getTgt_sdresult() {
        return tgt_sdresult;
    }

    public void setTgt_sdresult(Double tgt_sdresult) {
        this.tgt_sdresult = tgt_sdresult;
    }

    public Double getUS_STOCKS() {
        return US_STOCKS;
    }

    public void setUS_STOCKS(Double US_STOCKS) {
        this.US_STOCKS = US_STOCKS;
    }

    public Double getFOREIGN_STOCKS() {
        return FOREIGN_STOCKS;
    }

    public void setFOREIGN_STOCKS(Double FOREIGN_STOCKS) {
        this.FOREIGN_STOCKS = FOREIGN_STOCKS;
    }

    public Double getEMERGING_MARKETS() {
        return EMERGING_MARKETS;
    }

    public void setEMERGING_MARKETS(Double EMERGING_MARKETS) {
        this.EMERGING_MARKETS = EMERGING_MARKETS;
    }

    public Double getDIVIDEND_STOCKS() {
        return DIVIDEND_STOCKS;
    }

    public void setDIVIDEND_STOCKS(Double DIVIDEND_STOCKS) {
        this.DIVIDEND_STOCKS = DIVIDEND_STOCKS;
    }

    public Double getREAL_ESTATE() {
        return REAL_ESTATE;
    }

    public void setREAL_ESTATE(Double REAL_ESTATE) {
        this.REAL_ESTATE = REAL_ESTATE;
    }

    public Double getNATURAL_RESOURCES() {
        return NATURAL_RESOURCES;
    }

    public void setNATURAL_RESOURCES(Double NATURAL_RESOURCES) {
        this.NATURAL_RESOURCES = NATURAL_RESOURCES;
    }

    public Double getTIPS() {
        return TIPS;
    }

    public void setTIPS(Double TIPS) {
        this.TIPS = TIPS;
    }

    public Double getMUNICIPAL_BONDS() {
        return MUNICIPAL_BONDS;
    }

    public void setMUNICIPAL_BONDS(Double MUNICIPAL_BONDS) {
        this.MUNICIPAL_BONDS = MUNICIPAL_BONDS;
    }

    public Double getCORPORATE_BONDS() {
        return CORPORATE_BONDS;
    }

    public void setCORPORATE_BONDS(Double CORPORATE_BONDS) {
        this.CORPORATE_BONDS = CORPORATE_BONDS;
    }

    public Double getEMERGING_MARKET_BONDS() {
        return EMERGING_MARKET_BONDS;
    }

    public void setEMERGING_MARKET_BONDS(Double EMERGING_MARKET_BONDS) {
        this.EMERGING_MARKET_BONDS = EMERGING_MARKET_BONDS;
    }

    public Double getUS_GOVERNMENT_BONDS() {
        return US_GOVERNMENT_BONDS;
    }

    public void setUS_GOVERNMENT_BONDS(Double US_GOVERNMENT_BONDS) {
        this.US_GOVERNMENT_BONDS = US_GOVERNMENT_BONDS;
    }
}

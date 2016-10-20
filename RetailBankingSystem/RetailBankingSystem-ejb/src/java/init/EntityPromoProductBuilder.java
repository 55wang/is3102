/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.utils.UtilsSessionBeanLocal;
import entity.card.product.PromoProduct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import server.utilities.EnumUtils;

/**
 *
 * @author wang
 */
@LocalBean
@Stateless
public class EntityPromoProductBuilder {

    @EJB
    private UtilsSessionBeanLocal utilsBean;

    public PromoProduct initPromoProduct(PromoProduct demoPromoProduct) {
        PromoProduct p1 = new PromoProduct();
        p1.setName("Golden Village 1 Movie Ticket (Any day)");
        p1.setTerms("• Voucher will be mail within 7 days from the date of redemption. \n"
                + "• Valid for movie screenings at any Golden Village cinemas at anytime. \n"
                + "• Not valid for premium priced films, 3D films, Gold Class, Cinema Europa, group screenings, marathons, festivals and premieres. \n"
                + "• Not valid for internet, phone, AXS, Imode bookings. \n"
                + "• Vouchers do not guarantee seating and must be exchanged for movie tickets. \n"
                + "• Tickets printed out is not refundable. \n"
                + "• Maximum 10 vouchers allowed per redemption. \n"
                + "• Movie date should not be later than expiry date on voucher.");
        p1.setPoints(3500.0);
        p1.setType(EnumUtils.PromoType.VOUCHER);
        demoPromoProduct = (PromoProduct) utilsBean.persist(p1);

        PromoProduct p2 = new PromoProduct();
        p2.setName("Crabtree & Evelyn S$25 Voucher");
        p2.setTerms("• Usage of multiple vouchers allowed.\n"
                + "• Voucher is not valid for items on special offer, sale or discount.\n"
                + "• Crabtree & Evelyn reserves the right to make the final decisions concerning the promotion.\n"
                + "• Voucher is not exchangeable for cash and any unused value of the Voucher will be forfeited.\n"
                + "• Any balance payment must be made with your DBS Credit Card.\n"
                + "• Other terms & conditions apply. ");
        p2.setPoints(5500.0);
        p2.setType(EnumUtils.PromoType.VOUCHER);
        utilsBean.persist(p2);

        PromoProduct p3 = new PromoProduct();
        p3.setName("Best Denki S$50 Voucher");
        p3.setTerms("• Usage of multiple vouchers is allowed.\n"
                + "• Voucher is not valid for use with other Gift Vouchers, discounts or on-going promotions.\n"
                + "• Voucher is not exchangeable for cash and any unused value of the Voucher will be forfeited.\n"
                + "• Any balance payment must be made with your DBS Credit Card.\n"
                + "• Other terms & conditions apply.");
        p3.setPoints(9500.0);
        p3.setType(EnumUtils.PromoType.VOUCHER);
        utilsBean.persist(p3);

        PromoProduct p4 = new PromoProduct();
        p4.setName("Takashimaya Department Store S$100 Voucher");
        p4.setTerms("• Voucher will expire 3 months from date of redemption or date of issue. \n"
                + "• Voucher will be mail within 7 days from the date of redemption. \n"
                + "• Voucher is not exchangeable for cash or Takashimaya Gift Voucher and any unused value of the Voucher will be forfeited.  \n"
                + "• Usage of multiple vouchers is allowed.  \n"
                + "• Valid at Takashimaya Department Store except supermarket, tenants, services and restaurants.  \n"
                + "• Any balance payment must be made with your DBS Credit Card.  \n"
                + "• Other terms & conditions apply.");
        p4.setPoints(16500.0);
        p4.setType(EnumUtils.PromoType.VOUCHER);
        utilsBean.persist(p4);
        
        return demoPromoProduct;
    }
}

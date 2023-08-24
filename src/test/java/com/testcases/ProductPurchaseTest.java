package com.testcases;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.pageobjects.CartPage;
import com.pageobjects.CheckoutPage;
import com.pageobjects.HomePage;
import com.pageobjects.LoginPage;
import com.pageobjects.ProductDetailPage;
import com.pageobjects.ProductListingPage;
import com.testutility.BaseTest;
import com.utilities.Utility;

public class ProductPurchaseTest extends BaseTest {
	
	public ProductPurchaseTest()
	{
		super();
	}
	
	private ProductListingPage productlistpage;
	private ProductDetailPage productdetailpage;
	private CartPage cartpage;
	private HomePage homepage;
	private LoginPage loginpage;
	private CheckoutPage checkoutpage;
	public String totalcartvalue;
	
	
	@Test(priority=1,groups= {"regression","smoke","guestuser","sanity","memberuser","parallel"})
	public void verify_add_product_to_cart(ITestContext context) throws InterruptedException
	{
		homepage = new HomePage(getDriver());
		
		productlistpage= homepage.gotoshoesproductlistpage();
		step("Go to homepage and click on the shoes product");
		
		productlistpage.wait_for_product_heading();
		
		//verify user sees the correct product page
		String pageheading = productlistpage.getpageheading();
		
		//System.out.println(pageheading);
		
		
		Assert.assertEquals(pageheading, dataprop.getProperty("ShoesPageTitle"),"Shoes title not found");
		step("product listing page is opened");
		
		//select nike shoe product
		//productlistpage.selectproductandclick(dataprop.getProperty("shoesproductname"));
		
		
		//productlistpage.scrolltoproductelement();
		
		// Get all the products and print their names
		productlistpage.getProductElements().keySet().forEach(System.out::println);
		
		
		productdetailpage=productlistpage.clickonproductname(dataprop.getProperty("shoesproductname"));
		step("product detail page is opened");
		Thread.sleep(2000);
		
		//verify product detail page
		String product_title = productdetailpage.getproducttitle();
		Assert.assertEquals(product_title, dataprop.getProperty("shoesproductname"),"Shoes title not found");
		step("product title is matched");
		
		productdetailpage.selectshoesize(dataprop.getProperty("size"));
		step("product size is added");
		productdetailpage.selectshoecolor(dataprop.getProperty("color"));
		step("product color is added");
		productdetailpage.selectshoeprint();
		
		Assert.assertTrue(productdetailpage.ishoverimagevisible(),"Image is not visible");
		
		productdetailpage.clickanywhereonpage();
		
		Assert.assertFalse(productdetailpage.ishoverimagevisible(),"Image is not visible");
		
		step("product print is added");
		
		productdetailpage.clickonaddtocartbutton();
		
		String successmsg = productdetailpage.getsuccessmsg();
		Assert.assertEquals(successmsg, dataprop.getProperty("addtocartsuccessmsg"),"success msg is not matched");
		step("product is added to cart");
	}
	
	@Test(priority=2, groups= {"regression","smoke","guestuser","sanity","memberuser","parallel"}, dependsOnMethods = "verify_add_product_to_cart")
	public void verify_user_shopping_cart()
	{
		cartpage = productdetailpage.gotoshoppingcart();
		step("Clicked on the shopping cart link");
		
		cartpage.gettitleofthepage();
		Assert.assertEquals(cartpage.validate_cartpage_heading(),"Shopping cart", "Page heading not matched");
		step("Shopping cart title is visible");
		
		System.out.println(cartpage.getproductname());
		
		Assert.assertEquals(cartpage.getproductname(),dataprop.getProperty("shoesproductname"), "Productname not matched");
		step("Verified the product name");
	}
	
	@Test(priority=3,groups= {"regression","guestuser","memberuser","parallel"},dependsOnMethods="verify_user_shopping_cart")
	public void verify_update_shopping_cart()
	{
		cartpage.update_product_quantity("3");
		step("new quantity value added");
		
		cartpage.click_on_update_shopping_cart();
		Assert.assertEquals(cartpage.get_subtotal_price(),"$120.00", "subtotal price is not matched after updating the quantity");
		step("Quantity value and total is updated");
	}
	
	@Test(priority=4, groups= {"regression","guestuser","memberuser","parallel"},dependsOnMethods="verify_user_shopping_cart")
	public void verify_shopping_cart_total_amount_after_gift_wrapping_cost(ITestContext context) throws InterruptedException
	{
		cartpage.select_gift_wrapping_cost(dataprop.getProperty("giftwrapping"));
		step("gift charge is added");
		
		cartpage.wait_for_gift_value_to_appear();
		
		String giftcharge = cartpage.validate_gift_wrapping_cost();
		
		System.out.println(giftcharge);
		Assert.assertEquals(giftcharge,dataprop.getProperty("giftwrappingvalue"),"Gift wrapping value doesn't matched");
		
		String carttotal = cartpage.get_cart_total();
		// Store the cart total in testng ITestContext context
		context.setAttribute("cart_total_amount", carttotal);
		
		Assert.assertEquals(carttotal, "$130.00", "Cart total is not matched");
		step("Total is updated");
		
	}
	
	@Test(priority=5, groups= {"guestuser","parallel"}, dataProvider="getData")
	public void verify_checkout_process_as_guest_user(HashMap<String,String>input,ITestContext context) throws IOException
	{
		cartpage.clickoncheckoutbutton();
		
		
		String termsconditionmessage = cartpage.validate_term_condition_required_error_msg();
		Assert.assertEquals(termsconditionmessage,dataprop.getProperty("termconditionerror"), "Error message not foound");
		
		cartpage.click_on_close_icon();
		
		cartpage.clickontermscheckbox();
		step("clicked on term and condition checkbox");
		
		cartpage.validate_term_checkbox_is_selected();
		
		loginpage = cartpage.clickoncheckoutbutton();
		
		// Click on checkout as guestuser button
		String guestusermsg = loginpage.validate_checkoutpage_as_guestuser_page_heading();
		Assert.assertEquals(guestusermsg,"Checkout as a guest or register", "msg not visible");
		step("Select Continue as Guest user option");
		
		checkoutpage = loginpage.click_on_checkout_guest_user_button();
		
		
		String checkoutpageheading = checkoutpage.getcheckoutpageheading();
		Assert.assertEquals(checkoutpageheading,"Checkout","Page heading not visible");
		step("User redirected to the checkout page");
		
		Assert.assertTrue(checkoutpage.verify_billing_tab(),"Billing Tab is not visible");
		
		//Add billing details
		checkoutpage.enterbillinginformation(input.get("firstname"), input.get("lastname"), input.get("email"), input.get("country"), input.get("city"), input.get("address"), 
				input.get("postalcode"), input.get("phone") );
		
		step("Billing info is added");
		
		checkoutpage.click_on_continue_button();
		
		// check shipping details
		Assert.assertTrue(checkoutpage.verify_ground_transport_selected(), "Ground transport is not selected");
		step("Shipping detail added");
		
		checkoutpage.click_shipping_continue_button();
		
		Assert.assertTrue(checkoutpage.verify_payment_method_tab_visible(), "Payment tab is not visible");
		
		checkoutpage.click_on_credit_card_option();
		
		Assert.assertTrue(checkoutpage.validate_credit_card_option_selected(), "card option is not selected");
		
		checkoutpage.click_on_paymentmethod_continue_button();
		
		//Enter billing details
		checkoutpage.entercreditcarddetails(input.get("Visa"),input.get("cardholdername"), input.get("cardnumber"), 
	input.get("month"), input.get("year"), input.get("code"));
		step("payment info is added");
		
		checkoutpage.click_payment_continue_button();
		step("Payment confirmation page is visible ");
		
		
		Assert.assertTrue(checkoutpage.validate_billing_data(input.get("firstname")),"Firstname is not matched");
		Assert.assertTrue(checkoutpage.validate_billing_data(input.get("lastname")),"lastnamename is not matched");
		Assert.assertTrue(checkoutpage.validate_billing_data(input.get("email")),"Email is not matched");
		Assert.assertTrue(checkoutpage.validate_billing_data(input.get("address")),"Address is not matched");
		Assert.assertTrue(checkoutpage.validate_billing_data(input.get("postalcode")),"Postal is not matched");
		Assert.assertTrue(checkoutpage.validate_billing_data(input.get("phone")),"Phone number is not matched");
		
		//retrive cart total
		totalcartvalue=(String)context.getAttribute("cart_total_amount");
		Assert.assertEquals(totalcartvalue, checkoutpage.validate_cart_total(),"Product total value is not matched");
		
		if(totalcartvalue.equals(checkoutpage.validate_cart_total()) || checkoutpage.validate_billing_data(input.get("email")))
		{
			System.out.println(totalcartvalue);
			step("billing information is matched");
		}
		else
		{
			step("billing information not matched");
		}
		
		checkoutpage.click_on_confirm_button();
		
		Assert.assertTrue(checkoutpage.validate_success_message_visible(),"Success message is not visible");
		
		String msg = checkoutpage.getThankyoumsg();
		
		Assert.assertEquals(msg,"Your order has been successfully processed!", "Success message is not matched");
		
	}
	
	@Test(priority=6, groups= {"regression","memberuser","parallel"}, dataProvider="getData", dependsOnMethods="verify_shopping_cart_total_amount_after_gift_wrapping_cost")
	public void verify_checkout_process_as_member_user(HashMap<String,String>input,ITestContext context) throws IOException
	{
		cartpage.clickoncheckoutbutton();
		
		
		String termsconditionmessage = cartpage.validate_term_condition_required_error_msg();
		Assert.assertEquals(termsconditionmessage,dataprop.getProperty("termconditionerror"), "Error message not foound");
		
		cartpage.click_on_close_icon();
		
		cartpage.clickontermscheckbox();
		step("clicked on term and condition checkbox");
		
		cartpage.validate_term_checkbox_is_selected();
		
		checkoutpage = cartpage.clickon_checkoutbutton();
		step("clicked on the checkoutbutton");
		
		String checkoutpageheading = checkoutpage.getcheckoutpageheading();
		Assert.assertEquals(checkoutpageheading,"Checkout","Page heading not visible");
		step("User redirected to the checkout page");
		
		Assert.assertTrue(checkoutpage.verify_billing_tab(),"Billing Tab is not visible");
		
		//Add billing details
		Assert.assertTrue(checkoutpage.validate_ship_to_same_address_checkbox(),"Ship to same address checkbox not selected");
		
		checkoutpage.select_address_from_dropdown(input.get("address"));
		
		step("Billing info is added");
		
		checkoutpage.click_on_billing_continue_button();
		
		// check shipping details
		Assert.assertTrue(checkoutpage.verify_ground_transport_selected(), "Ground transport is not selected");
		step("Shipping detail added");
		
		checkoutpage.click_shipping_continue_button();
		
		Assert.assertTrue(checkoutpage.verify_payment_method_tab_visible(), "Payment tab is not visible");
		
		checkoutpage.click_on_credit_card_option();
		
		Assert.assertTrue(checkoutpage.validate_credit_card_option_selected(), "card option is not selected");
		
		checkoutpage.click_on_paymentmethod_continue_button();
		
		//Enter billing details
		checkoutpage.entercreditcarddetails(input.get("Visa"),input.get("cardholdername"), input.get("cardnumber"), 
	input.get("month"), input.get("year"), input.get("code"));
		step("payment info is added");
		
		checkoutpage.click_payment_continue_button();
		step("Payment confirmation page is visible ");
		
		
		Assert.assertTrue(checkoutpage.validate_billing_data(input.get("firstname")),"Firstname is not matched");
		Assert.assertTrue(checkoutpage.validate_billing_data(input.get("lastname")),"lastnamename is not matched");
		Assert.assertTrue(checkoutpage.validate_billing_data(input.get("email")),"Email is not matched");
		Assert.assertTrue(checkoutpage.validate_billing_data(input.get("address")),"Address is not matched");
		Assert.assertTrue(checkoutpage.validate_billing_data(input.get("postalcode")),"Postal is not matched");
		Assert.assertTrue(checkoutpage.validate_billing_data(input.get("phone")),"Phone number is not matched");
		
		//retrive cart total
		totalcartvalue=(String)context.getAttribute("cart_total_amount");
		Assert.assertEquals(totalcartvalue, checkoutpage.validate_cart_total(),"Product total value is not matched");
		
		if(totalcartvalue.equals(checkoutpage.validate_cart_total()) || checkoutpage.validate_billing_data(input.get("email")))
		{
			System.out.println(totalcartvalue);
			step("billing information is matched");
		}
		else
		{
			step("billing information not matched");
		}
		
		checkoutpage.click_on_confirm_button();
		
		Assert.assertTrue(checkoutpage.validate_success_message_visible(),"Success message is not visible");
		
		String msg = checkoutpage.getThankyoumsg();
		
		Assert.assertEquals(msg,"Your order has been successfully processed!", "Success message is not matched");
		
	}
		
	
	@DataProvider
	public Object[][] getData() throws IOException
	{
		List<HashMap<String,String>> data = getJsondatatoMap(System.getProperty("user.dir")+dataprop.getProperty("billingdetailsfilepath"));
		return new Object[][] {{data.get(0)}};
	}
	
	

}

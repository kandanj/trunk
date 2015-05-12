package washpost.paywall.dsi.api.dist;


import washpost.paywall.dsi.api.rest.*;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.ws.rs.Path;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import washpost.paywall.dsi.dao.DataAccessDist;
import washpost.paywall.dsi.api.model.AddrRequest;
import washpost.paywall.dsi.api.model.SubStartRequest;
import washpost.paywall.dsi.api.model.SubStartResponse;
import java.util.Calendar;
//import washpost.paywall.dsi.api.model.CreditDebitRequest;
import washpost.paywall.dsi.api.model.CommonRequest;
import washpost.paywall.dsi.api.model.CommonResponse;
//import washpost.paywall.dsi.api.model.CardResponse;
import washpost.paywall.dsi.api.model.CommonResponse;
import washpost.paywall.dsi.api.model.CardInfo;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.OPTIONS;
import static javax.ws.rs.HttpMethod.OPTIONS;
import javax.ws.rs.core.Context;
import washpost.paywall.dsi.api.model.CardRequest;
import washpost.paywall.dsi.api.model.CommonMethod;
import washpost.paywall.dsi.api.addr.AddressScrubService;
import washpost.paywall.dsi.api.addr.SaxHandler;
import washpost.paywall.dsi.api.model.ProdChangeRequest;

@Path("/substartprint")
@RequestScoped
public class SubStartPrintApi {

    @Inject
    AddrRequest addrreq;     
   
    @Inject
    CommonResponse addrres;   

    @Inject
    SubStartResponse res;

    @Inject
    CardInfo cardinfo;  
    
    @Inject  
    CommonResponse cardres;      
    
    @Inject
    CommonMethod method;    
    
    @Inject
    DataAccessDist dao;    
    
    @Inject
    DataAccessDist daodist;        
    
    @Context
    HttpServletResponse response;    

    @PersistenceContext(unitName = "paywallPU")
    private EntityManager em;
    @Resource
    UserTransaction ut;


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public SubStartResponse substart(@Valid SubStartRequest form) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        String s  = null;
        String name  = null;
        int acctnum = 0;      
        int acctcnt = 0;
        String comments = null;
        String acctstatus = null;
        String rtn = null;  
        String email = null;  
        String newacct  = null;  
        String rtnmsg = null;  
        String vendor = null; 
        String subs_tag = null; 
        int addrid = 0;    
        float subs_price = 0;
        Date cardexpiredate = null;
        String prodChange = null;


        name = form.getFirstName();
        System.out.println("substart name " + name);
                

        try {
            

       CommonRequest promopricereq = new CommonRequest();     
       CommonResponse promopriceres = new CommonResponse();
       AddressScrubService addressScrubService = new AddressScrubService();
              

        vendor = form.getVendor();
        subs_tag = form.getSubsTag();
        email = form.getEmail(); 
        prodChange = form.getProdChange();
        
        
        if (vendor == null) {
            vendor = "N";   
        }  
        else 
        {
            vendor = "Y";   
        }

        if (email == null) {
            email = "";    
        }
        else {
           email = "  Email is  " + email; 
        }  

        if (prodChange == null) {
            prodChange = "N";   
        }  
        else 
        {
            prodChange = "Y";   
        }        

        SaxHandler handler = addressScrubService.scrub(form.getAddress1(), form.getAddress2(), null, null, form.getZip());
        
        // delivery address fields
        //addrreq.setUnit(handler.getValues().get("Apt_Unit"));
        //addrreq.setCity(handler.getValues().get("City"));
        //addrreq.setState(handler.getValues().get("State"));
        form.setCity(handler.getValues().get("City"));
        form.setState(handler.getValues().get("State"));
        form.setUnit(handler.getValues().get("Apt_Unit"));
        //addrreq.setZip(handler.getValues().get("Zip"));
        if (handler.getValues().get("return_status").equals("ok")) {
            //addrreq.setAddrScrub(Boolean.TRUE);
            if (handler.getValues().get("House_Sort") != null) {
                //addrreq.setHouse(handler.getValues().get("House_Sort").toUpperCase());
                form.setHouse(handler.getValues().get("House_Sort").toUpperCase());
            }
            if (handler.getValues().get("Street_Name") != null) {
                //addrreq.setStreet(handler.getValues().get("Street_Name").toUpperCase());
                form.setStreet(handler.getValues().get("Street_Name").toUpperCase());
            }
            if (handler.getValues().get("Street_Suffix") != null) {
                //addrreq.setStreetSuffix(handler.getValues().get("Street_Suffix").toUpperCase());
                form.setStreetSuffix(handler.getValues().get("Street_Suffix").toUpperCase());
            }
            if (handler.getValues().get("Predirectional") != null) {
                //addrreq.setFstDir(handler.getValues().get("Predirectional").toUpperCase());
                form.setFstDir(handler.getValues().get("Predirectional").toUpperCase());
            }
            if (handler.getValues().get("Postdirectional") != null) {
                //addrreq.setSndDir(handler.getValues().get("Postdirectional").toUpperCase());
                form.setSndDir(handler.getValues().get("Postdirectional").toUpperCase());
            }
        } else {
            //addrreq.setAddrScrub(Boolean.FALSE);
        }

        System.out.println("substart name 3 " + name);
         addrres = dao.getAddrResponse(addrreq);

         //addrid = addrres.getAddrid();
         //System.out.println("addressid " + addrid);
         
         //kjaddrreq = method.getaddress(form.getAddress1(), form.getAddress2(), form.getZip());
         
         //System.out.println(form);
         addrreq = method.getaddr(form);
        
         addrid = addrreq.getAddrId();
         System.out.println("addressid  " + addrid);   

        //check if acct exist in address
        acctnum = dao.getAcctInAddr(form, addrid);
        System.out.println("acct exists " + acctnum);
        
        //check if multiple accts exist in address
        acctcnt = dao.getAcctsforAddr(form, addrid);        
        System.out.println("acctcnt " + acctcnt);
        
        //get start date default of todays date
        Calendar startCal = Calendar.getInstance();

        int startDay =  startCal.get(Calendar.DATE);
        int startMonth = startCal.get(Calendar.MONTH); 
        int startYear = startCal.get(Calendar.YEAR);                  
           
        
        
        //rtn = dao.addtoQueue(form, addrid, "NS", acctnum, comments + email);   
        //System.out.println("rtn " + rtn);
         //if multiple accta already exist add to queue
           if ( acctcnt > 1 ) {
             System.out.println("Multiple accts already exists. " );
             comments = "Multiple accts. " ;
             //may 2013
            if (vendor.equalsIgnoreCase("Y")) {
               rtn = dao.addtoQueue(form, addrid, "NS", acctnum, comments + email);    
            }
            else
               rtn = dao.addtoQueue(form, addrid, "NS", acctnum, comments + email);     
            }     
           
           
            //one acct exist, check other conditions
             if ( acctcnt == 1 ) {
                System.out.println("Acct already exists " + acctnum );
                
                acctstatus = dao.getAccountStatus(acctnum, startMonth, startDay,startYear) ;
                
                if (acctstatus.startsWith("Active")) {
                        System.out.println("Active.  " );
                        comments = "Active.  " ;
                } //inactive and terminated
                else if (acctstatus.startsWith("Inactive") || acctstatus.startsWith("Terminated")) {
                    String inact_date = acctstatus.substring(10).trim();
                    System.out.println("substr acctstatus " + inact_date);             
                    
                    if (inact_date.startsWith("Start Pending")) {
                        System.out.println("Inactive Start Pending."); 
                        comments = "Inactive Start Pending."; 
                    }
                    else {
                        //get values for queue criteria                                             
                        String[] queue = new String[5];
                        queue = dao.getqueueinfo(acctnum);
                        String queue_days = queue[0] ;     
                        String curr_prod_code = queue[1] ;     
                        float cc_bal_due = Float.valueOf(queue[2]).floatValue();   
                        String sub_type = queue[3] ; 
                        int cr_card_cnt = Integer.valueOf(queue[4]).intValue();
                        
                        //if stop_date less than 30 days should go to queue   
                        if (queue_days.equalsIgnoreCase("Y")) {
                            System.out.println("InActive/Terminated Stop date less than 30 days.");                
                            comments = "InActive/Terminated Stop date less than 30 days. " ;
                        }
                        else if (cr_card_cnt > 0) {//cr card on file
                            System.out.println("Restart not completed.  Account already has credit card.");                
                            comments = "Restart not completed.  Account already has credit card. " ;                                    
                        }
                        else {
                            //SubstartRequest subsstartbean = form.getSubstartRequest();                             
                            String new_prod_code = form.getProdCode();
            
                            //for cc subs, cc_bal should be 0, else queue
                            if (new_prod_code.equalsIgnoreCase(curr_prod_code)) {
                                if (sub_type != null && sub_type.equalsIgnoreCase("CC") && cc_bal_due != 0) {
                                    System.out.println("CC bal is not 0. " );
                                    comments = "CC bal is not 0. " ;
                                }
                            }

                        }

                    }
                                                
                }      
                else if (acctstatus.startsWith("Inact - Restart")) {
                        System.out.println("Inact ReStart.  " );
                        comments = "Inact ReStart.  " ;
                }                
                
           }
                //add to queue if neccessary for acctcnt is 1             
                if (comments != null) {
                    //kj may 2013
                    //if (vendor.equalsIgnoreCase("Y")) {
                    //    rtn = daoapi.addtoQueue(form, addr_id, "NS", acct, comments + email);    
                    // }
                    //else
                        rtn = dao.addtoQueue(form, addrid, "NS", acctnum, comments + email);
                    }                   
                
                
            if (addrid <= 0 ) {
             System.out.println(" Addr not correct. " );
             comments = "Addr not correct.";   
             //if (vendor.equalsIgnoreCase("Y")) {
             //       rtn = daoapi.addtoQueue(form, addr_id, "NS", acct, comments + email);    
             //}
            //else             
             rtn = dao.addtoQueue(form, addrid, "NA", 0, comments + email); 
             //}    
            }
                
                
            if (rtn != null) {
                System.out.println("rtn value from queue " + rtn);
            }
            
          response.setHeader("Access-Control-Allow-Origin", "*");
	  response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	  response.setHeader("Access-Control-Max-Age", "3600");
	  response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
          response.setHeader("Content-Type", "application/json");
          
 

          res.setResponse(response);
         
            //create the acct if not created in error queue
            if (comments == null) {
                //substartDoc = dao.subStart(substartXml, addr_id); //kj may 2013
                //if (vendor.equalsIgnoreCase("Y")) {
                //    substartDoc = daoapi.subStart(substartXml, addr_id); //kj may 2013
                //}
                //else {
                    res = dao.subStart(form, addrid);
                //}   
                
               
                
                rtnmsg = res.getReturnMessage();      
                //logger.info(" rtnmsg substartDoc " + rtnmsg );        
                            
                newacct  =  res.getAccount();
                System.out.println("newacct  "  + newacct);
                

                
                //if (subs_tag.equalsIgnoreCase("Y")) {
                if (subs_tag != null) {
                    String errmsg = null;
                    //kjString errmsg = dao.addTag(newacct, subs_tag);
                    if (errmsg != null) {
                        rtnmsg = subs_tag +  " subs_tag could not be added  " + errmsg;
                    }
                }                   

                //if new acct is not created or err msg, then add it to queue
                if (newacct == null || rtnmsg != null) {
                    if (rtnmsg == null) {//don't show value null in queue msg
                        rtnmsg = "";
                    }
                    //logger.info(" Error  " + rtnmsg );
                    comments = "  Error.  " + rtnmsg;
                    //if (vendor.equalsIgnoreCase("Y")) {
                    //    rtn = daoapi.addtoQueue(substartXml, addr_id, "NS", 0, comments + email); 
                    //}
                    //else {
                          rtn = dao.addtoQueue(form, addrid, "NS", 0, comments + email);   
                    //}
                    res.setQueueId(Integer.parseInt(rtn));                     
                }                
                subs_price = res.getSubscriptionPrice();            
                res.setReturnMessage("");
            }            
            else {
                //get the subscription price based on promo price when acct is not created                          
                promopricereq.setPaperCode(form.getPaperCode());
                promopricereq.setProdCode(form.getProdCode());
                promopricereq.setRateTable(form.getRateTable());
            
                promopriceres =  dao.getpromoprice(promopricereq);    
                
                res.setIsReturnValid(true);
                subs_price = promopriceres.getPromoPrice();
                res.setSubscriptionPrice(subs_price);  
                res.setQueueId(Integer.parseInt(rtn));                        
            }          
            
            System.out.println("subs_price  "  + subs_price);
            
            
            if (prodChange.equalsIgnoreCase("Y")) {
                ProdChangeRequest prodReq = new ProdChangeRequest();
                prodReq.setAccount( Integer.parseInt(newacct));
                prodReq.setPaperCode("TVR");
                prodReq.setProdCode("SO");
                prodReq.setRateTable(8);
                prodReq.setProdQty(1);
                
                daodist.subProductChange(prodReq);
                
            }
            
            
          //if not in queue, no err msg and acct was created then create cr. card
          /*  String createCard = form.getCreateCard();
            System.out.println("cr. card " + createCard);
            if (rtnmsg == null && newacct != null && comments == null && (createCard == null || createCard.equalsIgnoreCase("Y") ) ) {
               //if (vendor.equalsIgnoreCase("Y")) {
                //    cardDoc = daoapi.createCardInfo(cardBean, newacct);     
               // }
               // else {

                    cardinfo.setAccount(Integer.valueOf(newacct));
                    cardinfo.setCcardCode(form.getCcardCode());
                    cardinfo.setCcardNumber(form.getCcardNumber());
                    
                    if (form.getTip() != null) {
                          String tip = form.getTip().toString();
                          float tipamt=Float.parseFloat(tip);
                         cardinfo.setTip(tipamt);                          
                    }                    
                    
                    //String tip = form.getTip().toString();
                    Float onetimechg = form.getOneTimeCharge();
                    if (onetimechg != null) {
                        String onetime = onetimechg.toString();
                        float onetimecharge=Float.parseFloat(onetime);
                        cardinfo.setOneTimeCharge(onetimecharge);  
                    }
  
                    CardRequest cardreq = new CardRequest();
                    String cardexpire = form.getCcardExpire();                  
                    cardinfo.setCcardExpire(dao.convertStringtoSqlDate(cardexpire));
                    //cardinfo.setAutoRenew(form.getAutoRenew());
                    cardinfo.setOneTimeCharge(subs_price);
                    cardinfo.setAutoRenew("R");
                    cardres = dao.createCardInfo(cardinfo);     
                    //cardres = dao.createCardInfo(cardinfo, cardreq, "S");     
                    System.out.println("cardres  "  + cardres.getReturnMessage());
                //}  
                    
                rtnmsg =  cardres.getReturnMessage();
                                         
                if (rtnmsg != null) {//error on credit card
                    //logger.error(" rtnmsg cardDoc " + rtnmsg);  
                    comments = " Account created " + newacct + " .Credit Card Error.  " + rtnmsg;
                    // if (vendor.equalsIgnoreCase("Y")) {
                    //    rtn = daoapi.addtoQueue(substartXml, addr_id, "NS", 0, comments);
                    // }
                    // else {
                        rtn = dao.addtoQueue(form, addrid, "NS", 0, comments);
                    // }                                          
                }        
            }      
            
            //dao.addTag(newacct, "AF");

            //mar 2015
            /*String createDebit = form.getActivationFee();
            
            //if (newacct != null &&  !(createDebit.equalsIgnoreCase("N")) ) {
            if (newacct != null && createDebit == null) {
                    System.out.println("create debit trans");
                    CommonResponse debitResp = new CommonResponse();
                    CommonRequest debitReq  = new CommonRequest();
                    debitReq.setAccount(newacct);
                    debitReq.setProdCode(form.getProdCode());
                
                    debitResp = dao.makeCreditDebit(debitReq);
               
            }*/
            


            
         
            res.setIsReturnValid(true);
         
        } catch (Exception ex) {
            Logger.getLogger(SubStartPrintApi.class.getName()).log(Level.SEVERE, "SubStartPrintApi Failed ", ex.getMessage());
            res.setIsReturnValid(false);
            res.setReturnMessage(ex.getMessage());            
        }

            return res;
           //kjreturn Response.ok(res).build();
        }

    

    public SubStartPrintApi() {
    }
}

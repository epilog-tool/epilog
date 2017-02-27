
package org.cellularasynchrony;

import java.util.*;



public class Utils {
	
	private MainFrame mainFrame = null;
	
	public Utils() {
		this.mainFrame = mainFrame;

	}
	
    private static String USER_NAME = "*****";  // GMail user name (just the part before "@gmail.com")
    private static String PASSWORD = "********"; // GMail password
    private static String RECIPIENT = "lisboa.ny@gmail.com";

	  public static List<Integer> shuffleAndSelect(int N, int alpha) {
		  List<Integer> foo = new ArrayList<Integer>();
		  List<Integer> a = new ArrayList<Integer>();
		  
		  
		  for (int instance = 0; instance < N; instance++){
			  foo.add(instance);
		  }
		  
		  Collections.shuffle(foo);

		  for (int n =0; n<alpha; n++){
			  a.add(foo.get(n));
		  }
		  return a;
	  }

	public static List<Integer> selectInstances(List<Integer> schuffledInstances, int numberCells) {

		List<Integer> b = schuffledInstances;
		List<Integer> a = new ArrayList<Integer>();
		
		for (int n = 0; n< numberCells;n++){
			a.add(b.get(n));
		}
		return a;
	}
	
	public static List<Integer> findMinIdx(List<Double> exponentialInstances, int numberCells) {
	    if (exponentialInstances == null || exponentialInstances.size() == 0) return null; // Saves time for empty array
	    List<Integer> b =  new ArrayList<Integer>();

	    Double minVal =  Collections.max(exponentialInstances);// Keeps a running count of the smallest value so far
	    Double minVal_Aux = (double) 0; 
	    for (int n = 0; n<numberCells; n++){
	    	int minIdx = 0;
	    	minVal =  Collections.max(exponentialInstances);
		    for(int idx=0; idx<exponentialInstances.size(); idx++) {
		    	
		        if(exponentialInstances.get(idx) <= minVal && exponentialInstances.get(idx)>minVal_Aux) {
		            minVal = exponentialInstances.get(idx);
		            minIdx = idx;
		        }
		    }
		    minVal_Aux = minVal;
		    b.add(minIdx);
	    }
	    return b;
	}

	public static List<Integer> shuffleAndSelect(List<Integer> cellsAvailableToUpdate, int numberCells) {
		// TODO Auto-generated method stub

		  List<Integer> a = new ArrayList<Integer>();
		  
		  Collections.shuffle(cellsAvailableToUpdate);
		 
		  if (cellsAvailableToUpdate.size()>0)
			  for (int n =0; n<numberCells; n++){
				  a.add(cellsAvailableToUpdate.get(n));
			  }
		  return a;
	}

	public static List<Integer> findMinIdx(List<Double> exponentialInstances, int numberCells,
			List<Integer> updatableCells) {
		
	    if (exponentialInstances == null || exponentialInstances.size() == 0) return null; // Saves time for empty array
	    List<Integer> b =  new ArrayList<Integer>();

	    Double minVal =  Collections.max(exponentialInstances);// Keeps a running count of the smallest value so far
	    Double minVal_Aux = (double) 0; 
	    for (int n = 0; n<numberCells; n++){
	    	int minIdx = 0;
	    	minVal =  Collections.max(exponentialInstances);
		    for(int idx=0; idx<exponentialInstances.size(); idx++) {
		    	
		        if(exponentialInstances.get(idx) < minVal &&  exponentialInstances.get(idx)>minVal_Aux  && updatableCells.contains(idx)){
		            minVal = exponentialInstances.get(idx);
		            minIdx = idx;
		        }
		    }
		    minVal_Aux = minVal;
		    b.add(minIdx);
	    }
	    
	    return b;
	}
	

//    public static void sendFromGMail(String subject, String body) {
//        Properties props = System.getProperties();
//        String host = "smtp.gmail.com";
//        String from = "lisboa.ny";
//        String pass = "ENnHSM.1977";
//        String to = "lisboa.ny@gmail.com";
//        
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", host);
//        props.put("mail.smtp.user", from);
//        props.put("mail.smtp.password", pass);
//        props.put("mail.smtp.port", "587");
//        props.put("mail.smtp.auth", "true");
//
//
//        Session session = Session.getDefaultInstance(props);
//        MimeMessage message = new MimeMessage(session);
//
//        try {
//            message.setFrom(new InternetAddress(from));
//            InternetAddress[] toAddress = new InternetAddress[to.length()];
//
//            // To get the array of addresses
//            for( int i = 0; i < to.length(); i++ ) {
//                toAddress[i] = new InternetAddress(to);
//            }
//
//            for( int i = 0; i < toAddress.length; i++) {
//                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
//            }
//
//            message.setSubject(subject);
//            message.setText(body);
//            Transport transport = session.getTransport("smtp");
//            transport.connect(host, from, pass);
//            transport.sendMessage(message, message.getAllRecipients());
//            transport.close();
//        }
//        catch (AddressException ae) {
//            ae.printStackTrace();
//        }
//        catch (MessagingException me) {
//            me.printStackTrace();
//        }
//    }
	
	}

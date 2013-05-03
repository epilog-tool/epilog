package pt.igc.nmd.epilogue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.StringCharacterIterator;
import java.util.Date;

public final class Pqw implements Serializable {

   /**
   * This constructor requires all fields to be passed as parameters.
   *
   * @param aFirstName contains only letters, spaces, and apostrophes.
   * @param aLastName contains only letters, spaces, and apostrophes.
   * @param aAccountNumber is non-negative.
   * @param aDateOpened has a non-negative number of milliseconds.
   */
   public Pqw (
     String aFirstName, String aLastName, int aAccountNumber, Date aDateOpened
   ){
      super();
      setFirstName(aFirstName);
      setLastName(aLastName);
      setAccountNumber(aAccountNumber);
      //make a defensive copy of the mutable Date passed to the constructor
      setDateOpened( new Date(aDateOpened.getTime()) );
      //there is no need here to call validateState.
   }

   public Pqw () {
     this ("FirstName", "LastName", 0, new Date(System.currentTimeMillis()));
   }

   public final String getFirstName() {
     return fFirstName;
   }

   public final String getLastName(){
     return fLastName;
   }

   public final int getAccountNumber() {
     return fAccountNumber;
   }

   /**
   * Returns a defensive copy of the field.
   * The caller may change the state of the returned object in any way,
   * without affecting the internals of this class.
   */
   public final Date getDateOpened() {
     return new Date(fDateOpened.getTime());
   }

   /**
   * Names must contain only letters, spaces, and apostrophes.
   * Validate before setting field to new value.
   *
   * @throws IllegalArgumentException if the new value is not acceptable.
   */
   public final void setFirstName( String aNewFirstName ) {
      validateName(aNewFirstName);
      fFirstName = aNewFirstName;
   }

   /**
   * Names must contain only letters, spaces, and apostrophes.
   * Validate before setting field to new value.
   *
   * @throws IllegalArgumentException if the new value is not acceptable.
   */
   public final void setLastName ( String aNewLastName ) {
      validateName(aNewLastName);
      fLastName = aNewLastName;
   }

   /**
   * Validate before setting field to new value.
   *
   * @throws IllegalArgumentException if the new value is not acceptable.
   */
   public final void setAccountNumber( int aNewAccountNumber ) {
      validateAccountNumber(aNewAccountNumber);
      fAccountNumber = aNewAccountNumber;
   }

   public final void setDateOpened( Date aNewDate ){
     //make a defensive copy of the mutable date object
     Date newDate = new Date( aNewDate.getTime());
     validateDateOpened( newDate );
     fDateOpened = newDate;
   }

   // PRIVATE //

   /**
   * The client's first name.
   * @serial
   */
   private String fFirstName;

   /**
   * The client's last name.
   * @serial
   */
   private String fLastName;

   /**
   * The client's account number.
   * @serial
   */
   private int fAccountNumber;

   /**
   * The date the account was opened.
   * @serial
   */
   private Date fDateOpened;

   /**
   * Determines if a de-serialized file is compatible with this class.
   *
   * Maintainers must change this value if and only if the new version
   * of this class is not compatible with old versions. See Sun docs
   * for <a href=http://java.sun.com/products/jdk/1.1/docs/guide
   * /serialization/spec/version.doc.html> details. </a>
   *
   * Not necessary to include in first version of the class, but
   * included here as a reminder of its importance.
   */
   private static final long serialVersionUID = 7526471155622776147L;

   /**
   * Verify that all fields of this object take permissible values; that is,
   * this method defines the class invariant.
   *
   * In this style of implementation, both the entire state of the object
   * and its individual fields can be validated without repeating or
   * duplicating code.
   * Each condition is defined in one place. Checks on the entire
   * object are performed at the end of object construction, and at
   * the end of de-serialization. Checks on individual fields are
   * performed at the start of the corresponding setXXX method.
   * As well, this style replaces the if's and throwing
   * of exceptions at the start of a setXXX, with a simple call to validateXXX.
   * Validation is separated from the regular path of execution,
   * which leads to improved legibility.
   *
   * @throws IllegalArgumentException if any field takes an unpermitted value.
   */
   private void validateState() {
      validateAccountNumber(fAccountNumber);
      validateName(fFirstName);
      validateName(fLastName);
      validateDateOpened(fDateOpened);
   }

   /**
   * Ensure names contain only letters, spaces, and apostrophes.
   *
   * @throws IllegalArgumentException if field takes an unpermitted value.
   */
   private void validateName(String aName){
     boolean nameHasContent = (aName != null) && (!aName.equals(""));
     if (!nameHasContent){
       throw new IllegalArgumentException("Names must be non-null and non-empty.");
     }

     StringCharacterIterator iterator = new StringCharacterIterator(aName);
     char character =  iterator.current();
     while (character != StringCharacterIterator.DONE ){
       boolean isValidChar =
         (Character.isLetter(character) ||
         Character.isSpaceChar(character) ||
         character =='\''
       );
       if ( isValidChar ) {
         //do nothing
       }
       else {
        String message = "Names can contain only letters, spaces, and apostrophes.";
         throw new IllegalArgumentException(message);
       }
       character = iterator.next();
     }
  }

  /**
  * AccountNumber must be non-negative.
  * @throws IllegalArgumentException if field takes an unpermitted value.
  */
   private void validateAccountNumber(int aAccountNumber){
      if (aAccountNumber < 0) {
        String message = "Account Number must be greater than or equal to 0.";
        throw new IllegalArgumentException(message);
      }
   }

  /**
  * DateOpened must be after 1970.
  * @throws IllegalArgumentException if field takes an unpermitted value.
  */
   private void validateDateOpened( Date aDateOpened ) {
     if( aDateOpened.getTime()<0 ) {
       throw new IllegalArgumentException("Date Opened must be after 1970.");
     }
   }

   /**
   * Always treat de-serialization as a full-blown constructor, by
   * validating the final state of the de-serialized object.
   */
   private void readObject(
     ObjectInputStream aInputStream
   ) throws ClassNotFoundException, IOException {
     //always perform the default de-serialization first
     aInputStream.defaultReadObject();

     //make defensive copy of the mutable Date field
     fDateOpened = new Date( fDateOpened.getTime() );

     //ensure that object state has not been corrupted or tampered with maliciously
     validateState();
  }

    /**
    * This is the default implementation of writeObject.
    * Customise if necessary.
    */
    private void writeObject(
      ObjectOutputStream aOutputStream
    ) throws IOException {
      //perform the default serialization for all non-transient, non-static fields
      aOutputStream.defaultWriteObject();
    }
} 
